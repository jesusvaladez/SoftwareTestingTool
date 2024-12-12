package org.apache.ambari.server.collections.functors;
import org.apache.commons.collections.functors.EqualPredicate;
public class EqualsPredicate extends org.apache.ambari.server.collections.functors.OperationPredicate {
    public static final java.lang.String NAME = "equals";

    private final org.apache.commons.collections.functors.EqualPredicate delegate;

    public static org.apache.ambari.server.collections.functors.EqualsPredicate fromMap(java.util.Map<java.lang.String, java.lang.Object> map) {
        java.lang.Object data = (map == null) ? null : map.get(org.apache.ambari.server.collections.functors.EqualsPredicate.NAME);
        if (data == null) {
            throw new java.lang.IllegalArgumentException(("Missing data for '" + org.apache.ambari.server.collections.functors.EqualsPredicate.NAME) + "' operation");
        } else if (data instanceof java.util.Collection) {
            java.util.Collection<?> collection = ((java.util.Collection) (data));
            if (collection.size() == 2) {
                java.util.Iterator<?> iterator = collection.iterator();
                java.lang.Object d1 = iterator.next();
                java.lang.Object d2 = iterator.next();
                if ((d1 instanceof java.lang.String) && (d2 instanceof java.lang.String)) {
                    return new org.apache.ambari.server.collections.functors.EqualsPredicate(new org.apache.ambari.server.collections.functors.ContextTransformer(((java.lang.String) (d1))), ((java.lang.String) (d2)));
                } else {
                    throw new java.lang.IllegalArgumentException(java.lang.String.format("Unexpected data types: %s and %s", d1.getClass().getName(), d2.getClass().getName()));
                }
            } else {
                throw new java.lang.IllegalArgumentException(java.lang.String.format(("Missing data for '" + org.apache.ambari.server.collections.functors.EqualsPredicate.NAME) + "' operation - 2 predicates are needed, %d found", collection.size()));
            }
        } else {
            throw new java.lang.IllegalArgumentException(java.lang.String.format(("Unexpected data type for '" + org.apache.ambari.server.collections.functors.EqualsPredicate.NAME) + "' operation - %s", data.getClass().getName()));
        }
    }

    public EqualsPredicate(org.apache.ambari.server.collections.functors.ContextTransformer transformer, java.lang.String value) {
        super(org.apache.ambari.server.collections.functors.EqualsPredicate.NAME, transformer);
        delegate = new org.apache.commons.collections.functors.EqualPredicate(value);
    }

    public java.lang.String getValue() {
        java.lang.Object o = (delegate == null) ? null : delegate.getValue();
        return o == null ? null : o.toString();
    }

    @java.lang.Override
    public java.util.Map<java.lang.String, java.lang.Object> toMap() {
        return java.util.Collections.singletonMap(org.apache.ambari.server.collections.functors.EqualsPredicate.NAME, new java.util.ArrayList<>(java.util.Arrays.asList(getContextKey(), delegate.getValue().toString())));
    }

    @java.lang.Override
    public boolean evaluateTransformedData(java.lang.Object data) {
        return delegate.evaluate(data);
    }
}