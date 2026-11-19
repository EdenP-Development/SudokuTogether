#!/usr/bin/env python3
"""Protect the proposed GridMates Beta 2 expansion from duplicate IDs and
accidental regression against the existing source catalog.

This is a DESIGN CONTRACT test. It does not make proposed cosmetics playable.
Run: python3 -m unittest scripts.tests.test_beta2_content_contract -v
"""
import json
import re
import unittest
from pathlib import Path

ROOT = Path(__file__).resolve().parents[2]
CATALOG = ROOT / "core/src/main/kotlin/com/playereden/sudokutogether/core/Cosmetics.kt"
MANIFEST = ROOT / "design/beta2-content-pack/expansion-candidates.json"


def named_list(source, identifier):
    match = re.search(
        rf"private val {identifier} = listOf\((.*?)\n    \)",
        source, re.S
    )
    if not match:
        raise AssertionError(f"Missing catalog block: {identifier}")
    return re.findall(r'CatalogEntry\("([^"]+)"', match.group(1))


def current_catalog_ids(source):
    woodland_block = re.search(
        r"private val woodlandEntries = linkedMapOf\((.*?)\n    \)\.map",
        source, re.S
    )
    if not woodland_block:
        raise AssertionError("Missing legacy Woodland catalog")
    woodland = [
        "woodland_" + animal
        for animal in re.findall(r'"([^"]+)"\s+to\s+\d+', woodland_block.group(1))
    ]
    groups = {
        "woodland": woodland,
        "ocean": named_list(source, "oceanEntries"),
        "garden": named_list(source, "gardenEntries"),
        "inks": named_list(source, "inkEntries"),
        "boards": named_list(source, "boardEntries"),
        "fonts": named_list(source, "fontEntries"),
        "emblems": named_list(source, "emblemEntries"),
    }
    return groups


class Beta2ContentContractTest(unittest.TestCase):
    @classmethod
    def setUpClass(cls):
        cls.source = CATALOG.read_text(encoding="utf-8")
        cls.manifest = json.loads(MANIFEST.read_text(encoding="utf-8"))
        cls.items = cls.manifest["items"]
        cls.current = current_catalog_ids(cls.source)

    def test_source_baseline_still_matches_real_50_cosmetics(self):
        c = self.current
        observed = {
            "portraits": sum(len(c[k]) for k in ("woodland", "ocean", "garden")),
            "emblems": len(c["emblems"]),
            "boards": len(c["boards"]),
            "number_inks": len(c["inks"]),
            "number_fonts": len(c["fonts"]),
        }
        self.assertEqual(self.manifest["source_baseline"]["existing_counts"]["total"],
                         sum(observed.values()))
        for key, actual in observed.items():
            self.assertEqual(self.manifest["source_baseline"]["existing_counts"][key],
                             actual, f"Current catalog changed: {key}")

    def test_new_ids_are_unique_and_do_not_duplicate_existing_catalog(self):
        current_ids = set().union(*(set(v) for v in self.current.values()))
        ids = [x["id"] for x in self.items]
        self.assertEqual(len(ids), len(set(ids)))
        self.assertFalse(current_ids.intersection(ids),
                         f"Already registered cosmetic IDs: {current_ids.intersection(ids)}")
        self.assertEqual(29, len(ids))

    def test_roster_and_progress_distribution_are_reviewable(self):
        family_counts = {
            name: sum(item.get("family") == name for item in self.items)
            for name in ("Woodland", "Ocean", "Garden", "Farm")
        }
        self.assertEqual({"Woodland": 5, "Ocean": 5, "Garden": 5, "Farm": 6},
                         family_counts)
        self.assertEqual(21, sum(x["category"] == "portrait" for x in self.items))
        self.assertEqual(4, sum(x["category"] == "board" for x in self.items))
        self.assertEqual(4, sum(x["category"] == "font" for x in self.items))
        starters = [x["id"] for x in self.items if x["unlock"]["kind"] == "starter"]
        self.assertEqual(["farm_cow"], starters)
        for item in self.items:
            target = item["unlock"]["target"]
            self.assertIsInstance(target, int)
            self.assertGreaterEqual(target, 0)
            self.assertLessEqual(target, 100)
            self.assertEqual("starter" if target == 0 else "completed_puzzles",
                             item["unlock"]["kind"])
            self.assertEqual("proposed_not_shipped", item["state"])
            self.assertTrue(item["required_art"])

    def test_existing_owned_cosmetic_mechanism_is_not_replaced(self):
        # Constraints must stay visible to implementers and in core source.
        self.assertIn("ownedCosmeticIds", self.source)
        self.assertIn("fun preserveEarned(", self.source)
        self.assertIn("distinctBy { it.id }", self.source)
        self.assertIn("original production certificate",
                      " ".join(self.manifest["release_gates"]))

    def test_projected_inventory_math(self):
        expected = self.manifest["proposed_additions"]
        self.assertEqual(21, expected["portraits"])
        self.assertEqual(4, expected["boards"])
        self.assertEqual(4, expected["number_fonts"])
        self.assertEqual(0, expected["inks"])
        self.assertEqual(79, expected["projected_total"])
        self.assertEqual(50 + len(self.items), expected["projected_total"])


if __name__ == "__main__":
    unittest.main()
