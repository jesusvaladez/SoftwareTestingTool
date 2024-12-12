package org.apache.ambari.server.agent.stomp;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.annotation.SendToUser;
@org.springframework.stereotype.Controller
@org.springframework.messaging.simp.annotation.SendToUser("/")
@org.springframework.messaging.handler.annotation.MessageMapping("/agents")
public class AgentCurrentDataController {
    private final org.apache.ambari.server.agent.AgentSessionManager agentSessionManager;

    private final org.apache.ambari.server.agent.stomp.TopologyHolder topologyHolder;

    private final org.apache.ambari.server.agent.stomp.MetadataHolder metadataHolder;

    private final org.apache.ambari.server.agent.stomp.HostLevelParamsHolder hostLevelParamsHolder;

    private final org.apache.ambari.server.agent.stomp.AgentConfigsHolder agentConfigsHolder;

    private final org.apache.ambari.server.agent.stomp.AlertDefinitionsHolder alertDefinitionsHolder;

    public AgentCurrentDataController(com.google.inject.Injector injector) {
        agentSessionManager = injector.getInstance(org.apache.ambari.server.agent.AgentSessionManager.class);
        topologyHolder = injector.getInstance(org.apache.ambari.server.agent.stomp.TopologyHolder.class);
        metadataHolder = injector.getInstance(org.apache.ambari.server.agent.stomp.MetadataHolder.class);
        hostLevelParamsHolder = injector.getInstance(org.apache.ambari.server.agent.stomp.HostLevelParamsHolder.class);
        agentConfigsHolder = injector.getInstance(org.apache.ambari.server.agent.stomp.AgentConfigsHolder.class);
        alertDefinitionsHolder = injector.getInstance(org.apache.ambari.server.agent.stomp.AlertDefinitionsHolder.class);
    }

    @org.springframework.messaging.handler.annotation.MessageMapping("/topologies")
    public org.apache.ambari.server.events.TopologyUpdateEvent getCurrentTopology(org.apache.ambari.server.agent.stomp.dto.Hash hash) throws org.apache.ambari.server.AmbariException, org.apache.ambari.server.state.fsm.InvalidStateTransitionException {
        return topologyHolder.getUpdateIfChanged(hash.getHash());
    }

    @org.springframework.messaging.handler.annotation.MessageMapping("/metadata")
    public org.apache.ambari.server.events.MetadataUpdateEvent getCurrentMetadata(org.apache.ambari.server.agent.stomp.dto.Hash hash) throws org.apache.ambari.server.AmbariException {
        return metadataHolder.getUpdateIfChanged(hash.getHash());
    }

    @org.springframework.messaging.handler.annotation.MessageMapping("/alert_definitions")
    public org.apache.ambari.server.events.AlertDefinitionsAgentUpdateEvent getAlertDefinitions(@org.springframework.messaging.handler.annotation.Header
    java.lang.String simpSessionId, org.apache.ambari.server.agent.stomp.dto.Hash hash) throws org.apache.ambari.server.AmbariException {
        java.lang.Long hostId = agentSessionManager.getHost(simpSessionId).getHostId();
        return alertDefinitionsHolder.getUpdateIfChanged(hash.getHash(), hostId);
    }

    @org.springframework.messaging.handler.annotation.MessageMapping("/configs")
    public org.apache.ambari.server.events.AgentConfigsUpdateEvent getCurrentConfigs(@org.springframework.messaging.handler.annotation.Header
    java.lang.String simpSessionId, org.apache.ambari.server.agent.stomp.dto.Hash hash) throws org.apache.ambari.server.AmbariException {
        return agentConfigsHolder.getUpdateIfChanged(hash.getHash(), agentSessionManager.getHost(simpSessionId).getHostId());
    }

    @org.springframework.messaging.handler.annotation.MessageMapping("/host_level_params")
    public org.apache.ambari.server.events.HostLevelParamsUpdateEvent getCurrentHostLevelParams(@org.springframework.messaging.handler.annotation.Header
    java.lang.String simpSessionId, org.apache.ambari.server.agent.stomp.dto.Hash hash) throws org.apache.ambari.server.AmbariException {
        return hostLevelParamsHolder.getUpdateIfChanged(hash.getHash(), agentSessionManager.getHost(simpSessionId).getHostId());
    }
}