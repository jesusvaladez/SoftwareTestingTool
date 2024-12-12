package org.apache.ambari.server.events.listeners.upgrade;
import org.easymock.EasyMockRunner;
import org.easymock.EasyMockSupport;
import org.easymock.Mock;
import org.easymock.MockType;
import static org.easymock.EasyMock.anyLong;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
@org.junit.runner.RunWith(org.easymock.EasyMockRunner.class)
public class StackVersionListenerTest extends org.easymock.EasyMockSupport {
    private static final java.lang.String INVALID_NEW_VERSION = "1.2.3.4-5678";

    private static final java.lang.String VALID_NEW_VERSION = "2.4.0.0-1000";

    private static final java.lang.String SERVICE_COMPONENT_NAME = "Some component name";

    private static final java.lang.String SERVICE_NAME = "Service name";

    private static final java.lang.Long CLUSTER_ID = 1L;

    private static final java.lang.String UNKNOWN_VERSION = "UNKNOWN";

    private static final java.lang.String VALID_PREVIOUS_VERSION = "2.2.0.0";

    private static final org.apache.ambari.server.orm.entities.HostVersionEntity DUMMY_HOST_VERSION_ENTITY = new org.apache.ambari.server.orm.entities.HostVersionEntity();

    private static final org.apache.ambari.server.orm.entities.UpgradeEntity DUMMY_UPGRADE_ENTITY = new org.apache.ambari.server.orm.entities.UpgradeEntity();

    public static final java.lang.String STACK_NAME = "HDP";

    public static final java.lang.String STACK_VERSION = "2.4";

    private org.apache.ambari.server.state.Cluster cluster;

    private org.apache.ambari.server.state.ServiceComponentHost sch;

    private org.apache.ambari.server.state.Service service;

    private org.apache.ambari.server.state.ServiceComponent serviceComponent;

    private org.apache.ambari.server.events.publishers.VersionEventPublisher publisher = new org.apache.ambari.server.events.publishers.VersionEventPublisher();

    private org.apache.ambari.server.state.StackId stackId = new org.apache.ambari.server.state.StackId(org.apache.ambari.server.events.listeners.upgrade.StackVersionListenerTest.STACK_NAME, org.apache.ambari.server.events.listeners.upgrade.StackVersionListenerTest.STACK_VERSION);

    @org.easymock.Mock
    private com.google.inject.Provider<org.apache.ambari.server.api.services.AmbariMetaInfo> ambariMetaInfoProvider;

    @org.easymock.Mock
    private com.google.inject.Provider<org.apache.ambari.server.agent.stomp.HostLevelParamsHolder> m_hostLevelParamsHolder;

    @org.easymock.Mock
    private org.apache.ambari.server.state.ComponentInfo componentInfo;

    @org.easymock.Mock
    private org.apache.ambari.server.api.services.AmbariMetaInfo ambariMetaInfo;

    @org.easymock.Mock(type = org.easymock.MockType.NICE)
    private org.apache.ambari.server.agent.stomp.HostLevelParamsHolder hostLevelParamsHolder;

    private org.apache.ambari.server.events.listeners.upgrade.StackVersionListener listener;

