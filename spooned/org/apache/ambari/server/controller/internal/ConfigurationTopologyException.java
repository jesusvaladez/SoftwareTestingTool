package org.apache.ambari.server.controller.internal;
public class ConfigurationTopologyException extends java.lang.Exception {
    public ConfigurationTopologyException(java.util.Collection<java.lang.String> properties) {
        super(java.lang.String.format("Unable to resolve host names for the following configuration properties %s", properties));
    }

    public ConfigurationTopologyException(java.lang.String s) {
        super(s);
    }

    public ConfigurationTopologyException(java.lang.String s, java.lang.Throwable throwable) {
        super(s, throwable);
    }
}