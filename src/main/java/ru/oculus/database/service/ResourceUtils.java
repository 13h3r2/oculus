package ru.oculus.database.service;

import com.sun.jersey.api.NotFoundException;


public class ResourceUtils {
    public static void notNull(Object o) {
        if( o == null ) {
            throw new NotFoundException();
        }
    }
}
