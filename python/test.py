# import base

with open('lexicon.sql', 'r') as f:
    lexicon = f.read()

lexicon = lexicon.split("\n")

# node = lexicon[0]
#
# print(node)
#
# print(node[:12] + node[22:48] + node[52:1018] + '139' + node[1030:])

newList = []

lexicon = [node[:12] + node[22:48] + node[52:1018] + '139' + node[1030:] for node in lexicon]

lexicon = '\n'.join(lexicon)

with open('new.sql', 'w+', encoding='utf-8') as f:
    f.write(lexicon)
