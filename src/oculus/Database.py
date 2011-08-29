import time
import pickle
import logging
import cx_Oracle

class DatabaseStorage:
    def __init__(self):
#        self.databases = [
#            DatabaseInfo("mox04.sibirenergo-billing.ru", "qaasr", "SYS", "*"),
#            DatabaseInfo("mox05.sibirenergo-billing.ru", "prodasr", "SYS", "*"),
#        ]
        f = open("oculus/databases.pickle", 'rb+');
        self.databases = pickle.load(f);
        f.close();
        
    def gather(self):
        return self.databases;

    def getDbInfo(self, host, sid):
        for db in self.databases:
            if db.host == host and db.sid == sid:
                return db
        return None

class DatabaseInfo(object):

    def __init__(self, host, sid, sys_login, sys_password):
        self.host = host
        self.sid = sid
        self.sys_login = sys_login
        self.sys_password = sys_password
        
    def gather(self):
        time.sleep(1);
#        self.schemes = [
#            SchemeInfo('DEV_RENAT', '32GB'),
#            SchemeInfo('DEV_ROMANCHUK', '31GB'),
#            SchemeInfo('TRUNK_STABLE_QA', '30GB'),
#        ]
        connection = cx_Oracle.connect(self.sys_login, self.sys_password, cx_Oracle.makedsn(self.host, 1521, self.sid), cx_Oracle.SYSDBA)        
        cursor = connection.cursor()
        cursor.execute('''
select owner, ''||trunc(sum(bytes)/1024/1024/1024, 1), nvl(c_count, 0)
from dba_segments dbs
left outer join ( select count(*) as c_count, s.username as username from v$session s group by s.username) s on s.username = dbs.owner
group by owner, c_count
having sum(bytes) > 5000000000
order by sum(bytes) desc
        ''')
        self.schemes = []
        for record in cursor.fetchall():
            self.schemes.append(SchemeInfo(record[0], record[1], record[2]))
        cursor.close()
        connection.close()
        return self.schemes
        
        
class SchemeInfo:
    
    def __init__(self, name, size, connection_count):
        self.name = name
        self.size = size
        self.connection_count = connection_count
        
