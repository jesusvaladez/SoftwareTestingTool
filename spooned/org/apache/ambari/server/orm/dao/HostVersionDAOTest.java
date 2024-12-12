package org.apache.ambari.server.orm.dao;
public class HostVersionDAOTest {
    private static com.google.inject.Injector injector;

    private org.apache.ambari.server.orm.dao.ResourceTypeDAO resourceTypeDAO;

    private org.apache.ambari.server.orm.dao.ClusterDAO clusterDAO;

    private org.apache.ambari.server.orm.dao.StackDAO stackDAO;

    private org.apache.ambari.server.orm.dao.HostDAO hostDAO;

    private org.apache.ambari.server.orm.dao.HostVersionDAO hostVersionDAO;

    private org.apache.ambari.server.orm.OrmTestHelper helper;

    private static final org.apache.ambari.server.state.StackId HDP_22_STACK = new org.apache.ambari.server.state.StackId("HDP", "2.2.0");

    private static final org.apache.ambari.server.state.StackId BAD_STACK = new org.apache.ambari.server.state.StackId("BADSTACK", "1.0");

    private static final java.lang.String repoVersion_2200 = "2.2.0.0-1";

    private static final java.lang.String repoVersion_2201 = "2.2.0.1-2";

    private static final java.lang.String repoVersion_2202 = "2.2.0.2-3";

    @org.junit.Before
    public void before() {
        org.apache.ambari.server.orm.dao.HostVersionDAOTest.injector = com.google.inject.Guice.createInjector(new org.apache.ambari.server.orm.InMemoryDefaultTestModule());
        org.apache.ambari.server.H2DatabaseCleaner.resetSequences(org.apache.ambari.server.orm.dao.HostVersionDAOTest.injector);
        org.apache.ambari.server.orm.dao.HostVersionDAOTest.injector.getInstance(org.apache.ambari.server.orm.GuiceJpaInitializer.class);
        resourceTypeDAO = org.apache.ambari.server.orm.dao.HostVersionDAOTest.injector.getInstance(org.apache.ambari.server.orm.dao.ResourceTypeDAO.class);
        clusterDAO = org.apache.ambari.server.orm.dao.HostVersionDAOTest.injector.getInstance(org.apache.ambari.server.orm.dao.ClusterDAO.class);
        stackDAO = org.apache.ambari.server.orm.dao.HostVersionDAOTest.injector.getInstance(org.apache.ambari.server.orm.dao.StackDAO.class);
        hostDAO = org.apache.ambari.server.orm.dao.HostVersionDAOTest.injector.getInstance(org.apache.ambari.server.orm.dao.HostDAO.class);
        hostVersionDAO = org.apache.ambari.server.orm.dao.HostVersionDAOTest.injector.getInstance(org.apache.ambari.server.orm.dao.HostVersionDAO.class);
        helper = org.apache.ambari.server.orm.dao.HostVersionDAOTest.injector.getInstance(org.apache.ambari.server.orm.OrmTestHelper.class);
        org.apache.ambari.server.orm.dao.HostVersionDAOTest.injector.getInstance(org.apache.ambari.server.api.services.AmbariMetaInfo.class);
        createDefaultData();
    }

