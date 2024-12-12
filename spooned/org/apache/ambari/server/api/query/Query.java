package org.apache.ambari.server.api.query;
public interface Query {
    void addProperty(java.lang.String propertyId, org.apache.ambari.server.controller.spi.TemporalInfo temporalInfo);

    void addLocalProperty(java.lang.String property);

    java.util.Set<java.lang.String> getProperties();

    org.apache.ambari.server.api.services.Result execute() throws org.apache.ambari.server.controller.spi.UnsupportedPropertyException, org.apache.ambari.server.controller.spi.SystemException, org.apache.ambari.server.controller.spi.NoSuchResourceException, org.apache.ambari.server.controller.spi.NoSuchParentResourceException;

    org.apache.ambari.server.controller.spi.Predicate getPredicate();

    void setUserPredicate(org.apache.ambari.server.controller.spi.Predicate predicate);

    void setPageRequest(org.apache.ambari.server.controller.spi.PageRequest pageRequest);

    void setSortRequest(org.apache.ambari.server.controller.spi.SortRequest sortRequest);

    void setRenderer(org.apache.ambari.server.api.query.render.Renderer renderer);

    void setRequestInfoProps(java.util.Map<java.lang.String, java.lang.String> requestInfoProperties);

    java.util.Map<java.lang.String, java.lang.String> getRequestInfoProps();
}