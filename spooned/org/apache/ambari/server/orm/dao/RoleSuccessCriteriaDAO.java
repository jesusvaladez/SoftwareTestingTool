package org.apache.ambari.server.orm.dao;
import com.google.inject.persist.Transactional;
import javax.persistence.EntityManager;
@com.google.inject.Singleton
public class RoleSuccessCriteriaDAO {
    @com.google.inject.Inject
    com.google.inject.Provider<javax.persistence.EntityManager> entityManagerProvider;

    @com.google.inject.Inject
    org.apache.ambari.server.orm.dao.DaoUtils daoUtils;

    @org.apache.ambari.server.orm.RequiresSession
    public org.apache.ambari.server.orm.entities.RoleSuccessCriteriaEntity findByPK(org.apache.ambari.server.orm.entities.RoleSuccessCriteriaEntityPK roleSuccessCriteriaEntityPK) {
        entityManagerProvider.get().clear();
        return entityManagerProvider.get().find(org.apache.ambari.server.orm.entities.RoleSuccessCriteriaEntity.class, roleSuccessCriteriaEntityPK);
    }

    @org.apache.ambari.server.orm.RequiresSession
    public java.util.List<org.apache.ambari.server.orm.entities.RoleSuccessCriteriaEntity> findAll() {
        return daoUtils.selectAll(entityManagerProvider.get(), org.apache.ambari.server.orm.entities.RoleSuccessCriteriaEntity.class);
    }

    @com.google.inject.persist.Transactional
    public void create(org.apache.ambari.server.orm.entities.RoleSuccessCriteriaEntity stageEntity) {
        entityManagerProvider.get().persist(stageEntity);
    }

    @com.google.inject.persist.Transactional
    public org.apache.ambari.server.orm.entities.RoleSuccessCriteriaEntity merge(org.apache.ambari.server.orm.entities.RoleSuccessCriteriaEntity stageEntity) {
        return entityManagerProvider.get().merge(stageEntity);
    }

    @com.google.inject.persist.Transactional
    public void remove(org.apache.ambari.server.orm.entities.RoleSuccessCriteriaEntity stageEntity) {
        entityManagerProvider.get().remove(merge(stageEntity));
    }

    @com.google.inject.persist.Transactional
    public void removeByPK(org.apache.ambari.server.orm.entities.RoleSuccessCriteriaEntityPK roleSuccessCriteriaEntityPK) {
        remove(findByPK(roleSuccessCriteriaEntityPK));
    }
}