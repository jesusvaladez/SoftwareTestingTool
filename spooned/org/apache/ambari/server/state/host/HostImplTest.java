package org.apache.ambari.server.state.host;
import org.easymock.EasyMockSupport;
import static org.easymock.EasyMock.expect;
public class HostImplTest extends org.easymock.EasyMockSupport {
    @org.junit.Test
    public void testGetHostAttributes() throws java.lang.Exception {
        org.apache.ambari.server.orm.entities.HostEntity hostEntity = createNiceMock(org.apache.ambari.server.orm.entities.HostEntity.class);
        org.apache.ambari.server.orm.entities.HostStateEntity hostStateEntity = createNiceMock(org.apache.ambari.server.orm.entities.HostStateEntity.class);
        org.apache.ambari.server.orm.dao.HostDAO hostDAO = createNiceMock(org.apache.ambari.server.orm.dao.HostDAO.class);
        org.apache.ambari.server.orm.dao.HostStateDAO hostStateDAO = createNiceMock(org.apache.ambari.server.orm.dao.HostStateDAO.class);
        com.google.gson.Gson gson = new com.google.gson.Gson();
        EasyMock.expect(hostEntity.getHostAttributes()).andReturn("{\"foo\": \"aaa\", \"bar\":\"bbb\"}").anyTimes();
        EasyMock.expect(hostEntity.getHostId()).andReturn(1L).anyTimes();
        EasyMock.expect(hostEntity.getHostName()).andReturn("host1").anyTimes();
        EasyMock.expect(hostEntity.getHostStateEntity()).andReturn(hostStateEntity).anyTimes();
        EasyMock.expect(hostDAO.findById(1L)).andReturn(hostEntity).atLeastOnce();
        replayAll();
        org.apache.ambari.server.state.host.HostImpl host = new org.apache.ambari.server.state.host.HostImpl(hostEntity, gson, hostDAO, hostStateDAO);
        java.util.Map<java.lang.String, java.lang.String> hostAttributes = host.getHostAttributes();
        org.junit.Assert.assertEquals("aaa", hostAttributes.get("foo"));
        org.junit.Assert.assertEquals("bbb", hostAttributes.get("bar"));
        host = new org.apache.ambari.server.state.host.HostImpl(hostEntity, gson, hostDAO, hostStateDAO);
        hostAttributes = host.getHostAttributes();
        org.junit.Assert.assertEquals("aaa", hostAttributes.get("foo"));
        org.junit.Assert.assertEquals("bbb", hostAttributes.get("bar"));
        verifyAll();
    }

    @org.junit.Test
    public void testGetHealthStatus() throws java.lang.Exception {
        org.apache.ambari.server.orm.entities.HostEntity hostEntity = createNiceMock(org.apache.ambari.server.orm.entities.HostEntity.class);
        org.apache.ambari.server.orm.entities.HostStateEntity hostStateEntity = createNiceMock(org.apache.ambari.server.orm.entities.HostStateEntity.class);
        org.apache.ambari.server.orm.dao.HostDAO hostDAO = createNiceMock(org.apache.ambari.server.orm.dao.HostDAO.class);
        org.apache.ambari.server.orm.dao.HostStateDAO hostStateDAO = createNiceMock(org.apache.ambari.server.orm.dao.HostStateDAO.class);
        com.google.gson.Gson gson = new com.google.gson.Gson();
        EasyMock.expect(hostEntity.getHostAttributes()).andReturn("{\"foo\": \"aaa\", \"bar\":\"bbb\"}").anyTimes();
        EasyMock.expect(hostEntity.getHostName()).andReturn("host1").anyTimes();
        EasyMock.expect(hostEntity.getHostId()).andReturn(1L).anyTimes();
        EasyMock.expect(hostEntity.getHostStateEntity()).andReturn(hostStateEntity).anyTimes();
        EasyMock.expect(hostDAO.findById(1L)).andReturn(hostEntity).anyTimes();
        EasyMock.expect(hostStateDAO.findByHostId(1L)).andReturn(hostStateEntity).atLeastOnce();
        replayAll();
        org.apache.ambari.server.state.host.HostImpl host = new org.apache.ambari.server.state.host.HostImpl(hostEntity, gson, hostDAO, hostStateDAO);
        host.getHealthStatus();
        host = new org.apache.ambari.server.state.host.HostImpl(hostEntity, gson, hostDAO, hostStateDAO);
        host.getHealthStatus();
        verifyAll();
    }
}