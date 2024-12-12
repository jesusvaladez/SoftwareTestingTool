package org.apache.ambari.server.serveraction.kerberos;
import org.easymock.Capture;
import org.easymock.CaptureType;
import static org.easymock.EasyMock.anyObject;
import static org.easymock.EasyMock.capture;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.newCapture;
public class MITKerberosOperationHandlerTest extends org.apache.ambari.server.serveraction.kerberos.KDCKerberosOperationHandlerTest {
    private static java.lang.reflect.Method methodIsOpen;

    private static java.lang.reflect.Method methodPrincipalExists;

    private static java.lang.reflect.Method methodInvokeKAdmin;

    private static final java.util.Map<java.lang.String, java.lang.String> KERBEROS_ENV_MAP;

    static {
        java.util.Map<java.lang.String, java.lang.String> map = new java.util.HashMap<>(org.apache.ambari.server.serveraction.kerberos.KerberosOperationHandlerTest.DEFAULT_KERBEROS_ENV_MAP);
        map.put(org.apache.ambari.server.serveraction.kerberos.MITKerberosOperationHandler.KERBEROS_ENV_KDC_CREATE_ATTRIBUTES, "-attr1 -attr2 foo=345");
        KERBEROS_ENV_MAP = java.util.Collections.unmodifiableMap(map);
    }

    private com.google.inject.Injector injector;

    @org.junit.BeforeClass
    public static void beforeClassMITKerberosOperationHandlerTestC() throws java.lang.Exception {
        org.apache.ambari.server.serveraction.kerberos.MITKerberosOperationHandlerTest.methodIsOpen = org.apache.ambari.server.serveraction.kerberos.KerberosOperationHandler.class.getDeclaredMethod("isOpen");
        org.apache.ambari.server.serveraction.kerberos.MITKerberosOperationHandlerTest.methodPrincipalExists = org.apache.ambari.server.serveraction.kerberos.MITKerberosOperationHandler.class.getDeclaredMethod("principalExists", java.lang.String.class, boolean.class);
        org.apache.ambari.server.serveraction.kerberos.MITKerberosOperationHandlerTest.methodInvokeKAdmin = org.apache.ambari.server.serveraction.kerberos.MITKerberosOperationHandler.class.getDeclaredMethod("invokeKAdmin", java.lang.String.class);
    }

    @org.junit.Before
    public void beforeMITKerberosOperationHandlerTest() throws java.lang.Exception {
        injector = com.google.inject.Guice.createInjector(new com.google.inject.AbstractModule() {
            @java.lang.Override
            protected void configure() {
                org.apache.ambari.server.configuration.Configuration configuration = createNiceMock(org.apache.ambari.server.configuration.Configuration.class);
                EasyMock.expect(configuration.getServerOsFamily()).andReturn("redhat6").anyTimes();
                EasyMock.expect(configuration.getKerberosOperationRetryTimeout()).andReturn(1).anyTimes();
                bind(org.apache.ambari.server.state.Clusters.class).toInstance(createNiceMock(org.apache.ambari.server.state.Clusters.class));
                bind(org.apache.ambari.server.configuration.Configuration.class).toInstance(configuration);
                bind(org.apache.ambari.server.state.stack.OsFamily.class).toInstance(createNiceMock(org.apache.ambari.server.state.stack.OsFamily.class));
            }
        });
    }

