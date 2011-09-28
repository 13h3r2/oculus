package ru.oculus.database.service.dump;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import ru.oculus.database.service.SSHCommand;
import ru.oculus.database.service.sid.Sid;

@XmlRootElement
public class DumpService {
    public List<Dump> getAll(Sid sid) throws Exception {
        ArrayList<Dump> result = new ArrayList<Dump>();
        for (String walker : new SSHCommand(sid, "cd " + sid.getDumpDir() + " && ls -1 *.dmp").run().split("\n")) {
            result.add(new Dump(walker));
        }
        return result;
    }
}
