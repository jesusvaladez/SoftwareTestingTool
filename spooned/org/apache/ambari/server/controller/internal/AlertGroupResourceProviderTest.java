package org.apache.ambari.server.controller.internal;
import org.easymock.Capture;
import org.easymock.EasyMock;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import static org.easymock.EasyMock.anyObject;
import static org.easymock.EasyMock.capture;
import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.resetToStrict;
import static org.easymock.EasyMock.verify;
public class AlertGroupResourceProviderTest {
    private static final java.lang.Long ALERT_GROUP_ID = java.lang.Long.valueOf(28);

    private static final java.lang.String ALERT_GROUP_NAME = "Important Alerts";

    private static final long ALERT_GROUP_CLUSTER_ID = 1L;

    private static final java.lang.String ALERT_GROUP_CLUSTER_NAME = "c1";

    private static final java.lang.Long ALERT_TARGET_ID = java.lang.Long.valueOf(28);

    private static final java.lang.String ALERT_TARGET_NAME = "The Administrators";

    private static final java.lang.String ALERT_TARGET_DESC = "Admins and Others";

    private static final java.lang.String ALERT_TARGET_TYPE = org.apache.ambari.server.state.alert.TargetType.EMAIL.name();

    private static final java.lang.Long ALERT_DEF_ID = 10L;

    private static final java.lang.String ALERT_DEF_NAME = "Mock Definition";

    private static final java.lang.String ALERT_DEF_LABEL = "Mock Label";

    private static final java.lang.String ALERT_DEF_DESCRIPTION = "Mock Description";

    private static java.lang.String DEFINITION_UUID = java.util.UUID.randomUUID().toString();

    private org.apache.ambari.server.orm.dao.AlertDispatchDAO m_dao;

    private org.apache.ambari.server.orm.dao.AlertDefinitionDAO m_definitionDao;

    private com.google.inject.Injector m_injector;

    private org.apache.ambari.server.controller.AmbariManagementController m_amc;

    private org.apache.ambari.server.state.Clusters m_clusters;

    private org.apache.ambari.server.state.Cluster m_cluster;

