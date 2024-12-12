package org.apache.ambari.server.checks;
import org.codehaus.jackson.annotate.JsonProperty;
@com.google.inject.Singleton
@org.apache.ambari.annotations.UpgradeCheckInfo(group = org.apache.ambari.spi.upgrade.UpgradeCheckGroup.INFORMATIONAL_WARNING, required = { org.apache.ambari.spi.upgrade.UpgradeType.ROLLING, org.apache.ambari.spi.upgrade.UpgradeType.NON_ROLLING, org.apache.ambari.spi.upgrade.UpgradeType.HOST_ORDERED })
public class PluginChecksLoadedCheck extends org.apache.ambari.server.checks.ClusterCheck {
    private static final org.apache.ambari.spi.upgrade.UpgradeCheckDescription PLUGIN_CHECK_LOAD_FAILURE = new org.apache.ambari.spi.upgrade.UpgradeCheckDescription("PLUGIN_CHECK_LOAD_FAILURE", org.apache.ambari.spi.upgrade.UpgradeCheckType.CLUSTER, "Plugin Upgrade Checks", new com.google.common.collect.ImmutableMap.Builder<java.lang.String, java.lang.String>().put(org.apache.ambari.spi.upgrade.UpgradeCheckDescription.DEFAULT, "The following upgrade checks could not be loaded and were not run. " + ("Although this will not prevent your ability to upgrade, it is advised that you " + "correct these checks before proceeding.")).build());

    @com.google.inject.Inject
    com.google.inject.Provider<org.apache.ambari.server.checks.UpgradeCheckRegistry> m_upgradeCheckRegistryProvider;

    public PluginChecksLoadedCheck() {
        super(org.apache.ambari.server.checks.PluginChecksLoadedCheck.PLUGIN_CHECK_LOAD_FAILURE);
    }

    @java.lang.Override
    public org.apache.ambari.spi.upgrade.UpgradeCheckResult perform(org.apache.ambari.spi.upgrade.UpgradeCheckRequest request) throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.spi.upgrade.UpgradeCheckResult result = new org.apache.ambari.spi.upgrade.UpgradeCheckResult(this, org.apache.ambari.spi.upgrade.UpgradeCheckStatus.PASS);
        java.util.Set<java.lang.String> failedPluginClasses = m_upgradeCheckRegistryProvider.get().getFailedPluginClassNames();
        if ((null == failedPluginClasses) || failedPluginClasses.isEmpty()) {
            return result;
        }
        java.util.Set<org.apache.ambari.server.checks.PluginChecksLoadedCheck.FailedPluginClassDetail> failedPluginSimpleClasses = failedPluginClasses.stream().map(org.apache.ambari.server.checks.PluginChecksLoadedCheck.FailedPluginClassDetail::new).collect(java.util.stream.Collectors.toSet());
        result.setStatus(org.apache.ambari.spi.upgrade.UpgradeCheckStatus.WARNING);
        result.getFailedDetail().addAll(failedPluginSimpleClasses);
        result.setFailReason(getFailReason(result, request));
        result.getFailedOn().addAll(failedPluginSimpleClasses.stream().map(detail -> detail.toSimpleString()).collect(java.util.stream.Collectors.toSet()));
        return result;
    }

    static class FailedPluginClassDetail {
        final java.lang.String m_fullyQualifiedClass;

        @org.codehaus.jackson.annotate.JsonProperty("package_name")
        final java.lang.String m_packageName;

        @org.codehaus.jackson.annotate.JsonProperty("class_name")
        final java.lang.String m_className;

        FailedPluginClassDetail(java.lang.String fullyQualifiedClass) {
            m_fullyQualifiedClass = fullyQualifiedClass;
            int indexOfLastDot = fullyQualifiedClass.lastIndexOf('.');
            if (indexOfLastDot >= 0) {
                m_packageName = fullyQualifiedClass.substring(0, indexOfLastDot);
                m_className = fullyQualifiedClass.substring(indexOfLastDot + 1);
            } else {
                m_packageName = "";
                m_className = fullyQualifiedClass;
            }
        }

        @java.lang.Override
        public java.lang.String toString() {
            return m_fullyQualifiedClass;
        }

        public java.lang.String toSimpleString() {
            return m_className;
        }

        @java.lang.Override
        public int hashCode() {
            return java.util.Objects.hash(m_packageName, m_className);
        }

        @java.lang.Override
        public boolean equals(java.lang.Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            org.apache.ambari.server.checks.PluginChecksLoadedCheck.FailedPluginClassDetail other = ((org.apache.ambari.server.checks.PluginChecksLoadedCheck.FailedPluginClassDetail) (obj));
            return java.util.Objects.equals(m_packageName, other.m_packageName) && java.util.Objects.equals(m_className, other.m_className);
        }
    }
}