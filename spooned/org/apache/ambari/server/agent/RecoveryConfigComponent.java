package org.apache.ambari.server.agent;
public class RecoveryConfigComponent {
    @com.google.gson.annotations.SerializedName("component_name")
    @com.fasterxml.jackson.annotation.JsonProperty("component_name")
    private java.lang.String componentName;

    @com.google.gson.annotations.SerializedName("service_name")
    @com.fasterxml.jackson.annotation.JsonProperty("service_name")
    private java.lang.String serviceName;

    @com.google.gson.annotations.SerializedName("desired_state")
    @com.fasterxml.jackson.annotation.JsonProperty("desired_state")
    private java.lang.String desiredState;

    public RecoveryConfigComponent(java.lang.String componentName, java.lang.String serviceName, org.apache.ambari.server.state.State desiredState) {
        this.setComponentName(componentName);
        this.setServiceName(serviceName);
        this.setDesiredState(desiredState);
    }

    public RecoveryConfigComponent(org.apache.ambari.server.state.ServiceComponentHost sch) {
        this(sch.getServiceComponentName(), sch.getServiceName(), sch.getDesiredState());
    }

    public java.lang.String getComponentName() {
        return componentName;
    }

    public void setComponentName(java.lang.String componentName) {
        this.componentName = componentName;
    }

    public org.apache.ambari.server.state.State getDesiredState() {
        return org.apache.ambari.server.state.State.valueOf(desiredState);
    }

    public void setDesiredState(org.apache.ambari.server.state.State state) {
        this.desiredState = state.toString();
    }

    @java.lang.Override
    public java.lang.String toString() {
        java.lang.StringBuilder sb = new java.lang.StringBuilder("{").append("componentName=").append(componentName).append(", serviceName=").append(serviceName).append(", desiredState=").append(desiredState).append("}");
        return sb.toString();
    }

    public java.lang.String getServiceName() {
        return serviceName;
    }

    public void setServiceName(java.lang.String serviceName) {
        this.serviceName = serviceName;
    }

    @java.lang.Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if ((o == null) || (getClass() != o.getClass())) {
            return false;
        }
        final org.apache.ambari.server.agent.RecoveryConfigComponent that = ((org.apache.ambari.server.agent.RecoveryConfigComponent) (o));
        return (java.util.Objects.equals(componentName, that.componentName) && java.util.Objects.equals(serviceName, that.serviceName)) && java.util.Objects.equals(desiredState, that.desiredState);
    }

    @java.lang.Override
    public int hashCode() {
        return java.util.Objects.hash(componentName, serviceName, desiredState);
    }
}