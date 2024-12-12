package org.apache.ambari.server.state.stack;
@org.junit.experimental.categories.Category({ category.StackUpgradeTest.class })
public class ConfigUpgradePackTest {
    private com.google.inject.Injector injector;

    private org.apache.ambari.server.api.services.AmbariMetaInfo ambariMetaInfo;

    @org.junit.Before
    public void before() throws java.lang.Exception {
        injector = com.google.inject.Guice.createInjector(new org.apache.ambari.server.orm.InMemoryDefaultTestModule());
        injector.getInstance(org.apache.ambari.server.orm.GuiceJpaInitializer.class);
        ambariMetaInfo = injector.getInstance(org.apache.ambari.server.api.services.AmbariMetaInfo.class);
    }

    @org.junit.After
    public void teardown() throws org.apache.ambari.server.AmbariException, java.sql.SQLException {
        org.apache.ambari.server.H2DatabaseCleaner.clearDatabaseAndStopPersistenceService(injector);
    }

    @org.junit.Test
    public void testMerge() {
        java.util.ArrayList<org.apache.ambari.server.stack.upgrade.ConfigUpgradePack> cups = new java.util.ArrayList<>();
        for (int cupIndex = 0; cupIndex < 3; cupIndex++) {
            java.util.ArrayList<org.apache.ambari.server.stack.upgrade.ConfigUpgradePack.AffectedService> services = new java.util.ArrayList<>();
            for (int serviceIndex = 0; serviceIndex < 2; serviceIndex++) {
                java.lang.String serviceName;
                if (serviceIndex == 0) {
                    serviceName = "HDFS";
                } else {
                    serviceName = java.lang.String.format("SOME_SERVICE_%s", cupIndex);
                }
                java.util.ArrayList<org.apache.ambari.server.stack.upgrade.ConfigUpgradePack.AffectedComponent> components = new java.util.ArrayList<>();
                for (int componentIndex = 0; componentIndex < 2; componentIndex++) {
                    java.lang.String componentName;
                    if (componentIndex == 0) {
                        componentName = "NAMENODE";
                    } else {
                        componentName = "SOME_COMPONENT_" + cupIndex;
                    }
                    java.util.ArrayList<org.apache.ambari.server.stack.upgrade.ConfigUpgradeChangeDefinition> changeDefinitions = new java.util.ArrayList<>();
                    for (int changeIndex = 0; changeIndex < 2; changeIndex++) {
                        java.lang.String change_id = java.lang.String.format("CHANGE_%s_%s_%s_%s", cupIndex, serviceIndex, componentIndex, changeIndex);
                        org.apache.ambari.server.stack.upgrade.ConfigUpgradeChangeDefinition changeDefinition = new org.apache.ambari.server.stack.upgrade.ConfigUpgradeChangeDefinition();
                        changeDefinition.id = change_id;
                        changeDefinitions.add(changeDefinition);
                    }
                    org.apache.ambari.server.stack.upgrade.ConfigUpgradePack.AffectedComponent component = new org.apache.ambari.server.stack.upgrade.ConfigUpgradePack.AffectedComponent();
                    component.name = componentName;
                    component.changes = changeDefinitions;
                    components.add(component);
                }
                org.apache.ambari.server.stack.upgrade.ConfigUpgradePack.AffectedService service = new org.apache.ambari.server.stack.upgrade.ConfigUpgradePack.AffectedService();
                service.name = serviceName;
                service.components = components;
                services.add(service);
            }
            org.apache.ambari.server.stack.upgrade.ConfigUpgradePack cupI = new org.apache.ambari.server.stack.upgrade.ConfigUpgradePack();
            cupI.services = services;
            cups.add(cupI);
        }
        org.apache.ambari.server.stack.upgrade.ConfigUpgradePack result = org.apache.ambari.server.stack.upgrade.ConfigUpgradePack.merge(cups);
        org.junit.Assert.assertEquals(result.enumerateConfigChangesByID().entrySet().size(), 24);
        org.junit.Assert.assertEquals(result.getServiceMap().get("HDFS").getComponentMap().get("NAMENODE").changes.get(0).id, "CHANGE_0_0_0_0");
        org.junit.Assert.assertEquals(result.getServiceMap().get("HDFS").getComponentMap().get("NAMENODE").changes.get(1).id, "CHANGE_0_0_0_1");
        org.junit.Assert.assertEquals(result.getServiceMap().get("HDFS").getComponentMap().get("NAMENODE").changes.get(2).id, "CHANGE_1_0_0_0");
        org.junit.Assert.assertEquals(result.getServiceMap().get("HDFS").getComponentMap().get("NAMENODE").changes.get(3).id, "CHANGE_1_0_0_1");
        org.junit.Assert.assertEquals(result.getServiceMap().get("HDFS").getComponentMap().get("NAMENODE").changes.get(4).id, "CHANGE_2_0_0_0");
        org.junit.Assert.assertEquals(result.getServiceMap().get("HDFS").getComponentMap().get("NAMENODE").changes.get(5).id, "CHANGE_2_0_0_1");
        org.junit.Assert.assertEquals(result.getServiceMap().get("HDFS").getComponentMap().get("SOME_COMPONENT_0").changes.get(0).id, "CHANGE_0_0_1_0");
        org.junit.Assert.assertEquals(result.getServiceMap().get("HDFS").getComponentMap().get("SOME_COMPONENT_0").changes.get(1).id, "CHANGE_0_0_1_1");
        org.junit.Assert.assertEquals(result.getServiceMap().get("HDFS").getComponentMap().get("SOME_COMPONENT_1").changes.get(0).id, "CHANGE_1_0_1_0");
        org.junit.Assert.assertEquals(result.getServiceMap().get("HDFS").getComponentMap().get("SOME_COMPONENT_1").changes.get(1).id, "CHANGE_1_0_1_1");
        org.junit.Assert.assertEquals(result.getServiceMap().get("HDFS").getComponentMap().get("SOME_COMPONENT_2").changes.get(0).id, "CHANGE_2_0_1_0");
        org.junit.Assert.assertEquals(result.getServiceMap().get("HDFS").getComponentMap().get("SOME_COMPONENT_2").changes.get(1).id, "CHANGE_2_0_1_1");
        org.junit.Assert.assertEquals(result.getServiceMap().get("SOME_SERVICE_0").getComponentMap().get("NAMENODE").changes.get(0).id, "CHANGE_0_1_0_0");
        org.junit.Assert.assertEquals(result.getServiceMap().get("SOME_SERVICE_0").getComponentMap().get("NAMENODE").changes.get(1).id, "CHANGE_0_1_0_1");
        org.junit.Assert.assertEquals(result.getServiceMap().get("SOME_SERVICE_0").getComponentMap().get("SOME_COMPONENT_0").changes.get(0).id, "CHANGE_0_1_1_0");
        org.junit.Assert.assertEquals(result.getServiceMap().get("SOME_SERVICE_0").getComponentMap().get("SOME_COMPONENT_0").changes.get(1).id, "CHANGE_0_1_1_1");
        org.junit.Assert.assertEquals(result.getServiceMap().get("SOME_SERVICE_1").getComponentMap().get("NAMENODE").changes.get(0).id, "CHANGE_1_1_0_0");
        org.junit.Assert.assertEquals(result.getServiceMap().get("SOME_SERVICE_1").getComponentMap().get("NAMENODE").changes.get(1).id, "CHANGE_1_1_0_1");
        org.junit.Assert.assertEquals(result.getServiceMap().get("SOME_SERVICE_1").getComponentMap().get("SOME_COMPONENT_1").changes.get(0).id, "CHANGE_1_1_1_0");
        org.junit.Assert.assertEquals(result.getServiceMap().get("SOME_SERVICE_1").getComponentMap().get("SOME_COMPONENT_1").changes.get(1).id, "CHANGE_1_1_1_1");
        org.junit.Assert.assertEquals(result.getServiceMap().get("SOME_SERVICE_2").getComponentMap().get("NAMENODE").changes.get(0).id, "CHANGE_2_1_0_0");
        org.junit.Assert.assertEquals(result.getServiceMap().get("SOME_SERVICE_2").getComponentMap().get("NAMENODE").changes.get(1).id, "CHANGE_2_1_0_1");
        org.junit.Assert.assertEquals(result.getServiceMap().get("SOME_SERVICE_2").getComponentMap().get("SOME_COMPONENT_2").changes.get(0).id, "CHANGE_2_1_1_0");
        org.junit.Assert.assertEquals(result.getServiceMap().get("SOME_SERVICE_2").getComponentMap().get("SOME_COMPONENT_2").changes.get(1).id, "CHANGE_2_1_1_1");
    }

