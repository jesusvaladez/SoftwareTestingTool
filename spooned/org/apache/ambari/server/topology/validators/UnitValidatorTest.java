package org.apache.ambari.server.topology.validators;
import org.easymock.EasyMockRule;
import org.easymock.EasyMockSupport;
import org.easymock.Mock;
import static org.easymock.EasyMock.expect;
public class UnitValidatorTest extends org.easymock.EasyMockSupport {
    private static final java.lang.String CONFIG_TYPE = "config-type";

    private static final java.lang.String SERVICE = "service";

    @org.junit.Rule
    public org.easymock.EasyMockRule mocks = new org.easymock.EasyMockRule(this);

    private java.util.Map<java.lang.String, org.apache.ambari.server.controller.internal.Stack.ConfigProperty> stackConfigWithMetadata = new java.util.HashMap<>();

    private org.apache.ambari.server.topology.validators.UnitValidator validator;

    @org.easymock.Mock
    private org.apache.ambari.server.topology.ClusterTopology clusterTopology;

    @org.easymock.Mock
    private org.apache.ambari.server.topology.Blueprint blueprint;

    @org.easymock.Mock
    private org.apache.ambari.server.controller.internal.Stack stack;

    @org.junit.Test(expected = java.lang.IllegalArgumentException.class)
    public void rejectsPropertyWithDifferentUnitThanStackUnit() throws java.lang.Exception {
        stackUnitIs("property1", "MB");
        propertyToBeValidatedIs("property1", "12G");
        validate("property1");
    }

    @org.junit.Test
    public void acceptsPropertyWithSameUnitThanStackUnit() throws java.lang.Exception {
        stackUnitIs("property1", "MB");
        propertyToBeValidatedIs("property1", "12m");
        validate("property1");
    }

    @org.junit.Test
    public void skipsValidatingIrrelevantProperty() throws java.lang.Exception {
        stackUnitIs("property1", "MB");
        propertyToBeValidatedIs("property1", "12g");
        validate("property2");
    }

    @org.junit.Before
    public void setUp() throws java.lang.Exception {
        EasyMock.expect(clusterTopology.getBlueprint()).andReturn(blueprint).anyTimes();
        EasyMock.expect(clusterTopology.getHostGroupInfo()).andReturn(java.util.Collections.emptyMap()).anyTimes();
        EasyMock.expect(blueprint.getStack()).andReturn(stack).anyTimes();
        EasyMock.expect(stack.getConfigurationPropertiesWithMetadata(org.apache.ambari.server.topology.validators.UnitValidatorTest.SERVICE, org.apache.ambari.server.topology.validators.UnitValidatorTest.CONFIG_TYPE)).andReturn(stackConfigWithMetadata).anyTimes();
    }

    private void propertyToBeValidatedIs(java.lang.String propertyName, java.lang.String propertyValue) throws org.apache.ambari.server.topology.InvalidTopologyException, org.apache.ambari.server.controller.internal.ConfigurationTopologyException {
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> propertiesToBeValidated = new java.util.HashMap<java.lang.String, java.util.Map<java.lang.String, java.lang.String>>() {
            {
                put(org.apache.ambari.server.topology.validators.UnitValidatorTest.CONFIG_TYPE, new java.util.HashMap<java.lang.String, java.lang.String>() {
                    {
                        put(propertyName, propertyValue);
                    }
                });
            }
        };
        EasyMock.expect(clusterTopology.getConfiguration()).andReturn(new org.apache.ambari.server.topology.Configuration(propertiesToBeValidated, java.util.Collections.emptyMap())).anyTimes();
        replayAll();
    }

    private void validate(java.lang.String propertyName) throws org.apache.ambari.server.topology.InvalidTopologyException {
        validator = new org.apache.ambari.server.topology.validators.UnitValidator(com.google.common.collect.Sets.newHashSet(new org.apache.ambari.server.topology.validators.UnitValidatedProperty(org.apache.ambari.server.topology.validators.UnitValidatorTest.SERVICE, org.apache.ambari.server.topology.validators.UnitValidatorTest.CONFIG_TYPE, propertyName)));
        validator.validate(clusterTopology);
    }

    private void stackUnitIs(java.lang.String name, java.lang.String unit) {
        org.apache.ambari.server.state.ValueAttributesInfo propertyValueAttributes = new org.apache.ambari.server.state.ValueAttributesInfo();
        propertyValueAttributes.setUnit(unit);
        stackConfigWithMetadata.put(name, new org.apache.ambari.server.controller.internal.Stack.ConfigProperty(new org.apache.ambari.server.controller.StackConfigurationResponse(name, "any", "any", "any", "any", true, java.util.Collections.emptySet(), java.util.Collections.emptyMap(), propertyValueAttributes, java.util.Collections.emptySet())));
    }
}