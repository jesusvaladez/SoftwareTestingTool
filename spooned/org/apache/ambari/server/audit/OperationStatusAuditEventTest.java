package org.apache.ambari.server.audit;
import nl.jqno.equalsverifier.EqualsVerifier;
public class OperationStatusAuditEventTest {
    @org.junit.Test
    public void testAuditMessage() throws java.lang.Exception {
        java.lang.Long testRequestId = 100L;
        java.lang.String testStatus = "IN PROGRESS";
        java.lang.String testRemoteIp = "127.0.0.1";
        org.apache.ambari.server.audit.event.OperationStatusAuditEvent evnt = org.apache.ambari.server.audit.event.OperationStatusAuditEvent.builder().withTimestamp(java.lang.System.currentTimeMillis()).withRequestId(testRequestId.toString()).withStatus(testStatus).withRemoteIp(testRemoteIp).withUserName("testuser").withRequestContext("Start Service").build();
        java.lang.String actualAuditMessage = evnt.getAuditMessage();
        java.lang.String expectedAuditMessage = java.lang.String.format("User(testuser), RemoteIp(127.0.0.1), Operation(Start Service), Status(%s), RequestId(%s)", testStatus, testRequestId);
        org.junit.Assert.assertThat(actualAuditMessage, org.hamcrest.core.IsEqual.equalTo(expectedAuditMessage));
    }

    @org.junit.Test
    public void testTimestamp() throws java.lang.Exception {
        long testTimestamp = java.lang.System.currentTimeMillis();
        org.apache.ambari.server.audit.event.OperationStatusAuditEvent evnt = org.apache.ambari.server.audit.event.OperationStatusAuditEvent.builder().withTimestamp(testTimestamp).build();
        long actualTimestamp = evnt.getTimestamp();
        org.junit.Assert.assertThat(actualTimestamp, org.hamcrest.core.IsEqual.equalTo(testTimestamp));
    }

    @org.junit.Test
    public void testEquals() throws java.lang.Exception {
        nl.jqno.equalsverifier.EqualsVerifier.forClass(org.apache.ambari.server.audit.event.OperationStatusAuditEvent.class).verify();
    }
}