package org.apache.ambari.server.api.query;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Root;
import javax.persistence.metamodel.SingularAttribute;
public abstract class JpaPredicateVisitor<T> implements org.apache.ambari.server.controller.predicate.PredicateVisitor {
    private javax.persistence.EntityManager m_entityManager;

    private javax.persistence.criteria.CriteriaBuilder m_builder;

    private final javax.persistence.criteria.Root<T> m_root;

    private final javax.persistence.criteria.CriteriaQuery<T> m_query;

    private org.apache.ambari.server.controller.spi.Predicate m_lastPredicate = null;

    private java.util.ArrayDeque<java.util.List<javax.persistence.criteria.Predicate>> m_queue = new java.util.ArrayDeque<>();

    public JpaPredicateVisitor(javax.persistence.EntityManager entityManager, java.lang.Class<T> entityClass) {
        m_entityManager = entityManager;
        m_builder = m_entityManager.getCriteriaBuilder();
        m_query = m_builder.createQuery(entityClass);
        m_root = m_query.from(entityClass);
    }

    public abstract java.lang.Class<T> getEntityClass();

    public abstract java.util.List<? extends javax.persistence.metamodel.SingularAttribute<?, ?>> getPredicateMapping(java.lang.String propertyId);

    public org.apache.ambari.server.controller.spi.Predicate getJpaPredicate() {
        return m_lastPredicate;
    }

    public javax.persistence.criteria.CriteriaQuery<T> getCriteriaQuery() {
        return m_query;
    }

    public javax.persistence.criteria.CriteriaBuilder getCriteriaBuilder() {
        return m_builder;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings({ "unchecked", "rawtypes" })
    public void acceptComparisonPredicate(org.apache.ambari.server.controller.predicate.ComparisonPredicate predicate) {
        java.lang.String propertyId = predicate.getPropertyId();
        java.util.List<? extends javax.persistence.metamodel.SingularAttribute<?, ?>> singularAttributes = getPredicateMapping(propertyId);
        if ((null == singularAttributes) || (singularAttributes.size() == 0)) {
            return;
        }
        javax.persistence.metamodel.SingularAttribute<?, ?> lastSingularAttribute = null;
        javax.persistence.criteria.Path<java.lang.Comparable> path = null;
        for (javax.persistence.metamodel.SingularAttribute<?, ?> singularAttribute : singularAttributes) {
            lastSingularAttribute = singularAttribute;
            if (singularAttribute != null) {
                if (null == path) {
                    path = m_root.get(singularAttribute.getName());
                } else {
                    path = path.get(singularAttribute.getName());
                }
            }
        }
        if (null == path) {
            return;
        }
        java.lang.String operator = predicate.getOperator();
        java.lang.Comparable value = predicate.getValue();
        if (lastSingularAttribute != null) {
            java.lang.Class<?> clazz = lastSingularAttribute.getJavaType();
            if (clazz.isEnum()) {
                java.lang.Class<? extends java.lang.Enum> enumClass = ((java.lang.Class<? extends java.lang.Enum>) (clazz));
                value = java.lang.Enum.valueOf(enumClass, value.toString());
            }
        }
        javax.persistence.criteria.Predicate jpaPredicate = null;
        if ("=".equals(operator)) {
            jpaPredicate = m_builder.equal(path, value);
        } else if ("<".equals(operator)) {
            jpaPredicate = m_builder.lessThan(path, value);
        } else if ("<=".equals(operator)) {
            jpaPredicate = m_builder.lessThanOrEqualTo(path, value);
        } else if (">".equals(operator)) {
            jpaPredicate = m_builder.greaterThan(path, value);
        } else if (">=".equals(operator)) {
            jpaPredicate = m_builder.greaterThanOrEqualTo(path, value);
        }
        if (null == jpaPredicate) {
            return;
        }
        if (null == m_queue.peekLast()) {
            m_lastPredicate = jpaPredicate;
        } else {
            m_queue.peekLast().add(jpaPredicate);
        }
    }

    @java.lang.Override
    public void acceptArrayPredicate(org.apache.ambari.server.controller.predicate.ArrayPredicate predicate) {
        org.apache.ambari.server.controller.spi.Predicate[] predicates = predicate.getPredicates();
        if (predicates.length == 0) {
            return;
        }
        java.util.List<javax.persistence.criteria.Predicate> predicateList = new java.util.ArrayList<>();
        m_queue.add(predicateList);
        java.lang.String operator = predicate.getOperator();
        for (int i = 0; i < predicates.length; i++) {
            org.apache.ambari.server.controller.utilities.PredicateHelper.visit(predicates[i], this);
        }
        javax.persistence.criteria.Predicate jpaPredicate = null;
        predicateList = m_queue.pollLast();
        if (predicateList != null) {
            org.apache.ambari.server.api.query.persistence[] array = new javax.persistence.criteria.Predicate[predicateList.size()];
            array = predicateList.toArray(array);
            if ("AND".equals(operator)) {
                jpaPredicate = m_builder.and(array);
            } else {
                jpaPredicate = m_builder.or(array);
            }
            if (null == m_queue.peekLast()) {
                m_lastPredicate = jpaPredicate;
            } else {
                m_queue.peekLast().add(jpaPredicate);
            }
        }
    }

    @java.lang.Override
    public void acceptUnaryPredicate(org.apache.ambari.server.controller.predicate.UnaryPredicate predicate) {
    }

    @java.lang.Override
    public void acceptAlwaysPredicate(org.apache.ambari.server.controller.predicate.AlwaysPredicate predicate) {
    }

    @java.lang.Override
    public void acceptCategoryPredicate(org.apache.ambari.server.controller.predicate.CategoryPredicate predicate) {
    }
}