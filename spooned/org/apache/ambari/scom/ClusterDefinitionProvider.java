package org.apache.ambari.scom;
public class ClusterDefinitionProvider {
    private java.lang.String fileName;

    private java.lang.String clusterName;

    private java.lang.String versionId;

    private static org.apache.ambari.scom.ClusterDefinitionProvider singleton = new org.apache.ambari.scom.ClusterDefinitionProvider();

    protected static final java.lang.String SCOM_CLUSTER_DEFINITION_FILENAME = "scom.cluster.definition.filename";

    protected static final java.lang.String DEFAULT_SCOM_CLUSTER_DEFINITION_FILENAME = "clusterproperties.txt";

    protected static final java.lang.String SCOM_CLUSTER_NAME = "scom.cluster.name";

    protected static final java.lang.String DEFAULT_CLUSTER_NAME = "ambari";

    protected static final java.lang.String SCOM_VERSION_ID = "scom.version.id";

    protected static final java.lang.String DEFAULT_VERSION_ID = "HDP-1.3.0";

    protected ClusterDefinitionProvider() {
    }

    public void init(org.apache.ambari.server.configuration.Configuration configuration) {
        fileName = configuration.getProperty(org.apache.ambari.scom.ClusterDefinitionProvider.SCOM_CLUSTER_DEFINITION_FILENAME);
        if (fileName == null) {
            fileName = org.apache.ambari.scom.ClusterDefinitionProvider.DEFAULT_SCOM_CLUSTER_DEFINITION_FILENAME;
        }
        clusterName = configuration.getProperty(org.apache.ambari.scom.ClusterDefinitionProvider.SCOM_CLUSTER_NAME);
        if (clusterName == null) {
            clusterName = org.apache.ambari.scom.ClusterDefinitionProvider.DEFAULT_CLUSTER_NAME;
        }
        versionId = configuration.getProperty(org.apache.ambari.scom.ClusterDefinitionProvider.SCOM_VERSION_ID);
        if (versionId == null) {
            versionId = org.apache.ambari.scom.ClusterDefinitionProvider.DEFAULT_VERSION_ID;
        }
    }

    public static org.apache.ambari.scom.ClusterDefinitionProvider instance() {
        return org.apache.ambari.scom.ClusterDefinitionProvider.singleton;
    }

    public java.lang.String getFileName() {
        return fileName;
    }

    public java.lang.String getClusterName() {
        return clusterName;
    }

    public java.lang.String getVersionId() {
        return versionId;
    }

    protected void setFileName(java.lang.String fileName) {
        this.fileName = fileName;
    }

    protected void setClusterName(java.lang.String clusterName) {
        this.clusterName = clusterName;
    }

    protected void setVersionId(java.lang.String versionId) {
        this.versionId = versionId;
    }

    public java.io.InputStream getInputStream() {
        java.io.InputStream is;
        java.lang.String name = (this.fileName == null) ? org.apache.ambari.scom.ClusterDefinitionProvider.DEFAULT_SCOM_CLUSTER_DEFINITION_FILENAME : this.fileName;
        try {
            is = this.getClass().getClassLoader().getResourceAsStream(name);
            if (is == null) {
                throw new java.lang.IllegalStateException(("Can't find the resource " + name) + " in the classpath.");
            }
        } catch (java.lang.Exception e) {
            java.lang.String msg = ("Caught exception reading " + name) + ".";
            throw new java.lang.IllegalStateException(msg, e);
        }
        return is;
    }
}