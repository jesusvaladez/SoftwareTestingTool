package org.apache.ambari.server.serveraction.upgrades;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import javax.persistence.EntityManager;
import org.apache.commons.collections.MapUtils;
import org.easymock.Capture;
import org.easymock.EasyMockSupport;
import org.easymock.IAnswer;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.StandardPasswordEncoder;
import static org.apache.ambari.server.serveraction.upgrades.PreconfigureKerberosAction.UPGRADE_DIRECTION_KEY;
import static org.easymock.EasyMock.anyLong;
import static org.easymock.EasyMock.anyObject;
import static org.easymock.EasyMock.anyString;
import static org.easymock.EasyMock.capture;
import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.getCurrentArguments;
import static org.easymock.EasyMock.newCapture;
public class PreconfigureKerberosActionTest extends org.easymock.EasyMockSupport {
    private static final java.lang.String CLUSTER_NAME = "c1";

    @org.junit.Test
    public void testSkipWhenDowngrade() throws java.lang.Exception {
        com.google.inject.Injector injector = getInjector();
        java.util.Map<java.lang.String, java.lang.String> commandParams = getDefaultCommandParams();
        commandParams.put(org.apache.ambari.server.serveraction.upgrades.PreconfigureKerberosAction.UPGRADE_DIRECTION_KEY, org.apache.ambari.server.stack.upgrade.Direction.DOWNGRADE.name());
        org.apache.ambari.server.agent.ExecutionCommand executionCommand = createMockExecutionCommand(commandParams);
        replayAll();
        injector.getInstance(org.apache.ambari.server.api.services.AmbariMetaInfo.class).init();
        org.apache.ambari.server.serveraction.upgrades.PreconfigureKerberosAction action = injector.getInstance(org.apache.ambari.server.serveraction.upgrades.PreconfigureKerberosAction.class);
        java.util.concurrent.ConcurrentMap<java.lang.String, java.lang.Object> context = new java.util.concurrent.ConcurrentHashMap<>();
        action.setExecutionCommand(executionCommand);
        action.execute(context);
        verifyAll();
    }

    @org.junit.Test
    public void testSkipWhenNotKerberos() throws java.lang.Exception {
        com.google.inject.Injector injector = getInjector();
        org.apache.ambari.server.agent.ExecutionCommand executionCommand = createMockExecutionCommand(getDefaultCommandParams());
        org.apache.ambari.server.state.Cluster cluster = createMockCluster(org.apache.ambari.server.state.SecurityType.NONE, java.util.Collections.<org.apache.ambari.server.state.Host>emptyList(), java.util.Collections.<java.lang.String, org.apache.ambari.server.state.Service>emptyMap(), java.util.Collections.<java.lang.String, java.util.List<org.apache.ambari.server.state.ServiceComponentHost>>emptyMap(), createNiceMock(org.apache.ambari.server.state.StackId.class), java.util.Collections.<java.lang.String, org.apache.ambari.server.state.Config>emptyMap());
        org.apache.ambari.server.state.Clusters clusters = injector.getInstance(org.apache.ambari.server.state.Clusters.class);
        EasyMock.expect(clusters.getCluster(org.apache.ambari.server.serveraction.upgrades.PreconfigureKerberosActionTest.CLUSTER_NAME)).andReturn(cluster).atLeastOnce();
        replayAll();
        injector.getInstance(org.apache.ambari.server.api.services.AmbariMetaInfo.class).init();
        org.apache.ambari.server.serveraction.upgrades.PreconfigureKerberosAction action = injector.getInstance(org.apache.ambari.server.serveraction.upgrades.PreconfigureKerberosAction.class);
        java.util.concurrent.ConcurrentMap<java.lang.String, java.lang.Object> context = new java.util.concurrent.ConcurrentHashMap<>();
        action.setExecutionCommand(executionCommand);
        action.execute(context);
        verifyAll();
    }

    private java.lang.Long hostId = 1L;

    private org.apache.ambari.server.state.Host createMockHost(java.lang.String hostname) {
        org.apache.ambari.server.state.Host host = createNiceMock(org.apache.ambari.server.state.Host.class);
        EasyMock.expect(host.getHostName()).andReturn(hostname).anyTimes();
        EasyMock.expect(host.getHostId()).andReturn(hostId).anyTimes();
        hostId++;
        return host;
    }

