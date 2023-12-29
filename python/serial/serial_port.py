# -*- coding: utf-8 -*-
# by Cjsah


import serial, serial.tools.list_ports
from logger import *
from serial_util import *
from threading import Thread


def connect():
    ports = serial.tools.list_ports.comports(include_links=False)
    ports = [port.name for port in ports]
    count = len(ports)

    if count == 0:
        log('没有串口可连接')
        exit(-1)
    elif count == 1:
        port = ports[0]
    else:
        print('已有串口:')
        for i, j in enumerate(ports):
            print('{} -> {}'.format(i, j))
        choose = int(input('请选择串口: '))
        port = ports[choose]

    ser = serial.Serial(port=port, baudrate=2400)

    threads = []

    if ser.isOpen():
        log('串口已打开')
        threads.append(Thread(target=lambda: send(ser), name='串口发送线程'))
        threads.append(Thread(target=lambda: receive(ser), name='串口接收线程'))
    else:
        err('串口打开失败')
        exit(-1)

    for thread in threads:
        thread.start()
