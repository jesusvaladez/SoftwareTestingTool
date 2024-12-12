package org.apache.ambari.server.controller;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
public class UserAuthenticationSourceResponse implements org.apache.ambari.server.controller.ApiModel {
    private final java.lang.String userName;

    private final java.lang.Long sourceId;

    private final org.apache.ambari.server.security.authorization.UserAuthenticationType authenticationType;

    private final java.lang.String key;

    private final java.util.Date createTime;

    private final java.util.Date updateTime;

    public UserAuthenticationSourceResponse(java.lang.String userName, java.lang.Long sourceId, org.apache.ambari.server.security.authorization.UserAuthenticationType authenticationType, java.lang.String key, java.util.Date createTime, java.util.Date updateTime) {
        this.userName = userName;
        this.sourceId = sourceId;
        this.authenticationType = authenticationType;
        this.key = key;
        this.createTime = createTime;
        this.updateTime = updateTime;
    }

    @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.controller.internal.UserAuthenticationSourceResourceProvider.USER_NAME_PROPERTY_ID, required = true)
    public java.lang.String getUserName() {
        return userName;
    }

    @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.controller.internal.UserAuthenticationSourceResourceProvider.AUTHENTICATION_SOURCE_ID_PROPERTY_ID, required = true)
    public java.lang.Long getSourceId() {
        return sourceId;
    }

    @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.controller.internal.UserAuthenticationSourceResourceProvider.AUTHENTICATION_TYPE_PROPERTY_ID, required = true)
    public org.apache.ambari.server.security.authorization.UserAuthenticationType getAuthenticationType() {
        return authenticationType;
    }

    @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.controller.internal.UserAuthenticationSourceResourceProvider.KEY_PROPERTY_ID)
    public java.lang.String getKey() {
        return key;
    }

    @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.controller.internal.UserAuthenticationSourceResourceProvider.CREATED_PROPERTY_ID)
    public java.util.Date getCreateTime() {
        return createTime;
    }

    @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.controller.internal.UserAuthenticationSourceResourceProvider.UPDATED_PROPERTY_ID)
    public java.util.Date getUpdateTime() {
        return updateTime;
    }

    @java.lang.Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        } else if ((o == null) || (getClass() != o.getClass())) {
            return false;
        } else {
            org.apache.ambari.server.controller.UserAuthenticationSourceResponse that = ((org.apache.ambari.server.controller.UserAuthenticationSourceResponse) (o));
            org.apache.commons.lang.builder.EqualsBuilder equalsBuilder = new org.apache.commons.lang.builder.EqualsBuilder();
            equalsBuilder.append(userName, that.userName);
            equalsBuilder.append(sourceId, that.sourceId);
            equalsBuilder.append(authenticationType, that.authenticationType);
            equalsBuilder.append(key, that.key);
            equalsBuilder.append(createTime, that.createTime);
            equalsBuilder.append(updateTime, that.updateTime);
            return equalsBuilder.isEquals();
        }
    }

    @java.lang.Override
    public int hashCode() {
        org.apache.commons.lang.builder.HashCodeBuilder hashCodeBuilder = new org.apache.commons.lang.builder.HashCodeBuilder();
        hashCodeBuilder.append(userName);
        hashCodeBuilder.append(sourceId);
        hashCodeBuilder.append(authenticationType);
        hashCodeBuilder.append(key);
        hashCodeBuilder.append(createTime);
        hashCodeBuilder.append(updateTime);
        return hashCodeBuilder.toHashCode();
    }

    public interface UserAuthenticationSourceResponseSwagger {
        @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.controller.internal.UserAuthenticationSourceResourceProvider.AUTHENTICATION_SOURCE_RESOURCE_CATEGORY)
        @java.lang.SuppressWarnings("unused")
        org.apache.ambari.server.controller.UserAuthenticationSourceResponse getUserAuthenticationSourceResponse();
    }
}