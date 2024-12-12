package org.apache.ambari.server.state;
import static org.easymock.EasyMock.anyString;
import static org.easymock.EasyMock.createNiceMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
public class ConfigMergeHelperTest {
    private static final org.apache.ambari.server.state.StackId currentStackId = new org.apache.ambari.server.state.StackId("HDP-2.1.1");

    private static final org.apache.ambari.server.state.StackId newStackId = new org.apache.ambari.server.state.StackId("HPD-2.2.0");

    private com.google.inject.Injector injector;

    private org.apache.ambari.server.state.Clusters clustersMock;

    private org.apache.ambari.server.api.services.AmbariMetaInfo ambariMetaInfoMock;

    @org.junit.Before
    public void before() throws java.lang.Exception {
        clustersMock = EasyMock.createNiceMock(org.apache.ambari.server.state.Clusters.class);
        ambariMetaInfoMock = EasyMock.createNiceMock(org.apache.ambari.server.api.services.AmbariMetaInfo.class);
        final org.apache.ambari.server.orm.InMemoryDefaultTestModule injectorModule = new org.apache.ambari.server.orm.InMemoryDefaultTestModule() {
            @java.lang.Override
            protected void configure() {
                super.configure();
            }
        };
        org.apache.ambari.server.state.ConfigMergeHelperTest.MockModule mockModule = new org.apache.ambari.server.state.ConfigMergeHelperTest.MockModule();
        injector = com.google.inject.Guice.createInjector(com.google.inject.util.Modules.override(injectorModule).with(mockModule));
        injector.getInstance(org.apache.ambari.server.orm.GuiceJpaInitializer.class);
    }

    @org.junit.After
    public void teardown() throws org.apache.ambari.server.AmbariException, java.sql.SQLException {
        org.apache.ambari.server.H2DatabaseCleaner.clearDatabaseAndStopPersistenceService(injector);
    }

