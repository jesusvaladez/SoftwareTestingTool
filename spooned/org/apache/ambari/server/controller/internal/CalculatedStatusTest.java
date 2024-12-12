package org.apache.ambari.server.controller.internal;
import javax.persistence.EntityManager;
import static org.easymock.EasyMock.createNiceMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
@java.lang.SuppressWarnings("unchecked")
public class CalculatedStatusTest {
    private com.google.inject.Injector m_injector;

    @com.google.inject.Inject
    org.apache.ambari.server.actionmanager.HostRoleCommandFactory hostRoleCommandFactory;

    @com.google.inject.Inject
    org.apache.ambari.server.actionmanager.ExecutionCommandWrapperFactory ecwFactory;

    private static long taskId = 0L;

    private static long stageId = 0L;

    private static java.lang.reflect.Field s_field;

    @org.junit.Before
    public void setup() throws java.lang.Exception {
        m_injector = com.google.inject.Guice.createInjector(new org.apache.ambari.server.orm.InMemoryDefaultTestModule());
        m_injector.getInstance(org.apache.ambari.server.orm.GuiceJpaInitializer.class);
        m_injector.injectMembers(this);
        org.apache.ambari.server.controller.internal.CalculatedStatusTest.s_field = org.apache.ambari.server.actionmanager.HostRoleCommand.class.getDeclaredField("taskId");
        org.apache.ambari.server.controller.internal.CalculatedStatusTest.s_field.setAccessible(true);
    }

    @org.junit.After
    public void after() throws java.lang.Exception {
        org.apache.ambari.server.H2DatabaseCleaner.clearDatabase(m_injector.getProvider(javax.persistence.EntityManager.class).get());
    }

    @org.junit.Test
    public void testGetStatus() throws java.lang.Exception {
        java.util.Collection<org.apache.ambari.server.orm.entities.HostRoleCommandEntity> tasks = getTaskEntities(org.apache.ambari.server.actionmanager.HostRoleStatus.COMPLETED, org.apache.ambari.server.actionmanager.HostRoleStatus.COMPLETED, org.apache.ambari.server.actionmanager.HostRoleStatus.PENDING, org.apache.ambari.server.actionmanager.HostRoleStatus.PENDING, org.apache.ambari.server.actionmanager.HostRoleStatus.PENDING);
        org.apache.ambari.server.controller.internal.CalculatedStatus status = org.apache.ambari.server.controller.internal.CalculatedStatus.statusFromTaskEntities(tasks, false);
        org.junit.Assert.assertEquals(org.apache.ambari.server.actionmanager.HostRoleStatus.IN_PROGRESS, status.getStatus());
    }

    @org.junit.Test
    public void testGetPercent() throws java.lang.Exception {
        java.util.Collection<org.apache.ambari.server.orm.entities.HostRoleCommandEntity> tasks = getTaskEntities(org.apache.ambari.server.actionmanager.HostRoleStatus.COMPLETED, org.apache.ambari.server.actionmanager.HostRoleStatus.COMPLETED, org.apache.ambari.server.actionmanager.HostRoleStatus.PENDING, org.apache.ambari.server.actionmanager.HostRoleStatus.PENDING, org.apache.ambari.server.actionmanager.HostRoleStatus.PENDING);
        org.apache.ambari.server.controller.internal.CalculatedStatus status = org.apache.ambari.server.controller.internal.CalculatedStatus.statusFromTaskEntities(tasks, false);
        org.junit.Assert.assertEquals(40.0, status.getPercent(), 0.1);
    }

