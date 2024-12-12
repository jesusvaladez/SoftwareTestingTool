package org.apache.ambari.view.simple;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
public class SimpleService {
    private static final java.lang.String PROPERTY_VALUE = "what.is.the.value";

    @javax.inject.Inject
    org.apache.ambari.view.ViewContext context;

    @javax.ws.rs.GET
    @javax.ws.rs.Path("/")
    @javax.ws.rs.Produces({ "text/plain", "application/json" })
    public javax.ws.rs.core.Response getValue(@javax.ws.rs.core.Context
    javax.ws.rs.core.HttpHeaders headers, @javax.ws.rs.core.Context
    javax.ws.rs.core.UriInfo ui) {
        java.lang.String value = context.getProperties().get(org.apache.ambari.view.simple.SimpleService.PROPERTY_VALUE);
        java.lang.StringBuffer buf = new java.lang.StringBuffer();
        buf.append("{\"value\" : \"");
        buf.append(value);
        buf.append("\"}");
        return javax.ws.rs.core.Response.ok(buf.toString()).build();
    }
}