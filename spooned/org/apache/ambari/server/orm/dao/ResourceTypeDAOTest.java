package org.apache.ambari.server.orm.dao;
import javax.persistence.EntityManager;
import static org.easymock.EasyMock.createStrictMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.reset;
public class ResourceTypeDAOTest {
    com.google.inject.Provider<javax.persistence.EntityManager> entityManagerProvider = EasyMock.createStrictMock(com.google.inject.Provider.class);

    javax.persistence.EntityManager entityManager = EasyMock.createStrictMock(javax.persistence.EntityManager.class);

    @org.junit.Before
    public void init() {
        EasyMock.reset(entityManagerProvider);
        EasyMock.expect(entityManagerProvider.get()).andReturn(entityManager).atLeastOnce();
        EasyMock.replay(entityManagerProvider);
    }

    @org.junit.Test
    public void testFindById() throws java.lang.Exception {
        org.apache.ambari.server.orm.entities.ResourceTypeEntity entity = new org.apache.ambari.server.orm.entities.ResourceTypeEntity();
        EasyMock.expect(entityManager.find(org.apache.ambari.server.orm.entities.ResourceTypeEntity.class, 99)).andReturn(entity);
        EasyMock.replay(entityManager);
        org.apache.ambari.server.orm.dao.ResourceTypeDAO dao = new org.apache.ambari.server.orm.dao.ResourceTypeDAO();
        dao.entityManagerProvider = entityManagerProvider;
        org.junit.Assert.assertEquals(entity, dao.findById(99));
    }
}