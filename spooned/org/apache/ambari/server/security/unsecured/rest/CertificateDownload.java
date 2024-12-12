package org.apache.ambari.server.security.unsecured.rest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
@javax.ws.rs.Path("/cert/ca")
public class CertificateDownload {
    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.security.unsecured.rest.CertificateDownload.class);

    private static org.apache.ambari.server.security.CertificateManager certMan;

    @com.google.inject.Inject
    public static void init(org.apache.ambari.server.security.CertificateManager instance) {
        org.apache.ambari.server.security.unsecured.rest.CertificateDownload.certMan = instance;
    }

    @javax.ws.rs.GET
    @org.apache.ambari.annotations.ApiIgnore
    @javax.ws.rs.Produces({ javax.ws.rs.core.MediaType.TEXT_PLAIN })
    public java.lang.String downloadCACertificateChainFile() {
        return org.apache.ambari.server.security.unsecured.rest.CertificateDownload.certMan.getCACertificateChainContent();
    }
}