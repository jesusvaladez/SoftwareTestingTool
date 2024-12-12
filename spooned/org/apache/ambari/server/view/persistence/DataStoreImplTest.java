package org.apache.ambari.server.view.persistence;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;
import org.easymock.Capture;
import org.easymock.EasyMock;
import org.easymock.IAnswer;
import org.eclipse.persistence.dynamic.DynamicClassLoader;
import org.eclipse.persistence.dynamic.DynamicEntity;
import org.eclipse.persistence.dynamic.DynamicType;
import org.eclipse.persistence.jpa.JpaEntityManager;
import org.eclipse.persistence.jpa.JpaHelper;
import org.eclipse.persistence.jpa.dynamic.JPADynamicHelper;
import org.eclipse.persistence.sequencing.Sequence;
import org.eclipse.persistence.sessions.DatabaseLogin;
import org.eclipse.persistence.sessions.DatabaseSession;
import org.eclipse.persistence.sessions.server.ServerSession;
import org.eclipse.persistence.tools.schemaframework.SchemaManager;
import org.powermock.api.easymock.PowerMock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import static org.easymock.EasyMock.capture;
import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.createNiceMock;
import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
@org.junit.runner.RunWith(org.powermock.modules.junit4.PowerMockRunner.class)
@org.powermock.core.classloader.annotations.PrepareForTest(org.eclipse.persistence.jpa.JpaHelper.class)
public class DataStoreImplTest {
    private static final java.lang.String xml = "<view>\n" + (((((((((((((((("    <name>MY_VIEW</name>\n" + "    <label>My View!</label>\n") + "    <version>1.0.0</version>\n") + "    <instance>\n") + "        <name>INSTANCE1</name>\n") + "    </instance>\n") + "    <persistence>\n") + "      <entity>\n") + "        <class>org.apache.ambari.server.view.persistence.DataStoreImplTest$TestEntity</class>\n") + "        <id-property>id</id-property>\n") + "      </entity>\n") + "      <entity>\n") + "        <class>org.apache.ambari.server.view.persistence.DataStoreImplTest$TestSubEntity</class>\n") + "        <id-property>id</id-property>\n") + "      </entity>\n") + "    </persistence>") + "</view>");

    @org.junit.Test
    public void testStore_create() throws java.lang.Exception {
        org.eclipse.persistence.dynamic.DynamicClassLoader classLoader = new org.eclipse.persistence.dynamic.DynamicClassLoader(org.apache.ambari.server.view.persistence.DataStoreImplTest.class.getClassLoader());
        javax.persistence.EntityManagerFactory entityManagerFactory = EasyMock.createMock(javax.persistence.EntityManagerFactory.class);
        org.eclipse.persistence.jpa.JpaEntityManager jpaEntityManager = EasyMock.createMock(org.eclipse.persistence.jpa.JpaEntityManager.class);
        org.eclipse.persistence.sessions.server.ServerSession session = EasyMock.createMock(org.eclipse.persistence.sessions.server.ServerSession.class);
        org.eclipse.persistence.sessions.DatabaseLogin databaseLogin = EasyMock.createMock(org.eclipse.persistence.sessions.DatabaseLogin.class);
        javax.persistence.EntityManager entityManager = EasyMock.createMock(javax.persistence.EntityManager.class);
        org.eclipse.persistence.jpa.dynamic.JPADynamicHelper jpaDynamicHelper = EasyMock.createNiceMock(org.eclipse.persistence.jpa.dynamic.JPADynamicHelper.class);
        org.eclipse.persistence.tools.schemaframework.SchemaManager schemaManager = EasyMock.createNiceMock(org.eclipse.persistence.tools.schemaframework.SchemaManager.class);
        javax.persistence.EntityTransaction transaction = EasyMock.createMock(javax.persistence.EntityTransaction.class);
        org.powermock.api.easymock.PowerMock.mockStatic(org.eclipse.persistence.jpa.JpaHelper.class);
        EasyMock.expect(org.eclipse.persistence.jpa.JpaHelper.getEntityManager(entityManager)).andReturn(jpaEntityManager).anyTimes();
        org.powermock.api.easymock.PowerMock.replay(org.eclipse.persistence.jpa.JpaHelper.class);
        EasyMock.expect(jpaEntityManager.getServerSession()).andReturn(session).anyTimes();
        EasyMock.expect(session.getLogin()).andReturn(databaseLogin).anyTimes();
        org.easymock.Capture<org.eclipse.persistence.sequencing.Sequence> sequenceCapture = org.easymock.EasyMock.newCapture();
        databaseLogin.addSequence(EasyMock.capture(sequenceCapture));
        org.easymock.EasyMock.expectLastCall().anyTimes();
        org.easymock.Capture<org.eclipse.persistence.dynamic.DynamicEntity> entityCapture = org.easymock.EasyMock.newCapture();
        entityManager.persist(EasyMock.capture(entityCapture));
        org.easymock.EasyMock.expectLastCall().andAnswer(new org.easymock.IAnswer<java.lang.Object>() {
            @java.lang.Override
            public java.lang.Object answer() throws java.lang.Throwable {
                ((org.eclipse.persistence.dynamic.DynamicEntity) (org.easymock.EasyMock.getCurrentArguments()[0])).set("DS_id", 99);
                return null;
            }
        });
        org.easymock.Capture<org.eclipse.persistence.dynamic.DynamicEntity> entityCapture2 = org.easymock.EasyMock.newCapture();
        entityManager.persist(EasyMock.capture(entityCapture2));
        org.easymock.EasyMock.expectLastCall().andAnswer(new org.easymock.IAnswer<java.lang.Object>() {
            @java.lang.Override
            public java.lang.Object answer() throws java.lang.Throwable {
                ((org.eclipse.persistence.dynamic.DynamicEntity) (org.easymock.EasyMock.getCurrentArguments()[0])).set("DS_id", 100);
                return null;
            }
        });
        org.easymock.Capture<org.eclipse.persistence.dynamic.DynamicType> typeCapture = org.easymock.EasyMock.newCapture();
        org.easymock.Capture<org.eclipse.persistence.dynamic.DynamicType> typeCapture2 = org.easymock.EasyMock.newCapture();
        jpaDynamicHelper.addTypes(EasyMock.eq(true), EasyMock.eq(true), EasyMock.capture(typeCapture), EasyMock.capture(typeCapture2));
        EasyMock.expect(entityManagerFactory.createEntityManager()).andReturn(entityManager).anyTimes();
        EasyMock.expect(entityManager.getTransaction()).andReturn(transaction).anyTimes();
        entityManager.close();
        transaction.begin();
        transaction.commit();
        EasyMock.replay(entityManagerFactory, entityManager, jpaDynamicHelper, transaction, schemaManager, jpaEntityManager, session, databaseLogin);
        org.apache.ambari.server.view.persistence.DataStoreImpl dataStore = getDataStore(entityManagerFactory, jpaDynamicHelper, classLoader, schemaManager);
        dataStore.store(new org.apache.ambari.server.view.persistence.DataStoreImplTest.TestEntity("foo", new org.apache.ambari.server.view.persistence.DataStoreImplTest.TestSubEntity("bar")));
        org.junit.Assert.assertEquals("bar", entityCapture.getValue().get("DS_name"));
        org.junit.Assert.assertEquals(new java.lang.Integer(99), entityCapture.getValue().get("DS_id"));
        org.junit.Assert.assertEquals(new java.lang.Integer(100), entityCapture2.getValue().get("DS_id"));
        org.junit.Assert.assertEquals("foo", entityCapture2.getValue().get("DS_name"));
        EasyMock.verify(entityManagerFactory, entityManager, jpaDynamicHelper, transaction, schemaManager, jpaEntityManager, session, databaseLogin);
    }

