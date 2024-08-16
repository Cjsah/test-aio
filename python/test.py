import json

with open('run/api.json', 'r') as f:
    ids = json.load(f)

print(len(ids))
ids = [qid['id'] for qid in ids]

start = 0
end = 0

lines = []

for qid in ids:
    if qid == end + 1:
        end = qid
        continue
    lines.append(f'{start}-{end}->{start==end}\n')
    start = qid
    end = qid

lines.append(f'{start}-{end}->{start==end}\n')

with open('result.txt', 'w') as f:
    f.writelines(lines)
