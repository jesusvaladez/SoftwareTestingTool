package org.apache.ambari.server.collections;
public class PredicateUtilsTest {
    @org.junit.Test
    public void toMap() throws java.lang.Exception {
        junit.framework.Assert.assertNull(org.apache.ambari.server.collections.PredicateUtils.toMap(null));
        junit.framework.Assert.assertEquals(createMap(), org.apache.ambari.server.collections.PredicateUtils.toMap(createPredicate()));
    }

    @org.junit.Test
    public void fromMap() throws java.lang.Exception {
        verifyPredicate(org.apache.ambari.server.collections.PredicateUtils.fromMap(createMap()));
    }

    @org.junit.Test
    public void toJSON() throws java.lang.Exception {
        junit.framework.Assert.assertNull(org.apache.ambari.server.collections.PredicateUtils.toJSON(null));
        junit.framework.Assert.assertEquals(createJSON(), org.apache.ambari.server.collections.PredicateUtils.toJSON(createPredicate()));
    }

    @org.junit.Test
    public void fromJSON() throws java.lang.Exception {
        verifyPredicate(org.apache.ambari.server.collections.PredicateUtils.fromJSON(createJSON()));
    }

    private org.apache.ambari.server.collections.Predicate createPredicate() {
        org.apache.ambari.server.collections.functors.ContextTransformer transformer1 = new org.apache.ambari.server.collections.functors.ContextTransformer("services");
        org.apache.ambari.server.collections.functors.ContextTransformer transformer2 = new org.apache.ambari.server.collections.functors.ContextTransformer("configurations/service-env/property1");
        org.apache.ambari.server.collections.functors.ContextTransformer transformer3 = new org.apache.ambari.server.collections.functors.ContextTransformer("configurations/cluster-env/property1");
        org.apache.ambari.server.collections.functors.ContainsPredicate predicate1 = new org.apache.ambari.server.collections.functors.ContainsPredicate(transformer1, "HDFS");
        org.apache.ambari.server.collections.functors.EqualsPredicate predicate2 = new org.apache.ambari.server.collections.functors.EqualsPredicate(transformer2, "true");
        org.apache.ambari.server.collections.functors.EqualsPredicate predicate3 = new org.apache.ambari.server.collections.functors.EqualsPredicate(transformer3, "false");
        org.apache.ambari.server.collections.functors.AndPredicate andPredicate = new org.apache.ambari.server.collections.functors.AndPredicate(predicate1, predicate2);
        org.apache.ambari.server.collections.functors.OrPredicate orPredicate = new org.apache.ambari.server.collections.functors.OrPredicate(predicate3, andPredicate);
        return new org.apache.ambari.server.collections.functors.NotPredicate(orPredicate);
    }

    private java.util.Map<java.lang.String, java.lang.Object> createMap() {
        java.util.Map<java.lang.String, java.lang.Object> andMap = java.util.Collections.singletonMap(org.apache.ambari.server.collections.functors.AndPredicate.NAME, java.util.Arrays.asList(java.util.Collections.<java.lang.String, java.lang.Object>singletonMap(org.apache.ambari.server.collections.functors.ContainsPredicate.NAME, java.util.Arrays.asList("services", "HDFS")), java.util.Collections.<java.lang.String, java.lang.Object>singletonMap(org.apache.ambari.server.collections.functors.EqualsPredicate.NAME, java.util.Arrays.asList("configurations/service-env/property1", "true"))));
        java.util.Map<java.lang.String, java.lang.Object> orMap = java.util.Collections.singletonMap(org.apache.ambari.server.collections.functors.OrPredicate.NAME, java.util.Arrays.asList(java.util.Collections.<java.lang.String, java.lang.Object>singletonMap(org.apache.ambari.server.collections.functors.EqualsPredicate.NAME, java.util.Arrays.asList("configurations/cluster-env/property1", "false")), andMap));
        return java.util.Collections.singletonMap(org.apache.ambari.server.collections.functors.NotPredicate.NAME, orMap);
    }

    private java.lang.String createJSON() {
        java.lang.String andJSON = "{\"and\":[{\"contains\":[\"services\",\"HDFS\"]},{\"equals\":[\"configurations/service-env/property1\",\"true\"]}]}";
        java.lang.String orJSON = ("{\"or\":[{\"equals\":[\"configurations/cluster-env/property1\",\"false\"]}," + andJSON) + "]}";
        return ("{\"not\":" + orJSON) + "}";
    }

