package org.apache.ambari.server.controller.internal;
import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
public class FeedResourceProviderTest {
    @org.junit.Test
    public void testCreateResources() throws java.lang.Exception {
        org.apache.ambari.server.controller.ivory.IvoryService service = EasyMock.createMock(org.apache.ambari.server.controller.ivory.IvoryService.class);
        java.util.Set<java.util.Map<java.lang.String, java.lang.Object>> propertySet = new java.util.HashSet<>();
        java.util.Map<java.lang.String, java.lang.Object> properties = new java.util.HashMap<>();
        properties.put(org.apache.ambari.server.controller.internal.FeedResourceProvider.FEED_NAME_PROPERTY_ID, "Feed1");
        properties.put(org.apache.ambari.server.controller.internal.FeedResourceProvider.FEED_DESCRIPTION_PROPERTY_ID, "desc");
        properties.put(org.apache.ambari.server.controller.internal.FeedResourceProvider.FEED_SCHEDULE_PROPERTY_ID, "sched");
        properties.put(org.apache.ambari.server.controller.internal.FeedResourceProvider.FEED_STATUS_PROPERTY_ID, "SUBMITTED");
        properties.put(org.apache.ambari.server.controller.internal.FeedResourceProvider.FEED_SOURCE_CLUSTER_NAME_PROPERTY_ID, "source");
        properties.put(org.apache.ambari.server.controller.internal.FeedResourceProvider.FEED_TARGET_CLUSTER_NAME_PROPERTY_ID, "target");
        service.submitFeed(org.apache.ambari.server.controller.internal.FeedResourceProvider.getFeed("Feed1", properties));
        EasyMock.replay(service);
        propertySet.add(properties);
        org.apache.ambari.server.controller.spi.Request request = org.apache.ambari.server.controller.utilities.PropertyHelper.getCreateRequest(propertySet, java.util.Collections.emptyMap());
        org.apache.ambari.server.controller.internal.FeedResourceProvider provider = new org.apache.ambari.server.controller.internal.FeedResourceProvider(service);
        provider.createResources(request);
        EasyMock.verify(service);
    }

    @org.junit.Test
    public void testGetResources() throws java.lang.Exception {
        org.apache.ambari.server.controller.ivory.IvoryService service = EasyMock.createMock(org.apache.ambari.server.controller.ivory.IvoryService.class);
        java.util.Set<java.util.Map<java.lang.String, java.lang.Object>> propertySet = new java.util.HashSet<>();
        java.util.Map<java.lang.String, java.lang.Object> properties = new java.util.HashMap<>();
        java.util.List<java.lang.String> feedNames = new java.util.LinkedList<>();
        feedNames.add("Feed1");
        feedNames.add("Feed2");
        feedNames.add("Feed3");
        java.util.Map<java.lang.String, java.lang.String> props = new java.util.HashMap<>();
        org.apache.ambari.server.controller.ivory.Feed feed1 = new org.apache.ambari.server.controller.ivory.Feed("Feed1", "d", "s", "sch", "source", "st", "end", "l", "a", "target", "st", "end", "l", "a", props);
        org.apache.ambari.server.controller.ivory.Feed feed2 = new org.apache.ambari.server.controller.ivory.Feed("Feed2", "d", "s", "sch", "source", "st", "end", "l", "a", "target", "st", "end", "l", "a", props);
        org.apache.ambari.server.controller.ivory.Feed feed3 = new org.apache.ambari.server.controller.ivory.Feed("Feed3", "d", "s", "sch", "source", "st", "end", "l", "a", "target", "st", "end", "l", "a", props);
        EasyMock.expect(service.getFeedNames()).andReturn(feedNames);
        EasyMock.expect(service.getFeed("Feed1")).andReturn(feed1);
        EasyMock.expect(service.getFeed("Feed2")).andReturn(feed2);
        EasyMock.expect(service.getFeed("Feed3")).andReturn(feed3);
        EasyMock.replay(service);
        propertySet.add(properties);
        org.apache.ambari.server.controller.spi.Request request = org.apache.ambari.server.controller.utilities.PropertyHelper.getCreateRequest(propertySet, java.util.Collections.emptyMap());
        org.apache.ambari.server.controller.internal.FeedResourceProvider provider = new org.apache.ambari.server.controller.internal.FeedResourceProvider(service);
        java.util.Set<org.apache.ambari.server.controller.spi.Resource> resources = provider.getResources(request, null);
        org.junit.Assert.assertEquals(3, resources.size());
        EasyMock.verify(service);
    }

