package org.apache.ambari.server.orm.entities;
public class ServiceConfigEntityTest {
    @org.junit.Test
    public void testSettersGetters() {
        org.apache.ambari.server.orm.entities.ServiceConfigEntity entity = new org.apache.ambari.server.orm.entities.ServiceConfigEntity();
        entity.setServiceName("foo");
        entity.setUser("bar");
        entity.setNote("note");
        entity.setVersion(1L);
        entity.setServiceConfigId(1L);
        entity.setClusterId(1L);
        entity.setCreateTimestamp(1111L);
        org.junit.Assert.assertEquals("foo", entity.getServiceName());
        org.junit.Assert.assertEquals("bar", entity.getUser());
        org.junit.Assert.assertEquals("note", entity.getNote());
        org.junit.Assert.assertEquals(java.lang.Long.valueOf(1), entity.getVersion());
        org.junit.Assert.assertEquals(java.lang.Long.valueOf(1), entity.getServiceConfigId());
        org.junit.Assert.assertEquals(java.lang.Long.valueOf(1), entity.getClusterId());
        org.junit.Assert.assertEquals(java.lang.Long.valueOf(1111), entity.getCreateTimestamp());
    }
}