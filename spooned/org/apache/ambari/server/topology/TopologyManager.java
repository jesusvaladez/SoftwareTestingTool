package org.apache.ambari.server.topology;
import com.google.inject.persist.Transactional;
@com.google.inject.Singleton
public class TopologyManager {
    public static final java.lang.String INTERNAL_AUTH_TOKEN = "internal_topology_token";

    public static final java.lang.String INITIAL_CONFIG_TAG = "INITIAL";

    public static final java.lang.String TOPOLOGY_RESOLVED_TAG = "TOPOLOGY_RESOLVED";

    public static final java.lang.String KDC_ADMIN_CREDENTIAL = "kdc.admin.credential";

    private org.apache.ambari.server.topology.PersistedState persistedState;

    private final java.util.concurrent.ExecutorService executor = java.util.concurrent.Executors.newSingleThreadExecutor();

    private int topologyTaskExecutorThreadPoolSize;

    private final java.util.Map<java.lang.Long, org.apache.ambari.server.utils.ManagedThreadPoolExecutor> topologyTaskExecutorServiceMap = new java.util.HashMap<>();

    private java.util.Collection<java.lang.String> hostsToIgnore = new java.util.HashSet<>();

    private final java.util.List<org.apache.ambari.server.state.host.HostImpl> availableHosts = new java.util.LinkedList<>();

    private final java.util.Map<java.lang.String, org.apache.ambari.server.topology.LogicalRequest> reservedHosts = new java.util.HashMap<>();

    private final java.util.Map<java.lang.Long, org.apache.ambari.server.topology.LogicalRequest> allRequests = new java.util.HashMap<>();

    private final java.util.Collection<org.apache.ambari.server.topology.LogicalRequest> outstandingRequests = new java.util.ArrayList<>();

    private java.util.Map<java.lang.Long, org.apache.ambari.server.topology.ClusterTopology> clusterTopologyMap = new java.util.HashMap<>();

    @javax.inject.Inject
    private org.apache.ambari.server.api.services.stackadvisor.StackAdvisorBlueprintProcessor stackAdvisorBlueprintProcessor;

    @javax.inject.Inject
    private org.apache.ambari.server.topology.LogicalRequestFactory logicalRequestFactory;

    @javax.inject.Inject
    private org.apache.ambari.server.topology.AmbariContext ambariContext;

    private final java.lang.Object initializationLock = new java.lang.Object();

    @javax.inject.Inject
    private org.apache.ambari.server.topology.SecurityConfigurationFactory securityConfigurationFactory;

    @javax.inject.Inject
    private org.apache.ambari.server.topology.tasks.ConfigureClusterTaskFactory configureClusterTaskFactory;

    @javax.inject.Inject
    private org.apache.ambari.server.events.publishers.AmbariEventPublisher ambariEventPublisher;

    @javax.inject.Inject
    private org.apache.ambari.server.orm.dao.SettingDAO settingDAO;

    @javax.inject.Inject
    private org.apache.ambari.server.topology.validators.TopologyValidatorService topologyValidatorService;

