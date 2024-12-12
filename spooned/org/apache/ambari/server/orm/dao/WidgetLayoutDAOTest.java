package org.apache.ambari.server.orm.dao;
public class WidgetLayoutDAOTest {
    private static com.google.inject.Injector injector;

    private org.apache.ambari.server.orm.dao.WidgetLayoutDAO widgetLayoutDAO;

    private org.apache.ambari.server.orm.dao.WidgetDAO widgetDAO;

    org.apache.ambari.server.orm.OrmTestHelper helper;

    java.lang.Long clusterId;

    @org.junit.Before
    public void before() throws java.lang.Exception {
        org.apache.ambari.server.orm.dao.WidgetLayoutDAOTest.injector = com.google.inject.Guice.createInjector(new org.apache.ambari.server.orm.InMemoryDefaultTestModule());
        widgetLayoutDAO = org.apache.ambari.server.orm.dao.WidgetLayoutDAOTest.injector.getInstance(org.apache.ambari.server.orm.dao.WidgetLayoutDAO.class);
        widgetDAO = org.apache.ambari.server.orm.dao.WidgetLayoutDAOTest.injector.getInstance(org.apache.ambari.server.orm.dao.WidgetDAO.class);
        org.apache.ambari.server.orm.dao.WidgetLayoutDAOTest.injector.getInstance(org.apache.ambari.server.orm.GuiceJpaInitializer.class);
        helper = org.apache.ambari.server.orm.dao.WidgetLayoutDAOTest.injector.getInstance(org.apache.ambari.server.orm.OrmTestHelper.class);
        clusterId = helper.createCluster();
    }

    private void createRecords() {
        org.apache.ambari.server.orm.entities.WidgetLayoutEntity widgetLayoutEntity = new org.apache.ambari.server.orm.entities.WidgetLayoutEntity();
        widgetLayoutEntity.setClusterId(clusterId);
        widgetLayoutEntity.setLayoutName("layout name0");
        widgetLayoutEntity.setSectionName("section0");
        widgetLayoutEntity.setUserName("username");
        widgetLayoutEntity.setScope("CLUSTER");
        widgetLayoutEntity.setDisplayName("displ_name");
        org.apache.ambari.server.orm.entities.WidgetLayoutEntity widgetLayoutEntity2 = new org.apache.ambari.server.orm.entities.WidgetLayoutEntity();
        widgetLayoutEntity2.setClusterId(clusterId);
        widgetLayoutEntity2.setLayoutName("layout name1");
        widgetLayoutEntity2.setSectionName("section1");
        widgetLayoutEntity2.setUserName("username");
        widgetLayoutEntity2.setScope("CLUSTER");
        widgetLayoutEntity2.setDisplayName("displ_name2");
        java.util.List<org.apache.ambari.server.orm.entities.WidgetLayoutUserWidgetEntity> widgetLayoutUserWidgetEntityList = new java.util.LinkedList<>();
        for (int i = 0; i < 3; i++) {
            org.apache.ambari.server.orm.entities.WidgetEntity widgetEntity = new org.apache.ambari.server.orm.entities.WidgetEntity();
            widgetEntity.setDefaultSectionName("display name" + i);
            widgetEntity.setAuthor("author");
            widgetEntity.setClusterId(clusterId);
            widgetEntity.setMetrics("metrics");
            widgetEntity.setDescription("description");
            widgetEntity.setProperties("{\"warning_threshold\": 0.5,\"error_threshold\": 0.7 }");
            widgetEntity.setScope("CLUSTER");
            widgetEntity.setWidgetName("widget" + i);
            widgetEntity.setWidgetType("GAUGE");
            widgetEntity.setWidgetValues("${`jvmMemoryHeapUsed + jvmMemoryHeapMax`}");
            org.apache.ambari.server.orm.entities.WidgetLayoutUserWidgetEntity widgetLayoutUserWidget = new org.apache.ambari.server.orm.entities.WidgetLayoutUserWidgetEntity();
            widgetLayoutUserWidget.setWidget(widgetEntity);
            widgetLayoutUserWidget.setWidgetLayout(widgetLayoutEntity);
            widgetLayoutUserWidget.setWidgetOrder(0);
            widgetLayoutUserWidgetEntityList.add(widgetLayoutUserWidget);
        }
        widgetLayoutEntity.setListWidgetLayoutUserWidgetEntity(widgetLayoutUserWidgetEntityList);
        widgetLayoutDAO.create(widgetLayoutEntity);
        widgetLayoutDAO.create(widgetLayoutEntity2);
    }

    @org.junit.Test
    public void testFindByCluster() {
        createRecords();
        org.junit.Assert.assertEquals(0, widgetLayoutDAO.findByCluster(99999).size());
        org.junit.Assert.assertEquals(2, widgetLayoutDAO.findByCluster(clusterId).size());
    }

    @org.junit.Test
    public void testFindBySectionName() {
        createRecords();
        org.junit.Assert.assertEquals(0, widgetLayoutDAO.findBySectionName("non existing").size());
        java.util.List<org.apache.ambari.server.orm.entities.WidgetLayoutEntity> widgetLayoutEntityList1 = widgetLayoutDAO.findBySectionName("section0");
        java.util.List<org.apache.ambari.server.orm.entities.WidgetLayoutEntity> widgetLayoutEntityList2 = widgetLayoutDAO.findBySectionName("section1");
        org.junit.Assert.assertEquals(1, widgetLayoutEntityList1.size());
        org.junit.Assert.assertEquals(1, widgetLayoutEntityList2.size());
        org.junit.Assert.assertEquals(3, widgetLayoutEntityList1.get(0).getListWidgetLayoutUserWidgetEntity().size());
    }

    @org.junit.Test
    public void testFindAll() {
        createRecords();
        org.junit.Assert.assertEquals(2, widgetLayoutDAO.findAll().size());
    }

    @org.junit.After
    public void after() throws org.apache.ambari.server.AmbariException, java.sql.SQLException {
        org.apache.ambari.server.H2DatabaseCleaner.clearDatabaseAndStopPersistenceService(org.apache.ambari.server.orm.dao.WidgetLayoutDAOTest.injector);
        org.apache.ambari.server.orm.dao.WidgetLayoutDAOTest.injector = null;
    }
}