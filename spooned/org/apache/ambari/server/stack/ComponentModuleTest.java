package org.apache.ambari.server.stack;
import static org.easymock.EasyMock.createNiceMock;
public class ComponentModuleTest {
    @org.junit.Test
    public void testResolve_CommandScript() {
        org.apache.ambari.server.state.CommandScriptDefinition commandScript = new org.apache.ambari.server.state.CommandScriptDefinition();
        org.apache.ambari.server.state.ComponentInfo info = new org.apache.ambari.server.state.ComponentInfo();
        org.apache.ambari.server.state.ComponentInfo parentInfo = new org.apache.ambari.server.state.ComponentInfo();
        parentInfo.setCommandScript(commandScript);
        org.junit.Assert.assertSame(commandScript, resolveComponent(info, parentInfo).getModuleInfo().getCommandScript());
        info.setCommandScript(commandScript);
        parentInfo.setCommandScript(null);
        org.junit.Assert.assertSame(commandScript, resolveComponent(info, parentInfo).getModuleInfo().getCommandScript());
        org.apache.ambari.server.state.CommandScriptDefinition commandScript2 = EasyMock.createNiceMock(org.apache.ambari.server.state.CommandScriptDefinition.class);
        info.setCommandScript(commandScript);
        parentInfo.setCommandScript(commandScript2);
        org.junit.Assert.assertSame(commandScript, resolveComponent(info, parentInfo).getModuleInfo().getCommandScript());
    }

    @org.junit.Test
    public void testResolve_DisplayName() {
        java.lang.String displayName = "foo";
        org.apache.ambari.server.state.ComponentInfo info = new org.apache.ambari.server.state.ComponentInfo();
        org.apache.ambari.server.state.ComponentInfo parentInfo = new org.apache.ambari.server.state.ComponentInfo();
        parentInfo.setDisplayName(displayName);
        org.junit.Assert.assertEquals(displayName, resolveComponent(info, parentInfo).getModuleInfo().getDisplayName());
        info.setDisplayName(displayName);
        parentInfo.setDisplayName(null);
        org.junit.Assert.assertEquals(displayName, resolveComponent(info, parentInfo).getModuleInfo().getDisplayName());
        java.lang.String displayName2 = "foo2";
        info.setDisplayName(displayName2);
        parentInfo.setDisplayName(displayName);
        org.junit.Assert.assertEquals(displayName2, resolveComponent(info, parentInfo).getModuleInfo().getDisplayName());
    }

    @org.junit.Test
    public void testResolve_ClientConfigFiles() {
        java.util.List<org.apache.ambari.server.state.ClientConfigFileDefinition> clientConfigs = new java.util.ArrayList<>();
        org.apache.ambari.server.state.ClientConfigFileDefinition clientConfig1 = new org.apache.ambari.server.state.ClientConfigFileDefinition();
        clientConfig1.setType("type1");
        clientConfig1.setDictionaryName("dictName1");
        clientConfig1.setFileName("filename1");
        org.apache.ambari.server.state.ClientConfigFileDefinition clientConfig2 = new org.apache.ambari.server.state.ClientConfigFileDefinition();
        clientConfig1.setType("type1");
        clientConfig1.setDictionaryName("dictName1");
        clientConfig1.setFileName("filename1");
        clientConfigs.add(clientConfig1);
        clientConfigs.add(clientConfig2);
        org.apache.ambari.server.state.ComponentInfo info = new org.apache.ambari.server.state.ComponentInfo();
        org.apache.ambari.server.state.ComponentInfo parentInfo = new org.apache.ambari.server.state.ComponentInfo();
        parentInfo.setClientConfigFiles(clientConfigs);
        org.junit.Assert.assertEquals(clientConfigs, resolveComponent(info, parentInfo).getModuleInfo().getClientConfigFiles());
        info.setClientConfigFiles(clientConfigs);
        parentInfo.setClientConfigFiles(null);
        org.junit.Assert.assertEquals(clientConfigs, resolveComponent(info, parentInfo).getModuleInfo().getClientConfigFiles());
        java.util.List<org.apache.ambari.server.state.ClientConfigFileDefinition> clientConfigs2 = new java.util.ArrayList<>();
        org.apache.ambari.server.state.ClientConfigFileDefinition clientConfig3 = new org.apache.ambari.server.state.ClientConfigFileDefinition();
        clientConfig3.setType("type1");
        clientConfig3.setDictionaryName("dictName1");
        clientConfig3.setFileName("DIFFERENT filename");
        clientConfigs2.add(clientConfig3);
        info.setClientConfigFiles(clientConfigs2);
        parentInfo.setClientConfigFiles(clientConfigs);
        org.junit.Assert.assertEquals(clientConfigs2, resolveComponent(info, parentInfo).getModuleInfo().getClientConfigFiles());
    }

