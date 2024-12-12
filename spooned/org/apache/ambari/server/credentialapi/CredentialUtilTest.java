package org.apache.ambari.server.credentialapi;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.util.ToolRunner;
public class CredentialUtilTest {
    @org.junit.Rule
    public org.junit.rules.TemporaryFolder temporaryFolder = new org.junit.rules.TemporaryFolder();

    private final java.io.ByteArrayOutputStream out = new java.io.ByteArrayOutputStream();

    private final java.io.ByteArrayOutputStream err = new java.io.ByteArrayOutputStream();

    private static final java.lang.String CREATE_VERB = "create";

    private static final java.lang.String DELETE_VERB = "delete";

    private static final java.lang.String LIST_VERB = "list";

    private static final java.lang.String GET_VERB = "get";

    private java.lang.String getTempFolderName() {
        return temporaryFolder.getRoot().getAbsolutePath();
    }

    private java.lang.String getProviderPath(java.lang.String fileName) {
        return ((org.apache.ambari.server.credentialapi.CredentialUtil.jceksPrefix + getTempFolderName()) + "/") + fileName;
    }

    private java.lang.String[] getCreateArgs(java.lang.String alias, java.lang.String credential, java.lang.String providerPath) {
        java.util.List<java.lang.String> args = new java.util.ArrayList<>();
        args.add(org.apache.ambari.server.credentialapi.CredentialUtilTest.CREATE_VERB);
        args.add(alias);
        args.add("-value");
        args.add(credential);
        args.add("-provider");
        args.add(providerPath);
        return args.toArray(new java.lang.String[args.size()]);
    }

    private java.lang.String[] getSafeCreateArgs(java.lang.String alias, java.lang.String credential, java.lang.String providerPath) {
        java.util.List<java.lang.String> args = new java.util.ArrayList<>();
        args.add(org.apache.ambari.server.credentialapi.CredentialUtilTest.CREATE_VERB);
        args.add(alias);
        args.add("-value");
        args.add(credential);
        args.add("-provider");
        args.add(providerPath);
        args.add("-n");
        return args.toArray(new java.lang.String[args.size()]);
    }

    private java.lang.String[] getUpdateArgs(java.lang.String alias, java.lang.String credential, java.lang.String providerPath) {
        java.util.List<java.lang.String> args = new java.util.ArrayList<>();
        args.add(org.apache.ambari.server.credentialapi.CredentialUtilTest.CREATE_VERB);
        args.add(alias);
        args.add("-value");
        args.add(credential);
        args.add("-provider");
        args.add(providerPath);
        args.add("-f");
        return args.toArray(new java.lang.String[args.size()]);
    }

    private java.lang.String[] getDeleteArgs(java.lang.String alias, java.lang.String providerPath) {
        java.util.List<java.lang.String> args = new java.util.ArrayList<>();
        args.add(org.apache.ambari.server.credentialapi.CredentialUtilTest.DELETE_VERB);
        args.add(alias);
        args.add("-provider");
        args.add(providerPath);
        args.add("-f");
        return args.toArray(new java.lang.String[args.size()]);
    }

    private java.lang.String[] getListArgs(java.lang.String providerPath) {
        java.util.List<java.lang.String> args = new java.util.ArrayList<>();
        args.add(org.apache.ambari.server.credentialapi.CredentialUtilTest.LIST_VERB);
        args.add("-provider");
        args.add(providerPath);
        return args.toArray(new java.lang.String[args.size()]);
    }

    private java.lang.String[] getGetArgs(java.lang.String alias, java.lang.String providerPath) {
        java.util.List<java.lang.String> args = new java.util.ArrayList<>();
        args.add(org.apache.ambari.server.credentialapi.CredentialUtilTest.GET_VERB);
        args.add(alias);
        args.add("-provider");
        args.add(providerPath);
        return args.toArray(new java.lang.String[args.size()]);
    }

    private int executeCommand(java.lang.String[] args) throws java.lang.Exception {
        return org.apache.hadoop.util.ToolRunner.run(new org.apache.hadoop.conf.Configuration(), new org.apache.ambari.server.credentialapi.CredentialUtil(), args);
    }

    @org.junit.Before
    public void setupStreams() {
        java.lang.System.setOut(new java.io.PrintStream(out));
        java.lang.System.setErr(new java.io.PrintStream(err));
    }

    @org.junit.After
    public void teardownStreams() {
        java.lang.System.setOut(null);
        java.lang.System.setErr(null);
    }

    @org.junit.Test
    public void testCreateCommand() throws java.lang.Exception {
        java.lang.String alias = "javax.jdo.option.ConnectionPassword";
        java.lang.String credential = "MyTopSecretPassword";
        java.lang.String providerPath = getProviderPath("CreateCommandTest.jceks");
        java.lang.String[] args = getCreateArgs(alias, credential, providerPath);
        int exitCode = executeCommand(args);
        org.junit.Assert.assertEquals(exitCode, 0);
    }

