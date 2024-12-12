package org.apache.ambari.server.orm.dao;
public class SettingDAOTest {
    private com.google.inject.Injector injector;

    private org.apache.ambari.server.orm.dao.SettingDAO dao;

    @org.junit.Before
    public void setUp() throws java.lang.Exception {
        injector = com.google.inject.Guice.createInjector(new org.apache.ambari.server.orm.InMemoryDefaultTestModule());
        dao = injector.getInstance(org.apache.ambari.server.orm.dao.SettingDAO.class);
        injector.getInstance(org.apache.ambari.server.orm.GuiceJpaInitializer.class);
        injector.getInstance(org.apache.ambari.server.orm.OrmTestHelper.class).createCluster();
    }

    @org.junit.After
    public void teardown() throws java.lang.Exception {
        org.apache.ambari.server.H2DatabaseCleaner.clearDatabaseAndStopPersistenceService(injector);
    }

    @org.junit.Test
    public void testCRUD() {
        java.util.Map<java.lang.String, org.apache.ambari.server.orm.entities.SettingEntity> entities = new java.util.HashMap<>();
        for (int i = 0; i < 3; i++) {
            org.apache.ambari.server.orm.entities.SettingEntity entity = new org.apache.ambari.server.orm.entities.SettingEntity();
            entity.setName("motd" + i);
            entity.setContent("test content" + i);
            entity.setUpdatedBy("ambari");
            entity.setSettingType("ambari-server");
            entity.setUpdateTimestamp(java.lang.System.currentTimeMillis());
            entities.put(entity.getName(), entity);
            dao.create(entity);
        }
        retrieveAndValidateSame(entities);
        junit.framework.Assert.assertEquals(entities.size(), dao.findAll().size());
        junit.framework.Assert.assertNull(dao.findByName("does-not-exist"));
        for (java.util.Map.Entry<java.lang.String, org.apache.ambari.server.orm.entities.SettingEntity> entry : entities.entrySet()) {
            entry.getValue().setContent(java.util.Objects.toString(java.lang.Math.random()));
            dao.merge(entry.getValue());
        }
        retrieveAndValidateSame(entities);
        junit.framework.Assert.assertEquals(entities.size(), dao.findAll().size());
        for (java.util.Map.Entry<java.lang.String, org.apache.ambari.server.orm.entities.SettingEntity> entry : entities.entrySet()) {
            dao.removeByName(entry.getKey());
        }
        junit.framework.Assert.assertEquals(0, dao.findAll().size());
    }

    private void retrieveAndValidateSame(java.util.Map<java.lang.String, org.apache.ambari.server.orm.entities.SettingEntity> entities) {
        for (java.util.Map.Entry<java.lang.String, org.apache.ambari.server.orm.entities.SettingEntity> entry : entities.entrySet()) {
            java.lang.String name = entry.getKey();
            junit.framework.Assert.assertEquals(entry.getValue(), dao.findByName(name));
        }
    }
}