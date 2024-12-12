package org.apache.ambari.server.orm.dao;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import static org.easymock.EasyMock.createStrictMock;
import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.reset;
import static org.easymock.EasyMock.verify;
public class BlueprintDAOTest {
    com.google.inject.Provider<javax.persistence.EntityManager> entityManagerProvider = EasyMock.createStrictMock(com.google.inject.Provider.class);

    javax.persistence.EntityManager entityManager = EasyMock.createStrictMock(javax.persistence.EntityManager.class);

    @org.junit.Before
    public void init() {
        EasyMock.reset(entityManagerProvider);
        EasyMock.expect(entityManagerProvider.get()).andReturn(entityManager).atLeastOnce();
        EasyMock.replay(entityManagerProvider);
    }

    @org.junit.Test
    public void testFindByName() {
        org.apache.ambari.server.orm.entities.BlueprintEntity entity = new org.apache.ambari.server.orm.entities.BlueprintEntity();
        EasyMock.expect(entityManager.find(EasyMock.eq(org.apache.ambari.server.orm.entities.BlueprintEntity.class), EasyMock.eq("test-cluster-name"))).andReturn(entity);
        EasyMock.replay(entityManager);
        org.apache.ambari.server.orm.dao.BlueprintDAO dao = new org.apache.ambari.server.orm.dao.BlueprintDAO();
        dao.entityManagerProvider = entityManagerProvider;
        org.apache.ambari.server.orm.entities.BlueprintEntity result = dao.findByName("test-cluster-name");
        org.junit.Assert.assertSame(result, entity);
        EasyMock.verify(entityManagerProvider, entityManager);
    }

    @org.junit.Test
    public void testFindAll() {
        org.apache.ambari.server.orm.entities.BlueprintEntity entity = new org.apache.ambari.server.orm.entities.BlueprintEntity();
        javax.persistence.TypedQuery<org.apache.ambari.server.orm.entities.BlueprintEntity> query = EasyMock.createStrictMock(javax.persistence.TypedQuery.class);
        EasyMock.expect(entityManager.createNamedQuery(EasyMock.eq("allBlueprints"), EasyMock.eq(org.apache.ambari.server.orm.entities.BlueprintEntity.class))).andReturn(query);
        EasyMock.expect(query.getResultList()).andReturn(java.util.Collections.singletonList(entity));
        EasyMock.replay(entityManager, query);
        org.apache.ambari.server.orm.dao.BlueprintDAO dao = new org.apache.ambari.server.orm.dao.BlueprintDAO();
        dao.entityManagerProvider = entityManagerProvider;
        java.util.List<org.apache.ambari.server.orm.entities.BlueprintEntity> results = dao.findAll();
        org.junit.Assert.assertEquals(1, results.size());
        org.junit.Assert.assertSame(entity, results.get(0));
        EasyMock.verify(entityManagerProvider, entityManager, query);
    }

    @org.junit.Test
    public void testRefresh() {
        org.apache.ambari.server.orm.entities.BlueprintEntity entity = new org.apache.ambari.server.orm.entities.BlueprintEntity();
        entityManager.refresh(EasyMock.eq(entity));
        EasyMock.replay(entityManager);
        org.apache.ambari.server.orm.dao.BlueprintDAO dao = new org.apache.ambari.server.orm.dao.BlueprintDAO();
        dao.entityManagerProvider = entityManagerProvider;
        dao.refresh(entity);
        EasyMock.verify(entityManagerProvider, entityManager);
    }

    @org.junit.Test
    public void testCreate() {
        org.apache.ambari.server.orm.entities.BlueprintEntity entity = new org.apache.ambari.server.orm.entities.BlueprintEntity();
        entityManager.persist(EasyMock.eq(entity));
        EasyMock.replay(entityManager);
        org.apache.ambari.server.orm.dao.BlueprintDAO dao = new org.apache.ambari.server.orm.dao.BlueprintDAO();
        dao.entityManagerProvider = entityManagerProvider;
        dao.create(entity);
        EasyMock.verify(entityManagerProvider, entityManager);
    }

    @org.junit.Test
    public void testMerge() {
        org.apache.ambari.server.orm.entities.BlueprintEntity entity = new org.apache.ambari.server.orm.entities.BlueprintEntity();
        org.apache.ambari.server.orm.entities.BlueprintEntity entity2 = new org.apache.ambari.server.orm.entities.BlueprintEntity();
        EasyMock.expect(entityManager.merge(EasyMock.eq(entity))).andReturn(entity2);
        EasyMock.replay(entityManager);
        org.apache.ambari.server.orm.dao.BlueprintDAO dao = new org.apache.ambari.server.orm.dao.BlueprintDAO();
        dao.entityManagerProvider = entityManagerProvider;
        org.junit.Assert.assertSame(entity2, dao.merge(entity));
        EasyMock.verify(entityManagerProvider, entityManager);
    }

    @org.junit.Test
    public void testRemove() {
        org.apache.ambari.server.orm.entities.BlueprintEntity entity = new org.apache.ambari.server.orm.entities.BlueprintEntity();
        org.apache.ambari.server.orm.entities.BlueprintEntity entity2 = new org.apache.ambari.server.orm.entities.BlueprintEntity();
        EasyMock.expect(entityManager.merge(EasyMock.eq(entity))).andReturn(entity2);
        entityManager.remove(EasyMock.eq(entity2));
        EasyMock.replay(entityManager);
        org.apache.ambari.server.orm.dao.BlueprintDAO dao = new org.apache.ambari.server.orm.dao.BlueprintDAO();
        dao.entityManagerProvider = entityManagerProvider;
        dao.remove(entity);
        EasyMock.verify(entityManagerProvider, entityManager);
    }

    @org.junit.Test
    public void testRemoveByName() {
        org.apache.ambari.server.orm.entities.BlueprintEntity entity = new org.apache.ambari.server.orm.entities.BlueprintEntity();
        org.apache.ambari.server.orm.dao.BlueprintDAO dao = new org.apache.ambari.server.orm.dao.BlueprintDAO();
        dao.entityManagerProvider = entityManagerProvider;
        EasyMock.expect(entityManager.find(EasyMock.eq(org.apache.ambari.server.orm.entities.BlueprintEntity.class), EasyMock.eq("test"))).andReturn(entity);
        entityManager.remove(entity);
        EasyMock.expectLastCall();
        EasyMock.replay(entityManager);
        dao.removeByName("test");
        EasyMock.verify(entityManager);
    }
}