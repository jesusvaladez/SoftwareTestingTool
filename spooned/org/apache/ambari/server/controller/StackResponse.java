package org.apache.ambari.server.controller;
import io.swagger.annotations.ApiModelProperty;
public class StackResponse {
    private java.lang.String stackName;

    public StackResponse(java.lang.String stackName) {
        setStackName(stackName);
    }

    @io.swagger.annotations.ApiModelProperty(name = "stack_name")
    public java.lang.String getStackName() {
        return stackName;
    }

    public void setStackName(java.lang.String stackName) {
        this.stackName = stackName;
    }

    @java.lang.Override
    public int hashCode() {
        int result;
        result = 31 + getStackName().hashCode();
        return result;
    }

    @java.lang.Override
    public boolean equals(java.lang.Object obj) {
        if (!(obj instanceof org.apache.ambari.server.controller.StackResponse)) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        org.apache.ambari.server.controller.StackResponse stackResponse = ((org.apache.ambari.server.controller.StackResponse) (obj));
        return getStackName().equals(stackResponse.getStackName());
    }

    public interface StackResponseSwagger extends org.apache.ambari.server.controller.ApiModel {
        @io.swagger.annotations.ApiModelProperty(name = "Stacks")
        public org.apache.ambari.server.controller.StackResponse getStackResponse();
    }
}