package org.apache.ambari.server.topology.addservice;
import io.swagger.annotations.ApiModelProperty;
import static org.apache.ambari.server.controller.internal.BaseClusterRequest.PROVISION_ACTION_PROPERTY;
public final class Component {
    private static final java.lang.String COMPONENT_NAME = "name";

    private static final java.lang.String HOSTS = "hosts";

    private final java.lang.String name;

    private final java.util.Set<org.apache.ambari.server.topology.addservice.Host> hosts;

    private final org.apache.ambari.server.controller.internal.ProvisionAction provisionAction;

    @com.fasterxml.jackson.annotation.JsonCreator
    public Component(@com.fasterxml.jackson.annotation.JsonProperty(org.apache.ambari.server.topology.addservice.Component.COMPONENT_NAME)
    java.lang.String name, @com.fasterxml.jackson.annotation.JsonProperty(org.apache.ambari.server.controller.internal.BaseClusterRequest.PROVISION_ACTION_PROPERTY)
    org.apache.ambari.server.controller.internal.ProvisionAction provisionAction, @com.fasterxml.jackson.annotation.JsonProperty(org.apache.ambari.server.topology.addservice.Component.HOSTS)
    java.util.Set<org.apache.ambari.server.topology.addservice.Host> hosts) {
        this.name = name;
        this.provisionAction = provisionAction;
        this.hosts = (hosts != null) ? com.google.common.collect.ImmutableSet.copyOf(hosts) : com.google.common.collect.ImmutableSet.of();
    }

    public static org.apache.ambari.server.topology.addservice.Component of(java.lang.String name, java.lang.String... hosts) {
        return org.apache.ambari.server.topology.addservice.Component.of(name, null, hosts);
    }

    public static org.apache.ambari.server.topology.addservice.Component of(java.lang.String name, org.apache.ambari.server.controller.internal.ProvisionAction provisionAction, java.lang.String... hosts) {
        return new org.apache.ambari.server.topology.addservice.Component(name, provisionAction, java.util.Arrays.stream(hosts).map(org.apache.ambari.server.topology.addservice.Host::new).collect(java.util.stream.Collectors.toSet()));
    }

    @com.fasterxml.jackson.annotation.JsonProperty(org.apache.ambari.server.topology.addservice.Component.COMPONENT_NAME)
    @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.topology.addservice.Component.COMPONENT_NAME)
    public java.lang.String getName() {
        return name;
    }

    @com.fasterxml.jackson.annotation.JsonProperty(org.apache.ambari.server.topology.addservice.Component.HOSTS)
    @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.topology.addservice.Component.HOSTS)
    public java.util.Set<org.apache.ambari.server.topology.addservice.Host> getHosts() {
        return hosts;
    }

    @com.fasterxml.jackson.annotation.JsonProperty(org.apache.ambari.server.controller.internal.BaseClusterRequest.PROVISION_ACTION_PROPERTY)
    @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.controller.internal.BaseClusterRequest.PROVISION_ACTION_PROPERTY)
    public org.apache.ambari.server.controller.internal.ProvisionAction _getProvisionAction() {
        return provisionAction;
    }

    @org.apache.ambari.annotations.ApiIgnore
    @com.fasterxml.jackson.annotation.JsonIgnore
    public java.util.Optional<org.apache.ambari.server.controller.internal.ProvisionAction> getProvisionAction() {
        return java.util.Optional.ofNullable(provisionAction);
    }

    @java.lang.Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if ((o == null) || (getClass() != o.getClass())) {
            return false;
        }
        org.apache.ambari.server.topology.addservice.Component other = ((org.apache.ambari.server.topology.addservice.Component) (o));
        return (java.util.Objects.equals(name, other.name) && java.util.Objects.equals(hosts, other.hosts)) && java.util.Objects.equals(provisionAction, other.provisionAction);
    }

    @java.lang.Override
    public int hashCode() {
        return java.util.Objects.hash(name, hosts, provisionAction);
    }

    @java.lang.Override
    public java.lang.String toString() {
        return name;
    }
}