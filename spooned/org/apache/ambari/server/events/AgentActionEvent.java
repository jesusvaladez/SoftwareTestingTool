package org.apache.ambari.server.events;
@com.fasterxml.jackson.annotation.JsonInclude(com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY)
public class AgentActionEvent extends org.apache.ambari.server.events.STOMPHostEvent {
    private final java.lang.Long hostId;

    @com.fasterxml.jackson.annotation.JsonProperty("actionName")
    private org.apache.ambari.server.events.AgentActionEvent.AgentAction agentAction;

    public AgentActionEvent(org.apache.ambari.server.events.AgentActionEvent.AgentAction agentAction, java.lang.Long hostId) {
        super(org.apache.ambari.server.events.STOMPEvent.Type.AGENT_ACTIONS);
        this.agentAction = agentAction;
        this.hostId = hostId;
    }

    @java.lang.Override
    public java.lang.Long getHostId() {
        return hostId;
    }

    public enum AgentAction {

        RESTART_AGENT;}
}