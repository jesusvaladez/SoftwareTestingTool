package org.apache.ambari.server.checks;
import static org.apache.ambari.server.stack.upgrade.Task.Type.REGENERATE_KEYTABS;
@org.apache.ambari.annotations.UpgradeCheckInfo(group = org.apache.ambari.spi.upgrade.UpgradeCheckGroup.KERBEROS, required = { org.apache.ambari.spi.upgrade.UpgradeType.ROLLING, org.apache.ambari.spi.upgrade.UpgradeType.NON_ROLLING, org.apache.ambari.spi.upgrade.UpgradeType.HOST_ORDERED })
public class KerberosAdminPersistedCredentialCheck extends org.apache.ambari.server.checks.ClusterCheck {
    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.checks.KerberosAdminPersistedCredentialCheck.class);

    public static final java.lang.String KEY_PERSISTED_STORE_NOT_CONFIGURED = "persisted_store_no_configured";

    public static final java.lang.String KEY_CREDENTIAL_NOT_STORED = "admin_credential_not_stored";

    @com.google.inject.Inject
    private org.apache.ambari.server.security.encryption.CredentialStoreService credentialStoreService;

    @com.google.inject.Inject
    private org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeHelper upgradeHelper;

    static final org.apache.ambari.spi.upgrade.UpgradeCheckDescription KERBEROS_ADMIN_CREDENTIAL_CHECK = new org.apache.ambari.spi.upgrade.UpgradeCheckDescription("KERBEROS_ADMIN_CREDENTIAL_CHECK", org.apache.ambari.spi.upgrade.UpgradeCheckType.CLUSTER, "The KDC administrator credentials need to be stored in Ambari persisted credential store.", new com.google.common.collect.ImmutableMap.Builder<java.lang.String, java.lang.String>().put(org.apache.ambari.server.checks.KerberosAdminPersistedCredentialCheck.KEY_PERSISTED_STORE_NOT_CONFIGURED, "Ambari's credential store has not been configured.  " + "This is needed so the KDC administrator credential may be stored long enough to ensure it will be around if needed during the upgrade process.").put(org.apache.ambari.server.checks.KerberosAdminPersistedCredentialCheck.KEY_CREDENTIAL_NOT_STORED, "The KDC administrator credential has not been stored in the persisted credential store. " + ("Visit the Kerberos administrator page to set the credential. " + "This is needed so the KDC administrator credential may be stored long enough to ensure it will be around if needed during the upgrade process.")).build());

    @com.google.inject.Inject
    public KerberosAdminPersistedCredentialCheck() {
        super(org.apache.ambari.server.checks.KerberosAdminPersistedCredentialCheck.KERBEROS_ADMIN_CREDENTIAL_CHECK);
    }

    @java.lang.Override
    public java.util.Set<java.lang.String> getApplicableServices() {
        return java.util.Collections.emptySet();
    }

    @java.lang.Override
    public org.apache.ambari.spi.upgrade.UpgradeCheckResult perform(org.apache.ambari.spi.upgrade.UpgradeCheckRequest request) throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.spi.upgrade.UpgradeCheckResult result = new org.apache.ambari.spi.upgrade.UpgradeCheckResult(this);
        final java.lang.String clusterName = request.getClusterName();
        final org.apache.ambari.server.state.Cluster cluster = clustersProvider.get().getCluster(clusterName);
        if (cluster.getSecurityType() != org.apache.ambari.server.state.SecurityType.KERBEROS) {
            return result;
        }
        if (!upgradePack(request).anyGroupTaskMatch(task -> task.getType() == org.apache.ambari.server.stack.upgrade.Task.Type.REGENERATE_KEYTABS)) {
            org.apache.ambari.server.checks.KerberosAdminPersistedCredentialCheck.LOG.info("Skipping upgrade check {} because there is no {} in the upgrade pack.", this.getClass().getSimpleName(), org.apache.ambari.server.stack.upgrade.Task.Type.REGENERATE_KEYTABS);
            return result;
        }
        if (!"true".equalsIgnoreCase(getProperty(request, "kerberos-env", "manage_identities"))) {
            return result;
        }
        if (!credentialStoreService.isInitialized(org.apache.ambari.server.security.encryption.CredentialStoreType.PERSISTED)) {
            result.setFailReason(getFailReason(org.apache.ambari.server.checks.KerberosAdminPersistedCredentialCheck.KEY_PERSISTED_STORE_NOT_CONFIGURED, result, request));
            result.setStatus(org.apache.ambari.spi.upgrade.UpgradeCheckStatus.FAIL);
            result.getFailedOn().add(request.getClusterName());
        } else if (credentialStoreService.getCredential(clusterName, org.apache.ambari.server.controller.KerberosHelper.KDC_ADMINISTRATOR_CREDENTIAL_ALIAS, org.apache.ambari.server.security.encryption.CredentialStoreType.PERSISTED) == null) {
            result.setFailReason(getFailReason(org.apache.ambari.server.checks.KerberosAdminPersistedCredentialCheck.KEY_CREDENTIAL_NOT_STORED, result, request));
            result.setStatus(org.apache.ambari.spi.upgrade.UpgradeCheckStatus.FAIL);
            result.getFailedOn().add(request.getClusterName());
        }
        return result;
    }

    private org.apache.ambari.server.stack.upgrade.UpgradePack upgradePack(org.apache.ambari.spi.upgrade.UpgradeCheckRequest request) throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.server.state.Cluster cluster = clustersProvider.get().getCluster(request.getClusterName());
        return upgradeHelper.suggestUpgradePack(request.getClusterName(), cluster.getCurrentStackVersion(), new org.apache.ambari.server.state.StackId(request.getTargetRepositoryVersion().getStackId()), org.apache.ambari.server.stack.upgrade.Direction.UPGRADE, request.getUpgradeType(), null);
    }
}