package org.apache.ambari.server.stack.upgrade;
import com.esotericsoftware.yamlbeans.YamlReader;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import org.apache.commons.lang.StringUtils;
@javax.xml.bind.annotation.XmlSeeAlso({ org.apache.ambari.server.stack.upgrade.ColocatedGrouping.class, org.apache.ambari.server.stack.upgrade.ClusterGrouping.class, org.apache.ambari.server.stack.upgrade.UpdateStackGrouping.class, org.apache.ambari.server.stack.upgrade.ServiceCheckGrouping.class, org.apache.ambari.server.stack.upgrade.RestartGrouping.class, org.apache.ambari.server.stack.upgrade.StartGrouping.class, org.apache.ambari.server.stack.upgrade.StopGrouping.class, org.apache.ambari.server.stack.upgrade.HostOrderGrouping.class, org.apache.ambari.server.stack.upgrade.ParallelClientGrouping.class })
public class Grouping {
    private static final java.lang.String RACKS_YAML_KEY_NAME = "racks";

    private static final java.lang.String HOSTS_YAML_KEY_NAME = "hosts";

    private static final java.lang.String HOST_GROUPS_YAML_KEY_NAME = "hostGroups";

    @javax.xml.bind.annotation.XmlAttribute(name = "name")
    public java.lang.String name;

    @javax.xml.bind.annotation.XmlAttribute(name = "title")
    public java.lang.String title;

    @javax.xml.bind.annotation.XmlElement(name = "add-after-group")
    public java.lang.String addAfterGroup;

    @javax.xml.bind.annotation.XmlElement(name = "add-after-group-entry")
    public java.lang.String addAfterGroupEntry;

    @javax.xml.bind.annotation.XmlElement(name = "skippable", defaultValue = "false")
    public boolean skippable = false;

    @javax.xml.bind.annotation.XmlElement(name = "supports-auto-skip-failure", defaultValue = "true")
    public boolean supportsAutoSkipOnFailure = true;

    @javax.xml.bind.annotation.XmlElement(name = "allow-retry", defaultValue = "true")
    public boolean allowRetry = true;

    @javax.xml.bind.annotation.XmlElement(name = "service")
    public java.util.List<org.apache.ambari.server.stack.upgrade.UpgradePack.OrderService> services = new java.util.ArrayList<>();

    @javax.xml.bind.annotation.XmlElement(name = "service-check", defaultValue = "true")
    public boolean performServiceCheck = true;

    @javax.xml.bind.annotation.XmlElement(name = "direction")
    public org.apache.ambari.server.stack.upgrade.Direction intendedDirection = null;

    @javax.xml.bind.annotation.XmlElement(name = "parallel-scheduler")
    public org.apache.ambari.server.stack.upgrade.ParallelScheduler parallelScheduler;

    @javax.xml.bind.annotation.XmlElement(name = "scope")
    public org.apache.ambari.server.stack.upgrade.UpgradeScope scope = org.apache.ambari.server.stack.upgrade.UpgradeScope.ANY;

    @javax.xml.bind.annotation.XmlElement(name = "condition")
    public org.apache.ambari.server.stack.upgrade.Condition condition;

    public final boolean isProcessingGroup() {
        return serviceCheckAfterProcessing();
    }

    protected boolean serviceCheckAfterProcessing() {
        return true;
    }

    public org.apache.ambari.server.stack.upgrade.orchestrate.StageWrapperBuilder getBuilder() {
        return new org.apache.ambari.server.stack.upgrade.Grouping.DefaultBuilder(this, performServiceCheck);
    }

    private static class DefaultBuilder extends org.apache.ambari.server.stack.upgrade.orchestrate.StageWrapperBuilder {
        private java.util.List<org.apache.ambari.server.stack.upgrade.orchestrate.StageWrapper> m_stages = new java.util.ArrayList<>();

        private java.util.Set<java.lang.String> m_servicesToCheck = new java.util.HashSet<>();

        private boolean m_serviceCheck = true;

        private DefaultBuilder(org.apache.ambari.server.stack.upgrade.Grouping grouping, boolean serviceCheck) {
            super(grouping);
            m_serviceCheck = serviceCheck;
        }

