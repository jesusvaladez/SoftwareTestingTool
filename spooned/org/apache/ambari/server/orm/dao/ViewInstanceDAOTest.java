package org.apache.ambari.server.orm.dao;
import javax.persistence.EntityManager;
import static org.easymock.EasyMock.createStrictMock;
import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.reset;
import static org.easymock.EasyMock.verify;
public class ViewInstanceDAOTest {
    com.google.inject.Provider<javax.persistence.EntityManager> entityManagerProvider = EasyMock.createStrictMock(com.google.inject.Provider.class);

    javax.persistence.EntityManager entityManager = EasyMock.createStrictMock(javax.persistence.EntityManager.class);

    @org.junit.Before
    public void init() {
        EasyMock.reset(entityManagerProvider);
        EasyMock.expect(entityManagerProvider.get()).andReturn(entityManager).atLeastOnce();
        EasyMock.replay(entityManagerProvider);
    }

    @org.junit.Test
    public void testMergeData() throws java.lang.Exception {
        org.apache.ambari.server.orm.entities.ViewInstanceDataEntity entity = new org.apache.ambari.server.orm.entities.ViewInstanceDataEntity();
        org.apache.ambari.server.orm.entities.ViewInstanceDataEntity entity2 = new org.apache.ambari.server.orm.entities.ViewInstanceDataEntity();
        EasyMock.expect(entityManager.merge(EasyMock.eq(entity))).andReturn(entity2);
        EasyMock.replay(entityManager);
        org.apache.ambari.server.orm.dao.ViewInstanceDAO dao = new org.apache.ambari.server.orm.dao.ViewInstanceDAO();
        dao.entityManagerProvider = entityManagerProvider;
        org.junit.Assert.assertSame(entity2, dao.mergeData(entity));
        EasyMock.verify(entityManagerProvider, entityManager);
    }

    @org.junit.Test
    public void testRemoveData() throws java.lang.Exception {
        org.apache.ambari.server.orm.entities.ViewInstanceDataEntity entity = new org.apache.ambari.server.orm.entities.ViewInstanceDataEntity();
        org.apache.ambari.server.orm.entities.ViewInstanceDataEntity entity2 = new org.apache.ambari.server.orm.entities.ViewInstanceDataEntity();
        EasyMock.expect(entityManager.merge(EasyMock.eq(entity))).andReturn(entity2);
        entityManager.remove(EasyMock.eq(entity2));
        EasyMock.replay(entityManager);
        org.apache.ambari.server.orm.dao.ViewInstanceDAO dao = new org.apache.ambari.server.orm.dao.ViewInstanceDAO();
        dao.entityManagerProvider = entityManagerProvider;
        dao.removeData(entity);
        EasyMock.verify(entityManagerProvider, entityManager);
    }
}