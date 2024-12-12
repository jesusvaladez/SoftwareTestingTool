package org.apache.ambari.server.view;
public class ViewSubResourceProviderTest {
    private static java.lang.String xml = "<view>\n" + (((((((((((((("    <name>MY_VIEW</name>\n" + "    <label>My View!</label>\n") + "    <version>1.0.0</version>\n") + "    <resource>\n") + "        <name>resource</name>\n") + "        <plural-name>resources</plural-name>\n") + "        <id-property>id</id-property>\n") + "        <resource-class>org.apache.ambari.server.view.ViewSubResourceProviderTest$MyResource</resource-class>\n") + "        <provider-class>org.apache.ambari.server.view.ViewSubResourceProviderTest$MyResourceProvider</provider-class>\n") + "        <service-class>org.apache.ambari.server.view.ViewSubResourceProviderTest$MyResourceService</service-class>\n") + "    </resource>\n") + "    <instance>\n") + "        <name>INSTANCE1</name>\n") + "    </instance>\n") + "</view>");

    @org.junit.Test
    public void testGetResources() throws java.lang.Exception {
        java.util.Properties properties = new java.util.Properties();
        properties.put("p1", "v1");
        org.apache.ambari.server.configuration.Configuration ambariConfig = new org.apache.ambari.server.configuration.Configuration(properties);
        org.apache.ambari.server.view.configuration.ViewConfig config = org.apache.ambari.server.view.configuration.ViewConfigTest.getConfig(org.apache.ambari.server.view.ViewSubResourceProviderTest.xml);
        org.apache.ambari.server.orm.entities.ViewEntity viewEntity = org.apache.ambari.server.view.ViewRegistryTest.getViewEntity(config, ambariConfig, getClass().getClassLoader(), "");
        org.apache.ambari.server.view.ViewRegistryTest.getViewInstanceEntity(viewEntity, config.getInstances().get(0));
        java.util.Map<org.apache.ambari.server.controller.spi.Resource.Type, org.apache.ambari.server.view.ViewSubResourceDefinition> resourceDefinitions = viewEntity.getResourceDefinitions();
        org.junit.Assert.assertEquals(1, resourceDefinitions.size());
        org.apache.ambari.server.controller.spi.Resource.Type type = resourceDefinitions.keySet().iterator().next();
        org.apache.ambari.server.view.ViewSubResourceProvider viewSubResourceProvider = new org.apache.ambari.server.view.ViewSubResourceProvider(type, org.apache.ambari.server.view.ViewSubResourceProviderTest.MyResource.class, "id", viewEntity);
        org.apache.ambari.server.controller.spi.Request request = org.apache.ambari.server.controller.utilities.PropertyHelper.getReadRequest("id", "properties", "metrics/myMetric");
        org.apache.ambari.server.controller.spi.Predicate predicate = new org.apache.ambari.server.controller.predicate.AlwaysPredicate();
        java.util.Set<org.apache.ambari.server.controller.spi.Resource> resources = viewSubResourceProvider.getResources(request, predicate);
        org.junit.Assert.assertEquals(2, resources.size());
        predicate = new org.apache.ambari.server.controller.utilities.PredicateBuilder().property("metrics/myMetric").greaterThan(1).toPredicate();
        resources = viewSubResourceProvider.getResources(request, predicate);
        org.junit.Assert.assertEquals(1, resources.size());
        org.junit.Assert.assertTrue(((java.lang.Integer) (resources.iterator().next().getPropertyValue("metrics/myMetric"))) > 1);
    }

    @org.junit.Test
    public void testGetResources_temporal() throws java.lang.Exception {
        java.util.Properties properties = new java.util.Properties();
        properties.put("p1", "v1");
        org.apache.ambari.server.configuration.Configuration ambariConfig = new org.apache.ambari.server.configuration.Configuration(properties);
        org.apache.ambari.server.view.configuration.ViewConfig config = org.apache.ambari.server.view.configuration.ViewConfigTest.getConfig(org.apache.ambari.server.view.ViewSubResourceProviderTest.xml);
        org.apache.ambari.server.orm.entities.ViewEntity viewEntity = org.apache.ambari.server.view.ViewRegistryTest.getViewEntity(config, ambariConfig, getClass().getClassLoader(), "");
        org.apache.ambari.server.view.ViewRegistryTest.getViewInstanceEntity(viewEntity, config.getInstances().get(0));
        java.util.Map<org.apache.ambari.server.controller.spi.Resource.Type, org.apache.ambari.server.view.ViewSubResourceDefinition> resourceDefinitions = viewEntity.getResourceDefinitions();
        org.junit.Assert.assertEquals(1, resourceDefinitions.size());
        org.apache.ambari.server.controller.spi.Resource.Type type = resourceDefinitions.keySet().iterator().next();
        org.apache.ambari.server.view.ViewSubResourceProvider viewSubResourceProvider = new org.apache.ambari.server.view.ViewSubResourceProvider(type, org.apache.ambari.server.view.ViewSubResourceProviderTest.MyResource.class, "id", viewEntity);
        java.util.Set<java.lang.String> requestProperties = new java.util.HashSet<>();
        requestProperties.add("metrics/myMetric");
        java.util.Map<java.lang.String, org.apache.ambari.server.controller.spi.TemporalInfo> temporalInfoMap = new java.util.HashMap<>();
        org.apache.ambari.server.controller.spi.TemporalInfo temporalInfo = new org.apache.ambari.server.controller.internal.TemporalInfoImpl(1000L, 1100L, 10L);
        temporalInfoMap.put("metrics/myMetric", temporalInfo);
        org.apache.ambari.server.controller.spi.Request request = org.apache.ambari.server.controller.utilities.PropertyHelper.getReadRequest(requestProperties, temporalInfoMap);
        org.apache.ambari.server.controller.spi.Predicate predicate = new org.apache.ambari.server.controller.predicate.AlwaysPredicate();
        java.util.Set<org.apache.ambari.server.controller.spi.Resource> resources = viewSubResourceProvider.getResources(request, predicate);
        org.junit.Assert.assertEquals(2, resources.size());
    }

