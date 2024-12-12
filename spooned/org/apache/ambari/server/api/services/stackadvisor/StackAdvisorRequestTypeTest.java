package org.apache.ambari.server.api.services.stackadvisor;
public class StackAdvisorRequestTypeTest {
    @org.junit.Test
    public void testFromString_returnsHostGroupType() throws org.apache.ambari.server.api.services.stackadvisor.StackAdvisorException {
        testFromString("host_groups", org.apache.ambari.server.api.services.stackadvisor.StackAdvisorRequest.StackAdvisorRequestType.HOST_GROUPS);
    }

    @org.junit.Test
    public void testFromString_returnsConfigurationsType() throws org.apache.ambari.server.api.services.stackadvisor.StackAdvisorException {
        testFromString("configurations", org.apache.ambari.server.api.services.stackadvisor.StackAdvisorRequest.StackAdvisorRequestType.CONFIGURATIONS);
    }

    @org.junit.Test
    public void testFromString_returnsSingleSignOnConfigurationsType() throws org.apache.ambari.server.api.services.stackadvisor.StackAdvisorException {
        testFromString("sso-configurations", org.apache.ambari.server.api.services.stackadvisor.StackAdvisorRequest.StackAdvisorRequestType.SSO_CONFIGURATIONS);
    }

    @org.junit.Test
    public void testFromString_returnsLDAPConfigurationsType() throws org.apache.ambari.server.api.services.stackadvisor.StackAdvisorException {
        testFromString("ldap-configurations", org.apache.ambari.server.api.services.stackadvisor.StackAdvisorRequest.StackAdvisorRequestType.LDAP_CONFIGURATIONS);
    }

    @org.junit.Test
    public void testFromString_returnsKerberosConfigurationsType() throws org.apache.ambari.server.api.services.stackadvisor.StackAdvisorException {
        testFromString("kerberos-configurations", org.apache.ambari.server.api.services.stackadvisor.StackAdvisorRequest.StackAdvisorRequestType.KERBEROS_CONFIGURATIONS);
    }

    @org.junit.Test(expected = org.apache.ambari.server.api.services.stackadvisor.StackAdvisorException.class)
    public void testFromString_throwsException() throws org.apache.ambari.server.api.services.stackadvisor.StackAdvisorException {
        java.lang.String text = "unknown_type";
        org.apache.ambari.server.api.services.stackadvisor.StackAdvisorRequest.StackAdvisorRequestType.fromString(text);
        org.junit.Assert.fail("Expected StackAdvisorException");
    }

    private void testFromString(java.lang.String text, org.apache.ambari.server.api.services.stackadvisor.StackAdvisorRequest.StackAdvisorRequestType expectedType) throws org.apache.ambari.server.api.services.stackadvisor.StackAdvisorException {
        org.junit.Assert.assertEquals(expectedType, org.apache.ambari.server.api.services.stackadvisor.StackAdvisorRequest.StackAdvisorRequestType.fromString(text));
    }
}