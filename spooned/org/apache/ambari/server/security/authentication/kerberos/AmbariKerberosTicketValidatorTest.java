package org.apache.ambari.server.security.authentication.kerberos;
import org.easymock.EasyMockSupport;
import static org.easymock.EasyMock.expect;
public class AmbariKerberosTicketValidatorTest extends org.easymock.EasyMockSupport {
    @org.junit.Test
    public void testConstructor() throws java.lang.NoSuchMethodException {
        org.apache.ambari.server.security.authentication.kerberos.AmbariKerberosAuthenticationProperties properties = createMock(org.apache.ambari.server.security.authentication.kerberos.AmbariKerberosAuthenticationProperties.class);
        EasyMock.expect(properties.isKerberosAuthenticationEnabled()).andReturn(true).once();
        EasyMock.expect(properties.getSpnegoPrincipalName()).andReturn("HTTP/somehost.example.com").times(1);
        EasyMock.expect(properties.getSpnegoKeytabFilePath()).andReturn("/etc/security/keytabs/spnego.service.keytab").times(2);
        org.apache.ambari.server.configuration.Configuration configuration = createMock(org.apache.ambari.server.configuration.Configuration.class);
        EasyMock.expect(configuration.getKerberosAuthenticationProperties()).andReturn(properties).once();
        replayAll();
        new org.apache.ambari.server.security.authentication.kerberos.AmbariKerberosTicketValidator(configuration);
        verifyAll();
    }
}