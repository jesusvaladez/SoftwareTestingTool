package org.apache.ambari.server.collections.functors;
import org.easymock.EasyMockSupport;
public class ContextTransformerTest extends org.easymock.EasyMockSupport {
    @org.junit.Test
    public void testGetKey() {
        org.apache.ambari.server.collections.functors.ContextTransformer transformer = new org.apache.ambari.server.collections.functors.ContextTransformer("key");
        junit.framework.Assert.assertEquals("key", transformer.getKey());
    }

    @org.junit.Test
    public void testTransformSimple() {
        java.util.Map<java.lang.String, java.lang.Object> context = new java.util.HashMap<>();
        context.put("key", "value");
        context.put("key1", "value1");
        context.put("key2", "value2");
        org.apache.ambari.server.collections.functors.ContextTransformer transformer = new org.apache.ambari.server.collections.functors.ContextTransformer("key");
        junit.framework.Assert.assertEquals("value", transformer.transform(context));
    }

    @org.junit.Test
    public void testTransformTree() {
        java.util.Map<java.lang.String, java.lang.Object> serviceSite = new java.util.HashMap<>();
        serviceSite.put("property", "service-site-property");
        java.util.Map<java.lang.String, java.lang.Object> configurations = new java.util.HashMap<>();
        configurations.put("service-site", serviceSite);
        configurations.put("property", "configuration-property");
        java.util.Map<java.lang.String, java.lang.Object> context = new java.util.HashMap<>();
        context.put("configurations", configurations);
        context.put("property", "context-property");
        org.apache.ambari.server.collections.functors.ContextTransformer transformer;
        transformer = new org.apache.ambari.server.collections.functors.ContextTransformer("configurations/service-site/property");
        junit.framework.Assert.assertEquals("service-site-property", transformer.transform(context));
        transformer = new org.apache.ambari.server.collections.functors.ContextTransformer("/configurations/service-site/property");
        junit.framework.Assert.assertEquals("service-site-property", transformer.transform(context));
        transformer = new org.apache.ambari.server.collections.functors.ContextTransformer("/configurations/service-site");
        junit.framework.Assert.assertEquals(serviceSite, transformer.transform(context));
    }
}