    @org.junit.Test
    public void testStatusFromTaskEntities() throws java.lang.Exception {
        java.util.Collection<org.apache.ambari.server.orm.entities.HostRoleCommandEntity> tasks = getTaskEntities(org.apache.ambari.server.actionmanager.HostRoleStatus.PENDING, org.apache.ambari.server.actionmanager.HostRoleStatus.PENDING, org.apache.ambari.server.actionmanager.HostRoleStatus.PENDING, org.apache.ambari.server.actionmanager.HostRoleStatus.PENDING);
        org.apache.ambari.server.controller.internal.CalculatedStatus status = org.apache.ambari.server.controller.internal.CalculatedStatus.statusFromTaskEntities(tasks, false);
        org.junit.Assert.assertEquals(org.apache.ambari.server.actionmanager.HostRoleStatus.PENDING, status.getStatus());
        org.junit.Assert.assertEquals(0.0, status.getPercent(), 0.1);
        tasks = getTaskEntities(org.apache.ambari.server.actionmanager.HostRoleStatus.COMPLETED, org.apache.ambari.server.actionmanager.HostRoleStatus.FAILED, org.apache.ambari.server.actionmanager.HostRoleStatus.ABORTED, org.apache.ambari.server.actionmanager.HostRoleStatus.ABORTED, org.apache.ambari.server.actionmanager.HostRoleStatus.ABORTED);
        status = org.apache.ambari.server.controller.internal.CalculatedStatus.statusFromTaskEntities(tasks, false);
        org.junit.Assert.assertEquals(org.apache.ambari.server.actionmanager.HostRoleStatus.FAILED, status.getStatus());
        org.junit.Assert.assertEquals(100.0, status.getPercent(), 0.1);
        tasks = getTaskEntities(org.apache.ambari.server.actionmanager.HostRoleStatus.COMPLETED, org.apache.ambari.server.actionmanager.HostRoleStatus.FAILED, org.apache.ambari.server.actionmanager.HostRoleStatus.FAILED, org.apache.ambari.server.actionmanager.HostRoleStatus.FAILED, org.apache.ambari.server.actionmanager.HostRoleStatus.FAILED);
        status = org.apache.ambari.server.controller.internal.CalculatedStatus.statusFromTaskEntities(tasks, true);
        org.junit.Assert.assertEquals(org.apache.ambari.server.actionmanager.HostRoleStatus.COMPLETED, status.getStatus());
        org.junit.Assert.assertEquals(100.0, status.getPercent(), 0.1);
        tasks = getTaskEntities(org.apache.ambari.server.actionmanager.HostRoleStatus.COMPLETED, org.apache.ambari.server.actionmanager.HostRoleStatus.TIMEDOUT, org.apache.ambari.server.actionmanager.HostRoleStatus.TIMEDOUT, org.apache.ambari.server.actionmanager.HostRoleStatus.TIMEDOUT, org.apache.ambari.server.actionmanager.HostRoleStatus.TIMEDOUT);
        status = org.apache.ambari.server.controller.internal.CalculatedStatus.statusFromTaskEntities(tasks, false);
        org.junit.Assert.assertEquals(org.apache.ambari.server.actionmanager.HostRoleStatus.TIMEDOUT, status.getStatus());
        org.junit.Assert.assertEquals(100.0, status.getPercent(), 0.1);
        tasks = getTaskEntities(org.apache.ambari.server.actionmanager.HostRoleStatus.COMPLETED, org.apache.ambari.server.actionmanager.HostRoleStatus.TIMEDOUT, org.apache.ambari.server.actionmanager.HostRoleStatus.FAILED, org.apache.ambari.server.actionmanager.HostRoleStatus.TIMEDOUT, org.apache.ambari.server.actionmanager.HostRoleStatus.TIMEDOUT);
        status = org.apache.ambari.server.controller.internal.CalculatedStatus.statusFromTaskEntities(tasks, true);
        org.junit.Assert.assertEquals(org.apache.ambari.server.actionmanager.HostRoleStatus.COMPLETED, status.getStatus());
        org.junit.Assert.assertEquals(100.0, status.getPercent(), 0.1);
        tasks = getTaskEntities(org.apache.ambari.server.actionmanager.HostRoleStatus.COMPLETED, org.apache.ambari.server.actionmanager.HostRoleStatus.ABORTED, org.apache.ambari.server.actionmanager.HostRoleStatus.ABORTED, org.apache.ambari.server.actionmanager.HostRoleStatus.ABORTED, org.apache.ambari.server.actionmanager.HostRoleStatus.ABORTED);
        status = org.apache.ambari.server.controller.internal.CalculatedStatus.statusFromTaskEntities(tasks, false);
        org.junit.Assert.assertEquals(org.apache.ambari.server.actionmanager.HostRoleStatus.ABORTED, status.getStatus());
        org.junit.Assert.assertEquals(100.0, status.getPercent(), 0.1);
        tasks = getTaskEntities(org.apache.ambari.server.actionmanager.HostRoleStatus.COMPLETED, org.apache.ambari.server.actionmanager.HostRoleStatus.ABORTED, org.apache.ambari.server.actionmanager.HostRoleStatus.ABORTED, org.apache.ambari.server.actionmanager.HostRoleStatus.ABORTED, org.apache.ambari.server.actionmanager.HostRoleStatus.ABORTED);
        status = org.apache.ambari.server.controller.internal.CalculatedStatus.statusFromTaskEntities(tasks, true);
        org.junit.Assert.assertEquals(org.apache.ambari.server.actionmanager.HostRoleStatus.ABORTED, status.getStatus());
        org.junit.Assert.assertEquals(100.0, status.getPercent(), 0.1);
        tasks = getTaskEntities(org.apache.ambari.server.actionmanager.HostRoleStatus.COMPLETED, org.apache.ambari.server.actionmanager.HostRoleStatus.COMPLETED, org.apache.ambari.server.actionmanager.HostRoleStatus.PENDING, org.apache.ambari.server.actionmanager.HostRoleStatus.PENDING, org.apache.ambari.server.actionmanager.HostRoleStatus.PENDING);
        status = org.apache.ambari.server.controller.internal.CalculatedStatus.statusFromTaskEntities(tasks, false);
        org.junit.Assert.assertEquals(org.apache.ambari.server.actionmanager.HostRoleStatus.IN_PROGRESS, status.getStatus());
        org.junit.Assert.assertEquals(40.0, status.getPercent(), 0.1);
        tasks = getTaskEntities(org.apache.ambari.server.actionmanager.HostRoleStatus.COMPLETED, org.apache.ambari.server.actionmanager.HostRoleStatus.COMPLETED, org.apache.ambari.server.actionmanager.HostRoleStatus.COMPLETED, org.apache.ambari.server.actionmanager.HostRoleStatus.COMPLETED, org.apache.ambari.server.actionmanager.HostRoleStatus.COMPLETED);
        status = org.apache.ambari.server.controller.internal.CalculatedStatus.statusFromTaskEntities(tasks, false);
        org.junit.Assert.assertEquals(org.apache.ambari.server.actionmanager.HostRoleStatus.COMPLETED, status.getStatus());
        org.junit.Assert.assertEquals(100.0, status.getPercent(), 0.1);
        tasks = getTaskEntities(org.apache.ambari.server.actionmanager.HostRoleStatus.COMPLETED, org.apache.ambari.server.actionmanager.HostRoleStatus.COMPLETED, org.apache.ambari.server.actionmanager.HostRoleStatus.HOLDING, org.apache.ambari.server.actionmanager.HostRoleStatus.PENDING, org.apache.ambari.server.actionmanager.HostRoleStatus.PENDING);
        status = org.apache.ambari.server.controller.internal.CalculatedStatus.statusFromTaskEntities(tasks, false);
        org.junit.Assert.assertEquals(org.apache.ambari.server.actionmanager.HostRoleStatus.HOLDING, status.getStatus());
        org.junit.Assert.assertEquals(54.0, status.getPercent(), 0.1);
        tasks = getTaskEntities(org.apache.ambari.server.actionmanager.HostRoleStatus.COMPLETED, org.apache.ambari.server.actionmanager.HostRoleStatus.COMPLETED, org.apache.ambari.server.actionmanager.HostRoleStatus.HOLDING_FAILED, org.apache.ambari.server.actionmanager.HostRoleStatus.PENDING, org.apache.ambari.server.actionmanager.HostRoleStatus.PENDING);
        status = org.apache.ambari.server.controller.internal.CalculatedStatus.statusFromTaskEntities(tasks, false);
        org.junit.Assert.assertEquals(org.apache.ambari.server.actionmanager.HostRoleStatus.HOLDING_FAILED, status.getStatus());
        org.junit.Assert.assertEquals(54.0, status.getPercent(), 0.1);
        tasks = getTaskEntities(org.apache.ambari.server.actionmanager.HostRoleStatus.COMPLETED, org.apache.ambari.server.actionmanager.HostRoleStatus.COMPLETED, org.apache.ambari.server.actionmanager.HostRoleStatus.HOLDING_TIMEDOUT, org.apache.ambari.server.actionmanager.HostRoleStatus.PENDING, org.apache.ambari.server.actionmanager.HostRoleStatus.PENDING);
        status = org.apache.ambari.server.controller.internal.CalculatedStatus.statusFromTaskEntities(tasks, false);
        org.junit.Assert.assertEquals(org.apache.ambari.server.actionmanager.HostRoleStatus.HOLDING_TIMEDOUT, status.getStatus());
        org.junit.Assert.assertEquals(54.0, status.getPercent(), 0.1);
    }

    @org.junit.Test
    public void testAbortedCalculation() throws java.lang.Exception {
        java.util.Collection<org.apache.ambari.server.orm.entities.HostRoleCommandEntity> tasks = getTaskEntities(org.apache.ambari.server.actionmanager.HostRoleStatus.ABORTED);
        org.apache.ambari.server.controller.internal.CalculatedStatus status = org.apache.ambari.server.controller.internal.CalculatedStatus.statusFromTaskEntities(tasks, false);
        org.junit.Assert.assertEquals(org.apache.ambari.server.actionmanager.HostRoleStatus.ABORTED, status.getStatus());
        org.junit.Assert.assertEquals(100.0, status.getPercent(), 0.1);
        status = org.apache.ambari.server.controller.internal.CalculatedStatus.statusFromTaskEntities(tasks, true);
        org.junit.Assert.assertEquals(org.apache.ambari.server.actionmanager.HostRoleStatus.ABORTED, status.getStatus());
        org.junit.Assert.assertEquals(100.0, status.getPercent(), 0.1);
        tasks = getTaskEntities(org.apache.ambari.server.actionmanager.HostRoleStatus.COMPLETED, org.apache.ambari.server.actionmanager.HostRoleStatus.ABORTED);
        status = org.apache.ambari.server.controller.internal.CalculatedStatus.statusFromTaskEntities(tasks, false);
        org.junit.Assert.assertEquals(org.apache.ambari.server.actionmanager.HostRoleStatus.ABORTED, status.getStatus());
        org.junit.Assert.assertEquals(100.0, status.getPercent(), 0.1);
        status = org.apache.ambari.server.controller.internal.CalculatedStatus.statusFromTaskEntities(tasks, true);
        org.junit.Assert.assertEquals(org.apache.ambari.server.actionmanager.HostRoleStatus.ABORTED, status.getStatus());
        org.junit.Assert.assertEquals(100.0, status.getPercent(), 0.1);
    }

