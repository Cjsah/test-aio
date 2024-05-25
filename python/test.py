# import base
import re, json, requests

# with open('sql/lexicon_detail.sql', 'r', encoding='utf-8') as f:
#     lexicon = f.read()
#
# lexicon = lexicon.replace('INSERT INTO api.lexicon_detail (', 'INSERT INTO lexicon_detail (')
#
# with open('sql/lexicon_detail.sql', 'w+', encoding='utf-8') as f:
#     f.write(lexicon)


# with open('test.txt', 'r', encoding='utf-8') as f:
#     lines = f.read().splitlines()
#
# print(lines)
#
# words = []
# for i, line in enumerate(lines):
#     if i % 4 == 0:
#         words.append([line, lines[i+1], lines[i+2], lines[i+3]])
#
# print(json.dumps(words, ensure_ascii=False))

session = requests.session()

header = {
    'Accept': 'text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.7',
    'Accept-Encoding': 'gzip, deflate, br',
    'Accept-Language': 'zh-CN,zh;q=0.9',
}

session.headers.update(header)

res = session.get('https://www.51test.net/show/10848779.html', verify=False)

print(res.text)