    @org.junit.Test
    public void testResolve_Category() {
        java.lang.String category = "foo";
        org.apache.ambari.server.state.ComponentInfo info = new org.apache.ambari.server.state.ComponentInfo();
        org.apache.ambari.server.state.ComponentInfo parentInfo = new org.apache.ambari.server.state.ComponentInfo();
        parentInfo.setCategory(category);
        org.junit.Assert.assertEquals(category, resolveComponent(info, parentInfo).getModuleInfo().getCategory());
        info.setCategory(category);
        parentInfo.setCategory(null);
        org.junit.Assert.assertEquals(category, resolveComponent(info, parentInfo).getModuleInfo().getCategory());
        java.lang.String category2 = "foo2";
        info.setCategory(category2);
        parentInfo.setCategory(category);
        org.junit.Assert.assertEquals(category2, resolveComponent(info, parentInfo).getModuleInfo().getCategory());
    }

    @org.junit.Test
    public void testResolve_Cardinality() {
        java.lang.String cardinality = "foo";
        org.apache.ambari.server.state.ComponentInfo info = new org.apache.ambari.server.state.ComponentInfo();
        org.junit.Assert.assertEquals("0+", resolveComponent(info, null).getModuleInfo().getCardinality());
        org.apache.ambari.server.state.ComponentInfo parentInfo = new org.apache.ambari.server.state.ComponentInfo();
        info = new org.apache.ambari.server.state.ComponentInfo();
        parentInfo.setCardinality(cardinality);
        org.junit.Assert.assertEquals("foo", resolveComponent(info, parentInfo).getModuleInfo().getCardinality());
        info.setCardinality(cardinality);
        parentInfo.setCardinality(null);
        org.junit.Assert.assertEquals(cardinality, resolveComponent(info, parentInfo).getModuleInfo().getCardinality());
        java.lang.String cardinality2 = "foo2";
        info.setCardinality(cardinality2);
        parentInfo.setCardinality(cardinality);
        org.junit.Assert.assertEquals(cardinality2, resolveComponent(info, parentInfo).getModuleInfo().getCardinality());
    }

    @org.junit.Test
    public void testResolve_TimelineAppId() {
        java.lang.String timelineAppId = "app";
        org.apache.ambari.server.state.ComponentInfo info = new org.apache.ambari.server.state.ComponentInfo();
        org.junit.Assert.assertEquals(null, resolveComponent(info, null).getModuleInfo().getTimelineAppid());
        org.apache.ambari.server.state.ComponentInfo parentInfo = new org.apache.ambari.server.state.ComponentInfo();
        info = new org.apache.ambari.server.state.ComponentInfo();
        parentInfo.setTimelineAppid(timelineAppId);
        org.junit.Assert.assertEquals(timelineAppId, resolveComponent(info, parentInfo).getModuleInfo().getTimelineAppid());
        info.setTimelineAppid(timelineAppId);
        parentInfo.setTimelineAppid(null);
        org.junit.Assert.assertEquals(timelineAppId, resolveComponent(info, parentInfo).getModuleInfo().getTimelineAppid());
        java.lang.String timelineAppId2 = "app2";
        info.setTimelineAppid(timelineAppId2);
        parentInfo.setTimelineAppid(timelineAppId);
        org.junit.Assert.assertEquals(timelineAppId2, resolveComponent(info, parentInfo).getModuleInfo().getTimelineAppid());
    }

