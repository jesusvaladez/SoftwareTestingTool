package org.apache.ambari.server.state.repository;
import org.apache.commons.lang.StringUtils;
public class DefaultStackVersion implements org.apache.ambari.spi.stack.StackReleaseVersion {
    @java.lang.Override
    public java.lang.String getFullVersion(org.apache.ambari.spi.stack.StackReleaseInfo info) {
        java.lang.StringBuilder sb = new java.lang.StringBuilder(info.getVersion());
        if (org.apache.commons.lang.StringUtils.isNotBlank(info.getBuild())) {
            sb.append('-').append(org.apache.commons.lang.StringUtils.trim(info.getBuild()));
        }
        return sb.toString();
    }

    @java.lang.Override
    public java.util.Comparator<org.apache.ambari.spi.stack.StackReleaseInfo> getComparator() {
        return new java.util.Comparator<org.apache.ambari.spi.stack.StackReleaseInfo>() {
            @java.lang.Override
            public int compare(org.apache.ambari.spi.stack.StackReleaseInfo o1, org.apache.ambari.spi.stack.StackReleaseInfo o2) {
                return org.apache.ambari.server.utils.VersionUtils.compareVersionsWithBuild(getFullVersion(o1), getFullVersion(o2), 4);
            }
        };
    }

    @java.lang.Override
    public org.apache.ambari.spi.stack.StackReleaseInfo parse(java.lang.String versionString) {
        java.lang.String version = "0";
        java.lang.String build = "0";
        java.lang.String[] parts = org.apache.commons.lang.StringUtils.split(versionString, '-');
        if (1 == parts.length) {
            version = parts[0];
        } else if (parts.length > 1) {
            version = parts[0];
            build = parts[1];
        }
        return new org.apache.ambari.spi.stack.StackReleaseInfo(version, "0", build);
    }
}