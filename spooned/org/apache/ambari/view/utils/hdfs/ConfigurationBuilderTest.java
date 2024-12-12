package org.apache.ambari.view.utils.hdfs;
import org.easymock.EasyMockSupport;
import static org.apache.ambari.view.utils.hdfs.ConfigurationBuilder.HA_NAMENODES_INSTANCE_PROPERTY;
import static org.apache.ambari.view.utils.hdfs.ConfigurationBuilder.NAMENODE_HTTPS_NN_CLUSTER_PROPERTY;
import static org.apache.ambari.view.utils.hdfs.ConfigurationBuilder.NAMENODE_HTTPS_NN_INSTANCE_PROPERTY;
import static org.apache.ambari.view.utils.hdfs.ConfigurationBuilder.NAMENODE_HTTP_NN_CLUSTER_PROPERTY;
import static org.apache.ambari.view.utils.hdfs.ConfigurationBuilder.NAMENODE_HTTP_NN_INSTANCE_PROPERTY;
import static org.apache.ambari.view.utils.hdfs.ConfigurationBuilder.NAMENODE_RPC_NN_CLUSTER_PROPERTY;
import static org.apache.ambari.view.utils.hdfs.ConfigurationBuilder.NAMENODE_RPC_NN_INSTANCE_PROPERTY;
import static org.apache.ambari.view.utils.hdfs.ConfigurationBuilder.NAMESERVICES_INSTANCE_PROPERTY;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
public class ConfigurationBuilderTest extends org.easymock.EasyMockSupport {
    @org.junit.Test
    public void testAddProtocolMissing() throws java.lang.Exception {
        java.lang.String normalized = org.apache.ambari.view.utils.hdfs.ConfigurationBuilder.addProtocolIfMissing("namenode.example.com:50070");
        org.junit.Assert.assertEquals(normalized, "webhdfs://namenode.example.com:50070");
    }

    @org.junit.Test
    public void testAddProtocolPresent() throws java.lang.Exception {
        java.lang.String normalized = org.apache.ambari.view.utils.hdfs.ConfigurationBuilder.addProtocolIfMissing("webhdfs://namenode.example.com");
        org.junit.Assert.assertEquals(normalized, "webhdfs://namenode.example.com");
    }

    @org.junit.Test
    public void testAddPortMissing() throws java.lang.Exception {
        java.lang.String normalized = org.apache.ambari.view.utils.hdfs.ConfigurationBuilder.addPortIfMissing("webhdfs://namenode.example.com");
        org.junit.Assert.assertEquals(normalized, "webhdfs://namenode.example.com:50070");
    }

    @org.junit.Test
    public void testAddPortPresent() throws java.lang.Exception {
        java.lang.String normalized = org.apache.ambari.view.utils.hdfs.ConfigurationBuilder.addPortIfMissing("webhdfs://namenode.example.com:50070");
        org.junit.Assert.assertEquals(normalized, "webhdfs://namenode.example.com:50070");
    }

    @org.junit.Test
    public void testGetEncryptionKeyProviderUri() throws java.lang.Exception {
        java.lang.String keyProvider = "kms://http@localhost:16000/kms";
        org.apache.ambari.view.cluster.Cluster cluster = createNiceMock(org.apache.ambari.view.cluster.Cluster.class);
        EasyMock.expect(cluster.getConfigurationValue("hdfs-site", "dfs.encryption.key.provider.uri")).andReturn(keyProvider);
        EasyMock.replay(cluster);
        org.apache.ambari.view.ViewContext viewContext = createNiceMock(org.apache.ambari.view.ViewContext.class);
        EasyMock.expect(viewContext.getCluster()).andReturn(cluster).anyTimes();
        java.util.Map<java.lang.String, java.lang.String> instanceProperties = new java.util.HashMap<>();
        EasyMock.expect(viewContext.getProperties()).andReturn(instanceProperties).anyTimes();
        EasyMock.replay(viewContext);
        org.apache.ambari.view.utils.hdfs.ConfigurationBuilder configurationBuilder = new org.apache.ambari.view.utils.hdfs.ConfigurationBuilder(viewContext);
        java.lang.String encryptionKeyProviderUri = configurationBuilder.getEncryptionKeyProviderUri();
        org.junit.Assert.assertEquals(encryptionKeyProviderUri, keyProvider);
    }

