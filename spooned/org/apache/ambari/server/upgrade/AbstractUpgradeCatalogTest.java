package org.apache.ambari.server.upgrade;
import static org.easymock.EasyMock.anyObject;
import static org.easymock.EasyMock.anyString;
import static org.easymock.EasyMock.createNiceMock;
import static org.easymock.EasyMock.createStrictMock;
import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
public class AbstractUpgradeCatalogTest {
    private static final java.lang.String CONFIG_TYPE = "hdfs-site.xml";

    private final java.lang.String CLUSTER_NAME = "c1";

    private final java.lang.String SERVICE_NAME = "HDFS";

    private org.apache.ambari.server.controller.AmbariManagementController amc;

    private org.apache.ambari.server.state.ConfigHelper configHelper;

    private com.google.inject.Injector injector;

    private org.apache.ambari.server.state.Cluster cluster;

    private org.apache.ambari.server.state.Clusters clusters;

    private org.apache.ambari.server.state.ServiceInfo serviceInfo;

    private org.apache.ambari.server.state.Config oldConfig;

    private org.apache.ambari.server.upgrade.AbstractUpgradeCatalog upgradeCatalog;

    private java.util.HashMap<java.lang.String, java.lang.String> oldProperties;

    @org.junit.Before
    public void init() throws org.apache.ambari.server.AmbariException {
        injector = EasyMock.createNiceMock(com.google.inject.Injector.class);
        configHelper = EasyMock.createNiceMock(org.apache.ambari.server.state.ConfigHelper.class);
        amc = EasyMock.createNiceMock(org.apache.ambari.server.controller.AmbariManagementController.class);
        cluster = EasyMock.createNiceMock(org.apache.ambari.server.state.Cluster.class);
        clusters = EasyMock.createStrictMock(org.apache.ambari.server.state.Clusters.class);
        serviceInfo = EasyMock.createNiceMock(org.apache.ambari.server.state.ServiceInfo.class);
        oldConfig = EasyMock.createNiceMock(org.apache.ambari.server.state.Config.class);
        EasyMock.expect(injector.getInstance(org.apache.ambari.server.state.ConfigHelper.class)).andReturn(configHelper).anyTimes();
        EasyMock.expect(injector.getInstance(org.apache.ambari.server.controller.AmbariManagementController.class)).andReturn(amc).anyTimes();
        EasyMock.expect(injector.getInstance(org.apache.ambari.server.state.Clusters.class)).andReturn(clusters).anyTimes();
        java.util.HashMap<java.lang.String, org.apache.ambari.server.state.Cluster> clusterMap = new java.util.HashMap<>();
        clusterMap.put(CLUSTER_NAME, cluster);
        EasyMock.expect(clusters.getClusters()).andReturn(clusterMap).anyTimes();
        java.util.HashSet<org.apache.ambari.server.state.PropertyInfo> stackProperties = new java.util.HashSet<>();
        EasyMock.expect(configHelper.getStackProperties(cluster)).andReturn(stackProperties).anyTimes();
        java.util.HashMap<java.lang.String, org.apache.ambari.server.state.Service> serviceMap = new java.util.HashMap<>();
        serviceMap.put(SERVICE_NAME, null);
        EasyMock.expect(cluster.getServices()).andReturn(serviceMap).anyTimes();
        java.util.HashSet<org.apache.ambari.server.state.PropertyInfo> serviceProperties = new java.util.HashSet<>();
        serviceProperties.add(org.apache.ambari.server.upgrade.AbstractUpgradeCatalogTest.createProperty(org.apache.ambari.server.upgrade.AbstractUpgradeCatalogTest.CONFIG_TYPE, "prop1", true, false, false));
        serviceProperties.add(org.apache.ambari.server.upgrade.AbstractUpgradeCatalogTest.createProperty(org.apache.ambari.server.upgrade.AbstractUpgradeCatalogTest.CONFIG_TYPE, "prop2", false, true, false));
        serviceProperties.add(org.apache.ambari.server.upgrade.AbstractUpgradeCatalogTest.createProperty(org.apache.ambari.server.upgrade.AbstractUpgradeCatalogTest.CONFIG_TYPE, "prop3", false, false, true));
        serviceProperties.add(org.apache.ambari.server.upgrade.AbstractUpgradeCatalogTest.createProperty(org.apache.ambari.server.upgrade.AbstractUpgradeCatalogTest.CONFIG_TYPE, "prop4", true, false, false));
        EasyMock.expect(configHelper.getServiceProperties(cluster, SERVICE_NAME)).andReturn(serviceProperties).anyTimes();
        EasyMock.expect(configHelper.getPropertyValueFromStackDefinitions(cluster, "hdfs-site", "prop1")).andReturn("v1").anyTimes();
        EasyMock.expect(configHelper.getPropertyValueFromStackDefinitions(cluster, "hdfs-site", "prop2")).andReturn("v2").anyTimes();
        EasyMock.expect(configHelper.getPropertyValueFromStackDefinitions(cluster, "hdfs-site", "prop3")).andReturn("v3").anyTimes();
        EasyMock.expect(configHelper.getPropertyValueFromStackDefinitions(cluster, "hdfs-site", "prop4")).andReturn("v4").anyTimes();
        EasyMock.expect(configHelper.getPropertyOwnerService(EasyMock.eq(cluster), EasyMock.eq("hdfs-site"), EasyMock.anyString())).andReturn(serviceInfo).anyTimes();
        EasyMock.expect(serviceInfo.getName()).andReturn(SERVICE_NAME).anyTimes();
        EasyMock.expect(cluster.getDesiredConfigByType("hdfs-site")).andReturn(oldConfig).anyTimes();
        oldProperties = new java.util.HashMap<>();
        EasyMock.expect(oldConfig.getProperties()).andReturn(oldProperties).anyTimes();
        upgradeCatalog = new org.apache.ambari.server.upgrade.AbstractUpgradeCatalog(injector) {
            @java.lang.Override
            protected void executeDDLUpdates() throws org.apache.ambari.server.AmbariException, java.sql.SQLException {
            }

            @java.lang.Override
            protected void executePreDMLUpdates() throws org.apache.ambari.server.AmbariException, java.sql.SQLException {
            }

            @java.lang.Override
            protected void executeDMLUpdates() throws org.apache.ambari.server.AmbariException, java.sql.SQLException {
            }

            @java.lang.Override
            public java.lang.String getTargetVersion() {
                return null;
            }
        };
    }

