package org.apache.ambari.server.stack.upgrade.orchestrate;
import org.apache.commons.collections.CollectionUtils;
public class TaskWrapperBuilder {
    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.stack.upgrade.orchestrate.TaskWrapperBuilder.class);

    public static java.util.List<org.apache.ambari.server.stack.upgrade.orchestrate.TaskWrapper> getTaskList(java.lang.String service, java.lang.String component, org.apache.ambari.server.stack.HostsType hostsType, java.util.List<org.apache.ambari.server.stack.upgrade.Task> tasks, java.util.Map<java.lang.String, java.lang.String> params) {
        java.lang.String ambariServerHostname = org.apache.ambari.server.utils.StageUtils.getHostName();
        java.util.List<org.apache.ambari.server.stack.upgrade.orchestrate.TaskWrapper> collection = new java.util.ArrayList<>();
        for (org.apache.ambari.server.stack.upgrade.Task t : tasks) {
            if (t.getType().isServerAction() && org.apache.commons.collections.CollectionUtils.isNotEmpty(hostsType.getHosts())) {
                collection.add(new org.apache.ambari.server.stack.upgrade.orchestrate.TaskWrapper(service, component, java.util.Collections.singleton(ambariServerHostname), params, t));
                continue;
            }
            if (t.getType().equals(org.apache.ambari.server.stack.upgrade.Task.Type.EXECUTE)) {
                org.apache.ambari.server.stack.upgrade.ExecuteTask et = ((org.apache.ambari.server.stack.upgrade.ExecuteTask) (t));
                if (et.hosts == org.apache.ambari.server.stack.upgrade.ExecuteHostType.MASTER) {
                    if (hostsType.hasMasters()) {
                        collection.add(new org.apache.ambari.server.stack.upgrade.orchestrate.TaskWrapper(service, component, hostsType.getMasters(), params, t));
                        continue;
                    } else {
                        org.apache.ambari.server.stack.upgrade.orchestrate.TaskWrapperBuilder.LOG.error(java.text.MessageFormat.format("Found an Execute task for {0} and {1} meant to run on a master but could not find any masters to run on. Skipping this task.", service, component));
                        continue;
                    }
                }
                if (et.hosts == org.apache.ambari.server.stack.upgrade.ExecuteHostType.ANY) {
                    if (!hostsType.getHosts().isEmpty()) {
                        collection.add(new org.apache.ambari.server.stack.upgrade.orchestrate.TaskWrapper(service, component, java.util.Collections.singleton(hostsType.getHosts().iterator().next()), params, t));
                        continue;
                    } else {
                        org.apache.ambari.server.stack.upgrade.orchestrate.TaskWrapperBuilder.LOG.error(java.text.MessageFormat.format("Found an Execute task for {0} and {1} meant to run on any host but could not find host to run on. Skipping this task.", service, component));
                        continue;
                    }
                }
                if (et.hosts == org.apache.ambari.server.stack.upgrade.ExecuteHostType.FIRST) {
                    if (!hostsType.getHosts().isEmpty()) {
                        java.util.List<java.lang.String> sortedHosts = new java.util.ArrayList<>(hostsType.getHosts());
                        java.util.Collections.sort(sortedHosts, java.lang.String.CASE_INSENSITIVE_ORDER);
                        collection.add(new org.apache.ambari.server.stack.upgrade.orchestrate.TaskWrapper(service, component, java.util.Collections.singleton(sortedHosts.get(0)), params, t));
                        continue;
                    } else {
                        org.apache.ambari.server.stack.upgrade.orchestrate.TaskWrapperBuilder.LOG.error(java.text.MessageFormat.format("Found an Execute task for {0} and {1} meant to run on the first host sorted alphabetically but could not find host to run on. Skipping this task.", service, component));
                        continue;
                    }
                }
            }
            collection.add(new org.apache.ambari.server.stack.upgrade.orchestrate.TaskWrapper(service, component, hostsType.getHosts(), params, t));
        }
        return collection;
    }

    public static java.util.Set<java.lang.String> getEffectiveHosts(java.util.List<org.apache.ambari.server.stack.upgrade.orchestrate.TaskWrapper> tasks) {
        java.util.Set<java.lang.String> effectiveHosts = new java.util.HashSet<>();
        for (org.apache.ambari.server.stack.upgrade.orchestrate.TaskWrapper t : tasks) {
            effectiveHosts.addAll(t.getHosts());
        }
        return effectiveHosts;
    }
}