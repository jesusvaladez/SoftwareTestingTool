package org.apache.ambari.server.collections.functors;
import org.easymock.EasyMockSupport;
import static org.easymock.EasyMock.expect;
public class NotPredicateTest extends org.easymock.EasyMockSupport {
    @org.junit.Test
    public void testEvaluate() {
        org.apache.ambari.server.collections.Predicate mockPredicate = createStrictMock(org.apache.ambari.server.collections.Predicate.class);
        EasyMock.expect(mockPredicate.evaluate("context")).andReturn(true).times(1);
        EasyMock.expect(mockPredicate.evaluate("context")).andReturn(false).times(1);
        replayAll();
        org.apache.ambari.server.collections.functors.NotPredicate predicate = new org.apache.ambari.server.collections.functors.NotPredicate(mockPredicate);
        org.junit.Assert.assertFalse(predicate.evaluate("context"));
        org.junit.Assert.assertTrue(predicate.evaluate("context"));
        verifyAll();
        org.junit.Assert.assertArrayEquals(new org.apache.ambari.server.collections.Predicate[]{ mockPredicate }, predicate.getPredicates());
    }

    @org.junit.Test
    public void testToMap() {
        org.apache.ambari.server.collections.Predicate mockPredicate = createStrictMock(org.apache.ambari.server.collections.Predicate.class);
        EasyMock.expect(mockPredicate.toMap()).andReturn(java.util.Collections.singletonMap("nop", "foo")).times(1);
        replayAll();
        org.apache.ambari.server.collections.functors.NotPredicate predicate = new org.apache.ambari.server.collections.functors.NotPredicate(mockPredicate);
        java.util.Map<java.lang.String, java.lang.Object> actualMap = predicate.toMap();
        verifyAll();
        java.util.Map<java.lang.String, java.lang.Object> expectedMap = new java.util.HashMap<>();
        expectedMap.put("not", java.util.Collections.<java.lang.String, java.lang.Object>singletonMap("nop", "foo"));
        org.junit.Assert.assertEquals(expectedMap, actualMap);
    }

    @org.junit.Test
    public void testToJSON() {
        org.apache.ambari.server.collections.Predicate mockPredicate = createStrictMock(org.apache.ambari.server.collections.Predicate.class);
        EasyMock.expect(mockPredicate.toMap()).andReturn(java.util.Collections.singletonMap("nop", "foo")).times(1);
        replayAll();
        org.apache.ambari.server.collections.functors.NotPredicate predicate = new org.apache.ambari.server.collections.functors.NotPredicate(mockPredicate);
        java.lang.String actualJSON = predicate.toJSON();
        verifyAll();
        java.lang.String expectedJSON = "{\"not\":{\"nop\":\"foo\"}}";
        org.junit.Assert.assertEquals(expectedJSON, actualJSON);
    }
}