package ru.oculus.database.service.sid;

import java.io.IOException;
import java.util.List;

import javax.xml.bind.JAXBException;


public class SidService {
    private SidStorage storage;

    public List<Sid> getAllSids() throws JAXBException, IOException {
        if (storage == null) {
            storage = SidStorage.fromXml(SidService.class.getClassLoader().getResourceAsStream("sids.xml"));
        }
        return storage.getSids();
    }

    public Sid getSid(String host, String sid) throws JAXBException, IOException {
        for (Sid walker : getAllSids()) {
            if (walker.getHost().equals(host) && walker.getSid().equals(sid)) {
                return walker;
            }
        }
        return null;
    }
}