    @org.junit.Test
    public void testResolve_AutoDeploy() {
        org.apache.ambari.server.state.AutoDeployInfo autoDeployInfo = new org.apache.ambari.server.state.AutoDeployInfo();
        autoDeployInfo.setEnabled(true);
        autoDeployInfo.setCoLocate("foo/bar");
        org.apache.ambari.server.state.ComponentInfo info = new org.apache.ambari.server.state.ComponentInfo();
        org.apache.ambari.server.state.ComponentInfo parentInfo = new org.apache.ambari.server.state.ComponentInfo();
        parentInfo.setAutoDeploy(autoDeployInfo);
        org.junit.Assert.assertEquals(autoDeployInfo, resolveComponent(info, parentInfo).getModuleInfo().getAutoDeploy());
        info.setAutoDeploy(autoDeployInfo);
        parentInfo.setAutoDeploy(null);
        org.junit.Assert.assertEquals(autoDeployInfo, resolveComponent(info, parentInfo).getModuleInfo().getAutoDeploy());
        org.apache.ambari.server.state.AutoDeployInfo autoDeployInfo2 = new org.apache.ambari.server.state.AutoDeployInfo();
        info.setAutoDeploy(autoDeployInfo);
        parentInfo.setAutoDeploy(autoDeployInfo2);
        org.junit.Assert.assertEquals(autoDeployInfo, resolveComponent(info, parentInfo).getModuleInfo().getAutoDeploy());
    }

    @org.junit.Test
    public void testResolve_Dependencies() {
        java.util.List<org.apache.ambari.server.state.DependencyInfo> dependencies = new java.util.ArrayList<>();
        org.apache.ambari.server.state.DependencyInfo dependency1 = new org.apache.ambari.server.state.DependencyInfo();
        dependency1.setName("service/one");
        org.apache.ambari.server.state.DependencyInfo dependency2 = new org.apache.ambari.server.state.DependencyInfo();
        dependency2.setName("service/two");
        dependencies.add(dependency1);
        dependencies.add(dependency2);
        org.apache.ambari.server.state.ComponentInfo info = new org.apache.ambari.server.state.ComponentInfo();
        org.apache.ambari.server.state.ComponentInfo parentInfo = new org.apache.ambari.server.state.ComponentInfo();
        parentInfo.setDependencies(dependencies);
        org.junit.Assert.assertEquals(dependencies, resolveComponent(info, parentInfo).getModuleInfo().getDependencies());
        info.setDependencies(dependencies);
        parentInfo.setDependencies(null);
        org.junit.Assert.assertEquals(dependencies, resolveComponent(info, parentInfo).getModuleInfo().getDependencies());
        java.util.List<org.apache.ambari.server.state.DependencyInfo> dependencies2 = new java.util.ArrayList<>();
        org.apache.ambari.server.state.DependencyInfo dependency3 = new org.apache.ambari.server.state.DependencyInfo();
        dependency3.setName("service/two");
        org.apache.ambari.server.state.DependencyInfo dependency4 = new org.apache.ambari.server.state.DependencyInfo();
        dependency4.setName("service/four");
        dependencies2.add(dependency3);
        dependencies2.add(dependency4);
        info.setDependencies(dependencies2);
        parentInfo.setDependencies(dependencies);
        java.util.List<org.apache.ambari.server.state.DependencyInfo> resolvedDependencies = resolveComponent(info, parentInfo).getModuleInfo().getDependencies();
        org.junit.Assert.assertEquals(3, resolvedDependencies.size());
        org.junit.Assert.assertTrue(resolvedDependencies.contains(dependency1));
        org.junit.Assert.assertTrue(resolvedDependencies.contains(dependency3));
        org.junit.Assert.assertTrue(resolvedDependencies.contains(dependency4));
    }

