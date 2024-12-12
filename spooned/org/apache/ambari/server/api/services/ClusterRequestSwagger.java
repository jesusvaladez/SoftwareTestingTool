package org.apache.ambari.server.api.services;
import io.swagger.annotations.ApiModelProperty;
@java.lang.SuppressWarnings("unused")
public interface ClusterRequestSwagger extends org.apache.ambari.server.controller.ApiModel {
    @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.controller.internal.ClusterResourceProvider.RESPONSE_KEY)
    org.apache.ambari.server.controller.ClusterRequest getClusterRequest();
}