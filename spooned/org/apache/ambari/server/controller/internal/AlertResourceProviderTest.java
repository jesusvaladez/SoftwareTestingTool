package org.apache.ambari.server.controller.internal;
import javax.persistence.EntityManager;
import org.easymock.EasyMock;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import static org.apache.ambari.server.configuration.Configuration.JDBC_IN_MEMORY_DRIVER;
import static org.apache.ambari.server.configuration.Configuration.JDBC_IN_MEMORY_URL;
import static org.easymock.EasyMock.capture;
import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
public class AlertResourceProviderTest {
    private static final java.lang.Long ALERT_VALUE_ID = 1000L;

    private static final java.lang.String ALERT_VALUE_LABEL = "My Label";

    private static final java.lang.Long ALERT_VALUE_TIMESTAMP = 1L;

    private static final java.lang.String ALERT_VALUE_TEXT = "My Text";

    private static final java.lang.String ALERT_VALUE_COMPONENT = "component";

    private static final java.lang.String ALERT_VALUE_HOSTNAME = "host";

    private static final java.lang.String ALERT_VALUE_SERVICE = "service";

    private org.apache.ambari.server.orm.dao.AlertsDAO m_dao;

    private com.google.inject.Injector m_injector;

    private org.apache.ambari.server.controller.AmbariManagementController m_amc;

    @org.junit.Before
    public void before() throws java.lang.Exception {
        m_dao = org.easymock.EasyMock.createNiceMock(org.apache.ambari.server.orm.dao.AlertsDAO.class);
        m_injector = com.google.inject.Guice.createInjector(com.google.inject.util.Modules.override(new org.apache.ambari.server.orm.InMemoryDefaultTestModule()).with(new org.apache.ambari.server.controller.internal.AlertResourceProviderTest.MockModule()));
        m_amc = m_injector.getInstance(org.apache.ambari.server.controller.AmbariManagementController.class);
        org.apache.ambari.server.state.Cluster cluster = org.easymock.EasyMock.createMock(org.apache.ambari.server.state.Cluster.class);
        org.apache.ambari.server.state.Clusters clusters = m_injector.getInstance(org.apache.ambari.server.state.Clusters.class);
        EasyMock.expect(m_amc.getClusters()).andReturn(clusters).atLeastOnce();
        EasyMock.expect(clusters.getCluster(EasyMock.capture(org.easymock.EasyMock.<java.lang.String>newCapture()))).andReturn(cluster).atLeastOnce();
        EasyMock.expect(cluster.getClusterId()).andReturn(1L).anyTimes();
        EasyMock.expect(cluster.getResourceId()).andReturn(4L).anyTimes();
        EasyMock.expect(cluster.getClusterProperty(org.apache.ambari.server.state.ConfigHelper.CLUSTER_ENV_ALERT_REPEAT_TOLERANCE, "1")).andReturn("1").atLeastOnce();
        EasyMock.replay(m_amc, clusters, cluster);
        org.apache.ambari.server.security.authorization.AuthorizationHelperInitializer.viewInstanceDAOReturningNull();
    }

    @org.junit.After
    public void clearAuthentication() {
        org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(null);
    }

    @org.junit.Test
    public void testGetClusterAsAdministrator() throws java.lang.Exception {
        testGetCluster(org.apache.ambari.server.security.TestAuthenticationFactory.createAdministrator());
    }

    @org.junit.Test
    public void testGetClusterAsClusterAdministrator() throws java.lang.Exception {
        testGetCluster(org.apache.ambari.server.security.TestAuthenticationFactory.createClusterAdministrator());
    }

    @org.junit.Test
    public void testGetClusterAsClusterUser() throws java.lang.Exception {
        testGetCluster(org.apache.ambari.server.security.TestAuthenticationFactory.createClusterUser());
    }

    @org.junit.Test(expected = org.apache.ambari.server.security.authorization.AuthorizationException.class)
    public void testGetClusterAsViewOnlyUser() throws java.lang.Exception {
        testGetCluster(org.apache.ambari.server.security.TestAuthenticationFactory.createViewUser(99L));
    }

    private void testGetCluster(org.springframework.security.core.Authentication authentication) throws java.lang.Exception {
        EasyMock.expect(m_dao.findAll(EasyMock.capture(org.easymock.EasyMock.<org.apache.ambari.server.controller.AlertCurrentRequest>newCapture()))).andReturn(getClusterMockEntities()).anyTimes();
        EasyMock.replay(m_dao);
        org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(authentication);
        org.apache.ambari.server.controller.spi.Request request = org.apache.ambari.server.controller.utilities.PropertyHelper.getReadRequest(org.apache.ambari.server.controller.internal.AlertResourceProvider.ALERT_ID, org.apache.ambari.server.controller.internal.AlertResourceProvider.ALERT_DEFINITION_NAME, org.apache.ambari.server.controller.internal.AlertResourceProvider.ALERT_LABEL);
        org.apache.ambari.server.controller.spi.Predicate predicate = new org.apache.ambari.server.controller.utilities.PredicateBuilder().property(org.apache.ambari.server.controller.internal.AlertResourceProvider.ALERT_CLUSTER_NAME).equals("c1").toPredicate();
        org.apache.ambari.server.controller.internal.AlertResourceProvider provider = createProvider();
        java.util.Set<org.apache.ambari.server.controller.spi.Resource> results = provider.getResources(request, predicate);
        org.junit.Assert.assertEquals(1, results.size());
        org.apache.ambari.server.controller.spi.Resource r = results.iterator().next();
        org.junit.Assert.assertEquals("c1", r.getPropertyValue(org.apache.ambari.server.controller.internal.AlertResourceProvider.ALERT_CLUSTER_NAME));
        EasyMock.verify(m_dao);
    }

