package org.apache.ambari.server.state.alerts;
@org.junit.experimental.categories.Category({ category.AlertTest.class })
public class AlertEventPublisherTest {
    private org.apache.ambari.server.orm.dao.AlertDispatchDAO dispatchDao;

    private org.apache.ambari.server.orm.dao.AlertDefinitionDAO definitionDao;

    private org.apache.ambari.server.orm.dao.AlertsDAO alertsDao;

    private org.apache.ambari.server.state.Clusters clusters;

    private org.apache.ambari.server.state.Cluster cluster;

    private java.lang.String clusterName;

    private com.google.inject.Injector injector;

    private org.apache.ambari.server.state.ServiceFactory serviceFactory;

    private org.apache.ambari.server.orm.OrmTestHelper ormHelper;

    private org.apache.ambari.server.state.alert.AggregateDefinitionMapping aggregateMapping;

    private final java.lang.String STACK_VERSION = "2.0.6";

    private final java.lang.String REPO_VERSION = "2.0.6-1234";

    @org.junit.Before
    public void setup() throws java.lang.Exception {
        injector = com.google.inject.Guice.createInjector(new org.apache.ambari.server.orm.InMemoryDefaultTestModule());
        injector.getInstance(org.apache.ambari.server.orm.GuiceJpaInitializer.class);
        org.apache.ambari.server.utils.EventBusSynchronizer.synchronizeAmbariEventPublisher(injector);
        dispatchDao = injector.getInstance(org.apache.ambari.server.orm.dao.AlertDispatchDAO.class);
        definitionDao = injector.getInstance(org.apache.ambari.server.orm.dao.AlertDefinitionDAO.class);
        alertsDao = injector.getInstance(org.apache.ambari.server.orm.dao.AlertsDAO.class);
        clusters = injector.getInstance(org.apache.ambari.server.state.Clusters.class);
        serviceFactory = injector.getInstance(org.apache.ambari.server.state.ServiceFactory.class);
        ormHelper = injector.getInstance(org.apache.ambari.server.orm.OrmTestHelper.class);
        aggregateMapping = injector.getInstance(org.apache.ambari.server.state.alert.AggregateDefinitionMapping.class);
        clusterName = "foo";
        org.apache.ambari.server.state.StackId stackId = new org.apache.ambari.server.state.StackId("HDP", STACK_VERSION);
        ormHelper.createStack(stackId);
        clusters.addCluster(clusterName, stackId);
        cluster = clusters.getCluster(clusterName);
        junit.framework.Assert.assertNotNull(cluster);
    }

    @org.junit.After
    public void teardown() throws java.lang.Exception {
        org.apache.ambari.server.H2DatabaseCleaner.clearDatabaseAndStopPersistenceService(injector);
        injector = null;
    }

    @org.junit.Test
    public void testDefaultAlertGroupCreation() throws java.lang.Exception {
        junit.framework.Assert.assertEquals(0, dispatchDao.findAllGroups().size());
        installHdfsService();
        junit.framework.Assert.assertEquals(1, dispatchDao.findAllGroups().size());
    }

    @org.junit.Test
    public void testDefaultAlertGroupRemoved() throws java.lang.Exception {
        junit.framework.Assert.assertEquals(0, dispatchDao.findAllGroups().size());
        installHdfsService();
        junit.framework.Assert.assertEquals(1, dispatchDao.findAllGroups().size());
        cluster.getService("HDFS").delete(new org.apache.ambari.server.controller.internal.DeleteHostComponentStatusMetaData());
        junit.framework.Assert.assertEquals(0, dispatchDao.findAllGroups().size());
    }

    @org.junit.Test
    public void testAlertDefinitionInsertion() throws java.lang.Exception {
        junit.framework.Assert.assertEquals(0, definitionDao.findAll().size());
        installHdfsService();
        junit.framework.Assert.assertEquals(6, definitionDao.findAll().size());
    }

    @org.junit.Test
    public void testAlertDefinitionChanged() throws java.lang.Exception {
        installHdfsService();
        int definitionCount = definitionDao.findAll().size();
        org.apache.ambari.server.orm.entities.AlertDefinitionEntity definition = ormHelper.createAlertDefinition(cluster.getClusterId());
        junit.framework.Assert.assertEquals(definitionCount + 1, definitionDao.findAll().size());
        org.apache.ambari.server.state.alert.AggregateSource source = new org.apache.ambari.server.state.alert.AggregateSource();
        org.apache.ambari.server.state.alert.Reporting reporting = new org.apache.ambari.server.state.alert.Reporting();
        org.apache.ambari.server.state.alert.Reporting.ReportTemplate okTemplate = new org.apache.ambari.server.state.alert.Reporting.ReportTemplate();
        okTemplate.setValue(50.0);
        okTemplate.setText("foo");
        reporting.setOk(okTemplate);
        source.setReporting(reporting);
        source.setAlertName(definition.getDefinitionName());
        source.setType(org.apache.ambari.server.state.alert.SourceType.AGGREGATE);
        org.apache.ambari.server.orm.entities.AlertDefinitionEntity aggregateEntity = new org.apache.ambari.server.orm.entities.AlertDefinitionEntity();
        aggregateEntity.setClusterId(cluster.getClusterId());
        aggregateEntity.setComponentName("DATANODE");
        aggregateEntity.setEnabled(true);
        aggregateEntity.setDefinitionName("datanode_aggregate");
        aggregateEntity.setScope(org.apache.ambari.server.state.alert.Scope.ANY);
        aggregateEntity.setServiceName("HDFS");
        aggregateEntity.setSource(new com.google.gson.Gson().toJson(source));
        aggregateEntity.setHash(java.util.UUID.randomUUID().toString());
        aggregateEntity.setScheduleInterval(1);
        aggregateEntity.setSourceType(org.apache.ambari.server.state.alert.SourceType.AGGREGATE);
        definitionDao.create(aggregateEntity);
        org.apache.ambari.server.state.alert.AlertDefinition aggregate = aggregateMapping.getAggregateDefinition(cluster.getClusterId(), source.getAlertName());
        junit.framework.Assert.assertNotNull(aggregate);
        junit.framework.Assert.assertEquals("foo", aggregate.getSource().getReporting().getOk().getText());
        java.lang.String sourceText = aggregateEntity.getSource();
        sourceText = sourceText.replace("foo", "bar");
        aggregateEntity.setSource(sourceText);
        definitionDao.merge(aggregateEntity);
        aggregate = aggregateMapping.getAggregateDefinition(cluster.getClusterId(), source.getAlertName());
        junit.framework.Assert.assertNotNull(aggregate);
        junit.framework.Assert.assertEquals("bar", aggregate.getSource().getReporting().getOk().getText());
    }

