package org.apache.ambari.server.topology;
import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.createNiceMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.mock;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
public class BlueprintImplTest {
    private static final java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>>> EMPTY_ATTRIBUTES = new java.util.HashMap<>();

    private static final java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> EMPTY_PROPERTIES = new java.util.HashMap<>();

    private static final org.apache.ambari.server.topology.Configuration EMPTY_CONFIGURATION = new org.apache.ambari.server.topology.Configuration(org.apache.ambari.server.topology.BlueprintImplTest.EMPTY_PROPERTIES, org.apache.ambari.server.topology.BlueprintImplTest.EMPTY_ATTRIBUTES);

    org.apache.ambari.server.controller.internal.Stack stack = EasyMock.createNiceMock(org.apache.ambari.server.controller.internal.Stack.class);

    org.apache.ambari.server.topology.Setting setting = EasyMock.createNiceMock(org.apache.ambari.server.topology.Setting.class);

    org.apache.ambari.server.topology.HostGroup group1 = EasyMock.createMock(org.apache.ambari.server.topology.HostGroup.class);

    org.apache.ambari.server.topology.HostGroup group2 = EasyMock.createMock(org.apache.ambari.server.topology.HostGroup.class);

    java.util.Set<org.apache.ambari.server.topology.HostGroup> hostGroups = new java.util.HashSet<>();

    java.util.Set<java.lang.String> group1Components = new java.util.HashSet<>();

    java.util.Set<java.lang.String> group2Components = new java.util.HashSet<>();

    java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> properties = new java.util.HashMap<>();

    java.util.Map<java.lang.String, java.lang.String> hdfsProps = new java.util.HashMap<>();

    org.apache.ambari.server.topology.Configuration configuration = new org.apache.ambari.server.topology.Configuration(properties, org.apache.ambari.server.topology.BlueprintImplTest.EMPTY_ATTRIBUTES, org.apache.ambari.server.topology.BlueprintImplTest.EMPTY_CONFIGURATION);

    org.apache.ambari.server.configuration.Configuration serverConfig;

    @org.junit.Before
    public void setup() throws java.lang.NoSuchFieldException, java.lang.IllegalAccessException {
        properties.put("hdfs-site", hdfsProps);
        hdfsProps.put("foo", "val");
        hdfsProps.put("bar", "val");
        hdfsProps.put("dfs.nameservices", "val");
        java.util.Map<java.lang.String, java.lang.String> category1Props = new java.util.HashMap<>();
        properties.put("category1", category1Props);
        category1Props.put("prop1", "val");
        hostGroups.add(group1);
        hostGroups.add(group2);
        group1Components.add("c1");
        group1Components.add("c2");
        group2Components.add("c1");
        group2Components.add("c3");
        EasyMock.expect(stack.isPasswordProperty("HDFS", "hdfs-site", "foo")).andReturn(false).anyTimes();
        EasyMock.expect(stack.isPasswordProperty("HDFS", "hdfs-site", "bar")).andReturn(false).anyTimes();
        EasyMock.expect(stack.isPasswordProperty("HDFS", "hdfs-site", "some_password")).andReturn(true).anyTimes();
        EasyMock.expect(stack.isPasswordProperty("HDFS", "category1", "prop1")).andReturn(false).anyTimes();
        EasyMock.expect(stack.isPasswordProperty("SERVICE2", "category2", "prop2")).andReturn(false).anyTimes();
        EasyMock.expect(stack.getServiceForComponent("c1")).andReturn("HDFS").anyTimes();
        EasyMock.expect(stack.getServiceForComponent("c2")).andReturn("HDFS").anyTimes();
        EasyMock.expect(stack.getServiceForComponent("c3")).andReturn("SERVICE2").anyTimes();
        EasyMock.expect(group1.getName()).andReturn("group1").anyTimes();
        EasyMock.expect(group2.getName()).andReturn("group2").anyTimes();
        EasyMock.expect(group1.getConfiguration()).andReturn(org.apache.ambari.server.topology.BlueprintImplTest.EMPTY_CONFIGURATION).anyTimes();
        EasyMock.expect(group1.getComponentNames()).andReturn(group1Components).anyTimes();
        EasyMock.expect(group2.getComponentNames()).andReturn(group2Components).anyTimes();
        java.util.Collection<org.apache.ambari.server.controller.internal.Stack.ConfigProperty> requiredHDFSProperties = new java.util.HashSet<>();
        requiredHDFSProperties.add(new org.apache.ambari.server.controller.internal.Stack.ConfigProperty("hdfs-site", "foo", null));
        requiredHDFSProperties.add(new org.apache.ambari.server.controller.internal.Stack.ConfigProperty("hdfs-site", "bar", null));
        requiredHDFSProperties.add(new org.apache.ambari.server.controller.internal.Stack.ConfigProperty("hdfs-site", "some_password", null));
        requiredHDFSProperties.add(new org.apache.ambari.server.controller.internal.Stack.ConfigProperty("category1", "prop1", null));
        java.util.Collection<org.apache.ambari.server.controller.internal.Stack.ConfigProperty> requiredService2Properties = new java.util.HashSet<>();
        requiredService2Properties.add(new org.apache.ambari.server.controller.internal.Stack.ConfigProperty("category2", "prop2", null));
        EasyMock.expect(stack.getRequiredConfigurationProperties("HDFS")).andReturn(requiredHDFSProperties).anyTimes();
        EasyMock.expect(stack.getRequiredConfigurationProperties("SERVICE2")).andReturn(requiredService2Properties).anyTimes();
        serverConfig = org.apache.ambari.server.topology.BlueprintImplTest.setupConfigurationWithGPLLicense(true);
    }

