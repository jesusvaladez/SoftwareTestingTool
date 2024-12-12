package org.apache.ambari.server.orm.entities;
public class HostEntityTest {
    @org.junit.Test
    public void testGetHostComponentDesiredStateEntities() throws java.lang.Exception {
        org.apache.ambari.server.orm.entities.HostEntity hostEntity = new org.apache.ambari.server.orm.entities.HostEntity();
        org.apache.ambari.server.orm.entities.HostComponentDesiredStateEntity stateEntity = new org.apache.ambari.server.orm.entities.HostComponentDesiredStateEntity();
        hostEntity.setHostComponentDesiredStateEntities(new java.util.HashSet<>());
        java.util.Collection<org.apache.ambari.server.orm.entities.HostComponentDesiredStateEntity> stateEntities = hostEntity.getHostComponentDesiredStateEntities();
        org.junit.Assert.assertTrue(stateEntities.isEmpty());
        try {
            stateEntities.add(stateEntity);
            org.junit.Assert.fail("Expected UnsupportedOperationException");
        } catch (java.lang.UnsupportedOperationException e) {
        }
    }

    @org.junit.Test
    public void testAddHostComponentDesiredStateEntity() throws java.lang.Exception {
        org.apache.ambari.server.orm.entities.HostEntity hostEntity = new org.apache.ambari.server.orm.entities.HostEntity();
        org.apache.ambari.server.orm.entities.HostComponentDesiredStateEntity stateEntity = new org.apache.ambari.server.orm.entities.HostComponentDesiredStateEntity();
        hostEntity.setHostComponentDesiredStateEntities(new java.util.HashSet<>());
        java.util.Collection<org.apache.ambari.server.orm.entities.HostComponentDesiredStateEntity> stateEntities = hostEntity.getHostComponentDesiredStateEntities();
        org.junit.Assert.assertTrue(stateEntities.isEmpty());
        hostEntity.addHostComponentDesiredStateEntity(stateEntity);
        stateEntities = hostEntity.getHostComponentDesiredStateEntities();
        org.junit.Assert.assertTrue(stateEntities.contains(stateEntity));
    }

    @org.junit.Test
    public void testRemoveHostComponentDesiredStateEntity() throws java.lang.Exception {
        org.apache.ambari.server.orm.entities.HostEntity hostEntity = new org.apache.ambari.server.orm.entities.HostEntity();
        org.apache.ambari.server.orm.entities.HostComponentDesiredStateEntity stateEntity = new org.apache.ambari.server.orm.entities.HostComponentDesiredStateEntity();
        hostEntity.setHostComponentDesiredStateEntities(new java.util.HashSet<>());
        java.util.Collection<org.apache.ambari.server.orm.entities.HostComponentDesiredStateEntity> stateEntities = hostEntity.getHostComponentDesiredStateEntities();
        org.junit.Assert.assertTrue(stateEntities.isEmpty());
        hostEntity.addHostComponentDesiredStateEntity(stateEntity);
        stateEntities = hostEntity.getHostComponentDesiredStateEntities();
        org.junit.Assert.assertTrue(stateEntities.contains(stateEntity));
        hostEntity.removeHostComponentDesiredStateEntity(stateEntity);
        stateEntities = hostEntity.getHostComponentDesiredStateEntities();
        org.junit.Assert.assertFalse(stateEntities.contains(stateEntity));
    }

    @org.junit.Test
    public void testGetHostComponentStateEntities() throws java.lang.Exception {
        org.apache.ambari.server.orm.entities.HostEntity hostEntity = new org.apache.ambari.server.orm.entities.HostEntity();
        org.apache.ambari.server.orm.entities.HostComponentStateEntity stateEntity = new org.apache.ambari.server.orm.entities.HostComponentStateEntity();
        hostEntity.setHostComponentStateEntities(new java.util.HashSet<>());
        java.util.Collection<org.apache.ambari.server.orm.entities.HostComponentStateEntity> stateEntities = hostEntity.getHostComponentStateEntities();
        org.junit.Assert.assertTrue(stateEntities.isEmpty());
        try {
            stateEntities.add(stateEntity);
            org.junit.Assert.fail("Expected UnsupportedOperationException");
        } catch (java.lang.UnsupportedOperationException e) {
        }
    }

    @org.junit.Test
    public void testAddHostComponentStateEntity() throws java.lang.Exception {
        org.apache.ambari.server.orm.entities.HostEntity hostEntity = new org.apache.ambari.server.orm.entities.HostEntity();
        org.apache.ambari.server.orm.entities.HostComponentStateEntity stateEntity = new org.apache.ambari.server.orm.entities.HostComponentStateEntity();
        hostEntity.setHostComponentStateEntities(new java.util.HashSet<>());
        java.util.Collection<org.apache.ambari.server.orm.entities.HostComponentStateEntity> stateEntities = hostEntity.getHostComponentStateEntities();
        org.junit.Assert.assertTrue(stateEntities.isEmpty());
        hostEntity.addHostComponentStateEntity(stateEntity);
        stateEntities = hostEntity.getHostComponentStateEntities();
        org.junit.Assert.assertTrue(stateEntities.contains(stateEntity));
    }

    @org.junit.Test
    public void testRemoveHostComponentStateEntity() throws java.lang.Exception {
        org.apache.ambari.server.orm.entities.HostEntity hostEntity = new org.apache.ambari.server.orm.entities.HostEntity();
        org.apache.ambari.server.orm.entities.HostComponentStateEntity stateEntity = new org.apache.ambari.server.orm.entities.HostComponentStateEntity();
        hostEntity.setHostComponentStateEntities(new java.util.HashSet<>());
        java.util.Collection<org.apache.ambari.server.orm.entities.HostComponentStateEntity> stateEntities = hostEntity.getHostComponentStateEntities();
        org.junit.Assert.assertTrue(stateEntities.isEmpty());
        hostEntity.addHostComponentStateEntity(stateEntity);
        stateEntities = hostEntity.getHostComponentStateEntities();
        org.junit.Assert.assertTrue(stateEntities.contains(stateEntity));
        hostEntity.removeHostComponentStateEntity(stateEntity);
        stateEntities = hostEntity.getHostComponentStateEntities();
        org.junit.Assert.assertFalse(stateEntities.contains(stateEntity));
    }
}