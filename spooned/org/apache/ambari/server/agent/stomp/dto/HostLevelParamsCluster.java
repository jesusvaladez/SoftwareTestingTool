package org.apache.ambari.server.agent.stomp.dto;
@com.fasterxml.jackson.annotation.JsonInclude(com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY)
public class HostLevelParamsCluster {
    @com.fasterxml.jackson.annotation.JsonProperty("recoveryConfig")
    private org.apache.ambari.server.agent.RecoveryConfig recoveryConfig;

    @com.fasterxml.jackson.annotation.JsonProperty("blueprint_provisioning_state")
    private java.util.Map<java.lang.String, org.apache.ambari.server.state.BlueprintProvisioningState> blueprintProvisioningState;

    public HostLevelParamsCluster(org.apache.ambari.server.agent.RecoveryConfig recoveryConfig, java.util.Map<java.lang.String, org.apache.ambari.server.state.BlueprintProvisioningState> blueprintProvisioningState) {
        this.recoveryConfig = recoveryConfig;
        this.blueprintProvisioningState = blueprintProvisioningState;
    }

    public org.apache.ambari.server.agent.RecoveryConfig getRecoveryConfig() {
        return recoveryConfig;
    }

    public java.util.Map<java.lang.String, org.apache.ambari.server.state.BlueprintProvisioningState> getBlueprintProvisioningState() {
        return blueprintProvisioningState;
    }
}