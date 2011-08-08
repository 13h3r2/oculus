from http.server import BaseHTTPRequestHandler
import json
import logging
import socketserver
from ru.seb.oculus.Database import DatabaseStorage, DatabaseInfo



class DatabaseInfoHandler:
    def handle(self, path):
        return path

class APIEncoder(json.JSONEncoder):
    def default(self, obj):
        if isinstance(obj, DatabaseInfo):
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
        
        result = "xxx"
        if t.startswith("/api/"):
            api_call = t[5:]
            self.logger.warn("api call " + api_call)
            for walker in self.handlers.keys():
                if(api_call.startswith(walker)):
                    result = self.handlers[walker].handle(api_call[len(walker):])
            
            
        if result != "xxx":
            result = json.dumps(result, indent=4, cls=APIEncoder)
            
            result = result.encode()
            self.send_response(200);
            self.send_header("Content-Length", str(len(result)))
            self.send_header("Content-Type", "application/json")
            self.end_headers()
            self.wfile.write(result)
            return
        self.send_response(403)



if __name__ == '__main__':
    
    httpd = socketserver.TCPServer(("", 8000), Handler)
    httpd.serve_forever()

    
    
