package org.apache.ambari.server.audit;
public class TaskStatusAuditEventTest {
    @org.junit.Test
    public void testAuditMessage() throws java.lang.Exception {
        java.lang.String testUserName = "USER1";
        java.lang.String testRemoteIp = "127.0.0.1";
        java.lang.String testOperation = "START MYCOMPONENT";
        java.lang.String testRequestDetails = "Start MyComponent";
        java.lang.String testHostName = "ambari.example.com";
        org.apache.ambari.server.actionmanager.HostRoleStatus testStatus = org.apache.ambari.server.actionmanager.HostRoleStatus.IN_PROGRESS;
        java.lang.Long testRequestId = 100L;
        java.lang.Long testTaskId = 99L;
        org.apache.ambari.server.audit.event.TaskStatusAuditEvent event = org.apache.ambari.server.audit.event.TaskStatusAuditEvent.builder().withTimestamp(java.lang.System.currentTimeMillis()).withUserName(testUserName).withRemoteIp(testRemoteIp).withOperation(testOperation).withRequestId(testRequestId.toString()).withDetails(testRequestDetails).withHostName(testHostName).withStatus(testStatus.name()).withTaskId(testTaskId.toString()).build();
        java.lang.String actualAuditMessage = event.getAuditMessage();
        java.lang.String expectedAuditMessage = java.lang.String.format("User(%s), RemoteIp(%s), Operation(%s), Details(%s), Status(%s), RequestId(%d), TaskId(%d), Hostname(%s)", testUserName, testRemoteIp, testOperation, testRequestDetails, testStatus, testRequestId, testTaskId, testHostName);
        org.junit.Assert.assertThat(actualAuditMessage, org.hamcrest.core.IsEqual.equalTo(expectedAuditMessage));
    }
}