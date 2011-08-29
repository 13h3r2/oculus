from http.server import BaseHTTPRequestHandler
from oculus.Database import DatabaseStorage, DatabaseInfo, SchemeInfo
import json
import logging
import os
import shutil

class DatabaseInfoHandler:
    def __init__(self):
        self.storage = DatabaseStorage()
    
    def handle(self, path):
        if path.startswith('/'):
            request = path.split("/")
            if len(request) == 3:
                dbInfo = self.storage.getDbInfo(request[1], request[2])
                if dbInfo: 
                    return dbInfo.gather()
                 
        return None

class APIEncoder(json.JSONEncoder):
    def default(self, obj):
        if isinstance(obj, DatabaseInfo):
            return obj.__dict__
        if isinstance(obj, SchemeInfo):
            return obj.__dict__
        raise TypeError

class DatabaseListHandler:
    def __init__(self):
        self.storage = DatabaseStorage()
    
    def handle(self, path):
        return self.storage.gather() 

class Handler(BaseHTTPRequestHandler):
    
    logger = logging.getLogger()
    handlers = {
        'database/info' : DatabaseInfoHandler(),
        'database/list' : DatabaseListHandler()
    }
    
    def __init__(self , request, client_address, server):
        BaseHTTPRequestHandler.__init__(self, request, client_address, server)
    
    def do_GET(self):
        self.logger.warn(self.path)
        
        if  self.path == '' or self.path == '/' :
            self.send_response(403)
            return
        
        t = ""
        t = self.path;
        
        result = None
        if t.startswith("/api/"):
            api_call = t[5:]
            self.logger.warn("api call " + api_call)
            for walker in self.handlers.keys():
                if(api_call.startswith(walker)):
                    result = self.handlers[walker].handle(api_call[len(walker):])
            
            
        if result: 
            result = json.dumps(result, indent=4, cls=APIEncoder)
            
            result = result.encode()
            self.send_response(200);
            self.send_header("Content-Length", str(len(result)))
            self.send_header("Content-Type", "application/json")
            self.end_headers()
            self.wfile.write(result)
            return
        
        try:
            f = open("../html" + self.path, 'rb')            
        except IOError:
            self.send_response(404)
            return None
            
        self.send_response(200)
        fs = os.fstat(f.fileno())
        self.send_header("Content-Length", str(fs[6]))
        self.send_header("Last-Modified", self.date_time_string(fs.st_mtime))
        self.end_headers()
        
        shutil.copyfileobj(f, self.wfile)
        f.close()
    