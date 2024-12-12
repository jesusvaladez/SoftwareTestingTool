package org.apache.ambari.server.utils;
import javax.xml.bind.JAXBException;
import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import static org.apache.ambari.server.agent.ExecutionCommand.KeyNames.AMBARI_JAVA_HOME;
import static org.apache.ambari.server.agent.ExecutionCommand.KeyNames.AMBARI_JAVA_VERSION;
import static org.apache.ambari.server.agent.ExecutionCommand.KeyNames.AMBARI_JCE_NAME;
import static org.apache.ambari.server.agent.ExecutionCommand.KeyNames.AMBARI_JDK_NAME;
import static org.apache.ambari.server.agent.ExecutionCommand.KeyNames.JAVA_HOME;
import static org.apache.ambari.server.agent.ExecutionCommand.KeyNames.JAVA_VERSION;
import static org.apache.ambari.server.agent.ExecutionCommand.KeyNames.JCE_NAME;
import static org.apache.ambari.server.agent.ExecutionCommand.KeyNames.JDK_NAME;
public class StageUtils {
    public static final java.lang.Integer DEFAULT_PING_PORT = 8670;

    public static final java.lang.String DEFAULT_RACK = "/default-rack";

    public static final java.lang.String DEFAULT_IPV4_ADDRESS = "127.0.0.1";

    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.utils.StageUtils.class);

    protected static final java.lang.String AMBARI_SERVER_HOST = "ambari_server_host";

    protected static final java.lang.String AMBARI_SERVER_PORT = "ambari_server_port";

    protected static final java.lang.String AMBARI_SERVER_USE_SSL = "ambari_server_use_ssl";

    protected static final java.lang.String HOSTS_LIST = "all_hosts";

    protected static final java.lang.String PORTS = "all_ping_ports";

    protected static final java.lang.String RACKS = "all_racks";

    protected static final java.lang.String IPV4_ADDRESSES = "all_ipv4_ips";

    private static java.util.Map<java.lang.String, java.lang.String> componentToClusterInfoKeyMap = new java.util.HashMap<>();

    private static volatile com.google.gson.Gson gson;

    @com.google.inject.Inject
    private static org.apache.ambari.server.actionmanager.StageFactory stageFactory;

    @com.google.inject.Inject
    private static org.apache.ambari.server.topology.TopologyManager topologyManager;

    @com.google.inject.Inject
    private static org.apache.ambari.server.configuration.Configuration configuration;

    @com.google.inject.Inject
    public StageUtils(org.apache.ambari.server.actionmanager.StageFactory stageFactory) {
        org.apache.ambari.server.utils.StageUtils.stageFactory = stageFactory;
    }

    private static java.lang.String server_hostname;

    static {
        try {
            server_hostname = java.net.InetAddress.getLocalHost().getCanonicalHostName().toLowerCase();
        } catch (java.net.UnknownHostException e) {
            LOG.warn("Could not find canonical hostname ", e);
            server_hostname = "localhost";
        }
    }

    public static com.google.gson.Gson getGson() {
        if (org.apache.ambari.server.utils.StageUtils.gson != null) {
            return org.apache.ambari.server.utils.StageUtils.gson;
        } else {
            synchronized(org.apache.ambari.server.utils.StageUtils.LOG) {
                if (org.apache.ambari.server.utils.StageUtils.gson == null) {
                    org.apache.ambari.server.utils.StageUtils.gson = new com.google.gson.Gson();
                }
                return org.apache.ambari.server.utils.StageUtils.gson;
            }
        }
    }

    public static void setGson(com.google.gson.Gson gson) {
        if (gson == null) {
            org.apache.ambari.server.utils.StageUtils.gson = gson;
        }
    }

    public static void setTopologyManager(org.apache.ambari.server.topology.TopologyManager topologyManager) {
        org.apache.ambari.server.utils.StageUtils.topologyManager = topologyManager;
    }

    public static void setConfiguration(org.apache.ambari.server.configuration.Configuration configuration) {
        org.apache.ambari.server.utils.StageUtils.configuration = configuration;
    }

    private static void put2componentToClusterInfoKeyMap(java.lang.String component) {
        org.apache.ambari.server.utils.StageUtils.componentToClusterInfoKeyMap.put(component, org.apache.ambari.server.utils.StageUtils.getClusterHostInfoKey(component));
    }

    static {
        put2componentToClusterInfoKeyMap("NAMENODE");
        put2componentToClusterInfoKeyMap("JOBTRACKER");
        put2componentToClusterInfoKeyMap("SECONDARY_NAMENODE");
        put2componentToClusterInfoKeyMap("RESOURCEMANAGER");
        put2componentToClusterInfoKeyMap("NODEMANAGER");
        put2componentToClusterInfoKeyMap("HISTORYSERVER");
        put2componentToClusterInfoKeyMap("JOURNALNODE");
        put2componentToClusterInfoKeyMap("ZKFC");
        put2componentToClusterInfoKeyMap("ZOOKEEPER_SERVER");
        put2componentToClusterInfoKeyMap("FLUME_HANDLER");
        put2componentToClusterInfoKeyMap("HBASE_MASTER");
        put2componentToClusterInfoKeyMap("HBASE_REGIONSERVER");
        put2componentToClusterInfoKeyMap("HIVE_SERVER");
        put2componentToClusterInfoKeyMap("HIVE_METASTORE");
        put2componentToClusterInfoKeyMap("OOZIE_SERVER");
        put2componentToClusterInfoKeyMap("WEBHCAT_SERVER");
        put2componentToClusterInfoKeyMap("MYSQL_SERVER");
        put2componentToClusterInfoKeyMap("DASHBOARD");
        put2componentToClusterInfoKeyMap("GANGLIA_SERVER");
        put2componentToClusterInfoKeyMap("DATANODE");
        put2componentToClusterInfoKeyMap("TASKTRACKER");
        put2componentToClusterInfoKeyMap("ACCUMULO_MASTER");
        put2componentToClusterInfoKeyMap("ACCUMULO_MONITOR");
        put2componentToClusterInfoKeyMap("ACCUMULO_GC");
        put2componentToClusterInfoKeyMap("ACCUMULO_TRACER");
        put2componentToClusterInfoKeyMap("ACCUMULO_TSERVER");
    }

    public static java.lang.String getActionId(long requestId, long stageId) {
        return (requestId + "-") + stageId;
    }

    public static long[] getRequestStage(java.lang.String actionId) {
        java.lang.String[] fields = actionId.split("-");
        long[] requestStageIds = new long[2];
        requestStageIds[0] = java.lang.Long.parseLong(fields[0]);
        requestStageIds[1] = java.lang.Long.parseLong(fields[1]);
        return requestStageIds;
    }

    public static org.apache.ambari.server.actionmanager.Stage getATestStage(long requestId, long stageId, java.lang.String commandParamsStage, java.lang.String hostParamsStage) {
        java.lang.String hostname;
        try {
            hostname = java.net.InetAddress.getLocalHost().getHostName();
        } catch (java.net.UnknownHostException e) {
            hostname = "host-dummy";
        }
        return org.apache.ambari.server.utils.StageUtils.getATestStage(requestId, stageId, hostname, commandParamsStage, hostParamsStage);
    }

    @com.google.inject.Inject
    public static org.apache.ambari.server.actionmanager.Stage getATestStage(long requestId, long stageId, java.lang.String hostname, java.lang.String commandParamsStage, java.lang.String hostParamsStage) {
        org.apache.ambari.server.actionmanager.Stage s = org.apache.ambari.server.utils.StageUtils.stageFactory.createNew(requestId, "/tmp", "cluster1", 1L, "context", commandParamsStage, hostParamsStage);
        s.setStageId(stageId);
        long now = java.lang.System.currentTimeMillis();
        s.addHostRoleExecutionCommand(hostname, org.apache.ambari.server.Role.NAMENODE, org.apache.ambari.server.RoleCommand.INSTALL, new org.apache.ambari.server.state.svccomphost.ServiceComponentHostInstallEvent("NAMENODE", hostname, now, "HDP-1.2.0"), "cluster1", "HDFS", false, false);
        org.apache.ambari.server.agent.ExecutionCommand execCmd = s.getExecutionCommandWrapper(hostname, "NAMENODE").getExecutionCommand();
        execCmd.setRequestAndStage(s.getRequestId(), s.getStageId());
        java.util.List<java.lang.String> slaveHostList = new java.util.ArrayList<>();
        slaveHostList.add(hostname);
        slaveHostList.add("host2");
        java.util.Map<java.lang.String, java.lang.String> hdfsSite = new java.util.TreeMap<>();
        hdfsSite.put("dfs.block.size", "2560000000");
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> configurations = new java.util.TreeMap<>();
        configurations.put("hdfs-site", hdfsSite);
        execCmd.setConfigurations(configurations);
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>>> configurationAttributes = new java.util.TreeMap<>();
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> hdfsSiteAttributes = new java.util.TreeMap<>();
        java.util.Map<java.lang.String, java.lang.String> finalAttribute = new java.util.TreeMap<>();
        finalAttribute.put("dfs.block.size", "true");
        hdfsSiteAttributes.put("final", finalAttribute);
        configurationAttributes.put("hdfsSite", hdfsSiteAttributes);
        java.util.Map<java.lang.String, java.lang.String> params = new java.util.TreeMap<>();
        params.put("jdklocation", "/x/y/z");
        params.put("stack_version", "1.2.0");
        params.put("stack_name", "HDP");
        execCmd.setHostLevelParams(params);
        java.util.Map<java.lang.String, java.lang.String> roleParams = new java.util.TreeMap<>();
        roleParams.put("format", "false");
        execCmd.setRoleParams(roleParams);
        java.util.Map<java.lang.String, java.lang.String> commandParams = new java.util.TreeMap<>();
        commandParams.put(org.apache.ambari.server.agent.ExecutionCommand.KeyNames.COMMAND_TIMEOUT, "600");
        execCmd.setCommandParams(commandParams);
        return s;
    }

    public static java.lang.String jaxbToString(java.lang.Object jaxbObj) throws javax.xml.bind.JAXBException, org.codehaus.jackson.JsonGenerationException, org.codehaus.jackson.map.JsonMappingException, java.io.IOException {
        return org.apache.ambari.server.utils.StageUtils.getGson().toJson(jaxbObj);
    }

    public static <T> T fromJson(java.lang.String json, java.lang.Class<T> clazz) throws java.io.IOException {
        org.codehaus.jackson.map.ObjectMapper mapper = new org.codehaus.jackson.map.ObjectMapper();
        mapper.configure(SerializationConfig.Feature.INDENT_OUTPUT, true);
        mapper.configure(SerializationConfig.Feature.USE_ANNOTATIONS, true);
        java.io.InputStream is = new java.io.ByteArrayInputStream(json.getBytes(java.nio.charset.Charset.forName("UTF8")));
        return mapper.readValue(is, clazz);
    }

    public static java.util.Map<java.lang.String, java.lang.String> getCommandParamsStage(org.apache.ambari.server.controller.ActionExecutionContext actionExecContext, java.lang.String requestContext) throws org.apache.ambari.server.AmbariException {
        java.util.Map<java.lang.String, java.lang.String> commandParams = (actionExecContext.getParameters() != null) ? actionExecContext.getParameters() : new java.util.TreeMap<>();
        if (org.apache.commons.lang.StringUtils.isNotEmpty(requestContext) && requestContext.toLowerCase().contains("rolling-restart")) {
            commandParams.put("rolling_restart", "true");
        }
        return commandParams;
    }

    public static java.lang.String getClusterHostInfoKey(java.lang.String componentName) {
        if (componentName == null) {
            throw new java.lang.IllegalArgumentException("Component name cannot be null");
        }
        return componentName.toLowerCase() + "_hosts";
    }

    public static java.util.Map<java.lang.String, java.util.Set<java.lang.String>> getClusterHostInfo(org.apache.ambari.server.state.Cluster cluster) throws org.apache.ambari.server.AmbariException {
        java.util.Set<java.lang.String> hostsSet = new java.util.LinkedHashSet<>();
        java.util.List<java.lang.Integer> portsList = new java.util.ArrayList<>();
        java.util.List<java.lang.String> rackList = new java.util.ArrayList<>();
        java.util.List<java.lang.String> ipV4List = new java.util.ArrayList<>();
        java.util.Collection<org.apache.ambari.server.state.Host> allHosts = cluster.getHosts();
        for (org.apache.ambari.server.state.Host host : allHosts) {
            hostsSet.add(host.getHostName());
            java.lang.Integer currentPingPort = host.getCurrentPingPort();
            portsList.add(currentPingPort == null ? org.apache.ambari.server.utils.StageUtils.DEFAULT_PING_PORT : currentPingPort);
            java.lang.String rackInfo = host.getRackInfo();
            rackList.add(org.apache.commons.lang.StringUtils.isEmpty(rackInfo) ? org.apache.ambari.server.utils.StageUtils.DEFAULT_RACK : rackInfo);
            java.lang.String iPv4 = host.getIPv4();
            ipV4List.add(org.apache.commons.lang.StringUtils.isEmpty(iPv4) ? org.apache.ambari.server.utils.StageUtils.DEFAULT_IPV4_ADDRESS : iPv4);
        }
        java.util.Map<java.lang.String, java.util.Collection<java.lang.String>> pendingHostComponents = org.apache.ambari.server.utils.StageUtils.topologyManager.getPendingHostComponents();
        for (java.lang.String hostname : pendingHostComponents.keySet()) {
            if (!hostsSet.contains(hostname)) {
                hostsSet.add(hostname);
                portsList.add(org.apache.ambari.server.utils.StageUtils.DEFAULT_PING_PORT);
                rackList.add(org.apache.ambari.server.utils.StageUtils.DEFAULT_RACK);
                ipV4List.add(org.apache.ambari.server.utils.StageUtils.DEFAULT_IPV4_ADDRESS);
            }
        }
        java.util.List<java.lang.String> hostsList = new java.util.ArrayList<>(hostsSet);
        java.util.Map<java.lang.String, java.lang.String> additionalComponentToClusterInfoKeyMap = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.util.SortedSet<java.lang.Integer>> hostRolesInfo = new java.util.HashMap<>();
        for (java.util.Map.Entry<java.lang.String, org.apache.ambari.server.state.Service> serviceEntry : cluster.getServices().entrySet()) {
            org.apache.ambari.server.state.Service service = serviceEntry.getValue();
            for (java.util.Map.Entry<java.lang.String, org.apache.ambari.server.state.ServiceComponent> serviceComponentEntry : service.getServiceComponents().entrySet()) {
                org.apache.ambari.server.state.ServiceComponent serviceComponent = serviceComponentEntry.getValue();
                java.lang.String componentName = serviceComponent.getName();
                java.lang.String roleName = org.apache.ambari.server.utils.StageUtils.componentToClusterInfoKeyMap.get(componentName);
                if (null == roleName) {
                    roleName = additionalComponentToClusterInfoKeyMap.get(componentName);
                }
                if ((null == roleName) && (!serviceComponent.isClientComponent())) {
                    roleName = componentName.toLowerCase() + "_hosts";
                    additionalComponentToClusterInfoKeyMap.put(componentName, roleName);
                }
                if (roleName == null) {
                    continue;
                }
                for (java.lang.String hostName : serviceComponent.getServiceComponentHosts().keySet()) {
                    java.util.SortedSet<java.lang.Integer> hostsForComponentsHost = hostRolesInfo.get(roleName);
                    if (hostsForComponentsHost == null) {
                        hostsForComponentsHost = new java.util.TreeSet<>();
                        hostRolesInfo.put(roleName, hostsForComponentsHost);
                    }
                    int hostIndex = hostsList.indexOf(hostName);
                    hostsForComponentsHost.add(hostIndex);
                }
            }
        }
        for (java.util.Map.Entry<java.lang.String, java.util.Collection<java.lang.String>> entry : pendingHostComponents.entrySet()) {
            java.lang.String hostname = entry.getKey();
            java.util.Collection<java.lang.String> hostComponents = entry.getValue();
            for (java.lang.String hostComponent : hostComponents) {
                java.lang.String roleName = org.apache.ambari.server.utils.StageUtils.getClusterHostInfoKey(hostComponent);
                java.util.SortedSet<java.lang.Integer> hostsForComponentsHost = hostRolesInfo.get(roleName);
                if (hostsForComponentsHost == null) {
                    hostsForComponentsHost = new java.util.TreeSet<>();
                    hostRolesInfo.put(roleName, hostsForComponentsHost);
                }
                int hostIndex = hostsList.indexOf(hostname);
                if (hostIndex != (-1)) {
                    if (!hostsForComponentsHost.contains(hostIndex)) {
                        hostsForComponentsHost.add(hostIndex);
                    }
                } else {
                    throw new java.lang.RuntimeException("Unable to get host index for host: " + hostname);
                }
            }
        }
        java.util.Map<java.lang.String, java.util.Set<java.lang.String>> clusterHostInfo = new java.util.HashMap<>();
        for (java.util.Map.Entry<java.lang.String, java.util.SortedSet<java.lang.Integer>> entry : hostRolesInfo.entrySet()) {
            java.util.TreeSet<java.lang.Integer> sortedSet = new java.util.TreeSet<>(entry.getValue());
            java.util.Set<java.lang.String> replacedRangesSet = org.apache.ambari.server.utils.StageUtils.replaceRanges(sortedSet);
            clusterHostInfo.put(entry.getKey(), replacedRangesSet);
        }
        clusterHostInfo.put(org.apache.ambari.server.utils.StageUtils.HOSTS_LIST, hostsSet);
        clusterHostInfo.put(org.apache.ambari.server.utils.StageUtils.PORTS, org.apache.ambari.server.utils.StageUtils.replaceMappedRanges(portsList));
        clusterHostInfo.put(org.apache.ambari.server.utils.StageUtils.IPV4_ADDRESSES, org.apache.ambari.server.utils.StageUtils.replaceMappedRanges(ipV4List));
        clusterHostInfo.put(org.apache.ambari.server.utils.StageUtils.RACKS, org.apache.ambari.server.utils.StageUtils.replaceMappedRanges(rackList));
        clusterHostInfo.put(org.apache.ambari.server.utils.StageUtils.AMBARI_SERVER_HOST, com.google.common.collect.Sets.newHashSet(org.apache.ambari.server.utils.StageUtils.getHostName()));
        boolean serverUseSsl = org.apache.ambari.server.utils.StageUtils.configuration.getApiSSLAuthentication();
        int port = (serverUseSsl) ? org.apache.ambari.server.utils.StageUtils.configuration.getClientSSLApiPort() : org.apache.ambari.server.utils.StageUtils.configuration.getClientApiPort();
        clusterHostInfo.put(org.apache.ambari.server.utils.StageUtils.AMBARI_SERVER_PORT, com.google.common.collect.Sets.newHashSet(java.lang.Integer.toString(port)));
        clusterHostInfo.put(org.apache.ambari.server.utils.StageUtils.AMBARI_SERVER_USE_SSL, com.google.common.collect.Sets.newHashSet(java.lang.Boolean.toString(serverUseSsl)));
        return clusterHostInfo;
    }

    public static java.util.Map<java.lang.String, java.util.Set<java.lang.String>> substituteHostIndexes(java.util.Map<java.lang.String, java.util.Set<java.lang.String>> clusterHostInfo) throws org.apache.ambari.server.AmbariException {
        java.util.Set<java.lang.String> keysToSkip = new java.util.HashSet<>(java.util.Arrays.asList(org.apache.ambari.server.utils.StageUtils.HOSTS_LIST, org.apache.ambari.server.utils.StageUtils.PORTS, org.apache.ambari.server.utils.StageUtils.AMBARI_SERVER_HOST, org.apache.ambari.server.utils.StageUtils.AMBARI_SERVER_PORT, org.apache.ambari.server.utils.StageUtils.AMBARI_SERVER_USE_SSL, org.apache.ambari.server.utils.StageUtils.RACKS, org.apache.ambari.server.utils.StageUtils.IPV4_ADDRESSES));
        java.lang.String[] allHosts = new java.lang.String[]{  };
        if (clusterHostInfo.get(org.apache.ambari.server.utils.StageUtils.HOSTS_LIST) != null) {
            allHosts = clusterHostInfo.get(org.apache.ambari.server.utils.StageUtils.HOSTS_LIST).toArray(new java.lang.String[clusterHostInfo.get(org.apache.ambari.server.utils.StageUtils.HOSTS_LIST).size()]);
        }
        java.util.Set<java.lang.String> keys = clusterHostInfo.keySet();
        for (java.lang.String key : keys) {
            if (keysToSkip.contains(key)) {
                continue;
            }
            java.util.Set<java.lang.String> hosts = new java.util.HashSet<>();
            java.util.Set<java.lang.String> currentHostsIndexes = clusterHostInfo.get(key);
            if (currentHostsIndexes == null) {
                continue;
            }
            for (java.lang.String hostIndexRange : currentHostsIndexes) {
                for (java.lang.Integer hostIndex : org.apache.ambari.server.utils.StageUtils.rangeToSet(hostIndexRange)) {
                    try {
                        hosts.add(allHosts[hostIndex]);
                    } catch (java.lang.ArrayIndexOutOfBoundsException ex) {
                        throw new org.apache.ambari.server.AmbariException("Failed to fill cluster host info  ", ex);
                    }
                }
            }
            clusterHostInfo.put(key, hosts);
        }
        return clusterHostInfo;
    }

    public static java.util.Set<java.lang.String> replaceRanges(java.util.SortedSet<java.lang.Integer> set) {
        if (set == null) {
            return null;
        }
        java.util.Set<java.lang.String> rangedSet = new java.util.HashSet<>();
        java.lang.Integer prevElement = null;
        java.lang.Integer startOfRange = set.first();
        for (java.lang.Integer i : set) {
            if ((prevElement != null) && ((i - prevElement) > 1)) {
                java.lang.String rangeItem = org.apache.ambari.server.utils.StageUtils.getRangedItem(startOfRange, prevElement);
                rangedSet.add(rangeItem);
                startOfRange = i;
            }
            prevElement = i;
        }
        rangedSet.add(org.apache.ambari.server.utils.StageUtils.getRangedItem(startOfRange, prevElement));
        return rangedSet;
    }

    public static <T> java.util.Set<java.lang.String> replaceMappedRanges(java.util.List<T> values) {
        java.util.Map<T, java.util.SortedSet<java.lang.Integer>> convolutedValues = new java.util.HashMap<>();
        int valueIndex = 0;
        for (T value : values) {
            java.util.SortedSet<java.lang.Integer> correspValues = convolutedValues.get(value);
            if (correspValues == null) {
                correspValues = new java.util.TreeSet<>();
                convolutedValues.put(value, correspValues);
            }
            correspValues.add(valueIndex);
            valueIndex++;
        }
        java.util.Set<java.lang.String> result = new java.util.HashSet<>();
        for (java.util.Map.Entry<T, java.util.SortedSet<java.lang.Integer>> entry : convolutedValues.entrySet()) {
            java.util.Set<java.lang.String> replacedRanges = org.apache.ambari.server.utils.StageUtils.replaceRanges(entry.getValue());
            result.add((entry.getKey() + ":") + com.google.common.base.Joiner.on(",").join(replacedRanges));
        }
        return result;
    }

    public static java.lang.String getHostName() {
        return org.apache.ambari.server.utils.StageUtils.server_hostname;
    }

    private static java.util.Set<java.lang.Integer> rangeToSet(java.lang.String range) {
        java.util.Set<java.lang.Integer> indexSet = new java.util.HashSet<>();
        int startIndex;
        int endIndex;
        if (range.contains("-")) {
            startIndex = java.lang.Integer.parseInt(range.split("-")[0]);
            endIndex = java.lang.Integer.parseInt(range.split("-")[1]);
        } else if (range.contains(",")) {
            startIndex = java.lang.Integer.parseInt(range.split(",")[0]);
            endIndex = java.lang.Integer.parseInt(range.split(",")[1]);
        } else {
            startIndex = endIndex = java.lang.Integer.parseInt(range);
        }
        for (int i = startIndex; i <= endIndex; i++) {
            indexSet.add(i);
        }
        return indexSet;
    }

    private static java.lang.String getRangedItem(java.lang.Integer startOfRange, java.lang.Integer endOfRange) {
        java.lang.String separator = ((endOfRange - startOfRange) > 1) ? "-" : ",";
        java.lang.String rangeItem = (endOfRange.equals(startOfRange)) ? endOfRange.toString() : (startOfRange + separator) + endOfRange;
        return rangeItem;
    }

    public static void useAmbariJdkInCommandParams(java.util.Map<java.lang.String, java.lang.String> commandParams, org.apache.ambari.server.configuration.Configuration configuration) {
        if (org.apache.commons.lang.StringUtils.isNotEmpty(configuration.getJavaHome()) && (!configuration.getJavaHome().equals(configuration.getStackJavaHome()))) {
            commandParams.put(org.apache.ambari.server.agent.ExecutionCommand.KeyNames.AMBARI_JAVA_HOME, configuration.getJavaHome());
            commandParams.put(org.apache.ambari.server.agent.ExecutionCommand.KeyNames.AMBARI_JAVA_VERSION, java.lang.String.valueOf(configuration.getJavaVersion()));
            if (org.apache.commons.lang.StringUtils.isNotEmpty(configuration.getJDKName())) {
                commandParams.put(org.apache.ambari.server.agent.ExecutionCommand.KeyNames.AMBARI_JDK_NAME, configuration.getJDKName());
            }
            if (org.apache.commons.lang.StringUtils.isNotEmpty(configuration.getJCEName())) {
                commandParams.put(org.apache.ambari.server.agent.ExecutionCommand.KeyNames.AMBARI_JCE_NAME, configuration.getJCEName());
            }
        }
    }

    public static void useStackJdkIfExists(java.util.Map<java.lang.String, java.lang.String> hostLevelParams, org.apache.ambari.server.configuration.Configuration configuration) {
        hostLevelParams.put(org.apache.ambari.server.agent.ExecutionCommand.KeyNames.JAVA_HOME, configuration.getJavaHome());
        hostLevelParams.put(org.apache.ambari.server.agent.ExecutionCommand.KeyNames.JDK_NAME, configuration.getJDKName());
        hostLevelParams.put(org.apache.ambari.server.agent.ExecutionCommand.KeyNames.JCE_NAME, configuration.getJCEName());
        hostLevelParams.put(org.apache.ambari.server.agent.ExecutionCommand.KeyNames.JAVA_VERSION, java.lang.String.valueOf(configuration.getJavaVersion()));
        if (org.apache.commons.lang.StringUtils.isNotEmpty(configuration.getStackJavaHome()) && (!configuration.getStackJavaHome().equals(configuration.getJavaHome()))) {
            hostLevelParams.put(org.apache.ambari.server.agent.ExecutionCommand.KeyNames.JAVA_HOME, configuration.getStackJavaHome());
            if (org.apache.commons.lang.StringUtils.isNotEmpty(configuration.getStackJavaVersion())) {
                hostLevelParams.put(org.apache.ambari.server.agent.ExecutionCommand.KeyNames.JAVA_VERSION, configuration.getStackJavaVersion());
            }
            if (org.apache.commons.lang.StringUtils.isNotEmpty(configuration.getStackJDKName())) {
                hostLevelParams.put(org.apache.ambari.server.agent.ExecutionCommand.KeyNames.JDK_NAME, configuration.getStackJDKName());
            } else {
                hostLevelParams.put(org.apache.ambari.server.agent.ExecutionCommand.KeyNames.JDK_NAME, null);
            }
            if (org.apache.commons.lang.StringUtils.isNotEmpty(configuration.getStackJCEName())) {
                hostLevelParams.put(org.apache.ambari.server.agent.ExecutionCommand.KeyNames.JCE_NAME, configuration.getStackJCEName());
            } else {
                hostLevelParams.put(org.apache.ambari.server.agent.ExecutionCommand.KeyNames.JCE_NAME, null);
            }
        }
    }

    public static java.util.Map<java.lang.String, java.lang.String> createComponentHostMap(java.util.Collection<java.lang.String> services, java.util.function.Function<java.lang.String, java.util.Collection<java.lang.String>> componentsLookup, java.util.function.BiFunction<java.lang.String, java.lang.String, java.util.Collection<java.lang.String>> hostAssignmentLookup) {
        java.util.Map<java.lang.String, java.lang.String> componentHostsMap = new java.util.HashMap<>();
        for (java.lang.String service : services) {
            java.util.Collection<java.lang.String> components = componentsLookup.apply(service);
            for (java.lang.String component : components) {
                java.util.Collection<java.lang.String> hosts = hostAssignmentLookup.apply(service, component);
                componentHostsMap.put(org.apache.ambari.server.utils.StageUtils.getClusterHostInfoKey(component), org.apache.commons.lang.StringUtils.join(hosts, ","));
            }
        }
        return componentHostsMap;
    }
}