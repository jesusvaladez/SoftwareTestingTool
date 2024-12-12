package org.apache.ambari.server.stack;
import static org.easymock.EasyMock.anyObject;
import static org.easymock.EasyMock.createNiceMock;
import static org.easymock.EasyMock.createStrictMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
public class ServiceModuleTest {
    @org.junit.Test
    public void testResolve_Comment() throws java.lang.Exception {
        java.lang.String comment = "test comment";
        org.apache.ambari.server.state.ServiceInfo info = new org.apache.ambari.server.state.ServiceInfo();
        org.apache.ambari.server.state.ServiceInfo parentInfo = new org.apache.ambari.server.state.ServiceInfo();
        info.setComment(comment);
        org.apache.ambari.server.stack.ServiceModule service = resolveService(info, parentInfo);
        org.junit.Assert.assertEquals(comment, service.getModuleInfo().getComment());
        info.setComment(null);
        parentInfo.setComment(comment);
        service = resolveService(info, parentInfo);
        org.junit.Assert.assertEquals(comment, service.getModuleInfo().getComment());
        info.setComment(comment);
        parentInfo.setComment("other comment");
        service = resolveService(info, parentInfo);
        org.junit.Assert.assertEquals(comment, service.getModuleInfo().getComment());
    }

    @org.junit.Test
    public void testResolve_DisplayName() throws java.lang.Exception {
        java.lang.String displayName = "test_display_name";
        org.apache.ambari.server.state.ServiceInfo info = new org.apache.ambari.server.state.ServiceInfo();
        org.apache.ambari.server.state.ServiceInfo parentInfo = new org.apache.ambari.server.state.ServiceInfo();
        info.setDisplayName(displayName);
        org.apache.ambari.server.stack.ServiceModule service = resolveService(info, parentInfo);
        org.junit.Assert.assertEquals(displayName, service.getModuleInfo().getDisplayName());
        info.setDisplayName(null);
        parentInfo.setDisplayName(displayName);
        service = resolveService(info, parentInfo);
        org.junit.Assert.assertEquals(displayName, service.getModuleInfo().getDisplayName());
        info.setDisplayName(displayName);
        parentInfo.setDisplayName("other display name");
        service = resolveService(info, parentInfo);
        org.junit.Assert.assertEquals(displayName, service.getModuleInfo().getDisplayName());
    }

    @org.junit.Test
    public void testResolve_Version() throws java.lang.Exception {
        java.lang.String version = "1.1";
        org.apache.ambari.server.state.ServiceInfo info = new org.apache.ambari.server.state.ServiceInfo();
        org.apache.ambari.server.state.ServiceInfo parentInfo = new org.apache.ambari.server.state.ServiceInfo();
        info.setVersion(version);
        org.apache.ambari.server.stack.ServiceModule service = resolveService(info, parentInfo);
        org.junit.Assert.assertEquals(version, service.getModuleInfo().getVersion());
        info.setVersion(null);
        parentInfo.setVersion(version);
        service = resolveService(info, parentInfo);
        org.junit.Assert.assertEquals(version, service.getModuleInfo().getVersion());
        info.setVersion(version);
        parentInfo.setVersion("1.0");
        service = resolveService(info, parentInfo);
        org.junit.Assert.assertEquals(version, service.getModuleInfo().getVersion());
    }

    @org.junit.Test
    public void testResolve_RequiredServices() throws java.lang.Exception {
        java.util.List<java.lang.String> requiredServices = new java.util.ArrayList<>();
        requiredServices.add("foo");
        requiredServices.add("bar");
        org.apache.ambari.server.state.ServiceInfo info = new org.apache.ambari.server.state.ServiceInfo();
        org.apache.ambari.server.state.ServiceInfo parentInfo = new org.apache.ambari.server.state.ServiceInfo();
        info.setRequiredServices(requiredServices);
        org.apache.ambari.server.stack.ServiceModule service = resolveService(info, parentInfo);
        org.junit.Assert.assertEquals(requiredServices, service.getModuleInfo().getRequiredServices());
        info.setRequiredServices(null);
        parentInfo.setRequiredServices(requiredServices);
        service = resolveService(info, parentInfo);
        org.junit.Assert.assertEquals(requiredServices, service.getModuleInfo().getRequiredServices());
        info.setRequiredServices(requiredServices);
        parentInfo.setRequiredServices(java.util.Collections.singletonList("other"));
        service = resolveService(info, parentInfo);
        org.junit.Assert.assertEquals(requiredServices, service.getModuleInfo().getRequiredServices());
        info.setRequiredServices(null);
        parentInfo.setRequiredServices(null);
        service = resolveService(info, parentInfo);
        org.junit.Assert.assertTrue(service.getModuleInfo().getRequiredServices().isEmpty());
    }

    @org.junit.Test
    public void testResolve_RestartRequiredAfterChange() throws java.lang.Exception {
        java.lang.Boolean isRestartRequired = true;
        org.apache.ambari.server.state.ServiceInfo info = new org.apache.ambari.server.state.ServiceInfo();
        org.apache.ambari.server.state.ServiceInfo parentInfo = new org.apache.ambari.server.state.ServiceInfo();
        info.setRestartRequiredAfterChange(isRestartRequired);
        org.apache.ambari.server.stack.ServiceModule service = resolveService(info, parentInfo);
        org.junit.Assert.assertEquals(isRestartRequired, service.getModuleInfo().isRestartRequiredAfterChange());
        info.setRestartRequiredAfterChange(null);
        parentInfo.setRestartRequiredAfterChange(isRestartRequired);
        service = resolveService(info, parentInfo);
        org.junit.Assert.assertEquals(isRestartRequired, service.getModuleInfo().isRestartRequiredAfterChange());
        info.setRestartRequiredAfterChange(isRestartRequired);
        parentInfo.setRestartRequiredAfterChange(false);
        service = resolveService(info, parentInfo);
        org.junit.Assert.assertEquals(isRestartRequired, service.getModuleInfo().isRestartRequiredAfterChange());
    }

    @org.junit.Test
    public void testResolve_MonitoringService() throws java.lang.Exception {
        java.lang.Boolean isMonitoringService = true;
        org.apache.ambari.server.state.ServiceInfo info = new org.apache.ambari.server.state.ServiceInfo();
        org.apache.ambari.server.state.ServiceInfo parentInfo = new org.apache.ambari.server.state.ServiceInfo();
        info.setMonitoringService(isMonitoringService);
        org.apache.ambari.server.stack.ServiceModule service = resolveService(info, parentInfo);
        org.junit.Assert.assertEquals(isMonitoringService, service.getModuleInfo().isMonitoringService());
        info.setMonitoringService(null);
        parentInfo.setMonitoringService(isMonitoringService);
        service = resolveService(info, parentInfo);
        org.junit.Assert.assertEquals(isMonitoringService, service.getModuleInfo().isMonitoringService());
        info.setMonitoringService(isMonitoringService);
        parentInfo.setMonitoringService(false);
        service = resolveService(info, parentInfo);
        org.junit.Assert.assertEquals(isMonitoringService, service.getModuleInfo().isMonitoringService());
    }

    @org.junit.Test
    public void testResolve_OsSpecifics() throws java.lang.Exception {
        java.util.Map<java.lang.String, org.apache.ambari.server.state.ServiceOsSpecific> osSpecifics = new java.util.HashMap<>();
        osSpecifics.put("foo", new org.apache.ambari.server.state.ServiceOsSpecific());
        org.apache.ambari.server.state.ServiceInfo info = new org.apache.ambari.server.state.ServiceInfo();
        org.apache.ambari.server.state.ServiceInfo parentInfo = new org.apache.ambari.server.state.ServiceInfo();
        info.setOsSpecifics(osSpecifics);
        org.apache.ambari.server.stack.ServiceModule service = resolveService(info, parentInfo);
        org.junit.Assert.assertEquals(osSpecifics, service.getModuleInfo().getOsSpecifics());
        info.setOsSpecifics(null);
        parentInfo.setOsSpecifics(osSpecifics);
        service = resolveService(info, parentInfo);
        org.junit.Assert.assertEquals(osSpecifics, service.getModuleInfo().getOsSpecifics());
        java.util.Map<java.lang.String, org.apache.ambari.server.state.ServiceOsSpecific> osSpecifics2 = new java.util.HashMap<>();
        osSpecifics.put("bar", new org.apache.ambari.server.state.ServiceOsSpecific());
        info.setOsSpecifics(osSpecifics);
        parentInfo.setOsSpecifics(osSpecifics2);
        service = resolveService(info, parentInfo);
        org.junit.Assert.assertEquals(osSpecifics, service.getModuleInfo().getOsSpecifics());
    }

