package org.apache.ambari.server.state;
public class ExtensionId implements java.lang.Comparable<org.apache.ambari.server.state.ExtensionId> {
    private static final java.lang.String NAME_SEPARATOR = "-";

    private java.lang.String extensionName;

    private java.lang.String extensionVersion;

    public ExtensionId() {
        extensionName = "";
        extensionVersion = "";
    }

    public ExtensionId(java.lang.String extensionId) {
        parseExtensionIdHelper(this, extensionId);
    }

    public ExtensionId(org.apache.ambari.server.state.ExtensionInfo extension) {
        extensionName = extension.getName();
        extensionVersion = extension.getVersion();
    }

    public ExtensionId(java.lang.String extensionName, java.lang.String extensionVersion) {
        this((extensionName + org.apache.ambari.server.state.ExtensionId.NAME_SEPARATOR) + extensionVersion);
    }

    public ExtensionId(org.apache.ambari.server.orm.entities.ExtensionEntity entity) {
        this(entity.getExtensionName(), entity.getExtensionVersion());
    }

    public java.lang.String getExtensionName() {
        return extensionName;
    }

    public java.lang.String getExtensionVersion() {
        return extensionVersion;
    }

    public java.lang.String getExtensionId() {
        if (extensionName.isEmpty() && extensionVersion.isEmpty()) {
            return "";
        }
        return (extensionName + org.apache.ambari.server.state.ExtensionId.NAME_SEPARATOR) + extensionVersion;
    }

    public void setExtensionId(java.lang.String extensionId) {
        parseExtensionIdHelper(this, extensionId);
    }

    @java.lang.Override
    public boolean equals(java.lang.Object object) {
        if (!(object instanceof org.apache.ambari.server.state.ExtensionId)) {
            return false;
        }
        if (this == object) {
            return true;
        }
        org.apache.ambari.server.state.ExtensionId s = ((org.apache.ambari.server.state.ExtensionId) (object));
        return extensionName.equals(s.extensionName) && extensionVersion.equals(s.extensionVersion);
    }

    @java.lang.Override
    public int hashCode() {
        int result = (extensionName != null) ? extensionName.hashCode() : 0;
        result = (31 * result) + (extensionVersion != null ? extensionVersion.hashCode() : 0);
        return result;
    }

    @java.lang.Override
    public int compareTo(org.apache.ambari.server.state.ExtensionId other) {
        if (this == other) {
            return 0;
        }
        if (other == null) {
            throw new java.lang.RuntimeException("Cannot compare with a null value.");
        }
        int returnValue = getExtensionName().compareTo(other.getExtensionName());
        if (returnValue == 0) {
            returnValue = org.apache.ambari.server.utils.VersionUtils.compareVersions(getExtensionVersion(), other.getExtensionVersion());
        } else {
            throw new java.lang.RuntimeException("ExtensionId with different names cannot be compared.");
        }
        return returnValue;
    }

    @java.lang.Override
    public java.lang.String toString() {
        return getExtensionId();
    }

    private void parseExtensionIdHelper(org.apache.ambari.server.state.ExtensionId extensionVersion, java.lang.String extensionId) {
        if ((extensionId == null) || extensionId.isEmpty()) {
            extensionVersion.extensionName = "";
            extensionVersion.extensionVersion = "";
            return;
        }
        int pos = extensionId.indexOf('-');
        if ((pos == (-1)) || (extensionId.length() <= (pos + 1))) {
            throw new java.lang.RuntimeException(("Could not parse invalid Extension Id" + ", extensionId=") + extensionId);
        }
        extensionVersion.extensionName = extensionId.substring(0, pos);
        extensionVersion.extensionVersion = extensionId.substring(pos + 1);
    }
}