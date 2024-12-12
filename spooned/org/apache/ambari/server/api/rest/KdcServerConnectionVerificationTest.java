package org.apache.ambari.server.api.rest;
import org.apache.directory.kerberos.client.KdcConfig;
import org.apache.directory.kerberos.client.KdcConnection;
import org.apache.directory.kerberos.client.TgTicket;
import org.apache.directory.shared.kerberos.KerberosMessageType;
import org.apache.directory.shared.kerberos.exceptions.ErrorType;
import org.apache.directory.shared.kerberos.exceptions.KerberosException;
import org.apache.directory.shared.kerberos.messages.KrbError;
import static org.apache.ambari.server.KdcServerConnectionVerification.ConnectionProtocol.TCP;
import static org.apache.ambari.server.KdcServerConnectionVerification.ConnectionProtocol.UDP;
import static org.easymock.EasyMock.createNiceMock;
import static org.easymock.EasyMock.createStrictMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
public class KdcServerConnectionVerificationTest {
    private org.apache.ambari.server.configuration.Configuration configuration;

    private static final int KDC_TEST_PORT = 8090;

    @org.junit.Before
    public void before() throws java.lang.Exception {
        java.util.Properties configProps = new java.util.Properties();
        configProps.setProperty(org.apache.ambari.server.configuration.Configuration.KDC_PORT.getKey(), java.lang.Integer.toString(org.apache.ambari.server.api.rest.KdcServerConnectionVerificationTest.KDC_TEST_PORT));
        configuration = new org.apache.ambari.server.configuration.Configuration(configProps);
    }

    @org.junit.Test
    public void testValidate__Fail_InvalidPort() throws java.lang.Exception {
        org.junit.Assert.assertFalse(new org.apache.ambari.server.KdcServerConnectionVerification(configuration).isKdcReachable("test-host:abcd"));
    }

    @org.junit.Test
    public void testValidate__Success() throws java.lang.Exception {
        org.apache.directory.kerberos.client.KdcConnection connection = EasyMock.createStrictMock(org.apache.directory.kerberos.client.KdcConnection.class);
        EasyMock.expect(connection.getTgt("noUser@noRealm", "noPassword")).andReturn(null).once();
        EasyMock.replay(connection);
        org.apache.ambari.server.api.rest.KdcServerConnectionVerificationTest.TestKdcServerConnectionVerification kdcConnVerifier = new org.apache.ambari.server.api.rest.KdcServerConnectionVerificationTest.TestKdcServerConnectionVerification(configuration, connection);
        boolean result = kdcConnVerifier.isKdcReachable("test-host:11111");
        org.junit.Assert.assertTrue(result);
        org.apache.directory.kerberos.client.KdcConfig kdcConfig = kdcConnVerifier.getConfigUsedInConnectionCreation();
        org.junit.Assert.assertEquals("test-host", kdcConfig.getHostName());
        org.junit.Assert.assertEquals(11111, kdcConfig.getKdcPort());
        org.junit.Assert.assertEquals(10 * 1000, kdcConfig.getTimeout());
        EasyMock.verify(connection);
    }

    @org.junit.Test
    public void testValidateTCP__Successful() throws java.lang.Exception {
        org.apache.directory.kerberos.client.KdcConnection connection = EasyMock.createStrictMock(org.apache.directory.kerberos.client.KdcConnection.class);
        EasyMock.expect(connection.getTgt("noUser@noRealm", "noPassword")).andReturn(null).once();
        EasyMock.replay(connection);
        org.apache.ambari.server.api.rest.KdcServerConnectionVerificationTest.TestKdcServerConnectionVerification kdcConnVerifier = new org.apache.ambari.server.api.rest.KdcServerConnectionVerificationTest.TestKdcServerConnectionVerification(configuration, connection);
        boolean result = kdcConnVerifier.isKdcReachable("test-host", 11111, org.apache.ambari.server.KdcServerConnectionVerification.ConnectionProtocol.TCP);
        org.junit.Assert.assertTrue(result);
        org.apache.directory.kerberos.client.KdcConfig kdcConfig = kdcConnVerifier.getConfigUsedInConnectionCreation();
        org.junit.Assert.assertFalse(kdcConfig.isUseUdp());
        org.junit.Assert.assertEquals("test-host", kdcConfig.getHostName());
        org.junit.Assert.assertEquals(11111, kdcConfig.getKdcPort());
        org.junit.Assert.assertEquals(10 * 1000, kdcConfig.getTimeout());
        EasyMock.verify(connection);
    }

