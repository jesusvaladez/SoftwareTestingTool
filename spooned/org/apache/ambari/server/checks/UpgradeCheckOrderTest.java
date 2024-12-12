package org.apache.ambari.server.checks;
public class UpgradeCheckOrderTest {
    @org.junit.Test
    public void testUpgradeOrder() throws java.lang.Exception {
        java.lang.String sourceResourceDirectory = ((("src" + java.io.File.separator) + "test") + java.io.File.separator) + "resources";
        java.util.Properties properties = new java.util.Properties();
        properties.setProperty(org.apache.ambari.server.configuration.Configuration.SERVER_PERSISTENCE_TYPE.getKey(), "in-memory");
        properties.setProperty(org.apache.ambari.server.configuration.Configuration.OS_VERSION.getKey(), "centos6");
        properties.setProperty(org.apache.ambari.server.configuration.Configuration.SHARED_RESOURCES_DIR.getKey(), sourceResourceDirectory);
        com.google.inject.Injector injector = com.google.inject.Guice.createInjector(new org.apache.ambari.server.controller.ControllerModule(properties), new org.apache.ambari.server.audit.AuditLoggerModule(), new org.apache.ambari.server.ldap.LdapModule());
        org.apache.ambari.server.checks.UpgradeCheckRegistry registry = injector.getInstance(org.apache.ambari.server.checks.UpgradeCheckRegistry.class);
        org.apache.ambari.server.checks.UpgradeCheckRegistry registry2 = injector.getInstance(org.apache.ambari.server.checks.UpgradeCheckRegistry.class);
        org.junit.Assert.assertEquals(registry, registry2);
        java.util.List<org.apache.ambari.spi.upgrade.UpgradeCheck> checks = registry.getBuiltInUpgradeChecks();
        org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider scanner = new org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider(false);
        org.springframework.core.type.filter.AssignableTypeFilter filter = new org.springframework.core.type.filter.AssignableTypeFilter(org.apache.ambari.spi.upgrade.UpgradeCheck.class);
        scanner.addIncludeFilter(filter);
        java.util.Set<org.springframework.beans.factory.config.BeanDefinition> beanDefinitions = scanner.findCandidateComponents("org.apache.ambari.server.checks");
        org.junit.Assert.assertTrue(checks.size() > 0);
        org.junit.Assert.assertTrue(beanDefinitions.size() > 0);
        org.junit.Assert.assertEquals(beanDefinitions.size(), checks.size());
        org.apache.ambari.spi.upgrade.UpgradeCheck lastCheck = null;
        for (org.apache.ambari.spi.upgrade.UpgradeCheck check : checks) {
            org.apache.ambari.spi.upgrade.UpgradeCheckGroup group = org.apache.ambari.spi.upgrade.UpgradeCheckGroup.DEFAULT;
            org.apache.ambari.spi.upgrade.UpgradeCheckGroup lastGroup = org.apache.ambari.spi.upgrade.UpgradeCheckGroup.DEFAULT;
            if (null != lastCheck) {
                org.apache.ambari.annotations.UpgradeCheckInfo annotation = check.getClass().getAnnotation(org.apache.ambari.annotations.UpgradeCheckInfo.class);
                org.apache.ambari.annotations.UpgradeCheckInfo lastAnnotation = lastCheck.getClass().getAnnotation(org.apache.ambari.annotations.UpgradeCheckInfo.class);
                if ((null != annotation) && (null != lastAnnotation)) {
                    group = annotation.group();
                    lastGroup = lastAnnotation.group();
                    org.junit.Assert.assertTrue(lastGroup.getOrder().compareTo(group.getOrder()) <= 0);
                }
            }
            lastCheck = check;
        }
    }
}