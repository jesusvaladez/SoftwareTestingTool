package org.apache.ambari.server.utils;
public class SetUtilsTest {
    @org.junit.Test
    public void testSplit() {
        try {
            org.apache.ambari.server.utils.SetUtils.split(null, 0);
            junit.framework.Assert.fail("Expected IllegalArgumentException.");
        } catch (java.lang.IllegalArgumentException e) {
        }
        int size = 10;
        java.util.List<java.util.Set<java.lang.Integer>> subsets = org.apache.ambari.server.utils.SetUtils.split(null, size);
        junit.framework.Assert.assertEquals(0, subsets.size());
        subsets = org.apache.ambari.server.utils.SetUtils.split(java.util.Collections.<java.lang.Integer>emptySet(), size);
        junit.framework.Assert.assertEquals(0, subsets.size());
        subsets = org.apache.ambari.server.utils.SetUtils.split(java.util.Collections.singleton(0), size);
        junit.framework.Assert.assertEquals(1, subsets.size());
        junit.framework.Assert.assertEquals(1, subsets.get(0).size());
        java.util.Set<java.lang.Integer> set = new java.util.LinkedHashSet<>(5);
        for (int i = 0; i < 5; i++) {
            set.add(i);
        }
        subsets = org.apache.ambari.server.utils.SetUtils.split(set, size);
        junit.framework.Assert.assertEquals(1, subsets.size());
        junit.framework.Assert.assertEquals(5, subsets.get(0).size());
        set = new java.util.LinkedHashSet<>(10);
        for (int i = 0; i < 10; i++) {
            set.add(i);
        }
        subsets = org.apache.ambari.server.utils.SetUtils.split(set, size);
        junit.framework.Assert.assertEquals(1, subsets.size());
        junit.framework.Assert.assertEquals(10, subsets.get(0).size());
        set = new java.util.LinkedHashSet<>(11);
        for (int i = 0; i < 11; i++) {
            set.add(i);
        }
        subsets = org.apache.ambari.server.utils.SetUtils.split(set, size);
        junit.framework.Assert.assertEquals(2, subsets.size());
        junit.framework.Assert.assertEquals(10, subsets.get(0).size());
        junit.framework.Assert.assertEquals(1, subsets.get(1).size());
        set = new java.util.LinkedHashSet<>(20);
        for (int i = 0; i < 20; i++) {
            set.add(i);
        }
        subsets = org.apache.ambari.server.utils.SetUtils.split(set, size);
        junit.framework.Assert.assertEquals(2, subsets.size());
        junit.framework.Assert.assertEquals(10, subsets.get(0).size());
        junit.framework.Assert.assertEquals(10, subsets.get(1).size());
        set = new java.util.LinkedHashSet<>(27);
        for (int i = 0; i < 27; i++) {
            set.add(i);
        }
        subsets = org.apache.ambari.server.utils.SetUtils.split(set, size);
        junit.framework.Assert.assertEquals(3, subsets.size());
        junit.framework.Assert.assertEquals(10, subsets.get(0).size());
        junit.framework.Assert.assertEquals(10, subsets.get(1).size());
        junit.framework.Assert.assertEquals(7, subsets.get(2).size());
    }
}