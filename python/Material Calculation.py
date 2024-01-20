# -*- coding: utf-8 -*-
# by Cjsah

from math import floor, ceil

need = {
    '反应堆': {
        '下界晶体块': 6,
        '绝缘外壳': 4,
        '能量单元': 2,
        '热力发电机': 3,
        '麦克斯韦熵门': 1,
        '熔炉发电机': 1,
    },
    '下界晶体块': {
        '下界水晶': 9,
    },
    '能量单元': {
        '下界水晶': 4,
        '下界电容': 4,
        '绝缘外壳': 1,
    },
    '热力发电机': {
        '下界水晶': 9,
        '热力板': 3,
        '下界电容': 4,
        '岩浆发电机': 1,
    },
    '熔炉发电机': {
        '下界电容': 2,
        '绝缘外壳': 1,
    },
    '岩浆发电机': {
        '下界电容': 4,
        '轮机转子': 1,
        '绝缘外壳': 1,
        '高压线圈': 1,
        '散热器模块': 1,
    },
    '下界电容': {
        '下界水晶': 4,
        '绝缘覆层': 4,
        '轮机转子': 1,
        '绝缘外壳': 1,
        '高压线圈': 1,
        '散热器模块': 1,
    },
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


add('反应堆', 1)

for i in mat:
    if i.startswith('熔融'):
        print('{}: {}mb {}组+{}个'.format(i, mat[i], floor(mat[i] / 144 / 64), ceil(mat[i] / 144 % 64)))
    else:
        print('{}: {}个 {}组+{}个'.format(i, ceil(mat[i]), floor(mat[i] / 64), ceil(mat[i] % 64)))
