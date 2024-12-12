package org.apache.ambari.server.controller.internal;
import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.createNiceMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.verify;
import static org.powermock.api.easymock.PowerMock.createStrictMock;
import static org.powermock.api.easymock.PowerMock.replay;
import static org.powermock.api.easymock.PowerMock.reset;
@java.lang.SuppressWarnings("unchecked")
public class ProvisionClusterRequestTest {
    private static final java.lang.String CLUSTER_NAME = "cluster_name";

    private static final java.lang.String BLUEPRINT_NAME = "blueprint_name";

    private static final org.apache.ambari.server.topology.BlueprintFactory blueprintFactory = createStrictMock(org.apache.ambari.server.topology.BlueprintFactory.class);

    private static final org.apache.ambari.server.topology.Blueprint blueprint = EasyMock.createNiceMock(org.apache.ambari.server.topology.Blueprint.class);

    private static final org.apache.ambari.server.controller.spi.ResourceProvider hostResourceProvider = EasyMock.createMock(org.apache.ambari.server.controller.spi.ResourceProvider.class);

    private static final org.apache.ambari.server.topology.Configuration blueprintConfig = new org.apache.ambari.server.topology.Configuration(java.util.Collections.emptyMap(), java.util.Collections.emptyMap());

    @org.junit.Rule
    public org.junit.rules.ExpectedException expectedException = org.junit.rules.ExpectedException.none();

    @org.junit.Before
    public void setUp() throws java.lang.Exception {
        reset(org.apache.ambari.server.controller.internal.ProvisionClusterRequestTest.blueprintFactory, org.apache.ambari.server.controller.internal.ProvisionClusterRequestTest.blueprint, org.apache.ambari.server.controller.internal.ProvisionClusterRequestTest.hostResourceProvider);
        org.apache.ambari.server.controller.internal.ProvisionClusterRequest.init(org.apache.ambari.server.controller.internal.ProvisionClusterRequestTest.blueprintFactory);
        java.lang.Class clazz = org.apache.ambari.server.controller.internal.BaseClusterRequest.class;
        java.lang.reflect.Field f = clazz.getDeclaredField("hostResourceProvider");
        f.setAccessible(true);
        f.set(null, org.apache.ambari.server.controller.internal.ProvisionClusterRequestTest.hostResourceProvider);
        EasyMock.expect(org.apache.ambari.server.controller.internal.ProvisionClusterRequestTest.blueprintFactory.getBlueprint(org.apache.ambari.server.controller.internal.ProvisionClusterRequestTest.BLUEPRINT_NAME)).andReturn(org.apache.ambari.server.controller.internal.ProvisionClusterRequestTest.blueprint).once();
        EasyMock.expect(org.apache.ambari.server.controller.internal.ProvisionClusterRequestTest.blueprint.getConfiguration()).andReturn(org.apache.ambari.server.controller.internal.ProvisionClusterRequestTest.blueprintConfig).anyTimes();
        EasyMock.expect(org.apache.ambari.server.controller.internal.ProvisionClusterRequestTest.hostResourceProvider.checkPropertyIds(java.util.Collections.singleton("Hosts/host_name"))).andReturn(java.util.Collections.emptySet()).once();
        replay(org.apache.ambari.server.controller.internal.ProvisionClusterRequestTest.blueprintFactory, org.apache.ambari.server.controller.internal.ProvisionClusterRequestTest.blueprint, org.apache.ambari.server.controller.internal.ProvisionClusterRequestTest.hostResourceProvider);
    }

    @org.junit.After
    public void tearDown() {
        EasyMock.verify(org.apache.ambari.server.controller.internal.ProvisionClusterRequestTest.blueprintFactory, org.apache.ambari.server.controller.internal.ProvisionClusterRequestTest.blueprint, org.apache.ambari.server.controller.internal.ProvisionClusterRequestTest.hostResourceProvider);
    }

