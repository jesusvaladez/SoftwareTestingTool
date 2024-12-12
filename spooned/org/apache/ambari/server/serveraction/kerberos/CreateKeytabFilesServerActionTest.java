package org.apache.ambari.server.serveraction.kerberos;
public class CreateKeytabFilesServerActionTest {
    @org.junit.Rule
    public org.junit.rules.TemporaryFolder testFolder = new org.junit.rules.TemporaryFolder();

    @org.junit.Test
    public void testEnsureAmbariOnlyAccess() throws java.lang.Exception {
        org.junit.Assume.assumeTrue(org.apache.ambari.server.utils.ShellCommandUtil.UNIX_LIKE);
        java.nio.file.Path path;
        java.util.Set<java.nio.file.attribute.PosixFilePermission> permissions;
        java.io.File directory = testFolder.newFolder();
        junit.framework.Assert.assertNotNull(directory);
        new org.apache.ambari.server.serveraction.kerberos.CreateKeytabFilesServerAction().ensureAmbariOnlyAccess(directory);
        path = java.nio.file.Paths.get(directory.getAbsolutePath());
        junit.framework.Assert.assertNotNull(path);
        permissions = java.nio.file.Files.getPosixFilePermissions(path);
        junit.framework.Assert.assertNotNull(permissions);
        junit.framework.Assert.assertNotNull(permissions);
        junit.framework.Assert.assertEquals(3, permissions.size());
        junit.framework.Assert.assertTrue(permissions.contains(java.nio.file.attribute.PosixFilePermission.OWNER_READ));
        junit.framework.Assert.assertTrue(permissions.contains(java.nio.file.attribute.PosixFilePermission.OWNER_WRITE));
        junit.framework.Assert.assertTrue(permissions.contains(java.nio.file.attribute.PosixFilePermission.OWNER_EXECUTE));
        junit.framework.Assert.assertFalse(permissions.contains(java.nio.file.attribute.PosixFilePermission.GROUP_READ));
        junit.framework.Assert.assertFalse(permissions.contains(java.nio.file.attribute.PosixFilePermission.GROUP_WRITE));
        junit.framework.Assert.assertFalse(permissions.contains(java.nio.file.attribute.PosixFilePermission.GROUP_EXECUTE));
        junit.framework.Assert.assertFalse(permissions.contains(java.nio.file.attribute.PosixFilePermission.OTHERS_READ));
        junit.framework.Assert.assertFalse(permissions.contains(java.nio.file.attribute.PosixFilePermission.OTHERS_WRITE));
        junit.framework.Assert.assertFalse(permissions.contains(java.nio.file.attribute.PosixFilePermission.OTHERS_EXECUTE));
        java.io.File file = java.io.File.createTempFile("temp_", "", directory);
        junit.framework.Assert.assertNotNull(file);
        junit.framework.Assert.assertTrue(file.exists());
        new org.apache.ambari.server.serveraction.kerberos.CreateKeytabFilesServerAction().ensureAmbariOnlyAccess(file);
        path = java.nio.file.Paths.get(file.getAbsolutePath());
        junit.framework.Assert.assertNotNull(path);
        permissions = java.nio.file.Files.getPosixFilePermissions(path);
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
    }
}