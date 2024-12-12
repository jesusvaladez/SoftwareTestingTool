package org.apache.ambari.server.topology.validators;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.easymock.EasyMock;
import org.easymock.EasyMockRule;
import org.easymock.EasyMockSupport;
import org.easymock.Mock;
import org.easymock.TestSubject;
public class ClusterConfigTypeValidatorTest extends org.easymock.EasyMockSupport {
    @org.junit.Rule
    public org.easymock.EasyMockRule mocks = new org.easymock.EasyMockRule(this);

    @org.easymock.Mock
    private org.apache.ambari.server.topology.Configuration clusterConfigurationMock;

    @org.easymock.Mock
    private org.apache.ambari.server.topology.Blueprint blueprintMock;

    @org.easymock.Mock
    private org.apache.ambari.server.controller.internal.Stack stackMock;

    @org.easymock.Mock
    private org.apache.ambari.server.topology.ClusterTopology clusterTopologyMock;

    private java.util.Set<java.lang.String> clusterRequestConfigTypes;

    @org.easymock.TestSubject
    private org.apache.ambari.server.topology.validators.ClusterConfigTypeValidator clusterConfigTypeValidator = new org.apache.ambari.server.topology.validators.ClusterConfigTypeValidator();

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

    @org.junit.Test
    public void testShouldValidationPassWhenNoConfigTypesSpecifiedInCCTemplate() throws java.lang.Exception {
        clusterRequestConfigTypes = java.util.Collections.emptySet();
        org.easymock.EasyMock.expect(clusterConfigurationMock.getAllConfigTypes()).andReturn(clusterRequestConfigTypes).anyTimes();
        replayAll();
        clusterConfigTypeValidator.validate(clusterTopologyMock);
    }

    @org.junit.Test
    public void testShouldValidationPassWhenAllConfigTypesAreValid() throws java.lang.Exception {
        clusterRequestConfigTypes = new java.util.HashSet<>(java.util.Arrays.asList("core-site", "yarn-site"));
        org.easymock.EasyMock.expect(clusterConfigurationMock.getAllConfigTypes()).andReturn(clusterRequestConfigTypes).anyTimes();
        org.easymock.EasyMock.expect(blueprintMock.getServices()).andReturn(new java.util.HashSet<>(java.util.Arrays.asList("YARN", "HDFS")));
        org.easymock.EasyMock.expect(stackMock.getConfigurationTypes("HDFS")).andReturn(java.util.Collections.singletonList("core-site"));
        org.easymock.EasyMock.expect(stackMock.getConfigurationTypes("YARN")).andReturn(java.util.Collections.singletonList("yarn-site"));
        replayAll();
        clusterConfigTypeValidator.validate(clusterTopologyMock);
    }

    @org.junit.Test(expected = org.apache.ambari.server.topology.InvalidTopologyException.class)
    public void testShouldValidationFailWhenInvalidConfigGroupsSpecifiedInCCTemplate() throws java.lang.Exception {
        clusterRequestConfigTypes = new java.util.HashSet<>(java.util.Collections.singletonList("oozie-site"));
        org.easymock.EasyMock.expect(clusterConfigurationMock.getAllConfigTypes()).andReturn(clusterRequestConfigTypes).anyTimes();
        org.easymock.EasyMock.expect(blueprintMock.getServices()).andReturn(new java.util.HashSet<>(java.util.Arrays.asList("YARN", "HDFS")));
        org.easymock.EasyMock.expect(stackMock.getConfigurationTypes("HDFS")).andReturn(java.util.Collections.singletonList("core-site"));
        org.easymock.EasyMock.expect(stackMock.getConfigurationTypes("YARN")).andReturn(java.util.Collections.singletonList("yarn-site"));
        replayAll();
        clusterConfigTypeValidator.validate(clusterTopologyMock);
    }

    @org.junit.Test(expected = org.apache.ambari.server.topology.InvalidTopologyException.class)
    public void testShouldValidationFailWhenThereIsAnInvalidConfigGroupProvided() throws java.lang.Exception {
        clusterRequestConfigTypes = new java.util.HashSet<>(java.util.Arrays.asList("core-site", "yarn-site", "oozie-site"));
        org.easymock.EasyMock.expect(clusterConfigurationMock.getAllConfigTypes()).andReturn(clusterRequestConfigTypes).anyTimes();
        org.easymock.EasyMock.expect(blueprintMock.getServices()).andReturn(new java.util.HashSet<>(java.util.Arrays.asList("YARN", "HDFS")));
        org.easymock.EasyMock.expect(stackMock.getConfigurationTypes("HDFS")).andReturn(java.util.Collections.singletonList("core-site"));
        org.easymock.EasyMock.expect(stackMock.getConfigurationTypes("YARN")).andReturn(java.util.Collections.singletonList("yarn-site"));
        replayAll();
        clusterConfigTypeValidator.validate(clusterTopologyMock);
    }

    @org.junit.Test
    public void testEquals() throws java.lang.Exception {
        nl.jqno.equalsverifier.EqualsVerifier.forClass(org.apache.ambari.server.topology.validators.ClusterConfigTypeValidator.class).usingGetClass().verify();
    }
}