'''
Created on Aug 11, 2011

@author: ar
'''

import pickle;
import os
from oculus.Database import DatabaseStorage;

if __name__ == '__main__':
    f = os.popen("ssh oracle@mox04.sibirenergo-billing.ru 'cd /DB/oradata/exp && ls -1 *.dmp'")
    for s in f.readlines():
        print(s.strip())
#    f = open("oculus/databases.pickle", 'rb+');
#    databases = pickle.load(f);
#    f.close()
#    print(databases)
#    
#    for w in databases:
#        w.dump_dir = '/DB/oradata/exp'
#    print(databases)
#    f = open("oculus/databases.pickle", 'wb+');
#    pickle.dump(databases, f)
#    f.close()
