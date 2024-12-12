package org.apache.ambari.server.orm.entities;
public class BlueprintSettingEntityTest {
    @org.junit.Test
    public void testSetGetSettingName() {
        org.apache.ambari.server.orm.entities.BlueprintSettingEntity entity = new org.apache.ambari.server.orm.entities.BlueprintSettingEntity();
        entity.setSettingName("component_settings");
        org.junit.Assert.assertEquals("component_settings", entity.getSettingName());
    }

    @org.junit.Test
    public void testSetGetBlueprintEntity() {
        org.apache.ambari.server.orm.entities.BlueprintEntity bp = new org.apache.ambari.server.orm.entities.BlueprintEntity();
        org.apache.ambari.server.orm.entities.BlueprintSettingEntity entity = new org.apache.ambari.server.orm.entities.BlueprintSettingEntity();
        entity.setBlueprintEntity(bp);
        org.junit.Assert.assertSame(bp, entity.getBlueprintEntity());
    }

    @org.junit.Test
    public void testSetGetBlueprintName() {
        org.apache.ambari.server.orm.entities.BlueprintSettingEntity entity = new org.apache.ambari.server.orm.entities.BlueprintSettingEntity();
        entity.setBlueprintName("bp2");
        org.junit.Assert.assertEquals("bp2", entity.getBlueprintName());
    }

    @org.junit.Test
    public void testSetGetSettingData() {
        org.apache.ambari.server.orm.entities.BlueprintSettingEntity entity = new org.apache.ambari.server.orm.entities.BlueprintSettingEntity();
        java.lang.String settingData = "[{'recovery_settings':[{'recovery_enabled':'true'}]}]";
        entity.setSettingData(settingData);
        org.junit.Assert.assertEquals(settingData, entity.getSettingData());
    }
}