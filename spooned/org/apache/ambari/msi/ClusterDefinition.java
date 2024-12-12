package org.apache.ambari.msi;
import org.apache.commons.lang.StringUtils;
public class ClusterDefinition {
    private static final java.lang.String COMMENT_TAG = "#";

    private static final java.lang.String HA_PROPERTY_INDICATOR = "HA";

    private static java.lang.Boolean HA_ENABLE = java.lang.Boolean.FALSE;

    private final java.util.Set<java.lang.String> services = new java.util.HashSet<java.lang.String>();

    private final java.util.Set<java.lang.String> hosts = new java.util.HashSet<java.lang.String>();

    private final java.util.Map<java.lang.String, java.util.Set<java.lang.String>> components = new java.util.HashMap<java.lang.String, java.util.Set<java.lang.String>>();

    private final java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.util.Set<java.lang.String>>> hostComponents = new java.util.HashMap<java.lang.String, java.util.Map<java.lang.String, java.util.Set<java.lang.String>>>();

    private final java.util.Map<java.lang.Integer, org.apache.ambari.msi.StateProvider.Process> processes = new java.util.HashMap<java.lang.Integer, org.apache.ambari.msi.StateProvider.Process>();

    private final java.util.Set<org.apache.ambari.server.controller.spi.Resource> requestResources = new java.util.HashSet<org.apache.ambari.server.controller.spi.Resource>();

    private final java.util.Set<org.apache.ambari.server.controller.spi.Resource> taskResources = new java.util.HashSet<org.apache.ambari.server.controller.spi.Resource>();

    private final org.apache.ambari.msi.StateProvider stateProvider;

    private final org.apache.ambari.scom.ClusterDefinitionProvider definitionProvider;

    private final org.apache.ambari.scom.HostInfoProvider hostInfoProvider;

    private java.lang.String clusterName;

    private java.lang.String versionId;

    private int nextRequestId = 1;

    private int nextTaskId = 1;

    private final java.util.Set<java.lang.String> clientOnlyComponents = new java.util.HashSet<java.lang.String>() {
        {
            add("PIG");
            add("SQOOP");
            add("YARN_CLIENT");
            add("MAPREDUCE2_CLIENT");
        }
    };

    private java.lang.Boolean isClientOnlyComponent(java.lang.String componentName) {
        return clientOnlyComponents.contains(componentName);
    }

    private final java.util.Set<java.lang.String> clientOnlyServices = new java.util.HashSet<java.lang.String>() {
        {
            add("PIG");
            add("SQOOP");
        }
    };

    private java.lang.Boolean isClientOnlyService(java.lang.String serviceName) {
        return clientOnlyServices.contains(serviceName);
    }

    private final java.util.Map<java.lang.String, java.util.Set<java.lang.String>> componentNameMap = new java.util.HashMap<java.lang.String, java.util.Set<java.lang.String>>();