    public static class MyResource {
        private java.lang.String id;

        private java.lang.String property;

        private java.util.Map<java.lang.String, java.lang.Object> metrics;

        public MyResource() {
        }

        public MyResource(java.lang.String id, java.lang.String property, java.util.Map<java.lang.String, java.lang.Object> metrics) {
            this.id = id;
            this.property = property;
            this.metrics = metrics;
        }

        public java.lang.String getId() {
            return id;
        }

        public void setId(java.lang.String id) {
            this.id = id;
        }

        public java.lang.String getProperty() {
            return property;
        }

        public void setProperty(java.lang.String property) {
            this.property = property;
        }

        public java.util.Map<java.lang.String, java.lang.Object> getMetrics() {
            return metrics;
        }

        public void setMetrics(java.util.Map<java.lang.String, java.lang.Object> metrics) {
            this.metrics = metrics;
        }
    }

    public static class MyResourceProvider implements org.apache.ambari.view.ResourceProvider<org.apache.ambari.server.view.ViewSubResourceProviderTest.MyResource> {
        @java.lang.Override
        public org.apache.ambari.server.view.ViewSubResourceProviderTest.MyResource getResource(java.lang.String resourceId, java.util.Set<java.lang.String> properties) throws org.apache.ambari.view.SystemException, org.apache.ambari.view.NoSuchResourceException, org.apache.ambari.view.UnsupportedPropertyException {
            return null;
        }

        @java.lang.Override
        public java.util.Set<org.apache.ambari.server.view.ViewSubResourceProviderTest.MyResource> getResources(org.apache.ambari.view.ReadRequest request) throws org.apache.ambari.view.SystemException, org.apache.ambari.view.NoSuchResourceException, org.apache.ambari.view.UnsupportedPropertyException {
            java.util.Set<org.apache.ambari.server.view.ViewSubResourceProviderTest.MyResource> resources = new java.util.HashSet<>();
            resources.add(new org.apache.ambari.server.view.ViewSubResourceProviderTest.MyResource("1", "foo", getMetricsValue(1, request, "myMetric")));
            resources.add(new org.apache.ambari.server.view.ViewSubResourceProviderTest.MyResource("2", "bar", getMetricsValue(2, request, "myMetric")));
            return resources;
        }

        private java.util.Map<java.lang.String, java.lang.Object> getMetricsValue(java.lang.Number value, org.apache.ambari.view.ReadRequest request, java.lang.String metricName) {
            org.apache.ambari.view.ReadRequest.TemporalInfo temporalInfo = request.getTemporalInfo("metrics/" + metricName);
            if (temporalInfo != null) {
                int steps = ((int) ((temporalInfo.getEndTime() - temporalInfo.getStartTime()) / temporalInfo.getStep()));
                java.lang.Number[][] datapointsArray = new java.lang.Number[steps][2];
                for (int i = 0; i < steps; ++i) {
                    datapointsArray[i][0] = temporalInfo.getStartTime() + (i * temporalInfo.getStep());
                    datapointsArray[i][1] = value;
                }
                return java.util.Collections.singletonMap(metricName, datapointsArray);
            }
            return java.util.Collections.singletonMap(metricName, value);
        }

        @java.lang.Override
        public void createResource(java.lang.String resourceId, java.util.Map<java.lang.String, java.lang.Object> properties) throws org.apache.ambari.view.SystemException, org.apache.ambari.view.ResourceAlreadyExistsException, org.apache.ambari.view.NoSuchResourceException, org.apache.ambari.view.UnsupportedPropertyException {
        }

        @java.lang.Override
        public boolean updateResource(java.lang.String resourceId, java.util.Map<java.lang.String, java.lang.Object> properties) throws org.apache.ambari.view.SystemException, org.apache.ambari.view.NoSuchResourceException, org.apache.ambari.view.UnsupportedPropertyException {
            return false;
        }

        @java.lang.Override
        public boolean deleteResource(java.lang.String resourceId) throws org.apache.ambari.view.SystemException, org.apache.ambari.view.NoSuchResourceException, org.apache.ambari.view.UnsupportedPropertyException {
            return false;
        }
    }

    public static class MyResourceService {}
}