    @org.junit.Test
    public void testHostNameSpecified() throws java.lang.Exception {
        reset(org.apache.ambari.server.controller.internal.ProvisionClusterRequestTest.hostResourceProvider);
        replay(org.apache.ambari.server.controller.internal.ProvisionClusterRequestTest.hostResourceProvider);
        java.util.Map<java.lang.String, java.lang.Object> properties = org.apache.ambari.server.controller.internal.ProvisionClusterRequestTest.createBlueprintRequestPropertiesNameOnly(org.apache.ambari.server.controller.internal.ProvisionClusterRequestTest.CLUSTER_NAME, org.apache.ambari.server.controller.internal.ProvisionClusterRequestTest.BLUEPRINT_NAME);
        org.apache.ambari.server.controller.internal.ProvisionClusterRequest provisionClusterRequest = new org.apache.ambari.server.controller.internal.ProvisionClusterRequest(properties, null);
        org.junit.Assert.assertEquals(org.apache.ambari.server.controller.internal.ProvisionClusterRequestTest.CLUSTER_NAME, provisionClusterRequest.getClusterName());
        org.junit.Assert.assertEquals(org.apache.ambari.server.topology.TopologyRequest.Type.PROVISION, provisionClusterRequest.getType());
        org.junit.Assert.assertEquals(java.lang.String.format("Provision Cluster '%s'", org.apache.ambari.server.controller.internal.ProvisionClusterRequestTest.CLUSTER_NAME), provisionClusterRequest.getDescription());
        org.junit.Assert.assertSame(org.apache.ambari.server.controller.internal.ProvisionClusterRequestTest.blueprint, provisionClusterRequest.getBlueprint());
        java.util.Map<java.lang.String, org.apache.ambari.server.topology.HostGroupInfo> hostGroupInfo = provisionClusterRequest.getHostGroupInfo();
        org.junit.Assert.assertEquals(1, hostGroupInfo.size());
        org.apache.ambari.server.topology.HostGroupInfo group1Info = hostGroupInfo.get("group1");
        org.junit.Assert.assertEquals("group1", group1Info.getHostGroupName());
        org.junit.Assert.assertEquals(1, group1Info.getHostNames().size());
        org.junit.Assert.assertTrue(group1Info.getHostNames().contains("host1.mydomain.com"));
        org.junit.Assert.assertEquals(1, group1Info.getRequestedHostCount());
        org.junit.Assert.assertNull(group1Info.getPredicate());
        org.apache.ambari.server.topology.Configuration group1Configuration = group1Info.getConfiguration();
        org.junit.Assert.assertNull(group1Configuration.getParentConfiguration());
        org.junit.Assert.assertEquals(1, group1Configuration.getProperties().size());
        java.util.Map<java.lang.String, java.lang.String> group1TypeProperties = group1Configuration.getProperties().get("foo-type");
        org.junit.Assert.assertEquals(2, group1TypeProperties.size());
        org.junit.Assert.assertEquals("prop1Value", group1TypeProperties.get("hostGroup1Prop1"));
        org.junit.Assert.assertEquals("prop2Value", group1TypeProperties.get("hostGroup1Prop2"));
        org.junit.Assert.assertTrue(group1Configuration.getAttributes().isEmpty());
        org.apache.ambari.server.topology.Configuration clusterScopeConfiguration = provisionClusterRequest.getConfiguration();
        org.junit.Assert.assertSame(org.apache.ambari.server.controller.internal.ProvisionClusterRequestTest.blueprintConfig, clusterScopeConfiguration.getParentConfiguration());
        org.junit.Assert.assertEquals(1, clusterScopeConfiguration.getProperties().size());
        java.util.Map<java.lang.String, java.lang.String> clusterScopedProperties = clusterScopeConfiguration.getProperties().get("someType");
        org.junit.Assert.assertEquals(1, clusterScopedProperties.size());
        org.junit.Assert.assertEquals("someValue", clusterScopedProperties.get("property1"));
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>>> clusterScopedAttributes = clusterScopeConfiguration.getAttributes();
        org.junit.Assert.assertEquals(1, clusterScopedAttributes.size());
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> clusterScopedTypeAttributes = clusterScopedAttributes.get("someType");
        org.junit.Assert.assertEquals(1, clusterScopedTypeAttributes.size());
        java.util.Map<java.lang.String, java.lang.String> clusterScopedTypePropertyAttributes = clusterScopedTypeAttributes.get("attribute1");
        org.junit.Assert.assertEquals(1, clusterScopedTypePropertyAttributes.size());
        org.junit.Assert.assertEquals("someAttributePropValue", clusterScopedTypePropertyAttributes.get("property1"));
    }

    @org.junit.Test
    public void testHostCountSpecified() throws java.lang.Exception {
        reset(org.apache.ambari.server.controller.internal.ProvisionClusterRequestTest.hostResourceProvider);
        replay(org.apache.ambari.server.controller.internal.ProvisionClusterRequestTest.hostResourceProvider);
        java.util.Map<java.lang.String, java.lang.Object> properties = org.apache.ambari.server.controller.internal.ProvisionClusterRequestTest.createBlueprintRequestPropertiesCountOnly(org.apache.ambari.server.controller.internal.ProvisionClusterRequestTest.CLUSTER_NAME, org.apache.ambari.server.controller.internal.ProvisionClusterRequestTest.BLUEPRINT_NAME);
        org.apache.ambari.server.controller.internal.ProvisionClusterRequest provisionClusterRequest = new org.apache.ambari.server.controller.internal.ProvisionClusterRequest(properties, null);
        org.junit.Assert.assertEquals(org.apache.ambari.server.controller.internal.ProvisionClusterRequestTest.CLUSTER_NAME, provisionClusterRequest.getClusterName());
        org.junit.Assert.assertEquals(org.apache.ambari.server.topology.TopologyRequest.Type.PROVISION, provisionClusterRequest.getType());
        org.junit.Assert.assertEquals(java.lang.String.format("Provision Cluster '%s'", org.apache.ambari.server.controller.internal.ProvisionClusterRequestTest.CLUSTER_NAME), provisionClusterRequest.getDescription());
        org.junit.Assert.assertSame(org.apache.ambari.server.controller.internal.ProvisionClusterRequestTest.blueprint, provisionClusterRequest.getBlueprint());
        java.util.Map<java.lang.String, org.apache.ambari.server.topology.HostGroupInfo> hostGroupInfo = provisionClusterRequest.getHostGroupInfo();
        org.junit.Assert.assertEquals(1, hostGroupInfo.size());
        org.apache.ambari.server.topology.HostGroupInfo group2Info = hostGroupInfo.get("group2");
        org.junit.Assert.assertEquals("group2", group2Info.getHostGroupName());
        org.junit.Assert.assertTrue(group2Info.getHostNames().isEmpty());
        org.junit.Assert.assertEquals(5, group2Info.getRequestedHostCount());
        org.junit.Assert.assertNull(group2Info.getPredicate());
        org.apache.ambari.server.topology.Configuration group2Configuration = group2Info.getConfiguration();
        org.junit.Assert.assertNull(group2Configuration.getParentConfiguration());
        org.junit.Assert.assertEquals(1, group2Configuration.getProperties().size());
        java.util.Map<java.lang.String, java.lang.String> group2TypeProperties = group2Configuration.getProperties().get("foo-type");
        org.junit.Assert.assertEquals(1, group2TypeProperties.size());
        org.junit.Assert.assertEquals("prop1Value", group2TypeProperties.get("hostGroup2Prop1"));
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>>> group2Attributes = group2Configuration.getAttributes();
        org.junit.Assert.assertEquals(1, group2Attributes.size());
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> group2Type1Attributes = group2Attributes.get("foo-type");
        org.junit.Assert.assertEquals(1, group2Type1Attributes.size());
        java.util.Map<java.lang.String, java.lang.String> group2Type1Prop1Attributes = group2Type1Attributes.get("attribute1");
        org.junit.Assert.assertEquals(1, group2Type1Prop1Attributes.size());
        org.junit.Assert.assertEquals("attribute1Prop10-value", group2Type1Prop1Attributes.get("hostGroup2Prop10"));
        org.apache.ambari.server.topology.Configuration clusterScopeConfiguration = provisionClusterRequest.getConfiguration();
        org.junit.Assert.assertSame(org.apache.ambari.server.controller.internal.ProvisionClusterRequestTest.blueprintConfig, clusterScopeConfiguration.getParentConfiguration());
        org.junit.Assert.assertEquals(1, clusterScopeConfiguration.getProperties().size());
        java.util.Map<java.lang.String, java.lang.String> clusterScopedProperties = clusterScopeConfiguration.getProperties().get("someType");
        org.junit.Assert.assertEquals(1, clusterScopedProperties.size());
        org.junit.Assert.assertEquals("someValue", clusterScopedProperties.get("property1"));
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>>> clusterScopedAttributes = clusterScopeConfiguration.getAttributes();
        org.junit.Assert.assertEquals(1, clusterScopedAttributes.size());
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> clusterScopedTypeAttributes = clusterScopedAttributes.get("someType");
        org.junit.Assert.assertEquals(1, clusterScopedTypeAttributes.size());
        java.util.Map<java.lang.String, java.lang.String> clusterScopedTypePropertyAttributes = clusterScopedTypeAttributes.get("attribute1");
        org.junit.Assert.assertEquals(1, clusterScopedTypePropertyAttributes.size());
        org.junit.Assert.assertEquals("someAttributePropValue", clusterScopedTypePropertyAttributes.get("property1"));
    }