    private void initComponentNameMap() {
        componentNameMap.put("NAMENODE_HOST", java.util.Collections.singleton("NAMENODE"));
        componentNameMap.put("SECONDARY_NAMENODE_HOST", java.util.Collections.singleton("SECONDARY_NAMENODE"));
        componentNameMap.put("OOZIE_SERVER_HOST", java.util.Collections.singleton("OOZIE_SERVER"));
        componentNameMap.put("WEBHCAT_HOST", java.util.Collections.singleton("WEBHCAT_SERVER"));
        componentNameMap.put("FLUME_HOSTS", java.util.Collections.singleton("FLUME_SERVER"));
        componentNameMap.put("HBASE_MASTER", java.util.Collections.singleton("HBASE_MASTER"));
        componentNameMap.put("HBASE_REGIONSERVERS", java.util.Collections.singleton("HBASE_REGIONSERVER"));
        componentNameMap.put("ZOOKEEPER_HOSTS", java.util.Collections.singleton("ZOOKEEPER_SERVER"));
        java.util.Set<java.lang.String> slaveComponents = new java.util.HashSet<java.lang.String>();
        slaveComponents.add("DATANODE");
        componentNameMap.put("SLAVE_HOSTS", slaveComponents);
        java.util.Set<java.lang.String> hiveComponents = new java.util.HashSet<java.lang.String>();
        hiveComponents.add("HIVE_SERVER");
        hiveComponents.add("HIVE_METASTORE");
        hiveComponents.add("HIVE_CLIENT");
        componentNameMap.put("HIVE_SERVER_HOST", hiveComponents);
        java.lang.Integer majorStackVersion = getMajorStackVersion();
        java.lang.Integer minorStackVersion = getMinorStackVersion();
        if (majorStackVersion != null) {
            if (majorStackVersion == 1) {
                java.util.Set<java.lang.String> mapReduceComponents = new java.util.HashSet<java.lang.String>();
                mapReduceComponents.add("JOBTRACKER");
                mapReduceComponents.add("HISTORYSERVER");
                componentNameMap.put("JOBTRACKER_HOST", mapReduceComponents);
                slaveComponents.add("TASKTRACKER");
            }
            if (majorStackVersion == 2) {
                componentNameMap.put("JOURNALNODE_HOST", java.util.Collections.singleton("JOURNALNODE"));
                componentNameMap.put(minorStackVersion > 0 ? "NN_HA_JOURNALNODE_HOSTS" : "HA_JOURNALNODE_HOSTS", java.util.Collections.singleton("JOURNALNODE"));
                java.util.Set<java.lang.String> haNamenodeComponents = new java.util.HashSet<java.lang.String>();
                haNamenodeComponents.add("NAMENODE");
                haNamenodeComponents.add("ZKFC");
                componentNameMap.put(minorStackVersion > 0 ? "NN_HA_STANDBY_NAMENODE_HOST" : "HA_NAMENODE_HOST", haNamenodeComponents);
                java.util.Set<java.lang.String> mapReduce2Components = new java.util.HashSet<java.lang.String>();
                mapReduce2Components.add("HISTORYSERVER");
                mapReduce2Components.add("RESOURCEMANAGER");
                componentNameMap.put("RESOURCEMANAGER_HOST", mapReduce2Components);
                componentNameMap.put("RM_HA_STANDBY_RESOURCEMANAGER_HOST", java.util.Collections.singleton("RESOURCEMANAGER"));
                slaveComponents.add("NODEMANAGER");
                java.util.Set<java.lang.String> clientHosts = new java.util.HashSet<java.lang.String>();
                clientHosts.add("PIG");
                clientHosts.add("SQOOP");
                clientHosts.add("YARN_CLIENT");
                clientHosts.add("MAPREDUCE2_CLIENT");
                componentNameMap.put("CLIENT_HOSTS", clientHosts);
            }
        }
    }

    private final java.util.Map<java.lang.String, java.lang.String> componentServiceMap = new java.util.HashMap<java.lang.String, java.lang.String>();

    private void initComponentServiceMap() {
        componentServiceMap.put("NAMENODE", "HDFS");
        componentServiceMap.put("DATANODE", "HDFS");
        componentServiceMap.put("SECONDARY_NAMENODE", "HDFS");
        componentServiceMap.put("HIVE_SERVER", "HIVE");
        componentServiceMap.put("HIVE_METASTORE", "HIVE");
        componentServiceMap.put("HIVE_CLIENT", "HIVE");
        componentServiceMap.put("OOZIE_SERVER", "OOZIE");
        componentServiceMap.put("WEBHCAT_SERVER", "HIVE");
        componentServiceMap.put("FLUME_SERVER", "FLUME");
        componentServiceMap.put("HBASE_MASTER", "HBASE");
        componentServiceMap.put("HBASE_REGIONSERVER", "HBASE");
        componentServiceMap.put("ZOOKEEPER_SERVER", "ZOOKEEPER");
        java.lang.Integer majorStackVersion = getMajorStackVersion();
        if (majorStackVersion != null) {
            if (majorStackVersion == 1) {
                componentServiceMap.put("JOBTRACKER", "MAPREDUCE");
                componentServiceMap.put("HISTORYSERVER", "MAPREDUCE");
                componentServiceMap.put("TASKTRACKER", "MAPREDUCE");
            }
            if (majorStackVersion == 2) {
                componentServiceMap.put("PIG", "PIG");
                componentServiceMap.put("SQOOP", "SQOOP");
                componentServiceMap.put("HISTORYSERVER", "MAPREDUCE2");
                componentServiceMap.put("MAPREDUCE2_CLIENT", "MAPREDUCE2");
                componentServiceMap.put("JOURNALNODE", "HDFS");
                componentServiceMap.put("NODEMANAGER", "YARN");
                componentServiceMap.put("RESOURCEMANAGER", "YARN");
                componentServiceMap.put("YARN_CLIENT", "YARN");
                componentServiceMap.put("ZKFC", "HDFS");
            }
        }
    }

