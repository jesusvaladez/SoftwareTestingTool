package org.apache.ambari.server.utils;
import org.apache.commons.lang.StringUtils;
public class ModuleVersion implements java.lang.Comparable<org.apache.ambari.server.utils.ModuleVersion> {
    private static final java.lang.String VERSION_WITH_HOTFIX_AND_BUILD_PATTERN = "^([0-9]+).([0-9]+).([0-9]+).([0-9]+)-h([0-9]+)-b([0-9]+)";

    private static final java.lang.String VERSION_WITH_BUILD_PATTERN = "^([0-9]+).([0-9]+).([0-9]+).([0-9]+)-b([0-9]+)";

    private static final java.util.regex.Pattern PATTERN_WITH_HOTFIX = java.util.regex.Pattern.compile(org.apache.ambari.server.utils.ModuleVersion.VERSION_WITH_HOTFIX_AND_BUILD_PATTERN);

    private static final java.util.regex.Pattern PATTERN_WITHOUT_HOTFIX = java.util.regex.Pattern.compile(org.apache.ambari.server.utils.ModuleVersion.VERSION_WITH_BUILD_PATTERN);

    private int apacheMajor;

    private int apacheMinor;

    private int internalMinor;

    private int internalMaint;

    private int hotfix;

    private int build;

    public ModuleVersion(int apacheMajor, int apacheMinor, int internalMinor, int internalMaint, int hotfix, int build) {
        this.apacheMajor = apacheMajor;
        this.apacheMinor = apacheMinor;
        this.internalMinor = internalMinor;
        this.internalMaint = internalMaint;
        this.hotfix = hotfix;
        this.build = build;
    }

    public static org.apache.ambari.server.utils.ModuleVersion parse(java.lang.String moduleVersion) {
        java.util.regex.Matcher versionMatcher = org.apache.ambari.server.utils.ModuleVersion.validateModuleVersion(moduleVersion);
        org.apache.ambari.server.utils.ModuleVersion result = null;
        if (versionMatcher.pattern().pattern().equals(org.apache.ambari.server.utils.ModuleVersion.VERSION_WITH_HOTFIX_AND_BUILD_PATTERN)) {
            result = new org.apache.ambari.server.utils.ModuleVersion(java.lang.Integer.parseInt(versionMatcher.group(1)), java.lang.Integer.parseInt(versionMatcher.group(2)), java.lang.Integer.parseInt(versionMatcher.group(3)), java.lang.Integer.parseInt(versionMatcher.group(4)), java.lang.Integer.parseInt(versionMatcher.group(5)), java.lang.Integer.parseInt(versionMatcher.group(6)));
        } else {
            result = new org.apache.ambari.server.utils.ModuleVersion(java.lang.Integer.parseInt(versionMatcher.group(1)), java.lang.Integer.parseInt(versionMatcher.group(2)), java.lang.Integer.parseInt(versionMatcher.group(3)), java.lang.Integer.parseInt(versionMatcher.group(4)), 0, java.lang.Integer.parseInt(versionMatcher.group(5)));
        }
        return result;
    }

    private static java.util.regex.Matcher validateModuleVersion(java.lang.String version) {
        if (org.apache.commons.lang.StringUtils.isEmpty(version)) {
            throw new java.lang.IllegalArgumentException("Module version can't be empty or null");
        }
        java.lang.String moduleVersion = org.apache.commons.lang.StringUtils.trim(version);
        java.util.regex.Matcher versionMatcher = org.apache.ambari.server.utils.ModuleVersion.PATTERN_WITH_HOTFIX.matcher(moduleVersion);
        if (!versionMatcher.find()) {
            versionMatcher = org.apache.ambari.server.utils.ModuleVersion.PATTERN_WITHOUT_HOTFIX.matcher(moduleVersion);
            if (!versionMatcher.find()) {
                throw new java.lang.IllegalArgumentException("Wrong format for module version, should be N.N.N.N-bN or N.N.N-hN-bN");
            }
        }
        return versionMatcher;
    }

    @java.lang.Override
    public int compareTo(org.apache.ambari.server.utils.ModuleVersion other) {
        int result = this.apacheMajor - other.apacheMajor;
        if (result == 0) {
            result = this.apacheMinor - other.apacheMinor;
            if (result == 0) {
                result = this.internalMinor - other.internalMinor;
                if (result == 0) {
                    result = this.internalMaint - other.internalMaint;
                    if (result == 0) {
                        result = this.hotfix - other.hotfix;
                        if (result == 0) {
                            result = this.build - other.build;
                        }
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

        org.apache.ambari.server.utils.ModuleVersion that = ((org.apache.ambari.server.utils.ModuleVersion) (o));
        if (apacheMajor != that.apacheMajor)
            return false;

        if (apacheMinor != that.apacheMinor)
            return false;

        if (build != that.build)
            return false;

        if (hotfix != that.hotfix)
            return false;

        if (internalMaint != that.internalMaint)
            return false;

        if (internalMinor != that.internalMinor)
            return false;

        return true;
    }

    @java.lang.Override
    public int hashCode() {
        int result = apacheMajor;
        result = (31 * result) + apacheMinor;
        result = (31 * result) + internalMinor;
        result = (31 * result) + internalMaint;
        result = (31 * result) + hotfix;
        result = (31 * result) + build;
        return result;
    }
}