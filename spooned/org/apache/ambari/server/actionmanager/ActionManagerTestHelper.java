package org.apache.ambari.server.actionmanager;
import static org.easymock.EasyMock.createMockBuilder;
import static org.easymock.EasyMock.createNiceMock;
public class ActionManagerTestHelper {
    @com.google.inject.Inject
    private org.apache.ambari.server.actionmanager.ActionDBAccessor actionDBAccessor;

    @com.google.inject.Inject
    private com.google.inject.Injector injector;

    public org.apache.ambari.server.actionmanager.ActionManager getMockActionManager() {
        org.apache.ambari.server.actionmanager.ActionManager actionManager = EasyMock.createMockBuilder(org.apache.ambari.server.actionmanager.ActionManager.class).addMockedMethod("getTasks").withConstructor(actionDBAccessor, injector.getInstance(org.apache.ambari.server.actionmanager.RequestFactory.class), EasyMock.createNiceMock(org.apache.ambari.server.actionmanager.ActionScheduler.class)).createMock();
        return actionManager;
    }
}