    @org.junit.Test
    public void testGetServiceAsAdministrator() throws java.lang.Exception {
        testGetService(org.apache.ambari.server.security.TestAuthenticationFactory.createAdministrator());
    }

    @org.junit.Test
    public void testGetServiceAsClusterAdministrator() throws java.lang.Exception {
        testGetService(org.apache.ambari.server.security.TestAuthenticationFactory.createClusterAdministrator());
    }

    @org.junit.Test
    public void testGetServiceAsClusterUser() throws java.lang.Exception {
        testGetService(org.apache.ambari.server.security.TestAuthenticationFactory.createClusterUser());
    }

    @org.junit.Test(expected = org.apache.ambari.server.security.authorization.AuthorizationException.class)
    public void testGetServiceAsViewOnlyUser() throws java.lang.Exception {
        testGetService(org.apache.ambari.server.security.TestAuthenticationFactory.createViewUser(99L));
    }

    private void testGetService(org.springframework.security.core.Authentication authentication) throws java.lang.Exception {
        EasyMock.expect(m_dao.findAll(EasyMock.capture(org.easymock.EasyMock.<org.apache.ambari.server.controller.AlertCurrentRequest>newCapture()))).andReturn(getClusterMockEntities()).anyTimes();
        EasyMock.replay(m_dao);
        org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(authentication);
        org.apache.ambari.server.controller.spi.Request request = org.apache.ambari.server.controller.utilities.PropertyHelper.getReadRequest(org.apache.ambari.server.controller.internal.AlertResourceProvider.ALERT_ID, org.apache.ambari.server.controller.internal.AlertResourceProvider.ALERT_DEFINITION_NAME, org.apache.ambari.server.controller.internal.AlertResourceProvider.ALERT_LABEL);
        org.apache.ambari.server.controller.spi.Predicate predicate = new org.apache.ambari.server.controller.utilities.PredicateBuilder().property(org.apache.ambari.server.controller.internal.AlertResourceProvider.ALERT_CLUSTER_NAME).equals("c1").and().property(org.apache.ambari.server.controller.internal.AlertResourceProvider.ALERT_SERVICE).equals(org.apache.ambari.server.controller.internal.AlertResourceProviderTest.ALERT_VALUE_SERVICE).toPredicate();
        org.apache.ambari.server.controller.internal.AlertResourceProvider provider = createProvider();
        java.util.Set<org.apache.ambari.server.controller.spi.Resource> results = provider.getResources(request, predicate);
        org.junit.Assert.assertEquals(1, results.size());
        org.apache.ambari.server.controller.spi.Resource r = results.iterator().next();
        org.junit.Assert.assertEquals("c1", r.getPropertyValue(org.apache.ambari.server.controller.internal.AlertResourceProvider.ALERT_CLUSTER_NAME));
        org.junit.Assert.assertEquals(org.apache.ambari.server.controller.internal.AlertResourceProviderTest.ALERT_VALUE_SERVICE, r.getPropertyValue(org.apache.ambari.server.controller.internal.AlertResourceProvider.ALERT_SERVICE));
        EasyMock.verify(m_dao);
    }

    @org.junit.Test
    public void testGetHostAsAdministrator() throws java.lang.Exception {
        testGetHost(org.apache.ambari.server.security.TestAuthenticationFactory.createAdministrator());
    }

    @org.junit.Test
    public void testGetHostAsClusterAdministrator() throws java.lang.Exception {
        testGetHost(org.apache.ambari.server.security.TestAuthenticationFactory.createClusterAdministrator());
    }

    @org.junit.Test
    public void testGetHostAsClusterUser() throws java.lang.Exception {
        testGetHost(org.apache.ambari.server.security.TestAuthenticationFactory.createClusterUser());
    }

    @org.junit.Test(expected = org.apache.ambari.server.security.authorization.AuthorizationException.class)
    public void testGetHostAsViewOnlyUser() throws java.lang.Exception {
        testGetHost(org.apache.ambari.server.security.TestAuthenticationFactory.createViewUser(99L));
    }