    @org.junit.Test
    public void testCreateCommandOverwriteExisting() throws java.lang.Exception {
        java.lang.String alias = "javax.jdo.option.ConnectionPassword";
        java.lang.String credential = "MyTopSecretPassword";
        java.lang.String providerPath = getProviderPath("CreateCommandTest.jceks");
        java.lang.String[] args;
        int exitCode;
        args = getCreateArgs(alias, credential, providerPath);
        exitCode = org.apache.hadoop.util.ToolRunner.run(new org.apache.hadoop.conf.Configuration(), new org.apache.ambari.server.credentialapi.CredentialUtil(), args);
        org.junit.Assert.assertEquals(exitCode, 0);
        credential = "MyUpdatedTopSecretPassword";
        args = getUpdateArgs(alias, credential, providerPath);
        exitCode = executeCommand(args);
        org.junit.Assert.assertEquals(exitCode, 0);
    }

    @org.junit.Test
    public void testCreateCommandOverwriteNonExisting() throws java.lang.Exception {
        java.lang.String alias = "javax.jdo.option.ConnectionPassword";
        java.lang.String credential = "MyTopSecretPassword";
        java.lang.String providerPath = getProviderPath("CreateCommandTest.jceks");
        java.lang.String[] args;
        int exitCode;
        args = getUpdateArgs(alias, credential, providerPath);
        exitCode = executeCommand(args);
        org.junit.Assert.assertEquals(exitCode, 0);
    }

    @org.junit.Test
    public void testSafeCreateCommandExisting() throws java.lang.Exception {
        java.lang.String alias = "javax.jdo.option.ConnectionPassword";
        java.lang.String credential = "MyTopSecretPassword";
        java.lang.String providerPath = getProviderPath("CreateCommandTest.jceks");
        java.lang.String[] args;
        int exitCode;
        args = getCreateArgs(alias, credential, providerPath);
        exitCode = org.apache.hadoop.util.ToolRunner.run(new org.apache.hadoop.conf.Configuration(), new org.apache.ambari.server.credentialapi.CredentialUtil(), args);
        org.junit.Assert.assertEquals(exitCode, 0);
        credential = "AnotherTopSecretPassword";
        args = getSafeCreateArgs(alias, credential, providerPath);
        exitCode = executeCommand(args);
        org.junit.Assert.assertEquals(exitCode, 0);
    }

    @org.junit.Test
    public void testSafeCreateCommandNotExisting() throws java.lang.Exception {
        java.lang.String alias = "javax.jdo.option.ConnectionPassword";
        java.lang.String credential = "MyTopSecretPassword";
        java.lang.String providerPath = getProviderPath("CreateCommandTest.jceks");
        java.lang.String[] args;
        int exitCode;
        credential = "AnotherTopSecretPassword";
        args = getSafeCreateArgs(alias, credential, providerPath);
        exitCode = executeCommand(args);
        org.junit.Assert.assertEquals(exitCode, 0);
    }

    @org.junit.Test
    public void testDeleteCommandExisting() throws java.lang.Exception {
        java.lang.String alias = "javax.jdo.option.ConnectionPassword";
        java.lang.String credential = "MyTopSecretPassword";
        java.lang.String providerPath = getProviderPath("CreateCommandTest.jceks");
        java.lang.String[] args;
        int exitCode;
        args = getCreateArgs(alias, credential, providerPath);
        exitCode = executeCommand(args);
        org.junit.Assert.assertEquals(exitCode, 0);
        args = getDeleteArgs(alias, providerPath);
        exitCode = executeCommand(args);
        org.junit.Assert.assertEquals(exitCode, 0);
    }

    @org.junit.Test
    public void testDeleteCommandNonExisting() throws java.lang.Exception {
        java.lang.String alias = "javax.jdo.option.ConnectionPassword";
        java.lang.String providerPath = getProviderPath("CreateCommandTest.jceks");
        java.lang.String[] args;
        int exitCode;
        args = getDeleteArgs(alias, providerPath);
        exitCode = executeCommand(args);
        org.junit.Assert.assertEquals(exitCode, 1);
    }

    @org.junit.Test
    public void testGetCommandExisting() throws java.lang.Exception {
        java.lang.String alias = "javax.jdo.option.ConnectionPassword";
        java.lang.String credential = "MyTopSecretPassword";
        java.lang.String providerPath = getProviderPath("CreateCommandTest.jceks");
        java.lang.String[] args;
        int exitCode;
        args = getCreateArgs(alias, credential, providerPath);
        exitCode = executeCommand(args);
        org.junit.Assert.assertEquals(exitCode, 0);
        out.reset();
        args = getGetArgs(alias, providerPath);
        exitCode = executeCommand(args);
        org.junit.Assert.assertEquals(exitCode, 0);
        java.lang.String retrievedCredential = out.toString().trim();
        org.junit.Assert.assertEquals(credential, retrievedCredential);
    }

    @org.junit.Test
    public void testGetCommandNonExisting() throws java.lang.Exception {
        java.lang.String alias = "javax.jdo.option.ConnectionPassword";
        java.lang.String providerPath = getProviderPath("CreateCommandTest.jceks");
        java.lang.String[] args;
        int exitCode;
        args = getGetArgs(alias, providerPath);
        exitCode = executeCommand(args);
        org.junit.Assert.assertEquals(exitCode, 1);
    }

