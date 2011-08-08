'''
Created on 08.08.2011

@author: ar
'''

import cx_Oracle


class DatabaseStorage:
    def __init__(self):
        self.databases = [
            DatabaseInfo("mox04.sibirenergo-billing.ru", "qaasr", "asd", "asd"),
            DatabaseInfo("mox04.sibirenergo-billing.ru", "test", "asd", "asd"),
            DatabaseInfo("mox05.sibirenergo-billing.ru", "prodasr", "asd", "asd"),
        ]
    
    def gather(self):
        return self.databases;

class DatabaseInfo(object):

    def __init__(self, host, sid, sys_login, sys_password):
        self.host = host
        self.sid = sid
        self.sys_login = sys_login
        self.sys_password = sys_password
        
    def gather(self):
        self.schemes = []
        connection = cx_Oracle.connect(cx_Oracle.makedsn(self.host, 1521, self.sid))
        cursor = connection.cursor()
        cursor.execute('''
            
        ''')
        cursor.close()
        connection.close()
        
        
class SchemeInfo:
    
    def __init__(self, name, size):
        self.name = name
        self.size = size
        
