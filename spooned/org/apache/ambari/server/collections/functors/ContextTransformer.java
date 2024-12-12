package org.apache.ambari.server.collections.functors;
import org.apache.commons.collections.Transformer;
public class ContextTransformer implements org.apache.commons.collections.Transformer {
    private final java.lang.String key;

    public ContextTransformer(java.lang.String key) {
        this.key = key;
    }

    public java.lang.String getKey() {
        return key;
    }

    @java.lang.Override
    public java.lang.Object transform(java.lang.Object o) {
        return transform(key, o);
    }

    private java.lang.Object transform(java.lang.String key, java.lang.Object o) {
        java.lang.Object transformedData = null;
        if (key != null) {
            if (o instanceof java.util.Map) {
                java.util.Map<?, ?> data = ((java.util.Map) (o));
                if (data.containsKey(key)) {
                    transformedData = data.get(key);
                } else {
                    java.lang.String[] parts = key.split("\\/", 2);
                    if (parts.length == 2) {
                        if (parts[0].isEmpty()) {
                            transformedData = transform(parts[1], o);
                        } else {
                            transformedData = transform(parts[1], data.get(parts[0]));
                        }
                    }
                }
            }
        }
        return transformedData;
    }

    @java.lang.Override
    public int hashCode() {
        return 37 * (key == null ? 0 : key.hashCode());
    }

    @java.lang.Override
    public boolean equals(java.lang.Object obj) {
        if (obj == this) {
            return true;
        } else if (obj == null) {
            return false;
        } else if ((obj instanceof org.apache.ambari.server.collections.functors.ContextTransformer) && (hashCode() == obj.hashCode())) {
            org.apache.ambari.server.collections.functors.ContextTransformer t = ((org.apache.ambari.server.collections.functors.ContextTransformer) (obj));
            return key == null ? t.key == null : key.equals(t.key);
        } else {
            return false;
        }
    }
}