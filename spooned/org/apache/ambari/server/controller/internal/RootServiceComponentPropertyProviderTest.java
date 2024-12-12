package org.apache.ambari.server.controller.internal;
public class RootServiceComponentPropertyProviderTest {
    @org.junit.Test
    public void testPopulateResources_AmbariServer_None() throws java.lang.Exception {
        testPopulateResources(org.apache.ambari.server.controller.RootComponent.AMBARI_SERVER.name(), false, false, false, false);
    }

    @org.junit.Test
    public void testPopulateResources_AmbariServer_CiphersAndJCEPolicy() throws java.lang.Exception {
        testPopulateResources(org.apache.ambari.server.controller.RootComponent.AMBARI_SERVER.name(), true, true, true, true);
    }

    @org.junit.Test
    public void testPopulateResources_AmbariServer_JCEPolicy() throws java.lang.Exception {
        testPopulateResources(org.apache.ambari.server.controller.RootComponent.AMBARI_SERVER.name(), false, true, false, true);
    }

    @org.junit.Test
    public void testPopulateResources_AmbariServer_Ciphers() throws java.lang.Exception {
        testPopulateResources(org.apache.ambari.server.controller.RootComponent.AMBARI_SERVER.name(), true, false, true, false);
    }

    @org.junit.Test
    public void testPopulateResources_AmbariAgent_CiphersAndJCEPolicy() throws java.lang.Exception {
        testPopulateResources(org.apache.ambari.server.controller.RootComponent.AMBARI_AGENT.name(), true, true, false, false);
    }

    public void testPopulateResources(java.lang.String componentName, boolean requestCiphers, boolean requestJCEPolicy, boolean expectCiphers, boolean expectJCEPolicy) throws java.lang.Exception {
        org.apache.ambari.server.controller.internal.RootServiceComponentPropertyProvider propertyProvider = new org.apache.ambari.server.controller.internal.RootServiceComponentPropertyProvider();
        org.apache.ambari.server.controller.spi.Resource resource = new org.apache.ambari.server.controller.internal.ResourceImpl(org.apache.ambari.server.controller.spi.Resource.Type.RootService);
        resource.setProperty(org.apache.ambari.server.controller.internal.RootServiceComponentResourceProvider.COMPONENT_NAME_PROPERTY_ID, componentName);
        resource.setProperty(org.apache.ambari.server.controller.internal.RootServiceComponentResourceProvider.SERVICE_NAME_PROPERTY_ID, org.apache.ambari.server.controller.RootService.AMBARI.name());
        java.util.HashSet<java.lang.String> requestIds = new java.util.HashSet<>();
        if (requestCiphers) {
            requestIds.add(org.apache.ambari.server.controller.internal.RootServiceComponentPropertyProvider.CIPHER_PROPERTIES_PROPERTY_ID);
        }
        if (requestJCEPolicy) {
            requestIds.add(org.apache.ambari.server.controller.internal.RootServiceComponentPropertyProvider.JCE_POLICY_PROPERTY_ID);
        }
        org.apache.ambari.server.controller.spi.Request request = org.apache.ambari.server.controller.utilities.PropertyHelper.getReadRequest(requestIds, new java.util.HashMap<>());
        java.util.Set<org.apache.ambari.server.controller.spi.Resource> resources = propertyProvider.populateResources(java.util.Collections.singleton(resource), request, null);
        org.junit.Assert.assertEquals(1, resources.size());
        resource = resources.iterator().next();
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.Object>> values = resource.getPropertiesMap();
        org.junit.Assert.assertEquals(expectCiphers, values.containsKey(org.apache.ambari.server.controller.internal.RootServiceComponentPropertyProvider.CIPHER_PROPERTIES_PROPERTY_ID));
        org.junit.Assert.assertEquals(expectJCEPolicy, values.containsKey(org.apache.ambari.server.controller.internal.RootServiceComponentPropertyProvider.JCE_POLICY_PROPERTY_ID));
    }
}