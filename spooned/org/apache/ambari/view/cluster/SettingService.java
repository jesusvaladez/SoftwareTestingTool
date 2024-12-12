package org.apache.ambari.view.cluster;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
public class SettingService extends org.apache.ambari.view.cluster.PropertyService {
    @javax.ws.rs.GET
    @javax.ws.rs.Path("/")
    @javax.ws.rs.Produces({ "text/plain", "application/json" })
    public javax.ws.rs.core.Response getValue(@javax.ws.rs.core.Context
    javax.ws.rs.core.HttpHeaders headers, @javax.ws.rs.core.Context
    javax.ws.rs.core.UriInfo ui) {
        return javax.ws.rs.core.Response.ok(getResponse("setting1", "setting2")).build();
    }
}