    private void verifyPredicate(org.apache.ambari.server.collections.Predicate predicate) {
        junit.framework.Assert.assertNotNull(predicate);
        junit.framework.Assert.assertEquals(org.apache.ambari.server.collections.functors.NotPredicate.NAME, predicate.getName());
        junit.framework.Assert.assertTrue(predicate instanceof org.apache.ambari.server.collections.functors.NotPredicate);
        org.apache.ambari.server.collections.collections[] predicates;
        predicates = ((org.apache.ambari.server.collections.functors.NotPredicate) (predicate)).getPredicates();
        junit.framework.Assert.assertEquals(1, predicates.length);
        junit.framework.Assert.assertNotNull(predicates[0]);
        junit.framework.Assert.assertTrue(predicates[0] instanceof org.apache.ambari.server.collections.functors.OrPredicate);
        junit.framework.Assert.assertEquals(org.apache.ambari.server.collections.functors.OrPredicate.NAME, ((org.apache.ambari.server.collections.functors.OrPredicate) (predicates[0])).getName());
        predicates = ((org.apache.ambari.server.collections.functors.OrPredicate) (predicates[0])).getPredicates();
        junit.framework.Assert.assertEquals(2, predicates.length);
        junit.framework.Assert.assertNotNull(predicates[0]);
        junit.framework.Assert.assertTrue(predicates[0] instanceof org.apache.ambari.server.collections.functors.EqualsPredicate);
        junit.framework.Assert.assertEquals(org.apache.ambari.server.collections.functors.EqualsPredicate.NAME, ((org.apache.ambari.server.collections.functors.EqualsPredicate) (predicates[0])).getName());
        junit.framework.Assert.assertEquals("configurations/cluster-env/property1", ((org.apache.ambari.server.collections.functors.EqualsPredicate) (predicates[0])).getContextKey());
        junit.framework.Assert.assertEquals("false", ((org.apache.ambari.server.collections.functors.EqualsPredicate) (predicates[0])).getValue());
        junit.framework.Assert.assertNotNull(predicates[1]);
        junit.framework.Assert.assertTrue(predicates[1] instanceof org.apache.ambari.server.collections.functors.AndPredicate);
        junit.framework.Assert.assertEquals(org.apache.ambari.server.collections.functors.AndPredicate.NAME, ((org.apache.ambari.server.collections.functors.AndPredicate) (predicates[1])).getName());
        predicates = ((org.apache.ambari.server.collections.functors.AndPredicate) (predicates[1])).getPredicates();
        junit.framework.Assert.assertEquals(2, predicates.length);
        junit.framework.Assert.assertNotNull(predicates[0]);
        junit.framework.Assert.assertTrue(predicates[0] instanceof org.apache.ambari.server.collections.functors.ContainsPredicate);
        junit.framework.Assert.assertEquals(org.apache.ambari.server.collections.functors.ContainsPredicate.NAME, ((org.apache.ambari.server.collections.functors.ContainsPredicate) (predicates[0])).getName());
        junit.framework.Assert.assertEquals("services", ((org.apache.ambari.server.collections.functors.ContainsPredicate) (predicates[0])).getContextKey());
        junit.framework.Assert.assertEquals("HDFS", ((org.apache.ambari.server.collections.functors.ContainsPredicate) (predicates[0])).getValue());
        junit.framework.Assert.assertNotNull(predicates[1]);
        junit.framework.Assert.assertTrue(predicates[1] instanceof org.apache.ambari.server.collections.functors.EqualsPredicate);
        junit.framework.Assert.assertEquals(org.apache.ambari.server.collections.functors.EqualsPredicate.NAME, ((org.apache.ambari.server.collections.functors.EqualsPredicate) (predicates[1])).getName());
        junit.framework.Assert.assertEquals("configurations/service-env/property1", ((org.apache.ambari.server.collections.functors.EqualsPredicate) (predicates[1])).getContextKey());
        junit.framework.Assert.assertEquals("true", ((org.apache.ambari.server.collections.functors.EqualsPredicate) (predicates[1])).getValue());
    }
}