    private void testGetHost(org.springframework.security.core.Authentication authentication) throws java.lang.Exception {
        EasyMock.expect(m_dao.findAll(EasyMock.capture(org.easymock.EasyMock.<org.apache.ambari.server.controller.AlertCurrentRequest>newCapture()))).andReturn(getClusterMockEntities()).anyTimes();
        EasyMock.replay(m_dao);
        org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(authentication);
        org.apache.ambari.server.controller.spi.Request request = org.apache.ambari.server.controller.utilities.PropertyHelper.getReadRequest(org.apache.ambari.server.controller.internal.AlertResourceProvider.ALERT_ID, org.apache.ambari.server.controller.internal.AlertResourceProvider.ALERT_DEFINITION_NAME, org.apache.ambari.server.controller.internal.AlertResourceProvider.ALERT_LABEL);
        org.apache.ambari.server.controller.spi.Predicate predicate = new org.apache.ambari.server.controller.utilities.PredicateBuilder().property(org.apache.ambari.server.controller.internal.AlertResourceProvider.ALERT_CLUSTER_NAME).equals("c1").and().property(org.apache.ambari.server.controller.internal.AlertResourceProvider.ALERT_HOST).equals(org.apache.ambari.server.controller.internal.AlertResourceProviderTest.ALERT_VALUE_HOSTNAME).toPredicate();
        org.apache.ambari.server.controller.internal.AlertResourceProvider provider = createProvider();
        java.util.Set<org.apache.ambari.server.controller.spi.Resource> results = provider.getResources(request, predicate);
        org.junit.Assert.assertEquals(1, results.size());
        org.apache.ambari.server.controller.spi.Resource r = results.iterator().next();
        org.junit.Assert.assertEquals("c1", r.getPropertyValue(org.apache.ambari.server.controller.internal.AlertResourceProvider.ALERT_CLUSTER_NAME));
        org.junit.Assert.assertEquals(org.apache.ambari.server.controller.internal.AlertResourceProviderTest.ALERT_VALUE_HOSTNAME, r.getPropertyValue(org.apache.ambari.server.controller.internal.AlertResourceProvider.ALERT_HOST));
        EasyMock.verify(m_dao);
    }

    @org.junit.Test
    public void testGetClusterSummaryAsAdministrator() throws java.lang.Exception {
        testGetClusterSummary(org.apache.ambari.server.security.TestAuthenticationFactory.createAdministrator());
    }

    @org.junit.Test
    public void testGetClusterSummaryAsClusterAdministrator() throws java.lang.Exception {
        testGetClusterSummary(org.apache.ambari.server.security.TestAuthenticationFactory.createClusterAdministrator());
    }

    @org.junit.Test
    public void testGetClusterSummaryAsClusterUser() throws java.lang.Exception {
        testGetClusterSummary(org.apache.ambari.server.security.TestAuthenticationFactory.createClusterUser());
    }

    @org.junit.Test(expected = org.apache.ambari.server.security.authorization.AuthorizationException.class)
    public void testGetClusterSummaryAsViewOnlyUser() throws java.lang.Exception {
        testGetClusterSummary(org.apache.ambari.server.security.TestAuthenticationFactory.createViewUser(99L));
    }

    private void testGetClusterSummary(org.springframework.security.core.Authentication authentication) throws java.lang.Exception {
        EasyMock.expect(m_dao.findAll(EasyMock.capture(org.easymock.EasyMock.<org.apache.ambari.server.controller.AlertCurrentRequest>newCapture()))).andReturn(getMockEntitiesManyStates()).anyTimes();
        EasyMock.replay(m_dao);
        org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(authentication);
        org.apache.ambari.server.controller.spi.Request request = org.apache.ambari.server.controller.utilities.PropertyHelper.getReadRequest(org.apache.ambari.server.controller.internal.AlertResourceProvider.ALERT_ID, org.apache.ambari.server.controller.internal.AlertResourceProvider.ALERT_DEFINITION_NAME, org.apache.ambari.server.controller.internal.AlertResourceProvider.ALERT_LABEL, org.apache.ambari.server.controller.internal.AlertResourceProvider.ALERT_STATE, org.apache.ambari.server.controller.internal.AlertResourceProvider.ALERT_ORIGINAL_TIMESTAMP);
        org.apache.ambari.server.controller.spi.Predicate predicate = new org.apache.ambari.server.controller.utilities.PredicateBuilder().property(org.apache.ambari.server.controller.internal.AlertResourceProvider.ALERT_CLUSTER_NAME).equals("c1").toPredicate();
        org.apache.ambari.server.controller.internal.AlertResourceProvider provider = createProvider();
        java.util.Set<org.apache.ambari.server.controller.spi.Resource> results = provider.getResources(request, predicate);
        EasyMock.verify(m_dao);
        org.apache.ambari.server.api.query.render.AlertSummaryRenderer renderer = new org.apache.ambari.server.api.query.render.AlertSummaryRenderer();
        org.apache.ambari.server.api.services.ResultImpl result = new org.apache.ambari.server.api.services.ResultImpl(true);
        org.apache.ambari.server.api.util.TreeNode<org.apache.ambari.server.controller.spi.Resource> resources = result.getResultTree();
        java.util.concurrent.atomic.AtomicInteger alertResourceId = new java.util.concurrent.atomic.AtomicInteger(1);
        for (org.apache.ambari.server.controller.spi.Resource resource : results) {
            resources.addChild(resource, "Alert " + alertResourceId.getAndIncrement());
        }
        org.apache.ambari.server.api.services.Result summary = renderer.finalizeResult(result);
        org.junit.Assert.assertNotNull(summary);
        org.apache.ambari.server.api.util.TreeNode<org.apache.ambari.server.controller.spi.Resource> summaryResultTree = summary.getResultTree();
        org.apache.ambari.server.api.util.TreeNode<org.apache.ambari.server.controller.spi.Resource> summaryResources = summaryResultTree.getChild("alerts_summary");
        org.apache.ambari.server.controller.spi.Resource summaryResource = summaryResources.getObject();
        org.apache.ambari.server.api.query.render.AlertStateSummary alertStateSummary = ((org.apache.ambari.server.api.query.render.AlertStateSummary) (summaryResource.getPropertyValue("alerts_summary")));
        org.junit.Assert.assertEquals(10, alertStateSummary.Ok.Count);
        org.junit.Assert.assertEquals(2, alertStateSummary.Warning.Count);
        org.junit.Assert.assertEquals(1, alertStateSummary.Critical.Count);
        org.junit.Assert.assertEquals(3, alertStateSummary.Unknown.Count);
    }

