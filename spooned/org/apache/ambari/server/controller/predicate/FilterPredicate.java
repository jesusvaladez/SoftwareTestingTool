package org.apache.ambari.server.controller.predicate;
public class FilterPredicate extends org.apache.ambari.server.controller.predicate.ComparisonPredicate {
    private final java.util.regex.Matcher matcher;

    private final java.lang.String patternExpr;

    private final java.lang.String emptyString = "";

    @java.lang.SuppressWarnings("unchecked")
    public FilterPredicate(java.lang.String propertyId, java.lang.String patternExpr) {
        super(propertyId, patternExpr);
        this.patternExpr = patternExpr;
        try {
            java.util.regex.Pattern pattern = java.util.regex.Pattern.compile(patternExpr != null ? patternExpr : emptyString);
            matcher = pattern.matcher(emptyString);
        } catch (java.util.regex.PatternSyntaxException pe) {
            throw new java.lang.IllegalArgumentException(pe);
        }
    }

    @java.lang.Override
    public boolean evaluate(org.apache.ambari.server.controller.spi.Resource resource) {
        java.lang.Object propertyValue = resource.getPropertyValue(getPropertyId());
        matcher.reset(propertyValue != null ? propertyValue.toString() : emptyString);
        return patternExpr == null ? propertyValue == null : (propertyValue != null) && matcher.matches();
    }

    @java.lang.Override
    public java.lang.String getOperator() {
        return ".FILTER";
    }

    @java.lang.Override
    public org.apache.ambari.server.controller.predicate.ComparisonPredicate copy(java.lang.String propertyId) {
        return new org.apache.ambari.server.controller.predicate.FilterPredicate(propertyId, patternExpr);
    }

    @java.lang.Override
    public java.lang.String toString() {
        return (((getPropertyId() + getOperator()) + "(") + getValue()) + ")";
    }
}