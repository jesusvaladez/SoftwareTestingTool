package org.apache.ambari.server.topology.addservice;
import io.swagger.annotations.ApiModelProperty;
public final class Host {
    private static final java.lang.String FQDN = "fqdn";

    private final java.lang.String fqdn;

    @com.fasterxml.jackson.annotation.JsonCreator
    public Host(@com.fasterxml.jackson.annotation.JsonProperty(org.apache.ambari.server.topology.addservice.Host.FQDN)
    java.lang.String fqdn) {
        this.fqdn = fqdn;
    }

    @com.fasterxml.jackson.annotation.JsonProperty(org.apache.ambari.server.topology.addservice.Host.FQDN)
    @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.topology.addservice.Host.FQDN)
    public java.lang.String getFqdn() {
        return fqdn;
    }

    @java.lang.Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if ((o == null) || (getClass() != o.getClass())) {
            return false;
        }
        org.apache.ambari.server.topology.addservice.Host other = ((org.apache.ambari.server.topology.addservice.Host) (o));
        return java.util.Objects.equals(fqdn, other.fqdn);
    }

    @java.lang.Override
    public int hashCode() {
        return java.util.Objects.hashCode(fqdn);
    }

    @java.lang.Override
    public java.lang.String toString() {
        return "host: " + fqdn;
    }
}