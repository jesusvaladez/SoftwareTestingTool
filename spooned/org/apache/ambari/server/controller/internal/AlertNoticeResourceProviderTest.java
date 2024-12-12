package org.apache.ambari.server.controller.internal;
import org.easymock.EasyMock;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.createStrictMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
public class AlertNoticeResourceProviderTest {
    private org.apache.ambari.server.orm.dao.AlertDispatchDAO m_dao = null;

    private com.google.inject.Injector m_injector;

    @org.junit.Before
    public void before() {
        m_dao = EasyMock.createStrictMock(org.apache.ambari.server.orm.dao.AlertDispatchDAO.class);
        m_injector = com.google.inject.Guice.createInjector(com.google.inject.util.Modules.override(new org.apache.ambari.server.orm.InMemoryDefaultTestModule()).with(new org.apache.ambari.server.controller.internal.AlertNoticeResourceProviderTest.MockModule()));
        org.junit.Assert.assertNotNull(m_injector);
        org.apache.ambari.server.security.authorization.AuthorizationHelperInitializer.viewInstanceDAOReturningNull();
    }

    @org.junit.After
    public void clearAuthentication() {
        org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(null);
    }

    @org.junit.Test
    public void testGetResourcesNoPredicateAsAdministrator() throws java.lang.Exception {
        testGetResourcesNoPredicate(org.apache.ambari.server.security.TestAuthenticationFactory.createAdministrator());
    }

    @org.junit.Test
    public void testGetResourcesNoPredicateAsClusterAdministrator() throws java.lang.Exception {
        testGetResourcesNoPredicate(org.apache.ambari.server.security.TestAuthenticationFactory.createClusterAdministrator());
    }

    @org.junit.Test
    public void testGetResourcesNoPredicateAsServiceAdministrator() throws java.lang.Exception {
        testGetResourcesNoPredicate(org.apache.ambari.server.security.TestAuthenticationFactory.createServiceAdministrator());
    }

    @org.junit.Test
    public void testGetResourcesNoPredicateAsClusterUser() throws java.lang.Exception {
        testGetResourcesNoPredicate(org.apache.ambari.server.security.TestAuthenticationFactory.createClusterUser());
    }

    @org.junit.Test
    public void testGetResourcesNoPredicateAsViewUser() throws java.lang.Exception {
        testGetResourcesNoPredicate(org.apache.ambari.server.security.TestAuthenticationFactory.createViewUser(99L));
    }

    @java.lang.SuppressWarnings("unchecked")
    public void testGetResourcesNoPredicate(org.springframework.security.core.Authentication authentication) throws java.lang.Exception {
        org.apache.ambari.server.controller.internal.AlertNoticeResourceProvider provider = createProvider();
        org.apache.ambari.server.controller.spi.Request request = org.apache.ambari.server.controller.utilities.PropertyHelper.getReadRequest("AlertHistory/cluster_name", "AlertHistory/id");
        EasyMock.expect(m_dao.findAllNotices(org.easymock.EasyMock.anyObject(org.apache.ambari.server.controller.AlertNoticeRequest.class))).andReturn(java.util.Collections.emptyList());
        EasyMock.replay(m_dao);
        org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(authentication);
        java.util.Set<org.apache.ambari.server.controller.spi.Resource> results = provider.getResources(request, null);
        org.junit.Assert.assertEquals(0, results.size());
    }

    @org.junit.Test
    public void testGetResourcesClusterPredicateAsAdministrator() throws java.lang.Exception {
        testGetResourcesClusterPredicate(org.apache.ambari.server.security.TestAuthenticationFactory.createAdministrator());
    }

    @org.junit.Test
    public void testGetResourcesClusterPredicateAsClusterAdministrator() throws java.lang.Exception {
        testGetResourcesClusterPredicate(org.apache.ambari.server.security.TestAuthenticationFactory.createClusterAdministrator());
    }

    @org.junit.Test
    public void testGetResourcesClusterPredicateAsServiceAdministrator() throws java.lang.Exception {
        testGetResourcesClusterPredicate(org.apache.ambari.server.security.TestAuthenticationFactory.createServiceAdministrator());
    }