    @org.junit.Before
    public void setup() throws java.lang.Exception {
        cluster = createNiceMock(org.apache.ambari.server.state.Cluster.class);
        sch = createNiceMock(org.apache.ambari.server.state.ServiceComponentHost.class);
        service = createNiceMock(org.apache.ambari.server.state.Service.class);
        serviceComponent = createNiceMock(org.apache.ambari.server.state.ServiceComponent.class);
        componentInfo = createNiceMock(org.apache.ambari.server.state.ComponentInfo.class);
        EasyMock.expect(cluster.getClusterId()).andReturn(org.apache.ambari.server.events.listeners.upgrade.StackVersionListenerTest.CLUSTER_ID);
        EasyMock.expect(cluster.getService(org.apache.ambari.server.events.listeners.upgrade.StackVersionListenerTest.SERVICE_NAME)).andReturn(service).anyTimes();
        EasyMock.expect(service.getServiceComponent(org.apache.ambari.server.events.listeners.upgrade.StackVersionListenerTest.SERVICE_COMPONENT_NAME)).andReturn(serviceComponent).anyTimes();
        EasyMock.expect(sch.getDesiredStackId()).andReturn(stackId).atLeastOnce();
        EasyMock.expect(sch.getServiceName()).andReturn(org.apache.ambari.server.events.listeners.upgrade.StackVersionListenerTest.SERVICE_NAME).atLeastOnce();
        EasyMock.expect(sch.getServiceComponentName()).andReturn(org.apache.ambari.server.events.listeners.upgrade.StackVersionListenerTest.SERVICE_COMPONENT_NAME).atLeastOnce();
        EasyMock.expect(ambariMetaInfoProvider.get()).andReturn(ambariMetaInfo).atLeastOnce();
        EasyMock.expect(m_hostLevelParamsHolder.get()).andReturn(hostLevelParamsHolder).anyTimes();
        EasyMock.expect(ambariMetaInfo.getComponent(org.apache.ambari.server.events.listeners.upgrade.StackVersionListenerTest.STACK_NAME, org.apache.ambari.server.events.listeners.upgrade.StackVersionListenerTest.STACK_VERSION, org.apache.ambari.server.events.listeners.upgrade.StackVersionListenerTest.SERVICE_NAME, org.apache.ambari.server.events.listeners.upgrade.StackVersionListenerTest.SERVICE_COMPONENT_NAME)).andReturn(componentInfo).atLeastOnce();
        EasyMock.expect(hostLevelParamsHolder.getCurrentData(EasyMock.anyLong())).andReturn(createNiceMock(org.apache.ambari.server.events.HostLevelParamsUpdateEvent.class)).anyTimes();
        listener = new org.apache.ambari.server.events.listeners.upgrade.StackVersionListener(publisher, ambariMetaInfoProvider, m_hostLevelParamsHolder);
        injectMocks(listener);
    }

    @org.junit.Test
    public void testRecalculateHostVersionStateWhenVersionIsNullAndNewVersionIsNotBlank() throws org.apache.ambari.server.AmbariException {
        EasyMock.expect(sch.getVersion()).andReturn(null);
        EasyMock.expect(componentInfo.isVersionAdvertised()).andReturn(true).once();
        sch.setVersion(org.apache.ambari.server.events.listeners.upgrade.StackVersionListenerTest.INVALID_NEW_VERSION);
        EasyMock.expectLastCall().once();
        EasyMock.expect(sch.recalculateHostVersionState()).andReturn(null).once();
        replayAll();
        sendEventAndVerify(org.apache.ambari.server.events.listeners.upgrade.StackVersionListenerTest.INVALID_NEW_VERSION);
    }

    @org.junit.Test
    public void testRecalculateHostVersionStateWhenVersionIsUnknownAndNewVersionIsNotBlank() throws org.apache.ambari.server.AmbariException {
        EasyMock.expect(sch.getVersion()).andReturn(org.apache.ambari.server.events.listeners.upgrade.StackVersionListenerTest.UNKNOWN_VERSION);
        EasyMock.expect(componentInfo.isVersionAdvertised()).andReturn(true).once();
        sch.setVersion(org.apache.ambari.server.events.listeners.upgrade.StackVersionListenerTest.INVALID_NEW_VERSION);
        EasyMock.expectLastCall().once();
        EasyMock.expect(sch.recalculateHostVersionState()).andReturn(null).once();
        replayAll();
        sendEventAndVerify(org.apache.ambari.server.events.listeners.upgrade.StackVersionListenerTest.INVALID_NEW_VERSION);
    }

    @org.junit.Test
    public void testRecalculateClusterVersionStateWhenVersionIsNullAndNewVersionIsValid() throws org.apache.ambari.server.AmbariException {
        EasyMock.expect(sch.getVersion()).andReturn(null);
        EasyMock.expect(componentInfo.isVersionAdvertised()).andReturn(true).once();
        sch.setVersion(org.apache.ambari.server.events.listeners.upgrade.StackVersionListenerTest.VALID_NEW_VERSION);
        EasyMock.expectLastCall().once();
        EasyMock.expect(sch.recalculateHostVersionState()).andReturn(org.apache.ambari.server.events.listeners.upgrade.StackVersionListenerTest.DUMMY_HOST_VERSION_ENTITY).once();
        replayAll();
        sendEventAndVerify(org.apache.ambari.server.events.listeners.upgrade.StackVersionListenerTest.VALID_NEW_VERSION);
    }

