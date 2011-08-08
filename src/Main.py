'''
Created on 08.08.2011

@author: ar
'''

from ru.seb.oculus import Database
from ru.seb.oculus.Database import DatabaseInfo

if __name__ == '__main__':
    DatabaseInfo("mox04.sibirenergo-billing.ru", "qaasr", "SYSDBA", "123321123").gather()