package org.apache.ambari.server.controller;
import io.swagger.annotations.ApiModelProperty;
public interface ClusterArtifactResponse {
    @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.controller.internal.ArtifactResourceProvider.RESPONSE_KEY)
    @java.lang.SuppressWarnings("unused")
    org.apache.ambari.server.controller.ClusterArtifactResponse.ClusterArtifactResponseInfo getClusterArtifactResponseInfo();

    @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.controller.internal.ArtifactResourceProvider.ARTIFACT_DATA_PROPERTY)
    java.util.Map<java.lang.String, java.lang.Object> getArtifactData();

    interface ClusterArtifactResponseInfo {
        @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.controller.internal.ArtifactResourceProvider.ARTIFACT_NAME)
        java.lang.String getArtifactName();

        @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.controller.internal.ArtifactResourceProvider.CLUSTER_NAME)
        java.lang.String getClusterName();
    }
}