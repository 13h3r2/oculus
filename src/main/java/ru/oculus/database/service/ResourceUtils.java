package ru.oculus.database.service;

import com.sun.jersey.api.NotFoundException;


public class ResourceUtils {
    public static void notNull(Object o, String message) {
        if( o == null ) {
            throw new NotFoundException(message);
        }
    }
}
