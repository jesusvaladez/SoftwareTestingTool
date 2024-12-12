package org.apache.ambari.server.security.encryption;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import static org.easymock.EasyMock.expect;
import static org.powermock.api.easymock.PowerMock.mockStatic;
import static org.powermock.api.easymock.PowerMock.replayAll;
import static org.powermock.api.easymock.PowerMock.verifyAll;
@org.junit.runner.RunWith(org.powermock.modules.junit4.PowerMockRunner.class)
@org.powermock.core.classloader.annotations.PowerMockIgnore({ "javax.crypto.*", "org.apache.log4j.*" })
@org.powermock.core.classloader.annotations.PrepareForTest({ org.apache.ambari.server.security.encryption.MasterKeyServiceImpl.class })
public class MasterKeyServiceTest extends junit.framework.TestCase {
    @org.junit.Rule
    public org.junit.rules.TemporaryFolder tmpFolder = new org.junit.rules.TemporaryFolder();

    private java.lang.String fileDir;

    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.security.encryption.MasterKeyServiceTest.class);

    @java.lang.Override
    protected void setUp() throws java.lang.Exception {
        tmpFolder.create();
        fileDir = tmpFolder.newFolder("keys").getAbsolutePath();
        org.apache.ambari.server.security.encryption.MasterKeyServiceTest.LOG.info("Setting temp folder to: " + fileDir);
    }

    @org.junit.Test
    public void testInitializeMasterKey() throws java.lang.Exception {
        java.io.File masterKeyFile = new java.io.File(fileDir, "master");
        org.apache.ambari.server.security.encryption.MasterKeyServiceImpl ms = new org.apache.ambari.server.security.encryption.MasterKeyServiceImpl("dummyKey");
        junit.framework.Assert.assertTrue(ms.initializeMasterKeyFile(masterKeyFile, "ThisisSomePassPhrase"));
        ms = new org.apache.ambari.server.security.encryption.MasterKeyServiceImpl(masterKeyFile);
        junit.framework.Assert.assertTrue(ms.isMasterKeyInitialized());
        junit.framework.Assert.assertTrue(masterKeyFile.exists());
        java.util.Set<java.nio.file.attribute.PosixFilePermission> permissions = java.nio.file.Files.getPosixFilePermissions(java.nio.file.Paths.get(masterKeyFile.getAbsolutePath()));
        junit.framework.Assert.assertNotNull(permissions);
        junit.framework.Assert.assertEquals(2, permissions.size());
        junit.framework.Assert.assertTrue(permissions.contains(java.nio.file.attribute.PosixFilePermission.OWNER_READ));
        junit.framework.Assert.assertTrue(permissions.contains(java.nio.file.attribute.PosixFilePermission.OWNER_WRITE));
        junit.framework.Assert.assertFalse(permissions.contains(java.nio.file.attribute.PosixFilePermission.OWNER_EXECUTE));
        junit.framework.Assert.assertFalse(permissions.contains(java.nio.file.attribute.PosixFilePermission.GROUP_READ));
        junit.framework.Assert.assertFalse(permissions.contains(java.nio.file.attribute.PosixFilePermission.GROUP_WRITE));
        junit.framework.Assert.assertFalse(permissions.contains(java.nio.file.attribute.PosixFilePermission.GROUP_EXECUTE));
        junit.framework.Assert.assertFalse(permissions.contains(java.nio.file.attribute.PosixFilePermission.OTHERS_READ));
        junit.framework.Assert.assertFalse(permissions.contains(java.nio.file.attribute.PosixFilePermission.OTHERS_WRITE));
        junit.framework.Assert.assertFalse(permissions.contains(java.nio.file.attribute.PosixFilePermission.OTHERS_EXECUTE));
        org.apache.ambari.server.security.encryption.MasterKeyService ms1 = new org.apache.ambari.server.security.encryption.MasterKeyServiceImpl(masterKeyFile);
        junit.framework.Assert.assertTrue(ms1.isMasterKeyInitialized());
        junit.framework.Assert.assertEquals("ThisisSomePassPhrase", new java.lang.String(ms1.getMasterSecret()));
        junit.framework.Assert.assertEquals(new java.lang.String(ms.getMasterSecret()), new java.lang.String(ms1.getMasterSecret()));
    }

    @org.junit.Test
    public void testReadFromEnvAsKey() throws java.lang.Exception {
        java.util.Map<java.lang.String, java.lang.String> mapRet = new java.util.HashMap<>();
        mapRet.put("AMBARI_SECURITY_MASTER_KEY", "ThisisSomePassPhrase");
        mockStatic(java.lang.System.class);
        EasyMock.expect(java.lang.System.getenv()).andReturn(mapRet);
        replayAll();
        org.apache.ambari.server.configuration.Configuration configuration = new org.apache.ambari.server.configuration.Configuration(new java.util.Properties());
        org.apache.ambari.server.security.encryption.MasterKeyService ms = new org.apache.ambari.server.security.encryption.MasterKeyServiceImpl(configuration);
        verifyAll();
        junit.framework.Assert.assertTrue(ms.isMasterKeyInitialized());
        junit.framework.Assert.assertNotNull(ms.getMasterSecret());
        junit.framework.Assert.assertEquals("ThisisSomePassPhrase", new java.lang.String(ms.getMasterSecret()));
    }

    @org.junit.Test
    public void testReadFromEnvAsPath() throws java.lang.Exception {
        java.io.File masterKeyFile = new java.io.File(fileDir, "master");
        org.apache.ambari.server.security.encryption.MasterKeyServiceImpl ms = new org.apache.ambari.server.security.encryption.MasterKeyServiceImpl("dummyKey");
        junit.framework.Assert.assertTrue(ms.initializeMasterKeyFile(masterKeyFile, "ThisisSomePassPhrase"));
        ms = new org.apache.ambari.server.security.encryption.MasterKeyServiceImpl(masterKeyFile);
        junit.framework.Assert.assertTrue(ms.isMasterKeyInitialized());
        junit.framework.Assert.assertTrue(masterKeyFile.exists());
        java.util.Map<java.lang.String, java.lang.String> mapRet = new java.util.HashMap<>();
        mapRet.put(org.apache.ambari.server.configuration.Configuration.MASTER_KEY_LOCATION.getKey(), masterKeyFile.getAbsolutePath());
        mockStatic(java.lang.System.class);
        EasyMock.expect(java.lang.System.getenv()).andReturn(mapRet);
        replayAll();
        org.apache.ambari.server.configuration.Configuration configuration = new org.apache.ambari.server.configuration.Configuration(new java.util.Properties());
        ms = new org.apache.ambari.server.security.encryption.MasterKeyServiceImpl(configuration);
        verifyAll();
        junit.framework.Assert.assertTrue(ms.isMasterKeyInitialized());
        junit.framework.Assert.assertNotNull(ms.getMasterSecret());
        junit.framework.Assert.assertEquals("ThisisSomePassPhrase", new java.lang.String(ms.getMasterSecret()));
        junit.framework.Assert.assertTrue(masterKeyFile.exists());
    }

    @java.lang.Override
    protected void tearDown() throws java.lang.Exception {
        tmpFolder.delete();
    }
}