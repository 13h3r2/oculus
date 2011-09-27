package ru.oculus.database.service;

import java.io.IOException;

import javax.xml.bind.JAXBException;

public class SidStorageProvider {
    private static SidStorage storage;

    public static SidStorage getStorage() throws JAXBException, IOException {
        if (storage == null) {
            storage = SidStorage.fromXml(SidStorageProvider.class.getClassLoader().getResourceAsStream("sids.xml"));
        }
        return storage;
    }
}
