package org.apache.ambari.view.restricted;
import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
public class RestrictedResource {
    @javax.inject.Inject
    org.apache.ambari.view.ViewContext context;

    @javax.ws.rs.GET
    @javax.ws.rs.Produces({ "text/html" })
    public javax.ws.rs.core.Response getRestricted() throws java.io.IOException {
        java.lang.String userName = context.getUsername();
        try {
            context.hasPermission(userName, "RESTRICTED");
        } catch (org.apache.ambari.view.SecurityException e) {
            return javax.ws.rs.core.Response.status(401).build();
        }
        return javax.ws.rs.core.Response.ok("<b>You have accessed a restricted resource.</b>").type("text/html").build();
    }
}