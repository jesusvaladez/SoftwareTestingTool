package org.apache.ambari.server.stageplanner;
public interface RoleGraphFactory {
    org.apache.ambari.server.stageplanner.RoleGraph createNew();

    org.apache.ambari.server.stageplanner.RoleGraph createNew(org.apache.ambari.server.metadata.RoleCommandOrder rd);
}