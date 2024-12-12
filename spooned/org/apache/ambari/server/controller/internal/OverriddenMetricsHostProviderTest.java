package org.apache.ambari.server.controller.internal;
import org.easymock.EasyMockRule;
import org.easymock.EasyMockSupport;
import org.easymock.Mock;
import static org.easymock.EasyMock.expect;
public class OverriddenMetricsHostProviderTest extends org.easymock.EasyMockSupport {
    private static final java.lang.String COMPONENT_WITH_OVERRIDDEN_HOST = "component1";

    private static final java.lang.String CLUSTER_1 = "cluster1";

    private static final java.lang.String COMPONENT_WITHOUT_OVERRIDDEN_HOST = "componentWithoutOverriddenHost";

    private static final java.lang.String OVERRIDEN_HOST = "overridenHost1";

    private static final java.lang.String COMPONENT_WITH_OVERRIDDEN_HOST_PLACEHOLDER = "${hdfs-site/dfs.namenode.http-address}";

    private static final java.lang.String RESOLVED_HOST = "resolved.fqdn";

    @org.junit.Rule
    public org.easymock.EasyMockRule mocks = new org.easymock.EasyMockRule(this);

    @org.easymock.Mock
    private org.apache.ambari.server.controller.metrics.MetricHostProvider defaultHostProvider;

    @org.easymock.Mock
    private org.apache.ambari.server.state.ConfigHelper configHelper;

    private org.apache.ambari.server.controller.metrics.MetricHostProvider hostProvider;

    @org.junit.Before
    public void setUp() throws java.lang.Exception {
        hostProvider = new org.apache.ambari.server.controller.internal.OverriddenMetricsHostProvider(overrideHosts(), defaultHostProvider, configHelper);
    }

    @org.junit.Test
    public void testReturnsDefaultWhenNotOverridden() throws java.lang.Exception {
        replayAll();
        org.junit.Assert.assertThat(hostProvider.getExternalHostName(org.apache.ambari.server.controller.internal.OverriddenMetricsHostProviderTest.CLUSTER_1, org.apache.ambari.server.controller.internal.OverriddenMetricsHostProviderTest.COMPONENT_WITHOUT_OVERRIDDEN_HOST), org.hamcrest.core.Is.is(java.util.Optional.empty()));
        verifyAll();
    }

    @org.junit.Test
    public void testReturnOverriddenHostIfPresent() throws java.lang.Exception {
        EasyMock.expect(configHelper.getEffectiveConfigProperties(org.apache.ambari.server.controller.internal.OverriddenMetricsHostProviderTest.CLUSTER_1, null)).andReturn(java.util.Collections.emptyMap()).anyTimes();
        replayAll();
        org.junit.Assert.assertThat(hostProvider.getExternalHostName(org.apache.ambari.server.controller.internal.OverriddenMetricsHostProviderTest.CLUSTER_1, org.apache.ambari.server.controller.internal.OverriddenMetricsHostProviderTest.COMPONENT_WITH_OVERRIDDEN_HOST), org.hamcrest.core.Is.is(java.util.Optional.of(org.apache.ambari.server.controller.internal.OverriddenMetricsHostProviderTest.OVERRIDEN_HOST)));
        verifyAll();
    }

    @org.junit.Test
    public void testReplacesPlaceholderInOverriddenHost() throws java.lang.Exception {
        EasyMock.expect(configHelper.getEffectiveConfigProperties(org.apache.ambari.server.controller.internal.OverriddenMetricsHostProviderTest.CLUSTER_1, null)).andReturn(config()).anyTimes();
        replayAll();
        org.junit.Assert.assertThat(hostProvider.getExternalHostName(org.apache.ambari.server.controller.internal.OverriddenMetricsHostProviderTest.CLUSTER_1, org.apache.ambari.server.controller.internal.OverriddenMetricsHostProviderTest.COMPONENT_WITH_OVERRIDDEN_HOST_PLACEHOLDER), org.hamcrest.core.Is.is(java.util.Optional.of(org.apache.ambari.server.controller.internal.OverriddenMetricsHostProviderTest.RESOLVED_HOST)));
        verifyAll();
    }

    private java.util.Map<java.lang.String, java.lang.String> overrideHosts() {
        return new java.util.HashMap<java.lang.String, java.lang.String>() {
            {
                put(org.apache.ambari.server.controller.internal.OverriddenMetricsHostProviderTest.COMPONENT_WITH_OVERRIDDEN_HOST, org.apache.ambari.server.controller.internal.OverriddenMetricsHostProviderTest.OVERRIDEN_HOST);
                put(org.apache.ambari.server.controller.internal.OverriddenMetricsHostProviderTest.COMPONENT_WITH_OVERRIDDEN_HOST_PLACEHOLDER, "${hdfs-site/dfs.namenode.http-address}");
            }
        };
    }

    private java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> config() {
        return new java.util.HashMap<java.lang.String, java.util.Map<java.lang.String, java.lang.String>>() {
            {
                put("hdfs-site", new java.util.HashMap<java.lang.String, java.lang.String>() {
                    {
                        put("dfs.namenode.http-address", ("http://" + org.apache.ambari.server.controller.internal.OverriddenMetricsHostProviderTest.RESOLVED_HOST) + ":8080");
                    }
                });
            }
        };
    }
}