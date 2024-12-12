package org.apache.ambari.server.api.rest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
@javax.ws.rs.Path("/bootstrap")
public class BootStrapResource {
    private static org.apache.ambari.server.bootstrap.BootStrapImpl bsImpl;

    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.api.rest.BootStrapResource.class);

    @com.google.inject.Inject
    public static void init(org.apache.ambari.server.bootstrap.BootStrapImpl instance) {
        org.apache.ambari.server.api.rest.BootStrapResource.bsImpl = instance;
    }

    @javax.ws.rs.POST
    @org.apache.ambari.annotations.ApiIgnore
    @javax.ws.rs.Consumes(javax.ws.rs.core.MediaType.APPLICATION_JSON)
    @javax.ws.rs.Produces({ javax.ws.rs.core.MediaType.APPLICATION_JSON, javax.ws.rs.core.MediaType.APPLICATION_XML })
    public org.apache.ambari.server.bootstrap.BSResponse bootStrap(org.apache.ambari.server.bootstrap.SshHostInfo sshInfo, @javax.ws.rs.core.Context
    javax.ws.rs.core.UriInfo uriInfo) {
        normalizeHosts(sshInfo);
        org.apache.ambari.server.bootstrap.BSResponse resp = org.apache.ambari.server.api.rest.BootStrapResource.bsImpl.runBootStrap(sshInfo);
        return resp;
    }

    @javax.ws.rs.GET
    @org.apache.ambari.annotations.ApiIgnore
    @javax.ws.rs.Path("/{requestId}")
    @javax.ws.rs.Produces({ javax.ws.rs.core.MediaType.APPLICATION_JSON, javax.ws.rs.core.MediaType.APPLICATION_XML })
    public org.apache.ambari.server.bootstrap.BootStrapStatus getBootStrapStatus(@javax.ws.rs.PathParam("requestId")
    long requestId, @javax.ws.rs.core.Context
    javax.ws.rs.core.UriInfo info) {
        org.apache.ambari.server.bootstrap.BootStrapStatus status = org.apache.ambari.server.api.rest.BootStrapResource.bsImpl.getStatus(requestId);
        if (status == null)
            throw new javax.ws.rs.WebApplicationException(Response.Status.NO_CONTENT);

        return status;
    }

    @javax.ws.rs.GET
    @org.apache.ambari.annotations.ApiIgnore
    @javax.ws.rs.Path("/hosts")
    @javax.ws.rs.Produces(javax.ws.rs.core.MediaType.APPLICATION_JSON)
    public java.util.List<org.apache.ambari.server.bootstrap.BSHostStatus> getBootStrapHosts(@javax.ws.rs.core.Context
    javax.ws.rs.core.UriInfo uriInfo) {
        java.util.List<org.apache.ambari.server.bootstrap.BSHostStatus> allStatus = org.apache.ambari.server.api.rest.BootStrapResource.bsImpl.getHostInfo(null);
        if (0 == allStatus.size())
            throw new javax.ws.rs.WebApplicationException(Response.Status.NO_CONTENT);

        return allStatus;
    }

    @javax.ws.rs.POST
    @org.apache.ambari.annotations.ApiIgnore
    @javax.ws.rs.Path("/hosts")
    @javax.ws.rs.Produces(javax.ws.rs.core.MediaType.APPLICATION_JSON)
    public java.util.List<org.apache.ambari.server.bootstrap.BSHostStatus> getBootStrapHosts(org.apache.ambari.server.bootstrap.SshHostInfo info, @javax.ws.rs.core.Context
    javax.ws.rs.core.UriInfo uriInfo) {
        java.util.List<org.apache.ambari.server.bootstrap.BSHostStatus> allStatus = org.apache.ambari.server.api.rest.BootStrapResource.bsImpl.getHostInfo(info.getHosts());
        if (0 == allStatus.size())
            throw new javax.ws.rs.WebApplicationException(Response.Status.NO_CONTENT);

        return allStatus;
    }

    private void normalizeHosts(org.apache.ambari.server.bootstrap.SshHostInfo info) {
        java.util.List<java.lang.String> validHosts = new java.util.ArrayList<>();
        java.util.List<java.lang.String> newHosts = new java.util.ArrayList<>();
        for (java.lang.String host : info.getHosts()) {
            try {
                java.net.InetAddress addr = java.net.InetAddress.getByName(host);
                if (!validHosts.contains(addr.getHostAddress())) {
                    validHosts.add(addr.getHostAddress());
                    newHosts.add(host);
                } else {
                    org.apache.ambari.server.api.rest.BootStrapResource.LOG.warn(("Host " + host) + " has already been targeted to be bootstrapped.");
                }
            } catch (java.net.UnknownHostException e) {
                org.apache.ambari.server.api.rest.BootStrapResource.LOG.warn(("Host " + host) + " cannot be determined.");
            }
        }
        info.setHosts(newHosts);
    }
}