package org.apache.ambari.server.security.unsecured.rest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
@javax.ws.rs.Path("/connection_info")
public class ConnectionInfo {
    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.security.unsecured.rest.ConnectionInfo.class);

    private static java.util.HashMap<java.lang.String, java.lang.String> response = new java.util.HashMap<>();

    private static org.apache.ambari.server.configuration.Configuration conf;

    @com.google.inject.Inject
    public static void init(org.apache.ambari.server.configuration.Configuration instance) {
        org.apache.ambari.server.security.unsecured.rest.ConnectionInfo.conf = instance;
        org.apache.ambari.server.security.unsecured.rest.ConnectionInfo.response.put(org.apache.ambari.server.configuration.Configuration.SRVR_TWO_WAY_SSL.getKey(), java.lang.String.valueOf(org.apache.ambari.server.security.unsecured.rest.ConnectionInfo.conf.isTwoWaySsl()));
    }

    @javax.ws.rs.GET
    @org.apache.ambari.annotations.ApiIgnore
    @javax.ws.rs.Produces({ javax.ws.rs.core.MediaType.APPLICATION_JSON })
    public java.util.Map<java.lang.String, java.lang.String> connectionType() {
        return org.apache.ambari.server.security.unsecured.rest.ConnectionInfo.response;
    }
}