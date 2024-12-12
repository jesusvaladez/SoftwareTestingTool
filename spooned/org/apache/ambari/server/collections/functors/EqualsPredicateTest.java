package org.apache.ambari.server.collections.functors;
import org.easymock.EasyMock;
import org.easymock.EasyMockSupport;
import static org.easymock.EasyMock.expect;
public class EqualsPredicateTest extends org.easymock.EasyMockSupport {
    @org.junit.Test
    public void testEvaluate() {
        java.lang.String data1 = "value1";
        java.lang.String data2 = "value2";
        org.apache.ambari.server.collections.functors.ContextTransformer transformer = createStrictMock(org.apache.ambari.server.collections.functors.ContextTransformer.class);
        EasyMock.expect(transformer.transform(org.easymock.EasyMock.<java.util.Map<?, ?>>anyObject())).andReturn(data1).times(1);
        EasyMock.expect(transformer.transform(org.easymock.EasyMock.<java.util.Map<?, ?>>anyObject())).andReturn(data2).times(1);
        replayAll();
        org.apache.ambari.server.collections.functors.EqualsPredicate predicate = new org.apache.ambari.server.collections.functors.EqualsPredicate(transformer, "value1");
        org.junit.Assert.assertTrue(predicate.evaluate(java.util.Collections.singletonMap("data", data1)));
        org.junit.Assert.assertFalse(predicate.evaluate(java.util.Collections.singletonMap("data", data2)));
        verifyAll();
    }

    @org.junit.Test
    public void testToMap() {
        org.apache.ambari.server.collections.functors.ContextTransformer transformer = createStrictMock(org.apache.ambari.server.collections.functors.ContextTransformer.class);
        EasyMock.expect(transformer.getKey()).andReturn("data").times(1);
        replayAll();
        org.apache.ambari.server.collections.functors.EqualsPredicate predicate = new org.apache.ambari.server.collections.functors.EqualsPredicate(transformer, "value");
        java.util.Map<java.lang.String, java.lang.Object> actualMap = predicate.toMap();
        verifyAll();
        java.util.Map<java.lang.String, java.lang.Object> expectedMap = new java.util.HashMap<>();
        expectedMap.put("equals", new java.util.ArrayList<>(java.util.Arrays.asList("data", "value")));
        org.junit.Assert.assertEquals(expectedMap, actualMap);
    }

    @org.junit.Test
    public void testToJSON() {
        org.apache.ambari.server.collections.functors.ContextTransformer transformer = createStrictMock(org.apache.ambari.server.collections.functors.ContextTransformer.class);
        EasyMock.expect(transformer.getKey()).andReturn("data").times(1);
        replayAll();
        org.apache.ambari.server.collections.functors.EqualsPredicate predicate = new org.apache.ambari.server.collections.functors.EqualsPredicate(transformer, "value");
        java.lang.String actualJSON = predicate.toJSON();
        verifyAll();
        java.lang.String expectedJSON = "{\"equals\":[\"data\",\"value\"]}";
        org.junit.Assert.assertEquals(expectedJSON, actualJSON);
    }
}