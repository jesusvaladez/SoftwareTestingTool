package org.apache.ambari.server.security.authentication.kerberos;
public class AmbariKerberosAuthenticationPropertiesTest {
    @org.junit.Test
    public void testKerberosAuthenticationEnabled() throws java.lang.Exception {
        org.apache.ambari.server.security.authentication.kerberos.AmbariKerberosAuthenticationProperties properties = new org.apache.ambari.server.security.authentication.kerberos.AmbariKerberosAuthenticationProperties();
        properties.setKerberosAuthenticationEnabled(true);
        org.junit.Assert.assertEquals(true, properties.isKerberosAuthenticationEnabled());
        properties.setKerberosAuthenticationEnabled(false);
        org.junit.Assert.assertEquals(false, properties.isKerberosAuthenticationEnabled());
    }

    @org.junit.Test
    public void testSpnegoPrincipalName() throws java.lang.Exception {
        org.apache.ambari.server.security.authentication.kerberos.AmbariKerberosAuthenticationProperties properties = new org.apache.ambari.server.security.authentication.kerberos.AmbariKerberosAuthenticationProperties();
        properties.setSpnegoPrincipalName("HTTP/_HOST@EXAMPLE.COM");
        org.junit.Assert.assertEquals("HTTP/_HOST@EXAMPLE.COM", properties.getSpnegoPrincipalName());
        properties.setSpnegoPrincipalName("something else");
        org.junit.Assert.assertEquals("something else", properties.getSpnegoPrincipalName());
    }

    @org.junit.Test
    public void testSpnegoKeytabFilePath() throws java.lang.Exception {
        org.apache.ambari.server.security.authentication.kerberos.AmbariKerberosAuthenticationProperties properties = new org.apache.ambari.server.security.authentication.kerberos.AmbariKerberosAuthenticationProperties();
        properties.setSpnegoKeytabFilePath("/etc/security/keytabs/spnego.service.keytab");
        org.junit.Assert.assertEquals("/etc/security/keytabs/spnego.service.keytab", properties.getSpnegoKeytabFilePath());
        properties.setSpnegoKeytabFilePath("something else");
        org.junit.Assert.assertEquals("something else", properties.getSpnegoKeytabFilePath());
    }

    @org.junit.Test
    public void testAuthToLocalRules() throws java.lang.Exception {
        org.apache.ambari.server.security.authentication.kerberos.AmbariKerberosAuthenticationProperties properties = new org.apache.ambari.server.security.authentication.kerberos.AmbariKerberosAuthenticationProperties();
        properties.setAuthToLocalRules("RULE:[1:$1@$0](user1@EXAMPLE.COM)s/.*/user2/\\nDEFAULT");
        org.junit.Assert.assertEquals("RULE:[1:$1@$0](user1@EXAMPLE.COM)s/.*/user2/\\nDEFAULT", properties.getAuthToLocalRules());
        properties.setAuthToLocalRules("something else");
        org.junit.Assert.assertEquals("something else", properties.getAuthToLocalRules());
    }
}