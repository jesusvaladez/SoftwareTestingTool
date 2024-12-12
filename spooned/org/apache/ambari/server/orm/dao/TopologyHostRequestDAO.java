package org.apache.ambari.server.orm.dao;
import com.google.inject.persist.Transactional;
import javax.persistence.EntityManager;
@com.google.inject.Singleton
public class TopologyHostRequestDAO {
    @com.google.inject.Inject
    com.google.inject.Provider<javax.persistence.EntityManager> entityManagerProvider;

    @com.google.inject.Inject
    org.apache.ambari.server.orm.dao.DaoUtils daoUtils;

    @org.apache.ambari.server.orm.RequiresSession
    public org.apache.ambari.server.orm.entities.TopologyHostRequestEntity findById(java.lang.Long id) {
        return entityManagerProvider.get().find(org.apache.ambari.server.orm.entities.TopologyHostRequestEntity.class, id);
    }

    @org.apache.ambari.server.orm.RequiresSession
    public java.util.List<org.apache.ambari.server.orm.entities.TopologyHostRequestEntity> findAll() {
        return daoUtils.selectAll(entityManagerProvider.get(), org.apache.ambari.server.orm.entities.TopologyHostRequestEntity.class);
    }

    @com.google.inject.persist.Transactional
    public void create(org.apache.ambari.server.orm.entities.TopologyHostRequestEntity requestEntity) {
        entityManagerProvider.get().persist(requestEntity);
    }

    @com.google.inject.persist.Transactional
    public org.apache.ambari.server.orm.entities.TopologyHostRequestEntity merge(org.apache.ambari.server.orm.entities.TopologyHostRequestEntity requestEntity) {
        return entityManagerProvider.get().merge(requestEntity);
    }

    @com.google.inject.persist.Transactional
    public void remove(org.apache.ambari.server.orm.entities.TopologyHostRequestEntity requestEntity) {
        entityManagerProvider.get().remove(requestEntity);
    }
}