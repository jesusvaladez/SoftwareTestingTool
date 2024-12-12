package org.apache.ambari.scom;
public class ClusterDefinitionProviderTest {
    public static org.apache.ambari.scom.ClusterDefinitionProvider getProvider(java.lang.String filename, java.lang.String clusterName, java.lang.String versionId) {
        java.util.Properties ambariProperties = new java.util.Properties();
        ambariProperties.setProperty(org.apache.ambari.scom.ClusterDefinitionProvider.SCOM_CLUSTER_DEFINITION_FILENAME, filename);
        ambariProperties.setProperty(org.apache.ambari.scom.ClusterDefinitionProvider.SCOM_CLUSTER_NAME, clusterName);
        ambariProperties.setProperty(org.apache.ambari.scom.ClusterDefinitionProvider.SCOM_VERSION_ID, versionId);
        org.apache.ambari.server.configuration.Configuration configuration = new org.apache.ambari.scom.ClusterDefinitionProviderTest.TestConfiguration(ambariProperties);
        org.apache.ambari.scom.ClusterDefinitionProvider streamProvider = new org.apache.ambari.scom.ClusterDefinitionProvider();
        streamProvider.init(configuration);
        return streamProvider;
    }

    @org.junit.Test
    public void testGetFileName() throws java.lang.Exception {
        org.apache.ambari.scom.ClusterDefinitionProvider provider = org.apache.ambari.scom.ClusterDefinitionProviderTest.getProvider("myFile", "myCluster", "myVersion");
        org.junit.Assert.assertEquals("myFile", provider.getFileName());
    }

    @org.junit.Test
    public void testGetClusterName() throws java.lang.Exception {
        org.apache.ambari.scom.ClusterDefinitionProvider provider = org.apache.ambari.scom.ClusterDefinitionProviderTest.getProvider("myFile", "myCluster", "myVersion");
        org.junit.Assert.assertEquals("myCluster", provider.getClusterName());
    }

    @org.junit.Test
    public void testGetVersionId() throws java.lang.Exception {
        org.apache.ambari.scom.ClusterDefinitionProvider provider = org.apache.ambari.scom.ClusterDefinitionProviderTest.getProvider("myFile", "myCluster", "myVersion");
        org.junit.Assert.assertEquals("myVersion", provider.getVersionId());
    }

    @org.junit.Test
    public void testGetInputStream() throws java.lang.Exception {
        org.apache.ambari.scom.ClusterDefinitionProvider provider = org.apache.ambari.scom.ClusterDefinitionProviderTest.getProvider("clusterproperties.txt", "myCluster", "myVersion");
        java.io.InputStream inputStream = provider.getInputStream();
        org.junit.Assert.assertNotNull(inputStream);
    }

    private static class TestConfiguration extends org.apache.ambari.server.configuration.Configuration {
        private TestConfiguration(java.util.Properties properties) {
            super(properties);
        }

        @java.lang.Override
        protected void loadSSLParams() {
        }
    }
}