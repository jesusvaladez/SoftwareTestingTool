package org.apache.ambari.server.topology;
import javax.annotation.Nullable;
import org.easymock.EasyMockRule;
import org.easymock.EasyMockSupport;
import org.easymock.Mock;
import org.powermock.api.easymock.PowerMock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expect;
import static org.powermock.api.easymock.PowerMock.mockStatic;
@org.junit.runner.RunWith(org.powermock.modules.junit4.PowerMockRunner.class)
@org.powermock.core.classloader.annotations.PrepareForTest(org.apache.ambari.server.controller.AmbariServer.class)
public class LogicalRequestTest extends org.easymock.EasyMockSupport {
    @org.junit.Rule
    public org.easymock.EasyMockRule mocks = new org.easymock.EasyMockRule(this);

    @org.easymock.Mock
    private org.apache.ambari.server.topology.TopologyRequest replayedTopologyRequest;

    @org.easymock.Mock
    private org.apache.ambari.server.topology.ClusterTopology clusterTopology;

    @org.easymock.Mock
    private org.apache.ambari.server.orm.entities.TopologyLogicalRequestEntity logicalRequestEntity;

    @org.easymock.Mock
    private org.apache.ambari.server.controller.AmbariManagementController controller;

    @org.easymock.Mock
    private org.apache.ambari.server.topology.AmbariContext ambariContext;

    private final long clusterId = 2L;

    private final java.lang.String clusterName = "myCluster";

    @org.easymock.Mock
    private org.apache.ambari.server.state.Clusters clusters;

    @org.easymock.Mock
    private org.apache.ambari.server.state.Cluster cluster;

    @org.easymock.Mock
    private org.apache.ambari.server.topology.Blueprint blueprint;

    @org.easymock.Mock
    private org.apache.ambari.server.topology.HostGroup hostGroup1;

    @org.easymock.Mock
    private org.apache.ambari.server.topology.HostGroup hostGroup2;

    @org.junit.Before
    public void setup() throws java.lang.Exception {
        resetAll();
        EasyMock.expect(controller.getClusters()).andReturn(clusters).anyTimes();
        EasyMock.expect(clusters.getClusterById(clusterId)).andReturn(cluster).anyTimes();
        EasyMock.expect(cluster.getClusterName()).andReturn(clusterName).anyTimes();
        java.lang.String topologyReqestDescription = "Provision cluster";
        EasyMock.expect(replayedTopologyRequest.getDescription()).andReturn(topologyReqestDescription).anyTimes();
        EasyMock.expect(clusterTopology.getAmbariContext()).andReturn(ambariContext).anyTimes();
        EasyMock.expect(clusterTopology.getClusterId()).andReturn(clusterId).anyTimes();
        EasyMock.expect(clusterTopology.getProvisionAction()).andReturn(org.apache.ambari.server.controller.internal.ProvisionAction.INSTALL_ONLY).anyTimes();
        EasyMock.expect(clusterTopology.getBlueprint()).andReturn(blueprint).anyTimes();
        EasyMock.expect(blueprint.getName()).andReturn("blueprintDef").anyTimes();
        EasyMock.expect(blueprint.shouldSkipFailure()).andReturn(true).anyTimes();
        org.powermock.api.easymock.PowerMock.reset(org.apache.ambari.server.controller.AmbariServer.class);
        mockStatic(org.apache.ambari.server.controller.AmbariServer.class);
        EasyMock.expect(org.apache.ambari.server.controller.AmbariServer.getController()).andReturn(controller).anyTimes();
        org.powermock.api.easymock.PowerMock.replay(org.apache.ambari.server.controller.AmbariServer.class);
    }

