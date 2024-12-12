package org.apache.ambari.server.topology;
import org.easymock.EasyMockRule;
import org.easymock.EasyMockSupport;
import org.easymock.Mock;
import org.easymock.TestSubject;
import static org.easymock.EasyMock.expect;
public class RequiredPasswordValidatorTest extends org.easymock.EasyMockSupport {
    @org.junit.Rule
    public org.easymock.EasyMockRule mocks = new org.easymock.EasyMockRule(this);

    @org.easymock.Mock
    private org.apache.ambari.server.topology.ClusterTopology topology;

    @org.easymock.Mock
    private org.apache.ambari.server.topology.Blueprint blueprint;

    @org.easymock.Mock
    private org.apache.ambari.server.controller.internal.Stack stack;

    @org.easymock.Mock
    private org.apache.ambari.server.topology.HostGroup group1;

    @org.easymock.Mock
    private org.apache.ambari.server.topology.HostGroup group2;

    private static org.apache.ambari.server.topology.Configuration stackDefaults;

    private static org.apache.ambari.server.topology.Configuration bpClusterConfig;

    private static org.apache.ambari.server.topology.Configuration topoClusterConfig;

    private static org.apache.ambari.server.topology.Configuration bpGroup1Config;

    private static org.apache.ambari.server.topology.Configuration bpGroup2Config;

    private static org.apache.ambari.server.topology.Configuration topoGroup1Config;

    private static org.apache.ambari.server.topology.Configuration topoGroup2Config;

    private static final java.util.Map<java.lang.String, org.apache.ambari.server.topology.HostGroup> hostGroups = new java.util.HashMap<>();

    private static final java.util.Map<java.lang.String, org.apache.ambari.server.topology.HostGroupInfo> hostGroupInfo = new java.util.HashMap<>();

    private static final java.util.Collection<java.lang.String> group1Components = new java.util.HashSet<>();

    private static final java.util.Collection<java.lang.String> group2Components = new java.util.HashSet<>();

    private static final java.util.Collection<java.lang.String> service1Components = new java.util.HashSet<>();

    private static final java.util.Collection<java.lang.String> service2Components = new java.util.HashSet<>();

    private static final java.util.Collection<java.lang.String> service3Components = new java.util.HashSet<>();

    private static final java.util.Collection<org.apache.ambari.server.controller.internal.Stack.ConfigProperty> service1RequiredPwdConfigs = new java.util.HashSet<>();

    private static final java.util.Collection<org.apache.ambari.server.controller.internal.Stack.ConfigProperty> service2RequiredPwdConfigs = new java.util.HashSet<>();

    private static final java.util.Collection<org.apache.ambari.server.controller.internal.Stack.ConfigProperty> service3RequiredPwdConfigs = new java.util.HashSet<>();

    @org.easymock.TestSubject
    private org.apache.ambari.server.topology.validators.RequiredPasswordValidator validator = new org.apache.ambari.server.topology.validators.RequiredPasswordValidator();