    @org.junit.Test
    public void testGetCommandAfterDeletion() throws java.lang.Exception {
        java.lang.String alias = "javax.jdo.option.ConnectionPassword";
        java.lang.String credential = "MyTopSecretPassword";
        java.lang.String providerPath = getProviderPath("CreateCommandTest.jceks");
        java.lang.String[] args;
        int exitCode;
        args = getCreateArgs(alias, credential, providerPath);
        exitCode = executeCommand(args);
        org.junit.Assert.assertEquals(exitCode, 0);
        args = getDeleteArgs(alias, providerPath);
        exitCode = executeCommand(args);
        org.junit.Assert.assertEquals(exitCode, 0);
        args = getGetArgs(alias, providerPath);
        exitCode = executeCommand(args);
        org.junit.Assert.assertEquals(exitCode, 1);
    }

    @org.junit.Test
    public void testGetCommandWithNonExistingProvider() throws java.lang.Exception {
        java.lang.String alias = "javax.jdo.option.ConnectionPassword";
        java.lang.String credential = "MyTopSecretPassword";
        java.lang.String providerPath = getProviderPath("CreateCommandTest.jceks");
        java.lang.String[] args;
        int exitCode;
        args = getCreateArgs(alias, credential, providerPath);
        exitCode = executeCommand(args);
        org.junit.Assert.assertEquals(exitCode, 0);
        args = getGetArgs(alias, "BadProvider.jceks");
        exitCode = executeCommand(args);
        org.junit.Assert.assertEquals(exitCode, 1);
    }

    @org.junit.Test
    public void testListCommand() throws java.lang.Exception {
        java.lang.String providerPath = getProviderPath("CreateCommandTest.jceks");
        java.lang.String[] args;
        int exitCode;
        final int numEntries = 5;
        java.util.Properties properties = new java.util.Properties();
        for (int i = 0; i < numEntries; ++i) {
            java.lang.String alias = java.lang.String.format("alias_%d", i + 1);
            java.lang.String credential = java.lang.String.format("credential_%d", i + 1);
            properties.setProperty(alias, credential);
            args = getCreateArgs(alias, credential, providerPath);
            exitCode = executeCommand(args);
            org.junit.Assert.assertEquals(exitCode, 0);
        }
        out.reset();
        args = getListArgs(providerPath);
        exitCode = executeCommand(args);
        org.junit.Assert.assertEquals(exitCode, 0);
        java.util.List<java.lang.String> aliases = java.util.Arrays.asList(out.toString().split(java.lang.System.getProperty("line.separator")));
        java.util.Enumeration enumeration = properties.propertyNames();
        while (enumeration.hasMoreElements()) {
            java.lang.String alias = ((java.lang.String) (enumeration.nextElement()));
            org.junit.Assert.assertTrue(aliases.contains(alias));
        } 
    }

    @org.junit.Test
    public void testToolUsage() throws java.lang.Exception {
        java.lang.String[] args = new java.lang.String[1];
        int exitCode;
        args[0] = "-help";
        exitCode = executeCommand(args);
        org.junit.Assert.assertEquals(exitCode, 0);
        org.junit.Assert.assertTrue(!out.toString().isEmpty());
    }

    @org.junit.Test
    public void testCreateCommandUsage() throws java.lang.Exception {
        java.lang.String[] args = new java.lang.String[2];
        int exitCode;
        args[0] = org.apache.ambari.server.credentialapi.CredentialUtilTest.CREATE_VERB;
        args[1] = "-help";
        exitCode = executeCommand(args);
        org.junit.Assert.assertEquals(exitCode, 0);
        org.junit.Assert.assertTrue(!out.toString().isEmpty());
    }

    @org.junit.Test
    public void testDeleteCommandUsage() throws java.lang.Exception {
        java.lang.String[] args = new java.lang.String[2];
        int exitCode;
        args[0] = org.apache.ambari.server.credentialapi.CredentialUtilTest.DELETE_VERB;
        args[1] = "-help";
        exitCode = executeCommand(args);
        org.junit.Assert.assertEquals(exitCode, 0);
        org.junit.Assert.assertTrue(!out.toString().isEmpty());
    }

    @org.junit.Test
    public void testListCommandUsage() throws java.lang.Exception {
        java.lang.String[] args = new java.lang.String[2];
        int exitCode;
        args[0] = org.apache.ambari.server.credentialapi.CredentialUtilTest.LIST_VERB;
        args[1] = "-help";
        exitCode = executeCommand(args);
        org.junit.Assert.assertEquals(exitCode, 0);
        org.junit.Assert.assertTrue(!out.toString().isEmpty());
    }

    @org.junit.Test
    public void testGetCommandUsage() throws java.lang.Exception {
        java.lang.String[] args = new java.lang.String[2];
        int exitCode;
        args[0] = org.apache.ambari.server.credentialapi.CredentialUtilTest.GET_VERB;
        args[1] = "-help";
        exitCode = executeCommand(args);
        org.junit.Assert.assertEquals(exitCode, 0);
        org.junit.Assert.assertTrue(!out.toString().isEmpty());
    }
}