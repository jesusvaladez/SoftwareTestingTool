package org.apache.ambari.server.topology.addservice;
public interface HostGroupStrategy {
    java.util.Map<java.lang.String, java.util.Set<java.lang.String>> calculateHostGroups(java.util.Map<java.lang.String, java.util.Set<java.lang.String>> hostComponentMap);
}