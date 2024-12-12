package org.apache.ambari.server.stack;
import org.apache.commons.lang.StringUtils;
import org.easymock.EasyMock;
import static org.easymock.EasyMock.createNiceMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
public class StackManagerTest {
    private static org.apache.ambari.server.stack.StackManager stackManager;

    private static org.apache.ambari.server.orm.dao.MetainfoDAO metaInfoDao;

    private static org.apache.ambari.server.metadata.ActionMetadata actionMetadata;

    private static org.apache.ambari.server.state.stack.OsFamily osFamily;

    private static org.apache.ambari.server.orm.dao.StackDAO stackDao;

    private static org.apache.ambari.server.orm.dao.ExtensionDAO extensionDao;

    private static org.apache.ambari.server.orm.dao.ExtensionLinkDAO linkDao;

    @org.junit.BeforeClass
    public static void initStack() throws java.lang.Exception {
        org.apache.ambari.server.stack.StackManagerTest.stackManager = org.apache.ambari.server.stack.StackManagerTest.createTestStackManager();
    }

    public static org.apache.ambari.server.stack.StackManager createTestStackManager() throws java.lang.Exception {
        java.lang.String stack = java.lang.ClassLoader.getSystemClassLoader().getResource("stacks").getPath();
        return org.apache.ambari.server.stack.StackManagerTest.createTestStackManager(stack);
    }

    public static org.apache.ambari.server.stack.StackManager createTestStackManager(java.lang.String stackRoot) throws java.lang.Exception {
        org.apache.ambari.server.stack.StackManagerTest.metaInfoDao = EasyMock.createNiceMock(org.apache.ambari.server.orm.dao.MetainfoDAO.class);
        org.apache.ambari.server.stack.StackManagerTest.stackDao = EasyMock.createNiceMock(org.apache.ambari.server.orm.dao.StackDAO.class);
        org.apache.ambari.server.stack.StackManagerTest.extensionDao = EasyMock.createNiceMock(org.apache.ambari.server.orm.dao.ExtensionDAO.class);
        org.apache.ambari.server.stack.StackManagerTest.linkDao = EasyMock.createNiceMock(org.apache.ambari.server.orm.dao.ExtensionLinkDAO.class);
        org.apache.ambari.server.stack.StackManagerTest.actionMetadata = EasyMock.createNiceMock(org.apache.ambari.server.metadata.ActionMetadata.class);
        org.apache.ambari.server.configuration.Configuration config = EasyMock.createNiceMock(org.apache.ambari.server.configuration.Configuration.class);
        org.apache.ambari.server.orm.entities.ExtensionEntity extensionEntity = EasyMock.createNiceMock(org.apache.ambari.server.orm.entities.ExtensionEntity.class);
        EasyMock.expect(config.getSharedResourcesDirPath()).andReturn(java.lang.ClassLoader.getSystemClassLoader().getResource("").getPath()).anyTimes();
        EasyMock.expect(org.apache.ambari.server.stack.StackManagerTest.extensionDao.find(org.easymock.EasyMock.anyObject(java.lang.String.class), org.easymock.EasyMock.anyObject(java.lang.String.class))).andReturn(extensionEntity).atLeastOnce();
        java.util.List<org.apache.ambari.server.orm.entities.ExtensionLinkEntity> list = java.util.Collections.emptyList();
        EasyMock.expect(org.apache.ambari.server.stack.StackManagerTest.linkDao.findByStack(org.easymock.EasyMock.anyObject(java.lang.String.class), org.easymock.EasyMock.anyObject(java.lang.String.class))).andReturn(list).atLeastOnce();
        EasyMock.replay(config, org.apache.ambari.server.stack.StackManagerTest.metaInfoDao, org.apache.ambari.server.stack.StackManagerTest.stackDao, org.apache.ambari.server.stack.StackManagerTest.extensionDao, org.apache.ambari.server.stack.StackManagerTest.linkDao, org.apache.ambari.server.stack.StackManagerTest.actionMetadata);
        org.apache.ambari.server.stack.StackManagerTest.osFamily = new org.apache.ambari.server.state.stack.OsFamily(config);
        org.apache.ambari.server.controller.AmbariManagementHelper helper = new org.apache.ambari.server.controller.AmbariManagementHelper(org.apache.ambari.server.stack.StackManagerTest.stackDao, org.apache.ambari.server.stack.StackManagerTest.extensionDao, org.apache.ambari.server.stack.StackManagerTest.linkDao);
        org.apache.ambari.server.stack.StackManager stackManager = new org.apache.ambari.server.stack.StackManager(new java.io.File(stackRoot), null, null, org.apache.ambari.server.stack.StackManagerTest.osFamily, false, org.apache.ambari.server.stack.StackManagerTest.metaInfoDao, org.apache.ambari.server.stack.StackManagerTest.actionMetadata, org.apache.ambari.server.stack.StackManagerTest.stackDao, org.apache.ambari.server.stack.StackManagerTest.extensionDao, org.apache.ambari.server.stack.StackManagerTest.linkDao, helper);
        EasyMock.verify(config, org.apache.ambari.server.stack.StackManagerTest.metaInfoDao, org.apache.ambari.server.stack.StackManagerTest.stackDao, org.apache.ambari.server.stack.StackManagerTest.actionMetadata);
        return stackManager;
    }

    @org.junit.Test
    public void testGetsStacks() throws java.lang.Exception {
        java.util.Collection<org.apache.ambari.server.state.StackInfo> stacks = org.apache.ambari.server.stack.StackManagerTest.stackManager.getStacks();
        org.junit.Assert.assertEquals(21, stacks.size());
    }

