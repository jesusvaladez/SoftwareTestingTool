package org.apache.ambari.server.audit;
import nl.jqno.equalsverifier.EqualsVerifier;
public class LoginAuditEventTest {
    @org.junit.Test
    public void testAuditMessage() throws java.lang.Exception {
        java.lang.String testUserName = "USER1";
        java.lang.String testRemoteIp = "127.0.0.1";
        java.lang.String testProxyUserName = "PROXYUSER1";
        java.util.Map<java.lang.String, java.util.List<java.lang.String>> roles = new java.util.HashMap<>();
        roles.put("a", java.util.Arrays.asList("r1", "r2", "r3"));
        org.apache.ambari.server.audit.event.LoginAuditEvent evnt = org.apache.ambari.server.audit.event.LoginAuditEvent.builder().withTimestamp(java.lang.System.currentTimeMillis()).withRemoteIp(testRemoteIp).withUserName(testUserName).withProxyUserName(null).withRoles(roles).build();
        java.lang.String actualAuditMessage = evnt.getAuditMessage();
        java.lang.String roleMessage = "a: r1, r2, r3";
        java.lang.String expectedAuditMessage = java.lang.String.format("User(%s), RemoteIp(%s), Operation(User login), Roles(%s), Status(Success)", testUserName, testRemoteIp, roleMessage);
        org.junit.Assert.assertThat(actualAuditMessage, org.hamcrest.core.IsEqual.equalTo(expectedAuditMessage));
        evnt = org.apache.ambari.server.audit.event.LoginAuditEvent.builder().withTimestamp(java.lang.System.currentTimeMillis()).withRemoteIp(testRemoteIp).withUserName(testUserName).withProxyUserName(testProxyUserName).withRoles(roles).build();
        actualAuditMessage = evnt.getAuditMessage();
        roleMessage = "a: r1, r2, r3";
        expectedAuditMessage = java.lang.String.format("User(%s), RemoteIp(%s), ProxyUser(%s), Operation(User login), Roles(%s), Status(Success)", testUserName, testRemoteIp, testProxyUserName, roleMessage);
        org.junit.Assert.assertThat(actualAuditMessage, org.hamcrest.core.IsEqual.equalTo(expectedAuditMessage));
    }

    @org.junit.Test
    public void testFailedAuditMessage() throws java.lang.Exception {
        java.lang.String testUserName = "USER1";
        java.lang.String testRemoteIp = "127.0.0.1";
        java.lang.String testProxyUserName = "PROXYUSER1";
        java.lang.String reason = "Bad credentials";
        java.lang.Integer consecutiveFailures = 1;
        java.util.Map<java.lang.String, java.util.List<java.lang.String>> roles = new java.util.HashMap<>();
        roles.put("a", java.util.Arrays.asList("r1", "r2", "r3"));
        org.apache.ambari.server.audit.event.LoginAuditEvent evnt = org.apache.ambari.server.audit.event.LoginAuditEvent.builder().withTimestamp(java.lang.System.currentTimeMillis()).withRemoteIp(testRemoteIp).withUserName(testUserName).withProxyUserName(null).withRoles(roles).withReasonOfFailure(reason).withConsecutiveFailures(consecutiveFailures).build();
        java.lang.String actualAuditMessage = evnt.getAuditMessage();
        java.lang.String roleMessage = "a: r1, r2, r3";
        java.lang.String expectedAuditMessage = java.lang.String.format("User(%s), RemoteIp(%s), Operation(User login), Roles(%s), Status(Failed), Reason(%s), Consecutive failures(%d)", testUserName, testRemoteIp, roleMessage, reason, consecutiveFailures);
        org.junit.Assert.assertThat(actualAuditMessage, org.hamcrest.core.IsEqual.equalTo(expectedAuditMessage));
        evnt = org.apache.ambari.server.audit.event.LoginAuditEvent.builder().withTimestamp(java.lang.System.currentTimeMillis()).withRemoteIp(testRemoteIp).withUserName(testUserName).withProxyUserName(testProxyUserName).withRoles(roles).withReasonOfFailure(reason).withConsecutiveFailures(consecutiveFailures).build();
        actualAuditMessage = evnt.getAuditMessage();
        roleMessage = "a: r1, r2, r3";
        expectedAuditMessage = java.lang.String.format("User(%s), RemoteIp(%s), ProxyUser(%s), Operation(User login), Roles(%s), Status(Failed), Reason(%s), Consecutive failures(%d)", testUserName, testRemoteIp, testProxyUserName, roleMessage, reason, consecutiveFailures);
        org.junit.Assert.assertThat(actualAuditMessage, org.hamcrest.core.IsEqual.equalTo(expectedAuditMessage));
    }

