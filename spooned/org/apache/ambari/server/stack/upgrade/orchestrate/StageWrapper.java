package org.apache.ambari.server.stack.upgrade.orchestrate;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
public class StageWrapper {
    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.stack.upgrade.orchestrate.StageWrapper.class);

    private static com.google.gson.Gson gson = new com.google.gson.Gson();

    private java.lang.String text;

    private org.apache.ambari.server.stack.upgrade.orchestrate.StageWrapper.Type type;

    private java.util.Map<java.lang.String, java.lang.String> params;

    private java.util.List<org.apache.ambari.server.stack.upgrade.orchestrate.TaskWrapper> tasks;

    public StageWrapper(org.apache.ambari.server.stack.upgrade.orchestrate.StageWrapper.Type type, java.lang.String text, org.apache.ambari.server.stack.upgrade.orchestrate.TaskWrapper... tasks) {
        this(type, text, null, java.util.Arrays.asList(tasks));
    }

    public StageWrapper(org.apache.ambari.server.stack.upgrade.orchestrate.StageWrapper.Type type, java.lang.String text, java.util.Map<java.lang.String, java.lang.String> params, org.apache.ambari.server.stack.upgrade.orchestrate.TaskWrapper... tasks) {
        this(type, text, params, java.util.Arrays.asList(tasks));
    }

    public StageWrapper(org.apache.ambari.server.stack.upgrade.orchestrate.StageWrapper.Type type, java.lang.String text, java.util.Map<java.lang.String, java.lang.String> params, java.util.List<org.apache.ambari.server.stack.upgrade.orchestrate.TaskWrapper> tasks) {
        this.type = type;
        this.text = text;
        this.params = (params == null) ? java.util.Collections.emptyMap() : params;
        this.tasks = tasks;
    }

    public java.lang.String getHostsJson() {
        return org.apache.ambari.server.stack.upgrade.orchestrate.StageWrapper.gson.toJson(getHosts());
    }

    public java.lang.String getTasksJson() {
        java.util.List<org.apache.ambari.server.stack.upgrade.Task> realTasks = new java.util.ArrayList<>();
        for (org.apache.ambari.server.stack.upgrade.orchestrate.TaskWrapper tw : tasks) {
            realTasks.addAll(tw.getTasks());
        }
        return org.apache.ambari.server.stack.upgrade.orchestrate.StageWrapper.gson.toJson(realTasks);
    }

    public java.util.Set<java.lang.String> getHosts() {
        java.util.Set<java.lang.String> hosts = new java.util.HashSet<>();
        for (org.apache.ambari.server.stack.upgrade.orchestrate.TaskWrapper tw : tasks) {
            hosts.addAll(tw.getHosts());
        }
        return hosts;
    }

    public java.util.Map<java.lang.String, java.lang.String> getParams() {
        return params;
    }

    public java.util.List<org.apache.ambari.server.stack.upgrade.orchestrate.TaskWrapper> getTasks() {
        return tasks;
    }

    public java.lang.String getText() {
        return text;
    }

    public void setText(java.lang.String newText) {
        text = newText;
    }

    public org.apache.ambari.server.stack.upgrade.orchestrate.StageWrapper.Type getType() {
        return type;
    }

    public enum Type {

        SERVER_SIDE_ACTION,
        RESTART,
        UPGRADE_TASKS,
        SERVICE_CHECK,
        STOP,
        START,
        CONFIGURE,
        REGENERATE_KEYTABS;}

    @java.lang.Override
    public java.lang.String toString() {
        return com.google.common.base.MoreObjects.toStringHelper(this).add("type", type).add("text", text).omitNullValues().toString();
    }

    public java.lang.Integer getMaxTimeout(org.apache.ambari.server.configuration.Configuration configuration) {
        java.util.Set<java.lang.String> timeoutKeys = new java.util.HashSet<>();
        for (org.apache.ambari.server.stack.upgrade.orchestrate.TaskWrapper wrapper : tasks) {
            timeoutKeys.addAll(wrapper.getTimeoutKeys());
        }
        java.lang.Integer defaultTimeout = java.lang.Integer.valueOf(configuration.getDefaultAgentTaskTimeout(false));
        if (org.apache.commons.collections.CollectionUtils.isEmpty(timeoutKeys)) {
            return defaultTimeout;
        }
        java.lang.Integer timeout = null;
        for (java.lang.String key : timeoutKeys) {
            java.lang.String configValue = configuration.getProperty(key);
            if (org.apache.commons.lang.StringUtils.isNotBlank(configValue)) {
                try {
                    java.lang.Integer configTimeout = java.lang.Integer.valueOf(configValue);
                    if ((null == timeout) || (configTimeout > timeout)) {
                        timeout = configTimeout;
                    }
                } catch (java.lang.Exception e) {
                    org.apache.ambari.server.stack.upgrade.orchestrate.StageWrapper.LOG.warn("Could not parse {}/{} to a timeout value", key, configValue);
                }
            } else {
                org.apache.ambari.server.stack.upgrade.orchestrate.StageWrapper.LOG.warn("Configuration {} not found to compute timeout", key);
            }
        }
        return null == timeout ? defaultTimeout : timeout;
    }
}