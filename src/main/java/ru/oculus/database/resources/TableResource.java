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
import org.codehaus.jettison.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import com.sun.jersey.spi.resource.Singleton;

import ru.oculus.database.service.ResourceUtils;
import ru.oculus.database.service.sid.Sid;
import ru.oculus.database.service.sid.SidService;
import ru.oculus.database.service.table.TableInfo;
import ru.oculus.database.service.table.TableService;

@Path("/sid/{host}/{sid}/schema/{schema}/table")
@Singleton
@Produces(MediaType.APPLICATION_JSON)
public class TableResource {

	@Autowired
	private TableService tableService;
	@Autowired
	private SidService sidService;

	@GET
	public JSONArray getAll(@PathParam(value = "host") String host,
			@PathParam(value = "sid") String sid,
			@PathParam(value = "schema") String schema,
			@QueryParam("minSize") @DefaultValue("0") int minSize
			) throws Exception {
	    JSONArray result = new JSONArray();
		for (TableInfo walker : tableService.getAllTables(getSid(host, sid),
				schema,
				minSize)) {
			JSONObject object = new JSONObject();
			object.put("name", walker.getName());
			object.put("size", walker.getSize());
			result.put(object);
		}
        return result;
	}

	private Sid getSid(String host, String sidName) throws JAXBException,
			IOException {
		ResourceUtils.notNull(host, "Specify host");
		ResourceUtils.notNull(sidName, "Specify sid");
		Sid sid = sidService.getSid(host, sidName);
		ResourceUtils.notNull(sid, "Unkown sid " + sid + "@" + host);
		return sid;
	}
}
