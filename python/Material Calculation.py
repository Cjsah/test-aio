# -*- coding: utf-8 -*-
# by Cjsah

from math import floor, ceil

need = {
    '激光': {
        '下界水晶': 2,
        '灌注合金': 2,
        '中级机器框架': 1,
        '高级输导元件': 2,
    },
    # '高级输导元件': {
    #     '钻石电容': 5,
    #     '盖亚魂锭': 2,
    #     '基础输导元件': 1,
    #     '高级能量立方': 1,
    # },
    # '基础输导元件': {
    #     '强化合金': 2,
    #     '烈焰电容': 3,
    #     '基础能量立方': 1,
    #     '高压线圈': 3,
    # },
    # '高级能量立方': {
    #     '钻石电容': 2,
    #     '基础能量立方': 1,
    #     '石墨电极': 2,
    #     '液态锂': 1,
    #     '灌注合金': 3,
    # },
    # '基础能量立方': {
    #     '烈焰电容': 2,
    #     '中级机器框架': 1,
    #     '石墨电极': 2,
    #     '液态锂': 1,
    #     '信素锭': 3,
    # },
    # '钻石电容': {
    #     '钻石水晶': 4,
    #     '绝缘覆层': 4,
    #     '基础电容': 1,
    # },
    # '烈焰电容': {
    #     '烈焰水晶': 4,
    #     '绝缘覆层': 4,
    #     '基础电容': 1,
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


add('激光', 4)

for i in mat:
    if i.startswith('熔融'):
        print('{}: {}mb {}组+{}个'.format(i, mat[i], floor(mat[i] / 144 / 64), ceil(mat[i] / 144 % 64)))
    else:
        print('{}: {}个 {}组+{}个'.format(i, ceil(mat[i]), floor(mat[i] / 64), ceil(mat[i] % 64)))
