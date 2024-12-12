package org.apache.ambari.server.orm.dao;
public class RequestScheduleDAOTest {
    private com.google.inject.Injector injector;

    private org.apache.ambari.server.orm.dao.HostDAO hostDAO;

    private org.apache.ambari.server.orm.dao.ClusterDAO clusterDAO;

    private org.apache.ambari.server.orm.dao.RequestScheduleDAO requestScheduleDAO;

    private org.apache.ambari.server.orm.dao.RequestScheduleBatchRequestDAO batchRequestDAO;

    private org.apache.ambari.server.orm.dao.ResourceTypeDAO resourceTypeDAO;

    private java.lang.String testUri = "http://localhost/blah";

    private java.lang.String testBody = "ValidJson";

    private java.lang.String testType = org.apache.ambari.server.state.scheduler.BatchRequest.Type.POST.name();

    @org.junit.Before
    public void setup() throws java.lang.Exception {
        injector = com.google.inject.Guice.createInjector(new org.apache.ambari.server.orm.InMemoryDefaultTestModule());
        injector.getInstance(org.apache.ambari.server.orm.GuiceJpaInitializer.class);
        injector.getInstance(org.apache.ambari.server.api.services.AmbariMetaInfo.class);
        clusterDAO = injector.getInstance(org.apache.ambari.server.orm.dao.ClusterDAO.class);
        hostDAO = injector.getInstance(org.apache.ambari.server.orm.dao.HostDAO.class);
        clusterDAO = injector.getInstance(org.apache.ambari.server.orm.dao.ClusterDAO.class);
        requestScheduleDAO = injector.getInstance(org.apache.ambari.server.orm.dao.RequestScheduleDAO.class);
        batchRequestDAO = injector.getInstance(org.apache.ambari.server.orm.dao.RequestScheduleBatchRequestDAO.class);
        resourceTypeDAO = injector.getInstance(org.apache.ambari.server.orm.dao.ResourceTypeDAO.class);
    }

    @org.junit.After
    public void teardown() throws org.apache.ambari.server.AmbariException, java.sql.SQLException {
        org.apache.ambari.server.H2DatabaseCleaner.clearDatabaseAndStopPersistenceService(injector);
    }

    private org.apache.ambari.server.orm.entities.RequestScheduleEntity createScheduleEntity() {
        org.apache.ambari.server.orm.entities.RequestScheduleEntity scheduleEntity = new org.apache.ambari.server.orm.entities.RequestScheduleEntity();
        org.apache.ambari.server.orm.entities.ResourceTypeEntity resourceTypeEntity = resourceTypeDAO.findById(org.apache.ambari.server.security.authorization.ResourceType.CLUSTER.getId());
        if (resourceTypeEntity == null) {
            resourceTypeEntity = new org.apache.ambari.server.orm.entities.ResourceTypeEntity();
            resourceTypeEntity.setId(org.apache.ambari.server.security.authorization.ResourceType.CLUSTER.getId());
            resourceTypeEntity.setName(org.apache.ambari.server.security.authorization.ResourceType.CLUSTER.name());
            resourceTypeEntity = resourceTypeDAO.merge(resourceTypeEntity);
        }
        org.apache.ambari.server.orm.dao.StackDAO stackDAO = injector.getInstance(org.apache.ambari.server.orm.dao.StackDAO.class);
        org.apache.ambari.server.orm.entities.StackEntity stackEntity = stackDAO.find("HDP", "2.2.0");
        org.apache.ambari.server.orm.entities.ResourceEntity resourceEntity = new org.apache.ambari.server.orm.entities.ResourceEntity();
        resourceEntity.setResourceType(resourceTypeEntity);
        org.apache.ambari.server.orm.entities.ClusterEntity clusterEntity = new org.apache.ambari.server.orm.entities.ClusterEntity();
        clusterEntity.setClusterName("c1");
        clusterEntity.setResource(resourceEntity);
        clusterEntity.setDesiredStack(stackEntity);
        clusterDAO.create(clusterEntity);
        scheduleEntity.setClusterEntity(clusterEntity);
        scheduleEntity.setClusterId(clusterEntity.getClusterId());
        scheduleEntity.setStatus("SCHEDULED");
        scheduleEntity.setMinutes("30");
        scheduleEntity.setHours("12");
        scheduleEntity.setDayOfWeek("*");
        scheduleEntity.setDaysOfMonth("*");
        scheduleEntity.setYear("*");
        requestScheduleDAO.create(scheduleEntity);
        org.apache.ambari.server.orm.entities.HostEntity hostEntity = new org.apache.ambari.server.orm.entities.HostEntity();
        hostEntity.setHostName("h1");
        hostEntity.setOsType("centOS");
        hostDAO.create(hostEntity);
        org.apache.ambari.server.orm.entities.RequestScheduleBatchRequestEntity batchRequestEntity = new org.apache.ambari.server.orm.entities.RequestScheduleBatchRequestEntity();
        batchRequestEntity.setBatchId(1L);
        batchRequestEntity.setScheduleId(scheduleEntity.getScheduleId());
        batchRequestEntity.setRequestScheduleEntity(scheduleEntity);
        batchRequestEntity.setRequestScheduleEntity(scheduleEntity);
        batchRequestEntity.setRequestType(testType);
        batchRequestEntity.setRequestUri(testUri);
        batchRequestEntity.setRequestBody(testBody);
        batchRequestDAO.create(batchRequestEntity);
        scheduleEntity.getRequestScheduleBatchRequestEntities().add(batchRequestEntity);
        scheduleEntity = requestScheduleDAO.merge(scheduleEntity);
        return scheduleEntity;
    }

    @org.junit.Test
    public void testCreateRequestSchedule() throws java.lang.Exception {
        org.apache.ambari.server.orm.entities.RequestScheduleEntity scheduleEntity = createScheduleEntity();
        junit.framework.Assert.assertTrue(scheduleEntity.getScheduleId() > 0);
        junit.framework.Assert.assertEquals("SCHEDULED", scheduleEntity.getStatus());
        junit.framework.Assert.assertEquals("12", scheduleEntity.getHours());
        org.apache.ambari.server.orm.entities.RequestScheduleBatchRequestEntity batchRequestEntity = scheduleEntity.getRequestScheduleBatchRequestEntities().iterator().next();
        junit.framework.Assert.assertNotNull(batchRequestEntity);
        junit.framework.Assert.assertEquals(testUri, batchRequestEntity.getRequestUri());
        junit.framework.Assert.assertEquals(testType, batchRequestEntity.getRequestType());
        junit.framework.Assert.assertEquals(testBody, batchRequestEntity.getRequestBodyAsString());
    }

    @org.junit.Test
    public void testFindById() throws java.lang.Exception {
        org.apache.ambari.server.orm.entities.RequestScheduleEntity scheduleEntity = createScheduleEntity();
        org.apache.ambari.server.orm.entities.RequestScheduleEntity testScheduleEntity = requestScheduleDAO.findById(scheduleEntity.getScheduleId());
        junit.framework.Assert.assertEquals(scheduleEntity, testScheduleEntity);
    }

    @org.junit.Test
    public void testFindByStatus() throws java.lang.Exception {
        org.apache.ambari.server.orm.entities.RequestScheduleEntity scheduleEntity = createScheduleEntity();
        java.util.List<org.apache.ambari.server.orm.entities.RequestScheduleEntity> scheduleEntities = requestScheduleDAO.findByStatus("SCHEDULED");
        junit.framework.Assert.assertNotNull(scheduleEntities);
        junit.framework.Assert.assertEquals(scheduleEntity, scheduleEntities.get(0));
    }
}