    @org.junit.Test
    public void testResolve_CommandScript() throws java.lang.Exception {
        org.apache.ambari.server.state.CommandScriptDefinition commandScript = new org.apache.ambari.server.state.CommandScriptDefinition();
        org.apache.ambari.server.state.ServiceInfo info = new org.apache.ambari.server.state.ServiceInfo();
        org.apache.ambari.server.state.ServiceInfo parentInfo = new org.apache.ambari.server.state.ServiceInfo();
        info.setCommandScript(commandScript);
        org.apache.ambari.server.stack.ServiceModule service = resolveService(info, parentInfo);
        org.junit.Assert.assertEquals(commandScript, service.getModuleInfo().getCommandScript());
        info.setCommandScript(null);
        parentInfo.setCommandScript(commandScript);
        service = resolveService(info, parentInfo);
        org.junit.Assert.assertEquals(commandScript, service.getModuleInfo().getCommandScript());
        org.apache.ambari.server.state.CommandScriptDefinition commandScript2 = new org.apache.ambari.server.state.CommandScriptDefinition();
        info.setCommandScript(commandScript);
        parentInfo.setCommandScript(commandScript2);
        service = resolveService(info, parentInfo);
        org.junit.Assert.assertEquals(commandScript, service.getModuleInfo().getCommandScript());
    }

    @org.junit.Test
    public void testResolve_ServicePackageFolder() throws java.lang.Exception {
        java.lang.String servicePackageFolder = "packageDir";
        org.apache.ambari.server.state.ServiceInfo info = new org.apache.ambari.server.state.ServiceInfo();
        org.apache.ambari.server.state.ServiceInfo parentInfo = new org.apache.ambari.server.state.ServiceInfo();
        org.apache.ambari.server.stack.ServiceModule child = createServiceModule(info);
        org.apache.ambari.server.stack.ServiceModule parent = createServiceModule(parentInfo);
        org.junit.Assert.assertEquals("packageDir", child.getModuleInfo().getServicePackageFolder());
        parent.getModuleInfo().setServicePackageFolder(null);
        resolveService(child, parent);
        org.junit.Assert.assertEquals(servicePackageFolder, child.getModuleInfo().getServicePackageFolder());
        child = createServiceModule(info);
        parent = createServiceModule(parentInfo);
        parent.getModuleInfo().setServicePackageFolder(servicePackageFolder);
        child.getModuleInfo().setServicePackageFolder(null);
        resolveService(child, parent);
        org.junit.Assert.assertEquals(servicePackageFolder, child.getModuleInfo().getServicePackageFolder());
        child = createServiceModule(info);
        parent = createServiceModule(parentInfo);
        parent.getModuleInfo().setServicePackageFolder("someOtherDir");
        child.getModuleInfo().setServicePackageFolder(servicePackageFolder);
        resolveService(child, parent);
        org.junit.Assert.assertEquals(servicePackageFolder, child.getModuleInfo().getServicePackageFolder());
    }

    @org.junit.Test
    public void testResolve_MetricsFile() throws java.lang.Exception {
        java.io.File metricsFile = new java.io.File("testMetricsFile");
        org.apache.ambari.server.state.ServiceInfo info = new org.apache.ambari.server.state.ServiceInfo();
        org.apache.ambari.server.state.ServiceInfo parentInfo = new org.apache.ambari.server.state.ServiceInfo();
        org.apache.ambari.server.stack.ServiceModule child = createServiceModule(info);
        org.apache.ambari.server.stack.ServiceModule parent = createServiceModule(parentInfo);
        org.junit.Assert.assertEquals(metricsFile, child.getModuleInfo().getMetricsFile());
        parent.getModuleInfo().setMetricsFile(null);
        resolveService(child, parent);
        org.junit.Assert.assertEquals(metricsFile, child.getModuleInfo().getMetricsFile());
        child = createServiceModule(info);
        parent = createServiceModule(parentInfo);
        parent.getModuleInfo().setMetricsFile(metricsFile);
        child.getModuleInfo().setMetricsFile(null);
        resolveService(child, parent);
        org.junit.Assert.assertEquals(metricsFile, child.getModuleInfo().getMetricsFile());
        child = createServiceModule(info);
        parent = createServiceModule(parentInfo);
        parent.getModuleInfo().setMetricsFile(new java.io.File("someOtherDir"));
        child.getModuleInfo().setMetricsFile(metricsFile);
        resolveService(child, parent);
        org.junit.Assert.assertEquals(metricsFile, child.getModuleInfo().getMetricsFile());
    }

    @org.junit.Test
    public void testResolve_AlertsFile() throws java.lang.Exception {
        java.io.File alertsFile = new java.io.File("testAlertsFile");
        org.apache.ambari.server.state.ServiceInfo info = new org.apache.ambari.server.state.ServiceInfo();
        org.apache.ambari.server.state.ServiceInfo parentInfo = new org.apache.ambari.server.state.ServiceInfo();
        org.apache.ambari.server.stack.ServiceModule child = createServiceModule(info);
        org.apache.ambari.server.stack.ServiceModule parent = createServiceModule(parentInfo);
        org.junit.Assert.assertEquals(alertsFile, child.getModuleInfo().getAlertsFile());
        parent.getModuleInfo().setAlertsFile(null);
        resolveService(child, parent);
        org.junit.Assert.assertEquals(alertsFile, child.getModuleInfo().getAlertsFile());
        child = createServiceModule(info);
        parent = createServiceModule(parentInfo);
        parent.getModuleInfo().setAlertsFile(alertsFile);
        child.getModuleInfo().setAlertsFile(null);
        resolveService(child, parent);
        org.junit.Assert.assertEquals(alertsFile, child.getModuleInfo().getAlertsFile());
        child = createServiceModule(info);
        parent = createServiceModule(parentInfo);
        parent.getModuleInfo().setAlertsFile(new java.io.File("someOtherDir"));
        child.getModuleInfo().setAlertsFile(alertsFile);
        resolveService(child, parent);
        org.junit.Assert.assertEquals(alertsFile, child.getModuleInfo().getAlertsFile());
    }

    @org.junit.Test
    public void testResolve_KerberosDescriptorFile() throws java.lang.Exception {
        java.io.File kerberosDescriptorFile = new java.io.File("testKerberosDescriptorFile");
        org.apache.ambari.server.state.ServiceInfo info = new org.apache.ambari.server.state.ServiceInfo();
        org.apache.ambari.server.state.ServiceInfo parentInfo = new org.apache.ambari.server.state.ServiceInfo();
        org.apache.ambari.server.stack.ServiceModule child = createServiceModule(info);
        org.apache.ambari.server.stack.ServiceModule parent = createServiceModule(parentInfo);
        org.junit.Assert.assertEquals(kerberosDescriptorFile, child.getModuleInfo().getKerberosDescriptorFile());
        parent.getModuleInfo().setKerberosDescriptorFile(null);
        resolveService(child, parent);
        org.junit.Assert.assertEquals(kerberosDescriptorFile, child.getModuleInfo().getKerberosDescriptorFile());
        child = createServiceModule(info);
        parent = createServiceModule(parentInfo);
        parent.getModuleInfo().setKerberosDescriptorFile(kerberosDescriptorFile);
        child.getModuleInfo().setKerberosDescriptorFile(null);
        resolveService(child, parent);
        org.junit.Assert.assertEquals(kerberosDescriptorFile, child.getModuleInfo().getKerberosDescriptorFile());
        child = createServiceModule(info);
        parent = createServiceModule(parentInfo);
        parent.getModuleInfo().setKerberosDescriptorFile(new java.io.File("someOtherDir"));
        child.getModuleInfo().setKerberosDescriptorFile(kerberosDescriptorFile);
        resolveService(child, parent);
        org.junit.Assert.assertEquals(kerberosDescriptorFile, child.getModuleInfo().getKerberosDescriptorFile());
    }

    @org.junit.Test
    public void testResolveServiceAdvisor() throws java.lang.Exception {
        org.apache.ambari.server.state.ServiceInfo info = new org.apache.ambari.server.state.ServiceInfo();
        org.apache.ambari.server.state.ServiceInfo parentInfo = new org.apache.ambari.server.state.ServiceInfo();
        org.apache.ambari.server.stack.ServiceModule child = createServiceModule(info);
        org.apache.ambari.server.stack.ServiceModule parent = createServiceModule(parentInfo);
        parent.getModuleInfo().setServiceAdvisorType(null);
        child.getModuleInfo().setServiceAdvisorType(null);
        resolveService(child, parent);
        org.junit.Assert.assertEquals(org.apache.ambari.server.state.ServiceInfo.ServiceAdvisorType.PYTHON, child.getModuleInfo().getServiceAdvisorType());
        child.getModuleInfo().setServiceAdvisorType(org.apache.ambari.server.state.ServiceInfo.ServiceAdvisorType.JAVA);
        resolveService(child, parent);
        org.junit.Assert.assertEquals(org.apache.ambari.server.state.ServiceInfo.ServiceAdvisorType.JAVA, child.getModuleInfo().getServiceAdvisorType());
        parent.getModuleInfo().setServiceAdvisorType(org.apache.ambari.server.state.ServiceInfo.ServiceAdvisorType.JAVA);
        child.getModuleInfo().setServiceAdvisorType(null);
        resolveService(child, parent);
        org.junit.Assert.assertEquals(org.apache.ambari.server.state.ServiceInfo.ServiceAdvisorType.JAVA, child.getModuleInfo().getServiceAdvisorType());
        parent.getModuleInfo().setServiceAdvisorType(null);
        child.getModuleInfo().setServiceAdvisorType(org.apache.ambari.server.state.ServiceInfo.ServiceAdvisorType.PYTHON);
        resolveService(child, parent);
        org.junit.Assert.assertEquals(org.apache.ambari.server.state.ServiceInfo.ServiceAdvisorType.PYTHON, child.getModuleInfo().getServiceAdvisorType());
    }