    @org.junit.Test
    public void testStatusFromStageEntities() throws java.lang.Exception {
        java.util.Collection<org.apache.ambari.server.orm.entities.StageEntity> stages = getStageEntities(getTaskEntities(org.apache.ambari.server.actionmanager.HostRoleStatus.COMPLETED, org.apache.ambari.server.actionmanager.HostRoleStatus.COMPLETED, org.apache.ambari.server.actionmanager.HostRoleStatus.COMPLETED), getTaskEntities(org.apache.ambari.server.actionmanager.HostRoleStatus.COMPLETED, org.apache.ambari.server.actionmanager.HostRoleStatus.COMPLETED, org.apache.ambari.server.actionmanager.HostRoleStatus.COMPLETED), getTaskEntities(org.apache.ambari.server.actionmanager.HostRoleStatus.COMPLETED, org.apache.ambari.server.actionmanager.HostRoleStatus.COMPLETED, org.apache.ambari.server.actionmanager.HostRoleStatus.COMPLETED));
        org.apache.ambari.server.controller.internal.CalculatedStatus status = org.apache.ambari.server.controller.internal.CalculatedStatus.statusFromStageEntities(stages);
        org.junit.Assert.assertEquals(org.apache.ambari.server.actionmanager.HostRoleStatus.COMPLETED, status.getStatus());
        org.junit.Assert.assertEquals(100.0, status.getPercent(), 0.1);
        stages = getStageEntities(getTaskEntities(org.apache.ambari.server.actionmanager.HostRoleStatus.COMPLETED, org.apache.ambari.server.actionmanager.HostRoleStatus.COMPLETED, org.apache.ambari.server.actionmanager.HostRoleStatus.COMPLETED), getTaskEntities(org.apache.ambari.server.actionmanager.HostRoleStatus.COMPLETED, org.apache.ambari.server.actionmanager.HostRoleStatus.IN_PROGRESS, org.apache.ambari.server.actionmanager.HostRoleStatus.PENDING), getTaskEntities(org.apache.ambari.server.actionmanager.HostRoleStatus.PENDING, org.apache.ambari.server.actionmanager.HostRoleStatus.PENDING, org.apache.ambari.server.actionmanager.HostRoleStatus.PENDING));
        status = org.apache.ambari.server.controller.internal.CalculatedStatus.statusFromStageEntities(stages);
        org.junit.Assert.assertEquals(org.apache.ambari.server.actionmanager.HostRoleStatus.IN_PROGRESS, status.getStatus());
        org.junit.Assert.assertEquals(48.3, status.getPercent(), 0.1);
        stages = getStageEntities(getTaskEntities(org.apache.ambari.server.actionmanager.HostRoleStatus.PENDING, org.apache.ambari.server.actionmanager.HostRoleStatus.PENDING, org.apache.ambari.server.actionmanager.HostRoleStatus.PENDING), getTaskEntities(org.apache.ambari.server.actionmanager.HostRoleStatus.PENDING, org.apache.ambari.server.actionmanager.HostRoleStatus.PENDING, org.apache.ambari.server.actionmanager.HostRoleStatus.PENDING), getTaskEntities(org.apache.ambari.server.actionmanager.HostRoleStatus.PENDING, org.apache.ambari.server.actionmanager.HostRoleStatus.PENDING, org.apache.ambari.server.actionmanager.HostRoleStatus.PENDING));
        status = org.apache.ambari.server.controller.internal.CalculatedStatus.statusFromStageEntities(stages);
        org.junit.Assert.assertEquals(org.apache.ambari.server.actionmanager.HostRoleStatus.PENDING, status.getStatus());
        org.junit.Assert.assertEquals(0.0, status.getPercent(), 0.1);
        stages = getStageEntities(getTaskEntities(org.apache.ambari.server.actionmanager.HostRoleStatus.COMPLETED, org.apache.ambari.server.actionmanager.HostRoleStatus.COMPLETED, org.apache.ambari.server.actionmanager.HostRoleStatus.COMPLETED), getTaskEntities(org.apache.ambari.server.actionmanager.HostRoleStatus.COMPLETED, org.apache.ambari.server.actionmanager.HostRoleStatus.FAILED, org.apache.ambari.server.actionmanager.HostRoleStatus.PENDING), getTaskEntities(org.apache.ambari.server.actionmanager.HostRoleStatus.PENDING, org.apache.ambari.server.actionmanager.HostRoleStatus.PENDING, org.apache.ambari.server.actionmanager.HostRoleStatus.PENDING));
        status = org.apache.ambari.server.controller.internal.CalculatedStatus.statusFromStageEntities(stages);
        org.junit.Assert.assertEquals(org.apache.ambari.server.actionmanager.HostRoleStatus.FAILED, status.getStatus());
        org.junit.Assert.assertEquals(55.55, status.getPercent(), 0.1);
        stages = getStageEntities(getTaskEntities(org.apache.ambari.server.actionmanager.HostRoleStatus.COMPLETED, org.apache.ambari.server.actionmanager.HostRoleStatus.COMPLETED, org.apache.ambari.server.actionmanager.HostRoleStatus.COMPLETED), getTaskEntities(org.apache.ambari.server.actionmanager.HostRoleStatus.COMPLETED, org.apache.ambari.server.actionmanager.HostRoleStatus.COMPLETED, org.apache.ambari.server.actionmanager.HostRoleStatus.TIMEDOUT), getTaskEntities(org.apache.ambari.server.actionmanager.HostRoleStatus.PENDING, org.apache.ambari.server.actionmanager.HostRoleStatus.PENDING, org.apache.ambari.server.actionmanager.HostRoleStatus.PENDING));
        status = org.apache.ambari.server.controller.internal.CalculatedStatus.statusFromStageEntities(stages);
        org.junit.Assert.assertEquals(org.apache.ambari.server.actionmanager.HostRoleStatus.TIMEDOUT, status.getStatus());
        org.junit.Assert.assertEquals(66.66, status.getPercent(), 0.1);
        stages = getStageEntities(getTaskEntities(org.apache.ambari.server.actionmanager.HostRoleStatus.COMPLETED, org.apache.ambari.server.actionmanager.HostRoleStatus.COMPLETED, org.apache.ambari.server.actionmanager.HostRoleStatus.COMPLETED), getTaskEntities(org.apache.ambari.server.actionmanager.HostRoleStatus.COMPLETED, org.apache.ambari.server.actionmanager.HostRoleStatus.COMPLETED, org.apache.ambari.server.actionmanager.HostRoleStatus.HOLDING), getTaskEntities(org.apache.ambari.server.actionmanager.HostRoleStatus.PENDING, org.apache.ambari.server.actionmanager.HostRoleStatus.PENDING, org.apache.ambari.server.actionmanager.HostRoleStatus.PENDING), getTaskEntities(org.apache.ambari.server.actionmanager.HostRoleStatus.PENDING, org.apache.ambari.server.actionmanager.HostRoleStatus.PENDING, org.apache.ambari.server.actionmanager.HostRoleStatus.PENDING));
        status = org.apache.ambari.server.controller.internal.CalculatedStatus.statusFromStageEntities(stages);
        org.junit.Assert.assertEquals(org.apache.ambari.server.actionmanager.HostRoleStatus.HOLDING, status.getStatus());
        org.junit.Assert.assertEquals(47.5, status.getPercent(), 0.1);
        stages = getStageEntities(getTaskEntities(org.apache.ambari.server.actionmanager.HostRoleStatus.COMPLETED, org.apache.ambari.server.actionmanager.HostRoleStatus.COMPLETED, org.apache.ambari.server.actionmanager.HostRoleStatus.COMPLETED), getTaskEntities(org.apache.ambari.server.actionmanager.HostRoleStatus.COMPLETED, org.apache.ambari.server.actionmanager.HostRoleStatus.COMPLETED, org.apache.ambari.server.actionmanager.HostRoleStatus.HOLDING), getTaskEntities(org.apache.ambari.server.actionmanager.HostRoleStatus.PENDING, org.apache.ambari.server.actionmanager.HostRoleStatus.PENDING, org.apache.ambari.server.actionmanager.HostRoleStatus.PENDING), getTaskEntities(org.apache.ambari.server.actionmanager.HostRoleStatus.PENDING, org.apache.ambari.server.actionmanager.HostRoleStatus.PENDING, org.apache.ambari.server.actionmanager.HostRoleStatus.PENDING));
        status = org.apache.ambari.server.controller.internal.CalculatedStatus.statusFromStageEntities(stages);
        org.junit.Assert.assertEquals(org.apache.ambari.server.actionmanager.HostRoleStatus.HOLDING, status.getStatus());
        org.junit.Assert.assertEquals(47.5, status.getPercent(), 0.1);
        stages = getStageEntities(getTaskEntities(org.apache.ambari.server.actionmanager.HostRoleStatus.COMPLETED, org.apache.ambari.server.actionmanager.HostRoleStatus.COMPLETED, org.apache.ambari.server.actionmanager.HostRoleStatus.COMPLETED), getTaskEntities(org.apache.ambari.server.actionmanager.HostRoleStatus.COMPLETED, org.apache.ambari.server.actionmanager.HostRoleStatus.COMPLETED, org.apache.ambari.server.actionmanager.HostRoleStatus.HOLDING), getTaskEntities(org.apache.ambari.server.actionmanager.HostRoleStatus.PENDING, org.apache.ambari.server.actionmanager.HostRoleStatus.PENDING, org.apache.ambari.server.actionmanager.HostRoleStatus.PENDING), getTaskEntities(org.apache.ambari.server.actionmanager.HostRoleStatus.PENDING, org.apache.ambari.server.actionmanager.HostRoleStatus.PENDING, org.apache.ambari.server.actionmanager.HostRoleStatus.PENDING));
        status = org.apache.ambari.server.controller.internal.CalculatedStatus.statusFromStageEntities(stages);
        org.junit.Assert.assertEquals(org.apache.ambari.server.actionmanager.HostRoleStatus.HOLDING, status.getStatus());
        org.junit.Assert.assertEquals(47.5, status.getPercent(), 0.1);
    }