    private void createDefaultData() {
        org.apache.ambari.server.orm.entities.StackEntity stackEntity = stackDAO.find(org.apache.ambari.server.orm.dao.HostVersionDAOTest.HDP_22_STACK.getStackName(), org.apache.ambari.server.orm.dao.HostVersionDAOTest.HDP_22_STACK.getStackVersion());
        org.junit.Assert.assertNotNull(stackEntity);
        org.apache.ambari.server.orm.entities.ResourceTypeEntity resourceTypeEntity = new org.apache.ambari.server.orm.entities.ResourceTypeEntity();
        resourceTypeEntity.setId(org.apache.ambari.server.security.authorization.ResourceType.CLUSTER.getId());
        resourceTypeEntity.setName(org.apache.ambari.server.security.authorization.ResourceType.CLUSTER.name());
        resourceTypeEntity = resourceTypeDAO.merge(resourceTypeEntity);
        org.apache.ambari.server.orm.entities.ResourceEntity resourceEntity = new org.apache.ambari.server.orm.entities.ResourceEntity();
        resourceEntity.setResourceType(resourceTypeEntity);
        org.apache.ambari.server.orm.entities.ClusterEntity clusterEntity = new org.apache.ambari.server.orm.entities.ClusterEntity();
        clusterEntity.setClusterName("test_cluster1");
        clusterEntity.setClusterInfo("test_cluster_info1");
        clusterEntity.setResource(resourceEntity);
        clusterEntity.setDesiredStack(stackEntity);
        clusterDAO.create(clusterEntity);
        org.apache.ambari.server.orm.entities.RepositoryVersionEntity repoVersionEntity = helper.getOrCreateRepositoryVersion(org.apache.ambari.server.orm.dao.HostVersionDAOTest.HDP_22_STACK, org.apache.ambari.server.orm.dao.HostVersionDAOTest.repoVersion_2200);
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
        hostEntities.add(host3);
        host1.setClusterEntities(java.util.Arrays.asList(clusterEntity));
        host2.setClusterEntities(java.util.Arrays.asList(clusterEntity));
        host3.setClusterEntities(java.util.Arrays.asList(clusterEntity));
        hostDAO.create(host1);
        hostDAO.create(host2);
        hostDAO.create(host3);
        clusterEntity.setHostEntities(hostEntities);
        clusterDAO.merge(clusterEntity);
        org.apache.ambari.server.orm.entities.HostVersionEntity hostVersionEntity1 = new org.apache.ambari.server.orm.entities.HostVersionEntity(host1, repoVersionEntity, org.apache.ambari.server.state.RepositoryVersionState.CURRENT);
        org.apache.ambari.server.orm.entities.HostVersionEntity hostVersionEntity2 = new org.apache.ambari.server.orm.entities.HostVersionEntity(host2, repoVersionEntity, org.apache.ambari.server.state.RepositoryVersionState.INSTALLED);
        org.apache.ambari.server.orm.entities.HostVersionEntity hostVersionEntity3 = new org.apache.ambari.server.orm.entities.HostVersionEntity(host3, repoVersionEntity, org.apache.ambari.server.state.RepositoryVersionState.INSTALLED);
        hostVersionDAO.create(hostVersionEntity1);
        hostVersionDAO.create(hostVersionEntity2);
        hostVersionDAO.create(hostVersionEntity3);
    }

    private void addMoreVersions() {
        org.apache.ambari.server.orm.entities.ClusterEntity clusterEntity = clusterDAO.findByName("test_cluster1");
        org.apache.ambari.server.orm.entities.RepositoryVersionEntity repositoryVersionEnt_2_2_0_1 = helper.getOrCreateRepositoryVersion(org.apache.ambari.server.orm.dao.HostVersionDAOTest.HDP_22_STACK, org.apache.ambari.server.orm.dao.HostVersionDAOTest.repoVersion_2201);
        org.apache.ambari.server.orm.entities.HostEntity[] hostEntities = clusterEntity.getHostEntities().toArray(new org.apache.ambari.server.orm.entities.HostEntity[clusterEntity.getHostEntities().size()]);
        java.util.Arrays.sort(hostEntities);
        for (org.apache.ambari.server.orm.entities.HostEntity host : hostEntities) {
            org.apache.ambari.server.orm.entities.HostVersionEntity hostVersionEntity = new org.apache.ambari.server.orm.entities.HostVersionEntity(host, repositoryVersionEnt_2_2_0_1, org.apache.ambari.server.state.RepositoryVersionState.INSTALLED);
            hostVersionDAO.create(hostVersionEntity);
        }
        org.apache.ambari.server.orm.entities.RepositoryVersionEntity repositoryVersionEnt_2_2_0_2 = helper.getOrCreateRepositoryVersion(org.apache.ambari.server.orm.dao.HostVersionDAOTest.HDP_22_STACK, org.apache.ambari.server.orm.dao.HostVersionDAOTest.repoVersion_2202);
        for (int i = 0; i < hostEntities.length; i++) {
            org.apache.ambari.server.state.RepositoryVersionState desiredState = null;
            if ((i % 3) == 0) {
                desiredState = org.apache.ambari.server.state.RepositoryVersionState.INSTALLED;
            }
            if ((i % 3) == 1) {
                desiredState = org.apache.ambari.server.state.RepositoryVersionState.INSTALLING;
            }
            if ((i % 3) == 2) {
                desiredState = org.apache.ambari.server.state.RepositoryVersionState.INSTALL_FAILED;
            }
            org.apache.ambari.server.orm.entities.HostVersionEntity hostVersionEntity = new org.apache.ambari.server.orm.entities.HostVersionEntity(hostEntities[i], repositoryVersionEnt_2_2_0_2, desiredState);
            hostVersionDAO.create(hostVersionEntity);
        }
    }

