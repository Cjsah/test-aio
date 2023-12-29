from pathlib import Path
import os

dirs = os.listdir(Path())
for i in dirs:
    if i.startswith("①"):
        os.rename(i, '1 - ' + i[1:])
    if i.startswith("②"):
        os.rename(i, '2 - ' + i[1:])
    if i.startswith("③"):
        os.rename(i, '3 - ' + i[1:])
    if i.startswith("④"):
        os.rename(i, '4 - ' + i[1:])
    if i.startswith("⑤"):
        os.rename(i, '5 - ' + i[1:])
    if i.startswith("⑥"):
        os.rename(i, '6 - ' + i[1:])
    if i.startswith("⑦"):
        os.rename(i, '7 - ' + i[1:])
    if i.startswith("⑧"):
        os.rename(i, '8 - ' + i[1:])
    if i.startswith("⑨"):
        os.rename(i, '9 - ' + i[1:])
    if i.startswith("⑩"):
        os.rename(i, '10 - ' + i[1:])
    if i.startswith("⑪"):
        os.rename(i, '11 - ' + i[1:])
    if i.startswith("⑫"):
        os.rename(i, '12 - ' + i[1:])
    if i.startswith("⑬"):
        os.rename(i, '13 - ' + i[1:])
    if i.startswith("⑭"):
        os.rename(i, '14 - ' + i[1:])
    if i.startswith("⑮"):
        os.rename(i, '15 - ' + i[1:]
                  )

dirs = os.listdir(Path())
with open('dirlist.txt', 'w', encoding='utf-8')as f:
    for i in dirs:
        f.write(i + '\n')
