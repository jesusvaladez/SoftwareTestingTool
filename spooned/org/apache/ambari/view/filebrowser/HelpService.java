package org.apache.ambari.view.filebrowser;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.json.simple.JSONObject;
public class HelpService extends org.apache.ambari.view.commons.hdfs.HdfsService {
    public HelpService(org.apache.ambari.view.ViewContext context) {
        super(context);
    }

    public HelpService(org.apache.ambari.view.ViewContext context, java.util.Map<java.lang.String, java.lang.String> viewConfigs) {
        super(context, viewConfigs);
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
        return javax.ws.rs.core.Response.ok("Application to work with HDFS").build();
    }

    @javax.ws.rs.GET
    @javax.ws.rs.Path("/filesystem")
    @javax.ws.rs.Produces(javax.ws.rs.core.MediaType.TEXT_PLAIN)
    public javax.ws.rs.core.Response filesystem() {
        return javax.ws.rs.core.Response.ok(context.getProperties().get("webhdfs.url")).build();
    }

    @javax.ws.rs.GET
    @javax.ws.rs.Path("/hdfsStatus")
    @javax.ws.rs.Produces(javax.ws.rs.core.MediaType.APPLICATION_JSON)
    public javax.ws.rs.core.Response hdfsStatus() {
        org.apache.ambari.view.commons.hdfs.HdfsService.hdfsSmokeTest(context);
        return getOKResponse();
    }

    private javax.ws.rs.core.Response getOKResponse() {
        org.json.simple.JSONObject response = new org.json.simple.JSONObject();
        response.put("message", "OK");
        response.put("trace", null);
        response.put("status", "200");
        return javax.ws.rs.core.Response.ok().entity(response).type(MediaType.APPLICATION_JSON).build();
    }
}