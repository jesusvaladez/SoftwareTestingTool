package org.apache.ambari.server.resources.api.rest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
@javax.ws.rs.Path("/")
public class GetResource {
    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.resources.api.rest.GetResource.class);

    private static org.apache.ambari.server.resources.ResourceManager resourceManager;

    @com.google.inject.Inject
    public static void init(org.apache.ambari.server.resources.ResourceManager instance) {
        org.apache.ambari.server.resources.api.rest.GetResource.resourceManager = instance;
    }

    @javax.ws.rs.GET
    @org.apache.ambari.annotations.ApiIgnore
    @javax.ws.rs.Path("{resourcePath:.*}")
    @javax.ws.rs.Consumes(javax.ws.rs.core.MediaType.TEXT_PLAIN)
    @javax.ws.rs.Produces(javax.ws.rs.core.MediaType.APPLICATION_OCTET_STREAM)
    public javax.ws.rs.core.Response getResource(@javax.ws.rs.PathParam("resourcePath")
    java.lang.String resourcePath, @javax.ws.rs.core.Context
    javax.servlet.http.HttpServletRequest req) {
        if (org.apache.ambari.server.resources.api.rest.GetResource.LOG.isDebugEnabled()) {
            org.apache.ambari.server.resources.api.rest.GetResource.LOG.debug("Received a resource request from agent, resourcePath={}", resourcePath);
        }
        java.io.File resourceFile = org.apache.ambari.server.resources.api.rest.GetResource.resourceManager.getResource(resourcePath);
        if (!resourceFile.exists()) {
            return javax.ws.rs.core.Response.status(javax.servlet.http.HttpServletResponse.SC_NOT_FOUND).build();
        }
        return javax.ws.rs.core.Response.ok(resourceFile).build();
    }
}