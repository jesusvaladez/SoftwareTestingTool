package org.apache.ambari.server.orm.dao;
import javax.persistence.EntityManager;
import static org.easymock.EasyMock.createNiceMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
public class HostComponentStateDAOTest {
    @org.junit.Test
    public void testRemove() throws java.lang.Exception {
        com.google.inject.Provider<javax.persistence.EntityManager> entityManagerProvider = EasyMock.createNiceMock(com.google.inject.Provider.class);
        javax.persistence.EntityManager entityManager = EasyMock.createNiceMock(javax.persistence.EntityManager.class);
        org.apache.ambari.server.orm.dao.HostDAO hostDAO = EasyMock.createNiceMock(org.apache.ambari.server.orm.dao.HostDAO.class);
        org.apache.ambari.server.orm.entities.HostEntity hostEntity = EasyMock.createNiceMock(org.apache.ambari.server.orm.entities.HostEntity.class);
        org.apache.ambari.server.orm.entities.HostComponentStateEntity hostComponentStateEntity = EasyMock.createNiceMock(org.apache.ambari.server.orm.entities.HostComponentStateEntity.class);
        EasyMock.expect(entityManagerProvider.get()).andReturn(entityManager).anyTimes();
        entityManager.remove(hostComponentStateEntity);
        EasyMock.replay(entityManagerProvider, entityManager, hostDAO, hostEntity, hostComponentStateEntity);
        org.apache.ambari.server.orm.dao.HostComponentStateDAO dao = new org.apache.ambari.server.orm.dao.HostComponentStateDAO();
        dao.entityManagerProvider = entityManagerProvider;
        dao.hostDAO = hostDAO;
        dao.remove(hostComponentStateEntity);
        EasyMock.verify(entityManagerProvider, entityManager, hostDAO, hostEntity, hostComponentStateEntity);
    }
}