    @org.junit.Test
    public void testStore_create_longStringValue() throws java.lang.Exception {
        org.eclipse.persistence.dynamic.DynamicClassLoader classLoader = new org.eclipse.persistence.dynamic.DynamicClassLoader(org.apache.ambari.server.view.persistence.DataStoreImplTest.class.getClassLoader());
        org.eclipse.persistence.jpa.JpaEntityManager jpaEntityManager = EasyMock.createMock(org.eclipse.persistence.jpa.JpaEntityManager.class);
        org.eclipse.persistence.sessions.server.ServerSession session = EasyMock.createMock(org.eclipse.persistence.sessions.server.ServerSession.class);
        org.eclipse.persistence.sessions.DatabaseLogin databaseLogin = EasyMock.createMock(org.eclipse.persistence.sessions.DatabaseLogin.class);
        javax.persistence.EntityManagerFactory entityManagerFactory = EasyMock.createMock(javax.persistence.EntityManagerFactory.class);
        javax.persistence.EntityManager entityManager = EasyMock.createMock(javax.persistence.EntityManager.class);
        org.eclipse.persistence.jpa.dynamic.JPADynamicHelper jpaDynamicHelper = EasyMock.createNiceMock(org.eclipse.persistence.jpa.dynamic.JPADynamicHelper.class);
        org.eclipse.persistence.tools.schemaframework.SchemaManager schemaManager = EasyMock.createNiceMock(org.eclipse.persistence.tools.schemaframework.SchemaManager.class);
        javax.persistence.EntityTransaction transaction = EasyMock.createMock(javax.persistence.EntityTransaction.class);
        org.powermock.api.easymock.PowerMock.mockStatic(org.eclipse.persistence.jpa.JpaHelper.class);
        EasyMock.expect(org.eclipse.persistence.jpa.JpaHelper.getEntityManager(entityManager)).andReturn(jpaEntityManager).anyTimes();
        org.powermock.api.easymock.PowerMock.replay(org.eclipse.persistence.jpa.JpaHelper.class);
        EasyMock.expect(jpaEntityManager.getServerSession()).andReturn(session).anyTimes();
        EasyMock.expect(session.getLogin()).andReturn(databaseLogin).anyTimes();
        org.easymock.Capture<org.eclipse.persistence.sequencing.Sequence> sequenceCapture = org.easymock.EasyMock.newCapture();
        databaseLogin.addSequence(EasyMock.capture(sequenceCapture));
        org.easymock.EasyMock.expectLastCall().anyTimes();
        org.easymock.Capture<org.eclipse.persistence.dynamic.DynamicType> typeCapture = org.easymock.EasyMock.newCapture();
        org.easymock.Capture<org.eclipse.persistence.dynamic.DynamicType> typeCapture2 = org.easymock.EasyMock.newCapture();
        jpaDynamicHelper.addTypes(EasyMock.eq(true), EasyMock.eq(true), EasyMock.capture(typeCapture), EasyMock.capture(typeCapture2));
        EasyMock.expect(entityManagerFactory.createEntityManager()).andReturn(entityManager).anyTimes();
        EasyMock.expect(entityManager.getTransaction()).andReturn(transaction).anyTimes();
        entityManager.close();
        transaction.begin();
        EasyMock.expect(transaction.isActive()).andReturn(true);
        transaction.rollback();
        EasyMock.replay(entityManagerFactory, entityManager, jpaDynamicHelper, transaction, schemaManager, jpaEntityManager, session, databaseLogin);
        org.apache.ambari.server.view.persistence.DataStoreImpl dataStore = getDataStore(entityManagerFactory, jpaDynamicHelper, classLoader, schemaManager);
        java.lang.StringBuilder sb = new java.lang.StringBuilder();
        for (int i = 0; i < 5000; ++i) {
            sb.append("A");
        }
        java.lang.String longString = sb.toString();
        try {
            dataStore.store(new org.apache.ambari.server.view.persistence.DataStoreImplTest.TestEntity(longString, new org.apache.ambari.server.view.persistence.DataStoreImplTest.TestSubEntity("bar")));
            org.junit.Assert.fail("Expected PersistenceException.");
        } catch (org.apache.ambari.view.PersistenceException e) {
        }
        EasyMock.verify(entityManagerFactory, entityManager, jpaDynamicHelper, transaction, schemaManager, jpaEntityManager, session, databaseLogin);
    }