    @org.junit.Test
    public void testSetPrincipalPassword() throws java.lang.Exception {
        org.apache.ambari.server.serveraction.kerberos.MITKerberosOperationHandler handler = createMockedHandler(org.apache.ambari.server.serveraction.kerberos.MITKerberosOperationHandlerTest.methodIsOpen, org.apache.ambari.server.serveraction.kerberos.MITKerberosOperationHandlerTest.methodPrincipalExists);
        EasyMock.expect(handler.isOpen()).andReturn(true).atLeastOnce();
        EasyMock.expect(handler.principalExists(org.apache.ambari.server.serveraction.kerberos.KerberosOperationHandlerTest.DEFAULT_ADMIN_PRINCIPAL, false)).andReturn(true).atLeastOnce();
        EasyMock.expect(handler.principalExists(null, false)).andReturn(false).atLeastOnce();
        EasyMock.expect(handler.principalExists("", false)).andReturn(false).atLeastOnce();
        replayAll();
        java.lang.Integer expected = 0;
        junit.framework.Assert.assertEquals(expected, handler.setPrincipalPassword(org.apache.ambari.server.serveraction.kerberos.KerberosOperationHandlerTest.DEFAULT_ADMIN_PRINCIPAL, null, false));
        junit.framework.Assert.assertEquals(expected, handler.setPrincipalPassword(org.apache.ambari.server.serveraction.kerberos.KerberosOperationHandlerTest.DEFAULT_ADMIN_PRINCIPAL, "", false));
        try {
            handler.setPrincipalPassword(null, org.apache.ambari.server.serveraction.kerberos.KerberosOperationHandlerTest.DEFAULT_ADMIN_PASSWORD, false);
            junit.framework.Assert.fail("Expected KerberosPrincipalDoesNotExistException");
        } catch (org.apache.ambari.server.serveraction.kerberos.KerberosPrincipalDoesNotExistException e) {
        }
        try {
            handler.setPrincipalPassword("", org.apache.ambari.server.serveraction.kerberos.KerberosOperationHandlerTest.DEFAULT_ADMIN_PASSWORD, false);
            junit.framework.Assert.fail("Expected KerberosPrincipalDoesNotExistException");
        } catch (org.apache.ambari.server.serveraction.kerberos.KerberosPrincipalDoesNotExistException e) {
        }
        verifyAll();
    }

    @org.junit.Test
    public void testCreateServicePrincipal_AdditionalAttributes() throws java.lang.Exception {
        org.easymock.Capture<? extends java.lang.String> query = EasyMock.newCapture();
        org.apache.ambari.server.utils.ShellCommandUtil.Result result1 = createNiceMock(org.apache.ambari.server.utils.ShellCommandUtil.Result.class);
        EasyMock.expect(result1.getStderr()).andReturn("").anyTimes();
        EasyMock.expect(result1.getStdout()).andReturn(("Principal \"" + org.apache.ambari.server.serveraction.kerberos.KerberosOperationHandlerTest.DEFAULT_ADMIN_PRINCIPAL) + "\" created\"").anyTimes();
        org.apache.ambari.server.utils.ShellCommandUtil.Result result2 = createNiceMock(org.apache.ambari.server.utils.ShellCommandUtil.Result.class);
        EasyMock.expect(result2.getStderr()).andReturn("").anyTimes();
        EasyMock.expect(result2.getStdout()).andReturn("Key: vno 1").anyTimes();
        org.apache.ambari.server.utils.ShellCommandUtil.Result kinitResult = createMock(org.apache.ambari.server.utils.ShellCommandUtil.Result.class);
        EasyMock.expect(kinitResult.isSuccessful()).andReturn(true);
        org.apache.ambari.server.serveraction.kerberos.MITKerberosOperationHandler handler = createMockedHandler(org.apache.ambari.server.serveraction.kerberos.MITKerberosOperationHandlerTest.methodInvokeKAdmin, org.apache.ambari.server.serveraction.kerberos.KDCKerberosOperationHandlerTest.methodExecuteCommand);
        EasyMock.expect(handler.executeCommand(EasyMock.anyObject(java.lang.String[].class), EasyMock.anyObject(java.util.Map.class), EasyMock.anyObject(org.apache.ambari.server.serveraction.kerberos.KDCKerberosOperationHandler.InteractivePasswordHandler.class))).andReturn(kinitResult).once();
        EasyMock.expect(handler.invokeKAdmin(EasyMock.capture(query))).andReturn(result1).once();
        replayAll();
        handler.open(getAdminCredentials(), org.apache.ambari.server.serveraction.kerberos.KerberosOperationHandlerTest.DEFAULT_REALM, org.apache.ambari.server.serveraction.kerberos.MITKerberosOperationHandlerTest.KERBEROS_ENV_MAP);
        handler.createPrincipal(org.apache.ambari.server.serveraction.kerberos.KerberosOperationHandlerTest.DEFAULT_ADMIN_PRINCIPAL, org.apache.ambari.server.serveraction.kerberos.KerberosOperationHandlerTest.DEFAULT_ADMIN_PASSWORD, false);
        handler.close();
        verifyAll();
        junit.framework.Assert.assertTrue(query.getValue().contains((" " + org.apache.ambari.server.serveraction.kerberos.MITKerberosOperationHandlerTest.KERBEROS_ENV_MAP.get(org.apache.ambari.server.serveraction.kerberos.MITKerberosOperationHandler.KERBEROS_ENV_KDC_CREATE_ATTRIBUTES)) + " "));
    }

