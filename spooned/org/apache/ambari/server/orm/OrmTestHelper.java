package org.apache.ambari.server.orm;
import com.google.inject.persist.Transactional;
import javax.persistence.EntityManager;
import org.springframework.security.crypto.password.PasswordEncoder;
@com.google.inject.Singleton
public class OrmTestHelper {
    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.orm.OrmTestHelper.class);

    private java.util.concurrent.atomic.AtomicInteger uniqueCounter = new java.util.concurrent.atomic.AtomicInteger();

    @com.google.inject.Inject
    public com.google.inject.Provider<javax.persistence.EntityManager> entityManagerProvider;

    @com.google.inject.Inject
    public com.google.inject.Injector injector;

    @com.google.inject.Inject
    public org.apache.ambari.server.orm.dao.UserDAO userDAO;

    @com.google.inject.Inject
    public org.apache.ambari.server.orm.dao.AlertDefinitionDAO alertDefinitionDAO;

    @com.google.inject.Inject
    public org.apache.ambari.server.orm.dao.AlertDispatchDAO alertDispatchDAO;

    @com.google.inject.Inject
    public org.apache.ambari.server.orm.dao.AlertsDAO alertsDAO;

    @com.google.inject.Inject
    public org.apache.ambari.server.orm.dao.RepositoryVersionDAO repositoryVersionDAO;

    @com.google.inject.Inject
    public org.apache.ambari.server.orm.dao.HostVersionDAO hostVersionDAO;

    @com.google.inject.Inject
    public org.apache.ambari.server.orm.dao.HostDAO hostDAO;

    @com.google.inject.Inject
    private org.apache.ambari.server.orm.dao.StackDAO stackDAO;

    public static final java.lang.String CLUSTER_NAME = "test_cluster1";

    public javax.persistence.EntityManager getEntityManager() {
        return entityManagerProvider.get();
    }

    @com.google.inject.persist.Transactional
    public void createDefaultData() {
        org.apache.ambari.server.orm.entities.StackEntity stackEntity = stackDAO.find("HDP", "2.2.0");
        org.apache.ambari.server.orm.entities.ResourceTypeEntity resourceTypeEntity = new org.apache.ambari.server.orm.entities.ResourceTypeEntity();
        resourceTypeEntity.setId(org.apache.ambari.server.security.authorization.ResourceType.CLUSTER.getId());
        resourceTypeEntity.setName(org.apache.ambari.server.security.authorization.ResourceType.CLUSTER.name());
        org.apache.ambari.server.orm.entities.ResourceEntity resourceEntity = new org.apache.ambari.server.orm.entities.ResourceEntity();
        resourceEntity.setResourceType(resourceTypeEntity);
        org.apache.ambari.server.orm.entities.ClusterEntity clusterEntity = new org.apache.ambari.server.orm.entities.ClusterEntity();
        clusterEntity.setClusterName("test_cluster1");
        clusterEntity.setResource(resourceEntity);
        clusterEntity.setClusterInfo("test_cluster_info1");
        clusterEntity.setDesiredStack(stackEntity);
        org.apache.ambari.server.orm.entities.HostEntity host1 = new org.apache.ambari.server.orm.entities.HostEntity();
        org.apache.ambari.server.orm.entities.HostEntity host2 = new org.apache.ambari.server.orm.entities.HostEntity();
        org.apache.ambari.server.orm.entities.HostEntity host3 = new org.apache.ambari.server.orm.entities.HostEntity();
        host1.setHostName("test_host1");
        host2.setHostName("test_host2");
        host3.setHostName("test_host3");
        host1.setIpv4("192.168.0.1");
        host2.setIpv4("192.168.0.2");
        host3.setIpv4("192.168.0.3");
        java.util.List<org.apache.ambari.server.orm.entities.HostEntity> hostEntities = new java.util.ArrayList<>();
        hostEntities.add(host1);
        hostEntities.add(host2);
        clusterEntity.setHostEntities(hostEntities);
        host1.setClusterEntities(java.util.Arrays.asList(clusterEntity));
        host2.setClusterEntities(java.util.Arrays.asList(clusterEntity));
        org.apache.ambari.server.orm.entities.HostStateEntity hostStateEntity1 = new org.apache.ambari.server.orm.entities.HostStateEntity();
        hostStateEntity1.setCurrentState(org.apache.ambari.server.state.HostState.HEARTBEAT_LOST);
        hostStateEntity1.setHostEntity(host1);
        org.apache.ambari.server.orm.entities.HostStateEntity hostStateEntity2 = new org.apache.ambari.server.orm.entities.HostStateEntity();
        hostStateEntity2.setCurrentState(org.apache.ambari.server.state.HostState.HEALTHY);
        hostStateEntity2.setHostEntity(host2);
        host1.setHostStateEntity(hostStateEntity1);
        host2.setHostStateEntity(hostStateEntity2);
        org.apache.ambari.server.orm.entities.ClusterServiceEntity clusterServiceEntity = new org.apache.ambari.server.orm.entities.ClusterServiceEntity();
        clusterServiceEntity.setServiceName("HDFS");
        clusterServiceEntity.setClusterEntity(clusterEntity);
        java.util.List<org.apache.ambari.server.orm.entities.ClusterServiceEntity> clusterServiceEntities = new java.util.ArrayList<>();
        clusterServiceEntities.add(clusterServiceEntity);
        clusterEntity.setClusterServiceEntities(clusterServiceEntities);
        getEntityManager().persist(host1);
        getEntityManager().persist(host2);
        getEntityManager().persist(resourceTypeEntity);
        getEntityManager().persist(resourceEntity);
        getEntityManager().persist(clusterEntity);
        getEntityManager().persist(hostStateEntity1);
        getEntityManager().persist(hostStateEntity2);
        getEntityManager().persist(clusterServiceEntity);
    }

    @com.google.inject.persist.Transactional
    public void createTestUsers() {
        org.apache.ambari.server.orm.entities.PrincipalTypeEntity principalTypeEntity = new org.apache.ambari.server.orm.entities.PrincipalTypeEntity();
        principalTypeEntity.setName(org.apache.ambari.server.orm.entities.PrincipalTypeEntity.USER_PRINCIPAL_TYPE_NAME);
        getEntityManager().persist(principalTypeEntity);
        org.apache.ambari.server.orm.entities.PrincipalEntity principalEntity = new org.apache.ambari.server.orm.entities.PrincipalEntity();
        principalEntity.setPrincipalType(principalTypeEntity);
        getEntityManager().persist(principalEntity);
        org.springframework.security.crypto.password.PasswordEncoder encoder = injector.getInstance(org.springframework.security.crypto.password.PasswordEncoder.class);
        org.apache.ambari.server.orm.entities.UserEntity admin = new org.apache.ambari.server.orm.entities.UserEntity();
        admin.setUserName(org.apache.ambari.server.security.authorization.UserName.fromString("administrator").toString());
        admin.setPrincipal(principalEntity);
        java.util.Set<org.apache.ambari.server.orm.entities.UserEntity> users = new java.util.HashSet<>();
        users.add(admin);
        userDAO.create(admin);
        principalEntity = new org.apache.ambari.server.orm.entities.PrincipalEntity();
        principalEntity.setPrincipalType(principalTypeEntity);
        getEntityManager().persist(principalEntity);
        org.apache.ambari.server.orm.entities.UserEntity userWithoutRoles = new org.apache.ambari.server.orm.entities.UserEntity();
        userWithoutRoles.setUserName(org.apache.ambari.server.security.authorization.UserName.fromString("userWithoutRoles").toString());
        userWithoutRoles.setPrincipal(principalEntity);
        userDAO.create(userWithoutRoles);
    }

    @com.google.inject.persist.Transactional
    public void performTransactionMarkedForRollback() {
        org.apache.ambari.server.orm.dao.ClusterDAO clusterDAO = injector.getInstance(org.apache.ambari.server.orm.dao.ClusterDAO.class);
        clusterDAO.removeByName("test_cluster1");
        getEntityManager().getTransaction().setRollbackOnly();
    }

    @com.google.inject.persist.Transactional
    public void createStageCommands() {
        org.apache.ambari.server.orm.dao.ClusterDAO clusterDAO = injector.getInstance(org.apache.ambari.server.orm.dao.ClusterDAO.class);
        org.apache.ambari.server.orm.dao.StageDAO stageDAO = injector.getInstance(org.apache.ambari.server.orm.dao.StageDAO.class);
        org.apache.ambari.server.orm.dao.HostRoleCommandDAO hostRoleCommandDAO = injector.getInstance(org.apache.ambari.server.orm.dao.HostRoleCommandDAO.class);
        org.apache.ambari.server.orm.dao.HostDAO hostDAO = injector.getInstance(org.apache.ambari.server.orm.dao.HostDAO.class);
        org.apache.ambari.server.orm.dao.RequestDAO requestDAO = injector.getInstance(org.apache.ambari.server.orm.dao.RequestDAO.class);
        org.apache.ambari.server.orm.entities.RequestEntity requestEntity = new org.apache.ambari.server.orm.entities.RequestEntity();
        requestEntity.setRequestId(1L);
        requestEntity.setClusterId(clusterDAO.findByName("test_cluster1").getClusterId());
        org.apache.ambari.server.orm.entities.StageEntity stageEntity = new org.apache.ambari.server.orm.entities.StageEntity();
        stageEntity.setRequest(requestEntity);
        stageEntity.setClusterId(clusterDAO.findByName("test_cluster1").getClusterId());
        stageEntity.setRequestId(1L);
        stageEntity.setStageId(1L);
        requestEntity.setStages(java.util.Collections.singletonList(stageEntity));
        org.apache.ambari.server.orm.entities.HostRoleCommandEntity commandEntity = new org.apache.ambari.server.orm.entities.HostRoleCommandEntity();
        org.apache.ambari.server.orm.entities.HostRoleCommandEntity commandEntity2 = new org.apache.ambari.server.orm.entities.HostRoleCommandEntity();
        org.apache.ambari.server.orm.entities.HostRoleCommandEntity commandEntity3 = new org.apache.ambari.server.orm.entities.HostRoleCommandEntity();
        org.apache.ambari.server.orm.entities.HostEntity host1 = hostDAO.findByName("test_host1");
        org.apache.ambari.server.orm.entities.HostEntity host2 = hostDAO.findByName("test_host2");
        commandEntity.setHostEntity(host1);
        host1.getHostRoleCommandEntities().add(commandEntity);
        commandEntity.setRoleCommand(org.apache.ambari.server.RoleCommand.INSTALL);
        commandEntity.setStatus(org.apache.ambari.server.actionmanager.HostRoleStatus.QUEUED);
        commandEntity.setRole(org.apache.ambari.server.Role.DATANODE);
        commandEntity2.setHostEntity(host2);
        host2.getHostRoleCommandEntities().add(commandEntity2);
        commandEntity2.setRoleCommand(org.apache.ambari.server.RoleCommand.EXECUTE);
        commandEntity2.setRole(org.apache.ambari.server.Role.NAMENODE);
        commandEntity2.setStatus(org.apache.ambari.server.actionmanager.HostRoleStatus.COMPLETED);
        commandEntity3.setHostEntity(host1);
        host1.getHostRoleCommandEntities().add(commandEntity3);
        commandEntity3.setRoleCommand(org.apache.ambari.server.RoleCommand.START);
        commandEntity3.setRole(org.apache.ambari.server.Role.SECONDARY_NAMENODE);
        commandEntity3.setStatus(org.apache.ambari.server.actionmanager.HostRoleStatus.IN_PROGRESS);
        commandEntity.setStage(stageEntity);
        commandEntity2.setStage(stageEntity);
        commandEntity3.setStage(stageEntity);
        stageEntity.setHostRoleCommands(new java.util.ArrayList<>());
        stageEntity.getHostRoleCommands().add(commandEntity);
        stageEntity.getHostRoleCommands().add(commandEntity2);
        stageEntity.getHostRoleCommands().add(commandEntity3);
        requestDAO.create(requestEntity);
        stageDAO.create(stageEntity);
        hostRoleCommandDAO.create(commandEntity3);
        hostRoleCommandDAO.create(commandEntity);
        hostRoleCommandDAO.create(commandEntity2);
        hostDAO.merge(host1);
        hostDAO.merge(host2);
    }

    @com.google.inject.persist.Transactional
    public org.apache.ambari.server.orm.entities.StackEntity createStack(org.apache.ambari.server.state.StackId stackId) throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.server.orm.entities.StackEntity stackEntity = stackDAO.find(stackId.getStackName(), stackId.getStackVersion());
        if (null == stackEntity) {
            stackEntity = new org.apache.ambari.server.orm.entities.StackEntity();
            stackEntity.setStackName(stackId.getStackName());
            stackEntity.setStackVersion(stackId.getStackVersion());
            stackDAO.create(stackEntity);
        }
        return stackEntity;
    }

    @com.google.inject.persist.Transactional
    public java.lang.Long createCluster() throws java.lang.Exception {
        return createCluster(org.apache.ambari.server.orm.OrmTestHelper.CLUSTER_NAME);
    }

    @com.google.inject.persist.Transactional
    public java.lang.Long createCluster(java.lang.String clusterName) throws java.lang.Exception {
        injector.getInstance(org.apache.ambari.server.api.services.AmbariMetaInfo.class);
        org.apache.ambari.server.orm.dao.ResourceTypeDAO resourceTypeDAO = injector.getInstance(org.apache.ambari.server.orm.dao.ResourceTypeDAO.class);
        org.apache.ambari.server.orm.entities.ResourceTypeEntity resourceTypeEntity = new org.apache.ambari.server.orm.entities.ResourceTypeEntity();
        resourceTypeEntity.setId(org.apache.ambari.server.security.authorization.ResourceType.CLUSTER.getId());
        resourceTypeEntity.setName(org.apache.ambari.server.security.authorization.ResourceType.CLUSTER.name());
        resourceTypeEntity = resourceTypeDAO.merge(resourceTypeEntity);
        org.apache.ambari.server.orm.entities.ResourceEntity resourceEntity = new org.apache.ambari.server.orm.entities.ResourceEntity();
        resourceEntity.setResourceType(resourceTypeEntity);
        org.apache.ambari.server.orm.dao.ClusterDAO clusterDAO = injector.getInstance(org.apache.ambari.server.orm.dao.ClusterDAO.class);
        org.apache.ambari.server.orm.dao.StackDAO stackDAO = injector.getInstance(org.apache.ambari.server.orm.dao.StackDAO.class);
        org.apache.ambari.server.orm.entities.StackEntity stackEntity = stackDAO.find("HDP", "2.0.6");
        org.junit.Assert.assertNotNull(stackEntity);
        org.apache.ambari.server.orm.entities.ClusterEntity clusterEntity = new org.apache.ambari.server.orm.entities.ClusterEntity();
        clusterEntity.setClusterName(clusterName);
        clusterEntity.setClusterInfo("test_cluster_info1");
        clusterEntity.setResource(resourceEntity);
        clusterEntity.setDesiredStack(stackEntity);
        clusterDAO.create(clusterEntity);
        org.apache.ambari.server.orm.entities.ClusterStateEntity clusterStateEntity = new org.apache.ambari.server.orm.entities.ClusterStateEntity();
        clusterStateEntity.setCurrentStack(stackEntity);
        clusterStateEntity.setClusterEntity(clusterEntity);
        getEntityManager().persist(clusterStateEntity);
        clusterEntity = clusterDAO.findByName(clusterEntity.getClusterName());
        org.junit.Assert.assertNotNull(clusterEntity);
        org.junit.Assert.assertTrue(clusterEntity.getClusterId() > 0);
        clusterEntity.setClusterStateEntity(clusterStateEntity);
        clusterDAO.merge(clusterEntity);
        org.apache.ambari.server.state.Clusters clusters = injector.getInstance(org.apache.ambari.server.state.Clusters.class);
        java.lang.reflect.Method method = org.apache.ambari.server.state.cluster.ClustersImpl.class.getDeclaredMethod("loadClustersAndHosts");
        method.setAccessible(true);
        method.invoke(clusters);
        return clusterEntity.getClusterId();
    }

    public org.apache.ambari.server.state.Cluster buildNewCluster(org.apache.ambari.server.state.Clusters clusters, org.apache.ambari.server.state.ServiceFactory serviceFactory, org.apache.ambari.server.state.ServiceComponentFactory componentFactory, org.apache.ambari.server.state.ServiceComponentHostFactory schFactory, java.lang.String hostName) throws java.lang.Exception {
        java.lang.String clusterName = "cluster-" + java.lang.System.currentTimeMillis();
        org.apache.ambari.server.state.StackId stackId = new org.apache.ambari.server.state.StackId("HDP", "2.0.6");
        createStack(stackId);
        clusters.addCluster(clusterName, stackId);
        org.apache.ambari.server.state.Cluster cluster = clusters.getCluster(clusterName);
        cluster = initializeClusterWithStack(cluster);
        addHost(clusters, cluster, hostName);
        installHdfsService(cluster, serviceFactory, componentFactory, schFactory, hostName);
        installYarnService(cluster, serviceFactory, componentFactory, schFactory, hostName);
        return cluster;
    }

    public org.apache.ambari.server.state.Cluster initializeClusterWithStack(org.apache.ambari.server.state.Cluster cluster) throws java.lang.Exception {
        org.apache.ambari.server.state.StackId stackId = new org.apache.ambari.server.state.StackId("HDP", "2.0.6");
        cluster.setDesiredStackVersion(stackId);
        getOrCreateRepositoryVersion(stackId, stackId.getStackVersion());
        return cluster;
    }

    public void addHost(org.apache.ambari.server.state.Clusters clusters, org.apache.ambari.server.state.Cluster cluster, java.lang.String hostName) throws java.lang.Exception {
        clusters.addHost(hostName);
        org.apache.ambari.server.state.Host host = clusters.getHost(hostName);
        java.util.Map<java.lang.String, java.lang.String> hostAttributes = new java.util.HashMap<>();
        hostAttributes.put("os_family", "redhat");
        hostAttributes.put("os_release_version", "6.4");
        host.setHostAttributes(hostAttributes);
        host.setState(org.apache.ambari.server.state.HostState.HEALTHY);
        clusters.mapAndPublishHostsToCluster(java.util.Collections.singleton(hostName), cluster.getClusterName());
    }

    public void addHostComponent(org.apache.ambari.server.state.Cluster cluster, java.lang.String hostName, java.lang.String serviceName, java.lang.String componentName) throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.server.state.Service service = cluster.getService(serviceName);
        org.apache.ambari.server.state.ServiceComponent serviceComponent = service.getServiceComponent(componentName);
        org.apache.ambari.server.state.ServiceComponentHost serviceComponentHost = serviceComponent.addServiceComponentHost(hostName);
        serviceComponentHost.setDesiredState(org.apache.ambari.server.state.State.INSTALLED);
    }

    public void installHdfsService(org.apache.ambari.server.state.Cluster cluster, org.apache.ambari.server.state.ServiceFactory serviceFactory, org.apache.ambari.server.state.ServiceComponentFactory componentFactory, org.apache.ambari.server.state.ServiceComponentHostFactory schFactory, java.lang.String hostName) throws java.lang.Exception {
        org.apache.ambari.server.orm.entities.RepositoryVersionEntity repositoryVersion = repositoryVersionDAO.findByStackAndVersion(cluster.getDesiredStackVersion(), cluster.getDesiredStackVersion().getStackVersion());
        java.lang.String serviceName = "HDFS";
        org.apache.ambari.server.state.Service service = serviceFactory.createNew(cluster, serviceName, repositoryVersion);
        org.junit.Assert.assertNotNull(cluster.getService(serviceName));
        org.apache.ambari.server.state.ServiceComponent datanode = componentFactory.createNew(service, "DATANODE");
        service.addServiceComponent(datanode);
        datanode.setDesiredState(org.apache.ambari.server.state.State.INSTALLED);
        org.apache.ambari.server.state.ServiceComponentHost sch = schFactory.createNew(datanode, hostName);
        datanode.addServiceComponentHost(sch);
        sch.setDesiredState(org.apache.ambari.server.state.State.INSTALLED);
        sch.setState(org.apache.ambari.server.state.State.INSTALLED);
        org.apache.ambari.server.state.ServiceComponent namenode = componentFactory.createNew(service, "NAMENODE");
        service.addServiceComponent(namenode);
        namenode.setDesiredState(org.apache.ambari.server.state.State.INSTALLED);
        sch = schFactory.createNew(namenode, hostName);
        namenode.addServiceComponentHost(sch);
        sch.setDesiredState(org.apache.ambari.server.state.State.INSTALLED);
        sch.setState(org.apache.ambari.server.state.State.INSTALLED);
    }

    public void installYarnService(org.apache.ambari.server.state.Cluster cluster, org.apache.ambari.server.state.ServiceFactory serviceFactory, org.apache.ambari.server.state.ServiceComponentFactory componentFactory, org.apache.ambari.server.state.ServiceComponentHostFactory schFactory, java.lang.String hostName) throws java.lang.Exception {
        org.apache.ambari.server.orm.entities.RepositoryVersionEntity repositoryVersion = repositoryVersionDAO.findByStackAndVersion(cluster.getDesiredStackVersion(), cluster.getDesiredStackVersion().getStackVersion());
        java.lang.String serviceName = "YARN";
        org.apache.ambari.server.state.Service service = serviceFactory.createNew(cluster, serviceName, repositoryVersion);
        org.junit.Assert.assertNotNull(cluster.getService(serviceName));
        org.apache.ambari.server.state.ServiceComponent resourceManager = componentFactory.createNew(service, "RESOURCEMANAGER");
        service.addServiceComponent(resourceManager);
        resourceManager.setDesiredState(org.apache.ambari.server.state.State.INSTALLED);
        org.apache.ambari.server.state.ServiceComponentHost sch = schFactory.createNew(resourceManager, hostName);
        resourceManager.addServiceComponentHost(sch);
        sch.setDesiredState(org.apache.ambari.server.state.State.INSTALLED);
        sch.setState(org.apache.ambari.server.state.State.INSTALLED);
    }

    public org.apache.ambari.server.orm.entities.AlertTargetEntity createAlertTarget() throws java.lang.Exception {
        org.apache.ambari.server.orm.entities.AlertTargetEntity target = new org.apache.ambari.server.orm.entities.AlertTargetEntity();
        target.setDescription("Target Description");
        target.setNotificationType("EMAIL");
        target.setProperties("Target Properties");
        target.setTargetName("Target Name " + java.lang.System.currentTimeMillis());
        alertDispatchDAO.create(target);
        return target;
    }

    public org.apache.ambari.server.orm.entities.AlertTargetEntity createGlobalAlertTarget() throws java.lang.Exception {
        org.apache.ambari.server.orm.entities.AlertTargetEntity target = new org.apache.ambari.server.orm.entities.AlertTargetEntity();
        target.setDescription("Target Description");
        target.setNotificationType("EMAIL");
        target.setProperties("Target Properties");
        target.setTargetName("Target Name " + java.lang.System.currentTimeMillis());
        target.setGlobal(true);
        alertDispatchDAO.create(target);
        return target;
    }

    public org.apache.ambari.server.orm.entities.AlertDefinitionEntity createAlertDefinition(long clusterId) throws java.lang.Exception {
        org.apache.ambari.server.orm.entities.AlertDefinitionEntity definition = new org.apache.ambari.server.orm.entities.AlertDefinitionEntity();
        definition.setDefinitionName("Alert Definition " + java.lang.System.currentTimeMillis());
        definition.setServiceName("AMBARI");
        definition.setComponentName(null);
        definition.setClusterId(clusterId);
        definition.setHash(java.util.UUID.randomUUID().toString());
        definition.setScheduleInterval(60);
        definition.setScope(org.apache.ambari.server.state.alert.Scope.SERVICE);
        definition.setSource("{\"type\" : \"SCRIPT\"}");
        definition.setSourceType(org.apache.ambari.server.state.alert.SourceType.SCRIPT);
        alertDefinitionDAO.create(definition);
        return alertDefinitionDAO.findById(definition.getDefinitionId());
    }

    public org.apache.ambari.server.orm.entities.AlertGroupEntity createAlertGroup(long clusterId, java.util.Set<org.apache.ambari.server.orm.entities.AlertTargetEntity> targets) throws java.lang.Exception {
        org.apache.ambari.server.orm.entities.AlertGroupEntity group = new org.apache.ambari.server.orm.entities.AlertGroupEntity();
        group.setDefault(false);
        group.setGroupName(("Group Name " + java.lang.System.currentTimeMillis()) + uniqueCounter.incrementAndGet());
        group.setClusterId(clusterId);
        group.setAlertTargets(targets);
        alertDispatchDAO.create(group);
        return group;
    }

    public java.util.List<org.apache.ambari.server.orm.entities.AlertGroupEntity> createDefaultAlertGroups(long clusterId) throws java.lang.Exception {
        org.apache.ambari.server.orm.entities.AlertGroupEntity hdfsGroup = new org.apache.ambari.server.orm.entities.AlertGroupEntity();
        hdfsGroup.setDefault(true);
        hdfsGroup.setClusterId(clusterId);
        hdfsGroup.setGroupName("HDFS");
        hdfsGroup.setServiceName("HDFS");
        org.apache.ambari.server.orm.entities.AlertGroupEntity oozieGroup = new org.apache.ambari.server.orm.entities.AlertGroupEntity();
        oozieGroup.setDefault(true);
        oozieGroup.setClusterId(clusterId);
        oozieGroup.setGroupName("OOZIE");
        oozieGroup.setServiceName("OOZIE");
        alertDispatchDAO.create(hdfsGroup);
        alertDispatchDAO.create(oozieGroup);
        java.util.List<org.apache.ambari.server.orm.entities.AlertGroupEntity> defaultGroups = alertDispatchDAO.findAllGroups(clusterId);
        org.junit.Assert.assertEquals(2, defaultGroups.size());
        org.junit.Assert.assertNotNull(alertDispatchDAO.findDefaultServiceGroup(clusterId, "HDFS"));
        org.junit.Assert.assertNotNull(alertDispatchDAO.findDefaultServiceGroup(clusterId, "OOZIE"));
        return defaultGroups;
    }

    public org.apache.ambari.server.orm.entities.RepositoryVersionEntity getOrCreateRepositoryVersion(org.apache.ambari.server.state.Cluster cluster) {
        org.apache.ambari.server.state.StackId stackId = cluster.getCurrentStackVersion();
        java.lang.String version = stackId.getStackVersion() + ".1";
        org.apache.ambari.server.orm.dao.StackDAO stackDAO = injector.getInstance(org.apache.ambari.server.orm.dao.StackDAO.class);
        org.apache.ambari.server.orm.entities.StackEntity stackEntity = stackDAO.find(stackId.getStackName(), stackId.getStackVersion());
        org.junit.Assert.assertNotNull(stackEntity);
        org.apache.ambari.server.orm.entities.RepositoryVersionEntity repositoryVersion = repositoryVersionDAO.findByStackAndVersion(stackId, version);
        if (repositoryVersion == null) {
            try {
                java.util.List<org.apache.ambari.server.orm.entities.RepoOsEntity> operatingSystems = new java.util.ArrayList<>();
                org.apache.ambari.server.orm.entities.RepoDefinitionEntity repoDefinitionEntity1 = new org.apache.ambari.server.orm.entities.RepoDefinitionEntity();
                repoDefinitionEntity1.setRepoID("HDP");
                repoDefinitionEntity1.setBaseUrl("");
                repoDefinitionEntity1.setRepoName("HDP");
                org.apache.ambari.server.orm.entities.RepoDefinitionEntity repoDefinitionEntity2 = new org.apache.ambari.server.orm.entities.RepoDefinitionEntity();
                repoDefinitionEntity2.setRepoID("HDP-UTILS");
                repoDefinitionEntity2.setBaseUrl("");
                repoDefinitionEntity2.setRepoName("HDP-UTILS");
                org.apache.ambari.server.orm.entities.RepoOsEntity repoOsEntityRedHat6 = new org.apache.ambari.server.orm.entities.RepoOsEntity();
                repoOsEntityRedHat6.setFamily("redhat6");
                repoOsEntityRedHat6.setAmbariManaged(true);
                repoOsEntityRedHat6.addRepoDefinition(repoDefinitionEntity1);
                repoOsEntityRedHat6.addRepoDefinition(repoDefinitionEntity2);
                org.apache.ambari.server.orm.entities.RepoOsEntity repoOsEntityRedHat5 = new org.apache.ambari.server.orm.entities.RepoOsEntity();
                repoOsEntityRedHat5.setFamily("redhat5");
                repoOsEntityRedHat5.setAmbariManaged(true);
                repoOsEntityRedHat5.addRepoDefinition(repoDefinitionEntity1);
                repoOsEntityRedHat5.addRepoDefinition(repoDefinitionEntity2);
                operatingSystems.add(repoOsEntityRedHat6);
                operatingSystems.add(repoOsEntityRedHat5);
                repositoryVersion = repositoryVersionDAO.create(stackEntity, version, java.lang.String.valueOf(java.lang.System.currentTimeMillis()) + uniqueCounter.incrementAndGet(), operatingSystems);
            } catch (java.lang.Exception ex) {
                org.apache.ambari.server.orm.OrmTestHelper.LOG.error("Caught exception", ex);
                ex.printStackTrace();
                org.junit.Assert.fail(java.text.MessageFormat.format("Unable to create Repo Version for Stack {0} and version {1}", (stackEntity.getStackName() + "-") + stackEntity.getStackVersion(), version));
            }
        }
        return repositoryVersion;
    }

    public org.apache.ambari.server.orm.entities.RepositoryVersionEntity getOrCreateRepositoryVersion(org.apache.ambari.server.state.StackId stackId, java.lang.String version) {
        org.apache.ambari.server.orm.entities.StackEntity stackEntity = null;
        try {
            stackEntity = createStack(stackId);
        } catch (java.lang.Exception e) {
            org.apache.ambari.server.orm.OrmTestHelper.LOG.error("Expected successful repository", e);
        }
        org.junit.Assert.assertNotNull(stackEntity);
        org.apache.ambari.server.orm.entities.RepositoryVersionEntity repositoryVersion = repositoryVersionDAO.findByStackAndVersion(stackId, version);
        if (repositoryVersion == null) {
            try {
                java.util.List<org.apache.ambari.server.orm.entities.RepoOsEntity> operatingSystems = new java.util.ArrayList<>();
                org.apache.ambari.server.orm.entities.RepoDefinitionEntity repoDefinitionEntity1 = new org.apache.ambari.server.orm.entities.RepoDefinitionEntity();
                repoDefinitionEntity1.setRepoID("HDP");
                repoDefinitionEntity1.setBaseUrl("");
                repoDefinitionEntity1.setRepoName("HDP");
                org.apache.ambari.server.orm.entities.RepoDefinitionEntity repoDefinitionEntity2 = new org.apache.ambari.server.orm.entities.RepoDefinitionEntity();
                repoDefinitionEntity2.setRepoID("HDP-UTILS");
                repoDefinitionEntity2.setBaseUrl("");
                repoDefinitionEntity2.setRepoName("HDP-UTILS");
                org.apache.ambari.server.orm.entities.RepoOsEntity repoOsEntityRedHat6 = new org.apache.ambari.server.orm.entities.RepoOsEntity();
                repoOsEntityRedHat6.setFamily("redhat6");
                repoOsEntityRedHat6.setAmbariManaged(true);
                repoOsEntityRedHat6.addRepoDefinition(repoDefinitionEntity1);
                repoOsEntityRedHat6.addRepoDefinition(repoDefinitionEntity2);
                org.apache.ambari.server.orm.entities.RepoOsEntity repoOsEntityRedHat5 = new org.apache.ambari.server.orm.entities.RepoOsEntity();
                repoOsEntityRedHat5.setFamily("redhat5");
                repoOsEntityRedHat5.setAmbariManaged(true);
                repoOsEntityRedHat5.addRepoDefinition(repoDefinitionEntity1);
                repoOsEntityRedHat5.addRepoDefinition(repoDefinitionEntity2);
                operatingSystems.add(repoOsEntityRedHat6);
                operatingSystems.add(repoOsEntityRedHat5);
                repositoryVersion = repositoryVersionDAO.create(stackEntity, version, java.lang.String.valueOf(java.lang.System.currentTimeMillis()) + uniqueCounter.incrementAndGet(), operatingSystems);
                repositoryVersion.setResolved(true);
                repositoryVersion = repositoryVersionDAO.merge(repositoryVersion);
            } catch (java.lang.Exception ex) {
                org.apache.ambari.server.orm.OrmTestHelper.LOG.error("Caught exception", ex);
                org.junit.Assert.fail(java.text.MessageFormat.format("Unable to create Repo Version for Stack {0} and version {1}", (stackEntity.getStackName() + "-") + stackEntity.getStackVersion(), version));
            }
        }
        return repositoryVersion;
    }

    public org.apache.ambari.server.orm.entities.HostVersionEntity createHostVersion(java.lang.String hostName, org.apache.ambari.server.orm.entities.RepositoryVersionEntity repositoryVersionEntity, org.apache.ambari.server.state.RepositoryVersionState repositoryVersionState) {
        org.apache.ambari.server.orm.entities.HostEntity hostEntity = hostDAO.findByName(hostName);
        org.apache.ambari.server.orm.entities.HostVersionEntity hostVersionEntity = new org.apache.ambari.server.orm.entities.HostVersionEntity(hostEntity, repositoryVersionEntity, repositoryVersionState);
        hostVersionEntity.setHostId(hostEntity.getHostId());
        hostVersionDAO.create(hostVersionEntity);
        hostEntity.getHostVersionEntities().add(hostVersionEntity);
        hostDAO.merge(hostEntity);
        return hostVersionEntity;
    }
}