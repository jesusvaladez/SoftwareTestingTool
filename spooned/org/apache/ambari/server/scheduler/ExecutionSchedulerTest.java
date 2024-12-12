package org.apache.ambari.server.scheduler;
import org.powermock.api.easymock.PowerMock;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.quartz.Scheduler;
import org.quartz.impl.StdSchedulerFactory;
import static org.easymock.EasyMock.expect;
import static org.mockito.Mockito.spy;
import static org.powermock.api.easymock.PowerMock.createNiceMock;
import static org.powermock.api.easymock.PowerMock.expectNew;
import static org.powermock.api.easymock.PowerMock.expectPrivate;
@org.junit.runner.RunWith(org.powermock.modules.junit4.PowerMockRunner.class)
@org.powermock.core.classloader.annotations.PowerMockIgnore("javax.management.*")
public class ExecutionSchedulerTest {
    private org.apache.ambari.server.configuration.Configuration configuration;

    @org.junit.Before
    public void setup() throws java.lang.Exception {
        java.util.Properties properties = new java.util.Properties();
        properties.setProperty(org.apache.ambari.server.configuration.Configuration.EXECUTION_SCHEDULER_THREADS.getKey(), "2");
        properties.setProperty(org.apache.ambari.server.configuration.Configuration.EXECUTION_SCHEDULER_CLUSTERED.getKey(), "false");
        properties.setProperty(org.apache.ambari.server.configuration.Configuration.EXECUTION_SCHEDULER_CONNECTIONS.getKey(), "2");
        properties.setProperty(org.apache.ambari.server.configuration.Configuration.SERVER_JDBC_DRIVER.getKey(), "db.driver");
        properties.setProperty(org.apache.ambari.server.configuration.Configuration.SERVER_JDBC_URL.getKey(), "jdbc:postgresql://localhost/");
        properties.setProperty(org.apache.ambari.server.configuration.Configuration.SERVER_JDBC_USER_NAME.getKey(), "user");
        properties.setProperty(org.apache.ambari.server.configuration.Configuration.SERVER_DB_NAME.getKey(), "derby");
        configuration = new org.apache.ambari.server.configuration.Configuration(properties);
    }

    @org.junit.After
    public void teardown() throws java.lang.Exception {
    }

    @org.junit.Test
    @org.powermock.core.classloader.annotations.PrepareForTest({ org.apache.ambari.server.scheduler.ExecutionSchedulerImpl.class })
    public void testSchedulerInitialize() throws java.lang.Exception {
        org.apache.ambari.server.scheduler.ExecutionSchedulerImpl executionScheduler = Mockito.spy(new org.apache.ambari.server.scheduler.ExecutionSchedulerImpl(configuration));
        java.util.Properties actualProperties = executionScheduler.getQuartzSchedulerProperties();
        junit.framework.Assert.assertEquals("2", actualProperties.getProperty("org.quartz.threadPool.threadCount"));
        junit.framework.Assert.assertEquals("2", actualProperties.getProperty("org.quartz.dataSource.myDS.maxConnections"));
        junit.framework.Assert.assertEquals("false", actualProperties.getProperty("org.quartz.jobStore.isClustered"));
        junit.framework.Assert.assertEquals("org.quartz.impl.jdbcjobstore.PostgreSQLDelegate", actualProperties.getProperty("org.quartz.jobStore.driverDelegateClass"));
        junit.framework.Assert.assertEquals("select 0", actualProperties.getProperty("org.quartz.dataSource.myDS.validationQuery"));
        junit.framework.Assert.assertEquals(org.apache.ambari.server.scheduler.ExecutionSchedulerImpl.DEFAULT_SCHEDULER_NAME, actualProperties.getProperty("org.quartz.scheduler.instanceName"));
        junit.framework.Assert.assertEquals("org.quartz.simpl.SimpleThreadPool", actualProperties.getProperty("org.quartz.threadPool.class"));
    }

    @org.junit.Test
    @org.powermock.core.classloader.annotations.PrepareForTest({ org.apache.ambari.server.scheduler.ExecutionSchedulerImpl.class })
    public void testSchedulerStartStop() throws java.lang.Exception {
        org.quartz.impl.StdSchedulerFactory factory = createNiceMock(org.quartz.impl.StdSchedulerFactory.class);
        org.quartz.Scheduler scheduler = createNiceMock(org.quartz.Scheduler.class);
        EasyMock.expect(factory.getScheduler()).andReturn(scheduler);
        expectPrivate(scheduler, "startDelayed", new java.lang.Integer(180)).once();
        expectNew(org.quartz.impl.StdSchedulerFactory.class).andReturn(factory);
        expectPrivate(scheduler, "shutdown").once();
        org.powermock.api.easymock.PowerMock.replay(factory, org.quartz.impl.StdSchedulerFactory.class, scheduler);
        org.apache.ambari.server.scheduler.ExecutionSchedulerImpl executionScheduler = new org.apache.ambari.server.scheduler.ExecutionSchedulerImpl(configuration);
        executionScheduler.startScheduler(180);
        executionScheduler.stopScheduler();
        org.powermock.api.easymock.PowerMock.verify(factory, org.quartz.impl.StdSchedulerFactory.class, scheduler);
        junit.framework.Assert.assertTrue(executionScheduler.isInitialized());
    }

