package org.apache.ambari.server.events;
public class StaleConfigsUpdateEvent extends org.apache.ambari.server.events.AmbariEvent {
    private org.apache.ambari.server.state.svccomphost.ServiceComponentHostImpl serviceComponentHost;

    private java.lang.Boolean staleConfigs;

    public StaleConfigsUpdateEvent(org.apache.ambari.server.state.svccomphost.ServiceComponentHostImpl serviceComponentHost, java.lang.Boolean staleConfigs) {
        super(org.apache.ambari.server.events.AmbariEvent.AmbariEventType.STALE_CONFIGS_UPDATE);
        this.serviceComponentHost = serviceComponentHost;
        this.staleConfigs = staleConfigs;
    }

    public org.apache.ambari.server.state.svccomphost.ServiceComponentHostImpl getServiceComponentHost() {
        return serviceComponentHost;
    }

    public void setServiceComponentHost(org.apache.ambari.server.state.svccomphost.ServiceComponentHostImpl serviceComponentHost) {
        this.serviceComponentHost = serviceComponentHost;
    }

    public java.lang.Boolean getStaleConfigs() {
        return staleConfigs;
    }

    public void setStaleConfigs(java.lang.Boolean staleConfigs) {
        this.staleConfigs = staleConfigs;
    }
}