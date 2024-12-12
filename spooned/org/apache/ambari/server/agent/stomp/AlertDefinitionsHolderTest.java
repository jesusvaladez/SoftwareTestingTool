package org.apache.ambari.server.agent.stomp;
import static org.easymock.EasyMock.createNiceMock;
public class AlertDefinitionsHolderTest {
    private final java.lang.Long HOST_ID = 1L;

    @org.junit.Test
    public void testHandleUpdateEmptyCurrent() throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.server.events.AlertDefinitionsAgentUpdateEvent current = new org.apache.ambari.server.events.AlertDefinitionsAgentUpdateEvent(org.apache.ambari.server.events.AlertDefinitionEventType.CREATE, java.util.Collections.emptyMap(), "host1", HOST_ID);
        java.util.Map<java.lang.Long, org.apache.ambari.server.agent.stomp.dto.AlertCluster> clusters = new java.util.HashMap<>();
        org.apache.ambari.server.agent.stomp.dto.AlertCluster cluster = org.apache.ambari.server.agent.stomp.dto.AlertCluster.emptyAlertCluster();
        clusters.put(1L, cluster);
        org.apache.ambari.server.events.AlertDefinitionsAgentUpdateEvent update = new org.apache.ambari.server.events.AlertDefinitionsAgentUpdateEvent(org.apache.ambari.server.events.AlertDefinitionEventType.UPDATE, clusters, "host1", HOST_ID);
        org.apache.ambari.server.agent.stomp.AlertDefinitionsHolder alertDefinitionsHolder = new org.apache.ambari.server.agent.stomp.AlertDefinitionsHolder(EasyMock.createNiceMock(org.apache.ambari.server.events.publishers.AmbariEventPublisher.class));
        org.apache.ambari.server.events.AlertDefinitionsAgentUpdateEvent result = alertDefinitionsHolder.handleUpdate(current, update);
        org.junit.Assert.assertFalse(result == update);
        org.junit.Assert.assertFalse(result == current);
        org.junit.Assert.assertEquals(org.apache.ambari.server.events.AlertDefinitionEventType.CREATE, result.getEventType());
        org.junit.Assert.assertEquals(result.getClusters(), update.getClusters());
    }

    @org.junit.Test
    public void testHandleUpdateEmptyUpdate() throws org.apache.ambari.server.AmbariException {
        java.util.Map<java.lang.Long, org.apache.ambari.server.agent.stomp.dto.AlertCluster> clusters = new java.util.HashMap<>();
        org.apache.ambari.server.agent.stomp.dto.AlertCluster cluster = org.apache.ambari.server.agent.stomp.dto.AlertCluster.emptyAlertCluster();
        clusters.put(1L, cluster);
        org.apache.ambari.server.events.AlertDefinitionsAgentUpdateEvent current = new org.apache.ambari.server.events.AlertDefinitionsAgentUpdateEvent(org.apache.ambari.server.events.AlertDefinitionEventType.CREATE, clusters, "host1", HOST_ID);
        org.apache.ambari.server.events.AlertDefinitionsAgentUpdateEvent update = new org.apache.ambari.server.events.AlertDefinitionsAgentUpdateEvent(org.apache.ambari.server.events.AlertDefinitionEventType.UPDATE, java.util.Collections.emptyMap(), "host1", HOST_ID);
        org.apache.ambari.server.agent.stomp.AlertDefinitionsHolder alertDefinitionsHolder = new org.apache.ambari.server.agent.stomp.AlertDefinitionsHolder(EasyMock.createNiceMock(org.apache.ambari.server.events.publishers.AmbariEventPublisher.class));
        org.apache.ambari.server.events.AlertDefinitionsAgentUpdateEvent result = alertDefinitionsHolder.handleUpdate(current, update);
        org.junit.Assert.assertFalse(result == update);
        org.junit.Assert.assertFalse(result == current);
        org.junit.Assert.assertEquals(result, null);
    }

    @org.junit.Test
    public void testHandleUpdateNoChanges() throws org.apache.ambari.server.AmbariException {
        java.util.Map<java.lang.Long, org.apache.ambari.server.agent.stomp.dto.AlertCluster> currentClusters = new java.util.HashMap<>();
        org.apache.ambari.server.agent.stomp.dto.AlertCluster currentCluster = new org.apache.ambari.server.agent.stomp.dto.AlertCluster(java.util.Collections.emptyMap(), "host1");
        currentClusters.put(1L, currentCluster);
        org.apache.ambari.server.events.AlertDefinitionsAgentUpdateEvent current = new org.apache.ambari.server.events.AlertDefinitionsAgentUpdateEvent(org.apache.ambari.server.events.AlertDefinitionEventType.CREATE, currentClusters, "host1", HOST_ID);
        java.util.Map<java.lang.Long, org.apache.ambari.server.agent.stomp.dto.AlertCluster> updateClusters = new java.util.HashMap<>();
        org.apache.ambari.server.agent.stomp.dto.AlertCluster updateCluster = new org.apache.ambari.server.agent.stomp.dto.AlertCluster(java.util.Collections.emptyMap(), "host1");
        updateClusters.put(1L, updateCluster);
        org.apache.ambari.server.events.AlertDefinitionsAgentUpdateEvent update = new org.apache.ambari.server.events.AlertDefinitionsAgentUpdateEvent(org.apache.ambari.server.events.AlertDefinitionEventType.UPDATE, updateClusters, "host1", HOST_ID);
        org.apache.ambari.server.agent.stomp.AlertDefinitionsHolder alertDefinitionsHolder = new org.apache.ambari.server.agent.stomp.AlertDefinitionsHolder(EasyMock.createNiceMock(org.apache.ambari.server.events.publishers.AmbariEventPublisher.class));
        org.apache.ambari.server.events.AlertDefinitionsAgentUpdateEvent result = alertDefinitionsHolder.handleUpdate(current, update);
        org.junit.Assert.assertFalse(result == update);
        org.junit.Assert.assertFalse(result == current);
        org.junit.Assert.assertEquals(result, null);
    }

    @org.junit.Test
    public void testHandleUpdateOnChanges() throws org.apache.ambari.server.AmbariException {
        java.util.Map<java.lang.Long, org.apache.ambari.server.agent.stomp.dto.AlertCluster> currentClusters = new java.util.HashMap<>();
        org.apache.ambari.server.agent.stomp.dto.AlertCluster currentCluster = new org.apache.ambari.server.agent.stomp.dto.AlertCluster(java.util.Collections.emptyMap(), "host1");
        currentClusters.put(1L, currentCluster);
        org.apache.ambari.server.events.AlertDefinitionsAgentUpdateEvent current = new org.apache.ambari.server.events.AlertDefinitionsAgentUpdateEvent(org.apache.ambari.server.events.AlertDefinitionEventType.CREATE, currentClusters, "host1", HOST_ID);
        java.util.Map<java.lang.Long, org.apache.ambari.server.agent.stomp.dto.AlertCluster> updateClusters = new java.util.HashMap<>();
        org.apache.ambari.server.agent.stomp.dto.AlertCluster updateCluster = new org.apache.ambari.server.agent.stomp.dto.AlertCluster(java.util.Collections.emptyMap(), "host1");
        updateClusters.put(2L, updateCluster);
        org.apache.ambari.server.events.AlertDefinitionsAgentUpdateEvent update = new org.apache.ambari.server.events.AlertDefinitionsAgentUpdateEvent(org.apache.ambari.server.events.AlertDefinitionEventType.UPDATE, updateClusters, "host1", HOST_ID);
        org.apache.ambari.server.agent.stomp.AlertDefinitionsHolder alertDefinitionsHolder = new org.apache.ambari.server.agent.stomp.AlertDefinitionsHolder(EasyMock.createNiceMock(org.apache.ambari.server.events.publishers.AmbariEventPublisher.class));
        org.apache.ambari.server.events.AlertDefinitionsAgentUpdateEvent result = alertDefinitionsHolder.handleUpdate(current, update);
        org.junit.Assert.assertFalse(result == update);
        org.junit.Assert.assertFalse(result == current);
        org.junit.Assert.assertEquals(2, result.getClusters().size());
        org.junit.Assert.assertTrue(result.getClusters().containsKey(1L));
        org.junit.Assert.assertTrue(result.getClusters().containsKey(2L));
    }
}