    @org.junit.Test
    public void testCreateServicePrincipalExceptions() throws java.lang.Exception {
        org.apache.ambari.server.utils.ShellCommandUtil.Result kinitResult = createMock(org.apache.ambari.server.utils.ShellCommandUtil.Result.class);
        EasyMock.expect(kinitResult.isSuccessful()).andReturn(true);
        org.apache.ambari.server.serveraction.kerberos.MITKerberosOperationHandler handler = createMockedHandler(org.apache.ambari.server.serveraction.kerberos.KDCKerberosOperationHandlerTest.methodExecuteCommand);
        EasyMock.expect(handler.executeCommand(EasyMock.anyObject(java.lang.String[].class), EasyMock.anyObject(java.util.Map.class), EasyMock.anyObject(org.apache.ambari.server.serveraction.kerberos.KDCKerberosOperationHandler.InteractivePasswordHandler.class))).andReturn(kinitResult).once();
        replayAll();
        handler.open(new org.apache.ambari.server.security.credential.PrincipalKeyCredential(org.apache.ambari.server.serveraction.kerberos.KerberosOperationHandlerTest.DEFAULT_ADMIN_PRINCIPAL, org.apache.ambari.server.serveraction.kerberos.KerberosOperationHandlerTest.DEFAULT_ADMIN_PASSWORD), org.apache.ambari.server.serveraction.kerberos.KerberosOperationHandlerTest.DEFAULT_REALM, org.apache.ambari.server.serveraction.kerberos.MITKerberosOperationHandlerTest.KERBEROS_ENV_MAP);
        try {
            handler.createPrincipal(null, org.apache.ambari.server.serveraction.kerberos.KerberosOperationHandlerTest.DEFAULT_ADMIN_PASSWORD, false);
            junit.framework.Assert.fail("KerberosOperationException not thrown for null principal");
        } catch (java.lang.Throwable t) {
            junit.framework.Assert.assertEquals(org.apache.ambari.server.serveraction.kerberos.KerberosOperationException.class, t.getClass());
        }
        try {
            handler.createPrincipal("", org.apache.ambari.server.serveraction.kerberos.KerberosOperationHandlerTest.DEFAULT_ADMIN_PASSWORD, false);
            junit.framework.Assert.fail("KerberosOperationException not thrown for empty principal");
        } catch (java.lang.Throwable t) {
            junit.framework.Assert.assertEquals(org.apache.ambari.server.serveraction.kerberos.KerberosOperationException.class, t.getClass());
        }
        verifyAll();
    }