    @org.junit.Test
    public void testStore_create_largeEntity() throws java.lang.Exception {
        org.eclipse.persistence.dynamic.DynamicClassLoader classLoader = new org.eclipse.persistence.dynamic.DynamicClassLoader(org.apache.ambari.server.view.persistence.DataStoreImplTest.class.getClassLoader());
        org.eclipse.persistence.jpa.JpaEntityManager jpaEntityManager = EasyMock.createMock(org.eclipse.persistence.jpa.JpaEntityManager.class);
        org.eclipse.persistence.sessions.server.ServerSession session = EasyMock.createMock(org.eclipse.persistence.sessions.server.ServerSession.class);
        org.eclipse.persistence.sessions.DatabaseLogin databaseLogin = EasyMock.createMock(org.eclipse.persistence.sessions.DatabaseLogin.class);
        javax.persistence.EntityManagerFactory entityManagerFactory = EasyMock.createMock(javax.persistence.EntityManagerFactory.class);
        javax.persistence.EntityManager entityManager = EasyMock.createMock(javax.persistence.EntityManager.class);
        org.eclipse.persistence.jpa.dynamic.JPADynamicHelper jpaDynamicHelper = EasyMock.createNiceMock(org.eclipse.persistence.jpa.dynamic.JPADynamicHelper.class);
        org.eclipse.persistence.tools.schemaframework.SchemaManager schemaManager = EasyMock.createNiceMock(org.eclipse.persistence.tools.schemaframework.SchemaManager.class);
        javax.persistence.EntityTransaction transaction = EasyMock.createMock(javax.persistence.EntityTransaction.class);
        org.powermock.api.easymock.PowerMock.mockStatic(org.eclipse.persistence.jpa.JpaHelper.class);
        EasyMock.expect(org.eclipse.persistence.jpa.JpaHelper.getEntityManager(entityManager)).andReturn(jpaEntityManager).anyTimes();
        org.powermock.api.easymock.PowerMock.replay(org.eclipse.persistence.jpa.JpaHelper.class);
        EasyMock.expect(jpaEntityManager.getServerSession()).andReturn(session).anyTimes();
        EasyMock.expect(session.getLogin()).andReturn(databaseLogin).anyTimes();
        org.easymock.Capture<org.eclipse.persistence.sequencing.Sequence> sequenceCapture = org.easymock.EasyMock.newCapture();
        databaseLogin.addSequence(EasyMock.capture(sequenceCapture));
        org.easymock.EasyMock.expectLastCall().anyTimes();
        org.easymock.Capture<org.eclipse.persistence.dynamic.DynamicType> typeCapture = org.easymock.EasyMock.newCapture();
        org.easymock.Capture<org.eclipse.persistence.dynamic.DynamicType> typeCapture2 = org.easymock.EasyMock.newCapture();
        jpaDynamicHelper.addTypes(EasyMock.eq(true), EasyMock.eq(true), EasyMock.capture(typeCapture), EasyMock.capture(typeCapture2));
        EasyMock.expect(entityManagerFactory.createEntityManager()).andReturn(entityManager).anyTimes();
        EasyMock.expect(entityManager.getTransaction()).andReturn(transaction).anyTimes();
        entityManager.close();
        transaction.begin();
        EasyMock.expect(transaction.isActive()).andReturn(true);
        transaction.rollback();
        EasyMock.replay(entityManagerFactory, entityManager, jpaDynamicHelper, transaction, schemaManager, jpaEntityManager, session, databaseLogin);
        org.apache.ambari.server.view.persistence.DataStoreImpl dataStore = getDataStore(entityManagerFactory, jpaDynamicHelper, classLoader, schemaManager);
        try {
            dataStore.store(new org.apache.ambari.server.view.persistence.DataStoreImplTest.TestLargeEntity());
            org.junit.Assert.fail("Expected PersistenceException.");
        } catch (org.apache.ambari.view.PersistenceException e) {
        }
        EasyMock.verify(entityManagerFactory, entityManager, jpaDynamicHelper, transaction, schemaManager, jpaEntityManager, session, databaseLogin);
    }

    @org.junit.Test
    public void testStore_update() throws java.lang.Exception {
        org.eclipse.persistence.dynamic.DynamicClassLoader classLoader = new org.eclipse.persistence.dynamic.DynamicClassLoader(org.apache.ambari.server.view.persistence.DataStoreImplTest.class.getClassLoader());
        org.eclipse.persistence.jpa.JpaEntityManager jpaEntityManager = EasyMock.createMock(org.eclipse.persistence.jpa.JpaEntityManager.class);
        org.eclipse.persistence.sessions.server.ServerSession session = EasyMock.createMock(org.eclipse.persistence.sessions.server.ServerSession.class);
        org.eclipse.persistence.sessions.DatabaseLogin databaseLogin = EasyMock.createMock(org.eclipse.persistence.sessions.DatabaseLogin.class);
        javax.persistence.EntityManagerFactory entityManagerFactory = EasyMock.createMock(javax.persistence.EntityManagerFactory.class);
        javax.persistence.EntityManager entityManager = EasyMock.createMock(javax.persistence.EntityManager.class);
        org.eclipse.persistence.jpa.dynamic.JPADynamicHelper jpaDynamicHelper = EasyMock.createNiceMock(org.eclipse.persistence.jpa.dynamic.JPADynamicHelper.class);
        org.eclipse.persistence.tools.schemaframework.SchemaManager schemaManager = EasyMock.createNiceMock(org.eclipse.persistence.tools.schemaframework.SchemaManager.class);
        javax.persistence.EntityTransaction transaction = EasyMock.createMock(javax.persistence.EntityTransaction.class);
        org.eclipse.persistence.dynamic.DynamicEntity dynamicEntity = EasyMock.createMock(org.eclipse.persistence.dynamic.DynamicEntity.class);
        org.eclipse.persistence.dynamic.DynamicEntity dynamicSubEntity = EasyMock.createMock(org.eclipse.persistence.dynamic.DynamicEntity.class);
        org.powermock.api.easymock.PowerMock.mockStatic(org.eclipse.persistence.jpa.JpaHelper.class);
        EasyMock.expect(org.eclipse.persistence.jpa.JpaHelper.getEntityManager(entityManager)).andReturn(jpaEntityManager).anyTimes();
        org.powermock.api.easymock.PowerMock.replay(org.eclipse.persistence.jpa.JpaHelper.class);
        EasyMock.expect(jpaEntityManager.getServerSession()).andReturn(session).anyTimes();
        EasyMock.expect(session.getLogin()).andReturn(databaseLogin).anyTimes();
        org.easymock.Capture<org.eclipse.persistence.sequencing.Sequence> sequenceCapture = org.easymock.EasyMock.newCapture();
        databaseLogin.addSequence(EasyMock.capture(sequenceCapture));
        org.easymock.EasyMock.expectLastCall().anyTimes();
        org.easymock.Capture<org.eclipse.persistence.dynamic.DynamicType> typeCapture = org.easymock.EasyMock.newCapture();
        org.easymock.Capture<org.eclipse.persistence.dynamic.DynamicType> typeCapture2 = org.easymock.EasyMock.newCapture();
        jpaDynamicHelper.addTypes(EasyMock.eq(true), EasyMock.eq(true), EasyMock.capture(typeCapture), EasyMock.capture(typeCapture2));
        EasyMock.expect(entityManagerFactory.createEntityManager()).andReturn(entityManager).anyTimes();
        EasyMock.expect(entityManager.getTransaction()).andReturn(transaction).anyTimes();
        org.easymock.Capture<java.lang.Class> entityClassCapture = org.easymock.EasyMock.newCapture();
        EasyMock.expect(entityManager.find(EasyMock.capture(entityClassCapture), EasyMock.eq(100))).andReturn(dynamicEntity);
        org.easymock.Capture<java.lang.Class> entityClassCapture2 = org.easymock.EasyMock.newCapture();
        EasyMock.expect(entityManager.find(EasyMock.capture(entityClassCapture2), EasyMock.eq(99))).andReturn(dynamicSubEntity);
        entityManager.close();
        EasyMock.expect(dynamicEntity.set("DS_id", 100)).andReturn(dynamicEntity);
        EasyMock.expect(dynamicEntity.set("DS_name", "foo")).andReturn(dynamicEntity);
        EasyMock.expect(dynamicSubEntity.set("DS_id", 99)).andReturn(dynamicSubEntity);
        EasyMock.expect(dynamicSubEntity.set("DS_name", "bar")).andReturn(dynamicSubEntity);
        org.easymock.Capture<org.eclipse.persistence.dynamic.DynamicEntity> subEntityCapture = org.easymock.EasyMock.newCapture();
        EasyMock.expect(dynamicEntity.set(EasyMock.eq("DS_subEntity"), EasyMock.capture(subEntityCapture))).andReturn(dynamicSubEntity);
        EasyMock.expect(dynamicEntity.get("DS_id")).andReturn(100);
        EasyMock.expect(dynamicEntity.get("DS_name")).andReturn("foo");
        EasyMock.expect(dynamicEntity.get("DS_subEntity")).andReturn(dynamicSubEntity);
        EasyMock.expect(dynamicEntity.get("DS_class")).andReturn(dynamicEntity.getClass());
        EasyMock.expect(dynamicSubEntity.get("DS_id")).andReturn(99);
        EasyMock.expect(dynamicSubEntity.get("DS_name")).andReturn("bar");
        transaction.begin();
        transaction.commit();
        EasyMock.replay(entityManagerFactory, entityManager, jpaDynamicHelper, transaction, schemaManager, dynamicEntity, jpaEntityManager, session, databaseLogin, dynamicSubEntity);
        org.apache.ambari.server.view.persistence.DataStoreImpl dataStore = getDataStore(entityManagerFactory, jpaDynamicHelper, classLoader, schemaManager);
        dataStore.store(new org.apache.ambari.server.view.persistence.DataStoreImplTest.TestEntity(100, "foo", new org.apache.ambari.server.view.persistence.DataStoreImplTest.TestSubEntity(99, "bar")));
        if ((entityClassCapture.getValue() != typeCapture.getValue().getJavaClass()) && (entityClassCapture.getValue() != typeCapture2.getValue().getJavaClass())) {
            org.junit.Assert.fail();
        }
        if ((entityClassCapture2.getValue() != typeCapture.getValue().getJavaClass()) && (entityClassCapture2.getValue() != typeCapture2.getValue().getJavaClass())) {
            org.junit.Assert.fail();
        }
        EasyMock.verify(entityManagerFactory, entityManager, jpaDynamicHelper, transaction, schemaManager, dynamicEntity, jpaEntityManager, session, databaseLogin, dynamicSubEntity);
    }