    @org.junit.Test
    public void testPersistedRequestsWithReservedHosts() throws java.lang.Exception {
        java.lang.Long requestId = 1L;
        org.apache.ambari.server.orm.entities.TopologyHostInfoEntity host1 = new org.apache.ambari.server.orm.entities.TopologyHostInfoEntity();
        host1.setId(100L);
        host1.setFqdn("host1");
        org.apache.ambari.server.orm.entities.TopologyHostInfoEntity host2 = new org.apache.ambari.server.orm.entities.TopologyHostInfoEntity();
        host2.setId(101L);
        host2.setFqdn("host2");
        org.apache.ambari.server.orm.entities.TopologyHostInfoEntity host3 = new org.apache.ambari.server.orm.entities.TopologyHostInfoEntity();
        host3.setId(103L);
        host3.setFqdn("host3");
        org.apache.ambari.server.orm.entities.TopologyHostInfoEntity host4 = new org.apache.ambari.server.orm.entities.TopologyHostInfoEntity();
        host4.setId(104L);
        host4.setFqdn("host4");
        org.apache.ambari.server.orm.entities.TopologyHostGroupEntity hostGroupEntity1 = new org.apache.ambari.server.orm.entities.TopologyHostGroupEntity();
        hostGroupEntity1.setTopologyHostInfoEntities(com.google.common.collect.ImmutableSet.of(host1, host2, host3, host4));
        hostGroupEntity1.setName("host_group_1");
        org.apache.ambari.server.orm.entities.TopologyHostRequestEntity hostRequestEntityHost1Matched = new org.apache.ambari.server.orm.entities.TopologyHostRequestEntity();
        hostRequestEntityHost1Matched.setId(1L);
        hostRequestEntityHost1Matched.setHostName(host1.getFqdn());
        hostRequestEntityHost1Matched.setTopologyHostGroupEntity(hostGroupEntity1);
        hostRequestEntityHost1Matched.setTopologyHostTaskEntities(java.util.Collections.emptySet());
        EasyMock.expect(ambariContext.isHostRegisteredWithCluster(EasyMock.eq(clusterId), EasyMock.eq(host1.getFqdn()))).andReturn(true).anyTimes();
        org.apache.ambari.server.orm.entities.TopologyHostRequestEntity hostRequestEntityHost2Matched = new org.apache.ambari.server.orm.entities.TopologyHostRequestEntity();
        hostRequestEntityHost2Matched.setId(2L);
        hostRequestEntityHost2Matched.setHostName(host2.getFqdn());
        hostRequestEntityHost2Matched.setTopologyHostGroupEntity(hostGroupEntity1);
        hostRequestEntityHost2Matched.setTopologyHostTaskEntities(java.util.Collections.emptySet());
        EasyMock.expect(ambariContext.isHostRegisteredWithCluster(EasyMock.eq(clusterId), EasyMock.eq(host2.getFqdn()))).andReturn(true).anyTimes();
        org.apache.ambari.server.orm.entities.TopologyHostRequestEntity hostRequestEntityHost3NotMatched = new org.apache.ambari.server.orm.entities.TopologyHostRequestEntity();
        hostRequestEntityHost3NotMatched.setId(3L);
        hostRequestEntityHost3NotMatched.setTopologyHostGroupEntity(hostGroupEntity1);
        hostRequestEntityHost3NotMatched.setTopologyHostTaskEntities(java.util.Collections.emptySet());
        EasyMock.expect(ambariContext.isHostRegisteredWithCluster(EasyMock.eq(clusterId), EasyMock.eq(host3.getFqdn()))).andReturn(false).anyTimes();
        org.apache.ambari.server.orm.entities.TopologyHostRequestEntity hostRequestEntityHost4NotMatched = new org.apache.ambari.server.orm.entities.TopologyHostRequestEntity();
        hostRequestEntityHost4NotMatched.setId(4L);
        hostRequestEntityHost4NotMatched.setTopologyHostGroupEntity(hostGroupEntity1);
        hostRequestEntityHost4NotMatched.setTopologyHostTaskEntities(java.util.Collections.emptySet());
        EasyMock.expect(ambariContext.isHostRegisteredWithCluster(EasyMock.eq(clusterId), EasyMock.eq(host4.getFqdn()))).andReturn(false).anyTimes();
        java.util.Collection<org.apache.ambari.server.orm.entities.TopologyHostRequestEntity> reservedHostRequestEntities = com.google.common.collect.ImmutableSet.of(hostRequestEntityHost1Matched, hostRequestEntityHost2Matched, hostRequestEntityHost3NotMatched, hostRequestEntityHost4NotMatched);
        hostGroupEntity1.setTopologyHostRequestEntities(reservedHostRequestEntities);
        org.apache.ambari.server.orm.entities.TopologyRequestEntity topologyRequestEntity = new org.apache.ambari.server.orm.entities.TopologyRequestEntity();
        topologyRequestEntity.setTopologyHostGroupEntities(java.util.Collections.singleton(hostGroupEntity1));
        EasyMock.expect(logicalRequestEntity.getTopologyHostRequestEntities()).andReturn(reservedHostRequestEntities).atLeastOnce();
        EasyMock.expect(logicalRequestEntity.getTopologyRequestEntity()).andReturn(topologyRequestEntity).atLeastOnce();
        EasyMock.expect(blueprint.getHostGroup(EasyMock.eq("host_group_1"))).andReturn(hostGroup1).atLeastOnce();
        EasyMock.expect(hostGroup1.containsMasterComponent()).andReturn(false).atLeastOnce();
        replayAll();
        org.apache.ambari.server.topology.LogicalRequest req = new org.apache.ambari.server.topology.LogicalRequest(requestId, replayedTopologyRequest, clusterTopology, logicalRequestEntity);
        verifyAll();
        java.util.Collection<java.lang.String> actualReservedHosts = req.getReservedHosts();
        java.util.Collection<java.lang.String> expectedReservedHosts = com.google.common.collect.ImmutableSet.of(host3.getFqdn(), host4.getFqdn());
        org.junit.Assert.assertEquals(expectedReservedHosts, actualReservedHosts);
        java.util.Collection<org.apache.ambari.server.topology.HostRequest> actualCompletedHostReqs = req.getCompletedHostRequests();
        org.junit.Assert.assertEquals(2, actualCompletedHostReqs.size());
        com.google.common.base.Optional<org.apache.ambari.server.topology.HostRequest> completedHostReq1 = com.google.common.collect.Iterables.tryFind(actualCompletedHostReqs, new com.google.common.base.Predicate<org.apache.ambari.server.topology.HostRequest>() {
            @java.lang.Override
            public boolean apply(@javax.annotation.Nullable
            org.apache.ambari.server.topology.HostRequest input) {
                return "host1".equals(input.getHostName());
            }
        });
        com.google.common.base.Optional<org.apache.ambari.server.topology.HostRequest> completedHostReq2 = com.google.common.collect.Iterables.tryFind(actualCompletedHostReqs, new com.google.common.base.Predicate<org.apache.ambari.server.topology.HostRequest>() {
            @java.lang.Override
            public boolean apply(@javax.annotation.Nullable
            org.apache.ambari.server.topology.HostRequest input) {
                return "host2".equals(input.getHostName());
            }
        });
        org.junit.Assert.assertTrue(completedHostReq1.isPresent() && completedHostReq2.isPresent());
    }

