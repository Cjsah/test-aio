# import base
import re

# with open('sql/lexicon_detail.sql', 'r', encoding='utf-8') as f:
#     lexicon = f.read()
#
# lexicon = lexicon.replace('INSERT INTO api.lexicon_detail (', 'INSERT INTO lexicon_detail (')
#
# with open('sql/lexicon_detail.sql', 'w+', encoding='utf-8') as f:
#     f.write(lexicon)

value = 'article.getLevel{index}Word(),\narticle.getLevel{index}WordNumber(),\n'

value = [value.format(index=i+1) for i in range(20)]
value = ''.join(value)
print(value)
