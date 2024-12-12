package org.apache.ambari.server.stack;
import org.apache.commons.lang.StringUtils;
import org.easymock.EasyMock;
import static org.easymock.EasyMock.createNiceMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
public class StackManagerCommonServicesTest {
    private static org.apache.ambari.server.stack.StackManager stackManager;

    private static org.apache.ambari.server.orm.dao.MetainfoDAO metaInfoDao;

    private static org.apache.ambari.server.orm.dao.StackDAO stackDao;

    private static org.apache.ambari.server.orm.dao.ExtensionDAO extensionDao;

    private static org.apache.ambari.server.orm.dao.ExtensionLinkDAO linkDao;

    private static org.apache.ambari.server.metadata.ActionMetadata actionMetadata;

    private static org.apache.ambari.server.state.stack.OsFamily osFamily;

    @org.junit.BeforeClass
    public static void initStack() throws java.lang.Exception {
        org.apache.ambari.server.stack.StackManagerCommonServicesTest.stackManager = org.apache.ambari.server.stack.StackManagerCommonServicesTest.createTestStackManager();
    }

    public static org.apache.ambari.server.stack.StackManager createTestStackManager() throws java.lang.Exception {
        java.lang.String stack = java.lang.ClassLoader.getSystemClassLoader().getResource("stacks_with_common_services").getPath();
        java.lang.String commonServices = java.lang.ClassLoader.getSystemClassLoader().getResource("common-services").getPath();
        java.lang.String extensions = java.lang.ClassLoader.getSystemClassLoader().getResource("extensions").getPath();
        return org.apache.ambari.server.stack.StackManagerCommonServicesTest.createTestStackManager(stack, commonServices, extensions);
    }

    public static org.apache.ambari.server.stack.StackManager createTestStackManager(java.lang.String stackRoot, java.lang.String commonServicesRoot, java.lang.String extensionRoot) throws java.lang.Exception {
        org.apache.ambari.server.stack.StackManagerCommonServicesTest.metaInfoDao = EasyMock.createNiceMock(org.apache.ambari.server.orm.dao.MetainfoDAO.class);
        org.apache.ambari.server.stack.StackManagerCommonServicesTest.stackDao = EasyMock.createNiceMock(org.apache.ambari.server.orm.dao.StackDAO.class);
        org.apache.ambari.server.stack.StackManagerCommonServicesTest.extensionDao = EasyMock.createNiceMock(org.apache.ambari.server.orm.dao.ExtensionDAO.class);
        org.apache.ambari.server.stack.StackManagerCommonServicesTest.linkDao = EasyMock.createNiceMock(org.apache.ambari.server.orm.dao.ExtensionLinkDAO.class);
        org.apache.ambari.server.stack.StackManagerCommonServicesTest.actionMetadata = EasyMock.createNiceMock(org.apache.ambari.server.metadata.ActionMetadata.class);
        org.apache.ambari.server.configuration.Configuration config = EasyMock.createNiceMock(org.apache.ambari.server.configuration.Configuration.class);
        org.apache.ambari.server.orm.entities.StackEntity stackEntity = EasyMock.createNiceMock(org.apache.ambari.server.orm.entities.StackEntity.class);
        org.apache.ambari.server.orm.entities.ExtensionEntity extensionEntity = EasyMock.createNiceMock(org.apache.ambari.server.orm.entities.ExtensionEntity.class);
        EasyMock.expect(config.getSharedResourcesDirPath()).andReturn(java.lang.ClassLoader.getSystemClassLoader().getResource("").getPath()).anyTimes();
        EasyMock.expect(org.apache.ambari.server.stack.StackManagerCommonServicesTest.stackDao.find(org.easymock.EasyMock.anyObject(java.lang.String.class), org.easymock.EasyMock.anyObject(java.lang.String.class))).andReturn(stackEntity).atLeastOnce();
        EasyMock.expect(org.apache.ambari.server.stack.StackManagerCommonServicesTest.extensionDao.find(org.easymock.EasyMock.anyObject(java.lang.String.class), org.easymock.EasyMock.anyObject(java.lang.String.class))).andReturn(extensionEntity).atLeastOnce();
        java.util.List<org.apache.ambari.server.orm.entities.ExtensionLinkEntity> list = java.util.Collections.emptyList();
        EasyMock.expect(org.apache.ambari.server.stack.StackManagerCommonServicesTest.linkDao.findByStack(org.easymock.EasyMock.anyObject(java.lang.String.class), org.easymock.EasyMock.anyObject(java.lang.String.class))).andReturn(list).atLeastOnce();
        EasyMock.replay(config, org.apache.ambari.server.stack.StackManagerCommonServicesTest.stackDao, org.apache.ambari.server.stack.StackManagerCommonServicesTest.extensionDao, org.apache.ambari.server.stack.StackManagerCommonServicesTest.linkDao);
        org.apache.ambari.server.stack.StackManagerCommonServicesTest.osFamily = new org.apache.ambari.server.state.stack.OsFamily(config);
        EasyMock.replay(org.apache.ambari.server.stack.StackManagerCommonServicesTest.metaInfoDao, org.apache.ambari.server.stack.StackManagerCommonServicesTest.actionMetadata);
        org.apache.ambari.server.controller.AmbariManagementHelper helper = new org.apache.ambari.server.controller.AmbariManagementHelper(org.apache.ambari.server.stack.StackManagerCommonServicesTest.stackDao, org.apache.ambari.server.stack.StackManagerCommonServicesTest.extensionDao, org.apache.ambari.server.stack.StackManagerCommonServicesTest.linkDao);
        org.apache.ambari.server.stack.StackManager stackManager = new org.apache.ambari.server.stack.StackManager(new java.io.File(stackRoot), new java.io.File(commonServicesRoot), new java.io.File(extensionRoot), org.apache.ambari.server.stack.StackManagerCommonServicesTest.osFamily, true, org.apache.ambari.server.stack.StackManagerCommonServicesTest.metaInfoDao, org.apache.ambari.server.stack.StackManagerCommonServicesTest.actionMetadata, org.apache.ambari.server.stack.StackManagerCommonServicesTest.stackDao, org.apache.ambari.server.stack.StackManagerCommonServicesTest.extensionDao, org.apache.ambari.server.stack.StackManagerCommonServicesTest.linkDao, helper);
        org.easymock.EasyMock.verify(config, org.apache.ambari.server.stack.StackManagerCommonServicesTest.stackDao);
        return stackManager;
    }