    @org.junit.Test
    public void testStatusFromStages() throws java.lang.Exception {
        java.util.Collection<org.apache.ambari.server.actionmanager.Stage> stages;
        org.apache.ambari.server.controller.internal.CalculatedStatus status;
        stages = getStages(getTaskEntities(org.apache.ambari.server.actionmanager.HostRoleStatus.COMPLETED, org.apache.ambari.server.actionmanager.HostRoleStatus.COMPLETED, org.apache.ambari.server.actionmanager.HostRoleStatus.COMPLETED), getTaskEntities(org.apache.ambari.server.actionmanager.HostRoleStatus.COMPLETED, org.apache.ambari.server.actionmanager.HostRoleStatus.COMPLETED, org.apache.ambari.server.actionmanager.HostRoleStatus.COMPLETED), getTaskEntities(org.apache.ambari.server.actionmanager.HostRoleStatus.COMPLETED, org.apache.ambari.server.actionmanager.HostRoleStatus.COMPLETED, org.apache.ambari.server.actionmanager.HostRoleStatus.COMPLETED));
        status = org.apache.ambari.server.controller.internal.CalculatedStatus.statusFromStages(stages);
        org.junit.Assert.assertEquals(org.apache.ambari.server.actionmanager.HostRoleStatus.COMPLETED, status.getStatus());
        org.junit.Assert.assertEquals(100.0, status.getPercent(), 0.1);
        stages = getStages(getTaskEntities(org.apache.ambari.server.actionmanager.HostRoleStatus.COMPLETED, org.apache.ambari.server.actionmanager.HostRoleStatus.COMPLETED, org.apache.ambari.server.actionmanager.HostRoleStatus.COMPLETED), getTaskEntities(org.apache.ambari.server.actionmanager.HostRoleStatus.COMPLETED, org.apache.ambari.server.actionmanager.HostRoleStatus.IN_PROGRESS, org.apache.ambari.server.actionmanager.HostRoleStatus.PENDING), getTaskEntities(org.apache.ambari.server.actionmanager.HostRoleStatus.PENDING, org.apache.ambari.server.actionmanager.HostRoleStatus.PENDING, org.apache.ambari.server.actionmanager.HostRoleStatus.PENDING));
        status = org.apache.ambari.server.controller.internal.CalculatedStatus.statusFromStages(stages);
        org.junit.Assert.assertEquals(org.apache.ambari.server.actionmanager.HostRoleStatus.IN_PROGRESS, status.getStatus());
        org.junit.Assert.assertEquals(48.3, status.getPercent(), 0.1);
        stages = getStages(getTaskEntities(org.apache.ambari.server.actionmanager.HostRoleStatus.PENDING, org.apache.ambari.server.actionmanager.HostRoleStatus.PENDING, org.apache.ambari.server.actionmanager.HostRoleStatus.PENDING), getTaskEntities(org.apache.ambari.server.actionmanager.HostRoleStatus.PENDING, org.apache.ambari.server.actionmanager.HostRoleStatus.PENDING, org.apache.ambari.server.actionmanager.HostRoleStatus.PENDING), getTaskEntities(org.apache.ambari.server.actionmanager.HostRoleStatus.PENDING, org.apache.ambari.server.actionmanager.HostRoleStatus.PENDING, org.apache.ambari.server.actionmanager.HostRoleStatus.PENDING));
        status = org.apache.ambari.server.controller.internal.CalculatedStatus.statusFromStages(stages);
        org.junit.Assert.assertEquals(org.apache.ambari.server.actionmanager.HostRoleStatus.PENDING, status.getStatus());
        org.junit.Assert.assertEquals(0.0, status.getPercent(), 0.1);
        stages = getStages(getTaskEntities(org.apache.ambari.server.actionmanager.HostRoleStatus.COMPLETED, org.apache.ambari.server.actionmanager.HostRoleStatus.COMPLETED, org.apache.ambari.server.actionmanager.HostRoleStatus.COMPLETED), getTaskEntities(org.apache.ambari.server.actionmanager.HostRoleStatus.COMPLETED, org.apache.ambari.server.actionmanager.HostRoleStatus.FAILED, org.apache.ambari.server.actionmanager.HostRoleStatus.PENDING), getTaskEntities(org.apache.ambari.server.actionmanager.HostRoleStatus.PENDING, org.apache.ambari.server.actionmanager.HostRoleStatus.PENDING, org.apache.ambari.server.actionmanager.HostRoleStatus.PENDING));
        status = org.apache.ambari.server.controller.internal.CalculatedStatus.statusFromStages(stages);
        org.junit.Assert.assertEquals(org.apache.ambari.server.actionmanager.HostRoleStatus.FAILED, status.getStatus());
        org.junit.Assert.assertEquals(55.55, status.getPercent(), 0.1);
        stages = getStages(getTaskEntities(org.apache.ambari.server.actionmanager.HostRoleStatus.COMPLETED, org.apache.ambari.server.actionmanager.HostRoleStatus.COMPLETED, org.apache.ambari.server.actionmanager.HostRoleStatus.COMPLETED), getTaskEntities(org.apache.ambari.server.actionmanager.HostRoleStatus.COMPLETED, org.apache.ambari.server.actionmanager.HostRoleStatus.COMPLETED, org.apache.ambari.server.actionmanager.HostRoleStatus.TIMEDOUT), getTaskEntities(org.apache.ambari.server.actionmanager.HostRoleStatus.PENDING, org.apache.ambari.server.actionmanager.HostRoleStatus.PENDING, org.apache.ambari.server.actionmanager.HostRoleStatus.PENDING));
        status = org.apache.ambari.server.controller.internal.CalculatedStatus.statusFromStages(stages);
        org.junit.Assert.assertEquals(org.apache.ambari.server.actionmanager.HostRoleStatus.TIMEDOUT, status.getStatus());
        org.junit.Assert.assertEquals(66.66, status.getPercent(), 0.1);
        stages = getStages(getTaskEntities(org.apache.ambari.server.actionmanager.HostRoleStatus.COMPLETED, org.apache.ambari.server.actionmanager.HostRoleStatus.COMPLETED, org.apache.ambari.server.actionmanager.HostRoleStatus.COMPLETED), getTaskEntities(org.apache.ambari.server.actionmanager.HostRoleStatus.COMPLETED, org.apache.ambari.server.actionmanager.HostRoleStatus.COMPLETED, org.apache.ambari.server.actionmanager.HostRoleStatus.HOLDING), getTaskEntities(org.apache.ambari.server.actionmanager.HostRoleStatus.PENDING, org.apache.ambari.server.actionmanager.HostRoleStatus.PENDING, org.apache.ambari.server.actionmanager.HostRoleStatus.PENDING), getTaskEntities(org.apache.ambari.server.actionmanager.HostRoleStatus.PENDING, org.apache.ambari.server.actionmanager.HostRoleStatus.PENDING, org.apache.ambari.server.actionmanager.HostRoleStatus.PENDING));
        status = org.apache.ambari.server.controller.internal.CalculatedStatus.statusFromStages(stages);
        org.junit.Assert.assertEquals(org.apache.ambari.server.actionmanager.HostRoleStatus.HOLDING, status.getStatus());
        org.junit.Assert.assertEquals(47.5, status.getPercent(), 0.1);
        stages = getStages(getTaskEntities(org.apache.ambari.server.actionmanager.HostRoleStatus.COMPLETED, org.apache.ambari.server.actionmanager.HostRoleStatus.COMPLETED, org.apache.ambari.server.actionmanager.HostRoleStatus.COMPLETED), getTaskEntities(org.apache.ambari.server.actionmanager.HostRoleStatus.COMPLETED, org.apache.ambari.server.actionmanager.HostRoleStatus.COMPLETED, org.apache.ambari.server.actionmanager.HostRoleStatus.HOLDING), getTaskEntities(org.apache.ambari.server.actionmanager.HostRoleStatus.PENDING, org.apache.ambari.server.actionmanager.HostRoleStatus.PENDING, org.apache.ambari.server.actionmanager.HostRoleStatus.PENDING), getTaskEntities(org.apache.ambari.server.actionmanager.HostRoleStatus.PENDING, org.apache.ambari.server.actionmanager.HostRoleStatus.PENDING, org.apache.ambari.server.actionmanager.HostRoleStatus.PENDING));
        status = org.apache.ambari.server.controller.internal.CalculatedStatus.statusFromStages(stages);
        org.junit.Assert.assertEquals(org.apache.ambari.server.actionmanager.HostRoleStatus.HOLDING, status.getStatus());
        org.junit.Assert.assertEquals(47.5, status.getPercent(), 0.1);
        stages = getStages(getTaskEntities(org.apache.ambari.server.actionmanager.HostRoleStatus.COMPLETED, org.apache.ambari.server.actionmanager.HostRoleStatus.COMPLETED, org.apache.ambari.server.actionmanager.HostRoleStatus.COMPLETED), getTaskEntities(org.apache.ambari.server.actionmanager.HostRoleStatus.COMPLETED, org.apache.ambari.server.actionmanager.HostRoleStatus.COMPLETED, org.apache.ambari.server.actionmanager.HostRoleStatus.HOLDING), getTaskEntities(org.apache.ambari.server.actionmanager.HostRoleStatus.PENDING, org.apache.ambari.server.actionmanager.HostRoleStatus.PENDING, org.apache.ambari.server.actionmanager.HostRoleStatus.PENDING), getTaskEntities(org.apache.ambari.server.actionmanager.HostRoleStatus.PENDING, org.apache.ambari.server.actionmanager.HostRoleStatus.PENDING, org.apache.ambari.server.actionmanager.HostRoleStatus.PENDING));
        status = org.apache.ambari.server.controller.internal.CalculatedStatus.statusFromStages(stages);
        org.junit.Assert.assertEquals(org.apache.ambari.server.actionmanager.HostRoleStatus.HOLDING, status.getStatus());
        org.junit.Assert.assertNull(status.getDisplayStatus());
        org.junit.Assert.assertEquals(47.5, status.getPercent(), 0.1);
        stages = getStages(getTaskEntities(org.apache.ambari.server.actionmanager.HostRoleStatus.COMPLETED, org.apache.ambari.server.actionmanager.HostRoleStatus.COMPLETED, org.apache.ambari.server.actionmanager.HostRoleStatus.COMPLETED), getTaskEntities(org.apache.ambari.server.actionmanager.HostRoleStatus.COMPLETED, org.apache.ambari.server.actionmanager.HostRoleStatus.COMPLETED, org.apache.ambari.server.actionmanager.HostRoleStatus.ABORTED), getTaskEntities(org.apache.ambari.server.actionmanager.HostRoleStatus.ABORTED, org.apache.ambari.server.actionmanager.HostRoleStatus.ABORTED, org.apache.ambari.server.actionmanager.HostRoleStatus.ABORTED), getTaskEntities(org.apache.ambari.server.actionmanager.HostRoleStatus.ABORTED, org.apache.ambari.server.actionmanager.HostRoleStatus.ABORTED, org.apache.ambari.server.actionmanager.HostRoleStatus.ABORTED));
        status = org.apache.ambari.server.controller.internal.CalculatedStatus.statusFromStages(stages);
        org.junit.Assert.assertEquals(org.apache.ambari.server.actionmanager.HostRoleStatus.ABORTED, status.getStatus());
        org.junit.Assert.assertNull(status.getDisplayStatus());
        org.junit.Assert.assertEquals(100.0, status.getPercent(), 0.1);
        stages = getStages(getTaskEntities(org.apache.ambari.server.actionmanager.HostRoleStatus.COMPLETED, org.apache.ambari.server.actionmanager.HostRoleStatus.COMPLETED, org.apache.ambari.server.actionmanager.HostRoleStatus.COMPLETED), getTaskEntities(org.apache.ambari.server.actionmanager.HostRoleStatus.COMPLETED, org.apache.ambari.server.actionmanager.HostRoleStatus.COMPLETED, org.apache.ambari.server.actionmanager.HostRoleStatus.COMPLETED), getTaskEntities(org.apache.ambari.server.actionmanager.HostRoleStatus.ABORTED, org.apache.ambari.server.actionmanager.HostRoleStatus.ABORTED, org.apache.ambari.server.actionmanager.HostRoleStatus.PENDING), getTaskEntities(org.apache.ambari.server.actionmanager.HostRoleStatus.PENDING, org.apache.ambari.server.actionmanager.HostRoleStatus.PENDING, org.apache.ambari.server.actionmanager.HostRoleStatus.PENDING));
        status = org.apache.ambari.server.controller.internal.CalculatedStatus.statusFromStages(stages);
        org.junit.Assert.assertEquals(org.apache.ambari.server.actionmanager.HostRoleStatus.IN_PROGRESS, status.getStatus());
        org.junit.Assert.assertNull(status.getDisplayStatus());
        org.junit.Assert.assertEquals(66.6, status.getPercent(), 0.1);
    }

