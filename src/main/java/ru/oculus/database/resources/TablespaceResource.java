package ru.oculus.database.resources;

import java.io.IOException;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.xml.bind.JAXBException;

import org.apache.commons.lang3.Validate;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import com.sun.jersey.spi.resource.Singleton;

import ru.oculus.database.model.Sid;
import ru.oculus.database.service.sid.SidService;
import ru.oculus.database.service.tablespace.TablespaceInfo;
import ru.oculus.database.service.tablespace.TablespaceService;

@Path("/sid/{host}/{sid}/tablespace")
@Singleton
@Produces(MediaType.APPLICATION_JSON)
public class TablespaceResource {

    @Autowired
    private TablespaceService tablespaceService;

    @Autowired
    private SidService sidService;

    @GET
    public JSONArray getAll(
            @PathParam("host") String host,
            @PathParam("sid") String sidName
            ) throws JAXBException, IOException, JSONException {
        Validate.notNull(host);
        Validate.notNull(sidName);

        Sid sid = sidService.getSid(host, sidName);
        Validate.notNull(sid);

        JSONArray result = new JSONArray();
        for (TablespaceInfo walker : tablespaceService.getAll(sid)) {
            JSONObject obj = new JSONObject();
            obj.put("totalSpace", walker.getTotalSpace());
            obj.put("freeSpace", walker.getFreeSpace());
            obj.put("name", walker.getName());
            result.put(obj);
        }

        return result;
    }

}
