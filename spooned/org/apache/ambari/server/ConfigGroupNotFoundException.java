package org.apache.ambari.server;
public class ConfigGroupNotFoundException extends org.apache.ambari.server.ObjectNotFoundException {
    public ConfigGroupNotFoundException(java.lang.String clusterName, java.lang.String identifier) {
        super(((("Config group not found" + ", clusterName = ") + clusterName) + ", id = ") + identifier);
    }
}