    @org.junit.Test
    public void testGetResourcesClusterPredicateAsClusterUser() throws java.lang.Exception {
        testGetResourcesClusterPredicate(org.apache.ambari.server.security.TestAuthenticationFactory.createClusterUser());
    }

    @org.junit.Test(expected = org.apache.ambari.server.security.authorization.AuthorizationException.class)
    public void testGetResourcesClusterPredicateAsViewUser() throws java.lang.Exception {
        testGetResourcesClusterPredicate(org.apache.ambari.server.security.TestAuthenticationFactory.createViewUser(99L));
    }

    protected void testGetResourcesClusterPredicate(org.springframework.security.core.Authentication authentication) throws java.lang.Exception {
        org.apache.ambari.server.controller.spi.Request request = org.apache.ambari.server.controller.utilities.PropertyHelper.getReadRequest(org.apache.ambari.server.controller.internal.AlertNoticeResourceProvider.ALERT_NOTICE_CLUSTER_NAME, org.apache.ambari.server.controller.internal.AlertNoticeResourceProvider.ALERT_NOTICE_ID, org.apache.ambari.server.controller.internal.AlertNoticeResourceProvider.ALERT_NOTICE_HISTORY_ID, org.apache.ambari.server.controller.internal.AlertNoticeResourceProvider.ALERT_NOTICE_SERVICE_NAME, org.apache.ambari.server.controller.internal.AlertNoticeResourceProvider.ALERT_NOTICE_TARGET_ID, org.apache.ambari.server.controller.internal.AlertNoticeResourceProvider.ALERT_NOTICE_TARGET_NAME, org.apache.ambari.server.controller.internal.AlertNoticeResourceProvider.ALERT_NOTICE_STATE);
        org.apache.ambari.server.controller.spi.Predicate predicate = new org.apache.ambari.server.controller.utilities.PredicateBuilder().property(org.apache.ambari.server.controller.internal.AlertNoticeResourceProvider.ALERT_NOTICE_CLUSTER_NAME).equals("c1").toPredicate();
        EasyMock.expect(m_dao.findAllNotices(org.easymock.EasyMock.anyObject(org.apache.ambari.server.controller.AlertNoticeRequest.class))).andReturn(getMockEntities());
        org.apache.ambari.server.state.Cluster cluster = EasyMock.createMock(org.apache.ambari.server.state.Cluster.class);
        EasyMock.expect(cluster.getResourceId()).andReturn(4L).anyTimes();
        EasyMock.expect(cluster.getClusterId()).andReturn(2L).anyTimes();
        org.apache.ambari.server.state.Clusters clusters = m_injector.getInstance(org.apache.ambari.server.state.Clusters.class);
        EasyMock.expect(clusters.getCluster("c1")).andReturn(cluster).anyTimes();
        org.apache.ambari.server.controller.AmbariManagementController amc = m_injector.getInstance(org.apache.ambari.server.controller.AmbariManagementController.class);
        EasyMock.replay(m_dao, amc, clusters, cluster);
        org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(authentication);
        org.apache.ambari.server.controller.internal.AlertNoticeResourceProvider provider = createProvider();
        java.util.Set<org.apache.ambari.server.controller.spi.Resource> results = provider.getResources(request, predicate);
        org.junit.Assert.assertEquals(1, results.size());
        org.apache.ambari.server.controller.spi.Resource r = results.iterator().next();
        org.junit.Assert.assertEquals("Administrators", r.getPropertyValue(org.apache.ambari.server.controller.internal.AlertNoticeResourceProvider.ALERT_NOTICE_TARGET_NAME));
        org.junit.Assert.assertEquals(org.apache.ambari.server.state.NotificationState.FAILED, r.getPropertyValue(org.apache.ambari.server.controller.internal.AlertNoticeResourceProvider.ALERT_NOTICE_STATE));
        EasyMock.verify(m_dao, amc, clusters, cluster);
    }

