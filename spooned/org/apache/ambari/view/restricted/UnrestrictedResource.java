package org.apache.ambari.view.restricted;
import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
public class UnrestrictedResource {
    @javax.inject.Inject
    org.apache.ambari.view.ViewContext context;

    @javax.ws.rs.GET
    @javax.ws.rs.Produces({ "text/html" })
    public javax.ws.rs.core.Response getUnrestricted() throws java.io.IOException {
        return javax.ws.rs.core.Response.ok("<b>You have accessed an unrestricted resource.</b>").type("text/html").build();
    }
}