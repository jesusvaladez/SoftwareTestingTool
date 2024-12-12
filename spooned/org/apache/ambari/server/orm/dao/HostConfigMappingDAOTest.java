package org.apache.ambari.server.orm.dao;
public class HostConfigMappingDAOTest {
    private com.google.inject.Injector injector;

    @com.google.inject.Inject
    private org.apache.ambari.server.orm.dao.HostConfigMappingDAO hostConfigMappingDAO;

    @com.google.inject.Inject
    private org.apache.ambari.server.orm.dao.HostDAO hostDAO;

    @org.junit.Before
    public void setup() throws org.apache.ambari.server.AmbariException {
        injector = com.google.inject.Guice.createInjector(new org.apache.ambari.server.orm.InMemoryDefaultTestModule());
        injector.getInstance(org.apache.ambari.server.orm.GuiceJpaInitializer.class);
        hostConfigMappingDAO = injector.getInstance(org.apache.ambari.server.orm.dao.HostConfigMappingDAO.class);
        hostDAO = injector.getInstance(org.apache.ambari.server.orm.dao.HostDAO.class);
    }

    @org.junit.After
    public void teardown() throws org.apache.ambari.server.AmbariException, java.sql.SQLException {
        org.apache.ambari.server.H2DatabaseCleaner.clearDatabaseAndStopPersistenceService(injector);
    }

    private org.apache.ambari.server.orm.cache.HostConfigMapping createEntity(long clusterId, java.lang.String hostName, java.lang.String type, java.lang.String version) throws java.lang.Exception {
        org.apache.ambari.server.orm.cache.HostConfigMapping hostConfigMappingEntity = new org.apache.ambari.server.orm.cache.HostConfigMappingImpl();
        hostConfigMappingEntity.setClusterId(java.lang.Long.valueOf(clusterId));
        hostConfigMappingEntity.setCreateTimestamp(java.lang.Long.valueOf(java.lang.System.currentTimeMillis()));
        org.apache.ambari.server.orm.entities.HostEntity hostEntity = hostDAO.findByName(hostName);
        if (hostEntity == null) {
            hostEntity = new org.apache.ambari.server.orm.entities.HostEntity();
            hostEntity.setHostName(hostName);
            hostDAO.create(hostEntity);
        }
        hostConfigMappingEntity.setHostId(hostEntity.getHostId());
        hostConfigMappingEntity.setSelected(1);
        hostConfigMappingEntity.setType(type);
        hostConfigMappingEntity.setVersion(version);
        hostConfigMappingEntity.setUser("_test");
        hostConfigMappingDAO.create(hostConfigMappingEntity);
        return hostConfigMappingEntity;
    }

    @org.junit.Test
    public void testCreate() throws java.lang.Exception {
        createEntity(1L, "h1", "global", "v1");
    }

    @org.junit.Test
    public void testFindByType() throws java.lang.Exception {
        org.apache.ambari.server.orm.cache.HostConfigMapping source = createEntity(1L, "h1", "global", "v1");
        org.apache.ambari.server.orm.entities.HostEntity hostEntity = hostDAO.findByName("h1");
        java.util.Set<org.apache.ambari.server.orm.cache.HostConfigMapping> target = hostConfigMappingDAO.findByType(1L, hostEntity.getHostId(), "global");
        junit.framework.Assert.assertEquals("Expected one result", 1, target.size());
        for (org.apache.ambari.server.orm.cache.HostConfigMapping item : target)
            junit.framework.Assert.assertEquals("Expected version 'v1'", source.getVersion(), item.getVersion());

    }

