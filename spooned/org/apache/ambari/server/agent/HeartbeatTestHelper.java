package org.apache.ambari.server.agent;
import com.google.inject.persist.UnitOfWork;
import org.easymock.EasyMock;
import static org.apache.ambari.server.agent.DummyHeartbeatConstants.DummyCluster;
import static org.apache.ambari.server.agent.DummyHeartbeatConstants.DummyClusterId;
import static org.apache.ambari.server.agent.DummyHeartbeatConstants.DummyHostname1;
import static org.apache.ambari.server.agent.DummyHeartbeatConstants.DummyOSRelease;
import static org.apache.ambari.server.agent.DummyHeartbeatConstants.DummyOs;
import static org.apache.ambari.server.agent.DummyHeartbeatConstants.DummyRepositoryVersion;
import static org.apache.ambari.server.agent.DummyHeartbeatConstants.DummyStackId;
import static org.apache.ambari.server.agent.DummyHeartbeatConstants.HBASE;
@com.google.inject.Singleton
public class HeartbeatTestHelper {
    @com.google.inject.Inject
    org.apache.ambari.server.state.Clusters clusters;

    @com.google.inject.Inject
    org.apache.ambari.server.configuration.Configuration configuration;

    @com.google.inject.Inject
    com.google.inject.Injector injector;

    @com.google.inject.Inject
    org.apache.ambari.server.api.services.AmbariMetaInfo metaInfo;

    @com.google.inject.Inject
    org.apache.ambari.server.actionmanager.ActionDBAccessor actionDBAccessor;

    @com.google.inject.Inject
    org.apache.ambari.server.orm.dao.ClusterDAO clusterDAO;

    @com.google.inject.Inject
    org.apache.ambari.server.orm.dao.StackDAO stackDAO;

    @com.google.inject.Inject
    com.google.inject.persist.UnitOfWork unitOfWork;

    @com.google.inject.Inject
    org.apache.ambari.server.orm.dao.ResourceTypeDAO resourceTypeDAO;

    @com.google.inject.Inject
    org.apache.ambari.server.orm.OrmTestHelper helper;

    @com.google.inject.Inject
    private org.apache.ambari.server.orm.dao.HostDAO hostDAO;

    @com.google.inject.Inject
    private org.apache.ambari.server.actionmanager.StageFactory stageFactory;

    public static final org.apache.ambari.server.state.StackId HDP_22_STACK = new org.apache.ambari.server.state.StackId("HDP", "2.2.0");

    public static org.apache.ambari.server.orm.InMemoryDefaultTestModule getTestModule() {
        return new org.apache.ambari.server.orm.InMemoryDefaultTestModule() {
            @java.lang.Override
            protected void configure() {
                super.configure();
                binder().bind(org.apache.ambari.server.events.publishers.STOMPUpdatePublisher.class).toInstance(org.easymock.EasyMock.createNiceMock(org.apache.ambari.server.events.publishers.STOMPUpdatePublisher.class));
            }
        };
    }

    public org.apache.ambari.server.agent.HeartBeatHandler getHeartBeatHandler(org.apache.ambari.server.actionmanager.ActionManager am) throws org.apache.ambari.server.state.fsm.InvalidStateTransitionException, org.apache.ambari.server.AmbariException {
        org.apache.ambari.server.agent.HeartBeatHandler handler = new org.apache.ambari.server.agent.HeartBeatHandler(configuration, clusters, am, org.apache.ambari.server.security.encryption.Encryptor.NONE, injector);
        org.apache.ambari.server.agent.Register reg = new org.apache.ambari.server.agent.Register();
        org.apache.ambari.server.agent.HostInfo hi = new org.apache.ambari.server.agent.HostInfo();
        hi.setHostName(org.apache.ambari.server.agent.DummyHeartbeatConstants.DummyHostname1);
        hi.setOS(org.apache.ambari.server.agent.DummyHeartbeatConstants.DummyOs);
        hi.setOSRelease(org.apache.ambari.server.agent.DummyHeartbeatConstants.DummyOSRelease);
        reg.setHostname(org.apache.ambari.server.agent.DummyHeartbeatConstants.DummyHostname1);
        reg.setResponseId(0);
        reg.setHardwareProfile(hi);
        reg.setAgentVersion(metaInfo.getServerVersion());
        handler.handleRegistration(reg);
        return handler;
    }

    public org.apache.ambari.server.state.Cluster getDummyCluster() throws java.lang.Exception {
        java.util.Map<java.lang.String, java.lang.String> configProperties = new java.util.HashMap<java.lang.String, java.lang.String>() {
            {
                put(org.apache.ambari.server.agent.RecoveryConfigHelper.RECOVERY_ENABLED_KEY, "true");
                put(org.apache.ambari.server.agent.RecoveryConfigHelper.RECOVERY_TYPE_KEY, "AUTO_START");
                put(org.apache.ambari.server.agent.RecoveryConfigHelper.RECOVERY_MAX_COUNT_KEY, "4");
                put(org.apache.ambari.server.agent.RecoveryConfigHelper.RECOVERY_LIFETIME_MAX_COUNT_KEY, "10");
                put(org.apache.ambari.server.agent.RecoveryConfigHelper.RECOVERY_WINDOW_IN_MIN_KEY, "23");
                put(org.apache.ambari.server.agent.RecoveryConfigHelper.RECOVERY_RETRY_GAP_KEY, "2");
            }
        };
        java.util.Set<java.lang.String> hostNames = new java.util.HashSet<java.lang.String>() {
            {
                add(org.apache.ambari.server.agent.DummyHeartbeatConstants.DummyHostname1);
            }
        };
        return getDummyCluster(org.apache.ambari.server.agent.DummyHeartbeatConstants.DummyCluster, new java.lang.Long(org.apache.ambari.server.agent.DummyHeartbeatConstants.DummyClusterId), new org.apache.ambari.server.state.StackId(org.apache.ambari.server.agent.DummyHeartbeatConstants.DummyStackId), org.apache.ambari.server.agent.DummyHeartbeatConstants.DummyRepositoryVersion, configProperties, hostNames);
    }