    @org.junit.Test
    public void testGetSingleResourceAsAdministrator() throws java.lang.Exception {
        testGetSingleResource(org.apache.ambari.server.security.TestAuthenticationFactory.createAdministrator());
    }

    @org.junit.Test
    public void testGetSingleResourceAsClusterAdministrator() throws java.lang.Exception {
        testGetSingleResource(org.apache.ambari.server.security.TestAuthenticationFactory.createClusterAdministrator());
    }

    @org.junit.Test
    public void testGetSingleResourceAsServiceAdministrator() throws java.lang.Exception {
        testGetSingleResource(org.apache.ambari.server.security.TestAuthenticationFactory.createServiceAdministrator());
    }

    @org.junit.Test
    public void testGetSingleResourceAsClusterUser() throws java.lang.Exception {
        testGetSingleResource(org.apache.ambari.server.security.TestAuthenticationFactory.createClusterUser());
    }

    @org.junit.Test(expected = org.apache.ambari.server.security.authorization.AuthorizationException.class)
    public void testGetSingleResourceAsViewUser() throws java.lang.Exception {
        testGetSingleResource(org.apache.ambari.server.security.TestAuthenticationFactory.createViewUser(99L));
    }

    protected void testGetSingleResource(org.springframework.security.core.Authentication authentication) throws java.lang.Exception {
        org.apache.ambari.server.controller.spi.Request request = org.apache.ambari.server.controller.utilities.PropertyHelper.getReadRequest(org.apache.ambari.server.controller.internal.AlertNoticeResourceProvider.ALERT_NOTICE_CLUSTER_NAME, org.apache.ambari.server.controller.internal.AlertNoticeResourceProvider.ALERT_NOTICE_ID, org.apache.ambari.server.controller.internal.AlertNoticeResourceProvider.ALERT_NOTICE_HISTORY_ID, org.apache.ambari.server.controller.internal.AlertNoticeResourceProvider.ALERT_NOTICE_SERVICE_NAME, org.apache.ambari.server.controller.internal.AlertNoticeResourceProvider.ALERT_NOTICE_TARGET_ID, org.apache.ambari.server.controller.internal.AlertNoticeResourceProvider.ALERT_NOTICE_TARGET_NAME, org.apache.ambari.server.controller.internal.AlertNoticeResourceProvider.ALERT_NOTICE_STATE);
        org.apache.ambari.server.controller.spi.Predicate predicate = new org.apache.ambari.server.controller.utilities.PredicateBuilder().property(org.apache.ambari.server.controller.internal.AlertNoticeResourceProvider.ALERT_NOTICE_CLUSTER_NAME).equals("c1").and().property(org.apache.ambari.server.controller.internal.AlertNoticeResourceProvider.ALERT_NOTICE_ID).equals("1").toPredicate();
        EasyMock.expect(m_dao.findAllNotices(org.easymock.EasyMock.anyObject(org.apache.ambari.server.controller.AlertNoticeRequest.class))).andReturn(getMockEntities());
        org.apache.ambari.server.state.Cluster cluster = EasyMock.createMock(org.apache.ambari.server.state.Cluster.class);
        EasyMock.expect(cluster.getResourceId()).andReturn(4L).anyTimes();
        EasyMock.expect(cluster.getClusterId()).andReturn(2L).anyTimes();
        org.apache.ambari.server.state.Clusters clusters = m_injector.getInstance(org.apache.ambari.server.state.Clusters.class);
        EasyMock.expect(clusters.getCluster("c1")).andReturn(cluster).anyTimes();
        org.apache.ambari.server.controller.AmbariManagementController amc = m_injector.getInstance(org.apache.ambari.server.controller.AmbariManagementController.class);
        EasyMock.replay(m_dao, amc, clusters, cluster);
        org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(authentication);
        org.apache.ambari.server.controller.internal.AlertNoticeResourceProvider provider = createProvider();
        java.util.Set<org.apache.ambari.server.controller.spi.Resource> results = provider.getResources(request, predicate);
        org.junit.Assert.assertEquals(1, results.size());
        org.apache.ambari.server.controller.spi.Resource r = results.iterator().next();
        org.junit.Assert.assertEquals("Administrators", r.getPropertyValue(org.apache.ambari.server.controller.internal.AlertNoticeResourceProvider.ALERT_NOTICE_TARGET_NAME));
        org.junit.Assert.assertEquals(org.apache.ambari.server.state.NotificationState.FAILED, r.getPropertyValue(org.apache.ambari.server.controller.internal.AlertNoticeResourceProvider.ALERT_NOTICE_STATE));
    }

