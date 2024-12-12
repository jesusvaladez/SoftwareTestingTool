package org.apache.ambari.spi.stack;
public interface StackReleaseVersion {
    java.lang.String getFullVersion(org.apache.ambari.spi.stack.StackReleaseInfo info);

    java.util.Comparator<org.apache.ambari.spi.stack.StackReleaseInfo> getComparator();

    org.apache.ambari.spi.stack.StackReleaseInfo parse(java.lang.String versionString);
}