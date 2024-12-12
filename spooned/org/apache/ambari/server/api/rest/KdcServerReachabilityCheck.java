package org.apache.ambari.server.api.rest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;
@org.apache.ambari.server.StaticallyInject
@javax.ws.rs.Path("/kdc_check/")
public class KdcServerReachabilityCheck {
    private static final java.lang.String REACHABLE = "REACHABLE";

    private static final java.lang.String UNREACHABLE = "UNREACHABLE";

    @com.google.inject.Inject
    private static org.apache.ambari.server.KdcServerConnectionVerification kdcConnectionChecker;

    @javax.ws.rs.GET
    @org.apache.ambari.annotations.ApiIgnore
    @javax.ws.rs.Path("{hosts}")
    @javax.ws.rs.Produces(javax.ws.rs.core.MediaType.TEXT_PLAIN)
    public java.lang.String plainTextCheck(@javax.ws.rs.core.Context
    javax.ws.rs.core.HttpHeaders headers, @javax.ws.rs.core.Context
    javax.ws.rs.core.UriInfo ui, @javax.ws.rs.PathParam("hosts")
    java.lang.String hosts) {
        java.lang.String status = org.apache.ambari.server.api.rest.KdcServerReachabilityCheck.UNREACHABLE;
        if (hosts != null) {
            java.lang.String[] kdcHosts = hosts.split(",");
            for (java.lang.String kdcHost : kdcHosts) {
                kdcHost = kdcHost.trim();
                if (!kdcHost.isEmpty()) {
                    if (org.apache.ambari.server.api.rest.KdcServerReachabilityCheck.kdcConnectionChecker.isKdcReachable(kdcHost)) {
                        status = org.apache.ambari.server.api.rest.KdcServerReachabilityCheck.REACHABLE;
                        break;
                    }
                }
            }
        }
        return status;
    }

    @javax.ws.rs.GET
    @org.apache.ambari.annotations.ApiIgnore
    @javax.ws.rs.Path("{hostname}")
    @javax.ws.rs.Produces(javax.ws.rs.core.MediaType.TEXT_XML)
    public java.lang.String xmlCheck(@javax.ws.rs.core.Context
    javax.ws.rs.core.HttpHeaders headers, @javax.ws.rs.core.Context
    javax.ws.rs.core.UriInfo ui, @javax.ws.rs.PathParam("hostname")
    java.lang.String kdcServerHostName) {
        java.lang.String status = org.apache.ambari.server.api.rest.KdcServerReachabilityCheck.UNREACHABLE;
        if (org.apache.ambari.server.api.rest.KdcServerReachabilityCheck.kdcConnectionChecker.isKdcReachable(kdcServerHostName)) {
            status = org.apache.ambari.server.api.rest.KdcServerReachabilityCheck.REACHABLE;
        }
        return new java.lang.StringBuilder().append("<?xml version=\"1.0\"?>").append("<status>").append(status).append("</status>").toString();
    }

    @javax.ws.rs.GET
    @org.apache.ambari.annotations.ApiIgnore
    @javax.ws.rs.Path("{hostname}")
    @javax.ws.rs.Produces(javax.ws.rs.core.MediaType.TEXT_HTML)
    public java.lang.String htmlCheck(@javax.ws.rs.core.Context
    javax.ws.rs.core.HttpHeaders headers, @javax.ws.rs.core.Context
    javax.ws.rs.core.UriInfo ui, @javax.ws.rs.PathParam("hostname")
    java.lang.String kdcServerHostName) {
        java.lang.String status = org.apache.ambari.server.api.rest.KdcServerReachabilityCheck.UNREACHABLE;
        if (org.apache.ambari.server.api.rest.KdcServerReachabilityCheck.kdcConnectionChecker.isKdcReachable(kdcServerHostName)) {
            status = org.apache.ambari.server.api.rest.KdcServerReachabilityCheck.REACHABLE;
        }
        return new java.lang.StringBuilder().append("<html>\n").append("<title>").append("Status").append("</title>\n").append("<body><h1>").append(status).append("</body></h1>\n").append("</html> ").toString();
    }
}