    private org.apache.ambari.server.controller.internal.AlertNoticeResourceProvider createProvider() {
        return new org.apache.ambari.server.controller.internal.AlertNoticeResourceProvider(m_injector.getInstance(org.apache.ambari.server.controller.AmbariManagementController.class));
    }

    private java.util.List<org.apache.ambari.server.orm.entities.AlertNoticeEntity> getMockEntities() throws java.lang.Exception {
        org.apache.ambari.server.orm.entities.ResourceEntity clusterResource = new org.apache.ambari.server.orm.entities.ResourceEntity();
        clusterResource.setId(4L);
        org.apache.ambari.server.orm.entities.ClusterEntity cluster = new org.apache.ambari.server.orm.entities.ClusterEntity();
        cluster.setClusterName("c1");
        cluster.setClusterId(1L);
        cluster.setResource(clusterResource);
        org.apache.ambari.server.orm.entities.AlertDefinitionEntity definition = new org.apache.ambari.server.orm.entities.AlertDefinitionEntity();
        definition.setClusterId(1L);
        definition.setComponentName("NAMENODE");
        definition.setDefinitionName("namenode_definition");
        definition.setEnabled(true);
        definition.setServiceName("HDFS");
        definition.setCluster(cluster);
        org.apache.ambari.server.orm.entities.AlertHistoryEntity history = new org.apache.ambari.server.orm.entities.AlertHistoryEntity();
        history.setAlertId(1L);
        history.setAlertDefinition(definition);
        history.setClusterId(java.lang.Long.valueOf(1L));
        history.setComponentName(null);
        history.setAlertText("Mock Label");
        history.setServiceName("HDFS");
        history.setAlertState(org.apache.ambari.server.state.AlertState.WARNING);
        history.setAlertTimestamp(java.lang.System.currentTimeMillis());
        org.apache.ambari.server.orm.entities.AlertTargetEntity administrators = new org.apache.ambari.server.orm.entities.AlertTargetEntity();
        administrators.setDescription("The Administrators");
        administrators.setNotificationType("EMAIL");
        administrators.setTargetName("Administrators");
        org.apache.ambari.server.orm.entities.AlertNoticeEntity entity = new org.apache.ambari.server.orm.entities.AlertNoticeEntity();
        entity.setAlertHistory(history);
        entity.setAlertTarget(administrators);
        entity.setNotifyState(org.apache.ambari.server.state.NotificationState.FAILED);
        entity.setUuid(java.util.UUID.randomUUID().toString());
        return java.util.Arrays.asList(entity);
    }

    private class MockModule implements com.google.inject.Module {
        @java.lang.Override
        public void configure(com.google.inject.Binder binder) {
            org.apache.ambari.server.state.Clusters clusters = EasyMock.createMock(org.apache.ambari.server.state.Clusters.class);
            org.apache.ambari.server.controller.AmbariManagementController amc = EasyMock.createMock(org.apache.ambari.server.controller.AmbariManagementController.class);
            EasyMock.expect(amc.getClusters()).andReturn(clusters).anyTimes();
            binder.bind(org.apache.ambari.server.orm.dao.AlertDispatchDAO.class).toInstance(m_dao);
            binder.bind(org.apache.ambari.server.state.Clusters.class).toInstance(clusters);
            binder.bind(org.apache.ambari.server.controller.AmbariManagementController.class).toInstance(amc);
            binder.bind(org.apache.ambari.server.metadata.ActionMetadata.class);
        }
    }
}