    @org.junit.Test
    @org.junit.Ignore("Update accordingly to changes")
    public void testUpgrade() throws java.lang.Exception {
        org.easymock.Capture<? extends java.util.Map<java.lang.String, java.lang.String>> captureCoreSiteProperties = EasyMock.newCapture();
        com.google.inject.Injector injector = getInjector();
        org.apache.ambari.server.orm.dao.HostDAO hostDAO = injector.getInstance(org.apache.ambari.server.orm.dao.HostDAO.class);
        javax.persistence.EntityManager entityManager = injector.getInstance(javax.persistence.EntityManager.class);
        org.apache.ambari.server.orm.entities.HostEntity hostEntityMock = createNiceMock(org.apache.ambari.server.orm.entities.HostEntity.class);
        org.apache.ambari.server.orm.entities.KerberosKeytabPrincipalEntity principalMock = createNiceMock(org.apache.ambari.server.orm.entities.KerberosKeytabPrincipalEntity.class);
        EasyMock.expect(principalMock.getHostId()).andReturn(1L).anyTimes();
        EasyMock.expect(hostDAO.findByName(EasyMock.anyString())).andReturn(hostEntityMock).anyTimes();
        EasyMock.expect(hostDAO.findById(EasyMock.anyLong())).andReturn(hostEntityMock).anyTimes();
        EasyMock.expect(entityManager.find(EasyMock.eq(org.apache.ambari.server.orm.entities.KerberosKeytabEntity.class), EasyMock.anyString())).andReturn(createNiceMock(org.apache.ambari.server.orm.entities.KerberosKeytabEntity.class)).anyTimes();
        EasyMock.expect(entityManager.find(EasyMock.eq(org.apache.ambari.server.orm.entities.KerberosKeytabPrincipalEntity.class), EasyMock.anyObject())).andReturn(principalMock).anyTimes();
        org.apache.ambari.server.agent.ExecutionCommand executionCommand = createMockExecutionCommand(getDefaultCommandParams());
        org.apache.ambari.server.orm.entities.UpgradeEntity upgradeProgress = createMock(org.apache.ambari.server.orm.entities.UpgradeEntity.class);
        org.apache.ambari.server.state.StackId targetStackId = createMock(org.apache.ambari.server.state.StackId.class);
        EasyMock.expect(targetStackId.getStackId()).andReturn("HDP-2.6").anyTimes();
        EasyMock.expect(targetStackId.getStackName()).andReturn("HDP").anyTimes();
        EasyMock.expect(targetStackId.getStackVersion()).andReturn("2.6").anyTimes();
        final java.lang.String hostName1 = "c6401.ambari.apache.org";
        final java.lang.String hostName2 = "c6402.ambari.apache.org";
        final java.lang.String hostName3 = "c6403.ambari.apache.org";
        final org.apache.ambari.server.state.Host host1 = createMockHost(hostName1);
        org.apache.ambari.server.state.Host host2 = createMockHost(hostName2);
        org.apache.ambari.server.state.Host host3 = createMockHost(hostName3);
        java.util.Map<java.lang.String, org.apache.ambari.server.state.Host> hosts = new java.util.HashMap<>();
        hosts.put(hostName1, host1);
        hosts.put(hostName2, host2);
        hosts.put(hostName3, host3);
        java.util.Map<java.lang.String, org.apache.ambari.server.state.ServiceComponentHost> nnSchs = java.util.Collections.singletonMap(hostName1, createMockServiceComponentHost("HDFS", "NAMENODE", hostName1, host1));
        java.util.Map<java.lang.String, org.apache.ambari.server.state.ServiceComponentHost> rmSchs = java.util.Collections.singletonMap(hostName2, createMockServiceComponentHost("YARN", "RESOURCEMANAGER", hostName2, host2));
        java.util.Map<java.lang.String, org.apache.ambari.server.state.ServiceComponentHost> nmSchs = java.util.Collections.singletonMap(hostName2, createMockServiceComponentHost("YARN", "NODEMANAGER", hostName2, host2));
        java.util.Map<java.lang.String, org.apache.ambari.server.state.ServiceComponentHost> dnSchs = new java.util.HashMap<>();
        final java.util.Map<java.lang.String, org.apache.ambari.server.state.ServiceComponentHost> hcSchs = new java.util.HashMap<>();
        java.util.Map<java.lang.String, org.apache.ambari.server.state.ServiceComponentHost> zkSSchs = new java.util.HashMap<>();
        java.util.Map<java.lang.String, org.apache.ambari.server.state.ServiceComponentHost> zkCSchs = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.util.List<org.apache.ambari.server.state.ServiceComponentHost>> serviceComponentHosts = new java.util.HashMap<>();
        for (java.util.Map.Entry<java.lang.String, org.apache.ambari.server.state.Host> entry : hosts.entrySet()) {
            java.lang.String hostname = entry.getKey();
            java.util.List<org.apache.ambari.server.state.ServiceComponentHost> list = new java.util.ArrayList<>();
            org.apache.ambari.server.state.ServiceComponentHost sch;
            sch = createMockServiceComponentHost("HDFS", "DATANODE", hostname, entry.getValue());
            dnSchs.put(hostname, sch);
            list.add(sch);
            sch = createMockServiceComponentHost("HDFS", "HDFS_CLIENT", hostname, entry.getValue());
            hcSchs.put(hostname, sch);
            list.add(sch);
            sch = createMockServiceComponentHost("ZOOKEEPER", "ZOOKEEPER_SERVER", hostname, entry.getValue());
            zkSSchs.put(hostname, sch);
            list.add(sch);
            sch = createMockServiceComponentHost("ZOOKEEPER", "ZOOKEEPER_CLIENT", hostname, entry.getValue());
            zkCSchs.put(hostname, sch);
            list.add(sch);
            serviceComponentHosts.put(hostname, list);
        }
        java.util.Map<java.lang.String, org.apache.ambari.server.state.ServiceComponent> hdfsComponents = new java.util.HashMap<>();
        hdfsComponents.put("NAMENODE", createMockServiceComponent("NAMENODE", false, nnSchs));
        hdfsComponents.put("DATANODE", createMockServiceComponent("DATANODE", false, dnSchs));
        hdfsComponents.put("HDFS_CLIENT", createMockServiceComponent("HDFS_CLIENT", true, hcSchs));
        java.util.Map<java.lang.String, org.apache.ambari.server.state.ServiceComponent> yarnComponents = new java.util.HashMap<>();
        yarnComponents.put("RESOURCEMANAGER", createMockServiceComponent("RESOURCEMANAGER", false, rmSchs));
        yarnComponents.put("NODEMANAGER", createMockServiceComponent("NODEMANAGER", false, nmSchs));
        java.util.Map<java.lang.String, org.apache.ambari.server.state.ServiceComponent> zkCompnents = new java.util.HashMap<>();
        yarnComponents.put("ZOOKEEPER_SERVER", createMockServiceComponent("ZOOKEEPER_SERVER", false, zkSSchs));
        yarnComponents.put("ZOOKEEPER_CLIENT", createMockServiceComponent("ZOOKEEPER_CLIENT", true, zkCSchs));
        org.apache.ambari.server.state.Service hdfsService = createMockService("HDFS", hdfsComponents, targetStackId);
        org.apache.ambari.server.state.Service yarnService = createMockService("YARN", yarnComponents, targetStackId);
        org.apache.ambari.server.state.Service zkService = createMockService("ZOOKEEPER", zkCompnents, targetStackId);
        java.util.Map<java.lang.String, org.apache.ambari.server.state.Service> installedServices = new java.util.HashMap<>();
        installedServices.put("HDFS", hdfsService);
        installedServices.put("YARN", yarnService);
        installedServices.put("ZOOKEEPER", zkService);
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> clusterConfig = getClusterConfig();
        java.util.Map<java.lang.String, org.apache.ambari.server.state.Config> clusterConfigs = new java.util.HashMap<>();
        for (java.util.Map.Entry<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> entry : clusterConfig.entrySet()) {
            clusterConfigs.put(entry.getKey(), createMockConfig(entry.getValue()));
        }
        org.apache.ambari.server.state.Cluster cluster = createMockCluster(org.apache.ambari.server.state.SecurityType.KERBEROS, hosts.values(), installedServices, serviceComponentHosts, targetStackId, clusterConfigs);
        EasyMock.expect(cluster.getUpgradeInProgress()).andReturn(upgradeProgress).once();
        org.apache.ambari.server.orm.entities.RepositoryVersionEntity targetRepositoryVersion = createMock(org.apache.ambari.server.orm.entities.RepositoryVersionEntity.class);
        EasyMock.expect(targetRepositoryVersion.getStackId()).andReturn(targetStackId).atLeastOnce();
        org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeContext upgradeContext = createMock(org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeContext.class);
        EasyMock.expect(upgradeContext.getTargetRepositoryVersion(EasyMock.anyString())).andReturn(targetRepositoryVersion).atLeastOnce();
        org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeContextFactory upgradeContextFactory = injector.getInstance(org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeContextFactory.class);
        EasyMock.expect(upgradeContextFactory.create(cluster, upgradeProgress)).andReturn(upgradeContext).once();
        createMockClusters(injector, cluster);
        java.util.List<org.apache.ambari.server.state.PropertyInfo> knoxProperties = java.util.Arrays.asList(crateMockPropertyInfo("knox-env.xml", "knox_user", "knox"), crateMockPropertyInfo("knox-env.xml", "knox_group", "knox"), crateMockPropertyInfo("knox-env.xml", "knox_principal_name", "KERBEROS_PRINCIPAL"), crateMockPropertyInfo("gateway-site.xml", "gateway.port", "8443"), crateMockPropertyInfo("gateway-site.xml", "gateway.path", "gateway"));
        org.apache.ambari.server.api.services.AmbariMetaInfo ambariMetaInfo = injector.getInstance(org.apache.ambari.server.api.services.AmbariMetaInfo.class);
        EasyMock.expect(ambariMetaInfo.getKerberosDescriptor("HDP", "2.6", false)).andReturn(getKerberosDescriptor(false)).once();
        EasyMock.expect(ambariMetaInfo.getKerberosDescriptor("HDP", "2.6", true)).andReturn(getKerberosDescriptor(true)).once();
        EasyMock.expect(ambariMetaInfo.isValidService("HDP", "2.6", "BEACON")).andReturn(false).anyTimes();
        EasyMock.expect(ambariMetaInfo.isValidService("HDP", "2.6", "KNOX")).andReturn(true).anyTimes();
        EasyMock.expect(ambariMetaInfo.getService("HDP", "2.6", "KNOX")).andReturn(createMockServiceInfo("KNOX", knoxProperties, java.util.Collections.singletonList(createMockComponentInfo("KNOX_GATEWAY")))).anyTimes();
        org.apache.ambari.server.controller.AmbariManagementController managementController = injector.getInstance(org.apache.ambari.server.controller.AmbariManagementController.class);
        EasyMock.expect(managementController.findConfigurationTagsWithOverrides(cluster, null, null)).andReturn(clusterConfig).once();
        EasyMock.expect(managementController.getAuthName()).andReturn("admin").anyTimes();
        org.apache.ambari.server.state.ConfigHelper configHelper = injector.getInstance(org.apache.ambari.server.state.ConfigHelper.class);
        EasyMock.expect(configHelper.getEffectiveConfigProperties(cluster, clusterConfig)).andReturn(clusterConfig).anyTimes();
        configHelper.updateConfigType(EasyMock.eq(cluster), EasyMock.eq(targetStackId), EasyMock.eq(managementController), EasyMock.eq("core-site"), EasyMock.capture(captureCoreSiteProperties), EasyMock.anyObject(java.util.Collection.class), EasyMock.eq("admin"), EasyMock.anyString());
        EasyMock.expectLastCall().once();
        org.apache.ambari.server.topology.TopologyManager topologyManager = injector.getInstance(org.apache.ambari.server.topology.TopologyManager.class);
        EasyMock.expect(topologyManager.getPendingHostComponents()).andReturn(java.util.Collections.<java.lang.String, java.util.Collection<java.lang.String>>emptyMap()).anyTimes();
        org.apache.ambari.server.api.services.stackadvisor.StackAdvisorHelper stackAdvisorHelper = injector.getInstance(org.apache.ambari.server.api.services.stackadvisor.StackAdvisorHelper.class);
        EasyMock.expect(stackAdvisorHelper.recommend(EasyMock.anyObject(org.apache.ambari.server.api.services.stackadvisor.StackAdvisorRequest.class))).andAnswer(new org.easymock.IAnswer<org.apache.ambari.server.api.services.stackadvisor.recommendations.RecommendationResponse>() {
            @java.lang.Override
            public org.apache.ambari.server.api.services.stackadvisor.recommendations.RecommendationResponse answer() throws java.lang.Throwable {
                java.lang.Object[] args = EasyMock.getCurrentArguments();
                org.apache.ambari.server.api.services.stackadvisor.StackAdvisorRequest request = ((org.apache.ambari.server.api.services.stackadvisor.StackAdvisorRequest) (args[0]));
                org.apache.ambari.server.api.services.stackadvisor.StackAdvisorRequest.StackAdvisorRequestType requestType = request.getRequestType();
                if (requestType == org.apache.ambari.server.api.services.stackadvisor.StackAdvisorRequest.StackAdvisorRequestType.HOST_GROUPS) {
                    org.apache.ambari.server.api.services.stackadvisor.recommendations.RecommendationResponse.Blueprint blueprint = new org.apache.ambari.server.api.services.stackadvisor.recommendations.RecommendationResponse.Blueprint();
                    blueprint.setHostGroups(new java.util.HashSet<>(java.util.Arrays.asList(createRecommendationHostGroup(hostName1, java.util.Arrays.asList("ZOOKEEPER_SERVER", "ZOOKEEPER_CLIENT", "HDFS_CLIENT", "DATANODE", "NAMENODE", "KNOX_GATEWAY")), createRecommendationHostGroup(hostName2, java.util.Arrays.asList("ZOOKEEPER_SERVER", "ZOOKEEPER_CLIENT", "HDFS_CLIENT", "DATANODE", "RESOURCEMANAGER", "NODEMANAGER")), createRecommendationHostGroup(hostName3, java.util.Arrays.asList("ZOOKEEPER_SERVER", "ZOOKEEPER_CLIENT", "HDFS_CLIENT", "DATANODE")))));
                    java.util.Set<org.apache.ambari.server.api.services.stackadvisor.recommendations.RecommendationResponse.BindingHostGroup> bindingHostGroups = new java.util.HashSet<>(java.util.Arrays.asList(createBindingHostGroup(hostName1), createBindingHostGroup(hostName2), createBindingHostGroup(hostName3)));
                    org.apache.ambari.server.api.services.stackadvisor.recommendations.RecommendationResponse.BlueprintClusterBinding binding = new org.apache.ambari.server.api.services.stackadvisor.recommendations.RecommendationResponse.BlueprintClusterBinding();
                    binding.setHostGroups(bindingHostGroups);
                    org.apache.ambari.server.api.services.stackadvisor.recommendations.RecommendationResponse.Recommendation recommendation = new org.apache.ambari.server.api.services.stackadvisor.recommendations.RecommendationResponse.Recommendation();
                    recommendation.setBlueprint(blueprint);
                    recommendation.setBlueprintClusterBinding(binding);
                    org.apache.ambari.server.api.services.stackadvisor.recommendations.RecommendationResponse response = new org.apache.ambari.server.api.services.stackadvisor.recommendations.RecommendationResponse();
                    response.setRecommendations(recommendation);
                    return response;
                } else {
                    return null;
                }
            }
        }).anyTimes();
        replayAll();
        ambariMetaInfo.init();
        org.apache.ambari.server.utils.StageUtils.setTopologyManager(injector.getInstance(org.apache.ambari.server.topology.TopologyManager.class));
        org.apache.ambari.server.utils.StageUtils.setConfiguration(injector.getInstance(org.apache.ambari.server.configuration.Configuration.class));
        org.apache.ambari.server.serveraction.upgrades.PreconfigureKerberosAction action = injector.getInstance(org.apache.ambari.server.serveraction.upgrades.PreconfigureKerberosAction.class);
        java.util.concurrent.ConcurrentMap<java.lang.String, java.lang.Object> context = new java.util.concurrent.ConcurrentHashMap<>();
        action.setExecutionCommand(executionCommand);
        action.execute(context);
        verifyAll();
        org.junit.Assert.assertTrue(captureCoreSiteProperties.hasCaptured());
        java.util.Map<java.lang.String, java.lang.String> capturedProperties = captureCoreSiteProperties.getValue();
        org.junit.Assert.assertFalse(org.apache.commons.collections.MapUtils.isEmpty(capturedProperties));
        java.lang.String expectedAuthToLocalRules = "" + ((((((((("RULE:[1:$1@$0](ambari-qa-c1@EXAMPLE.COM)s/.*/ambari-qa/\n" + "RULE:[1:$1@$0](hdfs-c1@EXAMPLE.COM)s/.*/hdfs/\n") + "RULE:[1:$1@$0](.*@EXAMPLE.COM)s/@.*//\n") + "RULE:[2:$1@$0](beacon@EXAMPLE.COM)s/.*/beacon/\n") + "RULE:[2:$1@$0](dn@EXAMPLE.COM)s/.*/hdfs/\n") + "RULE:[2:$1@$0](knox@EXAMPLE.COM)s/.*/knox/\n") + "RULE:[2:$1@$0](nm@EXAMPLE.COM)s/.*/${yarn-env/yarn_user}/\n") + "RULE:[2:$1@$0](nn@EXAMPLE.COM)s/.*/hdfs/\n") + "RULE:[2:$1@$0](rm@EXAMPLE.COM)s/.*/${yarn-env/yarn_user}/\n") + "DEFAULT");
        org.junit.Assert.assertEquals(3, capturedProperties.size());
        org.junit.Assert.assertEquals("users", capturedProperties.get("hadoop.proxyuser.knox.groups"));
        org.junit.Assert.assertEquals("c6401.ambari.apache.org", capturedProperties.get("hadoop.proxyuser.knox.hosts"));
        org.junit.Assert.assertEquals(expectedAuthToLocalRules, capturedProperties.get("hadoop.security.auth_to_local"));
    }

