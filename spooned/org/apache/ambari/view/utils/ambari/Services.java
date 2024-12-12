package org.apache.ambari.view.utils.ambari;
import org.apache.commons.lang.StringUtils;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
public class Services {
    public static final java.lang.String HTTPS_ONLY = "HTTPS_ONLY";

    public static final java.lang.String HTTP_ONLY = "HTTP_ONLY";

    public static final java.lang.String YARN_SITE = "yarn-site";

    public static final java.lang.String YARN_HTTP_POLICY = "yarn.http.policy";

    public static final java.lang.String YARN_RESOURCEMANAGER_HA_ENABLED = "yarn.resourcemanager.ha.enabled";

    private static final java.lang.String YARN_RESOURCEMANAGER_HTTPS_KEY = "yarn.resourcemanager.webapp.https.address";

    private static final java.lang.String YARN_RESOURCEMANAGER_HTTP_KEY = "yarn.resourcemanager.webapp.address";

    private static final java.lang.String YARN_RESOURCEMANAGER_HA_RM_IDS_KEY = "yarn.resourcemanager.ha.rm-ids";

    private static final java.lang.String YARN_RESOURCEMANAGER_HTTP_HA_PARTIAL_KEY = "yarn.resourcemanager.webapp.address.";

    private static final java.lang.String YARN_RESOURCEMANAGER_HTTPS_HA_PARTIAL_KEY = "yarn.resourcemanager.webapp.https.address.";

    private static final java.lang.String YARN_RESOURCEMANAGER_HOSTNAME_KEY = "yarn.resourcemanager.hostname";

    private static final java.lang.String YARN_RESOURCEMANAGER_HOSTNAME_PARTIAL_KEY = org.apache.ambari.view.utils.ambari.Services.YARN_RESOURCEMANAGER_HOSTNAME_KEY + ".";

    private static final java.lang.String YARN_RESOURCEMANAGER_DEFAULT_HTTP_PORT = "8088";

    private static final java.lang.String YARN_RESOURCEMANAGER_DEFAULT_HTTPS_PORT = "8090";

    private static final java.lang.String YARN_ATS_URL = "yarn.ats.url";

    private static final java.lang.String YARN_TIMELINE_WEBAPP_HTTP_ADDRESS_KEY = "yarn.timeline-service.webapp.address";

    private static final java.lang.String YARN_TIMELINE_WEBAPP_HTTPS_ADDRESS_KEY = "yarn.timeline-service.webapp.https.address";

    public static final java.lang.String RM_INFO_API_ENDPOINT = "/ws/v1/cluster/info";

    public static final java.lang.String TIMELINE_AUTH_TYPE_PROP_KEY = "timeline.http.auth.type";

    public static final java.lang.String HADOOP_HTTP_AUTH_TYPE_KEY = "hadoop.http.auth.type";

    private final org.apache.ambari.view.utils.ambari.AmbariApi ambariApi;

    private org.apache.ambari.view.ViewContext context;

