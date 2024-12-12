package org.apache.ambari.server.state;
import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonProperty;
public abstract class ServiceComponentHostEvent extends org.apache.ambari.server.state.fsm.event.AbstractEvent<org.apache.ambari.server.state.ServiceComponentHostEventType> {
    private final java.lang.String serviceComponentName;

    private final java.lang.String hostName;

    private final long opTimestamp;

    private final java.lang.String stackId;

    public ServiceComponentHostEvent(org.apache.ambari.server.state.ServiceComponentHostEventType type, java.lang.String serviceComponentName, java.lang.String hostName, long opTimestamp) {
        this(type, serviceComponentName, hostName, opTimestamp, "");
    }

    public ServiceComponentHostEvent(org.apache.ambari.server.state.ServiceComponentHostEventType type, java.lang.String serviceComponentName, java.lang.String hostName, long opTimestamp, java.lang.String stackId) {
        super(type);
        this.serviceComponentName = serviceComponentName;
        this.hostName = hostName;
        this.opTimestamp = opTimestamp;
        this.stackId = stackId;
    }

    public java.lang.String getServiceComponentName() {
        return serviceComponentName;
    }

    public java.lang.String getHostName() {
        return hostName;
    }

    public long getOpTimestamp() {
        return opTimestamp;
    }

    @org.codehaus.jackson.annotate.JsonCreator
    public static org.apache.ambari.server.state.ServiceComponentHostEvent create(@org.codehaus.jackson.annotate.JsonProperty("type")
    org.apache.ambari.server.state.ServiceComponentHostEventType type, @org.codehaus.jackson.annotate.JsonProperty("serviceComponentName")
    java.lang.String serviceComponentName, @org.codehaus.jackson.annotate.JsonProperty("hostName")
    java.lang.String hostName, @org.codehaus.jackson.annotate.JsonProperty("opTimestamp")
    long opTimestamp, @org.codehaus.jackson.annotate.JsonProperty("configs")
    java.util.Map<java.lang.String, java.lang.String> configs, @org.codehaus.jackson.annotate.JsonProperty("stackId")
    java.lang.String stackId) {
        switch (type) {
            case HOST_SVCCOMP_INSTALL :
                return new org.apache.ambari.server.state.svccomphost.ServiceComponentHostInstallEvent(serviceComponentName, hostName, opTimestamp, stackId);
            case HOST_SVCCOMP_OP_FAILED :
                return new org.apache.ambari.server.state.svccomphost.ServiceComponentHostOpFailedEvent(serviceComponentName, hostName, opTimestamp);
            case HOST_SVCCOMP_OP_IN_PROGRESS :
                return new org.apache.ambari.server.state.svccomphost.ServiceComponentHostOpInProgressEvent(serviceComponentName, hostName, opTimestamp);
            case HOST_SVCCOMP_OP_RESTART :
                return new org.apache.ambari.server.state.svccomphost.ServiceComponentHostOpRestartedEvent(serviceComponentName, hostName, opTimestamp);
            case HOST_SVCCOMP_OP_SUCCEEDED :
                return new org.apache.ambari.server.state.svccomphost.ServiceComponentHostOpSucceededEvent(serviceComponentName, hostName, opTimestamp);
            case HOST_SVCCOMP_STOPPED :
                return new org.apache.ambari.server.state.svccomphost.ServiceComponentHostStoppedEvent(serviceComponentName, hostName, opTimestamp);
            case HOST_SVCCOMP_STARTED :
                return new org.apache.ambari.server.state.svccomphost.ServiceComponentHostStartedEvent(serviceComponentName, hostName, opTimestamp);
            case HOST_SVCCOMP_START :
                return new org.apache.ambari.server.state.svccomphost.ServiceComponentHostStartEvent(serviceComponentName, hostName, opTimestamp);
            case HOST_SVCCOMP_STOP :
                return new org.apache.ambari.server.state.svccomphost.ServiceComponentHostStopEvent(serviceComponentName, hostName, opTimestamp);
            case HOST_SVCCOMP_UNINSTALL :
                return new org.apache.ambari.server.state.svccomphost.ServiceComponentHostUninstallEvent(serviceComponentName, hostName, opTimestamp);
            case HOST_SVCCOMP_WIPEOUT :
                return new org.apache.ambari.server.state.svccomphost.ServiceComponentHostWipeoutEvent(serviceComponentName, hostName, opTimestamp);
            case HOST_SVCCOMP_UPGRADE :
                return new org.apache.ambari.server.state.svccomphost.ServiceComponentHostUpgradeEvent(serviceComponentName, hostName, opTimestamp, stackId);
            case HOST_SVCCOMP_DISABLE :
                return new org.apache.ambari.server.state.svccomphost.ServiceComponentHostDisableEvent(serviceComponentName, hostName, opTimestamp);
            case HOST_SVCCOMP_RESTORE :
                return new org.apache.ambari.server.state.svccomphost.ServiceComponentHostRestoreEvent(serviceComponentName, hostName, opTimestamp);
            case HOST_SVCCOMP_SERVER_ACTION :
                return new org.apache.ambari.server.state.svccomphost.ServiceComponentHostServerActionEvent(serviceComponentName, hostName, opTimestamp);
        }
        return null;
    }

    public java.lang.String getStackId() {
        return stackId;
    }
}