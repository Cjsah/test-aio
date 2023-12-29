# -*- coding: utf-8 -*-
# by Cjsah

from math import floor, ceil

need = {
    '64k': {
        '萤石粉': 4,
        '运算处理器': 1,
        '石英玻璃': 1,
        '16k': 3
    },
    # '16k': {
    #     '萤石粉': 4,
    #     '运算处理器': 1,
    #     '石英玻璃': 1,
    #     '4k': 3
    # },
    # '4k': {
    #     '红石粉': 4,
    #     '运算处理器': 1,
    #     '石英玻璃': 1,
    #     '1k': 3
    # },
    # '1k': {
    #     '运算构件': 4,
    #     '赛特斯石英水晶': 4,
    #     '红石粉': 1
    # },
    # '运算构件': {
    #     '硅': 2,
    #     '感应构件': 1
    # },
    '感应构件': {
        '辐射感应线圈': 2,
        '精密构件': 1
    },
    '精密构件': {
        '电子管': 2,
        '动力构件': 1
    },
    '动力构件': {
        '安山合金': 2,
        '橡木台阶': 1
    },
    '电子管': {
        '玫瑰石英': 1,
        '熔融铁': 16
    },
    '玫瑰石英': {
        '石英': 1,
        '红石粉': 4
    },
    # '石英玻璃': {
    #     '石英': 1
    # },
    # '运算处理器': {
    #     '硅': 1,
    #     '铜': 1,
    #     '红石粉': 2.5
    # }
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


add('64k', 159)

for i in mat:
    if i.startswith('熔融'):
        print('{}: {}mb {}组+{}个'.format(i, mat[i], floor(mat[i] / 144 / 64), ceil(mat[i] / 144 % 64)))
    else:
        print('{}: {}个 {}组+{}个'.format(i, ceil(mat[i]), floor(mat[i] / 64), ceil(mat[i] % 64)))
