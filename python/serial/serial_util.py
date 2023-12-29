# -*- coding: utf-8 -*-
# by Cjsah
from serial import Serial
from logger import err

receiver = None

def send(ser: Serial):
    while True:
        val = input('').upper() + '\r'
        val = bytes(val, encoding='gbk')
        ser.write(val)

def receive(ser: Serial):
    global receiver
    while True:
        data = ser.read(1)
        if data == b'(':
            receiver = ''
        elif data == b'\r':
            print(receiver)
            receiver = None
        else:
            receiver += str(data, encoding='gbk')
