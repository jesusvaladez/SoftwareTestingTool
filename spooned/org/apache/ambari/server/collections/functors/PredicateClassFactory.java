package org.apache.ambari.server.collections.functors;
public class PredicateClassFactory {
    private static final java.util.Map<java.lang.String, java.lang.Class<? extends org.apache.ambari.server.collections.Predicate>> NAME_TO_CLASS;

    static {
        java.util.Map<java.lang.String, java.lang.Class<? extends org.apache.ambari.server.collections.Predicate>> map = new java.util.HashMap<>();
        map.put(org.apache.ambari.server.collections.functors.AndPredicate.NAME, org.apache.ambari.server.collections.functors.AndPredicate.class);
        map.put(org.apache.ambari.server.collections.functors.OrPredicate.NAME, org.apache.ambari.server.collections.functors.OrPredicate.class);
        map.put(org.apache.ambari.server.collections.functors.NotPredicate.NAME, org.apache.ambari.server.collections.functors.NotPredicate.class);
        map.put(org.apache.ambari.server.collections.functors.ContainsPredicate.NAME, org.apache.ambari.server.collections.functors.ContainsPredicate.class);
        map.put(org.apache.ambari.server.collections.functors.EqualsPredicate.NAME, org.apache.ambari.server.collections.functors.EqualsPredicate.class);
        NAME_TO_CLASS = java.util.Collections.unmodifiableMap(map);
    }

    public static java.lang.Class<? extends org.apache.ambari.server.collections.Predicate> getPredicateClass(java.lang.String name) {
        return name == null ? null : org.apache.ambari.server.collections.functors.PredicateClassFactory.NAME_TO_CLASS.get(name);
    }
}