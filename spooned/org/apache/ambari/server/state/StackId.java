package org.apache.ambari.server.state;
public class StackId implements java.lang.Comparable<org.apache.ambari.server.state.StackId> {
    private static final java.lang.String NAME_SEPARATOR = "-";

    private java.lang.String stackName;

    private java.lang.String stackVersion;

    public StackId() {
        stackName = "";
        stackVersion = "";
    }

    public StackId(java.lang.String stackId) {
        parseStackIdHelper(this, stackId);
    }

    public StackId(org.apache.ambari.server.state.StackInfo stackInfo) {
        stackName = stackInfo.getName();
        stackVersion = stackInfo.getVersion();
    }

    public StackId(java.lang.String stackName, java.lang.String stackVersion) {
        this((stackName + org.apache.ambari.server.state.StackId.NAME_SEPARATOR) + stackVersion);
    }

    public StackId(org.apache.ambari.server.orm.entities.StackEntity stackEntity) {
        this(stackEntity.getStackName(), stackEntity.getStackVersion());
    }

    public java.lang.String getStackName() {
        return stackName;
    }

    public java.lang.String getStackVersion() {
        return stackVersion;
    }

    public java.lang.String getStackId() {
        if (stackName.isEmpty() && stackVersion.isEmpty()) {
            return "";
        }
        return (stackName + org.apache.ambari.server.state.StackId.NAME_SEPARATOR) + stackVersion;
    }

    public void setStackId(java.lang.String stackId) {
        parseStackIdHelper(this, stackId);
    }

    @java.lang.Override
    public boolean equals(java.lang.Object object) {
        if (!(object instanceof org.apache.ambari.server.state.StackId)) {
            return false;
        }
        if (this == object) {
            return true;
        }
        org.apache.ambari.server.state.StackId s = ((org.apache.ambari.server.state.StackId) (object));
        return stackName.equals(s.stackName) && stackVersion.equals(s.stackVersion);
    }

    @java.lang.Override
    public int hashCode() {
        int result = (stackName != null) ? stackName.hashCode() : 0;
        result = (31 * result) + (stackVersion != null ? stackVersion.hashCode() : 0);
        return result;
    }

    @java.lang.Override
    public int compareTo(org.apache.ambari.server.state.StackId other) {
        if (this == other) {
            return 0;
        }
        if (other == null) {
            throw new java.lang.RuntimeException("Cannot compare with a null value.");
        }
        int returnValue = getStackName().compareTo(other.getStackName());
        if (returnValue == 0) {
            returnValue = org.apache.ambari.server.utils.VersionUtils.compareVersions(getStackVersion(), other.getStackVersion());
        } else {
            throw new java.lang.RuntimeException("StackId with different names cannot be compared.");
        }
        return returnValue;
    }

    @java.lang.Override
    public java.lang.String toString() {
        return getStackId();
    }

    private void parseStackIdHelper(org.apache.ambari.server.state.StackId stackVersion, java.lang.String stackId) {
        if ((stackId == null) || stackId.isEmpty()) {
            stackVersion.stackName = "";
            stackVersion.stackVersion = "";
            return;
        }
        int pos = stackId.indexOf('-');
        if ((pos == (-1)) || (stackId.length() <= (pos + 1))) {
            throw new java.lang.RuntimeException(("Could not parse invalid Stack Id" + ", stackId=") + stackId);
        }
        stackVersion.stackName = stackId.substring(0, pos);
        stackVersion.stackVersion = stackId.substring(pos + 1);
    }
}