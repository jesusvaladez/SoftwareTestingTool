package org.apache.ambari.server.serveraction.kerberos;
import org.apache.commons.lang.StringUtils;
import org.easymock.EasyMock;
import org.easymock.IAnswer;
import org.easymock.IArgumentMatcher;
import static org.easymock.EasyMock.anyObject;
import static org.easymock.EasyMock.anyString;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.getCurrentArguments;
public abstract class KDCKerberosOperationHandlerTest extends org.apache.ambari.server.serveraction.kerberos.KerberosOperationHandlerTest {
    static java.lang.reflect.Method methodExecuteCommand;

    static java.lang.reflect.Method methodGetExecutable;

    @org.junit.BeforeClass
    public static void beforeKDCKerberosOperationHandlerTest() throws java.lang.Exception {
        org.apache.ambari.server.serveraction.kerberos.KDCKerberosOperationHandlerTest.methodExecuteCommand = org.apache.ambari.server.serveraction.kerberos.KDCKerberosOperationHandler.class.getDeclaredMethod("executeCommand", java.lang.String[].class, java.util.Map.class, org.apache.ambari.server.utils.ShellCommandUtil.InteractiveHandler.class);
        org.apache.ambari.server.serveraction.kerberos.KDCKerberosOperationHandlerTest.methodGetExecutable = org.apache.ambari.server.serveraction.kerberos.KerberosOperationHandler.class.getDeclaredMethod("getExecutable", java.lang.String.class);
    }

    @org.junit.Test
    public void testInteractivePasswordHandler() {
        org.apache.ambari.server.serveraction.kerberos.KDCKerberosOperationHandler.InteractivePasswordHandler handler = new org.apache.ambari.server.serveraction.kerberos.KDCKerberosOperationHandler.InteractivePasswordHandler("admin_password", "user_password");
        handler.start();
        junit.framework.Assert.assertEquals("admin_password", handler.getResponse("password"));
        junit.framework.Assert.assertFalse(handler.done());
        junit.framework.Assert.assertEquals("user_password", handler.getResponse("password"));
        junit.framework.Assert.assertFalse(handler.done());
        junit.framework.Assert.assertEquals("user_password", handler.getResponse("password"));
        junit.framework.Assert.assertTrue(handler.done());
        handler.start();
        junit.framework.Assert.assertEquals("admin_password", handler.getResponse("password"));
        junit.framework.Assert.assertFalse(handler.done());
        junit.framework.Assert.assertEquals("user_password", handler.getResponse("password"));
        junit.framework.Assert.assertFalse(handler.done());
        junit.framework.Assert.assertEquals("user_password", handler.getResponse("password"));
        junit.framework.Assert.assertTrue(handler.done());
    }

    @java.lang.Override
    protected org.apache.ambari.server.serveraction.kerberos.KerberosOperationHandler createMockedHandler() throws org.apache.ambari.server.serveraction.kerberos.KerberosOperationException {
        org.apache.ambari.server.serveraction.kerberos.KDCKerberosOperationHandler handler = createMockedHandler(org.apache.ambari.server.serveraction.kerberos.KDCKerberosOperationHandlerTest.methodExecuteCommand, org.apache.ambari.server.serveraction.kerberos.KDCKerberosOperationHandlerTest.methodGetExecutable);
        EasyMock.expect(handler.getExecutable(EasyMock.anyString())).andAnswer(new org.easymock.IAnswer<java.lang.String>() {
            @java.lang.Override
            public java.lang.String answer() throws java.lang.Throwable {
                java.lang.Object[] args = EasyMock.getCurrentArguments();
                return args[0].toString();
            }
        }).anyTimes();
        return handler;
    }

    @java.lang.Override
    protected void setupOpenSuccess(org.apache.ambari.server.serveraction.kerberos.KerberosOperationHandler handler) throws java.lang.Exception {
        org.apache.ambari.server.utils.ShellCommandUtil.Result result = createMock(org.apache.ambari.server.utils.ShellCommandUtil.Result.class);
        EasyMock.expect(result.isSuccessful()).andReturn(true);
        EasyMock.expect(handler.executeCommand(org.apache.ambari.server.serveraction.kerberos.KDCKerberosOperationHandlerTest.arrayContains("kinit"), EasyMock.anyObject(java.util.Map.class), EasyMock.anyObject(org.apache.ambari.server.serveraction.kerberos.KDCKerberosOperationHandler.InteractivePasswordHandler.class))).andReturn(result).anyTimes();
    }

    @java.lang.Override
    protected void setupOpenFailure(org.apache.ambari.server.serveraction.kerberos.KerberosOperationHandler handler) throws java.lang.Exception {
        org.apache.ambari.server.utils.ShellCommandUtil.Result result = createMock(org.apache.ambari.server.utils.ShellCommandUtil.Result.class);
        EasyMock.expect(result.isSuccessful()).andReturn(false).once();
        EasyMock.expect(result.getExitCode()).andReturn(-1).once();
        EasyMock.expect(result.getStdout()).andReturn("STDOUT data").once();
        EasyMock.expect(result.getStderr()).andReturn("STDERR data").once();
        EasyMock.expect(handler.executeCommand(org.apache.ambari.server.serveraction.kerberos.KDCKerberosOperationHandlerTest.arrayContains("kinit"), EasyMock.anyObject(java.util.Map.class), EasyMock.anyObject(org.apache.ambari.server.serveraction.kerberos.KDCKerberosOperationHandler.InteractivePasswordHandler.class))).andReturn(result).anyTimes();
    }

    protected abstract org.apache.ambari.server.serveraction.kerberos.KDCKerberosOperationHandler createMockedHandler(java.lang.reflect.Method... mockedMethods);

    public static class ArrayContains implements org.easymock.IArgumentMatcher {
        private java.lang.String[] startItems;

        ArrayContains(java.lang.String startItem) {
            this.startItems = new java.lang.String[]{ startItem };
        }

        ArrayContains(java.lang.String[] startItems) {
            this.startItems = startItems;
        }

        @java.lang.Override
        public boolean matches(java.lang.Object o) {
            if (o instanceof java.lang.String[]) {
                java.lang.String[] array = ((java.lang.String[]) (o));
                for (java.lang.String item : startItems) {
                    boolean valueContains = false;
                    for (java.lang.String value : array) {
                        if (value.contains(item)) {
                            valueContains = true;
                            break;
                        }
                    }
                    if (!valueContains) {
                        return false;
                    }
                }
                return true;
            }
            return false;
        }

        @java.lang.Override
        public void appendTo(java.lang.StringBuffer stringBuffer) {
            stringBuffer.append("arrayContains(");
            stringBuffer.append(org.apache.commons.lang.StringUtils.join(startItems, ", "));
            stringBuffer.append("\")");
        }
    }

    static java.lang.String[] arrayContains(java.lang.String in) {
        org.easymock.EasyMock.reportMatcher(new org.apache.ambari.server.serveraction.kerberos.KDCKerberosOperationHandlerTest.ArrayContains(in));
        return null;
    }

    static java.lang.String[] arrayContains(java.lang.String[] in) {
        org.easymock.EasyMock.reportMatcher(new org.apache.ambari.server.serveraction.kerberos.KDCKerberosOperationHandlerTest.ArrayContains(in));
        return null;
    }
}