    @org.junit.Test
    public void testStore_update_longStringValue() throws java.lang.Exception {
        org.eclipse.persistence.dynamic.DynamicClassLoader classLoader = new org.eclipse.persistence.dynamic.DynamicClassLoader(org.apache.ambari.server.view.persistence.DataStoreImplTest.class.getClassLoader());
        org.eclipse.persistence.jpa.JpaEntityManager jpaEntityManager = EasyMock.createMock(org.eclipse.persistence.jpa.JpaEntityManager.class);
        org.eclipse.persistence.sessions.server.ServerSession session = EasyMock.createMock(org.eclipse.persistence.sessions.server.ServerSession.class);
        org.eclipse.persistence.sessions.DatabaseLogin databaseLogin = EasyMock.createMock(org.eclipse.persistence.sessions.DatabaseLogin.class);
        javax.persistence.EntityManagerFactory entityManagerFactory = EasyMock.createMock(javax.persistence.EntityManagerFactory.class);
        javax.persistence.EntityManager entityManager = EasyMock.createMock(javax.persistence.EntityManager.class);
        org.eclipse.persistence.jpa.dynamic.JPADynamicHelper jpaDynamicHelper = EasyMock.createNiceMock(org.eclipse.persistence.jpa.dynamic.JPADynamicHelper.class);
        org.eclipse.persistence.tools.schemaframework.SchemaManager schemaManager = EasyMock.createNiceMock(org.eclipse.persistence.tools.schemaframework.SchemaManager.class);
        javax.persistence.EntityTransaction transaction = EasyMock.createMock(javax.persistence.EntityTransaction.class);
        org.eclipse.persistence.dynamic.DynamicEntity dynamicEntity = EasyMock.createMock(org.eclipse.persistence.dynamic.DynamicEntity.class);
        org.powermock.api.easymock.PowerMock.mockStatic(org.eclipse.persistence.jpa.JpaHelper.class);
        EasyMock.expect(org.eclipse.persistence.jpa.JpaHelper.getEntityManager(entityManager)).andReturn(jpaEntityManager).anyTimes();
        org.powermock.api.easymock.PowerMock.replay(org.eclipse.persistence.jpa.JpaHelper.class);
        EasyMock.expect(jpaEntityManager.getServerSession()).andReturn(session).anyTimes();
        EasyMock.expect(session.getLogin()).andReturn(databaseLogin).anyTimes();
        org.easymock.Capture<org.eclipse.persistence.sequencing.Sequence> sequenceCapture = org.easymock.EasyMock.newCapture();
        databaseLogin.addSequence(EasyMock.capture(sequenceCapture));
        org.easymock.EasyMock.expectLastCall().anyTimes();
        org.easymock.Capture<org.eclipse.persistence.dynamic.DynamicType> typeCapture = org.easymock.EasyMock.newCapture();
        org.easymock.Capture<org.eclipse.persistence.dynamic.DynamicType> typeCapture2 = org.easymock.EasyMock.newCapture();
        jpaDynamicHelper.addTypes(EasyMock.eq(true), EasyMock.eq(true), EasyMock.capture(typeCapture), EasyMock.capture(typeCapture2));
        EasyMock.expect(entityManagerFactory.createEntityManager()).andReturn(entityManager).anyTimes();
        EasyMock.expect(entityManager.getTransaction()).andReturn(transaction).anyTimes();
        org.easymock.Capture<java.lang.Class> entityClassCapture2 = org.easymock.EasyMock.newCapture();
        EasyMock.expect(entityManager.find(EasyMock.capture(entityClassCapture2), EasyMock.eq(99))).andReturn(dynamicEntity);
        entityManager.close();
        java.lang.StringBuilder sb = new java.lang.StringBuilder();
        for (int i = 0; i < 5000; ++i) {
            sb.append("A");
        }
        java.lang.String longString = sb.toString();
        EasyMock.expect(dynamicEntity.set("DS_id", 99)).andReturn(dynamicEntity).anyTimes();
        transaction.begin();
        EasyMock.expect(transaction.isActive()).andReturn(true).anyTimes();
        transaction.rollback();
        EasyMock.replay(entityManagerFactory, entityManager, jpaDynamicHelper, transaction, schemaManager, dynamicEntity, jpaEntityManager, session, databaseLogin);
        org.apache.ambari.server.view.persistence.DataStoreImpl dataStore = getDataStore(entityManagerFactory, jpaDynamicHelper, classLoader, schemaManager);
        try {
            dataStore.store(new org.apache.ambari.server.view.persistence.DataStoreImplTest.TestEntity(99, longString, new org.apache.ambari.server.view.persistence.DataStoreImplTest.TestSubEntity("bar")));
            org.junit.Assert.fail();
        } catch (org.apache.ambari.view.PersistenceException e) {
        }
        EasyMock.verify(entityManagerFactory, entityManager, jpaDynamicHelper, transaction, schemaManager, dynamicEntity, jpaEntityManager, session, databaseLogin);
    }

