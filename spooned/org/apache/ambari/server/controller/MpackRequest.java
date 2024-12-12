package org.apache.ambari.server.controller;
public class MpackRequest {
    private java.lang.Long id;

    private java.lang.Long registryId;

    private java.lang.String mpackName;

    private java.lang.String mpackVersion;

    private java.lang.String mpackUri;

    public MpackRequest(java.lang.Long id) {
        this.setId(id);
    }

    public MpackRequest() {
    }

    public java.lang.Long getId() {
        return id;
    }

    public void setId(java.lang.Long id) {
        this.id = id;
    }

    public java.lang.String getMpackName() {
        return mpackName;
    }

    public void setMpackName(java.lang.String mpackName) {
        this.mpackName = mpackName;
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

    public java.lang.Long getRegistryId() {
        return registryId;
    }

    public void setRegistryId(java.lang.Long registryId) {
        this.registryId = registryId;
    }

    @java.lang.Override
    public int hashCode() {
        int result;
        result = 31 + getId().hashCode();
        return result;
    }

    @java.lang.Override
    public boolean equals(java.lang.Object obj) {
        if (!(obj instanceof org.apache.ambari.server.controller.MpackRequest)) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        org.apache.ambari.server.controller.MpackRequest mpackRequest = ((org.apache.ambari.server.controller.MpackRequest) (obj));
        if (id == null) {
            if (mpackRequest.id != null) {
                return false;
            }
        } else if (!id.equals(mpackRequest.id)) {
            return false;
        }
        if (mpackName == null) {
            if (mpackRequest.mpackName != null) {
                return false;
            }
        } else if (!mpackName.equals(mpackRequest.mpackName)) {
            return false;
        }
        if (mpackUri == null) {
            if (mpackRequest.mpackUri != null) {
                return false;
            }
        } else if (!mpackUri.equals(mpackRequest.mpackUri)) {
            return false;
        }
        if (registryId == null) {
            if (mpackRequest.registryId != null) {
                return false;
            }
        } else if (!registryId.equals(mpackRequest.registryId)) {
            return false;
        }
        if (mpackVersion == null) {
            if (mpackRequest.mpackVersion != null) {
                return false;
            }
        } else if (!mpackVersion.equals(mpackRequest.mpackVersion)) {
            return false;
        }
        return true;
    }
}