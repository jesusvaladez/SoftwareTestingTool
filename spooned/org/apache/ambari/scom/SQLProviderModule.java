package org.apache.ambari.scom;
public class SQLProviderModule extends org.apache.ambari.server.controller.internal.DefaultProviderModule implements org.apache.ambari.scom.HostInfoProvider , org.apache.ambari.msi.StateProvider {
    private final org.apache.ambari.msi.ClusterDefinition clusterDefinition;

    private final org.apache.ambari.server.controller.jdbc.ConnectionFactory connectionFactory = org.apache.ambari.scom.SinkConnectionFactory.instance();

    private final org.apache.ambari.server.configuration.ComponentSSLConfiguration sslConfiguration = org.apache.ambari.server.configuration.ComponentSSLConfiguration.instance();

    private final org.apache.ambari.server.controller.internal.URLStreamProvider urlStreamProvider = new org.apache.ambari.server.controller.internal.URLStreamProvider(5000, 10000, sslConfiguration.getTruststorePath(), sslConfiguration.getTruststorePassword(), sslConfiguration.getTruststoreType());

    private static java.util.Map<java.lang.String, java.lang.String> serviceNames = new java.util.HashMap<java.lang.String, java.lang.String>();

    private void initServiceNames() {
        java.lang.Integer majorStackVersion = clusterDefinition.getMajorStackVersion();
        java.lang.Integer minorStackVersion = clusterDefinition.getMinorStackVersion();
        if (majorStackVersion != null) {
            org.apache.ambari.scom.SQLProviderModule.serviceNames.put("HIVE_SERVER", majorStackVersion == 1 ? "hiveserver" : "hiveserver2");
            if (minorStackVersion != null) {
                org.apache.ambari.scom.SQLProviderModule.serviceNames.put("HISTORYSERVER", (majorStackVersion > 1) && (minorStackVersion > 0) ? "jobhistoryserver" : "historyserver");
            }
        }
        org.apache.ambari.scom.SQLProviderModule.serviceNames.put("NAMENODE", "namenode");
        org.apache.ambari.scom.SQLProviderModule.serviceNames.put("SECONDARY_NAMENODE", "secondarynamenode");
        org.apache.ambari.scom.SQLProviderModule.serviceNames.put("JOBTRACKER", "jobtracker");
        org.apache.ambari.scom.SQLProviderModule.serviceNames.put("HIVE_METASTORE", "metastore");
        org.apache.ambari.scom.SQLProviderModule.serviceNames.put("HIVE_CLIENT", "hwi");
        org.apache.ambari.scom.SQLProviderModule.serviceNames.put("OOZIE_SERVER", "oozieservice");
        org.apache.ambari.scom.SQLProviderModule.serviceNames.put("FLUME_SERVER", "flumagent");
        org.apache.ambari.scom.SQLProviderModule.serviceNames.put("HBASE_MASTER", "master");
        org.apache.ambari.scom.SQLProviderModule.serviceNames.put("HBASE_REGIONSERVER", "regionserver");
        org.apache.ambari.scom.SQLProviderModule.serviceNames.put("ZOOKEEPER_SERVER", "zkServer");
        org.apache.ambari.scom.SQLProviderModule.serviceNames.put("ZKFC", "zkfc");
        org.apache.ambari.scom.SQLProviderModule.serviceNames.put("DATANODE", "datanode");
        org.apache.ambari.scom.SQLProviderModule.serviceNames.put("TASKTRACKER", "tasktracker");
        org.apache.ambari.scom.SQLProviderModule.serviceNames.put("WEBHCAT_SERVER", "templeton");
        org.apache.ambari.scom.SQLProviderModule.serviceNames.put("NODEMANAGER", "nodemanager");
        org.apache.ambari.scom.SQLProviderModule.serviceNames.put("RESOURCEMANAGER", "resourcemanager");
        org.apache.ambari.scom.SQLProviderModule.serviceNames.put("JOURNALNODE", "journalnode");
    }

    private static final java.lang.String STATE_PREFIX = "STATE              : ";

    public SQLProviderModule() {
        clusterDefinition = new org.apache.ambari.msi.ClusterDefinition(this, org.apache.ambari.scom.ClusterDefinitionProvider.instance(), this);
        initServiceNames();
    }