    @org.junit.Before
    public void before() throws java.lang.Exception {
        m_dao = EasyMock.createMock(org.apache.ambari.server.orm.dao.AlertDispatchDAO.class);
        m_definitionDao = EasyMock.createMock(org.apache.ambari.server.orm.dao.AlertDefinitionDAO.class);
        m_amc = EasyMock.createMock(org.apache.ambari.server.controller.AmbariManagementController.class);
        m_clusters = EasyMock.createMock(org.apache.ambari.server.state.Clusters.class);
        m_cluster = EasyMock.createMock(org.apache.ambari.server.state.Cluster.class);
        m_injector = com.google.inject.Guice.createInjector(com.google.inject.util.Modules.override(new org.apache.ambari.server.orm.InMemoryDefaultTestModule()).with(new org.apache.ambari.server.controller.internal.AlertGroupResourceProviderTest.MockModule()));
        junit.framework.Assert.assertNotNull(m_injector);
        EasyMock.expect(m_amc.getClusters()).andReturn(m_clusters).anyTimes();
        EasyMock.expect(m_clusters.getCluster(((java.lang.String) (EasyMock.anyObject())))).andReturn(m_cluster).anyTimes();
        EasyMock.expect(m_clusters.getClusterById(1L)).andReturn(m_cluster).anyTimes();
        EasyMock.expect(m_cluster.getClusterId()).andReturn(1L).anyTimes();
        EasyMock.expect(m_cluster.getResourceId()).andReturn(4L).anyTimes();
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

    private void testGetResourcesNoPredicate(org.springframework.security.core.Authentication authentication) throws java.lang.Exception {
        org.apache.ambari.server.controller.internal.AlertGroupResourceProvider provider = createProvider(m_amc);
        org.apache.ambari.server.controller.spi.Request request = org.apache.ambari.server.controller.utilities.PropertyHelper.getReadRequest("AlertGroup/cluster_name", "AlertGroup/id");
        org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(authentication);
        java.util.Set<org.apache.ambari.server.controller.spi.Resource> results = provider.getResources(request, null);
        junit.framework.Assert.assertEquals(0, results.size());
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
        org.apache.ambari.server.controller.spi.Request request = org.apache.ambari.server.controller.utilities.PropertyHelper.getReadRequest(org.apache.ambari.server.controller.internal.AlertGroupResourceProvider.ALERT_GROUP_ID, org.apache.ambari.server.controller.internal.AlertGroupResourceProvider.ALERT_GROUP_NAME, org.apache.ambari.server.controller.internal.AlertGroupResourceProvider.ALERT_GROUP_CLUSTER_NAME, org.apache.ambari.server.controller.internal.AlertGroupResourceProvider.ALERT_GROUP_DEFAULT);
        org.apache.ambari.server.controller.spi.Predicate predicate = new org.apache.ambari.server.controller.utilities.PredicateBuilder().property(org.apache.ambari.server.controller.internal.AlertGroupResourceProvider.ALERT_GROUP_CLUSTER_NAME).equals("c1").toPredicate();
        EasyMock.expect(m_dao.findAllGroups(org.apache.ambari.server.controller.internal.AlertGroupResourceProviderTest.ALERT_GROUP_CLUSTER_ID)).andReturn(getMockEntities());
        EasyMock.replay(m_amc, m_clusters, m_cluster, m_dao);
        org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(authentication);
        org.apache.ambari.server.controller.internal.AlertGroupResourceProvider provider = createProvider(m_amc);
        java.util.Set<org.apache.ambari.server.controller.spi.Resource> results = provider.getResources(request, predicate);
        junit.framework.Assert.assertEquals(expectResults ? 1 : 0, results.size());
        if (expectResults) {
            org.apache.ambari.server.controller.spi.Resource r = results.iterator().next();
            junit.framework.Assert.assertEquals(org.apache.ambari.server.controller.internal.AlertGroupResourceProviderTest.ALERT_GROUP_NAME, r.getPropertyValue(org.apache.ambari.server.controller.internal.AlertGroupResourceProvider.ALERT_GROUP_NAME));
            junit.framework.Assert.assertEquals(org.apache.ambari.server.controller.internal.AlertGroupResourceProviderTest.ALERT_GROUP_ID, r.getPropertyValue(org.apache.ambari.server.controller.internal.AlertGroupResourceProvider.ALERT_GROUP_ID));
            junit.framework.Assert.assertEquals(org.apache.ambari.server.controller.internal.AlertGroupResourceProviderTest.ALERT_GROUP_CLUSTER_NAME, r.getPropertyValue(org.apache.ambari.server.controller.internal.AlertGroupResourceProvider.ALERT_GROUP_CLUSTER_NAME));
            junit.framework.Assert.assertNull(r.getPropertyValue(org.apache.ambari.server.controller.internal.AlertGroupResourceProvider.ALERT_GROUP_DEFINITIONS));
            junit.framework.Assert.assertNull(r.getPropertyValue(org.apache.ambari.server.controller.internal.AlertGroupResourceProvider.ALERT_GROUP_TARGETS));
        }
        EasyMock.verify(m_amc, m_clusters, m_cluster, m_dao);
    }

    @org.junit.Test
    public void testGetResourcesAllPropertiesAsAdministrator() throws java.lang.Exception {
        testGetResourcesAllProperties(org.apache.ambari.server.security.TestAuthenticationFactory.createAdministrator(), true);
    }

    @org.junit.Test
    public void testGetResourcesAllPropertiesAsClusterAdministrator() throws java.lang.Exception {
        testGetResourcesAllProperties(org.apache.ambari.server.security.TestAuthenticationFactory.createClusterAdministrator(), true);
    }

    @org.junit.Test
    public void testGetResourcesAllPropertiesAsServiceAdministrator() throws java.lang.Exception {
        testGetResourcesAllProperties(org.apache.ambari.server.security.TestAuthenticationFactory.createServiceAdministrator(), true);
    }

    @org.junit.Test
    public void testGetResourcesAllPropertiesAsClusterUser() throws java.lang.Exception {
        testGetResourcesAllProperties(org.apache.ambari.server.security.TestAuthenticationFactory.createClusterUser(), true);
    }

    @org.junit.Test
    public void testGetResourcesAllPropertiesAsViewUser() throws java.lang.Exception {
        testGetResourcesAllProperties(org.apache.ambari.server.security.TestAuthenticationFactory.createViewUser(99L), false);
    }

    private void testGetResourcesAllProperties(org.springframework.security.core.Authentication authentication, boolean expectResults) throws java.lang.Exception {
        org.apache.ambari.server.controller.spi.Request request = org.apache.ambari.server.controller.utilities.PropertyHelper.getReadRequest();
        org.apache.ambari.server.controller.spi.Predicate predicate = new org.apache.ambari.server.controller.utilities.PredicateBuilder().property(org.apache.ambari.server.controller.internal.AlertGroupResourceProvider.ALERT_GROUP_CLUSTER_NAME).equals("c1").toPredicate();
        EasyMock.expect(m_dao.findAllGroups(org.apache.ambari.server.controller.internal.AlertGroupResourceProviderTest.ALERT_GROUP_CLUSTER_ID)).andReturn(getMockEntities());
        EasyMock.replay(m_amc, m_clusters, m_cluster, m_dao);
        org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(authentication);
        org.apache.ambari.server.controller.internal.AlertGroupResourceProvider provider = createProvider(m_amc);
        java.util.Set<org.apache.ambari.server.controller.spi.Resource> results = provider.getResources(request, predicate);
        junit.framework.Assert.assertEquals(expectResults ? 1 : 0, results.size());
        if (expectResults) {
            org.apache.ambari.server.controller.spi.Resource r = results.iterator().next();
            junit.framework.Assert.assertEquals(org.apache.ambari.server.controller.internal.AlertGroupResourceProviderTest.ALERT_GROUP_NAME, r.getPropertyValue(org.apache.ambari.server.controller.internal.AlertGroupResourceProvider.ALERT_GROUP_NAME));
            junit.framework.Assert.assertEquals(org.apache.ambari.server.controller.internal.AlertGroupResourceProviderTest.ALERT_GROUP_ID, r.getPropertyValue(org.apache.ambari.server.controller.internal.AlertGroupResourceProvider.ALERT_GROUP_ID));
            junit.framework.Assert.assertEquals(org.apache.ambari.server.controller.internal.AlertGroupResourceProviderTest.ALERT_GROUP_CLUSTER_NAME, r.getPropertyValue(org.apache.ambari.server.controller.internal.AlertGroupResourceProvider.ALERT_GROUP_CLUSTER_NAME));
            java.util.List<org.apache.ambari.server.controller.AlertDefinitionResponse> definitions = ((java.util.List<org.apache.ambari.server.controller.AlertDefinitionResponse>) (r.getPropertyValue(org.apache.ambari.server.controller.internal.AlertGroupResourceProvider.ALERT_GROUP_DEFINITIONS)));
            java.util.List<?> targets = ((java.util.List<?>) (r.getPropertyValue(org.apache.ambari.server.controller.internal.AlertGroupResourceProvider.ALERT_GROUP_TARGETS)));
            junit.framework.Assert.assertNotNull(definitions);
            junit.framework.Assert.assertEquals(1, definitions.size());
            junit.framework.Assert.assertEquals(org.apache.ambari.server.controller.internal.AlertGroupResourceProviderTest.ALERT_DEF_NAME, definitions.get(0).getName());
            junit.framework.Assert.assertEquals(org.apache.ambari.server.state.alert.SourceType.METRIC, definitions.get(0).getSourceType());
            junit.framework.Assert.assertNotNull(targets);
            junit.framework.Assert.assertEquals(1, targets.size());
        }
        EasyMock.verify(m_amc, m_clusters, m_cluster, m_dao);
    }

    @org.junit.Test
    public void testGetSingleResourceAsAdministrator() throws java.lang.Exception {
        testGetSingleResource(org.apache.ambari.server.security.TestAuthenticationFactory.createAdministrator(), true);
    }

    @org.junit.Test
    public void testGetSingleResourceAsClusterAdministrator() throws java.lang.Exception {
        testGetSingleResource(org.apache.ambari.server.security.TestAuthenticationFactory.createClusterAdministrator(), true);
    }

    @org.junit.Test
    public void testGetSingleResourceAsServiceAdministrator() throws java.lang.Exception {
        testGetSingleResource(org.apache.ambari.server.security.TestAuthenticationFactory.createServiceAdministrator(), true);
    }

    @org.junit.Test
    public void testGetSingleResourceAsClusterUser() throws java.lang.Exception {
        testGetSingleResource(org.apache.ambari.server.security.TestAuthenticationFactory.createClusterUser(), true);
    }

    @org.junit.Test(expected = org.apache.ambari.server.security.authorization.AuthorizationException.class)
    public void testGetSingleResourceAsViewUser() throws java.lang.Exception {
        testGetSingleResource(org.apache.ambari.server.security.TestAuthenticationFactory.createViewUser(99L), false);
    }

    @java.lang.SuppressWarnings("unchecked")
    private void testGetSingleResource(org.springframework.security.core.Authentication authentication, boolean expectResults) throws java.lang.Exception {
        org.apache.ambari.server.controller.spi.Request request = org.apache.ambari.server.controller.utilities.PropertyHelper.getReadRequest();
        org.apache.ambari.server.controller.AmbariManagementController amc = EasyMock.createMock(org.apache.ambari.server.controller.AmbariManagementController.class);
        org.apache.ambari.server.controller.spi.Predicate predicate = new org.apache.ambari.server.controller.utilities.PredicateBuilder().property(org.apache.ambari.server.controller.internal.AlertGroupResourceProvider.ALERT_GROUP_CLUSTER_NAME).equals(org.apache.ambari.server.controller.internal.AlertGroupResourceProviderTest.ALERT_GROUP_CLUSTER_NAME).and().property(org.apache.ambari.server.controller.internal.AlertGroupResourceProvider.ALERT_GROUP_ID).equals(org.apache.ambari.server.controller.internal.AlertGroupResourceProviderTest.ALERT_GROUP_ID.toString()).toPredicate();
        EasyMock.expect(m_dao.findGroupById(org.apache.ambari.server.controller.internal.AlertGroupResourceProviderTest.ALERT_GROUP_ID.longValue())).andReturn(getMockEntities().get(0));
        EasyMock.expect(amc.getClusters()).andReturn(m_clusters).atLeastOnce();
        EasyMock.replay(amc, m_dao, m_clusters, m_cluster);
        org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(authentication);
        org.apache.ambari.server.controller.internal.AlertGroupResourceProvider provider = createProvider(amc);
        java.util.Set<org.apache.ambari.server.controller.spi.Resource> results = provider.getResources(request, predicate);
        junit.framework.Assert.assertEquals(expectResults ? 1 : 0, results.size());
        if (expectResults) {
            org.apache.ambari.server.controller.spi.Resource r = results.iterator().next();
            junit.framework.Assert.assertEquals(org.apache.ambari.server.controller.internal.AlertGroupResourceProviderTest.ALERT_GROUP_NAME, r.getPropertyValue(org.apache.ambari.server.controller.internal.AlertGroupResourceProvider.ALERT_GROUP_NAME));
            junit.framework.Assert.assertEquals(org.apache.ambari.server.controller.internal.AlertGroupResourceProviderTest.ALERT_GROUP_ID, r.getPropertyValue(org.apache.ambari.server.controller.internal.AlertGroupResourceProvider.ALERT_GROUP_ID));
            junit.framework.Assert.assertEquals(org.apache.ambari.server.controller.internal.AlertGroupResourceProviderTest.ALERT_GROUP_CLUSTER_NAME, r.getPropertyValue(org.apache.ambari.server.controller.internal.AlertGroupResourceProvider.ALERT_GROUP_CLUSTER_NAME));
            java.util.List<org.apache.ambari.server.controller.AlertDefinitionResponse> definitions = ((java.util.List<org.apache.ambari.server.controller.AlertDefinitionResponse>) (r.getPropertyValue(org.apache.ambari.server.controller.internal.AlertGroupResourceProvider.ALERT_GROUP_DEFINITIONS)));
            java.util.List<org.apache.ambari.server.state.alert.AlertTarget> targets = ((java.util.List<org.apache.ambari.server.state.alert.AlertTarget>) (r.getPropertyValue(org.apache.ambari.server.controller.internal.AlertGroupResourceProvider.ALERT_GROUP_TARGETS)));
            junit.framework.Assert.assertNotNull(definitions);
            junit.framework.Assert.assertNotNull(targets);
            junit.framework.Assert.assertEquals(1, definitions.size());
            junit.framework.Assert.assertEquals(org.apache.ambari.server.controller.internal.AlertGroupResourceProviderTest.ALERT_DEF_NAME, definitions.get(0).getName());
            junit.framework.Assert.assertEquals(org.apache.ambari.server.state.alert.SourceType.METRIC, definitions.get(0).getSourceType());
            junit.framework.Assert.assertEquals(1, targets.size());
            junit.framework.Assert.assertEquals(org.apache.ambari.server.controller.internal.AlertGroupResourceProviderTest.ALERT_TARGET_NAME, targets.get(0).getName());
        }
        EasyMock.verify(amc, m_dao);
    }

    @org.junit.Test
    public void testCreateResourcesAsAdministrator() throws java.lang.Exception {
        testCreateResources(org.apache.ambari.server.security.TestAuthenticationFactory.createAdministrator());
    }

    @org.junit.Test
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
        org.easymock.Capture<java.util.List<org.apache.ambari.server.orm.entities.AlertGroupEntity>> listCapture = org.easymock.EasyMock.newCapture();
        java.util.List<java.lang.Long> definitionIds = new java.util.ArrayList<>();
        definitionIds.add(org.apache.ambari.server.controller.internal.AlertGroupResourceProviderTest.ALERT_DEF_ID);
        java.util.List<java.lang.Long> targetIds = new java.util.ArrayList<>();
        targetIds.add(org.apache.ambari.server.controller.internal.AlertGroupResourceProviderTest.ALERT_TARGET_ID);
        java.util.List<org.apache.ambari.server.orm.entities.AlertDefinitionEntity> definitionEntities = new java.util.ArrayList<>();
        definitionEntities.addAll(getMockDefinitions());
        java.util.List<org.apache.ambari.server.orm.entities.AlertTargetEntity> targetEntities = new java.util.ArrayList<>();
        targetEntities.addAll(getMockTargets());
        m_dao.createGroups(EasyMock.capture(listCapture));
        EasyMock.expectLastCall().once();
        EasyMock.expect(m_dao.findTargetsById(org.easymock.EasyMock.eq(targetIds))).andReturn(targetEntities).times(1);
        EasyMock.expect(m_definitionDao.findByIds(definitionIds)).andReturn(definitionEntities).times(1);
        EasyMock.replay(m_amc, m_clusters, m_cluster, m_dao, m_definitionDao);
        org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(authentication);
        org.apache.ambari.server.controller.internal.AlertGroupResourceProvider provider = createProvider(m_amc);
        java.util.Map<java.lang.String, java.lang.Object> requestProps = new java.util.HashMap<>();
        requestProps.put(org.apache.ambari.server.controller.internal.AlertGroupResourceProvider.ALERT_GROUP_NAME, org.apache.ambari.server.controller.internal.AlertGroupResourceProviderTest.ALERT_GROUP_NAME);
        requestProps.put(org.apache.ambari.server.controller.internal.AlertGroupResourceProvider.ALERT_GROUP_CLUSTER_NAME, org.apache.ambari.server.controller.internal.AlertGroupResourceProviderTest.ALERT_GROUP_CLUSTER_NAME);
        requestProps.put(org.apache.ambari.server.controller.internal.AlertGroupResourceProvider.ALERT_GROUP_DEFINITIONS, definitionIds);
        requestProps.put(org.apache.ambari.server.controller.internal.AlertGroupResourceProvider.ALERT_GROUP_TARGETS, targetIds);
        org.apache.ambari.server.controller.spi.Request request = org.apache.ambari.server.controller.utilities.PropertyHelper.getCreateRequest(java.util.Collections.singleton(requestProps), null);
        provider.createResources(request);
        junit.framework.Assert.assertTrue(listCapture.hasCaptured());
        org.apache.ambari.server.orm.entities.AlertGroupEntity entity = listCapture.getValue().get(0);
        junit.framework.Assert.assertNotNull(entity);
        junit.framework.Assert.assertEquals(org.apache.ambari.server.controller.internal.AlertGroupResourceProviderTest.ALERT_GROUP_NAME, entity.getGroupName());
        junit.framework.Assert.assertEquals(org.apache.ambari.server.controller.internal.AlertGroupResourceProviderTest.ALERT_GROUP_CLUSTER_ID, entity.getClusterId().longValue());
        EasyMock.verify(m_amc, m_clusters, m_cluster, m_dao, m_definitionDao);
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

    @java.lang.SuppressWarnings("unchecked")
    public void testUpdateResources(org.springframework.security.core.Authentication authentication) throws java.lang.Exception {
        org.easymock.Capture<org.apache.ambari.server.orm.entities.AlertGroupEntity> entityCapture = org.easymock.EasyMock.newCapture();
        java.util.List<java.lang.Long> definitionIds = new java.util.ArrayList<>();
        definitionIds.add(org.apache.ambari.server.controller.internal.AlertGroupResourceProviderTest.ALERT_DEF_ID);
        java.util.List<java.lang.Long> targetIds = new java.util.ArrayList<>();
        targetIds.add(org.apache.ambari.server.controller.internal.AlertGroupResourceProviderTest.ALERT_TARGET_ID);
        java.util.List<org.apache.ambari.server.orm.entities.AlertDefinitionEntity> definitionEntities = new java.util.ArrayList<>();
        definitionEntities.addAll(getMockDefinitions());
        java.util.List<org.apache.ambari.server.orm.entities.AlertTargetEntity> targetEntities = new java.util.ArrayList<>();
        targetEntities.addAll(getMockTargets());
        m_dao.createGroups(org.easymock.EasyMock.anyObject());
        EasyMock.expectLastCall().times(1);
        org.apache.ambari.server.orm.entities.AlertGroupEntity group = new org.apache.ambari.server.orm.entities.AlertGroupEntity();
        EasyMock.expect(m_dao.findGroupById(org.apache.ambari.server.controller.internal.AlertGroupResourceProviderTest.ALERT_GROUP_ID)).andReturn(group).times(1);
        EasyMock.expect(m_dao.merge(EasyMock.capture(entityCapture))).andReturn(group).once();
        EasyMock.expect(m_dao.findTargetsById(org.easymock.EasyMock.eq(targetIds))).andReturn(targetEntities).once();
        EasyMock.expect(m_definitionDao.findByIds(definitionIds)).andReturn(definitionEntities).once();
        EasyMock.replay(m_amc, m_clusters, m_cluster, m_dao, m_definitionDao);
        org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(authentication);
        org.apache.ambari.server.controller.internal.AlertGroupResourceProvider provider = createProvider(m_amc);
        java.util.Map<java.lang.String, java.lang.Object> requestProps = new java.util.HashMap<>();
        requestProps.put(org.apache.ambari.server.controller.internal.AlertGroupResourceProvider.ALERT_GROUP_NAME, org.apache.ambari.server.controller.internal.AlertGroupResourceProviderTest.ALERT_GROUP_NAME);
        requestProps.put(org.apache.ambari.server.controller.internal.AlertGroupResourceProvider.ALERT_GROUP_CLUSTER_NAME, org.apache.ambari.server.controller.internal.AlertGroupResourceProviderTest.ALERT_GROUP_CLUSTER_NAME);
        org.apache.ambari.server.controller.spi.Request request = org.apache.ambari.server.controller.utilities.PropertyHelper.getCreateRequest(java.util.Collections.singleton(requestProps), null);
        provider.createResources(request);
        requestProps = new java.util.HashMap<>();
        requestProps.put(org.apache.ambari.server.controller.internal.AlertGroupResourceProvider.ALERT_GROUP_ID, org.apache.ambari.server.controller.internal.AlertGroupResourceProviderTest.ALERT_GROUP_ID.toString());
        java.lang.String newName = org.apache.ambari.server.controller.internal.AlertGroupResourceProviderTest.ALERT_GROUP_NAME + " Foo";
        requestProps.put(org.apache.ambari.server.controller.internal.AlertGroupResourceProvider.ALERT_GROUP_NAME, newName);
        requestProps.put(org.apache.ambari.server.controller.internal.AlertGroupResourceProvider.ALERT_GROUP_DEFINITIONS, definitionIds);
        requestProps.put(org.apache.ambari.server.controller.internal.AlertGroupResourceProvider.ALERT_GROUP_TARGETS, targetIds);
        org.apache.ambari.server.controller.spi.Predicate predicate = new org.apache.ambari.server.controller.utilities.PredicateBuilder().property(org.apache.ambari.server.controller.internal.AlertGroupResourceProvider.ALERT_GROUP_CLUSTER_NAME).equals(org.apache.ambari.server.controller.internal.AlertGroupResourceProviderTest.ALERT_GROUP_CLUSTER_NAME).and().property(org.apache.ambari.server.controller.internal.AlertGroupResourceProvider.ALERT_GROUP_ID).equals(org.apache.ambari.server.controller.internal.AlertGroupResourceProviderTest.ALERT_GROUP_ID.toString()).toPredicate();
        request = org.apache.ambari.server.controller.utilities.PropertyHelper.getUpdateRequest(requestProps, null);
        provider.updateResources(request, predicate);
        junit.framework.Assert.assertTrue(entityCapture.hasCaptured());
        org.apache.ambari.server.orm.entities.AlertGroupEntity entity = entityCapture.getValue();
        junit.framework.Assert.assertEquals(newName, entity.getGroupName());
        EasyMock.verify(m_amc, m_clusters, m_cluster, m_dao, m_definitionDao);
    }

    @org.junit.Test
    public void testUpdateDefaultGroupAsAdministrator() throws java.lang.Exception {
        testUpdateDefaultGroup(org.apache.ambari.server.security.TestAuthenticationFactory.createAdministrator());
    }

    @org.junit.Test
    public void testUpdateDefaultGroupAsClusterAdministrator() throws java.lang.Exception {
        testUpdateDefaultGroup(org.apache.ambari.server.security.TestAuthenticationFactory.createClusterAdministrator());
    }

    @org.junit.Test(expected = org.apache.ambari.server.security.authorization.AuthorizationException.class)
    public void testUpdateDefaultGroupAsServiceAdministrator() throws java.lang.Exception {
        testUpdateDefaultGroup(org.apache.ambari.server.security.TestAuthenticationFactory.createServiceAdministrator());
    }

    @org.junit.Test(expected = org.apache.ambari.server.security.authorization.AuthorizationException.class)
    public void testUpdateDefaultGroupAsClusterUser() throws java.lang.Exception {
        testUpdateDefaultGroup(org.apache.ambari.server.security.TestAuthenticationFactory.createClusterUser());
    }

    @org.junit.Test(expected = org.apache.ambari.server.security.authorization.AuthorizationException.class)
    public void testUpdateDefaultGroupAsViewUser() throws java.lang.Exception {
        testUpdateDefaultGroup(org.apache.ambari.server.security.TestAuthenticationFactory.createViewUser(99L));
    }

    @java.lang.SuppressWarnings("unchecked")
    private void testUpdateDefaultGroup(org.springframework.security.core.Authentication authentication) throws java.lang.Exception {
        org.easymock.Capture<org.apache.ambari.server.orm.entities.AlertGroupEntity> entityCapture = org.easymock.EasyMock.newCapture();
        java.util.List<java.lang.Long> definitionIds = new java.util.ArrayList<>();
        definitionIds.add(org.apache.ambari.server.controller.internal.AlertGroupResourceProviderTest.ALERT_DEF_ID);
        java.util.List<java.lang.Long> targetIds = new java.util.ArrayList<>();
        targetIds.add(org.apache.ambari.server.controller.internal.AlertGroupResourceProviderTest.ALERT_TARGET_ID);
        java.util.List<org.apache.ambari.server.orm.entities.AlertDefinitionEntity> definitionEntities = new java.util.ArrayList<>();
        definitionEntities.addAll(getMockDefinitions());
        java.util.List<org.apache.ambari.server.orm.entities.AlertTargetEntity> newTargetEntities = new java.util.ArrayList<>();
        newTargetEntities.addAll(getMockTargets());
        java.util.Set<org.apache.ambari.server.orm.entities.AlertTargetEntity> mockTargets2 = getMockTargets();
        org.apache.ambari.server.orm.entities.AlertTargetEntity target2 = mockTargets2.iterator().next();
        target2.setTargetId(29L);
        newTargetEntities.add(target2);
        org.apache.ambari.server.orm.entities.AlertGroupEntity group = new org.apache.ambari.server.orm.entities.AlertGroupEntity();
        group.setDefault(true);
        group.setClusterId(1L);
        group.setGroupName(org.apache.ambari.server.controller.internal.AlertGroupResourceProviderTest.ALERT_GROUP_NAME);
        group.setAlertDefinitions(getMockDefinitions());
        group.setAlertTargets(getMockTargets());
        EasyMock.expect(m_dao.findGroupById(org.apache.ambari.server.controller.internal.AlertGroupResourceProviderTest.ALERT_GROUP_ID)).andReturn(group).times(1);
        EasyMock.expect(m_dao.merge(EasyMock.capture(entityCapture))).andReturn(group).once();
        java.util.List<java.lang.Long> newTargets = java.util.Arrays.asList(28L, 29L);
        EasyMock.expect(m_dao.findTargetsById(org.easymock.EasyMock.eq(newTargets))).andReturn(newTargetEntities).once();
        EasyMock.replay(m_dao, m_definitionDao, m_amc, m_clusters, m_cluster);
        org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(authentication);
        org.apache.ambari.server.controller.internal.AlertGroupResourceProvider provider = createProvider(m_amc);
        java.util.Map<java.lang.String, java.lang.Object> requestProps = new java.util.HashMap<>();
        requestProps.put(org.apache.ambari.server.controller.internal.AlertGroupResourceProvider.ALERT_GROUP_ID, org.apache.ambari.server.controller.internal.AlertGroupResourceProviderTest.ALERT_GROUP_ID.toString());
        java.lang.String newName = org.apache.ambari.server.controller.internal.AlertGroupResourceProviderTest.ALERT_GROUP_NAME + " Foo";
        requestProps.put(org.apache.ambari.server.controller.internal.AlertGroupResourceProvider.ALERT_GROUP_NAME, newName);
        requestProps.put(org.apache.ambari.server.controller.internal.AlertGroupResourceProvider.ALERT_GROUP_DEFINITIONS, new java.util.ArrayList<java.lang.Long>());
        requestProps.put(org.apache.ambari.server.controller.internal.AlertGroupResourceProvider.ALERT_GROUP_TARGETS, newTargets);
        org.apache.ambari.server.controller.spi.Predicate predicate = new org.apache.ambari.server.controller.utilities.PredicateBuilder().property(org.apache.ambari.server.controller.internal.AlertGroupResourceProvider.ALERT_GROUP_CLUSTER_NAME).equals(org.apache.ambari.server.controller.internal.AlertGroupResourceProviderTest.ALERT_GROUP_CLUSTER_NAME).and().property(org.apache.ambari.server.controller.internal.AlertGroupResourceProvider.ALERT_GROUP_ID).equals(org.apache.ambari.server.controller.internal.AlertGroupResourceProviderTest.ALERT_GROUP_ID.toString()).toPredicate();
        org.apache.ambari.server.controller.spi.Request request = org.apache.ambari.server.controller.utilities.PropertyHelper.getUpdateRequest(requestProps, null);
        provider.updateResources(request, predicate);
        junit.framework.Assert.assertTrue(entityCapture.hasCaptured());
        org.apache.ambari.server.orm.entities.AlertGroupEntity entity = entityCapture.getValue();
        junit.framework.Assert.assertEquals(org.apache.ambari.server.controller.internal.AlertGroupResourceProviderTest.ALERT_GROUP_NAME, entity.getGroupName());
        junit.framework.Assert.assertEquals(2, entity.getAlertTargets().size());
        junit.framework.Assert.assertEquals(1, entity.getAlertDefinitions().size());
        EasyMock.verify(m_dao, m_definitionDao);
    }

    @org.junit.Test
    public void testDeleteResourcesAsAdministrator() throws java.lang.Exception {
        testDeleteResources(org.apache.ambari.server.security.TestAuthenticationFactory.createAdministrator());
    }

    @org.junit.Test
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

    private void testDeleteResources(org.springframework.security.core.Authentication authentication) throws java.lang.Exception {
        org.easymock.Capture<org.apache.ambari.server.orm.entities.AlertGroupEntity> entityCapture = org.easymock.EasyMock.newCapture();
        org.easymock.Capture<java.util.List<org.apache.ambari.server.orm.entities.AlertGroupEntity>> listCapture = org.easymock.EasyMock.newCapture();
        m_dao.createGroups(EasyMock.capture(listCapture));
        EasyMock.expectLastCall();
        EasyMock.replay(m_amc, m_clusters, m_cluster, m_dao);
        org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(authentication);
        org.apache.ambari.server.controller.internal.AlertGroupResourceProvider provider = createProvider(m_amc);
        java.util.Map<java.lang.String, java.lang.Object> requestProps = new java.util.HashMap<>();
        requestProps.put(org.apache.ambari.server.controller.internal.AlertGroupResourceProvider.ALERT_GROUP_NAME, org.apache.ambari.server.controller.internal.AlertGroupResourceProviderTest.ALERT_GROUP_NAME);
        requestProps.put(org.apache.ambari.server.controller.internal.AlertGroupResourceProvider.ALERT_GROUP_CLUSTER_NAME, org.apache.ambari.server.controller.internal.AlertGroupResourceProviderTest.ALERT_GROUP_CLUSTER_NAME);
        org.apache.ambari.server.controller.spi.Request request = org.apache.ambari.server.controller.utilities.PropertyHelper.getCreateRequest(java.util.Collections.singleton(requestProps), null);
        provider.createResources(request);
        junit.framework.Assert.assertTrue(listCapture.hasCaptured());
        org.apache.ambari.server.orm.entities.AlertGroupEntity entity = listCapture.getValue().get(0);
        junit.framework.Assert.assertNotNull(entity);
        org.apache.ambari.server.controller.spi.Predicate predicate = new org.apache.ambari.server.controller.utilities.PredicateBuilder().property(org.apache.ambari.server.controller.internal.AlertGroupResourceProvider.ALERT_GROUP_CLUSTER_NAME).equals(org.apache.ambari.server.controller.internal.AlertGroupResourceProviderTest.ALERT_GROUP_CLUSTER_NAME).and().property(org.apache.ambari.server.controller.internal.AlertGroupResourceProvider.ALERT_GROUP_ID).equals(org.apache.ambari.server.controller.internal.AlertGroupResourceProviderTest.ALERT_GROUP_ID.toString()).toPredicate();
        entity.setGroupId(org.apache.ambari.server.controller.internal.AlertGroupResourceProviderTest.ALERT_GROUP_ID);
        EasyMock.resetToStrict(m_dao);
        EasyMock.expect(m_dao.findGroupById(org.apache.ambari.server.controller.internal.AlertGroupResourceProviderTest.ALERT_GROUP_ID.longValue())).andReturn(entity).anyTimes();
        m_dao.remove(EasyMock.capture(entityCapture));
        EasyMock.expectLastCall();
        EasyMock.replay(m_dao);
        provider.deleteResources(new org.apache.ambari.server.controller.internal.RequestImpl(null, null, null, null), predicate);
        org.apache.ambari.server.orm.entities.AlertGroupEntity entity1 = entityCapture.getValue();
        junit.framework.Assert.assertEquals(org.apache.ambari.server.controller.internal.AlertGroupResourceProviderTest.ALERT_GROUP_ID, entity1.getGroupId());
        EasyMock.verify(m_amc, m_clusters, m_cluster, m_dao);
    }

    @org.junit.Test
    public void testDeleteDefaultGroupAsAdministrator() throws java.lang.Exception {
        testDeleteDefaultGroup(org.apache.ambari.server.security.TestAuthenticationFactory.createAdministrator());
    }

    @org.junit.Test(expected = org.apache.ambari.server.security.authorization.AuthorizationException.class)
    public void testDeleteDefaultGroupAsClusterAdministrator() throws java.lang.Exception {
        testDeleteDefaultGroup(org.apache.ambari.server.security.TestAuthenticationFactory.createClusterAdministrator());
    }

    @org.junit.Test(expected = org.apache.ambari.server.security.authorization.AuthorizationException.class)
    public void testDeleteDefaultGroupAsServiceAdministrator() throws java.lang.Exception {
        testDeleteDefaultGroup(org.apache.ambari.server.security.TestAuthenticationFactory.createServiceAdministrator());
    }

    @org.junit.Test(expected = org.apache.ambari.server.security.authorization.AuthorizationException.class)
    public void testDeleteDefaultGroupAsClusterUser() throws java.lang.Exception {
        testDeleteDefaultGroup(org.apache.ambari.server.security.TestAuthenticationFactory.createClusterUser());
    }

    @org.junit.Test(expected = org.apache.ambari.server.security.authorization.AuthorizationException.class)
    public void testDeleteDefaultGroupAsViewUser() throws java.lang.Exception {
        testDeleteDefaultGroup(org.apache.ambari.server.security.TestAuthenticationFactory.createViewUser(99L));
    }

    private void testDeleteDefaultGroup(org.springframework.security.core.Authentication authentication) throws java.lang.Exception {
        org.apache.ambari.server.orm.entities.AlertGroupEntity group = new org.apache.ambari.server.orm.entities.AlertGroupEntity();
        group.setGroupId(org.apache.ambari.server.controller.internal.AlertGroupResourceProviderTest.ALERT_GROUP_ID);
        group.setDefault(true);
        group.setGroupName(org.apache.ambari.server.controller.internal.AlertGroupResourceProviderTest.ALERT_GROUP_NAME);
        group.setAlertDefinitions(getMockDefinitions());
        group.setAlertTargets(getMockTargets());
        EasyMock.resetToStrict(m_dao);
        EasyMock.expect(m_dao.findGroupById(org.apache.ambari.server.controller.internal.AlertGroupResourceProviderTest.ALERT_GROUP_ID)).andReturn(group).anyTimes();
        EasyMock.replay(m_dao, m_amc, m_clusters, m_cluster);
        org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(authentication);
        org.apache.ambari.server.controller.internal.AlertGroupResourceProvider provider = createProvider(m_amc);
        org.apache.ambari.server.controller.spi.Predicate predicate = new org.apache.ambari.server.controller.utilities.PredicateBuilder().property(org.apache.ambari.server.controller.internal.AlertGroupResourceProvider.ALERT_GROUP_CLUSTER_NAME).equals(org.apache.ambari.server.controller.internal.AlertGroupResourceProviderTest.ALERT_GROUP_CLUSTER_NAME).and().property(org.apache.ambari.server.controller.internal.AlertGroupResourceProvider.ALERT_GROUP_ID).equals(org.apache.ambari.server.controller.internal.AlertGroupResourceProviderTest.ALERT_GROUP_ID.toString()).toPredicate();
        provider.deleteResources(new org.apache.ambari.server.controller.internal.RequestImpl(null, null, null, null), predicate);
        EasyMock.verify(m_dao, m_amc);
    }

    private org.apache.ambari.server.controller.internal.AlertGroupResourceProvider createProvider(org.apache.ambari.server.controller.AmbariManagementController amc) {
        return new org.apache.ambari.server.controller.internal.AlertGroupResourceProvider(amc);
    }

    private java.util.List<org.apache.ambari.server.orm.entities.AlertGroupEntity> getMockEntities() throws java.lang.Exception {
        org.apache.ambari.server.orm.entities.AlertGroupEntity entity = new org.apache.ambari.server.orm.entities.AlertGroupEntity();
        entity.setGroupId(org.apache.ambari.server.controller.internal.AlertGroupResourceProviderTest.ALERT_GROUP_ID);
        entity.setGroupName(org.apache.ambari.server.controller.internal.AlertGroupResourceProviderTest.ALERT_GROUP_NAME);
        entity.setClusterId(org.apache.ambari.server.controller.internal.AlertGroupResourceProviderTest.ALERT_GROUP_CLUSTER_ID);
        entity.setDefault(false);
        entity.setAlertTargets(getMockTargets());
        entity.setAlertDefinitions(getMockDefinitions());
        return java.util.Arrays.asList(entity);
    }

    private java.util.Set<org.apache.ambari.server.orm.entities.AlertDefinitionEntity> getMockDefinitions() throws java.lang.Exception {
        org.apache.ambari.server.orm.entities.AlertDefinitionEntity entity = new org.apache.ambari.server.orm.entities.AlertDefinitionEntity();
        entity.setClusterId(java.lang.Long.valueOf(1L));
        entity.setComponentName(null);
        entity.setDefinitionId(org.apache.ambari.server.controller.internal.AlertGroupResourceProviderTest.ALERT_DEF_ID);
        entity.setDefinitionName(org.apache.ambari.server.controller.internal.AlertGroupResourceProviderTest.ALERT_DEF_NAME);
        entity.setLabel(org.apache.ambari.server.controller.internal.AlertGroupResourceProviderTest.ALERT_DEF_LABEL);
        entity.setDescription(org.apache.ambari.server.controller.internal.AlertGroupResourceProviderTest.ALERT_DEF_DESCRIPTION);
        entity.setEnabled(true);
        entity.setHash(org.apache.ambari.server.controller.internal.AlertGroupResourceProviderTest.DEFINITION_UUID);
        entity.setScheduleInterval(java.lang.Integer.valueOf(2));
        entity.setServiceName(null);
        entity.setSourceType(org.apache.ambari.server.state.alert.SourceType.METRIC);
        entity.setSource("{\"type\" : \"METRIC\"}");
        java.util.Set<org.apache.ambari.server.orm.entities.AlertDefinitionEntity> definitions = new java.util.HashSet<>();
        definitions.add(entity);
        return definitions;
    }

    private java.util.Set<org.apache.ambari.server.orm.entities.AlertTargetEntity> getMockTargets() throws java.lang.Exception {
        org.apache.ambari.server.orm.entities.AlertTargetEntity entity = new org.apache.ambari.server.orm.entities.AlertTargetEntity();
        entity.setTargetId(org.apache.ambari.server.controller.internal.AlertGroupResourceProviderTest.ALERT_TARGET_ID);
        entity.setDescription(org.apache.ambari.server.controller.internal.AlertGroupResourceProviderTest.ALERT_TARGET_DESC);
        entity.setTargetName(org.apache.ambari.server.controller.internal.AlertGroupResourceProviderTest.ALERT_TARGET_NAME);
        entity.setNotificationType(org.apache.ambari.server.controller.internal.AlertGroupResourceProviderTest.ALERT_TARGET_TYPE);
        java.util.Set<org.apache.ambari.server.orm.entities.AlertTargetEntity> targets = new java.util.HashSet<>();
        targets.add(entity);
        return targets;
    }

    private class MockModule implements com.google.inject.Module {
        @java.lang.Override
        public void configure(com.google.inject.Binder binder) {
            binder.bind(org.apache.ambari.server.orm.dao.AlertDispatchDAO.class).toInstance(m_dao);
            binder.bind(org.apache.ambari.server.orm.dao.AlertDefinitionDAO.class).toInstance(m_definitionDao);
            binder.bind(org.apache.ambari.server.state.Clusters.class).toInstance(m_clusters);
            binder.bind(org.apache.ambari.server.state.Cluster.class).toInstance(m_cluster);
            binder.bind(org.apache.ambari.server.metadata.ActionMetadata.class);
        }
    }
}