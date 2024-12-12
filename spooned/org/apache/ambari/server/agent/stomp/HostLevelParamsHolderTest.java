package org.apache.ambari.server.agent.stomp;
import static org.easymock.EasyMock.createNiceMock;
public class HostLevelParamsHolderTest {
    private final java.lang.Long HOST_ID = 1L;

    @org.junit.Test
    public void testHandleUpdateEmptyCurrent() {
        org.apache.ambari.server.events.HostLevelParamsUpdateEvent current = new org.apache.ambari.server.events.HostLevelParamsUpdateEvent(HOST_ID, java.util.Collections.emptyMap());
        java.util.Map<java.lang.String, org.apache.ambari.server.agent.stomp.dto.HostLevelParamsCluster> clusters = new java.util.HashMap<>();
        org.apache.ambari.server.agent.stomp.dto.HostLevelParamsCluster cluster = new org.apache.ambari.server.agent.stomp.dto.HostLevelParamsCluster(new org.apache.ambari.server.agent.RecoveryConfig(null), java.util.Collections.emptyMap());
        clusters.put("1", cluster);
        org.apache.ambari.server.events.HostLevelParamsUpdateEvent update = new org.apache.ambari.server.events.HostLevelParamsUpdateEvent(HOST_ID, clusters);
        org.apache.ambari.server.agent.stomp.HostLevelParamsHolder levelParamsHolder = new org.apache.ambari.server.agent.stomp.HostLevelParamsHolder(EasyMock.createNiceMock(org.apache.ambari.server.events.publishers.AmbariEventPublisher.class));
        org.apache.ambari.server.events.HostLevelParamsUpdateEvent result = levelParamsHolder.handleUpdate(current, update);
        org.junit.Assert.assertFalse(result == update);
        org.junit.Assert.assertFalse(result == current);
        org.junit.Assert.assertEquals(result, update);
    }

    @org.junit.Test
    public void testHandleUpdateEmptyUpdate() {
        java.util.Map<java.lang.String, org.apache.ambari.server.agent.stomp.dto.HostLevelParamsCluster> clusters = new java.util.HashMap<>();
        org.apache.ambari.server.agent.stomp.dto.HostLevelParamsCluster cluster = new org.apache.ambari.server.agent.stomp.dto.HostLevelParamsCluster(new org.apache.ambari.server.agent.RecoveryConfig(null), java.util.Collections.emptyMap());
        clusters.put("1", cluster);
        org.apache.ambari.server.events.HostLevelParamsUpdateEvent current = new org.apache.ambari.server.events.HostLevelParamsUpdateEvent(HOST_ID, clusters);
        org.apache.ambari.server.events.HostLevelParamsUpdateEvent update = new org.apache.ambari.server.events.HostLevelParamsUpdateEvent(HOST_ID, java.util.Collections.emptyMap());
        org.apache.ambari.server.agent.stomp.HostLevelParamsHolder levelParamsHolder = new org.apache.ambari.server.agent.stomp.HostLevelParamsHolder(EasyMock.createNiceMock(org.apache.ambari.server.events.publishers.AmbariEventPublisher.class));
        org.apache.ambari.server.events.HostLevelParamsUpdateEvent result = levelParamsHolder.handleUpdate(current, update);
        org.junit.Assert.assertFalse(result == update);
        org.junit.Assert.assertFalse(result == current);
        org.junit.Assert.assertEquals(result, null);
    }

    @org.junit.Test
    public void testHandleUpdateNoChanges() {
        java.util.Map<java.lang.String, org.apache.ambari.server.agent.stomp.dto.HostLevelParamsCluster> currentClusters = new java.util.HashMap<>();
        org.apache.ambari.server.agent.stomp.dto.HostLevelParamsCluster currentCluster = new org.apache.ambari.server.agent.stomp.dto.HostLevelParamsCluster(new org.apache.ambari.server.agent.RecoveryConfig(null), java.util.Collections.emptyMap());
        currentClusters.put("1", currentCluster);
        org.apache.ambari.server.events.HostLevelParamsUpdateEvent current = new org.apache.ambari.server.events.HostLevelParamsUpdateEvent(HOST_ID, currentClusters);
        java.util.Map<java.lang.String, org.apache.ambari.server.agent.stomp.dto.HostLevelParamsCluster> updateClusters = new java.util.HashMap<>();
        org.apache.ambari.server.agent.stomp.dto.HostLevelParamsCluster updateCluster = new org.apache.ambari.server.agent.stomp.dto.HostLevelParamsCluster(new org.apache.ambari.server.agent.RecoveryConfig(null), java.util.Collections.emptyMap());
        updateClusters.put("1", updateCluster);
        org.apache.ambari.server.events.HostLevelParamsUpdateEvent update = new org.apache.ambari.server.events.HostLevelParamsUpdateEvent(HOST_ID, updateClusters);
        org.apache.ambari.server.agent.stomp.HostLevelParamsHolder levelParamsHolder = new org.apache.ambari.server.agent.stomp.HostLevelParamsHolder(EasyMock.createNiceMock(org.apache.ambari.server.events.publishers.AmbariEventPublisher.class));
        org.apache.ambari.server.events.HostLevelParamsUpdateEvent result = levelParamsHolder.handleUpdate(current, update);
        org.junit.Assert.assertFalse(result == update);
        org.junit.Assert.assertFalse(result == current);
        org.junit.Assert.assertEquals(result, null);
    }

    @org.junit.Test
    public void testHandleUpdateOnChanges() {
        java.util.Map<java.lang.String, org.apache.ambari.server.agent.stomp.dto.HostLevelParamsCluster> currentClusters = new java.util.HashMap<>();
        org.apache.ambari.server.agent.stomp.dto.HostLevelParamsCluster currentCluster = new org.apache.ambari.server.agent.stomp.dto.HostLevelParamsCluster(new org.apache.ambari.server.agent.RecoveryConfig(null), java.util.Collections.emptyMap());
        currentClusters.put("1", currentCluster);
        org.apache.ambari.server.events.HostLevelParamsUpdateEvent current = new org.apache.ambari.server.events.HostLevelParamsUpdateEvent(HOST_ID, currentClusters);
        java.util.Map<java.lang.String, org.apache.ambari.server.agent.stomp.dto.HostLevelParamsCluster> updateClusters = new java.util.HashMap<>();
        org.apache.ambari.server.agent.stomp.dto.HostLevelParamsCluster updateCluster = new org.apache.ambari.server.agent.stomp.dto.HostLevelParamsCluster(new org.apache.ambari.server.agent.RecoveryConfig(null), java.util.Collections.emptyMap());
        updateClusters.put("2", updateCluster);
        org.apache.ambari.server.events.HostLevelParamsUpdateEvent update = new org.apache.ambari.server.events.HostLevelParamsUpdateEvent(HOST_ID, updateClusters);
        org.apache.ambari.server.agent.stomp.HostLevelParamsHolder levelParamsHolder = new org.apache.ambari.server.agent.stomp.HostLevelParamsHolder(EasyMock.createNiceMock(org.apache.ambari.server.events.publishers.AmbariEventPublisher.class));
        org.apache.ambari.server.events.HostLevelParamsUpdateEvent result = levelParamsHolder.handleUpdate(current, update);
        org.junit.Assert.assertFalse(result == update);
        org.junit.Assert.assertFalse(result == current);
        org.junit.Assert.assertEquals(2, result.getHostLevelParamsClusters().size());
        org.junit.Assert.assertTrue(result.getHostLevelParamsClusters().containsKey("1"));
        org.junit.Assert.assertTrue(result.getHostLevelParamsClusters().containsKey("2"));
    }
}