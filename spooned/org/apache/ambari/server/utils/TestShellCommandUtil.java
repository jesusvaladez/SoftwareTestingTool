package org.apache.ambari.server.utils;
public class TestShellCommandUtil {
    public org.junit.rules.TemporaryFolder temp = new org.junit.rules.TemporaryFolder();

    @org.junit.Before
    public void setUp() throws java.io.IOException {
        try {
            temp.create();
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
    }

    @org.junit.After
    public void tearDown() throws java.io.IOException {
        temp.delete();
    }

    @org.junit.Test
    public void testOSDetection() throws java.lang.Exception {
        junit.framework.Assert.assertTrue((org.apache.ambari.server.utils.ShellCommandUtil.LINUX ^ org.apache.ambari.server.utils.ShellCommandUtil.WINDOWS) ^ org.apache.ambari.server.utils.ShellCommandUtil.MAC);
        junit.framework.Assert.assertTrue(org.apache.ambari.server.utils.ShellCommandUtil.LINUX || (org.apache.ambari.server.utils.ShellCommandUtil.MAC == org.apache.ambari.server.utils.ShellCommandUtil.UNIX_LIKE));
    }

    @org.junit.Test
    public void testUnixFilePermissions() throws java.lang.Exception {
        java.io.File dummyFile = new java.io.File((temp.getRoot() + java.io.File.separator) + "dummy");
        new java.io.FileOutputStream(dummyFile).close();
        if (org.apache.ambari.server.utils.ShellCommandUtil.LINUX) {
            org.apache.ambari.server.utils.ShellCommandUtil.setUnixFilePermissions("600", dummyFile.getAbsolutePath());
            java.lang.String p = org.apache.ambari.server.utils.ShellCommandUtil.getUnixFilePermissions(dummyFile.getAbsolutePath());
            junit.framework.Assert.assertEquals("600", p);
            org.apache.ambari.server.utils.ShellCommandUtil.setUnixFilePermissions("444", dummyFile.getAbsolutePath());
            p = org.apache.ambari.server.utils.ShellCommandUtil.getUnixFilePermissions(dummyFile.getAbsolutePath());
            junit.framework.Assert.assertEquals("444", p);
            org.apache.ambari.server.utils.ShellCommandUtil.setUnixFilePermissions("777", dummyFile.getAbsolutePath());
            p = org.apache.ambari.server.utils.ShellCommandUtil.getUnixFilePermissions(dummyFile.getAbsolutePath());
            junit.framework.Assert.assertEquals("777", p);
        } else {
            org.apache.ambari.server.utils.ShellCommandUtil.setUnixFilePermissions(org.apache.ambari.server.utils.ShellCommandUtil.MASK_OWNER_ONLY_RW, dummyFile.getAbsolutePath());
            java.lang.String p = org.apache.ambari.server.utils.ShellCommandUtil.getUnixFilePermissions(dummyFile.getAbsolutePath());
            junit.framework.Assert.assertEquals(p, org.apache.ambari.server.utils.ShellCommandUtil.MASK_EVERYBODY_RWX);
        }
    }

    @org.junit.Test
    public void testRunCommand() throws java.lang.Exception {
        org.apache.ambari.server.utils.ShellCommandUtil.Result result;
        if (org.apache.ambari.server.utils.ShellCommandUtil.LINUX) {
            result = org.apache.ambari.server.utils.ShellCommandUtil.runCommand(new java.lang.String[]{ "echo", "dummy" });
            junit.framework.Assert.assertEquals(0, result.getExitCode());
            junit.framework.Assert.assertEquals("dummy\n", result.getStdout());
            junit.framework.Assert.assertEquals("", result.getStderr());
            junit.framework.Assert.assertTrue(result.isSuccessful());
            result = org.apache.ambari.server.utils.ShellCommandUtil.runCommand(new java.lang.String[]{ "false" });
            junit.framework.Assert.assertEquals(1, result.getExitCode());
            junit.framework.Assert.assertFalse(result.isSuccessful());
        } else {
        }
    }

    @org.junit.Test
    public void testRunInteractiveCommand() throws java.lang.Exception {
        org.apache.ambari.server.utils.ShellCommandUtil.InteractiveHandler interactiveHandler = new org.apache.ambari.server.utils.ShellCommandUtil.InteractiveHandler() {
            boolean done = false;

            @java.lang.Override
            public boolean done() {
                return done;
            }

            @java.lang.Override
            public java.lang.String getResponse(java.lang.String query) {
                if (query.contains("Arg1")) {
                    return "a1";
                } else if (query.contains("Arg2")) {
                    done = true;
                    return "a2";
                } else {
                    return null;
                }
            }

            @java.lang.Override
            public void start() {
            }
        };
        org.apache.ambari.server.utils.ShellCommandUtil.Result result = org.apache.ambari.server.utils.ShellCommandUtil.runCommand(new java.lang.String[]{ "./src/test/resources/interactive_shell_test.sh" }, null, interactiveHandler, false);
        junit.framework.Assert.assertEquals(0, result.getExitCode());
        junit.framework.Assert.assertTrue(result.isSuccessful());
        junit.framework.Assert.assertEquals("a1\na2\n", result.getStdout());
    }

    @org.junit.Test
    public void testHideOpenSslPassword() {
        java.lang.String command_pass = "openssl ca -config ca.config -in agent_hostname1.csr -out " + "agent_hostname1.crt -batch -passin pass:1234 -keyfile ca.key -cert ca.crt";
        java.lang.String command_key = "openssl ca -create_serial -out /var/lib/ambari-server/keys/ca.crt -days 365 -keyfile /var/lib/ambari-server/keys/ca.key " + (("-key 1234 -selfsign -extensions jdk7_ca " + "-config /var/lib/ambari-server/keys/ca.config -batch ") + "-infiles /var/lib/ambari-server/keys/ca.csr");
        junit.framework.Assert.assertFalse(org.apache.ambari.server.utils.ShellCommandUtil.hideOpenSslPassword(command_pass).contains("1234"));
        junit.framework.Assert.assertFalse(org.apache.ambari.server.utils.ShellCommandUtil.hideOpenSslPassword(command_key).contains("1234"));
    }

    @org.junit.Test
    public void testResultsClassIsPublic() throws java.lang.Exception {
        java.lang.Class resultClass = org.apache.ambari.server.utils.ShellCommandUtil.Result.class;
        junit.framework.Assert.assertEquals(java.lang.reflect.Modifier.PUBLIC, resultClass.getModifiers() & java.lang.reflect.Modifier.PUBLIC);
        for (java.lang.reflect.Method method : resultClass.getMethods()) {
            junit.framework.Assert.assertEquals(method.getName(), java.lang.reflect.Modifier.PUBLIC, method.getModifiers() & java.lang.reflect.Modifier.PUBLIC);
        }
    }

    @org.junit.Test
    public void testPathExists() throws java.lang.Exception {
        java.io.File nonExisting = new java.io.File(temp.getRoot(), "i_do_not_exist");
        java.io.File existing = temp.newFolder();
        org.apache.ambari.server.utils.ShellCommandUtil.Result result;
        result = org.apache.ambari.server.utils.ShellCommandUtil.pathExists(existing.getAbsolutePath(), false);
        junit.framework.Assert.assertTrue(existing.exists());
        junit.framework.Assert.assertTrue(result.isSuccessful());
        result = org.apache.ambari.server.utils.ShellCommandUtil.pathExists(nonExisting.getAbsolutePath(), false);
        junit.framework.Assert.assertFalse(nonExisting.exists());
        junit.framework.Assert.assertFalse(result.isSuccessful());
    }

    @org.junit.Test
    public void testMkdir() throws java.lang.Exception {
        java.io.File directory = new java.io.File(temp.getRoot(), "newdir");
        org.apache.ambari.server.utils.ShellCommandUtil.Result result = org.apache.ambari.server.utils.ShellCommandUtil.mkdir(directory.getAbsolutePath(), false);
        junit.framework.Assert.assertTrue(result.isSuccessful());
        junit.framework.Assert.assertTrue(directory.exists());
    }

    @org.junit.Test
    public void testCopy() throws java.lang.Exception {
        java.io.File srcFile = temp.newFile();
        java.io.File destFile = new java.io.File(srcFile.getParentFile(), "copied_file");
        java.io.FileWriter writer = new java.io.FileWriter(srcFile);
        writer.write("Hello World!!!!!");
        writer.close();
        org.apache.ambari.server.utils.ShellCommandUtil.Result result = org.apache.ambari.server.utils.ShellCommandUtil.copyFile(srcFile.getAbsolutePath(), destFile.getAbsolutePath(), false, false);
        junit.framework.Assert.assertTrue(result.isSuccessful());
        junit.framework.Assert.assertTrue(destFile.exists());
        junit.framework.Assert.assertTrue(destFile.length() > 0);
        junit.framework.Assert.assertEquals(destFile.length(), srcFile.length());
    }

    @org.junit.Test
    public void deleteExistingFile() throws java.lang.Exception {
        java.io.File file = temp.newFile();
        org.apache.ambari.server.utils.ShellCommandUtil.Result result = org.apache.ambari.server.utils.ShellCommandUtil.delete(file.getAbsolutePath(), false, false);
        junit.framework.Assert.assertTrue(result.getStderr(), result.isSuccessful());
        junit.framework.Assert.assertFalse(file.exists());
    }

    @org.junit.Test
    public void deleteNonexistentFile() throws java.lang.Exception {
        java.io.File file = temp.newFile();
        if (file.delete()) {
            org.apache.ambari.server.utils.ShellCommandUtil.Result result = org.apache.ambari.server.utils.ShellCommandUtil.delete(file.getAbsolutePath(), false, false);
            junit.framework.Assert.assertFalse(result.getStderr(), result.isSuccessful());
            junit.framework.Assert.assertFalse(file.exists());
        }
    }

    @org.junit.Test
    public void forceDeleteNonexistentFile() throws java.lang.Exception {
        java.io.File file = temp.newFile();
        if (file.delete()) {
            org.apache.ambari.server.utils.ShellCommandUtil.Result result = org.apache.ambari.server.utils.ShellCommandUtil.delete(file.getAbsolutePath(), true, false);
            junit.framework.Assert.assertTrue(result.getStderr(), result.isSuccessful());
            junit.framework.Assert.assertFalse(file.exists());
        }
    }
}