    public ClusterDefinition(org.apache.ambari.msi.StateProvider stateProvider, org.apache.ambari.scom.ClusterDefinitionProvider definitionProvider, org.apache.ambari.scom.HostInfoProvider hostInfoProvider) {
        this.stateProvider = stateProvider;
        this.definitionProvider = definitionProvider;
        this.hostInfoProvider = hostInfoProvider;
        this.clusterName = definitionProvider.getClusterName();
        this.versionId = definitionProvider.getVersionId();
        init();
        try {
            readClusterDefinition();
            haEnableSetup();
        } catch (java.io.IOException e) {
            java.lang.String msg = "Caught exception reading cluster definition file.";
            throw new java.lang.IllegalStateException(msg, e);
        }
    }

    private void haEnableSetup() {
        if (org.apache.ambari.msi.ClusterDefinition.HA_ENABLE) {
            java.util.Map<java.lang.String, java.util.Set<java.lang.String>> serviceHostComponents = hostComponents.get(componentServiceMap.get("ZKFC"));
            if (serviceHostComponents != null) {
                for (java.lang.String host : serviceHostComponents.keySet()) {
                    java.util.Set<java.lang.String> hostHostComponents = serviceHostComponents.get(host);
                    if ((hostHostComponents != null) && hostHostComponents.contains("NAMENODE")) {
                        hostHostComponents.add("ZKFC");
                    }
                }
            }
        }
    }

    public java.lang.Integer getMajorStackVersion() {
        if (org.apache.commons.lang.StringUtils.isNotEmpty(versionId)) {
            java.lang.String majorVersion = org.apache.commons.lang.StringUtils.substring(versionId, 4, 5);
            if (org.apache.commons.lang.StringUtils.isNotEmpty(majorVersion)) {
                return java.lang.Integer.parseInt(majorVersion);
            }
        }
        return null;
    }

    public java.lang.Integer getMinorStackVersion() {
        if (org.apache.commons.lang.StringUtils.isNotEmpty(versionId)) {
            java.lang.String majorVersion = org.apache.commons.lang.StringUtils.substring(versionId, 6, 7);
            if (org.apache.commons.lang.StringUtils.isNotEmpty(majorVersion)) {
                return java.lang.Integer.parseInt(majorVersion);
            }
        }
        return null;
    }

    public java.lang.String getClusterName() {
        return clusterName;
    }

    public java.lang.String getVersionId() {
        return versionId;
    }

    public java.util.Set<java.lang.String> getServices() {
        return services;
    }

    public java.util.Set<java.lang.String> getHosts() {
        return hosts;
    }

    public org.apache.ambari.scom.HostInfoProvider getHostInfoProvider() {
        return hostInfoProvider;
    }

    public java.util.Set<java.lang.String> getComponents(java.lang.String service) {
        java.util.Set<java.lang.String> componentSet = components.get(service);
        return componentSet == null ? java.util.Collections.<java.lang.String>emptySet() : componentSet;
    }

