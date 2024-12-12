package org.apache.ambari.server.checks;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.scanners.TypeAnnotationsScanner;
import org.reflections.util.ConfigurationBuilder;
public class UpgradeCheckRegistry {
    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.checks.UpgradeCheckRegistry.class);

    @com.google.inject.Inject
    private com.google.inject.Provider<org.apache.ambari.server.api.services.AmbariMetaInfo> metainfoProvider;

    @com.google.inject.Inject
    private com.google.inject.Injector m_injector;

    private final java.util.Set<org.apache.ambari.spi.upgrade.UpgradeCheck> m_builtInChecks = new java.util.TreeSet<>(new org.apache.ambari.server.checks.UpgradeCheckRegistry.PreUpgradeCheckComparator());

    private final java.util.Map<org.apache.ambari.server.stack.upgrade.UpgradePack, org.apache.ambari.server.checks.UpgradeCheckRegistry.PluginUpgradeChecks> m_pluginChecks = new java.util.HashMap<>();

    public void register(org.apache.ambari.spi.upgrade.UpgradeCheck upgradeCheck) {
        m_builtInChecks.add(upgradeCheck);
    }

    public java.util.List<org.apache.ambari.spi.upgrade.UpgradeCheck> getBuiltInUpgradeChecks() {
        return new java.util.ArrayList<>(m_builtInChecks);
    }

    public java.util.List<org.apache.ambari.spi.upgrade.UpgradeCheck> getFilteredUpgradeChecks(org.apache.ambari.server.stack.upgrade.UpgradePack upgradePack) throws org.apache.ambari.server.AmbariException {
        java.util.List<org.apache.ambari.spi.upgrade.UpgradeCheck> builtInRequiredChecks = new java.util.ArrayList<>();
        for (org.apache.ambari.spi.upgrade.UpgradeCheck builtInCheck : m_builtInChecks) {
            if (isBuiltInCheckRequired(builtInCheck, upgradePack.getType())) {
                builtInRequiredChecks.add(builtInCheck);
            }
        }
        org.apache.ambari.server.checks.UpgradeCheckRegistry.PluginUpgradeChecks pluginChecks = m_pluginChecks.get(upgradePack);
        if (null == pluginChecks) {
            pluginChecks = new org.apache.ambari.server.checks.UpgradeCheckRegistry.PluginUpgradeChecks(new java.util.TreeSet<>(new org.apache.ambari.server.checks.UpgradeCheckRegistry.PreUpgradeCheckComparator()), new java.util.TreeSet<>());
            m_pluginChecks.put(upgradePack, pluginChecks);
            java.util.List<java.lang.String> pluginCheckClassNames = upgradePack.getPrerequisiteChecks();
            if ((null != pluginCheckClassNames) && (!pluginCheckClassNames.isEmpty())) {
                loadPluginUpgradeChecksFromStack(upgradePack, pluginChecks);
            }
        }
        final java.util.Set<org.apache.ambari.spi.upgrade.UpgradeCheck> combinedUpgradeChecks = new java.util.TreeSet<>(new org.apache.ambari.server.checks.UpgradeCheckRegistry.PreUpgradeCheckComparator());
        combinedUpgradeChecks.addAll(builtInRequiredChecks);
        combinedUpgradeChecks.addAll(pluginChecks.m_loadedChecks);
        return new java.util.LinkedList<>(combinedUpgradeChecks);
    }

    public java.util.Set<java.lang.String> getFailedPluginClassNames() {
        java.util.Collection<org.apache.ambari.server.checks.UpgradeCheckRegistry.PluginUpgradeChecks> pluginUpgradeChecks = m_pluginChecks.values();
        return pluginUpgradeChecks.stream().flatMap(plugins -> plugins.m_failedChecks.stream()).collect(java.util.stream.Collectors.toSet());
    }

    private void loadPluginUpgradeChecksFromStack(org.apache.ambari.server.stack.upgrade.UpgradePack upgradePack, org.apache.ambari.server.checks.UpgradeCheckRegistry.PluginUpgradeChecks pluginChecks) throws org.apache.ambari.server.AmbariException {
        java.util.Set<java.lang.String> pluginCheckClassNames = new java.util.HashSet<>(upgradePack.getPrerequisiteChecks());
        org.apache.ambari.server.state.StackId ownerStackId = upgradePack.getOwnerStackId();
        org.apache.ambari.server.state.StackInfo stackInfo = metainfoProvider.get().getStack(ownerStackId);
        java.net.URLClassLoader classLoader = stackInfo.getLibraryClassLoader();
        if (null != classLoader) {
            for (java.lang.String pluginCheckClassName : pluginCheckClassNames) {
                try {
                    org.apache.ambari.spi.upgrade.UpgradeCheck upgradeCheck = stackInfo.getLibraryInstance(m_injector, pluginCheckClassName);
                    pluginChecks.m_loadedChecks.add(upgradeCheck);
                    org.apache.ambari.server.checks.UpgradeCheckRegistry.LOG.info("Registered pre-upgrade check {} for stack {}", pluginCheckClassName, ownerStackId);
                } catch (java.lang.Exception exception) {
                    org.apache.ambari.server.checks.UpgradeCheckRegistry.LOG.error("Unable to load the upgrade check {}", pluginCheckClassName, exception);
                    pluginChecks.m_failedChecks.add(pluginCheckClassName);
                }
            }
            org.reflections.Reflections reflections = new org.reflections.Reflections(new org.reflections.util.ConfigurationBuilder().addClassLoader(classLoader).addUrls(classLoader.getURLs()).setScanners(new org.reflections.scanners.SubTypesScanner(), new org.reflections.scanners.TypeAnnotationsScanner()));
            java.util.Set<java.lang.Class<? extends org.apache.ambari.spi.upgrade.UpgradeCheck>> upgradeChecksFromLoader = reflections.getSubTypesOf(org.apache.ambari.spi.upgrade.UpgradeCheck.class);
            if ((null != upgradeChecksFromLoader) && (!upgradeChecksFromLoader.isEmpty())) {
                for (java.lang.Class<? extends org.apache.ambari.spi.upgrade.UpgradeCheck> clazz : upgradeChecksFromLoader) {
                    if (pluginCheckClassNames.contains(clazz.getName())) {
                        continue;
                    }
                    org.apache.ambari.annotations.UpgradeCheckInfo upgradeCheckInfo = clazz.getAnnotation(org.apache.ambari.annotations.UpgradeCheckInfo.class);
                    if ((null != upgradeCheckInfo) && org.apache.commons.lang.ArrayUtils.contains(upgradeCheckInfo.required(), upgradePack.getType())) {
                        try {
                            pluginChecks.m_loadedChecks.add(clazz.newInstance());
                            org.apache.ambari.server.checks.UpgradeCheckRegistry.LOG.info("Registered pre-upgrade check {} for stack {}", clazz, ownerStackId);
                        } catch (java.lang.Exception exception) {
                            org.apache.ambari.server.checks.UpgradeCheckRegistry.LOG.error("Unable to load the upgrade check {}", clazz, exception);
                            pluginChecks.m_failedChecks.add(clazz.getName());
                        }
                    }
                }
            }
        } else {
            org.apache.ambari.server.checks.UpgradeCheckRegistry.LOG.error("Unable to perform the following upgrade checks because no libraries could be loaded for the {} stack: {}", ownerStackId, org.apache.commons.lang.StringUtils.join(pluginCheckClassNames, ", "));
            pluginChecks.m_failedChecks.addAll(pluginCheckClassNames);
        }
    }

    private boolean isBuiltInCheckRequired(org.apache.ambari.spi.upgrade.UpgradeCheck upgradeCheck, org.apache.ambari.spi.upgrade.UpgradeType upgradeType) {
        if (upgradeType == null) {
            return true;
        }
        org.apache.ambari.annotations.UpgradeCheckInfo annotation = upgradeCheck.getClass().getAnnotation(org.apache.ambari.annotations.UpgradeCheckInfo.class);
        if (null == annotation) {
            return false;
        }
        org.apache.ambari.spi.upgrade.UpgradeType[] upgradeTypes = annotation.required();
        return org.apache.commons.lang.ArrayUtils.contains(upgradeTypes, upgradeType);
    }

    private static final class PreUpgradeCheckComparator implements java.util.Comparator<org.apache.ambari.spi.upgrade.UpgradeCheck> {
        @java.lang.Override
        public int compare(org.apache.ambari.spi.upgrade.UpgradeCheck check1, org.apache.ambari.spi.upgrade.UpgradeCheck check2) {
            java.lang.Class<? extends org.apache.ambari.spi.upgrade.UpgradeCheck> clazz1 = check1.getClass();
            java.lang.Class<? extends org.apache.ambari.spi.upgrade.UpgradeCheck> clazz2 = check2.getClass();
            org.apache.ambari.annotations.UpgradeCheckInfo annotation1 = clazz1.getAnnotation(org.apache.ambari.annotations.UpgradeCheckInfo.class);
            org.apache.ambari.annotations.UpgradeCheckInfo annotation2 = clazz2.getAnnotation(org.apache.ambari.annotations.UpgradeCheckInfo.class);
            org.apache.ambari.spi.upgrade.UpgradeCheckGroup group1 = org.apache.ambari.spi.upgrade.UpgradeCheckGroup.DEFAULT;
            org.apache.ambari.spi.upgrade.UpgradeCheckGroup group2 = org.apache.ambari.spi.upgrade.UpgradeCheckGroup.DEFAULT;
            java.lang.Float groupOrder1 = java.lang.Float.valueOf(group1.getOrder());
            java.lang.Float groupOrder2 = java.lang.Float.valueOf(group2.getOrder());
            java.lang.Float order1 = 1.0F;
            java.lang.Float order2 = 1.0F;
            if (null != annotation1) {
                group1 = annotation1.group();
                groupOrder1 = java.lang.Float.valueOf(group1.getOrder());
                order1 = java.lang.Float.valueOf(annotation1.order());
            }
            if (null != annotation2) {
                group2 = annotation2.group();
                groupOrder2 = java.lang.Float.valueOf(group2.getOrder());
                order2 = java.lang.Float.valueOf(annotation2.order());
            }
            int groupComparison = groupOrder1.compareTo(groupOrder2);
            if (groupComparison != 0) {
                return groupComparison;
            }
            int orderComparison = order1.compareTo(order2);
            if (orderComparison != 0) {
                return orderComparison;
            }
            return clazz1.getName().compareTo(clazz2.getName());
        }
    }

    public static final class PluginUpgradeChecks {
        private final java.util.Set<org.apache.ambari.spi.upgrade.UpgradeCheck> m_loadedChecks;

        private final java.util.Set<java.lang.String> m_failedChecks;

        private PluginUpgradeChecks(java.util.Set<org.apache.ambari.spi.upgrade.UpgradeCheck> loadedChecks, java.util.Set<java.lang.String> failedChecks) {
            m_loadedChecks = loadedChecks;
            m_failedChecks = failedChecks;
        }
    }
}