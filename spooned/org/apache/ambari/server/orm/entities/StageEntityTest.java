package org.apache.ambari.server.orm.entities;
public class StageEntityTest {
    @org.junit.Test
    public void testSetGetRequestContext() {
        org.apache.ambari.server.orm.entities.StageEntity entity = new org.apache.ambari.server.orm.entities.StageEntity();
        entity.setRequestContext("testSetGetRequestContext");
        org.junit.Assert.assertEquals("testSetGetRequestContext", entity.getRequestContext());
    }
}