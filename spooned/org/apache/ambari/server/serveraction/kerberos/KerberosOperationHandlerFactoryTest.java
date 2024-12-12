package org.apache.ambari.server.serveraction.kerberos;
import org.easymock.EasyMock;
import static org.easymock.EasyMock.createNiceMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
public class KerberosOperationHandlerFactoryTest {
    private static com.google.inject.Injector injector;

    @org.junit.BeforeClass
    public static void beforeClass() throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.server.serveraction.kerberos.KerberosOperationHandlerFactoryTest.injector = com.google.inject.Guice.createInjector(new com.google.inject.AbstractModule() {
            @java.lang.Override
            protected void configure() {
                org.apache.ambari.server.configuration.Configuration configuration = org.easymock.EasyMock.createNiceMock(org.apache.ambari.server.configuration.Configuration.class);
                EasyMock.expect(configuration.getServerOsFamily()).andReturn("redhat6").anyTimes();
                EasyMock.replay(configuration);
                bind(org.apache.ambari.server.configuration.Configuration.class).toInstance(configuration);
                bind(org.apache.ambari.server.state.stack.OsFamily.class).toInstance(EasyMock.createNiceMock(org.apache.ambari.server.state.stack.OsFamily.class));
            }
        });
    }

    @org.junit.Test
    public void testForAD() {
        org.junit.Assert.assertEquals(org.apache.ambari.server.serveraction.kerberos.MITKerberosOperationHandler.class, org.apache.ambari.server.serveraction.kerberos.KerberosOperationHandlerFactoryTest.injector.getInstance(org.apache.ambari.server.serveraction.kerberos.KerberosOperationHandlerFactory.class).getKerberosOperationHandler(org.apache.ambari.server.serveraction.kerberos.KDCType.MIT_KDC).getClass());
    }

    @org.junit.Test
    public void testForMIT() {
        org.junit.Assert.assertEquals(org.apache.ambari.server.serveraction.kerberos.ADKerberosOperationHandler.class, org.apache.ambari.server.serveraction.kerberos.KerberosOperationHandlerFactoryTest.injector.getInstance(org.apache.ambari.server.serveraction.kerberos.KerberosOperationHandlerFactory.class).getKerberosOperationHandler(org.apache.ambari.server.serveraction.kerberos.KDCType.ACTIVE_DIRECTORY).getClass());
    }

    @org.junit.Test(expected = java.lang.IllegalArgumentException.class)
    public void testForNone() {
        org.junit.Assert.assertNull(org.apache.ambari.server.serveraction.kerberos.KerberosOperationHandlerFactoryTest.injector.getInstance(org.apache.ambari.server.serveraction.kerberos.KerberosOperationHandlerFactory.class).getKerberosOperationHandler(org.apache.ambari.server.serveraction.kerberos.KDCType.NONE));
    }

    @org.junit.Test(expected = java.lang.IllegalArgumentException.class)
    public void testForNull() {
        org.junit.Assert.assertNull(org.apache.ambari.server.serveraction.kerberos.KerberosOperationHandlerFactoryTest.injector.getInstance(org.apache.ambari.server.serveraction.kerberos.KerberosOperationHandlerFactory.class).getKerberosOperationHandler(null));
    }
}