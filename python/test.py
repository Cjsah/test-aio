import json

with open('run/area.json', 'r', encoding='utf-8') as f:
    value: dict = json.load(f)


with open('run/result.json', 'w+', encoding='utf-8') as f:
    # f.write(json.dumps(value, ensure_ascii=False, separators=(',', ':')))
    f.write(json.dumps(value, ensure_ascii=False, indent=4))
