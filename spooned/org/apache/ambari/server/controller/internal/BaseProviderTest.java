package org.apache.ambari.server.controller.internal;
public class BaseProviderTest {
    @org.junit.Test
    public void testGetProperties() {
        java.util.Set<java.lang.String> propertyIds = new java.util.HashSet<>();
        propertyIds.add("foo");
        propertyIds.add("bar");
        propertyIds.add("cat1/prop1");
        propertyIds.add("cat2/prop2");
        propertyIds.add("cat3/subcat3/prop3");
        org.apache.ambari.server.controller.internal.BaseProvider provider = new org.apache.ambari.server.controller.internal.BaseProviderTest.TestProvider(propertyIds);
        java.util.Set<java.lang.String> supportedPropertyIds = provider.getPropertyIds();
        org.junit.Assert.assertTrue(supportedPropertyIds.containsAll(propertyIds));
    }

    @org.junit.Test
    public void testCheckPropertyIds() {
        java.util.Set<java.lang.String> propertyIds = new java.util.HashSet<>();
        propertyIds.add("foo");
        propertyIds.add("bar");
        propertyIds.add("cat1/prop1");
        propertyIds.add("cat2/prop2");
        propertyIds.add("cat3/subcat3/prop3");
        propertyIds.add("cat4/subcat4/map");
        org.apache.ambari.server.controller.internal.BaseProvider provider = new org.apache.ambari.server.controller.internal.BaseProviderTest.TestProvider(propertyIds);
        org.junit.Assert.assertTrue(provider.checkPropertyIds(propertyIds).isEmpty());
        org.junit.Assert.assertTrue(provider.checkPropertyIds(java.util.Collections.singleton("cat1")).isEmpty());
        org.junit.Assert.assertTrue(provider.checkPropertyIds(java.util.Collections.singleton("cat2")).isEmpty());
        org.junit.Assert.assertTrue(provider.checkPropertyIds(java.util.Collections.singleton("cat3")).isEmpty());
        org.junit.Assert.assertTrue(provider.checkPropertyIds(java.util.Collections.singleton("cat3/subcat3")).isEmpty());
        org.junit.Assert.assertTrue(provider.checkPropertyIds(java.util.Collections.singleton("cat4/subcat4/map")).isEmpty());
        org.junit.Assert.assertTrue(provider.checkPropertyIds(java.util.Collections.singleton("cat4/subcat4/map/key")).isEmpty());
        propertyIds.add("badprop");
        propertyIds.add("badcat");
        java.util.Set<java.lang.String> unsupportedPropertyIds = provider.checkPropertyIds(propertyIds);
        org.junit.Assert.assertFalse(unsupportedPropertyIds.isEmpty());
        org.junit.Assert.assertEquals(2, unsupportedPropertyIds.size());
        org.junit.Assert.assertTrue(unsupportedPropertyIds.contains("badprop"));
        org.junit.Assert.assertTrue(unsupportedPropertyIds.contains("badcat"));
    }

