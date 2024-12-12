package org.apache.ambari.server.testing;
import com.google.inject.persist.PersistService;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
public class DBInconsistencyTests {
    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.testing.DBInconsistencyTests.class);

    @com.google.inject.Inject
    private com.google.inject.Injector injector;

    @com.google.inject.Inject
    private org.apache.ambari.server.orm.OrmTestHelper helper;

    @com.google.inject.Inject
    private org.apache.ambari.server.state.Clusters clusters;

    @com.google.inject.Inject
    private org.apache.ambari.server.state.ServiceFactory serviceFactory;

    @com.google.inject.Inject
    private org.apache.ambari.server.state.ServiceComponentFactory serviceComponentFactory;

    @com.google.inject.Inject
    private org.apache.ambari.server.mpack.MpackManagerFactory mpackManagerFactory;

    @com.google.inject.Inject
    private org.apache.ambari.server.state.ServiceComponentHostFactory serviceComponentHostFactory;

    @com.google.inject.Inject
    private org.apache.ambari.server.orm.dao.HostComponentDesiredStateDAO hostComponentDesiredStateDAO;

    @com.google.inject.Inject
    private org.apache.ambari.server.orm.dao.ServiceComponentDesiredStateDAO serviceComponentDesiredStateDAO;

    @com.google.inject.Inject
    private org.apache.ambari.server.orm.dao.ClusterDAO clusterDAO;

    @org.junit.Before
    public void setup() throws java.lang.Exception {
        injector = com.google.inject.Guice.createInjector(new org.apache.ambari.server.orm.InMemoryDefaultTestModule());
        injector.getInstance(org.apache.ambari.server.orm.GuiceJpaInitializer.class);
        injector.injectMembers(this);
    }

    @org.junit.After
    public void teardown() {
        injector.getInstance(com.google.inject.persist.PersistService.class).stop();
    }

    @org.junit.Test
    public void testOrphanedSCHDesiredEntityReAdd() throws java.lang.Exception {
        java.lang.Long clusterId = helper.createCluster();
        org.junit.Assert.assertNotNull(clusterId);
        org.apache.ambari.server.state.Cluster cluster = clusters.getCluster(org.apache.ambari.server.orm.OrmTestHelper.CLUSTER_NAME);
        org.junit.Assert.assertNotNull(cluster);
        helper.addHost(clusters, cluster, "h1");
        helper.initializeClusterWithStack(cluster);
        helper.installHdfsService(cluster, serviceFactory, serviceComponentFactory, serviceComponentHostFactory, "h1");
        java.util.Collection<org.apache.ambari.server.state.ServiceComponentHost> schList = clusters.getCluster(org.apache.ambari.server.orm.OrmTestHelper.CLUSTER_NAME).getServiceComponentHosts("HDFS", "DATANODE");
        org.junit.Assert.assertNotNull(schList);
        java.util.Collection<org.apache.ambari.server.state.ServiceComponent> scList = cluster.getService("HDFS").getServiceComponents().values();
        org.junit.Assert.assertNotNull(schList);
        cluster.deleteService("HDFS", new org.apache.ambari.server.controller.internal.DeleteHostComponentStatusMetaData());
        java.util.List<org.apache.ambari.server.orm.entities.HostComponentDesiredStateEntity> hostComponentDesiredStateEntities = hostComponentDesiredStateDAO.findAll();
        org.junit.Assert.assertTrue((hostComponentDesiredStateEntities == null) || hostComponentDesiredStateEntities.isEmpty());
        java.util.List<org.apache.ambari.server.orm.entities.ServiceComponentDesiredStateEntity> serviceComponentDesiredStateEntities = serviceComponentDesiredStateDAO.findAll();
        org.junit.Assert.assertTrue((serviceComponentDesiredStateEntities == null) || serviceComponentDesiredStateEntities.isEmpty());
        javax.persistence.EntityManager em = helper.getEntityManager();
        final javax.persistence.EntityTransaction txn = em.getTransaction();
        txn.begin();
        for (org.apache.ambari.server.state.ServiceComponentHost sch : schList) {
            sch.setDesiredState(org.apache.ambari.server.state.State.DISABLED);
        }
        for (org.apache.ambari.server.state.ServiceComponent sc : scList) {
            sc.setDesiredState(org.apache.ambari.server.state.State.DISABLED);
        }
        txn.commit();
        hostComponentDesiredStateEntities = hostComponentDesiredStateDAO.findAll();
        org.junit.Assert.assertTrue((hostComponentDesiredStateEntities == null) || hostComponentDesiredStateEntities.isEmpty());
        serviceComponentDesiredStateEntities = serviceComponentDesiredStateDAO.findAll();
        org.junit.Assert.assertTrue((serviceComponentDesiredStateEntities == null) || serviceComponentDesiredStateEntities.isEmpty());
    }

    @org.junit.Ignore
    @org.junit.Test
    public void testRefreshInSameTxn() throws java.lang.Exception {
        java.lang.Long clusterId = helper.createCluster();
        org.junit.Assert.assertNotNull(clusterId);
        org.apache.ambari.server.state.Cluster cluster = clusters.getCluster(org.apache.ambari.server.orm.OrmTestHelper.CLUSTER_NAME);
        org.junit.Assert.assertNotNull(cluster);
        javax.persistence.EntityManager em = helper.getEntityManager();
        final javax.persistence.EntityTransaction txn = em.getTransaction();
        txn.begin();
        org.apache.ambari.server.orm.entities.ClusterEntity entity = clusterDAO.findById(clusterId);
        entity.setProvisioningState(org.apache.ambari.server.state.State.DISABLED);
        clusterDAO.merge(entity);
        org.junit.Assert.assertEquals(org.apache.ambari.server.state.State.DISABLED, entity.getProvisioningState());
        entity = clusterDAO.findById(clusterId);
        org.junit.Assert.assertEquals(org.apache.ambari.server.state.State.DISABLED, entity.getProvisioningState());
        entity.setProvisioningState(org.apache.ambari.server.state.State.INIT);
        txn.commit();
        entity = clusterDAO.findById(clusterId);
        org.junit.Assert.assertEquals(org.apache.ambari.server.state.State.INIT, entity.getProvisioningState());
    }
}