    @org.junit.Test
    public void testRecalculateClusterVersionStateWhenVersionIsUnknownAndNewVersionIsValid() throws org.apache.ambari.server.AmbariException {
        EasyMock.expect(sch.getVersion()).andReturn(org.apache.ambari.server.events.listeners.upgrade.StackVersionListenerTest.UNKNOWN_VERSION);
        EasyMock.expect(componentInfo.isVersionAdvertised()).andReturn(true).once();
        sch.setVersion(org.apache.ambari.server.events.listeners.upgrade.StackVersionListenerTest.VALID_NEW_VERSION);
        EasyMock.expectLastCall().once();
        EasyMock.expect(sch.recalculateHostVersionState()).andReturn(org.apache.ambari.server.events.listeners.upgrade.StackVersionListenerTest.DUMMY_HOST_VERSION_ENTITY).once();
        replayAll();
        sendEventAndVerify(org.apache.ambari.server.events.listeners.upgrade.StackVersionListenerTest.VALID_NEW_VERSION);
    }

    @org.junit.Test
    public void testRecalculateHostVersionStateWhenComponentDesiredVersionIsUnknownAndNewVersionIsNotValid() throws org.apache.ambari.server.AmbariException {
        EasyMock.expect(serviceComponent.getDesiredVersion()).andReturn(org.apache.ambari.server.events.listeners.upgrade.StackVersionListenerTest.UNKNOWN_VERSION);
        EasyMock.expect(componentInfo.isVersionAdvertised()).andReturn(true).once();
        sch.setUpgradeState(org.apache.ambari.server.state.UpgradeState.NONE);
        EasyMock.expectLastCall().once();
        sch.setVersion(org.apache.ambari.server.events.listeners.upgrade.StackVersionListenerTest.INVALID_NEW_VERSION);
        EasyMock.expectLastCall().once();
        EasyMock.expect(sch.recalculateHostVersionState()).andReturn(null).once();
        replayAll();
        sendEventAndVerify(org.apache.ambari.server.events.listeners.upgrade.StackVersionListenerTest.INVALID_NEW_VERSION);
    }

    @org.junit.Test
    public void testRecalculateClusterVersionStateWhenComponentDesiredVersionIsUnknownAndNewVersionIsValid() throws org.apache.ambari.server.AmbariException {
        EasyMock.expect(serviceComponent.getDesiredVersion()).andReturn(org.apache.ambari.server.events.listeners.upgrade.StackVersionListenerTest.UNKNOWN_VERSION);
        EasyMock.expect(componentInfo.isVersionAdvertised()).andReturn(true).once();
        sch.setUpgradeState(org.apache.ambari.server.state.UpgradeState.NONE);
        EasyMock.expectLastCall().once();
        sch.setVersion(org.apache.ambari.server.events.listeners.upgrade.StackVersionListenerTest.VALID_NEW_VERSION);
        EasyMock.expectLastCall().once();
        EasyMock.expect(sch.recalculateHostVersionState()).andReturn(org.apache.ambari.server.events.listeners.upgrade.StackVersionListenerTest.DUMMY_HOST_VERSION_ENTITY).once();
        replayAll();
        sendEventAndVerify(org.apache.ambari.server.events.listeners.upgrade.StackVersionListenerTest.VALID_NEW_VERSION);
    }

    @org.junit.Test
    public void testRecalculateClusterVersionStateWhenVersionNotAdvertised() throws org.apache.ambari.server.AmbariException {
        EasyMock.expect(componentInfo.isVersionAdvertised()).andReturn(false).once();
        replayAll();
        sendEventAndVerify(org.apache.ambari.server.events.listeners.upgrade.StackVersionListenerTest.VALID_NEW_VERSION);
    }

    @org.junit.Test
    public void testNoActionTakenOnNullVersion() throws org.apache.ambari.server.AmbariException {
        EasyMock.expect(componentInfo.isVersionAdvertised()).andReturn(true).once();
        resetAll();
        replayAll();
        sendEventAndVerify(null);
    }