    @org.junit.Test
    public void testValidateTCP__Successful2() throws java.lang.Exception {
        org.apache.directory.shared.kerberos.messages.KrbError error = EasyMock.createNiceMock(org.apache.directory.shared.kerberos.messages.KrbError.class);
        EasyMock.expect(error.getErrorCode()).andReturn(ErrorType.KDC_ERR_C_PRINCIPAL_UNKNOWN).once();
        EasyMock.expect(error.getMessageType()).andReturn(KerberosMessageType.KRB_ERROR).once();
        org.apache.directory.shared.kerberos.exceptions.KerberosException exception = EasyMock.createNiceMock(org.apache.directory.shared.kerberos.exceptions.KerberosException.class);
        EasyMock.expect(exception.getError()).andReturn(error).once();
        org.apache.directory.kerberos.client.KdcConnection connection = EasyMock.createStrictMock(org.apache.directory.kerberos.client.KdcConnection.class);
        EasyMock.expect(connection.getTgt("noUser@noRealm", "noPassword")).andThrow(exception);
        EasyMock.replay(connection, exception, error);
        org.apache.ambari.server.api.rest.KdcServerConnectionVerificationTest.TestKdcServerConnectionVerification kdcConnVerifier = new org.apache.ambari.server.api.rest.KdcServerConnectionVerificationTest.TestKdcServerConnectionVerification(configuration, connection);
        boolean result = kdcConnVerifier.isKdcReachable("test-host", 11111, org.apache.ambari.server.KdcServerConnectionVerification.ConnectionProtocol.TCP);
        org.junit.Assert.assertTrue(result);
        org.apache.directory.kerberos.client.KdcConfig kdcConfig = kdcConnVerifier.getConfigUsedInConnectionCreation();
        org.junit.Assert.assertFalse(kdcConfig.isUseUdp());
        org.junit.Assert.assertEquals("test-host", kdcConfig.getHostName());
        org.junit.Assert.assertEquals(11111, kdcConfig.getKdcPort());
        org.junit.Assert.assertEquals(10 * 1000, kdcConfig.getTimeout());
        EasyMock.verify(connection, exception);
    }

    @org.junit.Test
    public void testValidateTCP__Fail_UnknownException() throws java.lang.Exception {
        org.apache.directory.kerberos.client.KdcConnection connection = EasyMock.createStrictMock(org.apache.directory.kerberos.client.KdcConnection.class);
        EasyMock.expect(connection.getTgt("noUser@noRealm", "noPassword")).andThrow(new java.lang.RuntimeException("This is a really bad exception"));
        EasyMock.replay(connection);
        org.apache.ambari.server.api.rest.KdcServerConnectionVerificationTest.TestKdcServerConnectionVerification kdcConnVerifier = new org.apache.ambari.server.api.rest.KdcServerConnectionVerificationTest.TestKdcServerConnectionVerification(configuration, connection);
        boolean result = kdcConnVerifier.isKdcReachable("test-host", 11111, org.apache.ambari.server.KdcServerConnectionVerification.ConnectionProtocol.TCP);
        org.junit.Assert.assertFalse(result);
        org.apache.directory.kerberos.client.KdcConfig kdcConfig = kdcConnVerifier.getConfigUsedInConnectionCreation();
        org.junit.Assert.assertFalse(kdcConfig.isUseUdp());
        org.junit.Assert.assertEquals("test-host", kdcConfig.getHostName());
        org.junit.Assert.assertEquals(11111, kdcConfig.getKdcPort());
        org.junit.Assert.assertEquals(10 * 1000, kdcConfig.getTimeout());
        EasyMock.verify(connection);
    }