    @org.junit.Test
    public void testFindAll() {
        org.junit.Assert.assertEquals(3, hostVersionDAO.findAll().size());
    }

    @org.junit.Test
    public void testFindByHost() {
        org.junit.Assert.assertEquals(1, hostVersionDAO.findByHost("test_host1").size());
        org.junit.Assert.assertEquals(1, hostVersionDAO.findByHost("test_host2").size());
        org.junit.Assert.assertEquals(1, hostVersionDAO.findByHost("test_host3").size());
        addMoreVersions();
        org.junit.Assert.assertEquals(3, hostVersionDAO.findByHost("test_host1").size());
        org.junit.Assert.assertEquals(3, hostVersionDAO.findByHost("test_host2").size());
        org.junit.Assert.assertEquals(3, hostVersionDAO.findByHost("test_host3").size());
    }

    @org.junit.Test
    public void testFindByClusterStackAndVersion() {
        org.junit.Assert.assertEquals(3, hostVersionDAO.findByClusterStackAndVersion("test_cluster1", org.apache.ambari.server.orm.dao.HostVersionDAOTest.HDP_22_STACK, org.apache.ambari.server.orm.dao.HostVersionDAOTest.repoVersion_2200).size());
        org.junit.Assert.assertEquals(3, hostVersionDAO.findAll().size());
        addMoreVersions();
        org.junit.Assert.assertEquals(3, hostVersionDAO.findByClusterStackAndVersion("test_cluster1", org.apache.ambari.server.orm.dao.HostVersionDAOTest.HDP_22_STACK, org.apache.ambari.server.orm.dao.HostVersionDAOTest.repoVersion_2201).size());
        org.junit.Assert.assertEquals(3, hostVersionDAO.findByClusterStackAndVersion("test_cluster1", org.apache.ambari.server.orm.dao.HostVersionDAOTest.HDP_22_STACK, org.apache.ambari.server.orm.dao.HostVersionDAOTest.repoVersion_2202).size());
        org.junit.Assert.assertEquals(9, hostVersionDAO.findAll().size());
    }

    @org.junit.Test
    public void testFindByClusterAndHost() {
        org.junit.Assert.assertEquals(1, hostVersionDAO.findByClusterAndHost("test_cluster1", "test_host1").size());
        org.junit.Assert.assertEquals(1, hostVersionDAO.findByClusterAndHost("test_cluster1", "test_host2").size());
        org.junit.Assert.assertEquals(1, hostVersionDAO.findByClusterAndHost("test_cluster1", "test_host3").size());
        addMoreVersions();
        org.junit.Assert.assertEquals(3, hostVersionDAO.findByClusterAndHost("test_cluster1", "test_host1").size());
        org.junit.Assert.assertEquals(3, hostVersionDAO.findByClusterAndHost("test_cluster1", "test_host2").size());
        org.junit.Assert.assertEquals(3, hostVersionDAO.findByClusterAndHost("test_cluster1", "test_host3").size());
    }