    @org.junit.Test
    public void testGetConflicts() throws java.lang.Exception {
        org.apache.ambari.server.state.Cluster clusterMock = EasyMock.createNiceMock(org.apache.ambari.server.state.Cluster.class);
        EasyMock.expect(clustersMock.getCluster(EasyMock.anyString())).andReturn(clusterMock);
        EasyMock.expect(clusterMock.getCurrentStackVersion()).andReturn(org.apache.ambari.server.state.ConfigMergeHelperTest.currentStackId);
        EasyMock.expect(clusterMock.getServices()).andReturn(new java.util.HashMap<java.lang.String, org.apache.ambari.server.state.Service>() {
            {
                put("HDFS", EasyMock.createNiceMock(org.apache.ambari.server.state.Service.class));
                put("ZK", EasyMock.createNiceMock(org.apache.ambari.server.state.Service.class));
            }
        });
        java.util.Set<org.apache.ambari.server.state.PropertyInfo> currentHDFSProperties = new java.util.HashSet<org.apache.ambari.server.state.PropertyInfo>() {
            {
                add(createPropertyInfo("hdfs-env.xml", "equal.key", "equal-value"));
            }
        };
        java.util.Set<org.apache.ambari.server.state.PropertyInfo> currentZKProperties = new java.util.HashSet<org.apache.ambari.server.state.PropertyInfo>() {
            {
                add(createPropertyInfo("zk-env.xml", "different.key", "different-value-1"));
            }
        };
        java.util.Set<org.apache.ambari.server.state.PropertyInfo> currentStackProperties = new java.util.HashSet<org.apache.ambari.server.state.PropertyInfo>() {
            {
                add(createPropertyInfo("hadoop-env.xml", "equal.key", "modified.value"));
            }
        };
        EasyMock.expect(ambariMetaInfoMock.getServiceProperties(org.apache.ambari.server.state.ConfigMergeHelperTest.currentStackId.getStackName(), org.apache.ambari.server.state.ConfigMergeHelperTest.currentStackId.getStackVersion(), "HDFS")).andReturn(currentHDFSProperties);
        EasyMock.expect(ambariMetaInfoMock.getServiceProperties(org.apache.ambari.server.state.ConfigMergeHelperTest.currentStackId.getStackName(), org.apache.ambari.server.state.ConfigMergeHelperTest.currentStackId.getStackVersion(), "ZK")).andReturn(currentZKProperties);
        EasyMock.expect(ambariMetaInfoMock.getStackProperties(org.apache.ambari.server.state.ConfigMergeHelperTest.currentStackId.getStackName(), org.apache.ambari.server.state.ConfigMergeHelperTest.currentStackId.getStackVersion())).andReturn(currentStackProperties);
        java.util.Set<org.apache.ambari.server.state.PropertyInfo> newHDFSProperties = new java.util.HashSet<org.apache.ambari.server.state.PropertyInfo>() {
            {
                add(createPropertyInfo("hdfs-env.xml", "equal.key", "equal-value"));
                add(createPropertyInfo("new-hdfs-config.xml", "equal.key", "equal-value"));
            }
        };
        java.util.Set<org.apache.ambari.server.state.PropertyInfo> newZKProperties = new java.util.HashSet<org.apache.ambari.server.state.PropertyInfo>() {
            {
                add(createPropertyInfo("zk-env.xml", "equal.key", "different-value-2"));
                add(createPropertyInfo("zk-env.xml", "new.key", "new-value-2"));
            }
        };
        java.util.Set<org.apache.ambari.server.state.PropertyInfo> newStackProperties = new java.util.HashSet<org.apache.ambari.server.state.PropertyInfo>() {
            {
                add(createPropertyInfo("hadoop-env.xml", "equal.key", "another.value"));
            }
        };
        EasyMock.expect(ambariMetaInfoMock.getServiceProperties(org.apache.ambari.server.state.ConfigMergeHelperTest.newStackId.getStackName(), org.apache.ambari.server.state.ConfigMergeHelperTest.newStackId.getStackVersion(), "HDFS")).andReturn(newHDFSProperties);
        EasyMock.expect(ambariMetaInfoMock.getServiceProperties(org.apache.ambari.server.state.ConfigMergeHelperTest.newStackId.getStackName(), org.apache.ambari.server.state.ConfigMergeHelperTest.newStackId.getStackVersion(), "ZK")).andReturn(newZKProperties);
        EasyMock.expect(ambariMetaInfoMock.getStackProperties(org.apache.ambari.server.state.ConfigMergeHelperTest.newStackId.getStackName(), org.apache.ambari.server.state.ConfigMergeHelperTest.newStackId.getStackVersion())).andReturn(newStackProperties);
        java.util.Map<java.lang.String, java.lang.String> desiredHdfsEnvProperties = new java.util.HashMap<>();
        EasyMock.expect(clusterMock.getDesiredConfigByType("hdfs-env.xml")).andReturn(createConfigMock(desiredHdfsEnvProperties));
        java.util.Map<java.lang.String, java.lang.String> desiredZkEnvProperties = new java.util.HashMap<>();
        EasyMock.expect(clusterMock.getDesiredConfigByType("hdfs-env.xml")).andReturn(createConfigMock(desiredZkEnvProperties));
        java.util.Map<java.lang.String, java.lang.String> desiredHadoopEnvProperties = new java.util.HashMap<>();
        EasyMock.expect(clusterMock.getDesiredConfigByType("hadoop-env.xml")).andReturn(createConfigMock(desiredHadoopEnvProperties));
        EasyMock.replay(clusterMock, clustersMock, ambariMetaInfoMock);
        org.apache.ambari.server.state.ConfigMergeHelper configMergeHelper = injector.getInstance(org.apache.ambari.server.state.ConfigMergeHelper.class);
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, org.apache.ambari.server.state.ConfigMergeHelper.ThreeWayValue>> conflicts = configMergeHelper.getConflicts("clustername", org.apache.ambari.server.state.ConfigMergeHelperTest.newStackId);
        org.junit.Assert.assertNotNull(conflicts);
        org.junit.Assert.assertEquals(2, conflicts.size());
        for (java.lang.String key : conflicts.keySet()) {
            if (key.equals("hdfs-env")) {
                java.util.Map<java.lang.String, org.apache.ambari.server.state.ConfigMergeHelper.ThreeWayValue> stringThreeWayValueMap = conflicts.get(key);
                org.junit.Assert.assertEquals(1, stringThreeWayValueMap.size());
                org.junit.Assert.assertEquals("equal-value", stringThreeWayValueMap.get("equal.key").oldStackValue);
                org.junit.Assert.assertEquals("equal-value", stringThreeWayValueMap.get("equal.key").newStackValue);
                org.junit.Assert.assertEquals("", stringThreeWayValueMap.get("equal.key").savedValue);
            } else if (key.equals("hadoop-env")) {
                java.util.Map<java.lang.String, org.apache.ambari.server.state.ConfigMergeHelper.ThreeWayValue> stringThreeWayValueMap = conflicts.get(key);
                org.junit.Assert.assertEquals(1, stringThreeWayValueMap.size());
                org.junit.Assert.assertEquals("modified.value", stringThreeWayValueMap.get("equal.key").oldStackValue);
                org.junit.Assert.assertEquals("another.value", stringThreeWayValueMap.get("equal.key").newStackValue);
                org.junit.Assert.assertEquals("", stringThreeWayValueMap.get("equal.key").savedValue);
            } else {
                org.junit.Assert.fail("Unexpected key");
            }
        }
        org.junit.Assert.assertEquals(2, conflicts.size());
    }

