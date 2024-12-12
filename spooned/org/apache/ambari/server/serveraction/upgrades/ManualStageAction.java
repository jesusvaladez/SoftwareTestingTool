package org.apache.ambari.server.serveraction.upgrades;
public class ManualStageAction extends org.apache.ambari.server.serveraction.upgrades.AbstractUpgradeServerAction {
    @java.lang.Override
    public org.apache.ambari.server.agent.CommandReport execute(java.util.concurrent.ConcurrentMap<java.lang.String, java.lang.Object> requestSharedDataContext) throws org.apache.ambari.server.AmbariException, java.lang.InterruptedException {
        java.lang.String structOut = getCommandParameterValue("structured_out");
        if (null == structOut) {
            structOut = "{}";
        }
        return createCommandReport(0, org.apache.ambari.server.actionmanager.HostRoleStatus.HOLDING, structOut, "", "");
    }
}