    @org.junit.Test
    public void testCalculateStatusCounts() throws java.lang.Exception {
        java.util.Collection<org.apache.ambari.server.actionmanager.HostRoleStatus> hostRoleStatuses = new java.util.LinkedList<>();
        hostRoleStatuses.add(org.apache.ambari.server.actionmanager.HostRoleStatus.PENDING);
        hostRoleStatuses.add(org.apache.ambari.server.actionmanager.HostRoleStatus.QUEUED);
        hostRoleStatuses.add(org.apache.ambari.server.actionmanager.HostRoleStatus.HOLDING);
        hostRoleStatuses.add(org.apache.ambari.server.actionmanager.HostRoleStatus.HOLDING_FAILED);
        hostRoleStatuses.add(org.apache.ambari.server.actionmanager.HostRoleStatus.HOLDING_TIMEDOUT);
        hostRoleStatuses.add(org.apache.ambari.server.actionmanager.HostRoleStatus.IN_PROGRESS);
        hostRoleStatuses.add(org.apache.ambari.server.actionmanager.HostRoleStatus.IN_PROGRESS);
        hostRoleStatuses.add(org.apache.ambari.server.actionmanager.HostRoleStatus.COMPLETED);
        hostRoleStatuses.add(org.apache.ambari.server.actionmanager.HostRoleStatus.COMPLETED);
        hostRoleStatuses.add(org.apache.ambari.server.actionmanager.HostRoleStatus.COMPLETED);
        hostRoleStatuses.add(org.apache.ambari.server.actionmanager.HostRoleStatus.COMPLETED);
        hostRoleStatuses.add(org.apache.ambari.server.actionmanager.HostRoleStatus.FAILED);
        hostRoleStatuses.add(org.apache.ambari.server.actionmanager.HostRoleStatus.TIMEDOUT);
        hostRoleStatuses.add(org.apache.ambari.server.actionmanager.HostRoleStatus.ABORTED);
        hostRoleStatuses.add(org.apache.ambari.server.actionmanager.HostRoleStatus.SKIPPED_FAILED);
        java.util.Map<org.apache.ambari.server.actionmanager.HostRoleStatus, java.lang.Integer> counts = org.apache.ambari.server.controller.internal.CalculatedStatus.calculateStatusCounts(hostRoleStatuses);
        org.junit.Assert.assertEquals(1L, ((long) (counts.get(org.apache.ambari.server.actionmanager.HostRoleStatus.PENDING))));
        org.junit.Assert.assertEquals(1L, ((long) (counts.get(org.apache.ambari.server.actionmanager.HostRoleStatus.QUEUED))));
        org.junit.Assert.assertEquals(1L, ((long) (counts.get(org.apache.ambari.server.actionmanager.HostRoleStatus.HOLDING))));
        org.junit.Assert.assertEquals(1L, ((long) (counts.get(org.apache.ambari.server.actionmanager.HostRoleStatus.HOLDING_FAILED))));
        org.junit.Assert.assertEquals(1L, ((long) (counts.get(org.apache.ambari.server.actionmanager.HostRoleStatus.HOLDING_TIMEDOUT))));
        org.junit.Assert.assertEquals(5L, ((long) (counts.get(org.apache.ambari.server.actionmanager.HostRoleStatus.IN_PROGRESS))));
        org.junit.Assert.assertEquals(8L, ((long) (counts.get(org.apache.ambari.server.actionmanager.HostRoleStatus.COMPLETED))));
        org.junit.Assert.assertEquals(1L, ((long) (counts.get(org.apache.ambari.server.actionmanager.HostRoleStatus.FAILED))));
        org.junit.Assert.assertEquals(1L, ((long) (counts.get(org.apache.ambari.server.actionmanager.HostRoleStatus.TIMEDOUT))));
        org.junit.Assert.assertEquals(1L, ((long) (counts.get(org.apache.ambari.server.actionmanager.HostRoleStatus.ABORTED))));
        org.junit.Assert.assertEquals(1L, ((long) (counts.get(org.apache.ambari.server.actionmanager.HostRoleStatus.SKIPPED_FAILED))));
    }