    @org.junit.Before
    public void setup() {
        org.apache.ambari.server.topology.RequiredPasswordValidatorTest.stackDefaults = new org.apache.ambari.server.topology.Configuration(new java.util.HashMap<>(), new java.util.HashMap<>());
        org.apache.ambari.server.topology.RequiredPasswordValidatorTest.bpClusterConfig = new org.apache.ambari.server.topology.Configuration(new java.util.HashMap<>(), new java.util.HashMap<>(), org.apache.ambari.server.topology.RequiredPasswordValidatorTest.stackDefaults);
        org.apache.ambari.server.topology.RequiredPasswordValidatorTest.topoClusterConfig = new org.apache.ambari.server.topology.Configuration(new java.util.HashMap<>(), new java.util.HashMap<>(), org.apache.ambari.server.topology.RequiredPasswordValidatorTest.bpClusterConfig);
        org.apache.ambari.server.topology.RequiredPasswordValidatorTest.bpGroup1Config = new org.apache.ambari.server.topology.Configuration(new java.util.HashMap<>(), new java.util.HashMap<>(), org.apache.ambari.server.topology.RequiredPasswordValidatorTest.topoClusterConfig);
        org.apache.ambari.server.topology.RequiredPasswordValidatorTest.bpGroup2Config = new org.apache.ambari.server.topology.Configuration(new java.util.HashMap<>(), new java.util.HashMap<>(), org.apache.ambari.server.topology.RequiredPasswordValidatorTest.topoClusterConfig);
        org.apache.ambari.server.topology.RequiredPasswordValidatorTest.topoGroup1Config = new org.apache.ambari.server.topology.Configuration(new java.util.HashMap<>(), new java.util.HashMap<>(), org.apache.ambari.server.topology.RequiredPasswordValidatorTest.bpGroup1Config);
        org.apache.ambari.server.topology.RequiredPasswordValidatorTest.topoGroup2Config = new org.apache.ambari.server.topology.Configuration(new java.util.HashMap<>(), new java.util.HashMap<>(), org.apache.ambari.server.topology.RequiredPasswordValidatorTest.bpGroup2Config);
        org.apache.ambari.server.topology.RequiredPasswordValidatorTest.service1RequiredPwdConfigs.clear();
        org.apache.ambari.server.topology.RequiredPasswordValidatorTest.service2RequiredPwdConfigs.clear();
        org.apache.ambari.server.topology.RequiredPasswordValidatorTest.service3RequiredPwdConfigs.clear();
        org.apache.ambari.server.topology.RequiredPasswordValidatorTest.hostGroups.put("group1", group1);
        org.apache.ambari.server.topology.RequiredPasswordValidatorTest.hostGroups.put("group2", group2);
        org.apache.ambari.server.topology.RequiredPasswordValidatorTest.group1Components.add("component1");
        org.apache.ambari.server.topology.RequiredPasswordValidatorTest.group1Components.add("component2");
        org.apache.ambari.server.topology.RequiredPasswordValidatorTest.group1Components.add("component3");
        org.apache.ambari.server.topology.RequiredPasswordValidatorTest.group2Components.add("component1");
        org.apache.ambari.server.topology.RequiredPasswordValidatorTest.group2Components.add("component4");
        org.apache.ambari.server.topology.RequiredPasswordValidatorTest.service1Components.add("component1");
        org.apache.ambari.server.topology.RequiredPasswordValidatorTest.service1Components.add("component2");
        org.apache.ambari.server.topology.RequiredPasswordValidatorTest.service2Components.add("component3");
        org.apache.ambari.server.topology.RequiredPasswordValidatorTest.service3Components.add("component4");
        org.apache.ambari.server.topology.HostGroupInfo hostGroup1Info = new org.apache.ambari.server.topology.HostGroupInfo("group1");
        hostGroup1Info.setConfiguration(org.apache.ambari.server.topology.RequiredPasswordValidatorTest.topoGroup1Config);
        org.apache.ambari.server.topology.HostGroupInfo hostGroup2Info = new org.apache.ambari.server.topology.HostGroupInfo("group2");
        hostGroup2Info.setConfiguration(org.apache.ambari.server.topology.RequiredPasswordValidatorTest.topoGroup2Config);
        org.apache.ambari.server.topology.RequiredPasswordValidatorTest.hostGroupInfo.put("group1", hostGroup1Info);
        org.apache.ambari.server.topology.RequiredPasswordValidatorTest.hostGroupInfo.put("group2", hostGroup2Info);
        EasyMock.expect(topology.getConfiguration()).andReturn(org.apache.ambari.server.topology.RequiredPasswordValidatorTest.topoClusterConfig).anyTimes();
        EasyMock.expect(topology.getBlueprint()).andReturn(blueprint).anyTimes();
        EasyMock.expect(topology.getHostGroupInfo()).andReturn(org.apache.ambari.server.topology.RequiredPasswordValidatorTest.hostGroupInfo).anyTimes();
        EasyMock.expect(blueprint.getHostGroups()).andReturn(org.apache.ambari.server.topology.RequiredPasswordValidatorTest.hostGroups).anyTimes();
        EasyMock.expect(blueprint.getHostGroup("group1")).andReturn(group1).anyTimes();
        EasyMock.expect(blueprint.getHostGroup("group2")).andReturn(group2).anyTimes();
        EasyMock.expect(blueprint.getStack()).andReturn(stack).anyTimes();
        EasyMock.expect(group1.getComponentNames()).andReturn(org.apache.ambari.server.topology.RequiredPasswordValidatorTest.group1Components).anyTimes();
        EasyMock.expect(group2.getComponentNames()).andReturn(org.apache.ambari.server.topology.RequiredPasswordValidatorTest.group2Components).anyTimes();
        EasyMock.expect(group1.getComponents("service1")).andReturn(java.util.Arrays.asList("component1", "component2")).anyTimes();
        EasyMock.expect(group1.getComponents("service2")).andReturn(java.util.Arrays.asList("component3")).anyTimes();
        EasyMock.expect(group1.getComponents("service3")).andReturn(java.util.Collections.emptySet()).anyTimes();
        EasyMock.expect(group2.getComponents("service1")).andReturn(java.util.Arrays.asList("component1")).anyTimes();
        EasyMock.expect(group2.getComponents("service2")).andReturn(java.util.Collections.emptySet()).anyTimes();
        EasyMock.expect(group2.getComponents("service3")).andReturn(java.util.Arrays.asList("component4")).anyTimes();
        EasyMock.expect(stack.getServiceForComponent("component1")).andReturn("service1").anyTimes();
        EasyMock.expect(stack.getServiceForComponent("component2")).andReturn("service1").anyTimes();
        EasyMock.expect(stack.getServiceForComponent("component3")).andReturn("service2").anyTimes();
        EasyMock.expect(stack.getServiceForComponent("component4")).andReturn("service3").anyTimes();
        EasyMock.expect(stack.getRequiredConfigurationProperties("service1", org.apache.ambari.server.state.PropertyInfo.PropertyType.PASSWORD)).andReturn(org.apache.ambari.server.topology.RequiredPasswordValidatorTest.service1RequiredPwdConfigs).anyTimes();
        EasyMock.expect(stack.getRequiredConfigurationProperties("service2", org.apache.ambari.server.state.PropertyInfo.PropertyType.PASSWORD)).andReturn(org.apache.ambari.server.topology.RequiredPasswordValidatorTest.service2RequiredPwdConfigs).anyTimes();
        EasyMock.expect(stack.getRequiredConfigurationProperties("service3", org.apache.ambari.server.state.PropertyInfo.PropertyType.PASSWORD)).andReturn(org.apache.ambari.server.topology.RequiredPasswordValidatorTest.service3RequiredPwdConfigs).anyTimes();
    }

