package org.apache.ambari.server.controller.internal;
import org.easymock.Capture;
import org.easymock.EasyMock;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import static org.easymock.EasyMock.anyObject;
import static org.easymock.EasyMock.capture;
import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.createNiceMock;
import static org.easymock.EasyMock.createStrictMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.resetToStrict;
import static org.easymock.EasyMock.verify;
public class AlertDefinitionResourceProviderTest {
    private org.apache.ambari.server.orm.dao.AlertDefinitionDAO dao = null;

    private org.apache.ambari.server.state.alert.AlertDefinitionHash definitionHash = null;

    private org.apache.ambari.server.state.alert.AlertDefinitionFactory m_factory = new org.apache.ambari.server.state.alert.AlertDefinitionFactory();

    private com.google.inject.Injector m_injector;

    private static java.lang.String DEFINITION_UUID = java.util.UUID.randomUUID().toString();

    @org.junit.Before
    public void before() {
        dao = EasyMock.createStrictMock(org.apache.ambari.server.orm.dao.AlertDefinitionDAO.class);
        definitionHash = EasyMock.createNiceMock(org.apache.ambari.server.state.alert.AlertDefinitionHash.class);
        m_injector = com.google.inject.Guice.createInjector(com.google.inject.util.Modules.override(new org.apache.ambari.server.orm.InMemoryDefaultTestModule()).with(new org.apache.ambari.server.controller.internal.AlertDefinitionResourceProviderTest.MockModule()));
        m_injector.injectMembers(m_factory);
        org.apache.ambari.server.security.authorization.AuthorizationHelperInitializer.viewInstanceDAOReturningNull();
    }

    @org.junit.After
    public void clearAuthentication() {
        org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(null);
    }

    @org.junit.Test
    public void testGetResourcesNoPredicate() throws java.lang.Exception {
        org.apache.ambari.server.controller.internal.AlertDefinitionResourceProvider provider = createProvider(null);
        org.apache.ambari.server.controller.spi.Request request = org.apache.ambari.server.controller.utilities.PropertyHelper.getReadRequest("AlertDefinition/cluster_name", "AlertDefinition/id");
        java.util.Set<org.apache.ambari.server.controller.spi.Resource> results = provider.getResources(request, null);
        org.junit.Assert.assertEquals(0, results.size());
    }

    @org.junit.Test
    public void testGetResourcesClusterPredicateAsAdministrator() throws java.lang.Exception {
        testGetResourcesClusterPredicate(org.apache.ambari.server.security.TestAuthenticationFactory.createAdministrator(), true);
    }

    @org.junit.Test
    public void testGetResourcesClusterPredicateAsClusterAdministrator() throws java.lang.Exception {
        testGetResourcesClusterPredicate(org.apache.ambari.server.security.TestAuthenticationFactory.createClusterAdministrator(), true);
    }

    @org.junit.Test
    public void testGetResourcesClusterPredicateAsServiceAdministrator() throws java.lang.Exception {
        testGetResourcesClusterPredicate(org.apache.ambari.server.security.TestAuthenticationFactory.createServiceAdministrator(), true);
    }

    @org.junit.Test
    public void testGetResourcesClusterPredicateAsClusterUser() throws java.lang.Exception {
        testGetResourcesClusterPredicate(org.apache.ambari.server.security.TestAuthenticationFactory.createClusterUser(), true);
    }

    @org.junit.Test
    public void testGetResourcesClusterPredicateAsViewUser() throws java.lang.Exception {
        testGetResourcesClusterPredicate(org.apache.ambari.server.security.TestAuthenticationFactory.createViewUser(99L), false);
    }