    @org.junit.Test
    public void testGetStacksCount() throws java.lang.Exception {
        java.util.Collection<org.apache.ambari.server.state.StackInfo> stacks = org.apache.ambari.server.stack.StackManagerCommonServicesTest.stackManager.getStacks();
        org.junit.Assert.assertEquals(2, stacks.size());
    }

    @org.junit.Test
    public void testGetStacksByName() {
        java.util.Collection<org.apache.ambari.server.state.StackInfo> stacks = org.apache.ambari.server.stack.StackManagerCommonServicesTest.stackManager.getStacks("HDP");
        org.junit.Assert.assertEquals(2, stacks.size());
    }

    @org.junit.Test
    public void testAddOnServiceRepoIsLoaded() {
        java.util.Collection<org.apache.ambari.server.state.StackInfo> stacks = org.apache.ambari.server.stack.StackManagerCommonServicesTest.stackManager.getStacks("HDP");
        org.apache.ambari.server.state.StackInfo stack = null;
        for (org.apache.ambari.server.state.StackInfo stackInfo : org.apache.ambari.server.stack.StackManagerCommonServicesTest.stackManager.getStacks()) {
            if ("0.2".equals(stackInfo.getVersion())) {
                stack = stackInfo;
                break;
            }
        }
        java.util.List<org.apache.ambari.server.state.RepositoryInfo> repos = stack.getRepositoriesByOs().get("redhat6");
        com.google.common.collect.ImmutableSet<java.lang.String> repoIds = com.google.common.collect.ImmutableSet.copyOf(com.google.common.collect.Lists.transform(repos, org.apache.ambari.server.state.RepositoryInfo.GET_REPO_ID_FUNCTION));
        org.junit.Assert.assertTrue("Repos are expected to contain MSFT_R-8.1", repoIds.contains("ADDON_REPO-1.0"));
    }