    @org.junit.After
    public void tearDown() {
        verifyAll();
        resetAll();
    }

    @org.junit.Test
    public void testValidate_noRequiredProps__noDefaultPwd() throws java.lang.Exception {
        EasyMock.expect(topology.getDefaultPassword()).andReturn(null);
        replayAll();
        validator.validate(topology);
    }

    @org.junit.Test
    public void testValidate_noRequiredProps__defaultPwd() throws java.lang.Exception {
        EasyMock.expect(topology.getDefaultPassword()).andReturn("pwd");
        replayAll();
        validator.validate(topology);
    }

    @org.junit.Test(expected = org.apache.ambari.server.topology.InvalidTopologyException.class)
    public void testValidate_missingPwd__NoDefaultPwd() throws java.lang.Exception {
        EasyMock.expect(topology.getDefaultPassword()).andReturn(null);
        replayAll();
        org.apache.ambari.server.controller.internal.Stack.ConfigProperty pwdProp = new org.apache.ambari.server.controller.internal.Stack.ConfigProperty("test-type", "pwdProp", null);
        org.apache.ambari.server.topology.RequiredPasswordValidatorTest.service1RequiredPwdConfigs.add(pwdProp);
        validator.validate(topology);
    }

    @org.junit.Test
    public void testValidate_missingPwd__defaultPwd() throws java.lang.Exception {
        EasyMock.expect(topology.getDefaultPassword()).andReturn("default-pwd");
        replayAll();
        org.apache.ambari.server.controller.internal.Stack.ConfigProperty pwdProp = new org.apache.ambari.server.controller.internal.Stack.ConfigProperty("test-type", "pwdProp", null);
        org.apache.ambari.server.topology.RequiredPasswordValidatorTest.service1RequiredPwdConfigs.add(pwdProp);
        validator.validate(topology);
        junit.framework.Assert.assertEquals(1, org.apache.ambari.server.topology.RequiredPasswordValidatorTest.topoClusterConfig.getProperties().size());
        junit.framework.Assert.assertEquals("default-pwd", org.apache.ambari.server.topology.RequiredPasswordValidatorTest.topoClusterConfig.getProperties().get("test-type").get("pwdProp"));
    }

