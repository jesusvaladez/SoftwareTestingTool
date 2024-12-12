package org.apache.ambari.server.orm.dao;
import com.google.inject.persist.Transactional;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
public class CrudDAO<E, K> {
    @com.google.inject.Inject
    com.google.inject.Provider<javax.persistence.EntityManager> entityManagerProvider;

    @com.google.inject.Inject
    org.apache.ambari.server.orm.dao.DaoUtils daoUtils;

    private java.lang.Class<E> entityClass;

    public CrudDAO(java.lang.Class<E> entityClass) {
        this.entityClass = entityClass;
    }

    @org.apache.ambari.server.orm.RequiresSession
    public E findByPK(K pk) {
        return entityManagerProvider.get().find(entityClass, pk);
    }

    @org.apache.ambari.server.orm.RequiresSession
    public java.util.List<E> findAll() {
        final javax.persistence.TypedQuery<E> query = entityManagerProvider.get().createQuery(("SELECT entity FROM " + entityClass.getSimpleName()) + " entity", entityClass);
        return daoUtils.selectList(query);
    }

    @org.apache.ambari.server.orm.RequiresSession
    public java.lang.Long findMaxId(java.lang.String idColName) {
        final javax.persistence.TypedQuery<java.lang.Long> query = entityManagerProvider.get().createQuery(((("SELECT MAX(entity." + idColName) + ") FROM ") + entityClass.getSimpleName()) + " entity", java.lang.Long.class);
        java.lang.Long result = daoUtils.selectOne(query);
        return result == null ? 0 : result;
    }

    @com.google.inject.persist.Transactional
    protected void create(E entity) {
        entityManagerProvider.get().persist(entity);
    }

    @com.google.inject.persist.Transactional
    public E merge(E entity) {
        return entityManagerProvider.get().merge(entity);
    }

    @com.google.inject.persist.Transactional
    public void refresh(E entity) {
        entityManagerProvider.get().refresh(entity);
    }

    @com.google.inject.persist.Transactional
    public void remove(E entity) {
        entityManagerProvider.get().remove(merge(entity));
        entityManagerProvider.get().getEntityManagerFactory().getCache().evictAll();
    }

    @com.google.inject.persist.Transactional
    public void remove(java.util.Collection<E> entities) {
        for (E entity : entities) {
            entityManagerProvider.get().remove(merge(entity));
        }
        entityManagerProvider.get().getEntityManagerFactory().getCache().evictAll();
    }

    @com.google.inject.persist.Transactional
    public void removeByPK(K pk) {
        remove(findByPK(pk));
    }
}