    @org.junit.Test
    public void testGetStacksByName() {
        java.util.Collection<org.apache.ambari.server.state.StackInfo> stacks = org.apache.ambari.server.stack.StackManagerTest.stackManager.getStacks("HDP");
        org.junit.Assert.assertEquals(17, stacks.size());
        stacks = org.apache.ambari.server.stack.StackManagerTest.stackManager.getStacks("OTHER");
        org.junit.Assert.assertEquals(2, stacks.size());
    }

    @org.junit.Test
    public void testHCFSServiceType() {
        org.apache.ambari.server.state.StackInfo stack = org.apache.ambari.server.stack.StackManagerTest.stackManager.getStack("HDP", "2.2.0.ECS");
        org.apache.ambari.server.state.ServiceInfo service = stack.getService("ECS");
        org.junit.Assert.assertEquals(service.getServiceType(), "HCFS");
        service = stack.getService("HDFS");
        org.junit.Assert.assertNull(service);
    }

    @org.junit.Test
    public void testServiceRemoved() {
        org.apache.ambari.server.state.StackInfo stack = org.apache.ambari.server.stack.StackManagerTest.stackManager.getStack("HDP", "2.0.8");
        org.apache.ambari.server.state.ServiceInfo service = stack.getService("SPARK");
        org.junit.Assert.assertNull(service);
        service = stack.getService("SPARK2");
        org.junit.Assert.assertNull(service);
        java.util.List<java.lang.String> removedServices = stack.getRemovedServices();
        org.junit.Assert.assertEquals(removedServices.size(), 2);
        java.util.HashSet<java.lang.String> expectedServices = new java.util.HashSet<>();
        expectedServices.add("SPARK");
        expectedServices.add("SPARK2");
        for (java.lang.String s : removedServices) {
            org.junit.Assert.assertTrue(expectedServices.remove(s));
        }
        org.junit.Assert.assertTrue(expectedServices.isEmpty());
    }

    @org.junit.Test
    public void testSerivcesWithNoConfigs() {
        org.apache.ambari.server.state.StackInfo stack = org.apache.ambari.server.stack.StackManagerTest.stackManager.getStack("HDP", "2.0.8");
        java.util.List<java.lang.String> servicesWithNoConfigs = stack.getServicesWithNoConfigs();
        org.junit.Assert.assertTrue(servicesWithNoConfigs.contains("SYSTEMML"));
        org.junit.Assert.assertFalse(servicesWithNoConfigs.contains("HIVE"));
        stack = org.apache.ambari.server.stack.StackManagerTest.stackManager.getStack("HDP", "2.0.7");
        servicesWithNoConfigs = stack.getServicesWithNoConfigs();
        org.junit.Assert.assertTrue(servicesWithNoConfigs.contains("SYSTEMML"));
    }

    @org.junit.Test
    public void testGetStack() {
        org.apache.ambari.server.state.StackInfo stack = org.apache.ambari.server.stack.StackManagerTest.stackManager.getStack("HDP", "0.1");
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
        org.junit.Assert.assertEquals(64, properties.size());
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
        org.junit.Assert.assertEquals(1, packages.size());
        org.apache.ambari.server.state.ServiceOsSpecific.Package pkg = packages.get(0);
        org.junit.Assert.assertEquals("pig", pkg.getName());
        org.junit.Assert.assertNull(pigService.getParent());
    }

    @org.junit.Test
    public void testStackVersionInheritance() {
        org.apache.ambari.server.state.StackInfo stack = org.apache.ambari.server.stack.StackManagerTest.stackManager.getStack("HDP", "2.1.1");
        org.junit.Assert.assertNotNull(stack);
        org.junit.Assert.assertEquals("HDP", stack.getName());
        org.junit.Assert.assertEquals("2.1.1", stack.getVersion());
        java.util.Collection<org.apache.ambari.server.state.ServiceInfo> services = stack.getServices();
        org.apache.ambari.server.state.ServiceInfo si = stack.getService("SPARK");
        org.junit.Assert.assertNull(si);
        si = stack.getService("SPARK2");
        org.junit.Assert.assertNull(si);
        si = stack.getService("SPARK3");
        org.junit.Assert.assertNotNull(si);
        org.junit.Assert.assertEquals(18, services.size());
        java.util.HashSet<java.lang.String> expectedServices = new java.util.HashSet<>();
        expectedServices.add("GANGLIA");
        expectedServices.add("HBASE");
        expectedServices.add("HCATALOG");
        expectedServices.add("HDFS");
        expectedServices.add("HIVE");
        expectedServices.add("MAPREDUCE2");
        expectedServices.add("OOZIE");
        expectedServices.add("PIG");
        expectedServices.add("SQOOP");
        expectedServices.add("YARN");
        expectedServices.add("ZOOKEEPER");
        expectedServices.add("STORM");
        expectedServices.add("FLUME");
        expectedServices.add("FAKENAGIOS");
        expectedServices.add("TEZ");
        expectedServices.add("AMBARI_METRICS");
        expectedServices.add("SPARK3");
        expectedServices.add("SYSTEMML");
        org.apache.ambari.server.state.ServiceInfo pigService = null;
        for (org.apache.ambari.server.state.ServiceInfo service : services) {
            if (service.getName().equals("PIG")) {
                pigService = service;
            }
            org.junit.Assert.assertTrue(expectedServices.remove(service.getName()));
        }
        org.junit.Assert.assertTrue(expectedServices.isEmpty());
        org.junit.Assert.assertNotNull(pigService);
        org.junit.Assert.assertEquals("0.12.1.2.1.1", pigService.getVersion());
        org.junit.Assert.assertEquals("Scripting platform for analyzing large datasets (Extended)", pigService.getComment());
        org.apache.ambari.server.state.ServiceInfo basePigService = org.apache.ambari.server.stack.StackManagerTest.stackManager.getStack("HDP", "2.0.5").getService("PIG");
        org.junit.Assert.assertEquals("0.11.1.2.0.5.0", basePigService.getVersion());
        org.junit.Assert.assertEquals(1, basePigService.getComponents().size());
        org.junit.Assert.assertEquals(2, pigService.getComponents().size());
        org.junit.Assert.assertEquals(0, basePigService.getProperties().size());
        org.junit.Assert.assertEquals(1, pigService.getProperties().size());
        org.junit.Assert.assertEquals("content", pigService.getProperties().get(0).getName());
    }