    @org.junit.Test
    public void testPersistedRequestsWithHostPredicate() throws java.lang.Exception {
        java.lang.Long requestId = 1L;
        org.apache.ambari.server.orm.entities.TopologyHostInfoEntity host = new org.apache.ambari.server.orm.entities.TopologyHostInfoEntity();
        host.setId(800L);
        host.setPredicate("Hosts/host_name.in(host[1-4])");
        org.apache.ambari.server.orm.entities.TopologyHostGroupEntity hostGroupEntity2 = new org.apache.ambari.server.orm.entities.TopologyHostGroupEntity();
        hostGroupEntity2.setTopologyHostInfoEntities(com.google.common.collect.ImmutableSet.of(host));
        hostGroupEntity2.setName("host_group_2");
        org.apache.ambari.server.orm.entities.TopologyHostRequestEntity hostRequestEntityHost1Matched = new org.apache.ambari.server.orm.entities.TopologyHostRequestEntity();
        hostRequestEntityHost1Matched.setId(1L);
        hostRequestEntityHost1Matched.setHostName("host1");
        hostRequestEntityHost1Matched.setTopologyHostGroupEntity(hostGroupEntity2);
        hostRequestEntityHost1Matched.setTopologyHostTaskEntities(java.util.Collections.emptySet());
        EasyMock.expect(ambariContext.isHostRegisteredWithCluster(EasyMock.eq(clusterId), EasyMock.eq("host1"))).andReturn(true).anyTimes();
        org.apache.ambari.server.orm.entities.TopologyHostRequestEntity hostRequestEntityHost2Matched = new org.apache.ambari.server.orm.entities.TopologyHostRequestEntity();
        hostRequestEntityHost2Matched.setId(2L);
        hostRequestEntityHost2Matched.setHostName("host2");
        hostRequestEntityHost2Matched.setTopologyHostGroupEntity(hostGroupEntity2);
        hostRequestEntityHost2Matched.setTopologyHostTaskEntities(java.util.Collections.emptySet());
        EasyMock.expect(ambariContext.isHostRegisteredWithCluster(EasyMock.eq(clusterId), EasyMock.eq("host2"))).andReturn(true).anyTimes();
        org.apache.ambari.server.orm.entities.TopologyHostRequestEntity hostRequestEntityHost3NotMatched = new org.apache.ambari.server.orm.entities.TopologyHostRequestEntity();
        hostRequestEntityHost3NotMatched.setId(3L);
        hostRequestEntityHost3NotMatched.setTopologyHostGroupEntity(hostGroupEntity2);
        hostRequestEntityHost3NotMatched.setTopologyHostTaskEntities(java.util.Collections.emptySet());
        EasyMock.expect(ambariContext.isHostRegisteredWithCluster(EasyMock.eq(clusterId), EasyMock.eq("host3"))).andReturn(false).anyTimes();
        org.apache.ambari.server.orm.entities.TopologyHostRequestEntity hostRequestEntityHost4NotMatched = new org.apache.ambari.server.orm.entities.TopologyHostRequestEntity();
        hostRequestEntityHost4NotMatched.setId(4L);
        hostRequestEntityHost4NotMatched.setTopologyHostGroupEntity(hostGroupEntity2);
        hostRequestEntityHost4NotMatched.setTopologyHostTaskEntities(java.util.Collections.emptySet());
        EasyMock.expect(ambariContext.isHostRegisteredWithCluster(EasyMock.eq(clusterId), EasyMock.eq("host4"))).andReturn(false).anyTimes();
        java.util.Collection<org.apache.ambari.server.orm.entities.TopologyHostRequestEntity> reservedHostRequestEntities = com.google.common.collect.ImmutableSet.of(hostRequestEntityHost1Matched, hostRequestEntityHost2Matched, hostRequestEntityHost3NotMatched, hostRequestEntityHost4NotMatched);
        hostGroupEntity2.setTopologyHostRequestEntities(reservedHostRequestEntities);
        org.apache.ambari.server.orm.entities.TopologyRequestEntity topologyRequestEntity = new org.apache.ambari.server.orm.entities.TopologyRequestEntity();
        topologyRequestEntity.setTopologyHostGroupEntities(java.util.Collections.singleton(hostGroupEntity2));
        EasyMock.expect(logicalRequestEntity.getTopologyHostRequestEntities()).andReturn(reservedHostRequestEntities).atLeastOnce();
        EasyMock.expect(logicalRequestEntity.getTopologyRequestEntity()).andReturn(topologyRequestEntity).atLeastOnce();
        EasyMock.expect(blueprint.getHostGroup(EasyMock.eq("host_group_2"))).andReturn(hostGroup2).atLeastOnce();
        EasyMock.expect(hostGroup2.containsMasterComponent()).andReturn(false).atLeastOnce();
        replayAll();
        org.apache.ambari.server.topology.LogicalRequest req = new org.apache.ambari.server.topology.LogicalRequest(requestId, replayedTopologyRequest, clusterTopology, logicalRequestEntity);
        verifyAll();
        java.util.Collection<java.lang.String> actualReservedHosts = req.getReservedHosts();
        java.util.Collection<java.lang.String> expectedReservedHosts = java.util.Collections.emptySet();
        org.junit.Assert.assertEquals(expectedReservedHosts, actualReservedHosts);
        java.util.Collection<org.apache.ambari.server.topology.HostRequest> actualCompletedHostReqs = req.getCompletedHostRequests();
        org.junit.Assert.assertEquals(2, actualCompletedHostReqs.size());
        com.google.common.base.Optional<org.apache.ambari.server.topology.HostRequest> completedHostReq1 = com.google.common.collect.Iterables.tryFind(actualCompletedHostReqs, new com.google.common.base.Predicate<org.apache.ambari.server.topology.HostRequest>() {
            @java.lang.Override
            public boolean apply(@javax.annotation.Nullable
            org.apache.ambari.server.topology.HostRequest input) {
                return "host1".equals(input.getHostName());
            }
        });
        com.google.common.base.Optional<org.apache.ambari.server.topology.HostRequest> completedHostReq2 = com.google.common.collect.Iterables.tryFind(actualCompletedHostReqs, new com.google.common.base.Predicate<org.apache.ambari.server.topology.HostRequest>() {
            @java.lang.Override
            public boolean apply(@javax.annotation.Nullable
            org.apache.ambari.server.topology.HostRequest input) {
                return "host2".equals(input.getHostName());
            }
        });
        org.junit.Assert.assertTrue(completedHostReq1.isPresent() && completedHostReq2.isPresent());
    }

