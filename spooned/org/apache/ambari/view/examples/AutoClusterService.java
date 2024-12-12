package org.apache.ambari.view.examples;
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
public class AutoClusterService {
    @javax.inject.Inject
    org.apache.ambari.view.ViewContext context;

    @javax.ws.rs.GET
    @javax.ws.rs.Path("/")
    @javax.ws.rs.Produces({ "text/plain", "application/json" })
    public javax.ws.rs.core.Response getValue(@javax.ws.rs.core.Context
    javax.ws.rs.core.HttpHeaders headers, @javax.ws.rs.core.Context
    javax.ws.rs.core.UriInfo ui) {
        java.lang.StringBuilder sb = new java.lang.StringBuilder();
        try {
            java.io.InputStream in = null;
            org.apache.ambari.view.cluster.Cluster c = context.getCluster();
            if (c != null) {
                org.apache.ambari.view.AmbariStreamProvider stream = context.getAmbariStreamProvider();
                java.lang.String clusterName = c.getName();
                in = stream.readFrom("/api/v1/clusters/" + clusterName, "GET", null, null, true);
            } else {
                org.apache.ambari.view.URLStreamProvider stream = context.getURLStreamProvider();
                java.lang.String baseUrl = context.getProperties().get("ambari.server.url");
                java.lang.String username = context.getProperties().get("ambari.server.username");
                java.lang.String password = context.getProperties().get("ambari.server.password");
                java.util.HashMap<java.lang.String, java.lang.String> hds = new java.util.HashMap<java.lang.String, java.lang.String>();
                hds.put("X-Requested-By", "auto-cluster-view");
                java.lang.String authString = (username + ":") + password;
                byte[] authEncBytes = org.apache.commons.codec.binary.Base64.encodeBase64(authString.getBytes());
                java.lang.String authStringEnc = new java.lang.String(authEncBytes);
                hds.put("Authorization", "Basic " + authStringEnc);
                in = stream.readFrom(baseUrl, "GET", null, hds);
            }
            java.io.BufferedReader r = new java.io.BufferedReader(new java.io.InputStreamReader(in));
            java.lang.String str = null;
            while ((str = r.readLine()) != null) {
                sb.append(str);
            } 
        } catch (java.io.IOException ioe) {
            ioe.printStackTrace();
        }
        return javax.ws.rs.core.Response.ok(sb.toString()).build();
    }
}