    @org.junit.Test
    public void testStackServiceExtension() {
        org.apache.ambari.server.state.StackInfo stack = org.apache.ambari.server.stack.StackManagerTest.stackManager.getStack("OTHER", "1.0");
        org.junit.Assert.assertNotNull(stack);
        org.junit.Assert.assertEquals("OTHER", stack.getName());
        org.junit.Assert.assertEquals("1.0", stack.getVersion());
        java.util.Collection<org.apache.ambari.server.state.ServiceInfo> services = stack.getServices();
        org.junit.Assert.assertEquals(4, services.size());
        org.junit.Assert.assertEquals(6, stack.getService("HDFS").getComponents().size());
        org.apache.ambari.server.state.ServiceInfo sqoopService = stack.getService("SQOOP2");
        org.junit.Assert.assertNotNull(sqoopService);
        org.junit.Assert.assertEquals("Extended SQOOP", sqoopService.getComment());
        org.junit.Assert.assertEquals("Extended Version", sqoopService.getVersion());
        org.junit.Assert.assertNull(sqoopService.getServicePackageFolder());
        java.util.Collection<org.apache.ambari.server.state.ComponentInfo> components = sqoopService.getComponents();
        org.junit.Assert.assertEquals(1, components.size());
        org.apache.ambari.server.state.ComponentInfo component = components.iterator().next();
        org.junit.Assert.assertEquals("SQOOP", component.getName());
        org.apache.ambari.server.state.StackInfo baseStack = org.apache.ambari.server.stack.StackManagerTest.stackManager.getStack("HDP", "2.1.1");
        org.apache.ambari.server.state.ServiceInfo baseSqoopService = baseStack.getService("SQOOP");
        org.junit.Assert.assertEquals(baseSqoopService.isDeleted(), sqoopService.isDeleted());
        org.junit.Assert.assertEquals(baseSqoopService.getAlertsFile(), sqoopService.getAlertsFile());
        org.junit.Assert.assertEquals(baseSqoopService.getClientComponent(), sqoopService.getClientComponent());
        org.junit.Assert.assertEquals(baseSqoopService.getCommandScript(), sqoopService.getCommandScript());
        org.junit.Assert.assertEquals(baseSqoopService.getConfigDependencies(), sqoopService.getConfigDependencies());
        org.junit.Assert.assertEquals(baseSqoopService.getConfigDir(), sqoopService.getConfigDir());
        org.junit.Assert.assertEquals(baseSqoopService.getConfigDependenciesWithComponents(), sqoopService.getConfigDependenciesWithComponents());
        org.junit.Assert.assertEquals(baseSqoopService.getConfigTypeAttributes(), sqoopService.getConfigTypeAttributes());
        org.junit.Assert.assertEquals(baseSqoopService.getCustomCommands(), sqoopService.getCustomCommands());
        org.junit.Assert.assertEquals(baseSqoopService.getExcludedConfigTypes(), sqoopService.getExcludedConfigTypes());
        org.junit.Assert.assertEquals(baseSqoopService.getProperties(), sqoopService.getProperties());
        org.junit.Assert.assertEquals(baseSqoopService.getMetrics(), sqoopService.getMetrics());
        org.junit.Assert.assertNull(baseSqoopService.getMetricsFile());
        org.junit.Assert.assertNull(sqoopService.getMetricsFile());
        org.junit.Assert.assertEquals(baseSqoopService.getOsSpecifics(), sqoopService.getOsSpecifics());
        org.junit.Assert.assertEquals(baseSqoopService.getRequiredServices(), sqoopService.getRequiredServices());
        org.junit.Assert.assertEquals(baseSqoopService.getSchemaVersion(), sqoopService.getSchemaVersion());
        org.apache.ambari.server.state.ServiceInfo stormService = stack.getService("STORM");
        org.junit.Assert.assertNotNull(stormService);
        org.junit.Assert.assertEquals("STORM", stormService.getName());
        org.apache.ambari.server.state.ServiceInfo baseStormService = baseStack.getService("STORM");
        org.junit.Assert.assertEquals("Apache Hadoop Stream processing framework (Extended)", stormService.getComment());
        org.junit.Assert.assertEquals("New version", stormService.getVersion());
        java.lang.String packageDir = org.apache.commons.lang.StringUtils.join(new java.lang.String[]{ "stacks", "OTHER", "1.0", "services", "STORM", "package" }, java.io.File.separator);
        org.junit.Assert.assertEquals(packageDir, stormService.getServicePackageFolder());
        java.util.List<org.apache.ambari.server.state.ComponentInfo> stormServiceComponents = stormService.getComponents();
        java.util.List<org.apache.ambari.server.state.ComponentInfo> baseStormServiceComponents = baseStormService.getComponents();
        org.junit.Assert.assertEquals(new java.util.HashSet<>(stormServiceComponents), new java.util.HashSet<>(baseStormServiceComponents));
        org.junit.Assert.assertEquals(baseStormService.isDeleted(), stormService.isDeleted());
        org.junit.Assert.assertEquals(baseStormService.getAlertsFile(), stormService.getAlertsFile());
        org.junit.Assert.assertEquals(baseStormService.getClientComponent(), stormService.getClientComponent());
        org.junit.Assert.assertEquals(baseStormService.getCommandScript(), stormService.getCommandScript());
        org.junit.Assert.assertEquals(baseStormService.getConfigDependencies(), stormService.getConfigDependencies());
        org.junit.Assert.assertEquals(baseStormService.getConfigDir(), stormService.getConfigDir());
        org.junit.Assert.assertEquals(baseStormService.getConfigDependenciesWithComponents(), stormService.getConfigDependenciesWithComponents());
        org.junit.Assert.assertEquals(baseStormService.getConfigTypeAttributes(), stormService.getConfigTypeAttributes());
        org.junit.Assert.assertEquals(baseStormService.getCustomCommands(), stormService.getCustomCommands());
        org.junit.Assert.assertEquals(baseStormService.getExcludedConfigTypes(), stormService.getExcludedConfigTypes());
        org.junit.Assert.assertEquals(baseStormService.getProperties(), stormService.getProperties());
        org.junit.Assert.assertEquals(baseStormService.getMetrics(), stormService.getMetrics());
        org.junit.Assert.assertNotNull(baseStormService.getMetricsFile());
        org.junit.Assert.assertNotNull(stormService.getMetricsFile());
        org.junit.Assert.assertFalse(baseStormService.getMetricsFile().equals(stormService.getMetricsFile()));
        org.junit.Assert.assertEquals(baseStormService.getOsSpecifics(), stormService.getOsSpecifics());
        org.junit.Assert.assertEquals(baseStormService.getRequiredServices(), stormService.getRequiredServices());
        org.junit.Assert.assertEquals(baseStormService.getSchemaVersion(), stormService.getSchemaVersion());
    }

