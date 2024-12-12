package org.apache.ambari.view.property;
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
public class PropertyService {
    @javax.inject.Inject
    org.apache.ambari.view.ViewContext context;

    @javax.ws.rs.GET
    @javax.ws.rs.Path("/")
    @javax.ws.rs.Produces({ "text/plain", "application/json" })
    public javax.ws.rs.core.Response getValue(@javax.ws.rs.core.Context
    javax.ws.rs.core.HttpHeaders headers, @javax.ws.rs.core.Context
    javax.ws.rs.core.UriInfo ui) {
        java.util.Map props = context.getProperties();
        java.util.Iterator it = props.entrySet().iterator();
        java.lang.StringBuffer buf = new java.lang.StringBuffer();
        boolean first = true;
        buf.append("[");
        while (it.hasNext()) {
            java.util.Map.Entry pairs = ((java.util.Map.Entry) (it.next()));
            if (first == false)
                buf.append(",\n");

            buf.append("{\"");
            buf.append(pairs.getKey());
            buf.append("\" : \"");
            buf.append(pairs.getValue());
            buf.append("\"}");
            first = false;
        } 
        buf.append("]");
        return javax.ws.rs.core.Response.ok(buf.toString()).build();
    }
}