package org.apache.ambari.server.orm.entities;
public class SettingEntityTest {
    @org.junit.Test
    public void testSetGetId() {
        long id = 1000;
        org.apache.ambari.server.orm.entities.SettingEntity entity = new org.apache.ambari.server.orm.entities.SettingEntity();
        entity.setId(id);
        junit.framework.Assert.assertEquals(id, entity.getId());
    }

    @org.junit.Test
    public void testSetGetName() {
        java.lang.String name = "motd";
        org.apache.ambari.server.orm.entities.SettingEntity entity = new org.apache.ambari.server.orm.entities.SettingEntity();
        entity.setName(name);
        junit.framework.Assert.assertEquals(name, entity.getName());
    }

    @org.junit.Test
    public void testSetGetSettingType() {
        java.lang.String settingType = "ambari-server";
        org.apache.ambari.server.orm.entities.SettingEntity entity = new org.apache.ambari.server.orm.entities.SettingEntity();
        entity.setSettingType(settingType);
        junit.framework.Assert.assertEquals(settingType, entity.getSettingType());
    }

    @org.junit.Test
    public void testSetGetContent() {
        java.lang.String content = "{tag:random-tag, text:random-text}";
        org.apache.ambari.server.orm.entities.SettingEntity entity = new org.apache.ambari.server.orm.entities.SettingEntity();
        entity.setContent(content);
        junit.framework.Assert.assertEquals(content, entity.getContent());
    }

    @org.junit.Test
    public void testSetGetUpdatedBy() {
        java.lang.String updatedBy = "ambari";
        org.apache.ambari.server.orm.entities.SettingEntity entity = new org.apache.ambari.server.orm.entities.SettingEntity();
        entity.setUpdatedBy(updatedBy);
        junit.framework.Assert.assertEquals(updatedBy, entity.getUpdatedBy());
    }

    @org.junit.Test
    public void testSetGetUpdatedTimeStamp() {
        long updateTimeStamp = 1234567890;
        org.apache.ambari.server.orm.entities.SettingEntity entity = new org.apache.ambari.server.orm.entities.SettingEntity();
        entity.setUpdateTimestamp(updateTimeStamp);
        junit.framework.Assert.assertEquals(updateTimeStamp, entity.getUpdateTimestamp());
    }

    @org.junit.Test
    public void testEquals() {
        org.apache.ambari.server.orm.entities.SettingEntity entity = new org.apache.ambari.server.orm.entities.SettingEntity();
        entity.setId(1);
        entity.setName("motd");
        entity.setContent("{tag:random-tag, text:random-text}");
        entity.setSettingType("ambari-server");
        entity.setUpdatedBy("ambari");
        entity.setUpdateTimestamp(1234567890);
        org.apache.ambari.server.orm.entities.SettingEntity newEntity = entity.clone();
        junit.framework.Assert.assertEquals(entity, newEntity);
    }
}