    @org.junit.Test
    public void testResolve_UpgradeCheckDirectory() throws java.lang.Exception {
        java.io.File checks = new java.io.File("checks");
        org.apache.ambari.server.state.ServiceInfo info = new org.apache.ambari.server.state.ServiceInfo();
        org.apache.ambari.server.state.ServiceInfo parentInfo = new org.apache.ambari.server.state.ServiceInfo();
        org.apache.ambari.server.stack.ServiceModule child = createServiceModule(info);
        org.apache.ambari.server.stack.ServiceModule parent = createServiceModule(parentInfo);
        child.getModuleInfo().setChecksFolder(checks);
        resolveService(child, parent);
        org.junit.Assert.assertEquals(checks.getPath(), child.getModuleInfo().getChecksFolder().getPath());
        child = createServiceModule(info);
        parent = createServiceModule(parentInfo);
        parent.getModuleInfo().setChecksFolder(checks);
        resolveService(child, parent);
        org.junit.Assert.assertEquals(checks.getPath(), child.getModuleInfo().getChecksFolder().getPath());
        info.setChecksFolder(checks);
        child = createServiceModule(info);
        child.getModuleInfo().setChecksFolder(checks);
        parent = createServiceModule(parentInfo);
        parent.getModuleInfo().setChecksFolder(new java.io.File("other"));
        resolveService(child, parent);
        org.junit.Assert.assertEquals(checks.getPath(), child.getModuleInfo().getChecksFolder().getPath());
    }

    @org.junit.Test
    public void testResolve_ServerActionDirectory() throws java.lang.Exception {
        java.io.File serverActions = new java.io.File("server_actions");
        org.apache.ambari.server.state.ServiceInfo info = new org.apache.ambari.server.state.ServiceInfo();
        org.apache.ambari.server.state.ServiceInfo parentInfo = new org.apache.ambari.server.state.ServiceInfo();
        org.apache.ambari.server.stack.ServiceModule child = createServiceModule(info);
        org.apache.ambari.server.stack.ServiceModule parent = createServiceModule(parentInfo);
        child.getModuleInfo().setServerActionsFolder(serverActions);
        resolveService(child, parent);
        org.junit.Assert.assertEquals(serverActions.getPath(), child.getModuleInfo().getServerActionsFolder().getPath());
        child = createServiceModule(info);
        parent = createServiceModule(parentInfo);
        parent.getModuleInfo().setServerActionsFolder(serverActions);
        resolveService(child, parent);
        org.junit.Assert.assertEquals(serverActions.getPath(), child.getModuleInfo().getServerActionsFolder().getPath());
        info.setServerActionsFolder(serverActions);
        child = createServiceModule(info);
        child.getModuleInfo().setServerActionsFolder(serverActions);
        parent = createServiceModule(parentInfo);
        parent.getModuleInfo().setServerActionsFolder(new java.io.File("other"));
        resolveService(child, parent);
        org.junit.Assert.assertEquals(serverActions.getPath(), child.getModuleInfo().getServerActionsFolder().getPath());
    }

    @org.junit.Test
    public void testResolve_CustomCommands() throws java.lang.Exception {
        java.util.List<org.apache.ambari.server.state.CustomCommandDefinition> customCommands = new java.util.ArrayList<>();
        org.apache.ambari.server.state.CustomCommandDefinition cmd1 = new org.apache.ambari.server.state.CustomCommandDefinition();
        setPrivateField(cmd1, "name", "cmd1");
        setPrivateField(cmd1, "background", false);
        org.apache.ambari.server.state.CustomCommandDefinition cmd2 = new org.apache.ambari.server.state.CustomCommandDefinition();
        setPrivateField(cmd2, "name", "cmd2");
        customCommands.add(cmd1);
        customCommands.add(cmd2);
        org.apache.ambari.server.state.ServiceInfo info = new org.apache.ambari.server.state.ServiceInfo();
        org.apache.ambari.server.state.ServiceInfo parentInfo = new org.apache.ambari.server.state.ServiceInfo();
        info.setCustomCommands(customCommands);
        org.apache.ambari.server.stack.ServiceModule service = resolveService(info, parentInfo);
        org.junit.Assert.assertEquals(customCommands, service.getModuleInfo().getCustomCommands());
        info.setCustomCommands(null);
        parentInfo.setCustomCommands(customCommands);
        service = resolveService(info, parentInfo);
        org.junit.Assert.assertEquals(customCommands, service.getModuleInfo().getCustomCommands());
        java.util.List<org.apache.ambari.server.state.CustomCommandDefinition> parentCustomCommands = new java.util.ArrayList<>();
        org.apache.ambari.server.state.CustomCommandDefinition cmd3 = new org.apache.ambari.server.state.CustomCommandDefinition();
        setPrivateField(cmd3, "name", "cmd1");
        setPrivateField(cmd3, "background", true);
        org.apache.ambari.server.state.CustomCommandDefinition cmd4 = new org.apache.ambari.server.state.CustomCommandDefinition();
        setPrivateField(cmd4, "name", "cmd4");
        parentCustomCommands.add(cmd3);
        parentCustomCommands.add(cmd4);
        info.setCustomCommands(customCommands);
        parentInfo.setCustomCommands(parentCustomCommands);
        service = resolveService(info, parentInfo);
        java.util.Collection<org.apache.ambari.server.state.CustomCommandDefinition> mergedCommands = service.getModuleInfo().getCustomCommands();
        org.junit.Assert.assertEquals(3, mergedCommands.size());
        org.junit.Assert.assertTrue(mergedCommands.contains(cmd2));
        org.junit.Assert.assertTrue(mergedCommands.contains(cmd3));
        org.junit.Assert.assertTrue(mergedCommands.contains(cmd4));
        info.setCustomCommands(null);
        parentInfo.setCustomCommands(null);
        service = resolveService(info, parentInfo);
        org.junit.Assert.assertTrue(service.getModuleInfo().getCustomCommands().isEmpty());
    }

    @org.junit.Test
    public void testResolve_ConfigDependencies() throws java.lang.Exception {
        java.util.List<java.lang.String> configDependencies = new java.util.ArrayList<>();
        configDependencies.add("foo");
        configDependencies.add("bar");
        org.apache.ambari.server.state.ServiceInfo info = new org.apache.ambari.server.state.ServiceInfo();
        org.apache.ambari.server.state.ServiceInfo parentInfo = new org.apache.ambari.server.state.ServiceInfo();
        info.setConfigDependencies(configDependencies);
        org.apache.ambari.server.stack.ServiceModule service = resolveService(info, parentInfo);
        org.junit.Assert.assertEquals(configDependencies, service.getModuleInfo().getConfigDependencies());
        info.setConfigDependencies(null);
        parentInfo.setConfigDependencies(configDependencies);
        service = resolveService(info, parentInfo);
        org.junit.Assert.assertEquals(configDependencies, service.getModuleInfo().getConfigDependencies());
        java.util.List<java.lang.String> parentCustomCommands = new java.util.ArrayList<>();
        parentCustomCommands.add("bar");
        parentCustomCommands.add("other");
        info.setConfigDependencies(configDependencies);
        parentInfo.setConfigDependencies(parentCustomCommands);
        service = resolveService(info, parentInfo);
        java.util.Collection<java.lang.String> mergedConfigDependencies = service.getModuleInfo().getConfigDependencies();
        org.junit.Assert.assertEquals(3, mergedConfigDependencies.size());
        org.junit.Assert.assertTrue(mergedConfigDependencies.contains("foo"));
        org.junit.Assert.assertTrue(mergedConfigDependencies.contains("bar"));
        org.junit.Assert.assertTrue(mergedConfigDependencies.contains("other"));
        info.setConfigDependencies(null);
        parentInfo.setConfigDependencies(null);
        service = resolveService(info, parentInfo);
        org.junit.Assert.assertTrue(service.getModuleInfo().getConfigDependencies().isEmpty());
    }

