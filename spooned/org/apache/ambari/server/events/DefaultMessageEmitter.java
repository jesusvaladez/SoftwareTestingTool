package org.apache.ambari.server.events;
import org.apache.commons.lang.StringUtils;
import org.springframework.messaging.simp.SimpMessagingTemplate;
public class DefaultMessageEmitter extends org.apache.ambari.server.events.MessageEmitter {
    private final java.util.Map<org.apache.ambari.server.events.STOMPEvent.Type, java.lang.String> DEFAULT_DESTINATIONS = java.util.Collections.unmodifiableMap(new java.util.HashMap<org.apache.ambari.server.events.STOMPEvent.Type, java.lang.String>() {
        {
            put(org.apache.ambari.server.events.STOMPEvent.Type.ALERT, "/events/alerts");
            put(org.apache.ambari.server.events.STOMPEvent.Type.ALERT_GROUP, "/events/alert_group");
            put(org.apache.ambari.server.events.STOMPEvent.Type.METADATA, "/events/metadata");
            put(org.apache.ambari.server.events.STOMPEvent.Type.HOSTLEVELPARAMS, "/host_level_params");
            put(org.apache.ambari.server.events.STOMPEvent.Type.UI_TOPOLOGY, "/events/ui_topologies");
            put(org.apache.ambari.server.events.STOMPEvent.Type.AGENT_TOPOLOGY, "/events/topologies");
            put(org.apache.ambari.server.events.STOMPEvent.Type.AGENT_CONFIGS, "/configs");
            put(org.apache.ambari.server.events.STOMPEvent.Type.CONFIGS, "/events/configs");
            put(org.apache.ambari.server.events.STOMPEvent.Type.HOSTCOMPONENT, "/events/hostcomponents");
            put(org.apache.ambari.server.events.STOMPEvent.Type.NAMEDTASK, "/events/tasks");
            put(org.apache.ambari.server.events.STOMPEvent.Type.REQUEST, "/events/requests");
            put(org.apache.ambari.server.events.STOMPEvent.Type.SERVICE, "/events/services");
            put(org.apache.ambari.server.events.STOMPEvent.Type.HOST, "/events/hosts");
            put(org.apache.ambari.server.events.STOMPEvent.Type.COMMAND, "/commands");
            put(org.apache.ambari.server.events.STOMPEvent.Type.ALERT_DEFINITIONS, "/alert_definitions");
            put(org.apache.ambari.server.events.STOMPEvent.Type.UI_ALERT_DEFINITIONS, "/events/alert_definitions");
            put(org.apache.ambari.server.events.STOMPEvent.Type.UPGRADE, "/events/upgrade");
            put(org.apache.ambari.server.events.STOMPEvent.Type.AGENT_ACTIONS, "/agent_actions");
            put(org.apache.ambari.server.events.STOMPEvent.Type.ENCRYPTION_KEY_UPDATE, "/events/encryption_key");
        }
    });

    public static final java.util.Set<org.apache.ambari.server.events.STOMPEvent.Type> DEFAULT_AGENT_EVENT_TYPES = java.util.Collections.unmodifiableSet(new java.util.HashSet<org.apache.ambari.server.events.STOMPEvent.Type>(java.util.Arrays.asList(org.apache.ambari.server.events.STOMPEvent.Type.METADATA, org.apache.ambari.server.events.STOMPEvent.Type.HOSTLEVELPARAMS, org.apache.ambari.server.events.STOMPEvent.Type.AGENT_TOPOLOGY, org.apache.ambari.server.events.STOMPEvent.Type.AGENT_CONFIGS, org.apache.ambari.server.events.STOMPEvent.Type.COMMAND, org.apache.ambari.server.events.STOMPEvent.Type.ALERT_DEFINITIONS, org.apache.ambari.server.events.STOMPEvent.Type.AGENT_ACTIONS, org.apache.ambari.server.events.STOMPEvent.Type.ENCRYPTION_KEY_UPDATE)));

    public static final java.util.Set<org.apache.ambari.server.events.STOMPEvent.Type> DEFAULT_API_EVENT_TYPES = java.util.Collections.unmodifiableSet(new java.util.HashSet<org.apache.ambari.server.events.STOMPEvent.Type>(java.util.Arrays.asList(org.apache.ambari.server.events.STOMPEvent.Type.ALERT, org.apache.ambari.server.events.STOMPEvent.Type.ALERT_GROUP, org.apache.ambari.server.events.STOMPEvent.Type.METADATA, org.apache.ambari.server.events.STOMPEvent.Type.UI_TOPOLOGY, org.apache.ambari.server.events.STOMPEvent.Type.CONFIGS, org.apache.ambari.server.events.STOMPEvent.Type.HOSTCOMPONENT, org.apache.ambari.server.events.STOMPEvent.Type.NAMEDTASK, org.apache.ambari.server.events.STOMPEvent.Type.REQUEST, org.apache.ambari.server.events.STOMPEvent.Type.SERVICE, org.apache.ambari.server.events.STOMPEvent.Type.HOST, org.apache.ambari.server.events.STOMPEvent.Type.UI_ALERT_DEFINITIONS, org.apache.ambari.server.events.STOMPEvent.Type.UPGRADE)));

    public DefaultMessageEmitter(org.apache.ambari.server.agent.AgentSessionManager agentSessionManager, org.springframework.messaging.simp.SimpMessagingTemplate simpMessagingTemplate, org.apache.ambari.server.events.publishers.AmbariEventPublisher ambariEventPublisher, int retryCount, int retryInterval) {
        super(agentSessionManager, simpMessagingTemplate, ambariEventPublisher, retryCount, retryInterval);
    }

    @java.lang.Override
    public void emitMessage(org.apache.ambari.server.events.STOMPEvent event) throws org.apache.ambari.server.AmbariException {
        if (org.apache.commons.lang.StringUtils.isEmpty(getDestination(event))) {
            throw new org.apache.ambari.server.MessageDestinationIsNotDefinedException(event.getType());
        }
        if (event instanceof org.apache.ambari.server.events.STOMPHostEvent) {
            org.apache.ambari.server.events.STOMPHostEvent hostUpdateEvent = ((org.apache.ambari.server.events.STOMPHostEvent) (event));
            if (hostUpdateEvent.getType().equals(org.apache.ambari.server.events.STOMPEvent.Type.COMMAND)) {
                emitMessageRetriable(((org.apache.ambari.server.events.ExecutionCommandEvent) (hostUpdateEvent)));
            } else {
                emitMessageToHost(hostUpdateEvent);
            }
        } else {
            emitMessageToAll(event);
        }
    }

    @java.lang.Override
    protected java.lang.String getDestination(org.apache.ambari.server.events.STOMPEvent stompEvent) {
        return stompEvent.completeDestination(DEFAULT_DESTINATIONS.get(stompEvent.getType()));
    }
}