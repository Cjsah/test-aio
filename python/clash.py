# -*- coding: utf-8 -*-
# by Cjsah
import re

file = '1661417470636.yml'
version = '1.0'
pre = ['cjsah.net', 'csdn.net', 'hlju.edu.cn']
insert = []

firstChange = True

with open(file, 'r', encoding='utf-8')as f:
    origin = f.read()
with open('backup.yml', 'w+', encoding='utf-8')as f:
    f.write(origin)

heads = origin.split('\n', 2)
head = heads[0]
domains = heads[1]
if re.match('^# version [0-9.]+$', head):
    firstChange = False
    for domain in pre:
        if domain not in domains:
            insert.append(domain)

if firstChange:
    insert = pre

origin = origin.split('rules:\n')
if not firstChange:
    origin[0] = origin[0].split('\n', 2)[2]
origin[1] = origin[1].replace('Domestic', 'DIRECT').replace('AsianTV', 'DIRECT')

with open(file, 'w+', encoding='utf-8')as f:
    f.write('# version {}\n'.format(version))
    f.write('# {}\n'.format(' '.join(insert)))
    f.write(origin[0] + 'rules:\n' + origin[1])
    f.write('\n\n# New Add\n')
    for domain in insert:
        f.write('- DOMAIN-SUFFIX,' + domain + ',DIRECT\n')
