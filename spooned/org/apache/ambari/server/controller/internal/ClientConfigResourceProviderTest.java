package org.apache.ambari.server.controller.internal;
import org.easymock.EasyMock;
import org.powermock.api.easymock.PowerMock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import static org.easymock.EasyMock.anyObject;
import static org.easymock.EasyMock.anyString;
import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.createNiceMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.powermock.api.mockito.PowerMockito.whenNew;
@org.junit.runner.RunWith(org.powermock.modules.junit4.PowerMockRunner.class)
@org.powermock.core.classloader.annotations.PrepareForTest({ org.apache.ambari.server.controller.internal.ClientConfigResourceProvider.class, org.apache.ambari.server.utils.StageUtils.class })
public class ClientConfigResourceProviderTest {
    @org.junit.After
    public void clearAuthentication() {
        org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(null);
    }

    @org.junit.Test
    public void testCreateResources() throws java.lang.Exception {
        org.apache.ambari.server.controller.spi.Resource.Type type = org.apache.ambari.server.controller.spi.Resource.Type.ClientConfig;
        org.apache.ambari.server.controller.AmbariManagementController managementController = EasyMock.createMock(org.apache.ambari.server.controller.AmbariManagementController.class);
        org.apache.ambari.server.controller.RequestStatusResponse response = EasyMock.createNiceMock(org.apache.ambari.server.controller.RequestStatusResponse.class);
        EasyMock.replay(managementController, response);
        org.apache.ambari.server.controller.spi.ResourceProvider provider = org.apache.ambari.server.controller.internal.AbstractControllerResourceProvider.getResourceProvider(type, managementController);
        java.util.Set<java.util.Map<java.lang.String, java.lang.Object>> propertySet = new java.util.LinkedHashSet<>();
        java.util.Map<java.lang.String, java.lang.Object> properties = new java.util.LinkedHashMap<>();
        properties.put(org.apache.ambari.server.controller.internal.ClientConfigResourceProvider.COMPONENT_CLUSTER_NAME_PROPERTY_ID, "c1");
        properties.put(org.apache.ambari.server.controller.internal.ClientConfigResourceProvider.COMPONENT_COMPONENT_NAME_PROPERTY_ID, "HDFS_CLIENT");
        properties.put(org.apache.ambari.server.controller.internal.ClientConfigResourceProvider.COMPONENT_SERVICE_NAME_PROPERTY_ID, "HDFS");
        propertySet.add(properties);
        org.apache.ambari.server.controller.spi.Request request = org.apache.ambari.server.controller.utilities.PropertyHelper.getCreateRequest(propertySet, null);
        try {
            provider.createResources(request);
            org.junit.Assert.fail("Expected an UnsupportedOperationException");
        } catch (org.apache.ambari.server.controller.spi.SystemException e) {
        }
        EasyMock.verify(managementController, response);
    }

    @org.junit.Test
    public void testUpdateResources() throws java.lang.Exception {
        org.apache.ambari.server.controller.spi.Resource.Type type = org.apache.ambari.server.controller.spi.Resource.Type.ClientConfig;
        org.apache.ambari.server.controller.AmbariManagementController managementController = EasyMock.createMock(org.apache.ambari.server.controller.AmbariManagementController.class);
        org.apache.ambari.server.controller.RequestStatusResponse response = EasyMock.createNiceMock(org.apache.ambari.server.controller.RequestStatusResponse.class);
        EasyMock.replay(managementController, response);
        org.apache.ambari.server.controller.spi.ResourceProvider provider = org.apache.ambari.server.controller.internal.AbstractControllerResourceProvider.getResourceProvider(type, managementController);
        java.util.Map<java.lang.String, java.lang.Object> properties = new java.util.LinkedHashMap<>();
        org.apache.ambari.server.controller.spi.Request request = org.apache.ambari.server.controller.utilities.PropertyHelper.getUpdateRequest(properties, null);
        org.apache.ambari.server.controller.spi.Predicate predicate = new org.apache.ambari.server.controller.utilities.PredicateBuilder().property(org.apache.ambari.server.controller.internal.ClientConfigResourceProvider.COMPONENT_CLUSTER_NAME_PROPERTY_ID).equals("c1").toPredicate();
        try {
            provider.updateResources(request, predicate);
            org.junit.Assert.fail("Expected an UnsupportedOperationException");
        } catch (org.apache.ambari.server.controller.spi.SystemException e) {
        }
        EasyMock.verify(managementController, response);
    }

    @org.junit.Test
    public void testGetResourcesForAdministrator() throws java.lang.Exception {
        testGetResources(org.apache.ambari.server.security.TestAuthenticationFactory.createAdministrator());
    }

    @org.junit.Test
    public void testGetResourcesForClusterAdministrator() throws java.lang.Exception {
        testGetResources(org.apache.ambari.server.security.TestAuthenticationFactory.createClusterAdministrator());
    }

    @org.junit.Test
    public void testGetResourcesForClusterOperator() throws java.lang.Exception {
        testGetResources(org.apache.ambari.server.security.TestAuthenticationFactory.createClusterOperator());
    }

    @org.junit.Test
    public void testGetResourcesForServiceAdministrator() throws java.lang.Exception {
        testGetResources(org.apache.ambari.server.security.TestAuthenticationFactory.createServiceAdministrator());
    }

