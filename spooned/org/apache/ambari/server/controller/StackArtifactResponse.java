package org.apache.ambari.server.controller;
import io.swagger.annotations.ApiModelProperty;
public interface StackArtifactResponse extends org.apache.ambari.server.controller.ApiModel {
    @io.swagger.annotations.ApiModelProperty(name = "Artifacts")
    org.apache.ambari.server.controller.StackArtifactResponse.Artifacts getArtifacts();

    @io.swagger.annotations.ApiModelProperty(name = "artifact_data")
    java.util.Map<java.lang.String, java.lang.Object> getArtifactData();

    interface Artifacts {
        @io.swagger.annotations.ApiModelProperty(name = "artifact_name")
        java.lang.String getArtifactName();

        @io.swagger.annotations.ApiModelProperty(name = "service_name")
        java.lang.String getServiceName();

        @io.swagger.annotations.ApiModelProperty(name = "stack_name")
        java.lang.String getStackName();

        @io.swagger.annotations.ApiModelProperty(name = "stack_version")
        java.lang.String getStackVersion();
    }
}