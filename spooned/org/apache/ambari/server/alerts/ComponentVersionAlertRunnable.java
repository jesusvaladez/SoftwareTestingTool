package org.apache.ambari.server.alerts;
import org.apache.commons.lang.StringUtils;
public class ComponentVersionAlertRunnable extends org.apache.ambari.server.alerts.AlertRunnable {
    private static final java.lang.String ALL_COMPONENTS_CORRECT_MSG = "All components are reporting their expected versions.";

    private static final java.lang.String UPGRADE_IN_PROGRESS_MSG = "This alert will be suspended while the {0} is in progress.";

    private static final java.lang.String UNKNOWN_COMPONENT_MSG_TEMPLATE = "Unable to retrieve component information for {0}/{1}";

    private static final java.lang.String MISMATCHED_VERSIONS_MSG = "The following components are reporting unexpected versions: ";

    @com.google.inject.Inject
    private org.apache.ambari.server.api.services.AmbariMetaInfo m_metaInfo;

    public ComponentVersionAlertRunnable(java.lang.String definitionName) {
        super(definitionName);
    }

    @java.lang.Override
    java.util.List<org.apache.ambari.server.state.Alert> execute(org.apache.ambari.server.state.Cluster cluster, org.apache.ambari.server.orm.entities.AlertDefinitionEntity myDefinition) throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.server.orm.entities.UpgradeEntity upgrade = cluster.getUpgradeInProgress();
        if (null != upgrade) {
            org.apache.ambari.server.stack.upgrade.Direction direction = upgrade.getDirection();
            java.lang.String message = java.text.MessageFormat.format(org.apache.ambari.server.alerts.ComponentVersionAlertRunnable.UPGRADE_IN_PROGRESS_MSG, direction.getText(false));
            return java.util.Collections.singletonList(buildAlert(cluster, myDefinition, org.apache.ambari.server.state.AlertState.SKIPPED, message));
        }
        java.util.TreeMap<org.apache.ambari.server.state.Host, java.util.Set<org.apache.ambari.server.state.ServiceComponentHost>> versionMismatches = new java.util.TreeMap<>();
        java.util.Collection<org.apache.ambari.server.state.Host> hosts = cluster.getHosts();
        for (org.apache.ambari.server.state.Host host : hosts) {
            java.util.List<org.apache.ambari.server.state.ServiceComponentHost> hostComponents = cluster.getServiceComponentHosts(host.getHostName());
            for (org.apache.ambari.server.state.ServiceComponentHost hostComponent : hostComponents) {
                org.apache.ambari.server.state.Service service = cluster.getService(hostComponent.getServiceName());
                org.apache.ambari.server.state.ServiceComponent serviceComponent = service.getServiceComponent(hostComponent.getServiceComponentName());
                org.apache.ambari.server.orm.entities.RepositoryVersionEntity desiredRepositoryVersion = service.getDesiredRepositoryVersion();
                org.apache.ambari.server.state.StackId desiredStackId = serviceComponent.getDesiredStackId();
                java.lang.String desiredVersion = desiredRepositoryVersion.getVersion();
                final org.apache.ambari.server.state.ComponentInfo componentInfo;
                try {
                    componentInfo = m_metaInfo.getComponent(desiredStackId.getStackName(), desiredStackId.getStackVersion(), hostComponent.getServiceName(), hostComponent.getServiceComponentName());
                } catch (org.apache.ambari.server.AmbariException ambariException) {
                    java.lang.String message = java.text.MessageFormat.format(org.apache.ambari.server.alerts.ComponentVersionAlertRunnable.UNKNOWN_COMPONENT_MSG_TEMPLATE, hostComponent.getServiceName(), hostComponent.getServiceComponentName());
                    return java.util.Collections.singletonList(buildAlert(cluster, myDefinition, org.apache.ambari.server.state.AlertState.UNKNOWN, message));
                }
                if (!componentInfo.isVersionAdvertised()) {
                    continue;
                }
                java.lang.String version = hostComponent.getVersion();
                if (!org.apache.commons.lang.StringUtils.equals(version, desiredVersion)) {
                    java.util.Set<org.apache.ambari.server.state.ServiceComponentHost> mismatchedComponents = versionMismatches.get(host);
                    if (null == mismatchedComponents) {
                        mismatchedComponents = new java.util.HashSet<>();
                        versionMismatches.put(host, mismatchedComponents);
                    }
                    mismatchedComponents.add(hostComponent);
                }
            }
        }
        org.apache.ambari.server.state.AlertState alertState = org.apache.ambari.server.state.AlertState.OK;
        java.lang.String alertText = org.apache.ambari.server.alerts.ComponentVersionAlertRunnable.ALL_COMPONENTS_CORRECT_MSG;
        if (!versionMismatches.isEmpty()) {
            java.lang.StringBuilder buffer = new java.lang.StringBuilder(org.apache.ambari.server.alerts.ComponentVersionAlertRunnable.MISMATCHED_VERSIONS_MSG);
            buffer.append(java.lang.System.lineSeparator());
            for (org.apache.ambari.server.state.Host host : versionMismatches.keySet()) {
                buffer.append("  ").append(host.getHostName());
                buffer.append(java.lang.System.lineSeparator());
                for (org.apache.ambari.server.state.ServiceComponentHost hostComponent : versionMismatches.get(host)) {
                    buffer.append("    ").append(hostComponent.getServiceComponentName()).append(": ").append(hostComponent.getVersion()).append(java.lang.System.lineSeparator());
                }
            }
            alertText = buffer.toString();
            alertState = org.apache.ambari.server.state.AlertState.WARNING;
        }
        return java.util.Collections.singletonList(buildAlert(cluster, myDefinition, alertState, alertText));
    }
}