    @org.junit.Test
    public void testCopyHAProperties() throws java.lang.Exception {
        java.util.Map<java.lang.String, java.lang.String> properties = new java.util.HashMap();
        java.lang.String[] nnrpc = new java.lang.String[]{ "nn1rpc", "nn2rpc", "nn3rpc" };
        java.lang.String[] nnhttp = new java.lang.String[]{ "nn1http", "nn2http", "nn3http" };
        java.lang.String[] nnhttps = new java.lang.String[]{ "nn1https", "nn2https", "nn3https" };
        java.lang.String nameservice = "mycluster";
        java.lang.String nameNodesString = "nn1,nn2,nn3";
        java.lang.String[] namenodes = nameNodesString.split(",");
        properties.put(org.apache.ambari.view.utils.hdfs.ConfigurationBuilder.NAMESERVICES_INSTANCE_PROPERTY, nameservice);
        properties.put(org.apache.ambari.view.utils.hdfs.ConfigurationBuilder.HA_NAMENODES_INSTANCE_PROPERTY, nameNodesString);
        properties.put(org.apache.ambari.view.utils.hdfs.ConfigurationBuilder.NAMENODE_RPC_NN_INSTANCE_PROPERTY, com.google.common.base.Joiner.on(",").join(java.util.Arrays.asList(nnrpc)));
        properties.put(org.apache.ambari.view.utils.hdfs.ConfigurationBuilder.NAMENODE_HTTP_NN_INSTANCE_PROPERTY, com.google.common.base.Joiner.on(",").join(java.util.Arrays.asList(nnhttp)));
        properties.put(org.apache.ambari.view.utils.hdfs.ConfigurationBuilder.NAMENODE_HTTPS_NN_INSTANCE_PROPERTY, com.google.common.base.Joiner.on(",").join(java.util.Arrays.asList(nnhttps)));
        java.lang.String defaultFS = "webhdfs://" + nameservice;
        org.apache.ambari.view.cluster.Cluster cluster = Mockito.mock(org.apache.ambari.view.cluster.Cluster.class);
        org.apache.ambari.view.ViewContext viewContext = Mockito.mock(org.apache.ambari.view.ViewContext.class);
        Mockito.when(viewContext.getCluster()).thenReturn(null);
        Mockito.when(viewContext.getProperties()).thenReturn(properties);
        org.apache.ambari.view.utils.hdfs.ConfigurationBuilder configurationBuilder = new org.apache.ambari.view.utils.hdfs.ConfigurationBuilder(viewContext);
        configurationBuilder.copyHAProperties(defaultFS);
        for (int i = 0; i < nnhttp.length; i++) {
            org.junit.Assert.assertEquals("name node rpc address not correct.", nnrpc[i], configurationBuilder.conf.get(java.lang.String.format(org.apache.ambari.view.utils.hdfs.ConfigurationBuilder.NAMENODE_RPC_NN_CLUSTER_PROPERTY, nameservice, namenodes[i])));
            org.junit.Assert.assertEquals("name node http address not correct.", nnhttp[i], configurationBuilder.conf.get(java.lang.String.format(org.apache.ambari.view.utils.hdfs.ConfigurationBuilder.NAMENODE_HTTP_NN_CLUSTER_PROPERTY, nameservice, namenodes[i])));
            org.junit.Assert.assertEquals("name node https address not correct.", nnhttps[i], configurationBuilder.conf.get(java.lang.String.format(org.apache.ambari.view.utils.hdfs.ConfigurationBuilder.NAMENODE_HTTPS_NN_CLUSTER_PROPERTY, nameservice, namenodes[i])));
        }
    }
}