    @org.junit.Test
    public void testGetClusterGroupedSummaryAsAdministrator() throws java.lang.Exception {
        testGetClusterGroupedSummary(org.apache.ambari.server.security.TestAuthenticationFactory.createAdministrator());
    }

    @org.junit.Test
    public void testGetClusterGroupedSummaryAsClusterAdministrator() throws java.lang.Exception {
        testGetClusterGroupedSummary(org.apache.ambari.server.security.TestAuthenticationFactory.createClusterAdministrator());
    }

    @org.junit.Test
    public void testGetClusterGroupedSummaryAsClusterUser() throws java.lang.Exception {
        testGetClusterGroupedSummary(org.apache.ambari.server.security.TestAuthenticationFactory.createClusterUser());
    }

    @org.junit.Test(expected = org.apache.ambari.server.security.authorization.AuthorizationException.class)
    public void testGetClusterGroupedSummaryAsViewOnlyUser() throws java.lang.Exception {
        testGetClusterGroupedSummary(org.apache.ambari.server.security.TestAuthenticationFactory.createViewUser(99L));
    }

    @java.lang.SuppressWarnings("unchecked")
    private void testGetClusterGroupedSummary(org.springframework.security.core.Authentication authentication) throws java.lang.Exception {
        EasyMock.expect(m_dao.findAll(EasyMock.capture(org.easymock.EasyMock.<org.apache.ambari.server.controller.AlertCurrentRequest>newCapture()))).andReturn(getMockEntitiesManyStates()).anyTimes();
        EasyMock.replay(m_dao);
        org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(authentication);
        org.apache.ambari.server.controller.spi.Request request = org.apache.ambari.server.controller.utilities.PropertyHelper.getReadRequest(org.apache.ambari.server.controller.internal.AlertResourceProvider.ALERT_ID, org.apache.ambari.server.controller.internal.AlertResourceProvider.ALERT_DEFINITION_NAME, org.apache.ambari.server.controller.internal.AlertResourceProvider.ALERT_LABEL, org.apache.ambari.server.controller.internal.AlertResourceProvider.ALERT_STATE, org.apache.ambari.server.controller.internal.AlertResourceProvider.ALERT_ORIGINAL_TIMESTAMP, org.apache.ambari.server.controller.internal.AlertResourceProvider.ALERT_TEXT);
        org.apache.ambari.server.controller.spi.Predicate predicate = new org.apache.ambari.server.controller.utilities.PredicateBuilder().property(org.apache.ambari.server.controller.internal.AlertResourceProvider.ALERT_CLUSTER_NAME).equals("c1").toPredicate();
        org.apache.ambari.server.controller.internal.AlertResourceProvider provider = createProvider();
        java.util.Set<org.apache.ambari.server.controller.spi.Resource> results = provider.getResources(request, predicate);
        EasyMock.verify(m_dao);
        org.apache.ambari.server.api.query.render.AlertSummaryGroupedRenderer renderer = new org.apache.ambari.server.api.query.render.AlertSummaryGroupedRenderer();
        org.apache.ambari.server.api.services.ResultImpl result = new org.apache.ambari.server.api.services.ResultImpl(true);
        org.apache.ambari.server.api.util.TreeNode<org.apache.ambari.server.controller.spi.Resource> resources = result.getResultTree();
        java.util.concurrent.atomic.AtomicInteger alertResourceId = new java.util.concurrent.atomic.AtomicInteger(1);
        for (org.apache.ambari.server.controller.spi.Resource resource : results) {
            resources.addChild(resource, "Alert " + alertResourceId.getAndIncrement());
        }
        org.apache.ambari.server.api.services.Result groupedSummary = renderer.finalizeResult(result);
        org.junit.Assert.assertNotNull(groupedSummary);
        org.apache.ambari.server.api.util.TreeNode<org.apache.ambari.server.controller.spi.Resource> summaryResultTree = groupedSummary.getResultTree();
        org.apache.ambari.server.api.util.TreeNode<org.apache.ambari.server.controller.spi.Resource> summaryResources = summaryResultTree.getChild("alerts_summary_grouped");
        org.apache.ambari.server.controller.spi.Resource summaryResource = summaryResources.getObject();
        java.util.List<org.apache.ambari.server.api.query.render.AlertSummaryGroupedRenderer.AlertDefinitionSummary> summaryList = ((java.util.List<org.apache.ambari.server.api.query.render.AlertSummaryGroupedRenderer.AlertDefinitionSummary>) (summaryResource.getPropertyValue("alerts_summary_grouped")));
        org.junit.Assert.assertEquals(4, summaryList.size());
        org.apache.ambari.server.api.query.render.AlertSummaryGroupedRenderer.AlertDefinitionSummary nnSummary = null;
        org.apache.ambari.server.api.query.render.AlertSummaryGroupedRenderer.AlertDefinitionSummary rmSummary = null;
        org.apache.ambari.server.api.query.render.AlertSummaryGroupedRenderer.AlertDefinitionSummary hiveSummary = null;
        org.apache.ambari.server.api.query.render.AlertSummaryGroupedRenderer.AlertDefinitionSummary flumeSummary = null;
        for (org.apache.ambari.server.api.query.render.AlertSummaryGroupedRenderer.AlertDefinitionSummary summary : summaryList) {
            if (summary.Name.equals("hdfs_namenode")) {
                nnSummary = summary;
            } else if (summary.Name.equals("yarn_resourcemanager")) {
                rmSummary = summary;
            } else if (summary.Name.equals("hive_server")) {
                hiveSummary = summary;
            } else if (summary.Name.equals("flume_handler")) {
                flumeSummary = summary;
            }
        }
        org.junit.Assert.assertNotNull(nnSummary);
        org.junit.Assert.assertNotNull(rmSummary);
        org.junit.Assert.assertNotNull(hiveSummary);
        org.junit.Assert.assertNotNull(flumeSummary);
        org.junit.Assert.assertEquals(10, nnSummary.State.Ok.Count);
        org.junit.Assert.assertEquals(org.apache.ambari.server.controller.internal.AlertResourceProviderTest.ALERT_VALUE_TEXT, nnSummary.State.Ok.AlertText);
        org.junit.Assert.assertEquals(0, nnSummary.State.Warning.Count);
        org.junit.Assert.assertEquals(0, nnSummary.State.Critical.Count);
        org.junit.Assert.assertEquals(0, nnSummary.State.Unknown.Count);
        org.junit.Assert.assertEquals(0, rmSummary.State.Ok.Count);
        org.junit.Assert.assertEquals(2, rmSummary.State.Warning.Count);
        org.junit.Assert.assertEquals(org.apache.ambari.server.controller.internal.AlertResourceProviderTest.ALERT_VALUE_TEXT, rmSummary.State.Warning.AlertText);
        org.junit.Assert.assertEquals(0, rmSummary.State.Critical.Count);
        org.junit.Assert.assertEquals(0, rmSummary.State.Unknown.Count);
        org.junit.Assert.assertEquals(0, hiveSummary.State.Ok.Count);
        org.junit.Assert.assertEquals(0, hiveSummary.State.Warning.Count);
        org.junit.Assert.assertEquals(1, hiveSummary.State.Critical.Count);
        org.junit.Assert.assertEquals(org.apache.ambari.server.controller.internal.AlertResourceProviderTest.ALERT_VALUE_TEXT, hiveSummary.State.Critical.AlertText);
        org.junit.Assert.assertEquals(0, hiveSummary.State.Unknown.Count);
        org.junit.Assert.assertEquals(0, flumeSummary.State.Ok.Count);
        org.junit.Assert.assertEquals(0, flumeSummary.State.Warning.Count);
        org.junit.Assert.assertEquals(0, flumeSummary.State.Critical.Count);
        org.junit.Assert.assertEquals(3, flumeSummary.State.Unknown.Count);
        org.junit.Assert.assertEquals(org.apache.ambari.server.controller.internal.AlertResourceProviderTest.ALERT_VALUE_TEXT, flumeSummary.State.Unknown.AlertText);
    }

