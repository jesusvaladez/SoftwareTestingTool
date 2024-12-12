package org.apache.ambari.server.controller.utilities;
public class PredicateBuilder {
    private java.lang.String propertyId;

    private java.util.List<org.apache.ambari.server.controller.spi.Predicate> predicates = new java.util.LinkedList<>();

    private org.apache.ambari.server.controller.utilities.PredicateBuilder.Operator operator = null;

    private final org.apache.ambari.server.controller.utilities.PredicateBuilder outer;

    private boolean done = false;

    private boolean not = false;

    public PredicateBuilder() {
        this.outer = null;
    }

    private PredicateBuilder(org.apache.ambari.server.controller.utilities.PredicateBuilder outer) {
        this.outer = outer;
    }

    public PredicateBuilder(org.apache.ambari.server.controller.spi.Predicate predicate) {
        this.outer = null;
        this.addPredicate(predicate);
    }

    private enum Operator {

        And,
        Or;}

    public org.apache.ambari.server.controller.utilities.PredicateBuilder.PredicateBuilderWithProperty property(java.lang.String id) {
        checkDone();
        propertyId = id;
        return new org.apache.ambari.server.controller.utilities.PredicateBuilder.PredicateBuilderWithProperty();
    }

    public org.apache.ambari.server.controller.utilities.PredicateBuilder not() {
        not = true;
        return this;
    }

    public org.apache.ambari.server.controller.utilities.PredicateBuilder begin() {
        checkDone();
        return new org.apache.ambari.server.controller.utilities.PredicateBuilder(this);
    }

    public org.apache.ambari.server.controller.spi.Predicate toPredicate() {
        return getPredicate();
    }

    private void checkDone() {
        if (done) {
            throw new java.lang.IllegalStateException("Can't reuse a predicate builder.");
        }
    }

    private org.apache.ambari.server.controller.utilities.PredicateBuilder.PredicateBuilderWithPredicate getPredicateBuilderWithPredicate() {
        return new org.apache.ambari.server.controller.utilities.PredicateBuilder.PredicateBuilderWithPredicate();
    }

    private void addPredicate(org.apache.ambari.server.controller.spi.Predicate predicate) {
        predicates.add(predicate);
    }

    private void handleComparator() {
        if (operator == null) {
            return;
        }
        if (predicates.size() == 0) {
            throw new java.lang.IllegalStateException("No left operand.");
        }
        org.apache.ambari.server.controller.spi.Predicate predicate;
        switch (operator) {
            case And :
                predicate = new org.apache.ambari.server.controller.predicate.AndPredicate(predicates.toArray(new org.apache.ambari.server.controller.spi.Predicate[predicates.size()]));
                break;
            case Or :
                predicate = new org.apache.ambari.server.controller.predicate.OrPredicate(predicates.toArray(new org.apache.ambari.server.controller.spi.Predicate[predicates.size()]));
                break;
            default :
                throw new java.lang.IllegalStateException("Unknown operator " + this.operator);
        }
        predicates.clear();
        addPredicate(predicate);
    }

    private org.apache.ambari.server.controller.spi.Predicate getPredicate() {
        handleComparator();
        if (predicates.size() == 1) {
            org.apache.ambari.server.controller.spi.Predicate predicate = predicates.get(0);
            if (not) {
                predicate = new org.apache.ambari.server.controller.predicate.NotPredicate(predicate);
                not = false;
            }
            return predicate;
        }
        throw new java.lang.IllegalStateException("Can't return a predicate.");
    }

    public class PredicateBuilderWithProperty {
        public <T> org.apache.ambari.server.controller.utilities.PredicateBuilder.PredicateBuilderWithPredicate equals(java.lang.Comparable<T> value) {
            if (propertyId == null) {
                throw new java.lang.IllegalStateException("No property.");
            }
            addPredicate(new org.apache.ambari.server.controller.predicate.EqualsPredicate<>(propertyId, value));
            return new org.apache.ambari.server.controller.utilities.PredicateBuilder.PredicateBuilderWithPredicate();
        }

        public <T> org.apache.ambari.server.controller.utilities.PredicateBuilder.PredicateBuilderWithPredicate greaterThan(java.lang.Comparable<T> value) {
            if (propertyId == null) {
                throw new java.lang.IllegalStateException("No property.");
            }
            addPredicate(new org.apache.ambari.server.controller.predicate.GreaterPredicate<>(propertyId, value));
            return new org.apache.ambari.server.controller.utilities.PredicateBuilder.PredicateBuilderWithPredicate();
        }

        public <T> org.apache.ambari.server.controller.utilities.PredicateBuilder.PredicateBuilderWithPredicate greaterThanEqualTo(java.lang.Comparable<T> value) {
            if (propertyId == null) {
                throw new java.lang.IllegalStateException("No property.");
            }
            addPredicate(new org.apache.ambari.server.controller.predicate.GreaterEqualsPredicate<>(propertyId, value));
            return new org.apache.ambari.server.controller.utilities.PredicateBuilder.PredicateBuilderWithPredicate();
        }

        public <T> org.apache.ambari.server.controller.utilities.PredicateBuilder.PredicateBuilderWithPredicate lessThan(java.lang.Comparable<T> value) {
            if (propertyId == null) {
                throw new java.lang.IllegalStateException("No property.");
            }
            addPredicate(new org.apache.ambari.server.controller.predicate.LessPredicate<>(propertyId, value));
            return new org.apache.ambari.server.controller.utilities.PredicateBuilder.PredicateBuilderWithPredicate();
        }

        public <T> org.apache.ambari.server.controller.utilities.PredicateBuilder.PredicateBuilderWithPredicate lessThanEqualTo(java.lang.Comparable<T> value) {
            if (propertyId == null) {
                throw new java.lang.IllegalStateException("No property.");
            }
            addPredicate(new org.apache.ambari.server.controller.predicate.LessEqualsPredicate<>(propertyId, value));
            return new org.apache.ambari.server.controller.utilities.PredicateBuilder.PredicateBuilderWithPredicate();
        }
    }

    public class PredicateBuilderWithPredicate {
        public org.apache.ambari.server.controller.utilities.PredicateBuilder and() {
            if (operator != org.apache.ambari.server.controller.utilities.PredicateBuilder.Operator.And) {
                handleComparator();
                operator = org.apache.ambari.server.controller.utilities.PredicateBuilder.Operator.And;
            }
            return PredicateBuilder.this;
        }

        public org.apache.ambari.server.controller.utilities.PredicateBuilder or() {
            if (operator != org.apache.ambari.server.controller.utilities.PredicateBuilder.Operator.Or) {
                handleComparator();
                operator = org.apache.ambari.server.controller.utilities.PredicateBuilder.Operator.Or;
            }
            return PredicateBuilder.this;
        }

        public org.apache.ambari.server.controller.spi.Predicate toPredicate() {
            if (outer != null) {
                throw new java.lang.IllegalStateException("Unbalanced block - missing end.");
            }
            done = true;
            return getPredicate();
        }

        public org.apache.ambari.server.controller.utilities.PredicateBuilder.PredicateBuilderWithPredicate end() {
            if (outer == null) {
                throw new java.lang.IllegalStateException("Unbalanced block - missing begin.");
            }
            outer.addPredicate(getPredicate());
            return outer.getPredicateBuilderWithPredicate();
        }
    }
}