    @org.junit.Test
    public void testGetStackServiceInheritance() {
        org.apache.ambari.server.state.StackInfo baseStack = org.apache.ambari.server.stack.StackManagerTest.stackManager.getStack("OTHER", "1.0");
        org.apache.ambari.server.state.StackInfo stack = org.apache.ambari.server.stack.StackManagerTest.stackManager.getStack("OTHER", "2.0");
        org.junit.Assert.assertEquals(5, stack.getServices().size());
        org.apache.ambari.server.state.ServiceInfo service = stack.getService("SQOOP2");
        org.apache.ambari.server.state.ServiceInfo baseSqoopService = baseStack.getService("SQOOP2");
        org.junit.Assert.assertEquals("SQOOP2", service.getName());
        org.junit.Assert.assertEquals("Inherited from parent", service.getComment());
        org.junit.Assert.assertEquals("Extended from parent version", service.getVersion());
        org.junit.Assert.assertNull(service.getServicePackageFolder());
        java.util.List<org.apache.ambari.server.state.ComponentInfo> serviceComponents = service.getComponents();
        java.util.List<org.apache.ambari.server.state.ComponentInfo> baseStormServiceCompoents = baseSqoopService.getComponents();
        org.junit.Assert.assertEquals(serviceComponents, baseStormServiceCompoents);
        org.junit.Assert.assertEquals(baseSqoopService.isDeleted(), service.isDeleted());
        org.junit.Assert.assertEquals(baseSqoopService.getAlertsFile(), service.getAlertsFile());
        org.junit.Assert.assertEquals(baseSqoopService.getClientComponent(), service.getClientComponent());
        org.junit.Assert.assertEquals(baseSqoopService.getCommandScript(), service.getCommandScript());
        org.junit.Assert.assertEquals(baseSqoopService.getConfigDependencies(), service.getConfigDependencies());
        org.junit.Assert.assertEquals(baseSqoopService.getConfigDir(), service.getConfigDir());
        org.junit.Assert.assertEquals(baseSqoopService.getConfigDependenciesWithComponents(), service.getConfigDependenciesWithComponents());
        org.junit.Assert.assertEquals(baseSqoopService.getConfigTypeAttributes(), service.getConfigTypeAttributes());
        org.junit.Assert.assertEquals(baseSqoopService.getCustomCommands(), service.getCustomCommands());
        org.junit.Assert.assertEquals(baseSqoopService.getExcludedConfigTypes(), service.getExcludedConfigTypes());
        org.junit.Assert.assertEquals(baseSqoopService.getProperties(), service.getProperties());
        org.junit.Assert.assertEquals(baseSqoopService.getMetrics(), service.getMetrics());
        org.junit.Assert.assertNull(baseSqoopService.getMetricsFile());
        org.junit.Assert.assertNull(service.getMetricsFile());
        org.junit.Assert.assertEquals(baseSqoopService.getOsSpecifics(), service.getOsSpecifics());
        org.junit.Assert.assertEquals(baseSqoopService.getRequiredServices(), service.getRequiredServices());
        org.junit.Assert.assertEquals(baseSqoopService.getSchemaVersion(), service.getSchemaVersion());
    }

    @org.junit.Test
    public void testConfigDependenciesInheritance() throws java.lang.Exception {
        org.apache.ambari.server.state.StackInfo stack = org.apache.ambari.server.stack.StackManagerTest.stackManager.getStack("HDP", "2.0.6");
        org.apache.ambari.server.state.ServiceInfo hdfsService = stack.getService("HDFS");
        org.junit.Assert.assertEquals(5, hdfsService.getConfigDependencies().size());
        org.junit.Assert.assertEquals(4, hdfsService.getConfigTypeAttributes().size());
        org.junit.Assert.assertTrue(hdfsService.getConfigDependencies().contains("core-site"));
        org.junit.Assert.assertTrue(hdfsService.getConfigDependencies().contains("global"));
        org.junit.Assert.assertTrue(hdfsService.getConfigDependencies().contains("hdfs-site"));
        org.junit.Assert.assertTrue(hdfsService.getConfigDependencies().contains("hdfs-log4j"));
        org.junit.Assert.assertTrue(hdfsService.getConfigDependencies().contains("hadoop-policy"));
        org.junit.Assert.assertTrue(java.lang.Boolean.valueOf(hdfsService.getConfigTypeAttributes().get("core-site").get("supports").get("final")));
        org.junit.Assert.assertFalse(java.lang.Boolean.valueOf(hdfsService.getConfigTypeAttributes().get("global").get("supports").get("final")));
    }