    @org.junit.Test(expected = org.apache.ambari.server.serveraction.kerberos.KerberosKDCConnectionException.class)
    public void testKDCConnectionException() throws java.lang.Exception {
        org.apache.ambari.server.utils.ShellCommandUtil.Result kinitResult = createMock(org.apache.ambari.server.utils.ShellCommandUtil.Result.class);
        EasyMock.expect(kinitResult.isSuccessful()).andReturn(true).anyTimes();
        org.apache.ambari.server.utils.ShellCommandUtil.Result kadminResult = createMock(org.apache.ambari.server.utils.ShellCommandUtil.Result.class);
        EasyMock.expect(kadminResult.getExitCode()).andReturn(1).anyTimes();
        EasyMock.expect(kadminResult.isSuccessful()).andReturn(false).anyTimes();
        EasyMock.expect(kadminResult.getStderr()).andReturn("kadmin: Cannot contact any KDC for requested realm while initializing kadmin interface").anyTimes();
        EasyMock.expect(kadminResult.getStdout()).andReturn("Authenticating as principal admin/admin with password.").anyTimes();
        org.apache.ambari.server.serveraction.kerberos.MITKerberosOperationHandler handler = createMockedHandler(org.apache.ambari.server.serveraction.kerberos.KDCKerberosOperationHandlerTest.methodExecuteCommand);
        EasyMock.expect(handler.executeCommand(EasyMock.anyObject(java.lang.String[].class), EasyMock.anyObject(java.util.Map.class), EasyMock.anyObject(org.apache.ambari.server.serveraction.kerberos.KDCKerberosOperationHandler.InteractivePasswordHandler.class))).andReturn(kinitResult).once();
        EasyMock.expect(handler.executeCommand(EasyMock.anyObject(java.lang.String[].class), EasyMock.anyObject(java.util.Map.class), EasyMock.anyObject(org.apache.ambari.server.serveraction.kerberos.KDCKerberosOperationHandler.InteractivePasswordHandler.class))).andReturn(kadminResult).once();
        replayAll();
        handler.open(getAdminCredentials(), org.apache.ambari.server.serveraction.kerberos.KerberosOperationHandlerTest.DEFAULT_REALM, org.apache.ambari.server.serveraction.kerberos.MITKerberosOperationHandlerTest.KERBEROS_ENV_MAP);
        handler.testAdministratorCredentials();
        handler.close();
        verifyAll();
    }

    @org.junit.Test(expected = org.apache.ambari.server.serveraction.kerberos.KerberosKDCConnectionException.class)
    public void testTestAdministratorCredentialsKDCConnectionException2() throws java.lang.Exception {
        org.apache.ambari.server.utils.ShellCommandUtil.Result kinitResult = createMock(org.apache.ambari.server.utils.ShellCommandUtil.Result.class);
        EasyMock.expect(kinitResult.isSuccessful()).andReturn(true).anyTimes();
        org.apache.ambari.server.utils.ShellCommandUtil.Result kadminResult = createMock(org.apache.ambari.server.utils.ShellCommandUtil.Result.class);
        EasyMock.expect(kadminResult.getExitCode()).andReturn(1).anyTimes();
        EasyMock.expect(kadminResult.isSuccessful()).andReturn(false).anyTimes();
        EasyMock.expect(kadminResult.getStderr()).andReturn("kadmin: Cannot resolve network address for admin server in requested realm while initializing kadmin interface").anyTimes();
        EasyMock.expect(kadminResult.getStdout()).andReturn("Authenticating as principal admin/admin with password.").anyTimes();
        org.apache.ambari.server.serveraction.kerberos.MITKerberosOperationHandler handler = createMockedHandler(org.apache.ambari.server.serveraction.kerberos.KDCKerberosOperationHandlerTest.methodExecuteCommand);
        EasyMock.expect(handler.executeCommand(EasyMock.anyObject(java.lang.String[].class), EasyMock.anyObject(java.util.Map.class), EasyMock.anyObject(org.apache.ambari.server.serveraction.kerberos.KDCKerberosOperationHandler.InteractivePasswordHandler.class))).andReturn(kinitResult).once();
        EasyMock.expect(handler.executeCommand(EasyMock.anyObject(java.lang.String[].class), EasyMock.anyObject(java.util.Map.class), EasyMock.anyObject(org.apache.ambari.server.serveraction.kerberos.KDCKerberosOperationHandler.InteractivePasswordHandler.class))).andReturn(kadminResult).once();
        replayAll();
        handler.open(getAdminCredentials(), org.apache.ambari.server.serveraction.kerberos.KerberosOperationHandlerTest.DEFAULT_REALM, org.apache.ambari.server.serveraction.kerberos.MITKerberosOperationHandlerTest.KERBEROS_ENV_MAP);
        handler.testAdministratorCredentials();
        handler.close();
        verifyAll();
    }

