package org.apache.ambari.server.agent;
import org.easymock.EasyMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
public class AgentSessionManagerTest {
    private org.apache.ambari.server.agent.AgentSessionManager underTest;

    @org.junit.Before
    public void setUp() {
        underTest = new org.apache.ambari.server.agent.AgentSessionManager();
    }

    @org.junit.Test
    public void hostIsRegistered() throws org.apache.ambari.server.HostNotRegisteredException {
        java.lang.String sessionId = "session ID";
        java.lang.Long hostId = 1L;
        org.apache.ambari.server.state.Host host = org.easymock.EasyMock.createNiceMock(org.apache.ambari.server.state.Host.class);
        EasyMock.expect(host.getHostId()).andReturn(hostId).anyTimes();
        EasyMock.replay(host);
        underTest.register(sessionId, host);
        org.junit.Assert.assertTrue(underTest.isRegistered(sessionId));
        org.junit.Assert.assertEquals(sessionId, underTest.getSessionId(hostId));
        org.junit.Assert.assertSame(host, underTest.getHost(sessionId));
    }

    @org.junit.Test(expected = org.apache.ambari.server.HostNotRegisteredException.class)
    public void exceptionThrownForUnknownHost() throws org.apache.ambari.server.HostNotRegisteredException {
        java.lang.Long notRegisteredHostId = 2L;
        underTest.getSessionId(notRegisteredHostId);
    }

    @org.junit.Test(expected = org.apache.ambari.server.HostNotRegisteredException.class)
    public void exceptionThrownForUnknownSessionId() throws org.apache.ambari.server.HostNotRegisteredException {
        underTest.getHost("unknown session ID");
    }

    @org.junit.Test
    public void registerRemovesOldSessionId() throws org.apache.ambari.server.HostNotRegisteredException {
        java.lang.String oldSessionId = "old session ID";
        java.lang.String newSessionId = "new session ID";
        java.lang.Long hostId = 1L;
        org.apache.ambari.server.state.Host host = org.easymock.EasyMock.createNiceMock(org.apache.ambari.server.state.Host.class);
        EasyMock.expect(host.getHostId()).andReturn(hostId).anyTimes();
        EasyMock.replay(host);
        underTest.register(oldSessionId, host);
        underTest.register(newSessionId, host);
        org.junit.Assert.assertFalse(underTest.isRegistered(oldSessionId));
        org.junit.Assert.assertEquals(newSessionId, underTest.getSessionId(hostId));
        org.junit.Assert.assertSame(host, underTest.getHost(newSessionId));
    }

    @org.junit.Test(expected = org.apache.ambari.server.HostNotRegisteredException.class)
    public void unregisterRemovesSessionId() throws org.apache.ambari.server.HostNotRegisteredException {
        java.lang.String sessionId = "session ID";
        java.lang.Long hostId = 1L;
        org.apache.ambari.server.state.Host host = org.easymock.EasyMock.createNiceMock(org.apache.ambari.server.state.Host.class);
        EasyMock.expect(host.getHostId()).andReturn(hostId).anyTimes();
        EasyMock.replay(host);
        underTest.register(sessionId, host);
        underTest.unregisterByHost(hostId);
        org.junit.Assert.assertFalse(underTest.isRegistered(sessionId));
        underTest.getSessionId(hostId);
    }
}