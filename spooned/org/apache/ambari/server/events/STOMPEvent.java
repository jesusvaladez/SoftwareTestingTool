package org.apache.ambari.server.events;
public abstract class STOMPEvent {
    protected final org.apache.ambari.server.events.STOMPEvent.Type type;

    public STOMPEvent(org.apache.ambari.server.events.STOMPEvent.Type type) {
        this.type = type;
    }

    @java.beans.Transient
    public org.apache.ambari.server.events.STOMPEvent.Type getType() {
        return type;
    }

    @java.beans.Transient
    public java.lang.String getMetricName() {
        return type.getMetricName();
    }

    public enum Type {

        ALERT("events.alerts"),
        ALERT_GROUP("events.alert_group"),
        METADATA("events.metadata"),
        HOSTLEVELPARAMS("events.hostlevelparams"),
        UI_TOPOLOGY("events.topology_update"),
        AGENT_TOPOLOGY("events.topology_update"),
        AGENT_CONFIGS("events.agent.configs"),
        CONFIGS("events.configs"),
        HOSTCOMPONENT("events.hostcomponents"),
        NAMEDHOSTCOMPONENT("events.hostrolecommands.named"),
        NAMEDTASK("events.tasks.named"),
        REQUEST("events.requests"),
        SERVICE("events.services"),
        HOST("events.hosts"),
        UI_ALERT_DEFINITIONS("events.alert_definitions"),
        ALERT_DEFINITIONS("alert_definitions"),
        UPGRADE("events.upgrade"),
        COMMAND("events.commands"),
        AGENT_ACTIONS("events.agentactions"),
        ENCRYPTION_KEY_UPDATE("events.encryption_key_update");
        private java.lang.String metricName;

        Type(java.lang.String metricName) {
            this.metricName = metricName;
        }

        public java.lang.String getMetricName() {
            return metricName;
        }
    }

    public java.lang.String completeDestination(java.lang.String destination) {
        return destination;
    }
}