    @org.junit.Test
    public void testGetClusterGroupedSummaryMaintenanceCountsAsAdministrator() throws java.lang.Exception {
        testGetClusterGroupedSummaryMaintenanceCounts(org.apache.ambari.server.security.TestAuthenticationFactory.createAdministrator());
    }

    @org.junit.Test
    public void testGetClusterGroupedSummaryMaintenanceCountsAsClusterAdministrator() throws java.lang.Exception {
        testGetClusterGroupedSummaryMaintenanceCounts(org.apache.ambari.server.security.TestAuthenticationFactory.createClusterAdministrator());
    }

    @org.junit.Test
    public void testGetClusterGroupedSummaryMaintenanceCountsAsClusterUser() throws java.lang.Exception {
        testGetClusterGroupedSummaryMaintenanceCounts(org.apache.ambari.server.security.TestAuthenticationFactory.createClusterUser());
    }

    @org.junit.Test(expected = org.apache.ambari.server.security.authorization.AuthorizationException.class)
    public void testGetClusterGroupedSummaryMaintenanceCountsAsViewOnlyUser() throws java.lang.Exception {
        testGetClusterGroupedSummaryMaintenanceCounts(org.apache.ambari.server.security.TestAuthenticationFactory.createViewUser(99L));
    }

    @java.lang.SuppressWarnings("unchecked")
    private void testGetClusterGroupedSummaryMaintenanceCounts(org.springframework.security.core.Authentication authentication) throws java.lang.Exception {
        java.util.List<org.apache.ambari.server.orm.entities.AlertCurrentEntity> currents = getMockEntitiesManyStates();
        for (org.apache.ambari.server.orm.entities.AlertCurrentEntity current : currents) {
            if (current.getAlertHistory().getAlertState() == org.apache.ambari.server.state.AlertState.WARNING) {
                current.setMaintenanceState(org.apache.ambari.server.state.MaintenanceState.ON);
            }
        }
        EasyMock.expect(m_dao.findAll(EasyMock.capture(org.easymock.EasyMock.<org.apache.ambari.server.controller.AlertCurrentRequest>newCapture()))).andReturn(currents).anyTimes();
        EasyMock.replay(m_dao);
        org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(authentication);
        org.apache.ambari.server.controller.spi.Request request = org.apache.ambari.server.controller.utilities.PropertyHelper.getReadRequest(org.apache.ambari.server.controller.internal.AlertResourceProvider.ALERT_ID, org.apache.ambari.server.controller.internal.AlertResourceProvider.ALERT_DEFINITION_NAME, org.apache.ambari.server.controller.internal.AlertResourceProvider.ALERT_LABEL, org.apache.ambari.server.controller.internal.AlertResourceProvider.ALERT_STATE, org.apache.ambari.server.controller.internal.AlertResourceProvider.ALERT_ORIGINAL_TIMESTAMP);
        org.apache.ambari.server.controller.spi.Predicate predicate = new org.apache.ambari.server.controller.utilities.PredicateBuilder().property(org.apache.ambari.server.controller.internal.AlertResourceProvider.ALERT_CLUSTER_NAME).equals("c1").toPredicate();
        org.apache.ambari.server.controller.internal.AlertResourceProvider provider = createProvider();
        java.util.Set<org.apache.ambari.server.controller.spi.Resource> results = provider.getResources(request, predicate);
        EasyMock.verify(m_dao);
        org.apache.ambari.server.api.query.render.AlertSummaryGroupedRenderer renderer = new org.apache.ambari.server.api.query.render.AlertSummaryGroupedRenderer();
        org.apache.ambari.server.api.services.ResultImpl result = new org.apache.ambari.server.api.services.ResultImpl(true);
        org.apache.ambari.server.api.util.TreeNode<org.apache.ambari.server.controller.spi.Resource> resources = result.getResultTree();
        java.util.concurrent.atomic.AtomicInteger alertResourceId = new java.util.concurrent.atomic.AtomicInteger(1);
        for (org.apache.ambari.server.controller.spi.Resource resource : results) {
            resources.addChild(resource, "Alert " + alertResourceId.getAndIncrement());
        }
        org.apache.ambari.server.api.services.Result groupedSummary = renderer.finalizeResult(result);
        org.junit.Assert.assertNotNull(groupedSummary);
        org.apache.ambari.server.api.util.TreeNode<org.apache.ambari.server.controller.spi.Resource> summaryResultTree = groupedSummary.getResultTree();
        org.apache.ambari.server.api.util.TreeNode<org.apache.ambari.server.controller.spi.Resource> summaryResources = summaryResultTree.getChild("alerts_summary_grouped");
        org.apache.ambari.server.controller.spi.Resource summaryResource = summaryResources.getObject();
        java.util.List<java.lang.Object> summaryList = ((java.util.List<java.lang.Object>) (summaryResource.getPropertyValue("alerts_summary_grouped")));
        org.junit.Assert.assertEquals(4, summaryList.size());
    }

