package org.apache.ambari.server.audit;
import nl.jqno.equalsverifier.EqualsVerifier;
public class AccessUnauthorizedAuditEventTest {
    @org.junit.Test
    public void testAuditMessage() throws java.lang.Exception {
        java.lang.String testUserName = "USER1";
        java.lang.String testProxyUserName = "PROXYUSER1";
        java.lang.String testRemoteIp = "127.0.0.1";
        java.lang.String testHttpMethod = "GET";
        java.lang.String testResourcePath = "/api/v1/hosts";
        org.apache.ambari.server.audit.event.AccessUnauthorizedAuditEvent evnt = org.apache.ambari.server.audit.event.AccessUnauthorizedAuditEvent.builder().withTimestamp(java.lang.System.currentTimeMillis()).withRemoteIp(testRemoteIp).withUserName(testUserName).withProxyUserName(null).withHttpMethodName(testHttpMethod).withResourcePath(testResourcePath).build();
        java.lang.String actualAuditMessage = evnt.getAuditMessage();
        java.lang.String expectedAuditMessage = java.lang.String.format("User(%s), RemoteIp(%s), Operation(%s), ResourcePath(%s), Status(Failed), Reason(Access not authorized)", testUserName, testRemoteIp, testHttpMethod, testResourcePath);
        org.junit.Assert.assertThat(actualAuditMessage, org.hamcrest.core.IsEqual.equalTo(expectedAuditMessage));
        evnt = org.apache.ambari.server.audit.event.AccessUnauthorizedAuditEvent.builder().withTimestamp(java.lang.System.currentTimeMillis()).withRemoteIp(testRemoteIp).withUserName(testUserName).withProxyUserName(testProxyUserName).withHttpMethodName(testHttpMethod).withResourcePath(testResourcePath).build();
        actualAuditMessage = evnt.getAuditMessage();
        expectedAuditMessage = java.lang.String.format("User(%s), RemoteIp(%s), ProxyUser(PROXYUSER1), Operation(%s), ResourcePath(%s), Status(Failed), Reason(Access not authorized)", testUserName, testRemoteIp, testHttpMethod, testResourcePath);
        org.junit.Assert.assertThat(actualAuditMessage, org.hamcrest.core.IsEqual.equalTo(expectedAuditMessage));
    }

    @org.junit.Test
    public void testTimestamp() throws java.lang.Exception {
        long testTimestamp = java.lang.System.currentTimeMillis();
        org.apache.ambari.server.audit.event.AccessUnauthorizedAuditEvent evnt = org.apache.ambari.server.audit.event.AccessUnauthorizedAuditEvent.builder().withTimestamp(testTimestamp).build();
        long actualTimestamp = evnt.getTimestamp();
        org.junit.Assert.assertThat(actualTimestamp, org.hamcrest.core.IsEqual.equalTo(testTimestamp));
    }

    @org.junit.Test
    public void testEquals() throws java.lang.Exception {
        nl.jqno.equalsverifier.EqualsVerifier.forClass(org.apache.ambari.server.audit.event.AccessUnauthorizedAuditEvent.class).verify();
    }
}