    @org.junit.Test
    public void testResolve_CustomCommands() throws java.lang.Exception {
        java.util.List<org.apache.ambari.server.state.CustomCommandDefinition> commands = new java.util.ArrayList<>();
        org.apache.ambari.server.state.CustomCommandDefinition command1 = new org.apache.ambari.server.state.CustomCommandDefinition();
        setPrivateField(command1, "name", "one");
        org.apache.ambari.server.state.CustomCommandDefinition command2 = new org.apache.ambari.server.state.CustomCommandDefinition();
        setPrivateField(command2, "name", "two");
        commands.add(command1);
        commands.add(command2);
        org.apache.ambari.server.state.ComponentInfo info = new org.apache.ambari.server.state.ComponentInfo();
        org.apache.ambari.server.state.ComponentInfo parentInfo = new org.apache.ambari.server.state.ComponentInfo();
        parentInfo.setCustomCommands(commands);
        org.junit.Assert.assertEquals(commands, resolveComponent(info, parentInfo).getModuleInfo().getCustomCommands());
        info.setCustomCommands(commands);
        parentInfo.setCustomCommands(null);
        org.junit.Assert.assertEquals(commands, resolveComponent(info, parentInfo).getModuleInfo().getCustomCommands());
        java.util.List<org.apache.ambari.server.state.CustomCommandDefinition> commands2 = new java.util.ArrayList<>();
        org.apache.ambari.server.state.CustomCommandDefinition command3 = new org.apache.ambari.server.state.CustomCommandDefinition();
        setPrivateField(command3, "name", "two");
        org.apache.ambari.server.state.CustomCommandDefinition command4 = new org.apache.ambari.server.state.CustomCommandDefinition();
        setPrivateField(command4, "name", "four");
        commands2.add(command3);
        commands2.add(command4);
        info.setCustomCommands(commands2);
        parentInfo.setCustomCommands(commands);
        java.util.List<org.apache.ambari.server.state.CustomCommandDefinition> resolvedCommands = resolveComponent(info, parentInfo).getModuleInfo().getCustomCommands();
        org.junit.Assert.assertEquals(3, resolvedCommands.size());
        org.junit.Assert.assertTrue(resolvedCommands.contains(command1));
        org.junit.Assert.assertTrue(resolvedCommands.contains(command3));
        org.junit.Assert.assertTrue(resolvedCommands.contains(command4));
    }

    @org.junit.Test
    public void testResolve_ConfigDependencies() {
        java.util.List<java.lang.String> dependencies = new java.util.ArrayList<>();
        java.lang.String dependency1 = "one";
        java.lang.String dependency2 = "two";
        dependencies.add(dependency1);
        dependencies.add(dependency2);
        org.apache.ambari.server.state.ComponentInfo info = new org.apache.ambari.server.state.ComponentInfo();
        org.apache.ambari.server.state.ComponentInfo parentInfo = new org.apache.ambari.server.state.ComponentInfo();
        parentInfo.setConfigDependencies(dependencies);
        org.junit.Assert.assertEquals(dependencies, resolveComponent(info, parentInfo).getModuleInfo().getConfigDependencies());
        info.setConfigDependencies(dependencies);
        parentInfo.setConfigDependencies(null);
        org.junit.Assert.assertEquals(dependencies, resolveComponent(info, parentInfo).getModuleInfo().getConfigDependencies());
        java.util.List<java.lang.String> dependencies2 = new java.util.ArrayList<>();
        java.lang.String dependency3 = "two";
        java.lang.String dependency4 = "four";
        dependencies2.add(dependency3);
        dependencies2.add(dependency4);
        info.setConfigDependencies(dependencies2);
        parentInfo.setConfigDependencies(dependencies);
        java.util.List<java.lang.String> resolvedDependencies = resolveComponent(info, parentInfo).getModuleInfo().getConfigDependencies();
        org.junit.Assert.assertEquals(2, resolvedDependencies.size());
        org.junit.Assert.assertTrue(resolvedDependencies.contains(dependency3));
        org.junit.Assert.assertTrue(resolvedDependencies.contains(dependency4));
    }

    @org.junit.Test
    public void testResolve_ClientToUpdateConfigs() {
        java.util.List<java.lang.String> clientsToUpdate = new java.util.ArrayList<>();
        java.lang.String client1 = "one";
        java.lang.String client2 = "two";
        clientsToUpdate.add(client1);
        clientsToUpdate.add(client2);
        org.apache.ambari.server.state.ComponentInfo info = new org.apache.ambari.server.state.ComponentInfo();
        org.apache.ambari.server.state.ComponentInfo parentInfo = new org.apache.ambari.server.state.ComponentInfo();
        parentInfo.setClientsToUpdateConfigs(clientsToUpdate);
        org.junit.Assert.assertEquals(clientsToUpdate, resolveComponent(info, parentInfo).getModuleInfo().getClientsToUpdateConfigs());
        info.setClientsToUpdateConfigs(clientsToUpdate);
        parentInfo.setClientsToUpdateConfigs(null);
        org.junit.Assert.assertEquals(clientsToUpdate, resolveComponent(info, parentInfo).getModuleInfo().getClientsToUpdateConfigs());
        java.util.List<java.lang.String> clientsToUpdate2 = new java.util.ArrayList<>();
        java.lang.String client3 = "two";
        java.lang.String client4 = "four";
        clientsToUpdate2.add(client3);
        clientsToUpdate2.add(client4);
        info.setClientsToUpdateConfigs(clientsToUpdate2);
        parentInfo.setClientsToUpdateConfigs(clientsToUpdate);
        java.util.List<java.lang.String> resolvedClientsToUpdate = resolveComponent(info, parentInfo).getModuleInfo().getClientsToUpdateConfigs();
        org.junit.Assert.assertEquals(2, resolvedClientsToUpdate.size());
        org.junit.Assert.assertTrue(resolvedClientsToUpdate.contains(client3));
        org.junit.Assert.assertTrue(resolvedClientsToUpdate.contains(client4));
    }

