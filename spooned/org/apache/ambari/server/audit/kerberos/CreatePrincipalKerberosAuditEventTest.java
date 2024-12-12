package org.apache.ambari.server.audit.kerberos;
public class CreatePrincipalKerberosAuditEventTest {
    @org.junit.Test
    public void testAuditMessage() throws java.lang.Exception {
        java.lang.String testOperation = "Create principal";
        java.lang.String testPrincipal = "testPrincipal";
        java.lang.Long testRequestId = 100L;
        java.lang.Long testTaskId = 99L;
        org.apache.ambari.server.audit.event.kerberos.CreatePrincipalKerberosAuditEvent event = org.apache.ambari.server.audit.event.kerberos.CreatePrincipalKerberosAuditEvent.builder().withTimestamp(java.lang.System.currentTimeMillis()).withOperation(testOperation).withRequestId(testRequestId).withPrincipal(testPrincipal).withTaskId(testTaskId).build();
        java.lang.String actualAuditMessage = event.getAuditMessage();
        java.lang.String expectedAuditMessage = java.lang.String.format("Operation(%s), Status(Success), RequestId(%d), TaskId(%d), Principal(%s)", testOperation, testRequestId, testTaskId, testPrincipal);
        org.junit.Assert.assertThat(actualAuditMessage, org.hamcrest.core.IsEqual.equalTo(expectedAuditMessage));
    }
}