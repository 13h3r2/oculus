import java.io.IOException;

import com.sun.grizzly.http.embed.GrizzlyWebServer;
import com.sun.grizzly.http.servlet.ServletAdapter;
import com.sun.grizzly.tcp.http11.GrizzlyAdapter;
import com.sun.grizzly.tcp.http11.GrizzlyRequest;
import com.sun.grizzly.tcp.http11.GrizzlyResponse;
import com.sun.jersey.spi.container.servlet.ServletContainer;

public class Main {

    public static void main(String[] args) throws IOException {
        GrizzlyWebServer ws = new GrizzlyWebServer(9998, "src/main/webapp");

        // Jersey web resources
        ServletAdapter jerseyAdapter = new ServletAdapter();
        jerseyAdapter.addInitParameter("com.sun.jersey.config.property.packages", "ru.oculus");
        jerseyAdapter.setContextPath("/api");
        jerseyAdapter.addContextParameter("com.sun.jersey.api.json.POJOMappingFeature", "true");
        jerseyAdapter.setServletInstance(new ServletContainer());

        ws.addGrizzlyAdapter(jerseyAdapter, new String[] { "/api" });
        GrizzlyAdapter staticAdapter = new GrizzlyAdapter("src/main/webapp") {
            @Override
            public void service(GrizzlyRequest request, GrizzlyResponse response) throws Exception {
                return;
            }
        };
        staticAdapter.setHandleStaticResources(true);
        ws.addGrizzlyAdapter(staticAdapter, new String[] { "/" });
        ws.start();

    }
}