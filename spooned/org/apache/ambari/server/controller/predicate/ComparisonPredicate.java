package org.apache.ambari.server.controller.predicate;
public abstract class ComparisonPredicate<T> extends org.apache.ambari.server.controller.predicate.PropertyPredicate implements org.apache.ambari.server.controller.predicate.BasePredicate {
    private final java.lang.Comparable<T> value;

    private final java.lang.String stringValue;

    private final java.lang.Double doubleValue;

    protected ComparisonPredicate(java.lang.String propertyId, java.lang.Comparable<T> value) {
        super(propertyId);
        this.value = value;
        if (value instanceof java.lang.Number) {
            stringValue = null;
            doubleValue = ((java.lang.Number) (value)).doubleValue();
        } else if (value instanceof java.lang.String) {
            stringValue = ((java.lang.String) (value));
            doubleValue = stringToDouble(stringValue);
        } else {
            stringValue = null;
            doubleValue = null;
        }
    }

    public java.lang.Comparable<T> getValue() {
        return value;
    }

    @java.lang.Override
    public boolean equals(java.lang.Object o) {
        if (this == o)
            return true;

        if (!(o instanceof org.apache.ambari.server.controller.predicate.ComparisonPredicate))
            return false;

        if (!super.equals(o))
            return false;

        org.apache.ambari.server.controller.predicate.ComparisonPredicate that = ((org.apache.ambari.server.controller.predicate.ComparisonPredicate) (o));
        return !(value != null ? !value.equals(that.value) : that.value != null);
    }

    @java.lang.Override
    public int hashCode() {
        int result = super.hashCode();
        result = (31 * result) + (value != null ? value.hashCode() : 0);
        return result;
    }

    @java.lang.Override
    public void accept(org.apache.ambari.server.controller.predicate.PredicateVisitor visitor) {
        visitor.acceptComparisonPredicate(this);
    }

    protected int compareValueToIgnoreCase(java.lang.Object propertyValue) throws java.lang.ClassCastException {
        return compareValueTo(propertyValue, true);
    }

    protected int compareValueTo(java.lang.Object propertyValue) throws java.lang.ClassCastException {
        return compareValueTo(propertyValue, false);
    }

    private int compareValueTo(java.lang.Object propertyValue, boolean ignoreCase) throws java.lang.ClassCastException {
        if (doubleValue != null) {
            if (propertyValue instanceof java.lang.Number) {
                return doubleValue.compareTo(((java.lang.Number) (propertyValue)).doubleValue());
            } else if (propertyValue instanceof java.lang.String) {
                java.lang.Double doubleFromString = stringToDouble(((java.lang.String) (propertyValue)));
                if (doubleFromString != null) {
                    return doubleValue.compareTo(doubleFromString);
                }
            }
        }
        if (stringValue != null) {
            if (ignoreCase) {
                return stringValue.compareToIgnoreCase(propertyValue.toString());
            } else {
                return stringValue.compareTo(propertyValue.toString());
            }
        }
        return getValue().compareTo(((T) (propertyValue)));
    }

    private java.lang.Double stringToDouble(java.lang.String stringValue) {
        if ((stringValue == null) || stringValue.isEmpty()) {
            return null;
        }
        java.text.ParsePosition parsePosition = new java.text.ParsePosition(0);
        java.text.NumberFormat numberFormat = java.text.NumberFormat.getInstance();
        java.lang.Number parsedNumber = numberFormat.parse(stringValue, parsePosition);
        return parsePosition.getIndex() == stringValue.length() ? parsedNumber.doubleValue() : null;
    }

    public abstract java.lang.String getOperator();

    public abstract org.apache.ambari.server.controller.predicate.ComparisonPredicate<T> copy(java.lang.String propertyId);

    @java.lang.Override
    public java.lang.String toString() {
        return (getPropertyId() + getOperator()) + getValue();
    }
}