    @org.junit.Test
    public void testResolve_Components() throws java.lang.Exception {
        org.apache.ambari.server.state.ComponentInfo info1 = new org.apache.ambari.server.state.ComponentInfo();
        info1.setName("1");
        org.apache.ambari.server.state.ComponentInfo info2 = new org.apache.ambari.server.state.ComponentInfo();
        info2.setName("2");
        org.apache.ambari.server.state.ComponentInfo XX = new org.apache.ambari.server.state.ComponentInfo();
        XX.setName("XX");
        org.apache.ambari.server.state.ComponentInfo info3 = new org.apache.ambari.server.state.ComponentInfo();
        info3.setName("1");
        info3.setCardinality("ALL");
        info3.setCategory("category");
        org.apache.ambari.server.state.ComponentInfo info4 = new org.apache.ambari.server.state.ComponentInfo();
        info4.setName("4");
        org.apache.ambari.server.state.ComponentInfo info5 = new org.apache.ambari.server.state.ComponentInfo();
        info5.setName("XX");
        info5.setDeleted(true);
        org.apache.ambari.server.state.ServiceInfo info = new org.apache.ambari.server.state.ServiceInfo();
        org.apache.ambari.server.state.ServiceInfo parentInfo = new org.apache.ambari.server.state.ServiceInfo();
        java.util.List<org.apache.ambari.server.state.ComponentInfo> childComponents = info.getComponents();
        childComponents.add(info3);
        childComponents.add(info4);
        childComponents.add(info5);
        java.util.List<org.apache.ambari.server.state.ComponentInfo> parentComponents = parentInfo.getComponents();
        parentComponents.add(info1);
        parentComponents.add(info2);
        org.apache.ambari.server.stack.ServiceModule child = createServiceModule(info);
        org.apache.ambari.server.stack.ServiceModule parent = createServiceModule(parentInfo);
        resolveService(child, parent);
        java.util.List<org.apache.ambari.server.state.ComponentInfo> components = child.getModuleInfo().getComponents();
        org.junit.Assert.assertEquals(3, components.size());
        java.util.Map<java.lang.String, org.apache.ambari.server.state.ComponentInfo> mergedComponents = new java.util.HashMap<>();
        for (org.apache.ambari.server.state.ComponentInfo component : components) {
            mergedComponents.put(component.getName(), component);
        }
        org.junit.Assert.assertTrue(mergedComponents.containsKey("1"));
        org.junit.Assert.assertTrue(mergedComponents.containsKey("2"));
        org.junit.Assert.assertTrue(mergedComponents.containsKey("4"));
        org.junit.Assert.assertEquals("ALL", mergedComponents.get("1").getCardinality());
        org.junit.Assert.assertEquals("category", mergedComponents.get("1").getCategory());
    }

    @org.junit.Test
    public void testResolve_Configuration__properties() throws java.lang.Exception {
        org.apache.ambari.server.state.ServiceInfo info = new org.apache.ambari.server.state.ServiceInfo();
        org.apache.ambari.server.state.ServiceInfo parentInfo = new org.apache.ambari.server.state.ServiceInfo();
        java.util.Collection<org.apache.ambari.server.state.PropertyInfo> childFooProperties = new java.util.ArrayList<>();
        org.apache.ambari.server.state.PropertyInfo childProp1 = new org.apache.ambari.server.state.PropertyInfo();
        childProp1.setName("childName1");
        childProp1.setValue("childVal1");
        childFooProperties.add(childProp1);
        java.util.Collection<org.apache.ambari.server.state.PropertyInfo> childBarProperties = new java.util.ArrayList<>();
        org.apache.ambari.server.state.PropertyInfo childProp2 = new org.apache.ambari.server.state.PropertyInfo();
        childProp2.setName("childName2");
        childProp2.setValue("childVal2");
        childBarProperties.add(childProp2);
        java.util.Map<java.lang.String, java.lang.String> attributes = new java.util.HashMap<>();
        attributes.put(org.apache.ambari.server.stack.ConfigurationInfo.Supports.DO_NOT_EXTEND.getXmlAttributeName(), "true");
        org.apache.ambari.server.stack.ConfigurationModule childConfigModule1 = createConfigurationModule("FOO", childFooProperties);
        org.apache.ambari.server.stack.ConfigurationModule childConfigModule2 = createConfigurationModule("BAR", childBarProperties, attributes);
        java.util.Collection<org.apache.ambari.server.stack.ConfigurationModule> childModules = new java.util.ArrayList<>();
        childModules.add(childConfigModule1);
        childModules.add(childConfigModule2);
        java.util.Collection<org.apache.ambari.server.state.PropertyInfo> parentFooProperties = new java.util.ArrayList<>();
        org.apache.ambari.server.state.PropertyInfo parentProp1 = new org.apache.ambari.server.state.PropertyInfo();
        parentProp1.setName("parentName1");
        parentProp1.setValue("parentVal1");
        parentFooProperties.add(parentProp1);
        org.apache.ambari.server.state.PropertyInfo parentProp12 = new org.apache.ambari.server.state.PropertyInfo();
        parentProp12.setName("childName1");
        parentProp12.setValue("parentVal1");
        parentFooProperties.add(parentProp12);
        java.util.Collection<org.apache.ambari.server.state.PropertyInfo> parentBarProperties = new java.util.ArrayList<>();
        org.apache.ambari.server.state.PropertyInfo parentProp2 = new org.apache.ambari.server.state.PropertyInfo();
        parentProp2.setName("parentName2");
        parentProp2.setValue("parentVal2");
        parentBarProperties.add(parentProp2);
        java.util.Collection<org.apache.ambari.server.state.PropertyInfo> parentOtherProperties = new java.util.ArrayList<>();
        org.apache.ambari.server.state.PropertyInfo parentProp3 = new org.apache.ambari.server.state.PropertyInfo();
        parentProp3.setName("parentName3");
        parentProp3.setValue("parentVal3");
        parentOtherProperties.add(parentProp3);
        org.apache.ambari.server.stack.ConfigurationModule parentConfigModule1 = createConfigurationModule("FOO", parentFooProperties);
        org.apache.ambari.server.stack.ConfigurationModule parentConfigModule2 = createConfigurationModule("BAR", parentBarProperties);
        org.apache.ambari.server.stack.ConfigurationModule parentConfigModule3 = createConfigurationModule("OTHER", parentOtherProperties);
        java.util.Collection<org.apache.ambari.server.stack.ConfigurationModule> parentModules = new java.util.ArrayList<>();
        parentModules.add(parentConfigModule1);
        parentModules.add(parentConfigModule2);
        parentModules.add(parentConfigModule3);
        org.apache.ambari.server.stack.ServiceModule child = createServiceModule(info, childModules);
        org.apache.ambari.server.stack.ServiceModule parent = createServiceModule(parentInfo, parentModules);
        resolveService(child, parent);
        java.util.List<org.apache.ambari.server.state.PropertyInfo> mergedProperties = child.getModuleInfo().getProperties();
        org.junit.Assert.assertEquals(4, mergedProperties.size());
        java.util.Map<java.lang.String, org.apache.ambari.server.state.PropertyInfo> mergedPropertyMap = new java.util.HashMap<>();
        for (org.apache.ambari.server.state.PropertyInfo prop : mergedProperties) {
            mergedPropertyMap.put(prop.getName(), prop);
        }
        org.junit.Assert.assertEquals("childVal1", mergedPropertyMap.get("childName1").getValue());
        org.junit.Assert.assertEquals("childVal2", mergedPropertyMap.get("childName2").getValue());
        org.junit.Assert.assertEquals("parentVal1", mergedPropertyMap.get("parentName1").getValue());
        org.junit.Assert.assertEquals("parentVal3", mergedPropertyMap.get("parentName3").getValue());
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>>> childAttributes = child.getModuleInfo().getConfigTypeAttributes();
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>>> parentAttributes = parent.getModuleInfo().getConfigTypeAttributes();
        org.junit.Assert.assertEquals(3, childAttributes.size());
        assertAttributes(childAttributes.get("FOO"), java.util.Collections.emptyMap());
        assertAttributes(childAttributes.get("BAR"), attributes);
        assertAttributes(childAttributes.get("OTHER"), java.util.Collections.emptyMap());
        org.junit.Assert.assertEquals(3, parentAttributes.size());
        assertAttributes(parentAttributes.get("FOO"), java.util.Collections.emptyMap());
        assertAttributes(parentAttributes.get("BAR"), java.util.Collections.emptyMap());
        assertAttributes(parentAttributes.get("OTHER"), java.util.Collections.emptyMap());
    }

    @org.junit.Test
    public void testResolve_Service__selection() throws java.lang.Exception {
        org.apache.ambari.server.state.ServiceInfo firstInfo = new org.apache.ambari.server.state.ServiceInfo();
        org.apache.ambari.server.state.ServiceInfo secondInfo = new org.apache.ambari.server.state.ServiceInfo();
        org.apache.ambari.server.state.ServiceInfo thirdInfo = new org.apache.ambari.server.state.ServiceInfo();
        firstInfo.setSelection(org.apache.ambari.server.state.ServiceInfo.Selection.MANDATORY);
        resolveService(secondInfo, firstInfo);
        org.junit.Assert.assertEquals(secondInfo.getSelection(), org.apache.ambari.server.state.ServiceInfo.Selection.MANDATORY);
        thirdInfo.setSelection(org.apache.ambari.server.state.ServiceInfo.Selection.TECH_PREVIEW);
        resolveService(thirdInfo, secondInfo);
        org.junit.Assert.assertEquals(thirdInfo.getSelection(), org.apache.ambari.server.state.ServiceInfo.Selection.TECH_PREVIEW);
    }

