package org.apache.ambari.server.events.listeners.upgrade;
import org.easymock.EasyMockRunner;
import org.easymock.EasyMockSupport;
import org.easymock.Mock;
import org.easymock.MockType;
import org.easymock.TestSubject;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
@org.junit.runner.RunWith(org.easymock.EasyMockRunner.class)
public class StackUpgradeFinishListenerTest extends org.easymock.EasyMockSupport {
    public static final java.lang.String STACK_NAME = "HDP-2.4.0.0";

    public static final java.lang.String STACK_VERSION = "2.4.0.0";

    private org.apache.ambari.server.events.publishers.VersionEventPublisher publisher = new org.apache.ambari.server.events.publishers.VersionEventPublisher();

    private org.apache.ambari.server.state.Cluster cluster;

    @org.easymock.TestSubject
    private org.apache.ambari.server.events.listeners.upgrade.StackUpgradeFinishListener listener = new org.apache.ambari.server.events.listeners.upgrade.StackUpgradeFinishListener(publisher);

    @org.easymock.Mock(type = org.easymock.MockType.NICE, fieldName = "roleCommandOrderProvider")
    private com.google.inject.Provider<org.apache.ambari.server.metadata.RoleCommandOrderProvider> roleCommandOrderProviderProviderMock;

    @org.easymock.Mock(type = org.easymock.MockType.NICE, fieldName = "ambariMetaInfo")
    private com.google.inject.Provider<org.apache.ambari.server.api.services.AmbariMetaInfo> ambariMetaInfoProvider = null;

    @org.junit.Before
    public void setup() throws java.lang.Exception {
        org.apache.ambari.server.api.services.AmbariMetaInfo ami = createNiceMock(org.apache.ambari.server.api.services.AmbariMetaInfo.class);
        cluster = createNiceMock(org.apache.ambari.server.state.Cluster.class);
        org.apache.ambari.server.state.ServiceComponent serviceComponent = createNiceMock(org.apache.ambari.server.state.ServiceComponent.class);
        org.apache.ambari.server.state.Service service = createNiceMock(org.apache.ambari.server.state.Service.class);
        java.util.Map<java.lang.String, org.apache.ambari.server.state.Service> services = new java.util.HashMap<>();
        java.util.Map<java.lang.String, org.apache.ambari.server.state.ServiceComponent> components = new java.util.HashMap<>();
        services.put("mock_service", service);
        components.put("mock_component", serviceComponent);
        EasyMock.expect(cluster.getServices()).andReturn(services);
        EasyMock.expect(service.getServiceComponents()).andReturn(components);
        EasyMock.expect(ambariMetaInfoProvider.get()).andReturn(ami);
        ami.reconcileAlertDefinitions(cluster, true);
        EasyMock.expectLastCall();
        serviceComponent.updateComponentInfo();
        service.updateServiceInfo();
    }

    @org.junit.Test
    public void testupdateComponentInfo() throws org.apache.ambari.server.AmbariException {
        replayAll();
        sendEventAndVerify();
    }

    private void sendEventAndVerify() {
        org.apache.ambari.server.events.StackUpgradeFinishEvent event = new org.apache.ambari.server.events.StackUpgradeFinishEvent(cluster);
        listener.onAmbariEvent(event);
        verifyAll();
    }
}