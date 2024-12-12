package org.apache.ambari.view;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
public interface ViewResourceHandler {
    public enum RequestType {

        GET,
        POST,
        PUT,
        DELETE,
        QUERY_POST;}

    public enum MediaType {

        TEXT_PLAIN,
        APPLICATION_JSON;}

    public javax.ws.rs.core.Response handleRequest(javax.ws.rs.core.HttpHeaders headers, javax.ws.rs.core.UriInfo ui, org.apache.ambari.view.ViewResourceHandler.RequestType requestType, org.apache.ambari.view.ViewResourceHandler.MediaType mediaType, java.lang.String resourceId);

    public javax.ws.rs.core.Response handleRequest(javax.ws.rs.core.HttpHeaders headers, javax.ws.rs.core.UriInfo ui, java.lang.String resourceId);
}