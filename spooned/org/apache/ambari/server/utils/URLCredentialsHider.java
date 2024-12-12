package org.apache.ambari.server.utils;
public class URLCredentialsHider {
    public static final java.lang.String INVALID_URL = "invalid_url";

    public static final java.lang.String HIDDEN_USER = "****";

    public static final java.lang.String HIDDEN_CREDENTIALS = (org.apache.ambari.server.utils.URLCredentialsHider.HIDDEN_USER + ":") + org.apache.ambari.server.utils.URLCredentialsHider.HIDDEN_USER;

    public static java.lang.String hideCredentials(java.lang.String urlString) {
        if (org.apache.commons.lang3.StringUtils.isEmpty(urlString)) {
            return urlString;
        }
        java.net.URL url;
        try {
            url = new java.net.URL(urlString);
        } catch (java.net.MalformedURLException e) {
            return org.apache.ambari.server.utils.URLCredentialsHider.INVALID_URL;
        }
        java.lang.String userInfo = url.getUserInfo();
        if (org.apache.commons.lang3.StringUtils.isNotEmpty(userInfo)) {
            if (userInfo.contains(":")) {
                return org.apache.commons.lang3.StringUtils.replaceOnce(urlString, userInfo, org.apache.ambari.server.utils.URLCredentialsHider.HIDDEN_CREDENTIALS);
            } else {
                return org.apache.commons.lang3.StringUtils.replaceOnce(urlString, userInfo, org.apache.ambari.server.utils.URLCredentialsHider.HIDDEN_USER);
            }
        }
        return urlString;
    }
}