        @java.lang.Override
        public void add(org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeContext context, org.apache.ambari.server.stack.HostsType hostsType, java.lang.String service, boolean clientOnly, org.apache.ambari.server.stack.upgrade.UpgradePack.ProcessingComponent pc, java.util.Map<java.lang.String, java.lang.String> params) {
            java.util.List<org.apache.ambari.server.stack.upgrade.Grouping.TaskBucket> buckets = org.apache.ambari.server.stack.upgrade.Grouping.buckets(resolveTasks(context, true, pc));
            for (org.apache.ambari.server.stack.upgrade.Grouping.TaskBucket bucket : buckets) {
                java.util.List<org.apache.ambari.server.stack.upgrade.orchestrate.TaskWrapper> preTasks = org.apache.ambari.server.stack.upgrade.orchestrate.TaskWrapperBuilder.getTaskList(service, pc.name, hostsType, bucket.tasks, params);
                java.util.List<java.util.List<org.apache.ambari.server.stack.upgrade.orchestrate.TaskWrapper>> organizedTasks = organizeTaskWrappersBySyncRules(preTasks);
                for (java.util.List<org.apache.ambari.server.stack.upgrade.orchestrate.TaskWrapper> tasks : organizedTasks) {
                    addTasksToStageInBatches(tasks, "Preparing", context, service, pc, params);
                }
            }
            org.apache.ambari.server.stack.upgrade.Task t = resolveTask(context, pc);
            if (null != t) {
                org.apache.ambari.server.stack.upgrade.orchestrate.TaskWrapper tw = new org.apache.ambari.server.stack.upgrade.orchestrate.TaskWrapper(service, pc.name, hostsType.getHosts(), params, java.util.Collections.singletonList(t));
                addTasksToStageInBatches(java.util.Collections.singletonList(tw), t.getActionVerb(), context, service, pc, params);
            }
            buckets = org.apache.ambari.server.stack.upgrade.Grouping.buckets(resolveTasks(context, false, pc));
            for (org.apache.ambari.server.stack.upgrade.Grouping.TaskBucket bucket : buckets) {
                java.util.List<org.apache.ambari.server.stack.upgrade.orchestrate.TaskWrapper> postTasks = org.apache.ambari.server.stack.upgrade.orchestrate.TaskWrapperBuilder.getTaskList(service, pc.name, hostsType, bucket.tasks, params);
                java.util.List<java.util.List<org.apache.ambari.server.stack.upgrade.orchestrate.TaskWrapper>> organizedTasks = organizeTaskWrappersBySyncRules(postTasks);
                for (java.util.List<org.apache.ambari.server.stack.upgrade.orchestrate.TaskWrapper> tasks : organizedTasks) {
                    addTasksToStageInBatches(tasks, "Completing", context, service, pc, params);
                }
            }
            if (m_serviceCheck && (!clientOnly)) {
                m_servicesToCheck.add(service);
            }
        }

        private java.util.List<java.util.List<org.apache.ambari.server.stack.upgrade.orchestrate.TaskWrapper>> organizeTaskWrappersBySyncRules(java.util.List<org.apache.ambari.server.stack.upgrade.orchestrate.TaskWrapper> tasks) {
            java.util.List<java.util.List<org.apache.ambari.server.stack.upgrade.orchestrate.TaskWrapper>> groupedTasks = new java.util.ArrayList<>();
            java.util.List<org.apache.ambari.server.stack.upgrade.orchestrate.TaskWrapper> subTasks = new java.util.ArrayList<>();
            for (org.apache.ambari.server.stack.upgrade.orchestrate.TaskWrapper tw : tasks) {
                if (tw.isAnyTaskSequential()) {
                    if (!subTasks.isEmpty()) {
                        groupedTasks.add(subTasks);
                        subTasks = new java.util.ArrayList<>();
                    }
                    groupedTasks.add(java.util.Collections.singletonList(tw));
                } else {
                    subTasks.add(tw);
                }
            }
            if (!subTasks.isEmpty()) {
                groupedTasks.add(subTasks);
            }
            return groupedTasks;
        }