    @org.junit.Test
    public void testValidateConfigurations__basic_positive() throws java.lang.Exception {
        EasyMock.expect(group1.getCardinality()).andReturn("1").atLeastOnce();
        EasyMock.expect(group1.getComponents()).andReturn(java.util.Arrays.asList(new org.apache.ambari.server.topology.Component("c1"), new org.apache.ambari.server.topology.Component("c2"))).atLeastOnce();
        EasyMock.expect(group2.getCardinality()).andReturn("1").atLeastOnce();
        EasyMock.expect(group2.getComponents()).andReturn(java.util.Arrays.asList(new org.apache.ambari.server.topology.Component("c1"), new org.apache.ambari.server.topology.Component("c3"))).atLeastOnce();
        EasyMock.expect(group2.getConfiguration()).andReturn(org.apache.ambari.server.topology.BlueprintImplTest.EMPTY_CONFIGURATION).atLeastOnce();
        EasyMock.replay(stack, group1, group2, serverConfig);
        java.util.Map<java.lang.String, java.lang.String> category2Props = new java.util.HashMap<>();
        properties.put("category2", category2Props);
        category2Props.put("prop2", "val");
        org.apache.ambari.server.topology.SecurityConfiguration securityConfiguration = org.apache.ambari.server.topology.SecurityConfiguration.withReference("testRef");
        org.apache.ambari.server.topology.Blueprint blueprint = new org.apache.ambari.server.topology.BlueprintImpl("test", hostGroups, stack, configuration, securityConfiguration);
        blueprint.validateRequiredProperties();
        org.apache.ambari.server.orm.entities.BlueprintEntity entity = blueprint.toEntity();
        EasyMock.verify(stack, group1, group2, serverConfig);
        org.junit.Assert.assertTrue(entity.getSecurityType() == org.apache.ambari.server.state.SecurityType.KERBEROS);
        org.junit.Assert.assertTrue(entity.getSecurityDescriptorReference().equals("testRef"));
    }

