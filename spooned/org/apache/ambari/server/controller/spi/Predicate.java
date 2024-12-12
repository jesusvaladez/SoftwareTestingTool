package org.apache.ambari.server.controller.spi;
public interface Predicate {
    boolean evaluate(org.apache.ambari.server.controller.spi.Resource resource);
}