package org.apache.ambari.server.controller.internal;
import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
public class TargetClusterResourceProviderTest {
    private static org.apache.ambari.server.controller.ivory.Cluster.Interface interface1 = new org.apache.ambari.server.controller.ivory.Cluster.Interface("write", "hdfs://ec2.a.b.com:8020", "1.1.2.22");

    private static java.util.Map<java.lang.String, java.lang.String> interfaces = new java.util.HashMap<>();

    static {
        interfaces.put("type", interface1.getType());
        interfaces.put("endpoint", interface1.getEndpoint());
        interfaces.put("version", interface1.getVersion());
    }

    private static org.apache.ambari.server.controller.ivory.Cluster.Location location1 = new org.apache.ambari.server.controller.ivory.Cluster.Location("location1", "/mirrorthis");

    private static java.util.Map<java.lang.String, java.lang.String> locations = new java.util.HashMap<>();

    static {
        locations.put("name", location1.getName());
        locations.put("path", location1.getPath());
    }

    @org.junit.Test
    public void testCreateResources() throws java.lang.Exception {
        org.apache.ambari.server.controller.ivory.IvoryService service = EasyMock.createMock(org.apache.ambari.server.controller.ivory.IvoryService.class);
        java.util.Set<java.util.Map<java.lang.String, java.lang.Object>> propertySet = new java.util.HashSet<>();
        java.util.Map<java.lang.String, java.lang.Object> properties = new java.util.HashMap<>();
        properties.put(org.apache.ambari.server.controller.internal.TargetClusterResourceProvider.CLUSTER_NAME_PROPERTY_ID, "Cluster1");
        properties.put(org.apache.ambari.server.controller.internal.TargetClusterResourceProvider.CLUSTER_COLO_PROPERTY_ID, "Colo");
        properties.put(org.apache.ambari.server.controller.internal.TargetClusterResourceProvider.CLUSTER_INTERFACES_PROPERTY_ID, java.util.Collections.singleton(org.apache.ambari.server.controller.internal.TargetClusterResourceProviderTest.interfaces));
        properties.put(org.apache.ambari.server.controller.internal.TargetClusterResourceProvider.CLUSTER_LOCATIONS_PROPERTY_ID, java.util.Collections.singleton(org.apache.ambari.server.controller.internal.TargetClusterResourceProviderTest.locations));
        properties.put(org.apache.ambari.server.controller.internal.TargetClusterResourceProvider.CLUSTER_PROPERTIES_PROPERTY_ID, java.util.Collections.singletonMap("P1", "V1"));
        service.submitCluster(org.apache.ambari.server.controller.internal.TargetClusterResourceProvider.getCluster("Cluster1", properties));
        EasyMock.replay(service);
        propertySet.add(properties);
        org.apache.ambari.server.controller.spi.Request request = org.apache.ambari.server.controller.utilities.PropertyHelper.getCreateRequest(propertySet, java.util.Collections.emptyMap());
        org.apache.ambari.server.controller.internal.TargetClusterResourceProvider provider = new org.apache.ambari.server.controller.internal.TargetClusterResourceProvider(service);
        provider.createResources(request);
        EasyMock.verify(service);
    }

    @org.junit.Test
    public void testGetResources() throws java.lang.Exception {
        org.apache.ambari.server.controller.ivory.IvoryService service = EasyMock.createMock(org.apache.ambari.server.controller.ivory.IvoryService.class);
        java.util.Set<java.util.Map<java.lang.String, java.lang.Object>> propertySet = new java.util.HashSet<>();
        java.util.Map<java.lang.String, java.lang.Object> properties = new java.util.HashMap<>();
        java.util.List<java.lang.String> targetClusterNames = new java.util.LinkedList<>();
        targetClusterNames.add("Cluster1");
        targetClusterNames.add("Cluster2");
        targetClusterNames.add("Cluster3");
        org.apache.ambari.server.controller.ivory.Cluster.Interface interface1 = new org.apache.ambari.server.controller.ivory.Cluster.Interface("type", "endpoint", "version");
        org.apache.ambari.server.controller.ivory.Cluster.Location location1 = new org.apache.ambari.server.controller.ivory.Cluster.Location("name", "path");
        org.apache.ambari.server.controller.ivory.Cluster targetCluster1 = new org.apache.ambari.server.controller.ivory.Cluster("Cluster1", "Colo", java.util.Collections.singleton(interface1), java.util.Collections.singleton(location1), java.util.Collections.singletonMap("P1", "V1"));
        org.apache.ambari.server.controller.ivory.Cluster targetCluster2 = new org.apache.ambari.server.controller.ivory.Cluster("Cluster2", "Colo", java.util.Collections.singleton(interface1), java.util.Collections.singleton(location1), java.util.Collections.singletonMap("P1", "V1"));
        org.apache.ambari.server.controller.ivory.Cluster targetCluster3 = new org.apache.ambari.server.controller.ivory.Cluster("Cluster3", "Colo", java.util.Collections.singleton(interface1), java.util.Collections.singleton(location1), java.util.Collections.singletonMap("P1", "V1"));
        EasyMock.expect(service.getClusterNames()).andReturn(targetClusterNames);
        EasyMock.expect(service.getCluster("Cluster1")).andReturn(targetCluster1);
        EasyMock.expect(service.getCluster("Cluster2")).andReturn(targetCluster2);
        EasyMock.expect(service.getCluster("Cluster3")).andReturn(targetCluster3);
        EasyMock.replay(service);
        propertySet.add(properties);
        org.apache.ambari.server.controller.spi.Request request = org.apache.ambari.server.controller.utilities.PropertyHelper.getCreateRequest(propertySet, java.util.Collections.emptyMap());
        org.apache.ambari.server.controller.internal.TargetClusterResourceProvider provider = new org.apache.ambari.server.controller.internal.TargetClusterResourceProvider(service);
        java.util.Set<org.apache.ambari.server.controller.spi.Resource> resources = provider.getResources(request, null);
        org.junit.Assert.assertEquals(3, resources.size());
        EasyMock.verify(service);
    }

