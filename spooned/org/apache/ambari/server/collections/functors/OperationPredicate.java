package org.apache.ambari.server.collections.functors;
import org.apache.commons.collections.Transformer;
abstract class OperationPredicate extends org.apache.ambari.server.collections.Predicate {
    private org.apache.ambari.server.collections.functors.ContextTransformer transformer = null;

    OperationPredicate(java.lang.String name, org.apache.ambari.server.collections.functors.ContextTransformer transformer) {
        super(name);
        this.transformer = transformer;
    }

    public org.apache.ambari.server.collections.functors.ContextTransformer getTransformer() {
        return transformer;
    }

    public void setTransformer(org.apache.ambari.server.collections.functors.ContextTransformer transformer) {
        this.transformer = transformer;
    }

    public java.lang.String getContextKey() {
        return this.transformer == null ? null : this.transformer.getKey();
    }

    @java.lang.Override
    public boolean evaluate(java.lang.Object o) {
        java.lang.Object data = (transformer == null) ? o : transformer.transform(o);
        return evaluateTransformedData(data);
    }

    @java.lang.Override
    public int hashCode() {
        return super.hashCode() + (37 * (transformer == null ? 0 : transformer.hashCode()));
    }

    @java.lang.Override
    public boolean equals(java.lang.Object obj) {
        if (obj == this) {
            return true;
        } else if (obj == null) {
            return false;
        } else if ((super.equals(obj) && (obj instanceof org.apache.ambari.server.collections.functors.OperationPredicate)) && (hashCode() == obj.hashCode())) {
            org.apache.ambari.server.collections.functors.OperationPredicate p = ((org.apache.ambari.server.collections.functors.OperationPredicate) (obj));
            return transformer == null ? p.transformer == null : transformer.equals(p.transformer);
        } else {
            return false;
        }
    }

    protected abstract boolean evaluateTransformedData(java.lang.Object data);
}