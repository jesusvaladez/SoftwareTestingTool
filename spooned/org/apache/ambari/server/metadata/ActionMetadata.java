package org.apache.ambari.server.metadata;
@com.google.inject.Singleton
public class ActionMetadata {
    private final java.util.Map<java.lang.String, java.util.List<java.lang.String>> serviceActions = new java.util.HashMap<>();

    private final java.util.Map<java.lang.String, java.lang.String> serviceClients = new java.util.HashMap<>();

    private final java.util.Map<java.lang.String, java.lang.String> serviceCheckActions = new java.util.HashMap<>();

    private final java.util.List<java.lang.String> defaultHostComponentCommands = new java.util.ArrayList<>();

    public static final java.lang.String SERVICE_CHECK_POSTFIX = "_SERVICE_CHECK";

    private static final java.util.Map<java.lang.String, java.lang.String> SERVICE_CHECKS;

    static {
        java.util.Map<java.lang.String, java.lang.String> serviceChecks = new java.util.HashMap<>();
        serviceChecks.put(org.apache.ambari.server.state.Service.Type.ZOOKEEPER.toString(), "ZOOKEEPER_QUORUM_SERVICE_CHECK");
        SERVICE_CHECKS = java.util.Collections.unmodifiableMap(serviceChecks);
    }

    public ActionMetadata() {
        fillServiceClients();
        fillHostComponentCommands();
    }

    private void fillHostComponentCommands() {
        defaultHostComponentCommands.add("RESTART");
        defaultHostComponentCommands.add("START");
        defaultHostComponentCommands.add("STOP");
        defaultHostComponentCommands.add("INSTALL");
        defaultHostComponentCommands.add("CONFIGURE");
        defaultHostComponentCommands.add("CONFIGURE_FUNCTION");
        defaultHostComponentCommands.add("DISABLE_SECURITY");
        defaultHostComponentCommands.add("RECONFIGURE");
    }

    private void fillServiceClients() {
        serviceClients.put("hdfs", org.apache.ambari.server.Role.HDFS_CLIENT.toString());
        serviceClients.put("glusterfs", org.apache.ambari.server.Role.GLUSTERFS_CLIENT.toString());
        serviceClients.put("hbase", org.apache.ambari.server.Role.HBASE_CLIENT.toString());
        serviceClients.put("mapreduce", org.apache.ambari.server.Role.MAPREDUCE_CLIENT.toString());
        serviceClients.put("zookeeper", org.apache.ambari.server.Role.ZOOKEEPER_CLIENT.toString());
        serviceClients.put("hive", org.apache.ambari.server.Role.HIVE_CLIENT.toString());
        serviceClients.put("hcat", org.apache.ambari.server.Role.HCAT.toString());
        serviceClients.put("oozie", org.apache.ambari.server.Role.OOZIE_CLIENT.toString());
        serviceClients.put("pig", org.apache.ambari.server.Role.PIG.toString());
        serviceClients.put("mahout", org.apache.ambari.server.Role.MAHOUT.toString());
        serviceClients.put("sqoop", org.apache.ambari.server.Role.SQOOP.toString());
        serviceClients.put("yarn", org.apache.ambari.server.Role.YARN_CLIENT.toString());
        serviceClients.put("kerberos", org.apache.ambari.server.Role.KERBEROS_CLIENT.toString());
        serviceClients.put("accumulo", org.apache.ambari.server.Role.ACCUMULO_CLIENT.toString());
    }

    public java.util.List<java.lang.String> getActions(java.lang.String serviceName) {
        java.util.List<java.lang.String> result = serviceActions.get(serviceName.toLowerCase());
        if (result != null) {
            return result;
        } else {
            return java.util.Collections.emptyList();
        }
    }

    public java.lang.String getClient(java.lang.String serviceName) {
        return serviceClients.get(serviceName.toLowerCase());
    }

    public java.lang.String getServiceCheckAction(java.lang.String serviceName) {
        return serviceCheckActions.get(serviceName.toLowerCase());
    }

    public java.lang.String getServiceNameByServiceCheckAction(java.lang.String serviceCheckAction) {
        for (java.util.Map.Entry<java.lang.String, java.lang.String> entry : serviceCheckActions.entrySet()) {
            if (entry.getValue().equals(serviceCheckAction)) {
                return entry.getKey().toUpperCase();
            }
        }
        return null;
    }

    public void addServiceCheckAction(java.lang.String serviceName) {
        java.lang.String actionName = serviceName + org.apache.ambari.server.metadata.ActionMetadata.SERVICE_CHECK_POSTFIX;
        if (org.apache.ambari.server.metadata.ActionMetadata.SERVICE_CHECKS.containsKey(serviceName)) {
            actionName = org.apache.ambari.server.metadata.ActionMetadata.SERVICE_CHECKS.get(serviceName);
        }
        serviceCheckActions.put(serviceName.toLowerCase(), actionName);
        serviceActions.put(serviceName.toLowerCase(), java.util.Arrays.asList(actionName));
    }

    public boolean isDefaultHostComponentCommand(java.lang.String command) {
        if ((command != null) && defaultHostComponentCommands.contains(command)) {
            return true;
        }
        return false;
    }
}