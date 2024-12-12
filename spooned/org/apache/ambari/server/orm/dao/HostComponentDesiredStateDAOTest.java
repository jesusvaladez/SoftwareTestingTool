package org.apache.ambari.server.orm.dao;
import javax.persistence.EntityManager;
import static org.easymock.EasyMock.createNiceMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
public class HostComponentDesiredStateDAOTest {
    @org.junit.Test
    public void testRemove() throws java.lang.Exception {
        com.google.inject.Provider<javax.persistence.EntityManager> entityManagerProvider = EasyMock.createNiceMock(com.google.inject.Provider.class);
        javax.persistence.EntityManager entityManager = EasyMock.createNiceMock(javax.persistence.EntityManager.class);
        org.apache.ambari.server.orm.dao.HostDAO hostDAO = EasyMock.createNiceMock(org.apache.ambari.server.orm.dao.HostDAO.class);
        org.apache.ambari.server.orm.entities.HostEntity hostEntity = EasyMock.createNiceMock(org.apache.ambari.server.orm.entities.HostEntity.class);
        org.apache.ambari.server.orm.entities.HostComponentDesiredStateEntity hostComponentDesiredStateEntity = EasyMock.createNiceMock(org.apache.ambari.server.orm.entities.HostComponentDesiredStateEntity.class);
        EasyMock.expect(entityManagerProvider.get()).andReturn(entityManager).anyTimes();
        entityManager.remove(hostComponentDesiredStateEntity);
        hostEntity.removeHostComponentDesiredStateEntity(hostComponentDesiredStateEntity);
        EasyMock.expect(hostDAO.merge(hostEntity)).andReturn(hostEntity).atLeastOnce();
        EasyMock.expect(hostComponentDesiredStateEntity.getHostEntity()).andReturn(hostEntity).atLeastOnce();
        EasyMock.replay(entityManagerProvider, entityManager, hostDAO, hostEntity, hostComponentDesiredStateEntity);
        org.apache.ambari.server.orm.dao.HostComponentDesiredStateDAO dao = new org.apache.ambari.server.orm.dao.HostComponentDesiredStateDAO();
        dao.entityManagerProvider = entityManagerProvider;
        dao.hostDAO = hostDAO;
        dao.remove(hostComponentDesiredStateEntity);
        EasyMock.verify(entityManagerProvider, entityManager, hostDAO, hostEntity, hostComponentDesiredStateEntity);
    }
}