package org.apache.ambari.server.controller.spi;
public interface QueryResponse {
    java.util.Set<org.apache.ambari.server.controller.spi.Resource> getResources();

    boolean isSortedResponse();

    boolean isPagedResponse();

    int getTotalResourceCount();
}