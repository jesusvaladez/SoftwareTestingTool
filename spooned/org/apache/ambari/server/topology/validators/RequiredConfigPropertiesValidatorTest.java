package org.apache.ambari.server.topology.validators;
import org.easymock.EasyMock;
import org.easymock.EasyMockRule;
import org.easymock.EasyMockSupport;
import org.easymock.Mock;
import org.easymock.TestSubject;
public class RequiredConfigPropertiesValidatorTest extends org.easymock.EasyMockSupport {
    @org.junit.Rule
    public org.easymock.EasyMockRule mocks = new org.easymock.EasyMockRule(this);

    @org.easymock.Mock
    private org.apache.ambari.server.topology.ClusterTopology clusterTopologyMock;

    @org.easymock.Mock
    private org.apache.ambari.server.topology.Configuration topologyConfigurationMock;

    @org.easymock.Mock
    private org.apache.ambari.server.topology.Blueprint blueprintMock;

    @org.easymock.Mock
    private org.apache.ambari.server.controller.internal.Stack stackMock;

    @org.easymock.Mock
    private org.apache.ambari.server.topology.HostGroup slaveHostGroupMock;

    @org.easymock.Mock
    private org.apache.ambari.server.topology.HostGroup masterHostGroupMock;

    @org.easymock.Mock
    private org.apache.ambari.server.topology.Configuration slaveHostGroupConfigurationMock;

    @org.easymock.Mock
    private org.apache.ambari.server.topology.Configuration masterHostGroupConfigurationMock;

    private java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> topologyConfigurationMap = new java.util.HashMap<>();

    private java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> masterHostGroupConfigurationMap = new java.util.HashMap<>();

    private java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> slaveHostGroupConfigurationMap = new java.util.HashMap<>();

    private java.util.Collection<java.lang.String> bpServices = new java.util.HashSet<>();

    private java.util.Collection<java.lang.String> slaveHostGroupServices = new java.util.HashSet<>();

    private java.util.Collection<java.lang.String> masterHostGroupServices = new java.util.HashSet<>();

    private java.util.Map<java.lang.String, org.apache.ambari.server.topology.HostGroup> hostGroups = new java.util.HashMap<>();

    private java.util.Map<java.lang.String, java.util.Collection<java.lang.String>> missingProps = new java.util.TreeMap<>();

    @org.easymock.TestSubject
    private org.apache.ambari.server.topology.validators.RequiredConfigPropertiesValidator testSubject = new org.apache.ambari.server.topology.validators.RequiredConfigPropertiesValidator();

    @org.junit.Before
    public void setup() {
        resetAll();
        org.easymock.EasyMock.expect(clusterTopologyMock.getConfiguration()).andReturn(topologyConfigurationMock);
        org.easymock.EasyMock.expect(topologyConfigurationMock.getFullProperties(1)).andReturn(topologyConfigurationMap);
        org.easymock.EasyMock.expect(clusterTopologyMock.getBlueprint()).andReturn(blueprintMock).anyTimes();
        org.easymock.EasyMock.expect(blueprintMock.getHostGroups()).andReturn(hostGroups);
        org.easymock.EasyMock.expect(blueprintMock.getServices()).andReturn(bpServices);
        org.easymock.EasyMock.expect(blueprintMock.getStack()).andReturn(stackMock).anyTimes();
        org.easymock.EasyMock.expect(masterHostGroupMock.getName()).andReturn("master").anyTimes();
        org.easymock.EasyMock.expect(masterHostGroupMock.getConfiguration()).andReturn(masterHostGroupConfigurationMock).anyTimes();
        org.easymock.EasyMock.expect(masterHostGroupMock.getServices()).andReturn(masterHostGroupServices);
        org.easymock.EasyMock.expect(slaveHostGroupMock.getName()).andReturn("slave").anyTimes();
        org.easymock.EasyMock.expect(slaveHostGroupMock.getConfiguration()).andReturn(slaveHostGroupConfigurationMock).anyTimes();
        org.easymock.EasyMock.expect(slaveHostGroupMock.getServices()).andReturn(slaveHostGroupServices);
        hostGroups.put("master", masterHostGroupMock);
        hostGroups.put("slave", slaveHostGroupMock);
        bpServices.addAll(com.google.common.collect.Lists.newArrayList("KERBEROS", "OOZIE"));
        masterHostGroupServices.addAll(java.util.Collections.singletonList("KERBEROS"));
        slaveHostGroupServices.addAll(java.util.Collections.singletonList("KERBEROS"));
        org.easymock.EasyMock.expect(masterHostGroupConfigurationMock.getProperties()).andReturn(masterHostGroupConfigurationMap);
        org.easymock.EasyMock.expect(slaveHostGroupConfigurationMock.getProperties()).andReturn(slaveHostGroupConfigurationMap);
        bpServices.addAll(com.google.common.collect.Lists.newArrayList("KERBEROS", "OOZIE"));
        org.easymock.EasyMock.expect(stackMock.getRequiredConfigurationProperties("KERBEROS")).andReturn(com.google.common.collect.Lists.newArrayList(new org.apache.ambari.server.controller.internal.Stack.ConfigProperty("kerberos-env", "realm", "value"), new org.apache.ambari.server.controller.internal.Stack.ConfigProperty("kerberos-env", "kdc_type", "value"), new org.apache.ambari.server.controller.internal.Stack.ConfigProperty("krb5-conf", "domains", "smthg")));
        org.easymock.EasyMock.expect(stackMock.getRequiredConfigurationProperties("OOZIE")).andReturn(java.util.Collections.EMPTY_LIST);
    }