    @org.junit.Test
    public void testRemove() throws java.lang.Exception {
        org.eclipse.persistence.dynamic.DynamicClassLoader classLoader = new org.eclipse.persistence.dynamic.DynamicClassLoader(org.apache.ambari.server.view.persistence.DataStoreImplTest.class.getClassLoader());
        org.eclipse.persistence.jpa.JpaEntityManager jpaEntityManager = EasyMock.createMock(org.eclipse.persistence.jpa.JpaEntityManager.class);
        org.eclipse.persistence.sessions.server.ServerSession session = EasyMock.createMock(org.eclipse.persistence.sessions.server.ServerSession.class);
        org.eclipse.persistence.sessions.DatabaseLogin databaseLogin = EasyMock.createMock(org.eclipse.persistence.sessions.DatabaseLogin.class);
        javax.persistence.EntityManagerFactory entityManagerFactory = EasyMock.createMock(javax.persistence.EntityManagerFactory.class);
        javax.persistence.EntityManager entityManager = EasyMock.createMock(javax.persistence.EntityManager.class);
        org.eclipse.persistence.jpa.dynamic.JPADynamicHelper jpaDynamicHelper = EasyMock.createNiceMock(org.eclipse.persistence.jpa.dynamic.JPADynamicHelper.class);
        org.eclipse.persistence.tools.schemaframework.SchemaManager schemaManager = EasyMock.createNiceMock(org.eclipse.persistence.tools.schemaframework.SchemaManager.class);
        javax.persistence.EntityTransaction transaction = EasyMock.createMock(javax.persistence.EntityTransaction.class);
        org.eclipse.persistence.dynamic.DynamicEntity dynamicEntity = EasyMock.createMock(org.eclipse.persistence.dynamic.DynamicEntity.class);
        org.powermock.api.easymock.PowerMock.mockStatic(org.eclipse.persistence.jpa.JpaHelper.class);
        EasyMock.expect(org.eclipse.persistence.jpa.JpaHelper.getEntityManager(entityManager)).andReturn(jpaEntityManager).anyTimes();
        org.powermock.api.easymock.PowerMock.replay(org.eclipse.persistence.jpa.JpaHelper.class);
        EasyMock.expect(jpaEntityManager.getServerSession()).andReturn(session).anyTimes();
        EasyMock.expect(session.getLogin()).andReturn(databaseLogin).anyTimes();
        org.easymock.Capture<org.eclipse.persistence.sequencing.Sequence> sequenceCapture = org.easymock.EasyMock.newCapture();
        databaseLogin.addSequence(EasyMock.capture(sequenceCapture));
        org.easymock.EasyMock.expectLastCall().anyTimes();
        org.easymock.Capture<org.eclipse.persistence.dynamic.DynamicType> typeCapture = org.easymock.EasyMock.newCapture();
        org.easymock.Capture<org.eclipse.persistence.dynamic.DynamicType> typeCapture2 = org.easymock.EasyMock.newCapture();
        jpaDynamicHelper.addTypes(EasyMock.eq(true), EasyMock.eq(true), EasyMock.capture(typeCapture), EasyMock.capture(typeCapture2));
        EasyMock.expect(entityManagerFactory.createEntityManager()).andReturn(entityManager).anyTimes();
        EasyMock.expect(entityManager.getTransaction()).andReturn(transaction).anyTimes();
        org.easymock.Capture<java.lang.Class> entityClassCapture = org.easymock.EasyMock.newCapture();
        EasyMock.expect(entityManager.getReference(EasyMock.capture(entityClassCapture), EasyMock.eq(99))).andReturn(dynamicEntity);
        entityManager.remove(dynamicEntity);
        entityManager.close();
        transaction.begin();
        transaction.commit();
        EasyMock.replay(entityManagerFactory, entityManager, jpaDynamicHelper, transaction, schemaManager, dynamicEntity, jpaEntityManager, session, databaseLogin);
        org.apache.ambari.server.view.persistence.DataStoreImpl dataStore = getDataStore(entityManagerFactory, jpaDynamicHelper, classLoader, schemaManager);
        dataStore.remove(new org.apache.ambari.server.view.persistence.DataStoreImplTest.TestEntity(99, "foo", new org.apache.ambari.server.view.persistence.DataStoreImplTest.TestSubEntity("bar")));
        if ((entityClassCapture.getValue() != typeCapture.getValue().getJavaClass()) && (entityClassCapture.getValue() != typeCapture2.getValue().getJavaClass())) {
            org.junit.Assert.fail();
        }
        EasyMock.verify(entityManagerFactory, entityManager, jpaDynamicHelper, transaction, schemaManager, dynamicEntity, jpaEntityManager, session, databaseLogin);
    }

    @org.junit.Test
    public void testFind() throws java.lang.Exception {
        org.eclipse.persistence.dynamic.DynamicClassLoader classLoader = new org.eclipse.persistence.dynamic.DynamicClassLoader(org.apache.ambari.server.view.persistence.DataStoreImplTest.class.getClassLoader());
        org.eclipse.persistence.jpa.JpaEntityManager jpaEntityManager = EasyMock.createMock(org.eclipse.persistence.jpa.JpaEntityManager.class);
        org.eclipse.persistence.sessions.server.ServerSession session = EasyMock.createMock(org.eclipse.persistence.sessions.server.ServerSession.class);
        org.eclipse.persistence.sessions.DatabaseLogin databaseLogin = EasyMock.createMock(org.eclipse.persistence.sessions.DatabaseLogin.class);
        javax.persistence.EntityManagerFactory entityManagerFactory = EasyMock.createMock(javax.persistence.EntityManagerFactory.class);
        javax.persistence.EntityManager entityManager = EasyMock.createMock(javax.persistence.EntityManager.class);
        org.eclipse.persistence.jpa.dynamic.JPADynamicHelper jpaDynamicHelper = EasyMock.createNiceMock(org.eclipse.persistence.jpa.dynamic.JPADynamicHelper.class);
        org.eclipse.persistence.tools.schemaframework.SchemaManager schemaManager = EasyMock.createNiceMock(org.eclipse.persistence.tools.schemaframework.SchemaManager.class);
        org.eclipse.persistence.dynamic.DynamicEntity dynamicEntity = EasyMock.createMock(org.eclipse.persistence.dynamic.DynamicEntity.class);
        org.powermock.api.easymock.PowerMock.mockStatic(org.eclipse.persistence.jpa.JpaHelper.class);
        EasyMock.expect(org.eclipse.persistence.jpa.JpaHelper.getEntityManager(entityManager)).andReturn(jpaEntityManager).anyTimes();
        org.powermock.api.easymock.PowerMock.replay(org.eclipse.persistence.jpa.JpaHelper.class);
        EasyMock.expect(jpaEntityManager.getServerSession()).andReturn(session).anyTimes();
        EasyMock.expect(session.getLogin()).andReturn(databaseLogin).anyTimes();
        org.easymock.Capture<org.eclipse.persistence.sequencing.Sequence> sequenceCapture = org.easymock.EasyMock.newCapture();
        databaseLogin.addSequence(EasyMock.capture(sequenceCapture));
        org.easymock.EasyMock.expectLastCall().anyTimes();
        org.easymock.Capture<org.eclipse.persistence.dynamic.DynamicType> typeCapture = org.easymock.EasyMock.newCapture();
        org.easymock.Capture<org.eclipse.persistence.dynamic.DynamicType> typeCapture2 = org.easymock.EasyMock.newCapture();
        jpaDynamicHelper.addTypes(EasyMock.eq(true), EasyMock.eq(true), EasyMock.capture(typeCapture), EasyMock.capture(typeCapture2));
        EasyMock.expect(entityManagerFactory.createEntityManager()).andReturn(entityManager).anyTimes();
        org.easymock.Capture<java.lang.Class> entityClassCapture = org.easymock.EasyMock.newCapture();
        EasyMock.expect(entityManager.find(EasyMock.capture(entityClassCapture), EasyMock.eq(99))).andReturn(dynamicEntity);
        entityManager.close();
        EasyMock.expect(dynamicEntity.get("DS_id")).andReturn(99);
        EasyMock.expect(dynamicEntity.get("DS_name")).andReturn("foo");
        org.apache.ambari.server.view.persistence.DataStoreImplTest.TestSubEntity subEntity = new org.apache.ambari.server.view.persistence.DataStoreImplTest.TestSubEntity("bar");
        EasyMock.expect(dynamicEntity.get("DS_subEntity")).andReturn(subEntity);
        EasyMock.replay(entityManagerFactory, entityManager, jpaDynamicHelper, dynamicEntity, schemaManager, jpaEntityManager, session, databaseLogin);
        org.apache.ambari.server.view.persistence.DataStoreImpl dataStore = getDataStore(entityManagerFactory, jpaDynamicHelper, classLoader, schemaManager);
        org.apache.ambari.server.view.persistence.DataStoreImplTest.TestEntity entity = dataStore.find(org.apache.ambari.server.view.persistence.DataStoreImplTest.TestEntity.class, 99);
        if ((entityClassCapture.getValue() != typeCapture.getValue().getJavaClass()) && (entityClassCapture.getValue() != typeCapture2.getValue().getJavaClass())) {
            org.junit.Assert.fail();
        }
        org.junit.Assert.assertEquals(99, ((int) (entity.getId())));
        org.junit.Assert.assertEquals("foo", entity.getName());
        EasyMock.verify(entityManagerFactory, entityManager, jpaDynamicHelper, dynamicEntity, schemaManager, jpaEntityManager, session, databaseLogin);
    }

