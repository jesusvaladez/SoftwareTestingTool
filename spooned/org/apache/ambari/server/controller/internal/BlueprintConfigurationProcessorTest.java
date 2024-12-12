package org.apache.ambari.server.controller.internal;
import org.apache.commons.lang.StringUtils;
import org.easymock.EasyMock;
import org.easymock.EasyMockRule;
import org.easymock.EasyMockSupport;
import org.easymock.Mock;
import org.easymock.MockType;
import org.powermock.api.easymock.PowerMock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import static org.apache.ambari.server.state.ConfigHelper.CLUSTER_ENV_STACK_FEATURES_PROPERTY;
import static org.apache.ambari.server.state.ConfigHelper.CLUSTER_ENV_STACK_NAME_PROPERTY;
import static org.apache.ambari.server.state.ConfigHelper.CLUSTER_ENV_STACK_PACKAGES_PROPERTY;
import static org.apache.ambari.server.state.ConfigHelper.CLUSTER_ENV_STACK_ROOT_PROPERTY;
import static org.apache.ambari.server.state.ConfigHelper.CLUSTER_ENV_STACK_TOOLS_PROPERTY;
import static org.easymock.EasyMock.anyObject;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.reset;
@org.junit.runner.RunWith(org.powermock.modules.junit4.PowerMockRunner.class)
@org.powermock.core.classloader.annotations.PrepareForTest(org.apache.ambari.server.controller.AmbariServer.class)
public class BlueprintConfigurationProcessorTest extends org.easymock.EasyMockSupport {
    private static final org.apache.ambari.server.topology.Configuration EMPTY_CONFIG = new org.apache.ambari.server.topology.Configuration(java.util.Collections.emptyMap(), java.util.Collections.emptyMap());

    private final java.util.Map<java.lang.String, java.util.Collection<java.lang.String>> serviceComponents = new java.util.HashMap<>();

    private final java.util.Map<java.lang.String, java.lang.String> defaultClusterEnvProperties = new java.util.HashMap<>();

    private final java.lang.String STACK_NAME = "testStack";

    private final java.lang.String STACK_VERSION = "1";

    private final java.lang.String CLUSTER_ENV_PROP = "cluster-env";

    private final java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> stackProperties = new java.util.HashMap<>();

    @org.junit.Rule
    public org.easymock.EasyMockRule mocks = new org.easymock.EasyMockRule(this);

    @org.easymock.Mock(type = org.easymock.MockType.NICE)
    private org.apache.ambari.server.topology.AmbariContext ambariContext;

    @org.easymock.Mock(type = org.easymock.MockType.NICE)
    private org.apache.ambari.server.topology.Blueprint bp;

    @org.easymock.Mock(type = org.easymock.MockType.NICE)
    private org.apache.ambari.server.state.ServiceInfo serviceInfo;

    @org.easymock.Mock(type = org.easymock.MockType.NICE)
    private org.apache.ambari.server.controller.internal.Stack stack;

    @org.easymock.Mock(type = org.easymock.MockType.NICE)
    private org.apache.ambari.server.controller.AmbariManagementController controller;

    @org.easymock.Mock(type = org.easymock.MockType.NICE)
    private org.apache.ambari.server.controller.KerberosHelper kerberosHelper;

    @org.easymock.Mock(type = org.easymock.MockType.NICE)
    private org.apache.ambari.server.state.kerberos.KerberosDescriptor kerberosDescriptor;

    @org.easymock.Mock(type = org.easymock.MockType.NICE)
    private org.apache.ambari.server.state.Clusters clusters;

    @org.easymock.Mock(type = org.easymock.MockType.NICE)
    private org.apache.ambari.server.state.Cluster cluster;

    @org.easymock.Mock
    private org.apache.ambari.server.topology.TopologyRequest topologyRequestMock;

    @org.easymock.Mock(type = org.easymock.MockType.NICE)
    private org.apache.ambari.server.state.ConfigHelper configHelper;

    @org.junit.Before
    public void init() throws java.lang.Exception {
        EasyMock.expect(bp.getStack()).andReturn(stack).anyTimes();
        EasyMock.expect(bp.getName()).andReturn("test-bp").anyTimes();
        EasyMock.expect(bp.getServiceInfos()).andReturn(java.util.Collections.emptyList()).anyTimes();
        EasyMock.expect(stack.getName()).andReturn(STACK_NAME).atLeastOnce();
        EasyMock.expect(stack.getVersion()).andReturn(STACK_VERSION).atLeastOnce();
        EasyMock.expect(stack.isMasterComponent(((java.lang.String) (EasyMock.anyObject())))).andReturn(false).anyTimes();
        EasyMock.expect(stack.getConfigurationPropertiesWithMetadata(EasyMock.anyObject(java.lang.String.class), EasyMock.anyObject(java.lang.String.class))).andReturn(java.util.Collections.emptyMap()).anyTimes();
        EasyMock.expect(stack.getConfiguration()).andReturn(org.apache.ambari.server.topology.Configuration.newEmpty()).anyTimes();
        EasyMock.expect(serviceInfo.getRequiredProperties()).andReturn(java.util.Collections.emptyMap()).anyTimes();
        EasyMock.expect(serviceInfo.getRequiredServices()).andReturn(java.util.Collections.emptyList()).anyTimes();
        java.util.Collection<java.lang.String> hdfsComponents = new java.util.HashSet<>();
        hdfsComponents.add("NAMENODE");
        hdfsComponents.add("SECONDARY_NAMENODE");
        hdfsComponents.add("DATANODE");
        hdfsComponents.add("HDFS_CLIENT");
        serviceComponents.put("HDFS", hdfsComponents);
        java.util.Collection<java.lang.String> yarnComponents = new java.util.HashSet<>();
        yarnComponents.add("RESOURCEMANAGER");
        yarnComponents.add("NODEMANAGER");
        yarnComponents.add("YARN_CLIENT");
        yarnComponents.add("APP_TIMELINE_SERVER");
        yarnComponents.add("TIMELINE_READER");
        serviceComponents.put("YARN", yarnComponents);
        java.util.Collection<java.lang.String> mrComponents = new java.util.HashSet<>();
        mrComponents.add("MAPREDUCE2_CLIENT");
        mrComponents.add("HISTORY_SERVER");
        serviceComponents.put("MAPREDUCE2", mrComponents);
        java.util.Collection<java.lang.String> zkComponents = new java.util.HashSet<>();
        zkComponents.add("ZOOKEEPER_SERVER");
        zkComponents.add("ZOOKEEPER_CLIENT");
        serviceComponents.put("ZOOKEEPER", zkComponents);
        java.util.Collection<java.lang.String> hiveComponents = new java.util.HashSet<>();
        hiveComponents.add("MYSQL_SERVER");
        hiveComponents.add("HIVE_METASTORE");
        hiveComponents.add("HIVE_SERVER");
        serviceComponents.put("HIVE", hiveComponents);
        java.util.Collection<java.lang.String> falconComponents = new java.util.HashSet<>();
        falconComponents.add("FALCON_SERVER");
        falconComponents.add("FALCON_CLIENT");
        serviceComponents.put("FALCON", falconComponents);
        java.util.Collection<java.lang.String> gangliaComponents = new java.util.HashSet<>();
        gangliaComponents.add("GANGLIA_SERVER");
        gangliaComponents.add("GANGLIA_CLIENT");
        serviceComponents.put("GANGLIA", gangliaComponents);
        java.util.Collection<java.lang.String> kafkaComponents = new java.util.HashSet<>();
        kafkaComponents.add("KAFKA_BROKER");
        serviceComponents.put("KAFKA", kafkaComponents);
        java.util.Collection<java.lang.String> knoxComponents = new java.util.HashSet<>();
        knoxComponents.add("KNOX_GATEWAY");
        serviceComponents.put("KNOX", knoxComponents);
        java.util.Collection<java.lang.String> oozieComponents = new java.util.HashSet<>();
        oozieComponents.add("OOZIE_SERVER");
        oozieComponents.add("OOZIE_CLIENT");
        serviceComponents.put("OOZIE", oozieComponents);
        java.util.Collection<java.lang.String> hbaseComponents = new java.util.HashSet<>();
        hbaseComponents.add("HBASE_MASTER");
        serviceComponents.put("HBASE", hbaseComponents);
        java.util.Collection<java.lang.String> atlasComponents = new java.util.HashSet<>();
        atlasComponents.add("ATLAS_SERVER");
        atlasComponents.add("ATLAS_CLIENT");
        serviceComponents.put("ATLAS", atlasComponents);
        java.util.Collection<java.lang.String> amsComponents = new java.util.HashSet<>();
        amsComponents.add("METRICS_COLLECTOR");
        serviceComponents.put("AMBARI_METRICS", amsComponents);
        java.util.Collection<java.lang.String> stormComponents = new java.util.HashSet<>();
        stormComponents.add("NIMBUS");
        serviceComponents.put("STORM", stormComponents);
        java.util.Collection<java.lang.String> rangerComponents = new java.util.HashSet<>();
        rangerComponents.add("RANGER_ADMIN");
        rangerComponents.add("RANGER_USERSYNC");
        rangerComponents.add("RANGER_TAGSYNC");
        serviceComponents.put("RANGER", rangerComponents);
        for (java.util.Map.Entry<java.lang.String, java.util.Collection<java.lang.String>> entry : serviceComponents.entrySet()) {
            java.lang.String service = entry.getKey();
            for (java.lang.String component : entry.getValue()) {
                EasyMock.expect(stack.getServiceForComponent(component)).andReturn(service).anyTimes();
            }
        }
        EasyMock.expect(stack.getCardinality("MYSQL_SERVER")).andReturn(new org.apache.ambari.server.topology.Cardinality("0-1")).anyTimes();
        java.util.Set<java.lang.String> emptySet = java.util.Collections.emptySet();
        EasyMock.expect(stack.getExcludedConfigurationTypes(EasyMock.anyObject(java.lang.String.class))).andReturn(emptySet).anyTimes();
        EasyMock.expect(ambariContext.getConfigHelper()).andReturn(configHelper).anyTimes();
        EasyMock.expect(configHelper.getDefaultStackProperties(org.easymock.EasyMock.eq(new org.apache.ambari.server.state.StackId(STACK_NAME, STACK_VERSION)))).andReturn(stackProperties).anyTimes();
        stackProperties.put(org.apache.ambari.server.state.ConfigHelper.CLUSTER_ENV, defaultClusterEnvProperties);
        EasyMock.expect(ambariContext.isClusterKerberosEnabled(1)).andReturn(true).once();
        EasyMock.expect(ambariContext.getClusterName(1L)).andReturn("clusterName").anyTimes();
        org.powermock.api.easymock.PowerMock.mockStatic(org.apache.ambari.server.controller.AmbariServer.class);
        EasyMock.expect(org.apache.ambari.server.controller.AmbariServer.getController()).andReturn(controller).anyTimes();
        org.powermock.api.easymock.PowerMock.replay(org.apache.ambari.server.controller.AmbariServer.class);
        EasyMock.expect(clusters.getCluster("clusterName")).andReturn(cluster).anyTimes();
        EasyMock.expect(controller.getKerberosHelper()).andReturn(kerberosHelper).anyTimes();
        EasyMock.expect(controller.getClusters()).andReturn(clusters).anyTimes();
        EasyMock.expect(kerberosHelper.getKerberosDescriptor(cluster, false)).andReturn(kerberosDescriptor).anyTimes();
        java.util.Set<java.lang.String> properties = new java.util.HashSet<>();
        properties.add("core-site/hadoop.security.auth_to_local");
        EasyMock.expect(kerberosDescriptor.getAllAuthToLocalProperties()).andReturn(properties).anyTimes();
    }

    @org.junit.After
    public void tearDown() {
        EasyMock.reset(bp, serviceInfo, stack, ambariContext, configHelper);
    }

    @org.junit.Test
    public void testDoUpdateForBlueprintExport_SingleHostProperty() throws java.lang.Exception {
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> properties = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.lang.String> typeProps = new java.util.HashMap<>();
        typeProps.put("yarn.resourcemanager.hostname", "testhost");
        properties.put("yarn-site", typeProps);
        org.apache.ambari.server.topology.Configuration clusterConfig = new org.apache.ambari.server.topology.Configuration(properties, java.util.Collections.emptyMap());
        java.util.Collection<java.lang.String> hgComponents = new java.util.HashSet<>();
        hgComponents.add("NAMENODE");
        hgComponents.add("SECONDARY_NAMENODE");
        hgComponents.add("RESOURCEMANAGER");
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup group1 = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup("group1", hgComponents, java.util.Collections.singleton("testhost"));
        java.util.Collection<java.lang.String> hgComponents2 = new java.util.HashSet<>();
        hgComponents2.add("DATANODE");
        hgComponents2.add("HDFS_CLIENT");
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup group2 = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup("group2", hgComponents2, java.util.Collections.singleton("testhost2"));
        java.util.Collection<org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup> hostGroups = new java.util.HashSet<>();
        hostGroups.add(group1);
        hostGroups.add(group2);
        org.apache.ambari.server.topology.ClusterTopology topology = createClusterTopology(bp, clusterConfig, hostGroups);
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor configProcessor = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor(topology);
        configProcessor.doUpdateForBlueprintExport(org.apache.ambari.server.controller.internal.BlueprintExportType.FULL);
        java.lang.String updatedVal = properties.get("yarn-site").get("yarn.resourcemanager.hostname");
        org.junit.Assert.assertEquals("%HOSTGROUP::group1%", updatedVal);
    }

    @org.junit.Test
    public void testDoUpdateForBlueprintExport_FilterProperties() throws java.lang.Exception {
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> properties = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.lang.String> kerberosEnvProps = new java.util.HashMap<>();
        kerberosEnvProps.put("admin_server_host", "test");
        kerberosEnvProps.put("kdc_hosts", "test");
        kerberosEnvProps.put("realm", "test");
        kerberosEnvProps.put("kdc_type", "test");
        kerberosEnvProps.put("ldap-url", "test");
        kerberosEnvProps.put("container_dn", "test");
        properties.put("kerberos-env", kerberosEnvProps);
        java.util.Map<java.lang.String, java.lang.String> krb5ConfProps = new java.util.HashMap<>();
        krb5ConfProps.put("domains", "test");
        properties.put("krb5-conf", krb5ConfProps);
        java.util.Map<java.lang.String, java.lang.String> tezSiteConfProps = new java.util.HashMap<>();
        tezSiteConfProps.put("tez.tez-ui.history-url.base", "test");
        properties.put("tez-site", tezSiteConfProps);
        org.apache.ambari.server.topology.Configuration clusterConfig = new org.apache.ambari.server.topology.Configuration(properties, java.util.Collections.emptyMap());
        java.util.Collection<java.lang.String> hgComponents = new java.util.HashSet<>();
        hgComponents.add("NAMENODE");
        hgComponents.add("SECONDARY_NAMENODE");
        hgComponents.add("RESOURCEMANAGER");
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup group1 = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup("group1", hgComponents, java.util.Collections.singleton("testhost"));
        java.util.Collection<java.lang.String> hgComponents2 = new java.util.HashSet<>();
        hgComponents2.add("DATANODE");
        hgComponents2.add("HDFS_CLIENT");
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup group2 = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup("group2", hgComponents2, java.util.Collections.singleton("testhost2"));
        java.util.Collection<org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup> hostGroups = new java.util.HashSet<>();
        hostGroups.add(group1);
        hostGroups.add(group2);
        org.apache.ambari.server.topology.ClusterTopology topology = createClusterTopology(bp, clusterConfig, hostGroups);
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor configProcessor = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor(topology);
        configProcessor.doUpdateForBlueprintExport(org.apache.ambari.server.controller.internal.BlueprintExportType.FULL);
        org.junit.Assert.assertEquals(properties.size(), 3);
        org.junit.Assert.assertEquals(((java.util.Map) (properties.get("kerberos-env"))).size(), 0);
        org.junit.Assert.assertEquals(((java.util.Map) (properties.get("krb5-conf"))).size(), 0);
        org.junit.Assert.assertEquals(((java.util.Map) (properties.get("tez-site"))).size(), 0);
    }

    @org.junit.Test
    public void testDoUpdateForBlueprintExportRangerHAPolicyMgrExternalUrlProperty() throws java.lang.Exception {
        java.util.Map<java.lang.String, java.lang.String> rangerAdminProperties = com.google.common.collect.Maps.newHashMap();
        rangerAdminProperties.put("DB_FLAVOR", "test_db_flavor");
        rangerAdminProperties.put("policymgr_external_url", "test_policymgr_external_url");
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> properties = com.google.common.collect.ImmutableMap.of("admin-properties", rangerAdminProperties);
        org.apache.ambari.server.topology.Configuration clusterConfig = new org.apache.ambari.server.topology.Configuration(properties, com.google.common.collect.ImmutableMap.of());
        java.util.Collection<java.lang.String> hostGroup1Components = com.google.common.collect.ImmutableSet.of("RANGER_ADMIN");
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup group1 = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup("group1", hostGroup1Components, java.util.Collections.singleton("testhost1"));
        java.util.Collection<java.lang.String> hostGroup2Components = com.google.common.collect.ImmutableSet.of("RANGER_ADMIN");
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup group2 = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup("group2", hostGroup2Components, java.util.Collections.singleton("testhost2"));
        java.util.Collection<org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup> hostGroups = com.google.common.collect.ImmutableSet.of(group1, group2);
        org.apache.ambari.server.topology.ClusterTopology topology = createClusterTopology(bp, clusterConfig, hostGroups);
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor configProcessor = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor(topology);
        configProcessor.doUpdateForBlueprintExport(org.apache.ambari.server.controller.internal.BlueprintExportType.FULL);
        org.junit.Assert.assertEquals("policymgr_external_url property's original value should be exported when Ranger Admin is deployed to multiple hosts.", "test_policymgr_external_url", properties.get("admin-properties").get("policymgr_external_url"));
    }

    @org.junit.Test
    public void testDoUpdateForBlueprintExport_SingleHostProperty_specifiedInParentConfig() throws java.lang.Exception {
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> properties = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.lang.String> yarnSiteProps = new java.util.HashMap<>();
        yarnSiteProps.put("yarn.resourcemanager.hostname", "testhost");
        properties.put("yarn-site", yarnSiteProps);
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> parentProperties = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.lang.String> parentYarnSiteProps = new java.util.HashMap<>();
        parentYarnSiteProps.put("yarn.resourcemanager.resource-tracker.address", "testhost");
        parentProperties.put("yarn-site", parentYarnSiteProps);
        org.apache.ambari.server.topology.Configuration parentClusterConfig = new org.apache.ambari.server.topology.Configuration(parentProperties, java.util.Collections.emptyMap());
        org.apache.ambari.server.topology.Configuration clusterConfig = new org.apache.ambari.server.topology.Configuration(properties, java.util.Collections.emptyMap(), parentClusterConfig);
        java.util.Collection<java.lang.String> hgComponents = new java.util.HashSet<>();
        hgComponents.add("NAMENODE");
        hgComponents.add("SECONDARY_NAMENODE");
        hgComponents.add("RESOURCEMANAGER");
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup group1 = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup("group1", hgComponents, java.util.Collections.singleton("testhost"));
        java.util.Collection<java.lang.String> hgComponents2 = new java.util.HashSet<>();
        hgComponents2.add("DATANODE");
        hgComponents2.add("HDFS_CLIENT");
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup group2 = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup("group2", hgComponents2, java.util.Collections.singleton("testhost2"));
        java.util.Collection<org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup> hostGroups = new java.util.HashSet<>();
        hostGroups.add(group1);
        hostGroups.add(group2);
        org.apache.ambari.server.topology.ClusterTopology topology = createClusterTopology(bp, clusterConfig, hostGroups);
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor configProcessor = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor(topology);
        configProcessor.doUpdateForBlueprintExport(org.apache.ambari.server.controller.internal.BlueprintExportType.FULL);
        org.junit.Assert.assertEquals("%HOSTGROUP::group1%", clusterConfig.getPropertyValue("yarn-site", "yarn.resourcemanager.hostname"));
        org.junit.Assert.assertEquals("%HOSTGROUP::group1%", clusterConfig.getPropertyValue("yarn-site", "yarn.resourcemanager.resource-tracker.address"));
    }

    @org.junit.Test
    public void testDoUpdateForBlueprintExport_SingleHostProperty_hostGroupConfiguration() throws java.lang.Exception {
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> properties = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.lang.String> typeProps = new java.util.HashMap<>();
        typeProps.put("yarn.resourcemanager.hostname", "testhost");
        properties.put("yarn-site", typeProps);
        org.apache.ambari.server.topology.Configuration clusterConfig = new org.apache.ambari.server.topology.Configuration(properties, java.util.Collections.emptyMap());
        java.util.Collection<java.lang.String> hgComponents = new java.util.HashSet<>();
        hgComponents.add("NAMENODE");
        hgComponents.add("SECONDARY_NAMENODE");
        hgComponents.add("RESOURCEMANAGER");
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup group1 = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup("group1", hgComponents, java.util.Collections.singleton("testhost"));
        java.util.Collection<java.lang.String> hgComponents2 = new java.util.HashSet<>();
        hgComponents2.add("DATANODE");
        hgComponents2.add("HDFS_CLIENT");
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> group2Properties = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.lang.String> group2YarnSiteProps = new java.util.HashMap<>();
        group2YarnSiteProps.put("yarn.resourcemanager.resource-tracker.address", "testhost");
        group2YarnSiteProps.put("yarn.resourcemanager.webapp.https.address", "{{rm_host}}");
        group2Properties.put("yarn-site", group2YarnSiteProps);
        org.apache.ambari.server.topology.Configuration group2BPConfiguration = new org.apache.ambari.server.topology.Configuration(java.util.Collections.emptyMap(), java.util.Collections.emptyMap(), clusterConfig);
        org.apache.ambari.server.topology.Configuration group2Configuration = new org.apache.ambari.server.topology.Configuration(group2Properties, java.util.Collections.emptyMap(), group2BPConfiguration);
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup group2 = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup("group2", hgComponents2, java.util.Collections.singleton("testhost2"), group2Configuration);
        java.util.Collection<org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup> hostGroups = new java.util.HashSet<>();
        hostGroups.add(group1);
        hostGroups.add(group2);
        org.apache.ambari.server.topology.ClusterTopology topology = createClusterTopology(bp, clusterConfig, hostGroups);
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor configProcessor = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor(topology);
        configProcessor.doUpdateForBlueprintExport(org.apache.ambari.server.controller.internal.BlueprintExportType.FULL);
        org.junit.Assert.assertEquals("%HOSTGROUP::group1%", properties.get("yarn-site").get("yarn.resourcemanager.hostname"));
        org.junit.Assert.assertEquals("%HOSTGROUP::group1%", group2Configuration.getPropertyValue("yarn-site", "yarn.resourcemanager.resource-tracker.address"));
        org.junit.Assert.assertNotNull("Placeholder property should not have been removed.", group2Configuration.getPropertyValue("yarn-site", "yarn.resourcemanager.webapp.https.address"));
    }

    @org.junit.Test
    public void testDoUpdateForBlueprintExport_SingleHostProperty__withPort() throws java.lang.Exception {
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> properties = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.lang.String> typeProps = new java.util.HashMap<>();
        typeProps.put("fs.defaultFS", "testhost:8020");
        properties.put("core-site", typeProps);
        org.apache.ambari.server.topology.Configuration clusterConfig = new org.apache.ambari.server.topology.Configuration(properties, java.util.Collections.emptyMap());
        java.util.Collection<java.lang.String> hgComponents = new java.util.HashSet<>();
        hgComponents.add("NAMENODE");
        hgComponents.add("SECONDARY_NAMENODE");
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup group1 = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup("group1", hgComponents, java.util.Collections.singleton("testhost"));
        java.util.Collection<java.lang.String> hgComponents2 = new java.util.HashSet<>();
        hgComponents2.add("DATANODE");
        hgComponents2.add("HDFS_CLIENT");
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup group2 = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup("group2", hgComponents2, java.util.Collections.singleton("testhost2"));
        java.util.Collection<org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup> hostGroups = new java.util.HashSet<>();
        hostGroups.add(group1);
        hostGroups.add(group2);
        org.apache.ambari.server.topology.ClusterTopology topology = createClusterTopology(bp, clusterConfig, hostGroups);
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor configProcessor = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor(topology);
        configProcessor.doUpdateForBlueprintExport(org.apache.ambari.server.controller.internal.BlueprintExportType.FULL);
        java.lang.String updatedVal = properties.get("core-site").get("fs.defaultFS");
        org.junit.Assert.assertEquals("%HOSTGROUP::group1%:8020", updatedVal);
    }

    @org.junit.Test
    public void testDoUpdateForBlueprintExport_SingleHostProperty__ExternalReference() throws java.lang.Exception {
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> properties = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.lang.String> typeProps = new java.util.HashMap<>();
        typeProps.put("yarn.resourcemanager.hostname", "external-host");
        properties.put("yarn-site", typeProps);
        org.apache.ambari.server.topology.Configuration clusterConfig = new org.apache.ambari.server.topology.Configuration(properties, java.util.Collections.emptyMap());
        java.util.Collection<java.lang.String> hgComponents = new java.util.HashSet<>();
        hgComponents.add("NAMENODE");
        hgComponents.add("SECONDARY_NAMENODE");
        hgComponents.add("RESOURCEMANAGER");
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup group1 = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup("group1", hgComponents, java.util.Collections.singleton("testhost"));
        java.util.Collection<java.lang.String> hgComponents2 = new java.util.HashSet<>();
        hgComponents2.add("DATANODE");
        hgComponents2.add("HDFS_CLIENT");
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup group2 = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup("group2", hgComponents2, java.util.Collections.singleton("testhost2"));
        java.util.Collection<org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup> hostGroups = new java.util.HashSet<>();
        hostGroups.add(group1);
        hostGroups.add(group2);
        org.apache.ambari.server.topology.ClusterTopology topology = createClusterTopology(bp, clusterConfig, hostGroups);
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor configProcessor = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor(topology);
        configProcessor.doUpdateForBlueprintExport(org.apache.ambari.server.controller.internal.BlueprintExportType.FULL);
        org.junit.Assert.assertFalse(properties.get("yarn-site").containsKey("yarn.resourcemanager.hostname"));
    }

    @org.junit.Test
    public void testDoUpdateForBlueprintExport_MultiHostProperty() throws java.lang.Exception {
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> properties = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.lang.String> typeProps = new java.util.HashMap<>();
        typeProps.put("hbase.zookeeper.quorum", "testhost,testhost2,testhost2a,testhost2b");
        properties.put("hbase-site", typeProps);
        org.apache.ambari.server.topology.Configuration clusterConfig = new org.apache.ambari.server.topology.Configuration(properties, java.util.Collections.emptyMap());
        java.util.Collection<java.lang.String> hgComponents = new java.util.HashSet<>();
        hgComponents.add("NAMENODE");
        hgComponents.add("SECONDARY_NAMENODE");
        hgComponents.add("ZOOKEEPER_SERVER");
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup group1 = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup("group1", hgComponents, java.util.Collections.singleton("testhost"));
        java.util.Collection<java.lang.String> hgComponents2 = new java.util.HashSet<>();
        hgComponents2.add("DATANODE");
        hgComponents2.add("HDFS_CLIENT");
        hgComponents2.add("ZOOKEEPER_SERVER");
        java.util.Set<java.lang.String> hosts2 = new java.util.HashSet<>();
        hosts2.add("testhost2");
        hosts2.add("testhost2a");
        hosts2.add("testhost2b");
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup group2 = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup("group2", hgComponents2, hosts2);
        java.util.Collection<java.lang.String> hgComponents3 = new java.util.HashSet<>();
        hgComponents2.add("HDFS_CLIENT");
        hgComponents2.add("ZOOKEEPER_CLIENT");
        java.util.Set<java.lang.String> hosts3 = new java.util.HashSet<>();
        hosts3.add("testhost3");
        hosts3.add("testhost3a");
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup group3 = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup("group3", hgComponents3, hosts3);
        java.util.Collection<org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup> hostGroups = new java.util.HashSet<>();
        hostGroups.add(group1);
        hostGroups.add(group2);
        hostGroups.add(group3);
        org.apache.ambari.server.topology.ClusterTopology topology = createClusterTopology(bp, clusterConfig, hostGroups);
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor configProcessor = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor(topology);
        configProcessor.doUpdateForBlueprintExport(org.apache.ambari.server.controller.internal.BlueprintExportType.FULL);
        java.lang.String updatedVal = properties.get("hbase-site").get("hbase.zookeeper.quorum");
        org.junit.Assert.assertEquals("%HOSTGROUP::group1%,%HOSTGROUP::group2%", updatedVal);
    }

    @org.junit.Test
    public void testDoUpdateForBlueprintExport_MultiHostProperty__WithPorts() throws java.lang.Exception {
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> properties = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.lang.String> typeProps = new java.util.HashMap<>();
        typeProps.put("templeton.zookeeper.hosts", "testhost:5050,testhost2:9090,testhost2a:9090,testhost2b:9090");
        properties.put("webhcat-site", typeProps);
        org.apache.ambari.server.topology.Configuration clusterConfig = new org.apache.ambari.server.topology.Configuration(properties, java.util.Collections.emptyMap());
        java.util.Collection<java.lang.String> hgComponents = new java.util.HashSet<>();
        hgComponents.add("NAMENODE");
        hgComponents.add("SECONDARY_NAMENODE");
        hgComponents.add("ZOOKEEPER_SERVER");
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup group1 = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup("group1", hgComponents, java.util.Collections.singleton("testhost"));
        java.util.Collection<java.lang.String> hgComponents2 = new java.util.HashSet<>();
        hgComponents2.add("DATANODE");
        hgComponents2.add("HDFS_CLIENT");
        hgComponents2.add("ZOOKEEPER_SERVER");
        java.util.Set<java.lang.String> hosts2 = new java.util.HashSet<>();
        hosts2.add("testhost2");
        hosts2.add("testhost2a");
        hosts2.add("testhost2b");
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup group2 = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup("group2", hgComponents2, hosts2);
        java.util.Collection<java.lang.String> hgComponents3 = new java.util.HashSet<>();
        hgComponents2.add("HDFS_CLIENT");
        hgComponents2.add("ZOOKEEPER_CLIENT");
        java.util.Set<java.lang.String> hosts3 = new java.util.HashSet<>();
        hosts3.add("testhost3");
        hosts3.add("testhost3a");
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup group3 = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup("group3", hgComponents3, hosts3);
        java.util.Collection<org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup> hostGroups = new java.util.HashSet<>();
        hostGroups.add(group1);
        hostGroups.add(group2);
        hostGroups.add(group3);
        org.apache.ambari.server.topology.ClusterTopology topology = createClusterTopology(bp, clusterConfig, hostGroups);
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor configProcessor = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor(topology);
        configProcessor.doUpdateForBlueprintExport(org.apache.ambari.server.controller.internal.BlueprintExportType.FULL);
        java.lang.String updatedVal = properties.get("webhcat-site").get("templeton.zookeeper.hosts");
        org.junit.Assert.assertEquals("%HOSTGROUP::group1%:5050,%HOSTGROUP::group2%:9090", updatedVal);
    }

    @org.junit.Test
    public void testDoUpdateForBlueprintExport_MultiHostProperty__WithPrefixAndPorts() throws java.lang.Exception {
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> properties = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.lang.String> typeProps = new java.util.HashMap<>();
        typeProps.put("atlas.server.bind.address", "http://testhost:21000,http://testhost2:21000,http://testhost2a:21000,http://testhost2b:21000");
        properties.put("application-properties", typeProps);
        org.apache.ambari.server.topology.Configuration clusterConfig = new org.apache.ambari.server.topology.Configuration(properties, java.util.Collections.emptyMap());
        java.util.Collection<java.lang.String> hgComponents = com.google.common.collect.Sets.newHashSet("NAMENODE", "SECONDARY_NAMENODE", "ZOOKEEPER_SERVER");
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup group1 = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup("group1", hgComponents, java.util.Collections.singleton("testhost"));
        java.util.Collection<java.lang.String> hgComponents2 = com.google.common.collect.Sets.newHashSet("DATANODE", "HDFS_CLIENT", "ZOOKEEPER_SERVER");
        java.util.Set<java.lang.String> hosts2 = com.google.common.collect.Sets.newHashSet("testhost2", "testhost2a", "testhost2b");
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup group2 = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup("group2", hgComponents2, hosts2);
        java.util.Collection<java.lang.String> hgComponents3 = com.google.common.collect.Sets.newHashSet("HDFS_CLIENT", "ZOOKEEPER_CLIENT");
        java.util.Set<java.lang.String> hosts3 = com.google.common.collect.Sets.newHashSet("testhost3", "testhost3a");
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup group3 = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup("group3", hgComponents3, hosts3);
        java.util.Collection<org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup> hostGroups = com.google.common.collect.Sets.newHashSet(group1, group2, group3);
        org.apache.ambari.server.topology.ClusterTopology topology = createClusterTopology(bp, clusterConfig, hostGroups);
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor configProcessor = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor(topology);
        configProcessor.doUpdateForBlueprintExport(org.apache.ambari.server.controller.internal.BlueprintExportType.FULL);
        java.lang.String updatedVal = properties.get("application-properties").get("atlas.server.bind.address");
        org.junit.Assert.assertEquals("http://%HOSTGROUP::group1%:21000,http://%HOSTGROUP::group2%:21000", updatedVal);
    }

    @org.junit.Test
    public void testDoUpdateForBlueprintExport_MultiHostProperty__YAML() throws java.lang.Exception {
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> properties = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.lang.String> typeProps = new java.util.HashMap<>();
        typeProps.put("storm.zookeeper.servers", "['testhost:5050','testhost2:9090','testhost2a:9090','testhost2b:9090']");
        typeProps.put("drpc_server_host", "['testhost:5050']");
        typeProps.put("storm_ui_server_host", "['testhost:5050']");
        typeProps.put("supervisor_hosts", "['testhost:5050','testhost2:9090']");
        properties.put("storm-site", typeProps);
        org.apache.ambari.server.topology.Configuration clusterConfig = new org.apache.ambari.server.topology.Configuration(properties, java.util.Collections.emptyMap());
        java.util.Collection<java.lang.String> hgComponents = new java.util.HashSet<>();
        hgComponents.add("NAMENODE");
        hgComponents.add("SECONDARY_NAMENODE");
        hgComponents.add("ZOOKEEPER_SERVER");
        hgComponents.add("DRPC_SERVER");
        hgComponents.add("STORM_UI_SERVER");
        hgComponents.add("SUPERVISOR");
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup group1 = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup("group1", hgComponents, java.util.Collections.singleton("testhost"));
        java.util.Collection<java.lang.String> hgComponents2 = new java.util.HashSet<>();
        hgComponents2.add("DATANODE");
        hgComponents2.add("HDFS_CLIENT");
        hgComponents2.add("ZOOKEEPER_SERVER");
        hgComponents2.add("SUPERVISOR");
        java.util.Set<java.lang.String> hosts2 = new java.util.HashSet<>();
        hosts2.add("testhost2");
        hosts2.add("testhost2a");
        hosts2.add("testhost2b");
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup group2 = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup("group2", hgComponents2, hosts2);
        java.util.Collection<java.lang.String> hgComponents3 = new java.util.HashSet<>();
        hgComponents2.add("HDFS_CLIENT");
        hgComponents2.add("ZOOKEEPER_CLIENT");
        java.util.Set<java.lang.String> hosts3 = new java.util.HashSet<>();
        hosts3.add("testhost3");
        hosts3.add("testhost3a");
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup group3 = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup("group3", hgComponents3, hosts3);
        java.util.Collection<org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup> hostGroups = new java.util.HashSet<>();
        hostGroups.add(group1);
        hostGroups.add(group2);
        hostGroups.add(group3);
        org.apache.ambari.server.topology.ClusterTopology topology = createClusterTopology(bp, clusterConfig, hostGroups);
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor configProcessor = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor(topology);
        configProcessor.doUpdateForBlueprintExport(org.apache.ambari.server.controller.internal.BlueprintExportType.FULL);
        java.lang.String updatedVal = properties.get("storm-site").get("storm.zookeeper.servers");
        org.junit.Assert.assertEquals("['%HOSTGROUP::group1%:5050','%HOSTGROUP::group2%:9090']", updatedVal);
        java.lang.String updatedVa2 = properties.get("storm-site").get("drpc_server_host");
        org.junit.Assert.assertEquals("['%HOSTGROUP::group1%:5050']", updatedVa2);
        java.lang.String updatedVa3 = properties.get("storm-site").get("storm_ui_server_host");
        org.junit.Assert.assertEquals("['%HOSTGROUP::group1%:5050']", updatedVa3);
        java.lang.String updatedVa4 = properties.get("storm-site").get("supervisor_hosts");
        org.junit.Assert.assertEquals("['%HOSTGROUP::group1%:5050','%HOSTGROUP::group2%:9090']", updatedVa4);
    }

    @org.junit.Test
    public void testDoUpdateForBlueprintExport_DBHostProperty() throws java.lang.Exception {
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> properties = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.lang.String> hiveSiteProps = new java.util.HashMap<>();
        hiveSiteProps.put("javax.jdo.option.ConnectionURL", "jdbc:mysql://testhost/hive?createDatabaseIfNotExist=true");
        properties.put("hive-site", hiveSiteProps);
        org.apache.ambari.server.topology.Configuration clusterConfig = new org.apache.ambari.server.topology.Configuration(properties, java.util.Collections.emptyMap());
        java.util.Collection<java.lang.String> hgComponents = new java.util.HashSet<>();
        hgComponents.add("NAMENODE");
        hgComponents.add("SECONDARY_NAMENODE");
        hgComponents.add("RESOURCEMANAGER");
        hgComponents.add("MYSQL_SERVER");
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup group1 = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup("group1", hgComponents, java.util.Collections.singleton("testhost"));
        java.util.Collection<java.lang.String> hgComponents2 = new java.util.HashSet<>();
        hgComponents2.add("DATANODE");
        hgComponents2.add("HDFS_CLIENT");
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup group2 = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup("group2", hgComponents2, java.util.Collections.singleton("testhost2"));
        java.util.Collection<org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup> hostGroups = new java.util.HashSet<>();
        hostGroups.add(group1);
        hostGroups.add(group2);
        org.apache.ambari.server.topology.ClusterTopology topology = createClusterTopology(bp, clusterConfig, hostGroups);
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor configProcessor = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor(topology);
        configProcessor.doUpdateForBlueprintExport(org.apache.ambari.server.controller.internal.BlueprintExportType.FULL);
        java.lang.String updatedVal = properties.get("hive-site").get("javax.jdo.option.ConnectionURL");
        org.junit.Assert.assertEquals("jdbc:mysql://%HOSTGROUP::group1%/hive?createDatabaseIfNotExist=true", updatedVal);
    }

    @org.junit.Test
    public void testDoUpdateForBlueprintExport_DBHostProperty__External() throws java.lang.Exception {
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> properties = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.lang.String> typeProps = new java.util.HashMap<>();
        typeProps.put("javax.jdo.option.ConnectionURL", "jdbc:mysql://external-host/hive?createDatabaseIfNotExist=true");
        properties.put("hive-site", typeProps);
        org.apache.ambari.server.topology.Configuration clusterConfig = new org.apache.ambari.server.topology.Configuration(properties, java.util.Collections.emptyMap());
        java.util.Collection<java.lang.String> hgComponents = new java.util.HashSet<>();
        hgComponents.add("NAMENODE");
        hgComponents.add("SECONDARY_NAMENODE");
        hgComponents.add("RESOURCEMANAGER");
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup group1 = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup("group1", hgComponents, java.util.Collections.singleton("testhost"));
        java.util.Collection<java.lang.String> hgComponents2 = new java.util.HashSet<>();
        hgComponents2.add("DATANODE");
        hgComponents2.add("HDFS_CLIENT");
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup group2 = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup("group2", hgComponents2, java.util.Collections.singleton("testhost2"));
        java.util.Collection<org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup> hostGroups = new java.util.HashSet<>();
        hostGroups.add(group1);
        hostGroups.add(group2);
        org.apache.ambari.server.topology.ClusterTopology topology = createClusterTopology(bp, clusterConfig, hostGroups);
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor configProcessor = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor(topology);
        configProcessor.doUpdateForBlueprintExport(org.apache.ambari.server.controller.internal.BlueprintExportType.FULL);
        org.junit.Assert.assertFalse(properties.get("hive-site").containsKey("javax.jdo.option.ConnectionURL"));
    }

    @org.junit.Test
    public void testDoUpdateForBlueprintExport_PasswordFilterApplied() throws java.lang.Exception {
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> properties = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.lang.String> typeProps = new java.util.HashMap<>();
        typeProps.put("REPOSITORY_CONFIG_PASSWORD", "test-password-one");
        typeProps.put("SSL_KEYSTORE_PASSWORD", "test-password-two");
        typeProps.put("SSL_TRUSTSTORE_PASSWORD", "test-password-three");
        typeProps.put("XAAUDIT.DB.PASSWORD", "test-password-four");
        typeProps.put("test.ssl.password", "test-password-five");
        typeProps.put("test.password.should.be.included", "test-another-pwd");
        java.util.Map<java.lang.String, java.lang.String> secretProps = new java.util.HashMap<>();
        secretProps.put("knox_master_secret", "test-secret-one");
        secretProps.put("test.secret.should.be.included", "test-another-secret");
        java.util.Map<java.lang.String, java.lang.String> customProps = new java.util.HashMap<>();
        customProps.put("my_test_PASSWORD", "should be excluded");
        customProps.put("PASSWORD_mytest", "should be included");
        customProps.put("my_test_SECRET", "should be excluded");
        customProps.put("SECRET_mytest", "should be included");
        properties.put("ranger-yarn-plugin-properties", typeProps);
        properties.put("custom-test-properties", customProps);
        properties.put("secret-test-properties", secretProps);
        org.apache.ambari.server.topology.Configuration clusterConfig = new org.apache.ambari.server.topology.Configuration(properties, java.util.Collections.emptyMap());
        java.util.Collection<java.lang.String> hgComponents = new java.util.HashSet<>();
        hgComponents.add("NAMENODE");
        hgComponents.add("SECONDARY_NAMENODE");
        hgComponents.add("RESOURCEMANAGER");
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup group1 = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup("group1", hgComponents, java.util.Collections.singleton("testhost"));
        java.util.Collection<java.lang.String> hgComponents2 = new java.util.HashSet<>();
        hgComponents2.add("DATANODE");
        hgComponents2.add("HDFS_CLIENT");
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup group2 = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup("group2", hgComponents2, java.util.Collections.singleton("testhost2"));
        java.util.Collection<org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup> hostGroups = new java.util.HashSet<>();
        hostGroups.add(group1);
        hostGroups.add(group2);
        org.apache.ambari.server.topology.ClusterTopology topology = createClusterTopology(bp, clusterConfig, hostGroups);
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor configProcessor = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor(topology);
        configProcessor.doUpdateForBlueprintExport(org.apache.ambari.server.controller.internal.BlueprintExportType.FULL);
        org.junit.Assert.assertEquals("Exported properties map was not of the expected size", 2, properties.get("custom-test-properties").size());
        org.junit.Assert.assertEquals("ranger-yarn-plugin-properties config type was not properly exported", 1, properties.get("ranger-yarn-plugin-properties").size());
        org.junit.Assert.assertEquals("Exported secret properties map was not of the expected size", 1, properties.get("secret-test-properties").size());
        org.junit.Assert.assertFalse("Password property should have been excluded", properties.get("ranger-yarn-plugin-properties").containsKey("REPOSITORY_CONFIG_PASSWORD"));
        org.junit.Assert.assertFalse("Password property should have been excluded", properties.get("ranger-yarn-plugin-properties").containsKey("SSL_KEYSTORE_PASSWORD"));
        org.junit.Assert.assertFalse("Password property should have been excluded", properties.get("ranger-yarn-plugin-properties").containsKey("SSL_TRUSTSTORE_PASSWORD"));
        org.junit.Assert.assertFalse("Password property should have been excluded", properties.get("ranger-yarn-plugin-properties").containsKey("XAAUDIT.DB.PASSWORD"));
        org.junit.Assert.assertFalse("Password property should have been excluded", properties.get("ranger-yarn-plugin-properties").containsKey("test.ssl.password"));
        org.junit.Assert.assertTrue("Expected password property not found", properties.get("ranger-yarn-plugin-properties").containsKey("test.password.should.be.included"));
        org.junit.Assert.assertFalse("Secret property should have been excluded", properties.get("secret-test-properties").containsKey("knox_master_secret"));
        org.junit.Assert.assertTrue("Expected secret property not found", properties.get("secret-test-properties").containsKey("test.secret.should.be.included"));
        org.junit.Assert.assertEquals("custom-test-properties type was not properly exported", 2, properties.get("custom-test-properties").size());
        org.junit.Assert.assertFalse("Password property should have been excluded", properties.get("custom-test-properties").containsKey("my_test_PASSWORD"));
        org.junit.Assert.assertTrue("Expected password property not found", properties.get("custom-test-properties").containsKey("PASSWORD_mytest"));
        org.junit.Assert.assertEquals("Expected password property should not have been modified", "should be included", properties.get("custom-test-properties").get("PASSWORD_mytest"));
    }

    @org.junit.Test
    public void testFalconConfigExport() throws java.lang.Exception {
        final java.lang.String expectedHostName = "c6401.apache.ambari.org";
        final java.lang.String expectedPortNum = "808080";
        final java.lang.String expectedHostGroupName = "host_group_1";
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> configProperties = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.lang.String> falconStartupProperties = new java.util.HashMap<>();
        configProperties.put("falcon-startup.properties", falconStartupProperties);
        falconStartupProperties.put("*.broker.url", (expectedHostName + ":") + expectedPortNum);
        falconStartupProperties.put("*.falcon.service.authentication.kerberos.principal", ("falcon/" + expectedHostName) + "@EXAMPLE.COM");
        falconStartupProperties.put("*.falcon.http.authentication.kerberos.principal", ("HTTP/" + expectedHostName) + "@EXAMPLE.COM");
        org.apache.ambari.server.topology.Configuration clusterConfig = new org.apache.ambari.server.topology.Configuration(configProperties, java.util.Collections.emptyMap());
        java.util.Collection<java.lang.String> groupComponents = new java.util.HashSet<>();
        groupComponents.add("FALCON_SERVER");
        java.util.Collection<java.lang.String> hosts = new java.util.ArrayList<>();
        hosts.add(expectedHostName);
        hosts.add("serverTwo");
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup group = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup(expectedHostGroupName, groupComponents, hosts);
        java.util.Collection<org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup> hostGroups = new java.util.HashSet<>();
        hostGroups.add(group);
        org.apache.ambari.server.topology.ClusterTopology topology = createClusterTopology(bp, clusterConfig, hostGroups);
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor configProcessor = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor(topology);
        configProcessor.doUpdateForBlueprintExport(org.apache.ambari.server.controller.internal.BlueprintExportType.FULL);
        org.junit.Assert.assertEquals("Falcon Broker URL property not properly exported", org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.createExportedAddress(expectedPortNum, expectedHostGroupName), falconStartupProperties.get("*.broker.url"));
    }

    @org.junit.Test
    public void testTezConfigExport() throws java.lang.Exception {
        final java.lang.String expectedHostName = "c6401.apache.ambari.org";
        final java.lang.String expectedHostGroupName = "host_group_1";
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> configProperties = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.lang.String> tezSiteProperties = new java.util.HashMap<>();
        configProperties.put("tez-site", tezSiteProperties);
        tezSiteProperties.put("tez.tez-ui.history-url.base", "http://host:port/TEZ/TEZ_VIEW");
        org.apache.ambari.server.topology.Configuration clusterConfig = new org.apache.ambari.server.topology.Configuration(configProperties, java.util.Collections.emptyMap());
        java.util.Collection<java.lang.String> groupComponents = new java.util.HashSet<>();
        groupComponents.add("TEZ_CLIENT");
        java.util.Collection<java.lang.String> hosts = new java.util.ArrayList<>();
        hosts.add(expectedHostName);
        hosts.add("serverTwo");
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup group = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup(expectedHostGroupName, groupComponents, hosts);
        java.util.Collection<org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup> hostGroups = new java.util.HashSet<>();
        hostGroups.add(group);
        org.apache.ambari.server.topology.ClusterTopology topology = createClusterTopology(bp, clusterConfig, hostGroups);
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor configProcessor = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor(topology);
        configProcessor.doUpdateForBlueprintExport(org.apache.ambari.server.controller.internal.BlueprintExportType.FULL);
        org.junit.Assert.assertFalse("tez.tez-ui.history-url.base should not be present in exported blueprint in tez-site", tezSiteProperties.containsKey("tez.tez-ui.history-url.base"));
    }

    @org.junit.Test
    public void testKerberosConfigExport() throws java.lang.Exception {
        final java.lang.String expectedHostName = "c6401.apache.ambari.org";
        final java.lang.String expectedHostGroupName = "host_group_1";
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> configProperties = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.lang.String> kerberosEnvProperties = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.lang.String> coreSiteProperties = new java.util.HashMap<>();
        configProperties.put("kerberos-env", kerberosEnvProperties);
        configProperties.put("core-site", coreSiteProperties);
        kerberosEnvProperties.put("admin_server_host", expectedHostName);
        kerberosEnvProperties.put("kdc_hosts", expectedHostName + ",secondary.kdc.org");
        kerberosEnvProperties.put("master_kdc", expectedHostName);
        coreSiteProperties.put("hadoop.proxyuser.yarn.hosts", expectedHostName);
        coreSiteProperties.put("hadoop.security.auth_to_local", "RULE:clustername");
        org.apache.ambari.server.topology.Configuration clusterConfig = new org.apache.ambari.server.topology.Configuration(configProperties, java.util.Collections.emptyMap());
        java.util.Collection<java.lang.String> groupComponents = new java.util.HashSet<>();
        groupComponents.add("TEZ_CLIENT");
        groupComponents.add("RESOURCEMANAGER");
        java.util.Collection<java.lang.String> hosts = new java.util.ArrayList<>();
        hosts.add(expectedHostName);
        hosts.add("serverTwo");
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup group = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup(expectedHostGroupName, groupComponents, hosts);
        java.util.Collection<org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup> hostGroups = new java.util.HashSet<>();
        hostGroups.add(group);
        org.apache.ambari.server.topology.ClusterTopology topology = createClusterTopology(bp, clusterConfig, hostGroups);
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor configProcessor = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor(topology);
        configProcessor.doUpdateForBlueprintExport(org.apache.ambari.server.controller.internal.BlueprintExportType.FULL);
        org.junit.Assert.assertFalse("admin_server_host should not be present in exported blueprint in kerberos-env", kerberosEnvProperties.containsKey("admin_server_host"));
        org.junit.Assert.assertFalse("kdc_hosts should not be present in exported blueprint in kerberos-env", kerberosEnvProperties.containsKey("kdc_hosts"));
        org.junit.Assert.assertFalse("master_kdc should not be present in exported blueprint in kerberos-env", kerberosEnvProperties.containsKey("master_kdc"));
        org.junit.Assert.assertEquals("hadoop.proxyuser.yarn.hosts was not exported correctly", org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.createExportedHostName("host_group_1"), coreSiteProperties.get("hadoop.proxyuser.yarn.hosts"));
        org.junit.Assert.assertFalse("hadoop.security.auth_to_local should not be present in exported blueprint in core-site", coreSiteProperties.containsKey("hadoop.security.auth_to_local"));
    }

    @org.junit.Test
    public void testDoNameNodeHighAvailabilityExportWithHAEnabled() throws java.lang.Exception {
        final java.lang.String expectedNameService = "mynameservice";
        final java.lang.String expectedHostName = "c6401.apache.ambari.org";
        final java.lang.String expectedPortNum = "808080";
        final java.lang.String expectedNodeOne = "nn1";
        final java.lang.String expectedNodeTwo = "nn2";
        final java.lang.String expectedHostGroupName = "host_group_1";
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> configProperties = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.lang.String> hdfsSiteProperties = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.lang.String> coreSiteProperties = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.lang.String> hbaseSiteProperties = new java.util.HashMap<>();
        configProperties.put("hdfs-site", hdfsSiteProperties);
        configProperties.put("core-site", coreSiteProperties);
        configProperties.put("hbase-site", hbaseSiteProperties);
        hdfsSiteProperties.put("dfs.internal.nameservices", expectedNameService);
        hdfsSiteProperties.put("dfs.nameservices", expectedNameService);
        hdfsSiteProperties.put("dfs.ha.namenodes.mynameservice", (expectedNodeOne + ", ") + expectedNodeTwo);
        hdfsSiteProperties.put((("dfs.namenode.https-address." + expectedNameService) + ".") + expectedNodeOne, (expectedHostName + ":") + expectedPortNum);
        hdfsSiteProperties.put((("dfs.namenode.https-address." + expectedNameService) + ".") + expectedNodeTwo, (expectedHostName + ":") + expectedPortNum);
        hdfsSiteProperties.put((("dfs.namenode.http-address." + expectedNameService) + ".") + expectedNodeOne, (expectedHostName + ":") + expectedPortNum);
        hdfsSiteProperties.put((("dfs.namenode.http-address." + expectedNameService) + ".") + expectedNodeTwo, (expectedHostName + ":") + expectedPortNum);
        hdfsSiteProperties.put((("dfs.namenode.rpc-address." + expectedNameService) + ".") + expectedNodeOne, (expectedHostName + ":") + expectedPortNum);
        hdfsSiteProperties.put((("dfs.namenode.rpc-address." + expectedNameService) + ".") + expectedNodeTwo, (expectedHostName + ":") + expectedPortNum);
        org.apache.ambari.server.topology.Configuration clusterConfig = new org.apache.ambari.server.topology.Configuration(configProperties, java.util.Collections.emptyMap());
        java.util.Collection<java.lang.String> groupComponents = new java.util.HashSet<>();
        groupComponents.add("NAMENODE");
        java.util.Collection<java.lang.String> hosts = new java.util.ArrayList<>();
        hosts.add(expectedHostName);
        hosts.add("serverTwo");
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup group = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup(expectedHostGroupName, groupComponents, hosts);
        java.util.Collection<org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup> hostGroups = new java.util.HashSet<>();
        hostGroups.add(group);
        org.apache.ambari.server.topology.ClusterTopology topology = createClusterTopology(bp, clusterConfig, hostGroups);
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor configProcessor = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor(topology);
        configProcessor.doUpdateForBlueprintExport(org.apache.ambari.server.controller.internal.BlueprintExportType.FULL);
        org.junit.Assert.assertEquals("HTTPS address HA property not properly exported", org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.createExportedAddress(expectedPortNum, expectedHostGroupName), hdfsSiteProperties.get((("dfs.namenode.https-address." + expectedNameService) + ".") + expectedNodeOne));
        org.junit.Assert.assertEquals("HTTPS address HA property not properly exported", org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.createExportedAddress(expectedPortNum, expectedHostGroupName), hdfsSiteProperties.get((("dfs.namenode.https-address." + expectedNameService) + ".") + expectedNodeTwo));
        org.junit.Assert.assertEquals("HTTPS address HA property not properly exported", org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.createExportedAddress(expectedPortNum, expectedHostGroupName), hdfsSiteProperties.get((("dfs.namenode.http-address." + expectedNameService) + ".") + expectedNodeOne));
        org.junit.Assert.assertEquals("HTTPS address HA property not properly exported", org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.createExportedAddress(expectedPortNum, expectedHostGroupName), hdfsSiteProperties.get((("dfs.namenode.http-address." + expectedNameService) + ".") + expectedNodeTwo));
        org.junit.Assert.assertEquals("HTTPS address HA property not properly exported", org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.createExportedAddress(expectedPortNum, expectedHostGroupName), hdfsSiteProperties.get((("dfs.namenode.rpc-address." + expectedNameService) + ".") + expectedNodeOne));
        org.junit.Assert.assertEquals("HTTPS address HA property not properly exported", org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.createExportedAddress(expectedPortNum, expectedHostGroupName), hdfsSiteProperties.get((("dfs.namenode.rpc-address." + expectedNameService) + ".") + expectedNodeTwo));
    }

    @org.junit.Test
    public void testDoNameNodeHighAvailabilityExportWithHAEnabledPrimaryNamePreferenceNotExported() throws java.lang.Exception {
        final java.lang.String expectedNameService = "mynameservice";
        final java.lang.String expectedHostName = "c6401.apache.ambari.org";
        final java.lang.String expectedPortNum = "808080";
        final java.lang.String expectedNodeOne = "nn1";
        final java.lang.String expectedNodeTwo = "nn2";
        final java.lang.String expectedHostGroupName = "host_group_1";
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> configProperties = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.lang.String> hdfsSiteProperties = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.lang.String> coreSiteProperties = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.lang.String> hbaseSiteProperties = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.lang.String> hadoopEnvProperties = new java.util.HashMap<>();
        configProperties.put("hdfs-site", hdfsSiteProperties);
        configProperties.put("core-site", coreSiteProperties);
        configProperties.put("hbase-site", hbaseSiteProperties);
        configProperties.put("hadoop-env", hadoopEnvProperties);
        hdfsSiteProperties.put("dfs.internal.nameservices", expectedNameService);
        hdfsSiteProperties.put("dfs.nameservices", expectedNameService);
        hdfsSiteProperties.put("dfs.ha.namenodes.mynameservice", (expectedNodeOne + ", ") + expectedNodeTwo);
        hdfsSiteProperties.put((("dfs.namenode.https-address." + expectedNameService) + ".") + expectedNodeOne, (expectedHostName + ":") + expectedPortNum);
        hdfsSiteProperties.put((("dfs.namenode.https-address." + expectedNameService) + ".") + expectedNodeTwo, (expectedHostName + ":") + expectedPortNum);
        hdfsSiteProperties.put((("dfs.namenode.http-address." + expectedNameService) + ".") + expectedNodeOne, (expectedHostName + ":") + expectedPortNum);
        hdfsSiteProperties.put((("dfs.namenode.http-address." + expectedNameService) + ".") + expectedNodeTwo, (expectedHostName + ":") + expectedPortNum);
        hdfsSiteProperties.put((("dfs.namenode.rpc-address." + expectedNameService) + ".") + expectedNodeOne, (expectedHostName + ":") + expectedPortNum);
        hdfsSiteProperties.put((("dfs.namenode.rpc-address." + expectedNameService) + ".") + expectedNodeTwo, (expectedHostName + ":") + expectedPortNum);
        hadoopEnvProperties.put("dfs_ha_initial_namenode_active", expectedHostName);
        hadoopEnvProperties.put("dfs_ha_initial_namenode_standby", expectedHostName);
        org.apache.ambari.server.topology.Configuration clusterConfig = new org.apache.ambari.server.topology.Configuration(configProperties, java.util.Collections.emptyMap());
        java.util.Collection<java.lang.String> groupComponents = new java.util.HashSet<>();
        groupComponents.add("NAMENODE");
        java.util.Collection<java.lang.String> hosts = new java.util.ArrayList<>();
        hosts.add(expectedHostName);
        hosts.add("serverTwo");
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup group = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup(expectedHostGroupName, groupComponents, hosts);
        java.util.Collection<org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup> hostGroups = new java.util.HashSet<>();
        hostGroups.add(group);
        org.apache.ambari.server.topology.ClusterTopology topology = createClusterTopology(bp, clusterConfig, hostGroups);
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor configProcessor = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor(topology);
        configProcessor.doUpdateForBlueprintExport(org.apache.ambari.server.controller.internal.BlueprintExportType.FULL);
        org.junit.Assert.assertEquals("HTTPS address HA property not properly exported", org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.createExportedAddress(expectedPortNum, expectedHostGroupName), hdfsSiteProperties.get((("dfs.namenode.https-address." + expectedNameService) + ".") + expectedNodeOne));
        org.junit.Assert.assertEquals("HTTPS address HA property not properly exported", org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.createExportedAddress(expectedPortNum, expectedHostGroupName), hdfsSiteProperties.get((("dfs.namenode.https-address." + expectedNameService) + ".") + expectedNodeTwo));
        org.junit.Assert.assertEquals("HTTPS address HA property not properly exported", org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.createExportedAddress(expectedPortNum, expectedHostGroupName), hdfsSiteProperties.get((("dfs.namenode.http-address." + expectedNameService) + ".") + expectedNodeOne));
        org.junit.Assert.assertEquals("HTTPS address HA property not properly exported", org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.createExportedAddress(expectedPortNum, expectedHostGroupName), hdfsSiteProperties.get((("dfs.namenode.http-address." + expectedNameService) + ".") + expectedNodeTwo));
        org.junit.Assert.assertEquals("HTTPS address HA property not properly exported", org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.createExportedAddress(expectedPortNum, expectedHostGroupName), hdfsSiteProperties.get((("dfs.namenode.rpc-address." + expectedNameService) + ".") + expectedNodeOne));
        org.junit.Assert.assertEquals("HTTPS address HA property not properly exported", org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.createExportedAddress(expectedPortNum, expectedHostGroupName), hdfsSiteProperties.get((("dfs.namenode.rpc-address." + expectedNameService) + ".") + expectedNodeTwo));
        org.junit.Assert.assertNull("Initial NameNode HA property exported although should not have", hadoopEnvProperties.get("dfs_ha_initial_namenode_active"));
        org.junit.Assert.assertNull("Initial NameNode HA property exported although should not have", hadoopEnvProperties.get("dfs_ha_initial_namenode_standby"));
        java.util.Map<java.lang.String, java.lang.String> clusterEnv = clusterConfig.getProperties().get("cluster-env");
        org.junit.Assert.assertTrue("Initial NameNode HA property exported although should not have", (clusterEnv == null) || (clusterEnv.get("dfs_ha_initial_namenode_active") == null));
        org.junit.Assert.assertTrue("Initial NameNode HA property exported although should not have", (clusterEnv == null) || (clusterEnv.get("dfs_ha_initial_namenode_standby") == null));
    }

    @org.junit.Test
    public void testDoNameNodeHighAvailabilityExportWithHAEnabledNameServicePropertiesIncluded() throws java.lang.Exception {
        final java.lang.String expectedNameService = "mynameservice";
        final java.lang.String expectedHostName = "c6401.apache.ambari.org";
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> configProperties = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.lang.String> coreSiteProperties = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.lang.String> hbaseSiteProperties = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.lang.String> accumuloSiteProperties = new java.util.HashMap<>();
        configProperties.put("core-site", coreSiteProperties);
        configProperties.put("hbase-site", hbaseSiteProperties);
        configProperties.put("accumulo-site", accumuloSiteProperties);
        coreSiteProperties.put("fs.defaultFS", "hdfs://" + expectedNameService);
        hbaseSiteProperties.put("hbase.rootdir", ("hdfs://" + expectedNameService) + "/apps/hbase/data");
        accumuloSiteProperties.put("instance.volumes", ("hdfs://" + expectedNameService) + "/apps/accumulo/data");
        org.apache.ambari.server.topology.Configuration clusterConfig = new org.apache.ambari.server.topology.Configuration(configProperties, java.util.Collections.emptyMap());
        java.util.Collection<java.lang.String> groupComponents = new java.util.HashSet<>();
        groupComponents.add("RESOURCEMANAGER");
        java.util.Collection<java.lang.String> hosts = new java.util.ArrayList<>();
        hosts.add(expectedHostName);
        hosts.add("serverTwo");
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup group = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup("group1", groupComponents, hosts);
        java.util.Collection<org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup> hostGroups = new java.util.HashSet<>();
        hostGroups.add(group);
        org.apache.ambari.server.topology.ClusterTopology topology = createClusterTopology(bp, clusterConfig, hostGroups);
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor configProcessor = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor(topology);
        configProcessor.doUpdateForBlueprintExport(org.apache.ambari.server.controller.internal.BlueprintExportType.FULL);
        org.junit.Assert.assertEquals("Property containing an HA nameservice (fs.defaultFS), was not correctly exported by the processor", "hdfs://" + expectedNameService, coreSiteProperties.get("fs.defaultFS"));
        org.junit.Assert.assertEquals("Property containing an HA nameservice (hbase.rootdir), was not correctly exported by the processor", ("hdfs://" + expectedNameService) + "/apps/hbase/data", hbaseSiteProperties.get("hbase.rootdir"));
        org.junit.Assert.assertEquals("Property containing an HA nameservice (instance.volumes), was not correctly exported by the processor", ("hdfs://" + expectedNameService) + "/apps/accumulo/data", accumuloSiteProperties.get("instance.volumes"));
    }

    @org.junit.Test
    public void testDoNameNodeHighAvailabilityExportWithHANotEnabled() throws java.lang.Exception {
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> configProperties = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.lang.String> hdfsSiteProperties = new java.util.HashMap<>();
        configProperties.put("hdfs-site", hdfsSiteProperties);
        org.junit.Assert.assertEquals("Incorrect initial state for hdfs-site config", 0, hdfsSiteProperties.size());
        org.apache.ambari.server.topology.Configuration clusterConfig = new org.apache.ambari.server.topology.Configuration(configProperties, java.util.Collections.emptyMap());
        java.util.Collection<java.lang.String> groupComponents = new java.util.HashSet<>();
        groupComponents.add("NAMENODE");
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup group = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup("group1", groupComponents, java.util.Collections.singleton("host1"));
        java.util.Collection<org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup> hostGroups = new java.util.HashSet<>();
        hostGroups.add(group);
        org.apache.ambari.server.topology.ClusterTopology topology = createClusterTopology(bp, clusterConfig, hostGroups);
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor configProcessor = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor(topology);
        configProcessor.doUpdateForBlueprintExport(org.apache.ambari.server.controller.internal.BlueprintExportType.FULL);
        org.junit.Assert.assertEquals("Incorrect state for hdfs-site config after HA call in non-HA environment, should be zero", 0, hdfsSiteProperties.size());
    }

    @org.junit.Test
    public void testDoNameNodeHighAvailabilityExportWithHAEnabledMultipleServices() throws java.lang.Exception {
        final java.lang.String expectedNameServiceOne = "mynameserviceOne";
        final java.lang.String expectedNameServiceTwo = "mynameserviceTwo";
        final java.lang.String expectedHostNameOne = "c6401.apache.ambari.org";
        final java.lang.String expectedHostNameTwo = "c6402.apache.ambari.org";
        final java.lang.String expectedPortNum = "808080";
        final java.lang.String expectedNodeOne = "nn1";
        final java.lang.String expectedNodeTwo = "nn2";
        final java.lang.String expectedHostGroupName = "host_group_1";
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> configProperties = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.lang.String> hdfsSiteProperties = new java.util.HashMap<>();
        configProperties.put("hdfs-site", hdfsSiteProperties);
        hdfsSiteProperties.put("dfs.internal.nameservices", (expectedNameServiceOne + ",") + expectedNameServiceTwo);
        hdfsSiteProperties.put("dfs.nameservices", (expectedNameServiceOne + ",") + expectedNameServiceTwo);
        hdfsSiteProperties.put("dfs.ha.namenodes." + expectedNameServiceOne, (expectedNodeOne + ", ") + expectedNodeTwo);
        hdfsSiteProperties.put("dfs.ha.namenodes." + expectedNameServiceTwo, (expectedNodeOne + ", ") + expectedNodeTwo);
        hdfsSiteProperties.put((("dfs.namenode.https-address." + expectedNameServiceOne) + ".") + expectedNodeOne, (expectedHostNameOne + ":") + expectedPortNum);
        hdfsSiteProperties.put((("dfs.namenode.https-address." + expectedNameServiceOne) + ".") + expectedNodeTwo, (expectedHostNameOne + ":") + expectedPortNum);
        hdfsSiteProperties.put((("dfs.namenode.http-address." + expectedNameServiceOne) + ".") + expectedNodeOne, (expectedHostNameOne + ":") + expectedPortNum);
        hdfsSiteProperties.put((("dfs.namenode.http-address." + expectedNameServiceOne) + ".") + expectedNodeTwo, (expectedHostNameOne + ":") + expectedPortNum);
        hdfsSiteProperties.put((("dfs.namenode.rpc-address." + expectedNameServiceOne) + ".") + expectedNodeOne, (expectedHostNameOne + ":") + expectedPortNum);
        hdfsSiteProperties.put((("dfs.namenode.rpc-address." + expectedNameServiceOne) + ".") + expectedNodeTwo, (expectedHostNameOne + ":") + expectedPortNum);
        hdfsSiteProperties.put((("dfs.namenode.https-address." + expectedNameServiceTwo) + ".") + expectedNodeOne, (expectedHostNameTwo + ":") + expectedPortNum);
        hdfsSiteProperties.put((("dfs.namenode.https-address." + expectedNameServiceTwo) + ".") + expectedNodeTwo, (expectedHostNameTwo + ":") + expectedPortNum);
        hdfsSiteProperties.put((("dfs.namenode.http-address." + expectedNameServiceTwo) + ".") + expectedNodeOne, (expectedHostNameTwo + ":") + expectedPortNum);
        hdfsSiteProperties.put((("dfs.namenode.http-address." + expectedNameServiceTwo) + ".") + expectedNodeTwo, (expectedHostNameTwo + ":") + expectedPortNum);
        hdfsSiteProperties.put((("dfs.namenode.rpc-address." + expectedNameServiceTwo) + ".") + expectedNodeOne, (expectedHostNameTwo + ":") + expectedPortNum);
        hdfsSiteProperties.put((("dfs.namenode.rpc-address." + expectedNameServiceTwo) + ".") + expectedNodeTwo, (expectedHostNameTwo + ":") + expectedPortNum);
        org.apache.ambari.server.topology.Configuration clusterConfig = new org.apache.ambari.server.topology.Configuration(configProperties, java.util.Collections.emptyMap());
        java.util.Collection<java.lang.String> groupComponents = new java.util.HashSet<>();
        groupComponents.add("NAMENODE");
        java.util.Collection<java.lang.String> hosts = new java.util.ArrayList<>();
        hosts.add(expectedHostNameOne);
        hosts.add(expectedHostNameTwo);
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup group = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup(expectedHostGroupName, groupComponents, hosts);
        java.util.Collection<org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup> hostGroups = new java.util.HashSet<>();
        hostGroups.add(group);
        org.apache.ambari.server.topology.ClusterTopology topology = createClusterTopology(bp, clusterConfig, hostGroups);
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor configProcessor = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor(topology);
        configProcessor.doUpdateForBlueprintExport(org.apache.ambari.server.controller.internal.BlueprintExportType.FULL);
        org.junit.Assert.assertEquals("HTTPS address HA property not properly exported", org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.createExportedAddress(expectedPortNum, expectedHostGroupName), hdfsSiteProperties.get((("dfs.namenode.https-address." + expectedNameServiceOne) + ".") + expectedNodeOne));
        org.junit.Assert.assertEquals("HTTPS address HA property not properly exported", org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.createExportedAddress(expectedPortNum, expectedHostGroupName), hdfsSiteProperties.get((("dfs.namenode.https-address." + expectedNameServiceOne) + ".") + expectedNodeTwo));
        org.junit.Assert.assertEquals("HTTPS address HA property not properly exported", org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.createExportedAddress(expectedPortNum, expectedHostGroupName), hdfsSiteProperties.get((("dfs.namenode.http-address." + expectedNameServiceOne) + ".") + expectedNodeOne));
        org.junit.Assert.assertEquals("HTTPS address HA property not properly exported", org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.createExportedAddress(expectedPortNum, expectedHostGroupName), hdfsSiteProperties.get((("dfs.namenode.http-address." + expectedNameServiceOne) + ".") + expectedNodeTwo));
        org.junit.Assert.assertEquals("HTTPS address HA property not properly exported", org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.createExportedAddress(expectedPortNum, expectedHostGroupName), hdfsSiteProperties.get((("dfs.namenode.rpc-address." + expectedNameServiceOne) + ".") + expectedNodeOne));
        org.junit.Assert.assertEquals("HTTPS address HA property not properly exported", org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.createExportedAddress(expectedPortNum, expectedHostGroupName), hdfsSiteProperties.get((("dfs.namenode.rpc-address." + expectedNameServiceOne) + ".") + expectedNodeTwo));
        org.junit.Assert.assertEquals("HTTPS address HA property not properly exported", org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.createExportedAddress(expectedPortNum, expectedHostGroupName), hdfsSiteProperties.get((("dfs.namenode.https-address." + expectedNameServiceTwo) + ".") + expectedNodeOne));
        org.junit.Assert.assertEquals("HTTPS address HA property not properly exported", org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.createExportedAddress(expectedPortNum, expectedHostGroupName), hdfsSiteProperties.get((("dfs.namenode.https-address." + expectedNameServiceTwo) + ".") + expectedNodeTwo));
        org.junit.Assert.assertEquals("HTTPS address HA property not properly exported", org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.createExportedAddress(expectedPortNum, expectedHostGroupName), hdfsSiteProperties.get((("dfs.namenode.http-address." + expectedNameServiceTwo) + ".") + expectedNodeOne));
        org.junit.Assert.assertEquals("HTTPS address HA property not properly exported", org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.createExportedAddress(expectedPortNum, expectedHostGroupName), hdfsSiteProperties.get((("dfs.namenode.http-address." + expectedNameServiceTwo) + ".") + expectedNodeTwo));
        org.junit.Assert.assertEquals("HTTPS address HA property not properly exported", org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.createExportedAddress(expectedPortNum, expectedHostGroupName), hdfsSiteProperties.get((("dfs.namenode.rpc-address." + expectedNameServiceTwo) + ".") + expectedNodeOne));
        org.junit.Assert.assertEquals("HTTPS address HA property not properly exported", org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.createExportedAddress(expectedPortNum, expectedHostGroupName), hdfsSiteProperties.get((("dfs.namenode.rpc-address." + expectedNameServiceTwo) + ".") + expectedNodeTwo));
    }

    @org.junit.Test
    public void testYarnConfigExported() throws java.lang.Exception {
        final java.lang.String expectedHostName = "c6401.apache.ambari.org";
        final java.lang.String expectedPortNum = "808080";
        final java.lang.String expectedHostGroupName = "host_group_1";
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> configProperties = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.lang.String> yarnSiteProperties = new java.util.HashMap<>();
        configProperties.put("yarn-site", yarnSiteProperties);
        yarnSiteProperties.put("yarn.log.server.url", ("http://" + expectedHostName) + ":19888/jobhistory/logs");
        yarnSiteProperties.put("yarn.resourcemanager.hostname", expectedHostName);
        yarnSiteProperties.put("yarn.resourcemanager.resource-tracker.address", (expectedHostName + ":") + expectedPortNum);
        yarnSiteProperties.put("yarn.resourcemanager.webapp.address", (expectedHostName + ":") + expectedPortNum);
        yarnSiteProperties.put("yarn.resourcemanager.scheduler.address", (expectedHostName + ":") + expectedPortNum);
        yarnSiteProperties.put("yarn.resourcemanager.address", (expectedHostName + ":") + expectedPortNum);
        yarnSiteProperties.put("yarn.resourcemanager.admin.address", (expectedHostName + ":") + expectedPortNum);
        yarnSiteProperties.put("yarn.timeline-service.address", (expectedHostName + ":") + expectedPortNum);
        yarnSiteProperties.put("yarn.timeline-service.webapp.address", (expectedHostName + ":") + expectedPortNum);
        yarnSiteProperties.put("yarn.timeline-service.webapp.https.address", (expectedHostName + ":") + expectedPortNum);
        yarnSiteProperties.put("yarn.log.server.web-service.url", (expectedHostName + ":") + expectedPortNum);
        yarnSiteProperties.put("yarn.timeline-service.reader.webapp.address", (expectedHostName + ":") + expectedPortNum);
        yarnSiteProperties.put("yarn.timeline-service.reader.webapp.https.address", (expectedHostName + ":") + expectedPortNum);
        org.apache.ambari.server.topology.Configuration clusterConfig = new org.apache.ambari.server.topology.Configuration(configProperties, java.util.Collections.emptyMap());
        java.util.Collection<java.lang.String> groupComponents = new java.util.HashSet<>();
        groupComponents.add("RESOURCEMANAGER");
        java.util.Collection<java.lang.String> hosts = new java.util.ArrayList<>();
        hosts.add(expectedHostName);
        hosts.add("serverTwo");
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup group = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup(expectedHostGroupName, groupComponents, hosts);
        java.util.Collection<org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup> hostGroups = new java.util.HashSet<>();
        hostGroups.add(group);
        org.apache.ambari.server.topology.ClusterTopology topology = createClusterTopology(bp, clusterConfig, hostGroups);
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor configProcessor = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor(topology);
        configProcessor.doUpdateForBlueprintExport(org.apache.ambari.server.controller.internal.BlueprintExportType.FULL);
        org.junit.Assert.assertEquals("Yarn Log Server URL was incorrectly exported", ((("http://" + "%HOSTGROUP::") + expectedHostGroupName) + "%") + ":19888/jobhistory/logs", yarnSiteProperties.get("yarn.log.server.url"));
        org.junit.Assert.assertEquals("Yarn ResourceManager hostname was incorrectly exported", org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.createExportedHostName(expectedHostGroupName), yarnSiteProperties.get("yarn.resourcemanager.hostname"));
        org.junit.Assert.assertEquals("Yarn ResourceManager tracker address was incorrectly exported", org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.createExportedAddress(expectedPortNum, expectedHostGroupName), yarnSiteProperties.get("yarn.resourcemanager.resource-tracker.address"));
        org.junit.Assert.assertEquals("Yarn ResourceManager webapp address was incorrectly exported", org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.createExportedAddress(expectedPortNum, expectedHostGroupName), yarnSiteProperties.get("yarn.resourcemanager.webapp.address"));
        org.junit.Assert.assertEquals("Yarn ResourceManager scheduler address was incorrectly exported", org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.createExportedAddress(expectedPortNum, expectedHostGroupName), yarnSiteProperties.get("yarn.resourcemanager.scheduler.address"));
        org.junit.Assert.assertEquals("Yarn ResourceManager address was incorrectly exported", org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.createExportedAddress(expectedPortNum, expectedHostGroupName), yarnSiteProperties.get("yarn.resourcemanager.address"));
        org.junit.Assert.assertEquals("Yarn ResourceManager admin address was incorrectly exported", org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.createExportedAddress(expectedPortNum, expectedHostGroupName), yarnSiteProperties.get("yarn.resourcemanager.admin.address"));
        org.junit.Assert.assertEquals("Yarn ResourceManager timeline-service address was incorrectly exported", org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.createExportedAddress(expectedPortNum, expectedHostGroupName), yarnSiteProperties.get("yarn.timeline-service.address"));
        org.junit.Assert.assertEquals("Yarn ResourceManager timeline webapp address was incorrectly exported", org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.createExportedAddress(expectedPortNum, expectedHostGroupName), yarnSiteProperties.get("yarn.timeline-service.webapp.address"));
        org.junit.Assert.assertEquals("Yarn ResourceManager timeline webapp HTTPS address was incorrectly exported", org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.createExportedAddress(expectedPortNum, expectedHostGroupName), yarnSiteProperties.get("yarn.timeline-service.webapp.https.address"));
        org.junit.Assert.assertEquals("Yarn ResourceManager timeline web service url was incorrectly exported", org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.createExportedAddress(expectedPortNum, expectedHostGroupName), yarnSiteProperties.get("yarn.log.server.web-service.url"));
        org.junit.Assert.assertEquals("Yarn ResourceManager timeline reader webapp address was incorrectly exported", org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.createExportedAddress(expectedPortNum, expectedHostGroupName), yarnSiteProperties.get("yarn.timeline-service.reader.webapp.address"));
        org.junit.Assert.assertEquals("Yarn ResourceManager timeline reader webapp HTTPS address was incorrectly exported", org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.createExportedAddress(expectedPortNum, expectedHostGroupName), yarnSiteProperties.get("yarn.timeline-service.reader.webapp.https.address"));
    }

    @org.junit.Test
    public void testYarnConfigExportedWithDefaultZeroHostAddress() throws java.lang.Exception {
        final java.lang.String expectedHostName = "c6401.apache.ambari.org";
        final java.lang.String expectedPortNum = "808080";
        final java.lang.String expectedHostGroupName = "host_group_1";
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> configProperties = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.lang.String> yarnSiteProperties = new java.util.HashMap<>();
        configProperties.put("yarn-site", yarnSiteProperties);
        yarnSiteProperties.put("yarn.log.server.url", ("http://" + expectedHostName) + ":19888/jobhistory/logs");
        yarnSiteProperties.put("yarn.resourcemanager.hostname", expectedHostName);
        yarnSiteProperties.put("yarn.resourcemanager.resource-tracker.address", (expectedHostName + ":") + expectedPortNum);
        yarnSiteProperties.put("yarn.resourcemanager.webapp.address", (expectedHostName + ":") + expectedPortNum);
        yarnSiteProperties.put("yarn.resourcemanager.scheduler.address", (expectedHostName + ":") + expectedPortNum);
        yarnSiteProperties.put("yarn.resourcemanager.address", (expectedHostName + ":") + expectedPortNum);
        yarnSiteProperties.put("yarn.resourcemanager.admin.address", (expectedHostName + ":") + expectedPortNum);
        yarnSiteProperties.put("yarn.timeline-service.address", ("0.0.0.0" + ":") + expectedPortNum);
        yarnSiteProperties.put("yarn.timeline-service.webapp.address", ("0.0.0.0" + ":") + expectedPortNum);
        yarnSiteProperties.put("yarn.timeline-service.webapp.https.address", ("0.0.0.0" + ":") + expectedPortNum);
        yarnSiteProperties.put("yarn.timeline-service.reader.webapp.address", ("0.0.0.0" + ":") + expectedPortNum);
        yarnSiteProperties.put("yarn.timeline-service.reader.webapp.https.address", ("0.0.0.0" + ":") + expectedPortNum);
        org.apache.ambari.server.topology.Configuration clusterConfig = new org.apache.ambari.server.topology.Configuration(configProperties, java.util.Collections.emptyMap());
        java.util.Collection<java.lang.String> groupComponents = new java.util.HashSet<>();
        groupComponents.add("RESOURCEMANAGER");
        java.util.Collection<java.lang.String> hosts = new java.util.ArrayList<>();
        hosts.add(expectedHostName);
        hosts.add("serverTwo");
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup group = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup(expectedHostGroupName, groupComponents, hosts);
        java.util.Collection<org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup> hostGroups = new java.util.HashSet<>();
        hostGroups.add(group);
        org.apache.ambari.server.topology.ClusterTopology topology = createClusterTopology(bp, clusterConfig, hostGroups);
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor configProcessor = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor(topology);
        configProcessor.doUpdateForBlueprintExport(org.apache.ambari.server.controller.internal.BlueprintExportType.FULL);
        org.junit.Assert.assertEquals("Yarn Log Server URL was incorrectly exported", ((("http://" + "%HOSTGROUP::") + expectedHostGroupName) + "%") + ":19888/jobhistory/logs", yarnSiteProperties.get("yarn.log.server.url"));
        org.junit.Assert.assertEquals("Yarn ResourceManager hostname was incorrectly exported", org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.createExportedHostName(expectedHostGroupName), yarnSiteProperties.get("yarn.resourcemanager.hostname"));
        org.junit.Assert.assertEquals("Yarn ResourceManager tracker address was incorrectly exported", org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.createExportedAddress(expectedPortNum, expectedHostGroupName), yarnSiteProperties.get("yarn.resourcemanager.resource-tracker.address"));
        org.junit.Assert.assertEquals("Yarn ResourceManager webapp address was incorrectly exported", org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.createExportedAddress(expectedPortNum, expectedHostGroupName), yarnSiteProperties.get("yarn.resourcemanager.webapp.address"));
        org.junit.Assert.assertEquals("Yarn ResourceManager scheduler address was incorrectly exported", org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.createExportedAddress(expectedPortNum, expectedHostGroupName), yarnSiteProperties.get("yarn.resourcemanager.scheduler.address"));
        org.junit.Assert.assertEquals("Yarn ResourceManager address was incorrectly exported", org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.createExportedAddress(expectedPortNum, expectedHostGroupName), yarnSiteProperties.get("yarn.resourcemanager.address"));
        org.junit.Assert.assertEquals("Yarn ResourceManager admin address was incorrectly exported", org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.createExportedAddress(expectedPortNum, expectedHostGroupName), yarnSiteProperties.get("yarn.resourcemanager.admin.address"));
        org.junit.Assert.assertEquals("Yarn ResourceManager timeline-service address was incorrectly exported", ("0.0.0.0" + ":") + expectedPortNum, yarnSiteProperties.get("yarn.timeline-service.address"));
        org.junit.Assert.assertEquals("Yarn ResourceManager timeline webapp address was incorrectly exported", ("0.0.0.0" + ":") + expectedPortNum, yarnSiteProperties.get("yarn.timeline-service.webapp.address"));
        org.junit.Assert.assertEquals("Yarn ResourceManager timeline webapp HTTPS address was incorrectly exported", ("0.0.0.0" + ":") + expectedPortNum, yarnSiteProperties.get("yarn.timeline-service.webapp.https.address"));
        org.junit.Assert.assertEquals("Yarn ResourceManager timeline reader webapp address was incorrectly exported", ("0.0.0.0" + ":") + expectedPortNum, yarnSiteProperties.get("yarn.timeline-service.reader.webapp.address"));
        org.junit.Assert.assertEquals("Yarn ResourceManager timeline reader webapp HTTPS address was incorrectly exported", ("0.0.0.0" + ":") + expectedPortNum, yarnSiteProperties.get("yarn.timeline-service.reader.webapp.https.address"));
    }

    @org.junit.Test
    public void testHDFSConfigExported() throws java.lang.Exception {
        final java.lang.String expectedHostName = "c6401.apache.ambari.org";
        final java.lang.String expectedPortNum = "808080";
        final java.lang.String expectedHostGroupName = "host_group_1";
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> configProperties = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.lang.String> hdfsSiteProperties = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.lang.String> coreSiteProperties = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.lang.String> hbaseSiteProperties = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.lang.String> accumuloSiteProperties = new java.util.HashMap<>();
        configProperties.put("hdfs-site", hdfsSiteProperties);
        configProperties.put("core-site", coreSiteProperties);
        configProperties.put("hbase-site", hbaseSiteProperties);
        configProperties.put("accumulo-site", accumuloSiteProperties);
        hdfsSiteProperties.put("dfs.http.address", (expectedHostName + ":") + expectedPortNum);
        hdfsSiteProperties.put("dfs.https.address", (expectedHostName + ":") + expectedPortNum);
        hdfsSiteProperties.put("dfs.namenode.http-address", (expectedHostName + ":") + expectedPortNum);
        hdfsSiteProperties.put("dfs.namenode.https-address", (expectedHostName + ":") + expectedPortNum);
        hdfsSiteProperties.put("dfs.secondary.http.address", (expectedHostName + ":") + expectedPortNum);
        hdfsSiteProperties.put("dfs.namenode.secondary.http-address", (expectedHostName + ":") + expectedPortNum);
        hdfsSiteProperties.put("dfs.namenode.shared.edits.dir", (expectedHostName + ":") + expectedPortNum);
        coreSiteProperties.put("fs.default.name", (expectedHostName + ":") + expectedPortNum);
        coreSiteProperties.put("fs.defaultFS", (("hdfs://" + expectedHostName) + ":") + expectedPortNum);
        hbaseSiteProperties.put("hbase.rootdir", ((("hdfs://" + expectedHostName) + ":") + expectedPortNum) + "/apps/hbase/data");
        accumuloSiteProperties.put("instance.volumes", ((("hdfs://" + expectedHostName) + ":") + expectedPortNum) + "/apps/accumulo/data");
        org.apache.ambari.server.topology.Configuration clusterConfig = new org.apache.ambari.server.topology.Configuration(configProperties, java.util.Collections.emptyMap());
        java.util.Collection<java.lang.String> groupComponents = new java.util.HashSet<>();
        groupComponents.add("NAMENODE");
        groupComponents.add("SECONDARY_NAMENODE");
        java.util.Collection<java.lang.String> hosts = new java.util.ArrayList<>();
        hosts.add(expectedHostName);
        hosts.add("serverTwo");
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup group = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup(expectedHostGroupName, groupComponents, hosts);
        java.util.Collection<org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup> hostGroups = new java.util.HashSet<>();
        hostGroups.add(group);
        org.apache.ambari.server.topology.ClusterTopology topology = createClusterTopology(bp, clusterConfig, hostGroups);
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor configProcessor = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor(topology);
        configProcessor.doUpdateForBlueprintExport(org.apache.ambari.server.controller.internal.BlueprintExportType.FULL);
        org.junit.Assert.assertEquals("hdfs config property not exported properly", org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.createExportedAddress(expectedPortNum, expectedHostGroupName), hdfsSiteProperties.get("dfs.http.address"));
        org.junit.Assert.assertEquals("hdfs config property not exported properly", org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.createExportedAddress(expectedPortNum, expectedHostGroupName), hdfsSiteProperties.get("dfs.https.address"));
        org.junit.Assert.assertEquals("hdfs config property not exported properly", org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.createExportedAddress(expectedPortNum, expectedHostGroupName), hdfsSiteProperties.get("dfs.namenode.http-address"));
        org.junit.Assert.assertEquals("hdfs config property not exported properly", org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.createExportedAddress(expectedPortNum, expectedHostGroupName), hdfsSiteProperties.get("dfs.namenode.https-address"));
        org.junit.Assert.assertEquals("hdfs config property not exported properly", org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.createExportedAddress(expectedPortNum, expectedHostGroupName), hdfsSiteProperties.get("dfs.secondary.http.address"));
        org.junit.Assert.assertEquals("hdfs config property not exported properly", org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.createExportedAddress(expectedPortNum, expectedHostGroupName), hdfsSiteProperties.get("dfs.namenode.secondary.http-address"));
        org.junit.Assert.assertEquals("hdfs config property not exported properly", org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.createExportedAddress(expectedPortNum, expectedHostGroupName), hdfsSiteProperties.get("dfs.namenode.shared.edits.dir"));
        org.junit.Assert.assertEquals("hdfs config in core-site not exported properly", org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.createExportedAddress(expectedPortNum, expectedHostGroupName), coreSiteProperties.get("fs.default.name"));
        org.junit.Assert.assertEquals("hdfs config in core-site not exported properly", "hdfs://" + org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.createExportedAddress(expectedPortNum, expectedHostGroupName), coreSiteProperties.get("fs.defaultFS"));
        org.junit.Assert.assertEquals("hdfs config in hbase-site not exported properly", ("hdfs://" + org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.createExportedAddress(expectedPortNum, expectedHostGroupName)) + "/apps/hbase/data", hbaseSiteProperties.get("hbase.rootdir"));
        org.junit.Assert.assertEquals("hdfs config in accumulo-site not exported properly", ("hdfs://" + org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.createExportedAddress(expectedPortNum, expectedHostGroupName)) + "/apps/accumulo/data", accumuloSiteProperties.get("instance.volumes"));
    }

    @org.junit.Test
    public void testHiveConfigExported() throws java.lang.Exception {
        final java.lang.String expectedHostName = "c6401.apache.ambari.org";
        final java.lang.String expectedHostNameTwo = "c6402.ambari.apache.org";
        final java.lang.String expectedPortNum = "808080";
        final java.lang.String expectedHostGroupName = "host_group_1";
        final java.lang.String expectedHostGroupNameTwo = "host_group_2";
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> configProperties = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.lang.String> hiveSiteProperties = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.lang.String> hiveEnvProperties = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.lang.String> webHCatSiteProperties = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.lang.String> coreSiteProperties = new java.util.HashMap<>();
        configProperties.put("hive-site", hiveSiteProperties);
        configProperties.put("hive-env", hiveEnvProperties);
        configProperties.put("webhcat-site", webHCatSiteProperties);
        configProperties.put("core-site", coreSiteProperties);
        hiveSiteProperties.put("hive.metastore.uris", (("thrift://" + expectedHostName) + ":") + expectedPortNum);
        hiveSiteProperties.put("javax.jdo.option.ConnectionURL", (expectedHostName + ":") + expectedPortNum);
        hiveSiteProperties.put("hive.zookeeper.quorum", (((((expectedHostName + ":") + expectedPortNum) + ",") + expectedHostNameTwo) + ":") + expectedPortNum);
        hiveSiteProperties.put("hive.cluster.delegation.token.store.zookeeper.connectString", (((((expectedHostName + ":") + expectedPortNum) + ",") + expectedHostNameTwo) + ":") + expectedPortNum);
        webHCatSiteProperties.put("templeton.hive.properties", (expectedHostName + ",") + expectedHostNameTwo);
        webHCatSiteProperties.put("templeton.kerberos.principal", expectedHostName);
        coreSiteProperties.put("hadoop.proxyuser.hive.hosts", (expectedHostName + ",") + expectedHostNameTwo);
        coreSiteProperties.put("hadoop.proxyuser.HTTP.hosts", (expectedHostName + ",") + expectedHostNameTwo);
        coreSiteProperties.put("hadoop.proxyuser.hcat.hosts", (expectedHostName + ",") + expectedHostNameTwo);
        org.apache.ambari.server.topology.Configuration clusterConfig = new org.apache.ambari.server.topology.Configuration(configProperties, java.util.Collections.emptyMap());
        java.util.Collection<java.lang.String> groupComponents = new java.util.HashSet<>();
        groupComponents.add("HIVE_SERVER");
        java.util.Collection<java.lang.String> hosts = new java.util.ArrayList<>();
        hosts.add(expectedHostName);
        hosts.add("serverTwo");
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup group = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup(expectedHostGroupName, groupComponents, hosts);
        java.util.Collection<java.lang.String> groupComponents2 = new java.util.HashSet<>();
        groupComponents2.add("HIVE_CLIENT");
        java.util.Collection<java.lang.String> hosts2 = new java.util.ArrayList<>();
        hosts2.add(expectedHostNameTwo);
        hosts2.add("serverFour");
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup group2 = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup(expectedHostGroupNameTwo, groupComponents2, hosts2);
        java.util.Collection<org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup> hostGroups = new java.util.HashSet<>();
        hostGroups.add(group);
        hostGroups.add(group2);
        org.apache.ambari.server.topology.ClusterTopology topology = createClusterTopology(bp, clusterConfig, hostGroups);
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor configProcessor = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor(topology);
        configProcessor.doUpdateForBlueprintExport(org.apache.ambari.server.controller.internal.BlueprintExportType.FULL);
        org.junit.Assert.assertEquals("hive property not properly exported", "thrift://" + org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.createExportedAddress(expectedPortNum, expectedHostGroupName), hiveSiteProperties.get("hive.metastore.uris"));
        org.junit.Assert.assertEquals("hive property not properly exported", org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.createExportedAddress(expectedPortNum, expectedHostGroupName), hiveSiteProperties.get("javax.jdo.option.ConnectionURL"));
        org.junit.Assert.assertEquals("hive property not properly exported", (org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.createExportedHostName(expectedHostGroupName) + ",") + org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.createExportedHostName(expectedHostGroupNameTwo), webHCatSiteProperties.get("templeton.hive.properties"));
        org.junit.Assert.assertEquals("hive property not properly exported", (org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.createExportedHostName(expectedHostGroupName) + ",") + org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.createExportedHostName(expectedHostGroupNameTwo), coreSiteProperties.get("hadoop.proxyuser.hive.hosts"));
        org.junit.Assert.assertEquals("hive property not properly exported", (org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.createExportedHostName(expectedHostGroupName) + ",") + org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.createExportedHostName(expectedHostGroupNameTwo), coreSiteProperties.get("hadoop.proxyuser.HTTP.hosts"));
        org.junit.Assert.assertEquals("hive property not properly exported", (org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.createExportedHostName(expectedHostGroupName) + ",") + org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.createExportedHostName(expectedHostGroupNameTwo), coreSiteProperties.get("hadoop.proxyuser.hcat.hosts"));
        org.junit.Assert.assertEquals("hive zookeeper quorum property not properly exported", (org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.createExportedAddress(expectedPortNum, expectedHostGroupName) + ",") + org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.createExportedAddress(expectedPortNum, expectedHostGroupNameTwo), hiveSiteProperties.get("hive.zookeeper.quorum"));
        org.junit.Assert.assertEquals("hive zookeeper connectString property not properly exported", (org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.createExportedAddress(expectedPortNum, expectedHostGroupName) + ",") + org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.createExportedAddress(expectedPortNum, expectedHostGroupNameTwo), hiveSiteProperties.get("hive.cluster.delegation.token.store.zookeeper.connectString"));
    }

    @org.junit.Test
    public void testHiveConfigExportedMultipleHiveMetaStoreServers() throws java.lang.Exception {
        final java.lang.String expectedHostName = "c6401.apache.ambari.org";
        final java.lang.String expectedHostNameTwo = "c6402.ambari.apache.org";
        final java.lang.String expectedPortNum = "808080";
        final java.lang.String expectedHostGroupName = "host_group_1";
        final java.lang.String expectedHostGroupNameTwo = "host_group_2";
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> configProperties = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.lang.String> hiveSiteProperties = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.lang.String> hiveEnvProperties = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.lang.String> webHCatSiteProperties = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.lang.String> coreSiteProperties = new java.util.HashMap<>();
        configProperties.put("hive-site", hiveSiteProperties);
        configProperties.put("hive-env", hiveEnvProperties);
        configProperties.put("webhcat-site", webHCatSiteProperties);
        configProperties.put("core-site", coreSiteProperties);
        hiveSiteProperties.put("hive.metastore.uris", ((((((("thrift://" + expectedHostName) + ":") + expectedPortNum) + ",") + "thrift://") + expectedHostNameTwo) + ":") + expectedPortNum);
        hiveSiteProperties.put("hive.server2.authentication.ldap.url", "ldap://myexternalhost.com:1389");
        hiveSiteProperties.put("javax.jdo.option.ConnectionURL", (expectedHostName + ":") + expectedPortNum);
        hiveSiteProperties.put("hive.zookeeper.quorum", (((((expectedHostName + ":") + expectedPortNum) + ",") + expectedHostNameTwo) + ":") + expectedPortNum);
        hiveSiteProperties.put("hive.cluster.delegation.token.store.zookeeper.connectString", (((((expectedHostName + ":") + expectedPortNum) + ",") + expectedHostNameTwo) + ":") + expectedPortNum);
        webHCatSiteProperties.put("templeton.hive.properties", (expectedHostName + ",") + expectedHostNameTwo);
        webHCatSiteProperties.put("templeton.kerberos.principal", expectedHostName);
        coreSiteProperties.put("hadoop.proxyuser.hive.hosts", (expectedHostName + ",") + expectedHostNameTwo);
        coreSiteProperties.put("hadoop.proxyuser.HTTP.hosts", (expectedHostName + ",") + expectedHostNameTwo);
        coreSiteProperties.put("hadoop.proxyuser.hcat.hosts", (expectedHostName + ",") + expectedHostNameTwo);
        org.apache.ambari.server.topology.Configuration clusterConfig = new org.apache.ambari.server.topology.Configuration(configProperties, java.util.Collections.emptyMap());
        java.util.Collection<java.lang.String> groupComponents = new java.util.HashSet<>();
        groupComponents.add("NAMENODE");
        java.util.Collection<java.lang.String> hosts = new java.util.ArrayList<>();
        hosts.add(expectedHostName);
        hosts.add("serverTwo");
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup group = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup(expectedHostGroupName, groupComponents, hosts);
        java.util.Collection<java.lang.String> groupComponents2 = new java.util.HashSet<>();
        groupComponents2.add("DATANODE");
        java.util.Collection<java.lang.String> hosts2 = new java.util.ArrayList<>();
        hosts2.add(expectedHostNameTwo);
        hosts2.add("serverThree");
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup group2 = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup(expectedHostGroupNameTwo, groupComponents2, hosts2);
        java.util.Collection<org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup> hostGroups = new java.util.HashSet<>();
        hostGroups.add(group);
        hostGroups.add(group2);
        org.apache.ambari.server.topology.ClusterTopology topology = createClusterTopology(bp, clusterConfig, hostGroups);
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor configProcessor = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor(topology);
        configProcessor.doUpdateForBlueprintExport(org.apache.ambari.server.controller.internal.BlueprintExportType.FULL);
        java.lang.System.out.println("RWN: exported value of hive.metastore.uris = " + hiveSiteProperties.get("hive.metastore.uris"));
        org.junit.Assert.assertEquals("hive property not properly exported", ((("thrift://" + org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.createExportedAddress(expectedPortNum, expectedHostGroupName)) + ",") + "thrift://") + org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.createExportedAddress(expectedPortNum, expectedHostGroupNameTwo), hiveSiteProperties.get("hive.metastore.uris"));
        org.junit.Assert.assertEquals("hive property not properly exported", org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.createExportedAddress(expectedPortNum, expectedHostGroupName), hiveSiteProperties.get("javax.jdo.option.ConnectionURL"));
        org.junit.Assert.assertEquals("hive property not properly exported", (org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.createExportedHostName(expectedHostGroupName) + ",") + org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.createExportedHostName(expectedHostGroupNameTwo), webHCatSiteProperties.get("templeton.hive.properties"));
        org.junit.Assert.assertEquals("hive property not properly exported", (org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.createExportedHostName(expectedHostGroupName) + ",") + org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.createExportedHostName(expectedHostGroupNameTwo), coreSiteProperties.get("hadoop.proxyuser.hive.hosts"));
        org.junit.Assert.assertFalse("hive.server2.authentication.ldap.url should not have been present in the exported configuration", hiveSiteProperties.containsKey("hive.server2.authentication.ldap.url"));
        org.junit.Assert.assertEquals("hive property not properly exported", (org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.createExportedHostName(expectedHostGroupName) + ",") + org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.createExportedHostName(expectedHostGroupNameTwo), coreSiteProperties.get("hadoop.proxyuser.HTTP.hosts"));
        org.junit.Assert.assertEquals("hive property not properly exported", (org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.createExportedHostName(expectedHostGroupName) + ",") + org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.createExportedHostName(expectedHostGroupNameTwo), coreSiteProperties.get("hadoop.proxyuser.hcat.hosts"));
        org.junit.Assert.assertEquals("hive zookeeper quorum property not properly exported", (org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.createExportedAddress(expectedPortNum, expectedHostGroupName) + ",") + org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.createExportedAddress(expectedPortNum, expectedHostGroupNameTwo), hiveSiteProperties.get("hive.zookeeper.quorum"));
        org.junit.Assert.assertEquals("hive zookeeper connectString property not properly exported", (org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.createExportedAddress(expectedPortNum, expectedHostGroupName) + ",") + org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.createExportedAddress(expectedPortNum, expectedHostGroupNameTwo), hiveSiteProperties.get("hive.cluster.delegation.token.store.zookeeper.connectString"));
    }

    @org.junit.Test
    public void testOozieConfigExported() throws java.lang.Exception {
        final java.lang.String expectedHostName = "c6401.apache.ambari.org";
        final java.lang.String expectedHostNameTwo = "c6402.ambari.apache.org";
        final java.lang.String expectedExternalHost = "c6408.ambari.apache.org";
        final java.lang.String expectedHostGroupName = "host_group_1";
        final java.lang.String expectedHostGroupNameTwo = "host_group_2";
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> configProperties = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.lang.String> oozieSiteProperties = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.lang.String> oozieEnvProperties = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.lang.String> hiveEnvProperties = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.lang.String> coreSiteProperties = new java.util.HashMap<>();
        configProperties.put("oozie-site", oozieSiteProperties);
        configProperties.put("oozie-env", oozieEnvProperties);
        configProperties.put("hive-env", hiveEnvProperties);
        configProperties.put("core-site", coreSiteProperties);
        oozieSiteProperties.put("oozie.base.url", expectedHostName);
        oozieSiteProperties.put("oozie.authentication.kerberos.principal", expectedHostName);
        oozieSiteProperties.put("oozie.service.HadoopAccessorService.kerberos.principal", expectedHostName);
        oozieSiteProperties.put("oozie.service.JPAService.jdbc.url", ("jdbc:mysql://" + expectedExternalHost) + "/ooziedb");
        oozieEnvProperties.put("oozie_existing_mysql_host", expectedExternalHost);
        hiveEnvProperties.put("hive_existing_oracle_host", expectedExternalHost);
        oozieEnvProperties.put("oozie_heapsize", "1024m");
        oozieEnvProperties.put("oozie_permsize", "2048m");
        coreSiteProperties.put("hadoop.proxyuser.oozie.hosts", (expectedHostName + ",") + expectedHostNameTwo);
        org.apache.ambari.server.topology.Configuration clusterConfig = new org.apache.ambari.server.topology.Configuration(configProperties, java.util.Collections.emptyMap());
        java.util.Collection<java.lang.String> groupComponents = new java.util.HashSet<>();
        groupComponents.add("OOZIE_SERVER");
        java.util.Collection<java.lang.String> hosts = new java.util.ArrayList<>();
        hosts.add(expectedHostName);
        hosts.add("serverTwo");
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup group = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup(expectedHostGroupName, groupComponents, hosts);
        java.util.Collection<java.lang.String> groupComponents2 = new java.util.HashSet<>();
        groupComponents2.add("OOZIE_SERVER");
        java.util.Collection<java.lang.String> hosts2 = new java.util.ArrayList<>();
        hosts2.add(expectedHostNameTwo);
        hosts2.add("serverFour");
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup group2 = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup(expectedHostGroupNameTwo, groupComponents2, hosts2);
        java.util.Collection<org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup> hostGroups = new java.util.HashSet<>();
        hostGroups.add(group);
        hostGroups.add(group2);
        if ((org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.singleHostTopologyUpdaters != null) && org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.singleHostTopologyUpdaters.containsKey("oozie-site")) {
            org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.singleHostTopologyUpdaters.get("oozie-site").remove("oozie.service.JPAService.jdbc.url");
        }
        org.apache.ambari.server.topology.ClusterTopology topology = createClusterTopology(bp, clusterConfig, hostGroups);
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor configProcessor = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor(topology);
        configProcessor.doUpdateForBlueprintExport(org.apache.ambari.server.controller.internal.BlueprintExportType.FULL);
        org.junit.Assert.assertFalse(org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.singleHostTopologyUpdaters.get("oozie-site").containsKey("oozie.service.JPAService.jdbc.url"));
        org.junit.Assert.assertTrue(configProcessor.getRemovePropertyUpdaters().get("oozie-site").containsKey("oozie.service.JPAService.jdbc.url"));
        org.junit.Assert.assertEquals("oozie property not exported correctly", org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.createExportedHostName(expectedHostGroupName), oozieSiteProperties.get("oozie.base.url"));
        org.junit.Assert.assertEquals("oozie property not exported correctly", (org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.createExportedHostName(expectedHostGroupName) + ",") + org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.createExportedHostName(expectedHostGroupNameTwo), coreSiteProperties.get("hadoop.proxyuser.oozie.hosts"));
        org.junit.Assert.assertFalse("oozie_existing_mysql_host should not have been present in the exported configuration", oozieEnvProperties.containsKey("oozie_existing_mysql_host"));
        org.junit.Assert.assertFalse("hive_existing_oracle_host should not have been present in the exported configuration", hiveEnvProperties.containsKey("hive_existing_oracle_host"));
        org.junit.Assert.assertFalse("oozie.service.JPAService.jdbc.url should not have been present in the exported configuration", oozieSiteProperties.containsKey("oozie.service.JPAService.jdbc.url"));
        org.junit.Assert.assertEquals("oozie_heapsize should have been included in exported configuration", "1024m", oozieEnvProperties.get("oozie_heapsize"));
        org.junit.Assert.assertEquals("oozie_permsize should have been included in exported configuration", "2048m", oozieEnvProperties.get("oozie_permsize"));
    }

    @org.junit.Test
    public void testOozieJDBCPropertiesNotRemoved() throws java.lang.Exception {
        final java.lang.String expectedHostName = "c6401.apache.ambari.org";
        final java.lang.String expectedHostNameTwo = "c6402.ambari.apache.org";
        final java.lang.String expectedHostGroupName = "host_group_1";
        final java.lang.String expectedHostGroupNameTwo = "host_group_2";
        final java.lang.String expectedPortNum = "80000";
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> configProperties = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.lang.String> oozieSiteProperties = new java.util.HashMap<>();
        configProperties.put("oozie-site", oozieSiteProperties);
        oozieSiteProperties.put("oozie.service.JPAService.jdbc.url", ("jdbc:mysql://" + expectedHostNameTwo) + "/ooziedb");
        org.apache.ambari.server.topology.Configuration clusterConfig = new org.apache.ambari.server.topology.Configuration(configProperties, java.util.Collections.emptyMap());
        java.util.Collection<java.lang.String> hgComponents = new java.util.HashSet<>();
        hgComponents.add("OOZIE_SERVER");
        hgComponents.add("ZOOKEEPER_SERVER");
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup group1 = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup(expectedHostGroupName, hgComponents, java.util.Collections.singleton(expectedHostName));
        java.util.Collection<java.lang.String> hgComponents2 = new java.util.HashSet<>();
        hgComponents2.add("OOZIE_SERVER");
        hgComponents2.add("ZOOKEEPER_SERVER");
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup group2 = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup(expectedHostGroupNameTwo, hgComponents2, java.util.Collections.singleton(expectedHostNameTwo));
        java.util.Collection<org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup> hostGroups = new java.util.HashSet<>();
        hostGroups.add(group1);
        hostGroups.add(group2);
        EasyMock.expect(stack.getCardinality("OOZIE_SERVER")).andReturn(new org.apache.ambari.server.topology.Cardinality("1+")).anyTimes();
        org.apache.ambari.server.topology.ClusterTopology topology = createClusterTopology(bp, clusterConfig, hostGroups);
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor blueprintConfigurationProcessor = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor(topology);
        org.junit.Assert.assertTrue(org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.singleHostTopologyUpdaters.get("oozie-site").containsKey("oozie.service.JPAService.jdbc.url"));
        org.junit.Assert.assertNull(blueprintConfigurationProcessor.getRemovePropertyUpdaters().get("oozie-site"));
    }

    @org.junit.Test
    public void testOozieJDBCPropertyAddedToSingleHostMapDuringImport() throws java.lang.Exception {
        final java.lang.String expectedHostName = "c6401.apache.ambari.org";
        final java.lang.String expectedHostNameTwo = "c6402.ambari.apache.org";
        final java.lang.String expectedHostGroupName = "host_group_1";
        final java.lang.String expectedHostGroupNameTwo = "host_group_2";
        final java.lang.String expectedPortNum = "80000";
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> configProperties = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.lang.String> oozieSiteProperties = new java.util.HashMap<>();
        configProperties.put("oozie-site", oozieSiteProperties);
        oozieSiteProperties.put("oozie.service.JPAService.jdbc.url", "jdbc:mysql://" + ("%HOSTGROUP::group1%" + "/ooziedb"));
        org.apache.ambari.server.topology.Configuration clusterConfig = new org.apache.ambari.server.topology.Configuration(configProperties, java.util.Collections.emptyMap());
        java.util.Collection<java.lang.String> hgComponents = new java.util.HashSet<>();
        hgComponents.add("OOZIE_SERVER");
        hgComponents.add("ZOOKEEPER_SERVER");
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup group1 = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup(expectedHostGroupName, hgComponents, java.util.Collections.singleton(expectedHostName));
        java.util.Collection<java.lang.String> hgComponents2 = new java.util.HashSet<>();
        hgComponents2.add("OOZIE_SERVER");
        hgComponents2.add("ZOOKEEPER_SERVER");
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup group2 = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup(expectedHostGroupNameTwo, hgComponents2, java.util.Collections.singleton(expectedHostNameTwo));
        java.util.Collection<org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup> hostGroups = new java.util.HashSet<>();
        hostGroups.add(group1);
        hostGroups.add(group2);
        EasyMock.expect(stack.getCardinality("OOZIE_SERVER")).andReturn(new org.apache.ambari.server.topology.Cardinality("1+")).anyTimes();
        org.apache.ambari.server.topology.ClusterTopology topology = createClusterTopology(bp, clusterConfig, hostGroups);
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor blueprintConfigurationProcessor = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor(topology);
        org.junit.Assert.assertTrue(org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.singleHostTopologyUpdaters.get("oozie-site").containsKey("oozie.service.JPAService.jdbc.url"));
        org.junit.Assert.assertNull(blueprintConfigurationProcessor.getRemovePropertyUpdaters().get("oozie-site"));
    }

    @org.junit.Test
    public void testZookeeperConfigExported() throws java.lang.Exception {
        final java.lang.String expectedHostName = "c6401.apache.ambari.org";
        final java.lang.String expectedHostNameTwo = "c6402.ambari.apache.org";
        final java.lang.String expectedHostGroupName = "host_group_1";
        final java.lang.String expectedHostGroupNameTwo = "host_group_2";
        final java.lang.String expectedPortNumberOne = "2112";
        final java.lang.String expectedPortNumberTwo = "1221";
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> configProperties = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.lang.String> coreSiteProperties = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.lang.String> hbaseSiteProperties = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.lang.String> webHCatSiteProperties = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.lang.String> sliderClientProperties = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.lang.String> yarnSiteProperties = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.lang.String> kafkaBrokerProperties = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.lang.String> accumuloSiteProperties = new java.util.HashMap<>();
        configProperties.put("core-site", coreSiteProperties);
        configProperties.put("hbase-site", hbaseSiteProperties);
        configProperties.put("webhcat-site", webHCatSiteProperties);
        configProperties.put("slider-client", sliderClientProperties);
        configProperties.put("yarn-site", yarnSiteProperties);
        configProperties.put("kafka-broker", kafkaBrokerProperties);
        configProperties.put("accumulo-site", accumuloSiteProperties);
        coreSiteProperties.put("ha.zookeeper.quorum", (expectedHostName + ",") + expectedHostNameTwo);
        hbaseSiteProperties.put("hbase.zookeeper.quorum", (expectedHostName + ",") + expectedHostNameTwo);
        webHCatSiteProperties.put("templeton.zookeeper.hosts", (expectedHostName + ",") + expectedHostNameTwo);
        yarnSiteProperties.put("hadoop.registry.zk.quorum", (org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.createHostAddress(expectedHostName, expectedPortNumberOne) + ",") + org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.createHostAddress(expectedHostNameTwo, expectedPortNumberTwo));
        sliderClientProperties.put("slider.zookeeper.quorum", (org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.createHostAddress(expectedHostName, expectedPortNumberOne) + ",") + org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.createHostAddress(expectedHostNameTwo, expectedPortNumberTwo));
        kafkaBrokerProperties.put("zookeeper.connect", (org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.createHostAddress(expectedHostName, expectedPortNumberOne) + ",") + org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.createHostAddress(expectedHostNameTwo, expectedPortNumberTwo));
        accumuloSiteProperties.put("instance.zookeeper.host", (org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.createHostAddress(expectedHostName, expectedPortNumberOne) + ",") + org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.createHostAddress(expectedHostNameTwo, expectedPortNumberTwo));
        org.apache.ambari.server.topology.Configuration clusterConfig = new org.apache.ambari.server.topology.Configuration(configProperties, java.util.Collections.emptyMap());
        java.util.Collection<java.lang.String> groupComponents = new java.util.HashSet<>();
        groupComponents.add("ZOOKEEPER_SERVER");
        java.util.Collection<java.lang.String> hosts = new java.util.ArrayList<>();
        hosts.add(expectedHostName);
        hosts.add("serverTwo");
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup group = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup(expectedHostGroupName, groupComponents, hosts);
        java.util.Collection<java.lang.String> groupComponents2 = new java.util.HashSet<>();
        groupComponents2.add("ZOOKEEPER_SERVER");
        java.util.Collection<java.lang.String> hosts2 = new java.util.ArrayList<>();
        hosts2.add(expectedHostNameTwo);
        hosts2.add("serverFour");
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup group2 = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup(expectedHostGroupNameTwo, groupComponents2, hosts2);
        java.util.Collection<org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup> hostGroups = new java.util.HashSet<>();
        hostGroups.add(group);
        hostGroups.add(group2);
        org.apache.ambari.server.topology.ClusterTopology topology = createClusterTopology(bp, clusterConfig, hostGroups);
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor configProcessor = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor(topology);
        configProcessor.doUpdateForBlueprintExport(org.apache.ambari.server.controller.internal.BlueprintExportType.FULL);
        org.junit.Assert.assertEquals("zookeeper config not properly exported", (org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.createExportedHostName(expectedHostGroupName) + ",") + org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.createExportedHostName(expectedHostGroupNameTwo), coreSiteProperties.get("ha.zookeeper.quorum"));
        org.junit.Assert.assertEquals("zookeeper config not properly exported", (org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.createExportedHostName(expectedHostGroupName) + ",") + org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.createExportedHostName(expectedHostGroupNameTwo), hbaseSiteProperties.get("hbase.zookeeper.quorum"));
        org.junit.Assert.assertEquals("zookeeper config not properly exported", (org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.createExportedHostName(expectedHostGroupName) + ",") + org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.createExportedHostName(expectedHostGroupNameTwo), webHCatSiteProperties.get("templeton.zookeeper.hosts"));
        org.junit.Assert.assertEquals("yarn-site zookeeper config not properly exported", (org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.createExportedHostName(expectedHostGroupName, expectedPortNumberOne) + ",") + org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.createExportedHostName(expectedHostGroupNameTwo, expectedPortNumberTwo), yarnSiteProperties.get("hadoop.registry.zk.quorum"));
        org.junit.Assert.assertEquals("kafka zookeeper config not properly exported", (org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.createExportedHostName(expectedHostGroupName, expectedPortNumberOne) + ",") + org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.createExportedHostName(expectedHostGroupNameTwo, expectedPortNumberTwo), kafkaBrokerProperties.get("zookeeper.connect"));
        org.junit.Assert.assertEquals("accumulo-site zookeeper config not properly exported", (org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.createExportedHostName(expectedHostGroupName, expectedPortNumberOne) + ",") + org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.createExportedHostName(expectedHostGroupNameTwo, expectedPortNumberTwo), accumuloSiteProperties.get("instance.zookeeper.host"));
    }

    @org.junit.Test
    public void testKnoxSecurityConfigExported() throws java.lang.Exception {
        final java.lang.String expectedHostName = "c6401.apache.ambari.org";
        final java.lang.String expectedHostNameTwo = "c6402.ambari.apache.org";
        final java.lang.String expectedHostGroupName = "host_group_1";
        final java.lang.String expectedHostGroupNameTwo = "host_group_2";
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> configProperties = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.lang.String> coreSiteProperties = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.lang.String> webHCatSiteProperties = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.lang.String> oozieSiteProperties = new java.util.HashMap<>();
        configProperties.put("core-site", coreSiteProperties);
        configProperties.put("webhcat-site", webHCatSiteProperties);
        configProperties.put("oozie-site", oozieSiteProperties);
        coreSiteProperties.put("hadoop.proxyuser.knox.hosts", (expectedHostName + ",") + expectedHostNameTwo);
        webHCatSiteProperties.put("webhcat.proxyuser.knox.hosts", (expectedHostName + ",") + expectedHostNameTwo);
        oozieSiteProperties.put("hadoop.proxyuser.knox.hosts", (expectedHostName + ",") + expectedHostNameTwo);
        oozieSiteProperties.put("oozie.service.ProxyUserService.proxyuser.knox.hosts", (expectedHostName + ",") + expectedHostNameTwo);
        org.apache.ambari.server.topology.Configuration clusterConfig = new org.apache.ambari.server.topology.Configuration(configProperties, java.util.Collections.emptyMap());
        java.util.Collection<java.lang.String> groupComponents = new java.util.HashSet<>();
        groupComponents.add("KNOX_GATEWAY");
        java.util.Collection<java.lang.String> hosts = new java.util.ArrayList<>();
        hosts.add(expectedHostName);
        hosts.add("serverTwo");
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup group = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup(expectedHostGroupName, groupComponents, hosts);
        java.util.Collection<java.lang.String> groupComponents2 = new java.util.HashSet<>();
        groupComponents2.add("KNOX_GATEWAY");
        java.util.Collection<java.lang.String> hosts2 = new java.util.ArrayList<>();
        hosts2.add(expectedHostNameTwo);
        hosts2.add("serverFour");
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup group2 = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup(expectedHostGroupNameTwo, groupComponents2, hosts2);
        java.util.Collection<org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup> hostGroups = new java.util.HashSet<>();
        hostGroups.add(group);
        hostGroups.add(group2);
        org.apache.ambari.server.topology.ClusterTopology topology = createClusterTopology(bp, clusterConfig, hostGroups);
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor configProcessor = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor(topology);
        configProcessor.doUpdateForBlueprintExport(org.apache.ambari.server.controller.internal.BlueprintExportType.FULL);
        org.junit.Assert.assertEquals("Knox for core-site config not properly exported", (org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.createExportedHostName(expectedHostGroupName) + ",") + org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.createExportedHostName(expectedHostGroupNameTwo), coreSiteProperties.get("hadoop.proxyuser.knox.hosts"));
        org.junit.Assert.assertEquals("Knox config for WebHCat not properly exported", (org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.createExportedHostName(expectedHostGroupName) + ",") + org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.createExportedHostName(expectedHostGroupNameTwo), webHCatSiteProperties.get("webhcat.proxyuser.knox.hosts"));
        org.junit.Assert.assertEquals("Knox config for Oozie not properly exported", (org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.createExportedHostName(expectedHostGroupName) + ",") + org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.createExportedHostName(expectedHostGroupNameTwo), oozieSiteProperties.get("hadoop.proxyuser.knox.hosts"));
        org.junit.Assert.assertEquals("Knox config for Oozie not properly exported", (org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.createExportedHostName(expectedHostGroupName) + ",") + org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.createExportedHostName(expectedHostGroupNameTwo), oozieSiteProperties.get("oozie.service.ProxyUserService.proxyuser.knox.hosts"));
    }

    @org.junit.Test
    public void testKafkaConfigExported() throws java.lang.Exception {
        final java.lang.String expectedHostName = "c6401.apache.ambari.org";
        final java.lang.String expectedHostGroupName = "host_group_1";
        final java.lang.String expectedPortNumberOne = "2112";
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> configProperties = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.lang.String> kafkaBrokerProperties = new java.util.HashMap<>();
        configProperties.put("kafka-broker", kafkaBrokerProperties);
        kafkaBrokerProperties.put("kafka.ganglia.metrics.host", org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.createHostAddress(expectedHostName, expectedPortNumberOne));
        org.apache.ambari.server.topology.Configuration clusterConfig = new org.apache.ambari.server.topology.Configuration(configProperties, java.util.Collections.emptyMap());
        java.util.Collection<java.lang.String> groupComponents = new java.util.HashSet<>();
        groupComponents.add("KAFKA_BROKER");
        java.util.Collection<java.lang.String> hosts = new java.util.ArrayList<>();
        hosts.add(expectedHostName);
        hosts.add("serverTwo");
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup group = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup(expectedHostGroupName, groupComponents, hosts);
        java.util.Collection<java.lang.String> groupComponents2 = new java.util.HashSet<>();
        groupComponents2.add("NAMENODE");
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup group2 = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup("group2", groupComponents2, java.util.Collections.singleton("group2Host"));
        java.util.Collection<org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup> hostGroups = new java.util.HashSet<>();
        hostGroups.add(group);
        hostGroups.add(group2);
        org.apache.ambari.server.topology.ClusterTopology topology = createClusterTopology(bp, clusterConfig, hostGroups);
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor configProcessor = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor(topology);
        configProcessor.doUpdateForBlueprintExport(org.apache.ambari.server.controller.internal.BlueprintExportType.FULL);
        org.junit.Assert.assertEquals("kafka Ganglia config not properly exported", org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.createExportedHostName(expectedHostGroupName, expectedPortNumberOne), kafkaBrokerProperties.get("kafka.ganglia.metrics.host"));
    }

    @org.junit.Test
    public void testPropertyWithUndefinedHostisExported() throws java.lang.Exception {
        final java.lang.String expectedHostName = "c6401.apache.ambari.org";
        final java.lang.String expectedHostGroupName = "host_group_1";
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> configProperties = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.lang.String> properties = new java.util.HashMap<>();
        configProperties.put("storm-site", properties);
        properties.put("storm.zookeeper.servers", expectedHostName);
        properties.put("nimbus.childopts", "undefined");
        properties.put("worker.childopts", "some other info, undefined, more info");
        org.apache.ambari.server.topology.Configuration clusterConfig = new org.apache.ambari.server.topology.Configuration(configProperties, java.util.Collections.emptyMap());
        java.util.Collection<java.lang.String> groupComponents = new java.util.HashSet<>();
        groupComponents.add("ZOOKEEPER_SERVER");
        java.util.Collection<java.lang.String> hosts = new java.util.ArrayList<>();
        hosts.add(expectedHostName);
        hosts.add("serverTwo");
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup group = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup(expectedHostGroupName, groupComponents, hosts);
        java.util.Collection<org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup> hostGroups = new java.util.HashSet<>();
        hostGroups.add(group);
        org.apache.ambari.server.topology.ClusterTopology topology = createClusterTopology(bp, clusterConfig, hostGroups);
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor configProcessor = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor(topology);
        configProcessor.doUpdateForBlueprintExport(org.apache.ambari.server.controller.internal.BlueprintExportType.FULL);
        org.junit.Assert.assertEquals("Property was incorrectly exported", ("%HOSTGROUP::" + expectedHostGroupName) + "%", properties.get("storm.zookeeper.servers"));
        org.junit.Assert.assertEquals("Property with undefined host was incorrectly exported", "undefined", properties.get("nimbus.childopts"));
        org.junit.Assert.assertEquals("Property with undefined host was incorrectly exported", "some other info, undefined, more info", properties.get("worker.childopts"));
    }

    @org.junit.Test
    public void testDoUpdateForClusterCreate_SingleHostProperty__defaultValue() throws java.lang.Exception {
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> properties = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.lang.String> typeProps = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.lang.String> typeProps2 = new java.util.HashMap<>();
        typeProps.put("yarn.resourcemanager.hostname", "localhost");
        typeProps2.put("oozie_heapsize", "1024");
        typeProps2.put("oozie_permsize", "128");
        properties.put("oozie-env", typeProps2);
        properties.put("yarn-site", typeProps);
        org.apache.ambari.server.topology.Configuration clusterConfig = new org.apache.ambari.server.topology.Configuration(properties, java.util.Collections.emptyMap());
        java.util.Collection<java.lang.String> group1Components = new java.util.HashSet<>();
        group1Components.add("NAMENODE");
        group1Components.add("SECONDARY_NAMENODE");
        group1Components.add("RESOURCEMANAGER");
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup group1 = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup("group1", group1Components, java.util.Collections.singleton("testhost"));
        java.util.Collection<java.lang.String> group2Components = new java.util.HashSet<>();
        group2Components.add("DATANODE");
        group2Components.add("HDFS_CLIENT");
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup group2 = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup("group2", group2Components, java.util.Collections.singleton("testhost2"));
        java.util.Collection<org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup> hostGroups = new java.util.HashSet<>();
        hostGroups.add(group1);
        hostGroups.add(group2);
        org.apache.ambari.server.topology.ClusterTopology topology = createClusterTopology(bp, clusterConfig, hostGroups);
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor updater = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor(topology);
        java.util.Set<java.lang.String> configTypesUpdated = updater.doUpdateForClusterCreate();
        java.lang.String updatedVal = properties.get("yarn-site").get("yarn.resourcemanager.hostname");
        org.junit.Assert.assertEquals("testhost", updatedVal);
        java.lang.String updatedVal1 = properties.get("oozie-env").get("oozie_heapsize");
        org.junit.Assert.assertEquals("1024m", updatedVal1);
        java.lang.String updatedVal2 = properties.get("oozie-env").get("oozie_permsize");
        org.junit.Assert.assertEquals("128m", updatedVal2);
        org.junit.Assert.assertEquals("Incorrect number of config types updated", 3, configTypesUpdated.size());
        org.junit.Assert.assertTrue("Expected config type not updated", configTypesUpdated.contains("oozie-env"));
        org.junit.Assert.assertTrue("Expected config type not updated", configTypesUpdated.contains("yarn-site"));
        org.junit.Assert.assertTrue("Expected config type not updated", configTypesUpdated.contains("cluster-env"));
    }

    @org.junit.Test
    public void testDoUpdateForClusterCreate_HostgroupReplacement_DefaultUpdater() throws java.lang.Exception {
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> stackProperties = new java.util.HashMap<>(com.google.common.collect.ImmutableMap.of("hdfs-site", new java.util.HashMap<>(com.google.common.collect.ImmutableMap.of("dfs.http.address", "testhost2")), "myservice-env", new java.util.HashMap<>(com.google.common.collect.ImmutableMap.of("myservice_master_address", "%HOSTGROUP::group1%:8080", "myservice_some_other_property", "some_value"))));
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> hostGroupProperties = new java.util.HashMap<>(com.google.common.collect.ImmutableMap.of("hdfs-site", new java.util.HashMap<>(com.google.common.collect.ImmutableMap.of("dfs.https.address", "testhost3")), "myservice-site", new java.util.HashMap<>(com.google.common.collect.ImmutableMap.of("myservice_slave_address", "%HOSTGROUP::group1%:8080"))));
        hostGroupProperties.get("hdfs-site").put("null_property", null);
        org.apache.ambari.server.topology.Configuration clusterConfig = new org.apache.ambari.server.topology.Configuration(new java.util.HashMap<>(), new java.util.HashMap<>());
        clusterConfig.setParentConfiguration(new org.apache.ambari.server.topology.Configuration(stackProperties, java.util.Collections.emptyMap()));
        org.apache.ambari.server.topology.Configuration hostGroupConfig = new org.apache.ambari.server.topology.Configuration(hostGroupProperties, java.util.Collections.emptyMap());
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup group1 = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup("group1", com.google.common.collect.Sets.newHashSet("NAMENODE", "SECONDARY_NAMENODE", "RESOURCEMANAGER"), java.util.Collections.singleton("testhost"), hostGroupConfig);
        java.util.Collection<org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup> hostGroups = com.google.common.collect.Sets.newHashSet(group1);
        org.apache.ambari.server.topology.ClusterTopology topology = createClusterTopology(bp, clusterConfig, hostGroups);
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor updater = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor(topology);
        java.util.Set<java.lang.String> configTypesUpdated = updater.doUpdateForClusterCreate();
        org.junit.Assert.assertTrue(configTypesUpdated.containsAll(com.google.common.collect.ImmutableList.of("myservice-env", "myservice-site")));
        org.junit.Assert.assertTrue(!configTypesUpdated.contains("hdfs-site"));
        java.util.Map<java.lang.String, java.lang.String> clusterHdfsSite = clusterConfig.getProperties().get("hdfs-site");
        org.junit.Assert.assertTrue(clusterHdfsSite.containsKey("dfs.http.address"));
        java.util.Map<java.lang.String, java.lang.String> myserviceEnv = clusterConfig.getProperties().get("myservice-env");
        org.junit.Assert.assertEquals(com.google.common.collect.ImmutableMap.of("myservice_master_address", "testhost:8080"), myserviceEnv);
        java.util.Map<java.lang.String, java.lang.String> myserviceSite = hostGroupConfig.getProperties().get("myservice-site");
        org.junit.Assert.assertEquals(myserviceSite, com.google.common.collect.ImmutableMap.of("myservice_slave_address", "testhost:8080"));
    }

    @org.junit.Test
    public void testHostgroupUpdater() throws java.lang.Exception {
        java.util.Set<java.lang.String> components = com.google.common.collect.ImmutableSet.of("NAMENODE", "SECONDARY_NAMENODE", "RESOURCEMANAGER");
        org.apache.ambari.server.topology.Configuration hostGroupConfig = new org.apache.ambari.server.topology.Configuration(java.util.Collections.emptyMap(), java.util.Collections.emptyMap());
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup group1 = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup("master1", components, com.google.common.collect.ImmutableList.of("master1_host"), hostGroupConfig);
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup group2 = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup("master2", components, com.google.common.collect.ImmutableList.of("master2_host"), hostGroupConfig);
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup group3 = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup("master3", components, com.google.common.collect.ImmutableList.of("master3_host1", "master3_host2"), hostGroupConfig);
        org.apache.ambari.server.topology.Configuration clusterConfig = new org.apache.ambari.server.topology.Configuration(new java.util.HashMap<>(), new java.util.HashMap<>());
        org.apache.ambari.server.topology.ClusterTopology topology = createClusterTopology(bp, clusterConfig, com.google.common.collect.ImmutableList.of(group1, group2, group3));
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.HostGroupUpdater updater = org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.HostGroupUpdater.INSTANCE;
        org.junit.Assert.assertEquals("master1_host:8080", updater.updateForClusterCreate("mycomponent.url", "%HOSTGROUP::master1%:8080", clusterConfig.getProperties(), topology));
        org.junit.Assert.assertEquals("master1_host:8080,master2_host:8080", updater.updateForClusterCreate("mycomponent.urls", "%HOSTGROUP::master1%:8080,%HOSTGROUP::master2%:8080", clusterConfig.getProperties(), topology));
    }

    @org.junit.Test(expected = java.lang.IllegalArgumentException.class)
    public void testHostgroupUpdater_NonExistingHostgroup() throws java.lang.Exception {
        java.util.Set<java.lang.String> components = com.google.common.collect.ImmutableSet.of("NAMENODE", "SECONDARY_NAMENODE", "RESOURCEMANAGER");
        org.apache.ambari.server.topology.Configuration hostGroupConfig = new org.apache.ambari.server.topology.Configuration(java.util.Collections.emptyMap(), java.util.Collections.emptyMap());
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup group1 = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup("master1", components, com.google.common.collect.ImmutableList.of("master1_host"), hostGroupConfig);
        org.apache.ambari.server.topology.Configuration clusterConfig = new org.apache.ambari.server.topology.Configuration(new java.util.HashMap<>(), new java.util.HashMap<>());
        org.apache.ambari.server.topology.ClusterTopology topology = createClusterTopology(bp, clusterConfig, com.google.common.collect.ImmutableList.of(group1));
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.HostGroupUpdater updater = org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.HostGroupUpdater.INSTANCE;
        updater.updateForClusterCreate("mycomponent.urls", "%HOSTGROUP::master2%:8080", clusterConfig.getProperties(), topology);
    }

    @org.junit.Test(expected = java.lang.IllegalStateException.class)
    public void testHostgroupUpdater_SameGroupMultipleTimes() throws java.lang.Exception {
        java.util.Set<java.lang.String> components = com.google.common.collect.ImmutableSet.of("NAMENODE", "SECONDARY_NAMENODE", "RESOURCEMANAGER");
        org.apache.ambari.server.topology.Configuration hostGroupConfig = new org.apache.ambari.server.topology.Configuration(java.util.Collections.emptyMap(), java.util.Collections.emptyMap());
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup group1 = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup("master1", components, com.google.common.collect.ImmutableList.of("master1_host1", "master1_host2"), hostGroupConfig);
        org.apache.ambari.server.topology.Configuration clusterConfig = new org.apache.ambari.server.topology.Configuration(new java.util.HashMap<>(), new java.util.HashMap<>());
        org.apache.ambari.server.topology.ClusterTopology topology = createClusterTopology(bp, clusterConfig, com.google.common.collect.ImmutableList.of(group1));
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.HostGroupUpdater updater = org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.HostGroupUpdater.INSTANCE;
        updater.updateForClusterCreate("mycomponent.urls", "%HOSTGROUP::master1%:8080,%HOSTGROUP::master1%:8090", clusterConfig.getProperties(), topology);
    }

    @org.junit.Test
    public void testHostgroupUpdater_getRequiredHostgroups() throws java.lang.Exception {
        java.util.Set<java.lang.String> components = com.google.common.collect.ImmutableSet.of("NAMENODE", "SECONDARY_NAMENODE", "RESOURCEMANAGER");
        org.apache.ambari.server.topology.Configuration hostGroupConfig = new org.apache.ambari.server.topology.Configuration(java.util.Collections.emptyMap(), java.util.Collections.emptyMap());
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup group1 = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup("master1", components, com.google.common.collect.ImmutableList.of("master1_host"), hostGroupConfig);
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup group2 = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup("master2", components, com.google.common.collect.ImmutableList.of("master2_host"), hostGroupConfig);
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup group3 = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup("master3", components, com.google.common.collect.ImmutableList.of("master3_host"), hostGroupConfig);
        org.apache.ambari.server.topology.Configuration clusterConfig = new org.apache.ambari.server.topology.Configuration(new java.util.HashMap<>(), new java.util.HashMap<>());
        org.apache.ambari.server.topology.ClusterTopology topology = createClusterTopology(bp, clusterConfig, com.google.common.collect.ImmutableList.of(group1, group2, group3));
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.HostGroupUpdater updater = org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.HostGroupUpdater.INSTANCE;
        org.junit.Assert.assertEquals(com.google.common.collect.ImmutableSet.of("master1"), com.google.common.collect.ImmutableSet.copyOf(updater.getRequiredHostGroups("mycomponent.url", "%HOSTGROUP::master1%:8080", clusterConfig.getProperties(), topology)));
        org.junit.Assert.assertEquals(com.google.common.collect.ImmutableSet.of("master1", "master2"), com.google.common.collect.ImmutableSet.copyOf(updater.getRequiredHostGroups("mycomponent.urls", "%HOSTGROUP::master1%:8080,%HOSTGROUP::master2%:8080", clusterConfig.getProperties(), topology)));
        org.junit.Assert.assertEquals(com.google.common.collect.ImmutableSet.of("master3"), com.google.common.collect.ImmutableSet.copyOf(updater.getRequiredHostGroups("mycomponent.urls", "%HOSTGROUP::master3%:8080", clusterConfig.getProperties(), topology)));
        org.junit.Assert.assertEquals(java.util.Collections.emptyList(), updater.getRequiredHostGroups("mycomponent.urls", null, clusterConfig.getProperties(), topology));
    }

    @org.junit.Test
    public void testDoUpdateForClusterCreate_OnlyOneUpdaterForEachProperty() throws java.lang.Exception {
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor bpConfigProcessor = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor(createClusterTopology(bp, new org.apache.ambari.server.topology.Configuration(java.util.Collections.emptyMap(), java.util.Collections.emptyMap()), java.util.Collections.emptyList()));
        java.util.Collection<java.util.Map<java.lang.String, java.util.Map<java.lang.String, org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.PropertyUpdater>>> updaters = bpConfigProcessor.createCollectionOfUpdaters();
        java.util.List<org.apache.commons.lang3.tuple.Pair<java.lang.String, java.lang.String>> propertiesWithUpdaters = updaters.stream().flatMap(map -> map.entrySet().stream()).flatMap(entry -> {
            java.lang.String configType = entry.getKey();
            return entry.getValue().keySet().stream().map(propertyName -> org.apache.commons.lang3.tuple.Pair.of(configType, propertyName));
        }).sorted().collect(java.util.stream.Collectors.toList());
        java.util.Set<org.apache.commons.lang3.tuple.Pair<java.lang.String, java.lang.String>> duplicates = new java.util.HashSet<>();
        for (com.google.common.collect.PeekingIterator<org.apache.commons.lang3.tuple.Pair<java.lang.String, java.lang.String>> it = com.google.common.collect.Iterators.peekingIterator(propertiesWithUpdaters.iterator()); it.hasNext();) {
            org.apache.commons.lang3.tuple.Pair<java.lang.String, java.lang.String> property = it.next();
            if (it.hasNext() && it.peek().equals(property)) {
                duplicates.add(property);
            }
        }
        org.junit.Assert.assertTrue("There are properties with multiple updaters: " + duplicates, duplicates.isEmpty());
    }

    @org.junit.Test
    public void testDoUpdateForClusterCreate_SingleHostProperty__defaultValue_providedInParent() throws java.lang.Exception {
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> properties = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.lang.String> yarnSiteProps = new java.util.HashMap<>();
        yarnSiteProps.put("yarn.resourcemanager.hostname", "localhost");
        properties.put("yarn-site", yarnSiteProps);
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> parentProperties = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.lang.String> parentYarnSiteProps = new java.util.HashMap<>();
        parentYarnSiteProps.put("yarn.resourcemanager.resource-tracker.address", "localhost");
        parentProperties.put("yarn-site", parentYarnSiteProps);
        org.apache.ambari.server.topology.Configuration parentClusterConfig = new org.apache.ambari.server.topology.Configuration(parentProperties, java.util.Collections.emptyMap());
        org.apache.ambari.server.topology.Configuration clusterConfig = new org.apache.ambari.server.topology.Configuration(properties, java.util.Collections.emptyMap(), parentClusterConfig);
        java.util.Collection<java.lang.String> group1Components = new java.util.HashSet<>();
        group1Components.add("NAMENODE");
        group1Components.add("SECONDARY_NAMENODE");
        group1Components.add("RESOURCEMANAGER");
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup group1 = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup("group1", group1Components, java.util.Collections.singleton("testhost"));
        java.util.Collection<java.lang.String> group2Components = new java.util.HashSet<>();
        group2Components.add("DATANODE");
        group2Components.add("HDFS_CLIENT");
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup group2 = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup("group2", group2Components, java.util.Collections.singleton("testhost2"));
        java.util.Collection<org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup> hostGroups = new java.util.HashSet<>();
        hostGroups.add(group1);
        hostGroups.add(group2);
        org.apache.ambari.server.topology.ClusterTopology topology = createClusterTopology(bp, clusterConfig, hostGroups);
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor updater = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor(topology);
        updater.doUpdateForClusterCreate();
        org.junit.Assert.assertEquals("testhost", clusterConfig.getPropertyValue("yarn-site", "yarn.resourcemanager.hostname"));
        org.junit.Assert.assertEquals("testhost", clusterConfig.getPropertyValue("yarn-site", "yarn.resourcemanager.resource-tracker.address"));
    }

    @org.junit.Test
    public void testDoUpdateForClusterCreate_SingleHostProperty__defaultValue_hostGroupConfig() throws java.lang.Exception {
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> properties = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.lang.String> yarnSiteProps = new java.util.HashMap<>();
        yarnSiteProps.put("yarn.resourcemanager.hostname", "localhost");
        properties.put("yarn-site", yarnSiteProps);
        org.apache.ambari.server.topology.Configuration clusterConfig = new org.apache.ambari.server.topology.Configuration(properties, java.util.Collections.emptyMap());
        java.util.Collection<java.lang.String> group1Components = new java.util.HashSet<>();
        group1Components.add("NAMENODE");
        group1Components.add("SECONDARY_NAMENODE");
        group1Components.add("RESOURCEMANAGER");
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup group1 = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup("group1", group1Components, java.util.Collections.singleton("testhost"));
        java.util.Collection<java.lang.String> group2Components = new java.util.HashSet<>();
        group2Components.add("DATANODE");
        group2Components.add("HDFS_CLIENT");
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> group2Properties = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.lang.String> group2YarnSiteProperties = new java.util.HashMap<>();
        group2YarnSiteProperties.put("yarn.resourcemanager.resource-tracker.address", "localhost");
        group2Properties.put("yarn-site", group2YarnSiteProperties);
        org.apache.ambari.server.topology.Configuration group2BPConfig = new org.apache.ambari.server.topology.Configuration(java.util.Collections.emptyMap(), java.util.Collections.emptyMap(), clusterConfig);
        org.apache.ambari.server.topology.Configuration group2Config = new org.apache.ambari.server.topology.Configuration(group2Properties, java.util.Collections.emptyMap(), group2BPConfig);
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup group2 = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup("group2", group2Components, java.util.Collections.singleton("testhost2"), group2Config);
        java.util.Collection<org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup> hostGroups = new java.util.HashSet<>();
        hostGroups.add(group1);
        hostGroups.add(group2);
        org.apache.ambari.server.topology.ClusterTopology topology = createClusterTopology(bp, clusterConfig, hostGroups);
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor updater = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor(topology);
        updater.doUpdateForClusterCreate();
        org.junit.Assert.assertEquals("testhost", clusterConfig.getPropertyValue("yarn-site", "yarn.resourcemanager.hostname"));
        org.junit.Assert.assertEquals("testhost", group2Config.getProperties().get("yarn-site").get("yarn.resourcemanager.resource-tracker.address"));
    }

    @org.junit.Test
    public void testDoUpdateForClusterCreate_SingleHostProperty__defaultValue_BPHostGroupConfig() throws java.lang.Exception {
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> properties = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.lang.String> yarnSiteProps = new java.util.HashMap<>();
        yarnSiteProps.put("yarn.resourcemanager.hostname", "localhost");
        properties.put("yarn-site", yarnSiteProps);
        org.apache.ambari.server.topology.Configuration clusterConfig = new org.apache.ambari.server.topology.Configuration(properties, java.util.Collections.emptyMap());
        java.util.Collection<java.lang.String> group1Components = new java.util.HashSet<>();
        group1Components.add("NAMENODE");
        group1Components.add("SECONDARY_NAMENODE");
        group1Components.add("RESOURCEMANAGER");
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup group1 = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup("group1", group1Components, java.util.Collections.singleton("testhost"));
        java.util.Collection<java.lang.String> group2Components = new java.util.HashSet<>();
        group2Components.add("DATANODE");
        group2Components.add("HDFS_CLIENT");
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> group2BPProperties = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.lang.String> group2YarnSiteProperties = new java.util.HashMap<>();
        group2YarnSiteProperties.put("yarn.resourcemanager.resource-tracker.address", "localhost");
        group2BPProperties.put("yarn-site", group2YarnSiteProperties);
        org.apache.ambari.server.topology.Configuration group2BPConfig = new org.apache.ambari.server.topology.Configuration(group2BPProperties, java.util.Collections.emptyMap(), clusterConfig);
        org.apache.ambari.server.topology.Configuration group2Config = new org.apache.ambari.server.topology.Configuration(new java.util.HashMap<>(), java.util.Collections.emptyMap());
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup group2 = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup("group2", group2Components, java.util.Collections.singleton("testhost2"), group2Config);
        java.util.Collection<org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup> hostGroups = new java.util.HashSet<>();
        hostGroups.add(group1);
        hostGroups.add(group2);
        org.apache.ambari.server.topology.ClusterTopology topology = createClusterTopology(bp, clusterConfig, hostGroups);
        topology.getHostGroupInfo().get("group2").getConfiguration().setParentConfiguration(group2BPConfig);
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor updater = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor(topology);
        updater.doUpdateForClusterCreate();
        org.junit.Assert.assertEquals("testhost", clusterConfig.getPropertyValue("yarn-site", "yarn.resourcemanager.hostname"));
        org.junit.Assert.assertEquals("testhost", group2Config.getProperties().get("yarn-site").get("yarn.resourcemanager.resource-tracker.address"));
    }

    @org.junit.Test
    public void testDoUpdateForClusterCreate_SingleHostProperty__MissingComponent() throws java.lang.Exception {
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> properties = new java.util.HashMap<>();
        properties.put("mapred-site", new java.util.HashMap<>(com.google.common.collect.ImmutableMap.of("mapreduce.job.hdfs-servers", "localhost")));
        org.apache.ambari.server.topology.Configuration clusterConfig = new org.apache.ambari.server.topology.Configuration(properties, java.util.Collections.emptyMap());
        java.util.Collection<java.lang.String> group1Components = new java.util.HashSet<>();
        group1Components.add("SECONDARY_NAMENODE");
        group1Components.add("RESOURCEMANAGER");
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup group1 = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup("group1", group1Components, java.util.Collections.singleton("testhost"));
        java.util.Collection<java.lang.String> group2Components = new java.util.HashSet<>();
        group2Components.add("DATANODE");
        group2Components.add("HDFS_CLIENT");
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup group2 = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup("group2", group2Components, java.util.Collections.singleton("testhost2"));
        java.util.Collection<org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup> hostGroups = new java.util.HashSet<>();
        hostGroups.add(group1);
        hostGroups.add(group2);
        EasyMock.expect(stack.getCardinality("NAMENODE")).andReturn(new org.apache.ambari.server.topology.Cardinality("1")).anyTimes();
        org.apache.ambari.server.topology.ClusterTopology topology = createClusterTopology(bp, clusterConfig, hostGroups);
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor updater = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor(topology);
        try {
            updater.doUpdateForClusterCreate();
            org.junit.Assert.fail("IllegalArgumentException should have been thrown");
        } catch (java.lang.IllegalArgumentException illegalArgumentException) {
        }
    }

    @org.junit.Test
    public void testDoUpdateForClusterCreate_SingleHostProperty__MissingComponent_NoValidationForFqdn() throws java.lang.Exception {
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> properties = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.lang.String> typeProps = new java.util.HashMap<>();
        properties.put("mapred-site", new java.util.HashMap<>(com.google.common.collect.ImmutableMap.of("mapreduce.job.hdfs-servers", "www.externalnamenode.org")));
        org.apache.ambari.server.topology.Configuration clusterConfig = new org.apache.ambari.server.topology.Configuration(properties, java.util.Collections.emptyMap());
        java.util.Collection<java.lang.String> group1Components = new java.util.HashSet<>();
        group1Components.add("SECONDARY_NAMENODE");
        group1Components.add("RESOURCEMANAGER");
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup group1 = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup("group1", group1Components, java.util.Collections.singleton("testhost"));
        java.util.Collection<java.lang.String> group2Components = new java.util.HashSet<>();
        group2Components.add("DATANODE");
        group2Components.add("HDFS_CLIENT");
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup group2 = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup("group2", group2Components, java.util.Collections.singleton("testhost2"));
        java.util.Collection<org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup> hostGroups = new java.util.HashSet<>();
        hostGroups.add(group1);
        hostGroups.add(group2);
        EasyMock.expect(stack.getCardinality("NAMENODE")).andReturn(new org.apache.ambari.server.topology.Cardinality("1")).anyTimes();
        org.apache.ambari.server.topology.ClusterTopology topology = createClusterTopology(bp, clusterConfig, hostGroups);
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor updater = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor(topology);
        updater.doUpdateForClusterCreate();
        org.junit.Assert.assertEquals("www.externalnamenode.org", clusterConfig.getPropertyValue("mapred-site", "mapreduce.job.hdfs-servers"));
    }

    @org.junit.Test
    public void testDoUpdateForClusterCreate_SingleHostProperty__MultipleMatchingHostGroupsError() throws java.lang.Exception {
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> properties = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.lang.String> typeProps = new java.util.HashMap<>();
        properties.put("mapred-site", new java.util.HashMap<>(com.google.common.collect.ImmutableMap.of("mapreduce.job.hdfs-servers", "localhost")));
        org.apache.ambari.server.topology.Configuration clusterConfig = new org.apache.ambari.server.topology.Configuration(properties, java.util.Collections.emptyMap());
        java.util.Collection<java.lang.String> group1Components = new java.util.HashSet<>();
        group1Components.add("NAMENODE");
        group1Components.add("SECONDARY_NAMENODE");
        group1Components.add("RESOURCEMANAGER");
        group1Components.add("APP_TIMELINE_SERVER");
        group1Components.add("TIMELINE_READER");
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup group1 = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup("group1", group1Components, java.util.Collections.singleton("testhost"));
        java.util.Collection<java.lang.String> group2Components = new java.util.HashSet<>();
        group2Components.add("NAMENODE");
        group2Components.add("DATANODE");
        group2Components.add("HDFS_CLIENT");
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup group2 = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup("group2", group2Components, java.util.Collections.singleton("testhost2"));
        java.util.Collection<org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup> hostGroups = new java.util.HashSet<>();
        hostGroups.add(group1);
        hostGroups.add(group2);
        EasyMock.expect(stack.getCardinality("NAMENODE")).andReturn(new org.apache.ambari.server.topology.Cardinality("0-1")).anyTimes();
        org.apache.ambari.server.topology.ClusterTopology topology = createClusterTopology(bp, clusterConfig, hostGroups);
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor updater = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor(topology);
        try {
            updater.doUpdateForClusterCreate();
            org.junit.Assert.fail("IllegalArgumentException should have been thrown");
        } catch (java.lang.IllegalArgumentException illegalArgumentException) {
        }
    }

    @org.junit.Test
    public void testDoUpdateForClusterCreate_SingleHostProperty__MultipleAppTimelineServer() throws java.lang.Exception {
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> properties = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.lang.String> typeProps = new java.util.HashMap<>();
        typeProps.put("yarn.timeline-service.address", "testhost:10200");
        typeProps.put("yarn.timeline-service.webapp.address", "testhost:8188");
        typeProps.put("yarn.timeline-service.webapp.https.address", "testhost:8190");
        properties.put("yarn-site", typeProps);
        org.apache.ambari.server.topology.Configuration clusterConfig = new org.apache.ambari.server.topology.Configuration(properties, java.util.Collections.emptyMap());
        java.util.Collection<java.lang.String> group1Components = new java.util.HashSet<>();
        group1Components.add("NAMENODE");
        group1Components.add("SECONDARY_NAMENODE");
        group1Components.add("RESOURCEMANAGER");
        group1Components.add("APP_TIMELINE_SERVER");
        group1Components.add("TIMELINE_READER");
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup group1 = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup("group1", group1Components, java.util.Collections.singleton("testhost"));
        java.util.Collection<java.lang.String> group2Components = new java.util.HashSet<>();
        group2Components.add("DATANODE");
        group2Components.add("HDFS_CLIENT");
        group2Components.add("APP_TIMELINE_SERVER");
        group2Components.add("TIMELINE_READER");
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup group2 = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup("group2", group2Components, java.util.Collections.singleton("testhost2"));
        java.util.Collection<org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup> hostGroups = new java.util.HashSet<>();
        hostGroups.add(group1);
        hostGroups.add(group2);
        EasyMock.expect(stack.getCardinality("APP_TIMELINE_SERVER")).andReturn(new org.apache.ambari.server.topology.Cardinality("0-1")).anyTimes();
        EasyMock.expect(stack.getCardinality("TIMELINE_READER")).andReturn(new org.apache.ambari.server.topology.Cardinality("0-1")).anyTimes();
        org.apache.ambari.server.topology.ClusterTopology topology = createClusterTopology(bp, clusterConfig, hostGroups);
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor updater = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor(topology);
        updater.doUpdateForClusterCreate();
        java.lang.String updatedVal = topology.getConfiguration().getFullProperties().get("yarn-site").get("yarn.timeline-service.address");
        org.junit.Assert.assertEquals("Timeline Server config property should not have been updated", "testhost:10200", updatedVal);
    }

    @org.junit.Test
    public void testDoUpdateForClusterCreate_SingleHostProperty__MissingOptionalComponent() throws java.lang.Exception {
        final java.lang.String expectedHostName = "localhost";
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> properties = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.lang.String> typeProps = new java.util.HashMap<>();
        typeProps.put("yarn.timeline-service.address", expectedHostName);
        properties.put("yarn-site", typeProps);
        org.apache.ambari.server.topology.Configuration clusterConfig = new org.apache.ambari.server.topology.Configuration(properties, java.util.Collections.emptyMap());
        java.util.Collection<java.lang.String> group1Components = new java.util.HashSet<>();
        group1Components.add("NAMENODE");
        group1Components.add("SECONDARY_NAMENODE");
        group1Components.add("RESOURCEMANAGER");
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup group1 = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup("group1", group1Components, java.util.Collections.singleton("testhost"));
        java.util.Collection<java.lang.String> group2Components = new java.util.HashSet<>();
        group2Components.add("DATANODE");
        group2Components.add("HDFS_CLIENT");
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup group2 = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup("group2", group2Components, java.util.Collections.singleton("testhost2"));
        java.util.Collection<org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup> hostGroups = new java.util.HashSet<>();
        hostGroups.add(group1);
        hostGroups.add(group2);
        EasyMock.expect(stack.getCardinality("APP_TIMELINE_SERVER")).andReturn(new org.apache.ambari.server.topology.Cardinality("0-1")).anyTimes();
        EasyMock.expect(stack.getCardinality("TIMELINE_READER")).andReturn(new org.apache.ambari.server.topology.Cardinality("0-1")).anyTimes();
        org.apache.ambari.server.topology.ClusterTopology topology = createClusterTopology(bp, clusterConfig, hostGroups);
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor updater = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor(topology);
        updater.doUpdateForClusterCreate();
        java.lang.String updatedVal = topology.getConfiguration().getFullProperties().get("yarn-site").get("yarn.timeline-service.address");
        org.junit.Assert.assertEquals("Timeline Server config property should not have been updated", expectedHostName, updatedVal);
    }

    @org.junit.Test
    public void testDoUpdateForClusterCreate_SingleHostProperty__defaultValue__WithPort() throws java.lang.Exception {
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> properties = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.lang.String> typeProps = new java.util.HashMap<>();
        typeProps.put("fs.defaultFS", "localhost:5050");
        properties.put("core-site", typeProps);
        org.apache.ambari.server.topology.Configuration clusterConfig = new org.apache.ambari.server.topology.Configuration(properties, java.util.Collections.emptyMap());
        java.util.Collection<java.lang.String> hgComponents = new java.util.HashSet<>();
        hgComponents.add("NAMENODE");
        hgComponents.add("SECONDARY_NAMENODE");
        hgComponents.add("RESOURCEMANAGER");
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup group1 = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup("group1", hgComponents, java.util.Collections.singleton("testhost"));
        java.util.Collection<java.lang.String> hgComponents2 = new java.util.HashSet<>();
        hgComponents2.add("DATANODE");
        hgComponents2.add("HDFS_CLIENT");
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup group2 = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup("group2", hgComponents2, java.util.Collections.singleton("testhost2"));
        java.util.Collection<org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup> hostGroups = new java.util.HashSet<>();
        hostGroups.add(group1);
        hostGroups.add(group2);
        org.apache.ambari.server.topology.ClusterTopology topology = createClusterTopology(bp, clusterConfig, hostGroups);
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor updater = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor(topology);
        updater.doUpdateForClusterCreate();
        java.lang.String updatedVal = topology.getConfiguration().getFullProperties().get("core-site").get("fs.defaultFS");
        org.junit.Assert.assertEquals("testhost:5050", updatedVal);
    }

    @org.junit.Test
    public void testDoUpdateForClusterCreate_MultiHostProperty__defaultValues() throws java.lang.Exception {
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> properties = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.lang.String> typeProps = new java.util.HashMap<>();
        typeProps.put("hbase.zookeeper.quorum", "localhost");
        properties.put("hbase-site", typeProps);
        java.util.Map<java.lang.String, java.lang.String> livyConf = new java.util.HashMap<>();
        livyConf.put("livy.server.recovery.state-store.url", "/livy2-recovery");
        properties.put("livy2-conf", livyConf);
        java.util.Map<java.lang.String, java.lang.String> originalLivyConf = com.google.common.collect.ImmutableMap.copyOf(livyConf);
        org.apache.ambari.server.topology.Configuration clusterConfig = new org.apache.ambari.server.topology.Configuration(properties, java.util.Collections.emptyMap());
        java.util.Collection<java.lang.String> hgComponents = new java.util.HashSet<>();
        hgComponents.add("NAMENODE");
        hgComponents.add("SECONDARY_NAMENODE");
        hgComponents.add("ZOOKEEPER_SERVER");
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup group1 = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup("group1", hgComponents, java.util.Collections.singleton("testhost"));
        java.util.Collection<java.lang.String> hgComponents2 = new java.util.HashSet<>();
        hgComponents2.add("DATANODE");
        hgComponents2.add("HDFS_CLIENT");
        hgComponents2.add("ZOOKEEPER_SERVER");
        java.util.Set<java.lang.String> hosts2 = new java.util.HashSet<>();
        hosts2.add("testhost2");
        hosts2.add("testhost2a");
        hosts2.add("testhost2b");
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup group2 = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup("group2", hgComponents2, hosts2);
        java.util.Collection<java.lang.String> hgComponents3 = new java.util.HashSet<>();
        hgComponents2.add("HDFS_CLIENT");
        hgComponents2.add("ZOOKEEPER_CLIENT");
        java.util.Set<java.lang.String> hosts3 = new java.util.HashSet<>();
        hosts3.add("testhost3");
        hosts3.add("testhost3a");
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup group3 = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup("group3", hgComponents3, hosts3);
        java.util.Collection<org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup> hostGroups = new java.util.HashSet<>();
        hostGroups.add(group1);
        hostGroups.add(group2);
        hostGroups.add(group3);
        org.apache.ambari.server.topology.ClusterTopology topology = createClusterTopology(bp, clusterConfig, hostGroups);
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor updater = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor(topology);
        updater.doUpdateForClusterCreate();
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> fullProperties = topology.getConfiguration().getFullProperties();
        java.lang.String updatedVal = fullProperties.get("hbase-site").get("hbase.zookeeper.quorum");
        java.lang.String[] hosts = updatedVal.split(",");
        java.util.Collection<java.lang.String> expectedHosts = new java.util.HashSet<>();
        expectedHosts.add("testhost");
        expectedHosts.add("testhost2");
        expectedHosts.add("testhost2a");
        expectedHosts.add("testhost2b");
        org.junit.Assert.assertEquals(4, hosts.length);
        for (java.lang.String host : hosts) {
            org.junit.Assert.assertTrue(expectedHosts.contains(host));
            expectedHosts.remove(host);
        }
        org.junit.Assert.assertEquals(originalLivyConf, fullProperties.get("livy2-conf"));
    }

    @org.junit.Test
    public void testDoUpdateForClusterCreate_MultiHostProperty__defaultValues___withPorts() throws java.lang.Exception {
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> properties = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.lang.String> typeProps = new java.util.HashMap<>();
        typeProps.put("templeton.zookeeper.hosts", "localhost:9090");
        properties.put("webhcat-site", typeProps);
        org.apache.ambari.server.topology.Configuration clusterConfig = new org.apache.ambari.server.topology.Configuration(properties, java.util.Collections.emptyMap());
        java.util.Collection<java.lang.String> hgComponents = new java.util.HashSet<>();
        hgComponents.add("NAMENODE");
        hgComponents.add("SECONDARY_NAMENODE");
        hgComponents.add("ZOOKEEPER_SERVER");
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup group1 = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup("group1", hgComponents, java.util.Collections.singleton("testhost"));
        java.util.Collection<java.lang.String> hgComponents2 = new java.util.HashSet<>();
        hgComponents2.add("DATANODE");
        hgComponents2.add("HDFS_CLIENT");
        hgComponents2.add("ZOOKEEPER_SERVER");
        java.util.Set<java.lang.String> hosts2 = new java.util.HashSet<>();
        hosts2.add("testhost2");
        hosts2.add("testhost2a");
        hosts2.add("testhost2b");
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup group2 = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup("group2", hgComponents2, hosts2);
        java.util.Collection<java.lang.String> hgComponents3 = new java.util.HashSet<>();
        hgComponents2.add("HDFS_CLIENT");
        hgComponents2.add("ZOOKEEPER_CLIENT");
        java.util.Set<java.lang.String> hosts3 = new java.util.HashSet<>();
        hosts3.add("testhost3");
        hosts3.add("testhost3a");
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup group3 = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup("group3", hgComponents3, hosts3);
        java.util.Collection<org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup> hostGroups = new java.util.HashSet<>();
        hostGroups.add(group1);
        hostGroups.add(group2);
        hostGroups.add(group3);
        org.apache.ambari.server.topology.ClusterTopology topology = createClusterTopology(bp, clusterConfig, hostGroups);
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor updater = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor(topology);
        updater.doUpdateForClusterCreate();
        java.lang.String updatedVal = topology.getConfiguration().getFullProperties().get("webhcat-site").get("templeton.zookeeper.hosts");
        java.lang.String[] hosts = updatedVal.split(",");
        java.util.Collection<java.lang.String> expectedHosts = new java.util.HashSet<>();
        expectedHosts.add("testhost:9090");
        expectedHosts.add("testhost2:9090");
        expectedHosts.add("testhost2a:9090");
        expectedHosts.add("testhost2b:9090");
        org.junit.Assert.assertEquals(4, hosts.length);
        for (java.lang.String host : hosts) {
            org.junit.Assert.assertTrue(expectedHosts.contains(host));
            expectedHosts.remove(host);
        }
    }

    @org.junit.Test
    public void testMultipleHostTopologyUpdater__localhost__singleHost() throws java.lang.Exception {
        final java.lang.String typeName = "hbase-site";
        final java.lang.String propertyName = "hbase.zookeeper.quorum";
        final java.lang.String originalValue = "localhost";
        final java.lang.String component1 = "ZOOKEEPER_SERVER";
        final java.lang.String component2 = "ZOOKEEPER_CLIENT";
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> properties = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.lang.String> typeProps = new java.util.HashMap<>();
        typeProps.put(propertyName, originalValue);
        properties.put(typeName, typeProps);
        org.apache.ambari.server.topology.Configuration clusterConfig = new org.apache.ambari.server.topology.Configuration(properties, java.util.Collections.emptyMap());
        java.util.Collection<java.lang.String> hgComponents = new java.util.HashSet<>();
        hgComponents.add(component1);
        java.util.Set<java.lang.String> hosts1 = new java.util.HashSet<>();
        hosts1.add("testhost1a");
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup group1 = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup("group1", hgComponents, hosts1);
        java.util.Collection<java.lang.String> hgComponents2 = new java.util.HashSet<>();
        hgComponents2.add(component2);
        java.util.Set<java.lang.String> hosts2 = new java.util.HashSet<>();
        hosts2.add("testhost2");
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup group2 = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup("group2", hgComponents2, hosts2);
        java.util.Collection<org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup> hostGroups = new java.util.HashSet<>();
        hostGroups.add(group1);
        hostGroups.add(group2);
        org.apache.ambari.server.topology.ClusterTopology topology = createClusterTopology(bp, clusterConfig, hostGroups);
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.MultipleHostTopologyUpdater mhtu = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.MultipleHostTopologyUpdater(component1);
        java.lang.String newValue = mhtu.updateForClusterCreate(propertyName, originalValue, properties, topology);
        org.junit.Assert.assertEquals("testhost1a", newValue);
    }

    @org.junit.Test
    public void testMultipleHostTopologyUpdater__localhost__singleHostGroup() throws java.lang.Exception {
        final java.lang.String typeName = "hbase-site";
        final java.lang.String propertyName = "hbase.zookeeper.quorum";
        final java.lang.String originalValue = "localhost";
        final java.lang.String component1 = "ZOOKEEPER_SERVER";
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> properties = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.lang.String> typeProps = new java.util.HashMap<>();
        typeProps.put(propertyName, originalValue);
        properties.put(typeName, typeProps);
        org.apache.ambari.server.topology.Configuration clusterConfig = new org.apache.ambari.server.topology.Configuration(properties, java.util.Collections.emptyMap());
        java.util.Collection<java.lang.String> hgComponents = new java.util.HashSet<>();
        hgComponents.add(component1);
        java.util.Set<java.lang.String> hosts1 = new java.util.HashSet<>();
        hosts1.add("testhost1a");
        hosts1.add("testhost1b");
        hosts1.add("testhost1c");
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup group1 = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup("group1", hgComponents, hosts1);
        java.util.Collection<org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup> hostGroups = new java.util.HashSet<>();
        hostGroups.add(group1);
        org.apache.ambari.server.topology.ClusterTopology topology = createClusterTopology(bp, clusterConfig, hostGroups);
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.MultipleHostTopologyUpdater mhtu = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.MultipleHostTopologyUpdater(component1);
        java.lang.String newValue = mhtu.updateForClusterCreate(propertyName, originalValue, properties, topology);
        java.util.List<java.lang.String> hostArray = java.util.Arrays.asList(newValue.split(","));
        org.junit.Assert.assertTrue(hostArray.containsAll(hosts1) && hosts1.containsAll(hostArray));
    }

    @org.junit.Test
    public void testMultipleHostTopologyUpdater__hostgroup__singleHostGroup() throws java.lang.Exception {
        final java.lang.String typeName = "hbase-site";
        final java.lang.String propertyName = "hbase.zookeeper.quorum";
        final java.lang.String originalValue = "%HOSTGROUP::group1%";
        final java.lang.String component1 = "ZOOKEEPER_SERVER";
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> properties = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.lang.String> typeProps = new java.util.HashMap<>();
        typeProps.put(propertyName, originalValue);
        properties.put(typeName, typeProps);
        org.apache.ambari.server.topology.Configuration clusterConfig = new org.apache.ambari.server.topology.Configuration(properties, java.util.Collections.emptyMap());
        java.util.Collection<java.lang.String> hgComponents = new java.util.HashSet<>();
        hgComponents.add(component1);
        java.util.Set<java.lang.String> hosts1 = new java.util.HashSet<>();
        hosts1.add("testhost1a");
        hosts1.add("testhost1b");
        hosts1.add("testhost1c");
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup group1 = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup("group1", hgComponents, hosts1);
        java.util.Collection<org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup> hostGroups = new java.util.HashSet<>();
        hostGroups.add(group1);
        org.apache.ambari.server.topology.ClusterTopology topology = createClusterTopology(bp, clusterConfig, hostGroups);
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.MultipleHostTopologyUpdater mhtu = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.MultipleHostTopologyUpdater(component1);
        java.lang.String newValue = mhtu.updateForClusterCreate(propertyName, originalValue, properties, topology);
        java.util.List<java.lang.String> hostArray = java.util.Arrays.asList(newValue.split(","));
        org.junit.Assert.assertTrue(hostArray.containsAll(hosts1) && hosts1.containsAll(hostArray));
    }

    @org.junit.Test
    public void testMultipleHostTopologyUpdater__hostgroup__multipleHostGroups() throws java.lang.Exception {
        final java.lang.String typeName = "application-properties";
        final java.lang.String propertyName = "atlas.rest.address";
        final java.lang.String originalValue = "http://%HOSTGROUP::group1%:21000,http://%HOSTGROUP::group2%:21000";
        final java.lang.String component = "ATLAS_SERVER";
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> properties = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.lang.String> typeProps = new java.util.HashMap<>();
        typeProps.put(propertyName, originalValue);
        properties.put(typeName, typeProps);
        org.apache.ambari.server.topology.Configuration clusterConfig = new org.apache.ambari.server.topology.Configuration(properties, java.util.Collections.emptyMap());
        java.util.Set<java.lang.String> components = com.google.common.collect.ImmutableSet.of(component);
        java.util.Set<java.lang.String> group1Hosts = com.google.common.collect.ImmutableSet.of("testhost1a", "testhost1b", "testhost1c");
        java.util.Set<java.lang.String> group2Hosts = com.google.common.collect.ImmutableSet.of("testhost2a", "testhost2b", "testhost2c");
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup group1 = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup("group1", components, group1Hosts);
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup group2 = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup("group2", components, group2Hosts);
        java.util.Collection<org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup> hostGroups = com.google.common.collect.ImmutableSet.of(group1, group2);
        org.apache.ambari.server.topology.ClusterTopology topology = createClusterTopology(bp, clusterConfig, hostGroups);
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.MultipleHostTopologyUpdater mhtu = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.MultipleHostTopologyUpdater(component, ',', true, true, true);
        java.lang.String newValue = mhtu.updateForClusterCreate(propertyName, originalValue, properties, topology);
        java.util.Set<java.lang.String> expectedAddresses = com.google.common.collect.Sets.union(group1Hosts, group2Hosts).stream().map(host -> ("http://" + host) + ":21000").collect(java.util.stream.Collectors.toSet());
        java.util.Set<java.lang.String> replacedAddresses = com.google.common.collect.ImmutableSet.copyOf(newValue.split(","));
        org.junit.Assert.assertEquals(expectedAddresses, replacedAddresses);
    }

    @org.junit.Test
    public void testDoUpdateForClusterVerifyRetrySettingsDefault() throws java.lang.Exception {
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> configProperties = new java.util.HashMap<>();
        java.util.HashMap<java.lang.String, java.lang.String> clusterEnvProperties = new java.util.HashMap<>();
        configProperties.put("cluster-env", clusterEnvProperties);
        org.apache.ambari.server.topology.Configuration clusterConfig = new org.apache.ambari.server.topology.Configuration(configProperties, java.util.Collections.emptyMap());
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup testHostGroup = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup("test-host-group-one", java.util.Collections.emptySet(), java.util.Collections.emptySet());
        org.apache.ambari.server.topology.ClusterTopology topology = createClusterTopology(bp, clusterConfig, java.util.Collections.singleton(testHostGroup));
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor updater = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor(topology);
        java.util.Set<java.lang.String> updatedConfigTypes = updater.doUpdateForClusterCreate();
        org.junit.Assert.assertEquals("Incorrect number of properties added to cluster-env for retry", 3, clusterEnvProperties.size());
        org.junit.Assert.assertEquals("command_retry_enabled was not set to the expected default", "true", clusterEnvProperties.get("command_retry_enabled"));
        org.junit.Assert.assertEquals("commands_to_retry was not set to the expected default", "INSTALL,START", clusterEnvProperties.get("commands_to_retry"));
        org.junit.Assert.assertEquals("command_retry_max_time_in_sec was not set to the expected default", "600", clusterEnvProperties.get("command_retry_max_time_in_sec"));
        org.junit.Assert.assertEquals("Incorrect number of config types updated by this operation", 1, updatedConfigTypes.size());
        org.junit.Assert.assertTrue("Expected type not included in the updated set", updatedConfigTypes.contains("cluster-env"));
    }

    @org.junit.Test
    public void testDoUpdateForClusterVerifyRetrySettingsCustomized() throws java.lang.Exception {
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> configProperties = new java.util.HashMap<>();
        java.util.HashMap<java.lang.String, java.lang.String> clusterEnvProperties = new java.util.HashMap<>();
        configProperties.put("cluster-env", clusterEnvProperties);
        clusterEnvProperties.put("command_retry_enabled", "false");
        clusterEnvProperties.put("commands_to_retry", "TEST");
        clusterEnvProperties.put("command_retry_max_time_in_sec", "1");
        org.apache.ambari.server.topology.Configuration clusterConfig = new org.apache.ambari.server.topology.Configuration(configProperties, java.util.Collections.emptyMap());
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup testHostGroup = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup("test-host-group-one", java.util.Collections.emptySet(), java.util.Collections.emptySet());
        org.apache.ambari.server.topology.ClusterTopology topology = createClusterTopology(bp, clusterConfig, java.util.Collections.singleton(testHostGroup));
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor updater = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor(topology);
        java.util.Set<java.lang.String> updatedConfigTypes = updater.doUpdateForClusterCreate();
        org.junit.Assert.assertEquals("Incorrect number of properties added to cluster-env for retry", 3, clusterEnvProperties.size());
        org.junit.Assert.assertEquals("command_retry_enabled was not set to the expected default", "false", clusterEnvProperties.get("command_retry_enabled"));
        org.junit.Assert.assertEquals("commands_to_retry was not set to the expected default", "TEST", clusterEnvProperties.get("commands_to_retry"));
        org.junit.Assert.assertEquals("command_retry_max_time_in_sec was not set to the expected default", "1", clusterEnvProperties.get("command_retry_max_time_in_sec"));
        org.junit.Assert.assertEquals("Incorrect number of config types updated", 0, updatedConfigTypes.size());
    }

    @org.junit.Test
    public void testDoUpdateForClusterWithNameNodeHAEnabledSpecifyingHostNamesDirectly() throws java.lang.Exception {
        final java.lang.String expectedNameService = "mynameservice";
        final java.lang.String expectedHostName = "c6401.apache.ambari.org";
        final java.lang.String expectedHostNameTwo = "server-two";
        final java.lang.String expectedPortNum = "808080";
        final java.lang.String expectedNodeOne = "nn1";
        final java.lang.String expectedNodeTwo = "nn2";
        final java.lang.String expectedHostGroupName = "host_group_1";
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> configProperties = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.lang.String> hdfsSiteProperties = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.lang.String> hbaseSiteProperties = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.lang.String> hadoopEnvProperties = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.lang.String> coreSiteProperties = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.lang.String> accumuloSiteProperties = new java.util.HashMap<>();
        configProperties.put("hdfs-site", hdfsSiteProperties);
        configProperties.put("hadoop-env", hadoopEnvProperties);
        configProperties.put("core-site", coreSiteProperties);
        configProperties.put("hbase-site", hbaseSiteProperties);
        configProperties.put("accumulo-site", accumuloSiteProperties);
        hdfsSiteProperties.put("dfs.nameservices", expectedNameService);
        hdfsSiteProperties.put("dfs.ha.namenodes.mynameservice", (expectedNodeOne + ", ") + expectedNodeTwo);
        hdfsSiteProperties.put((("dfs.namenode.https-address." + expectedNameService) + ".") + expectedNodeOne, org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.createHostAddress(expectedHostName, expectedPortNum));
        hdfsSiteProperties.put((("dfs.namenode.https-address." + expectedNameService) + ".") + expectedNodeTwo, org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.createHostAddress(expectedHostNameTwo, expectedPortNum));
        hdfsSiteProperties.put((("dfs.namenode.http-address." + expectedNameService) + ".") + expectedNodeOne, org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.createHostAddress(expectedHostName, expectedPortNum));
        hdfsSiteProperties.put((("dfs.namenode.http-address." + expectedNameService) + ".") + expectedNodeTwo, org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.createHostAddress(expectedHostNameTwo, expectedPortNum));
        hdfsSiteProperties.put((("dfs.namenode.rpc-address." + expectedNameService) + ".") + expectedNodeOne, org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.createHostAddress(expectedHostName, expectedPortNum));
        hdfsSiteProperties.put((("dfs.namenode.rpc-address." + expectedNameService) + ".") + expectedNodeTwo, org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.createHostAddress(expectedHostNameTwo, expectedPortNum));
        hdfsSiteProperties.put("dfs.secondary.http.address", "localhost:8080");
        hdfsSiteProperties.put("dfs.namenode.secondary.http-address", "localhost:8080");
        coreSiteProperties.put("fs.defaultFS", "hdfs://" + expectedNameService);
        hbaseSiteProperties.put("hbase.rootdir", ("hdfs://" + expectedNameService) + "/hbase/test/root/dir");
        accumuloSiteProperties.put("instance.volumes", ("hdfs://" + expectedNameService) + "/accumulo/test/instance/volumes");
        org.apache.ambari.server.topology.Configuration clusterConfig = new org.apache.ambari.server.topology.Configuration(configProperties, java.util.Collections.emptyMap());
        java.util.Collection<java.lang.String> hgComponents = new java.util.HashSet<>();
        hgComponents.add("NAMENODE");
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup group1 = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup(expectedHostGroupName, hgComponents, java.util.Collections.singleton(expectedHostName));
        java.util.Collection<java.lang.String> hgComponents2 = new java.util.HashSet<>();
        hgComponents2.add("NAMENODE");
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup group2 = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup("host-group-2", hgComponents2, java.util.Collections.singleton(expectedHostNameTwo));
        java.util.Collection<org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup> hostGroups = new java.util.HashSet<>();
        hostGroups.add(group1);
        hostGroups.add(group2);
        EasyMock.expect(stack.getCardinality("NAMENODE")).andReturn(new org.apache.ambari.server.topology.Cardinality("1-2")).anyTimes();
        EasyMock.expect(stack.getCardinality("SECONDARY_NAMENODE")).andReturn(new org.apache.ambari.server.topology.Cardinality("1")).anyTimes();
        org.apache.ambari.server.topology.ClusterTopology topology = createClusterTopology(bp, clusterConfig, hostGroups);
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor updater = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor(topology);
        updater.doUpdateForClusterCreate();
        org.junit.Assert.assertEquals("dfs.internal.nameservices wasn't added", expectedNameService, hdfsSiteProperties.get("dfs.internal.nameservices"));
        org.junit.Assert.assertEquals("HTTPS address HA property not properly exported", (expectedHostName + ":") + expectedPortNum, hdfsSiteProperties.get((("dfs.namenode.https-address." + expectedNameService) + ".") + expectedNodeOne));
        org.junit.Assert.assertEquals("HTTPS address HA property not properly exported", (expectedHostNameTwo + ":") + expectedPortNum, hdfsSiteProperties.get((("dfs.namenode.https-address." + expectedNameService) + ".") + expectedNodeTwo));
        org.junit.Assert.assertEquals("HTTPS address HA property not properly exported", (expectedHostName + ":") + expectedPortNum, hdfsSiteProperties.get((("dfs.namenode.http-address." + expectedNameService) + ".") + expectedNodeOne));
        org.junit.Assert.assertEquals("HTTPS address HA property not properly exported", (expectedHostNameTwo + ":") + expectedPortNum, hdfsSiteProperties.get((("dfs.namenode.http-address." + expectedNameService) + ".") + expectedNodeTwo));
        org.junit.Assert.assertEquals("HTTPS address HA property not properly exported", (expectedHostName + ":") + expectedPortNum, hdfsSiteProperties.get((("dfs.namenode.rpc-address." + expectedNameService) + ".") + expectedNodeOne));
        org.junit.Assert.assertEquals("HTTPS address HA property not properly exported", (expectedHostNameTwo + ":") + expectedPortNum, hdfsSiteProperties.get((("dfs.namenode.rpc-address." + expectedNameService) + ".") + expectedNodeTwo));
        java.util.Map<java.lang.String, java.lang.String> clusterEnv = clusterConfig.getProperties().get("cluster-env");
        java.lang.String activeHost = clusterEnv.get("dfs_ha_initial_namenode_active");
        if (activeHost.equals(expectedHostName)) {
            org.junit.Assert.assertEquals("Standby Namenode hostname was not set correctly", expectedHostNameTwo, clusterEnv.get("dfs_ha_initial_namenode_standby"));
        } else if (activeHost.equals(expectedHostNameTwo)) {
            org.junit.Assert.assertEquals("Standby Namenode hostname was not set correctly", expectedHostName, clusterEnv.get("dfs_ha_initial_namenode_standby"));
        } else {
            org.junit.Assert.fail("Active Namenode hostname was not set correctly: " + activeHost);
        }
        org.junit.Assert.assertEquals("fs.defaultFS should not be modified by cluster update when NameNode HA is enabled.", "hdfs://" + expectedNameService, coreSiteProperties.get("fs.defaultFS"));
        org.junit.Assert.assertEquals("hbase.rootdir should not be modified by cluster update when NameNode HA is enabled.", ("hdfs://" + expectedNameService) + "/hbase/test/root/dir", hbaseSiteProperties.get("hbase.rootdir"));
        org.junit.Assert.assertEquals("instance.volumes should not be modified by cluster update when NameNode HA is enabled.", ("hdfs://" + expectedNameService) + "/accumulo/test/instance/volumes", accumuloSiteProperties.get("instance.volumes"));
    }

    @org.junit.Test
    public void testHiveConfigClusterUpdateCustomValueSpecifyingHostNamesMetaStoreHA() throws java.lang.Exception {
        final java.lang.String expectedHostGroupName = "host_group_1";
        final java.lang.String expectedPropertyValue = "hive.metastore.local=false,hive.metastore.uris=thrift://headnode0.ivantestcluster2-ssh.d1.internal.cloudapp.net:9083,hive.user.install.directory=/user";
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> configProperties = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.lang.String> webHCatSiteProperties = new java.util.HashMap<>();
        configProperties.put("webhcat-site", webHCatSiteProperties);
        webHCatSiteProperties.put("templeton.hive.properties", expectedPropertyValue);
        org.apache.ambari.server.topology.Configuration clusterConfig = new org.apache.ambari.server.topology.Configuration(configProperties, java.util.Collections.emptyMap());
        java.util.Collection<java.lang.String> hgComponents = new java.util.HashSet<>();
        hgComponents.add("NAMENODE");
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup group1 = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup(expectedHostGroupName, hgComponents, java.util.Collections.singleton("some-host"));
        java.util.Collection<java.lang.String> hgComponents2 = new java.util.HashSet<>();
        hgComponents2.add("DATANODE");
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup group2 = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup("host_group_2", hgComponents2, java.util.Collections.singleton("some-host2"));
        java.util.Collection<org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup> hostGroups = new java.util.HashSet<>();
        hostGroups.add(group1);
        hostGroups.add(group2);
        org.apache.ambari.server.topology.ClusterTopology topology = createClusterTopology(bp, clusterConfig, hostGroups);
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor updater = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor(topology);
        updater.doUpdateForClusterCreate();
        org.junit.Assert.assertEquals("Unexpected config update for templeton.hive.properties", expectedPropertyValue, webHCatSiteProperties.get("templeton.hive.properties"));
    }

    @org.junit.Test
    public void testHiveConfigClusterUpdateSpecifyingHostNamesHiveServer2HA() throws java.lang.Exception {
        final java.lang.String expectedHostGroupName = "host_group_1";
        final java.lang.String expectedMetaStoreURIs = "thrift://c6401.ambari.apache.org:9083,thrift://c6402.ambari.apache.org:9083";
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> configProperties = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.lang.String> hiveEnvProperties = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.lang.String> hiveSiteProperties = new java.util.HashMap<>();
        configProperties.put("hive-env", hiveEnvProperties);
        configProperties.put("hive-site", hiveSiteProperties);
        hiveSiteProperties.put("hive.server2.support.dynamic.service.discovery", "true");
        hiveSiteProperties.put("hive.metastore.uris", expectedMetaStoreURIs);
        org.apache.ambari.server.topology.Configuration clusterConfig = new org.apache.ambari.server.topology.Configuration(configProperties, java.util.Collections.emptyMap());
        java.util.Collection<java.lang.String> hgComponents = new java.util.HashSet<>();
        hgComponents.add("HIVE_SERVER");
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup group1 = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup(expectedHostGroupName, hgComponents, java.util.Collections.singleton("some-host"));
        java.util.Collection<java.lang.String> hgComponents2 = new java.util.HashSet<>();
        hgComponents2.add("HIVE_SERVER");
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup group2 = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup("host_group_2", hgComponents2, java.util.Collections.singleton("some-host2"));
        java.util.Collection<org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup> hostGroups = new java.util.HashSet<>();
        hostGroups.add(group1);
        hostGroups.add(group2);
        EasyMock.expect(stack.getCardinality("HIVE_SERVER")).andReturn(new org.apache.ambari.server.topology.Cardinality("1+")).anyTimes();
        org.apache.ambari.server.topology.ClusterTopology topology = createClusterTopology(bp, clusterConfig, hostGroups);
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor updater = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor(topology);
        updater.doUpdateForClusterCreate();
        org.junit.Assert.assertEquals("Unexpected config update for hive.metastore.uris", expectedMetaStoreURIs, hiveSiteProperties.get("hive.metastore.uris"));
    }

    @org.junit.Test
    public void testHiveConfigClusterUpdateUsingExportedNamesHiveServer2HA() throws java.lang.Exception {
        final java.lang.String expectedHostGroupNameOne = "host_group_1";
        final java.lang.String expectedHostGroupNameTwo = "host_group_2";
        final java.lang.String expectedHostNameOne = "c6401.ambari.apache.org";
        final java.lang.String expectedHostNameTwo = "c6402.ambari.apache.org";
        final java.lang.String inputMetaStoreURIs = ((("thrift://" + org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.createExportedAddress("9083", expectedHostGroupNameOne)) + ",") + "thrift://") + org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.createExportedAddress("9083", expectedHostGroupNameTwo);
        final java.lang.String expectedMetaStoreURIs = "thrift://c6401.ambari.apache.org:9083,thrift://c6402.ambari.apache.org:9083";
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> configProperties = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.lang.String> hiveEnvProperties = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.lang.String> hiveSiteProperties = new java.util.HashMap<>();
        configProperties.put("hive-env", hiveEnvProperties);
        configProperties.put("hive-site", hiveSiteProperties);
        hiveSiteProperties.put("hive.server2.support.dynamic.service.discovery", "true");
        hiveSiteProperties.put("hive.metastore.uris", inputMetaStoreURIs);
        org.apache.ambari.server.topology.Configuration clusterConfig = new org.apache.ambari.server.topology.Configuration(configProperties, java.util.Collections.emptyMap());
        java.util.Collection<java.lang.String> hgComponents = new java.util.HashSet<>();
        hgComponents.add("HIVE_SERVER");
        hgComponents.add("HIVE_METASTORE");
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup group1 = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup(expectedHostGroupNameOne, hgComponents, java.util.Collections.singleton(expectedHostNameOne));
        java.util.Collection<java.lang.String> hgComponents2 = new java.util.HashSet<>();
        hgComponents2.add("HIVE_SERVER");
        hgComponents2.add("HIVE_METASTORE");
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup group2 = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup(expectedHostGroupNameTwo, hgComponents2, java.util.Collections.singleton(expectedHostNameTwo));
        java.util.Collection<org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup> hostGroups = new java.util.HashSet<>();
        hostGroups.add(group1);
        hostGroups.add(group2);
        EasyMock.expect(stack.getCardinality("HIVE_SERVER")).andReturn(new org.apache.ambari.server.topology.Cardinality("1+")).anyTimes();
        org.apache.ambari.server.topology.ClusterTopology topology = createClusterTopology(bp, clusterConfig, hostGroups);
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor updater = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor(topology);
        updater.doUpdateForClusterCreate();
        org.junit.Assert.assertEquals("Unexpected config update for hive.metastore.uris", expectedMetaStoreURIs, hiveSiteProperties.get("hive.metastore.uris"));
    }

    @org.junit.Test
    public void testHivePropertiesLocalhostReplacedComma() throws java.lang.Exception {
        testHiveMetastoreHA(",");
    }

    @org.junit.Test
    public void testHivePropertiesLocalhostReplacedCommaSpace() throws java.lang.Exception {
        testHiveMetastoreHA(", ");
    }

    @org.junit.Test
    public void testHivePropertiesLocalhostReplacedSpaceComma() throws java.lang.Exception {
        testHiveMetastoreHA(" ,");
    }

    @org.junit.Test
    public void testHivePropertiesLocalhostReplacedSpaceCommaSpace() throws java.lang.Exception {
        testHiveMetastoreHA(" , ");
    }

    private void testHiveMetastoreHA(java.lang.String separator) throws org.apache.ambari.server.topology.InvalidTopologyException, org.apache.ambari.server.controller.internal.ConfigurationTopologyException {
        final java.lang.String[] parts = new java.lang.String[]{ "hive.metastore.local=false", "hive.metastore.uris=" + org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.getThriftURI("localhost"), "hive.metastore.sasl.enabled=false" };
        final java.lang.String[] hostNames = new java.lang.String[]{ "c6401.ambari.apache.org", "example.com", "c6402.ambari.apache.org" };
        final java.util.Set<java.lang.String> expectedUris = new java.util.HashSet<>();
        for (java.lang.String hostName : hostNames) {
            expectedUris.add(org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.getThriftURI(hostName));
        }
        final java.lang.String initialPropertyValue = org.apache.commons.lang.StringUtils.join(parts, separator);
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> configProperties = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.lang.String> webHCatSiteProperties = new java.util.HashMap<>();
        configProperties.put("webhcat-site", webHCatSiteProperties);
        java.lang.String propertyKey = "templeton.hive.properties";
        webHCatSiteProperties.put(propertyKey, initialPropertyValue);
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>>> attributes = java.util.Collections.emptyMap();
        org.apache.ambari.server.topology.Configuration clusterConfig = new org.apache.ambari.server.topology.Configuration(configProperties, attributes);
        java.util.Collection<org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup> hostGroups = new java.util.HashSet<>();
        for (int i = 0; i < hostNames.length; ++i) {
            java.util.Collection<java.lang.String> components = new java.util.HashSet<>(java.util.Collections.singleton("HIVE_METASTORE"));
            hostGroups.add(new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup("host_group_" + i, components, java.util.Collections.singleton(hostNames[i])));
        }
        org.apache.ambari.server.topology.ClusterTopology topology = createClusterTopology(bp, clusterConfig, hostGroups);
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor updater = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor(topology);
        updater.doUpdateForClusterCreate();
        java.lang.String updatedValue = webHCatSiteProperties.get(propertyKey);
        java.lang.String prefix = parts[0] + ",";
        org.junit.Assert.assertTrue(updatedValue, updatedValue.startsWith(prefix));
        java.lang.String suffix = "," + parts[2];
        org.junit.Assert.assertTrue(updatedValue, updatedValue.endsWith(suffix));
        java.lang.String part1 = updatedValue.replace(prefix, "").replace(suffix, "");
        java.lang.String key = "hive.metastore.uris=";
        org.junit.Assert.assertTrue(part1, part1.startsWith(key));
        java.util.Set<java.lang.String> updatedUris = new java.util.HashSet<>(java.util.Arrays.asList(part1.replace(key, "").split("\\\\,")));
        org.junit.Assert.assertEquals(expectedUris, updatedUris);
    }

    private static java.lang.String getThriftURI(java.lang.String hostName) {
        return ("thrift://" + hostName) + ":9933";
    }

    @org.junit.Test
    public void testHiveInteractiveLlapZookeeperConfigExported() throws java.lang.Exception {
        final java.lang.String expectedHostName = "c6401.apache.ambari.org";
        final java.lang.String expectedHostNameTwo = "c6402.ambari.apache.org";
        final java.lang.String expectedHostGroupName = "host_group_1";
        final java.lang.String expectedHostGroupNameTwo = "host_group_2";
        final java.lang.String llapZkProperty = "hive.llap.zk.sm.connectionString";
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> configProperties = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.lang.String> hiveInteractiveSiteProperties = new java.util.HashMap<>();
        configProperties.put("hive-interactive-site", hiveInteractiveSiteProperties);
        hiveInteractiveSiteProperties.put(llapZkProperty, (org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.createHostAddress(expectedHostName, "2181") + ",") + org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.createHostAddress(expectedHostNameTwo, "2181"));
        org.apache.ambari.server.topology.Configuration clusterConfig = new org.apache.ambari.server.topology.Configuration(configProperties, java.util.Collections.emptyMap());
        java.util.Collection<java.lang.String> hgComponents = new java.util.HashSet<>();
        hgComponents.add("ZOOKEEPER_SERVER");
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup group1 = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup(expectedHostGroupName, hgComponents, java.util.Collections.singleton(expectedHostName));
        java.util.Collection<java.lang.String> hgComponents2 = new java.util.HashSet<>();
        hgComponents2.add("ZOOKEEPER_SERVER");
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup group2 = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup(expectedHostGroupNameTwo, hgComponents2, java.util.Collections.singleton(expectedHostNameTwo));
        java.util.Collection<org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup> hostGroups = new java.util.HashSet<>();
        hostGroups.add(group1);
        hostGroups.add(group2);
        org.apache.ambari.server.topology.ClusterTopology topology = createClusterTopology(bp, clusterConfig, hostGroups);
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor updater = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor(topology);
        updater.doUpdateForBlueprintExport(org.apache.ambari.server.controller.internal.BlueprintExportType.FULL);
        final java.lang.String expectedPropertyValue = (org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.createExportedAddress("2181", expectedHostGroupName) + ",") + org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.createExportedAddress("2181", expectedHostGroupNameTwo);
        org.junit.Assert.assertEquals("hive.llap.zk.sm.connectionString property not updated correctly", expectedPropertyValue, hiveInteractiveSiteProperties.get(llapZkProperty));
    }

    @org.junit.Test
    public void testOozieConfigClusterUpdateHAEnabledSpecifyingHostNamesDirectly() throws java.lang.Exception {
        final java.lang.String expectedHostName = "c6401.apache.ambari.org";
        final java.lang.String expectedHostNameTwo = "c6402.ambari.apache.org";
        final java.lang.String expectedExternalHost = "c6408.ambari.apache.org";
        final java.lang.String expectedHostGroupName = "host_group_1";
        final java.lang.String expectedHostGroupNameTwo = "host_group_2";
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> configProperties = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.lang.String> oozieSiteProperties = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.lang.String> oozieEnvProperties = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.lang.String> coreSiteProperties = new java.util.HashMap<>();
        configProperties.put("oozie-site", oozieSiteProperties);
        configProperties.put("oozie-env", oozieEnvProperties);
        configProperties.put("hive-env", oozieEnvProperties);
        configProperties.put("core-site", coreSiteProperties);
        oozieSiteProperties.put("oozie.base.url", expectedHostName);
        oozieSiteProperties.put("oozie.authentication.kerberos.principal", expectedHostName);
        oozieSiteProperties.put("oozie.service.HadoopAccessorService.kerberos.principal", expectedHostName);
        oozieSiteProperties.put("oozie.service.JPAService.jdbc.url", ("jdbc:mysql://" + expectedExternalHost) + "/ooziedb");
        oozieSiteProperties.put("oozie.services.ext", "org.apache.oozie.service.ZKLocksService,org.apache.oozie.service.ZKXLogStreamingService,org.apache.oozie.service.ZKJobsConcurrencyService,org.apache.oozie.service.ZKUUIDService");
        oozieEnvProperties.put("oozie_existing_mysql_host", expectedExternalHost);
        coreSiteProperties.put("hadoop.proxyuser.oozie.hosts", (expectedHostName + ",") + expectedHostNameTwo);
        org.apache.ambari.server.topology.Configuration clusterConfig = new org.apache.ambari.server.topology.Configuration(configProperties, java.util.Collections.emptyMap());
        java.util.Collection<java.lang.String> hgComponents = new java.util.HashSet<>();
        hgComponents.add("OOZIE_SERVER");
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup group1 = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup(expectedHostGroupName, hgComponents, java.util.Collections.singleton("host1"));
        java.util.Collection<java.lang.String> hgComponents2 = new java.util.HashSet<>();
        hgComponents2.add("OOZIE_SERVER");
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup group2 = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup(expectedHostGroupNameTwo, hgComponents2, java.util.Collections.singleton("host2"));
        java.util.Collection<org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup> hostGroups = new java.util.HashSet<>();
        hostGroups.add(group1);
        hostGroups.add(group2);
        EasyMock.expect(stack.getCardinality("OOZIE_SERVER")).andReturn(new org.apache.ambari.server.topology.Cardinality("1+")).anyTimes();
        org.apache.ambari.server.topology.ClusterTopology topology = createClusterTopology(bp, clusterConfig, hostGroups);
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor updater = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor(topology);
        updater.doUpdateForClusterCreate();
        org.junit.Assert.assertEquals("oozie property not updated correctly", expectedHostName, oozieSiteProperties.get("oozie.base.url"));
        org.junit.Assert.assertEquals("oozie property not updated correctly", expectedHostName, oozieSiteProperties.get("oozie.authentication.kerberos.principal"));
        org.junit.Assert.assertEquals("oozie property not updated correctly", expectedHostName, oozieSiteProperties.get("oozie.service.HadoopAccessorService.kerberos.principal"));
        org.junit.Assert.assertEquals("oozie property not updated correctly", (expectedHostName + ",") + expectedHostNameTwo, coreSiteProperties.get("hadoop.proxyuser.oozie.hosts"));
    }

    @org.junit.Test
    public void testOozieHAEnabledExport() throws java.lang.Exception {
        final java.lang.String expectedHostName = "c6401.apache.ambari.org";
        final java.lang.String expectedHostNameTwo = "c6402.ambari.apache.org";
        final java.lang.String expectedExternalHost = "c6408.ambari.apache.org";
        final java.lang.String expectedHostGroupName = "host_group_1";
        final java.lang.String expectedHostGroupNameTwo = "host_group_2";
        final java.lang.String expectedPortNum = "80000";
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> configProperties = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.lang.String> oozieSiteProperties = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.lang.String> oozieEnvProperties = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.lang.String> coreSiteProperties = new java.util.HashMap<>();
        configProperties.put("oozie-site", oozieSiteProperties);
        configProperties.put("oozie-env", oozieEnvProperties);
        configProperties.put("hive-env", oozieEnvProperties);
        configProperties.put("core-site", coreSiteProperties);
        oozieSiteProperties.put("oozie.base.url", (expectedHostName + ":") + expectedPortNum);
        oozieSiteProperties.put("oozie.authentication.kerberos.principal", expectedHostName);
        oozieSiteProperties.put("oozie.service.HadoopAccessorService.kerberos.principal", expectedHostName);
        oozieSiteProperties.put("oozie.service.JPAService.jdbc.url", ("jdbc:mysql://" + expectedExternalHost) + "/ooziedb");
        oozieSiteProperties.put("oozie.services.ext", "org.apache.oozie.service.ZKLocksService,org.apache.oozie.service.ZKXLogStreamingService,org.apache.oozie.service.ZKJobsConcurrencyService,org.apache.oozie.service.ZKUUIDService");
        oozieSiteProperties.put("oozie.zookeeper.connection.string", (org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.createHostAddress(expectedHostName, "2181") + ",") + org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.createHostAddress(expectedHostNameTwo, "2181"));
        oozieEnvProperties.put("oozie_existing_mysql_host", expectedExternalHost);
        coreSiteProperties.put("hadoop.proxyuser.oozie.hosts", (expectedHostName + ",") + expectedHostNameTwo);
        org.apache.ambari.server.topology.Configuration clusterConfig = new org.apache.ambari.server.topology.Configuration(configProperties, java.util.Collections.emptyMap());
        java.util.Collection<java.lang.String> hgComponents = new java.util.HashSet<>();
        hgComponents.add("OOZIE_SERVER");
        hgComponents.add("ZOOKEEPER_SERVER");
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup group1 = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup(expectedHostGroupName, hgComponents, java.util.Collections.singleton(expectedHostName));
        java.util.Collection<java.lang.String> hgComponents2 = new java.util.HashSet<>();
        hgComponents2.add("OOZIE_SERVER");
        hgComponents2.add("ZOOKEEPER_SERVER");
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup group2 = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup(expectedHostGroupNameTwo, hgComponents2, java.util.Collections.singleton(expectedHostNameTwo));
        java.util.Collection<org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup> hostGroups = new java.util.HashSet<>();
        hostGroups.add(group1);
        hostGroups.add(group2);
        EasyMock.expect(stack.getCardinality("OOZIE_SERVER")).andReturn(new org.apache.ambari.server.topology.Cardinality("1+")).anyTimes();
        org.apache.ambari.server.topology.ClusterTopology topology = createClusterTopology(bp, clusterConfig, hostGroups);
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor updater = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor(topology);
        updater.doUpdateForBlueprintExport(org.apache.ambari.server.controller.internal.BlueprintExportType.FULL);
        org.junit.Assert.assertEquals("oozie property not updated correctly", org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.createExportedHostName(expectedHostGroupName, expectedPortNum), oozieSiteProperties.get("oozie.base.url"));
        org.junit.Assert.assertEquals("oozie property not updated correctly", (org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.createExportedHostName(expectedHostGroupName) + ",") + org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.createExportedHostName(expectedHostGroupNameTwo), coreSiteProperties.get("hadoop.proxyuser.oozie.hosts"));
        org.junit.Assert.assertEquals("oozie property not updated correctly", (org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.createExportedAddress("2181", expectedHostGroupName) + ",") + org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.createExportedAddress("2181", expectedHostGroupNameTwo), oozieSiteProperties.get("oozie.zookeeper.connection.string"));
    }

    @org.junit.Test
    public void testYarnHighAvailabilityConfigClusterUpdateSpecifyingHostNamesDirectly() throws java.lang.Exception {
        final java.lang.String expectedHostName = "c6401.apache.ambari.org";
        final java.lang.String expectedPortNum = "808080";
        final java.lang.String expectedHostGroupName = "host_group_1";
        final java.lang.String expectedHostGroupNameTwo = "host_group_2";
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> configProperties = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.lang.String> yarnSiteProperties = new java.util.HashMap<>();
        configProperties.put("yarn-site", yarnSiteProperties);
        yarnSiteProperties.put("yarn.log.server.url", ("http://" + expectedHostName) + ":19888/jobhistory/logs");
        yarnSiteProperties.put("yarn.resourcemanager.hostname", expectedHostName);
        yarnSiteProperties.put("yarn.resourcemanager.resource-tracker.address", (expectedHostName + ":") + expectedPortNum);
        yarnSiteProperties.put("yarn.resourcemanager.webapp.address", (expectedHostName + ":") + expectedPortNum);
        yarnSiteProperties.put("yarn.resourcemanager.scheduler.address", (expectedHostName + ":") + expectedPortNum);
        yarnSiteProperties.put("yarn.resourcemanager.address", (expectedHostName + ":") + expectedPortNum);
        yarnSiteProperties.put("yarn.resourcemanager.admin.address", (expectedHostName + ":") + expectedPortNum);
        yarnSiteProperties.put("yarn.timeline-service.address", (expectedHostName + ":") + expectedPortNum);
        yarnSiteProperties.put("yarn.timeline-service.webapp.address", (expectedHostName + ":") + expectedPortNum);
        yarnSiteProperties.put("yarn.timeline-service.webapp.https.address", (expectedHostName + ":") + expectedPortNum);
        yarnSiteProperties.put("yarn.timeline-service.reader.webapp.address", (expectedHostName + ":") + expectedPortNum);
        yarnSiteProperties.put("yarn.timeline-service.reader.webapp.https.address", (expectedHostName + ":") + expectedPortNum);
        yarnSiteProperties.put("yarn.resourcemanager.ha.enabled", "true");
        yarnSiteProperties.put("yarn.resourcemanager.ha.rm-ids", "rm1, rm2");
        org.apache.ambari.server.topology.Configuration clusterConfig = new org.apache.ambari.server.topology.Configuration(configProperties, java.util.Collections.emptyMap());
        java.util.Collection<java.lang.String> hgComponents = new java.util.HashSet<>();
        hgComponents.add("RESOURCEMANAGER");
        hgComponents.add("APP_TIMELINE_SERVER");
        hgComponents.add("TIMELINE_READER");
        hgComponents.add("HISTORYSERVER");
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup group1 = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup(expectedHostGroupName, hgComponents, java.util.Collections.singleton(expectedHostName));
        java.util.Collection<java.lang.String> hgComponents2 = new java.util.HashSet<>();
        hgComponents2.add("RESOURCEMANAGER");
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup group2 = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup(expectedHostGroupNameTwo, hgComponents2, java.util.Collections.singleton("host2"));
        java.util.Collection<org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup> hostGroups = new java.util.HashSet<>();
        hostGroups.add(group1);
        hostGroups.add(group2);
        EasyMock.expect(stack.getCardinality("RESOURCEMANAGER")).andReturn(new org.apache.ambari.server.topology.Cardinality("1-2")).anyTimes();
        org.apache.ambari.server.topology.ClusterTopology topology = createClusterTopology(bp, clusterConfig, hostGroups);
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor updater = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor(topology);
        updater.doUpdateForClusterCreate();
        org.junit.Assert.assertEquals("Yarn Log Server URL was incorrectly updated", ("http://" + expectedHostName) + ":19888/jobhistory/logs", yarnSiteProperties.get("yarn.log.server.url"));
        org.junit.Assert.assertEquals("Yarn ResourceManager hostname was incorrectly exported", expectedHostName, yarnSiteProperties.get("yarn.resourcemanager.hostname"));
        org.junit.Assert.assertEquals("Yarn ResourceManager tracker address was incorrectly updated", org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.createHostAddress(expectedHostName, expectedPortNum), yarnSiteProperties.get("yarn.resourcemanager.resource-tracker.address"));
        org.junit.Assert.assertEquals("Yarn ResourceManager webapp address was incorrectly updated", org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.createHostAddress(expectedHostName, expectedPortNum), yarnSiteProperties.get("yarn.resourcemanager.webapp.address"));
        org.junit.Assert.assertEquals("Yarn ResourceManager scheduler address was incorrectly updated", org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.createHostAddress(expectedHostName, expectedPortNum), yarnSiteProperties.get("yarn.resourcemanager.scheduler.address"));
        org.junit.Assert.assertEquals("Yarn ResourceManager address was incorrectly updated", org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.createHostAddress(expectedHostName, expectedPortNum), yarnSiteProperties.get("yarn.resourcemanager.address"));
        org.junit.Assert.assertEquals("Yarn ResourceManager admin address was incorrectly updated", org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.createHostAddress(expectedHostName, expectedPortNum), yarnSiteProperties.get("yarn.resourcemanager.admin.address"));
        org.junit.Assert.assertEquals("Yarn ResourceManager timeline-service address was incorrectly updated", org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.createHostAddress(expectedHostName, expectedPortNum), yarnSiteProperties.get("yarn.timeline-service.address"));
        org.junit.Assert.assertEquals("Yarn ResourceManager timeline webapp address was incorrectly updated", org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.createHostAddress(expectedHostName, expectedPortNum), yarnSiteProperties.get("yarn.timeline-service.webapp.address"));
        org.junit.Assert.assertEquals("Yarn ResourceManager timeline webapp HTTPS address was incorrectly updated", org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.createHostAddress(expectedHostName, expectedPortNum), yarnSiteProperties.get("yarn.timeline-service.webapp.https.address"));
        org.junit.Assert.assertEquals("Yarn ResourceManager timeline reader webapp address was incorrectly updated", org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.createHostAddress(expectedHostName, expectedPortNum), yarnSiteProperties.get("yarn.timeline-service.reader.webapp.address"));
        org.junit.Assert.assertEquals("Yarn ResourceManager timeline reader webapp HTTPS address was incorrectly updated", org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.createHostAddress(expectedHostName, expectedPortNum), yarnSiteProperties.get("yarn.timeline-service.reader.webapp.https.address"));
    }

    @org.junit.Test
    public void testYarnHighAvailabilityExport() throws java.lang.Exception {
        final java.lang.String expectedHostName = "c6401.apache.ambari.org";
        final java.lang.String expectedHostNameTwo = "c6402.apache.ambari.org";
        final java.lang.String expectedPortNum = "808080";
        final java.lang.String expectedHostGroupName = "host_group_1";
        final java.lang.String expectedHostGroupNameTwo = "host_group_2";
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> configProperties = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.lang.String> yarnSiteProperties = new java.util.HashMap<>();
        configProperties.put("yarn-site", yarnSiteProperties);
        yarnSiteProperties.put("yarn.log.server.url", ("http://" + expectedHostName) + ":19888/jobhistory/logs");
        yarnSiteProperties.put("yarn.resourcemanager.hostname", expectedHostName);
        yarnSiteProperties.put("yarn.resourcemanager.resource-tracker.address", (expectedHostName + ":") + expectedPortNum);
        yarnSiteProperties.put("yarn.resourcemanager.webapp.address", (expectedHostName + ":") + expectedPortNum);
        yarnSiteProperties.put("yarn.resourcemanager.scheduler.address", (expectedHostName + ":") + expectedPortNum);
        yarnSiteProperties.put("yarn.resourcemanager.address", (expectedHostName + ":") + expectedPortNum);
        yarnSiteProperties.put("yarn.resourcemanager.admin.address", (expectedHostName + ":") + expectedPortNum);
        yarnSiteProperties.put("yarn.timeline-service.address", (expectedHostName + ":") + expectedPortNum);
        yarnSiteProperties.put("yarn.timeline-service.webapp.address", (expectedHostName + ":") + expectedPortNum);
        yarnSiteProperties.put("yarn.timeline-service.webapp.https.address", (expectedHostName + ":") + expectedPortNum);
        yarnSiteProperties.put("yarn.timeline-service.reader.webapp.address", (expectedHostName + ":") + expectedPortNum);
        yarnSiteProperties.put("yarn.timeline-service.reader.webapp.https.address", (expectedHostName + ":") + expectedPortNum);
        yarnSiteProperties.put("yarn.resourcemanager.ha.enabled", "true");
        yarnSiteProperties.put("yarn.resourcemanager.ha.rm-ids", "rm1, rm2");
        yarnSiteProperties.put("yarn.resourcemanager.hostname.rm1", expectedHostName);
        yarnSiteProperties.put("yarn.resourcemanager.hostname.rm2", expectedHostNameTwo);
        yarnSiteProperties.put("yarn.resourcemanager.address.rm1", (expectedHostName + ":") + expectedPortNum);
        yarnSiteProperties.put("yarn.resourcemanager.address.rm2", (expectedHostNameTwo + ":") + expectedPortNum);
        yarnSiteProperties.put("yarn.resourcemanager.admin.address.rm1", (expectedHostName + ":") + expectedPortNum);
        yarnSiteProperties.put("yarn.resourcemanager.admin.address.rm2", (expectedHostNameTwo + ":") + expectedPortNum);
        yarnSiteProperties.put("yarn.resourcemanager.resource-tracker.address.rm1", (expectedHostName + ":") + expectedPortNum);
        yarnSiteProperties.put("yarn.resourcemanager.resource-tracker.address.rm2", (expectedHostNameTwo + ":") + expectedPortNum);
        yarnSiteProperties.put("yarn.resourcemanager.scheduler.address.rm1", (expectedHostName + ":") + expectedPortNum);
        yarnSiteProperties.put("yarn.resourcemanager.scheduler.address.rm2", (expectedHostNameTwo + ":") + expectedPortNum);
        yarnSiteProperties.put("yarn.resourcemanager.webapp.address.rm1", (expectedHostName + ":") + expectedPortNum);
        yarnSiteProperties.put("yarn.resourcemanager.webapp.address.rm2", (expectedHostNameTwo + ":") + expectedPortNum);
        yarnSiteProperties.put("yarn.resourcemanager.webapp.https.address.rm1", (expectedHostName + ":") + expectedPortNum);
        yarnSiteProperties.put("yarn.resourcemanager.webapp.https.address.rm2", (expectedHostNameTwo + ":") + expectedPortNum);
        yarnSiteProperties.put("yarn.resourcemanager.zk-address", (((((expectedHostName + ":") + "2181") + ",") + expectedHostNameTwo) + ":") + "2181");
        yarnSiteProperties.put("yarn.resourcemanager.webapp.https.address", (expectedHostName + ":") + "8080");
        org.apache.ambari.server.topology.Configuration clusterConfig = new org.apache.ambari.server.topology.Configuration(configProperties, java.util.Collections.emptyMap());
        java.util.Collection<java.lang.String> hgComponents = new java.util.HashSet<>();
        hgComponents.add("RESOURCEMANAGER");
        hgComponents.add("APP_TIMELINE_SERVER");
        hgComponents.add("TIMELINE_READER");
        hgComponents.add("HISTORYSERVER");
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup group1 = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup(expectedHostGroupName, hgComponents, java.util.Collections.singleton(expectedHostName));
        java.util.Collection<java.lang.String> hgComponents2 = new java.util.HashSet<>();
        hgComponents2.add("RESOURCEMANAGER");
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup group2 = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup(expectedHostGroupNameTwo, hgComponents2, java.util.Collections.singleton(expectedHostNameTwo));
        java.util.Collection<org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup> hostGroups = new java.util.HashSet<>();
        hostGroups.add(group1);
        hostGroups.add(group2);
        EasyMock.expect(stack.getCardinality("RESOURCEMANAGER")).andReturn(new org.apache.ambari.server.topology.Cardinality("1-2")).anyTimes();
        org.apache.ambari.server.topology.ClusterTopology topology = createClusterTopology(bp, clusterConfig, hostGroups);
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor updater = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor(topology);
        updater.doUpdateForBlueprintExport(org.apache.ambari.server.controller.internal.BlueprintExportType.FULL);
        org.junit.Assert.assertEquals("Yarn Log Server URL was incorrectly updated", ("http://" + org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.createExportedAddress("19888", expectedHostGroupName)) + "/jobhistory/logs", yarnSiteProperties.get("yarn.log.server.url"));
        org.junit.Assert.assertEquals("Yarn ResourceManager hostname was incorrectly updated", org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.createExportedHostName(expectedHostGroupName), yarnSiteProperties.get("yarn.resourcemanager.hostname"));
        org.junit.Assert.assertEquals("Yarn ResourceManager tracker address was incorrectly updated", org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.createExportedHostName(expectedHostGroupName, expectedPortNum), yarnSiteProperties.get("yarn.resourcemanager.resource-tracker.address"));
        org.junit.Assert.assertEquals("Yarn ResourceManager webapp address was incorrectly updated", org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.createExportedHostName(expectedHostGroupName, expectedPortNum), yarnSiteProperties.get("yarn.resourcemanager.webapp.address"));
        org.junit.Assert.assertEquals("Yarn ResourceManager scheduler address was incorrectly updated", org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.createExportedHostName(expectedHostGroupName, expectedPortNum), yarnSiteProperties.get("yarn.resourcemanager.scheduler.address"));
        org.junit.Assert.assertEquals("Yarn ResourceManager address was incorrectly updated", org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.createExportedHostName(expectedHostGroupName, expectedPortNum), yarnSiteProperties.get("yarn.resourcemanager.address"));
        org.junit.Assert.assertEquals("Yarn ResourceManager admin address was incorrectly updated", org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.createExportedHostName(expectedHostGroupName, expectedPortNum), yarnSiteProperties.get("yarn.resourcemanager.admin.address"));
        org.junit.Assert.assertEquals("Yarn ResourceManager timeline-service address was incorrectly updated", org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.createExportedHostName(expectedHostGroupName, expectedPortNum), yarnSiteProperties.get("yarn.timeline-service.address"));
        org.junit.Assert.assertEquals("Yarn ResourceManager timeline webapp address was incorrectly updated", org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.createExportedHostName(expectedHostGroupName, expectedPortNum), yarnSiteProperties.get("yarn.timeline-service.webapp.address"));
        org.junit.Assert.assertEquals("Yarn ResourceManager timeline webapp HTTPS address was incorrectly updated", org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.createExportedHostName(expectedHostGroupName, expectedPortNum), yarnSiteProperties.get("yarn.timeline-service.webapp.https.address"));
        org.junit.Assert.assertEquals("Yarn ResourceManager timeline reader webapp address was incorrectly updated", org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.createExportedHostName(expectedHostGroupName, expectedPortNum), yarnSiteProperties.get("yarn.timeline-service.reader.webapp.address"));
        org.junit.Assert.assertEquals("Yarn ResourceManager timeline reader ebapp HTTPS address was incorrectly updated", org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.createExportedHostName(expectedHostGroupName, expectedPortNum), yarnSiteProperties.get("yarn.timeline-service.reader.webapp.https.address"));
        java.util.List<java.lang.String> properties = java.util.Arrays.asList("yarn.resourcemanager.address", "yarn.resourcemanager.admin.address", "yarn.resourcemanager.resource-tracker.address", "yarn.resourcemanager.scheduler.address", "yarn.resourcemanager.webapp.address", "yarn.resourcemanager.webapp.https.address");
        for (java.lang.String property : properties) {
            java.lang.String propertyWithID = property + ".rm1";
            org.junit.Assert.assertEquals(propertyWithID, org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.createExportedHostName(expectedHostGroupName, expectedPortNum), yarnSiteProperties.get(propertyWithID));
            propertyWithID = property + ".rm2";
            org.junit.Assert.assertEquals(propertyWithID, org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.createExportedHostName(expectedHostGroupNameTwo, expectedPortNum), yarnSiteProperties.get(propertyWithID));
        }
        org.junit.Assert.assertEquals("Yarn Zookeeper address property not exported properly", (org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.createExportedHostName(expectedHostGroupName, "2181") + ",") + org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.createExportedHostName(expectedHostGroupNameTwo, "2181"), yarnSiteProperties.get("yarn.resourcemanager.zk-address"));
        org.junit.Assert.assertEquals("Yarn RM webapp address not exported properly", org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.createExportedHostName(expectedHostGroupName, "8080"), yarnSiteProperties.get("yarn.resourcemanager.webapp.https.address"));
    }

    @org.junit.Test
    public void testHDFSConfigClusterUpdateQuorumJournalURLSpecifyingHostNamesDirectly() throws java.lang.Exception {
        final java.lang.String expectedHostNameOne = "c6401.apache.ambari.org";
        final java.lang.String expectedHostNameTwo = "c6402.apache.ambari.org";
        final java.lang.String expectedPortNum = "808080";
        final java.lang.String expectedHostGroupName = "host_group_1";
        final java.lang.String expectedHostGroupNameTwo = "host_group_2";
        final java.lang.String expectedQuorumJournalURL = ((("qjournal://" + org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.createHostAddress(expectedHostNameOne, expectedPortNum)) + ";") + org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.createHostAddress(expectedHostNameTwo, expectedPortNum)) + "/mycluster";
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> configProperties = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.lang.String> hdfsSiteProperties = new java.util.HashMap<>();
        configProperties.put("hdfs-site", hdfsSiteProperties);
        hdfsSiteProperties.put("dfs.namenode.shared.edits.dir", expectedQuorumJournalURL);
        org.apache.ambari.server.topology.Configuration clusterConfig = new org.apache.ambari.server.topology.Configuration(configProperties, java.util.Collections.emptyMap());
        java.util.Collection<java.lang.String> hgComponents = new java.util.HashSet<>();
        hgComponents.add("NAMENODE");
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup group1 = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup(expectedHostGroupName, hgComponents, java.util.Collections.singleton("host1"));
        java.util.Collection<java.lang.String> hgComponents2 = new java.util.HashSet<>();
        hgComponents2.add("DATANODE");
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup group2 = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup(expectedHostGroupNameTwo, hgComponents2, java.util.Collections.singleton("host2"));
        java.util.Collection<org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup> hostGroups = new java.util.HashSet<>();
        hostGroups.add(group1);
        hostGroups.add(group2);
        org.apache.ambari.server.topology.ClusterTopology topology = createClusterTopology(bp, clusterConfig, hostGroups);
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor updater = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor(topology);
        updater.doUpdateForClusterCreate();
        org.junit.Assert.assertEquals("HDFS HA shared edits directory property should not have been modified, since FQDNs were specified.", expectedQuorumJournalURL, hdfsSiteProperties.get("dfs.namenode.shared.edits.dir"));
    }

    @org.junit.Test
    public void testDoUpdateForClusterCreate_MultiHostProperty__defaultValues___YAML() throws java.lang.Exception {
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> properties = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.lang.String> typeProps = new java.util.HashMap<>();
        typeProps.put("storm.zookeeper.servers", "['localhost']");
        properties.put("storm-site", typeProps);
        org.apache.ambari.server.topology.Configuration clusterConfig = new org.apache.ambari.server.topology.Configuration(properties, java.util.Collections.emptyMap());
        java.util.Collection<java.lang.String> hgComponents = new java.util.HashSet<>();
        hgComponents.add("NAMENODE");
        hgComponents.add("SECONDARY_NAMENODE");
        hgComponents.add("ZOOKEEPER_SERVER");
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup group1 = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup("group1", hgComponents, java.util.Collections.singleton("testhost"));
        java.util.Collection<java.lang.String> hgComponents2 = new java.util.HashSet<>();
        hgComponents2.add("DATANODE");
        hgComponents2.add("HDFS_CLIENT");
        hgComponents2.add("ZOOKEEPER_SERVER");
        java.util.Set<java.lang.String> hosts2 = new java.util.HashSet<>();
        hosts2.add("testhost2");
        hosts2.add("testhost2a");
        hosts2.add("testhost2b");
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup group2 = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup("group2", hgComponents2, hosts2);
        java.util.Collection<java.lang.String> hgComponents3 = new java.util.HashSet<>();
        hgComponents2.add("HDFS_CLIENT");
        hgComponents2.add("ZOOKEEPER_CLIENT");
        java.util.Set<java.lang.String> hosts3 = new java.util.HashSet<>();
        hosts3.add("testhost3");
        hosts3.add("testhost3a");
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup group3 = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup("group3", hgComponents3, hosts3);
        java.util.Collection<org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup> hostGroups = new java.util.HashSet<>();
        hostGroups.add(group1);
        hostGroups.add(group2);
        hostGroups.add(group3);
        org.apache.ambari.server.topology.ClusterTopology topology = createClusterTopology(bp, clusterConfig, hostGroups);
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor updater = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor(topology);
        updater.doUpdateForClusterCreate();
        java.lang.String updatedVal = topology.getConfiguration().getFullProperties().get("storm-site").get("storm.zookeeper.servers");
        org.junit.Assert.assertTrue(updatedVal.startsWith("["));
        org.junit.Assert.assertTrue(updatedVal.endsWith("]"));
        updatedVal = updatedVal.replaceAll("[\\[\\]]", "");
        java.lang.String[] hosts = updatedVal.split(",");
        java.util.Collection<java.lang.String> expectedHosts = new java.util.HashSet<>();
        expectedHosts.add("'testhost'");
        expectedHosts.add("'testhost2'");
        expectedHosts.add("'testhost2a'");
        expectedHosts.add("'testhost2b'");
        org.junit.Assert.assertEquals(4, hosts.length);
        for (java.lang.String host : hosts) {
            org.junit.Assert.assertTrue(expectedHosts.contains(host));
            expectedHosts.remove(host);
        }
    }

    @org.junit.Test
    public void testDoUpdateForClusterCreate_Storm_Nimbus_HA_Enabled__defaultValues_YAML() throws java.lang.Exception {
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> properties = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.lang.String> typeProps = new java.util.HashMap<>();
        typeProps.put("nimbus.seeds", "localhost");
        properties.put("storm-site", typeProps);
        org.apache.ambari.server.topology.Configuration clusterConfig = new org.apache.ambari.server.topology.Configuration(properties, java.util.Collections.emptyMap());
        java.util.Collection<java.lang.String> hgComponents = new java.util.HashSet<>();
        hgComponents.add("NIMBUS");
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup group1 = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup("group1", hgComponents, java.util.Collections.singleton("testhost"));
        java.util.Collection<java.lang.String> hgComponents2 = new java.util.HashSet<>();
        hgComponents2.add("NIMBUS");
        java.util.Set<java.lang.String> hosts2 = new java.util.HashSet<>();
        hosts2.add("testhost2");
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup group2 = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup("group2", hgComponents2, hosts2);
        java.util.Collection<org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup> hostGroups = new java.util.HashSet<>();
        hostGroups.add(group1);
        hostGroups.add(group2);
        org.apache.ambari.server.topology.ClusterTopology topology = createClusterTopology(bp, clusterConfig, hostGroups);
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor updater = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor(topology);
        updater.doUpdateForClusterCreate();
        java.lang.String updatedVal = topology.getConfiguration().getFullProperties().get("storm-site").get("nimbus.seeds");
        org.junit.Assert.assertTrue("Updated YAML value should start with bracket", updatedVal.startsWith("["));
        org.junit.Assert.assertTrue("Updated YAML value should end with bracket", updatedVal.endsWith("]"));
        updatedVal = updatedVal.replaceAll("[\\[\\]]", "");
        java.lang.String[] hosts = updatedVal.split(",");
        java.util.Collection<java.lang.String> expectedHosts = new java.util.HashSet<>();
        expectedHosts.add("testhost");
        expectedHosts.add("testhost2");
        org.junit.Assert.assertEquals("Incorrect number of hosts found in updated Nimbus config property", 2, hosts.length);
        for (java.lang.String host : hosts) {
            org.junit.Assert.assertTrue(("Expected host name = " + host) + " not found in updated Nimbus config property", expectedHosts.contains(host));
            expectedHosts.remove(host);
        }
    }

    @org.junit.Test
    public void testDoUpdateForClusterCreate_Storm_Nimbus_HA_Enabled__FQDN_ValuesSpecified_YAML() throws java.lang.Exception {
        final java.lang.String expectedValue = "[c6401.ambari.apache.org, c6402.ambari.apache.org]";
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> properties = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.lang.String> typeProps = new java.util.HashMap<>();
        typeProps.put("nimbus.seeds", expectedValue);
        properties.put("storm-site", typeProps);
        org.apache.ambari.server.topology.Configuration clusterConfig = new org.apache.ambari.server.topology.Configuration(properties, java.util.Collections.emptyMap());
        java.util.Collection<java.lang.String> hgComponents = new java.util.HashSet<>();
        hgComponents.add("NIMBUS");
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup group1 = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup("group1", hgComponents, java.util.Collections.singleton("testhost"));
        java.util.Collection<java.lang.String> hgComponents2 = new java.util.HashSet<>();
        hgComponents2.add("NIMBUS");
        java.util.Set<java.lang.String> hosts2 = new java.util.HashSet<>();
        hosts2.add("testhost2");
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup group2 = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup("group2", hgComponents2, hosts2);
        java.util.Collection<org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup> hostGroups = new java.util.HashSet<>();
        hostGroups.add(group1);
        hostGroups.add(group2);
        org.apache.ambari.server.topology.ClusterTopology topology = createClusterTopology(bp, clusterConfig, hostGroups);
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor updater = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor(topology);
        updater.doUpdateForClusterCreate();
        java.lang.String updatedVal = topology.getConfiguration().getFullProperties().get("storm-site").get("nimbus.seeds");
        org.junit.Assert.assertEquals("nimbus.seeds property should not be updated when FQDNs are specified in configuration", expectedValue, updatedVal);
    }

    @org.junit.Test
    public void testDoUpdateForClusterCreate_MProperty__defaultValues() throws java.lang.Exception {
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> properties = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.lang.String> typeProps = new java.util.HashMap<>();
        typeProps.put("hbase_master_heapsize", "512m");
        properties.put("hbase-env", typeProps);
        org.apache.ambari.server.topology.Configuration clusterConfig = new org.apache.ambari.server.topology.Configuration(properties, java.util.Collections.emptyMap());
        java.util.Collection<java.lang.String> hgComponents = new java.util.HashSet<>();
        hgComponents.add("NAMENODE");
        hgComponents.add("SECONDARY_NAMENODE");
        hgComponents.add("RESOURCEMANAGER");
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup group1 = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup("group1", hgComponents, java.util.Collections.singleton("testhost"));
        java.util.Collection<java.lang.String> hgComponents2 = new java.util.HashSet<>();
        hgComponents2.add("DATANODE");
        hgComponents2.add("HDFS_CLIENT");
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup group2 = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup("group2", hgComponents2, java.util.Collections.singleton("testhost2"));
        java.util.Collection<org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup> hostGroups = new java.util.HashSet<>();
        hostGroups.add(group1);
        hostGroups.add(group2);
        org.apache.ambari.server.topology.ClusterTopology topology = createClusterTopology(bp, clusterConfig, hostGroups);
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor updater = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor(topology);
        updater.doUpdateForClusterCreate();
        java.lang.String updatedVal = topology.getConfiguration().getFullProperties().get("hbase-env").get("hbase_master_heapsize");
        org.junit.Assert.assertEquals("512m", updatedVal);
    }

    @org.junit.Test
    public void testDoUpdateForClusterCreate_MProperty__missingM() throws java.lang.Exception {
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> properties = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.lang.String> typeProps = new java.util.HashMap<>();
        typeProps.put("hbase_master_heapsize", "512");
        properties.put("hbase-env", typeProps);
        org.apache.ambari.server.topology.Configuration clusterConfig = new org.apache.ambari.server.topology.Configuration(properties, java.util.Collections.emptyMap());
        java.util.Collection<java.lang.String> hgComponents = new java.util.HashSet<>();
        hgComponents.add("NAMENODE");
        hgComponents.add("SECONDARY_NAMENODE");
        hgComponents.add("RESOURCEMANAGER");
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup group1 = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup("group1", hgComponents, java.util.Collections.singleton("testhost"));
        java.util.Collection<java.lang.String> hgComponents2 = new java.util.HashSet<>();
        hgComponents2.add("DATANODE");
        hgComponents2.add("HDFS_CLIENT");
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup group2 = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup("group2", hgComponents2, java.util.Collections.singleton("testhost2"));
        java.util.Collection<org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup> hostGroups = new java.util.HashSet<>();
        hostGroups.add(group1);
        hostGroups.add(group2);
        org.apache.ambari.server.topology.ClusterTopology topology = createClusterTopology(bp, clusterConfig, hostGroups);
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor updater = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor(topology);
        updater.doUpdateForClusterCreate();
        java.lang.String updatedVal = topology.getConfiguration().getFullProperties().get("hbase-env").get("hbase_master_heapsize");
        org.junit.Assert.assertEquals("512m", updatedVal);
    }

    @org.junit.Test
    public void testDoUpdateForClusterCreate_SingleHostProperty__exportedValue() throws java.lang.Exception {
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> properties = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.lang.String> typeProps = new java.util.HashMap<>();
        typeProps.put("yarn.resourcemanager.hostname", "%HOSTGROUP::group1%");
        properties.put("yarn-site", typeProps);
        org.apache.ambari.server.topology.Configuration clusterConfig = new org.apache.ambari.server.topology.Configuration(properties, java.util.Collections.emptyMap());
        java.util.Collection<java.lang.String> hgComponents = new java.util.HashSet<>();
        hgComponents.add("NAMENODE");
        hgComponents.add("SECONDARY_NAMENODE");
        hgComponents.add("RESOURCEMANAGER");
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup group1 = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup("group1", hgComponents, java.util.Collections.singleton("testhost"));
        java.util.Collection<java.lang.String> hgComponents2 = new java.util.HashSet<>();
        hgComponents2.add("DATANODE");
        hgComponents2.add("HDFS_CLIENT");
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup group2 = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup("group2", hgComponents2, java.util.Collections.singleton("testhost2"));
        java.util.Collection<org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup> hostGroups = new java.util.HashSet<>();
        hostGroups.add(group1);
        hostGroups.add(group2);
        org.apache.ambari.server.topology.ClusterTopology topology = createClusterTopology(bp, clusterConfig, hostGroups);
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor updater = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor(topology);
        updater.doUpdateForClusterCreate();
        java.lang.String updatedVal = topology.getConfiguration().getFullProperties().get("yarn-site").get("yarn.resourcemanager.hostname");
        org.junit.Assert.assertEquals("testhost", updatedVal);
    }

    @org.junit.Test
    public void testDoUpdateForClusterCreate_SingleHostProperty__exportedValue_UsingMinusSymbolInHostGroupName() throws java.lang.Exception {
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> properties = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.lang.String> typeProps = new java.util.HashMap<>();
        typeProps.put("yarn.resourcemanager.hostname", "%HOSTGROUP::os-amb-r6-secha-1427972156-hbaseha-3-6%");
        properties.put("yarn-site", typeProps);
        org.apache.ambari.server.topology.Configuration clusterConfig = new org.apache.ambari.server.topology.Configuration(properties, java.util.Collections.emptyMap());
        java.util.Collection<java.lang.String> hgComponents = new java.util.HashSet<>();
        hgComponents.add("NAMENODE");
        hgComponents.add("SECONDARY_NAMENODE");
        hgComponents.add("RESOURCEMANAGER");
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup group1 = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup("os-amb-r6-secha-1427972156-hbaseha-3-6", hgComponents, java.util.Collections.singleton("testhost"));
        java.util.Collection<java.lang.String> hgComponents2 = new java.util.HashSet<>();
        hgComponents2.add("DATANODE");
        hgComponents2.add("HDFS_CLIENT");
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup group2 = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup("group2", hgComponents2, java.util.Collections.singleton("testhost2"));
        java.util.Collection<org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup> hostGroups = new java.util.HashSet<>();
        hostGroups.add(group1);
        hostGroups.add(group2);
        org.apache.ambari.server.topology.ClusterTopology topology = createClusterTopology(bp, clusterConfig, hostGroups);
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor updater = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor(topology);
        updater.doUpdateForClusterCreate();
        java.lang.String updatedVal = topology.getConfiguration().getFullProperties().get("yarn-site").get("yarn.resourcemanager.hostname");
        org.junit.Assert.assertEquals("testhost", updatedVal);
    }

    @org.junit.Test
    public void testDoUpdateForClusterCreate_SingleHostProperty__exportedValue_WithPort_UsingMinusSymbolInHostGroupName() throws java.lang.Exception {
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> properties = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.lang.String> typeProps = new java.util.HashMap<>();
        typeProps.put("yarn.resourcemanager.hostname", "%HOSTGROUP::os-amb-r6-secha-1427972156-hbaseha-3-6%:2180");
        properties.put("yarn-site", typeProps);
        org.apache.ambari.server.topology.Configuration clusterConfig = new org.apache.ambari.server.topology.Configuration(properties, java.util.Collections.emptyMap());
        java.util.Collection<java.lang.String> hgComponents = new java.util.HashSet<>();
        hgComponents.add("NAMENODE");
        hgComponents.add("SECONDARY_NAMENODE");
        hgComponents.add("RESOURCEMANAGER");
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup group1 = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup("os-amb-r6-secha-1427972156-hbaseha-3-6", hgComponents, java.util.Collections.singleton("testhost"));
        java.util.Collection<java.lang.String> hgComponents2 = new java.util.HashSet<>();
        hgComponents2.add("DATANODE");
        hgComponents2.add("HDFS_CLIENT");
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup group2 = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup("group2", hgComponents2, java.util.Collections.singleton("testhost2"));
        java.util.Collection<org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup> hostGroups = new java.util.HashSet<>();
        hostGroups.add(group1);
        hostGroups.add(group2);
        org.apache.ambari.server.topology.ClusterTopology topology = createClusterTopology(bp, clusterConfig, hostGroups);
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor updater = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor(topology);
        updater.doUpdateForClusterCreate();
        java.lang.String updatedVal = topology.getConfiguration().getFullProperties().get("yarn-site").get("yarn.resourcemanager.hostname");
        org.junit.Assert.assertEquals("testhost:2180", updatedVal);
    }

    @org.junit.Test
    public void testDoUpdateForClusterCreate_SingleHostProperty__exportedValue__WithPort() throws java.lang.Exception {
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> properties = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.lang.String> typeProps = new java.util.HashMap<>();
        typeProps.put("fs.defaultFS", "%HOSTGROUP::group1%:5050");
        properties.put("core-site", typeProps);
        org.apache.ambari.server.topology.Configuration clusterConfig = new org.apache.ambari.server.topology.Configuration(properties, java.util.Collections.emptyMap());
        java.util.Collection<java.lang.String> hgComponents = new java.util.HashSet<>();
        hgComponents.add("NAMENODE");
        hgComponents.add("SECONDARY_NAMENODE");
        hgComponents.add("RESOURCEMANAGER");
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup group1 = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup("group1", hgComponents, java.util.Collections.singleton("testhost"));
        java.util.Collection<java.lang.String> hgComponents2 = new java.util.HashSet<>();
        hgComponents2.add("DATANODE");
        hgComponents2.add("HDFS_CLIENT");
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup group2 = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup("group2", hgComponents2, java.util.Collections.singleton("testhost2"));
        java.util.Collection<org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup> hostGroups = new java.util.HashSet<>();
        hostGroups.add(group1);
        hostGroups.add(group2);
        org.apache.ambari.server.topology.ClusterTopology topology = createClusterTopology(bp, clusterConfig, hostGroups);
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor updater = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor(topology);
        updater.doUpdateForClusterCreate();
        java.lang.String updatedVal = topology.getConfiguration().getFullProperties().get("core-site").get("fs.defaultFS");
        org.junit.Assert.assertEquals("testhost:5050", updatedVal);
    }

    @org.junit.Test
    public void testDoUpdateForClusterCreate_MultiHostProperty__exportedValues() throws java.lang.Exception {
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> properties = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.lang.String> typeProps = new java.util.HashMap<>();
        typeProps.put("hbase.zookeeper.quorum", "%HOSTGROUP::group1%,%HOSTGROUP::group2%");
        properties.put("hbase-site", typeProps);
        org.apache.ambari.server.topology.Configuration clusterConfig = new org.apache.ambari.server.topology.Configuration(properties, java.util.Collections.emptyMap());
        java.util.Collection<java.lang.String> hgComponents = new java.util.HashSet<>();
        hgComponents.add("NAMENODE");
        hgComponents.add("SECONDARY_NAMENODE");
        hgComponents.add("ZOOKEEPER_SERVER");
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup group1 = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup("group1", hgComponents, java.util.Collections.singleton("testhost"));
        java.util.Collection<java.lang.String> hgComponents2 = new java.util.HashSet<>();
        hgComponents2.add("DATANODE");
        hgComponents2.add("HDFS_CLIENT");
        hgComponents2.add("ZOOKEEPER_SERVER");
        java.util.Set<java.lang.String> hosts2 = new java.util.HashSet<>();
        hosts2.add("testhost2");
        hosts2.add("testhost2a");
        hosts2.add("testhost2b");
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup group2 = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup("group2", hgComponents2, hosts2);
        java.util.Collection<java.lang.String> hgComponents3 = new java.util.HashSet<>();
        hgComponents2.add("HDFS_CLIENT");
        hgComponents2.add("ZOOKEEPER_CLIENT");
        java.util.Set<java.lang.String> hosts3 = new java.util.HashSet<>();
        hosts3.add("testhost3");
        hosts3.add("testhost3a");
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup group3 = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup("group3", hgComponents3, hosts3);
        java.util.Collection<org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup> hostGroups = new java.util.HashSet<>();
        hostGroups.add(group1);
        hostGroups.add(group2);
        hostGroups.add(group3);
        org.apache.ambari.server.topology.ClusterTopology topology = createClusterTopology(bp, clusterConfig, hostGroups);
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor updater = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor(topology);
        updater.doUpdateForClusterCreate();
        java.lang.String updatedVal = topology.getConfiguration().getFullProperties().get("hbase-site").get("hbase.zookeeper.quorum");
        java.lang.String[] hosts = updatedVal.split(",");
        java.util.Collection<java.lang.String> expectedHosts = new java.util.HashSet<>();
        expectedHosts.add("testhost");
        expectedHosts.add("testhost2");
        expectedHosts.add("testhost2a");
        expectedHosts.add("testhost2b");
        org.junit.Assert.assertEquals(4, hosts.length);
        for (java.lang.String host : hosts) {
            org.junit.Assert.assertTrue(expectedHosts.contains(host));
            expectedHosts.remove(host);
        }
    }

    @org.junit.Test
    public void testDoUpdateForClusterCreate_MultiHostProperty__exportedValues___withPorts() throws java.lang.Exception {
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> properties = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.lang.String> typeProps = new java.util.HashMap<>();
        typeProps.put("templeton.zookeeper.hosts", "%HOSTGROUP::group1%:9090,%HOSTGROUP::group2%:9091");
        properties.put("webhcat-site", typeProps);
        org.apache.ambari.server.topology.Configuration clusterConfig = new org.apache.ambari.server.topology.Configuration(properties, java.util.Collections.emptyMap());
        java.util.Collection<java.lang.String> hgComponents = new java.util.HashSet<>();
        hgComponents.add("NAMENODE");
        hgComponents.add("SECONDARY_NAMENODE");
        hgComponents.add("ZOOKEEPER_SERVER");
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup group1 = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup("group1", hgComponents, java.util.Collections.singleton("testhost"));
        java.util.Collection<java.lang.String> hgComponents2 = new java.util.HashSet<>();
        hgComponents2.add("DATANODE");
        hgComponents2.add("HDFS_CLIENT");
        hgComponents2.add("ZOOKEEPER_SERVER");
        java.util.Set<java.lang.String> hosts2 = new java.util.HashSet<>();
        hosts2.add("testhost2");
        hosts2.add("testhost2a");
        hosts2.add("testhost2b");
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup group2 = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup("group2", hgComponents2, hosts2);
        java.util.Collection<java.lang.String> hgComponents3 = new java.util.HashSet<>();
        hgComponents2.add("HDFS_CLIENT");
        hgComponents2.add("ZOOKEEPER_CLIENT");
        java.util.Set<java.lang.String> hosts3 = new java.util.HashSet<>();
        hosts3.add("testhost3");
        hosts3.add("testhost3a");
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup group3 = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup("group3", hgComponents3, hosts3);
        java.util.Collection<org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup> hostGroups = new java.util.HashSet<>();
        hostGroups.add(group1);
        hostGroups.add(group2);
        hostGroups.add(group3);
        org.apache.ambari.server.topology.ClusterTopology topology = createClusterTopology(bp, clusterConfig, hostGroups);
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor updater = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor(topology);
        updater.doUpdateForClusterCreate();
        java.lang.String updatedVal = topology.getConfiguration().getFullProperties().get("webhcat-site").get("templeton.zookeeper.hosts");
        java.lang.String[] hosts = updatedVal.split(",");
        java.util.Collection<java.lang.String> expectedHosts = new java.util.HashSet<>();
        expectedHosts.add("testhost:9090");
        expectedHosts.add("testhost2:9091");
        expectedHosts.add("testhost2a:9091");
        expectedHosts.add("testhost2b:9091");
        org.junit.Assert.assertEquals(4, hosts.length);
        for (java.lang.String host : hosts) {
            org.junit.Assert.assertTrue(expectedHosts.contains(host));
            expectedHosts.remove(host);
        }
    }

    @org.junit.Test
    public void testDoUpdateForClusterCreate_MultiHostProperty__exportedValues___withPorts_UsingMinusSymbolInHostGroupName() throws java.lang.Exception {
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> properties = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.lang.String> typeProps = new java.util.HashMap<>();
        typeProps.put("ha.zookeeper.quorum", "%HOSTGROUP::os-amb-r6-secha-1427972156-hbaseha-3-6%:2181,%HOSTGROUP::os-amb-r6-secha-1427972156-hbaseha-3-5%:2181,%HOSTGROUP::os-amb-r6-secha-1427972156-hbaseha-3-7%:2181");
        properties.put("core-site", typeProps);
        org.apache.ambari.server.topology.Configuration clusterConfig = new org.apache.ambari.server.topology.Configuration(properties, java.util.Collections.emptyMap());
        java.util.Collection<java.lang.String> hgComponents = new java.util.HashSet<>();
        hgComponents.add("NAMENODE");
        hgComponents.add("SECONDARY_NAMENODE");
        hgComponents.add("ZOOKEEPER_SERVER");
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup group1 = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup("os-amb-r6-secha-1427972156-hbaseha-3-6", hgComponents, java.util.Collections.singleton("testhost"));
        java.util.Collection<java.lang.String> hgComponents2 = new java.util.HashSet<>();
        hgComponents2.add("DATANODE");
        hgComponents2.add("HDFS_CLIENT");
        hgComponents2.add("ZOOKEEPER_SERVER");
        java.util.Set<java.lang.String> hosts2 = new java.util.HashSet<>();
        hosts2.add("testhost2");
        hosts2.add("testhost2a");
        hosts2.add("testhost2b");
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup group2 = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup("os-amb-r6-secha-1427972156-hbaseha-3-5", hgComponents2, hosts2);
        java.util.Collection<java.lang.String> hgComponents3 = new java.util.HashSet<>();
        hgComponents2.add("HDFS_CLIENT");
        hgComponents2.add("ZOOKEEPER_CLIENT");
        java.util.Set<java.lang.String> hosts3 = new java.util.HashSet<>();
        hosts3.add("testhost3");
        hosts3.add("testhost3a");
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup group3 = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup("os-amb-r6-secha-1427972156-hbaseha-3-7", hgComponents3, hosts3);
        java.util.Collection<org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup> hostGroups = new java.util.HashSet<>();
        hostGroups.add(group1);
        hostGroups.add(group2);
        hostGroups.add(group3);
        org.apache.ambari.server.topology.ClusterTopology topology = createClusterTopology(bp, clusterConfig, hostGroups);
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor updater = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor(topology);
        updater.doUpdateForClusterCreate();
        java.lang.String updatedVal = topology.getConfiguration().getFullProperties().get("core-site").get("ha.zookeeper.quorum");
        java.lang.String[] hosts = updatedVal.split(",");
        java.util.Collection<java.lang.String> expectedHosts = new java.util.HashSet<>();
        expectedHosts.add("testhost:2181");
        expectedHosts.add("testhost2:2181");
        expectedHosts.add("testhost2a:2181");
        expectedHosts.add("testhost2b:2181");
        expectedHosts.add("testhost3:2181");
        expectedHosts.add("testhost3a:2181");
        org.junit.Assert.assertEquals(6, hosts.length);
        for (java.lang.String host : hosts) {
            org.junit.Assert.assertTrue(("Expected host :" + host) + "was not included in the multi-server list in this property.", expectedHosts.contains(host));
            expectedHosts.remove(host);
        }
    }

    @org.junit.Test
    public void testDoUpdateForClusterCreate_MultiHostProperty_exportedValues_withPorts_singleHostValue() throws java.lang.Exception {
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> properties = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.lang.String> yarnSiteConfig = new java.util.HashMap<>();
        yarnSiteConfig.put("hadoop.registry.zk.quorum", "%HOSTGROUP::host_group_1%:2181");
        properties.put("yarn-site", yarnSiteConfig);
        org.apache.ambari.server.topology.Configuration clusterConfig = new org.apache.ambari.server.topology.Configuration(properties, java.util.Collections.emptyMap());
        java.util.Collection<java.lang.String> hgComponents = new java.util.HashSet<>();
        hgComponents.add("NAMENODE");
        hgComponents.add("SECONDARY_NAMENODE");
        hgComponents.add("ZOOKEEPER_SERVER");
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup group1 = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup("host_group_1", hgComponents, java.util.Collections.singleton("testhost"));
        java.util.Collection<org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup> hostGroups = new java.util.HashSet<>();
        hostGroups.add(group1);
        org.apache.ambari.server.topology.ClusterTopology topology = createClusterTopology(bp, clusterConfig, hostGroups);
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor updater = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor(topology);
        updater.doUpdateForClusterCreate();
        org.junit.Assert.assertEquals("Multi-host property with single host value was not correctly updated for cluster create.", "testhost:2181", topology.getConfiguration().getFullProperties().get("yarn-site").get("hadoop.registry.zk.quorum"));
    }

    @org.junit.Test
    public void testDoUpdateForClusterCreate_MultiHostProperty__exportedValues___YAML() throws java.lang.Exception {
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> properties = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.lang.String> typeProps = new java.util.HashMap<>();
        typeProps.put("storm.zookeeper.servers", "['%HOSTGROUP::group1%:9090','%HOSTGROUP::group2%:9091']");
        typeProps.put("nimbus.seeds", "[%HOSTGROUP::group1%, %HOSTGROUP::group4%]");
        properties.put("storm-site", typeProps);
        org.apache.ambari.server.topology.Configuration clusterConfig = new org.apache.ambari.server.topology.Configuration(properties, java.util.Collections.emptyMap());
        java.util.Collection<java.lang.String> hgComponents = new java.util.HashSet<>();
        hgComponents.add("NAMENODE");
        hgComponents.add("SECONDARY_NAMENODE");
        hgComponents.add("ZOOKEEPER_SERVER");
        hgComponents.add("NIMBUS");
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup group1 = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup("group1", hgComponents, java.util.Collections.singleton("testhost"));
        java.util.Collection<java.lang.String> hgComponents2 = new java.util.HashSet<>();
        hgComponents2.add("DATANODE");
        hgComponents2.add("HDFS_CLIENT");
        hgComponents2.add("ZOOKEEPER_SERVER");
        hgComponents2.add("NIMBUS");
        java.util.Set<java.lang.String> hosts2 = new java.util.HashSet<>();
        hosts2.add("testhost2");
        hosts2.add("testhost2a");
        hosts2.add("testhost2b");
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup group2 = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup("group2", hgComponents2, hosts2);
        java.util.Collection<java.lang.String> hgComponents3 = new java.util.HashSet<>();
        hgComponents2.add("HDFS_CLIENT");
        hgComponents2.add("ZOOKEEPER_CLIENT");
        java.util.Set<java.lang.String> hosts3 = new java.util.HashSet<>();
        hosts3.add("testhost3");
        hosts3.add("testhost3a");
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup group3 = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup("group3", hgComponents3, hosts3);
        java.util.Collection<java.lang.String> hgComponents4 = new java.util.HashSet<>();
        hgComponents4.add("NIMBUS");
        java.util.Set<java.lang.String> hosts4 = new java.util.HashSet<>();
        hosts4.add("testhost4");
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup group4 = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup("group4", hgComponents4, hosts4);
        java.util.Collection<org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup> hostGroups = new java.util.HashSet<>();
        hostGroups.add(group1);
        hostGroups.add(group2);
        hostGroups.add(group3);
        hostGroups.add(group4);
        org.apache.ambari.server.topology.ClusterTopology topology = createClusterTopology(bp, clusterConfig, hostGroups);
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor updater = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor(topology);
        updater.doUpdateForClusterCreate();
        java.lang.String updatedVal = topology.getConfiguration().getFullProperties().get("storm-site").get("storm.zookeeper.servers");
        org.junit.Assert.assertTrue(updatedVal.startsWith("["));
        org.junit.Assert.assertTrue(updatedVal.endsWith("]"));
        updatedVal = updatedVal.replaceAll("[\\[\\]]", "");
        java.lang.String[] hosts = updatedVal.split(",");
        java.util.Collection<java.lang.String> expectedHosts = new java.util.HashSet<>();
        expectedHosts.add("'testhost:9090'");
        expectedHosts.add("'testhost2:9091'");
        expectedHosts.add("'testhost2a:9091'");
        expectedHosts.add("'testhost2b:9091'");
        org.junit.Assert.assertEquals(4, hosts.length);
        for (java.lang.String host : hosts) {
            org.junit.Assert.assertTrue(expectedHosts.contains(host));
            expectedHosts.remove(host);
        }
        java.lang.String updatedNimbusSeedsVal = topology.getConfiguration().getFullProperties().get("storm-site").get("nimbus.seeds");
        org.junit.Assert.assertTrue("Updated YAML value should start with bracket", updatedNimbusSeedsVal.startsWith("["));
        org.junit.Assert.assertTrue("Updated YAML value should end with bracket", updatedNimbusSeedsVal.endsWith("]"));
        updatedNimbusSeedsVal = updatedNimbusSeedsVal.replaceAll("[\\[\\]]", "");
        java.lang.String[] nimbusHosts = updatedNimbusSeedsVal.split(",");
        java.util.Collection<java.lang.String> expectedNimbusHosts = new java.util.HashSet<>();
        expectedNimbusHosts.add("testhost");
        expectedNimbusHosts.add("testhost4");
        org.junit.Assert.assertEquals("Incorrect number of hosts found in updated Nimbus config property", 2, nimbusHosts.length);
        for (java.lang.String host : nimbusHosts) {
            org.junit.Assert.assertTrue(("Expected Nimbus host = " + host) + " not found in nimbus.seeds property value", expectedNimbusHosts.contains(host));
            expectedHosts.remove(host);
        }
    }

    @org.junit.Test
    public void testDoUpdateForClusterCreate_DBHostProperty__defaultValue() throws java.lang.Exception {
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> properties = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.lang.String> hiveSiteProps = new java.util.HashMap<>();
        hiveSiteProps.put("javax.jdo.option.ConnectionURL", "jdbc:mysql://localhost/hive?createDatabaseIfNotExist=true");
        java.util.Map<java.lang.String, java.lang.String> hiveEnvProps = new java.util.HashMap<>();
        hiveEnvProps.put("hive_database", "New MySQL Database");
        properties.put("hive-site", hiveSiteProps);
        properties.put("hive-env", hiveEnvProps);
        org.apache.ambari.server.topology.Configuration clusterConfig = new org.apache.ambari.server.topology.Configuration(properties, java.util.Collections.emptyMap());
        java.util.Collection<java.lang.String> hgComponents = new java.util.HashSet<>();
        hgComponents.add("NAMENODE");
        hgComponents.add("SECONDARY_NAMENODE");
        hgComponents.add("RESOURCEMANAGER");
        hgComponents.add("MYSQL_SERVER");
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup group1 = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup("group1", hgComponents, java.util.Collections.singleton("testhost"));
        java.util.Collection<java.lang.String> hgComponents2 = new java.util.HashSet<>();
        hgComponents2.add("DATANODE");
        hgComponents2.add("HDFS_CLIENT");
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup group2 = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup("group2", hgComponents2, java.util.Collections.singleton("testhost2"));
        java.util.Collection<org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup> hostGroups = new java.util.HashSet<>();
        hostGroups.add(group1);
        hostGroups.add(group2);
        org.apache.ambari.server.topology.ClusterTopology topology = createClusterTopology(bp, clusterConfig, hostGroups);
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor updater = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor(topology);
        updater.doUpdateForClusterCreate();
        java.lang.String updatedVal = topology.getConfiguration().getFullProperties().get("hive-site").get("javax.jdo.option.ConnectionURL");
        org.junit.Assert.assertEquals("jdbc:mysql://testhost/hive?createDatabaseIfNotExist=true", updatedVal);
    }

    @org.junit.Test
    public void testDoUpdateForClusterCreate_DBHostProperty__exportedValue() throws java.lang.Exception {
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> properties = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.lang.String> hiveSiteProps = new java.util.HashMap<>();
        hiveSiteProps.put("javax.jdo.option.ConnectionURL", "jdbc:mysql://%HOSTGROUP::group1%/hive?createDatabaseIfNotExist=true");
        java.util.Map<java.lang.String, java.lang.String> hiveEnvProps = new java.util.HashMap<>();
        hiveEnvProps.put("hive_database", "New MySQL Database");
        properties.put("hive-site", hiveSiteProps);
        properties.put("hive-env", hiveEnvProps);
        org.apache.ambari.server.topology.Configuration clusterConfig = new org.apache.ambari.server.topology.Configuration(properties, java.util.Collections.emptyMap());
        java.util.Collection<java.lang.String> hgComponents = new java.util.HashSet<>();
        hgComponents.add("NAMENODE");
        hgComponents.add("SECONDARY_NAMENODE");
        hgComponents.add("RESOURCEMANAGER");
        hgComponents.add("MYSQL_SERVER");
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup group1 = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup("group1", hgComponents, java.util.Collections.singleton("testhost"));
        java.util.Collection<java.lang.String> hgComponents2 = new java.util.HashSet<>();
        hgComponents2.add("DATANODE");
        hgComponents2.add("HDFS_CLIENT");
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup group2 = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup("group2", hgComponents2, java.util.Collections.singleton("testhost2"));
        java.util.Collection<org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup> hostGroups = new java.util.HashSet<>();
        hostGroups.add(group1);
        hostGroups.add(group2);
        org.apache.ambari.server.topology.ClusterTopology topology = createClusterTopology(bp, clusterConfig, hostGroups);
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor updater = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor(topology);
        updater.doUpdateForClusterCreate();
        java.lang.String updatedVal = topology.getConfiguration().getFullProperties().get("hive-site").get("javax.jdo.option.ConnectionURL");
        org.junit.Assert.assertEquals("jdbc:mysql://testhost/hive?createDatabaseIfNotExist=true", updatedVal);
    }

    @org.junit.Test
    public void testDoUpdateForClusterCreate_DBHostProperty__external() throws java.lang.Exception {
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> properties = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.lang.String> typeProps = new java.util.HashMap<>();
        typeProps.put("javax.jdo.option.ConnectionURL", "jdbc:mysql://myHost.com/hive?createDatabaseIfNotExist=true");
        typeProps.put("hive_database", "Existing MySQL Database");
        properties.put("hive-env", typeProps);
        org.apache.ambari.server.topology.Configuration clusterConfig = new org.apache.ambari.server.topology.Configuration(properties, java.util.Collections.emptyMap());
        java.util.Collection<java.lang.String> hgComponents = new java.util.HashSet<>();
        hgComponents.add("NAMENODE");
        hgComponents.add("SECONDARY_NAMENODE");
        hgComponents.add("RESOURCEMANAGER");
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup group1 = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup("group1", hgComponents, java.util.Collections.singleton("testhost"));
        java.util.Collection<java.lang.String> hgComponents2 = new java.util.HashSet<>();
        hgComponents2.add("DATANODE");
        hgComponents2.add("HDFS_CLIENT");
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup group2 = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup("group2", hgComponents2, java.util.Collections.singleton("testhost2"));
        java.util.Collection<org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup> hostGroups = new java.util.HashSet<>();
        hostGroups.add(group1);
        hostGroups.add(group2);
        org.apache.ambari.server.topology.ClusterTopology topology = createClusterTopology(bp, clusterConfig, hostGroups);
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor updater = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor(topology);
        updater.doUpdateForClusterCreate();
        java.lang.String updatedVal = topology.getConfiguration().getFullProperties().get("hive-env").get("javax.jdo.option.ConnectionURL");
        org.junit.Assert.assertEquals("jdbc:mysql://myHost.com/hive?createDatabaseIfNotExist=true", updatedVal);
    }

    @org.junit.Test
    public void testExcludedPropertiesShouldBeAddedWhenServiceIsInBlueprint() throws java.lang.Exception {
        EasyMock.reset(stack);
        EasyMock.expect(stack.getName()).andReturn("testStack").anyTimes();
        EasyMock.expect(stack.getVersion()).andReturn("1").anyTimes();
        EasyMock.expect(stack.isMasterComponent(((java.lang.String) (EasyMock.anyObject())))).andReturn(false).anyTimes();
        EasyMock.expect(stack.getExcludedConfigurationTypes("FALCON")).andReturn(java.util.Collections.singleton("oozie-site"));
        EasyMock.expect(stack.getExcludedConfigurationTypes("OOZIE")).andReturn(java.util.Collections.emptySet());
        EasyMock.expect(stack.getConfigurationProperties("FALCON", "oozie-site")).andReturn(java.util.Collections.singletonMap("oozie.service.ELService.ext.functions.coord-job-submit-instances", "testValue")).anyTimes();
        EasyMock.expect(stack.getServiceForConfigType("oozie-site")).andReturn("OOZIE").anyTimes();
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> properties = new java.util.HashMap<>();
        org.apache.ambari.server.topology.Configuration clusterConfig = new org.apache.ambari.server.topology.Configuration(properties, java.util.Collections.emptyMap());
        java.util.Collection<java.lang.String> hgComponents = new java.util.HashSet<>();
        hgComponents.add("FALCON_SERVER");
        hgComponents.add("FALCON_CLIENT");
        hgComponents.add("OOZIE_SERVER");
        hgComponents.add("OOZIE_CLIENT");
        java.util.List<java.lang.String> hosts = new java.util.ArrayList<>();
        hosts.add("c6401.apache.ambari.org");
        hosts.add("serverTwo");
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup group1 = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup("host_group_1", hgComponents, hosts);
        java.util.Collection<org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup> hostGroups = new java.util.HashSet<>();
        hostGroups.add(group1);
        org.apache.ambari.server.topology.ClusterTopology topology = createClusterTopology(bp, clusterConfig, hostGroups);
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor updater = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor(topology);
        updater.doUpdateForClusterCreate();
        org.junit.Assert.assertEquals("Falcon Broker URL property not properly exported", "testValue", clusterConfig.getPropertyValue("oozie-site", "oozie.service.ELService.ext.functions.coord-job-submit-instances"));
    }

    @org.junit.Test
    public void testExcludedPropertiesShouldBeIgnoredWhenServiceIsNotInBlueprint() throws java.lang.Exception {
        EasyMock.reset(stack);
        EasyMock.expect(stack.getName()).andReturn("testStack").anyTimes();
        EasyMock.expect(stack.getVersion()).andReturn("1").anyTimes();
        EasyMock.expect(stack.isMasterComponent(((java.lang.String) (EasyMock.anyObject())))).andReturn(false).anyTimes();
        EasyMock.expect(stack.getExcludedConfigurationTypes("FALCON")).andReturn(java.util.Collections.singleton("oozie-site")).anyTimes();
        EasyMock.expect(stack.getConfigurationProperties("FALCON", "oozie-site")).andReturn(java.util.Collections.singletonMap("oozie.service.ELService.ext.functions.coord-job-submit-instances", "testValue")).anyTimes();
        EasyMock.expect(stack.getServiceForConfigType("oozie-site")).andReturn("OOZIE").anyTimes();
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> properties = new java.util.HashMap<>();
        org.apache.ambari.server.topology.Configuration clusterConfig = new org.apache.ambari.server.topology.Configuration(properties, java.util.Collections.emptyMap());
        java.util.Collection<java.lang.String> hgComponents = new java.util.HashSet<>();
        hgComponents.add("FALCON_SERVER");
        hgComponents.add("FALCON_CLIENT");
        java.util.List<java.lang.String> hosts = new java.util.ArrayList<>();
        hosts.add("c6401.apache.ambari.org");
        hosts.add("serverTwo");
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup group1 = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup("host_group_1", hgComponents, hosts);
        java.util.Collection<org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup> hostGroups = new java.util.HashSet<>();
        hostGroups.add(group1);
        org.apache.ambari.server.topology.ClusterTopology topology = createClusterTopology(bp, clusterConfig, hostGroups);
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor updater = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor(topology);
        updater.doUpdateForClusterCreate();
        org.junit.Assert.assertNull("Excluded properties shouldn't be added in this setup!", clusterConfig.getPropertyValue("oozie-site", "oozie.service.ELService.ext.functions.coord-job-submit-instances"));
    }

    @org.junit.Test
    public void testAddExcludedPropertiesAreOverwrittenByBlueprintConfigs() throws java.lang.Exception {
        EasyMock.reset(stack);
        EasyMock.expect(stack.getName()).andReturn("testStack").anyTimes();
        EasyMock.expect(stack.getVersion()).andReturn("1").anyTimes();
        EasyMock.expect(stack.isMasterComponent(((java.lang.String) (EasyMock.anyObject())))).andReturn(false).anyTimes();
        EasyMock.expect(stack.getConfigurationPropertiesWithMetadata(EasyMock.anyObject(java.lang.String.class), EasyMock.anyObject(java.lang.String.class))).andReturn(java.util.Collections.emptyMap()).anyTimes();
        EasyMock.expect(stack.getExcludedConfigurationTypes("FALCON")).andReturn(java.util.Collections.singleton("oozie-site")).anyTimes();
        EasyMock.expect(stack.getConfigurationProperties("FALCON", "oozie-site")).andReturn(java.util.Collections.singletonMap("oozie.service.ELService.ext.functions.coord-job-submit-instances", "testValue")).anyTimes();
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> properties = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.lang.String> typeProps = new java.util.HashMap<>();
        typeProps.put("oozie.service.ELService.ext.functions.coord-job-submit-instances", "overridedValue");
        properties.put("oozie-site", typeProps);
        org.apache.ambari.server.topology.Configuration clusterConfig = new org.apache.ambari.server.topology.Configuration(properties, java.util.Collections.emptyMap());
        java.util.Collection<java.lang.String> hgComponents = new java.util.HashSet<>();
        hgComponents.add("FALCON_SERVER");
        hgComponents.add("FALCON_CLIENT");
        java.util.List<java.lang.String> hosts = new java.util.ArrayList<>();
        hosts.add("c6401.apache.ambari.org");
        hosts.add("serverTwo");
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup group1 = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup("host_group_1", hgComponents, hosts);
        java.util.Collection<org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup> hostGroups = new java.util.HashSet<>();
        hostGroups.add(group1);
        org.apache.ambari.server.topology.ClusterTopology topology = createClusterTopology(bp, clusterConfig, hostGroups);
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor updater = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor(topology);
        updater.doUpdateForClusterCreate();
        org.junit.Assert.assertEquals("Falcon Broker URL property not properly exported", "overridedValue", clusterConfig.getPropertyValue("oozie-site", "oozie.service.ELService.ext.functions.coord-job-submit-instances"));
    }

    @org.junit.Test
    public void testExcludedPropertiesHandlingWhenExcludedConfigServiceIsNotFoundInStack() throws java.lang.Exception {
        EasyMock.reset(stack);
        EasyMock.expect(stack.getName()).andReturn("testStack").anyTimes();
        EasyMock.expect(stack.getVersion()).andReturn("1").anyTimes();
        EasyMock.expect(stack.isMasterComponent(((java.lang.String) (EasyMock.anyObject())))).andReturn(false).anyTimes();
        java.util.Set<java.lang.String> excludedConfigTypes = new java.util.HashSet<>();
        excludedConfigTypes.add("oozie-site");
        excludedConfigTypes.add("storm-site");
        EasyMock.expect(stack.getExcludedConfigurationTypes("FALCON")).andReturn(excludedConfigTypes);
        EasyMock.expect(stack.getExcludedConfigurationTypes("OOZIE")).andReturn(java.util.Collections.emptySet());
        EasyMock.expect(stack.getConfigurationProperties("FALCON", "oozie-site")).andReturn(java.util.Collections.singletonMap("oozie.service.ELService.ext.functions.coord-job-submit-instances", "testValue")).anyTimes();
        EasyMock.expect(stack.getServiceForConfigType("oozie-site")).andReturn("OOZIE").anyTimes();
        EasyMock.expect(stack.getServiceForConfigType("storm-site")).andThrow(new java.lang.IllegalArgumentException("TEST: Configuration not found in stack definitions!"));
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> properties = new java.util.HashMap<>();
        org.apache.ambari.server.topology.Configuration clusterConfig = new org.apache.ambari.server.topology.Configuration(properties, java.util.Collections.emptyMap());
        java.util.Collection<java.lang.String> hgComponents = new java.util.HashSet<>();
        hgComponents.add("FALCON_SERVER");
        hgComponents.add("FALCON_CLIENT");
        hgComponents.add("OOZIE_SERVER");
        hgComponents.add("OOZIE_CLIENT");
        java.util.List<java.lang.String> hosts = new java.util.ArrayList<>();
        hosts.add("c6401.apache.ambari.org");
        hosts.add("serverTwo");
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup group1 = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup("host_group_1", hgComponents, hosts);
        java.util.Collection<org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup> hostGroups = new java.util.HashSet<>();
        hostGroups.add(group1);
        org.apache.ambari.server.topology.ClusterTopology topology = createClusterTopology(bp, clusterConfig, hostGroups);
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor updater = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor(topology);
        updater.doUpdateForClusterCreate();
        org.junit.Assert.assertEquals("Falcon Broker URL property not properly exported", "testValue", clusterConfig.getPropertyValue("oozie-site", "oozie.service.ELService.ext.functions.coord-job-submit-instances"));
    }

    @org.junit.Test
    public void testFalconConfigClusterUpdate() throws java.lang.Exception {
        final java.lang.String expectedHostName = "c6401.apache.ambari.org";
        final java.lang.String expectedPortNum = "808080";
        final java.lang.String expectedHostGroupName = "host_group_1";
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> properties = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.lang.String> falconStartupProperties = new java.util.HashMap<>();
        properties.put("falcon-startup.properties", falconStartupProperties);
        falconStartupProperties.put("*.broker.url", org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.createExportedAddress(expectedPortNum, expectedHostGroupName));
        falconStartupProperties.put("*.falcon.service.authentication.kerberos.principal", ("falcon/" + org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.createExportedHostName(expectedHostGroupName)) + "@EXAMPLE.COM");
        falconStartupProperties.put("*.falcon.http.authentication.kerberos.principal", ("HTTP/" + org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.createExportedHostName(expectedHostGroupName)) + "@EXAMPLE.COM");
        org.apache.ambari.server.topology.Configuration clusterConfig = new org.apache.ambari.server.topology.Configuration(properties, java.util.Collections.emptyMap());
        java.util.Collection<java.lang.String> hgComponents = new java.util.HashSet<>();
        hgComponents.add("FALCON_SERVER");
        hgComponents.add("FALCON_CLIENT");
        java.util.List<java.lang.String> hosts = new java.util.ArrayList<>();
        hosts.add("c6401.apache.ambari.org");
        hosts.add("server-two");
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup group1 = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup("host_group_1", hgComponents, hosts);
        java.util.Collection<org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup> hostGroups = new java.util.HashSet<>();
        hostGroups.add(group1);
        org.apache.ambari.server.topology.ClusterTopology topology = createClusterTopology(bp, clusterConfig, hostGroups);
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor updater = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor(topology);
        updater.doUpdateForClusterCreate();
        org.junit.Assert.assertEquals("Falcon Broker URL property not properly exported", (expectedHostName + ":") + expectedPortNum, falconStartupProperties.get("*.broker.url"));
    }

    @org.junit.Test
    public void testFalconConfigClusterUpdateDefaultConfig() throws java.lang.Exception {
        final java.lang.String expectedHostName = "c6401.apache.ambari.org";
        final java.lang.String expectedPortNum = "808080";
        final java.lang.String expectedHostGroupName = "host_group_1";
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> properties = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.lang.String> falconStartupProperties = new java.util.HashMap<>();
        properties.put("falcon-startup.properties", falconStartupProperties);
        falconStartupProperties.put("*.broker.url", "localhost:" + expectedPortNum);
        falconStartupProperties.put("*.falcon.service.authentication.kerberos.principal", "falcon/" + ("localhost" + "@EXAMPLE.COM"));
        falconStartupProperties.put("*.falcon.http.authentication.kerberos.principal", "HTTP/" + ("localhost" + "@EXAMPLE.COM"));
        org.apache.ambari.server.topology.Configuration clusterConfig = new org.apache.ambari.server.topology.Configuration(properties, java.util.Collections.emptyMap());
        java.util.Collection<java.lang.String> hgComponents = new java.util.HashSet<>();
        hgComponents.add("FALCON_SERVER");
        hgComponents.add("FALCON_CLIENT");
        java.util.List<java.lang.String> hosts = new java.util.ArrayList<>();
        hosts.add(expectedHostName);
        hosts.add("server-two");
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup group1 = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup(expectedHostGroupName, hgComponents, hosts);
        java.util.Collection<org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup> hostGroups = new java.util.HashSet<>();
        hostGroups.add(group1);
        org.apache.ambari.server.topology.ClusterTopology topology = createClusterTopology(bp, clusterConfig, hostGroups);
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor updater = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor(topology);
        updater.doUpdateForClusterCreate();
        org.junit.Assert.assertEquals("Falcon Broker URL property not properly exported", (expectedHostName + ":") + expectedPortNum, falconStartupProperties.get("*.broker.url"));
    }

    @org.junit.Test
    public void testHiveConfigClusterUpdateCustomValue() throws java.lang.Exception {
        final java.lang.String expectedHostGroupName = "host_group_1";
        final java.lang.String expectedPropertyValue = "hive.metastore.local=false,hive.metastore.uris=thrift://headnode0.ivantestcluster2-ssh.d1.internal.cloudapp.net:9083,hive.user.install.directory=/user";
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> properties = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.lang.String> webHCatSiteProperties = new java.util.HashMap<>();
        properties.put("webhcat-site", webHCatSiteProperties);
        webHCatSiteProperties.put("templeton.hive.properties", expectedPropertyValue);
        org.apache.ambari.server.topology.Configuration clusterConfig = new org.apache.ambari.server.topology.Configuration(properties, java.util.Collections.emptyMap());
        java.util.Collection<java.lang.String> hgComponents = new java.util.HashSet<>();
        hgComponents.add("NAMENODE");
        java.util.List<java.lang.String> hosts = new java.util.ArrayList<>();
        hosts.add("some-hose");
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup group1 = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup(expectedHostGroupName, hgComponents, hosts);
        java.util.Collection<org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup> hostGroups = new java.util.HashSet<>();
        hostGroups.add(group1);
        org.apache.ambari.server.topology.ClusterTopology topology = createClusterTopology(bp, clusterConfig, hostGroups);
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor updater = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor(topology);
        updater.doUpdateForClusterCreate();
        org.junit.Assert.assertEquals("Unexpected config update for templeton.hive.properties", expectedPropertyValue, webHCatSiteProperties.get("templeton.hive.properties"));
    }

    @org.junit.Test
    public void testHiveConfigClusterUpdatePropertiesFilterAuthenticationOff() throws java.lang.Exception {
        EasyMock.reset(stack);
        final java.lang.String expectedHostGroupName = "host_group_1";
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> properties = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.lang.String> hiveSiteProperties = new java.util.HashMap<>();
        properties.put("hive-site", hiveSiteProperties);
        hiveSiteProperties.put("hive.server2.authentication", "NONE");
        hiveSiteProperties.put("hive.server2.authentication.kerberos.keytab", " ");
        hiveSiteProperties.put("hive.server2.authentication.kerberos.principal", " ");
        java.util.Map<java.lang.String, org.apache.ambari.server.controller.internal.Stack.ConfigProperty> mapOfMetadata = new java.util.HashMap<>();
        org.apache.ambari.server.controller.internal.Stack.ConfigProperty configProperty1 = new org.apache.ambari.server.controller.internal.Stack.ConfigProperty("hive-site", "hive.server2.authentication.kerberos.keytab", " ") {
            @java.lang.Override
            java.util.Set<org.apache.ambari.server.state.PropertyDependencyInfo> getDependsOnProperties() {
                org.apache.ambari.server.state.PropertyDependencyInfo dependencyInfo = new org.apache.ambari.server.state.PropertyDependencyInfo("hive-site", "hive.server2.authentication");
                return java.util.Collections.singleton(dependencyInfo);
            }
        };
        org.apache.ambari.server.controller.internal.Stack.ConfigProperty configProperty2 = new org.apache.ambari.server.controller.internal.Stack.ConfigProperty("hive-site", "hive.server2.authentication.kerberos.principal", " ") {
            @java.lang.Override
            java.util.Set<org.apache.ambari.server.state.PropertyDependencyInfo> getDependsOnProperties() {
                org.apache.ambari.server.state.PropertyDependencyInfo dependencyInfo = new org.apache.ambari.server.state.PropertyDependencyInfo("hive-site", "hive.server2.authentication");
                return java.util.Collections.singleton(dependencyInfo);
            }
        };
        mapOfMetadata.put("hive.server2.authentication.kerberos.keytab", configProperty1);
        mapOfMetadata.put("hive.server2.authentication.kerberos.principal", configProperty2);
        EasyMock.expect(stack.getName()).andReturn("testStack").anyTimes();
        EasyMock.expect(stack.getVersion()).andReturn("1").anyTimes();
        EasyMock.expect(stack.isMasterComponent(((java.lang.String) (EasyMock.anyObject())))).andReturn(false).anyTimes();
        java.util.Set<java.lang.String> emptySet = java.util.Collections.emptySet();
        EasyMock.expect(stack.getExcludedConfigurationTypes(EasyMock.anyObject(java.lang.String.class))).andReturn(emptySet).anyTimes();
        EasyMock.expect(stack.getServiceForConfigType("hive-site")).andReturn("HIVE").atLeastOnce();
        EasyMock.expect(stack.getConfigurationPropertiesWithMetadata("HIVE", "hive-site")).andReturn(mapOfMetadata).atLeastOnce();
        org.apache.ambari.server.topology.Configuration clusterConfig = new org.apache.ambari.server.topology.Configuration(properties, java.util.Collections.emptyMap());
        java.util.Collection<java.lang.String> hgComponents = new java.util.HashSet<>();
        hgComponents.add("NAMENODE");
        java.util.List<java.lang.String> hosts = new java.util.ArrayList<>();
        hosts.add("some-hose");
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup group1 = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup(expectedHostGroupName, hgComponents, hosts);
        java.util.Collection<org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup> hostGroups = new java.util.HashSet<>();
        hostGroups.add(group1);
        org.apache.ambari.server.topology.ClusterTopology topology = createClusterTopology(bp, clusterConfig, hostGroups);
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor updater = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor(topology);
        updater.doUpdateForClusterCreate();
        org.junit.Assert.assertFalse("hive.server2.authentication.kerberos.keytab should have been filtered out of configuration", hiveSiteProperties.containsKey("hive.server2.authentication.kerberos.keytab"));
        org.junit.Assert.assertFalse("hive.server2.authentication.kerberos.principal should have been filtered out of configuration", hiveSiteProperties.containsKey("hive.server2.authentication.kerberos.principal"));
    }

    @org.junit.Test
    public void testHiveConfigClusterUpdatePropertiesFilterAuthenticationOffFilterThrowsError() throws java.lang.Exception {
        EasyMock.reset(stack);
        final java.lang.String expectedHostGroupName = "host_group_1";
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> properties = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.lang.String> hiveSiteProperties = new java.util.HashMap<>();
        properties.put("hive-site", hiveSiteProperties);
        hiveSiteProperties.put("hive.server2.authentication", "NONE");
        hiveSiteProperties.put("hive.server2.authentication.kerberos.keytab", " ");
        hiveSiteProperties.put("hive.server2.authentication.kerberos.principal", " ");
        java.util.Map<java.lang.String, org.apache.ambari.server.controller.internal.Stack.ConfigProperty> mapOfMetadata = new java.util.HashMap<>();
        org.apache.ambari.server.controller.internal.Stack.ConfigProperty configProperty1 = new org.apache.ambari.server.controller.internal.Stack.ConfigProperty("hive-site", "hive.server2.authentication.kerberos.keytab", " ") {
            @java.lang.Override
            java.util.Set<org.apache.ambari.server.state.PropertyDependencyInfo> getDependsOnProperties() {
                org.apache.ambari.server.state.PropertyDependencyInfo dependencyInfo = new org.apache.ambari.server.state.PropertyDependencyInfo("hive-site", "hive.server2.authentication");
                return java.util.Collections.singleton(dependencyInfo);
            }
        };
        org.apache.ambari.server.controller.internal.Stack.ConfigProperty configProperty2 = new org.apache.ambari.server.controller.internal.Stack.ConfigProperty("hive-site", "hive.server2.authentication.kerberos.principal", " ") {
            @java.lang.Override
            java.util.Set<org.apache.ambari.server.state.PropertyDependencyInfo> getDependsOnProperties() {
                org.apache.ambari.server.state.PropertyDependencyInfo dependencyInfo = new org.apache.ambari.server.state.PropertyDependencyInfo("hive-site", "hive.server2.authentication");
                return java.util.Collections.singleton(dependencyInfo);
            }
        };
        mapOfMetadata.put("hive.server2.authentication.kerberos.keytab", configProperty1);
        mapOfMetadata.put("hive.server2.authentication.kerberos.principal", configProperty2);
        EasyMock.expect(stack.getName()).andReturn("testStack").anyTimes();
        EasyMock.expect(stack.getVersion()).andReturn("1").anyTimes();
        EasyMock.expect(stack.isMasterComponent(((java.lang.String) (EasyMock.anyObject())))).andReturn(false).anyTimes();
        java.util.Set<java.lang.String> emptySet = java.util.Collections.emptySet();
        EasyMock.expect(stack.getExcludedConfigurationTypes(EasyMock.anyObject(java.lang.String.class))).andReturn(emptySet).anyTimes();
        EasyMock.expect(stack.getServiceForConfigType("hive-site")).andThrow(new java.lang.RuntimeException("Expected Test Error")).once();
        EasyMock.expect(stack.getConfigurationPropertiesWithMetadata("HIVE", "hive-site")).andReturn(mapOfMetadata).atLeastOnce();
        org.apache.ambari.server.topology.Configuration clusterConfig = new org.apache.ambari.server.topology.Configuration(properties, java.util.Collections.emptyMap());
        java.util.Collection<java.lang.String> hgComponents = new java.util.HashSet<>();
        hgComponents.add("NAMENODE");
        java.util.List<java.lang.String> hosts = new java.util.ArrayList<>();
        hosts.add("some-hose");
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup group1 = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup(expectedHostGroupName, hgComponents, hosts);
        java.util.Collection<org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup> hostGroups = new java.util.HashSet<>();
        hostGroups.add(group1);
        org.apache.ambari.server.topology.ClusterTopology topology = createClusterTopology(bp, clusterConfig, hostGroups);
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor updater = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor(topology);
        updater.doUpdateForClusterCreate();
        org.junit.Assert.assertTrue("hive.server2.authentication.kerberos.keytab should not have been filtered, due to error condition", hiveSiteProperties.containsKey("hive.server2.authentication.kerberos.keytab"));
        org.junit.Assert.assertTrue("hive.server2.authentication.kerberos.principal should not have been filtered, due to error condition", hiveSiteProperties.containsKey("hive.server2.authentication.kerberos.principal"));
    }

    @org.junit.Test
    public void testHiveConfigClusterUpdatePropertiesFilterAuthenticationOn() throws java.lang.Exception {
        EasyMock.reset(stack);
        final java.lang.String expectedHostGroupName = "host_group_1";
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> properties = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.lang.String> hiveSiteProperties = new java.util.HashMap<>();
        properties.put("hive-site", hiveSiteProperties);
        hiveSiteProperties.put("hive.server2.authentication", "KERBEROS");
        hiveSiteProperties.put("hive.server2.authentication.kerberos.keytab", " ");
        hiveSiteProperties.put("hive.server2.authentication.kerberos.principal", " ");
        java.util.Map<java.lang.String, org.apache.ambari.server.controller.internal.Stack.ConfigProperty> mapOfMetadata = new java.util.HashMap<>();
        org.apache.ambari.server.controller.internal.Stack.ConfigProperty configProperty1 = new org.apache.ambari.server.controller.internal.Stack.ConfigProperty("hive-site", "hive.server2.authentication.kerberos.keytab", " ") {
            @java.lang.Override
            java.util.Set<org.apache.ambari.server.state.PropertyDependencyInfo> getDependsOnProperties() {
                org.apache.ambari.server.state.PropertyDependencyInfo dependencyInfo = new org.apache.ambari.server.state.PropertyDependencyInfo("hive-site", "hive.server2.authentication");
                return java.util.Collections.singleton(dependencyInfo);
            }
        };
        org.apache.ambari.server.controller.internal.Stack.ConfigProperty configProperty2 = new org.apache.ambari.server.controller.internal.Stack.ConfigProperty("hive-site", "hive.server2.authentication.kerberos.principal", " ") {
            @java.lang.Override
            java.util.Set<org.apache.ambari.server.state.PropertyDependencyInfo> getDependsOnProperties() {
                org.apache.ambari.server.state.PropertyDependencyInfo dependencyInfo = new org.apache.ambari.server.state.PropertyDependencyInfo("hive-site", "hive.server2.authentication");
                return java.util.Collections.singleton(dependencyInfo);
            }
        };
        mapOfMetadata.put("hive.server2.authentication.kerberos.keytab", configProperty1);
        mapOfMetadata.put("hive.server2.authentication.kerberos.principal", configProperty2);
        EasyMock.expect(stack.getName()).andReturn("testStack").anyTimes();
        EasyMock.expect(stack.getVersion()).andReturn("1").anyTimes();
        EasyMock.expect(stack.isMasterComponent(((java.lang.String) (EasyMock.anyObject())))).andReturn(false).anyTimes();
        java.util.Set<java.lang.String> emptySet = java.util.Collections.emptySet();
        EasyMock.expect(stack.getExcludedConfigurationTypes(EasyMock.anyObject(java.lang.String.class))).andReturn(emptySet).anyTimes();
        EasyMock.expect(stack.getServiceForConfigType("hive-site")).andReturn("HIVE").atLeastOnce();
        EasyMock.expect(stack.getConfigurationPropertiesWithMetadata("HIVE", "hive-site")).andReturn(mapOfMetadata).atLeastOnce();
        org.apache.ambari.server.topology.Configuration clusterConfig = new org.apache.ambari.server.topology.Configuration(properties, java.util.Collections.emptyMap());
        java.util.Collection<java.lang.String> hgComponents = new java.util.HashSet<>();
        hgComponents.add("NAMENODE");
        java.util.List<java.lang.String> hosts = new java.util.ArrayList<>();
        hosts.add("some-hose");
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup group1 = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup(expectedHostGroupName, hgComponents, hosts);
        java.util.Collection<org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup> hostGroups = new java.util.HashSet<>();
        hostGroups.add(group1);
        org.apache.ambari.server.topology.ClusterTopology topology = createClusterTopology(bp, clusterConfig, hostGroups);
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor updater = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor(topology);
        updater.doUpdateForClusterCreate();
        org.junit.Assert.assertTrue("hive.server2.authentication.kerberos.keytab should have been included in configuration", hiveSiteProperties.containsKey("hive.server2.authentication.kerberos.keytab"));
        org.junit.Assert.assertTrue("hive.server2.authentication.kerberos.principal should have been included in configuration", hiveSiteProperties.containsKey("hive.server2.authentication.kerberos.principal"));
    }

    @org.junit.Test
    public void testHBaseConfigClusterUpdatePropertiesFilterAuthorizationOff() throws java.lang.Exception {
        EasyMock.reset(stack);
        final java.lang.String expectedHostGroupName = "host_group_1";
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> properties = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.lang.String> hbaseSiteProperties = new java.util.HashMap<>();
        properties.put("hbase-site", hbaseSiteProperties);
        hbaseSiteProperties.put("hbase.security.authorization", "false");
        hbaseSiteProperties.put("hbase.coprocessor.regionserver.classes", " ");
        hbaseSiteProperties.put("hbase.coprocessor.master.classes", "");
        java.util.Map<java.lang.String, org.apache.ambari.server.controller.internal.Stack.ConfigProperty> mapOfMetadata = new java.util.HashMap<>();
        org.apache.ambari.server.controller.internal.Stack.ConfigProperty configProperty1 = new org.apache.ambari.server.controller.internal.Stack.ConfigProperty("hbase-site", "hbase.coprocessor.regionserver.classes", " ") {
            @java.lang.Override
            java.util.Set<org.apache.ambari.server.state.PropertyDependencyInfo> getDependsOnProperties() {
                org.apache.ambari.server.state.PropertyDependencyInfo dependencyInfo = new org.apache.ambari.server.state.PropertyDependencyInfo("hbase-site", "hbase.security.authorization");
                return java.util.Collections.singleton(dependencyInfo);
            }
        };
        org.apache.ambari.server.controller.internal.Stack.ConfigProperty configProperty2 = new org.apache.ambari.server.controller.internal.Stack.ConfigProperty("hbase-site", "hbase.coprocessor.master.classes", "") {
            @java.lang.Override
            java.util.Set<org.apache.ambari.server.state.PropertyDependencyInfo> getDependsOnProperties() {
                org.apache.ambari.server.state.PropertyDependencyInfo dependencyInfo = new org.apache.ambari.server.state.PropertyDependencyInfo("hbase-site", "hbase.security.authorization");
                return java.util.Collections.singleton(dependencyInfo);
            }
        };
        org.apache.ambari.server.controller.internal.Stack.ConfigProperty configProperty3 = new org.apache.ambari.server.controller.internal.Stack.ConfigProperty("hbase-site", "hbase.coprocessor.region.classes", "") {
            @java.lang.Override
            java.util.Set<org.apache.ambari.server.state.PropertyDependencyInfo> getDependsOnProperties() {
                org.apache.ambari.server.state.PropertyDependencyInfo dependencyInfo = new org.apache.ambari.server.state.PropertyDependencyInfo("hbase-site", "hbase.security.authorization");
                return java.util.Collections.singleton(dependencyInfo);
            }
        };
        mapOfMetadata.put("hbase.coprocessor.regionserver.classes", configProperty1);
        mapOfMetadata.put("hbase.coprocessor.master.classes", configProperty2);
        mapOfMetadata.put("hbase.coprocessor.region.classes", configProperty3);
        EasyMock.expect(stack.getName()).andReturn("testStack").anyTimes();
        EasyMock.expect(stack.getVersion()).andReturn("1").anyTimes();
        EasyMock.expect(stack.isMasterComponent(((java.lang.String) (EasyMock.anyObject())))).andReturn(false).anyTimes();
        java.util.Set<java.lang.String> emptySet = java.util.Collections.emptySet();
        EasyMock.expect(stack.getExcludedConfigurationTypes(EasyMock.anyObject(java.lang.String.class))).andReturn(emptySet).anyTimes();
        EasyMock.expect(stack.getServiceForConfigType("hbase-site")).andReturn("HBASE").atLeastOnce();
        EasyMock.expect(stack.getConfigurationPropertiesWithMetadata("HBASE", "hbase-site")).andReturn(mapOfMetadata).atLeastOnce();
        org.apache.ambari.server.topology.Configuration clusterConfig = new org.apache.ambari.server.topology.Configuration(properties, java.util.Collections.emptyMap());
        java.util.Collection<java.lang.String> hgComponents = new java.util.HashSet<>();
        hgComponents.add("NAMENODE");
        java.util.List<java.lang.String> hosts = new java.util.ArrayList<>();
        hosts.add("some-hose");
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup group1 = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup(expectedHostGroupName, hgComponents, hosts);
        java.util.Collection<org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup> hostGroups = new java.util.HashSet<>();
        hostGroups.add(group1);
        org.apache.ambari.server.topology.ClusterTopology topology = createClusterTopology(bp, clusterConfig, hostGroups);
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor updater = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor(topology);
        updater.doUpdateForClusterCreate();
        org.junit.Assert.assertFalse("hbase.coprocessor.regionserver.classes should have been filtered out of configuration", hbaseSiteProperties.containsKey("hbase.coprocessor.regionserver.classes"));
        org.junit.Assert.assertTrue("hbase.coprocessor.master.classes should not have been filtered out of configuration", hbaseSiteProperties.containsKey("hbase.coprocessor.master.classes"));
        org.junit.Assert.assertTrue("hbase.coprocessor.region.classes should not have been filtered out of configuration", hbaseSiteProperties.containsKey("hbase.coprocessor.master.classes"));
    }

    @org.junit.Test
    public void testHBaseConfigClusterUpdatePropertiesFilterAuthorizationOn() throws java.lang.Exception {
        EasyMock.reset(stack);
        final java.lang.String expectedHostGroupName = "host_group_1";
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> properties = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.lang.String> hbaseSiteProperties = new java.util.HashMap<>();
        properties.put("hbase-site", hbaseSiteProperties);
        hbaseSiteProperties.put("hbase.security.authorization", "true");
        hbaseSiteProperties.put("hbase.coprocessor.regionserver.classes", " ");
        java.util.Map<java.lang.String, org.apache.ambari.server.controller.internal.Stack.ConfigProperty> mapOfMetadata = new java.util.HashMap<>();
        org.apache.ambari.server.controller.internal.Stack.ConfigProperty configProperty1 = new org.apache.ambari.server.controller.internal.Stack.ConfigProperty("hbase-site", "hbase.coprocessor.regionserver.classes", " ") {
            @java.lang.Override
            java.util.Set<org.apache.ambari.server.state.PropertyDependencyInfo> getDependsOnProperties() {
                org.apache.ambari.server.state.PropertyDependencyInfo dependencyInfo = new org.apache.ambari.server.state.PropertyDependencyInfo("hbase-site", "hbase.security.authorization");
                return java.util.Collections.singleton(dependencyInfo);
            }
        };
        mapOfMetadata.put("hbase.coprocessor.regionserver.classes", configProperty1);
        EasyMock.expect(stack.getName()).andReturn("testStack").anyTimes();
        EasyMock.expect(stack.getVersion()).andReturn("1").anyTimes();
        EasyMock.expect(stack.isMasterComponent(((java.lang.String) (EasyMock.anyObject())))).andReturn(false).anyTimes();
        java.util.Set<java.lang.String> emptySet = java.util.Collections.emptySet();
        EasyMock.expect(stack.getExcludedConfigurationTypes(EasyMock.anyObject(java.lang.String.class))).andReturn(emptySet).anyTimes();
        EasyMock.expect(stack.getServiceForConfigType("hbase-site")).andReturn("HBASE").atLeastOnce();
        EasyMock.expect(stack.getConfigurationPropertiesWithMetadata("HBASE", "hbase-site")).andReturn(mapOfMetadata).atLeastOnce();
        org.apache.ambari.server.topology.Configuration clusterConfig = new org.apache.ambari.server.topology.Configuration(properties, java.util.Collections.emptyMap());
        java.util.Collection<java.lang.String> hgComponents = new java.util.HashSet<>();
        hgComponents.add("NAMENODE");
        java.util.List<java.lang.String> hosts = new java.util.ArrayList<>();
        hosts.add("some-hose");
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup group1 = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup(expectedHostGroupName, hgComponents, hosts);
        java.util.Collection<org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup> hostGroups = new java.util.HashSet<>();
        hostGroups.add(group1);
        org.apache.ambari.server.topology.ClusterTopology topology = createClusterTopology(bp, clusterConfig, hostGroups);
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor updater = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor(topology);
        updater.doUpdateForClusterCreate();
        org.junit.Assert.assertTrue("hbase.coprocessor.regionserver.classes should have been included in configuration", hbaseSiteProperties.containsKey("hbase.coprocessor.regionserver.classes"));
    }

    @org.junit.Test
    public void testHiveConfigClusterUpdateDefaultValue() throws java.lang.Exception {
        final java.lang.String expectedHostGroupName = "host_group_1";
        final java.lang.String expectedHostName = "c6401.ambari.apache.org";
        final java.lang.String expectedPropertyValue = "hive.metastore.local=false,hive.metastore.uris=thrift://localhost:9933,hive.metastore.sasl.enabled=false";
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> properties = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.lang.String> webHCatSiteProperties = new java.util.HashMap<>();
        properties.put("webhcat-site", webHCatSiteProperties);
        webHCatSiteProperties.put("templeton.hive.properties", expectedPropertyValue);
        org.apache.ambari.server.topology.Configuration clusterConfig = new org.apache.ambari.server.topology.Configuration(properties, java.util.Collections.emptyMap());
        java.util.Collection<java.lang.String> hgComponents = new java.util.HashSet<>();
        hgComponents.add("HIVE_METASTORE");
        java.util.List<java.lang.String> hosts = new java.util.ArrayList<>();
        hosts.add(expectedHostName);
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup group1 = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup(expectedHostGroupName, hgComponents, hosts);
        java.util.Collection<org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup> hostGroups = new java.util.HashSet<>();
        hostGroups.add(group1);
        org.apache.ambari.server.topology.ClusterTopology topology = createClusterTopology(bp, clusterConfig, hostGroups);
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor updater = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor(topology);
        updater.doUpdateForClusterCreate();
        org.junit.Assert.assertEquals("Unexpected config update for templeton.hive.properties", ("hive.metastore.local=false,hive.metastore.uris=thrift://" + expectedHostName) + ":9933,hive.metastore.sasl.enabled=false", webHCatSiteProperties.get("templeton.hive.properties"));
    }

    @org.junit.Test
    public void testAtlas() throws java.lang.Exception {
        final java.lang.String expectedHostGroupName = "host_group_1";
        final java.lang.String zkHostGroupName = "zk_host_group";
        final java.lang.String host1 = "c6401.ambari.apache.org";
        final java.lang.String host2 = "c6402.ambari.apache.org";
        final java.lang.String host3 = "c6403.ambari.apache.org";
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> properties = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.lang.String> atlasProperties = new java.util.HashMap<>();
        properties.put("application-properties", atlasProperties);
        atlasProperties.put("atlas.kafka.bootstrap.servers", "localhost:6667");
        atlasProperties.put("atlas.kafka.zookeeper.connect", "localhost:2181");
        atlasProperties.put("atlas.graph.index.search.solr.zookeeper-url", "localhost:2181/ambari-solr");
        atlasProperties.put("atlas.graph.storage.hostname", "localhost");
        atlasProperties.put("atlas.audit.hbase.zookeeper.quorum", "localhost");
        org.apache.ambari.server.topology.Configuration clusterConfig = new org.apache.ambari.server.topology.Configuration(properties, java.util.Collections.emptyMap());
        java.util.Collection<java.lang.String> hg1Components = new java.util.HashSet<>();
        hg1Components.add("KAFKA_BROKER");
        hg1Components.add("HBASE_MASTER");
        java.util.List<java.lang.String> hosts = new java.util.ArrayList<>();
        hosts.add(host1);
        hosts.add(host2);
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup group1 = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup(expectedHostGroupName, hg1Components, hosts);
        java.util.Collection<java.lang.String> zkHostGroupComponents = new java.util.HashSet<>();
        zkHostGroupComponents.add("ZOOKEEPER_SERVER");
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup group2 = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup(zkHostGroupName, zkHostGroupComponents, java.util.Collections.singletonList(host3));
        java.util.Collection<org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup> hostGroups = new java.util.HashSet<>();
        hostGroups.add(group1);
        hostGroups.add(group2);
        org.apache.ambari.server.topology.ClusterTopology topology = createClusterTopology(bp, clusterConfig, hostGroups);
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor updater = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor(topology);
        updater.doUpdateForClusterCreate();
        java.util.List<java.lang.String> hostArray = java.util.Arrays.asList(atlasProperties.get("atlas.kafka.bootstrap.servers").split(","));
        java.util.List<java.lang.String> expected = java.util.Arrays.asList("c6401.ambari.apache.org:6667", "c6402.ambari.apache.org:6667");
        org.junit.Assert.assertTrue(hostArray.containsAll(expected) && expected.containsAll(hostArray));
        hostArray = java.util.Arrays.asList(atlasProperties.get("atlas.kafka.zookeeper.connect").split(","));
        expected = java.util.Arrays.asList("c6403.ambari.apache.org:2181");
        org.junit.Assert.assertTrue(hostArray.containsAll(expected) && expected.containsAll(hostArray));
        hostArray = java.util.Arrays.asList(atlasProperties.get("atlas.graph.index.search.solr.zookeeper-url").split(","));
        expected = java.util.Arrays.asList("c6403.ambari.apache.org:2181/ambari-solr");
        org.junit.Assert.assertTrue(hostArray.containsAll(expected) && expected.containsAll(hostArray));
        hostArray = java.util.Arrays.asList(atlasProperties.get("atlas.graph.storage.hostname").split(","));
        expected = java.util.Arrays.asList("c6403.ambari.apache.org");
        org.junit.Assert.assertTrue(hostArray.containsAll(expected) && expected.containsAll(hostArray));
        hostArray = java.util.Arrays.asList(atlasProperties.get("atlas.audit.hbase.zookeeper.quorum").split(","));
        expected = java.util.Arrays.asList("c6403.ambari.apache.org");
        org.junit.Assert.assertTrue(hostArray.containsAll(expected) && expected.containsAll(hostArray));
    }

    @org.junit.Test
    public void testHiveConfigClusterUpdateExportedHostGroupValue() throws java.lang.Exception {
        final java.lang.String expectedHostGroupName = "host_group_1";
        final java.lang.String expectedHostName = "c6401.ambari.apache.org";
        final java.lang.String expectedPropertyValue = "hive.metastore.local=false,hive.metastore.uris=thrift://%HOSTGROUP::host_group_1%:9083,hive.metastore.sasl.enabled=false,hive.metastore.execute.setugi=true";
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> properties = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.lang.String> webHCatSiteProperties = new java.util.HashMap<>();
        properties.put("webhcat-site", webHCatSiteProperties);
        webHCatSiteProperties.put("templeton.hive.properties", expectedPropertyValue);
        org.apache.ambari.server.topology.Configuration clusterConfig = new org.apache.ambari.server.topology.Configuration(properties, java.util.Collections.emptyMap());
        java.util.Collection<java.lang.String> hgComponents = new java.util.HashSet<>();
        hgComponents.add("HIVE_METASTORE");
        java.util.List<java.lang.String> hosts = new java.util.ArrayList<>();
        hosts.add(expectedHostName);
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup group1 = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup(expectedHostGroupName, hgComponents, hosts);
        java.util.Collection<org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup> hostGroups = new java.util.HashSet<>();
        hostGroups.add(group1);
        org.apache.ambari.server.topology.ClusterTopology topology = createClusterTopology(bp, clusterConfig, hostGroups);
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor updater = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor(topology);
        updater.doUpdateForClusterCreate();
        org.junit.Assert.assertEquals("Unexpected config update for templeton.hive.properties", ("hive.metastore.local=false,hive.metastore.uris=thrift://" + expectedHostName) + ":9083,hive.metastore.sasl.enabled=false,hive.metastore.execute.setugi=true", webHCatSiteProperties.get("templeton.hive.properties"));
    }

    @org.junit.Test
    public void testStormAndKafkaConfigClusterUpdateWithoutGangliaServer() throws java.lang.Exception {
        final java.lang.String expectedHostGroupName = "host_group_1";
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> properties = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.lang.String> stormSiteProperties = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.lang.String> kafkaBrokerProperties = new java.util.HashMap<>();
        properties.put("storm-site", stormSiteProperties);
        properties.put("kafka-broker", kafkaBrokerProperties);
        stormSiteProperties.put("worker.childopts", "localhost");
        stormSiteProperties.put("supervisor.childopts", "localhost");
        stormSiteProperties.put("nimbus.childopts", "localhost");
        kafkaBrokerProperties.put("kafka.ganglia.metrics.host", "localhost");
        org.apache.ambari.server.topology.Configuration clusterConfig = new org.apache.ambari.server.topology.Configuration(properties, java.util.Collections.emptyMap());
        java.util.Collection<java.lang.String> hgComponents = new java.util.HashSet<>();
        hgComponents.add("HIVE_METASTORE");
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup group1 = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup(expectedHostGroupName, hgComponents, java.util.Collections.singleton("testserver"));
        java.util.Collection<org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup> hostGroups = new java.util.HashSet<>();
        hostGroups.add(group1);
        EasyMock.expect(stack.getCardinality("GANGLIA_SERVER")).andReturn(new org.apache.ambari.server.topology.Cardinality("1")).anyTimes();
        org.apache.ambari.server.topology.ClusterTopology topology = createClusterTopology(bp, clusterConfig, hostGroups);
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor updater = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor(topology);
        updater.doUpdateForClusterCreate();
        org.junit.Assert.assertEquals("worker startup settings not properly handled by cluster create", "localhost", stormSiteProperties.get("worker.childopts"));
        org.junit.Assert.assertEquals("supervisor startup settings not properly handled by cluster create", "localhost", stormSiteProperties.get("supervisor.childopts"));
        org.junit.Assert.assertEquals("nimbus startup settings not properly handled by cluster create", "localhost", stormSiteProperties.get("nimbus.childopts"));
        org.junit.Assert.assertEquals("Kafka ganglia host property not properly handled by cluster create", "localhost", kafkaBrokerProperties.get("kafka.ganglia.metrics.host"));
    }

    @org.junit.Test
    public void testStormandKafkaConfigClusterUpdateWithGangliaServer() throws java.lang.Exception {
        final java.lang.String expectedHostName = "c6401.apache.ambari.org";
        final java.lang.String expectedHostGroupName = "host_group_1";
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> properties = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.lang.String> stormSiteProperties = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.lang.String> kafkaBrokerProperties = new java.util.HashMap<>();
        properties.put("storm-site", stormSiteProperties);
        properties.put("kafka-broker", kafkaBrokerProperties);
        stormSiteProperties.put("worker.childopts", "localhost");
        stormSiteProperties.put("supervisor.childopts", "localhost");
        stormSiteProperties.put("nimbus.childopts", "localhost");
        kafkaBrokerProperties.put("kafka.ganglia.metrics.host", "localhost");
        org.apache.ambari.server.topology.Configuration clusterConfig = new org.apache.ambari.server.topology.Configuration(properties, java.util.Collections.emptyMap());
        java.util.Collection<java.lang.String> hgComponents = new java.util.HashSet<>();
        hgComponents.add("GANGLIA_SERVER");
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup group1 = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup(expectedHostGroupName, hgComponents, java.util.Collections.singleton(expectedHostName));
        java.util.Collection<org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup> hostGroups = new java.util.HashSet<>();
        hostGroups.add(group1);
        org.apache.ambari.server.topology.ClusterTopology topology = createClusterTopology(bp, clusterConfig, hostGroups);
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor updater = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor(topology);
        updater.doUpdateForClusterCreate();
        org.junit.Assert.assertEquals("worker startup settings not properly handled by cluster create", expectedHostName, stormSiteProperties.get("worker.childopts"));
        org.junit.Assert.assertEquals("supervisor startup settings not properly handled by cluster create", expectedHostName, stormSiteProperties.get("supervisor.childopts"));
        org.junit.Assert.assertEquals("nimbus startup settings not properly handled by cluster create", expectedHostName, stormSiteProperties.get("nimbus.childopts"));
        org.junit.Assert.assertEquals("Kafka ganglia host property not properly handled by cluster create", expectedHostName, kafkaBrokerProperties.get("kafka.ganglia.metrics.host"));
    }

    @org.junit.Test
    public void testDoUpdateForClusterWithNameNodeHAEnabled() throws java.lang.Exception {
        final java.lang.String expectedNameService = "mynameservice";
        final java.lang.String expectedHostName = "c6401.apache.ambari.org";
        final java.lang.String expectedHostNameTwo = "server-two";
        final java.lang.String expectedPortNum = "808080";
        final java.lang.String expectedNodeOne = "nn1";
        final java.lang.String expectedNodeTwo = "nn2";
        final java.lang.String expectedHostGroupName = "host_group_1";
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> properties = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.lang.String> hdfsSiteProperties = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.lang.String> hbaseSiteProperties = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.lang.String> hadoopEnvProperties = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.lang.String> coreSiteProperties = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.lang.String> accumuloSiteProperties = new java.util.HashMap<>();
        properties.put("hdfs-site", hdfsSiteProperties);
        properties.put("hadoop-env", hadoopEnvProperties);
        properties.put("core-site", coreSiteProperties);
        properties.put("hbase-site", hbaseSiteProperties);
        properties.put("accumulo-site", accumuloSiteProperties);
        hdfsSiteProperties.put("dfs.nameservices", expectedNameService);
        hdfsSiteProperties.put("dfs.ha.namenodes.mynameservice", (expectedNodeOne + ", ") + expectedNodeTwo);
        hdfsSiteProperties.put((("dfs.namenode.https-address." + expectedNameService) + ".") + expectedNodeOne, org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.createExportedAddress(expectedPortNum, expectedHostGroupName));
        hdfsSiteProperties.put((("dfs.namenode.https-address." + expectedNameService) + ".") + expectedNodeTwo, org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.createExportedAddress(expectedPortNum, expectedHostGroupName));
        hdfsSiteProperties.put((("dfs.namenode.http-address." + expectedNameService) + ".") + expectedNodeOne, org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.createExportedAddress(expectedPortNum, expectedHostGroupName));
        hdfsSiteProperties.put((("dfs.namenode.http-address." + expectedNameService) + ".") + expectedNodeTwo, org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.createExportedAddress(expectedPortNum, expectedHostGroupName));
        hdfsSiteProperties.put((("dfs.namenode.rpc-address." + expectedNameService) + ".") + expectedNodeOne, org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.createExportedAddress(expectedPortNum, expectedHostGroupName));
        hdfsSiteProperties.put((("dfs.namenode.rpc-address." + expectedNameService) + ".") + expectedNodeTwo, org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.createExportedAddress(expectedPortNum, expectedHostGroupName));
        hdfsSiteProperties.put("dfs.secondary.http.address", "localhost:8080");
        hdfsSiteProperties.put("dfs.namenode.secondary.http-address", "localhost:8080");
        hdfsSiteProperties.put("dfs.namenode.http-address", "localhost:8080");
        hdfsSiteProperties.put("dfs.namenode.https-address", "localhost:8081");
        hdfsSiteProperties.put("dfs.namenode.rpc-address", "localhost:8082");
        coreSiteProperties.put("fs.defaultFS", "hdfs://" + expectedNameService);
        hbaseSiteProperties.put("hbase.rootdir", ("hdfs://" + expectedNameService) + "/hbase/test/root/dir");
        accumuloSiteProperties.put("instance.volumes", ("hdfs://" + expectedNameService) + "/accumulo/test/instance/volumes");
        org.apache.ambari.server.topology.Configuration clusterConfig = new org.apache.ambari.server.topology.Configuration(properties, java.util.Collections.emptyMap());
        java.util.Collection<java.lang.String> hgComponents = new java.util.HashSet<>();
        hgComponents.add("NAMENODE");
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup group1 = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup(expectedHostGroupName, hgComponents, java.util.Collections.singleton(expectedHostName));
        java.util.Collection<java.lang.String> hgComponents2 = new java.util.HashSet<>();
        hgComponents2.add("NAMENODE");
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup group2 = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup("host-group-2", hgComponents2, java.util.Collections.singleton(expectedHostNameTwo));
        java.util.Collection<org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup> hostGroups = new java.util.ArrayList<>();
        hostGroups.add(group1);
        hostGroups.add(group2);
        EasyMock.expect(stack.getCardinality("NAMENODE")).andReturn(new org.apache.ambari.server.topology.Cardinality("1-2")).anyTimes();
        EasyMock.expect(stack.getCardinality("SECONDARY_NAMENODE")).andReturn(new org.apache.ambari.server.topology.Cardinality("1")).anyTimes();
        org.apache.ambari.server.topology.ClusterTopology topology = createClusterTopology(bp, clusterConfig, hostGroups);
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor updater = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor(topology);
        java.util.Set<java.lang.String> updatedConfigTypes = updater.doUpdateForClusterCreate();
        org.junit.Assert.assertEquals("dfs.internal.nameservices wasn't added", expectedNameService, hdfsSiteProperties.get("dfs.internal.nameservices"));
        org.junit.Assert.assertEquals("HTTPS address HA property not properly exported", (expectedHostName + ":") + expectedPortNum, hdfsSiteProperties.get((("dfs.namenode.https-address." + expectedNameService) + ".") + expectedNodeOne));
        org.junit.Assert.assertEquals("HTTPS address HA property not properly exported", (expectedHostName + ":") + expectedPortNum, hdfsSiteProperties.get((("dfs.namenode.https-address." + expectedNameService) + ".") + expectedNodeTwo));
        org.junit.Assert.assertEquals("HTTPS address HA property not properly exported", (expectedHostName + ":") + expectedPortNum, hdfsSiteProperties.get((("dfs.namenode.http-address." + expectedNameService) + ".") + expectedNodeOne));
        org.junit.Assert.assertEquals("HTTPS address HA property not properly exported", (expectedHostName + ":") + expectedPortNum, hdfsSiteProperties.get((("dfs.namenode.http-address." + expectedNameService) + ".") + expectedNodeTwo));
        org.junit.Assert.assertEquals("HTTPS address HA property not properly exported", (expectedHostName + ":") + expectedPortNum, hdfsSiteProperties.get((("dfs.namenode.rpc-address." + expectedNameService) + ".") + expectedNodeOne));
        org.junit.Assert.assertEquals("HTTPS address HA property not properly exported", (expectedHostName + ":") + expectedPortNum, hdfsSiteProperties.get((("dfs.namenode.rpc-address." + expectedNameService) + ".") + expectedNodeTwo));
        java.util.Map<java.lang.String, java.lang.String> clusterEnv = clusterConfig.getProperties().get("cluster-env");
        java.lang.String initialActiveHost = clusterEnv.get("dfs_ha_initial_namenode_active");
        java.lang.String expectedStandbyHost = null;
        if (initialActiveHost.equals(expectedHostName)) {
            expectedStandbyHost = expectedHostNameTwo;
        } else if (initialActiveHost.equals(expectedHostNameTwo)) {
            expectedStandbyHost = expectedHostName;
        } else {
            org.junit.Assert.fail("Active Namenode hostname was not set correctly");
        }
        org.junit.Assert.assertEquals("Standby Namenode hostname was not set correctly", expectedStandbyHost, clusterEnv.get("dfs_ha_initial_namenode_standby"));
        org.junit.Assert.assertEquals("fs.defaultFS should not be modified by cluster update when NameNode HA is enabled.", "hdfs://" + expectedNameService, coreSiteProperties.get("fs.defaultFS"));
        org.junit.Assert.assertEquals("hbase.rootdir should not be modified by cluster update when NameNode HA is enabled.", ("hdfs://" + expectedNameService) + "/hbase/test/root/dir", hbaseSiteProperties.get("hbase.rootdir"));
        org.junit.Assert.assertEquals("instance.volumes should not be modified by cluster update when NameNode HA is enabled.", ("hdfs://" + expectedNameService) + "/accumulo/test/instance/volumes", accumuloSiteProperties.get("instance.volumes"));
        org.junit.Assert.assertFalse("dfs.namenode.http-address should have been filtered out of this HA configuration", hdfsSiteProperties.containsKey("dfs.namenode.http-address"));
        org.junit.Assert.assertFalse("dfs.namenode.https-address should have been filtered out of this HA configuration", hdfsSiteProperties.containsKey("dfs.namenode.https-address"));
        org.junit.Assert.assertFalse("dfs.namenode.rpc-address should have been filtered out of this HA configuration", hdfsSiteProperties.containsKey("dfs.namenode.rpc-address"));
        org.junit.Assert.assertEquals(com.google.common.collect.ImmutableSet.of("cluster-env", "hdfs-site"), updatedConfigTypes);
    }

    @org.junit.Test(expected = org.apache.ambari.server.controller.internal.ConfigurationTopologyException.class)
    public void testDoUpdateForClusterWithNameNodeHAEnabled_insufficientNameNodes() throws java.lang.Exception {
        final java.lang.String expectedNameService = "mynameservice";
        final java.lang.String expectedHostName = "c6401.apache.ambari.org";
        final java.lang.String expectedHostNameTwo = "server-two";
        final java.lang.String expectedPortNum = "808080";
        final java.lang.String expectedNodeOne = "nn1";
        final java.lang.String expectedNodeTwo = "nn2";
        final java.lang.String expectedHostGroupName = "host_group_1";
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> properties = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.lang.String> hdfsSiteProperties = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.lang.String> hbaseSiteProperties = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.lang.String> hadoopEnvProperties = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.lang.String> coreSiteProperties = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.lang.String> accumuloSiteProperties = new java.util.HashMap<>();
        properties.put("hdfs-site", hdfsSiteProperties);
        properties.put("hadoop-env", hadoopEnvProperties);
        properties.put("core-site", coreSiteProperties);
        properties.put("hbase-site", hbaseSiteProperties);
        properties.put("accumulo-site", accumuloSiteProperties);
        hdfsSiteProperties.put("dfs.nameservices", expectedNameService);
        hdfsSiteProperties.put("dfs.ha.namenodes.mynameservice", (expectedNodeOne + ", ") + expectedNodeTwo);
        hdfsSiteProperties.put((("dfs.namenode.https-address." + expectedNameService) + ".") + expectedNodeOne, org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.createExportedAddress(expectedPortNum, expectedHostGroupName));
        hdfsSiteProperties.put((("dfs.namenode.https-address." + expectedNameService) + ".") + expectedNodeTwo, org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.createExportedAddress(expectedPortNum, expectedHostGroupName));
        hdfsSiteProperties.put((("dfs.namenode.http-address." + expectedNameService) + ".") + expectedNodeOne, org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.createExportedAddress(expectedPortNum, expectedHostGroupName));
        hdfsSiteProperties.put((("dfs.namenode.http-address." + expectedNameService) + ".") + expectedNodeTwo, org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.createExportedAddress(expectedPortNum, expectedHostGroupName));
        hdfsSiteProperties.put((("dfs.namenode.rpc-address." + expectedNameService) + ".") + expectedNodeOne, org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.createExportedAddress(expectedPortNum, expectedHostGroupName));
        hdfsSiteProperties.put((("dfs.namenode.rpc-address." + expectedNameService) + ".") + expectedNodeTwo, org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.createExportedAddress(expectedPortNum, expectedHostGroupName));
        hdfsSiteProperties.put("dfs.secondary.http.address", "localhost:8080");
        hdfsSiteProperties.put("dfs.namenode.secondary.http-address", "localhost:8080");
        hdfsSiteProperties.put("dfs.namenode.http-address", "localhost:8080");
        hdfsSiteProperties.put("dfs.namenode.https-address", "localhost:8081");
        hdfsSiteProperties.put("dfs.namenode.rpc-address", "localhost:8082");
        coreSiteProperties.put("fs.defaultFS", "hdfs://" + expectedNameService);
        hbaseSiteProperties.put("hbase.rootdir", ("hdfs://" + expectedNameService) + "/hbase/test/root/dir");
        accumuloSiteProperties.put("instance.volumes", ("hdfs://" + expectedNameService) + "/accumulo/test/instance/volumes");
        org.apache.ambari.server.topology.Configuration clusterConfig = new org.apache.ambari.server.topology.Configuration(properties, java.util.Collections.emptyMap());
        java.util.Collection<java.lang.String> hgComponents = com.google.common.collect.Sets.newHashSet("NAMENODE");
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup group1 = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup(expectedHostGroupName, hgComponents, java.util.Collections.singleton(expectedHostName));
        java.util.Collection<java.lang.String> hgComponents2 = com.google.common.collect.Sets.newHashSet("DATANODE");
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup group2 = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup("host-group-2", hgComponents2, java.util.Collections.singleton(expectedHostNameTwo));
        java.util.Collection<org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup> hostGroups = new java.util.ArrayList<>();
        hostGroups.add(group1);
        hostGroups.add(group2);
        EasyMock.expect(stack.getCardinality("NAMENODE")).andReturn(new org.apache.ambari.server.topology.Cardinality("1-2")).anyTimes();
        EasyMock.expect(stack.getCardinality("SECONDARY_NAMENODE")).andReturn(new org.apache.ambari.server.topology.Cardinality("1")).anyTimes();
        org.apache.ambari.server.topology.ClusterTopology topology = createClusterTopology(bp, clusterConfig, hostGroups);
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor updater = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor(topology);
        updater.doUpdateForClusterCreate();
    }

    @org.junit.Test
    public void testDoUpdateForClusterWithNameNodeHAEnabled_externalNameNodes() throws java.lang.Exception {
        final java.lang.String expectedNameService = "mynameservice";
        final java.lang.String expectedHostName = "c6401.apache.ambari.org";
        final java.lang.String expectedHostNameTwo = "server-two";
        final java.lang.String expectedPortNum = "808080";
        final java.lang.String expectedNodeOne = "nn1";
        final java.lang.String expectedNodeTwo = "nn2";
        final java.lang.String expectedHostGroupName = "host_group_1";
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> properties = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.lang.String> hdfsSiteProperties = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.lang.String> hbaseSiteProperties = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.lang.String> hadoopEnvProperties = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.lang.String> coreSiteProperties = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.lang.String> accumuloSiteProperties = new java.util.HashMap<>();
        properties.put("hdfs-site", hdfsSiteProperties);
        properties.put("hadoop-env", hadoopEnvProperties);
        properties.put("core-site", coreSiteProperties);
        properties.put("hbase-site", hbaseSiteProperties);
        properties.put("accumulo-site", accumuloSiteProperties);
        hdfsSiteProperties.put("dfs.nameservices", expectedNameService);
        hdfsSiteProperties.put("dfs.ha.namenodes.mynameservice", (expectedNodeOne + ", ") + expectedNodeTwo);
        hdfsSiteProperties.put((("dfs.namenode.rpc-address." + expectedNameService) + ".") + expectedNodeOne, "externalhost1:" + expectedPortNum);
        hdfsSiteProperties.put((("dfs.namenode.rpc-address." + expectedNameService) + ".") + expectedNodeTwo, "externalhost2:" + expectedPortNum);
        hdfsSiteProperties.put("dfs.secondary.http.address", "localhost:8080");
        hdfsSiteProperties.put("dfs.namenode.secondary.http-address", "localhost:8080");
        hdfsSiteProperties.put("dfs.namenode.http-address", "localhost:8080");
        hdfsSiteProperties.put("dfs.namenode.https-address", "localhost:8081");
        hdfsSiteProperties.put("dfs.namenode.rpc-address", "localhost:8082");
        coreSiteProperties.put("fs.defaultFS", "hdfs://" + expectedNameService);
        hbaseSiteProperties.put("hbase.rootdir", ("hdfs://" + expectedNameService) + "/hbase/test/root/dir");
        accumuloSiteProperties.put("instance.volumes", ("hdfs://" + expectedNameService) + "/accumulo/test/instance/volumes");
        org.apache.ambari.server.topology.Configuration clusterConfig = new org.apache.ambari.server.topology.Configuration(properties, java.util.Collections.emptyMap());
        java.util.Collection<java.lang.String> hgComponents = com.google.common.collect.Sets.newHashSet("JOURNALNODE");
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup group1 = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup(expectedHostGroupName, hgComponents, java.util.Collections.singleton(expectedHostName));
        java.util.Collection<java.lang.String> hgComponents2 = com.google.common.collect.Sets.newHashSet("DATANODE");
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup group2 = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup("host-group-2", hgComponents2, java.util.Collections.singleton(expectedHostNameTwo));
        java.util.Collection<org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup> hostGroups = new java.util.ArrayList<>();
        hostGroups.add(group1);
        hostGroups.add(group2);
        EasyMock.expect(stack.getCardinality("NAMENODE")).andReturn(new org.apache.ambari.server.topology.Cardinality("1-2")).anyTimes();
        EasyMock.expect(stack.getCardinality("SECONDARY_NAMENODE")).andReturn(new org.apache.ambari.server.topology.Cardinality("1")).anyTimes();
        org.apache.ambari.server.topology.ClusterTopology topology = createClusterTopology(bp, clusterConfig, hostGroups);
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor updater = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor(topology);
        java.util.Set<java.lang.String> updatedConfigTypes = updater.doUpdateForClusterCreate();
        org.junit.Assert.assertFalse("dfs.internal.nameservices shouldn't be set", hdfsSiteProperties.containsKey("dfs.internal.nameservices"));
        java.util.Map<java.lang.String, java.lang.String> clusterEnv = clusterConfig.getProperties().get("cluster-env");
        org.junit.Assert.assertFalse("dfs_ha_initial_namenode_active shouldn't be set", clusterEnv.containsKey("dfs_ha_initial_namenode_active"));
        org.junit.Assert.assertFalse("dfs_ha_initial_namenode_standby shouldn't be set", clusterEnv.containsKey("dfs_ha_initial_namenode_standby"));
        org.junit.Assert.assertEquals("fs.defaultFS should not be modified by cluster update when NameNode HA is enabled.", "hdfs://" + expectedNameService, coreSiteProperties.get("fs.defaultFS"));
        org.junit.Assert.assertEquals("hbase.rootdir should not be modified by cluster update when NameNode HA is enabled.", ("hdfs://" + expectedNameService) + "/hbase/test/root/dir", hbaseSiteProperties.get("hbase.rootdir"));
        org.junit.Assert.assertEquals("instance.volumes should not be modified by cluster update when NameNode HA is enabled.", ("hdfs://" + expectedNameService) + "/accumulo/test/instance/volumes", accumuloSiteProperties.get("instance.volumes"));
        org.junit.Assert.assertFalse("dfs.namenode.http-address should have been filtered out of this HA configuration", hdfsSiteProperties.containsKey("dfs.namenode.http-address"));
        org.junit.Assert.assertFalse("dfs.namenode.https-address should have been filtered out of this HA configuration", hdfsSiteProperties.containsKey("dfs.namenode.https-address"));
        org.junit.Assert.assertFalse("dfs.namenode.rpc-address should have been filtered out of this HA configuration", hdfsSiteProperties.containsKey("dfs.namenode.rpc-address"));
        org.junit.Assert.assertEquals(com.google.common.collect.ImmutableSet.of("cluster-env", "hdfs-site"), updatedConfigTypes);
    }

    @org.junit.Test(expected = org.apache.ambari.server.controller.internal.ConfigurationTopologyException.class)
    public void testDoUpdateForClusterWithNameNodeHAEnabled_externalNameNodes_invalid() throws java.lang.Exception {
        final java.lang.String expectedNameService = "mynameservice";
        final java.lang.String expectedHostName = "c6401.apache.ambari.org";
        final java.lang.String expectedHostNameTwo = "server-two";
        final java.lang.String expectedPortNum = "808080";
        final java.lang.String expectedNodeOne = "nn1";
        final java.lang.String expectedNodeTwo = "nn2";
        final java.lang.String expectedHostGroupName = "host_group_1";
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> properties = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.lang.String> hdfsSiteProperties = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.lang.String> hbaseSiteProperties = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.lang.String> hadoopEnvProperties = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.lang.String> coreSiteProperties = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.lang.String> accumuloSiteProperties = new java.util.HashMap<>();
        properties.put("hdfs-site", hdfsSiteProperties);
        properties.put("hadoop-env", hadoopEnvProperties);
        properties.put("core-site", coreSiteProperties);
        properties.put("hbase-site", hbaseSiteProperties);
        properties.put("accumulo-site", accumuloSiteProperties);
        hdfsSiteProperties.put("dfs.nameservices", expectedNameService);
        hdfsSiteProperties.put("dfs.ha.namenodes.mynameservice", (expectedNodeOne + ", ") + expectedNodeTwo);
        hdfsSiteProperties.put("dfs.secondary.http.address", "localhost:8080");
        hdfsSiteProperties.put("dfs.namenode.secondary.http-address", "localhost:8080");
        hdfsSiteProperties.put("dfs.namenode.http-address", "localhost:8080");
        hdfsSiteProperties.put("dfs.namenode.https-address", "localhost:8081");
        hdfsSiteProperties.put("dfs.namenode.rpc-address", "localhost:8082");
        coreSiteProperties.put("fs.defaultFS", "hdfs://" + expectedNameService);
        hbaseSiteProperties.put("hbase.rootdir", ("hdfs://" + expectedNameService) + "/hbase/test/root/dir");
        accumuloSiteProperties.put("instance.volumes", ("hdfs://" + expectedNameService) + "/accumulo/test/instance/volumes");
        org.apache.ambari.server.topology.Configuration clusterConfig = new org.apache.ambari.server.topology.Configuration(properties, java.util.Collections.emptyMap());
        java.util.Collection<java.lang.String> hgComponents = com.google.common.collect.Sets.newHashSet("JOURNALNODE");
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup group1 = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup(expectedHostGroupName, hgComponents, java.util.Collections.singleton(expectedHostName));
        java.util.Collection<java.lang.String> hgComponents2 = com.google.common.collect.Sets.newHashSet("DATANODE");
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup group2 = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup("host-group-2", hgComponents2, java.util.Collections.singleton(expectedHostNameTwo));
        java.util.Collection<org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup> hostGroups = new java.util.ArrayList<>();
        hostGroups.add(group1);
        hostGroups.add(group2);
        EasyMock.expect(stack.getCardinality("NAMENODE")).andReturn(new org.apache.ambari.server.topology.Cardinality("1-2")).anyTimes();
        EasyMock.expect(stack.getCardinality("SECONDARY_NAMENODE")).andReturn(new org.apache.ambari.server.topology.Cardinality("1")).anyTimes();
        org.apache.ambari.server.topology.ClusterTopology topology = createClusterTopology(bp, clusterConfig, hostGroups);
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor updater = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor(topology);
        java.util.Set<java.lang.String> updatedConfigTypes = updater.doUpdateForClusterCreate();
    }

    @org.junit.Test
    public void testDoUpdateForClusterWithNameNodeHAEnabledThreeNameNodes() throws java.lang.Exception {
        final java.lang.String expectedNameService = "mynameservice";
        final java.lang.String expectedHostName = "c6401.apache.ambari.org";
        final java.lang.String expectedHostNameTwo = "server-two";
        final java.lang.String expectedHostNameThree = "server-three";
        final java.lang.String expectedPortNum = "808080";
        final java.lang.String expectedNodeOne = "nn1";
        final java.lang.String expectedNodeTwo = "nn2";
        final java.lang.String expectedHostGroupName = "host_group_1";
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> properties = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.lang.String> hdfsSiteProperties = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.lang.String> hbaseSiteProperties = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.lang.String> hadoopEnvProperties = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.lang.String> coreSiteProperties = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.lang.String> accumuloSiteProperties = new java.util.HashMap<>();
        properties.put("hdfs-site", hdfsSiteProperties);
        properties.put("hadoop-env", hadoopEnvProperties);
        properties.put("core-site", coreSiteProperties);
        properties.put("hbase-site", hbaseSiteProperties);
        properties.put("accumulo-site", accumuloSiteProperties);
        hdfsSiteProperties.put("dfs.nameservices", expectedNameService);
        hdfsSiteProperties.put("dfs.ha.namenodes.mynameservice", (expectedNodeOne + ", ") + expectedNodeTwo);
        hdfsSiteProperties.put((("dfs.namenode.https-address." + expectedNameService) + ".") + expectedNodeOne, org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.createExportedAddress(expectedPortNum, expectedHostGroupName));
        hdfsSiteProperties.put((("dfs.namenode.https-address." + expectedNameService) + ".") + expectedNodeTwo, org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.createExportedAddress(expectedPortNum, expectedHostGroupName));
        hdfsSiteProperties.put((("dfs.namenode.http-address." + expectedNameService) + ".") + expectedNodeOne, org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.createExportedAddress(expectedPortNum, expectedHostGroupName));
        hdfsSiteProperties.put((("dfs.namenode.http-address." + expectedNameService) + ".") + expectedNodeTwo, org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.createExportedAddress(expectedPortNum, expectedHostGroupName));
        hdfsSiteProperties.put((("dfs.namenode.rpc-address." + expectedNameService) + ".") + expectedNodeOne, org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.createExportedAddress(expectedPortNum, expectedHostGroupName));
        hdfsSiteProperties.put((("dfs.namenode.rpc-address." + expectedNameService) + ".") + expectedNodeTwo, org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.createExportedAddress(expectedPortNum, expectedHostGroupName));
        hdfsSiteProperties.put("dfs.secondary.http.address", "localhost:8080");
        hdfsSiteProperties.put("dfs.namenode.secondary.http-address", "localhost:8080");
        hdfsSiteProperties.put("dfs.namenode.http-address", "localhost:8080");
        hdfsSiteProperties.put("dfs.namenode.https-address", "localhost:8081");
        hdfsSiteProperties.put("dfs.namenode.rpc-address", "localhost:8082");
        coreSiteProperties.put("fs.defaultFS", "hdfs://" + expectedNameService);
        hbaseSiteProperties.put("hbase.rootdir", ("hdfs://" + expectedNameService) + "/hbase/test/root/dir");
        accumuloSiteProperties.put("instance.volumes", ("hdfs://" + expectedNameService) + "/accumulo/test/instance/volumes");
        org.apache.ambari.server.topology.Configuration clusterConfig = new org.apache.ambari.server.topology.Configuration(properties, java.util.Collections.emptyMap());
        java.util.Collection<java.lang.String> hgComponents = new java.util.HashSet<>();
        hgComponents.add("NAMENODE");
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup group1 = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup(expectedHostGroupName, hgComponents, java.util.Collections.singleton(expectedHostName));
        java.util.Collection<java.lang.String> hgComponents2 = new java.util.HashSet<>();
        hgComponents2.add("NAMENODE");
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup group2 = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup("host-group-2", hgComponents2, java.util.Collections.singleton(expectedHostNameTwo));
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup group3 = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup("host-group-3", java.util.Collections.singleton("NAMENODE"), java.util.Collections.singleton(expectedHostNameThree));
        java.util.Collection<org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup> hostGroups = new java.util.ArrayList<>();
        hostGroups.add(group1);
        hostGroups.add(group2);
        hostGroups.add(group3);
        EasyMock.expect(stack.getCardinality("NAMENODE")).andReturn(new org.apache.ambari.server.topology.Cardinality("1-2")).anyTimes();
        EasyMock.expect(stack.getCardinality("SECONDARY_NAMENODE")).andReturn(new org.apache.ambari.server.topology.Cardinality("1")).anyTimes();
        org.apache.ambari.server.topology.ClusterTopology topology = createClusterTopology(bp, clusterConfig, hostGroups);
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor updater = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor(topology);
        java.util.Set<java.lang.String> updatedConfigTypes = updater.doUpdateForClusterCreate();
        org.junit.Assert.assertEquals("dfs.internal.nameservices wasn't added", expectedNameService, hdfsSiteProperties.get("dfs.internal.nameservices"));
        org.junit.Assert.assertEquals("HTTPS address HA property not properly exported", (expectedHostName + ":") + expectedPortNum, hdfsSiteProperties.get((("dfs.namenode.https-address." + expectedNameService) + ".") + expectedNodeOne));
        org.junit.Assert.assertEquals("HTTPS address HA property not properly exported", (expectedHostName + ":") + expectedPortNum, hdfsSiteProperties.get((("dfs.namenode.https-address." + expectedNameService) + ".") + expectedNodeTwo));
        org.junit.Assert.assertEquals("HTTPS address HA property not properly exported", (expectedHostName + ":") + expectedPortNum, hdfsSiteProperties.get((("dfs.namenode.http-address." + expectedNameService) + ".") + expectedNodeOne));
        org.junit.Assert.assertEquals("HTTPS address HA property not properly exported", (expectedHostName + ":") + expectedPortNum, hdfsSiteProperties.get((("dfs.namenode.http-address." + expectedNameService) + ".") + expectedNodeTwo));
        org.junit.Assert.assertEquals("HTTPS address HA property not properly exported", (expectedHostName + ":") + expectedPortNum, hdfsSiteProperties.get((("dfs.namenode.rpc-address." + expectedNameService) + ".") + expectedNodeOne));
        org.junit.Assert.assertEquals("HTTPS address HA property not properly exported", (expectedHostName + ":") + expectedPortNum, hdfsSiteProperties.get((("dfs.namenode.rpc-address." + expectedNameService) + ".") + expectedNodeTwo));
        org.junit.Assert.assertEquals("fs.defaultFS should not be modified by cluster update when NameNode HA is enabled.", "hdfs://" + expectedNameService, coreSiteProperties.get("fs.defaultFS"));
        org.junit.Assert.assertEquals("hbase.rootdir should not be modified by cluster update when NameNode HA is enabled.", ("hdfs://" + expectedNameService) + "/hbase/test/root/dir", hbaseSiteProperties.get("hbase.rootdir"));
        org.junit.Assert.assertEquals("instance.volumes should not be modified by cluster update when NameNode HA is enabled.", ("hdfs://" + expectedNameService) + "/accumulo/test/instance/volumes", accumuloSiteProperties.get("instance.volumes"));
        org.junit.Assert.assertFalse("dfs.namenode.http-address should have been filtered out of this HA configuration", hdfsSiteProperties.containsKey("dfs.namenode.http-address"));
        org.junit.Assert.assertFalse("dfs.namenode.https-address should have been filtered out of this HA configuration", hdfsSiteProperties.containsKey("dfs.namenode.https-address"));
        org.junit.Assert.assertFalse("dfs.namenode.rpc-address should have been filtered out of this HA configuration", hdfsSiteProperties.containsKey("dfs.namenode.rpc-address"));
        org.junit.Assert.assertEquals(com.google.common.collect.ImmutableSet.of("cluster-env", "hdfs-site"), updatedConfigTypes);
    }

    @org.junit.Test
    public void testDoUpdateForClusterWithNameNodeFederationEnabled() throws java.lang.Exception {
        final java.lang.String expectedNameService = "mynameservice";
        final java.lang.String expectedNameServiceTwo = "mynameservicetwo";
        final java.lang.String expectedHostName = "c6401.apache.ambari.org";
        final java.lang.String expectedHostNameTwo = "c6402.apache.ambari.org";
        final java.lang.String expectedHostNameThree = "c6403.apache.ambari.org";
        final java.lang.String expectedHostNameFour = "c6404.apache.ambari.org";
        final java.lang.String expectedPortNum = "808080";
        final java.lang.String expectedNodeOne = "nn1";
        final java.lang.String expectedNodeTwo = "nn2";
        final java.lang.String expectedNodeThree = "nn3";
        final java.lang.String expectedNodeFour = "nn4";
        final java.lang.String expectedHostGroupName = "host_group_1";
        final java.lang.String expectedHostGroupNameTwo = "host_group_2";
        final java.lang.String expectedHostGroupNameThree = "host-group-3";
        final java.lang.String expectedHostGroupNameFour = "host-group-4";
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> properties = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.lang.String> hdfsSiteProperties = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.lang.String> hbaseSiteProperties = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.lang.String> hadoopEnvProperties = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.lang.String> coreSiteProperties = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.lang.String> accumuloSiteProperties = new java.util.HashMap<>();
        properties.put("hdfs-site", hdfsSiteProperties);
        properties.put("hadoop-env", hadoopEnvProperties);
        properties.put("core-site", coreSiteProperties);
        properties.put("hbase-site", hbaseSiteProperties);
        properties.put("accumulo-site", accumuloSiteProperties);
        hdfsSiteProperties.put("dfs.nameservices", (expectedNameService + ",") + expectedNameServiceTwo);
        hdfsSiteProperties.put("dfs.ha.namenodes.mynameservice", (expectedNodeOne + ", ") + expectedNodeTwo);
        hdfsSiteProperties.put("dfs.ha.namenodes." + expectedNameServiceTwo, (expectedNodeThree + ",") + expectedNodeFour);
        hdfsSiteProperties.put(("dfs.namenode.shared.edits.dir" + ".") + expectedNameService, ((("qjournal://" + org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.createExportedAddress(expectedPortNum, expectedHostGroupName)) + ";") + org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.createExportedAddress(expectedPortNum, expectedHostGroupNameTwo)) + "/ns1");
        hdfsSiteProperties.put(("dfs.namenode.shared.edits.dir" + ".") + expectedNameServiceTwo, ((("qjournal://" + org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.createExportedAddress(expectedPortNum, expectedHostGroupName)) + ";") + org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.createExportedAddress(expectedPortNum, expectedHostGroupNameTwo)) + "/ns2");
        hdfsSiteProperties.put((("dfs.namenode.https-address." + expectedNameService) + ".") + expectedNodeOne, org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.createExportedAddress(expectedPortNum, expectedHostGroupName));
        hdfsSiteProperties.put((("dfs.namenode.https-address." + expectedNameService) + ".") + expectedNodeTwo, org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.createExportedAddress(expectedPortNum, expectedHostGroupNameTwo));
        hdfsSiteProperties.put((("dfs.namenode.http-address." + expectedNameService) + ".") + expectedNodeOne, org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.createExportedAddress(expectedPortNum, expectedHostGroupName));
        hdfsSiteProperties.put((("dfs.namenode.http-address." + expectedNameService) + ".") + expectedNodeTwo, org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.createExportedAddress(expectedPortNum, expectedHostGroupNameTwo));
        hdfsSiteProperties.put((("dfs.namenode.rpc-address." + expectedNameService) + ".") + expectedNodeOne, org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.createExportedAddress(expectedPortNum, expectedHostGroupName));
        hdfsSiteProperties.put((("dfs.namenode.rpc-address." + expectedNameService) + ".") + expectedNodeTwo, org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.createExportedAddress(expectedPortNum, expectedHostGroupNameTwo));
        hdfsSiteProperties.put((("dfs.namenode.servicerpc-address." + expectedNameService) + ".") + expectedNodeOne, org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.createExportedAddress(expectedPortNum, expectedHostGroupName));
        hdfsSiteProperties.put((("dfs.namenode.servicerpc-address." + expectedNameService) + ".") + expectedNodeTwo, org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.createExportedAddress(expectedPortNum, expectedHostGroupNameTwo));
        hdfsSiteProperties.put((("dfs.namenode.https-address." + expectedNameServiceTwo) + ".") + expectedNodeThree, org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.createExportedAddress(expectedPortNum, expectedHostGroupNameThree));
        hdfsSiteProperties.put((("dfs.namenode.https-address." + expectedNameServiceTwo) + ".") + expectedNodeFour, org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.createExportedAddress(expectedPortNum, expectedHostGroupNameFour));
        hdfsSiteProperties.put((("dfs.namenode.http-address." + expectedNameServiceTwo) + ".") + expectedNodeThree, org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.createExportedAddress(expectedPortNum, expectedHostGroupNameThree));
        hdfsSiteProperties.put((("dfs.namenode.http-address." + expectedNameServiceTwo) + ".") + expectedNodeFour, org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.createExportedAddress(expectedPortNum, expectedHostGroupNameFour));
        hdfsSiteProperties.put((("dfs.namenode.rpc-address." + expectedNameServiceTwo) + ".") + expectedNodeThree, org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.createExportedAddress(expectedPortNum, expectedHostGroupNameThree));
        hdfsSiteProperties.put((("dfs.namenode.rpc-address." + expectedNameServiceTwo) + ".") + expectedNodeFour, org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.createExportedAddress(expectedPortNum, expectedHostGroupNameFour));
        hdfsSiteProperties.put((("dfs.namenode.servicerpc-address." + expectedNameServiceTwo) + ".") + expectedNodeThree, org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.createExportedAddress(expectedPortNum, expectedHostGroupNameThree));
        hdfsSiteProperties.put((("dfs.namenode.servicerpc-address." + expectedNameServiceTwo) + ".") + expectedNodeFour, org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.createExportedAddress(expectedPortNum, expectedHostGroupNameFour));
        hdfsSiteProperties.put("dfs.secondary.http.address", "localhost:8080");
        hdfsSiteProperties.put("dfs.namenode.secondary.http-address", "localhost:8080");
        hdfsSiteProperties.put("dfs.namenode.http-address", "localhost:8080");
        hdfsSiteProperties.put("dfs.namenode.https-address", "localhost:8081");
        hdfsSiteProperties.put("dfs.namenode.rpc-address", "localhost:8082");
        coreSiteProperties.put("fs.defaultFS", "hdfs://" + expectedNameService);
        hbaseSiteProperties.put("hbase.rootdir", ("hdfs://" + expectedNameService) + "/hbase/test/root/dir");
        accumuloSiteProperties.put("instance.volumes", ("hdfs://" + expectedNameService) + "/accumulo/test/instance/volumes");
        org.apache.ambari.server.topology.Configuration clusterConfig = new org.apache.ambari.server.topology.Configuration(properties, java.util.Collections.emptyMap());
        java.util.Collection<java.lang.String> hgComponents = new java.util.HashSet<>();
        hgComponents.add("NAMENODE");
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup group1 = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup(expectedHostGroupName, hgComponents, java.util.Collections.singleton(expectedHostName));
        java.util.Collection<java.lang.String> hgComponents2 = new java.util.HashSet<>();
        hgComponents2.add("NAMENODE");
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup group2 = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup(expectedHostGroupNameTwo, hgComponents2, java.util.Collections.singleton(expectedHostNameTwo));
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup group3 = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup(expectedHostGroupNameThree, java.util.Collections.singleton("NAMENODE"), java.util.Collections.singleton(expectedHostNameThree));
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup group4 = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup(expectedHostGroupNameFour, java.util.Collections.singleton("NAMENODE"), java.util.Collections.singleton(expectedHostNameFour));
        java.util.Collection<org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup> hostGroups = new java.util.ArrayList<>();
        hostGroups.add(group1);
        hostGroups.add(group2);
        hostGroups.add(group3);
        hostGroups.add(group4);
        EasyMock.expect(stack.getCardinality("NAMENODE")).andReturn(new org.apache.ambari.server.topology.Cardinality("1-2")).anyTimes();
        EasyMock.expect(stack.getCardinality("SECONDARY_NAMENODE")).andReturn(new org.apache.ambari.server.topology.Cardinality("1")).anyTimes();
        org.apache.ambari.server.topology.ClusterTopology topology = createClusterTopology(bp, clusterConfig, hostGroups);
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor updater = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor(topology);
        java.util.Set<java.lang.String> updatedConfigTypes = updater.doUpdateForClusterCreate();
        org.junit.Assert.assertEquals("dfs.internal.nameservices wasn't added", (expectedNameService + ",") + expectedNameServiceTwo, hdfsSiteProperties.get("dfs.internal.nameservices"));
        org.junit.Assert.assertEquals("HTTPS address HA property not properly exported", (expectedHostName + ":") + expectedPortNum, hdfsSiteProperties.get((("dfs.namenode.https-address." + expectedNameService) + ".") + expectedNodeOne));
        org.junit.Assert.assertEquals("HTTPS address HA property not properly exported", (expectedHostNameTwo + ":") + expectedPortNum, hdfsSiteProperties.get((("dfs.namenode.https-address." + expectedNameService) + ".") + expectedNodeTwo));
        org.junit.Assert.assertEquals("HTTPS address HA property not properly exported", (expectedHostName + ":") + expectedPortNum, hdfsSiteProperties.get((("dfs.namenode.http-address." + expectedNameService) + ".") + expectedNodeOne));
        org.junit.Assert.assertEquals("HTTPS address HA property not properly exported", (expectedHostNameTwo + ":") + expectedPortNum, hdfsSiteProperties.get((("dfs.namenode.http-address." + expectedNameService) + ".") + expectedNodeTwo));
        org.junit.Assert.assertEquals("HTTPS address HA property not properly exported", (expectedHostName + ":") + expectedPortNum, hdfsSiteProperties.get((("dfs.namenode.rpc-address." + expectedNameService) + ".") + expectedNodeOne));
        org.junit.Assert.assertEquals("HTTPS address HA property not properly exported", (expectedHostNameTwo + ":") + expectedPortNum, hdfsSiteProperties.get((("dfs.namenode.rpc-address." + expectedNameService) + ".") + expectedNodeTwo));
        org.junit.Assert.assertEquals("servicerpc-address property not handled properly", (expectedHostName + ":") + expectedPortNum, hdfsSiteProperties.get((("dfs.namenode.servicerpc-address." + expectedNameService) + ".") + expectedNodeOne));
        org.junit.Assert.assertEquals("servicerpc-address property not handled properly", (expectedHostNameTwo + ":") + expectedPortNum, hdfsSiteProperties.get((("dfs.namenode.servicerpc-address." + expectedNameService) + ".") + expectedNodeTwo));
        org.junit.Assert.assertEquals("fs.defaultFS should not be modified by cluster update when NameNode HA is enabled.", "hdfs://" + expectedNameService, coreSiteProperties.get("fs.defaultFS"));
        org.junit.Assert.assertEquals("hbase.rootdir should not be modified by cluster update when NameNode HA is enabled.", ("hdfs://" + expectedNameService) + "/hbase/test/root/dir", hbaseSiteProperties.get("hbase.rootdir"));
        org.junit.Assert.assertEquals("instance.volumes should not be modified by cluster update when NameNode HA is enabled.", ("hdfs://" + expectedNameService) + "/accumulo/test/instance/volumes", accumuloSiteProperties.get("instance.volumes"));
        org.junit.Assert.assertFalse("dfs.namenode.http-address should have been filtered out of this HA configuration", hdfsSiteProperties.containsKey("dfs.namenode.http-address"));
        org.junit.Assert.assertFalse("dfs.namenode.https-address should have been filtered out of this HA configuration", hdfsSiteProperties.containsKey("dfs.namenode.https-address"));
        org.junit.Assert.assertFalse("dfs.namenode.rpc-address should have been filtered out of this HA configuration", hdfsSiteProperties.containsKey("dfs.namenode.rpc-address"));
        org.junit.Assert.assertEquals("HDFS HA shared edits directory property not properly updated for cluster create.", ((("qjournal://" + org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.createHostAddress(expectedHostName, expectedPortNum)) + ";") + org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.createHostAddress(expectedHostNameTwo, expectedPortNum)) + "/ns1", hdfsSiteProperties.get(("dfs.namenode.shared.edits.dir" + ".") + expectedNameService));
        org.junit.Assert.assertEquals("HDFS HA shared edits directory property not properly updated for cluster create.", ((("qjournal://" + org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.createHostAddress(expectedHostName, expectedPortNum)) + ";") + org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.createHostAddress(expectedHostNameTwo, expectedPortNum)) + "/ns2", hdfsSiteProperties.get(("dfs.namenode.shared.edits.dir" + ".") + expectedNameServiceTwo));
        org.junit.Assert.assertEquals(com.google.common.collect.ImmutableSet.of("cluster-env", "hdfs-site"), updatedConfigTypes);
        java.util.Map<java.lang.String, java.lang.String> clusterEnv = clusterConfig.getProperties().get("cluster-env");
        org.junit.Assert.assertFalse("Single-node nameservice config should not have been set", clusterEnv.containsKey("dfs_ha_initial_namenode_active"));
        org.junit.Assert.assertFalse("Single-node nameservice config should not have been set", clusterEnv.containsKey("dfs_ha_initial_namenode_standby"));
        org.junit.Assert.assertTrue("Expected active set not found in hadoop-env", clusterEnv.containsKey("dfs_ha_initial_namenode_active_set"));
        org.junit.Assert.assertTrue("Expected standby set not found in hadoop-env", clusterEnv.containsKey("dfs_ha_initial_namenode_standby_set"));
        org.junit.Assert.assertTrue("Expected clusterId not found in hadoop-env", clusterEnv.containsKey("dfs_ha_initial_cluster_id"));
        org.junit.Assert.assertEquals("Expected clusterId was not set to expected value", "clusterName", clusterEnv.get("dfs_ha_initial_cluster_id"));
        java.lang.String[] activeHostNames = clusterEnv.get("dfs_ha_initial_namenode_active_set").split(",");
        org.junit.Assert.assertEquals("NameNode active set did not contain the expected number of hosts", 2, activeHostNames.length);
        java.util.Set<java.lang.String> setOfActiveHostNames = new java.util.HashSet<java.lang.String>(java.util.Arrays.asList(activeHostNames));
        org.junit.Assert.assertTrue("Expected host name not found in the active map", setOfActiveHostNames.contains(expectedHostName));
        org.junit.Assert.assertTrue("Expected host name not found in the active map", setOfActiveHostNames.contains(expectedHostNameThree));
        java.lang.String[] standbyHostNames = clusterEnv.get("dfs_ha_initial_namenode_standby_set").split(",");
        org.junit.Assert.assertEquals("NameNode standby set did not contain the expected number of hosts", 2, standbyHostNames.length);
        java.util.Set<java.lang.String> setOfStandbyHostNames = new java.util.HashSet<java.lang.String>(java.util.Arrays.asList(standbyHostNames));
        org.junit.Assert.assertTrue("Expected host name not found in the standby map", setOfStandbyHostNames.contains(expectedHostNameTwo));
        org.junit.Assert.assertTrue("Expected host name not found in the standby map", setOfStandbyHostNames.contains(expectedHostNameFour));
    }

    @org.junit.Test(expected = org.apache.ambari.server.controller.internal.ConfigurationTopologyException.class)
    public void testDoUpdateForClusterWithNameNodeFederationEnabledErrorClusterNameNotFound() throws java.lang.Exception {
        final java.lang.String expectedNameService = "mynameservice";
        final java.lang.String expectedNameServiceTwo = "mynameservicetwo";
        final java.lang.String expectedHostName = "c6401.apache.ambari.org";
        final java.lang.String expectedHostNameTwo = "c6402.apache.ambari.org";
        final java.lang.String expectedHostNameThree = "c6403.apache.ambari.org";
        final java.lang.String expectedHostNameFour = "c6404.apache.ambari.org";
        final java.lang.String expectedPortNum = "808080";
        final java.lang.String expectedNodeOne = "nn1";
        final java.lang.String expectedNodeTwo = "nn2";
        final java.lang.String expectedNodeThree = "nn3";
        final java.lang.String expectedNodeFour = "nn4";
        final java.lang.String expectedHostGroupName = "host_group_1";
        final java.lang.String expectedHostGroupNameTwo = "host_group_2";
        final java.lang.String expectedHostGroupNameThree = "host-group-3";
        final java.lang.String expectedHostGroupNameFour = "host-group-4";
        org.easymock.EasyMockSupport mockSupport = new org.easymock.EasyMockSupport();
        org.apache.ambari.server.topology.AmbariContext mockAmbariContext = mockSupport.createMock(org.apache.ambari.server.topology.AmbariContext.class);
        EasyMock.expect(mockAmbariContext.getClusterName(1)).andReturn(null).anyTimes();
        mockSupport.replayAll();
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> properties = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.lang.String> hdfsSiteProperties = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.lang.String> hbaseSiteProperties = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.lang.String> hadoopEnvProperties = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.lang.String> coreSiteProperties = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.lang.String> accumuloSiteProperties = new java.util.HashMap<>();
        properties.put("hdfs-site", hdfsSiteProperties);
        properties.put("hadoop-env", hadoopEnvProperties);
        properties.put("core-site", coreSiteProperties);
        properties.put("hbase-site", hbaseSiteProperties);
        properties.put("accumulo-site", accumuloSiteProperties);
        hdfsSiteProperties.put("dfs.nameservices", (expectedNameService + ",") + expectedNameServiceTwo);
        hdfsSiteProperties.put("dfs.ha.namenodes.mynameservice", (expectedNodeOne + ", ") + expectedNodeTwo);
        hdfsSiteProperties.put("dfs.ha.namenodes." + expectedNameServiceTwo, (expectedNodeThree + ",") + expectedNodeFour);
        hdfsSiteProperties.put(("dfs.namenode.shared.edits.dir" + ".") + expectedNameService, ((("qjournal://" + org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.createExportedAddress(expectedPortNum, expectedHostGroupName)) + ";") + org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.createExportedAddress(expectedPortNum, expectedHostGroupNameTwo)) + "/ns1");
        hdfsSiteProperties.put(("dfs.namenode.shared.edits.dir" + ".") + expectedNameServiceTwo, ((("qjournal://" + org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.createExportedAddress(expectedPortNum, expectedHostGroupName)) + ";") + org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.createExportedAddress(expectedPortNum, expectedHostGroupNameTwo)) + "/ns2");
        hdfsSiteProperties.put((("dfs.namenode.https-address." + expectedNameService) + ".") + expectedNodeOne, org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.createExportedAddress(expectedPortNum, expectedHostGroupName));
        hdfsSiteProperties.put((("dfs.namenode.https-address." + expectedNameService) + ".") + expectedNodeTwo, org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.createExportedAddress(expectedPortNum, expectedHostGroupNameTwo));
        hdfsSiteProperties.put((("dfs.namenode.http-address." + expectedNameService) + ".") + expectedNodeOne, org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.createExportedAddress(expectedPortNum, expectedHostGroupName));
        hdfsSiteProperties.put((("dfs.namenode.http-address." + expectedNameService) + ".") + expectedNodeTwo, org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.createExportedAddress(expectedPortNum, expectedHostGroupNameTwo));
        hdfsSiteProperties.put((("dfs.namenode.rpc-address." + expectedNameService) + ".") + expectedNodeOne, org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.createExportedAddress(expectedPortNum, expectedHostGroupName));
        hdfsSiteProperties.put((("dfs.namenode.rpc-address." + expectedNameService) + ".") + expectedNodeTwo, org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.createExportedAddress(expectedPortNum, expectedHostGroupNameTwo));
        hdfsSiteProperties.put((("dfs.namenode.servicerpc-address." + expectedNameService) + ".") + expectedNodeOne, org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.createExportedAddress(expectedPortNum, expectedHostGroupName));
        hdfsSiteProperties.put((("dfs.namenode.servicerpc-address." + expectedNameService) + ".") + expectedNodeTwo, org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.createExportedAddress(expectedPortNum, expectedHostGroupNameTwo));
        hdfsSiteProperties.put((("dfs.namenode.https-address." + expectedNameServiceTwo) + ".") + expectedNodeThree, org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.createExportedAddress(expectedPortNum, expectedHostGroupNameThree));
        hdfsSiteProperties.put((("dfs.namenode.https-address." + expectedNameServiceTwo) + ".") + expectedNodeFour, org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.createExportedAddress(expectedPortNum, expectedHostGroupNameFour));
        hdfsSiteProperties.put((("dfs.namenode.http-address." + expectedNameServiceTwo) + ".") + expectedNodeThree, org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.createExportedAddress(expectedPortNum, expectedHostGroupNameThree));
        hdfsSiteProperties.put((("dfs.namenode.http-address." + expectedNameServiceTwo) + ".") + expectedNodeFour, org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.createExportedAddress(expectedPortNum, expectedHostGroupNameFour));
        hdfsSiteProperties.put((("dfs.namenode.rpc-address." + expectedNameServiceTwo) + ".") + expectedNodeThree, org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.createExportedAddress(expectedPortNum, expectedHostGroupNameThree));
        hdfsSiteProperties.put((("dfs.namenode.rpc-address." + expectedNameServiceTwo) + ".") + expectedNodeFour, org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.createExportedAddress(expectedPortNum, expectedHostGroupNameFour));
        hdfsSiteProperties.put((("dfs.namenode.servicerpc-address." + expectedNameServiceTwo) + ".") + expectedNodeThree, org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.createExportedAddress(expectedPortNum, expectedHostGroupNameThree));
        hdfsSiteProperties.put((("dfs.namenode.servicerpc-address." + expectedNameServiceTwo) + ".") + expectedNodeFour, org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.createExportedAddress(expectedPortNum, expectedHostGroupNameFour));
        hdfsSiteProperties.put("dfs.secondary.http.address", "localhost:8080");
        hdfsSiteProperties.put("dfs.namenode.secondary.http-address", "localhost:8080");
        hdfsSiteProperties.put("dfs.namenode.http-address", "localhost:8080");
        hdfsSiteProperties.put("dfs.namenode.https-address", "localhost:8081");
        hdfsSiteProperties.put("dfs.namenode.rpc-address", "localhost:8082");
        coreSiteProperties.put("fs.defaultFS", "hdfs://" + expectedNameService);
        hbaseSiteProperties.put("hbase.rootdir", ("hdfs://" + expectedNameService) + "/hbase/test/root/dir");
        accumuloSiteProperties.put("instance.volumes", ("hdfs://" + expectedNameService) + "/accumulo/test/instance/volumes");
        org.apache.ambari.server.topology.Configuration clusterConfig = new org.apache.ambari.server.topology.Configuration(properties, java.util.Collections.emptyMap());
        java.util.Collection<java.lang.String> hgComponents = new java.util.HashSet<>();
        hgComponents.add("NAMENODE");
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup group1 = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup(expectedHostGroupName, hgComponents, java.util.Collections.singleton(expectedHostName));
        java.util.Collection<java.lang.String> hgComponents2 = new java.util.HashSet<>();
        hgComponents2.add("NAMENODE");
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup group2 = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup(expectedHostGroupNameTwo, hgComponents2, java.util.Collections.singleton(expectedHostNameTwo));
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup group3 = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup(expectedHostGroupNameThree, java.util.Collections.singleton("NAMENODE"), java.util.Collections.singleton(expectedHostNameThree));
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup group4 = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup(expectedHostGroupNameFour, java.util.Collections.singleton("NAMENODE"), java.util.Collections.singleton(expectedHostNameFour));
        java.util.Collection<org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup> hostGroups = new java.util.ArrayList<>();
        hostGroups.add(group1);
        hostGroups.add(group2);
        hostGroups.add(group3);
        hostGroups.add(group4);
        EasyMock.expect(stack.getCardinality("NAMENODE")).andReturn(new org.apache.ambari.server.topology.Cardinality("1-2")).anyTimes();
        EasyMock.expect(stack.getCardinality("SECONDARY_NAMENODE")).andReturn(new org.apache.ambari.server.topology.Cardinality("1")).anyTimes();
        org.apache.ambari.server.topology.ClusterTopology topology = createClusterTopology(bp, clusterConfig, hostGroups, mockAmbariContext);
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor updater = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor(topology);
        updater.doUpdateForClusterCreate();
    }

    @org.junit.Test
    public void testDoUpdateForClusterWithNameNodeFederationEnabledWithCustomizedActiveStandbyHostSets() throws java.lang.Exception {
        final java.lang.String expectedNameService = "mynameservice";
        final java.lang.String expectedNameServiceTwo = "mynameservicetwo";
        final java.lang.String expectedHostName = "c6401.apache.ambari.org";
        final java.lang.String expectedHostNameTwo = "c6402.apache.ambari.org";
        final java.lang.String expectedHostNameThree = "c6403.apache.ambari.org";
        final java.lang.String expectedHostNameFour = "c6404.apache.ambari.org";
        final java.lang.String expectedPortNum = "808080";
        final java.lang.String expectedNodeOne = "nn1";
        final java.lang.String expectedNodeTwo = "nn2";
        final java.lang.String expectedNodeThree = "nn3";
        final java.lang.String expectedNodeFour = "nn4";
        final java.lang.String expectedHostGroupName = "host_group_1";
        final java.lang.String expectedHostGroupNameTwo = "host_group_2";
        final java.lang.String expectedHostGroupNameThree = "host-group-3";
        final java.lang.String expectedHostGroupNameFour = "host-group-4";
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> properties = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.lang.String> hdfsSiteProperties = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.lang.String> hbaseSiteProperties = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.lang.String> hadoopEnvProperties = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.lang.String> coreSiteProperties = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.lang.String> accumuloSiteProperties = new java.util.HashMap<>();
        properties.put("hdfs-site", hdfsSiteProperties);
        properties.put("hadoop-env", hadoopEnvProperties);
        properties.put("core-site", coreSiteProperties);
        properties.put("hbase-site", hbaseSiteProperties);
        properties.put("accumulo-site", accumuloSiteProperties);
        hadoopEnvProperties.put("dfs_ha_initial_namenode_active_set", "test-server-five,test-server-six");
        hadoopEnvProperties.put("dfs_ha_initial_namenode_standby_set", "test-server-seven,test-server-eight");
        hadoopEnvProperties.put("dfs_ha_initial_cluster_id", "my-custom-cluster-name");
        hdfsSiteProperties.put("dfs.nameservices", (expectedNameService + ",") + expectedNameServiceTwo);
        hdfsSiteProperties.put("dfs.ha.namenodes.mynameservice", (expectedNodeOne + ", ") + expectedNodeTwo);
        hdfsSiteProperties.put("dfs.ha.namenodes." + expectedNameServiceTwo, (expectedNodeThree + ",") + expectedNodeFour);
        hdfsSiteProperties.put((("dfs.namenode.https-address." + expectedNameService) + ".") + expectedNodeOne, org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.createExportedAddress(expectedPortNum, expectedHostGroupName));
        hdfsSiteProperties.put((("dfs.namenode.https-address." + expectedNameService) + ".") + expectedNodeTwo, org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.createExportedAddress(expectedPortNum, expectedHostGroupNameTwo));
        hdfsSiteProperties.put((("dfs.namenode.http-address." + expectedNameService) + ".") + expectedNodeOne, org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.createExportedAddress(expectedPortNum, expectedHostGroupName));
        hdfsSiteProperties.put((("dfs.namenode.http-address." + expectedNameService) + ".") + expectedNodeTwo, org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.createExportedAddress(expectedPortNum, expectedHostGroupNameTwo));
        hdfsSiteProperties.put((("dfs.namenode.rpc-address." + expectedNameService) + ".") + expectedNodeOne, org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.createExportedAddress(expectedPortNum, expectedHostGroupName));
        hdfsSiteProperties.put((("dfs.namenode.rpc-address." + expectedNameService) + ".") + expectedNodeTwo, org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.createExportedAddress(expectedPortNum, expectedHostGroupNameTwo));
        hdfsSiteProperties.put((("dfs.namenode.https-address." + expectedNameServiceTwo) + ".") + expectedNodeThree, org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.createExportedAddress(expectedPortNum, expectedHostGroupNameThree));
        hdfsSiteProperties.put((("dfs.namenode.https-address." + expectedNameServiceTwo) + ".") + expectedNodeFour, org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.createExportedAddress(expectedPortNum, expectedHostGroupNameFour));
        hdfsSiteProperties.put((("dfs.namenode.http-address." + expectedNameServiceTwo) + ".") + expectedNodeThree, org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.createExportedAddress(expectedPortNum, expectedHostGroupNameThree));
        hdfsSiteProperties.put((("dfs.namenode.http-address." + expectedNameServiceTwo) + ".") + expectedNodeFour, org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.createExportedAddress(expectedPortNum, expectedHostGroupNameFour));
        hdfsSiteProperties.put((("dfs.namenode.rpc-address." + expectedNameServiceTwo) + ".") + expectedNodeThree, org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.createExportedAddress(expectedPortNum, expectedHostGroupNameThree));
        hdfsSiteProperties.put((("dfs.namenode.rpc-address." + expectedNameServiceTwo) + ".") + expectedNodeFour, org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.createExportedAddress(expectedPortNum, expectedHostGroupNameFour));
        hdfsSiteProperties.put("dfs.secondary.http.address", "localhost:8080");
        hdfsSiteProperties.put("dfs.namenode.secondary.http-address", "localhost:8080");
        hdfsSiteProperties.put("dfs.namenode.http-address", "localhost:8080");
        hdfsSiteProperties.put("dfs.namenode.https-address", "localhost:8081");
        hdfsSiteProperties.put("dfs.namenode.rpc-address", "localhost:8082");
        coreSiteProperties.put("fs.defaultFS", "hdfs://" + expectedNameService);
        hbaseSiteProperties.put("hbase.rootdir", ("hdfs://" + expectedNameService) + "/hbase/test/root/dir");
        accumuloSiteProperties.put("instance.volumes", ("hdfs://" + expectedNameService) + "/accumulo/test/instance/volumes");
        org.apache.ambari.server.topology.Configuration clusterConfig = new org.apache.ambari.server.topology.Configuration(properties, java.util.Collections.emptyMap());
        java.util.Collection<java.lang.String> hgComponents = new java.util.HashSet<>();
        hgComponents.add("NAMENODE");
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup group1 = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup(expectedHostGroupName, hgComponents, java.util.Collections.singleton(expectedHostName));
        java.util.Collection<java.lang.String> hgComponents2 = new java.util.HashSet<>();
        hgComponents2.add("NAMENODE");
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup group2 = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup(expectedHostGroupNameTwo, hgComponents2, java.util.Collections.singleton(expectedHostNameTwo));
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup group3 = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup(expectedHostGroupNameThree, java.util.Collections.singleton("NAMENODE"), java.util.Collections.singleton(expectedHostNameThree));
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup group4 = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup(expectedHostGroupNameFour, java.util.Collections.singleton("NAMENODE"), java.util.Collections.singleton(expectedHostNameFour));
        java.util.Collection<org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup> hostGroups = new java.util.ArrayList<>();
        hostGroups.add(group1);
        hostGroups.add(group2);
        hostGroups.add(group3);
        hostGroups.add(group4);
        EasyMock.expect(stack.getCardinality("NAMENODE")).andReturn(new org.apache.ambari.server.topology.Cardinality("1-2")).anyTimes();
        EasyMock.expect(stack.getCardinality("SECONDARY_NAMENODE")).andReturn(new org.apache.ambari.server.topology.Cardinality("1")).anyTimes();
        org.apache.ambari.server.topology.ClusterTopology topology = createClusterTopology(bp, clusterConfig, hostGroups);
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor updater = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor(topology);
        java.util.Set<java.lang.String> updatedConfigTypes = updater.doUpdateForClusterCreate();
        org.junit.Assert.assertEquals("dfs.internal.nameservices wasn't added", (expectedNameService + ",") + expectedNameServiceTwo, hdfsSiteProperties.get("dfs.internal.nameservices"));
        org.junit.Assert.assertEquals("HTTPS address HA property not properly exported", (expectedHostName + ":") + expectedPortNum, hdfsSiteProperties.get((("dfs.namenode.https-address." + expectedNameService) + ".") + expectedNodeOne));
        org.junit.Assert.assertEquals("HTTPS address HA property not properly exported", (expectedHostNameTwo + ":") + expectedPortNum, hdfsSiteProperties.get((("dfs.namenode.https-address." + expectedNameService) + ".") + expectedNodeTwo));
        org.junit.Assert.assertEquals("HTTPS address HA property not properly exported", (expectedHostName + ":") + expectedPortNum, hdfsSiteProperties.get((("dfs.namenode.http-address." + expectedNameService) + ".") + expectedNodeOne));
        org.junit.Assert.assertEquals("HTTPS address HA property not properly exported", (expectedHostNameTwo + ":") + expectedPortNum, hdfsSiteProperties.get((("dfs.namenode.http-address." + expectedNameService) + ".") + expectedNodeTwo));
        org.junit.Assert.assertEquals("HTTPS address HA property not properly exported", (expectedHostName + ":") + expectedPortNum, hdfsSiteProperties.get((("dfs.namenode.rpc-address." + expectedNameService) + ".") + expectedNodeOne));
        org.junit.Assert.assertEquals("HTTPS address HA property not properly exported", (expectedHostNameTwo + ":") + expectedPortNum, hdfsSiteProperties.get((("dfs.namenode.rpc-address." + expectedNameService) + ".") + expectedNodeTwo));
        org.junit.Assert.assertEquals("fs.defaultFS should not be modified by cluster update when NameNode HA is enabled.", "hdfs://" + expectedNameService, coreSiteProperties.get("fs.defaultFS"));
        org.junit.Assert.assertEquals("hbase.rootdir should not be modified by cluster update when NameNode HA is enabled.", ("hdfs://" + expectedNameService) + "/hbase/test/root/dir", hbaseSiteProperties.get("hbase.rootdir"));
        org.junit.Assert.assertEquals("instance.volumes should not be modified by cluster update when NameNode HA is enabled.", ("hdfs://" + expectedNameService) + "/accumulo/test/instance/volumes", accumuloSiteProperties.get("instance.volumes"));
        org.junit.Assert.assertFalse("dfs.namenode.http-address should have been filtered out of this HA configuration", hdfsSiteProperties.containsKey("dfs.namenode.http-address"));
        org.junit.Assert.assertFalse("dfs.namenode.https-address should have been filtered out of this HA configuration", hdfsSiteProperties.containsKey("dfs.namenode.https-address"));
        org.junit.Assert.assertFalse("dfs.namenode.rpc-address should have been filtered out of this HA configuration", hdfsSiteProperties.containsKey("dfs.namenode.rpc-address"));
        org.junit.Assert.assertEquals(com.google.common.collect.ImmutableSet.of("cluster-env", "hadoop-env", "hdfs-site"), updatedConfigTypes);
        java.util.Map<java.lang.String, java.lang.String> clusterEnv = clusterConfig.getProperties().get("cluster-env");
        org.junit.Assert.assertFalse("Single-node nameservice config should not have been set", clusterEnv.containsKey("dfs_ha_initial_namenode_active"));
        org.junit.Assert.assertFalse("Single-node nameservice config should not have been set", clusterEnv.containsKey("dfs_ha_initial_namenode_standby"));
        org.junit.Assert.assertTrue("Expected active set not found in hadoop-env", clusterEnv.containsKey("dfs_ha_initial_namenode_active_set"));
        org.junit.Assert.assertTrue("Expected standby set not found in hadoop-env", clusterEnv.containsKey("dfs_ha_initial_namenode_standby_set"));
        org.junit.Assert.assertTrue("Expected clusterId not found in hadoop-env", clusterEnv.containsKey("dfs_ha_initial_cluster_id"));
        org.junit.Assert.assertEquals("Expected clusterId was not set to expected value", "my-custom-cluster-name", clusterEnv.get("dfs_ha_initial_cluster_id"));
        java.lang.String[] activeHostNames = clusterEnv.get("dfs_ha_initial_namenode_active_set").split(",");
        org.junit.Assert.assertEquals("NameNode active set did not contain the expected number of hosts", 2, activeHostNames.length);
        java.util.Set<java.lang.String> setOfActiveHostNames = new java.util.HashSet<java.lang.String>(java.util.Arrays.asList(activeHostNames));
        org.junit.Assert.assertTrue("Expected host name not found in the active map", setOfActiveHostNames.contains("test-server-five"));
        org.junit.Assert.assertTrue("Expected host name not found in the active map", setOfActiveHostNames.contains("test-server-six"));
        java.lang.String[] standbyHostNames = clusterEnv.get("dfs_ha_initial_namenode_standby_set").split(",");
        org.junit.Assert.assertEquals("NameNode standby set did not contain the expected number of hosts", 2, standbyHostNames.length);
        java.util.Set<java.lang.String> setOfStandbyHostNames = new java.util.HashSet<java.lang.String>(java.util.Arrays.asList(standbyHostNames));
        org.junit.Assert.assertTrue("Expected host name not found in the standby map", setOfStandbyHostNames.contains("test-server-seven"));
        org.junit.Assert.assertTrue("Expected host name not found in the standby map", setOfStandbyHostNames.contains("test-server-eight"));
    }

    @org.junit.Test(expected = org.apache.ambari.server.controller.internal.ConfigurationTopologyException.class)
    public void testDoUpdateForClusterWithNameNodeFederationEnabledErrorRPCAddressNotSpecified() throws java.lang.Exception {
        final java.lang.String expectedNameService = "mynameservice";
        final java.lang.String expectedNameServiceTwo = "mynameservicetwo";
        final java.lang.String expectedHostName = "c6401.apache.ambari.org";
        final java.lang.String expectedHostNameTwo = "c6402.apache.ambari.org";
        final java.lang.String expectedHostNameThree = "c6403.apache.ambari.org";
        final java.lang.String expectedHostNameFour = "c6404.apache.ambari.org";
        final java.lang.String expectedPortNum = "808080";
        final java.lang.String expectedNodeOne = "nn1";
        final java.lang.String expectedNodeTwo = "nn2";
        final java.lang.String expectedNodeThree = "nn3";
        final java.lang.String expectedNodeFour = "nn4";
        final java.lang.String expectedHostGroupName = "host_group_1";
        final java.lang.String expectedHostGroupNameTwo = "host_group_2";
        final java.lang.String expectedHostGroupNameThree = "host-group-3";
        final java.lang.String expectedHostGroupNameFour = "host-group-4";
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> properties = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.lang.String> hdfsSiteProperties = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.lang.String> hbaseSiteProperties = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.lang.String> hadoopEnvProperties = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.lang.String> coreSiteProperties = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.lang.String> accumuloSiteProperties = new java.util.HashMap<>();
        properties.put("hdfs-site", hdfsSiteProperties);
        properties.put("hadoop-env", hadoopEnvProperties);
        properties.put("core-site", coreSiteProperties);
        properties.put("hbase-site", hbaseSiteProperties);
        properties.put("accumulo-site", accumuloSiteProperties);
        hdfsSiteProperties.put("dfs.nameservices", (expectedNameService + ",") + expectedNameServiceTwo);
        hdfsSiteProperties.put("dfs.ha.namenodes.mynameservice", (expectedNodeOne + ", ") + expectedNodeTwo);
        hdfsSiteProperties.put("dfs.ha.namenodes." + expectedNameServiceTwo, (expectedNodeThree + ",") + expectedNodeFour);
        hdfsSiteProperties.put((("dfs.namenode.https-address." + expectedNameService) + ".") + expectedNodeOne, org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.createExportedAddress(expectedPortNum, expectedHostGroupName));
        hdfsSiteProperties.put((("dfs.namenode.https-address." + expectedNameService) + ".") + expectedNodeTwo, org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.createExportedAddress(expectedPortNum, expectedHostGroupNameTwo));
        hdfsSiteProperties.put((("dfs.namenode.http-address." + expectedNameService) + ".") + expectedNodeOne, org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.createExportedAddress(expectedPortNum, expectedHostGroupName));
        hdfsSiteProperties.put((("dfs.namenode.http-address." + expectedNameService) + ".") + expectedNodeTwo, org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.createExportedAddress(expectedPortNum, expectedHostGroupNameTwo));
        hdfsSiteProperties.put((("dfs.namenode.https-address." + expectedNameServiceTwo) + ".") + expectedNodeThree, org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.createExportedAddress(expectedPortNum, expectedHostGroupNameThree));
        hdfsSiteProperties.put((("dfs.namenode.https-address." + expectedNameServiceTwo) + ".") + expectedNodeFour, org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.createExportedAddress(expectedPortNum, expectedHostGroupNameFour));
        hdfsSiteProperties.put((("dfs.namenode.http-address." + expectedNameServiceTwo) + ".") + expectedNodeThree, org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.createExportedAddress(expectedPortNum, expectedHostGroupNameThree));
        hdfsSiteProperties.put((("dfs.namenode.http-address." + expectedNameServiceTwo) + ".") + expectedNodeFour, org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.createExportedAddress(expectedPortNum, expectedHostGroupNameFour));
        hdfsSiteProperties.put("dfs.secondary.http.address", "localhost:8080");
        hdfsSiteProperties.put("dfs.namenode.secondary.http-address", "localhost:8080");
        org.apache.ambari.server.topology.Configuration clusterConfig = new org.apache.ambari.server.topology.Configuration(properties, java.util.Collections.emptyMap());
        java.util.Collection<java.lang.String> hgComponents = new java.util.HashSet<>();
        hgComponents.add("NAMENODE");
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup group1 = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup(expectedHostGroupName, hgComponents, java.util.Collections.singleton(expectedHostName));
        java.util.Collection<java.lang.String> hgComponents2 = new java.util.HashSet<>();
        hgComponents2.add("NAMENODE");
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup group2 = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup(expectedHostGroupNameTwo, hgComponents2, java.util.Collections.singleton(expectedHostNameTwo));
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup group3 = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup(expectedHostGroupNameThree, java.util.Collections.singleton("NAMENODE"), java.util.Collections.singleton(expectedHostNameThree));
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup group4 = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup(expectedHostGroupNameFour, java.util.Collections.singleton("NAMENODE"), java.util.Collections.singleton(expectedHostNameFour));
        java.util.Collection<org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup> hostGroups = new java.util.ArrayList<>();
        hostGroups.add(group1);
        hostGroups.add(group2);
        hostGroups.add(group3);
        hostGroups.add(group4);
        EasyMock.expect(stack.getCardinality("NAMENODE")).andReturn(new org.apache.ambari.server.topology.Cardinality("1-2")).anyTimes();
        EasyMock.expect(stack.getCardinality("SECONDARY_NAMENODE")).andReturn(new org.apache.ambari.server.topology.Cardinality("1")).anyTimes();
        org.apache.ambari.server.topology.ClusterTopology topology = createClusterTopology(bp, clusterConfig, hostGroups);
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor updater = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor(topology);
        java.util.Set<java.lang.String> updatedConfigTypes = updater.doUpdateForClusterCreate();
    }

    @org.junit.Test
    public void testDoUpdateForClusterWithNameNodeHANotEnabled() throws java.lang.Exception {
        final java.lang.String expectedHostName = "c6401.apache.ambari.org";
        final java.lang.String expectedHostNameTwo = "serverTwo";
        final java.lang.String expectedHostGroupName = "host_group_1";
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> properties = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.lang.String> hdfsSiteProperties = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.lang.String> hbaseSiteProperties = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.lang.String> hadoopEnvProperties = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.lang.String> coreSiteProperties = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.lang.String> accumuloSiteProperties = new java.util.HashMap<>();
        properties.put("hdfs-site", hdfsSiteProperties);
        properties.put("hadoop-env", hadoopEnvProperties);
        properties.put("core-site", coreSiteProperties);
        properties.put("hbase-site", hbaseSiteProperties);
        properties.put("accumulo-site", accumuloSiteProperties);
        hdfsSiteProperties.put("dfs.secondary.http.address", "localhost:8080");
        hdfsSiteProperties.put("dfs.namenode.secondary.http-address", "localhost:8080");
        hdfsSiteProperties.put("dfs.namenode.http-address", "localhost:8080");
        hdfsSiteProperties.put("dfs.namenode.https-address", "localhost:8081");
        hdfsSiteProperties.put("dfs.namenode.rpc-address", "localhost:8082");
        org.apache.ambari.server.topology.Configuration clusterConfig = new org.apache.ambari.server.topology.Configuration(properties, java.util.Collections.emptyMap());
        java.util.Collection<java.lang.String> hgComponents = new java.util.HashSet<>();
        hgComponents.add("NAMENODE");
        hgComponents.add("SECONDARY_NAMENODE");
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup group1 = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup(expectedHostGroupName, hgComponents, java.util.Collections.singleton(expectedHostName));
        java.util.Collection<java.lang.String> hgComponents2 = new java.util.HashSet<>();
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup group2 = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup("host-group-2", hgComponents2, java.util.Collections.singleton(expectedHostNameTwo));
        java.util.Collection<org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup> hostGroups = new java.util.ArrayList<>();
        hostGroups.add(group1);
        hostGroups.add(group2);
        EasyMock.expect(stack.getCardinality("NAMENODE")).andReturn(new org.apache.ambari.server.topology.Cardinality("1-2")).anyTimes();
        EasyMock.expect(stack.getCardinality("SECONDARY_NAMENODE")).andReturn(new org.apache.ambari.server.topology.Cardinality("1")).anyTimes();
        org.apache.ambari.server.topology.ClusterTopology topology = createClusterTopology(bp, clusterConfig, hostGroups);
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor updater = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor(topology);
        java.util.Set<java.lang.String> updatedConfigTypes = updater.doUpdateForClusterCreate();
        org.junit.Assert.assertTrue("dfs.namenode.http-address should have been included in this HA configuration", hdfsSiteProperties.containsKey("dfs.namenode.http-address"));
        org.junit.Assert.assertTrue("dfs.namenode.https-address should have been included in this HA configuration", hdfsSiteProperties.containsKey("dfs.namenode.https-address"));
        org.junit.Assert.assertTrue("dfs.namenode.rpc-address should have been included in this HA configuration", hdfsSiteProperties.containsKey("dfs.namenode.rpc-address"));
        org.junit.Assert.assertEquals(com.google.common.collect.ImmutableSet.of("cluster-env", "hdfs-site"), updatedConfigTypes);
    }

    @org.junit.Test
    public void testDoUpdateForClusterWithNameNodeHAEnabledAndActiveNodeSet() throws java.lang.Exception {
        final java.lang.String expectedNameService = "mynameservice";
        final java.lang.String expectedHostName = "server-three";
        final java.lang.String expectedHostNameTwo = "server-four";
        final java.lang.String expectedPortNum = "808080";
        final java.lang.String expectedNodeOne = "nn1";
        final java.lang.String expectedNodeTwo = "nn2";
        final java.lang.String expectedHostGroupName = "host_group_1";
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> properties = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.lang.String> hdfsSiteProperties = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.lang.String> hadoopEnvProperties = new java.util.HashMap<>();
        properties.put("hdfs-site", hdfsSiteProperties);
        properties.put("hadoop-env", hadoopEnvProperties);
        hdfsSiteProperties.put("dfs.nameservices", expectedNameService);
        hdfsSiteProperties.put("dfs.ha.namenodes.mynameservice", (expectedNodeOne + ", ") + expectedNodeTwo);
        hdfsSiteProperties.put((("dfs.namenode.https-address." + expectedNameService) + ".") + expectedNodeOne, org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.createExportedAddress(expectedPortNum, expectedHostGroupName));
        hdfsSiteProperties.put((("dfs.namenode.https-address." + expectedNameService) + ".") + expectedNodeTwo, org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.createExportedAddress(expectedPortNum, expectedHostGroupName));
        hdfsSiteProperties.put((("dfs.namenode.http-address." + expectedNameService) + ".") + expectedNodeOne, org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.createExportedAddress(expectedPortNum, expectedHostGroupName));
        hdfsSiteProperties.put((("dfs.namenode.http-address." + expectedNameService) + ".") + expectedNodeTwo, org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.createExportedAddress(expectedPortNum, expectedHostGroupName));
        hdfsSiteProperties.put((("dfs.namenode.rpc-address." + expectedNameService) + ".") + expectedNodeOne, org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.createExportedAddress(expectedPortNum, expectedHostGroupName));
        hdfsSiteProperties.put((("dfs.namenode.rpc-address." + expectedNameService) + ".") + expectedNodeTwo, org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.createExportedAddress(expectedPortNum, expectedHostGroupName));
        hadoopEnvProperties.put("dfs_ha_initial_namenode_active", expectedHostName);
        hadoopEnvProperties.put("dfs_ha_initial_namenode_standby", expectedHostNameTwo);
        org.apache.ambari.server.topology.Configuration clusterConfig = new org.apache.ambari.server.topology.Configuration(properties, java.util.Collections.emptyMap());
        java.util.Collection<java.lang.String> hgComponents = new java.util.HashSet<>();
        hgComponents.add("NAMENODE");
        java.util.Collection<java.lang.String> hosts = new java.util.ArrayList<>();
        hosts.add(expectedHostName);
        hosts.add(expectedHostNameTwo);
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup group = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup(expectedHostGroupName, hgComponents, hosts);
        java.util.Collection<org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup> hostGroups = new java.util.ArrayList<>();
        hostGroups.add(group);
        org.apache.ambari.server.topology.ClusterTopology topology = createClusterTopology(bp, clusterConfig, hostGroups);
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor updater = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor(topology);
        updater.doUpdateForClusterCreate();
        java.lang.String expectedPropertyValue = hdfsSiteProperties.get((("dfs.namenode.https-address." + expectedNameService) + ".") + expectedNodeOne);
        if ((!expectedPropertyValue.equals((expectedHostName + ":") + expectedPortNum)) && (!expectedPropertyValue.equals((expectedHostNameTwo + ":") + expectedPortNum))) {
            org.junit.Assert.fail("HTTPS address HA property not properly exported");
        }
        org.junit.Assert.assertEquals("HTTPS address HA property not properly exported", expectedPropertyValue, hdfsSiteProperties.get((("dfs.namenode.https-address." + expectedNameService) + ".") + expectedNodeTwo));
        org.junit.Assert.assertEquals("HTTPS address HA property not properly exported", expectedPropertyValue, hdfsSiteProperties.get((("dfs.namenode.http-address." + expectedNameService) + ".") + expectedNodeOne));
        org.junit.Assert.assertEquals("HTTPS address HA property not properly exported", expectedPropertyValue, hdfsSiteProperties.get((("dfs.namenode.http-address." + expectedNameService) + ".") + expectedNodeTwo));
        org.junit.Assert.assertEquals("HTTPS address HA property not properly exported", expectedPropertyValue, hdfsSiteProperties.get((("dfs.namenode.rpc-address." + expectedNameService) + ".") + expectedNodeOne));
        org.junit.Assert.assertEquals("HTTPS address HA property not properly exported", expectedPropertyValue, hdfsSiteProperties.get((("dfs.namenode.rpc-address." + expectedNameService) + ".") + expectedNodeTwo));
        java.util.Map<java.lang.String, java.lang.String> clusterEnv = clusterConfig.getProperties().get("cluster-env");
        org.junit.Assert.assertEquals("Active Namenode hostname was not set correctly", expectedHostName, clusterEnv.get("dfs_ha_initial_namenode_active"));
        org.junit.Assert.assertEquals("Standby Namenode hostname was not set correctly", expectedHostNameTwo, clusterEnv.get("dfs_ha_initial_namenode_standby"));
    }

    private java.util.Map<java.lang.String, java.lang.String> defaultStackProps() {
        return com.google.common.collect.Maps.newHashMap(com.google.common.collect.ImmutableMap.of(org.apache.ambari.server.state.ConfigHelper.CLUSTER_ENV_STACK_NAME_PROPERTY, STACK_NAME, org.apache.ambari.server.state.ConfigHelper.CLUSTER_ENV_STACK_ROOT_PROPERTY, "/usr/" + STACK_NAME, org.apache.ambari.server.state.ConfigHelper.CLUSTER_ENV_STACK_TOOLS_PROPERTY, "{ some tools... }", org.apache.ambari.server.state.ConfigHelper.CLUSTER_ENV_STACK_FEATURES_PROPERTY, "{ some features... }", org.apache.ambari.server.state.ConfigHelper.CLUSTER_ENV_STACK_PACKAGES_PROPERTY, "{ some packages... }"));
    }

    @org.junit.Test
    public void testSetStackToolsAndFeatures_ClusterEnvDidNotChange() throws java.lang.Exception {
        defaultClusterEnvProperties.putAll(defaultStackProps());
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> blueprintProps = com.google.common.collect.Maps.newHashMap(com.google.common.collect.ImmutableMap.of("cluster-env", defaultStackProps()));
        org.apache.ambari.server.topology.Configuration clusterConfig = new org.apache.ambari.server.topology.Configuration(blueprintProps, java.util.Collections.emptyMap());
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup group = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup("groups1", com.google.common.collect.Sets.newHashSet("NAMENODE"), com.google.common.collect.ImmutableSet.of("host1"));
        org.apache.ambari.server.topology.ClusterTopology topology = createClusterTopology(bp, clusterConfig, com.google.common.collect.ImmutableSet.of(group));
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor updater = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor(topology);
        java.util.Set<java.lang.String> configTypesUpdated = com.google.common.collect.Sets.newHashSet();
        updater.setStackToolsAndFeatures(clusterConfig, configTypesUpdated);
        org.junit.Assert.assertEquals("cluster-env should NOT have been updated", com.google.common.collect.ImmutableSet.of(), configTypesUpdated);
    }

    @org.junit.Test
    public void testSetStackToolsAndFeatures_ClusterEnvChanged() throws java.lang.Exception {
        defaultClusterEnvProperties.putAll(defaultStackProps());
        java.util.Map<java.lang.String, java.lang.String> blueprintClusterEnv = defaultStackProps();
        blueprintClusterEnv.put(org.apache.ambari.server.state.ConfigHelper.CLUSTER_ENV_STACK_ROOT_PROPERTY, "/opt/" + STACK_NAME);
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> blueprintProps = com.google.common.collect.Maps.newHashMap(com.google.common.collect.ImmutableMap.of("cluster-env", blueprintClusterEnv));
        org.apache.ambari.server.topology.Configuration clusterConfig = new org.apache.ambari.server.topology.Configuration(blueprintProps, java.util.Collections.emptyMap());
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup group = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup("groups1", com.google.common.collect.Sets.newHashSet("NAMENODE"), com.google.common.collect.ImmutableSet.of("host1"));
        org.apache.ambari.server.topology.ClusterTopology topology = createClusterTopology(bp, clusterConfig, com.google.common.collect.ImmutableSet.of(group));
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor updater = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor(topology);
        java.util.Set<java.lang.String> configTypesUpdated = com.google.common.collect.Sets.newHashSet();
        updater.setStackToolsAndFeatures(clusterConfig, configTypesUpdated);
        org.junit.Assert.assertEquals("cluster-env should have been updated", com.google.common.collect.ImmutableSet.of("cluster-env"), configTypesUpdated);
    }

    @org.junit.Test
    public void testSetStackToolsAndFeatures_ClusterEnvChanged_TrimmedValuesEqual() throws java.lang.Exception {
        defaultClusterEnvProperties.putAll(defaultStackProps());
        java.util.Map<java.lang.String, java.lang.String> blueprintClusterEnv = defaultStackProps();
        blueprintClusterEnv.put(org.apache.ambari.server.state.ConfigHelper.CLUSTER_ENV_STACK_ROOT_PROPERTY, blueprintClusterEnv.get(org.apache.ambari.server.state.ConfigHelper.CLUSTER_ENV_STACK_ROOT_PROPERTY) + "       \n");
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> blueprintProps = com.google.common.collect.Maps.newHashMap(com.google.common.collect.ImmutableMap.of("cluster-env", blueprintClusterEnv));
        org.apache.ambari.server.topology.Configuration clusterConfig = new org.apache.ambari.server.topology.Configuration(blueprintProps, java.util.Collections.emptyMap());
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup group = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup("groups1", com.google.common.collect.Sets.newHashSet("NAMENODE"), com.google.common.collect.ImmutableSet.of("host1"));
        org.apache.ambari.server.topology.ClusterTopology topology = createClusterTopology(bp, clusterConfig, com.google.common.collect.ImmutableSet.of(group));
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor updater = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor(topology);
        java.util.Set<java.lang.String> configTypesUpdated = com.google.common.collect.Sets.newHashSet();
        updater.setStackToolsAndFeatures(clusterConfig, configTypesUpdated);
        org.junit.Assert.assertEquals("cluster-env should NOT have been updated", com.google.common.collect.ImmutableSet.of(), configTypesUpdated);
    }

    @org.junit.Test
    public void testParseNameServices() throws java.lang.Exception {
        java.util.Map<java.lang.String, java.lang.String> hdfsSiteConfigMap = new java.util.HashMap<>();
        hdfsSiteConfigMap.put("dfs.nameservices", "serviceOne");
        java.lang.String[] result = org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.parseNameServices(hdfsSiteConfigMap);
        org.junit.Assert.assertNotNull("Resulting array was null", result);
        org.junit.Assert.assertEquals("Incorrect array size", 1, result.length);
        org.junit.Assert.assertEquals("Incorrect value for returned name service", "serviceOne", result[0]);
        hdfsSiteConfigMap.put("dfs.internal.nameservices", "serviceTwo");
        result = org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.parseNameServices(hdfsSiteConfigMap);
        org.junit.Assert.assertNotNull("Resulting array was null", result);
        org.junit.Assert.assertEquals("Incorrect array size", 1, result.length);
        org.junit.Assert.assertEquals("Incorrect value for returned name service", "serviceTwo", result[0]);
        hdfsSiteConfigMap.put("dfs.internal.nameservices", " serviceTwo, serviceThree, serviceFour");
        java.lang.String[] resultTwo = org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.parseNameServices(hdfsSiteConfigMap);
        org.junit.Assert.assertNotNull("Resulting array was null", resultTwo);
        org.junit.Assert.assertEquals("Incorrect array size", 3, resultTwo.length);
        org.junit.Assert.assertEquals("Incorrect value for returned name service", "serviceTwo", resultTwo[0]);
        org.junit.Assert.assertEquals("Incorrect value for returned name service", "serviceThree", resultTwo[1]);
        org.junit.Assert.assertEquals("Incorrect value for returned name service", "serviceFour", resultTwo[2]);
    }

    @org.junit.Test
    public void testParseNameNodes() throws java.lang.Exception {
        final java.lang.String expectedServiceName = "serviceOne";
        java.util.Map<java.lang.String, java.lang.String> hdfsSiteConfigMap = new java.util.HashMap<>();
        hdfsSiteConfigMap.put("dfs.ha.namenodes." + expectedServiceName, "node1");
        java.lang.String[] result = org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.parseNameNodes(expectedServiceName, hdfsSiteConfigMap);
        org.junit.Assert.assertNotNull("Resulting array was null", result);
        org.junit.Assert.assertEquals("Incorrect array size", 1, result.length);
        org.junit.Assert.assertEquals("Incorrect value for returned name nodes", "node1", result[0]);
        hdfsSiteConfigMap.put("dfs.ha.namenodes." + expectedServiceName, " nodeSeven, nodeEight, nodeNine");
        java.lang.String[] resultTwo = org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.parseNameNodes(expectedServiceName, hdfsSiteConfigMap);
        org.junit.Assert.assertNotNull("Resulting array was null", resultTwo);
        org.junit.Assert.assertEquals("Incorrect array size", 3, resultTwo.length);
        org.junit.Assert.assertEquals("Incorrect value for returned name node", "nodeSeven", resultTwo[0]);
        org.junit.Assert.assertEquals("Incorrect value for returned name node", "nodeEight", resultTwo[1]);
        org.junit.Assert.assertEquals("Incorrect value for returned name node", "nodeNine", resultTwo[2]);
    }

    @org.junit.Test
    public void testHDFSConfigClusterUpdateQuorumJournalURL() throws java.lang.Exception {
        final java.lang.String expectedHostNameOne = "c6401.apache.ambari.org";
        final java.lang.String expectedHostNameTwo = "c6402.apache.ambari.org";
        final java.lang.String expectedPortNum = "808080";
        final java.lang.String expectedHostGroupName = "host_group_1";
        final java.lang.String expectedHostGroupNameTwo = "host_group_2";
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> properties = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.lang.String> hdfsSiteProperties = new java.util.HashMap<>();
        properties.put("hdfs-site", hdfsSiteProperties);
        hdfsSiteProperties.put("dfs.namenode.shared.edits.dir", ((("qjournal://" + org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.createExportedAddress(expectedPortNum, expectedHostGroupName)) + ";") + org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.createExportedAddress(expectedPortNum, expectedHostGroupNameTwo)) + "/mycluster");
        org.apache.ambari.server.topology.Configuration clusterConfig = new org.apache.ambari.server.topology.Configuration(properties, java.util.Collections.emptyMap());
        java.util.Collection<java.lang.String> hgComponents1 = new java.util.HashSet<>();
        hgComponents1.add("NAMENODE");
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup group1 = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup(expectedHostGroupName, hgComponents1, java.util.Collections.singleton(expectedHostNameOne));
        java.util.Collection<java.lang.String> hgComponents2 = new java.util.HashSet<>();
        hgComponents2.add("NAMENODE");
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup group2 = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup(expectedHostGroupNameTwo, hgComponents2, java.util.Collections.singleton(expectedHostNameTwo));
        java.util.Collection<org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup> hostGroups = new java.util.ArrayList<>();
        hostGroups.add(group1);
        hostGroups.add(group2);
        org.apache.ambari.server.topology.ClusterTopology topology = createClusterTopology(bp, clusterConfig, hostGroups);
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor updater = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor(topology);
        updater.doUpdateForClusterCreate();
        org.junit.Assert.assertEquals("HDFS HA shared edits directory property not properly updated for cluster create.", ((("qjournal://" + org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.createHostAddress(expectedHostNameOne, expectedPortNum)) + ";") + org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.createHostAddress(expectedHostNameTwo, expectedPortNum)) + "/mycluster", hdfsSiteProperties.get("dfs.namenode.shared.edits.dir"));
    }

    @org.junit.Test
    public void testHDFSConfigClusterUpdateQuorumJournalURL_UsingMinusSymbolInHostName() throws java.lang.Exception {
        final java.lang.String expectedHostNameOne = "c6401.apache.ambari.org";
        final java.lang.String expectedHostNameTwo = "c6402.apache.ambari.org";
        final java.lang.String expectedPortNum = "808080";
        final java.lang.String expectedHostGroupName = "host-group-1";
        final java.lang.String expectedHostGroupNameTwo = "host-group-2";
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> properties = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.lang.String> hdfsSiteProperties = new java.util.HashMap<>();
        properties.put("hdfs-site", hdfsSiteProperties);
        hdfsSiteProperties.put("dfs.namenode.shared.edits.dir", ((("qjournal://" + org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.createExportedAddress(expectedPortNum, expectedHostGroupName)) + ";") + org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.createExportedAddress(expectedPortNum, expectedHostGroupNameTwo)) + "/mycluster");
        org.apache.ambari.server.topology.Configuration clusterConfig = new org.apache.ambari.server.topology.Configuration(properties, java.util.Collections.emptyMap());
        java.util.Collection<java.lang.String> hgComponents1 = new java.util.HashSet<>();
        hgComponents1.add("NAMENODE");
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup group1 = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup(expectedHostGroupName, hgComponents1, java.util.Collections.singleton(expectedHostNameOne));
        java.util.Collection<java.lang.String> hgComponents2 = new java.util.HashSet<>();
        hgComponents2.add("NAMENODE");
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup group2 = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup(expectedHostGroupNameTwo, hgComponents2, java.util.Collections.singleton(expectedHostNameTwo));
        java.util.Collection<org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup> hostGroups = new java.util.ArrayList<>();
        hostGroups.add(group1);
        hostGroups.add(group2);
        org.apache.ambari.server.topology.ClusterTopology topology = createClusterTopology(bp, clusterConfig, hostGroups);
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor updater = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor(topology);
        updater.doUpdateForClusterCreate();
        org.junit.Assert.assertEquals("HDFS HA shared edits directory property not properly updated for cluster create.", ((("qjournal://" + org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.createHostAddress(expectedHostNameOne, expectedPortNum)) + ";") + org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.createHostAddress(expectedHostNameTwo, expectedPortNum)) + "/mycluster", hdfsSiteProperties.get("dfs.namenode.shared.edits.dir"));
    }

    @org.junit.Test
    public void testHadoopHaNameNode() throws java.lang.Exception {
        final java.lang.String configType = "cluster-env";
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> properties = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.lang.String> hdfsSite = new java.util.HashMap<>();
        hdfsSite.put("dfs.nameservices", "mycluster");
        hdfsSite.put("dfs.ha.namenodes.mycluster", "nn1,nn2");
        hdfsSite.put("dfs.namenode.http-address", "%HOSTGROUP::master_1%:50070");
        hdfsSite.put("dfs.namenode.http-address.mycluster.nn1", "%HOSTGROUP::master_1%:50070");
        hdfsSite.put("dfs.namenode.http-address.mycluster.nn2", "%HOSTGROUP::master_2%:50070");
        hdfsSite.put("dfs.namenode.https-address", "%HOSTGROUP::master_1%:50470");
        hdfsSite.put("dfs.namenode.https-address.mycluster.nn1", "%HOSTGROUP::master_1%:50470");
        hdfsSite.put("dfs.namenode.https-address.mycluster.nn2", "%HOSTGROUP::master_2%:50470");
        hdfsSite.put("dfs.namenode.rpc-address.mycluster.nn1", "%HOSTGROUP::master_1%:8020");
        hdfsSite.put("dfs.namenode.rpc-address.mycluster.nn2", "%HOSTGROUP::master_2%:8020");
        hdfsSite.put("dfs.namenode.shared.edits.dir", "qjournal://%HOSTGROUP::master_1%:8485;%HOSTGROUP::master_2%:8485;%HOSTGROUP::master_2%:8485/mycluster");
        hdfsSite.put("dfs.ha.automatic-failover.enabled", "true");
        hdfsSite.put("dfs.ha.fencing.methods", "shell(/bin/true)");
        properties.put("hdfs-site", hdfsSite);
        java.util.Map<java.lang.String, java.lang.String> hadoopEnv = new java.util.HashMap<>();
        hadoopEnv.put("dfs_ha_initial_namenode_active", "%HOSTGROUP::master_1%");
        hadoopEnv.put("dfs_ha_initial_namenode_standby", "%HOSTGROUP::master_2%");
        properties.put("hadoop-env", hadoopEnv);
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> parentProperties = new java.util.HashMap<>();
        org.apache.ambari.server.topology.Configuration parentClusterConfig = new org.apache.ambari.server.topology.Configuration(parentProperties, java.util.Collections.emptyMap());
        org.apache.ambari.server.topology.Configuration clusterConfig = new org.apache.ambari.server.topology.Configuration(properties, java.util.Collections.emptyMap(), parentClusterConfig);
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup group1 = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup("master_1", com.google.common.collect.ImmutableSet.of("DATANODE", "NAMENODE"), java.util.Collections.singleton("node_1"));
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup group2 = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup("master_2", com.google.common.collect.ImmutableSet.of("DATANODE", "NAMENODE"), java.util.Collections.singleton("node_2"));
        java.util.Collection<org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup> hostGroups = com.google.common.collect.Lists.newArrayList(group1, group2);
        org.apache.ambari.server.topology.ClusterTopology topology = createClusterTopology(bp, clusterConfig, hostGroups);
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor configProcessor = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor(topology);
        configProcessor.doUpdateForClusterCreate();
        org.junit.Assert.assertEquals("node_1", clusterConfig.getPropertyValue(configType, "dfs_ha_initial_namenode_active"));
        org.junit.Assert.assertEquals("node_2", clusterConfig.getPropertyValue(configType, "dfs_ha_initial_namenode_standby"));
    }

    @org.junit.Test
    public void testGetRequiredHostGroups___validComponentCountOfZero() throws java.lang.Exception {
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> properties = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.lang.String> hiveSite = new java.util.HashMap<>();
        properties.put("hive-site", hiveSite);
        java.util.Map<java.lang.String, java.lang.String> hiveEnv = new java.util.HashMap<>();
        properties.put("hive-env", hiveEnv);
        hiveSite.put("javax.jdo.option.ConnectionURL", "localhost:1111");
        hiveEnv.put("hive_database", "New Database");
        org.apache.ambari.server.topology.Configuration clusterConfig = new org.apache.ambari.server.topology.Configuration(properties, java.util.Collections.emptyMap());
        java.util.Collection<java.lang.String> hgComponents1 = new java.util.HashSet<>();
        hgComponents1.add("HIVE_SERVER");
        hgComponents1.add("NAMENODE");
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup group1 = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup("group1", hgComponents1, java.util.Collections.singleton("host1"));
        java.util.Collection<java.lang.String> hgComponents2 = new java.util.HashSet<>();
        hgComponents2.add("DATANODE");
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup group2 = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup("group2", hgComponents2, java.util.Collections.singleton("host2"));
        java.util.Collection<org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup> hostGroups = new java.util.ArrayList<>();
        hostGroups.add(group1);
        hostGroups.add(group2);
        org.apache.ambari.server.topology.ClusterTopology topology = createClusterTopology(bp, clusterConfig, hostGroups);
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor updater = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor(topology);
        java.util.Collection<java.lang.String> requiredGroups = updater.getRequiredHostGroups();
        org.junit.Assert.assertEquals(0, requiredGroups.size());
    }

    @org.junit.Test
    public void testGetRequiredHostGroups___invalidComponentCountOfZero() throws java.lang.Exception {
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> properties = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.lang.String> coreSiteMap = new java.util.HashMap<>();
        properties.put("core-site", coreSiteMap);
        coreSiteMap.put("fs.defaultFS", "localhost");
        org.apache.ambari.server.topology.Configuration clusterConfig = new org.apache.ambari.server.topology.Configuration(properties, java.util.Collections.emptyMap());
        EasyMock.expect(stack.getCardinality("NAMENODE")).andReturn(new org.apache.ambari.server.topology.Cardinality("1")).anyTimes();
        java.util.Collection<java.lang.String> hgComponents1 = new java.util.HashSet<>();
        hgComponents1.add("HIVE_SERVER");
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup group1 = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup("group1", hgComponents1, java.util.Collections.singleton("host1"));
        java.util.Collection<java.lang.String> hgComponents2 = new java.util.HashSet<>();
        hgComponents2.add("DATANODE");
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup group2 = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup("group2", hgComponents2, java.util.Collections.singleton("host2"));
        java.util.Collection<org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup> hostGroups = new java.util.ArrayList<>();
        hostGroups.add(group1);
        hostGroups.add(group2);
        org.apache.ambari.server.topology.ClusterTopology topology = createClusterTopology(bp, clusterConfig, hostGroups);
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor updater = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor(topology);
        java.util.Collection<java.lang.String> requiredGroups = updater.getRequiredHostGroups();
        org.junit.Assert.assertEquals(0, requiredGroups.size());
    }

    @org.junit.Test
    public void testGetRequiredHostGroups___multipleGroups() throws java.lang.Exception {
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> properties = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.lang.String> coreSiteMap = new java.util.HashMap<>();
        properties.put("core-site", coreSiteMap);
        coreSiteMap.put("fs.defaultFS", "localhost");
        org.apache.ambari.server.topology.Configuration clusterConfig = new org.apache.ambari.server.topology.Configuration(properties, java.util.Collections.emptyMap());
        EasyMock.expect(stack.getCardinality("NAMENODE")).andReturn(new org.apache.ambari.server.topology.Cardinality("1")).anyTimes();
        java.util.Collection<java.lang.String> hgComponents1 = new java.util.HashSet<>();
        hgComponents1.add("HIVE_SERVER");
        hgComponents1.add("NAMENODE");
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup group1 = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup("group1", hgComponents1, java.util.Collections.singleton("host1"));
        java.util.Collection<java.lang.String> hgComponents2 = new java.util.HashSet<>();
        hgComponents2.add("NAMENODE");
        hgComponents2.add("DATANODE");
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup group2 = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup("group2", hgComponents2, java.util.Collections.singleton("host2"));
        java.util.Collection<org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup> hostGroups = new java.util.ArrayList<>();
        hostGroups.add(group1);
        hostGroups.add(group2);
        org.apache.ambari.server.topology.ClusterTopology topology = createClusterTopology(bp, clusterConfig, hostGroups);
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor updater = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor(topology);
        java.util.Collection<java.lang.String> requiredGroups = updater.getRequiredHostGroups();
        org.junit.Assert.assertEquals(2, requiredGroups.size());
        org.junit.Assert.assertTrue(requiredGroups.containsAll(java.util.Arrays.asList("group1", "group2")));
    }

    @org.junit.Test
    public void testGetRequiredHostGroups___defaultUpdater() throws java.lang.Exception {
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> properties = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.lang.String> coreSiteMap = new java.util.HashMap<>();
        coreSiteMap.put("fs.defaultFS", "localhost");
        properties.put("core-site", coreSiteMap);
        properties.put("myservice-site", com.google.common.collect.ImmutableMap.of("myservice.slave.urls", "%HOSTGROUP::group4%:8080,%HOSTGROUP::group4%:8080,%HOSTGROUP::group5%:8080"));
        org.apache.ambari.server.topology.Configuration clusterConfig = new org.apache.ambari.server.topology.Configuration(properties, java.util.Collections.emptyMap());
        EasyMock.expect(stack.getCardinality("NAMENODE")).andReturn(new org.apache.ambari.server.topology.Cardinality("1")).anyTimes();
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup group1 = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup("group1", com.google.common.collect.ImmutableSet.of("HIVE_SERVER", "NAMENODE"), java.util.Collections.singleton("host1"));
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup group2 = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup("group2", com.google.common.collect.ImmutableSet.of("NAMENODE", "DATANODE"), java.util.Collections.singleton("host2"));
        org.apache.ambari.server.topology.Configuration group3Configuration = new org.apache.ambari.server.topology.Configuration(com.google.common.collect.ImmutableMap.of("myservice-site", com.google.common.collect.ImmutableMap.of("myservice.master.url", "%HOSTGROUP::group3%:8080")), java.util.Collections.emptyMap());
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup group3 = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup("group3", com.google.common.collect.ImmutableSet.of(), java.util.Collections.singleton("host3"), group3Configuration);
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup group4 = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup("group4", com.google.common.collect.ImmutableSet.of(), com.google.common.collect.ImmutableSet.of("host4a", "host4b"));
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup group5 = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup("group5", com.google.common.collect.ImmutableSet.of(), java.util.Collections.singleton("host5"));
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup group6 = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup("group6", com.google.common.collect.ImmutableSet.of(), java.util.Collections.singleton("host6"));
        java.util.Collection<org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup> hostGroups = com.google.common.collect.Lists.newArrayList(group1, group2, group3, group4, group5, group6);
        org.apache.ambari.server.topology.ClusterTopology topology = createClusterTopology(bp, clusterConfig, hostGroups);
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor updater = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor(topology);
        java.util.Set<java.lang.String> requiredGroups = updater.getRequiredHostGroups();
        org.junit.Assert.assertEquals(com.google.common.collect.ImmutableSet.of("group1", "group2", "group3", "group4", "group5"), requiredGroups);
    }

    @org.junit.Test
    public void testAllDefaultUserAndGroupProxyPropertiesSet() throws java.lang.Exception {
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> properties = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.lang.String> oozieEnvProperties = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.lang.String> hiveEnvProperties = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.lang.String> hbaseEnvProperties = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.lang.String> falconEnvProperties = new java.util.HashMap<>();
        properties.put("oozie-env", oozieEnvProperties);
        properties.put("hive-env", hiveEnvProperties);
        properties.put("hbase-env", hbaseEnvProperties);
        properties.put("falcon-env", falconEnvProperties);
        oozieEnvProperties.put("oozie_user", "test-oozie-user");
        hiveEnvProperties.put("hive_user", "test-hive-user");
        hiveEnvProperties.put("webhcat_user", "test-hcat-user");
        hbaseEnvProperties.put("hbase_user", "test-hbase-user");
        falconEnvProperties.put("falcon_user", "test-falcon-user");
        org.apache.ambari.server.topology.Configuration clusterConfig = new org.apache.ambari.server.topology.Configuration(properties, java.util.Collections.emptyMap());
        java.util.Collection<java.lang.String> hgComponents1 = new java.util.HashSet<>();
        hgComponents1.add("DATANODE");
        hgComponents1.add("OOZIE_SERVER");
        hgComponents1.add("HIVE_SERVER");
        hgComponents1.add("HBASE_MASTER");
        hgComponents1.add("FALCON_SERVER");
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup group1 = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup("group1", hgComponents1, java.util.Collections.singleton("host1"));
        java.util.Collection<org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup> hostGroups = java.util.Collections.singletonList(group1);
        org.apache.ambari.server.topology.ClusterTopology topology = createClusterTopology(bp, clusterConfig, hostGroups);
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor configProcessor = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor(topology);
        configProcessor.doUpdateForClusterCreate();
        org.junit.Assert.assertEquals("*", properties.get("core-site").get("hadoop.proxyuser.test-oozie-user.hosts"));
        org.junit.Assert.assertEquals("*", properties.get("core-site").get("hadoop.proxyuser.test-oozie-user.groups"));
        org.junit.Assert.assertEquals("*", properties.get("core-site").get("hadoop.proxyuser.test-hive-user.hosts"));
        org.junit.Assert.assertEquals("*", properties.get("core-site").get("hadoop.proxyuser.test-hive-user.groups"));
        org.junit.Assert.assertEquals("*", properties.get("core-site").get("hadoop.proxyuser.test-hcat-user.hosts"));
        org.junit.Assert.assertEquals("*", properties.get("core-site").get("hadoop.proxyuser.test-hcat-user.groups"));
        org.junit.Assert.assertEquals("*", properties.get("core-site").get("hadoop.proxyuser.test-hbase-user.hosts"));
        org.junit.Assert.assertEquals("*", properties.get("core-site").get("hadoop.proxyuser.test-hbase-user.groups"));
        org.junit.Assert.assertEquals("*", properties.get("core-site").get("hadoop.proxyuser.test-falcon-user.hosts"));
        org.junit.Assert.assertEquals("*", properties.get("core-site").get("hadoop.proxyuser.test-falcon-user.groups"));
    }

    @org.junit.Test
    public void testRelevantDefaultUserAndGroupProxyPropertiesSet() throws java.lang.Exception {
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> properties = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.lang.String> oozieEnvProperties = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.lang.String> falconEnvProperties = new java.util.HashMap<>();
        properties.put("oozie-env", oozieEnvProperties);
        properties.put("falcon-env", falconEnvProperties);
        oozieEnvProperties.put("oozie_user", "test-oozie-user");
        falconEnvProperties.put("falcon_user", "test-falcon-user");
        org.apache.ambari.server.topology.Configuration clusterConfig = new org.apache.ambari.server.topology.Configuration(properties, java.util.Collections.emptyMap());
        java.util.Collection<java.lang.String> hgComponents1 = new java.util.HashSet<>();
        hgComponents1.add("DATANODE");
        hgComponents1.add("OOZIE_SERVER");
        hgComponents1.add("FALCON_SERVER");
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup group1 = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup("group1", hgComponents1, java.util.Collections.singleton("host1"));
        java.util.Collection<org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup> hostGroups = java.util.Collections.singletonList(group1);
        org.apache.ambari.server.topology.ClusterTopology topology = createClusterTopology(bp, clusterConfig, hostGroups);
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor configProcessor = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor(topology);
        configProcessor.doUpdateForClusterCreate();
        java.util.Map<java.lang.String, java.lang.String> coreSiteProperties = properties.get("core-site");
        org.junit.Assert.assertEquals(4, coreSiteProperties.size());
        org.junit.Assert.assertEquals("*", coreSiteProperties.get("hadoop.proxyuser.test-oozie-user.hosts"));
        org.junit.Assert.assertEquals("*", coreSiteProperties.get("hadoop.proxyuser.test-oozie-user.groups"));
        org.junit.Assert.assertEquals("*", coreSiteProperties.get("hadoop.proxyuser.test-falcon-user.hosts"));
        org.junit.Assert.assertEquals("*", coreSiteProperties.get("hadoop.proxyuser.test-falcon-user.groups"));
    }

    @org.junit.Test
    public void testDefaultUserAndGroupProxyPropertiesSetWhenNotProvided() throws java.lang.Exception {
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> properties = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.lang.String> coreSiteProperties = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.lang.String> oozieEnvProperties = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.lang.String> falconEnvProperties = new java.util.HashMap<>();
        properties.put("core-site", coreSiteProperties);
        properties.put("oozie-env", oozieEnvProperties);
        properties.put("falcon-env", falconEnvProperties);
        coreSiteProperties.put("hadoop.proxyuser.test-oozie-user.hosts", "testOozieHostsVal");
        coreSiteProperties.put("hadoop.proxyuser.test-oozie-user.groups", "testOozieGroupsVal");
        oozieEnvProperties.put("oozie_user", "test-oozie-user");
        falconEnvProperties.put("falcon_user", "test-falcon-user");
        org.apache.ambari.server.topology.Configuration clusterConfig = new org.apache.ambari.server.topology.Configuration(properties, java.util.Collections.emptyMap());
        java.util.Collection<java.lang.String> hgComponents1 = new java.util.HashSet<>();
        hgComponents1.add("DATANODE");
        hgComponents1.add("OOZIE_SERVER");
        hgComponents1.add("FALCON_SERVER");
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup group1 = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup("group1", hgComponents1, java.util.Collections.singleton("host1"));
        java.util.Collection<org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup> hostGroups = java.util.Collections.singletonList(group1);
        org.apache.ambari.server.topology.ClusterTopology topology = createClusterTopology(bp, clusterConfig, hostGroups);
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor configProcessor = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor(topology);
        configProcessor.doUpdateForClusterCreate();
        org.junit.Assert.assertEquals(4, coreSiteProperties.size());
        org.junit.Assert.assertEquals("testOozieHostsVal", coreSiteProperties.get("hadoop.proxyuser.test-oozie-user.hosts"));
        org.junit.Assert.assertEquals("testOozieGroupsVal", coreSiteProperties.get("hadoop.proxyuser.test-oozie-user.groups"));
        org.junit.Assert.assertEquals("*", coreSiteProperties.get("hadoop.proxyuser.test-falcon-user.hosts"));
        org.junit.Assert.assertEquals("*", coreSiteProperties.get("hadoop.proxyuser.test-falcon-user.groups"));
    }

    @org.junit.Test
    public void testDefaultUserAndGroupProxyPropertiesSetWhenNotProvided2() throws java.lang.Exception {
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> properties = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.lang.String> falconEnvProperties = new java.util.HashMap<>();
        properties.put("falcon-env", falconEnvProperties);
        falconEnvProperties.put("falcon_user", "test-falcon-user");
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> parentProperties = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.lang.String> oozieEnvProperties = new java.util.HashMap<>();
        parentProperties.put("oozie-env", oozieEnvProperties);
        oozieEnvProperties.put("oozie_user", "test-oozie-user");
        java.util.Map<java.lang.String, java.lang.String> coreSiteProperties = new java.util.HashMap<>();
        parentProperties.put("core-site", coreSiteProperties);
        coreSiteProperties.put("hadoop.proxyuser.test-oozie-user.hosts", "testOozieHostsVal");
        org.apache.ambari.server.topology.Configuration parentClusterConfig = new org.apache.ambari.server.topology.Configuration(parentProperties, java.util.Collections.emptyMap());
        org.apache.ambari.server.topology.Configuration clusterConfig = new org.apache.ambari.server.topology.Configuration(properties, java.util.Collections.emptyMap(), parentClusterConfig);
        java.util.Collection<java.lang.String> hgComponents1 = new java.util.HashSet<>();
        hgComponents1.add("DATANODE");
        hgComponents1.add("OOZIE_SERVER");
        hgComponents1.add("FALCON_SERVER");
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup group1 = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup("group1", hgComponents1, java.util.Collections.singleton("host1"));
        java.util.Collection<org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup> hostGroups = java.util.Collections.singletonList(group1);
        org.apache.ambari.server.topology.ClusterTopology topology = createClusterTopology(bp, clusterConfig, hostGroups);
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor configProcessor = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor(topology);
        configProcessor.doUpdateForClusterCreate();
        java.util.Map<java.lang.String, java.lang.String> leafConfigCoreSiteProps = properties.get("core-site");
        org.junit.Assert.assertEquals(3, leafConfigCoreSiteProps.size());
        org.junit.Assert.assertEquals("testOozieHostsVal", clusterConfig.getPropertyValue("core-site", "hadoop.proxyuser.test-oozie-user.hosts"));
        org.junit.Assert.assertEquals("*", leafConfigCoreSiteProps.get("hadoop.proxyuser.test-oozie-user.groups"));
        org.junit.Assert.assertEquals("*", leafConfigCoreSiteProps.get("hadoop.proxyuser.test-falcon-user.hosts"));
        org.junit.Assert.assertEquals("*", leafConfigCoreSiteProps.get("hadoop.proxyuser.test-falcon-user.groups"));
    }

    @org.junit.Test
    public void testHiveWithoutAtlas() throws java.lang.Exception {
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> properties = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.lang.String> hiveProperties = new java.util.HashMap<>();
        hiveProperties.put("hive.exec.post.hooks", "");
        properties.put("hive-site", hiveProperties);
        java.util.Map<java.lang.String, java.lang.String> hiveEnv = new java.util.HashMap<>();
        hiveEnv.put("hive.atlas.hook", "false");
        properties.put("hive-env", hiveEnv);
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> parentProperties = new java.util.HashMap<>();
        org.apache.ambari.server.topology.Configuration parentClusterConfig = new org.apache.ambari.server.topology.Configuration(parentProperties, java.util.Collections.emptyMap());
        org.apache.ambari.server.topology.Configuration clusterConfig = new org.apache.ambari.server.topology.Configuration(properties, java.util.Collections.emptyMap(), parentClusterConfig);
        java.util.Collection<java.lang.String> hgComponents1 = new java.util.HashSet<>();
        hgComponents1.add("HIVE_SERVER");
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup group1 = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup("group1", hgComponents1, java.util.Collections.singleton("host1"));
        java.util.Collection<org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup> hostGroups = java.util.Collections.singletonList(group1);
        org.apache.ambari.server.topology.ClusterTopology topology = createClusterTopology(bp, clusterConfig, hostGroups);
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor configProcessor = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor(topology);
        configProcessor.doUpdateForClusterCreate();
        org.junit.Assert.assertEquals(null, clusterConfig.getPropertyValue("hive-site", "atlas.cluster.name"));
        org.junit.Assert.assertEquals(null, clusterConfig.getPropertyValue("hive-site", "atlas.rest.address"));
    }

    @org.junit.Test
    public void testAtlasHiveProperties() throws java.lang.Exception {
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> properties = getAtlasHivePropertiesForTestCase();
        validateAtlasHivePropertiesForTestCase(properties);
    }

    @org.junit.Test
    public void testAtlasHivePropertiesWithHiveHookSpace() throws java.lang.Exception {
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> properties = getAtlasHivePropertiesForTestCase();
        java.util.Map<java.lang.String, java.lang.String> hiveProperties = properties.get("hive-site");
        hiveProperties.put("hive.exec.post.hooks", " ");
        properties.put("hive-site", hiveProperties);
        validateAtlasHivePropertiesForTestCase(properties);
    }

    @org.junit.Test
    public void testAtlasHivePropertiesWithAtlasHookAlreadyExist() throws java.lang.Exception {
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> properties = getAtlasHivePropertiesForTestCase();
        java.util.Map<java.lang.String, java.lang.String> hiveProperties = properties.get("hive-site");
        hiveProperties.put("hive.exec.post.hooks", "org.apache.atlas.hive.hook.HiveHook");
        properties.put("hive-site", hiveProperties);
        validateAtlasHivePropertiesForTestCase(properties);
    }

    private java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> getAtlasHivePropertiesForTestCase() {
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> properties = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.lang.String> atlasProperties = new java.util.HashMap<>();
        atlasProperties.put("atlas.enableTLS", "false");
        atlasProperties.put("atlas.server.bind.address", "localhost");
        atlasProperties.put("atlas.server.http.port", "21000");
        properties.put("application-properties", atlasProperties);
        java.util.Map<java.lang.String, java.lang.String> atlasEnv = new java.util.HashMap<>();
        properties.put("atlas-env", atlasEnv);
        java.util.Map<java.lang.String, java.lang.String> hiveProperties = new java.util.HashMap<>();
        hiveProperties.put("hive.exec.post.hooks", "");
        properties.put("hive-site", hiveProperties);
        java.util.Map<java.lang.String, java.lang.String> hiveEnv = new java.util.HashMap<>();
        properties.put("hive-env", hiveEnv);
        return properties;
    }

    private void validateAtlasHivePropertiesForTestCase(java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> properties) throws java.lang.Exception {
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> parentProperties = new java.util.HashMap<>();
        org.apache.ambari.server.topology.Configuration parentClusterConfig = new org.apache.ambari.server.topology.Configuration(parentProperties, java.util.Collections.emptyMap());
        org.apache.ambari.server.topology.Configuration clusterConfig = new org.apache.ambari.server.topology.Configuration(properties, java.util.Collections.emptyMap(), parentClusterConfig);
        java.util.Collection<java.lang.String> hgComponents1 = new java.util.HashSet<>();
        hgComponents1.add("ATLAS_SERVER");
        hgComponents1.add("HIVE_SERVER");
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup group1 = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup("group1", hgComponents1, java.util.Collections.singleton("host1"));
        java.util.Collection<org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup> hostGroups = java.util.Collections.singletonList(group1);
        org.apache.ambari.server.topology.ClusterTopology topology1 = createClusterTopology(bp, clusterConfig, hostGroups);
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor configProcessor = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor(topology1);
        configProcessor.doUpdateForClusterCreate();
        org.junit.Assert.assertEquals("org.apache.atlas.hive.hook.HiveHook", clusterConfig.getPropertyValue("hive-site", "hive.exec.post.hooks"));
        org.junit.Assert.assertEquals(null, clusterConfig.getPropertyValue("hive-site", "atlas.cluster.name"));
        org.junit.Assert.assertEquals(null, clusterConfig.getPropertyValue("hive-site", "atlas.rest.address"));
        org.junit.Assert.assertEquals("host1", clusterConfig.getPropertyValue("application-properties", "atlas.server.bind.address"));
    }

    @org.junit.Test
    public void testAtlasHivePropertiesWithHTTPS() throws java.lang.Exception {
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> properties = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.lang.String> atlasProperties = new java.util.HashMap<>();
        properties.put("application-properties", atlasProperties);
        atlasProperties.put("atlas.enableTLS", "true");
        atlasProperties.put("atlas.server.bind.address", "localhost");
        atlasProperties.put("atlas.server.https.port", "99999");
        java.util.Map<java.lang.String, java.lang.String> atlasEnv = new java.util.HashMap<>();
        properties.put("atlas-env", atlasEnv);
        java.util.Map<java.lang.String, java.lang.String> hiveProperties = new java.util.HashMap<>();
        hiveProperties.put("hive.exec.post.hooks", "foo");
        properties.put("hive-site", hiveProperties);
        java.util.Map<java.lang.String, java.lang.String> hiveEnv = new java.util.HashMap<>();
        hiveEnv.put("hive.atlas.hook", "false");
        properties.put("hive-env", hiveEnv);
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> parentProperties = new java.util.HashMap<>();
        org.apache.ambari.server.topology.Configuration parentClusterConfig = new org.apache.ambari.server.topology.Configuration(parentProperties, java.util.Collections.emptyMap());
        org.apache.ambari.server.topology.Configuration clusterConfig = new org.apache.ambari.server.topology.Configuration(properties, java.util.Collections.emptyMap(), parentClusterConfig);
        java.util.Collection<java.lang.String> hgComponents1 = new java.util.HashSet<>();
        hgComponents1.add("ATLAS_SERVER");
        hgComponents1.add("HIVE_SERVER");
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup group1 = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup("group1", hgComponents1, java.util.Collections.singleton("host1"));
        java.util.Collection<org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup> hostGroups = java.util.Collections.singletonList(group1);
        org.apache.ambari.server.topology.ClusterTopology topology = createClusterTopology(bp, clusterConfig, hostGroups);
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor configProcessor = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor(topology);
        configProcessor.doUpdateForClusterCreate();
        org.junit.Assert.assertEquals("foo,org.apache.atlas.hive.hook.HiveHook", clusterConfig.getPropertyValue("hive-site", "hive.exec.post.hooks"));
        org.junit.Assert.assertEquals(null, clusterConfig.getPropertyValue("hive-site", "atlas.cluster.name"));
        org.junit.Assert.assertEquals(null, clusterConfig.getPropertyValue("hive-site", "atlas.rest.address"));
    }

    @org.junit.Test
    public void testStormAmsPropertiesDefault() throws java.lang.Exception {
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> properties = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.lang.String> stormSite = new java.util.HashMap<>();
        stormSite.put("metrics.reporter.register", "");
        properties.put("storm-site", stormSite);
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> parentProperties = new java.util.HashMap<>();
        org.apache.ambari.server.topology.Configuration parentClusterConfig = new org.apache.ambari.server.topology.Configuration(parentProperties, java.util.Collections.emptyMap());
        org.apache.ambari.server.topology.Configuration clusterConfig = new org.apache.ambari.server.topology.Configuration(properties, java.util.Collections.emptyMap(), parentClusterConfig);
        java.util.Collection<java.lang.String> hgComponents1 = new java.util.HashSet<>();
        hgComponents1.add("METRICS_COLLECTOR");
        hgComponents1.add("NIMBUS");
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup group1 = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup("group1", hgComponents1, java.util.Collections.singleton("host1"));
        java.util.Collection<org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup> hostGroups = java.util.Collections.singletonList(group1);
        org.apache.ambari.server.topology.ClusterTopology topology = createClusterTopology(bp, clusterConfig, hostGroups);
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor configProcessor = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor(topology);
        configProcessor.doUpdateForClusterCreate();
        org.junit.Assert.assertEquals("org.apache.hadoop.metrics2.sink.storm.StormTimelineMetricsReporter", clusterConfig.getPropertyValue("storm-site", "metrics.reporter.register"));
    }

    @org.junit.Test
    public void testStormAmsPropertiesUserDefinedReporter() throws java.lang.Exception {
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> properties = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.lang.String> stormSite = new java.util.HashMap<>();
        stormSite.put("metrics.reporter.register", "user.Reporter");
        properties.put("storm-site", stormSite);
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> parentProperties = new java.util.HashMap<>();
        org.apache.ambari.server.topology.Configuration parentClusterConfig = new org.apache.ambari.server.topology.Configuration(parentProperties, java.util.Collections.emptyMap());
        org.apache.ambari.server.topology.Configuration clusterConfig = new org.apache.ambari.server.topology.Configuration(properties, java.util.Collections.emptyMap(), parentClusterConfig);
        java.util.Collection<java.lang.String> hgComponents1 = new java.util.HashSet<>();
        hgComponents1.add("METRICS_COLLECTOR");
        hgComponents1.add("NIMBUS");
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup group1 = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup("group1", hgComponents1, java.util.Collections.singleton("host1"));
        java.util.Collection<org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup> hostGroups = java.util.Collections.singletonList(group1);
        org.apache.ambari.server.topology.ClusterTopology topology = createClusterTopology(bp, clusterConfig, hostGroups);
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor configProcessor = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor(topology);
        configProcessor.doUpdateForClusterCreate();
        org.junit.Assert.assertEquals("user.Reporter", clusterConfig.getPropertyValue("storm-site", "metrics.reporter.register"));
    }

    @org.junit.Test
    public void testKafkaAmsProperties() throws java.lang.Exception {
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> properties = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.lang.String> stormSite = new java.util.HashMap<>();
        stormSite.put("kafka.metrics.reporters", "");
        properties.put("kafka-broker", stormSite);
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> parentProperties = new java.util.HashMap<>();
        org.apache.ambari.server.topology.Configuration parentClusterConfig = new org.apache.ambari.server.topology.Configuration(parentProperties, java.util.Collections.emptyMap());
        org.apache.ambari.server.topology.Configuration clusterConfig = new org.apache.ambari.server.topology.Configuration(properties, java.util.Collections.emptyMap(), parentClusterConfig);
        java.util.Collection<java.lang.String> hgComponents1 = new java.util.HashSet<>();
        hgComponents1.add("METRICS_COLLECTOR");
        hgComponents1.add("KAFKA_BROKER");
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup group1 = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup("group1", hgComponents1, java.util.Collections.singleton("host1"));
        java.util.Collection<org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup> hostGroups = java.util.Collections.singletonList(group1);
        org.apache.ambari.server.topology.ClusterTopology topology = createClusterTopology(bp, clusterConfig, hostGroups);
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor configProcessor = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor(topology);
        configProcessor.doUpdateForClusterCreate();
        org.junit.Assert.assertEquals("org.apache.hadoop.metrics2.sink.kafka.KafkaTimelineMetricsReporter", clusterConfig.getPropertyValue("kafka-broker", "kafka.metrics.reporters"));
    }

    @org.junit.Test
    public void testKafkaAmsPropertiesMultipleReporters() throws java.lang.Exception {
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> properties = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.lang.String> stormSite = new java.util.HashMap<>();
        stormSite.put("kafka.metrics.reporters", "user.Reporter");
        properties.put("kafka-broker", stormSite);
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> parentProperties = new java.util.HashMap<>();
        org.apache.ambari.server.topology.Configuration parentClusterConfig = new org.apache.ambari.server.topology.Configuration(parentProperties, java.util.Collections.emptyMap());
        org.apache.ambari.server.topology.Configuration clusterConfig = new org.apache.ambari.server.topology.Configuration(properties, java.util.Collections.emptyMap(), parentClusterConfig);
        java.util.Collection<java.lang.String> hgComponents1 = new java.util.HashSet<>();
        hgComponents1.add("METRICS_COLLECTOR");
        hgComponents1.add("KAFKA_BROKER");
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup group1 = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup("group1", hgComponents1, java.util.Collections.singleton("host1"));
        java.util.Collection<org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup> hostGroups = java.util.Collections.singletonList(group1);
        org.apache.ambari.server.topology.ClusterTopology topology = createClusterTopology(bp, clusterConfig, hostGroups);
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor configProcessor = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor(topology);
        configProcessor.doUpdateForClusterCreate();
        org.junit.Assert.assertEquals("user.Reporter,org.apache.hadoop.metrics2.sink.kafka.KafkaTimelineMetricsReporter", clusterConfig.getPropertyValue("kafka-broker", "kafka.metrics.reporters"));
    }

    @org.junit.Test
    public void testRecommendConfiguration_applyStackDefaultsOnly() throws java.lang.Exception {
        final java.lang.String expectedHostName = "c6401.apache.ambari.org";
        final java.lang.String expectedPortNum = "808080";
        final java.lang.String expectedHostGroupName = "host_group_1";
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> properties = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.lang.String> coreSiteMap = new java.util.HashMap<>();
        properties.put("core-site", coreSiteMap);
        coreSiteMap.put("fs.default.name", (expectedHostName + ":") + expectedPortNum);
        coreSiteMap.put("fs.defaultFS", (("hdfs://" + expectedHostName) + ":") + expectedPortNum);
        coreSiteMap.put("fs.stackDefault.key2", "dummyValue");
        java.util.Map<java.lang.String, java.lang.String> dummySiteMap = new java.util.HashMap<>();
        properties.put("dummy-site", dummySiteMap);
        dummySiteMap.put("dummy.prop", "dummyValue2");
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> parentProperties = new java.util.HashMap<>();
        java.util.Collection<java.lang.String> hgComponents = new java.util.HashSet<>();
        hgComponents.add("NAMENODE");
        hgComponents.add("SECONDARY_NAMENODE");
        hgComponents.add("RESOURCEMANAGER");
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup group1 = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup("group1", hgComponents, java.util.Collections.singleton("testhost"));
        java.util.Collection<java.lang.String> hgComponents2 = new java.util.HashSet<>();
        hgComponents2.add("DATANODE");
        hgComponents2.add("HDFS_CLIENT");
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup group2 = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup("group2", hgComponents2, java.util.Collections.singleton(expectedHostGroupName));
        java.util.Collection<org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup> hostGroups = new java.util.HashSet<>();
        hostGroups.add(group1);
        hostGroups.add(group2);
        org.apache.ambari.server.topology.Configuration parentConfig = new org.apache.ambari.server.topology.Configuration(parentProperties, java.util.Collections.emptyMap(), createStackDefaults());
        org.apache.ambari.server.topology.Configuration clusterConfig = new org.apache.ambari.server.topology.Configuration(properties, java.util.Collections.emptyMap(), parentConfig);
        org.apache.ambari.server.topology.ClusterTopology topology = createClusterTopology(bp, clusterConfig, hostGroups);
        topology.getAdvisedConfigurations().putAll(createAdvisedConfigMap());
        topology.setConfigRecommendationStrategy(org.apache.ambari.server.topology.ConfigRecommendationStrategy.ONLY_STACK_DEFAULTS_APPLY);
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor configProcessor = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor(topology);
        EasyMock.reset(stack);
        EasyMock.expect(stack.getName()).andReturn(STACK_NAME).anyTimes();
        EasyMock.expect(stack.getVersion()).andReturn(STACK_VERSION).anyTimes();
        EasyMock.expect(stack.getConfiguration(bp.getServices())).andReturn(createStackDefaults()).anyTimes();
        java.util.Set<java.lang.String> emptySet = java.util.Collections.emptySet();
        EasyMock.expect(stack.getExcludedConfigurationTypes(EasyMock.anyObject(java.lang.String.class))).andReturn(emptySet).anyTimes();
        EasyMock.replay(stack);
        java.util.Set<java.lang.String> configTypeUpdated = configProcessor.doUpdateForClusterCreate();
        org.junit.Assert.assertEquals((expectedHostName + ":") + expectedPortNum, clusterConfig.getPropertyValue("core-site", "fs.default.name"));
        org.junit.Assert.assertEquals("stackDefaultUpgraded", clusterConfig.getPropertyValue("core-site", "fs.stackDefault.key1"));
        org.junit.Assert.assertNull(clusterConfig.getPropertyValue("core-site", "fs.stackDefault.key2"));
        org.junit.Assert.assertNull(clusterConfig.getPropertyValue("core-site", "fs.notStackDefault"));
        org.junit.Assert.assertTrue(configTypeUpdated.contains("dummy-site"));
    }

    @org.junit.Test
    public void testRecommendConfiguration_EmptyConfiguration_applyStackDefaultsOnly() throws java.lang.Exception {
        final java.lang.String expectedHostGroupName = "host_group_1";
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> properties = new java.util.HashMap<>();
        java.util.Collection<java.lang.String> hgComponents = new java.util.HashSet<>();
        hgComponents.add("NAMENODE");
        hgComponents.add("SECONDARY_NAMENODE");
        hgComponents.add("RESOURCEMANAGER");
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup group1 = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup("group1", hgComponents, java.util.Collections.singleton("testhost"));
        java.util.Collection<java.lang.String> hgComponents2 = new java.util.HashSet<>();
        hgComponents2.add("DATANODE");
        hgComponents2.add("HDFS_CLIENT");
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup group2 = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup("group2", hgComponents2, java.util.Collections.singleton(expectedHostGroupName));
        java.util.Collection<org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup> hostGroups = new java.util.HashSet<>();
        hostGroups.add(group1);
        hostGroups.add(group2);
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> parentProperties = new java.util.HashMap<>();
        org.apache.ambari.server.topology.Configuration parentConfig = new org.apache.ambari.server.topology.Configuration(parentProperties, java.util.Collections.emptyMap(), createStackDefaults());
        org.apache.ambari.server.topology.Configuration clusterConfig = new org.apache.ambari.server.topology.Configuration(properties, java.util.Collections.emptyMap(), parentConfig);
        org.apache.ambari.server.topology.ClusterTopology topology = createClusterTopology(bp, clusterConfig, hostGroups);
        topology.getAdvisedConfigurations().putAll(createAdvisedConfigMap());
        topology.setConfigRecommendationStrategy(org.apache.ambari.server.topology.ConfigRecommendationStrategy.ONLY_STACK_DEFAULTS_APPLY);
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor configProcessor = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor(topology);
        EasyMock.reset(stack);
        EasyMock.expect(stack.getName()).andReturn(STACK_NAME).anyTimes();
        EasyMock.expect(stack.getVersion()).andReturn(STACK_VERSION).anyTimes();
        EasyMock.expect(stack.getConfiguration(bp.getServices())).andReturn(createStackDefaults()).anyTimes();
        java.util.Set<java.lang.String> emptySet = java.util.Collections.emptySet();
        EasyMock.expect(stack.getExcludedConfigurationTypes(EasyMock.anyObject(java.lang.String.class))).andReturn(emptySet).anyTimes();
        EasyMock.replay(stack);
        configProcessor.doUpdateForClusterCreate();
        org.junit.Assert.assertEquals("stackDefaultUpgraded", clusterConfig.getPropertyValue("core-site", "fs.stackDefault.key1"));
        org.junit.Assert.assertNull(clusterConfig.getPropertyValue("core-site", "fs.stackDefault.key2"));
        org.junit.Assert.assertNull(clusterConfig.getPropertyValue("core-site", "fs.notStackDefault"));
    }

    @org.junit.Test
    public void testRecommendConfiguration_applyAlways() throws java.lang.Exception {
        final java.lang.String expectedHostName = "c6401.apache.ambari.org";
        final java.lang.String expectedPortNum = "808080";
        final java.lang.String expectedHostGroupName = "host_group_1";
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> properties = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.lang.String> coreSiteMap = new java.util.HashMap<>();
        properties.put("core-site", coreSiteMap);
        coreSiteMap.put("fs.default.name", (expectedHostName + ":") + expectedPortNum);
        coreSiteMap.put("fs.defaultFS", (("hdfs://" + expectedHostName) + ":") + expectedPortNum);
        coreSiteMap.put("fs.stackDefault.key2", "dummyValue");
        java.util.Map<java.lang.String, java.lang.String> dummySiteMap = new java.util.HashMap<>();
        properties.put("dummy-site", dummySiteMap);
        dummySiteMap.put("dummy.prop", "dummyValue");
        java.util.Map<java.lang.String, java.lang.String> dummy2SiteMap = new java.util.HashMap<>();
        properties.put("dummy2-site", dummy2SiteMap);
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> parentProperties = new java.util.HashMap<>();
        java.util.Collection<java.lang.String> hgComponents = new java.util.HashSet<>();
        hgComponents.add("NAMENODE");
        hgComponents.add("SECONDARY_NAMENODE");
        hgComponents.add("RESOURCEMANAGER");
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup group1 = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup("group1", hgComponents, java.util.Collections.singleton("testhost"));
        java.util.Collection<java.lang.String> hgComponents2 = new java.util.HashSet<>();
        hgComponents2.add("DATANODE");
        hgComponents2.add("HDFS_CLIENT");
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup group2 = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup("group2", hgComponents2, java.util.Collections.singleton(expectedHostGroupName));
        java.util.Collection<org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup> hostGroups = new java.util.HashSet<>();
        hostGroups.add(group1);
        hostGroups.add(group2);
        org.apache.ambari.server.topology.Configuration parentClusterConfig = new org.apache.ambari.server.topology.Configuration(parentProperties, java.util.Collections.emptyMap(), createStackDefaults());
        org.apache.ambari.server.topology.Configuration clusterConfig = new org.apache.ambari.server.topology.Configuration(properties, java.util.Collections.emptyMap(), parentClusterConfig);
        org.apache.ambari.server.topology.ClusterTopology topology = createClusterTopology(bp, clusterConfig, hostGroups);
        topology.getAdvisedConfigurations().putAll(createAdvisedConfigMap());
        topology.setConfigRecommendationStrategy(org.apache.ambari.server.topology.ConfigRecommendationStrategy.ALWAYS_APPLY);
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor configProcessor = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor(topology);
        java.util.Set<java.lang.String> configTypes = configProcessor.doUpdateForClusterCreate();
        org.junit.Assert.assertEquals((expectedHostName + ":") + expectedPortNum, clusterConfig.getPropertyValue("core-site", "fs.default.name"));
        org.junit.Assert.assertEquals("stackDefaultUpgraded", clusterConfig.getPropertyValue("core-site", "fs.stackDefault.key1"));
        org.junit.Assert.assertNull(clusterConfig.getPropertyValue("core-site", "fs.stackDefault.key2"));
        org.junit.Assert.assertNotNull(clusterConfig.getPropertyValue("core-site", "fs.notStackDefault"));
        org.junit.Assert.assertEquals(3, topology.getAdvisedConfigurations().size());
        org.junit.Assert.assertFalse(configTypes.contains("dummy-site"));
        org.junit.Assert.assertFalse(configTypes.contains("dummy2-site"));
    }

    @org.junit.Test
    public void testRecommendConfiguration_neverApply() throws java.lang.Exception {
        final java.lang.String expectedHostName = "c6401.apache.ambari.org";
        final java.lang.String expectedPortNum = "808080";
        final java.lang.String expectedHostGroupName = "host_group_1";
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> properties = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.lang.String> coreSiteMap = new java.util.HashMap<>();
        properties.put("core-site", coreSiteMap);
        coreSiteMap.put("fs.default.name", (expectedHostName + ":") + expectedPortNum);
        coreSiteMap.put("fs.defaultFS", (("hdfs://" + expectedHostName) + ":") + expectedPortNum);
        coreSiteMap.put("fs.stackDefault.key2", "dummyValue");
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> parentProperties = new java.util.HashMap<>();
        java.util.Collection<java.lang.String> hgComponents = new java.util.HashSet<>();
        hgComponents.add("NAMENODE");
        hgComponents.add("SECONDARY_NAMENODE");
        hgComponents.add("RESOURCEMANAGER");
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup group1 = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup("group1", hgComponents, java.util.Collections.singleton("testhost"));
        java.util.Collection<java.lang.String> hgComponents2 = new java.util.HashSet<>();
        hgComponents2.add("DATANODE");
        hgComponents2.add("HDFS_CLIENT");
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup group2 = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup("group2", hgComponents2, java.util.Collections.singleton(expectedHostGroupName));
        java.util.Collection<org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup> hostGroups = new java.util.HashSet<>();
        hostGroups.add(group1);
        hostGroups.add(group2);
        org.apache.ambari.server.topology.Configuration parentClusterConfig = new org.apache.ambari.server.topology.Configuration(parentProperties, java.util.Collections.emptyMap(), createStackDefaults());
        org.apache.ambari.server.topology.Configuration clusterConfig = new org.apache.ambari.server.topology.Configuration(properties, java.util.Collections.emptyMap(), parentClusterConfig);
        org.apache.ambari.server.topology.ClusterTopology topology = createClusterTopology(bp, clusterConfig, hostGroups);
        topology.getAdvisedConfigurations().putAll(createAdvisedConfigMap());
        topology.setConfigRecommendationStrategy(org.apache.ambari.server.topology.ConfigRecommendationStrategy.NEVER_APPLY);
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor configProcessor = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor(topology);
        configProcessor.doUpdateForClusterCreate();
        org.junit.Assert.assertEquals((expectedHostName + ":") + expectedPortNum, clusterConfig.getPropertyValue("core-site", "fs.default.name"));
        org.junit.Assert.assertNull(clusterConfig.getPropertyValue("core-site", "fs.notStackDefault"));
        org.junit.Assert.assertEquals("stackDefaultValue1", clusterConfig.getPropertyValue("core-site", "fs.stackDefault.key1"));
        org.junit.Assert.assertNotNull(clusterConfig.getPropertyValue("core-site", "fs.stackDefault.key2"));
    }

    @org.junit.Test
    public void testRangerAdminProperties() throws java.lang.Exception {
        final java.lang.String rangerAdminConfigType = "admin-properties";
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> properties = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.lang.String> rangerAdminProperties = new java.util.HashMap<>();
        properties.put(rangerAdminConfigType, rangerAdminProperties);
        rangerAdminProperties.put("policymgr_external_url", "http://%HOSTGROUP::group1%:100");
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> parentProperties = new java.util.HashMap<>();
        org.apache.ambari.server.topology.Configuration parentClusterConfig = new org.apache.ambari.server.topology.Configuration(parentProperties, java.util.Collections.emptyMap());
        org.apache.ambari.server.topology.Configuration clusterConfig = new org.apache.ambari.server.topology.Configuration(properties, java.util.Collections.emptyMap(), parentClusterConfig);
        java.util.Collection<java.lang.String> rangerComponents = new java.util.HashSet<>();
        rangerComponents.add("RANGER_ADMIN");
        rangerComponents.add("RANGER_USERSYNC");
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup group1 = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup("group1", rangerComponents, java.util.Collections.singleton("host1"));
        java.util.Collection<org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup> hostGroups = java.util.Collections.singletonList(group1);
        org.apache.ambari.server.topology.ClusterTopology topology = createClusterTopology(bp, clusterConfig, hostGroups);
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor configProcessor = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor(topology);
        configProcessor.doUpdateForClusterCreate();
        org.junit.Assert.assertEquals("http://host1:100", clusterConfig.getPropertyValue(rangerAdminConfigType, "policymgr_external_url"));
    }

    @org.junit.Test
    public void testRangerAdminProperties_defaults() throws java.lang.Exception {
        final java.lang.String rangerAdminConfigType = "admin-properties";
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> properties = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.lang.String> rangerAdminProperties = new java.util.HashMap<>();
        properties.put(rangerAdminConfigType, rangerAdminProperties);
        rangerAdminProperties.put("policymgr_external_url", "http://localhost:100");
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> parentProperties = new java.util.HashMap<>();
        org.apache.ambari.server.topology.Configuration parentClusterConfig = new org.apache.ambari.server.topology.Configuration(parentProperties, java.util.Collections.emptyMap());
        org.apache.ambari.server.topology.Configuration clusterConfig = new org.apache.ambari.server.topology.Configuration(properties, java.util.Collections.emptyMap(), parentClusterConfig);
        java.util.Collection<java.lang.String> rangerComponents = new java.util.HashSet<>();
        rangerComponents.add("RANGER_ADMIN");
        rangerComponents.add("RANGER_USERSYNC");
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup group1 = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup("group1", rangerComponents, java.util.Collections.singleton("host1"));
        java.util.Collection<org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup> hostGroups = java.util.Collections.singletonList(group1);
        org.apache.ambari.server.topology.ClusterTopology topology = createClusterTopology(bp, clusterConfig, hostGroups);
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor configProcessor = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor(topology);
        configProcessor.doUpdateForClusterCreate();
        org.junit.Assert.assertEquals("http://host1:100", clusterConfig.getPropertyValue(rangerAdminConfigType, "policymgr_external_url"));
    }

    @org.junit.Test
    public void testRangerAdminProperties_HA() throws java.lang.Exception {
        final java.lang.String rangerAdminConfigType = "admin-properties";
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> properties = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.lang.String> rangerAdminProperties = new java.util.HashMap<>();
        properties.put(rangerAdminConfigType, rangerAdminProperties);
        rangerAdminProperties.put("policymgr_external_url", "http://my.ranger.loadbalancer.com");
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> parentProperties = new java.util.HashMap<>();
        org.apache.ambari.server.topology.Configuration parentClusterConfig = new org.apache.ambari.server.topology.Configuration(parentProperties, java.util.Collections.emptyMap());
        org.apache.ambari.server.topology.Configuration clusterConfig = new org.apache.ambari.server.topology.Configuration(properties, java.util.Collections.emptyMap(), parentClusterConfig);
        java.util.Collection<java.lang.String> rangerComponents = new java.util.HashSet<>();
        rangerComponents.add("RANGER_ADMIN");
        rangerComponents.add("RANGER_USERSYNC");
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup group1 = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup("group1", rangerComponents, java.util.Collections.singleton("host1"));
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup group2 = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup("group2", rangerComponents, java.util.Collections.singleton("host2"));
        java.util.Collection<org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup> hostGroups = com.google.common.collect.Lists.newArrayList(group1, group2);
        org.apache.ambari.server.topology.ClusterTopology topology = createClusterTopology(bp, clusterConfig, hostGroups);
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor configProcessor = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor(topology);
        configProcessor.doUpdateForClusterCreate();
        org.junit.Assert.assertEquals("http://my.ranger.loadbalancer.com", clusterConfig.getPropertyValue(rangerAdminConfigType, "policymgr_external_url"));
    }

    @org.junit.Test
    public void testRangerEnv_defaults() throws java.lang.Exception {
        java.util.List<java.lang.String> configTypesWithRangerHdfsAuditDir = com.google.common.collect.ImmutableList.of("ranger-env", "ranger-yarn-audit", "ranger-hdfs-audit", "ranger-hbase-audit", "ranger-hive-audit", "ranger-knox-audit", "ranger-kafka-audit", "ranger-storm-audit", "ranger-atlas-audit");
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> clusterConfigProperties = new java.util.HashMap<>();
        for (java.lang.String configType : configTypesWithRangerHdfsAuditDir) {
            java.util.Map<java.lang.String, java.lang.String> configProperties = new java.util.HashMap<>();
            configProperties.put("xasecure.audit.destination.hdfs.dir", "hdfs://localhost:100");
            clusterConfigProperties.put(configType, configProperties);
        }
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> parentProperties = new java.util.HashMap<>();
        org.apache.ambari.server.topology.Configuration parentClusterConfig = new org.apache.ambari.server.topology.Configuration(parentProperties, new java.util.HashMap<>());
        org.apache.ambari.server.topology.Configuration clusterConfig = new org.apache.ambari.server.topology.Configuration(clusterConfigProperties, new java.util.HashMap<>(), parentClusterConfig);
        java.util.Collection<java.lang.String> rangerComponents = new java.util.HashSet<>();
        rangerComponents.add("RANGER_ADMIN");
        rangerComponents.add("RANGER_USERSYNC");
        java.util.Collection<java.lang.String> hdfsComponents = new java.util.HashSet<>();
        hdfsComponents.add("NAMENODE");
        hdfsComponents.add("DATANODE");
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup group1 = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup("group1", rangerComponents, java.util.Collections.singleton("host1"));
        group1.components.add("DATANODE");
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup group2 = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup("group2", hdfsComponents, java.util.Collections.singleton("nn_host"));
        java.util.Collection<org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup> hostGroups = com.google.common.collect.Lists.newArrayList(group1, group2);
        org.apache.ambari.server.topology.ClusterTopology topology = createClusterTopology(bp, clusterConfig, hostGroups);
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor configProcessor = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor(topology);
        configProcessor.doUpdateForClusterCreate();
        java.lang.String expectedAuditHdfsDir = "hdfs://nn_host:100";
        org.junit.Assert.assertEquals(expectedAuditHdfsDir, clusterConfig.getPropertyValue("ranger-env", "xasecure.audit.destination.hdfs.dir"));
        org.junit.Assert.assertEquals(expectedAuditHdfsDir, clusterConfig.getPropertyValue("ranger-yarn-audit", "xasecure.audit.destination.hdfs.dir"));
        org.junit.Assert.assertEquals(expectedAuditHdfsDir, clusterConfig.getPropertyValue("ranger-hdfs-audit", "xasecure.audit.destination.hdfs.dir"));
        org.junit.Assert.assertEquals(expectedAuditHdfsDir, clusterConfig.getPropertyValue("ranger-hbase-audit", "xasecure.audit.destination.hdfs.dir"));
        org.junit.Assert.assertEquals(expectedAuditHdfsDir, clusterConfig.getPropertyValue("ranger-hive-audit", "xasecure.audit.destination.hdfs.dir"));
        org.junit.Assert.assertEquals(expectedAuditHdfsDir, clusterConfig.getPropertyValue("ranger-knox-audit", "xasecure.audit.destination.hdfs.dir"));
        org.junit.Assert.assertEquals(expectedAuditHdfsDir, clusterConfig.getPropertyValue("ranger-kafka-audit", "xasecure.audit.destination.hdfs.dir"));
        org.junit.Assert.assertEquals(expectedAuditHdfsDir, clusterConfig.getPropertyValue("ranger-storm-audit", "xasecure.audit.destination.hdfs.dir"));
        org.junit.Assert.assertEquals(expectedAuditHdfsDir, clusterConfig.getPropertyValue("ranger-atlas-audit", "xasecure.audit.destination.hdfs.dir"));
    }

    @org.junit.Test
    public void testRangerEnv_defaults_NO_HDFS() throws java.lang.Exception {
        java.util.List<java.lang.String> configTypesWithRangerHdfsAuditDir = com.google.common.collect.ImmutableList.of("ranger-env", "ranger-yarn-audit", "ranger-hdfs-audit", "ranger-hbase-audit", "ranger-hive-audit", "ranger-knox-audit", "ranger-kafka-audit", "ranger-storm-audit", "ranger-atlas-audit");
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> clusterConfigProperties = new java.util.HashMap<>();
        for (java.lang.String configType : configTypesWithRangerHdfsAuditDir) {
            java.util.Map<java.lang.String, java.lang.String> configProperties = new java.util.HashMap<>();
            configProperties.put("xasecure.audit.destination.hdfs.dir", "hdfs://localhost:100");
            clusterConfigProperties.put(configType, configProperties);
        }
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> parentProperties = new java.util.HashMap<>();
        org.apache.ambari.server.topology.Configuration parentClusterConfig = new org.apache.ambari.server.topology.Configuration(parentProperties, new java.util.HashMap<>());
        org.apache.ambari.server.topology.Configuration clusterConfig = new org.apache.ambari.server.topology.Configuration(clusterConfigProperties, new java.util.HashMap<>(), parentClusterConfig);
        java.util.Collection<java.lang.String> rangerComponents = new java.util.HashSet<>();
        rangerComponents.add("RANGER_ADMIN");
        rangerComponents.add("RANGER_USERSYNC");
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup group1 = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup("group1", rangerComponents, java.util.Collections.singleton("host1"));
        group1.components.add("OOZIE_SERVER");
        EasyMock.expect(stack.getCardinality("NAMENODE")).andReturn(new org.apache.ambari.server.topology.Cardinality("1+")).anyTimes();
        java.util.Collection<org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup> hostGroups = com.google.common.collect.Lists.newArrayList(group1);
        org.apache.ambari.server.topology.ClusterTopology topology = createClusterTopology(bp, clusterConfig, hostGroups);
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor configProcessor = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor(topology);
        configProcessor.doUpdateForClusterCreate();
        java.lang.String expectedAuditHdfsDir = "hdfs://localhost:100";
        org.junit.Assert.assertEquals(expectedAuditHdfsDir, clusterConfig.getPropertyValue("ranger-env", "xasecure.audit.destination.hdfs.dir"));
        org.junit.Assert.assertEquals(expectedAuditHdfsDir, clusterConfig.getPropertyValue("ranger-yarn-audit", "xasecure.audit.destination.hdfs.dir"));
        org.junit.Assert.assertEquals(expectedAuditHdfsDir, clusterConfig.getPropertyValue("ranger-hdfs-audit", "xasecure.audit.destination.hdfs.dir"));
        org.junit.Assert.assertEquals(expectedAuditHdfsDir, clusterConfig.getPropertyValue("ranger-hbase-audit", "xasecure.audit.destination.hdfs.dir"));
        org.junit.Assert.assertEquals(expectedAuditHdfsDir, clusterConfig.getPropertyValue("ranger-hive-audit", "xasecure.audit.destination.hdfs.dir"));
        org.junit.Assert.assertEquals(expectedAuditHdfsDir, clusterConfig.getPropertyValue("ranger-knox-audit", "xasecure.audit.destination.hdfs.dir"));
        org.junit.Assert.assertEquals(expectedAuditHdfsDir, clusterConfig.getPropertyValue("ranger-kafka-audit", "xasecure.audit.destination.hdfs.dir"));
        org.junit.Assert.assertEquals(expectedAuditHdfsDir, clusterConfig.getPropertyValue("ranger-storm-audit", "xasecure.audit.destination.hdfs.dir"));
        org.junit.Assert.assertEquals(expectedAuditHdfsDir, clusterConfig.getPropertyValue("ranger-atlas-audit", "xasecure.audit.destination.hdfs.dir"));
    }

    @org.junit.Test
    public void testRangerEnv() throws java.lang.Exception {
        java.util.List<java.lang.String> configTypesWithRangerHdfsAuditDir = com.google.common.collect.ImmutableList.of("ranger-env", "ranger-yarn-audit", "ranger-hdfs-audit", "ranger-hbase-audit", "ranger-hive-audit", "ranger-knox-audit", "ranger-kafka-audit", "ranger-storm-audit", "ranger-atlas-audit");
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> clusterConfigProperties = new java.util.HashMap<>();
        for (java.lang.String configType : configTypesWithRangerHdfsAuditDir) {
            java.util.Map<java.lang.String, java.lang.String> configProperties = new java.util.HashMap<>();
            configProperties.put("xasecure.audit.destination.hdfs.dir", "hdfs://%HOSTGROUP::group2%:100");
            clusterConfigProperties.put(configType, configProperties);
        }
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> parentProperties = new java.util.HashMap<>();
        org.apache.ambari.server.topology.Configuration parentClusterConfig = new org.apache.ambari.server.topology.Configuration(parentProperties, new java.util.HashMap<>());
        org.apache.ambari.server.topology.Configuration clusterConfig = new org.apache.ambari.server.topology.Configuration(clusterConfigProperties, new java.util.HashMap<>(), parentClusterConfig);
        java.util.Collection<java.lang.String> rangerComponents = new java.util.HashSet<>();
        rangerComponents.add("RANGER_ADMIN");
        rangerComponents.add("RANGER_USERSYNC");
        java.util.Collection<java.lang.String> hdfsComponents = new java.util.HashSet<>();
        hdfsComponents.add("NAMENODE");
        hdfsComponents.add("DATANODE");
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup group1 = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup("group1", rangerComponents, java.util.Collections.singleton("host1"));
        group1.components.add("DATANODE");
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup group2 = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup("group2", hdfsComponents, java.util.Collections.singleton("nn_host"));
        java.util.Collection<org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup> hostGroups = com.google.common.collect.Lists.newArrayList(group1, group2);
        org.apache.ambari.server.topology.ClusterTopology topology = createClusterTopology(bp, clusterConfig, hostGroups);
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor configProcessor = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor(topology);
        configProcessor.doUpdateForClusterCreate();
        java.lang.String expectedAuditHdfsDir = "hdfs://nn_host:100";
        org.junit.Assert.assertEquals(expectedAuditHdfsDir, clusterConfig.getPropertyValue("ranger-env", "xasecure.audit.destination.hdfs.dir"));
        org.junit.Assert.assertEquals(expectedAuditHdfsDir, clusterConfig.getPropertyValue("ranger-yarn-audit", "xasecure.audit.destination.hdfs.dir"));
        org.junit.Assert.assertEquals(expectedAuditHdfsDir, clusterConfig.getPropertyValue("ranger-hdfs-audit", "xasecure.audit.destination.hdfs.dir"));
        org.junit.Assert.assertEquals(expectedAuditHdfsDir, clusterConfig.getPropertyValue("ranger-hbase-audit", "xasecure.audit.destination.hdfs.dir"));
        org.junit.Assert.assertEquals(expectedAuditHdfsDir, clusterConfig.getPropertyValue("ranger-hive-audit", "xasecure.audit.destination.hdfs.dir"));
        org.junit.Assert.assertEquals(expectedAuditHdfsDir, clusterConfig.getPropertyValue("ranger-knox-audit", "xasecure.audit.destination.hdfs.dir"));
        org.junit.Assert.assertEquals(expectedAuditHdfsDir, clusterConfig.getPropertyValue("ranger-kafka-audit", "xasecure.audit.destination.hdfs.dir"));
        org.junit.Assert.assertEquals(expectedAuditHdfsDir, clusterConfig.getPropertyValue("ranger-storm-audit", "xasecure.audit.destination.hdfs.dir"));
        org.junit.Assert.assertEquals(expectedAuditHdfsDir, clusterConfig.getPropertyValue("ranger-atlas-audit", "xasecure.audit.destination.hdfs.dir"));
    }

    @org.junit.Test
    public void testRangerEnvWithHdfsHA() throws java.lang.Exception {
        java.util.List<java.lang.String> configTypesWithRangerHdfsAuditDir = com.google.common.collect.ImmutableList.of("ranger-env", "ranger-yarn-audit", "ranger-hdfs-audit", "ranger-hbase-audit", "ranger-hive-audit", "ranger-knox-audit", "ranger-kafka-audit", "ranger-storm-audit", "ranger-atlas-audit");
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> clusterConfigProperties = new java.util.HashMap<>();
        for (java.lang.String configType : configTypesWithRangerHdfsAuditDir) {
            java.util.Map<java.lang.String, java.lang.String> configProperties = new java.util.HashMap<>();
            configProperties.put("xasecure.audit.destination.hdfs.dir", "hdfs://my_name_service:100");
            clusterConfigProperties.put(configType, configProperties);
        }
        final java.lang.String hdfsSiteConfigType = "hdfs-site";
        java.util.Map<java.lang.String, java.lang.String> hdfsSiteProperties = new java.util.HashMap<>();
        clusterConfigProperties.put(hdfsSiteConfigType, hdfsSiteProperties);
        hdfsSiteProperties.put("dfs.nameservices", "my_name_service");
        hdfsSiteProperties.put("dfs.ha.namenodes.my_name_service", "nn1,nn2");
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> parentProperties = new java.util.HashMap<>();
        org.apache.ambari.server.topology.Configuration parentClusterConfig = new org.apache.ambari.server.topology.Configuration(parentProperties, new java.util.HashMap<>());
        org.apache.ambari.server.topology.Configuration clusterConfig = new org.apache.ambari.server.topology.Configuration(clusterConfigProperties, new java.util.HashMap<>(), parentClusterConfig);
        java.util.Collection<java.lang.String> rangerComponents = new java.util.HashSet<>();
        rangerComponents.add("RANGER_ADMIN");
        rangerComponents.add("RANGER_USERSYNC");
        java.util.Collection<java.lang.String> hdfsComponents = new java.util.HashSet<>();
        hdfsComponents.add("NAMENODE");
        hdfsComponents.add("DATANODE");
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup group1 = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup("group1", rangerComponents, java.util.Collections.singleton("host1"));
        group1.components.addAll(hdfsComponents);
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup group2 = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup("group2", hdfsComponents, java.util.Collections.singleton("host2"));
        java.util.Collection<org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup> hostGroups = com.google.common.collect.Lists.newArrayList(group1, group2);
        org.apache.ambari.server.topology.ClusterTopology topology = createClusterTopology(bp, clusterConfig, hostGroups);
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor configProcessor = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor(topology);
        configProcessor.doUpdateForClusterCreate();
        java.lang.String expectedAuditHdfsDir = "hdfs://my_name_service:100";
        org.junit.Assert.assertEquals(expectedAuditHdfsDir, clusterConfig.getPropertyValue("ranger-env", "xasecure.audit.destination.hdfs.dir"));
        org.junit.Assert.assertEquals(expectedAuditHdfsDir, clusterConfig.getPropertyValue("ranger-yarn-audit", "xasecure.audit.destination.hdfs.dir"));
        org.junit.Assert.assertEquals(expectedAuditHdfsDir, clusterConfig.getPropertyValue("ranger-hdfs-audit", "xasecure.audit.destination.hdfs.dir"));
        org.junit.Assert.assertEquals(expectedAuditHdfsDir, clusterConfig.getPropertyValue("ranger-hbase-audit", "xasecure.audit.destination.hdfs.dir"));
        org.junit.Assert.assertEquals(expectedAuditHdfsDir, clusterConfig.getPropertyValue("ranger-hive-audit", "xasecure.audit.destination.hdfs.dir"));
        org.junit.Assert.assertEquals(expectedAuditHdfsDir, clusterConfig.getPropertyValue("ranger-knox-audit", "xasecure.audit.destination.hdfs.dir"));
        org.junit.Assert.assertEquals(expectedAuditHdfsDir, clusterConfig.getPropertyValue("ranger-kafka-audit", "xasecure.audit.destination.hdfs.dir"));
        org.junit.Assert.assertEquals(expectedAuditHdfsDir, clusterConfig.getPropertyValue("ranger-storm-audit", "xasecure.audit.destination.hdfs.dir"));
        org.junit.Assert.assertEquals(expectedAuditHdfsDir, clusterConfig.getPropertyValue("ranger-atlas-audit", "xasecure.audit.destination.hdfs.dir"));
    }

    @org.junit.Test
    public void testRangerEnvBlueprintExport() throws java.lang.Exception {
        java.util.List<java.lang.String> configTypesWithRangerHdfsAuditDir = com.google.common.collect.ImmutableList.of("ranger-env", "ranger-yarn-audit", "ranger-hdfs-audit", "ranger-hbase-audit", "ranger-hive-audit", "ranger-knox-audit", "ranger-kafka-audit", "ranger-storm-audit", "ranger-atlas-audit");
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> clusterConfigProperties = new java.util.HashMap<>();
        for (java.lang.String configType : configTypesWithRangerHdfsAuditDir) {
            java.util.Map<java.lang.String, java.lang.String> configProperties = new java.util.HashMap<>();
            configProperties.put("xasecure.audit.destination.hdfs.dir", "hdfs://nn_host:100");
            clusterConfigProperties.put(configType, configProperties);
        }
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> parentProperties = new java.util.HashMap<>();
        org.apache.ambari.server.topology.Configuration parentClusterConfig = new org.apache.ambari.server.topology.Configuration(parentProperties, new java.util.HashMap<>());
        org.apache.ambari.server.topology.Configuration clusterConfig = new org.apache.ambari.server.topology.Configuration(clusterConfigProperties, new java.util.HashMap<>(), parentClusterConfig);
        java.util.Collection<java.lang.String> rangerComponents = new java.util.HashSet<>();
        rangerComponents.add("RANGER_ADMIN");
        rangerComponents.add("RANGER_USERSYNC");
        java.util.Collection<java.lang.String> hdfsComponents = new java.util.HashSet<>();
        hdfsComponents.add("NAMENODE");
        hdfsComponents.add("DATANODE");
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup group1 = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup("group1", rangerComponents, java.util.Collections.singleton("host1"));
        group1.components.add("DATANODE");
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup group2 = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup("group2", hdfsComponents, java.util.Collections.singleton("nn_host"));
        java.util.Collection<org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup> hostGroups = com.google.common.collect.Lists.newArrayList(group1, group2);
        org.apache.ambari.server.topology.ClusterTopology topology = createClusterTopology(bp, clusterConfig, hostGroups);
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor configProcessor = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor(topology);
        configProcessor.doUpdateForBlueprintExport(org.apache.ambari.server.controller.internal.BlueprintExportType.FULL);
        java.lang.String expectedAuditHdfsDir = "hdfs://%HOSTGROUP::group2%:100";
        org.junit.Assert.assertEquals(expectedAuditHdfsDir, clusterConfig.getPropertyValue("ranger-env", "xasecure.audit.destination.hdfs.dir"));
        org.junit.Assert.assertEquals(expectedAuditHdfsDir, clusterConfig.getPropertyValue("ranger-yarn-audit", "xasecure.audit.destination.hdfs.dir"));
        org.junit.Assert.assertEquals(expectedAuditHdfsDir, clusterConfig.getPropertyValue("ranger-hdfs-audit", "xasecure.audit.destination.hdfs.dir"));
        org.junit.Assert.assertEquals(expectedAuditHdfsDir, clusterConfig.getPropertyValue("ranger-hbase-audit", "xasecure.audit.destination.hdfs.dir"));
        org.junit.Assert.assertEquals(expectedAuditHdfsDir, clusterConfig.getPropertyValue("ranger-hive-audit", "xasecure.audit.destination.hdfs.dir"));
        org.junit.Assert.assertEquals(expectedAuditHdfsDir, clusterConfig.getPropertyValue("ranger-knox-audit", "xasecure.audit.destination.hdfs.dir"));
        org.junit.Assert.assertEquals(expectedAuditHdfsDir, clusterConfig.getPropertyValue("ranger-kafka-audit", "xasecure.audit.destination.hdfs.dir"));
        org.junit.Assert.assertEquals(expectedAuditHdfsDir, clusterConfig.getPropertyValue("ranger-storm-audit", "xasecure.audit.destination.hdfs.dir"));
        org.junit.Assert.assertEquals(expectedAuditHdfsDir, clusterConfig.getPropertyValue("ranger-atlas-audit", "xasecure.audit.destination.hdfs.dir"));
    }

    @org.junit.Test
    public void testRangerEnvExportBlueprintWithHdfsHA() throws java.lang.Exception {
        java.util.List<java.lang.String> configTypesWithRangerHdfsAuditDir = com.google.common.collect.ImmutableList.of("ranger-env", "ranger-yarn-audit", "ranger-hdfs-audit", "ranger-hbase-audit", "ranger-hive-audit", "ranger-knox-audit", "ranger-kafka-audit", "ranger-storm-audit", "ranger-atlas-audit");
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> clusterConfigProperties = new java.util.HashMap<>();
        for (java.lang.String configType : configTypesWithRangerHdfsAuditDir) {
            java.util.Map<java.lang.String, java.lang.String> configProperties = new java.util.HashMap<>();
            configProperties.put("xasecure.audit.destination.hdfs.dir", "hdfs://my_name_service:100");
            clusterConfigProperties.put(configType, configProperties);
        }
        final java.lang.String hdfsSiteConfigType = "hdfs-site";
        java.util.Map<java.lang.String, java.lang.String> hdfsSiteProperties = new java.util.HashMap<>();
        clusterConfigProperties.put(hdfsSiteConfigType, hdfsSiteProperties);
        hdfsSiteProperties.put("dfs.nameservices", "my_name_service");
        hdfsSiteProperties.put("dfs.ha.namenodes.my_name_service", "nn1,nn2");
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> parentProperties = new java.util.HashMap<>();
        org.apache.ambari.server.topology.Configuration parentClusterConfig = new org.apache.ambari.server.topology.Configuration(parentProperties, new java.util.HashMap<>());
        org.apache.ambari.server.topology.Configuration clusterConfig = new org.apache.ambari.server.topology.Configuration(clusterConfigProperties, new java.util.HashMap<>(), parentClusterConfig);
        java.util.Collection<java.lang.String> rangerComponents = new java.util.HashSet<>();
        rangerComponents.add("RANGER_ADMIN");
        rangerComponents.add("RANGER_USERSYNC");
        java.util.Collection<java.lang.String> hdfsComponents = new java.util.HashSet<>();
        hdfsComponents.add("NAMENODE");
        hdfsComponents.add("DATANODE");
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup group1 = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup("group1", rangerComponents, java.util.Collections.singleton("host1"));
        group1.components.addAll(hdfsComponents);
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup group2 = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup("group2", hdfsComponents, java.util.Collections.singleton("host2"));
        java.util.Collection<org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup> hostGroups = com.google.common.collect.Lists.newArrayList(group1, group2);
        org.apache.ambari.server.topology.ClusterTopology topology = createClusterTopology(bp, clusterConfig, hostGroups);
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor configProcessor = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor(topology);
        configProcessor.doUpdateForBlueprintExport(org.apache.ambari.server.controller.internal.BlueprintExportType.FULL);
        java.lang.String expectedAuditHdfsDir = "hdfs://my_name_service:100";
        org.junit.Assert.assertEquals(expectedAuditHdfsDir, clusterConfig.getPropertyValue("ranger-env", "xasecure.audit.destination.hdfs.dir"));
        org.junit.Assert.assertEquals(expectedAuditHdfsDir, clusterConfig.getPropertyValue("ranger-yarn-audit", "xasecure.audit.destination.hdfs.dir"));
        org.junit.Assert.assertEquals(expectedAuditHdfsDir, clusterConfig.getPropertyValue("ranger-hdfs-audit", "xasecure.audit.destination.hdfs.dir"));
        org.junit.Assert.assertEquals(expectedAuditHdfsDir, clusterConfig.getPropertyValue("ranger-hbase-audit", "xasecure.audit.destination.hdfs.dir"));
        org.junit.Assert.assertEquals(expectedAuditHdfsDir, clusterConfig.getPropertyValue("ranger-hive-audit", "xasecure.audit.destination.hdfs.dir"));
        org.junit.Assert.assertEquals(expectedAuditHdfsDir, clusterConfig.getPropertyValue("ranger-knox-audit", "xasecure.audit.destination.hdfs.dir"));
        org.junit.Assert.assertEquals(expectedAuditHdfsDir, clusterConfig.getPropertyValue("ranger-kafka-audit", "xasecure.audit.destination.hdfs.dir"));
        org.junit.Assert.assertEquals(expectedAuditHdfsDir, clusterConfig.getPropertyValue("ranger-storm-audit", "xasecure.audit.destination.hdfs.dir"));
        org.junit.Assert.assertEquals(expectedAuditHdfsDir, clusterConfig.getPropertyValue("ranger-atlas-audit", "xasecure.audit.destination.hdfs.dir"));
    }

    @org.junit.Test
    public void testRangerKmsServerProperties() throws java.lang.Exception {
        final java.lang.String kmsSiteConfigType = "kms-site";
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> properties = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.lang.String> kmsSiteProperties = new java.util.HashMap<>();
        properties.put(kmsSiteConfigType, kmsSiteProperties);
        kmsSiteProperties.put("hadoop.kms.authentication.signer.secret.provider.zookeeper.connection.string", (org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.createHostAddress("%HOSTGROUP::group1%", "2181") + ",") + org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.createHostAddress("%HOSTGROUP::group2%", "2181"));
        kmsSiteProperties.put("hadoop.kms.key.provider.uri", "dbks://http@localhost:9292/kms");
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> parentProperties = new java.util.HashMap<>();
        org.apache.ambari.server.topology.Configuration parentClusterConfig = new org.apache.ambari.server.topology.Configuration(parentProperties, java.util.Collections.emptyMap());
        org.apache.ambari.server.topology.Configuration clusterConfig = new org.apache.ambari.server.topology.Configuration(properties, java.util.Collections.emptyMap(), parentClusterConfig);
        java.util.Collection<java.lang.String> kmsServerComponents = new java.util.HashSet<>();
        kmsServerComponents.add("RANGER_KMS_SERVER");
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup group1 = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup("group1", kmsServerComponents, java.util.Collections.singleton("host1"));
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup group2 = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup("group2", kmsServerComponents, java.util.Collections.singleton("host2"));
        java.util.Collection<org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup> hostGroups = new java.util.HashSet<>();
        hostGroups.add(group1);
        hostGroups.add(group2);
        org.apache.ambari.server.topology.ClusterTopology topology = createClusterTopology(bp, clusterConfig, hostGroups);
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor configProcessor = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor(topology);
        configProcessor.doUpdateForClusterCreate();
        org.junit.Assert.assertEquals("host1:2181,host2:2181", clusterConfig.getPropertyValue(kmsSiteConfigType, "hadoop.kms.authentication.signer.secret.provider.zookeeper.connection.string"));
        org.junit.Assert.assertEquals("dbks://http@localhost:9292/kms", clusterConfig.getPropertyValue(kmsSiteConfigType, "hadoop.kms.key.provider.uri"));
    }

    @org.junit.Test
    public void testRangerKmsServerProperties_default() throws java.lang.Exception {
        final java.lang.String kmsSiteConfigType = "kms-site";
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> properties = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.lang.String> kmsSiteProperties = new java.util.HashMap<>();
        properties.put(kmsSiteConfigType, kmsSiteProperties);
        kmsSiteProperties.put("hadoop.kms.authentication.signer.secret.provider.zookeeper.connection.string", org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.createHostAddress("%HOSTGROUP::group1%", "2181"));
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> parentProperties = new java.util.HashMap<>();
        org.apache.ambari.server.topology.Configuration parentClusterConfig = new org.apache.ambari.server.topology.Configuration(parentProperties, java.util.Collections.emptyMap());
        org.apache.ambari.server.topology.Configuration clusterConfig = new org.apache.ambari.server.topology.Configuration(properties, java.util.Collections.emptyMap(), parentClusterConfig);
        java.util.Collection<java.lang.String> kmsServerComponents = new java.util.HashSet<>();
        kmsServerComponents.add("RANGER_KMS_SERVER");
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup group1 = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup("group1", kmsServerComponents, java.util.Collections.singleton("host1"));
        java.util.Collection<org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup> hostGroups = java.util.Collections.singleton(group1);
        org.apache.ambari.server.topology.ClusterTopology topology = createClusterTopology(bp, clusterConfig, hostGroups);
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor configProcessor = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor(topology);
        configProcessor.doUpdateForClusterCreate();
        org.junit.Assert.assertEquals("host1:2181", clusterConfig.getPropertyValue(kmsSiteConfigType, "hadoop.kms.authentication.signer.secret.provider.zookeeper.connection.string"));
    }

    @org.junit.Test
    public void testHdfsWithRangerKmsServer() throws java.lang.Exception {
        final java.lang.String configType = "hdfs-site";
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> properties = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.lang.String> configProperties = new java.util.HashMap<>();
        properties.put(configType, configProperties);
        configProperties.put("dfs.encryption.key.provider.uri", "kms://http@%HOSTGROUP::group1%;%HOSTGROUP::group2%:9292/kms");
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> parentProperties = new java.util.HashMap<>();
        org.apache.ambari.server.topology.Configuration parentClusterConfig = new org.apache.ambari.server.topology.Configuration(parentProperties, java.util.Collections.emptyMap());
        org.apache.ambari.server.topology.Configuration clusterConfig = new org.apache.ambari.server.topology.Configuration(properties, java.util.Collections.emptyMap(), parentClusterConfig);
        java.util.Collection<java.lang.String> kmsServerComponents = new java.util.HashSet<>();
        kmsServerComponents.add("RANGER_KMS_SERVER");
        java.util.Collection<java.lang.String> hdfsComponents = new java.util.HashSet<>();
        hdfsComponents.add("NAMENODE");
        hdfsComponents.add("DATANODE");
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup group1 = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup("group1", kmsServerComponents, java.util.Collections.singleton("host1"));
        group1.components.add("DATANODE");
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup group2 = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup("group2", hdfsComponents, java.util.Collections.singleton("host2"));
        java.util.Collection<org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup> hostGroups = com.google.common.collect.Lists.newArrayList(group1, group2);
        org.apache.ambari.server.topology.ClusterTopology topology = createClusterTopology(bp, clusterConfig, hostGroups);
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor configProcessor = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor(topology);
        configProcessor.doUpdateForClusterCreate();
        java.lang.String updatedVal = clusterConfig.getPropertyValue(configType, "dfs.encryption.key.provider.uri");
        org.junit.Assert.assertTrue(updatedVal.startsWith("kms://http@"));
        org.junit.Assert.assertTrue(updatedVal.endsWith(":9292/kms"));
        java.lang.String hostsString = updatedVal.substring(11, updatedVal.length() - 9);
        java.util.List<java.lang.String> hostArray = java.util.Arrays.asList(hostsString.split(";"));
        java.util.List<java.lang.String> expected = java.util.Arrays.asList("host1", "host2");
        org.junit.Assert.assertTrue(hostArray.containsAll(expected) && expected.containsAll(hostArray));
    }

    @org.junit.Test
    public void testHdfsWithNoRangerKmsServer() throws java.lang.Exception {
        EasyMock.expect(stack.getCardinality("NAMENODE")).andReturn(new org.apache.ambari.server.topology.Cardinality("1-2")).anyTimes();
        EasyMock.expect(stack.getCardinality("DATANODE")).andReturn(new org.apache.ambari.server.topology.Cardinality("1+")).anyTimes();
        EasyMock.expect(stack.getCardinality("RANGER_KMS_SERVER")).andReturn(new org.apache.ambari.server.topology.Cardinality("1+")).anyTimes();
        final java.lang.String configType = "hdfs-site";
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> properties = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.lang.String> configProperties = new java.util.HashMap<>();
        properties.put(configType, configProperties);
        configProperties.put("dfs.encryption.key.provider.uri", "leave_untouched");
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> parentProperties = new java.util.HashMap<>();
        org.apache.ambari.server.topology.Configuration parentClusterConfig = new org.apache.ambari.server.topology.Configuration(parentProperties, java.util.Collections.emptyMap());
        org.apache.ambari.server.topology.Configuration clusterConfig = new org.apache.ambari.server.topology.Configuration(properties, java.util.Collections.emptyMap(), parentClusterConfig);
        java.util.Collection<java.lang.String> hdfsComponents = new java.util.HashSet<>();
        hdfsComponents.add("NAMENODE");
        hdfsComponents.add("DATANODE");
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup group1 = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup("group1", java.util.Collections.singletonList("DATANODE"), java.util.Collections.singleton("host1"));
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup group2 = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup("group2", hdfsComponents, java.util.Collections.singleton("host2"));
        java.util.Collection<org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup> hostGroups = com.google.common.collect.Lists.newArrayList(group1, group2);
        org.apache.ambari.server.topology.ClusterTopology topology = createClusterTopology(bp, clusterConfig, hostGroups);
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor configProcessor = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor(topology);
        configProcessor.doUpdateForClusterCreate();
        org.junit.Assert.assertEquals("leave_untouched", clusterConfig.getPropertyValue(configType, "dfs.encryption.key.provider.uri"));
    }

    @org.junit.Test
    public void testHdfsWithRangerKmsServer_default() throws java.lang.Exception {
        final java.lang.String configType = "hdfs-site";
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> properties = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.lang.String> configProperties = new java.util.HashMap<>();
        properties.put(configType, configProperties);
        configProperties.put("dfs.encryption.key.provider.uri", "kms://http@localhost:9292/kms");
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> parentProperties = new java.util.HashMap<>();
        org.apache.ambari.server.topology.Configuration parentClusterConfig = new org.apache.ambari.server.topology.Configuration(parentProperties, java.util.Collections.emptyMap());
        org.apache.ambari.server.topology.Configuration clusterConfig = new org.apache.ambari.server.topology.Configuration(properties, java.util.Collections.emptyMap(), parentClusterConfig);
        java.util.Collection<java.lang.String> kmsServerComponents = new java.util.HashSet<>();
        kmsServerComponents.add("RANGER_KMS_SERVER");
        java.util.Collection<java.lang.String> hdfsComponents = new java.util.HashSet<>();
        hdfsComponents.add("NAMENODE");
        hdfsComponents.add("DATANODE");
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup group1 = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup("group1", kmsServerComponents, java.util.Collections.singleton("host1"));
        group1.components.add("DATANODE");
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup group2 = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup("group2", hdfsComponents, java.util.Collections.singleton("host2"));
        java.util.Collection<org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup> hostGroups = com.google.common.collect.Lists.newArrayList(group1, group2);
        org.apache.ambari.server.topology.ClusterTopology topology = createClusterTopology(bp, clusterConfig, hostGroups);
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor configProcessor = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor(topology);
        configProcessor.doUpdateForClusterCreate();
        org.junit.Assert.assertEquals("kms://http@host1:9292/kms", clusterConfig.getPropertyValue(configType, "dfs.encryption.key.provider.uri"));
    }

    @org.junit.Test
    public void testHdfsWithRangerKmsServer__multiple_hosts__localhost() throws java.lang.Exception {
        final java.lang.String configType = "hdfs-site";
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> properties = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.lang.String> configProperties = new java.util.HashMap<>();
        properties.put(configType, configProperties);
        configProperties.put("dfs.encryption.key.provider.uri", "kms://http@localhost:9292/kms");
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> parentProperties = new java.util.HashMap<>();
        org.apache.ambari.server.topology.Configuration parentClusterConfig = new org.apache.ambari.server.topology.Configuration(parentProperties, java.util.Collections.emptyMap());
        org.apache.ambari.server.topology.Configuration clusterConfig = new org.apache.ambari.server.topology.Configuration(properties, java.util.Collections.emptyMap(), parentClusterConfig);
        java.util.Collection<java.lang.String> kmsServerComponents = new java.util.HashSet<>();
        kmsServerComponents.add("RANGER_KMS_SERVER");
        java.util.Collection<java.lang.String> hdfsComponents = new java.util.HashSet<>();
        hdfsComponents.add("NAMENODE");
        hdfsComponents.add("DATANODE");
        java.util.Collection<java.lang.String> hosts = new java.util.HashSet<>();
        hosts.add("host1");
        hosts.add("host2");
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup group1 = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup("group1", kmsServerComponents, hosts);
        group1.components.add("DATANODE");
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup group2 = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup("group2", hdfsComponents, java.util.Collections.singleton("host3"));
        java.util.Collection<org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup> hostGroups = com.google.common.collect.Lists.newArrayList(group1, group2);
        org.apache.ambari.server.topology.ClusterTopology topology = createClusterTopology(bp, clusterConfig, hostGroups);
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor configProcessor = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor(topology);
        configProcessor.doUpdateForClusterCreate();
        java.lang.String updatedVal = clusterConfig.getPropertyValue(configType, "dfs.encryption.key.provider.uri");
        org.junit.Assert.assertTrue(updatedVal.startsWith("kms://http@"));
        org.junit.Assert.assertTrue(updatedVal.endsWith(":9292/kms"));
        java.lang.String hostsString = updatedVal.substring(11, updatedVal.length() - 9);
        java.util.List<java.lang.String> hostArray = java.util.Arrays.asList(hostsString.split(";"));
        java.util.List<java.lang.String> expected = java.util.Arrays.asList("host1", "host2");
        org.junit.Assert.assertTrue(hostArray.containsAll(expected) && expected.containsAll(hostArray));
    }

    @org.junit.Test
    public void testHdfsWithRangerKmsServer__multiple_hosts__hostgroup() throws java.lang.Exception {
        final java.lang.String configType = "hdfs-site";
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> properties = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.lang.String> configProperties = new java.util.HashMap<>();
        properties.put(configType, configProperties);
        configProperties.put("dfs.encryption.key.provider.uri", "kms://http@%HOSTGROUP::group1%:9292/kms");
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> parentProperties = new java.util.HashMap<>();
        org.apache.ambari.server.topology.Configuration parentClusterConfig = new org.apache.ambari.server.topology.Configuration(parentProperties, java.util.Collections.emptyMap());
        org.apache.ambari.server.topology.Configuration clusterConfig = new org.apache.ambari.server.topology.Configuration(properties, java.util.Collections.emptyMap(), parentClusterConfig);
        java.util.Collection<java.lang.String> kmsServerComponents = new java.util.HashSet<>();
        kmsServerComponents.add("RANGER_KMS_SERVER");
        java.util.Collection<java.lang.String> hdfsComponents = new java.util.HashSet<>();
        hdfsComponents.add("NAMENODE");
        hdfsComponents.add("DATANODE");
        java.util.Collection<java.lang.String> hosts = new java.util.HashSet<>();
        hosts.add("host1");
        hosts.add("host2");
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup group1 = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup("group1", kmsServerComponents, hosts);
        group1.components.add("DATANODE");
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup group2 = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup("group2", hdfsComponents, java.util.Collections.singleton("host3"));
        java.util.Collection<org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup> hostGroups = com.google.common.collect.Lists.newArrayList(group1, group2);
        org.apache.ambari.server.topology.ClusterTopology topology = createClusterTopology(bp, clusterConfig, hostGroups);
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor configProcessor = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor(topology);
        configProcessor.doUpdateForClusterCreate();
        java.lang.String updatedVal = clusterConfig.getPropertyValue(configType, "dfs.encryption.key.provider.uri");
        org.junit.Assert.assertTrue(updatedVal.startsWith("kms://http@"));
        org.junit.Assert.assertTrue(updatedVal.endsWith(":9292/kms"));
        java.lang.String hostsString = updatedVal.substring(11, updatedVal.length() - 9);
        java.util.List<java.lang.String> hostArray = java.util.Arrays.asList(hostsString.split(";"));
        java.util.List<java.lang.String> expected = java.util.Arrays.asList("host1", "host2");
        org.junit.Assert.assertTrue(hostArray.containsAll(expected) && expected.containsAll(hostArray));
    }

    @org.junit.Test
    public void testResolutionOfDRPCServerAndNN() throws java.lang.Exception {
        final java.lang.String stormConfigType = "storm-site";
        final java.lang.String mrConfigType = "mapred-site";
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> properties = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.lang.String> stormConfigProperties = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.lang.String> mrConfigProperties = new java.util.HashMap<>();
        properties.put(stormConfigType, stormConfigProperties);
        properties.put(mrConfigType, mrConfigProperties);
        stormConfigProperties.put("drpc.servers", "['%HOSTGROUP::group1%']");
        mrConfigProperties.put("mapreduce.job.hdfs-servers", "['%HOSTGROUP::group2%']");
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> parentProperties = new java.util.HashMap<>();
        org.apache.ambari.server.topology.Configuration parentClusterConfig = new org.apache.ambari.server.topology.Configuration(parentProperties, java.util.Collections.emptyMap());
        org.apache.ambari.server.topology.Configuration clusterConfig = new org.apache.ambari.server.topology.Configuration(properties, java.util.Collections.emptyMap(), parentClusterConfig);
        java.util.Collection<java.lang.String> stormComponents = new java.util.HashSet<>();
        stormComponents.add("NIMBUS");
        stormComponents.add("DRPC_SERVER");
        java.util.Collection<java.lang.String> hdfsComponents = new java.util.HashSet<>();
        hdfsComponents.add("NAMENODE");
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup group1 = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup("group1", stormComponents, java.util.Collections.singleton("host1"));
        group1.components.add("DATANODE");
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup group2 = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup("group2", hdfsComponents, java.util.Collections.singleton("host2"));
        java.util.Collection<org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup> hostGroups = com.google.common.collect.Lists.newArrayList(group1, group2);
        org.apache.ambari.server.topology.ClusterTopology topology = createClusterTopology(bp, clusterConfig, hostGroups);
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor configProcessor = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor(topology);
        configProcessor.doUpdateForClusterCreate();
        org.junit.Assert.assertEquals("['host1']", clusterConfig.getPropertyValue(stormConfigType, "drpc.servers"));
        org.junit.Assert.assertEquals("['host2']", clusterConfig.getPropertyValue(mrConfigType, "mapreduce.job.hdfs-servers"));
    }

    @org.junit.Test
    public void testHadoopWithRangerKmsServer() throws java.lang.Exception {
        final java.lang.String configType = "core-site";
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> properties = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.lang.String> configProperties = new java.util.HashMap<>();
        properties.put(configType, configProperties);
        configProperties.put("hadoop.security.key.provider.path", "kms://http@%HOSTGROUP::group1%;%HOSTGROUP::group2%:9292/kms");
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> parentProperties = new java.util.HashMap<>();
        org.apache.ambari.server.topology.Configuration parentClusterConfig = new org.apache.ambari.server.topology.Configuration(parentProperties, java.util.Collections.emptyMap());
        org.apache.ambari.server.topology.Configuration clusterConfig = new org.apache.ambari.server.topology.Configuration(properties, java.util.Collections.emptyMap(), parentClusterConfig);
        java.util.Collection<java.lang.String> kmsServerComponents = new java.util.HashSet<>();
        kmsServerComponents.add("RANGER_KMS_SERVER");
        java.util.Collection<java.lang.String> hdfsComponents = new java.util.HashSet<>();
        hdfsComponents.add("NAMENODE");
        hdfsComponents.add("DATANODE");
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup group1 = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup("group1", kmsServerComponents, java.util.Collections.singleton("host1"));
        group1.components.add("DATANODE");
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup group2 = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup("group2", hdfsComponents, java.util.Collections.singleton("host2"));
        java.util.Collection<org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup> hostGroups = com.google.common.collect.Lists.newArrayList(group1, group2);
        org.apache.ambari.server.topology.ClusterTopology topology = createClusterTopology(bp, clusterConfig, hostGroups);
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor configProcessor = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor(topology);
        configProcessor.doUpdateForClusterCreate();
        org.junit.Assert.assertEquals("kms://http@host1;host2:9292/kms", clusterConfig.getPropertyValue(configType, "hadoop.security.key.provider.path"));
    }

    @org.junit.Test
    public void testHadoopWithNoRangerKmsServer() throws java.lang.Exception {
        EasyMock.expect(stack.getCardinality("NAMENODE")).andReturn(new org.apache.ambari.server.topology.Cardinality("1-2")).anyTimes();
        EasyMock.expect(stack.getCardinality("DATANODE")).andReturn(new org.apache.ambari.server.topology.Cardinality("1+")).anyTimes();
        EasyMock.expect(stack.getCardinality("RANGER_KMS_SERVER")).andReturn(new org.apache.ambari.server.topology.Cardinality("1+")).anyTimes();
        final java.lang.String configType = "core-site";
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> properties = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.lang.String> configProperties = new java.util.HashMap<>();
        properties.put(configType, configProperties);
        configProperties.put("hadoop.security.key.provider.path", "leave_untouched");
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> parentProperties = new java.util.HashMap<>();
        org.apache.ambari.server.topology.Configuration parentClusterConfig = new org.apache.ambari.server.topology.Configuration(parentProperties, java.util.Collections.emptyMap());
        org.apache.ambari.server.topology.Configuration clusterConfig = new org.apache.ambari.server.topology.Configuration(properties, java.util.Collections.emptyMap(), parentClusterConfig);
        java.util.Collection<java.lang.String> hdfsComponents = new java.util.HashSet<>();
        hdfsComponents.add("NAMENODE");
        hdfsComponents.add("DATANODE");
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup group1 = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup("group1", java.util.Collections.singletonList("DATANODE"), java.util.Collections.singleton("host1"));
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup group2 = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup("group2", hdfsComponents, java.util.Collections.singleton("host2"));
        java.util.Collection<org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup> hostGroups = com.google.common.collect.Lists.newArrayList(group1, group2);
        org.apache.ambari.server.topology.ClusterTopology topology = createClusterTopology(bp, clusterConfig, hostGroups);
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor configProcessor = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor(topology);
        configProcessor.doUpdateForClusterCreate();
        org.junit.Assert.assertEquals("leave_untouched", clusterConfig.getPropertyValue(configType, "hadoop.security.key.provider.path"));
    }

    @org.junit.Test
    public void testHadoopWithRangerKmsServer_default() throws java.lang.Exception {
        final java.lang.String configType = "core-site";
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> properties = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.lang.String> configProperties = new java.util.HashMap<>();
        properties.put(configType, configProperties);
        configProperties.put("hadoop.security.key.provider.path", "kms://http@localhost:9292/kms");
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> parentProperties = new java.util.HashMap<>();
        org.apache.ambari.server.topology.Configuration parentClusterConfig = new org.apache.ambari.server.topology.Configuration(parentProperties, java.util.Collections.emptyMap());
        org.apache.ambari.server.topology.Configuration clusterConfig = new org.apache.ambari.server.topology.Configuration(properties, java.util.Collections.emptyMap(), parentClusterConfig);
        java.util.Collection<java.lang.String> kmsServerComponents = new java.util.HashSet<>();
        kmsServerComponents.add("RANGER_KMS_SERVER");
        java.util.Collection<java.lang.String> hdfsComponents = new java.util.HashSet<>();
        hdfsComponents.add("NAMENODE");
        hdfsComponents.add("DATANODE");
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup group1 = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup("group1", kmsServerComponents, java.util.Collections.singleton("host1"));
        group1.components.add("DATANODE");
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup group2 = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup("group2", hdfsComponents, java.util.Collections.singleton("host2"));
        java.util.Collection<org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup> hostGroups = com.google.common.collect.Lists.newArrayList(group1, group2);
        org.apache.ambari.server.topology.ClusterTopology topology = createClusterTopology(bp, clusterConfig, hostGroups);
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor configProcessor = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor(topology);
        configProcessor.doUpdateForClusterCreate();
        org.junit.Assert.assertEquals("kms://http@host1:9292/kms", clusterConfig.getPropertyValue(configType, "hadoop.security.key.provider.path"));
    }

    @org.junit.Test
    public void testYamlMultiValueWithSingleQuoteFlowStyleFormatSingleValue() throws java.lang.Exception {
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.YamlMultiValuePropertyDecorator yamlMultiValuePropertyDecorator = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.YamlMultiValuePropertyDecorator(null);
        java.lang.String originalValue = "test_value";
        java.lang.String newValue = yamlMultiValuePropertyDecorator.doFormat(originalValue);
        java.lang.String expectedValue = "['test_value']";
        org.junit.Assert.assertEquals(expectedValue, newValue);
    }

    @org.junit.Test
    public void testYamlMultiValueWithPlainFlowStyleFormatSingleValue() throws java.lang.Exception {
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.YamlMultiValuePropertyDecorator yamlMultiValuePropertyDecorator = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.YamlMultiValuePropertyDecorator(null, org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.YamlMultiValuePropertyDecorator.FlowStyle.PLAIN);
        java.lang.String originalValue = "test_value";
        java.lang.String newValue = yamlMultiValuePropertyDecorator.doFormat(originalValue);
        java.lang.String expectedValue = "[test_value]";
        org.junit.Assert.assertEquals(expectedValue, newValue);
    }

    @org.junit.Test
    public void testYamlMultiValueWithSingleQuoteFlowStyleFormatMultiValue() throws java.lang.Exception {
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.YamlMultiValuePropertyDecorator yamlMultiValuePropertyDecorator = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.YamlMultiValuePropertyDecorator(null);
        java.lang.String originalValue = "test_value1,test_value2";
        java.lang.String newValue = yamlMultiValuePropertyDecorator.doFormat(originalValue);
        java.lang.String expectedValue = "['test_value1','test_value2']";
        org.junit.Assert.assertEquals(expectedValue, newValue);
    }

    @org.junit.Test
    public void testYamlMultiValueWithPlainFlowStyleFormatMultiValue() throws java.lang.Exception {
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.YamlMultiValuePropertyDecorator yamlMultiValuePropertyDecorator = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.YamlMultiValuePropertyDecorator(null, org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.YamlMultiValuePropertyDecorator.FlowStyle.PLAIN);
        java.lang.String originalValue = "test_value1,test_value2";
        java.lang.String newValue = yamlMultiValuePropertyDecorator.doFormat(originalValue);
        java.lang.String expectedValue = "[test_value1,test_value2]";
        org.junit.Assert.assertEquals(expectedValue, newValue);
    }

    @org.junit.Test
    public void testYamlMultiValueWithSingleQuoteFlowStyleFormatSingleValueInSquareBrackets() throws java.lang.Exception {
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.YamlMultiValuePropertyDecorator yamlMultiValuePropertyDecorator = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.YamlMultiValuePropertyDecorator(null);
        java.lang.String originalValue = "['test_value']";
        java.lang.String newValue = yamlMultiValuePropertyDecorator.doFormat(originalValue);
        java.lang.String expectedValue = "['test_value']";
        org.junit.Assert.assertEquals(expectedValue, newValue);
    }

    @org.junit.Test
    public void testYamlMultiValueFormatWithPlainFlowStyleSingleValueInSquareBrackets() throws java.lang.Exception {
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.YamlMultiValuePropertyDecorator yamlMultiValuePropertyDecorator = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.YamlMultiValuePropertyDecorator(null, org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.YamlMultiValuePropertyDecorator.FlowStyle.PLAIN);
        java.lang.String originalValue = "[test_value]";
        java.lang.String newValue = yamlMultiValuePropertyDecorator.doFormat(originalValue);
        java.lang.String expectedValue = "[test_value]";
        org.junit.Assert.assertEquals(expectedValue, newValue);
    }

    @org.junit.Test
    public void testYamlMultiValueWithSingleQuoteFlowStyleFormatMultiValueInSquareBrackets() throws java.lang.Exception {
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.YamlMultiValuePropertyDecorator yamlMultiValuePropertyDecorator = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.YamlMultiValuePropertyDecorator(null);
        java.lang.String originalValue = "['test_value1','test_value2']";
        java.lang.String newValue = yamlMultiValuePropertyDecorator.doFormat(originalValue);
        java.lang.String expectedValue = "['test_value1','test_value2']";
        org.junit.Assert.assertEquals(expectedValue, newValue);
    }

    @org.junit.Test
    public void testYamlMultiValueWithPlainFlowStyleFormatMultiValueInSquareBrackets() throws java.lang.Exception {
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.YamlMultiValuePropertyDecorator yamlMultiValuePropertyDecorator = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.YamlMultiValuePropertyDecorator(null, org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.YamlMultiValuePropertyDecorator.FlowStyle.PLAIN);
        java.lang.String originalValue = "[test_value1,test_value2]";
        java.lang.String newValue = yamlMultiValuePropertyDecorator.doFormat(originalValue);
        java.lang.String expectedValue = "[test_value1,test_value2]";
        org.junit.Assert.assertEquals(expectedValue, newValue);
    }

    @org.junit.Test
    public void testMultipleHostTopologyUpdaterWithYamlPropertySingleHostValue() throws java.lang.Exception {
        java.lang.String component = "test_component";
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.MultipleHostTopologyUpdater mhtu = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.MultipleHostTopologyUpdater(component);
        java.lang.String propertyOriginalValue1 = "['%HOSTGROUP::group_1%']";
        java.lang.String propertyOriginalValue2 = "[%HOSTGROUP::group_1%]";
        java.lang.String updatedValue1 = mhtu.resolveHostGroupPlaceholder(propertyOriginalValue1, com.google.common.collect.ImmutableList.of("host1:100"));
        java.lang.String updatedValue2 = mhtu.resolveHostGroupPlaceholder(propertyOriginalValue2, com.google.common.collect.ImmutableList.of("host1:100"));
        org.junit.Assert.assertEquals("host1:100", updatedValue1);
        org.junit.Assert.assertEquals("host1:100", updatedValue2);
    }

    @org.junit.Test
    public void testMultipleHostTopologyUpdaterWithYamlPropertyMultiHostValue() throws java.lang.Exception {
        java.lang.String component = "test_component";
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.MultipleHostTopologyUpdater mhtu = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.MultipleHostTopologyUpdater(component);
        java.lang.String propertyOriginalValue1 = "['%HOSTGROUP::group_1%', '%HOSTGROUP::group_2%']";
        java.lang.String propertyOriginalValue2 = "[%HOSTGROUP::group_1%, %HOSTGROUP::group_2%]";
        java.lang.String updatedValue1 = mhtu.resolveHostGroupPlaceholder(propertyOriginalValue1, com.google.common.collect.ImmutableList.of("host1:100", "host2:200"));
        java.lang.String updatedValue2 = mhtu.resolveHostGroupPlaceholder(propertyOriginalValue2, com.google.common.collect.ImmutableList.of("host1:100", "host2:200"));
        org.junit.Assert.assertEquals("host1:100,host2:200", updatedValue1);
        org.junit.Assert.assertEquals("host1:100,host2:200", updatedValue2);
    }

    @org.junit.Test
    public void testMultipleHostTopologyUpdaterWithSingleHostWithSuffixValue() throws java.lang.Exception {
        java.lang.String component = "test_component";
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.MultipleHostTopologyUpdater mhtu = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.MultipleHostTopologyUpdater(component);
        java.lang.String propertyOriginalValue = "http://%HOSTGROUP::group_1%#";
        java.lang.String updatedValue = mhtu.resolveHostGroupPlaceholder(propertyOriginalValue, com.google.common.collect.ImmutableList.of("host1:100"));
        org.junit.Assert.assertEquals("http://host1:100#", updatedValue);
    }

    @org.junit.Test
    public void testMultipleHostTopologyUpdaterWithMultiHostWithSuffixValue() throws java.lang.Exception {
        java.lang.String component = "test_component";
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.MultipleHostTopologyUpdater mhtu = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.MultipleHostTopologyUpdater(component);
        java.lang.String propertyOriginalValue = "http://%HOSTGROUP::group_1,HOSTGROUP::group_2%/resource";
        java.lang.String updatedValue = mhtu.resolveHostGroupPlaceholder(propertyOriginalValue, com.google.common.collect.ImmutableList.of("host1:100", "host2:200"));
        org.junit.Assert.assertEquals("http://host1:100,host2:200/resource", updatedValue);
    }

    @org.junit.Test
    public void testMultipleHostTopologyUpdaterWithMultiHostValue() throws java.lang.Exception {
        java.lang.String component = "test_component";
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.MultipleHostTopologyUpdater mhtu = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.MultipleHostTopologyUpdater(component);
        java.lang.String propertyOriginalValue = "%HOSTGROUP::group_1%:11,%HOSTGROUP::group_2%:11";
        java.lang.String updatedValue = mhtu.resolveHostGroupPlaceholder(propertyOriginalValue, com.google.common.collect.ImmutableList.of("host1:100", "host2:200"));
        org.junit.Assert.assertEquals("host1:100,host2:200", updatedValue);
    }

    @org.junit.Test
    public void testHawqConfigClusterUpdate() throws java.lang.Exception {
        final java.lang.String expectedHostNameHawqMaster = "c6401.apache.ambari.org";
        final java.lang.String expectedHostNameHawqStandby = "c6402.apache.ambari.org";
        final java.lang.String expectedHostNameNamenode = "c6403.apache.ambari.org";
        final java.lang.String expectedPortNamenode = "8020";
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> properties = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.lang.String> hawqSite = new java.util.HashMap<>();
        properties.put("hawq-site", hawqSite);
        hawqSite.put("hawq_master_address_host", "localhost");
        hawqSite.put("hawq_standby_address_host", "localhost");
        hawqSite.put("hawq_dfs_url", org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.createHostAddress("localhost", expectedPortNamenode) + "/hawq_data");
        org.apache.ambari.server.topology.Configuration clusterConfig = new org.apache.ambari.server.topology.Configuration(properties, java.util.Collections.emptyMap());
        java.util.Collection<java.lang.String> hgComponents = new java.util.HashSet<>();
        hgComponents.add("NAMENODE");
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup group1 = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup("group1", hgComponents, java.util.Collections.singleton(expectedHostNameNamenode));
        java.util.Collection<java.lang.String> hgComponents2 = new java.util.HashSet<>();
        hgComponents2.add("HAWQMASTER");
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup group2 = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup("group2", hgComponents2, java.util.Collections.singleton(expectedHostNameHawqMaster));
        java.util.Collection<java.lang.String> hgComponents3 = new java.util.HashSet<>();
        hgComponents3.add("HAWQSTANDBY");
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup group3 = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup("group3", hgComponents3, java.util.Collections.singleton(expectedHostNameHawqStandby));
        java.util.Collection<org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup> hostGroups = new java.util.HashSet<>();
        hostGroups.add(group1);
        hostGroups.add(group2);
        hostGroups.add(group3);
        org.apache.ambari.server.topology.ClusterTopology topology = createClusterTopology(bp, clusterConfig, hostGroups);
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor updater = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor(topology);
        updater.doUpdateForClusterCreate();
        org.junit.Assert.assertEquals(expectedHostNameHawqMaster, hawqSite.get("hawq_master_address_host"));
        org.junit.Assert.assertEquals(expectedHostNameHawqStandby, hawqSite.get("hawq_standby_address_host"));
        org.junit.Assert.assertEquals(org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.createHostAddress(expectedHostNameNamenode, expectedPortNamenode) + "/hawq_data", hawqSite.get("hawq_dfs_url"));
    }

    @org.junit.Test
    public void testHawqNonHaConfigClusterUpdate() throws java.lang.Exception {
        final java.lang.String expectedHostNameHawqMaster = "c6401.apache.ambari.org";
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> properties = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.lang.String> hawqSite = new java.util.HashMap<>();
        properties.put("hawq-site", hawqSite);
        hawqSite.put("hawq_master_address_host", "localhost");
        hawqSite.put("hawq_standby_address_host", "localhost");
        org.apache.ambari.server.topology.Configuration clusterConfig = new org.apache.ambari.server.topology.Configuration(properties, java.util.Collections.emptyMap());
        java.util.Collection<java.lang.String> hgComponents1 = new java.util.HashSet<>();
        hgComponents1.add("HAWQMASTER");
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup group1 = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup("group1", hgComponents1, java.util.Collections.singleton(expectedHostNameHawqMaster));
        java.util.Collection<org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup> hostGroups = new java.util.HashSet<>();
        hostGroups.add(group1);
        org.apache.ambari.server.topology.ClusterTopology topology = createClusterTopology(bp, clusterConfig, hostGroups);
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor updater = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor(topology);
        updater.doUpdateForClusterCreate();
        org.junit.Assert.assertEquals(expectedHostNameHawqMaster, hawqSite.get("hawq_master_address_host"));
        org.junit.Assert.assertFalse("hawq_standby_address_host should have been filtered out of this non-HAWQ HA configuration", hawqSite.containsKey("hawq_standby_address_host"));
    }

    @org.junit.Test
    public void testDoUpdateForBlueprintExport_NonTopologyProperty__AtlasClusterName() throws java.lang.Exception {
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> properties = new java.util.HashMap<>();
        org.apache.ambari.server.topology.Configuration clusterConfig = new org.apache.ambari.server.topology.Configuration(properties, java.util.Collections.emptyMap());
        java.util.Collection<java.lang.String> hgComponents = new java.util.HashSet<>();
        hgComponents.add("ATLAS_SERVER");
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup group1 = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup("group1", hgComponents, java.util.Collections.singleton("testhost"));
        java.util.Collection<org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup> hostGroups = new java.util.HashSet<>();
        hostGroups.add(group1);
        org.apache.ambari.server.topology.ClusterTopology topology = createClusterTopology(bp, clusterConfig, hostGroups);
        java.lang.Long clusterId = topology.getClusterId();
        java.util.Map<java.lang.String, java.lang.String> typeProps = new java.util.HashMap<>();
        typeProps.put("atlas.cluster.name", java.lang.String.valueOf(clusterId));
        properties.put("hive-site", typeProps);
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor configProcessor = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor(topology);
        configProcessor.doUpdateForBlueprintExport(org.apache.ambari.server.controller.internal.BlueprintExportType.FULL);
        java.lang.String updatedVal = properties.get("hive-site").get("atlas.cluster.name");
        org.junit.Assert.assertEquals("primary", updatedVal);
    }

    @org.junit.Test
    public void testDoUpdateForBlueprintExport_NonTopologyProperty() throws java.lang.Exception {
        java.lang.String someString = "String.To.Represent.A.String.Value";
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> properties = new java.util.HashMap<>();
        org.apache.ambari.server.topology.Configuration clusterConfig = new org.apache.ambari.server.topology.Configuration(properties, java.util.Collections.emptyMap());
        java.util.Collection<java.lang.String> hgComponents = new java.util.HashSet<>();
        hgComponents.add("ATLAS_SERVER");
        hgComponents.add("HIVE_SERVER");
        hgComponents.add("KAFKA_BROKER");
        hgComponents.add("NIMBUS");
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup group1 = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup("group1", hgComponents, java.util.Collections.singleton("testhost"));
        java.util.Collection<org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup> hostGroups = new java.util.HashSet<>();
        hostGroups.add(group1);
        org.apache.ambari.server.topology.ClusterTopology topology = createClusterTopology(bp, clusterConfig, hostGroups);
        java.lang.Long clusterId = topology.getClusterId();
        java.util.Map<java.lang.String, java.lang.String> hiveSiteProps = new java.util.HashMap<>();
        hiveSiteProps.put("hive.exec.post.hooks", someString);
        properties.put("hive-site", hiveSiteProps);
        java.util.Map<java.lang.String, java.lang.String> kafkaBrokerProps = new java.util.HashMap<>();
        kafkaBrokerProps.put("kafka.metrics.reporters", someString);
        properties.put("kafka-broker", kafkaBrokerProps);
        java.util.Map<java.lang.String, java.lang.String> stormSiteProps = new java.util.HashMap<>();
        stormSiteProps.put("metrics.reporter.register", someString);
        properties.put("storm-site", stormSiteProps);
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor configProcessor = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor(topology);
        configProcessor.doUpdateForBlueprintExport(org.apache.ambari.server.controller.internal.BlueprintExportType.FULL);
        java.lang.String hiveExecPostHooks = properties.get("hive-site").get("hive.exec.post.hooks");
        java.lang.String kafkaMetricsReporters = properties.get("kafka-broker").get("kafka.metrics.reporters");
        java.lang.String metricsReporterRegister = properties.get("storm-site").get("metrics.reporter.register");
        org.junit.Assert.assertEquals(someString, hiveExecPostHooks);
        org.junit.Assert.assertEquals(someString, kafkaMetricsReporters);
        org.junit.Assert.assertEquals(someString, metricsReporterRegister);
    }

    @org.junit.Test
    public void druidProperties() throws java.lang.Exception {
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> properties = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.lang.String> druidCommon = new java.util.HashMap<>();
        java.lang.String connectUriKey = "druid.metadata.storage.connector.connectURI";
        java.lang.String metastoreHostnameKey = "metastore_hostname";
        java.lang.String connectUriTemplate = "jdbc:mysql://%s:3306/druid?createDatabaseIfNotExist=true";
        druidCommon.put(connectUriKey, java.lang.String.format(connectUriTemplate, "%HOSTGROUP::group1%"));
        druidCommon.put(metastoreHostnameKey, "%HOSTGROUP::group1%");
        properties.put("druid-common", druidCommon);
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> parentProperties = new java.util.HashMap<>();
        org.apache.ambari.server.topology.Configuration parentClusterConfig = new org.apache.ambari.server.topology.Configuration(parentProperties, java.util.Collections.emptyMap());
        org.apache.ambari.server.topology.Configuration clusterConfig = new org.apache.ambari.server.topology.Configuration(properties, java.util.Collections.emptyMap(), parentClusterConfig);
        java.util.Collection<java.lang.String> hgComponents1 = com.google.common.collect.Sets.newHashSet("DRUID_COORDINATOR");
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup group1 = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup("group1", hgComponents1, java.util.Collections.singleton("host1"));
        java.util.Collection<java.lang.String> hgComponents2 = com.google.common.collect.Sets.newHashSet("DRUID_BROKER", "DRUID_OVERLORD", "DRUID_ROUTER");
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup group2 = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup("group2", hgComponents2, java.util.Collections.singleton("host2"));
        java.util.Collection<org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup> hostGroups = java.util.Arrays.asList(group1, group2);
        org.apache.ambari.server.topology.ClusterTopology topology = createClusterTopology(bp, clusterConfig, hostGroups);
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor configProcessor = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor(topology);
        configProcessor.doUpdateForClusterCreate();
        org.junit.Assert.assertEquals(java.lang.String.format(connectUriTemplate, "host1"), clusterConfig.getPropertyValue("druid-common", connectUriKey));
        org.junit.Assert.assertEquals("host1", clusterConfig.getPropertyValue("druid-common", metastoreHostnameKey));
    }

    @org.junit.Test
    public void testAmsPropertiesDefault() throws java.lang.Exception {
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> properties = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.lang.String> amsSite = new java.util.HashMap<>();
        amsSite.put("timeline.metrics.service.webapp.address", "localhost:6188");
        properties.put("ams-site", amsSite);
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> parentProperties = new java.util.HashMap<>();
        org.apache.ambari.server.topology.Configuration parentClusterConfig = new org.apache.ambari.server.topology.Configuration(parentProperties, java.util.Collections.emptyMap());
        org.apache.ambari.server.topology.Configuration clusterConfig = new org.apache.ambari.server.topology.Configuration(properties, java.util.Collections.emptyMap(), parentClusterConfig);
        java.util.Collection<java.lang.String> hgComponents1 = new java.util.HashSet<>();
        hgComponents1.add("METRICS_COLLECTOR");
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup group1 = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup("group1", hgComponents1, java.util.Collections.singleton("host1"));
        java.util.Collection<org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup> hostGroups = java.util.Collections.singletonList(group1);
        org.apache.ambari.server.topology.ClusterTopology topology = createClusterTopology(bp, clusterConfig, hostGroups);
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor configProcessor = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor(topology);
        configProcessor.doUpdateForClusterCreate();
        org.junit.Assert.assertEquals("0.0.0.0:6188", clusterConfig.getPropertyValue("ams-site", "timeline.metrics.service.webapp.address"));
    }

    @org.junit.Test
    public void testAmsPropertiesSpecialAddress() throws java.lang.Exception {
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> properties = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.lang.String> amsSite = new java.util.HashMap<>();
        amsSite.put("timeline.metrics.service.webapp.address", "0.0.0.0:6188");
        properties.put("ams-site", amsSite);
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> parentProperties = new java.util.HashMap<>();
        org.apache.ambari.server.topology.Configuration parentClusterConfig = new org.apache.ambari.server.topology.Configuration(parentProperties, java.util.Collections.emptyMap());
        org.apache.ambari.server.topology.Configuration clusterConfig = new org.apache.ambari.server.topology.Configuration(properties, java.util.Collections.emptyMap(), parentClusterConfig);
        java.util.Collection<java.lang.String> hgComponents1 = new java.util.HashSet<>();
        hgComponents1.add("METRICS_COLLECTOR");
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup group1 = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup("group1", hgComponents1, java.util.Collections.singleton("host1"));
        java.util.Collection<org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup> hostGroups = java.util.Collections.singletonList(group1);
        org.apache.ambari.server.topology.ClusterTopology topology = createClusterTopology(bp, clusterConfig, hostGroups);
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor configProcessor = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor(topology);
        configProcessor.doUpdateForClusterCreate();
        org.junit.Assert.assertEquals("0.0.0.0:6188", clusterConfig.getPropertyValue("ams-site", "timeline.metrics.service.webapp.address"));
    }

    @org.junit.Test
    public void testAmsPropertiesSpecialAddressMultipleCollectors() throws java.lang.Exception {
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> properties = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.lang.String> amsSite = new java.util.HashMap<>();
        amsSite.put("timeline.metrics.service.webapp.address", "0.0.0.0:6188");
        properties.put("ams-site", amsSite);
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> parentProperties = new java.util.HashMap<>();
        org.apache.ambari.server.topology.Configuration parentClusterConfig = new org.apache.ambari.server.topology.Configuration(parentProperties, java.util.Collections.emptyMap());
        org.apache.ambari.server.topology.Configuration clusterConfig = new org.apache.ambari.server.topology.Configuration(properties, java.util.Collections.emptyMap(), parentClusterConfig);
        java.util.Collection<java.lang.String> hgComponents1 = new java.util.HashSet<>();
        java.util.Collection<java.lang.String> hgComponents2 = new java.util.HashSet<>();
        hgComponents1.add("METRICS_COLLECTOR");
        hgComponents2.add("METRICS_COLLECTOR");
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup group1 = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup("group1", hgComponents1, java.util.Collections.singleton("host1"));
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup group2 = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup("group2", hgComponents1, java.util.Collections.singleton("host2"));
        java.util.Collection<org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup> hostGroups = new java.util.LinkedList<>();
        hostGroups.add(group1);
        hostGroups.add(group2);
        org.apache.ambari.server.topology.ClusterTopology topology = createClusterTopology(bp, clusterConfig, hostGroups);
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor configProcessor = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor(topology);
        configProcessor.doUpdateForClusterCreate();
        org.junit.Assert.assertEquals("0.0.0.0:6188", clusterConfig.getPropertyValue("ams-site", "timeline.metrics.service.webapp.address"));
    }

    @org.junit.Test
    public void testStackPasswordPropertyFilter() throws java.lang.Exception {
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> properties = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.lang.String> rangerAdminSiteProps = new java.util.HashMap<>();
        rangerAdminSiteProps.put("ranger.service.https.attrib.keystore.pass", "SECRET:admin-prp:1:ranger.service.pass");
        properties.put("ranger-admin-site", rangerAdminSiteProps);
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> parentProperties = new java.util.HashMap<>();
        org.apache.ambari.server.topology.Configuration parentClusterConfig = new org.apache.ambari.server.topology.Configuration(parentProperties, java.util.Collections.emptyMap());
        org.apache.ambari.server.topology.Configuration clusterConfig = new org.apache.ambari.server.topology.Configuration(properties, java.util.Collections.emptyMap(), parentClusterConfig);
        java.util.Collection<java.lang.String> hgComponents = new java.util.HashSet<>();
        hgComponents.add("NAMENODE");
        hgComponents.add("SECONDARY_NAMENODE");
        hgComponents.add("RESOURCEMANAGER");
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup group1 = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup("group1", hgComponents, java.util.Collections.singleton("testhost"));
        java.util.Collection<java.lang.String> hgComponents2 = new java.util.HashSet<>();
        hgComponents2.add("DATANODE");
        hgComponents2.add("HDFS_CLIENT");
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup group2 = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup("group2", hgComponents2, java.util.Collections.singleton("testhost2"));
        java.util.Collection<org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup> hostGroups = new java.util.HashSet<>();
        hostGroups.add(group1);
        hostGroups.add(group2);
        EasyMock.expect(stack.isPasswordProperty(((java.lang.String) (EasyMock.anyObject())), ((java.lang.String) (EasyMock.anyObject())), ((java.lang.String) (EasyMock.anyObject())))).andReturn(true).once();
        org.apache.ambari.server.topology.ClusterTopology topology = createClusterTopology(bp, clusterConfig, hostGroups);
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor configProcessor = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor(topology);
        configProcessor.doUpdateForBlueprintExport(org.apache.ambari.server.controller.internal.BlueprintExportType.FULL);
        org.junit.Assert.assertFalse(properties.get("ranger-admin-site").containsKey("ranger.service.https.attrib.keystore.pass"));
    }

    private java.util.Map<java.lang.String, org.apache.ambari.server.topology.AdvisedConfiguration> createAdvisedConfigMap() {
        java.util.Map<java.lang.String, org.apache.ambari.server.topology.AdvisedConfiguration> advMap = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.lang.String> confProp = new java.util.HashMap<>();
        confProp.put("fs.stackDefault.key1", "stackDefaultUpgraded");
        confProp.put("fs.notStackDefault", "notStackDefault");
        java.util.Map<java.lang.String, org.apache.ambari.server.state.ValueAttributesInfo> valueAttributesInfoMap = new java.util.HashMap<>();
        org.apache.ambari.server.state.ValueAttributesInfo vaInfo1 = new org.apache.ambari.server.state.ValueAttributesInfo();
        vaInfo1.setDelete("true");
        org.apache.ambari.server.state.ValueAttributesInfo vaInfo2 = new org.apache.ambari.server.state.ValueAttributesInfo();
        vaInfo2.setMaximum("150");
        org.apache.ambari.server.state.ValueAttributesInfo vaInfo3 = new org.apache.ambari.server.state.ValueAttributesInfo();
        vaInfo3.setMinimum("100");
        valueAttributesInfoMap.put("fs.stackDefault.key2", vaInfo1);
        valueAttributesInfoMap.put("fs.notStackDefault", vaInfo2);
        valueAttributesInfoMap.put("fs.stackDefault.key3", vaInfo3);
        advMap.put("core-site", new org.apache.ambari.server.topology.AdvisedConfiguration(confProp, valueAttributesInfoMap));
        java.util.Map<java.lang.String, java.lang.String> dummyConfProp = new java.util.HashMap<>();
        dummyConfProp.put("dummy.prop", "dummyValue");
        advMap.put("dummy-site", new org.apache.ambari.server.topology.AdvisedConfiguration(dummyConfProp, new java.util.HashMap<>()));
        java.util.Map<java.lang.String, org.apache.ambari.server.state.ValueAttributesInfo> dummy2attrMap = new java.util.HashMap<>();
        org.apache.ambari.server.state.ValueAttributesInfo dummy2valInfo = new org.apache.ambari.server.state.ValueAttributesInfo();
        dummy2valInfo.setDelete("true");
        dummy2attrMap.put("dummy2.property", dummy2valInfo);
        advMap.put("dummy2-site", new org.apache.ambari.server.topology.AdvisedConfiguration(new java.util.HashMap<>(), dummy2attrMap));
        return advMap;
    }

    @org.junit.Test
    public void testValuesTrimming() throws java.lang.Exception {
        EasyMock.reset(stack);
        EasyMock.expect(stack.getName()).andReturn(STACK_NAME).anyTimes();
        EasyMock.expect(stack.getVersion()).andReturn(STACK_VERSION).anyTimes();
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> properties = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.lang.String> hdfsSite = new java.util.HashMap<>();
        hdfsSite.put("test.spaces", " spaces at    the end should be deleted      ");
        hdfsSite.put("test.directories", "  /all/spaces , should/be  , deleted  ");
        hdfsSite.put("test.password", "  stays,   same    ");
        hdfsSite.put("test.single.space", " ");
        hdfsSite.put("test.host", " https://just.trims ");
        properties.put("hdfs-site", hdfsSite);
        java.util.Map<java.lang.String, org.apache.ambari.server.controller.internal.Stack.ConfigProperty> propertyConfigs = new java.util.HashMap<>();
        org.apache.ambari.server.state.ValueAttributesInfo valueAttributesInfoDirs = new org.apache.ambari.server.state.ValueAttributesInfo();
        valueAttributesInfoDirs.setType("directories");
        org.apache.ambari.server.state.ValueAttributesInfo valueAttributesInfoHost = new org.apache.ambari.server.state.ValueAttributesInfo();
        valueAttributesInfoHost.setType("host");
        propertyConfigs.put("test.directories", new org.apache.ambari.server.controller.internal.Stack.ConfigProperty(new org.apache.ambari.server.controller.StackConfigurationResponse(null, null, null, null, "hdfs-site", null, null, null, valueAttributesInfoDirs, null)));
        propertyConfigs.put("test.password", new org.apache.ambari.server.controller.internal.Stack.ConfigProperty(new org.apache.ambari.server.controller.StackConfigurationResponse(null, null, null, null, "hdfs-site", null, java.util.Collections.singleton(org.apache.ambari.server.state.PropertyInfo.PropertyType.PASSWORD), null, null, null)));
        propertyConfigs.put("test.host", new org.apache.ambari.server.controller.internal.Stack.ConfigProperty(new org.apache.ambari.server.controller.StackConfigurationResponse(null, null, null, null, "hdfs-site", null, null, null, valueAttributesInfoHost, null)));
        EasyMock.expect(stack.getServiceForConfigType("hdfs-site")).andReturn("HDFS").anyTimes();
        EasyMock.expect(stack.getConfigurationPropertiesWithMetadata("HDFS", "hdfs-site")).andReturn(propertyConfigs).anyTimes();
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> parentProperties = new java.util.HashMap<>();
        org.apache.ambari.server.topology.Configuration parentClusterConfig = new org.apache.ambari.server.topology.Configuration(parentProperties, java.util.Collections.emptyMap());
        org.apache.ambari.server.topology.Configuration clusterConfig = new org.apache.ambari.server.topology.Configuration(properties, java.util.Collections.emptyMap(), parentClusterConfig);
        java.util.Collection<java.lang.String> hgComponents1 = new java.util.HashSet<>();
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup group1 = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup("group1", hgComponents1, java.util.Collections.singleton("host1"));
        java.util.Collection<org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup> hostGroups = java.util.Collections.singletonList(group1);
        org.apache.ambari.server.topology.ClusterTopology topology = createClusterTopology(bp, clusterConfig, hostGroups);
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor configProcessor = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor(topology);
        configProcessor.doUpdateForClusterCreate();
        org.junit.Assert.assertEquals(" spaces at    the end should be deleted", clusterConfig.getPropertyValue("hdfs-site", "test.spaces"));
        org.junit.Assert.assertEquals("/all/spaces,should/be,deleted", clusterConfig.getPropertyValue("hdfs-site", "test.directories"));
        org.junit.Assert.assertEquals("  stays,   same    ", clusterConfig.getPropertyValue("hdfs-site", "test.password"));
        org.junit.Assert.assertEquals(" https://just.trims ".trim(), clusterConfig.getPropertyValue("hdfs-site", "test.host"));
        org.junit.Assert.assertEquals(" ", clusterConfig.getPropertyValue("hdfs-site", "test.single.space"));
    }

    @org.junit.Test
    public void testDoUpdateForClusterWithNameNodeFederationEnabledTagsyncEnabledDefaultRepoName() throws java.lang.Exception {
        final java.lang.String expectedNameService = "mynameservice";
        final java.lang.String expectedNameServiceTwo = "mynameservicetwo";
        final java.lang.String expectedHostName = "c6401.apache.ambari.org";
        final java.lang.String expectedHostNameTwo = "c6402.apache.ambari.org";
        final java.lang.String expectedHostNameThree = "c6403.apache.ambari.org";
        final java.lang.String expectedHostNameFour = "c6404.apache.ambari.org";
        final java.lang.String expectedPortNum = "808080";
        final java.lang.String expectedNodeOne = "nn1";
        final java.lang.String expectedNodeTwo = "nn2";
        final java.lang.String expectedNodeThree = "nn3";
        final java.lang.String expectedNodeFour = "nn4";
        final java.lang.String expectedHostGroupName = "host_group_1";
        final java.lang.String expectedHostGroupNameTwo = "host_group_2";
        final java.lang.String expectedHostGroupNameThree = "host-group-3";
        final java.lang.String expectedHostGroupNameFour = "host-group-4";
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> properties = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.lang.String> hdfsSiteProperties = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.lang.String> hbaseSiteProperties = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.lang.String> hadoopEnvProperties = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.lang.String> coreSiteProperties = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.lang.String> accumuloSiteProperties = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.lang.String> rangerHDFSPluginProperties = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.lang.String> rangerHDFSSecurityProperties = new java.util.HashMap<>();
        properties.put("hdfs-site", hdfsSiteProperties);
        properties.put("hadoop-env", hadoopEnvProperties);
        properties.put("core-site", coreSiteProperties);
        properties.put("hbase-site", hbaseSiteProperties);
        properties.put("accumulo-site", accumuloSiteProperties);
        properties.put("ranger-hdfs-plugin-properties", rangerHDFSPluginProperties);
        properties.put("ranger-hdfs-security", rangerHDFSSecurityProperties);
        hdfsSiteProperties.put("dfs.nameservices", (expectedNameService + ",") + expectedNameServiceTwo);
        hdfsSiteProperties.put("dfs.ha.namenodes.mynameservice", (expectedNodeOne + ", ") + expectedNodeTwo);
        hdfsSiteProperties.put("dfs.ha.namenodes." + expectedNameServiceTwo, (expectedNodeThree + ",") + expectedNodeFour);
        hdfsSiteProperties.put(("dfs.namenode.shared.edits.dir" + ".") + expectedNameService, ((("qjournal://" + org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.createExportedAddress(expectedPortNum, expectedHostGroupName)) + ";") + org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.createExportedAddress(expectedPortNum, expectedHostGroupNameTwo)) + "/ns1");
        hdfsSiteProperties.put(("dfs.namenode.shared.edits.dir" + ".") + expectedNameServiceTwo, ((("qjournal://" + org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.createExportedAddress(expectedPortNum, expectedHostGroupName)) + ";") + org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.createExportedAddress(expectedPortNum, expectedHostGroupNameTwo)) + "/ns2");
        hdfsSiteProperties.put((("dfs.namenode.https-address." + expectedNameService) + ".") + expectedNodeOne, org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.createExportedAddress(expectedPortNum, expectedHostGroupName));
        hdfsSiteProperties.put((("dfs.namenode.https-address." + expectedNameService) + ".") + expectedNodeTwo, org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.createExportedAddress(expectedPortNum, expectedHostGroupNameTwo));
        hdfsSiteProperties.put((("dfs.namenode.http-address." + expectedNameService) + ".") + expectedNodeOne, org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.createExportedAddress(expectedPortNum, expectedHostGroupName));
        hdfsSiteProperties.put((("dfs.namenode.http-address." + expectedNameService) + ".") + expectedNodeTwo, org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.createExportedAddress(expectedPortNum, expectedHostGroupNameTwo));
        hdfsSiteProperties.put((("dfs.namenode.rpc-address." + expectedNameService) + ".") + expectedNodeOne, org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.createExportedAddress(expectedPortNum, expectedHostGroupName));
        hdfsSiteProperties.put((("dfs.namenode.rpc-address." + expectedNameService) + ".") + expectedNodeTwo, org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.createExportedAddress(expectedPortNum, expectedHostGroupNameTwo));
        hdfsSiteProperties.put((("dfs.namenode.servicerpc-address." + expectedNameService) + ".") + expectedNodeOne, org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.createExportedAddress(expectedPortNum, expectedHostGroupName));
        hdfsSiteProperties.put((("dfs.namenode.servicerpc-address." + expectedNameService) + ".") + expectedNodeTwo, org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.createExportedAddress(expectedPortNum, expectedHostGroupNameTwo));
        hdfsSiteProperties.put((("dfs.namenode.https-address." + expectedNameServiceTwo) + ".") + expectedNodeThree, org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.createExportedAddress(expectedPortNum, expectedHostGroupNameThree));
        hdfsSiteProperties.put((("dfs.namenode.https-address." + expectedNameServiceTwo) + ".") + expectedNodeFour, org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.createExportedAddress(expectedPortNum, expectedHostGroupNameFour));
        hdfsSiteProperties.put((("dfs.namenode.http-address." + expectedNameServiceTwo) + ".") + expectedNodeThree, org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.createExportedAddress(expectedPortNum, expectedHostGroupNameThree));
        hdfsSiteProperties.put((("dfs.namenode.http-address." + expectedNameServiceTwo) + ".") + expectedNodeFour, org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.createExportedAddress(expectedPortNum, expectedHostGroupNameFour));
        hdfsSiteProperties.put((("dfs.namenode.rpc-address." + expectedNameServiceTwo) + ".") + expectedNodeThree, org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.createExportedAddress(expectedPortNum, expectedHostGroupNameThree));
        hdfsSiteProperties.put((("dfs.namenode.rpc-address." + expectedNameServiceTwo) + ".") + expectedNodeFour, org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.createExportedAddress(expectedPortNum, expectedHostGroupNameFour));
        hdfsSiteProperties.put((("dfs.namenode.servicerpc-address." + expectedNameServiceTwo) + ".") + expectedNodeThree, org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.createExportedAddress(expectedPortNum, expectedHostGroupNameThree));
        hdfsSiteProperties.put((("dfs.namenode.servicerpc-address." + expectedNameServiceTwo) + ".") + expectedNodeFour, org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.createExportedAddress(expectedPortNum, expectedHostGroupNameFour));
        hdfsSiteProperties.put("dfs.secondary.http.address", "localhost:8080");
        hdfsSiteProperties.put("dfs.namenode.secondary.http-address", "localhost:8080");
        hdfsSiteProperties.put("dfs.namenode.http-address", "localhost:8080");
        hdfsSiteProperties.put("dfs.namenode.https-address", "localhost:8081");
        hdfsSiteProperties.put("dfs.namenode.rpc-address", "localhost:8082");
        coreSiteProperties.put("fs.defaultFS", "hdfs://" + expectedNameService);
        hbaseSiteProperties.put("hbase.rootdir", ("hdfs://" + expectedNameService) + "/hbase/test/root/dir");
        accumuloSiteProperties.put("instance.volumes", ("hdfs://" + expectedNameService) + "/accumulo/test/instance/volumes");
        rangerHDFSPluginProperties.put("hadoop.rpc.protection", "authentication");
        rangerHDFSPluginProperties.put("REPOSITORY_CONFIG_USERNAME", "hadoop");
        rangerHDFSPluginProperties.put("REPOSITORY_CONFIG_PASSWORD", "hadoop");
        rangerHDFSPluginProperties.put("ranger-hdfs-plugin-enabled", "Yes");
        rangerHDFSSecurityProperties.put("ranger.plugin.hdfs.service.name", "{{repo_name}}");
        org.apache.ambari.server.topology.Configuration clusterConfig = new org.apache.ambari.server.topology.Configuration(properties, java.util.Collections.emptyMap());
        java.util.Collection<java.lang.String> hgComponents = new java.util.HashSet<>();
        hgComponents.add("NAMENODE");
        hgComponents.add("RANGER_ADMIN");
        hgComponents.add("RANGER_USERSYNC");
        hgComponents.add("RANGER_TAGSYNC");
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup group1 = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup(expectedHostGroupName, hgComponents, java.util.Collections.singleton(expectedHostName));
        java.util.Collection<java.lang.String> hgComponents2 = new java.util.HashSet<>();
        hgComponents2.add("NAMENODE");
        hgComponents2.add("ATLAS_SERVER");
        hgComponents2.add("ATLAS_CLIENT");
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup group2 = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup(expectedHostGroupNameTwo, hgComponents2, java.util.Collections.singleton(expectedHostNameTwo));
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup group3 = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup(expectedHostGroupNameThree, java.util.Collections.singleton("NAMENODE"), java.util.Collections.singleton(expectedHostNameThree));
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup group4 = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup(expectedHostGroupNameFour, java.util.Collections.singleton("NAMENODE"), java.util.Collections.singleton(expectedHostNameFour));
        java.util.Collection<org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup> hostGroups = new java.util.ArrayList<>();
        hostGroups.add(group1);
        hostGroups.add(group2);
        hostGroups.add(group3);
        hostGroups.add(group4);
        EasyMock.expect(stack.getCardinality("NAMENODE")).andReturn(new org.apache.ambari.server.topology.Cardinality("1-2")).anyTimes();
        EasyMock.expect(stack.getCardinality("SECONDARY_NAMENODE")).andReturn(new org.apache.ambari.server.topology.Cardinality("1")).anyTimes();
        org.apache.ambari.server.topology.ClusterTopology topology = createClusterTopology(bp, clusterConfig, hostGroups);
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor updater = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor(topology);
        java.util.Set<java.lang.String> updatedConfigTypes = updater.doUpdateForClusterCreate();
        org.junit.Assert.assertEquals(com.google.common.collect.ImmutableSet.of("cluster-env", "hdfs-site", "ranger-tagsync-site"), updatedConfigTypes);
        java.util.Map<java.lang.String, java.lang.String> updatedRangerTagsyncSiteConfigurations = clusterConfig.getProperties().get("ranger-tagsync-site");
        org.junit.Assert.assertTrue("Expected property ranger.tagsync.atlas.hdfs.instance.clusterName.nameservice.mynameservice.ranger.service not found.", updatedRangerTagsyncSiteConfigurations.containsKey("ranger.tagsync.atlas.hdfs.instance.clusterName.nameservice.mynameservice.ranger.service"));
        org.junit.Assert.assertTrue("Expected property ranger.tagsync.atlas.hdfs.instance.clusterName.nameservice.mynameservicetwo.ranger.service not found.", updatedRangerTagsyncSiteConfigurations.containsKey("ranger.tagsync.atlas.hdfs.instance.clusterName.nameservice.mynameservicetwo.ranger.service"));
        org.junit.Assert.assertTrue("Expected property ranger.tagsync.atlas.hdfs.instance.clusterName.ranger.service not found.", updatedRangerTagsyncSiteConfigurations.containsKey("ranger.tagsync.atlas.hdfs.instance.clusterName.ranger.service"));
        org.junit.Assert.assertEquals("Expected name service clusterName_hadoop_mynameservice not found", "clusterName_hadoop_mynameservice", updatedRangerTagsyncSiteConfigurations.get("ranger.tagsync.atlas.hdfs.instance.clusterName.nameservice.mynameservice.ranger.service"));
        org.junit.Assert.assertEquals("Expected name service clusterName_hadoop_mynameservicetwo not found", "clusterName_hadoop_mynameservicetwo", updatedRangerTagsyncSiteConfigurations.get("ranger.tagsync.atlas.hdfs.instance.clusterName.nameservice.mynameservicetwo.ranger.service"));
        org.junit.Assert.assertEquals("Expected name service clusterName_hadoop_mynameservicetwo not found", "clusterName_hadoop_mynameservice", updatedRangerTagsyncSiteConfigurations.get("ranger.tagsync.atlas.hdfs.instance.clusterName.ranger.service"));
    }

    @org.junit.Test
    public void testDoUpdateForClusterWithNameNodeFederationEnabledTagsyncEnabledCustomRepoName() throws java.lang.Exception {
        final java.lang.String expectedNameService = "mynameservice";
        final java.lang.String expectedNameServiceTwo = "mynameservicetwo";
        final java.lang.String expectedHostName = "c6401.apache.ambari.org";
        final java.lang.String expectedHostNameTwo = "c6402.apache.ambari.org";
        final java.lang.String expectedHostNameThree = "c6403.apache.ambari.org";
        final java.lang.String expectedHostNameFour = "c6404.apache.ambari.org";
        final java.lang.String expectedPortNum = "808080";
        final java.lang.String expectedNodeOne = "nn1";
        final java.lang.String expectedNodeTwo = "nn2";
        final java.lang.String expectedNodeThree = "nn3";
        final java.lang.String expectedNodeFour = "nn4";
        final java.lang.String expectedHostGroupName = "host_group_1";
        final java.lang.String expectedHostGroupNameTwo = "host_group_2";
        final java.lang.String expectedHostGroupNameThree = "host-group-3";
        final java.lang.String expectedHostGroupNameFour = "host-group-4";
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> properties = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.lang.String> hdfsSiteProperties = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.lang.String> hbaseSiteProperties = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.lang.String> hadoopEnvProperties = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.lang.String> coreSiteProperties = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.lang.String> accumuloSiteProperties = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.lang.String> rangerHDFSPluginProperties = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.lang.String> rangerHDFSSecurityProperties = new java.util.HashMap<>();
        properties.put("hdfs-site", hdfsSiteProperties);
        properties.put("hadoop-env", hadoopEnvProperties);
        properties.put("core-site", coreSiteProperties);
        properties.put("hbase-site", hbaseSiteProperties);
        properties.put("accumulo-site", accumuloSiteProperties);
        properties.put("ranger-hdfs-plugin-properties", rangerHDFSPluginProperties);
        properties.put("ranger-hdfs-security", rangerHDFSSecurityProperties);
        hdfsSiteProperties.put("dfs.nameservices", (expectedNameService + ",") + expectedNameServiceTwo);
        hdfsSiteProperties.put("dfs.ha.namenodes.mynameservice", (expectedNodeOne + ", ") + expectedNodeTwo);
        hdfsSiteProperties.put("dfs.ha.namenodes." + expectedNameServiceTwo, (expectedNodeThree + ",") + expectedNodeFour);
        hdfsSiteProperties.put(("dfs.namenode.shared.edits.dir" + ".") + expectedNameService, ((("qjournal://" + org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.createExportedAddress(expectedPortNum, expectedHostGroupName)) + ";") + org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.createExportedAddress(expectedPortNum, expectedHostGroupNameTwo)) + "/ns1");
        hdfsSiteProperties.put(("dfs.namenode.shared.edits.dir" + ".") + expectedNameServiceTwo, ((("qjournal://" + org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.createExportedAddress(expectedPortNum, expectedHostGroupName)) + ";") + org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.createExportedAddress(expectedPortNum, expectedHostGroupNameTwo)) + "/ns2");
        hdfsSiteProperties.put((("dfs.namenode.https-address." + expectedNameService) + ".") + expectedNodeOne, org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.createExportedAddress(expectedPortNum, expectedHostGroupName));
        hdfsSiteProperties.put((("dfs.namenode.https-address." + expectedNameService) + ".") + expectedNodeTwo, org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.createExportedAddress(expectedPortNum, expectedHostGroupNameTwo));
        hdfsSiteProperties.put((("dfs.namenode.http-address." + expectedNameService) + ".") + expectedNodeOne, org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.createExportedAddress(expectedPortNum, expectedHostGroupName));
        hdfsSiteProperties.put((("dfs.namenode.http-address." + expectedNameService) + ".") + expectedNodeTwo, org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.createExportedAddress(expectedPortNum, expectedHostGroupNameTwo));
        hdfsSiteProperties.put((("dfs.namenode.rpc-address." + expectedNameService) + ".") + expectedNodeOne, org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.createExportedAddress(expectedPortNum, expectedHostGroupName));
        hdfsSiteProperties.put((("dfs.namenode.rpc-address." + expectedNameService) + ".") + expectedNodeTwo, org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.createExportedAddress(expectedPortNum, expectedHostGroupNameTwo));
        hdfsSiteProperties.put((("dfs.namenode.servicerpc-address." + expectedNameService) + ".") + expectedNodeOne, org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.createExportedAddress(expectedPortNum, expectedHostGroupName));
        hdfsSiteProperties.put((("dfs.namenode.servicerpc-address." + expectedNameService) + ".") + expectedNodeTwo, org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.createExportedAddress(expectedPortNum, expectedHostGroupNameTwo));
        hdfsSiteProperties.put((("dfs.namenode.https-address." + expectedNameServiceTwo) + ".") + expectedNodeThree, org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.createExportedAddress(expectedPortNum, expectedHostGroupNameThree));
        hdfsSiteProperties.put((("dfs.namenode.https-address." + expectedNameServiceTwo) + ".") + expectedNodeFour, org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.createExportedAddress(expectedPortNum, expectedHostGroupNameFour));
        hdfsSiteProperties.put((("dfs.namenode.http-address." + expectedNameServiceTwo) + ".") + expectedNodeThree, org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.createExportedAddress(expectedPortNum, expectedHostGroupNameThree));
        hdfsSiteProperties.put((("dfs.namenode.http-address." + expectedNameServiceTwo) + ".") + expectedNodeFour, org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.createExportedAddress(expectedPortNum, expectedHostGroupNameFour));
        hdfsSiteProperties.put((("dfs.namenode.rpc-address." + expectedNameServiceTwo) + ".") + expectedNodeThree, org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.createExportedAddress(expectedPortNum, expectedHostGroupNameThree));
        hdfsSiteProperties.put((("dfs.namenode.rpc-address." + expectedNameServiceTwo) + ".") + expectedNodeFour, org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.createExportedAddress(expectedPortNum, expectedHostGroupNameFour));
        hdfsSiteProperties.put((("dfs.namenode.servicerpc-address." + expectedNameServiceTwo) + ".") + expectedNodeThree, org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.createExportedAddress(expectedPortNum, expectedHostGroupNameThree));
        hdfsSiteProperties.put((("dfs.namenode.servicerpc-address." + expectedNameServiceTwo) + ".") + expectedNodeFour, org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.createExportedAddress(expectedPortNum, expectedHostGroupNameFour));
        hdfsSiteProperties.put("dfs.secondary.http.address", "localhost:8080");
        hdfsSiteProperties.put("dfs.namenode.secondary.http-address", "localhost:8080");
        hdfsSiteProperties.put("dfs.namenode.http-address", "localhost:8080");
        hdfsSiteProperties.put("dfs.namenode.https-address", "localhost:8081");
        hdfsSiteProperties.put("dfs.namenode.rpc-address", "localhost:8082");
        coreSiteProperties.put("fs.defaultFS", "hdfs://" + expectedNameService);
        hbaseSiteProperties.put("hbase.rootdir", ("hdfs://" + expectedNameService) + "/hbase/test/root/dir");
        accumuloSiteProperties.put("instance.volumes", ("hdfs://" + expectedNameService) + "/accumulo/test/instance/volumes");
        rangerHDFSPluginProperties.put("hadoop.rpc.protection", "authentication");
        rangerHDFSPluginProperties.put("REPOSITORY_CONFIG_USERNAME", "hadoop");
        rangerHDFSPluginProperties.put("REPOSITORY_CONFIG_PASSWORD", "hadoop");
        rangerHDFSPluginProperties.put("ranger-hdfs-plugin-enabled", "Yes");
        rangerHDFSSecurityProperties.put("ranger.plugin.hdfs.service.name", "hdfs_service");
        org.apache.ambari.server.topology.Configuration clusterConfig = new org.apache.ambari.server.topology.Configuration(properties, java.util.Collections.emptyMap());
        java.util.Collection<java.lang.String> hgComponents = new java.util.HashSet<>();
        hgComponents.add("NAMENODE");
        hgComponents.add("RANGER_ADMIN");
        hgComponents.add("RANGER_USERSYNC");
        hgComponents.add("RANGER_TAGSYNC");
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup group1 = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup(expectedHostGroupName, hgComponents, java.util.Collections.singleton(expectedHostName));
        java.util.Collection<java.lang.String> hgComponents2 = new java.util.HashSet<>();
        hgComponents2.add("NAMENODE");
        hgComponents2.add("ATLAS_SERVER");
        hgComponents2.add("ATLAS_CLIENT");
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup group2 = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup(expectedHostGroupNameTwo, hgComponents2, java.util.Collections.singleton(expectedHostNameTwo));
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup group3 = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup(expectedHostGroupNameThree, java.util.Collections.singleton("NAMENODE"), java.util.Collections.singleton(expectedHostNameThree));
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup group4 = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup(expectedHostGroupNameFour, java.util.Collections.singleton("NAMENODE"), java.util.Collections.singleton(expectedHostNameFour));
        java.util.Collection<org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup> hostGroups = new java.util.ArrayList<>();
        hostGroups.add(group1);
        hostGroups.add(group2);
        hostGroups.add(group3);
        hostGroups.add(group4);
        EasyMock.expect(stack.getCardinality("NAMENODE")).andReturn(new org.apache.ambari.server.topology.Cardinality("1-2")).anyTimes();
        EasyMock.expect(stack.getCardinality("SECONDARY_NAMENODE")).andReturn(new org.apache.ambari.server.topology.Cardinality("1")).anyTimes();
        org.apache.ambari.server.topology.ClusterTopology topology = createClusterTopology(bp, clusterConfig, hostGroups);
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor updater = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor(topology);
        java.util.Set<java.lang.String> updatedConfigTypes = updater.doUpdateForClusterCreate();
        org.junit.Assert.assertEquals(com.google.common.collect.ImmutableSet.of("cluster-env", "hdfs-site", "ranger-tagsync-site"), updatedConfigTypes);
        java.util.Map<java.lang.String, java.lang.String> updatedRangerTagsyncSiteConfigurations = clusterConfig.getProperties().get("ranger-tagsync-site");
        org.junit.Assert.assertTrue("Expected property ranger.tagsync.atlas.hdfs.instance.clusterName.nameservice.mynameservice.ranger.service not found.", updatedRangerTagsyncSiteConfigurations.containsKey("ranger.tagsync.atlas.hdfs.instance.clusterName.nameservice.mynameservice.ranger.service"));
        org.junit.Assert.assertTrue("Expected property ranger.tagsync.atlas.hdfs.instance.clusterName.nameservice.mynameservicetwo.ranger.service not found.", updatedRangerTagsyncSiteConfigurations.containsKey("ranger.tagsync.atlas.hdfs.instance.clusterName.nameservice.mynameservicetwo.ranger.service"));
        org.junit.Assert.assertTrue("Expected property ranger.tagsync.atlas.hdfs.instance.clusterName.ranger.service not found.", updatedRangerTagsyncSiteConfigurations.containsKey("ranger.tagsync.atlas.hdfs.instance.clusterName.ranger.service"));
        org.junit.Assert.assertEquals("Expected name service hdfs_service_mynameservice not found", "hdfs_service_mynameservice", updatedRangerTagsyncSiteConfigurations.get("ranger.tagsync.atlas.hdfs.instance.clusterName.nameservice.mynameservice.ranger.service"));
        org.junit.Assert.assertEquals("Expected name service hdfs_service_mynameservicetwo not found", "hdfs_service_mynameservicetwo", updatedRangerTagsyncSiteConfigurations.get("ranger.tagsync.atlas.hdfs.instance.clusterName.nameservice.mynameservicetwo.ranger.service"));
        org.junit.Assert.assertEquals("Expected name service hdfs_service_mynameservicetwo not found", "hdfs_service_mynameservice", updatedRangerTagsyncSiteConfigurations.get("ranger.tagsync.atlas.hdfs.instance.clusterName.ranger.service"));
    }

    @org.junit.Test
    public void testDoUpdateForClusterWithNameNodeFederationEnabledTagsyncEnabledPluginDisabled() throws java.lang.Exception {
        final java.lang.String expectedNameService = "mynameservice";
        final java.lang.String expectedNameServiceTwo = "mynameservicetwo";
        final java.lang.String expectedHostName = "c6401.apache.ambari.org";
        final java.lang.String expectedHostNameTwo = "c6402.apache.ambari.org";
        final java.lang.String expectedHostNameThree = "c6403.apache.ambari.org";
        final java.lang.String expectedHostNameFour = "c6404.apache.ambari.org";
        final java.lang.String expectedPortNum = "808080";
        final java.lang.String expectedNodeOne = "nn1";
        final java.lang.String expectedNodeTwo = "nn2";
        final java.lang.String expectedNodeThree = "nn3";
        final java.lang.String expectedNodeFour = "nn4";
        final java.lang.String expectedHostGroupName = "host_group_1";
        final java.lang.String expectedHostGroupNameTwo = "host_group_2";
        final java.lang.String expectedHostGroupNameThree = "host-group-3";
        final java.lang.String expectedHostGroupNameFour = "host-group-4";
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> properties = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.lang.String> hdfsSiteProperties = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.lang.String> hbaseSiteProperties = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.lang.String> hadoopEnvProperties = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.lang.String> coreSiteProperties = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.lang.String> accumuloSiteProperties = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.lang.String> rangerHDFSPluginProperties = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.lang.String> rangerHDFSSecurityProperties = new java.util.HashMap<>();
        properties.put("hdfs-site", hdfsSiteProperties);
        properties.put("hadoop-env", hadoopEnvProperties);
        properties.put("core-site", coreSiteProperties);
        properties.put("hbase-site", hbaseSiteProperties);
        properties.put("accumulo-site", accumuloSiteProperties);
        properties.put("ranger-hdfs-plugin-properties", rangerHDFSPluginProperties);
        properties.put("ranger-hdfs-security", rangerHDFSSecurityProperties);
        hdfsSiteProperties.put("dfs.nameservices", (expectedNameService + ",") + expectedNameServiceTwo);
        hdfsSiteProperties.put("dfs.ha.namenodes.mynameservice", (expectedNodeOne + ", ") + expectedNodeTwo);
        hdfsSiteProperties.put("dfs.ha.namenodes." + expectedNameServiceTwo, (expectedNodeThree + ",") + expectedNodeFour);
        hdfsSiteProperties.put(("dfs.namenode.shared.edits.dir" + ".") + expectedNameService, ((("qjournal://" + org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.createExportedAddress(expectedPortNum, expectedHostGroupName)) + ";") + org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.createExportedAddress(expectedPortNum, expectedHostGroupNameTwo)) + "/ns1");
        hdfsSiteProperties.put(("dfs.namenode.shared.edits.dir" + ".") + expectedNameServiceTwo, ((("qjournal://" + org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.createExportedAddress(expectedPortNum, expectedHostGroupName)) + ";") + org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.createExportedAddress(expectedPortNum, expectedHostGroupNameTwo)) + "/ns2");
        hdfsSiteProperties.put((("dfs.namenode.https-address." + expectedNameService) + ".") + expectedNodeOne, org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.createExportedAddress(expectedPortNum, expectedHostGroupName));
        hdfsSiteProperties.put((("dfs.namenode.https-address." + expectedNameService) + ".") + expectedNodeTwo, org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.createExportedAddress(expectedPortNum, expectedHostGroupNameTwo));
        hdfsSiteProperties.put((("dfs.namenode.http-address." + expectedNameService) + ".") + expectedNodeOne, org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.createExportedAddress(expectedPortNum, expectedHostGroupName));
        hdfsSiteProperties.put((("dfs.namenode.http-address." + expectedNameService) + ".") + expectedNodeTwo, org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.createExportedAddress(expectedPortNum, expectedHostGroupNameTwo));
        hdfsSiteProperties.put((("dfs.namenode.rpc-address." + expectedNameService) + ".") + expectedNodeOne, org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.createExportedAddress(expectedPortNum, expectedHostGroupName));
        hdfsSiteProperties.put((("dfs.namenode.rpc-address." + expectedNameService) + ".") + expectedNodeTwo, org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.createExportedAddress(expectedPortNum, expectedHostGroupNameTwo));
        hdfsSiteProperties.put((("dfs.namenode.servicerpc-address." + expectedNameService) + ".") + expectedNodeOne, org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.createExportedAddress(expectedPortNum, expectedHostGroupName));
        hdfsSiteProperties.put((("dfs.namenode.servicerpc-address." + expectedNameService) + ".") + expectedNodeTwo, org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.createExportedAddress(expectedPortNum, expectedHostGroupNameTwo));
        hdfsSiteProperties.put((("dfs.namenode.https-address." + expectedNameServiceTwo) + ".") + expectedNodeThree, org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.createExportedAddress(expectedPortNum, expectedHostGroupNameThree));
        hdfsSiteProperties.put((("dfs.namenode.https-address." + expectedNameServiceTwo) + ".") + expectedNodeFour, org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.createExportedAddress(expectedPortNum, expectedHostGroupNameFour));
        hdfsSiteProperties.put((("dfs.namenode.http-address." + expectedNameServiceTwo) + ".") + expectedNodeThree, org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.createExportedAddress(expectedPortNum, expectedHostGroupNameThree));
        hdfsSiteProperties.put((("dfs.namenode.http-address." + expectedNameServiceTwo) + ".") + expectedNodeFour, org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.createExportedAddress(expectedPortNum, expectedHostGroupNameFour));
        hdfsSiteProperties.put((("dfs.namenode.rpc-address." + expectedNameServiceTwo) + ".") + expectedNodeThree, org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.createExportedAddress(expectedPortNum, expectedHostGroupNameThree));
        hdfsSiteProperties.put((("dfs.namenode.rpc-address." + expectedNameServiceTwo) + ".") + expectedNodeFour, org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.createExportedAddress(expectedPortNum, expectedHostGroupNameFour));
        hdfsSiteProperties.put((("dfs.namenode.servicerpc-address." + expectedNameServiceTwo) + ".") + expectedNodeThree, org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.createExportedAddress(expectedPortNum, expectedHostGroupNameThree));
        hdfsSiteProperties.put((("dfs.namenode.servicerpc-address." + expectedNameServiceTwo) + ".") + expectedNodeFour, org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.createExportedAddress(expectedPortNum, expectedHostGroupNameFour));
        hdfsSiteProperties.put("dfs.secondary.http.address", "localhost:8080");
        hdfsSiteProperties.put("dfs.namenode.secondary.http-address", "localhost:8080");
        hdfsSiteProperties.put("dfs.namenode.http-address", "localhost:8080");
        hdfsSiteProperties.put("dfs.namenode.https-address", "localhost:8081");
        hdfsSiteProperties.put("dfs.namenode.rpc-address", "localhost:8082");
        coreSiteProperties.put("fs.defaultFS", "hdfs://" + expectedNameService);
        hbaseSiteProperties.put("hbase.rootdir", ("hdfs://" + expectedNameService) + "/hbase/test/root/dir");
        accumuloSiteProperties.put("instance.volumes", ("hdfs://" + expectedNameService) + "/accumulo/test/instance/volumes");
        rangerHDFSPluginProperties.put("hadoop.rpc.protection", "authentication");
        rangerHDFSPluginProperties.put("REPOSITORY_CONFIG_USERNAME", "hadoop");
        rangerHDFSPluginProperties.put("REPOSITORY_CONFIG_PASSWORD", "hadoop");
        rangerHDFSPluginProperties.put("ranger-hdfs-plugin-enabled", "No");
        rangerHDFSSecurityProperties.put("ranger.plugin.hdfs.service.name", "{{repo_name}}");
        org.apache.ambari.server.topology.Configuration clusterConfig = new org.apache.ambari.server.topology.Configuration(properties, java.util.Collections.emptyMap());
        java.util.Collection<java.lang.String> hgComponents = new java.util.HashSet<>();
        hgComponents.add("NAMENODE");
        hgComponents.add("RANGER_ADMIN");
        hgComponents.add("RANGER_USERSYNC");
        hgComponents.add("RANGER_TAGSYNC");
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup group1 = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup(expectedHostGroupName, hgComponents, java.util.Collections.singleton(expectedHostName));
        java.util.Collection<java.lang.String> hgComponents2 = new java.util.HashSet<>();
        hgComponents2.add("NAMENODE");
        hgComponents2.add("ATLAS_SERVER");
        hgComponents2.add("ATLAS_CLIENT");
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup group2 = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup(expectedHostGroupNameTwo, hgComponents2, java.util.Collections.singleton(expectedHostNameTwo));
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup group3 = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup(expectedHostGroupNameThree, java.util.Collections.singleton("NAMENODE"), java.util.Collections.singleton(expectedHostNameThree));
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup group4 = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup(expectedHostGroupNameFour, java.util.Collections.singleton("NAMENODE"), java.util.Collections.singleton(expectedHostNameFour));
        java.util.Collection<org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup> hostGroups = new java.util.ArrayList<>();
        hostGroups.add(group1);
        hostGroups.add(group2);
        hostGroups.add(group3);
        hostGroups.add(group4);
        EasyMock.expect(stack.getCardinality("NAMENODE")).andReturn(new org.apache.ambari.server.topology.Cardinality("1-2")).anyTimes();
        EasyMock.expect(stack.getCardinality("SECONDARY_NAMENODE")).andReturn(new org.apache.ambari.server.topology.Cardinality("1")).anyTimes();
        org.apache.ambari.server.topology.ClusterTopology topology = createClusterTopology(bp, clusterConfig, hostGroups);
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor updater = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor(topology);
        java.util.Set<java.lang.String> updatedConfigTypes = updater.doUpdateForClusterCreate();
        org.junit.Assert.assertEquals(com.google.common.collect.ImmutableSet.of("cluster-env", "hdfs-site"), updatedConfigTypes);
        java.util.Map<java.lang.String, java.lang.String> updatedRangerTagsyncSiteConfigurations = clusterConfig.getProperties().get("ranger-tagsync-site");
        org.junit.Assert.assertNull(updatedRangerTagsyncSiteConfigurations);
    }

    @org.junit.Test
    public void testDoUpdateForClusterWithNameNodeFederationEnabledTagsyncEnabledPluginEnabledNoAtlas() throws java.lang.Exception {
        final java.lang.String expectedNameService = "mynameservice";
        final java.lang.String expectedNameServiceTwo = "mynameservicetwo";
        final java.lang.String expectedHostName = "c6401.apache.ambari.org";
        final java.lang.String expectedHostNameTwo = "c6402.apache.ambari.org";
        final java.lang.String expectedHostNameThree = "c6403.apache.ambari.org";
        final java.lang.String expectedHostNameFour = "c6404.apache.ambari.org";
        final java.lang.String expectedPortNum = "808080";
        final java.lang.String expectedNodeOne = "nn1";
        final java.lang.String expectedNodeTwo = "nn2";
        final java.lang.String expectedNodeThree = "nn3";
        final java.lang.String expectedNodeFour = "nn4";
        final java.lang.String expectedHostGroupName = "host_group_1";
        final java.lang.String expectedHostGroupNameTwo = "host_group_2";
        final java.lang.String expectedHostGroupNameThree = "host-group-3";
        final java.lang.String expectedHostGroupNameFour = "host-group-4";
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> properties = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.lang.String> hdfsSiteProperties = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.lang.String> hbaseSiteProperties = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.lang.String> hadoopEnvProperties = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.lang.String> coreSiteProperties = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.lang.String> accumuloSiteProperties = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.lang.String> rangerHDFSPluginProperties = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.lang.String> rangerHDFSSecurityProperties = new java.util.HashMap<>();
        properties.put("hdfs-site", hdfsSiteProperties);
        properties.put("hadoop-env", hadoopEnvProperties);
        properties.put("core-site", coreSiteProperties);
        properties.put("hbase-site", hbaseSiteProperties);
        properties.put("accumulo-site", accumuloSiteProperties);
        properties.put("ranger-hdfs-plugin-properties", rangerHDFSPluginProperties);
        properties.put("ranger-hdfs-security", rangerHDFSSecurityProperties);
        hdfsSiteProperties.put("dfs.nameservices", (expectedNameService + ",") + expectedNameServiceTwo);
        hdfsSiteProperties.put("dfs.ha.namenodes.mynameservice", (expectedNodeOne + ", ") + expectedNodeTwo);
        hdfsSiteProperties.put("dfs.ha.namenodes." + expectedNameServiceTwo, (expectedNodeThree + ",") + expectedNodeFour);
        hdfsSiteProperties.put(("dfs.namenode.shared.edits.dir" + ".") + expectedNameService, ((("qjournal://" + org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.createExportedAddress(expectedPortNum, expectedHostGroupName)) + ";") + org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.createExportedAddress(expectedPortNum, expectedHostGroupNameTwo)) + "/ns1");
        hdfsSiteProperties.put(("dfs.namenode.shared.edits.dir" + ".") + expectedNameServiceTwo, ((("qjournal://" + org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.createExportedAddress(expectedPortNum, expectedHostGroupName)) + ";") + org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.createExportedAddress(expectedPortNum, expectedHostGroupNameTwo)) + "/ns2");
        hdfsSiteProperties.put((("dfs.namenode.https-address." + expectedNameService) + ".") + expectedNodeOne, org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.createExportedAddress(expectedPortNum, expectedHostGroupName));
        hdfsSiteProperties.put((("dfs.namenode.https-address." + expectedNameService) + ".") + expectedNodeTwo, org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.createExportedAddress(expectedPortNum, expectedHostGroupNameTwo));
        hdfsSiteProperties.put((("dfs.namenode.http-address." + expectedNameService) + ".") + expectedNodeOne, org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.createExportedAddress(expectedPortNum, expectedHostGroupName));
        hdfsSiteProperties.put((("dfs.namenode.http-address." + expectedNameService) + ".") + expectedNodeTwo, org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.createExportedAddress(expectedPortNum, expectedHostGroupNameTwo));
        hdfsSiteProperties.put((("dfs.namenode.rpc-address." + expectedNameService) + ".") + expectedNodeOne, org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.createExportedAddress(expectedPortNum, expectedHostGroupName));
        hdfsSiteProperties.put((("dfs.namenode.rpc-address." + expectedNameService) + ".") + expectedNodeTwo, org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.createExportedAddress(expectedPortNum, expectedHostGroupNameTwo));
        hdfsSiteProperties.put((("dfs.namenode.servicerpc-address." + expectedNameService) + ".") + expectedNodeOne, org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.createExportedAddress(expectedPortNum, expectedHostGroupName));
        hdfsSiteProperties.put((("dfs.namenode.servicerpc-address." + expectedNameService) + ".") + expectedNodeTwo, org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.createExportedAddress(expectedPortNum, expectedHostGroupNameTwo));
        hdfsSiteProperties.put((("dfs.namenode.https-address." + expectedNameServiceTwo) + ".") + expectedNodeThree, org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.createExportedAddress(expectedPortNum, expectedHostGroupNameThree));
        hdfsSiteProperties.put((("dfs.namenode.https-address." + expectedNameServiceTwo) + ".") + expectedNodeFour, org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.createExportedAddress(expectedPortNum, expectedHostGroupNameFour));
        hdfsSiteProperties.put((("dfs.namenode.http-address." + expectedNameServiceTwo) + ".") + expectedNodeThree, org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.createExportedAddress(expectedPortNum, expectedHostGroupNameThree));
        hdfsSiteProperties.put((("dfs.namenode.http-address." + expectedNameServiceTwo) + ".") + expectedNodeFour, org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.createExportedAddress(expectedPortNum, expectedHostGroupNameFour));
        hdfsSiteProperties.put((("dfs.namenode.rpc-address." + expectedNameServiceTwo) + ".") + expectedNodeThree, org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.createExportedAddress(expectedPortNum, expectedHostGroupNameThree));
        hdfsSiteProperties.put((("dfs.namenode.rpc-address." + expectedNameServiceTwo) + ".") + expectedNodeFour, org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.createExportedAddress(expectedPortNum, expectedHostGroupNameFour));
        hdfsSiteProperties.put((("dfs.namenode.servicerpc-address." + expectedNameServiceTwo) + ".") + expectedNodeThree, org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.createExportedAddress(expectedPortNum, expectedHostGroupNameThree));
        hdfsSiteProperties.put((("dfs.namenode.servicerpc-address." + expectedNameServiceTwo) + ".") + expectedNodeFour, org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.createExportedAddress(expectedPortNum, expectedHostGroupNameFour));
        hdfsSiteProperties.put("dfs.secondary.http.address", "localhost:8080");
        hdfsSiteProperties.put("dfs.namenode.secondary.http-address", "localhost:8080");
        hdfsSiteProperties.put("dfs.namenode.http-address", "localhost:8080");
        hdfsSiteProperties.put("dfs.namenode.https-address", "localhost:8081");
        hdfsSiteProperties.put("dfs.namenode.rpc-address", "localhost:8082");
        coreSiteProperties.put("fs.defaultFS", "hdfs://" + expectedNameService);
        hbaseSiteProperties.put("hbase.rootdir", ("hdfs://" + expectedNameService) + "/hbase/test/root/dir");
        accumuloSiteProperties.put("instance.volumes", ("hdfs://" + expectedNameService) + "/accumulo/test/instance/volumes");
        rangerHDFSPluginProperties.put("hadoop.rpc.protection", "authentication");
        rangerHDFSPluginProperties.put("REPOSITORY_CONFIG_USERNAME", "hadoop");
        rangerHDFSPluginProperties.put("REPOSITORY_CONFIG_PASSWORD", "hadoop");
        rangerHDFSPluginProperties.put("ranger-hdfs-plugin-enabled", "Yes");
        rangerHDFSSecurityProperties.put("ranger.plugin.hdfs.service.name", "{{repo_name}}");
        org.apache.ambari.server.topology.Configuration clusterConfig = new org.apache.ambari.server.topology.Configuration(properties, java.util.Collections.emptyMap());
        java.util.Collection<java.lang.String> hgComponents = new java.util.HashSet<>();
        hgComponents.add("NAMENODE");
        hgComponents.add("RANGER_ADMIN");
        hgComponents.add("RANGER_USERSYNC");
        hgComponents.add("RANGER_TAGSYNC");
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup group1 = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup(expectedHostGroupName, hgComponents, java.util.Collections.singleton(expectedHostName));
        java.util.Collection<java.lang.String> hgComponents2 = new java.util.HashSet<>();
        hgComponents2.add("NAMENODE");
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup group2 = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup(expectedHostGroupNameTwo, hgComponents2, java.util.Collections.singleton(expectedHostNameTwo));
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup group3 = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup(expectedHostGroupNameThree, java.util.Collections.singleton("NAMENODE"), java.util.Collections.singleton(expectedHostNameThree));
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup group4 = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup(expectedHostGroupNameFour, java.util.Collections.singleton("NAMENODE"), java.util.Collections.singleton(expectedHostNameFour));
        java.util.Collection<org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup> hostGroups = new java.util.ArrayList<>();
        hostGroups.add(group1);
        hostGroups.add(group2);
        hostGroups.add(group3);
        hostGroups.add(group4);
        EasyMock.expect(stack.getCardinality("NAMENODE")).andReturn(new org.apache.ambari.server.topology.Cardinality("1-2")).anyTimes();
        EasyMock.expect(stack.getCardinality("SECONDARY_NAMENODE")).andReturn(new org.apache.ambari.server.topology.Cardinality("1")).anyTimes();
        org.apache.ambari.server.topology.ClusterTopology topology = createClusterTopology(bp, clusterConfig, hostGroups);
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor updater = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor(topology);
        java.util.Set<java.lang.String> updatedConfigTypes = updater.doUpdateForClusterCreate();
        org.junit.Assert.assertEquals(com.google.common.collect.ImmutableSet.of("cluster-env", "hdfs-site"), updatedConfigTypes);
        java.util.Map<java.lang.String, java.lang.String> updatedRangerTagsyncSiteConfigurations = clusterConfig.getProperties().get("ranger-tagsync-site");
        org.junit.Assert.assertNull(updatedRangerTagsyncSiteConfigurations);
    }

    @org.junit.Test
    public void testDoUpdateForClusterWithNameNodeFederationEnabledTagsyncEnabledPluginEnabledNoTagsync() throws java.lang.Exception {
        final java.lang.String expectedNameService = "mynameservice";
        final java.lang.String expectedNameServiceTwo = "mynameservicetwo";
        final java.lang.String expectedHostName = "c6401.apache.ambari.org";
        final java.lang.String expectedHostNameTwo = "c6402.apache.ambari.org";
        final java.lang.String expectedHostNameThree = "c6403.apache.ambari.org";
        final java.lang.String expectedHostNameFour = "c6404.apache.ambari.org";
        final java.lang.String expectedPortNum = "808080";
        final java.lang.String expectedNodeOne = "nn1";
        final java.lang.String expectedNodeTwo = "nn2";
        final java.lang.String expectedNodeThree = "nn3";
        final java.lang.String expectedNodeFour = "nn4";
        final java.lang.String expectedHostGroupName = "host_group_1";
        final java.lang.String expectedHostGroupNameTwo = "host_group_2";
        final java.lang.String expectedHostGroupNameThree = "host-group-3";
        final java.lang.String expectedHostGroupNameFour = "host-group-4";
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> properties = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.lang.String> hdfsSiteProperties = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.lang.String> hbaseSiteProperties = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.lang.String> hadoopEnvProperties = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.lang.String> coreSiteProperties = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.lang.String> accumuloSiteProperties = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.lang.String> rangerHDFSPluginProperties = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.lang.String> rangerHDFSSecurityProperties = new java.util.HashMap<>();
        properties.put("hdfs-site", hdfsSiteProperties);
        properties.put("hadoop-env", hadoopEnvProperties);
        properties.put("core-site", coreSiteProperties);
        properties.put("hbase-site", hbaseSiteProperties);
        properties.put("accumulo-site", accumuloSiteProperties);
        properties.put("ranger-hdfs-plugin-properties", rangerHDFSPluginProperties);
        properties.put("ranger-hdfs-security", rangerHDFSSecurityProperties);
        hdfsSiteProperties.put("dfs.nameservices", (expectedNameService + ",") + expectedNameServiceTwo);
        hdfsSiteProperties.put("dfs.ha.namenodes.mynameservice", (expectedNodeOne + ", ") + expectedNodeTwo);
        hdfsSiteProperties.put("dfs.ha.namenodes." + expectedNameServiceTwo, (expectedNodeThree + ",") + expectedNodeFour);
        hdfsSiteProperties.put(("dfs.namenode.shared.edits.dir" + ".") + expectedNameService, ((("qjournal://" + org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.createExportedAddress(expectedPortNum, expectedHostGroupName)) + ";") + org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.createExportedAddress(expectedPortNum, expectedHostGroupNameTwo)) + "/ns1");
        hdfsSiteProperties.put(("dfs.namenode.shared.edits.dir" + ".") + expectedNameServiceTwo, ((("qjournal://" + org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.createExportedAddress(expectedPortNum, expectedHostGroupName)) + ";") + org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.createExportedAddress(expectedPortNum, expectedHostGroupNameTwo)) + "/ns2");
        hdfsSiteProperties.put((("dfs.namenode.https-address." + expectedNameService) + ".") + expectedNodeOne, org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.createExportedAddress(expectedPortNum, expectedHostGroupName));
        hdfsSiteProperties.put((("dfs.namenode.https-address." + expectedNameService) + ".") + expectedNodeTwo, org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.createExportedAddress(expectedPortNum, expectedHostGroupNameTwo));
        hdfsSiteProperties.put((("dfs.namenode.http-address." + expectedNameService) + ".") + expectedNodeOne, org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.createExportedAddress(expectedPortNum, expectedHostGroupName));
        hdfsSiteProperties.put((("dfs.namenode.http-address." + expectedNameService) + ".") + expectedNodeTwo, org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.createExportedAddress(expectedPortNum, expectedHostGroupNameTwo));
        hdfsSiteProperties.put((("dfs.namenode.rpc-address." + expectedNameService) + ".") + expectedNodeOne, org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.createExportedAddress(expectedPortNum, expectedHostGroupName));
        hdfsSiteProperties.put((("dfs.namenode.rpc-address." + expectedNameService) + ".") + expectedNodeTwo, org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.createExportedAddress(expectedPortNum, expectedHostGroupNameTwo));
        hdfsSiteProperties.put((("dfs.namenode.servicerpc-address." + expectedNameService) + ".") + expectedNodeOne, org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.createExportedAddress(expectedPortNum, expectedHostGroupName));
        hdfsSiteProperties.put((("dfs.namenode.servicerpc-address." + expectedNameService) + ".") + expectedNodeTwo, org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.createExportedAddress(expectedPortNum, expectedHostGroupNameTwo));
        hdfsSiteProperties.put((("dfs.namenode.https-address." + expectedNameServiceTwo) + ".") + expectedNodeThree, org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.createExportedAddress(expectedPortNum, expectedHostGroupNameThree));
        hdfsSiteProperties.put((("dfs.namenode.https-address." + expectedNameServiceTwo) + ".") + expectedNodeFour, org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.createExportedAddress(expectedPortNum, expectedHostGroupNameFour));
        hdfsSiteProperties.put((("dfs.namenode.http-address." + expectedNameServiceTwo) + ".") + expectedNodeThree, org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.createExportedAddress(expectedPortNum, expectedHostGroupNameThree));
        hdfsSiteProperties.put((("dfs.namenode.http-address." + expectedNameServiceTwo) + ".") + expectedNodeFour, org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.createExportedAddress(expectedPortNum, expectedHostGroupNameFour));
        hdfsSiteProperties.put((("dfs.namenode.rpc-address." + expectedNameServiceTwo) + ".") + expectedNodeThree, org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.createExportedAddress(expectedPortNum, expectedHostGroupNameThree));
        hdfsSiteProperties.put((("dfs.namenode.rpc-address." + expectedNameServiceTwo) + ".") + expectedNodeFour, org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.createExportedAddress(expectedPortNum, expectedHostGroupNameFour));
        hdfsSiteProperties.put((("dfs.namenode.servicerpc-address." + expectedNameServiceTwo) + ".") + expectedNodeThree, org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.createExportedAddress(expectedPortNum, expectedHostGroupNameThree));
        hdfsSiteProperties.put((("dfs.namenode.servicerpc-address." + expectedNameServiceTwo) + ".") + expectedNodeFour, org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.createExportedAddress(expectedPortNum, expectedHostGroupNameFour));
        hdfsSiteProperties.put("dfs.secondary.http.address", "localhost:8080");
        hdfsSiteProperties.put("dfs.namenode.secondary.http-address", "localhost:8080");
        hdfsSiteProperties.put("dfs.namenode.http-address", "localhost:8080");
        hdfsSiteProperties.put("dfs.namenode.https-address", "localhost:8081");
        hdfsSiteProperties.put("dfs.namenode.rpc-address", "localhost:8082");
        coreSiteProperties.put("fs.defaultFS", "hdfs://" + expectedNameService);
        hbaseSiteProperties.put("hbase.rootdir", ("hdfs://" + expectedNameService) + "/hbase/test/root/dir");
        accumuloSiteProperties.put("instance.volumes", ("hdfs://" + expectedNameService) + "/accumulo/test/instance/volumes");
        rangerHDFSPluginProperties.put("hadoop.rpc.protection", "authentication");
        rangerHDFSPluginProperties.put("REPOSITORY_CONFIG_USERNAME", "hadoop");
        rangerHDFSPluginProperties.put("REPOSITORY_CONFIG_PASSWORD", "hadoop");
        rangerHDFSPluginProperties.put("ranger-hdfs-plugin-enabled", "Yes");
        rangerHDFSSecurityProperties.put("ranger.plugin.hdfs.service.name", "{{repo_name}}");
        org.apache.ambari.server.topology.Configuration clusterConfig = new org.apache.ambari.server.topology.Configuration(properties, java.util.Collections.emptyMap());
        java.util.Collection<java.lang.String> hgComponents = new java.util.HashSet<>();
        hgComponents.add("NAMENODE");
        hgComponents.add("RANGER_ADMIN");
        hgComponents.add("RANGER_USERSYNC");
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup group1 = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup(expectedHostGroupName, hgComponents, java.util.Collections.singleton(expectedHostName));
        java.util.Collection<java.lang.String> hgComponents2 = new java.util.HashSet<>();
        hgComponents2.add("NAMENODE");
        hgComponents2.add("ATLAS_SERVER");
        hgComponents2.add("ATLAS_CLIENT");
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup group2 = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup(expectedHostGroupNameTwo, hgComponents2, java.util.Collections.singleton(expectedHostNameTwo));
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup group3 = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup(expectedHostGroupNameThree, java.util.Collections.singleton("NAMENODE"), java.util.Collections.singleton(expectedHostNameThree));
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup group4 = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup(expectedHostGroupNameFour, java.util.Collections.singleton("NAMENODE"), java.util.Collections.singleton(expectedHostNameFour));
        java.util.Collection<org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup> hostGroups = new java.util.ArrayList<>();
        hostGroups.add(group1);
        hostGroups.add(group2);
        hostGroups.add(group3);
        hostGroups.add(group4);
        EasyMock.expect(stack.getCardinality("NAMENODE")).andReturn(new org.apache.ambari.server.topology.Cardinality("1-2")).anyTimes();
        EasyMock.expect(stack.getCardinality("SECONDARY_NAMENODE")).andReturn(new org.apache.ambari.server.topology.Cardinality("1")).anyTimes();
        org.apache.ambari.server.topology.ClusterTopology topology = createClusterTopology(bp, clusterConfig, hostGroups);
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor updater = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor(topology);
        java.util.Set<java.lang.String> updatedConfigTypes = updater.doUpdateForClusterCreate();
        org.junit.Assert.assertEquals(com.google.common.collect.ImmutableSet.of("cluster-env", "hdfs-site"), updatedConfigTypes);
        java.util.Map<java.lang.String, java.lang.String> updatedRangerTagsyncSiteConfigurations = clusterConfig.getProperties().get("ranger-tagsync-site");
        org.junit.Assert.assertNull(updatedRangerTagsyncSiteConfigurations);
    }

    @org.junit.Test
    public void defaultConfigs() {
        org.apache.ambari.server.topology.Configuration stackConfig = org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.createTestStack();
        org.apache.ambari.server.topology.Configuration clusterConfig = stackConfig.copy();
        org.apache.ambari.server.topology.Configuration customConfig = org.apache.ambari.server.topology.Configuration.newEmpty();
        org.apache.ambari.server.topology.ClusterTopology topology = createNiceMock(org.apache.ambari.server.topology.ClusterTopology.class);
        org.apache.ambari.server.controller.internal.Stack stack = createNiceMock(org.apache.ambari.server.controller.internal.Stack.class);
        java.util.Set<java.lang.String> services = com.google.common.collect.ImmutableSet.of("HDFS");
        EasyMock.expect(stack.getServices()).andReturn(services).anyTimes();
        EasyMock.expect(stack.getConfiguration()).andReturn(stackConfig).anyTimes();
        EasyMock.expect(topology.getConfiguration()).andReturn(clusterConfig).anyTimes();
        EasyMock.expect(topology.getHostGroupInfo()).andReturn(java.util.Collections.emptyMap()).anyTimes();
        EasyMock.replay(stack, topology);
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor updater = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor(topology);
        updater.applyTypeSpecificFilter(org.apache.ambari.server.controller.internal.BlueprintExportType.MINIMAL, clusterConfig, stackConfig, services);
        org.junit.Assert.assertEquals(customConfig.getProperties(), clusterConfig.getProperties());
    }

    @org.junit.Test
    public void customConfigs() {
        org.apache.ambari.server.topology.Configuration stackConfig = org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.createTestStack();
        org.apache.ambari.server.topology.Configuration clusterConfig = stackConfig.copy();
        org.apache.ambari.server.topology.Configuration customConfig = org.apache.ambari.server.topology.Configuration.newEmpty();
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.customize(clusterConfig, customConfig, "core-site", "hadoop.security.authorization", "true");
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.customize(clusterConfig, customConfig, "core-site", "fs.trash.interval", "0");
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.customize(clusterConfig, customConfig, "hdfs-site", "dfs.webhdfs.enabled", "false");
        org.apache.ambari.server.topology.ClusterTopology topology = createNiceMock(org.apache.ambari.server.topology.ClusterTopology.class);
        org.apache.ambari.server.controller.internal.Stack stack = createNiceMock(org.apache.ambari.server.controller.internal.Stack.class);
        java.util.Set<java.lang.String> services = com.google.common.collect.ImmutableSet.of("HDFS");
        EasyMock.expect(stack.getServices()).andReturn(services).anyTimes();
        EasyMock.expect(stack.getConfiguration()).andReturn(stackConfig).anyTimes();
        EasyMock.expect(topology.getConfiguration()).andReturn(clusterConfig).anyTimes();
        EasyMock.expect(topology.getHostGroupInfo()).andReturn(java.util.Collections.emptyMap()).anyTimes();
        EasyMock.replay(stack, topology);
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor updater = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor(topology);
        updater.applyTypeSpecificFilter(org.apache.ambari.server.controller.internal.BlueprintExportType.MINIMAL, clusterConfig, stackConfig, services);
        org.junit.Assert.assertEquals(customConfig.getProperties(), clusterConfig.getProperties());
    }

    private static org.apache.ambari.server.topology.Configuration createTestStack() {
        org.apache.ambari.server.topology.Configuration stackConfig = org.apache.ambari.server.topology.Configuration.newEmpty();
        stackConfig.setProperty("core-site", "io.file.buffer.size", "131072");
        stackConfig.setProperty("core-site", "hadoop.security.authorization", "false");
        stackConfig.setProperty("core-site", "fs.trash.interval", "360");
        stackConfig.setProperty("hdfs-site", "dfs.namenode.name.dir", "/hadoop/hdfs/namenode");
        stackConfig.setProperty("hdfs-site", "dfs.datanode.data.dir", "/hadoop/hdfs/data");
        stackConfig.setProperty("hdfs-site", "dfs.webhdfs.enabled", "true");
        return stackConfig;
    }

    private static void customize(org.apache.ambari.server.topology.Configuration clusterConfig, org.apache.ambari.server.topology.Configuration customConfig, java.lang.String configType, java.lang.String propertyName, java.lang.String value) {
        clusterConfig.setProperty(configType, propertyName, value);
        customConfig.setProperty(configType, propertyName, value);
    }

    private static java.lang.String createExportedAddress(java.lang.String expectedPortNum, java.lang.String expectedHostGroupName) {
        return org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.createExportedHostName(expectedHostGroupName, expectedPortNum);
    }

    private static java.lang.String createExportedHostName(java.lang.String expectedHostGroupName, java.lang.String expectedPortNumber) {
        return (org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.createExportedHostName(expectedHostGroupName) + ":") + expectedPortNumber;
    }

    private static java.lang.String createExportedHostName(java.lang.String expectedHostGroupName) {
        return ("%HOSTGROUP::" + expectedHostGroupName) + "%";
    }

    private static java.lang.String createHostAddress(java.lang.String hostName, java.lang.String portNumber) {
        return (hostName + ":") + portNumber;
    }

    private org.apache.ambari.server.topology.Configuration createStackDefaults() {
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> stackDefaultProps = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.lang.String> coreSiteDefault = new java.util.HashMap<>();
        coreSiteDefault.put("fs.stackDefault.key1", "stackDefaultValue1");
        coreSiteDefault.put("fs.stackDefault.key2", "stackDefaultValue2");
        stackDefaultProps.put("core-site", coreSiteDefault);
        java.util.Map<java.lang.String, java.lang.String> dummySiteDefaults = new java.util.HashMap<>();
        dummySiteDefaults.put("dummy.prop", "dummyValue");
        stackDefaultProps.put("dummy-site", dummySiteDefaults);
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>>> stackDefaultAttributes = new java.util.HashMap<>();
        return new org.apache.ambari.server.topology.Configuration(stackDefaultProps, stackDefaultAttributes);
    }

    private org.apache.ambari.server.topology.ClusterTopology createClusterTopology(org.apache.ambari.server.topology.Blueprint blueprint, org.apache.ambari.server.topology.Configuration configuration, java.util.Collection<org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup> hostGroups) throws org.apache.ambari.server.topology.InvalidTopologyException {
        return createClusterTopology(blueprint, configuration, hostGroups, null);
    }

    private org.apache.ambari.server.topology.ClusterTopology createClusterTopology(org.apache.ambari.server.topology.Blueprint blueprint, org.apache.ambari.server.topology.Configuration configuration, java.util.Collection<org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup> hostGroups, org.apache.ambari.server.topology.AmbariContext ambariContextReplacement) throws org.apache.ambari.server.topology.InvalidTopologyException {
        EasyMock.replay(stack, serviceInfo, ambariContext, configHelper, controller, kerberosHelper, kerberosDescriptor, clusters, cluster);
        java.util.Map<java.lang.String, org.apache.ambari.server.topology.HostGroupInfo> hostGroupInfo = new java.util.HashMap<>();
        java.util.Collection<java.lang.String> allServices = new java.util.HashSet<>();
        java.util.Map<java.lang.String, org.apache.ambari.server.topology.HostGroup> allHostGroups = new java.util.HashMap<>();
        for (org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.TestHostGroup hostGroup : hostGroups) {
            org.apache.ambari.server.topology.HostGroupInfo groupInfo = new org.apache.ambari.server.topology.HostGroupInfo(hostGroup.name);
            groupInfo.addHosts(hostGroup.hosts);
            groupInfo.setConfiguration(hostGroup.configuration);
            java.util.List<org.apache.ambari.server.topology.Component> componentList = new java.util.ArrayList<>();
            for (java.lang.String componentName : hostGroup.components) {
                componentList.add(new org.apache.ambari.server.topology.Component(componentName));
            }
            allHostGroups.put(hostGroup.name, new org.apache.ambari.server.topology.HostGroupImpl(hostGroup.name, "test-bp", stack, componentList, org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessorTest.EMPTY_CONFIG, "1"));
            hostGroupInfo.put(hostGroup.name, groupInfo);
            for (java.lang.String component : hostGroup.components) {
                for (java.util.Map.Entry<java.lang.String, java.util.Collection<java.lang.String>> serviceComponentsEntry : serviceComponents.entrySet()) {
                    if (serviceComponentsEntry.getValue().contains(component)) {
                        allServices.add(serviceComponentsEntry.getKey());
                    }
                }
            }
        }
        EasyMock.expect(bp.getServices()).andReturn(allServices).anyTimes();
        for (org.apache.ambari.server.topology.HostGroup group : allHostGroups.values()) {
            EasyMock.expect(bp.getHostGroup(group.getName())).andReturn(group).anyTimes();
        }
        EasyMock.expect(bp.getHostGroups()).andReturn(allHostGroups).anyTimes();
        EasyMock.expect(topologyRequestMock.getClusterId()).andReturn(1L).anyTimes();
        EasyMock.expect(topologyRequestMock.getBlueprint()).andReturn(blueprint).anyTimes();
        EasyMock.expect(topologyRequestMock.getConfiguration()).andReturn(configuration).anyTimes();
        EasyMock.expect(topologyRequestMock.getHostGroupInfo()).andReturn(hostGroupInfo).anyTimes();
        EasyMock.replay(bp, topologyRequestMock);
        if (ambariContextReplacement != null) {
            ambariContext = ambariContextReplacement;
        }
        org.apache.ambari.server.topology.ClusterTopology topology = new org.apache.ambari.server.topology.ClusterTopologyImpl(ambariContext, topologyRequestMock);
        topology.setConfigRecommendationStrategy(org.apache.ambari.server.topology.ConfigRecommendationStrategy.NEVER_APPLY);
        return topology;
    }

    private class TestHostGroup {
        private java.lang.String name;

        private java.util.Collection<java.lang.String> components;

        private java.util.Collection<java.lang.String> hosts;

        private org.apache.ambari.server.topology.Configuration configuration;

        public TestHostGroup(java.lang.String name, java.util.Collection<java.lang.String> components, java.util.Collection<java.lang.String> hosts) {
            this.name = name;
            this.components = components;
            this.hosts = hosts;
            configuration = new org.apache.ambari.server.topology.Configuration(java.util.Collections.emptyMap(), java.util.Collections.emptyMap());
        }

        public TestHostGroup(java.lang.String name, java.util.Collection<java.lang.String> components, java.util.Collection<java.lang.String> hosts, org.apache.ambari.server.topology.Configuration configuration) {
            this.name = name;
            this.components = components;
            this.hosts = hosts;
            this.configuration = configuration;
        }
    }
}