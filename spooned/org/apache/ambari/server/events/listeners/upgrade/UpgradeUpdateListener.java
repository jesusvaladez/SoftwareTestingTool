package org.apache.ambari.server.events.listeners.upgrade;
@com.google.inject.Singleton
@org.apache.ambari.server.EagerSingleton
public class UpgradeUpdateListener {
    private org.apache.ambari.server.events.publishers.STOMPUpdatePublisher STOMPUpdatePublisher;

    @com.google.inject.Inject
    private org.apache.ambari.server.orm.dao.UpgradeDAO upgradeDAO;

    @com.google.inject.Inject
    private org.apache.ambari.server.orm.dao.HostRoleCommandDAO hostRoleCommandDAO;

    @com.google.inject.Inject
    private org.apache.ambari.server.orm.dao.RequestDAO requestDAO;

    @com.google.inject.Inject
    public UpgradeUpdateListener(org.apache.ambari.server.events.publishers.STOMPUpdatePublisher STOMPUpdatePublisher) {
        STOMPUpdatePublisher.registerAPI(this);
        this.STOMPUpdatePublisher = STOMPUpdatePublisher;
    }

    @com.google.common.eventbus.Subscribe
    public void onRequestUpdate(org.apache.ambari.server.events.RequestUpdateEvent requestUpdateEvent) {
        org.apache.ambari.server.orm.entities.UpgradeEntity upgradeEntity = upgradeDAO.findUpgradeByRequestId(requestUpdateEvent.getRequestId());
        if (upgradeEntity != null) {
            STOMPUpdatePublisher.publish(org.apache.ambari.server.events.UpgradeUpdateEvent.formUpdateEvent(hostRoleCommandDAO, requestDAO, upgradeEntity));
        }
    }
}