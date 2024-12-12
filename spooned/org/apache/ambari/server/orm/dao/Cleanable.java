package org.apache.ambari.server.orm.dao;
public interface Cleanable {
    long cleanup(org.apache.ambari.server.cleanup.TimeBasedCleanupPolicy policy);
}