    @org.junit.Test
    public void shouldAddConfigurationFromXml() throws org.apache.ambari.server.AmbariException {
        oldProperties.put("prop1", "v1-old");
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> tags = java.util.Collections.emptyMap();
        java.util.Map<java.lang.String, java.lang.String> mergedProperties = new java.util.HashMap<>();
        mergedProperties.put("prop1", "v1-old");
        mergedProperties.put("prop4", "v4");
        EasyMock.expect(amc.createConfig(EasyMock.eq(cluster), EasyMock.anyObject(org.apache.ambari.server.state.StackId.class), EasyMock.eq("hdfs-site"), EasyMock.eq(mergedProperties), EasyMock.anyString(), EasyMock.eq(tags))).andReturn(null);
        EasyMock.replay(injector, configHelper, amc, cluster, clusters, serviceInfo, oldConfig);
        upgradeCatalog.addNewConfigurationsFromXml();
        EasyMock.verify(configHelper, amc, cluster, clusters, serviceInfo, oldConfig);
    }

    @org.junit.Test
    public void shouldUpdateConfigurationFromXml() throws org.apache.ambari.server.AmbariException {
        oldProperties.put("prop1", "v1-old");
        oldProperties.put("prop2", "v2-old");
        oldProperties.put("prop3", "v3-old");
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> tags = java.util.Collections.emptyMap();
        java.util.Map<java.lang.String, java.lang.String> mergedProperties = new java.util.HashMap<>();
        mergedProperties.put("prop1", "v1-old");
        mergedProperties.put("prop2", "v2");
        mergedProperties.put("prop3", "v3-old");
        EasyMock.expect(amc.createConfig(EasyMock.eq(cluster), EasyMock.anyObject(org.apache.ambari.server.state.StackId.class), EasyMock.eq("hdfs-site"), EasyMock.eq(mergedProperties), EasyMock.anyString(), EasyMock.eq(tags))).andReturn(null);
        EasyMock.replay(injector, configHelper, amc, cluster, clusters, serviceInfo, oldConfig);
        upgradeCatalog.addNewConfigurationsFromXml();
        EasyMock.verify(configHelper, amc, cluster, clusters, serviceInfo, oldConfig);
    }

    @org.junit.Test
    public void shouldDeleteConfigurationFromXml() throws org.apache.ambari.server.AmbariException {
        oldProperties.put("prop1", "v1-old");
        oldProperties.put("prop3", "v3-old");
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> tags = java.util.Collections.emptyMap();
        java.util.Map<java.lang.String, java.lang.String> mergedProperties = new java.util.HashMap<>();
        mergedProperties.put("prop1", "v1-old");
        EasyMock.expect(amc.createConfig(EasyMock.eq(cluster), EasyMock.anyObject(org.apache.ambari.server.state.StackId.class), EasyMock.eq("hdfs-site"), EasyMock.eq(mergedProperties), EasyMock.anyString(), EasyMock.eq(tags))).andReturn(null);
        EasyMock.replay(injector, configHelper, amc, cluster, clusters, serviceInfo, oldConfig);
        upgradeCatalog.addNewConfigurationsFromXml();
        EasyMock.verify(configHelper, amc, cluster, clusters, serviceInfo, oldConfig);
    }

    private static org.apache.ambari.server.state.PropertyInfo createProperty(java.lang.String filename, java.lang.String name, boolean add, boolean update, boolean delete) {
        org.apache.ambari.server.state.PropertyInfo propertyInfo = new org.apache.ambari.server.state.PropertyInfo();
        propertyInfo.setFilename(filename);
        propertyInfo.setName(name);
        propertyInfo.setPropertyAmbariUpgradeBehavior(new org.apache.ambari.server.state.PropertyUpgradeBehavior(add, delete, update));
        return propertyInfo;
    }
}