    @org.junit.Test
    public void testMultipleGroups() throws java.lang.Exception {
        java.util.Map<java.lang.String, java.lang.Object> properties = org.apache.ambari.server.controller.internal.ProvisionClusterRequestTest.createBlueprintRequestProperties(org.apache.ambari.server.controller.internal.ProvisionClusterRequestTest.CLUSTER_NAME, org.apache.ambari.server.controller.internal.ProvisionClusterRequestTest.BLUEPRINT_NAME);
        org.apache.ambari.server.controller.internal.ProvisionClusterRequest provisionClusterRequest = new org.apache.ambari.server.controller.internal.ProvisionClusterRequest(properties, null);
        org.junit.Assert.assertEquals(org.apache.ambari.server.controller.internal.ProvisionClusterRequestTest.CLUSTER_NAME, provisionClusterRequest.getClusterName());
        org.junit.Assert.assertEquals(org.apache.ambari.server.topology.TopologyRequest.Type.PROVISION, provisionClusterRequest.getType());
        org.junit.Assert.assertEquals(java.lang.String.format("Provision Cluster '%s'", org.apache.ambari.server.controller.internal.ProvisionClusterRequestTest.CLUSTER_NAME), provisionClusterRequest.getDescription());
        org.junit.Assert.assertSame(org.apache.ambari.server.controller.internal.ProvisionClusterRequestTest.blueprint, provisionClusterRequest.getBlueprint());
        java.util.Map<java.lang.String, org.apache.ambari.server.topology.HostGroupInfo> hostGroupInfo = provisionClusterRequest.getHostGroupInfo();
        org.junit.Assert.assertEquals(2, hostGroupInfo.size());
        org.apache.ambari.server.topology.HostGroupInfo group1Info = hostGroupInfo.get("group1");
        org.junit.Assert.assertEquals("group1", group1Info.getHostGroupName());
        org.junit.Assert.assertEquals(1, group1Info.getHostNames().size());
        org.junit.Assert.assertTrue(group1Info.getHostNames().contains("host1.mydomain.com"));
        org.junit.Assert.assertEquals(1, group1Info.getRequestedHostCount());
        org.junit.Assert.assertNull(group1Info.getPredicate());
        org.apache.ambari.server.topology.Configuration group1Configuration = group1Info.getConfiguration();
        org.junit.Assert.assertNull(group1Configuration.getParentConfiguration());
        org.junit.Assert.assertEquals(1, group1Configuration.getProperties().size());
        java.util.Map<java.lang.String, java.lang.String> group1TypeProperties = group1Configuration.getProperties().get("foo-type");
        org.junit.Assert.assertEquals(2, group1TypeProperties.size());
        org.junit.Assert.assertEquals("prop1Value", group1TypeProperties.get("hostGroup1Prop1"));
        org.junit.Assert.assertEquals("prop2Value", group1TypeProperties.get("hostGroup1Prop2"));
        org.junit.Assert.assertTrue(group1Configuration.getAttributes().isEmpty());
        org.apache.ambari.server.topology.HostGroupInfo group2Info = hostGroupInfo.get("group2");
        org.junit.Assert.assertEquals("group2", group2Info.getHostGroupName());
        org.junit.Assert.assertTrue(group2Info.getHostNames().isEmpty());
        org.junit.Assert.assertEquals(5, group2Info.getRequestedHostCount());
        org.junit.Assert.assertNotNull(group2Info.getPredicate());
        org.apache.ambari.server.topology.Configuration group2Configuration = group2Info.getConfiguration();
        org.junit.Assert.assertNull(group2Configuration.getParentConfiguration());
        org.junit.Assert.assertEquals(1, group2Configuration.getProperties().size());
        java.util.Map<java.lang.String, java.lang.String> group2TypeProperties = group2Configuration.getProperties().get("foo-type");
        org.junit.Assert.assertEquals(1, group2TypeProperties.size());
        org.junit.Assert.assertEquals("prop1Value", group2TypeProperties.get("hostGroup2Prop1"));
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>>> group2Attributes = group2Configuration.getAttributes();
        org.junit.Assert.assertEquals(1, group2Attributes.size());
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> group2Type1Attributes = group2Attributes.get("foo-type");
        org.junit.Assert.assertEquals(1, group2Type1Attributes.size());
        java.util.Map<java.lang.String, java.lang.String> group2Type1Prop1Attributes = group2Type1Attributes.get("attribute1");
        org.junit.Assert.assertEquals(1, group2Type1Prop1Attributes.size());
        org.junit.Assert.assertEquals("attribute1Prop10-value", group2Type1Prop1Attributes.get("hostGroup2Prop10"));
        org.apache.ambari.server.topology.Configuration clusterScopeConfiguration = provisionClusterRequest.getConfiguration();
        org.junit.Assert.assertSame(org.apache.ambari.server.controller.internal.ProvisionClusterRequestTest.blueprintConfig, clusterScopeConfiguration.getParentConfiguration());
        org.junit.Assert.assertEquals(1, clusterScopeConfiguration.getProperties().size());
        java.util.Map<java.lang.String, java.lang.String> clusterScopedProperties = clusterScopeConfiguration.getProperties().get("someType");
        org.junit.Assert.assertEquals(1, clusterScopedProperties.size());
        org.junit.Assert.assertEquals("someValue", clusterScopedProperties.get("property1"));
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>>> clusterScopedAttributes = clusterScopeConfiguration.getAttributes();
        org.junit.Assert.assertEquals(1, clusterScopedAttributes.size());
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> clusterScopedTypeAttributes = clusterScopedAttributes.get("someType");
        org.junit.Assert.assertEquals(1, clusterScopedTypeAttributes.size());
        java.util.Map<java.lang.String, java.lang.String> clusterScopedTypePropertyAttributes = clusterScopedTypeAttributes.get("attribute1");
        org.junit.Assert.assertEquals(1, clusterScopedTypePropertyAttributes.size());
        org.junit.Assert.assertEquals("someAttributePropValue", clusterScopedTypePropertyAttributes.get("property1"));
    }

