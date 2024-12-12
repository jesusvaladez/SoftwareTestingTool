package org.apache.ambari.server.stack.upgrade.orchestrate;
import org.apache.commons.collections.CollectionUtils;
public class ParallelClientGroupingBuilder extends org.apache.ambari.server.stack.upgrade.orchestrate.StageWrapperBuilder {
    private java.util.Map<java.lang.String, org.apache.ambari.server.stack.upgrade.orchestrate.ParallelClientGroupingBuilder.HostHolder> serviceToHostMap = new java.util.HashMap<>();

    public ParallelClientGroupingBuilder(org.apache.ambari.server.stack.upgrade.Grouping grouping) {
        super(grouping);
    }

    @java.lang.Override
    public void add(org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeContext upgradeContext, org.apache.ambari.server.stack.HostsType hostsType, java.lang.String service, boolean clientOnly, org.apache.ambari.server.stack.upgrade.UpgradePack.ProcessingComponent pc, java.util.Map<java.lang.String, java.lang.String> params) {
        if ((null == hostsType) || org.apache.commons.collections.CollectionUtils.isEmpty(hostsType.getHosts())) {
            return;
        }
        org.apache.ambari.server.stack.upgrade.Task task = resolveTask(upgradeContext, pc);
        if (null == task) {
            return;
        }
        java.util.Iterator<java.lang.String> hostIterator = hostsType.getHosts().iterator();
        org.apache.ambari.server.stack.upgrade.orchestrate.ParallelClientGroupingBuilder.HostHolder holder = new org.apache.ambari.server.stack.upgrade.orchestrate.ParallelClientGroupingBuilder.HostHolder();
        holder.m_firstHost = hostIterator.next();
        while (hostIterator.hasNext()) {
            holder.m_remainingHosts.add(hostIterator.next());
        } 
        holder.m_component = pc.name;
        holder.m_tasks = java.util.Collections.singletonList(task);
        holder.m_preTasks = resolveTasks(upgradeContext, true, pc);
        holder.m_postTasks = resolveTasks(upgradeContext, false, pc);
        serviceToHostMap.put(service, holder);
    }

    @java.lang.Override
    public java.util.List<org.apache.ambari.server.stack.upgrade.orchestrate.StageWrapper> build(org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeContext upgradeContext, java.util.List<org.apache.ambari.server.stack.upgrade.orchestrate.StageWrapper> stageWrappers) {
        if (0 == serviceToHostMap.size()) {
            return stageWrappers;
        }
        java.util.List<org.apache.ambari.server.stack.upgrade.orchestrate.StageWrapper> starterUpgrades = new java.util.ArrayList<>();
        java.util.List<org.apache.ambari.server.stack.upgrade.orchestrate.StageWrapper> finisherUpgrades = new java.util.ArrayList<>();
        serviceToHostMap.forEach((service, holder) -> {
            starterUpgrades.addAll(buildStageWrappers(upgradeContext, service, holder, holder.m_preTasks, true, "Preparing"));
            starterUpgrades.addAll(buildStageWrappers(upgradeContext, service, holder, holder.m_tasks, true, "Upgrading"));
            starterUpgrades.addAll(buildStageWrappers(upgradeContext, service, holder, holder.m_postTasks, true, "Completing"));
            org.apache.ambari.server.stack.upgrade.orchestrate.StageWrapper serviceCheck = new org.apache.ambari.server.stack.upgrade.ServiceCheckGrouping.ServiceCheckStageWrapper(service, upgradeContext.getServiceDisplay(service), false, holder.m_firstHost);
            starterUpgrades.add(serviceCheck);
            finisherUpgrades.addAll(buildStageWrappers(upgradeContext, service, holder, holder.m_preTasks, false, "Prepare Remaining"));
            finisherUpgrades.addAll(buildStageWrappers(upgradeContext, service, holder, holder.m_tasks, false, "Upgrade Remaining"));
            finisherUpgrades.addAll(buildStageWrappers(upgradeContext, service, holder, holder.m_postTasks, false, "Complete Remaining"));
        });
        java.util.List<org.apache.ambari.server.stack.upgrade.orchestrate.StageWrapper> results = new java.util.ArrayList<>(stageWrappers);
        results.addAll(starterUpgrades);
        results.addAll(finisherUpgrades);
        return results;
    }

