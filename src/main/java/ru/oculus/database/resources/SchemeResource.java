package ru.oculus.database.resources;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;

import org.apache.commons.lang3.Validate;

@Path("/scheme")
public class SchemeResource {

    @GET
    @Path("/all")
    public String getAll(
            @QueryParam(value = "host") String host,
            @QueryParam(value = "sid") String sid
            ) {
        Validate.notNull(host);
        Validate.notNull(sid);
        return "" + host + sid;
    }

}
