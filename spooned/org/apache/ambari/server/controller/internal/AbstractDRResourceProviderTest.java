package org.apache.ambari.server.controller.internal;
import static org.easymock.EasyMock.createMock;
public class AbstractDRResourceProviderTest {
    @org.junit.Test
    public void testGetResourceProvider() throws java.lang.Exception {
        java.util.Set<java.lang.String> propertyIds = new java.util.HashSet<>();
        propertyIds.add("foo");
        propertyIds.add("cat1/foo");
        propertyIds.add("cat2/bar");
        propertyIds.add("cat2/baz");
        propertyIds.add("cat3/sub1/bam");
        propertyIds.add("cat4/sub2/sub3/bat");
        propertyIds.add("cat5/subcat5/map");
        java.util.Map<org.apache.ambari.server.controller.spi.Resource.Type, java.lang.String> keyPropertyIds = new java.util.HashMap<>();
        org.apache.ambari.server.controller.ivory.IvoryService ivoryService = EasyMock.createMock(org.apache.ambari.server.controller.ivory.IvoryService.class);
        org.apache.ambari.server.controller.internal.AbstractResourceProvider provider = ((org.apache.ambari.server.controller.internal.AbstractResourceProvider) (org.apache.ambari.server.controller.internal.AbstractDRResourceProvider.getResourceProvider(org.apache.ambari.server.controller.spi.Resource.Type.DRFeed, ivoryService)));
        junit.framework.Assert.assertTrue(provider instanceof org.apache.ambari.server.controller.internal.FeedResourceProvider);
    }
}