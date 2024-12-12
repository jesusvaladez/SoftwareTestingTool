package org.apache.ambari.server.controller;
import com.google.inject.persist.Transactional;
import javax.annotation.Nullable;
import javax.persistence.RollbackException;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.http.HttpStatus;
import org.apache.http.client.utils.URIBuilder;
import static org.apache.ambari.server.agent.ExecutionCommand.KeyNames.AGENT_STACK_RETRY_COUNT;
import static org.apache.ambari.server.agent.ExecutionCommand.KeyNames.AGENT_STACK_RETRY_ON_UNAVAILABILITY;
import static org.apache.ambari.server.agent.ExecutionCommand.KeyNames.AMBARI_DB_RCA_DRIVER;
import static org.apache.ambari.server.agent.ExecutionCommand.KeyNames.AMBARI_DB_RCA_PASSWORD;
import static org.apache.ambari.server.agent.ExecutionCommand.KeyNames.AMBARI_DB_RCA_URL;
import static org.apache.ambari.server.agent.ExecutionCommand.KeyNames.AMBARI_DB_RCA_USERNAME;
import static org.apache.ambari.server.agent.ExecutionCommand.KeyNames.CLIENTS_TO_UPDATE_CONFIGS;
import static org.apache.ambari.server.agent.ExecutionCommand.KeyNames.CLUSTER_NAME;
import static org.apache.ambari.server.agent.ExecutionCommand.KeyNames.COMMAND_RETRY_ENABLED;
import static org.apache.ambari.server.agent.ExecutionCommand.KeyNames.COMMAND_TIMEOUT;
import static org.apache.ambari.server.agent.ExecutionCommand.KeyNames.CUSTOM_FOLDER;
import static org.apache.ambari.server.agent.ExecutionCommand.KeyNames.DB_DRIVER_FILENAME;
import static org.apache.ambari.server.agent.ExecutionCommand.KeyNames.DB_NAME;
import static org.apache.ambari.server.agent.ExecutionCommand.KeyNames.DFS_TYPE;
import static org.apache.ambari.server.agent.ExecutionCommand.KeyNames.GPL_LICENSE_ACCEPTED;
import static org.apache.ambari.server.agent.ExecutionCommand.KeyNames.GROUP_LIST;
import static org.apache.ambari.server.agent.ExecutionCommand.KeyNames.HOOKS_FOLDER;
import static org.apache.ambari.server.agent.ExecutionCommand.KeyNames.HOST_SYS_PREPPED;
import static org.apache.ambari.server.agent.ExecutionCommand.KeyNames.JAVA_HOME;
import static org.apache.ambari.server.agent.ExecutionCommand.KeyNames.JAVA_VERSION;
import static org.apache.ambari.server.agent.ExecutionCommand.KeyNames.JCE_NAME;
import static org.apache.ambari.server.agent.ExecutionCommand.KeyNames.JDK_LOCATION;
import static org.apache.ambari.server.agent.ExecutionCommand.KeyNames.JDK_NAME;
import static org.apache.ambari.server.agent.ExecutionCommand.KeyNames.MAX_DURATION_OF_RETRIES;
import static org.apache.ambari.server.agent.ExecutionCommand.KeyNames.MYSQL_JDBC_URL;
import static org.apache.ambari.server.agent.ExecutionCommand.KeyNames.NOT_MANAGED_HDFS_PATH_LIST;
import static org.apache.ambari.server.agent.ExecutionCommand.KeyNames.ORACLE_JDBC_URL;
import static org.apache.ambari.server.agent.ExecutionCommand.KeyNames.PACKAGE_LIST;
import static org.apache.ambari.server.agent.ExecutionCommand.KeyNames.SCRIPT;
import static org.apache.ambari.server.agent.ExecutionCommand.KeyNames.SCRIPT_TYPE;
import static org.apache.ambari.server.agent.ExecutionCommand.KeyNames.SERVICE_PACKAGE_FOLDER;
import static org.apache.ambari.server.agent.ExecutionCommand.KeyNames.SERVICE_REPO_INFO;
import static org.apache.ambari.server.agent.ExecutionCommand.KeyNames.STACK_NAME;
import static org.apache.ambari.server.agent.ExecutionCommand.KeyNames.STACK_VERSION;
import static org.apache.ambari.server.agent.ExecutionCommand.KeyNames.USER_GROUPS;
import static org.apache.ambari.server.agent.ExecutionCommand.KeyNames.USER_LIST;
import static org.apache.ambari.server.agent.ExecutionCommand.KeyNames.VERSION;
import static org.apache.ambari.server.controller.AmbariCustomCommandExecutionHelper.masterToSlaveMappingForDecom;
@com.google.inject.Singleton
public class AmbariManagementControllerImpl implements org.apache.ambari.server.controller.AmbariManagementController {
    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.controller.AmbariManagementControllerImpl.class);

    private static final org.slf4j.Logger configChangeLog = org.slf4j.LoggerFactory.getLogger("configchange");

    private static final java.lang.reflect.Type hostAttributesType = new com.google.gson.reflect.TypeToken<java.util.Map<java.lang.String, java.lang.String>>() {}.getType();

    public static final java.lang.String CLUSTER_PHASE_PROPERTY = "phase";

    public static final java.lang.String CLUSTER_PHASE_INITIAL_INSTALL = "INITIAL_INSTALL";

    public static final java.lang.String CLUSTER_PHASE_INITIAL_START = "INITIAL_START";

    private static final java.lang.String AMBARI_SERVER_HOST = "ambari_server_host";

    private static final java.lang.String AMBARI_SERVER_PORT = "ambari_server_port";

    private static final java.lang.String AMBARI_SERVER_USE_SSL = "ambari_server_use_ssl";

    private static final java.lang.String BASE_LOG_DIR = "/tmp/ambari";

    private static final java.lang.String PASSWORD = "password";

    public static final java.lang.String CLUSTER_NAME_VALIDATION_REGEXP = "^[a-zA-Z0-9_-]{1,100}$";

    public static final java.util.regex.Pattern CLUSTER_NAME_PTRN = java.util.regex.Pattern.compile(org.apache.ambari.server.controller.AmbariManagementControllerImpl.CLUSTER_NAME_VALIDATION_REGEXP);

    private final org.apache.ambari.server.state.Clusters clusters;

    private final org.apache.ambari.server.actionmanager.ActionManager actionManager;

    private final com.google.inject.Injector injector;

    private final com.google.gson.Gson gson;

    @com.google.inject.Inject
    private org.apache.ambari.server.metadata.RoleCommandOrderProvider roleCommandOrderProvider;

    @com.google.inject.Inject
    private org.apache.ambari.server.state.ServiceComponentFactory serviceComponentFactory;

    @com.google.inject.Inject
    private org.apache.ambari.server.state.ServiceComponentHostFactory serviceComponentHostFactory;

    @com.google.inject.Inject
    private org.apache.ambari.server.state.ConfigFactory configFactory;

    @com.google.inject.Inject
    private org.apache.ambari.server.actionmanager.StageFactory stageFactory;

    @com.google.inject.Inject
    private org.apache.ambari.server.actionmanager.RequestFactory requestFactory;

    @com.google.inject.Inject
    private org.apache.ambari.server.metadata.ActionMetadata actionMetadata;

    @com.google.inject.Inject
    private org.apache.ambari.server.api.services.AmbariMetaInfo ambariMetaInfo;

    @com.google.inject.Inject
    private org.apache.ambari.server.security.authorization.Users users;

    @com.google.inject.Inject
    private org.apache.ambari.server.controller.HostsMap hostsMap;

    @com.google.inject.Inject
    private org.apache.ambari.server.configuration.Configuration configs;

    @com.google.inject.Inject
    private org.apache.ambari.server.controller.AbstractRootServiceResponseFactory rootServiceResponseFactory;

    @com.google.inject.Inject
    private org.apache.ambari.server.stageplanner.RoleGraphFactory roleGraphFactory;

    @com.google.inject.Inject
    private org.apache.ambari.server.state.configgroup.ConfigGroupFactory configGroupFactory;

    @com.google.inject.Inject
    private org.apache.ambari.server.state.ConfigHelper configHelper;

    @com.google.inject.Inject
    private org.apache.ambari.server.state.scheduler.RequestExecutionFactory requestExecutionFactory;

    @com.google.inject.Inject
    private org.apache.ambari.server.scheduler.ExecutionScheduleManager executionScheduleManager;

    @com.google.inject.Inject
    private org.apache.ambari.server.security.ldap.AmbariLdapDataPopulator ldapDataPopulator;

    @com.google.inject.Inject
    private org.apache.ambari.server.orm.dao.RepositoryVersionDAO repositoryVersionDAO;

    @com.google.inject.Inject
    private org.apache.ambari.server.orm.dao.WidgetDAO widgetDAO;

    @com.google.inject.Inject
    private org.apache.ambari.server.orm.dao.WidgetLayoutDAO widgetLayoutDAO;

    @com.google.inject.Inject
    private org.apache.ambari.server.orm.dao.ClusterDAO clusterDAO;

    @com.google.inject.Inject
    private org.apache.ambari.server.security.encryption.CredentialStoreService credentialStoreService;

    @com.google.inject.Inject
    private org.apache.ambari.server.orm.dao.SettingDAO settingDAO;

    private org.apache.ambari.server.controller.MaintenanceStateHelper maintenanceStateHelper;

    private org.apache.ambari.server.controller.AmbariManagementHelper helper;

    @com.google.inject.Inject
    private org.apache.ambari.server.orm.dao.ExtensionDAO extensionDAO;

    @com.google.inject.Inject
    private org.apache.ambari.server.orm.dao.ExtensionLinkDAO linkDAO;

    @com.google.inject.Inject
    private org.apache.ambari.server.orm.dao.StackDAO stackDAO;

    @com.google.inject.Inject
    protected org.apache.ambari.server.state.stack.OsFamily osFamily;

    @com.google.inject.Inject
    private org.apache.ambari.server.topology.STOMPComponentsDeleteHandler STOMPComponentsDeleteHandler;

    @com.google.inject.Inject
    private com.google.inject.Provider<org.apache.ambari.server.agent.stomp.TopologyHolder> m_topologyHolder;

    @com.google.inject.Inject
    private com.google.inject.Provider<org.apache.ambari.server.agent.stomp.HostLevelParamsHolder> m_hostLevelParamsHolder;

    @com.google.inject.Inject
    private org.apache.ambari.server.orm.dao.ServiceComponentDesiredStateDAO serviceComponentDesiredStateDAO;

    @com.google.inject.Inject
    private org.apache.ambari.server.stack.upgrade.RepositoryVersionHelper repoVersionHelper;

    @com.google.inject.Inject
    private org.apache.ambari.server.orm.dao.HostComponentDesiredStateDAO hostComponentDesiredStateDAO;

    private org.apache.ambari.server.controller.KerberosHelper kerberosHelper;

    private final java.lang.String masterHostname;

    private final java.lang.Integer masterPort;

    private final java.lang.String masterProtocol;

    private static final java.lang.String JDK_RESOURCE_LOCATION = "/resources";

    private static final int REPO_URL_CONNECT_TIMEOUT = 3000;

    private static final int REPO_URL_READ_TIMEOUT = 2000;

    private final java.lang.String jdkResourceUrl;

    private final java.lang.String javaHome;

    private final java.lang.String jdkName;

    private final java.lang.String jceName;

    private final java.lang.String ojdbcUrl;

    private final java.lang.String serverDB;

    private final java.lang.String mysqljdbcUrl;

    private boolean ldapSyncInProgress;

    private com.google.common.cache.Cache<org.apache.ambari.server.controller.ClusterRequest, org.apache.ambari.server.controller.ClusterResponse> clusterUpdateCache = com.google.common.cache.CacheBuilder.newBuilder().expireAfterWrite(5, java.util.concurrent.TimeUnit.MINUTES).build();

    private com.google.common.cache.Cache<org.apache.ambari.server.controller.ConfigGroupRequest, org.apache.ambari.server.controller.ConfigGroupResponse> configGroupUpdateCache = com.google.common.cache.CacheBuilder.newBuilder().expireAfterWrite(5, java.util.concurrent.TimeUnit.MINUTES).build();

    @com.google.inject.Inject
    private org.apache.ambari.server.controller.AmbariCustomCommandExecutionHelper customCommandExecutionHelper;

    @com.google.inject.Inject
    private org.apache.ambari.server.controller.AmbariActionExecutionHelper actionExecutionHelper;

    private java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>>> configCredentialsForService = new java.util.HashMap<>();

    @com.google.inject.Inject
    public AmbariManagementControllerImpl(org.apache.ambari.server.actionmanager.ActionManager actionManager, org.apache.ambari.server.state.Clusters clusters, com.google.inject.Injector injector) throws java.lang.Exception {
        this.clusters = clusters;
        this.actionManager = actionManager;
        this.injector = injector;
        injector.injectMembers(this);
        gson = injector.getInstance(com.google.gson.Gson.class);
        org.apache.ambari.server.controller.AmbariManagementControllerImpl.LOG.info("Initializing the AmbariManagementControllerImpl");
        masterHostname = java.net.InetAddress.getLocalHost().getCanonicalHostName();
        maintenanceStateHelper = injector.getInstance(org.apache.ambari.server.controller.MaintenanceStateHelper.class);
        kerberosHelper = injector.getInstance(org.apache.ambari.server.controller.KerberosHelper.class);
        if (configs != null) {
            if (configs.getApiSSLAuthentication()) {
                masterProtocol = "https";
                masterPort = configs.getClientSSLApiPort();
            } else {
                masterProtocol = "http";
                masterPort = configs.getClientApiPort();
            }
            jdkResourceUrl = getAmbariServerURI(org.apache.ambari.server.controller.AmbariManagementControllerImpl.JDK_RESOURCE_LOCATION);
            javaHome = configs.getJavaHome();
            jdkName = configs.getJDKName();
            jceName = configs.getJCEName();
            ojdbcUrl = getAmbariServerURI((org.apache.ambari.server.controller.AmbariManagementControllerImpl.JDK_RESOURCE_LOCATION + "/") + configs.getOjdbcJarName());
            mysqljdbcUrl = getAmbariServerURI((org.apache.ambari.server.controller.AmbariManagementControllerImpl.JDK_RESOURCE_LOCATION + "/") + configs.getMySQLJarName());
            serverDB = configs.getServerDBName();
        } else {
            masterProtocol = null;
            masterPort = null;
            jdkResourceUrl = null;
            javaHome = null;
            jdkName = null;
            jceName = null;
            ojdbcUrl = null;
            mysqljdbcUrl = null;
            serverDB = null;
        }
        helper = new org.apache.ambari.server.controller.AmbariManagementHelper(stackDAO, extensionDAO, linkDAO);
    }

    @java.lang.Override
    public java.lang.String getAmbariServerURI(java.lang.String path) {
        if (((masterProtocol == null) || (masterHostname == null)) || (masterPort == null)) {
            return null;
        }
        org.apache.http.client.utils.URIBuilder uriBuilder = new org.apache.http.client.utils.URIBuilder();
        uriBuilder.setScheme(masterProtocol);
        uriBuilder.setHost(masterHostname);
        uriBuilder.setPort(masterPort);
        java.lang.String[] parts = path.split("\\?");
        if (parts.length > 1) {
            uriBuilder.setPath(parts[0]);
            uriBuilder.setQuery(parts[1]);
        } else {
            uriBuilder.setPath(path);
        }
        return uriBuilder.toString();
    }

    @java.lang.Override
    public org.apache.ambari.server.metadata.RoleCommandOrder getRoleCommandOrder(org.apache.ambari.server.state.Cluster cluster) {
        return roleCommandOrderProvider.getRoleCommandOrder(cluster);
    }

    @java.lang.Override
    public void createCluster(org.apache.ambari.server.controller.ClusterRequest request) throws org.apache.ambari.server.AmbariException {
        if (((request.getClusterName() == null) || request.getClusterName().isEmpty()) || (request.getClusterId() != null)) {
            throw new java.lang.IllegalArgumentException("Cluster name should be provided and clusterId should be null");
        }
        if (org.apache.ambari.server.controller.AmbariManagementControllerImpl.LOG.isDebugEnabled()) {
            org.apache.ambari.server.controller.AmbariManagementControllerImpl.LOG.debug("Received a createCluster request, clusterName={}, request={}", request.getClusterName(), request);
        }
        if ((request.getStackVersion() == null) || request.getStackVersion().isEmpty()) {
            throw new java.lang.IllegalArgumentException("Stack information should be provided when creating a cluster");
        }
        org.apache.ambari.server.state.StackId stackId = new org.apache.ambari.server.state.StackId(request.getStackVersion());
        org.apache.ambari.server.state.StackInfo stackInfo = ambariMetaInfo.getStack(stackId.getStackName(), stackId.getStackVersion());
        if (stackInfo == null) {
            throw new org.apache.ambari.server.StackAccessException((("stackName=" + stackId.getStackName()) + ", stackVersion=") + stackId.getStackVersion());
        }
        boolean foundInvalidHosts = false;
        java.lang.StringBuilder invalidHostsStr = new java.lang.StringBuilder();
        if (request.getHostNames() != null) {
            for (java.lang.String hostname : request.getHostNames()) {
                try {
                    clusters.getHost(hostname);
                } catch (org.apache.ambari.server.HostNotFoundException e) {
                    if (foundInvalidHosts) {
                        invalidHostsStr.append(",");
                    }
                    foundInvalidHosts = true;
                    invalidHostsStr.append(hostname);
                }
            }
        }
        if (foundInvalidHosts) {
            throw new org.apache.ambari.server.HostNotFoundException(invalidHostsStr.toString());
        }
        clusters.addCluster(request.getClusterName(), stackId, request.getSecurityType());
        org.apache.ambari.server.state.Cluster c = clusters.getCluster(request.getClusterName());
        if (request.getHostNames() != null) {
            clusters.mapAndPublishHostsToCluster(request.getHostNames(), request.getClusterName());
        }
        initializeWidgetsAndLayouts(c, null);
    }

    @java.lang.Override
    public synchronized void createHostComponents(java.util.Set<org.apache.ambari.server.controller.ServiceComponentHostRequest> requests) throws org.apache.ambari.server.AmbariException, org.apache.ambari.server.security.authorization.AuthorizationException {
        createHostComponents(requests, false);
    }

    @java.lang.Override
    public org.apache.ambari.server.controller.MpackResponse registerMpack(org.apache.ambari.server.controller.MpackRequest request) throws java.io.IOException, org.apache.ambari.server.security.authorization.AuthorizationException, org.apache.ambari.server.controller.spi.ResourceAlreadyExistsException {
        org.apache.ambari.server.controller.MpackResponse mpackResponse = ambariMetaInfo.registerMpack(request);
        updateStacks();
        return mpackResponse;
    }

    @java.lang.Override
    public java.util.Set<org.apache.ambari.server.controller.MpackResponse> getMpacks() {
        java.util.Collection<org.apache.ambari.server.state.Mpack> mpacks = ambariMetaInfo.getMpacks();
        java.util.Set<org.apache.ambari.server.controller.MpackResponse> responseSet = new java.util.HashSet<>();
        for (org.apache.ambari.server.state.Mpack mpack : mpacks) {
            responseSet.add(new org.apache.ambari.server.controller.MpackResponse(mpack));
        }
        return responseSet;
    }

    @java.lang.Override
    public org.apache.ambari.server.controller.MpackResponse getMpack(java.lang.Long mpackId) {
        org.apache.ambari.server.state.Mpack mpack = ambariMetaInfo.getMpack(mpackId);
        if (mpack != null) {
            return new org.apache.ambari.server.controller.MpackResponse(mpack);
        } else {
            return null;
        }
    }

    @java.lang.Override
    public java.util.List<org.apache.ambari.server.state.Module> getModules(java.lang.Long mpackId) {
        return ambariMetaInfo.getModules(mpackId);
    }

    @java.lang.Override
    public synchronized void createHostComponents(java.util.Set<org.apache.ambari.server.controller.ServiceComponentHostRequest> requests, boolean isBlueprintProvisioned) throws org.apache.ambari.server.AmbariException, org.apache.ambari.server.security.authorization.AuthorizationException {
        if (requests.isEmpty()) {
            org.apache.ambari.server.controller.AmbariManagementControllerImpl.LOG.warn("Received an empty requests set");
            return;
        }
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.util.Set<java.lang.String>>>> hostComponentNames = new java.util.HashMap<>();
        java.util.Set<java.lang.String> duplicates = new java.util.HashSet<>();
        for (org.apache.ambari.server.controller.ServiceComponentHostRequest request : requests) {
            validateServiceComponentHostRequest(request);
            org.apache.ambari.server.state.Cluster cluster;
            try {
                cluster = clusters.getCluster(request.getClusterName());
            } catch (org.apache.ambari.server.ClusterNotFoundException e) {
                throw new org.apache.ambari.server.ParentObjectNotFoundException("Attempted to add a host_component to a cluster which doesn't exist: ", e);
            }
            if (!org.apache.ambari.server.security.authorization.AuthorizationHelper.isAuthorized(org.apache.ambari.server.security.authorization.ResourceType.CLUSTER, cluster.getResourceId(), java.util.EnumSet.of(org.apache.ambari.server.security.authorization.RoleAuthorization.SERVICE_ADD_DELETE_SERVICES, org.apache.ambari.server.security.authorization.RoleAuthorization.HOST_ADD_DELETE_COMPONENTS))) {
                throw new org.apache.ambari.server.security.authorization.AuthorizationException("The authenticated user is not authorized to install service components on to hosts");
            }
            if (org.apache.commons.lang.StringUtils.isEmpty(request.getServiceName())) {
                request.setServiceName(findServiceName(cluster, request.getComponentName()));
            }
            if (org.apache.ambari.server.controller.AmbariManagementControllerImpl.LOG.isDebugEnabled()) {
                org.apache.ambari.server.controller.AmbariManagementControllerImpl.LOG.debug("Received a createHostComponent request, clusterName={}, serviceName={}, componentName={}, hostname={}, request={}", request.getClusterName(), request.getServiceName(), request.getComponentName(), request.getHostname(), request);
            }
            if (!hostComponentNames.containsKey(request.getClusterName())) {
                hostComponentNames.put(request.getClusterName(), new java.util.HashMap<>());
            }
            if (!hostComponentNames.get(request.getClusterName()).containsKey(request.getServiceName())) {
                hostComponentNames.get(request.getClusterName()).put(request.getServiceName(), new java.util.HashMap<>());
            }
            if (!hostComponentNames.get(request.getClusterName()).get(request.getServiceName()).containsKey(request.getComponentName())) {
                hostComponentNames.get(request.getClusterName()).get(request.getServiceName()).put(request.getComponentName(), new java.util.HashSet<>());
            }
            if (hostComponentNames.get(request.getClusterName()).get(request.getServiceName()).get(request.getComponentName()).contains(request.getHostname())) {
                duplicates.add(((((("[clusterName=" + request.getClusterName()) + ", hostName=") + request.getHostname()) + ", componentName=") + request.getComponentName()) + ']');
                continue;
            }
            hostComponentNames.get(request.getClusterName()).get(request.getServiceName()).get(request.getComponentName()).add(request.getHostname());
            if ((request.getDesiredState() != null) && (!request.getDesiredState().isEmpty())) {
                org.apache.ambari.server.state.State state = org.apache.ambari.server.state.State.valueOf(request.getDesiredState());
                if ((!state.isValidDesiredState()) || (state != org.apache.ambari.server.state.State.INIT)) {
                    throw new java.lang.IllegalArgumentException(("Invalid desired state" + (" only INIT state allowed during creation" + ", providedDesiredState=")) + request.getDesiredState());
                }
            }
            org.apache.ambari.server.state.Service s;
            try {
                s = cluster.getService(request.getServiceName());
            } catch (org.apache.ambari.server.ServiceNotFoundException e) {
                throw new java.lang.IllegalArgumentException(((((("The service[" + request.getServiceName()) + "] associated with the component[") + request.getComponentName()) + "] doesn't exist for the cluster[") + request.getClusterName()) + "]");
            }
            org.apache.ambari.server.state.ServiceComponent sc = s.getServiceComponent(request.getComponentName());
            setRestartRequiredServices(s, request.getComponentName());
            org.apache.ambari.server.state.Host host;
            try {
                host = clusters.getHost(request.getHostname());
            } catch (org.apache.ambari.server.HostNotFoundException e) {
                throw new org.apache.ambari.server.ParentObjectNotFoundException("Attempted to add a host_component to a host that doesn't exist: ", e);
            }
            java.util.Set<org.apache.ambari.server.state.Cluster> mappedClusters = clusters.getClustersForHost(request.getHostname());
            boolean validCluster = false;
            if (org.apache.ambari.server.controller.AmbariManagementControllerImpl.LOG.isDebugEnabled()) {
                org.apache.ambari.server.controller.AmbariManagementControllerImpl.LOG.debug("Looking to match host to cluster, hostnameViaReg={}, hostname={}, clusterName={}, hostClusterMapCount={}", host.getHostName(), request.getHostname(), request.getClusterName(), mappedClusters.size());
            }
            for (org.apache.ambari.server.state.Cluster mappedCluster : mappedClusters) {
                if (org.apache.ambari.server.controller.AmbariManagementControllerImpl.LOG.isDebugEnabled()) {
                    org.apache.ambari.server.controller.AmbariManagementControllerImpl.LOG.debug("Host belongs to cluster, hostname={}, clusterName={}", request.getHostname(), mappedCluster.getClusterName());
                }
                if (mappedCluster.getClusterName().equals(request.getClusterName())) {
                    validCluster = true;
                    break;
                }
            }
            if (!validCluster) {
                throw new org.apache.ambari.server.ParentObjectNotFoundException(((("Attempted to add a host_component to a host that doesn't exist: " + "clusterName=") + request.getClusterName()) + ", hostName=") + request.getHostname());
            }
            try {
                org.apache.ambari.server.state.ServiceComponentHost sch = sc.getServiceComponentHost(request.getHostname());
                if (sch != null) {
                    duplicates.add(((((("[clusterName=" + request.getClusterName()) + ", hostName=") + request.getHostname()) + ", componentName=") + request.getComponentName()) + ']');
                }
            } catch (org.apache.ambari.server.AmbariException e) {
            }
        }
        if (hostComponentNames.size() != 1) {
            throw new java.lang.IllegalArgumentException("Invalid arguments - updates allowed" + " on only one cluster at a time");
        }
        if (!duplicates.isEmpty()) {
            final java.lang.String names = java.lang.String.join(",", duplicates);
            java.lang.String msg;
            if (duplicates.size() == 1) {
                msg = "Attempted to create a host_component which already exists: ";
            } else {
                msg = "Attempted to create host_component's which already exist: ";
            }
            throw new org.apache.ambari.server.DuplicateResourceException(msg + names);
        }
        if (!isBlueprintProvisioned) {
            validateExclusiveDependencies(hostComponentNames);
        }
        setMonitoringServicesRestartRequired(requests);
        persistServiceComponentHosts(requests, isBlueprintProvisioned);
        m_topologyHolder.get().updateData(getAddedComponentsTopologyEvent(requests));
    }

    private void validateExclusiveDependencies(java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.util.Set<java.lang.String>>>> hostComponentNames) throws org.apache.ambari.server.AmbariException {
        java.util.List<java.lang.String> validationIssues = new java.util.ArrayList<>();
        for (java.util.Map.Entry<java.lang.String, java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.util.Set<java.lang.String>>>> clusterEntry : hostComponentNames.entrySet()) {
            for (java.util.Map.Entry<java.lang.String, java.util.Map<java.lang.String, java.util.Set<java.lang.String>>> serviceEntry : clusterEntry.getValue().entrySet()) {
                for (java.util.Map.Entry<java.lang.String, java.util.Set<java.lang.String>> componentEntry : serviceEntry.getValue().entrySet()) {
                    java.util.Set<java.lang.String> hostnames = componentEntry.getValue();
                    if ((hostnames != null) && (!hostnames.isEmpty())) {
                        org.apache.ambari.server.state.ServiceComponent sc = clusters.getCluster(clusterEntry.getKey()).getService(serviceEntry.getKey()).getServiceComponent(componentEntry.getKey());
                        org.apache.ambari.server.state.StackId stackId = sc.getDesiredStackId();
                        java.util.List<org.apache.ambari.server.state.DependencyInfo> dependencyInfos = ambariMetaInfo.getComponentDependencies(stackId.getStackName(), stackId.getStackVersion(), serviceEntry.getKey(), componentEntry.getKey());
                        for (org.apache.ambari.server.state.DependencyInfo dependencyInfo : dependencyInfos) {
                            if ("host".equals(dependencyInfo.getScope()) && "exclusive".equals(dependencyInfo.getType())) {
                                org.apache.ambari.server.state.Service depService;
                                try {
                                    depService = clusters.getCluster(clusterEntry.getKey()).getService(dependencyInfo.getServiceName());
                                } catch (org.apache.ambari.server.ServiceNotFoundException e) {
                                    org.apache.ambari.server.controller.AmbariManagementControllerImpl.LOG.debug(((("Skipping dependency " + dependencyInfo) + " for ") + serviceEntry.getKey()) + " since the dependent service is not installed ");
                                    continue;
                                }
                                if ((depService != null) && depService.getServiceComponents().containsKey(dependencyInfo.getComponentName())) {
                                    org.apache.ambari.server.state.ServiceComponent dependentSC = depService.getServiceComponent(dependencyInfo.getComponentName());
                                    if (dependentSC != null) {
                                        java.util.Set<java.lang.String> dependentComponentHosts = new java.util.HashSet<>(dependentSC.getServiceComponentHosts().keySet());
                                        if (clusterEntry.getValue().containsKey(dependentSC.getServiceName()) && clusterEntry.getValue().get(dependentSC.getServiceName()).containsKey(dependentSC.getName())) {
                                            dependentComponentHosts.addAll(clusterEntry.getValue().get(dependentSC.getServiceName()).get(dependentSC.getName()));
                                        }
                                        dependentComponentHosts.retainAll(hostnames);
                                        if (!dependentComponentHosts.isEmpty()) {
                                            validationIssues.add(((((("Component " + componentEntry.getKey()) + " can't be co-hosted with component ") + dependencyInfo.getComponentName()) + " on hosts ") + dependentComponentHosts) + " due to exclusive dependency");
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        if (!validationIssues.isEmpty()) {
            throw new org.apache.ambari.server.AmbariException("The components exclusive dependencies are not respected: " + validationIssues);
        }
    }

    void persistServiceComponentHosts(java.util.Set<org.apache.ambari.server.controller.ServiceComponentHostRequest> requests, boolean isBlueprintProvisioned) throws org.apache.ambari.server.AmbariException {
        com.google.common.collect.Multimap<org.apache.ambari.server.state.Cluster, org.apache.ambari.server.state.ServiceComponentHost> schMap = com.google.common.collect.ArrayListMultimap.create();
        java.util.Map<java.lang.Long, java.util.Map<java.lang.String, java.util.List<java.lang.String>>> serviceComponentNames = new java.util.HashMap<>();
        java.util.Map<java.lang.Long, java.util.Map<java.lang.String, java.util.Map<java.lang.String, org.apache.ambari.server.orm.entities.ServiceComponentDesiredStateEntity>>> serviceComponentDesiredStateEntities = new java.util.HashMap<>();
        for (org.apache.ambari.server.controller.ServiceComponentHostRequest request : requests) {
            org.apache.ambari.server.state.Cluster cluster = clusters.getCluster(request.getClusterName());
            org.apache.ambari.server.state.Service s = cluster.getService(request.getServiceName());
            org.apache.ambari.server.state.ServiceComponent sc = s.getServiceComponent(request.getComponentName());
            serviceComponentNames.computeIfAbsent(sc.getClusterId(), c -> new java.util.HashMap<>()).computeIfAbsent(sc.getServiceName(), h -> new java.util.ArrayList<>()).add(sc.getName());
        }
        java.util.List<org.apache.ambari.server.orm.entities.ServiceComponentDesiredStateEntity> entities = serviceComponentDesiredStateDAO.findByNames(serviceComponentNames);
        for (org.apache.ambari.server.orm.entities.ServiceComponentDesiredStateEntity stateEntity : entities) {
            serviceComponentDesiredStateEntities.computeIfAbsent(stateEntity.getClusterId(), c -> new java.util.HashMap<>()).computeIfAbsent(stateEntity.getServiceName(), h -> new java.util.HashMap<>()).putIfAbsent(stateEntity.getComponentName(), stateEntity);
        }
        for (org.apache.ambari.server.controller.ServiceComponentHostRequest request : requests) {
            org.apache.ambari.server.state.Cluster cluster = clusters.getCluster(request.getClusterName());
            org.apache.ambari.server.state.Service s = cluster.getService(request.getServiceName());
            org.apache.ambari.server.state.ServiceComponent sc = s.getServiceComponent(request.getComponentName());
            org.apache.ambari.server.state.ServiceComponentHost sch = serviceComponentHostFactory.createNew(sc, request.getHostname(), serviceComponentDesiredStateEntities.get(cluster.getClusterId()).get(s.getName()).get(sc.getName()));
            if ((request.getDesiredState() != null) && (!request.getDesiredState().isEmpty())) {
                org.apache.ambari.server.state.State state = org.apache.ambari.server.state.State.valueOf(request.getDesiredState());
                sch.setDesiredState(state);
            }
            if (isBlueprintProvisioned && (!sch.isClientComponent())) {
                org.apache.ambari.server.orm.entities.HostComponentDesiredStateEntity desiredStateEntity = sch.getDesiredStateEntity();
                desiredStateEntity.setBlueprintProvisioningState(org.apache.ambari.server.state.BlueprintProvisioningState.IN_PROGRESS);
                hostComponentDesiredStateDAO.merge(desiredStateEntity);
            }
            schMap.put(cluster, sch);
        }
        for (org.apache.ambari.server.state.Cluster cluster : schMap.keySet()) {
            cluster.addServiceComponentHosts(schMap.get(cluster));
        }
    }

    @java.lang.Override
    public org.apache.ambari.server.events.TopologyUpdateEvent getAddedComponentsTopologyEvent(java.util.Set<org.apache.ambari.server.controller.ServiceComponentHostRequest> requests) throws org.apache.ambari.server.AmbariException {
        java.util.TreeMap<java.lang.String, org.apache.ambari.server.agent.stomp.dto.TopologyCluster> topologyUpdates = new java.util.TreeMap<>();
        java.util.Set<java.lang.String> hostsToUpdate = new java.util.HashSet<>();
        for (org.apache.ambari.server.controller.ServiceComponentHostRequest request : requests) {
            java.lang.String serviceName = request.getServiceName();
            java.lang.String componentName = request.getComponentName();
            org.apache.ambari.server.state.Cluster cluster = clusters.getCluster(request.getClusterName());
            java.util.Collection<org.apache.ambari.server.state.Host> clusterHosts = cluster.getHosts();
            org.apache.ambari.server.state.Service s = cluster.getService(serviceName);
            org.apache.ambari.server.state.ServiceComponent sc = s.getServiceComponent(componentName);
            java.lang.String hostName = request.getHostname();
            hostsToUpdate.add(hostName);
            java.util.Set<java.lang.Long> hostIds = clusterHosts.stream().filter(h -> hostName.equals(h.getHostName())).map(h -> h.getHostId()).collect(java.util.stream.Collectors.toSet());
            java.util.Set<java.lang.String> publicHostNames = clusterHosts.stream().filter(h -> hostName.equals(h.getHostName())).map(h -> h.getPublicHostName()).collect(java.util.stream.Collectors.toSet());
            java.util.Set<java.lang.String> hostNames = new java.util.HashSet<>();
            hostNames.add(hostName);
            org.apache.ambari.server.state.ServiceComponentHost sch = sc.getServiceComponentHost(request.getHostname());
            org.apache.ambari.server.agent.stomp.dto.TopologyComponent newComponent = org.apache.ambari.server.agent.stomp.dto.TopologyComponent.newBuilder().setComponentName(sch.getServiceComponentName()).setServiceName(sch.getServiceName()).setDisplayName(sc.getDisplayName()).setHostIdentifiers(hostIds, hostNames).setPublicHostNames(publicHostNames).setComponentLevelParams(getTopologyComponentLevelParams(cluster.getClusterId(), serviceName, componentName, cluster.getSecurityType())).setCommandParams(getTopologyCommandParams(cluster.getClusterId(), serviceName, componentName, sch)).build();
            java.lang.String clusterId = java.lang.Long.toString(cluster.getClusterId());
            if (!topologyUpdates.containsKey(clusterId)) {
                topologyUpdates.put(clusterId, new org.apache.ambari.server.agent.stomp.dto.TopologyCluster());
            }
            if (topologyUpdates.get(clusterId).getTopologyComponents().contains(newComponent)) {
                java.util.Set<org.apache.ambari.server.agent.stomp.dto.TopologyComponent> newComponents = new java.util.HashSet<>();
                newComponents.add(newComponent);
                topologyUpdates.get(clusterId).update(newComponents, java.util.Collections.emptySet(), org.apache.ambari.server.events.UpdateEventType.UPDATE, new org.apache.ambari.server.agent.stomp.dto.TopologyUpdateHandlingReport());
            } else {
                topologyUpdates.get(clusterId).addTopologyComponent(newComponent);
            }
        }
        for (java.lang.String hostName : hostsToUpdate) {
            org.apache.ambari.server.state.Host host = clusters.getHost(hostName);
            m_hostLevelParamsHolder.get().updateData(m_hostLevelParamsHolder.get().getCurrentData(host.getHostId()));
        }
        return new org.apache.ambari.server.events.TopologyUpdateEvent(topologyUpdates, org.apache.ambari.server.events.UpdateEventType.UPDATE);
    }

    private void setMonitoringServicesRestartRequired(java.util.Set<org.apache.ambari.server.controller.ServiceComponentHostRequest> requests) throws org.apache.ambari.server.AmbariException {
        for (org.apache.ambari.server.controller.ServiceComponentHostRequest request : requests) {
            org.apache.ambari.server.state.Cluster cluster = clusters.getCluster(request.getClusterName());
            for (org.apache.ambari.server.state.Service service : cluster.getServices().values()) {
                org.apache.ambari.server.state.ServiceInfo serviceInfo = ambariMetaInfo.getService(service);
                if (!org.apache.commons.lang.BooleanUtils.toBoolean(serviceInfo.isMonitoringService())) {
                    continue;
                }
                for (org.apache.ambari.server.state.ServiceComponent sc : service.getServiceComponents().values()) {
                    if (sc.isMasterComponent()) {
                        for (org.apache.ambari.server.state.ServiceComponentHost sch : sc.getServiceComponentHosts().values()) {
                            sch.setRestartRequired(true);
                        }
                        continue;
                    }
                    java.lang.String hostname = request.getHostname();
                    if (sc.getServiceComponentHosts().containsKey(hostname)) {
                        org.apache.ambari.server.state.ServiceComponentHost sch = sc.getServiceComponentHost(hostname);
                        sch.setRestartRequired(true);
                    }
                }
            }
        }
    }

    private void setRestartRequiredServices(org.apache.ambari.server.state.Service service, java.lang.String componentName) throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.server.state.StackId stackId = service.getDesiredStackId();
        if (service.getServiceComponent(componentName).isClientComponent()) {
            return;
        }
        java.util.Set<java.lang.String> needRestartServices = ambariMetaInfo.getRestartRequiredServicesNames(stackId.getStackName(), stackId.getStackVersion());
        if (needRestartServices.contains(service.getName())) {
            java.util.Map<java.lang.String, org.apache.ambari.server.state.ServiceComponent> m = service.getServiceComponents();
            for (java.util.Map.Entry<java.lang.String, org.apache.ambari.server.state.ServiceComponent> entry : m.entrySet()) {
                org.apache.ambari.server.state.ServiceComponent serviceComponent = entry.getValue();
                java.util.Map<java.lang.String, org.apache.ambari.server.state.ServiceComponentHost> schMap = serviceComponent.getServiceComponentHosts();
                for (java.util.Map.Entry<java.lang.String, org.apache.ambari.server.state.ServiceComponentHost> sch : schMap.entrySet()) {
                    org.apache.ambari.server.state.ServiceComponentHost serviceComponentHost = sch.getValue();
                    serviceComponentHost.setRestartRequired(true);
                }
            }
        }
    }

    @java.lang.Override
    public void registerRackChange(java.lang.String clusterName) throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.server.state.Cluster cluster = clusters.getCluster(clusterName);
        for (org.apache.ambari.server.state.Service service : cluster.getServices().values()) {
            org.apache.ambari.server.state.ServiceInfo serviceInfo = ambariMetaInfo.getService(service);
            if (!org.apache.commons.lang.BooleanUtils.toBoolean(serviceInfo.isRestartRequiredAfterRackChange())) {
                continue;
            }
            java.util.Map<java.lang.String, org.apache.ambari.server.state.ServiceComponent> serviceComponents = service.getServiceComponents();
            for (org.apache.ambari.server.state.ServiceComponent serviceComponent : serviceComponents.values()) {
                java.util.Map<java.lang.String, org.apache.ambari.server.state.ServiceComponentHost> schMap = serviceComponent.getServiceComponentHosts();
                for (java.util.Map.Entry<java.lang.String, org.apache.ambari.server.state.ServiceComponentHost> sch : schMap.entrySet()) {
                    org.apache.ambari.server.state.ServiceComponentHost serviceComponentHost = sch.getValue();
                    serviceComponentHost.setRestartRequired(true);
                }
            }
        }
    }

    @java.lang.Override
    public synchronized org.apache.ambari.server.controller.ConfigurationResponse createConfiguration(org.apache.ambari.server.controller.ConfigurationRequest request, boolean refreshCluster) throws org.apache.ambari.server.AmbariException, org.apache.ambari.server.security.authorization.AuthorizationException {
        if (((((null == request.getClusterName()) || request.getClusterName().isEmpty()) || (null == request.getType())) || request.getType().isEmpty()) || (null == request.getProperties())) {
            throw new java.lang.IllegalArgumentException("Invalid Arguments," + (" clustername, config type and configs should not" + " be null or empty"));
        }
        org.apache.ambari.server.state.Cluster cluster = clusters.getCluster(request.getClusterName());
        java.lang.String configType = request.getType();
        java.lang.String service = cluster.getServiceByConfigType(configType);
        java.util.Map<java.lang.String, java.lang.String[]> propertyChanges = getPropertyChanges(cluster, request);
        if (org.apache.commons.lang.StringUtils.isEmpty(service)) {
            validateAuthorizationToManageServiceAutoStartConfiguration(cluster, configType, propertyChanges);
            validateAuthorizationToModifyConfigurations(cluster, configType, propertyChanges, java.util.Collections.singletonMap("cluster-env", java.util.Collections.singleton("recovery_enabled")), false);
        } else {
            validateAuthorizationToModifyConfigurations(cluster, configType, propertyChanges, null, true);
            validateAuthorizationToUpdateServiceUsersAndGroups(cluster, configType, propertyChanges);
        }
        java.util.Map<java.lang.String, java.lang.String> requestProperties = request.getProperties();
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> requestPropertiesAttributes = request.getPropertiesAttributes();
        if ((requestPropertiesAttributes != null) && requestPropertiesAttributes.containsKey(org.apache.ambari.server.controller.AmbariManagementControllerImpl.PASSWORD)) {
            for (java.util.Map.Entry<java.lang.String, java.lang.String> requestEntry : requestPropertiesAttributes.get(org.apache.ambari.server.controller.AmbariManagementControllerImpl.PASSWORD).entrySet()) {
                java.lang.String passwordProperty = requestEntry.getKey();
                if (requestProperties.containsKey(passwordProperty) && requestEntry.getValue().equals("true")) {
                    java.lang.String passwordPropertyValue = requestProperties.get(passwordProperty);
                    if (!org.apache.ambari.server.utils.SecretReference.isSecret(passwordPropertyValue)) {
                        continue;
                    }
                    org.apache.ambari.server.utils.SecretReference ref = new org.apache.ambari.server.utils.SecretReference(passwordPropertyValue, cluster);
                    java.lang.String refValue = ref.getValue();
                    requestProperties.put(passwordProperty, refValue);
                }
            }
        }
        java.util.Map<org.apache.ambari.server.state.PropertyInfo.PropertyType, java.util.Set<java.lang.String>> propertiesTypes = cluster.getConfigPropertiesTypes(request.getType());
        if (propertiesTypes.containsKey(org.apache.ambari.server.state.PropertyInfo.PropertyType.PASSWORD)) {
            for (java.lang.String passwordProperty : propertiesTypes.get(org.apache.ambari.server.state.PropertyInfo.PropertyType.PASSWORD)) {
                if (requestProperties.containsKey(passwordProperty)) {
                    java.lang.String passwordPropertyValue = requestProperties.get(passwordProperty);
                    if (!org.apache.ambari.server.utils.SecretReference.isSecret(passwordPropertyValue)) {
                        continue;
                    }
                    org.apache.ambari.server.utils.SecretReference ref = new org.apache.ambari.server.utils.SecretReference(passwordPropertyValue, cluster);
                    java.lang.String refValue = ref.getValue();
                    requestProperties.put(passwordProperty, refValue);
                }
            }
        }
        java.util.Map<java.lang.String, org.apache.ambari.server.state.Config> configs = cluster.getConfigsByType(request.getType());
        if (null == configs) {
            configs = new java.util.HashMap<>();
        }
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> propertiesAttributes = new java.util.HashMap<>();
        java.util.Set<org.apache.ambari.server.state.StackId> visitedStacks = new java.util.HashSet<>();
        for (org.apache.ambari.server.state.Service clusterService : cluster.getServices().values()) {
            org.apache.ambari.server.state.StackId stackId = clusterService.getDesiredStackId();
            org.apache.ambari.server.state.StackInfo stackInfo = ambariMetaInfo.getStack(clusterService.getDesiredStackId());
            if (visitedStacks.contains(stackId)) {
                continue;
            }
            java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> defaultConfigAttributes = stackInfo.getDefaultConfigAttributesForConfigType(configType);
            if (null != defaultConfigAttributes) {
                org.apache.ambari.server.state.ConfigHelper.mergeConfigAttributes(propertiesAttributes, defaultConfigAttributes);
            }
            visitedStacks.add(stackId);
        }
        if (requestPropertiesAttributes != null) {
            org.apache.ambari.server.state.ConfigHelper.mergeConfigAttributes(propertiesAttributes, requestPropertiesAttributes);
        }
        if (configs.containsKey(request.getVersionTag())) {
            throw new org.apache.ambari.server.AmbariException(java.text.MessageFormat.format("Configuration with tag ''{0}'' exists for ''{1}''", request.getVersionTag(), request.getType()));
        }
        org.apache.ambari.server.state.StackId stackId = null;
        if (null != service) {
            try {
                org.apache.ambari.server.state.Service svc = cluster.getService(service);
                stackId = svc.getDesiredStackId();
            } catch (org.apache.ambari.server.AmbariException ambariException) {
                org.apache.ambari.server.controller.AmbariManagementControllerImpl.LOG.warn("Adding configurations for {} even though its parent service {} is not installed", configType, service);
            }
        }
        if (null == stackId) {
            stackId = cluster.getDesiredStackVersion();
        }
        org.apache.ambari.server.state.Config config = createConfig(cluster, stackId, request.getType(), requestProperties, request.getVersionTag(), propertiesAttributes, refreshCluster);
        org.apache.ambari.server.controller.AmbariManagementControllerImpl.LOG.info(java.text.MessageFormat.format("Creating configuration with tag ''{0}'' to cluster ''{1}''  for configuration type {2}", request.getVersionTag(), request.getClusterName(), configType));
        return new org.apache.ambari.server.controller.ConfigurationResponse(cluster.getClusterName(), config);
    }

    @java.lang.Override
    public synchronized org.apache.ambari.server.controller.ConfigurationResponse createConfiguration(org.apache.ambari.server.controller.ConfigurationRequest request) throws org.apache.ambari.server.AmbariException, org.apache.ambari.server.security.authorization.AuthorizationException {
        return createConfiguration(request, true);
    }

    @java.lang.Override
    public org.apache.ambari.server.state.Config createConfig(org.apache.ambari.server.state.Cluster cluster, org.apache.ambari.server.state.StackId stackId, java.lang.String type, java.util.Map<java.lang.String, java.lang.String> properties, java.lang.String versionTag, java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> propertiesAttributes, boolean refreshCluster) {
        org.apache.ambari.server.state.Config config = configFactory.createNew(stackId, type, cluster, versionTag, properties, propertiesAttributes, refreshCluster);
        cluster.addConfig(config);
        return config;
    }

    @java.lang.Override
    public org.apache.ambari.server.state.Config createConfig(org.apache.ambari.server.state.Cluster cluster, org.apache.ambari.server.state.StackId stackId, java.lang.String type, java.util.Map<java.lang.String, java.lang.String> properties, java.lang.String versionTag, java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> propertiesAttributes) {
        return createConfig(cluster, stackId, type, properties, versionTag, propertiesAttributes, true);
    }

    @java.lang.Override
    public void createGroups(java.util.Set<org.apache.ambari.server.controller.GroupRequest> requests) throws org.apache.ambari.server.AmbariException {
        for (org.apache.ambari.server.controller.GroupRequest request : requests) {
            if (org.apache.commons.lang.StringUtils.isBlank(request.getGroupName())) {
                throw new org.apache.ambari.server.AmbariException("Group name must be supplied.");
            }
            final org.apache.ambari.server.security.authorization.Group group = users.getGroup(request.getGroupName());
            if (group != null) {
                throw new org.apache.ambari.server.AmbariException("Group already exists.");
            }
            users.createGroup(request.getGroupName(), org.apache.ambari.server.security.authorization.GroupType.LOCAL);
        }
    }

    @java.lang.Override
    public void createMembers(java.util.Set<org.apache.ambari.server.controller.MemberRequest> requests) throws org.apache.ambari.server.AmbariException {
        for (org.apache.ambari.server.controller.MemberRequest request : requests) {
            if (org.apache.commons.lang.StringUtils.isBlank(request.getGroupName()) || org.apache.commons.lang.StringUtils.isBlank(request.getUserName())) {
                throw new org.apache.ambari.server.AmbariException("Both group name and user name must be supplied.");
            }
            users.addMemberToGroup(request.getGroupName(), request.getUserName());
        }
    }

    @java.lang.Override
    public java.util.Set<org.apache.ambari.server.controller.MemberResponse> getMembers(java.util.Set<org.apache.ambari.server.controller.MemberRequest> requests) throws org.apache.ambari.server.AmbariException {
        final java.util.Set<org.apache.ambari.server.controller.MemberResponse> responses = new java.util.HashSet<>();
        for (org.apache.ambari.server.controller.MemberRequest request : requests) {
            org.apache.ambari.server.controller.AmbariManagementControllerImpl.LOG.debug("Received a getMembers request, {}", request);
            final org.apache.ambari.server.security.authorization.Group group = users.getGroup(request.getGroupName());
            if (null == group) {
                if (requests.size() == 1) {
                    throw new org.apache.ambari.server.ObjectNotFoundException(("Cannot find group '" + request.getGroupName()) + "'");
                }
            } else {
                for (org.apache.ambari.server.security.authorization.User user : users.getGroupMembers(group.getGroupName())) {
                    final org.apache.ambari.server.controller.MemberResponse response = new org.apache.ambari.server.controller.MemberResponse(group.getGroupName(), user.getUserName());
                    responses.add(response);
                }
            }
        }
        return responses;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public synchronized void updateMembers(java.util.Set<org.apache.ambari.server.controller.MemberRequest> requests) throws org.apache.ambari.server.AmbariException {
        java.lang.String groupName = null;
        for (org.apache.ambari.server.controller.MemberRequest request : requests) {
            if ((groupName != null) && (!request.getGroupName().equals(groupName))) {
                throw new org.apache.ambari.server.AmbariException("Can't manage members of different groups in one request");
            }
            groupName = request.getGroupName();
        }
        final java.util.List<java.lang.String> requiredMembers = new java.util.ArrayList<>();
        for (org.apache.ambari.server.controller.MemberRequest request : requests) {
            if (request.getUserName() != null) {
                requiredMembers.add(request.getUserName());
            }
        }
        final java.util.List<java.lang.String> currentMembers = users.getAllMembers(groupName);
        for (java.lang.String user : ((java.util.Collection<java.lang.String>) (org.apache.commons.collections.CollectionUtils.subtract(currentMembers, requiredMembers)))) {
            users.removeMemberFromGroup(groupName, user);
        }
        for (java.lang.String user : ((java.util.Collection<java.lang.String>) (org.apache.commons.collections.CollectionUtils.subtract(requiredMembers, currentMembers)))) {
            users.addMemberToGroup(groupName, user);
        }
    }

    private org.apache.ambari.server.actionmanager.Stage createNewStage(long id, org.apache.ambari.server.state.Cluster cluster, long requestId, java.lang.String requestContext, java.lang.String commandParamsStage, java.lang.String hostParamsStage) {
        java.lang.String logDir = (org.apache.ambari.server.controller.AmbariManagementControllerImpl.BASE_LOG_DIR + java.io.File.pathSeparator) + requestId;
        org.apache.ambari.server.actionmanager.Stage stage = stageFactory.createNew(requestId, logDir, null == cluster ? null : cluster.getClusterName(), null == cluster ? -1L : cluster.getClusterId(), requestContext, commandParamsStage, hostParamsStage);
        stage.setStageId(id);
        return stage;
    }

    private java.util.Set<org.apache.ambari.server.controller.ClusterResponse> getClusters(org.apache.ambari.server.controller.ClusterRequest request) throws org.apache.ambari.server.AmbariException, org.apache.ambari.server.security.authorization.AuthorizationException {
        java.util.Set<org.apache.ambari.server.controller.ClusterResponse> response = new java.util.HashSet<>();
        if (org.apache.ambari.server.controller.AmbariManagementControllerImpl.LOG.isDebugEnabled()) {
            org.apache.ambari.server.controller.AmbariManagementControllerImpl.LOG.debug("Received a getClusters request, clusterName={}, clusterId={}, stackInfo={}", request.getClusterName(), request.getClusterId(), request.getStackVersion());
        }
        org.apache.ambari.server.state.Cluster singleCluster = null;
        try {
            if (request.getClusterName() != null) {
                singleCluster = clusters.getCluster(request.getClusterName());
            } else if (request.getClusterId() != null) {
                singleCluster = clusters.getClusterById(request.getClusterId());
            }
        } catch (org.apache.ambari.server.ClusterNotFoundException e) {
            if (org.apache.ambari.server.security.authorization.AuthorizationHelper.isAuthorized(org.apache.ambari.server.security.authorization.ResourceType.AMBARI, null, org.apache.ambari.server.security.authorization.RoleAuthorization.AMBARI_ADD_DELETE_CLUSTERS)) {
                throw e;
            } else {
                throw new org.apache.ambari.server.security.authorization.AuthorizationException();
            }
        }
        if (singleCluster != null) {
            org.apache.ambari.server.controller.ClusterResponse cr = singleCluster.convertToResponse();
            cr.setDesiredConfigs(singleCluster.getDesiredConfigs());
            cr.setDesiredServiceConfigVersions(singleCluster.getActiveServiceConfigVersions());
            cr.setCredentialStoreServiceProperties(getCredentialStoreServiceProperties());
            response.add(cr);
            return response;
        }
        java.util.Map<java.lang.String, org.apache.ambari.server.state.Cluster> allClusters = clusters.getClusters();
        for (org.apache.ambari.server.state.Cluster c : allClusters.values()) {
            org.apache.ambari.server.controller.ClusterResponse cr = c.convertToResponse();
            cr.setDesiredConfigs(c.getDesiredConfigs());
            cr.setDesiredServiceConfigVersions(c.getActiveServiceConfigVersions());
            cr.setCredentialStoreServiceProperties(getCredentialStoreServiceProperties());
            response.add(cr);
        }
        java.lang.StringBuilder builder = new java.lang.StringBuilder();
        if (org.apache.ambari.server.controller.AmbariManagementControllerImpl.LOG.isDebugEnabled()) {
            clusters.debugDump(builder);
            org.apache.ambari.server.controller.AmbariManagementControllerImpl.LOG.debug("Cluster State for cluster {}", builder);
        }
        return response;
    }

    private java.util.Set<org.apache.ambari.server.controller.ServiceComponentHostResponse> getHostComponents(org.apache.ambari.server.controller.ServiceComponentHostRequest request) throws org.apache.ambari.server.AmbariException {
        return getHostComponents(request, false);
    }

    private java.util.Set<org.apache.ambari.server.controller.ServiceComponentHostResponse> getHostComponents(org.apache.ambari.server.controller.ServiceComponentHostRequest request, boolean statusOnly) throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.server.controller.AmbariManagementControllerImpl.LOG.debug("Processing request {}", request);
        if ((request.getClusterName() == null) || request.getClusterName().isEmpty()) {
            java.lang.IllegalArgumentException e = new java.lang.IllegalArgumentException("Invalid arguments, cluster name should not be null");
            org.apache.ambari.server.controller.AmbariManagementControllerImpl.LOG.debug("Cluster not specified in request", e);
            throw e;
        }
        final org.apache.ambari.server.state.Cluster cluster;
        try {
            cluster = clusters.getCluster(request.getClusterName());
        } catch (org.apache.ambari.server.ClusterNotFoundException e) {
            org.apache.ambari.server.controller.AmbariManagementControllerImpl.LOG.error("Cluster not found ", e);
            throw new org.apache.ambari.server.ParentObjectNotFoundException("Parent Cluster resource doesn't exist", e);
        }
        if (request.getHostname() != null) {
            try {
                if (!clusters.getClustersForHost(request.getHostname()).contains(cluster)) {
                    org.apache.ambari.server.controller.AmbariManagementControllerImpl.LOG.error("Host doesn't belong to cluster - " + request.getHostname());
                    throw new org.apache.ambari.server.ParentObjectNotFoundException("Parent Host resource doesn't exist", new org.apache.ambari.server.HostNotFoundException(request.getClusterName(), request.getHostname()));
                }
            } catch (org.apache.ambari.server.HostNotFoundException e) {
                org.apache.ambari.server.controller.AmbariManagementControllerImpl.LOG.error("Host not found", e);
                throw new org.apache.ambari.server.ParentObjectNotFoundException("Parent Host resource doesn't exist", new org.apache.ambari.server.HostNotFoundException(request.getClusterName(), request.getHostname()));
            }
        }
        if (request.getComponentName() != null) {
            if (org.apache.commons.lang.StringUtils.isBlank(request.getServiceName())) {
                java.lang.String serviceName = findServiceName(cluster, request.getComponentName());
                if (org.apache.commons.lang.StringUtils.isBlank(serviceName)) {
                    org.apache.ambari.server.controller.AmbariManagementControllerImpl.LOG.error("Unable to find service for component {}", request.getComponentName());
                    throw new org.apache.ambari.server.ServiceComponentHostNotFoundException(cluster.getClusterName(), null, request.getComponentName(), request.getHostname());
                }
                request.setServiceName(serviceName);
            }
        }
        java.util.Set<org.apache.ambari.server.state.Service> services = new java.util.HashSet<>();
        if ((request.getServiceName() != null) && (!request.getServiceName().isEmpty())) {
            services.add(cluster.getService(request.getServiceName()));
        } else {
            services.addAll(cluster.getServices().values());
        }
        java.util.Set<org.apache.ambari.server.controller.ServiceComponentHostResponse> response = new java.util.HashSet<>();
        boolean checkDesiredState = false;
        org.apache.ambari.server.state.State desiredStateToCheck = null;
        boolean checkState = false;
        org.apache.ambari.server.state.State stateToCheck = null;
        boolean filterBasedConfigStaleness = false;
        boolean staleConfig = true;
        if (request.getStaleConfig() != null) {
            filterBasedConfigStaleness = true;
            staleConfig = "true".equals(request.getStaleConfig().toLowerCase());
        }
        if ((request.getDesiredState() != null) && (!request.getDesiredState().isEmpty())) {
            desiredStateToCheck = org.apache.ambari.server.state.State.valueOf(request.getDesiredState());
            if (!desiredStateToCheck.isValidDesiredState()) {
                throw new java.lang.IllegalArgumentException(("Invalid arguments, invalid desired" + " state, desiredState=") + desiredStateToCheck);
            }
            checkDesiredState = true;
        }
        if (!org.apache.commons.lang.StringUtils.isEmpty(request.getState())) {
            stateToCheck = org.apache.ambari.server.state.State.valueOf(request.getState());
            if (stateToCheck == null) {
                throw new java.lang.IllegalArgumentException("Invalid arguments, invalid state, State=" + request.getState());
            }
            checkState = true;
        }
        java.util.Map<java.lang.String, org.apache.ambari.server.state.DesiredConfig> desiredConfigs = cluster.getDesiredConfigs();
        java.util.Map<java.lang.String, org.apache.ambari.server.state.Host> hosts = clusters.getHostsForCluster(cluster.getClusterName());
        for (org.apache.ambari.server.state.Service s : services) {
            java.util.Set<org.apache.ambari.server.state.ServiceComponent> components = new java.util.HashSet<>();
            if (request.getComponentName() != null) {
                components.add(s.getServiceComponent(request.getComponentName()));
            } else {
                components.addAll(s.getServiceComponents().values());
            }
            for (org.apache.ambari.server.state.ServiceComponent sc : components) {
                if (request.getComponentName() != null) {
                    if (!sc.getName().equals(request.getComponentName())) {
                        continue;
                    }
                }
                java.util.Map<java.lang.String, org.apache.ambari.server.state.ServiceComponentHost> serviceComponentHostMap = sc.getServiceComponentHosts();
                if (request.getHostname() != null) {
                    try {
                        if ((serviceComponentHostMap == null) || (!serviceComponentHostMap.containsKey(request.getHostname()))) {
                            throw new org.apache.ambari.server.ServiceComponentHostNotFoundException(cluster.getClusterName(), s.getName(), sc.getName(), request.getHostname());
                        }
                        org.apache.ambari.server.state.ServiceComponentHost sch = serviceComponentHostMap.get(request.getHostname());
                        if (null == sch) {
                            continue;
                        }
                        if (checkDesiredState && (desiredStateToCheck != sch.getDesiredState())) {
                            continue;
                        }
                        if (checkState && (stateToCheck != sch.getState())) {
                            continue;
                        }
                        if (request.getAdminState() != null) {
                            java.lang.String stringToMatch = (sch.getComponentAdminState() == null) ? "" : sch.getComponentAdminState().name();
                            if (!request.getAdminState().equals(stringToMatch)) {
                                continue;
                            }
                        }
                        org.apache.ambari.server.controller.ServiceComponentHostResponse r = (statusOnly) ? sch.convertToResponseStatusOnly(desiredConfigs, filterBasedConfigStaleness) : sch.convertToResponse(desiredConfigs);
                        if ((null == r) || (filterBasedConfigStaleness && (r.isStaleConfig() != staleConfig))) {
                            continue;
                        }
                        org.apache.ambari.server.state.Host host = hosts.get(sch.getHostName());
                        if (host == null) {
                            throw new org.apache.ambari.server.HostNotFoundException(cluster.getClusterName(), sch.getHostName());
                        }
                        org.apache.ambari.server.state.MaintenanceState effectiveMaintenanceState = maintenanceStateHelper.getEffectiveState(sch, host);
                        if (filterByMaintenanceState(request, effectiveMaintenanceState)) {
                            continue;
                        }
                        r.setMaintenanceState(effectiveMaintenanceState.name());
                        response.add(r);
                    } catch (org.apache.ambari.server.ServiceComponentHostNotFoundException e) {
                        if ((request.getServiceName() == null) || (request.getComponentName() == null)) {
                            org.apache.ambari.server.controller.AmbariManagementControllerImpl.LOG.debug("Ignoring not specified host_component ", e);
                        } else {
                            org.apache.ambari.server.controller.AmbariManagementControllerImpl.LOG.debug("ServiceComponentHost not found ", e);
                            throw new org.apache.ambari.server.ServiceComponentHostNotFoundException(cluster.getClusterName(), request.getServiceName(), request.getComponentName(), request.getHostname());
                        }
                    }
                } else {
                    for (org.apache.ambari.server.state.ServiceComponentHost sch : serviceComponentHostMap.values()) {
                        if (null == sch) {
                            continue;
                        }
                        if (checkDesiredState && (desiredStateToCheck != sch.getDesiredState())) {
                            continue;
                        }
                        if (checkState && (stateToCheck != sch.getState())) {
                            continue;
                        }
                        if (request.getAdminState() != null) {
                            java.lang.String stringToMatch = (sch.getComponentAdminState() == null) ? "" : sch.getComponentAdminState().name();
                            if (!request.getAdminState().equals(stringToMatch)) {
                                continue;
                            }
                        }
                        org.apache.ambari.server.controller.ServiceComponentHostResponse r = (statusOnly) ? sch.convertToResponseStatusOnly(desiredConfigs, filterBasedConfigStaleness) : sch.convertToResponse(desiredConfigs);
                        if ((null == r) || (filterBasedConfigStaleness && (r.isStaleConfig() != staleConfig))) {
                            continue;
                        }
                        org.apache.ambari.server.state.Host host = hosts.get(sch.getHostName());
                        if (host == null) {
                            throw new org.apache.ambari.server.HostNotFoundException(cluster.getClusterName(), sch.getHostName());
                        }
                        org.apache.ambari.server.state.MaintenanceState effectiveMaintenanceState = maintenanceStateHelper.getEffectiveState(sch, host);
                        if (filterByMaintenanceState(request, effectiveMaintenanceState)) {
                            continue;
                        }
                        r.setMaintenanceState(effectiveMaintenanceState.name());
                        response.add(r);
                    }
                }
            }
        }
        return response;
    }

    private boolean filterByMaintenanceState(org.apache.ambari.server.controller.ServiceComponentHostRequest request, org.apache.ambari.server.state.MaintenanceState effectiveMaintenanceState) {
        if (request.getMaintenanceState() != null) {
            org.apache.ambari.server.state.MaintenanceState desiredMaintenanceState = org.apache.ambari.server.state.MaintenanceState.valueOf(request.getMaintenanceState());
            if (desiredMaintenanceState.equals(org.apache.ambari.server.state.MaintenanceState.ON)) {
                if (effectiveMaintenanceState.equals(org.apache.ambari.server.state.MaintenanceState.OFF)) {
                    return true;
                }
            } else if (!desiredMaintenanceState.equals(effectiveMaintenanceState)) {
                return true;
            }
        }
        return false;
    }

    @java.lang.Override
    public org.apache.ambari.server.state.MaintenanceState getEffectiveMaintenanceState(org.apache.ambari.server.state.ServiceComponentHost sch) throws org.apache.ambari.server.AmbariException {
        return maintenanceStateHelper.getEffectiveState(sch);
    }

    private java.util.Set<org.apache.ambari.server.controller.ConfigurationResponse> getConfigurations(org.apache.ambari.server.controller.ConfigurationRequest request) throws org.apache.ambari.server.AmbariException {
        if (request.getClusterName() == null) {
            throw new java.lang.IllegalArgumentException("Invalid arguments, cluster name" + " should not be null");
        }
        org.apache.ambari.server.state.Cluster cluster = clusters.getCluster(request.getClusterName());
        java.util.Set<org.apache.ambari.server.controller.ConfigurationResponse> responses = new java.util.HashSet<>();
        if ((null != request.getType()) && (null != request.getVersionTag())) {
            org.apache.ambari.server.state.Config config = cluster.getConfig(request.getType(), request.getVersionTag());
            if (null != config) {
                org.apache.ambari.server.controller.ConfigurationResponse response = new org.apache.ambari.server.controller.ConfigurationResponse(cluster.getClusterName(), config);
                responses.add(response);
            }
        } else {
            boolean includeProps = request.includeProperties();
            if (null != request.getType()) {
                java.util.Map<java.lang.String, org.apache.ambari.server.state.Config> configs = cluster.getConfigsByType(request.getType());
                if (null != configs) {
                    for (java.util.Map.Entry<java.lang.String, org.apache.ambari.server.state.Config> entry : configs.entrySet()) {
                        org.apache.ambari.server.state.Config config = entry.getValue();
                        org.apache.ambari.server.controller.ConfigurationResponse response = new org.apache.ambari.server.controller.ConfigurationResponse(cluster.getClusterName(), config.getStackId(), request.getType(), config.getTag(), entry.getValue().getVersion(), includeProps ? config.getProperties() : new java.util.HashMap<>(), includeProps ? config.getPropertiesAttributes() : new java.util.HashMap<>(), config.getPropertiesTypes());
                        responses.add(response);
                    }
                }
            } else {
                java.util.Collection<org.apache.ambari.server.state.Config> all = cluster.getAllConfigs();
                for (org.apache.ambari.server.state.Config config : all) {
                    org.apache.ambari.server.controller.ConfigurationResponse response = new org.apache.ambari.server.controller.ConfigurationResponse(cluster.getClusterName(), config.getStackId(), config.getType(), config.getTag(), config.getVersion(), includeProps ? config.getProperties() : new java.util.HashMap<>(), includeProps ? config.getPropertiesAttributes() : new java.util.HashMap<>(), config.getPropertiesTypes());
                    responses.add(response);
                }
            }
        }
        return responses;
    }

    @java.lang.Override
    @com.google.inject.persist.Transactional
    public synchronized org.apache.ambari.server.controller.RequestStatusResponse updateClusters(java.util.Set<org.apache.ambari.server.controller.ClusterRequest> requests, java.util.Map<java.lang.String, java.lang.String> requestProperties) throws org.apache.ambari.server.AmbariException, org.apache.ambari.server.security.authorization.AuthorizationException {
        return updateClusters(requests, requestProperties, true, true);
    }

    @java.lang.Override
    @com.google.inject.persist.Transactional
    public synchronized org.apache.ambari.server.controller.RequestStatusResponse updateClusters(java.util.Set<org.apache.ambari.server.controller.ClusterRequest> requests, java.util.Map<java.lang.String, java.lang.String> requestProperties, boolean fireAgentUpdates, boolean refreshCluster) throws org.apache.ambari.server.AmbariException, org.apache.ambari.server.security.authorization.AuthorizationException {
        org.apache.ambari.server.controller.RequestStatusResponse response = null;
        for (org.apache.ambari.server.controller.ClusterRequest request : requests) {
            response = updateCluster(request, requestProperties, fireAgentUpdates, refreshCluster);
        }
        return response;
    }

    private java.util.Map<java.lang.String, java.lang.String> getConfigKeyDeltaToAction(org.apache.ambari.server.state.Config existingConfig, java.util.Map<java.lang.String, java.lang.String> newConfigValues) {
        java.util.Map<java.lang.String, java.lang.String> configsChanged = new java.util.HashMap<>();
        if (null != existingConfig) {
            java.util.Map<java.lang.String, java.lang.String> existingConfigValues = existingConfig.getProperties();
            java.util.Iterator it = existingConfigValues.entrySet().iterator();
            while (it.hasNext()) {
                java.util.Map.Entry<java.lang.String, java.lang.String> pair = ((java.util.Map.Entry) (it.next()));
                if (newConfigValues.containsKey(pair.getKey())) {
                    if (!newConfigValues.get(pair.getKey()).equals(pair.getValue())) {
                        configsChanged.put(pair.getKey(), "changed");
                    }
                } else {
                    configsChanged.put(pair.getKey(), "deleted");
                }
            } 
            it = newConfigValues.entrySet().iterator();
            while (it.hasNext()) {
                java.util.Map.Entry<java.lang.String, java.lang.String> pair = ((java.util.Map.Entry) (it.next()));
                if (!existingConfigValues.keySet().contains(pair.getKey())) {
                    configsChanged.put(pair.getKey(), "added");
                }
            } 
        } else {
            for (java.lang.String key : newConfigValues.keySet()) {
                configsChanged.put(key, "added");
            }
        }
        return configsChanged;
    }

    private java.util.Map<java.lang.String, java.util.List<java.lang.String>> inverseMapByValue(java.util.Map<java.lang.String, java.lang.String> configKeyToAction) {
        java.util.Map<java.lang.String, java.util.List<java.lang.String>> mapByValue = new java.util.HashMap<>();
        java.util.Iterator it = configKeyToAction.entrySet().iterator();
        while (it.hasNext()) {
            java.util.Map.Entry<java.lang.String, java.lang.String> pair = ((java.util.Map.Entry) (it.next()));
            if (mapByValue.containsKey(pair.getValue())) {
                mapByValue.get(pair.getValue()).add(pair.getKey());
            } else {
                java.util.List<java.lang.String> configListForAction = new java.util.ArrayList<>();
                configListForAction.add(pair.getKey());
                mapByValue.put(pair.getValue(), configListForAction);
            }
        } 
        return mapByValue;
    }

    private java.lang.String getActionToConfigListAsString(java.util.Map<java.lang.String, java.util.List<java.lang.String>> actionToConfigKeyList) {
        java.lang.String output = "";
        java.lang.String[] actions = new java.lang.String[]{ "added", "deleted", "changed" };
        int i = 0;
        for (java.lang.String action : actions) {
            i++;
            output += action + ": [";
            if (actionToConfigKeyList.containsKey(action)) {
                java.util.List<java.lang.String> values = actionToConfigKeyList.get(action);
                output += org.apache.commons.lang.StringUtils.join(values, ", ");
            }
            if (i < actions.length) {
                output += "], ";
            } else {
                output += "]";
            }
        }
        return output;
    }

    private synchronized org.apache.ambari.server.controller.RequestStatusResponse updateCluster(org.apache.ambari.server.controller.ClusterRequest request, java.util.Map<java.lang.String, java.lang.String> requestProperties, boolean fireAgentUpdates, boolean refreshCluster) throws org.apache.ambari.server.AmbariException, org.apache.ambari.server.security.authorization.AuthorizationException {
        org.apache.ambari.server.controller.internal.RequestStageContainer requestStageContainer = null;
        if ((request.getClusterId() == null) && ((request.getClusterName() == null) || request.getClusterName().isEmpty())) {
            throw new java.lang.IllegalArgumentException("Invalid arguments, cluster id or cluster name should not be null");
        }
        org.apache.ambari.server.controller.AmbariManagementControllerImpl.LOG.info(((((((("Received a updateCluster request" + ", clusterId=") + request.getClusterId()) + ", clusterName=") + request.getClusterName()) + ", securityType=") + request.getSecurityType()) + ", request=") + request);
        final org.apache.ambari.server.state.Cluster cluster;
        if (request.getClusterId() == null) {
            cluster = clusters.getCluster(request.getClusterName());
        } else {
            cluster = clusters.getClusterById(request.getClusterId());
        }
        java.util.List<org.apache.ambari.server.controller.ConfigurationRequest> desiredConfigs = request.getDesiredConfig();
        if (desiredConfigs != null) {
            for (org.apache.ambari.server.controller.ConfigurationRequest configurationRequest : desiredConfigs) {
                if (org.apache.commons.lang.StringUtils.isEmpty(configurationRequest.getVersionTag())) {
                    configurationRequest.setVersionTag(java.util.UUID.randomUUID().toString());
                }
            }
        }
        org.apache.ambari.server.security.authorization.AuthorizationHelper.verifyAuthorization(org.apache.ambari.server.security.authorization.ResourceType.CLUSTER, cluster.getResourceId(), org.apache.ambari.server.security.authorization.RoleAuthorization.AUTHORIZATIONS_UPDATE_CLUSTER);
        java.util.List<org.apache.ambari.server.controller.ConfigurationResponse> configurationResponses = new java.util.LinkedList<>();
        org.apache.ambari.server.controller.ServiceConfigVersionResponse serviceConfigVersionResponse = null;
        boolean nonServiceConfigsChanged = false;
        if ((desiredConfigs != null) && (request.getServiceConfigVersionRequest() != null)) {
            java.lang.String msg = "Unable to set desired configs and rollback at same time, request = " + request;
            org.apache.ambari.server.controller.AmbariManagementControllerImpl.LOG.error(msg);
            throw new java.lang.IllegalArgumentException(msg);
        }
        if ((request.getClusterName() != null) && (!cluster.getClusterName().equals(request.getClusterName()))) {
            org.apache.ambari.server.controller.AmbariManagementControllerImpl.validateClusterName(request.getClusterName());
            if (org.apache.ambari.server.controller.AmbariManagementControllerImpl.LOG.isDebugEnabled()) {
                org.apache.ambari.server.controller.AmbariManagementControllerImpl.LOG.debug("Received cluster name change request from {} to {}", cluster.getClusterName(), request.getClusterName());
            }
            if (!org.apache.ambari.server.security.authorization.AuthorizationHelper.isAuthorized(org.apache.ambari.server.security.authorization.ResourceType.AMBARI, null, java.util.EnumSet.of(org.apache.ambari.server.security.authorization.RoleAuthorization.AMBARI_RENAME_CLUSTER))) {
                throw new org.apache.ambari.server.security.authorization.AuthorizationException("The authenticated user does not have authorization to rename the cluster");
            }
            cluster.setClusterName(request.getClusterName());
        }
        boolean isConfigurationCreationNeeded = false;
        if (desiredConfigs != null) {
            for (org.apache.ambari.server.controller.ConfigurationRequest desiredConfig : desiredConfigs) {
                java.util.Map<java.lang.String, java.lang.String> requestConfigProperties = desiredConfig.getProperties();
                java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> requestConfigAttributes = desiredConfig.getPropertiesAttributes();
                if ((requestConfigProperties != null) && (!requestConfigProperties.isEmpty())) {
                    java.util.Map<org.apache.ambari.server.state.PropertyInfo.PropertyType, java.util.Set<java.lang.String>> propertiesTypes = cluster.getConfigPropertiesTypes(desiredConfig.getType());
                    for (java.util.Map.Entry<java.lang.String, java.lang.String> property : requestConfigProperties.entrySet()) {
                        java.lang.String propertyName = property.getKey();
                        java.lang.String propertyValue = property.getValue();
                        if ((propertiesTypes.containsKey(org.apache.ambari.server.state.PropertyInfo.PropertyType.PASSWORD) && propertiesTypes.get(org.apache.ambari.server.state.PropertyInfo.PropertyType.PASSWORD).contains(propertyName)) || ((((requestConfigAttributes != null) && requestConfigAttributes.containsKey(org.apache.ambari.server.controller.AmbariManagementControllerImpl.PASSWORD)) && requestConfigAttributes.get(org.apache.ambari.server.controller.AmbariManagementControllerImpl.PASSWORD).containsKey(propertyName)) && requestConfigAttributes.get(org.apache.ambari.server.controller.AmbariManagementControllerImpl.PASSWORD).get(propertyName).equals("true"))) {
                            if (org.apache.ambari.server.utils.SecretReference.isSecret(propertyValue)) {
                                org.apache.ambari.server.utils.SecretReference ref = new org.apache.ambari.server.utils.SecretReference(propertyValue, cluster);
                                requestConfigProperties.put(propertyName, ref.getValue());
                            }
                        }
                    }
                }
                org.apache.ambari.server.state.Config clusterConfig = cluster.getDesiredConfigByType(desiredConfig.getType());
                java.util.Map<java.lang.String, java.lang.String> clusterConfigProperties = null;
                java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> clusterConfigAttributes = null;
                if (clusterConfig != null) {
                    clusterConfigProperties = clusterConfig.getProperties();
                    clusterConfigAttributes = clusterConfig.getPropertiesAttributes();
                    if (!isAttributeMapsEqual(requestConfigAttributes, clusterConfigAttributes)) {
                        isConfigurationCreationNeeded = true;
                        break;
                    }
                } else {
                    isConfigurationCreationNeeded = true;
                    break;
                }
                if ((requestConfigProperties == null) || requestConfigProperties.isEmpty()) {
                    org.apache.ambari.server.state.Config existingConfig = cluster.getConfig(desiredConfig.getType(), desiredConfig.getVersionTag());
                    if (existingConfig != null) {
                        if (!org.apache.commons.lang.StringUtils.equals(existingConfig.getTag(), clusterConfig.getTag())) {
                            isConfigurationCreationNeeded = true;
                            break;
                        }
                    }
                }
                if ((requestConfigProperties != null) && (clusterConfigProperties != null)) {
                    if (requestConfigProperties.size() != clusterConfigProperties.size()) {
                        isConfigurationCreationNeeded = true;
                        break;
                    } else {
                        if ((cluster.getServiceByConfigType(clusterConfig.getType()) != null) && clusterConfig.getServiceConfigVersions().isEmpty()) {
                            org.apache.ambari.server.controller.AmbariManagementControllerImpl.LOG.warn("Existing desired config doesn't belong to any service config version, " + ("forcing config recreation, " + "clusterName={}, type = {}, tag={}"), cluster.getClusterName(), clusterConfig.getType(), clusterConfig.getTag());
                            isConfigurationCreationNeeded = true;
                            break;
                        }
                        for (java.util.Map.Entry<java.lang.String, java.lang.String> property : requestConfigProperties.entrySet()) {
                            if (!org.apache.commons.lang.StringUtils.equals(property.getValue(), clusterConfigProperties.get(property.getKey()))) {
                                isConfigurationCreationNeeded = true;
                                break;
                            }
                        }
                    }
                }
            }
        }
        if (isConfigurationCreationNeeded) {
            if (!desiredConfigs.isEmpty()) {
                java.util.Set<org.apache.ambari.server.state.Config> configs = new java.util.HashSet<>();
                java.lang.String note = null;
                for (org.apache.ambari.server.controller.ConfigurationRequest cr : desiredConfigs) {
                    java.lang.String configType = cr.getType();
                    if (null != cr.getProperties()) {
                        java.util.Map<java.lang.String, org.apache.ambari.server.state.Config> all = cluster.getConfigsByType(configType);
                        if (((null == all) || (!all.containsKey(cr.getVersionTag()))) || (cr.getProperties().size() > 0)) {
                            cr.setClusterName(cluster.getClusterName());
                            configurationResponses.add(createConfiguration(cr, refreshCluster));
                            org.apache.ambari.server.controller.AmbariManagementControllerImpl.LOG.info(java.text.MessageFormat.format("Applying configuration with tag ''{0}'' to cluster ''{1}''  for configuration type {2}", cr.getVersionTag(), request.getClusterName(), configType));
                        }
                    }
                    note = cr.getServiceConfigVersionNote();
                    org.apache.ambari.server.state.Config config = cluster.getConfig(configType, cr.getVersionTag());
                    if (null != config) {
                        configs.add(config);
                    }
                }
                if (!configs.isEmpty()) {
                    java.util.Map<java.lang.String, org.apache.ambari.server.state.Config> existingConfigTypeToConfig = new java.util.HashMap();
                    for (org.apache.ambari.server.state.Config config : configs) {
                        org.apache.ambari.server.state.Config existingConfig = cluster.getDesiredConfigByType(config.getType());
                        existingConfigTypeToConfig.put(config.getType(), existingConfig);
                    }
                    java.lang.String authName = getAuthName();
                    serviceConfigVersionResponse = cluster.addDesiredConfig(authName, configs, note);
                    if (serviceConfigVersionResponse != null) {
                        java.util.List<java.lang.String> hosts = serviceConfigVersionResponse.getHosts();
                        int numAffectedHosts = (null != hosts) ? hosts.size() : 0;
                        org.apache.ambari.server.controller.AmbariManagementControllerImpl.configChangeLog.info("(configchange) Changing default config. cluster: '{}', changed by: '{}', service_name: '{}', config_group: '{}', num affected hosts during creation: '{}', note: '{}'", request.getClusterName(), authName, serviceConfigVersionResponse.getServiceName(), serviceConfigVersionResponse.getGroupName(), numAffectedHosts, serviceConfigVersionResponse.getNote());
                        for (org.apache.ambari.server.state.Config config : configs) {
                            config.getVersion();
                            serviceConfigVersionResponse.getNote();
                            org.apache.ambari.server.controller.AmbariManagementControllerImpl.configChangeLog.info("(configchange)    type: '{}', tag: '{}', version: '{}'", config.getType(), config.getTag(), config.getVersion());
                            java.util.Map<java.lang.String, java.lang.String> configKeyToAction = getConfigKeyDeltaToAction(existingConfigTypeToConfig.get(config.getType()), config.getProperties());
                            java.util.Map<java.lang.String, java.util.List<java.lang.String>> actionToListConfigKeys = inverseMapByValue(configKeyToAction);
                            if (!actionToListConfigKeys.isEmpty()) {
                                java.lang.String configOutput = getActionToConfigListAsString(actionToListConfigKeys);
                                org.apache.ambari.server.controller.AmbariManagementControllerImpl.configChangeLog.info("(configchange)    Config type '{}' was modified with the following keys, {}", config.getType(), configOutput);
                            }
                        }
                    } else {
                        nonServiceConfigsChanged = true;
                    }
                }
            }
        }
        org.apache.ambari.server.state.StackId currentVersion = cluster.getCurrentStackVersion();
        org.apache.ambari.server.state.StackId desiredVersion = cluster.getDesiredStackVersion();
        if (currentVersion == null) {
            if (!org.apache.ambari.server.security.authorization.AuthorizationHelper.isAuthorized(org.apache.ambari.server.security.authorization.ResourceType.CLUSTER, cluster.getResourceId(), java.util.EnumSet.of(org.apache.ambari.server.security.authorization.RoleAuthorization.CLUSTER_UPGRADE_DOWNGRADE_STACK))) {
                throw new org.apache.ambari.server.security.authorization.AuthorizationException("The authenticated user does not have authorization to modify stack version");
            }
            cluster.setCurrentStackVersion(desiredVersion);
        }
        boolean requiresHostListUpdate = (request.getHostNames() != null) && (!request.getHostNames().isEmpty());
        if (requiresHostListUpdate) {
            clusters.mapAndPublishHostsToCluster(request.getHostNames(), request.getClusterName());
        }
        if (null != request.getProvisioningState()) {
            org.apache.ambari.server.state.State oldProvisioningState = cluster.getProvisioningState();
            org.apache.ambari.server.state.State provisioningState = org.apache.ambari.server.state.State.valueOf(request.getProvisioningState());
            if ((provisioningState != org.apache.ambari.server.state.State.INIT) && (provisioningState != org.apache.ambari.server.state.State.INSTALLED)) {
                org.apache.ambari.server.controller.AmbariManagementControllerImpl.LOG.warn("Invalid cluster provisioning state {} cannot be set on the cluster {}", provisioningState, request.getClusterName());
                throw new java.lang.IllegalArgumentException((("Invalid cluster provisioning state " + provisioningState) + " cannot be set on cluster ") + request.getClusterName());
            }
            if (provisioningState != oldProvisioningState) {
                boolean isStateTransitionValid = org.apache.ambari.server.state.State.isValidDesiredStateTransition(oldProvisioningState, provisioningState);
                if (!isStateTransitionValid) {
                    org.apache.ambari.server.controller.AmbariManagementControllerImpl.LOG.warn("Invalid cluster provisioning state {} cannot be set on the cluster {} because the current state is {}", provisioningState, request.getClusterName(), oldProvisioningState);
                    throw new org.apache.ambari.server.AmbariException(((((((("Invalid transition for" + (" cluster provisioning state" + ", clusterName=")) + cluster.getClusterName()) + ", clusterId=") + cluster.getClusterId()) + ", currentProvisioningState=") + oldProvisioningState) + ", newProvisioningState=") + provisioningState);
                }
            }
            cluster.setProvisioningState(provisioningState);
        }
        if (null != request.getServiceConfigVersionRequest()) {
            if (!org.apache.ambari.server.security.authorization.AuthorizationHelper.isAuthorized(org.apache.ambari.server.security.authorization.ResourceType.CLUSTER, cluster.getResourceId(), java.util.EnumSet.of(org.apache.ambari.server.security.authorization.RoleAuthorization.SERVICE_MODIFY_CONFIGS))) {
                throw new org.apache.ambari.server.security.authorization.AuthorizationException("The authenticated user does not have authorization to modify service configurations");
            }
            org.apache.ambari.server.controller.ServiceConfigVersionRequest serviceConfigVersionRequest = request.getServiceConfigVersionRequest();
            if (org.apache.commons.lang.StringUtils.isEmpty(serviceConfigVersionRequest.getServiceName()) || (null == serviceConfigVersionRequest.getVersion())) {
                java.lang.String msg = "Service name and version should be specified in service config version";
                org.apache.ambari.server.controller.AmbariManagementControllerImpl.LOG.error(msg);
                throw new java.lang.IllegalArgumentException(msg);
            }
            serviceConfigVersionResponse = cluster.setServiceConfigVersion(serviceConfigVersionRequest.getServiceName(), serviceConfigVersionRequest.getVersion(), getAuthName(), serviceConfigVersionRequest.getNote());
        }
        if (serviceConfigVersionResponse != null) {
            if (!configurationResponses.isEmpty()) {
                serviceConfigVersionResponse.setConfigurations(configurationResponses);
            }
            org.apache.ambari.server.controller.ClusterResponse clusterResponse = new org.apache.ambari.server.controller.ClusterResponse(cluster.getClusterId(), cluster.getClusterName(), null, null, null, 0, null, null);
            java.util.Map<java.lang.String, java.util.Collection<org.apache.ambari.server.controller.ServiceConfigVersionResponse>> map = new java.util.HashMap<>();
            map.put(serviceConfigVersionResponse.getServiceName(), java.util.Collections.singletonList(serviceConfigVersionResponse));
            clusterResponse.setDesiredServiceConfigVersions(map);
            saveClusterUpdate(request, clusterResponse);
        }
        org.apache.ambari.server.state.SecurityType securityType = request.getSecurityType();
        if (securityType != null) {
            if (kerberosHelper.shouldExecuteCustomOperations(securityType, requestProperties)) {
                if (!org.apache.ambari.server.security.authorization.AuthorizationHelper.isAuthorized(org.apache.ambari.server.security.authorization.ResourceType.CLUSTER, cluster.getResourceId(), java.util.EnumSet.of(org.apache.ambari.server.security.authorization.RoleAuthorization.CLUSTER_TOGGLE_KERBEROS))) {
                    throw new org.apache.ambari.server.security.authorization.AuthorizationException("The authenticated user does not have authorization to perform Kerberos-specific operations");
                }
                try {
                    requestStageContainer = kerberosHelper.executeCustomOperations(cluster, requestProperties, requestStageContainer, kerberosHelper.getManageIdentitiesDirective(requestProperties));
                } catch (org.apache.ambari.server.serveraction.kerberos.KerberosOperationException e) {
                    throw new java.lang.IllegalArgumentException(e.getMessage(), e);
                }
            } else {
                boolean forceToggleKerberos = kerberosHelper.getForceToggleKerberosDirective(requestProperties);
                if (forceToggleKerberos || (cluster.getSecurityType() != securityType)) {
                    org.apache.ambari.server.controller.AmbariManagementControllerImpl.LOG.info("Received cluster security type change request from {} to {} (forced: {})", cluster.getSecurityType().name(), securityType.name(), forceToggleKerberos);
                    if ((securityType == org.apache.ambari.server.state.SecurityType.KERBEROS) || (securityType == org.apache.ambari.server.state.SecurityType.NONE)) {
                        if (!org.apache.ambari.server.security.authorization.AuthorizationHelper.isAuthorized(org.apache.ambari.server.security.authorization.ResourceType.CLUSTER, cluster.getResourceId(), java.util.EnumSet.of(org.apache.ambari.server.security.authorization.RoleAuthorization.CLUSTER_TOGGLE_KERBEROS))) {
                            throw new org.apache.ambari.server.security.authorization.AuthorizationException("The authenticated user does not have authorization to enable or disable Kerberos");
                        }
                        try {
                            requestStageContainer = kerberosHelper.toggleKerberos(cluster, securityType, requestStageContainer, kerberosHelper.getManageIdentitiesDirective(requestProperties));
                        } catch (org.apache.ambari.server.serveraction.kerberos.KerberosOperationException e) {
                            throw new java.lang.IllegalArgumentException(e.getMessage(), e);
                        }
                    } else {
                        throw new java.lang.IllegalArgumentException(java.lang.String.format("Unexpected security type encountered: %s", securityType.name()));
                    }
                    cluster.setSecurityType(securityType);
                }
            }
        }
        if (fireAgentUpdates && ((serviceConfigVersionResponse != null) || nonServiceConfigsChanged)) {
            configHelper.updateAgentConfigs(java.util.Collections.singleton(cluster.getClusterName()));
        }
        if (requestStageContainer != null) {
            requestStageContainer.persist();
            return requestStageContainer.getRequestStatusResponse();
        } else {
            return null;
        }
    }

    public static void validateClusterName(java.lang.String clusterName) {
        if (clusterName == null) {
            throw new java.lang.IllegalArgumentException("Invalid arguments, cluster name should not be null");
        }
        if (clusterName.isEmpty()) {
            throw new java.lang.IllegalArgumentException("Invalid arguments, cluster name should not be empty");
        }
        java.util.regex.Matcher mtch = org.apache.ambari.server.controller.AmbariManagementControllerImpl.CLUSTER_NAME_PTRN.matcher(clusterName);
        if (!mtch.matches()) {
            throw new java.lang.IllegalArgumentException("Invalid arguments, cluster name should contains only alphabetical, numeric, '_' and '-' characters and length 1-100 characters");
        }
    }

    private java.util.Map<java.lang.String, java.lang.String[]> getPropertyChanges(org.apache.ambari.server.state.Cluster cluster, org.apache.ambari.server.controller.ConfigurationRequest request) {
        java.util.Map<java.lang.String, java.lang.String[]> changedProperties = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.lang.String> requestedProperties = request.getProperties();
        if (requestedProperties == null) {
            requestedProperties = java.util.Collections.emptyMap();
        }
        org.apache.ambari.server.state.Config existingConfig = cluster.getDesiredConfigByType(request.getType());
        java.util.Map<java.lang.String, java.lang.String> existingProperties = (existingConfig == null) ? null : existingConfig.getProperties();
        if (existingProperties == null) {
            existingProperties = java.util.Collections.emptyMap();
        }
        java.util.Set<java.lang.String> propertyNames = new java.util.HashSet<>();
        propertyNames.addAll(requestedProperties.keySet());
        propertyNames.addAll(existingProperties.keySet());
        for (java.lang.String propertyName : propertyNames) {
            java.lang.String requestedValue = requestedProperties.get(propertyName);
            java.lang.String existingValue = existingProperties.get(propertyName);
            if (requestedValue == null ? existingValue != null : !requestedValue.equals(existingValue)) {
                changedProperties.put(propertyName, new java.lang.String[]{ existingValue, requestedValue });
            }
        }
        return changedProperties;
    }

    public boolean isAttributeMapsEqual(java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> requestConfigAttributes, java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> clusterConfigAttributes) {
        boolean isAttributesEqual = true;
        if ((((requestConfigAttributes != null) && (clusterConfigAttributes == null)) || ((requestConfigAttributes == null) && (clusterConfigAttributes != null))) || (((requestConfigAttributes != null) && (clusterConfigAttributes != null)) && (!requestConfigAttributes.keySet().equals(clusterConfigAttributes.keySet())))) {
            return false;
        } else if ((clusterConfigAttributes != null) && (requestConfigAttributes != null)) {
            for (java.util.Map.Entry<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> ClusterEntrySet : clusterConfigAttributes.entrySet()) {
                java.util.Map<java.lang.String, java.lang.String> clusterMapAttributes = ClusterEntrySet.getValue();
                java.util.Map<java.lang.String, java.lang.String> requestMapAttributes = requestConfigAttributes.get(ClusterEntrySet.getKey());
                if ((((requestMapAttributes != null) && (clusterMapAttributes == null)) || ((requestMapAttributes == null) && (clusterMapAttributes != null))) || (((requestMapAttributes != null) && (clusterMapAttributes != null)) && (!requestMapAttributes.keySet().equals(clusterMapAttributes.keySet())))) {
                    return false;
                } else if ((requestMapAttributes != null) && (clusterMapAttributes != null)) {
                    for (java.util.Map.Entry<java.lang.String, java.lang.String> requestPropertyEntrySet : requestMapAttributes.entrySet()) {
                        java.lang.String requestPropertyValue = requestPropertyEntrySet.getValue();
                        java.lang.String clusterPropertyValue = clusterMapAttributes.get(requestPropertyEntrySet.getKey());
                        if ((((requestPropertyValue != null) && (clusterPropertyValue == null)) || ((requestPropertyValue == null) && (clusterPropertyValue != null))) || (((requestPropertyValue != null) && (clusterPropertyValue != null)) && (!requestPropertyValue.equals(clusterPropertyValue)))) {
                            return false;
                        }
                    }
                }
            }
        }
        return isAttributesEqual;
    }

    public void saveClusterUpdate(org.apache.ambari.server.controller.ClusterRequest clusterRequest, org.apache.ambari.server.controller.ClusterResponse clusterResponse) {
        clusterUpdateCache.put(clusterRequest, clusterResponse);
    }

    @java.lang.Override
    public org.apache.ambari.server.controller.ClusterResponse getClusterUpdateResults(org.apache.ambari.server.controller.ClusterRequest clusterRequest) {
        return clusterUpdateCache.getIfPresent(clusterRequest);
    }

    @java.lang.Override
    public org.apache.ambari.server.controller.ConfigGroupResponse getConfigGroupUpdateResults(org.apache.ambari.server.controller.ConfigGroupRequest configGroupRequest) {
        return configGroupUpdateCache.getIfPresent(configGroupRequest);
    }

    @java.lang.Override
    public void saveConfigGroupUpdate(org.apache.ambari.server.controller.ConfigGroupRequest configGroupRequest, org.apache.ambari.server.controller.ConfigGroupResponse configGroupResponse) {
        configGroupUpdateCache.put(configGroupRequest, configGroupResponse);
    }

    @java.lang.Override
    public java.lang.String getJobTrackerHost(org.apache.ambari.server.state.Cluster cluster) {
        try {
            org.apache.ambari.server.state.Service svc = cluster.getService("MAPREDUCE");
            org.apache.ambari.server.state.ServiceComponent sc = svc.getServiceComponent(org.apache.ambari.server.Role.JOBTRACKER.toString());
            if ((sc.getServiceComponentHosts() != null) && (!sc.getServiceComponentHosts().isEmpty())) {
                return sc.getServiceComponentHosts().keySet().iterator().next();
            }
        } catch (org.apache.ambari.server.AmbariException ex) {
            return null;
        }
        return null;
    }

    private java.util.Set<java.lang.String> getServicesForSmokeTests(org.apache.ambari.server.state.Cluster cluster, java.util.Map<org.apache.ambari.server.state.State, java.util.List<org.apache.ambari.server.state.Service>> changedServices, java.util.Map<java.lang.String, java.util.Map<org.apache.ambari.server.state.State, java.util.List<org.apache.ambari.server.state.ServiceComponentHost>>> changedScHosts, boolean runSmokeTest) throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.server.controller.spi.Resource.Type opLvl = org.apache.ambari.server.controller.spi.Resource.Type.Cluster;
        java.util.Set<java.lang.String> smokeTestServices = new java.util.HashSet<>();
        if (changedServices != null) {
            for (java.util.Map.Entry<org.apache.ambari.server.state.State, java.util.List<org.apache.ambari.server.state.Service>> entry : changedServices.entrySet()) {
                if (org.apache.ambari.server.state.State.STARTED != entry.getKey()) {
                    continue;
                }
                for (org.apache.ambari.server.state.Service s : entry.getValue()) {
                    if (runSmokeTest && ((org.apache.ambari.server.state.State.INSTALLED == s.getDesiredState()) && maintenanceStateHelper.isOperationAllowed(opLvl, s))) {
                        smokeTestServices.add(s.getName());
                    }
                }
            }
        }
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.Integer>> changedComponentCount = new java.util.HashMap<>();
        for (java.util.Map<org.apache.ambari.server.state.State, java.util.List<org.apache.ambari.server.state.ServiceComponentHost>> stateScHostMap : changedScHosts.values()) {
            for (java.util.Map.Entry<org.apache.ambari.server.state.State, java.util.List<org.apache.ambari.server.state.ServiceComponentHost>> entry : stateScHostMap.entrySet()) {
                if (org.apache.ambari.server.state.State.STARTED != entry.getKey()) {
                    continue;
                }
                for (org.apache.ambari.server.state.ServiceComponentHost sch : entry.getValue()) {
                    if (org.apache.ambari.server.state.State.INSTALLED != sch.getState()) {
                        continue;
                    }
                    if (!maintenanceStateHelper.isOperationAllowed(opLvl, sch)) {
                        continue;
                    }
                    if (!changedComponentCount.containsKey(sch.getServiceName())) {
                        changedComponentCount.put(sch.getServiceName(), new java.util.HashMap<>());
                    }
                    if (!changedComponentCount.get(sch.getServiceName()).containsKey(sch.getServiceComponentName())) {
                        changedComponentCount.get(sch.getServiceName()).put(sch.getServiceComponentName(), 1);
                    } else {
                        java.lang.Integer i = changedComponentCount.get(sch.getServiceName()).get(sch.getServiceComponentName());
                        changedComponentCount.get(sch.getServiceName()).put(sch.getServiceComponentName(), ++i);
                    }
                }
            }
        }
        for (java.util.Map.Entry<java.lang.String, java.util.Map<java.lang.String, java.lang.Integer>> entry : changedComponentCount.entrySet()) {
            java.lang.String serviceName = entry.getKey();
            org.apache.ambari.server.state.Service s = cluster.getService(serviceName);
            if ((runSmokeTest && (entry.getValue().size() > 1)) && maintenanceStateHelper.isOperationAllowed(opLvl, s)) {
                smokeTestServices.add(serviceName);
                continue;
            }
            for (java.lang.String componentName : changedComponentCount.get(serviceName).keySet()) {
                org.apache.ambari.server.state.ServiceComponent sc = cluster.getService(serviceName).getServiceComponent(componentName);
                org.apache.ambari.server.state.StackId stackId = sc.getDesiredStackId();
                org.apache.ambari.server.state.ComponentInfo compInfo = ambariMetaInfo.getComponent(stackId.getStackName(), stackId.getStackVersion(), serviceName, componentName);
                if ((runSmokeTest && compInfo.isMaster()) && maintenanceStateHelper.isOperationAllowed(opLvl, s)) {
                    smokeTestServices.add(serviceName);
                }
            }
        }
        return smokeTestServices;
    }

    private void addClientSchForReinstall(org.apache.ambari.server.state.Cluster cluster, java.util.Map<org.apache.ambari.server.state.State, java.util.List<org.apache.ambari.server.state.Service>> changedServices, java.util.Map<java.lang.String, java.util.Map<org.apache.ambari.server.state.State, java.util.List<org.apache.ambari.server.state.ServiceComponentHost>>> changedScHosts) throws org.apache.ambari.server.AmbariException {
        java.util.Set<java.lang.String> services = new java.util.HashSet<>();
        if (changedServices != null) {
            for (java.util.Map.Entry<org.apache.ambari.server.state.State, java.util.List<org.apache.ambari.server.state.Service>> entry : changedServices.entrySet()) {
                if (org.apache.ambari.server.state.State.STARTED != entry.getKey()) {
                    continue;
                }
                for (org.apache.ambari.server.state.Service s : entry.getValue()) {
                    if (org.apache.ambari.server.state.State.INSTALLED == s.getDesiredState()) {
                        services.add(s.getName());
                    }
                }
            }
        }
        java.util.List<org.apache.ambari.server.state.ServiceComponentHost> serviceComponentHosts = new java.util.ArrayList<>();
        if ((changedScHosts != null) && (!changedScHosts.isEmpty())) {
            for (java.util.Map.Entry<java.lang.String, java.util.Map<org.apache.ambari.server.state.State, java.util.List<org.apache.ambari.server.state.ServiceComponentHost>>> stringMapEntry : changedScHosts.entrySet()) {
                for (org.apache.ambari.server.state.State state : stringMapEntry.getValue().keySet()) {
                    if (state == org.apache.ambari.server.state.State.STARTED) {
                        serviceComponentHosts.addAll(stringMapEntry.getValue().get(state));
                    }
                }
            }
        }
        if (!serviceComponentHosts.isEmpty()) {
            for (org.apache.ambari.server.state.ServiceComponentHost sch : serviceComponentHosts) {
                services.add(sch.getServiceName());
            }
        }
        if (services.isEmpty()) {
            return;
        }
        java.util.Map<java.lang.String, java.util.List<org.apache.ambari.server.state.ServiceComponentHost>> clientSchs = new java.util.HashMap<>();
        for (java.lang.String serviceName : services) {
            org.apache.ambari.server.state.Service s = cluster.getService(serviceName);
            for (java.lang.String component : s.getServiceComponents().keySet()) {
                java.util.List<org.apache.ambari.server.state.ServiceComponentHost> potentialHosts = new java.util.ArrayList<>();
                org.apache.ambari.server.state.ServiceComponent sc = s.getServiceComponents().get(component);
                if (sc.isClientComponent()) {
                    for (org.apache.ambari.server.state.ServiceComponentHost potentialSch : sc.getServiceComponentHosts().values()) {
                        org.apache.ambari.server.state.Host host = clusters.getHost(potentialSch.getHostName());
                        if (((!potentialSch.getHostState().equals(org.apache.ambari.server.state.HostState.HEARTBEAT_LOST)) && (potentialSch.getMaintenanceState() != org.apache.ambari.server.state.MaintenanceState.ON)) && (host.getMaintenanceState(cluster.getClusterId()) == org.apache.ambari.server.state.MaintenanceState.OFF)) {
                            potentialHosts.add(potentialSch);
                        }
                    }
                }
                if (!potentialHosts.isEmpty()) {
                    clientSchs.put(sc.getName(), potentialHosts);
                }
            }
        }
        org.apache.ambari.server.controller.AmbariManagementControllerImpl.LOG.info("Client hosts for reinstall : " + clientSchs.size());
        if (changedScHosts != null) {
            for (java.util.Map.Entry<java.lang.String, java.util.List<org.apache.ambari.server.state.ServiceComponentHost>> stringListEntry : clientSchs.entrySet()) {
                java.util.Map<org.apache.ambari.server.state.State, java.util.List<org.apache.ambari.server.state.ServiceComponentHost>> schMap = new java.util.EnumMap<>(org.apache.ambari.server.state.State.class);
                schMap.put(org.apache.ambari.server.state.State.INSTALLED, stringListEntry.getValue());
                changedScHosts.put(stringListEntry.getKey(), schMap);
            }
        }
    }

    @java.lang.Override
    public java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> findConfigurationTagsWithOverrides(org.apache.ambari.server.state.Cluster cluster, java.lang.String hostName, @javax.annotation.Nullable
    java.util.Map<java.lang.String, org.apache.ambari.server.state.DesiredConfig> desiredConfigs) throws org.apache.ambari.server.AmbariException {
        return configHelper.getEffectiveDesiredTags(cluster, hostName, desiredConfigs);
    }

    @java.lang.Override
    public org.apache.ambari.server.state.scheduler.RequestExecutionFactory getRequestExecutionFactory() {
        return requestExecutionFactory;
    }

    @java.lang.Override
    public org.apache.ambari.server.scheduler.ExecutionScheduleManager getExecutionScheduleManager() {
        return executionScheduleManager;
    }

    private void createHostAction(org.apache.ambari.server.state.Cluster cluster, org.apache.ambari.server.actionmanager.Stage stage, org.apache.ambari.server.state.ServiceComponentHost scHost, org.apache.ambari.server.RoleCommand roleCommand, java.util.Map<java.lang.String, java.lang.String> commandParamsInp, org.apache.ambari.server.state.ServiceComponentHostEvent event, boolean skipFailure, org.apache.ambari.server.orm.entities.RepositoryVersionEntity repoVersion, boolean isUpgradeSuspended, org.apache.ambari.server.configuration.Configuration.DatabaseType databaseType, java.util.Map<java.lang.String, org.apache.ambari.server.state.DesiredConfig> clusterDesiredConfigs, boolean useLatestConfigs) throws org.apache.ambari.server.AmbariException {
        java.lang.String serviceName = scHost.getServiceName();
        stage.addHostRoleExecutionCommand(scHost.getHost(), org.apache.ambari.server.Role.valueOf(scHost.getServiceComponentName()), roleCommand, event, cluster, serviceName, false, skipFailure);
        java.lang.String componentName = scHost.getServiceComponentName();
        java.lang.String hostname = scHost.getHostName();
        org.apache.ambari.server.state.Host host = clusters.getHost(hostname);
        org.apache.ambari.server.orm.entities.HostEntity hostEntity = host.getHostEntity();
        java.util.Map<java.lang.String, java.lang.String> hostAttributes = gson.fromJson(hostEntity.getHostAttributes(), org.apache.ambari.server.controller.AmbariManagementControllerImpl.hostAttributesType);
        java.lang.String osFamily = host.getOSFamilyFromHostAttributes(hostAttributes);
        org.apache.ambari.server.state.StackId stackId = scHost.getServiceComponent().getDesiredStackId();
        org.apache.ambari.server.state.ServiceInfo serviceInfo = ambariMetaInfo.getService(stackId.getStackName(), stackId.getStackVersion(), serviceName);
        org.apache.ambari.server.state.ComponentInfo componentInfo = ambariMetaInfo.getComponent(stackId.getStackName(), stackId.getStackVersion(), serviceName, componentName);
        org.apache.ambari.server.state.StackInfo stackInfo = ambariMetaInfo.getStack(stackId.getStackName(), stackId.getStackVersion());
        java.util.Map<java.lang.String, org.apache.ambari.server.state.ServiceInfo> servicesMap = ambariMetaInfo.getServices(stackInfo.getName(), stackInfo.getVersion());
        org.apache.ambari.server.actionmanager.ExecutionCommandWrapper execCmdWrapper = stage.getExecutionCommandWrapper(hostname, componentName);
        org.apache.ambari.server.agent.ExecutionCommand execCmd = execCmdWrapper.getExecutionCommand();
        execCmd.setConfigurations(new java.util.TreeMap<>());
        org.apache.ambari.server.state.Service clusterService = cluster.getService(serviceName);
        execCmd.setCredentialStoreEnabled(java.lang.String.valueOf(clusterService.isCredentialStoreEnabled()));
        org.apache.ambari.server.state.ServiceComponent component = clusterService.getServiceComponent(componentName);
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> configCredentials;
        configCredentials = configCredentialsForService.get(clusterService.getName());
        if (configCredentials == null) {
            configCredentials = configHelper.getCredentialStoreEnabledProperties(stackId, clusterService);
            configCredentialsForService.put(clusterService.getName(), configCredentials);
        }
        execCmd.setConfigurationCredentials(configCredentials);
        java.util.Map<java.lang.String, java.lang.String> commandParams = new java.util.TreeMap<>();
        if (commandParamsInp != null) {
            commandParams.putAll(commandParamsInp);
        }
        boolean isInstallCommand = roleCommand.equals(org.apache.ambari.server.RoleCommand.INSTALL);
        java.lang.String agentDefaultCommandTimeout = configs.getDefaultAgentTaskTimeout(isInstallCommand);
        java.lang.String scriptCommandTimeout = "";
        org.apache.ambari.server.state.CommandScriptDefinition script = componentInfo.getCommandScript();
        if (serviceInfo.getSchemaVersion().equals(org.apache.ambari.server.api.services.AmbariMetaInfo.SCHEMA_VERSION_2)) {
            if (script != null) {
                commandParams.put(org.apache.ambari.server.agent.ExecutionCommand.KeyNames.SCRIPT, script.getScript());
                commandParams.put(org.apache.ambari.server.agent.ExecutionCommand.KeyNames.SCRIPT_TYPE, script.getScriptType().toString());
                boolean retryEnabled = false;
                java.lang.Integer retryMaxTime = 0;
                if (commandParams.containsKey(org.apache.ambari.server.controller.AmbariManagementControllerImpl.CLUSTER_PHASE_PROPERTY) && (commandParams.get(org.apache.ambari.server.controller.AmbariManagementControllerImpl.CLUSTER_PHASE_PROPERTY).equals(org.apache.ambari.server.controller.AmbariManagementControllerImpl.CLUSTER_PHASE_INITIAL_INSTALL) || commandParams.get(org.apache.ambari.server.controller.AmbariManagementControllerImpl.CLUSTER_PHASE_PROPERTY).equals(org.apache.ambari.server.controller.AmbariManagementControllerImpl.CLUSTER_PHASE_INITIAL_START))) {
                    java.lang.String retryEnabledStr = configHelper.getValueFromDesiredConfigurations(cluster, org.apache.ambari.server.state.ConfigHelper.CLUSTER_ENV, org.apache.ambari.server.state.ConfigHelper.CLUSTER_ENV_RETRY_ENABLED);
                    java.lang.String commandsStr = configHelper.getValueFromDesiredConfigurations(cluster, org.apache.ambari.server.state.ConfigHelper.CLUSTER_ENV, org.apache.ambari.server.state.ConfigHelper.CLUSTER_ENV_RETRY_COMMANDS);
                    java.lang.String retryMaxTimeStr = configHelper.getValueFromDesiredConfigurations(cluster, org.apache.ambari.server.state.ConfigHelper.CLUSTER_ENV, org.apache.ambari.server.state.ConfigHelper.CLUSTER_ENV_RETRY_MAX_TIME_IN_SEC);
                    if (org.apache.commons.lang.StringUtils.isNotEmpty(retryEnabledStr)) {
                        retryEnabled = java.lang.Boolean.TRUE.toString().equals(retryEnabledStr);
                    }
                    if (retryEnabled) {
                        retryMaxTime = org.apache.commons.lang.math.NumberUtils.toInt(retryMaxTimeStr, 0);
                        if (retryMaxTime < 0) {
                            retryMaxTime = 0;
                        }
                        if (org.apache.commons.lang.StringUtils.isNotEmpty(commandsStr)) {
                            boolean commandMayBeRetried = false;
                            java.lang.String[] commands = commandsStr.split(",");
                            for (java.lang.String command : commands) {
                                if (roleCommand.toString().equals(command.trim())) {
                                    commandMayBeRetried = true;
                                }
                            }
                            retryEnabled = commandMayBeRetried;
                        }
                    }
                    org.apache.ambari.server.controller.AmbariManagementControllerImpl.LOG.info("Auto retry setting for {}-{} on {} is retryEnabled={} and retryMaxTime={}", serviceName, componentName, scHost.getHostName(), retryEnabled, retryMaxTime);
                }
                commandParams.put(org.apache.ambari.server.agent.ExecutionCommand.KeyNames.MAX_DURATION_OF_RETRIES, java.lang.Integer.toString(retryMaxTime));
                commandParams.put(org.apache.ambari.server.agent.ExecutionCommand.KeyNames.COMMAND_RETRY_ENABLED, java.lang.Boolean.toString(retryEnabled));
                if (script.getTimeout() > 0) {
                    scriptCommandTimeout = java.lang.String.valueOf(script.getTimeout());
                }
            } else {
                java.lang.String message = java.lang.String.format("Component %s of service %s has no " + "command script defined", componentName, serviceName);
                throw new org.apache.ambari.server.AmbariException(message);
            }
        }
        java.lang.String actualTimeout = (!scriptCommandTimeout.equals("")) ? scriptCommandTimeout : agentDefaultCommandTimeout;
        if ((roleCommand.equals(org.apache.ambari.server.RoleCommand.INSTALL) && (!agentDefaultCommandTimeout.equals(""))) && (java.lang.Integer.parseInt(actualTimeout) < java.lang.Integer.parseInt(agentDefaultCommandTimeout))) {
            actualTimeout = agentDefaultCommandTimeout;
        }
        commandParams.put(org.apache.ambari.server.agent.ExecutionCommand.KeyNames.COMMAND_TIMEOUT, actualTimeout);
        java.lang.String customCacheDirectory = componentInfo.getCustomFolder();
        if (customCacheDirectory != null) {
            java.io.File customCache = new java.io.File(configs.getResourceDirPath(), customCacheDirectory);
            if (customCache.exists() && customCache.isDirectory()) {
                commandParams.put(org.apache.ambari.server.agent.ExecutionCommand.KeyNames.CUSTOM_FOLDER, customCacheDirectory);
            }
        }
        java.lang.String clusterName = cluster.getClusterName();
        if (customCommandExecutionHelper.isTopologyRefreshRequired(roleCommand.name(), clusterName, serviceName)) {
            commandParams.put(org.apache.ambari.server.agent.ExecutionCommand.KeyNames.REFRESH_TOPOLOGY, "True");
        }
        org.apache.ambari.server.utils.StageUtils.useAmbariJdkInCommandParams(commandParams, configs);
        java.lang.String repoInfo;
        try {
            repoInfo = repoVersionHelper.getRepoInfoString(cluster, component, host);
        } catch (org.apache.ambari.server.controller.spi.SystemException e) {
            throw new java.lang.RuntimeException(e);
        }
        if (org.apache.ambari.server.controller.AmbariManagementControllerImpl.LOG.isDebugEnabled()) {
            org.apache.ambari.server.controller.AmbariManagementControllerImpl.LOG.debug("Sending repo information to agent, hostname={}, clusterName={}, stackInfo={}, repoInfo={}", scHost.getHostName(), clusterName, stackId.getStackId(), repoInfo);
        }
        java.util.Map<java.lang.String, java.lang.String> hostParams = new java.util.TreeMap<>();
        if (roleCommand.equals(org.apache.ambari.server.RoleCommand.INSTALL)) {
            java.util.List<org.apache.ambari.server.state.ServiceOsSpecific.Package> packages = getPackagesForServiceHost(serviceInfo, hostParams, osFamily);
            java.lang.String packageList = gson.toJson(packages);
            commandParams.put(org.apache.ambari.server.agent.ExecutionCommand.KeyNames.PACKAGE_LIST, packageList);
        }
        java.util.Set<org.apache.ambari.server.state.PropertyInfo> stackProperties = ambariMetaInfo.getStackProperties(stackInfo.getName(), stackInfo.getVersion());
        java.util.Set<java.lang.String> userSet = configHelper.getPropertyValuesWithPropertyType(org.apache.ambari.server.state.PropertyInfo.PropertyType.USER, cluster, clusterDesiredConfigs, servicesMap, stackProperties);
        java.lang.String userList = gson.toJson(userSet);
        hostParams.put(org.apache.ambari.server.agent.ExecutionCommand.KeyNames.USER_LIST, userList);
        java.util.Map<java.lang.String, java.util.Set<java.lang.String>> userGroupsMap = configHelper.createUserGroupsMap(cluster, clusterDesiredConfigs, servicesMap, stackProperties);
        java.lang.String userGroups = gson.toJson(userGroupsMap);
        hostParams.put(org.apache.ambari.server.agent.ExecutionCommand.KeyNames.USER_GROUPS, userGroups);
        java.util.Set<java.lang.String> groupSet = configHelper.getPropertyValuesWithPropertyType(org.apache.ambari.server.state.PropertyInfo.PropertyType.GROUP, cluster, clusterDesiredConfigs, servicesMap, stackProperties);
        java.lang.String groupList = gson.toJson(groupSet);
        hostParams.put(org.apache.ambari.server.agent.ExecutionCommand.KeyNames.GROUP_LIST, groupList);
        java.util.Map<org.apache.ambari.server.state.PropertyInfo, java.lang.String> notManagedHdfsPathMap = configHelper.getPropertiesWithPropertyType(org.apache.ambari.server.state.PropertyInfo.PropertyType.NOT_MANAGED_HDFS_PATH, cluster, clusterDesiredConfigs, servicesMap, stackProperties);
        java.util.Set<java.lang.String> notManagedHdfsPathSet = configHelper.filterInvalidPropertyValues(notManagedHdfsPathMap, org.apache.ambari.server.agent.ExecutionCommand.KeyNames.NOT_MANAGED_HDFS_PATH_LIST);
        java.lang.String notManagedHdfsPathList = gson.toJson(notManagedHdfsPathSet);
        hostParams.put(org.apache.ambari.server.agent.ExecutionCommand.KeyNames.NOT_MANAGED_HDFS_PATH_LIST, notManagedHdfsPathList);
        if (databaseType == org.apache.ambari.server.configuration.Configuration.DatabaseType.ORACLE) {
            hostParams.put(org.apache.ambari.server.agent.ExecutionCommand.KeyNames.DB_DRIVER_FILENAME, configs.getOjdbcJarName());
        } else if (databaseType == org.apache.ambari.server.configuration.Configuration.DatabaseType.MYSQL) {
            hostParams.put(org.apache.ambari.server.agent.ExecutionCommand.KeyNames.DB_DRIVER_FILENAME, configs.getMySQLJarName());
        }
        hostParams.put(org.apache.ambari.server.agent.ExecutionCommand.KeyNames.CLIENTS_TO_UPDATE_CONFIGS, getClientsToUpdateConfigs(componentInfo));
        execCmd.setHostLevelParams(hostParams);
        java.util.Map<java.lang.String, java.lang.String> roleParams = new java.util.TreeMap<>();
        if (isUpgradeSuspended) {
            cluster.addSuspendedUpgradeParameters(commandParams, roleParams);
        }
        execCmd.setRoleParams(roleParams);
        execCmd.setCommandParams(commandParams);
        org.apache.ambari.server.agent.CommandRepository commandRepository;
        try {
            commandRepository = repoVersionHelper.getCommandRepository(cluster, component, host);
        } catch (org.apache.ambari.server.controller.spi.SystemException e) {
            throw new java.lang.RuntimeException(e);
        }
        execCmd.setRepositoryFile(commandRepository);
        execCmdWrapper.setVersions(cluster, null);
        if (useLatestConfigs) {
            execCmd.setUseLatestConfigs(useLatestConfigs);
        }
    }

    protected org.apache.ambari.server.state.ServiceOsSpecific populateServicePackagesInfo(org.apache.ambari.server.state.ServiceInfo serviceInfo, java.util.Map<java.lang.String, java.lang.String> hostParams, java.lang.String osFamily) {
        org.apache.ambari.server.state.ServiceOsSpecific hostOs = new org.apache.ambari.server.state.ServiceOsSpecific(osFamily);
        java.util.List<org.apache.ambari.server.state.ServiceOsSpecific> foundOSSpecifics = getOSSpecificsByFamily(serviceInfo.getOsSpecifics(), osFamily);
        if (!foundOSSpecifics.isEmpty()) {
            for (org.apache.ambari.server.state.ServiceOsSpecific osSpecific : foundOSSpecifics) {
                hostOs.addPackages(osSpecific.getPackages());
            }
            org.apache.ambari.server.state.ServiceOsSpecific.Repo serviceRepo = hostOs.getRepo();
            if (serviceRepo != null) {
                java.lang.String serviceRepoInfo = gson.toJson(serviceRepo);
                hostParams.put(org.apache.ambari.server.agent.ExecutionCommand.KeyNames.SERVICE_REPO_INFO, serviceRepoInfo);
            }
        }
        return hostOs;
    }

    @java.lang.Override
    public java.util.List<org.apache.ambari.server.state.ServiceOsSpecific.Package> getPackagesForServiceHost(org.apache.ambari.server.state.ServiceInfo serviceInfo, java.util.Map<java.lang.String, java.lang.String> hostParams, java.lang.String osFamily) {
        org.apache.ambari.server.state.ServiceOsSpecific anyOs = null;
        if (serviceInfo.getOsSpecifics().containsKey(org.apache.ambari.server.api.services.AmbariMetaInfo.ANY_OS)) {
            anyOs = serviceInfo.getOsSpecifics().get(org.apache.ambari.server.api.services.AmbariMetaInfo.ANY_OS);
        }
        org.apache.ambari.server.state.ServiceOsSpecific hostOs = populateServicePackagesInfo(serviceInfo, hostParams, osFamily);
        java.util.List<org.apache.ambari.server.state.ServiceOsSpecific.Package> packages = new java.util.ArrayList<>();
        if (anyOs != null) {
            packages.addAll(anyOs.getPackages());
        }
        if (hostOs != null) {
            packages.addAll(hostOs.getPackages());
        }
        return packages;
    }

    private java.util.List<org.apache.ambari.server.state.ServiceOsSpecific> getOSSpecificsByFamily(java.util.Map<java.lang.String, org.apache.ambari.server.state.ServiceOsSpecific> osSpecifics, java.lang.String osFamily) {
        java.util.List<org.apache.ambari.server.state.ServiceOsSpecific> foundOSSpecifics = new java.util.ArrayList<>();
        for (java.util.Map.Entry<java.lang.String, org.apache.ambari.server.state.ServiceOsSpecific> osSpecific : osSpecifics.entrySet()) {
            java.lang.String[] osFamilyNames = osSpecific.getKey().split("\\s*,\\s*");
            for (java.lang.String osFamilyName : osFamilyNames) {
                if (this.osFamily.isVersionedOsFamilyExtendedByVersionedFamily(osFamily, osFamilyName)) {
                    foundOSSpecifics.add(osSpecific.getValue());
                    break;
                }
            }
        }
        return foundOSSpecifics;
    }

    private org.apache.ambari.server.controller.ActionExecutionContext getActionExecutionContext(org.apache.ambari.server.controller.ExecuteActionRequest actionRequest) throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.server.controller.internal.RequestOperationLevel operationLevel = actionRequest.getOperationLevel();
        if (actionRequest.isCommand()) {
            org.apache.ambari.server.controller.ActionExecutionContext actionExecutionContext = new org.apache.ambari.server.controller.ActionExecutionContext(actionRequest.getClusterName(), actionRequest.getCommandName(), actionRequest.getResourceFilters(), actionRequest.getParameters());
            actionExecutionContext.setOperationLevel(operationLevel);
            return actionExecutionContext;
        } else {
            org.apache.ambari.server.customactions.ActionDefinition actionDef = ambariMetaInfo.getActionDefinition(actionRequest.getActionName());
            if (actionDef == null) {
                throw new org.apache.ambari.server.AmbariException(("Action " + actionRequest.getActionName()) + " does not exist");
            }
            org.apache.ambari.server.controller.ActionExecutionContext actionExecutionContext = new org.apache.ambari.server.controller.ActionExecutionContext(actionRequest.getClusterName(), actionRequest.getActionName(), actionRequest.getResourceFilters(), actionRequest.getParameters(), actionDef.getTargetType(), actionDef.getDefaultTimeout(), actionDef.getTargetService(), actionDef.getTargetComponent());
            actionExecutionContext.setOperationLevel(operationLevel);
            return actionExecutionContext;
        }
    }

    protected org.apache.ambari.server.controller.internal.RequestStageContainer doStageCreation(org.apache.ambari.server.controller.internal.RequestStageContainer requestStages, org.apache.ambari.server.state.Cluster cluster, java.util.Map<org.apache.ambari.server.state.State, java.util.List<org.apache.ambari.server.state.Service>> changedServices, java.util.Map<org.apache.ambari.server.state.State, java.util.List<org.apache.ambari.server.state.ServiceComponent>> changedComps, java.util.Map<java.lang.String, java.util.Map<org.apache.ambari.server.state.State, java.util.List<org.apache.ambari.server.state.ServiceComponentHost>>> changedScHosts, java.util.Map<java.lang.String, java.lang.String> requestParameters, java.util.Map<java.lang.String, java.lang.String> requestProperties, boolean runSmokeTest, boolean reconfigureClients, boolean useLatestConfigs, boolean useClusterHostInfo) throws org.apache.ambari.server.AmbariException {
        if ((((changedServices == null) || changedServices.isEmpty()) && ((changedComps == null) || changedComps.isEmpty())) && ((changedScHosts == null) || changedScHosts.isEmpty())) {
            org.apache.ambari.server.controller.AmbariManagementControllerImpl.LOG.info("Created 0 stages");
            return requestStages;
        }
        configHelper.checkAllStageConfigsPresentInDesiredConfigs(cluster);
        boolean isUpgradeSuspended = cluster.isUpgradeSuspended();
        org.apache.ambari.server.configuration.Configuration.DatabaseType databaseType = configs.getDatabaseType();
        java.util.Set<java.lang.String> smokeTestServices = getServicesForSmokeTests(cluster, changedServices, changedScHosts, runSmokeTest);
        if (reconfigureClients) {
            addClientSchForReinstall(cluster, changedServices, changedScHosts);
        }
        if ((!changedScHosts.isEmpty()) || (!smokeTestServices.isEmpty())) {
            long nowTimestamp = java.lang.System.currentTimeMillis();
            java.util.Map<java.lang.String, java.util.Set<java.lang.String>> clusterHostInfo = org.apache.ambari.server.utils.StageUtils.getClusterHostInfo(cluster);
            java.lang.String clusterHostInfoJson = org.apache.ambari.server.utils.StageUtils.getGson().toJson(clusterHostInfo);
            org.apache.ambari.server.actionmanager.Stage stage = createNewStage(requestStages.getLastStageId(), cluster, requestStages.getId(), requestProperties.get(org.apache.ambari.server.controller.internal.RequestResourceProvider.CONTEXT), "{}", null);
            boolean skipFailure = false;
            if (requestProperties.containsKey(org.apache.ambari.server.topology.Setting.SETTING_NAME_SKIP_FAILURE) && requestProperties.get(org.apache.ambari.server.topology.Setting.SETTING_NAME_SKIP_FAILURE).equalsIgnoreCase("true")) {
                skipFailure = true;
            }
            stage.setAutoSkipFailureSupported(skipFailure);
            stage.setSkippable(skipFailure);
            java.util.Collection<org.apache.ambari.server.state.ServiceComponentHost> componentsToEnableKerberos = new java.util.ArrayList<>();
            java.util.Set<java.lang.String> hostsToForceKerberosOperations = new java.util.HashSet<>();
            if (kerberosHelper.isClusterKerberosEnabled(cluster)) {
                java.util.Collection<org.apache.ambari.server.state.ServiceComponentHost> componentsToConfigureForKerberos = new java.util.ArrayList<>();
                for (java.util.Map<org.apache.ambari.server.state.State, java.util.List<org.apache.ambari.server.state.ServiceComponentHost>> changedScHostStates : changedScHosts.values()) {
                    if (changedScHostStates != null) {
                        for (java.util.Map.Entry<org.apache.ambari.server.state.State, java.util.List<org.apache.ambari.server.state.ServiceComponentHost>> changedScHostState : changedScHostStates.entrySet()) {
                            org.apache.ambari.server.state.State newState = changedScHostState.getKey();
                            if (newState == org.apache.ambari.server.state.State.INSTALLED) {
                                java.util.List<org.apache.ambari.server.state.ServiceComponentHost> scHosts = changedScHostState.getValue();
                                if (scHosts != null) {
                                    for (org.apache.ambari.server.state.ServiceComponentHost scHost : scHosts) {
                                        org.apache.ambari.server.state.State oldSchState = scHost.getState();
                                        if ((oldSchState == org.apache.ambari.server.state.State.INIT) || (oldSchState == org.apache.ambari.server.state.State.INSTALL_FAILED)) {
                                            if ((!hostComponentAlreadyExists(cluster, scHost)) && (!org.apache.ambari.server.controller.AmbariManagementControllerImpl.CLUSTER_PHASE_INITIAL_INSTALL.equals(requestProperties.get(org.apache.ambari.server.controller.AmbariManagementControllerImpl.CLUSTER_PHASE_PROPERTY)))) {
                                                componentsToConfigureForKerberos.add(scHost);
                                            }
                                            componentsToEnableKerberos.add(scHost);
                                            if (org.apache.ambari.server.state.Service.Type.KERBEROS.name().equalsIgnoreCase(scHost.getServiceName()) && org.apache.ambari.server.Role.KERBEROS_CLIENT.name().equalsIgnoreCase(scHost.getServiceComponentName())) {
                                                hostsToForceKerberosOperations.add(scHost.getHostName());
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                if (!componentsToConfigureForKerberos.isEmpty()) {
                    java.util.Map<java.lang.String, java.util.Collection<java.lang.String>> serviceFilter = new java.util.HashMap<>();
                    for (org.apache.ambari.server.state.ServiceComponentHost scHost : componentsToConfigureForKerberos) {
                        java.lang.String serviceName = scHost.getServiceName();
                        java.util.Collection<java.lang.String> componentFilter = serviceFilter.get(serviceName);
                        if (componentFilter == null) {
                            componentFilter = new java.util.HashSet<>();
                            serviceFilter.put(serviceName, componentFilter);
                        }
                        componentFilter.add(scHost.getServiceComponentName());
                    }
                    try {
                        kerberosHelper.configureServices(cluster, serviceFilter);
                    } catch (org.apache.ambari.server.serveraction.kerberos.KerberosInvalidConfigurationException e) {
                        throw new org.apache.ambari.server.AmbariException(e.getMessage(), e);
                    }
                }
            }
            for (java.lang.String compName : changedScHosts.keySet()) {
                for (org.apache.ambari.server.state.State newState : changedScHosts.get(compName).keySet()) {
                    for (org.apache.ambari.server.state.ServiceComponentHost scHost : changedScHosts.get(compName).get(newState)) {
                        org.apache.ambari.server.state.Service service = cluster.getService(scHost.getServiceName());
                        org.apache.ambari.server.state.ServiceComponent serviceComponent = service.getServiceComponent(compName);
                        if (org.apache.commons.lang.StringUtils.isBlank(stage.getHostParamsStage())) {
                            org.apache.ambari.server.orm.entities.RepositoryVersionEntity repositoryVersion = serviceComponent.getDesiredRepositoryVersion();
                            stage.setHostParamsStage(org.apache.ambari.server.utils.StageUtils.getGson().toJson(customCommandExecutionHelper.createDefaultHostParams(cluster, repositoryVersion.getStackId())));
                        }
                        if (scHost.getHostState().equals(org.apache.ambari.server.state.HostState.HEARTBEAT_LOST)) {
                            org.apache.ambari.server.controller.AmbariManagementControllerImpl.LOG.info(((((((((((((("Command is not created for servicecomponenthost " + ", clusterName=") + cluster.getClusterName()) + ", clusterId=") + cluster.getClusterId()) + ", serviceName=") + scHost.getServiceName()) + ", componentName=") + scHost.getServiceComponentName()) + ", hostname=") + scHost.getHostName()) + ", hostState=") + scHost.getHostState()) + ", targetNewState=") + newState);
                            continue;
                        }
                        org.apache.ambari.server.RoleCommand roleCommand;
                        org.apache.ambari.server.state.State oldSchState = scHost.getState();
                        org.apache.ambari.server.state.ServiceComponentHostEvent event;
                        switch (newState) {
                            case INSTALLED :
                                if ((((((oldSchState == org.apache.ambari.server.state.State.INIT) || (oldSchState == org.apache.ambari.server.state.State.UNINSTALLED)) || (oldSchState == org.apache.ambari.server.state.State.INSTALLED)) || (oldSchState == org.apache.ambari.server.state.State.INSTALLING)) || (oldSchState == org.apache.ambari.server.state.State.UNKNOWN)) || (oldSchState == org.apache.ambari.server.state.State.INSTALL_FAILED)) {
                                    roleCommand = org.apache.ambari.server.RoleCommand.INSTALL;
                                    if (scHost.isClientComponent() && (oldSchState == org.apache.ambari.server.state.State.INSTALLED)) {
                                        event = new org.apache.ambari.server.state.svccomphost.ServiceComponentHostOpInProgressEvent(scHost.getServiceComponentName(), scHost.getHostName(), nowTimestamp);
                                    } else {
                                        event = new org.apache.ambari.server.state.svccomphost.ServiceComponentHostInstallEvent(scHost.getServiceComponentName(), scHost.getHostName(), nowTimestamp, serviceComponent.getDesiredStackId().getStackId());
                                    }
                                } else if ((oldSchState == org.apache.ambari.server.state.State.STARTED) || (oldSchState == org.apache.ambari.server.state.State.STOPPING)) {
                                    roleCommand = org.apache.ambari.server.RoleCommand.STOP;
                                    event = new org.apache.ambari.server.state.svccomphost.ServiceComponentHostStopEvent(scHost.getServiceComponentName(), scHost.getHostName(), nowTimestamp);
                                } else if (oldSchState == org.apache.ambari.server.state.State.UPGRADING) {
                                    roleCommand = org.apache.ambari.server.RoleCommand.UPGRADE;
                                    event = new org.apache.ambari.server.state.svccomphost.ServiceComponentHostUpgradeEvent(scHost.getServiceComponentName(), scHost.getHostName(), nowTimestamp, serviceComponent.getDesiredStackId().getStackId());
                                } else {
                                    throw new org.apache.ambari.server.AmbariException(((((((((((((("Invalid transition for" + (" servicecomponenthost" + ", clusterName=")) + cluster.getClusterName()) + ", clusterId=") + cluster.getClusterId()) + ", serviceName=") + scHost.getServiceName()) + ", componentName=") + scHost.getServiceComponentName()) + ", hostname=") + scHost.getHostName()) + ", currentState=") + oldSchState) + ", newDesiredState=") + newState);
                                }
                                break;
                            case STARTED :
                                org.apache.ambari.server.state.StackId stackId = serviceComponent.getDesiredStackId();
                                org.apache.ambari.server.state.ComponentInfo compInfo = ambariMetaInfo.getComponent(stackId.getStackName(), stackId.getStackVersion(), scHost.getServiceName(), scHost.getServiceComponentName());
                                if (((oldSchState == org.apache.ambari.server.state.State.INSTALLED) || (oldSchState == org.apache.ambari.server.state.State.STARTING)) || true) {
                                    roleCommand = org.apache.ambari.server.RoleCommand.START;
                                    event = new org.apache.ambari.server.state.svccomphost.ServiceComponentHostStartEvent(scHost.getServiceComponentName(), scHost.getHostName(), nowTimestamp);
                                } else {
                                    java.lang.String error = ((((((((((((("Invalid transition for" + (" servicecomponenthost" + ", clusterName=")) + cluster.getClusterName()) + ", clusterId=") + cluster.getClusterId()) + ", serviceName=") + scHost.getServiceName()) + ", componentName=") + scHost.getServiceComponentName()) + ", hostname=") + scHost.getHostName()) + ", currentState=") + oldSchState) + ", newDesiredState=") + newState;
                                    if (compInfo.isMaster()) {
                                        throw new org.apache.ambari.server.AmbariException(error);
                                    } else {
                                        org.apache.ambari.server.controller.AmbariManagementControllerImpl.LOG.info("Ignoring: " + error);
                                        continue;
                                    }
                                }
                                break;
                            case UNINSTALLED :
                                if ((oldSchState == org.apache.ambari.server.state.State.INSTALLED) || (oldSchState == org.apache.ambari.server.state.State.UNINSTALLING)) {
                                    roleCommand = org.apache.ambari.server.RoleCommand.UNINSTALL;
                                    event = new org.apache.ambari.server.state.svccomphost.ServiceComponentHostStartEvent(scHost.getServiceComponentName(), scHost.getHostName(), nowTimestamp);
                                } else {
                                    throw new org.apache.ambari.server.AmbariException(((((((((((((("Invalid transition for" + (" servicecomponenthost" + ", clusterName=")) + cluster.getClusterName()) + ", clusterId=") + cluster.getClusterId()) + ", serviceName=") + scHost.getServiceName()) + ", componentName=") + scHost.getServiceComponentName()) + ", hostname=") + scHost.getHostName()) + ", currentState=") + oldSchState) + ", newDesiredState=") + newState);
                                }
                                break;
                            case INIT :
                                if (((oldSchState == org.apache.ambari.server.state.State.INSTALLED) || (oldSchState == org.apache.ambari.server.state.State.INSTALL_FAILED)) || (oldSchState == org.apache.ambari.server.state.State.INIT)) {
                                    scHost.setState(org.apache.ambari.server.state.State.INIT);
                                    continue;
                                } else {
                                    throw new org.apache.ambari.server.AmbariException(((((((((((((("Unsupported transition to INIT for" + (" servicecomponenthost" + ", clusterName=")) + cluster.getClusterName()) + ", clusterId=") + cluster.getClusterId()) + ", serviceName=") + scHost.getServiceName()) + ", componentName=") + scHost.getServiceComponentName()) + ", hostname=") + scHost.getHostName()) + ", currentState=") + oldSchState) + ", newDesiredState=") + newState);
                                }
                            default :
                                throw new org.apache.ambari.server.AmbariException(("Unsupported state change operation" + ", newState=") + newState);
                        }
                        if (org.apache.ambari.server.controller.AmbariManagementControllerImpl.LOG.isDebugEnabled()) {
                            org.apache.ambari.server.controller.AmbariManagementControllerImpl.LOG.debug("Create a new host action, requestId={}, componentName={}, hostname={}, roleCommand={}", requestStages.getId(), scHost.getServiceComponentName(), scHost.getHostName(), roleCommand.name());
                        }
                        java.lang.String keyName = scHost.getServiceComponentName().toLowerCase();
                        if (requestProperties.containsKey(keyName)) {
                            if (oldSchState == newState) {
                                switch (oldSchState) {
                                    case INSTALLED :
                                        roleCommand = org.apache.ambari.server.RoleCommand.STOP;
                                        event = new org.apache.ambari.server.state.svccomphost.ServiceComponentHostStopEvent(scHost.getServiceComponentName(), scHost.getHostName(), nowTimestamp);
                                        break;
                                    case STARTED :
                                        roleCommand = org.apache.ambari.server.RoleCommand.START;
                                        event = new org.apache.ambari.server.state.svccomphost.ServiceComponentHostStartEvent(scHost.getServiceComponentName(), scHost.getHostName(), nowTimestamp);
                                        break;
                                    default :
                                        break;
                                }
                            }
                            if (null == requestParameters) {
                                requestParameters = new java.util.HashMap<>();
                            }
                            requestParameters.put(keyName, requestProperties.get(keyName));
                        }
                        if (requestProperties.containsKey(org.apache.ambari.server.controller.AmbariManagementControllerImpl.CLUSTER_PHASE_PROPERTY)) {
                            if (null == requestParameters) {
                                requestParameters = new java.util.HashMap<>();
                            }
                            requestParameters.put(org.apache.ambari.server.controller.AmbariManagementControllerImpl.CLUSTER_PHASE_PROPERTY, requestProperties.get(org.apache.ambari.server.controller.AmbariManagementControllerImpl.CLUSTER_PHASE_PROPERTY));
                        }
                        java.util.Map<java.lang.String, org.apache.ambari.server.state.DesiredConfig> clusterDesiredConfigs = cluster.getDesiredConfigs();
                        if ((newState == org.apache.ambari.server.state.State.INSTALLED) && skipInstallTaskForComponent(requestProperties, cluster, scHost)) {
                            org.apache.ambari.server.controller.AmbariManagementControllerImpl.LOG.info("Skipping create of INSTALL task for {} on {}.", scHost.getServiceComponentName(), scHost.getHostName());
                            scHost.setState(org.apache.ambari.server.state.State.INSTALLING);
                            long now = java.lang.System.currentTimeMillis();
                            try {
                                scHost.handleEvent(new org.apache.ambari.server.state.svccomphost.ServiceComponentHostOpSucceededEvent(scHost.getServiceComponentName(), scHost.getHostName(), now));
                            } catch (org.apache.ambari.server.state.fsm.InvalidStateTransitionException e) {
                                org.apache.ambari.server.controller.AmbariManagementControllerImpl.LOG.error("Error transitioning ServiceComponentHost state to INSTALLED", e);
                            }
                        } else {
                            org.apache.ambari.server.orm.entities.RepositoryVersionEntity repoVersion = serviceComponent.getDesiredRepositoryVersion();
                            createHostAction(cluster, stage, scHost, roleCommand, requestParameters, event, skipFailure, repoVersion, isUpgradeSuspended, databaseType, clusterDesiredConfigs, useLatestConfigs);
                        }
                    }
                }
            }
            for (java.lang.String serviceName : smokeTestServices) {
                org.apache.ambari.server.state.Service s = cluster.getService(serviceName);
                org.apache.ambari.server.state.ServiceComponent component = getClientComponentForRunningAction(cluster, s);
                java.lang.String componentName = (component != null) ? component.getName() : null;
                java.lang.String clientHost = getClientHostForRunningAction(cluster, s, component);
                java.lang.String smokeTestRole = actionMetadata.getServiceCheckAction(serviceName);
                if ((clientHost == null) || (smokeTestRole == null)) {
                    org.apache.ambari.server.controller.AmbariManagementControllerImpl.LOG.info(((((((("Nothing to do for service check as could not find role or" + (" or host to run check on" + ", clusterName=")) + cluster.getClusterName()) + ", serviceName=") + serviceName) + ", clientHost=") + clientHost) + ", serviceCheckRole=") + smokeTestRole);
                    continue;
                }
                if (org.apache.commons.lang.StringUtils.isBlank(stage.getHostParamsStage())) {
                    org.apache.ambari.server.orm.entities.RepositoryVersionEntity repositoryVersion = component.getDesiredRepositoryVersion();
                    stage.setHostParamsStage(org.apache.ambari.server.utils.StageUtils.getGson().toJson(customCommandExecutionHelper.createDefaultHostParams(cluster, repositoryVersion.getStackId())));
                }
                customCommandExecutionHelper.addServiceCheckAction(stage, clientHost, smokeTestRole, nowTimestamp, serviceName, componentName, null, false, false, useLatestConfigs);
            }
            org.apache.ambari.server.metadata.RoleCommandOrder rco = getRoleCommandOrder(cluster);
            org.apache.ambari.server.stageplanner.RoleGraph rg = roleGraphFactory.createNew(rco);
            if ((org.apache.ambari.server.actionmanager.CommandExecutionType.DEPENDENCY_ORDERED == configs.getStageExecutionType()) && org.apache.ambari.server.controller.AmbariManagementControllerImpl.CLUSTER_PHASE_INITIAL_START.equals(requestProperties.get(org.apache.ambari.server.controller.AmbariManagementControllerImpl.CLUSTER_PHASE_PROPERTY))) {
                org.apache.ambari.server.controller.AmbariManagementControllerImpl.LOG.info("Set DEPENDENCY_ORDERED CommandExecutionType on stage: {}", stage.getRequestContext());
                rg.setCommandExecutionType(org.apache.ambari.server.actionmanager.CommandExecutionType.DEPENDENCY_ORDERED);
            }
            rg.build(stage);
            if (useClusterHostInfo) {
                requestStages.setClusterHostInfo(clusterHostInfoJson);
            }
            requestStages.addStages(rg.getStages());
            if (!componentsToEnableKerberos.isEmpty()) {
                java.util.Map<java.lang.String, java.util.Collection<java.lang.String>> serviceFilter = new java.util.HashMap<>();
                java.util.Set<java.lang.String> hostFilter = new java.util.HashSet<>();
                for (org.apache.ambari.server.state.ServiceComponentHost scHost : componentsToEnableKerberos) {
                    java.lang.String serviceName = scHost.getServiceName();
                    java.util.Collection<java.lang.String> componentFilter = serviceFilter.get(serviceName);
                    if (componentFilter == null) {
                        componentFilter = new java.util.HashSet<>();
                        serviceFilter.put(serviceName, componentFilter);
                    }
                    componentFilter.add(scHost.getServiceComponentName());
                    hostFilter.add(scHost.getHostName());
                }
                try {
                    kerberosHelper.ensureIdentities(cluster, serviceFilter, hostFilter, null, hostsToForceKerberosOperations, requestStages, kerberosHelper.getManageIdentitiesDirective(requestProperties));
                } catch (org.apache.ambari.server.serveraction.kerberos.KerberosOperationException e) {
                    throw new java.lang.IllegalArgumentException(e.getMessage(), e);
                }
            }
            java.util.List<org.apache.ambari.server.actionmanager.Stage> stages = requestStages.getStages();
            org.apache.ambari.server.controller.AmbariManagementControllerImpl.LOG.debug("Created {} stages", stages != null ? stages.size() : 0);
        } else {
            org.apache.ambari.server.controller.AmbariManagementControllerImpl.LOG.debug("Created 0 stages");
        }
        return requestStages;
    }

    private boolean hostComponentAlreadyExists(org.apache.ambari.server.state.Cluster cluster, org.apache.ambari.server.state.ServiceComponentHost sch) throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.server.state.Service service = cluster.getService(sch.getServiceName());
        if (service != null) {
            org.apache.ambari.server.state.ServiceComponent serviceComponent = service.getServiceComponent(sch.getServiceComponentName());
            if (serviceComponent != null) {
                java.util.Map<java.lang.String, org.apache.ambari.server.state.ServiceComponentHost> serviceComponentHostMap = serviceComponent.getServiceComponentHosts();
                for (org.apache.ambari.server.state.ServiceComponentHost serviceComponentHost : serviceComponentHostMap.values()) {
                    if ((serviceComponentHost.getState() == org.apache.ambari.server.state.State.INSTALLED) || (serviceComponentHost.getState() == org.apache.ambari.server.state.State.STARTED)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private boolean skipInstallTaskForComponent(java.util.Map<java.lang.String, java.lang.String> requestProperties, org.apache.ambari.server.state.Cluster cluster, org.apache.ambari.server.state.ServiceComponentHost sch) throws org.apache.ambari.server.AmbariException {
        boolean isClientComponent = false;
        org.apache.ambari.server.state.Service service = cluster.getService(sch.getServiceName());
        java.lang.String componentName = sch.getServiceComponentName();
        if (service != null) {
            org.apache.ambari.server.state.ServiceComponent serviceComponent = service.getServiceComponent(componentName);
            if (serviceComponent != null) {
                isClientComponent = serviceComponent.isClientComponent();
            }
        }
        return org.apache.ambari.server.controller.internal.HostComponentResourceProvider.shouldSkipInstallTaskForComponent(componentName, isClientComponent, requestProperties);
    }

    @java.lang.Override
    public org.apache.ambari.server.agent.ExecutionCommand getExecutionCommand(org.apache.ambari.server.state.Cluster cluster, org.apache.ambari.server.state.ServiceComponentHost scHost, org.apache.ambari.server.RoleCommand roleCommand) throws org.apache.ambari.server.AmbariException {
        java.util.Map<java.lang.String, java.lang.String> hostParamsCmd = customCommandExecutionHelper.createDefaultHostParams(cluster, scHost.getServiceComponent().getDesiredStackId());
        org.apache.ambari.server.actionmanager.Stage stage = createNewStage(0, cluster, 1, "", "{}", "");
        org.apache.ambari.server.orm.entities.RepositoryVersionEntity repoVersion = null;
        if (null != scHost.getServiceComponent().getDesiredRepositoryVersion()) {
            repoVersion = scHost.getServiceComponent().getDesiredRepositoryVersion();
        } else {
            org.apache.ambari.server.state.Service service = cluster.getService(scHost.getServiceName());
            repoVersion = service.getDesiredRepositoryVersion();
        }
        boolean isUpgradeSuspended = cluster.isUpgradeSuspended();
        org.apache.ambari.server.configuration.Configuration.DatabaseType databaseType = configs.getDatabaseType();
        java.util.Map<java.lang.String, org.apache.ambari.server.state.DesiredConfig> clusterDesiredConfigs = cluster.getDesiredConfigs();
        createHostAction(cluster, stage, scHost, roleCommand, null, null, false, repoVersion, isUpgradeSuspended, databaseType, clusterDesiredConfigs, false);
        org.apache.ambari.server.agent.ExecutionCommand ec = stage.getExecutionCommands().get(scHost.getHostName()).get(0).getExecutionCommand();
        hostParamsCmd.putAll(ec.getHostLevelParams());
        ec.getHostLevelParams().putAll(hostParamsCmd);
        for (org.apache.ambari.server.state.ServiceComponentHost sch : cluster.getServiceComponentHosts(scHost.getHostName())) {
            ec.getLocalComponents().add(sch.getServiceComponentName());
        }
        return ec;
    }

    @java.lang.Override
    public java.util.Set<org.apache.ambari.server.controller.StackConfigurationDependencyResponse> getStackConfigurationDependencies(java.util.Set<org.apache.ambari.server.controller.StackConfigurationDependencyRequest> requests) throws org.apache.ambari.server.AmbariException {
        java.util.Set<org.apache.ambari.server.controller.StackConfigurationDependencyResponse> response = new java.util.HashSet<>();
        if (requests != null) {
            for (org.apache.ambari.server.controller.StackConfigurationDependencyRequest request : requests) {
                java.lang.String stackName = request.getStackName();
                java.lang.String stackVersion = request.getStackVersion();
                java.lang.String serviceName = request.getServiceName();
                java.lang.String propertyName = request.getPropertyName();
                java.util.Set<org.apache.ambari.server.controller.StackConfigurationDependencyResponse> stackConfigurations = getStackConfigurationDependencies(request);
                for (org.apache.ambari.server.controller.StackConfigurationDependencyResponse dependencyResponse : stackConfigurations) {
                    dependencyResponse.setStackName(stackName);
                    dependencyResponse.setStackVersion(stackVersion);
                    dependencyResponse.setServiceName(serviceName);
                    dependencyResponse.setPropertyName(propertyName);
                }
                response.addAll(stackConfigurations);
            }
        }
        return response;
    }

    private java.util.Set<org.apache.ambari.server.controller.StackConfigurationDependencyResponse> getStackConfigurationDependencies(org.apache.ambari.server.controller.StackConfigurationDependencyRequest request) throws org.apache.ambari.server.AmbariException {
        java.util.Set<org.apache.ambari.server.controller.StackConfigurationDependencyResponse> response = new java.util.HashSet<>();
        java.lang.String stackName = request.getStackName();
        java.lang.String stackVersion = request.getStackVersion();
        java.lang.String serviceName = request.getServiceName();
        java.lang.String propertyName = request.getPropertyName();
        java.lang.String dependencyName = request.getDependencyName();
        java.util.Set<org.apache.ambari.server.state.PropertyInfo> properties = ambariMetaInfo.getPropertiesByName(stackName, stackVersion, serviceName, propertyName);
        for (org.apache.ambari.server.state.PropertyInfo property : properties) {
            for (org.apache.ambari.server.state.PropertyDependencyInfo dependency : property.getDependedByProperties()) {
                if ((dependencyName == null) || dependency.getName().equals(dependencyName)) {
                    response.add(dependency.convertToResponse());
                }
            }
        }
        return response;
    }

    @com.google.inject.persist.Transactional
    void updateServiceStates(org.apache.ambari.server.state.Cluster cluster, java.util.Map<org.apache.ambari.server.state.State, java.util.List<org.apache.ambari.server.state.Service>> changedServices, java.util.Map<org.apache.ambari.server.state.State, java.util.List<org.apache.ambari.server.state.ServiceComponent>> changedComps, java.util.Map<java.lang.String, java.util.Map<org.apache.ambari.server.state.State, java.util.List<org.apache.ambari.server.state.ServiceComponentHost>>> changedScHosts, java.util.Collection<org.apache.ambari.server.state.ServiceComponentHost> ignoredScHosts) {
        if (changedServices != null) {
            for (java.util.Map.Entry<org.apache.ambari.server.state.State, java.util.List<org.apache.ambari.server.state.Service>> entry : changedServices.entrySet()) {
                org.apache.ambari.server.state.State newState = entry.getKey();
                for (org.apache.ambari.server.state.Service s : entry.getValue()) {
                    if (s.isClientOnlyService() && (newState == org.apache.ambari.server.state.State.STARTED)) {
                        continue;
                    }
                    s.setDesiredState(newState);
                }
            }
        }
        if (changedComps != null) {
            for (java.util.Map.Entry<org.apache.ambari.server.state.State, java.util.List<org.apache.ambari.server.state.ServiceComponent>> entry : changedComps.entrySet()) {
                org.apache.ambari.server.state.State newState = entry.getKey();
                for (org.apache.ambari.server.state.ServiceComponent sc : entry.getValue()) {
                    sc.setDesiredState(newState);
                }
            }
        }
        java.util.Map<java.lang.String, java.lang.String> serviceMasterForDecommissionMap = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.util.Set<java.lang.String>> masterSlaveHostsMap = new java.util.HashMap<>();
        for (java.util.Map<org.apache.ambari.server.state.State, java.util.List<org.apache.ambari.server.state.ServiceComponentHost>> stateScHostMap : changedScHosts.values()) {
            for (java.util.Map.Entry<org.apache.ambari.server.state.State, java.util.List<org.apache.ambari.server.state.ServiceComponentHost>> entry : stateScHostMap.entrySet()) {
                org.apache.ambari.server.state.State newState = entry.getKey();
                for (org.apache.ambari.server.state.ServiceComponentHost sch : entry.getValue()) {
                    java.lang.String componentName = sch.getServiceComponentName();
                    if ((org.apache.ambari.server.controller.AmbariCustomCommandExecutionHelper.masterToSlaveMappingForDecom.containsValue(componentName) && (sch.getState() == org.apache.ambari.server.state.State.INIT)) && (newState == org.apache.ambari.server.state.State.INSTALLED)) {
                        java.lang.String serviceName = sch.getServiceName();
                        java.lang.String masterComponentName = null;
                        for (java.util.Map.Entry<java.lang.String, java.lang.String> entrySet : org.apache.ambari.server.controller.AmbariCustomCommandExecutionHelper.masterToSlaveMappingForDecom.entrySet()) {
                            if (entrySet.getValue().equals(componentName)) {
                                masterComponentName = entrySet.getKey();
                            }
                        }
                        try {
                            if (isServiceComponentStartedOnAnyHost(cluster, serviceName, masterComponentName)) {
                                serviceMasterForDecommissionMap.put(serviceName, masterComponentName);
                                masterSlaveHostsMap.putIfAbsent(masterComponentName, new java.util.HashSet<>());
                                masterSlaveHostsMap.get(masterComponentName).add(sch.getHostName());
                            } else {
                                org.apache.ambari.server.controller.AmbariManagementControllerImpl.LOG.info(java.lang.String.format("Not adding %s service from include/exclude files refresh map because it's master is not started", serviceName));
                            }
                        } catch (org.apache.ambari.server.AmbariException e) {
                            org.apache.ambari.server.controller.AmbariManagementControllerImpl.LOG.error("Exception during INIT masters cleanup : ", e);
                        }
                    }
                    sch.setDesiredState(newState);
                }
            }
        }
        try {
            createAndExecuteRefreshIncludeExcludeFilesActionForMasters(serviceMasterForDecommissionMap, masterSlaveHostsMap, cluster.getClusterName(), false);
        } catch (org.apache.ambari.server.AmbariException e) {
            org.apache.ambari.server.controller.AmbariManagementControllerImpl.LOG.error("Exception during refresh include exclude files action : ", e);
        }
        if (ignoredScHosts != null) {
            for (org.apache.ambari.server.state.ServiceComponentHost scHost : ignoredScHosts) {
                scHost.setDesiredState(scHost.getState());
            }
        }
    }

    private boolean isServiceComponentStartedOnAnyHost(org.apache.ambari.server.state.Cluster cluster, java.lang.String serviceName, java.lang.String masterComponentName) throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.server.state.Service service = cluster.getService(serviceName);
        org.apache.ambari.server.state.ServiceComponent serviceComponent = service.getServiceComponent(masterComponentName);
        java.util.Map<java.lang.String, org.apache.ambari.server.state.ServiceComponentHost> schMap = serviceComponent.getServiceComponentHosts();
        for (org.apache.ambari.server.state.ServiceComponentHost sch : schMap.values()) {
            if (sch.getState() == org.apache.ambari.server.state.State.STARTED) {
                return true;
            }
        }
        return false;
    }

    @java.lang.Override
    public org.apache.ambari.server.controller.RequestStatusResponse createAndPersistStages(org.apache.ambari.server.state.Cluster cluster, java.util.Map<java.lang.String, java.lang.String> requestProperties, java.util.Map<java.lang.String, java.lang.String> requestParameters, java.util.Map<org.apache.ambari.server.state.State, java.util.List<org.apache.ambari.server.state.Service>> changedServices, java.util.Map<org.apache.ambari.server.state.State, java.util.List<org.apache.ambari.server.state.ServiceComponent>> changedComponents, java.util.Map<java.lang.String, java.util.Map<org.apache.ambari.server.state.State, java.util.List<org.apache.ambari.server.state.ServiceComponentHost>>> changedHosts, java.util.Collection<org.apache.ambari.server.state.ServiceComponentHost> ignoredHosts, boolean runSmokeTest, boolean reconfigureClients) throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.server.controller.internal.RequestStageContainer request = addStages(null, cluster, requestProperties, requestParameters, changedServices, changedComponents, changedHosts, ignoredHosts, runSmokeTest, reconfigureClients, false);
        request.persist();
        return request.getRequestStatusResponse();
    }

    @java.lang.Override
    public org.apache.ambari.server.controller.internal.RequestStageContainer addStages(org.apache.ambari.server.controller.internal.RequestStageContainer requestStages, org.apache.ambari.server.state.Cluster cluster, java.util.Map<java.lang.String, java.lang.String> requestProperties, java.util.Map<java.lang.String, java.lang.String> requestParameters, java.util.Map<org.apache.ambari.server.state.State, java.util.List<org.apache.ambari.server.state.Service>> changedServices, java.util.Map<org.apache.ambari.server.state.State, java.util.List<org.apache.ambari.server.state.ServiceComponent>> changedComponents, java.util.Map<java.lang.String, java.util.Map<org.apache.ambari.server.state.State, java.util.List<org.apache.ambari.server.state.ServiceComponentHost>>> changedHosts, java.util.Collection<org.apache.ambari.server.state.ServiceComponentHost> ignoredHosts, boolean runSmokeTest, boolean reconfigureClients, boolean useGeneratedConfigs) throws org.apache.ambari.server.AmbariException {
        return addStages(requestStages, cluster, requestProperties, requestParameters, changedServices, changedComponents, changedHosts, ignoredHosts, runSmokeTest, reconfigureClients, useGeneratedConfigs, false);
    }

    @java.lang.Override
    public org.apache.ambari.server.controller.internal.RequestStageContainer addStages(org.apache.ambari.server.controller.internal.RequestStageContainer requestStages, org.apache.ambari.server.state.Cluster cluster, java.util.Map<java.lang.String, java.lang.String> requestProperties, java.util.Map<java.lang.String, java.lang.String> requestParameters, java.util.Map<org.apache.ambari.server.state.State, java.util.List<org.apache.ambari.server.state.Service>> changedServices, java.util.Map<org.apache.ambari.server.state.State, java.util.List<org.apache.ambari.server.state.ServiceComponent>> changedComponents, java.util.Map<java.lang.String, java.util.Map<org.apache.ambari.server.state.State, java.util.List<org.apache.ambari.server.state.ServiceComponentHost>>> changedHosts, java.util.Collection<org.apache.ambari.server.state.ServiceComponentHost> ignoredHosts, boolean runSmokeTest, boolean reconfigureClients, boolean useGeneratedConfigs, boolean useClusterHostInfo) throws org.apache.ambari.server.AmbariException {
        if (requestStages == null) {
            requestStages = new org.apache.ambari.server.controller.internal.RequestStageContainer(actionManager.getNextRequestId(), null, requestFactory, actionManager);
        }
        requestStages = doStageCreation(requestStages, cluster, changedServices, changedComponents, changedHosts, requestParameters, requestProperties, runSmokeTest, reconfigureClients, useGeneratedConfigs, useClusterHostInfo);
        updateServiceStates(cluster, changedServices, changedComponents, changedHosts, ignoredHosts);
        return requestStages;
    }

    public void validateServiceComponentHostRequest(org.apache.ambari.server.controller.ServiceComponentHostRequest request) {
        if ((((((request.getClusterName() == null) || request.getClusterName().isEmpty()) || (request.getComponentName() == null)) || request.getComponentName().isEmpty()) || (request.getHostname() == null)) || request.getHostname().isEmpty()) {
            throw new java.lang.IllegalArgumentException("Invalid arguments" + (", cluster name, component name and host name should be" + " provided"));
        }
        if (request.getAdminState() != null) {
            throw new java.lang.IllegalArgumentException("Property adminState cannot be modified through update. Use service " + "specific DECOMMISSION action to decommision/recommission components.");
        }
    }

    private void checkIfHostComponentsInDeleteFriendlyState(org.apache.ambari.server.controller.ServiceComponentHostRequest request, org.apache.ambari.server.state.Cluster cluster) throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.server.state.Service service = cluster.getService(request.getServiceName());
        org.apache.ambari.server.state.ServiceComponent component = service.getServiceComponent(request.getComponentName());
        org.apache.ambari.server.state.ServiceComponentHost componentHost = component.getServiceComponentHost(request.getHostname());
        if (!componentHost.canBeRemoved()) {
            throw new org.apache.ambari.server.AmbariException(((((((((((("Current host component state prohibiting component removal." + ", clusterName=") + request.getClusterName()) + ", serviceName=") + request.getServiceName()) + ", componentName=") + request.getComponentName()) + ", hostname=") + request.getHostname()) + ", request=") + request) + ", state=") + componentHost.getState());
        }
    }

    @java.lang.Override
    public java.lang.String findServiceName(org.apache.ambari.server.state.Cluster cluster, java.lang.String componentName) throws org.apache.ambari.server.AmbariException {
        return cluster.getServiceByComponentName(componentName).getName();
    }

    @java.lang.Override
    public synchronized void deleteCluster(org.apache.ambari.server.controller.ClusterRequest request) throws org.apache.ambari.server.AmbariException {
        if ((request.getClusterName() == null) || request.getClusterName().isEmpty()) {
            throw new org.apache.ambari.server.AmbariException("Invalid arguments");
        }
        org.apache.ambari.server.controller.AmbariManagementControllerImpl.LOG.info("Received a delete cluster request, clusterName = " + request.getClusterName());
        if (request.getHostNames() != null) {
        } else {
            clusters.deleteCluster(request.getClusterName());
        }
    }

    @java.lang.Override
    public org.apache.ambari.server.controller.internal.DeleteStatusMetaData deleteHostComponents(java.util.Set<org.apache.ambari.server.controller.ServiceComponentHostRequest> requests) throws org.apache.ambari.server.AmbariException, org.apache.ambari.server.security.authorization.AuthorizationException {
        java.util.Set<org.apache.ambari.server.controller.ServiceComponentHostRequest> expanded = new java.util.HashSet<>();
        for (org.apache.ambari.server.controller.ServiceComponentHostRequest request : requests) {
            if (null == request.getComponentName()) {
                if ((((null == request.getClusterName()) || request.getClusterName().isEmpty()) || (null == request.getHostname())) || request.getHostname().isEmpty()) {
                    throw new java.lang.IllegalArgumentException("Cluster name and hostname must be specified.");
                }
                org.apache.ambari.server.state.Cluster cluster = clusters.getCluster(request.getClusterName());
                if (!org.apache.ambari.server.security.authorization.AuthorizationHelper.isAuthorized(org.apache.ambari.server.security.authorization.ResourceType.CLUSTER, cluster.getResourceId(), java.util.EnumSet.of(org.apache.ambari.server.security.authorization.RoleAuthorization.SERVICE_ADD_DELETE_SERVICES, org.apache.ambari.server.security.authorization.RoleAuthorization.HOST_ADD_DELETE_COMPONENTS))) {
                    throw new org.apache.ambari.server.security.authorization.AuthorizationException("The authenticated user is not authorized to delete service components from hosts");
                }
                for (org.apache.ambari.server.state.ServiceComponentHost sch : cluster.getServiceComponentHosts(request.getHostname())) {
                    org.apache.ambari.server.controller.ServiceComponentHostRequest schr = new org.apache.ambari.server.controller.ServiceComponentHostRequest(request.getClusterName(), sch.getServiceName(), sch.getServiceComponentName(), sch.getHostName(), null);
                    expanded.add(schr);
                }
            } else {
                expanded.add(request);
            }
        }
        java.util.Map<org.apache.ambari.server.state.ServiceComponent, java.util.Set<org.apache.ambari.server.state.ServiceComponentHost>> safeToRemoveSCHs = new java.util.HashMap<>();
        org.apache.ambari.server.controller.internal.DeleteHostComponentStatusMetaData deleteMetaData = new org.apache.ambari.server.controller.internal.DeleteHostComponentStatusMetaData();
        for (org.apache.ambari.server.controller.ServiceComponentHostRequest request : expanded) {
            validateServiceComponentHostRequest(request);
            org.apache.ambari.server.state.Cluster cluster = clusters.getCluster(request.getClusterName());
            if (org.apache.commons.lang.StringUtils.isEmpty(request.getServiceName())) {
                request.setServiceName(findServiceName(cluster, request.getComponentName()));
            }
            org.apache.ambari.server.controller.AmbariManagementControllerImpl.LOG.info(((((((((("Received a hostComponent DELETE request" + ", clusterName=") + request.getClusterName()) + ", serviceName=") + request.getServiceName()) + ", componentName=") + request.getComponentName()) + ", hostname=") + request.getHostname()) + ", request=") + request);
            org.apache.ambari.server.state.Service service = cluster.getService(request.getServiceName());
            org.apache.ambari.server.state.ServiceComponent component = service.getServiceComponent(request.getComponentName());
            org.apache.ambari.server.state.ServiceComponentHost componentHost = component.getServiceComponentHost(request.getHostname());
            setRestartRequiredServices(service, request.getComponentName());
            try {
                checkIfHostComponentsInDeleteFriendlyState(request, cluster);
                if (!safeToRemoveSCHs.containsKey(component)) {
                    safeToRemoveSCHs.put(component, new java.util.HashSet<>());
                }
                safeToRemoveSCHs.get(component).add(componentHost);
            } catch (java.lang.Exception ex) {
                deleteMetaData.addException((request.getHostname() + "/") + request.getComponentName(), ex);
            }
        }
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> clusterServiceMasterForDecommissionMap = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.util.Set<java.lang.String>>> clusterMasterSlaveHostsMap = new java.util.HashMap<>();
        for (java.util.Map.Entry<org.apache.ambari.server.state.ServiceComponent, java.util.Set<org.apache.ambari.server.state.ServiceComponentHost>> entry : safeToRemoveSCHs.entrySet()) {
            for (org.apache.ambari.server.state.ServiceComponentHost componentHost : entry.getValue()) {
                try {
                    entry.getKey().deleteServiceComponentHosts(componentHost.getHostName(), deleteMetaData);
                    java.lang.String componentName = componentHost.getServiceComponentName();
                    if (org.apache.ambari.server.controller.AmbariCustomCommandExecutionHelper.masterToSlaveMappingForDecom.containsValue(componentName)) {
                        java.lang.String masterComponentName = null;
                        for (java.util.Map.Entry<java.lang.String, java.lang.String> entrySet : org.apache.ambari.server.controller.AmbariCustomCommandExecutionHelper.masterToSlaveMappingForDecom.entrySet()) {
                            if (entrySet.getValue().equals(componentName)) {
                                masterComponentName = entrySet.getKey();
                            }
                        }
                        if (clusterServiceMasterForDecommissionMap.containsKey(componentHost.getClusterName())) {
                            clusterServiceMasterForDecommissionMap.get(componentHost.getClusterName()).put(componentHost.getServiceName(), masterComponentName);
                            java.util.Map<java.lang.String, java.util.Set<java.lang.String>> masterSlaveMap = clusterMasterSlaveHostsMap.get(componentHost.getClusterName());
                            masterSlaveMap.putIfAbsent(masterComponentName, new java.util.HashSet<>());
                            masterSlaveMap.get(masterComponentName).add(componentHost.getHostName());
                        } else {
                            java.util.Map<java.lang.String, java.lang.String> serviceMasterMap = new java.util.HashMap<>();
                            serviceMasterMap.put(componentHost.getServiceName(), masterComponentName);
                            clusterServiceMasterForDecommissionMap.put(componentHost.getClusterName(), serviceMasterMap);
                            java.util.Map<java.lang.String, java.util.Set<java.lang.String>> masterSlaveHostsMap = new java.util.HashMap<>();
                            masterSlaveHostsMap.put(masterComponentName, new java.util.HashSet<>(java.util.Collections.singletonList(componentHost.getHostName())));
                            clusterMasterSlaveHostsMap.put(componentHost.getClusterName(), masterSlaveHostsMap);
                        }
                    }
                } catch (java.lang.Exception ex) {
                    deleteMetaData.addException((componentHost.getHostName() + "/") + componentHost.getServiceComponentName(), ex);
                }
            }
        }
        for (java.lang.String cluster : clusterServiceMasterForDecommissionMap.keySet()) {
            createAndExecuteRefreshIncludeExcludeFilesActionForMasters(clusterServiceMasterForDecommissionMap.get(cluster), clusterMasterSlaveHostsMap.get(cluster), cluster, true);
        }
        if ((deleteMetaData.getDeletedKeys().size() + deleteMetaData.getExceptionForKeys().size()) == 1) {
            if (deleteMetaData.getDeletedKeys().size() == 1) {
                STOMPComponentsDeleteHandler.processDeleteByMetaData(deleteMetaData);
                return null;
            }
            java.lang.Exception ex = deleteMetaData.getExceptionForKeys().values().iterator().next();
            if (ex instanceof org.apache.ambari.server.AmbariException) {
                throw ((org.apache.ambari.server.AmbariException) (ex));
            } else {
                throw new org.apache.ambari.server.AmbariException(ex.getMessage(), ex);
            }
        }
        if (!safeToRemoveSCHs.isEmpty()) {
            setMonitoringServicesRestartRequired(requests);
        }
        STOMPComponentsDeleteHandler.processDeleteByMetaData(deleteMetaData);
        return deleteMetaData;
    }

    @java.lang.Override
    public void deleteGroups(java.util.Set<org.apache.ambari.server.controller.GroupRequest> requests) throws org.apache.ambari.server.AmbariException {
        for (org.apache.ambari.server.controller.GroupRequest request : requests) {
            org.apache.ambari.server.controller.AmbariManagementControllerImpl.LOG.debug("Received a delete group request, groupname={}", request.getGroupName());
            final org.apache.ambari.server.security.authorization.Group group = users.getGroup(request.getGroupName());
            if (group != null) {
                users.removeGroup(group);
            }
        }
    }

    private void createAndExecuteRefreshIncludeExcludeFilesActionForMasters(java.util.Map<java.lang.String, java.lang.String> serviceMasterMap, java.util.Map<java.lang.String, java.util.Set<java.lang.String>> masterSlaveHostsMap, java.lang.String clusterName, boolean isDecommission) throws org.apache.ambari.server.AmbariException {
        serviceMasterMap.remove(org.apache.ambari.server.state.Service.Type.HBASE.toString());
        if (serviceMasterMap.isEmpty()) {
            return;
        }
        org.apache.ambari.server.controller.AmbariManagementControllerImpl.LOG.debug("Refresh include/exclude files action will be executed for " + serviceMasterMap);
        java.util.HashMap<java.lang.String, java.lang.String> requestProperties = new java.util.HashMap<>();
        requestProperties.put(org.apache.ambari.server.controller.internal.RequestResourceProvider.CONTEXT, "Update Include/Exclude Files for " + serviceMasterMap.keySet().toString());
        java.util.HashMap<java.lang.String, java.lang.String> params = new java.util.HashMap<>();
        params.put(org.apache.ambari.server.controller.AmbariCustomCommandExecutionHelper.UPDATE_FILES_ONLY, java.lang.String.valueOf(isDecommission));
        for (java.lang.String masterName : masterSlaveHostsMap.keySet()) {
            if (!isDecommission) {
                params.put((masterName + "_") + org.apache.ambari.server.controller.AmbariCustomCommandExecutionHelper.DECOM_INCLUDED_HOSTS, org.apache.commons.lang.StringUtils.join(masterSlaveHostsMap.get(masterName).toArray(), ","));
            }
        }
        params.put(org.apache.ambari.server.controller.AmbariCustomCommandExecutionHelper.IS_ADD_OR_DELETE_SLAVE_REQUEST, "true");
        java.util.List<org.apache.ambari.server.controller.internal.RequestResourceFilter> resourceFilters = new java.util.ArrayList<>(serviceMasterMap.size());
        for (java.lang.String serviceName : serviceMasterMap.keySet()) {
            resourceFilters.add(new org.apache.ambari.server.controller.internal.RequestResourceFilter(serviceName, serviceMasterMap.get(serviceName), null));
        }
        org.apache.ambari.server.controller.ExecuteActionRequest actionRequest = new org.apache.ambari.server.controller.ExecuteActionRequest(clusterName, org.apache.ambari.server.controller.AmbariCustomCommandExecutionHelper.DECOMMISSION_COMMAND_NAME, null, resourceFilters, null, params, false);
        createAction(actionRequest, requestProperties);
    }

    @java.lang.Override
    public void deleteMembers(java.util.Set<org.apache.ambari.server.controller.MemberRequest> requests) throws org.apache.ambari.server.AmbariException {
        for (org.apache.ambari.server.controller.MemberRequest request : requests) {
            org.apache.ambari.server.controller.AmbariManagementControllerImpl.LOG.debug("Received a delete member request, {}", request);
            users.removeMemberFromGroup(request.getGroupName(), request.getUserName());
        }
    }

    @java.lang.Override
    public void removeMpack(org.apache.ambari.server.orm.entities.MpackEntity mpackEntity, org.apache.ambari.server.orm.entities.StackEntity stackEntity) throws java.io.IOException {
        ambariMetaInfo.removeMpack(mpackEntity, stackEntity);
    }

    @java.lang.Override
    public java.util.Set<org.apache.ambari.server.controller.ServiceConfigVersionResponse> createServiceConfigVersion(java.util.Set<org.apache.ambari.server.controller.ServiceConfigVersionRequest> requests) throws org.apache.ambari.server.AmbariException, org.apache.ambari.server.security.authorization.AuthorizationException {
        return null;
    }

    org.apache.ambari.server.controller.RequestStatusResponse getRequestStatusResponse(long requestId) {
        org.apache.ambari.server.controller.RequestStatusResponse response = new org.apache.ambari.server.controller.RequestStatusResponse(requestId);
        java.util.List<org.apache.ambari.server.actionmanager.HostRoleCommand> hostRoleCommands = actionManager.getRequestTasks(requestId);
        response.setRequestContext(actionManager.getRequestContext(requestId));
        java.util.List<org.apache.ambari.server.controller.ShortTaskStatus> tasks = new java.util.ArrayList<>();
        for (org.apache.ambari.server.actionmanager.HostRoleCommand hostRoleCommand : hostRoleCommands) {
            tasks.add(new org.apache.ambari.server.controller.ShortTaskStatus(hostRoleCommand));
        }
        response.setTasks(tasks);
        return response;
    }

    @java.lang.Override
    public java.util.Set<org.apache.ambari.server.controller.ClusterResponse> getClusters(java.util.Set<org.apache.ambari.server.controller.ClusterRequest> requests) throws org.apache.ambari.server.AmbariException, org.apache.ambari.server.security.authorization.AuthorizationException {
        java.util.Set<org.apache.ambari.server.controller.ClusterResponse> response = new java.util.HashSet<>();
        for (org.apache.ambari.server.controller.ClusterRequest request : requests) {
            try {
                response.addAll(getClusters(request));
            } catch (org.apache.ambari.server.ClusterNotFoundException e) {
                if (requests.size() == 1) {
                    throw e;
                }
            }
        }
        return response;
    }

    @java.lang.Override
    public java.util.Set<org.apache.ambari.server.controller.ServiceComponentHostResponse> getHostComponents(java.util.Set<org.apache.ambari.server.controller.ServiceComponentHostRequest> requests) throws org.apache.ambari.server.AmbariException {
        return getHostComponents(requests, false);
    }

    @java.lang.Override
    public java.util.Set<org.apache.ambari.server.controller.ServiceComponentHostResponse> getHostComponents(java.util.Set<org.apache.ambari.server.controller.ServiceComponentHostRequest> requests, boolean statusOnly) throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.server.controller.AmbariManagementControllerImpl.LOG.debug("Processing requests: {}", requests);
        java.util.Set<org.apache.ambari.server.controller.ServiceComponentHostResponse> response = new java.util.HashSet<>();
        for (org.apache.ambari.server.controller.ServiceComponentHostRequest request : requests) {
            try {
                response.addAll(getHostComponents(request, statusOnly));
            } catch (org.apache.ambari.server.ServiceComponentHostNotFoundException | org.apache.ambari.server.ServiceComponentNotFoundException | org.apache.ambari.server.ServiceNotFoundException e) {
                if (requests.size() == 1) {
                    throw e;
                } else {
                    org.apache.ambari.server.controller.AmbariManagementControllerImpl.LOG.debug("Ignoring not found exception due to other requests", e);
                }
            } catch (org.apache.ambari.server.ParentObjectNotFoundException e) {
                boolean throwException = true;
                if ((requests.size() > 1) && org.apache.ambari.server.HostNotFoundException.class.isInstance(e.getCause())) {
                    for (org.apache.ambari.server.controller.ServiceComponentHostRequest r : requests) {
                        if (r.getHostname() == null) {
                            throwException = false;
                            org.apache.ambari.server.controller.AmbariManagementControllerImpl.LOG.debug("HostNotFoundException ignored", e);
                            break;
                        }
                    }
                }
                if (throwException) {
                    throw e;
                }
            }
        }
        return response;
    }

    @java.lang.Override
    public java.util.Set<org.apache.ambari.server.controller.ConfigurationResponse> getConfigurations(java.util.Set<org.apache.ambari.server.controller.ConfigurationRequest> requests) throws org.apache.ambari.server.AmbariException {
        java.util.Set<org.apache.ambari.server.controller.ConfigurationResponse> response = new java.util.HashSet<>();
        for (org.apache.ambari.server.controller.ConfigurationRequest request : requests) {
            response.addAll(getConfigurations(request));
        }
        return response;
    }

    @java.lang.Override
    public java.util.Set<org.apache.ambari.server.controller.ServiceConfigVersionResponse> getServiceConfigVersions(java.util.Set<org.apache.ambari.server.controller.ServiceConfigVersionRequest> requests) throws org.apache.ambari.server.AmbariException {
        java.util.Set<org.apache.ambari.server.controller.ServiceConfigVersionResponse> responses = new java.util.LinkedHashSet<>();
        for (org.apache.ambari.server.controller.ServiceConfigVersionRequest request : requests) {
            responses.addAll(getServiceConfigVersions(request));
        }
        return responses;
    }

    private java.util.Set<org.apache.ambari.server.controller.ServiceConfigVersionResponse> getServiceConfigVersions(org.apache.ambari.server.controller.ServiceConfigVersionRequest request) throws org.apache.ambari.server.AmbariException {
        if (request.getClusterName() == null) {
            throw new java.lang.IllegalArgumentException("Invalid arguments, cluster name" + " should not be null");
        }
        org.apache.ambari.server.state.Cluster cluster = clusters.getCluster(request.getClusterName());
        java.util.Set<org.apache.ambari.server.controller.ServiceConfigVersionResponse> result = new java.util.LinkedHashSet<>();
        java.lang.String serviceName = request.getServiceName();
        java.util.List<org.apache.ambari.server.controller.ServiceConfigVersionResponse> serviceConfigVersionResponses = new java.util.ArrayList<>();
        if (java.lang.Boolean.TRUE.equals(request.getIsCurrent()) && (serviceName != null)) {
            serviceConfigVersionResponses.addAll(cluster.getActiveServiceConfigVersionResponse(serviceName));
        } else {
            serviceConfigVersionResponses.addAll(cluster.getServiceConfigVersions());
        }
        for (org.apache.ambari.server.controller.ServiceConfigVersionResponse response : serviceConfigVersionResponses) {
            if ((serviceName != null) && (!org.apache.commons.lang.StringUtils.equals(serviceName, response.getServiceName()))) {
                continue;
            }
            if ((request.getVersion() != null) && (org.apache.commons.lang.math.NumberUtils.compare(request.getVersion(), response.getVersion()) != 0)) {
                continue;
            }
            if ((request.getUserName() != null) && (!org.apache.commons.lang.StringUtils.equals(request.getUserName(), response.getUserName()))) {
                continue;
            }
            result.add(response);
        }
        return result;
    }

    @java.lang.Override
    public java.util.Set<org.apache.ambari.server.controller.GroupResponse> getGroups(java.util.Set<org.apache.ambari.server.controller.GroupRequest> requests) throws org.apache.ambari.server.AmbariException {
        final java.util.Set<org.apache.ambari.server.controller.GroupResponse> responses = new java.util.HashSet<>();
        for (org.apache.ambari.server.controller.GroupRequest request : requests) {
            org.apache.ambari.server.controller.AmbariManagementControllerImpl.LOG.debug("Received a getGroups request, groupRequest={}", request);
            if (null == request.getGroupName()) {
                for (org.apache.ambari.server.security.authorization.Group group : users.getAllGroups()) {
                    final org.apache.ambari.server.controller.GroupResponse response = new org.apache.ambari.server.controller.GroupResponse(group.getGroupName(), group.isLdapGroup(), group.getGroupType());
                    responses.add(response);
                }
            } else {
                final org.apache.ambari.server.security.authorization.Group group = users.getGroup(request.getGroupName());
                if (null == group) {
                    if (requests.size() == 1) {
                        throw new org.apache.ambari.server.ObjectNotFoundException(("Cannot find group '" + request.getGroupName()) + "'");
                    }
                } else {
                    final org.apache.ambari.server.controller.GroupResponse response = new org.apache.ambari.server.controller.GroupResponse(group.getGroupName(), group.isLdapGroup(), group.getGroupType());
                    responses.add(response);
                }
            }
        }
        return responses;
    }

    @java.lang.Override
    public void updateGroups(java.util.Set<org.apache.ambari.server.controller.GroupRequest> requests) throws org.apache.ambari.server.AmbariException {
    }

    protected java.lang.String getClientHostForRunningAction(org.apache.ambari.server.state.Cluster cluster, org.apache.ambari.server.state.Service service, org.apache.ambari.server.state.ServiceComponent serviceComponent) throws org.apache.ambari.server.AmbariException {
        if ((serviceComponent != null) && (!serviceComponent.getServiceComponentHosts().isEmpty())) {
            java.util.Set<java.lang.String> candidateHosts = serviceComponent.getServiceComponentHosts().keySet();
            filterHostsForAction(candidateHosts, service, cluster, org.apache.ambari.server.controller.spi.Resource.Type.Cluster);
            return getHealthyHost(candidateHosts);
        }
        return null;
    }

    protected org.apache.ambari.server.state.ServiceComponent getClientComponentForRunningAction(org.apache.ambari.server.state.Cluster cluster, org.apache.ambari.server.state.Service service) throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.server.state.StackId stackId = service.getDesiredStackId();
        org.apache.ambari.server.state.ComponentInfo compInfo = ambariMetaInfo.getService(stackId.getStackName(), stackId.getStackVersion(), service.getName()).getClientComponent();
        if (compInfo != null) {
            try {
                org.apache.ambari.server.state.ServiceComponent serviceComponent = service.getServiceComponent(compInfo.getName());
                if (!serviceComponent.getServiceComponentHosts().isEmpty()) {
                    return serviceComponent;
                }
            } catch (org.apache.ambari.server.ServiceComponentNotFoundException e) {
                org.apache.ambari.server.controller.AmbariManagementControllerImpl.LOG.warn(((((("Could not find required component to run action" + ", clusterName=") + cluster.getClusterName()) + ", serviceName=") + service.getName()) + ", componentName=") + compInfo.getName());
            }
        }
        java.util.Map<java.lang.String, org.apache.ambari.server.state.ServiceComponent> components = service.getServiceComponents();
        if (!components.isEmpty()) {
            for (org.apache.ambari.server.state.ServiceComponent serviceComponent : components.values()) {
                if (!serviceComponent.getServiceComponentHosts().isEmpty()) {
                    return serviceComponent;
                }
            }
        }
        return null;
    }

    protected void filterHostsForAction(java.util.Set<java.lang.String> candidateHosts, org.apache.ambari.server.state.Service service, final org.apache.ambari.server.state.Cluster cluster, final org.apache.ambari.server.controller.spi.Resource.Type level) throws org.apache.ambari.server.AmbariException {
        java.util.Set<java.lang.String> ignoredHosts = maintenanceStateHelper.filterHostsInMaintenanceState(candidateHosts, new org.apache.ambari.server.controller.MaintenanceStateHelper.HostPredicate() {
            @java.lang.Override
            public boolean shouldHostBeRemoved(final java.lang.String hostname) throws org.apache.ambari.server.AmbariException {
                org.apache.ambari.server.state.Host host = clusters.getHost(hostname);
                return !maintenanceStateHelper.isOperationAllowed(host, cluster.getClusterId(), level);
            }
        });
        org.apache.ambari.server.controller.AmbariManagementControllerImpl.LOG.debug("Ignoring hosts when selecting available hosts for action due to maintenance state.Ignored hosts ={}, cluster={}, service={}", ignoredHosts, cluster.getClusterName(), service.getName());
    }

    @java.lang.Override
    public java.util.List<java.lang.String> selectHealthyHosts(java.util.Set<java.lang.String> hostList) throws org.apache.ambari.server.AmbariException {
        java.util.List<java.lang.String> healthyHosts = new java.util.ArrayList<>();
        for (java.lang.String candidateHostName : hostList) {
            org.apache.ambari.server.state.Host candidateHost = clusters.getHost(candidateHostName);
            if (candidateHost.getState() == org.apache.ambari.server.state.HostState.HEALTHY) {
                healthyHosts.add(candidateHostName);
            }
        }
        return healthyHosts;
    }

    @java.lang.Override
    public java.lang.String getHealthyHost(java.util.Set<java.lang.String> hostList) throws org.apache.ambari.server.AmbariException {
        java.util.List<java.lang.String> healthyHosts = selectHealthyHosts(hostList);
        if (!healthyHosts.isEmpty()) {
            java.util.Collections.shuffle(healthyHosts);
            return healthyHosts.get(0);
        }
        return null;
    }

    @java.lang.Override
    public org.apache.ambari.server.controller.RequestStatusResponse createAction(org.apache.ambari.server.controller.ExecuteActionRequest actionRequest, java.util.Map<java.lang.String, java.lang.String> requestProperties) throws org.apache.ambari.server.AmbariException {
        java.lang.String clusterName = actionRequest.getClusterName();
        java.lang.String requestContext = "";
        if (requestProperties != null) {
            requestContext = org.apache.commons.text.StringEscapeUtils.escapeHtml4(requestProperties.get(org.apache.ambari.server.controller.internal.RequestResourceProvider.CONTEXT));
            if (requestContext == null) {
                requestContext = "";
            }
        }
        org.apache.ambari.server.state.Cluster cluster = null;
        if (null != clusterName) {
            cluster = clusters.getCluster(clusterName);
            org.apache.ambari.server.controller.AmbariManagementControllerImpl.LOG.info(((("Received action execution request" + ", clusterName=") + actionRequest.getClusterName()) + ", request=") + actionRequest);
        }
        org.apache.ambari.server.controller.ActionExecutionContext actionExecContext = getActionExecutionContext(actionRequest);
        if (actionRequest.isCommand()) {
            customCommandExecutionHelper.validateAction(actionRequest);
        } else {
            actionExecutionHelper.validateAction(actionRequest);
        }
        long requestId = actionManager.getNextRequestId();
        org.apache.ambari.server.controller.internal.RequestStageContainer requestStageContainer = new org.apache.ambari.server.controller.internal.RequestStageContainer(requestId, null, requestFactory, actionManager, actionRequest);
        @org.apache.ambari.annotations.Experimental(feature = org.apache.ambari.annotations.ExperimentalFeature.MULTI_SERVICE, comment = "This must change with Multi-Service since the cluster won't have a desired stack version")
        org.apache.ambari.server.controller.ExecuteCommandJson jsons = customCommandExecutionHelper.getCommandJson(actionExecContext, cluster, null == cluster ? null : cluster.getDesiredStackVersion(), requestContext);
        java.lang.String commandParamsForStage = jsons.getCommandParamsForStage();
        java.util.Map<java.lang.String, java.lang.String> commandParamsStage = gson.fromJson(commandParamsForStage, new com.google.gson.reflect.TypeToken<java.util.Map<java.lang.String, java.lang.String>>() {}.getType());
        if (!requestContext.isEmpty()) {
            requestStageContainer.setRequestContext(requestContext);
        }
        org.apache.ambari.server.utils.SecretReference.replaceReferencesWithPasswords(commandParamsStage, cluster);
        boolean kerberosServiceCheck = org.apache.ambari.server.Role.KERBEROS_SERVICE_CHECK.name().equals(actionRequest.getCommandName());
        if (kerberosServiceCheck) {
            try {
                requestStageContainer = kerberosHelper.createTestIdentity(cluster, commandParamsStage, requestStageContainer);
            } catch (org.apache.ambari.server.serveraction.kerberos.KerberosOperationException e) {
                throw new java.lang.IllegalArgumentException(e.getMessage(), e);
            }
        }
        commandParamsForStage = gson.toJson(commandParamsStage);
        org.apache.ambari.server.actionmanager.Stage stage = createNewStage(requestStageContainer.getLastStageId(), cluster, requestId, requestContext, commandParamsForStage, jsons.getHostParamsForStage());
        if (actionRequest.isCommand()) {
            customCommandExecutionHelper.addExecutionCommandsToStage(actionExecContext, stage, requestProperties, jsons);
        } else {
            actionExecutionHelper.addExecutionCommandsToStage(actionExecContext, stage, requestProperties);
        }
        org.apache.ambari.server.stageplanner.RoleGraph rg;
        if (null != cluster) {
            org.apache.ambari.server.metadata.RoleCommandOrder rco = getRoleCommandOrder(cluster);
            rg = roleGraphFactory.createNew(rco);
        } else {
            rg = roleGraphFactory.createNew();
        }
        rg.build(stage);
        java.util.List<org.apache.ambari.server.actionmanager.Stage> stages = rg.getStages();
        if ((stages != null) && (!stages.isEmpty())) {
            requestStageContainer.addStages(stages);
        }
        if (kerberosServiceCheck) {
            commandParamsStage = gson.fromJson(commandParamsForStage, new com.google.gson.reflect.TypeToken<java.util.Map<java.lang.String, java.lang.String>>() {}.getType());
            try {
                requestStageContainer = kerberosHelper.deleteTestIdentity(cluster, commandParamsStage, requestStageContainer);
            } catch (org.apache.ambari.server.serveraction.kerberos.KerberosOperationException e) {
                throw new java.lang.IllegalArgumentException(e.getMessage(), e);
            }
        }
        requestStageContainer.persist();
        return requestStageContainer.getRequestStatusResponse();
    }

    @java.lang.Override
    public java.util.Set<org.apache.ambari.server.controller.StackResponse> getStacks(java.util.Set<org.apache.ambari.server.controller.StackRequest> requests) throws org.apache.ambari.server.AmbariException {
        java.util.Set<org.apache.ambari.server.controller.StackResponse> response = new java.util.HashSet<>();
        for (org.apache.ambari.server.controller.StackRequest request : requests) {
            try {
                response.addAll(getStacks(request));
            } catch (org.apache.ambari.server.StackAccessException e) {
                if (requests.size() == 1) {
                    throw e;
                }
            }
        }
        return response;
    }

    private java.util.Set<org.apache.ambari.server.controller.StackResponse> getStacks(org.apache.ambari.server.controller.StackRequest request) throws org.apache.ambari.server.AmbariException {
        java.util.Set<org.apache.ambari.server.controller.StackResponse> response;
        java.lang.String stackName = request.getStackName();
        if (stackName != null) {
            ambariMetaInfo.getStacks(stackName);
            response = java.util.Collections.singleton(new org.apache.ambari.server.controller.StackResponse(stackName));
        } else {
            java.util.Collection<org.apache.ambari.server.state.StackInfo> supportedStacks = ambariMetaInfo.getStacks();
            response = new java.util.HashSet<>();
            for (org.apache.ambari.server.state.StackInfo stack : supportedStacks) {
                response.add(new org.apache.ambari.server.controller.StackResponse(stack.getName()));
            }
        }
        return response;
    }

    @java.lang.Override
    public synchronized org.apache.ambari.server.controller.RequestStatusResponse updateStacks() throws org.apache.ambari.server.AmbariException {
        try {
            ambariMetaInfo.init();
        } catch (org.apache.ambari.server.AmbariException e) {
            throw e;
        } catch (java.lang.Exception e) {
            throw new org.apache.ambari.server.AmbariException("Ambari Meta Information can't be read from the stack root directory");
        }
        return null;
    }

    @java.lang.Override
    public java.util.Set<org.apache.ambari.server.controller.ExtensionResponse> getExtensions(java.util.Set<org.apache.ambari.server.controller.ExtensionRequest> requests) throws org.apache.ambari.server.AmbariException {
        java.util.Set<org.apache.ambari.server.controller.ExtensionResponse> response = new java.util.HashSet<>();
        for (org.apache.ambari.server.controller.ExtensionRequest request : requests) {
            try {
                response.addAll(getExtensions(request));
            } catch (org.apache.ambari.server.StackAccessException e) {
                if (requests.size() == 1) {
                    throw e;
                }
            }
        }
        return response;
    }

    private java.util.Set<org.apache.ambari.server.controller.ExtensionResponse> getExtensions(org.apache.ambari.server.controller.ExtensionRequest request) throws org.apache.ambari.server.AmbariException {
        java.util.Set<org.apache.ambari.server.controller.ExtensionResponse> response;
        java.lang.String extensionName = request.getExtensionName();
        if (extensionName != null) {
            ambariMetaInfo.getExtensions(extensionName);
            response = java.util.Collections.singleton(new org.apache.ambari.server.controller.ExtensionResponse(extensionName));
        } else {
            java.util.Collection<org.apache.ambari.server.state.ExtensionInfo> supportedExtensions = ambariMetaInfo.getExtensions();
            response = new java.util.HashSet<>();
            for (org.apache.ambari.server.state.ExtensionInfo extension : supportedExtensions) {
                response.add(new org.apache.ambari.server.controller.ExtensionResponse(extension.getName()));
            }
        }
        return response;
    }

    @java.lang.Override
    public java.util.Set<org.apache.ambari.server.controller.ExtensionVersionResponse> getExtensionVersions(java.util.Set<org.apache.ambari.server.controller.ExtensionVersionRequest> requests) throws org.apache.ambari.server.AmbariException {
        java.util.Set<org.apache.ambari.server.controller.ExtensionVersionResponse> response = new java.util.HashSet<>();
        for (org.apache.ambari.server.controller.ExtensionVersionRequest request : requests) {
            java.lang.String extensionName = request.getExtensionName();
            try {
                java.util.Set<org.apache.ambari.server.controller.ExtensionVersionResponse> stackVersions = getExtensionVersions(request);
                for (org.apache.ambari.server.controller.ExtensionVersionResponse stackVersionResponse : stackVersions) {
                    stackVersionResponse.setExtensionName(extensionName);
                }
                response.addAll(stackVersions);
            } catch (org.apache.ambari.server.StackAccessException e) {
                if (requests.size() == 1) {
                    throw e;
                }
            }
        }
        return response;
    }

    private java.util.Set<org.apache.ambari.server.controller.ExtensionVersionResponse> getExtensionVersions(org.apache.ambari.server.controller.ExtensionVersionRequest request) throws org.apache.ambari.server.AmbariException {
        java.util.Set<org.apache.ambari.server.controller.ExtensionVersionResponse> response;
        java.lang.String extensionName = request.getExtensionName();
        java.lang.String extensionVersion = request.getExtensionVersion();
        if (extensionVersion != null) {
            org.apache.ambari.server.state.ExtensionInfo extensionInfo = ambariMetaInfo.getExtension(extensionName, extensionVersion);
            response = java.util.Collections.singleton(extensionInfo.convertToResponse());
        } else {
            try {
                java.util.Collection<org.apache.ambari.server.state.ExtensionInfo> extensionInfos = ambariMetaInfo.getExtensions(extensionName);
                response = new java.util.HashSet<>();
                for (org.apache.ambari.server.state.ExtensionInfo extensionInfo : extensionInfos) {
                    response.add(extensionInfo.convertToResponse());
                }
            } catch (org.apache.ambari.server.StackAccessException e) {
                response = java.util.Collections.emptySet();
            }
        }
        return response;
    }

    @java.lang.Override
    public java.util.Set<org.apache.ambari.server.controller.RepositoryResponse> getRepositories(java.util.Set<org.apache.ambari.server.controller.RepositoryRequest> requests) throws org.apache.ambari.server.AmbariException {
        java.util.Set<org.apache.ambari.server.controller.RepositoryResponse> response = new java.util.HashSet<>();
        for (org.apache.ambari.server.controller.RepositoryRequest request : requests) {
            try {
                java.lang.String stackName = request.getStackName();
                java.lang.String stackVersion = request.getStackVersion();
                java.util.Set<org.apache.ambari.server.controller.RepositoryResponse> repositories = getRepositories(request);
                for (org.apache.ambari.server.controller.RepositoryResponse repositoryResponse : repositories) {
                    if (repositoryResponse.getStackName() == null) {
                        repositoryResponse.setStackName(stackName);
                    }
                    if (repositoryResponse.getStackVersion() == null) {
                        repositoryResponse.setStackVersion(stackVersion);
                    }
                    repositoryResponse.setClusterVersionId(request.getClusterVersionId());
                }
                response.addAll(repositories);
            } catch (org.apache.ambari.server.StackAccessException e) {
                if (requests.size() == 1) {
                    throw e;
                }
            }
        }
        return response;
    }

    @org.apache.ambari.annotations.Experimental(feature = org.apache.ambari.annotations.ExperimentalFeature.CUSTOM_SERVICE_REPOS, comment = "Remove logic for handling custom service repos after enabling multi-mpack cluster deployment")
    private java.util.Set<org.apache.ambari.server.controller.RepositoryResponse> getRepositories(org.apache.ambari.server.controller.RepositoryRequest request) throws org.apache.ambari.server.AmbariException {
        java.lang.String stackName = request.getStackName();
        java.lang.String stackVersion = request.getStackVersion();
        java.lang.String osType = request.getOsType();
        java.lang.String repoId = request.getRepoId();
        java.lang.Long repositoryVersionId = request.getRepositoryVersionId();
        java.lang.String versionDefinitionId = request.getVersionDefinitionId();
        if ((null == repositoryVersionId) && (null != versionDefinitionId)) {
            if (org.apache.commons.lang.math.NumberUtils.isDigits(versionDefinitionId)) {
                repositoryVersionId = java.lang.Long.valueOf(versionDefinitionId);
            }
        }
        java.util.Set<org.apache.ambari.server.controller.RepositoryResponse> responses = new java.util.HashSet<>();
        if (repositoryVersionId != null) {
            final org.apache.ambari.server.orm.entities.RepositoryVersionEntity repositoryVersion = repositoryVersionDAO.findByPK(repositoryVersionId);
            if (repositoryVersion != null) {
                for (org.apache.ambari.server.orm.entities.RepoOsEntity operatingSystem : repositoryVersion.getRepoOsEntities()) {
                    if (operatingSystem.getFamily().equals(osType)) {
                        for (org.apache.ambari.server.orm.entities.RepoDefinitionEntity repository : operatingSystem.getRepoDefinitionEntities()) {
                            final org.apache.ambari.server.controller.RepositoryResponse response = new org.apache.ambari.server.controller.RepositoryResponse(repository.getBaseUrl(), osType, repository.getRepoID(), repository.getRepoName(), repository.getDistribution(), repository.getComponents(), "", "", repository.getTags(), repository.getApplicableServices());
                            if (null != versionDefinitionId) {
                                response.setVersionDefinitionId(versionDefinitionId);
                            } else {
                                response.setRepositoryVersionId(repositoryVersionId);
                            }
                            response.setStackName(repositoryVersion.getStackName());
                            response.setStackVersion(repositoryVersion.getStackVersion());
                            responses.add(response);
                        }
                        break;
                    }
                }
            }
        } else if (null != versionDefinitionId) {
            org.apache.ambari.server.state.repository.VersionDefinitionXml xml = ambariMetaInfo.getVersionDefinition(versionDefinitionId);
            if (null == xml) {
                throw new org.apache.ambari.server.AmbariException(java.lang.String.format("Version identified by %s does not exist", versionDefinitionId));
            }
            org.apache.ambari.server.state.StackId stackId = new org.apache.ambari.server.state.StackId(xml.release.stackId);
            com.google.common.collect.ListMultimap<java.lang.String, org.apache.ambari.server.state.RepositoryInfo> stackRepositoriesByOs = ambariMetaInfo.getStackManager().getStack(stackName, stackVersion).getRepositoriesByOs();
            for (org.apache.ambari.server.state.stack.RepositoryXml.Os os : xml.repositoryInfo.getOses()) {
                for (org.apache.ambari.server.state.stack.RepositoryXml.Repo repo : os.getRepos()) {
                    org.apache.ambari.server.controller.RepositoryResponse resp = new org.apache.ambari.server.controller.RepositoryResponse(repo.getBaseUrl(), os.getFamily(), repo.getRepoId(), repo.getRepoName(), repo.getDistribution(), repo.getComponents(), repo.getMirrorsList(), repo.getBaseUrl(), repo.getTags(), java.util.Collections.EMPTY_LIST);
                    resp.setVersionDefinitionId(versionDefinitionId);
                    resp.setStackName(stackId.getStackName());
                    resp.setStackVersion(stackId.getStackVersion());
                    responses.add(resp);
                }
            }
            java.util.List<org.apache.ambari.server.state.RepositoryInfo> serviceRepos = org.apache.ambari.server.stack.RepoUtil.getServiceRepos(xml.repositoryInfo.getRepositories(), stackRepositoriesByOs);
            responses.addAll(org.apache.ambari.server.stack.RepoUtil.asResponses(serviceRepos, versionDefinitionId, stackName, stackVersion));
        } else if (repoId == null) {
            java.util.List<org.apache.ambari.server.state.RepositoryInfo> repositories = ambariMetaInfo.getRepositories(stackName, stackVersion, osType);
            for (org.apache.ambari.server.state.RepositoryInfo repository : repositories) {
                responses.add(repository.convertToResponse());
            }
        } else {
            org.apache.ambari.server.state.RepositoryInfo repository = ambariMetaInfo.getRepository(stackName, stackVersion, osType, repoId);
            responses = java.util.Collections.singleton(repository.convertToResponse());
        }
        return responses;
    }

    @java.lang.Override
    public void verifyRepositories(java.util.Set<org.apache.ambari.server.controller.RepositoryRequest> requests) throws org.apache.ambari.server.AmbariException {
        for (org.apache.ambari.server.controller.RepositoryRequest request : requests) {
            if (request.getBaseUrl() == null) {
                throw new org.apache.ambari.server.AmbariException("Base url is missing for request " + request);
            }
            verifyRepository(request);
        }
    }

    private void verifyRepository(org.apache.ambari.server.controller.RepositoryRequest request) throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.server.controller.internal.URLRedirectProvider usp = new org.apache.ambari.server.controller.internal.URLRedirectProvider(org.apache.ambari.server.controller.AmbariManagementControllerImpl.REPO_URL_CONNECT_TIMEOUT, org.apache.ambari.server.controller.AmbariManagementControllerImpl.REPO_URL_READ_TIMEOUT, true);
        java.lang.String repoName = request.getRepoName();
        if (org.apache.commons.lang.StringUtils.isEmpty(repoName)) {
            throw new java.lang.IllegalArgumentException("repo_name is required to verify repository");
        }
        java.lang.String errorMessage = null;
        java.lang.Exception e = null;
        java.lang.String[] suffixes = configs.getRepoValidationSuffixes(request.getOsType());
        for (java.lang.String suffix : suffixes) {
            java.lang.String formatted_suffix = java.lang.String.format(suffix, repoName);
            java.lang.String spec = request.getBaseUrl().trim();
            if ((spec.charAt(spec.length() - 1) != '/') && (formatted_suffix.charAt(0) != '/')) {
                spec = (spec + "/") + formatted_suffix;
            } else if ((spec.charAt(spec.length() - 1) == '/') && (formatted_suffix.charAt(0) == '/')) {
                spec = spec + formatted_suffix.substring(1);
            } else {
                spec = spec + formatted_suffix;
            }
            final java.lang.String FILE_SCHEME = "file://";
            if (spec.toLowerCase().startsWith(FILE_SCHEME)) {
                java.lang.String filePath = spec.substring(FILE_SCHEME.length());
                java.io.File f = new java.io.File(filePath);
                if (!f.exists()) {
                    errorMessage = ("Could not access base url . " + spec) + " . ";
                    e = new java.io.FileNotFoundException(errorMessage);
                    break;
                }
            } else {
                try {
                    org.apache.ambari.server.controller.internal.URLRedirectProvider.RequestResult result = usp.executeGet(spec);
                    if (result.getCode() != org.apache.http.HttpStatus.SC_OK) {
                        errorMessage = java.lang.String.format("Could not access base url '%s', code: '%d', response: '%s'", org.apache.ambari.server.utils.URLCredentialsHider.hideCredentials(request.getBaseUrl()), result.getCode(), result.getContent());
                    }
                } catch (java.io.IOException ioe) {
                    e = ioe;
                    errorMessage = java.lang.String.format("Could not access base url '%s'", org.apache.ambari.server.utils.URLCredentialsHider.hideCredentials(request.getBaseUrl()));
                    if (org.apache.ambari.server.controller.AmbariManagementControllerImpl.LOG.isDebugEnabled()) {
                        errorMessage += ioe;
                    } else {
                        errorMessage += ioe.getMessage();
                    }
                    break;
                }
            }
        }
        if (errorMessage != null) {
            org.apache.ambari.server.controller.AmbariManagementControllerImpl.LOG.error(errorMessage);
            if (e == null) {
                throw new java.lang.IllegalArgumentException(errorMessage);
            } else {
                throw new java.lang.IllegalArgumentException(errorMessage, e);
            }
        }
    }

    @java.lang.Override
    public java.util.Set<org.apache.ambari.server.controller.StackVersionResponse> getStackVersions(java.util.Set<org.apache.ambari.server.controller.StackVersionRequest> requests) throws org.apache.ambari.server.AmbariException {
        java.util.Set<org.apache.ambari.server.controller.StackVersionResponse> response = new java.util.HashSet<>();
        for (org.apache.ambari.server.controller.StackVersionRequest request : requests) {
            java.lang.String stackName = request.getStackName();
            try {
                java.util.Set<org.apache.ambari.server.controller.StackVersionResponse> stackVersions = getStackVersions(request);
                for (org.apache.ambari.server.controller.StackVersionResponse stackVersionResponse : stackVersions) {
                    stackVersionResponse.setStackName(stackName);
                }
                response.addAll(stackVersions);
            } catch (org.apache.ambari.server.StackAccessException e) {
                if (requests.size() == 1) {
                    throw e;
                }
            }
        }
        return response;
    }

    private java.util.Set<org.apache.ambari.server.controller.StackVersionResponse> getStackVersions(org.apache.ambari.server.controller.StackVersionRequest request) throws org.apache.ambari.server.AmbariException {
        java.util.Set<org.apache.ambari.server.controller.StackVersionResponse> response;
        java.lang.String stackName = request.getStackName();
        java.lang.String stackVersion = request.getStackVersion();
        if (stackVersion != null) {
            org.apache.ambari.server.state.StackInfo stackInfo = ambariMetaInfo.getStack(stackName, stackVersion);
            response = java.util.Collections.singleton(stackInfo.convertToResponse());
        } else {
            try {
                java.util.Collection<org.apache.ambari.server.state.StackInfo> stackInfos = ambariMetaInfo.getStacks(stackName);
                response = new java.util.HashSet<>();
                for (org.apache.ambari.server.state.StackInfo stackInfo : stackInfos) {
                    response.add(stackInfo.convertToResponse());
                }
            } catch (org.apache.ambari.server.StackAccessException e) {
                response = java.util.Collections.emptySet();
            }
        }
        return response;
    }

    @java.lang.Override
    public java.util.Set<org.apache.ambari.server.controller.StackServiceResponse> getStackServices(java.util.Set<org.apache.ambari.server.controller.StackServiceRequest> requests) throws org.apache.ambari.server.AmbariException {
        java.util.Set<org.apache.ambari.server.controller.StackServiceResponse> response = new java.util.HashSet<>();
        for (org.apache.ambari.server.controller.StackServiceRequest request : requests) {
            java.lang.String stackName = request.getStackName();
            java.lang.String stackVersion = request.getStackVersion();
            try {
                java.util.Set<org.apache.ambari.server.controller.StackServiceResponse> stackServices = getStackServices(request);
                for (org.apache.ambari.server.controller.StackServiceResponse stackServiceResponse : stackServices) {
                    stackServiceResponse.setStackName(stackName);
                    stackServiceResponse.setStackVersion(stackVersion);
                }
                response.addAll(stackServices);
            } catch (org.apache.ambari.server.StackAccessException e) {
                if (requests.size() == 1) {
                    throw e;
                }
            }
        }
        return response;
    }

    private java.util.Set<org.apache.ambari.server.controller.StackServiceResponse> getStackServices(org.apache.ambari.server.controller.StackServiceRequest request) throws org.apache.ambari.server.AmbariException {
        java.util.Set<org.apache.ambari.server.controller.StackServiceResponse> response;
        java.lang.String stackName = request.getStackName();
        java.lang.String stackVersion = request.getStackVersion();
        java.lang.String serviceName = request.getServiceName();
        if (serviceName != null) {
            org.apache.ambari.server.state.ServiceInfo service = ambariMetaInfo.getService(stackName, stackVersion, serviceName);
            response = java.util.Collections.singleton(new org.apache.ambari.server.controller.StackServiceResponse(service));
        } else {
            java.util.Map<java.lang.String, org.apache.ambari.server.state.ServiceInfo> services = ambariMetaInfo.getServices(stackName, stackVersion);
            response = new java.util.HashSet<>();
            for (org.apache.ambari.server.state.ServiceInfo service : services.values()) {
                response.add(new org.apache.ambari.server.controller.StackServiceResponse(service));
            }
        }
        return response;
    }

    @java.lang.Override
    public java.util.Set<org.apache.ambari.server.controller.StackConfigurationResponse> getStackLevelConfigurations(java.util.Set<org.apache.ambari.server.controller.StackLevelConfigurationRequest> requests) throws org.apache.ambari.server.AmbariException {
        java.util.Set<org.apache.ambari.server.controller.StackConfigurationResponse> response = new java.util.HashSet<>();
        for (org.apache.ambari.server.controller.StackLevelConfigurationRequest request : requests) {
            java.lang.String stackName = request.getStackName();
            java.lang.String stackVersion = request.getStackVersion();
            java.util.Set<org.apache.ambari.server.controller.StackConfigurationResponse> stackConfigurations = getStackLevelConfigurations(request);
            for (org.apache.ambari.server.controller.StackConfigurationResponse stackConfigurationResponse : stackConfigurations) {
                stackConfigurationResponse.setStackName(stackName);
                stackConfigurationResponse.setStackVersion(stackVersion);
            }
            response.addAll(stackConfigurations);
        }
        return response;
    }

    private java.util.Set<org.apache.ambari.server.controller.StackConfigurationResponse> getStackLevelConfigurations(org.apache.ambari.server.controller.StackLevelConfigurationRequest request) throws org.apache.ambari.server.AmbariException {
        java.util.Set<org.apache.ambari.server.controller.StackConfigurationResponse> response = new java.util.HashSet<>();
        java.lang.String stackName = request.getStackName();
        java.lang.String stackVersion = request.getStackVersion();
        java.lang.String propertyName = request.getPropertyName();
        java.util.Set<org.apache.ambari.server.state.PropertyInfo> configs;
        if (propertyName != null) {
            configs = ambariMetaInfo.getStackPropertiesByName(stackName, stackVersion, propertyName);
        } else {
            configs = ambariMetaInfo.getStackProperties(stackName, stackVersion);
        }
        for (org.apache.ambari.server.state.PropertyInfo property : configs) {
            response.add(property.convertToResponse());
        }
        return response;
    }

    @java.lang.Override
    public java.util.Set<org.apache.ambari.server.controller.StackConfigurationResponse> getStackConfigurations(java.util.Set<org.apache.ambari.server.controller.StackConfigurationRequest> requests) throws org.apache.ambari.server.AmbariException {
        java.util.Set<org.apache.ambari.server.controller.StackConfigurationResponse> response = new java.util.HashSet<>();
        for (org.apache.ambari.server.controller.StackConfigurationRequest request : requests) {
            java.lang.String stackName = request.getStackName();
            java.lang.String stackVersion = request.getStackVersion();
            java.lang.String serviceName = request.getServiceName();
            java.util.Set<org.apache.ambari.server.controller.StackConfigurationResponse> stackConfigurations = getStackConfigurations(request);
            for (org.apache.ambari.server.controller.StackConfigurationResponse stackConfigurationResponse : stackConfigurations) {
                stackConfigurationResponse.setStackName(stackName);
                stackConfigurationResponse.setStackVersion(stackVersion);
                stackConfigurationResponse.setServiceName(serviceName);
            }
            response.addAll(stackConfigurations);
        }
        return response;
    }

    private java.util.Set<org.apache.ambari.server.controller.StackConfigurationResponse> getStackConfigurations(org.apache.ambari.server.controller.StackConfigurationRequest request) throws org.apache.ambari.server.AmbariException {
        java.util.Set<org.apache.ambari.server.controller.StackConfigurationResponse> response = new java.util.HashSet<>();
        java.lang.String stackName = request.getStackName();
        java.lang.String stackVersion = request.getStackVersion();
        java.lang.String serviceName = request.getServiceName();
        java.lang.String propertyName = request.getPropertyName();
        java.util.Set<org.apache.ambari.server.state.PropertyInfo> properties;
        if (propertyName != null) {
            properties = ambariMetaInfo.getPropertiesByName(stackName, stackVersion, serviceName, propertyName);
        } else {
            properties = ambariMetaInfo.getServiceProperties(stackName, stackVersion, serviceName);
        }
        for (org.apache.ambari.server.state.PropertyInfo property : properties) {
            response.add(property.convertToResponse());
        }
        return response;
    }

    @java.lang.Override
    public java.util.Set<org.apache.ambari.server.controller.StackServiceComponentResponse> getStackComponents(java.util.Set<org.apache.ambari.server.controller.StackServiceComponentRequest> requests) throws org.apache.ambari.server.AmbariException {
        java.util.Set<org.apache.ambari.server.controller.StackServiceComponentResponse> response = new java.util.HashSet<>();
        for (org.apache.ambari.server.controller.StackServiceComponentRequest request : requests) {
            java.lang.String stackName = request.getStackName();
            java.lang.String stackVersion = request.getStackVersion();
            java.lang.String serviceName = request.getServiceName();
            try {
                java.util.Set<org.apache.ambari.server.controller.StackServiceComponentResponse> stackComponents = getStackComponents(request);
                for (org.apache.ambari.server.controller.StackServiceComponentResponse stackServiceComponentResponse : stackComponents) {
                    stackServiceComponentResponse.setStackName(stackName);
                    stackServiceComponentResponse.setStackVersion(stackVersion);
                    stackServiceComponentResponse.setServiceName(serviceName);
                }
                response.addAll(stackComponents);
            } catch (org.apache.ambari.server.StackAccessException e) {
                if (requests.size() == 1) {
                    throw e;
                }
            }
        }
        return response;
    }

    private java.util.Set<org.apache.ambari.server.controller.StackServiceComponentResponse> getStackComponents(org.apache.ambari.server.controller.StackServiceComponentRequest request) throws org.apache.ambari.server.AmbariException {
        java.util.Set<org.apache.ambari.server.controller.StackServiceComponentResponse> response;
        java.lang.String stackName = request.getStackName();
        java.lang.String stackVersion = request.getStackVersion();
        java.lang.String serviceName = request.getServiceName();
        java.lang.String componentName = request.getComponentName();
        if (componentName != null) {
            org.apache.ambari.server.state.ComponentInfo component = ambariMetaInfo.getComponent(stackName, stackVersion, serviceName, componentName);
            response = java.util.Collections.singleton(new org.apache.ambari.server.controller.StackServiceComponentResponse(component));
        } else {
            java.util.List<org.apache.ambari.server.state.ComponentInfo> components = ambariMetaInfo.getComponentsByService(stackName, stackVersion, serviceName);
            response = new java.util.HashSet<>();
            for (org.apache.ambari.server.state.ComponentInfo component : components) {
                response.add(new org.apache.ambari.server.controller.StackServiceComponentResponse(component));
            }
        }
        return response;
    }

    @java.lang.Override
    public java.util.Set<org.apache.ambari.server.controller.OperatingSystemResponse> getOperatingSystems(java.util.Set<org.apache.ambari.server.controller.OperatingSystemRequest> requests) throws org.apache.ambari.server.AmbariException {
        java.util.Set<org.apache.ambari.server.controller.OperatingSystemResponse> response = new java.util.HashSet<>();
        for (org.apache.ambari.server.controller.OperatingSystemRequest request : requests) {
            try {
                java.lang.String stackName = request.getStackName();
                java.lang.String stackVersion = request.getStackVersion();
                java.util.Set<org.apache.ambari.server.controller.OperatingSystemResponse> stackOperatingSystems = getOperatingSystems(request);
                for (org.apache.ambari.server.controller.OperatingSystemResponse operatingSystemResponse : stackOperatingSystems) {
                    if (operatingSystemResponse.getStackName() == null) {
                        operatingSystemResponse.setStackName(stackName);
                    }
                    if (operatingSystemResponse.getStackVersion() == null) {
                        operatingSystemResponse.setStackVersion(stackVersion);
                    }
                }
                response.addAll(stackOperatingSystems);
            } catch (org.apache.ambari.server.StackAccessException e) {
                if (requests.size() == 1) {
                    throw e;
                }
            }
        }
        return response;
    }

    private java.util.Set<org.apache.ambari.server.controller.OperatingSystemResponse> getOperatingSystems(org.apache.ambari.server.controller.OperatingSystemRequest request) throws org.apache.ambari.server.AmbariException {
        java.util.Set<org.apache.ambari.server.controller.OperatingSystemResponse> responses = new java.util.HashSet<>();
        java.lang.String stackName = request.getStackName();
        java.lang.String stackVersion = request.getStackVersion();
        java.lang.String osType = request.getOsType();
        java.lang.Long repositoryVersionId = request.getRepositoryVersionId();
        java.lang.String versionDefinitionId = request.getVersionDefinitionId();
        if ((null == repositoryVersionId) && (null != versionDefinitionId)) {
            if (org.apache.commons.lang.math.NumberUtils.isDigits(versionDefinitionId)) {
                repositoryVersionId = java.lang.Long.valueOf(versionDefinitionId);
            }
        }
        if (repositoryVersionId != null) {
            final org.apache.ambari.server.orm.entities.RepositoryVersionEntity repositoryVersion = repositoryVersionDAO.findByPK(repositoryVersionId);
            if (repositoryVersion != null) {
                for (org.apache.ambari.server.orm.entities.RepoOsEntity operatingSystem : repositoryVersion.getRepoOsEntities()) {
                    final org.apache.ambari.server.controller.OperatingSystemResponse response = new org.apache.ambari.server.controller.OperatingSystemResponse(operatingSystem.getFamily());
                    if (null != versionDefinitionId) {
                        response.setVersionDefinitionId(repositoryVersionId.toString());
                    } else {
                        response.setRepositoryVersionId(repositoryVersionId);
                    }
                    response.setStackName(repositoryVersion.getStackName());
                    response.setStackVersion(repositoryVersion.getStackVersion());
                    response.setAmbariManagedRepos(operatingSystem.isAmbariManaged());
                    responses.add(response);
                }
            }
        } else if (null != versionDefinitionId) {
            org.apache.ambari.server.state.repository.VersionDefinitionXml xml = ambariMetaInfo.getVersionDefinition(versionDefinitionId);
            if (null == xml) {
                throw new org.apache.ambari.server.AmbariException(java.lang.String.format("Version identified by %s does not exist", versionDefinitionId));
            }
            org.apache.ambari.server.state.StackId stackId = new org.apache.ambari.server.state.StackId(xml.release.stackId);
            for (org.apache.ambari.server.state.stack.RepositoryXml.Os os : xml.repositoryInfo.getOses()) {
                org.apache.ambari.server.controller.OperatingSystemResponse resp = new org.apache.ambari.server.controller.OperatingSystemResponse(os.getFamily());
                resp.setVersionDefinitionId(versionDefinitionId);
                resp.setStackName(stackId.getStackName());
                resp.setStackVersion(stackId.getStackVersion());
                responses.add(resp);
            }
        } else if (osType != null) {
            org.apache.ambari.server.state.OperatingSystemInfo operatingSystem = ambariMetaInfo.getOperatingSystem(stackName, stackVersion, osType);
            responses = java.util.Collections.singleton(operatingSystem.convertToResponse());
        } else {
            java.util.Set<org.apache.ambari.server.state.OperatingSystemInfo> operatingSystems = ambariMetaInfo.getOperatingSystems(stackName, stackVersion);
            for (org.apache.ambari.server.state.OperatingSystemInfo operatingSystem : operatingSystems) {
                responses.add(operatingSystem.convertToResponse());
            }
        }
        return responses;
    }

    @java.lang.Override
    public java.lang.String getAuthName() {
        return org.apache.ambari.server.security.authorization.AuthorizationHelper.getAuthenticatedName(configs.getAnonymousAuditName());
    }

    @java.lang.Override
    public int getAuthId() {
        return org.apache.ambari.server.security.authorization.AuthorizationHelper.getAuthenticatedId();
    }

    @java.lang.Override
    public java.util.Set<org.apache.ambari.server.controller.RootServiceResponse> getRootServices(java.util.Set<org.apache.ambari.server.controller.RootServiceRequest> requests) throws org.apache.ambari.server.AmbariException {
        java.util.Set<org.apache.ambari.server.controller.RootServiceResponse> response = new java.util.HashSet<>();
        for (org.apache.ambari.server.controller.RootServiceRequest request : requests) {
            try {
                response.addAll(getRootServices(request));
            } catch (org.apache.ambari.server.AmbariException e) {
                if (requests.size() == 1) {
                    throw e;
                }
            }
        }
        return response;
    }

    private java.util.Set<org.apache.ambari.server.controller.RootServiceResponse> getRootServices(org.apache.ambari.server.controller.RootServiceRequest request) throws org.apache.ambari.server.AmbariException {
        return rootServiceResponseFactory.getRootServices(request);
    }

    @java.lang.Override
    public java.util.Set<org.apache.ambari.server.controller.RootServiceComponentResponse> getRootServiceComponents(java.util.Set<org.apache.ambari.server.controller.RootServiceComponentRequest> requests) throws org.apache.ambari.server.AmbariException {
        java.util.Set<org.apache.ambari.server.controller.RootServiceComponentResponse> response = new java.util.HashSet<>();
        for (org.apache.ambari.server.controller.RootServiceComponentRequest request : requests) {
            try {
                java.util.Set<org.apache.ambari.server.controller.RootServiceComponentResponse> rootServiceComponents = getRootServiceComponents(request);
                response.addAll(rootServiceComponents);
            } catch (org.apache.ambari.server.AmbariException e) {
                if (requests.size() == 1) {
                    throw e;
                }
            }
        }
        return response;
    }

    private java.util.Set<org.apache.ambari.server.controller.RootServiceComponentResponse> getRootServiceComponents(org.apache.ambari.server.controller.RootServiceComponentRequest request) throws org.apache.ambari.server.AmbariException {
        return rootServiceResponseFactory.getRootServiceComponents(request);
    }

    @java.lang.Override
    public org.apache.ambari.server.state.Clusters getClusters() {
        return clusters;
    }

    @java.lang.Override
    public org.apache.ambari.server.state.ConfigHelper getConfigHelper() {
        return configHelper;
    }

    @java.lang.Override
    public org.apache.ambari.server.api.services.AmbariMetaInfo getAmbariMetaInfo() {
        return ambariMetaInfo;
    }

    @java.lang.Override
    public org.apache.ambari.server.state.ServiceComponentFactory getServiceComponentFactory() {
        return serviceComponentFactory;
    }

    @java.lang.Override
    public org.apache.ambari.server.state.configgroup.ConfigGroupFactory getConfigGroupFactory() {
        return configGroupFactory;
    }

    @java.lang.Override
    public org.apache.ambari.server.stageplanner.RoleGraphFactory getRoleGraphFactory() {
        return roleGraphFactory;
    }

    @java.lang.Override
    public org.apache.ambari.server.controller.AbstractRootServiceResponseFactory getRootServiceResponseFactory() {
        return rootServiceResponseFactory;
    }

    @java.lang.Override
    public org.apache.ambari.server.actionmanager.ActionManager getActionManager() {
        return actionManager;
    }

    @java.lang.Override
    public java.lang.String getJdkResourceUrl() {
        return jdkResourceUrl;
    }

    @java.lang.Override
    public java.lang.String getJavaHome() {
        return javaHome;
    }

    @java.lang.Override
    public java.lang.String getJDKName() {
        return jdkName;
    }

    @java.lang.Override
    public java.lang.String getJCEName() {
        return jceName;
    }

    @java.lang.Override
    public java.lang.String getServerDB() {
        return serverDB;
    }

    @java.lang.Override
    public java.lang.String getOjdbcUrl() {
        return ojdbcUrl;
    }

    @java.lang.Override
    public java.lang.String getMysqljdbcUrl() {
        return mysqljdbcUrl;
    }

    @java.lang.Override
    public java.util.Map<java.lang.String, java.lang.String> getRcaParameters() {
        java.lang.String hostName = org.apache.ambari.server.utils.StageUtils.getHostName();
        java.lang.String url = configs.getRcaDatabaseUrl();
        if (url.contains(org.apache.ambari.server.configuration.Configuration.HOSTNAME_MACRO)) {
            url = url.replace(org.apache.ambari.server.configuration.Configuration.HOSTNAME_MACRO, hostsMap.getHostMap(hostName));
        }
        java.util.Map<java.lang.String, java.lang.String> rcaParameters = new java.util.HashMap<>();
        rcaParameters.put(org.apache.ambari.server.agent.ExecutionCommand.KeyNames.AMBARI_DB_RCA_URL, url);
        rcaParameters.put(org.apache.ambari.server.agent.ExecutionCommand.KeyNames.AMBARI_DB_RCA_DRIVER, configs.getRcaDatabaseDriver());
        rcaParameters.put(org.apache.ambari.server.agent.ExecutionCommand.KeyNames.AMBARI_DB_RCA_USERNAME, configs.getRcaDatabaseUser());
        rcaParameters.put(org.apache.ambari.server.agent.ExecutionCommand.KeyNames.AMBARI_DB_RCA_PASSWORD, configs.getRcaDatabasePassword());
        return rcaParameters;
    }

    @java.lang.Override
    public boolean checkLdapConfigured() {
        return ldapDataPopulator.isLdapEnabled();
    }

    @java.lang.Override
    public org.apache.ambari.server.security.ldap.LdapSyncDto getLdapSyncInfo() throws org.apache.ambari.server.AmbariException {
        return ldapDataPopulator.getLdapSyncInfo();
    }

    @java.lang.Override
    public boolean isLdapSyncInProgress() {
        return ldapSyncInProgress;
    }

    @java.lang.Override
    public synchronized org.apache.ambari.server.security.ldap.LdapBatchDto synchronizeLdapUsersAndGroups(org.apache.ambari.server.controller.LdapSyncRequest userRequest, org.apache.ambari.server.controller.LdapSyncRequest groupRequest) throws org.apache.ambari.server.AmbariException {
        ldapSyncInProgress = true;
        try {
            final org.apache.ambari.server.security.ldap.LdapBatchDto batchInfo = new org.apache.ambari.server.security.ldap.LdapBatchDto();
            boolean postProcessExistingUsers = false;
            boolean postProcessExistingUsersInGroups = false;
            if (userRequest != null) {
                postProcessExistingUsers = userRequest.getPostProcessExistingUsers();
                if (postProcessExistingUsers && (!configs.isUserHookEnabled())) {
                    org.apache.ambari.server.controller.AmbariManagementControllerImpl.LOG.warn("Post processing existing users is requested while processing users; however, the user post creation hook is turned off.");
                    postProcessExistingUsers = false;
                }
                switch (userRequest.getType()) {
                    case ALL :
                        ldapDataPopulator.synchronizeAllLdapUsers(batchInfo, postProcessExistingUsers);
                        break;
                    case EXISTING :
                        ldapDataPopulator.synchronizeExistingLdapUsers(batchInfo, postProcessExistingUsers);
                        break;
                    case SPECIFIC :
                        ldapDataPopulator.synchronizeLdapUsers(userRequest.getPrincipalNames(), batchInfo, postProcessExistingUsers);
                        break;
                }
            }
            if (groupRequest != null) {
                postProcessExistingUsersInGroups = groupRequest.getPostProcessExistingUsers();
                if (postProcessExistingUsersInGroups && (!configs.isUserHookEnabled())) {
                    org.apache.ambari.server.controller.AmbariManagementControllerImpl.LOG.warn("Post processing existing users is requested while processing groups; however, the user post creation hook is turned off.");
                    postProcessExistingUsersInGroups = false;
                }
                switch (groupRequest.getType()) {
                    case ALL :
                        ldapDataPopulator.synchronizeAllLdapGroups(batchInfo, postProcessExistingUsersInGroups);
                        break;
                    case EXISTING :
                        ldapDataPopulator.synchronizeExistingLdapGroups(batchInfo, postProcessExistingUsersInGroups);
                        break;
                    case SPECIFIC :
                        ldapDataPopulator.synchronizeLdapGroups(groupRequest.getPrincipalNames(), batchInfo, postProcessExistingUsersInGroups);
                        break;
                }
            }
            users.processLdapSync(batchInfo);
            if (postProcessExistingUsers || postProcessExistingUsersInGroups) {
                java.util.Set<org.apache.ambari.server.security.ldap.LdapUserDto> ignoredUsers = batchInfo.getUsersIgnored();
                if (org.apache.commons.collections.CollectionUtils.isNotEmpty(ignoredUsers)) {
                    java.util.Map<java.lang.String, java.util.Set<java.lang.String>> userGroupsMap = new java.util.HashMap<>();
                    for (org.apache.ambari.server.security.ldap.LdapUserDto ignoredUser : ignoredUsers) {
                        userGroupsMap.put(ignoredUser.getUserName(), java.util.Collections.emptySet());
                    }
                    users.executeUserHook(userGroupsMap);
                }
            }
            return batchInfo;
        } finally {
            ldapSyncInProgress = false;
        }
    }

    @java.lang.SuppressWarnings("unchecked")
    @java.lang.Override
    public void initializeWidgetsAndLayouts(org.apache.ambari.server.state.Cluster cluster, org.apache.ambari.server.state.Service service) throws org.apache.ambari.server.AmbariException {
        java.lang.reflect.Type widgetLayoutType = new com.google.gson.reflect.TypeToken<java.util.Map<java.lang.String, java.util.List<org.apache.ambari.server.state.stack.WidgetLayout>>>() {}.getType();
        java.util.Set<java.io.File> widgetDescriptorFiles = new java.util.HashSet<>();
        if (null != service) {
            org.apache.ambari.server.state.ServiceInfo serviceInfo = ambariMetaInfo.getService(service);
            java.io.File widgetDescriptorFile = serviceInfo.getWidgetsDescriptorFile();
            if ((widgetDescriptorFile != null) && widgetDescriptorFile.exists()) {
                widgetDescriptorFiles.add(widgetDescriptorFile);
            }
        } else {
            java.io.File commonWidgetsFile = ambariMetaInfo.getCommonWidgetsDescriptorFile();
            if ((commonWidgetsFile != null) && commonWidgetsFile.exists()) {
                widgetDescriptorFiles.add(commonWidgetsFile);
            } else {
                org.apache.ambari.server.controller.AmbariManagementControllerImpl.LOG.warn("Common widgets file with path {%s} doesn't exist. No cluster widgets will be created.", commonWidgetsFile);
            }
        }
        for (java.io.File widgetDescriptorFile : widgetDescriptorFiles) {
            java.util.Map<java.lang.String, java.lang.Object> widgetDescriptor = null;
            try {
                widgetDescriptor = gson.fromJson(new java.io.FileReader(widgetDescriptorFile), widgetLayoutType);
                for (java.lang.Object artifact : widgetDescriptor.values()) {
                    java.util.List<org.apache.ambari.server.state.stack.WidgetLayout> widgetLayouts = ((java.util.List<org.apache.ambari.server.state.stack.WidgetLayout>) (artifact));
                    createWidgetsAndLayouts(cluster, widgetLayouts);
                }
            } catch (java.lang.Exception ex) {
                java.lang.String msg = "Error loading widgets from file: " + widgetDescriptorFile;
                org.apache.ambari.server.controller.AmbariManagementControllerImpl.LOG.error(msg, ex);
                throw new org.apache.ambari.server.AmbariException(msg);
            }
        }
    }

    private org.apache.ambari.server.orm.entities.WidgetEntity addIfNotExistsWidgetEntity(org.apache.ambari.server.state.stack.WidgetLayoutInfo layoutInfo, org.apache.ambari.server.orm.entities.ClusterEntity clusterEntity, java.lang.String user, long createTime) {
        java.util.List<org.apache.ambari.server.orm.entities.WidgetEntity> createdEntities = widgetDAO.findByName(clusterEntity.getClusterId(), layoutInfo.getWidgetName(), user, layoutInfo.getDefaultSectionName());
        if ((createdEntities == null) || createdEntities.isEmpty()) {
            org.apache.ambari.server.orm.entities.WidgetEntity widgetEntity = new org.apache.ambari.server.orm.entities.WidgetEntity();
            widgetEntity.setClusterId(clusterEntity.getClusterId());
            widgetEntity.setClusterEntity(clusterEntity);
            widgetEntity.setScope(org.apache.ambari.server.controller.internal.WidgetResourceProvider.SCOPE.CLUSTER.name());
            widgetEntity.setWidgetName(layoutInfo.getWidgetName());
            widgetEntity.setDefaultSectionName(layoutInfo.getDefaultSectionName());
            widgetEntity.setAuthor(user);
            widgetEntity.setDescription(layoutInfo.getDescription());
            widgetEntity.setTimeCreated(createTime);
            widgetEntity.setWidgetType(layoutInfo.getType());
            widgetEntity.setMetrics(gson.toJson(layoutInfo.getMetricsInfo()));
            widgetEntity.setProperties(gson.toJson(layoutInfo.getProperties()));
            widgetEntity.setWidgetValues(gson.toJson(layoutInfo.getValues()));
            widgetEntity.setListWidgetLayoutUserWidgetEntity(new java.util.LinkedList<>());
            org.apache.ambari.server.controller.AmbariManagementControllerImpl.LOG.info(((((("Creating cluster widget with: name = " + layoutInfo.getWidgetName()) + ", type = ") + layoutInfo.getType()) + ", ") + "cluster = ") + clusterEntity.getClusterName());
            if (!layoutInfo.isVisible()) {
                widgetDAO.create(widgetEntity);
            }
            return widgetEntity;
        } else {
            org.apache.ambari.server.controller.AmbariManagementControllerImpl.LOG.warn((((((("Skip creating widget from stack artifact since one or more " + "already exits with name = ") + layoutInfo.getWidgetName()) + ", ") + "clusterId = ") + clusterEntity.getClusterId()) + ", user = ") + user);
        }
        return null;
    }

    @com.google.inject.persist.Transactional
    void createWidgetsAndLayouts(org.apache.ambari.server.state.Cluster cluster, java.util.List<org.apache.ambari.server.state.stack.WidgetLayout> widgetLayouts) {
        java.lang.String user = "ambari";
        java.lang.Long clusterId = cluster.getClusterId();
        org.apache.ambari.server.orm.entities.ClusterEntity clusterEntity = clusterDAO.findById(clusterId);
        if (clusterEntity == null) {
            return;
        }
        java.lang.Long now = java.lang.System.currentTimeMillis();
        if (widgetLayouts != null) {
            for (org.apache.ambari.server.state.stack.WidgetLayout widgetLayout : widgetLayouts) {
                java.util.List<org.apache.ambari.server.orm.entities.WidgetLayoutEntity> existingEntities = widgetLayoutDAO.findByName(clusterId, widgetLayout.getLayoutName(), user);
                if ((existingEntities == null) || existingEntities.isEmpty()) {
                    org.apache.ambari.server.orm.entities.WidgetLayoutEntity layoutEntity = new org.apache.ambari.server.orm.entities.WidgetLayoutEntity();
                    layoutEntity.setClusterEntity(clusterEntity);
                    layoutEntity.setClusterId(clusterId);
                    layoutEntity.setLayoutName(widgetLayout.getLayoutName());
                    layoutEntity.setDisplayName(widgetLayout.getDisplayName());
                    layoutEntity.setSectionName(widgetLayout.getSectionName());
                    layoutEntity.setScope(org.apache.ambari.server.controller.internal.WidgetLayoutResourceProvider.SCOPE.CLUSTER.name());
                    layoutEntity.setUserName(user);
                    java.util.List<org.apache.ambari.server.orm.entities.WidgetLayoutUserWidgetEntity> widgetLayoutUserWidgetEntityList = new java.util.LinkedList<>();
                    int order = 0;
                    for (org.apache.ambari.server.state.stack.WidgetLayoutInfo layoutInfo : widgetLayout.getWidgetLayoutInfoList()) {
                        if (layoutInfo.getDefaultSectionName() == null) {
                            layoutInfo.setDefaultSectionName(layoutEntity.getSectionName());
                        }
                        org.apache.ambari.server.orm.entities.WidgetEntity widgetEntity = addIfNotExistsWidgetEntity(layoutInfo, clusterEntity, user, now);
                        if ((widgetEntity != null) && layoutInfo.isVisible()) {
                            org.apache.ambari.server.orm.entities.WidgetLayoutUserWidgetEntity widgetLayoutUserWidgetEntity = new org.apache.ambari.server.orm.entities.WidgetLayoutUserWidgetEntity();
                            widgetLayoutUserWidgetEntity.setWidget(widgetEntity);
                            widgetLayoutUserWidgetEntity.setWidgetOrder(order++);
                            widgetLayoutUserWidgetEntity.setWidgetLayout(layoutEntity);
                            widgetLayoutUserWidgetEntityList.add(widgetLayoutUserWidgetEntity);
                            widgetEntity.getListWidgetLayoutUserWidgetEntity().add(widgetLayoutUserWidgetEntity);
                        }
                    }
                    layoutEntity.setListWidgetLayoutUserWidgetEntity(widgetLayoutUserWidgetEntityList);
                    widgetLayoutDAO.createWithFlush(layoutEntity);
                } else if (existingEntities.size() > 1) {
                    org.apache.ambari.server.controller.AmbariManagementControllerImpl.LOG.warn((((((("Skip updating layout since multiple widget layouts " + "found with: name = ") + widgetLayout.getLayoutName()) + ", ") + "user = ") + user) + ", cluster = ") + cluster.getClusterName());
                } else {
                    org.apache.ambari.server.orm.entities.WidgetLayoutEntity existingLayoutEntity = existingEntities.iterator().next();
                    existingLayoutEntity.setSectionName(widgetLayout.getSectionName());
                    existingLayoutEntity.setDisplayName(widgetLayout.getDisplayName());
                    java.util.List<org.apache.ambari.server.orm.entities.WidgetLayoutUserWidgetEntity> layoutUserWidgetEntities = existingLayoutEntity.getListWidgetLayoutUserWidgetEntity();
                    if (layoutUserWidgetEntities == null) {
                        layoutUserWidgetEntities = new java.util.LinkedList<>();
                        existingLayoutEntity.setListWidgetLayoutUserWidgetEntity(layoutUserWidgetEntities);
                    }
                    int order = layoutUserWidgetEntities.size() - 1;
                    java.util.List<org.apache.ambari.server.state.stack.WidgetLayoutInfo> layoutInfoList = widgetLayout.getWidgetLayoutInfoList();
                    if ((layoutInfoList != null) && (!layoutInfoList.isEmpty())) {
                        for (org.apache.ambari.server.state.stack.WidgetLayoutInfo layoutInfo : layoutInfoList) {
                            org.apache.ambari.server.orm.entities.WidgetEntity widgetEntity = addIfNotExistsWidgetEntity(layoutInfo, clusterEntity, user, now);
                            if ((widgetEntity != null) && layoutInfo.isVisible()) {
                                org.apache.ambari.server.orm.entities.WidgetLayoutUserWidgetEntity widgetLayoutUserWidgetEntity = new org.apache.ambari.server.orm.entities.WidgetLayoutUserWidgetEntity();
                                widgetLayoutUserWidgetEntity.setWidget(widgetEntity);
                                widgetLayoutUserWidgetEntity.setWidgetOrder(order++);
                                widgetLayoutUserWidgetEntity.setWidgetLayout(existingLayoutEntity);
                                layoutUserWidgetEntities.add(widgetLayoutUserWidgetEntity);
                                widgetEntity.getListWidgetLayoutUserWidgetEntity().add(widgetLayoutUserWidgetEntity);
                            }
                        }
                    }
                    widgetLayoutDAO.mergeWithFlush(existingLayoutEntity);
                }
            }
        }
    }

    @java.lang.Override
    public org.apache.ambari.server.controller.metrics.timeline.cache.TimelineMetricCacheProvider getTimelineMetricCacheProvider() {
        return injector.getInstance(org.apache.ambari.server.controller.metrics.timeline.cache.TimelineMetricCacheProvider.class);
    }

    @java.lang.Override
    public org.apache.ambari.server.controller.metrics.MetricPropertyProviderFactory getMetricPropertyProviderFactory() {
        return injector.getInstance(org.apache.ambari.server.controller.metrics.MetricPropertyProviderFactory.class);
    }

    @java.lang.Override
    public org.apache.ambari.server.controller.logging.LoggingSearchPropertyProvider getLoggingSearchPropertyProvider() {
        return injector.getInstance(org.apache.ambari.server.controller.logging.LoggingSearchPropertyProvider.class);
    }

    @java.lang.Override
    public org.apache.ambari.server.api.services.LoggingService getLoggingService(java.lang.String clusterName) {
        org.apache.ambari.server.api.services.LoggingService loggingService = new org.apache.ambari.server.api.services.LoggingService(clusterName);
        injector.injectMembers(loggingService);
        return loggingService;
    }

    @java.lang.Override
    public org.apache.ambari.server.events.publishers.AmbariEventPublisher getAmbariEventPublisher() {
        return injector.getInstance(org.apache.ambari.server.events.publishers.AmbariEventPublisher.class);
    }

    @java.lang.Override
    public org.apache.ambari.server.controller.KerberosHelper getKerberosHelper() {
        return kerberosHelper;
    }

    @java.lang.Override
    public org.apache.ambari.server.security.encryption.CredentialStoreService getCredentialStoreService() {
        return credentialStoreService;
    }

    public java.util.Map<java.lang.String, java.lang.String> getCredentialStoreServiceProperties() {
        java.util.Map<java.lang.String, java.lang.String> properties = new java.util.HashMap<>();
        properties.put("storage.persistent", java.lang.String.valueOf(credentialStoreService.isInitialized(org.apache.ambari.server.security.encryption.CredentialStoreType.PERSISTED)));
        properties.put("storage.temporary", java.lang.String.valueOf(credentialStoreService.isInitialized(org.apache.ambari.server.security.encryption.CredentialStoreType.TEMPORARY)));
        return properties;
    }

    @java.lang.Override
    public org.apache.ambari.server.controller.metrics.MetricsCollectorHAManager getMetricsCollectorHAManager() {
        return injector.getInstance(org.apache.ambari.server.controller.metrics.MetricsCollectorHAManager.class);
    }

    protected void validateAuthorizationToUpdateServiceUsersAndGroups(org.apache.ambari.server.state.Cluster cluster, java.lang.String configType, java.util.Map<java.lang.String, java.lang.String[]> propertyChanges) throws org.apache.ambari.server.security.authorization.AuthorizationException {
        if ((propertyChanges != null) && (!propertyChanges.isEmpty())) {
            if (!org.apache.ambari.server.security.authorization.AuthorizationHelper.isAuthorized(org.apache.ambari.server.security.authorization.ResourceType.CLUSTER, cluster.getResourceId(), org.apache.ambari.server.security.authorization.RoleAuthorization.SERVICE_SET_SERVICE_USERS_GROUPS)) {
                java.util.Map<org.apache.ambari.server.state.PropertyInfo.PropertyType, java.util.Set<java.lang.String>> propertyTypes = cluster.getConfigPropertiesTypes(configType);
                java.util.Set<java.lang.String> propertiesToCheck = new java.util.HashSet<>();
                java.util.Set<java.lang.String> userProperties = propertyTypes.get(org.apache.ambari.server.state.PropertyInfo.PropertyType.USER);
                if (userProperties != null) {
                    propertiesToCheck.addAll(userProperties);
                }
                java.util.Set<java.lang.String> groupProperties = propertyTypes.get(org.apache.ambari.server.state.PropertyInfo.PropertyType.GROUP);
                if (groupProperties != null) {
                    propertiesToCheck.addAll(groupProperties);
                }
                for (java.lang.String propertyName : propertiesToCheck) {
                    java.lang.String[] values = propertyChanges.get(propertyName);
                    if (values != null) {
                        java.lang.String existingValue = values[0];
                        java.lang.String requestedValue = values[1];
                        if (existingValue == null ? requestedValue != null : !existingValue.equals(requestedValue)) {
                            throw new org.apache.ambari.server.security.authorization.AuthorizationException("The authenticated user is not authorized to set service user and groups");
                        }
                    }
                }
            }
        }
    }

    protected void validateAuthorizationToManageServiceAutoStartConfiguration(org.apache.ambari.server.state.Cluster cluster, java.lang.String configType, java.util.Map<java.lang.String, java.lang.String[]> propertyChanges) throws org.apache.ambari.server.security.authorization.AuthorizationException {
        if (!org.apache.ambari.server.security.authorization.AuthorizationHelper.isAuthorized(org.apache.ambari.server.security.authorization.ResourceType.CLUSTER, cluster.getResourceId(), org.apache.ambari.server.security.authorization.RoleAuthorization.CLUSTER_MANAGE_AUTO_START)) {
            if ("cluster-env".equals(configType) && propertyChanges.containsKey("recovery_enabled")) {
                throw new org.apache.ambari.server.security.authorization.AuthorizationException("The authenticated user is not authorized to set service user and groups");
            }
        }
    }

    private void validateAuthorizationToModifyConfigurations(org.apache.ambari.server.state.Cluster cluster, java.lang.String configType, java.util.Map<java.lang.String, java.lang.String[]> propertyChanges, java.util.Map<java.lang.String, java.util.Set<java.lang.String>> changesToIgnore, boolean isServiceConfiguration) throws org.apache.ambari.server.security.authorization.AuthorizationException {
        if ((propertyChanges != null) && (!propertyChanges.isEmpty())) {
            boolean isAuthorized = (isServiceConfiguration) ? org.apache.ambari.server.security.authorization.AuthorizationHelper.isAuthorized(org.apache.ambari.server.security.authorization.ResourceType.CLUSTER, cluster.getResourceId(), org.apache.ambari.server.security.authorization.RoleAuthorization.SERVICE_MODIFY_CONFIGS) : org.apache.ambari.server.security.authorization.AuthorizationHelper.isAuthorized(org.apache.ambari.server.security.authorization.ResourceType.CLUSTER, cluster.getResourceId(), org.apache.ambari.server.security.authorization.RoleAuthorization.CLUSTER_MODIFY_CONFIGS);
            if ((!isAuthorized) && (changesToIgnore != null)) {
                java.util.Set<java.lang.String> relevantChangesToIgnore = changesToIgnore.get(configType);
                java.util.Map<java.lang.String, java.lang.String[]> relevantPropertyChanges;
                if (relevantChangesToIgnore == null) {
                    relevantPropertyChanges = propertyChanges;
                } else {
                    relevantPropertyChanges = new java.util.HashMap<>(propertyChanges);
                    for (java.lang.String propertyName : relevantChangesToIgnore) {
                        relevantPropertyChanges.remove(propertyName);
                    }
                }
                if (relevantPropertyChanges.size() == 0) {
                    return;
                }
            }
            if (!isAuthorized) {
                throw new org.apache.ambari.server.security.authorization.AuthorizationException(java.lang.String.format("The authenticated user does not have authorization to modify %s configurations", isServiceConfiguration ? "service" : "cluster"));
            }
        }
    }

    @java.lang.Override
    public void deleteExtensionLink(org.apache.ambari.server.controller.ExtensionLinkRequest request) throws org.apache.ambari.server.AmbariException {
        if (request.getLinkId() == null) {
            throw new java.lang.IllegalArgumentException("Link ID should be provided");
        }
        org.apache.ambari.server.orm.entities.ExtensionLinkEntity linkEntity = null;
        try {
            linkEntity = linkDAO.findById(java.lang.Long.parseLong(request.getLinkId()));
        } catch (javax.persistence.RollbackException e) {
            throw new org.apache.ambari.server.AmbariException(("Unable to find extension link" + ", linkId=") + request.getLinkId(), e);
        }
        org.apache.ambari.server.state.StackInfo stackInfo = ambariMetaInfo.getStack(linkEntity.getStack().getStackName(), linkEntity.getStack().getStackVersion());
        if (stackInfo == null) {
            throw new org.apache.ambari.server.StackAccessException((("stackName=" + linkEntity.getStack().getStackName()) + ", stackVersion=") + linkEntity.getStack().getStackVersion());
        }
        org.apache.ambari.server.state.ExtensionInfo extensionInfo = ambariMetaInfo.getExtension(linkEntity.getExtension().getExtensionName(), linkEntity.getExtension().getExtensionVersion());
        if (extensionInfo == null) {
            throw new org.apache.ambari.server.StackAccessException((("extensionName=" + linkEntity.getExtension().getExtensionName()) + ", extensionVersion=") + linkEntity.getExtension().getExtensionVersion());
        }
        org.apache.ambari.server.stack.ExtensionHelper.validateDeleteLink(getClusters(), stackInfo, extensionInfo);
        ambariMetaInfo.getStackManager().unlinkStackAndExtension(stackInfo, extensionInfo);
        try {
            linkDAO.remove(linkEntity);
        } catch (javax.persistence.RollbackException e) {
            throw new org.apache.ambari.server.AmbariException(((((((((("Unable to delete extension link" + ", linkId=") + request.getLinkId()) + ", stackName=") + request.getStackName()) + ", stackVersion=") + request.getStackVersion()) + ", extensionName=") + request.getExtensionName()) + ", extensionVersion=") + request.getExtensionVersion(), e);
        }
    }

    @java.lang.Override
    public void createExtensionLink(org.apache.ambari.server.controller.ExtensionLinkRequest request) throws org.apache.ambari.server.AmbariException {
        if (((org.apache.commons.lang.StringUtils.isBlank(request.getStackName()) || org.apache.commons.lang.StringUtils.isBlank(request.getStackVersion())) || org.apache.commons.lang.StringUtils.isBlank(request.getExtensionName())) || org.apache.commons.lang.StringUtils.isBlank(request.getExtensionVersion())) {
            throw new java.lang.IllegalArgumentException("Stack name, stack version, extension name and extension version should be provided");
        }
        org.apache.ambari.server.state.StackInfo stackInfo = ambariMetaInfo.getStack(request.getStackName(), request.getStackVersion());
        if (stackInfo == null) {
            throw new org.apache.ambari.server.StackAccessException((("stackName=" + request.getStackName()) + ", stackVersion=") + request.getStackVersion());
        }
        org.apache.ambari.server.state.ExtensionInfo extensionInfo = ambariMetaInfo.getExtension(request.getExtensionName(), request.getExtensionVersion());
        if (extensionInfo == null) {
            throw new org.apache.ambari.server.StackAccessException((("extensionName=" + request.getExtensionName()) + ", extensionVersion=") + request.getExtensionVersion());
        }
        helper.createExtensionLink(ambariMetaInfo.getStackManager(), stackInfo, extensionInfo);
    }

    @java.lang.Override
    public void updateExtensionLink(org.apache.ambari.server.controller.ExtensionLinkRequest request) throws org.apache.ambari.server.AmbariException {
        if (request.getLinkId() == null) {
            throw new org.apache.ambari.server.AmbariException("Link ID should be provided");
        }
        org.apache.ambari.server.orm.entities.ExtensionLinkEntity linkEntity = null;
        try {
            linkEntity = linkDAO.findById(java.lang.Long.parseLong(request.getLinkId()));
        } catch (javax.persistence.RollbackException e) {
            throw new org.apache.ambari.server.AmbariException(("Unable to find extension link" + ", linkId=") + request.getLinkId(), e);
        }
        updateExtensionLink(linkEntity, request);
    }

    @java.lang.Override
    public void updateExtensionLink(org.apache.ambari.server.orm.entities.ExtensionLinkEntity oldLinkEntity, org.apache.ambari.server.controller.ExtensionLinkRequest newLinkRequest) throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.server.state.StackInfo stackInfo = ambariMetaInfo.getStack(oldLinkEntity.getStack().getStackName(), oldLinkEntity.getStack().getStackVersion());
        if (stackInfo == null) {
            throw new org.apache.ambari.server.StackAccessException(java.lang.String.format("stackName=%s, stackVersion=%s", oldLinkEntity.getStack().getStackName(), oldLinkEntity.getStack().getStackVersion()));
        }
        if ((newLinkRequest.getExtensionName() == null) || (newLinkRequest.getExtensionVersion() == null)) {
            throw new org.apache.ambari.server.AmbariException(java.lang.String.format("Invalid extension name or version: %s/%s", newLinkRequest.getExtensionName(), newLinkRequest.getExtensionVersion()));
        }
        if (!newLinkRequest.getExtensionName().equals(oldLinkEntity.getExtension().getExtensionName())) {
            throw new org.apache.ambari.server.AmbariException(java.lang.String.format("Update is not allowed to switch the extension name, only the version.  Old name/new name: %s/%s", oldLinkEntity.getExtension().getExtensionName(), newLinkRequest.getExtensionName()));
        }
        org.apache.ambari.server.state.ExtensionInfo oldExtensionInfo = ambariMetaInfo.getExtension(oldLinkEntity.getExtension().getExtensionName(), oldLinkEntity.getExtension().getExtensionVersion());
        org.apache.ambari.server.state.ExtensionInfo newExtensionInfo = ambariMetaInfo.getExtension(newLinkRequest.getExtensionName(), newLinkRequest.getExtensionVersion());
        if (oldExtensionInfo == null) {
            throw new org.apache.ambari.server.StackAccessException(java.lang.String.format("Old extensionName=%s, extensionVersion=%s", oldLinkEntity.getExtension().getExtensionName(), oldLinkEntity.getExtension().getExtensionVersion()));
        }
        if (newExtensionInfo == null) {
            throw new org.apache.ambari.server.StackAccessException(java.lang.String.format("New extensionName=%s, extensionVersion=%s", newLinkRequest.getExtensionName(), newLinkRequest.getExtensionVersion()));
        }
        helper.updateExtensionLink(ambariMetaInfo.getStackManager(), oldLinkEntity, stackInfo, oldExtensionInfo, newExtensionInfo);
    }

    @java.lang.Override
    public org.apache.ambari.server.state.quicklinksprofile.QuickLinkVisibilityController getQuicklinkVisibilityController() {
        org.apache.ambari.server.orm.entities.SettingEntity entity = settingDAO.findByName(org.apache.ambari.server.state.quicklinksprofile.QuickLinksProfile.SETTING_NAME_QUICKLINKS_PROFILE);
        java.lang.String quickLinkProfileJson = (null != entity) ? entity.getContent() : null;
        return org.apache.ambari.server.state.quicklinksprofile.QuickLinkVisibilityControllerFactory.get(quickLinkProfileJson);
    }

    public org.apache.ambari.server.events.MetadataUpdateEvent getClustersMetadata() throws org.apache.ambari.server.AmbariException {
        java.util.TreeMap<java.lang.String, org.apache.ambari.server.agent.stomp.dto.MetadataCluster> metadataClusters = new java.util.TreeMap<>();
        for (org.apache.ambari.server.state.Cluster cl : clusters.getClusters().values()) {
            org.apache.ambari.server.state.StackId stackId = cl.getDesiredStackVersion();
            org.apache.ambari.server.state.SecurityType securityType = cl.getSecurityType();
            org.apache.ambari.server.agent.stomp.dto.MetadataCluster metadataCluster = new org.apache.ambari.server.agent.stomp.dto.MetadataCluster(securityType, getMetadataServiceLevelParams(cl), true, getMetadataClusterLevelParams(cl, stackId), null);
            metadataClusters.put(java.lang.Long.toString(cl.getClusterId()), metadataCluster);
        }
        org.apache.ambari.server.events.MetadataUpdateEvent metadataUpdateEvent = new org.apache.ambari.server.events.MetadataUpdateEvent(metadataClusters, getMetadataAmbariLevelParams(), getMetadataAgentConfigs(), org.apache.ambari.server.events.UpdateEventType.CREATE);
        return metadataUpdateEvent;
    }

    public org.apache.ambari.server.events.MetadataUpdateEvent getClusterMetadata(org.apache.ambari.server.state.Cluster cl) throws org.apache.ambari.server.AmbariException {
        final java.util.TreeMap<java.lang.String, org.apache.ambari.server.agent.stomp.dto.MetadataCluster> metadataClusters = new java.util.TreeMap<>();
        org.apache.ambari.server.agent.stomp.dto.MetadataCluster metadataCluster = new org.apache.ambari.server.agent.stomp.dto.MetadataCluster(cl.getSecurityType(), getMetadataServiceLevelParams(cl), true, getMetadataClusterLevelParams(cl, cl.getDesiredStackVersion()), null);
        metadataClusters.put(java.lang.Long.toString(cl.getClusterId()), metadataCluster);
        return new org.apache.ambari.server.events.MetadataUpdateEvent(metadataClusters, null, getMetadataAgentConfigs(), org.apache.ambari.server.events.UpdateEventType.UPDATE);
    }

    @java.lang.Override
    public org.apache.ambari.server.events.MetadataUpdateEvent getClusterMetadataOnConfigsUpdate(org.apache.ambari.server.state.Cluster cl) throws org.apache.ambari.server.AmbariException {
        final java.util.TreeMap<java.lang.String, org.apache.ambari.server.agent.stomp.dto.MetadataCluster> metadataClusters = new java.util.TreeMap<>();
        metadataClusters.put(java.lang.Long.toString(cl.getClusterId()), org.apache.ambari.server.agent.stomp.dto.MetadataCluster.clusterLevelParamsMetadataCluster(null, getMetadataClusterLevelParams(cl, cl.getDesiredStackVersion())));
        return new org.apache.ambari.server.events.MetadataUpdateEvent(metadataClusters, null, getMetadataAgentConfigs(), org.apache.ambari.server.events.UpdateEventType.UPDATE);
    }

    public org.apache.ambari.server.events.MetadataUpdateEvent getClusterMetadataOnRepoUpdate(org.apache.ambari.server.state.Cluster cl) throws org.apache.ambari.server.AmbariException {
        java.util.TreeMap<java.lang.String, org.apache.ambari.server.agent.stomp.dto.MetadataCluster> metadataClusters = new java.util.TreeMap<>();
        metadataClusters.put(java.lang.Long.toString(cl.getClusterId()), org.apache.ambari.server.agent.stomp.dto.MetadataCluster.serviceLevelParamsMetadataCluster(null, getMetadataServiceLevelParams(cl), true));
        return new org.apache.ambari.server.events.MetadataUpdateEvent(metadataClusters, null, getMetadataAgentConfigs(), org.apache.ambari.server.events.UpdateEventType.UPDATE);
    }

    public org.apache.ambari.server.events.MetadataUpdateEvent getClusterMetadataOnServiceInstall(org.apache.ambari.server.state.Cluster cl, java.lang.String serviceName) throws org.apache.ambari.server.AmbariException {
        return getClusterMetadataOnServiceCredentialStoreUpdate(cl, serviceName);
    }

    public org.apache.ambari.server.events.MetadataUpdateEvent getClusterMetadataOnServiceCredentialStoreUpdate(org.apache.ambari.server.state.Cluster cl, java.lang.String serviceName) throws org.apache.ambari.server.AmbariException {
        final java.util.TreeMap<java.lang.String, org.apache.ambari.server.agent.stomp.dto.MetadataCluster> metadataClusters = new java.util.TreeMap<>();
        metadataClusters.put(java.lang.Long.toString(cl.getClusterId()), org.apache.ambari.server.agent.stomp.dto.MetadataCluster.serviceLevelParamsMetadataCluster(null, getMetadataServiceLevelParams(cl), false));
        return new org.apache.ambari.server.events.MetadataUpdateEvent(metadataClusters, null, getMetadataAgentConfigs(), org.apache.ambari.server.events.UpdateEventType.UPDATE);
    }

    private java.lang.String getClientsToUpdateConfigs(org.apache.ambari.server.state.ComponentInfo componentInfo) {
        java.util.List<java.lang.String> clientsToUpdateConfigsList = componentInfo.getClientsToUpdateConfigs();
        if (clientsToUpdateConfigsList == null) {
            clientsToUpdateConfigsList = new java.util.ArrayList<>();
            clientsToUpdateConfigsList.add("*");
        }
        return gson.toJson(clientsToUpdateConfigsList);
    }

    private java.lang.Boolean getUnlimitedKeyJCERequirement(org.apache.ambari.server.state.ComponentInfo componentInfo, org.apache.ambari.server.state.SecurityType clusterSecurityType) {
        org.apache.ambari.server.state.UnlimitedKeyJCERequirement unlimitedKeyJCERequirement = componentInfo.getUnlimitedKeyJCERequired();
        if (unlimitedKeyJCERequirement == null) {
            unlimitedKeyJCERequirement = org.apache.ambari.server.state.UnlimitedKeyJCERequirement.DEFAULT;
        }
        return (org.apache.ambari.server.state.UnlimitedKeyJCERequirement.ALWAYS == unlimitedKeyJCERequirement) || ((org.apache.ambari.server.state.UnlimitedKeyJCERequirement.KERBEROS_ENABLED == unlimitedKeyJCERequirement) && (clusterSecurityType == org.apache.ambari.server.state.SecurityType.KERBEROS));
    }

    public java.util.TreeMap<java.lang.String, java.lang.String> getTopologyComponentLevelParams(java.lang.Long clusterId, java.lang.String serviceName, java.lang.String componentName, org.apache.ambari.server.state.SecurityType clusterSecurityType) throws org.apache.ambari.server.AmbariException {
        java.util.TreeMap<java.lang.String, java.lang.String> statusCommandParams = new java.util.TreeMap<>();
        org.apache.ambari.server.orm.entities.RepositoryVersionEntity repositoryVersion = getComponentRepositoryVersion(clusterId, serviceName, componentName);
        if (null != repositoryVersion) {
            org.apache.ambari.server.state.StackId stackId = repositoryVersion.getStackId();
            org.apache.ambari.server.state.ComponentInfo componentInfo = ambariMetaInfo.getComponent(stackId.getStackName(), stackId.getStackVersion(), serviceName, componentName);
            statusCommandParams.put(org.apache.ambari.server.agent.ExecutionCommand.KeyNames.CLIENTS_TO_UPDATE_CONFIGS, getClientsToUpdateConfigs(componentInfo));
            statusCommandParams.put(org.apache.ambari.server.agent.ExecutionCommand.KeyNames.UNLIMITED_KEY_JCE_REQUIRED, java.lang.Boolean.toString(getUnlimitedKeyJCERequirement(componentInfo, clusterSecurityType)));
        }
        return statusCommandParams;
    }

    @java.lang.Override
    public java.util.Map<java.lang.String, org.apache.ambari.server.state.BlueprintProvisioningState> getBlueprintProvisioningStates(java.lang.Long clusterId, java.lang.Long hostId) throws org.apache.ambari.server.AmbariException {
        java.util.Map<java.lang.String, org.apache.ambari.server.state.BlueprintProvisioningState> blueprintProvisioningStates = new java.util.HashMap<>();
        org.apache.ambari.server.state.Host host = clusters.getHostById(hostId);
        org.apache.ambari.server.state.Cluster cl = clusters.getCluster(clusterId);
        for (org.apache.ambari.server.state.ServiceComponentHost sch : cl.getServiceComponentHosts(host.getHostName())) {
            if (!sch.isClientComponent()) {
                blueprintProvisioningStates.put(sch.getServiceComponentName(), sch.getDesiredStateEntity().getBlueprintProvisioningState());
            }
        }
        return blueprintProvisioningStates;
    }

    public java.util.TreeMap<java.lang.String, java.lang.String> getTopologyCommandParams(java.lang.Long clusterId, java.lang.String serviceName, java.lang.String componentName, org.apache.ambari.server.state.ServiceComponentHost sch) throws org.apache.ambari.server.AmbariException {
        java.util.TreeMap<java.lang.String, java.lang.String> commandParams = new java.util.TreeMap<>();
        org.apache.ambari.server.orm.entities.RepositoryVersionEntity repositoryVersion = getComponentRepositoryVersion(clusterId, serviceName, componentName);
        if (null != repositoryVersion) {
            org.apache.ambari.server.state.StackId stackId = repositoryVersion.getStackId();
            org.apache.ambari.server.state.ServiceInfo serviceInfo = ambariMetaInfo.getService(stackId.getStackName(), stackId.getStackVersion(), serviceName);
            org.apache.ambari.server.state.ComponentInfo componentInfo = ambariMetaInfo.getComponent(stackId.getStackName(), stackId.getStackVersion(), serviceName, componentName);
            commandParams.put(org.apache.ambari.server.agent.ExecutionCommand.KeyNames.SERVICE_PACKAGE_FOLDER, serviceInfo.getServicePackageFolder());
            java.lang.String scriptName = null;
            java.lang.String scriptCommandTimeout = "";
            org.apache.ambari.server.state.CommandScriptDefinition script = componentInfo.getCommandScript();
            if (serviceInfo.getSchemaVersion().equals(org.apache.ambari.server.api.services.AmbariMetaInfo.SCHEMA_VERSION_2)) {
                if (script != null) {
                    scriptName = script.getScript();
                    if (script.getTimeout() > 0) {
                        scriptCommandTimeout = java.lang.String.valueOf(script.getTimeout());
                    }
                } else {
                    java.lang.String message = java.lang.String.format("Component %s of service %s has not " + "command script defined", componentName, serviceName);
                    throw new org.apache.ambari.server.AmbariException(message);
                }
            }
            java.lang.String agentDefaultCommandTimeout = configs.getDefaultAgentTaskTimeout(false);
            java.lang.String actualTimeout = (!scriptCommandTimeout.equals("")) ? scriptCommandTimeout : agentDefaultCommandTimeout;
            commandParams.put(org.apache.ambari.server.agent.ExecutionCommand.KeyNames.COMMAND_TIMEOUT, actualTimeout);
            commandParams.put(org.apache.ambari.server.agent.ExecutionCommand.KeyNames.SCRIPT, scriptName);
            commandParams.put(org.apache.ambari.server.agent.ExecutionCommand.KeyNames.SCRIPT_TYPE, script.getScriptType().toString());
        }
        java.lang.String schVersion = sch.getVersion();
        if (!schVersion.equals("UNKNOWN")) {
            commandParams.put(org.apache.ambari.server.agent.ExecutionCommand.KeyNames.VERSION, schVersion);
        }
        return commandParams;
    }

    private org.apache.ambari.server.orm.entities.RepositoryVersionEntity getComponentRepositoryVersion(java.lang.Long clusterId, java.lang.String serviceName, java.lang.String componentName) throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.server.orm.entities.RepositoryVersionEntity repositoryVersion = null;
        if (!org.apache.commons.lang.StringUtils.isEmpty(serviceName)) {
            org.apache.ambari.server.state.Service service = clusters.getCluster(clusterId).getService(serviceName);
            if (null != service) {
                repositoryVersion = service.getDesiredRepositoryVersion();
                if ((!org.apache.commons.lang.StringUtils.isEmpty(componentName)) && service.getServiceComponents().containsKey(componentName)) {
                    org.apache.ambari.server.state.ServiceComponent serviceComponent = service.getServiceComponent(componentName);
                    if (null != serviceComponent) {
                        repositoryVersion = serviceComponent.getDesiredRepositoryVersion();
                    }
                }
            }
        }
        return repositoryVersion;
    }

    public java.util.TreeMap<java.lang.String, java.lang.String> getMetadataClusterLevelParams(org.apache.ambari.server.state.Cluster cluster, org.apache.ambari.server.state.StackId stackId) throws org.apache.ambari.server.AmbariException {
        java.util.TreeMap<java.lang.String, java.lang.String> clusterLevelParams = new java.util.TreeMap<>();
        clusterLevelParams.put(org.apache.ambari.server.agent.ExecutionCommand.KeyNames.STACK_NAME, stackId.getStackName());
        clusterLevelParams.put(org.apache.ambari.server.agent.ExecutionCommand.KeyNames.STACK_VERSION, stackId.getStackVersion());
        clusterLevelParams.putAll(getMetadataClusterLevelConfigsParams(cluster, stackId));
        clusterLevelParams.put(org.apache.ambari.server.agent.ExecutionCommand.KeyNames.CLUSTER_NAME, cluster.getClusterName());
        clusterLevelParams.put(org.apache.ambari.server.agent.ExecutionCommand.KeyNames.HOOKS_FOLDER, configs.getProperty(org.apache.ambari.server.configuration.Configuration.HOOKS_FOLDER));
        return clusterLevelParams;
    }

    private java.util.TreeMap<java.lang.String, java.lang.String> getMetadataClusterLevelConfigsParams(org.apache.ambari.server.state.Cluster cluster, org.apache.ambari.server.state.StackId stackId) throws org.apache.ambari.server.AmbariException {
        java.util.TreeMap<java.lang.String, java.lang.String> clusterLevelParams = new java.util.TreeMap<>();
        java.util.Map<java.lang.String, org.apache.ambari.server.state.DesiredConfig> desiredConfigs = cluster.getDesiredConfigs(false);
        if (org.apache.commons.collections.MapUtils.isNotEmpty(desiredConfigs)) {
            java.util.Set<java.lang.String> userSet = configHelper.getPropertyValuesWithPropertyType(stackId, org.apache.ambari.server.state.PropertyInfo.PropertyType.USER, cluster, desiredConfigs);
            java.lang.String userList = gson.toJson(userSet);
            clusterLevelParams.put(org.apache.ambari.server.agent.ExecutionCommand.KeyNames.USER_LIST, userList);
            java.util.Map<java.lang.String, java.util.Set<java.lang.String>> userGroupsMap = configHelper.createUserGroupsMap(stackId, cluster, desiredConfigs);
            java.lang.String userGroups = gson.toJson(userGroupsMap);
            clusterLevelParams.put(org.apache.ambari.server.agent.ExecutionCommand.KeyNames.USER_GROUPS, userGroups);
            java.util.Set<java.lang.String> groupSet = configHelper.getPropertyValuesWithPropertyType(stackId, org.apache.ambari.server.state.PropertyInfo.PropertyType.GROUP, cluster, desiredConfigs);
            java.lang.String groupList = gson.toJson(groupSet);
            clusterLevelParams.put(org.apache.ambari.server.agent.ExecutionCommand.KeyNames.GROUP_LIST, groupList);
        }
        java.util.Set<java.lang.String> notManagedHdfsPathSet = configHelper.getPropertyValuesWithPropertyType(stackId, org.apache.ambari.server.state.PropertyInfo.PropertyType.NOT_MANAGED_HDFS_PATH, cluster, desiredConfigs);
        java.lang.String notManagedHdfsPathList = gson.toJson(notManagedHdfsPathSet);
        clusterLevelParams.put(org.apache.ambari.server.agent.ExecutionCommand.KeyNames.NOT_MANAGED_HDFS_PATH_LIST, notManagedHdfsPathList);
        for (org.apache.ambari.server.state.Service service : cluster.getServices().values()) {
            org.apache.ambari.server.state.ServiceInfo serviceInfoInstance = ambariMetaInfo.getService(service);
            java.lang.String serviceType = serviceInfoInstance.getServiceType();
            if (serviceType != null) {
                org.apache.ambari.server.controller.AmbariManagementControllerImpl.LOG.debug("Adding {} to command parameters for {}", serviceInfoInstance.getServiceType(), serviceInfoInstance.getName());
                clusterLevelParams.put(org.apache.ambari.server.agent.ExecutionCommand.KeyNames.DFS_TYPE, serviceInfoInstance.getServiceType());
                break;
            }
        }
        return clusterLevelParams;
    }

    public java.util.TreeMap<java.lang.String, org.apache.ambari.server.agent.stomp.dto.MetadataServiceInfo> getMetadataServiceLevelParams(org.apache.ambari.server.state.Cluster cluster) throws org.apache.ambari.server.AmbariException {
        java.util.TreeMap<java.lang.String, org.apache.ambari.server.agent.stomp.dto.MetadataServiceInfo> serviceLevelParams = new java.util.TreeMap<>();
        for (java.util.Map.Entry<java.lang.String, org.apache.ambari.server.state.Service> serviceEntry : cluster.getServices().entrySet()) {
            org.apache.ambari.server.state.Service service = serviceEntry.getValue();
            serviceLevelParams.putAll(getMetadataServiceLevelParams(service));
        }
        return serviceLevelParams;
    }

    public java.util.TreeMap<java.lang.String, org.apache.ambari.server.agent.stomp.dto.MetadataServiceInfo> getMetadataServiceLevelParams(org.apache.ambari.server.state.Service service) throws org.apache.ambari.server.AmbariException {
        java.util.TreeMap<java.lang.String, org.apache.ambari.server.agent.stomp.dto.MetadataServiceInfo> serviceLevelParams = new java.util.TreeMap<>();
        org.apache.ambari.server.orm.entities.RepositoryVersionEntity repositoryVersion = service.getDesiredRepositoryVersion();
        if (null != repositoryVersion) {
            org.apache.ambari.server.state.StackId serviceStackId = repositoryVersion.getStackId();
            org.apache.ambari.server.state.ServiceInfo serviceInfo = ambariMetaInfo.getService(serviceStackId.getStackName(), serviceStackId.getStackVersion(), service.getName());
            java.lang.Long statusCommandTimeout = null;
            if (serviceInfo.getCommandScript() != null) {
                statusCommandTimeout = new java.lang.Long(customCommandExecutionHelper.getStatusCommandTimeout(serviceInfo));
            }
            java.lang.String servicePackageFolder = serviceInfo.getServicePackageFolder();
            java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> configCredentials;
            configCredentials = configCredentialsForService.get(service.getName());
            if (configCredentials == null) {
                configCredentials = configHelper.getCredentialStoreEnabledProperties(serviceStackId, service);
                configCredentialsForService.put(service.getName(), configCredentials);
            }
            serviceLevelParams.put(serviceInfo.getName(), new org.apache.ambari.server.agent.stomp.dto.MetadataServiceInfo(serviceInfo.getVersion(), service.isCredentialStoreEnabled(), configCredentials, statusCommandTimeout, servicePackageFolder));
        }
        return serviceLevelParams;
    }

    public java.util.TreeMap<java.lang.String, java.lang.String> getMetadataAmbariLevelParams() {
        java.util.TreeMap<java.lang.String, java.lang.String> clusterLevelParams = new java.util.TreeMap<>();
        clusterLevelParams.put(org.apache.ambari.server.agent.ExecutionCommand.KeyNames.JDK_LOCATION, getJdkResourceUrl());
        clusterLevelParams.put(org.apache.ambari.server.agent.ExecutionCommand.KeyNames.JAVA_HOME, getJavaHome());
        clusterLevelParams.put(org.apache.ambari.server.agent.ExecutionCommand.KeyNames.JAVA_VERSION, java.lang.String.valueOf(configs.getJavaVersion()));
        clusterLevelParams.put(org.apache.ambari.server.agent.ExecutionCommand.KeyNames.JDK_NAME, getJDKName());
        clusterLevelParams.put(org.apache.ambari.server.agent.ExecutionCommand.KeyNames.JCE_NAME, getJCEName());
        clusterLevelParams.put(org.apache.ambari.server.agent.ExecutionCommand.KeyNames.DB_NAME, getServerDB());
        clusterLevelParams.put(org.apache.ambari.server.agent.ExecutionCommand.KeyNames.MYSQL_JDBC_URL, getMysqljdbcUrl());
        clusterLevelParams.put(org.apache.ambari.server.agent.ExecutionCommand.KeyNames.ORACLE_JDBC_URL, getOjdbcUrl());
        clusterLevelParams.put(org.apache.ambari.server.agent.ExecutionCommand.KeyNames.DB_DRIVER_FILENAME, configs.getMySQLJarName());
        clusterLevelParams.putAll(getRcaParameters());
        clusterLevelParams.put(org.apache.ambari.server.agent.ExecutionCommand.KeyNames.HOST_SYS_PREPPED, configs.areHostsSysPrepped());
        clusterLevelParams.put(org.apache.ambari.server.agent.ExecutionCommand.KeyNames.AGENT_STACK_RETRY_ON_UNAVAILABILITY, configs.isAgentStackRetryOnInstallEnabled());
        clusterLevelParams.put(org.apache.ambari.server.agent.ExecutionCommand.KeyNames.AGENT_STACK_RETRY_COUNT, configs.getAgentStackRetryOnInstallCount());
        boolean serverUseSsl = configs.getApiSSLAuthentication();
        int port = (serverUseSsl) ? configs.getClientSSLApiPort() : configs.getClientApiPort();
        clusterLevelParams.put(org.apache.ambari.server.controller.AmbariManagementControllerImpl.AMBARI_SERVER_HOST, org.apache.ambari.server.utils.StageUtils.getHostName());
        clusterLevelParams.put(org.apache.ambari.server.controller.AmbariManagementControllerImpl.AMBARI_SERVER_PORT, java.lang.Integer.toString(port));
        clusterLevelParams.put(org.apache.ambari.server.controller.AmbariManagementControllerImpl.AMBARI_SERVER_USE_SSL, java.lang.Boolean.toString(serverUseSsl));
        for (java.util.Map.Entry<java.lang.String, java.lang.String> dbConnectorName : configs.getDatabaseConnectorNames().entrySet()) {
            clusterLevelParams.put(dbConnectorName.getKey(), dbConnectorName.getValue());
        }
        for (java.util.Map.Entry<java.lang.String, java.lang.String> previousDBConnectorName : configs.getPreviousDatabaseConnectorNames().entrySet()) {
            clusterLevelParams.put(previousDBConnectorName.getKey(), previousDBConnectorName.getValue());
        }
        clusterLevelParams.put(org.apache.ambari.server.agent.ExecutionCommand.KeyNames.GPL_LICENSE_ACCEPTED, configs.getGplLicenseAccepted().toString());
        return clusterLevelParams;
    }

    public java.util.SortedMap<java.lang.String, java.util.SortedMap<java.lang.String, java.lang.String>> getMetadataAgentConfigs() {
        java.util.SortedMap<java.lang.String, java.util.SortedMap<java.lang.String, java.lang.String>> agentConfigs = new java.util.TreeMap<>();
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> agentConfigsMap = configs.getAgentConfigsMap();
        for (java.lang.String key : agentConfigsMap.keySet()) {
            agentConfigs.put(key, new java.util.TreeMap<>(agentConfigsMap.get(key)));
        }
        return agentConfigs;
    }
}