    @org.junit.Test(expected = org.apache.ambari.server.topology.InvalidTopologyTemplateException.class)
    public void test_NoHostGroupInfo() throws java.lang.Exception {
        java.util.Map<java.lang.String, java.lang.Object> properties = org.apache.ambari.server.controller.internal.ProvisionClusterRequestTest.createBlueprintRequestProperties(org.apache.ambari.server.controller.internal.ProvisionClusterRequestTest.CLUSTER_NAME, org.apache.ambari.server.controller.internal.ProvisionClusterRequestTest.BLUEPRINT_NAME);
        ((java.util.Collection) (properties.get("host_groups"))).clear();
        reset(org.apache.ambari.server.controller.internal.ProvisionClusterRequestTest.hostResourceProvider);
        replay(org.apache.ambari.server.controller.internal.ProvisionClusterRequestTest.hostResourceProvider);
        new org.apache.ambari.server.controller.internal.ProvisionClusterRequest(properties, null);
    }

    @org.junit.Test
    public void test_Creditentials() throws java.lang.Exception {
        java.util.Map<java.lang.String, java.lang.Object> properties = org.apache.ambari.server.controller.internal.ProvisionClusterRequestTest.createBlueprintRequestProperties(org.apache.ambari.server.controller.internal.ProvisionClusterRequestTest.CLUSTER_NAME, org.apache.ambari.server.controller.internal.ProvisionClusterRequestTest.BLUEPRINT_NAME);
        java.util.HashMap<java.lang.String, java.lang.String> credentialHashMap = new java.util.HashMap<>();
        credentialHashMap.put("alias", "testAlias");
        credentialHashMap.put("principal", "testPrincipal");
        credentialHashMap.put("key", "testKey");
        credentialHashMap.put("type", "temporary");
        java.util.Set<java.util.Map<java.lang.String, java.lang.String>> credentialsSet = new java.util.HashSet<>();
        credentialsSet.add(credentialHashMap);
        properties.put("credentials", credentialsSet);
        org.apache.ambari.server.controller.internal.ProvisionClusterRequest provisionClusterRequest = new org.apache.ambari.server.controller.internal.ProvisionClusterRequest(properties, null);
        org.junit.Assert.assertEquals(provisionClusterRequest.getCredentialsMap().get("testAlias").getAlias(), "testAlias");
        org.junit.Assert.assertEquals(provisionClusterRequest.getCredentialsMap().get("testAlias").getPrincipal(), "testPrincipal");
        org.junit.Assert.assertEquals(provisionClusterRequest.getCredentialsMap().get("testAlias").getKey(), "testKey");
        org.junit.Assert.assertEquals(provisionClusterRequest.getCredentialsMap().get("testAlias").getType().name(), "TEMPORARY");
    }