    private volatile boolean isInitialized;

    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.topology.TopologyManager.class);

    private java.util.Map<java.lang.Long, org.apache.ambari.server.topology.LogicalRequest> clusterProvisionWithBlueprintCreateRequests = new java.util.HashMap<>();

    private java.util.Map<java.lang.Long, java.lang.Boolean> clusterProvisionWithBlueprintCreationFinished = new java.util.HashMap<>();

    public TopologyManager() {
        topologyTaskExecutorThreadPoolSize = 1;
    }

    @javax.inject.Inject
    public TopologyManager(org.apache.ambari.server.configuration.Configuration configuration) {
        topologyTaskExecutorThreadPoolSize = configuration.getParallelTopologyTaskCreationThreadPoolSize();
        if (!configuration.isParallelTopologyTaskCreationEnabled()) {
            topologyTaskExecutorThreadPoolSize = 1;
        }
    }

    @javax.inject.Inject
    private void register() {
        ambariEventPublisher.register(this);
    }

    @javax.inject.Inject
    private void setPersistedState() {
        persistedState = ambariContext.getPersistedTopologyState();
    }

    private void ensureInitialized() {
        if (!isInitialized) {
            synchronized(initializationLock) {
                if (!isInitialized) {
                    replayRequests(persistedState.getAllRequests());
                    for (org.apache.ambari.server.topology.ClusterTopology clusterTopology : clusterTopologyMap.values()) {
                        if (clusterTopology.isClusterKerberosEnabled() && isKerberosClientInstallAllowed(clusterTopology)) {
                            addKerberosClient(clusterTopology);
                        }
                    }
                    isInitialized = true;
                }
            }
        }
    }

    @com.google.common.eventbus.Subscribe
    public void onRequestFinished(org.apache.ambari.server.events.RequestFinishedEvent event) {
        if (((event.getType() != org.apache.ambari.server.events.AmbariEvent.AmbariEventType.REQUEST_FINISHED) || clusterProvisionWithBlueprintCreateRequests.isEmpty()) || java.lang.Boolean.TRUE.equals(clusterProvisionWithBlueprintCreationFinished.get(event.getClusterId()))) {
            return;
        }
        if (isClusterProvisionWithBlueprintFinished(event.getClusterId())) {
            clusterProvisionWithBlueprintCreationFinished.put(event.getClusterId(), java.lang.Boolean.TRUE);
            org.apache.ambari.server.topology.LogicalRequest provisionRequest = clusterProvisionWithBlueprintCreateRequests.get(event.getClusterId());
            if (isLogicalRequestSuccessful(provisionRequest)) {
                org.apache.ambari.server.topology.TopologyManager.LOG.info("Cluster creation request id={} using Blueprint {} successfully completed for cluster id={}", clusterProvisionWithBlueprintCreateRequests.get(event.getClusterId()).getRequestId(), clusterTopologyMap.get(event.getClusterId()).getBlueprint().getName(), event.getClusterId());
                ambariEventPublisher.publish(new org.apache.ambari.server.events.ClusterProvisionedEvent(event.getClusterId()));
            } else {
                org.apache.ambari.server.topology.TopologyManager.LOG.info("Cluster creation request id={} using Blueprint {} failed for cluster id={}", clusterProvisionWithBlueprintCreateRequests.get(event.getClusterId()).getRequestId(), clusterTopologyMap.get(event.getClusterId()).getBlueprint().getName(), event.getClusterId());
            }
        }
    }

    public boolean isClusterProvisionWithBlueprintTracked(long clusterId) {
        return clusterProvisionWithBlueprintCreateRequests.containsKey(clusterId);
    }

    public boolean isClusterProvisionWithBlueprintFinished(long clusterId) {
        if (!isClusterProvisionWithBlueprintTracked(clusterId)) {
            return false;
        }
        if (clusterProvisionWithBlueprintCreationFinished.containsKey(clusterId) && clusterProvisionWithBlueprintCreationFinished.get(clusterId)) {
            return true;
        }
        return isLogicalRequestFinished(clusterProvisionWithBlueprintCreateRequests.get(clusterId));
    }

    public org.apache.ambari.server.controller.RequestStatusResponse provisionCluster(final org.apache.ambari.server.controller.internal.ProvisionClusterRequest request) throws org.apache.ambari.server.topology.InvalidTopologyException, org.apache.ambari.server.AmbariException {
        ensureInitialized();
        final org.apache.ambari.server.topology.ClusterTopology topology = new org.apache.ambari.server.topology.ClusterTopologyImpl(ambariContext, request);
        final java.lang.String clusterName = request.getClusterName();
        final org.apache.ambari.server.controller.internal.Stack stack = topology.getBlueprint().getStack();
        final java.lang.String repoVersion = request.getRepositoryVersion();
        final java.lang.Long repoVersionID = request.getRepositoryVersionId();
        final java.lang.Long provisionId = ambariContext.getNextRequestId();
        org.apache.ambari.server.state.SecurityType securityType = null;
        org.apache.ambari.server.topology.Credential credential = null;
        org.apache.ambari.server.topology.SecurityConfiguration securityConfiguration = processSecurityConfiguration(request);
        if ((securityConfiguration != null) && (securityConfiguration.getType() == org.apache.ambari.server.state.SecurityType.KERBEROS)) {
            securityType = org.apache.ambari.server.state.SecurityType.KERBEROS;
            if (isKerberosClientInstallAllowed(topology)) {
                addKerberosClient(topology);
            }
            topology.getBlueprint().getConfiguration().setParentConfiguration(stack.getConfiguration(topology.getBlueprint().getServices()));
            credential = request.getCredentialsMap().get(org.apache.ambari.server.topology.TopologyManager.KDC_ADMIN_CREDENTIAL);
            if (credential == null) {
                throw new org.apache.ambari.server.topology.InvalidTopologyException(org.apache.ambari.server.topology.TopologyManager.KDC_ADMIN_CREDENTIAL + " is missing from request.");
            }
        }
        topologyValidatorService.validateTopologyConfiguration(topology);
        ambariContext.createAmbariResources(topology, clusterName, securityType, repoVersion, repoVersionID);
        if (securityConfiguration != null) {
            securityConfiguration.getDescriptor().ifPresent(descriptor -> submitKerberosDescriptorAsArtifact(clusterName, descriptor));
        }
        if (credential != null) {
            org.apache.ambari.server.topology.TopologyManager.submitCredential(clusterName, credential);
        }
        long clusterId = ambariContext.getClusterId(clusterName);
        topology.setClusterId(clusterId);
        request.setClusterId(clusterId);
        topology.setConfigRecommendationStrategy(request.getConfigRecommendationStrategy());
        topology.setProvisionAction(request.getProvisionAction());
        getOrCreateTopologyTaskExecutor(clusterId);
        org.apache.ambari.server.topology.LogicalRequest logicalRequest = org.apache.ambari.server.utils.RetryHelper.executeWithRetry(new java.util.concurrent.Callable<org.apache.ambari.server.topology.LogicalRequest>() {
            @java.lang.Override
            public org.apache.ambari.server.topology.LogicalRequest call() throws java.lang.Exception {
                org.apache.ambari.server.topology.LogicalRequest logicalRequest = processAndPersistProvisionClusterTopologyRequest(request, topology, provisionId);
                return logicalRequest;
            }
        });
        clusterTopologyMap.put(clusterId, topology);
        addClusterConfigRequest(logicalRequest, topology, new org.apache.ambari.server.topology.ClusterConfigurationRequest(ambariContext, topology, true, stackAdvisorBlueprintProcessor, securityType == org.apache.ambari.server.state.SecurityType.KERBEROS));
        processRequest(request, topology, logicalRequest);
        ambariContext.persistInstallStateForUI(clusterName, stack.getName(), stack.getVersion());
        clusterProvisionWithBlueprintCreateRequests.put(clusterId, logicalRequest);
        ambariEventPublisher.publish(new org.apache.ambari.server.events.ClusterProvisionStartedEvent(clusterId));
        return getRequestStatus(logicalRequest.getRequestId());
    }

    private boolean isKerberosClientInstallAllowed(final org.apache.ambari.server.topology.ClusterTopology topology) {
        final org.apache.ambari.server.topology.Configuration topologyConfig = topology.getBlueprint().getConfiguration();
        final java.lang.String kdc_type = topologyConfig.getPropertyValue(org.apache.ambari.server.controller.KerberosHelper.KERBEROS_ENV, org.apache.ambari.server.controller.KerberosHelper.KDC_TYPE);
        final java.lang.String manage_identities = topologyConfig.getPropertyValue(org.apache.ambari.server.controller.KerberosHelper.KERBEROS_ENV, org.apache.ambari.server.controller.KerberosHelper.MANAGE_IDENTITIES);
        return (org.apache.ambari.server.serveraction.kerberos.KDCType.NONE != org.apache.ambari.server.serveraction.kerberos.KDCType.translate(kdc_type)) || java.lang.Boolean.parseBoolean(manage_identities);
    }

    @com.google.common.eventbus.Subscribe
    public void onClusterConfigFinishedEvent(org.apache.ambari.server.events.ClusterConfigFinishedEvent event) {
        org.apache.ambari.server.utils.ManagedThreadPoolExecutor taskExecutor = topologyTaskExecutorServiceMap.get(event.getClusterId());
        if (taskExecutor == null) {
            org.apache.ambari.server.topology.TopologyManager.LOG.error("Can't find executor service taskQueue not found for cluster: {} ", event.getClusterName());
        } else {
            org.apache.ambari.server.topology.TopologyManager.LOG.info("Starting topology task ExecutorService for cluster: {}", event.getClusterName());
            taskExecutor.start();
        }
    }

    void saveOrUpdateQuickLinksProfile(java.lang.String quickLinksProfileJson) {
        org.apache.ambari.server.orm.entities.SettingEntity settingEntity = settingDAO.findByName(org.apache.ambari.server.state.quicklinksprofile.QuickLinksProfile.SETTING_NAME_QUICKLINKS_PROFILE);
        if (null == settingEntity) {
            settingEntity = new org.apache.ambari.server.orm.entities.SettingEntity();
            settingEntity.setName(org.apache.ambari.server.state.quicklinksprofile.QuickLinksProfile.SETTING_NAME_QUICKLINKS_PROFILE);
            settingEntity.setSettingType(org.apache.ambari.server.state.quicklinksprofile.QuickLinksProfile.SETTING_TYPE_AMBARI_SERVER);
            settingEntity.setContent(quickLinksProfileJson);
            settingEntity.setUpdatedBy(org.apache.ambari.server.security.authorization.AuthorizationHelper.getAuthenticatedName());
            settingEntity.setUpdateTimestamp(java.lang.System.currentTimeMillis());
            settingDAO.create(settingEntity);
        } else {
            settingEntity.setContent(quickLinksProfileJson);
            settingEntity.setUpdatedBy(org.apache.ambari.server.security.authorization.AuthorizationHelper.getAuthenticatedName());
            settingEntity.setUpdateTimestamp(java.lang.System.currentTimeMillis());
            settingDAO.merge(settingEntity);
        }
    }

    private static void submitCredential(java.lang.String clusterName, org.apache.ambari.server.topology.Credential credential) {
        org.apache.ambari.server.controller.spi.ResourceProvider provider = org.apache.ambari.server.topology.AmbariContext.getClusterController().ensureResourceProvider(org.apache.ambari.server.controller.spi.Resource.Type.Credential);
        java.util.Map<java.lang.String, java.lang.Object> credentialProperties = org.apache.ambari.server.topology.addservice.ResourceProviderAdapter.createCredentialRequestProperties(clusterName, credential);
        org.apache.ambari.server.controller.spi.Request request = new org.apache.ambari.server.controller.internal.RequestImpl(com.google.common.collect.ImmutableSet.of(), com.google.common.collect.ImmutableSet.of(credentialProperties), com.google.common.collect.ImmutableMap.of(), null);
        java.lang.String baseMessage = java.lang.String.format("Failed to add credential %s to cluster %s", credential.getAlias(), clusterName);
        try {
            org.apache.ambari.server.controller.spi.RequestStatus status = provider.createResources(request);
            if (status.getStatus() != org.apache.ambari.server.controller.spi.RequestStatus.Status.Complete) {
                java.lang.String msg = java.lang.String.format("%s, received status: %s", baseMessage, status.getStatus());
                org.apache.ambari.server.topology.TopologyManager.LOG.error(msg);
                throw new java.lang.RuntimeException(msg);
            }
        } catch (org.apache.ambari.server.controller.spi.ResourceAlreadyExistsException | org.apache.ambari.server.controller.spi.SystemException | org.apache.ambari.server.controller.spi.UnsupportedPropertyException | org.apache.ambari.server.controller.spi.NoSuchParentResourceException e) {
            java.lang.String msg = java.lang.String.format("%s, %s", baseMessage, e);
            org.apache.ambari.server.topology.TopologyManager.LOG.error(msg);
            throw new java.lang.RuntimeException(msg, e);
        }
    }

    private org.apache.ambari.server.topology.SecurityConfiguration processSecurityConfiguration(org.apache.ambari.server.controller.internal.ProvisionClusterRequest request) {
        org.apache.ambari.server.topology.TopologyManager.LOG.debug("Getting security configuration from the request ...");
        org.apache.ambari.server.topology.SecurityConfiguration securityConfiguration = request.getSecurityConfiguration();
        if (securityConfiguration == null) {
            org.apache.ambari.server.topology.TopologyManager.LOG.debug("There's no security configuration in the request, retrieving it from the associated blueprint");
            securityConfiguration = request.getBlueprint().getSecurity();
            if (((securityConfiguration != null) && (securityConfiguration.getType() == org.apache.ambari.server.state.SecurityType.KERBEROS)) && (securityConfiguration.getDescriptorReference() != null)) {
                securityConfiguration = securityConfigurationFactory.loadSecurityConfigurationByReference(securityConfiguration.getDescriptorReference());
            }
        }
        return securityConfiguration;
    }

    private void submitKerberosDescriptorAsArtifact(java.lang.String clusterName, java.util.Map<?, ?> descriptor) {
        org.apache.ambari.server.controller.spi.ResourceProvider artifactProvider = ambariContext.getClusterController().ensureResourceProvider(org.apache.ambari.server.controller.spi.Resource.Type.Artifact);
        java.util.Map<java.lang.String, java.lang.Object> properties = org.apache.ambari.server.topology.addservice.ResourceProviderAdapter.createKerberosDescriptorRequestProperties(clusterName);
        java.util.Map<java.lang.String, java.lang.String> requestInfoProps = com.google.common.collect.ImmutableMap.of(org.apache.ambari.server.controller.spi.Request.REQUEST_INFO_BODY_PROPERTY, org.apache.ambari.server.controller.internal.ArtifactResourceProvider.toArtifactDataJson(descriptor));
        org.apache.ambari.server.controller.spi.Request request = new org.apache.ambari.server.controller.internal.RequestImpl(java.util.Collections.emptySet(), java.util.Collections.singleton(properties), requestInfoProps, null);
        try {
            org.apache.ambari.server.controller.spi.RequestStatus status = artifactProvider.createResources(request);
            try {
                while (status.getStatus() != org.apache.ambari.server.controller.spi.RequestStatus.Status.Complete) {
                    org.apache.ambari.server.topology.TopologyManager.LOG.info("Waiting for kerberos_descriptor artifact creation.");
                    java.lang.Thread.sleep(100);
                } 
            } catch (java.lang.InterruptedException e) {
                org.apache.ambari.server.topology.TopologyManager.LOG.info("Wait for resource creation interrupted!");
            }
            if (status.getStatus() != org.apache.ambari.server.controller.spi.RequestStatus.Status.Complete) {
                throw new java.lang.RuntimeException("Failed to attach kerberos_descriptor artifact to cluster!");
            }
        } catch (org.apache.ambari.server.controller.spi.SystemException | org.apache.ambari.server.controller.spi.UnsupportedPropertyException | org.apache.ambari.server.controller.spi.NoSuchParentResourceException e) {
            throw new java.lang.RuntimeException("Failed to attach kerberos_descriptor artifact to cluster: " + e);
        } catch (org.apache.ambari.server.controller.spi.ResourceAlreadyExistsException e) {
            throw new java.lang.RuntimeException("Failed to attach kerberos_descriptor artifact to cluster as resource already exists.");
        }
    }

    public org.apache.ambari.server.controller.RequestStatusResponse scaleHosts(final org.apache.ambari.server.controller.internal.ScaleClusterRequest request) throws org.apache.ambari.server.topology.InvalidTopologyException, org.apache.ambari.server.AmbariException {
        ensureInitialized();
        org.apache.ambari.server.topology.TopologyManager.LOG.info("TopologyManager.scaleHosts: Entering");
        java.lang.String clusterName = request.getClusterName();
        long clusterId = ambariContext.getClusterId(clusterName);
        final org.apache.ambari.server.topology.ClusterTopology topology = clusterTopologyMap.get(clusterId);
        if (topology == null) {
            throw new org.apache.ambari.server.topology.InvalidTopologyException("Unable to retrieve cluster topology for cluster. This is most likely a " + ((("result of trying to scale a cluster via the API which was created using " + "the Ambari UI. At this time only clusters created via the API using a ") + "blueprint can be scaled with this API.  If the cluster was originally created ") + "via the API as described above, please file a Jira for this matter."));
        }
        hostNameCheck(request, topology);
        request.setClusterId(clusterId);
        if (ambariContext.isTopologyResolved(clusterId)) {
            getOrCreateTopologyTaskExecutor(clusterId).start();
        }
        topology.update(request);
        final java.lang.Long requestId = ambariContext.getNextRequestId();
        org.apache.ambari.server.topology.LogicalRequest logicalRequest = org.apache.ambari.server.utils.RetryHelper.executeWithRetry(new java.util.concurrent.Callable<org.apache.ambari.server.topology.LogicalRequest>() {
            @java.lang.Override
            public org.apache.ambari.server.topology.LogicalRequest call() throws java.lang.Exception {
                org.apache.ambari.server.topology.LogicalRequest logicalRequest = processAndPersistTopologyRequest(request, topology, requestId);
                return logicalRequest;
            }
        });
        processRequest(request, topology, logicalRequest);
        return getRequestStatus(logicalRequest.getRequestId());
    }

    public void removePendingHostRequests(java.lang.String clusterName, long requestId) {
        ensureInitialized();
        org.apache.ambari.server.topology.TopologyManager.LOG.info("TopologyManager.removePendingHostRequests: Entering");
        long clusterId = 0;
        try {
            clusterId = ambariContext.getClusterId(clusterName);
        } catch (org.apache.ambari.server.AmbariException e) {
            org.apache.ambari.server.topology.TopologyManager.LOG.error("Unable to retrieve clusterId", e);
            throw new java.lang.IllegalArgumentException("Unable to retrieve clusterId");
        }
        org.apache.ambari.server.topology.ClusterTopology topology = clusterTopologyMap.get(clusterId);
        if (topology == null) {
            throw new java.lang.IllegalArgumentException("Unable to retrieve cluster topology for cluster");
        }
        org.apache.ambari.server.topology.LogicalRequest logicalRequest = allRequests.get(requestId);
        if (logicalRequest == null) {
            throw new java.lang.IllegalArgumentException("No Logical Request found for requestId: " + requestId);
        }
        java.util.Collection<org.apache.ambari.server.topology.HostRequest> pendingHostRequests = logicalRequest.removePendingHostRequests(null);
        if (!logicalRequest.hasPendingHostRequests()) {
            outstandingRequests.remove(logicalRequest);
        }
        if (logicalRequest.getHostRequests().isEmpty()) {
            allRequests.remove(requestId);
        }
        persistedState.removeHostRequests(requestId, pendingHostRequests);
        for (org.apache.ambari.server.topology.HostGroupInfo currentHostGroupInfo : topology.getHostGroupInfo().values()) {
            currentHostGroupInfo.setRequestedCount(currentHostGroupInfo.getHostNames().size());
        }
        org.apache.ambari.server.topology.TopologyManager.LOG.info("TopologyManager.removePendingHostRequests: Exit");
    }

    public void removeHostRequests(java.lang.String hostName) {
        ensureInitialized();
        for (java.util.Iterator<org.apache.ambari.server.topology.LogicalRequest> iter = allRequests.values().iterator(); iter.hasNext();) {
            org.apache.ambari.server.topology.LogicalRequest logicalRequest = iter.next();
            java.util.Collection<org.apache.ambari.server.topology.HostRequest> removed = logicalRequest.removeHostRequestByHostName(hostName);
            if (!logicalRequest.hasPendingHostRequests()) {
                outstandingRequests.remove(logicalRequest);
            }
            if (logicalRequest.getHostRequests().isEmpty()) {
                iter.remove();
            }
            if (!removed.isEmpty()) {
                persistedState.removeHostRequests(logicalRequest.getRequestId(), removed);
            }
        }
    }

    @com.google.inject.persist.Transactional
    protected org.apache.ambari.server.topology.LogicalRequest processAndPersistProvisionClusterTopologyRequest(org.apache.ambari.server.controller.internal.ProvisionClusterRequest request, org.apache.ambari.server.topology.ClusterTopology topology, java.lang.Long logicalRequestId) throws org.apache.ambari.server.topology.InvalidTopologyException, org.apache.ambari.server.AmbariException {
        if (null != request.getQuickLinksProfileJson()) {
            saveOrUpdateQuickLinksProfile(request.getQuickLinksProfileJson());
        }
        org.apache.ambari.server.topology.LogicalRequest logicalRequest = processAndPersistTopologyRequest(request, topology, logicalRequestId);
        return logicalRequest;
    }

    @com.google.inject.persist.Transactional
    protected org.apache.ambari.server.topology.LogicalRequest processAndPersistTopologyRequest(org.apache.ambari.server.controller.internal.BaseClusterRequest request, org.apache.ambari.server.topology.ClusterTopology topology, java.lang.Long logicalRequestId) throws org.apache.ambari.server.topology.InvalidTopologyException, org.apache.ambari.server.AmbariException {
        org.apache.ambari.server.topology.PersistedTopologyRequest persistedRequest = persistedState.persistTopologyRequest(request);
        org.apache.ambari.server.topology.LogicalRequest logicalRequest = createLogicalRequest(persistedRequest, topology, logicalRequestId);
        return logicalRequest;
    }

    private void hostNameCheck(org.apache.ambari.server.controller.internal.ScaleClusterRequest request, org.apache.ambari.server.topology.ClusterTopology topology) throws org.apache.ambari.server.topology.InvalidTopologyException {
        java.util.Set<java.lang.String> hostNames = new java.util.HashSet<>();
        for (java.util.Map.Entry<java.lang.String, org.apache.ambari.server.topology.HostGroupInfo> entry : request.getHostGroupInfo().entrySet()) {
            hostNames.addAll(entry.getValue().getHostNames());
        }
        for (java.lang.String hostName : hostNames) {
            if (topology.getHostGroupForHost(hostName) != null) {
                throw new org.apache.ambari.server.topology.InvalidTopologyException(("Host " + hostName) + " cannot be added, because it is already in the cluster");
            }
        }
    }

    public void onHostRegistered(org.apache.ambari.server.state.host.HostImpl host, boolean associatedWithCluster) {
        ensureInitialized();
        org.apache.ambari.server.topology.TopologyManager.LOG.info("TopologyManager.onHostRegistered: Entering");
        if (associatedWithCluster || isHostIgnored(host.getHostName())) {
            org.apache.ambari.server.topology.TopologyManager.LOG.info("TopologyManager.onHostRegistered: host = {} is already associated with the cluster or is currently being processed", host.getHostName());
            return;
        }
        boolean matchedToRequest = false;
        java.lang.String hostName = host.getHostName();
        synchronized(availableHosts) {
            synchronized(reservedHosts) {
                if (reservedHosts.containsKey(hostName)) {
                    org.apache.ambari.server.topology.LogicalRequest request = reservedHosts.remove(hostName);
                    org.apache.ambari.server.topology.HostOfferResponse response = request.offer(host);
                    if (response.getAnswer() != org.apache.ambari.server.topology.HostOfferResponse.Answer.ACCEPTED) {
                        throw new java.lang.RuntimeException("LogicalRequest declined host offer of explicitly requested host: " + hostName);
                    }
                    org.apache.ambari.server.topology.TopologyManager.LOG.info("TopologyManager.onHostRegistered: processing accepted host offer for reserved host = {}", hostName);
                    processAcceptedHostOffer(getClusterTopology(request.getClusterId()), response, host);
                    matchedToRequest = true;
                }
            }
            if (!matchedToRequest) {
                synchronized(outstandingRequests) {
                    java.util.Iterator<org.apache.ambari.server.topology.LogicalRequest> outstandingRequestIterator = outstandingRequests.iterator();
                    while ((!matchedToRequest) && outstandingRequestIterator.hasNext()) {
                        org.apache.ambari.server.topology.LogicalRequest request = outstandingRequestIterator.next();
                        org.apache.ambari.server.topology.HostOfferResponse hostOfferResponse = request.offer(host);
                        switch (hostOfferResponse.getAnswer()) {
                            case ACCEPTED :
                                matchedToRequest = true;
                                org.apache.ambari.server.topology.TopologyManager.LOG.info("TopologyManager.onHostRegistered: processing accepted host offer for matched host = {}", hostName);
                                processAcceptedHostOffer(getClusterTopology(request.getClusterId()), hostOfferResponse, host);
                                break;
                            case DECLINED_DONE :
                                org.apache.ambari.server.topology.TopologyManager.LOG.info("TopologyManager.onHostRegistered: DECLINED_DONE received for host = {}", hostName);
                                outstandingRequestIterator.remove();
                                break;
                            case DECLINED_PREDICATE :
                                org.apache.ambari.server.topology.TopologyManager.LOG.info("TopologyManager.onHostRegistered: DECLINED_PREDICATE received for host = {}", hostName);
                                break;
                        }
                    } 
                }
            }
            if (!matchedToRequest) {
                boolean addToAvailableList = true;
                for (org.apache.ambari.server.state.host.HostImpl registered : availableHosts) {
                    if (java.util.Objects.equals(registered.getHostId(), host.getHostId())) {
                        org.apache.ambari.server.topology.TopologyManager.LOG.info("Host {} re-registered, will not be added to the available hosts list", hostName);
                        addToAvailableList = false;
                        break;
                    }
                }
                if (addToAvailableList) {
                    org.apache.ambari.server.topology.TopologyManager.LOG.info("TopologyManager: Queueing available host {}", hostName);
                    availableHosts.add(host);
                }
            }
        }
    }

    public void onHostHeartBeatLost(org.apache.ambari.server.state.Host host) {
        if (org.apache.ambari.server.controller.AmbariServer.getController() == null) {
            return;
        }
        ensureInitialized();
        synchronized(availableHosts) {
            org.apache.ambari.server.topology.TopologyManager.LOG.info("Hearbeat for host {} lost thus removing it from available hosts.", host.getHostName());
            availableHosts.remove(host);
        }
    }

    public org.apache.ambari.server.topology.LogicalRequest getRequest(long requestId) {
        ensureInitialized();
        return allRequests.get(requestId);
    }

    public java.util.Collection<org.apache.ambari.server.topology.LogicalRequest> getRequests(java.util.Collection<java.lang.Long> requestIds) {
        ensureInitialized();
        if (requestIds.isEmpty()) {
            return allRequests.values();
        } else {
            java.util.Collection<org.apache.ambari.server.topology.LogicalRequest> matchingRequests = new java.util.ArrayList<>();
            for (long id : requestIds) {
                org.apache.ambari.server.topology.LogicalRequest request = allRequests.get(id);
                if (request != null) {
                    matchingRequests.add(request);
                }
            }
            return matchingRequests;
        }
    }

    public java.util.Collection<org.apache.ambari.server.orm.entities.StageEntity> getStages() {
        ensureInitialized();
        java.util.Collection<org.apache.ambari.server.orm.entities.StageEntity> stages = new java.util.ArrayList<>();
        for (org.apache.ambari.server.topology.LogicalRequest logicalRequest : allRequests.values()) {
            stages.addAll(logicalRequest.getStageEntities());
        }
        return stages;
    }

    public java.util.Collection<org.apache.ambari.server.actionmanager.HostRoleCommand> getTasks(long requestId) {
        ensureInitialized();
        org.apache.ambari.server.topology.LogicalRequest request = allRequests.get(requestId);
        return request == null ? java.util.Collections.emptyList() : request.getCommands();
    }

    public java.util.Collection<org.apache.ambari.server.actionmanager.HostRoleCommand> getTasks(java.util.Collection<java.lang.Long> requestIds) {
        ensureInitialized();
        java.util.Collection<org.apache.ambari.server.actionmanager.HostRoleCommand> tasks = new java.util.ArrayList<>();
        for (long id : requestIds) {
            tasks.addAll(getTasks(id));
        }
        return tasks;
    }

    public java.util.Map<java.lang.Long, org.apache.ambari.server.orm.dao.HostRoleCommandStatusSummaryDTO> getStageSummaries(java.lang.Long requestId) {
        ensureInitialized();
        org.apache.ambari.server.topology.LogicalRequest request = allRequests.get(requestId);
        return request == null ? java.util.Collections.emptyMap() : request.getStageSummaries();
    }

    public org.apache.ambari.server.controller.RequestStatusResponse getRequestStatus(long requestId) {
        ensureInitialized();
        org.apache.ambari.server.topology.LogicalRequest request = allRequests.get(requestId);
        return request == null ? null : request.getRequestStatus();
    }

    public java.util.Collection<org.apache.ambari.server.controller.RequestStatusResponse> getRequestStatus(java.util.Collection<java.lang.Long> ids) {
        ensureInitialized();
        java.util.List<org.apache.ambari.server.controller.RequestStatusResponse> requestStatusResponses = new java.util.ArrayList<>();
        for (long id : ids) {
            org.apache.ambari.server.controller.RequestStatusResponse response = getRequestStatus(id);
            if (response != null) {
                requestStatusResponses.add(response);
            }
        }
        return requestStatusResponses;
    }

    public org.apache.ambari.server.topology.ClusterTopology getClusterTopology(java.lang.Long clusterId) {
        ensureInitialized();
        return clusterTopologyMap.get(clusterId);
    }

    public java.util.Map<java.lang.String, java.util.Collection<java.lang.String>> getPendingHostComponents() {
        ensureInitialized();
        java.util.Map<java.lang.String, java.util.Collection<java.lang.String>> hostComponentMap = new java.util.HashMap<>();
        for (org.apache.ambari.server.topology.LogicalRequest logicalRequest : allRequests.values()) {
            java.util.Map<java.lang.Long, org.apache.ambari.server.orm.dao.HostRoleCommandStatusSummaryDTO> summary = logicalRequest.getStageSummaries();
            final org.apache.ambari.server.controller.internal.CalculatedStatus status = org.apache.ambari.server.controller.internal.CalculatedStatus.statusFromStageSummary(summary, summary.keySet());
            boolean logicalRequestInProgress = false;
            if (status.getStatus().isInProgress() || (summary.isEmpty() && (logicalRequest.getEndTime() <= 0))) {
                logicalRequestInProgress = true;
            }
            if (logicalRequestInProgress) {
                java.util.Map<java.lang.String, java.util.Collection<java.lang.String>> requestTopology = logicalRequest.getProjectedTopology();
                for (java.util.Map.Entry<java.lang.String, java.util.Collection<java.lang.String>> entry : requestTopology.entrySet()) {
                    java.lang.String host = entry.getKey();
                    java.util.Collection<java.lang.String> hostComponents = hostComponentMap.get(host);
                    if (hostComponents == null) {
                        hostComponents = new java.util.HashSet<>();
                        hostComponentMap.put(host, hostComponents);
                    }
                    hostComponents.addAll(entry.getValue());
                }
            }
        }
        return hostComponentMap;
    }

    private void processRequest(org.apache.ambari.server.topology.TopologyRequest request, org.apache.ambari.server.topology.ClusterTopology topology, final org.apache.ambari.server.topology.LogicalRequest logicalRequest) throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.server.topology.TopologyManager.LOG.info("TopologyManager.processRequest: Entering");
        finalizeTopology(request, topology);
        boolean requestHostComplete = false;
        synchronized(availableHosts) {
            java.util.Iterator<org.apache.ambari.server.state.host.HostImpl> hostIterator = availableHosts.iterator();
            while ((!requestHostComplete) && hostIterator.hasNext()) {
                org.apache.ambari.server.state.host.HostImpl host = hostIterator.next();
                synchronized(reservedHosts) {
                    java.lang.String hostname = host.getHostName();
                    if (reservedHosts.containsKey(hostname)) {
                        if (logicalRequest.equals(reservedHosts.get(hostname))) {
                            org.apache.ambari.server.topology.TopologyManager.LOG.info("TopologyManager.processRequest: host name = {} is mapped to LogicalRequest ID = {} and will be removed from the reserved hosts.", hostname, logicalRequest.getRequestId());
                            reservedHosts.remove(hostname);
                        } else {
                            org.apache.ambari.server.topology.TopologyManager.LOG.info("TopologyManager.processRequest: host name = {} is registered with another request, and will not be offered to LogicalRequest ID = {}", hostname, logicalRequest.getRequestId());
                            continue;
                        }
                    }
                }
                org.apache.ambari.server.topology.TopologyManager.LOG.info("TopologyManager.processRequest: offering host name = {} to LogicalRequest ID = {}", host.getHostName(), logicalRequest.getRequestId());
                org.apache.ambari.server.topology.HostOfferResponse response = logicalRequest.offer(host);
                switch (response.getAnswer()) {
                    case ACCEPTED :
                        hostIterator.remove();
                        org.apache.ambari.server.topology.TopologyManager.LOG.info("TopologyManager.processRequest: host name = {} was ACCEPTED by LogicalRequest ID = {} , host has been removed from available hosts.", host.getHostName(), logicalRequest.getRequestId());
                        processAcceptedHostOffer(getClusterTopology(logicalRequest.getClusterId()), response, host);
                        break;
                    case DECLINED_DONE :
                        requestHostComplete = true;
                        org.apache.ambari.server.topology.TopologyManager.LOG.info("TopologyManager.processRequest: host name = {} was DECLINED_DONE by LogicalRequest ID = {}", host.getHostName(), logicalRequest.getRequestId());
                        break;
                    case DECLINED_PREDICATE :
                        org.apache.ambari.server.topology.TopologyManager.LOG.info("TopologyManager.processRequest: host name = {} was DECLINED_PREDICATE by LogicalRequest ID = {}", host.getHostName(), logicalRequest.getRequestId());
                        break;
                }
            } 
            if (!requestHostComplete) {
                org.apache.ambari.server.topology.TopologyManager.LOG.info("TopologyManager.processRequest: not all required hosts have been matched, so adding LogicalRequest ID = {} to outstanding requests", logicalRequest.getRequestId());
                synchronized(outstandingRequests) {
                    outstandingRequests.add(logicalRequest);
                }
            }
        }
    }

    @com.google.inject.persist.Transactional
    protected org.apache.ambari.server.topology.LogicalRequest createLogicalRequest(final org.apache.ambari.server.topology.PersistedTopologyRequest request, org.apache.ambari.server.topology.ClusterTopology topology, java.lang.Long requestId) throws org.apache.ambari.server.AmbariException {
        final org.apache.ambari.server.topology.LogicalRequest logicalRequest = logicalRequestFactory.createRequest(requestId, request.getRequest(), topology);
        persistedState.persistLogicalRequest(logicalRequest, request.getId());
        allRequests.put(logicalRequest.getRequestId(), logicalRequest);
        org.apache.ambari.server.topology.TopologyManager.LOG.info("TopologyManager.createLogicalRequest: created LogicalRequest with ID = {} and completed persistence of this request.", logicalRequest.getRequestId());
        synchronized(reservedHosts) {
            for (java.lang.String host : logicalRequest.getReservedHosts()) {
                reservedHosts.put(host, logicalRequest);
            }
        }
        return logicalRequest;
    }

    private void processAcceptedHostOffer(final org.apache.ambari.server.topology.ClusterTopology topology, final org.apache.ambari.server.topology.HostOfferResponse response, final org.apache.ambari.server.state.host.HostImpl host) {
        final java.lang.String hostName = host.getHostName();
        try {
            topology.addHostToTopology(response.getHostGroupName(), hostName);
            updateHostWithRackInfo(topology, response, host);
        } catch (org.apache.ambari.server.topology.InvalidTopologyException | org.apache.ambari.server.topology.NoSuchHostGroupException e) {
            throw new java.lang.RuntimeException("An internal error occurred while performing request host registration: " + e, e);
        }
        try {
            org.apache.ambari.server.utils.RetryHelper.executeWithRetry(new java.util.concurrent.Callable<java.lang.Void>() {
                @java.lang.Override
                public java.lang.Void call() throws java.lang.Exception {
                    persistTopologyHostRegistration(response.getHostRequestId(), host);
                    return null;
                }
            });
        } catch (org.apache.ambari.server.AmbariException e) {
            org.apache.ambari.server.topology.TopologyManager.LOG.error("Exception ocurred while registering host name", e);
            throw new java.lang.RuntimeException(e);
        }
        org.apache.ambari.server.topology.TopologyManager.LOG.info("TopologyManager.processAcceptedHostOffer: queue tasks for host = {} which responded {}", hostName, response.getAnswer());
        queueHostTasks(topology, response, hostName);
    }

    @com.google.inject.persist.Transactional
    protected void persistTopologyHostRegistration(long hostRequestId, final org.apache.ambari.server.state.host.HostImpl host) {
        persistedState.registerHostName(hostRequestId, host.getHostName());
        persistedState.registerInTopologyHostInfo(host);
    }

    private org.apache.ambari.server.utils.ManagedThreadPoolExecutor getOrCreateTopologyTaskExecutor(java.lang.Long clusterId) {
        org.apache.ambari.server.utils.ManagedThreadPoolExecutor topologyTaskExecutor = this.topologyTaskExecutorServiceMap.get(clusterId);
        if (topologyTaskExecutor == null) {
            org.apache.ambari.server.topology.TopologyManager.LOG.info("Creating TopologyTaskExecutorService for clusterId: {}", clusterId);
            topologyTaskExecutor = new org.apache.ambari.server.utils.ManagedThreadPoolExecutor(topologyTaskExecutorThreadPoolSize, topologyTaskExecutorThreadPoolSize, 0L, java.util.concurrent.TimeUnit.MILLISECONDS, new java.util.concurrent.LinkedBlockingQueue<java.lang.Runnable>());
            topologyTaskExecutorServiceMap.put(clusterId, topologyTaskExecutor);
        }
        return topologyTaskExecutor;
    }

    private void queueHostTasks(org.apache.ambari.server.topology.ClusterTopology topology, org.apache.ambari.server.topology.HostOfferResponse response, java.lang.String hostName) {
        org.apache.ambari.server.topology.TopologyManager.LOG.info("TopologyManager.processAcceptedHostOffer: queueing tasks for host = {}", hostName);
        java.util.concurrent.ExecutorService executorService = getOrCreateTopologyTaskExecutor(topology.getClusterId());
        response.executeTasks(executorService, hostName, topology, ambariContext);
    }

    private void updateHostWithRackInfo(org.apache.ambari.server.topology.ClusterTopology topology, org.apache.ambari.server.topology.HostOfferResponse response, org.apache.ambari.server.state.host.HostImpl host) {
        java.lang.String rackInfoFromTemplate = topology.getHostGroupInfo().get(response.getHostGroupName()).getHostRackInfo().get(host.getHostName());
        if (null != rackInfoFromTemplate) {
            host.setRackInfo(rackInfoFromTemplate);
            try {
                ambariContext.getController().registerRackChange(ambariContext.getClusterName(topology.getClusterId()));
            } catch (org.apache.ambari.server.AmbariException e) {
                org.apache.ambari.server.topology.TopologyManager.LOG.error("Could not register rack change for cluster id {}", topology.getClusterId());
                org.apache.ambari.server.topology.TopologyManager.LOG.error("Exception during rack change: ", e);
            }
        }
    }

    private void replayRequests(java.util.Map<org.apache.ambari.server.topology.ClusterTopology, java.util.List<org.apache.ambari.server.topology.LogicalRequest>> persistedRequests) {
        org.apache.ambari.server.topology.TopologyManager.LOG.info("TopologyManager.replayRequests: Entering");
        boolean configChecked = false;
        for (java.util.Map.Entry<org.apache.ambari.server.topology.ClusterTopology, java.util.List<org.apache.ambari.server.topology.LogicalRequest>> requestEntry : persistedRequests.entrySet()) {
            org.apache.ambari.server.topology.ClusterTopology topology = requestEntry.getKey();
            clusterTopologyMap.put(topology.getClusterId(), topology);
            org.apache.ambari.server.topology.LogicalRequest provisionRequest = persistedState.getProvisionRequest(topology.getClusterId());
            if (provisionRequest != null) {
                clusterProvisionWithBlueprintCreateRequests.put(topology.getClusterId(), provisionRequest);
                clusterProvisionWithBlueprintCreationFinished.put(topology.getClusterId(), isLogicalRequestFinished(clusterProvisionWithBlueprintCreateRequests.get(topology.getClusterId())));
            }
            for (org.apache.ambari.server.topology.LogicalRequest logicalRequest : requestEntry.getValue()) {
                allRequests.put(logicalRequest.getRequestId(), logicalRequest);
                if (logicalRequest.hasPendingHostRequests()) {
                    outstandingRequests.add(logicalRequest);
                    for (java.lang.String reservedHost : logicalRequest.getReservedHosts()) {
                        reservedHosts.put(reservedHost, logicalRequest);
                    }
                    for (org.apache.ambari.server.topology.HostRequest hostRequest : logicalRequest.getCompletedHostRequests()) {
                        try {
                            java.lang.String hostName = hostRequest.getHostName();
                            topology.addHostToTopology(hostRequest.getHostgroupName(), hostName);
                            hostsToIgnore.add(hostName);
                            org.apache.ambari.server.topology.TopologyManager.LOG.info("TopologyManager.replayRequests: host name = {} has been added to cluster and to ignore list.", hostName);
                        } catch (org.apache.ambari.server.topology.InvalidTopologyException e) {
                            org.apache.ambari.server.topology.TopologyManager.LOG.warn("Attempted to add host to multiple host groups while replaying requests: " + e, e);
                        } catch (org.apache.ambari.server.topology.NoSuchHostGroupException e) {
                            org.apache.ambari.server.topology.TopologyManager.LOG.warn("Failed to add host to topology while replaying requests: " + e, e);
                        }
                    }
                }
            }
            if (!configChecked) {
                configChecked = true;
                if (!ambariContext.isTopologyResolved(topology.getClusterId())) {
                    if (provisionRequest == null) {
                        org.apache.ambari.server.topology.TopologyManager.LOG.info("TopologyManager.replayRequests: no config with TOPOLOGY_RESOLVED found, but provision request missing, skipping cluster config request");
                    } else if (provisionRequest.isFinished()) {
                        org.apache.ambari.server.topology.TopologyManager.LOG.info("TopologyManager.replayRequests: no config with TOPOLOGY_RESOLVED found, but provision request is finished, skipping cluster config request");
                    } else {
                        org.apache.ambari.server.topology.TopologyManager.LOG.info("TopologyManager.replayRequests: no config with TOPOLOGY_RESOLVED found, adding cluster config request");
                        org.apache.ambari.server.topology.ClusterConfigurationRequest configRequest = new org.apache.ambari.server.topology.ClusterConfigurationRequest(ambariContext, topology, false, stackAdvisorBlueprintProcessor);
                        addClusterConfigRequest(provisionRequest, topology, configRequest);
                    }
                } else {
                    getOrCreateTopologyTaskExecutor(topology.getClusterId()).start();
                }
            }
        }
        org.apache.ambari.server.topology.TopologyManager.LOG.info("TopologyManager.replayRequests: Exit");
    }

    private boolean isLogicalRequestFinished(org.apache.ambari.server.topology.LogicalRequest logicalRequest) {
        return (logicalRequest != null) && logicalRequest.isFinished();
    }

    private boolean isLogicalRequestSuccessful(org.apache.ambari.server.topology.LogicalRequest logicalRequest) {
        return (logicalRequest != null) && logicalRequest.isSuccessful();
    }

    private void finalizeTopology(org.apache.ambari.server.topology.TopologyRequest request, org.apache.ambari.server.topology.ClusterTopology topology) {
    }

    private boolean isHostIgnored(java.lang.String host) {
        return hostsToIgnore.remove(host);
    }

    private void addKerberosClient(org.apache.ambari.server.topology.ClusterTopology topology) {
        for (org.apache.ambari.server.topology.HostGroup group : topology.getBlueprint().getHostGroups().values()) {
            group.addComponent("KERBEROS_CLIENT");
        }
    }

    private void addClusterConfigRequest(final org.apache.ambari.server.topology.LogicalRequest logicalRequest, org.apache.ambari.server.topology.ClusterTopology topology, org.apache.ambari.server.topology.ClusterConfigurationRequest configurationRequest) {
        org.apache.ambari.server.topology.tasks.ConfigureClusterTask task = configureClusterTaskFactory.createConfigureClusterTask(topology, configurationRequest, ambariEventPublisher);
        executor.submit(new org.apache.ambari.server.topology.AsyncCallableService<>(task, task.getTimeout(), task.getRepeatDelay(), "ConfigureClusterTask", throwable -> {
            org.apache.ambari.server.actionmanager.HostRoleStatus status = (throwable instanceof java.util.concurrent.TimeoutException) ? org.apache.ambari.server.actionmanager.HostRoleStatus.TIMEDOUT : org.apache.ambari.server.actionmanager.HostRoleStatus.FAILED;
            org.apache.ambari.server.topology.TopologyManager.LOG.info("ConfigureClusterTask failed, marking host requests {}", status);
            for (org.apache.ambari.server.topology.HostRequest hostRequest : logicalRequest.getHostRequests()) {
                hostRequest.markHostRequestFailed(status, throwable, persistedState);
            }
        }));
    }

    @com.google.common.eventbus.Subscribe
    public void processHostRemovedEvent(org.apache.ambari.server.events.HostsRemovedEvent hostsRemovedEvent) {
        if (hostsRemovedEvent.getHostNames().isEmpty()) {
            org.apache.ambari.server.topology.TopologyManager.LOG.warn("Missing host name from host removed event [{}] !", hostsRemovedEvent);
            return;
        }
        org.apache.ambari.server.topology.TopologyManager.LOG.info("Removing hosts [{}] from available hosts on hosts removed event.", hostsRemovedEvent.getHostNames());
        java.util.Set<org.apache.ambari.server.state.host.HostImpl> toBeRemoved = new java.util.HashSet<>();
        synchronized(availableHosts) {
            for (org.apache.ambari.server.state.host.HostImpl hostImpl : availableHosts) {
                for (java.lang.String hostName : hostsRemovedEvent.getHostNames()) {
                    if (hostName.equals(hostImpl.getHostName())) {
                        toBeRemoved.add(hostImpl);
                        break;
                    }
                }
            }
            if (!toBeRemoved.isEmpty()) {
                for (org.apache.ambari.server.state.host.HostImpl host : toBeRemoved) {
                    availableHosts.remove(host);
                    org.apache.ambari.server.topology.TopologyManager.LOG.info("Removed host: [{}] from available hosts", host.getHostName());
                }
            } else {
                org.apache.ambari.server.topology.TopologyManager.LOG.debug("No any host [{}] found in available hosts", hostsRemovedEvent.getHostNames());
            }
        }
    }
}