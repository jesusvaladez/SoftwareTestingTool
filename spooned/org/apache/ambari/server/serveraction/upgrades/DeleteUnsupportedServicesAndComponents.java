package org.apache.ambari.server.serveraction.upgrades;
import org.apache.commons.lang.StringUtils;
import static org.apache.commons.collections.CollectionUtils.union;
@org.apache.ambari.annotations.Experimental(feature = org.apache.ambari.annotations.ExperimentalFeature.ORPHAN_KERBEROS_IDENTITY_REMOVAL, comment = "Not removing identities yet")
public class DeleteUnsupportedServicesAndComponents extends org.apache.ambari.server.serveraction.upgrades.AbstractUpgradeServerAction {
    @com.google.inject.Inject
    private org.apache.ambari.server.state.ServiceComponentSupport serviceComponentSupport;

    @com.google.inject.Inject
    private org.apache.ambari.server.topology.STOMPComponentsDeleteHandler STOMPComponentsDeleteHandler;

    @java.lang.Override
    public org.apache.ambari.server.agent.CommandReport execute(java.util.concurrent.ConcurrentMap<java.lang.String, java.lang.Object> requestSharedDataContext) throws org.apache.ambari.server.AmbariException, java.lang.InterruptedException {
        org.apache.ambari.server.state.Cluster cluster = getClusters().getCluster(getExecutionCommand().getClusterName());
        if (cluster.getUpgradeInProgress().isDowngradeAllowed()) {
            throw new org.apache.ambari.server.AmbariException(this.getClass() + " should not be used in upgrade packs with downgrade support");
        }
        org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeContext upgradeContext = getUpgradeContext(cluster);
        java.util.Set<java.lang.String> removedComponents = deleteUnsupportedComponents(cluster, upgradeContext.getRepositoryVersion());
        java.util.Set<java.lang.String> removedServices = deleteUnsupportedServices(cluster, upgradeContext.getRepositoryVersion());
        return createCommandReport(0, org.apache.ambari.server.actionmanager.HostRoleStatus.COMPLETED, "{}", "Removed services: " + org.apache.commons.lang.StringUtils.join(org.apache.commons.collections.CollectionUtils.union(removedComponents, removedServices), ", "), "");
    }

    private java.util.Set<java.lang.String> deleteUnsupportedServices(org.apache.ambari.server.state.Cluster cluster, org.apache.ambari.server.orm.entities.RepositoryVersionEntity repoVersion) throws org.apache.ambari.server.AmbariException {
        java.util.Set<java.lang.String> servicesToBeRemoved = serviceComponentSupport.unsupportedServices(cluster, repoVersion.getStackName(), repoVersion.getStackVersion());
        for (java.lang.String serviceName : servicesToBeRemoved) {
            org.apache.ambari.server.controller.internal.DeleteHostComponentStatusMetaData deleteMetaData = new org.apache.ambari.server.controller.internal.DeleteHostComponentStatusMetaData();
            cluster.deleteService(serviceName, deleteMetaData);
            STOMPComponentsDeleteHandler.processDeleteByMetaDataException(deleteMetaData);
            STOMPComponentsDeleteHandler.processDeleteByMetaData(deleteMetaData);
            deleteUpgradeHistory(cluster, history -> serviceName.equals(history.getServiceName()));
        }
        return servicesToBeRemoved;
    }

    private java.util.Set<java.lang.String> deleteUnsupportedComponents(org.apache.ambari.server.state.Cluster cluster, org.apache.ambari.server.orm.entities.RepositoryVersionEntity repoVersion) throws org.apache.ambari.server.AmbariException {
        java.util.Set<java.lang.String> deletedComponents = new java.util.HashSet<>();
        for (org.apache.ambari.server.state.ServiceComponent component : serviceComponentSupport.unsupportedComponents(cluster, repoVersion.getStackName(), repoVersion.getStackVersion())) {
            org.apache.ambari.server.controller.internal.DeleteHostComponentStatusMetaData deleteMetaData = new org.apache.ambari.server.controller.internal.DeleteHostComponentStatusMetaData();
            cluster.getService(component.getServiceName()).deleteServiceComponent(component.getName(), deleteMetaData);
            STOMPComponentsDeleteHandler.processDeleteByMetaDataException(deleteMetaData);
            STOMPComponentsDeleteHandler.processDeleteByMetaData(deleteMetaData);
            deleteUpgradeHistory(cluster, history -> component.getName().equals(history.getComponentName()));
            deletedComponents.add(component.getName());
        }
        return deletedComponents;
    }

    private void deleteUpgradeHistory(org.apache.ambari.server.state.Cluster cluster, java.util.function.Predicate<org.apache.ambari.server.orm.entities.UpgradeHistoryEntity> predicate) {
        org.apache.ambari.server.orm.entities.UpgradeEntity upgradeInProgress = cluster.getUpgradeInProgress();
        java.util.List<org.apache.ambari.server.orm.entities.UpgradeHistoryEntity> removed = upgradeInProgress.getHistory().stream().filter(each -> (each != null) && predicate.test(each)).collect(java.util.stream.Collectors.toList());
        upgradeInProgress.removeHistories(removed);
        m_upgradeDAO.merge(upgradeInProgress);
    }
}