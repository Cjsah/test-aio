# -*- coding: utf-8 -*-
# by Cjsah

from http.server import BaseHTTPRequestHandler, HTTPServer


class MyHandler(BaseHTTPRequestHandler):
    def do_GET(self):
        self.send_response(200)
        self.send_header('Content-type', 'text/html')
        self.end_headers()
        self.wfile.write(b'Hello World!')
        return

    def do_POST(self):
        self.send_response(404)
        self.end_headers()


server = HTTPServer(('0.0.0.0', 8081), MyHandler)

server.serve_forever()