    @org.junit.Test
    public void testGetQuartzDbDelegateClassAndValidationQuery() throws java.lang.Exception {
        java.util.Properties testProperties = new java.util.Properties();
        testProperties.setProperty(org.apache.ambari.server.configuration.Configuration.SERVER_JDBC_URL.getKey(), "jdbc:postgresql://host:port/dbname");
        testProperties.setProperty(org.apache.ambari.server.configuration.Configuration.SERVER_DB_NAME.getKey(), "ambari");
        org.apache.ambari.server.configuration.Configuration configuration1 = new org.apache.ambari.server.configuration.Configuration(testProperties);
        org.apache.ambari.server.scheduler.ExecutionSchedulerImpl executionScheduler = Mockito.spy(new org.apache.ambari.server.scheduler.ExecutionSchedulerImpl(configuration1));
        java.lang.String[] subProps = executionScheduler.getQuartzDbDelegateClassAndValidationQuery();
        junit.framework.Assert.assertEquals("org.quartz.impl.jdbcjobstore.PostgreSQLDelegate", subProps[0]);
        junit.framework.Assert.assertEquals("select 0", subProps[1]);
        testProperties.setProperty(org.apache.ambari.server.configuration.Configuration.SERVER_JDBC_URL.getKey(), "jdbc:mysql://host:port/dbname");
        configuration1 = new org.apache.ambari.server.configuration.Configuration(testProperties);
        executionScheduler = Mockito.spy(new org.apache.ambari.server.scheduler.ExecutionSchedulerImpl(configuration1));
        subProps = executionScheduler.getQuartzDbDelegateClassAndValidationQuery();
        junit.framework.Assert.assertEquals("org.quartz.impl.jdbcjobstore.StdJDBCDelegate", subProps[0]);
        junit.framework.Assert.assertEquals("select 0", subProps[1]);
        testProperties.setProperty(org.apache.ambari.server.configuration.Configuration.SERVER_JDBC_URL.getKey(), "jdbc:oracle:thin://host:port/dbname");
        configuration1 = new org.apache.ambari.server.configuration.Configuration(testProperties);
        executionScheduler = Mockito.spy(new org.apache.ambari.server.scheduler.ExecutionSchedulerImpl(configuration1));
        subProps = executionScheduler.getQuartzDbDelegateClassAndValidationQuery();
        junit.framework.Assert.assertEquals("org.quartz.impl.jdbcjobstore.oracle.OracleDelegate", subProps[0]);
        junit.framework.Assert.assertEquals("select 0 from dual", subProps[1]);
    }

    @org.junit.Test
    @org.powermock.core.classloader.annotations.PrepareForTest({ org.apache.ambari.server.scheduler.ExecutionSchedulerImpl.class })
    public void testSchedulerStartDelay() throws java.lang.Exception {
        org.quartz.impl.StdSchedulerFactory factory = createNiceMock(org.quartz.impl.StdSchedulerFactory.class);
        org.quartz.Scheduler scheduler = createNiceMock(org.quartz.Scheduler.class);
        EasyMock.expect(factory.getScheduler()).andReturn(scheduler).anyTimes();
        expectNew(org.quartz.impl.StdSchedulerFactory.class).andReturn(factory);
        EasyMock.expect(scheduler.isStarted()).andReturn(false).anyTimes();
        expectPrivate(scheduler, "startDelayed", new java.lang.Integer(180)).once();
        expectPrivate(scheduler, "start").once();
        org.powermock.api.easymock.PowerMock.replay(factory, org.quartz.impl.StdSchedulerFactory.class, scheduler);
        org.apache.ambari.server.scheduler.ExecutionSchedulerImpl executionScheduler = new org.apache.ambari.server.scheduler.ExecutionSchedulerImpl(configuration);
        executionScheduler.startScheduler(180);
        executionScheduler.startScheduler(null);
        org.powermock.api.easymock.PowerMock.verify(factory, org.quartz.impl.StdSchedulerFactory.class, scheduler);
        junit.framework.Assert.assertTrue(executionScheduler.isInitialized());
    }
}