    private java.util.List<org.apache.ambari.server.stack.upgrade.orchestrate.StageWrapper> buildStageWrappers(org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeContext upgradeContext, java.lang.String service, org.apache.ambari.server.stack.upgrade.orchestrate.ParallelClientGroupingBuilder.HostHolder holder, java.util.List<org.apache.ambari.server.stack.upgrade.Task> tasks, boolean firstHost, java.lang.String prefix) {
        if (org.apache.commons.collections.CollectionUtils.isEmpty(tasks)) {
            return java.util.Collections.emptyList();
        }
        java.util.Set<java.lang.String> hosts = (firstHost) ? java.util.Collections.singleton(holder.m_firstHost) : holder.m_remainingHosts;
        java.lang.String component = holder.m_component;
        java.lang.String componentDisplay = upgradeContext.getComponentDisplay(service, component);
        java.lang.String text = getStageText(prefix, componentDisplay, hosts);
        java.util.List<org.apache.ambari.server.stack.upgrade.orchestrate.TaskWrapper> wrappers = buildTaskWrappers(service, component, tasks, hosts, firstHost);
        java.util.List<org.apache.ambari.server.stack.upgrade.orchestrate.StageWrapper> results = new java.util.ArrayList<>();
        wrappers.forEach(task -> {
            org.apache.ambari.server.stack.upgrade.orchestrate.StageWrapper.Type type = task.getTasks().get(0).getStageWrapperType();
            org.apache.ambari.server.stack.upgrade.orchestrate.StageWrapper stage = new org.apache.ambari.server.stack.upgrade.orchestrate.StageWrapper(type, text, new java.util.HashMap<>(), java.util.Collections.singletonList(task));
            results.add(stage);
        });
        return results;
    }

    private java.util.List<org.apache.ambari.server.stack.upgrade.orchestrate.TaskWrapper> buildTaskWrappers(java.lang.String service, java.lang.String component, java.util.List<org.apache.ambari.server.stack.upgrade.Task> tasks, java.util.Set<java.lang.String> hosts, boolean firstHost) {
        java.util.List<org.apache.ambari.server.stack.upgrade.orchestrate.TaskWrapper> results = new java.util.ArrayList<>();
        tasks.forEach(task -> {
            if (task.getType().isServerAction()) {
                if (firstHost) {
                    java.lang.String ambariServerHostname = org.apache.ambari.server.utils.StageUtils.getHostName();
                    results.add(new org.apache.ambari.server.stack.upgrade.orchestrate.TaskWrapper(service, component, java.util.Collections.singleton(ambariServerHostname), task));
                }
                return;
            }
            if ((!firstHost) && (task.getType() == org.apache.ambari.server.stack.upgrade.Task.Type.EXECUTE)) {
                org.apache.ambari.server.stack.upgrade.ExecuteTask et = ((org.apache.ambari.server.stack.upgrade.ExecuteTask) (task));
                if (et.hosts != org.apache.ambari.server.stack.upgrade.ExecuteHostType.ALL) {
                    return;
                }
            }
            results.add(new org.apache.ambari.server.stack.upgrade.orchestrate.TaskWrapper(service, component, hosts, task));
        });
        return results;
    }

    private static class HostHolder {
        private java.lang.String m_component;

        private java.lang.String m_firstHost;

        private java.util.Set<java.lang.String> m_remainingHosts = new java.util.HashSet<>();

        private java.util.List<org.apache.ambari.server.stack.upgrade.Task> m_tasks;

        private java.util.List<org.apache.ambari.server.stack.upgrade.Task> m_preTasks;

        private java.util.List<org.apache.ambari.server.stack.upgrade.Task> m_postTasks;
    }
}