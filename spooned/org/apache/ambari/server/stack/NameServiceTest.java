package org.apache.ambari.server.stack;
import org.easymock.EasyMock;
import org.easymock.EasyMockSupport;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.hasSize;
public class NameServiceTest extends org.easymock.EasyMockSupport {
    private org.apache.ambari.server.state.ConfigHelper config = org.easymock.EasyMock.createNiceMock(org.apache.ambari.server.state.ConfigHelper.class);

    private org.apache.ambari.server.state.Cluster cluster = mock(org.apache.ambari.server.state.Cluster.class);

    @org.junit.Test
    public void testParseSingleNameService() {
        defineHdfsProperty("dfs.internal.nameservices", "ns1");
        defineHdfsProperty("dfs.ha.namenodes.ns1", "nn1");
        defineHdfsProperty("dfs.namenode.http-address.ns1.nn1", "c6401:1234");
        EasyMock.replay(config);
        java.util.List<org.apache.ambari.server.stack.NameService> nameServices = org.apache.ambari.server.stack.NameService.fromConfig(config, cluster);
        org.junit.Assert.assertThat(nameServices, org.hamcrest.Matchers.hasSize(1));
        org.junit.Assert.assertThat(nameServices.get(0).nameServiceId, org.hamcrest.core.Is.is("ns1"));
        org.junit.Assert.assertThat(nameServices.get(0).getNameNodes(), hasOnlyItems(org.hamcrest.core.AllOf.allOf(hasHost("c6401"), hasPort(1234), hasPropertyName("dfs.namenode.http-address.ns1.nn1"))));
    }

    @org.junit.Test
    public void testParseSingleNameServiceWhenHttpsEnabled() {
        defineHdfsProperty("dfs.internal.nameservices", "ns1");
        defineHdfsProperty("dfs.ha.namenodes.ns1", "nn1");
        defineHdfsProperty("dfs.namenode.https-address.ns1.nn1", "c6401:4567");
        defineHdfsProperty("dfs.http.policy", org.apache.ambari.server.state.ConfigHelper.HTTPS_ONLY);
        EasyMock.replay(config);
        java.util.List<org.apache.ambari.server.stack.NameService> nameServices = org.apache.ambari.server.stack.NameService.fromConfig(config, cluster);
        org.junit.Assert.assertThat(nameServices.get(0).getNameNodes(), hasOnlyItems(org.hamcrest.core.AllOf.allOf(hasPort(4567), hasPropertyName("dfs.namenode.https-address.ns1.nn1"))));
    }

    @org.junit.Test
    public void testParseFederatedNameService() {
        defineHdfsProperty("dfs.internal.nameservices", "ns1,ns2");
        defineHdfsProperty("dfs.ha.namenodes.ns1", "nn1,nn2");
        defineHdfsProperty("dfs.ha.namenodes.ns2", "nn3,nn4");
        defineHdfsProperty("dfs.namenode.http-address.ns1.nn1", "c6401:1234");
        defineHdfsProperty("dfs.namenode.http-address.ns1.nn2", "c6402:1234");
        defineHdfsProperty("dfs.namenode.http-address.ns2.nn3", "c6403:1234");
        defineHdfsProperty("dfs.namenode.http-address.ns2.nn4", "c6404:1234");
        EasyMock.replay(config);
        org.junit.Assert.assertThat(org.apache.ambari.server.stack.NameService.fromConfig(config, cluster), hasOnlyItems(hasNameNodes(hasOnlyItems(hasHost("c6401"), hasHost("c6402"))), hasNameNodes(hasOnlyItems(hasHost("c6403"), hasHost("c6404")))));
    }

    @org.junit.Test
    public void tesEmptyWhenNameServiceIdIsMissingFromConfig() {
        defineHdfsProperty("dfs.internal.nameservices", null);
        EasyMock.replay(config);
        org.junit.Assert.assertThat(org.apache.ambari.server.stack.NameService.fromConfig(config, cluster), org.hamcrest.Matchers.hasSize(0));
    }

    @org.junit.Test
    public void tesEmptyNameNodesWhenNs1IsMissingFromConfig() {
        defineHdfsProperty("dfs.internal.nameservices", "ns1");
        defineHdfsProperty("dfs.ha.namenodes.ns1", null);
        EasyMock.replay(config);
        org.junit.Assert.assertThat(org.apache.ambari.server.stack.NameService.fromConfig(config, cluster).get(0).getNameNodes(), org.hamcrest.Matchers.hasSize(0));
    }

    @org.junit.Test(expected = java.lang.IllegalArgumentException.class)
    public void tesExceptionWhenNameNodeAddressIsMissingFromConfig() {
        defineHdfsProperty("dfs.internal.nameservices", "ns1");
        defineHdfsProperty("dfs.ha.namenodes.ns1", "nn1");
        defineHdfsProperty("dfs.namenode.http-address.ns1.nn1", null);
        EasyMock.replay(config);
        org.apache.ambari.server.stack.NameService.fromConfig(config, cluster).get(0).getNameNodes().get(0).getHost();
    }

    private org.hamcrest.Matcher hasOnlyItems(org.hamcrest.Matcher... matchers) {
        return org.hamcrest.core.AllOf.allOf(org.hamcrest.Matchers.hasSize(matchers.length), org.junit.internal.matchers.IsCollectionContaining.hasItems(matchers));
    }

    private org.hamcrest.Matcher<org.apache.ambari.server.stack.NameService> hasNameNodes(org.hamcrest.Matcher matcher) {
        return org.hamcrest.Matchers.hasProperty("nameNodes", matcher);
    }

    private org.hamcrest.Matcher<org.apache.ambari.server.stack.NameService.NameNode> hasHost(java.lang.String host) {
        return org.hamcrest.Matchers.hasProperty("host", org.hamcrest.core.Is.is(host));
    }

    private org.hamcrest.Matcher<java.lang.Object> hasPort(int port) {
        return org.hamcrest.Matchers.hasProperty("port", org.hamcrest.core.Is.is(port));
    }

    private org.hamcrest.Matcher<java.lang.Object> hasPropertyName(java.lang.String propertyName) {
        return org.hamcrest.Matchers.hasProperty("propertyName", org.hamcrest.core.Is.is(propertyName));
    }

    private void defineHdfsProperty(java.lang.String propertyName, java.lang.String propertyValue) {
        EasyMock.expect(config.getValueFromDesiredConfigurations(cluster, "hdfs-site", propertyName)).andReturn(propertyValue).anyTimes();
    }
}