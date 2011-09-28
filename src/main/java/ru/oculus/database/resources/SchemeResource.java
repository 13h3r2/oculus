package ru.oculus.database.resources;

import java.io.IOException;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.xml.bind.JAXBException;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import com.sun.jersey.spi.resource.Singleton;

import ru.oculus.database.service.ResourceUtils;
import ru.oculus.database.service.scheme.SchemeInfo;
import ru.oculus.database.service.scheme.SchemeService;
import ru.oculus.database.service.sid.Sid;
import ru.oculus.database.service.sid.SidService;

@Path("/sid/{host}/{sid}/schema")
@Singleton
@Produces(MediaType.APPLICATION_JSON)
public class SchemeResource {

    @Autowired
    private SchemeService schemeService;

    @Autowired
    private SidService sidService;

    @GET
    public JSONArray getAll(
            @PathParam(value = "host") String host,
            @PathParam(value = "sid") String sidName,
            @DefaultValue("0")  @QueryParam(value = "minSize") String minSizeGbString
            ) throws JAXBException, IOException, JSONException {
        ResourceUtils.notNull(host);
        ResourceUtils.notNull(sidName);

        Sid sid = sidService.getSid(host,  sidName);
        ResourceUtils.notNull(sid);

        double minSizeGb = Double.parseDouble(minSizeGbString);
        JSONArray result = new JSONArray();
        for (SchemeInfo walker : schemeService.getAll(sid)) {
            if (walker.getSize().doubleValue() > minSizeGb) {
                JSONObject obj = new JSONObject();
                obj.put("connectionCount", walker.getConnectionCount());
                obj.put("name", walker.getName());
                obj.put("size", walker.getSize());
                result.put(obj);
            }
        }

        return result;
    }

}