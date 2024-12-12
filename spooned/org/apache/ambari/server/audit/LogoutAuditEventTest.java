package org.apache.ambari.server.audit;
import nl.jqno.equalsverifier.EqualsVerifier;
public class LogoutAuditEventTest {
    @org.junit.Test
    public void testAuditMessage() throws java.lang.Exception {
        java.lang.String testUserName = "USER1";
        java.lang.String testRemoteIp = "127.0.0.1";
        java.lang.String testProxyUserName = "PROXYUSER1";
        org.apache.ambari.server.audit.event.LogoutAuditEvent evnt = org.apache.ambari.server.audit.event.LogoutAuditEvent.builder().withTimestamp(java.lang.System.currentTimeMillis()).withRemoteIp(testRemoteIp).withUserName(testUserName).withProxyUserName(null).build();
        java.lang.String actualAuditMessage = evnt.getAuditMessage();
        java.lang.String expectedAuditMessage = java.lang.String.format("User(%s), RemoteIp(%s), Operation(Logout), Status(Success)", testUserName, testRemoteIp);
        org.junit.Assert.assertThat(actualAuditMessage, org.hamcrest.core.IsEqual.equalTo(expectedAuditMessage));
        evnt = org.apache.ambari.server.audit.event.LogoutAuditEvent.builder().withTimestamp(java.lang.System.currentTimeMillis()).withRemoteIp(testRemoteIp).withUserName(testUserName).withProxyUserName(testProxyUserName).build();
        actualAuditMessage = evnt.getAuditMessage();
        expectedAuditMessage = java.lang.String.format("User(%s), RemoteIp(%s), ProxyUser(%s), Operation(Logout), Status(Success)", testUserName, testRemoteIp, testProxyUserName);
        org.junit.Assert.assertThat(actualAuditMessage, org.hamcrest.core.IsEqual.equalTo(expectedAuditMessage));
    }

    @org.junit.Test
    public void testTimestamp() throws java.lang.Exception {
        long testTimestamp = java.lang.System.currentTimeMillis();
        org.apache.ambari.server.audit.event.LogoutAuditEvent evnt = org.apache.ambari.server.audit.event.LogoutAuditEvent.builder().withTimestamp(testTimestamp).build();
        long actualTimestamp = evnt.getTimestamp();
        org.junit.Assert.assertThat(actualTimestamp, org.hamcrest.core.IsEqual.equalTo(testTimestamp));
    }

    @org.junit.Test
    public void testEquals() throws java.lang.Exception {
        nl.jqno.equalsverifier.EqualsVerifier.forClass(org.apache.ambari.server.audit.event.LogoutAuditEvent.class).verify();
    }
}