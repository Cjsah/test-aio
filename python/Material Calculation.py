# -*- coding: utf-8 -*-
# by Cjsah

from math import floor, ceil

need = {
    '涡轮排气口': {
        '涡轮外壳': 3,
        '流体管道': 1,
        '输液管止回阀': 0.5,
    },
    '分压元件': {
        '高级压力管道': 8,
        '钢质机壳': 1,
    },
    '饱和冷凝器': {
        '流体管道': 7,
        '钢质机壳': 1,
        '散热片': 1,
    },
    '电磁线圈': {
        '基础疏导元件': 4,
        '铜线圈': 4,
        '超级机器框架': 1,
    },
    '复杂旋钮装置': {
        '网络电缆': 10,
        '精英控制电路': 2,
        '强化合金': 2,
        '压缩铁齿轮': 8,
        '平板电脑': 1,
        '节点': 1,
        '高级机器框架': 1,
    },
    # '涡轮转子': {
    #     '钢锭': 6,
    #     '灌注合金': 3,
    # },
}

mat = {}


def add(name, count):
    if name in need:
        for n in need[name]:
            add(n, count * need[name][n])
    else:
        if name not in mat:
            mat[name] = 0
        mat[name] += count


# add('涡轮转子', 10)
add('涡轮外壳', 1017)
# add('涡轮排气口', 585)
# add('分压元件', 224)
# add('饱和冷凝器', 293)
# add('电磁线圈', 5)
# add('涡轮叶片', 20)
# add('复杂旋钮装置', 1)

for i in mat:
    if i.startswith('熔融'):
        print('{}: {}mb {}组+{}个'.format(i, mat[i], floor(mat[i] / 144 / 64), ceil(mat[i] / 144 % 64)))
    else:
        print('{}: {}个 {}组+{}个'.format(i, ceil(mat[i]), floor(mat[i] / 64), ceil(mat[i] % 64)))
