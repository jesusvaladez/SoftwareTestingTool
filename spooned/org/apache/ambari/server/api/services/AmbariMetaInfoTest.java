package org.apache.ambari.server.api.services;
import javax.persistence.EntityManager;
import org.apache.commons.lang.StringUtils;
import static org.easymock.EasyMock.createNiceMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
public class AmbariMetaInfoTest {
    private static final java.lang.String STACK_NAME_HDP = "HDP";

    private static final java.lang.String STACK_VERSION_HDP = "0.1";

    private static final java.lang.String EXT_STACK_NAME = "2.0.6";

    private static final java.lang.String STACK_VERSION_HDP_02 = "0.2";

    private static final java.lang.String STACK_MINIMAL_VERSION_HDP = "0.0";

    private static final java.lang.String SERVICE_NAME_HDFS = "HDFS";

    private static final java.lang.String SERVICE_NAME_MAPRED2 = "MAPREDUCE2";

    private static final java.lang.String SERVICE_COMPONENT_NAME = "NAMENODE";

    private static final java.lang.String OS_TYPE = "centos5";

    private static final java.lang.String REPO_ID = "HDP-UTILS-1.1.0.15";

    private static final java.lang.String PROPERTY_NAME = "hbase.regionserver.msginterval";

    private static final java.lang.String SHARED_PROPERTY_NAME = "content";

    private static final java.lang.String NON_EXT_VALUE = "XXX";

    private static final int REPOS_CNT = 3;

    private static final int PROPERTIES_CNT = 64;

    private static final int OS_CNT = 4;