    @org.junit.Test
    public void testValidateConfigurations__hostGroupConfig() throws java.lang.Exception {
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> group2Props = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.lang.String> group2Category2Props = new java.util.HashMap<>();
        group2Props.put("category2", group2Category2Props);
        group2Category2Props.put("prop2", "val");
        org.apache.ambari.server.topology.Configuration group2Configuration = new org.apache.ambari.server.topology.Configuration(group2Props, org.apache.ambari.server.topology.BlueprintImplTest.EMPTY_ATTRIBUTES, configuration);
        EasyMock.expect(group2.getConfiguration()).andReturn(group2Configuration).atLeastOnce();
        EasyMock.expect(group1.getCardinality()).andReturn("1").atLeastOnce();
        EasyMock.expect(group1.getComponents()).andReturn(java.util.Arrays.asList(new org.apache.ambari.server.topology.Component("NAMENODE"))).atLeastOnce();
        EasyMock.expect(group2.getCardinality()).andReturn("1").atLeastOnce();
        EasyMock.expect(group2.getComponents()).andReturn(java.util.Arrays.asList(new org.apache.ambari.server.topology.Component("NAMENODE"))).atLeastOnce();
        java.util.Map<java.lang.String, java.lang.String> category2Props = new java.util.HashMap<>();
        properties.put("category2", category2Props);
        category2Props.put("prop2", "val");
        group1Components.add("NAMENODE");
        group2Components.add("NAMENODE");
        java.util.Map<java.lang.String, java.lang.String> hdfsProps = new java.util.HashMap<>();
        properties.put("hdfs-site", hdfsProps);
        hdfsProps.put("foo", "val");
        hdfsProps.put("bar", "val");
        java.util.Map<java.lang.String, java.lang.String> hadoopProps = new java.util.HashMap<>();
        properties.put("hadoop-env", hadoopProps);
        hadoopProps.put("dfs_ha_initial_namenode_active", "%HOSTGROUP:group1%");
        hadoopProps.put("dfs_ha_initial_namenode_standby", "%HOSTGROUP:group2%");
        EasyMock.replay(stack, group1, group2, serverConfig);
        org.apache.ambari.server.topology.Blueprint blueprint = new org.apache.ambari.server.topology.BlueprintImpl("test", hostGroups, stack, configuration, null);
        blueprint.validateRequiredProperties();
        org.apache.ambari.server.orm.entities.BlueprintEntity entity = blueprint.toEntity();
        EasyMock.verify(stack, group1, group2, serverConfig);
        org.junit.Assert.assertTrue(entity.getSecurityType() == org.apache.ambari.server.state.SecurityType.NONE);
        org.junit.Assert.assertTrue(entity.getSecurityDescriptorReference() == null);
    }

    @org.junit.Test
    public void testValidateConfigurations__hostGroupConfigForNameNodeHAPositive() throws java.lang.Exception {
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> group2Props = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.lang.String> group2Category2Props = new java.util.HashMap<>();
        group2Props.put("category2", group2Category2Props);
        group2Category2Props.put("prop2", "val");
        org.apache.ambari.server.topology.Configuration group2Configuration = new org.apache.ambari.server.topology.Configuration(group2Props, org.apache.ambari.server.topology.BlueprintImplTest.EMPTY_ATTRIBUTES, configuration);
        EasyMock.expect(group2.getConfiguration()).andReturn(group2Configuration).atLeastOnce();
        EasyMock.expect(group1.getCardinality()).andReturn("1").atLeastOnce();
        EasyMock.expect(group1.getComponents()).andReturn(java.util.Arrays.asList(new org.apache.ambari.server.topology.Component("NAMENODE"), new org.apache.ambari.server.topology.Component("ZKFC"))).atLeastOnce();
        EasyMock.expect(group2.getCardinality()).andReturn("1").atLeastOnce();
        EasyMock.expect(group2.getComponents()).andReturn(java.util.Arrays.asList(new org.apache.ambari.server.topology.Component("NAMENODE"), new org.apache.ambari.server.topology.Component("ZKFC"))).atLeastOnce();
        java.util.Map<java.lang.String, java.lang.String> category2Props = new java.util.HashMap<>();
        properties.put("category2", category2Props);
        category2Props.put("prop2", "val");
        group1Components.add("NAMENODE");
        group1Components.add("ZKFC");
        group2Components.add("NAMENODE");
        group2Components.add("ZKFC");
        java.util.Map<java.lang.String, java.lang.String> hdfsProps = new java.util.HashMap<>();
        properties.put("hdfs-site", hdfsProps);
        hdfsProps.put("foo", "val");
        hdfsProps.put("bar", "val");
        hdfsProps.put("dfs.nameservices", "val");
        java.util.Map<java.lang.String, java.lang.String> hadoopProps = new java.util.HashMap<>();
        properties.put("hadoop-env", hadoopProps);
        hadoopProps.put("dfs_ha_initial_namenode_active", "%HOSTGROUP::group1%");
        hadoopProps.put("dfs_ha_initial_namenode_standby", "%HOSTGROUP::group2%");
        EasyMock.replay(stack, group1, group2, serverConfig);
        org.apache.ambari.server.topology.Blueprint blueprint = new org.apache.ambari.server.topology.BlueprintImpl("test", hostGroups, stack, configuration, null);
        blueprint.validateRequiredProperties();
        org.apache.ambari.server.orm.entities.BlueprintEntity entity = blueprint.toEntity();
        EasyMock.verify(stack, group1, group2, serverConfig);
        org.junit.Assert.assertTrue(entity.getSecurityType() == org.apache.ambari.server.state.SecurityType.NONE);
        org.junit.Assert.assertTrue(entity.getSecurityDescriptorReference() == null);
    }

