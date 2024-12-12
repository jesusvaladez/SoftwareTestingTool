package org.apache.ambari.server.api.services;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
@javax.ws.rs.Path("/feeds/")
public class FeedService extends org.apache.ambari.server.api.services.BaseService {
    @javax.ws.rs.GET
    @org.apache.ambari.annotations.ApiIgnore
    @javax.ws.rs.Path("{feedName}")
    @javax.ws.rs.Produces("text/plain")
    public javax.ws.rs.core.Response getFeed(java.lang.String body, @javax.ws.rs.core.Context
    javax.ws.rs.core.HttpHeaders headers, @javax.ws.rs.core.Context
    javax.ws.rs.core.UriInfo ui, @javax.ws.rs.PathParam("feedName")
    java.lang.String feedName) {
        return handleRequest(headers, body, ui, org.apache.ambari.server.api.services.Request.Type.GET, createFeedResource(feedName));
    }

    @javax.ws.rs.GET
    @org.apache.ambari.annotations.ApiIgnore
    @javax.ws.rs.Produces("text/plain")
    public javax.ws.rs.core.Response getFeeds(java.lang.String body, @javax.ws.rs.core.Context
    javax.ws.rs.core.HttpHeaders headers, @javax.ws.rs.core.Context
    javax.ws.rs.core.UriInfo ui) {
        return handleRequest(headers, body, ui, org.apache.ambari.server.api.services.Request.Type.GET, createFeedResource(null));
    }

    @javax.ws.rs.POST
    @org.apache.ambari.annotations.ApiIgnore
    @javax.ws.rs.Path("{feedName}")
    @javax.ws.rs.Produces("text/plain")
    public javax.ws.rs.core.Response createFeed(java.lang.String body, @javax.ws.rs.core.Context
    javax.ws.rs.core.HttpHeaders headers, @javax.ws.rs.core.Context
    javax.ws.rs.core.UriInfo ui, @javax.ws.rs.PathParam("feedName")
    java.lang.String feedName) {
        return handleRequest(headers, body, ui, org.apache.ambari.server.api.services.Request.Type.POST, createFeedResource(feedName));
    }

    @javax.ws.rs.PUT
    @org.apache.ambari.annotations.ApiIgnore
    @javax.ws.rs.Path("{feedName}")
    @javax.ws.rs.Produces("text/plain")
    public javax.ws.rs.core.Response updateFeed(java.lang.String body, @javax.ws.rs.core.Context
    javax.ws.rs.core.HttpHeaders headers, @javax.ws.rs.core.Context
    javax.ws.rs.core.UriInfo ui, @javax.ws.rs.PathParam("feedName")
    java.lang.String feedName) {
        return handleRequest(headers, body, ui, org.apache.ambari.server.api.services.Request.Type.PUT, createFeedResource(feedName));
    }

    @javax.ws.rs.DELETE
    @org.apache.ambari.annotations.ApiIgnore
    @javax.ws.rs.Path("{feedName}")
    @javax.ws.rs.Produces("text/plain")
    public javax.ws.rs.core.Response deleteFeed(@javax.ws.rs.core.Context
    javax.ws.rs.core.HttpHeaders headers, @javax.ws.rs.core.Context
    javax.ws.rs.core.UriInfo ui, @javax.ws.rs.PathParam("feedName")
    java.lang.String feedName) {
        return handleRequest(headers, null, ui, org.apache.ambari.server.api.services.Request.Type.DELETE, createFeedResource(feedName));
    }

    @javax.ws.rs.Path("{feedName}/instances")
    public org.apache.ambari.server.api.services.InstanceService getHostHandler(@javax.ws.rs.PathParam("feedName")
    java.lang.String feedName) {
        return new org.apache.ambari.server.api.services.InstanceService(feedName);
    }

    org.apache.ambari.server.api.resources.ResourceInstance createFeedResource(java.lang.String feedName) {
        return createResource(org.apache.ambari.server.controller.spi.Resource.Type.DRFeed, java.util.Collections.singletonMap(org.apache.ambari.server.controller.spi.Resource.Type.DRFeed, feedName));
    }
}