package org.apache.ambari.server.utils;
import org.apache.commons.lang.StringUtils;
public class MpackVersion implements java.lang.Comparable<org.apache.ambari.server.utils.MpackVersion> {
    private static final java.lang.String VERSION_WITH_HOTFIX_AND_BUILD_PATTERN = "^([0-9]+).([0-9]+).([0-9]+)-h([0-9]+)-b([0-9]+)";

    private static final java.lang.String VERSION_WITH_BUILD_PATTERN = "^([0-9]+).([0-9]+).([0-9]+)-b([0-9]+)";

    private static final java.lang.String LEGACY_STACK_VERSION_PATTERN = "^([0-9]+).([0-9]+).([0-9]+).([0-9]+)-([0-9]+)";

    private static final java.lang.String FORMAT_VERSION_PATTERN = "^([0-9]+)(\\.)?([0-9]+)?(\\.)?([0-9]+)?(-h)?([0-9]+)?(-b)?([0-9]+)?";

    private static final java.util.regex.Pattern PATTERN_WITH_HOTFIX = java.util.regex.Pattern.compile(org.apache.ambari.server.utils.MpackVersion.VERSION_WITH_HOTFIX_AND_BUILD_PATTERN);

    private static final java.util.regex.Pattern PATTERN_LEGACY_STACK_VERSION = java.util.regex.Pattern.compile(org.apache.ambari.server.utils.MpackVersion.LEGACY_STACK_VERSION_PATTERN);

    private static final java.util.regex.Pattern PATTERN_WITHOUT_HOTFIX = java.util.regex.Pattern.compile(org.apache.ambari.server.utils.MpackVersion.VERSION_WITH_BUILD_PATTERN);

