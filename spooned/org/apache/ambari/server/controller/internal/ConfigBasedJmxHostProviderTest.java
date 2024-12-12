package org.apache.ambari.server.controller.internal;
import org.easymock.EasyMockRule;
import org.easymock.EasyMockSupport;
import org.easymock.Mock;
import static org.easymock.EasyMock.expect;
public class ConfigBasedJmxHostProviderTest extends org.easymock.EasyMockSupport {
    private static final java.lang.String COMPONENT_WITH_OVERRIDDEN_HOST = "component1";

    private static final java.lang.String CLUSTER_1 = "cluster1";

    private static final java.lang.String COMPONENT_WITHOUT_OVERRIDDEN_HOST = "componentWithoutOverriddenHost";

    private static final java.lang.String RESOLVED_HOST = "resolved.fqdn";

    private static final java.util.Set<java.lang.String> resolvedUris = new java.util.HashSet<>(java.util.Arrays.asList(org.apache.ambari.server.controller.internal.ConfigBasedJmxHostProviderTest.RESOLVED_HOST));

    @org.junit.Rule
    public org.easymock.EasyMockRule mocks = new org.easymock.EasyMockRule(this);

    @org.easymock.Mock
    private org.apache.ambari.server.controller.jmx.JMXHostProvider defaultHostProvider;

    @org.easymock.Mock
    private org.apache.ambari.server.state.ConfigHelper configHelper;

    private org.apache.ambari.server.controller.jmx.JMXHostProvider hostProvider;

    @org.junit.Before
    public void setUp() throws java.lang.Exception {
        hostProvider = new org.apache.ambari.server.controller.internal.ConfigBasedJmxHostProvider(overrideHosts(), defaultHostProvider, configHelper);
    }

    @org.junit.Test
    public void testDelegatesWhenHostIsNotOverridden() throws java.lang.Exception {
        EasyMock.expect(defaultHostProvider.getHostNames(org.apache.ambari.server.controller.internal.ConfigBasedJmxHostProviderTest.CLUSTER_1, org.apache.ambari.server.controller.internal.ConfigBasedJmxHostProviderTest.COMPONENT_WITHOUT_OVERRIDDEN_HOST)).andReturn(org.apache.ambari.server.controller.internal.ConfigBasedJmxHostProviderTest.resolvedUris).anyTimes();
        replayAll();
        org.junit.Assert.assertThat(hostProvider.getHostNames(org.apache.ambari.server.controller.internal.ConfigBasedJmxHostProviderTest.CLUSTER_1, org.apache.ambari.server.controller.internal.ConfigBasedJmxHostProviderTest.COMPONENT_WITHOUT_OVERRIDDEN_HOST), org.hamcrest.core.Is.is(org.apache.ambari.server.controller.internal.ConfigBasedJmxHostProviderTest.resolvedUris));
        verifyAll();
    }

    @org.junit.Test
    public void testGetsUriFromConfigWhenHostIsOverridden() throws java.lang.Exception {
        EasyMock.expect(configHelper.getEffectiveConfigProperties(org.apache.ambari.server.controller.internal.ConfigBasedJmxHostProviderTest.CLUSTER_1, null)).andReturn(config()).anyTimes();
        replayAll();
        org.junit.Assert.assertThat(hostProvider.getHostNames(org.apache.ambari.server.controller.internal.ConfigBasedJmxHostProviderTest.CLUSTER_1, org.apache.ambari.server.controller.internal.ConfigBasedJmxHostProviderTest.COMPONENT_WITH_OVERRIDDEN_HOST), org.hamcrest.core.Is.is(org.apache.ambari.server.controller.internal.ConfigBasedJmxHostProviderTest.resolvedUris));
        verifyAll();
    }

    private java.util.Map<java.lang.String, org.apache.ambari.server.state.UriInfo> overrideHosts() {
        org.apache.ambari.server.state.UriInfo uri = new org.apache.ambari.server.state.UriInfo();
        uri.setHttpUri("${hdfs-site/dfs.namenode.http-address}");
        return new java.util.HashMap<java.lang.String, org.apache.ambari.server.state.UriInfo>() {
            {
                put(org.apache.ambari.server.controller.internal.ConfigBasedJmxHostProviderTest.COMPONENT_WITH_OVERRIDDEN_HOST, uri);
            }
        };
    }

    private java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> config() {
        return new java.util.HashMap<java.lang.String, java.util.Map<java.lang.String, java.lang.String>>() {
            {
                put("hdfs-site", new java.util.HashMap<java.lang.String, java.lang.String>() {
                    {
                        put("dfs.namenode.http-address", org.apache.ambari.server.controller.internal.ConfigBasedJmxHostProviderTest.RESOLVED_HOST);
                    }
                });
            }
        };
    }
}