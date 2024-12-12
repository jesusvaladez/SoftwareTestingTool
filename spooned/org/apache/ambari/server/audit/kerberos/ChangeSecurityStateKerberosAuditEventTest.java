package org.apache.ambari.server.audit.kerberos;
public class ChangeSecurityStateKerberosAuditEventTest {
    @org.junit.Test
    public void testAuditMessage() throws java.lang.Exception {
        java.lang.String testOperation = "Change security state";
        java.lang.String testHostName = "testhost.example.com";
        java.lang.String testService = "MyService";
        java.lang.String testComponent = "MyComponent";
        java.lang.String testState = "MyState";
        java.lang.Long testRequestId = 100L;
        java.lang.Long testTaskId = 99L;
        org.apache.ambari.server.audit.event.kerberos.ChangeSecurityStateKerberosAuditEvent event = org.apache.ambari.server.audit.event.kerberos.ChangeSecurityStateKerberosAuditEvent.builder().withTimestamp(java.lang.System.currentTimeMillis()).withOperation(testOperation).withRequestId(testRequestId).withTaskId(testTaskId).withHostName(testHostName).withComponent(testComponent).withService(testService).withState(testState).build();
        java.lang.String actualAuditMessage = event.getAuditMessage();
        java.lang.String expectedAuditMessage = java.lang.String.format("Operation(%s), Status(Success), RequestId(%s), TaskId(%s), Hostname(%s), Service(%s), Component(%s), State(%s)", testOperation, testRequestId, testTaskId, testHostName, testService, testComponent, testState);
        org.junit.Assert.assertThat(actualAuditMessage, org.hamcrest.core.IsEqual.equalTo(expectedAuditMessage));
    }
}