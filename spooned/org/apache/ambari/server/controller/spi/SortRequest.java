package org.apache.ambari.server.controller.spi;
public interface SortRequest {
    java.util.List<org.apache.ambari.server.controller.spi.SortRequestProperty> getProperties();

    java.util.List<java.lang.String> getPropertyIds();

    enum Order {

        ASC,
        DESC;}
}