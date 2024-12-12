package org.apache.ambari.server.audit;
import nl.jqno.equalsverifier.EqualsVerifier;
public class StartOperationRequestAuditEventTest {
    @org.junit.Test
    public void testAuditMessage() throws java.lang.Exception {
        java.lang.String testUserName = "USER1";
        java.lang.String testRemoteIp = "127.0.0.1";
        java.lang.String testRequestDetails = "{ \"key\": \"value\"}";
        java.lang.Long testRequestId = 100L;
        java.lang.String testProxyUserName = "PROXYUSER1";
        org.apache.ambari.server.audit.event.request.StartOperationRequestAuditEvent evnt = org.apache.ambari.server.audit.event.request.StartOperationRequestAuditEvent.builder().withTimestamp(java.lang.System.currentTimeMillis()).withRemoteIp(testRemoteIp).withUserName(testUserName).withProxyUserName(null).withOperation(testRequestDetails).withRequestId(testRequestId.toString()).build();
        java.lang.String actualAuditMessage = evnt.getAuditMessage();
        java.lang.String expectedAuditMessage = java.lang.String.format("User(%s), RemoteIp(%s), Operation(%s), RequestId(%d), Status(Successfully queued)", testUserName, testRemoteIp, testRequestDetails, testRequestId);
        org.junit.Assert.assertThat(actualAuditMessage, org.hamcrest.core.IsEqual.equalTo(expectedAuditMessage));
        evnt = org.apache.ambari.server.audit.event.request.StartOperationRequestAuditEvent.builder().withTimestamp(java.lang.System.currentTimeMillis()).withRemoteIp(testRemoteIp).withUserName(testUserName).withProxyUserName(testProxyUserName).withOperation(testRequestDetails).withRequestId(testRequestId.toString()).build();
        actualAuditMessage = evnt.getAuditMessage();
        expectedAuditMessage = java.lang.String.format("User(%s), RemoteIp(%s), ProxyUser(%s), Operation(%s), RequestId(%d), Status(Successfully queued)", testUserName, testRemoteIp, testProxyUserName, testRequestDetails, testRequestId);
        org.junit.Assert.assertThat(actualAuditMessage, org.hamcrest.core.IsEqual.equalTo(expectedAuditMessage));
    }

    @org.junit.Test
    public void testTimestamp() throws java.lang.Exception {
        long testTimestamp = java.lang.System.currentTimeMillis();
        org.apache.ambari.server.audit.event.request.StartOperationRequestAuditEvent evnt = org.apache.ambari.server.audit.event.request.StartOperationRequestAuditEvent.builder().withTimestamp(testTimestamp).build();
        long actualTimestamp = evnt.getTimestamp();
        org.junit.Assert.assertThat(actualTimestamp, org.hamcrest.core.IsEqual.equalTo(testTimestamp));
    }

    @org.junit.Test
    public void testEquals() throws java.lang.Exception {
        nl.jqno.equalsverifier.EqualsVerifier.forClass(org.apache.ambari.server.audit.event.LoginAuditEvent.class).verify();
    }
}