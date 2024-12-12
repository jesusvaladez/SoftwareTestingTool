package org.apache.ambari.server.controller.internal;
public class SchemaImplTest {
    private static final java.util.Set<java.lang.String> resourceProviderProperties = new java.util.HashSet<>();

    static {
        resourceProviderProperties.add(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("c1", "p1"));
        resourceProviderProperties.add(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("c1", "p2"));
        resourceProviderProperties.add(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("c1", "p3"));
        resourceProviderProperties.add(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("c2", "p4"));
    }

    private static final org.apache.ambari.server.controller.spi.ResourceProvider resourceProvider = new org.apache.ambari.server.controller.spi.ResourceProvider() {
        @java.lang.Override
        public java.util.Set<org.apache.ambari.server.controller.spi.Resource> getResources(org.apache.ambari.server.controller.spi.Request request, org.apache.ambari.server.controller.spi.Predicate predicate) throws org.apache.ambari.server.controller.spi.SystemException, org.apache.ambari.server.controller.spi.UnsupportedPropertyException, org.apache.ambari.server.controller.spi.NoSuchResourceException, org.apache.ambari.server.controller.spi.NoSuchParentResourceException {
            return null;
        }

        @java.lang.Override
        public org.apache.ambari.server.controller.spi.RequestStatus createResources(org.apache.ambari.server.controller.spi.Request request) throws org.apache.ambari.server.controller.spi.SystemException, org.apache.ambari.server.controller.spi.UnsupportedPropertyException, org.apache.ambari.server.controller.spi.ResourceAlreadyExistsException, org.apache.ambari.server.controller.spi.NoSuchParentResourceException {
            return new org.apache.ambari.server.controller.internal.RequestStatusImpl(null);
        }

        @java.lang.Override
        public org.apache.ambari.server.controller.spi.RequestStatus updateResources(org.apache.ambari.server.controller.spi.Request request, org.apache.ambari.server.controller.spi.Predicate predicate) throws org.apache.ambari.server.controller.spi.SystemException, org.apache.ambari.server.controller.spi.UnsupportedPropertyException, org.apache.ambari.server.controller.spi.NoSuchResourceException, org.apache.ambari.server.controller.spi.NoSuchParentResourceException {
            return new org.apache.ambari.server.controller.internal.RequestStatusImpl(null);
        }

        @java.lang.Override
        public org.apache.ambari.server.controller.spi.RequestStatus deleteResources(org.apache.ambari.server.controller.spi.Request request, org.apache.ambari.server.controller.spi.Predicate predicate) throws org.apache.ambari.server.controller.spi.SystemException, org.apache.ambari.server.controller.spi.UnsupportedPropertyException, org.apache.ambari.server.controller.spi.NoSuchResourceException, org.apache.ambari.server.controller.spi.NoSuchParentResourceException {
            return new org.apache.ambari.server.controller.internal.RequestStatusImpl(null);
        }

        @java.lang.Override
        public java.util.Map<org.apache.ambari.server.controller.spi.Resource.Type, java.lang.String> getKeyPropertyIds() {
            return org.apache.ambari.server.controller.internal.SchemaImplTest.keyPropertyIds;
        }

        @java.lang.Override
        public java.util.Set<java.lang.String> checkPropertyIds(java.util.Set<java.lang.String> propertyIds) {
            if (!org.apache.ambari.server.controller.internal.SchemaImplTest.resourceProviderProperties.containsAll(propertyIds)) {
                java.util.Set<java.lang.String> unsupportedPropertyIds = new java.util.HashSet<>(propertyIds);
                unsupportedPropertyIds.removeAll(org.apache.ambari.server.controller.internal.SchemaImplTest.resourceProviderProperties);
                return unsupportedPropertyIds;
            }
            return java.util.Collections.emptySet();
        }
    };

    private static final java.util.Set<java.lang.String> propertyProviderProperties = new java.util.HashSet<>();

    static {
        propertyProviderProperties.add(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("c3", "p5"));
        propertyProviderProperties.add(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("c3", "p6"));
        propertyProviderProperties.add(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("c4", "p7"));
        propertyProviderProperties.add(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("c4", "p8"));
    }

    private static final org.apache.ambari.server.controller.spi.PropertyProvider propertyProvider = new org.apache.ambari.server.controller.spi.PropertyProvider() {
        @java.lang.Override
        public java.util.Set<org.apache.ambari.server.controller.spi.Resource> populateResources(java.util.Set<org.apache.ambari.server.controller.spi.Resource> resources, org.apache.ambari.server.controller.spi.Request request, org.apache.ambari.server.controller.spi.Predicate predicate) {
            return null;
        }

        @java.lang.Override
        public java.util.Set<java.lang.String> checkPropertyIds(java.util.Set<java.lang.String> propertyIds) {
            if (!org.apache.ambari.server.controller.internal.SchemaImplTest.propertyProviderProperties.containsAll(propertyIds)) {
                java.util.Set<java.lang.String> unsupportedPropertyIds = new java.util.HashSet<>(propertyIds);
                unsupportedPropertyIds.removeAll(org.apache.ambari.server.controller.internal.SchemaImplTest.propertyProviderProperties);
                return unsupportedPropertyIds;
            }
            return java.util.Collections.emptySet();
        }
    };

    private static final java.util.List<org.apache.ambari.server.controller.spi.PropertyProvider> propertyProviders = new java.util.LinkedList<>();

    static {
        propertyProviders.add(propertyProvider);
    }

    private static final java.util.Map<org.apache.ambari.server.controller.spi.Resource.Type, java.lang.String> keyPropertyIds = new java.util.HashMap<>();

    static {
        keyPropertyIds.put(org.apache.ambari.server.controller.spi.Resource.Type.Cluster, org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("c1", "p1"));
        keyPropertyIds.put(org.apache.ambari.server.controller.spi.Resource.Type.Host, org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("c1", "p2"));
        keyPropertyIds.put(org.apache.ambari.server.controller.spi.Resource.Type.Component, org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("c1", "p3"));
    }

    @org.junit.Test
    public void testGetKeyPropertyId() {
        org.apache.ambari.server.controller.spi.Schema schema = new org.apache.ambari.server.controller.internal.SchemaImpl(org.apache.ambari.server.controller.internal.SchemaImplTest.resourceProvider);
        junit.framework.Assert.assertEquals(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("c1", "p1"), schema.getKeyPropertyId(org.apache.ambari.server.controller.spi.Resource.Type.Cluster));
        junit.framework.Assert.assertEquals(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("c1", "p2"), schema.getKeyPropertyId(org.apache.ambari.server.controller.spi.Resource.Type.Host));
        junit.framework.Assert.assertEquals(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("c1", "p3"), schema.getKeyPropertyId(org.apache.ambari.server.controller.spi.Resource.Type.Component));
    }
}