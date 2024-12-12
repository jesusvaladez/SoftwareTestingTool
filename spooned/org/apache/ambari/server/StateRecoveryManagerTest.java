package org.apache.ambari.server;
import org.easymock.Capture;
import org.easymock.EasyMock;
import static org.easymock.EasyMock.capture;
import static org.easymock.EasyMock.createNiceMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.replay;
public class StateRecoveryManagerTest {
    private com.google.inject.Injector injector;

    private org.apache.ambari.server.orm.dao.HostVersionDAO hostVersionDAOMock;

    private org.apache.ambari.server.orm.dao.ServiceComponentDesiredStateDAO serviceComponentDesiredStateDAOMock;

    @org.junit.Before
    public void setup() throws java.lang.Exception {
        hostVersionDAOMock = EasyMock.createNiceMock(org.apache.ambari.server.orm.dao.HostVersionDAO.class);
        serviceComponentDesiredStateDAOMock = EasyMock.createNiceMock(org.apache.ambari.server.orm.dao.ServiceComponentDesiredStateDAO.class);
        org.apache.ambari.server.orm.InMemoryDefaultTestModule module = new org.apache.ambari.server.orm.InMemoryDefaultTestModule();
        injector = com.google.inject.Guice.createInjector(com.google.inject.util.Modules.override(module).with(new org.apache.ambari.server.StateRecoveryManagerTest.MockModule()));
        injector.getInstance(org.apache.ambari.server.orm.GuiceJpaInitializer.class);
    }

    @org.junit.After
    public void teardown() throws org.apache.ambari.server.AmbariException, java.sql.SQLException {
        org.apache.ambari.server.H2DatabaseCleaner.clearDatabaseAndStopPersistenceService(injector);
    }

    @org.junit.Test
    public void testCheckHostAndClusterVersions() throws java.lang.Exception {
        org.apache.ambari.server.StateRecoveryManager stateRecoveryManager = injector.getInstance(org.apache.ambari.server.StateRecoveryManager.class);
        final org.easymock.Capture<org.apache.ambari.server.state.RepositoryVersionState> installFailedHostVersionCapture = org.easymock.EasyMock.newCapture();
        final org.easymock.Capture<org.apache.ambari.server.state.RepositoryVersionState> installingHostVersionCapture = org.easymock.EasyMock.newCapture();
        final org.easymock.Capture<org.apache.ambari.server.state.RepositoryVersionState> installedHostVersionCapture = org.easymock.EasyMock.newCapture();
        final org.easymock.Capture<org.apache.ambari.server.state.RepositoryVersionState> outOfSyncHostVersionCapture = org.easymock.EasyMock.newCapture();
        final org.easymock.Capture<org.apache.ambari.server.state.RepositoryVersionState> upgradeFailedHostVersionCapture = org.easymock.EasyMock.newCapture();
        final org.easymock.Capture<org.apache.ambari.server.state.RepositoryVersionState> upgradingHostVersionCapture = org.easymock.EasyMock.newCapture();
        final org.easymock.Capture<org.apache.ambari.server.state.RepositoryVersionState> upgradedHostVersionCapture = org.easymock.EasyMock.newCapture();
        final org.easymock.Capture<org.apache.ambari.server.state.RepositoryVersionState> currentHostVersionCapture = org.easymock.EasyMock.newCapture();
        EasyMock.expect(hostVersionDAOMock.findAll()).andReturn(com.google.common.collect.Lists.newArrayList(getHostVersionMock("install_failed_version", org.apache.ambari.server.state.RepositoryVersionState.INSTALL_FAILED, installFailedHostVersionCapture), getHostVersionMock("installing_version", org.apache.ambari.server.state.RepositoryVersionState.INSTALLING, installingHostVersionCapture), getHostVersionMock("installed_version", org.apache.ambari.server.state.RepositoryVersionState.INSTALLED, installedHostVersionCapture), getHostVersionMock("out_of_sync_version", org.apache.ambari.server.state.RepositoryVersionState.OUT_OF_SYNC, outOfSyncHostVersionCapture)));
        final org.easymock.Capture<org.apache.ambari.server.state.RepositoryVersionState> installFailedClusterVersionCapture = org.easymock.EasyMock.newCapture();
        final org.easymock.Capture<org.apache.ambari.server.state.RepositoryVersionState> installingClusterVersionCapture = org.easymock.EasyMock.newCapture();
        final org.easymock.Capture<org.apache.ambari.server.state.RepositoryVersionState> installedClusterVersionCapture = org.easymock.EasyMock.newCapture();
        final org.easymock.Capture<org.apache.ambari.server.state.RepositoryVersionState> outOfSyncClusterVersionCapture = org.easymock.EasyMock.newCapture();
        final org.easymock.Capture<org.apache.ambari.server.state.RepositoryVersionState> upgradeFailedClusterVersionCapture = org.easymock.EasyMock.newCapture();
        final org.easymock.Capture<org.apache.ambari.server.state.RepositoryVersionState> upgradingClusterVersionCapture = org.easymock.EasyMock.newCapture();
        final org.easymock.Capture<org.apache.ambari.server.state.RepositoryVersionState> upgradedClusterVersionCapture = org.easymock.EasyMock.newCapture();
        EasyMock.expect(serviceComponentDesiredStateDAOMock.findAll()).andReturn(com.google.common.collect.Lists.newArrayList(getDesiredStateEntityMock("install_failed_version", org.apache.ambari.server.state.RepositoryVersionState.INSTALL_FAILED, installFailedClusterVersionCapture), getDesiredStateEntityMock("installing_version", org.apache.ambari.server.state.RepositoryVersionState.INSTALLING, installingClusterVersionCapture), getDesiredStateEntityMock("installed_version", org.apache.ambari.server.state.RepositoryVersionState.INSTALLED, installedClusterVersionCapture), getDesiredStateEntityMock("out_of_sync_version", org.apache.ambari.server.state.RepositoryVersionState.OUT_OF_SYNC, outOfSyncClusterVersionCapture)));
        EasyMock.replay(hostVersionDAOMock, serviceComponentDesiredStateDAOMock);
        stateRecoveryManager.checkHostAndClusterVersions();
        org.junit.Assert.assertFalse(installFailedHostVersionCapture.hasCaptured());
        org.junit.Assert.assertEquals(installingHostVersionCapture.getValue(), org.apache.ambari.server.state.RepositoryVersionState.INSTALL_FAILED);
        org.junit.Assert.assertFalse(installedHostVersionCapture.hasCaptured());
        org.junit.Assert.assertFalse(outOfSyncHostVersionCapture.hasCaptured());
        org.junit.Assert.assertFalse(upgradeFailedHostVersionCapture.hasCaptured());
        org.junit.Assert.assertFalse(upgradingHostVersionCapture.hasCaptured());
        org.junit.Assert.assertFalse(upgradedHostVersionCapture.hasCaptured());
        org.junit.Assert.assertFalse(currentHostVersionCapture.hasCaptured());
        org.junit.Assert.assertFalse(installFailedClusterVersionCapture.hasCaptured());
        org.junit.Assert.assertEquals(installingClusterVersionCapture.getValue(), org.apache.ambari.server.state.RepositoryVersionState.INSTALL_FAILED);
        org.junit.Assert.assertFalse(installedClusterVersionCapture.hasCaptured());
        org.junit.Assert.assertFalse(outOfSyncClusterVersionCapture.hasCaptured());
        org.junit.Assert.assertFalse(upgradeFailedClusterVersionCapture.hasCaptured());
        org.junit.Assert.assertFalse(upgradingClusterVersionCapture.hasCaptured());
        org.junit.Assert.assertFalse(upgradedClusterVersionCapture.hasCaptured());
    }

