package org.apache.ambari.scom;
public class TestClusterDefinitionProvider extends org.apache.ambari.scom.ClusterDefinitionProvider {
    public TestClusterDefinitionProvider() {
        setFileName("clusterproperties.txt");
        setClusterName("myCluster");
        setVersionId("HDP-1.2.9");
    }

    public TestClusterDefinitionProvider(java.lang.String fileName, java.lang.String clusterName, java.lang.String versionId) {
        setFileName(fileName);
        setClusterName(clusterName);
        setVersionId(versionId);
    }
}