    @org.junit.Test(expected = java.lang.IllegalArgumentException.class)
    public void testValidateConfigurations__hostGroupConfigForNameNodeHAInCorrectHostGroups() throws java.lang.Exception {
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> group2Props = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.lang.String> group2Category2Props = new java.util.HashMap<>();
        group2Props.put("category2", group2Category2Props);
        group2Category2Props.put("prop2", "val");
        org.apache.ambari.server.topology.Configuration group2Configuration = new org.apache.ambari.server.topology.Configuration(group2Props, org.apache.ambari.server.topology.BlueprintImplTest.EMPTY_ATTRIBUTES, configuration);
        EasyMock.expect(group2.getConfiguration()).andReturn(group2Configuration).atLeastOnce();
        EasyMock.expect(group1.getCardinality()).andReturn("1").atLeastOnce();
        EasyMock.expect(group1.getComponents()).andReturn(java.util.Arrays.asList(new org.apache.ambari.server.topology.Component("NAMENODE"), new org.apache.ambari.server.topology.Component("ZKFC"))).atLeastOnce();
        EasyMock.expect(group2.getCardinality()).andReturn("1").atLeastOnce();
        EasyMock.expect(group2.getComponents()).andReturn(java.util.Arrays.asList(new org.apache.ambari.server.topology.Component("NAMENODE"), new org.apache.ambari.server.topology.Component("ZKFC"))).atLeastOnce();
        java.util.Map<java.lang.String, java.lang.String> category2Props = new java.util.HashMap<>();
        properties.put("category2", category2Props);
        category2Props.put("prop2", "val");
        group1Components.add("NAMENODE");
        group1Components.add("ZKFC");
        group2Components.add("NAMENODE");
        group2Components.add("ZKFC");
        EasyMock.expect(stack.getServiceForComponent("NAMENODE")).andReturn("SERVICE2").atLeastOnce();
        EasyMock.expect(stack.getServiceForComponent("ZKFC")).andReturn("SERVICE2").atLeastOnce();
        java.util.Map<java.lang.String, java.lang.String> hdfsProps = new java.util.HashMap<>();
        properties.put("hdfs-site", hdfsProps);
        hdfsProps.put("foo", "val");
        hdfsProps.put("bar", "val");
        hdfsProps.put("dfs.nameservices", "val");
        java.util.Map<java.lang.String, java.lang.String> hadoopProps = new java.util.HashMap<>();
        properties.put("hadoop-env", hadoopProps);
        hadoopProps.put("dfs_ha_initial_namenode_active", "%HOSTGROUP::group2%");
        hadoopProps.put("dfs_ha_initial_namenode_standby", "%HOSTGROUP::group3%");
        EasyMock.replay(stack, group1, group2, serverConfig);
        org.apache.ambari.server.topology.Blueprint blueprint = new org.apache.ambari.server.topology.BlueprintImpl("test", hostGroups, stack, configuration, null);
        blueprint.validateRequiredProperties();
        EasyMock.verify(stack, group1, group2, serverConfig);
    }