    @org.junit.Test
    public void testGetRequestPropertyIds() {
        java.util.Set<java.lang.String> providerPropertyIds = new java.util.HashSet<>();
        providerPropertyIds.add("foo");
        providerPropertyIds.add("bar");
        providerPropertyIds.add("cat1/sub1");
        org.apache.ambari.server.controller.internal.BaseProvider provider = new org.apache.ambari.server.controller.internal.BaseProviderTest.TestProvider(providerPropertyIds);
        org.apache.ambari.server.controller.spi.Request request = org.apache.ambari.server.controller.utilities.PropertyHelper.getReadRequest("foo");
        java.util.Set<java.lang.String> requestedPropertyIds = provider.getRequestPropertyIds(request, null);
        org.junit.Assert.assertEquals(1, requestedPropertyIds.size());
        org.junit.Assert.assertTrue(requestedPropertyIds.contains("foo"));
        request = org.apache.ambari.server.controller.utilities.PropertyHelper.getReadRequest("foo", "bar");
        requestedPropertyIds = provider.getRequestPropertyIds(request, null);
        org.junit.Assert.assertEquals(2, requestedPropertyIds.size());
        org.junit.Assert.assertTrue(requestedPropertyIds.contains("foo"));
        org.junit.Assert.assertTrue(requestedPropertyIds.contains("bar"));
        request = org.apache.ambari.server.controller.utilities.PropertyHelper.getReadRequest("foo", "baz", "bar", "cat", "cat1/prop1");
        requestedPropertyIds = provider.getRequestPropertyIds(request, null);
        org.junit.Assert.assertEquals(2, requestedPropertyIds.size());
        org.junit.Assert.assertTrue(requestedPropertyIds.contains("foo"));
        org.junit.Assert.assertTrue(requestedPropertyIds.contains("bar"));
        request = org.apache.ambari.server.controller.utilities.PropertyHelper.getReadRequest("foo", "cat1/sub1/prop1");
        requestedPropertyIds = provider.getRequestPropertyIds(request, null);
        org.junit.Assert.assertEquals(2, requestedPropertyIds.size());
        org.junit.Assert.assertTrue(requestedPropertyIds.contains("foo"));
        org.junit.Assert.assertTrue(requestedPropertyIds.contains("cat1/sub1/prop1"));
    }

