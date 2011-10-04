package ru.oculus.database.resources;

import java.io.IOException;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.xml.bind.JAXBException;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import com.sun.jersey.api.NotFoundException;
import com.sun.jersey.spi.resource.Singleton;

import ru.oculus.database.service.ResourceUtils;
import ru.oculus.database.service.dump.Dump;
import ru.oculus.database.service.dump.DumpService;
import ru.oculus.database.service.sid.Sid;
import ru.oculus.database.service.sid.SidService;

@Path("/sid/{host}/{sid}/dump")
@Singleton
@Produces(MediaType.APPLICATION_JSON)
public class DumpResource {

    @Autowired
    private DumpService dumpService;

    @Autowired
    private SidService sidService;

    @GET
    public JSONArray getAll(
            @PathParam("host") String host,
            @PathParam("sid") String sidName
            ) throws Exception {
        Sid sid = getSid(host, sidName);

        JSONArray result = new JSONArray();
        for (Dump dump: dumpService.getAll(sid)) {
            JSONObject item = new JSONObject();
            item.put("name", dump.getName());
            result.put(item);
        }

        return result;
    }

    @GET
    @Path("{dumpName}")
    public Object dumpAction(
            @PathParam("host") String host,
            @PathParam("sid") String sidName,
            @PathParam(value = "dumpName") String dumpName,
            @QueryParam("action") String action,
            @QueryParam("schema") String schema,
            @QueryParam("remapFrom") String remapFrom
            ) throws Exception {
        Sid sid = getSid(host, sidName);
        ResourceUtils.notNull(schema, "Specify schema name to create");
        ResourceUtils.notNull(remapFrom, "Specify schema name to remap from");

        if (action.equals("install")) {
            dumpService.installDumpAsync(sid, dumpName, schema, remapFrom);
            return OperationResult.OK;
        }

        throw new NotFoundException();

    }

    private Sid getSid(String host, String sidName) throws JAXBException, IOException {
        ResourceUtils.notNull(host, "Specify host");
        ResourceUtils.notNull(sidName, "Specify sid");
        Sid sid = sidService.getSid(host,  sidName);
        ResourceUtils.notNull(sid, "Unkown sid " + sid + "@" + host);
        return sid;
    }

}