    @org.junit.Test
    public void testFindAll() throws java.lang.Exception {
        org.eclipse.persistence.dynamic.DynamicClassLoader classLoader = new org.eclipse.persistence.dynamic.DynamicClassLoader(org.apache.ambari.server.view.persistence.DataStoreImplTest.class.getClassLoader());
        org.eclipse.persistence.jpa.JpaEntityManager jpaEntityManager = EasyMock.createMock(org.eclipse.persistence.jpa.JpaEntityManager.class);
        org.eclipse.persistence.sessions.server.ServerSession session = EasyMock.createMock(org.eclipse.persistence.sessions.server.ServerSession.class);
        org.eclipse.persistence.sessions.DatabaseLogin databaseLogin = EasyMock.createMock(org.eclipse.persistence.sessions.DatabaseLogin.class);
        javax.persistence.EntityManagerFactory entityManagerFactory = EasyMock.createMock(javax.persistence.EntityManagerFactory.class);
        javax.persistence.EntityManager entityManager = EasyMock.createMock(javax.persistence.EntityManager.class);
        org.eclipse.persistence.jpa.dynamic.JPADynamicHelper jpaDynamicHelper = EasyMock.createNiceMock(org.eclipse.persistence.jpa.dynamic.JPADynamicHelper.class);
        org.eclipse.persistence.tools.schemaframework.SchemaManager schemaManager = EasyMock.createNiceMock(org.eclipse.persistence.tools.schemaframework.SchemaManager.class);
        org.eclipse.persistence.dynamic.DynamicEntity dynamicEntity = EasyMock.createMock(org.eclipse.persistence.dynamic.DynamicEntity.class);
        javax.persistence.Query query = EasyMock.createMock(javax.persistence.Query.class);
        org.powermock.api.easymock.PowerMock.mockStatic(org.eclipse.persistence.jpa.JpaHelper.class);
        EasyMock.expect(org.eclipse.persistence.jpa.JpaHelper.getEntityManager(entityManager)).andReturn(jpaEntityManager).anyTimes();
        org.powermock.api.easymock.PowerMock.replay(org.eclipse.persistence.jpa.JpaHelper.class);
        EasyMock.expect(jpaEntityManager.getServerSession()).andReturn(session).anyTimes();
        EasyMock.expect(session.getLogin()).andReturn(databaseLogin).anyTimes();
        org.easymock.Capture<org.eclipse.persistence.sequencing.Sequence> sequenceCapture = org.easymock.EasyMock.newCapture();
        databaseLogin.addSequence(EasyMock.capture(sequenceCapture));
        org.easymock.EasyMock.expectLastCall().anyTimes();
        org.easymock.Capture<org.eclipse.persistence.dynamic.DynamicType> typeCapture = org.easymock.EasyMock.newCapture();
        org.easymock.Capture<org.eclipse.persistence.dynamic.DynamicType> typeCapture2 = org.easymock.EasyMock.newCapture();
        jpaDynamicHelper.addTypes(EasyMock.eq(true), EasyMock.eq(true), EasyMock.capture(typeCapture), EasyMock.capture(typeCapture2));
        EasyMock.expect(entityManagerFactory.createEntityManager()).andReturn(entityManager).anyTimes();
        EasyMock.expect(entityManager.createQuery("SELECT e FROM DS_DataStoreImplTest$TestEntity_1 e WHERE e.DS_id=99")).andReturn(query);
        entityManager.close();
        EasyMock.expect(query.getResultList()).andReturn(java.util.Collections.singletonList(dynamicEntity));
        EasyMock.expect(dynamicEntity.get("DS_id")).andReturn(99);
        EasyMock.expect(dynamicEntity.get("DS_name")).andReturn("foo");
        org.apache.ambari.server.view.persistence.DataStoreImplTest.TestSubEntity subEntity = new org.apache.ambari.server.view.persistence.DataStoreImplTest.TestSubEntity("bar");
        EasyMock.expect(dynamicEntity.get("DS_subEntity")).andReturn(subEntity);
        EasyMock.replay(entityManagerFactory, entityManager, jpaDynamicHelper, dynamicEntity, query, schemaManager, jpaEntityManager, session, databaseLogin);
        org.apache.ambari.server.view.persistence.DataStoreImpl dataStore = getDataStore(entityManagerFactory, jpaDynamicHelper, classLoader, schemaManager);
        java.util.Collection<org.apache.ambari.server.view.persistence.DataStoreImplTest.TestEntity> entities = dataStore.findAll(org.apache.ambari.server.view.persistence.DataStoreImplTest.TestEntity.class, "id=99");
        org.junit.Assert.assertEquals(1, entities.size());
        org.apache.ambari.server.view.persistence.DataStoreImplTest.TestEntity entity = entities.iterator().next();
        org.junit.Assert.assertEquals(99, ((int) (entity.getId())));
        org.junit.Assert.assertEquals("foo", entity.getName());
        EasyMock.verify(entityManagerFactory, entityManager, jpaDynamicHelper, dynamicEntity, query, schemaManager, jpaEntityManager, session, databaseLogin);
    }

