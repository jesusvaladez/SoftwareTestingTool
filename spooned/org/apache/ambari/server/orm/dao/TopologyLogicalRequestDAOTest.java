package org.apache.ambari.server.orm.dao;
public class TopologyLogicalRequestDAOTest {
    private com.google.inject.Injector injector;

    private org.apache.ambari.server.orm.dao.TopologyRequestDAO requestDAO;

    private org.apache.ambari.server.orm.dao.TopologyLogicalRequestDAO logicalRequestDAO;

    private org.apache.ambari.server.orm.dao.TopologyHostGroupDAO hostGroupDAO;

    org.apache.ambari.server.orm.OrmTestHelper helper;

    java.lang.Long clusterId;

    @org.junit.Before
    public void setup() throws java.lang.Exception {
        injector = com.google.inject.Guice.createInjector(new org.apache.ambari.server.orm.InMemoryDefaultTestModule());
        injector.getInstance(org.apache.ambari.server.orm.GuiceJpaInitializer.class);
        requestDAO = injector.getInstance(org.apache.ambari.server.orm.dao.TopologyRequestDAO.class);
        logicalRequestDAO = injector.getInstance(org.apache.ambari.server.orm.dao.TopologyLogicalRequestDAO.class);
        hostGroupDAO = injector.getInstance(org.apache.ambari.server.orm.dao.TopologyHostGroupDAO.class);
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
        requestDAO.create(requestEntity);
        java.util.List<org.apache.ambari.server.orm.entities.TopologyRequestEntity> requestEntities = requestDAO.findAll();
        junit.framework.Assert.assertEquals(1, requestEntities.size());
        requestEntity = requestEntities.iterator().next();
        org.apache.ambari.server.orm.entities.TopologyHostGroupEntity hostGroupEntity = new org.apache.ambari.server.orm.entities.TopologyHostGroupEntity();
        hostGroupEntity.setName("hg1");
        hostGroupEntity.setGroupProperties("test");
        hostGroupEntity.setGroupAttributes("test");
        hostGroupEntity.setTopologyRequestEntity(requestEntity);
        org.apache.ambari.server.orm.entities.TopologyHostInfoEntity hostInfoEntity = new org.apache.ambari.server.orm.entities.TopologyHostInfoEntity();
        hostInfoEntity.setHostCount(1);
        hostInfoEntity.setPredicate("test");
        hostInfoEntity.setFqdn("fqdn");
        hostInfoEntity.setTopologyHostGroupEntity(hostGroupEntity);
        hostGroupDAO.create(hostGroupEntity);
        java.util.List<org.apache.ambari.server.orm.entities.TopologyHostGroupEntity> hostGroupEntities = hostGroupDAO.findAll();
        junit.framework.Assert.assertEquals(1, hostGroupEntities.size());
        hostGroupEntity = hostGroupEntities.iterator().next();
        org.apache.ambari.server.orm.entities.TopologyLogicalRequestEntity logicalRequestEntity = new org.apache.ambari.server.orm.entities.TopologyLogicalRequestEntity();
        logicalRequestEntity.setId(1L);
        logicalRequestEntity.setDescription("description");
        logicalRequestEntity.setTopologyRequestEntity(requestEntity);
        logicalRequestEntity.setTopologyRequestId(requestEntity.getId());
        org.apache.ambari.server.orm.entities.TopologyHostRequestEntity hostRequestEntity = new org.apache.ambari.server.orm.entities.TopologyHostRequestEntity();
        hostGroupEntity.setId(1L);
        hostRequestEntity.setHostName("h1");
        hostRequestEntity.setStageId(1L);
        hostRequestEntity.setTopologyLogicalRequestEntity(logicalRequestEntity);
        hostRequestEntity.setTopologyHostGroupEntity(hostGroupEntity);
        org.apache.ambari.server.orm.entities.TopologyHostTaskEntity hostTaskEntity = new org.apache.ambari.server.orm.entities.TopologyHostTaskEntity();
        hostTaskEntity.setType("type");
        hostTaskEntity.setTopologyHostRequestEntity(hostRequestEntity);
        org.apache.ambari.server.orm.entities.TopologyLogicalTaskEntity logicalTaskEntity = new org.apache.ambari.server.orm.entities.TopologyLogicalTaskEntity();
        logicalTaskEntity.setComponentName("NAMENODE");
        logicalTaskEntity.setHostRoleCommandEntity(null);
        logicalTaskEntity.setTopologyHostTaskEntity(hostTaskEntity);
        hostGroupEntity.setTopologyHostRequestEntities(java.util.Collections.singletonList(hostRequestEntity));
        hostRequestEntity.setTopologyHostTaskEntities(java.util.Collections.singletonList(hostTaskEntity));
        hostRequestEntity.setTopologyHostGroupEntity(hostGroupEntity);
        hostTaskEntity.setTopologyLogicalTaskEntities(java.util.Collections.singletonList(logicalTaskEntity));
        logicalRequestEntity.setTopologyHostRequestEntities(java.util.Collections.singletonList(hostRequestEntity));
        logicalRequestDAO.create(logicalRequestEntity);
    }

    @org.junit.Test
    @org.junit.Ignore
    public void testFindAll() throws java.lang.Exception {
        create();
        java.util.List<org.apache.ambari.server.orm.entities.TopologyLogicalRequestEntity> logicalRequestEntities = logicalRequestDAO.findAll();
        junit.framework.Assert.assertEquals(1, logicalRequestEntities.size());
        org.apache.ambari.server.orm.entities.TopologyLogicalRequestEntity logicalRequestEntity = logicalRequestEntities.iterator().next();
        junit.framework.Assert.assertNotNull(logicalRequestEntity.getTopologyRequestId());
        junit.framework.Assert.assertEquals(java.lang.Long.valueOf(1), logicalRequestEntity.getId());
        junit.framework.Assert.assertEquals("description", logicalRequestEntity.getDescription());
        junit.framework.Assert.assertNotNull(logicalRequestEntity.getTopologyRequestEntity());
        java.util.Collection<org.apache.ambari.server.orm.entities.TopologyHostRequestEntity> hostRequestEntities = logicalRequestEntity.getTopologyHostRequestEntities();
        junit.framework.Assert.assertEquals(1, hostRequestEntities.size());
        org.apache.ambari.server.orm.entities.TopologyHostRequestEntity hostRequestEntity = hostRequestEntities.iterator().next();
        junit.framework.Assert.assertNotNull(hostRequestEntity.getTopologyHostGroupEntity());
        junit.framework.Assert.assertEquals(hostRequestEntity.getTopologyHostGroupEntity().getId(), hostRequestEntity.getHostGroupId());
        java.util.Collection<org.apache.ambari.server.orm.entities.TopologyHostTaskEntity> taskEntities = hostRequestEntity.getTopologyHostTaskEntities();
        junit.framework.Assert.assertEquals(1, taskEntities.size());
        org.apache.ambari.server.orm.entities.TopologyHostTaskEntity taskEntity = taskEntities.iterator().next();
        junit.framework.Assert.assertNotNull(taskEntity.getTopologyHostRequestEntity());
        junit.framework.Assert.assertNotNull(taskEntity.getTopologyLogicalTaskEntities());
        junit.framework.Assert.assertEquals(1, taskEntity.getTopologyLogicalTaskEntities().size());
        junit.framework.Assert.assertNotNull(taskEntity.getTopologyLogicalTaskEntities().iterator().next().getTopologyHostTaskEntity());
    }
}