    @org.junit.Test
    public void testResolve_Configuration__attributes() throws java.lang.Exception {
        org.apache.ambari.server.state.ServiceInfo info = new org.apache.ambari.server.state.ServiceInfo();
        org.apache.ambari.server.state.ServiceInfo parentInfo = new org.apache.ambari.server.state.ServiceInfo();
        java.util.Collection<org.apache.ambari.server.state.PropertyInfo> childFooProperties = new java.util.ArrayList<>();
        org.apache.ambari.server.state.PropertyInfo childProp1 = new org.apache.ambari.server.state.PropertyInfo();
        childProp1.setName("childName1");
        childProp1.setValue("childVal1");
        childFooProperties.add(childProp1);
        java.util.Map<java.lang.String, java.lang.String> childFooAttributes = new java.util.HashMap<>();
        childFooAttributes.put(org.apache.ambari.server.stack.ConfigurationInfo.Supports.ADDING_FORBIDDEN.getXmlAttributeName(), "false");
        org.apache.ambari.server.stack.ConfigurationModule childConfigModule1 = createConfigurationModule("FOO", childFooProperties, childFooAttributes);
        java.util.Collection<org.apache.ambari.server.stack.ConfigurationModule> childModules = new java.util.ArrayList<>();
        childModules.add(childConfigModule1);
        java.util.Collection<org.apache.ambari.server.state.PropertyInfo> parentFooProperties = new java.util.ArrayList<>();
        org.apache.ambari.server.state.PropertyInfo parentProp1 = new org.apache.ambari.server.state.PropertyInfo();
        parentProp1.setName("parentName1");
        parentProp1.setValue("parentVal1");
        parentFooProperties.add(parentProp1);
        java.util.Map<java.lang.String, java.lang.String> parentFooAttributes = new java.util.HashMap<>();
        parentFooAttributes.put(org.apache.ambari.server.stack.ConfigurationInfo.Supports.FINAL.getXmlAttributeName(), "true");
        parentFooAttributes.put(org.apache.ambari.server.stack.ConfigurationInfo.Supports.ADDING_FORBIDDEN.getXmlAttributeName(), "true");
        java.util.Collection<org.apache.ambari.server.state.PropertyInfo> parentBarProperties = new java.util.ArrayList<>();
        org.apache.ambari.server.state.PropertyInfo parentProp2 = new org.apache.ambari.server.state.PropertyInfo();
        parentProp2.setName("parentName2");
        parentProp2.setValue("parentVal2");
        parentBarProperties.add(parentProp2);
        org.apache.ambari.server.stack.ConfigurationModule parentConfigModule1 = createConfigurationModule("FOO", parentFooProperties, parentFooAttributes);
        org.apache.ambari.server.stack.ConfigurationModule parentConfigModule2 = createConfigurationModule("BAR", parentBarProperties);
        java.util.Collection<org.apache.ambari.server.stack.ConfigurationModule> parentModules = new java.util.ArrayList<>();
        parentModules.add(parentConfigModule1);
        parentModules.add(parentConfigModule2);
        org.apache.ambari.server.stack.ServiceModule child = createServiceModule(info, childModules);
        org.apache.ambari.server.stack.ServiceModule parent = createServiceModule(parentInfo, parentModules);
        resolveService(child, parent);
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>>> childTypeAttributes = child.getModuleInfo().getConfigTypeAttributes();
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>>> parentTypeAttributes = parent.getModuleInfo().getConfigTypeAttributes();
        org.junit.Assert.assertTrue(childTypeAttributes.containsKey("FOO"));
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> mergedChildFooAttributes = childTypeAttributes.get("FOO");
        org.junit.Assert.assertTrue(mergedChildFooAttributes.containsKey(org.apache.ambari.server.stack.ConfigurationInfo.Supports.KEYWORD));
        org.junit.Assert.assertEquals("true", mergedChildFooAttributes.get(org.apache.ambari.server.stack.ConfigurationInfo.Supports.KEYWORD).get(org.apache.ambari.server.stack.ConfigurationInfo.Supports.valueOf("FINAL").getPropertyName()));
        org.junit.Assert.assertEquals("false", mergedChildFooAttributes.get(org.apache.ambari.server.stack.ConfigurationInfo.Supports.KEYWORD).get(org.apache.ambari.server.stack.ConfigurationInfo.Supports.valueOf("ADDING_FORBIDDEN").getPropertyName()));
        org.junit.Assert.assertEquals(2, childTypeAttributes.size());
        org.junit.Assert.assertEquals(2, parentTypeAttributes.size());
        assertAttributes(parentTypeAttributes.get("FOO"), parentFooAttributes);
        assertAttributes(parentTypeAttributes.get("BAR"), java.util.Collections.emptyMap());
    }

    @org.junit.Test
    public void testResolve_Configuration__ExcludedTypes() throws java.lang.Exception {
        org.apache.ambari.server.state.ServiceInfo info = new org.apache.ambari.server.state.ServiceInfo();
        info.setExcludedConfigTypes(java.util.Collections.singleton("BAR"));
        java.util.Collection<org.apache.ambari.server.state.PropertyInfo> fooProperties = new java.util.ArrayList<>();
        org.apache.ambari.server.state.PropertyInfo prop1 = new org.apache.ambari.server.state.PropertyInfo();
        prop1.setName("name1");
        prop1.setValue("val1");
        fooProperties.add(prop1);
        org.apache.ambari.server.state.PropertyInfo prop2 = new org.apache.ambari.server.state.PropertyInfo();
        prop2.setName("name2");
        prop2.setValue("val2");
        fooProperties.add(prop2);
        java.util.Collection<org.apache.ambari.server.state.PropertyInfo> barProperties = new java.util.ArrayList<>();
        org.apache.ambari.server.state.PropertyInfo prop3 = new org.apache.ambari.server.state.PropertyInfo();
        prop3.setName("name1");
        prop3.setValue("val3");
        barProperties.add(prop3);
        java.util.Collection<org.apache.ambari.server.state.PropertyInfo> otherProperties = new java.util.ArrayList<>();
        org.apache.ambari.server.state.PropertyInfo prop4 = new org.apache.ambari.server.state.PropertyInfo();
        prop4.setName("name1");
        prop4.setValue("val4");
        otherProperties.add(prop4);
        org.apache.ambari.server.stack.ConfigurationModule configModule1 = createConfigurationModule("FOO", fooProperties);
        org.apache.ambari.server.stack.ConfigurationModule configModule2 = createConfigurationModule("BAR", barProperties);
        org.apache.ambari.server.stack.ConfigurationModule configModule3 = createConfigurationModule("OTHER", otherProperties);
        java.util.Collection<org.apache.ambari.server.stack.ConfigurationModule> configModules = new java.util.ArrayList<>();
        configModules.add(configModule1);
        configModules.add(configModule2);
        configModules.add(configModule3);
        org.apache.ambari.server.stack.ServiceModule service = createServiceModule(info, configModules);
        java.util.List<org.apache.ambari.server.state.PropertyInfo> properties = service.getModuleInfo().getProperties();
        org.junit.Assert.assertEquals(4, properties.size());
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>>> attributes = service.getModuleInfo().getConfigTypeAttributes();
        org.junit.Assert.assertEquals(2, attributes.size());
        org.junit.Assert.assertTrue(attributes.containsKey("FOO"));
        org.junit.Assert.assertTrue(attributes.containsKey("OTHER"));
    }

    @org.junit.Test
    public void testResolve_Configuration__ExcludedTypes__ParentType() throws java.lang.Exception {
        org.apache.ambari.server.state.ServiceInfo info = new org.apache.ambari.server.state.ServiceInfo();
        info.setExcludedConfigTypes(java.util.Collections.singleton("BAR"));
        java.util.Collection<org.apache.ambari.server.state.PropertyInfo> fooProperties = new java.util.ArrayList<>();
        org.apache.ambari.server.state.PropertyInfo prop1 = new org.apache.ambari.server.state.PropertyInfo();
        prop1.setName("name1");
        prop1.setValue("val1");
        fooProperties.add(prop1);
        org.apache.ambari.server.state.PropertyInfo prop2 = new org.apache.ambari.server.state.PropertyInfo();
        prop2.setName("name2");
        prop2.setValue("val2");
        fooProperties.add(prop2);
        org.apache.ambari.server.stack.ConfigurationModule childConfigModule = createConfigurationModule("FOO", fooProperties);
        java.util.Collection<org.apache.ambari.server.stack.ConfigurationModule> childConfigModules = new java.util.ArrayList<>();
        childConfigModules.add(childConfigModule);
        org.apache.ambari.server.state.ServiceInfo parentInfo = new org.apache.ambari.server.state.ServiceInfo();
        java.util.Collection<org.apache.ambari.server.state.PropertyInfo> barProperties = new java.util.ArrayList<>();
        org.apache.ambari.server.state.PropertyInfo prop3 = new org.apache.ambari.server.state.PropertyInfo();
        prop3.setName("name1");
        prop3.setValue("val3");
        barProperties.add(prop3);
        org.apache.ambari.server.stack.ConfigurationModule parentConfigModule = createConfigurationModule("BAR", barProperties);
        java.util.Collection<org.apache.ambari.server.stack.ConfigurationModule> parentConfigModules = new java.util.ArrayList<>();
        parentConfigModules.add(parentConfigModule);
        org.apache.ambari.server.stack.ServiceModule service = createServiceModule(info, childConfigModules);
        org.apache.ambari.server.stack.ServiceModule parentService = createServiceModule(parentInfo, parentConfigModules);
        resolveService(service, parentService);
        java.util.List<org.apache.ambari.server.state.PropertyInfo> properties = service.getModuleInfo().getProperties();
        org.junit.Assert.assertEquals(2, properties.size());
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>>> attributes = service.getModuleInfo().getConfigTypeAttributes();
        org.junit.Assert.assertEquals(1, attributes.size());
        org.junit.Assert.assertTrue(attributes.containsKey("FOO"));
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>>> parentAttributes = parentService.getModuleInfo().getConfigTypeAttributes();
        org.junit.Assert.assertEquals(1, parentAttributes.size());
        org.junit.Assert.assertTrue(parentAttributes.containsKey("BAR"));
    }