    @org.junit.Test
    public void testRemoveHostRequestByHostName() throws java.lang.Exception {
        java.lang.Long requestId = 1L;
        final org.apache.ambari.server.orm.entities.TopologyHostInfoEntity host1 = new org.apache.ambari.server.orm.entities.TopologyHostInfoEntity();
        host1.setId(100L);
        host1.setFqdn("host1");
        final org.apache.ambari.server.orm.entities.TopologyHostInfoEntity host2 = new org.apache.ambari.server.orm.entities.TopologyHostInfoEntity();
        host2.setId(101L);
        host2.setFqdn("host2");
        final org.apache.ambari.server.orm.entities.TopologyHostInfoEntity host3 = new org.apache.ambari.server.orm.entities.TopologyHostInfoEntity();
        host3.setId(103L);
        host3.setFqdn("host3");
        final org.apache.ambari.server.orm.entities.TopologyHostInfoEntity host4 = new org.apache.ambari.server.orm.entities.TopologyHostInfoEntity();
        host4.setId(104L);
        host4.setFqdn("host4");
        org.apache.ambari.server.orm.entities.TopologyHostGroupEntity hostGroupEntity1 = new org.apache.ambari.server.orm.entities.TopologyHostGroupEntity();
        hostGroupEntity1.setTopologyHostInfoEntities(com.google.common.collect.ImmutableSet.of(host1, host2, host3));
        hostGroupEntity1.setName("host_group_1");
        org.apache.ambari.server.orm.entities.TopologyHostRequestEntity hostRequestEntityHost1Matched = new org.apache.ambari.server.orm.entities.TopologyHostRequestEntity();
        hostRequestEntityHost1Matched.setId(1L);
        hostRequestEntityHost1Matched.setHostName(host1.getFqdn());
        hostRequestEntityHost1Matched.setTopologyHostGroupEntity(hostGroupEntity1);
        hostRequestEntityHost1Matched.setTopologyHostTaskEntities(java.util.Collections.emptySet());
        EasyMock.expect(ambariContext.isHostRegisteredWithCluster(EasyMock.eq(clusterId), EasyMock.eq(host1.getFqdn()))).andReturn(true).anyTimes();
        org.apache.ambari.server.orm.entities.TopologyHostRequestEntity hostRequestEntityHost2Matched = new org.apache.ambari.server.orm.entities.TopologyHostRequestEntity();
        hostRequestEntityHost2Matched.setId(2L);
        hostRequestEntityHost2Matched.setHostName(host2.getFqdn());
        hostRequestEntityHost2Matched.setTopologyHostGroupEntity(hostGroupEntity1);
        hostRequestEntityHost2Matched.setTopologyHostTaskEntities(java.util.Collections.emptySet());
        EasyMock.expect(ambariContext.isHostRegisteredWithCluster(EasyMock.eq(clusterId), EasyMock.eq(host2.getFqdn()))).andReturn(true).anyTimes();
        org.apache.ambari.server.orm.entities.TopologyHostRequestEntity hostRequestEntityHost3NotMatched = new org.apache.ambari.server.orm.entities.TopologyHostRequestEntity();
        hostRequestEntityHost3NotMatched.setId(3L);
        hostRequestEntityHost3NotMatched.setTopologyHostGroupEntity(hostGroupEntity1);
        hostRequestEntityHost3NotMatched.setTopologyHostTaskEntities(java.util.Collections.emptySet());
        EasyMock.expect(ambariContext.isHostRegisteredWithCluster(EasyMock.eq(clusterId), EasyMock.eq(host3.getFqdn()))).andReturn(false).anyTimes();
        org.apache.ambari.server.orm.entities.TopologyHostRequestEntity hostRequestEntityHost4Matched = new org.apache.ambari.server.orm.entities.TopologyHostRequestEntity();
        hostRequestEntityHost4Matched.setId(4L);
        hostRequestEntityHost4Matched.setHostName(host4.getFqdn());
        hostRequestEntityHost4Matched.setTopologyHostGroupEntity(hostGroupEntity1);
        hostRequestEntityHost4Matched.setTopologyHostTaskEntities(java.util.Collections.emptySet());
        EasyMock.expect(ambariContext.isHostRegisteredWithCluster(EasyMock.eq(clusterId), EasyMock.eq(host4.getFqdn()))).andReturn(true).anyTimes();
        java.util.Collection<org.apache.ambari.server.orm.entities.TopologyHostRequestEntity> reservedHostRequestEntities = com.google.common.collect.ImmutableSet.of(hostRequestEntityHost1Matched, hostRequestEntityHost2Matched, hostRequestEntityHost3NotMatched, hostRequestEntityHost4Matched);
        hostGroupEntity1.setTopologyHostRequestEntities(reservedHostRequestEntities);
        org.apache.ambari.server.orm.entities.TopologyRequestEntity topologyRequestEntity = new org.apache.ambari.server.orm.entities.TopologyRequestEntity();
        topologyRequestEntity.setTopologyHostGroupEntities(java.util.Collections.singleton(hostGroupEntity1));
        EasyMock.expect(logicalRequestEntity.getTopologyRequestEntity()).andReturn(topologyRequestEntity).atLeastOnce();
        EasyMock.expect(logicalRequestEntity.getTopologyHostRequestEntities()).andReturn(reservedHostRequestEntities).atLeastOnce();
        EasyMock.expect(blueprint.getHostGroup(EasyMock.eq("host_group_1"))).andReturn(hostGroup1).atLeastOnce();
        EasyMock.expect(hostGroup1.containsMasterComponent()).andReturn(false).atLeastOnce();
        replayAll();
        org.apache.ambari.server.topology.LogicalRequest req = new org.apache.ambari.server.topology.LogicalRequest(requestId, replayedTopologyRequest, clusterTopology, logicalRequestEntity);
        req.removeHostRequestByHostName(host4.getFqdn());
        verifyAll();
        java.util.Collection<org.apache.ambari.server.topology.HostRequest> hostRequests = req.getHostRequests();
        org.junit.Assert.assertEquals(3, hostRequests.size());
        com.google.common.base.Optional<org.apache.ambari.server.topology.HostRequest> hostReqHost1 = com.google.common.collect.Iterables.tryFind(hostRequests, new com.google.common.base.Predicate<org.apache.ambari.server.topology.HostRequest>() {
            @java.lang.Override
            public boolean apply(@javax.annotation.Nullable
            org.apache.ambari.server.topology.HostRequest input) {
                return host1.getFqdn().equals(input.getHostName());
            }
        });
        com.google.common.base.Optional<org.apache.ambari.server.topology.HostRequest> hostReqHost2 = com.google.common.collect.Iterables.tryFind(hostRequests, new com.google.common.base.Predicate<org.apache.ambari.server.topology.HostRequest>() {
            @java.lang.Override
            public boolean apply(@javax.annotation.Nullable
            org.apache.ambari.server.topology.HostRequest input) {
                return host2.getFqdn().equals(input.getHostName());
            }
        });
        com.google.common.base.Optional<org.apache.ambari.server.topology.HostRequest> hostReqHost3 = com.google.common.collect.Iterables.tryFind(hostRequests, new com.google.common.base.Predicate<org.apache.ambari.server.topology.HostRequest>() {
            @java.lang.Override
            public boolean apply(@javax.annotation.Nullable
            org.apache.ambari.server.topology.HostRequest input) {
                return input.getHostName() == null;
            }
        });
        com.google.common.base.Optional<org.apache.ambari.server.topology.HostRequest> hostReqHost4 = com.google.common.collect.Iterables.tryFind(hostRequests, new com.google.common.base.Predicate<org.apache.ambari.server.topology.HostRequest>() {
            @java.lang.Override
            public boolean apply(@javax.annotation.Nullable
            org.apache.ambari.server.topology.HostRequest input) {
                return host4.getFqdn().equals(input.getHostName());
            }
        });
        org.junit.Assert.assertTrue(((hostReqHost1.isPresent() && hostReqHost2.isPresent()) && hostReqHost3.isPresent()) && (!hostReqHost4.isPresent()));
    }

