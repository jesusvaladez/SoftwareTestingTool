package org.apache.ambari.server.orm.dao;
import com.google.inject.persist.Transactional;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
@com.google.inject.Singleton
public class MpackDAO {
    protected static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.orm.dao.MpackDAO.class);

    @com.google.inject.Inject
    com.google.inject.Provider<javax.persistence.EntityManager> m_entityManagerProvider;

    @com.google.inject.Inject
    private org.apache.ambari.server.orm.dao.DaoUtils m_daoUtils;

    @com.google.inject.persist.Transactional
    public java.lang.Long create(org.apache.ambari.server.orm.entities.MpackEntity mpackEntity) {
        m_entityManagerProvider.get().persist(mpackEntity);
        return mpackEntity.getId();
    }

    @org.apache.ambari.server.orm.RequiresSession
    public org.apache.ambari.server.orm.entities.MpackEntity findById(long id) {
        return m_entityManagerProvider.get().find(org.apache.ambari.server.orm.entities.MpackEntity.class, id);
    }

    @org.apache.ambari.server.orm.RequiresSession
    public java.util.List<org.apache.ambari.server.orm.entities.MpackEntity> findByNameVersion(java.lang.String mpackName, java.lang.String mpackVersion) {
        javax.persistence.TypedQuery<org.apache.ambari.server.orm.entities.MpackEntity> query = m_entityManagerProvider.get().createNamedQuery("MpackEntity.findByNameVersion", org.apache.ambari.server.orm.entities.MpackEntity.class);
        query.setParameter("mpackName", mpackName);
        query.setParameter("mpackVersion", mpackVersion);
        return m_daoUtils.selectList(query);
    }

    @org.apache.ambari.server.orm.RequiresSession
    public java.util.List<org.apache.ambari.server.orm.entities.MpackEntity> findAll() {
        javax.persistence.TypedQuery<org.apache.ambari.server.orm.entities.MpackEntity> query = m_entityManagerProvider.get().createNamedQuery("MpackEntity.findAll", org.apache.ambari.server.orm.entities.MpackEntity.class);
        return m_daoUtils.selectList(query);
    }

    @com.google.inject.persist.Transactional
    public void removeById(java.lang.Long id) {
        m_entityManagerProvider.get().remove(findById(id));
    }
}