    @org.junit.Test
    public void test_CreditentialsInvalidType() throws java.lang.Exception {
        expectedException.expect(org.apache.ambari.server.topology.InvalidTopologyTemplateException.class);
        expectedException.expectMessage("credential.type [TESTTYPE] is invalid. acceptable values: " + java.util.Arrays.toString(org.apache.ambari.server.security.encryption.CredentialStoreType.values()));
        java.util.Map<java.lang.String, java.lang.Object> properties = org.apache.ambari.server.controller.internal.ProvisionClusterRequestTest.createBlueprintRequestProperties(org.apache.ambari.server.controller.internal.ProvisionClusterRequestTest.CLUSTER_NAME, org.apache.ambari.server.controller.internal.ProvisionClusterRequestTest.BLUEPRINT_NAME);
        java.util.HashMap<java.lang.String, java.lang.String> credentialHashMap = new java.util.HashMap<>();
        credentialHashMap.put("alias", "testAlias");
        credentialHashMap.put("principal", "testPrincipal");
        credentialHashMap.put("key", "testKey");
        credentialHashMap.put("type", "testType");
        java.util.Set<java.util.Map<java.lang.String, java.lang.String>> credentialsSet = new java.util.HashSet<>();
        credentialsSet.add(credentialHashMap);
        properties.put("credentials", credentialsSet);
        org.apache.ambari.server.controller.internal.ProvisionClusterRequest provisionClusterRequest = new org.apache.ambari.server.controller.internal.ProvisionClusterRequest(properties, null);
    }

    @org.junit.Test(expected = org.apache.ambari.server.topology.InvalidTopologyTemplateException.class)
    public void test_GroupInfoMissingName() throws java.lang.Exception {
        java.util.Map<java.lang.String, java.lang.Object> properties = org.apache.ambari.server.controller.internal.ProvisionClusterRequestTest.createBlueprintRequestProperties(org.apache.ambari.server.controller.internal.ProvisionClusterRequestTest.CLUSTER_NAME, org.apache.ambari.server.controller.internal.ProvisionClusterRequestTest.BLUEPRINT_NAME);
        ((java.util.Collection<java.util.Map<java.lang.String, java.lang.Object>>) (properties.get("host_groups"))).iterator().next().remove("name");
        reset(org.apache.ambari.server.controller.internal.ProvisionClusterRequestTest.hostResourceProvider);
        replay(org.apache.ambari.server.controller.internal.ProvisionClusterRequestTest.hostResourceProvider);
        new org.apache.ambari.server.controller.internal.ProvisionClusterRequest(properties, null);
    }

    @org.junit.Test(expected = org.apache.ambari.server.topology.InvalidTopologyTemplateException.class)
    public void test_NoHostsInfo() throws java.lang.Exception {
        java.util.Map<java.lang.String, java.lang.Object> properties = org.apache.ambari.server.controller.internal.ProvisionClusterRequestTest.createBlueprintRequestProperties(org.apache.ambari.server.controller.internal.ProvisionClusterRequestTest.CLUSTER_NAME, org.apache.ambari.server.controller.internal.ProvisionClusterRequestTest.BLUEPRINT_NAME);
        ((java.util.Collection<java.util.Map<java.lang.String, java.lang.Object>>) (properties.get("host_groups"))).iterator().next().remove("hosts");
        reset(org.apache.ambari.server.controller.internal.ProvisionClusterRequestTest.hostResourceProvider);
        replay(org.apache.ambari.server.controller.internal.ProvisionClusterRequestTest.hostResourceProvider);
        new org.apache.ambari.server.controller.internal.ProvisionClusterRequest(properties, null);
    }

    @org.junit.Test(expected = org.apache.ambari.server.topology.InvalidTopologyTemplateException.class)
    public void test_NoHostNameOrHostCount() throws java.lang.Exception {
        java.util.Map<java.lang.String, java.lang.Object> properties = org.apache.ambari.server.controller.internal.ProvisionClusterRequestTest.createBlueprintRequestProperties(org.apache.ambari.server.controller.internal.ProvisionClusterRequestTest.CLUSTER_NAME, org.apache.ambari.server.controller.internal.ProvisionClusterRequestTest.BLUEPRINT_NAME);
        for (java.util.Map<java.lang.String, java.lang.Object> groupProps : ((java.util.Collection<java.util.Map<java.lang.String, java.lang.Object>>) (properties.get("host_groups")))) {
            java.util.Collection<java.util.Map<java.lang.String, java.lang.Object>> hostInfo = ((java.util.Collection<java.util.Map<java.lang.String, java.lang.Object>>) (groupProps.get("hosts")));
            java.util.Map<java.lang.String, java.lang.Object> next = hostInfo.iterator().next();
            if (next.containsKey("fqdn")) {
                next.remove("fqdn");
                break;
            }
        }
        reset(org.apache.ambari.server.controller.internal.ProvisionClusterRequestTest.hostResourceProvider);
        replay(org.apache.ambari.server.controller.internal.ProvisionClusterRequestTest.hostResourceProvider);
        new org.apache.ambari.server.controller.internal.ProvisionClusterRequest(properties, null);
    }

    @org.junit.Test(expected = org.apache.ambari.server.topology.InvalidTopologyTemplateException.class)
    public void testInvalidPredicateProperty() throws java.lang.Exception {
        reset(org.apache.ambari.server.controller.internal.ProvisionClusterRequestTest.hostResourceProvider);
        EasyMock.expect(org.apache.ambari.server.controller.internal.ProvisionClusterRequestTest.hostResourceProvider.checkPropertyIds(java.util.Collections.singleton("Hosts/host_name"))).andReturn(java.util.Collections.singleton("Hosts/host_name"));
        replay(org.apache.ambari.server.controller.internal.ProvisionClusterRequestTest.hostResourceProvider);
        new org.apache.ambari.server.controller.internal.ProvisionClusterRequest(org.apache.ambari.server.controller.internal.ProvisionClusterRequestTest.createBlueprintRequestProperties(org.apache.ambari.server.controller.internal.ProvisionClusterRequestTest.CLUSTER_NAME, org.apache.ambari.server.controller.internal.ProvisionClusterRequestTest.BLUEPRINT_NAME), null);
    }

