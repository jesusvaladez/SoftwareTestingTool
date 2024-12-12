package org.apache.ambari.server.serveraction.kerberos;
import javax.persistence.EntityManager;
import org.easymock.EasyMockSupport;
import static org.easymock.EasyMock.anyObject;
import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
public class ConfigureAmbariIdentitiesServerActionTest extends org.easymock.EasyMockSupport {
    @org.junit.Rule
    public org.junit.rules.TemporaryFolder testFolder = new org.junit.rules.TemporaryFolder();

    @org.junit.Test
    public void installAmbariServerIdentity() throws java.lang.Exception {
        installAmbariServerIdentity(createNiceMock(org.apache.ambari.server.serveraction.ActionLog.class), true);
    }

    @org.junit.Test
    public void installAmbariServerIdentityWithNoAgentOnAmbariServer() throws java.lang.Exception {
        installAmbariServerIdentity(createNiceMock(org.apache.ambari.server.serveraction.ActionLog.class), false);
    }

    @org.junit.Test
    public void installAmbariServerIdentityWithNullActionLog() throws java.lang.Exception {
        installAmbariServerIdentity(null, true);
    }

    private void installAmbariServerIdentity(org.apache.ambari.server.serveraction.ActionLog actionLog, boolean ambariServerHasAgent) throws java.lang.Exception {
        java.lang.String principal = "ambari-server@EXAMPLE.COM";
        java.io.File srcKeytabFile = testFolder.newFile();
        java.io.File destKeytabFile = new java.io.File(testFolder.getRoot().getAbsolutePath(), "ambari-server.keytab");
        com.google.inject.Injector injector = createInjector();
        org.apache.ambari.server.orm.dao.HostDAO hostDAO = injector.getInstance(org.apache.ambari.server.orm.dao.HostDAO.class);
        org.apache.ambari.server.orm.entities.HostEntity hostEntity;
        if (ambariServerHasAgent) {
            hostEntity = createMock(org.apache.ambari.server.orm.entities.HostEntity.class);
            EasyMock.expect(hostEntity.getHostId()).andReturn(1L).once();
            EasyMock.expect(hostDAO.findById(1L)).andReturn(hostEntity).once();
        } else {
            hostEntity = null;
        }
        EasyMock.expect(hostDAO.findByName(org.apache.ambari.server.utils.StageUtils.getHostName())).andReturn(hostEntity).once();
        org.apache.ambari.server.orm.dao.KerberosKeytabPrincipalDAO kerberosKeytabPrincipalDAO = injector.getInstance(org.apache.ambari.server.orm.dao.KerberosKeytabPrincipalDAO.class);
        org.apache.ambari.server.orm.entities.KerberosKeytabPrincipalEntity kkp = createNiceMock(org.apache.ambari.server.orm.entities.KerberosKeytabPrincipalEntity.class);
        org.apache.ambari.server.orm.dao.KerberosKeytabPrincipalDAO.KeytabPrincipalFindOrCreateResult result = new org.apache.ambari.server.orm.dao.KerberosKeytabPrincipalDAO.KeytabPrincipalFindOrCreateResult();
        result.created = true;
        result.kkp = kkp;
        EasyMock.expect(kerberosKeytabPrincipalDAO.findOrCreate(EasyMock.anyObject(), EasyMock.eq(hostEntity), EasyMock.anyObject(), EasyMock.anyObject())).andReturn(result).once();
        EasyMock.expect(kerberosKeytabPrincipalDAO.merge(kkp)).andReturn(createNiceMock(org.apache.ambari.server.orm.entities.KerberosKeytabPrincipalEntity.class)).once();
        java.lang.reflect.Method methodCopyFile = org.apache.ambari.server.serveraction.kerberos.ConfigureAmbariIdentitiesServerAction.class.getDeclaredMethod("copyFile", java.lang.String.class, java.lang.String.class);
        java.lang.reflect.Method methodSetFileACL = org.apache.ambari.server.serveraction.kerberos.ConfigureAmbariIdentitiesServerAction.class.getDeclaredMethod("setFileACL", java.lang.String.class, java.lang.String.class, boolean.class, boolean.class, java.lang.String.class, boolean.class, boolean.class);
        org.apache.ambari.server.serveraction.kerberos.ConfigureAmbariIdentitiesServerAction action = createMockBuilder(org.apache.ambari.server.serveraction.kerberos.ConfigureAmbariIdentitiesServerAction.class).addMockedMethod(methodCopyFile).addMockedMethod(methodSetFileACL).createMock();
        action.copyFile(srcKeytabFile.getAbsolutePath(), destKeytabFile.getAbsolutePath());
        EasyMock.expectLastCall().once();
        action.setFileACL(destKeytabFile.getAbsolutePath(), "user1", true, true, "groupA", true, false);
        EasyMock.expectLastCall().once();
        replayAll();
        injector.injectMembers(action);
        action.installAmbariServerIdentity(new org.apache.ambari.server.serveraction.kerberos.stageutils.ResolvedKerberosPrincipal(null, null, principal, false, null, org.apache.ambari.server.controller.RootService.AMBARI.name(), org.apache.ambari.server.controller.RootComponent.AMBARI_SERVER.name(), destKeytabFile.getPath()), srcKeytabFile.getAbsolutePath(), destKeytabFile.getAbsolutePath(), "user1", "rw", "groupA", "r", actionLog);
        verifyAll();
    }