    @org.junit.Test
    public void testGetResourcesForServiceOperator() throws java.lang.Exception {
        testGetResources(org.apache.ambari.server.security.TestAuthenticationFactory.createServiceOperator());
    }

    @org.junit.Test
    public void testGetResourcesForClusterUser() throws java.lang.Exception {
        testGetResources(org.apache.ambari.server.security.TestAuthenticationFactory.createClusterUser());
    }

    @org.junit.Test(expected = org.apache.ambari.server.security.authorization.AuthorizationException.class)
    public void testGetResourcesForNoRoleUser() throws java.lang.Exception {
        testGetResources(org.apache.ambari.server.security.TestAuthenticationFactory.createNoRoleUser());
    }

    @org.junit.Test
    public void testGetResourcesFromCommonServicesForAdministrator() throws java.lang.Exception {
        testGetResourcesFromCommonServices(org.apache.ambari.server.security.TestAuthenticationFactory.createAdministrator());
    }

    @org.junit.Test
    public void testGetResourcesFromCommonServicesForClusterAdministrator() throws java.lang.Exception {
        testGetResourcesFromCommonServices(org.apache.ambari.server.security.TestAuthenticationFactory.createClusterAdministrator());
    }

    @org.junit.Test
    public void testGetResourcesFromCommonServicesForClusterOperator() throws java.lang.Exception {
        testGetResourcesFromCommonServices(org.apache.ambari.server.security.TestAuthenticationFactory.createClusterOperator());
    }

    @org.junit.Test
    public void testGetResourcesFromCommonServicesForServiceAdministrator() throws java.lang.Exception {
        testGetResourcesFromCommonServices(org.apache.ambari.server.security.TestAuthenticationFactory.createServiceAdministrator());
    }

    @org.junit.Test
    public void testGetResourcesFromCommonServicesForServiceOperator() throws java.lang.Exception {
        testGetResourcesFromCommonServices(org.apache.ambari.server.security.TestAuthenticationFactory.createServiceOperator());
    }

    @org.junit.Test
    public void testGetResourcesFromCommonServicesForClusterUser() throws java.lang.Exception {
        testGetResourcesFromCommonServices(org.apache.ambari.server.security.TestAuthenticationFactory.createClusterUser());
    }

    @org.junit.Test(expected = org.apache.ambari.server.security.authorization.AuthorizationException.class)
    public void testGetResourcesFromCommonServicesForNoRoleUser() throws java.lang.Exception {
        testGetResourcesFromCommonServices(org.apache.ambari.server.security.TestAuthenticationFactory.createNoRoleUser());
    }

    @org.junit.Test
    public void testDeleteResources() throws java.lang.Exception {
        org.apache.ambari.server.controller.spi.Resource.Type type = org.apache.ambari.server.controller.spi.Resource.Type.ClientConfig;
        org.apache.ambari.server.controller.AmbariManagementController managementController = EasyMock.createMock(org.apache.ambari.server.controller.AmbariManagementController.class);
        EasyMock.replay(managementController);
        org.apache.ambari.server.controller.spi.ResourceProvider provider = org.apache.ambari.server.controller.internal.AbstractControllerResourceProvider.getResourceProvider(type, managementController);
        org.apache.ambari.server.controller.spi.Predicate predicate = new org.apache.ambari.server.controller.utilities.PredicateBuilder().property(org.apache.ambari.server.controller.internal.ClientConfigResourceProvider.COMPONENT_COMPONENT_NAME_PROPERTY_ID).equals("HDFS_CLIENT").toPredicate();
        try {
            provider.deleteResources(new org.apache.ambari.server.controller.internal.RequestImpl(null, null, null, null), predicate);
            org.junit.Assert.fail("Expected an UnsupportedOperationException");
        } catch (org.apache.ambari.server.controller.spi.SystemException e) {
        }
        EasyMock.verify(managementController);
    }