    @org.junit.Test
    public void testValidateTCP__Fail_Timeout() throws java.lang.Exception {
        int timeout = 1;
        org.apache.directory.kerberos.client.KdcConnection connection = new org.apache.ambari.server.api.rest.KdcServerConnectionVerificationTest.BlockingKdcConnection(null);
        org.apache.ambari.server.api.rest.KdcServerConnectionVerificationTest.TestKdcServerConnectionVerification kdcConnVerifier = new org.apache.ambari.server.api.rest.KdcServerConnectionVerificationTest.TestKdcServerConnectionVerification(configuration, connection);
        kdcConnVerifier.setConnectionTimeout(timeout);
        boolean result = kdcConnVerifier.isKdcReachable("test-host", 11111, org.apache.ambari.server.KdcServerConnectionVerification.ConnectionProtocol.TCP);
        org.junit.Assert.assertFalse(result);
        org.apache.directory.kerberos.client.KdcConfig kdcConfig = kdcConnVerifier.getConfigUsedInConnectionCreation();
        org.junit.Assert.assertFalse(kdcConfig.isUseUdp());
        org.junit.Assert.assertEquals("test-host", kdcConfig.getHostName());
        org.junit.Assert.assertEquals(11111, kdcConfig.getKdcPort());
        org.junit.Assert.assertEquals(timeout * 1000, kdcConfig.getTimeout());
    }

    @org.junit.Test
    public void testValidateTCP__Fail_TimeoutErrorCode() throws java.lang.Exception {
        org.apache.directory.kerberos.client.KdcConnection connection = EasyMock.createStrictMock(org.apache.directory.kerberos.client.KdcConnection.class);
        EasyMock.expect(connection.getTgt("noUser@noRealm", "noPassword")).andThrow(new org.apache.directory.shared.kerberos.exceptions.KerberosException(org.apache.directory.shared.kerberos.exceptions.ErrorType.KRB_ERR_GENERIC, "TimeOut occurred"));
        EasyMock.replay(connection);
        org.apache.ambari.server.api.rest.KdcServerConnectionVerificationTest.TestKdcServerConnectionVerification kdcConnVerifier = new org.apache.ambari.server.api.rest.KdcServerConnectionVerificationTest.TestKdcServerConnectionVerification(configuration, connection);
        boolean result = kdcConnVerifier.isKdcReachable("test-host", 11111, org.apache.ambari.server.KdcServerConnectionVerification.ConnectionProtocol.TCP);
        org.junit.Assert.assertFalse(result);
        org.apache.directory.kerberos.client.KdcConfig kdcConfig = kdcConnVerifier.getConfigUsedInConnectionCreation();
        org.junit.Assert.assertFalse(kdcConfig.isUseUdp());
        org.junit.Assert.assertEquals("test-host", kdcConfig.getHostName());
        org.junit.Assert.assertEquals(11111, kdcConfig.getKdcPort());
        org.junit.Assert.assertEquals(10 * 1000, kdcConfig.getTimeout());
        EasyMock.verify(connection);
    }

    @org.junit.Test
    public void testValidateTCP__Fail_GeneralErrorCode_NotTimeout() throws java.lang.Exception {
        org.apache.directory.shared.kerberos.messages.KrbError error = EasyMock.createNiceMock(org.apache.directory.shared.kerberos.messages.KrbError.class);
        EasyMock.expect(error.getErrorCode()).andReturn(ErrorType.KRB_ERR_GENERIC).once();
        EasyMock.expect(error.getMessageType()).andReturn(KerberosMessageType.KRB_ERROR).once();
        org.apache.directory.shared.kerberos.exceptions.KerberosException exception = EasyMock.createNiceMock(org.apache.directory.shared.kerberos.exceptions.KerberosException.class);
        EasyMock.expect(exception.getError()).andReturn(error).once();
        org.apache.directory.kerberos.client.KdcConnection connection = EasyMock.createStrictMock(org.apache.directory.kerberos.client.KdcConnection.class);
        EasyMock.expect(connection.getTgt("noUser@noRealm", "noPassword")).andThrow(exception);
        EasyMock.replay(connection, exception, error);
        org.apache.ambari.server.api.rest.KdcServerConnectionVerificationTest.TestKdcServerConnectionVerification kdcConnVerifier = new org.apache.ambari.server.api.rest.KdcServerConnectionVerificationTest.TestKdcServerConnectionVerification(configuration, connection);
        boolean result = kdcConnVerifier.isKdcReachable("test-host", 11111, org.apache.ambari.server.KdcServerConnectionVerification.ConnectionProtocol.TCP);
        org.junit.Assert.assertTrue(result);
        org.apache.directory.kerberos.client.KdcConfig kdcConfig = kdcConnVerifier.getConfigUsedInConnectionCreation();
        org.junit.Assert.assertFalse(kdcConfig.isUseUdp());
        org.junit.Assert.assertEquals("test-host", kdcConfig.getHostName());
        org.junit.Assert.assertEquals(11111, kdcConfig.getKdcPort());
        org.junit.Assert.assertEquals(10 * 1000, kdcConfig.getTimeout());
        EasyMock.verify(connection, exception);
    }

