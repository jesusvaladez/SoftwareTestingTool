package org.apache.ambari.server.checks;
public class UpgradeCheckRegistryProvider implements com.google.inject.Provider<org.apache.ambari.server.checks.UpgradeCheckRegistry> {
    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.checks.UpgradeCheckRegistryProvider.class);

    @com.google.inject.Inject
    private com.google.inject.Injector m_injector;

    private org.apache.ambari.server.checks.UpgradeCheckRegistry m_checkRegistry;

    private java.util.Set<org.springframework.beans.factory.config.BeanDefinition> m_beanDefinitions = null;

    @java.lang.Override
    public org.apache.ambari.server.checks.UpgradeCheckRegistry get() {
        if ((null == m_beanDefinitions) || m_beanDefinitions.isEmpty()) {
            java.lang.String packageName = org.apache.ambari.server.checks.ClusterCheck.class.getPackage().getName();
            org.apache.ambari.server.checks.UpgradeCheckRegistryProvider.LOG.info("Searching package {} for classes matching {}", packageName, org.apache.ambari.spi.upgrade.UpgradeCheck.class);
            org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider scanner = new org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider(false);
            org.springframework.core.type.filter.AssignableTypeFilter filter = new org.springframework.core.type.filter.AssignableTypeFilter(org.apache.ambari.spi.upgrade.UpgradeCheck.class);
            scanner.addIncludeFilter(filter);
            m_beanDefinitions = scanner.findCandidateComponents(packageName);
        }
        if ((null == m_beanDefinitions) || (m_beanDefinitions.size() == 0)) {
            org.apache.ambari.server.checks.UpgradeCheckRegistryProvider.LOG.error("No instances of {} found to register", org.apache.ambari.spi.upgrade.UpgradeCheck.class);
            return null;
        }
        m_checkRegistry = new org.apache.ambari.server.checks.UpgradeCheckRegistry();
        m_injector.injectMembers(m_checkRegistry);
        for (org.springframework.beans.factory.config.BeanDefinition beanDefinition : m_beanDefinitions) {
            java.lang.String className = beanDefinition.getBeanClassName();
            java.lang.Class<?> clazz = org.springframework.util.ClassUtils.resolveClassName(className, org.springframework.util.ClassUtils.getDefaultClassLoader());
            try {
                org.apache.ambari.spi.upgrade.UpgradeCheck upgradeCheck = ((org.apache.ambari.spi.upgrade.UpgradeCheck) (m_injector.getInstance(clazz)));
                m_checkRegistry.register(upgradeCheck);
                org.apache.ambari.server.checks.UpgradeCheckRegistryProvider.LOG.info("Registered pre-upgrade check {}", upgradeCheck.getClass());
            } catch (java.lang.Exception exception) {
                org.apache.ambari.server.checks.UpgradeCheckRegistryProvider.LOG.error("Unable to bind and register upgrade check {}", clazz, exception);
            }
        }
        return m_checkRegistry;
    }
}