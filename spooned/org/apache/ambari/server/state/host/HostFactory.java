package org.apache.ambari.server.state.host;
public interface HostFactory {
    org.apache.ambari.server.state.Host create(org.apache.ambari.server.orm.entities.HostEntity hostEntity);
}