    @org.junit.Test(expected = org.apache.ambari.server.topology.InvalidTopologyTemplateException.class)
    public void testHostNameAndCountSpecified() throws java.lang.Exception {
        reset(org.apache.ambari.server.controller.internal.ProvisionClusterRequestTest.hostResourceProvider);
        replay(org.apache.ambari.server.controller.internal.ProvisionClusterRequestTest.hostResourceProvider);
        java.util.Map<java.lang.String, java.lang.Object> properties = org.apache.ambari.server.controller.internal.ProvisionClusterRequestTest.createBlueprintRequestPropertiesNameOnly(org.apache.ambari.server.controller.internal.ProvisionClusterRequestTest.CLUSTER_NAME, org.apache.ambari.server.controller.internal.ProvisionClusterRequestTest.BLUEPRINT_NAME);
        ((java.util.Map) (((java.util.List) (properties.get("host_groups"))).iterator().next())).put("host_count", "5");
        new org.apache.ambari.server.controller.internal.ProvisionClusterRequest(properties, null);
    }

    @org.junit.Test(expected = org.apache.ambari.server.topology.InvalidTopologyTemplateException.class)
    public void testHostNameAndPredicateSpecified() throws java.lang.Exception {
        reset(org.apache.ambari.server.controller.internal.ProvisionClusterRequestTest.hostResourceProvider);
        replay(org.apache.ambari.server.controller.internal.ProvisionClusterRequestTest.hostResourceProvider);
        java.util.Map<java.lang.String, java.lang.Object> properties = org.apache.ambari.server.controller.internal.ProvisionClusterRequestTest.createBlueprintRequestPropertiesNameOnly(org.apache.ambari.server.controller.internal.ProvisionClusterRequestTest.CLUSTER_NAME, org.apache.ambari.server.controller.internal.ProvisionClusterRequestTest.BLUEPRINT_NAME);
        ((java.util.Map) (((java.util.List) (properties.get("host_groups"))).iterator().next())).put("host_predicate", "Hosts/host_name=myTestHost");
        new org.apache.ambari.server.controller.internal.ProvisionClusterRequest(properties, null);
    }

    @org.junit.Test
    public void testQuickLinksProfile_NoDataInRequest() throws java.lang.Exception {
        java.util.Map<java.lang.String, java.lang.Object> properties = org.apache.ambari.server.controller.internal.ProvisionClusterRequestTest.createBlueprintRequestProperties(org.apache.ambari.server.controller.internal.ProvisionClusterRequestTest.CLUSTER_NAME, org.apache.ambari.server.controller.internal.ProvisionClusterRequestTest.BLUEPRINT_NAME);
        org.apache.ambari.server.controller.internal.ProvisionClusterRequest request = new org.apache.ambari.server.controller.internal.ProvisionClusterRequest(properties, null);
        org.junit.Assert.assertNull("No quick links profile is expected", request.getQuickLinksProfileJson());
    }

    @org.junit.Test
    public void testQuickLinksProfile_OnlyGlobalFilterDataInRequest() throws java.lang.Exception {
        java.util.Map<java.lang.String, java.lang.Object> properties = org.apache.ambari.server.controller.internal.ProvisionClusterRequestTest.createBlueprintRequestProperties(org.apache.ambari.server.controller.internal.ProvisionClusterRequestTest.CLUSTER_NAME, org.apache.ambari.server.controller.internal.ProvisionClusterRequestTest.BLUEPRINT_NAME);
        properties.put(org.apache.ambari.server.controller.internal.ProvisionClusterRequest.QUICKLINKS_PROFILE_FILTERS_PROPERTY, com.google.common.collect.Sets.newHashSet(org.apache.ambari.server.state.quicklinksprofile.QuickLinksProfileBuilderTest.filter(null, null, true)));
        org.apache.ambari.server.controller.internal.ProvisionClusterRequest request = new org.apache.ambari.server.controller.internal.ProvisionClusterRequest(properties, null);
        org.junit.Assert.assertEquals("Quick links profile doesn't match expected", "{\"filters\":[{\"visible\":true}]}", request.getQuickLinksProfileJson());
    }

    @org.junit.Test
    public void testQuickLinksProfile_OnlyServiceFilterDataInRequest() throws java.lang.Exception {
        java.util.Map<java.lang.String, java.lang.Object> properties = org.apache.ambari.server.controller.internal.ProvisionClusterRequestTest.createBlueprintRequestProperties(org.apache.ambari.server.controller.internal.ProvisionClusterRequestTest.CLUSTER_NAME, org.apache.ambari.server.controller.internal.ProvisionClusterRequestTest.BLUEPRINT_NAME);
        java.util.Map<java.lang.String, java.lang.String> filter = org.apache.ambari.server.state.quicklinksprofile.QuickLinksProfileBuilderTest.filter(null, null, true);
        java.util.Map<java.lang.String, java.lang.Object> hdfs = org.apache.ambari.server.state.quicklinksprofile.QuickLinksProfileBuilderTest.service("HDFS", null, com.google.common.collect.Sets.newHashSet(filter));
        java.util.Set<java.util.Map<java.lang.String, java.lang.Object>> services = com.google.common.collect.Sets.newHashSet(hdfs);
        properties.put(org.apache.ambari.server.controller.internal.ProvisionClusterRequest.QUICKLINKS_PROFILE_SERVICES_PROPERTY, services);
        org.apache.ambari.server.controller.internal.ProvisionClusterRequest request = new org.apache.ambari.server.controller.internal.ProvisionClusterRequest(properties, null);
        org.junit.Assert.assertEquals("Quick links profile doesn't match expected", "{\"services\":[{\"name\":\"HDFS\",\"filters\":[{\"visible\":true}]}]}", request.getQuickLinksProfileJson());
    }

