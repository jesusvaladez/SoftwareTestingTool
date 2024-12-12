package org.apache.ambari.server.controller.spi;
public interface PropertyProvider {
    java.util.Set<org.apache.ambari.server.controller.spi.Resource> populateResources(java.util.Set<org.apache.ambari.server.controller.spi.Resource> resources, org.apache.ambari.server.controller.spi.Request request, org.apache.ambari.server.controller.spi.Predicate predicate) throws org.apache.ambari.server.controller.spi.SystemException;

    java.util.Set<java.lang.String> checkPropertyIds(java.util.Set<java.lang.String> propertyIds);
}