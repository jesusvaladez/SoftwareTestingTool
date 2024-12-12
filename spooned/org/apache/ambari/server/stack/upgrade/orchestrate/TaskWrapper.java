package org.apache.ambari.server.stack.upgrade.orchestrate;
import org.apache.commons.lang.StringUtils;
public class TaskWrapper {
    private java.lang.String service;

    private java.lang.String component;

    private java.util.Set<java.lang.String> hosts;

    private java.util.Map<java.lang.String, java.lang.String> params;

    private java.util.List<org.apache.ambari.server.stack.upgrade.Task> tasks;

    private java.util.Set<java.lang.String> timeoutKeys = new java.util.HashSet<>();

    public TaskWrapper(java.lang.String s, java.lang.String c, java.util.Set<java.lang.String> hosts, org.apache.ambari.server.stack.upgrade.Task task) {
        this(s, c, hosts, null, task);
    }

    public TaskWrapper(java.lang.String s, java.lang.String c, java.util.Set<java.lang.String> hosts, java.util.Map<java.lang.String, java.lang.String> params, org.apache.ambari.server.stack.upgrade.Task... tasks) {
        this(s, c, hosts, params, java.util.Arrays.asList(tasks));
    }

    public TaskWrapper(java.lang.String s, java.lang.String c, java.util.Set<java.lang.String> hosts, java.util.Map<java.lang.String, java.lang.String> params, java.util.List<org.apache.ambari.server.stack.upgrade.Task> tasks) {
        service = s;
        component = c;
        this.hosts = hosts;
        this.params = (params == null) ? new java.util.HashMap<>() : params;
        this.tasks = tasks;
        for (org.apache.ambari.server.stack.upgrade.Task task : tasks) {
            if (org.apache.commons.lang.StringUtils.isNotBlank(task.timeoutConfig)) {
                timeoutKeys.add(task.timeoutConfig);
            }
        }
    }

    public java.util.Map<java.lang.String, java.lang.String> getParams() {
        return params;
    }

    public java.util.List<org.apache.ambari.server.stack.upgrade.Task> getTasks() {
        return tasks;
    }

    public java.util.Set<java.lang.String> getHosts() {
        return hosts;
    }

    @java.lang.Override
    public java.lang.String toString() {
        return com.google.common.base.MoreObjects.toStringHelper(this).add("service", service).add("component", component).add("tasks", tasks).add("hosts", hosts).omitNullValues().toString();
    }

    public java.lang.String getService() {
        return service;
    }

    public java.lang.String getComponent() {
        return component;
    }

    public boolean isAnyTaskSequential() {
        for (org.apache.ambari.server.stack.upgrade.Task t : getTasks()) {
            if (t.isSequential) {
                return true;
            }
        }
        return false;
    }

    public java.util.Set<java.lang.String> getTimeoutKeys() {
        return timeoutKeys;
    }
}