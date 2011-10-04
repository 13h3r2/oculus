import java.io.IOException;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import com.sun.grizzly.http.embed.GrizzlyWebServer;
import com.sun.grizzly.http.servlet.ServletAdapter;
import com.sun.grizzly.tcp.http11.GrizzlyAdapter;
import com.sun.grizzly.tcp.http11.GrizzlyRequest;
import com.sun.grizzly.tcp.http11.GrizzlyResponse;
import com.sun.jersey.spi.spring.container.servlet.SpringServlet;

public class Main {

    public static void main(String[] args) throws IOException {

        BasicConfigurator.configure();
        Logger.getLogger("ru.oculus").setLevel(Level.INFO);
        Logger.getRootLogger().setLevel(Level.WARN);

        GrizzlyWebServer ws = new GrizzlyWebServer(9998, "src/main/webapp");

        // Jersey web resources
        ServletAdapter jerseyAdapter = new ServletAdapter();
        jerseyAdapter.setContextPath("/api");
        jerseyAdapter.addInitParameter("com.sun.jersey.config.property.packages", "ru.oculus");
        jerseyAdapter.addContextParameter("com.sun.jersey.api.json.POJOMappingFeature", "true");
        jerseyAdapter.addContextParameter("contextConfigLocation", "classpath:spring-config.xml");
        jerseyAdapter.addServletListener("org.springframework.web.context.ContextLoaderListener");
        jerseyAdapter.setServletInstance(new SpringServlet());

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