    @org.junit.Test
    public void testGetId() {
        org.apache.ambari.server.state.ComponentInfo info = new org.apache.ambari.server.state.ComponentInfo();
        info.setName("foo");
        org.apache.ambari.server.stack.ComponentModule component = new org.apache.ambari.server.stack.ComponentModule(info);
        org.junit.Assert.assertEquals("foo", component.getId());
    }

    @org.junit.Test
    public void testIsDeleted() {
        org.apache.ambari.server.state.ComponentInfo info = new org.apache.ambari.server.state.ComponentInfo();
        info.setName("foo");
        org.apache.ambari.server.stack.ComponentModule component = new org.apache.ambari.server.stack.ComponentModule(info);
        org.junit.Assert.assertFalse(component.isDeleted());
        info = new org.apache.ambari.server.state.ComponentInfo();
        info.setName("foo");
        info.setDeleted(true);
        component = new org.apache.ambari.server.stack.ComponentModule(info);
        org.junit.Assert.assertTrue(component.isDeleted());
    }

    @org.junit.Test
    public void testResolve_BulkCommandsDefinition() {
        org.apache.ambari.server.state.BulkCommandDefinition bulkCommandsDefinition = new org.apache.ambari.server.state.BulkCommandDefinition();
        org.apache.ambari.server.state.ComponentInfo info = new org.apache.ambari.server.state.ComponentInfo();
        org.apache.ambari.server.state.ComponentInfo parentInfo = new org.apache.ambari.server.state.ComponentInfo();
        parentInfo.setBulkCommands(bulkCommandsDefinition);
        org.junit.Assert.assertSame(bulkCommandsDefinition, resolveComponent(info, parentInfo).getModuleInfo().getBulkCommandDefinition());
        info.setBulkCommands(bulkCommandsDefinition);
        parentInfo.setBulkCommands(null);
        org.junit.Assert.assertSame(bulkCommandsDefinition, resolveComponent(info, parentInfo).getModuleInfo().getBulkCommandDefinition());
        org.apache.ambari.server.state.BulkCommandDefinition bulkCommandsDefinition2 = EasyMock.createNiceMock(org.apache.ambari.server.state.BulkCommandDefinition.class);
        info.setBulkCommands(bulkCommandsDefinition);
        parentInfo.setBulkCommands(bulkCommandsDefinition2);
        org.junit.Assert.assertSame(bulkCommandsDefinition, resolveComponent(info, parentInfo).getModuleInfo().getBulkCommandDefinition());
    }

    @org.junit.Test
    public void testResolve_DecommissionAllowedInheritance() {
        java.util.List<org.apache.ambari.server.state.ComponentInfo> components = createComponentInfo(2);
        org.apache.ambari.server.state.ComponentInfo info = components.get(0);
        org.apache.ambari.server.state.ComponentInfo parentInfo = components.get(1);
        parentInfo.setDecommissionAllowed("true");
        org.junit.Assert.assertSame("true", resolveComponent(info, parentInfo).getModuleInfo().getDecommissionAllowed());
    }

    @org.junit.Test
    public void testResolve_DecommissionAllowed() {
        java.util.List<org.apache.ambari.server.state.ComponentInfo> components = createComponentInfo(2);
        org.apache.ambari.server.state.ComponentInfo info = components.get(0);
        org.apache.ambari.server.state.ComponentInfo parentInfo = components.get(1);
        info.setDecommissionAllowed("false");
        org.junit.Assert.assertSame("false", resolveComponent(info, parentInfo).getModuleInfo().getDecommissionAllowed());
    }

