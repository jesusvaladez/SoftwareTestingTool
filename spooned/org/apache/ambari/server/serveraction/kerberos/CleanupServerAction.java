package org.apache.ambari.server.serveraction.kerberos;
public class CleanupServerAction extends org.apache.ambari.server.serveraction.kerberos.KerberosServerAction {
    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.serveraction.kerberos.CleanupServerAction.class);

    @java.lang.Override
    protected boolean pruneServiceFilter() {
        return false;
    }

    @java.lang.Override
    protected org.apache.ambari.server.agent.CommandReport processIdentity(org.apache.ambari.server.serveraction.kerberos.stageutils.ResolvedKerberosPrincipal resolvedPrincipal, org.apache.ambari.server.serveraction.kerberos.KerberosOperationHandler operationHandler, java.util.Map<java.lang.String, java.lang.String> kerberosConfiguration, boolean includedInFilter, java.util.Map<java.lang.String, java.lang.Object> requestSharedDataContext) throws org.apache.ambari.server.AmbariException {
        return null;
    }

    @java.lang.Override
    public org.apache.ambari.server.agent.CommandReport execute(java.util.concurrent.ConcurrentMap<java.lang.String, java.lang.Object> requestSharedDataContext) throws org.apache.ambari.server.AmbariException, java.lang.InterruptedException {
        org.apache.ambari.server.state.Cluster cluster = getCluster();
        if (cluster.getSecurityType().equals(org.apache.ambari.server.state.SecurityType.NONE)) {
            removeKerberosArtifact(cluster);
        }
        return createCommandReport(0, org.apache.ambari.server.actionmanager.HostRoleStatus.COMPLETED, "{}", actionLog.getStdOut(), actionLog.getStdErr());
    }

    private void removeKerberosArtifact(org.apache.ambari.server.state.Cluster cluster) throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.server.controller.utilities.PredicateBuilder pb = new org.apache.ambari.server.controller.utilities.PredicateBuilder();
        org.apache.ambari.server.controller.spi.Predicate predicate = pb.begin().property("Artifacts/cluster_name").equals(cluster.getClusterName()).and().property(org.apache.ambari.server.controller.internal.ArtifactResourceProvider.ARTIFACT_NAME_PROPERTY).equals("kerberos_descriptor").end().toPredicate();
        org.apache.ambari.server.controller.spi.ClusterController clusterController = org.apache.ambari.server.controller.utilities.ClusterControllerHelper.getClusterController();
        org.apache.ambari.server.controller.spi.ResourceProvider artifactProvider = clusterController.ensureResourceProvider(org.apache.ambari.server.controller.spi.Resource.Type.Artifact);
        try {
            artifactProvider.deleteResources(new org.apache.ambari.server.controller.internal.RequestImpl(null, null, null, null), predicate);
            org.apache.ambari.server.serveraction.kerberos.CleanupServerAction.LOG.info("Kerberos descriptor removed successfully.");
            actionLog.writeStdOut("Kerberos descriptor removed successfully.");
        } catch (org.apache.ambari.server.controller.spi.NoSuchResourceException e) {
            org.apache.ambari.server.serveraction.kerberos.CleanupServerAction.LOG.warn("The Kerberos descriptor was not found in the database while attempting to remove.");
            actionLog.writeStdOut("The Kerberos descriptor was not found in the database while attempting to remove.");
        } catch (java.lang.Exception e) {
            throw new org.apache.ambari.server.AmbariException("An unknown error occurred while trying to delete the cluster Kerberos descriptor", e);
        }
    }
}