    @org.junit.Test
    public void testFindByCluster() {
        org.junit.Assert.assertEquals(3, hostVersionDAO.findByCluster("test_cluster1").size());
        addMoreVersions();
        org.junit.Assert.assertEquals(9, hostVersionDAO.findByCluster("test_cluster1").size());
    }

    @org.junit.Test
    public void testFindByClusterHostAndState() {
        org.junit.Assert.assertEquals(1, hostVersionDAO.findByClusterHostAndState("test_cluster1", "test_host1", org.apache.ambari.server.state.RepositoryVersionState.CURRENT).size());
        org.junit.Assert.assertEquals(0, hostVersionDAO.findByClusterHostAndState("test_cluster1", "test_host1", org.apache.ambari.server.state.RepositoryVersionState.INSTALLED).size());
        org.junit.Assert.assertEquals(0, hostVersionDAO.findByClusterHostAndState("test_cluster1", "test_host2", org.apache.ambari.server.state.RepositoryVersionState.INSTALLING).size());
        org.junit.Assert.assertEquals(0, hostVersionDAO.findByClusterHostAndState("test_cluster1", "test_host3", org.apache.ambari.server.state.RepositoryVersionState.INSTALL_FAILED).size());
        addMoreVersions();
        org.junit.Assert.assertEquals(2, hostVersionDAO.findByClusterHostAndState("test_cluster1", "test_host1", org.apache.ambari.server.state.RepositoryVersionState.INSTALLED).size());
        org.junit.Assert.assertEquals(2, hostVersionDAO.findByClusterHostAndState("test_cluster1", "test_host2", org.apache.ambari.server.state.RepositoryVersionState.INSTALLED).size());
        org.junit.Assert.assertEquals(2, hostVersionDAO.findByClusterHostAndState("test_cluster1", "test_host3", org.apache.ambari.server.state.RepositoryVersionState.INSTALLED).size());
        org.junit.Assert.assertEquals(1, hostVersionDAO.findByClusterHostAndState("test_cluster1", "test_host1", org.apache.ambari.server.state.RepositoryVersionState.CURRENT).size());
        org.junit.Assert.assertEquals(1, hostVersionDAO.findByClusterHostAndState("test_cluster1", "test_host2", org.apache.ambari.server.state.RepositoryVersionState.INSTALLING).size());
        org.junit.Assert.assertEquals(1, hostVersionDAO.findByClusterHostAndState("test_cluster1", "test_host3", org.apache.ambari.server.state.RepositoryVersionState.INSTALL_FAILED).size());
    }