    @org.junit.Test
    public void testValidate_pwdPropertyInTopoGroupConfig__NoDefaultPwd() throws java.lang.Exception {
        EasyMock.expect(topology.getDefaultPassword()).andReturn(null);
        replayAll();
        org.apache.ambari.server.controller.internal.Stack.ConfigProperty pwdProp = new org.apache.ambari.server.controller.internal.Stack.ConfigProperty("test-type", "pwdProp", null);
        org.apache.ambari.server.topology.RequiredPasswordValidatorTest.service3RequiredPwdConfigs.add(pwdProp);
        org.apache.ambari.server.topology.RequiredPasswordValidatorTest.topoGroup2Config.getProperties().put("test-type", java.util.Collections.singletonMap("pwdProp", "secret"));
        validator.validate(topology);
    }

    @org.junit.Test
    public void testValidate_pwdPropertyInTopoClusterConfig__NoDefaultPwd() throws java.lang.Exception {
        EasyMock.expect(topology.getDefaultPassword()).andReturn(null);
        replayAll();
        org.apache.ambari.server.controller.internal.Stack.ConfigProperty pwdProp = new org.apache.ambari.server.controller.internal.Stack.ConfigProperty("test-type", "pwdProp", null);
        org.apache.ambari.server.topology.RequiredPasswordValidatorTest.service3RequiredPwdConfigs.add(pwdProp);
        org.apache.ambari.server.topology.RequiredPasswordValidatorTest.topoClusterConfig.getProperties().put("test-type", java.util.Collections.singletonMap("pwdProp", "secret"));
        validator.validate(topology);
    }

    @org.junit.Test
    public void testValidate_pwdPropertyInBPGroupConfig__NoDefaultPwd() throws java.lang.Exception {
        EasyMock.expect(topology.getDefaultPassword()).andReturn(null);
        replayAll();
        org.apache.ambari.server.controller.internal.Stack.ConfigProperty pwdProp = new org.apache.ambari.server.controller.internal.Stack.ConfigProperty("test-type", "pwdProp", null);
        org.apache.ambari.server.topology.RequiredPasswordValidatorTest.service3RequiredPwdConfigs.add(pwdProp);
        org.apache.ambari.server.topology.RequiredPasswordValidatorTest.bpGroup2Config.getProperties().put("test-type", java.util.Collections.singletonMap("pwdProp", "secret"));
        validator.validate(topology);
    }

    @org.junit.Test
    public void testValidate_pwdPropertyInBPClusterConfig__NoDefaultPwd() throws java.lang.Exception {
        EasyMock.expect(topology.getDefaultPassword()).andReturn(null);
        replayAll();
        org.apache.ambari.server.controller.internal.Stack.ConfigProperty pwdProp = new org.apache.ambari.server.controller.internal.Stack.ConfigProperty("test-type", "pwdProp", null);
        org.apache.ambari.server.topology.RequiredPasswordValidatorTest.service3RequiredPwdConfigs.add(pwdProp);
        org.apache.ambari.server.topology.RequiredPasswordValidatorTest.bpClusterConfig.getProperties().put("test-type", java.util.Collections.singletonMap("pwdProp", "secret"));
        validator.validate(topology);
    }

    @org.junit.Test(expected = org.apache.ambari.server.topology.InvalidTopologyException.class)
    public void testValidate_pwdPropertyInStackConfig__NoDefaultPwd() throws java.lang.Exception {
        EasyMock.expect(topology.getDefaultPassword()).andReturn(null);
        replayAll();
        org.apache.ambari.server.controller.internal.Stack.ConfigProperty pwdProp = new org.apache.ambari.server.controller.internal.Stack.ConfigProperty("test-type", "pwdProp", null);
        org.apache.ambari.server.topology.RequiredPasswordValidatorTest.service3RequiredPwdConfigs.add(pwdProp);
        org.apache.ambari.server.topology.RequiredPasswordValidatorTest.stackDefaults.getProperties().put("test-type", java.util.Collections.singletonMap("pwdProp", "secret"));
        validator.validate(topology);
    }