    private org.apache.ambari.server.api.services.stackadvisor.recommendations.RecommendationResponse.BindingHostGroup createBindingHostGroup(java.lang.String hostName) {
        org.apache.ambari.server.api.services.stackadvisor.recommendations.RecommendationResponse.BindingHostGroup bindingHostGroup = new org.apache.ambari.server.api.services.stackadvisor.recommendations.RecommendationResponse.BindingHostGroup();
        bindingHostGroup.setName(hostName);
        bindingHostGroup.setHosts(java.util.Collections.singleton(java.util.Collections.singletonMap("fqdn", hostName)));
        return bindingHostGroup;
    }

    private org.apache.ambari.server.api.services.stackadvisor.recommendations.RecommendationResponse.HostGroup createRecommendationHostGroup(java.lang.String hostName, java.util.List<java.lang.String> components) {
        java.util.Set<java.util.Map<java.lang.String, java.lang.String>> componentDetails = new java.util.HashSet<>();
        for (java.lang.String component : components) {
            componentDetails.add(java.util.Collections.singletonMap("name", component));
        }
        org.apache.ambari.server.api.services.stackadvisor.recommendations.RecommendationResponse.HostGroup hostGroup = new org.apache.ambari.server.api.services.stackadvisor.recommendations.RecommendationResponse.HostGroup();
        hostGroup.setComponents(componentDetails);
        hostGroup.setName(hostName);
        return hostGroup;
    }

