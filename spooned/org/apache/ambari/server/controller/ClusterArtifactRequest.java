package org.apache.ambari.server.controller;
import io.swagger.annotations.ApiModelProperty;
@java.lang.SuppressWarnings("unused")
public interface ClusterArtifactRequest extends org.apache.ambari.server.controller.ApiModel {
    @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.controller.internal.ArtifactResourceProvider.RESPONSE_KEY)
    org.apache.ambari.server.controller.ClusterArtifactRequest.ClusterArtifactRequestInfo getClusterArtifactRequestInfo();

    @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.controller.internal.ArtifactResourceProvider.ARTIFACT_DATA_PROPERTY)
    java.util.Map<java.lang.String, java.lang.Object> getArtifactData();

    interface ClusterArtifactRequestInfo {
        @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.controller.internal.ArtifactResourceProvider.ARTIFACT_NAME)
        java.lang.String getArtifactName();
    }
}