    @org.junit.Test
    public void testValidate_twoRequiredPwdOneSpecified__defaultPwd() throws java.lang.Exception {
        EasyMock.expect(topology.getDefaultPassword()).andReturn("default-pwd");
        replayAll();
        org.apache.ambari.server.controller.internal.Stack.ConfigProperty pwdProp = new org.apache.ambari.server.controller.internal.Stack.ConfigProperty("test-type", "pwdProp", null);
        org.apache.ambari.server.controller.internal.Stack.ConfigProperty pwdProp2 = new org.apache.ambari.server.controller.internal.Stack.ConfigProperty("test2-type", "pwdProp2", null);
        org.apache.ambari.server.topology.RequiredPasswordValidatorTest.service1RequiredPwdConfigs.add(pwdProp);
        org.apache.ambari.server.topology.RequiredPasswordValidatorTest.service3RequiredPwdConfigs.add(pwdProp2);
        org.apache.ambari.server.topology.RequiredPasswordValidatorTest.topoClusterConfig.getProperties().put("test2-type", java.util.Collections.singletonMap("pwdProp2", "secret"));
        validator.validate(topology);
        junit.framework.Assert.assertEquals(2, org.apache.ambari.server.topology.RequiredPasswordValidatorTest.topoClusterConfig.getProperties().size());
        junit.framework.Assert.assertEquals("default-pwd", org.apache.ambari.server.topology.RequiredPasswordValidatorTest.topoClusterConfig.getProperties().get("test-type").get("pwdProp"));
        junit.framework.Assert.assertEquals("secret", org.apache.ambari.server.topology.RequiredPasswordValidatorTest.topoClusterConfig.getProperties().get("test2-type").get("pwdProp2"));
    }

    @org.junit.Test
    public void testValidate_twoRequiredPwdTwoSpecified__noDefaultPwd() throws java.lang.Exception {
        EasyMock.expect(topology.getDefaultPassword()).andReturn("default-pwd");
        replayAll();
        org.apache.ambari.server.controller.internal.Stack.ConfigProperty pwdProp = new org.apache.ambari.server.controller.internal.Stack.ConfigProperty("test-type", "pwdProp", null);
        org.apache.ambari.server.controller.internal.Stack.ConfigProperty pwdProp2 = new org.apache.ambari.server.controller.internal.Stack.ConfigProperty("test2-type", "pwdProp2", null);
        org.apache.ambari.server.topology.RequiredPasswordValidatorTest.service1RequiredPwdConfigs.add(pwdProp);
        org.apache.ambari.server.topology.RequiredPasswordValidatorTest.service3RequiredPwdConfigs.add(pwdProp2);
        org.apache.ambari.server.topology.RequiredPasswordValidatorTest.topoClusterConfig.getProperties().put("test2-type", java.util.Collections.singletonMap("pwdProp2", "secret2"));
        org.apache.ambari.server.topology.RequiredPasswordValidatorTest.topoClusterConfig.getProperties().put("test-type", java.util.Collections.singletonMap("pwdProp", "secret1"));
        validator.validate(topology);
        junit.framework.Assert.assertEquals(2, org.apache.ambari.server.topology.RequiredPasswordValidatorTest.topoClusterConfig.getProperties().size());
        junit.framework.Assert.assertEquals("secret1", org.apache.ambari.server.topology.RequiredPasswordValidatorTest.topoClusterConfig.getProperties().get("test-type").get("pwdProp"));
        junit.framework.Assert.assertEquals("secret2", org.apache.ambari.server.topology.RequiredPasswordValidatorTest.topoClusterConfig.getProperties().get("test2-type").get("pwdProp2"));
    }

    @org.junit.Test
    public void testValidate_multipleMissingPwd__defaultPwd() throws java.lang.Exception {
        EasyMock.expect(topology.getDefaultPassword()).andReturn("default-pwd");
        replayAll();
        org.apache.ambari.server.controller.internal.Stack.ConfigProperty pwdProp = new org.apache.ambari.server.controller.internal.Stack.ConfigProperty("test-type", "pwdProp", null);
        org.apache.ambari.server.controller.internal.Stack.ConfigProperty pwdProp2 = new org.apache.ambari.server.controller.internal.Stack.ConfigProperty("test2-type", "pwdProp2", null);
        org.apache.ambari.server.topology.RequiredPasswordValidatorTest.service1RequiredPwdConfigs.add(pwdProp);
        org.apache.ambari.server.topology.RequiredPasswordValidatorTest.service3RequiredPwdConfigs.add(pwdProp2);
        validator.validate(topology);
        junit.framework.Assert.assertEquals(2, org.apache.ambari.server.topology.RequiredPasswordValidatorTest.topoClusterConfig.getProperties().size());
        junit.framework.Assert.assertEquals("default-pwd", org.apache.ambari.server.topology.RequiredPasswordValidatorTest.topoClusterConfig.getProperties().get("test-type").get("pwdProp"));
        junit.framework.Assert.assertEquals("default-pwd", org.apache.ambari.server.topology.RequiredPasswordValidatorTest.topoClusterConfig.getProperties().get("test2-type").get("pwdProp2"));
    }
}