    @org.junit.Test(expected = java.lang.IllegalArgumentException.class)
    public void testValidateConfigurations__hostGroupConfigForNameNodeHAMappedSameHostGroup() throws java.lang.Exception {
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> group2Props = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.lang.String> group2Category2Props = new java.util.HashMap<>();
        group2Props.put("category2", group2Category2Props);
        group2Category2Props.put("prop2", "val");
        org.apache.ambari.server.topology.Configuration group2Configuration = new org.apache.ambari.server.topology.Configuration(group2Props, org.apache.ambari.server.topology.BlueprintImplTest.EMPTY_ATTRIBUTES, configuration);
        EasyMock.expect(group2.getConfiguration()).andReturn(group2Configuration).atLeastOnce();
        EasyMock.expect(group1.getCardinality()).andReturn("1").atLeastOnce();
        EasyMock.expect(group1.getComponents()).andReturn(java.util.Arrays.asList(new org.apache.ambari.server.topology.Component("NAMENODE"), new org.apache.ambari.server.topology.Component("ZKFC"))).atLeastOnce();
        EasyMock.expect(group2.getCardinality()).andReturn("1").atLeastOnce();
        EasyMock.expect(group2.getComponents()).andReturn(java.util.Arrays.asList(new org.apache.ambari.server.topology.Component("NAMENODE"), new org.apache.ambari.server.topology.Component("ZKFC"))).atLeastOnce();
        java.util.Map<java.lang.String, java.lang.String> category2Props = new java.util.HashMap<>();
        properties.put("category2", category2Props);
        category2Props.put("prop2", "val");
        group1Components.add("NAMENODE");
        group1Components.add("ZKFC");
        group2Components.add("NAMENODE");
        group2Components.add("ZKFC");
        EasyMock.expect(stack.getServiceForComponent("NAMENODE")).andReturn("SERVICE2").atLeastOnce();
        EasyMock.expect(stack.getServiceForComponent("ZKFC")).andReturn("SERVICE2").atLeastOnce();
        java.util.Map<java.lang.String, java.lang.String> hdfsProps = new java.util.HashMap<>();
        properties.put("hdfs-site", hdfsProps);
        hdfsProps.put("foo", "val");
        hdfsProps.put("bar", "val");
        hdfsProps.put("dfs.nameservices", "val");
        java.util.Map<java.lang.String, java.lang.String> hadoopProps = new java.util.HashMap<>();
        properties.put("hadoop-env", hadoopProps);
        hadoopProps.put("dfs_ha_initial_namenode_active", "%HOSTGROUP::group2%");
        hadoopProps.put("dfs_ha_initial_namenode_standby", "%HOSTGROUP::group2%");
        EasyMock.replay(stack, group1, group2, serverConfig);
        org.apache.ambari.server.topology.Blueprint blueprint = new org.apache.ambari.server.topology.BlueprintImpl("test", hostGroups, stack, configuration, null);
        blueprint.validateRequiredProperties();
        EasyMock.verify(stack, group1, group2, serverConfig);
    }

    @org.junit.Test(expected = org.apache.ambari.server.topology.InvalidTopologyException.class)
    public void testValidateConfigurations__secretReference() throws org.apache.ambari.server.topology.InvalidTopologyException, org.apache.ambari.server.topology.GPLLicenseNotAcceptedException, java.lang.NoSuchFieldException, java.lang.IllegalAccessException {
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> group2Props = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.lang.String> group2Category2Props = new java.util.HashMap<>();
        group2Props.put("category2", group2Category2Props);
        group2Category2Props.put("prop2", "val");
        hdfsProps.put("secret", "SECRET:hdfs-site:1:test");
        EasyMock.replay(stack, group1, group2, serverConfig);
        org.apache.ambari.server.topology.Blueprint blueprint = new org.apache.ambari.server.topology.BlueprintImpl("test", hostGroups, stack, configuration, null);
        blueprint.validateRequiredProperties();
        EasyMock.verify(stack, group1, group2, serverConfig);
    }