    @org.junit.Test
    public void testMerge_Configuration__ExcludedTypes() throws java.lang.Exception {
        org.apache.ambari.server.state.ServiceInfo info = new org.apache.ambari.server.state.ServiceInfo();
        java.util.Set<java.lang.String> childExcludedConfigTypes = new java.util.HashSet<>();
        childExcludedConfigTypes.add("FOO");
        info.setExcludedConfigTypes(childExcludedConfigTypes);
        java.util.Collection<org.apache.ambari.server.state.PropertyInfo> fooProperties = new java.util.ArrayList<>();
        org.apache.ambari.server.stack.ConfigurationModule childConfigModule = createConfigurationModule("FOO", fooProperties);
        java.util.Collection<org.apache.ambari.server.stack.ConfigurationModule> childConfigModules = new java.util.ArrayList<>();
        childConfigModules.add(childConfigModule);
        org.apache.ambari.server.state.ServiceInfo parentInfo = new org.apache.ambari.server.state.ServiceInfo();
        java.util.Set<java.lang.String> parentExcludedConfigTypes = new java.util.HashSet<>();
        childExcludedConfigTypes.add("BAR");
        info.setExcludedConfigTypes(childExcludedConfigTypes);
        parentInfo.setExcludedConfigTypes(parentExcludedConfigTypes);
        java.util.Collection<org.apache.ambari.server.state.PropertyInfo> barProperties = new java.util.ArrayList<>();
        org.apache.ambari.server.stack.ConfigurationModule parentConfigModule = createConfigurationModule("BAR", barProperties);
        java.util.Collection<org.apache.ambari.server.stack.ConfigurationModule> parentConfigModules = new java.util.ArrayList<>();
        parentConfigModules.add(parentConfigModule);
        org.apache.ambari.server.stack.ServiceModule service = createServiceModule(info, childConfigModules);
        org.apache.ambari.server.stack.ServiceModule parentService = createServiceModule(parentInfo, parentConfigModules);
        resolveService(service, parentService);
        org.junit.Assert.assertEquals(2, service.getModuleInfo().getExcludedConfigTypes().size());
    }

    @org.junit.Test
    public void testResolve_CredentialStoreInfo() throws java.lang.Exception {
        org.apache.ambari.server.state.CredentialStoreInfo credentialStoreInfoChild = new org.apache.ambari.server.state.CredentialStoreInfo(true, false, true);
        org.apache.ambari.server.state.CredentialStoreInfo credentialStoreInfoParent = new org.apache.ambari.server.state.CredentialStoreInfo(true, true, false);
        org.apache.ambari.server.state.ServiceInfo childInfo = new org.apache.ambari.server.state.ServiceInfo();
        org.apache.ambari.server.state.ServiceInfo parentInfo = new org.apache.ambari.server.state.ServiceInfo();
        org.apache.ambari.server.stack.ServiceModule service;
        childInfo.setCredentialStoreInfo(credentialStoreInfoChild);
        parentInfo.setCredentialStoreInfo(null);
        service = resolveService(childInfo, parentInfo);
        org.junit.Assert.assertEquals(credentialStoreInfoChild.isSupported(), service.getModuleInfo().isCredentialStoreSupported());
        org.junit.Assert.assertEquals(credentialStoreInfoChild.isEnabled(), service.getModuleInfo().isCredentialStoreEnabled());
        org.junit.Assert.assertEquals(credentialStoreInfoChild.isRequired(), service.getModuleInfo().isCredentialStoreRequired());
        childInfo.setCredentialStoreInfo(null);
        parentInfo.setCredentialStoreInfo(credentialStoreInfoParent);
        service = resolveService(childInfo, parentInfo);
        org.junit.Assert.assertEquals(credentialStoreInfoParent.isSupported(), service.getModuleInfo().isCredentialStoreSupported());
        org.junit.Assert.assertEquals(credentialStoreInfoParent.isEnabled(), service.getModuleInfo().isCredentialStoreEnabled());
        org.junit.Assert.assertEquals(credentialStoreInfoParent.isRequired(), service.getModuleInfo().isCredentialStoreRequired());
        childInfo.setCredentialStoreInfo(credentialStoreInfoChild);
        parentInfo.setCredentialStoreInfo(credentialStoreInfoParent);
        service = resolveService(childInfo, parentInfo);
        org.junit.Assert.assertEquals(credentialStoreInfoChild.isSupported(), service.getModuleInfo().isCredentialStoreSupported());
        org.junit.Assert.assertEquals(credentialStoreInfoChild.isEnabled(), service.getModuleInfo().isCredentialStoreEnabled());
        org.junit.Assert.assertEquals(credentialStoreInfoChild.isRequired(), service.getModuleInfo().isCredentialStoreRequired());
    }

    @org.junit.Test
    public void testResolve_SingleSignOnInfo() throws java.lang.Exception {
        org.apache.ambari.server.state.SingleSignOnInfo singleSignOnInfoChild = new org.apache.ambari.server.state.SingleSignOnInfo(false, null, true);
        org.apache.ambari.server.state.SingleSignOnInfo singleSignOnInfoParent = new org.apache.ambari.server.state.SingleSignOnInfo(true, "config-type/property_name", false);
        org.apache.ambari.server.state.ServiceInfo childInfo = new org.apache.ambari.server.state.ServiceInfo();
        org.apache.ambari.server.state.ServiceInfo parentInfo = new org.apache.ambari.server.state.ServiceInfo();
        org.apache.ambari.server.stack.ServiceModule serviceModule;
        org.apache.ambari.server.state.ServiceInfo serviceInfo;
        childInfo.setSingleSignOnInfo(singleSignOnInfoChild);
        parentInfo.setSingleSignOnInfo(null);
        serviceModule = resolveService(childInfo, parentInfo);
        serviceInfo = serviceModule.getModuleInfo();
        org.junit.Assert.assertEquals(singleSignOnInfoChild.isSupported(), serviceInfo.isSingleSignOnSupported());
        org.junit.Assert.assertEquals(singleSignOnInfoChild.isSupported(), serviceInfo.getSingleSignOnInfo().isSupported());
        org.junit.Assert.assertEquals(singleSignOnInfoChild.getSupported(), serviceInfo.getSingleSignOnInfo().getSupported());
        org.junit.Assert.assertEquals(singleSignOnInfoChild.getEnabledConfiguration(), serviceInfo.getSingleSignOnInfo().getEnabledConfiguration());
        org.junit.Assert.assertEquals(singleSignOnInfoChild.getSsoEnabledTest(), serviceInfo.getSingleSignOnInfo().getSsoEnabledTest());
        org.junit.Assert.assertEquals(singleSignOnInfoChild.isKerberosRequired(), serviceInfo.isKerberosRequiredForSingleSignOnIntegration());
        org.junit.Assert.assertEquals(singleSignOnInfoChild.isKerberosRequired(), serviceInfo.getSingleSignOnInfo().isKerberosRequired());
        childInfo.setSingleSignOnInfo(null);
        parentInfo.setSingleSignOnInfo(singleSignOnInfoParent);
        serviceModule = resolveService(childInfo, parentInfo);
        serviceInfo = serviceModule.getModuleInfo();
        org.junit.Assert.assertEquals(singleSignOnInfoParent.isSupported(), serviceInfo.isSingleSignOnSupported());
        org.junit.Assert.assertEquals(singleSignOnInfoParent.isSupported(), serviceInfo.getSingleSignOnInfo().isSupported());
        org.junit.Assert.assertEquals(singleSignOnInfoParent.getSupported(), serviceInfo.getSingleSignOnInfo().getSupported());
        org.junit.Assert.assertEquals(singleSignOnInfoParent.getEnabledConfiguration(), serviceInfo.getSingleSignOnInfo().getEnabledConfiguration());
        org.junit.Assert.assertEquals(singleSignOnInfoParent.getSsoEnabledTest(), serviceInfo.getSingleSignOnInfo().getSsoEnabledTest());
        org.junit.Assert.assertEquals(singleSignOnInfoParent.isKerberosRequired(), serviceInfo.isKerberosRequiredForSingleSignOnIntegration());
        org.junit.Assert.assertEquals(singleSignOnInfoParent.isKerberosRequired(), serviceInfo.getSingleSignOnInfo().isKerberosRequired());
        childInfo.setSingleSignOnInfo(singleSignOnInfoChild);
        parentInfo.setSingleSignOnInfo(singleSignOnInfoParent);
        serviceModule = resolveService(childInfo, parentInfo);
        serviceInfo = serviceModule.getModuleInfo();
        org.junit.Assert.assertEquals(singleSignOnInfoChild.isSupported(), serviceInfo.isSingleSignOnSupported());
        org.junit.Assert.assertEquals(singleSignOnInfoChild.isSupported(), serviceInfo.getSingleSignOnInfo().isSupported());
        org.junit.Assert.assertEquals(singleSignOnInfoChild.getSupported(), serviceInfo.getSingleSignOnInfo().getSupported());
        org.junit.Assert.assertEquals(singleSignOnInfoChild.getEnabledConfiguration(), serviceInfo.getSingleSignOnInfo().getEnabledConfiguration());
        org.junit.Assert.assertEquals(singleSignOnInfoChild.getSsoEnabledTest(), serviceInfo.getSingleSignOnInfo().getSsoEnabledTest());
        org.junit.Assert.assertEquals(singleSignOnInfoChild.isKerberosRequired(), serviceInfo.isKerberosRequiredForSingleSignOnIntegration());
        org.junit.Assert.assertEquals(singleSignOnInfoChild.isKerberosRequired(), serviceInfo.getSingleSignOnInfo().isKerberosRequired());
    }