    @org.junit.Test
    public void testQuickLinksProfile_BothGlobalAndServiceLevelFilters() throws java.lang.Exception {
        java.util.Map<java.lang.String, java.lang.Object> properties = org.apache.ambari.server.controller.internal.ProvisionClusterRequestTest.createBlueprintRequestProperties(org.apache.ambari.server.controller.internal.ProvisionClusterRequestTest.CLUSTER_NAME, org.apache.ambari.server.controller.internal.ProvisionClusterRequestTest.BLUEPRINT_NAME);
        properties.put(org.apache.ambari.server.controller.internal.ProvisionClusterRequest.QUICKLINKS_PROFILE_FILTERS_PROPERTY, com.google.common.collect.Sets.newHashSet(org.apache.ambari.server.state.quicklinksprofile.QuickLinksProfileBuilderTest.filter(null, null, true)));
        java.util.Map<java.lang.String, java.lang.String> filter = org.apache.ambari.server.state.quicklinksprofile.QuickLinksProfileBuilderTest.filter(null, null, true);
        java.util.Map<java.lang.String, java.lang.Object> hdfs = org.apache.ambari.server.state.quicklinksprofile.QuickLinksProfileBuilderTest.service("HDFS", null, com.google.common.collect.Sets.newHashSet(filter));
        java.util.Set<java.util.Map<java.lang.String, java.lang.Object>> services = com.google.common.collect.Sets.newHashSet(hdfs);
        properties.put(org.apache.ambari.server.controller.internal.ProvisionClusterRequest.QUICKLINKS_PROFILE_SERVICES_PROPERTY, services);
        org.apache.ambari.server.controller.internal.ProvisionClusterRequest request = new org.apache.ambari.server.controller.internal.ProvisionClusterRequest(properties, null);
        java.lang.System.out.println(request.getQuickLinksProfileJson());
        org.junit.Assert.assertEquals("Quick links profile doesn't match expected", "{\"filters\":[{\"visible\":true}],\"services\":[{\"name\":\"HDFS\",\"filters\":[{\"visible\":true}]}]}", request.getQuickLinksProfileJson());
    }

    @org.junit.Test(expected = org.apache.ambari.server.topology.InvalidTopologyTemplateException.class)
    public void testQuickLinksProfile_InvalidRequestData() throws java.lang.Exception {
        java.util.Map<java.lang.String, java.lang.Object> properties = org.apache.ambari.server.controller.internal.ProvisionClusterRequestTest.createBlueprintRequestProperties(org.apache.ambari.server.controller.internal.ProvisionClusterRequestTest.CLUSTER_NAME, org.apache.ambari.server.controller.internal.ProvisionClusterRequestTest.BLUEPRINT_NAME);
        properties.put(org.apache.ambari.server.controller.internal.ProvisionClusterRequest.QUICKLINKS_PROFILE_SERVICES_PROPERTY, "Hello World!");
        org.apache.ambari.server.controller.internal.ProvisionClusterRequest request = new org.apache.ambari.server.controller.internal.ProvisionClusterRequest(properties, null);
    }

    public static java.util.Map<java.lang.String, java.lang.Object> createBlueprintRequestProperties(java.lang.String clusterName, java.lang.String blueprintName) {
        java.util.Map<java.lang.String, java.lang.Object> properties = new java.util.LinkedHashMap<>();
        properties.put(org.apache.ambari.server.controller.internal.ClusterResourceProvider.CLUSTER_NAME_PROPERTY_ID, clusterName);
        properties.put(org.apache.ambari.server.controller.internal.ClusterResourceProvider.BLUEPRINT, blueprintName);
        java.util.Collection<java.util.Map<java.lang.String, java.lang.Object>> hostGroups = new java.util.ArrayList<>();
        properties.put("host_groups", hostGroups);
        java.util.Map<java.lang.String, java.lang.Object> hostGroup1Properties = new java.util.HashMap<>();
        hostGroups.add(hostGroup1Properties);
        hostGroup1Properties.put("name", "group1");
        java.util.Collection<java.util.Map<java.lang.String, java.lang.String>> hostGroup1Hosts = new java.util.ArrayList<>();
        hostGroup1Properties.put("hosts", hostGroup1Hosts);
        java.util.Map<java.lang.String, java.lang.String> hostGroup1HostProperties = new java.util.HashMap<>();
        hostGroup1HostProperties.put("fqdn", "host1.myDomain.com");
        hostGroup1Hosts.add(hostGroup1HostProperties);
        java.util.Collection<java.util.Map<java.lang.String, java.lang.String>> hostGroup1Configurations = new java.util.ArrayList<>();
        hostGroup1Properties.put("configurations", hostGroup1Configurations);
        java.util.Map<java.lang.String, java.lang.String> hostGroup1Configuration1 = new java.util.HashMap<>();
        hostGroup1Configuration1.put("foo-type/hostGroup1Prop1", "prop1Value");
        hostGroup1Configuration1.put("foo-type/hostGroup1Prop2", "prop2Value");
        hostGroup1Configurations.add(hostGroup1Configuration1);
        java.util.Map<java.lang.String, java.lang.Object> hostGroup2Properties = new java.util.HashMap<>();
        hostGroups.add(hostGroup2Properties);
        hostGroup2Properties.put("name", "group2");
        hostGroup2Properties.put("host_count", "5");
        hostGroup2Properties.put("host_predicate", "Hosts/host_name=myTestHost");
        java.util.Collection<java.util.Map<java.lang.String, java.lang.String>> hostGroup2Configurations = new java.util.ArrayList<>();
        hostGroup2Properties.put("configurations", hostGroup2Configurations);
        java.util.Map<java.lang.String, java.lang.String> hostGroup2Configuration1 = new java.util.HashMap<>();
        hostGroup2Configuration1.put("foo-type/properties/hostGroup2Prop1", "prop1Value");
        hostGroup2Configuration1.put("foo-type/properties_attributes/attribute1/hostGroup2Prop10", "attribute1Prop10-value");
        hostGroup2Configurations.add(hostGroup2Configuration1);
        java.util.Collection<java.util.Map<java.lang.String, java.lang.String>> clusterConfigurations = new java.util.ArrayList<>();
        properties.put("configurations", clusterConfigurations);
        java.util.Map<java.lang.String, java.lang.String> clusterConfigurationProperties = new java.util.HashMap<>();
        clusterConfigurations.add(clusterConfigurationProperties);
        clusterConfigurationProperties.put("someType/properties/property1", "someValue");
        clusterConfigurationProperties.put("someType/properties_attributes/attribute1/property1", "someAttributePropValue");
        return properties;
    }