    @org.junit.Test
    public void testValidateUDP__Successful() throws java.lang.Exception {
        org.apache.directory.kerberos.client.KdcConnection connection = EasyMock.createStrictMock(org.apache.directory.kerberos.client.KdcConnection.class);
        EasyMock.expect(connection.getTgt("noUser@noRealm", "noPassword")).andReturn(null).once();
        EasyMock.replay(connection);
        org.apache.ambari.server.api.rest.KdcServerConnectionVerificationTest.TestKdcServerConnectionVerification kdcConnVerifier = new org.apache.ambari.server.api.rest.KdcServerConnectionVerificationTest.TestKdcServerConnectionVerification(configuration, connection);
        boolean result = kdcConnVerifier.isKdcReachable("test-host", 11111, org.apache.ambari.server.KdcServerConnectionVerification.ConnectionProtocol.UDP);
        org.junit.Assert.assertTrue(result);
        org.apache.directory.kerberos.client.KdcConfig kdcConfig = kdcConnVerifier.getConfigUsedInConnectionCreation();
        org.junit.Assert.assertTrue(kdcConfig.isUseUdp());
        org.junit.Assert.assertEquals("test-host", kdcConfig.getHostName());
        org.junit.Assert.assertEquals(11111, kdcConfig.getKdcPort());
        org.junit.Assert.assertEquals(10 * 1000, kdcConfig.getTimeout());
        EasyMock.verify(connection);
    }

    @org.junit.Test
    public void testValidateUDP__Successful2() throws java.lang.Exception {
        org.apache.directory.shared.kerberos.messages.KrbError error = EasyMock.createNiceMock(org.apache.directory.shared.kerberos.messages.KrbError.class);
        EasyMock.expect(error.getErrorCode()).andReturn(ErrorType.KDC_ERR_C_PRINCIPAL_UNKNOWN).once();
        EasyMock.expect(error.getMessageType()).andReturn(KerberosMessageType.KRB_ERROR).once();
        org.apache.directory.shared.kerberos.exceptions.KerberosException exception = EasyMock.createNiceMock(org.apache.directory.shared.kerberos.exceptions.KerberosException.class);
        EasyMock.expect(exception.getError()).andReturn(error).once();
        org.apache.directory.kerberos.client.KdcConnection connection = EasyMock.createStrictMock(org.apache.directory.kerberos.client.KdcConnection.class);
        EasyMock.expect(connection.getTgt("noUser@noRealm", "noPassword")).andThrow(exception);
        EasyMock.replay(connection, exception, error);
        org.apache.ambari.server.api.rest.KdcServerConnectionVerificationTest.TestKdcServerConnectionVerification kdcConnVerifier = new org.apache.ambari.server.api.rest.KdcServerConnectionVerificationTest.TestKdcServerConnectionVerification(configuration, connection);
        boolean result = kdcConnVerifier.isKdcReachable("test-host", 11111, org.apache.ambari.server.KdcServerConnectionVerification.ConnectionProtocol.UDP);
        org.junit.Assert.assertTrue(result);
        org.apache.directory.kerberos.client.KdcConfig kdcConfig = kdcConnVerifier.getConfigUsedInConnectionCreation();
        org.junit.Assert.assertTrue(kdcConfig.isUseUdp());
        org.junit.Assert.assertEquals("test-host", kdcConfig.getHostName());
        org.junit.Assert.assertEquals(11111, kdcConfig.getKdcPort());
        org.junit.Assert.assertEquals(10 * 1000, kdcConfig.getTimeout());
        EasyMock.verify(connection, exception);
    }