    @org.junit.Test
    public void testClientConfigFilesInheritance() throws java.lang.Exception {
        org.apache.ambari.server.state.StackInfo stack = org.apache.ambari.server.stack.StackManagerTest.stackManager.getStack("HDP", "2.0.6");
        org.apache.ambari.server.state.ServiceInfo zkService = stack.getService("ZOOKEEPER");
        java.util.List<org.apache.ambari.server.state.ComponentInfo> components = zkService.getComponents();
        org.junit.Assert.assertTrue(components.size() == 2);
        org.apache.ambari.server.state.ComponentInfo componentInfo = components.get(1);
        java.util.List<org.apache.ambari.server.state.ClientConfigFileDefinition> clientConfigs = componentInfo.getClientConfigFiles();
        org.junit.Assert.assertEquals(2, clientConfigs.size());
        org.junit.Assert.assertEquals("zookeeper-env", clientConfigs.get(0).getDictionaryName());
        org.junit.Assert.assertEquals("zookeeper-env.sh", clientConfigs.get(0).getFileName());
        org.junit.Assert.assertEquals("env", clientConfigs.get(0).getType());
        org.junit.Assert.assertEquals("zookeeper-log4j", clientConfigs.get(1).getDictionaryName());
        org.junit.Assert.assertEquals("log4j.properties", clientConfigs.get(1).getFileName());
        org.junit.Assert.assertEquals("env", clientConfigs.get(1).getType());
    }

    @org.junit.Test
    public void testPackageInheritance() throws java.lang.Exception {
        org.apache.ambari.server.state.StackInfo stack = org.apache.ambari.server.stack.StackManagerTest.stackManager.getStack("HDP", "2.0.7");
        org.junit.Assert.assertNotNull(stack.getService("HBASE"));
        org.apache.ambari.server.state.ServiceInfo hbase = stack.getService("HBASE");
        org.junit.Assert.assertNotNull("Package dir is " + hbase.getServicePackageFolder(), hbase.getServicePackageFolder());
        stack = org.apache.ambari.server.stack.StackManagerTest.stackManager.getStack("HDP", "2.0.8");
        org.junit.Assert.assertNotNull(stack.getService("HBASE"));
        hbase = stack.getService("HBASE");
        org.junit.Assert.assertNotNull("Package dir is " + hbase.getServicePackageFolder(), hbase.getServicePackageFolder());
    }

    @org.junit.Test
    public void testMonitoringServicePropertyInheritance() throws java.lang.Exception {
        org.apache.ambari.server.state.StackInfo stack = org.apache.ambari.server.stack.StackManagerTest.stackManager.getStack("HDP", "2.0.8");
        java.util.Collection<org.apache.ambari.server.state.ServiceInfo> allServices = stack.getServices();
        org.junit.Assert.assertEquals(15, allServices.size());
        boolean monitoringServiceFound = false;
        for (org.apache.ambari.server.state.ServiceInfo serviceInfo : allServices) {
            if (serviceInfo.getName().equals("FAKENAGIOS")) {
                monitoringServiceFound = true;
                org.junit.Assert.assertTrue(serviceInfo.isMonitoringService());
            } else {
                org.junit.Assert.assertNull(serviceInfo.isMonitoringService());
            }
        }
        org.junit.Assert.assertTrue(monitoringServiceFound);
    }

    @org.junit.Test
    public void testServiceDeletion() {
        org.apache.ambari.server.state.StackInfo stack = org.apache.ambari.server.stack.StackManagerTest.stackManager.getStack("HDP", "2.0.6");
        java.util.Collection<org.apache.ambari.server.state.ServiceInfo> allServices = stack.getServices();
        org.junit.Assert.assertEquals(12, allServices.size());
        java.util.HashSet<java.lang.String> expectedServices = new java.util.HashSet<>();
        expectedServices.add("GANGLIA");
        expectedServices.add("HBASE");
        expectedServices.add("HCATALOG");
        expectedServices.add("HDFS");
        expectedServices.add("HIVE");
        expectedServices.add("MAPREDUCE2");
        expectedServices.add("OOZIE");
        expectedServices.add("PIG");
        expectedServices.add("SPARK");
        expectedServices.add("ZOOKEEPER");
        expectedServices.add("FLUME");
        expectedServices.add("YARN");
        for (org.apache.ambari.server.state.ServiceInfo service : allServices) {
            org.junit.Assert.assertTrue(expectedServices.remove(service.getName()));
        }
        org.junit.Assert.assertTrue(expectedServices.isEmpty());
    }

    @org.junit.Test
    public void testComponentDeletion() {
        org.apache.ambari.server.state.StackInfo stack = org.apache.ambari.server.stack.StackManagerTest.stackManager.getStack("HDP", "2.0.6");
        org.apache.ambari.server.state.ServiceInfo yarnService = stack.getService("YARN");
        org.junit.Assert.assertNull(yarnService.getComponentByName("YARN_CLIENT"));
        stack = org.apache.ambari.server.stack.StackManagerTest.stackManager.getStack("HDP", "2.0.6.1");
        yarnService = stack.getService("YARN");
        org.junit.Assert.assertNull(yarnService.getComponentByName("YARN_CLIENT"));
        stack = org.apache.ambari.server.stack.StackManagerTest.stackManager.getStack("HDP", "2.0.7");
        yarnService = stack.getService("YARN");
        org.junit.Assert.assertNotNull(yarnService.getComponentByName("YARN_CLIENT"));
    }

