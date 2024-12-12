package org.apache.ambari.server.agent.stomp.dto;
public class MetadataClusterTest {
    @org.junit.Test
    public void shouldReturnFalseWhenUpdatingServiceLevelParamsWithoutNewOrRemovedServices() throws java.lang.Exception {
        final java.util.SortedMap<java.lang.String, org.apache.ambari.server.agent.stomp.dto.MetadataServiceInfo> current = new java.util.TreeMap<>();
        current.put("service1", new org.apache.ambari.server.agent.stomp.dto.MetadataServiceInfo("v1", java.lang.Boolean.FALSE, null, 1L, "servicePackageFolder"));
        current.put("service2", new org.apache.ambari.server.agent.stomp.dto.MetadataServiceInfo("v1", java.lang.Boolean.FALSE, null, 1L, "servicePackageFolder"));
        current.put("service3", new org.apache.ambari.server.agent.stomp.dto.MetadataServiceInfo("v1", java.lang.Boolean.FALSE, null, 1L, "servicePackageFolder"));
        final org.apache.ambari.server.agent.stomp.dto.MetadataCluster metadataCluster = org.apache.ambari.server.agent.stomp.dto.MetadataCluster.serviceLevelParamsMetadataCluster(null, current, true);
        final java.util.SortedMap<java.lang.String, org.apache.ambari.server.agent.stomp.dto.MetadataServiceInfo> updated = new java.util.TreeMap<>(current);
        org.junit.Assert.assertFalse(metadataCluster.updateServiceLevelParams(updated, true));
        org.junit.Assert.assertEquals(current, metadataCluster.getServiceLevelParams());
    }

    @org.junit.Test
    public void shouldReturnTrueWhenUpdatingServiceLevelParamsUponServiceAddition() throws java.lang.Exception {
        final java.util.SortedMap<java.lang.String, org.apache.ambari.server.agent.stomp.dto.MetadataServiceInfo> current = new java.util.TreeMap<>();
        current.put("service1", new org.apache.ambari.server.agent.stomp.dto.MetadataServiceInfo("v1", java.lang.Boolean.FALSE, null, 1L, "servicePackageFolder"));
        current.put("service2", new org.apache.ambari.server.agent.stomp.dto.MetadataServiceInfo("v1", java.lang.Boolean.FALSE, null, 1L, "servicePackageFolder"));
        final org.apache.ambari.server.agent.stomp.dto.MetadataCluster metadataCluster = org.apache.ambari.server.agent.stomp.dto.MetadataCluster.serviceLevelParamsMetadataCluster(null, current, true);
        final java.util.SortedMap<java.lang.String, org.apache.ambari.server.agent.stomp.dto.MetadataServiceInfo> updated = new java.util.TreeMap<>(current);
        updated.put("service3", new org.apache.ambari.server.agent.stomp.dto.MetadataServiceInfo("v1", java.lang.Boolean.FALSE, null, 1L, "servicePackageFolder"));
        org.junit.Assert.assertTrue(metadataCluster.updateServiceLevelParams(updated, true));
        org.junit.Assert.assertEquals(updated, metadataCluster.getServiceLevelParams());
    }

