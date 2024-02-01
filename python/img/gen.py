# -*- coding: utf-8 -*-
# by Cjsah

import cv2


def hasPixel(pixel: tuple) -> bool:
    return pixel[3] == 255


image = cv2.imread('loong_border.png', cv2.IMREAD_UNCHANGED)
pixels = image.tolist()

width, height, _ = image.shape

pos = []

for row in range(height):
    for col in range(width):
        pixel = pixels[row][col]
        if hasPixel(pixel):
            pos.append(f'[{row},{col}]')

with open('dragon.json', 'w+') as f:
    f.write(f"[{','.join(pos)}]")

