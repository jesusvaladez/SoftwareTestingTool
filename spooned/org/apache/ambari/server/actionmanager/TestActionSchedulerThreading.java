package org.apache.ambari.server.actionmanager;
import com.google.inject.persist.UnitOfWork;
import javax.persistence.EntityManager;
public class TestActionSchedulerThreading {
    private static com.google.inject.Injector injector;

    private org.apache.ambari.server.state.Clusters clusters;

    private org.apache.ambari.server.orm.OrmTestHelper ormTestHelper;

    @org.junit.Before
    public void setup() throws java.lang.Exception {
        org.apache.ambari.server.actionmanager.TestActionSchedulerThreading.injector = com.google.inject.Guice.createInjector(new org.apache.ambari.server.orm.InMemoryDefaultTestModule());
        org.apache.ambari.server.actionmanager.TestActionSchedulerThreading.injector.getInstance(org.apache.ambari.server.orm.GuiceJpaInitializer.class);
        clusters = org.apache.ambari.server.actionmanager.TestActionSchedulerThreading.injector.getInstance(org.apache.ambari.server.state.Clusters.class);
        ormTestHelper = org.apache.ambari.server.actionmanager.TestActionSchedulerThreading.injector.getInstance(org.apache.ambari.server.orm.OrmTestHelper.class);
    }

    @org.junit.After
    public void tearDown() throws org.apache.ambari.server.AmbariException, java.sql.SQLException {
        org.apache.ambari.server.H2DatabaseCleaner.clearDatabaseAndStopPersistenceService(org.apache.ambari.server.actionmanager.TestActionSchedulerThreading.injector);
    }

    @org.junit.Test
    public void testDesiredConfigurationsAfterApplyingLatestForStackInOtherThreads() throws java.lang.Exception {
        long clusterId = ormTestHelper.createCluster(java.util.UUID.randomUUID().toString());
        org.apache.ambari.server.state.Cluster cluster = clusters.getCluster(clusterId);
        ormTestHelper.addHost(clusters, cluster, "h1");
        org.apache.ambari.server.state.StackId stackId = cluster.getCurrentStackVersion();
        org.apache.ambari.server.state.StackId newStackId = new org.apache.ambari.server.state.StackId("HDP-2.2.0");
        org.apache.ambari.server.orm.entities.RepositoryVersionEntity repoVersion220 = ormTestHelper.getOrCreateRepositoryVersion(newStackId, "2.2.0-1234");
        junit.framework.Assert.assertFalse(stackId.equals(newStackId));
        java.lang.String serviceName = "ZOOKEEPER";
        org.apache.ambari.server.orm.entities.RepositoryVersionEntity repositoryVersion = ormTestHelper.getOrCreateRepositoryVersion(cluster);
        org.apache.ambari.server.state.Service service = cluster.addService(serviceName, repositoryVersion);
        java.lang.String configType = "zoo.cfg";
        java.util.Map<java.lang.String, java.lang.String> properties = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> propertiesAttributes = new java.util.HashMap<>();
        org.apache.ambari.server.state.ConfigFactory configFactory = org.apache.ambari.server.actionmanager.TestActionSchedulerThreading.injector.getInstance(org.apache.ambari.server.state.ConfigFactory.class);
        properties.put("foo-property-1", "foo-value-1");
        org.apache.ambari.server.state.Config c1 = configFactory.createNew(stackId, cluster, configType, "version-1", properties, propertiesAttributes);
        cluster.addDesiredConfig("admin", com.google.common.collect.Sets.newHashSet(c1), "note-1");
        service.setDesiredRepositoryVersion(repoVersion220);
        properties.put("foo-property-2", "foo-value-2");
        org.apache.ambari.server.state.Config c2 = configFactory.createNew(newStackId, cluster, configType, "version-2", properties, propertiesAttributes);
        cluster.addDesiredConfig("admin", com.google.common.collect.Sets.newHashSet(c2), "note-2");
        java.util.Map<java.lang.String, org.apache.ambari.server.state.DesiredConfig> desiredConfigs = cluster.getDesiredConfigs();
        org.apache.ambari.server.state.DesiredConfig desiredConfig = desiredConfigs.get(configType);
        desiredConfig = desiredConfigs.get(configType);
        org.junit.Assert.assertNotNull(desiredConfig);
        org.junit.Assert.assertEquals(java.lang.Long.valueOf(2), desiredConfig.getVersion());
        org.junit.Assert.assertEquals("version-2", desiredConfig.getTag());
        final java.lang.String hostName = cluster.getHosts().iterator().next().getHostName();
        service.setDesiredRepositoryVersion(repositoryVersion);
        java.util.concurrent.Semaphore applyLatestConfigsSemaphore = new java.util.concurrent.Semaphore(1, true);
        java.util.concurrent.Semaphore threadInitialCachingSemaphore = new java.util.concurrent.Semaphore(1, true);
        threadInitialCachingSemaphore.acquire();
        applyLatestConfigsSemaphore.acquire();
        final org.apache.ambari.server.actionmanager.TestActionSchedulerThreading.InstrumentedActionScheduler runnable = new org.apache.ambari.server.actionmanager.TestActionSchedulerThreading.InstrumentedActionScheduler(clusterId, hostName, threadInitialCachingSemaphore, applyLatestConfigsSemaphore);
        org.apache.ambari.server.actionmanager.TestActionSchedulerThreading.injector.injectMembers(runnable);
        final java.lang.Thread thread = new java.lang.Thread(runnable);
        thread.start();
        threadInitialCachingSemaphore.acquire();
        cluster.applyLatestConfigurations(stackId, serviceName);
        applyLatestConfigsSemaphore.release();
        thread.join();
        runnable.validateAssertions();
    }

