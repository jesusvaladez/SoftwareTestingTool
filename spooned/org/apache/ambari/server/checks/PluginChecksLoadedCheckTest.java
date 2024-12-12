package org.apache.ambari.server.checks;
import org.apache.commons.lang.StringUtils;
import org.easymock.EasyMockSupport;
import static org.easymock.EasyMock.expect;
public class PluginChecksLoadedCheckTest extends org.easymock.EasyMockSupport {
    private final org.apache.ambari.server.checks.PluginChecksLoadedCheck m_check = new org.apache.ambari.server.checks.PluginChecksLoadedCheck();

    private final org.apache.ambari.server.checks.UpgradeCheckRegistry m_upgradeCheckRegistry = createNiceMock(org.apache.ambari.server.checks.UpgradeCheckRegistry.class);

    @org.junit.Before
    public void before() throws java.lang.Exception {
        m_check.m_upgradeCheckRegistryProvider = new com.google.inject.Provider<org.apache.ambari.server.checks.UpgradeCheckRegistry>() {
            @java.lang.Override
            public org.apache.ambari.server.checks.UpgradeCheckRegistry get() {
                return m_upgradeCheckRegistry;
            }
        };
    }

    @org.junit.Test
    public void testPerform() throws java.lang.Exception {
        java.util.Set<java.lang.String> failedClasses = com.google.common.collect.Sets.newHashSet("foo.bar.Baz", "foo.bar.Baz2");
        EasyMock.expect(m_upgradeCheckRegistry.getFailedPluginClassNames()).andReturn(failedClasses).atLeastOnce();
        replayAll();
        org.apache.ambari.spi.upgrade.UpgradeCheckRequest request = new org.apache.ambari.spi.upgrade.UpgradeCheckRequest(null, org.apache.ambari.spi.upgrade.UpgradeType.ROLLING, null, null, null);
        org.apache.ambari.spi.upgrade.UpgradeCheckResult check = m_check.perform(request);
        org.junit.Assert.assertEquals(org.apache.ambari.spi.upgrade.UpgradeCheckStatus.WARNING, check.getStatus());
        java.util.List<java.lang.Object> failedDetails = check.getFailedDetail();
        org.junit.Assert.assertEquals(2, failedDetails.size());
        org.junit.Assert.assertEquals(2, check.getFailedOn().size());
        org.junit.Assert.assertTrue(check.getFailedOn().contains("Baz"));
        verifyAll();
    }

    @org.junit.Test
    public void testPerformWithSuccess() throws java.lang.Exception {
        EasyMock.expect(m_upgradeCheckRegistry.getFailedPluginClassNames()).andReturn(new java.util.HashSet<>()).atLeastOnce();
        replayAll();
        org.apache.ambari.spi.upgrade.UpgradeCheckRequest request = new org.apache.ambari.spi.upgrade.UpgradeCheckRequest(null, org.apache.ambari.spi.upgrade.UpgradeType.ROLLING, null, null, null);
        org.apache.ambari.spi.upgrade.UpgradeCheckResult check = m_check.perform(request);
        org.junit.Assert.assertEquals(org.apache.ambari.spi.upgrade.UpgradeCheckStatus.PASS, check.getStatus());
        org.junit.Assert.assertTrue(org.apache.commons.lang.StringUtils.isBlank(check.getFailReason()));
        verifyAll();
    }
}