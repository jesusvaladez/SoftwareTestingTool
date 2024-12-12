package org.apache.ambari.server.orm.dao;
public class WidgetDAOTest {
    private static com.google.inject.Injector injector;

    private org.apache.ambari.server.orm.dao.WidgetDAO widgetDAO;

    private org.apache.ambari.server.orm.dao.WidgetLayoutDAO widgetLayoutDAO;

    org.apache.ambari.server.orm.OrmTestHelper helper;

    java.lang.Long clusterId;

    @org.junit.Before
    public void before() throws java.lang.Exception {
        org.apache.ambari.server.orm.dao.WidgetDAOTest.injector = com.google.inject.Guice.createInjector(new org.apache.ambari.server.orm.InMemoryDefaultTestModule());
        widgetDAO = org.apache.ambari.server.orm.dao.WidgetDAOTest.injector.getInstance(org.apache.ambari.server.orm.dao.WidgetDAO.class);
        widgetLayoutDAO = org.apache.ambari.server.orm.dao.WidgetDAOTest.injector.getInstance(org.apache.ambari.server.orm.dao.WidgetLayoutDAO.class);
        org.apache.ambari.server.orm.dao.WidgetDAOTest.injector.getInstance(org.apache.ambari.server.orm.GuiceJpaInitializer.class);
        helper = org.apache.ambari.server.orm.dao.WidgetDAOTest.injector.getInstance(org.apache.ambari.server.orm.OrmTestHelper.class);
        clusterId = helper.createCluster();
    }

    private void createRecords() {
        for (int i = 0; i < 3; i++) {
            org.apache.ambari.server.orm.entities.WidgetEntity widgetEntity = new org.apache.ambari.server.orm.entities.WidgetEntity();
            widgetEntity.setAuthor("author");
            widgetEntity.setDefaultSectionName("section_name");
            widgetEntity.setClusterId(clusterId);
            widgetEntity.setMetrics("metrics");
            widgetEntity.setDescription("description");
            widgetEntity.setProperties("{\"warning_threshold\": 0.5,\"error_threshold\": 0.7 }");
            widgetEntity.setScope("CLUSTER");
            widgetEntity.setWidgetName("widget" + i);
            widgetEntity.setWidgetType("GAUGE");
            widgetEntity.setWidgetValues("${`jvmMemoryHeapUsed + jvmMemoryHeapMax`}");
            widgetEntity.setListWidgetLayoutUserWidgetEntity(new java.util.LinkedList<>());
            final org.apache.ambari.server.orm.entities.WidgetLayoutEntity widgetLayoutEntity = new org.apache.ambari.server.orm.entities.WidgetLayoutEntity();
            widgetLayoutEntity.setClusterId(clusterId);
            widgetLayoutEntity.setLayoutName("layout name" + i);
            widgetLayoutEntity.setSectionName("section" + (i % 2));
            widgetLayoutEntity.setDisplayName("display_name");
            widgetLayoutEntity.setUserName("user_name");
            widgetLayoutEntity.setScope("CLUSTER");
            final org.apache.ambari.server.orm.entities.WidgetLayoutUserWidgetEntity widgetLayoutUserWidget = new org.apache.ambari.server.orm.entities.WidgetLayoutUserWidgetEntity();
            widgetLayoutUserWidget.setWidget(widgetEntity);
            widgetLayoutUserWidget.setWidgetLayout(widgetLayoutEntity);
            widgetLayoutUserWidget.setWidgetOrder(0);
            widgetEntity.getListWidgetLayoutUserWidgetEntity().add(widgetLayoutUserWidget);
            java.util.List<org.apache.ambari.server.orm.entities.WidgetLayoutUserWidgetEntity> widgetLayoutUserWidgetEntityList = new java.util.LinkedList<>();
            widgetLayoutUserWidgetEntityList.add(widgetLayoutUserWidget);
            widgetLayoutEntity.setListWidgetLayoutUserWidgetEntity(widgetLayoutUserWidgetEntityList);
            widgetLayoutDAO.create(widgetLayoutEntity);
        }
    }

    @org.junit.Test
    public void testFindByCluster() {
        createRecords();
        org.junit.Assert.assertEquals(0, widgetDAO.findByCluster(99999).size());
        org.junit.Assert.assertEquals(3, widgetDAO.findByCluster(clusterId).size());
    }

    @org.junit.Test
    public void testFindBySectionName() {
        createRecords();
        org.junit.Assert.assertEquals(0, widgetDAO.findBySectionName("non existing").size());
        org.junit.Assert.assertEquals(2, widgetDAO.findBySectionName("section0").size());
        org.junit.Assert.assertEquals(1, widgetDAO.findBySectionName("section1").size());
    }

    @org.junit.Test
    public void testFindAll() {
        createRecords();
        org.junit.Assert.assertEquals(3, widgetDAO.findAll().size());
    }

    @org.junit.Test
    public void testFindByName() {
        createRecords();
        org.junit.Assert.assertEquals(1, widgetDAO.findByName(clusterId, "widget0", "author", "section_name").size());
    }

    @org.junit.After
    public void after() throws org.apache.ambari.server.AmbariException, java.sql.SQLException {
        org.apache.ambari.server.H2DatabaseCleaner.clearDatabaseAndStopPersistenceService(org.apache.ambari.server.orm.dao.WidgetDAOTest.injector);
        org.apache.ambari.server.orm.dao.WidgetDAOTest.injector = null;
    }
}