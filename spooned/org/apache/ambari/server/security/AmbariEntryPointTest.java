package org.apache.ambari.server.security;
import org.easymock.EasyMockSupport;
import org.springframework.security.core.AuthenticationException;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
public class AmbariEntryPointTest extends org.easymock.EasyMockSupport {
    @org.junit.Rule
    public org.junit.rules.TemporaryFolder temporaryFolder = new org.junit.rules.TemporaryFolder();

    @org.junit.Test
    public void testCommenceDefault() throws java.lang.Exception {
        testCommence(null);
    }

    @org.junit.Test
    public void testCommenceKerberosAuthenticationEnabled() throws java.lang.Exception {
        testCommence(java.lang.Boolean.TRUE);
    }

    @org.junit.Test
    public void testCommenceKerberosAuthenticationNotEnabled() throws java.lang.Exception {
        testCommence(java.lang.Boolean.FALSE);
    }

    private void testCommence(java.lang.Boolean kerberosAuthenticationEnabled) throws java.io.IOException, javax.servlet.ServletException {
        javax.servlet.http.HttpServletRequest request = createStrictMock(javax.servlet.http.HttpServletRequest.class);
        javax.servlet.http.HttpServletResponse response = createStrictMock(javax.servlet.http.HttpServletResponse.class);
        org.springframework.security.core.AuthenticationException exception = createStrictMock(org.springframework.security.core.AuthenticationException.class);
        if (java.lang.Boolean.TRUE == kerberosAuthenticationEnabled) {
            response.setHeader("WWW-Authenticate", "Negotiate");
            EasyMock.expectLastCall().once();
            response.sendError(javax.servlet.http.HttpServletResponse.SC_UNAUTHORIZED, "Authentication requested");
            EasyMock.expectLastCall().once();
        } else {
            EasyMock.expect(exception.getMessage()).andReturn("message").once();
            response.sendError(javax.servlet.http.HttpServletResponse.SC_FORBIDDEN, "message");
            EasyMock.expectLastCall().once();
        }
        replayAll();
        java.util.Properties properties = new java.util.Properties();
        if (kerberosAuthenticationEnabled != null) {
            properties.setProperty(org.apache.ambari.server.configuration.Configuration.KERBEROS_AUTH_ENABLED.getKey(), kerberosAuthenticationEnabled.toString());
            properties.setProperty(org.apache.ambari.server.configuration.Configuration.KERBEROS_AUTH_SPNEGO_KEYTAB_FILE.getKey(), temporaryFolder.newFile().getAbsolutePath());
        }
        org.apache.ambari.server.security.AmbariEntryPoint entryPoint = new org.apache.ambari.server.security.AmbariEntryPoint(new org.apache.ambari.server.configuration.Configuration(properties));
        entryPoint.commence(request, response, exception);
        verifyAll();
    }
}