    @org.junit.Test
    public void testResolve_DecommissionAllowedOverwrite() {
        java.util.List<org.apache.ambari.server.state.ComponentInfo> components = createComponentInfo(2);
        org.apache.ambari.server.state.ComponentInfo info = components.get(0);
        org.apache.ambari.server.state.ComponentInfo parentInfo = components.get(1);
        parentInfo.setDecommissionAllowed("false");
        info.setDecommissionAllowed("true");
        org.junit.Assert.assertSame("true", resolveComponent(info, parentInfo).getModuleInfo().getDecommissionAllowed());
    }

    @org.junit.Test
    public void testResolve_UnlimitedKeyJCERequiredInheritance() {
        java.util.List<org.apache.ambari.server.state.ComponentInfo> components = createComponentInfo(2);
        org.apache.ambari.server.state.ComponentInfo info = components.get(0);
        org.apache.ambari.server.state.ComponentInfo parentInfo = components.get(1);
        parentInfo.setUnlimitedKeyJCERequired(org.apache.ambari.server.state.UnlimitedKeyJCERequirement.ALWAYS);
        org.junit.Assert.assertSame(org.apache.ambari.server.state.UnlimitedKeyJCERequirement.ALWAYS, resolveComponent(info, parentInfo).getModuleInfo().getUnlimitedKeyJCERequired());
    }

    @org.junit.Test
    public void testResolve_UnlimitedKeyJCERequired() {
        java.util.List<org.apache.ambari.server.state.ComponentInfo> components = createComponentInfo(2);
        org.apache.ambari.server.state.ComponentInfo info = components.get(0);
        org.apache.ambari.server.state.ComponentInfo parentInfo = components.get(1);
        info.setUnlimitedKeyJCERequired(org.apache.ambari.server.state.UnlimitedKeyJCERequirement.NEVER);
        org.junit.Assert.assertSame(org.apache.ambari.server.state.UnlimitedKeyJCERequirement.NEVER, resolveComponent(info, parentInfo).getModuleInfo().getUnlimitedKeyJCERequired());
    }

    @org.junit.Test
    public void testResolve_UnlimitedKeyJCERequiredOverwrite() {
        java.util.List<org.apache.ambari.server.state.ComponentInfo> components = createComponentInfo(2);
        org.apache.ambari.server.state.ComponentInfo info = components.get(0);
        org.apache.ambari.server.state.ComponentInfo parentInfo = components.get(1);
        parentInfo.setUnlimitedKeyJCERequired(org.apache.ambari.server.state.UnlimitedKeyJCERequirement.KERBEROS_ENABLED);
        info.setUnlimitedKeyJCERequired(org.apache.ambari.server.state.UnlimitedKeyJCERequirement.ALWAYS);
        org.junit.Assert.assertSame(org.apache.ambari.server.state.UnlimitedKeyJCERequirement.ALWAYS, resolveComponent(info, parentInfo).getModuleInfo().getUnlimitedKeyJCERequired());
    }

    @org.junit.Test
    public void testResolve_Reassignable() {
        java.util.List<org.apache.ambari.server.state.ComponentInfo> components = createComponentInfo(2);
        org.apache.ambari.server.state.ComponentInfo info = components.get(0);
        org.apache.ambari.server.state.ComponentInfo parentInfo = components.get(1);
        info.setReassignAllowed("false");
        org.junit.Assert.assertSame("false", resolveComponent(info, parentInfo).getModuleInfo().getReassignAllowed());
    }

    @org.junit.Test
    public void testResolve_ReassignableInheritance() {
        java.util.List<org.apache.ambari.server.state.ComponentInfo> components = createComponentInfo(2);
        org.apache.ambari.server.state.ComponentInfo info = components.get(0);
        org.apache.ambari.server.state.ComponentInfo parentInfo = components.get(1);
        parentInfo.setReassignAllowed("true");
        org.junit.Assert.assertSame("true", resolveComponent(info, parentInfo).getModuleInfo().getReassignAllowed());
    }

    @org.junit.Test
    public void testResolve_ReassignableOverwrite() {
        java.util.List<org.apache.ambari.server.state.ComponentInfo> components = createComponentInfo(2);
        org.apache.ambari.server.state.ComponentInfo info = components.get(0);
        org.apache.ambari.server.state.ComponentInfo parentInfo = components.get(1);
        parentInfo.setReassignAllowed("false");
        info.setReassignAllowed("true");
        org.junit.Assert.assertSame("true", resolveComponent(info, parentInfo).getModuleInfo().getReassignAllowed());
    }

