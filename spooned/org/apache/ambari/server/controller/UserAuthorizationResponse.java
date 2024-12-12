package org.apache.ambari.server.controller;
import io.swagger.annotations.ApiModelProperty;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.UriInfo;
public class UserAuthorizationResponse implements org.apache.ambari.server.controller.ApiModel {
    private final java.lang.String authorizationId;

    private final java.lang.String authorizationName;

    private final java.lang.String resourceType;

    private final java.lang.String userName;

    private java.lang.String clusterName;

    private java.lang.String viewName;

    private java.lang.String viewVersion;

    private java.lang.String viewInstanceName;

    public UserAuthorizationResponse(java.lang.String authorizationId, java.lang.String authorizationName, java.lang.String clusterName, java.lang.String resourceType, java.lang.String userName) {
        this.authorizationId = authorizationId;
        this.authorizationName = authorizationName;
        this.clusterName = clusterName;
        this.resourceType = resourceType;
        this.userName = userName;
    }

    public UserAuthorizationResponse(java.lang.String authorizationId, java.lang.String authorizationName, java.lang.String resourceType, java.lang.String userName, java.lang.String viewName, java.lang.String viewVersion, java.lang.String viewInstanceName) {
        this.authorizationId = authorizationId;
        this.authorizationName = authorizationName;
        this.resourceType = resourceType;
        this.userName = userName;
        this.viewName = viewName;
        this.viewVersion = viewVersion;
        this.viewInstanceName = viewInstanceName;
    }

    @io.swagger.annotations.ApiModelProperty(name = "AuthorizationInfo/authorization_id")
    public java.lang.String getAuthorizationId() {
        return authorizationId;
    }

    @io.swagger.annotations.ApiModelProperty(name = "AuthorizationInfo/authorization_name")
    public java.lang.String getAuthorizationName() {
        return authorizationName;
    }

    @io.swagger.annotations.ApiModelProperty(name = "AuthorizationInfo/resource_type")
    public java.lang.String getResourceType() {
        return resourceType;
    }

    @io.swagger.annotations.ApiModelProperty(name = "AuthorizationInfo/view_version")
    public java.lang.String getViewVersion() {
        return viewVersion;
    }

    @io.swagger.annotations.ApiModelProperty(name = "AuthorizationInfo/view_instance_name")
    public java.lang.String getViewInstanceName() {
        return viewInstanceName;
    }

    @io.swagger.annotations.ApiModelProperty(name = "AuthorizationInfo/user_name", required = true)
    public java.lang.String getUserName() {
        return userName;
    }

    @io.swagger.annotations.ApiModelProperty(name = "AuthorizationInfo/cluster_name")
    public java.lang.String getClusterName() {
        return clusterName;
    }

    @io.swagger.annotations.ApiModelProperty(name = "AuthorizationInfo/view_name")
    public java.lang.String getViewName() {
        return viewName;
    }
}