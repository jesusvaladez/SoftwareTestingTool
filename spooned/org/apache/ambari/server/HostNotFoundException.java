package org.apache.ambari.server;
@java.lang.SuppressWarnings("serial")
public class HostNotFoundException extends org.apache.ambari.server.ObjectNotFoundException {
    public HostNotFoundException(java.lang.String hostname) {
        super("Host not found, hostname=" + hostname);
    }

    public HostNotFoundException(java.lang.String clusterName, java.lang.String hostname) {
        super((("Host not found, cluster=" + clusterName) + ", hostname=") + hostname);
    }
}