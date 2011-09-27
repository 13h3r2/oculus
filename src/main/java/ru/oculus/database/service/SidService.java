package ru.oculus.database.service;

import java.io.IOException;

import javax.xml.bind.JAXBException;

public class SidService {
    private SidStorage storage;

    public SidStorage getStorage() throws JAXBException, IOException {
        if (storage == null) {
            storage = SidStorage.fromXml(SidService.class.getClassLoader().getResourceAsStream("sids.xml"));
        }
        return storage;
    }


}
