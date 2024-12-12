package org.apache.ambari.server.serveraction.upgrades;
import org.apache.commons.lang.StringUtils;
public class ComponentVersionCheckAction extends org.apache.ambari.server.serveraction.upgrades.FinalizeUpgradeAction {
    @java.lang.Override
    public org.apache.ambari.server.agent.CommandReport execute(java.util.concurrent.ConcurrentMap<java.lang.String, java.lang.Object> requestSharedDataContext) throws org.apache.ambari.server.AmbariException, java.lang.InterruptedException {
        java.lang.String clusterName = getExecutionCommand().getClusterName();
        org.apache.ambari.server.state.Cluster cluster = getClusters().getCluster(clusterName);
        org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeContext upgradeContext = getUpgradeContext(cluster);
        java.util.Set<org.apache.ambari.server.serveraction.upgrades.FinalizeUpgradeAction.InfoTuple> errors = validateComponentVersions(upgradeContext);
        java.lang.StringBuilder outSB = new java.lang.StringBuilder();
        java.lang.StringBuilder errSB = new java.lang.StringBuilder();
        if (errors.isEmpty()) {
            outSB.append("All service components are reporting the correct version.");
            return createCommandReport(0, org.apache.ambari.server.actionmanager.HostRoleStatus.COMPLETED, "{}", outSB.toString(), errSB.toString());
        } else {
            java.lang.String structuredOut = getErrors(outSB, errSB, errors);
            return createCommandReport(-1, org.apache.ambari.server.actionmanager.HostRoleStatus.FAILED, structuredOut, outSB.toString(), errSB.toString());
        }
    }

    private java.lang.String getErrors(java.lang.StringBuilder outSB, java.lang.StringBuilder errSB, java.util.Set<org.apache.ambari.server.serveraction.upgrades.FinalizeUpgradeAction.InfoTuple> errors) {
        errSB.append("Finalization will not be able to completed because of the following version inconsistencies:");
        errSB.append(java.lang.System.lineSeparator());
        java.util.Set<java.lang.String> hosts = new java.util.TreeSet<>();
        java.util.Map<java.lang.String, com.google.gson.JsonArray> hostDetails = new java.util.HashMap<>();
        for (org.apache.ambari.server.serveraction.upgrades.FinalizeUpgradeAction.InfoTuple tuple : errors) {
            errSB.append("  ");
            errSB.append(tuple.hostName).append(": ");
            errSB.append(java.lang.System.lineSeparator()).append("    ");
            errSB.append(tuple.serviceName).append('/').append(tuple.componentName);
            errSB.append(" reports ").append(org.apache.commons.lang.StringUtils.trimToEmpty(tuple.currentVersion));
            errSB.append(" but expects ").append(tuple.targetVersion);
            errSB.append(java.lang.System.lineSeparator());
            hosts.add(tuple.hostName);
            if (!hostDetails.containsKey(tuple.hostName)) {
                hostDetails.put(tuple.hostName, new com.google.gson.JsonArray());
            }
            com.google.gson.JsonObject obj = new com.google.gson.JsonObject();
            obj.addProperty("service", tuple.serviceName);
            obj.addProperty("component", tuple.componentName);
            obj.addProperty("version", tuple.currentVersion);
            obj.addProperty("targetVersion", tuple.targetVersion);
            hostDetails.get(tuple.hostName).add(obj);
        }
        com.google.gson.JsonArray hostJson = new com.google.gson.JsonArray();
        for (java.lang.String h : hosts) {
            hostJson.add(new com.google.gson.JsonPrimitive(h));
        }
        com.google.gson.JsonObject valueJson = new com.google.gson.JsonObject();
        for (java.util.Map.Entry<java.lang.String, com.google.gson.JsonArray> entry : hostDetails.entrySet()) {
            valueJson.add(entry.getKey(), entry.getValue());
        }
        outSB.append(java.lang.String.format("There were errors on the following hosts: %s", org.apache.commons.lang.StringUtils.join(hosts, ", ")));
        com.google.gson.JsonObject obj = new com.google.gson.JsonObject();
        obj.add("hosts", hostJson);
        obj.add("host_detail", valueJson);
        return obj.toString();
    }
}