package org.apache.ambari.server.agent;
@com.google.inject.Singleton
public class AgentSessionManager {
    private final java.util.concurrent.ConcurrentMap<java.lang.String, org.apache.ambari.server.state.Host> registeredHosts = new java.util.concurrent.ConcurrentHashMap<>();

    private final java.util.concurrent.ConcurrentMap<java.lang.Long, java.lang.String> registeredSessionIds = new java.util.concurrent.ConcurrentHashMap<>();

    public void register(java.lang.String sessionId, org.apache.ambari.server.state.Host host) {
        com.google.common.base.Preconditions.checkNotNull(sessionId);
        com.google.common.base.Preconditions.checkNotNull(host);
        com.google.common.base.Preconditions.checkNotNull(host.getHostId());
        java.lang.String oldSessionId = registeredSessionIds.put(host.getHostId(), sessionId);
        if (oldSessionId != null) {
            registeredHosts.remove(oldSessionId);
        }
        registeredHosts.put(sessionId, host);
    }

    public boolean isRegistered(java.lang.String sessionId) {
        return registeredHosts.containsKey(sessionId);
    }

    public org.apache.ambari.server.state.Host getHost(java.lang.String sessionId) throws org.apache.ambari.server.HostNotRegisteredException {
        com.google.common.base.Preconditions.checkNotNull(sessionId);
        org.apache.ambari.server.state.Host host = registeredHosts.get(sessionId);
        if (host != null) {
            return host;
        }
        throw org.apache.ambari.server.HostNotRegisteredException.forSessionId(sessionId);
    }

    public java.lang.String getSessionId(java.lang.Long hostId) throws org.apache.ambari.server.HostNotRegisteredException {
        com.google.common.base.Preconditions.checkNotNull(hostId);
        java.lang.String sessionId = registeredSessionIds.get(hostId);
        if (sessionId != null) {
            return sessionId;
        }
        throw org.apache.ambari.server.HostNotRegisteredException.forHostId(hostId);
    }

    public void unregisterByHost(java.lang.Long hostId) {
        com.google.common.base.Preconditions.checkNotNull(hostId);
        java.lang.String sessionId = registeredSessionIds.remove(hostId);
        if (sessionId != null) {
            registeredHosts.remove(sessionId);
        }
    }
}