    @org.junit.Test
    public void testServiceCheckRegistered() throws java.lang.Exception {
        org.apache.ambari.server.state.ServiceInfo info = new org.apache.ambari.server.state.ServiceInfo();
        info.setName("service1");
        info.setCommandScript(EasyMock.createNiceMock(org.apache.ambari.server.state.CommandScriptDefinition.class));
        org.apache.ambari.server.stack.StackContext context = createStackContext(info.getName(), true);
        org.apache.ambari.server.stack.ServiceModule service = createServiceModule(info, java.util.Collections.emptySet(), context);
        service.finalizeModule();
        EasyMock.verify(context);
    }

    @org.junit.Test
    public void testServiceCheckNotRegisteredForDeletedService() throws java.lang.Exception {
        org.apache.ambari.server.state.ServiceInfo info = new org.apache.ambari.server.state.ServiceInfo();
        info.setName("service1");
        info.setCommandScript(EasyMock.createNiceMock(org.apache.ambari.server.state.CommandScriptDefinition.class));
        info.setDeleted(true);
        org.apache.ambari.server.stack.StackContext context = createStackContext(info.getName(), false);
        org.apache.ambari.server.stack.ServiceModule service = createServiceModule(info, java.util.Collections.emptySet(), context);
        service.finalizeModule();
        EasyMock.verify(context);
    }

    @org.junit.Test
    public void testInvalidServiceInfo() {
        org.apache.ambari.server.state.ServiceInfo serviceInfo = new org.apache.ambari.server.state.ServiceInfo();
        serviceInfo.setName("TEST_SERVICE");
        serviceInfo.setVersion("1.0.0");
        serviceInfo.setValid(false);
        serviceInfo.addError("Test error message");
        org.apache.ambari.server.stack.ServiceModule serviceModule = createServiceModule(serviceInfo);
        org.junit.Assert.assertFalse("Service module should be invalid due to the service info being invalid !", serviceModule.isValid());
        org.junit.Assert.assertTrue("Service module error collection should contain error message that caused service info being invalid !", serviceModule.getErrors().contains("Test error message"));
    }

    @org.junit.Test
    public void testMergeServicePropertiesInheritFromParent() throws java.lang.Exception {
        org.apache.ambari.server.state.ServiceInfo serviceInfo = new org.apache.ambari.server.state.ServiceInfo();
        org.apache.ambari.server.state.ServiceInfo parentServiceInfo = new org.apache.ambari.server.state.ServiceInfo();
        org.apache.ambari.server.state.ServicePropertyInfo p1 = new org.apache.ambari.server.state.ServicePropertyInfo();
        p1.setName("P1");
        p1.setValue("V1");
        org.apache.ambari.server.state.ServicePropertyInfo p2 = new org.apache.ambari.server.state.ServicePropertyInfo();
        p2.setName("P2");
        p2.setValue("V2");
        java.util.List<org.apache.ambari.server.state.ServicePropertyInfo> parentServicePropertyList = com.google.common.collect.Lists.newArrayList(p1, p2);
        parentServiceInfo.setServicePropertyList(parentServicePropertyList);
        org.apache.ambari.server.stack.ServiceModule serviceModule = resolveService(serviceInfo, parentServiceInfo);
        java.util.Map<java.lang.String, java.lang.String> parentServiceProperties = com.google.common.collect.ImmutableMap.<java.lang.String, java.lang.String>builder().put("P1", "V1").put("P2", "V2").put(org.apache.ambari.server.state.ServiceInfo.DEFAULT_SERVICE_INSTALLABLE_PROPERTY).put(org.apache.ambari.server.state.ServiceInfo.DEFAULT_SERVICE_MANAGED_PROPERTY).put(org.apache.ambari.server.state.ServiceInfo.DEFAULT_SERVICE_MONITORED_PROPERTY).build();
        org.junit.Assert.assertEquals(parentServicePropertyList, serviceModule.getModuleInfo().getServicePropertyList());
        org.junit.Assert.assertEquals(parentServiceProperties, serviceModule.getModuleInfo().getServiceProperties());
    }

    @org.junit.Test
    public void testMergeServicePropertiesInheritFromEmptyParent() throws java.lang.Exception {
        org.apache.ambari.server.state.ServiceInfo serviceInfo = new org.apache.ambari.server.state.ServiceInfo();
        org.apache.ambari.server.state.ServiceInfo parentServiceInfo = new org.apache.ambari.server.state.ServiceInfo();
        org.apache.ambari.server.state.ServicePropertyInfo p1 = new org.apache.ambari.server.state.ServicePropertyInfo();
        p1.setName("P1");
        p1.setValue("V1");
        org.apache.ambari.server.state.ServicePropertyInfo p2 = new org.apache.ambari.server.state.ServicePropertyInfo();
        p2.setName("P2");
        p2.setValue("V2");
        java.util.List<org.apache.ambari.server.state.ServicePropertyInfo> servicePropertyList = com.google.common.collect.Lists.newArrayList(p1, p2);
        serviceInfo.setServicePropertyList(servicePropertyList);
        org.apache.ambari.server.stack.ServiceModule serviceModule = resolveService(serviceInfo, parentServiceInfo);
        java.util.Map<java.lang.String, java.lang.String> serviceProperties = com.google.common.collect.ImmutableMap.<java.lang.String, java.lang.String>builder().put("P1", "V1").put("P2", "V2").put(org.apache.ambari.server.state.ServiceInfo.DEFAULT_SERVICE_INSTALLABLE_PROPERTY).put(org.apache.ambari.server.state.ServiceInfo.DEFAULT_SERVICE_MANAGED_PROPERTY).put(org.apache.ambari.server.state.ServiceInfo.DEFAULT_SERVICE_MONITORED_PROPERTY).build();
        org.junit.Assert.assertEquals(servicePropertyList, serviceModule.getModuleInfo().getServicePropertyList());
        org.junit.Assert.assertEquals(serviceProperties, serviceModule.getModuleInfo().getServiceProperties());
    }

    @org.junit.Test
    public void testMergeServiceProperties() throws java.lang.Exception {
        org.apache.ambari.server.state.ServiceInfo serviceInfo = new org.apache.ambari.server.state.ServiceInfo();
        org.apache.ambari.server.state.ServiceInfo parentServiceInfo = new org.apache.ambari.server.state.ServiceInfo();
        org.apache.ambari.server.state.ServicePropertyInfo p1 = new org.apache.ambari.server.state.ServicePropertyInfo();
        p1.setName("P1");
        p1.setValue("V1");
        org.apache.ambari.server.state.ServicePropertyInfo p2 = new org.apache.ambari.server.state.ServicePropertyInfo();
        p2.setName("P2");
        p2.setValue("V2");
        org.apache.ambari.server.state.ServicePropertyInfo p2Override = new org.apache.ambari.server.state.ServicePropertyInfo();
        p2Override.setName("P2");
        p2Override.setValue("V2_OVERRIDE");
        org.apache.ambari.server.state.ServicePropertyInfo p3 = new org.apache.ambari.server.state.ServicePropertyInfo();
        p3.setName("P3");
        p3.setValue("V3");
        java.util.List<org.apache.ambari.server.state.ServicePropertyInfo> parentServicePropertyList = com.google.common.collect.Lists.newArrayList(p1, p2);
        parentServiceInfo.setServicePropertyList(parentServicePropertyList);
        java.util.List<org.apache.ambari.server.state.ServicePropertyInfo> servicePropertyList = com.google.common.collect.Lists.newArrayList(p2Override, p3);
        serviceInfo.setServicePropertyList(servicePropertyList);
        org.apache.ambari.server.stack.ServiceModule serviceModule = resolveService(serviceInfo, parentServiceInfo);
        java.util.List<org.apache.ambari.server.state.ServicePropertyInfo> expectedPropertyList = com.google.common.collect.Lists.newArrayList(p1, p2Override, p3);
        java.util.Map<java.lang.String, java.lang.String> expectedServiceProperties = com.google.common.collect.ImmutableMap.<java.lang.String, java.lang.String>builder().put("P1", "V1").put("P2", "V2_OVERRIDE").put("P3", "V3").put(org.apache.ambari.server.state.ServiceInfo.DEFAULT_SERVICE_INSTALLABLE_PROPERTY).put(org.apache.ambari.server.state.ServiceInfo.DEFAULT_SERVICE_MANAGED_PROPERTY).put(org.apache.ambari.server.state.ServiceInfo.DEFAULT_SERVICE_MONITORED_PROPERTY).build();
        java.util.List<org.apache.ambari.server.state.ServicePropertyInfo> actualPropertyList = serviceModule.getModuleInfo().getServicePropertyList();
        org.junit.Assert.assertTrue(actualPropertyList.containsAll(expectedPropertyList) && expectedPropertyList.containsAll(actualPropertyList));
        org.junit.Assert.assertEquals(expectedServiceProperties, serviceModule.getModuleInfo().getServiceProperties());
    }

