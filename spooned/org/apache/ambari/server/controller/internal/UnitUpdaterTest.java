package org.apache.ambari.server.controller.internal;
import org.easymock.EasyMockRule;
import org.easymock.EasyMockSupport;
import org.easymock.Mock;
import static org.easymock.EasyMock.expect;
public class UnitUpdaterTest extends org.easymock.EasyMockSupport {
    public static final java.lang.String HEAPSIZE = "oozie_heapsize";

    @org.junit.Rule
    public org.easymock.EasyMockRule mocks = new org.easymock.EasyMockRule(this);

    public static final java.lang.String OOZIE = "OOZIE";

    public static final java.lang.String OOZIE_ENV = "oozie-env";

    private java.util.Map<java.lang.String, org.apache.ambari.server.controller.internal.Stack.ConfigProperty> stackConfigWithMetadata = new java.util.HashMap<>();

    private org.apache.ambari.server.controller.internal.UnitUpdater unitUpdater;

    @org.easymock.Mock
    private org.apache.ambari.server.topology.ClusterTopology clusterTopology;

    @org.easymock.Mock
    private org.apache.ambari.server.topology.Blueprint blueprint;

    @org.easymock.Mock
    private org.apache.ambari.server.controller.internal.Stack stack;

    @org.junit.Test
    public void testStackUnitIsAppendedWhereUnitIsNotDefined() throws java.lang.Exception {
        stackUnitIs(org.apache.ambari.server.controller.internal.UnitUpdaterTest.HEAPSIZE, "GB");
        org.junit.Assert.assertEquals("1g", updateUnit(org.apache.ambari.server.controller.internal.UnitUpdaterTest.OOZIE, org.apache.ambari.server.controller.internal.UnitUpdaterTest.OOZIE_ENV, org.apache.ambari.server.controller.internal.UnitUpdaterTest.HEAPSIZE, "1"));
    }

    @org.junit.Test
    public void testDefaultMbStackUnitIsAppendedWhereUnitIsNotDefined() throws java.lang.Exception {
        org.junit.Assert.assertEquals("4096m", updateUnit(org.apache.ambari.server.controller.internal.UnitUpdaterTest.OOZIE, org.apache.ambari.server.controller.internal.UnitUpdaterTest.OOZIE_ENV, org.apache.ambari.server.controller.internal.UnitUpdaterTest.HEAPSIZE, "4096"));
    }

    @org.junit.Test
    public void testNoUnitIsAppendedWhenPropertyAlreadyHasTheStackUnit() throws java.lang.Exception {
        stackUnitIs(org.apache.ambari.server.controller.internal.UnitUpdaterTest.HEAPSIZE, "MB");
        org.junit.Assert.assertEquals("128m", updateUnit(org.apache.ambari.server.controller.internal.UnitUpdaterTest.OOZIE, org.apache.ambari.server.controller.internal.UnitUpdaterTest.OOZIE_ENV, org.apache.ambari.server.controller.internal.UnitUpdaterTest.HEAPSIZE, "128m"));
    }

    @org.junit.Test
    public void testNoUnitIsAppendedIfStackUnitIsInBytes() throws java.lang.Exception {
        stackUnitIs(org.apache.ambari.server.controller.internal.UnitUpdaterTest.HEAPSIZE, "Bytes");
        org.junit.Assert.assertEquals("128", updateUnit(org.apache.ambari.server.controller.internal.UnitUpdaterTest.OOZIE, org.apache.ambari.server.controller.internal.UnitUpdaterTest.OOZIE_ENV, org.apache.ambari.server.controller.internal.UnitUpdaterTest.HEAPSIZE, "128"));
    }

    @org.junit.Test
    public void testUnitSuffixIsCaseInsenitiveAndWhiteSpaceTolerant() throws java.lang.Exception {
        stackUnitIs(org.apache.ambari.server.controller.internal.UnitUpdaterTest.HEAPSIZE, "GB");
        org.junit.Assert.assertEquals("1g", updateUnit(org.apache.ambari.server.controller.internal.UnitUpdaterTest.OOZIE, org.apache.ambari.server.controller.internal.UnitUpdaterTest.OOZIE_ENV, org.apache.ambari.server.controller.internal.UnitUpdaterTest.HEAPSIZE, " 1G "));
    }

    @org.junit.Test(expected = java.lang.IllegalArgumentException.class)
    public void testRejectValuesWhereStackUnitDoesNotMatchToGiveUnit() throws java.lang.Exception {
        stackUnitIs(org.apache.ambari.server.controller.internal.UnitUpdaterTest.HEAPSIZE, "MB");
        updateUnit(org.apache.ambari.server.controller.internal.UnitUpdaterTest.OOZIE, org.apache.ambari.server.controller.internal.UnitUpdaterTest.OOZIE_ENV, org.apache.ambari.server.controller.internal.UnitUpdaterTest.HEAPSIZE, "2g");
    }

    @org.junit.Test(expected = java.lang.IllegalArgumentException.class)
    public void testRejectEmptyPropertyValue() throws java.lang.Exception {
        updateUnit(org.apache.ambari.server.controller.internal.UnitUpdaterTest.OOZIE, org.apache.ambari.server.controller.internal.UnitUpdaterTest.OOZIE_ENV, org.apache.ambari.server.controller.internal.UnitUpdaterTest.HEAPSIZE, "");
    }

