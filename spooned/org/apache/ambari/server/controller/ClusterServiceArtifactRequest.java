package org.apache.ambari.server.controller;
import io.swagger.annotations.ApiModelProperty;
@java.lang.SuppressWarnings("unused")
public interface ClusterServiceArtifactRequest extends org.apache.ambari.server.controller.ApiModel {
    @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.controller.internal.ArtifactResourceProvider.RESPONSE_KEY)
    org.apache.ambari.server.controller.ClusterServiceArtifactRequest.ClusterServiceArtifactRequestInfo getClusterServiceArtifactRequestInfo();

    @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.controller.internal.ArtifactResourceProvider.ARTIFACT_DATA_PROPERTY)
    java.util.Map<java.lang.String, java.lang.Object> getArtifactData();

    interface ClusterServiceArtifactRequestInfo {
        @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.controller.internal.ArtifactResourceProvider.ARTIFACT_NAME)
        java.lang.String getArtifactName();
    }
}