    @org.junit.Test
    public void testGetAdminServerHost() throws org.apache.ambari.server.serveraction.kerberos.KerberosOperationException {
        org.apache.ambari.server.utils.ShellCommandUtil.Result kinitResult = createMock(org.apache.ambari.server.utils.ShellCommandUtil.Result.class);
        EasyMock.expect(kinitResult.isSuccessful()).andReturn(true).anyTimes();
        org.easymock.Capture<java.lang.String[]> capturedKinitCommand = EasyMock.newCapture(CaptureType.ALL);
        org.apache.ambari.server.serveraction.kerberos.MITKerberosOperationHandler handler = createMockedHandler(org.apache.ambari.server.serveraction.kerberos.KDCKerberosOperationHandlerTest.methodExecuteCommand);
        EasyMock.expect(handler.executeCommand(EasyMock.capture(capturedKinitCommand), EasyMock.anyObject(java.util.Map.class), EasyMock.anyObject(org.apache.ambari.server.serveraction.kerberos.KDCKerberosOperationHandler.InteractivePasswordHandler.class))).andReturn(kinitResult).anyTimes();
        java.util.Map<java.lang.String, java.lang.String> config = new java.util.HashMap<>();
        config.put("encryption_types", "aes des3-cbc-sha1 rc4 des-cbc-md5");
        config.put(org.apache.ambari.server.serveraction.kerberos.MITKerberosOperationHandler.KERBEROS_ENV_KADMIN_PRINCIPAL_NAME, "kadmin/kdc.example.com");
        replayAll();
        config.put("admin_server_host", "kdc.example.com");
        handler.open(getAdminCredentials(), org.apache.ambari.server.serveraction.kerberos.KerberosOperationHandlerTest.DEFAULT_REALM, config);
        junit.framework.Assert.assertEquals("kdc.example.com", handler.getAdminServerHost(false));
        junit.framework.Assert.assertEquals("kdc.example.com", handler.getAdminServerHost(true));
        handler.close();
        config.put("admin_server_host", "kdc.example.com:749");
        handler.open(getAdminCredentials(), org.apache.ambari.server.serveraction.kerberos.KerberosOperationHandlerTest.DEFAULT_REALM, config);
        junit.framework.Assert.assertEquals("kdc.example.com", handler.getAdminServerHost(false));
        junit.framework.Assert.assertEquals("kdc.example.com:749", handler.getAdminServerHost(true));
        handler.close();
        verifyAll();
        junit.framework.Assert.assertTrue(capturedKinitCommand.hasCaptured());
        java.util.List<java.lang.String[]> capturedValues = capturedKinitCommand.getValues();
        junit.framework.Assert.assertEquals(2, capturedValues.size());
        junit.framework.Assert.assertEquals("kadmin/kdc.example.com", capturedValues.get(0)[4]);
        junit.framework.Assert.assertEquals("kadmin/kdc.example.com", capturedValues.get(1)[4]);
    }

    @java.lang.Override
    protected org.apache.ambari.server.serveraction.kerberos.MITKerberosOperationHandler createMockedHandler(java.lang.reflect.Method... mockedMethods) {
        org.apache.ambari.server.serveraction.kerberos.MITKerberosOperationHandler handler = createMockBuilder(org.apache.ambari.server.serveraction.kerberos.MITKerberosOperationHandler.class).addMockedMethods(mockedMethods).createMock();
        injector.injectMembers(handler);
        return handler;
    }

    @java.lang.Override
    protected java.util.Map<java.lang.String, java.lang.String> getKerberosEnv() {
        return org.apache.ambari.server.serveraction.kerberos.MITKerberosOperationHandlerTest.KERBEROS_ENV_MAP;
    }

    @java.lang.Override
    protected void setupPrincipalAlreadyExists(org.apache.ambari.server.serveraction.kerberos.KerberosOperationHandler handler, boolean service) throws java.lang.Exception {
        org.apache.ambari.server.utils.ShellCommandUtil.Result result = createMock(org.apache.ambari.server.utils.ShellCommandUtil.Result.class);
        EasyMock.expect(result.getExitCode()).andReturn(0).anyTimes();
        EasyMock.expect(result.isSuccessful()).andReturn(true).anyTimes();
        EasyMock.expect(result.getStderr()).andReturn(java.lang.String.format("add_principal: Principal or policy already exists while creating \"%s@EXAMPLE.COM\".", service ? "service/host" : "user")).anyTimes();
        EasyMock.expect(result.getStdout()).andReturn("Authenticating as principal admin/admin with password.").anyTimes();
        EasyMock.expect(handler.executeCommand(org.apache.ambari.server.serveraction.kerberos.KDCKerberosOperationHandlerTest.arrayContains(new java.lang.String[]{ "kadmin", "add_principal" }), EasyMock.anyObject(java.util.Map.class), EasyMock.anyObject(org.apache.ambari.server.serveraction.kerberos.KDCKerberosOperationHandler.InteractivePasswordHandler.class))).andReturn(result).anyTimes();
    }

