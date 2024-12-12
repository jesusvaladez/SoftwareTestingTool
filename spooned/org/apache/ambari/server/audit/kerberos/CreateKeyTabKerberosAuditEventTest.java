package org.apache.ambari.server.audit.kerberos;
public class CreateKeyTabKerberosAuditEventTest {
    @org.junit.Test
    public void testAuditMessage() throws java.lang.Exception {
        java.lang.String testOperation = "Create keytab file";
        java.lang.String testPrincipal = "testPrincipal";
        java.lang.String testHostName = "testhost.example.com";
        java.lang.String testKeyTabFile = "/tmp/mykeytabfile.ktf";
        java.lang.Long testRequestId = 100L;
        java.lang.Long testTaskId = 99L;
        org.apache.ambari.server.audit.event.kerberos.CreateKeyTabKerberosAuditEvent event = org.apache.ambari.server.audit.event.kerberos.CreateKeyTabKerberosAuditEvent.builder().withTimestamp(java.lang.System.currentTimeMillis()).withOperation(testOperation).withRequestId(testRequestId).withPrincipal(testPrincipal).withTaskId(testTaskId).withHostName(testHostName).withKeyTabFilePath(testKeyTabFile).build();
        java.lang.String actualAuditMessage = event.getAuditMessage();
        java.lang.String expectedAuditMessage = java.lang.String.format("Operation(%s), Status(Success), RequestId(%s), TaskId(%s), Principal(%s), Hostname(%s), Keytab file(%s)", testOperation, testRequestId, testTaskId, testPrincipal, testHostName, testKeyTabFile);
        org.junit.Assert.assertThat(actualAuditMessage, org.hamcrest.core.IsEqual.equalTo(expectedAuditMessage));
    }
}