    @org.junit.Test
    public void testRemovePendingHostRequests() throws java.lang.Exception {
        java.lang.Long requestId = 1L;
        final org.apache.ambari.server.orm.entities.TopologyHostInfoEntity host1 = new org.apache.ambari.server.orm.entities.TopologyHostInfoEntity();
        host1.setId(100L);
        host1.setFqdn("host1");
        final org.apache.ambari.server.orm.entities.TopologyHostInfoEntity host2 = new org.apache.ambari.server.orm.entities.TopologyHostInfoEntity();
        host2.setId(102L);
        host2.setFqdn("host2");
        org.apache.ambari.server.orm.entities.TopologyHostGroupEntity hostGroupEntity1 = new org.apache.ambari.server.orm.entities.TopologyHostGroupEntity();
        hostGroupEntity1.setTopologyHostInfoEntities(com.google.common.collect.ImmutableSet.of(host1, host2));
        hostGroupEntity1.setName("host_group_1");
        org.apache.ambari.server.orm.entities.TopologyHostRequestEntity hostRequestEntityHost1Matched = new org.apache.ambari.server.orm.entities.TopologyHostRequestEntity();
        hostRequestEntityHost1Matched.setId(1L);
        hostRequestEntityHost1Matched.setHostName(host1.getFqdn());
        hostRequestEntityHost1Matched.setTopologyHostGroupEntity(hostGroupEntity1);
        hostRequestEntityHost1Matched.setTopologyHostTaskEntities(java.util.Collections.emptySet());
        EasyMock.expect(ambariContext.isHostRegisteredWithCluster(EasyMock.eq(clusterId), EasyMock.eq(host1.getFqdn()))).andReturn(true).anyTimes();
        org.apache.ambari.server.orm.entities.TopologyHostRequestEntity hostRequestEntityHost2NotMatched = new org.apache.ambari.server.orm.entities.TopologyHostRequestEntity();
        hostRequestEntityHost2NotMatched.setId(2L);
        hostRequestEntityHost2NotMatched.setTopologyHostGroupEntity(hostGroupEntity1);
        hostRequestEntityHost2NotMatched.setTopologyHostTaskEntities(java.util.Collections.emptySet());
        EasyMock.expect(ambariContext.isHostRegisteredWithCluster(EasyMock.eq(clusterId), EasyMock.eq(host2.getFqdn()))).andReturn(false).anyTimes();
        java.util.Collection<org.apache.ambari.server.orm.entities.TopologyHostRequestEntity> reservedHostRequestEntities = com.google.common.collect.ImmutableSet.of(hostRequestEntityHost1Matched, hostRequestEntityHost2NotMatched);
        hostGroupEntity1.setTopologyHostRequestEntities(reservedHostRequestEntities);
        org.apache.ambari.server.orm.entities.TopologyRequestEntity topologyRequestEntity = new org.apache.ambari.server.orm.entities.TopologyRequestEntity();
        topologyRequestEntity.setTopologyHostGroupEntities(java.util.Collections.singleton(hostGroupEntity1));
        EasyMock.expect(logicalRequestEntity.getTopologyRequestEntity()).andReturn(topologyRequestEntity).atLeastOnce();
        EasyMock.expect(logicalRequestEntity.getTopologyHostRequestEntities()).andReturn(reservedHostRequestEntities).atLeastOnce();
        EasyMock.expect(blueprint.getHostGroup(EasyMock.eq("host_group_1"))).andReturn(hostGroup1).atLeastOnce();
        EasyMock.expect(hostGroup1.containsMasterComponent()).andReturn(false).atLeastOnce();
        replayAll();
        org.apache.ambari.server.topology.LogicalRequest req = new org.apache.ambari.server.topology.LogicalRequest(requestId, replayedTopologyRequest, clusterTopology, logicalRequestEntity);
        req.removePendingHostRequests(null);
        verifyAll();
        org.junit.Assert.assertEquals(1, req.getHostRequests().size());
        org.junit.Assert.assertEquals(0, req.getPendingHostRequestCount());
    }