    @org.junit.Test
    public void updateUnits() {
        stackUnitIs(org.apache.ambari.server.controller.internal.UnitUpdaterTest.HEAPSIZE, "MB");
        setUpStack(org.apache.ambari.server.controller.internal.UnitUpdaterTest.OOZIE, org.apache.ambari.server.controller.internal.UnitUpdaterTest.OOZIE_ENV);
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> properties = org.apache.ambari.server.testutils.TestCollectionUtils.map(org.apache.ambari.server.controller.internal.UnitUpdaterTest.OOZIE_ENV, org.apache.ambari.server.testutils.TestCollectionUtils.map(org.apache.ambari.server.controller.internal.UnitUpdaterTest.HEAPSIZE, "1024"), "core-site", org.apache.ambari.server.testutils.TestCollectionUtils.map("fs.trash.interval", "360"));
        org.apache.ambari.server.topology.Configuration configuration = new org.apache.ambari.server.topology.Configuration(properties, new java.util.HashMap<>());
        org.apache.ambari.server.controller.internal.UnitUpdater.updateUnits(configuration, stack);
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> expected = org.apache.ambari.server.testutils.TestCollectionUtils.map(org.apache.ambari.server.controller.internal.UnitUpdaterTest.OOZIE_ENV, org.apache.ambari.server.testutils.TestCollectionUtils.map(org.apache.ambari.server.controller.internal.UnitUpdaterTest.HEAPSIZE, "1024m"), "core-site", org.apache.ambari.server.testutils.TestCollectionUtils.map("fs.trash.interval", "360"));
        org.junit.Assert.assertEquals(expected, configuration.getProperties());
    }

    @org.junit.Test
    public void removeUnits() {
        stackUnitIs(org.apache.ambari.server.controller.internal.UnitUpdaterTest.HEAPSIZE, "MB");
        setUpStack(org.apache.ambari.server.controller.internal.UnitUpdaterTest.OOZIE, org.apache.ambari.server.controller.internal.UnitUpdaterTest.OOZIE_ENV);
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> properties = org.apache.ambari.server.testutils.TestCollectionUtils.map(org.apache.ambari.server.controller.internal.UnitUpdaterTest.OOZIE_ENV, org.apache.ambari.server.testutils.TestCollectionUtils.map(org.apache.ambari.server.controller.internal.UnitUpdaterTest.HEAPSIZE, "1024m"), "core-site", org.apache.ambari.server.testutils.TestCollectionUtils.map("fs.trash.interval", "360"));
        org.apache.ambari.server.topology.Configuration configuration = new org.apache.ambari.server.topology.Configuration(properties, new java.util.HashMap<>());
        org.apache.ambari.server.controller.internal.UnitUpdater.removeUnits(configuration, stack);
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> expected = org.apache.ambari.server.testutils.TestCollectionUtils.map(org.apache.ambari.server.controller.internal.UnitUpdaterTest.OOZIE_ENV, org.apache.ambari.server.testutils.TestCollectionUtils.map(org.apache.ambari.server.controller.internal.UnitUpdaterTest.HEAPSIZE, "1024"), "core-site", org.apache.ambari.server.testutils.TestCollectionUtils.map("fs.trash.interval", "360"));
        org.junit.Assert.assertEquals(expected, configuration.getProperties());
    }

    private void stackUnitIs(java.lang.String name, java.lang.String unit) {
        org.apache.ambari.server.state.ValueAttributesInfo propertyValueAttributes = new org.apache.ambari.server.state.ValueAttributesInfo();
        propertyValueAttributes.setUnit(unit);
        stackConfigWithMetadata.put(name, org.apache.ambari.server.controller.internal.UnitUpdaterTest.configProperty(name, unit));
    }

    public static org.apache.ambari.server.controller.internal.Stack.ConfigProperty configProperty(java.lang.String name, java.lang.String unit) {
        org.apache.ambari.server.state.ValueAttributesInfo propertyValueAttributes = new org.apache.ambari.server.state.ValueAttributesInfo();
        propertyValueAttributes.setUnit(unit);
        return new org.apache.ambari.server.controller.internal.Stack.ConfigProperty(new org.apache.ambari.server.controller.StackConfigurationResponse(name, "any", "any", "any", "any", true, java.util.Collections.emptySet(), java.util.Collections.emptyMap(), propertyValueAttributes, java.util.Collections.emptySet()));
    }

    private void setUpStack(java.lang.String serviceName, java.lang.String configType) {
        EasyMock.expect(clusterTopology.getBlueprint()).andReturn(blueprint).anyTimes();
        EasyMock.expect(blueprint.getStack()).andReturn(stack).anyTimes();
        EasyMock.expect(stack.getConfigurationPropertiesWithMetadata(serviceName, configType)).andReturn(stackConfigWithMetadata).anyTimes();
        replayAll();
    }

    private java.lang.String updateUnit(java.lang.String serviceName, java.lang.String configType, java.lang.String propName, java.lang.String propValue) {
        org.apache.ambari.server.controller.internal.UnitUpdater updater = new org.apache.ambari.server.controller.internal.UnitUpdater(serviceName, configType);
        setUpStack(serviceName, configType);
        return updater.updateForClusterCreate(propName, propValue, java.util.Collections.emptyMap(), clusterTopology);
    }
}