    public java.util.Set<java.lang.String> getHostComponents(java.lang.String service, java.lang.String host) {
        java.util.Set<java.lang.String> resultSet = null;
        java.util.Map<java.lang.String, java.util.Set<java.lang.String>> serviceHostComponents = hostComponents.get(service);
        if (serviceHostComponents != null) {
            resultSet = serviceHostComponents.get(host);
        }
        return resultSet == null ? java.util.Collections.<java.lang.String>emptySet() : resultSet;
    }

    public java.lang.String getHostState(java.lang.String hostName) {
        for (java.util.Map.Entry<java.lang.String, java.util.Map<java.lang.String, java.util.Set<java.lang.String>>> entry : hostComponents.entrySet()) {
            java.util.Map<java.lang.String, java.util.Set<java.lang.String>> serviceHostComponents = entry.getValue();
            for (java.util.Map.Entry<java.lang.String, java.util.Set<java.lang.String>> hostEntry : serviceHostComponents.entrySet()) {
                if (hostEntry.getKey().equals(hostName)) {
                    java.util.Set<java.lang.String> componentNames = hostEntry.getValue();
                    for (java.lang.String componentName : componentNames) {
                        if (isClientOnlyComponent(componentName))
                            continue;

                        if (stateProvider.getRunningState(hostName, componentName) != org.apache.ambari.msi.StateProvider.State.Running) {
                            return "UNHEALTHY";
                        }
                    }
                }
            }
        }
        return "HEALTHY";
    }

    public java.lang.String getServiceState(java.lang.String serviceName) {
        if (isClientOnlyService(serviceName))
            return "INSTALLED";

        java.util.Map<java.lang.String, java.util.Set<java.lang.String>> serviceHostComponents = hostComponents.get(serviceName);
        if (serviceHostComponents != null) {
            for (java.util.Map.Entry<java.lang.String, java.util.Set<java.lang.String>> entry : serviceHostComponents.entrySet()) {
                java.lang.String hostName = entry.getKey();
                java.util.Set<java.lang.String> componentNames = entry.getValue();
                for (java.lang.String componentName : componentNames) {
                    if (isClientOnlyComponent(componentName))
                        continue;

                    if (stateProvider.getRunningState(hostName, componentName) != org.apache.ambari.msi.StateProvider.State.Running) {
                        return "INSTALLED";
                    }
                }
            }
        }
        return "STARTED";
    }

    public int setServiceState(java.lang.String serviceName, java.lang.String state) {
        org.apache.ambari.msi.StateProvider.State s = (state.equals("STARTED")) ? org.apache.ambari.msi.StateProvider.State.Running : state.equals("INSTALLED") ? org.apache.ambari.msi.StateProvider.State.Stopped : org.apache.ambari.msi.StateProvider.State.Unknown;
        int requestId = -1;
        if (!isClientOnlyService(serviceName)) {
            if ((s != org.apache.ambari.msi.StateProvider.State.Unknown) && (!state.equals(getServiceState(serviceName)))) {
                java.util.Map<java.lang.String, java.util.Set<java.lang.String>> serviceHostComponents = hostComponents.get(serviceName);
                if (serviceHostComponents != null) {
                    for (java.util.Map.Entry<java.lang.String, java.util.Set<java.lang.String>> entry : serviceHostComponents.entrySet()) {
                        java.lang.String hostName = entry.getKey();
                        java.util.Set<java.lang.String> componentNames = entry.getValue();
                        for (java.lang.String componentName : componentNames) {
                            if (isClientOnlyComponent(componentName) || state.equals(getHostComponentState(hostName, componentName)))
                                continue;

                            requestId = recordProcess(stateProvider.setRunningState(hostName, componentName, s), requestId, (("Set service " + serviceName) + " state to ") + s);
                        }
                    }
                }
            }
        }
        return requestId;
    }

