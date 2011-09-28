package ru.oculus.database.resources;

import java.io.IOException;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.xml.bind.JAXBException;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import com.sun.jersey.spi.resource.Singleton;

import ru.oculus.database.service.sid.Sid;
import ru.oculus.database.service.sid.SidService;

@Path("/sid")
@Singleton
@Produces(MediaType.APPLICATION_JSON)
public class SidResource {

    @Autowired
    private SidService sidStorageProvider;

    @GET
    public JSONArray getSIDList() throws JSONException, JAXBException, IOException {
        JSONArray result = new JSONArray();
        for (Sid s : sidStorageProvider.getAllSids()) {
            JSONObject obj = new JSONObject();
            obj.put("host", s.getHost());
            obj.put("sid", s.getSid());
            result.put(obj);
        }
        return result;
    }

}