    @org.junit.Test
    public void testValidateUDP__Fail_UnknownException() throws java.lang.Exception {
        org.apache.directory.kerberos.client.KdcConnection connection = EasyMock.createStrictMock(org.apache.directory.kerberos.client.KdcConnection.class);
        EasyMock.expect(connection.getTgt("noUser@noRealm", "noPassword")).andThrow(new java.lang.RuntimeException("This is a really bad exception"));
        EasyMock.replay(connection);
        org.apache.ambari.server.api.rest.KdcServerConnectionVerificationTest.TestKdcServerConnectionVerification kdcConnVerifier = new org.apache.ambari.server.api.rest.KdcServerConnectionVerificationTest.TestKdcServerConnectionVerification(configuration, connection);
        boolean result = kdcConnVerifier.isKdcReachable("test-host", 11111, org.apache.ambari.server.KdcServerConnectionVerification.ConnectionProtocol.UDP);
        org.junit.Assert.assertFalse(result);
        org.apache.directory.kerberos.client.KdcConfig kdcConfig = kdcConnVerifier.getConfigUsedInConnectionCreation();
        org.junit.Assert.assertTrue(kdcConfig.isUseUdp());
        org.junit.Assert.assertEquals("test-host", kdcConfig.getHostName());
        org.junit.Assert.assertEquals(11111, kdcConfig.getKdcPort());
        org.junit.Assert.assertEquals(10 * 1000, kdcConfig.getTimeout());
        EasyMock.verify(connection);
    }

    @org.junit.Test
    public void testValidateUDP__Fail_Timeout() throws java.lang.Exception {
        int timeout = 1;
        org.apache.directory.kerberos.client.KdcConnection connection = new org.apache.ambari.server.api.rest.KdcServerConnectionVerificationTest.BlockingKdcConnection(null);
        org.apache.ambari.server.api.rest.KdcServerConnectionVerificationTest.TestKdcServerConnectionVerification kdcConnVerifier = new org.apache.ambari.server.api.rest.KdcServerConnectionVerificationTest.TestKdcServerConnectionVerification(configuration, connection);
        kdcConnVerifier.setConnectionTimeout(timeout);
        boolean result = kdcConnVerifier.isKdcReachable("test-host", 11111, org.apache.ambari.server.KdcServerConnectionVerification.ConnectionProtocol.UDP);
        org.junit.Assert.assertFalse(result);
        org.apache.directory.kerberos.client.KdcConfig kdcConfig = kdcConnVerifier.getConfigUsedInConnectionCreation();
        org.junit.Assert.assertTrue(kdcConfig.isUseUdp());
        org.junit.Assert.assertEquals("test-host", kdcConfig.getHostName());
        org.junit.Assert.assertEquals(11111, kdcConfig.getKdcPort());
        org.junit.Assert.assertEquals(timeout * 1000, kdcConfig.getTimeout());
    }

    @org.junit.Test
    public void testValidateUDP__Fail_TimeoutErrorCode() throws java.lang.Exception {
        org.apache.directory.kerberos.client.KdcConnection connection = EasyMock.createStrictMock(org.apache.directory.kerberos.client.KdcConnection.class);
        EasyMock.expect(connection.getTgt("noUser@noRealm", "noPassword")).andThrow(new org.apache.directory.shared.kerberos.exceptions.KerberosException(org.apache.directory.shared.kerberos.exceptions.ErrorType.KRB_ERR_GENERIC, "TimeOut occurred"));
        EasyMock.replay(connection);
        org.apache.ambari.server.api.rest.KdcServerConnectionVerificationTest.TestKdcServerConnectionVerification kdcConnVerifier = new org.apache.ambari.server.api.rest.KdcServerConnectionVerificationTest.TestKdcServerConnectionVerification(configuration, connection);
        boolean result = kdcConnVerifier.isKdcReachable("test-host", 11111, org.apache.ambari.server.KdcServerConnectionVerification.ConnectionProtocol.UDP);
        org.junit.Assert.assertFalse(result);
        org.apache.directory.kerberos.client.KdcConfig kdcConfig = kdcConnVerifier.getConfigUsedInConnectionCreation();
        org.junit.Assert.assertTrue(kdcConfig.isUseUdp());
        org.junit.Assert.assertEquals("test-host", kdcConfig.getHostName());
        org.junit.Assert.assertEquals(11111, kdcConfig.getKdcPort());
        org.junit.Assert.assertEquals(10 * 1000, kdcConfig.getTimeout());
        EasyMock.verify(connection);
    }