    @org.junit.Test
    public void testFindAll_multiple() throws java.lang.Exception {
        org.eclipse.persistence.dynamic.DynamicClassLoader classLoader = new org.eclipse.persistence.dynamic.DynamicClassLoader(org.apache.ambari.server.view.persistence.DataStoreImplTest.class.getClassLoader());
        org.eclipse.persistence.jpa.JpaEntityManager jpaEntityManager = EasyMock.createMock(org.eclipse.persistence.jpa.JpaEntityManager.class);
        org.eclipse.persistence.sessions.server.ServerSession session = EasyMock.createMock(org.eclipse.persistence.sessions.server.ServerSession.class);
        org.eclipse.persistence.sessions.DatabaseLogin databaseLogin = EasyMock.createMock(org.eclipse.persistence.sessions.DatabaseLogin.class);
        javax.persistence.EntityManagerFactory entityManagerFactory = EasyMock.createMock(javax.persistence.EntityManagerFactory.class);
        javax.persistence.EntityManager entityManager = EasyMock.createMock(javax.persistence.EntityManager.class);
        org.eclipse.persistence.jpa.dynamic.JPADynamicHelper jpaDynamicHelper = EasyMock.createNiceMock(org.eclipse.persistence.jpa.dynamic.JPADynamicHelper.class);
        org.eclipse.persistence.tools.schemaframework.SchemaManager schemaManager = EasyMock.createNiceMock(org.eclipse.persistence.tools.schemaframework.SchemaManager.class);
        org.eclipse.persistence.dynamic.DynamicEntity dynamicEntity1 = EasyMock.createMock(org.eclipse.persistence.dynamic.DynamicEntity.class);
        org.eclipse.persistence.dynamic.DynamicEntity dynamicEntity2 = EasyMock.createMock(org.eclipse.persistence.dynamic.DynamicEntity.class);
        org.eclipse.persistence.dynamic.DynamicEntity dynamicEntity3 = EasyMock.createMock(org.eclipse.persistence.dynamic.DynamicEntity.class);
        javax.persistence.Query query = EasyMock.createMock(javax.persistence.Query.class);
        org.powermock.api.easymock.PowerMock.mockStatic(org.eclipse.persistence.jpa.JpaHelper.class);
        EasyMock.expect(org.eclipse.persistence.jpa.JpaHelper.getEntityManager(entityManager)).andReturn(jpaEntityManager).anyTimes();
        org.powermock.api.easymock.PowerMock.replay(org.eclipse.persistence.jpa.JpaHelper.class);
        EasyMock.expect(jpaEntityManager.getServerSession()).andReturn(session).anyTimes();
        EasyMock.expect(session.getLogin()).andReturn(databaseLogin).anyTimes();
        org.easymock.Capture<org.eclipse.persistence.sequencing.Sequence> sequenceCapture = org.easymock.EasyMock.newCapture();
        databaseLogin.addSequence(EasyMock.capture(sequenceCapture));
        org.easymock.EasyMock.expectLastCall().anyTimes();
        org.easymock.Capture<org.eclipse.persistence.dynamic.DynamicType> typeCapture = org.easymock.EasyMock.newCapture();
        org.easymock.Capture<org.eclipse.persistence.dynamic.DynamicType> typeCapture2 = org.easymock.EasyMock.newCapture();
        jpaDynamicHelper.addTypes(EasyMock.eq(true), EasyMock.eq(true), EasyMock.capture(typeCapture), EasyMock.capture(typeCapture2));
        EasyMock.expect(entityManagerFactory.createEntityManager()).andReturn(entityManager).anyTimes();
        EasyMock.expect(entityManager.createQuery("SELECT e FROM DS_DataStoreImplTest$TestEntity_1 e WHERE e.DS_name='foo'")).andReturn(query);
        entityManager.close();
        java.util.List<org.eclipse.persistence.dynamic.DynamicEntity> entityList = new java.util.LinkedList<>();
        entityList.add(dynamicEntity1);
        entityList.add(dynamicEntity2);
        entityList.add(dynamicEntity3);
        EasyMock.expect(query.getResultList()).andReturn(entityList);
        EasyMock.expect(dynamicEntity1.get("DS_id")).andReturn(99);
        EasyMock.expect(dynamicEntity1.get("DS_name")).andReturn("foo");
        org.apache.ambari.server.view.persistence.DataStoreImplTest.TestSubEntity subEntity1 = new org.apache.ambari.server.view.persistence.DataStoreImplTest.TestSubEntity("bar");
        EasyMock.expect(dynamicEntity1.get("DS_subEntity")).andReturn(subEntity1);
        EasyMock.expect(dynamicEntity2.get("DS_id")).andReturn(100);
        EasyMock.expect(dynamicEntity2.get("DS_name")).andReturn("foo");
        org.apache.ambari.server.view.persistence.DataStoreImplTest.TestSubEntity subEntity2 = new org.apache.ambari.server.view.persistence.DataStoreImplTest.TestSubEntity("bar");
        EasyMock.expect(dynamicEntity2.get("DS_subEntity")).andReturn(subEntity2);
        EasyMock.expect(dynamicEntity3.get("DS_id")).andReturn(101);
        EasyMock.expect(dynamicEntity3.get("DS_name")).andReturn("foo");
        org.apache.ambari.server.view.persistence.DataStoreImplTest.TestSubEntity subEntity3 = new org.apache.ambari.server.view.persistence.DataStoreImplTest.TestSubEntity("bar");
        EasyMock.expect(dynamicEntity3.get("DS_subEntity")).andReturn(subEntity3);
        EasyMock.replay(entityManagerFactory, entityManager, jpaDynamicHelper, dynamicEntity1, dynamicEntity2, dynamicEntity3, query, schemaManager, jpaEntityManager, session, databaseLogin);
        org.apache.ambari.server.view.persistence.DataStoreImpl dataStore = getDataStore(entityManagerFactory, jpaDynamicHelper, classLoader, schemaManager);
        java.util.Collection<org.apache.ambari.server.view.persistence.DataStoreImplTest.TestEntity> entities = dataStore.findAll(org.apache.ambari.server.view.persistence.DataStoreImplTest.TestEntity.class, "name='foo'");
        org.junit.Assert.assertEquals(3, entities.size());
        for (org.apache.ambari.server.view.persistence.DataStoreImplTest.TestEntity entity : entities) {
            org.junit.Assert.assertEquals("foo", entity.getName());
        }
        EasyMock.verify(entityManagerFactory, entityManager, jpaDynamicHelper, dynamicEntity1, dynamicEntity2, dynamicEntity3, query, schemaManager, jpaEntityManager, session, databaseLogin);
    }

