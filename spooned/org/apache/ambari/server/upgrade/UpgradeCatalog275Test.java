package org.apache.ambari.server.upgrade;
import org.easymock.Capture;
import static org.easymock.EasyMock.capture;
import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.createNiceMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
public class UpgradeCatalog275Test {
    @org.junit.Test
    public void testRemoveDfsHAInitial() {
        com.google.inject.Injector injector = EasyMock.createNiceMock(com.google.inject.Injector.class);
        org.apache.ambari.server.orm.dao.BlueprintDAO blueprintDAO = EasyMock.createMock(org.apache.ambari.server.orm.dao.BlueprintDAO.class);
        org.apache.ambari.server.orm.entities.BlueprintConfigEntity blueprintConfigEntity = new org.apache.ambari.server.orm.entities.BlueprintConfigEntity();
        blueprintConfigEntity.setType("hadoop-env");
        blueprintConfigEntity.setConfigData("{\"dfs_ha_initial_namenode_standby\":\"%HOSTGROUP::master_2%\"," + "\"dfs_ha_initial_namenode_active\":\"u1602.ambari.apache.org\"}");
        java.util.List<org.apache.ambari.server.orm.entities.BlueprintConfigEntity> blueprintConfigurations = java.util.Collections.singletonList(blueprintConfigEntity);
        org.apache.ambari.server.orm.entities.BlueprintEntity blueprintEntity = new org.apache.ambari.server.orm.entities.BlueprintEntity();
        blueprintEntity.setConfigurations(blueprintConfigurations);
        java.util.List<org.apache.ambari.server.orm.entities.BlueprintEntity> blueprintEntityList = java.util.Collections.singletonList(blueprintEntity);
        EasyMock.expect(injector.getInstance(org.apache.ambari.server.orm.dao.BlueprintDAO.class)).andReturn(blueprintDAO);
        EasyMock.expect(blueprintDAO.findAll()).andReturn(blueprintEntityList);
        org.easymock.Capture<org.apache.ambari.server.orm.entities.BlueprintEntity> blueprintEntityCapture = org.easymock.Capture.newInstance();
        EasyMock.expect(blueprintDAO.merge(EasyMock.capture(blueprintEntityCapture))).andReturn(null);
        EasyMock.replay(injector, blueprintDAO);
        org.apache.ambari.server.upgrade.UpgradeCatalog275 upgradeCatalog275 = new org.apache.ambari.server.upgrade.UpgradeCatalog275(injector);
        upgradeCatalog275.removeDfsHAInitial();
        EasyMock.verify(injector, blueprintDAO);
        org.junit.Assert.assertNotNull(blueprintEntityCapture.getValues());
        org.junit.Assert.assertEquals(1, blueprintEntityCapture.getValues().size());
        org.apache.ambari.server.orm.entities.BlueprintEntity blueprintEntityToMerge = blueprintEntityCapture.getValue();
        java.util.Collection<org.apache.ambari.server.orm.entities.BlueprintConfigEntity> resultConfigurations = blueprintEntityToMerge.getConfigurations();
        for (org.apache.ambari.server.orm.entities.BlueprintConfigEntity resultConfiguration : resultConfigurations) {
            if (resultConfiguration.getType().equals("hadoop-env")) {
                java.lang.String configData = resultConfiguration.getConfigData();
                java.util.Map<java.lang.String, java.lang.String> typeProperties = org.apache.ambari.server.upgrade.UpgradeCatalog275.GSON.<java.util.Map<java.lang.String, java.lang.String>>fromJson(configData, java.util.Map.class);
                org.junit.Assert.assertEquals(0, typeProperties.size());
                return;
            }
        }
        org.junit.Assert.fail("No \"hadoop-env\" config type was found in result configuration");
    }
}