    private org.apache.ambari.server.stack.ServiceModule createServiceModule(org.apache.ambari.server.state.ServiceInfo serviceInfo) {
        java.lang.String configType = "type1";
        if (serviceInfo.getName() == null) {
            serviceInfo.setName("service1");
        }
        org.apache.ambari.server.stack.StackContext context = createStackContext(serviceInfo.getName(), true);
        org.apache.ambari.server.stack.ConfigurationInfo configInfo = createConfigurationInfo(java.util.Collections.emptyList(), java.util.Collections.emptyMap());
        org.apache.ambari.server.stack.ConfigurationModule module = createConfigurationModule(configType, configInfo);
        org.apache.ambari.server.stack.ConfigurationDirectory configDirectory = createConfigurationDirectory(java.util.Collections.singletonList(module));
        org.apache.ambari.server.stack.ServiceDirectory serviceDirectory = createServiceDirectory(serviceInfo.getConfigDir(), configDirectory);
        return createServiceModule(context, serviceInfo, serviceDirectory);
    }

    private org.apache.ambari.server.stack.ServiceModule createServiceModule(org.apache.ambari.server.state.ServiceInfo serviceInfo, java.util.Collection<org.apache.ambari.server.stack.ConfigurationModule> configurations, org.apache.ambari.server.stack.StackContext context) {
        if (serviceInfo.getName() == null) {
            serviceInfo.setName("service1");
        }
        org.apache.ambari.server.stack.ConfigurationDirectory configDirectory = createConfigurationDirectory(configurations);
        org.apache.ambari.server.stack.ServiceDirectory serviceDirectory = createServiceDirectory(serviceInfo.getConfigDir(), configDirectory);
        return createServiceModule(context, serviceInfo, serviceDirectory);
    }

    private org.apache.ambari.server.stack.ServiceModule createServiceModule(org.apache.ambari.server.state.ServiceInfo serviceInfo, java.util.Collection<org.apache.ambari.server.stack.ConfigurationModule> configurations) {
        java.lang.String serviceName = serviceInfo.getName();
        if (serviceInfo.getName() == null) {
            serviceInfo.setName("service1");
        }
        return createServiceModule(serviceInfo, configurations, createStackContext(serviceName, true));
    }

    private org.apache.ambari.server.stack.ServiceModule createServiceModule(org.apache.ambari.server.stack.StackContext context, org.apache.ambari.server.state.ServiceInfo serviceInfo, org.apache.ambari.server.stack.ServiceDirectory serviceDirectory) {
        return new org.apache.ambari.server.stack.ServiceModule(context, serviceInfo, serviceDirectory);
    }

    private org.apache.ambari.server.stack.ServiceDirectory createServiceDirectory(java.lang.String dir, org.apache.ambari.server.stack.ConfigurationDirectory configDir) {
        org.apache.ambari.server.stack.ServiceDirectory serviceDirectory = EasyMock.createNiceMock(org.apache.ambari.server.stack.ServiceDirectory.class);
        EasyMock.expect(serviceDirectory.getConfigurationDirectory(dir, org.apache.ambari.server.stack.StackDirectory.SERVICE_PROPERTIES_FOLDER_NAME)).andReturn(configDir).anyTimes();
        EasyMock.expect(serviceDirectory.getMetricsFile(EasyMock.anyObject(java.lang.String.class))).andReturn(new java.io.File("testMetricsFile")).anyTimes();
        EasyMock.expect(serviceDirectory.getWidgetsDescriptorFile(EasyMock.anyObject(java.lang.String.class))).andReturn(new java.io.File("testWidgetsFile")).anyTimes();
        EasyMock.expect(serviceDirectory.getAlertsFile()).andReturn(new java.io.File("testAlertsFile")).anyTimes();
        EasyMock.expect(serviceDirectory.getKerberosDescriptorFile()).andReturn(new java.io.File("testKerberosDescriptorFile")).anyTimes();
        EasyMock.expect(serviceDirectory.getPackageDir()).andReturn("packageDir").anyTimes();
        EasyMock.replay(serviceDirectory);
        return serviceDirectory;
    }

    private org.apache.ambari.server.stack.ConfigurationDirectory createConfigurationDirectory(java.util.Collection<org.apache.ambari.server.stack.ConfigurationModule> modules) {
        org.apache.ambari.server.stack.ConfigurationDirectory configDir = EasyMock.createNiceMock(org.apache.ambari.server.stack.ConfigurationDirectory.class);
        EasyMock.expect(configDir.getConfigurationModules()).andReturn(modules).anyTimes();
        EasyMock.replay(configDir);
        return configDir;
    }

    private org.apache.ambari.server.stack.ConfigurationModule createConfigurationModule(java.lang.String configType, org.apache.ambari.server.stack.ConfigurationInfo info) {
        return new org.apache.ambari.server.stack.ConfigurationModule(configType, info);
    }

    private org.apache.ambari.server.stack.ConfigurationModule createConfigurationModule(java.lang.String configType, java.util.Collection<org.apache.ambari.server.state.PropertyInfo> properties) {
        org.apache.ambari.server.stack.ConfigurationInfo info = new org.apache.ambari.server.stack.ConfigurationInfo(properties, java.util.Collections.emptyMap());
        return new org.apache.ambari.server.stack.ConfigurationModule(configType, info);
    }

    private org.apache.ambari.server.stack.ConfigurationModule createConfigurationModule(java.lang.String configType, java.util.Collection<org.apache.ambari.server.state.PropertyInfo> properties, java.util.Map<java.lang.String, java.lang.String> attributes) {
        org.apache.ambari.server.stack.ConfigurationInfo info = new org.apache.ambari.server.stack.ConfigurationInfo(properties, attributes);
        return new org.apache.ambari.server.stack.ConfigurationModule(configType, info);
    }

    private org.apache.ambari.server.stack.ConfigurationInfo createConfigurationInfo(java.util.Collection<org.apache.ambari.server.state.PropertyInfo> properties, java.util.Map<java.lang.String, java.lang.String> attributes) {
        return new org.apache.ambari.server.stack.ConfigurationInfo(properties, attributes);
    }

    private org.apache.ambari.server.stack.StackContext createStackContext(java.lang.String serviceName, boolean expectServiceRegistration) {
        org.apache.ambari.server.stack.StackContext context = EasyMock.createStrictMock(org.apache.ambari.server.stack.StackContext.class);
        if (expectServiceRegistration) {
            context.registerServiceCheck(serviceName);
        }
        EasyMock.replay(context);
        return context;
    }

    private org.apache.ambari.server.stack.ServiceModule resolveService(org.apache.ambari.server.state.ServiceInfo info, org.apache.ambari.server.state.ServiceInfo parentInfo) throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.server.stack.ServiceModule service = createServiceModule(info);
        org.apache.ambari.server.stack.ServiceModule parentService = createServiceModule(parentInfo);
        resolveService(service, parentService);
        return service;
    }

    private void resolveService(org.apache.ambari.server.stack.ServiceModule service, org.apache.ambari.server.stack.ServiceModule parent) throws org.apache.ambari.server.AmbariException {
        service.resolve(parent, java.util.Collections.emptyMap(), java.util.Collections.emptyMap(), java.util.Collections.emptyMap());
        service.finalizeModule();
        parent.finalizeModule();
    }

    private void assertAttributes(java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> mergedAttributes, java.util.Map<java.lang.String, java.lang.String> specifiedAttributes) {
        org.junit.Assert.assertEquals(1, mergedAttributes.size());
        java.util.Map<java.lang.String, java.lang.String> supportsAttributes = mergedAttributes.get(org.apache.ambari.server.stack.ConfigurationInfo.Supports.KEYWORD);
        org.junit.Assert.assertEquals(org.apache.ambari.server.stack.ConfigurationInfo.Supports.values().length, supportsAttributes.size());
        for (java.util.Map.Entry<java.lang.String, java.lang.String> attribute : supportsAttributes.entrySet()) {
            java.lang.String attributeName = attribute.getKey();
            java.lang.String attributeValue = attribute.getValue();
            org.apache.ambari.server.stack.ConfigurationInfo.Supports s = org.apache.ambari.server.stack.ConfigurationInfo.Supports.valueOf(attributeName.toUpperCase());
            java.lang.String specifiedVal = specifiedAttributes.get(s.getXmlAttributeName());
            if (specifiedVal != null) {
                org.junit.Assert.assertEquals(specifiedVal, attributeValue);
            } else {
                org.junit.Assert.assertEquals(s.getDefaultValue(), attributeValue);
            }
        }
    }

    private void setPrivateField(java.lang.Object o, java.lang.String field, java.lang.Object value) throws java.lang.Exception {
        java.lang.Class<?> c = o.getClass();
        java.lang.reflect.Field f = c.getDeclaredField(field);
        f.setAccessible(true);
        f.set(o, value);
    }
}