    private org.apache.ambari.server.state.ComponentInfo createMockComponentInfo(java.lang.String componentName) {
        org.apache.ambari.server.state.ComponentInfo componentInfo = createMock(org.apache.ambari.server.state.ComponentInfo.class);
        EasyMock.expect(componentInfo.getName()).andReturn(componentName).anyTimes();
        return componentInfo;
    }

    private org.apache.ambari.server.state.PropertyInfo crateMockPropertyInfo(java.lang.String fileName, java.lang.String propertyName, java.lang.String propertyValue) {
        org.apache.ambari.server.state.PropertyInfo propertyInfo = createMock(org.apache.ambari.server.state.PropertyInfo.class);
        EasyMock.expect(propertyInfo.getFilename()).andReturn(fileName).anyTimes();
        EasyMock.expect(propertyInfo.getName()).andReturn(propertyName).anyTimes();
        EasyMock.expect(propertyInfo.getValue()).andReturn(propertyValue).anyTimes();
        return propertyInfo;
    }

    private org.apache.ambari.server.state.ServiceInfo createMockServiceInfo(java.lang.String name, java.util.List<org.apache.ambari.server.state.PropertyInfo> properties, java.util.List<org.apache.ambari.server.state.ComponentInfo> components) {
        org.apache.ambari.server.state.ServiceInfo serviceInfo = createMock(org.apache.ambari.server.state.ServiceInfo.class);
        EasyMock.expect(serviceInfo.getName()).andReturn(name).anyTimes();
        EasyMock.expect(serviceInfo.getProperties()).andReturn(properties).anyTimes();
        EasyMock.expect(serviceInfo.getComponents()).andReturn(components).anyTimes();
        return serviceInfo;
    }

