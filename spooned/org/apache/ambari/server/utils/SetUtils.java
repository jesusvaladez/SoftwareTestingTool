package org.apache.ambari.server.utils;
public class SetUtils {
    public static <T> java.util.List<java.util.Set<T>> split(java.util.Set<T> original, int subsetSize) {
        if (subsetSize <= 0) {
            throw new java.lang.IllegalArgumentException("Incorrect max size");
        }
        if ((original == null) || original.isEmpty()) {
            return java.util.Collections.emptyList();
        }
        int subsetCount = ((int) (java.lang.Math.ceil(((double) (original.size())) / subsetSize)));
        java.util.ArrayList<java.util.Set<T>> subsets = new java.util.ArrayList<>(subsetCount);
        java.util.Iterator<T> iterator = original.iterator();
        for (int i = 0; i < subsetCount; i++) {
            java.util.Set<T> subset = new java.util.LinkedHashSet<>(subsetSize);
            for (int j = 0; (j < subsetSize) && iterator.hasNext(); j++) {
                subset.add(iterator.next());
            }
            subsets.add(subset);
        }
        return subsets;
    }
}