    @org.junit.Test
    public void testResolve_VersionAdvertised() {
        java.util.List<org.apache.ambari.server.state.ComponentInfo> components = createComponentInfo(2);
        org.apache.ambari.server.state.ComponentInfo info = components.get(0);
        org.apache.ambari.server.state.ComponentInfo parentInfo = components.get(1);
        parentInfo.setVersionAdvertisedField(new java.lang.Boolean(true));
        parentInfo.setVersionAdvertised(true);
        info.setVersionAdvertisedField(new java.lang.Boolean(true));
        org.junit.Assert.assertEquals(true, resolveComponent(info, parentInfo).getModuleInfo().isVersionAdvertised());
        parentInfo.setVersionAdvertisedField(new java.lang.Boolean(true));
        parentInfo.setVersionAdvertised(true);
        info.setVersionAdvertisedField(new java.lang.Boolean(false));
        org.junit.Assert.assertEquals(false, resolveComponent(info, parentInfo).getModuleInfo().isVersionAdvertised());
        parentInfo.setVersionAdvertisedField(new java.lang.Boolean(false));
        parentInfo.setVersionAdvertised(false);
        info.setVersionAdvertisedField(new java.lang.Boolean(true));
        org.junit.Assert.assertEquals(true, resolveComponent(info, parentInfo).getModuleInfo().isVersionAdvertised());
        parentInfo.setVersionAdvertisedField(null);
        parentInfo.setVersionAdvertised(false);
        info.setVersionAdvertisedField(new java.lang.Boolean(true));
        org.junit.Assert.assertEquals(true, resolveComponent(info, parentInfo).getModuleInfo().isVersionAdvertised());
        parentInfo.setVersionAdvertisedField(new java.lang.Boolean(true));
        parentInfo.setVersionAdvertised(true);
        info.setVersionAdvertisedField(null);
        org.junit.Assert.assertEquals(true, resolveComponent(info, parentInfo).getModuleInfo().isVersionAdvertised());
        parentInfo.setVersionAdvertisedField(new java.lang.Boolean(true));
        parentInfo.setVersionAdvertised(true);
        info.setVersionAdvertisedField(null);
        org.junit.Assert.assertEquals(true, resolveComponent(info, parentInfo).getModuleInfo().isVersionAdvertised());
        parentInfo.setVersionAdvertisedField(new java.lang.Boolean(false));
        parentInfo.setVersionAdvertised(false);
        info.setVersionAdvertisedField(null);
        org.junit.Assert.assertEquals(false, resolveComponent(info, parentInfo).getModuleInfo().isVersionAdvertised());
        parentInfo.setVersionAdvertisedField(new java.lang.Boolean(false));
        parentInfo.setVersionAdvertised(false);
        info.setVersionAdvertisedField(null);
        org.junit.Assert.assertEquals(false, resolveComponent(info, parentInfo).getModuleInfo().isVersionAdvertised());
    }

    private java.util.List<org.apache.ambari.server.state.ComponentInfo> createComponentInfo(int count) {
        java.util.List<org.apache.ambari.server.state.ComponentInfo> result = new java.util.ArrayList<>();
        if (count > 0) {
            for (int i = 0; i < count; i++) {
                result.add(new org.apache.ambari.server.state.ComponentInfo());
            }
        }
        return result;
    }

    private org.apache.ambari.server.stack.ComponentModule resolveComponent(org.apache.ambari.server.state.ComponentInfo info, org.apache.ambari.server.state.ComponentInfo parentInfo) {
        info.setName("FOO");
        org.apache.ambari.server.stack.ComponentModule component = new org.apache.ambari.server.stack.ComponentModule(info);
        org.apache.ambari.server.stack.ComponentModule parentComponent = null;
        if (parentInfo != null) {
            parentInfo.setName("FOO");
            parentComponent = new org.apache.ambari.server.stack.ComponentModule(parentInfo);
        }
        component.resolve(parentComponent, java.util.Collections.emptyMap(), java.util.Collections.emptyMap(), java.util.Collections.emptyMap());
        return component;
    }

    private void setPrivateField(java.lang.Object o, java.lang.String field, java.lang.Object value) throws java.lang.Exception {
        java.lang.Class<?> c = o.getClass();
        java.lang.reflect.Field f = c.getDeclaredField(field);
        f.setAccessible(true);
        f.set(o, value);
    }
}