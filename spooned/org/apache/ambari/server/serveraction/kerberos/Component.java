package org.apache.ambari.server.serveraction.kerberos;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
public class Component {
    private final java.lang.String hostName;

    private final java.lang.String serviceName;

    private final java.lang.String serviceComponentName;

    private final java.lang.Long hostId;

    public static org.apache.ambari.server.serveraction.kerberos.Component fromServiceComponentHost(org.apache.ambari.server.state.ServiceComponentHost serviceComponentHost) {
        return new org.apache.ambari.server.serveraction.kerberos.Component(serviceComponentHost.getHostName(), serviceComponentHost.getServiceName(), serviceComponentHost.getServiceComponentName(), serviceComponentHost.getHost().getHostId());
    }

    public Component(java.lang.String hostName, java.lang.String serviceName, java.lang.String serviceComponentName, java.lang.Long hostId) {
        this.hostName = hostName;
        this.serviceName = serviceName;
        this.serviceComponentName = serviceComponentName;
        this.hostId = hostId;
    }

    public java.lang.String getHostName() {
        return hostName;
    }

    public java.lang.String getServiceName() {
        return serviceName;
    }

    public java.lang.String getServiceComponentName() {
        return serviceComponentName;
    }

    public java.lang.Long getHostId() {
        return hostId;
    }

    @java.lang.Override
    public boolean equals(java.lang.Object o) {
        if (this == o)
            return true;

        if ((o == null) || (getClass() != o.getClass()))
            return false;

        org.apache.ambari.server.serveraction.kerberos.Component component = ((org.apache.ambari.server.serveraction.kerberos.Component) (o));
        return new org.apache.commons.lang.builder.EqualsBuilder().append(hostName, component.hostName).append(serviceName, component.serviceName).append(serviceComponentName, component.serviceComponentName).append(hostId, component.hostId).isEquals();
    }

    @java.lang.Override
    public int hashCode() {
        return new org.apache.commons.lang.builder.HashCodeBuilder(17, 37).append(hostName).append(serviceName).append(serviceComponentName).append(hostId).toHashCode();
    }
}