    private void testGetResources(org.springframework.security.core.Authentication authentication) throws java.lang.Exception {
        org.apache.ambari.server.controller.spi.Resource.Type type = org.apache.ambari.server.controller.spi.Resource.Type.ClientConfig;
        org.apache.ambari.server.controller.AmbariManagementController managementController = EasyMock.createNiceMock(org.apache.ambari.server.controller.AmbariManagementController.class);
        org.apache.ambari.server.state.Clusters clusters = EasyMock.createNiceMock(org.apache.ambari.server.state.Clusters.class);
        org.apache.ambari.server.state.Cluster cluster = EasyMock.createNiceMock(org.apache.ambari.server.state.Cluster.class);
        org.apache.ambari.server.api.services.AmbariMetaInfo ambariMetaInfo = EasyMock.createNiceMock(org.apache.ambari.server.api.services.AmbariMetaInfo.class);
        org.apache.ambari.server.state.StackId stackId = EasyMock.createNiceMock(org.apache.ambari.server.state.StackId.class);
        org.apache.ambari.server.state.ComponentInfo componentInfo = EasyMock.createNiceMock(org.apache.ambari.server.state.ComponentInfo.class);
        org.apache.ambari.server.state.ServiceInfo serviceInfo = EasyMock.createNiceMock(org.apache.ambari.server.state.ServiceInfo.class);
        org.apache.ambari.server.state.CommandScriptDefinition commandScriptDefinition = EasyMock.createNiceMock(org.apache.ambari.server.state.CommandScriptDefinition.class);
        org.apache.ambari.server.state.Config clusterConfig = EasyMock.createNiceMock(org.apache.ambari.server.state.Config.class);
        org.apache.ambari.server.state.DesiredConfig desiredConfig = EasyMock.createNiceMock(org.apache.ambari.server.state.DesiredConfig.class);
        org.apache.ambari.server.state.Host host = EasyMock.createNiceMock(org.apache.ambari.server.state.Host.class);
        org.apache.ambari.server.state.Service service = EasyMock.createNiceMock(org.apache.ambari.server.state.Service.class);
        org.apache.ambari.server.state.ServiceComponent serviceComponent = EasyMock.createNiceMock(org.apache.ambari.server.state.ServiceComponent.class);
        org.apache.ambari.server.state.ServiceComponentHost serviceComponentHost = EasyMock.createNiceMock(org.apache.ambari.server.state.ServiceComponentHost.class);
        org.apache.ambari.server.state.ConfigHelper configHelper = EasyMock.createNiceMock(org.apache.ambari.server.state.ConfigHelper.class);
        org.apache.ambari.server.configuration.Configuration configuration = org.powermock.api.easymock.PowerMock.createStrictMockAndExpectNew(org.apache.ambari.server.configuration.Configuration.class);
        java.io.File newFile = java.io.File.createTempFile("config", ".json", new java.io.File("/tmp/"));
        newFile.deleteOnExit();
        java.lang.Runtime runtime = EasyMock.createMock(java.lang.Runtime.class);
        java.lang.Process process = EasyMock.createNiceMock(java.lang.Process.class);
        java.util.Map<java.lang.String, org.apache.ambari.server.state.DesiredConfig> desiredConfigMap = new java.util.HashMap<>();
        desiredConfigMap.put("hive-site", desiredConfig);
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> allConfigTags = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> properties = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> configTags = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>>> attributes = new java.util.HashMap<>();
        org.apache.ambari.server.state.ClientConfigFileDefinition clientConfigFileDefinition = new org.apache.ambari.server.state.ClientConfigFileDefinition();
        clientConfigFileDefinition.setDictionaryName("pig-env");
        clientConfigFileDefinition.setFileName("pig-env.sh");
        clientConfigFileDefinition.setType("env");
        java.util.List<org.apache.ambari.server.state.ClientConfigFileDefinition> clientConfigFileDefinitionList = new java.util.LinkedList<>();
        clientConfigFileDefinitionList.add(clientConfigFileDefinition);
        org.apache.ambari.server.controller.spi.ResourceProvider provider = org.apache.ambari.server.controller.internal.AbstractControllerResourceProvider.getResourceProvider(type, managementController);
        java.lang.String clusterName = "C1";
        java.lang.String serviceName = "PIG";
        java.lang.String componentName = "PIG";
        java.lang.String displayName = "Pig Client";
        java.lang.String hostName = "Host100";
        java.lang.String publicHostname = "Host100";
        java.lang.String desiredState = "INSTALLED";
        java.lang.String stackName = "S1";
        java.lang.String stackVersion = "V1";
        java.lang.String stackRoot = "/tmp/stacks/S1/V1";
        java.lang.String packageFolder = "PIG/package";
        if (java.lang.System.getProperty("os.name").contains("Windows")) {
            stackRoot = "C:\\tmp\\stacks\\S1\\V1";
            packageFolder = "PIG\\package";
        }
        org.apache.ambari.server.controller.ServiceComponentHostResponse shr1 = new org.apache.ambari.server.controller.ServiceComponentHostResponse(clusterName, serviceName, componentName, displayName, hostName, publicHostname, desiredState, "", null, null, null, null);
        java.util.Set<org.apache.ambari.server.controller.ServiceComponentHostResponse> responses = new java.util.LinkedHashSet<>();
        responses.add(shr1);
        java.util.Map<java.lang.String, java.lang.String> returnConfigMap = new java.util.HashMap<>();
        returnConfigMap.put(org.apache.ambari.server.configuration.Configuration.SERVER_TMP_DIR.getKey(), org.apache.ambari.server.configuration.Configuration.SERVER_TMP_DIR.getDefaultValue());
        returnConfigMap.put(org.apache.ambari.server.configuration.Configuration.AMBARI_PYTHON_WRAP.getKey(), org.apache.ambari.server.configuration.Configuration.AMBARI_PYTHON_WRAP.getDefaultValue());
        EasyMock.expect(managementController.getConfigHelper()).andReturn(configHelper);
        EasyMock.expect(managementController.getAmbariMetaInfo()).andReturn(ambariMetaInfo).anyTimes();
        EasyMock.expect(managementController.getClusters()).andReturn(clusters).anyTimes();
        EasyMock.expect(clusters.getCluster(clusterName)).andReturn(cluster).anyTimes();
        EasyMock.expect(configHelper.getEffectiveConfigProperties(cluster, configTags)).andReturn(properties);
        EasyMock.expect(configHelper.getEffectiveConfigAttributes(cluster, configTags)).andReturn(attributes);
        EasyMock.expect(configuration.getConfigsMap()).andReturn(returnConfigMap);
        EasyMock.expect(configuration.getResourceDirPath()).andReturn(stackRoot);
        EasyMock.expect(configuration.getExternalScriptThreadPoolSize()).andReturn(org.apache.ambari.server.configuration.Configuration.THREAD_POOL_SIZE_FOR_EXTERNAL_SCRIPT.getDefaultValue());
        EasyMock.expect(configuration.getExternalScriptTimeout()).andReturn(org.apache.ambari.server.configuration.Configuration.EXTERNAL_SCRIPT_TIMEOUT.getDefaultValue());
        java.util.Map<java.lang.String, java.lang.String> props = new java.util.HashMap<>();
        props.put("key", "value");
        EasyMock.expect(clusterConfig.getProperties()).andReturn(props);
        EasyMock.expect(configHelper.getEffectiveDesiredTags(cluster, null)).andReturn(allConfigTags);
        EasyMock.expect(cluster.getClusterName()).andReturn(clusterName);
        EasyMock.expect(managementController.getHostComponents(org.easymock.EasyMock.anyObject())).andReturn(responses).anyTimes();
        org.powermock.api.easymock.PowerMock.mockStaticPartial(org.apache.ambari.server.utils.StageUtils.class, "getClusterHostInfo");
        java.util.Map<java.lang.String, java.util.Set<java.lang.String>> clusterHostInfo = new java.util.HashMap<>();
        java.util.Set<java.lang.String> all_hosts = new java.util.HashSet<>(java.util.Arrays.asList("Host100", "Host101", "Host102"));
        java.util.Set<java.lang.String> some_hosts = new java.util.HashSet<>(java.util.Arrays.asList("0-1", "2"));
        java.util.Set<java.lang.String> ohter_hosts = java.util.Collections.singleton("0,1");
        java.util.Set<java.lang.String> clusterHostTypes = new java.util.HashSet<>(java.util.Arrays.asList("nm_hosts", "hs_host", "namenode_host", "rm_host", "snamenode_host", "slave_hosts", "zookeeper_hosts"));
        for (java.lang.String hostTypes : clusterHostTypes) {
            if (hostTypes.equals("slave_hosts")) {
                clusterHostInfo.put(hostTypes, ohter_hosts);
            } else {
                clusterHostInfo.put(hostTypes, some_hosts);
            }
        }
        clusterHostInfo.put("all_hosts", all_hosts);
        EasyMock.expect(org.apache.ambari.server.utils.StageUtils.getClusterHostInfo(cluster)).andReturn(clusterHostInfo);
        EasyMock.expect(stackId.getStackName()).andReturn(stackName).anyTimes();
        EasyMock.expect(stackId.getStackVersion()).andReturn(stackVersion).anyTimes();
        EasyMock.expect(ambariMetaInfo.getComponent(stackName, stackVersion, serviceName, componentName)).andReturn(componentInfo);
        EasyMock.expect(ambariMetaInfo.getService(stackName, stackVersion, serviceName)).andReturn(serviceInfo);
        EasyMock.expect(serviceInfo.getServicePackageFolder()).andReturn(packageFolder);
        EasyMock.expect(ambariMetaInfo.getComponent(EasyMock.anyString(), EasyMock.anyString(), EasyMock.anyString(), EasyMock.anyString())).andReturn(componentInfo).anyTimes();
        EasyMock.expect(componentInfo.getCommandScript()).andReturn(commandScriptDefinition);
        EasyMock.expect(componentInfo.getClientConfigFiles()).andReturn(clientConfigFileDefinitionList);
        EasyMock.expect(cluster.getConfig("hive-site", null)).andReturn(clusterConfig);
        EasyMock.expect(clusterConfig.getType()).andReturn("hive-site").anyTimes();
        EasyMock.expect(cluster.getDesiredConfigs()).andReturn(desiredConfigMap);
        EasyMock.expect(clusters.getHost(hostName)).andReturn(host);
        EasyMock.expect(cluster.getService(serviceName)).andReturn(service).atLeastOnce();
        EasyMock.expect(service.getServiceComponent(componentName)).andReturn(serviceComponent).atLeastOnce();
        EasyMock.expect(serviceComponent.getDesiredStackId()).andReturn(stackId).atLeastOnce();
        java.util.HashMap<java.lang.String, java.lang.String> rcaParams = new java.util.HashMap<>();
        rcaParams.put("key", "value");
        EasyMock.expect(managementController.getRcaParameters()).andReturn(rcaParams).anyTimes();
        EasyMock.expect(ambariMetaInfo.getService(stackName, stackVersion, serviceName)).andReturn(serviceInfo);
        EasyMock.expect(serviceInfo.getOsSpecifics()).andReturn(new java.util.HashMap<>()).anyTimes();
        java.util.Map<org.apache.ambari.server.state.PropertyInfo, java.lang.String> userProperties = new java.util.HashMap<>();
        java.util.Map<org.apache.ambari.server.state.PropertyInfo, java.lang.String> groupProperties = new java.util.HashMap<>();
        org.apache.ambari.server.state.PropertyInfo userProperty = new org.apache.ambari.server.state.PropertyInfo();
        userProperty.setFilename("hadoop-env.xml");
        userProperty.setName("hdfs-user");
        userProperty.setValue("hdfsUser");
        org.apache.ambari.server.state.PropertyInfo groupProperty = new org.apache.ambari.server.state.PropertyInfo();
        groupProperty.setFilename("hadoop-env.xml");
        groupProperty.setName("hdfs-group");
        groupProperty.setValue("hdfsGroup");
        org.apache.ambari.server.state.ValueAttributesInfo valueAttributesInfo = new org.apache.ambari.server.state.ValueAttributesInfo();
        valueAttributesInfo.setType("user");
        java.util.Set<org.apache.ambari.server.state.UserGroupInfo> userGroupEntries = new java.util.HashSet<>();
        org.apache.ambari.server.state.UserGroupInfo userGroupInfo = new org.apache.ambari.server.state.UserGroupInfo();
        userGroupInfo.setType("hadoop-env");
        userGroupInfo.setName("hdfs-group");
        userGroupEntries.add(userGroupInfo);
        valueAttributesInfo.setUserGroupEntries(userGroupEntries);
        userProperty.setPropertyValueAttributes(valueAttributesInfo);
        userProperties.put(userProperty, "hdfsUser");
        groupProperties.put(groupProperty, "hdfsGroup");
        java.util.Map<java.lang.String, java.util.Set<java.lang.String>> userGroupsMap = new java.util.HashMap<>();
        userGroupsMap.put("hdfsUser", java.util.Collections.singleton("hdfsGroup"));
        EasyMock.expect(configHelper.getPropertiesWithPropertyType(stackId, org.apache.ambari.server.state.PropertyInfo.PropertyType.USER, cluster, desiredConfigMap)).andReturn(userProperties).anyTimes();
        EasyMock.expect(configHelper.getPropertiesWithPropertyType(stackId, org.apache.ambari.server.state.PropertyInfo.PropertyType.GROUP, cluster, desiredConfigMap)).andReturn(groupProperties).anyTimes();
        EasyMock.expect(configHelper.createUserGroupsMap(stackId, cluster, desiredConfigMap)).andReturn(userGroupsMap).anyTimes();
        org.powermock.api.easymock.PowerMock.expectNew(java.io.File.class, new java.lang.Class<?>[]{ java.lang.String.class }, EasyMock.anyObject(java.lang.String.class)).andReturn(newFile).anyTimes();
        org.powermock.api.easymock.PowerMock.mockStatic(java.io.File.class);
        EasyMock.expect(java.io.File.createTempFile(EasyMock.anyString(), EasyMock.anyString(), EasyMock.anyObject(java.io.File.class))).andReturn(newFile);
        java.lang.String commandLine = (("ambari-python-wrap /tmp/stacks/S1/V1/PIG/package/null generate_configs " + newFile) + " /tmp/stacks/S1/V1/PIG/package /var/lib/ambari-server/tmp/structured-out.json ") + "INFO /var/lib/ambari-server/tmp";
        if (java.lang.System.getProperty("os.name").contains("Windows")) {
            commandLine = (((("ambari-python-wrap " + stackRoot) + "\\PIG\\package\\null generate_configs null ") + stackRoot) + "\\PIG\\package /var/lib/ambari-server/tmp\\structured-out.json ") + "INFO /var/lib/ambari-server/tmp";
        }
        java.lang.ProcessBuilder processBuilder = org.powermock.api.easymock.PowerMock.createNiceMock(java.lang.ProcessBuilder.class);
        org.powermock.api.easymock.PowerMock.expectNew(java.lang.ProcessBuilder.class, java.util.Arrays.asList(commandLine.split("\\s+"))).andReturn(processBuilder).once();
        EasyMock.expect(processBuilder.start()).andReturn(process).once();
        java.io.InputStream inputStream = new java.io.ByteArrayInputStream("some logging info".getBytes());
        EasyMock.expect(process.getInputStream()).andReturn(inputStream);
        org.apache.ambari.server.controller.internal.ClientConfigResourceProvider.TarUtils tarUtilMock = org.powermock.api.mockito.PowerMockito.mock(org.apache.ambari.server.controller.internal.ClientConfigResourceProvider.TarUtils.class);
        whenNew(org.apache.ambari.server.controller.internal.ClientConfigResourceProvider.TarUtils.class).withAnyArguments().thenReturn(tarUtilMock);
        tarUtilMock.tarConfigFiles();
        EasyMock.expectLastCall().once();
        org.apache.ambari.server.controller.spi.Request request = org.apache.ambari.server.controller.utilities.PropertyHelper.getReadRequest(org.apache.ambari.server.controller.internal.ClientConfigResourceProvider.COMPONENT_CLUSTER_NAME_PROPERTY_ID, "c1", org.apache.ambari.server.controller.internal.ClientConfigResourceProvider.COMPONENT_COMPONENT_NAME_PROPERTY_ID, org.apache.ambari.server.controller.internal.ClientConfigResourceProvider.COMPONENT_SERVICE_NAME_PROPERTY_ID);
        org.apache.ambari.server.controller.spi.Predicate predicate = new org.apache.ambari.server.controller.utilities.PredicateBuilder().property(org.apache.ambari.server.controller.internal.ClientConfigResourceProvider.COMPONENT_CLUSTER_NAME_PROPERTY_ID).equals("c1").and().property(org.apache.ambari.server.controller.internal.ClientConfigResourceProvider.COMPONENT_SERVICE_NAME_PROPERTY_ID).equals("PIG").toPredicate();
        EasyMock.replay(managementController, clusters, cluster, ambariMetaInfo, stackId, componentInfo, commandScriptDefinition, clusterConfig, host, service, serviceComponent, serviceComponentHost, serviceInfo, configHelper, runtime, process);
        org.powermock.api.easymock.PowerMock.replayAll();
        org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(authentication);
        java.util.Set<org.apache.ambari.server.controller.spi.Resource> resources = provider.getResources(request, predicate);
        org.junit.Assert.assertFalse(resources.isEmpty());
        org.junit.Assert.assertFalse(newFile.exists());
        EasyMock.verify(managementController, clusters, cluster, ambariMetaInfo, stackId, componentInfo, commandScriptDefinition, clusterConfig, host, service, serviceComponent, serviceComponentHost, serviceInfo, configHelper, runtime, process);
        org.powermock.api.easymock.PowerMock.verifyAll();
    }

