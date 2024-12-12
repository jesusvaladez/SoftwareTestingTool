package org.apache.ambari.server.stack;
import org.apache.commons.lang.StringUtils;
@org.apache.ambari.annotations.Experimental(feature = org.apache.ambari.annotations.ExperimentalFeature.REFACTOR_TO_SPI)
public class MasterHostResolver {
    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.stack.MasterHostResolver.class);

    private final org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeContext m_upgradeContext;

    private final org.apache.ambari.server.state.Cluster m_cluster;

    private final org.apache.ambari.server.state.ConfigHelper m_configHelper;

    public enum Service {

        HDFS,
        HBASE,
        YARN,
        OTHER;
        public static org.apache.ambari.server.stack.MasterHostResolver.Service fromString(java.lang.String serviceName) {
            try {
                return org.apache.ambari.server.stack.MasterHostResolver.Service.valueOf(serviceName.toUpperCase());
            } catch (java.lang.Exception ignore) {
                return org.apache.ambari.server.stack.MasterHostResolver.Service.OTHER;
            }
        }
    }

    protected enum Status {

        ACTIVE,
        STANDBY;}

    public MasterHostResolver(org.apache.ambari.server.state.Cluster cluster, org.apache.ambari.server.state.ConfigHelper configHelper, org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeContext upgradeContext) {
        m_configHelper = configHelper;
        m_upgradeContext = upgradeContext;
        m_cluster = cluster;
    }

    public org.apache.ambari.server.state.Cluster getCluster() {
        return m_cluster;
    }

    public org.apache.ambari.server.stack.HostsType getMasterAndHosts(java.lang.String serviceName, java.lang.String componentName) {
        if ((serviceName == null) || (componentName == null)) {
            return null;
        }
        java.util.LinkedHashSet<java.lang.String> componentHosts = new java.util.LinkedHashSet<>(m_cluster.getHosts(serviceName, componentName));
        if (componentHosts.isEmpty()) {
            return null;
        }
        org.apache.ambari.server.stack.HostsType hostsType = org.apache.ambari.server.stack.HostsType.normal(componentHosts);
        try {
            switch (org.apache.ambari.server.stack.MasterHostResolver.Service.fromString(serviceName)) {
                case HDFS :
                    if (componentName.equalsIgnoreCase("NAMENODE") && (componentHosts.size() >= 2)) {
                        try {
                            hostsType = org.apache.ambari.server.stack.HostsType.federated(nameSpaces(componentHosts), componentHosts);
                        } catch (org.apache.ambari.server.stack.ClassifyNameNodeException | java.lang.IllegalArgumentException e) {
                            if (componentHosts.size() == 2) {
                                hostsType = org.apache.ambari.server.stack.HostsType.guessHighAvailability(componentHosts);
                                org.apache.ambari.server.stack.MasterHostResolver.LOG.warn("Could not determine the active/standby states from NameNodes {}. Using {} as active and {} as standbys.", componentHosts, hostsType.getMasters(), hostsType.getSecondaries());
                            } else {
                                org.apache.ambari.server.stack.MasterHostResolver.LOG.warn("Could not determine the active/standby states of federated NameNode from NameNodes {}.", componentHosts);
                            }
                        }
                    }
                    break;
                case YARN :
                    if (componentName.equalsIgnoreCase("RESOURCEMANAGER")) {
                        hostsType = resolveResourceManagers(getCluster(), componentHosts);
                    }
                    break;
                case HBASE :
                    if (componentName.equalsIgnoreCase("HBASE_MASTER")) {
                        hostsType = resolveHBaseMasters(getCluster(), componentHosts);
                    }
                    break;
                default :
                    break;
            }
        } catch (java.lang.Exception err) {
            org.apache.ambari.server.stack.MasterHostResolver.LOG.error((("Unable to get master and hosts for Component " + componentName) + ". Error: ") + err.getMessage(), err);
        }
        return filterHosts(hostsType, serviceName, componentName);
    }

    public static java.util.Collection<org.apache.ambari.server.state.Host> getCandidateHosts(org.apache.ambari.server.state.Cluster cluster, org.apache.ambari.server.stack.upgrade.ExecuteHostType executeHostType, java.lang.String serviceName, java.lang.String componentName) {
        java.util.Collection<org.apache.ambari.server.state.Host> candidates = cluster.getHosts();
        if (org.apache.commons.lang.StringUtils.isNotBlank(serviceName) && org.apache.commons.lang.StringUtils.isNotBlank(componentName)) {
            java.util.List<org.apache.ambari.server.state.ServiceComponentHost> schs = cluster.getServiceComponentHosts(serviceName, componentName);
            candidates = schs.stream().map(sch -> sch.getHost()).collect(java.util.stream.Collectors.toList());
        }
        if (candidates.isEmpty()) {
            return candidates;
        }
        java.util.List<org.apache.ambari.server.state.Host> winners = com.google.common.collect.Lists.newArrayList();
        switch (executeHostType) {
            case ALL :
                winners.addAll(candidates);
                break;
            case FIRST :
                winners.add(candidates.iterator().next());
                break;
            case MASTER :
                winners.add(candidates.iterator().next());
                break;
            case ANY :
                winners.add(candidates.iterator().next());
                break;
        }
        return winners;
    }

    private org.apache.ambari.server.stack.HostsType filterHosts(org.apache.ambari.server.stack.HostsType hostsType, java.lang.String service, java.lang.String component) {
        try {
            org.apache.ambari.server.state.Service svc = m_cluster.getService(service);
            org.apache.ambari.server.state.ServiceComponent sc = svc.getServiceComponent(component);
            java.util.List<org.apache.ambari.server.state.ServiceComponentHost> unhealthyHosts = new java.util.ArrayList<>();
            java.util.LinkedHashSet<java.lang.String> upgradeHosts = new java.util.LinkedHashSet<>();
            for (java.lang.String hostName : hostsType.getHosts()) {
                org.apache.ambari.server.state.ServiceComponentHost sch = sc.getServiceComponentHost(hostName);
                org.apache.ambari.server.state.Host host = sch.getHost();
                org.apache.ambari.server.state.MaintenanceState maintenanceState = host.getMaintenanceState(sch.getClusterId());
                if (maintenanceState != org.apache.ambari.server.state.MaintenanceState.OFF) {
                    unhealthyHosts.add(sch);
                    continue;
                }
                if (sch.getUpgradeState() == org.apache.ambari.server.state.UpgradeState.FAILED) {
                    upgradeHosts.add(hostName);
                    continue;
                }
                if (m_upgradeContext.getDirection() == org.apache.ambari.server.stack.upgrade.Direction.UPGRADE) {
                    org.apache.ambari.server.orm.entities.RepositoryVersionEntity targetRepositoryVersion = m_upgradeContext.getRepositoryVersion();
                    if (!org.apache.commons.lang.StringUtils.equals(targetRepositoryVersion.getVersion(), sch.getVersion())) {
                        upgradeHosts.add(hostName);
                    }
                    continue;
                }
                org.apache.ambari.server.orm.entities.RepositoryVersionEntity downgradeToRepositoryVersion = m_upgradeContext.getTargetRepositoryVersion(service);
                java.lang.String downgradeToVersion = downgradeToRepositoryVersion.getVersion();
                if (!org.apache.commons.lang.StringUtils.equals(downgradeToVersion, sch.getVersion())) {
                    upgradeHosts.add(hostName);
                    continue;
                }
            }
            hostsType.unhealthy = unhealthyHosts;
            hostsType.setHosts(upgradeHosts);
            return hostsType;
        } catch (org.apache.ambari.server.AmbariException e) {
            org.apache.ambari.server.stack.MasterHostResolver.LOG.warn("Could not determine host components to upgrade. Defaulting to saved hosts.", e);
            return hostsType;
        }
    }

    public boolean isNameNodeHA() throws org.apache.ambari.server.AmbariException {
        java.util.Map<java.lang.String, org.apache.ambari.server.state.Service> services = m_cluster.getServices();
        if ((services != null) && services.containsKey("HDFS")) {
            java.util.Set<java.lang.String> secondaryNameNodeHosts = m_cluster.getHosts("HDFS", "SECONDARY_NAMENODE");
            java.util.Set<java.lang.String> nameNodeHosts = m_cluster.getHosts("HDFS", "NAMENODE");
            if ((secondaryNameNodeHosts.size() == 1) && (nameNodeHosts.size() == 1)) {
                return false;
            }
            if (nameNodeHosts.size() > 1) {
                return true;
            }
            throw new org.apache.ambari.server.AmbariException("Unable to determine if cluster has NameNode HA.");
        }
        return false;
    }

    private java.util.List<org.apache.ambari.server.stack.HostsType.HighAvailabilityHosts> nameSpaces(java.util.Set<java.lang.String> componentHosts) {
        return org.apache.ambari.server.stack.NameService.fromConfig(m_configHelper, getCluster()).stream().map(each -> findMasterAndSecondaries(each, componentHosts)).collect(java.util.stream.Collectors.toList());
    }

    public java.lang.String getValueFromDesiredConfigurations(final java.lang.String configType, final java.lang.String propertyName) {
        return m_configHelper.getValueFromDesiredConfigurations(m_cluster, configType, propertyName);
    }

    private org.apache.ambari.server.stack.HostsType.HighAvailabilityHosts findMasterAndSecondaries(org.apache.ambari.server.stack.NameService nameService, java.util.Set<java.lang.String> componentHosts) throws org.apache.ambari.server.stack.ClassifyNameNodeException {
        java.lang.String master = null;
        java.util.List<java.lang.String> secondaries = new java.util.ArrayList<>();
        for (org.apache.ambari.server.stack.NameService.NameNode nameNode : nameService.getNameNodes()) {
            org.apache.ambari.server.stack.MasterHostResolver.checkForDualNetworkCards(componentHosts, nameNode);
            java.lang.String state = queryJmxBeanValue(nameNode.getHost(), nameNode.getPort(), "Hadoop:service=NameNode,name=NameNodeStatus", "State", true, nameNode.isEncrypted());
            if (org.apache.ambari.server.stack.MasterHostResolver.Status.ACTIVE.toString().equalsIgnoreCase(state)) {
                master = nameNode.getHost();
            } else if (org.apache.ambari.server.stack.MasterHostResolver.Status.STANDBY.toString().equalsIgnoreCase(state)) {
                secondaries.add(nameNode.getHost());
            } else {
                org.apache.ambari.server.stack.MasterHostResolver.LOG.error(java.lang.String.format("Could not retrieve state for NameNode %s from property %s by querying JMX.", nameNode.getHost(), nameNode.getPropertyName()));
            }
        }
        if (org.apache.ambari.server.stack.MasterHostResolver.masterAndSecondariesAreFound(componentHosts, master, secondaries)) {
            return new org.apache.ambari.server.stack.HostsType.HighAvailabilityHosts(master, secondaries);
        }
        throw new org.apache.ambari.server.stack.ClassifyNameNodeException(nameService);
    }

    private static void checkForDualNetworkCards(java.util.Set<java.lang.String> componentHosts, org.apache.ambari.server.stack.NameService.NameNode nameNode) {
        if (!componentHosts.contains(nameNode.getHost())) {
            org.apache.ambari.server.stack.MasterHostResolver.LOG.error(java.text.MessageFormat.format("Hadoop NameNode HA configuration {0} contains host {1} that does not exist in the NameNode hosts list {3}", nameNode.getPropertyName(), nameNode.getHost(), componentHosts.toString()));
        }
    }

    private static boolean masterAndSecondariesAreFound(java.util.Set<java.lang.String> componentHosts, java.lang.String master, java.util.List<java.lang.String> secondaries) {
        return ((master != null) && ((secondaries.size() + 1) == componentHosts.size())) && (!secondaries.contains(master));
    }

    private org.apache.ambari.server.utils.HostAndPort parseHostPort(org.apache.ambari.server.state.Cluster cluster, java.lang.String propertyName, java.lang.String configType) throws java.net.MalformedURLException {
        java.lang.String propertyValue = m_configHelper.getValueFromDesiredConfigurations(cluster, configType, propertyName);
        org.apache.ambari.server.utils.HostAndPort hp = org.apache.ambari.server.utils.HTTPUtils.getHostAndPortFromProperty(propertyValue);
        if (hp == null) {
            throw new java.net.MalformedURLException("Could not parse host and port from " + propertyValue);
        }
        return hp;
    }

    private org.apache.ambari.server.stack.HostsType resolveResourceManagers(org.apache.ambari.server.state.Cluster cluster, java.util.Set<java.lang.String> hosts) throws java.net.MalformedURLException {
        java.lang.String master = null;
        java.util.LinkedHashSet<java.lang.String> orderedHosts = new java.util.LinkedHashSet<>(hosts);
        org.apache.ambari.server.utils.HostAndPort hp = parseHostPort(cluster, "yarn.resourcemanager.webapp.address", org.apache.ambari.server.state.ConfigHelper.YARN_SITE);
        for (java.lang.String hostname : hosts) {
            java.lang.String value = queryJmxBeanValue(hostname, hp.port, "Hadoop:service=ResourceManager,name=RMNMInfo", "modelerType", true);
            if (null != value) {
                if (master != null) {
                    master = hostname.toLowerCase();
                }
                orderedHosts.remove(hostname.toLowerCase());
                orderedHosts.add(hostname.toLowerCase());
            }
        }
        return org.apache.ambari.server.stack.HostsType.from(master, null, orderedHosts);
    }

    private org.apache.ambari.server.stack.HostsType resolveHBaseMasters(org.apache.ambari.server.state.Cluster cluster, java.util.Set<java.lang.String> hosts) throws org.apache.ambari.server.AmbariException {
        java.lang.String master = null;
        java.lang.String secondary = null;
        java.lang.String hbaseMasterInfoPortProperty = "hbase.master.info.port";
        java.lang.String hbaseMasterInfoPortValue = m_configHelper.getValueFromDesiredConfigurations(cluster, org.apache.ambari.server.state.ConfigHelper.HBASE_SITE, hbaseMasterInfoPortProperty);
        if ((hbaseMasterInfoPortValue == null) || hbaseMasterInfoPortValue.isEmpty()) {
            throw new org.apache.ambari.server.AmbariException("Could not find property " + hbaseMasterInfoPortProperty);
        }
        final int hbaseMasterInfoPort = java.lang.Integer.parseInt(hbaseMasterInfoPortValue);
        for (java.lang.String hostname : hosts) {
            java.lang.String value = queryJmxBeanValue(hostname, hbaseMasterInfoPort, "Hadoop:service=HBase,name=Master,sub=Server", "tag.isActiveMaster", false);
            if (null != value) {
                java.lang.Boolean bool = java.lang.Boolean.valueOf(value);
                if (bool.booleanValue()) {
                    master = hostname.toLowerCase();
                } else {
                    secondary = hostname.toLowerCase();
                }
            }
        }
        return org.apache.ambari.server.stack.HostsType.from(master, secondary, new java.util.LinkedHashSet<>(hosts));
    }

    protected java.lang.String queryJmxBeanValue(java.lang.String hostname, int port, java.lang.String beanName, java.lang.String attributeName, boolean asQuery) {
        return queryJmxBeanValue(hostname, port, beanName, attributeName, asQuery, false);
    }

    protected java.lang.String queryJmxBeanValue(java.lang.String hostname, int port, java.lang.String beanName, java.lang.String attributeName, boolean asQuery, boolean encrypted) {
        java.lang.String protocol = (encrypted) ? "https://" : "http://";
        java.lang.String endPoint = protocol + (asQuery ? java.lang.String.format("%s:%s/jmx?qry=%s", hostname, port, beanName) : java.lang.String.format("%s:%s/jmx?get=%s::%s", hostname, port, beanName, attributeName));
        java.lang.String response = org.apache.ambari.server.utils.HTTPUtils.requestURL(endPoint);
        if ((null == response) || response.isEmpty()) {
            return null;
        }
        java.lang.reflect.Type type = new com.google.common.reflect.TypeToken<java.util.Map<java.lang.String, java.util.ArrayList<java.util.HashMap<java.lang.String, java.lang.String>>>>() {}.getType();
        try {
            java.util.Map<java.lang.String, java.util.ArrayList<java.util.HashMap<java.lang.String, java.lang.String>>> jmxBeans = org.apache.ambari.server.utils.StageUtils.getGson().fromJson(response, type);
            return jmxBeans.get("beans").get(0).get(attributeName);
        } catch (java.lang.Exception e) {
            if (org.apache.ambari.server.stack.MasterHostResolver.LOG.isDebugEnabled()) {
                org.apache.ambari.server.stack.MasterHostResolver.LOG.debug("Could not load JMX from {}/{} from {}", beanName, attributeName, hostname, e);
            } else {
                org.apache.ambari.server.stack.MasterHostResolver.LOG.debug("Could not load JMX from {}/{} from {}", beanName, attributeName, hostname);
            }
        }
        return null;
    }
}