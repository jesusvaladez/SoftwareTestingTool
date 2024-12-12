package org.apache.ambari.view.utils.hdfs;
import org.apache.hadoop.conf.Configuration;
public class ConfigurationBuilder {
    protected static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.view.utils.hdfs.ConfigurationBuilder.class);

    public static final java.lang.String CORE_SITE = "core-site";

    public static final java.lang.String HDFS_SITE = "hdfs-site";

    public static final java.lang.String DEFAULT_FS_INSTANCE_PROPERTY = "webhdfs.url";

    public static final java.lang.String DEFAULT_FS_CLUSTER_PROPERTY = "fs.defaultFS";

    public static final java.lang.String NAMESERVICES_INSTANCE_PROPERTY = "webhdfs.nameservices";

    public static final java.lang.String NAMESERVICES_CLUSTER_PROPERTY = "dfs.nameservices";

    public static final java.lang.String HA_NAMENODES_INSTANCE_PROPERTY = "webhdfs.ha.namenodes.list";

    public static final java.lang.String HA_NAMENODES_CLUSTER_PROPERTY = "dfs.ha.namenodes.%s";

    public static final java.lang.String NAMENODE_RPC_NN_INSTANCE_PROPERTY = "webhdfs.ha.namenode.rpc-address.list";

    public static final java.lang.String NAMENODE_RPC_NN_CLUSTER_PROPERTY = "dfs.namenode.rpc-address.%s.%s";

    public static final java.lang.String NAMENODE_HTTP_NN_INSTANCE_PROPERTY = "webhdfs.ha.namenode.http-address.list";

    public static final java.lang.String NAMENODE_HTTP_NN_CLUSTER_PROPERTY = "dfs.namenode.http-address.%s.%s";

    public static final java.lang.String NAMENODE_HTTPS_NN_INSTANCE_PROPERTY = "webhdfs.ha.namenode.https-address.list";

    public static final java.lang.String NAMENODE_HTTPS_NN_CLUSTER_PROPERTY = "dfs.namenode.https-address.%s.%s";

    public static final java.lang.String FAILOVER_PROXY_PROVIDER_INSTANCE_PROPERTY = "webhdfs.client.failover.proxy.provider";

    public static final java.lang.String FAILOVER_PROXY_PROVIDER_CLUSTER_PROPERTY = "dfs.client.failover.proxy.provider.%s";

    public static final java.lang.String UMASK_CLUSTER_PROPERTY = "fs.permissions.umask-mode";

    public static final java.lang.String UMASK_INSTANCE_PROPERTY = "hdfs.umask-mode";

    public static final java.lang.String DFS_WEBHDFS_ENABLED = "dfs.webhdfs.enabled";

    public static final java.lang.String DFS_HTTP_POLICY = "dfs.http.policy";

    public static final java.lang.String DFS_HTTP_POLICY_HTTPS_ONLY = "HTTPS_ONLY";

    public static final java.lang.String DFS_NAMENODE_HTTP_ADDERSS = "dfs.namenode.http-address";

    public static final java.lang.String DFS_NAMENODE_HTTPS_ADDERSS = "dfs.namenode.https-address";

    protected org.apache.hadoop.conf.Configuration conf = new org.apache.hadoop.conf.Configuration();

    private org.apache.ambari.view.ViewContext context;

    private org.apache.ambari.view.utils.hdfs.AuthConfigurationBuilder authParamsBuilder;

    private java.util.Map<java.lang.String, java.lang.String> authParams;

    private java.net.URI defaultFsUri;

    private java.util.Map<java.lang.String, java.lang.String> customProperties;

    public ConfigurationBuilder(org.apache.ambari.view.ViewContext context) {
        this.context = context;
        this.authParamsBuilder = new org.apache.ambari.view.utils.hdfs.AuthConfigurationBuilder(context);
    }

    public ConfigurationBuilder(org.apache.ambari.view.ViewContext context, java.util.Map<java.lang.String, java.lang.String> customProperties) {
        this.context = context;
        this.authParamsBuilder = new org.apache.ambari.view.utils.hdfs.AuthConfigurationBuilder(context);
        this.customProperties = customProperties;
    }

    private void parseProperties() throws org.apache.ambari.view.utils.hdfs.HdfsApiException {
        java.lang.String defaultFS = getDefaultFS(context);
        try {
            if (isHAEnabled(defaultFS)) {
                copyHAProperties(defaultFS);
                org.apache.ambari.view.utils.hdfs.ConfigurationBuilder.LOG.info("HA HDFS cluster found.");
            } else if (defaultFS.startsWith("webhdfs://") && (!org.apache.ambari.view.utils.hdfs.ConfigurationBuilder.hasPort(defaultFS))) {
                defaultFS = org.apache.ambari.view.utils.hdfs.ConfigurationBuilder.addPortIfMissing(defaultFS);
            }
            defaultFsUri = new java.net.URI(defaultFS);
        } catch (java.net.URISyntaxException e) {
            throw new org.apache.ambari.view.utils.hdfs.HdfsApiException(((("HDFS060 Invalid " + org.apache.ambari.view.utils.hdfs.ConfigurationBuilder.DEFAULT_FS_INSTANCE_PROPERTY) + "='") + defaultFS) + "' URI", e);
        }
        conf.set("fs.defaultFS", defaultFS);
        org.apache.ambari.view.utils.hdfs.ConfigurationBuilder.LOG.info(java.lang.String.format("HdfsApi configured to connect to defaultFS='%s'", defaultFS));
        if (context.getCluster() != null) {
            java.lang.String encryptionKeyProviderUri = getEncryptionKeyProviderUri();
            if (encryptionKeyProviderUri != null) {
                conf.set("dfs.encryption.key.provider.uri", encryptionKeyProviderUri);
            }
        }
    }

    protected java.lang.String getEncryptionKeyProviderUri() {
        java.lang.String encryptionKeyProviderUri = getProperty("hdfs-site", "dfs.encryption.key.provider.uri");
        return encryptionKeyProviderUri;
    }

    private java.lang.String getDefaultFS(org.apache.ambari.view.ViewContext context) throws org.apache.ambari.view.utils.hdfs.HdfsApiException {
        java.lang.String defaultFS = getProperty(org.apache.ambari.view.utils.hdfs.ConfigurationBuilder.CORE_SITE, org.apache.ambari.view.utils.hdfs.ConfigurationBuilder.DEFAULT_FS_CLUSTER_PROPERTY, org.apache.ambari.view.utils.hdfs.ConfigurationBuilder.DEFAULT_FS_INSTANCE_PROPERTY);
        if ((defaultFS == null) || defaultFS.isEmpty())
            throw new org.apache.ambari.view.utils.hdfs.HdfsApiException("HDFS070 fs.defaultFS is not configured");

        defaultFS = org.apache.ambari.view.utils.hdfs.ConfigurationBuilder.addProtocolIfMissing(defaultFS);
        if (context.getCluster() != null) {
            try {
                java.net.URI fsUri = new java.net.URI(defaultFS);
                java.lang.String protocol = fsUri.getScheme();
                java.lang.String hostWithPort = defaultFS.substring(protocol.length() + 3);
                java.lang.Boolean webHdfsEnabled = java.lang.Boolean.valueOf(getProperty(org.apache.ambari.view.utils.hdfs.ConfigurationBuilder.HDFS_SITE, org.apache.ambari.view.utils.hdfs.ConfigurationBuilder.DFS_WEBHDFS_ENABLED));
                java.lang.Boolean isHttps = org.apache.ambari.view.utils.hdfs.ConfigurationBuilder.DFS_HTTP_POLICY_HTTPS_ONLY.equals(getProperty(org.apache.ambari.view.utils.hdfs.ConfigurationBuilder.HDFS_SITE, org.apache.ambari.view.utils.hdfs.ConfigurationBuilder.DFS_HTTP_POLICY));
                boolean isHA = isHAEnabled(defaultFS);
                if ((webHdfsEnabled && isHttps) && "hdfs".equals(protocol)) {
                    protocol = "swebhdfs";
                    java.lang.String httpAddr = getProperty(org.apache.ambari.view.utils.hdfs.ConfigurationBuilder.HDFS_SITE, org.apache.ambari.view.utils.hdfs.ConfigurationBuilder.DFS_NAMENODE_HTTPS_ADDERSS);
                    if ((!isHA) && (httpAddr != null))
                        hostWithPort = httpAddr;

                } else if (webHdfsEnabled && "hdfs".equals(protocol)) {
                    protocol = "webhdfs";
                    java.lang.String httpsAddr = getProperty(org.apache.ambari.view.utils.hdfs.ConfigurationBuilder.HDFS_SITE, org.apache.ambari.view.utils.hdfs.ConfigurationBuilder.DFS_NAMENODE_HTTP_ADDERSS);
                    if (!isHA)
                        hostWithPort = httpsAddr;

                }
                return (protocol + "://") + hostWithPort;
            } catch (java.net.URISyntaxException e) {
                throw new org.apache.ambari.view.utils.hdfs.HdfsApiException("Invalid URI format." + e.getMessage(), e);
            }
        }
        return defaultFS;
    }

    private java.lang.String getProperty(java.lang.String type, java.lang.String key, java.lang.String instanceProperty) {
        java.lang.String value;
        if (context.getCluster() != null) {
            value = getProperty(type, key);
        } else {
            value = getViewProperty(instanceProperty);
        }
        return value;
    }

    private java.lang.String getViewProperty(java.lang.String instanceProperty) {
        return context.getProperties().get(instanceProperty);
    }

    private java.lang.String getProperty(java.lang.String type, java.lang.String key) {
        if (context.getCluster() != null) {
            return context.getCluster().getConfigurationValue(type, key);
        }
        return null;
    }

    private void copyPropertyIfExists(java.lang.String type, java.lang.String key) {
        java.lang.String value;
        if (context.getCluster() != null) {
            value = context.getCluster().getConfigurationValue(type, key);
            if (value != null) {
                conf.set(key, value);
                org.apache.ambari.view.utils.hdfs.ConfigurationBuilder.LOG.debug((("set " + key) + " = ") + value);
            } else {
                org.apache.ambari.view.utils.hdfs.ConfigurationBuilder.LOG.debug((("No such property " + type) + "/") + key);
            }
        } else {
            org.apache.ambari.view.utils.hdfs.ConfigurationBuilder.LOG.debug((("No such property " + type) + "/") + key);
        }
    }

    private void copyPropertiesBySite(java.lang.String type) {
        if (context.getCluster() != null) {
            java.util.Map<java.lang.String, java.lang.String> configs = context.getCluster().getConfigByType(type);
            org.apache.ambari.view.utils.hdfs.ConfigurationBuilder.LOG.debug("configs from core-site : {}", configs);
            copyProperties(configs);
        } else {
            org.apache.ambari.view.utils.hdfs.ConfigurationBuilder.LOG.error("Cannot find cluster.");
        }
    }

    private void copyProperties(java.util.Map<java.lang.String, java.lang.String> configs) {
        if (null != configs) {
            for (java.util.Map.Entry<java.lang.String, java.lang.String> entry : configs.entrySet()) {
                java.lang.String key = entry.getKey();
                java.lang.String value = entry.getValue();
                conf.set(key, value);
            }
        } else {
            org.apache.ambari.view.utils.hdfs.ConfigurationBuilder.LOG.error("configs were null.");
        }
    }

    @com.google.common.annotations.VisibleForTesting
    void copyHAProperties(java.lang.String defaultFS) throws java.net.URISyntaxException, org.apache.ambari.view.utils.hdfs.HdfsApiException {
        java.net.URI uri = new java.net.URI(defaultFS);
        java.lang.String nameservice = uri.getHost();
        copyClusterProperty(org.apache.ambari.view.utils.hdfs.ConfigurationBuilder.NAMESERVICES_CLUSTER_PROPERTY, org.apache.ambari.view.utils.hdfs.ConfigurationBuilder.NAMESERVICES_INSTANCE_PROPERTY);
        java.lang.String namenodeIDs = copyClusterProperty(java.lang.String.format(org.apache.ambari.view.utils.hdfs.ConfigurationBuilder.HA_NAMENODES_CLUSTER_PROPERTY, nameservice), org.apache.ambari.view.utils.hdfs.ConfigurationBuilder.HA_NAMENODES_INSTANCE_PROPERTY);
        java.lang.String[] namenodes = namenodeIDs.split(",");
        java.util.List<java.lang.String> rpcAddresses = new java.util.ArrayList<>(namenodes.length);
        java.util.List<java.lang.String> httpAddresses = new java.util.ArrayList<>(namenodes.length);
        java.util.List<java.lang.String> httpsAddresses = new java.util.ArrayList<>(namenodes.length);
        for (java.lang.String namenode : namenodes) {
            java.lang.String rpcAddress = getProperty(org.apache.ambari.view.utils.hdfs.ConfigurationBuilder.HDFS_SITE, java.lang.String.format(org.apache.ambari.view.utils.hdfs.ConfigurationBuilder.NAMENODE_RPC_NN_CLUSTER_PROPERTY, nameservice, namenode));
            if (!com.google.common.base.Strings.isNullOrEmpty(rpcAddress)) {
                rpcAddresses.add(rpcAddress);
            }
            java.lang.String httpAddress = getProperty(org.apache.ambari.view.utils.hdfs.ConfigurationBuilder.HDFS_SITE, java.lang.String.format(org.apache.ambari.view.utils.hdfs.ConfigurationBuilder.NAMENODE_HTTP_NN_CLUSTER_PROPERTY, nameservice, namenode));
            if (!com.google.common.base.Strings.isNullOrEmpty(httpAddress)) {
                httpAddresses.add(httpAddress);
            }
            java.lang.String httpsAddress = getProperty(org.apache.ambari.view.utils.hdfs.ConfigurationBuilder.HDFS_SITE, java.lang.String.format(org.apache.ambari.view.utils.hdfs.ConfigurationBuilder.NAMENODE_HTTPS_NN_CLUSTER_PROPERTY, nameservice, namenode));
            if (!com.google.common.base.Strings.isNullOrEmpty(httpsAddress)) {
                httpsAddresses.add(httpsAddress);
            }
        }
        addAddresses(rpcAddresses, org.apache.ambari.view.utils.hdfs.ConfigurationBuilder.NAMENODE_RPC_NN_INSTANCE_PROPERTY);
        addAddresses(httpAddresses, org.apache.ambari.view.utils.hdfs.ConfigurationBuilder.NAMENODE_HTTP_NN_INSTANCE_PROPERTY);
        addAddresses(httpsAddresses, org.apache.ambari.view.utils.hdfs.ConfigurationBuilder.NAMENODE_HTTPS_NN_INSTANCE_PROPERTY);
        for (int i = 0; i < namenodes.length; i++) {
            conf.set(java.lang.String.format(org.apache.ambari.view.utils.hdfs.ConfigurationBuilder.NAMENODE_RPC_NN_CLUSTER_PROPERTY, nameservice, namenodes[i]), rpcAddresses.get(i));
            conf.set(java.lang.String.format(org.apache.ambari.view.utils.hdfs.ConfigurationBuilder.NAMENODE_HTTP_NN_CLUSTER_PROPERTY, nameservice, namenodes[i]), httpAddresses.get(i));
            conf.set(java.lang.String.format(org.apache.ambari.view.utils.hdfs.ConfigurationBuilder.NAMENODE_HTTPS_NN_CLUSTER_PROPERTY, nameservice, namenodes[i]), httpsAddresses.get(i));
        }
        copyClusterProperty(java.lang.String.format(org.apache.ambari.view.utils.hdfs.ConfigurationBuilder.FAILOVER_PROXY_PROVIDER_CLUSTER_PROPERTY, nameservice), org.apache.ambari.view.utils.hdfs.ConfigurationBuilder.FAILOVER_PROXY_PROVIDER_INSTANCE_PROPERTY);
    }

    private void addAddresses(java.util.List<java.lang.String> addressList, java.lang.String propertyName) {
        if (addressList.isEmpty()) {
            java.lang.String addressString = getViewProperty(propertyName);
            org.apache.ambari.view.utils.hdfs.ConfigurationBuilder.LOG.debug("value of {} in view is : {}", propertyName, addressString);
            if (!com.google.common.base.Strings.isNullOrEmpty(addressString)) {
                addressList.addAll(java.util.Arrays.asList(addressString.split(",")));
            }
        }
    }

    private java.lang.String copyClusterProperty(java.lang.String propertyName, java.lang.String instancePropertyName) {
        java.lang.String value = getProperty(org.apache.ambari.view.utils.hdfs.ConfigurationBuilder.HDFS_SITE, propertyName, instancePropertyName);
        if (!org.apache.commons.lang3.StringUtils.isEmpty(value)) {
            conf.set(propertyName, value);
        }
        org.apache.ambari.view.utils.hdfs.ConfigurationBuilder.LOG.debug((("set " + propertyName) + " = ") + value);
        return value;
    }

    private boolean isHAEnabled(java.lang.String defaultFS) throws java.net.URISyntaxException {
        java.net.URI uri = new java.net.URI(defaultFS);
        java.lang.String nameservice = uri.getHost();
        java.lang.String namenodeIDs = getProperty(org.apache.ambari.view.utils.hdfs.ConfigurationBuilder.HDFS_SITE, java.lang.String.format(org.apache.ambari.view.utils.hdfs.ConfigurationBuilder.HA_NAMENODES_CLUSTER_PROPERTY, nameservice), org.apache.ambari.view.utils.hdfs.ConfigurationBuilder.HA_NAMENODES_INSTANCE_PROPERTY);
        org.apache.ambari.view.utils.hdfs.ConfigurationBuilder.LOG.debug("namenodeIDs " + namenodeIDs);
        return !org.apache.commons.lang3.StringUtils.isEmpty(namenodeIDs);
    }

    private static boolean hasPort(java.lang.String url) throws java.net.URISyntaxException {
        java.net.URI uri = new java.net.URI(url);
        return uri.getPort() != (-1);
    }

    protected static java.lang.String addPortIfMissing(java.lang.String defaultFs) throws java.net.URISyntaxException {
        if (!org.apache.ambari.view.utils.hdfs.ConfigurationBuilder.hasPort(defaultFs)) {
            defaultFs = defaultFs + ":50070";
        }
        return defaultFs;
    }

    protected static java.lang.String addProtocolIfMissing(java.lang.String defaultFs) {
        if (!defaultFs.matches("^[^:]+://.*$")) {
            defaultFs = "webhdfs://" + defaultFs;
        }
        return defaultFs;
    }

    public void setAuthParams(java.util.Map<java.lang.String, java.lang.String> authParams) {
        java.lang.String auth = authParams.get("auth");
        if (auth != null) {
            conf.set("hadoop.security.authentication", auth);
        }
    }

    public org.apache.hadoop.conf.Configuration buildConfig() throws org.apache.ambari.view.utils.hdfs.HdfsApiException {
        copyPropertiesBySite(org.apache.ambari.view.utils.hdfs.ConfigurationBuilder.CORE_SITE);
        copyPropertiesBySite(org.apache.ambari.view.utils.hdfs.ConfigurationBuilder.HDFS_SITE);
        parseProperties();
        setAuthParams(buildAuthenticationConfig());
        java.lang.String umask = getViewProperty(org.apache.ambari.view.utils.hdfs.ConfigurationBuilder.UMASK_INSTANCE_PROPERTY);
        if ((umask != null) && (!umask.isEmpty()))
            conf.set(org.apache.ambari.view.utils.hdfs.ConfigurationBuilder.UMASK_CLUSTER_PROPERTY, umask);

        if (null != this.customProperties) {
            copyProperties(this.customProperties);
        }
        if (org.apache.ambari.view.utils.hdfs.ConfigurationBuilder.LOG.isDebugEnabled()) {
            org.apache.ambari.view.utils.hdfs.ConfigurationBuilder.LOG.debug("final conf : {}", printConf());
        }
        return conf;
    }

    private java.lang.String printConf() {
        try {
            java.io.StringWriter stringWriter = new java.io.StringWriter();
            conf.writeXml(stringWriter);
            stringWriter.close();
            return stringWriter.toString().replace("\n", "");
        } catch (java.io.IOException e) {
            org.apache.ambari.view.utils.hdfs.ConfigurationBuilder.LOG.error("error while converting conf to xml : ", e);
            return "";
        }
    }

    public java.util.Map<java.lang.String, java.lang.String> buildAuthenticationConfig() throws org.apache.ambari.view.utils.hdfs.HdfsApiException {
        if (authParams == null) {
            authParams = authParamsBuilder.build();
        }
        return authParams;
    }
}