    @org.junit.Test
    public void testSetUpgradeStateToCompleteWhenUpgradeIsInProgressAndNewVersionIsEqualToComponentDesiredVersion() throws org.apache.ambari.server.AmbariException {
        EasyMock.expect(cluster.getUpgradeInProgress()).andReturn(org.apache.ambari.server.events.listeners.upgrade.StackVersionListenerTest.DUMMY_UPGRADE_ENTITY);
        EasyMock.expect(sch.getVersion()).andReturn(org.apache.ambari.server.events.listeners.upgrade.StackVersionListenerTest.VALID_PREVIOUS_VERSION);
        EasyMock.expect(sch.getUpgradeState()).andReturn(org.apache.ambari.server.state.UpgradeState.IN_PROGRESS);
        EasyMock.expect(componentInfo.isVersionAdvertised()).andReturn(true).once();
        sch.setUpgradeState(org.apache.ambari.server.state.UpgradeState.COMPLETE);
        EasyMock.expectLastCall().once();
        EasyMock.expect(serviceComponent.getDesiredVersion()).andStubReturn(org.apache.ambari.server.events.listeners.upgrade.StackVersionListenerTest.VALID_NEW_VERSION);
        replayAll();
        sendEventAndVerify(org.apache.ambari.server.events.listeners.upgrade.StackVersionListenerTest.VALID_NEW_VERSION);
    }

    @org.junit.Test
    public void testSetUpgradeStateToNoneWhenNoUpgradeAndNewVersionIsEqualToComponentDesiredVersion() throws org.apache.ambari.server.AmbariException {
        EasyMock.expect(sch.getVersion()).andReturn(org.apache.ambari.server.events.listeners.upgrade.StackVersionListenerTest.VALID_PREVIOUS_VERSION);
        EasyMock.expect(sch.getUpgradeState()).andReturn(org.apache.ambari.server.state.UpgradeState.IN_PROGRESS);
        EasyMock.expect(componentInfo.isVersionAdvertised()).andReturn(true).once();
        sch.setUpgradeState(org.apache.ambari.server.state.UpgradeState.NONE);
        EasyMock.expectLastCall().once();
        EasyMock.expect(serviceComponent.getDesiredVersion()).andStubReturn(org.apache.ambari.server.events.listeners.upgrade.StackVersionListenerTest.VALID_NEW_VERSION);
        replayAll();
        sendEventAndVerify(org.apache.ambari.server.events.listeners.upgrade.StackVersionListenerTest.VALID_NEW_VERSION);
    }

    @org.junit.Test
    public void testSetUpgradeStateToVersionMismatchWhenUpgradeIsInProgressAndNewVersionIsNotEqualToComponentDesiredVersion() throws org.apache.ambari.server.AmbariException {
        EasyMock.expect(sch.getVersion()).andReturn(org.apache.ambari.server.events.listeners.upgrade.StackVersionListenerTest.VALID_PREVIOUS_VERSION);
        EasyMock.expect(sch.getUpgradeState()).andReturn(org.apache.ambari.server.state.UpgradeState.IN_PROGRESS);
        EasyMock.expect(componentInfo.isVersionAdvertised()).andReturn(true).once();
        sch.setUpgradeState(org.apache.ambari.server.state.UpgradeState.VERSION_MISMATCH);
        EasyMock.expectLastCall().once();
        sch.setVersion(org.apache.ambari.server.events.listeners.upgrade.StackVersionListenerTest.VALID_NEW_VERSION);
        EasyMock.expectLastCall().once();
        EasyMock.expect(serviceComponent.getDesiredVersion()).andStubReturn(org.apache.ambari.server.events.listeners.upgrade.StackVersionListenerTest.VALID_PREVIOUS_VERSION);
        replayAll();
        sendEventAndVerify(org.apache.ambari.server.events.listeners.upgrade.StackVersionListenerTest.VALID_NEW_VERSION);
    }

