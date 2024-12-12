package org.apache.ambari.server.api.services;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.UriInfo;
public class QueryPostRequest extends org.apache.ambari.server.api.services.PostRequest {
    public QueryPostRequest(javax.ws.rs.core.HttpHeaders headers, org.apache.ambari.server.api.services.RequestBody body, javax.ws.rs.core.UriInfo uriInfo, org.apache.ambari.server.api.resources.ResourceInstance resource) {
        super(headers, body, uriInfo, resource);
    }

    @java.lang.Override
    public org.apache.ambari.server.api.services.Request.Type getRequestType() {
        return org.apache.ambari.server.api.services.Request.Type.QUERY_POST;
    }

    @java.lang.Override
    protected org.apache.ambari.server.api.handlers.RequestHandler getRequestHandler() {
        return new org.apache.ambari.server.api.handlers.QueryCreateHandler();
    }
}