    @org.junit.Test
    public void shouldReturnTrueWhenUpdatingServiceLevelParamsUponServiceRemoval() throws java.lang.Exception {
        final java.util.SortedMap<java.lang.String, org.apache.ambari.server.agent.stomp.dto.MetadataServiceInfo> current = new java.util.TreeMap<>();
        current.put("service1", new org.apache.ambari.server.agent.stomp.dto.MetadataServiceInfo("v1", java.lang.Boolean.FALSE, null, 1L, "servicePackageFolder"));
        current.put("service2", new org.apache.ambari.server.agent.stomp.dto.MetadataServiceInfo("v1", java.lang.Boolean.FALSE, null, 1L, "servicePackageFolder"));
        current.put("service3", new org.apache.ambari.server.agent.stomp.dto.MetadataServiceInfo("v1", java.lang.Boolean.FALSE, null, 1L, "servicePackageFolder"));
        final org.apache.ambari.server.agent.stomp.dto.MetadataCluster metadataCluster = org.apache.ambari.server.agent.stomp.dto.MetadataCluster.serviceLevelParamsMetadataCluster(null, current, true);
        final java.util.SortedMap<java.lang.String, org.apache.ambari.server.agent.stomp.dto.MetadataServiceInfo> updated = new java.util.TreeMap<>(current);
        updated.remove("service2");
        org.junit.Assert.assertTrue(metadataCluster.updateServiceLevelParams(updated, true));
        org.junit.Assert.assertEquals(updated, metadataCluster.getServiceLevelParams());
    }

    @org.junit.Test
    public void shouldReturnFalseWhenNullServiceLevelParamsArePassedBecauseOfPartialConfigurationUpdate() throws java.lang.Exception {
        final java.util.SortedMap<java.lang.String, org.apache.ambari.server.agent.stomp.dto.MetadataServiceInfo> current = new java.util.TreeMap<>();
        current.put("service1", new org.apache.ambari.server.agent.stomp.dto.MetadataServiceInfo("v1", java.lang.Boolean.FALSE, null, 1L, "servicePackageFolder"));
        current.put("service2", new org.apache.ambari.server.agent.stomp.dto.MetadataServiceInfo("v1", java.lang.Boolean.FALSE, null, 1L, "servicePackageFolder"));
        current.put("service3", new org.apache.ambari.server.agent.stomp.dto.MetadataServiceInfo("v1", java.lang.Boolean.FALSE, null, 1L, "servicePackageFolder"));
        final org.apache.ambari.server.agent.stomp.dto.MetadataCluster metadataCluster = org.apache.ambari.server.agent.stomp.dto.MetadataCluster.serviceLevelParamsMetadataCluster(null, current, true);
        org.junit.Assert.assertFalse(metadataCluster.updateServiceLevelParams(null, true));
        org.junit.Assert.assertEquals(current, metadataCluster.getServiceLevelParams());
    }

    @org.junit.Test
    public void shouldReturnTrueWhenUpdatingServiceLevelParamsWithoutFullServiceLevelMetadata() throws java.lang.Exception {
        final java.util.SortedMap<java.lang.String, org.apache.ambari.server.agent.stomp.dto.MetadataServiceInfo> current = new java.util.TreeMap<>();
        current.put("service1", new org.apache.ambari.server.agent.stomp.dto.MetadataServiceInfo("v1", java.lang.Boolean.FALSE, null, 1L, "servicePackageFolder"));
        current.put("service2", new org.apache.ambari.server.agent.stomp.dto.MetadataServiceInfo("v1", java.lang.Boolean.FALSE, null, 1L, "servicePackageFolder"));
        current.put("service3", new org.apache.ambari.server.agent.stomp.dto.MetadataServiceInfo("v1", java.lang.Boolean.FALSE, null, 1L, "servicePackageFolder"));
        final org.apache.ambari.server.agent.stomp.dto.MetadataCluster metadataCluster = org.apache.ambari.server.agent.stomp.dto.MetadataCluster.serviceLevelParamsMetadataCluster(null, current, true);
        final java.util.SortedMap<java.lang.String, org.apache.ambari.server.agent.stomp.dto.MetadataServiceInfo> updated = new java.util.TreeMap<>();
        updated.put("service3", new org.apache.ambari.server.agent.stomp.dto.MetadataServiceInfo("v2", java.lang.Boolean.TRUE, null, 2L, "servicePackageFolder2"));
        updated.put("service4", new org.apache.ambari.server.agent.stomp.dto.MetadataServiceInfo("v1", java.lang.Boolean.FALSE, null, 1L, "servicePackageFolder"));
        org.junit.Assert.assertTrue(metadataCluster.updateServiceLevelParams(updated, false));
        final java.util.SortedMap<java.lang.String, org.apache.ambari.server.agent.stomp.dto.MetadataServiceInfo> expected = current;
        expected.putAll(updated);
        org.junit.Assert.assertEquals(expected, metadataCluster.getServiceLevelParams());
    }

