package org.apache.ambari.server.orm.dao;
import com.google.inject.persist.Transactional;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
@com.google.inject.Singleton
public class BlueprintDAO {
    @com.google.inject.Inject
    com.google.inject.Provider<javax.persistence.EntityManager> entityManagerProvider;

    @com.google.inject.Inject
    org.apache.ambari.server.orm.dao.StackDAO stackDAO;

    @org.apache.ambari.server.orm.RequiresSession
    public org.apache.ambari.server.orm.entities.BlueprintEntity findByName(java.lang.String blueprint_name) {
        return entityManagerProvider.get().find(org.apache.ambari.server.orm.entities.BlueprintEntity.class, blueprint_name);
    }

    @org.apache.ambari.server.orm.RequiresSession
    public java.util.List<org.apache.ambari.server.orm.entities.BlueprintEntity> findAll() {
        javax.persistence.TypedQuery<org.apache.ambari.server.orm.entities.BlueprintEntity> query = entityManagerProvider.get().createNamedQuery("allBlueprints", org.apache.ambari.server.orm.entities.BlueprintEntity.class);
        return query.getResultList();
    }

    @com.google.inject.persist.Transactional
    public void refresh(org.apache.ambari.server.orm.entities.BlueprintEntity blueprintEntity) {
        ensureStackIdSet(blueprintEntity);
        entityManagerProvider.get().refresh(blueprintEntity);
    }

    @com.google.inject.persist.Transactional
    public void create(org.apache.ambari.server.orm.entities.BlueprintEntity blueprintEntity) {
        ensureStackIdSet(blueprintEntity);
        entityManagerProvider.get().persist(blueprintEntity);
    }

    @com.google.inject.persist.Transactional
    public org.apache.ambari.server.orm.entities.BlueprintEntity merge(org.apache.ambari.server.orm.entities.BlueprintEntity blueprintEntity) {
        ensureStackIdSet(blueprintEntity);
        return entityManagerProvider.get().merge(blueprintEntity);
    }

    @com.google.inject.persist.Transactional
    public void remove(org.apache.ambari.server.orm.entities.BlueprintEntity blueprintEntity) {
        ensureStackIdSet(blueprintEntity);
        entityManagerProvider.get().remove(merge(blueprintEntity));
    }

    @com.google.inject.persist.Transactional
    public void removeByName(java.lang.String blueprint_name) {
        entityManagerProvider.get().remove(findByName(blueprint_name));
    }

    private void ensureStackIdSet(org.apache.ambari.server.orm.entities.BlueprintEntity entity) {
        org.apache.ambari.server.orm.entities.StackEntity stack = entity.getStack();
        if ((stack != null) && (stack.getStackId() == null)) {
            entity.setStack(stackDAO.find(stack.getStackName(), stack.getStackVersion()));
        }
    }
}