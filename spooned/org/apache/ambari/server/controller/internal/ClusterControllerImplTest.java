package org.apache.ambari.server.controller.internal;
import org.easymock.EasyMock;
import static org.easymock.EasyMock.anyObject;
import static org.easymock.EasyMock.createNiceMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
public class ClusterControllerImplTest {
    private static final java.util.Set<java.lang.String> propertyProviderProperties = new java.util.HashSet<>();

    private static final java.lang.String UNSUPPORTED_PROPERTY = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("c1", "unsupported");

    static {
        propertyProviderProperties.add(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("c3", "p5"));
        propertyProviderProperties.add(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("c3", "p6"));
        propertyProviderProperties.add(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("c4", "p7"));
        propertyProviderProperties.add(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("c4", "p8"));
        propertyProviderProperties.add(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("alerts_summary", "WARNING"));
        propertyProviderProperties.add(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("alerts_summary", "CRITICAL"));
    }

    private static final org.apache.ambari.server.controller.spi.PropertyProvider propertyProvider = new org.apache.ambari.server.controller.spi.PropertyProvider() {
        @java.lang.Override
        public java.util.Set<org.apache.ambari.server.controller.spi.Resource> populateResources(java.util.Set<org.apache.ambari.server.controller.spi.Resource> resources, org.apache.ambari.server.controller.spi.Request request, org.apache.ambari.server.controller.spi.Predicate predicate) {
            int cnt = 0;
            for (org.apache.ambari.server.controller.spi.Resource resource : resources) {
                resource.setProperty(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("c3", "p5"), cnt + 100);
                resource.setProperty(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("c3", "p6"), cnt % 2);
                resource.setProperty(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("c4", "p7"), "monkey");
                resource.setProperty(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("c4", "p8"), "runner");
                ++cnt;
            }
            return resources;
        }

        @java.lang.Override
        public java.util.Set<java.lang.String> checkPropertyIds(java.util.Set<java.lang.String> propertyIds) {
            if (!org.apache.ambari.server.controller.internal.ClusterControllerImplTest.propertyProviderProperties.containsAll(propertyIds)) {
                java.util.Set<java.lang.String> unsupportedPropertyIds = new java.util.HashSet<>(propertyIds);
                unsupportedPropertyIds.removeAll(org.apache.ambari.server.controller.internal.ClusterControllerImplTest.propertyProviderProperties);
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
        keyPropertyIds.put(org.apache.ambari.server.controller.spi.Resource.Type.Cluster, org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("Hosts", "cluster_name"));
        keyPropertyIds.put(org.apache.ambari.server.controller.spi.Resource.Type.Host, org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("Hosts", "host_name"));
    }

    private static final java.util.Set<java.lang.String> resourceProviderProperties = new java.util.HashSet<>();

    static {
        resourceProviderProperties.add(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("Hosts", "cluster_name"));
        resourceProviderProperties.add(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("Hosts", "host_name"));
        resourceProviderProperties.add(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("c1", "p1"));
        resourceProviderProperties.add(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("c1", "p2"));
        resourceProviderProperties.add(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("c1", "p3"));
        resourceProviderProperties.add(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("c2", "p4"));
        resourceProviderProperties.add("Hosts");
        resourceProviderProperties.add("c1");
        resourceProviderProperties.add("c2");
    }

    @org.junit.Test
    public void testGetResources() throws java.lang.Exception {
        org.apache.ambari.server.controller.internal.ClusterControllerImpl controller = new org.apache.ambari.server.controller.internal.ClusterControllerImpl(new org.apache.ambari.server.controller.internal.ClusterControllerImplTest.TestProviderModule());
        java.util.Set<java.lang.String> propertyIds = new java.util.HashSet<>();
        propertyIds.add(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("c1", "p1"));
        propertyIds.add(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("c1", "p3"));
        org.apache.ambari.server.controller.spi.Request request = org.apache.ambari.server.controller.utilities.PropertyHelper.getReadRequest(propertyIds);
        java.lang.Iterable<org.apache.ambari.server.controller.spi.Resource> iterable = controller.getResourceIterable(org.apache.ambari.server.controller.spi.Resource.Type.Host, request, null);
        int cnt = 0;
        for (org.apache.ambari.server.controller.spi.Resource resource : iterable) {
            junit.framework.Assert.assertEquals(org.apache.ambari.server.controller.spi.Resource.Type.Host, resource.getType());
            ++cnt;
        }
        junit.framework.Assert.assertEquals(4, cnt);
    }

    @org.junit.Test
    public void testGetResourcesPageFromStart() throws java.lang.Exception {
        org.apache.ambari.server.controller.internal.ClusterControllerImpl controller = new org.apache.ambari.server.controller.internal.ClusterControllerImpl(new org.apache.ambari.server.controller.internal.ClusterControllerImplTest.TestProviderModule());
        java.util.Set<java.lang.String> propertyIds = new java.util.HashSet<>();
        org.apache.ambari.server.controller.spi.Request request = org.apache.ambari.server.controller.utilities.PropertyHelper.getReadRequest(propertyIds);
        org.apache.ambari.server.controller.spi.PageRequest pageRequest = new org.apache.ambari.server.controller.internal.PageRequestImpl(org.apache.ambari.server.controller.spi.PageRequest.StartingPoint.Beginning, 2, 0, null, null);
        org.apache.ambari.server.controller.spi.PageResponse pageResponse = controller.getResources(org.apache.ambari.server.controller.spi.Resource.Type.Host, request, null, pageRequest, null);
        java.lang.Iterable<org.apache.ambari.server.controller.spi.Resource> iterable = pageResponse.getIterable();
        java.util.List<org.apache.ambari.server.controller.spi.Resource> list = new java.util.LinkedList<>();
        for (org.apache.ambari.server.controller.spi.Resource resource : iterable) {
            list.add(resource);
        }
        junit.framework.Assert.assertEquals(2, list.size());
        junit.framework.Assert.assertEquals("host:0", ((java.lang.String) (list.get(0).getPropertyValue(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("Hosts", "host_name")))));
        junit.framework.Assert.assertEquals(org.apache.ambari.server.controller.spi.Resource.Type.Host, list.get(0).getType());
        junit.framework.Assert.assertEquals("host:1", ((java.lang.String) (list.get(1).getPropertyValue(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("Hosts", "host_name")))));
        junit.framework.Assert.assertEquals(org.apache.ambari.server.controller.spi.Resource.Type.Host, list.get(1).getType());
        junit.framework.Assert.assertEquals(4, pageResponse.getTotalResourceCount().intValue());
        pageRequest = new org.apache.ambari.server.controller.internal.PageRequestImpl(org.apache.ambari.server.controller.spi.PageRequest.StartingPoint.Beginning, 3, 0, null, null);
        pageResponse = controller.getResources(org.apache.ambari.server.controller.spi.Resource.Type.Host, request, null, pageRequest, null);
        iterable = pageResponse.getIterable();
        list = new java.util.LinkedList<>();
        for (org.apache.ambari.server.controller.spi.Resource resource : iterable) {
            list.add(resource);
        }
        junit.framework.Assert.assertEquals(3, list.size());
        junit.framework.Assert.assertEquals("host:0", ((java.lang.String) (list.get(0).getPropertyValue(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("Hosts", "host_name")))));
        junit.framework.Assert.assertEquals(org.apache.ambari.server.controller.spi.Resource.Type.Host, list.get(0).getType());
        junit.framework.Assert.assertEquals("host:1", ((java.lang.String) (list.get(1).getPropertyValue(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("Hosts", "host_name")))));
        junit.framework.Assert.assertEquals(org.apache.ambari.server.controller.spi.Resource.Type.Host, list.get(1).getType());
        junit.framework.Assert.assertEquals("host:2", ((java.lang.String) (list.get(2).getPropertyValue(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("Hosts", "host_name")))));
        junit.framework.Assert.assertEquals(org.apache.ambari.server.controller.spi.Resource.Type.Host, list.get(2).getType());
        junit.framework.Assert.assertEquals(4, pageResponse.getTotalResourceCount().intValue());
    }

    @org.junit.Test
    public void testGetResourcesSortedByProperty() throws java.lang.Exception {
        org.apache.ambari.server.controller.internal.ClusterControllerImpl controller = new org.apache.ambari.server.controller.internal.ClusterControllerImpl(new org.apache.ambari.server.controller.internal.ClusterControllerImplTest.TestProviderModule());
        java.util.Set<java.lang.String> propertyIds = new java.util.HashSet<>();
        propertyIds.add(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("c1", "p1"));
        propertyIds.add(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("c1", "p2"));
        propertyIds.add(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("c1", "p3"));
        propertyIds.add(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("c2", "p4"));
        org.apache.ambari.server.controller.spi.Request request = org.apache.ambari.server.controller.utilities.PropertyHelper.getReadRequest(propertyIds);
        java.util.List<org.apache.ambari.server.controller.spi.SortRequestProperty> sortRequestProperties = java.util.Collections.singletonList(new org.apache.ambari.server.controller.spi.SortRequestProperty("Hosts/host_name", org.apache.ambari.server.controller.spi.SortRequest.Order.ASC));
        org.apache.ambari.server.controller.spi.SortRequest sortRequest = new org.apache.ambari.server.controller.internal.SortRequestImpl(sortRequestProperties);
        java.lang.Iterable<org.apache.ambari.server.controller.spi.Resource> iterable = controller.getResources(org.apache.ambari.server.controller.spi.Resource.Type.Host, request, null, null, sortRequest).getIterable();
        java.util.List<org.apache.ambari.server.controller.spi.Resource> list = new java.util.LinkedList<>();
        for (org.apache.ambari.server.controller.spi.Resource resource : iterable) {
            list.add(resource);
        }
        junit.framework.Assert.assertEquals(4, list.size());
        junit.framework.Assert.assertEquals("host:0", ((java.lang.String) (list.get(0).getPropertyValue(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("Hosts", "host_name")))));
        junit.framework.Assert.assertEquals("host:1", ((java.lang.String) (list.get(1).getPropertyValue(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("Hosts", "host_name")))));
        junit.framework.Assert.assertEquals("host:2", ((java.lang.String) (list.get(2).getPropertyValue(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("Hosts", "host_name")))));
        junit.framework.Assert.assertEquals("host:3", ((java.lang.String) (list.get(3).getPropertyValue(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("Hosts", "host_name")))));
        sortRequestProperties = java.util.Collections.singletonList(new org.apache.ambari.server.controller.spi.SortRequestProperty("Hosts/host_name", org.apache.ambari.server.controller.spi.SortRequest.Order.DESC));
        sortRequest = new org.apache.ambari.server.controller.internal.SortRequestImpl(sortRequestProperties);
        iterable = controller.getResources(org.apache.ambari.server.controller.spi.Resource.Type.Host, request, null, null, sortRequest).getIterable();
        list = new java.util.LinkedList<>();
        for (org.apache.ambari.server.controller.spi.Resource resource : iterable) {
            list.add(resource);
        }
        junit.framework.Assert.assertEquals(4, list.size());
        junit.framework.Assert.assertEquals("host:3", ((java.lang.String) (list.get(0).getPropertyValue(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("Hosts", "host_name")))));
        junit.framework.Assert.assertEquals("host:2", ((java.lang.String) (list.get(1).getPropertyValue(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("Hosts", "host_name")))));
        junit.framework.Assert.assertEquals("host:1", ((java.lang.String) (list.get(2).getPropertyValue(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("Hosts", "host_name")))));
        junit.framework.Assert.assertEquals("host:0", ((java.lang.String) (list.get(3).getPropertyValue(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("Hosts", "host_name")))));
    }

    @org.junit.Test
    public void testGetResourcesSortedByMultiProperty() throws java.lang.Exception {
        org.apache.ambari.server.controller.internal.ClusterControllerImpl controller = new org.apache.ambari.server.controller.internal.ClusterControllerImpl(new org.apache.ambari.server.controller.internal.ClusterControllerImplTest.TestProviderModule());
        java.util.Set<java.lang.String> propertyIds = new java.util.HashSet<>();
        propertyIds.add(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("c1", "p1"));
        propertyIds.add(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("c1", "p2"));
        propertyIds.add(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("c1", "p3"));
        propertyIds.add(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("c2", "p4"));
        org.apache.ambari.server.controller.spi.Request request = org.apache.ambari.server.controller.utilities.PropertyHelper.getReadRequest(propertyIds);
        java.util.List<org.apache.ambari.server.controller.spi.SortRequestProperty> sortRequestProperties = new java.util.ArrayList<org.apache.ambari.server.controller.spi.SortRequestProperty>() {
            {
                add(new org.apache.ambari.server.controller.spi.SortRequestProperty("c1/p2", org.apache.ambari.server.controller.spi.SortRequest.Order.DESC));
                add(new org.apache.ambari.server.controller.spi.SortRequestProperty("c1/p1", org.apache.ambari.server.controller.spi.SortRequest.Order.DESC));
            }
        };
        org.apache.ambari.server.controller.spi.SortRequest sortRequest = new org.apache.ambari.server.controller.internal.SortRequestImpl(sortRequestProperties);
        java.lang.Iterable<org.apache.ambari.server.controller.spi.Resource> iterable = controller.getResources(org.apache.ambari.server.controller.spi.Resource.Type.Host, request, null, null, sortRequest).getIterable();
        java.util.List<org.apache.ambari.server.controller.spi.Resource> list = new java.util.LinkedList<>();
        for (org.apache.ambari.server.controller.spi.Resource resource : iterable) {
            list.add(resource);
        }
        junit.framework.Assert.assertEquals(4, list.size());
        junit.framework.Assert.assertEquals("host:3", ((java.lang.String) (list.get(0).getPropertyValue(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("Hosts", "host_name")))));
        junit.framework.Assert.assertEquals("host:1", ((java.lang.String) (list.get(1).getPropertyValue(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("Hosts", "host_name")))));
        junit.framework.Assert.assertEquals("host:2", ((java.lang.String) (list.get(2).getPropertyValue(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("Hosts", "host_name")))));
        junit.framework.Assert.assertEquals("host:0", ((java.lang.String) (list.get(3).getPropertyValue(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("Hosts", "host_name")))));
    }

    @org.junit.Test
    public void testGetResourcesPageFromOffset() throws java.lang.Exception {
        org.apache.ambari.server.controller.internal.ClusterControllerImpl controller = new org.apache.ambari.server.controller.internal.ClusterControllerImpl(new org.apache.ambari.server.controller.internal.ClusterControllerImplTest.TestProviderModule());
        java.util.Set<java.lang.String> propertyIds = new java.util.HashSet<>();
        org.apache.ambari.server.controller.spi.Request request = org.apache.ambari.server.controller.utilities.PropertyHelper.getReadRequest(propertyIds);
        org.apache.ambari.server.controller.spi.PageRequest pageRequest = new org.apache.ambari.server.controller.internal.PageRequestImpl(org.apache.ambari.server.controller.spi.PageRequest.StartingPoint.OffsetStart, 2, 1, null, null);
        org.apache.ambari.server.controller.spi.PageResponse pageResponse = controller.getResources(org.apache.ambari.server.controller.spi.Resource.Type.Host, request, null, pageRequest, null);
        junit.framework.Assert.assertEquals(1, pageResponse.getOffset());
        junit.framework.Assert.assertEquals("host:0", pageResponse.getPreviousResource().getPropertyValue(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("Hosts", "host_name")));
        junit.framework.Assert.assertEquals("host:3", pageResponse.getNextResource().getPropertyValue(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("Hosts", "host_name")));
        java.lang.Iterable<org.apache.ambari.server.controller.spi.Resource> iterable = pageResponse.getIterable();
        java.util.List<org.apache.ambari.server.controller.spi.Resource> list = new java.util.LinkedList<>();
        for (org.apache.ambari.server.controller.spi.Resource resource : iterable) {
            list.add(resource);
        }
        junit.framework.Assert.assertEquals(2, list.size());
        junit.framework.Assert.assertEquals("host:1", ((java.lang.String) (list.get(0).getPropertyValue(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("Hosts", "host_name")))));
        junit.framework.Assert.assertEquals(org.apache.ambari.server.controller.spi.Resource.Type.Host, list.get(0).getType());
        junit.framework.Assert.assertEquals("host:2", ((java.lang.String) (list.get(1).getPropertyValue(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("Hosts", "host_name")))));
        junit.framework.Assert.assertEquals(org.apache.ambari.server.controller.spi.Resource.Type.Host, list.get(1).getType());
        junit.framework.Assert.assertEquals(4, pageResponse.getTotalResourceCount().intValue());
        pageRequest = new org.apache.ambari.server.controller.internal.PageRequestImpl(org.apache.ambari.server.controller.spi.PageRequest.StartingPoint.OffsetStart, 3, 0, null, null);
        pageResponse = controller.getResources(org.apache.ambari.server.controller.spi.Resource.Type.Host, request, null, pageRequest, null);
        junit.framework.Assert.assertEquals(0, pageResponse.getOffset());
        junit.framework.Assert.assertNull(pageResponse.getPreviousResource());
        junit.framework.Assert.assertEquals("host:3", pageResponse.getNextResource().getPropertyValue(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("Hosts", "host_name")));
        iterable = pageResponse.getIterable();
        list = new java.util.LinkedList<>();
        for (org.apache.ambari.server.controller.spi.Resource resource : iterable) {
            list.add(resource);
        }
        junit.framework.Assert.assertEquals(3, list.size());
        junit.framework.Assert.assertEquals("host:0", ((java.lang.String) (list.get(0).getPropertyValue(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("Hosts", "host_name")))));
        junit.framework.Assert.assertEquals(org.apache.ambari.server.controller.spi.Resource.Type.Host, list.get(0).getType());
        junit.framework.Assert.assertEquals("host:1", ((java.lang.String) (list.get(1).getPropertyValue(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("Hosts", "host_name")))));
        junit.framework.Assert.assertEquals(org.apache.ambari.server.controller.spi.Resource.Type.Host, list.get(1).getType());
        junit.framework.Assert.assertEquals("host:2", ((java.lang.String) (list.get(2).getPropertyValue(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("Hosts", "host_name")))));
        junit.framework.Assert.assertEquals(org.apache.ambari.server.controller.spi.Resource.Type.Host, list.get(2).getType());
        junit.framework.Assert.assertEquals(4, pageResponse.getTotalResourceCount().intValue());
    }

    @org.junit.Test
    public void testGetResourcesPageToEnd() throws java.lang.Exception {
        org.apache.ambari.server.controller.internal.ClusterControllerImpl controller = new org.apache.ambari.server.controller.internal.ClusterControllerImpl(new org.apache.ambari.server.controller.internal.ClusterControllerImplTest.TestProviderModule());
        java.util.Set<java.lang.String> propertyIds = new java.util.HashSet<>();
        org.apache.ambari.server.controller.spi.Request request = org.apache.ambari.server.controller.utilities.PropertyHelper.getReadRequest(propertyIds);
        org.apache.ambari.server.controller.spi.PageRequest pageRequest = new org.apache.ambari.server.controller.internal.PageRequestImpl(org.apache.ambari.server.controller.spi.PageRequest.StartingPoint.End, 2, 0, null, null);
        org.apache.ambari.server.controller.spi.PageResponse pageResponse = controller.getResources(org.apache.ambari.server.controller.spi.Resource.Type.Host, request, null, pageRequest, null);
        java.lang.Iterable<org.apache.ambari.server.controller.spi.Resource> iterable = pageResponse.getIterable();
        java.util.List<org.apache.ambari.server.controller.spi.Resource> list = new java.util.LinkedList<>();
        for (org.apache.ambari.server.controller.spi.Resource resource : iterable) {
            list.add(resource);
        }
        junit.framework.Assert.assertEquals(2, list.size());
        junit.framework.Assert.assertEquals("host:2", ((java.lang.String) (list.get(0).getPropertyValue(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("Hosts", "host_name")))));
        junit.framework.Assert.assertEquals(org.apache.ambari.server.controller.spi.Resource.Type.Host, list.get(0).getType());
        junit.framework.Assert.assertEquals("host:3", ((java.lang.String) (list.get(1).getPropertyValue(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("Hosts", "host_name")))));
        junit.framework.Assert.assertEquals(org.apache.ambari.server.controller.spi.Resource.Type.Host, list.get(1).getType());
        junit.framework.Assert.assertEquals(4, pageResponse.getTotalResourceCount().intValue());
        pageRequest = new org.apache.ambari.server.controller.internal.PageRequestImpl(org.apache.ambari.server.controller.spi.PageRequest.StartingPoint.End, 3, 0, null, null);
        pageResponse = controller.getResources(org.apache.ambari.server.controller.spi.Resource.Type.Host, request, null, pageRequest, null);
        iterable = pageResponse.getIterable();
        list = new java.util.LinkedList<>();
        for (org.apache.ambari.server.controller.spi.Resource resource : iterable) {
            list.add(resource);
        }
        junit.framework.Assert.assertEquals(3, list.size());
        junit.framework.Assert.assertEquals("host:1", ((java.lang.String) (list.get(0).getPropertyValue(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("Hosts", "host_name")))));
        junit.framework.Assert.assertEquals(org.apache.ambari.server.controller.spi.Resource.Type.Host, list.get(0).getType());
        junit.framework.Assert.assertEquals("host:2", ((java.lang.String) (list.get(1).getPropertyValue(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("Hosts", "host_name")))));
        junit.framework.Assert.assertEquals(org.apache.ambari.server.controller.spi.Resource.Type.Host, list.get(1).getType());
        junit.framework.Assert.assertEquals("host:3", ((java.lang.String) (list.get(2).getPropertyValue(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("Hosts", "host_name")))));
        junit.framework.Assert.assertEquals(org.apache.ambari.server.controller.spi.Resource.Type.Host, list.get(2).getType());
        junit.framework.Assert.assertEquals(4, pageResponse.getTotalResourceCount().intValue());
    }

    @org.junit.Test
    public void testGetResourcesPageToOffset() throws java.lang.Exception {
        org.apache.ambari.server.controller.internal.ClusterControllerImpl controller = new org.apache.ambari.server.controller.internal.ClusterControllerImpl(new org.apache.ambari.server.controller.internal.ClusterControllerImplTest.TestProviderModule());
        java.util.Set<java.lang.String> propertyIds = new java.util.HashSet<>();
        org.apache.ambari.server.controller.spi.Request request = org.apache.ambari.server.controller.utilities.PropertyHelper.getReadRequest(propertyIds);
        org.apache.ambari.server.controller.spi.PageRequest pageRequest = new org.apache.ambari.server.controller.internal.PageRequestImpl(org.apache.ambari.server.controller.spi.PageRequest.StartingPoint.OffsetEnd, 2, 2, null, null);
        org.apache.ambari.server.controller.spi.PageResponse pageResponse = controller.getResources(org.apache.ambari.server.controller.spi.Resource.Type.Host, request, null, pageRequest, null);
        junit.framework.Assert.assertEquals(1, pageResponse.getOffset());
        junit.framework.Assert.assertEquals("host:0", pageResponse.getPreviousResource().getPropertyValue(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("Hosts", "host_name")));
        junit.framework.Assert.assertEquals("host:3", pageResponse.getNextResource().getPropertyValue(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("Hosts", "host_name")));
        junit.framework.Assert.assertEquals(4, pageResponse.getTotalResourceCount().intValue());
        java.lang.Iterable<org.apache.ambari.server.controller.spi.Resource> iterable = pageResponse.getIterable();
        java.util.List<org.apache.ambari.server.controller.spi.Resource> list = new java.util.LinkedList<>();
        for (org.apache.ambari.server.controller.spi.Resource resource : iterable) {
            list.add(resource);
        }
        junit.framework.Assert.assertEquals(2, list.size());
        junit.framework.Assert.assertEquals("host:1", ((java.lang.String) (list.get(0).getPropertyValue(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("Hosts", "host_name")))));
        junit.framework.Assert.assertEquals(org.apache.ambari.server.controller.spi.Resource.Type.Host, list.get(0).getType());
        junit.framework.Assert.assertEquals("host:2", ((java.lang.String) (list.get(1).getPropertyValue(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("Hosts", "host_name")))));
        junit.framework.Assert.assertEquals(org.apache.ambari.server.controller.spi.Resource.Type.Host, list.get(1).getType());
        pageRequest = new org.apache.ambari.server.controller.internal.PageRequestImpl(org.apache.ambari.server.controller.spi.PageRequest.StartingPoint.OffsetEnd, 3, 2, null, null);
        pageResponse = controller.getResources(org.apache.ambari.server.controller.spi.Resource.Type.Host, request, null, pageRequest, null);
        junit.framework.Assert.assertEquals(0, pageResponse.getOffset());
        junit.framework.Assert.assertNull(pageResponse.getPreviousResource());
        junit.framework.Assert.assertEquals("host:3", pageResponse.getNextResource().getPropertyValue(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("Hosts", "host_name")));
        iterable = pageResponse.getIterable();
        list = new java.util.LinkedList<>();
        for (org.apache.ambari.server.controller.spi.Resource resource : iterable) {
            list.add(resource);
        }
        junit.framework.Assert.assertEquals(3, list.size());
        junit.framework.Assert.assertEquals("host:0", ((java.lang.String) (list.get(0).getPropertyValue(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("Hosts", "host_name")))));
        junit.framework.Assert.assertEquals(org.apache.ambari.server.controller.spi.Resource.Type.Host, list.get(0).getType());
        junit.framework.Assert.assertEquals("host:1", ((java.lang.String) (list.get(1).getPropertyValue(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("Hosts", "host_name")))));
        junit.framework.Assert.assertEquals(org.apache.ambari.server.controller.spi.Resource.Type.Host, list.get(1).getType());
        junit.framework.Assert.assertEquals("host:2", ((java.lang.String) (list.get(2).getPropertyValue(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("Hosts", "host_name")))));
        junit.framework.Assert.assertEquals(org.apache.ambari.server.controller.spi.Resource.Type.Host, list.get(2).getType());
        junit.framework.Assert.assertEquals(4, pageResponse.getTotalResourceCount().intValue());
    }

    @org.junit.Test
    public void testGetResourcesEmptyRequest() throws java.lang.Exception {
        org.apache.ambari.server.controller.internal.ClusterControllerImpl controller = new org.apache.ambari.server.controller.internal.ClusterControllerImpl(new org.apache.ambari.server.controller.internal.ClusterControllerImplTest.TestProviderModule());
        java.util.Set<java.lang.String> propertyIds = new java.util.HashSet<>();
        org.apache.ambari.server.controller.spi.Request request = org.apache.ambari.server.controller.utilities.PropertyHelper.getReadRequest(propertyIds);
        java.lang.Iterable<org.apache.ambari.server.controller.spi.Resource> iterable = controller.getResourceIterable(org.apache.ambari.server.controller.spi.Resource.Type.Host, request, null);
        int cnt = 0;
        for (org.apache.ambari.server.controller.spi.Resource resource : iterable) {
            junit.framework.Assert.assertEquals(org.apache.ambari.server.controller.spi.Resource.Type.Host, resource.getType());
            ++cnt;
        }
        junit.framework.Assert.assertEquals(4, cnt);
    }

    @org.junit.Test
    public void testGetResourcesCheckOrder() throws java.lang.Exception {
        org.apache.ambari.server.controller.internal.ClusterControllerImpl controller = new org.apache.ambari.server.controller.internal.ClusterControllerImpl(new org.apache.ambari.server.controller.internal.ClusterControllerImplTest.TestProviderModule());
        java.util.Set<java.lang.String> propertyIds = new java.util.HashSet<>();
        org.apache.ambari.server.controller.spi.Request request = org.apache.ambari.server.controller.utilities.PropertyHelper.getReadRequest(propertyIds);
        java.lang.Iterable<org.apache.ambari.server.controller.spi.Resource> iterable = controller.getResourceIterable(org.apache.ambari.server.controller.spi.Resource.Type.Host, request, null);
        java.lang.String lastHostName = null;
        int cnt = 0;
        for (org.apache.ambari.server.controller.spi.Resource resource : iterable) {
            junit.framework.Assert.assertEquals(org.apache.ambari.server.controller.spi.Resource.Type.Host, resource.getType());
            java.lang.String hostName = ((java.lang.String) (resource.getPropertyValue(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("Hosts", "host_name"))));
            if (lastHostName != null) {
                junit.framework.Assert.assertTrue(hostName.compareTo(lastHostName) > 0);
            }
            lastHostName = hostName;
            ++cnt;
        }
        junit.framework.Assert.assertEquals(4, cnt);
    }

    @org.junit.Test
    public void testGetResourcesWithPredicate() throws java.lang.Exception {
        org.apache.ambari.server.controller.internal.ClusterControllerImpl controller = new org.apache.ambari.server.controller.internal.ClusterControllerImpl(new org.apache.ambari.server.controller.internal.ClusterControllerImplTest.TestProviderModule());
        java.util.Set<java.lang.String> propertyIds = new java.util.HashSet<>();
        propertyIds.add(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("c1", "p1"));
        propertyIds.add(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("c1", "p2"));
        propertyIds.add(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("c1", "p3"));
        propertyIds.add(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("c2", "p4"));
        org.apache.ambari.server.controller.spi.Request request = org.apache.ambari.server.controller.utilities.PropertyHelper.getReadRequest(propertyIds);
        org.apache.ambari.server.controller.spi.Predicate predicate = new org.apache.ambari.server.controller.utilities.PredicateBuilder().property("c1/p2").equals(1).toPredicate();
        java.lang.Iterable<org.apache.ambari.server.controller.spi.Resource> iterable = controller.getResourceIterable(org.apache.ambari.server.controller.spi.Resource.Type.Host, request, predicate);
        int cnt = 0;
        for (org.apache.ambari.server.controller.spi.Resource resource : iterable) {
            junit.framework.Assert.assertEquals(org.apache.ambari.server.controller.spi.Resource.Type.Host, resource.getType());
            ++cnt;
        }
        junit.framework.Assert.assertEquals(2, cnt);
    }

    @org.junit.Test
    public void testGetResourcesWithUnsupportedPropertyPredicate() throws java.lang.Exception {
        org.apache.ambari.server.controller.internal.ClusterControllerImpl controller = new org.apache.ambari.server.controller.internal.ClusterControllerImpl(new org.apache.ambari.server.controller.internal.ClusterControllerImplTest.TestProviderModule());
        java.util.Set<java.lang.String> propertyIds = new java.util.HashSet<>();
        propertyIds.add(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("c1", "p1"));
        propertyIds.add(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("c1", "p2"));
        propertyIds.add(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("c1", "p3"));
        propertyIds.add(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("c2", "p4"));
        org.apache.ambari.server.controller.spi.Request request = org.apache.ambari.server.controller.utilities.PropertyHelper.getReadRequest(propertyIds);
        org.apache.ambari.server.controller.spi.Predicate predicate = new org.apache.ambari.server.controller.utilities.PredicateBuilder().property(org.apache.ambari.server.controller.internal.ClusterControllerImplTest.UNSUPPORTED_PROPERTY).equals(1).toPredicate();
        try {
            controller.getResourceIterable(org.apache.ambari.server.controller.spi.Resource.Type.Host, request, predicate);
            junit.framework.Assert.fail("Expected an UnsupportedPropertyException for the unsupported properties.");
        } catch (org.apache.ambari.server.controller.spi.UnsupportedPropertyException e) {
        }
    }

    @org.junit.Test
    public void testGetResourcesWithUnsupportedPropertyRequest() throws java.lang.Exception {
        org.apache.ambari.server.controller.internal.ClusterControllerImpl controller = new org.apache.ambari.server.controller.internal.ClusterControllerImpl(new org.apache.ambari.server.controller.internal.ClusterControllerImplTest.TestProviderModule());
        java.util.Set<java.lang.String> propertyIds = new java.util.HashSet<>();
        propertyIds.add(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("c1", "p1"));
        propertyIds.add(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("c1", "p2"));
        propertyIds.add(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("c1", "p3"));
        propertyIds.add(org.apache.ambari.server.controller.internal.ClusterControllerImplTest.UNSUPPORTED_PROPERTY);
        org.apache.ambari.server.controller.spi.Request request = org.apache.ambari.server.controller.utilities.PropertyHelper.getReadRequest(propertyIds);
        org.apache.ambari.server.controller.spi.Predicate predicate = new org.apache.ambari.server.controller.utilities.PredicateBuilder().property("c1/p2").equals(1).toPredicate();
        try {
            controller.getResourceIterable(org.apache.ambari.server.controller.spi.Resource.Type.Host, request, predicate);
            junit.framework.Assert.fail("Expected an UnsupportedPropertyException for the unsupported properties.");
        } catch (org.apache.ambari.server.controller.spi.UnsupportedPropertyException e) {
        }
    }

    @org.junit.Test
    public void testGetResourcesSortedWithPredicateWithItemsTotal() throws java.lang.Exception {
        org.apache.ambari.server.controller.internal.ClusterControllerImpl controller = new org.apache.ambari.server.controller.internal.ClusterControllerImpl(new org.apache.ambari.server.controller.internal.ClusterControllerImplTest.TestProviderModule());
        java.util.Set<java.lang.String> propertyIds = new java.util.HashSet<>();
        org.apache.ambari.server.controller.spi.Request request = org.apache.ambari.server.controller.utilities.PropertyHelper.getReadRequest(propertyIds);
        org.apache.ambari.server.controller.spi.Predicate predicate = new org.apache.ambari.server.controller.utilities.PredicateBuilder().property("c1/p2").equals(1).toPredicate();
        java.util.List<org.apache.ambari.server.controller.spi.SortRequestProperty> sortRequestProperties = java.util.Collections.singletonList(new org.apache.ambari.server.controller.spi.SortRequestProperty("Hosts/host_name", org.apache.ambari.server.controller.spi.SortRequest.Order.DESC));
        org.apache.ambari.server.controller.spi.SortRequest sortRequest = new org.apache.ambari.server.controller.internal.SortRequestImpl(sortRequestProperties);
        org.apache.ambari.server.controller.spi.PageRequest pageRequest = new org.apache.ambari.server.controller.internal.PageRequestImpl(org.apache.ambari.server.controller.spi.PageRequest.StartingPoint.Beginning, 1, 0, null, null);
        org.apache.ambari.server.controller.spi.PageResponse pageResponse = controller.getResources(org.apache.ambari.server.controller.spi.Resource.Type.Host, request, predicate, pageRequest, sortRequest);
        java.lang.Iterable<org.apache.ambari.server.controller.spi.Resource> iterable = pageResponse.getIterable();
        java.util.List<org.apache.ambari.server.controller.spi.Resource> list = new java.util.LinkedList<>();
        for (org.apache.ambari.server.controller.spi.Resource resource : iterable) {
            list.add(resource);
        }
        junit.framework.Assert.assertEquals(1, list.size());
        junit.framework.Assert.assertEquals(2, pageResponse.getTotalResourceCount().intValue());
        junit.framework.Assert.assertEquals("host:3", ((java.lang.String) (list.get(0).getPropertyValue(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("Hosts", "host_name")))));
        junit.framework.Assert.assertEquals(org.apache.ambari.server.controller.spi.Resource.Type.Host, list.get(0).getType());
        pageRequest = new org.apache.ambari.server.controller.internal.PageRequestImpl(org.apache.ambari.server.controller.spi.PageRequest.StartingPoint.OffsetStart, 1, 1, null, null);
        pageResponse = controller.getResources(org.apache.ambari.server.controller.spi.Resource.Type.Host, request, predicate, pageRequest, sortRequest);
        iterable = pageResponse.getIterable();
        list.clear();
        for (org.apache.ambari.server.controller.spi.Resource resource : iterable) {
            list.add(resource);
        }
        junit.framework.Assert.assertEquals(1, list.size());
        junit.framework.Assert.assertEquals(2, pageResponse.getTotalResourceCount().intValue());
        junit.framework.Assert.assertEquals("host:1", ((java.lang.String) (list.get(0).getPropertyValue(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("Hosts", "host_name")))));
        junit.framework.Assert.assertEquals(org.apache.ambari.server.controller.spi.Resource.Type.Host, list.get(0).getType());
    }

    @org.junit.Test
    public void testCreateResources() throws java.lang.Exception {
        org.apache.ambari.server.controller.internal.ClusterControllerImplTest.TestProviderModule providerModule = new org.apache.ambari.server.controller.internal.ClusterControllerImplTest.TestProviderModule();
        org.apache.ambari.server.controller.internal.ClusterControllerImplTest.TestHostResourceProvider resourceProvider = ((org.apache.ambari.server.controller.internal.ClusterControllerImplTest.TestHostResourceProvider) (providerModule.getResourceProvider(org.apache.ambari.server.controller.spi.Resource.Type.Host)));
        org.apache.ambari.server.controller.spi.ClusterController controller = new org.apache.ambari.server.controller.internal.ClusterControllerImpl(providerModule);
        java.util.Set<java.util.Map<java.lang.String, java.lang.Object>> properties = new java.util.HashSet<>();
        java.util.Map<java.lang.String, java.lang.Object> propertyMap = new java.util.HashMap<>();
        propertyMap.put(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("c1", "p1"), 99);
        propertyMap.put(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("c1", "p2"), 2);
        properties.add(propertyMap);
        org.apache.ambari.server.controller.spi.Request request = org.apache.ambari.server.controller.utilities.PropertyHelper.getCreateRequest(properties, null);
        controller.createResources(org.apache.ambari.server.controller.spi.Resource.Type.Host, request);
        junit.framework.Assert.assertEquals(org.apache.ambari.server.controller.internal.ClusterControllerImplTest.TestHostResourceProvider.Action.Create, resourceProvider.getLastAction());
        junit.framework.Assert.assertSame(request, resourceProvider.getLastRequest());
        junit.framework.Assert.assertNull(resourceProvider.getLastPredicate());
    }

    @org.junit.Test
    public void testCreateResourcesWithUnsupportedProperty() throws java.lang.Exception {
        org.apache.ambari.server.controller.internal.ClusterControllerImplTest.TestProviderModule providerModule = new org.apache.ambari.server.controller.internal.ClusterControllerImplTest.TestProviderModule();
        org.apache.ambari.server.controller.spi.ClusterController controller = new org.apache.ambari.server.controller.internal.ClusterControllerImpl(providerModule);
        java.util.Set<java.util.Map<java.lang.String, java.lang.Object>> properties = new java.util.HashSet<>();
        java.util.Map<java.lang.String, java.lang.Object> propertyMap = new java.util.HashMap<>();
        propertyMap.put(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("c1", "p1"), 99);
        propertyMap.put(org.apache.ambari.server.controller.internal.ClusterControllerImplTest.UNSUPPORTED_PROPERTY, 2);
        properties.add(propertyMap);
        org.apache.ambari.server.controller.spi.Request request = org.apache.ambari.server.controller.utilities.PropertyHelper.getCreateRequest(properties, null);
        try {
            controller.createResources(org.apache.ambari.server.controller.spi.Resource.Type.Host, request);
            junit.framework.Assert.fail("Expected an UnsupportedPropertyException for the unsupported properties.");
        } catch (org.apache.ambari.server.controller.spi.UnsupportedPropertyException e) {
        }
    }

    @org.junit.Test
    public void testUpdateResources() throws java.lang.Exception {
        org.apache.ambari.server.controller.internal.ClusterControllerImplTest.TestProviderModule providerModule = new org.apache.ambari.server.controller.internal.ClusterControllerImplTest.TestProviderModule();
        org.apache.ambari.server.controller.internal.ClusterControllerImplTest.TestHostResourceProvider resourceProvider = ((org.apache.ambari.server.controller.internal.ClusterControllerImplTest.TestHostResourceProvider) (providerModule.getResourceProvider(org.apache.ambari.server.controller.spi.Resource.Type.Host)));
        org.apache.ambari.server.controller.spi.ClusterController controller = new org.apache.ambari.server.controller.internal.ClusterControllerImpl(providerModule);
        java.util.Map<java.lang.String, java.lang.Object> propertyMap = new java.util.HashMap<>();
        propertyMap.put(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("c1", "p1"), 99);
        propertyMap.put(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("c1", "p2"), 2);
        org.apache.ambari.server.controller.spi.Request request = org.apache.ambari.server.controller.utilities.PropertyHelper.getUpdateRequest(propertyMap, null);
        org.apache.ambari.server.controller.spi.Predicate predicate = new org.apache.ambari.server.controller.utilities.PredicateBuilder().property("c1/p2").equals(1).toPredicate();
        controller.updateResources(org.apache.ambari.server.controller.spi.Resource.Type.Host, request, predicate);
        junit.framework.Assert.assertEquals(org.apache.ambari.server.controller.internal.ClusterControllerImplTest.TestHostResourceProvider.Action.Update, resourceProvider.getLastAction());
        junit.framework.Assert.assertSame(request, resourceProvider.getLastRequest());
        junit.framework.Assert.assertSame(predicate, resourceProvider.getLastPredicate());
    }

    @org.junit.Test
    public void testUpdateResourcesWithUnsupportedPropertyRequest() throws java.lang.Exception {
        org.apache.ambari.server.controller.internal.ClusterControllerImplTest.TestProviderModule providerModule = new org.apache.ambari.server.controller.internal.ClusterControllerImplTest.TestProviderModule();
        org.apache.ambari.server.controller.spi.ClusterController controller = new org.apache.ambari.server.controller.internal.ClusterControllerImpl(providerModule);
        java.util.Map<java.lang.String, java.lang.Object> propertyMap = new java.util.HashMap<>();
        propertyMap.put(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("c1", "p1"), 99);
        propertyMap.put(org.apache.ambari.server.controller.internal.ClusterControllerImplTest.UNSUPPORTED_PROPERTY, 2);
        org.apache.ambari.server.controller.spi.Request request = org.apache.ambari.server.controller.utilities.PropertyHelper.getUpdateRequest(propertyMap, null);
        org.apache.ambari.server.controller.spi.Predicate predicate = new org.apache.ambari.server.controller.utilities.PredicateBuilder().property("c1/p2").equals(1).toPredicate();
        try {
            controller.updateResources(org.apache.ambari.server.controller.spi.Resource.Type.Host, request, predicate);
            junit.framework.Assert.fail("Expected an UnsupportedPropertyException for the unsupported properties.");
        } catch (org.apache.ambari.server.controller.spi.UnsupportedPropertyException e) {
        }
    }

    @org.junit.Test
    public void testUpdateResourcesWithUnsupportedPropertyPredicate() throws java.lang.Exception {
        org.apache.ambari.server.controller.internal.ClusterControllerImplTest.TestProviderModule providerModule = new org.apache.ambari.server.controller.internal.ClusterControllerImplTest.TestProviderModule();
        org.apache.ambari.server.controller.spi.ClusterController controller = new org.apache.ambari.server.controller.internal.ClusterControllerImpl(providerModule);
        java.util.Map<java.lang.String, java.lang.Object> propertyMap = new java.util.HashMap<>();
        propertyMap.put(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("c1", "p1"), 99);
        propertyMap.put(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("c1", "p2"), 2);
        org.apache.ambari.server.controller.spi.Request request = org.apache.ambari.server.controller.utilities.PropertyHelper.getUpdateRequest(propertyMap, null);
        org.apache.ambari.server.controller.spi.Predicate predicate = new org.apache.ambari.server.controller.utilities.PredicateBuilder().property(org.apache.ambari.server.controller.internal.ClusterControllerImplTest.UNSUPPORTED_PROPERTY).equals(1).toPredicate();
        try {
            controller.updateResources(org.apache.ambari.server.controller.spi.Resource.Type.Host, request, predicate);
            junit.framework.Assert.fail("Expected an UnsupportedPropertyException for the unsupported properties.");
        } catch (org.apache.ambari.server.controller.spi.UnsupportedPropertyException e) {
        }
    }

    @org.junit.Test
    public void testUpdateResourcesResolvePredicate() throws java.lang.Exception {
        org.apache.ambari.server.controller.internal.ClusterControllerImplTest.TestProviderModule providerModule = new org.apache.ambari.server.controller.internal.ClusterControllerImplTest.TestProviderModule();
        org.apache.ambari.server.controller.internal.ClusterControllerImplTest.TestHostResourceProvider resourceProvider = ((org.apache.ambari.server.controller.internal.ClusterControllerImplTest.TestHostResourceProvider) (providerModule.getResourceProvider(org.apache.ambari.server.controller.spi.Resource.Type.Host)));
        org.apache.ambari.server.controller.spi.ClusterController controller = new org.apache.ambari.server.controller.internal.ClusterControllerImpl(providerModule);
        java.util.Map<java.lang.String, java.lang.Object> propertyMap = new java.util.HashMap<>();
        propertyMap.put(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("c1", "p1"), 99);
        propertyMap.put(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("c1", "p2"), 2);
        org.apache.ambari.server.controller.spi.Request request = org.apache.ambari.server.controller.utilities.PropertyHelper.getUpdateRequest(propertyMap, null);
        org.apache.ambari.server.controller.spi.Predicate predicate = new org.apache.ambari.server.controller.utilities.PredicateBuilder().property("c3/p6").equals(1).toPredicate();
        controller.updateResources(org.apache.ambari.server.controller.spi.Resource.Type.Host, request, predicate);
        junit.framework.Assert.assertEquals(org.apache.ambari.server.controller.internal.ClusterControllerImplTest.TestHostResourceProvider.Action.Update, resourceProvider.getLastAction());
        junit.framework.Assert.assertSame(request, resourceProvider.getLastRequest());
        org.apache.ambari.server.controller.spi.Predicate lastPredicate = resourceProvider.getLastPredicate();
        junit.framework.Assert.assertFalse(predicate.equals(lastPredicate));
        java.util.Set<java.lang.String> predicatePropertyIds = org.apache.ambari.server.controller.utilities.PredicateHelper.getPropertyIds(lastPredicate);
        java.util.Collection<java.lang.String> keyPropertyIds = resourceProvider.getKeyPropertyIds().values();
        junit.framework.Assert.assertEquals(predicatePropertyIds.size(), keyPropertyIds.size());
        junit.framework.Assert.assertTrue(keyPropertyIds.containsAll(predicatePropertyIds));
    }

    @org.junit.Test
    public void testDeleteResources() throws java.lang.Exception {
        org.apache.ambari.server.controller.internal.ClusterControllerImplTest.TestProviderModule providerModule = new org.apache.ambari.server.controller.internal.ClusterControllerImplTest.TestProviderModule();
        org.apache.ambari.server.controller.internal.ClusterControllerImplTest.TestHostResourceProvider resourceProvider = ((org.apache.ambari.server.controller.internal.ClusterControllerImplTest.TestHostResourceProvider) (providerModule.getResourceProvider(org.apache.ambari.server.controller.spi.Resource.Type.Host)));
        org.apache.ambari.server.controller.spi.ClusterController controller = new org.apache.ambari.server.controller.internal.ClusterControllerImpl(providerModule);
        org.apache.ambari.server.controller.spi.Predicate predicate = new org.apache.ambari.server.controller.utilities.PredicateBuilder().property("c1/p2").equals(1).toPredicate();
        controller.deleteResources(org.apache.ambari.server.controller.spi.Resource.Type.Host, new org.apache.ambari.server.controller.internal.RequestImpl(null, null, null, null), predicate);
        junit.framework.Assert.assertEquals(org.apache.ambari.server.controller.internal.ClusterControllerImplTest.TestHostResourceProvider.Action.Delete, resourceProvider.getLastAction());
        junit.framework.Assert.assertNull(resourceProvider.getLastRequest());
        junit.framework.Assert.assertSame(predicate, resourceProvider.getLastPredicate());
    }

    @org.junit.Test
    public void testDeleteResourcesWithUnsupportedProperty() throws java.lang.Exception {
        org.apache.ambari.server.controller.internal.ClusterControllerImplTest.TestProviderModule providerModule = new org.apache.ambari.server.controller.internal.ClusterControllerImplTest.TestProviderModule();
        org.apache.ambari.server.controller.spi.ClusterController controller = new org.apache.ambari.server.controller.internal.ClusterControllerImpl(providerModule);
        org.apache.ambari.server.controller.spi.Predicate predicate = new org.apache.ambari.server.controller.utilities.PredicateBuilder().property(org.apache.ambari.server.controller.internal.ClusterControllerImplTest.UNSUPPORTED_PROPERTY).equals(1).toPredicate();
        try {
            controller.deleteResources(org.apache.ambari.server.controller.spi.Resource.Type.Host, new org.apache.ambari.server.controller.internal.RequestImpl(null, null, null, null), predicate);
            junit.framework.Assert.fail("Expected an UnsupportedPropertyException for the unsupported properties.");
        } catch (org.apache.ambari.server.controller.spi.UnsupportedPropertyException e) {
        }
    }

    @org.junit.Test
    public void testDeleteResourcesResolvePredicate() throws java.lang.Exception {
        org.apache.ambari.server.controller.internal.ClusterControllerImplTest.TestProviderModule providerModule = new org.apache.ambari.server.controller.internal.ClusterControllerImplTest.TestProviderModule();
        org.apache.ambari.server.controller.internal.ClusterControllerImplTest.TestHostResourceProvider resourceProvider = ((org.apache.ambari.server.controller.internal.ClusterControllerImplTest.TestHostResourceProvider) (providerModule.getResourceProvider(org.apache.ambari.server.controller.spi.Resource.Type.Host)));
        org.apache.ambari.server.controller.spi.ClusterController controller = new org.apache.ambari.server.controller.internal.ClusterControllerImpl(providerModule);
        org.apache.ambari.server.controller.spi.Predicate predicate = new org.apache.ambari.server.controller.utilities.PredicateBuilder().property("c3/p6").equals(1).toPredicate();
        controller.deleteResources(org.apache.ambari.server.controller.spi.Resource.Type.Host, new org.apache.ambari.server.controller.internal.RequestImpl(null, null, null, null), predicate);
        junit.framework.Assert.assertEquals(org.apache.ambari.server.controller.internal.ClusterControllerImplTest.TestHostResourceProvider.Action.Delete, resourceProvider.getLastAction());
        junit.framework.Assert.assertNull(resourceProvider.getLastRequest());
        org.apache.ambari.server.controller.spi.Predicate lastPredicate = resourceProvider.getLastPredicate();
        junit.framework.Assert.assertFalse(predicate.equals(lastPredicate));
        java.util.Set<java.lang.String> predicatePropertyIds = org.apache.ambari.server.controller.utilities.PredicateHelper.getPropertyIds(lastPredicate);
        java.util.Collection<java.lang.String> keyPropertyIds = resourceProvider.getKeyPropertyIds().values();
        junit.framework.Assert.assertEquals(predicatePropertyIds.size(), keyPropertyIds.size());
        junit.framework.Assert.assertTrue(keyPropertyIds.containsAll(predicatePropertyIds));
    }

    @org.junit.Test
    public void testComparator() {
        org.apache.ambari.server.controller.internal.ClusterControllerImplTest.TestProviderModule providerModule = new org.apache.ambari.server.controller.internal.ClusterControllerImplTest.TestProviderModule();
        org.apache.ambari.server.controller.internal.ClusterControllerImpl controller = new org.apache.ambari.server.controller.internal.ClusterControllerImpl(providerModule);
        java.util.Comparator<org.apache.ambari.server.controller.spi.Resource> comparator = controller.getComparator();
        org.apache.ambari.server.controller.spi.Resource resource1 = new org.apache.ambari.server.controller.internal.ResourceImpl(org.apache.ambari.server.controller.spi.Resource.Type.Host);
        org.apache.ambari.server.controller.spi.Resource resource2 = new org.apache.ambari.server.controller.internal.ResourceImpl(org.apache.ambari.server.controller.spi.Resource.Type.Host);
        org.apache.ambari.server.controller.spi.Resource resource3 = new org.apache.ambari.server.controller.internal.ResourceImpl(org.apache.ambari.server.controller.spi.Resource.Type.Service);
        junit.framework.Assert.assertEquals(0, comparator.compare(resource1, resource2));
        junit.framework.Assert.assertEquals(0, comparator.compare(resource2, resource1));
        junit.framework.Assert.assertTrue(comparator.compare(resource1, resource3) < 0);
        junit.framework.Assert.assertTrue(comparator.compare(resource3, resource1) > 0);
        resource1.setProperty(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("Hosts", "cluster_name"), "c1");
        resource1.setProperty(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("Hosts", "host_name"), "h1");
        resource2.setProperty(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("Hosts", "cluster_name"), "c1");
        resource2.setProperty(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("Hosts", "host_name"), "h1");
        junit.framework.Assert.assertEquals(0, comparator.compare(resource1, resource2));
        junit.framework.Assert.assertEquals(0, comparator.compare(resource2, resource1));
        resource2.setProperty(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("Hosts", "host_name"), "h2");
        junit.framework.Assert.assertTrue(comparator.compare(resource1, resource2) < 0);
        junit.framework.Assert.assertTrue(comparator.compare(resource2, resource1) > 0);
        resource2.setProperty(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("Hosts", "host_name"), "h1");
        resource1.setProperty("p1", "foo");
        resource2.setProperty("p1", "foo");
        junit.framework.Assert.assertEquals(0, comparator.compare(resource1, resource2));
        junit.framework.Assert.assertEquals(0, comparator.compare(resource2, resource1));
        resource2.setProperty("p1", "bar");
        junit.framework.Assert.assertFalse(comparator.compare(resource1, resource2) == 0);
        junit.framework.Assert.assertFalse(comparator.compare(resource2, resource1) == 0);
    }

    @org.junit.Test
    public void testPopulateResources_allTypes() throws java.lang.Exception {
        org.apache.ambari.server.controller.internal.ClusterControllerImplTest.TestProviderModule providerModule = new org.apache.ambari.server.controller.internal.ClusterControllerImplTest.TestProviderModule();
        org.apache.ambari.server.controller.internal.ClusterControllerImpl controller = new org.apache.ambari.server.controller.internal.ClusterControllerImpl(providerModule);
        org.apache.ambari.server.controller.spi.Request request = org.apache.ambari.server.controller.utilities.PropertyHelper.getReadRequest();
        org.apache.ambari.server.controller.spi.Predicate predicate = new org.apache.ambari.server.controller.utilities.PredicateBuilder().property("c3/p6").equals(1).toPredicate();
        for (org.apache.ambari.server.controller.spi.Resource.Type type : org.apache.ambari.server.controller.spi.Resource.Type.values()) {
            org.apache.ambari.server.controller.spi.Resource resource = new org.apache.ambari.server.controller.internal.ResourceImpl(type);
            controller.populateResources(type, java.util.Collections.singleton(resource), request, predicate);
        }
    }

    @org.junit.Test
    public void testResourceProviderResponse() throws java.lang.Exception {
        org.apache.ambari.server.controller.spi.ProviderModule providerModule = EasyMock.createNiceMock(org.apache.ambari.server.controller.spi.ProviderModule.class);
        org.apache.ambari.server.controller.spi.ResourceProvider resourceProvider = EasyMock.createNiceMock(org.apache.ambari.server.controller.spi.ResourceProvider.class);
        EasyMock.expect(providerModule.getResourceProvider(org.apache.ambari.server.controller.spi.Resource.Type.AlertHistory)).andReturn(resourceProvider).anyTimes();
        EasyMock.expect(resourceProvider.checkPropertyIds(java.util.Collections.singleton(org.apache.ambari.server.controller.internal.AlertHistoryResourceProvider.ALERT_HISTORY_HOSTNAME))).andReturn(java.util.Collections.emptySet()).anyTimes();
        EasyMock.expect(resourceProvider.getResources(EasyMock.anyObject(org.apache.ambari.server.controller.spi.Request.class), EasyMock.anyObject(org.apache.ambari.server.controller.spi.Predicate.class))).andReturn(java.util.Collections.emptySet()).anyTimes();
        EasyMock.expect(resourceProvider.checkPropertyIds(org.easymock.EasyMock.anyObject())).andReturn(new java.util.HashSet<>()).anyTimes();
        org.apache.ambari.server.controller.spi.PageRequest pageRequest = org.easymock.EasyMock.createStrictMock(org.apache.ambari.server.controller.spi.PageRequest.class);
        org.apache.ambari.server.controller.spi.SortRequest sortRequest = org.easymock.EasyMock.createStrictMock(org.apache.ambari.server.controller.spi.SortRequest.class);
        EasyMock.expect(sortRequest.getPropertyIds()).andReturn(new java.util.ArrayList<>()).atLeastOnce();
        EasyMock.replay(providerModule, resourceProvider, pageRequest, sortRequest);
        org.apache.ambari.server.controller.internal.ClusterControllerImpl controller = new org.apache.ambari.server.controller.internal.ClusterControllerImpl(providerModule);
        java.util.Set<java.lang.String> propertyIds = new java.util.HashSet<>();
        propertyIds.add(org.apache.ambari.server.controller.internal.AlertHistoryResourceProvider.ALERT_HISTORY_HOSTNAME);
        java.util.Set<org.apache.ambari.server.controller.spi.Resource> providerResources = new java.util.LinkedHashSet<>();
        providerResources.add(new org.apache.ambari.server.controller.internal.ResourceImpl(org.apache.ambari.server.controller.spi.Resource.Type.AlertHistory));
        org.apache.ambari.server.controller.spi.Request request = org.apache.ambari.server.controller.utilities.PropertyHelper.getReadRequest(propertyIds, null, null, pageRequest, sortRequest);
        org.apache.ambari.server.controller.spi.Predicate predicate = new org.apache.ambari.server.controller.utilities.PredicateBuilder().property(org.apache.ambari.server.controller.internal.AlertHistoryResourceProvider.ALERT_HISTORY_HOSTNAME).equals("c6401.ambari.apache.org").toPredicate();
        org.apache.ambari.server.controller.spi.PageResponse pageResponse = controller.getPage(org.apache.ambari.server.controller.spi.Resource.Type.AlertHistory, new org.apache.ambari.server.controller.internal.QueryResponseImpl(providerResources, true, true, 0), request, predicate, pageRequest, sortRequest);
        EasyMock.verify(providerModule, resourceProvider, pageRequest, sortRequest);
    }

    public static class TestProviderModule implements org.apache.ambari.server.controller.spi.ProviderModule {
        private java.util.Map<org.apache.ambari.server.controller.spi.Resource.Type, org.apache.ambari.server.controller.spi.ResourceProvider> providers = new java.util.HashMap<>();

        public TestProviderModule() {
            for (org.apache.ambari.server.controller.spi.Resource.Type type : org.apache.ambari.server.controller.spi.Resource.Type.values()) {
                providers.put(type, new org.apache.ambari.server.controller.internal.ClusterControllerImplTest.TestResourceProvider());
            }
            providers.put(org.apache.ambari.server.controller.spi.Resource.Type.Cluster, new org.apache.ambari.server.controller.internal.ClusterControllerImplTest.TestClusterResourceProvider());
            providers.put(org.apache.ambari.server.controller.spi.Resource.Type.Host, new org.apache.ambari.server.controller.internal.ClusterControllerImplTest.TestHostResourceProvider());
            providers.put(org.apache.ambari.server.controller.spi.Resource.Type.Stack, new org.apache.ambari.server.controller.internal.ClusterControllerImplTest.TestStackResourceProvider());
            providers.put(org.apache.ambari.server.controller.spi.Resource.Type.StackVersion, new org.apache.ambari.server.controller.internal.ClusterControllerImplTest.TestStackVersionResourceProvider());
            providers.put(org.apache.ambari.server.controller.spi.Resource.Type.OperatingSystem, new org.apache.ambari.server.controller.internal.ClusterControllerImplTest.TestOperatingSystemResourceProvider());
            providers.put(org.apache.ambari.server.controller.spi.Resource.Type.Repository, new org.apache.ambari.server.controller.internal.ClusterControllerImplTest.TestRepositoryResourceProvider());
            providers.put(org.apache.ambari.server.controller.spi.Resource.Type.RepositoryVersion, new org.apache.ambari.server.controller.internal.ClusterControllerImplTest.TestRepositoryVersionResourceProvider());
            providers.put(org.apache.ambari.server.controller.spi.Resource.Type.CompatibleRepositoryVersion, new org.apache.ambari.server.controller.internal.ClusterControllerImplTest.TestCompatibleRepositoryVersionResourceProvider());
            providers.put(org.apache.ambari.server.controller.spi.Resource.Type.StackArtifact, new org.apache.ambari.server.controller.internal.ClusterControllerImplTest.TestStackArtifactResourceProvider());
        }

        @java.lang.Override
        public org.apache.ambari.server.controller.spi.ResourceProvider getResourceProvider(org.apache.ambari.server.controller.spi.Resource.Type type) {
            return providers.get(type);
        }

        @java.lang.Override
        public java.util.List<org.apache.ambari.server.controller.spi.PropertyProvider> getPropertyProviders(org.apache.ambari.server.controller.spi.Resource.Type type) {
            if (type.equals(org.apache.ambari.server.controller.spi.Resource.Type.Configuration)) {
                return null;
            }
            return org.apache.ambari.server.controller.internal.ClusterControllerImplTest.propertyProviders;
        }
    }

    private static class TestResourceProvider extends org.apache.ambari.server.controller.internal.AbstractResourceProvider {
        private TestResourceProvider() {
            super(new java.util.HashSet<>(), new java.util.HashMap<>());
        }

        private TestResourceProvider(java.util.Set<java.lang.String> propertyIds, java.util.Map<org.apache.ambari.server.controller.spi.Resource.Type, java.lang.String> keyPropertyIds) {
            super(propertyIds, keyPropertyIds);
        }

        @java.lang.Override
        public org.apache.ambari.server.controller.spi.RequestStatus createResources(org.apache.ambari.server.controller.spi.Request request) throws org.apache.ambari.server.controller.spi.SystemException, org.apache.ambari.server.controller.spi.UnsupportedPropertyException, org.apache.ambari.server.controller.spi.ResourceAlreadyExistsException, org.apache.ambari.server.controller.spi.NoSuchParentResourceException {
            throw new java.lang.UnsupportedOperationException();
        }

        @java.lang.Override
        public java.util.Set<org.apache.ambari.server.controller.spi.Resource> getResources(org.apache.ambari.server.controller.spi.Request request, org.apache.ambari.server.controller.spi.Predicate predicate) throws org.apache.ambari.server.controller.spi.SystemException, org.apache.ambari.server.controller.spi.UnsupportedPropertyException, org.apache.ambari.server.controller.spi.NoSuchResourceException, org.apache.ambari.server.controller.spi.NoSuchParentResourceException {
            return java.util.Collections.emptySet();
        }

        @java.lang.Override
        public org.apache.ambari.server.controller.spi.RequestStatus updateResources(org.apache.ambari.server.controller.spi.Request request, org.apache.ambari.server.controller.spi.Predicate predicate) throws org.apache.ambari.server.controller.spi.SystemException, org.apache.ambari.server.controller.spi.UnsupportedPropertyException, org.apache.ambari.server.controller.spi.NoSuchResourceException, org.apache.ambari.server.controller.spi.NoSuchParentResourceException {
            throw new java.lang.UnsupportedOperationException();
        }

        @java.lang.Override
        public org.apache.ambari.server.controller.spi.RequestStatus deleteResources(org.apache.ambari.server.controller.spi.Request request, org.apache.ambari.server.controller.spi.Predicate predicate) throws org.apache.ambari.server.controller.spi.SystemException, org.apache.ambari.server.controller.spi.UnsupportedPropertyException, org.apache.ambari.server.controller.spi.NoSuchResourceException, org.apache.ambari.server.controller.spi.NoSuchParentResourceException {
            throw new java.lang.UnsupportedOperationException();
        }

        @java.lang.Override
        protected java.util.Set<java.lang.String> getPKPropertyIds() {
            return java.util.Collections.emptySet();
        }

        protected java.util.Set<org.apache.ambari.server.controller.spi.Resource> getResources(org.apache.ambari.server.controller.spi.Resource.Type type, org.apache.ambari.server.controller.spi.Predicate predicate, java.lang.String keyPropertyId, java.util.Set<java.lang.String> keyPropertyValues) throws org.apache.ambari.server.controller.spi.SystemException, org.apache.ambari.server.controller.spi.UnsupportedPropertyException, org.apache.ambari.server.controller.spi.NoSuchParentResourceException, org.apache.ambari.server.controller.spi.NoSuchResourceException {
            java.util.Set<org.apache.ambari.server.controller.spi.Resource> resources = new java.util.HashSet<>();
            for (java.util.Map<java.lang.String, java.lang.Object> propertyMap : getPropertyMaps(predicate)) {
                java.util.Set<org.apache.ambari.server.controller.spi.Resource> resources2 = new java.util.HashSet<>();
                if (!propertyMap.containsKey(keyPropertyId)) {
                    for (java.lang.String keyPropertyValue : keyPropertyValues) {
                        org.apache.ambari.server.controller.internal.ResourceImpl resource = new org.apache.ambari.server.controller.internal.ResourceImpl(type);
                        resource.setProperty(keyPropertyId, keyPropertyValue);
                        resources2.add(resource);
                    }
                } else {
                    resources2.add(new org.apache.ambari.server.controller.internal.ResourceImpl(type));
                }
                for (org.apache.ambari.server.controller.spi.Resource resource : resources2) {
                    for (java.util.Map.Entry<java.lang.String, java.lang.Object> entry : propertyMap.entrySet()) {
                        resource.setProperty(entry.getKey(), entry.getValue());
                    }
                }
                resources.addAll(resources2);
            }
            return resources;
        }
    }

    private static class TestClusterResourceProvider extends org.apache.ambari.server.controller.internal.ClusterControllerImplTest.TestResourceProvider {
        private TestClusterResourceProvider() {
            super(org.apache.ambari.server.controller.internal.ClusterResourceProvider.propertyIds, org.apache.ambari.server.controller.internal.ClusterResourceProvider.keyPropertyIds);
        }

        @java.lang.Override
        public java.util.Set<org.apache.ambari.server.controller.spi.Resource> getResources(org.apache.ambari.server.controller.spi.Request request, org.apache.ambari.server.controller.spi.Predicate predicate) throws org.apache.ambari.server.controller.spi.SystemException, org.apache.ambari.server.controller.spi.UnsupportedPropertyException, org.apache.ambari.server.controller.spi.NoSuchResourceException, org.apache.ambari.server.controller.spi.NoSuchParentResourceException {
            org.apache.ambari.server.controller.internal.ResourceImpl resource = new org.apache.ambari.server.controller.internal.ResourceImpl(org.apache.ambari.server.controller.spi.Resource.Type.Cluster);
            resource.setProperty(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("Clusters", "cluster_name"), "cluster");
            return java.util.Collections.singleton(resource);
        }
    }

    private static class TestHostResourceProvider extends org.apache.ambari.server.controller.internal.ClusterControllerImplTest.TestResourceProvider {
        private org.apache.ambari.server.controller.internal.ClusterControllerImplTest.TestHostResourceProvider.Action lastAction = null;

        private org.apache.ambari.server.controller.spi.Request lastRequest = null;

        private org.apache.ambari.server.controller.spi.Predicate lastPredicate = null;

        private TestHostResourceProvider() {
            super(org.apache.ambari.server.controller.internal.HostResourceProvider.propertyIds, org.apache.ambari.server.controller.internal.HostResourceProvider.keyPropertyIds);
        }

        @java.lang.Override
        public java.util.Set<org.apache.ambari.server.controller.spi.Resource> getResources(org.apache.ambari.server.controller.spi.Request request, org.apache.ambari.server.controller.spi.Predicate predicate) throws org.apache.ambari.server.controller.spi.SystemException, org.apache.ambari.server.controller.spi.UnsupportedPropertyException, org.apache.ambari.server.controller.spi.NoSuchResourceException, org.apache.ambari.server.controller.spi.NoSuchParentResourceException {
            java.util.Set<org.apache.ambari.server.controller.spi.Resource> resources = new java.util.HashSet<>();
            for (int cnt = 0; cnt < 4; ++cnt) {
                org.apache.ambari.server.controller.internal.ResourceImpl resource = new org.apache.ambari.server.controller.internal.ResourceImpl(org.apache.ambari.server.controller.spi.Resource.Type.Host);
                resource.setProperty(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("Hosts", "cluster_name"), "cluster");
                resource.setProperty(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("Hosts", "host_name"), "host:" + (4 - cnt));
                resource.setProperty(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("Hosts", "cluster_name"), "cluster");
                resource.setProperty(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("Hosts", "host_name"), "host:" + cnt);
                resource.setProperty(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("c1", "p1"), cnt);
                resource.setProperty(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("c1", "p2"), cnt % 2);
                resource.setProperty(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("c1", "p3"), "foo");
                resource.setProperty(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("c2", "p4"), "bar");
                if ((cnt % 2) == 0) {
                    resource.setProperty(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("alerts_summary", "CRITICAL"), "1");
                } else {
                    resource.setProperty(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("alerts_summary", "WARNING"), "1");
                }
                resources.add(resource);
            }
            return resources;
        }

        @java.lang.Override
        public org.apache.ambari.server.controller.spi.RequestStatus createResources(org.apache.ambari.server.controller.spi.Request request) {
            lastAction = org.apache.ambari.server.controller.internal.ClusterControllerImplTest.TestHostResourceProvider.Action.Create;
            lastRequest = request;
            lastPredicate = null;
            return new org.apache.ambari.server.controller.internal.RequestStatusImpl(null);
        }

        @java.lang.Override
        public org.apache.ambari.server.controller.spi.RequestStatus updateResources(org.apache.ambari.server.controller.spi.Request request, org.apache.ambari.server.controller.spi.Predicate predicate) throws org.apache.ambari.server.controller.spi.SystemException, org.apache.ambari.server.controller.spi.UnsupportedPropertyException, org.apache.ambari.server.controller.spi.NoSuchResourceException, org.apache.ambari.server.controller.spi.NoSuchParentResourceException {
            lastAction = org.apache.ambari.server.controller.internal.ClusterControllerImplTest.TestHostResourceProvider.Action.Update;
            lastRequest = request;
            lastPredicate = predicate;
            return new org.apache.ambari.server.controller.internal.RequestStatusImpl(null);
        }

        @java.lang.Override
        public org.apache.ambari.server.controller.spi.RequestStatus deleteResources(org.apache.ambari.server.controller.spi.Request request, org.apache.ambari.server.controller.spi.Predicate predicate) throws org.apache.ambari.server.controller.spi.SystemException, org.apache.ambari.server.controller.spi.UnsupportedPropertyException, org.apache.ambari.server.controller.spi.NoSuchResourceException, org.apache.ambari.server.controller.spi.NoSuchParentResourceException {
            lastAction = org.apache.ambari.server.controller.internal.ClusterControllerImplTest.TestHostResourceProvider.Action.Delete;
            lastRequest = null;
            lastPredicate = predicate;
            return new org.apache.ambari.server.controller.internal.RequestStatusImpl(null);
        }

        @java.lang.Override
        public java.util.Set<java.lang.String> checkPropertyIds(java.util.Set<java.lang.String> propertyIds) {
            if (!org.apache.ambari.server.controller.internal.ClusterControllerImplTest.resourceProviderProperties.containsAll(propertyIds)) {
                java.util.Set<java.lang.String> unsupportedPropertyIds = new java.util.HashSet<>(propertyIds);
                unsupportedPropertyIds.removeAll(org.apache.ambari.server.controller.internal.ClusterControllerImplTest.resourceProviderProperties);
                return unsupportedPropertyIds;
            }
            return java.util.Collections.emptySet();
        }

        @java.lang.Override
        public java.util.Map<org.apache.ambari.server.controller.spi.Resource.Type, java.lang.String> getKeyPropertyIds() {
            return keyPropertyIds;
        }

        public org.apache.ambari.server.controller.internal.ClusterControllerImplTest.TestHostResourceProvider.Action getLastAction() {
            return lastAction;
        }

        public org.apache.ambari.server.controller.spi.Request getLastRequest() {
            return lastRequest;
        }

        public org.apache.ambari.server.controller.spi.Predicate getLastPredicate() {
            return lastPredicate;
        }

        public enum Action {

            Create,
            Update,
            Delete;}
    }

    private static class TestStackResourceProvider extends org.apache.ambari.server.controller.internal.ClusterControllerImplTest.TestResourceProvider {
        private TestStackResourceProvider() {
            super(org.apache.ambari.server.controller.internal.StackResourceProvider.propertyIds, org.apache.ambari.server.controller.internal.StackResourceProvider.keyPropertyIds);
        }

        @java.lang.Override
        public java.util.Set<org.apache.ambari.server.controller.spi.Resource> getResources(org.apache.ambari.server.controller.spi.Request request, org.apache.ambari.server.controller.spi.Predicate predicate) throws org.apache.ambari.server.controller.spi.SystemException, org.apache.ambari.server.controller.spi.UnsupportedPropertyException, org.apache.ambari.server.controller.spi.NoSuchResourceException, org.apache.ambari.server.controller.spi.NoSuchParentResourceException {
            java.util.Set<java.lang.String> keyPropertyValues = new java.util.HashSet<>();
            return getResources(org.apache.ambari.server.controller.spi.Resource.Type.Stack, predicate, "Stacks/stack_name", keyPropertyValues);
        }
    }

    private static class TestStackVersionResourceProvider extends org.apache.ambari.server.controller.internal.ClusterControllerImplTest.TestResourceProvider {
        private TestStackVersionResourceProvider() {
            super(org.apache.ambari.server.controller.internal.StackVersionResourceProvider.PROPERTY_IDS, org.apache.ambari.server.controller.internal.StackVersionResourceProvider.KEY_PROPERTY_IDS);
        }

        @java.lang.Override
        public java.util.Set<org.apache.ambari.server.controller.spi.Resource> getResources(org.apache.ambari.server.controller.spi.Request request, org.apache.ambari.server.controller.spi.Predicate predicate) throws org.apache.ambari.server.controller.spi.SystemException, org.apache.ambari.server.controller.spi.UnsupportedPropertyException, org.apache.ambari.server.controller.spi.NoSuchResourceException, org.apache.ambari.server.controller.spi.NoSuchParentResourceException {
            java.util.Set<java.lang.String> keyPropertyValues = new java.util.LinkedHashSet<>();
            keyPropertyValues.add("1.2.1");
            keyPropertyValues.add("1.2.2");
            keyPropertyValues.add("2.0.1");
            return getResources(org.apache.ambari.server.controller.spi.Resource.Type.StackVersion, predicate, "Versions/stack_version", keyPropertyValues);
        }
    }

    private static class TestOperatingSystemResourceProvider extends org.apache.ambari.server.controller.internal.ClusterControllerImplTest.TestResourceProvider {
        private TestOperatingSystemResourceProvider() {
            super(org.apache.ambari.server.controller.internal.OperatingSystemResourceProvider.propertyIds, org.apache.ambari.server.controller.internal.OperatingSystemResourceProvider.keyPropertyIds);
        }

        @java.lang.Override
        public java.util.Set<org.apache.ambari.server.controller.spi.Resource> getResources(org.apache.ambari.server.controller.spi.Request request, org.apache.ambari.server.controller.spi.Predicate predicate) throws org.apache.ambari.server.controller.spi.SystemException, org.apache.ambari.server.controller.spi.UnsupportedPropertyException, org.apache.ambari.server.controller.spi.NoSuchResourceException, org.apache.ambari.server.controller.spi.NoSuchParentResourceException {
            java.util.Set<java.lang.String> keyPropertyValues = new java.util.LinkedHashSet<>();
            keyPropertyValues.add("centos5");
            keyPropertyValues.add("centos6");
            keyPropertyValues.add("oraclelinux5");
            return getResources(org.apache.ambari.server.controller.spi.Resource.Type.OperatingSystem, predicate, "OperatingSystems/os_type", keyPropertyValues);
        }
    }

    private static class TestRepositoryResourceProvider extends org.apache.ambari.server.controller.internal.ClusterControllerImplTest.TestResourceProvider {
        private TestRepositoryResourceProvider() {
            super(org.apache.ambari.server.controller.internal.RepositoryResourceProvider.propertyIds, org.apache.ambari.server.controller.internal.RepositoryResourceProvider.keyPropertyIds);
        }

        @java.lang.Override
        public java.util.Set<org.apache.ambari.server.controller.spi.Resource> getResources(org.apache.ambari.server.controller.spi.Request request, org.apache.ambari.server.controller.spi.Predicate predicate) throws org.apache.ambari.server.controller.spi.SystemException, org.apache.ambari.server.controller.spi.UnsupportedPropertyException, org.apache.ambari.server.controller.spi.NoSuchResourceException, org.apache.ambari.server.controller.spi.NoSuchParentResourceException {
            java.util.Set<java.lang.String> keyPropertyValues = new java.util.LinkedHashSet<>();
            keyPropertyValues.add("repo1");
            keyPropertyValues.add("repo2");
            return getResources(org.apache.ambari.server.controller.spi.Resource.Type.Repository, predicate, "Repositories/repo_id", keyPropertyValues);
        }
    }

    private static class TestRepositoryVersionResourceProvider extends org.apache.ambari.server.controller.internal.ClusterControllerImplTest.TestResourceProvider {
        private TestRepositoryVersionResourceProvider() {
            super(org.apache.ambari.server.controller.internal.RepositoryVersionResourceProvider.propertyIds, org.apache.ambari.server.controller.internal.RepositoryVersionResourceProvider.keyPropertyIds);
        }

        @java.lang.Override
        public java.util.Set<org.apache.ambari.server.controller.spi.Resource> getResources(org.apache.ambari.server.controller.spi.Request request, org.apache.ambari.server.controller.spi.Predicate predicate) throws org.apache.ambari.server.controller.spi.SystemException, org.apache.ambari.server.controller.spi.UnsupportedPropertyException, org.apache.ambari.server.controller.spi.NoSuchResourceException, org.apache.ambari.server.controller.spi.NoSuchParentResourceException {
            java.util.Set<java.lang.String> keyPropertyValues = new java.util.LinkedHashSet<>();
            keyPropertyValues.add("1");
            keyPropertyValues.add("2");
            return getResources(org.apache.ambari.server.controller.spi.Resource.Type.RepositoryVersion, predicate, "RepositoriVersions/id", keyPropertyValues);
        }
    }

    private static class TestCompatibleRepositoryVersionResourceProvider extends org.apache.ambari.server.controller.internal.ClusterControllerImplTest.TestResourceProvider {
        private TestCompatibleRepositoryVersionResourceProvider() {
            super(org.apache.ambari.server.controller.internal.CompatibleRepositoryVersionResourceProvider.propertyIds, org.apache.ambari.server.controller.internal.CompatibleRepositoryVersionResourceProvider.keyPropertyIds);
        }

        @java.lang.Override
        public java.util.Set<org.apache.ambari.server.controller.spi.Resource> getResources(org.apache.ambari.server.controller.spi.Request request, org.apache.ambari.server.controller.spi.Predicate predicate) throws org.apache.ambari.server.controller.spi.SystemException, org.apache.ambari.server.controller.spi.UnsupportedPropertyException, org.apache.ambari.server.controller.spi.NoSuchResourceException, org.apache.ambari.server.controller.spi.NoSuchParentResourceException {
            java.util.Set<java.lang.String> keyPropertyValues = new java.util.LinkedHashSet<>();
            keyPropertyValues.add("1");
            keyPropertyValues.add("2");
            return getResources(org.apache.ambari.server.controller.spi.Resource.Type.CompatibleRepositoryVersion, predicate, "CompatibleRepositoriVersions/id", keyPropertyValues);
        }
    }

    private static class TestStackArtifactResourceProvider extends org.apache.ambari.server.controller.internal.ClusterControllerImplTest.TestResourceProvider {
        private TestStackArtifactResourceProvider() {
            super(org.apache.ambari.server.controller.internal.StackArtifactResourceProvider.propertyIds, org.apache.ambari.server.controller.internal.StackArtifactResourceProvider.keyPropertyIds);
        }

        @java.lang.Override
        public java.util.Set<org.apache.ambari.server.controller.spi.Resource> getResources(org.apache.ambari.server.controller.spi.Request request, org.apache.ambari.server.controller.spi.Predicate predicate) throws org.apache.ambari.server.controller.spi.SystemException, org.apache.ambari.server.controller.spi.UnsupportedPropertyException, org.apache.ambari.server.controller.spi.NoSuchResourceException, org.apache.ambari.server.controller.spi.NoSuchParentResourceException {
            java.util.Set<java.lang.String> keyPropertyValues = new java.util.LinkedHashSet<>();
            keyPropertyValues.add("kerberos_descriptor");
            return getResources(org.apache.ambari.server.controller.spi.Resource.Type.StackArtifact, predicate, "Artifacts/artifact_name", keyPropertyValues);
        }
    }
}