    @org.junit.Test
    public void testInheritanceAfterComponentDeletion() {
        org.apache.ambari.server.state.StackInfo stack = org.apache.ambari.server.stack.StackManagerTest.stackManager.getStack("HDP", "2.0.6");
        org.apache.ambari.server.state.ServiceInfo yarnService = stack.getService("HBASE");
        org.junit.Assert.assertNull(yarnService.getComponentByName("HBASE_CLIENT"));
        stack = org.apache.ambari.server.stack.StackManagerTest.stackManager.getStack("HDP", "2.0.6.1");
        yarnService = stack.getService("HBASE");
        org.junit.Assert.assertNull(yarnService.getComponentByName("HBASE_CLIENT"));
        stack = org.apache.ambari.server.stack.StackManagerTest.stackManager.getStack("HDP", "2.0.8");
        yarnService = stack.getService("HBASE");
        org.junit.Assert.assertNotNull(yarnService.getComponentByName("HBASE_CLIENT"));
    }

    @org.junit.Test
    public void testPopulateConfigTypes() throws java.lang.Exception {
        org.apache.ambari.server.state.StackInfo stack = org.apache.ambari.server.stack.StackManagerTest.stackManager.getStack("HDP", "2.0.7");
        org.apache.ambari.server.state.ServiceInfo hdfsService = stack.getService("HDFS");
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>>> configTypes = hdfsService.getConfigTypeAttributes();
        org.junit.Assert.assertEquals(4, configTypes.size());
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> configType = configTypes.get("global");
        org.junit.Assert.assertEquals(1, configType.size());
        java.util.Map<java.lang.String, java.lang.String> supportsMap = configType.get("supports");
        org.junit.Assert.assertEquals(3, supportsMap.size());
        org.junit.Assert.assertEquals("true", supportsMap.get("final"));
        org.junit.Assert.assertEquals("false", supportsMap.get("adding_forbidden"));
        org.junit.Assert.assertEquals("false", supportsMap.get("do_not_extend"));
        configType = configTypes.get("hdfs-site");
        org.junit.Assert.assertEquals(1, configType.size());
        supportsMap = configType.get("supports");
        org.junit.Assert.assertEquals(3, supportsMap.size());
        org.junit.Assert.assertEquals("false", supportsMap.get("final"));
        org.junit.Assert.assertEquals("false", supportsMap.get("adding_forbidden"));
        org.junit.Assert.assertEquals("false", supportsMap.get("do_not_extend"));
        configType = configTypes.get("core-site");
        org.junit.Assert.assertEquals(1, configType.size());
        supportsMap = configType.get("supports");
        org.junit.Assert.assertEquals(3, supportsMap.size());
        org.junit.Assert.assertEquals("false", supportsMap.get("final"));
        org.junit.Assert.assertEquals("false", supportsMap.get("adding_forbidden"));
        org.junit.Assert.assertEquals("false", supportsMap.get("do_not_extend"));
        configType = configTypes.get("hadoop-policy");
        org.junit.Assert.assertEquals(1, configType.size());
        supportsMap = configType.get("supports");
        org.junit.Assert.assertEquals(3, supportsMap.size());
        org.junit.Assert.assertEquals("false", supportsMap.get("final"));
        org.junit.Assert.assertEquals("false", supportsMap.get("adding_forbidden"));
        org.junit.Assert.assertEquals("false", supportsMap.get("do_not_extend"));
        org.apache.ambari.server.state.ServiceInfo yarnService = stack.getService("YARN");
        configTypes = yarnService.getConfigTypeAttributes();
        org.junit.Assert.assertEquals(4, configTypes.size());
        org.junit.Assert.assertTrue(configTypes.containsKey("yarn-site"));
        org.junit.Assert.assertTrue(configTypes.containsKey("core-site"));
        org.junit.Assert.assertTrue(configTypes.containsKey("global"));
        org.junit.Assert.assertTrue(configTypes.containsKey("capacity-scheduler"));
        configType = configTypes.get("yarn-site");
        supportsMap = configType.get("supports");
        org.junit.Assert.assertEquals(3, supportsMap.size());
        org.junit.Assert.assertEquals("false", supportsMap.get("final"));
        org.junit.Assert.assertEquals("true", supportsMap.get("adding_forbidden"));
        org.junit.Assert.assertEquals("true", supportsMap.get("do_not_extend"));
        org.apache.ambari.server.state.ServiceInfo mrService = stack.getService("MAPREDUCE2");
        configTypes = mrService.getConfigTypeAttributes();
        org.junit.Assert.assertEquals(3, configTypes.size());
        org.junit.Assert.assertTrue(configTypes.containsKey("mapred-site"));
        org.junit.Assert.assertTrue(configTypes.containsKey("core-site"));
        org.junit.Assert.assertTrue(configTypes.containsKey("mapred-queue-acls"));
    }

