package org.apache.ambari.server.api.resources;
public interface ResourceInstance {
    void setKeyValueMap(java.util.Map<org.apache.ambari.server.controller.spi.Resource.Type, java.lang.String> keyValueMap);

    java.util.Map<org.apache.ambari.server.controller.spi.Resource.Type, java.lang.String> getKeyValueMap();

    org.apache.ambari.server.api.query.Query getQuery();

    org.apache.ambari.server.api.resources.ResourceDefinition getResourceDefinition();

    java.util.Map<java.lang.String, org.apache.ambari.server.api.resources.ResourceInstance> getSubResources();

    boolean isCollectionResource();
}