    @org.junit.Test(expected = org.apache.ambari.server.topology.GPLLicenseNotAcceptedException.class)
    public void testValidateConfigurations__gplIsNotAllowedCodecsProperty() throws org.apache.ambari.server.topology.InvalidTopologyException, org.apache.ambari.server.topology.GPLLicenseNotAcceptedException, java.lang.NoSuchFieldException, java.lang.IllegalAccessException {
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> lzoProperties = new java.util.HashMap<>();
        lzoProperties.put("core-site", new java.util.HashMap<java.lang.String, java.lang.String>() {
            {
                put(org.apache.ambari.server.topology.BlueprintValidatorImpl.CODEC_CLASSES_PROPERTY_NAME, "OtherCodec, " + org.apache.ambari.server.topology.BlueprintValidatorImpl.LZO_CODEC_CLASS);
            }
        });
        org.apache.ambari.server.topology.Configuration lzoUsageConfiguration = new org.apache.ambari.server.topology.Configuration(lzoProperties, org.apache.ambari.server.topology.BlueprintImplTest.EMPTY_ATTRIBUTES, org.apache.ambari.server.topology.BlueprintImplTest.EMPTY_CONFIGURATION);
        serverConfig = org.apache.ambari.server.topology.BlueprintImplTest.setupConfigurationWithGPLLicense(false);
        EasyMock.replay(stack, group1, group2, serverConfig);
        org.apache.ambari.server.topology.Blueprint blueprint = new org.apache.ambari.server.topology.BlueprintImpl("test", hostGroups, stack, lzoUsageConfiguration, null);
        blueprint.validateRequiredProperties();
        EasyMock.verify(stack, group1, group2, serverConfig);
    }

    @org.junit.Test(expected = org.apache.ambari.server.topology.GPLLicenseNotAcceptedException.class)
    public void testValidateConfigurations__gplIsNotAllowedLZOProperty() throws org.apache.ambari.server.topology.InvalidTopologyException, org.apache.ambari.server.topology.GPLLicenseNotAcceptedException, java.lang.NoSuchFieldException, java.lang.IllegalAccessException {
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> lzoProperties = new java.util.HashMap<>();
        lzoProperties.put("core-site", new java.util.HashMap<java.lang.String, java.lang.String>() {
            {
                put(org.apache.ambari.server.topology.BlueprintValidatorImpl.LZO_CODEC_CLASS_PROPERTY_NAME, org.apache.ambari.server.topology.BlueprintValidatorImpl.LZO_CODEC_CLASS);
            }
        });
        org.apache.ambari.server.topology.Configuration lzoUsageConfiguration = new org.apache.ambari.server.topology.Configuration(lzoProperties, org.apache.ambari.server.topology.BlueprintImplTest.EMPTY_ATTRIBUTES, org.apache.ambari.server.topology.BlueprintImplTest.EMPTY_CONFIGURATION);
        serverConfig = org.apache.ambari.server.topology.BlueprintImplTest.setupConfigurationWithGPLLicense(false);
        EasyMock.replay(stack, group1, group2, serverConfig);
        org.apache.ambari.server.topology.Blueprint blueprint = new org.apache.ambari.server.topology.BlueprintImpl("test", hostGroups, stack, lzoUsageConfiguration, null);
        blueprint.validateRequiredProperties();
        EasyMock.verify(stack, group1, group2, serverConfig);
    }