    @org.junit.Test
    public void testShouldValidationFailWhenNoHostGroupConfigurationProvidedAndRequiredConfigTypesAreMissing() throws java.lang.Exception {
        topologyConfigurationMap.put("kerberos-env", new java.util.HashMap<>());
        topologyConfigurationMap.get("kerberos-env").put("realm", "etwas");
        topologyConfigurationMap.get("kerberos-env").put("kdc_type", "mit-kdc");
        missingProps.put("slave", new java.util.TreeSet<>(java.util.Collections.singletonList("domains")));
        missingProps.put("master", new java.util.TreeSet<>(java.util.Collections.singletonList("domains")));
        replayAll();
        java.lang.String expectedMsg = java.lang.String.format("Missing required properties.  Specify a value for these properties in the blueprint or cluster creation template configuration. %s", missingProps);
        java.lang.String actualMsg = "";
        try {
            testSubject.validate(clusterTopologyMock);
        } catch (org.apache.ambari.server.topology.InvalidTopologyException e) {
            actualMsg = e.getMessage();
        }
        org.junit.Assert.assertEquals("The exception message should be the expected one", expectedMsg, actualMsg);
    }

    @org.junit.Test
    public void testShouldValidationFailWhenNoHostGroupConfigurationProvidedAndRequiredPropertiesAreMissing() throws java.lang.Exception {
        topologyConfigurationMap.put("kerberos-env", new java.util.HashMap<>());
        topologyConfigurationMap.get("kerberos-env").put("realm", "etwas");
        topologyConfigurationMap.put("krb5-conf", new java.util.HashMap<>());
        topologyConfigurationMap.get("krb5-conf").put("domains", "smthg");
        missingProps.put("master", java.util.Collections.singletonList("kdc_type"));
        missingProps.put("slave", java.util.Collections.singletonList("kdc_type"));
        replayAll();
        java.lang.String expectedMsg = java.lang.String.format("Missing required properties.  Specify a value for these properties in the blueprint or cluster creation template configuration. %s", missingProps);
        java.lang.String actualMsg = "";
        try {
            testSubject.validate(clusterTopologyMock);
        } catch (org.apache.ambari.server.topology.InvalidTopologyException e) {
            actualMsg = e.getMessage();
        }
        org.junit.Assert.assertEquals("The exception message should be the expected one", expectedMsg, actualMsg);
    }

