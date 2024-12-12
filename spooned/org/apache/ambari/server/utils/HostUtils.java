package org.apache.ambari.server.utils;
public class HostUtils {
    private static final java.util.regex.Pattern REGEX_VALID_HOSTNAME = java.util.regex.Pattern.compile("^(?:(?:[a-zA-Z0-9]|[a-zA-Z0-9][a-zA-Z0-9\\-]*[a-zA-Z0-9])\\.)*(?:[A-Za-z0-9]|[A-Za-z0-9][A-Za-z0-9\\-]*[A-Za-z0-9])$");

    public static boolean isValidHostname(java.lang.String hostname) {
        return (hostname != null) && org.apache.ambari.server.utils.HostUtils.REGEX_VALID_HOSTNAME.matcher(hostname).matches();
    }
}