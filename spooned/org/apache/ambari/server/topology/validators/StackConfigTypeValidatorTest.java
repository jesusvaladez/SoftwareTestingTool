package org.apache.ambari.server.topology.validators;
import org.easymock.EasyMock;
import org.easymock.EasyMockRule;
import org.easymock.EasyMockSupport;
import org.easymock.Mock;
import org.easymock.TestSubject;
public class StackConfigTypeValidatorTest extends org.easymock.EasyMockSupport {
    @org.junit.Rule
    public org.easymock.EasyMockRule mocks = new org.easymock.EasyMockRule(this);

    @org.easymock.Mock
    private org.apache.ambari.server.topology.Configuration clusterConfigurationMock;

    @org.easymock.Mock
    private org.apache.ambari.server.topology.Configuration stackConfigurationMock;

    @org.easymock.Mock
    private org.apache.ambari.server.topology.Blueprint blueprintMock;

    @org.easymock.Mock
    private org.apache.ambari.server.controller.internal.Stack stackMock;

    @org.easymock.Mock
    private org.apache.ambari.server.topology.ClusterTopology clusterTopologyMock;

    private java.util.Set<java.lang.String> clusterRequestConfigTypes;

    @org.easymock.TestSubject
    private org.apache.ambari.server.topology.validators.StackConfigTypeValidator stackConfigTypeValidator = new org.apache.ambari.server.topology.validators.StackConfigTypeValidator();

    @org.junit.Before
    public void before() {
        org.easymock.EasyMock.expect(clusterTopologyMock.getConfiguration()).andReturn(clusterConfigurationMock).anyTimes();
        org.easymock.EasyMock.expect(clusterTopologyMock.getBlueprint()).andReturn(blueprintMock).anyTimes();
        org.easymock.EasyMock.expect(blueprintMock.getStack()).andReturn(stackMock).anyTimes();
    }

    @org.junit.After
    public void after() {
        resetAll();
    }

    @org.junit.Test(expected = org.apache.ambari.server.topology.InvalidTopologyException.class)
    public void testShouldValidationFailWhenUnknownConfigTypeComesIn() throws java.lang.Exception {
        org.easymock.EasyMock.expect(stackMock.getConfiguration()).andReturn(stackConfigurationMock);
        org.easymock.EasyMock.expect(stackConfigurationMock.getAllConfigTypes()).andReturn(new java.util.HashSet<>(java.util.Arrays.asList("core-site", "yarn-site")));
        org.easymock.EasyMock.expect(clusterConfigurationMock.getAllConfigTypes()).andReturn(new java.util.HashSet<>(java.util.Arrays.asList("invalid-site")));
        replayAll();
        stackConfigTypeValidator.validate(clusterTopologyMock);
    }

    @org.junit.Test
    public void testShouldValidationPassifNoConfigTypesomeIn() throws java.lang.Exception {
        org.easymock.EasyMock.expect(stackMock.getConfiguration()).andReturn(stackConfigurationMock);
        org.easymock.EasyMock.expect(stackConfigurationMock.getAllConfigTypes()).andReturn(new java.util.HashSet<>(java.util.Arrays.asList("core-site", "yarn-site")));
        org.easymock.EasyMock.expect(clusterConfigurationMock.getAllConfigTypes()).andReturn(new java.util.HashSet<>(java.util.Collections.emptyList()));
        replayAll();
        stackConfigTypeValidator.validate(clusterTopologyMock);
    }

    @org.junit.Test(expected = org.apache.ambari.server.topology.InvalidTopologyException.class)
    public void testShouldValidationFailIfMultipleInvalidConfigTypesComeIn() throws java.lang.Exception {
        org.easymock.EasyMock.expect(stackMock.getConfiguration()).andReturn(stackConfigurationMock);
        org.easymock.EasyMock.expect(stackConfigurationMock.getAllConfigTypes()).andReturn(new java.util.HashSet<>(java.util.Arrays.asList("core-site", "yarn-site")));
        org.easymock.EasyMock.expect(clusterConfigurationMock.getAllConfigTypes()).andReturn(new java.util.HashSet<>(java.util.Arrays.asList("invalid-site-1", "invalid-default")));
        replayAll();
        stackConfigTypeValidator.validate(clusterTopologyMock);
    }
}