    private void testGetResourcesClusterPredicate(org.springframework.security.core.Authentication authentication, boolean expectResults) throws java.lang.Exception {
        org.apache.ambari.server.controller.spi.Request request = org.apache.ambari.server.controller.utilities.PropertyHelper.getReadRequest(org.apache.ambari.server.controller.internal.AlertDefinitionResourceProvider.ALERT_DEF_CLUSTER_NAME, org.apache.ambari.server.controller.internal.AlertDefinitionResourceProvider.ALERT_DEF_ID, org.apache.ambari.server.controller.internal.AlertDefinitionResourceProvider.ALERT_DEF_NAME, org.apache.ambari.server.controller.internal.AlertDefinitionResourceProvider.ALERT_DEF_LABEL);
        org.apache.ambari.server.controller.AmbariManagementController amc = EasyMock.createMock(org.apache.ambari.server.controller.AmbariManagementController.class);
        org.apache.ambari.server.state.Clusters clusters = EasyMock.createMock(org.apache.ambari.server.state.Clusters.class);
        org.apache.ambari.server.state.Cluster cluster = EasyMock.createMock(org.apache.ambari.server.state.Cluster.class);
        EasyMock.expect(amc.getClusters()).andReturn(clusters).atLeastOnce();
        EasyMock.expect(clusters.getCluster(((java.lang.String) (EasyMock.anyObject())))).andReturn(cluster).atLeastOnce();
        EasyMock.expect(cluster.getClusterId()).andReturn(1L).anyTimes();
        EasyMock.expect(cluster.getResourceId()).andReturn(4L).anyTimes();
        org.apache.ambari.server.controller.spi.Predicate predicate = new org.apache.ambari.server.controller.utilities.PredicateBuilder().property(org.apache.ambari.server.controller.internal.AlertDefinitionResourceProvider.ALERT_DEF_CLUSTER_NAME).equals("c1").toPredicate();
        EasyMock.expect(dao.findAll(1L)).andReturn(getMockEntities());
        EasyMock.replay(amc, clusters, cluster, dao);
        org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(authentication);
        org.apache.ambari.server.controller.internal.AlertDefinitionResourceProvider provider = createProvider(amc);
        java.util.Set<org.apache.ambari.server.controller.spi.Resource> results = provider.getResources(request, predicate);
        org.junit.Assert.assertEquals(expectResults ? 1 : 0, results.size());
        if (expectResults) {
            org.apache.ambari.server.controller.spi.Resource r = results.iterator().next();
            org.junit.Assert.assertEquals("my_def", r.getPropertyValue(org.apache.ambari.server.controller.internal.AlertDefinitionResourceProvider.ALERT_DEF_NAME));
            org.junit.Assert.assertEquals("Mock Label", r.getPropertyValue(org.apache.ambari.server.controller.internal.AlertDefinitionResourceProvider.ALERT_DEF_LABEL));
        }
        EasyMock.verify(amc, clusters, cluster, dao);
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

    private void testGetSingleResource(org.springframework.security.core.Authentication authentication) throws java.lang.Exception {
        org.apache.ambari.server.controller.spi.Request request = org.apache.ambari.server.controller.utilities.PropertyHelper.getReadRequest(org.apache.ambari.server.controller.internal.AlertDefinitionResourceProvider.ALERT_DEF_CLUSTER_NAME, org.apache.ambari.server.controller.internal.AlertDefinitionResourceProvider.ALERT_DEF_ID, org.apache.ambari.server.controller.internal.AlertDefinitionResourceProvider.ALERT_DEF_NAME, org.apache.ambari.server.controller.internal.AlertDefinitionResourceProvider.ALERT_DEF_LABEL, org.apache.ambari.server.controller.internal.AlertDefinitionResourceProvider.ALERT_DEF_DESCRIPTION, org.apache.ambari.server.controller.internal.AlertDefinitionResourceProvider.ALERT_DEF_IGNORE_HOST, org.apache.ambari.server.controller.internal.AlertDefinitionResourceProvider.ALERT_DEF_SOURCE, org.apache.ambari.server.controller.internal.AlertDefinitionResourceProvider.ALERT_DEF_SOURCE_TYPE, org.apache.ambari.server.controller.internal.AlertDefinitionResourceProvider.ALERT_DEF_HELP_URL);
        org.apache.ambari.server.controller.AmbariManagementController amc = EasyMock.createMock(org.apache.ambari.server.controller.AmbariManagementController.class);
        org.apache.ambari.server.state.Clusters clusters = EasyMock.createMock(org.apache.ambari.server.state.Clusters.class);
        org.apache.ambari.server.state.Cluster cluster = EasyMock.createMock(org.apache.ambari.server.state.Cluster.class);
        EasyMock.expect(amc.getClusters()).andReturn(clusters).atLeastOnce();
        EasyMock.expect(clusters.getCluster(((java.lang.String) (EasyMock.anyObject())))).andReturn(cluster).atLeastOnce();
        EasyMock.expect(cluster.getClusterId()).andReturn(java.lang.Long.valueOf(1)).anyTimes();
        org.apache.ambari.server.controller.spi.Predicate predicate = new org.apache.ambari.server.controller.utilities.PredicateBuilder().property(org.apache.ambari.server.controller.internal.AlertDefinitionResourceProvider.ALERT_DEF_CLUSTER_NAME).equals("c1").and().property(org.apache.ambari.server.controller.internal.AlertDefinitionResourceProvider.ALERT_DEF_ID).equals("1").toPredicate();
        EasyMock.expect(dao.findById(1L)).andReturn(getMockEntities().get(0));
        EasyMock.replay(amc, clusters, cluster, dao);
        org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(authentication);
        org.apache.ambari.server.controller.internal.AlertDefinitionResourceProvider provider = createProvider(amc);
        java.util.Set<org.apache.ambari.server.controller.spi.Resource> results = provider.getResources(request, predicate);
        org.junit.Assert.assertEquals(1, results.size());
        org.apache.ambari.server.controller.spi.Resource r = results.iterator().next();
        org.junit.Assert.assertEquals("my_def", r.getPropertyValue(org.apache.ambari.server.controller.internal.AlertDefinitionResourceProvider.ALERT_DEF_NAME));
        org.junit.Assert.assertEquals(org.apache.ambari.server.state.alert.SourceType.METRIC.name(), r.getPropertyValue(org.apache.ambari.server.controller.internal.AlertDefinitionResourceProvider.ALERT_DEF_SOURCE_TYPE));
        org.apache.ambari.server.state.alert.Source source = getMockSource();
        java.lang.String okJson = source.getReporting().getOk().getText();
        java.lang.Object reporting = r.getPropertyValue("AlertDefinition/source/reporting");
        org.junit.Assert.assertTrue(reporting.toString().contains(okJson));
        org.junit.Assert.assertEquals("Mock Label", r.getPropertyValue(org.apache.ambari.server.controller.internal.AlertDefinitionResourceProvider.ALERT_DEF_LABEL));
        org.junit.Assert.assertEquals("Mock Description", r.getPropertyValue(org.apache.ambari.server.controller.internal.AlertDefinitionResourceProvider.ALERT_DEF_DESCRIPTION));
        org.junit.Assert.assertEquals(java.lang.Boolean.FALSE, r.getPropertyValue(org.apache.ambari.server.controller.internal.AlertDefinitionResourceProvider.ALERT_DEF_IGNORE_HOST));
        org.junit.Assert.assertEquals("http://test-help-url", r.getPropertyValue(org.apache.ambari.server.controller.internal.AlertDefinitionResourceProvider.ALERT_DEF_HELP_URL));
        org.junit.Assert.assertNotNull(r.getPropertyValue("AlertDefinition/source/type"));
    }

    @org.junit.Test
    public void testGetResourcesAssertSourceTypeAsAdministrator() throws java.lang.Exception {
        testGetResourcesAssertSourceType(org.apache.ambari.server.security.TestAuthenticationFactory.createAdministrator(), true);
    }

    @org.junit.Test
    public void testGetResourcesAssertSourceTypeAsClusterAdministrator() throws java.lang.Exception {
        testGetResourcesAssertSourceType(org.apache.ambari.server.security.TestAuthenticationFactory.createClusterAdministrator(), true);
    }

    @org.junit.Test
    public void testGetResourcesAssertSourceTypeAsServiceAdministrator() throws java.lang.Exception {
        testGetResourcesAssertSourceType(org.apache.ambari.server.security.TestAuthenticationFactory.createServiceAdministrator(), true);
    }

    @org.junit.Test
    public void testGetResourcesAssertSourceTypeAsClusterUser() throws java.lang.Exception {
        testGetResourcesAssertSourceType(org.apache.ambari.server.security.TestAuthenticationFactory.createClusterUser(), true);
    }

    @org.junit.Test
    public void testGetResourcesAssertSourceTypeAsViewUser() throws java.lang.Exception {
        testGetResourcesAssertSourceType(org.apache.ambari.server.security.TestAuthenticationFactory.createViewUser(99L), false);
    }

    private void testGetResourcesAssertSourceType(org.springframework.security.core.Authentication authentication, boolean expectResults) throws java.lang.Exception {
        org.apache.ambari.server.controller.spi.Request request = org.apache.ambari.server.controller.utilities.PropertyHelper.getReadRequest(org.apache.ambari.server.controller.internal.AlertDefinitionResourceProvider.ALERT_DEF_CLUSTER_NAME, org.apache.ambari.server.controller.internal.AlertDefinitionResourceProvider.ALERT_DEF_ID, org.apache.ambari.server.controller.internal.AlertDefinitionResourceProvider.ALERT_DEF_NAME, org.apache.ambari.server.controller.internal.AlertDefinitionResourceProvider.ALERT_DEF_LABEL, org.apache.ambari.server.controller.internal.AlertDefinitionResourceProvider.ALERT_DEF_DESCRIPTION, org.apache.ambari.server.controller.internal.AlertDefinitionResourceProvider.ALERT_DEF_SOURCE_TYPE);
        org.apache.ambari.server.controller.AmbariManagementController amc = EasyMock.createMock(org.apache.ambari.server.controller.AmbariManagementController.class);
        org.apache.ambari.server.state.Clusters clusters = EasyMock.createMock(org.apache.ambari.server.state.Clusters.class);
        org.apache.ambari.server.state.Cluster cluster = EasyMock.createMock(org.apache.ambari.server.state.Cluster.class);
        EasyMock.expect(amc.getClusters()).andReturn(clusters).atLeastOnce();
        EasyMock.expect(clusters.getCluster(((java.lang.String) (EasyMock.anyObject())))).andReturn(cluster).atLeastOnce();
        EasyMock.expect(cluster.getClusterId()).andReturn(1L).anyTimes();
        EasyMock.expect(cluster.getResourceId()).andReturn(4L).anyTimes();
        org.apache.ambari.server.controller.spi.Predicate predicate = new org.apache.ambari.server.controller.utilities.PredicateBuilder().property(org.apache.ambari.server.controller.internal.AlertDefinitionResourceProvider.ALERT_DEF_CLUSTER_NAME).equals("c1").toPredicate();
        EasyMock.expect(dao.findAll(1L)).andReturn(getMockEntities()).atLeastOnce();
        EasyMock.replay(amc, clusters, cluster, dao);
        org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(authentication);
        org.apache.ambari.server.controller.internal.AlertDefinitionResourceProvider provider = createProvider(amc);
        java.util.Set<org.apache.ambari.server.controller.spi.Resource> results = provider.getResources(request, predicate);
        org.junit.Assert.assertEquals(expectResults ? 1 : 0, results.size());
        if (expectResults) {
            org.apache.ambari.server.controller.spi.Resource resource = results.iterator().next();
            org.junit.Assert.assertEquals("my_def", resource.getPropertyValue(org.apache.ambari.server.controller.internal.AlertDefinitionResourceProvider.ALERT_DEF_NAME));
            java.util.Map<?, ?> reporting = ((java.util.Map<?, ?>) (resource.getPropertyValue("AlertDefinition/source/reporting")));
            org.junit.Assert.assertTrue(reporting.containsKey("ok"));
            org.junit.Assert.assertTrue(reporting.containsKey("critical"));
        }
        EasyMock.verify(amc, clusters, cluster, dao);
        request = org.apache.ambari.server.controller.utilities.PropertyHelper.getReadRequest(org.apache.ambari.server.controller.internal.AlertDefinitionResourceProvider.ALERT_DEF_CLUSTER_NAME, org.apache.ambari.server.controller.internal.AlertDefinitionResourceProvider.ALERT_DEF_ID, org.apache.ambari.server.controller.internal.AlertDefinitionResourceProvider.ALERT_DEF_NAME);
        results = provider.getResources(request, predicate);
        if (!results.isEmpty()) {
            org.apache.ambari.server.controller.spi.Resource resource = results.iterator().next();
            org.junit.Assert.assertEquals("my_def", resource.getPropertyValue(org.apache.ambari.server.controller.internal.AlertDefinitionResourceProvider.ALERT_DEF_NAME));
            java.util.Map<?, ?> reporting = ((java.util.Map<?, ?>) (resource.getPropertyValue("AlertDefinition/source/reporting")));
            org.junit.Assert.assertNull(reporting);
        }
    }

    @org.junit.Test
    public void testCreateResourcesAsAdministrator() throws java.lang.Exception {
        testCreateResources(org.apache.ambari.server.security.TestAuthenticationFactory.createAdministrator());
    }

    @org.junit.Test(expected = org.apache.ambari.server.security.authorization.AuthorizationException.class)
    public void testCreateResourcesAsClusterAdministrator() throws java.lang.Exception {
        testCreateResources(org.apache.ambari.server.security.TestAuthenticationFactory.createClusterAdministrator());
    }

    @org.junit.Test(expected = org.apache.ambari.server.security.authorization.AuthorizationException.class)
    public void testCreateResourcesAsServiceAdministrator() throws java.lang.Exception {
        testCreateResources(org.apache.ambari.server.security.TestAuthenticationFactory.createServiceAdministrator());
    }

    @org.junit.Test(expected = org.apache.ambari.server.security.authorization.AuthorizationException.class)
    public void testCreateResourcesAsClusterUser() throws java.lang.Exception {
        testCreateResources(org.apache.ambari.server.security.TestAuthenticationFactory.createClusterUser());
    }

    @org.junit.Test(expected = org.apache.ambari.server.security.authorization.AuthorizationException.class)
    public void testCreateResourcesAsViewUser() throws java.lang.Exception {
        testCreateResources(org.apache.ambari.server.security.TestAuthenticationFactory.createViewUser(99L));
    }

    public void testCreateResources(org.springframework.security.core.Authentication authentication) throws java.lang.Exception {
        org.apache.ambari.server.controller.AmbariManagementController amc = EasyMock.createMock(org.apache.ambari.server.controller.AmbariManagementController.class);
        org.apache.ambari.server.state.Clusters clusters = EasyMock.createMock(org.apache.ambari.server.state.Clusters.class);
        org.apache.ambari.server.state.Cluster cluster = EasyMock.createMock(org.apache.ambari.server.state.Cluster.class);
        EasyMock.expect(amc.getClusters()).andReturn(clusters).atLeastOnce();
        EasyMock.expect(clusters.getCluster(((java.lang.String) (EasyMock.anyObject())))).andReturn(cluster).atLeastOnce();
        EasyMock.expect(cluster.getClusterId()).andReturn(java.lang.Long.valueOf(1)).anyTimes();
        org.easymock.Capture<org.apache.ambari.server.orm.entities.AlertDefinitionEntity> entityCapture = org.easymock.EasyMock.newCapture();
        dao.create(EasyMock.capture(entityCapture));
        EasyMock.expectLastCall();
        EasyMock.expect(definitionHash.invalidateHosts(org.easymock.EasyMock.anyObject(org.apache.ambari.server.orm.entities.AlertDefinitionEntity.class))).andReturn(new java.util.HashSet<>()).once();
        EasyMock.replay(amc, clusters, cluster, dao, definitionHash);
        org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(authentication);
        com.google.gson.Gson gson = m_factory.getGson();
        org.apache.ambari.server.state.alert.MetricSource source = ((org.apache.ambari.server.state.alert.MetricSource) (getMockSource()));
        org.apache.ambari.server.controller.internal.AlertDefinitionResourceProvider provider = createProvider(amc);
        java.util.Map<java.lang.String, java.lang.Object> requestProps = new java.util.HashMap<>();
        requestProps.put(org.apache.ambari.server.controller.internal.AlertDefinitionResourceProvider.ALERT_DEF_CLUSTER_NAME, "c1");
        requestProps.put(org.apache.ambari.server.controller.internal.AlertDefinitionResourceProvider.ALERT_DEF_INTERVAL, "1");
        requestProps.put(org.apache.ambari.server.controller.internal.AlertDefinitionResourceProvider.ALERT_DEF_NAME, "my_def");
        requestProps.put(org.apache.ambari.server.controller.internal.AlertDefinitionResourceProvider.ALERT_DEF_SERVICE_NAME, "HDFS");
        requestProps.put(org.apache.ambari.server.controller.internal.AlertDefinitionResourceProvider.ALERT_DEF_LABEL, "Mock Label (Create)");
        requestProps.put(org.apache.ambari.server.controller.internal.AlertDefinitionResourceProvider.ALERT_DEF_DESCRIPTION, "Mock Description (Create)");
        requestProps.put(org.apache.ambari.server.controller.internal.AlertDefinitionResourceProvider.ALERT_DEF_SOURCE_TYPE, org.apache.ambari.server.state.alert.SourceType.METRIC.name());
        requestProps.put("AlertDefinition/source/jmx/value", source.getJmxInfo().getValue().toString());
        requestProps.put("AlertDefinition/source/jmx/property_list", source.getJmxInfo().getPropertyList());
        requestProps.put("AlertDefinition/source/uri/http", source.getUri().getHttpUri());
        requestProps.put("AlertDefinition/source/uri/https", source.getUri().getHttpsUri());
        requestProps.put("AlertDefinition/source/uri/https_property", source.getUri().getHttpsProperty());
        requestProps.put("AlertDefinition/source/uri/https_property_value", source.getUri().getHttpsPropertyValue());
        requestProps.put("AlertDefinition/source/reporting/critical/text", source.getReporting().getCritical().getText());
        requestProps.put("AlertDefinition/source/reporting/critical/value", source.getReporting().getCritical().getValue());
        requestProps.put("AlertDefinition/source/reporting/ok/text", source.getReporting().getOk().getText());
        requestProps.put("AlertDefinition/source/reporting/warning/text", source.getReporting().getWarning().getText());
        requestProps.put("AlertDefinition/source/reporting/warning/value", source.getReporting().getWarning().getValue());
        requestProps.put("AlertDefinition/source/reporting/units", "Gigabytes");
        org.apache.ambari.server.controller.spi.Request request = org.apache.ambari.server.controller.utilities.PropertyHelper.getCreateRequest(java.util.Collections.singleton(requestProps), null);
        provider.createResources(request);
        org.junit.Assert.assertTrue(entityCapture.hasCaptured());
        org.apache.ambari.server.orm.entities.AlertDefinitionEntity entity = entityCapture.getValue();
        org.junit.Assert.assertNotNull(entity);
        org.junit.Assert.assertEquals(java.lang.Long.valueOf(1), entity.getClusterId());
        org.junit.Assert.assertNull(entity.getComponentName());
        org.junit.Assert.assertEquals("my_def", entity.getDefinitionName());
        org.junit.Assert.assertTrue(entity.getEnabled());
        org.junit.Assert.assertNotNull(entity.getHash());
        org.junit.Assert.assertEquals(java.lang.Integer.valueOf(1), entity.getScheduleInterval());
        org.junit.Assert.assertEquals(org.apache.ambari.server.state.alert.Scope.ANY, entity.getScope());
        org.junit.Assert.assertEquals("HDFS", entity.getServiceName());
        org.junit.Assert.assertEquals(org.apache.ambari.server.state.alert.SourceType.METRIC, entity.getSourceType());
        org.junit.Assert.assertEquals("Mock Label (Create)", entity.getLabel());
        org.junit.Assert.assertEquals("Mock Description (Create)", entity.getDescription());
        org.junit.Assert.assertEquals(false, entity.isHostIgnored());
        org.junit.Assert.assertNotNull(entity.getSource());
        org.apache.ambari.server.state.alert.MetricSource actualSource = gson.fromJson(entity.getSource(), org.apache.ambari.server.state.alert.MetricSource.class);
        org.junit.Assert.assertNotNull(actualSource);
        org.junit.Assert.assertEquals(source.getReporting().getOk().getText(), actualSource.getReporting().getOk().getText());
        org.junit.Assert.assertEquals(source.getReporting().getWarning().getText(), actualSource.getReporting().getWarning().getText());
        org.junit.Assert.assertEquals(source.getReporting().getCritical().getText(), actualSource.getReporting().getCritical().getText());
        org.junit.Assert.assertEquals("Gigabytes", actualSource.getReporting().getUnits());
        org.junit.Assert.assertNotNull(source.getUri().getHttpUri());
        org.junit.Assert.assertNotNull(source.getUri().getHttpsUri());
        org.junit.Assert.assertEquals(source.getUri().getHttpUri(), actualSource.getUri().getHttpUri());
        org.junit.Assert.assertEquals(source.getUri().getHttpsUri(), actualSource.getUri().getHttpsUri());
        EasyMock.verify(amc, clusters, cluster, dao);
    }

    @org.junit.Test
    public void testUpdateResourcesAsAdministrator() throws java.lang.Exception {
        testUpdateResources(org.apache.ambari.server.security.TestAuthenticationFactory.createAdministrator());
    }

    @org.junit.Test(expected = org.apache.ambari.server.security.authorization.AuthorizationException.class)
    public void testUpdateResourcesAsClusterAdministrator() throws java.lang.Exception {
        testUpdateResources(org.apache.ambari.server.security.TestAuthenticationFactory.createClusterAdministrator());
    }

    @org.junit.Test(expected = org.apache.ambari.server.security.authorization.AuthorizationException.class)
    public void testUpdateResourcesAsServiceAdministrator() throws java.lang.Exception {
        testUpdateResources(org.apache.ambari.server.security.TestAuthenticationFactory.createServiceAdministrator());
    }

    @org.junit.Test(expected = org.apache.ambari.server.security.authorization.AuthorizationException.class)
    public void testUpdateResourcesAsClusterUser() throws java.lang.Exception {
        testUpdateResources(org.apache.ambari.server.security.TestAuthenticationFactory.createClusterUser());
    }

    @org.junit.Test(expected = org.apache.ambari.server.security.authorization.AuthorizationException.class)
    public void testUpdateResourcesAsViewUser() throws java.lang.Exception {
        testUpdateResources(org.apache.ambari.server.security.TestAuthenticationFactory.createViewUser(99L));
    }

    private void testUpdateResources(org.springframework.security.core.Authentication authentication) throws java.lang.Exception {
        org.apache.ambari.server.controller.AmbariManagementController amc = EasyMock.createMock(org.apache.ambari.server.controller.AmbariManagementController.class);
        org.apache.ambari.server.state.Clusters clusters = EasyMock.createMock(org.apache.ambari.server.state.Clusters.class);
        org.apache.ambari.server.state.Cluster cluster = EasyMock.createMock(org.apache.ambari.server.state.Cluster.class);
        EasyMock.expect(amc.getClusters()).andReturn(clusters).atLeastOnce();
        EasyMock.expect(clusters.getCluster(((java.lang.String) (EasyMock.anyObject())))).andReturn(cluster).atLeastOnce();
        EasyMock.expect(cluster.getClusterId()).andReturn(java.lang.Long.valueOf(1)).atLeastOnce();
        org.easymock.Capture<org.apache.ambari.server.orm.entities.AlertDefinitionEntity> entityCapture = org.easymock.EasyMock.newCapture();
        dao.create(EasyMock.capture(entityCapture));
        EasyMock.expectLastCall();
        EasyMock.expect(definitionHash.invalidateHosts(org.easymock.EasyMock.anyObject(org.apache.ambari.server.orm.entities.AlertDefinitionEntity.class))).andReturn(new java.util.HashSet<>()).atLeastOnce();
        EasyMock.replay(amc, clusters, cluster, dao, definitionHash);
        org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(authentication);
        org.apache.ambari.server.state.alert.MetricSource source = ((org.apache.ambari.server.state.alert.MetricSource) (getMockSource()));
        java.util.Map<java.lang.String, java.lang.Object> requestProps = new java.util.HashMap<>();
        requestProps.put(org.apache.ambari.server.controller.internal.AlertDefinitionResourceProvider.ALERT_DEF_CLUSTER_NAME, "c1");
        requestProps.put(org.apache.ambari.server.controller.internal.AlertDefinitionResourceProvider.ALERT_DEF_INTERVAL, "1");
        requestProps.put(org.apache.ambari.server.controller.internal.AlertDefinitionResourceProvider.ALERT_DEF_NAME, "my_def");
        requestProps.put(org.apache.ambari.server.controller.internal.AlertDefinitionResourceProvider.ALERT_DEF_LABEL, "Label");
        requestProps.put(org.apache.ambari.server.controller.internal.AlertDefinitionResourceProvider.ALERT_DEF_DESCRIPTION, "Description");
        requestProps.put(org.apache.ambari.server.controller.internal.AlertDefinitionResourceProvider.ALERT_DEF_SERVICE_NAME, "HDFS");
        requestProps.put(org.apache.ambari.server.controller.internal.AlertDefinitionResourceProvider.ALERT_DEF_SOURCE_TYPE, "METRIC");
        requestProps.put(org.apache.ambari.server.controller.internal.AlertDefinitionResourceProvider.ALERT_DEF_ENABLED, java.lang.Boolean.TRUE.toString());
        requestProps.put("AlertDefinition/source/jmx/value", source.getJmxInfo().getValue().toString());
        requestProps.put("AlertDefinition/source/jmx/property_list", source.getJmxInfo().getPropertyList());
        requestProps.put("AlertDefinition/source/uri/http", source.getUri().getHttpUri());
        requestProps.put("AlertDefinition/source/uri/https", source.getUri().getHttpsUri());
        requestProps.put("AlertDefinition/source/uri/https_property", source.getUri().getHttpsProperty());
        requestProps.put("AlertDefinition/source/uri/https_property_value", source.getUri().getHttpsPropertyValue());
        requestProps.put("AlertDefinition/source/reporting/critical/text", source.getReporting().getCritical().getText());
        requestProps.put("AlertDefinition/source/reporting/critical/value", source.getReporting().getCritical().getValue());
        requestProps.put("AlertDefinition/source/reporting/ok/text", source.getReporting().getOk().getText());
        requestProps.put("AlertDefinition/source/reporting/warning/text", source.getReporting().getWarning().getText());
        requestProps.put("AlertDefinition/source/reporting/warning/value", source.getReporting().getWarning().getValue());
        org.apache.ambari.server.controller.spi.Request request = org.apache.ambari.server.controller.utilities.PropertyHelper.getCreateRequest(java.util.Collections.singleton(requestProps), null);
        org.apache.ambari.server.controller.internal.AlertDefinitionResourceProvider provider = createProvider(amc);
        provider.createResources(request);
        org.junit.Assert.assertTrue(entityCapture.hasCaptured());
        org.apache.ambari.server.orm.entities.AlertDefinitionEntity entity = entityCapture.getValue();
        org.junit.Assert.assertNotNull(entity);
        org.apache.ambari.server.controller.spi.Predicate p = new org.apache.ambari.server.controller.utilities.PredicateBuilder().property(org.apache.ambari.server.controller.internal.AlertDefinitionResourceProvider.ALERT_DEF_ID).equals("1").and().property(org.apache.ambari.server.controller.internal.AlertDefinitionResourceProvider.ALERT_DEF_CLUSTER_NAME).equals("c1").toPredicate();
        entity.setDefinitionId(java.lang.Long.valueOf(1));
        java.lang.String oldName = entity.getDefinitionName();
        java.lang.String oldHash = entity.getHash();
        java.lang.Integer oldInterval = entity.getScheduleInterval();
        boolean oldEnabled = entity.getEnabled();
        boolean oldHostIgnore = entity.isHostIgnored();
        java.lang.String oldSource = entity.getSource();
        java.lang.String oldDescription = entity.getDescription();
        EasyMock.resetToStrict(dao);
        EasyMock.expect(dao.findById(1L)).andReturn(entity).anyTimes();
        EasyMock.expect(dao.merge(((org.apache.ambari.server.orm.entities.AlertDefinitionEntity) (EasyMock.anyObject())))).andReturn(entity).anyTimes();
        EasyMock.replay(dao);
        requestProps = new java.util.HashMap<>();
        requestProps.put(org.apache.ambari.server.controller.internal.AlertDefinitionResourceProvider.ALERT_DEF_ID, "1");
        requestProps.put(org.apache.ambari.server.controller.internal.AlertDefinitionResourceProvider.ALERT_DEF_CLUSTER_NAME, "c1");
        requestProps.put(org.apache.ambari.server.controller.internal.AlertDefinitionResourceProvider.ALERT_DEF_INTERVAL, "2");
        requestProps.put(org.apache.ambari.server.controller.internal.AlertDefinitionResourceProvider.ALERT_DEF_NAME, "my_def2");
        requestProps.put(org.apache.ambari.server.controller.internal.AlertDefinitionResourceProvider.ALERT_DEF_LABEL, "Label 2");
        requestProps.put(org.apache.ambari.server.controller.internal.AlertDefinitionResourceProvider.ALERT_DEF_DESCRIPTION, "Description 2");
        requestProps.put(org.apache.ambari.server.controller.internal.AlertDefinitionResourceProvider.ALERT_DEF_SERVICE_NAME, "HDFS");
        requestProps.put(org.apache.ambari.server.controller.internal.AlertDefinitionResourceProvider.ALERT_DEF_SOURCE_TYPE, "METRIC");
        requestProps.put("AlertDefinition/source/uri/http", source.getUri().getHttpUri() + "_foobarbaz");
        requestProps.put("AlertDefinition/source/uri/https", source.getUri().getHttpsUri() + "_foobarbaz");
        requestProps.put("AlertDefinition/source/uri/https_property", source.getUri().getHttpsProperty() + "_foobarbaz");
        requestProps.put("AlertDefinition/source/uri/https_property_value", source.getUri().getHttpsPropertyValue() + "_foobarbaz");
        requestProps.put(org.apache.ambari.server.controller.internal.AlertDefinitionResourceProvider.ALERT_DEF_ENABLED, java.lang.Boolean.FALSE.toString());
        requestProps.put(org.apache.ambari.server.controller.internal.AlertDefinitionResourceProvider.ALERT_DEF_IGNORE_HOST, java.lang.Boolean.TRUE.toString());
        request = org.apache.ambari.server.controller.utilities.PropertyHelper.getUpdateRequest(requestProps, null);
        provider.updateResources(request, p);
        org.junit.Assert.assertFalse(oldHash.equals(entity.getHash()));
        org.junit.Assert.assertFalse(oldName.equals(entity.getDefinitionName()));
        org.junit.Assert.assertFalse(oldDescription.equals(entity.getDescription()));
        org.junit.Assert.assertFalse(oldInterval.equals(entity.getScheduleInterval()));
        org.junit.Assert.assertFalse(oldEnabled == entity.getEnabled());
        org.junit.Assert.assertFalse(oldHostIgnore == entity.isHostIgnored());
        org.junit.Assert.assertFalse(oldSource.equals(entity.getSource()));
        org.junit.Assert.assertTrue(entity.getSource().contains("_foobarbaz"));
        EasyMock.verify(amc, clusters, cluster, dao);
    }

    @org.junit.Test
    public void testUpdateResourcesWithNumbersAsStrings() throws java.lang.Exception {
        org.apache.ambari.server.controller.AmbariManagementController amc = EasyMock.createMock(org.apache.ambari.server.controller.AmbariManagementController.class);
        org.apache.ambari.server.state.Clusters clusters = EasyMock.createMock(org.apache.ambari.server.state.Clusters.class);
        org.apache.ambari.server.state.Cluster cluster = EasyMock.createMock(org.apache.ambari.server.state.Cluster.class);
        EasyMock.expect(amc.getClusters()).andReturn(clusters).atLeastOnce();
        EasyMock.expect(clusters.getCluster(((java.lang.String) (EasyMock.anyObject())))).andReturn(cluster).atLeastOnce();
        EasyMock.expect(cluster.getClusterId()).andReturn(java.lang.Long.valueOf(1)).atLeastOnce();
        org.easymock.Capture<org.apache.ambari.server.orm.entities.AlertDefinitionEntity> entityCapture = org.easymock.EasyMock.newCapture();
        dao.create(EasyMock.capture(entityCapture));
        EasyMock.expectLastCall();
        EasyMock.expect(definitionHash.invalidateHosts(org.easymock.EasyMock.anyObject(org.apache.ambari.server.orm.entities.AlertDefinitionEntity.class))).andReturn(new java.util.HashSet<>()).atLeastOnce();
        EasyMock.replay(amc, clusters, cluster, dao, definitionHash);
        org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(org.apache.ambari.server.security.TestAuthenticationFactory.createAdministrator());
        org.apache.ambari.server.state.alert.MetricSource source = ((org.apache.ambari.server.state.alert.MetricSource) (getMockSource()));
        java.util.Map<java.lang.String, java.lang.Object> requestProps = new java.util.HashMap<>();
        requestProps.put(org.apache.ambari.server.controller.internal.AlertDefinitionResourceProvider.ALERT_DEF_CLUSTER_NAME, "c1");
        requestProps.put(org.apache.ambari.server.controller.internal.AlertDefinitionResourceProvider.ALERT_DEF_INTERVAL, "1");
        requestProps.put(org.apache.ambari.server.controller.internal.AlertDefinitionResourceProvider.ALERT_DEF_NAME, "my_def");
        requestProps.put(org.apache.ambari.server.controller.internal.AlertDefinitionResourceProvider.ALERT_DEF_SERVICE_NAME, "HDFS");
        requestProps.put(org.apache.ambari.server.controller.internal.AlertDefinitionResourceProvider.ALERT_DEF_SOURCE_TYPE, "METRIC");
        requestProps.put("AlertDefinition/source/reporting/critical/text", source.getReporting().getCritical().getText());
        requestProps.put("AlertDefinition/source/reporting/critical/value", "1234.5");
        org.apache.ambari.server.controller.spi.Request request = org.apache.ambari.server.controller.utilities.PropertyHelper.getCreateRequest(java.util.Collections.singleton(requestProps), null);
        org.apache.ambari.server.controller.internal.AlertDefinitionResourceProvider provider = createProvider(amc);
        provider.createResources(request);
        org.junit.Assert.assertTrue(entityCapture.hasCaptured());
        org.apache.ambari.server.orm.entities.AlertDefinitionEntity entity = entityCapture.getValue();
        org.junit.Assert.assertNotNull(entity);
        java.lang.String sourceJson = entity.getSource();
        com.google.gson.Gson gson = new com.google.gson.Gson();
        source = gson.fromJson(sourceJson, org.apache.ambari.server.state.alert.MetricSource.class);
        org.junit.Assert.assertEquals(new java.lang.Double(1234.5), source.getReporting().getCritical().getValue());
        EasyMock.verify(amc, clusters, cluster, dao);
    }

    @org.junit.Test
    public void testDeleteResourcesAsAdministrator() throws java.lang.Exception {
        testDeleteResources(org.apache.ambari.server.security.TestAuthenticationFactory.createAdministrator());
    }

    @org.junit.Test(expected = org.apache.ambari.server.security.authorization.AuthorizationException.class)
    public void testDeleteResourcesAsClusterAdministrator() throws java.lang.Exception {
        testDeleteResources(org.apache.ambari.server.security.TestAuthenticationFactory.createClusterAdministrator());
    }

    @org.junit.Test(expected = org.apache.ambari.server.security.authorization.AuthorizationException.class)
    public void testDeleteResourcesAsServiceAdministrator() throws java.lang.Exception {
        testDeleteResources(org.apache.ambari.server.security.TestAuthenticationFactory.createServiceAdministrator());
    }

    @org.junit.Test(expected = org.apache.ambari.server.security.authorization.AuthorizationException.class)
    public void testDeleteResourcesAsClusterUser() throws java.lang.Exception {
        testDeleteResources(org.apache.ambari.server.security.TestAuthenticationFactory.createClusterUser());
    }

    @org.junit.Test(expected = org.apache.ambari.server.security.authorization.AuthorizationException.class)
    public void testDeleteResourcesAsViewUser() throws java.lang.Exception {
        testDeleteResources(org.apache.ambari.server.security.TestAuthenticationFactory.createViewUser(99L));
    }

    public void testDeleteResources(org.springframework.security.core.Authentication authentication) throws java.lang.Exception {
        org.apache.ambari.server.controller.AmbariManagementController amc = EasyMock.createMock(org.apache.ambari.server.controller.AmbariManagementController.class);
        org.apache.ambari.server.state.Clusters clusters = EasyMock.createMock(org.apache.ambari.server.state.Clusters.class);
        org.apache.ambari.server.state.Cluster cluster = EasyMock.createMock(org.apache.ambari.server.state.Cluster.class);
        EasyMock.expect(amc.getClusters()).andReturn(clusters).atLeastOnce();
        EasyMock.expect(clusters.getCluster(((java.lang.String) (EasyMock.anyObject())))).andReturn(cluster).atLeastOnce();
        EasyMock.expect(cluster.getClusterId()).andReturn(java.lang.Long.valueOf(1)).anyTimes();
        org.easymock.Capture<org.apache.ambari.server.orm.entities.AlertDefinitionEntity> entityCapture = org.easymock.EasyMock.newCapture();
        dao.create(EasyMock.capture(entityCapture));
        EasyMock.expectLastCall();
        EasyMock.expect(definitionHash.invalidateHosts(org.easymock.EasyMock.anyObject(org.apache.ambari.server.orm.entities.AlertDefinitionEntity.class))).andReturn(new java.util.HashSet<>()).atLeastOnce();
        EasyMock.replay(amc, clusters, cluster, dao, definitionHash);
        org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(authentication);
        org.apache.ambari.server.controller.internal.AlertDefinitionResourceProvider provider = createProvider(amc);
        java.util.Map<java.lang.String, java.lang.Object> requestProps = new java.util.HashMap<>();
        requestProps.put(org.apache.ambari.server.controller.internal.AlertDefinitionResourceProvider.ALERT_DEF_CLUSTER_NAME, "c1");
        requestProps.put(org.apache.ambari.server.controller.internal.AlertDefinitionResourceProvider.ALERT_DEF_INTERVAL, "1");
        requestProps.put(org.apache.ambari.server.controller.internal.AlertDefinitionResourceProvider.ALERT_DEF_NAME, "my_def");
        requestProps.put(org.apache.ambari.server.controller.internal.AlertDefinitionResourceProvider.ALERT_DEF_SERVICE_NAME, "HDFS");
        requestProps.put(org.apache.ambari.server.controller.internal.AlertDefinitionResourceProvider.ALERT_DEF_SOURCE_TYPE, "METRIC");
        org.apache.ambari.server.controller.spi.Request request = org.apache.ambari.server.controller.utilities.PropertyHelper.getCreateRequest(java.util.Collections.singleton(requestProps), null);
        provider.createResources(request);
        org.junit.Assert.assertTrue(entityCapture.hasCaptured());
        org.apache.ambari.server.orm.entities.AlertDefinitionEntity entity = entityCapture.getValue();
        org.junit.Assert.assertNotNull(entity);
        org.apache.ambari.server.controller.spi.Predicate p = new org.apache.ambari.server.controller.utilities.PredicateBuilder().property(org.apache.ambari.server.controller.internal.AlertDefinitionResourceProvider.ALERT_DEF_ID).equals("1").and().property(org.apache.ambari.server.controller.internal.AlertDefinitionResourceProvider.ALERT_DEF_CLUSTER_NAME).equals("c1").toPredicate();
        entity.setDefinitionId(java.lang.Long.valueOf(1));
        EasyMock.resetToStrict(dao);
        EasyMock.expect(dao.findById(1L)).andReturn(entity).anyTimes();
        dao.remove(EasyMock.capture(entityCapture));
        EasyMock.expectLastCall();
        EasyMock.replay(dao);
        provider.deleteResources(new org.apache.ambari.server.controller.internal.RequestImpl(null, null, null, null), p);
        org.apache.ambari.server.orm.entities.AlertDefinitionEntity entity1 = entityCapture.getValue();
        org.junit.Assert.assertEquals(java.lang.Long.valueOf(1), entity1.getDefinitionId());
        EasyMock.verify(amc, clusters, cluster, dao);
    }

    private org.apache.ambari.server.controller.internal.AlertDefinitionResourceProvider createProvider(org.apache.ambari.server.controller.AmbariManagementController amc) {
        return new org.apache.ambari.server.controller.internal.AlertDefinitionResourceProvider(amc);
    }

    private java.util.List<org.apache.ambari.server.orm.entities.AlertDefinitionEntity> getMockEntities() throws java.lang.Exception {
        org.apache.ambari.server.state.alert.Source source = getMockSource();
        java.lang.String sourceJson = new com.google.gson.Gson().toJson(source);
        org.apache.ambari.server.orm.entities.ResourceEntity clusterResourceEntity = new org.apache.ambari.server.orm.entities.ResourceEntity();
        clusterResourceEntity.setId(4L);
        org.apache.ambari.server.orm.entities.ClusterEntity clusterEntity = new org.apache.ambari.server.orm.entities.ClusterEntity();
        clusterEntity.setResource(clusterResourceEntity);
        org.apache.ambari.server.orm.entities.AlertDefinitionEntity entity = new org.apache.ambari.server.orm.entities.AlertDefinitionEntity();
        entity.setClusterId(java.lang.Long.valueOf(1L));
        entity.setComponentName(null);
        entity.setDefinitionId(java.lang.Long.valueOf(1L));
        entity.setDefinitionName("my_def");
        entity.setLabel("Mock Label");
        entity.setDescription("Mock Description");
        entity.setEnabled(true);
        entity.setHash(org.apache.ambari.server.controller.internal.AlertDefinitionResourceProviderTest.DEFINITION_UUID);
        entity.setScheduleInterval(java.lang.Integer.valueOf(2));
        entity.setServiceName(null);
        entity.setSourceType(org.apache.ambari.server.state.alert.SourceType.METRIC);
        entity.setSource(sourceJson);
        entity.setCluster(clusterEntity);
        entity.setHelpURL("http://test-help-url");
        return java.util.Arrays.asList(entity);
    }

    private org.apache.ambari.server.state.alert.Source getMockSource() throws java.lang.Exception {
        java.io.File alertsFile = new java.io.File("src/test/resources/stacks/HDP/2.0.5/services/HDFS/alerts.json");
        org.junit.Assert.assertTrue(alertsFile.exists());
        java.util.Set<org.apache.ambari.server.state.alert.AlertDefinition> set = m_factory.getAlertDefinitions(alertsFile, "HDFS");
        org.apache.ambari.server.state.alert.AlertDefinition nameNodeCpu = null;
        java.util.Iterator<org.apache.ambari.server.state.alert.AlertDefinition> definitions = set.iterator();
        while (definitions.hasNext()) {
            org.apache.ambari.server.state.alert.AlertDefinition definition = definitions.next();
            if (definition.getName().equals("namenode_cpu")) {
                nameNodeCpu = definition;
            }
        } 
        org.junit.Assert.assertNotNull(nameNodeCpu.getSource());
        return nameNodeCpu.getSource();
    }

    private class MockModule implements com.google.inject.Module {
        @java.lang.Override
        public void configure(com.google.inject.Binder binder) {
            binder.bind(org.apache.ambari.server.orm.dao.AlertDefinitionDAO.class).toInstance(dao);
            binder.bind(org.apache.ambari.server.state.alert.AlertDefinitionHash.class).toInstance(definitionHash);
            binder.bind(org.apache.ambari.server.state.Clusters.class).toInstance(org.easymock.EasyMock.createNiceMock(org.apache.ambari.server.state.Clusters.class));
            binder.bind(org.apache.ambari.server.state.Cluster.class).toInstance(org.easymock.EasyMock.createNiceMock(org.apache.ambari.server.state.Cluster.class));
            binder.bind(org.apache.ambari.server.metadata.ActionMetadata.class);
        }
    }
}