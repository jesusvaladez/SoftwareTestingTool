package org.apache.ambari.server.controller;
import io.swagger.annotations.ApiModelProperty;
public class MpackResponse {
    private java.lang.Long id;

    private java.lang.String mpackId;

    private java.lang.String mpackName;

    private java.lang.String mpackVersion;

    private java.lang.String mpackUri;

    private java.lang.Long registryId;

    private java.lang.String stackId;

    private java.lang.String description;

    private java.lang.String displayName;

    public MpackResponse(org.apache.ambari.server.state.Mpack mpack) {
        this.id = mpack.getResourceId();
        this.mpackId = mpack.getMpackId();
        this.mpackName = mpack.getName();
        this.mpackVersion = mpack.getVersion();
        this.mpackUri = mpack.getMpackUri();
        this.registryId = mpack.getRegistryId();
        this.description = mpack.getDescription();
        this.displayName = mpack.getDisplayName();
    }

    public java.lang.Long getId() {
        return id;
    }

    public void setId(java.lang.Long id) {
        this.id = id;
    }

    public java.lang.Long getRegistryId() {
        return registryId;
    }

    public void setRegistryId(java.lang.Long registryId) {
        this.registryId = registryId;
    }

    public java.lang.String getMpackId() {
        return mpackId;
    }

    public void setMpackId(java.lang.String mpackId) {
        this.mpackId = mpackId;
    }

    public java.lang.String getMpackName() {
        return mpackName;
    }

    public void setMpackName(java.lang.String mpackName) {
        this.mpackName = mpackName;
    }

    public java.lang.String getDescription() {
        return description;
    }

    public void setDescription(java.lang.String description) {
        this.description = description;
    }

    public java.lang.String getMpackVersion() {
        return mpackVersion;
    }

    public void setMpackVersion(java.lang.String mpackVersion) {
        this.mpackVersion = mpackVersion;
    }

    public java.lang.String getMpackUri() {
        return mpackUri;
    }

    public void setMpackUri(java.lang.String mpackUri) {
        this.mpackUri = mpackUri;
    }

    public java.lang.String getStackId() {
        return stackId;
    }

    public void setStackId(java.lang.String stackId) {
        this.stackId = stackId;
    }

    public java.lang.String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(java.lang.String displayName) {
        this.displayName = displayName;
    }

    @java.lang.Override
    public int hashCode() {
        int result;
        result = 31 + getId().hashCode();
        return result;
    }

    @java.lang.Override
    public boolean equals(java.lang.Object obj) {
        if (!(obj instanceof org.apache.ambari.server.controller.MpackResponse)) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        org.apache.ambari.server.controller.MpackResponse MpackResponse = ((org.apache.ambari.server.controller.MpackResponse) (obj));
        return getId().equals(MpackResponse.getId());
    }

    public interface MpackResponseWrapper extends org.apache.ambari.server.controller.ApiModel {
        @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.controller.internal.MpackResourceProvider.RESPONSE_KEY)
        @java.lang.SuppressWarnings("unused")
        org.apache.ambari.server.controller.MpackResponse getMpackResponse();
    }
}