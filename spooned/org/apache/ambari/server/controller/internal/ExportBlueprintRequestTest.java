package org.apache.ambari.server.controller.internal;
import static org.easymock.EasyMock.anyObject;
import static org.easymock.EasyMock.createNiceMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
public class ExportBlueprintRequestTest {
    private static final java.lang.String CLUSTER_NAME = "c1";

    private static final java.lang.String CLUSTER_ID = "2";

    @org.junit.Test
    public void testExport_noConfigs() throws java.lang.Exception {
        org.apache.ambari.server.controller.AmbariManagementController controller = EasyMock.createNiceMock(org.apache.ambari.server.controller.AmbariManagementController.class);
        EasyMock.expect(controller.getStackServices(EasyMock.anyObject())).andReturn(java.util.Collections.emptySet()).anyTimes();
        EasyMock.expect(controller.getStackLevelConfigurations(EasyMock.anyObject())).andReturn(java.util.Collections.emptySet()).anyTimes();
        EasyMock.replay(controller);
        org.apache.ambari.server.controller.spi.Resource clusterResource = new org.apache.ambari.server.controller.internal.ResourceImpl(org.apache.ambari.server.controller.spi.Resource.Type.Cluster);
        clusterResource.setProperty(org.apache.ambari.server.controller.internal.ClusterResourceProvider.CLUSTER_NAME_PROPERTY_ID, org.apache.ambari.server.controller.internal.ExportBlueprintRequestTest.CLUSTER_NAME);
        clusterResource.setProperty(org.apache.ambari.server.controller.internal.ClusterResourceProvider.CLUSTER_ID_PROPERTY_ID, org.apache.ambari.server.controller.internal.ExportBlueprintRequestTest.CLUSTER_ID);
        clusterResource.setProperty(org.apache.ambari.server.controller.internal.ClusterResourceProvider.CLUSTER_VERSION_PROPERTY_ID, "TEST-1.0");
        org.apache.ambari.server.api.util.TreeNode<org.apache.ambari.server.controller.spi.Resource> clusterNode = new org.apache.ambari.server.api.util.TreeNodeImpl<>(null, clusterResource, "cluster");
        org.apache.ambari.server.controller.spi.Resource configResource = new org.apache.ambari.server.controller.internal.ResourceImpl(org.apache.ambari.server.controller.spi.Resource.Type.Configuration);
        clusterNode.addChild(configResource, "configurations");
        org.apache.ambari.server.controller.spi.Resource hostsResource = new org.apache.ambari.server.controller.internal.ResourceImpl(org.apache.ambari.server.controller.spi.Resource.Type.Host);
        org.apache.ambari.server.controller.spi.Resource host1Resource = new org.apache.ambari.server.controller.internal.ResourceImpl(org.apache.ambari.server.controller.spi.Resource.Type.Host);
        org.apache.ambari.server.controller.spi.Resource host2Resource = new org.apache.ambari.server.controller.internal.ResourceImpl(org.apache.ambari.server.controller.spi.Resource.Type.Host);
        org.apache.ambari.server.controller.spi.Resource host3Resource = new org.apache.ambari.server.controller.internal.ResourceImpl(org.apache.ambari.server.controller.spi.Resource.Type.Host);
        org.apache.ambari.server.api.util.TreeNode<org.apache.ambari.server.controller.spi.Resource> hostsNode = clusterNode.addChild(hostsResource, "hosts");
        org.apache.ambari.server.api.util.TreeNode<org.apache.ambari.server.controller.spi.Resource> host1Node = hostsNode.addChild(host1Resource, "host_1");
        org.apache.ambari.server.api.util.TreeNode<org.apache.ambari.server.controller.spi.Resource> host2Node = hostsNode.addChild(host2Resource, "host_2");
        org.apache.ambari.server.api.util.TreeNode<org.apache.ambari.server.controller.spi.Resource> host3Node = hostsNode.addChild(host3Resource, "host_3");
        host1Resource.setProperty("Hosts/host_name", "host1");
        host2Resource.setProperty("Hosts/host_name", "host2");
        host3Resource.setProperty("Hosts/host_name", "host3");
        java.util.List<java.lang.String> host1ComponentsList = java.util.Arrays.asList("NAMENODE", "HDFS_CLIENT", "ZOOKEEPER_SERVER", "SECONDARY_NAMENODE");
        java.util.List<java.lang.String> host2ComponentsList = java.util.Arrays.asList("DATANODE", "HDFS_CLIENT", "ZOOKEEPER_SERVER");
        java.util.List<java.lang.String> host3ComponentsList = java.util.Arrays.asList("DATANODE", "HDFS_CLIENT", "ZOOKEEPER_SERVER");
        processHostGroupComponents(host1Node, host1ComponentsList);
        processHostGroupComponents(host2Node, host2ComponentsList);
        processHostGroupComponents(host3Node, host3ComponentsList);
        org.apache.ambari.server.controller.internal.ExportBlueprintRequest exportBlueprintRequest = new org.apache.ambari.server.controller.internal.ExportBlueprintRequest(clusterNode, controller);
        org.junit.Assert.assertEquals(org.apache.ambari.server.controller.internal.ExportBlueprintRequestTest.CLUSTER_NAME, exportBlueprintRequest.getClusterName());
        org.apache.ambari.server.topology.Blueprint bp = exportBlueprintRequest.getBlueprint();
        org.junit.Assert.assertEquals("exported-blueprint", bp.getName());
        java.util.Map<java.lang.String, org.apache.ambari.server.topology.HostGroup> hostGroups = bp.getHostGroups();
        org.junit.Assert.assertEquals(2, hostGroups.size());
        java.lang.String hg1Name = null;
        java.lang.String hg2Name = null;
        for (org.apache.ambari.server.topology.HostGroup group : hostGroups.values()) {
            java.util.Collection<java.lang.String> components = group.getComponentNames();
            if (components.containsAll(host1ComponentsList)) {
                org.junit.Assert.assertEquals(host1ComponentsList.size(), components.size());
                org.junit.Assert.assertEquals("1", group.getCardinality());
                hg1Name = group.getName();
            } else if (components.containsAll(host2ComponentsList)) {
                org.junit.Assert.assertEquals(host2ComponentsList.size(), components.size());
                org.junit.Assert.assertEquals("2", group.getCardinality());
                hg2Name = group.getName();
            } else {
                org.junit.Assert.fail("Host group contained invalid components");
            }
        }
        org.junit.Assert.assertNotNull(hg1Name);
        org.junit.Assert.assertNotNull(hg2Name);
        org.apache.ambari.server.topology.HostGroupInfo host1Info = exportBlueprintRequest.getHostGroupInfo().get(hg1Name);
        org.junit.Assert.assertEquals(1, host1Info.getHostNames().size());
        org.junit.Assert.assertEquals("host1", host1Info.getHostNames().iterator().next());
        org.apache.ambari.server.topology.HostGroupInfo host2Info = exportBlueprintRequest.getHostGroupInfo().get(hg2Name);
        org.junit.Assert.assertEquals(2, host2Info.getHostNames().size());
        org.junit.Assert.assertTrue(host2Info.getHostNames().contains("host2") && host2Info.getHostNames().contains("host3"));
    }

    private void processHostGroupComponents(org.apache.ambari.server.api.util.TreeNode<org.apache.ambari.server.controller.spi.Resource> hostNode, java.util.Collection<java.lang.String> components) {
        org.apache.ambari.server.controller.spi.Resource hostComponentsResource = new org.apache.ambari.server.controller.internal.ResourceImpl(org.apache.ambari.server.controller.spi.Resource.Type.HostComponent);
        org.apache.ambari.server.api.util.TreeNode<org.apache.ambari.server.controller.spi.Resource> hostComponentsNode = hostNode.addChild(hostComponentsResource, "host_components");
        int componentCount = 1;
        for (java.lang.String component : components) {
            org.apache.ambari.server.controller.spi.Resource componentResource = new org.apache.ambari.server.controller.internal.ResourceImpl(org.apache.ambari.server.controller.spi.Resource.Type.HostComponent);
            componentResource.setProperty("HostRoles/component_name", component);
            hostComponentsNode.addChild(componentResource, "host_component_" + (componentCount++));
        }
    }
}