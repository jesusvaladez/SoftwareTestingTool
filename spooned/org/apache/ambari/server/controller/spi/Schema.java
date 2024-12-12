package org.apache.ambari.server.controller.spi;
public interface Schema {
    java.lang.String getKeyPropertyId(org.apache.ambari.server.controller.spi.Resource.Type type);

    java.util.Set<org.apache.ambari.server.controller.spi.Resource.Type> getKeyTypes();
}