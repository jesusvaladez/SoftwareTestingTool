package org.apache.ambari.server.topology.validators;
import org.easymock.EasyMock;
import org.easymock.EasyMockRule;
import org.easymock.EasyMockSupport;
import org.easymock.Mock;
import org.easymock.TestSubject;
public class HiveServiceValidatorTest extends org.easymock.EasyMockSupport {
    @org.junit.Rule
    public org.easymock.EasyMockRule mocks = new org.easymock.EasyMockRule(this);

    @org.easymock.Mock
    private org.apache.ambari.server.topology.ClusterTopology clusterTopologyMock;

    @org.easymock.Mock
    private org.apache.ambari.server.topology.Blueprint blueprintMock;

    @org.easymock.Mock
    private org.apache.ambari.server.topology.Configuration configurationMock;

    @org.easymock.TestSubject
    private org.apache.ambari.server.topology.validators.HiveServiceValidator hiveServiceValidator = new org.apache.ambari.server.topology.validators.HiveServiceValidator();

    @org.junit.Before
    public void setUp() throws java.lang.Exception {
    }

    @org.junit.After
    public void tearDown() throws java.lang.Exception {
        resetAll();
    }

    @org.junit.Test
    public void testShouldValidationPassWhenHiveServiceIsNotInBlueprint() throws java.lang.Exception {
        org.easymock.EasyMock.expect(clusterTopologyMock.getBlueprint()).andReturn(blueprintMock);
        org.easymock.EasyMock.expect(blueprintMock.getServices()).andReturn(java.util.Collections.emptySet());
        replayAll();
        hiveServiceValidator.validate(clusterTopologyMock);
    }

    @org.junit.Test(expected = org.apache.ambari.server.topology.InvalidTopologyException.class)
    public void testShouldValidationFailWhenHiveServiceIsMissingConfigType() throws java.lang.Exception {
        java.util.Collection<java.lang.String> blueprintServices = java.util.Arrays.asList("HIVE", "OOZIE");
        org.easymock.EasyMock.expect(clusterTopologyMock.getBlueprint()).andReturn(blueprintMock);
        org.easymock.EasyMock.expect(blueprintMock.getServices()).andReturn(blueprintServices);
        org.easymock.EasyMock.expect(clusterTopologyMock.getConfiguration()).andReturn(configurationMock);
        org.easymock.EasyMock.expect(configurationMock.getAllConfigTypes()).andReturn(java.util.Collections.emptySet());
        replayAll();
        hiveServiceValidator.validate(clusterTopologyMock);
    }

    @org.junit.Test
    public void testShouldValidationPassWhenCustomHiveDatabaseSettingsProvided() throws java.lang.Exception {
        java.util.Collection<java.lang.String> blueprintServices = java.util.Arrays.asList("HIVE", "OOZIE");
        java.util.Collection<java.lang.String> configTypes = java.util.Arrays.asList("hive-env", "core-site", "hadoop-env");
        org.easymock.EasyMock.expect(clusterTopologyMock.getBlueprint()).andReturn(blueprintMock);
        org.easymock.EasyMock.expect(blueprintMock.getServices()).andReturn(blueprintServices);
        org.easymock.EasyMock.expect(clusterTopologyMock.getConfiguration()).andReturn(configurationMock);
        org.easymock.EasyMock.expect(configurationMock.getAllConfigTypes()).andReturn(configTypes);
        org.easymock.EasyMock.expect(configurationMock.getPropertyValue("hive-env", "hive_database")).andReturn("PSQL");
        replayAll();
        hiveServiceValidator.validate(clusterTopologyMock);
    }

    @org.junit.Test(expected = org.apache.ambari.server.topology.InvalidTopologyException.class)
    public void testShouldValidationFailWhenDefaultsAreUsedAndMysqlComponentIsMissing() throws java.lang.Exception {
        java.util.Collection<java.lang.String> blueprintServices = java.util.Arrays.asList("HIVE", "HDFS");
        java.util.Collection<java.lang.String> configTypes = java.util.Arrays.asList("hive-env", "core-site", "hadoop-env");
        org.easymock.EasyMock.expect(clusterTopologyMock.getBlueprint()).andReturn(blueprintMock).anyTimes();
        org.easymock.EasyMock.expect(blueprintMock.getServices()).andReturn(blueprintServices).anyTimes();
        org.easymock.EasyMock.expect(blueprintMock.getComponents("HIVE")).andReturn(java.util.Collections.emptyList()).anyTimes();
        org.easymock.EasyMock.expect(clusterTopologyMock.getConfiguration()).andReturn(configurationMock);
        org.easymock.EasyMock.expect(configurationMock.getAllConfigTypes()).andReturn(configTypes);
        org.easymock.EasyMock.expect(configurationMock.getPropertyValue("hive-env", "hive_database")).andReturn("New MySQL Database");
        replayAll();
        hiveServiceValidator.validate(clusterTopologyMock);
    }

    @org.junit.Test
    public void testShouldValidationPassWhenDefaultsAreUsedAndMsqlComponentIsListed() throws java.lang.Exception {
        java.util.Collection<java.lang.String> blueprintServices = java.util.Arrays.asList("HIVE", "HDFS", "MYSQL_SERVER");
        java.util.Collection<java.lang.String> hiveComponents = java.util.Arrays.asList("MYSQL_SERVER");
        java.util.Collection<java.lang.String> configTypes = java.util.Arrays.asList("hive-env", "core-site", "hadoop-env");
        org.easymock.EasyMock.expect(clusterTopologyMock.getBlueprint()).andReturn(blueprintMock).anyTimes();
        org.easymock.EasyMock.expect(blueprintMock.getServices()).andReturn(blueprintServices).anyTimes();
        org.easymock.EasyMock.expect(blueprintMock.getComponents("HIVE")).andReturn(hiveComponents).anyTimes();
        org.easymock.EasyMock.expect(clusterTopologyMock.getConfiguration()).andReturn(configurationMock);
        org.easymock.EasyMock.expect(configurationMock.getAllConfigTypes()).andReturn(configTypes);
        org.easymock.EasyMock.expect(configurationMock.getPropertyValue("hive-env", "hive_database")).andReturn("New MySQL Database");
        replayAll();
        hiveServiceValidator.validate(clusterTopologyMock);
    }
}