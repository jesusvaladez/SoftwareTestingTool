package org.apache.ambari.server.state;
public class OperatingSystemInfo {
    @java.lang.Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = (prime * result) + (osType == null ? 0 : osType.hashCode());
        return result;
    }

    @java.lang.Override
    public boolean equals(java.lang.Object obj) {
        if (this == obj)
            return true;

        if (obj == null)
            return false;

        if (getClass() != obj.getClass())
            return false;

        org.apache.ambari.server.state.OperatingSystemInfo other = ((org.apache.ambari.server.state.OperatingSystemInfo) (obj));
        if (osType == null) {
            if (other.osType != null)
                return false;

        } else if (!osType.equals(other.osType))
            return false;

        return true;
    }

    private java.lang.String osType;

    public OperatingSystemInfo(java.lang.String osType) {
        setOsType(osType);
    }

    public java.lang.String getOsType() {
        return osType;
    }

    public void setOsType(java.lang.String osType) {
        this.osType = osType;
    }

    public org.apache.ambari.server.controller.OperatingSystemResponse convertToResponse() {
        return new org.apache.ambari.server.controller.OperatingSystemResponse(getOsType());
    }
}