package org.apache.ambari.server.controller;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
@org.apache.ambari.server.controller.ApiModel
public class UserAuthenticationSourceRequest {
    private final java.lang.String username;

    private final java.lang.Long sourceId;

    private final org.apache.ambari.server.security.authorization.UserAuthenticationType authenticationType;

    private final java.lang.String key;

    private final java.lang.String oldKey;

    public UserAuthenticationSourceRequest(java.lang.String username, java.lang.Long sourceId) {
        this(username, sourceId, null, null);
    }

    public UserAuthenticationSourceRequest(java.lang.String username, java.lang.Long sourceId, org.apache.ambari.server.security.authorization.UserAuthenticationType authenticationType) {
        this(username, sourceId, authenticationType, null);
    }

    public UserAuthenticationSourceRequest(java.lang.String username, java.lang.Long sourceId, org.apache.ambari.server.security.authorization.UserAuthenticationType authenticationType, java.lang.String key) {
        this(username, sourceId, authenticationType, key, null);
    }

    public UserAuthenticationSourceRequest(java.lang.String username, java.lang.Long sourceId, org.apache.ambari.server.security.authorization.UserAuthenticationType authenticationType, java.lang.String key, java.lang.String oldKey) {
        this.username = username;
        this.sourceId = sourceId;
        this.authenticationType = authenticationType;
        this.key = key;
        this.oldKey = oldKey;
    }

    @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.controller.internal.UserAuthenticationSourceResourceProvider.USER_NAME_PROPERTY_ID, hidden = true)
    public java.lang.String getUsername() {
        return username;
    }

    @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.controller.internal.UserAuthenticationSourceResourceProvider.AUTHENTICATION_SOURCE_ID_PROPERTY_ID)
    public java.lang.Long getSourceId() {
        return sourceId;
    }

    @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.controller.internal.UserAuthenticationSourceResourceProvider.AUTHENTICATION_TYPE_PROPERTY_ID)
    public org.apache.ambari.server.security.authorization.UserAuthenticationType getAuthenticationType() {
        return authenticationType;
    }

    @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.controller.internal.UserAuthenticationSourceResourceProvider.KEY_PROPERTY_ID)
    public java.lang.String getKey() {
        return key;
    }

    @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.controller.internal.UserAuthenticationSourceResourceProvider.OLD_KEY_PROPERTY_ID)
    public java.lang.String getOldKey() {
        return oldKey;
    }
}