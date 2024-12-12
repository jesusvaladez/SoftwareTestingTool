package org.apache.ambari.server.orm.dao;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import static org.easymock.EasyMock.createStrictMock;
import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.reset;
import static org.easymock.EasyMock.verify;
public class GroupDAOTest {
    @com.google.inject.Inject
    org.apache.ambari.server.orm.dao.DaoUtils daoUtils;

    com.google.inject.Provider<javax.persistence.EntityManager> entityManagerProvider = EasyMock.createStrictMock(com.google.inject.Provider.class);

    javax.persistence.EntityManager entityManager = EasyMock.createStrictMock(javax.persistence.EntityManager.class);

    @org.junit.Before
    public void init() {
        EasyMock.reset(entityManagerProvider);
        EasyMock.expect(entityManagerProvider.get()).andReturn(entityManager).atLeastOnce();
        EasyMock.replay(entityManagerProvider);
    }

    @org.junit.Test
    public void testfindGroupByName() {
        final java.lang.String groupName = "engineering";
        final org.apache.ambari.server.orm.entities.GroupEntity entity = new org.apache.ambari.server.orm.entities.GroupEntity();
        entity.setGroupName(groupName);
        javax.persistence.TypedQuery<org.apache.ambari.server.orm.entities.GroupEntity> query = EasyMock.createStrictMock(javax.persistence.TypedQuery.class);
        EasyMock.expect(entityManager.createNamedQuery(EasyMock.eq("groupByName"), EasyMock.eq(org.apache.ambari.server.orm.entities.GroupEntity.class))).andReturn(query);
        EasyMock.expect(query.setParameter("groupname", groupName)).andReturn(query);
        EasyMock.expect(query.getSingleResult()).andReturn(entity);
        EasyMock.replay(entityManager, query);
        final org.apache.ambari.server.orm.dao.GroupDAO dao = new org.apache.ambari.server.orm.dao.GroupDAO();
        dao.entityManagerProvider = entityManagerProvider;
        final org.apache.ambari.server.orm.entities.GroupEntity result = dao.findGroupByName(groupName);
        org.junit.Assert.assertSame(entity, result);
        EasyMock.verify(entityManagerProvider, entityManager, query);
    }
}