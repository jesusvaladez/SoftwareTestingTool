package org.apache.ambari.server.api.services;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
@javax.ws.rs.Path("/stacks/{stackName}/versions/{stackVersion}/validations")
public class ValidationService extends org.apache.ambari.server.api.services.BaseService {
    @javax.ws.rs.POST
    @org.apache.ambari.annotations.ApiIgnore
    @javax.ws.rs.Produces(javax.ws.rs.core.MediaType.TEXT_PLAIN)
    public javax.ws.rs.core.Response getValidation(java.lang.String body, @javax.ws.rs.core.Context
    javax.ws.rs.core.HttpHeaders headers, @javax.ws.rs.core.Context
    javax.ws.rs.core.UriInfo ui, @javax.ws.rs.PathParam("stackName")
    java.lang.String stackName, @javax.ws.rs.PathParam("stackVersion")
    java.lang.String stackVersion) {
        return handleRequest(headers, body, ui, org.apache.ambari.server.api.services.Request.Type.POST, createValidationResource(stackName, stackVersion));
    }

    org.apache.ambari.server.api.resources.ResourceInstance createValidationResource(java.lang.String stackName, java.lang.String stackVersion) {
        java.util.Map<org.apache.ambari.server.controller.spi.Resource.Type, java.lang.String> mapIds = new java.util.HashMap<>();
        mapIds.put(org.apache.ambari.server.controller.spi.Resource.Type.Stack, stackName);
        mapIds.put(org.apache.ambari.server.controller.spi.Resource.Type.StackVersion, stackVersion);
        return createResource(org.apache.ambari.server.controller.spi.Resource.Type.Validation, mapIds);
    }
}