    private org.apache.ambari.server.state.PropertyInfo createPropertyInfo(java.lang.String fileName, java.lang.String name, java.lang.String value) {
        org.apache.ambari.server.state.PropertyInfo result = new org.apache.ambari.server.state.PropertyInfo();
        result.setFilename(fileName);
        result.setName(name);
        result.setValue(value);
        return result;
    }

    private org.apache.ambari.server.state.Config createConfigMock(java.util.Map<java.lang.String, java.lang.String> properties) {
        org.apache.ambari.server.state.Config result = EasyMock.createNiceMock(org.apache.ambari.server.state.Config.class);
        EasyMock.expect(result.getProperties()).andReturn(properties);
        return result;
    }

    @org.junit.Test
    public void testNormalizeValue() throws java.lang.Exception {
        java.lang.String normalizedValue = org.apache.ambari.server.state.ConfigMergeHelper.normalizeValue(null, "2048m");
        org.junit.Assert.assertEquals("2048m", normalizedValue);
        normalizedValue = org.apache.ambari.server.state.ConfigMergeHelper.normalizeValue("3k", "2048");
        org.junit.Assert.assertEquals("2048", normalizedValue);
        normalizedValue = org.apache.ambari.server.state.ConfigMergeHelper.normalizeValue("1024m", "2048");
        org.junit.Assert.assertEquals("2048m", normalizedValue);
        normalizedValue = org.apache.ambari.server.state.ConfigMergeHelper.normalizeValue("1024M", "2048");
        org.junit.Assert.assertEquals("2048M", normalizedValue);
        normalizedValue = org.apache.ambari.server.state.ConfigMergeHelper.normalizeValue("4g", "2");
        org.junit.Assert.assertEquals("2g", normalizedValue);
        normalizedValue = org.apache.ambari.server.state.ConfigMergeHelper.normalizeValue("4G", "2");
        org.junit.Assert.assertEquals("2G", normalizedValue);
    }

    private class MockModule implements com.google.inject.Module {
        @java.lang.Override
        public void configure(com.google.inject.Binder binder) {
            binder.bind(org.apache.ambari.server.state.Clusters.class).toInstance(clustersMock);
            binder.bind(org.apache.ambari.server.api.services.AmbariMetaInfo.class).toInstance(ambariMetaInfoMock);
        }
    }
}