package org.apache.ambari.server.topology.addservice;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import static org.apache.ambari.server.controller.internal.BaseClusterRequest.PROVISION_ACTION_PROPERTY;
@io.swagger.annotations.ApiModel
public final class Service {
    private static final java.lang.String NAME = "name";

    private final java.lang.String name;

    private final org.apache.ambari.server.controller.internal.ProvisionAction provisionAction;

    @com.fasterxml.jackson.annotation.JsonCreator
    public Service(@com.fasterxml.jackson.annotation.JsonProperty(org.apache.ambari.server.topology.addservice.Service.NAME)
    java.lang.String name, @com.fasterxml.jackson.annotation.JsonProperty(org.apache.ambari.server.controller.internal.BaseClusterRequest.PROVISION_ACTION_PROPERTY)
    org.apache.ambari.server.controller.internal.ProvisionAction provisionAction) {
        this.name = name;
        this.provisionAction = provisionAction;
    }

    public static org.apache.ambari.server.topology.addservice.Service of(java.lang.String name) {
        return org.apache.ambari.server.topology.addservice.Service.of(name, null);
    }

    public static org.apache.ambari.server.topology.addservice.Service of(java.lang.String name, org.apache.ambari.server.controller.internal.ProvisionAction provisionAction) {
        return new org.apache.ambari.server.topology.addservice.Service(name, provisionAction);
    }

    @com.fasterxml.jackson.annotation.JsonProperty(org.apache.ambari.server.topology.addservice.Service.NAME)
    @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.topology.addservice.Service.NAME)
    public java.lang.String getName() {
        return name;
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
        if (this == o)
            return true;

        if ((o == null) || (getClass() != o.getClass()))
            return false;

        org.apache.ambari.server.topology.addservice.Service service = ((org.apache.ambari.server.topology.addservice.Service) (o));
        return java.util.Objects.equals(name, service.name) && java.util.Objects.equals(provisionAction, service.provisionAction);
    }

    @java.lang.Override
    public int hashCode() {
        return java.util.Objects.hash(name, provisionAction);
    }

    @java.lang.Override
    public java.lang.String toString() {
        return name;
    }
}