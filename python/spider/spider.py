# -*- coding: utf-8 -*-
# by Cjsah
import requests
from bs4 import BeautifulSoup

session = requests.session()

res = session.get('https://wiki.biligame.com/persona/P5R%E9%81%93%E5%85%B7%E5%9B%BE%E9%89%B4').text

bs = BeautifulSoup(res, 'lxml')

trs = bs.find_all('tbody')[2].find_all('tr')[1:]

print(trs)