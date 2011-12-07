package ru.oculus.database.resources;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.codehaus.jettison.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;

import ru.oculus.database.service.metadata.SchemaMetadataService;
import ru.oculus.database.service.sid.SidService;

import com.sun.jersey.spi.resource.Singleton;

@Path("/sid/{host}/{sid}/schema/{schema}/metadata")
@Singleton
@Produces(MediaType.APPLICATION_JSON)
public class SchemaMetadataResource {
    @Autowired
    private SchemaMetadataService metadataService;
    @Autowired
    private SidService sidService;

    @GET
    public JSONArray getAll(
            @PathParam(value = "host") String host,
            @PathParam(value = "sid") String sid,
            @PathParam(value = "schema") String schema
            ) throws Exception {
        return null;
    }

}