    @org.junit.Test
    public void configureJAAS() throws java.lang.Exception {
        configureJAAS(createNiceMock(org.apache.ambari.server.serveraction.ActionLog.class));
    }

    @org.junit.Test
    public void configureJAASWithNullActionLog() throws java.lang.Exception {
        configureJAAS(null);
    }

    private void configureJAAS(org.apache.ambari.server.serveraction.ActionLog actionLog) throws java.lang.Exception {
        java.lang.String principal = "ambari-server@EXAMPLE.COM";
        java.lang.String keytabFilePath = "/etc/security/keytabs/ambari.server.keytab";
        java.io.File jaasConfFile = testFolder.newFile();
        java.io.File jaasConfFileBak = new java.io.File(jaasConfFile.getAbsolutePath() + ".bak");
        java.lang.String originalJAASFileContent = "com.sun.security.jgss.krb5.initiate {\n" + (((((((("    com.sun.security.auth.module.Krb5LoginModule required\n" + "    renewTGT=false\n") + "    doNotPrompt=true\n") + "    useKeyTab=true\n") + "    keyTab=\"/etc/security/keytabs/ambari.keytab\"\n") + "    principal=\"ambari@EXAMPLE.COM\"\n") + "    storeKey=true\n") + "    useTicketCache=false;\n") + "};\n");
        org.apache.commons.io.FileUtils.writeStringToFile(jaasConfFile, originalJAASFileContent, java.nio.charset.Charset.defaultCharset());
        com.google.inject.Injector injector = createInjector();
        java.lang.reflect.Method methodGetJAASConfFilePath = org.apache.ambari.server.serveraction.kerberos.ConfigureAmbariIdentitiesServerAction.class.getDeclaredMethod("getJAASConfFilePath");
        org.apache.ambari.server.serveraction.kerberos.ConfigureAmbariIdentitiesServerAction action = createMockBuilder(org.apache.ambari.server.serveraction.kerberos.ConfigureAmbariIdentitiesServerAction.class).addMockedMethod(methodGetJAASConfFilePath).createMock();
        EasyMock.expect(action.getJAASConfFilePath()).andReturn(jaasConfFile.getAbsolutePath());
        replayAll();
        injector.injectMembers(action);
        action.configureJAAS(principal, keytabFilePath, actionLog);
        verifyAll();
        junit.framework.Assert.assertEquals("com.sun.security.jgss.krb5.initiate {\n" + (((((((("    com.sun.security.auth.module.Krb5LoginModule required\n" + "    renewTGT=false\n") + "    doNotPrompt=true\n") + "    useKeyTab=true\n") + "    keyTab=\"/etc/security/keytabs/ambari.server.keytab\"\n") + "    principal=\"ambari-server@EXAMPLE.COM\"\n") + "    storeKey=true\n") + "    useTicketCache=false;\n") + "};\n"), org.apache.commons.io.FileUtils.readFileToString(jaasConfFile, java.nio.charset.Charset.defaultCharset()));
        junit.framework.Assert.assertEquals(originalJAASFileContent, org.apache.commons.io.FileUtils.readFileToString(jaasConfFileBak, java.nio.charset.Charset.defaultCharset()));
    }

    private com.google.inject.Injector createInjector() {
        return com.google.inject.Guice.createInjector(new com.google.inject.AbstractModule() {
            @java.lang.Override
            protected void configure() {
                bind(javax.persistence.EntityManager.class).toInstance(createNiceMock(javax.persistence.EntityManager.class));
                bind(org.apache.ambari.server.orm.DBAccessor.class).toInstance(createNiceMock(org.apache.ambari.server.orm.DBAccessor.class));
                bind(org.apache.ambari.server.audit.AuditLogger.class).toInstance(createNiceMock(org.apache.ambari.server.audit.AuditLogger.class));
                bind(org.apache.ambari.server.state.Clusters.class).toInstance(createNiceMock(org.apache.ambari.server.state.Clusters.class));
                bind(org.apache.ambari.server.controller.KerberosHelper.class).toInstance(createNiceMock(org.apache.ambari.server.controller.KerberosHelper.class));
                bind(org.apache.ambari.server.state.stack.OsFamily.class).toInstance(createNiceMock(org.apache.ambari.server.state.stack.OsFamily.class));
                bind(org.apache.ambari.server.orm.dao.HostDAO.class).toInstance(createMock(org.apache.ambari.server.orm.dao.HostDAO.class));
                bind(org.apache.ambari.server.orm.dao.KerberosKeytabPrincipalDAO.class).toInstance(createMock(org.apache.ambari.server.orm.dao.KerberosKeytabPrincipalDAO.class));
            }
        });
    }
}