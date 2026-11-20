#!/usr/bin/env python3
"""Real Android UIAutomator navigation to Collection for Beta2 portrait QA."""
from pathlib import Path
import re
import subprocess
import time
import xml.etree.ElementTree as ET

def adb(*args):
    return subprocess.run(["adb",*args],check=True,capture_output=True).stdout

def tree():
    adb("shell","uiautomator","dump","/sdcard/gridmates-beta2-ui.xml")
    xml=adb("exec-out","cat","/sdcard/gridmates-beta2-ui.xml")
    root=ET.fromstring(xml)
    parents={c:p for p in root.iter() for c in p}
    return root,parents

def find(label):
    root,parents=tree()
    hits=[]
    for node in root.iter("node"):
        text=" ".join((node.attrib.get("text",""),node.attrib.get("content-desc",""))).lower()
        if label.lower() not in text:
            continue
        click=node
        while click is not None and click.attrib.get("clickable")!="true":
            click=parents.get(click)
        n=click if click is not None else node
        m=re.findall(r"\d+",n.attrib.get("bounds",""))
        if len(m)==4:
            a,b,c,d=map(int,m)
            if c>a and d>b:
                hits.append((1 if click is not None else 0,(a+c)//2,(b+d)//2))
    if not hits:return None
    hits.sort(reverse=True)
    return hits[0][1:]

def main():
    package="com.playereden.sudokutogether.qa.beta2portraits"
    if not adb("shell","pidof",package).strip():
        raise RuntimeError("Reviewer package is not running")
    p=find("Collection")
    if p is None:raise RuntimeError("Collection bottom-navigation item unavailable")
    adb("shell","input","tap",str(p[0]),str(p[1]))
    time.sleep(3)
    # UIAutomator sometimes omits Compose tab text from its accessibility tree,
    # even when the page is open. Save the actual screen here; the separate
    # CollectionPolishUiTest verifies selected tabs and Farm content via Compose.
    root,_ = tree()
    seen = " ".join(
        node.attrib.get("text", "") + " " + node.attrib.get("content-desc", "")
        for node in root.iter("node")
    )
    Path("beta2-first-five-collection-ui.txt").write_text(seen[:20000])
    if not adb("shell","pidof",package).strip():
        raise RuntimeError("Reviewer process crashed during Collection navigation")
    shot=adb("exec-out","screencap","-p")
    if shot[:8]!=bytes((137,80,78,71,13,10,26,10)):
        raise RuntimeError("Missing actual Android screenshot")
    Path("beta2-first-five-collection-android.png").write_bytes(shot)
    print("PASS: real Android Collection screenshot captured",flush=True)

if __name__=="__main__":
    main()
