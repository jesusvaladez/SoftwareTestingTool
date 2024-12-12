package org.apache.ambari.server.api.rest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
@javax.ws.rs.Path("/check")
public class HealthCheck {
    private static final java.lang.String status = "RUNNING";

    @javax.ws.rs.GET
    @org.apache.ambari.annotations.ApiIgnore
    @javax.ws.rs.Produces(javax.ws.rs.core.MediaType.TEXT_PLAIN)
    public java.lang.String plainTextCheck() {
        return org.apache.ambari.server.api.rest.HealthCheck.status;
    }

    @javax.ws.rs.GET
    @org.apache.ambari.annotations.ApiIgnore
    @javax.ws.rs.Produces(javax.ws.rs.core.MediaType.TEXT_XML)
    public java.lang.String xmlCheck() {
        return (("<?xml version=\"1.0\"?>" + "<status> ") + org.apache.ambari.server.api.rest.HealthCheck.status) + "</status>";
    }

    @javax.ws.rs.GET
    @org.apache.ambari.annotations.ApiIgnore
    @javax.ws.rs.Produces(javax.ws.rs.core.MediaType.TEXT_HTML)
    public java.lang.String htmlCheck() {
        return ((("<html> " + ((("<title>" + "Status") + "</title>") + "<body><h1>")) + org.apache.ambari.server.api.rest.HealthCheck.status) + "</body></h1>") + "</html> ";
    }
}