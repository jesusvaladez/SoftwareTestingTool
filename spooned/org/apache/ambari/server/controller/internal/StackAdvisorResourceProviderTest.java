package org.apache.ambari.server.controller.internal;
import javax.annotation.Nonnull;
import static org.apache.ambari.server.controller.internal.StackAdvisorResourceProvider.CONFIGURATIONS_PROPERTY_ID;
import static org.apache.ambari.server.controller.internal.StackAdvisorResourceProvider.USER_CONTEXT_OPERATION_DETAILS_PROPERTY;
import static org.apache.ambari.server.controller.internal.StackAdvisorResourceProvider.USER_CONTEXT_OPERATION_PROPERTY;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
public class StackAdvisorResourceProviderTest {
    private org.apache.ambari.server.controller.internal.RecommendationResourceProvider provider;

    @org.junit.Test
    public void testCalculateConfigurations() throws java.lang.Exception {
        org.apache.ambari.server.controller.spi.Request request = createMockRequest(org.apache.ambari.server.controller.internal.StackAdvisorResourceProvider.CONFIGURATIONS_PROPERTY_ID + "site/properties/string_prop", "string", org.apache.ambari.server.controller.internal.StackAdvisorResourceProvider.CONFIGURATIONS_PROPERTY_ID + "site/properties/array_prop", com.google.common.collect.Lists.newArrayList("array1", "array2"));
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>>> calculatedConfigurations = provider.calculateConfigurations(request);
        org.junit.Assert.assertNotNull(calculatedConfigurations);
        org.junit.Assert.assertEquals(1, calculatedConfigurations.size());
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> site = calculatedConfigurations.get("site");
        org.junit.Assert.assertNotNull(site);
        org.junit.Assert.assertEquals(1, site.size());
        java.util.Map<java.lang.String, java.lang.String> properties = site.get("properties");
        org.junit.Assert.assertNotNull(properties);
        org.junit.Assert.assertEquals(2, properties.size());
        org.junit.Assert.assertEquals("string", properties.get("string_prop"));
        org.junit.Assert.assertEquals("[array1, array2]", properties.get("array_prop"));
    }

    @javax.annotation.Nonnull
    private org.apache.ambari.server.controller.internal.RecommendationResourceProvider createRecommendationResourceProvider() {
        org.apache.ambari.server.controller.AmbariManagementController ambariManagementController = Mockito.mock(org.apache.ambari.server.controller.AmbariManagementController.class);
        return new org.apache.ambari.server.controller.internal.RecommendationResourceProvider(ambariManagementController);
    }

    @javax.annotation.Nonnull
    private org.apache.ambari.server.controller.spi.Request createMockRequest(java.lang.Object... propertyKeysAndValues) {
        org.apache.ambari.server.controller.spi.Request request = Mockito.mock(org.apache.ambari.server.controller.spi.Request.class);
        java.util.Set<java.util.Map<java.lang.String, java.lang.Object>> propertiesSet = new java.util.HashSet<>();
        java.util.Map<java.lang.String, java.lang.Object> propertiesMap = new java.util.HashMap<>();
        java.util.Iterator<java.lang.Object> it = java.util.Arrays.asList(propertyKeysAndValues).iterator();
        while (it.hasNext()) {
            java.lang.String key = ((java.lang.String) (it.next()));
            java.lang.Object value = it.next();
            propertiesMap.put(key, value);
        } 
        propertiesSet.add(propertiesMap);
        Mockito.doReturn(propertiesSet).when(request).getProperties();
        return request;
    }

    @org.junit.Test
    public void testReadUserContext() throws java.lang.Exception {
        org.apache.ambari.server.controller.spi.Request request = createMockRequest(org.apache.ambari.server.controller.internal.StackAdvisorResourceProvider.CONFIGURATIONS_PROPERTY_ID + "site/properties/string_prop", "string", org.apache.ambari.server.controller.internal.StackAdvisorResourceProvider.USER_CONTEXT_OPERATION_PROPERTY, "op1", org.apache.ambari.server.controller.internal.StackAdvisorResourceProvider.USER_CONTEXT_OPERATION_DETAILS_PROPERTY, "op_det");
        java.util.Map<java.lang.String, java.lang.String> userContext = provider.readUserContext(request);
        org.junit.Assert.assertNotNull(userContext);
        org.junit.Assert.assertEquals(2, userContext.size());
        org.junit.Assert.assertEquals("op1", userContext.get("operation"));
        org.junit.Assert.assertEquals("op_det", userContext.get("operation_details"));
    }

    @org.junit.Test
    public void testCalculateConfigurationsWithNullPropertyValues() throws java.lang.Exception {
        org.apache.ambari.server.controller.spi.Request request = createMockRequest(org.apache.ambari.server.controller.internal.StackAdvisorResourceProvider.CONFIGURATIONS_PROPERTY_ID + "site/properties/string_prop", null, org.apache.ambari.server.controller.internal.StackAdvisorResourceProvider.CONFIGURATIONS_PROPERTY_ID + "site/properties/array_prop", com.google.common.collect.Lists.newArrayList("array1", "array2"));
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>>> calculatedConfigurations = provider.calculateConfigurations(request);
        org.junit.Assert.assertNotNull(calculatedConfigurations);
        org.junit.Assert.assertEquals(1, calculatedConfigurations.size());
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> site = calculatedConfigurations.get("site");
        org.junit.Assert.assertNotNull(site);
        org.junit.Assert.assertEquals(1, site.size());
        java.util.Map<java.lang.String, java.lang.String> properties = site.get("properties");
        org.junit.Assert.assertNotNull(properties);
        org.junit.Assert.assertEquals("[array1, array2]", properties.get("array_prop"));
        org.junit.Assert.assertFalse(properties.containsKey("string_prop"));
    }

    @org.junit.Test
    public void testStackAdvisorWithEmptyHosts() {
        org.apache.ambari.server.controller.AmbariManagementController ambariManagementController = Mockito.mock(org.apache.ambari.server.controller.AmbariManagementController.class);
        org.apache.ambari.server.controller.internal.RecommendationResourceProvider provider = new org.apache.ambari.server.controller.internal.RecommendationResourceProvider(ambariManagementController);
        org.apache.ambari.server.controller.spi.Request request = Mockito.mock(org.apache.ambari.server.controller.spi.Request.class);
        java.util.Set<java.util.Map<java.lang.String, java.lang.Object>> propertiesSet = new java.util.HashSet<>();
        java.util.Map<java.lang.String, java.lang.Object> propertiesMap = new java.util.HashMap<>();
        propertiesMap.put("hosts", new java.util.LinkedHashSet<>());
        propertiesMap.put("recommend", "configurations");
        propertiesSet.add(propertiesMap);
        Mockito.doReturn(propertiesSet).when(request).getProperties();
        try {
            provider.createResources(request);
            org.junit.Assert.fail();
        } catch (java.lang.Exception e) {
        }
    }

    @org.junit.Before
    public void init() {
        provider = createRecommendationResourceProvider();
    }
}