package org.apache.ambari.view.utils.hdfs;
public class AuthConfigurationBuilder {
    protected static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.view.utils.hdfs.AuthConfigurationBuilder.class);

    private java.util.Map<java.lang.String, java.lang.String> params = new java.util.HashMap<java.lang.String, java.lang.String>();

    private org.apache.ambari.view.ViewContext context;

    public AuthConfigurationBuilder(org.apache.ambari.view.ViewContext context) {
        this.context = context;
    }

    private void parseProperties() throws org.apache.ambari.view.utils.hdfs.HdfsApiException {
        java.lang.String auth;
        auth = context.getProperties().get("webhdfs.auth");
        if (com.google.common.base.Strings.isNullOrEmpty(auth)) {
            if (context.getCluster() != null) {
                auth = getConfigurationFromAmbari();
            } else {
                auth = "auth=SIMPLE";
                org.apache.ambari.view.utils.hdfs.AuthConfigurationBuilder.LOG.warn(java.lang.String.format("HDFS090 Authentication parameters could not be determined. %s assumed.", auth));
            }
        }
        org.apache.ambari.view.utils.hdfs.AuthConfigurationBuilder.LOG.debug("Hdfs auth params : {}", auth);
        parseAuthString(auth);
    }

    private void parseAuthString(java.lang.String auth) {
        for (java.lang.String param : auth.split(";")) {
            java.lang.String[] keyvalue = param.split("=");
            if (keyvalue.length != 2) {
                org.apache.ambari.view.utils.hdfs.AuthConfigurationBuilder.LOG.error((("HDFS050 Can not parse authentication param " + param) + " in ") + auth);
                continue;
            }
            params.put(keyvalue[0], keyvalue[1]);
        }
    }

    private java.lang.String getConfigurationFromAmbari() throws org.apache.ambari.view.utils.ambari.NoClusterAssociatedException {
        java.lang.String authMethod = context.getCluster().getConfigurationValue("core-site", "hadoop.security.authentication");
        java.lang.String authString = java.lang.String.format("auth=%s", authMethod);
        java.lang.String proxyUser = context.getCluster().getConfigurationValue("cluster-env", "ambari_principal_name");
        if ((proxyUser != null) && (!authMethod.equalsIgnoreCase("SIMPLE"))) {
            authString = authString + java.lang.String.format(";proxyuser=%s", proxyUser.split("@")[0]);
        }
        return authString;
    }

    public java.util.Map<java.lang.String, java.lang.String> build() throws org.apache.ambari.view.utils.hdfs.HdfsApiException {
        parseProperties();
        return params;
    }
}