    protected static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.view.utils.ambari.Services.class);

    public Services(org.apache.ambari.view.utils.ambari.AmbariApi ambariApi, org.apache.ambari.view.ViewContext context) {
        this.ambariApi = ambariApi;
        this.context = context;
    }

    public java.lang.String getRMUrl() {
        java.lang.String url;
        if (context.getCluster() != null) {
            url = getRMUrlFromClusterConfig();
        } else {
            url = getRmUrlFromCustomConfig();
        }
        return removeTrailingSlash(url);
    }

    private java.lang.String getRMUrlFromClusterConfig() {
        java.lang.String url;
        java.lang.String haEnabled = getYarnConfig(org.apache.ambari.view.utils.ambari.Services.YARN_RESOURCEMANAGER_HA_ENABLED);
        java.lang.String httpPolicy = getYarnConfig(org.apache.ambari.view.utils.ambari.Services.YARN_HTTP_POLICY);
        if (!(org.apache.ambari.view.utils.ambari.Services.HTTP_ONLY.equals(httpPolicy) || org.apache.ambari.view.utils.ambari.Services.HTTPS_ONLY.equals(httpPolicy))) {
            org.apache.ambari.view.utils.ambari.Services.LOG.error(java.lang.String.format("RA030 Unknown value %s of yarn-site/yarn.http.policy. HTTP_ONLY assumed.", httpPolicy));
            httpPolicy = org.apache.ambari.view.utils.ambari.Services.HTTP_ONLY;
        }
        if ((haEnabled != null) && haEnabled.equals("true")) {
            java.lang.String[] urls = getRMHAUrls(httpPolicy);
            url = getActiveRMUrl(urls);
        } else {
            url = (httpPolicy.equals(org.apache.ambari.view.utils.ambari.Services.HTTPS_ONLY)) ? getYarnConfig(org.apache.ambari.view.utils.ambari.Services.YARN_RESOURCEMANAGER_HTTPS_KEY) : getYarnConfig(org.apache.ambari.view.utils.ambari.Services.YARN_RESOURCEMANAGER_HTTP_KEY);
            if ((url == null) || url.isEmpty()) {
                url = (getYarnConfig(org.apache.ambari.view.utils.ambari.Services.YARN_RESOURCEMANAGER_HOSTNAME_KEY).trim() + ":") + getDefaultRMPort(httpPolicy);
            }
            url = org.apache.ambari.view.utils.ambari.Services.addProtocolIfMissing(url, getProtocol(httpPolicy));
        }
        return url;
    }

    private java.lang.String[] getRMHAUrls(java.lang.String httpPolicy) {
        java.lang.String haRmIds = getYarnConfig(org.apache.ambari.view.utils.ambari.Services.YARN_RESOURCEMANAGER_HA_RM_IDS_KEY);
        java.lang.String[] ids = haRmIds.split(",");
        int index = 0;
        java.lang.String[] urls = new java.lang.String[ids.length];
        for (java.lang.String id : ids) {
            java.lang.String url = (org.apache.ambari.view.utils.ambari.Services.HTTPS_ONLY.equals(httpPolicy)) ? getYarnConfig(org.apache.ambari.view.utils.ambari.Services.YARN_RESOURCEMANAGER_HTTPS_HA_PARTIAL_KEY + id) : getYarnConfig(org.apache.ambari.view.utils.ambari.Services.YARN_RESOURCEMANAGER_HTTP_HA_PARTIAL_KEY + id);
            if ((url == null) || url.isEmpty()) {
                url = (getYarnConfig(org.apache.ambari.view.utils.ambari.Services.YARN_RESOURCEMANAGER_HOSTNAME_PARTIAL_KEY + id).trim() + ":") + getDefaultRMPort(httpPolicy);
            }
            urls[index++] = org.apache.ambari.view.utils.ambari.Services.addProtocolIfMissing(url.trim(), getProtocol(httpPolicy));
        }
        return urls;
    }

    private java.lang.String getRmUrlFromCustomConfig() {
        java.lang.String resourceManagerUrls = context.getProperties().get("yarn.resourcemanager.url");
        if (!org.apache.commons.lang.StringUtils.isEmpty(resourceManagerUrls)) {
            java.lang.String[] urls = resourceManagerUrls.split(",");
            if (!org.apache.ambari.view.utils.ambari.Services.hasProtocol(urls)) {
                throw new org.apache.ambari.view.utils.ambari.AmbariApiException("RA070 View is not cluster associated. All Resource Manager URL should contain protocol.");
            }
            return getActiveRMUrl(urls);
        } else {
            throw new org.apache.ambari.view.utils.ambari.AmbariApiException("RA070 View is not cluster associated. 'YARN ResourceManager URL' should be provided");
        }
    }

    private java.lang.String removeTrailingSlash(java.lang.String url) {
        if (url.endsWith("/")) {
            url = url.substring(0, url.length() - 1);
        }
        return url;
    }

    private java.lang.String getActiveRMUrl(java.lang.String[] urls) {
        if (urls.length == 1)
            return urls[0].trim();
        else {
            for (java.lang.String url : urls) {
                url = url.trim();
                if (isActiveUrl(url))
                    return url;

            }
        }
        org.apache.ambari.view.utils.ambari.Services.LOG.error("All ResourceManagers are not accessible or none seem to be active.");
        throw new org.apache.ambari.view.utils.ambari.AmbariApiException("RA110 All ResourceManagers are not accessible or none seem to be active.");
    }

    private boolean isActiveUrl(java.lang.String url) {
        java.io.InputStream inputStream = null;
        try {
            inputStream = context.getURLStreamProvider().readFrom(url + org.apache.ambari.view.utils.ambari.Services.RM_INFO_API_ENDPOINT, "GET", ((java.lang.String) (null)), new java.util.HashMap<java.lang.String, java.lang.String>());
            java.lang.String response = org.apache.commons.io.IOUtils.toString(inputStream);
            java.lang.String haState = getHAStateFromRMResponse(response);
            if (org.apache.commons.lang.StringUtils.isNotEmpty(haState) && "ACTIVE".equals(haState))
                return true;

        } catch (java.io.IOException e) {
            org.apache.ambari.view.utils.ambari.Services.LOG.error("Resource Manager : %s is not accessible. This cannot be a active RM. Returning false.");
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (java.io.IOException e) {
                }
            }
        }
        return false;
    }

    private java.lang.String getHAStateFromRMResponse(java.lang.String response) {
        org.json.simple.JSONObject jsonObject = ((org.json.simple.JSONObject) (org.json.simple.JSONValue.parse(response)));
        org.json.simple.JSONObject clusterInfo = ((org.json.simple.JSONObject) (jsonObject.get("clusterInfo")));
        return ((java.lang.String) (clusterInfo.get("haState")));
    }

    public java.lang.String getWebHCatURL() {
        java.lang.String host = null;
        if (context.getCluster() != null) {
            java.util.List<java.lang.String> hiveServerHosts = context.getCluster().getHostsForServiceComponent("HIVE", "WEBHCAT_SERVER");
            if (!hiveServerHosts.isEmpty()) {
                host = hiveServerHosts.get(0);
                org.apache.ambari.view.utils.ambari.Services.LOG.info("WEBHCAT_SERVER component was found on host " + host);
            } else {
                org.apache.ambari.view.utils.ambari.Services.LOG.warn("No host was found with WEBHCAT_SERVER component. Using hive.host property to get hostname.");
            }
        }
        if (host == null) {
            host = context.getProperties().get("webhcat.hostname");
            if ((host == null) || host.isEmpty()) {
                throw new org.apache.ambari.view.utils.ambari.AmbariApiException("RA080 Can't determine WebHCat hostname neither by associated cluster nor by webhcat.hostname property.");
            }
        }
        java.lang.String port = context.getProperties().get("webhcat.port");
        if ((port == null) || port.isEmpty()) {
            throw new org.apache.ambari.view.utils.ambari.AmbariApiException("RA090 Can't determine WebHCat port neither by associated cluster nor by webhcat.port property.");
        }
        return java.lang.String.format("http://%s:%s/templeton/v1", host, port);
    }

    public java.lang.String getTimelineServerUrl() {
        java.lang.String url = (context.getCluster() != null) ? getATSUrlFromCluster() : getATSUrlFromCustom();
        return removeTrailingSlash(url);
    }

    private java.lang.String getATSUrlFromCustom() {
        java.lang.String atsUrl = context.getProperties().get(org.apache.ambari.view.utils.ambari.Services.YARN_ATS_URL);
        if (!org.apache.commons.lang.StringUtils.isEmpty(atsUrl)) {
            if (!org.apache.ambari.view.utils.ambari.Services.hasProtocol(atsUrl)) {
                throw new org.apache.ambari.view.utils.ambari.AmbariApiException("RA070 View is not cluster associated. Timeline Server URL should contain protocol.");
            }
            return atsUrl;
        } else {
            throw new org.apache.ambari.view.utils.ambari.AmbariApiException("RA070 View is not cluster associated. 'YARN Timeline Server URL' should be provided");
        }
    }

    public java.lang.String getYARNProtocol() {
        java.lang.String httpPolicy = getYarnConfig(org.apache.ambari.view.utils.ambari.Services.YARN_HTTP_POLICY);
        if (!(org.apache.ambari.view.utils.ambari.Services.HTTP_ONLY.equals(httpPolicy) || org.apache.ambari.view.utils.ambari.Services.HTTPS_ONLY.equals(httpPolicy))) {
            org.apache.ambari.view.utils.ambari.Services.LOG.error(java.lang.String.format("RA030 Unknown value %s of yarn-site/yarn.http.policy. HTTP_ONLY assumed.", httpPolicy));
            httpPolicy = org.apache.ambari.view.utils.ambari.Services.HTTP_ONLY;
        }
        return getProtocol(httpPolicy);
    }

    private java.lang.String getATSUrlFromCluster() {
        java.lang.String url;
        java.lang.String httpPolicy = getYarnConfig(org.apache.ambari.view.utils.ambari.Services.YARN_HTTP_POLICY);
        if (!(org.apache.ambari.view.utils.ambari.Services.HTTP_ONLY.equals(httpPolicy) || org.apache.ambari.view.utils.ambari.Services.HTTPS_ONLY.equals(httpPolicy))) {
            org.apache.ambari.view.utils.ambari.Services.LOG.error(java.lang.String.format("RA030 Unknown value %s of yarn-site/yarn.http.policy. HTTP_ONLY assumed.", httpPolicy));
            httpPolicy = org.apache.ambari.view.utils.ambari.Services.HTTP_ONLY;
        }
        if (httpPolicy.equals(org.apache.ambari.view.utils.ambari.Services.HTTPS_ONLY)) {
            url = getYarnConfig(org.apache.ambari.view.utils.ambari.Services.YARN_TIMELINE_WEBAPP_HTTPS_ADDRESS_KEY);
        } else {
            url = getYarnConfig(org.apache.ambari.view.utils.ambari.Services.YARN_TIMELINE_WEBAPP_HTTP_ADDRESS_KEY);
        }
        url = org.apache.ambari.view.utils.ambari.Services.addProtocolIfMissing(url, getProtocol(httpPolicy));
        return url;
    }

    public static java.lang.String addProtocolIfMissing(java.lang.String url, java.lang.String protocol) throws org.apache.ambari.view.utils.ambari.AmbariApiException {
        if (!org.apache.ambari.view.utils.ambari.Services.hasProtocol(url)) {
            url = (protocol + "://") + url;
        }
        return url;
    }

    public static boolean hasProtocol(java.lang.String[] urls) {
        for (java.lang.String url : urls) {
            if (!org.apache.ambari.view.utils.ambari.Services.hasProtocol(url))
                return false;

        }
        return true;
    }

    public static boolean hasProtocol(java.lang.String url) {
        return url.matches("^[^:]+://.*$");
    }

    private java.lang.String getProtocol(java.lang.String yarnHttpPolicyConfig) {
        return org.apache.ambari.view.utils.ambari.Services.HTTPS_ONLY.equals(yarnHttpPolicyConfig) ? "https" : "http";
    }

    private java.lang.String getYarnConfig(java.lang.String key) {
        return context.getCluster().getConfigurationValue(org.apache.ambari.view.utils.ambari.Services.YARN_SITE, key);
    }

    private java.lang.String getDefaultRMPort(java.lang.String yarnHttpPolicy) {
        return org.apache.ambari.view.utils.ambari.Services.HTTPS_ONLY.equals(yarnHttpPolicy) ? org.apache.ambari.view.utils.ambari.Services.YARN_RESOURCEMANAGER_DEFAULT_HTTPS_PORT : org.apache.ambari.view.utils.ambari.Services.YARN_RESOURCEMANAGER_DEFAULT_HTTP_PORT;
    }

    public java.lang.String getHadoopHttpWebAuthType() {
        return context.getProperties().get(org.apache.ambari.view.utils.ambari.Services.HADOOP_HTTP_AUTH_TYPE_KEY);
    }

    public java.lang.String getTimelineServerAuthType() {
        return context.getProperties().get(org.apache.ambari.view.utils.ambari.Services.TIMELINE_AUTH_TYPE_PROP_KEY);
    }
}