    @org.junit.Test
    public void testConfigUpgradeDefinitionParsing() throws java.lang.Exception {
        org.apache.ambari.server.stack.upgrade.ConfigUpgradePack cup = ambariMetaInfo.getConfigUpgradePack("HDP", "2.1.1");
        java.util.Map<java.lang.String, org.apache.ambari.server.stack.upgrade.ConfigUpgradeChangeDefinition> changesByID = cup.enumerateConfigChangesByID();
        org.apache.ambari.server.stack.upgrade.ConfigUpgradeChangeDefinition hdp_2_1_1_nm_pre_upgrade = changesByID.get("hdp_2_1_1_nm_pre_upgrade");
        org.junit.Assert.assertEquals("core-site", hdp_2_1_1_nm_pre_upgrade.getConfigType());
        org.junit.Assert.assertEquals(4, hdp_2_1_1_nm_pre_upgrade.getTransfers().size());
        org.apache.ambari.server.stack.upgrade.ConfigUpgradeChangeDefinition.Transfer t1 = hdp_2_1_1_nm_pre_upgrade.getTransfers().get(0);
        org.junit.Assert.assertEquals(org.apache.ambari.server.stack.upgrade.TransferOperation.COPY, t1.operation);
        org.junit.Assert.assertEquals("copy-key", t1.fromKey);
        org.junit.Assert.assertEquals("copy-key-to", t1.toKey);
        org.apache.ambari.server.stack.upgrade.ConfigUpgradeChangeDefinition.Transfer t2 = hdp_2_1_1_nm_pre_upgrade.getTransfers().get(1);
        org.junit.Assert.assertEquals(org.apache.ambari.server.stack.upgrade.TransferOperation.COPY, t2.operation);
        org.junit.Assert.assertEquals("my-site", t2.fromType);
        org.junit.Assert.assertEquals("my-copy-key", t2.fromKey);
        org.junit.Assert.assertEquals("my-copy-key-to", t2.toKey);
        org.junit.Assert.assertTrue(t2.keepKeys.isEmpty());
        org.apache.ambari.server.stack.upgrade.ConfigUpgradeChangeDefinition.Transfer t3 = hdp_2_1_1_nm_pre_upgrade.getTransfers().get(2);
        org.junit.Assert.assertEquals(org.apache.ambari.server.stack.upgrade.TransferOperation.MOVE, t3.operation);
        org.junit.Assert.assertEquals("move-key", t3.fromKey);
        org.junit.Assert.assertEquals("move-key-to", t3.toKey);
        org.apache.ambari.server.stack.upgrade.ConfigUpgradeChangeDefinition.Transfer t4 = hdp_2_1_1_nm_pre_upgrade.getTransfers().get(3);
        org.junit.Assert.assertEquals(org.apache.ambari.server.stack.upgrade.TransferOperation.DELETE, t4.operation);
        org.junit.Assert.assertEquals("delete-key", t4.deleteKey);
        org.junit.Assert.assertNull(t4.toKey);
        org.junit.Assert.assertTrue(t4.preserveEdits);
        org.junit.Assert.assertEquals(1, t4.keepKeys.size());
        org.junit.Assert.assertEquals("important-key", t4.keepKeys.get(0));
    }
}