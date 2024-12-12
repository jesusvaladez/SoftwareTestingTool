package org.apache.ambari.server.orm.dao;
public class TopologyRequestDAOTest {
    private com.google.inject.Injector injector;

    private org.apache.ambari.server.orm.dao.TopologyRequestDAO requestDAO;

    org.apache.ambari.server.orm.OrmTestHelper helper;

    java.lang.Long clusterId;

    @org.junit.Before
    public void setup() throws java.lang.Exception {
        injector = com.google.inject.Guice.createInjector(new org.apache.ambari.server.orm.InMemoryDefaultTestModule());
        injector.getInstance(org.apache.ambari.server.orm.GuiceJpaInitializer.class);
        requestDAO = injector.getInstance(org.apache.ambari.server.orm.dao.TopologyRequestDAO.class);
        helper = injector.getInstance(org.apache.ambari.server.orm.OrmTestHelper.class);
        clusterId = helper.createCluster();
    }

    @org.junit.After
    public void teardown() throws org.apache.ambari.server.AmbariException, java.sql.SQLException {
        org.apache.ambari.server.H2DatabaseCleaner.clearDatabaseAndStopPersistenceService(injector);
    }

    private void create() {
        org.apache.ambari.server.orm.entities.TopologyRequestEntity requestEntity = new org.apache.ambari.server.orm.entities.TopologyRequestEntity();
        requestEntity.setAction("a1");
        requestEntity.setBlueprintName("bp1");
        requestEntity.setClusterAttributes("attributes");
        requestEntity.setClusterProperties("properties");
        requestEntity.setClusterId(clusterId);
        requestEntity.setDescription("description");
        org.apache.ambari.server.orm.entities.TopologyHostGroupEntity hostGroupEntity = new org.apache.ambari.server.orm.entities.TopologyHostGroupEntity();
        hostGroupEntity.setName("hg1");
        hostGroupEntity.setGroupAttributes("hg-attributes");
        hostGroupEntity.setGroupProperties("hg-properties");
        hostGroupEntity.setTopologyRequestEntity(requestEntity);
        requestEntity.setTopologyHostGroupEntities(java.util.Collections.singletonList(hostGroupEntity));
        org.apache.ambari.server.orm.entities.TopologyHostInfoEntity hostInfoEntity = new org.apache.ambari.server.orm.entities.TopologyHostInfoEntity();
        hostInfoEntity.setTopologyHostGroupEntity(hostGroupEntity);
        hostGroupEntity.setTopologyHostInfoEntities(java.util.Collections.singletonList(hostInfoEntity));
        hostInfoEntity.setFqdn("fqdn");
        hostInfoEntity.setHostCount(12);
        hostInfoEntity.setPredicate("predicate");
        requestDAO.create(requestEntity);
    }

    private void testRequestEntity(java.util.List<org.apache.ambari.server.orm.entities.TopologyRequestEntity> requestEntities) {
        junit.framework.Assert.assertEquals(1, requestEntities.size());
        org.apache.ambari.server.orm.entities.TopologyRequestEntity requestEntity = requestEntities.iterator().next();
        junit.framework.Assert.assertEquals("a1", requestEntity.getAction());
        junit.framework.Assert.assertEquals("bp1", requestEntity.getBlueprintName());
        junit.framework.Assert.assertEquals("attributes", requestEntity.getClusterAttributes());
        junit.framework.Assert.assertEquals("properties", requestEntity.getClusterProperties());
        junit.framework.Assert.assertEquals("description", requestEntity.getDescription());
        java.util.Collection<org.apache.ambari.server.orm.entities.TopologyHostGroupEntity> hostGroupEntities = requestEntity.getTopologyHostGroupEntities();
        junit.framework.Assert.assertEquals(1, hostGroupEntities.size());
        org.apache.ambari.server.orm.entities.TopologyHostGroupEntity hostGroupEntity = hostGroupEntities.iterator().next();
        junit.framework.Assert.assertEquals("hg1", hostGroupEntity.getName());
        junit.framework.Assert.assertEquals("hg-attributes", hostGroupEntity.getGroupAttributes());
        junit.framework.Assert.assertEquals("hg-properties", hostGroupEntity.getGroupProperties());
        junit.framework.Assert.assertEquals(requestEntity.getId(), hostGroupEntity.getRequestId());
        java.util.Collection<org.apache.ambari.server.orm.entities.TopologyHostInfoEntity> infoEntities = hostGroupEntity.getTopologyHostInfoEntities();
        junit.framework.Assert.assertEquals(1, infoEntities.size());
        org.apache.ambari.server.orm.entities.TopologyHostInfoEntity infoEntity = infoEntities.iterator().next();
        junit.framework.Assert.assertEquals("hg1", hostGroupEntity.getName());
        junit.framework.Assert.assertEquals(hostGroupEntity.getId(), infoEntity.getGroupId());
        junit.framework.Assert.assertEquals("fqdn", infoEntity.getFqdn());
        junit.framework.Assert.assertEquals(12, infoEntity.getHostCount().intValue());
        junit.framework.Assert.assertEquals("predicate", infoEntity.getPredicate());
    }

    @org.junit.Test
    public void testAndFindAll() throws java.lang.Exception {
        create();
        testRequestEntity(requestDAO.findAll());
    }

    @org.junit.Test
    public void testFindByClusterId() throws java.lang.Exception {
        create();
        testRequestEntity(requestDAO.findByClusterId(clusterId));
    }

    @org.junit.Test
    public void testRemoveAll() throws java.lang.Exception {
        create();
        requestDAO.removeAll(clusterId);
        java.util.List<org.apache.ambari.server.orm.entities.TopologyRequestEntity> requestEntities = requestDAO.findByClusterId(clusterId);
        junit.framework.Assert.assertEquals("All topology request entities associated with cluster should be removed !", 0, requestEntities.size());
    }
}