package org.apache.ambari.server.controller;
import io.swagger.annotations.ApiModelProperty;
public interface ClusterServiceArtifactResponse {
    @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.controller.internal.ArtifactResourceProvider.RESPONSE_KEY)
    @java.lang.SuppressWarnings("unused")
    org.apache.ambari.server.controller.ClusterServiceArtifactResponse.ClusterServiceArtifactResponseInfo getClusterServiceArtifactResponseInfo();

    @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.controller.internal.ArtifactResourceProvider.ARTIFACT_DATA_PROPERTY)
    java.util.Map<java.lang.String, java.lang.Object> getArtifactData();

    interface ClusterServiceArtifactResponseInfo {
        @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.controller.internal.ArtifactResourceProvider.ARTIFACT_NAME)
        java.lang.String getArtifactName();

        @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.controller.internal.ArtifactResourceProvider.CLUSTER_NAME)
        java.lang.String getClusterName();

        @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.controller.internal.ArtifactResourceProvider.SERVICE_NAME)
        java.lang.String getServiceName();
    }
}