    @org.junit.Test
    public void testResponseIsPaginated() throws java.lang.Exception {
        EasyMock.expect(m_dao.findAll(org.easymock.EasyMock.anyObject(org.apache.ambari.server.controller.AlertCurrentRequest.class))).andReturn(getClusterMockEntities()).atLeastOnce();
        EasyMock.expect(m_dao.getCount(org.easymock.EasyMock.anyObject(org.apache.ambari.server.controller.spi.Predicate.class))).andReturn(0).atLeastOnce();
        EasyMock.replay(m_dao);
        org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(org.apache.ambari.server.security.TestAuthenticationFactory.createAdministrator());
        java.util.Set<java.lang.String> requestProperties = new java.util.HashSet<>();
        requestProperties.add(org.apache.ambari.server.controller.internal.AlertResourceProvider.ALERT_ID);
        requestProperties.add(org.apache.ambari.server.controller.internal.AlertResourceProvider.ALERT_DEFINITION_NAME);
        org.apache.ambari.server.controller.spi.Request request = org.apache.ambari.server.controller.utilities.PropertyHelper.getReadRequest(requestProperties);
        org.apache.ambari.server.controller.spi.Predicate predicate = new org.apache.ambari.server.controller.utilities.PredicateBuilder().property(org.apache.ambari.server.controller.internal.AlertResourceProvider.ALERT_CLUSTER_NAME).equals("c1").toPredicate();
        org.apache.ambari.server.controller.internal.AlertResourceProvider provider = createProvider();
        org.apache.ambari.server.controller.spi.QueryResponse response = provider.queryForResources(request, predicate);
        org.junit.Assert.assertFalse(response.isPagedResponse());
        org.apache.ambari.server.controller.spi.PageRequest pageRequest = new org.apache.ambari.server.controller.internal.PageRequestImpl(org.apache.ambari.server.controller.spi.PageRequest.StartingPoint.Beginning, 5, 10, predicate, null);
        request = org.apache.ambari.server.controller.utilities.PropertyHelper.getReadRequest(requestProperties, null, null, pageRequest, null);
        response = provider.queryForResources(request, predicate);
        org.junit.Assert.assertTrue(response.isPagedResponse());
        EasyMock.verify(m_dao);
    }

