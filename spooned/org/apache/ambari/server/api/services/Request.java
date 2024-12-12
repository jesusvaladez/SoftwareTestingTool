package org.apache.ambari.server.api.services;
public interface Request {
    enum Type {

        GET,
        POST,
        PUT,
        DELETE,
        QUERY_POST;}

    org.apache.ambari.server.api.services.Result process();

    org.apache.ambari.server.api.resources.ResourceInstance getResource();

    java.lang.String getURI();

    org.apache.ambari.server.api.services.Request.Type getRequestType();

    int getAPIVersion();

    org.apache.ambari.server.controller.spi.Predicate getQueryPredicate();

    java.util.Map<java.lang.String, org.apache.ambari.server.controller.spi.TemporalInfo> getFields();

    org.apache.ambari.server.api.services.RequestBody getBody();

    java.util.Map<java.lang.String, java.util.List<java.lang.String>> getHttpHeaders();

    org.apache.ambari.server.controller.spi.PageRequest getPageRequest();

    org.apache.ambari.server.controller.spi.SortRequest getSortRequest();

    org.apache.ambari.server.api.query.render.Renderer getRenderer();

    java.lang.String getRemoteAddress();
}