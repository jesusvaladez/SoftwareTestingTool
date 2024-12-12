package org.apache.ambari.server.orm.dao;
import com.google.inject.persist.Transactional;
import javax.persistence.EntityManager;
@com.google.inject.Singleton
public class ExecutionCommandDAO {
    @com.google.inject.Inject
    com.google.inject.Provider<javax.persistence.EntityManager> entityManagerProvider;

    @com.google.inject.Inject
    org.apache.ambari.server.orm.dao.DaoUtils daoUtils;

    @org.apache.ambari.server.orm.RequiresSession
    public org.apache.ambari.server.orm.entities.ExecutionCommandEntity findByPK(long taskId) {
        return entityManagerProvider.get().find(org.apache.ambari.server.orm.entities.ExecutionCommandEntity.class, taskId);
    }

    @org.apache.ambari.server.orm.RequiresSession
    public java.util.List<org.apache.ambari.server.orm.entities.ExecutionCommandEntity> findAll() {
        return daoUtils.selectAll(entityManagerProvider.get(), org.apache.ambari.server.orm.entities.ExecutionCommandEntity.class);
    }

    @com.google.inject.persist.Transactional
    public void create(org.apache.ambari.server.orm.entities.ExecutionCommandEntity executionCommand) {
        entityManagerProvider.get().persist(executionCommand);
    }

    @com.google.inject.persist.Transactional
    public org.apache.ambari.server.orm.entities.ExecutionCommandEntity merge(org.apache.ambari.server.orm.entities.ExecutionCommandEntity executionCommand) {
        return entityManagerProvider.get().merge(executionCommand);
    }

    @com.google.inject.persist.Transactional
    public void remove(org.apache.ambari.server.orm.entities.ExecutionCommandEntity executionCommand) {
        entityManagerProvider.get().remove(merge(executionCommand));
    }

    @com.google.inject.persist.Transactional
    public void removeByPK(long taskId) {
        remove(findByPK(taskId));
    }
}