    @org.junit.Test
    public void testValidateConfigurations__gplISAllowed() throws org.apache.ambari.server.topology.InvalidTopologyException, org.apache.ambari.server.topology.GPLLicenseNotAcceptedException, java.lang.NoSuchFieldException, java.lang.IllegalAccessException {
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> lzoProperties = new java.util.HashMap<>();
        lzoProperties.put("core-site", new java.util.HashMap<java.lang.String, java.lang.String>() {
            {
                put(org.apache.ambari.server.topology.BlueprintValidatorImpl.LZO_CODEC_CLASS_PROPERTY_NAME, org.apache.ambari.server.topology.BlueprintValidatorImpl.LZO_CODEC_CLASS);
                put(org.apache.ambari.server.topology.BlueprintValidatorImpl.CODEC_CLASSES_PROPERTY_NAME, "OtherCodec, " + org.apache.ambari.server.topology.BlueprintValidatorImpl.LZO_CODEC_CLASS);
            }
        });
        org.apache.ambari.server.topology.Configuration lzoUsageConfiguration = new org.apache.ambari.server.topology.Configuration(lzoProperties, org.apache.ambari.server.topology.BlueprintImplTest.EMPTY_ATTRIBUTES, org.apache.ambari.server.topology.BlueprintImplTest.EMPTY_CONFIGURATION);
        EasyMock.expect(group2.getConfiguration()).andReturn(org.apache.ambari.server.topology.BlueprintImplTest.EMPTY_CONFIGURATION).atLeastOnce();
        EasyMock.replay(stack, group1, group2, serverConfig);
        org.apache.ambari.server.topology.Blueprint blueprint = new org.apache.ambari.server.topology.BlueprintImpl("test", hostGroups, stack, lzoUsageConfiguration, null);
        blueprint.validateRequiredProperties();
        EasyMock.verify(stack, group1, group2, serverConfig);
    }

    @org.junit.Test
    public void testAutoSkipFailureEnabled() {
        org.apache.ambari.server.topology.Blueprint blueprint = new org.apache.ambari.server.topology.BlueprintImpl("test", hostGroups, stack, configuration, null, setting);
        java.util.HashMap<java.lang.String, java.lang.String> skipFailureSetting = new java.util.HashMap<>();
        skipFailureSetting.put(org.apache.ambari.server.topology.Setting.SETTING_NAME_SKIP_FAILURE, "true");
        EasyMock.expect(setting.getSettingValue(org.apache.ambari.server.topology.Setting.SETTING_NAME_DEPLOYMENT_SETTINGS)).andReturn(java.util.Collections.singleton(skipFailureSetting));
        EasyMock.replay(stack, setting);
        org.junit.Assert.assertTrue(blueprint.shouldSkipFailure());
        EasyMock.verify(stack, setting);
    }

    @org.junit.Test
    public void testAutoSkipFailureDisabled() {
        org.apache.ambari.server.topology.Blueprint blueprint = new org.apache.ambari.server.topology.BlueprintImpl("test", hostGroups, stack, configuration, null, setting);
        java.util.HashMap<java.lang.String, java.lang.String> skipFailureSetting = new java.util.HashMap<>();
        skipFailureSetting.put(org.apache.ambari.server.topology.Setting.SETTING_NAME_SKIP_FAILURE, "false");
        EasyMock.expect(setting.getSettingValue(org.apache.ambari.server.topology.Setting.SETTING_NAME_DEPLOYMENT_SETTINGS)).andReturn(java.util.Collections.singleton(skipFailureSetting));
        EasyMock.replay(stack, setting);
        org.junit.Assert.assertFalse(blueprint.shouldSkipFailure());
        EasyMock.verify(stack, setting);
    }

    public static org.apache.ambari.server.configuration.Configuration setupConfigurationWithGPLLicense(boolean isGPLAllowed) throws java.lang.NoSuchFieldException, java.lang.IllegalAccessException {
        org.apache.ambari.server.configuration.Configuration serverConfig = EasyMock.mock(org.apache.ambari.server.configuration.Configuration.class);
        EasyMock.expect(serverConfig.getGplLicenseAccepted()).andReturn(isGPLAllowed).atLeastOnce();
        java.lang.reflect.Field field = org.apache.ambari.server.topology.BlueprintValidatorImpl.class.getDeclaredField("configuration");
        field.setAccessible(true);
        field.set(null, serverConfig);
        return serverConfig;
    }
}