    public org.apache.ambari.server.state.Cluster getDummyCluster(java.lang.String clusterName, java.lang.Long clusterId, org.apache.ambari.server.state.StackId stackId, java.lang.String repositoryVersion, java.util.Map<java.lang.String, java.lang.String> configProperties, java.util.Set<java.lang.String> hostNames) throws java.lang.Exception {
        org.apache.ambari.server.orm.entities.StackEntity stackEntity = stackDAO.find(stackId.getStackName(), stackId.getStackVersion());
        org.junit.Assert.assertNotNull(stackEntity);
        org.apache.ambari.server.orm.entities.ResourceTypeEntity resourceTypeEntity = new org.apache.ambari.server.orm.entities.ResourceTypeEntity();
        resourceTypeEntity.setId(org.apache.ambari.server.security.authorization.ResourceType.CLUSTER.getId());
        resourceTypeEntity.setName(org.apache.ambari.server.security.authorization.ResourceType.CLUSTER.name());
        resourceTypeEntity = resourceTypeDAO.merge(resourceTypeEntity);
        org.apache.ambari.server.orm.entities.ResourceEntity resourceEntity = new org.apache.ambari.server.orm.entities.ResourceEntity();
        resourceEntity.setResourceType(resourceTypeEntity);
        org.apache.ambari.server.orm.entities.ClusterEntity clusterEntity = new org.apache.ambari.server.orm.entities.ClusterEntity();
        clusterEntity.setClusterName(clusterName);
        clusterEntity.setClusterId(clusterId);
        clusterEntity.setClusterInfo("test_cluster_info1");
        clusterEntity.setResource(resourceEntity);
        clusterEntity.setDesiredStack(stackEntity);
        clusterDAO.create(clusterEntity);
        org.apache.ambari.server.state.Clusters clusters = injector.getInstance(org.apache.ambari.server.state.Clusters.class);
        java.lang.reflect.Method method = org.apache.ambari.server.state.cluster.ClustersImpl.class.getDeclaredMethod("safelyLoadClustersAndHosts");
        method.setAccessible(true);
        method.invoke(clusters);
        org.apache.ambari.server.state.Cluster cluster = clusters.getCluster(clusterName);
        cluster.setDesiredStackVersion(stackId);
        cluster.setCurrentStackVersion(stackId);
        org.apache.ambari.server.state.ConfigFactory cf = injector.getInstance(org.apache.ambari.server.state.ConfigFactory.class);
        org.apache.ambari.server.state.Config config = cf.createNew(cluster, "cluster-env", "version1", configProperties, new java.util.HashMap<>());
        cluster.addDesiredConfig("user", java.util.Collections.singleton(config));
        java.util.Map<java.lang.String, java.lang.String> hostAttributes = new java.util.HashMap<>();
        hostAttributes.put("os_family", "redhat");
        hostAttributes.put("os_release_version", "6.3");
        java.util.List<org.apache.ambari.server.orm.entities.HostEntity> hostEntities = new java.util.ArrayList<>();
        for (java.lang.String hostName : hostNames) {
            clusters.addHost(hostName);
            org.apache.ambari.server.state.Host host = clusters.getHost(hostName);
            host.setHostAttributes(hostAttributes);
            org.apache.ambari.server.orm.entities.HostEntity hostEntity = hostDAO.findByName(hostName);
            junit.framework.Assert.assertNotNull(hostEntity);
            hostEntities.add(hostEntity);
        }
        clusterEntity.setHostEntities(hostEntities);
        clusters.mapAndPublishHostsToCluster(hostNames, clusterName);
        return cluster;
    }

    public void populateActionDB(org.apache.ambari.server.actionmanager.ActionDBAccessor db, java.lang.String DummyHostname1, long requestId, long stageId) throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.server.actionmanager.Stage s = stageFactory.createNew(requestId, "/a/b", org.apache.ambari.server.agent.DummyHeartbeatConstants.DummyCluster, 1L, "heartbeat handler test", "commandParamsStage", "hostParamsStage");
        s.setStageId(stageId);
        java.lang.String filename = null;
        s.addHostRoleExecutionCommand(DummyHostname1, org.apache.ambari.server.Role.HBASE_MASTER, org.apache.ambari.server.RoleCommand.START, new org.apache.ambari.server.state.svccomphost.ServiceComponentHostStartEvent(org.apache.ambari.server.Role.HBASE_MASTER.toString(), DummyHostname1, java.lang.System.currentTimeMillis()), org.apache.ambari.server.agent.DummyHeartbeatConstants.DummyCluster, org.apache.ambari.server.agent.DummyHeartbeatConstants.HBASE, false, false);
        java.util.List<org.apache.ambari.server.actionmanager.Stage> stages = new java.util.ArrayList<>();
        stages.add(s);
        org.apache.ambari.server.actionmanager.Request request = new org.apache.ambari.server.actionmanager.Request(stages, "clusterHostInfo", clusters);
        db.persistActions(request);
    }
}