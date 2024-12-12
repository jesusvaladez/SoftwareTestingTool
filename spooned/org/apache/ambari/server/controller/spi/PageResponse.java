package org.apache.ambari.server.controller.spi;
public interface PageResponse {
    java.lang.Iterable<org.apache.ambari.server.controller.spi.Resource> getIterable();

    int getOffset();

    org.apache.ambari.server.controller.spi.Resource getPreviousResource();

    org.apache.ambari.server.controller.spi.Resource getNextResource();

    java.lang.Integer getTotalResourceCount();
}