    private org.apache.ambari.server.view.persistence.DataStoreImpl getDataStore(javax.persistence.EntityManagerFactory entityManagerFactory, org.eclipse.persistence.jpa.dynamic.JPADynamicHelper jpaDynamicHelper, org.eclipse.persistence.dynamic.DynamicClassLoader classLoader, org.eclipse.persistence.tools.schemaframework.SchemaManager schemaManager) throws java.lang.Exception {
        org.apache.ambari.server.view.configuration.ViewConfig viewConfig = org.apache.ambari.server.view.configuration.ViewConfigTest.getConfig(org.apache.ambari.server.view.persistence.DataStoreImplTest.xml);
        org.apache.ambari.server.orm.entities.ViewEntity viewDefinition = org.apache.ambari.server.orm.entities.ViewEntityTest.getViewEntity(viewConfig);
        org.apache.ambari.server.view.configuration.InstanceConfig instanceConfig = org.apache.ambari.server.view.configuration.InstanceConfigTest.getInstanceConfigs().get(0);
        org.apache.ambari.server.orm.entities.ViewInstanceEntity viewInstanceEntity = new org.apache.ambari.server.orm.entities.ViewInstanceEntity(viewDefinition, instanceConfig);
        org.apache.ambari.server.view.persistence.DataStoreImplTest.setPersistenceEntities(viewInstanceEntity);
        com.google.inject.Injector injector = com.google.inject.Guice.createInjector(new org.apache.ambari.server.view.persistence.DataStoreImplTest.TestModule(viewInstanceEntity, entityManagerFactory, jpaDynamicHelper, classLoader, schemaManager));
        return injector.getInstance(org.apache.ambari.server.view.persistence.DataStoreImpl.class);
    }

    private static void setPersistenceEntities(org.apache.ambari.server.orm.entities.ViewInstanceEntity viewInstanceDefinition) {
        org.apache.ambari.server.orm.entities.ViewEntity viewDefinition = viewInstanceDefinition.getViewEntity();
        java.util.Collection<org.apache.ambari.server.orm.entities.ViewEntityEntity> entities = new java.util.HashSet<>();
        org.apache.ambari.server.view.configuration.ViewConfig viewConfig = viewDefinition.getConfiguration();
        for (org.apache.ambari.server.view.configuration.EntityConfig entityConfiguration : viewConfig.getPersistence().getEntities()) {
            org.apache.ambari.server.orm.entities.ViewEntityEntity viewEntityEntity = new org.apache.ambari.server.orm.entities.ViewEntityEntity();
            viewEntityEntity.setId(1L);
            viewEntityEntity.setViewName(viewDefinition.getName());
            viewEntityEntity.setViewInstanceName(viewInstanceDefinition.getName());
            viewEntityEntity.setClassName(entityConfiguration.getClassName());
            viewEntityEntity.setIdProperty(entityConfiguration.getIdProperty());
            viewEntityEntity.setViewInstance(viewInstanceDefinition);
            entities.add(viewEntityEntity);
        }
        viewInstanceDefinition.setEntities(entities);
    }

    public static class TestEntity {
        public TestEntity() {
        }

        TestEntity(int id, java.lang.String name, org.apache.ambari.server.view.persistence.DataStoreImplTest.TestSubEntity subEntity) {
            this.id = id;
            this.name = name;
            this.subEntity = subEntity;
        }

        TestEntity(java.lang.String name, org.apache.ambari.server.view.persistence.DataStoreImplTest.TestSubEntity subEntity) {
            this.name = name;
            this.subEntity = subEntity;
        }

        java.lang.Integer id = null;

        java.lang.String name;

        org.apache.ambari.server.view.persistence.DataStoreImplTest.TestSubEntity subEntity;

        public java.lang.Integer getId() {
            return id;
        }

        public void setId(java.lang.Integer id) {
            this.id = id;
        }

        public java.lang.String getName() {
            return name;
        }

        public void setName(java.lang.String name) {
            this.name = name;
        }

        public org.apache.ambari.server.view.persistence.DataStoreImplTest.TestSubEntity getSubEntity() {
            return subEntity;
        }

        public void setSubEntity(org.apache.ambari.server.view.persistence.DataStoreImplTest.TestSubEntity subEntity) {
            this.subEntity = subEntity;
        }
    }

    public static class TestSubEntity {
        private java.lang.Integer id = null;

        public TestSubEntity() {
        }

        TestSubEntity(java.lang.String name) {
            this.name = name;
        }

        TestSubEntity(java.lang.Integer id, java.lang.String name) {
            this.id = id;
            this.name = name;
        }

        java.lang.String name;

        public java.lang.Integer getId() {
            return id;
        }

        public void setId(java.lang.Integer id) {
            this.id = id;
        }

        public java.lang.String getName() {
            return name;
        }

        public void setName(java.lang.String name) {
            this.name = name;
        }
    }

    public static class TestLargeEntity {
        public TestLargeEntity() {
        }

        public TestLargeEntity(int id) {
            this.id = id;
        }

        java.lang.Integer id = null;

        public java.lang.Integer getId() {
            return id;
        }

        public void setId(java.lang.Integer id) {
            this.id = id;
        }
    }

    private static class TestModule implements com.google.inject.Module , org.apache.ambari.server.view.persistence.SchemaManagerFactory {
        private final org.apache.ambari.server.orm.entities.ViewInstanceEntity viewInstanceEntity;

        private final javax.persistence.EntityManagerFactory entityManagerFactory;

        private final org.eclipse.persistence.jpa.dynamic.JPADynamicHelper jpaDynamicHelper;

        private final org.eclipse.persistence.dynamic.DynamicClassLoader classLoader;

        private final org.eclipse.persistence.tools.schemaframework.SchemaManager schemaManager;

        private TestModule(org.apache.ambari.server.orm.entities.ViewInstanceEntity viewInstanceEntity, javax.persistence.EntityManagerFactory entityManagerFactory, org.eclipse.persistence.jpa.dynamic.JPADynamicHelper jpaDynamicHelper, org.eclipse.persistence.dynamic.DynamicClassLoader classLoader, org.eclipse.persistence.tools.schemaframework.SchemaManager schemaManager) {
            this.viewInstanceEntity = viewInstanceEntity;
            this.entityManagerFactory = entityManagerFactory;
            this.jpaDynamicHelper = jpaDynamicHelper;
            this.classLoader = classLoader;
            this.schemaManager = schemaManager;
        }

        @java.lang.Override
        public void configure(com.google.inject.Binder binder) {
            binder.bind(org.apache.ambari.server.orm.entities.ViewInstanceEntity.class).toInstance(viewInstanceEntity);
            binder.bind(javax.persistence.EntityManagerFactory.class).toInstance(entityManagerFactory);
            binder.bind(org.eclipse.persistence.jpa.dynamic.JPADynamicHelper.class).toInstance(jpaDynamicHelper);
            binder.bind(org.eclipse.persistence.dynamic.DynamicClassLoader.class).toInstance(classLoader);
            binder.bind(org.apache.ambari.server.view.persistence.SchemaManagerFactory.class).toInstance(this);
        }

        @java.lang.Override
        public org.eclipse.persistence.tools.schemaframework.SchemaManager getSchemaManager(org.eclipse.persistence.sessions.DatabaseSession session) {
            return schemaManager;
        }
    }
}