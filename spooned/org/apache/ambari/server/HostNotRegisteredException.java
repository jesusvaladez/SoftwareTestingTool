package org.apache.ambari.server;
public class HostNotRegisteredException extends org.apache.ambari.server.AmbariException {
    public static org.apache.ambari.server.HostNotRegisteredException forSessionId(java.lang.String sessionId) {
        return new org.apache.ambari.server.HostNotRegisteredException(java.lang.String.format("Host with sessionId '%s' not registered", sessionId));
    }

    public static org.apache.ambari.server.HostNotRegisteredException forHostId(java.lang.Long hostId) {
        return new org.apache.ambari.server.HostNotRegisteredException(java.lang.String.format("Host with hostId '%s' not registered", hostId));
    }

    private HostNotRegisteredException(java.lang.String message) {
        super(message);
    }
}