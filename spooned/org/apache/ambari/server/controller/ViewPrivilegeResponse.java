package org.apache.ambari.server.controller;
import io.swagger.annotations.ApiModelProperty;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.UriInfo;
public class ViewPrivilegeResponse extends org.apache.ambari.server.controller.PrivilegeResponse implements org.apache.ambari.server.controller.ApiModel {
    @java.lang.Override
    @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.controller.internal.ClusterResourceProvider.CLUSTER_NAME, hidden = true)
    public java.lang.String getClusterName() {
        return clusterName;
    }

    @java.lang.Override
    @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.controller.internal.PrivilegeResourceProvider.TYPE_PROPERTY_ID, hidden = true)
    public org.apache.ambari.server.security.authorization.ResourceType getType() {
        return type;
    }

    public interface ViewPrivilegeResponseWrapper extends org.apache.ambari.server.controller.ApiModel {
        @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.controller.internal.ViewPrivilegeResourceProvider.PRIVILEGE_INFO)
        @java.lang.SuppressWarnings("unused")
        org.apache.ambari.server.controller.ViewPrivilegeResponse getViewPrivilegeResponse();
    }
}