# -*- coding: utf-8 -*-
# by Cjsah

import cv2, json
import numpy as np

with open('dragon.json', 'r') as f:
    points = json.loads(f.read())

size = 256

img = np.zeros((size + 1, size, 3), dtype=np.uint8)

for row in range(size):
    for col in range(size):
        img[row + 1, col] = [255, 255, 255]

for point in points:
    img[point[0], point[1]] = [0, 0, 255]

cv2.imshow('img', img)
cv2.waitKey(0)