    @org.junit.Test
    public void testExcludedConfigTypes() {
        org.apache.ambari.server.state.StackInfo stack = org.apache.ambari.server.stack.StackManagerTest.stackManager.getStack("HDP", "2.0.8");
        org.apache.ambari.server.state.ServiceInfo service = stack.getService("HBASE");
        org.junit.Assert.assertFalse(service.hasConfigType("global"));
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>>> configTypes = service.getConfigTypeAttributes();
        org.junit.Assert.assertEquals(2, configTypes.size());
        org.junit.Assert.assertTrue(configTypes.containsKey("hbase-site"));
        org.junit.Assert.assertTrue(configTypes.containsKey("hbase-policy"));
        stack = org.apache.ambari.server.stack.StackManagerTest.stackManager.getStack("HDP", "2.1.1");
        service = stack.getService("HBASE");
        org.junit.Assert.assertFalse(service.hasConfigType("global"));
        configTypes = service.getConfigTypeAttributes();
        org.junit.Assert.assertEquals(2, configTypes.size());
        org.junit.Assert.assertTrue(configTypes.containsKey("hbase-site"));
        org.junit.Assert.assertTrue(configTypes.containsKey("hbase-policy"));
        org.junit.Assert.assertFalse(configTypes.containsKey("global"));
        stack = org.apache.ambari.server.stack.StackManagerTest.stackManager.getStack("OTHER", "2.0");
        service = stack.getService("HBASE");
        org.junit.Assert.assertFalse(service.hasConfigType("hbase-policy"));
        org.junit.Assert.assertFalse(service.hasConfigType("global"));
        configTypes = service.getConfigTypeAttributes();
        org.junit.Assert.assertEquals(1, configTypes.size());
        org.junit.Assert.assertTrue(configTypes.containsKey("hbase-site"));
    }

    @org.junit.Test
    public void testHDFSServiceContainsMetricsFile() throws java.lang.Exception {
        org.apache.ambari.server.state.StackInfo stack = org.apache.ambari.server.stack.StackManagerTest.stackManager.getStack("HDP", "2.0.6");
        org.apache.ambari.server.state.ServiceInfo hdfsService = stack.getService("HDFS");
        org.junit.Assert.assertEquals("HDFS", hdfsService.getName());
        org.junit.Assert.assertNotNull(hdfsService.getMetricsFile());
    }

    @org.junit.Test
    public void testMergeRoleCommandOrder() throws java.lang.Exception {
        org.apache.ambari.server.state.StackInfo stack = org.apache.ambari.server.stack.StackManagerTest.stackManager.getStack("HDP", "2.1.1");
        java.util.Map<java.lang.String, java.lang.Object> roleCommandOrder = stack.getRoleCommandOrder().getContent();
        org.junit.Assert.assertTrue(roleCommandOrder.containsKey("optional_glusterfs"));
        org.junit.Assert.assertTrue(roleCommandOrder.containsKey("general_deps"));
        org.junit.Assert.assertTrue(roleCommandOrder.containsKey("optional_no_glusterfs"));
        org.junit.Assert.assertTrue(roleCommandOrder.containsKey("namenode_optional_ha"));
        org.junit.Assert.assertTrue(roleCommandOrder.containsKey("resourcemanager_optional_ha"));
        java.util.Map<java.lang.String, java.lang.Object> generalDeps = ((java.util.Map<java.lang.String, java.lang.Object>) (roleCommandOrder.get("general_deps")));
        org.junit.Assert.assertTrue(generalDeps.containsKey("HBASE_MASTER-START"));
        org.junit.Assert.assertTrue(generalDeps.containsKey("HBASE_REGIONSERVER-START"));
        java.util.Map<java.lang.String, java.lang.Object> optionalNoGlusterfs = ((java.util.Map<java.lang.String, java.lang.Object>) (roleCommandOrder.get("optional_no_glusterfs")));
        org.junit.Assert.assertTrue(optionalNoGlusterfs.containsKey("SECONDARY_NAMENODE-START"));
        java.util.ArrayList<java.lang.String> hbaseMasterStartValues = ((java.util.ArrayList<java.lang.String>) (generalDeps.get("HBASE_MASTER-START")));
        org.junit.Assert.assertTrue(hbaseMasterStartValues.get(0).equals("ZOOKEEPER_SERVER-START"));
        org.apache.ambari.server.state.ServiceInfo service = stack.getService("PIG");
        org.junit.Assert.assertNotNull("PIG's roll command order is null", service.getRoleCommandOrder());
        org.junit.Assert.assertTrue(optionalNoGlusterfs.containsKey("NAMENODE-STOP"));
        java.util.ArrayList<java.lang.String> nameNodeStopValues = ((java.util.ArrayList<java.lang.String>) (optionalNoGlusterfs.get("NAMENODE-STOP")));
        org.junit.Assert.assertTrue(nameNodeStopValues.contains("JOBTRACKER-STOP"));
        org.junit.Assert.assertTrue(nameNodeStopValues.contains("CUSTOM_MASTER-STOP"));
        org.junit.Assert.assertTrue(generalDeps.containsKey("CUSTOM_MASTER-START"));
        java.util.ArrayList<java.lang.String> customMasterStartValues = ((java.util.ArrayList<java.lang.String>) (generalDeps.get("CUSTOM_MASTER-START")));
        org.junit.Assert.assertTrue(customMasterStartValues.contains("ZOOKEEPER_SERVER-START"));
        org.junit.Assert.assertTrue(customMasterStartValues.contains("NAMENODE-START"));
    }

    @org.junit.Test
    public void testUpgradePacksInitializedAfterUnmarshalling() throws java.lang.Exception {
        org.apache.ambari.server.state.StackInfo stack = org.apache.ambari.server.stack.StackManagerTest.stackManager.getStack("HDP", "2.2.0");
        java.util.Map<java.lang.String, org.apache.ambari.server.stack.upgrade.UpgradePack> upgradePacks = stack.getUpgradePacks();
        for (org.apache.ambari.server.stack.upgrade.UpgradePack upgradePack : upgradePacks.values()) {
            org.junit.Assert.assertNotNull(upgradePack);
            org.junit.Assert.assertNotNull(upgradePack.getTasks());
            org.junit.Assert.assertTrue(upgradePack.getTasks().size() > 0);
            org.junit.Assert.assertTrue(upgradePack.getTasks() == upgradePack.getTasks());
        }
        org.apache.ambari.server.stack.upgrade.ConfigUpgradePack configUpgradePack = stack.getConfigUpgradePack();
        org.junit.Assert.assertNotNull(configUpgradePack);
        org.junit.Assert.assertNotNull(configUpgradePack.services);
    }