    public java.lang.String getComponentState(java.lang.String serviceName, java.lang.String componentName) {
        java.util.Map<java.lang.String, java.util.Set<java.lang.String>> serviceHostComponents = hostComponents.get(serviceName);
        if (serviceHostComponents != null) {
            for (java.util.Map.Entry<java.lang.String, java.util.Set<java.lang.String>> entry : serviceHostComponents.entrySet()) {
                java.lang.String hostName = entry.getKey();
                java.util.Set<java.lang.String> componentNames = entry.getValue();
                for (java.lang.String name : componentNames) {
                    if (name.equals(componentName)) {
                        if (isClientOnlyComponent(componentName))
                            return "INSTALLED";

                        if (stateProvider.getRunningState(hostName, componentName) != org.apache.ambari.msi.StateProvider.State.Running) {
                            return "INSTALLED";
                        }
                    }
                }
            }
        }
        return "STARTED";
    }

    public int setComponentState(java.lang.String serviceName, java.lang.String componentName, java.lang.String state) {
        org.apache.ambari.msi.StateProvider.State s = (state.equals("STARTED")) ? org.apache.ambari.msi.StateProvider.State.Running : state.equals("INSTALLED") ? org.apache.ambari.msi.StateProvider.State.Stopped : org.apache.ambari.msi.StateProvider.State.Unknown;
        int requestId = -1;
        if (!isClientOnlyComponent(componentName)) {
            if (s != org.apache.ambari.msi.StateProvider.State.Unknown) {
                java.util.Map<java.lang.String, java.util.Set<java.lang.String>> serviceHostComponents = hostComponents.get(serviceName);
                if (serviceHostComponents != null) {
                    for (java.util.Map.Entry<java.lang.String, java.util.Set<java.lang.String>> entry : serviceHostComponents.entrySet()) {
                        java.lang.String hostName = entry.getKey();
                        java.util.Set<java.lang.String> componentNames = entry.getValue();
                        for (java.lang.String name : componentNames) {
                            if (name.equals(componentName)) {
                                if (state.equals(getHostComponentState(hostName, componentName)))
                                    continue;

                                requestId = recordProcess(stateProvider.setRunningState(hostName, componentName, s), requestId, (("Set component " + componentName) + " state to ") + s);
                            }
                        }
                    }
                }
            }
        }
        return requestId;
    }

    public java.lang.String getHostComponentState(java.lang.String hostName, java.lang.String componentName) {
        java.lang.Boolean healthy = java.lang.Boolean.FALSE;
        if (!isClientOnlyComponent(componentName))
            healthy = stateProvider.getRunningState(hostName, componentName) == org.apache.ambari.msi.StateProvider.State.Running;

        return healthy ? "STARTED" : "INSTALLED";
    }

    public int setHostComponentState(java.lang.String hostName, java.lang.String componentName, java.lang.String state) {
        org.apache.ambari.msi.StateProvider.State s = (state.equals("STARTED")) ? org.apache.ambari.msi.StateProvider.State.Running : state.equals("INSTALLED") ? org.apache.ambari.msi.StateProvider.State.Stopped : org.apache.ambari.msi.StateProvider.State.Unknown;
        int requestId = -1;
        if (!isClientOnlyComponent(componentName)) {
            if ((s != org.apache.ambari.msi.StateProvider.State.Unknown) && (!state.equals(getHostComponentState(hostName, componentName)))) {
                requestId = recordProcess(stateProvider.setRunningState(hostName, componentName, s), -1, (("Set host component " + componentName) + " state to ") + s);
            }
        }
        return requestId;
    }

    public org.apache.ambari.msi.StateProvider.Process getProcess(java.lang.Integer id) {
        return processes.get(id);
    }

    public java.util.Set<org.apache.ambari.server.controller.spi.Resource> getRequestResources() {
        return requestResources;
    }

    public java.util.Set<org.apache.ambari.server.controller.spi.Resource> getTaskResources() {
        return taskResources;
    }

    private void init() {
        initComponentNameMap();
        initComponentServiceMap();
    }

