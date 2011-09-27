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

import ru.oculus.database.model.Sid;
import ru.oculus.database.service.SidStorageProvider;

@Path("/database")
@Produces(MediaType.APPLICATION_JSON)
public class SidResource {

    @Autowired
    private SidStorageProvider sidStorageProvider;

    @GET
    public JSONArray getSIDList() throws JSONException, JAXBException, IOException {
        JSONArray result = new JSONArray();
        for( Sid s : sidStorageProvider.getStorage().getSids() ) {
            JSONObject obj = new JSONObject();
            obj.put("host", s.getHost());
            obj.put("sid", s.getSid());
            result.put(obj);
        }
        return result;
    }



}