    private static final java.util.regex.Pattern PATTERN_FORMAT_VERSION = java.util.regex.Pattern.compile(org.apache.ambari.server.utils.MpackVersion.FORMAT_VERSION_PATTERN);

    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.utils.MpackVersion.class);

    private int major;

    private int minor;

    private int maint;

    private int hotfix;

    private int build;

    public MpackVersion(int major, int minor, int maint, int hotfix, int build) {
        this.major = major;
        this.minor = minor;
        this.maint = maint;
        this.hotfix = hotfix;
        this.build = build;
    }

    public static org.apache.ambari.server.utils.MpackVersion parse(java.lang.String mpackVersion) {
        return org.apache.ambari.server.utils.MpackVersion.parse(mpackVersion, true);
    }

    public static org.apache.ambari.server.utils.MpackVersion parse(java.lang.String mpackVersion, boolean strict) {
        if (!strict) {
            mpackVersion = org.apache.ambari.server.utils.MpackVersion.format(mpackVersion);
        }
        java.util.regex.Matcher versionMatcher = org.apache.ambari.server.utils.MpackVersion.validateMpackVersion(mpackVersion);
        if (versionMatcher.pattern().pattern().equals(org.apache.ambari.server.utils.MpackVersion.VERSION_WITH_BUILD_PATTERN)) {
            return new org.apache.ambari.server.utils.MpackVersion(java.lang.Integer.parseInt(versionMatcher.group(1)), java.lang.Integer.parseInt(versionMatcher.group(2)), java.lang.Integer.parseInt(versionMatcher.group(3)), 0, java.lang.Integer.parseInt(versionMatcher.group(4)));
        } else if (versionMatcher.pattern().pattern().equals(org.apache.ambari.server.utils.MpackVersion.VERSION_WITH_HOTFIX_AND_BUILD_PATTERN)) {
            return new org.apache.ambari.server.utils.MpackVersion(java.lang.Integer.parseInt(versionMatcher.group(1)), java.lang.Integer.parseInt(versionMatcher.group(2)), java.lang.Integer.parseInt(versionMatcher.group(3)), java.lang.Integer.parseInt(versionMatcher.group(4)), java.lang.Integer.parseInt(versionMatcher.group(5)));
        } else {
            throw new java.lang.IllegalArgumentException("Wrong format for mpack version");
        }
    }

    public static java.lang.String format(java.lang.String mpackVersion) {
        java.util.regex.Matcher m = org.apache.ambari.server.utils.MpackVersion.PATTERN_FORMAT_VERSION.matcher(mpackVersion);
        if (m.matches()) {
            java.lang.String majorVersion = m.group(1);
            java.lang.String minorVersion = m.group(3);
            java.lang.String maintVersion = m.group(5);
            java.lang.String hotfixNum = m.group(7);
            java.lang.String buildNum = m.group(9);
            if ((hotfixNum != null) || (buildNum != null)) {
                if ((minorVersion == null) || (maintVersion == null)) {
                    throw new java.lang.IllegalArgumentException("Wrong format for mpack version");
                }
            }
            minorVersion = (minorVersion != null) ? minorVersion : "0";
            maintVersion = (maintVersion != null) ? maintVersion : "0";
            hotfixNum = (hotfixNum != null) ? hotfixNum : "0";
            buildNum = (buildNum != null) ? buildNum : "0";
            java.lang.String formattedMpackVersion = java.lang.String.format("%s.%s.%s-h%s-b%s", majorVersion, minorVersion, maintVersion, hotfixNum, buildNum);
            return formattedMpackVersion;
        } else {
            throw new java.lang.IllegalArgumentException("Wrong format for mpack version");
        }
    }

    public static org.apache.ambari.server.utils.MpackVersion parseStackVersion(java.lang.String stackVersion) {
        java.util.regex.Matcher versionMatcher = org.apache.ambari.server.utils.MpackVersion.validateStackVersion(stackVersion);
        if (versionMatcher.pattern().pattern().equals(org.apache.ambari.server.utils.MpackVersion.LEGACY_STACK_VERSION_PATTERN)) {
            return new org.apache.ambari.server.utils.MpackVersion(java.lang.Integer.parseInt(versionMatcher.group(1)), java.lang.Integer.parseInt(versionMatcher.group(2)), java.lang.Integer.parseInt(versionMatcher.group(3)), java.lang.Integer.parseInt(versionMatcher.group(4)), java.lang.Integer.parseInt(versionMatcher.group(5)));
        } else {
            throw new java.lang.IllegalArgumentException("Wrong format for mpack version");
        }
    }

    private static java.util.regex.Matcher validateStackVersion(java.lang.String version) {
        if (org.apache.commons.lang.StringUtils.isEmpty(version)) {
            throw new java.lang.IllegalArgumentException("Stack version can't be empty or null");
        }
        java.lang.String stackVersion = org.apache.commons.lang.StringUtils.trim(version);
        java.util.regex.Matcher versionMatcher = org.apache.ambari.server.utils.MpackVersion.PATTERN_LEGACY_STACK_VERSION.matcher(stackVersion);
        if (!versionMatcher.find()) {
            throw new java.lang.IllegalArgumentException("Wrong format for stack version, should be N.N.N.N-N or N.N.N-hN-bN");
        }
        return versionMatcher;
    }

    private static java.util.regex.Matcher validateMpackVersion(java.lang.String version) {
        if (org.apache.commons.lang.StringUtils.isEmpty(version)) {
            throw new java.lang.IllegalArgumentException("Mpack version can't be empty or null");
        }
        java.lang.String mpackVersion = org.apache.commons.lang.StringUtils.trim(version);
        java.util.regex.Matcher versionMatcher = org.apache.ambari.server.utils.MpackVersion.PATTERN_WITH_HOTFIX.matcher(mpackVersion);
        if (!versionMatcher.find()) {
            versionMatcher = org.apache.ambari.server.utils.MpackVersion.PATTERN_WITHOUT_HOTFIX.matcher(mpackVersion);
            if (!versionMatcher.find()) {
                throw new java.lang.IllegalArgumentException("Wrong format for mpack version, should be N.N.N-bN or N.N.N-hN-bN");
            }
        }
        return versionMatcher;
    }

    @java.lang.Override
    public int compareTo(org.apache.ambari.server.utils.MpackVersion other) {
        int result = this.major - other.major;
        if (result == 0) {
            result = this.minor - other.minor;
            if (result == 0) {
                result = this.maint - other.maint;
                if (result == 0) {
                    result = this.hotfix - other.hotfix;
                    if (result == 0) {
                        result = this.build - other.build;
                    }
                }
            }
        }
        return result > 0 ? 1 : result < 0 ? -1 : 0;
    }

    @java.lang.Override
    public boolean equals(java.lang.Object o) {
        if (this == o)
            return true;

        if ((o == null) || (getClass() != o.getClass()))
            return false;

        org.apache.ambari.server.utils.MpackVersion that = ((org.apache.ambari.server.utils.MpackVersion) (o));
        if (build != that.build)
            return false;

        if (hotfix != that.hotfix)
            return false;

        if (maint != that.maint)
            return false;

        if (major != that.major)
            return false;

        if (minor != that.minor)
            return false;

        return true;
    }

    @java.lang.Override
    public int hashCode() {
        int result = major;
        result = (31 * result) + minor;
        result = (31 * result) + maint;
        result = (31 * result) + hotfix;
        result = (31 * result) + build;
        return result;
    }
}