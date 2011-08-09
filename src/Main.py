'''
Created on 08.08.2011

@author: ar
'''

from ru.seb.oculus import Database
from ru.seb.oculus.Database import DatabaseInfo
import socketserver
from ru.seb.oculus.Server import Handler

if __name__ == '__main__':
    print('go!')
    httpd = socketserver.TCPServer(("0.0.0.0", 8010), Handler)
    httpd.serve_forever()