    private org.apache.ambari.server.orm.entities.HostVersionEntity getHostVersionMock(java.lang.String name, org.apache.ambari.server.state.RepositoryVersionState state, org.easymock.Capture<org.apache.ambari.server.state.RepositoryVersionState> newStateCaptor) {
        org.apache.ambari.server.orm.entities.HostVersionEntity hvMock = EasyMock.createNiceMock(org.apache.ambari.server.orm.entities.HostVersionEntity.class);
        EasyMock.expect(hvMock.getState()).andReturn(state);
        hvMock.setState(EasyMock.capture(newStateCaptor));
        EasyMock.expectLastCall();
        org.apache.ambari.server.orm.entities.RepositoryVersionEntity rvMock = EasyMock.createNiceMock(org.apache.ambari.server.orm.entities.RepositoryVersionEntity.class);
        EasyMock.expect(rvMock.getDisplayName()).andReturn(name);
        EasyMock.expect(hvMock.getRepositoryVersion()).andReturn(rvMock);
        EasyMock.expect(hvMock.getHostName()).andReturn("somehost");
        EasyMock.replay(hvMock, rvMock);
        return hvMock;
    }

    private org.apache.ambari.server.orm.entities.ServiceComponentDesiredStateEntity getDesiredStateEntityMock(java.lang.String name, org.apache.ambari.server.state.RepositoryVersionState state, org.easymock.Capture<org.apache.ambari.server.state.RepositoryVersionState> newStateCapture) {
        org.apache.ambari.server.orm.entities.ServiceComponentDesiredStateEntity mock = EasyMock.createNiceMock(org.apache.ambari.server.orm.entities.ServiceComponentDesiredStateEntity.class);
        EasyMock.expect(mock.getRepositoryState()).andReturn(state);
        mock.setRepositoryState(EasyMock.capture(newStateCapture));
        EasyMock.expectLastCall();
        org.apache.ambari.server.orm.entities.RepositoryVersionEntity repositoryVersionMock = EasyMock.createNiceMock(org.apache.ambari.server.orm.entities.RepositoryVersionEntity.class);
        EasyMock.expect(repositoryVersionMock.getVersion()).andReturn(name);
        EasyMock.expect(mock.getDesiredRepositoryVersion()).andReturn(repositoryVersionMock);
        EasyMock.replay(mock, repositoryVersionMock);
        return mock;
    }

    public class MockModule extends com.google.inject.AbstractModule {
        @java.lang.Override
        protected void configure() {
            bind(org.apache.ambari.server.orm.dao.HostVersionDAO.class).toInstance(hostVersionDAOMock);
            bind(org.apache.ambari.server.orm.dao.ServiceComponentDesiredStateDAO.class).toInstance(serviceComponentDesiredStateDAOMock);
        }
    }
}