    @org.junit.Test
    public void testFindByClusterStackVersionAndHost() {
        org.apache.ambari.server.orm.entities.HostEntity host1 = hostDAO.findByName("test_host1");
        org.apache.ambari.server.orm.entities.HostEntity host2 = hostDAO.findByName("test_host2");
        org.apache.ambari.server.orm.entities.HostEntity host3 = hostDAO.findByName("test_host3");
        org.apache.ambari.server.orm.entities.HostVersionEntity hostVersionEntity1 = new org.apache.ambari.server.orm.entities.HostVersionEntity(host1, helper.getOrCreateRepositoryVersion(org.apache.ambari.server.orm.dao.HostVersionDAOTest.HDP_22_STACK, org.apache.ambari.server.orm.dao.HostVersionDAOTest.repoVersion_2200), org.apache.ambari.server.state.RepositoryVersionState.CURRENT);
        hostVersionEntity1.setId(1L);
        org.apache.ambari.server.orm.entities.HostVersionEntity hostVersionEntity2 = new org.apache.ambari.server.orm.entities.HostVersionEntity(host2, helper.getOrCreateRepositoryVersion(org.apache.ambari.server.orm.dao.HostVersionDAOTest.HDP_22_STACK, org.apache.ambari.server.orm.dao.HostVersionDAOTest.repoVersion_2200), org.apache.ambari.server.state.RepositoryVersionState.INSTALLED);
        hostVersionEntity2.setId(2L);
        org.apache.ambari.server.orm.entities.HostVersionEntity hostVersionEntity3 = new org.apache.ambari.server.orm.entities.HostVersionEntity(host3, helper.getOrCreateRepositoryVersion(org.apache.ambari.server.orm.dao.HostVersionDAOTest.HDP_22_STACK, org.apache.ambari.server.orm.dao.HostVersionDAOTest.repoVersion_2200), org.apache.ambari.server.state.RepositoryVersionState.INSTALLED);
        hostVersionEntity3.setId(3L);
        hostVersionEntity1.equals(hostVersionDAO.findByClusterStackVersionAndHost("test_cluster1", org.apache.ambari.server.orm.dao.HostVersionDAOTest.HDP_22_STACK, org.apache.ambari.server.orm.dao.HostVersionDAOTest.repoVersion_2200, "test_host1"));
        org.junit.Assert.assertEquals(hostVersionEntity1, hostVersionDAO.findByClusterStackVersionAndHost("test_cluster1", org.apache.ambari.server.orm.dao.HostVersionDAOTest.HDP_22_STACK, org.apache.ambari.server.orm.dao.HostVersionDAOTest.repoVersion_2200, "test_host1"));
        org.junit.Assert.assertEquals(hostVersionEntity2, hostVersionDAO.findByClusterStackVersionAndHost("test_cluster1", org.apache.ambari.server.orm.dao.HostVersionDAOTest.HDP_22_STACK, org.apache.ambari.server.orm.dao.HostVersionDAOTest.repoVersion_2200, "test_host2"));
        org.junit.Assert.assertEquals(hostVersionEntity3, hostVersionDAO.findByClusterStackVersionAndHost("test_cluster1", org.apache.ambari.server.orm.dao.HostVersionDAOTest.HDP_22_STACK, org.apache.ambari.server.orm.dao.HostVersionDAOTest.repoVersion_2200, "test_host3"));
        org.junit.Assert.assertEquals(null, hostVersionDAO.findByClusterStackVersionAndHost("non_existent_cluster", org.apache.ambari.server.orm.dao.HostVersionDAOTest.HDP_22_STACK, org.apache.ambari.server.orm.dao.HostVersionDAOTest.repoVersion_2200, "test_host3"));
        org.junit.Assert.assertEquals(null, hostVersionDAO.findByClusterStackVersionAndHost("test_cluster1", org.apache.ambari.server.orm.dao.HostVersionDAOTest.BAD_STACK, org.apache.ambari.server.orm.dao.HostVersionDAOTest.repoVersion_2200, "test_host3"));
        org.junit.Assert.assertEquals(null, hostVersionDAO.findByClusterStackVersionAndHost("test_cluster1", org.apache.ambari.server.orm.dao.HostVersionDAOTest.HDP_22_STACK, "non_existent_version", "test_host3"));
        org.junit.Assert.assertEquals(null, hostVersionDAO.findByClusterStackVersionAndHost("test_cluster1", org.apache.ambari.server.orm.dao.HostVersionDAOTest.HDP_22_STACK, "non_existent_version", "non_existent_host"));
        addMoreVersions();
        org.apache.ambari.server.orm.entities.HostVersionEntity hostVersionEntity1LastExpected = new org.apache.ambari.server.orm.entities.HostVersionEntity(host1, helper.getOrCreateRepositoryVersion(org.apache.ambari.server.orm.dao.HostVersionDAOTest.HDP_22_STACK, org.apache.ambari.server.orm.dao.HostVersionDAOTest.repoVersion_2202), org.apache.ambari.server.state.RepositoryVersionState.INSTALLED);
        org.apache.ambari.server.orm.entities.HostVersionEntity hostVersionEntity2LastExpected = new org.apache.ambari.server.orm.entities.HostVersionEntity(host2, helper.getOrCreateRepositoryVersion(org.apache.ambari.server.orm.dao.HostVersionDAOTest.HDP_22_STACK, org.apache.ambari.server.orm.dao.HostVersionDAOTest.repoVersion_2202), org.apache.ambari.server.state.RepositoryVersionState.INSTALLING);
        org.apache.ambari.server.orm.entities.HostVersionEntity hostVersionEntity3LastExpected = new org.apache.ambari.server.orm.entities.HostVersionEntity(host3, helper.getOrCreateRepositoryVersion(org.apache.ambari.server.orm.dao.HostVersionDAOTest.HDP_22_STACK, org.apache.ambari.server.orm.dao.HostVersionDAOTest.repoVersion_2202), org.apache.ambari.server.state.RepositoryVersionState.INSTALL_FAILED);
        org.apache.ambari.server.orm.entities.HostVersionEntity hostVersionEntity1LastActual = hostVersionDAO.findByClusterStackVersionAndHost("test_cluster1", org.apache.ambari.server.orm.dao.HostVersionDAOTest.HDP_22_STACK, org.apache.ambari.server.orm.dao.HostVersionDAOTest.repoVersion_2202, "test_host1");
        org.apache.ambari.server.orm.entities.HostVersionEntity hostVersionEntity2LastActual = hostVersionDAO.findByClusterStackVersionAndHost("test_cluster1", org.apache.ambari.server.orm.dao.HostVersionDAOTest.HDP_22_STACK, org.apache.ambari.server.orm.dao.HostVersionDAOTest.repoVersion_2202, "test_host2");
        org.apache.ambari.server.orm.entities.HostVersionEntity hostVersionEntity3LastActual = hostVersionDAO.findByClusterStackVersionAndHost("test_cluster1", org.apache.ambari.server.orm.dao.HostVersionDAOTest.HDP_22_STACK, org.apache.ambari.server.orm.dao.HostVersionDAOTest.repoVersion_2202, "test_host3");
        org.junit.Assert.assertEquals(hostVersionEntity1LastExpected, new org.apache.ambari.server.orm.entities.HostVersionEntity(hostVersionEntity1LastActual));
        org.junit.Assert.assertEquals(hostVersionEntity2LastExpected, new org.apache.ambari.server.orm.entities.HostVersionEntity(hostVersionEntity2LastActual));
        org.junit.Assert.assertEquals(hostVersionEntity3LastExpected, new org.apache.ambari.server.orm.entities.HostVersionEntity(hostVersionEntity3LastActual));
    }