    @org.junit.Test
    public void testValidateUDP__Fail_GeneralErrorCode_NotTimeout() throws java.lang.Exception {
        org.apache.directory.shared.kerberos.messages.KrbError error = EasyMock.createNiceMock(org.apache.directory.shared.kerberos.messages.KrbError.class);
        EasyMock.expect(error.getErrorCode()).andReturn(ErrorType.KRB_ERR_GENERIC).once();
        EasyMock.expect(error.getMessageType()).andReturn(KerberosMessageType.KRB_ERROR).once();
        org.apache.directory.shared.kerberos.exceptions.KerberosException exception = EasyMock.createNiceMock(org.apache.directory.shared.kerberos.exceptions.KerberosException.class);
        EasyMock.expect(exception.getError()).andReturn(error).once();
        org.apache.directory.kerberos.client.KdcConnection connection = EasyMock.createStrictMock(org.apache.directory.kerberos.client.KdcConnection.class);
        EasyMock.expect(connection.getTgt("noUser@noRealm", "noPassword")).andThrow(exception);
        EasyMock.replay(connection, exception, error);
        org.apache.ambari.server.api.rest.KdcServerConnectionVerificationTest.TestKdcServerConnectionVerification kdcConnVerifier = new org.apache.ambari.server.api.rest.KdcServerConnectionVerificationTest.TestKdcServerConnectionVerification(configuration, connection);
        boolean result = kdcConnVerifier.isKdcReachable("test-host", 11111, org.apache.ambari.server.KdcServerConnectionVerification.ConnectionProtocol.UDP);
        org.junit.Assert.assertTrue(result);
        org.apache.directory.kerberos.client.KdcConfig kdcConfig = kdcConnVerifier.getConfigUsedInConnectionCreation();
        org.junit.Assert.assertTrue(kdcConfig.isUseUdp());
        org.junit.Assert.assertEquals("test-host", kdcConfig.getHostName());
        org.junit.Assert.assertEquals(11111, kdcConfig.getKdcPort());
        org.junit.Assert.assertEquals(10 * 1000, kdcConfig.getTimeout());
        EasyMock.verify(connection, exception);
    }

    @org.junit.Test
    @org.junit.Ignore
    public void testValidate__Live() throws java.lang.Exception {
        org.apache.ambari.server.KdcServerConnectionVerification kdcConnVerifier = new org.apache.ambari.server.KdcServerConnectionVerification(configuration);
        boolean result = kdcConnVerifier.isKdcReachable("c6501:88");
        org.junit.Assert.assertTrue(result);
    }

    private static class TestKdcServerConnectionVerification extends org.apache.ambari.server.KdcServerConnectionVerification {
        private org.apache.directory.kerberos.client.KdcConnection connection;

        private org.apache.directory.kerberos.client.KdcConfig kdcConfig = null;

        public TestKdcServerConnectionVerification(org.apache.ambari.server.configuration.Configuration config, org.apache.directory.kerberos.client.KdcConnection connectionMock) {
            super(config);
            connection = connectionMock;
        }

        @java.lang.Override
        protected org.apache.directory.kerberos.client.KdcConnection getKdcConnection(org.apache.directory.kerberos.client.KdcConfig config) {
            kdcConfig = config;
            return connection;
        }

        public org.apache.directory.kerberos.client.KdcConfig getConfigUsedInConnectionCreation() {
            return kdcConfig;
        }
    }

    private static class BlockingKdcConnection extends org.apache.directory.kerberos.client.KdcConnection {
        public BlockingKdcConnection(org.apache.directory.kerberos.client.KdcConfig config) {
            super(config);
        }

        @java.lang.Override
        public org.apache.directory.kerberos.client.TgTicket getTgt(java.lang.String principal, java.lang.String password) throws java.lang.Exception {
            java.lang.Thread.sleep(60000);
            return null;
        }
    }
}