    @org.junit.Test
    public void testRemovePendingHostRequestsByHostCount() throws java.lang.Exception {
        int hostCount = 3;
        org.apache.ambari.server.topology.LogicalRequest req = createTopologyRequestByHostCount(hostCount, "host_group");
        org.junit.Assert.assertEquals(hostCount, req.getPendingHostRequestCount());
        req.removePendingHostRequests(null);
        org.junit.Assert.assertEquals(0, req.getPendingHostRequestCount());
        verifyAll();
    }

    @org.junit.Test
    public void testRemovePendingHostRequestsOfSpecificHostGroupByHostCount() throws java.lang.Exception {
        int hostCount = 3;
        java.lang.String hostGroupName = "host_group";
        org.apache.ambari.server.topology.LogicalRequest req = createTopologyRequestByHostCount(hostCount, hostGroupName);
        org.junit.Assert.assertEquals(hostCount, req.getPendingHostRequestCount());
        req.removePendingHostRequests(hostGroupName);
        org.junit.Assert.assertEquals(0, req.getPendingHostRequestCount());
        verifyAll();
    }

    @org.junit.Test
    public void testRemovePendingHostRequestsOfNonexistentHostGroupByHostCount() throws java.lang.Exception {
        int hostCount = 3;
        org.apache.ambari.server.topology.LogicalRequest req = createTopologyRequestByHostCount(hostCount, "host_group");
        org.junit.Assert.assertEquals(hostCount, req.getPendingHostRequestCount());
        req.removePendingHostRequests("no_such_host_group");
        org.junit.Assert.assertEquals(hostCount, req.getPendingHostRequestCount());
        verifyAll();
    }

