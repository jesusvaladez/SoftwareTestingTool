package org.apache.ambari.server.controller.internal;
import org.springframework.security.core.context.SecurityContextHolder;
import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.createNiceMock;
public class LdapSyncEventResourceProviderTest {
    @org.junit.BeforeClass
    public static void setupAuthentication() {
        org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(org.apache.ambari.server.security.TestAuthenticationFactory.createAdministrator());
    }

    @org.junit.Test
    public void testCreateResources() throws java.lang.Exception {
        org.apache.ambari.server.controller.AmbariManagementController amc = EasyMock.createMock(org.apache.ambari.server.controller.AmbariManagementController.class);
        org.apache.ambari.server.controller.internal.LdapSyncEventResourceProvider provider = new org.apache.ambari.server.controller.internal.LdapSyncEventResourceProviderTest.TestLdapSyncEventResourceProvider(amc);
        java.util.Set<java.util.Map<java.lang.String, java.lang.Object>> properties = new java.util.HashSet<>();
        java.util.Map<java.lang.String, java.lang.Object> propertyMap = new java.util.HashMap<>();
        java.util.Set<java.util.Map<java.lang.String, java.lang.String>> specs = new java.util.HashSet<>();
        propertyMap.put(org.apache.ambari.server.controller.internal.LdapSyncEventResourceProvider.EVENT_SPECS_PROPERTY_ID, specs);
        properties.add(propertyMap);
        provider.createResources(org.apache.ambari.server.controller.utilities.PropertyHelper.getCreateRequest(properties, null));
        java.util.Set<org.apache.ambari.server.controller.spi.Resource> resources = provider.getResources(org.apache.ambari.server.controller.utilities.PropertyHelper.getReadRequest(), null);
        org.junit.Assert.assertEquals(1, resources.size());
    }

    @org.junit.Test
    public void testGetResources() throws java.lang.Exception {
        org.apache.ambari.server.controller.AmbariManagementController amc = EasyMock.createMock(org.apache.ambari.server.controller.AmbariManagementController.class);
        org.apache.ambari.server.controller.internal.LdapSyncEventResourceProvider provider = new org.apache.ambari.server.controller.internal.LdapSyncEventResourceProviderTest.TestLdapSyncEventResourceProvider(amc);
        java.util.Set<java.util.Map<java.lang.String, java.lang.Object>> properties = new java.util.HashSet<>();
        java.util.Map<java.lang.String, java.lang.Object> propertyMap = new java.util.HashMap<>();
        java.util.Set<java.util.Map<java.lang.String, java.lang.String>> specs = new java.util.HashSet<>();
        propertyMap.put(org.apache.ambari.server.controller.internal.LdapSyncEventResourceProvider.EVENT_SPECS_PROPERTY_ID, specs);
        properties.add(propertyMap);
        provider.createResources(org.apache.ambari.server.controller.utilities.PropertyHelper.getCreateRequest(properties, null));
        java.util.Set<org.apache.ambari.server.controller.spi.Resource> resources = provider.getResources(org.apache.ambari.server.controller.utilities.PropertyHelper.getReadRequest(), null);
        org.junit.Assert.assertEquals(1, resources.size());
    }

    @org.junit.Test
    public void testUpdateResources() throws java.lang.Exception {
        org.apache.ambari.server.controller.AmbariManagementController amc = EasyMock.createMock(org.apache.ambari.server.controller.AmbariManagementController.class);
        org.apache.ambari.server.controller.internal.LdapSyncEventResourceProvider provider = new org.apache.ambari.server.controller.internal.LdapSyncEventResourceProviderTest.TestLdapSyncEventResourceProvider(amc);
        org.apache.ambari.server.controller.spi.Request request = EasyMock.createNiceMock(org.apache.ambari.server.controller.spi.Request.class);
        try {
            provider.updateResources(request, null);
            org.junit.Assert.fail("expected UnsupportedOperationException");
        } catch (java.lang.UnsupportedOperationException e) {
        }
    }

    @org.junit.Test
    public void testDeleteResources() throws java.lang.Exception {
        org.apache.ambari.server.controller.AmbariManagementController amc = EasyMock.createMock(org.apache.ambari.server.controller.AmbariManagementController.class);
        org.apache.ambari.server.controller.internal.LdapSyncEventResourceProvider provider = new org.apache.ambari.server.controller.internal.LdapSyncEventResourceProviderTest.TestLdapSyncEventResourceProvider(amc);
        java.util.Set<java.util.Map<java.lang.String, java.lang.Object>> properties = new java.util.HashSet<>();
        java.util.Map<java.lang.String, java.lang.Object> propertyMap = new java.util.HashMap<>();
        java.util.Set<java.util.Map<java.lang.String, java.lang.String>> specs = new java.util.HashSet<>();
        propertyMap.put(org.apache.ambari.server.controller.internal.LdapSyncEventResourceProvider.EVENT_SPECS_PROPERTY_ID, specs);
        properties.add(propertyMap);
        provider.createResources(org.apache.ambari.server.controller.utilities.PropertyHelper.getCreateRequest(properties, null));
        java.util.Set<org.apache.ambari.server.controller.spi.Resource> resources = provider.getResources(org.apache.ambari.server.controller.utilities.PropertyHelper.getReadRequest(), null);
        org.junit.Assert.assertEquals(1, resources.size());
        provider.deleteResources(new org.apache.ambari.server.controller.internal.RequestImpl(null, null, null, null), null);
        resources = provider.getResources(org.apache.ambari.server.controller.utilities.PropertyHelper.getReadRequest(), null);
        org.junit.Assert.assertEquals(0, resources.size());
    }

    protected static class TestLdapSyncEventResourceProvider extends org.apache.ambari.server.controller.internal.LdapSyncEventResourceProvider {
        public TestLdapSyncEventResourceProvider(org.apache.ambari.server.controller.AmbariManagementController managementController) {
            super(managementController);
        }

        @java.lang.Override
        protected void ensureEventProcessor() {
        }
    }
}