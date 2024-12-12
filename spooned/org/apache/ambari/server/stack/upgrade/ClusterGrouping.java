package org.apache.ambari.server.stack.upgrade;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import org.apache.commons.lang.StringUtils;
@javax.xml.bind.annotation.XmlRootElement
@javax.xml.bind.annotation.XmlAccessorType(javax.xml.bind.annotation.XmlAccessType.FIELD)
@javax.xml.bind.annotation.XmlType(name = "cluster")
public class ClusterGrouping extends org.apache.ambari.server.stack.upgrade.Grouping {
    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.stack.upgrade.ClusterGrouping.class);

    @javax.xml.bind.annotation.XmlElement(name = "execute-stage")
    public java.util.List<org.apache.ambari.server.stack.upgrade.ExecuteStage> executionStages;

    @java.lang.Override
    public org.apache.ambari.server.stack.upgrade.ClusterGrouping.ClusterBuilder getBuilder() {
        return new org.apache.ambari.server.stack.upgrade.ClusterGrouping.ClusterBuilder(this);
    }

    @java.lang.Override
    protected boolean serviceCheckAfterProcessing() {
        return false;
    }

    public class ClusterBuilder extends org.apache.ambari.server.stack.upgrade.orchestrate.StageWrapperBuilder {
        private ClusterBuilder(org.apache.ambari.server.stack.upgrade.Grouping grouping) {
            super(grouping);
        }

        @java.lang.Override
        public void add(org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeContext ctx, org.apache.ambari.server.stack.HostsType hostsType, java.lang.String service, boolean clientOnly, org.apache.ambari.server.stack.upgrade.UpgradePack.ProcessingComponent pc, java.util.Map<java.lang.String, java.lang.String> params) {
        }

        @java.lang.Override
        public java.util.List<org.apache.ambari.server.stack.upgrade.orchestrate.StageWrapper> build(org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeContext upgradeContext, java.util.List<org.apache.ambari.server.stack.upgrade.orchestrate.StageWrapper> stageWrappers) {
            if (null == executionStages) {
                return stageWrappers;
            }
            java.util.List<org.apache.ambari.server.stack.upgrade.orchestrate.StageWrapper> results = new java.util.ArrayList<>(stageWrappers);
            for (org.apache.ambari.server.stack.upgrade.ExecuteStage execution : executionStages) {
                if ((null != execution.intendedDirection) && (execution.intendedDirection != upgradeContext.getDirection())) {
                    continue;
                }
                if ((null != execution.condition) && (!execution.condition.isSatisfied(upgradeContext))) {
                    org.apache.ambari.server.stack.upgrade.ClusterGrouping.LOG.info("Skipping {} while building upgrade orchestration due to {}", execution, execution.condition);
                    continue;
                }
                if (org.apache.commons.lang.StringUtils.isNotBlank(execution.service)) {
                    if (!upgradeContext.isServiceSupported(execution.service)) {
                        continue;
                    }
                }
                if ((null != execution.task.condition) && (!execution.task.condition.isSatisfied(upgradeContext))) {
                    org.apache.ambari.server.stack.upgrade.ClusterGrouping.LOG.info("Skipping {} while building upgrade orchestration due to {}", execution, execution.task.condition);
                    continue;
                }
                org.apache.ambari.server.stack.upgrade.Task task = execution.task;
                org.apache.ambari.server.stack.upgrade.orchestrate.StageWrapper wrapper = null;
                switch (task.getType()) {
                    case MANUAL :
                    case SERVER_ACTION :
                    case CONFIGURE :
                    case ADD_COMPONENT :
                        wrapper = getServerActionStageWrapper(upgradeContext, execution);
                        break;
                    case EXECUTE :
                        wrapper = getExecuteStageWrapper(upgradeContext, execution);
                        break;
                    case REGENERATE_KEYTABS :
                        wrapper = getRegenerateKeytabsWrapper(upgradeContext, execution);
                        break;
                    default :
                        break;
                }
                if (null != wrapper) {
                    results.add(wrapper);
                }
            }
            return results;
        }
    }

    private org.apache.ambari.server.stack.upgrade.orchestrate.StageWrapper getServerActionStageWrapper(org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeContext ctx, org.apache.ambari.server.stack.upgrade.ExecuteStage execution) {
        java.lang.String service = execution.service;
        java.lang.String component = execution.component;
        org.apache.ambari.server.stack.upgrade.Task task = execution.task;
        java.util.Set<java.lang.String> realHosts = java.util.Collections.emptySet();
        if (org.apache.commons.lang.StringUtils.isNotEmpty(service) && org.apache.commons.lang.StringUtils.isNotEmpty(component)) {
            org.apache.ambari.server.stack.HostsType hosts = ctx.getResolver().getMasterAndHosts(service, component);
            if ((null == hosts) || hosts.getHosts().isEmpty()) {
                return null;
            } else {
                realHosts = new java.util.LinkedHashSet<>(hosts.getHosts());
            }
        }
        if (org.apache.ambari.server.stack.upgrade.Task.Type.MANUAL == task.getType()) {
            return new org.apache.ambari.server.stack.upgrade.orchestrate.StageWrapper(org.apache.ambari.server.stack.upgrade.orchestrate.StageWrapper.Type.SERVER_SIDE_ACTION, execution.title, new org.apache.ambari.server.stack.upgrade.orchestrate.TaskWrapper(service, component, realHosts, task));
        } else {
            return new org.apache.ambari.server.stack.upgrade.orchestrate.StageWrapper(org.apache.ambari.server.stack.upgrade.orchestrate.StageWrapper.Type.SERVER_SIDE_ACTION, execution.title, new org.apache.ambari.server.stack.upgrade.orchestrate.TaskWrapper(null, null, java.util.Collections.emptySet(), task));
        }
    }

    private org.apache.ambari.server.stack.upgrade.orchestrate.StageWrapper getRegenerateKeytabsWrapper(org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeContext ctx, org.apache.ambari.server.stack.upgrade.ExecuteStage execution) {
        org.apache.ambari.server.stack.upgrade.Task task = execution.task;
        org.apache.ambari.server.stack.HostsType hosts = org.apache.ambari.server.stack.HostsType.healthy(ctx.getCluster());
        return new org.apache.ambari.server.stack.upgrade.orchestrate.StageWrapper(org.apache.ambari.server.stack.upgrade.orchestrate.StageWrapper.Type.REGENERATE_KEYTABS, execution.title, new org.apache.ambari.server.stack.upgrade.orchestrate.TaskWrapper(null, null, hosts.getHosts(), task));
    }

    private org.apache.ambari.server.stack.upgrade.orchestrate.StageWrapper getExecuteStageWrapper(org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeContext ctx, org.apache.ambari.server.stack.upgrade.ExecuteStage execution) {
        java.lang.String service = execution.service;
        java.lang.String component = execution.component;
        org.apache.ambari.server.stack.upgrade.ExecuteTask et = ((org.apache.ambari.server.stack.upgrade.ExecuteTask) (execution.task));
        if (org.apache.commons.lang.StringUtils.isNotBlank(service) && org.apache.commons.lang.StringUtils.isNotBlank(component)) {
            if (!ctx.isScoped(execution.scope)) {
                return null;
            }
            if (!ctx.isServiceSupported(service)) {
                return null;
            }
            org.apache.ambari.server.stack.HostsType hosts = ctx.getResolver().getMasterAndHosts(service, component);
            if (hosts != null) {
                java.util.Set<java.lang.String> realHosts = new java.util.LinkedHashSet<>(hosts.getHosts());
                if ((org.apache.ambari.server.stack.upgrade.ExecuteHostType.MASTER == et.hosts) && hosts.hasMasters()) {
                    realHosts = hosts.getMasters();
                }
                if ((org.apache.ambari.server.stack.upgrade.ExecuteHostType.ANY == et.hosts) && (!hosts.getHosts().isEmpty())) {
                    realHosts = java.util.Collections.singleton(hosts.getHosts().iterator().next());
                }
                if ((org.apache.ambari.server.stack.upgrade.ExecuteHostType.FIRST == et.hosts) && (!hosts.getHighAvailabilityHosts().isEmpty())) {
                    java.util.List<java.lang.String> sortedHosts = new java.util.ArrayList<>(hosts.getHosts());
                    java.util.Collections.sort(sortedHosts, java.lang.String.CASE_INSENSITIVE_ORDER);
                    realHosts = java.util.Collections.singleton(sortedHosts.get(0));
                }
                if (realHosts.isEmpty()) {
                    return null;
                }
                return new org.apache.ambari.server.stack.upgrade.orchestrate.StageWrapper(org.apache.ambari.server.stack.upgrade.orchestrate.StageWrapper.Type.UPGRADE_TASKS, execution.title, new org.apache.ambari.server.stack.upgrade.orchestrate.TaskWrapper(service, component, realHosts, et));
            }
        } else if ((null == service) && (null == component)) {
            org.apache.ambari.server.state.Cluster cluster = ctx.getCluster();
            java.util.Set<java.lang.String> hostNames = new java.util.HashSet<>();
            for (org.apache.ambari.server.state.Host host : ctx.getCluster().getHosts()) {
                org.apache.ambari.server.state.MaintenanceState maintenanceState = host.getMaintenanceState(cluster.getClusterId());
                if (maintenanceState == org.apache.ambari.server.state.MaintenanceState.OFF) {
                    hostNames.add(host.getHostName());
                }
            }
            return new org.apache.ambari.server.stack.upgrade.orchestrate.StageWrapper(org.apache.ambari.server.stack.upgrade.orchestrate.StageWrapper.Type.UPGRADE_TASKS, execution.title, new org.apache.ambari.server.stack.upgrade.orchestrate.TaskWrapper(service, component, hostNames, et));
        }
        return null;
    }

    @java.lang.Override
    public void merge(java.util.Iterator<org.apache.ambari.server.stack.upgrade.Grouping> iterator) throws org.apache.ambari.server.AmbariException {
        if (executionStages == null) {
            executionStages = new java.util.ArrayList<>();
        }
        java.util.Map<java.lang.String, java.util.List<org.apache.ambari.server.stack.upgrade.ExecuteStage>> skippedStages = new java.util.HashMap<>();
        while (iterator.hasNext()) {
            org.apache.ambari.server.stack.upgrade.Grouping next = iterator.next();
            if (!(next instanceof org.apache.ambari.server.stack.upgrade.ClusterGrouping)) {
                throw new org.apache.ambari.server.AmbariException(("Invalid group type " + next.getClass().getSimpleName()) + " expected cluster group");
            }
            org.apache.ambari.server.stack.upgrade.ClusterGrouping clusterGroup = ((org.apache.ambari.server.stack.upgrade.ClusterGrouping) (next));
            boolean added = addGroupingStages(clusterGroup.executionStages, clusterGroup.addAfterGroupEntry);
            if (added) {
                addSkippedStages(skippedStages, clusterGroup.executionStages);
            } else if (skippedStages.containsKey(next.addAfterGroupEntry)) {
                java.util.List<org.apache.ambari.server.stack.upgrade.ExecuteStage> tmp = skippedStages.get(clusterGroup.addAfterGroupEntry);
                tmp.addAll(clusterGroup.executionStages);
            } else {
                skippedStages.put(clusterGroup.addAfterGroupEntry, clusterGroup.executionStages);
            }
        } 
    }

    private boolean addGroupingStages(java.util.List<org.apache.ambari.server.stack.upgrade.ExecuteStage> stagesToAdd, java.lang.String after) {
        if (after == null) {
            executionStages.addAll(stagesToAdd);
            return true;
        } else {
            for (int index = executionStages.size() - 1; index >= 0; index--) {
                org.apache.ambari.server.stack.upgrade.ExecuteStage stage = executionStages.get(index);
                if (((stage.service != null) && stage.service.equals(after)) || stage.title.equals(after)) {
                    executionStages.addAll(index + 1, stagesToAdd);
                    return true;
                }
            }
        }
        return false;
    }

    private void addSkippedStages(java.util.Map<java.lang.String, java.util.List<org.apache.ambari.server.stack.upgrade.ExecuteStage>> skippedStages, java.util.List<org.apache.ambari.server.stack.upgrade.ExecuteStage> stagesJustAdded) {
        for (org.apache.ambari.server.stack.upgrade.ExecuteStage stage : stagesJustAdded) {
            if (skippedStages.containsKey(stage.service)) {
                java.util.List<org.apache.ambari.server.stack.upgrade.ExecuteStage> stagesToAdd = skippedStages.remove(stage.service);
                addGroupingStages(stagesToAdd, stage.service);
                addSkippedStages(skippedStages, stagesToAdd);
            }
        }
    }
}