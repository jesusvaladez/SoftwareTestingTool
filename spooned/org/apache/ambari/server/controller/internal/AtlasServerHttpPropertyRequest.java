package org.apache.ambari.server.controller.internal;
public class AtlasServerHttpPropertyRequest extends org.apache.ambari.server.controller.internal.JsonHttpPropertyRequest {
    private static final java.lang.String PROPERTY_ENABLE_TLS = "atlas.enableTLS";

    private static final java.lang.String PROPERTY_SERVER_HTTPS_PORT = "atlas.server.https.port";

    private static final java.lang.String PROPERTY_SERVER_HTTP_PORT = "atlas.server.http.port";

    private static final java.lang.String CONFIG_APPLICATION_PROPERTIES = "application-properties";

    private static final java.lang.String URL_TEMPLATE = "%s://%s:%s/api/atlas/admin/status";

    private static final java.util.Map<java.lang.String, java.lang.String> PROPERTY_MAPPINGS = java.util.Collections.singletonMap("Status", "HostRoles/ha_state");

    public AtlasServerHttpPropertyRequest() {
        super(org.apache.ambari.server.controller.internal.AtlasServerHttpPropertyRequest.PROPERTY_MAPPINGS);
    }

    @java.lang.Override
    public java.lang.String getUrl(org.apache.ambari.server.state.Cluster cluster, java.lang.String hostName) throws org.apache.ambari.server.controller.spi.SystemException {
        java.util.Map<java.lang.String, java.lang.String> atlasConfig = cluster.getDesiredConfigByType(org.apache.ambari.server.controller.internal.AtlasServerHttpPropertyRequest.CONFIG_APPLICATION_PROPERTIES).getProperties();
        boolean useHttps = java.lang.Boolean.parseBoolean(getConfigValue(atlasConfig, org.apache.ambari.server.controller.internal.AtlasServerHttpPropertyRequest.PROPERTY_ENABLE_TLS, "false"));
        java.lang.String port = (useHttps) ? getConfigValue(atlasConfig, org.apache.ambari.server.controller.internal.AtlasServerHttpPropertyRequest.PROPERTY_SERVER_HTTPS_PORT, "21443") : getConfigValue(atlasConfig, org.apache.ambari.server.controller.internal.AtlasServerHttpPropertyRequest.PROPERTY_SERVER_HTTP_PORT, "21000");
        return java.lang.String.format(org.apache.ambari.server.controller.internal.AtlasServerHttpPropertyRequest.URL_TEMPLATE, useHttps ? "https" : "http", hostName, port);
    }

    private java.lang.String getConfigValue(java.util.Map<java.lang.String, java.lang.String> atlasConfig, java.lang.String property, java.lang.String defaultValue) {
        return atlasConfig.containsKey(property) ? atlasConfig.get(property) : defaultValue;
    }
}