    private synchronized int recordProcess(org.apache.ambari.msi.StateProvider.Process process, int requestId, java.lang.String context) {
        if (requestId == (-1)) {
            requestId = nextRequestId++;
            org.apache.ambari.server.controller.spi.Resource request = new org.apache.ambari.server.controller.internal.ResourceImpl(org.apache.ambari.server.controller.spi.Resource.Type.Request);
            request.setProperty(org.apache.ambari.msi.RequestProvider.REQUEST_ID_PROPERTY_ID, requestId);
            request.setProperty(org.apache.ambari.msi.RequestProvider.REQUEST_CLUSTER_NAME_PROPERTY_ID, clusterName);
            request.setProperty(org.apache.ambari.msi.RequestProvider.REQUEST_CONTEXT_ID, context);
            requestResources.add(request);
        }
        org.apache.ambari.server.controller.spi.Resource task = new org.apache.ambari.server.controller.internal.ResourceImpl(org.apache.ambari.server.controller.spi.Resource.Type.Task);
        int taskId = nextTaskId++;
        taskResources.add(task);
        task.setProperty(org.apache.ambari.msi.TaskProvider.TASK_ID_PROPERTY_ID, taskId);
        task.setProperty(org.apache.ambari.msi.TaskProvider.TASK_REQUEST_ID_PROPERTY_ID, requestId);
        task.setProperty(org.apache.ambari.msi.TaskProvider.TASK_CLUSTER_NAME_PROPERTY_ID, clusterName);
        processes.put(taskId, process);
        return requestId;
    }

    private void readClusterDefinition() throws java.io.IOException {
        java.io.InputStream is = definitionProvider.getInputStream();
        try {
            java.io.BufferedReader br = new java.io.BufferedReader(new java.io.InputStreamReader(is));
            java.lang.String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.startsWith(org.apache.ambari.msi.ClusterDefinition.COMMENT_TAG))
                    continue;

                int i = line.indexOf('=');
                if (i == (-1))
                    continue;

                java.lang.String propertyName = line.substring(0, i);
                java.lang.String propertyValue = line.substring(i + 1);
                if (propertyName.equalsIgnoreCase(org.apache.ambari.msi.ClusterDefinition.HA_PROPERTY_INDICATOR)) {
                    org.apache.ambari.msi.ClusterDefinition.HA_ENABLE = (propertyValue.equalsIgnoreCase("YES")) ? java.lang.Boolean.TRUE : java.lang.Boolean.FALSE;
                }
                java.util.Set<java.lang.String> componentNames = componentNameMap.get(propertyName);
                if (componentNames != null) {
                    for (java.lang.String componentName : componentNames) {
                        java.lang.String serviceName = componentServiceMap.get(componentName);
                        services.add(serviceName);
                        java.util.Set<java.lang.String> serviceComponents = components.get(serviceName);
                        if (serviceComponents == null) {
                            serviceComponents = new java.util.HashSet<java.lang.String>();
                            components.put(serviceName, serviceComponents);
                        }
                        serviceComponents.add(componentName);
                        java.util.Map<java.lang.String, java.util.Set<java.lang.String>> serviceHostComponents = hostComponents.get(serviceName);
                        if (serviceHostComponents == null) {
                            serviceHostComponents = new java.util.HashMap<java.lang.String, java.util.Set<java.lang.String>>();
                            hostComponents.put(serviceName, serviceHostComponents);
                        }
                        java.lang.String[] hostNames = propertyValue.split(",");
                        for (java.lang.String hostName : hostNames) {
                            hostName = hostName.trim();
                            java.util.Set<java.lang.String> hostHostComponents = serviceHostComponents.get(hostName);
                            if (hostHostComponents == null) {
                                hostHostComponents = new java.util.HashSet<java.lang.String>();
                                serviceHostComponents.put(hostName, hostHostComponents);
                            }
                            hostHostComponents.add(componentName);
                            hosts.add(hostName);
                        }
                    }
                }
            } 
        } finally {
            is.close();
        }
    }
}