    @java.lang.Override
    protected org.apache.ambari.server.controller.spi.ResourceProvider createResourceProvider(org.apache.ambari.server.controller.spi.Resource.Type type) {
        return org.apache.ambari.msi.AbstractResourceProvider.getResourceProvider(type, clusterDefinition);
    }

    @java.lang.Override
    protected void createPropertyProviders(org.apache.ambari.server.controller.spi.Resource.Type type) {
        java.util.List<org.apache.ambari.server.controller.spi.PropertyProvider> providers = new java.util.LinkedList<org.apache.ambari.server.controller.spi.PropertyProvider>();
        if (type.equals(org.apache.ambari.server.controller.spi.Resource.Type.Component)) {
            providers.add(new org.apache.ambari.server.controller.jmx.JMXPropertyProvider(org.apache.ambari.scom.utilities.SCOMMetricHelper.getJMXPropertyIds(type), urlStreamProvider, this, this, org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("ServiceComponentInfo", "cluster_name"), null, org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("ServiceComponentInfo", "component_name"), org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("ServiceComponentInfo", "state")));
            providers.add(new org.apache.ambari.scom.SQLPropertyProvider(org.apache.ambari.scom.utilities.SCOMMetricHelper.getSqlServerPropertyIds(type), this, org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("ServiceComponentInfo", "cluster_name"), null, org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("ServiceComponentInfo", "component_name"), org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("ServiceComponentInfo", "service_name"), connectionFactory));
        } else if (type.equals(org.apache.ambari.server.controller.spi.Resource.Type.HostComponent)) {
            providers.add(new org.apache.ambari.server.controller.jmx.JMXPropertyProvider(org.apache.ambari.scom.utilities.SCOMMetricHelper.getJMXPropertyIds(type), urlStreamProvider, this, this, org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("HostRoles", "cluster_name"), org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("HostRoles", "host_name"), org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("HostRoles", "component_name"), org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("HostRoles", "state")));
            providers.add(new org.apache.ambari.scom.SQLPropertyProvider(org.apache.ambari.scom.utilities.SCOMMetricHelper.getSqlServerPropertyIds(type), this, org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("HostRoles", "cluster_name"), org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("HostRoles", "host_name"), org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("HostRoles", "component_name"), org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("HostRoles", "service_name"), connectionFactory));
        }
        putPropertyProviders(type, providers);
    }

    @java.lang.Override
    public java.lang.String getHostName(java.lang.String clusterName, java.lang.String componentName) throws org.apache.ambari.server.controller.spi.SystemException {
        return getClusterNodeName(super.getHostName(clusterName, componentName));
    }

    @java.lang.Override
    public java.util.Set<java.lang.String> getHostNames(java.lang.String clusterName, java.lang.String componentName) {
        return super.getHostNames(clusterName, componentName);
    }

    @java.lang.Override
    public java.lang.String getHostName(java.lang.String id) throws org.apache.ambari.server.controller.spi.SystemException {
        return getClusterNodeName(id);
    }

    @java.lang.Override
    public java.lang.String getHostAddress(java.lang.String id) throws org.apache.ambari.server.controller.spi.SystemException {
        return getClusterHostAddress(id);
    }

    @java.lang.Override
    public org.apache.ambari.msi.StateProvider.State getRunningState(java.lang.String hostName, java.lang.String componentName) {
        java.lang.String serviceName = getServiceName(componentName);
        if (serviceName != null) {
            java.lang.String[] cmdStrings = new java.lang.String[]{ "sc", "\\\\" + hostName, "query", ("\"" + serviceName) + "\"" };
            java.lang.Process process = runProcess(cmdStrings);
            if (process.exitValue() == 0) {
                java.lang.String response = org.apache.ambari.scom.SQLProviderModule.getProcessResponse(process.getInputStream());
                int i = response.indexOf(org.apache.ambari.scom.SQLProviderModule.STATE_PREFIX);
                if (i >= 0) {
                    int state = java.lang.Integer.parseInt(response.substring(i + org.apache.ambari.scom.SQLProviderModule.STATE_PREFIX.length(), (i + org.apache.ambari.scom.SQLProviderModule.STATE_PREFIX.length()) + 1));
                    switch (state) {
                        case 1 :
                            return org.apache.ambari.msi.StateProvider.State.Stopped;
                        case 4 :
                            return org.apache.ambari.msi.StateProvider.State.Running;
                    }
                }
            }
        }
        return org.apache.ambari.msi.StateProvider.State.Unknown;
    }