    private static final class InstrumentedActionScheduler extends org.apache.ambari.server.actionmanager.ActionScheduler {
        private final long clusterId;

        private final java.util.concurrent.Semaphore threadInitialCachingSemaphore;

        private final java.util.concurrent.Semaphore applyLatestConfigsSemaphore;

        private final java.lang.String hostName;

        private java.lang.Throwable throwable = null;

        @com.google.inject.Inject
        private org.apache.ambari.server.state.ConfigHelper configHelper;

        @com.google.inject.Inject
        private org.apache.ambari.server.state.Clusters clusters;

        @com.google.inject.Inject
        private com.google.inject.persist.UnitOfWork unitOfWork;

        private InstrumentedActionScheduler(long clusterId, java.lang.String hostName, java.util.concurrent.Semaphore threadInitialCachingSemaphore, java.util.concurrent.Semaphore applyLatestConfigsSemaphore) {
            super(1000, 1000, org.apache.ambari.server.actionmanager.TestActionSchedulerThreading.injector.getInstance(org.apache.ambari.server.actionmanager.ActionDBAccessor.class), org.apache.ambari.server.actionmanager.TestActionSchedulerThreading.injector.getInstance(org.apache.ambari.server.events.publishers.JPAEventPublisher.class));
            this.clusterId = clusterId;
            this.threadInitialCachingSemaphore = threadInitialCachingSemaphore;
            this.applyLatestConfigsSemaphore = applyLatestConfigsSemaphore;
            this.hostName = hostName;
        }

        @java.lang.Override
        public void run() {
            unitOfWork.begin();
            try {
                threadEntityManager = entityManagerProvider.get();
                org.apache.ambari.server.state.Cluster cluster = clusters.getCluster(clusterId);
                java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> effectiveDesiredTags = configHelper.getEffectiveDesiredTags(cluster, hostName);
                org.junit.Assert.assertEquals("version-2", effectiveDesiredTags.get("zoo.cfg").get("tag"));
                threadInitialCachingSemaphore.release();
                applyLatestConfigsSemaphore.acquire();
                effectiveDesiredTags = configHelper.getEffectiveDesiredTags(cluster, hostName);
                org.junit.Assert.assertEquals("version-1", effectiveDesiredTags.get("zoo.cfg").get("tag"));
            } catch (java.lang.Throwable throwable) {
                this.throwable = throwable;
            } finally {
                applyLatestConfigsSemaphore.release();
                unitOfWork.end();
            }
        }

        private void validateAssertions() {
            if (null != throwable) {
                throw new java.lang.AssertionError(throwable);
            }
        }
    }
}