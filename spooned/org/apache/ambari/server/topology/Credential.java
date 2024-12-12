package org.apache.ambari.server.topology;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import static org.apache.ambari.server.controller.internal.ProvisionClusterRequest.ALIAS;
import static org.apache.ambari.server.controller.internal.ProvisionClusterRequest.KEY;
import static org.apache.ambari.server.controller.internal.ProvisionClusterRequest.PRINCIPAL;
import static org.apache.ambari.server.controller.internal.ProvisionClusterRequest.TYPE;
@io.swagger.annotations.ApiModel
public class Credential {
    private final java.lang.String alias;

    private final java.lang.String principal;

    private final java.lang.String key;

    private final org.apache.ambari.server.security.encryption.CredentialStoreType type;

    @com.fasterxml.jackson.annotation.JsonCreator
    public Credential(@com.fasterxml.jackson.annotation.JsonProperty(org.apache.ambari.server.controller.internal.ProvisionClusterRequest.ALIAS)
    java.lang.String alias, @com.fasterxml.jackson.annotation.JsonProperty(org.apache.ambari.server.controller.internal.ProvisionClusterRequest.PRINCIPAL)
    java.lang.String principal, @com.fasterxml.jackson.annotation.JsonProperty(org.apache.ambari.server.controller.internal.ProvisionClusterRequest.KEY)
    java.lang.String key, @com.fasterxml.jackson.annotation.JsonProperty(org.apache.ambari.server.controller.internal.ProvisionClusterRequest.TYPE)
    org.apache.ambari.server.security.encryption.CredentialStoreType type) {
        this.alias = alias;
        this.principal = principal;
        this.key = key;
        this.type = type;
    }

    @com.fasterxml.jackson.annotation.JsonProperty(org.apache.ambari.server.controller.internal.ProvisionClusterRequest.ALIAS)
    @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.controller.internal.ProvisionClusterRequest.ALIAS)
    public java.lang.String getAlias() {
        return alias;
    }

    @com.fasterxml.jackson.annotation.JsonProperty(org.apache.ambari.server.controller.internal.ProvisionClusterRequest.PRINCIPAL)
    @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.controller.internal.ProvisionClusterRequest.PRINCIPAL)
    public java.lang.String getPrincipal() {
        return principal;
    }

    @com.fasterxml.jackson.annotation.JsonProperty(org.apache.ambari.server.controller.internal.ProvisionClusterRequest.KEY)
    @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.controller.internal.ProvisionClusterRequest.KEY)
    public java.lang.String getKey() {
        return key;
    }

    @com.fasterxml.jackson.annotation.JsonProperty(org.apache.ambari.server.controller.internal.ProvisionClusterRequest.TYPE)
    @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.controller.internal.ProvisionClusterRequest.TYPE)
    public org.apache.ambari.server.security.encryption.CredentialStoreType getType() {
        return type;
    }

    @java.lang.Override
    public boolean equals(java.lang.Object obj) {
        if (this == obj) {
            return true;
        }
        if ((obj == null) || (obj.getClass() != getClass())) {
            return false;
        }
        org.apache.ambari.server.topology.Credential other = ((org.apache.ambari.server.topology.Credential) (obj));
        return ((java.util.Objects.equals(alias, other.alias) && java.util.Objects.equals(principal, other.principal)) && java.util.Objects.equals(key, other.key)) && java.util.Objects.equals(type, other.type);
    }

    @java.lang.Override
    public int hashCode() {
        return java.util.Objects.hash(alias, principal, key, type);
    }

    @java.lang.Override
    public java.lang.String toString() {
        return alias;
    }
}