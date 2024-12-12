package org.apache.ambari.server.orm.entities;
public class BlueprintEntityTest {
    private org.apache.ambari.server.orm.entities.StackEntity stackEntity = new org.apache.ambari.server.orm.entities.StackEntity();

    @org.junit.Before
    public void setup() {
        stackEntity = new org.apache.ambari.server.orm.entities.StackEntity();
        stackEntity.setStackName("HDP");
        stackEntity.setStackVersion("2.0.6");
    }

    @org.junit.Test
    public void testSetGetBlueprintName() {
        org.apache.ambari.server.orm.entities.BlueprintEntity entity = new org.apache.ambari.server.orm.entities.BlueprintEntity();
        entity.setBlueprintName("foo");
        org.junit.Assert.assertEquals("foo", entity.getBlueprintName());
    }

    @org.junit.Test
    public void testSetGetStack() {
        org.apache.ambari.server.orm.entities.BlueprintEntity entity = new org.apache.ambari.server.orm.entities.BlueprintEntity();
        entity.setStack(stackEntity);
        org.junit.Assert.assertEquals(stackEntity, entity.getStack());
    }

    @org.junit.Test
    public void testSetGetHostGroups() {
        org.apache.ambari.server.orm.entities.BlueprintEntity entity = new org.apache.ambari.server.orm.entities.BlueprintEntity();
        java.util.Collection<org.apache.ambari.server.orm.entities.HostGroupEntity> hostGroups = java.util.Collections.emptyList();
        entity.setHostGroups(hostGroups);
        org.junit.Assert.assertSame(hostGroups, entity.getHostGroups());
    }

    @org.junit.Test
    public void testSetGetConfigurations() {
        org.apache.ambari.server.orm.entities.BlueprintEntity entity = new org.apache.ambari.server.orm.entities.BlueprintEntity();
        java.util.Collection<org.apache.ambari.server.orm.entities.BlueprintConfigEntity> configurations = java.util.Collections.emptyList();
        entity.setConfigurations(configurations);
        org.junit.Assert.assertSame(configurations, entity.getConfigurations());
    }

    @org.junit.Test
    public void testSetGetSetting() {
        org.apache.ambari.server.orm.entities.BlueprintEntity entity = new org.apache.ambari.server.orm.entities.BlueprintEntity();
        java.util.Collection<org.apache.ambari.server.orm.entities.BlueprintSettingEntity> setting = java.util.Collections.emptyList();
        entity.setSettings(setting);
        org.junit.Assert.assertSame(setting, entity.getSettings());
    }
}