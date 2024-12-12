package org.apache.ambari.server.orm.models;
import org.codehaus.jackson.annotate.JsonProperty;
@org.apache.ambari.server.StaticallyInject
public class HostComponentSummary {
    @org.codehaus.jackson.annotate.JsonProperty("service_name")
    private java.lang.String serviceName;

    @org.codehaus.jackson.annotate.JsonProperty("component_name")
    private java.lang.String componentName;

    @org.codehaus.jackson.annotate.JsonProperty("host_id")
    private java.lang.Long hostId;

    @org.codehaus.jackson.annotate.JsonProperty("host_name")
    private java.lang.String hostName;

    @org.codehaus.jackson.annotate.JsonProperty("desired_state")
    private org.apache.ambari.server.state.State desiredState;

    @org.codehaus.jackson.annotate.JsonProperty("current_state")
    private org.apache.ambari.server.state.State currentState;

    @com.google.inject.Inject
    private static org.apache.ambari.server.orm.dao.HostDAO hostDao;

    @com.google.inject.Inject
    private static org.apache.ambari.server.orm.dao.HostComponentStateDAO hostComponentStateDao;

    @com.google.inject.Inject
    private static org.apache.ambari.server.orm.dao.HostComponentDesiredStateDAO hostComponentDesiredStateDao;

    public HostComponentSummary(java.lang.String serviceName, java.lang.String componentName, java.lang.Long hostId, org.apache.ambari.server.state.State desiredState, org.apache.ambari.server.state.State currentState) {
        this.serviceName = serviceName;
        this.componentName = componentName;
        this.hostId = hostId;
        org.apache.ambari.server.orm.entities.HostEntity host = org.apache.ambari.server.orm.models.HostComponentSummary.hostDao.findById(hostId);
        if (host != null) {
            hostName = host.getHostName();
        }
        this.desiredState = desiredState;
        this.currentState = currentState;
    }

    public long getHostId() {
        return hostId;
    }

    public java.lang.String getHostName() {
        return (hostName == null) || hostName.isEmpty() ? "" : hostName;
    }

    public org.apache.ambari.server.state.State getDesiredState() {
        return desiredState;
    }

    public org.apache.ambari.server.state.State getCurrentState() {
        return currentState;
    }

    public static java.util.List<org.apache.ambari.server.orm.models.HostComponentSummary> getHostComponentSummaries(java.lang.String serviceName, java.lang.String componentName) {
        java.util.List<org.apache.ambari.server.orm.models.HostComponentSummary> hostComponentSummaries = new java.util.ArrayList<>();
        java.util.List<org.apache.ambari.server.orm.entities.HostComponentStateEntity> hostComponentStates = org.apache.ambari.server.orm.models.HostComponentSummary.hostComponentStateDao.findByServiceAndComponent(serviceName, componentName);
        if (hostComponentStates != null) {
            for (org.apache.ambari.server.orm.entities.HostComponentStateEntity hcse : hostComponentStates) {
                org.apache.ambari.server.orm.entities.HostComponentDesiredStateEntity hcdse = org.apache.ambari.server.orm.models.HostComponentSummary.hostComponentDesiredStateDao.findByServiceComponentAndHost(hcse.getServiceName(), hcse.getComponentName(), hcse.getHostName());
                if (hcdse != null) {
                    org.apache.ambari.server.orm.models.HostComponentSummary s = new org.apache.ambari.server.orm.models.HostComponentSummary(hcse.getServiceName(), hcse.getComponentName(), hcse.getHostId(), hcdse.getDesiredState(), hcse.getCurrentState());
                    hostComponentSummaries.add(s);
                }
            }
        }
        return hostComponentSummaries;
    }

    @java.lang.Override
    public int hashCode() {
        int result;
        result = 31 + (serviceName != null ? serviceName.hashCode() : 0);
        result = result + (componentName != null ? componentName.hashCode() : 0);
        result = result + (hostId != null ? hostId.hashCode() : 0);
        return result;
    }
}