    private static org.apache.ambari.server.api.services.AmbariMetaInfoTest.TestAmbariMetaInfo metaInfo = null;

    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.api.services.AmbariMetaInfoTest.class);

    private static final java.lang.String FILE_NAME = "hbase-site.xml";

    private static final java.lang.String HADOOP_ENV_FILE_NAME = "hadoop-env.xml";

    private static final java.lang.String HDFS_LOG4J_FILE_NAME = "hdfs-log4j.xml";

    @org.junit.Rule
    public org.junit.rules.TemporaryFolder tmpFolder = new org.junit.rules.TemporaryFolder();

    @org.junit.BeforeClass
    public static void beforeClass() throws java.lang.Exception {
        java.io.File stacks = new java.io.File("src/test/resources/stacks");
        java.io.File version = new java.io.File("src/test/resources/version");
        java.io.File resourcesRoot = new java.io.File("src/test/resources/");
        if (java.lang.System.getProperty("os.name").contains("Windows")) {
            stacks = new java.io.File(java.lang.ClassLoader.getSystemClassLoader().getResource("stacks").getPath());
            version = new java.io.File(new java.io.File(java.lang.ClassLoader.getSystemClassLoader().getResource("").getPath()).getParent(), "version");
        }
        org.apache.ambari.server.api.services.AmbariMetaInfoTest.metaInfo = org.apache.ambari.server.api.services.AmbariMetaInfoTest.createAmbariMetaInfo(stacks, version, resourcesRoot);
    }

    @org.junit.AfterClass
    public static void tearDown() throws java.lang.Exception {
        org.apache.ambari.server.H2DatabaseCleaner.clearDatabase(org.apache.ambari.server.api.services.AmbariMetaInfoTest.metaInfo.injector.getProvider(javax.persistence.EntityManager.class).get());
    }

    public class MockModule extends com.google.inject.AbstractModule {
        @java.lang.Override
        protected void configure() {
            bind(org.apache.ambari.server.metadata.ActionMetadata.class);
        }
    }

    @org.junit.Test
    public void getRestartRequiredServicesNames() throws org.apache.ambari.server.AmbariException {
        java.util.Set<java.lang.String> res = org.apache.ambari.server.api.services.AmbariMetaInfoTest.metaInfo.getRestartRequiredServicesNames(org.apache.ambari.server.api.services.AmbariMetaInfoTest.STACK_NAME_HDP, "2.0.7");
        org.junit.Assert.assertEquals(1, res.size());
    }

    @org.junit.Test
    public void testGetRackSensitiveServicesNames() throws org.apache.ambari.server.AmbariException {
        java.util.Set<java.lang.String> res = org.apache.ambari.server.api.services.AmbariMetaInfoTest.metaInfo.getRackSensitiveServicesNames(org.apache.ambari.server.api.services.AmbariMetaInfoTest.STACK_NAME_HDP, "2.0.7");
        org.junit.Assert.assertEquals(1, res.size());
        org.junit.Assert.assertEquals("HDFS", res.iterator().next());
    }

    @org.junit.Test
    public void getComponentsByService() throws org.apache.ambari.server.AmbariException {
        java.util.List<org.apache.ambari.server.state.ComponentInfo> components = org.apache.ambari.server.api.services.AmbariMetaInfoTest.metaInfo.getComponentsByService(org.apache.ambari.server.api.services.AmbariMetaInfoTest.STACK_NAME_HDP, org.apache.ambari.server.api.services.AmbariMetaInfoTest.STACK_VERSION_HDP, org.apache.ambari.server.api.services.AmbariMetaInfoTest.SERVICE_NAME_HDFS);
        org.junit.Assert.assertNotNull(components);
        org.junit.Assert.assertTrue(components.size() > 0);
    }

    @org.junit.Test
    public void getLogs() throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.server.state.ComponentInfo component;
        component = org.apache.ambari.server.api.services.AmbariMetaInfoTest.metaInfo.getComponent(org.apache.ambari.server.api.services.AmbariMetaInfoTest.STACK_NAME_HDP, "2.1.1", org.apache.ambari.server.api.services.AmbariMetaInfoTest.SERVICE_NAME_HDFS, "NAMENODE");
        org.junit.Assert.assertNotNull(component);
        org.junit.Assert.assertNotNull(component.getLogs());
        org.junit.Assert.assertTrue(component.getLogs().size() == 2);
        org.junit.Assert.assertEquals(component.getLogs().get(0).getLogId(), "hdfs_namenode");
        org.junit.Assert.assertEquals(component.getLogs().get(1).getLogId(), "hdfs_audit");
        org.junit.Assert.assertTrue(component.getLogs().get(0).isPrimary());
        org.junit.Assert.assertFalse(component.getLogs().get(1).isPrimary());
        component = org.apache.ambari.server.api.services.AmbariMetaInfoTest.metaInfo.getComponent(org.apache.ambari.server.api.services.AmbariMetaInfoTest.STACK_NAME_HDP, "2.1.1", org.apache.ambari.server.api.services.AmbariMetaInfoTest.SERVICE_NAME_HDFS, "DATANODE");
        org.junit.Assert.assertNotNull(component);
        org.junit.Assert.assertNotNull(component.getLogs());
        org.junit.Assert.assertTrue(component.getLogs().size() == 1);
        org.junit.Assert.assertEquals(component.getLogs().get(0).getLogId(), "hdfs_datanode");
        org.junit.Assert.assertTrue(component.getLogs().get(0).isPrimary());
        component = org.apache.ambari.server.api.services.AmbariMetaInfoTest.metaInfo.getComponent(org.apache.ambari.server.api.services.AmbariMetaInfoTest.STACK_NAME_HDP, "2.1.1", org.apache.ambari.server.api.services.AmbariMetaInfoTest.SERVICE_NAME_HDFS, "HDFS_CLIENT");
        org.junit.Assert.assertNotNull(component);
        org.junit.Assert.assertNotNull(component.getLogs());
        org.junit.Assert.assertTrue(component.getLogs().isEmpty());
    }

    @org.junit.Test
    public void getRepository() throws org.apache.ambari.server.AmbariException {
        java.util.Map<java.lang.String, java.util.List<org.apache.ambari.server.state.RepositoryInfo>> repository = org.apache.ambari.server.api.services.AmbariMetaInfoTest.metaInfo.getRepository(org.apache.ambari.server.api.services.AmbariMetaInfoTest.STACK_NAME_HDP, org.apache.ambari.server.api.services.AmbariMetaInfoTest.STACK_VERSION_HDP);
        org.junit.Assert.assertNotNull(repository);
        org.junit.Assert.assertFalse(repository.get("centos5").isEmpty());
        org.junit.Assert.assertFalse(repository.get("centos6").isEmpty());
    }

    @org.junit.Test
    @org.junit.Ignore
    public void testGetRepositoryDefault() throws java.lang.Exception {
        java.lang.String buildDir = tmpFolder.getRoot().getAbsolutePath();
        org.apache.ambari.server.api.services.AmbariMetaInfo ambariMetaInfo = setupTempAmbariMetaInfo(buildDir);
        ambariMetaInfo.init();
        org.apache.ambari.server.api.services.AmbariMetaInfoTest.waitForAllReposToBeResolved(ambariMetaInfo);
        java.util.List<org.apache.ambari.server.state.RepositoryInfo> redhat6Repo = ambariMetaInfo.getRepositories(org.apache.ambari.server.api.services.AmbariMetaInfoTest.STACK_NAME_HDP, "2.1.1", "redhat6");
        org.junit.Assert.assertNotNull(redhat6Repo);
        for (org.apache.ambari.server.state.RepositoryInfo ri : redhat6Repo) {
            if (org.apache.ambari.server.api.services.AmbariMetaInfoTest.STACK_NAME_HDP.equals(ri.getRepoName())) {
                org.junit.Assert.assertTrue(ri.getBaseUrl().equals(ri.getDefaultBaseUrl()));
            }
        }
    }

    @org.junit.Test
    public void testGetRepositoryNoInternetDefault() throws java.lang.Exception {
        java.lang.String buildDir = tmpFolder.getRoot().getAbsolutePath();
        setupTempAmbariMetaInfoDirs(buildDir);
        java.io.File latestUrlFile = new java.io.File(buildDir, "ambari-metaInfo/HDP/2.1.1/repos/hdp.json");
        if (java.lang.System.getProperty("os.name").contains("Windows")) {
            latestUrlFile.deleteOnExit();
        } else {
            org.apache.commons.io.FileUtils.deleteQuietly(latestUrlFile);
            org.junit.Assert.assertTrue(!latestUrlFile.exists());
        }
        org.apache.ambari.server.api.services.AmbariMetaInfo ambariMetaInfo = setupTempAmbariMetaInfoExistingDirs(buildDir);
        java.util.List<org.apache.ambari.server.state.RepositoryInfo> redhat6Repo = ambariMetaInfo.getRepositories(org.apache.ambari.server.api.services.AmbariMetaInfoTest.STACK_NAME_HDP, "2.1.1", "redhat6");
        org.junit.Assert.assertNotNull(redhat6Repo);
        for (org.apache.ambari.server.state.RepositoryInfo ri : redhat6Repo) {
            if (org.apache.ambari.server.api.services.AmbariMetaInfoTest.STACK_NAME_HDP.equals(ri.getRepoName())) {
                org.junit.Assert.assertEquals(ri.getBaseUrl(), ri.getDefaultBaseUrl());
            }
        }
    }

    @org.junit.Test
    public void isSupportedStack() throws org.apache.ambari.server.AmbariException {
        boolean supportedStack = org.apache.ambari.server.api.services.AmbariMetaInfoTest.metaInfo.isSupportedStack(org.apache.ambari.server.api.services.AmbariMetaInfoTest.STACK_NAME_HDP, org.apache.ambari.server.api.services.AmbariMetaInfoTest.STACK_VERSION_HDP);
        org.junit.Assert.assertTrue(supportedStack);
        boolean notSupportedStack = org.apache.ambari.server.api.services.AmbariMetaInfoTest.metaInfo.isSupportedStack(org.apache.ambari.server.api.services.AmbariMetaInfoTest.NON_EXT_VALUE, org.apache.ambari.server.api.services.AmbariMetaInfoTest.NON_EXT_VALUE);
        org.junit.Assert.assertFalse(notSupportedStack);
    }

    @org.junit.Test
    public void isValidService() throws org.apache.ambari.server.AmbariException {
        boolean valid = org.apache.ambari.server.api.services.AmbariMetaInfoTest.metaInfo.isValidService(org.apache.ambari.server.api.services.AmbariMetaInfoTest.STACK_NAME_HDP, org.apache.ambari.server.api.services.AmbariMetaInfoTest.STACK_VERSION_HDP, org.apache.ambari.server.api.services.AmbariMetaInfoTest.SERVICE_NAME_HDFS);
        org.junit.Assert.assertTrue(valid);
        boolean invalid = org.apache.ambari.server.api.services.AmbariMetaInfoTest.metaInfo.isValidService(org.apache.ambari.server.api.services.AmbariMetaInfoTest.STACK_NAME_HDP, org.apache.ambari.server.api.services.AmbariMetaInfoTest.STACK_VERSION_HDP, org.apache.ambari.server.api.services.AmbariMetaInfoTest.NON_EXT_VALUE);
        org.junit.Assert.assertFalse(invalid);
    }

    @org.junit.Test
    public void isServiceWithNoConfigs() throws org.apache.ambari.server.AmbariException {
        org.junit.Assert.assertTrue(org.apache.ambari.server.api.services.AmbariMetaInfoTest.metaInfo.isServiceWithNoConfigs(org.apache.ambari.server.api.services.AmbariMetaInfoTest.STACK_NAME_HDP, "2.0.8", "SYSTEMML"));
        org.junit.Assert.assertTrue(org.apache.ambari.server.api.services.AmbariMetaInfoTest.metaInfo.isServiceWithNoConfigs(org.apache.ambari.server.api.services.AmbariMetaInfoTest.STACK_NAME_HDP, "2.0.7", "SYSTEMML"));
        org.junit.Assert.assertFalse(org.apache.ambari.server.api.services.AmbariMetaInfoTest.metaInfo.isServiceWithNoConfigs(org.apache.ambari.server.api.services.AmbariMetaInfoTest.STACK_NAME_HDP, "2.0.8", "HIVE"));
    }

    @org.junit.Test
    public void testServiceNameUsingComponentName() throws org.apache.ambari.server.AmbariException {
        java.lang.String serviceName = org.apache.ambari.server.api.services.AmbariMetaInfoTest.metaInfo.getComponentToService(org.apache.ambari.server.api.services.AmbariMetaInfoTest.STACK_NAME_HDP, org.apache.ambari.server.api.services.AmbariMetaInfoTest.STACK_VERSION_HDP, org.apache.ambari.server.api.services.AmbariMetaInfoTest.SERVICE_COMPONENT_NAME);
        org.junit.Assert.assertEquals("HDFS", serviceName);
    }

    @org.junit.Test
    public void getServices() throws org.apache.ambari.server.AmbariException {
        java.util.Map<java.lang.String, org.apache.ambari.server.state.ServiceInfo> services = org.apache.ambari.server.api.services.AmbariMetaInfoTest.metaInfo.getServices(org.apache.ambari.server.api.services.AmbariMetaInfoTest.STACK_NAME_HDP, org.apache.ambari.server.api.services.AmbariMetaInfoTest.STACK_VERSION_HDP);
        org.apache.ambari.server.api.services.AmbariMetaInfoTest.LOG.info("Getting all the services ");
        for (java.util.Map.Entry<java.lang.String, org.apache.ambari.server.state.ServiceInfo> entry : services.entrySet()) {
            org.apache.ambari.server.api.services.AmbariMetaInfoTest.LOG.info((("Service Name " + entry.getKey()) + " values ") + entry.getValue());
        }
        org.junit.Assert.assertTrue(services.containsKey("HDFS"));
        org.junit.Assert.assertTrue(services.containsKey("MAPREDUCE"));
        org.junit.Assert.assertNotNull(services);
        org.junit.Assert.assertFalse(services.keySet().size() == 0);
    }

    @org.junit.Test
    public void getServiceInfo() throws java.lang.Exception {
        org.apache.ambari.server.state.ServiceInfo si = org.apache.ambari.server.api.services.AmbariMetaInfoTest.metaInfo.getService(org.apache.ambari.server.api.services.AmbariMetaInfoTest.STACK_NAME_HDP, org.apache.ambari.server.api.services.AmbariMetaInfoTest.STACK_VERSION_HDP, org.apache.ambari.server.api.services.AmbariMetaInfoTest.SERVICE_NAME_HDFS);
        org.junit.Assert.assertNotNull(si);
    }

    @org.junit.Test
    public void testConfigDependencies() throws java.lang.Exception {
        org.apache.ambari.server.state.ServiceInfo serviceInfo = org.apache.ambari.server.api.services.AmbariMetaInfoTest.metaInfo.getService(org.apache.ambari.server.api.services.AmbariMetaInfoTest.STACK_NAME_HDP, org.apache.ambari.server.api.services.AmbariMetaInfoTest.EXT_STACK_NAME, org.apache.ambari.server.api.services.AmbariMetaInfoTest.SERVICE_NAME_MAPRED2);
        org.junit.Assert.assertNotNull(serviceInfo);
        org.junit.Assert.assertTrue(!serviceInfo.getConfigDependencies().isEmpty());
    }

    @org.junit.Test
    public void testGetRepos() throws java.lang.Exception {
        java.util.Map<java.lang.String, java.util.List<org.apache.ambari.server.state.RepositoryInfo>> repos = org.apache.ambari.server.api.services.AmbariMetaInfoTest.metaInfo.getRepository(org.apache.ambari.server.api.services.AmbariMetaInfoTest.STACK_NAME_HDP, org.apache.ambari.server.api.services.AmbariMetaInfoTest.STACK_VERSION_HDP);
        java.util.Set<java.lang.String> centos5Cnt = new java.util.HashSet<>();
        java.util.Set<java.lang.String> centos6Cnt = new java.util.HashSet<>();
        java.util.Set<java.lang.String> redhat6cnt = new java.util.HashSet<>();
        java.util.Set<java.lang.String> redhat5cnt = new java.util.HashSet<>();
        for (java.util.List<org.apache.ambari.server.state.RepositoryInfo> vals : repos.values()) {
            for (org.apache.ambari.server.state.RepositoryInfo repo : vals) {
                org.apache.ambari.server.api.services.AmbariMetaInfoTest.LOG.debug("Dumping repo info : {}", repo);
                if (repo.getOsType().equals("centos5")) {
                    centos5Cnt.add(repo.getRepoId());
                } else if (repo.getOsType().equals("centos6")) {
                    centos6Cnt.add(repo.getRepoId());
                } else if (repo.getOsType().equals("redhat6")) {
                    redhat6cnt.add(repo.getRepoId());
                } else if (repo.getOsType().equals("redhat5")) {
                    redhat5cnt.add(repo.getRepoId());
                } else {
                    org.junit.Assert.fail("Found invalid os " + repo.getOsType());
                }
                if (repo.getRepoId().equals("epel")) {
                    org.junit.Assert.assertFalse(repo.getMirrorsList().isEmpty());
                    org.junit.Assert.assertNull(repo.getBaseUrl());
                } else {
                    org.junit.Assert.assertNull(repo.getMirrorsList());
                    org.junit.Assert.assertFalse(repo.getBaseUrl().isEmpty());
                }
            }
        }
        org.junit.Assert.assertEquals(3, centos5Cnt.size());
        org.junit.Assert.assertEquals(3, redhat6cnt.size());
        org.junit.Assert.assertEquals(3, redhat5cnt.size());
        org.junit.Assert.assertEquals(3, centos6Cnt.size());
    }

    @org.junit.Test
    public void testGlobalMapping() throws java.lang.Exception {
        org.apache.ambari.server.state.ServiceInfo sinfo = org.apache.ambari.server.api.services.AmbariMetaInfoTest.metaInfo.getService("HDP", "0.2", "HDFS");
        java.util.List<org.apache.ambari.server.state.PropertyInfo> pinfo = sinfo.getProperties();
        boolean checkforglobal = false;
        for (org.apache.ambari.server.state.PropertyInfo pinfol : pinfo) {
            if ("global.xml".equals(pinfol.getFilename())) {
                checkforglobal = true;
            }
        }
        junit.framework.Assert.assertTrue(checkforglobal);
        sinfo = org.apache.ambari.server.api.services.AmbariMetaInfoTest.metaInfo.getService("HDP", "0.2", "MAPREDUCE");
        boolean checkforhadoopheapsize = false;
        pinfo = sinfo.getProperties();
        for (org.apache.ambari.server.state.PropertyInfo pinfol : pinfo) {
            if ("global.xml".equals(pinfol.getFilename())) {
                if ("hadoop_heapsize".equals(pinfol.getName())) {
                    checkforhadoopheapsize = true;
                }
            }
        }
        junit.framework.Assert.assertTrue(checkforhadoopheapsize);
    }

    @org.junit.Test
    public void testMetaInfoFileFilter() throws java.lang.Exception {
        java.lang.String buildDir = tmpFolder.getRoot().getAbsolutePath();
        java.io.File stackRoot = new java.io.File("src/test/resources/stacks");
        java.io.File version = new java.io.File("src/test/resources/version");
        if (java.lang.System.getProperty("os.name").contains("Windows")) {
            stackRoot = new java.io.File(java.lang.ClassLoader.getSystemClassLoader().getResource("stacks").getPath());
            version = new java.io.File(new java.io.File(java.lang.ClassLoader.getSystemClassLoader().getResource("").getPath()).getParent(), "version");
        }
        java.io.File stackRootTmp = getStackRootTmp(buildDir);
        stackRootTmp.mkdir();
        org.apache.commons.io.FileUtils.copyDirectory(stackRoot, stackRootTmp);
        java.io.File f1;
        java.io.File f2;
        java.io.File f3;
        f1 = new java.io.File(stackRootTmp.getAbsolutePath() + "/001.svn");
        f1.createNewFile();
        f2 = new java.io.File(stackRootTmp.getAbsolutePath() + "/abcd.svn/001.svn");
        f2.mkdirs();
        f2.createNewFile();
        f3 = new java.io.File(stackRootTmp.getAbsolutePath() + "/.svn");
        if (!f3.exists()) {
            f3.createNewFile();
        }
        org.apache.ambari.server.api.services.AmbariMetaInfo ambariMetaInfo = org.apache.ambari.server.api.services.AmbariMetaInfoTest.createAmbariMetaInfo(stackRootTmp, version, new java.io.File(""));
        getServices();
        getComponentsByService();
        junit.framework.Assert.assertNotNull(ambariMetaInfo.getStack("abcd.svn", "001.svn"));
        junit.framework.Assert.assertFalse(ambariMetaInfo.isSupportedStack(".svn", ""));
        junit.framework.Assert.assertFalse(ambariMetaInfo.isSupportedStack(".svn", ""));
    }

    @org.junit.Test
    public void testGetComponent() throws java.lang.Exception {
        org.apache.ambari.server.state.ComponentInfo component = org.apache.ambari.server.api.services.AmbariMetaInfoTest.metaInfo.getComponent(org.apache.ambari.server.api.services.AmbariMetaInfoTest.STACK_NAME_HDP, org.apache.ambari.server.api.services.AmbariMetaInfoTest.STACK_VERSION_HDP, org.apache.ambari.server.api.services.AmbariMetaInfoTest.SERVICE_NAME_HDFS, org.apache.ambari.server.api.services.AmbariMetaInfoTest.SERVICE_COMPONENT_NAME);
        junit.framework.Assert.assertEquals(component.getName(), org.apache.ambari.server.api.services.AmbariMetaInfoTest.SERVICE_COMPONENT_NAME);
        try {
            org.apache.ambari.server.api.services.AmbariMetaInfoTest.metaInfo.getComponent(org.apache.ambari.server.api.services.AmbariMetaInfoTest.STACK_NAME_HDP, org.apache.ambari.server.api.services.AmbariMetaInfoTest.STACK_VERSION_HDP, org.apache.ambari.server.api.services.AmbariMetaInfoTest.SERVICE_NAME_HDFS, org.apache.ambari.server.api.services.AmbariMetaInfoTest.NON_EXT_VALUE);
        } catch (org.apache.ambari.server.StackAccessException e) {
        }
    }

    @org.junit.Test
    public void testGetRepositories() throws java.lang.Exception {
        java.util.List<org.apache.ambari.server.state.RepositoryInfo> repositories = org.apache.ambari.server.api.services.AmbariMetaInfoTest.metaInfo.getRepositories(org.apache.ambari.server.api.services.AmbariMetaInfoTest.STACK_NAME_HDP, org.apache.ambari.server.api.services.AmbariMetaInfoTest.STACK_VERSION_HDP, org.apache.ambari.server.api.services.AmbariMetaInfoTest.OS_TYPE);
        junit.framework.Assert.assertEquals(repositories.size(), org.apache.ambari.server.api.services.AmbariMetaInfoTest.REPOS_CNT);
    }

    @org.junit.Test
    public void testGetRepository() throws java.lang.Exception {
        org.apache.ambari.server.state.RepositoryInfo repository = org.apache.ambari.server.api.services.AmbariMetaInfoTest.metaInfo.getRepository(org.apache.ambari.server.api.services.AmbariMetaInfoTest.STACK_NAME_HDP, org.apache.ambari.server.api.services.AmbariMetaInfoTest.STACK_VERSION_HDP, org.apache.ambari.server.api.services.AmbariMetaInfoTest.OS_TYPE, org.apache.ambari.server.api.services.AmbariMetaInfoTest.REPO_ID);
        junit.framework.Assert.assertEquals(repository.getRepoId(), org.apache.ambari.server.api.services.AmbariMetaInfoTest.REPO_ID);
        try {
            org.apache.ambari.server.api.services.AmbariMetaInfoTest.metaInfo.getRepository(org.apache.ambari.server.api.services.AmbariMetaInfoTest.STACK_NAME_HDP, org.apache.ambari.server.api.services.AmbariMetaInfoTest.STACK_VERSION_HDP, org.apache.ambari.server.api.services.AmbariMetaInfoTest.OS_TYPE, org.apache.ambari.server.api.services.AmbariMetaInfoTest.NON_EXT_VALUE);
        } catch (org.apache.ambari.server.StackAccessException e) {
        }
    }

    @org.junit.Test
    public void testGetService() throws java.lang.Exception {
        org.apache.ambari.server.state.ServiceInfo service = org.apache.ambari.server.api.services.AmbariMetaInfoTest.metaInfo.getService(org.apache.ambari.server.api.services.AmbariMetaInfoTest.STACK_NAME_HDP, org.apache.ambari.server.api.services.AmbariMetaInfoTest.STACK_VERSION_HDP, org.apache.ambari.server.api.services.AmbariMetaInfoTest.SERVICE_NAME_HDFS);
        junit.framework.Assert.assertEquals(service.getName(), org.apache.ambari.server.api.services.AmbariMetaInfoTest.SERVICE_NAME_HDFS);
        try {
            org.apache.ambari.server.api.services.AmbariMetaInfoTest.metaInfo.getService(org.apache.ambari.server.api.services.AmbariMetaInfoTest.STACK_NAME_HDP, org.apache.ambari.server.api.services.AmbariMetaInfoTest.STACK_VERSION_HDP, org.apache.ambari.server.api.services.AmbariMetaInfoTest.NON_EXT_VALUE);
        } catch (org.apache.ambari.server.StackAccessException e) {
        }
    }

    @org.junit.Test
    public void testGetStacks() {
    }

    @org.junit.Test
    public void testGetStackInfo() throws java.lang.Exception {
        org.apache.ambari.server.state.StackInfo stackInfo = org.apache.ambari.server.api.services.AmbariMetaInfoTest.metaInfo.getStack(org.apache.ambari.server.api.services.AmbariMetaInfoTest.STACK_NAME_HDP, org.apache.ambari.server.api.services.AmbariMetaInfoTest.STACK_VERSION_HDP);
        junit.framework.Assert.assertEquals(stackInfo.getName(), org.apache.ambari.server.api.services.AmbariMetaInfoTest.STACK_NAME_HDP);
        junit.framework.Assert.assertEquals(stackInfo.getVersion(), org.apache.ambari.server.api.services.AmbariMetaInfoTest.STACK_VERSION_HDP);
        try {
            org.apache.ambari.server.api.services.AmbariMetaInfoTest.metaInfo.getStack(org.apache.ambari.server.api.services.AmbariMetaInfoTest.STACK_NAME_HDP, org.apache.ambari.server.api.services.AmbariMetaInfoTest.NON_EXT_VALUE);
        } catch (org.apache.ambari.server.StackAccessException e) {
        }
    }

    @org.junit.Test
    public void testGetStackParentVersions() throws java.lang.Exception {
        java.util.List<java.lang.String> parents = org.apache.ambari.server.api.services.AmbariMetaInfoTest.metaInfo.getStackParentVersions(org.apache.ambari.server.api.services.AmbariMetaInfoTest.STACK_NAME_HDP, "2.0.8");
        junit.framework.Assert.assertEquals(3, parents.size());
        junit.framework.Assert.assertEquals("2.0.7", parents.get(0));
        junit.framework.Assert.assertEquals("2.0.6", parents.get(1));
        junit.framework.Assert.assertEquals("2.0.5", parents.get(2));
    }

    @org.junit.Test
    public void testGetProperties() throws java.lang.Exception {
        java.util.Set<org.apache.ambari.server.state.PropertyInfo> properties = org.apache.ambari.server.api.services.AmbariMetaInfoTest.metaInfo.getServiceProperties(org.apache.ambari.server.api.services.AmbariMetaInfoTest.STACK_NAME_HDP, org.apache.ambari.server.api.services.AmbariMetaInfoTest.STACK_VERSION_HDP, org.apache.ambari.server.api.services.AmbariMetaInfoTest.SERVICE_NAME_HDFS);
        junit.framework.Assert.assertEquals(properties.size(), org.apache.ambari.server.api.services.AmbariMetaInfoTest.PROPERTIES_CNT);
    }

    @org.junit.Test
    public void testGetPropertiesNoName() throws java.lang.Exception {
        java.util.Set<org.apache.ambari.server.state.PropertyInfo> properties = org.apache.ambari.server.api.services.AmbariMetaInfoTest.metaInfo.getPropertiesByName(org.apache.ambari.server.api.services.AmbariMetaInfoTest.STACK_NAME_HDP, org.apache.ambari.server.api.services.AmbariMetaInfoTest.STACK_VERSION_HDP, org.apache.ambari.server.api.services.AmbariMetaInfoTest.SERVICE_NAME_HDFS, org.apache.ambari.server.api.services.AmbariMetaInfoTest.PROPERTY_NAME);
        junit.framework.Assert.assertEquals(1, properties.size());
        for (org.apache.ambari.server.state.PropertyInfo propertyInfo : properties) {
            junit.framework.Assert.assertEquals(org.apache.ambari.server.api.services.AmbariMetaInfoTest.PROPERTY_NAME, propertyInfo.getName());
            junit.framework.Assert.assertEquals(org.apache.ambari.server.api.services.AmbariMetaInfoTest.FILE_NAME, propertyInfo.getFilename());
        }
        try {
            org.apache.ambari.server.api.services.AmbariMetaInfoTest.metaInfo.getPropertiesByName(org.apache.ambari.server.api.services.AmbariMetaInfoTest.STACK_NAME_HDP, org.apache.ambari.server.api.services.AmbariMetaInfoTest.STACK_VERSION_HDP, org.apache.ambari.server.api.services.AmbariMetaInfoTest.SERVICE_NAME_HDFS, org.apache.ambari.server.api.services.AmbariMetaInfoTest.NON_EXT_VALUE);
        } catch (org.apache.ambari.server.StackAccessException e) {
        }
    }

    @org.junit.Test
    public void testGetPropertiesSharedName() throws java.lang.Exception {
        java.util.Set<org.apache.ambari.server.state.PropertyInfo> properties = org.apache.ambari.server.api.services.AmbariMetaInfoTest.metaInfo.getPropertiesByName(org.apache.ambari.server.api.services.AmbariMetaInfoTest.STACK_NAME_HDP, org.apache.ambari.server.api.services.AmbariMetaInfoTest.STACK_VERSION_HDP_02, org.apache.ambari.server.api.services.AmbariMetaInfoTest.SERVICE_NAME_HDFS, org.apache.ambari.server.api.services.AmbariMetaInfoTest.SHARED_PROPERTY_NAME);
        junit.framework.Assert.assertEquals(2, properties.size());
        for (org.apache.ambari.server.state.PropertyInfo propertyInfo : properties) {
            junit.framework.Assert.assertEquals(org.apache.ambari.server.api.services.AmbariMetaInfoTest.SHARED_PROPERTY_NAME, propertyInfo.getName());
            junit.framework.Assert.assertTrue(propertyInfo.getFilename().equals(org.apache.ambari.server.api.services.AmbariMetaInfoTest.HADOOP_ENV_FILE_NAME) || propertyInfo.getFilename().equals(org.apache.ambari.server.api.services.AmbariMetaInfoTest.HDFS_LOG4J_FILE_NAME));
        }
    }

    @org.junit.Test
    public void testGetOperatingSystems() throws java.lang.Exception {
        java.util.Set<org.apache.ambari.server.state.OperatingSystemInfo> operatingSystems = org.apache.ambari.server.api.services.AmbariMetaInfoTest.metaInfo.getOperatingSystems(org.apache.ambari.server.api.services.AmbariMetaInfoTest.STACK_NAME_HDP, org.apache.ambari.server.api.services.AmbariMetaInfoTest.STACK_VERSION_HDP);
        junit.framework.Assert.assertEquals(org.apache.ambari.server.api.services.AmbariMetaInfoTest.OS_CNT, operatingSystems.size());
    }

    @org.junit.Test
    public void testGetOperatingSystem() throws java.lang.Exception {
        org.apache.ambari.server.state.OperatingSystemInfo operatingSystem = org.apache.ambari.server.api.services.AmbariMetaInfoTest.metaInfo.getOperatingSystem(org.apache.ambari.server.api.services.AmbariMetaInfoTest.STACK_NAME_HDP, org.apache.ambari.server.api.services.AmbariMetaInfoTest.STACK_VERSION_HDP, org.apache.ambari.server.api.services.AmbariMetaInfoTest.OS_TYPE);
        junit.framework.Assert.assertEquals(operatingSystem.getOsType(), org.apache.ambari.server.api.services.AmbariMetaInfoTest.OS_TYPE);
        try {
            org.apache.ambari.server.api.services.AmbariMetaInfoTest.metaInfo.getOperatingSystem(org.apache.ambari.server.api.services.AmbariMetaInfoTest.STACK_NAME_HDP, org.apache.ambari.server.api.services.AmbariMetaInfoTest.STACK_VERSION_HDP, org.apache.ambari.server.api.services.AmbariMetaInfoTest.NON_EXT_VALUE);
        } catch (org.apache.ambari.server.StackAccessException e) {
        }
    }

    @org.junit.Test
    public void isOsSupported() throws java.lang.Exception {
        junit.framework.Assert.assertTrue(org.apache.ambari.server.api.services.AmbariMetaInfoTest.metaInfo.isOsSupported("redhat5"));
        junit.framework.Assert.assertTrue(org.apache.ambari.server.api.services.AmbariMetaInfoTest.metaInfo.isOsSupported("centos5"));
        junit.framework.Assert.assertTrue(org.apache.ambari.server.api.services.AmbariMetaInfoTest.metaInfo.isOsSupported("oraclelinux5"));
        junit.framework.Assert.assertTrue(org.apache.ambari.server.api.services.AmbariMetaInfoTest.metaInfo.isOsSupported("redhat6"));
        junit.framework.Assert.assertTrue(org.apache.ambari.server.api.services.AmbariMetaInfoTest.metaInfo.isOsSupported("centos6"));
        junit.framework.Assert.assertTrue(org.apache.ambari.server.api.services.AmbariMetaInfoTest.metaInfo.isOsSupported("oraclelinux6"));
        junit.framework.Assert.assertTrue(org.apache.ambari.server.api.services.AmbariMetaInfoTest.metaInfo.isOsSupported("suse11"));
        junit.framework.Assert.assertTrue(org.apache.ambari.server.api.services.AmbariMetaInfoTest.metaInfo.isOsSupported("sles11"));
        junit.framework.Assert.assertTrue(org.apache.ambari.server.api.services.AmbariMetaInfoTest.metaInfo.isOsSupported("ubuntu12"));
        junit.framework.Assert.assertTrue(org.apache.ambari.server.api.services.AmbariMetaInfoTest.metaInfo.isOsSupported("win2008server6"));
        junit.framework.Assert.assertTrue(org.apache.ambari.server.api.services.AmbariMetaInfoTest.metaInfo.isOsSupported("win2008serverr26"));
        junit.framework.Assert.assertTrue(org.apache.ambari.server.api.services.AmbariMetaInfoTest.metaInfo.isOsSupported("win2012server6"));
        junit.framework.Assert.assertTrue(org.apache.ambari.server.api.services.AmbariMetaInfoTest.metaInfo.isOsSupported("win2012serverr26"));
    }

    @org.junit.Test
    public void testExtendedStackDefinition() throws java.lang.Exception {
        org.apache.ambari.server.state.StackInfo stackInfo = org.apache.ambari.server.api.services.AmbariMetaInfoTest.metaInfo.getStack(org.apache.ambari.server.api.services.AmbariMetaInfoTest.STACK_NAME_HDP, org.apache.ambari.server.api.services.AmbariMetaInfoTest.EXT_STACK_NAME);
        junit.framework.Assert.assertTrue(stackInfo != null);
        java.util.Collection<org.apache.ambari.server.state.ServiceInfo> serviceInfos = stackInfo.getServices();
        junit.framework.Assert.assertFalse(serviceInfos.isEmpty());
        junit.framework.Assert.assertTrue(serviceInfos.size() > 1);
        org.apache.ambari.server.state.ServiceInfo deletedService = null;
        org.apache.ambari.server.state.ServiceInfo redefinedService = null;
        for (org.apache.ambari.server.state.ServiceInfo serviceInfo : serviceInfos) {
            if (serviceInfo.getName().equals("SQOOP")) {
                deletedService = serviceInfo;
            }
            if (serviceInfo.getName().equals("YARN")) {
                redefinedService = serviceInfo;
            }
        }
        junit.framework.Assert.assertNull("SQOOP is a deleted service, should not be a part of " + "the extended stack.", deletedService);
        junit.framework.Assert.assertNotNull(redefinedService);
        junit.framework.Assert.assertEquals("YARN service is expected to be defined with 4 active" + " components.", 4, redefinedService.getComponents().size());
        junit.framework.Assert.assertEquals("TEZ is expected to be a part of extended stack " + "definition", "TEZ", redefinedService.getClientComponent().getName());
        junit.framework.Assert.assertFalse("YARN CLIENT is a deleted component.", redefinedService.getClientComponent().getName().equals("YARN_CLIENT"));
        junit.framework.Assert.assertNotNull(redefinedService.getProperties());
        junit.framework.Assert.assertTrue(redefinedService.getProperties().size() > 4);
        org.apache.ambari.server.state.PropertyInfo deleteProperty1 = null;
        org.apache.ambari.server.state.PropertyInfo deleteProperty2 = null;
        org.apache.ambari.server.state.PropertyInfo redefinedProperty1 = null;
        org.apache.ambari.server.state.PropertyInfo redefinedProperty2 = null;
        org.apache.ambari.server.state.PropertyInfo redefinedProperty3 = null;
        org.apache.ambari.server.state.PropertyInfo inheritedProperty = null;
        org.apache.ambari.server.state.PropertyInfo newProperty = null;
        org.apache.ambari.server.state.PropertyInfo newEnhancedProperty = null;
        org.apache.ambari.server.state.PropertyInfo propertyWithExtraValueAttributes = null;
        org.apache.ambari.server.state.PropertyInfo originalProperty = null;
        org.apache.ambari.server.state.PropertyDependencyInfo propertyDependencyInfo = new org.apache.ambari.server.state.PropertyDependencyInfo("yarn-site", "new-enhanced-yarn-property");
        for (org.apache.ambari.server.state.PropertyInfo propertyInfo : redefinedService.getProperties()) {
            if (propertyInfo.getName().equals("yarn.resourcemanager.resource-tracker.address")) {
                deleteProperty1 = propertyInfo;
            } else if (propertyInfo.getName().equals("yarn.resourcemanager.scheduler.address")) {
                deleteProperty2 = propertyInfo;
            } else if (propertyInfo.getName().equals("yarn.resourcemanager.address")) {
                redefinedProperty1 = propertyInfo;
            } else if (propertyInfo.getName().equals("yarn.resourcemanager.admin.address")) {
                redefinedProperty2 = propertyInfo;
            } else if (propertyInfo.getName().equals("yarn.nodemanager.health-checker.interval-ms")) {
                redefinedProperty3 = propertyInfo;
            } else if (propertyInfo.getName().equals("yarn.nodemanager.address")) {
                inheritedProperty = propertyInfo;
            } else if (propertyInfo.getName().equals("new-yarn-property")) {
                newProperty = propertyInfo;
            } else if (propertyInfo.getName().equals("new-enhanced-yarn-property")) {
                newEnhancedProperty = propertyInfo;
            } else if (propertyInfo.getName().equals("yarn.nodemanager.aux-services")) {
                originalProperty = propertyInfo;
            } else if (propertyInfo.getName().equals("property.with.extra.value.attributes")) {
                propertyWithExtraValueAttributes = propertyInfo;
            }
        }
        junit.framework.Assert.assertNull(deleteProperty1);
        junit.framework.Assert.assertNull(deleteProperty2);
        junit.framework.Assert.assertNotNull(redefinedProperty1);
        junit.framework.Assert.assertNotNull(redefinedProperty2);
        junit.framework.Assert.assertNotNull("yarn.nodemanager.address expected to be inherited " + "from parent", inheritedProperty);
        junit.framework.Assert.assertEquals("localhost:100009", redefinedProperty1.getValue());
        junit.framework.Assert.assertNotNull(redefinedProperty3);
        junit.framework.Assert.assertEquals("135000", redefinedProperty3.getValue());
        junit.framework.Assert.assertEquals("", redefinedProperty2.getValue());
        junit.framework.Assert.assertNotNull(newProperty);
        junit.framework.Assert.assertEquals("some-value", newProperty.getValue());
        junit.framework.Assert.assertEquals("some description.", newProperty.getDescription());
        junit.framework.Assert.assertEquals("yarn-site.xml", newProperty.getFilename());
        junit.framework.Assert.assertEquals(1, newProperty.getDependedByProperties().size());
        junit.framework.Assert.assertTrue(newProperty.getDependedByProperties().contains(propertyDependencyInfo));
        junit.framework.Assert.assertNotNull(newEnhancedProperty);
        junit.framework.Assert.assertEquals("1024", newEnhancedProperty.getValue());
        junit.framework.Assert.assertEquals("some enhanced description.", newEnhancedProperty.getDescription());
        junit.framework.Assert.assertEquals("yarn-site.xml", newEnhancedProperty.getFilename());
        junit.framework.Assert.assertEquals(2, newEnhancedProperty.getDependsOnProperties().size());
        junit.framework.Assert.assertTrue(newEnhancedProperty.getDependsOnProperties().contains(new org.apache.ambari.server.state.PropertyDependencyInfo("yarn-site", "new-yarn-property")));
        junit.framework.Assert.assertTrue(newEnhancedProperty.getDependsOnProperties().contains(new org.apache.ambari.server.state.PropertyDependencyInfo("global", "yarn_heapsize")));
        junit.framework.Assert.assertEquals("MB", newEnhancedProperty.getPropertyValueAttributes().getUnit());
        junit.framework.Assert.assertEquals("int", newEnhancedProperty.getPropertyValueAttributes().getType());
        junit.framework.Assert.assertEquals("512", newEnhancedProperty.getPropertyValueAttributes().getMinimum());
        junit.framework.Assert.assertEquals("15360", newEnhancedProperty.getPropertyValueAttributes().getMaximum());
        junit.framework.Assert.assertEquals("256", newEnhancedProperty.getPropertyValueAttributes().getIncrementStep());
        junit.framework.Assert.assertNull(newEnhancedProperty.getPropertyValueAttributes().getEntries());
        junit.framework.Assert.assertNull(newEnhancedProperty.getPropertyValueAttributes().getEntriesEditable());
        junit.framework.Assert.assertTrue(propertyWithExtraValueAttributes.getPropertyValueAttributes().getEmptyValueValid());
        junit.framework.Assert.assertTrue(propertyWithExtraValueAttributes.getPropertyValueAttributes().getVisible());
        junit.framework.Assert.assertTrue(propertyWithExtraValueAttributes.getPropertyValueAttributes().getReadOnly());
        junit.framework.Assert.assertEquals(java.lang.Boolean.FALSE, propertyWithExtraValueAttributes.getPropertyValueAttributes().getEditableOnlyAtInstall());
        junit.framework.Assert.assertEquals(java.lang.Boolean.FALSE, propertyWithExtraValueAttributes.getPropertyValueAttributes().getOverridable());
        junit.framework.Assert.assertEquals(java.lang.Boolean.FALSE, propertyWithExtraValueAttributes.getPropertyValueAttributes().getShowPropertyName());
        junit.framework.Assert.assertNotNull(originalProperty);
        junit.framework.Assert.assertEquals("mapreduce.shuffle", originalProperty.getValue());
        junit.framework.Assert.assertEquals("Auxilliary services of NodeManager", originalProperty.getDescription());
        junit.framework.Assert.assertEquals(6, redefinedService.getConfigDependencies().size());
        junit.framework.Assert.assertEquals(7, redefinedService.getConfigDependenciesWithComponents().size());
    }

    @org.junit.Test
    public void testPropertyCount() throws java.lang.Exception {
        java.util.Set<org.apache.ambari.server.state.PropertyInfo> properties = org.apache.ambari.server.api.services.AmbariMetaInfoTest.metaInfo.getServiceProperties(org.apache.ambari.server.api.services.AmbariMetaInfoTest.STACK_NAME_HDP, org.apache.ambari.server.api.services.AmbariMetaInfoTest.STACK_VERSION_HDP_02, org.apache.ambari.server.api.services.AmbariMetaInfoTest.SERVICE_NAME_HDFS);
        junit.framework.Assert.assertEquals(103, properties.size());
    }

    @org.junit.Test
    public void testBadStack() throws java.lang.Exception {
        java.io.File stackRoot = new java.io.File("src/test/resources/bad-stacks");
        java.io.File version = new java.io.File("src/test/resources/version");
        if (java.lang.System.getProperty("os.name").contains("Windows")) {
            stackRoot = new java.io.File(java.lang.ClassLoader.getSystemClassLoader().getResource("bad-stacks").getPath());
            version = new java.io.File(new java.io.File(java.lang.ClassLoader.getSystemClassLoader().getResource("").getPath()).getParent(), "version");
        }
        org.apache.ambari.server.api.services.AmbariMetaInfoTest.LOG.info("Stacks file " + stackRoot.getAbsolutePath());
        org.apache.ambari.server.api.services.AmbariMetaInfoTest.TestAmbariMetaInfo ambariMetaInfo = org.apache.ambari.server.api.services.AmbariMetaInfoTest.createAmbariMetaInfo(stackRoot, version, new java.io.File(""));
        junit.framework.Assert.assertEquals(1, ambariMetaInfo.getStackManager().getStacks().size());
        junit.framework.Assert.assertEquals(false, ambariMetaInfo.getStackManager().getStack("HDP", "0.1").isValid());
        junit.framework.Assert.assertEquals(2, ambariMetaInfo.getStackManager().getStack("HDP", "0.1").getErrors().size());
    }

    @org.junit.Test
    public void testMetricsJson() throws java.lang.Exception {
        org.apache.ambari.server.state.ServiceInfo svc = org.apache.ambari.server.api.services.AmbariMetaInfoTest.metaInfo.getService(org.apache.ambari.server.api.services.AmbariMetaInfoTest.STACK_NAME_HDP, "2.0.5", "HDFS");
        junit.framework.Assert.assertNotNull(svc);
        junit.framework.Assert.assertNotNull(svc.getMetricsFile());
        svc = org.apache.ambari.server.api.services.AmbariMetaInfoTest.metaInfo.getService(org.apache.ambari.server.api.services.AmbariMetaInfoTest.STACK_NAME_HDP, "2.0.6", "HDFS");
        junit.framework.Assert.assertNotNull(svc);
        junit.framework.Assert.assertNotNull(svc.getMetricsFile());
        java.util.List<org.apache.ambari.server.state.stack.MetricDefinition> list = org.apache.ambari.server.api.services.AmbariMetaInfoTest.metaInfo.getMetrics(org.apache.ambari.server.api.services.AmbariMetaInfoTest.STACK_NAME_HDP, "2.0.5", "HDFS", org.apache.ambari.server.api.services.AmbariMetaInfoTest.SERVICE_COMPONENT_NAME, org.apache.ambari.server.controller.spi.Resource.Type.Component.name());
        junit.framework.Assert.assertNotNull(list);
        org.apache.ambari.server.api.services.AmbariMetaInfoTest.checkNoAggregatedFunctionsForJmx(list);
        list = org.apache.ambari.server.api.services.AmbariMetaInfoTest.metaInfo.getMetrics(org.apache.ambari.server.api.services.AmbariMetaInfoTest.STACK_NAME_HDP, "2.0.5", "HDFS", "DATANODE", org.apache.ambari.server.controller.spi.Resource.Type.Component.name());
        junit.framework.Assert.assertNull(list);
        java.util.List<org.apache.ambari.server.state.stack.MetricDefinition> list0 = org.apache.ambari.server.api.services.AmbariMetaInfoTest.metaInfo.getMetrics(org.apache.ambari.server.api.services.AmbariMetaInfoTest.STACK_NAME_HDP, "2.0.5", "HDFS", "DATANODE", org.apache.ambari.server.controller.spi.Resource.Type.Component.name());
        junit.framework.Assert.assertNull(list0);
        junit.framework.Assert.assertTrue("Expecting subsequent calls to use a cached value for the definition", list == list0);
        list = org.apache.ambari.server.api.services.AmbariMetaInfoTest.metaInfo.getMetrics(org.apache.ambari.server.api.services.AmbariMetaInfoTest.STACK_NAME_HDP, "2.0.6", "HDFS", "DATANODE", org.apache.ambari.server.controller.spi.Resource.Type.Component.name());
        junit.framework.Assert.assertNull(list);
    }

    @org.junit.Test
    public void testKerberosJson() throws java.lang.Exception {
        org.apache.ambari.server.state.ServiceInfo svc;
        svc = org.apache.ambari.server.api.services.AmbariMetaInfoTest.metaInfo.getService(org.apache.ambari.server.api.services.AmbariMetaInfoTest.STACK_NAME_HDP, "2.0.8", "HDFS");
        junit.framework.Assert.assertNotNull(svc);
        java.io.File kerberosDescriptorFile1 = svc.getKerberosDescriptorFile();
        junit.framework.Assert.assertNotNull(kerberosDescriptorFile1);
        junit.framework.Assert.assertTrue(kerberosDescriptorFile1.exists());
        svc = org.apache.ambari.server.api.services.AmbariMetaInfoTest.metaInfo.getService(org.apache.ambari.server.api.services.AmbariMetaInfoTest.STACK_NAME_HDP, "2.1.1", "HDFS");
        junit.framework.Assert.assertNotNull(svc);
        java.io.File kerberosDescriptorFile2 = svc.getKerberosDescriptorFile();
        junit.framework.Assert.assertNotNull(kerberosDescriptorFile1);
        junit.framework.Assert.assertTrue(kerberosDescriptorFile1.exists());
        junit.framework.Assert.assertEquals(kerberosDescriptorFile1, kerberosDescriptorFile2);
        svc = org.apache.ambari.server.api.services.AmbariMetaInfoTest.metaInfo.getService(org.apache.ambari.server.api.services.AmbariMetaInfoTest.STACK_NAME_HDP, "2.0.7", "HDFS");
        junit.framework.Assert.assertNotNull(svc);
        java.io.File kerberosDescriptorFile3 = svc.getKerberosDescriptorFile();
        junit.framework.Assert.assertNull(kerberosDescriptorFile3);
    }

    @org.junit.Test
    public void testGanglia134Dependencies() throws java.lang.Exception {
        org.apache.ambari.server.state.ServiceInfo service = org.apache.ambari.server.api.services.AmbariMetaInfoTest.metaInfo.getService(org.apache.ambari.server.api.services.AmbariMetaInfoTest.STACK_NAME_HDP, "1.3.4", "GANGLIA");
        java.util.List<org.apache.ambari.server.state.ComponentInfo> componentList = service.getComponents();
        junit.framework.Assert.assertEquals(2, componentList.size());
        for (org.apache.ambari.server.state.ComponentInfo component : componentList) {
            java.lang.String name = component.getName();
            if (name.equals("GANGLIA_SERVER")) {
                junit.framework.Assert.assertEquals(0, component.getDependencies().size());
                junit.framework.Assert.assertNull(component.getAutoDeploy());
                junit.framework.Assert.assertEquals("1", component.getCardinality());
            }
            if (name.equals("GANGLIA_MONITOR")) {
                junit.framework.Assert.assertEquals(0, component.getDependencies().size());
                junit.framework.Assert.assertTrue(component.getAutoDeploy().isEnabled());
                junit.framework.Assert.assertEquals("ALL", component.getCardinality());
            }
        }
    }

    @org.junit.Test
    public void testHBase134Dependencies() throws java.lang.Exception {
        org.apache.ambari.server.state.ServiceInfo service = org.apache.ambari.server.api.services.AmbariMetaInfoTest.metaInfo.getService(org.apache.ambari.server.api.services.AmbariMetaInfoTest.STACK_NAME_HDP, "1.3.4", "HBASE");
        java.util.List<org.apache.ambari.server.state.ComponentInfo> componentList = service.getComponents();
        junit.framework.Assert.assertEquals(3, componentList.size());
        for (org.apache.ambari.server.state.ComponentInfo component : componentList) {
            java.lang.String name = component.getName();
            if (name.equals("HBASE_MASTER")) {
                java.util.List<org.apache.ambari.server.state.DependencyInfo> dependencyList = component.getDependencies();
                junit.framework.Assert.assertEquals(2, dependencyList.size());
                for (org.apache.ambari.server.state.DependencyInfo dependency : dependencyList) {
                    if (dependency.getName().equals("HDFS/HDFS_CLIENT")) {
                        junit.framework.Assert.assertEquals("host", dependency.getScope());
                        junit.framework.Assert.assertEquals(true, dependency.getAutoDeploy().isEnabled());
                    } else if (dependency.getName().equals("ZOOKEEPER/ZOOKEEPER_SERVER")) {
                        junit.framework.Assert.assertEquals("cluster", dependency.getScope());
                        org.apache.ambari.server.state.AutoDeployInfo autoDeploy = dependency.getAutoDeploy();
                        junit.framework.Assert.assertEquals(true, autoDeploy.isEnabled());
                        junit.framework.Assert.assertEquals("HBASE/HBASE_MASTER", autoDeploy.getCoLocate());
                    } else {
                        junit.framework.Assert.fail("Unexpected dependency");
                    }
                }
                junit.framework.Assert.assertNull(component.getAutoDeploy());
                junit.framework.Assert.assertEquals("1", component.getCardinality());
            }
            if (name.equals("HBASE_REGIONSERVER")) {
                junit.framework.Assert.assertEquals(0, component.getDependencies().size());
                junit.framework.Assert.assertNull(component.getAutoDeploy());
                junit.framework.Assert.assertEquals("1+", component.getCardinality());
            }
            if (name.equals("HBASE_CLIENT")) {
                junit.framework.Assert.assertEquals(0, component.getDependencies().size());
                junit.framework.Assert.assertNull(component.getAutoDeploy());
                junit.framework.Assert.assertEquals("0+", component.getCardinality());
            }
        }
    }

    @org.junit.Test
    public void testHDFS134Dependencies() throws java.lang.Exception {
        org.apache.ambari.server.state.ServiceInfo service = org.apache.ambari.server.api.services.AmbariMetaInfoTest.metaInfo.getService(org.apache.ambari.server.api.services.AmbariMetaInfoTest.STACK_NAME_HDP, "1.3.4", "HDFS");
        java.util.List<org.apache.ambari.server.state.ComponentInfo> componentList = service.getComponents();
        junit.framework.Assert.assertEquals(4, componentList.size());
        for (org.apache.ambari.server.state.ComponentInfo component : componentList) {
            java.lang.String name = component.getName();
            if (name.equals("NAMENODE")) {
                junit.framework.Assert.assertEquals(0, component.getDependencies().size());
                junit.framework.Assert.assertNull(component.getAutoDeploy());
                junit.framework.Assert.assertEquals("1", component.getCardinality());
            }
            if (name.equals("DATANODE")) {
                junit.framework.Assert.assertEquals(0, component.getDependencies().size());
                junit.framework.Assert.assertNull(component.getAutoDeploy());
                junit.framework.Assert.assertEquals("1+", component.getCardinality());
            }
            if (name.equals("SECONDARY_NAMENODE")) {
                junit.framework.Assert.assertEquals(0, component.getDependencies().size());
                junit.framework.Assert.assertNull(component.getAutoDeploy());
                junit.framework.Assert.assertEquals("1", component.getCardinality());
            }
            if (name.equals("HDFS_CLIENT")) {
                junit.framework.Assert.assertEquals(0, component.getDependencies().size());
                junit.framework.Assert.assertNull(component.getAutoDeploy());
                junit.framework.Assert.assertEquals("0+", component.getCardinality());
            }
        }
    }

    @org.junit.Test
    public void testHive134Dependencies() throws java.lang.Exception {
        org.apache.ambari.server.state.ServiceInfo service = org.apache.ambari.server.api.services.AmbariMetaInfoTest.metaInfo.getService(org.apache.ambari.server.api.services.AmbariMetaInfoTest.STACK_NAME_HDP, "1.3.4", "HIVE");
        java.util.List<org.apache.ambari.server.state.ComponentInfo> componentList = service.getComponents();
        junit.framework.Assert.assertEquals(4, componentList.size());
        for (org.apache.ambari.server.state.ComponentInfo component : componentList) {
            java.lang.String name = component.getName();
            if (name.equals("HIVE_METASTORE")) {
                junit.framework.Assert.assertEquals(0, component.getDependencies().size());
                org.apache.ambari.server.state.AutoDeployInfo autoDeploy = component.getAutoDeploy();
                junit.framework.Assert.assertTrue(autoDeploy.isEnabled());
                junit.framework.Assert.assertEquals("HIVE/HIVE_SERVER", autoDeploy.getCoLocate());
                junit.framework.Assert.assertEquals("1", component.getCardinality());
            }
            if (name.equals("HIVE_SERVER")) {
                java.util.List<org.apache.ambari.server.state.DependencyInfo> dependencyList = component.getDependencies();
                junit.framework.Assert.assertEquals(1, dependencyList.size());
                org.apache.ambari.server.state.DependencyInfo dependency = dependencyList.get(0);
                junit.framework.Assert.assertEquals("ZOOKEEPER/ZOOKEEPER_SERVER", dependency.getName());
                junit.framework.Assert.assertEquals("cluster", dependency.getScope());
                org.apache.ambari.server.state.AutoDeployInfo autoDeploy = dependency.getAutoDeploy();
                junit.framework.Assert.assertTrue(autoDeploy.isEnabled());
                junit.framework.Assert.assertEquals("HIVE/HIVE_SERVER", autoDeploy.getCoLocate());
                junit.framework.Assert.assertNull(component.getAutoDeploy());
                junit.framework.Assert.assertEquals("1", component.getCardinality());
            }
            if (name.equals("MYSQL_SERVER")) {
                junit.framework.Assert.assertEquals(0, component.getDependencies().size());
                org.apache.ambari.server.state.AutoDeployInfo autoDeploy = component.getAutoDeploy();
                junit.framework.Assert.assertTrue(autoDeploy.isEnabled());
                junit.framework.Assert.assertEquals("HIVE/HIVE_SERVER", autoDeploy.getCoLocate());
                junit.framework.Assert.assertEquals("1", component.getCardinality());
            }
            if (name.equals("HIVE_CLIENT")) {
                junit.framework.Assert.assertEquals(0, component.getDependencies().size());
                junit.framework.Assert.assertNull(component.getAutoDeploy());
                junit.framework.Assert.assertEquals("0+", component.getCardinality());
            }
        }
    }

    @org.junit.Test
    public void testHue134Dependencies() throws java.lang.Exception {
        org.apache.ambari.server.state.ServiceInfo service = org.apache.ambari.server.api.services.AmbariMetaInfoTest.metaInfo.getService(org.apache.ambari.server.api.services.AmbariMetaInfoTest.STACK_NAME_HDP, "1.3.4", "HUE");
        java.util.List<org.apache.ambari.server.state.ComponentInfo> componentList = service.getComponents();
        junit.framework.Assert.assertEquals(1, componentList.size());
        org.apache.ambari.server.state.ComponentInfo component = componentList.get(0);
        junit.framework.Assert.assertEquals("HUE_SERVER", component.getName());
        junit.framework.Assert.assertEquals(0, component.getDependencies().size());
        junit.framework.Assert.assertNull(component.getAutoDeploy());
        junit.framework.Assert.assertEquals("1", component.getCardinality());
    }

    @org.junit.Test
    public void testMapReduce134Dependencies() throws java.lang.Exception {
        org.apache.ambari.server.state.ServiceInfo service = org.apache.ambari.server.api.services.AmbariMetaInfoTest.metaInfo.getService(org.apache.ambari.server.api.services.AmbariMetaInfoTest.STACK_NAME_HDP, "1.3.4", "MAPREDUCE");
        java.util.List<org.apache.ambari.server.state.ComponentInfo> componentList = service.getComponents();
        junit.framework.Assert.assertEquals(4, componentList.size());
        for (org.apache.ambari.server.state.ComponentInfo component : componentList) {
            java.lang.String name = component.getName();
            if (name.equals("JOBTRACKER")) {
                junit.framework.Assert.assertEquals(0, component.getDependencies().size());
                junit.framework.Assert.assertNull(component.getAutoDeploy());
                junit.framework.Assert.assertEquals("1", component.getCardinality());
            }
            if (name.equals("TASKTRACKER")) {
                junit.framework.Assert.assertEquals(0, component.getDependencies().size());
                junit.framework.Assert.assertNull(component.getAutoDeploy());
                junit.framework.Assert.assertEquals("1+", component.getCardinality());
            }
            if (name.equals("HISTORYSERVER")) {
                junit.framework.Assert.assertEquals(0, component.getDependencies().size());
                org.apache.ambari.server.state.AutoDeployInfo autoDeploy = component.getAutoDeploy();
                junit.framework.Assert.assertTrue(autoDeploy.isEnabled());
                junit.framework.Assert.assertEquals("MAPREDUCE/JOBTRACKER", autoDeploy.getCoLocate());
                junit.framework.Assert.assertEquals("1", component.getCardinality());
            }
            if (name.equals("MAPREDUCE_CLIENT")) {
                junit.framework.Assert.assertEquals(0, component.getDependencies().size());
                junit.framework.Assert.assertNull(component.getAutoDeploy());
                junit.framework.Assert.assertEquals("0+", component.getCardinality());
            }
        }
    }

    @org.junit.Test
    public void testOozie134Dependencies() throws java.lang.Exception {
        org.apache.ambari.server.state.ServiceInfo service = org.apache.ambari.server.api.services.AmbariMetaInfoTest.metaInfo.getService(org.apache.ambari.server.api.services.AmbariMetaInfoTest.STACK_NAME_HDP, "1.3.4", "OOZIE");
        java.util.List<org.apache.ambari.server.state.ComponentInfo> componentList = service.getComponents();
        junit.framework.Assert.assertEquals(2, componentList.size());
        for (org.apache.ambari.server.state.ComponentInfo component : componentList) {
            java.lang.String name = component.getName();
            if (name.equals("OOZIE_SERVER")) {
                java.util.List<org.apache.ambari.server.state.DependencyInfo> dependencyList = component.getDependencies();
                junit.framework.Assert.assertEquals(2, dependencyList.size());
                for (org.apache.ambari.server.state.DependencyInfo dependency : dependencyList) {
                    if (dependency.getName().equals("HDFS/HDFS_CLIENT")) {
                        junit.framework.Assert.assertEquals("host", dependency.getScope());
                        junit.framework.Assert.assertEquals(true, dependency.getAutoDeploy().isEnabled());
                    } else if (dependency.getName().equals("MAPREDUCE/MAPREDUCE_CLIENT")) {
                        junit.framework.Assert.assertEquals("host", dependency.getScope());
                        junit.framework.Assert.assertEquals(true, dependency.getAutoDeploy().isEnabled());
                    } else {
                        junit.framework.Assert.fail("Unexpected dependency");
                    }
                }
                junit.framework.Assert.assertNull(component.getAutoDeploy());
                junit.framework.Assert.assertEquals("1", component.getCardinality());
            }
            if (name.equals("OOZIE_CLIENT")) {
                java.util.List<org.apache.ambari.server.state.DependencyInfo> dependencyList = component.getDependencies();
                junit.framework.Assert.assertEquals(2, dependencyList.size());
                for (org.apache.ambari.server.state.DependencyInfo dependency : dependencyList) {
                    if (dependency.getName().equals("HDFS/HDFS_CLIENT")) {
                        junit.framework.Assert.assertEquals("host", dependency.getScope());
                        junit.framework.Assert.assertEquals(true, dependency.getAutoDeploy().isEnabled());
                    } else if (dependency.getName().equals("MAPREDUCE/MAPREDUCE_CLIENT")) {
                        junit.framework.Assert.assertEquals("host", dependency.getScope());
                        junit.framework.Assert.assertEquals(true, dependency.getAutoDeploy().isEnabled());
                    } else {
                        junit.framework.Assert.fail("Unexpected dependency");
                    }
                }
                junit.framework.Assert.assertNull(component.getAutoDeploy());
                junit.framework.Assert.assertEquals("0+", component.getCardinality());
            }
        }
    }

    @org.junit.Test
    public void testPig134Dependencies() throws java.lang.Exception {
        org.apache.ambari.server.state.ServiceInfo service = org.apache.ambari.server.api.services.AmbariMetaInfoTest.metaInfo.getService(org.apache.ambari.server.api.services.AmbariMetaInfoTest.STACK_NAME_HDP, "1.3.4", "PIG");
        java.util.List<org.apache.ambari.server.state.ComponentInfo> componentList = service.getComponents();
        junit.framework.Assert.assertEquals(1, componentList.size());
        org.apache.ambari.server.state.ComponentInfo component = componentList.get(0);
        junit.framework.Assert.assertEquals("PIG", component.getName());
        junit.framework.Assert.assertEquals(0, component.getDependencies().size());
        junit.framework.Assert.assertNull(component.getAutoDeploy());
        junit.framework.Assert.assertEquals("0+", component.getCardinality());
    }

    @org.junit.Test
    public void testSqoop134Dependencies() throws java.lang.Exception {
        org.apache.ambari.server.state.ServiceInfo service = org.apache.ambari.server.api.services.AmbariMetaInfoTest.metaInfo.getService(org.apache.ambari.server.api.services.AmbariMetaInfoTest.STACK_NAME_HDP, "1.3.4", "SQOOP");
        java.util.List<org.apache.ambari.server.state.ComponentInfo> componentList = service.getComponents();
        junit.framework.Assert.assertEquals(1, componentList.size());
        org.apache.ambari.server.state.ComponentInfo component = componentList.get(0);
        junit.framework.Assert.assertEquals("SQOOP", component.getName());
        java.util.List<org.apache.ambari.server.state.DependencyInfo> dependencyList = component.getDependencies();
        junit.framework.Assert.assertEquals(2, dependencyList.size());
        for (org.apache.ambari.server.state.DependencyInfo dependency : dependencyList) {
            if (dependency.getName().equals("HDFS/HDFS_CLIENT")) {
                junit.framework.Assert.assertEquals("host", dependency.getScope());
                junit.framework.Assert.assertEquals(true, dependency.getAutoDeploy().isEnabled());
            } else if (dependency.getName().equals("MAPREDUCE/MAPREDUCE_CLIENT")) {
                junit.framework.Assert.assertEquals("host", dependency.getScope());
                junit.framework.Assert.assertEquals(true, dependency.getAutoDeploy().isEnabled());
            } else {
                junit.framework.Assert.fail("Unexpected dependency");
            }
        }
        junit.framework.Assert.assertNull(component.getAutoDeploy());
        junit.framework.Assert.assertEquals("0+", component.getCardinality());
    }

    @org.junit.Test
    public void testWebHCat134Dependencies() throws java.lang.Exception {
        org.apache.ambari.server.state.ServiceInfo service = org.apache.ambari.server.api.services.AmbariMetaInfoTest.metaInfo.getService(org.apache.ambari.server.api.services.AmbariMetaInfoTest.STACK_NAME_HDP, "1.3.4", "WEBHCAT");
        java.util.List<org.apache.ambari.server.state.ComponentInfo> componentList = service.getComponents();
        junit.framework.Assert.assertEquals(1, componentList.size());
        org.apache.ambari.server.state.ComponentInfo component = componentList.get(0);
        junit.framework.Assert.assertEquals("WEBHCAT_SERVER", component.getName());
        java.util.List<org.apache.ambari.server.state.DependencyInfo> dependencyList = component.getDependencies();
        junit.framework.Assert.assertEquals(4, dependencyList.size());
        for (org.apache.ambari.server.state.DependencyInfo dependency : dependencyList) {
            if (dependency.getName().equals("HDFS/HDFS_CLIENT")) {
                junit.framework.Assert.assertEquals("host", dependency.getScope());
                junit.framework.Assert.assertEquals(true, dependency.getAutoDeploy().isEnabled());
            } else if (dependency.getName().equals("MAPREDUCE/MAPREDUCE_CLIENT")) {
                junit.framework.Assert.assertEquals("host", dependency.getScope());
                junit.framework.Assert.assertEquals(true, dependency.getAutoDeploy().isEnabled());
            } else if (dependency.getName().equals("ZOOKEEPER/ZOOKEEPER_SERVER")) {
                junit.framework.Assert.assertEquals("cluster", dependency.getScope());
                org.apache.ambari.server.state.AutoDeployInfo autoDeploy = dependency.getAutoDeploy();
                junit.framework.Assert.assertEquals(true, autoDeploy.isEnabled());
                junit.framework.Assert.assertEquals("WEBHCAT/WEBHCAT_SERVER", autoDeploy.getCoLocate());
            } else if (dependency.getName().equals("ZOOKEEPER/ZOOKEEPER_CLIENT")) {
                junit.framework.Assert.assertEquals("host", dependency.getScope());
                junit.framework.Assert.assertEquals(true, dependency.getAutoDeploy().isEnabled());
            } else {
                junit.framework.Assert.fail("Unexpected dependency");
            }
        }
        junit.framework.Assert.assertNull(component.getAutoDeploy());
        junit.framework.Assert.assertEquals("1", component.getCardinality());
    }

    @org.junit.Test
    public void testZooKeeper134Dependencies() throws java.lang.Exception {
        org.apache.ambari.server.state.ServiceInfo service = org.apache.ambari.server.api.services.AmbariMetaInfoTest.metaInfo.getService(org.apache.ambari.server.api.services.AmbariMetaInfoTest.STACK_NAME_HDP, "1.3.4", "ZOOKEEPER");
        java.util.List<org.apache.ambari.server.state.ComponentInfo> componentList = service.getComponents();
        junit.framework.Assert.assertEquals(2, componentList.size());
        for (org.apache.ambari.server.state.ComponentInfo component : componentList) {
            java.lang.String name = component.getName();
            if (name.equals("ZOOKEEPER_SERVER")) {
                junit.framework.Assert.assertEquals(0, component.getDependencies().size());
                junit.framework.Assert.assertNull(component.getAutoDeploy());
                junit.framework.Assert.assertEquals("1", component.getCardinality());
            }
            if (name.equals("ZOOKEEPER_CLIENT")) {
                junit.framework.Assert.assertEquals(0, component.getDependencies().size());
                junit.framework.Assert.assertNull(component.getAutoDeploy());
                junit.framework.Assert.assertEquals("0+", component.getCardinality());
            }
        }
    }

    @org.junit.Test
    public void testServicePackageDirInheritance() throws java.lang.Exception {
        java.lang.String assertionTemplate07 = org.apache.commons.lang.StringUtils.join(new java.lang.String[]{ "stacks", "HDP", "2.0.7", "services", "%s", "package" }, java.io.File.separator);
        java.lang.String assertionTemplate08 = org.apache.commons.lang.StringUtils.join(new java.lang.String[]{ "stacks", "HDP", "2.0.8", "services", "%s", "package" }, java.io.File.separator);
        org.apache.ambari.server.state.ServiceInfo service = org.apache.ambari.server.api.services.AmbariMetaInfoTest.metaInfo.getService(org.apache.ambari.server.api.services.AmbariMetaInfoTest.STACK_NAME_HDP, "2.0.7", "HBASE");
        junit.framework.Assert.assertEquals(java.lang.String.format(assertionTemplate07, "HBASE"), service.getServicePackageFolder());
        service = org.apache.ambari.server.api.services.AmbariMetaInfoTest.metaInfo.getService(org.apache.ambari.server.api.services.AmbariMetaInfoTest.STACK_NAME_HDP, "2.0.7", "HDFS");
        junit.framework.Assert.assertEquals(java.lang.String.format(assertionTemplate07, "HDFS"), service.getServicePackageFolder());
        service = org.apache.ambari.server.api.services.AmbariMetaInfoTest.metaInfo.getService(org.apache.ambari.server.api.services.AmbariMetaInfoTest.STACK_NAME_HDP, "2.0.8", "HBASE");
        junit.framework.Assert.assertEquals(java.lang.String.format(assertionTemplate07, "HBASE"), service.getServicePackageFolder());
        service = org.apache.ambari.server.api.services.AmbariMetaInfoTest.metaInfo.getService(org.apache.ambari.server.api.services.AmbariMetaInfoTest.STACK_NAME_HDP, "2.0.8", "HDFS");
        junit.framework.Assert.assertEquals(java.lang.String.format(assertionTemplate08, "HDFS"), service.getServicePackageFolder());
    }

    @org.junit.Test
    public void testServiceCommandScriptInheritance() throws java.lang.Exception {
        org.apache.ambari.server.state.ServiceInfo service = org.apache.ambari.server.api.services.AmbariMetaInfoTest.metaInfo.getService(org.apache.ambari.server.api.services.AmbariMetaInfoTest.STACK_NAME_HDP, "2.0.7", "HDFS");
        junit.framework.Assert.assertEquals("scripts/service_check_1.py", service.getCommandScript().getScript());
        service = org.apache.ambari.server.api.services.AmbariMetaInfoTest.metaInfo.getService(org.apache.ambari.server.api.services.AmbariMetaInfoTest.STACK_NAME_HDP, "2.0.7", "HBASE");
        junit.framework.Assert.assertEquals("scripts/service_check.py", service.getCommandScript().getScript());
        service = org.apache.ambari.server.api.services.AmbariMetaInfoTest.metaInfo.getService(org.apache.ambari.server.api.services.AmbariMetaInfoTest.STACK_NAME_HDP, "2.0.8", "HBASE");
        junit.framework.Assert.assertEquals("scripts/service_check.py", service.getCommandScript().getScript());
        service = org.apache.ambari.server.api.services.AmbariMetaInfoTest.metaInfo.getService(org.apache.ambari.server.api.services.AmbariMetaInfoTest.STACK_NAME_HDP, "2.0.8", "HDFS");
        junit.framework.Assert.assertEquals("scripts/service_check_2.py", service.getCommandScript().getScript());
    }

    @org.junit.Test
    public void testComponentCommandScriptInheritance() throws java.lang.Exception {
        org.apache.ambari.server.state.ComponentInfo component = org.apache.ambari.server.api.services.AmbariMetaInfoTest.metaInfo.getComponent(org.apache.ambari.server.api.services.AmbariMetaInfoTest.STACK_NAME_HDP, "2.0.7", "HDFS", "HDFS_CLIENT");
        junit.framework.Assert.assertEquals("scripts/hdfs_client.py", component.getCommandScript().getScript());
        component = org.apache.ambari.server.api.services.AmbariMetaInfoTest.metaInfo.getComponent(org.apache.ambari.server.api.services.AmbariMetaInfoTest.STACK_NAME_HDP, "2.0.7", "HBASE", "HBASE_MASTER");
        junit.framework.Assert.assertEquals("scripts/hbase_master.py", component.getCommandScript().getScript());
        component = org.apache.ambari.server.api.services.AmbariMetaInfoTest.metaInfo.getComponent(org.apache.ambari.server.api.services.AmbariMetaInfoTest.STACK_NAME_HDP, "2.0.8", "HBASE", "HBASE_MASTER");
        junit.framework.Assert.assertEquals("scripts/hbase_master.py", component.getCommandScript().getScript());
        component = org.apache.ambari.server.api.services.AmbariMetaInfoTest.metaInfo.getComponent(org.apache.ambari.server.api.services.AmbariMetaInfoTest.STACK_NAME_HDP, "2.0.8", "HDFS", "HDFS_CLIENT");
        junit.framework.Assert.assertEquals("scripts/hdfs_client_overridden.py", component.getCommandScript().getScript());
    }

    @org.junit.Test
    public void testServiceCustomCommandScriptInheritance() throws java.lang.Exception {
        org.apache.ambari.server.state.ServiceInfo service = org.apache.ambari.server.api.services.AmbariMetaInfoTest.metaInfo.getService(org.apache.ambari.server.api.services.AmbariMetaInfoTest.STACK_NAME_HDP, "2.0.7", "HDFS");
        org.apache.ambari.server.state.CustomCommandDefinition ccd = findCustomCommand("RESTART", service);
        junit.framework.Assert.assertEquals("scripts/restart_parent.py", ccd.getCommandScript().getScript());
        ccd = findCustomCommand("YET_ANOTHER_PARENT_SRV_COMMAND", service);
        junit.framework.Assert.assertEquals("scripts/yet_another_parent_srv_command.py", ccd.getCommandScript().getScript());
        junit.framework.Assert.assertEquals(2, service.getCustomCommands().size());
        service = org.apache.ambari.server.api.services.AmbariMetaInfoTest.metaInfo.getService(org.apache.ambari.server.api.services.AmbariMetaInfoTest.STACK_NAME_HDP, "2.0.8", "HDFS");
        junit.framework.Assert.assertEquals(3, service.getCustomCommands().size());
        ccd = findCustomCommand("YET_ANOTHER_PARENT_SRV_COMMAND", service);
        junit.framework.Assert.assertEquals("scripts/yet_another_parent_srv_command.py", ccd.getCommandScript().getScript());
        service = org.apache.ambari.server.api.services.AmbariMetaInfoTest.metaInfo.getService(org.apache.ambari.server.api.services.AmbariMetaInfoTest.STACK_NAME_HDP, "2.0.8", "HDFS");
        ccd = findCustomCommand("RESTART", service);
        junit.framework.Assert.assertEquals("scripts/restart_child.py", ccd.getCommandScript().getScript());
        ccd = findCustomCommand("YET_ANOTHER_CHILD_SRV_COMMAND", service);
        junit.framework.Assert.assertEquals("scripts/yet_another_child_srv_command.py", ccd.getCommandScript().getScript());
    }

    @org.junit.Test
    public void testChildCustomCommandScriptInheritance() throws java.lang.Exception {
        org.apache.ambari.server.state.ComponentInfo component = org.apache.ambari.server.api.services.AmbariMetaInfoTest.metaInfo.getComponent(org.apache.ambari.server.api.services.AmbariMetaInfoTest.STACK_NAME_HDP, "2.0.7", "HDFS", "NAMENODE");
        org.apache.ambari.server.state.CustomCommandDefinition ccd = findCustomCommand("DECOMMISSION", component);
        junit.framework.Assert.assertEquals("scripts/namenode_dec.py", ccd.getCommandScript().getScript());
        ccd = findCustomCommand("YET_ANOTHER_PARENT_COMMAND", component);
        junit.framework.Assert.assertEquals("scripts/yet_another_parent_command.py", ccd.getCommandScript().getScript());
        ccd = findCustomCommand("REBALANCEHDFS", component);
        junit.framework.Assert.assertEquals("scripts/namenode.py", ccd.getCommandScript().getScript());
        junit.framework.Assert.assertTrue(ccd.isBackground());
        junit.framework.Assert.assertEquals(3, component.getCustomCommands().size());
        component = org.apache.ambari.server.api.services.AmbariMetaInfoTest.metaInfo.getComponent(org.apache.ambari.server.api.services.AmbariMetaInfoTest.STACK_NAME_HDP, "2.0.8", "HDFS", "NAMENODE");
        junit.framework.Assert.assertEquals(4, component.getCustomCommands().size());
        ccd = findCustomCommand("YET_ANOTHER_PARENT_COMMAND", component);
        junit.framework.Assert.assertEquals("scripts/yet_another_parent_command.py", ccd.getCommandScript().getScript());
        ccd = findCustomCommand("DECOMMISSION", component);
        junit.framework.Assert.assertEquals("scripts/namenode_dec_overr.py", ccd.getCommandScript().getScript());
        ccd = findCustomCommand("YET_ANOTHER_CHILD_COMMAND", component);
        junit.framework.Assert.assertEquals("scripts/yet_another_child_command.py", ccd.getCommandScript().getScript());
    }

    @org.junit.Test
    public void testServiceOsSpecificsInheritance() throws java.lang.Exception {
        org.apache.ambari.server.state.ServiceInfo service = org.apache.ambari.server.api.services.AmbariMetaInfoTest.metaInfo.getService(org.apache.ambari.server.api.services.AmbariMetaInfoTest.STACK_NAME_HDP, "2.0.7", "HDFS");
        junit.framework.Assert.assertEquals("parent-package-def", service.getOsSpecifics().get("any").getPackages().get(0).getName());
        service = org.apache.ambari.server.api.services.AmbariMetaInfoTest.metaInfo.getService(org.apache.ambari.server.api.services.AmbariMetaInfoTest.STACK_NAME_HDP, "2.0.7", "HBASE");
        junit.framework.Assert.assertEquals(2, service.getOsSpecifics().keySet().size());
        service = org.apache.ambari.server.api.services.AmbariMetaInfoTest.metaInfo.getService(org.apache.ambari.server.api.services.AmbariMetaInfoTest.STACK_NAME_HDP, "2.0.8", "HBASE");
        junit.framework.Assert.assertEquals(2, service.getOsSpecifics().keySet().size());
        service = org.apache.ambari.server.api.services.AmbariMetaInfoTest.metaInfo.getService(org.apache.ambari.server.api.services.AmbariMetaInfoTest.STACK_NAME_HDP, "2.0.8", "HDFS");
        junit.framework.Assert.assertEquals("child-package-def", service.getOsSpecifics().get("any").getPackages().get(0).getName());
    }

    @org.junit.Test
    public void testServiceSchemaVersionInheritance() throws java.lang.Exception {
        org.apache.ambari.server.state.ServiceInfo service = org.apache.ambari.server.api.services.AmbariMetaInfoTest.metaInfo.getService(org.apache.ambari.server.api.services.AmbariMetaInfoTest.STACK_NAME_HDP, "2.0.7", "SQOOP");
        junit.framework.Assert.assertEquals("2.0", service.getSchemaVersion());
        service = org.apache.ambari.server.api.services.AmbariMetaInfoTest.metaInfo.getService(org.apache.ambari.server.api.services.AmbariMetaInfoTest.STACK_NAME_HDP, "2.0.8", "SQOOP");
        junit.framework.Assert.assertEquals("2.0", service.getSchemaVersion());
    }

    private org.apache.ambari.server.state.CustomCommandDefinition findCustomCommand(java.lang.String ccName, org.apache.ambari.server.state.ServiceInfo service) {
        for (org.apache.ambari.server.state.CustomCommandDefinition ccd : service.getCustomCommands()) {
            if (ccd.getName().equals(ccName)) {
                return ccd;
            }
        }
        return null;
    }

    private org.apache.ambari.server.state.CustomCommandDefinition findCustomCommand(java.lang.String ccName, org.apache.ambari.server.state.ComponentInfo component) {
        for (org.apache.ambari.server.state.CustomCommandDefinition ccd : component.getCustomCommands()) {
            if (ccd.getName().equals(ccName)) {
                return ccd;
            }
        }
        return null;
    }

    @org.junit.Test
    public void testCustomConfigDir() throws java.lang.Exception {
        org.apache.ambari.server.state.ServiceInfo service = org.apache.ambari.server.api.services.AmbariMetaInfoTest.metaInfo.getService(org.apache.ambari.server.api.services.AmbariMetaInfoTest.STACK_NAME_HDP, "2.0.7", "MAPREDUCE2");
        boolean found = false;
        for (org.apache.ambari.server.state.PropertyInfo pi : service.getProperties()) {
            if (pi.getName().equals("mr2-prop")) {
                junit.framework.Assert.assertEquals("some-mr2-value", pi.getValue());
                found = true;
            }
        }
        junit.framework.Assert.assertTrue(found);
    }

    @org.junit.Test
    public void testLatestRepo() throws java.lang.Exception {
        org.apache.ambari.server.stack.StackManager sm = org.apache.ambari.server.api.services.AmbariMetaInfoTest.metaInfo.getStackManager();
        int maxWait = 45000;
        int waitTime = 0;
        while ((waitTime < maxWait) && (!sm.haveAllRepoUrlsBeenResolved())) {
            java.lang.Thread.sleep(5);
            waitTime += 5;
        } 
        if (waitTime >= maxWait) {
            org.junit.Assert.fail("Latest Repo tasks did not complete");
        }
        for (org.apache.ambari.server.state.RepositoryInfo ri : org.apache.ambari.server.api.services.AmbariMetaInfoTest.metaInfo.getRepositories("HDP", "2.1.1", "centos6")) {
            junit.framework.Assert.assertEquals("Expected the default URL to be the same as in the xml file", "http://public-repo-1.hortonworks.com/HDP/centos6/2.x/updates/2.0.6.0", ri.getDefaultBaseUrl());
        }
    }

    @org.junit.Test
    public void testLatestVdf() throws java.lang.Exception {
        org.apache.ambari.server.stack.StackManager sm = org.apache.ambari.server.api.services.AmbariMetaInfoTest.metaInfo.getStackManager();
        int maxWait = 45000;
        int waitTime = 0;
        while ((waitTime < maxWait) && (!sm.haveAllRepoUrlsBeenResolved())) {
            java.lang.Thread.sleep(5);
            waitTime += 5;
        } 
        if (waitTime >= maxWait) {
            org.junit.Assert.fail("Latest Repo tasks did not complete");
        }
        org.apache.ambari.server.state.repository.VersionDefinitionXml vdf = org.apache.ambari.server.api.services.AmbariMetaInfoTest.metaInfo.getVersionDefinition("HDP-2.2.0");
        org.junit.Assert.assertNotNull(vdf);
        org.junit.Assert.assertEquals(1, vdf.repositoryInfo.getOses().size());
        vdf = org.apache.ambari.server.api.services.AmbariMetaInfoTest.metaInfo.getVersionDefinition("HDP-2.2.1");
        org.junit.Assert.assertNotNull(vdf);
        org.junit.Assert.assertEquals(2, vdf.repositoryInfo.getOses().size());
    }

    @org.junit.Test
    public void testGetComponentDependency() throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.server.state.DependencyInfo dependency = org.apache.ambari.server.api.services.AmbariMetaInfoTest.metaInfo.getComponentDependency("HDP", "1.3.4", "HIVE", "HIVE_SERVER", "ZOOKEEPER_SERVER");
        org.junit.Assert.assertEquals("ZOOKEEPER/ZOOKEEPER_SERVER", dependency.getName());
        org.junit.Assert.assertEquals("ZOOKEEPER_SERVER", dependency.getComponentName());
        org.junit.Assert.assertEquals("ZOOKEEPER", dependency.getServiceName());
        org.junit.Assert.assertEquals("cluster", dependency.getScope());
    }

    @org.junit.Test
    public void testGetComponentDependencies() throws org.apache.ambari.server.AmbariException {
        java.util.List<org.apache.ambari.server.state.DependencyInfo> dependencies = org.apache.ambari.server.api.services.AmbariMetaInfoTest.metaInfo.getComponentDependencies("HDP", "1.3.4", "HBASE", "HBASE_MASTER");
        org.junit.Assert.assertEquals(2, dependencies.size());
        org.apache.ambari.server.state.DependencyInfo dependency = dependencies.get(0);
        org.junit.Assert.assertEquals("HDFS/HDFS_CLIENT", dependency.getName());
        org.junit.Assert.assertEquals("HDFS_CLIENT", dependency.getComponentName());
        org.junit.Assert.assertEquals("HDFS", dependency.getServiceName());
        org.junit.Assert.assertEquals("host", dependency.getScope());
        dependency = dependencies.get(1);
        org.junit.Assert.assertEquals("ZOOKEEPER/ZOOKEEPER_SERVER", dependency.getName());
        org.junit.Assert.assertEquals("ZOOKEEPER_SERVER", dependency.getComponentName());
        org.junit.Assert.assertEquals("ZOOKEEPER", dependency.getServiceName());
        org.junit.Assert.assertEquals("cluster", dependency.getScope());
    }

    @org.junit.Test
    public void testPasswordPropertyAttribute() throws java.lang.Exception {
        org.apache.ambari.server.state.ServiceInfo service = org.apache.ambari.server.api.services.AmbariMetaInfoTest.metaInfo.getService(org.apache.ambari.server.api.services.AmbariMetaInfoTest.STACK_NAME_HDP, "2.0.1", "HIVE");
        java.util.List<org.apache.ambari.server.state.PropertyInfo> propertyInfoList = service.getProperties();
        junit.framework.Assert.assertNotNull(propertyInfoList);
        org.apache.ambari.server.state.PropertyInfo passwordProperty = null;
        for (org.apache.ambari.server.state.PropertyInfo propertyInfo : propertyInfoList) {
            if (propertyInfo.isRequireInput() && propertyInfo.getPropertyTypes().contains(org.apache.ambari.server.state.PropertyInfo.PropertyType.PASSWORD)) {
                passwordProperty = propertyInfo;
            } else {
                junit.framework.Assert.assertTrue(propertyInfo.getPropertyTypes().isEmpty());
            }
        }
        junit.framework.Assert.assertNotNull(passwordProperty);
        junit.framework.Assert.assertEquals("javax.jdo.option.ConnectionPassword", passwordProperty.getName());
    }

    @org.junit.Test
    public void testAlertsJson() throws java.lang.Exception {
        org.apache.ambari.server.state.ServiceInfo svc = org.apache.ambari.server.api.services.AmbariMetaInfoTest.metaInfo.getService(org.apache.ambari.server.api.services.AmbariMetaInfoTest.STACK_NAME_HDP, "2.0.5", "HDFS");
        junit.framework.Assert.assertNotNull(svc);
        junit.framework.Assert.assertNotNull(svc.getAlertsFile());
        svc = org.apache.ambari.server.api.services.AmbariMetaInfoTest.metaInfo.getService(org.apache.ambari.server.api.services.AmbariMetaInfoTest.STACK_NAME_HDP, "2.0.6", "HDFS");
        junit.framework.Assert.assertNotNull(svc);
        junit.framework.Assert.assertNotNull(svc.getAlertsFile());
        svc = org.apache.ambari.server.api.services.AmbariMetaInfoTest.metaInfo.getService(org.apache.ambari.server.api.services.AmbariMetaInfoTest.STACK_NAME_HDP, "1.3.4", "HDFS");
        junit.framework.Assert.assertNotNull(svc);
        junit.framework.Assert.assertNull(svc.getAlertsFile());
        java.util.Set<org.apache.ambari.server.state.alert.AlertDefinition> set = org.apache.ambari.server.api.services.AmbariMetaInfoTest.metaInfo.getAlertDefinitions(org.apache.ambari.server.api.services.AmbariMetaInfoTest.STACK_NAME_HDP, "2.0.5", "HDFS");
        junit.framework.Assert.assertNotNull(set);
        junit.framework.Assert.assertTrue(set.size() > 0);
        org.apache.ambari.server.state.alert.AlertDefinition nameNodeProcess = null;
        org.apache.ambari.server.state.alert.AlertDefinition nameNodeCpu = null;
        org.apache.ambari.server.state.alert.AlertDefinition datanodeStorage = null;
        org.apache.ambari.server.state.alert.AlertDefinition ignoreHost = null;
        java.util.Iterator<org.apache.ambari.server.state.alert.AlertDefinition> iterator = set.iterator();
        while (iterator.hasNext()) {
            org.apache.ambari.server.state.alert.AlertDefinition definition = iterator.next();
            if (definition.getName().equals("namenode_process")) {
                nameNodeProcess = definition;
            }
            if (definition.getName().equals("namenode_cpu")) {
                nameNodeCpu = definition;
            }
            if (definition.getName().equals("datanode_storage")) {
                datanodeStorage = definition;
            }
            if (definition.getName().equals("hdfs_ignore_host_test")) {
                ignoreHost = definition;
            }
        } 
        org.junit.Assert.assertNotNull(nameNodeProcess);
        org.junit.Assert.assertNotNull(nameNodeCpu);
        org.junit.Assert.assertNotNull(ignoreHost);
        org.junit.Assert.assertEquals("NameNode Host CPU Utilization", nameNodeCpu.getLabel());
        org.junit.Assert.assertFalse(nameNodeProcess.isHostIgnored());
        org.junit.Assert.assertEquals("A description of namenode_process", nameNodeProcess.getDescription());
        org.apache.ambari.server.state.alert.Source source = nameNodeProcess.getSource();
        org.junit.Assert.assertNotNull(source);
        org.junit.Assert.assertNotNull(((org.apache.ambari.server.state.alert.PortSource) (source)).getPort());
        org.apache.ambari.server.state.alert.Reporting reporting = source.getReporting();
        org.junit.Assert.assertNotNull(reporting);
        org.junit.Assert.assertNotNull(reporting.getOk());
        org.junit.Assert.assertNotNull(reporting.getOk().getText());
        org.junit.Assert.assertNull(reporting.getOk().getValue());
        org.junit.Assert.assertNotNull(reporting.getCritical());
        org.junit.Assert.assertNotNull(reporting.getCritical().getText());
        org.junit.Assert.assertNull(reporting.getCritical().getValue());
        org.junit.Assert.assertNull(reporting.getWarning());
        org.junit.Assert.assertFalse(nameNodeCpu.isHostIgnored());
        org.junit.Assert.assertEquals("A description of namenode_cpu", nameNodeCpu.getDescription());
        source = nameNodeCpu.getSource();
        org.junit.Assert.assertNotNull(source);
        reporting = source.getReporting();
        org.junit.Assert.assertNotNull(reporting);
        org.junit.Assert.assertNotNull(reporting.getOk());
        org.junit.Assert.assertNotNull(reporting.getOk().getText());
        org.junit.Assert.assertNull(reporting.getOk().getValue());
        org.junit.Assert.assertNotNull(reporting.getCritical());
        org.junit.Assert.assertNotNull(reporting.getCritical().getText());
        org.junit.Assert.assertNotNull(reporting.getCritical().getValue());
        org.junit.Assert.assertNotNull(reporting.getWarning());
        org.junit.Assert.assertNotNull(reporting.getWarning().getText());
        org.junit.Assert.assertNotNull(reporting.getWarning().getValue());
        org.junit.Assert.assertNotNull(datanodeStorage);
        org.junit.Assert.assertEquals("A description of datanode_storage", datanodeStorage.getDescription());
        org.junit.Assert.assertFalse(datanodeStorage.isHostIgnored());
        org.apache.ambari.server.state.alert.MetricSource metricSource = ((org.apache.ambari.server.state.alert.MetricSource) (datanodeStorage.getSource()));
        org.junit.Assert.assertNotNull(metricSource.getUri());
        org.junit.Assert.assertNotNull(metricSource.getUri().getHttpsProperty());
        org.junit.Assert.assertNotNull(metricSource.getUri().getHttpsPropertyValue());
        org.junit.Assert.assertNotNull(metricSource.getUri().getHttpsUri());
        org.junit.Assert.assertNotNull(metricSource.getUri().getHttpUri());
        org.junit.Assert.assertEquals(12345, metricSource.getUri().getDefaultPort().intValue());
        org.junit.Assert.assertTrue(ignoreHost.isHostIgnored());
    }

    @org.junit.Test
    public void testAlertDefinitionMerging() throws java.lang.Exception {
        final java.lang.String stackVersion = "2.0.6";
        final java.lang.String repoVersion = "2.0.6-1234";
        com.google.inject.Injector injector = com.google.inject.Guice.createInjector(com.google.inject.util.Modules.override(new org.apache.ambari.server.orm.InMemoryDefaultTestModule()).with(new org.apache.ambari.server.api.services.AmbariMetaInfoTest.MockModule()));
        org.apache.ambari.server.utils.EventBusSynchronizer.synchronizeAmbariEventPublisher(injector);
        injector.getInstance(org.apache.ambari.server.orm.GuiceJpaInitializer.class);
        injector.getInstance(javax.persistence.EntityManager.class);
        org.apache.ambari.server.orm.OrmTestHelper ormHelper = injector.getInstance(org.apache.ambari.server.orm.OrmTestHelper.class);
        long clusterId = ormHelper.createCluster("cluster" + java.lang.System.currentTimeMillis());
        java.lang.Class<?> c = org.apache.ambari.server.api.services.AmbariMetaInfoTest.metaInfo.getClass().getSuperclass();
        java.lang.reflect.Field f = c.getDeclaredField("alertDefinitionDao");
        f.setAccessible(true);
        f.set(org.apache.ambari.server.api.services.AmbariMetaInfoTest.metaInfo, injector.getInstance(org.apache.ambari.server.orm.dao.AlertDefinitionDAO.class));
        f = c.getDeclaredField("ambariServiceAlertDefinitions");
        f.setAccessible(true);
        f.set(org.apache.ambari.server.api.services.AmbariMetaInfoTest.metaInfo, injector.getInstance(org.apache.ambari.server.metadata.AmbariServiceAlertDefinitions.class));
        org.apache.ambari.server.state.Clusters clusters = injector.getInstance(org.apache.ambari.server.state.Clusters.class);
        org.apache.ambari.server.state.Cluster cluster = clusters.getClusterById(clusterId);
        cluster.setDesiredStackVersion(new org.apache.ambari.server.state.StackId(org.apache.ambari.server.api.services.AmbariMetaInfoTest.STACK_NAME_HDP, stackVersion));
        org.apache.ambari.server.orm.entities.RepositoryVersionEntity repositoryVersion = ormHelper.getOrCreateRepositoryVersion(cluster.getCurrentStackVersion(), repoVersion);
        cluster.addService("HDFS", repositoryVersion);
        org.apache.ambari.server.api.services.AmbariMetaInfoTest.metaInfo.reconcileAlertDefinitions(clusters, false);
        org.apache.ambari.server.orm.dao.AlertDefinitionDAO dao = injector.getInstance(org.apache.ambari.server.orm.dao.AlertDefinitionDAO.class);
        java.util.List<org.apache.ambari.server.orm.entities.AlertDefinitionEntity> definitions = dao.findAll(clusterId);
        org.junit.Assert.assertEquals(13, definitions.size());
        int hostAlertCount = 0;
        for (org.apache.ambari.server.orm.entities.AlertDefinitionEntity definition : definitions) {
            if (definition.getServiceName().equals("AMBARI") && definition.getComponentName().equals("AMBARI_AGENT")) {
                hostAlertCount++;
            }
        }
        org.junit.Assert.assertEquals(3, hostAlertCount);
        org.junit.Assert.assertEquals(10, definitions.size() - hostAlertCount);
        for (org.apache.ambari.server.orm.entities.AlertDefinitionEntity definition : definitions) {
            definition.setScheduleInterval(28);
            dao.merge(definition);
        }
        org.apache.ambari.server.api.services.AmbariMetaInfoTest.metaInfo.reconcileAlertDefinitions(clusters, false);
        definitions = dao.findAll();
        org.junit.Assert.assertEquals(13, definitions.size());
        for (org.apache.ambari.server.orm.entities.AlertDefinitionEntity definition : definitions) {
            org.junit.Assert.assertEquals(28, definition.getScheduleInterval().intValue());
        }
        definitions = dao.findAllEnabled(cluster.getClusterId());
        org.junit.Assert.assertEquals(12, definitions.size());
        org.apache.ambari.server.orm.entities.AlertDefinitionEntity entity = new org.apache.ambari.server.orm.entities.AlertDefinitionEntity();
        entity.setClusterId(clusterId);
        entity.setDefinitionName("bad_hdfs_alert");
        entity.setLabel("Bad HDFS Alert");
        entity.setDescription("A way to fake a component being removed");
        entity.setEnabled(true);
        entity.setHash(java.util.UUID.randomUUID().toString());
        entity.setScheduleInterval(1);
        entity.setServiceName("HDFS");
        entity.setComponentName("BAD_COMPONENT");
        entity.setSourceType(org.apache.ambari.server.state.alert.SourceType.METRIC);
        entity.setSource("{\"type\" : \"METRIC\"}");
        dao.create(entity);
        definitions = dao.findAllEnabled(cluster.getClusterId());
        org.junit.Assert.assertEquals(13, definitions.size());
        org.apache.ambari.server.api.services.AmbariMetaInfoTest.metaInfo.reconcileAlertDefinitions(clusters, false);
        definitions = dao.findAllEnabled(cluster.getClusterId());
        org.junit.Assert.assertEquals(12, definitions.size());
        definitions = dao.findAll();
        org.junit.Assert.assertEquals(14, definitions.size());
        entity = dao.findById(entity.getDefinitionId());
        org.junit.Assert.assertFalse(entity.getEnabled());
    }

    @org.junit.Test
    public void testAlertDefinitionMergingRemoveScenario() throws java.lang.Exception {
        final java.lang.String repoVersion = "2.0.6-1234";
        final java.lang.String stackVersion = "2.0.6";
        com.google.inject.Injector injector = com.google.inject.Guice.createInjector(com.google.inject.util.Modules.override(new org.apache.ambari.server.orm.InMemoryDefaultTestModule()).with(new org.apache.ambari.server.api.services.AmbariMetaInfoTest.MockModule()));
        org.apache.ambari.server.utils.EventBusSynchronizer.synchronizeAmbariEventPublisher(injector);
        injector.getInstance(org.apache.ambari.server.orm.GuiceJpaInitializer.class);
        injector.getInstance(javax.persistence.EntityManager.class);
        org.apache.ambari.server.orm.OrmTestHelper ormHelper = injector.getInstance(org.apache.ambari.server.orm.OrmTestHelper.class);
        long clusterId = ormHelper.createCluster("cluster" + java.lang.System.currentTimeMillis());
        java.lang.Class<?> c = org.apache.ambari.server.api.services.AmbariMetaInfoTest.metaInfo.getClass().getSuperclass();
        java.lang.reflect.Field f = c.getDeclaredField("alertDefinitionDao");
        f.setAccessible(true);
        f.set(org.apache.ambari.server.api.services.AmbariMetaInfoTest.metaInfo, injector.getInstance(org.apache.ambari.server.orm.dao.AlertDefinitionDAO.class));
        f = c.getDeclaredField("ambariServiceAlertDefinitions");
        f.setAccessible(true);
        f.set(org.apache.ambari.server.api.services.AmbariMetaInfoTest.metaInfo, injector.getInstance(org.apache.ambari.server.metadata.AmbariServiceAlertDefinitions.class));
        org.apache.ambari.server.state.Clusters clusters = injector.getInstance(org.apache.ambari.server.state.Clusters.class);
        org.apache.ambari.server.state.Cluster cluster = clusters.getClusterById(clusterId);
        cluster.setDesiredStackVersion(new org.apache.ambari.server.state.StackId(org.apache.ambari.server.api.services.AmbariMetaInfoTest.STACK_NAME_HDP, stackVersion));
        org.apache.ambari.server.orm.entities.RepositoryVersionEntity repositoryVersion = ormHelper.getOrCreateRepositoryVersion(cluster.getCurrentStackVersion(), repoVersion);
        cluster.addService("HDFS", repositoryVersion);
        org.apache.ambari.server.api.services.AmbariMetaInfoTest.metaInfo.reconcileAlertDefinitions(clusters, false);
        org.apache.ambari.server.orm.dao.AlertDefinitionDAO dao = injector.getInstance(org.apache.ambari.server.orm.dao.AlertDefinitionDAO.class);
        java.util.List<org.apache.ambari.server.orm.entities.AlertDefinitionEntity> definitions = dao.findAll(clusterId);
        org.junit.Assert.assertEquals(13, definitions.size());
        cluster.deleteService("HDFS", new org.apache.ambari.server.controller.internal.DeleteHostComponentStatusMetaData());
        org.apache.ambari.server.api.services.AmbariMetaInfoTest.metaInfo.reconcileAlertDefinitions(clusters, false);
        java.util.List<org.apache.ambari.server.orm.entities.AlertDefinitionEntity> updatedDefinitions = dao.findAll(clusterId);
        org.junit.Assert.assertEquals(7, updatedDefinitions.size());
    }

    @org.junit.Test
    public void testKerberosDescriptor() throws java.lang.Exception {
        org.apache.ambari.server.state.ServiceInfo service;
        service = org.apache.ambari.server.api.services.AmbariMetaInfoTest.metaInfo.getService(org.apache.ambari.server.api.services.AmbariMetaInfoTest.STACK_NAME_HDP, "2.1.1", "PIG");
        junit.framework.Assert.assertNotNull(service);
        junit.framework.Assert.assertNull(service.getKerberosDescriptorFile());
        service = org.apache.ambari.server.api.services.AmbariMetaInfoTest.metaInfo.getService(org.apache.ambari.server.api.services.AmbariMetaInfoTest.STACK_NAME_HDP, "2.0.8", "HDFS");
        junit.framework.Assert.assertNotNull(service);
        junit.framework.Assert.assertNotNull(service.getKerberosDescriptorFile());
        service = org.apache.ambari.server.api.services.AmbariMetaInfoTest.metaInfo.getService(org.apache.ambari.server.api.services.AmbariMetaInfoTest.STACK_NAME_HDP, "2.1.1", "HDFS");
        junit.framework.Assert.assertNotNull(service);
        junit.framework.Assert.assertNotNull(service.getKerberosDescriptorFile());
        java.util.Map<?, ?> kerberosDescriptorData = new com.google.gson.Gson().fromJson(new java.io.FileReader(service.getKerberosDescriptorFile()), java.util.Map.class);
        junit.framework.Assert.assertNotNull(kerberosDescriptorData);
        junit.framework.Assert.assertEquals(1, kerberosDescriptorData.size());
    }

    @org.junit.Test
    public void testReadKerberosDescriptorFromFile() throws org.apache.ambari.server.AmbariException {
        java.lang.String path = org.apache.ambari.server.api.services.AmbariMetaInfoTest.metaInfo.getCommonKerberosDescriptorFileLocation();
        org.apache.ambari.server.state.kerberos.KerberosDescriptor descriptor = org.apache.ambari.server.api.services.AmbariMetaInfoTest.metaInfo.readKerberosDescriptorFromFile(path);
        junit.framework.Assert.assertNotNull(descriptor);
        junit.framework.Assert.assertNotNull(descriptor.getProperties());
        junit.framework.Assert.assertEquals(3, descriptor.getProperties().size());
        junit.framework.Assert.assertNotNull(descriptor.getIdentities());
        junit.framework.Assert.assertEquals(1, descriptor.getIdentities().size());
        junit.framework.Assert.assertEquals("spnego", descriptor.getIdentities().get(0).getName());
        junit.framework.Assert.assertNotNull(descriptor.getConfigurations());
        junit.framework.Assert.assertEquals(1, descriptor.getConfigurations().size());
        junit.framework.Assert.assertNotNull(descriptor.getConfigurations().get("core-site"));
        junit.framework.Assert.assertNotNull(descriptor.getConfiguration("core-site"));
        junit.framework.Assert.assertNull(descriptor.getServices());
    }

    @org.junit.Test
    public void testGetKerberosDescriptor() throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.server.state.kerberos.KerberosDescriptor descriptor = org.apache.ambari.server.api.services.AmbariMetaInfoTest.metaInfo.getKerberosDescriptor(org.apache.ambari.server.api.services.AmbariMetaInfoTest.STACK_NAME_HDP, "2.0.8", false);
        junit.framework.Assert.assertNotNull(descriptor);
        junit.framework.Assert.assertNotNull(descriptor.getProperties());
        junit.framework.Assert.assertEquals(3, descriptor.getProperties().size());
        junit.framework.Assert.assertNotNull(descriptor.getIdentities());
        junit.framework.Assert.assertEquals(1, descriptor.getIdentities().size());
        junit.framework.Assert.assertEquals("spnego", descriptor.getIdentities().get(0).getName());
        junit.framework.Assert.assertNotNull(descriptor.getConfigurations());
        junit.framework.Assert.assertEquals(1, descriptor.getConfigurations().size());
        junit.framework.Assert.assertNotNull(descriptor.getConfigurations().get("core-site"));
        junit.framework.Assert.assertNotNull(descriptor.getConfiguration("core-site"));
        junit.framework.Assert.assertNotNull(descriptor.getServices());
        junit.framework.Assert.assertEquals(1, descriptor.getServices().size());
        junit.framework.Assert.assertNotNull(descriptor.getServices().get("HDFS"));
        junit.framework.Assert.assertNotNull(descriptor.getService("HDFS"));
        junit.framework.Assert.assertFalse(descriptor.getService("HDFS").shouldPreconfigure());
    }

    @org.junit.Test
    public void testGetKerberosDescriptorWithPreconfigure() throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.server.state.kerberos.KerberosDescriptor descriptor = org.apache.ambari.server.api.services.AmbariMetaInfoTest.metaInfo.getKerberosDescriptor(org.apache.ambari.server.api.services.AmbariMetaInfoTest.STACK_NAME_HDP, "2.0.8", true);
        junit.framework.Assert.assertNotNull(descriptor);
        junit.framework.Assert.assertNotNull(descriptor.getProperties());
        junit.framework.Assert.assertEquals(3, descriptor.getProperties().size());
        junit.framework.Assert.assertNotNull(descriptor.getIdentities());
        junit.framework.Assert.assertEquals(1, descriptor.getIdentities().size());
        junit.framework.Assert.assertEquals("spnego", descriptor.getIdentities().get(0).getName());
        junit.framework.Assert.assertNotNull(descriptor.getConfigurations());
        junit.framework.Assert.assertEquals(1, descriptor.getConfigurations().size());
        junit.framework.Assert.assertNotNull(descriptor.getConfigurations().get("core-site"));
        junit.framework.Assert.assertNotNull(descriptor.getConfiguration("core-site"));
        junit.framework.Assert.assertNotNull(descriptor.getServices());
        junit.framework.Assert.assertEquals(2, descriptor.getServices().size());
        junit.framework.Assert.assertNotNull(descriptor.getServices().get("HDFS"));
        junit.framework.Assert.assertNotNull(descriptor.getService("HDFS"));
        junit.framework.Assert.assertTrue(descriptor.getService("HDFS").shouldPreconfigure());
        junit.framework.Assert.assertNotNull(descriptor.getServices().get("HDFS"));
        junit.framework.Assert.assertNotNull(descriptor.getService("HDFS"));
        junit.framework.Assert.assertTrue(descriptor.getService("HDFS").shouldPreconfigure());
        junit.framework.Assert.assertNotNull(descriptor.getServices().get("NEW_SERVICE"));
        junit.framework.Assert.assertNotNull(descriptor.getService("NEW_SERVICE"));
        junit.framework.Assert.assertTrue(descriptor.getService("NEW_SERVICE").shouldPreconfigure());
    }

    @org.junit.Test
    public void testGetCommonWidgetsFile() throws org.apache.ambari.server.AmbariException {
        java.io.File widgetsFile = org.apache.ambari.server.api.services.AmbariMetaInfoTest.metaInfo.getCommonWidgetsDescriptorFile();
        junit.framework.Assert.assertNotNull(widgetsFile);
        junit.framework.Assert.assertEquals("src/test/resources/widgets.json", widgetsFile.getPath());
    }

    @org.junit.Test
    public void testGetVersionDefinitionsForDisabledStack() throws org.apache.ambari.server.AmbariException {
        java.util.Map<java.lang.String, org.apache.ambari.server.state.repository.VersionDefinitionXml> versionDefinitions = org.apache.ambari.server.api.services.AmbariMetaInfoTest.metaInfo.getVersionDefinitions();
        junit.framework.Assert.assertNotNull(versionDefinitions);
        java.util.Map.Entry<java.lang.String, org.apache.ambari.server.state.repository.VersionDefinitionXml> vdfEntry = null;
        for (java.util.Map.Entry<java.lang.String, org.apache.ambari.server.state.repository.VersionDefinitionXml> entry : versionDefinitions.entrySet()) {
            if (entry.getKey().equals("HDP-2.2.1")) {
                vdfEntry = entry;
            }
        }
        junit.framework.Assert.assertNotNull("Candidate stack and vdf for test case.", vdfEntry);
        org.apache.ambari.server.state.StackInfo stackInfo = org.apache.ambari.server.api.services.AmbariMetaInfoTest.metaInfo.getStack("HDP", "2.2.1");
        stackInfo.setActive(false);
        org.apache.ambari.server.api.services.AmbariMetaInfoTest.metaInfo.versionDefinitions = null;
        versionDefinitions = org.apache.ambari.server.api.services.AmbariMetaInfoTest.metaInfo.getVersionDefinitions();
        vdfEntry = null;
        for (java.util.Map.Entry<java.lang.String, org.apache.ambari.server.state.repository.VersionDefinitionXml> entry : versionDefinitions.entrySet()) {
            if (entry.getKey().equals("HDP-2.2.1")) {
                vdfEntry = entry;
            }
        }
        junit.framework.Assert.assertNull("Disabled stack should not be returned by the API", vdfEntry);
    }

    private java.io.File getStackRootTmp(java.lang.String buildDir) {
        return new java.io.File(buildDir + "/ambari-metaInfo");
    }

    private java.io.File getVersion() {
        java.io.File version = new java.io.File("src/test/resources/version");
        if (java.lang.System.getProperty("os.name").contains("Windows")) {
            version = new java.io.File(new java.io.File(java.lang.ClassLoader.getSystemClassLoader().getResource("").getPath()).getParent(), "version");
        }
        return version;
    }

    private void setupTempAmbariMetaInfoDirs(java.lang.String buildDir) throws java.lang.Exception {
        java.io.File stackRootTmp = getStackRootTmp(buildDir);
        java.io.File stackRoot = new java.io.File("src/test/resources/stacks");
        if (java.lang.System.getProperty("os.name").contains("Windows")) {
            stackRoot = new java.io.File(java.lang.ClassLoader.getSystemClassLoader().getResource("stacks").getPath());
        }
        stackRootTmp.mkdir();
        org.apache.commons.io.FileUtils.copyDirectory(stackRoot, stackRootTmp);
    }

    private org.apache.ambari.server.api.services.AmbariMetaInfoTest.TestAmbariMetaInfo setupTempAmbariMetaInfo(java.lang.String buildDir) throws java.lang.Exception {
        setupTempAmbariMetaInfoDirs(buildDir);
        org.apache.ambari.server.api.services.AmbariMetaInfoTest.TestAmbariMetaInfo ambariMetaInfo = setupTempAmbariMetaInfoExistingDirs(buildDir);
        return ambariMetaInfo;
    }

    private org.apache.ambari.server.api.services.AmbariMetaInfoTest.TestAmbariMetaInfo setupTempAmbariMetaInfoExistingDirs(java.lang.String buildDir) throws java.lang.Exception {
        java.io.File version = getVersion();
        java.io.File stackRootTmp = getStackRootTmp(buildDir);
        org.apache.ambari.server.api.services.AmbariMetaInfoTest.TestAmbariMetaInfo ambariMetaInfo = org.apache.ambari.server.api.services.AmbariMetaInfoTest.createAmbariMetaInfo(stackRootTmp, version, new java.io.File(""));
        return ambariMetaInfo;
    }

    private static org.apache.ambari.server.api.services.AmbariMetaInfoTest.TestAmbariMetaInfo createAmbariMetaInfo(java.io.File stackRoot, java.io.File versionFile, java.io.File resourcesRoot) throws java.lang.Exception {
        java.util.Properties properties = new java.util.Properties();
        properties.setProperty(org.apache.ambari.server.configuration.Configuration.METADATA_DIR_PATH.getKey(), stackRoot.getPath());
        properties.setProperty(org.apache.ambari.server.configuration.Configuration.SERVER_VERSION_FILE.getKey(), versionFile.getPath());
        properties.setProperty(org.apache.ambari.server.configuration.Configuration.RESOURCES_DIR.getKey(), resourcesRoot.getPath());
        properties.setProperty(org.apache.ambari.server.configuration.Configuration.MPACKS_V2_STAGING_DIR_PATH.getKey(), "src/test/resources/mpacks-v2");
        org.apache.ambari.server.configuration.Configuration configuration = new org.apache.ambari.server.configuration.Configuration(properties);
        org.apache.ambari.server.api.services.AmbariMetaInfoTest.TestAmbariMetaInfo metaInfo = new org.apache.ambari.server.api.services.AmbariMetaInfoTest.TestAmbariMetaInfo(configuration);
        metaInfo.replayAllMocks();
        try {
            metaInfo.init();
        } catch (java.lang.Exception e) {
            org.apache.ambari.server.api.services.AmbariMetaInfoTest.LOG.info("Error in initializing ", e);
            throw e;
        }
        org.apache.ambari.server.api.services.AmbariMetaInfoTest.waitForAllReposToBeResolved(metaInfo);
        return metaInfo;
    }

    private static void checkNoAggregatedFunctionsForJmx(java.util.List<org.apache.ambari.server.state.stack.MetricDefinition> metricDefinitions) {
        for (org.apache.ambari.server.state.stack.MetricDefinition metricDefinition : metricDefinitions) {
            if ("jmx".equals(metricDefinition.getType())) {
                for (java.lang.String metric : metricDefinition.getMetrics().keySet()) {
                    if (metric.endsWith("._sum")) {
                        junit.framework.Assert.fail("Aggregated functions aren't supported for JMX metrics. " + metric);
                    }
                }
            }
        }
    }

    private static void waitForAllReposToBeResolved(org.apache.ambari.server.api.services.AmbariMetaInfo metaInfo) throws java.lang.Exception {
        int maxWait = 45000;
        int waitTime = 0;
        org.apache.ambari.server.stack.StackManager sm = metaInfo.getStackManager();
        while ((waitTime < maxWait) && (!sm.haveAllRepoUrlsBeenResolved())) {
            java.lang.Thread.sleep(5);
            waitTime += 5;
        } 
        if (waitTime >= maxWait) {
            org.junit.Assert.fail("Latest Repo tasks did not complete");
        }
    }

    private static class TestAmbariMetaInfo extends org.apache.ambari.server.api.services.AmbariMetaInfo {
        org.apache.ambari.server.orm.dao.AlertDefinitionDAO alertDefinitionDAO;

        org.apache.ambari.server.state.alert.AlertDefinitionFactory alertDefinitionFactory;

        org.apache.ambari.server.state.stack.OsFamily osFamily;

        com.google.inject.Injector injector;

        public TestAmbariMetaInfo(org.apache.ambari.server.configuration.Configuration configuration) throws java.lang.Exception {
            super(configuration);
            injector = com.google.inject.Guice.createInjector(com.google.inject.util.Modules.override(new org.apache.ambari.server.orm.InMemoryDefaultTestModule()).with(new org.apache.ambari.server.api.services.AmbariMetaInfoTest.TestAmbariMetaInfo.MockModule()));
            injector.getInstance(org.apache.ambari.server.orm.GuiceJpaInitializer.class);
            injector.getInstance(javax.persistence.EntityManager.class);
            java.lang.Class<?> c = getClass().getSuperclass();
            org.apache.ambari.server.stack.StackManagerFactory stackManagerFactory = injector.getInstance(org.apache.ambari.server.stack.StackManagerFactory.class);
            java.lang.reflect.Field f = c.getDeclaredField("stackManagerFactory");
            f.setAccessible(true);
            f.set(this, stackManagerFactory);
            org.apache.ambari.server.mpack.MpackManagerFactory mpackManagerFactory = injector.getInstance(org.apache.ambari.server.mpack.MpackManagerFactory.class);
            f = c.getDeclaredField("mpackManagerFactory");
            f.setAccessible(true);
            f.set(this, mpackManagerFactory);
            alertDefinitionDAO = EasyMock.createNiceMock(org.apache.ambari.server.orm.dao.AlertDefinitionDAO.class);
            f = c.getDeclaredField("alertDefinitionDao");
            f.setAccessible(true);
            f.set(this, alertDefinitionDAO);
            alertDefinitionFactory = new org.apache.ambari.server.state.alert.AlertDefinitionFactory();
            f = c.getDeclaredField("alertDefinitionFactory");
            f.setAccessible(true);
            f.set(this, alertDefinitionFactory);
            org.apache.ambari.server.events.publishers.AmbariEventPublisher ambariEventPublisher = new org.apache.ambari.server.events.publishers.AmbariEventPublisher();
            f = c.getDeclaredField("eventPublisher");
            f.setAccessible(true);
            f.set(this, ambariEventPublisher);
            org.apache.ambari.server.state.kerberos.KerberosDescriptorFactory kerberosDescriptorFactory = new org.apache.ambari.server.state.kerberos.KerberosDescriptorFactory();
            f = c.getDeclaredField("kerberosDescriptorFactory");
            f.setAccessible(true);
            f.set(this, kerberosDescriptorFactory);
            org.apache.ambari.server.state.kerberos.KerberosServiceDescriptorFactory kerberosServiceDescriptorFactory = new org.apache.ambari.server.state.kerberos.KerberosServiceDescriptorFactory();
            f = c.getDeclaredField("kerberosServiceDescriptorFactory");
            f.setAccessible(true);
            f.set(this, kerberosServiceDescriptorFactory);
            org.apache.ambari.server.configuration.Configuration config = EasyMock.createNiceMock(org.apache.ambari.server.configuration.Configuration.class);
            if (java.lang.System.getProperty("os.name").contains("Windows")) {
                EasyMock.expect(config.getSharedResourcesDirPath()).andReturn(java.lang.ClassLoader.getSystemClassLoader().getResource("").getPath()).anyTimes();
                EasyMock.expect(config.getResourceDirPath()).andReturn(java.lang.ClassLoader.getSystemClassLoader().getResource("").getPath()).anyTimes();
            } else {
                EasyMock.expect(config.getSharedResourcesDirPath()).andReturn("./src/test/resources").anyTimes();
                EasyMock.expect(config.getResourceDirPath()).andReturn("./src/test/resources").anyTimes();
            }
            EasyMock.replay(config);
            osFamily = new org.apache.ambari.server.state.stack.OsFamily(config);
            f = c.getDeclaredField("osFamily");
            f.setAccessible(true);
            f.set(this, osFamily);
        }

        public void replayAllMocks() {
            EasyMock.replay(alertDefinitionDAO);
        }

        public class MockModule extends com.google.inject.AbstractModule {
            @java.lang.Override
            protected void configure() {
                bind(org.apache.ambari.server.metadata.ActionMetadata.class);
                bind(org.apache.ambari.server.orm.dao.MetainfoDAO.class).toInstance(EasyMock.createNiceMock(org.apache.ambari.server.orm.dao.MetainfoDAO.class));
            }
        }
    }
}