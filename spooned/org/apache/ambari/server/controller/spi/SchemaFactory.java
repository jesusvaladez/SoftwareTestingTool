package org.apache.ambari.server.controller.spi;
public interface SchemaFactory {
    org.apache.ambari.server.controller.spi.Schema getSchema(org.apache.ambari.server.controller.spi.Resource.Type type);
}