    @org.junit.Test
    public void testMerge() throws java.lang.Exception {
        org.apache.ambari.server.orm.cache.HostConfigMapping source = createEntity(1L, "h1", "global", "v1");
        org.apache.ambari.server.orm.entities.HostEntity hostEntity = hostDAO.findByName("h1");
        java.util.Set<org.apache.ambari.server.orm.cache.HostConfigMapping> target = hostConfigMappingDAO.findByType(1L, hostEntity.getHostId(), "global");
        junit.framework.Assert.assertEquals("Expected one result", 1, target.size());
        org.apache.ambari.server.orm.cache.HostConfigMapping toChange = null;
        for (org.apache.ambari.server.orm.cache.HostConfigMapping item : target) {
            junit.framework.Assert.assertEquals("Expected version 'v1'", source.getVersion(), item.getVersion());
            junit.framework.Assert.assertEquals("Expected selected flag 1", 1, ((int) (item.getSelected())));
            toChange = item;
            toChange.setSelected(0);
        }
        hostConfigMappingDAO.merge(toChange);
        target = hostConfigMappingDAO.findByType(1L, hostEntity.getHostId(), "global");
        junit.framework.Assert.assertEquals("Expected one result", 1, target.size());
        for (org.apache.ambari.server.orm.cache.HostConfigMapping item : target) {
            junit.framework.Assert.assertEquals("Expected version 'v1'", source.getVersion(), item.getVersion());
            junit.framework.Assert.assertEquals("Expected selected flag 0", 0, ((int) (item.getSelected())));
        }
    }

    @org.junit.Test
    public void testFindSelected() throws java.lang.Exception {
        createEntity(1L, "h1", "global", "version1");
        org.apache.ambari.server.orm.cache.HostConfigMapping coreSiteConfigV1 = createEntity(1L, "h1", "core-site", "version1");
        org.apache.ambari.server.orm.entities.HostEntity hostEntity = hostDAO.findByName("h1");
        java.util.Set<org.apache.ambari.server.orm.cache.HostConfigMapping> targets = hostConfigMappingDAO.findSelected(1L, hostEntity.getHostId());
        junit.framework.Assert.assertEquals("Expected two entities", 2, targets.size());
        coreSiteConfigV1.setSelected(0);
        hostConfigMappingDAO.merge(coreSiteConfigV1);
        createEntity(1L, "h1", "core-site", "version2");
        targets = hostConfigMappingDAO.findSelected(1L, hostEntity.getHostId());
        junit.framework.Assert.assertEquals("Expected two entities", 2, targets.size());
    }

    @org.junit.Test
    public void testFindSelectedByType() throws java.lang.Exception {
        org.apache.ambari.server.orm.cache.HostConfigMapping entity1 = createEntity(1L, "h1", "global", "version1");
        org.apache.ambari.server.orm.entities.HostEntity hostEntity = hostDAO.findByName("h1");
        org.apache.ambari.server.orm.cache.HostConfigMapping target = hostConfigMappingDAO.findSelectedByType(1L, hostEntity.getHostId(), "core-site");
        junit.framework.Assert.assertNull("Expected null entity for type 'core-site'", target);
        target = hostConfigMappingDAO.findSelectedByType(1L, hostEntity.getHostId(), "global");
        junit.framework.Assert.assertNotNull("Expected non-null entity for type 'global'", target);
        junit.framework.Assert.assertEquals(("Expected version to be '" + entity1.getVersion()) + "'", entity1.getVersion(), target.getVersion());
        target.setSelected(0);
        hostConfigMappingDAO.merge(target);
        org.apache.ambari.server.orm.cache.HostConfigMapping entity2 = createEntity(1L, "h1", "global", "version2");
        target = hostConfigMappingDAO.findSelectedByType(1L, hostEntity.getHostId(), "global");
        junit.framework.Assert.assertNotNull("Expected non-null entity for type 'global'", target);
        junit.framework.Assert.assertEquals(("Expected version to be '" + entity2.getVersion()) + "'", entity2.getVersion(), target.getVersion());
        junit.framework.Assert.assertEquals("Expected instance equality", entity2, target);
    }

    @org.junit.Test
    public void testEmptyTable() throws java.lang.Exception {
        createEntity(1L, "h1", "global", "version1");
        org.apache.ambari.server.orm.entities.HostEntity hostEntity = hostDAO.findByName("h1");
        hostConfigMappingDAO.removeByClusterAndHostName(1L, "h1");
        org.apache.ambari.server.orm.cache.HostConfigMapping target = hostConfigMappingDAO.findSelectedByType(1L, hostEntity.getHostId(), "core-site");
        junit.framework.Assert.assertEquals(null, target);
    }
}