    @org.junit.Test
    public void testSetResourceProperty() {
        java.util.Set<java.lang.String> propertyIds = new java.util.HashSet<>();
        propertyIds.add("p1");
        propertyIds.add("foo");
        propertyIds.add("cat1/foo");
        propertyIds.add("cat2/bar");
        propertyIds.add("cat2/baz");
        propertyIds.add("cat3/sub1/bam");
        propertyIds.add("cat4/sub2/sub3/bat");
        propertyIds.add("cat5/sub5");
        org.apache.ambari.server.controller.spi.Resource resource = new org.apache.ambari.server.controller.internal.ResourceImpl(org.apache.ambari.server.controller.spi.Resource.Type.Service);
        org.junit.Assert.assertNull(resource.getPropertyValue("foo"));
        org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, "foo", "value1", propertyIds);
        org.junit.Assert.assertEquals("value1", resource.getPropertyValue("foo"));
        org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, "cat2/bar", "value2", propertyIds);
        org.junit.Assert.assertEquals("value2", resource.getPropertyValue("cat2/bar"));
        org.junit.Assert.assertNull(resource.getPropertyValue("unsupported"));
        org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, "unsupported", "valueX", propertyIds);
        org.junit.Assert.assertNull(resource.getPropertyValue("unsupported"));
        org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, "cat5/sub5/prop5", "value5", propertyIds);
        org.junit.Assert.assertEquals("value5", resource.getPropertyValue("cat5/sub5/prop5"));
        org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, "cat5/sub5/sub5a/prop5a", "value5", propertyIds);
        org.junit.Assert.assertEquals("value5", resource.getPropertyValue("cat5/sub5/sub5a/prop5a"));
        org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, "cat5/sub7/unsupported", "valueX", propertyIds);
        org.junit.Assert.assertNull(resource.getPropertyValue("cat5/sub7/unsupported"));
    }

    @org.junit.Test
    public void testIsPropertyRequested() {
        java.util.Set<java.lang.String> propertyIds = new java.util.HashSet<>();
        propertyIds.add("p1");
        propertyIds.add("foo");
        propertyIds.add("cat1/foo");
        propertyIds.add("cat2/bar");
        propertyIds.add("cat2/baz");
        propertyIds.add("cat3/sub1/bam");
        propertyIds.add("cat4/sub2/sub3/bat");
        propertyIds.add("cat5/sub5");
        org.junit.Assert.assertTrue(org.apache.ambari.server.controller.internal.BaseProvider.isPropertyRequested("foo", propertyIds));
        org.junit.Assert.assertTrue(org.apache.ambari.server.controller.internal.BaseProvider.isPropertyRequested("cat2", propertyIds));
        org.junit.Assert.assertTrue(org.apache.ambari.server.controller.internal.BaseProvider.isPropertyRequested("cat2/bar", propertyIds));
        org.junit.Assert.assertFalse(org.apache.ambari.server.controller.internal.BaseProvider.isPropertyRequested("unsupported", propertyIds));
        org.junit.Assert.assertTrue(org.apache.ambari.server.controller.internal.BaseProvider.isPropertyRequested("cat5/sub5/prop5", propertyIds));
        org.junit.Assert.assertTrue(org.apache.ambari.server.controller.internal.BaseProvider.isPropertyRequested("cat5/sub5/sub5a/prop5a", propertyIds));
        org.junit.Assert.assertFalse(org.apache.ambari.server.controller.internal.BaseProvider.isPropertyRequested("cat5/sub7/unsupported", propertyIds));
    }

    @org.junit.Test
    public void testSetResourcePropertyWithMaps() {
        java.util.Set<java.lang.String> propertyIds = new java.util.HashSet<>();
        propertyIds.add("cat1/emptyMapProperty");
        propertyIds.add("cat1/mapProperty");
        propertyIds.add("cat2/mapMapProperty");
        propertyIds.add("cat3/mapProperty3/key2");
        propertyIds.add("cat4/mapMapProperty4/subMap1/key3");
        propertyIds.add("cat4/mapMapProperty4/subMap2");
        org.apache.ambari.server.controller.spi.Resource resource = new org.apache.ambari.server.controller.internal.ResourceImpl(org.apache.ambari.server.controller.spi.Resource.Type.Service);
        java.util.Map<java.lang.String, java.lang.String> emptyMapProperty = new java.util.HashMap<>();
        org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, "cat1/emptyMapProperty", emptyMapProperty, propertyIds);
        org.junit.Assert.assertTrue(resource.getPropertiesMap().containsKey("cat1/emptyMapProperty"));
        java.util.Map<java.lang.String, java.lang.String> mapProperty = new java.util.HashMap<>();
        mapProperty.put("key1", "value1");
        mapProperty.put("key2", "value2");
        mapProperty.put("key3", "value3");
        org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, "cat1/mapProperty", mapProperty, propertyIds);
        org.junit.Assert.assertNull(resource.getPropertyValue("cat1/mapProperty"));
        org.junit.Assert.assertEquals("value1", resource.getPropertyValue("cat1/mapProperty/key1"));
        org.junit.Assert.assertEquals("value2", resource.getPropertyValue("cat1/mapProperty/key2"));
        org.junit.Assert.assertEquals("value3", resource.getPropertyValue("cat1/mapProperty/key3"));
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> mapMapProperty = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.lang.String> mapSubProperty1 = new java.util.HashMap<>();
        mapSubProperty1.put("key1", "value11");
        mapSubProperty1.put("key2", "value12");
        mapSubProperty1.put("key3", "value13");
        mapMapProperty.put("subMap1", mapSubProperty1);
        java.util.Map<java.lang.String, java.lang.String> mapSubProperty2 = new java.util.HashMap<>();
        mapSubProperty2.put("key1", "value21");
        mapSubProperty2.put("key2", "value22");
        mapSubProperty2.put("key3", "value23");
        mapMapProperty.put("subMap2", mapSubProperty2);
        java.util.Map<java.lang.String, java.lang.String> mapSubProperty3 = new java.util.HashMap<>();
        mapMapProperty.put("subMap3", mapSubProperty3);
        org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, "cat2/mapMapProperty", mapMapProperty, propertyIds);
        org.junit.Assert.assertNull(resource.getPropertyValue("cat2/mapMapProperty"));
        org.junit.Assert.assertNull(resource.getPropertyValue("cat2/mapMapProperty/subMap1"));
        org.junit.Assert.assertNull(resource.getPropertyValue("cat2/mapMapProperty/subMap2"));
        org.junit.Assert.assertTrue(resource.getPropertiesMap().containsKey("cat2/mapMapProperty/subMap3"));
        org.junit.Assert.assertEquals("value11", resource.getPropertyValue("cat2/mapMapProperty/subMap1/key1"));
        org.junit.Assert.assertEquals("value12", resource.getPropertyValue("cat2/mapMapProperty/subMap1/key2"));
        org.junit.Assert.assertEquals("value13", resource.getPropertyValue("cat2/mapMapProperty/subMap1/key3"));
        org.junit.Assert.assertEquals("value21", resource.getPropertyValue("cat2/mapMapProperty/subMap2/key1"));
        org.junit.Assert.assertEquals("value22", resource.getPropertyValue("cat2/mapMapProperty/subMap2/key2"));
        org.junit.Assert.assertEquals("value23", resource.getPropertyValue("cat2/mapMapProperty/subMap2/key3"));
        java.util.Map<java.lang.String, java.lang.String> mapProperty3 = new java.util.HashMap<>();
        mapProperty3.put("key1", "value1");
        mapProperty3.put("key2", "value2");
        mapProperty3.put("key3", "value3");
        org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, "cat3/mapProperty3", mapProperty3, propertyIds);
        org.junit.Assert.assertNull(resource.getPropertyValue("cat3/mapProperty3"));
        org.junit.Assert.assertNull(resource.getPropertyValue("cat3/mapProperty3/key1"));
        org.junit.Assert.assertEquals("value2", resource.getPropertyValue("cat3/mapProperty3/key2"));
        org.junit.Assert.assertNull(resource.getPropertyValue("cat3/mapProperty3/key3"));
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> mapMapProperty4 = new java.util.HashMap<>();
        mapMapProperty4.put("subMap1", mapSubProperty1);
        mapMapProperty4.put("subMap2", mapSubProperty2);
        org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, "cat4/mapMapProperty4", mapMapProperty4, propertyIds);
        org.junit.Assert.assertNull(resource.getPropertyValue("cat4/mapMapProperty4"));
        org.junit.Assert.assertNull(resource.getPropertyValue("cat4/mapMapProperty4/subMap1"));
        org.junit.Assert.assertNull(resource.getPropertyValue("cat4/mapMapProperty4/subMap2"));
        org.junit.Assert.assertNull(resource.getPropertyValue("cat4/mapMapProperty4/subMap1/key1"));
        org.junit.Assert.assertNull(resource.getPropertyValue("cat4/mapMapProperty4/subMap1/key2"));
        org.junit.Assert.assertEquals("value13", resource.getPropertyValue("cat4/mapMapProperty4/subMap1/key3"));
        org.junit.Assert.assertEquals("value21", resource.getPropertyValue("cat4/mapMapProperty4/subMap2/key1"));
        org.junit.Assert.assertEquals("value22", resource.getPropertyValue("cat4/mapMapProperty4/subMap2/key2"));
        org.junit.Assert.assertEquals("value23", resource.getPropertyValue("cat4/mapMapProperty4/subMap2/key3"));
    }

    @org.junit.Test
    public void testRegexpMethods() {
        java.util.Set<java.lang.String> propertyIds = new java.util.HashSet<>();
        java.lang.String regexp = "cat/$1.replaceAll(\\\"([.])\\\",\\\"/\\\")/key";
        java.lang.String propertyId = "cat/sub/key";
        java.lang.String regexp2 = "cat/$1.replaceAll(\\\"([.])\\\",\\\"/\\\")/something/$2/key";
        java.lang.String propertyId2 = "cat/sub/something/sub2/key";
        java.lang.String incorrectPropertyId = "some/property/id";
        propertyIds.add(regexp);
        propertyIds.add(regexp2);
        org.apache.ambari.server.controller.internal.BaseProvider provider = new org.apache.ambari.server.controller.internal.BaseProviderTest.TestProvider(propertyIds);
        java.util.Map.Entry<java.lang.String, java.util.regex.Pattern> regexEntry = provider.getRegexEntry(propertyId);
        org.junit.Assert.assertEquals(regexp, regexEntry.getKey());
        org.junit.Assert.assertNull(provider.getRegexEntry(incorrectPropertyId));
        org.junit.Assert.assertEquals("sub", provider.getRegexGroups(regexp, propertyId).get(0));
        org.junit.Assert.assertEquals("sub2", provider.getRegexGroups(regexp2, propertyId2).get(1));
        org.junit.Assert.assertTrue(provider.getRegexGroups(regexp, incorrectPropertyId).isEmpty());
    }

    @org.junit.Test
    public void testComplexMetricParsing() {
        java.util.Set<java.lang.String> propertyIds = new java.util.HashSet<>();
        propertyIds.add("metrics/flume/$1.substring(0)/CHANNEL/$2.replaceAll(\"[^-]+\",\"\")EventPutSuccessCount/rate/sum");
        propertyIds.add("metrics/yarn/Queue/$1.replaceAll(\"([.])\",\"/\")/AppsCompleted");
        propertyIds.add("metrics/yarn/Queue/$1.replaceAll(\",q(\\d+)=\",\"/\").substring(1)/AppsFailed");
        org.apache.ambari.server.controller.internal.BaseProviderTest.TestProvider provider = new org.apache.ambari.server.controller.internal.BaseProviderTest.TestProvider(propertyIds);
        java.util.Map.Entry<java.lang.String, java.util.regex.Pattern> entry = provider.getRegexEntry("metrics/flume/flume");
        org.junit.Assert.assertEquals("metrics/flume/$1", entry.getKey());
        org.junit.Assert.assertEquals("metrics/flume/(\\S*)", entry.getValue().pattern());
        entry = provider.getRegexEntry("metrics/flume/flume/CHANNEL");
        org.junit.Assert.assertEquals("metrics/flume/$1/CHANNEL", entry.getKey());
        org.junit.Assert.assertEquals("metrics/flume/(\\S*)/CHANNEL", entry.getValue().pattern());
        entry = provider.getRegexEntry("metrics/flume/flume/CHANNEL/EventPutSuccessCount");
        org.junit.Assert.assertEquals("metrics/flume/$1/CHANNEL/$2EventPutSuccessCount", entry.getKey());
        org.junit.Assert.assertEquals("metrics/flume/(\\S*)/CHANNEL/(\\S*)EventPutSuccessCount", entry.getValue().pattern());
        entry = provider.getRegexEntry("metrics/flume/flume/CHANNEL/EventPutSuccessCount/rate");
        org.junit.Assert.assertEquals("metrics/flume/$1/CHANNEL/$2EventPutSuccessCount/rate", entry.getKey());
        org.junit.Assert.assertEquals("metrics/flume/(\\S*)/CHANNEL/(\\S*)EventPutSuccessCount/rate", entry.getValue().pattern());
        entry = provider.getRegexEntry("metrics/yarn/Queue/root/AppsCompleted");
        org.junit.Assert.assertEquals("metrics/yarn/Queue/$1.replaceAll(\"([.])\",\"/\")/AppsCompleted", entry.getKey());
        org.junit.Assert.assertEquals("metrics/yarn/Queue/(\\S*)/AppsCompleted", entry.getValue().pattern());
        entry = provider.getRegexEntry("metrics/yarn/Queue/root/default/AppsCompleted");
        org.junit.Assert.assertEquals("metrics/yarn/Queue/$1.replaceAll(\"([.])\",\"/\")/AppsCompleted", entry.getKey());
        org.junit.Assert.assertEquals("metrics/yarn/Queue/(\\S*)/AppsCompleted", entry.getValue().pattern());
        entry = provider.getRegexEntry("metrics/yarn/Queue/root/default/AppsFailed");
        org.junit.Assert.assertEquals("metrics/yarn/Queue/$1.replaceAll(\",q(\\d+)=\",\"/\").substring(1)/AppsFailed", entry.getKey());
        org.junit.Assert.assertEquals("metrics/yarn/Queue/(\\S*)/AppsFailed", entry.getValue().pattern());
    }

    static class TestProvider extends org.apache.ambari.server.controller.internal.BaseProvider {
        public TestProvider(java.util.Set<java.lang.String> propertyIds) {
            super(propertyIds);
        }
    }
}