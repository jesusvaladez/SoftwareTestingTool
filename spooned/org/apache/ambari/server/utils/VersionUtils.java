package org.apache.ambari.server.utils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
public class VersionUtils {
    public static final java.lang.String DEV_VERSION = "${ambariVersion}";

    public static int compareVersions(java.lang.String version1, java.lang.String version2, int maxLengthToCompare) throws java.lang.IllegalArgumentException {
        if (version1 == null) {
            throw new java.lang.IllegalArgumentException("version1 cannot be null");
        }
        if (version2 == null) {
            throw new java.lang.IllegalArgumentException("version2 cannot be null");
        }
        version1 = org.apache.commons.lang.StringUtils.trim(version1);
        version2 = org.apache.commons.lang.StringUtils.trim(version2);
        if (version1.indexOf('-') >= 0) {
            version1 = version1.substring(0, version1.indexOf('-'));
        }
        if (version2.indexOf('-') >= 0) {
            version2 = version2.substring(0, version2.indexOf('-'));
        }
        if (version1.isEmpty()) {
            throw new java.lang.IllegalArgumentException("version1 cannot be empty");
        }
        if (version2.isEmpty()) {
            throw new java.lang.IllegalArgumentException("version2 cannot be empty");
        }
        if (maxLengthToCompare < 0) {
            throw new java.lang.IllegalArgumentException("maxLengthToCompare cannot be less than 0");
        }
        if (org.apache.ambari.server.utils.VersionUtils.DEV_VERSION.equals(version1.trim())) {
            return 0;
        }
        java.lang.String pattern = "([0-9]+).([0-9]+).([0-9]+).?([0-9]+)?.*";
        java.lang.String[] version1Parts = version1.replaceAll(pattern, "$1.$2.$3.$4").split("\\.");
        java.lang.String[] version2Parts = version2.replaceAll(pattern, "$1.$2.$3.$4").split("\\.");
        int length = java.lang.Math.max(version1Parts.length, version2Parts.length);
        length = ((maxLengthToCompare == 0) || (maxLengthToCompare > length)) ? length : maxLengthToCompare;
        java.util.List<java.lang.Integer> stack1Parts = new java.util.ArrayList<>();
        java.util.List<java.lang.Integer> stack2Parts = new java.util.ArrayList<>();
        for (int i = 0; i < length; i++) {
            try {
                int stack1Part = (i < version1Parts.length) ? java.lang.Integer.parseInt(version1Parts[i]) : 0;
                stack1Parts.add(stack1Part);
            } catch (java.lang.NumberFormatException e) {
                stack1Parts.add(0);
            }
            try {
                int stack2Part = (i < version2Parts.length) ? java.lang.Integer.parseInt(version2Parts[i]) : 0;
                stack2Parts.add(stack2Part);
            } catch (java.lang.NumberFormatException e) {
                stack2Parts.add(0);
            }
        }
        length = java.lang.Math.max(stack1Parts.size(), stack2Parts.size());
        for (int i = 0; i < length; i++) {
            java.lang.Integer stack1Part = stack1Parts.get(i);
            java.lang.Integer stack2Part = stack2Parts.get(i);
            if (stack1Part < stack2Part) {
                return -1;
            }
            if (stack1Part > stack2Part) {
                return 1;
            }
        }
        return 0;
    }

    public static int compareVersions(java.lang.String version1, java.lang.String version2, boolean allowEmptyVersions) {
        if (allowEmptyVersions) {
            if ((version1 != null) && version1.equals(org.apache.ambari.server.utils.VersionUtils.DEV_VERSION)) {
                return 0;
            }
            if ((version1 == null) && (version2 == null)) {
                return 0;
            } else {
                if (version1 == null) {
                    return -1;
                }
                if (version2 == null) {
                    return 1;
                }
            }
            if (version1.isEmpty() && version2.isEmpty()) {
                return 0;
            } else {
                if (version1.isEmpty()) {
                    return -1;
                }
                if (version2.isEmpty()) {
                    return 1;
                }
            }
        }
        return org.apache.ambari.server.utils.VersionUtils.compareVersions(version1, version2, 0);
    }

    public static int compareVersions(java.lang.String version1, java.lang.String version2) {
        return org.apache.ambari.server.utils.VersionUtils.compareVersions(version1, version2, 0);
    }

    public static boolean areVersionsEqual(java.lang.String version1, java.lang.String version2, boolean allowEmptyVersions) {
        return 0 == org.apache.ambari.server.utils.VersionUtils.compareVersions(version1, version2, allowEmptyVersions);
    }

    public static java.lang.String getVersionSubstring(java.lang.String version) {
        java.lang.String[] versionParts = version.split("\\.");
        if (versionParts.length < 3) {
            throw new java.lang.IllegalArgumentException("Invalid version number");
        }
        return (((versionParts[0] + ".") + versionParts[1]) + ".") + versionParts[2];
    }

    public static int compareVersionsWithBuild(java.lang.String version1, java.lang.String version2, int places) {
        version1 = (null == version1) ? "0" : version1;
        version2 = (null == version2) ? "0" : version2;
        if (org.apache.commons.lang.StringUtils.equals(version1, version2)) {
            return 0;
        }
        int compare = org.apache.ambari.server.utils.VersionUtils.compareVersions(version1, version2, places);
        if (0 != compare) {
            return compare;
        }
        int v1 = 0;
        int v2 = 0;
        if (version1.indexOf('-') > (-1)) {
            v1 = org.apache.commons.lang.math.NumberUtils.toInt(version1.substring(version1.indexOf('-')), 0);
        }
        if (version2.indexOf('-') > (-1)) {
            v2 = org.apache.commons.lang.math.NumberUtils.toInt(version2.substring(version2.indexOf('-')), 0);
        }
        compare = v2 - v1;
        return java.lang.Integer.compare(compare, 0);
    }

    public static int compareTo(java.lang.Comparable v1, java.lang.Comparable v2) {
        return v1 == null ? v2 == null ? 0 : -1 : v2 == null ? 1 : v1.compareTo(v2);
    }
}