    @org.junit.Test
    public void testCountsWithRepeatHosts() throws java.lang.Exception {
        java.util.List<org.apache.ambari.server.actionmanager.Stage> stages = new java.util.ArrayList<>();
        stages.addAll(getStages(getTaskEntities(org.apache.ambari.server.actionmanager.HostRoleStatus.COMPLETED, org.apache.ambari.server.actionmanager.HostRoleStatus.COMPLETED, org.apache.ambari.server.actionmanager.HostRoleStatus.COMPLETED, org.apache.ambari.server.actionmanager.HostRoleStatus.COMPLETED)));
        org.apache.ambari.server.orm.entities.HostRoleCommandEntity entity = new org.apache.ambari.server.orm.entities.HostRoleCommandEntity();
        entity.setTaskId(org.apache.ambari.server.controller.internal.CalculatedStatusTest.taskId++);
        entity.setStatus(org.apache.ambari.server.actionmanager.HostRoleStatus.PENDING);
        stages.addAll(getStages(java.util.Collections.singleton(entity)));
        org.apache.ambari.server.controller.internal.CalculatedStatus calc = org.apache.ambari.server.controller.internal.CalculatedStatus.statusFromStages(stages);
        org.junit.Assert.assertEquals(org.apache.ambari.server.actionmanager.HostRoleStatus.IN_PROGRESS, calc.getStatus());
        org.junit.Assert.assertEquals(80.0, calc.getPercent(), 0.1);
    }

