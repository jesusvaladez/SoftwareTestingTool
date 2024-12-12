package org.apache.ambari.server.orm.dao;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
@com.google.inject.Singleton
public class DaoUtils {
    public <T> java.util.List<T> selectAll(javax.persistence.EntityManager entityManager, java.lang.Class<T> entityClass) {
        javax.persistence.criteria.CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        javax.persistence.criteria.CriteriaQuery<T> query = criteriaBuilder.createQuery(entityClass);
        javax.persistence.criteria.Root<T> root = query.from(entityClass);
        query.select(root);
        javax.persistence.TypedQuery<T> typedQuery = entityManager.createQuery(query);
        try {
            return typedQuery.getResultList();
        } catch (javax.persistence.NoResultException ignored) {
            return java.util.Collections.emptyList();
        }
    }

    public <T> java.util.List<T> selectList(javax.persistence.TypedQuery<T> query, java.lang.Object... parameters) {
        setParameters(query, parameters);
        try {
            return query.getResultList();
        } catch (javax.persistence.NoResultException ignored) {
            return java.util.Collections.emptyList();
        }
    }

    public <T> T selectSingle(javax.persistence.TypedQuery<T> query, java.lang.Object... parameters) {
        setParameters(query, parameters);
        try {
            return query.getSingleResult();
        } catch (javax.persistence.NoResultException ignored) {
            return null;
        }
    }

    public <T> T selectOne(javax.persistence.TypedQuery<T> query, java.lang.Object... parameters) {
        setParameters(query, parameters);
        try {
            return query.setMaxResults(1).getSingleResult();
        } catch (javax.persistence.NoResultException ignored) {
            return null;
        }
    }

    public int executeUpdate(javax.persistence.Query query, java.lang.Object... parameters) {
        setParameters(query, parameters);
        return query.executeUpdate();
    }

    public void setParameters(javax.persistence.Query query, java.lang.Object... parameters) {
        for (int i = 0; i < parameters.length; i++) {
            query.setParameter(i + 1, parameters[i]);
        }
    }
}