package org.apache.ambari.server.stack.upgrade;
import javax.xml.bind.annotation.XmlType;
import org.apache.commons.collections.CollectionUtils;
@javax.xml.bind.annotation.XmlType(name = "host-order")
public class HostOrderGrouping extends org.apache.ambari.server.stack.upgrade.Grouping {
    private static final java.lang.String TYPE = "type";

    private static final java.lang.String HOST = "host";

    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.stack.upgrade.HostOrderGrouping.class);

    private java.util.List<org.apache.ambari.server.stack.upgrade.HostOrderItem> m_hostOrderItems;

    public HostOrderGrouping() {
    }

    public void setHostOrderItems(java.util.List<org.apache.ambari.server.stack.upgrade.HostOrderItem> hostOrderItems) {
        m_hostOrderItems = hostOrderItems;
    }

    @java.lang.Override
    public org.apache.ambari.server.stack.upgrade.orchestrate.StageWrapperBuilder getBuilder() {
        return new org.apache.ambari.server.stack.upgrade.HostOrderGrouping.HostBuilder(this);
    }

    private static class HostBuilder extends org.apache.ambari.server.stack.upgrade.orchestrate.StageWrapperBuilder {
        private final java.util.List<org.apache.ambari.server.stack.upgrade.HostOrderItem> hostOrderItems;

        protected HostBuilder(org.apache.ambari.server.stack.upgrade.HostOrderGrouping grouping) {
            super(grouping);
            hostOrderItems = grouping.m_hostOrderItems;
        }

        @java.lang.Override
        public void add(org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeContext upgradeContext, org.apache.ambari.server.stack.HostsType hostsType, java.lang.String service, boolean clientOnly, org.apache.ambari.server.stack.upgrade.UpgradePack.ProcessingComponent pc, java.util.Map<java.lang.String, java.lang.String> params) {
        }

        @java.lang.Override
        public java.util.List<org.apache.ambari.server.stack.upgrade.orchestrate.StageWrapper> build(org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeContext upgradeContext, java.util.List<org.apache.ambari.server.stack.upgrade.orchestrate.StageWrapper> stageWrappers) {
            java.util.List<org.apache.ambari.server.stack.upgrade.orchestrate.StageWrapper> wrappers = new java.util.ArrayList<>(stageWrappers);
            for (org.apache.ambari.server.stack.upgrade.HostOrderItem orderItem : hostOrderItems) {
                switch (orderItem.getType()) {
                    case HOST_UPGRADE :
                        wrappers.addAll(buildHosts(upgradeContext, orderItem.getActionItems()));
                        break;
                    case SERVICE_CHECK :
                        wrappers.addAll(buildServiceChecks(upgradeContext, orderItem.getActionItems()));
                        break;
                }
            }
            return wrappers;
        }

        private java.util.List<org.apache.ambari.server.stack.upgrade.orchestrate.StageWrapper> buildHosts(org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeContext upgradeContext, java.util.List<java.lang.String> hosts) {
            if (org.apache.commons.collections.CollectionUtils.isEmpty(hosts)) {
                return java.util.Collections.emptyList();
            }
            org.apache.ambari.server.state.Cluster cluster = upgradeContext.getCluster();
            java.util.List<org.apache.ambari.server.stack.upgrade.orchestrate.StageWrapper> wrappers = new java.util.ArrayList<>();
            org.apache.ambari.server.actionmanager.HostRoleCommandFactory hrcFactory = upgradeContext.getHostRoleCommandFactory();
            org.apache.ambari.server.metadata.RoleCommandOrder roleCommandOrder = getRoleCommandOrderForUpgrade(cluster);
            for (java.lang.String hostName : hosts) {
                java.util.List<org.apache.ambari.server.stack.upgrade.orchestrate.TaskWrapper> stopTasks = new java.util.ArrayList<>();
                java.util.Map<java.lang.String, java.util.Map<java.lang.String, org.apache.ambari.server.actionmanager.HostRoleCommand>> restartCommandsForHost = new java.util.HashMap<>();
                java.util.Map<java.lang.String, org.apache.ambari.server.actionmanager.HostRoleCommand> restartCommandsByRole = new java.util.HashMap<>();
                restartCommandsForHost.put(hostName, restartCommandsByRole);
                for (org.apache.ambari.server.state.ServiceComponentHost sch : cluster.getServiceComponentHosts(hostName)) {
                    if (!isVersionAdvertised(upgradeContext, sch)) {
                        continue;
                    }
                    org.apache.ambari.server.stack.HostsType hostsType = upgradeContext.getResolver().getMasterAndHosts(sch.getServiceName(), sch.getServiceComponentName());
                    if ((null != hostsType) && (!hostsType.getHosts().contains(hostName))) {
                        org.apache.ambari.server.orm.entities.RepositoryVersionEntity targetRepositoryVersion = upgradeContext.getTargetRepositoryVersion(sch.getServiceName());
                        org.apache.ambari.server.stack.upgrade.HostOrderGrouping.LOG.warn("Host {} could not be orchestrated. Either there are no components for {}/{} " + "or the target version {} is already current.", hostName, sch.getServiceName(), sch.getServiceComponentName(), targetRepositoryVersion.getVersion());
                        continue;
                    }
                    if (!sch.isClientComponent()) {
                        stopTasks.add(new org.apache.ambari.server.stack.upgrade.orchestrate.TaskWrapper(sch.getServiceName(), sch.getServiceComponentName(), java.util.Collections.singleton(hostName), new org.apache.ambari.server.stack.upgrade.StopTask()));
                    }
                    org.apache.ambari.server.Role role = org.apache.ambari.server.Role.valueOf(sch.getServiceComponentName());
                    org.apache.ambari.server.actionmanager.HostRoleCommand hostRoleCommand = hrcFactory.create(hostName, role, null, org.apache.ambari.server.RoleCommand.START);
                    restartCommandsByRole.put(role.name(), hostRoleCommand);
                }
                if (stopTasks.isEmpty() && restartCommandsByRole.isEmpty()) {
                    org.apache.ambari.server.stack.upgrade.HostOrderGrouping.LOG.info("There were no {} commands generated for {}", upgradeContext.getDirection().getText(false), hostName);
                    continue;
                }
                org.apache.ambari.server.stageplanner.RoleGraphFactory roleGraphFactory = upgradeContext.getRoleGraphFactory();
                org.apache.ambari.server.stageplanner.RoleGraph roleGraph = roleGraphFactory.createNew(roleCommandOrder);
                java.util.List<java.util.Map<java.lang.String, java.util.List<org.apache.ambari.server.actionmanager.HostRoleCommand>>> stages = roleGraph.getOrderedHostRoleCommands(restartCommandsForHost);
                java.util.List<org.apache.ambari.server.stack.upgrade.orchestrate.StageWrapper> stageWrappers = new java.util.ArrayList<>();
                int phaseCounter = 1;
                for (java.util.Map<java.lang.String, java.util.List<org.apache.ambari.server.actionmanager.HostRoleCommand>> stage : stages) {
                    java.util.List<org.apache.ambari.server.actionmanager.HostRoleCommand> stageCommandsForHost = stage.get(hostName);
                    java.lang.String stageTitle = java.lang.String.format("Starting components on %s (phase %d)", hostName, phaseCounter++);
                    java.util.List<org.apache.ambari.server.stack.upgrade.orchestrate.TaskWrapper> taskWrappers = new java.util.ArrayList<>();
                    for (org.apache.ambari.server.actionmanager.HostRoleCommand command : stageCommandsForHost) {
                        org.apache.ambari.server.state.StackId stackId = upgradeContext.getRepositoryVersion().getStackId();
                        java.lang.String componentName = command.getRole().name();
                        java.lang.String serviceName = null;
                        try {
                            org.apache.ambari.server.api.services.AmbariMetaInfo ambariMetaInfo = upgradeContext.getAmbariMetaInfo();
                            serviceName = ambariMetaInfo.getComponentToService(stackId.getStackName(), stackId.getStackVersion(), componentName);
                        } catch (org.apache.ambari.server.AmbariException ambariException) {
                            org.apache.ambari.server.stack.upgrade.HostOrderGrouping.LOG.error("Unable to lookup service by component {} for stack {}-{}", componentName, stackId.getStackName(), stackId.getStackVersion());
                        }
                        org.apache.ambari.server.stack.upgrade.orchestrate.TaskWrapper taskWrapper = new org.apache.ambari.server.stack.upgrade.orchestrate.TaskWrapper(serviceName, componentName, java.util.Collections.singleton(hostName), new org.apache.ambari.server.stack.upgrade.RestartTask());
                        taskWrappers.add(taskWrapper);
                    }
                    if (!taskWrappers.isEmpty()) {
                        org.apache.ambari.server.stack.upgrade.orchestrate.StageWrapper startWrapper = new org.apache.ambari.server.stack.upgrade.orchestrate.StageWrapper(org.apache.ambari.server.stack.upgrade.orchestrate.StageWrapper.Type.RESTART, stageTitle, taskWrappers.toArray(new org.apache.ambari.server.stack.upgrade.orchestrate.TaskWrapper[taskWrappers.size()]));
                        stageWrappers.add(startWrapper);
                    }
                }
                org.apache.ambari.server.stack.upgrade.ManualTask mt = new org.apache.ambari.server.stack.upgrade.ManualTask();
                java.lang.String message = java.lang.String.format("Please acknowledge that host %s has been prepared.", hostName);
                mt.messages.add(message);
                com.google.gson.JsonObject structuredOut = new com.google.gson.JsonObject();
                structuredOut.addProperty(org.apache.ambari.server.stack.upgrade.HostOrderGrouping.TYPE, org.apache.ambari.server.stack.upgrade.HostOrderItem.HostOrderActionType.HOST_UPGRADE.toString());
                structuredOut.addProperty(org.apache.ambari.server.stack.upgrade.HostOrderGrouping.HOST, hostName);
                mt.structuredOut = structuredOut.toString();
                if (!stopTasks.isEmpty()) {
                    org.apache.ambari.server.stack.upgrade.orchestrate.StageWrapper stopWrapper = new org.apache.ambari.server.stack.upgrade.orchestrate.StageWrapper(org.apache.ambari.server.stack.upgrade.orchestrate.StageWrapper.Type.STOP, java.lang.String.format("Stop on %s", hostName), stopTasks.toArray(new org.apache.ambari.server.stack.upgrade.orchestrate.TaskWrapper[stopTasks.size()]));
                    wrappers.add(stopWrapper);
                }
                org.apache.ambari.server.stack.upgrade.orchestrate.StageWrapper manualWrapper = new org.apache.ambari.server.stack.upgrade.orchestrate.StageWrapper(org.apache.ambari.server.stack.upgrade.orchestrate.StageWrapper.Type.SERVER_SIDE_ACTION, "Manual Confirmation", new org.apache.ambari.server.stack.upgrade.orchestrate.TaskWrapper(null, null, java.util.Collections.emptySet(), mt));
                wrappers.add(manualWrapper);
                wrappers.addAll(stageWrappers);
            }
            return wrappers;
        }

        private java.util.List<org.apache.ambari.server.stack.upgrade.orchestrate.StageWrapper> buildServiceChecks(org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeContext upgradeContext, java.util.List<java.lang.String> serviceChecks) {
            if (org.apache.commons.collections.CollectionUtils.isEmpty(serviceChecks)) {
                return java.util.Collections.emptyList();
            }
            java.util.List<org.apache.ambari.server.stack.upgrade.orchestrate.StageWrapper> wrappers = new java.util.ArrayList<>();
            org.apache.ambari.server.state.Cluster cluster = upgradeContext.getCluster();
            for (java.lang.String serviceName : serviceChecks) {
                boolean hasService = false;
                try {
                    cluster.getService(serviceName);
                    hasService = true;
                } catch (java.lang.Exception e) {
                    org.apache.ambari.server.stack.upgrade.HostOrderGrouping.LOG.warn("Service {} not found to orchestrate", serviceName);
                }
                if (!hasService) {
                    continue;
                }
                org.apache.ambari.server.stack.upgrade.orchestrate.StageWrapper wrapper = new org.apache.ambari.server.stack.upgrade.orchestrate.StageWrapper(org.apache.ambari.server.stack.upgrade.orchestrate.StageWrapper.Type.SERVICE_CHECK, java.lang.String.format("Service Check %s", upgradeContext.getServiceDisplay(serviceName)), new org.apache.ambari.server.stack.upgrade.orchestrate.TaskWrapper(serviceName, "", java.util.Collections.emptySet(), new org.apache.ambari.server.stack.upgrade.ServiceCheckTask()));
                wrappers.add(wrapper);
            }
            return wrappers;
        }

        private boolean isVersionAdvertised(org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeContext upgradeContext, org.apache.ambari.server.state.ServiceComponentHost sch) {
            org.apache.ambari.server.orm.entities.RepositoryVersionEntity targetRepositoryVersion = upgradeContext.getTargetRepositoryVersion(sch.getServiceName());
            org.apache.ambari.server.state.StackId targetStack = targetRepositoryVersion.getStackId();
            try {
                org.apache.ambari.server.state.ComponentInfo component = upgradeContext.getAmbariMetaInfo().getComponent(targetStack.getStackName(), targetStack.getStackVersion(), sch.getServiceName(), sch.getServiceComponentName());
                return component.isVersionAdvertised();
            } catch (org.apache.ambari.server.AmbariException e) {
                org.apache.ambari.server.stack.upgrade.HostOrderGrouping.LOG.warn("Could not determine if {}/{}/{} could be upgraded; returning false", targetStack, sch.getServiceName(), sch.getServiceComponentName(), e);
                return false;
            }
        }

        private org.apache.ambari.server.metadata.RoleCommandOrder getRoleCommandOrderForUpgrade(org.apache.ambari.server.state.Cluster cluster) {
            org.apache.ambari.server.metadata.RoleCommandOrder roleCommandOrder = cluster.getRoleCommandOrder();
            try {
                roleCommandOrder = ((org.apache.ambari.server.metadata.RoleCommandOrder) (roleCommandOrder.clone()));
            } catch (java.lang.CloneNotSupportedException cloneNotSupportedException) {
                org.apache.ambari.server.stack.upgrade.HostOrderGrouping.LOG.warn("Unable to clone role command order and apply overrides for this upgrade", cloneNotSupportedException);
            }
            java.util.LinkedHashSet<java.lang.String> sectionKeys = roleCommandOrder.getSectionKeys();
            sectionKeys.add("host_ordered_upgrade");
            roleCommandOrder.initialize(cluster, sectionKeys);
            return roleCommandOrder;
        }
    }
}