    @org.junit.Test
    public void testSkippedFailed_Stage() throws java.lang.Exception {
        java.util.Collection<org.apache.ambari.server.orm.entities.HostRoleCommandEntity> tasks = getTaskEntities(org.apache.ambari.server.actionmanager.HostRoleStatus.SKIPPED_FAILED);
        org.apache.ambari.server.controller.internal.CalculatedStatus status = org.apache.ambari.server.controller.internal.CalculatedStatus.statusFromTaskEntities(tasks, false);
        org.junit.Assert.assertEquals(org.apache.ambari.server.actionmanager.HostRoleStatus.COMPLETED, status.getStatus());
    }

    @org.junit.Test
    public void testSkippedFailed_UpgradeGroup() throws java.lang.Exception {
        final org.apache.ambari.server.orm.dao.HostRoleCommandStatusSummaryDTO summary1 = EasyMock.createNiceMock(org.apache.ambari.server.orm.dao.HostRoleCommandStatusSummaryDTO.class);
        java.util.ArrayList<org.apache.ambari.server.actionmanager.HostRoleStatus> taskStatuses1 = new java.util.ArrayList<org.apache.ambari.server.actionmanager.HostRoleStatus>() {
            {
                add(org.apache.ambari.server.actionmanager.HostRoleStatus.COMPLETED);
                add(org.apache.ambari.server.actionmanager.HostRoleStatus.COMPLETED);
                add(org.apache.ambari.server.actionmanager.HostRoleStatus.COMPLETED);
            }
        };
        final org.apache.ambari.server.orm.dao.HostRoleCommandStatusSummaryDTO summary2 = EasyMock.createNiceMock(org.apache.ambari.server.orm.dao.HostRoleCommandStatusSummaryDTO.class);
        java.util.ArrayList<org.apache.ambari.server.actionmanager.HostRoleStatus> taskStatuses2 = new java.util.ArrayList<org.apache.ambari.server.actionmanager.HostRoleStatus>() {
            {
                add(org.apache.ambari.server.actionmanager.HostRoleStatus.COMPLETED);
                add(org.apache.ambari.server.actionmanager.HostRoleStatus.SKIPPED_FAILED);
                add(org.apache.ambari.server.actionmanager.HostRoleStatus.COMPLETED);
            }
        };
        java.util.Map<java.lang.Long, org.apache.ambari.server.orm.dao.HostRoleCommandStatusSummaryDTO> stageDto = new java.util.HashMap<java.lang.Long, org.apache.ambari.server.orm.dao.HostRoleCommandStatusSummaryDTO>() {
            {
                put(1L, summary1);
                put(2L, summary2);
            }
        };
        java.util.Set<java.lang.Long> stageIds = new java.util.HashSet<java.lang.Long>() {
            {
                add(1L);
                add(2L);
            }
        };
        EasyMock.expect(summary1.getTaskTotal()).andReturn(taskStatuses1.size()).anyTimes();
        EasyMock.expect(summary2.getTaskTotal()).andReturn(taskStatuses2.size()).anyTimes();
        EasyMock.expect(summary1.isStageSkippable()).andReturn(true).anyTimes();
        EasyMock.expect(summary2.isStageSkippable()).andReturn(true).anyTimes();
        EasyMock.expect(summary1.getTaskStatuses()).andReturn(taskStatuses1).anyTimes();
        EasyMock.expect(summary2.getTaskStatuses()).andReturn(taskStatuses2).anyTimes();
        EasyMock.replay(summary1, summary2);
        org.apache.ambari.server.controller.internal.CalculatedStatus calc = org.apache.ambari.server.controller.internal.CalculatedStatus.statusFromStageSummary(stageDto, stageIds);
        org.junit.Assert.assertEquals(org.apache.ambari.server.actionmanager.HostRoleStatus.SKIPPED_FAILED, calc.getDisplayStatus());
        org.junit.Assert.assertEquals(org.apache.ambari.server.actionmanager.HostRoleStatus.COMPLETED, calc.getStatus());
    }

    @org.junit.Test
    public void testSummaryStatus_UpgradeGroup() throws java.lang.Exception {
        final org.apache.ambari.server.orm.dao.HostRoleCommandStatusSummaryDTO summary1 = EasyMock.createNiceMock(org.apache.ambari.server.orm.dao.HostRoleCommandStatusSummaryDTO.class);
        java.util.ArrayList<org.apache.ambari.server.actionmanager.HostRoleStatus> taskStatuses1 = new java.util.ArrayList<org.apache.ambari.server.actionmanager.HostRoleStatus>() {
            {
                add(org.apache.ambari.server.actionmanager.HostRoleStatus.COMPLETED);
                add(org.apache.ambari.server.actionmanager.HostRoleStatus.COMPLETED);
                add(org.apache.ambari.server.actionmanager.HostRoleStatus.COMPLETED);
            }
        };
        final org.apache.ambari.server.orm.dao.HostRoleCommandStatusSummaryDTO summary2 = EasyMock.createNiceMock(org.apache.ambari.server.orm.dao.HostRoleCommandStatusSummaryDTO.class);
        java.util.ArrayList<org.apache.ambari.server.actionmanager.HostRoleStatus> taskStatuses2 = new java.util.ArrayList<org.apache.ambari.server.actionmanager.HostRoleStatus>() {
            {
                add(org.apache.ambari.server.actionmanager.HostRoleStatus.IN_PROGRESS);
                add(org.apache.ambari.server.actionmanager.HostRoleStatus.COMPLETED);
                add(org.apache.ambari.server.actionmanager.HostRoleStatus.COMPLETED);
            }
        };
        java.util.Map<java.lang.Long, org.apache.ambari.server.orm.dao.HostRoleCommandStatusSummaryDTO> stageDto = new java.util.HashMap<java.lang.Long, org.apache.ambari.server.orm.dao.HostRoleCommandStatusSummaryDTO>() {
            {
                put(1L, summary1);
                put(2L, summary2);
            }
        };
        java.util.Set<java.lang.Long> stageIds = new java.util.HashSet<java.lang.Long>() {
            {
                add(1L);
                add(2L);
            }
        };
        EasyMock.expect(summary1.getTaskTotal()).andReturn(taskStatuses1.size()).anyTimes();
        EasyMock.expect(summary2.getTaskTotal()).andReturn(taskStatuses2.size()).anyTimes();
        EasyMock.expect(summary1.isStageSkippable()).andReturn(true).anyTimes();
        EasyMock.expect(summary2.isStageSkippable()).andReturn(true).anyTimes();
        EasyMock.expect(summary1.getTaskStatuses()).andReturn(taskStatuses1).anyTimes();
        EasyMock.expect(summary2.getTaskStatuses()).andReturn(taskStatuses2).anyTimes();
        EasyMock.replay(summary1, summary2);
        org.apache.ambari.server.controller.internal.CalculatedStatus calc = org.apache.ambari.server.controller.internal.CalculatedStatus.statusFromStageSummary(stageDto, stageIds);
        org.junit.Assert.assertEquals(org.apache.ambari.server.actionmanager.HostRoleStatus.IN_PROGRESS, calc.getDisplayStatus());
        org.junit.Assert.assertEquals(org.apache.ambari.server.actionmanager.HostRoleStatus.IN_PROGRESS, calc.getStatus());
    }

