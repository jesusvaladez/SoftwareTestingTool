package org.apache.ambari.server.stack.upgrade;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
@javax.xml.bind.annotation.XmlType(name = "colocated")
public class ColocatedGrouping extends org.apache.ambari.server.stack.upgrade.Grouping {
    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.stack.upgrade.ColocatedGrouping.class);

    @javax.xml.bind.annotation.XmlElement(name = "batch")
    public org.apache.ambari.server.stack.upgrade.Batch batch;

    @java.lang.Override
    public org.apache.ambari.server.stack.upgrade.orchestrate.StageWrapperBuilder getBuilder() {
        return new org.apache.ambari.server.stack.upgrade.ColocatedGrouping.MultiHomedBuilder(this, batch, performServiceCheck, parallelScheduler);
    }

    private static class MultiHomedBuilder extends org.apache.ambari.server.stack.upgrade.orchestrate.StageWrapperBuilder {
        private org.apache.ambari.server.stack.upgrade.Batch m_batch;

        private boolean m_serviceCheck = true;

        private java.util.List<org.apache.ambari.server.stack.upgrade.ColocatedGrouping.TaskProxy> initialBatch = new java.util.LinkedList<>();

        private java.util.List<org.apache.ambari.server.stack.upgrade.ColocatedGrouping.TaskProxy> finalBatches = new java.util.LinkedList<>();

        private MultiHomedBuilder(org.apache.ambari.server.stack.upgrade.Grouping grouping, org.apache.ambari.server.stack.upgrade.Batch batch, boolean serviceCheck, org.apache.ambari.server.stack.upgrade.ParallelScheduler parallel) {
            super(grouping);
            m_batch = batch;
            m_serviceCheck = serviceCheck;
        }

        @java.lang.Override
        public void add(org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeContext context, org.apache.ambari.server.stack.HostsType hostsType, java.lang.String service, boolean clientOnly, org.apache.ambari.server.stack.upgrade.UpgradePack.ProcessingComponent pc, java.util.Map<java.lang.String, java.lang.String> params) {
            int count = java.lang.Double.valueOf(java.lang.Math.ceil((((double) (m_batch.percent)) / 100) * hostsType.getHosts().size())).intValue();
            java.util.LinkedHashSet<java.lang.String> first = new java.util.LinkedHashSet<>();
            java.util.LinkedHashSet<java.lang.String> remaining = new java.util.LinkedHashSet<>();
            hostsType.getHosts().stream().forEach(hostName -> {
                if (first.size() < count) {
                    first.add(hostName);
                } else {
                    remaining.add(hostName);
                }
            });
            java.util.List<org.apache.ambari.server.stack.upgrade.Task> preTasks = resolveTasks(context, true, pc);
            org.apache.ambari.server.stack.upgrade.Task task = resolveTask(context, pc);
            java.util.List<org.apache.ambari.server.stack.upgrade.Task> postTasks = resolveTasks(context, false, pc);
            java.util.concurrent.atomic.AtomicBoolean processInitial = new java.util.concurrent.atomic.AtomicBoolean(true);
            int parallelCount = -1;
            org.apache.ambari.spi.upgrade.OrchestrationOptions options = context.getOrchestrationOptions();
            if (null != options) {
                parallelCount = context.getOrchestrationOptions().getConcurrencyCount(context.getCluster().buildClusterInformation(), service, pc.name);
            }
            if (parallelCount < 1) {
                parallelCount = getParallelHostCount(context, 1);
            }
            final int hostCount = parallelCount;
            java.util.stream.Stream.of(first, remaining).forEach(hosts -> {
                java.util.List<org.apache.ambari.server.stack.upgrade.ColocatedGrouping.TaskProxy> targetList = (processInitial.get()) ? initialBatch : finalBatches;
                java.util.List<java.util.Set<java.lang.String>> hostSplit = org.apache.ambari.server.utils.SetUtils.split(hosts, hostCount);
                hostSplit.forEach(hostSet -> {
                    java.util.List<org.apache.ambari.server.stack.upgrade.Task> tasks = preTasks;
                    org.apache.ambari.server.stack.upgrade.ColocatedGrouping.TaskProxy proxy;
                    if (org.apache.commons.collections.CollectionUtils.isNotEmpty(preTasks)) {
                        org.apache.ambari.server.stack.upgrade.orchestrate.StageWrapper.Type type = preTasks.get(0).getStageWrapperType();
                        proxy = new org.apache.ambari.server.stack.upgrade.ColocatedGrouping.TaskProxy();
                        proxy.clientOnly = clientOnly;
                        proxy.message = getStageText("Preparing", context.getComponentDisplay(service, pc.name), hostSet);
                        proxy.tasks.addAll(org.apache.ambari.server.stack.upgrade.orchestrate.TaskWrapperBuilder.getTaskList(service, pc.name, org.apache.ambari.server.stack.HostsType.normal(new java.util.LinkedHashSet<>(hostSet)), tasks, params));
                        proxy.service = service;
                        proxy.component = pc.name;
                        proxy.type = type;
                        targetList.add(proxy);
                    }
                    if ((null != task) && org.apache.ambari.server.stack.upgrade.RestartTask.class.isInstance(task)) {
                        proxy = new org.apache.ambari.server.stack.upgrade.ColocatedGrouping.TaskProxy();
                        proxy.clientOnly = clientOnly;
                        proxy.tasks.add(new org.apache.ambari.server.stack.upgrade.orchestrate.TaskWrapper(service, pc.name, hostSet, params, task));
                        proxy.restart = true;
                        proxy.service = service;
                        proxy.component = pc.name;
                        proxy.type = org.apache.ambari.server.stack.upgrade.orchestrate.StageWrapper.Type.RESTART;
                        proxy.message = getStageText("Restarting", context.getComponentDisplay(service, pc.name), hostSet);
                        targetList.add(proxy);
                    }
                    tasks = postTasks;
                    if (org.apache.commons.collections.CollectionUtils.isNotEmpty(preTasks)) {
                        org.apache.ambari.server.stack.upgrade.orchestrate.StageWrapper.Type type = preTasks.get(0).getStageWrapperType();
                        proxy = new org.apache.ambari.server.stack.upgrade.ColocatedGrouping.TaskProxy();
                        proxy.clientOnly = clientOnly;
                        proxy.message = getStageText("Completing", context.getComponentDisplay(service, pc.name), hostSet);
                        proxy.tasks.addAll(org.apache.ambari.server.stack.upgrade.orchestrate.TaskWrapperBuilder.getTaskList(service, pc.name, org.apache.ambari.server.stack.HostsType.normal(new java.util.LinkedHashSet<>(hostSet)), tasks, params));
                        proxy.service = service;
                        proxy.component = pc.name;
                        proxy.type = type;
                        targetList.add(proxy);
                    }
                });
                processInitial.set(false);
            });
        }

        @java.lang.Override
        public java.util.List<org.apache.ambari.server.stack.upgrade.orchestrate.StageWrapper> build(org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeContext upgradeContext, java.util.List<org.apache.ambari.server.stack.upgrade.orchestrate.StageWrapper> stageWrappers) {
            final java.util.List<org.apache.ambari.server.stack.upgrade.Task> visitedServerSideTasks = new java.util.ArrayList<>();
            com.google.common.base.Predicate<org.apache.ambari.server.stack.upgrade.Task> predicate = new com.google.common.base.Predicate<org.apache.ambari.server.stack.upgrade.Task>() {
                @java.lang.Override
                public boolean apply(org.apache.ambari.server.stack.upgrade.Task input) {
                    if (visitedServerSideTasks.contains(input)) {
                        return false;
                    }
                    if (input.getType().isServerAction()) {
                        visitedServerSideTasks.add(input);
                    }
                    return true;
                }
            };
            java.util.List<org.apache.ambari.server.stack.upgrade.orchestrate.StageWrapper> results = new java.util.ArrayList<>(stageWrappers);
            if (org.apache.ambari.server.stack.upgrade.ColocatedGrouping.LOG.isDebugEnabled()) {
                org.apache.ambari.server.stack.upgrade.ColocatedGrouping.LOG.debug("RU initial: {}", initialBatch);
                org.apache.ambari.server.stack.upgrade.ColocatedGrouping.LOG.debug("RU final: {}", finalBatches);
            }
            java.util.List<org.apache.ambari.server.stack.upgrade.orchestrate.StageWrapper> befores = fromProxies(upgradeContext.getDirection(), initialBatch, predicate);
            results.addAll(befores);
            if (!befores.isEmpty()) {
                org.apache.ambari.server.stack.upgrade.ManualTask task = new org.apache.ambari.server.stack.upgrade.ManualTask();
                task.summary = m_batch.summary;
                java.util.List<java.lang.String> messages = new java.util.ArrayList<>();
                messages.add(m_batch.message);
                task.messages = messages;
                formatFirstBatch(upgradeContext, task, befores);
                org.apache.ambari.server.stack.upgrade.orchestrate.StageWrapper wrapper = new org.apache.ambari.server.stack.upgrade.orchestrate.StageWrapper(org.apache.ambari.server.stack.upgrade.orchestrate.StageWrapper.Type.SERVER_SIDE_ACTION, "Validate Partial " + upgradeContext.getDirection().getText(true), new org.apache.ambari.server.stack.upgrade.orchestrate.TaskWrapper(null, null, java.util.Collections.emptySet(), task));
                results.add(wrapper);
            }
            results.addAll(fromProxies(upgradeContext.getDirection(), finalBatches, predicate));
            return results;
        }

        private java.util.List<org.apache.ambari.server.stack.upgrade.orchestrate.StageWrapper> fromProxies(org.apache.ambari.server.stack.upgrade.Direction direction, java.util.List<org.apache.ambari.server.stack.upgrade.ColocatedGrouping.TaskProxy> proxies, com.google.common.base.Predicate<org.apache.ambari.server.stack.upgrade.Task> predicate) {
            java.util.List<org.apache.ambari.server.stack.upgrade.orchestrate.StageWrapper> results = new java.util.ArrayList<>();
            java.util.Set<java.lang.String> serviceChecks = new java.util.HashSet<>();
            proxies.forEach(proxy -> {
                org.apache.ambari.server.stack.upgrade.orchestrate.StageWrapper wrapper = null;
                java.util.List<org.apache.ambari.server.stack.upgrade.orchestrate.StageWrapper> execwrappers = new java.util.ArrayList<>();
                if (!proxy.clientOnly) {
                    serviceChecks.add(proxy.service);
                }
                if (!proxy.restart) {
                    org.apache.ambari.server.stack.upgrade.orchestrate.TaskWrapper[] tasks = proxy.getTasksArray(predicate);
                    if (org.apache.ambari.server.stack.upgrade.ColocatedGrouping.LOG.isDebugEnabled()) {
                        for (org.apache.ambari.server.stack.upgrade.orchestrate.TaskWrapper tw : tasks) {
                            org.apache.ambari.server.stack.upgrade.ColocatedGrouping.LOG.debug("{}", tw);
                        }
                    }
                    if (org.apache.commons.lang.ArrayUtils.isNotEmpty(tasks)) {
                        wrapper = new org.apache.ambari.server.stack.upgrade.orchestrate.StageWrapper(proxy.type, proxy.message, tasks);
                    }
                } else {
                    org.apache.ambari.server.stack.upgrade.orchestrate.TaskWrapper[] tasks = proxy.getTasksArray(null);
                    if (org.apache.ambari.server.stack.upgrade.ColocatedGrouping.LOG.isDebugEnabled()) {
                        for (org.apache.ambari.server.stack.upgrade.orchestrate.TaskWrapper tw : tasks) {
                            org.apache.ambari.server.stack.upgrade.ColocatedGrouping.LOG.debug("{}", tw);
                        }
                    }
                    execwrappers.add(new org.apache.ambari.server.stack.upgrade.orchestrate.StageWrapper(org.apache.ambari.server.stack.upgrade.orchestrate.StageWrapper.Type.RESTART, proxy.message, tasks));
                }
                if (null != wrapper) {
                    results.add(wrapper);
                }
                if (execwrappers.size() > 0) {
                    results.addAll(execwrappers);
                }
            });
            if ((direction.isUpgrade() && m_serviceCheck) && (serviceChecks.size() > 0)) {
                java.util.List<org.apache.ambari.server.stack.upgrade.orchestrate.TaskWrapper> tasks = new java.util.ArrayList<>();
                java.util.Set<java.lang.String> displays = new java.util.HashSet<>();
                for (java.lang.String service : serviceChecks) {
                    tasks.add(new org.apache.ambari.server.stack.upgrade.orchestrate.TaskWrapper(service, "", java.util.Collections.emptySet(), new org.apache.ambari.server.stack.upgrade.ServiceCheckTask()));
                    displays.add(service);
                }
                org.apache.ambari.server.stack.upgrade.orchestrate.StageWrapper wrapper = new org.apache.ambari.server.stack.upgrade.orchestrate.StageWrapper(org.apache.ambari.server.stack.upgrade.orchestrate.StageWrapper.Type.SERVICE_CHECK, "Service Check " + org.apache.commons.lang.StringUtils.join(displays, ", "), tasks.toArray(new org.apache.ambari.server.stack.upgrade.orchestrate.TaskWrapper[tasks.size()]));
                results.add(wrapper);
            }
            return results;
        }

        private void formatFirstBatch(org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeContext ctx, org.apache.ambari.server.stack.upgrade.ManualTask task, java.util.List<org.apache.ambari.server.stack.upgrade.orchestrate.StageWrapper> wrappers) {
            java.util.Set<java.lang.String> names = new java.util.LinkedHashSet<>();
            java.util.Map<java.lang.String, java.util.Set<java.lang.String>> compLocations = new java.util.HashMap<>();
            for (org.apache.ambari.server.stack.upgrade.orchestrate.StageWrapper sw : wrappers) {
                for (org.apache.ambari.server.stack.upgrade.orchestrate.TaskWrapper tw : sw.getTasks()) {
                    if (org.apache.commons.lang.StringUtils.isNotEmpty(tw.getService()) && org.apache.commons.lang.StringUtils.isNotBlank(tw.getComponent())) {
                        for (java.lang.String host : tw.getHosts()) {
                            if (!compLocations.containsKey(host)) {
                                compLocations.put(host, new java.util.HashSet<>());
                            }
                            compLocations.get(host).add(tw.getComponent());
                        }
                        names.add(ctx.getComponentDisplay(tw.getService(), tw.getComponent()));
                    }
                }
            }
            for (int i = 0; i < task.messages.size(); i++) {
                java.lang.String message = task.messages.get(i);
                if (message.contains("{{components}}")) {
                    java.lang.StringBuilder sb = new java.lang.StringBuilder();
                    java.util.List<java.lang.String> compNames = new java.util.ArrayList<>(names);
                    if (compNames.size() == 1) {
                        sb.append(compNames.get(0));
                    } else if (names.size() > 1) {
                        java.lang.String last = compNames.remove(compNames.size() - 1);
                        sb.append(org.apache.commons.lang.StringUtils.join(compNames, ", "));
                        sb.append(" and ").append(last);
                    }
                    message = message.replace("{{components}}", sb.toString());
                    task.messages.set(i, message);
                }
            }
            com.google.gson.JsonArray arr = new com.google.gson.JsonArray();
            for (java.util.Map.Entry<java.lang.String, java.util.Set<java.lang.String>> entry : compLocations.entrySet()) {
                com.google.gson.JsonObject obj = new com.google.gson.JsonObject();
                obj.addProperty("host_name", entry.getKey());
                com.google.gson.JsonArray comps = new com.google.gson.JsonArray();
                for (java.lang.String comp : entry.getValue()) {
                    comps.add(new com.google.gson.JsonPrimitive(comp));
                }
                obj.add("components", comps);
                arr.add(obj);
            }
            com.google.gson.JsonObject master = new com.google.gson.JsonObject();
            master.add("topology", arr);
            task.structuredOut = master.toString();
        }
    }

    private static class TaskProxy {
        private boolean restart = false;

        private java.lang.String service;

        private java.lang.String component;

        private java.lang.String message;

        private org.apache.ambari.server.stack.upgrade.orchestrate.StageWrapper.Type type;

        private boolean clientOnly = false;

        private java.util.List<org.apache.ambari.server.stack.upgrade.orchestrate.TaskWrapper> tasks = new java.util.ArrayList<>();

        @java.lang.Override
        public java.lang.String toString() {
            java.lang.String s = "";
            for (org.apache.ambari.server.stack.upgrade.orchestrate.TaskWrapper t : tasks) {
                s += ((component + "/") + t.getTasks()) + " ";
            }
            return s;
        }

        private org.apache.ambari.server.stack.upgrade.orchestrate.TaskWrapper[] getTasksArray(com.google.common.base.Predicate<org.apache.ambari.server.stack.upgrade.Task> predicate) {
            if (null == predicate) {
                return tasks.toArray(new org.apache.ambari.server.stack.upgrade.orchestrate.TaskWrapper[tasks.size()]);
            }
            java.util.List<org.apache.ambari.server.stack.upgrade.orchestrate.TaskWrapper> interim = new java.util.ArrayList<>();
            for (org.apache.ambari.server.stack.upgrade.orchestrate.TaskWrapper wrapper : tasks) {
                java.util.Collection<org.apache.ambari.server.stack.upgrade.Task> filtered = com.google.common.collect.Collections2.filter(wrapper.getTasks(), predicate);
                if (org.apache.commons.collections.CollectionUtils.isNotEmpty(filtered)) {
                    interim.add(wrapper);
                }
            }
            return interim.toArray(new org.apache.ambari.server.stack.upgrade.orchestrate.TaskWrapper[interim.size()]);
        }
    }
}