    public static java.util.Map<java.lang.String, java.lang.Object> createBlueprintRequestPropertiesNameOnly(java.lang.String clusterName, java.lang.String blueprintName) {
        java.util.Map<java.lang.String, java.lang.Object> properties = new java.util.LinkedHashMap<>();
        properties.put(org.apache.ambari.server.controller.internal.ClusterResourceProvider.CLUSTER_NAME_PROPERTY_ID, clusterName);
        properties.put(org.apache.ambari.server.controller.internal.ClusterResourceProvider.BLUEPRINT, blueprintName);
        java.util.Collection<java.util.Map<java.lang.String, java.lang.Object>> hostGroups = new java.util.ArrayList<>();
        properties.put("host_groups", hostGroups);
        java.util.Map<java.lang.String, java.lang.Object> hostGroup1Properties = new java.util.HashMap<>();
        hostGroups.add(hostGroup1Properties);
        hostGroup1Properties.put("name", "group1");
        java.util.Collection<java.util.Map<java.lang.String, java.lang.String>> hostGroup1Hosts = new java.util.ArrayList<>();
        hostGroup1Properties.put("hosts", hostGroup1Hosts);
        java.util.Map<java.lang.String, java.lang.String> hostGroup1HostProperties = new java.util.HashMap<>();
        hostGroup1HostProperties.put("fqdn", "host1.myDomain.com");
        hostGroup1Hosts.add(hostGroup1HostProperties);
        java.util.Collection<java.util.Map<java.lang.String, java.lang.String>> hostGroup1Configurations = new java.util.ArrayList<>();
        hostGroup1Properties.put("configurations", hostGroup1Configurations);
        java.util.Map<java.lang.String, java.lang.String> hostGroup1Configuration1 = new java.util.HashMap<>();
        hostGroup1Configuration1.put("foo-type/hostGroup1Prop1", "prop1Value");
        hostGroup1Configuration1.put("foo-type/hostGroup1Prop2", "prop2Value");
        hostGroup1Configurations.add(hostGroup1Configuration1);
        java.util.Collection<java.util.Map<java.lang.String, java.lang.String>> clusterConfigurations = new java.util.ArrayList<>();
        properties.put("configurations", clusterConfigurations);
        java.util.Map<java.lang.String, java.lang.String> clusterConfigurationProperties = new java.util.HashMap<>();
        clusterConfigurations.add(clusterConfigurationProperties);
        clusterConfigurationProperties.put("someType/properties/property1", "someValue");
        clusterConfigurationProperties.put("someType/properties_attributes/attribute1/property1", "someAttributePropValue");
        return properties;
    }

    public static java.util.Map<java.lang.String, java.lang.Object> createBlueprintRequestPropertiesCountOnly(java.lang.String clusterName, java.lang.String blueprintName) {
        java.util.Map<java.lang.String, java.lang.Object> properties = new java.util.LinkedHashMap<>();
        properties.put(org.apache.ambari.server.controller.internal.ClusterResourceProvider.CLUSTER_NAME_PROPERTY_ID, clusterName);
        properties.put(org.apache.ambari.server.controller.internal.ClusterResourceProvider.BLUEPRINT, blueprintName);
        java.util.Collection<java.util.Map<java.lang.String, java.lang.Object>> hostGroups = new java.util.ArrayList<>();
        properties.put("host_groups", hostGroups);
        java.util.Map<java.lang.String, java.lang.Object> hostGroup2Properties = new java.util.HashMap<>();
        hostGroups.add(hostGroup2Properties);
        hostGroup2Properties.put("name", "group2");
        hostGroup2Properties.put("host_count", "5");
        java.util.Collection<java.util.Map<java.lang.String, java.lang.String>> hostGroup2Configurations = new java.util.ArrayList<>();
        hostGroup2Properties.put("configurations", hostGroup2Configurations);
        java.util.Map<java.lang.String, java.lang.String> hostGroup2Configuration1 = new java.util.HashMap<>();
        hostGroup2Configuration1.put("foo-type/properties/hostGroup2Prop1", "prop1Value");
        hostGroup2Configuration1.put("foo-type/properties_attributes/attribute1/hostGroup2Prop10", "attribute1Prop10-value");
        hostGroup2Configurations.add(hostGroup2Configuration1);
        java.util.Collection<java.util.Map<java.lang.String, java.lang.String>> clusterConfigurations = new java.util.ArrayList<>();
        properties.put("configurations", clusterConfigurations);
        java.util.Map<java.lang.String, java.lang.String> clusterConfigurationProperties = new java.util.HashMap<>();
        clusterConfigurations.add(clusterConfigurationProperties);
        clusterConfigurationProperties.put("someType/properties/property1", "someValue");
        clusterConfigurationProperties.put("someType/properties_attributes/attribute1/property1", "someAttributePropValue");
        return properties;
    }
}