    @org.junit.Test
    public void testGetCompletedStatusForNoTasks() throws java.lang.Exception {
        org.apache.ambari.server.controller.internal.CalculatedStatus status = org.apache.ambari.server.controller.internal.CalculatedStatus.statusFromTaskEntities(new java.util.ArrayList<>(), false);
        org.junit.Assert.assertEquals(org.apache.ambari.server.actionmanager.HostRoleStatus.COMPLETED, status.getStatus());
        status = org.apache.ambari.server.controller.internal.CalculatedStatus.statusFromStageSummary(new java.util.HashMap<>(), new java.util.HashSet<>());
        org.junit.Assert.assertEquals(org.apache.ambari.server.actionmanager.HostRoleStatus.COMPLETED, status.getStatus());
        java.util.Map<org.apache.ambari.server.actionmanager.HostRoleStatus, java.lang.Integer> counts = org.apache.ambari.server.controller.internal.CalculatedStatus.calculateStatusCounts(new java.util.ArrayList<>());
        java.util.Map<org.apache.ambari.server.actionmanager.HostRoleStatus, java.lang.Integer> displayCounts = org.apache.ambari.server.controller.internal.CalculatedStatus.calculateStatusCounts(new java.util.ArrayList<>());
        org.apache.ambari.server.actionmanager.HostRoleStatus hostRoleStatus = org.apache.ambari.server.controller.internal.CalculatedStatus.calculateSummaryStatusOfUpgrade(counts, 0);
        org.apache.ambari.server.actionmanager.HostRoleStatus hostRoleDisplayStatus = org.apache.ambari.server.controller.internal.CalculatedStatus.calculateSummaryDisplayStatus(displayCounts, 0, false);
        org.junit.Assert.assertEquals(org.apache.ambari.server.actionmanager.HostRoleStatus.COMPLETED, hostRoleStatus);
        org.junit.Assert.assertEquals(org.apache.ambari.server.actionmanager.HostRoleStatus.COMPLETED, hostRoleDisplayStatus);
    }

    private java.util.Collection<org.apache.ambari.server.orm.entities.HostRoleCommandEntity> getTaskEntities(org.apache.ambari.server.actionmanager.HostRoleStatus... statuses) {
        java.util.Collection<org.apache.ambari.server.orm.entities.HostRoleCommandEntity> entities = new java.util.LinkedList<>();
        for (int i = 0; i < statuses.length; i++) {
            org.apache.ambari.server.actionmanager.HostRoleStatus status = statuses[i];
            org.apache.ambari.server.orm.entities.HostRoleCommandEntity entity = new org.apache.ambari.server.orm.entities.HostRoleCommandEntity();
            entity.setTaskId(org.apache.ambari.server.controller.internal.CalculatedStatusTest.taskId++);
            entity.setStatus(status);
            entities.add(entity);
        }
        return entities;
    }

    private java.util.Collection<org.apache.ambari.server.orm.entities.StageEntity> getStageEntities(java.util.Collection<org.apache.ambari.server.orm.entities.HostRoleCommandEntity>... taskCollections) {
        java.util.Collection<org.apache.ambari.server.orm.entities.StageEntity> entities = new java.util.LinkedList<>();
        for (java.util.Collection<org.apache.ambari.server.orm.entities.HostRoleCommandEntity> taskEntities : taskCollections) {
            org.apache.ambari.server.orm.entities.StageEntity entity = new org.apache.ambari.server.orm.entities.StageEntity();
            entity.setStageId(org.apache.ambari.server.controller.internal.CalculatedStatusTest.stageId++);
            entity.setHostRoleCommands(taskEntities);
            entities.add(entity);
        }
        return entities;
    }

    private java.util.Collection<org.apache.ambari.server.actionmanager.Stage> getStages(java.util.Collection<org.apache.ambari.server.orm.entities.HostRoleCommandEntity>... taskCollections) {
        java.util.Collection<org.apache.ambari.server.actionmanager.Stage> entities = new java.util.LinkedList<>();
        for (java.util.Collection<org.apache.ambari.server.orm.entities.HostRoleCommandEntity> taskEntities : taskCollections) {
            org.apache.ambari.server.controller.internal.CalculatedStatusTest.TestStage stage = new org.apache.ambari.server.controller.internal.CalculatedStatusTest.TestStage();
            stage.setStageId(org.apache.ambari.server.controller.internal.CalculatedStatusTest.stageId++);
            stage.setHostRoleCommands(taskEntities);
            entities.add(stage);
        }
        return entities;
    }

    private class TestStage extends org.apache.ambari.server.actionmanager.Stage {
        private final java.util.List<org.apache.ambari.server.actionmanager.HostRoleCommand> hostRoleCommands = new java.util.LinkedList<>();

        private TestStage() {
            super(1L, "", "", 1L, "", "", "", hostRoleCommandFactory, ecwFactory);
        }

        void setHostRoleCommands(java.util.Collection<org.apache.ambari.server.orm.entities.HostRoleCommandEntity> tasks) {
            for (org.apache.ambari.server.orm.entities.HostRoleCommandEntity task : tasks) {
                org.apache.ambari.server.actionmanager.HostRoleCommand command = org.apache.ambari.server.controller.internal.CalculatedStatusTest.HostRoleCommandHelper.createWithTaskId(task.getHostName(), org.apache.ambari.server.controller.internal.CalculatedStatusTest.taskId++, hostRoleCommandFactory);
                command.setStatus(task.getStatus());
                hostRoleCommands.add(command);
            }
        }

        @java.lang.Override
        public java.util.List<org.apache.ambari.server.actionmanager.HostRoleCommand> getOrderedHostRoleCommands() {
            return hostRoleCommands;
        }
    }

    private static class HostRoleCommandHelper {
        public static org.apache.ambari.server.actionmanager.HostRoleCommand createWithTaskId(java.lang.String hostName, long taskId, org.apache.ambari.server.actionmanager.HostRoleCommandFactory hostRoleCommandFactory1) {
            org.apache.ambari.server.actionmanager.HostRoleCommand hrc = hostRoleCommandFactory1.create(hostName, org.apache.ambari.server.Role.AMBARI_SERVER_ACTION, null, org.apache.ambari.server.RoleCommand.START);
            try {
                org.apache.ambari.server.controller.internal.CalculatedStatusTest.s_field.set(hrc, java.lang.Long.valueOf(taskId));
            } catch (java.lang.Exception e) {
                e.printStackTrace();
            }
            return hrc;
        }
    }
}