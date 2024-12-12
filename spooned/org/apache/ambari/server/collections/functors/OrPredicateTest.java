package org.apache.ambari.server.collections.functors;
import org.easymock.EasyMockSupport;
import static org.easymock.EasyMock.expect;
public class OrPredicateTest extends org.easymock.EasyMockSupport {
    @org.junit.Test
    public void testEvaluate() {
        org.apache.ambari.server.collections.Predicate mockPredicate1 = createStrictMock(org.apache.ambari.server.collections.Predicate.class);
        EasyMock.expect(mockPredicate1.evaluate("context")).andReturn(true).times(1);
        EasyMock.expect(mockPredicate1.evaluate("context")).andReturn(false).times(1);
        EasyMock.expect(mockPredicate1.evaluate("context")).andReturn(true).times(1);
        org.apache.ambari.server.collections.Predicate mockPredicate2 = createStrictMock(org.apache.ambari.server.collections.Predicate.class);
        EasyMock.expect(mockPredicate2.evaluate("context")).andReturn(true).times(1);
        replayAll();
        org.apache.ambari.server.collections.functors.OrPredicate predicate = new org.apache.ambari.server.collections.functors.OrPredicate(mockPredicate1, mockPredicate2);
        predicate.evaluate("context");
        predicate.evaluate("context");
        predicate.evaluate("context");
        verifyAll();
        org.junit.Assert.assertArrayEquals(new org.apache.ambari.server.collections.Predicate[]{ mockPredicate1, mockPredicate2 }, predicate.getPredicates());
    }

    @org.junit.Test
    public void testToMap() {
        org.apache.ambari.server.collections.Predicate mockPredicate1 = createStrictMock(org.apache.ambari.server.collections.Predicate.class);
        EasyMock.expect(mockPredicate1.toMap()).andReturn(java.util.Collections.singletonMap("nop", "foo")).times(1);
        org.apache.ambari.server.collections.Predicate mockPredicate2 = createStrictMock(org.apache.ambari.server.collections.Predicate.class);
        EasyMock.expect(mockPredicate2.toMap()).andReturn(java.util.Collections.singletonMap("nop", "baz")).times(1);
        replayAll();
        org.apache.ambari.server.collections.functors.OrPredicate predicate = new org.apache.ambari.server.collections.functors.OrPredicate(mockPredicate1, mockPredicate2);
        java.util.Map<java.lang.String, java.lang.Object> actualMap = predicate.toMap();
        verifyAll();
        java.util.Map<java.lang.String, java.lang.Object> expectedMap = new java.util.HashMap<>();
        expectedMap.put("or", new java.util.ArrayList<>(java.util.Arrays.asList(java.util.Collections.<java.lang.String, java.lang.Object>singletonMap("nop", "foo"), java.util.Collections.<java.lang.String, java.lang.Object>singletonMap("nop", "baz"))));
        org.junit.Assert.assertEquals(expectedMap, actualMap);
    }

    @org.junit.Test
    public void testToJSON() {
        org.apache.ambari.server.collections.Predicate mockPredicate1 = createStrictMock(org.apache.ambari.server.collections.Predicate.class);
        EasyMock.expect(mockPredicate1.toMap()).andReturn(java.util.Collections.singletonMap("nop", "foo")).times(1);
        org.apache.ambari.server.collections.Predicate mockPredicate2 = createStrictMock(org.apache.ambari.server.collections.Predicate.class);
        EasyMock.expect(mockPredicate2.toMap()).andReturn(java.util.Collections.singletonMap("nop", "baz")).times(1);
        replayAll();
        org.apache.ambari.server.collections.functors.OrPredicate predicate = new org.apache.ambari.server.collections.functors.OrPredicate(mockPredicate1, mockPredicate2);
        java.lang.String actualJSON = predicate.toJSON();
        verifyAll();
        java.lang.String expectedJSON = "{\"or\":[{\"nop\":\"foo\"},{\"nop\":\"baz\"}]}";
        org.junit.Assert.assertEquals(expectedJSON, actualJSON);
    }
}