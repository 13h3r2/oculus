package ru.oculus.database.resources;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class OculusExceptionMapper implements ExceptionMapper<Exception> {
    public Response toResponse(Exception ex) {
        return Response.status(500).entity(ex.getMessage()).type("text/plain").build();
    }
}