    private void testGetResourcesFromCommonServices(org.springframework.security.core.Authentication authentication) throws java.lang.Exception {
        org.apache.ambari.server.controller.spi.Resource.Type type = org.apache.ambari.server.controller.spi.Resource.Type.ClientConfig;
        org.apache.ambari.server.controller.AmbariManagementController managementController = EasyMock.createNiceMock(org.apache.ambari.server.controller.AmbariManagementController.class);
        org.apache.ambari.server.state.Clusters clusters = EasyMock.createNiceMock(org.apache.ambari.server.state.Clusters.class);
        org.apache.ambari.server.state.Cluster cluster = EasyMock.createNiceMock(org.apache.ambari.server.state.Cluster.class);
        org.apache.ambari.server.api.services.AmbariMetaInfo ambariMetaInfo = EasyMock.createNiceMock(org.apache.ambari.server.api.services.AmbariMetaInfo.class);
        org.apache.ambari.server.state.StackId stackId = EasyMock.createNiceMock(org.apache.ambari.server.state.StackId.class);
        org.apache.ambari.server.state.ComponentInfo componentInfo = EasyMock.createNiceMock(org.apache.ambari.server.state.ComponentInfo.class);
        org.apache.ambari.server.state.ServiceInfo serviceInfo = EasyMock.createNiceMock(org.apache.ambari.server.state.ServiceInfo.class);
        org.apache.ambari.server.state.CommandScriptDefinition commandScriptDefinition = EasyMock.createNiceMock(org.apache.ambari.server.state.CommandScriptDefinition.class);
        org.apache.ambari.server.state.Config clusterConfig = EasyMock.createNiceMock(org.apache.ambari.server.state.Config.class);
        org.apache.ambari.server.state.DesiredConfig desiredConfig = EasyMock.createNiceMock(org.apache.ambari.server.state.DesiredConfig.class);
        org.apache.ambari.server.state.Host host = EasyMock.createNiceMock(org.apache.ambari.server.state.Host.class);
        org.apache.ambari.server.state.Service service = EasyMock.createNiceMock(org.apache.ambari.server.state.Service.class);
        org.apache.ambari.server.state.ServiceComponent serviceComponent = EasyMock.createNiceMock(org.apache.ambari.server.state.ServiceComponent.class);
        org.apache.ambari.server.state.ServiceComponentHost serviceComponentHost = EasyMock.createNiceMock(org.apache.ambari.server.state.ServiceComponentHost.class);
        org.apache.ambari.server.state.ConfigHelper configHelper = EasyMock.createNiceMock(org.apache.ambari.server.state.ConfigHelper.class);
        org.apache.ambari.server.configuration.Configuration configuration = org.powermock.api.easymock.PowerMock.createStrictMockAndExpectNew(org.apache.ambari.server.configuration.Configuration.class);
        java.io.File mockFile = org.powermock.api.easymock.PowerMock.createNiceMock(java.io.File.class);
        java.lang.Runtime runtime = EasyMock.createMock(java.lang.Runtime.class);
        java.lang.Process process = EasyMock.createNiceMock(java.lang.Process.class);
        java.util.Map<java.lang.String, org.apache.ambari.server.state.DesiredConfig> desiredConfigMap = new java.util.HashMap<>();
        desiredConfigMap.put("hive-site", desiredConfig);
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> allConfigTags = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> properties = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> configTags = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>>> attributes = new java.util.HashMap<>();
        org.apache.ambari.server.state.ClientConfigFileDefinition clientConfigFileDefinition = new org.apache.ambari.server.state.ClientConfigFileDefinition();
        clientConfigFileDefinition.setDictionaryName("pig-env");
        clientConfigFileDefinition.setFileName("pig-env.sh");
        clientConfigFileDefinition.setType("env");
        java.util.List<org.apache.ambari.server.state.ClientConfigFileDefinition> clientConfigFileDefinitionList = new java.util.LinkedList<>();
        clientConfigFileDefinitionList.add(clientConfigFileDefinition);
        org.apache.ambari.server.controller.spi.ResourceProvider provider = org.apache.ambari.server.controller.internal.AbstractControllerResourceProvider.getResourceProvider(type, managementController);
        org.apache.ambari.server.controller.spi.Request request = org.apache.ambari.server.controller.utilities.PropertyHelper.getReadRequest(org.apache.ambari.server.controller.internal.ClientConfigResourceProvider.COMPONENT_CLUSTER_NAME_PROPERTY_ID, "c1", org.apache.ambari.server.controller.internal.ClientConfigResourceProvider.COMPONENT_COMPONENT_NAME_PROPERTY_ID, org.apache.ambari.server.controller.internal.ClientConfigResourceProvider.COMPONENT_SERVICE_NAME_PROPERTY_ID);
        org.apache.ambari.server.controller.spi.Predicate predicate = new org.apache.ambari.server.controller.utilities.PredicateBuilder().property(org.apache.ambari.server.controller.internal.ClientConfigResourceProvider.COMPONENT_CLUSTER_NAME_PROPERTY_ID).equals("c1").and().property(org.apache.ambari.server.controller.internal.ClientConfigResourceProvider.COMPONENT_COMPONENT_NAME_PROPERTY_ID).equals("PIG").and().property(org.apache.ambari.server.controller.internal.ClientConfigResourceProvider.COMPONENT_SERVICE_NAME_PROPERTY_ID).equals("PIG").toPredicate();
        java.lang.String clusterName = "C1";
        java.lang.String serviceName = "PIG";
        java.lang.String componentName = "PIG";
        java.lang.String displayName = "Pig Client";
        java.lang.String hostName = "Host100";
        java.lang.String publicHostName = "Host100";
        java.lang.String desiredState = "INSTALLED";
        java.lang.String stackName = "S1";
        java.lang.String stackVersion = "V1";
        java.lang.String packageFolder = org.apache.ambari.server.stack.StackManager.COMMON_SERVICES + "/PIG/package";
        java.lang.String commonServicesPath = ("/var/lib/ambari-server/src/main/resources" + java.io.File.separator) + "common-services";
        if (java.lang.System.getProperty("os.name").contains("Windows")) {
            packageFolder = org.apache.ambari.server.stack.StackManager.COMMON_SERVICES + "\\PIG\\package";
        }
        org.apache.ambari.server.controller.ServiceComponentHostResponse shr1 = new org.apache.ambari.server.controller.ServiceComponentHostResponse(clusterName, serviceName, componentName, displayName, hostName, publicHostName, desiredState, "", null, null, null, null);
        java.util.Set<org.apache.ambari.server.controller.ServiceComponentHostResponse> responses = new java.util.LinkedHashSet<>();
        responses.add(shr1);
        java.util.Map<java.lang.String, java.lang.String> returnConfigMap = new java.util.HashMap<>();
        returnConfigMap.put(org.apache.ambari.server.configuration.Configuration.SERVER_TMP_DIR.getKey(), org.apache.ambari.server.configuration.Configuration.SERVER_TMP_DIR.getDefaultValue());
        returnConfigMap.put(org.apache.ambari.server.configuration.Configuration.AMBARI_PYTHON_WRAP.getKey(), org.apache.ambari.server.configuration.Configuration.AMBARI_PYTHON_WRAP.getDefaultValue());
        EasyMock.expect(managementController.getConfigHelper()).andReturn(configHelper);
        EasyMock.expect(managementController.getAmbariMetaInfo()).andReturn(ambariMetaInfo).anyTimes();
        EasyMock.expect(managementController.getClusters()).andReturn(clusters).anyTimes();
        EasyMock.expect(clusters.getCluster(clusterName)).andReturn(cluster).anyTimes();
        EasyMock.expect(configHelper.getEffectiveConfigProperties(cluster, configTags)).andReturn(properties);
        EasyMock.expect(configHelper.getEffectiveConfigAttributes(cluster, configTags)).andReturn(attributes);
        EasyMock.expect(configuration.getConfigsMap()).andReturn(returnConfigMap);
        EasyMock.expect(configuration.getResourceDirPath()).andReturn("/var/lib/ambari-server/src/main/resources");
        EasyMock.expect(configuration.getExternalScriptThreadPoolSize()).andReturn(org.apache.ambari.server.configuration.Configuration.THREAD_POOL_SIZE_FOR_EXTERNAL_SCRIPT.getDefaultValue());
        EasyMock.expect(configuration.getExternalScriptTimeout()).andReturn(org.apache.ambari.server.configuration.Configuration.EXTERNAL_SCRIPT_TIMEOUT.getDefaultValue());
        java.util.Map<java.lang.String, java.lang.String> props = new java.util.HashMap<>();
        props.put("key", "value");
        EasyMock.expect(clusterConfig.getProperties()).andReturn(props);
        EasyMock.expect(configHelper.getEffectiveDesiredTags(cluster, null)).andReturn(allConfigTags);
        EasyMock.expect(cluster.getClusterName()).andReturn(clusterName);
        EasyMock.expect(managementController.getHostComponents(org.easymock.EasyMock.anyObject())).andReturn(responses).anyTimes();
        org.powermock.api.easymock.PowerMock.mockStaticPartial(org.apache.ambari.server.utils.StageUtils.class, "getClusterHostInfo");
        java.util.Map<java.lang.String, java.util.Set<java.lang.String>> clusterHostInfo = new java.util.HashMap<>();
        java.util.Set<java.lang.String> all_hosts = new java.util.HashSet<>(java.util.Arrays.asList("Host100", "Host101", "Host102"));
        java.util.Set<java.lang.String> some_hosts = new java.util.HashSet<>(java.util.Arrays.asList("0-1", "2"));
        java.util.Set<java.lang.String> ohter_hosts = java.util.Collections.singleton("0,1");
        java.util.Set<java.lang.String> clusterHostTypes = new java.util.HashSet<>(java.util.Arrays.asList("nm_hosts", "hs_host", "namenode_host", "rm_host", "snamenode_host", "slave_hosts", "zookeeper_hosts"));
        for (java.lang.String hostTypes : clusterHostTypes) {
            if (hostTypes.equals("slave_hosts")) {
                clusterHostInfo.put(hostTypes, ohter_hosts);
            } else {
                clusterHostInfo.put(hostTypes, some_hosts);
            }
        }
        clusterHostInfo.put("all_hosts", all_hosts);
        EasyMock.expect(org.apache.ambari.server.utils.StageUtils.getClusterHostInfo(cluster)).andReturn(clusterHostInfo);
        EasyMock.expect(stackId.getStackName()).andReturn(stackName).anyTimes();
        EasyMock.expect(stackId.getStackVersion()).andReturn(stackVersion).anyTimes();
        EasyMock.expect(ambariMetaInfo.getComponent(stackName, stackVersion, serviceName, componentName)).andReturn(componentInfo);
        EasyMock.expect(ambariMetaInfo.getService(stackName, stackVersion, serviceName)).andReturn(serviceInfo);
        EasyMock.expect(serviceInfo.getServicePackageFolder()).andReturn(packageFolder);
        EasyMock.expect(ambariMetaInfo.getComponent(EasyMock.anyString(), EasyMock.anyString(), EasyMock.anyString(), EasyMock.anyString())).andReturn(componentInfo).anyTimes();
        EasyMock.expect(componentInfo.getCommandScript()).andReturn(commandScriptDefinition);
        EasyMock.expect(componentInfo.getClientConfigFiles()).andReturn(clientConfigFileDefinitionList);
        EasyMock.expect(cluster.getConfig("hive-site", null)).andReturn(clusterConfig);
        EasyMock.expect(clusterConfig.getType()).andReturn("hive-site").anyTimes();
        EasyMock.expect(cluster.getDesiredConfigs()).andReturn(desiredConfigMap);
        EasyMock.expect(clusters.getHost(hostName)).andReturn(host);
        EasyMock.expect(cluster.getService(serviceName)).andReturn(service).atLeastOnce();
        EasyMock.expect(service.getServiceComponent(componentName)).andReturn(serviceComponent).atLeastOnce();
        EasyMock.expect(serviceComponent.getDesiredStackId()).andReturn(stackId).atLeastOnce();
        java.util.HashMap<java.lang.String, java.lang.String> rcaParams = new java.util.HashMap<>();
        rcaParams.put("key", "value");
        EasyMock.expect(managementController.getRcaParameters()).andReturn(rcaParams).anyTimes();
        EasyMock.expect(ambariMetaInfo.getService(stackName, stackVersion, serviceName)).andReturn(serviceInfo);
        EasyMock.expect(serviceInfo.getOsSpecifics()).andReturn(new java.util.HashMap<>()).anyTimes();
        org.powermock.api.easymock.PowerMock.expectNew(java.io.File.class, new java.lang.Class<?>[]{ java.lang.String.class }, EasyMock.anyObject(java.lang.String.class)).andReturn(mockFile).anyTimes();
        org.powermock.api.easymock.PowerMock.mockStatic(java.io.File.class);
        EasyMock.expect(mockFile.exists()).andReturn(true);
        EasyMock.expect(java.io.File.createTempFile(EasyMock.anyString(), EasyMock.anyString(), EasyMock.anyObject(java.io.File.class))).andReturn(org.powermock.api.easymock.PowerMock.createNiceMock(java.io.File.class));
        org.powermock.api.easymock.PowerMock.createNiceMockAndExpectNew(java.io.PrintWriter.class, EasyMock.anyObject());
        org.powermock.api.easymock.PowerMock.mockStatic(java.lang.Runtime.class);
        java.lang.String commandLine = (((("ambari-python-wrap " + commonServicesPath) + "/PIG/package/null generate_configs null ") + commonServicesPath) + "/PIG/package /var/lib/ambari-server/tmp/structured-out.json ") + "INFO /var/lib/ambari-server/tmp";
        if (java.lang.System.getProperty("os.name").contains("Windows")) {
            commandLine = (((("ambari-python-wrap " + commonServicesPath) + "\\PIG\\package\\null generate_configs null ") + commonServicesPath) + "\\PIG\\package /var/lib/ambari-server/tmp\\structured-out.json ") + "INFO /var/lib/ambari-server/tmp";
        }
        java.lang.ProcessBuilder processBuilder = org.powermock.api.easymock.PowerMock.createNiceMock(java.lang.ProcessBuilder.class);
        org.powermock.api.easymock.PowerMock.expectNew(java.lang.ProcessBuilder.class, java.util.Arrays.asList(commandLine.split("\\s+"))).andReturn(processBuilder).once();
        EasyMock.expect(processBuilder.start()).andReturn(process).once();
        java.io.InputStream inputStream = new java.io.ByteArrayInputStream("some logging info".getBytes());
        EasyMock.expect(process.getInputStream()).andReturn(inputStream);
        EasyMock.replay(managementController, clusters, cluster, ambariMetaInfo, stackId, componentInfo, commandScriptDefinition, clusterConfig, host, service, serviceComponent, serviceComponentHost, serviceInfo, configHelper, runtime, process);
        org.powermock.api.easymock.PowerMock.replayAll();
        org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(authentication);
        java.util.Set<org.apache.ambari.server.controller.spi.Resource> resources = provider.getResources(request, predicate);
        org.junit.Assert.assertFalse(resources.isEmpty());
        EasyMock.verify(managementController, clusters, cluster, ambariMetaInfo, stackId, componentInfo, commandScriptDefinition, clusterConfig, host, service, serviceComponent, serviceComponentHost, serviceInfo, configHelper, runtime, process);
        org.powermock.api.easymock.PowerMock.verifyAll();
    }
}