    private org.apache.ambari.server.controller.internal.AlertResourceProvider createProvider() {
        return new org.apache.ambari.server.controller.internal.AlertResourceProvider(m_amc);
    }

    private java.util.List<org.apache.ambari.server.orm.entities.AlertCurrentEntity> getClusterMockEntities() throws java.lang.Exception {
        org.apache.ambari.server.orm.entities.AlertCurrentEntity current = new org.apache.ambari.server.orm.entities.AlertCurrentEntity();
        current.setAlertId(java.lang.Long.valueOf(1000L));
        current.setHistoryId(org.apache.ambari.server.controller.internal.AlertResourceProviderTest.ALERT_VALUE_ID);
        current.setLatestTimestamp(java.lang.Long.valueOf(1L));
        current.setOriginalTimestamp(java.lang.Long.valueOf(2L));
        org.apache.ambari.server.orm.entities.AlertHistoryEntity history = new org.apache.ambari.server.orm.entities.AlertHistoryEntity();
        history.setAlertId(org.apache.ambari.server.controller.internal.AlertResourceProviderTest.ALERT_VALUE_ID);
        history.setAlertInstance(null);
        history.setAlertLabel(org.apache.ambari.server.controller.internal.AlertResourceProviderTest.ALERT_VALUE_LABEL);
        history.setAlertState(org.apache.ambari.server.state.AlertState.OK);
        history.setAlertText(org.apache.ambari.server.controller.internal.AlertResourceProviderTest.ALERT_VALUE_TEXT);
        history.setAlertTimestamp(org.apache.ambari.server.controller.internal.AlertResourceProviderTest.ALERT_VALUE_TIMESTAMP);
        history.setClusterId(java.lang.Long.valueOf(1L));
        history.setComponentName(org.apache.ambari.server.controller.internal.AlertResourceProviderTest.ALERT_VALUE_COMPONENT);
        history.setHostName(org.apache.ambari.server.controller.internal.AlertResourceProviderTest.ALERT_VALUE_HOSTNAME);
        history.setServiceName(org.apache.ambari.server.controller.internal.AlertResourceProviderTest.ALERT_VALUE_SERVICE);
        org.apache.ambari.server.orm.entities.ResourceEntity clusterResourceEntity = new org.apache.ambari.server.orm.entities.ResourceEntity();
        clusterResourceEntity.setId(4L);
        org.apache.ambari.server.orm.entities.ClusterEntity clusterEntity = new org.apache.ambari.server.orm.entities.ClusterEntity();
        clusterEntity.setClusterId(2L);
        clusterEntity.setResource(clusterResourceEntity);
        org.apache.ambari.server.orm.entities.AlertDefinitionEntity definition = new org.apache.ambari.server.orm.entities.AlertDefinitionEntity();
        definition.setCluster(clusterEntity);
        history.setAlertDefinition(definition);
        current.setAlertHistory(history);
        return java.util.Arrays.asList(current);
    }

    private java.util.List<org.apache.ambari.server.orm.entities.AlertCurrentEntity> getMockEntitiesManyStates() throws java.lang.Exception {
        java.util.concurrent.atomic.AtomicLong timestamp = new java.util.concurrent.atomic.AtomicLong(java.lang.System.currentTimeMillis() - 86400000);
        java.util.concurrent.atomic.AtomicLong alertId = new java.util.concurrent.atomic.AtomicLong(1);
        int ok = 10;
        int warning = 2;
        int critical = 1;
        int unknown = 3;
        int total = ((ok + warning) + critical) + unknown;
        java.util.List<org.apache.ambari.server.orm.entities.AlertCurrentEntity> currents = new java.util.ArrayList<>(total);
        for (int i = 0; i < total; i++) {
            org.apache.ambari.server.state.AlertState state = org.apache.ambari.server.state.AlertState.OK;
            java.lang.String service = "HDFS";
            java.lang.String component = "NAMENODE";
            java.lang.String definitionName = "hdfs_namenode";
            if ((i >= ok) && (i < (ok + warning))) {
                state = org.apache.ambari.server.state.AlertState.WARNING;
                service = "YARN";
                component = "RESOURCEMANAGER";
                definitionName = "yarn_resourcemanager";
            } else if ((i >= (ok + warning)) & (i < ((ok + warning) + critical))) {
                state = org.apache.ambari.server.state.AlertState.CRITICAL;
                service = "HIVE";
                component = "HIVE_SERVER";
                definitionName = "hive_server";
            } else if (i >= ((ok + warning) + critical)) {
                state = org.apache.ambari.server.state.AlertState.UNKNOWN;
                service = "FLUME";
                component = "FLUME_HANDLER";
                definitionName = "flume_handler";
            }
            org.apache.ambari.server.orm.entities.AlertCurrentEntity current = new org.apache.ambari.server.orm.entities.AlertCurrentEntity();
            current.setAlertId(alertId.getAndIncrement());
            current.setOriginalTimestamp(timestamp.getAndAdd(10000));
            current.setLatestTimestamp(timestamp.getAndAdd(10000));
            current.setLatestText(org.apache.ambari.server.controller.internal.AlertResourceProviderTest.ALERT_VALUE_TEXT);
            org.apache.ambari.server.orm.entities.AlertHistoryEntity history = new org.apache.ambari.server.orm.entities.AlertHistoryEntity();
            history.setAlertId(alertId.getAndIncrement());
            history.setAlertInstance(null);
            history.setAlertLabel(org.apache.ambari.server.controller.internal.AlertResourceProviderTest.ALERT_VALUE_LABEL);
            history.setAlertState(state);
            history.setAlertText(org.apache.ambari.server.controller.internal.AlertResourceProviderTest.ALERT_VALUE_TEXT);
            history.setAlertTimestamp(current.getOriginalTimestamp());
            history.setClusterId(java.lang.Long.valueOf(1L));
            history.setComponentName(component);
            history.setHostName(org.apache.ambari.server.controller.internal.AlertResourceProviderTest.ALERT_VALUE_HOSTNAME);
            history.setServiceName(service);
            org.apache.ambari.server.orm.entities.ResourceEntity clusterResourceEntity = new org.apache.ambari.server.orm.entities.ResourceEntity();
            clusterResourceEntity.setId(4L);
            org.apache.ambari.server.orm.entities.ClusterEntity clusterEntity = new org.apache.ambari.server.orm.entities.ClusterEntity();
            clusterEntity.setClusterId(2L);
            clusterEntity.setResource(clusterResourceEntity);
            org.apache.ambari.server.orm.entities.AlertDefinitionEntity definition = new org.apache.ambari.server.orm.entities.AlertDefinitionEntity();
            definition.setDefinitionId(java.lang.Long.valueOf(i));
            definition.setDefinitionName(definitionName);
            definition.setCluster(clusterEntity);
            history.setAlertDefinition(definition);
            current.setAlertHistory(history);
            currents.add(current);
        }
        return currents;
    }

