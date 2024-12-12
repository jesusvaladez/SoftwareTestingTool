package org.apache.ambari.server.orm.entities;
public class ViewEntityEntityTest {
    @org.junit.Test
    public void testSetGetId() throws java.lang.Exception {
        org.apache.ambari.server.orm.entities.ViewEntityEntity viewEntityEntity = new org.apache.ambari.server.orm.entities.ViewEntityEntity();
        viewEntityEntity.setId(99L);
        org.junit.Assert.assertEquals(99L, ((long) (viewEntityEntity.getId())));
    }

    @org.junit.Test
    public void testSetGetViewName() throws java.lang.Exception {
        org.apache.ambari.server.orm.entities.ViewEntityEntity viewEntityEntity = new org.apache.ambari.server.orm.entities.ViewEntityEntity();
        viewEntityEntity.setViewName("foo");
        org.junit.Assert.assertEquals("foo", viewEntityEntity.getViewName());
    }

    @org.junit.Test
    public void testSetGetViewInstanceName() throws java.lang.Exception {
        org.apache.ambari.server.orm.entities.ViewEntityEntity viewEntityEntity = new org.apache.ambari.server.orm.entities.ViewEntityEntity();
        viewEntityEntity.setViewInstanceName("foo");
        org.junit.Assert.assertEquals("foo", viewEntityEntity.getViewInstanceName());
    }

    @org.junit.Test
    public void testSetGetClassName() throws java.lang.Exception {
        org.apache.ambari.server.orm.entities.ViewEntityEntity viewEntityEntity = new org.apache.ambari.server.orm.entities.ViewEntityEntity();
        viewEntityEntity.setClassName("TestClass");
        org.junit.Assert.assertEquals("TestClass", viewEntityEntity.getClassName());
    }

    @org.junit.Test
    public void testSetGetIdProperty() throws java.lang.Exception {
        org.apache.ambari.server.orm.entities.ViewEntityEntity viewEntityEntity = new org.apache.ambari.server.orm.entities.ViewEntityEntity();
        viewEntityEntity.setIdProperty("id");
        org.junit.Assert.assertEquals("id", viewEntityEntity.getIdProperty());
    }

    @org.junit.Test
    public void testSetGetViewInstance() throws java.lang.Exception {
        org.apache.ambari.server.orm.entities.ViewInstanceEntity viewInstanceEntity = org.apache.ambari.server.orm.entities.ViewInstanceEntityTest.getViewInstanceEntity();
        org.apache.ambari.server.orm.entities.ViewEntityEntity viewEntityEntity = new org.apache.ambari.server.orm.entities.ViewEntityEntity();
        viewEntityEntity.setViewInstance(viewInstanceEntity);
        org.junit.Assert.assertEquals(viewInstanceEntity, viewEntityEntity.getViewInstance());
    }

    @org.junit.Test
    public void testEquals() throws java.lang.Exception {
        org.apache.ambari.server.orm.entities.ViewEntityEntity viewEntityEntity = new org.apache.ambari.server.orm.entities.ViewEntityEntity();
        viewEntityEntity.setId(99L);
        viewEntityEntity.setClassName("TestClass");
        viewEntityEntity.setIdProperty("id");
        viewEntityEntity.setViewName("foo");
        viewEntityEntity.setViewInstanceName("bar");
        org.apache.ambari.server.orm.entities.ViewEntityEntity viewEntityEntity2 = new org.apache.ambari.server.orm.entities.ViewEntityEntity();
        viewEntityEntity2.setId(99L);
        viewEntityEntity2.setClassName("TestClass");
        viewEntityEntity2.setIdProperty("id");
        viewEntityEntity2.setViewName("foo");
        viewEntityEntity2.setViewInstanceName("bar");
        org.junit.Assert.assertTrue(viewEntityEntity.equals(viewEntityEntity2));
        viewEntityEntity2.setId(100L);
        org.junit.Assert.assertFalse(viewEntityEntity.equals(viewEntityEntity2));
    }

    @org.junit.Test
    public void testHashCode() throws java.lang.Exception {
        org.apache.ambari.server.orm.entities.ViewEntityEntity viewEntityEntity = new org.apache.ambari.server.orm.entities.ViewEntityEntity();
        viewEntityEntity.setId(99L);
        viewEntityEntity.setClassName("TestClass");
        viewEntityEntity.setIdProperty("id");
        viewEntityEntity.setViewName("foo");
        viewEntityEntity.setViewInstanceName("bar");
        org.apache.ambari.server.orm.entities.ViewEntityEntity viewEntityEntity2 = new org.apache.ambari.server.orm.entities.ViewEntityEntity();
        viewEntityEntity2.setId(99L);
        viewEntityEntity2.setClassName("TestClass");
        viewEntityEntity2.setIdProperty("id");
        viewEntityEntity2.setViewName("foo");
        viewEntityEntity2.setViewInstanceName("bar");
        org.junit.Assert.assertEquals(viewEntityEntity.hashCode(), viewEntityEntity2.hashCode());
    }
}