    @org.junit.Test
    public void shouldReturnFalseWhenUpdatingClusterLevelParamsWithoutClusterLevelParameterAdditionOrRemoval() throws java.lang.Exception {
        final java.util.SortedMap<java.lang.String, java.lang.String> current = new java.util.TreeMap<>();
        current.put("param1", "value1");
        current.put("param2", "value2");
        current.put("param3", "value3");
        final org.apache.ambari.server.agent.stomp.dto.MetadataCluster metadataCluster = org.apache.ambari.server.agent.stomp.dto.MetadataCluster.clusterLevelParamsMetadataCluster(null, current);
        final java.util.SortedMap<java.lang.String, java.lang.String> updated = new java.util.TreeMap<>(current);
        org.junit.Assert.assertFalse(metadataCluster.updateClusterLevelParams(updated));
        org.junit.Assert.assertEquals(current, metadataCluster.getClusterLevelParams());
    }

    @org.junit.Test
    public void shouldReturnTrueWhenUpdatingClusterLevelParamsUponClusterLevelParameterAddition() throws java.lang.Exception {
        final java.util.SortedMap<java.lang.String, java.lang.String> current = new java.util.TreeMap<>();
        current.put("param1", "value1");
        current.put("param2", "value2");
        final org.apache.ambari.server.agent.stomp.dto.MetadataCluster metadataCluster = org.apache.ambari.server.agent.stomp.dto.MetadataCluster.clusterLevelParamsMetadataCluster(null, current);
        final java.util.SortedMap<java.lang.String, java.lang.String> updated = new java.util.TreeMap<>(current);
        updated.put("param3", "value3");
        org.junit.Assert.assertTrue(metadataCluster.updateClusterLevelParams(updated));
        org.junit.Assert.assertEquals(updated, metadataCluster.getClusterLevelParams());
    }

    @org.junit.Test
    public void shouldReturnTrueWhenUpdatingClusterLevelParamsUponClusterLevelParameterRemoval() throws java.lang.Exception {
        final java.util.SortedMap<java.lang.String, java.lang.String> current = new java.util.TreeMap<>();
        current.put("param1", "value1");
        current.put("param2", "value2");
        current.put("param3", "value3");
        final org.apache.ambari.server.agent.stomp.dto.MetadataCluster metadataCluster = org.apache.ambari.server.agent.stomp.dto.MetadataCluster.clusterLevelParamsMetadataCluster(null, current);
        final java.util.SortedMap<java.lang.String, java.lang.String> updated = new java.util.TreeMap<>(current);
        updated.remove("param2");
        org.junit.Assert.assertTrue(metadataCluster.updateClusterLevelParams(updated));
        org.junit.Assert.assertEquals(updated, metadataCluster.getClusterLevelParams());
    }

    @org.junit.Test
    public void shouldReturnFalseWhenNullClusterLevelParamsArePassedBecauseOfPartialConfigurationUpdate() throws java.lang.Exception {
        final java.util.SortedMap<java.lang.String, java.lang.String> current = new java.util.TreeMap<>();
        current.put("param1", "value1");
        current.put("param2", "value2");
        current.put("param3", "value3");
        final org.apache.ambari.server.agent.stomp.dto.MetadataCluster metadataCluster = org.apache.ambari.server.agent.stomp.dto.MetadataCluster.clusterLevelParamsMetadataCluster(null, current);
        org.junit.Assert.assertFalse(metadataCluster.updateClusterLevelParams(null));
        org.junit.Assert.assertEquals(current, metadataCluster.getClusterLevelParams());
    }
}