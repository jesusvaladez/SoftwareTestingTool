package org.apache.ambari.view.capacityscheduler;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
public class HelpService {
    private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.view.capacityscheduler.HelpService.class);

    private org.apache.ambari.view.ViewContext context;

    public HelpService(org.apache.ambari.view.ViewContext context) {
        this.context = context;
    }

    @javax.ws.rs.GET
    @javax.ws.rs.Path("/version")
    @javax.ws.rs.Produces(javax.ws.rs.core.MediaType.TEXT_PLAIN)
    public javax.ws.rs.core.Response version() {
        return javax.ws.rs.core.Response.ok("0.0.1-SNAPSHOT").build();
    }

    @javax.ws.rs.GET
    @javax.ws.rs.Path("/description")
    @javax.ws.rs.Produces(javax.ws.rs.core.MediaType.TEXT_PLAIN)
    public javax.ws.rs.core.Response description() {
        return javax.ws.rs.core.Response.ok("Application to manage YARN Capacity Scheduler").build();
    }
}