    @org.junit.Test
    public void testSetUpgradeStateToCompleteWhenHostHasVersionMismatchAndNewVersionIsEqualToComponentDesiredVersionAndClusterUpgradeIsInProgress() throws org.apache.ambari.server.AmbariException {
        EasyMock.expect(sch.getVersion()).andReturn(org.apache.ambari.server.events.listeners.upgrade.StackVersionListenerTest.VALID_PREVIOUS_VERSION);
        EasyMock.expect(sch.getUpgradeState()).andReturn(org.apache.ambari.server.state.UpgradeState.VERSION_MISMATCH);
        EasyMock.expect(cluster.getUpgradeInProgress()).andReturn(org.apache.ambari.server.events.listeners.upgrade.StackVersionListenerTest.DUMMY_UPGRADE_ENTITY);
        EasyMock.expect(serviceComponent.getDesiredVersion()).andStubReturn(org.apache.ambari.server.events.listeners.upgrade.StackVersionListenerTest.VALID_NEW_VERSION);
        EasyMock.expect(componentInfo.isVersionAdvertised()).andReturn(true).once();
        sch.setUpgradeState(org.apache.ambari.server.state.UpgradeState.COMPLETE);
        EasyMock.expectLastCall().once();
        replayAll();
        sendEventAndVerify(org.apache.ambari.server.events.listeners.upgrade.StackVersionListenerTest.VALID_NEW_VERSION);
    }

    @org.junit.Test
    public void testSetUpgradeStateToNoneWhenHostHasVersionMismatchAndNewVersionIsEqualToComponentDesiredVersion() throws org.apache.ambari.server.AmbariException {
        EasyMock.expect(sch.getVersion()).andReturn(org.apache.ambari.server.events.listeners.upgrade.StackVersionListenerTest.VALID_PREVIOUS_VERSION);
        EasyMock.expect(sch.getUpgradeState()).andReturn(org.apache.ambari.server.state.UpgradeState.VERSION_MISMATCH);
        EasyMock.expect(serviceComponent.getDesiredVersion()).andStubReturn(org.apache.ambari.server.events.listeners.upgrade.StackVersionListenerTest.VALID_NEW_VERSION);
        EasyMock.expect(componentInfo.isVersionAdvertised()).andReturn(true).once();
        sch.setUpgradeState(org.apache.ambari.server.state.UpgradeState.NONE);
        EasyMock.expectLastCall().once();
        replayAll();
        sendEventAndVerify(org.apache.ambari.server.events.listeners.upgrade.StackVersionListenerTest.VALID_NEW_VERSION);
    }

    @org.junit.Test
    public void testSetUpgradeStateToVersionMismatchByDefaultWhenHostAndNewVersionsAreValid() throws org.apache.ambari.server.AmbariException {
        EasyMock.expect(sch.getVersion()).andReturn(org.apache.ambari.server.events.listeners.upgrade.StackVersionListenerTest.VALID_PREVIOUS_VERSION);
        EasyMock.expect(componentInfo.isVersionAdvertised()).andReturn(true).once();
        sch.setUpgradeState(org.apache.ambari.server.state.UpgradeState.VERSION_MISMATCH);
        EasyMock.expectLastCall().once();
        replayAll();
        sendEventAndVerify(org.apache.ambari.server.events.listeners.upgrade.StackVersionListenerTest.VALID_NEW_VERSION);
    }

    @org.junit.Test
    public void testSetRepositoryVersion() throws java.lang.Exception {
        org.apache.ambari.server.state.Host host = createMock(org.apache.ambari.server.state.Host.class);
        EasyMock.expect(host.getHostId()).andReturn(1L).anyTimes();
        EasyMock.expect(sch.getVersion()).andReturn(org.apache.ambari.server.events.listeners.upgrade.StackVersionListenerTest.UNKNOWN_VERSION);
        EasyMock.expect(sch.getHost()).andReturn(host).anyTimes();
        EasyMock.expect(componentInfo.isVersionAdvertised()).andReturn(true).once();
        org.apache.ambari.server.orm.dao.RepositoryVersionDAO dao = createNiceMock(org.apache.ambari.server.orm.dao.RepositoryVersionDAO.class);
        org.apache.ambari.server.orm.entities.RepositoryVersionEntity entity = createNiceMock(org.apache.ambari.server.orm.entities.RepositoryVersionEntity.class);
        EasyMock.expect(entity.getVersion()).andReturn("2.4.0.0").once();
        entity.setResolved(true);
        EasyMock.expectLastCall().once();
        EasyMock.expect(dao.findByPK(1L)).andReturn(entity).once();
        EasyMock.expect(dao.merge(entity)).andReturn(entity).once();
        replayAll();
        java.lang.String newVersion = org.apache.ambari.server.events.listeners.upgrade.StackVersionListenerTest.VALID_NEW_VERSION;
        org.apache.ambari.server.events.HostComponentVersionAdvertisedEvent event = new org.apache.ambari.server.events.HostComponentVersionAdvertisedEvent(cluster, sch, newVersion, 1L);
        java.lang.reflect.Field field = org.apache.ambari.server.events.listeners.upgrade.StackVersionListener.class.getDeclaredField("repositoryVersionDAO");
        field.setAccessible(true);
        field.set(listener, dao);
        listener.onAmbariEvent(event);
        verifyAll();
    }