    @java.lang.Override
    protected void setupPrincipalDoesNotExist(org.apache.ambari.server.serveraction.kerberos.KerberosOperationHandler handler, boolean service) throws java.lang.Exception {
        org.apache.ambari.server.utils.ShellCommandUtil.Result result = createMock(org.apache.ambari.server.utils.ShellCommandUtil.Result.class);
        EasyMock.expect(result.getExitCode()).andReturn(0).anyTimes();
        EasyMock.expect(result.isSuccessful()).andReturn(true).anyTimes();
        EasyMock.expect(result.getStderr()).andReturn(java.lang.String.format("get_principal: Principal does not exist while retrieving \"%s@EXAMPLE.COM\".", service ? "service/host" : "user")).anyTimes();
        EasyMock.expect(result.getStdout()).andReturn("Authenticating as principal admin/admin with password.").anyTimes();
        EasyMock.expect(handler.executeCommand(org.apache.ambari.server.serveraction.kerberos.KDCKerberosOperationHandlerTest.arrayContains(new java.lang.String[]{ "kadmin", "get_principal" }), EasyMock.anyObject(java.util.Map.class), EasyMock.anyObject(org.apache.ambari.server.serveraction.kerberos.KDCKerberosOperationHandler.InteractivePasswordHandler.class))).andReturn(result).anyTimes();
    }

    @java.lang.Override
    protected void setupPrincipalExists(org.apache.ambari.server.serveraction.kerberos.KerberosOperationHandler handler, boolean service) throws java.lang.Exception {
        org.apache.ambari.server.utils.ShellCommandUtil.Result result = createMock(org.apache.ambari.server.utils.ShellCommandUtil.Result.class);
        EasyMock.expect(result.getExitCode()).andReturn(0).anyTimes();
        EasyMock.expect(result.isSuccessful()).andReturn(true).anyTimes();
        EasyMock.expect(result.getStderr()).andReturn("").anyTimes();
        EasyMock.expect(result.getStdout()).andReturn(java.lang.String.format("Authenticating as principal admin/admin with password.\n" + ((((((((((((((((((("Principal: %s@EXAMPLE.COM\n" + "Expiration date: [never]\n") + "Last password change: Thu Jan 08 13:09:52 UTC 2015\n") + "Password expiration date: [none]\n") + "Maximum ticket life: 1 day 00:00:00\n") + "Maximum renewable life: 0 days 00:00:00\n") + "Last modified: Thu Jan 08 13:09:52 UTC 2015 (root/admin@EXAMPLE.COM)\n") + "Last successful authentication: [never]\n") + "Last failed authentication: [never]\n") + "Failed password attempts: 0\n") + "Number of keys: 6\n") + "Key: vno 1, aes256-cts-hmac-sha1-96, no salt\n") + "Key: vno 1, aes128-cts-hmac-sha1-96, no salt\n") + "Key: vno 1, des3-cbc-sha1, no salt\n") + "Key: vno 1, arcfour-hmac, no salt\n") + "Key: vno 1, des-hmac-sha1, no salt\n") + "Key: vno 1, des-cbc-md5, no salt\n") + "MKey: vno 1\n") + "Attributes:\n") + "Policy: [none]"), service ? "service/host" : "user")).anyTimes();
        EasyMock.expect(handler.executeCommand(org.apache.ambari.server.serveraction.kerberos.KDCKerberosOperationHandlerTest.arrayContains(new java.lang.String[]{ "kadmin", "get_principal" }), EasyMock.anyObject(java.util.Map.class), EasyMock.anyObject(org.apache.ambari.server.serveraction.kerberos.KDCKerberosOperationHandler.InteractivePasswordHandler.class))).andReturn(result).anyTimes();
    }
}