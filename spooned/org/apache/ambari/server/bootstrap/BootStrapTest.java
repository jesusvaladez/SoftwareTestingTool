package org.apache.ambari.server.bootstrap;
public class BootStrapTest extends junit.framework.TestCase {
    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.bootstrap.BootStrapTest.class);

    public org.junit.rules.TemporaryFolder temp = new org.junit.rules.TemporaryFolder();

    @java.lang.Override
    @org.junit.Before
    public void setUp() throws java.io.IOException {
        temp.create();
    }

    @java.lang.Override
    @org.junit.After
    public void tearDown() throws java.io.IOException {
        temp.delete();
    }

    @org.junit.Test
    public void testRun() throws java.lang.Exception {
        java.util.Properties properties = new java.util.Properties();
        java.lang.String bootdir = temp.newFolder("bootdir").toString();
        java.lang.String metadetadir = temp.newFolder("metadetadir").toString();
        java.lang.String serverVersionFilePath = temp.newFolder("serverVersionFilePath").toString();
        org.apache.ambari.server.bootstrap.BootStrapTest.LOG.info("Bootdir is " + bootdir);
        org.apache.ambari.server.bootstrap.BootStrapTest.LOG.info("Metadetadir is " + metadetadir);
        org.apache.ambari.server.bootstrap.BootStrapTest.LOG.info("ServerVersionFilePath is " + serverVersionFilePath);
        java.lang.String sharedResourcesDir = "src/test/resources/";
        if (java.lang.System.getProperty("os.name").contains("Windows")) {
            sharedResourcesDir = java.lang.ClassLoader.getSystemClassLoader().getResource("").getPath();
        }
        properties.setProperty(org.apache.ambari.server.configuration.Configuration.BOOTSTRAP_DIRECTORY.getKey(), bootdir);
        properties.setProperty(org.apache.ambari.server.configuration.Configuration.BOOTSTRAP_SCRIPT.getKey(), org.apache.ambari.server.bootstrap.BootStrapTest.prepareEchoCommand(bootdir));
        properties.setProperty(org.apache.ambari.server.configuration.Configuration.SRVR_KSTR_DIR.getKey(), ("target" + java.io.File.separator) + "classes");
        properties.setProperty(org.apache.ambari.server.configuration.Configuration.METADATA_DIR_PATH.getKey(), metadetadir);
        properties.setProperty(org.apache.ambari.server.configuration.Configuration.SERVER_VERSION_FILE.getKey(), serverVersionFilePath);
        properties.setProperty(org.apache.ambari.server.configuration.Configuration.SHARED_RESOURCES_DIR.getKey(), sharedResourcesDir);
        properties.setProperty(org.apache.ambari.server.configuration.Configuration.MPACKS_V2_STAGING_DIR_PATH.getKey(), "src/main/resources/mpacks-v2");
        org.apache.ambari.server.configuration.Configuration conf = new org.apache.ambari.server.configuration.Configuration(properties);
        org.apache.ambari.server.api.services.AmbariMetaInfo ambariMetaInfo = new org.apache.ambari.server.api.services.AmbariMetaInfo(conf);
        org.apache.ambari.server.bootstrap.BootStrapImpl impl = new org.apache.ambari.server.bootstrap.BootStrapImpl(conf, ambariMetaInfo);
        impl.init();
        org.apache.ambari.server.bootstrap.SshHostInfo info = new org.apache.ambari.server.bootstrap.SshHostInfo();
        info.setSshKey("xyz");
        java.util.ArrayList<java.lang.String> hosts = new java.util.ArrayList<>();
        hosts.add("host1");
        hosts.add("host2");
        info.setUserRunAs("root");
        info.setHosts(hosts);
        info.setUser("user");
        info.setPassword("passwd");
        org.apache.ambari.server.bootstrap.BSResponse response = impl.runBootStrap(info);
        org.apache.ambari.server.bootstrap.BootStrapTest.LOG.info("Response id from bootstrap " + response.getRequestId());
        org.apache.ambari.server.bootstrap.BootStrapStatus status = impl.getStatus(response.getRequestId());
        org.apache.ambari.server.bootstrap.BootStrapTest.LOG.info("Status " + status.getStatus());
        int num = 0;
        while ((status.getStatus() == org.apache.ambari.server.bootstrap.BootStrapStatus.BSStat.RUNNING) && (num < 50)) {
            status = impl.getStatus(response.getRequestId());
            java.lang.Thread.sleep(1000);
            num++;
        } 
        java.lang.Thread.sleep(5000);
        org.apache.ambari.server.bootstrap.BootStrapTest.LOG.info((("Status: log " + status.getLog()) + " status=") + status.getStatus());
        junit.framework.Assert.assertTrue(status.getLog().contains("host1,host2"));
        junit.framework.Assert.assertEquals(org.apache.ambari.server.bootstrap.BootStrapStatus.BSStat.SUCCESS, status.getStatus());
        junit.framework.Assert.assertFalse(new java.io.File((((bootdir + java.io.File.separator) + "1") + java.io.File.separator) + "sshKey").exists());
        junit.framework.Assert.assertFalse(new java.io.File((((bootdir + java.io.File.separator) + "1") + java.io.File.separator) + "host_pass").exists());
    }

    private static java.lang.String prepareEchoCommand(java.lang.String bootdir) throws java.io.IOException {
        if (java.lang.System.getProperty("os.name").contains("Windows")) {
            java.lang.String pythonEcho = "import sys;\nif __name__ == \'__main__\':\n" + (("  args = sys.argv\n" + "  if len(args) > 1:\n") + "    print args[1]");
            java.io.File echo = new java.io.File(bootdir, "echo.py");
            echo.delete();
            org.apache.commons.io.FileUtils.writeStringToFile(echo, pythonEcho, java.nio.charset.Charset.defaultCharset());
            return echo.getPath();
        } else {
            return "echo";
        }
    }

    @org.junit.Test
    public void testHostFailure() throws java.lang.Exception {
        java.util.Properties properties = new java.util.Properties();
        java.lang.String bootdir = temp.newFolder("bootdir").toString();
        java.lang.String metadetadir = temp.newFolder("metadetadir").toString();
        java.lang.String serverVersionFilePath = temp.newFolder("serverVersionFilePath").toString();
        org.apache.ambari.server.bootstrap.BootStrapTest.LOG.info("Bootdir is " + bootdir);
        org.apache.ambari.server.bootstrap.BootStrapTest.LOG.info("Metadetadir is " + metadetadir);
        org.apache.ambari.server.bootstrap.BootStrapTest.LOG.info("ServerVersionFilePath is " + serverVersionFilePath);
        java.lang.String sharedResourcesDir = "src/test/resources/";
        java.lang.String serverKSTRDir = ("target" + java.io.File.separator) + "classes";
        java.lang.String mpacksv2staging = "src/main/resources/mpacks-v2";
        if (java.lang.System.getProperty("os.name").contains("Windows")) {
            sharedResourcesDir = java.lang.ClassLoader.getSystemClassLoader().getResource("").getPath();
            serverKSTRDir = new java.io.File(new java.io.File(java.lang.ClassLoader.getSystemClassLoader().getResource("").getPath()).getParent(), "classes").getPath();
        }
        properties.setProperty(org.apache.ambari.server.configuration.Configuration.BOOTSTRAP_DIRECTORY.getKey(), bootdir);
        properties.setProperty(org.apache.ambari.server.configuration.Configuration.BOOTSTRAP_SCRIPT.getKey(), org.apache.ambari.server.bootstrap.BootStrapTest.prepareEchoCommand(bootdir));
        properties.setProperty(org.apache.ambari.server.configuration.Configuration.SRVR_KSTR_DIR.getKey(), serverKSTRDir);
        properties.setProperty(org.apache.ambari.server.configuration.Configuration.METADATA_DIR_PATH.getKey(), metadetadir);
        properties.setProperty(org.apache.ambari.server.configuration.Configuration.SERVER_VERSION_FILE.getKey(), serverVersionFilePath);
        properties.setProperty(org.apache.ambari.server.configuration.Configuration.SHARED_RESOURCES_DIR.getKey(), sharedResourcesDir);
        properties.setProperty(org.apache.ambari.server.configuration.Configuration.MPACKS_V2_STAGING_DIR_PATH.getKey(), mpacksv2staging);
        org.apache.ambari.server.configuration.Configuration conf = new org.apache.ambari.server.configuration.Configuration(properties);
        org.apache.ambari.server.api.services.AmbariMetaInfo ambariMetaInfo = new org.apache.ambari.server.api.services.AmbariMetaInfo(conf);
        org.apache.ambari.server.bootstrap.BootStrapImpl impl = new org.apache.ambari.server.bootstrap.BootStrapImpl(conf, ambariMetaInfo);
        impl.init();
        org.apache.ambari.server.bootstrap.SshHostInfo info = new org.apache.ambari.server.bootstrap.SshHostInfo();
        info.setSshKey("xyz");
        java.util.ArrayList<java.lang.String> hosts = new java.util.ArrayList<>();
        hosts.add("host1");
        hosts.add("host2");
        info.setHosts(hosts);
        info.setUser("user");
        info.setUserRunAs("root");
        info.setPassword("passwd");
        org.apache.ambari.server.bootstrap.BSResponse response = impl.runBootStrap(info);
        long requestId = response.getRequestId();
        org.apache.ambari.server.bootstrap.BootStrapTest.LOG.info("Response id from bootstrap " + requestId);
        java.io.File requestDir = new java.io.File(bootdir, java.lang.Long.toString(requestId));
        int num = 0;
        while ((!requestDir.exists()) && (num < 500)) {
            java.lang.Thread.sleep(100);
            num++;
        } 
        if (!requestDir.exists()) {
            org.apache.ambari.server.bootstrap.BootStrapTest.LOG.warn("RequestDir does not exists");
        }
        org.apache.commons.io.FileUtils.writeStringToFile(new java.io.File(requestDir, "host1.done"), "0", java.nio.charset.Charset.defaultCharset());
        org.apache.commons.io.FileUtils.writeStringToFile(new java.io.File(requestDir, "host2.done"), "1", java.nio.charset.Charset.defaultCharset());
        org.apache.ambari.server.bootstrap.BootStrapStatus status = impl.getStatus(response.getRequestId());
        org.apache.ambari.server.bootstrap.BootStrapTest.LOG.info("Status " + status.getStatus());
        num = 0;
        while ((status.getStatus() == org.apache.ambari.server.bootstrap.BootStrapStatus.BSStat.RUNNING) && (num < 500)) {
            status = impl.getStatus(response.getRequestId());
            java.lang.Thread.sleep(100);
            num++;
        } 
        org.apache.ambari.server.bootstrap.BootStrapTest.LOG.info((("Status: log " + status.getLog()) + " status=") + status.getStatus());
        junit.framework.Assert.assertTrue(status.getLog().contains("host1,host2"));
        junit.framework.Assert.assertEquals(org.apache.ambari.server.bootstrap.BootStrapStatus.BSStat.ERROR, status.getStatus());
        junit.framework.Assert.assertEquals("DONE", status.getHostsStatus().get(0).getStatus());
        junit.framework.Assert.assertEquals("FAILED", status.getHostsStatus().get(1).getStatus());
    }

    @org.junit.Test
    public void testPolling() throws java.lang.Exception {
        java.io.File tmpFolder = temp.newFolder("bootstrap");
        org.apache.commons.io.FileUtils.writeStringToFile(new java.io.File(tmpFolder, "host1.done"), "0", java.nio.charset.Charset.defaultCharset());
        org.apache.commons.io.FileUtils.writeStringToFile(new java.io.File(tmpFolder, "host1.log"), "err_log_1", java.nio.charset.Charset.defaultCharset());
        org.apache.commons.io.FileUtils.writeStringToFile(new java.io.File(tmpFolder, "host2.done"), "1", java.nio.charset.Charset.defaultCharset());
        org.apache.commons.io.FileUtils.writeStringToFile(new java.io.File(tmpFolder, "host2.log"), "err_log_2", java.nio.charset.Charset.defaultCharset());
        java.util.List<java.lang.String> listHosts = new java.util.ArrayList<>();
        listHosts.add("host1");
        listHosts.add("host2");
        org.apache.ambari.server.bootstrap.BSHostStatusCollector collector = new org.apache.ambari.server.bootstrap.BSHostStatusCollector(tmpFolder, listHosts);
        collector.run();
        java.util.List<org.apache.ambari.server.bootstrap.BSHostStatus> polledHostStatus = collector.getHostStatus();
        junit.framework.Assert.assertTrue(polledHostStatus.size() == 2);
        junit.framework.Assert.assertEquals(polledHostStatus.get(0).getHostName(), "host1");
        junit.framework.Assert.assertEquals(polledHostStatus.get(0).getLog(), "err_log_1");
        junit.framework.Assert.assertEquals(polledHostStatus.get(0).getStatus(), "DONE");
        junit.framework.Assert.assertEquals(polledHostStatus.get(1).getHostName(), "host2");
        junit.framework.Assert.assertEquals(polledHostStatus.get(1).getLog(), "err_log_2");
        junit.framework.Assert.assertEquals(polledHostStatus.get(1).getStatus(), "FAILED");
    }
}