    private org.apache.ambari.server.topology.LogicalRequest createTopologyRequestByHostCount(int hostCount, java.lang.String hostGroupName) throws java.lang.Exception {
        final org.apache.ambari.server.orm.entities.TopologyHostInfoEntity hostInfo = new org.apache.ambari.server.orm.entities.TopologyHostInfoEntity();
        hostInfo.setId(100L);
        hostInfo.setHostCount(hostCount);
        org.apache.ambari.server.orm.entities.TopologyHostGroupEntity hostGroupEntity = new org.apache.ambari.server.orm.entities.TopologyHostGroupEntity();
        hostGroupEntity.setTopologyHostInfoEntities(com.google.common.collect.ImmutableSet.of(hostInfo));
        hostGroupEntity.setName(hostGroupName);
        org.apache.ambari.server.orm.entities.TopologyRequestEntity topologyRequestEntity = new org.apache.ambari.server.orm.entities.TopologyRequestEntity();
        topologyRequestEntity.setTopologyHostGroupEntities(java.util.Collections.singleton(hostGroupEntity));
        java.util.Set<org.apache.ambari.server.orm.entities.TopologyHostRequestEntity> hostRequests = new java.util.HashSet<>();
        for (long i = 0; i < hostCount; ++i) {
            org.apache.ambari.server.orm.entities.TopologyHostRequestEntity hostRequestEntity = new org.apache.ambari.server.orm.entities.TopologyHostRequestEntity();
            hostRequestEntity.setId(i);
            hostRequestEntity.setTopologyHostGroupEntity(hostGroupEntity);
            hostRequestEntity.setTopologyHostTaskEntities(java.util.Collections.emptySet());
            hostRequests.add(hostRequestEntity);
        }
        EasyMock.expect(logicalRequestEntity.getTopologyRequestEntity()).andReturn(topologyRequestEntity).anyTimes();
        EasyMock.expect(logicalRequestEntity.getTopologyHostRequestEntities()).andReturn(hostRequests).anyTimes();
        EasyMock.expect(blueprint.getHostGroup(EasyMock.eq(hostGroupEntity.getName()))).andReturn(hostGroup1).anyTimes();
        EasyMock.expect(hostGroup1.containsMasterComponent()).andReturn(false).anyTimes();
        replayAll();
        return new org.apache.ambari.server.topology.LogicalRequest(1L, replayedTopologyRequest, clusterTopology, logicalRequestEntity);
    }
}