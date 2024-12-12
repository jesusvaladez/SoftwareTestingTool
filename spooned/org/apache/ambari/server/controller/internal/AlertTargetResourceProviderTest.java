package org.apache.ambari.server.controller.internal;
import org.easymock.Capture;
import org.easymock.EasyMock;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import static org.easymock.EasyMock.capture;
import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.createStrictMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.newCapture;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.resetToStrict;
import static org.easymock.EasyMock.verify;
public class AlertTargetResourceProviderTest {
    private static final java.lang.Long ALERT_TARGET_ID = java.lang.Long.valueOf(28);

    private static final java.lang.String ALERT_TARGET_NAME = "The Administrators";

    private static final java.lang.String ALERT_TARGET_DESC = "Admins and Others";

    private static final java.lang.String ALERT_TARGET_TYPE = org.apache.ambari.server.state.alert.TargetType.EMAIL.name();

    private static final java.lang.String ALERT_TARGET_PROPS = "{\"foo\":\"bar\",\"foobar\":\"baz\"}";

    private static final java.lang.String ALERT_TARGET_PROPS2 = "{\"foobar\":\"baz\"}";

    private org.apache.ambari.server.orm.dao.AlertDispatchDAO m_dao;

    private com.google.inject.Injector m_injector;

    private org.apache.ambari.server.controller.AmbariManagementController m_amc;

