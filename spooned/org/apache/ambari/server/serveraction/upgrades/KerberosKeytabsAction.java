package org.apache.ambari.server.serveraction.upgrades;
public class KerberosKeytabsAction extends org.apache.ambari.server.serveraction.upgrades.AbstractUpgradeServerAction {
    private static final java.lang.String KERBEROS_ENV = "kerberos-env";

    private static final java.lang.String KDC_TYPE_KEY = "kdc_type";

    @com.google.inject.Inject
    private org.apache.ambari.server.controller.KerberosHelper m_kerberosHelper;

    @java.lang.Override
    public org.apache.ambari.server.agent.CommandReport execute(java.util.concurrent.ConcurrentMap<java.lang.String, java.lang.Object> requestSharedDataContext) throws org.apache.ambari.server.AmbariException, java.lang.InterruptedException {
        java.lang.String clusterName = getExecutionCommand().getClusterName();
        org.apache.ambari.server.state.Cluster cluster = getClusters().getCluster(clusterName);
        java.lang.StringBuilder stdout = new java.lang.StringBuilder();
        stdout.append(java.lang.String.format("Checking %s is secured by Kerberos... %s", clusterName, m_kerberosHelper.isClusterKerberosEnabled(cluster))).append(java.lang.System.lineSeparator());
        if (!m_kerberosHelper.isClusterKerberosEnabled(cluster)) {
            stdout.append(java.lang.String.format("Cluster %s is not secured by Kerberos.  No action required.", clusterName));
            return createCommandReport(0, org.apache.ambari.server.actionmanager.HostRoleStatus.COMPLETED, "{}", stdout.toString(), "");
        }
        stdout.append(java.lang.String.format("Loading %s for cluster %s", org.apache.ambari.server.serveraction.upgrades.KerberosKeytabsAction.KERBEROS_ENV, clusterName)).append(java.lang.System.lineSeparator());
        org.apache.ambari.server.state.Config kerberosEnv = cluster.getDesiredConfigByType(org.apache.ambari.server.serveraction.upgrades.KerberosKeytabsAction.KERBEROS_ENV);
        if (kerberosEnv == null) {
            stdout.append(java.lang.String.format("Configuration %s was not found.  No action required.", org.apache.ambari.server.serveraction.upgrades.KerberosKeytabsAction.KERBEROS_ENV));
            return createCommandReport(0, org.apache.ambari.server.actionmanager.HostRoleStatus.COMPLETED, "{}", stdout.toString(), "");
        }
        java.util.Map<java.lang.String, java.lang.String> kerbProperties = kerberosEnv.getProperties();
        org.apache.ambari.server.serveraction.kerberos.KDCType kdcType = org.apache.ambari.server.serveraction.kerberos.KDCType.NONE;
        if ((null != kerbProperties) && kerbProperties.containsKey(org.apache.ambari.server.serveraction.upgrades.KerberosKeytabsAction.KDC_TYPE_KEY)) {
            kdcType = org.apache.ambari.server.serveraction.kerberos.KDCType.translate(kerbProperties.get(org.apache.ambari.server.serveraction.upgrades.KerberosKeytabsAction.KDC_TYPE_KEY));
        }
        stdout.append(java.lang.String.format("Checking KDC type... %s", kdcType)).append(java.lang.System.lineSeparator());
        if (org.apache.ambari.server.serveraction.kerberos.KDCType.NONE == kdcType) {
            stdout.append(java.lang.String.format("KDC Type is %s, keytabs are managed manually.  No action required.", kdcType));
            return createCommandReport(0, org.apache.ambari.server.actionmanager.HostRoleStatus.COMPLETED, "{}", stdout.toString(), "");
        }
        stdout.append(java.lang.String.format("Ambari is managing Kerberos keytabs.  Regenerate " + "keytabs after upgrade is complete."));
        return createCommandReport(0, org.apache.ambari.server.actionmanager.HostRoleStatus.HOLDING, "{}", stdout.toString(), "");
    }
}