    @org.junit.Test
    public void testMetricsLoaded() throws java.lang.Exception {
        java.net.URL rootDirectoryURL = org.apache.ambari.server.stack.StackManagerTest.class.getResource("/");
        org.springframework.util.Assert.notNull(rootDirectoryURL);
        java.io.File resourcesDirectory = new java.io.File(new java.io.File(rootDirectoryURL.getFile()).getParentFile().getParentFile(), "src/test/resources");
        java.io.File stackRoot = new java.io.File(resourcesDirectory, "stacks");
        java.io.File commonServices = new java.io.File(resourcesDirectory, "common-services");
        java.io.File extensions = null;
        try {
            java.net.URL extensionsURL = java.lang.ClassLoader.getSystemClassLoader().getResource("extensions");
            if (extensionsURL != null) {
                extensions = new java.io.File(extensionsURL.getPath().replace("test-classes", "classes"));
            }
        } catch (java.lang.Exception e) {
        }
        org.apache.ambari.server.orm.dao.MetainfoDAO metaInfoDao = EasyMock.createNiceMock(org.apache.ambari.server.orm.dao.MetainfoDAO.class);
        org.apache.ambari.server.orm.dao.StackDAO stackDao = EasyMock.createNiceMock(org.apache.ambari.server.orm.dao.StackDAO.class);
        org.apache.ambari.server.orm.dao.ExtensionDAO extensionDao = EasyMock.createNiceMock(org.apache.ambari.server.orm.dao.ExtensionDAO.class);
        org.apache.ambari.server.orm.dao.ExtensionLinkDAO linkDao = EasyMock.createNiceMock(org.apache.ambari.server.orm.dao.ExtensionLinkDAO.class);
        org.apache.ambari.server.metadata.ActionMetadata actionMetadata = EasyMock.createNiceMock(org.apache.ambari.server.metadata.ActionMetadata.class);
        org.apache.ambari.server.configuration.Configuration config = EasyMock.createNiceMock(org.apache.ambari.server.configuration.Configuration.class);
        org.apache.ambari.server.orm.entities.ExtensionEntity extensionEntity = EasyMock.createNiceMock(org.apache.ambari.server.orm.entities.ExtensionEntity.class);
        EasyMock.expect(config.getSharedResourcesDirPath()).andReturn(java.lang.ClassLoader.getSystemClassLoader().getResource("").getPath()).anyTimes();
        EasyMock.expect(extensionDao.find(org.easymock.EasyMock.anyObject(java.lang.String.class), org.easymock.EasyMock.anyObject(java.lang.String.class))).andReturn(extensionEntity).atLeastOnce();
        java.util.List<org.apache.ambari.server.orm.entities.ExtensionLinkEntity> list = java.util.Collections.emptyList();
        EasyMock.expect(linkDao.findByStack(org.easymock.EasyMock.anyObject(java.lang.String.class), org.easymock.EasyMock.anyObject(java.lang.String.class))).andReturn(list).atLeastOnce();
        EasyMock.replay(config, metaInfoDao, stackDao, extensionDao, linkDao, actionMetadata);
        org.apache.ambari.server.state.stack.OsFamily osFamily = new org.apache.ambari.server.state.stack.OsFamily(config);
        org.apache.ambari.server.controller.AmbariManagementHelper helper = new org.apache.ambari.server.controller.AmbariManagementHelper(stackDao, extensionDao, linkDao);
        org.apache.ambari.server.stack.StackManager stackManager = new org.apache.ambari.server.stack.StackManager(stackRoot, commonServices, extensions, osFamily, false, metaInfoDao, actionMetadata, stackDao, extensionDao, linkDao, helper);
        for (org.apache.ambari.server.state.StackInfo stackInfo : stackManager.getStacks()) {
            for (org.apache.ambari.server.state.ServiceInfo serviceInfo : stackInfo.getServices()) {
                java.lang.reflect.Type type = new com.google.gson.reflect.TypeToken<java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.util.List<org.apache.ambari.server.state.stack.MetricDefinition>>>>() {}.getType();
                com.google.gson.Gson gson = new com.google.gson.Gson();
                java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.util.List<org.apache.ambari.server.state.stack.MetricDefinition>>> map = null;
                if (serviceInfo.getMetricsFile() != null) {
                    try {
                        map = gson.fromJson(new java.io.FileReader(serviceInfo.getMetricsFile()), type);
                    } catch (java.lang.Exception e) {
                        e.printStackTrace();
                        throw new org.apache.ambari.server.AmbariException("Failed to load metrics from file " + serviceInfo.getMetricsFile().getAbsolutePath());
                    }
                }
            }
        }
    }

    @org.junit.Test
    public void testVersionDefinitionStackRepoUpdateLinkExists() {
        org.apache.ambari.server.state.StackInfo stack = org.apache.ambari.server.stack.StackManagerTest.stackManager.getStack("HDP", "2.1.1");
        java.lang.String latestUri = stack.getRepositoryXml().getLatestURI();
        org.junit.Assert.assertTrue(latestUri != null);
        stack = org.apache.ambari.server.stack.StackManagerTest.stackManager.getStack("HDP", "2.0.8");
        latestUri = stack.getRepositoryXml().getLatestURI();
        org.junit.Assert.assertTrue(latestUri == null);
    }
}