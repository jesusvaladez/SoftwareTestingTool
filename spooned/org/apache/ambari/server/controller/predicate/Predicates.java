package org.apache.ambari.server.controller.predicate;
public class Predicates {
    public static java.util.Optional<org.apache.ambari.server.controller.spi.Predicate> anyOf(java.util.Collection<? extends org.apache.ambari.server.controller.spi.Predicate> predicates) {
        return (predicates != null) && (!predicates.isEmpty()) ? java.util.Optional.of(org.apache.ambari.server.controller.predicate.OrPredicate.of(predicates)) : java.util.Optional.empty();
    }

    public static java.util.function.Function<org.apache.ambari.server.controller.spi.Predicate, org.apache.ambari.server.controller.spi.Predicate> and(org.apache.ambari.server.controller.spi.Predicate presetPredicate) {
        return predicate -> new org.apache.ambari.server.controller.predicate.AndPredicate(presetPredicate, predicate);
    }
}