    private class MockModule implements com.google.inject.Module {
        @java.lang.Override
        public void configure(com.google.inject.Binder binder) {
            binder.bind(javax.persistence.EntityManager.class).toInstance(org.easymock.EasyMock.createMock(javax.persistence.EntityManager.class));
            binder.bind(org.apache.ambari.server.orm.dao.AlertsDAO.class).toInstance(m_dao);
            binder.bind(org.apache.ambari.server.controller.AmbariManagementController.class).toInstance(EasyMock.createMock(org.apache.ambari.server.controller.AmbariManagementController.class));
            binder.bind(org.apache.ambari.server.orm.DBAccessor.class).to(org.apache.ambari.server.orm.DBAccessorImpl.class);
            org.apache.ambari.server.state.Clusters clusters = org.easymock.EasyMock.createNiceMock(org.apache.ambari.server.state.Clusters.class);
            org.apache.ambari.server.configuration.Configuration configuration = org.easymock.EasyMock.createNiceMock(org.apache.ambari.server.configuration.Configuration.class);
            binder.bind(org.apache.ambari.server.state.Clusters.class).toInstance(clusters);
            binder.bind(org.apache.ambari.server.configuration.Configuration.class).toInstance(configuration);
            EasyMock.expect(configuration.getDatabaseUrl()).andReturn(org.apache.ambari.server.configuration.Configuration.JDBC_IN_MEMORY_URL).anyTimes();
            EasyMock.expect(configuration.getDatabaseDriver()).andReturn(org.apache.ambari.server.configuration.Configuration.JDBC_IN_MEMORY_DRIVER).anyTimes();
            EasyMock.expect(configuration.getDatabaseUser()).andReturn("sa").anyTimes();
            EasyMock.expect(configuration.getDatabasePassword()).andReturn("").anyTimes();
            EasyMock.expect(configuration.getAlertEventPublisherCorePoolSize()).andReturn(java.lang.Integer.valueOf(org.apache.ambari.server.configuration.Configuration.ALERTS_EXECUTION_SCHEDULER_THREADS_CORE_SIZE.getDefaultValue())).anyTimes();
            EasyMock.expect(configuration.getAlertEventPublisherMaxPoolSize()).andReturn(java.lang.Integer.valueOf(org.apache.ambari.server.configuration.Configuration.ALERTS_EXECUTION_SCHEDULER_THREADS_MAX_SIZE.getDefaultValue())).anyTimes();
            EasyMock.expect(configuration.getAlertEventPublisherWorkerQueueSize()).andReturn(java.lang.Integer.valueOf(org.apache.ambari.server.configuration.Configuration.ALERTS_EXECUTION_SCHEDULER_WORKER_QUEUE_SIZE.getDefaultValue())).anyTimes();
            EasyMock.expect(configuration.getMasterKeyLocation()).andReturn(new java.io.File("/test")).anyTimes();
            EasyMock.expect(configuration.getTemporaryKeyStoreRetentionMinutes()).andReturn(2L).anyTimes();
            EasyMock.expect(configuration.isActivelyPurgeTemporaryKeyStore()).andReturn(true).anyTimes();
            EasyMock.expect(configuration.getDatabaseSchema()).andReturn(org.apache.ambari.server.configuration.Configuration.DEFAULT_DERBY_SCHEMA).anyTimes();
            EasyMock.replay(configuration);
        }
    }
}