        private void addTasksToStageInBatches(java.util.List<org.apache.ambari.server.stack.upgrade.orchestrate.TaskWrapper> tasks, java.lang.String verb, org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeContext ctx, java.lang.String service, org.apache.ambari.server.stack.upgrade.UpgradePack.ProcessingComponent pc, java.util.Map<java.lang.String, java.lang.String> params) {
            if ((((tasks == null) || tasks.isEmpty()) || (tasks.get(0).getTasks() == null)) || tasks.get(0).getTasks().isEmpty()) {
                return;
            }
            org.apache.ambari.server.stack.upgrade.orchestrate.StageWrapper.Type type = tasks.get(0).getTasks().get(0).getStageWrapperType();
            for (org.apache.ambari.server.stack.upgrade.orchestrate.TaskWrapper tw : tasks) {
                java.util.List<java.util.Set<java.lang.String>> hostSets = null;
                int parallel = getParallelHostCount(ctx, 1);
                final java.lang.String rackYamlFile = ctx.getResolver().getValueFromDesiredConfigurations(org.apache.ambari.server.state.ConfigHelper.CLUSTER_ENV, "rack_yaml_file_path");
                if (org.apache.commons.lang.StringUtils.isNotEmpty(rackYamlFile)) {
                    java.util.Map<java.lang.String, java.util.Set<java.lang.String>> hostsByRack = organizeHostsByRack(tw.getHosts(), rackYamlFile);
                    java.util.List<java.util.Set<java.lang.String>> hostSetsForRack;
                    for (java.lang.String rack : hostsByRack.keySet()) {
                        hostSetsForRack = org.apache.ambari.server.utils.SetUtils.split(hostsByRack.get(rack), parallel);
                        if (hostSets == null) {
                            hostSets = hostSetsForRack;
                        } else {
                            hostSets.addAll(hostSetsForRack);
                        }
                    }
                } else {
                    hostSets = org.apache.ambari.server.utils.SetUtils.split(tw.getHosts(), parallel);
                }
                int numBatchesNeeded = hostSets.size();
                int batchNum = 0;
                for (java.util.Set<java.lang.String> hostSubset : hostSets) {
                    batchNum++;
                    java.lang.String stageText = getStageText(verb, ctx.getComponentDisplay(service, pc.name), hostSubset, batchNum, numBatchesNeeded);
                    org.apache.ambari.server.stack.upgrade.orchestrate.StageWrapper stage = new org.apache.ambari.server.stack.upgrade.orchestrate.StageWrapper(type, stageText, params, new org.apache.ambari.server.stack.upgrade.orchestrate.TaskWrapper(service, pc.name, hostSubset, params, tw.getTasks()));
                    m_stages.add(stage);
                }
            }
        }

        private java.util.Map<java.lang.String, java.util.Set<java.lang.String>> organizeHostsByRack(java.util.Set<java.lang.String> hosts, java.lang.String rackYamlFile) {
            try {
                java.util.Map<java.lang.String, java.lang.String> hostToRackMap = org.apache.ambari.server.stack.upgrade.Grouping.DefaultBuilder.getHostToRackMap(rackYamlFile);
                java.util.Map<java.lang.String, java.util.Set<java.lang.String>> rackToHostsMap = new java.util.HashMap<>();
                for (java.lang.String host : hosts) {
                    if (hostToRackMap.containsKey(host)) {
                        java.lang.String rack = hostToRackMap.get(host);
                        if (!rackToHostsMap.containsKey(rack)) {
                            rackToHostsMap.put(rack, new java.util.HashSet<>());
                        }
                        rackToHostsMap.get(rack).add(host);
                    } else {
                        throw new java.lang.RuntimeException(java.lang.String.format("Rack mapping is not present for host name: %s", host));
                    }
                }
                return rackToHostsMap;
            } catch (java.lang.Exception e) {
                throw new java.lang.RuntimeException(java.lang.String.format("Failed to generate Rack to Hosts mapping. filePath: %s", rackYamlFile), e);
            }
        }

