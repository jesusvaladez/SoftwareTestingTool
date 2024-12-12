package org.apache.ambari.server.orm.dao;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import static org.easymock.EasyMock.anyObject;
import static org.easymock.EasyMock.anyString;
import static org.easymock.EasyMock.createNiceMock;
import static org.easymock.EasyMock.createStrictMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
public class UserDAOTest {
    private static java.lang.String SERVICEOP_USER_NAME = "serviceopuser";

    private org.apache.ambari.server.orm.dao.UserDAO userDAO;

    public void init(org.apache.ambari.server.orm.entities.UserEntity userInDB) {
        final javax.persistence.EntityManager entityManager = EasyMock.createStrictMock(javax.persistence.EntityManager.class);
        final org.apache.ambari.server.orm.dao.DaoUtils daoUtils = EasyMock.createNiceMock(org.apache.ambari.server.orm.dao.DaoUtils.class);
        final org.apache.ambari.server.orm.DBAccessor dbAccessor = EasyMock.createNiceMock(org.apache.ambari.server.orm.DBAccessor.class);
        final com.google.inject.Injector mockInjector = com.google.inject.Guice.createInjector(new com.google.inject.AbstractModule() {
            @java.lang.Override
            protected void configure() {
                bind(org.apache.ambari.server.orm.dao.UserDAOTest.EntityManagerProvider.class);
                bind(javax.persistence.EntityManager.class).toInstance(entityManager);
                bind(org.apache.ambari.server.orm.DBAccessor.class).toInstance(dbAccessor);
                bind(org.apache.ambari.server.orm.dao.DaoUtils.class).toInstance(daoUtils);
            }
        });
        userDAO = mockInjector.getInstance(org.apache.ambari.server.orm.dao.UserDAO.class);
        javax.persistence.TypedQuery<org.apache.ambari.server.orm.entities.UserEntity> userQuery = EasyMock.createNiceMock(javax.persistence.TypedQuery.class);
        EasyMock.expect(userQuery.getSingleResult()).andReturn(userInDB);
        EasyMock.expect(entityManager.createNamedQuery(EasyMock.anyString(), EasyMock.anyObject(java.lang.Class.class))).andReturn(userQuery);
        EasyMock.replay(entityManager, daoUtils, dbAccessor, userQuery);
    }

    @org.junit.Test
    public void testUserByName() {
        init(org.apache.ambari.server.orm.dao.UserDAOTest.user());
        org.junit.Assert.assertEquals(org.apache.ambari.server.orm.dao.UserDAOTest.SERVICEOP_USER_NAME, userDAO.findUserByName(org.apache.ambari.server.orm.dao.UserDAOTest.SERVICEOP_USER_NAME).getUserName());
    }

    private static final org.apache.ambari.server.orm.entities.UserEntity user() {
        return org.apache.ambari.server.orm.dao.UserDAOTest.user(org.apache.ambari.server.orm.dao.UserDAOTest.SERVICEOP_USER_NAME);
    }

    private static final org.apache.ambari.server.orm.entities.UserEntity user(java.lang.String name) {
        org.apache.ambari.server.orm.entities.UserEntity userEntity = new org.apache.ambari.server.orm.entities.UserEntity();
        userEntity.setUserName(org.apache.ambari.server.security.authorization.UserName.fromString(name).toString());
        return userEntity;
    }

    static class EntityManagerProvider implements com.google.inject.Provider<javax.persistence.EntityManager> {
        private final javax.persistence.EntityManager entityManager;

        @com.google.inject.Inject
        public EntityManagerProvider(javax.persistence.EntityManager entityManager) {
            this.entityManager = entityManager;
        }

        @java.lang.Override
        public javax.persistence.EntityManager get() {
            return entityManager;
        }
    }
}