    @org.junit.Before
    public void before() {
        m_dao = EasyMock.createStrictMock(org.apache.ambari.server.orm.dao.AlertDispatchDAO.class);
        m_amc = EasyMock.createMock(org.apache.ambari.server.controller.AmbariManagementController.class);
        m_injector = com.google.inject.Guice.createInjector(com.google.inject.util.Modules.override(new org.apache.ambari.server.orm.InMemoryDefaultTestModule()).with(new org.apache.ambari.server.controller.internal.AlertTargetResourceProviderTest.MockModule()));
        org.junit.Assert.assertNotNull(m_injector);
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
        org.apache.ambari.server.controller.spi.Request request = org.apache.ambari.server.controller.utilities.PropertyHelper.getReadRequest(org.apache.ambari.server.controller.internal.AlertTargetResourceProvider.ALERT_TARGET_DESCRIPTION, org.apache.ambari.server.controller.internal.AlertTargetResourceProvider.ALERT_TARGET_ID, org.apache.ambari.server.controller.internal.AlertTargetResourceProvider.ALERT_TARGET_NAME, org.apache.ambari.server.controller.internal.AlertTargetResourceProvider.ALERT_TARGET_NOTIFICATION_TYPE);
        EasyMock.expect(m_dao.findAllTargets()).andReturn(getMockEntities());
        EasyMock.replay(m_dao);
        org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(authentication);
        org.apache.ambari.server.controller.internal.AlertTargetResourceProvider provider = createProvider(m_amc);
        java.util.Set<org.apache.ambari.server.controller.spi.Resource> results = provider.getResources(request, null);
        junit.framework.Assert.assertEquals(1, results.size());
        org.apache.ambari.server.controller.spi.Resource resource = results.iterator().next();
        org.junit.Assert.assertEquals(org.apache.ambari.server.controller.internal.AlertTargetResourceProviderTest.ALERT_TARGET_NAME, resource.getPropertyValue(org.apache.ambari.server.controller.internal.AlertTargetResourceProvider.ALERT_TARGET_NAME));
        java.util.Map<java.lang.String, java.lang.String> properties = ((java.util.Map<java.lang.String, java.lang.String>) (resource.getPropertyValue(org.apache.ambari.server.controller.internal.AlertTargetResourceProvider.ALERT_TARGET_PROPERTIES)));
        java.util.Collection<java.lang.String> alertStates = ((java.util.Collection<java.lang.String>) (resource.getPropertyValue(org.apache.ambari.server.controller.internal.AlertTargetResourceProvider.ALERT_TARGET_STATES)));
        junit.framework.Assert.assertNull(properties);
        junit.framework.Assert.assertNull(alertStates);
        junit.framework.Assert.assertNull(resource.getPropertyValue(org.apache.ambari.server.controller.internal.AlertTargetResourceProvider.ALERT_TARGET_GLOBAL));
        EasyMock.verify(m_dao);
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

    @org.junit.Test
    public void testGetSingleResourceAsViewUser() throws java.lang.Exception {
        testGetSingleResource(org.apache.ambari.server.security.TestAuthenticationFactory.createViewUser(99L));
    }

    @java.lang.SuppressWarnings("unchecked")
    private void testGetSingleResource(org.springframework.security.core.Authentication authentication) throws java.lang.Exception {
        org.apache.ambari.server.controller.spi.Request request = org.apache.ambari.server.controller.utilities.PropertyHelper.getReadRequest(org.apache.ambari.server.controller.internal.AlertTargetResourceProvider.ALERT_TARGET_DESCRIPTION, org.apache.ambari.server.controller.internal.AlertTargetResourceProvider.ALERT_TARGET_ID, org.apache.ambari.server.controller.internal.AlertTargetResourceProvider.ALERT_TARGET_NAME, org.apache.ambari.server.controller.internal.AlertTargetResourceProvider.ALERT_TARGET_NOTIFICATION_TYPE, org.apache.ambari.server.controller.internal.AlertTargetResourceProvider.ALERT_TARGET_STATES, org.apache.ambari.server.controller.internal.AlertTargetResourceProvider.ALERT_TARGET_GLOBAL);
        org.apache.ambari.server.controller.spi.Predicate predicate = new org.apache.ambari.server.controller.utilities.PredicateBuilder().property(org.apache.ambari.server.controller.internal.AlertTargetResourceProvider.ALERT_TARGET_ID).equals(org.apache.ambari.server.controller.internal.AlertTargetResourceProviderTest.ALERT_TARGET_ID.toString()).toPredicate();
        EasyMock.expect(m_dao.findTargetById(org.apache.ambari.server.controller.internal.AlertTargetResourceProviderTest.ALERT_TARGET_ID.longValue())).andReturn(getMockEntities().get(0)).atLeastOnce();
        EasyMock.replay(m_amc, m_dao);
        org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(authentication);
        org.apache.ambari.server.controller.internal.AlertTargetResourceProvider provider = createProvider(m_amc);
        java.util.Set<org.apache.ambari.server.controller.spi.Resource> results = provider.getResources(request, predicate);
        junit.framework.Assert.assertEquals(1, results.size());
        org.apache.ambari.server.controller.spi.Resource resource = results.iterator().next();
        org.junit.Assert.assertEquals(org.apache.ambari.server.controller.internal.AlertTargetResourceProviderTest.ALERT_TARGET_ID, resource.getPropertyValue(org.apache.ambari.server.controller.internal.AlertTargetResourceProvider.ALERT_TARGET_ID));
        org.junit.Assert.assertEquals(org.apache.ambari.server.controller.internal.AlertTargetResourceProviderTest.ALERT_TARGET_NAME, resource.getPropertyValue(org.apache.ambari.server.controller.internal.AlertTargetResourceProvider.ALERT_TARGET_NAME));
        java.util.Collection<java.lang.String> alertStates = ((java.util.Collection<java.lang.String>) (resource.getPropertyValue(org.apache.ambari.server.controller.internal.AlertTargetResourceProvider.ALERT_TARGET_STATES)));
        org.junit.Assert.assertNotNull(alertStates);
        org.junit.Assert.assertEquals(2, alertStates.size());
        org.junit.Assert.assertTrue(alertStates.contains(org.apache.ambari.server.state.AlertState.CRITICAL));
        org.junit.Assert.assertTrue(alertStates.contains(org.apache.ambari.server.state.AlertState.WARNING));
        java.util.Map<java.lang.String, java.lang.String> properties = ((java.util.Map<java.lang.String, java.lang.String>) (resource.getPropertyValue(org.apache.ambari.server.controller.internal.AlertTargetResourceProvider.ALERT_TARGET_PROPERTIES)));
        junit.framework.Assert.assertNull(properties);
        junit.framework.Assert.assertEquals(java.lang.Boolean.FALSE, resource.getPropertyValue(org.apache.ambari.server.controller.internal.AlertTargetResourceProvider.ALERT_TARGET_GLOBAL));
        request = org.apache.ambari.server.controller.utilities.PropertyHelper.getReadRequest();
        results = provider.getResources(request, predicate);
        junit.framework.Assert.assertEquals(1, results.size());
        resource = results.iterator().next();
        properties = ((java.util.Map<java.lang.String, java.lang.String>) (resource.getPropertyValue(org.apache.ambari.server.controller.internal.AlertTargetResourceProvider.ALERT_TARGET_PROPERTIES)));
        junit.framework.Assert.assertEquals("bar", properties.get("foo"));
        junit.framework.Assert.assertEquals("baz", properties.get("foobar"));
        EasyMock.verify(m_amc, m_dao);
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

    private void testCreateResources(org.springframework.security.core.Authentication authentication) throws java.lang.Exception {
        EasyMock.expect(m_dao.findTargetByName(org.apache.ambari.server.controller.internal.AlertTargetResourceProviderTest.ALERT_TARGET_NAME)).andReturn(null).atLeastOnce();
        org.easymock.Capture<org.apache.ambari.server.orm.entities.AlertTargetEntity> targetCapture = EasyMock.newCapture();
        m_dao.create(EasyMock.capture(targetCapture));
        EasyMock.expectLastCall();
        EasyMock.replay(m_amc, m_dao);
        org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(authentication);
        org.apache.ambari.server.controller.internal.AlertTargetResourceProvider provider = createProvider(m_amc);
        java.util.Map<java.lang.String, java.lang.Object> requestProps = getCreationProperties();
        org.apache.ambari.server.controller.spi.Request request = org.apache.ambari.server.controller.utilities.PropertyHelper.getCreateRequest(java.util.Collections.singleton(requestProps), null);
        provider.createResources(request);
        org.junit.Assert.assertTrue(targetCapture.hasCaptured());
        org.apache.ambari.server.orm.entities.AlertTargetEntity entity = targetCapture.getValue();
        org.junit.Assert.assertNotNull(entity);
        junit.framework.Assert.assertEquals(org.apache.ambari.server.controller.internal.AlertTargetResourceProviderTest.ALERT_TARGET_NAME, entity.getTargetName());
        junit.framework.Assert.assertEquals(org.apache.ambari.server.controller.internal.AlertTargetResourceProviderTest.ALERT_TARGET_DESC, entity.getDescription());
        junit.framework.Assert.assertEquals(org.apache.ambari.server.controller.internal.AlertTargetResourceProviderTest.ALERT_TARGET_TYPE, entity.getNotificationType());
        junit.framework.Assert.assertTrue(org.apache.ambari.server.utils.CollectionPresentationUtils.isJsonsEquals(org.apache.ambari.server.controller.internal.AlertTargetResourceProviderTest.ALERT_TARGET_PROPS, entity.getProperties()));
        junit.framework.Assert.assertEquals(false, entity.isGlobal());
        junit.framework.Assert.assertNotNull(entity.getAlertStates());
        junit.framework.Assert.assertEquals(java.util.EnumSet.allOf(org.apache.ambari.server.state.AlertState.class), entity.getAlertStates());
        EasyMock.verify(m_amc, m_dao);
    }

    @org.junit.Test
    public void testCreateResourcesWithGroupsAsAdministrator() throws java.lang.Exception {
        testCreateResourcesWithGroups(org.apache.ambari.server.security.TestAuthenticationFactory.createAdministrator());
    }

    @org.junit.Test
    public void testCreateResourcesWithGroupsAsClusterAdministrator() throws java.lang.Exception {
        testCreateResourcesWithGroups(org.apache.ambari.server.security.TestAuthenticationFactory.createClusterAdministrator());
    }

    @org.junit.Test(expected = org.apache.ambari.server.security.authorization.AuthorizationException.class)
    public void testCreateResourcesWithGroupsAsServiceAdministrator() throws java.lang.Exception {
        testCreateResourcesWithGroups(org.apache.ambari.server.security.TestAuthenticationFactory.createServiceAdministrator());
    }

    @org.junit.Test(expected = org.apache.ambari.server.security.authorization.AuthorizationException.class)
    public void testCreateResourcesWithGroupsAsClusterUser() throws java.lang.Exception {
        testCreateResourcesWithGroups(org.apache.ambari.server.security.TestAuthenticationFactory.createClusterUser());
    }

    @org.junit.Test(expected = org.apache.ambari.server.security.authorization.AuthorizationException.class)
    public void testCreateResourcesWithGroupsAsViewUser() throws java.lang.Exception {
        testCreateResourcesWithGroups(org.apache.ambari.server.security.TestAuthenticationFactory.createViewUser(99L));
    }

    private void testCreateResourcesWithGroups(org.springframework.security.core.Authentication authentication) throws java.lang.Exception {
        java.util.List<java.lang.Long> groupIds = java.util.Arrays.asList(1L, 2L, 3L);
        java.util.List<org.apache.ambari.server.orm.entities.AlertGroupEntity> groups = new java.util.ArrayList<>();
        org.apache.ambari.server.orm.entities.AlertGroupEntity group1 = new org.apache.ambari.server.orm.entities.AlertGroupEntity();
        org.apache.ambari.server.orm.entities.AlertGroupEntity group2 = new org.apache.ambari.server.orm.entities.AlertGroupEntity();
        org.apache.ambari.server.orm.entities.AlertGroupEntity group3 = new org.apache.ambari.server.orm.entities.AlertGroupEntity();
        group1.setGroupId(1L);
        group2.setGroupId(2L);
        group3.setGroupId(3L);
        groups.addAll(java.util.Arrays.asList(group1, group2, group3));
        EasyMock.expect(m_dao.findTargetByName(org.apache.ambari.server.controller.internal.AlertTargetResourceProviderTest.ALERT_TARGET_NAME)).andReturn(null).atLeastOnce();
        EasyMock.expect(m_dao.findGroupsById(groupIds)).andReturn(groups).once();
        org.easymock.Capture<org.apache.ambari.server.orm.entities.AlertTargetEntity> targetCapture = org.easymock.EasyMock.newCapture();
        m_dao.create(EasyMock.capture(targetCapture));
        EasyMock.expectLastCall();
        EasyMock.replay(m_amc, m_dao);
        org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(authentication);
        org.apache.ambari.server.controller.internal.AlertTargetResourceProvider provider = createProvider(m_amc);
        java.util.Map<java.lang.String, java.lang.Object> requestProps = getCreationProperties();
        requestProps.put(org.apache.ambari.server.controller.internal.AlertTargetResourceProvider.ALERT_TARGET_GROUPS, groupIds);
        org.apache.ambari.server.controller.spi.Request request = org.apache.ambari.server.controller.utilities.PropertyHelper.getCreateRequest(java.util.Collections.singleton(requestProps), null);
        provider.createResources(request);
        org.junit.Assert.assertTrue(targetCapture.hasCaptured());
        org.apache.ambari.server.orm.entities.AlertTargetEntity entity = targetCapture.getValue();
        org.junit.Assert.assertNotNull(entity);
        junit.framework.Assert.assertEquals(org.apache.ambari.server.controller.internal.AlertTargetResourceProviderTest.ALERT_TARGET_NAME, entity.getTargetName());
        junit.framework.Assert.assertEquals(org.apache.ambari.server.controller.internal.AlertTargetResourceProviderTest.ALERT_TARGET_DESC, entity.getDescription());
        junit.framework.Assert.assertEquals(org.apache.ambari.server.controller.internal.AlertTargetResourceProviderTest.ALERT_TARGET_TYPE, entity.getNotificationType());
        junit.framework.Assert.assertTrue(org.apache.ambari.server.utils.CollectionPresentationUtils.isJsonsEquals(org.apache.ambari.server.controller.internal.AlertTargetResourceProviderTest.ALERT_TARGET_PROPS, entity.getProperties()));
        junit.framework.Assert.assertEquals(false, entity.isGlobal());
        junit.framework.Assert.assertEquals(3, entity.getAlertGroups().size());
        junit.framework.Assert.assertNotNull(entity.getAlertStates());
        junit.framework.Assert.assertEquals(java.util.EnumSet.allOf(org.apache.ambari.server.state.AlertState.class), entity.getAlertStates());
        EasyMock.verify(m_amc, m_dao);
    }

    @org.junit.Test
    public void testCreateGlobalTargetAsAdministrator() throws java.lang.Exception {
        testCreateGlobalTarget(org.apache.ambari.server.security.TestAuthenticationFactory.createAdministrator());
    }

    @org.junit.Test
    public void testCreateGlobalTargetAsClusterAdministrator() throws java.lang.Exception {
        testCreateGlobalTarget(org.apache.ambari.server.security.TestAuthenticationFactory.createClusterAdministrator());
    }

    @org.junit.Test(expected = org.apache.ambari.server.security.authorization.AuthorizationException.class)
    public void testCreateGlobalTargetAsServiceAdministrator() throws java.lang.Exception {
        testCreateGlobalTarget(org.apache.ambari.server.security.TestAuthenticationFactory.createServiceAdministrator());
    }

    @org.junit.Test(expected = org.apache.ambari.server.security.authorization.AuthorizationException.class)
    public void testCreateGlobalTargetAsClusterUser() throws java.lang.Exception {
        testCreateGlobalTarget(org.apache.ambari.server.security.TestAuthenticationFactory.createClusterUser());
    }

    @org.junit.Test(expected = org.apache.ambari.server.security.authorization.AuthorizationException.class)
    public void testCreateGlobalTargetAsViewUser() throws java.lang.Exception {
        testCreateGlobalTarget(org.apache.ambari.server.security.TestAuthenticationFactory.createViewUser(99L));
    }

    private void testCreateGlobalTarget(org.springframework.security.core.Authentication authentication) throws java.lang.Exception {
        EasyMock.expect(m_dao.findTargetByName(org.apache.ambari.server.controller.internal.AlertTargetResourceProviderTest.ALERT_TARGET_NAME)).andReturn(null).atLeastOnce();
        org.easymock.Capture<org.apache.ambari.server.orm.entities.AlertTargetEntity> targetCapture = org.easymock.EasyMock.newCapture();
        m_dao.create(EasyMock.capture(targetCapture));
        EasyMock.expectLastCall();
        EasyMock.replay(m_amc, m_dao);
        org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(authentication);
        org.apache.ambari.server.controller.internal.AlertTargetResourceProvider provider = createProvider(m_amc);
        java.util.Map<java.lang.String, java.lang.Object> requestProps = getCreationProperties();
        requestProps.put(org.apache.ambari.server.controller.internal.AlertTargetResourceProvider.ALERT_TARGET_GLOBAL, "true");
        org.apache.ambari.server.controller.spi.Request request = org.apache.ambari.server.controller.utilities.PropertyHelper.getCreateRequest(java.util.Collections.singleton(requestProps), null);
        provider.createResources(request);
        org.junit.Assert.assertTrue(targetCapture.hasCaptured());
        org.apache.ambari.server.orm.entities.AlertTargetEntity entity = targetCapture.getValue();
        org.junit.Assert.assertNotNull(entity);
        junit.framework.Assert.assertEquals(org.apache.ambari.server.controller.internal.AlertTargetResourceProviderTest.ALERT_TARGET_NAME, entity.getTargetName());
        junit.framework.Assert.assertEquals(org.apache.ambari.server.controller.internal.AlertTargetResourceProviderTest.ALERT_TARGET_DESC, entity.getDescription());
        junit.framework.Assert.assertEquals(org.apache.ambari.server.controller.internal.AlertTargetResourceProviderTest.ALERT_TARGET_TYPE, entity.getNotificationType());
        junit.framework.Assert.assertTrue(org.apache.ambari.server.utils.CollectionPresentationUtils.isJsonsEquals(org.apache.ambari.server.controller.internal.AlertTargetResourceProviderTest.ALERT_TARGET_PROPS, entity.getProperties()));
        junit.framework.Assert.assertEquals(true, entity.isGlobal());
        junit.framework.Assert.assertNotNull(entity.getAlertStates());
        junit.framework.Assert.assertEquals(java.util.EnumSet.allOf(org.apache.ambari.server.state.AlertState.class), entity.getAlertStates());
        EasyMock.verify(m_amc, m_dao);
    }

    @org.junit.Test
    public void testCreateResourceWithRecipientArrayAsAdministrator() throws java.lang.Exception {
        testCreateResourceWithRecipientArray(org.apache.ambari.server.security.TestAuthenticationFactory.createAdministrator());
    }

    @org.junit.Test
    public void testCreateResourceWithRecipientArrayAsClusterAdministrator() throws java.lang.Exception {
        testCreateResourceWithRecipientArray(org.apache.ambari.server.security.TestAuthenticationFactory.createClusterAdministrator());
    }

    @org.junit.Test(expected = org.apache.ambari.server.security.authorization.AuthorizationException.class)
    public void testCreateResourceWithRecipientArrayAsServiceAdministrator() throws java.lang.Exception {
        testCreateResourceWithRecipientArray(org.apache.ambari.server.security.TestAuthenticationFactory.createServiceAdministrator());
    }

    @org.junit.Test(expected = org.apache.ambari.server.security.authorization.AuthorizationException.class)
    public void testCreateResourceWithRecipientArrayAsClusterUser() throws java.lang.Exception {
        testCreateResourceWithRecipientArray(org.apache.ambari.server.security.TestAuthenticationFactory.createClusterUser());
    }

    @org.junit.Test(expected = org.apache.ambari.server.security.authorization.AuthorizationException.class)
    public void testCreateResourcesWithRecipientArrayAsViewUser() throws java.lang.Exception {
        testCreateResourceWithRecipientArray(org.apache.ambari.server.security.TestAuthenticationFactory.createViewUser(99L));
    }

    private void testCreateResourceWithRecipientArray(org.springframework.security.core.Authentication authentication) throws java.lang.Exception {
        EasyMock.expect(m_dao.findTargetByName(org.apache.ambari.server.controller.internal.AlertTargetResourceProviderTest.ALERT_TARGET_NAME)).andReturn(null).atLeastOnce();
        org.easymock.Capture<org.apache.ambari.server.orm.entities.AlertTargetEntity> targetCapture = org.easymock.EasyMock.newCapture();
        m_dao.create(EasyMock.capture(targetCapture));
        EasyMock.expectLastCall();
        EasyMock.replay(m_amc, m_dao);
        org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(authentication);
        org.apache.ambari.server.controller.internal.AlertTargetResourceProvider provider = createProvider(m_amc);
        java.util.Map<java.lang.String, java.lang.Object> requestProps = getRecipientCreationProperties();
        org.apache.ambari.server.controller.spi.Request request = org.apache.ambari.server.controller.utilities.PropertyHelper.getCreateRequest(java.util.Collections.singleton(requestProps), null);
        provider.createResources(request);
        org.junit.Assert.assertTrue(targetCapture.hasCaptured());
        org.apache.ambari.server.orm.entities.AlertTargetEntity entity = targetCapture.getValue();
        org.junit.Assert.assertNotNull(entity);
        junit.framework.Assert.assertEquals(org.apache.ambari.server.controller.internal.AlertTargetResourceProviderTest.ALERT_TARGET_NAME, entity.getTargetName());
        junit.framework.Assert.assertEquals(org.apache.ambari.server.controller.internal.AlertTargetResourceProviderTest.ALERT_TARGET_DESC, entity.getDescription());
        junit.framework.Assert.assertEquals(org.apache.ambari.server.controller.internal.AlertTargetResourceProviderTest.ALERT_TARGET_TYPE, entity.getNotificationType());
        junit.framework.Assert.assertEquals("{\"ambari.dispatch.recipients\":\"[\\\"ambari@ambari.apache.org\\\"]\"}", entity.getProperties());
        junit.framework.Assert.assertNotNull(entity.getAlertStates());
        junit.framework.Assert.assertEquals(java.util.EnumSet.allOf(org.apache.ambari.server.state.AlertState.class), entity.getAlertStates());
        EasyMock.verify(m_amc, m_dao);
    }

    @org.junit.Test
    public void testCreateResourceWithAlertStatesAsAdministrator() throws java.lang.Exception {
        testCreateResourceWithAlertStates(org.apache.ambari.server.security.TestAuthenticationFactory.createAdministrator());
    }

    @org.junit.Test
    public void testCreateResourceWithAlertStatesAsClusterAdministrator() throws java.lang.Exception {
        testCreateResourceWithAlertStates(org.apache.ambari.server.security.TestAuthenticationFactory.createClusterAdministrator());
    }

    @org.junit.Test(expected = org.apache.ambari.server.security.authorization.AuthorizationException.class)
    public void testCreateResourceWithAlertStatesAsServiceAdministrator() throws java.lang.Exception {
        testCreateResourceWithAlertStates(org.apache.ambari.server.security.TestAuthenticationFactory.createServiceAdministrator());
    }

    @org.junit.Test(expected = org.apache.ambari.server.security.authorization.AuthorizationException.class)
    public void testCreateResourceWithAlertStatesAsClusterUser() throws java.lang.Exception {
        testCreateResourceWithAlertStates(org.apache.ambari.server.security.TestAuthenticationFactory.createClusterUser());
    }

    @org.junit.Test(expected = org.apache.ambari.server.security.authorization.AuthorizationException.class)
    public void testCreateResourceWithAlertStatesAsViewUser() throws java.lang.Exception {
        testCreateResourceWithAlertStates(org.apache.ambari.server.security.TestAuthenticationFactory.createViewUser(99L));
    }

    private void testCreateResourceWithAlertStates(org.springframework.security.core.Authentication authentication) throws java.lang.Exception {
        EasyMock.expect(m_dao.findTargetByName(org.apache.ambari.server.controller.internal.AlertTargetResourceProviderTest.ALERT_TARGET_NAME)).andReturn(null).atLeastOnce();
        org.easymock.Capture<org.apache.ambari.server.orm.entities.AlertTargetEntity> targetCapture = org.easymock.EasyMock.newCapture();
        m_dao.create(EasyMock.capture(targetCapture));
        EasyMock.expectLastCall();
        EasyMock.replay(m_amc, m_dao);
        org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(authentication);
        org.apache.ambari.server.controller.internal.AlertTargetResourceProvider provider = createProvider(m_amc);
        java.util.Map<java.lang.String, java.lang.Object> requestProps = getCreationProperties();
        requestProps.put(org.apache.ambari.server.controller.internal.AlertTargetResourceProvider.ALERT_TARGET_STATES, new java.util.ArrayList<>(java.util.Arrays.asList(org.apache.ambari.server.state.AlertState.OK.name(), org.apache.ambari.server.state.AlertState.UNKNOWN.name())));
        org.apache.ambari.server.controller.spi.Request request = org.apache.ambari.server.controller.utilities.PropertyHelper.getCreateRequest(java.util.Collections.singleton(requestProps), null);
        provider.createResources(request);
        org.junit.Assert.assertTrue(targetCapture.hasCaptured());
        org.apache.ambari.server.orm.entities.AlertTargetEntity entity = targetCapture.getValue();
        org.junit.Assert.assertNotNull(entity);
        junit.framework.Assert.assertEquals(org.apache.ambari.server.controller.internal.AlertTargetResourceProviderTest.ALERT_TARGET_NAME, entity.getTargetName());
        junit.framework.Assert.assertEquals(org.apache.ambari.server.controller.internal.AlertTargetResourceProviderTest.ALERT_TARGET_DESC, entity.getDescription());
        junit.framework.Assert.assertEquals(org.apache.ambari.server.controller.internal.AlertTargetResourceProviderTest.ALERT_TARGET_TYPE, entity.getNotificationType());
        junit.framework.Assert.assertTrue(org.apache.ambari.server.utils.CollectionPresentationUtils.isJsonsEquals(org.apache.ambari.server.controller.internal.AlertTargetResourceProviderTest.ALERT_TARGET_PROPS, entity.getProperties()));
        java.util.Set<org.apache.ambari.server.state.AlertState> alertStates = entity.getAlertStates();
        junit.framework.Assert.assertNotNull(alertStates);
        junit.framework.Assert.assertEquals(2, alertStates.size());
        junit.framework.Assert.assertTrue(alertStates.contains(org.apache.ambari.server.state.AlertState.OK));
        junit.framework.Assert.assertTrue(alertStates.contains(org.apache.ambari.server.state.AlertState.UNKNOWN));
        EasyMock.verify(m_amc, m_dao);
    }

    @org.junit.Test
    public void testUpdateResourcesAsAdministrator() throws java.lang.Exception {
        testUpdateResources(org.apache.ambari.server.security.TestAuthenticationFactory.createAdministrator());
    }

    @org.junit.Test
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
        EasyMock.expect(m_dao.findTargetByName(org.apache.ambari.server.controller.internal.AlertTargetResourceProviderTest.ALERT_TARGET_NAME)).andReturn(null).atLeastOnce();
        org.easymock.Capture<org.apache.ambari.server.orm.entities.AlertTargetEntity> entityCapture = org.easymock.EasyMock.newCapture();
        m_dao.create(EasyMock.capture(entityCapture));
        EasyMock.expectLastCall().times(1);
        org.apache.ambari.server.orm.entities.AlertTargetEntity target = new org.apache.ambari.server.orm.entities.AlertTargetEntity();
        EasyMock.expect(m_dao.findTargetById(org.apache.ambari.server.controller.internal.AlertTargetResourceProviderTest.ALERT_TARGET_ID)).andReturn(target).times(1);
        EasyMock.expect(m_dao.merge(EasyMock.capture(entityCapture))).andReturn(target).once();
        EasyMock.replay(m_amc, m_dao);
        org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(authentication);
        org.apache.ambari.server.controller.internal.AlertTargetResourceProvider provider = createProvider(m_amc);
        java.util.Map<java.lang.String, java.lang.Object> requestProps = getCreationProperties();
        org.apache.ambari.server.controller.spi.Request request = org.apache.ambari.server.controller.utilities.PropertyHelper.getCreateRequest(java.util.Collections.singleton(requestProps), null);
        provider.createResources(request);
        requestProps = new java.util.HashMap<>();
        requestProps.put(org.apache.ambari.server.controller.internal.AlertTargetResourceProvider.ALERT_TARGET_ID, org.apache.ambari.server.controller.internal.AlertTargetResourceProviderTest.ALERT_TARGET_ID.toString());
        java.lang.String newName = org.apache.ambari.server.controller.internal.AlertTargetResourceProviderTest.ALERT_TARGET_NAME + " Foo";
        requestProps.put(org.apache.ambari.server.controller.internal.AlertTargetResourceProvider.ALERT_TARGET_NAME, newName);
        requestProps.put(org.apache.ambari.server.controller.internal.AlertTargetResourceProvider.ALERT_TARGET_PROPERTIES + "/foobar", "baz");
        org.apache.ambari.server.controller.spi.Predicate predicate = new org.apache.ambari.server.controller.utilities.PredicateBuilder().property(org.apache.ambari.server.controller.internal.AlertTargetResourceProvider.ALERT_TARGET_ID).equals(org.apache.ambari.server.controller.internal.AlertTargetResourceProviderTest.ALERT_TARGET_ID.toString()).toPredicate();
        request = org.apache.ambari.server.controller.utilities.PropertyHelper.getUpdateRequest(requestProps, null);
        provider.updateResources(request, predicate);
        junit.framework.Assert.assertTrue(entityCapture.hasCaptured());
        org.apache.ambari.server.orm.entities.AlertTargetEntity entity = entityCapture.getValue();
        junit.framework.Assert.assertEquals(newName, entity.getTargetName());
        junit.framework.Assert.assertEquals(org.apache.ambari.server.controller.internal.AlertTargetResourceProviderTest.ALERT_TARGET_PROPS2, entity.getProperties());
        EasyMock.verify(m_amc, m_dao);
    }

    @org.junit.Test
    public void testUpdateResourcesWithGroupsAsAdministrator() throws java.lang.Exception {
        testUpdateResourcesWithGroups(org.apache.ambari.server.security.TestAuthenticationFactory.createAdministrator());
    }

    @org.junit.Test
    public void testUpdateResourcesWithGroupsAsClusterAdministrator() throws java.lang.Exception {
        testUpdateResourcesWithGroups(org.apache.ambari.server.security.TestAuthenticationFactory.createClusterAdministrator());
    }

    @org.junit.Test(expected = org.apache.ambari.server.security.authorization.AuthorizationException.class)
    public void testUpdateResourcesWithGroupsAsServiceAdministrator() throws java.lang.Exception {
        testUpdateResourcesWithGroups(org.apache.ambari.server.security.TestAuthenticationFactory.createServiceAdministrator());
    }

    @org.junit.Test(expected = org.apache.ambari.server.security.authorization.AuthorizationException.class)
    public void testUpdateResourcesWithGroupsAsClusterUser() throws java.lang.Exception {
        testUpdateResourcesWithGroups(org.apache.ambari.server.security.TestAuthenticationFactory.createClusterUser());
    }

    @org.junit.Test(expected = org.apache.ambari.server.security.authorization.AuthorizationException.class)
    public void testUpdateResourcesWithGroupsAsViewUser() throws java.lang.Exception {
        testUpdateResourcesWithGroups(org.apache.ambari.server.security.TestAuthenticationFactory.createViewUser(99L));
    }

    private void testUpdateResourcesWithGroups(org.springframework.security.core.Authentication authentication) throws java.lang.Exception {
        EasyMock.expect(m_dao.findTargetByName(org.apache.ambari.server.controller.internal.AlertTargetResourceProviderTest.ALERT_TARGET_NAME)).andReturn(null).atLeastOnce();
        org.easymock.Capture<org.apache.ambari.server.orm.entities.AlertTargetEntity> entityCapture = org.easymock.EasyMock.newCapture();
        m_dao.create(EasyMock.capture(entityCapture));
        EasyMock.expectLastCall().times(1);
        org.apache.ambari.server.orm.entities.AlertTargetEntity target = new org.apache.ambari.server.orm.entities.AlertTargetEntity();
        EasyMock.expect(m_dao.findTargetById(org.apache.ambari.server.controller.internal.AlertTargetResourceProviderTest.ALERT_TARGET_ID)).andReturn(target).times(1);
        java.util.List<java.lang.Long> groupIds = java.util.Arrays.asList(1L, 2L, 3L);
        java.util.List<org.apache.ambari.server.orm.entities.AlertGroupEntity> groups = new java.util.ArrayList<>();
        org.apache.ambari.server.orm.entities.AlertGroupEntity group1 = new org.apache.ambari.server.orm.entities.AlertGroupEntity();
        org.apache.ambari.server.orm.entities.AlertGroupEntity group2 = new org.apache.ambari.server.orm.entities.AlertGroupEntity();
        org.apache.ambari.server.orm.entities.AlertGroupEntity group3 = new org.apache.ambari.server.orm.entities.AlertGroupEntity();
        group1.setGroupId(1L);
        group2.setGroupId(2L);
        group3.setGroupId(3L);
        groups.addAll(java.util.Arrays.asList(group1, group2, group3));
        EasyMock.expect(m_dao.findGroupsById(org.easymock.EasyMock.eq(groupIds))).andReturn(groups).once();
        EasyMock.expect(m_dao.merge(EasyMock.capture(entityCapture))).andReturn(target).once();
        EasyMock.replay(m_amc, m_dao);
        org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(authentication);
        org.apache.ambari.server.controller.internal.AlertTargetResourceProvider provider = createProvider(m_amc);
        java.util.Map<java.lang.String, java.lang.Object> requestProps = getCreationProperties();
        org.apache.ambari.server.controller.spi.Request request = org.apache.ambari.server.controller.utilities.PropertyHelper.getCreateRequest(java.util.Collections.singleton(requestProps), null);
        provider.createResources(request);
        requestProps = new java.util.HashMap<>();
        requestProps.put(org.apache.ambari.server.controller.internal.AlertTargetResourceProvider.ALERT_TARGET_ID, org.apache.ambari.server.controller.internal.AlertTargetResourceProviderTest.ALERT_TARGET_ID.toString());
        requestProps.put(org.apache.ambari.server.controller.internal.AlertTargetResourceProvider.ALERT_TARGET_GROUPS, groupIds);
        org.apache.ambari.server.controller.spi.Predicate predicate = new org.apache.ambari.server.controller.utilities.PredicateBuilder().property(org.apache.ambari.server.controller.internal.AlertTargetResourceProvider.ALERT_TARGET_ID).equals(org.apache.ambari.server.controller.internal.AlertTargetResourceProviderTest.ALERT_TARGET_ID.toString()).toPredicate();
        request = org.apache.ambari.server.controller.utilities.PropertyHelper.getUpdateRequest(requestProps, null);
        provider.updateResources(request, predicate);
        junit.framework.Assert.assertTrue(entityCapture.hasCaptured());
        org.apache.ambari.server.orm.entities.AlertTargetEntity entity = entityCapture.getValue();
        junit.framework.Assert.assertEquals(3, entity.getAlertGroups().size());
        EasyMock.verify(m_amc, m_dao);
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
        EasyMock.expect(m_dao.findTargetByName(org.apache.ambari.server.controller.internal.AlertTargetResourceProviderTest.ALERT_TARGET_NAME)).andReturn(null).atLeastOnce();
        org.easymock.Capture<org.apache.ambari.server.orm.entities.AlertTargetEntity> entityCapture = org.easymock.EasyMock.newCapture();
        m_dao.create(EasyMock.capture(entityCapture));
        EasyMock.expectLastCall().times(1);
        EasyMock.replay(m_amc, m_dao);
        org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(authentication);
        org.apache.ambari.server.controller.internal.AlertTargetResourceProvider provider = createProvider(m_amc);
        java.util.Map<java.lang.String, java.lang.Object> requestProps = getCreationProperties();
        org.apache.ambari.server.controller.spi.Request request = org.apache.ambari.server.controller.utilities.PropertyHelper.getCreateRequest(java.util.Collections.singleton(requestProps), null);
        provider.createResources(request);
        org.junit.Assert.assertTrue(entityCapture.hasCaptured());
        org.apache.ambari.server.orm.entities.AlertTargetEntity entity = entityCapture.getValue();
        org.junit.Assert.assertNotNull(entity);
        org.apache.ambari.server.controller.spi.Predicate p = new org.apache.ambari.server.controller.utilities.PredicateBuilder().property(org.apache.ambari.server.controller.internal.AlertTargetResourceProvider.ALERT_TARGET_ID).equals(org.apache.ambari.server.controller.internal.AlertTargetResourceProviderTest.ALERT_TARGET_ID.toString()).toPredicate();
        entity.setTargetId(org.apache.ambari.server.controller.internal.AlertTargetResourceProviderTest.ALERT_TARGET_ID);
        EasyMock.resetToStrict(m_dao);
        EasyMock.expect(m_dao.findTargetById(org.apache.ambari.server.controller.internal.AlertTargetResourceProviderTest.ALERT_TARGET_ID.longValue())).andReturn(entity).anyTimes();
        m_dao.remove(EasyMock.capture(entityCapture));
        EasyMock.expectLastCall();
        EasyMock.replay(m_dao);
        provider.deleteResources(new org.apache.ambari.server.controller.internal.RequestImpl(null, null, null, null), p);
        org.apache.ambari.server.orm.entities.AlertTargetEntity entity1 = entityCapture.getValue();
        org.junit.Assert.assertEquals(org.apache.ambari.server.controller.internal.AlertTargetResourceProviderTest.ALERT_TARGET_ID, entity1.getTargetId());
        EasyMock.verify(m_amc, m_dao);
    }

    @org.junit.Test
    public void testDuplicateDirectiveAsClusterAdministrator() throws java.lang.Exception {
        testDuplicate(org.apache.ambari.server.security.TestAuthenticationFactory.createClusterAdministrator());
    }

    @org.junit.Test(expected = org.apache.ambari.server.security.authorization.AuthorizationException.class)
    public void testDuplicateDirectiveAsServiceAdministrator() throws java.lang.Exception {
        testDuplicate(org.apache.ambari.server.security.TestAuthenticationFactory.createServiceAdministrator());
    }

    @org.junit.Test(expected = org.apache.ambari.server.security.authorization.AuthorizationException.class)
    public void testDuplicateDirectiveAsClusterUser() throws java.lang.Exception {
        testDuplicate(org.apache.ambari.server.security.TestAuthenticationFactory.createClusterUser());
    }

    @org.junit.Test(expected = org.apache.ambari.server.security.authorization.AuthorizationException.class)
    public void testDuplicateDirectiveAsViewUser() throws java.lang.Exception {
        testDuplicate(org.apache.ambari.server.security.TestAuthenticationFactory.createViewUser(99L));
    }

    @org.junit.Test
    public void testDuplicateDirectiveAsAdministrator() throws java.lang.Exception {
        testDuplicate(org.apache.ambari.server.security.TestAuthenticationFactory.createAdministrator());
    }

    public void testDuplicate(org.springframework.security.core.Authentication authentication) throws java.lang.Exception {
        org.apache.ambari.server.orm.entities.AlertTargetEntity entity = getMockEntities().get(0);
        EasyMock.expect(m_dao.findTargetByName(org.apache.ambari.server.controller.internal.AlertTargetResourceProviderTest.ALERT_TARGET_NAME)).andReturn(entity).atLeastOnce();
        EasyMock.replay(m_amc, m_dao);
        org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(authentication);
        org.apache.ambari.server.controller.internal.AlertTargetResourceProvider provider = createProvider(m_amc);
        java.util.Map<java.lang.String, java.lang.Object> requestProps = getCreationProperties();
        java.util.Map<java.lang.String, java.lang.String> requestInfoProperties = new java.util.HashMap<>();
        org.apache.ambari.server.controller.spi.Request request = org.apache.ambari.server.controller.utilities.PropertyHelper.getCreateRequest(java.util.Collections.singleton(requestProps), requestInfoProperties);
        try {
            provider.createResources(request);
            org.junit.Assert.fail("IllegalArgumentException expected as duplicate not allowed");
        } catch (java.lang.IllegalArgumentException e) {
            junit.framework.Assert.assertEquals(e.getMessage(), "Alert targets already exists and can't be created");
        }
        EasyMock.verify(m_amc, m_dao);
    }

    @org.junit.Test
    public void testOverwriteDirectiveAsAdministrator() throws java.lang.Exception {
        testOverwriteDirective(org.apache.ambari.server.security.TestAuthenticationFactory.createAdministrator());
    }

    @org.junit.Test
    public void testOverwriteDirectiveAsClusterAdministrator() throws java.lang.Exception {
        testOverwriteDirective(org.apache.ambari.server.security.TestAuthenticationFactory.createClusterAdministrator());
    }

    @org.junit.Test(expected = org.apache.ambari.server.security.authorization.AuthorizationException.class)
    public void testOverwriteDirectiveAsServiceAdministrator() throws java.lang.Exception {
        testOverwriteDirective(org.apache.ambari.server.security.TestAuthenticationFactory.createServiceAdministrator());
    }

    @org.junit.Test(expected = org.apache.ambari.server.security.authorization.AuthorizationException.class)
    public void testOverwriteDirectiveAsClusterUser() throws java.lang.Exception {
        testOverwriteDirective(org.apache.ambari.server.security.TestAuthenticationFactory.createClusterUser());
    }

    @org.junit.Test(expected = org.apache.ambari.server.security.authorization.AuthorizationException.class)
    public void testOverwriteDirectiveAsViewUser() throws java.lang.Exception {
        testOverwriteDirective(org.apache.ambari.server.security.TestAuthenticationFactory.createViewUser(99L));
    }

    private void testOverwriteDirective(org.springframework.security.core.Authentication authentication) throws java.lang.Exception {
        org.apache.ambari.server.orm.entities.AlertTargetEntity entity = getMockEntities().get(0);
        EasyMock.expect(m_dao.findTargetByName(org.apache.ambari.server.controller.internal.AlertTargetResourceProviderTest.ALERT_TARGET_NAME)).andReturn(entity).atLeastOnce();
        org.easymock.Capture<org.apache.ambari.server.orm.entities.AlertTargetEntity> targetCapture = org.easymock.EasyMock.newCapture();
        EasyMock.expect(m_dao.merge(EasyMock.capture(targetCapture))).andReturn(entity).once();
        EasyMock.replay(m_amc, m_dao);
        org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(authentication);
        org.apache.ambari.server.controller.internal.AlertTargetResourceProvider provider = createProvider(m_amc);
        java.util.Map<java.lang.String, java.lang.Object> requestProps = getCreationProperties();
        java.util.Map<java.lang.String, java.lang.String> requestInfoProperties = new java.util.HashMap<>();
        requestInfoProperties.put(org.apache.ambari.server.api.resources.AlertTargetResourceDefinition.OVERWRITE_DIRECTIVE, "true");
        org.apache.ambari.server.controller.spi.Request request = org.apache.ambari.server.controller.utilities.PropertyHelper.getCreateRequest(java.util.Collections.singleton(requestProps), requestInfoProperties);
        provider.createResources(request);
        org.junit.Assert.assertTrue(targetCapture.hasCaptured());
        entity = targetCapture.getValue();
        org.junit.Assert.assertNotNull(entity);
        junit.framework.Assert.assertEquals(org.apache.ambari.server.controller.internal.AlertTargetResourceProviderTest.ALERT_TARGET_NAME, entity.getTargetName());
        junit.framework.Assert.assertEquals(org.apache.ambari.server.controller.internal.AlertTargetResourceProviderTest.ALERT_TARGET_DESC, entity.getDescription());
        junit.framework.Assert.assertEquals(org.apache.ambari.server.controller.internal.AlertTargetResourceProviderTest.ALERT_TARGET_TYPE, entity.getNotificationType());
        junit.framework.Assert.assertTrue(org.apache.ambari.server.utils.CollectionPresentationUtils.isJsonsEquals(org.apache.ambari.server.controller.internal.AlertTargetResourceProviderTest.ALERT_TARGET_PROPS, entity.getProperties()));
        junit.framework.Assert.assertEquals(false, entity.isGlobal());
        junit.framework.Assert.assertNotNull(entity.getAlertStates());
        junit.framework.Assert.assertEquals(java.util.EnumSet.allOf(org.apache.ambari.server.state.AlertState.class), entity.getAlertStates());
        EasyMock.verify(m_amc, m_dao);
    }

    @org.junit.Test
    public void testUpdateAlertTargetsWithCustomGroups() throws java.lang.Exception {
        EasyMock.expect(m_dao.findTargetByName(org.apache.ambari.server.controller.internal.AlertTargetResourceProviderTest.ALERT_TARGET_NAME)).andReturn(null).atLeastOnce();
        org.easymock.Capture<org.apache.ambari.server.orm.entities.AlertTargetEntity> entityCapture = org.easymock.EasyMock.newCapture();
        m_dao.create(EasyMock.capture(entityCapture));
        EasyMock.expectLastCall().times(1);
        org.apache.ambari.server.orm.entities.AlertTargetEntity target = new org.apache.ambari.server.orm.entities.AlertTargetEntity();
        EasyMock.expect(m_dao.findTargetById(org.apache.ambari.server.controller.internal.AlertTargetResourceProviderTest.ALERT_TARGET_ID)).andReturn(target).once();
        org.easymock.Capture<org.apache.ambari.server.orm.entities.AlertGroupEntity> groupEntityCapture = org.easymock.EasyMock.newCapture();
        java.util.List<org.apache.ambari.server.orm.entities.AlertGroupEntity> groups = getMockGroupEntities();
        java.util.List<java.lang.Long> groupIds = java.util.Arrays.asList(1L, 2L, 3L);
        EasyMock.expect(m_dao.findGroupsById(org.easymock.EasyMock.eq(groupIds))).andReturn(groups).anyTimes();
        EasyMock.expect(m_dao.findAllGroups()).andReturn(groups).anyTimes();
        for (org.apache.ambari.server.orm.entities.AlertGroupEntity group : groups) {
            EasyMock.expect(m_dao.merge(EasyMock.capture(groupEntityCapture))).andReturn(group).anyTimes();
        }
        EasyMock.expect(m_dao.merge(EasyMock.capture(entityCapture))).andReturn(target).anyTimes();
        EasyMock.replay(m_amc, m_dao);
        org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(org.apache.ambari.server.security.TestAuthenticationFactory.createAdministrator());
        org.apache.ambari.server.controller.internal.AlertTargetResourceProvider provider = createProvider(m_amc);
        java.util.Map<java.lang.String, java.lang.Object> requestProps = getCreationProperties();
        org.apache.ambari.server.controller.spi.Request request = org.apache.ambari.server.controller.utilities.PropertyHelper.getCreateRequest(java.util.Collections.singleton(requestProps), null);
        provider.createResources(request);
        requestProps = new java.util.HashMap<>();
        requestProps.put(org.apache.ambari.server.controller.internal.AlertTargetResourceProvider.ALERT_TARGET_ID, org.apache.ambari.server.controller.internal.AlertTargetResourceProviderTest.ALERT_TARGET_ID.toString());
        requestProps.put(org.apache.ambari.server.controller.internal.AlertTargetResourceProvider.ALERT_TARGET_GLOBAL, "false");
        requestProps.put(org.apache.ambari.server.controller.internal.AlertTargetResourceProvider.ALERT_TARGET_GROUPS, groupIds);
        org.apache.ambari.server.controller.spi.Predicate predicate = new org.apache.ambari.server.controller.utilities.PredicateBuilder().property(org.apache.ambari.server.controller.internal.AlertTargetResourceProvider.ALERT_TARGET_ID).equals(org.apache.ambari.server.controller.internal.AlertTargetResourceProviderTest.ALERT_TARGET_ID.toString()).toPredicate();
        request = org.apache.ambari.server.controller.utilities.PropertyHelper.getUpdateRequest(requestProps, null);
        provider.updateResources(request, predicate);
        junit.framework.Assert.assertTrue(entityCapture.hasCaptured());
        org.apache.ambari.server.orm.entities.AlertTargetEntity entity = entityCapture.getValue();
        junit.framework.Assert.assertEquals(3, entity.getAlertGroups().size());
        junit.framework.Assert.assertEquals(false, entity.isGlobal());
        EasyMock.verify(m_amc, m_dao);
    }

    @org.junit.Test
    public void testUpdateAlertTargetsWithAllGroups() throws java.lang.Exception {
        EasyMock.expect(m_dao.findTargetByName(org.apache.ambari.server.controller.internal.AlertTargetResourceProviderTest.ALERT_TARGET_NAME)).andReturn(null).atLeastOnce();
        org.easymock.Capture<org.apache.ambari.server.orm.entities.AlertTargetEntity> entityCapture = org.easymock.EasyMock.newCapture();
        m_dao.create(EasyMock.capture(entityCapture));
        EasyMock.expectLastCall().times(1);
        org.apache.ambari.server.orm.entities.AlertTargetEntity target = new org.apache.ambari.server.orm.entities.AlertTargetEntity();
        EasyMock.expect(m_dao.findTargetById(org.apache.ambari.server.controller.internal.AlertTargetResourceProviderTest.ALERT_TARGET_ID)).andReturn(target).once();
        java.util.List<org.apache.ambari.server.orm.entities.AlertGroupEntity> groups = getMockGroupEntities();
        java.util.List<java.lang.Long> groupIds = java.util.Arrays.asList(1L, 2L, 3L);
        EasyMock.expect(m_dao.findGroupsById(org.easymock.EasyMock.eq(groupIds))).andReturn(groups).anyTimes();
        EasyMock.expect(m_dao.findAllGroups()).andReturn(groups).once();
        EasyMock.expect(m_dao.merge(EasyMock.capture(entityCapture))).andReturn(target).anyTimes();
        EasyMock.replay(m_amc, m_dao);
        org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(org.apache.ambari.server.security.TestAuthenticationFactory.createAdministrator());
        org.apache.ambari.server.controller.internal.AlertTargetResourceProvider provider = createProvider(m_amc);
        java.util.Map<java.lang.String, java.lang.Object> requestProps = getCreationProperties();
        org.apache.ambari.server.controller.spi.Request request = org.apache.ambari.server.controller.utilities.PropertyHelper.getCreateRequest(java.util.Collections.singleton(requestProps), null);
        provider.createResources(request);
        requestProps = new java.util.HashMap<>();
        requestProps.put(org.apache.ambari.server.controller.internal.AlertTargetResourceProvider.ALERT_TARGET_ID, org.apache.ambari.server.controller.internal.AlertTargetResourceProviderTest.ALERT_TARGET_ID.toString());
        requestProps.put(org.apache.ambari.server.controller.internal.AlertTargetResourceProvider.ALERT_TARGET_GLOBAL, "true");
        org.apache.ambari.server.controller.spi.Predicate predicate = new org.apache.ambari.server.controller.utilities.PredicateBuilder().property(org.apache.ambari.server.controller.internal.AlertTargetResourceProvider.ALERT_TARGET_ID).equals(org.apache.ambari.server.controller.internal.AlertTargetResourceProviderTest.ALERT_TARGET_ID.toString()).toPredicate();
        request = org.apache.ambari.server.controller.utilities.PropertyHelper.getUpdateRequest(requestProps, null);
        provider.updateResources(request, predicate);
        junit.framework.Assert.assertTrue(entityCapture.hasCaptured());
        org.apache.ambari.server.orm.entities.AlertTargetEntity entity = entityCapture.getValue();
        junit.framework.Assert.assertEquals(3, entity.getAlertGroups().size());
        junit.framework.Assert.assertEquals(true, entity.isGlobal());
        EasyMock.verify(m_amc, m_dao);
    }

    @org.junit.Test
    public void testEnable() throws java.lang.Exception {
        EasyMock.expect(m_dao.findTargetByName(org.apache.ambari.server.controller.internal.AlertTargetResourceProviderTest.ALERT_TARGET_NAME)).andReturn(null).atLeastOnce();
        org.easymock.Capture<org.apache.ambari.server.orm.entities.AlertTargetEntity> entityCapture = org.easymock.EasyMock.newCapture();
        m_dao.create(EasyMock.capture(entityCapture));
        EasyMock.expectLastCall().times(1);
        org.apache.ambari.server.orm.entities.AlertTargetEntity target = new org.apache.ambari.server.orm.entities.AlertTargetEntity();
        target.setEnabled(false);
        target.setProperties("{prop1=val1}");
        EasyMock.expect(m_dao.findTargetById(org.apache.ambari.server.controller.internal.AlertTargetResourceProviderTest.ALERT_TARGET_ID)).andReturn(target).times(1);
        EasyMock.expect(m_dao.merge(EasyMock.capture(entityCapture))).andReturn(target).once();
        EasyMock.replay(m_amc, m_dao);
        org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(org.apache.ambari.server.security.TestAuthenticationFactory.createAdministrator());
        org.apache.ambari.server.controller.internal.AlertTargetResourceProvider provider = createProvider(m_amc);
        java.util.Map<java.lang.String, java.lang.Object> requestProps = getCreationProperties();
        org.apache.ambari.server.controller.spi.Request request = org.apache.ambari.server.controller.utilities.PropertyHelper.getCreateRequest(java.util.Collections.singleton(requestProps), null);
        provider.createResources(request);
        requestProps = new java.util.HashMap<>();
        requestProps.put(org.apache.ambari.server.controller.internal.AlertTargetResourceProvider.ALERT_TARGET_ID, org.apache.ambari.server.controller.internal.AlertTargetResourceProviderTest.ALERT_TARGET_ID.toString());
        requestProps.put(org.apache.ambari.server.controller.internal.AlertTargetResourceProvider.ALERT_TARGET_ENABLED, "true");
        org.apache.ambari.server.controller.spi.Predicate predicate = new org.apache.ambari.server.controller.utilities.PredicateBuilder().property(org.apache.ambari.server.controller.internal.AlertTargetResourceProvider.ALERT_TARGET_ID).equals(org.apache.ambari.server.controller.internal.AlertTargetResourceProviderTest.ALERT_TARGET_ID.toString()).toPredicate();
        request = org.apache.ambari.server.controller.utilities.PropertyHelper.getUpdateRequest(requestProps, null);
        provider.updateResources(request, predicate);
        junit.framework.Assert.assertTrue(entityCapture.hasCaptured());
        org.apache.ambari.server.orm.entities.AlertTargetEntity entity = entityCapture.getValue();
        junit.framework.Assert.assertTrue("{prop1=val1}".equals(entity.getProperties()));
        junit.framework.Assert.assertTrue(entity.isEnabled());
        EasyMock.verify(m_amc, m_dao);
    }

    private org.apache.ambari.server.controller.internal.AlertTargetResourceProvider createProvider(org.apache.ambari.server.controller.AmbariManagementController amc) {
        return new org.apache.ambari.server.controller.internal.AlertTargetResourceProvider();
    }

    @java.lang.SuppressWarnings({ "rawtypes", "unchecked" })
    private java.util.List<org.apache.ambari.server.orm.entities.AlertTargetEntity> getMockEntities() throws java.lang.Exception {
        org.apache.ambari.server.orm.entities.AlertTargetEntity entity = new org.apache.ambari.server.orm.entities.AlertTargetEntity();
        entity.setTargetId(org.apache.ambari.server.controller.internal.AlertTargetResourceProviderTest.ALERT_TARGET_ID);
        entity.setDescription(org.apache.ambari.server.controller.internal.AlertTargetResourceProviderTest.ALERT_TARGET_DESC);
        entity.setTargetName(org.apache.ambari.server.controller.internal.AlertTargetResourceProviderTest.ALERT_TARGET_NAME);
        entity.setNotificationType(org.apache.ambari.server.controller.internal.AlertTargetResourceProviderTest.ALERT_TARGET_TYPE);
        entity.setProperties(org.apache.ambari.server.controller.internal.AlertTargetResourceProviderTest.ALERT_TARGET_PROPS);
        entity.setAlertStates(new java.util.HashSet(java.util.Arrays.asList(org.apache.ambari.server.state.AlertState.CRITICAL, org.apache.ambari.server.state.AlertState.WARNING)));
        return java.util.Arrays.asList(entity);
    }

    private java.util.Map<java.lang.String, java.lang.Object> getCreationProperties() throws java.lang.Exception {
        java.util.Map<java.lang.String, java.lang.Object> requestProps = new java.util.HashMap<>();
        requestProps.put(org.apache.ambari.server.controller.internal.AlertTargetResourceProvider.ALERT_TARGET_NAME, org.apache.ambari.server.controller.internal.AlertTargetResourceProviderTest.ALERT_TARGET_NAME);
        requestProps.put(org.apache.ambari.server.controller.internal.AlertTargetResourceProvider.ALERT_TARGET_DESCRIPTION, org.apache.ambari.server.controller.internal.AlertTargetResourceProviderTest.ALERT_TARGET_DESC);
        requestProps.put(org.apache.ambari.server.controller.internal.AlertTargetResourceProvider.ALERT_TARGET_NOTIFICATION_TYPE, org.apache.ambari.server.controller.internal.AlertTargetResourceProviderTest.ALERT_TARGET_TYPE);
        requestProps.put(org.apache.ambari.server.controller.internal.AlertTargetResourceProvider.ALERT_TARGET_PROPERTIES + "/foo", "bar");
        requestProps.put(org.apache.ambari.server.controller.internal.AlertTargetResourceProvider.ALERT_TARGET_PROPERTIES + "/foobar", "baz");
        return requestProps;
    }

    private java.util.Map<java.lang.String, java.lang.Object> getRecipientCreationProperties() throws java.lang.Exception {
        java.util.Map<java.lang.String, java.lang.Object> requestProps = new java.util.HashMap<>();
        requestProps.put(org.apache.ambari.server.controller.internal.AlertTargetResourceProvider.ALERT_TARGET_NAME, org.apache.ambari.server.controller.internal.AlertTargetResourceProviderTest.ALERT_TARGET_NAME);
        requestProps.put(org.apache.ambari.server.controller.internal.AlertTargetResourceProvider.ALERT_TARGET_DESCRIPTION, org.apache.ambari.server.controller.internal.AlertTargetResourceProviderTest.ALERT_TARGET_DESC);
        requestProps.put(org.apache.ambari.server.controller.internal.AlertTargetResourceProvider.ALERT_TARGET_NOTIFICATION_TYPE, org.apache.ambari.server.controller.internal.AlertTargetResourceProviderTest.ALERT_TARGET_TYPE);
        requestProps.put(org.apache.ambari.server.controller.internal.AlertTargetResourceProvider.ALERT_TARGET_PROPERTIES + "/ambari.dispatch.recipients", "[\"ambari@ambari.apache.org\"]");
        return requestProps;
    }

    private java.util.List<org.apache.ambari.server.orm.entities.AlertGroupEntity> getMockGroupEntities() throws java.lang.Exception {
        org.apache.ambari.server.orm.entities.AlertGroupEntity group1 = new org.apache.ambari.server.orm.entities.AlertGroupEntity();
        org.apache.ambari.server.orm.entities.AlertGroupEntity group2 = new org.apache.ambari.server.orm.entities.AlertGroupEntity();
        org.apache.ambari.server.orm.entities.AlertGroupEntity group3 = new org.apache.ambari.server.orm.entities.AlertGroupEntity();
        group1.setGroupId(1L);
        group1.setClusterId(1L);
        group2.setGroupId(2L);
        group2.setClusterId(1L);
        group3.setGroupId(3L);
        group3.setClusterId(1L);
        return java.util.Arrays.asList(group1, group2, group3);
    }

    private class MockModule implements com.google.inject.Module {
        @java.lang.Override
        public void configure(com.google.inject.Binder binder) {
            binder.bind(org.apache.ambari.server.orm.dao.AlertDispatchDAO.class).toInstance(m_dao);
            binder.bind(org.apache.ambari.server.state.Clusters.class).toInstance(org.easymock.EasyMock.createNiceMock(org.apache.ambari.server.state.Clusters.class));
            binder.bind(org.apache.ambari.server.state.Cluster.class).toInstance(org.easymock.EasyMock.createNiceMock(org.apache.ambari.server.state.Cluster.class));
            binder.bind(org.apache.ambari.server.metadata.ActionMetadata.class);
        }
    }
}