    private java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> getClusterConfig() throws java.net.URISyntaxException, java.io.FileNotFoundException {
        java.net.URL url = java.lang.ClassLoader.getSystemResource("PreconfigureActionTest_cluster_config.json");
        return new com.google.gson.Gson().fromJson(new java.io.FileReader(new java.io.File(url.toURI())), new com.google.gson.reflect.TypeToken<java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>>>() {}.getType());
    }

    private org.apache.ambari.server.state.kerberos.KerberosDescriptor getKerberosDescriptor(boolean includePreconfigureData) throws java.net.URISyntaxException, java.io.IOException {
        java.net.URL url;
        if (includePreconfigureData) {
            url = java.lang.ClassLoader.getSystemResource("PreconfigureActionTest_kerberos_descriptor_stack_preconfigure.json");
        } else {
            url = java.lang.ClassLoader.getSystemResource("PreconfigureActionTest_kerberos_descriptor_stack.json");
        }
        return new org.apache.ambari.server.state.kerberos.KerberosDescriptorFactory().createInstance(new java.io.File(url.toURI()));
    }

    private org.apache.ambari.server.state.ServiceComponent createMockServiceComponent(java.lang.String name, java.lang.Boolean isClientComponent, java.util.Map<java.lang.String, org.apache.ambari.server.state.ServiceComponentHost> serviceComponentHostMap) throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.server.state.ServiceComponent serviceComponent = createMock(org.apache.ambari.server.state.ServiceComponent.class);
        EasyMock.expect(serviceComponent.getName()).andReturn(name).anyTimes();
        EasyMock.expect(serviceComponent.isClientComponent()).andReturn(isClientComponent).anyTimes();
        for (java.util.Map.Entry<java.lang.String, org.apache.ambari.server.state.ServiceComponentHost> entry : serviceComponentHostMap.entrySet()) {
            EasyMock.expect(serviceComponent.getServiceComponentHost(entry.getKey())).andReturn(serviceComponentHostMap.get(entry.getKey())).anyTimes();
        }
        EasyMock.expect(serviceComponent.getServiceComponentHosts()).andReturn(serviceComponentHostMap).anyTimes();
        return serviceComponent;
    }

    private org.apache.ambari.server.state.ServiceComponentHost createMockServiceComponentHost(java.lang.String serviceName, java.lang.String componentName, java.lang.String hostname, org.apache.ambari.server.state.Host host) {
        org.apache.ambari.server.state.ServiceComponentHost serviceComponentHost = createMock(org.apache.ambari.server.state.ServiceComponentHost.class);
        EasyMock.expect(serviceComponentHost.getServiceName()).andReturn(serviceName).anyTimes();
        EasyMock.expect(serviceComponentHost.getServiceComponentName()).andReturn(componentName).anyTimes();
        EasyMock.expect(serviceComponentHost.getHostName()).andReturn(hostname).anyTimes();
        EasyMock.expect(serviceComponentHost.getHost()).andReturn(host).anyTimes();
        EasyMock.expect(serviceComponentHost.getComponentAdminState()).andReturn(org.apache.ambari.server.state.HostComponentAdminState.INSERVICE).anyTimes();
        return serviceComponentHost;
    }

    private org.apache.ambari.server.state.Service createMockService(java.lang.String name, java.util.Map<java.lang.String, org.apache.ambari.server.state.ServiceComponent> components, org.apache.ambari.server.state.StackId desiredStackId) {
        org.apache.ambari.server.state.Service service = createMock(org.apache.ambari.server.state.Service.class);
        EasyMock.expect(service.getName()).andReturn(name).anyTimes();
        EasyMock.expect(service.getServiceComponents()).andReturn(components).anyTimes();
        EasyMock.expect(service.getDesiredStackId()).andReturn(desiredStackId).anyTimes();
        return service;
    }

    private org.apache.ambari.server.state.Clusters createMockClusters(com.google.inject.Injector injector, org.apache.ambari.server.state.Cluster cluster) throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.server.state.Clusters clusters = injector.getInstance(org.apache.ambari.server.state.Clusters.class);
        EasyMock.expect(clusters.getCluster(org.apache.ambari.server.serveraction.upgrades.PreconfigureKerberosActionTest.CLUSTER_NAME)).andReturn(cluster).atLeastOnce();
        return clusters;
    }

    private org.apache.ambari.server.state.Cluster createMockCluster(org.apache.ambari.server.state.SecurityType securityType, java.util.Collection<org.apache.ambari.server.state.Host> hosts, java.util.Map<java.lang.String, org.apache.ambari.server.state.Service> services, java.util.Map<java.lang.String, java.util.List<org.apache.ambari.server.state.ServiceComponentHost>> serviceComponentHosts, org.apache.ambari.server.state.StackId currentStackId, final java.util.Map<java.lang.String, org.apache.ambari.server.state.Config> clusterConfigs) {
        final org.apache.ambari.server.state.Cluster cluster = createMock(org.apache.ambari.server.state.Cluster.class);
        EasyMock.expect(cluster.getSecurityType()).andReturn(securityType).anyTimes();
        EasyMock.expect(cluster.getClusterName()).andReturn(org.apache.ambari.server.serveraction.upgrades.PreconfigureKerberosActionTest.CLUSTER_NAME).anyTimes();
        EasyMock.expect(cluster.getClusterId()).andReturn(1L).anyTimes();
        EasyMock.expect(cluster.getHosts()).andReturn(hosts).anyTimes();
        EasyMock.expect(cluster.getServices()).andReturn(services).anyTimes();
        EasyMock.expect(cluster.getCurrentStackVersion()).andReturn(currentStackId).anyTimes();
        for (java.util.Map.Entry<java.lang.String, java.util.List<org.apache.ambari.server.state.ServiceComponentHost>> entry : serviceComponentHosts.entrySet()) {
            EasyMock.expect(cluster.getServiceComponentHosts(entry.getKey())).andReturn(entry.getValue()).atLeastOnce();
        }
        EasyMock.expect(cluster.getServiceComponentHostMap(null, new java.util.HashSet<>(java.util.Arrays.asList("HDFS", "ZOOKEEPER", "YARN", "KNOX")))).andReturn(null).anyTimes();
        EasyMock.expect(cluster.getServiceComponentHostMap(null, new java.util.HashSet<>(java.util.Arrays.asList("HDFS", "ZOOKEEPER", "YARN")))).andReturn(null).anyTimes();
        java.util.Map<java.lang.String, java.lang.String> configTypeService = new java.util.HashMap<>();
        configTypeService.put("hdfs-site", "HDFS");
        configTypeService.put("core-site", "HDFS");
        configTypeService.put("hadoop-env", "HDFS");
        configTypeService.put("cluster-env", null);
        configTypeService.put("kerberos-env", "KERBEROS");
        configTypeService.put("ranger-hdfs-audit", "RANGER");
        configTypeService.put("zookeeper-env", "ZOOKEEPER");
        configTypeService.put("gateway-site", "KNOX");
        for (java.util.Map.Entry<java.lang.String, java.lang.String> entry : configTypeService.entrySet()) {
            EasyMock.expect(cluster.getServiceByConfigType(entry.getKey())).andReturn(entry.getValue()).anyTimes();
        }
        for (java.util.Map.Entry<java.lang.String, org.apache.ambari.server.state.Config> entry : clusterConfigs.entrySet()) {
            EasyMock.expect(cluster.getDesiredConfigByType(entry.getKey())).andReturn(entry.getValue()).anyTimes();
            EasyMock.expect(cluster.getConfigsByType(entry.getKey())).andReturn(java.util.Collections.singletonMap(entry.getKey(), entry.getValue())).anyTimes();
            EasyMock.expect(cluster.getConfigPropertiesTypes(entry.getKey())).andReturn(java.util.Collections.<org.apache.ambari.server.state.PropertyInfo.PropertyType, java.util.Set<java.lang.String>>emptyMap()).anyTimes();
        }
        return cluster;
    }

    private org.apache.ambari.server.state.Config createMockConfig(java.util.Map<java.lang.String, java.lang.String> properties) {
        org.apache.ambari.server.state.Config config = createMock(org.apache.ambari.server.state.Config.class);
        EasyMock.expect(config.getProperties()).andReturn(properties).anyTimes();
        EasyMock.expect(config.getPropertiesAttributes()).andReturn(java.util.Collections.<java.lang.String, java.util.Map<java.lang.String, java.lang.String>>emptyMap()).anyTimes();
        return config;
    }

    private java.util.Map<java.lang.String, java.lang.String> getDefaultCommandParams() {
        java.util.Map<java.lang.String, java.lang.String> commandParams = new java.util.HashMap<>();
        commandParams.put("clusterName", org.apache.ambari.server.serveraction.upgrades.PreconfigureKerberosActionTest.CLUSTER_NAME);
        commandParams.put(org.apache.ambari.server.serveraction.upgrades.PreconfigureKerberosAction.UPGRADE_DIRECTION_KEY, org.apache.ambari.server.stack.upgrade.Direction.UPGRADE.name());
        return commandParams;
    }

    private org.apache.ambari.server.agent.ExecutionCommand createMockExecutionCommand(java.util.Map<java.lang.String, java.lang.String> commandParams) {
        org.apache.ambari.server.agent.ExecutionCommand executionCommand = createMock(org.apache.ambari.server.agent.ExecutionCommand.class);
        EasyMock.expect(executionCommand.getCommandParams()).andReturn(commandParams).atLeastOnce();
        return executionCommand;
    }

    private com.google.inject.Injector getInjector() {
        return com.google.inject.Guice.createInjector(new com.google.inject.AbstractModule() {
            @java.lang.Override
            protected void configure() {
                org.apache.ambari.server.testutils.PartialNiceMockBinder.newBuilder(PreconfigureKerberosActionTest.this).addActionDBAccessorConfigsBindings().addLdapBindings().addPasswordEncryptorBindings().build().configure(binder());
                bind(javax.persistence.EntityManager.class).toInstance(createMock(javax.persistence.EntityManager.class));
                bind(org.apache.ambari.server.orm.DBAccessor.class).toInstance(createMock(org.apache.ambari.server.orm.DBAccessor.class));
                bind(org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeContextFactory.class).toInstance(createMock(org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeContextFactory.class));
                bind(org.apache.ambari.server.state.stack.OsFamily.class).toInstance(createMock(org.apache.ambari.server.state.stack.OsFamily.class));
                bind(org.apache.ambari.server.stack.StackManagerFactory.class).toInstance(createMock(org.apache.ambari.server.stack.StackManagerFactory.class));
                bind(org.apache.ambari.server.actionmanager.StageFactory.class).toInstance(createMock(org.apache.ambari.server.actionmanager.StageFactory.class));
                bind(org.apache.ambari.server.api.services.AmbariMetaInfo.class).toInstance(createMock(org.apache.ambari.server.api.services.AmbariMetaInfo.class));
                bind(org.apache.ambari.server.controller.AmbariCustomCommandExecutionHelper.class).toInstance(createMock(org.apache.ambari.server.controller.AmbariCustomCommandExecutionHelper.class));
                bind(org.apache.ambari.server.actionmanager.ActionManager.class).toInstance(createMock(org.apache.ambari.server.actionmanager.ActionManager.class));
                bind(org.apache.ambari.server.orm.dao.HostRoleCommandDAO.class).toInstance(createNiceMock(org.apache.ambari.server.orm.dao.HostRoleCommandDAO.class));
                bind(org.apache.ambari.server.audit.AuditLogger.class).toInstance(createNiceMock(org.apache.ambari.server.audit.AuditLogger.class));
                bind(org.apache.ambari.server.orm.dao.ArtifactDAO.class).toInstance(createNiceMock(org.apache.ambari.server.orm.dao.ArtifactDAO.class));
                bind(org.apache.ambari.server.orm.dao.KerberosPrincipalDAO.class).toInstance(createNiceMock(org.apache.ambari.server.orm.dao.KerberosPrincipalDAO.class));
                bind(org.apache.ambari.server.metadata.RoleCommandOrderProvider.class).to(org.apache.ambari.server.metadata.CachedRoleCommandOrderProvider.class);
                bind(org.apache.ambari.server.actionmanager.HostRoleCommandFactory.class).to(org.apache.ambari.server.actionmanager.HostRoleCommandFactoryImpl.class);
                bind(org.apache.ambari.server.metadata.RoleCommandOrderProvider.class).to(org.apache.ambari.server.metadata.CachedRoleCommandOrderProvider.class);
                bind(org.apache.ambari.server.actionmanager.HostRoleCommandFactory.class).to(org.apache.ambari.server.actionmanager.HostRoleCommandFactoryImpl.class);
                bind(org.apache.ambari.server.stageplanner.RoleGraphFactory.class).toInstance(createMock(org.apache.ambari.server.stageplanner.RoleGraphFactory.class));
                bind(org.apache.ambari.server.actionmanager.RequestFactory.class).toInstance(createMock(org.apache.ambari.server.actionmanager.RequestFactory.class));
                bind(org.apache.ambari.server.state.scheduler.RequestExecutionFactory.class).toInstance(createMock(org.apache.ambari.server.state.scheduler.RequestExecutionFactory.class));
                bind(org.apache.ambari.server.security.encryption.CredentialStoreService.class).toInstance(createMock(org.apache.ambari.server.security.encryption.CredentialStoreService.class));
                bind(org.apache.ambari.server.topology.TopologyManager.class).toInstance(createNiceMock(org.apache.ambari.server.topology.TopologyManager.class));
                bind(org.apache.ambari.server.state.ConfigFactory.class).toInstance(createMock(org.apache.ambari.server.state.ConfigFactory.class));
                bind(org.apache.ambari.server.topology.PersistedState.class).toInstance(createMock(org.apache.ambari.server.topology.PersistedState.class));
                bind(org.apache.ambari.server.topology.tasks.ConfigureClusterTaskFactory.class).toInstance(createNiceMock(org.apache.ambari.server.topology.tasks.ConfigureClusterTaskFactory.class));
                bind(org.apache.ambari.server.configuration.Configuration.class).toInstance(new org.apache.ambari.server.configuration.Configuration(new java.util.Properties()));
                bind(org.springframework.security.crypto.password.PasswordEncoder.class).toInstance(new org.springframework.security.crypto.password.StandardPasswordEncoder());
                bind(org.apache.ambari.server.hooks.HookService.class).to(org.apache.ambari.server.hooks.users.UserHookService.class);
                bind(org.apache.ambari.server.controller.AbstractRootServiceResponseFactory.class).to(org.apache.ambari.server.controller.RootServiceResponseFactory.class);
                bind(org.apache.ambari.server.mpack.MpackManagerFactory.class).toInstance(createNiceMock(org.apache.ambari.server.mpack.MpackManagerFactory.class));
                bind(org.apache.ambari.server.controller.AmbariManagementController.class).toInstance(createMock(org.apache.ambari.server.controller.AmbariManagementController.class));
                bind(org.apache.ambari.server.controller.KerberosHelper.class).to(org.apache.ambari.server.controller.KerberosHelperImpl.class);
                bind(org.apache.ambari.server.state.Clusters.class).toInstance(createMock(org.apache.ambari.server.state.Clusters.class));
                bind(org.apache.ambari.server.api.services.stackadvisor.StackAdvisorHelper.class).toInstance(createMock(org.apache.ambari.server.api.services.stackadvisor.StackAdvisorHelper.class));
                bind(org.apache.ambari.server.state.ConfigHelper.class).toInstance(createMock(org.apache.ambari.server.state.ConfigHelper.class));
                bind(org.apache.ambari.server.orm.dao.HostDAO.class).toInstance(createMock(org.apache.ambari.server.orm.dao.HostDAO.class));
                bind(org.apache.ambari.server.scheduler.ExecutionScheduler.class).to(org.apache.ambari.server.scheduler.ExecutionSchedulerImpl.class);
                bind(org.apache.ambari.server.actionmanager.ActionDBAccessor.class).to(org.apache.ambari.server.actionmanager.ActionDBAccessorImpl.class);
                install(new com.google.inject.assistedinject.FactoryModuleBuilder().implement(org.apache.ambari.server.hooks.HookContext.class, org.apache.ambari.server.hooks.users.PostUserCreationHookContext.class).build(org.apache.ambari.server.hooks.HookContextFactory.class));
                install(new com.google.inject.assistedinject.FactoryModuleBuilder().implement(org.apache.ambari.server.state.ServiceComponentHost.class, org.apache.ambari.server.state.svccomphost.ServiceComponentHostImpl.class).build(org.apache.ambari.server.state.ServiceComponentHostFactory.class));
                install(new com.google.inject.assistedinject.FactoryModuleBuilder().implement(org.apache.ambari.server.state.ServiceComponent.class, org.apache.ambari.server.state.ServiceComponentImpl.class).build(org.apache.ambari.server.state.ServiceComponentFactory.class));
                install(new com.google.inject.assistedinject.FactoryModuleBuilder().implement(org.apache.ambari.server.state.configgroup.ConfigGroup.class, org.apache.ambari.server.state.configgroup.ConfigGroupImpl.class).build(org.apache.ambari.server.state.configgroup.ConfigGroupFactory.class));
                install(new com.google.inject.assistedinject.FactoryModuleBuilder().implement(org.apache.ambari.server.events.AmbariEvent.class, com.google.inject.name.Names.named("userCreated"), org.apache.ambari.server.hooks.users.UserCreatedEvent.class).build(org.apache.ambari.server.hooks.AmbariEventFactory.class));
                install(new com.google.inject.assistedinject.FactoryModuleBuilder().implement(org.apache.ambari.server.state.Cluster.class, org.apache.ambari.server.state.cluster.ClusterImpl.class).build(org.apache.ambari.server.state.cluster.ClusterFactory.class));
                install(new com.google.inject.assistedinject.FactoryModuleBuilder().implement(org.apache.ambari.server.state.Host.class, org.apache.ambari.server.state.host.HostImpl.class).build(org.apache.ambari.server.state.host.HostFactory.class));
                install(new com.google.inject.assistedinject.FactoryModuleBuilder().implement(org.apache.ambari.server.state.Service.class, org.apache.ambari.server.state.ServiceImpl.class).build(org.apache.ambari.server.state.ServiceFactory.class));
            }
        });
    }
}