    @org.junit.Test
    public void testShouldValidationFailWhenHostGroupConfigurationProvidedAndRequiredConfigTypesAreMissingFromBothHostgroups() throws java.lang.Exception {
        missingProps.put("master", com.google.common.collect.Sets.newTreeSet(com.google.common.collect.Lists.newArrayList("kdc_type", "domains", "realm")));
        missingProps.put("slave", com.google.common.collect.Sets.newTreeSet(com.google.common.collect.Lists.newArrayList("kdc_type", "domains", "realm")));
        replayAll();
        java.lang.String expectedMsg = java.lang.String.format("Missing required properties.  Specify a value for these properties in the blueprint or cluster creation template configuration. %s", missingProps);
        java.lang.String actualMsg = "";
        try {
            testSubject.validate(clusterTopologyMock);
        } catch (org.apache.ambari.server.topology.InvalidTopologyException e) {
            actualMsg = e.getMessage();
        }
        org.junit.Assert.assertEquals("The exception message should be the expected one", expectedMsg, actualMsg);
    }

    @org.junit.Test
    public void testShouldValidationFailWhenHostGroupConfigurationProvidedAndRequiredConfigTypesAreMissingFromSlaveHostgroup() throws java.lang.Exception {
        masterHostGroupConfigurationMap.put("kerberos-env", new java.util.HashMap<>());
        masterHostGroupConfigurationMap.get("kerberos-env").put("realm", "etwas");
        masterHostGroupConfigurationMap.get("kerberos-env").put("kdc_type", "mit-kdc");
        masterHostGroupConfigurationMap.put("krb5-conf", new java.util.HashMap<>());
        masterHostGroupConfigurationMap.get("krb5-conf").put("domains", "smthg");
        missingProps.put("slave", com.google.common.collect.Sets.newTreeSet(com.google.common.collect.Lists.newArrayList("kdc_type", "domains", "realm")));
        replayAll();
        java.lang.String expectedMsg = java.lang.String.format("Missing required properties.  Specify a value for these properties in the blueprint or cluster creation template configuration. %s", missingProps);
        java.lang.String actualMsg = "";
        try {
            testSubject.validate(clusterTopologyMock);
        } catch (org.apache.ambari.server.topology.InvalidTopologyException e) {
            actualMsg = e.getMessage();
        }
        org.junit.Assert.assertEquals("The exception message should be the expected one", expectedMsg, actualMsg);
    }

    @org.junit.Test
    public void testShouldValidationPassWhenAllRequiredPropertiesAreProvidedInHostGroupConfiguration() throws java.lang.Exception {
        masterHostGroupConfigurationMap.put("kerberos-env", new java.util.HashMap<>());
        masterHostGroupConfigurationMap.get("kerberos-env").put("realm", "etwas");
        masterHostGroupConfigurationMap.get("kerberos-env").put("kdc_type", "mit-kdc");
        masterHostGroupConfigurationMap.put("krb5-conf", new java.util.HashMap<>());
        masterHostGroupConfigurationMap.get("krb5-conf").put("domains", "smthg");
        slaveHostGroupConfigurationMap.put("kerberos-env", new java.util.HashMap<>());
        slaveHostGroupConfigurationMap.get("kerberos-env").put("realm", "etwas");
        slaveHostGroupConfigurationMap.get("kerberos-env").put("kdc_type", "mit-kdc");
        slaveHostGroupConfigurationMap.put("krb5-conf", new java.util.HashMap<>());
        slaveHostGroupConfigurationMap.get("krb5-conf").put("domains", "smthg");
        replayAll();
        testSubject.validate(clusterTopologyMock);
    }

    @org.junit.Test
    public void testShouldValidationPassWhenAllRequiredPropertiesAreProvidedInTopologyConfiguration() throws java.lang.Exception {
        topologyConfigurationMap.put("kerberos-env", new java.util.HashMap<>());
        topologyConfigurationMap.get("kerberos-env").put("realm", "etwas");
        topologyConfigurationMap.get("kerberos-env").put("kdc_type", "value");
        topologyConfigurationMap.put("krb5-conf", new java.util.HashMap<>());
        topologyConfigurationMap.get("krb5-conf").put("domains", "smthg");
        replayAll();
        testSubject.validate(clusterTopologyMock);
    }
}