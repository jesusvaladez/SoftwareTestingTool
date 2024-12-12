package org.apache.ambari.server.api.services;
import com.sun.jersey.core.util.MultivaluedMapImpl;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.PathSegment;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
public class LocalUriInfo implements javax.ws.rs.core.UriInfo {
    private final java.net.URI uri;

    public LocalUriInfo(java.lang.String uri) {
        try {
            this.uri = new java.net.URI(uri);
        } catch (java.net.URISyntaxException e) {
            throw new java.lang.RuntimeException("URI syntax is not correct", e);
        }
    }

    @java.lang.Override
    public java.net.URI getAbsolutePath() {
        throw new java.lang.UnsupportedOperationException("Method is not supported");
    }

    @java.lang.Override
    public javax.ws.rs.core.UriBuilder getAbsolutePathBuilder() {
        throw new java.lang.UnsupportedOperationException("Method is not supported");
    }

    @java.lang.Override
    public java.net.URI getBaseUri() {
        throw new java.lang.UnsupportedOperationException("Method is not supported");
    }

    @java.lang.Override
    public javax.ws.rs.core.UriBuilder getBaseUriBuilder() {
        throw new java.lang.UnsupportedOperationException("Method is not supported");
    }

    @java.lang.Override
    public java.util.List<java.lang.Object> getMatchedResources() {
        throw new java.lang.UnsupportedOperationException("Method is not supported");
    }

    @java.lang.Override
    public java.util.List<java.lang.String> getMatchedURIs() {
        throw new java.lang.UnsupportedOperationException("Method is not supported");
    }

    @java.lang.Override
    public java.util.List<java.lang.String> getMatchedURIs(boolean arg0) {
        throw new java.lang.UnsupportedOperationException("Method is not supported");
    }

    @java.lang.Override
    public java.lang.String getPath() {
        throw new java.lang.UnsupportedOperationException("Method is not supported");
    }

    @java.lang.Override
    public java.lang.String getPath(boolean arg0) {
        throw new java.lang.UnsupportedOperationException("Method is not supported");
    }

    @java.lang.Override
    public javax.ws.rs.core.MultivaluedMap<java.lang.String, java.lang.String> getPathParameters() {
        throw new java.lang.UnsupportedOperationException("Method is not supported");
    }

    @java.lang.Override
    public javax.ws.rs.core.MultivaluedMap<java.lang.String, java.lang.String> getPathParameters(boolean arg0) {
        throw new java.lang.UnsupportedOperationException("Method is not supported");
    }

    @java.lang.Override
    public java.util.List<javax.ws.rs.core.PathSegment> getPathSegments() {
        throw new java.lang.UnsupportedOperationException("Method is not supported");
    }

    @java.lang.Override
    public java.util.List<javax.ws.rs.core.PathSegment> getPathSegments(boolean arg0) {
        throw new java.lang.UnsupportedOperationException("Method is not supported");
    }

    @java.lang.Override
    public javax.ws.rs.core.MultivaluedMap<java.lang.String, java.lang.String> getQueryParameters() {
        java.util.List<org.apache.http.NameValuePair> parametersList = org.apache.http.client.utils.URLEncodedUtils.parse(uri, "UTF-8");
        javax.ws.rs.core.MultivaluedMap<java.lang.String, java.lang.String> parameters = new com.sun.jersey.core.util.MultivaluedMapImpl();
        for (org.apache.http.NameValuePair pair : parametersList) {
            parameters.add(pair.getName(), pair.getValue());
        }
        return parameters;
    }

    @java.lang.Override
    public javax.ws.rs.core.MultivaluedMap<java.lang.String, java.lang.String> getQueryParameters(boolean arg0) {
        throw new java.lang.UnsupportedOperationException("Method is not supported");
    }

    @java.lang.Override
    public java.net.URI getRequestUri() {
        return uri;
    }

    @java.lang.Override
    public javax.ws.rs.core.UriBuilder getRequestUriBuilder() {
        throw new java.lang.UnsupportedOperationException("Method is not supported");
    }
}