package org.apache.ambari.server.collections;
import org.apache.commons.lang.StringUtils;
public class PredicateUtils {
    private static final java.lang.reflect.Type PARSED_TYPE = new com.google.common.reflect.TypeToken<java.util.Map<java.lang.String, java.lang.Object>>() {}.getType();

    public static java.util.Map<java.lang.String, java.lang.Object> toMap(org.apache.ambari.server.collections.Predicate predicate) {
        return predicate == null ? null : predicate.toMap();
    }

    public static org.apache.ambari.server.collections.Predicate fromMap(java.util.Map<?, ?> map) {
        org.apache.ambari.server.collections.Predicate predicate = null;
        if ((map != null) && (!map.isEmpty())) {
            if (map.size() == 1) {
                java.util.Map.Entry<?, ?> entry = map.entrySet().iterator().next();
                java.lang.String name = java.util.Objects.toString(entry.getKey());
                java.lang.Class<? extends org.apache.ambari.server.collections.Predicate> predicateClass = org.apache.ambari.server.collections.functors.PredicateClassFactory.getPredicateClass(name);
                if (predicateClass == null) {
                    throw new java.lang.IllegalArgumentException(java.lang.String.format("Unexpected predicate name - %s", name));
                } else {
                    try {
                        java.lang.reflect.Method method = predicateClass.getMethod("fromMap", java.util.Map.class);
                        if (method == null) {
                            throw new java.lang.UnsupportedOperationException(java.lang.String.format("Cannot translate data to a %s - %s", predicateClass.getName(), "Failed to find toMap method"));
                        } else {
                            predicate = ((org.apache.ambari.server.collections.Predicate) (method.invoke(null, java.util.Collections.singletonMap(name, entry.getValue()))));
                        }
                    } catch (java.lang.NoSuchMethodException | java.lang.reflect.InvocationTargetException | java.lang.IllegalAccessException e) {
                        throw new java.lang.UnsupportedOperationException(java.lang.String.format("Cannot translate data to a %s - %s", predicateClass.getName(), e.getLocalizedMessage()), e);
                    }
                }
            } else {
                throw new java.lang.IllegalArgumentException(java.lang.String.format("Too many map entries have been encountered - %d", map.size()));
            }
        }
        return predicate;
    }

    public static java.lang.String toJSON(org.apache.ambari.server.collections.Predicate predicate) {
        return predicate == null ? null : predicate.toJSON();
    }

    public static org.apache.ambari.server.collections.Predicate fromJSON(java.lang.String json) {
        java.util.Map<java.lang.String, java.lang.Object> map = new com.google.gson.Gson().fromJson(json, org.apache.ambari.server.collections.PredicateUtils.PARSED_TYPE);
        return org.apache.commons.lang.StringUtils.isEmpty(json) ? null : org.apache.ambari.server.collections.PredicateUtils.fromMap(map);
    }
}