    @org.junit.Test
    public void testUpdateResources() throws java.lang.Exception {
        org.apache.ambari.server.controller.ivory.IvoryService service = EasyMock.createMock(org.apache.ambari.server.controller.ivory.IvoryService.class);
        java.util.Set<java.util.Map<java.lang.String, java.lang.Object>> propertySet = new java.util.HashSet<>();
        java.util.Map<java.lang.String, java.lang.Object> properties = new java.util.HashMap<>();
        properties.put(org.apache.ambari.server.controller.internal.TargetClusterResourceProvider.CLUSTER_NAME_PROPERTY_ID, "Cluster1");
        properties.put(org.apache.ambari.server.controller.internal.TargetClusterResourceProvider.CLUSTER_COLO_PROPERTY_ID, "Colo");
        properties.put(org.apache.ambari.server.controller.internal.TargetClusterResourceProvider.CLUSTER_INTERFACES_PROPERTY_ID, java.util.Collections.singleton(org.apache.ambari.server.controller.internal.TargetClusterResourceProviderTest.interfaces));
        properties.put(org.apache.ambari.server.controller.internal.TargetClusterResourceProvider.CLUSTER_LOCATIONS_PROPERTY_ID, java.util.Collections.singleton(org.apache.ambari.server.controller.internal.TargetClusterResourceProviderTest.locations));
        properties.put(org.apache.ambari.server.controller.internal.TargetClusterResourceProvider.CLUSTER_PROPERTIES_PROPERTY_ID + "/P1", "V1");
        java.util.List<java.lang.String> targetClusterNames = new java.util.LinkedList<>();
        targetClusterNames.add("Cluster1");
        java.util.Set<org.apache.ambari.server.controller.ivory.Cluster.Interface> interfaceSet = java.util.Collections.singleton(org.apache.ambari.server.controller.internal.TargetClusterResourceProviderTest.interface1);
        java.util.Set<org.apache.ambari.server.controller.ivory.Cluster.Location> locationSet = java.util.Collections.singleton(org.apache.ambari.server.controller.internal.TargetClusterResourceProviderTest.location1);
        org.apache.ambari.server.controller.ivory.Cluster targetCluster1 = new org.apache.ambari.server.controller.ivory.Cluster("Cluster1", "Colo", interfaceSet, locationSet, java.util.Collections.singletonMap("P1", "V1"));
        EasyMock.expect(service.getClusterNames()).andReturn(targetClusterNames);
        EasyMock.expect(service.getCluster("Cluster1")).andReturn(targetCluster1);
        service.updateCluster(targetCluster1);
        EasyMock.replay(service);
        propertySet.add(properties);
        org.apache.ambari.server.controller.spi.Request request = org.apache.ambari.server.controller.utilities.PropertyHelper.getCreateRequest(propertySet, java.util.Collections.emptyMap());
        org.apache.ambari.server.controller.internal.TargetClusterResourceProvider provider = new org.apache.ambari.server.controller.internal.TargetClusterResourceProvider(service);
        provider.updateResources(request, null);
        EasyMock.verify(service);
    }

    @org.junit.Test
    public void testDeleteResources() throws java.lang.Exception {
        org.apache.ambari.server.controller.ivory.IvoryService service = EasyMock.createMock(org.apache.ambari.server.controller.ivory.IvoryService.class);
        java.util.List<java.lang.String> targetClusterNames = new java.util.LinkedList<>();
        targetClusterNames.add("Cluster1");
        org.apache.ambari.server.controller.ivory.Cluster.Interface interface1 = new org.apache.ambari.server.controller.ivory.Cluster.Interface("type", "endpoint", "version");
        org.apache.ambari.server.controller.ivory.Cluster.Location location1 = new org.apache.ambari.server.controller.ivory.Cluster.Location("name", "path");
        org.apache.ambari.server.controller.ivory.Cluster targetCluster1 = new org.apache.ambari.server.controller.ivory.Cluster("Cluster1", "Colo", java.util.Collections.singleton(interface1), java.util.Collections.singleton(location1), java.util.Collections.singletonMap("P1", "V1"));
        EasyMock.expect(service.getClusterNames()).andReturn(targetClusterNames);
        EasyMock.expect(service.getCluster("Cluster1")).andReturn(targetCluster1);
        service.deleteCluster("Cluster1");
        EasyMock.replay(service);
        org.apache.ambari.server.controller.internal.TargetClusterResourceProvider provider = new org.apache.ambari.server.controller.internal.TargetClusterResourceProvider(service);
        org.apache.ambari.server.controller.spi.Predicate predicate = new org.apache.ambari.server.controller.utilities.PredicateBuilder().property(org.apache.ambari.server.controller.internal.TargetClusterResourceProvider.CLUSTER_NAME_PROPERTY_ID).equals("Cluster1").toPredicate();
        provider.deleteResources(new org.apache.ambari.server.controller.internal.RequestImpl(null, null, null, null), predicate);
        EasyMock.verify(service);
    }
}