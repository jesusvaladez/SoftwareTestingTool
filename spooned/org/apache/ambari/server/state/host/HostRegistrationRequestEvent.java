package org.apache.ambari.server.state.host;
public class HostRegistrationRequestEvent extends org.apache.ambari.server.state.HostEvent {
    final long registrationTime;

    final org.apache.ambari.server.agent.HostInfo hostInfo;

    final org.apache.ambari.server.state.AgentVersion agentVersion;

    final java.lang.String publicHostName;

    final org.apache.ambari.server.agent.AgentEnv agentEnv;

    final long agentStartTime;

    public HostRegistrationRequestEvent(java.lang.String hostName, org.apache.ambari.server.state.AgentVersion agentVersion, long registrationTime, org.apache.ambari.server.agent.HostInfo hostInfo, org.apache.ambari.server.agent.AgentEnv env, long agentStartTime) {
        this(hostName, hostName, agentVersion, registrationTime, hostInfo, env, agentStartTime);
    }

    public HostRegistrationRequestEvent(java.lang.String hostName, java.lang.String publicName, org.apache.ambari.server.state.AgentVersion agentVersion, long registrationTime, org.apache.ambari.server.agent.HostInfo hostInfo, org.apache.ambari.server.agent.AgentEnv env, long agentStartTime) {
        super(hostName, org.apache.ambari.server.state.HostEventType.HOST_REGISTRATION_REQUEST);
        this.registrationTime = registrationTime;
        this.hostInfo = hostInfo;
        this.agentVersion = agentVersion;
        this.publicHostName = (null == publicName) ? hostName : publicName;
        this.agentEnv = env;
        this.agentStartTime = agentStartTime;
    }
}