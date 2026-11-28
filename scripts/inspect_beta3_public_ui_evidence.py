#!/usr/bin/env python3
"""Summarize archived public rehearsal evidence without needing an emulator."""
import json
import re
import sys
import zipfile
import xml.etree.ElementTree as ET

def inspect(data):
    tree = ET.fromstring(data)
    parents = {c: p for p in tree.iter() for c in p}
    nodes = list(tree.iter("node"))
    board = {}
    for node in nodes:
        m = re.match(r"Row (\d+) column (\d+), (.*)", node.get("content-desc", ""))
        if m:
            key = f"{m[1]},{m[2]}"
            assert key not in board, "Duplicate board coordinate"
            board[key] = m[3]
    controls = []
    for node in nodes:
        if node.get("text") != "Continue":
            continue
        label = node
        while node.get("clickable") != "true" and node in parents:
            node = parents[node]
        controls.append({"label_bounds": label.get("bounds"), "control_bounds": node.get("bounds"), "enabled": node.get("enabled"), "clickable": node.get("clickable")})
    return {"board": board, "continue": controls, "progress_labels": [n.get("text") for n in nodes if " filled" in n.get("text", "")]}

def main(path):
    with zipfile.ZipFile(path) as z:
        assert z.testzip() is None
        evidence = {n.rsplit("/", 1)[-1][:-4]: inspect(z.read(n)) for n in z.namelist() if "/public-ui/" in "/" + n and n.endswith(".xml")}
    seed = evidence.get("beta2-board-seeded", {}).get("board", {})
    for name, item in sorted(evidence.items()):
        print(json.dumps({"snapshot": name, "cells": len(item["board"]), "matches_seed": len(seed) == 81 and item["board"] == seed, "continue": item["continue"], "progress_labels": item["progress_labels"]}))
    for name in ("beta3-board-resumed", "beta3-board-restarted"):
        item = evidence.get(name, {})
        ok = len(seed) == 81 and item.get("board") == seed
        print(f"{name}: {'PASS' if ok else 'NOT VERIFIED'}")
    # Diagnostic summary only; never replaces the runtime gate.
if __name__ == "__main__":
    main(sys.argv[1])
