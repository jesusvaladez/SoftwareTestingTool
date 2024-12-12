package org.apache.ambari.server.orm.dao;
import com.google.inject.persist.Transactional;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
@com.google.inject.Singleton
public class ArtifactDAO {
    @com.google.inject.Inject
    com.google.inject.Provider<javax.persistence.EntityManager> entityManagerProvider;

    @org.apache.ambari.server.orm.RequiresSession
    public org.apache.ambari.server.orm.entities.ArtifactEntity findByNameAndForeignKeys(java.lang.String artifactName, java.util.TreeMap<java.lang.String, java.lang.String> foreignKeys) {
        javax.persistence.TypedQuery<org.apache.ambari.server.orm.entities.ArtifactEntity> query = entityManagerProvider.get().createNamedQuery("artifactByNameAndForeignKeys", org.apache.ambari.server.orm.entities.ArtifactEntity.class);
        query.setParameter("artifactName", artifactName);
        query.setParameter("foreignKeys", org.apache.ambari.server.orm.entities.ArtifactEntity.serializeForeignKeys(foreignKeys));
        try {
            return query.getSingleResult();
        } catch (javax.persistence.NoResultException ignored) {
            return null;
        }
    }

    @org.apache.ambari.server.orm.RequiresSession
    public java.util.List<org.apache.ambari.server.orm.entities.ArtifactEntity> findByForeignKeys(java.util.TreeMap<java.lang.String, java.lang.String> foreignKeys) {
        javax.persistence.TypedQuery<org.apache.ambari.server.orm.entities.ArtifactEntity> query = entityManagerProvider.get().createNamedQuery("artifactByForeignKeys", org.apache.ambari.server.orm.entities.ArtifactEntity.class);
        query.setParameter("foreignKeys", org.apache.ambari.server.orm.entities.ArtifactEntity.serializeForeignKeys(foreignKeys));
        return query.getResultList();
    }

    @org.apache.ambari.server.orm.RequiresSession
    public java.util.List<org.apache.ambari.server.orm.entities.ArtifactEntity> findByName(java.lang.String artifactName) {
        javax.persistence.TypedQuery<org.apache.ambari.server.orm.entities.ArtifactEntity> query = entityManagerProvider.get().createNamedQuery("artifactByName", org.apache.ambari.server.orm.entities.ArtifactEntity.class);
        query.setParameter("artifactName", artifactName);
        return query.getResultList();
    }

    @com.google.inject.persist.Transactional
    public void refresh(org.apache.ambari.server.orm.entities.ArtifactEntity entity) {
        entityManagerProvider.get().refresh(entity);
    }

    @com.google.inject.persist.Transactional
    public void create(org.apache.ambari.server.orm.entities.ArtifactEntity entity) {
        entityManagerProvider.get().persist(entity);
    }

    @com.google.inject.persist.Transactional
    public org.apache.ambari.server.orm.entities.ArtifactEntity merge(org.apache.ambari.server.orm.entities.ArtifactEntity artifactEntity) {
        return entityManagerProvider.get().merge(artifactEntity);
    }

    @com.google.inject.persist.Transactional
    public void remove(org.apache.ambari.server.orm.entities.ArtifactEntity artifactEntity) {
        entityManagerProvider.get().remove(merge(artifactEntity));
    }
}