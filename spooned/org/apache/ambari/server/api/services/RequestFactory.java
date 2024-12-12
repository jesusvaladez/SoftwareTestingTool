package org.apache.ambari.server.api.services;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.UriInfo;
public class RequestFactory {
    public org.apache.ambari.server.api.services.Request createRequest(javax.ws.rs.core.HttpHeaders headers, org.apache.ambari.server.api.services.RequestBody body, javax.ws.rs.core.UriInfo uriInfo, org.apache.ambari.server.api.services.Request.Type requestType, org.apache.ambari.server.api.resources.ResourceInstance resource) {
        switch (requestType) {
            case GET :
                return createGetRequest(headers, body, uriInfo, resource);
            case PUT :
                return createPutRequest(headers, body, uriInfo, resource);
            case DELETE :
                return createDeleteRequest(headers, body, uriInfo, resource);
            case POST :
                return createPostRequest(headers, body, uriInfo, resource);
            default :
                throw new java.lang.IllegalArgumentException("Invalid request type: " + requestType);
        }
    }

    private org.apache.ambari.server.api.services.Request createGetRequest(javax.ws.rs.core.HttpHeaders headers, org.apache.ambari.server.api.services.RequestBody body, javax.ws.rs.core.UriInfo uriInfo, org.apache.ambari.server.api.resources.ResourceInstance resource) {
        applyDirectives(org.apache.ambari.server.api.services.Request.Type.GET, body, uriInfo, resource);
        return new org.apache.ambari.server.api.services.GetRequest(headers, body, uriInfo, resource);
    }

    private org.apache.ambari.server.api.services.Request createPostRequest(javax.ws.rs.core.HttpHeaders headers, org.apache.ambari.server.api.services.RequestBody body, javax.ws.rs.core.UriInfo uriInfo, org.apache.ambari.server.api.resources.ResourceInstance resource) {
        boolean batchCreate = !applyDirectives(org.apache.ambari.server.api.services.Request.Type.POST, body, uriInfo, resource);
        return batchCreate ? new org.apache.ambari.server.api.services.QueryPostRequest(headers, body, uriInfo, resource) : new org.apache.ambari.server.api.services.PostRequest(headers, body, uriInfo, resource);
    }

    private org.apache.ambari.server.api.services.Request createPutRequest(javax.ws.rs.core.HttpHeaders headers, org.apache.ambari.server.api.services.RequestBody body, javax.ws.rs.core.UriInfo uriInfo, org.apache.ambari.server.api.resources.ResourceInstance resource) {
        applyDirectives(org.apache.ambari.server.api.services.Request.Type.PUT, body, uriInfo, resource);
        return new org.apache.ambari.server.api.services.PutRequest(headers, body, uriInfo, resource);
    }

    private org.apache.ambari.server.api.services.DeleteRequest createDeleteRequest(javax.ws.rs.core.HttpHeaders headers, org.apache.ambari.server.api.services.RequestBody body, javax.ws.rs.core.UriInfo uriInfo, org.apache.ambari.server.api.resources.ResourceInstance resource) {
        applyDirectives(org.apache.ambari.server.api.services.Request.Type.DELETE, body, uriInfo, resource);
        return new org.apache.ambari.server.api.services.DeleteRequest(headers, body, uriInfo, resource);
    }

    private java.util.Map<java.lang.String, java.lang.String> getQueryParameters(javax.ws.rs.core.UriInfo uriInfo, org.apache.ambari.server.api.services.RequestBody body) {
        java.util.Map<java.lang.String, java.lang.String> queryParameters = new java.util.HashMap<>();
        for (java.util.Map.Entry<java.lang.String, java.util.List<java.lang.String>> entry : uriInfo.getQueryParameters().entrySet()) {
            queryParameters.put(entry.getKey(), entry.getValue().get(0));
        }
        java.lang.String bodyQueryString = body.getQueryString();
        if ((bodyQueryString != null) && (!bodyQueryString.isEmpty())) {
            java.lang.String[] toks = bodyQueryString.split("&");
            for (java.lang.String tok : toks) {
                java.lang.String[] keyVal = tok.split("=");
                queryParameters.put(keyVal[0], keyVal.length == 2 ? keyVal[1] : "");
            }
        }
        return queryParameters;
    }

    private boolean applyDirectives(org.apache.ambari.server.api.services.Request.Type requestType, org.apache.ambari.server.api.services.RequestBody body, javax.ws.rs.core.UriInfo uriInfo, org.apache.ambari.server.api.resources.ResourceInstance resource) {
        java.util.Map<java.lang.String, java.lang.String> queryParameters = getQueryParameters(uriInfo, body);
        queryParameters.remove(org.apache.ambari.server.api.predicate.QueryLexer.QUERY_DOAS);
        java.util.Map<java.lang.String, java.lang.String> requestInfoProperties;
        boolean allDirectivesApplicable = true;
        if (!queryParameters.isEmpty()) {
            org.apache.ambari.server.api.resources.ResourceDefinition resourceDefinition = resource.getResourceDefinition();
            java.util.Collection<java.lang.String> directives;
            switch (requestType) {
                case PUT :
                    directives = resourceDefinition.getUpdateDirectives();
                    break;
                case POST :
                    directives = resourceDefinition.getCreateDirectives();
                    break;
                case GET :
                    directives = resourceDefinition.getReadDirectives();
                    break;
                case DELETE :
                    directives = resourceDefinition.getDeleteDirectives();
                    break;
                default :
                    return false;
            }
            requestInfoProperties = body.getRequestInfoProperties();
            for (java.util.Map.Entry<java.lang.String, java.lang.String> entry : queryParameters.entrySet()) {
                if (directives.contains(entry.getKey())) {
                    requestInfoProperties.put(entry.getKey(), entry.getValue());
                } else {
                    allDirectivesApplicable = false;
                }
            }
        }
        return allDirectivesApplicable;
    }
}