        private static java.util.Map<java.lang.String, java.lang.String> getHostToRackMap(java.lang.String rackYamlFile) throws java.io.IOException {
            com.esotericsoftware.yamlbeans.YamlReader yamlReader = new com.esotericsoftware.yamlbeans.YamlReader(new java.io.FileReader(rackYamlFile));
            java.util.Map rackHostsMap;
            try {
                rackHostsMap = ((java.util.Map) (yamlReader.read()));
            } finally {
                yamlReader.close();
            }
            java.util.Map racks = ((java.util.Map) (rackHostsMap.get(org.apache.ambari.server.stack.upgrade.Grouping.RACKS_YAML_KEY_NAME)));
            java.util.Map<java.lang.String, java.lang.String> hostToRackMap = new java.util.HashMap<>();
            for (java.util.Map.Entry entry : ((java.util.Set<java.util.Map.Entry>) (racks.entrySet()))) {
                java.util.Map rackInfoMap = ((java.util.Map) (entry.getValue()));
                java.lang.String rackName = ((java.lang.String) (entry.getKey()));
                if (rackInfoMap.containsKey(org.apache.ambari.server.stack.upgrade.Grouping.HOSTS_YAML_KEY_NAME)) {
                    java.util.List<java.lang.String> hostList = ((java.util.List<java.lang.String>) (rackInfoMap.get(org.apache.ambari.server.stack.upgrade.Grouping.HOSTS_YAML_KEY_NAME)));
                    for (java.lang.String host : hostList) {
                        hostToRackMap.put(host, rackName);
                    }
                }
                if (rackInfoMap.containsKey(org.apache.ambari.server.stack.upgrade.Grouping.HOST_GROUPS_YAML_KEY_NAME)) {
                    java.util.List<java.util.Map> hostGroups = ((java.util.List<java.util.Map>) (rackInfoMap.get(org.apache.ambari.server.stack.upgrade.Grouping.HOST_GROUPS_YAML_KEY_NAME)));
                    for (java.util.Map hostGroup : hostGroups) {
                        if (hostGroup.containsKey(org.apache.ambari.server.stack.upgrade.Grouping.HOSTS_YAML_KEY_NAME)) {
                            java.util.List<java.lang.String> hostList = ((java.util.List<java.lang.String>) (hostGroup.get(org.apache.ambari.server.stack.upgrade.Grouping.HOSTS_YAML_KEY_NAME)));
                            for (java.lang.String host : hostList) {
                                hostToRackMap.put(host, rackName);
                            }
                        }
                    }
                }
            }
            return hostToRackMap;
        }

        @java.lang.Override
        public java.util.List<org.apache.ambari.server.stack.upgrade.orchestrate.StageWrapper> build(org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeContext upgradeContext, java.util.List<org.apache.ambari.server.stack.upgrade.orchestrate.StageWrapper> stageWrappers) {
            if (!stageWrappers.isEmpty()) {
                m_stages.addAll(0, stageWrappers);
            }
            java.util.List<org.apache.ambari.server.stack.upgrade.orchestrate.TaskWrapper> tasks = new java.util.ArrayList<>();
            java.util.List<java.lang.String> displays = new java.util.ArrayList<>();
            for (java.lang.String service : m_servicesToCheck) {
                tasks.add(new org.apache.ambari.server.stack.upgrade.orchestrate.TaskWrapper(service, "", java.util.Collections.emptySet(), new org.apache.ambari.server.stack.upgrade.ServiceCheckTask()));
                displays.add(upgradeContext.getServiceDisplay(service));
            }
            if ((upgradeContext.getDirection().isUpgrade() && m_serviceCheck) && (m_servicesToCheck.size() > 0)) {
                org.apache.ambari.server.stack.upgrade.orchestrate.StageWrapper wrapper = new org.apache.ambari.server.stack.upgrade.orchestrate.StageWrapper(org.apache.ambari.server.stack.upgrade.orchestrate.StageWrapper.Type.SERVICE_CHECK, "Service Check " + org.apache.commons.lang.StringUtils.join(displays, ", "), tasks.toArray(new org.apache.ambari.server.stack.upgrade.orchestrate.TaskWrapper[0]));
                m_stages.add(wrapper);
            }
            return m_stages;
        }
    }

    private static java.util.List<org.apache.ambari.server.stack.upgrade.Grouping.TaskBucket> buckets(java.util.List<org.apache.ambari.server.stack.upgrade.Task> tasks) {
        if ((null == tasks) || tasks.isEmpty()) {
            return java.util.Collections.emptyList();
        }
        java.util.List<org.apache.ambari.server.stack.upgrade.Grouping.TaskBucket> holders = new java.util.ArrayList<>();
        org.apache.ambari.server.stack.upgrade.Grouping.TaskBucket current = null;
        int i = 0;
        for (org.apache.ambari.server.stack.upgrade.Task t : tasks) {
            if (i == 0) {
                current = new org.apache.ambari.server.stack.upgrade.Grouping.TaskBucket(t);
                holders.add(current);
            } else if ((i > 0) && (t.getType() != tasks.get(i - 1).getType())) {
                current = new org.apache.ambari.server.stack.upgrade.Grouping.TaskBucket(t);
                holders.add(current);
            } else {
                current.tasks.add(t);
            }
            i++;
        }
        return holders;
    }