    @org.junit.Test
    public void testGetStack() {
        org.apache.ambari.server.state.StackInfo stack = org.apache.ambari.server.stack.StackManagerCommonServicesTest.stackManager.getStack("HDP", "0.1");
        org.junit.Assert.assertNotNull(stack);
        org.junit.Assert.assertEquals("HDP", stack.getName());
        org.junit.Assert.assertEquals("0.1", stack.getVersion());
        java.util.Collection<org.apache.ambari.server.state.ServiceInfo> services = stack.getServices();
        org.junit.Assert.assertEquals(3, services.size());
        java.util.Map<java.lang.String, org.apache.ambari.server.state.ServiceInfo> serviceMap = new java.util.HashMap<>();
        for (org.apache.ambari.server.state.ServiceInfo service : services) {
            serviceMap.put(service.getName(), service);
        }
        org.apache.ambari.server.state.ServiceInfo hdfsService = serviceMap.get("HDFS");
        org.junit.Assert.assertNotNull(hdfsService);
        java.util.List<org.apache.ambari.server.state.ComponentInfo> components = hdfsService.getComponents();
        org.junit.Assert.assertEquals(6, components.size());
        java.util.List<org.apache.ambari.server.state.PropertyInfo> properties = hdfsService.getProperties();
        org.junit.Assert.assertEquals(62, properties.size());
        boolean hdfsPropFound = false;
        boolean hbasePropFound = false;
        for (org.apache.ambari.server.state.PropertyInfo p : properties) {
            if (p.getName().equals("hbase.regionserver.msginterval")) {
                org.junit.Assert.assertEquals("hbase-site.xml", p.getFilename());
                hbasePropFound = true;
            } else if (p.getName().equals("dfs.name.dir")) {
                org.junit.Assert.assertEquals("hdfs-site.xml", p.getFilename());
                hdfsPropFound = true;
            }
        }
        org.junit.Assert.assertTrue(hbasePropFound);
        org.junit.Assert.assertTrue(hdfsPropFound);
        org.apache.ambari.server.state.ServiceInfo mrService = serviceMap.get("MAPREDUCE");
        org.junit.Assert.assertNotNull(mrService);
        components = mrService.getComponents();
        org.junit.Assert.assertEquals(3, components.size());
        org.apache.ambari.server.state.ServiceInfo pigService = serviceMap.get("PIG");
        org.junit.Assert.assertNotNull(pigService);
        org.junit.Assert.assertEquals("PIG", pigService.getName());
        org.junit.Assert.assertEquals("1.0", pigService.getVersion());
        org.junit.Assert.assertEquals("This is comment for PIG service", pigService.getComment());
        components = pigService.getComponents();
        org.junit.Assert.assertEquals(1, components.size());
        org.apache.ambari.server.state.CommandScriptDefinition commandScript = pigService.getCommandScript();
        org.junit.Assert.assertEquals("scripts/service_check.py", commandScript.getScript());
        org.junit.Assert.assertEquals(org.apache.ambari.server.state.CommandScriptDefinition.Type.PYTHON, commandScript.getScriptType());
        org.junit.Assert.assertEquals(300, commandScript.getTimeout());
        java.util.List<java.lang.String> configDependencies = pigService.getConfigDependencies();
        org.junit.Assert.assertEquals(1, configDependencies.size());
        org.junit.Assert.assertEquals("global", configDependencies.get(0));
        org.junit.Assert.assertEquals("global", pigService.getConfigDependenciesWithComponents().get(0));
        org.apache.ambari.server.state.ComponentInfo client = pigService.getClientComponent();
        org.junit.Assert.assertNotNull(client);
        org.junit.Assert.assertEquals("PIG", client.getName());
        org.junit.Assert.assertEquals("0+", client.getCardinality());
        org.junit.Assert.assertEquals("CLIENT", client.getCategory());
        org.junit.Assert.assertEquals("configuration", pigService.getConfigDir());
        org.junit.Assert.assertEquals("2.0", pigService.getSchemaVersion());
        java.util.Map<java.lang.String, org.apache.ambari.server.state.ServiceOsSpecific> osInfoMap = pigService.getOsSpecifics();
        org.junit.Assert.assertEquals(1, osInfoMap.size());
        org.apache.ambari.server.state.ServiceOsSpecific osSpecific = osInfoMap.get("centos6");
        org.junit.Assert.assertNotNull(osSpecific);
        org.junit.Assert.assertEquals("centos6", osSpecific.getOsFamily());
        org.junit.Assert.assertNull(osSpecific.getRepo());
        java.util.List<org.apache.ambari.server.state.ServiceOsSpecific.Package> packages = osSpecific.getPackages();
        org.junit.Assert.assertEquals(2, packages.size());
        org.apache.ambari.server.state.ServiceOsSpecific.Package pkg = packages.get(0);
        org.junit.Assert.assertEquals("pig", pkg.getName());
        org.junit.Assert.assertFalse(pkg.getSkipUpgrade());
        org.apache.ambari.server.state.ServiceOsSpecific.Package lzoPackage = packages.get(1);
        org.junit.Assert.assertEquals("lzo", lzoPackage.getName());
        org.junit.Assert.assertTrue(lzoPackage.getSkipUpgrade());
        org.junit.Assert.assertEquals(pigService.getParent(), "common-services/PIG/1.0");
    }

    @org.junit.Test
    public void testGetServicePackageFolder() {
        org.apache.ambari.server.state.StackInfo stack = org.apache.ambari.server.stack.StackManagerCommonServicesTest.stackManager.getStack("HDP", "0.1");
        org.junit.Assert.assertNotNull(stack);
        org.junit.Assert.assertEquals("HDP", stack.getName());
        org.junit.Assert.assertEquals("0.1", stack.getVersion());
        org.apache.ambari.server.state.ServiceInfo hdfsService1 = stack.getService("HDFS");
        org.junit.Assert.assertNotNull(hdfsService1);
        stack = org.apache.ambari.server.stack.StackManagerCommonServicesTest.stackManager.getStack("HDP", "0.2");
        org.junit.Assert.assertNotNull(stack);
        org.junit.Assert.assertEquals("HDP", stack.getName());
        org.junit.Assert.assertEquals("0.2", stack.getVersion());
        org.apache.ambari.server.state.ServiceInfo hdfsService2 = stack.getService("HDFS");
        org.junit.Assert.assertNotNull(hdfsService2);
        java.lang.String packageDir1 = org.apache.commons.lang.StringUtils.join(new java.lang.String[]{ "common-services", "HDFS", "1.0", "package" }, java.io.File.separator);
        java.lang.String packageDir2 = org.apache.commons.lang.StringUtils.join(new java.lang.String[]{ "stacks_with_common_services", "HDP", "0.2", "services", "HDFS", "package" }, java.io.File.separator);
        org.junit.Assert.assertEquals(packageDir1, hdfsService1.getServicePackageFolder());
        org.junit.Assert.assertEquals(packageDir2, hdfsService2.getServicePackageFolder());
    }
}