    @org.junit.Test
    public void testRepositoryResolvedWhenVersionsMatch() throws java.lang.Exception {
        java.lang.String version = "2.4.0.0";
        org.apache.ambari.server.state.Host host = createMock(org.apache.ambari.server.state.Host.class);
        EasyMock.expect(host.getHostId()).andReturn(1L).anyTimes();
        EasyMock.expect(sch.getVersion()).andReturn(version);
        EasyMock.expect(sch.getHost()).andReturn(host).anyTimes();
        EasyMock.expect(componentInfo.isVersionAdvertised()).andReturn(true).once();
        org.apache.ambari.server.orm.dao.RepositoryVersionDAO dao = createNiceMock(org.apache.ambari.server.orm.dao.RepositoryVersionDAO.class);
        org.apache.ambari.server.orm.entities.RepositoryVersionEntity entity = createNiceMock(org.apache.ambari.server.orm.entities.RepositoryVersionEntity.class);
        EasyMock.expect(entity.getVersion()).andReturn(version).once();
        EasyMock.expect(entity.isResolved()).andReturn(false).once();
        entity.setResolved(true);
        EasyMock.expectLastCall().once();
        EasyMock.expect(dao.findByPK(1L)).andReturn(entity).once();
        EasyMock.expect(dao.merge(entity)).andReturn(entity).once();
        replayAll();
        java.lang.String newVersion = version;
        org.apache.ambari.server.events.HostComponentVersionAdvertisedEvent event = new org.apache.ambari.server.events.HostComponentVersionAdvertisedEvent(cluster, sch, newVersion, 1L);
        java.lang.reflect.Field field = org.apache.ambari.server.events.listeners.upgrade.StackVersionListener.class.getDeclaredField("repositoryVersionDAO");
        field.setAccessible(true);
        field.set(listener, dao);
        listener.onAmbariEvent(event);
        verifyAll();
    }

    @org.junit.Test
    public void testRepositoryVersionNotSetDuringUpgrade() throws java.lang.Exception {
        EasyMock.expect(componentInfo.isVersionAdvertised()).andReturn(true).once();
        EasyMock.expect(cluster.getUpgradeInProgress()).andReturn(createNiceMock(org.apache.ambari.server.orm.entities.UpgradeEntity.class));
        org.apache.ambari.server.orm.dao.RepositoryVersionDAO dao = createStrictMock(org.apache.ambari.server.orm.dao.RepositoryVersionDAO.class);
        replayAll();
        java.lang.reflect.Field field = org.apache.ambari.server.events.listeners.upgrade.StackVersionListener.class.getDeclaredField("repositoryVersionDAO");
        field.setAccessible(true);
        field.set(listener, dao);
        org.apache.ambari.server.events.HostComponentVersionAdvertisedEvent event = new org.apache.ambari.server.events.HostComponentVersionAdvertisedEvent(cluster, sch, org.apache.ambari.server.events.listeners.upgrade.StackVersionListenerTest.VALID_NEW_VERSION, 1L);
        junit.framework.Assert.assertNotNull(event.getRepositoryVersionId());
        listener.onAmbariEvent(event);
        verifyAll();
    }

    private void sendEventAndVerify(java.lang.String newVersion) throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.server.events.HostComponentVersionAdvertisedEvent event = new org.apache.ambari.server.events.HostComponentVersionAdvertisedEvent(cluster, sch, newVersion);
        listener.onAmbariEvent(event);
        verifyAll();
    }
}