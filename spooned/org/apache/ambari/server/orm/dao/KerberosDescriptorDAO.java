package org.apache.ambari.server.orm.dao;
import com.google.inject.persist.Transactional;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
@javax.inject.Singleton
public class KerberosDescriptorDAO {
    @javax.inject.Inject
    private javax.inject.Provider<javax.persistence.EntityManager> entityManagerProvider;

    @org.apache.ambari.server.orm.RequiresSession
    public org.apache.ambari.server.orm.entities.KerberosDescriptorEntity findByName(java.lang.String kerberosDescriptorName) {
        return entityManagerProvider.get().find(org.apache.ambari.server.orm.entities.KerberosDescriptorEntity.class, kerberosDescriptorName);
    }

    @org.apache.ambari.server.orm.RequiresSession
    public java.util.List<org.apache.ambari.server.orm.entities.KerberosDescriptorEntity> findAll() {
        javax.persistence.TypedQuery<org.apache.ambari.server.orm.entities.KerberosDescriptorEntity> query = entityManagerProvider.get().createNamedQuery("allKerberosDescriptors", org.apache.ambari.server.orm.entities.KerberosDescriptorEntity.class);
        return query.getResultList();
    }

    @com.google.inject.persist.Transactional
    public void create(org.apache.ambari.server.orm.entities.KerberosDescriptorEntity kerberosDescriptorEntity) {
        entityManagerProvider.get().persist(kerberosDescriptorEntity);
    }

    @com.google.inject.persist.Transactional
    public void refresh(org.apache.ambari.server.orm.entities.KerberosDescriptorEntity kerberosDescriptorEntity) {
        entityManagerProvider.get().refresh(kerberosDescriptorEntity);
    }

    @com.google.inject.persist.Transactional
    public org.apache.ambari.server.orm.entities.KerberosDescriptorEntity merge(org.apache.ambari.server.orm.entities.KerberosDescriptorEntity kerberosDescriptorEntity) {
        return entityManagerProvider.get().merge(kerberosDescriptorEntity);
    }

    @com.google.inject.persist.Transactional
    public void remove(org.apache.ambari.server.orm.entities.KerberosDescriptorEntity kerberosDescriptorEntity) {
        entityManagerProvider.get().remove(merge(kerberosDescriptorEntity));
    }

    @com.google.inject.persist.Transactional
    public void removeByName(java.lang.String name) {
        entityManagerProvider.get().remove(findByName(name));
    }
}