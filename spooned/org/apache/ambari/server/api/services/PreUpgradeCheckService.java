package org.apache.ambari.server.api.services;
import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
public class PreUpgradeCheckService extends org.apache.ambari.server.api.services.BaseService {
    private java.lang.String clusterName = null;

    public PreUpgradeCheckService(java.lang.String clusterName) {
        this.clusterName = clusterName;
    }

    @javax.ws.rs.GET
    @org.apache.ambari.annotations.ApiIgnore
    @javax.ws.rs.Produces("text/plain")
    public javax.ws.rs.core.Response getPreUpgradeChecks(@javax.ws.rs.core.Context
    javax.ws.rs.core.HttpHeaders headers, @javax.ws.rs.core.Context
    javax.ws.rs.core.UriInfo ui) {
        return handleRequest(headers, null, ui, org.apache.ambari.server.api.services.Request.Type.GET, createResource());
    }

    private org.apache.ambari.server.api.resources.ResourceInstance createResource() {
        return createResource(org.apache.ambari.server.controller.spi.Resource.Type.PreUpgradeCheck, java.util.Collections.singletonMap(org.apache.ambari.server.controller.spi.Resource.Type.Cluster, clusterName));
    }
}