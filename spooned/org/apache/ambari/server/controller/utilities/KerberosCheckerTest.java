package org.apache.ambari.server.controller.utilities;
import org.easymock.EasyMockSupport;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
public class KerberosCheckerTest extends org.easymock.EasyMockSupport {
    @org.junit.Test
    public void testCheckPassed() throws java.lang.Exception {
        org.apache.ambari.server.configuration.Configuration config = createMock(org.apache.ambari.server.configuration.Configuration.class);
        org.apache.ambari.server.controller.utilities.LoginContextHelper loginContextHelper = createMock(org.apache.ambari.server.controller.utilities.LoginContextHelper.class);
        javax.security.auth.login.LoginContext lc = createMock(javax.security.auth.login.LoginContext.class);
        EasyMock.expect(config.isKerberosJaasConfigurationCheckEnabled()).andReturn(true).once();
        EasyMock.expect(loginContextHelper.createLoginContext(org.apache.ambari.server.controller.utilities.KerberosChecker.HTTP_SPNEGO_STANDARD_ENTRY)).andReturn(lc).once();
        lc.login();
        EasyMock.expectLastCall().once();
        lc.logout();
        EasyMock.expectLastCall().once();
        replayAll();
        org.apache.ambari.server.controller.utilities.KerberosChecker.config = config;
        org.apache.ambari.server.controller.utilities.KerberosChecker.loginContextHelper = loginContextHelper;
        org.apache.ambari.server.controller.utilities.KerberosChecker.checkJaasConfiguration();
        verifyAll();
    }

    @org.junit.Test(expected = org.apache.ambari.server.AmbariException.class)
    public void testCheckFailed() throws java.lang.Exception {
        org.apache.ambari.server.configuration.Configuration config = createMock(org.apache.ambari.server.configuration.Configuration.class);
        org.apache.ambari.server.controller.utilities.LoginContextHelper loginContextHelper = createMock(org.apache.ambari.server.controller.utilities.LoginContextHelper.class);
        EasyMock.expect(config.isKerberosJaasConfigurationCheckEnabled()).andReturn(true).once();
        EasyMock.expect(loginContextHelper.createLoginContext(org.apache.ambari.server.controller.utilities.KerberosChecker.HTTP_SPNEGO_STANDARD_ENTRY)).andThrow(new javax.security.auth.login.LoginException()).once();
        replayAll();
        org.apache.ambari.server.controller.utilities.KerberosChecker.config = config;
        org.apache.ambari.server.controller.utilities.KerberosChecker.loginContextHelper = loginContextHelper;
        org.apache.ambari.server.controller.utilities.KerberosChecker.checkJaasConfiguration();
        verifyAll();
    }
}