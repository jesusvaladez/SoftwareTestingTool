package org.apache.ambari.server.serveraction.upgrades;
public class AddComponentAction extends org.apache.ambari.server.serveraction.upgrades.AbstractUpgradeServerAction {
    @java.lang.Override
    public org.apache.ambari.server.agent.CommandReport execute(java.util.concurrent.ConcurrentMap<java.lang.String, java.lang.Object> requestSharedDataContext) throws org.apache.ambari.server.AmbariException, java.lang.InterruptedException {
        java.util.Map<java.lang.String, java.lang.String> commandParameters = getCommandParameters();
        if ((null == commandParameters) || commandParameters.isEmpty()) {
            return createCommandReport(0, org.apache.ambari.server.actionmanager.HostRoleStatus.FAILED, "{}", "", "Unable to add a new component to the cluster as there is no information on what to add.");
        }
        java.lang.String clusterName = commandParameters.get("clusterName");
        org.apache.ambari.server.state.Cluster cluster = getClusters().getCluster(clusterName);
        org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeContext upgradeContext = getUpgradeContext(cluster);
        if (upgradeContext.isDowngradeAllowed() || upgradeContext.isPatchRevert()) {
            return createCommandReport(0, org.apache.ambari.server.actionmanager.HostRoleStatus.SKIPPED_FAILED, "{}", "", "Unable to add a component during an upgrade which can be downgraded.");
        }
        java.lang.String serializedJson = commandParameters.get(org.apache.ambari.server.stack.upgrade.AddComponentTask.PARAMETER_SERIALIZED_ADD_COMPONENT_TASK);
        com.google.gson.Gson gson = getGson();
        org.apache.ambari.server.stack.upgrade.AddComponentTask task = gson.fromJson(serializedJson, org.apache.ambari.server.stack.upgrade.AddComponentTask.class);
        final org.apache.ambari.server.state.Service service;
        try {
            service = cluster.getService(task.service);
        } catch (org.apache.ambari.server.ServiceNotFoundException snfe) {
            return createCommandReport(0, org.apache.ambari.server.actionmanager.HostRoleStatus.COMPLETED, "{}", "", java.lang.String.format("%s was not installed in this cluster since %s is not an installed service.", task.component, task.service));
        }
        java.util.Collection<org.apache.ambari.server.state.Host> candidates = org.apache.ambari.server.stack.MasterHostResolver.getCandidateHosts(cluster, task.hosts, task.hostService, task.hostComponent);
        if (candidates.isEmpty()) {
            return createCommandReport(0, org.apache.ambari.server.actionmanager.HostRoleStatus.FAILED, "{}", "", java.lang.String.format("Unable to add a new component to the cluster as there are no hosts which contain %s's %s", task.hostService, task.hostComponent));
        }
        org.apache.ambari.server.state.ServiceComponent serviceComponent;
        try {
            serviceComponent = service.getServiceComponent(task.component);
        } catch (org.apache.ambari.server.ServiceComponentNotFoundException scnfe) {
            serviceComponent = service.addServiceComponent(task.component);
            serviceComponent.setDesiredState(org.apache.ambari.server.state.State.INSTALLED);
        }
        java.lang.StringBuilder buffer = new java.lang.StringBuilder(java.lang.String.format("Successfully added %s's %s to the cluster", task.service, task.component)).append(java.lang.System.lineSeparator());
        java.util.Map<java.lang.String, org.apache.ambari.server.state.ServiceComponentHost> existingSCHs = serviceComponent.getServiceComponentHosts();
        for (org.apache.ambari.server.state.Host host : candidates) {
            if (existingSCHs.containsKey(host.getHostName())) {
                buffer.append("  ").append(host.getHostName()).append(": ").append("Already Installed").append(java.lang.System.lineSeparator());
                continue;
            }
            org.apache.ambari.server.state.ServiceComponentHost sch = serviceComponent.addServiceComponentHost(host.getHostName());
            sch.setDesiredState(org.apache.ambari.server.state.State.INSTALLED);
            sch.setState(org.apache.ambari.server.state.State.INSTALLED);
            sch.setVersion(org.apache.ambari.server.events.listeners.upgrade.StackVersionListener.UNKNOWN_VERSION);
            buffer.append("  ").append(host.getHostName()).append(": ").append("Installed").append(java.lang.System.lineSeparator());
        }
        java.util.Set<java.lang.String> sortedHosts = candidates.stream().map(host -> host.getHostName()).collect(java.util.stream.Collectors.toCollection(() -> new java.util.TreeSet<>()));
        java.util.Map<java.lang.String, java.lang.Object> structureOutMap = new java.util.LinkedHashMap<>();
        structureOutMap.put("service", task.service);
        structureOutMap.put("component", task.component);
        structureOutMap.put("hosts", sortedHosts);
        return createCommandReport(0, org.apache.ambari.server.actionmanager.HostRoleStatus.COMPLETED, gson.toJson(structureOutMap), buffer.toString(), "");
    }
}