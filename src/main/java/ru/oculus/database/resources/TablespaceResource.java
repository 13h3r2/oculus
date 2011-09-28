package ru.oculus.database.resources;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.xml.bind.JAXBException;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import com.sun.jersey.spi.resource.Singleton;

import ru.oculus.database.service.ResourceUtils;
import ru.oculus.database.service.sid.Sid;
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
        ResourceUtils.notNull(host);
        ResourceUtils.notNull(sidName);

        Sid sid = sidService.getSid(host, sidName);
        ResourceUtils.notNull(sid);

        JSONArray result = new JSONArray();
        for (TablespaceInfo walker : tablespaceService.getAll(sid)) {
            JSONObject obj = new JSONObject();
            obj.put("totalSpace", walker.getTotalSpace());
            obj.put("freeSpace", walker.getFreeSpace());
            obj.put("used", new BigDecimal((walker.getTotalSpace().doubleValue() - walker.getFreeSpace().doubleValue()) / walker.getTotalSpace().doubleValue() * 100 ).setScale(2, RoundingMode.HALF_DOWN));
            obj.put("name", walker.getName());
            result.put(obj);
        }

        return result;
    }

}