    private static class TaskBucket {
        private org.apache.ambari.server.stack.upgrade.orchestrate.StageWrapper.Type type;

        private java.util.List<org.apache.ambari.server.stack.upgrade.Task> tasks = new java.util.ArrayList<>();

        private TaskBucket(org.apache.ambari.server.stack.upgrade.Task initial) {
            switch (initial.getType()) {
                case CONFIGURE :
                case CREATE_AND_CONFIGURE :
                case SERVER_ACTION :
                case MANUAL :
                case ADD_COMPONENT :
                    type = org.apache.ambari.server.stack.upgrade.orchestrate.StageWrapper.Type.SERVER_SIDE_ACTION;
                    break;
                case EXECUTE :
                    type = org.apache.ambari.server.stack.upgrade.orchestrate.StageWrapper.Type.UPGRADE_TASKS;
                    break;
                case CONFIGURE_FUNCTION :
                    type = org.apache.ambari.server.stack.upgrade.orchestrate.StageWrapper.Type.CONFIGURE;
                    break;
                case RESTART :
                    type = org.apache.ambari.server.stack.upgrade.orchestrate.StageWrapper.Type.RESTART;
                    break;
                case START :
                    type = org.apache.ambari.server.stack.upgrade.orchestrate.StageWrapper.Type.START;
                    break;
                case STOP :
                    type = org.apache.ambari.server.stack.upgrade.orchestrate.StageWrapper.Type.STOP;
                    break;
                case SERVICE_CHECK :
                    type = org.apache.ambari.server.stack.upgrade.orchestrate.StageWrapper.Type.SERVICE_CHECK;
                    break;
                case REGENERATE_KEYTABS :
                    type = org.apache.ambari.server.stack.upgrade.orchestrate.StageWrapper.Type.REGENERATE_KEYTABS;
                    break;
            }
            tasks.add(initial);
        }
    }

    public void merge(java.util.Iterator<org.apache.ambari.server.stack.upgrade.Grouping> iterator) throws org.apache.ambari.server.AmbariException {
        java.util.Map<java.lang.String, java.util.List<org.apache.ambari.server.stack.upgrade.UpgradePack.OrderService>> skippedServices = new java.util.HashMap<>();
        while (iterator.hasNext()) {
            org.apache.ambari.server.stack.upgrade.Grouping group = iterator.next();
            boolean added = addGroupingServices(group.services, group.addAfterGroupEntry);
            if (added) {
                addSkippedServices(skippedServices, group.services);
            } else if (skippedServices.containsKey(group.addAfterGroupEntry)) {
                java.util.List<org.apache.ambari.server.stack.upgrade.UpgradePack.OrderService> tmp = skippedServices.get(group.addAfterGroupEntry);
                tmp.addAll(group.services);
            } else {
                skippedServices.put(group.addAfterGroupEntry, group.services);
            }
        } 
    }

    private boolean addGroupingServices(java.util.List<org.apache.ambari.server.stack.upgrade.UpgradePack.OrderService> servicesToAdd, java.lang.String after) {
        if (after == null) {
            services.addAll(servicesToAdd);
            return true;
        } else {
            for (int index = services.size() - 1; index >= 0; index--) {
                org.apache.ambari.server.stack.upgrade.UpgradePack.OrderService service = services.get(index);
                if (service.serviceName.equals(after)) {
                    services.addAll(index + 1, servicesToAdd);
                    return true;
                }
            }
        }
        return false;
    }

    private void addSkippedServices(java.util.Map<java.lang.String, java.util.List<org.apache.ambari.server.stack.upgrade.UpgradePack.OrderService>> skippedServices, java.util.List<org.apache.ambari.server.stack.upgrade.UpgradePack.OrderService> servicesJustAdded) {
        for (org.apache.ambari.server.stack.upgrade.UpgradePack.OrderService service : servicesJustAdded) {
            if (skippedServices.containsKey(service.serviceName)) {
                java.util.List<org.apache.ambari.server.stack.upgrade.UpgradePack.OrderService> servicesToAdd = skippedServices.remove(service.serviceName);
                addGroupingServices(servicesToAdd, service.serviceName);
                addSkippedServices(skippedServices, servicesToAdd);
            }
        }
    }

    @java.lang.Override
    public java.lang.String toString() {
        return com.google.common.base.MoreObjects.toStringHelper(this).add("name", name).toString();
    }
}