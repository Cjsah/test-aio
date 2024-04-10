# import base
import re, json

# with open('sql/lexicon_detail.sql', 'r', encoding='utf-8') as f:
#     lexicon = f.read()
#
# lexicon = lexicon.replace('INSERT INTO api.lexicon_detail (', 'INSERT INTO lexicon_detail (')
#
# with open('sql/lexicon_detail.sql', 'w+', encoding='utf-8') as f:
#     f.write(lexicon)


with open('test.txt', 'r', encoding='utf-8') as f:
    lines = f.read().splitlines()

print(lines)

words = []
for i, line in enumerate(lines):
    if i % 4 == 0:
        words.append([line, lines[i+1], lines[i+2], lines[i+3]])

print(json.dumps(words, ensure_ascii=False))
