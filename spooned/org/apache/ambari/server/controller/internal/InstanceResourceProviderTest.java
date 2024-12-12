package org.apache.ambari.server.controller.internal;
import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
public class InstanceResourceProviderTest {
    @org.junit.Test
    public void testCreateResources() throws java.lang.Exception {
        org.apache.ambari.server.controller.ivory.IvoryService service = EasyMock.createMock(org.apache.ambari.server.controller.ivory.IvoryService.class);
        java.util.Set<java.util.Map<java.lang.String, java.lang.Object>> propertySet = new java.util.HashSet<>();
        java.util.Map<java.lang.String, java.lang.Object> properties = new java.util.HashMap<>();
        EasyMock.replay(service);
        propertySet.add(properties);
        org.apache.ambari.server.controller.spi.Request request = org.apache.ambari.server.controller.utilities.PropertyHelper.getCreateRequest(propertySet, java.util.Collections.emptyMap());
        org.apache.ambari.server.controller.internal.InstanceResourceProvider provider = new org.apache.ambari.server.controller.internal.InstanceResourceProvider(service);
        try {
            provider.createResources(request);
            org.junit.Assert.fail("Expected UnsupportedOperationException");
        } catch (java.lang.UnsupportedOperationException e) {
        }
        EasyMock.verify(service);
    }

    @org.junit.Test
    public void testGetResources() throws java.lang.Exception {
        org.apache.ambari.server.controller.ivory.IvoryService service = EasyMock.createMock(org.apache.ambari.server.controller.ivory.IvoryService.class);
        java.util.Set<java.util.Map<java.lang.String, java.lang.Object>> propertySet = new java.util.HashSet<>();
        java.util.Map<java.lang.String, java.lang.Object> properties = new java.util.HashMap<>();
        java.util.List<org.apache.ambari.server.controller.ivory.Instance> instances = new java.util.LinkedList<>();
        org.apache.ambari.server.controller.ivory.Instance instance1 = new org.apache.ambari.server.controller.ivory.Instance("Feed1", "Instance1", "s", "st", "et", "d", "l");
        org.apache.ambari.server.controller.ivory.Instance instance2 = new org.apache.ambari.server.controller.ivory.Instance("Feed1", "Instance2", "s", "st", "et", "d", "l");
        org.apache.ambari.server.controller.ivory.Instance instance3 = new org.apache.ambari.server.controller.ivory.Instance("Feed1", "Instance3", "s", "st", "et", "d", "l");
        instances.add(instance1);
        instances.add(instance2);
        instances.add(instance3);
        EasyMock.expect(service.getFeedNames()).andReturn(java.util.Collections.singletonList("Feed1"));
        EasyMock.expect(service.getInstances("Feed1")).andReturn(instances);
        EasyMock.replay(service);
        propertySet.add(properties);
        org.apache.ambari.server.controller.spi.Request request = org.apache.ambari.server.controller.utilities.PropertyHelper.getCreateRequest(propertySet, java.util.Collections.emptyMap());
        org.apache.ambari.server.controller.internal.InstanceResourceProvider provider = new org.apache.ambari.server.controller.internal.InstanceResourceProvider(service);
        java.util.Set<org.apache.ambari.server.controller.spi.Resource> resources = provider.getResources(request, null);
        org.junit.Assert.assertEquals(3, resources.size());
        EasyMock.verify(service);
    }

    @org.junit.Test
    public void testUpdateResources() throws java.lang.Exception {
        org.apache.ambari.server.controller.ivory.IvoryService service = EasyMock.createMock(org.apache.ambari.server.controller.ivory.IvoryService.class);
        java.util.Set<java.util.Map<java.lang.String, java.lang.Object>> propertySet = new java.util.HashSet<>();
        java.util.Map<java.lang.String, java.lang.Object> properties = new java.util.HashMap<>();
        properties.put(org.apache.ambari.server.controller.internal.InstanceResourceProvider.INSTANCE_FEED_NAME_PROPERTY_ID, "Feed1");
        properties.put(org.apache.ambari.server.controller.internal.InstanceResourceProvider.INSTANCE_ID_PROPERTY_ID, "Instance1");
        properties.put(org.apache.ambari.server.controller.internal.InstanceResourceProvider.INSTANCE_STATUS_PROPERTY_ID, "SUSPEND");
        properties.put(org.apache.ambari.server.controller.internal.InstanceResourceProvider.INSTANCE_START_TIME_PROPERTY_ID, "ST");
        properties.put(org.apache.ambari.server.controller.internal.InstanceResourceProvider.INSTANCE_END_TIME_PROPERTY_ID, "ET");
        properties.put(org.apache.ambari.server.controller.internal.InstanceResourceProvider.INSTANCE_DETAILS_PROPERTY_ID, "DETAILS");
        properties.put(org.apache.ambari.server.controller.internal.InstanceResourceProvider.INSTANCE_LOG_PROPERTY_ID, "log");
        java.util.List<org.apache.ambari.server.controller.ivory.Instance> instances = new java.util.LinkedList<>();
        EasyMock.expect(service.getFeedNames()).andReturn(java.util.Collections.singletonList("Feed1"));
        EasyMock.expect(service.getInstances("Feed1")).andReturn(instances);
        EasyMock.replay(service);
        propertySet.add(properties);
        org.apache.ambari.server.controller.spi.Request request = org.apache.ambari.server.controller.utilities.PropertyHelper.getCreateRequest(propertySet, java.util.Collections.emptyMap());
        org.apache.ambari.server.controller.internal.InstanceResourceProvider provider = new org.apache.ambari.server.controller.internal.InstanceResourceProvider(service);
        provider.updateResources(request, null);
        EasyMock.verify(service);
    }

    @org.junit.Test
    public void testDeleteResources() throws java.lang.Exception {
        org.apache.ambari.server.controller.ivory.IvoryService service = EasyMock.createMock(org.apache.ambari.server.controller.ivory.IvoryService.class);
        org.apache.ambari.server.controller.ivory.Instance instance1 = new org.apache.ambari.server.controller.ivory.Instance("Feed1", "Instance1", "SUBMITTED", "start", "end", "details", "log");
        EasyMock.expect(service.getFeedNames()).andReturn(java.util.Collections.singletonList("Feed1"));
        EasyMock.expect(service.getInstances("Feed1")).andReturn(java.util.Collections.singletonList(instance1));
        service.killInstance("Feed1", "Instance1");
        EasyMock.replay(service);
        org.apache.ambari.server.controller.internal.InstanceResourceProvider provider = new org.apache.ambari.server.controller.internal.InstanceResourceProvider(service);
        org.apache.ambari.server.controller.spi.Predicate predicate = new org.apache.ambari.server.controller.utilities.PredicateBuilder().property(org.apache.ambari.server.controller.internal.InstanceResourceProvider.INSTANCE_ID_PROPERTY_ID).equals("Instance1").toPredicate();
        provider.deleteResources(new org.apache.ambari.server.controller.internal.RequestImpl(null, null, null, null), predicate);
        EasyMock.verify(service);
    }
}