    @org.junit.Test
    public void testFailedAuditMessageUnknownUser() throws java.lang.Exception {
        java.lang.String testUserName = "USER1";
        java.lang.String testRemoteIp = "127.0.0.1";
        java.lang.String reason = "Bad credentials";
        java.lang.String testProxyUserName = "PROXYUSER1";
        java.util.Map<java.lang.String, java.util.List<java.lang.String>> roles = new java.util.HashMap<>();
        roles.put("a", java.util.Arrays.asList("r1", "r2", "r3"));
        org.apache.ambari.server.audit.event.LoginAuditEvent evnt = org.apache.ambari.server.audit.event.LoginAuditEvent.builder().withTimestamp(java.lang.System.currentTimeMillis()).withRemoteIp(testRemoteIp).withUserName(testUserName).withProxyUserName(null).withRoles(roles).withReasonOfFailure(reason).withConsecutiveFailures(null).build();
        java.lang.String actualAuditMessage = evnt.getAuditMessage();
        java.lang.String roleMessage = "a: r1, r2, r3";
        java.lang.String expectedAuditMessage = java.lang.String.format("User(%s), RemoteIp(%s), Operation(User login), Roles(%s), Status(Failed), Reason(%s), Consecutive failures(UNKNOWN USER)", testUserName, testRemoteIp, roleMessage, reason);
        org.junit.Assert.assertThat(actualAuditMessage, org.hamcrest.core.IsEqual.equalTo(expectedAuditMessage));
        evnt = org.apache.ambari.server.audit.event.LoginAuditEvent.builder().withTimestamp(java.lang.System.currentTimeMillis()).withRemoteIp(testRemoteIp).withUserName(testUserName).withProxyUserName(testProxyUserName).withRoles(roles).withReasonOfFailure(reason).withConsecutiveFailures(null).build();
        actualAuditMessage = evnt.getAuditMessage();
        roleMessage = "a: r1, r2, r3";
        expectedAuditMessage = java.lang.String.format("User(%s), RemoteIp(%s), ProxyUser(%s), Operation(User login), Roles(%s), Status(Failed), Reason(%s), Consecutive failures(UNKNOWN USER)", testUserName, testRemoteIp, testProxyUserName, roleMessage, reason);
        org.junit.Assert.assertThat(actualAuditMessage, org.hamcrest.core.IsEqual.equalTo(expectedAuditMessage));
    }

    @org.junit.Test
    public void testTimestamp() throws java.lang.Exception {
        long testTimestamp = java.lang.System.currentTimeMillis();
        org.apache.ambari.server.audit.event.LoginAuditEvent evnt = org.apache.ambari.server.audit.event.LoginAuditEvent.builder().withTimestamp(testTimestamp).build();
        long actualTimestamp = evnt.getTimestamp();
        org.junit.Assert.assertThat(actualTimestamp, org.hamcrest.core.IsEqual.equalTo(testTimestamp));
    }

    @org.junit.Test
    public void testEquals() throws java.lang.Exception {
        nl.jqno.equalsverifier.EqualsVerifier.forClass(org.apache.ambari.server.audit.event.LoginAuditEvent.class).verify();
    }
}