    @java.lang.Override
    public org.apache.ambari.msi.StateProvider.Process setRunningState(java.lang.String hostName, java.lang.String componentName, org.apache.ambari.msi.StateProvider.State state) {
        java.lang.String serviceName = getServiceName(componentName);
        if (serviceName != null) {
            java.lang.String command = (state == org.apache.ambari.msi.StateProvider.State.Running) ? "start" : "stop";
            java.lang.String[] cmdStrings = new java.lang.String[]{ "sc", "\\\\" + hostName, command, ("\"" + serviceName) + "\"" };
            return new org.apache.ambari.scom.SQLProviderModule.StateProcess(runProcess(cmdStrings));
        }
        return null;
    }

    private java.lang.String getClusterNodeName(java.lang.String hostname) throws org.apache.ambari.server.controller.spi.SystemException {
        try {
            if (hostname.equalsIgnoreCase("localhost")) {
                return java.net.InetAddress.getLocalHost().getCanonicalHostName();
            }
            return java.net.InetAddress.getByName(hostname).getCanonicalHostName();
        } catch (java.lang.Exception e) {
            throw new org.apache.ambari.server.controller.spi.SystemException("Error getting hostname.", e);
        }
    }

    private java.lang.String getClusterHostAddress(java.lang.String hostname) throws org.apache.ambari.server.controller.spi.SystemException {
        try {
            if (hostname.equalsIgnoreCase("localhost")) {
                return java.net.InetAddress.getLocalHost().getHostAddress();
            }
            return java.net.InetAddress.getByName(hostname).getHostAddress();
        } catch (java.lang.Exception e) {
            throw new org.apache.ambari.server.controller.spi.SystemException("Error getting ip address.", e);
        }
    }

    private java.lang.String getServiceName(java.lang.String componentName) {
        return org.apache.ambari.scom.SQLProviderModule.serviceNames.get(componentName);
    }

    private java.lang.Process runProcess(java.lang.String... commands) {
        java.lang.Runtime runtime = java.lang.Runtime.getRuntime();
        java.lang.Process process;
        try {
            process = runtime.exec(commands);
            process.waitFor();
        } catch (java.lang.Exception e) {
            return null;
        }
        return process;
    }

    private static java.lang.String getProcessResponse(java.io.InputStream stream) {
        java.lang.StringBuilder sb = new java.lang.StringBuilder();
        java.io.BufferedReader stdInput = new java.io.BufferedReader(new java.io.InputStreamReader(stream));
        try {
            java.lang.String line;
            while ((line = stdInput.readLine()) != null) {
                sb.append(line);
            } 
        } catch (java.lang.Exception e) {
            return null;
        }
        return sb.toString();
    }

    public static class StateProcess implements org.apache.ambari.msi.StateProvider.Process {
        private final java.lang.Process process;

        private java.lang.String output = null;

        private java.lang.String error = null;

        public StateProcess(java.lang.Process process) {
            this.process = process;
        }

        @java.lang.Override
        public boolean isRunning() {
            try {
                process.exitValue();
            } catch (java.lang.IllegalThreadStateException e) {
                return true;
            }
            return false;
        }

        @java.lang.Override
        public int getExitCode() {
            return process.exitValue();
        }

        @java.lang.Override
        public java.lang.String getOutput() {
            if (output != null) {
                return output;
            }
            java.lang.String processResponse = org.apache.ambari.scom.SQLProviderModule.getProcessResponse(process.getInputStream());
            if (!isRunning()) {
                output = processResponse;
            }
            return processResponse;
        }

        @java.lang.Override
        public java.lang.String getError() {
            if (error != null) {
                return error;
            }
            java.lang.String processResponse = org.apache.ambari.scom.SQLProviderModule.getProcessResponse(process.getErrorStream());
            if (!isRunning()) {
                error = processResponse;
            }
            return processResponse;
        }
    }
}