    @org.junit.Test
    public void testAlertDefinitionNameChangeEvent() throws java.lang.Exception {
        installHdfsService();
        org.apache.ambari.server.orm.entities.AlertDefinitionEntity definition = definitionDao.findAll().get(0);
        org.apache.ambari.server.orm.entities.AlertHistoryEntity history = new org.apache.ambari.server.orm.entities.AlertHistoryEntity();
        history.setServiceName(definition.getServiceName());
        history.setClusterId(cluster.getClusterId());
        history.setAlertDefinition(definition);
        history.setAlertLabel(definition.getLabel());
        history.setAlertText(definition.getDefinitionName());
        history.setAlertTimestamp(java.lang.Long.valueOf(1L));
        history.setHostName(null);
        history.setAlertState(org.apache.ambari.server.state.AlertState.OK);
        alertsDao.create(history);
        org.apache.ambari.server.orm.entities.AlertHistoryEntity history2 = new org.apache.ambari.server.orm.entities.AlertHistoryEntity();
        history2.setServiceName(definition.getServiceName());
        history2.setClusterId(cluster.getClusterId());
        history2.setAlertDefinition(definition);
        history2.setAlertLabel(definition.getLabel());
        history2.setAlertText(definition.getDefinitionName());
        history2.setAlertTimestamp(java.lang.Long.valueOf(1L));
        history2.setHostName(null);
        history2.setAlertState(org.apache.ambari.server.state.AlertState.CRITICAL);
        org.apache.ambari.server.orm.entities.AlertCurrentEntity current = new org.apache.ambari.server.orm.entities.AlertCurrentEntity();
        current.setOriginalTimestamp(1L);
        current.setLatestTimestamp(2L);
        current.setAlertHistory(history2);
        alertsDao.create(current);
        definition.setLabel("testAlertDefinitionNameChangeEvent");
        definitionDao.merge(definition);
        history = alertsDao.findById(history.getAlertId());
        history2 = alertsDao.findById(history2.getAlertId());
        junit.framework.Assert.assertFalse(definition.getLabel().equals(history.getAlertLabel()));
        junit.framework.Assert.assertEquals(definition.getLabel(), history2.getAlertLabel());
    }

    @org.junit.Test
    public void testAlertDefinitionRemoval() throws java.lang.Exception {
        junit.framework.Assert.assertEquals(0, definitionDao.findAll().size());
        org.apache.ambari.server.orm.entities.AlertDefinitionEntity definition = ormHelper.createAlertDefinition(cluster.getClusterId());
        junit.framework.Assert.assertEquals(1, definitionDao.findAll().size());
        org.apache.ambari.server.state.alert.AggregateSource source = new org.apache.ambari.server.state.alert.AggregateSource();
        source.setAlertName(definition.getDefinitionName());
        org.apache.ambari.server.state.alert.AlertDefinition aggregate = new org.apache.ambari.server.state.alert.AlertDefinition();
        aggregate.setClusterId(cluster.getClusterId());
        aggregate.setComponentName("DATANODE");
        aggregate.setEnabled(true);
        aggregate.setInterval(1);
        aggregate.setLabel("DataNode Aggregate");
        aggregate.setName("datanode_aggregate");
        aggregate.setScope(org.apache.ambari.server.state.alert.Scope.ANY);
        aggregate.setServiceName("HDFS");
        aggregate.setSource(source);
        aggregate.setUuid("uuid");
        aggregateMapping.registerAggregate(cluster.getClusterId(), aggregate);
        junit.framework.Assert.assertNotNull(aggregateMapping.getAggregateDefinition(cluster.getClusterId(), source.getAlertName()));
        definitionDao.remove(definition);
        junit.framework.Assert.assertNull(aggregateMapping.getAggregateDefinition(cluster.getClusterId(), source.getAlertName()));
    }

    private void installHdfsService() throws java.lang.Exception {
        org.apache.ambari.server.orm.entities.RepositoryVersionEntity repositoryVersion = ormHelper.getOrCreateRepositoryVersion(cluster.getCurrentStackVersion(), REPO_VERSION);
        java.lang.String serviceName = "HDFS";
        serviceFactory.createNew(cluster, serviceName, repositoryVersion);
        junit.framework.Assert.assertNotNull(cluster.getService(serviceName));
    }
}