    @org.junit.Test
    public void testUpdateResources() throws java.lang.Exception {
        org.apache.ambari.server.controller.ivory.IvoryService service = EasyMock.createMock(org.apache.ambari.server.controller.ivory.IvoryService.class);
        java.util.Set<java.util.Map<java.lang.String, java.lang.Object>> propertySet = new java.util.HashSet<>();
        java.util.Map<java.lang.String, java.lang.Object> properties = new java.util.HashMap<>();
        properties.put(org.apache.ambari.server.controller.internal.FeedResourceProvider.FEED_NAME_PROPERTY_ID, "Feed1");
        properties.put(org.apache.ambari.server.controller.internal.FeedResourceProvider.FEED_DESCRIPTION_PROPERTY_ID, "desc");
        properties.put(org.apache.ambari.server.controller.internal.FeedResourceProvider.FEED_SCHEDULE_PROPERTY_ID, "sched");
        properties.put(org.apache.ambari.server.controller.internal.FeedResourceProvider.FEED_STATUS_PROPERTY_ID, "WAITING");
        properties.put(org.apache.ambari.server.controller.internal.FeedResourceProvider.FEED_SOURCE_CLUSTER_NAME_PROPERTY_ID, "source");
        properties.put(org.apache.ambari.server.controller.internal.FeedResourceProvider.FEED_TARGET_CLUSTER_NAME_PROPERTY_ID, "target");
        java.util.List<java.lang.String> feedNames = new java.util.LinkedList<>();
        feedNames.add("Feed1");
        java.util.Map<java.lang.String, java.lang.String> props = new java.util.HashMap<>();
        org.apache.ambari.server.controller.ivory.Feed feed1 = new org.apache.ambari.server.controller.ivory.Feed("Feed1", "desc", "WAITING", "sched", "source", "st", "end", "l", "a", "target", "st", "end", "l", "a", props);
        EasyMock.expect(service.getFeedNames()).andReturn(feedNames);
        EasyMock.expect(service.getFeed("Feed1")).andReturn(feed1);
        service.updateFeed(feed1);
        EasyMock.replay(service);
        propertySet.add(properties);
        org.apache.ambari.server.controller.spi.Request request = org.apache.ambari.server.controller.utilities.PropertyHelper.getCreateRequest(propertySet, java.util.Collections.emptyMap());
        org.apache.ambari.server.controller.internal.FeedResourceProvider provider = new org.apache.ambari.server.controller.internal.FeedResourceProvider(service);
        provider.updateResources(request, null);
        EasyMock.verify(service);
    }

    @org.junit.Test
    public void testDeleteResources() throws java.lang.Exception {
        org.apache.ambari.server.controller.ivory.IvoryService service = EasyMock.createMock(org.apache.ambari.server.controller.ivory.IvoryService.class);
        java.util.List<java.lang.String> feedNames = new java.util.LinkedList<>();
        feedNames.add("Feed1");
        java.util.Map<java.lang.String, java.lang.String> props = new java.util.HashMap<>();
        org.apache.ambari.server.controller.ivory.Feed feed1 = new org.apache.ambari.server.controller.ivory.Feed("Feed1", "d", "s", "sch", "source", "st", "end", "l", "a", "target", "st", "end", "l", "a", props);
        EasyMock.expect(service.getFeedNames()).andReturn(feedNames);
        EasyMock.expect(service.getFeed("Feed1")).andReturn(feed1);
        service.deleteFeed("Feed1");
        EasyMock.replay(service);
        org.apache.ambari.server.controller.internal.FeedResourceProvider provider = new org.apache.ambari.server.controller.internal.FeedResourceProvider(service);
        org.apache.ambari.server.controller.spi.Predicate predicate = new org.apache.ambari.server.controller.utilities.PredicateBuilder().property(org.apache.ambari.server.controller.internal.FeedResourceProvider.FEED_NAME_PROPERTY_ID).equals("Feed1").toPredicate();
        provider.deleteResources(new org.apache.ambari.server.controller.internal.RequestImpl(null, null, null, null), predicate);
        EasyMock.verify(service);
    }
}