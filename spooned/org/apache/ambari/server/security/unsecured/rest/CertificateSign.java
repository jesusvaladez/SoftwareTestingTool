package org.apache.ambari.server.security.unsecured.rest;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
@javax.ws.rs.Path("/certs")
public class CertificateSign {
    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.security.unsecured.rest.CertificateSign.class);

    private static org.apache.ambari.server.security.CertificateManager certMan;

    @com.google.inject.Inject
    public static void init(org.apache.ambari.server.security.CertificateManager instance) {
        org.apache.ambari.server.security.unsecured.rest.CertificateSign.certMan = instance;
    }

    @javax.ws.rs.Path("{hostName}")
    @javax.ws.rs.POST
    @org.apache.ambari.annotations.ApiIgnore
    @javax.ws.rs.Consumes(javax.ws.rs.core.MediaType.APPLICATION_JSON)
    @javax.ws.rs.Produces({ javax.ws.rs.core.MediaType.APPLICATION_JSON, javax.ws.rs.core.MediaType.APPLICATION_XML })
    public org.apache.ambari.server.security.SignCertResponse signAgentCrt(@javax.ws.rs.PathParam("hostName")
    java.lang.String hostname, org.apache.ambari.server.security.SignMessage message, @javax.ws.rs.core.Context
    javax.servlet.http.HttpServletRequest req) {
        return org.apache.ambari.server.security.unsecured.rest.CertificateSign.certMan.signAgentCrt(hostname, message.getCsr(), message.getPassphrase());
    }
}