package org.apache.ambari.server.collections.functors;
public class ContainsPredicate extends org.apache.ambari.server.collections.functors.OperationPredicate {
    public static final java.lang.String NAME = "contains";

    private final java.lang.String value;

    public static org.apache.ambari.server.collections.functors.ContainsPredicate fromMap(java.util.Map<java.lang.String, java.lang.Object> map) {
        java.lang.Object data = (map == null) ? null : map.get(org.apache.ambari.server.collections.functors.ContainsPredicate.NAME);
        if (data == null) {
            throw new java.lang.IllegalArgumentException(("Missing data for '" + org.apache.ambari.server.collections.functors.ContainsPredicate.NAME) + "' operation");
        } else if (data instanceof java.util.Collection) {
            java.util.Collection<?> collection = ((java.util.Collection) (data));
            if (collection.size() == 2) {
                java.util.Iterator<?> iterator = collection.iterator();
                java.lang.Object d1 = iterator.next();
                java.lang.Object d2 = iterator.next();
                if ((d1 instanceof java.lang.String) && (d2 instanceof java.lang.String)) {
                    return new org.apache.ambari.server.collections.functors.ContainsPredicate(new org.apache.ambari.server.collections.functors.ContextTransformer(((java.lang.String) (d1))), ((java.lang.String) (d2)));
                } else {
                    throw new java.lang.IllegalArgumentException(java.lang.String.format("Unexpected data types: %s and %s", d1.getClass().getName(), d2.getClass().getName()));
                }
            } else {
                throw new java.lang.IllegalArgumentException(java.lang.String.format(("Missing data for '" + org.apache.ambari.server.collections.functors.ContainsPredicate.NAME) + "' operation - 2 predicates are needed, %d found", collection.size()));
            }
        } else {
            throw new java.lang.IllegalArgumentException(java.lang.String.format(("Unexpected data type for '" + org.apache.ambari.server.collections.functors.ContainsPredicate.NAME) + "' operation - %s", data.getClass().getName()));
        }
    }

    public ContainsPredicate(org.apache.ambari.server.collections.functors.ContextTransformer transformer, java.lang.String value) {
        super(org.apache.ambari.server.collections.functors.ContainsPredicate.NAME, transformer);
        this.value = value;
    }

    public java.lang.String getValue() {
        return value;
    }

    @java.lang.Override
    protected boolean evaluateTransformedData(java.lang.Object data) {
        return ((this.value != null) && (data instanceof java.util.Set)) && ((java.util.Set<?>) (data)).contains(this.value);
    }

    @java.lang.Override
    public java.util.Map<java.lang.String, java.lang.Object> toMap() {
        return java.util.Collections.singletonMap(org.apache.ambari.server.collections.functors.ContainsPredicate.NAME, new java.util.ArrayList<>(java.util.Arrays.asList(getContextKey(), value)));
    }

    @java.lang.Override
    public int hashCode() {
        return super.hashCode() + (37 * (this.value == null ? 0 : this.value.hashCode()));
    }

    @java.lang.Override
    public boolean equals(java.lang.Object obj) {
        if (obj == this) {
            return true;
        } else if (obj == null) {
            return false;
        } else if ((super.equals(obj) && (obj instanceof org.apache.ambari.server.collections.functors.ContainsPredicate)) && (hashCode() == obj.hashCode())) {
            org.apache.ambari.server.collections.functors.ContainsPredicate p = ((org.apache.ambari.server.collections.functors.ContainsPredicate) (obj));
            return value == null ? p.value == null : value.equals(p.value);
        } else {
            return false;
        }
    }
}