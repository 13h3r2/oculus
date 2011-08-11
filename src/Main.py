'''
Created on 08.08.2011

@author: ar
'''

from oculus import Database
from oculus.Database import DatabaseInfo
from oculus.Server import Handler
import socketserver

if __name__ == '__main__':
    print('go!')
    httpd = socketserver.TCPServer(("0.0.0.0", 8010), Handler)
    httpd.serve_forever()
