'''
Created on Aug 11, 2011

@author: ar
'''

import pickle;
from oculus.Database import DatabaseStorage;

if __name__ == '__main__':
    f = open("databases.pickle", 'wb+');
    pickle.dump(DatabaseStorage().gather(), f);
    f.close();
    
#    f = open("databases.pickle", 'rb+');
#    print(pickle.load(f)[0].__dict__);