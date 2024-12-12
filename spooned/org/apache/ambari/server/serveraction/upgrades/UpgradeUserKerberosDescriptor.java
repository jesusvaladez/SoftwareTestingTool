package org.apache.ambari.server.serveraction.upgrades;
import org.apache.commons.lang.StringUtils;
public class UpgradeUserKerberosDescriptor extends org.apache.ambari.server.serveraction.upgrades.AbstractUpgradeServerAction {
    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.serveraction.upgrades.UpgradeUserKerberosDescriptor.class);

    private static final java.lang.String KERBEROS_DESCRIPTOR_NAME = "kerberos_descriptor";

    private static final java.lang.String KERBEROS_DESCRIPTOR_BACKUP_NAME = "kerberos_descriptor_backup";

    @com.google.inject.Inject
    private org.apache.ambari.server.orm.dao.ArtifactDAO artifactDAO;

    @com.google.inject.Inject
    private org.apache.ambari.server.api.services.AmbariMetaInfo ambariMetaInfo;

    @com.google.inject.Inject
    private org.apache.ambari.server.state.kerberos.KerberosDescriptorFactory kerberosDescriptorFactory;

    protected UpgradeUserKerberosDescriptor() {
    }

    @java.lang.Override
    public org.apache.ambari.server.agent.CommandReport execute(java.util.concurrent.ConcurrentMap<java.lang.String, java.lang.Object> requestSharedDataContext) throws org.apache.ambari.server.AmbariException, java.lang.InterruptedException {
        org.apache.ambari.server.actionmanager.HostRoleCommand hostRoleCommand = getHostRoleCommand();
        java.lang.String clusterName = hostRoleCommand.getExecutionCommandWrapper().getExecutionCommand().getClusterName();
        org.apache.ambari.server.state.Cluster cluster = getClusters().getCluster(clusterName);
        java.util.List<java.lang.String> messages = new java.util.ArrayList<>();
        java.util.List<java.lang.String> errorMessages = new java.util.ArrayList<>();
        org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeContext upgradeContext = getUpgradeContext(cluster);
        logMessage(messages, "Obtaining the user-defined Kerberos descriptor");
        java.util.TreeMap<java.lang.String, java.lang.String> foreignKeys = new java.util.TreeMap<>();
        foreignKeys.put("cluster", java.lang.String.valueOf(cluster.getClusterId()));
        org.apache.ambari.server.orm.entities.ArtifactEntity entity = artifactDAO.findByNameAndForeignKeys("kerberos_descriptor", foreignKeys);
        org.apache.ambari.server.state.kerberos.KerberosDescriptor userDescriptor = (entity == null) ? null : kerberosDescriptorFactory.createInstance(entity.getArtifactData());
        if (userDescriptor != null) {
            @org.apache.ambari.annotations.Experimental(feature = org.apache.ambari.annotations.ExperimentalFeature.PATCH_UPGRADES, comment = "This needs to be correctly done per-service")
            org.apache.ambari.server.state.StackId originalStackId = cluster.getCurrentStackVersion();
            org.apache.ambari.server.state.StackId targetStackId = upgradeContext.getRepositoryVersion().getStackId();
            if (upgradeContext.getDirection() == org.apache.ambari.server.stack.upgrade.Direction.DOWNGRADE) {
                restoreDescriptor(foreignKeys, messages, errorMessages);
            } else {
                backupDescriptor(foreignKeys, messages, errorMessages);
                org.apache.ambari.server.state.kerberos.KerberosDescriptor newDescriptor = null;
                org.apache.ambari.server.state.kerberos.KerberosDescriptor previousDescriptor = null;
                if (targetStackId == null) {
                    logErrorMessage(messages, errorMessages, "The new stack version information was not found.");
                } else {
                    logMessage(messages, java.lang.String.format("Obtaining new stack Kerberos descriptor for %s.", targetStackId.toString()));
                    newDescriptor = ambariMetaInfo.getKerberosDescriptor(targetStackId.getStackName(), targetStackId.getStackVersion(), false);
                    if (newDescriptor == null) {
                        logErrorMessage(messages, errorMessages, java.lang.String.format("The Kerberos descriptor for the new stack version, %s, was not found.", targetStackId.toString()));
                    }
                }
                if (originalStackId == null) {
                    logErrorMessage(messages, errorMessages, "The previous stack version information was not found.");
                } else {
                    logMessage(messages, java.lang.String.format("Obtaining previous stack Kerberos descriptor for %s.", originalStackId.toString()));
                    previousDescriptor = ambariMetaInfo.getKerberosDescriptor(originalStackId.getStackName(), originalStackId.getStackVersion(), false);
                    if (newDescriptor == null) {
                        logErrorMessage(messages, errorMessages, java.lang.String.format("The Kerberos descriptor for the previous stack version, %s, was not found.", originalStackId.toString()));
                    }
                }
                if (errorMessages.isEmpty()) {
                    logMessage(messages, "Updating the user-specified Kerberos descriptor.");
                    org.apache.ambari.server.state.kerberos.KerberosDescriptor updatedDescriptor = org.apache.ambari.server.state.kerberos.KerberosDescriptorUpdateHelper.updateUserKerberosDescriptor(previousDescriptor, newDescriptor, userDescriptor);
                    logMessage(messages, "Storing updated user-specified Kerberos descriptor.");
                    entity.setArtifactData(updatedDescriptor.toMap());
                    artifactDAO.merge(entity);
                    logMessage(messages, "Successfully updated the user-specified Kerberos descriptor.");
                }
            }
        } else {
            logMessage(messages, "A user-specified Kerberos descriptor was not found. No updates are necessary.");
        }
        if (!errorMessages.isEmpty()) {
            logErrorMessage(messages, errorMessages, "No updates have been performed due to previous issues.");
        }
        return createCommandReport(0, org.apache.ambari.server.actionmanager.HostRoleStatus.COMPLETED, "{}", org.apache.commons.lang.StringUtils.join(messages, "\n"), org.apache.commons.lang.StringUtils.join(errorMessages, "\n"));
    }

    private void logMessage(java.util.List<java.lang.String> messages, java.lang.String message) {
        org.apache.ambari.server.serveraction.upgrades.UpgradeUserKerberosDescriptor.LOG.info(message);
        messages.add(message);
    }

    private void logErrorMessage(java.util.List<java.lang.String> messages, java.util.List<java.lang.String> errorMessages, java.lang.String message) {
        org.apache.ambari.server.serveraction.upgrades.UpgradeUserKerberosDescriptor.LOG.error(message);
        messages.add(message);
        errorMessages.add(message);
    }

    private void backupDescriptor(java.util.TreeMap<java.lang.String, java.lang.String> foreignKeys, java.util.List<java.lang.String> messages, java.util.List<java.lang.String> errorMessages) {
        org.apache.ambari.server.orm.entities.ArtifactEntity backupEntity = artifactDAO.findByNameAndForeignKeys(org.apache.ambari.server.serveraction.upgrades.UpgradeUserKerberosDescriptor.KERBEROS_DESCRIPTOR_BACKUP_NAME, foreignKeys);
        if (backupEntity != null) {
            artifactDAO.remove(backupEntity);
        }
        org.apache.ambari.server.orm.entities.ArtifactEntity entity = artifactDAO.findByNameAndForeignKeys(org.apache.ambari.server.serveraction.upgrades.UpgradeUserKerberosDescriptor.KERBEROS_DESCRIPTOR_NAME, foreignKeys);
        if (entity != null) {
            backupEntity = new org.apache.ambari.server.orm.entities.ArtifactEntity();
            backupEntity.setArtifactName(org.apache.ambari.server.serveraction.upgrades.UpgradeUserKerberosDescriptor.KERBEROS_DESCRIPTOR_BACKUP_NAME);
            backupEntity.setForeignKeys(entity.getForeignKeys());
            backupEntity.setArtifactData(entity.getArtifactData());
            artifactDAO.create(backupEntity);
            logMessage(messages, "Created backup of kerberos descriptor");
        } else {
            logErrorMessage(messages, errorMessages, "No backup of kerberos descriptor created due to missing original kerberos descriptor");
        }
    }

    private void restoreDescriptor(java.util.TreeMap<java.lang.String, java.lang.String> foreignKeys, java.util.List<java.lang.String> messages, java.util.List<java.lang.String> errorMessages) {
        org.apache.ambari.server.orm.entities.ArtifactEntity backupEntity = artifactDAO.findByNameAndForeignKeys(org.apache.ambari.server.serveraction.upgrades.UpgradeUserKerberosDescriptor.KERBEROS_DESCRIPTOR_BACKUP_NAME, foreignKeys);
        if (backupEntity != null) {
            org.apache.ambari.server.orm.entities.ArtifactEntity entity = artifactDAO.findByNameAndForeignKeys(org.apache.ambari.server.serveraction.upgrades.UpgradeUserKerberosDescriptor.KERBEROS_DESCRIPTOR_NAME, foreignKeys);
            if (entity != null) {
                artifactDAO.remove(entity);
            }
            entity = new org.apache.ambari.server.orm.entities.ArtifactEntity();
            entity.setArtifactName(org.apache.ambari.server.serveraction.upgrades.UpgradeUserKerberosDescriptor.KERBEROS_DESCRIPTOR_NAME);
            entity.setForeignKeys(backupEntity.getForeignKeys());
            entity.setArtifactData(backupEntity.getArtifactData());
            artifactDAO.create(entity);
            logMessage(messages, "Restored kerberos descriptor from backup");
        } else {
            logErrorMessage(messages, errorMessages, "No backup of kerberos descriptor found");
        }
    }
}