    @org.junit.Test
    public void testDuplicates() throws java.lang.Exception {
        org.apache.ambari.server.orm.entities.HostEntity host1 = hostDAO.findByName("test_host1");
        org.apache.ambari.server.orm.entities.RepositoryVersionEntity repoVersion = helper.getOrCreateRepositoryVersion(org.apache.ambari.server.orm.dao.HostVersionDAOTest.HDP_22_STACK, org.apache.ambari.server.orm.dao.HostVersionDAOTest.repoVersion_2200);
        org.apache.ambari.server.orm.entities.HostVersionEntity hostVersionEntity1 = new org.apache.ambari.server.orm.entities.HostVersionEntity(host1, repoVersion, org.apache.ambari.server.state.RepositoryVersionState.CURRENT);
        try {
            hostVersionDAO.create(hostVersionEntity1);
            org.junit.Assert.fail("Each host can have a relationship to a repo version, but cannot have more than one for the same repo");
        } catch (java.lang.Exception e) {
        }
    }

    @org.junit.After
    public void after() throws org.apache.ambari.server.AmbariException, java.sql.SQLException {
        org.apache.ambari.server.H2DatabaseCleaner.clearDatabaseAndStopPersistenceService(org.apache.ambari.server.orm.dao.HostVersionDAOTest.injector);
        org.apache.ambari.server.orm.dao.HostVersionDAOTest.injector = null;
    }
}