package org.apache.ambari.server.view;
import org.easymock.EasyMock;
import org.easymock.IAnswer;
import static org.easymock.EasyMock.createNiceMock;
import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.isNull;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
public class RemoteAmbariClusterTest {
    @org.junit.Rule
    public org.junit.rules.ExpectedException thrown = org.junit.rules.ExpectedException.none();

    @org.junit.Test
    public void testGetConfigurationValue() throws java.lang.Exception {
        org.apache.ambari.view.AmbariStreamProvider clusterStreamProvider = EasyMock.createNiceMock(org.apache.ambari.view.AmbariStreamProvider.class);
        final java.lang.String desiredConfigsString = "{\"Clusters\": {\"desired_configs\": {\"test-site\": {\"tag\": \"TAG\"}}}}";
        final java.lang.String configurationString = "{\"items\": [{\"properties\": {\"test.property.name\": \"test property value\"}}]}";
        final int[] desiredConfigPolls = new int[]{ 0 };
        final int[] testConfigPolls = new int[]{ 0 };
        final java.lang.String clusterPath = "/api/v1/clusters/Test";
        EasyMock.expect(clusterStreamProvider.readFrom(EasyMock.eq(clusterPath + "?fields=services/ServiceInfo,hosts,Clusters"), EasyMock.eq("GET"), ((java.lang.String) (EasyMock.isNull())), org.easymock.EasyMock.anyObject())).andAnswer(new org.easymock.IAnswer<java.io.InputStream>() {
            @java.lang.Override
            public java.io.InputStream answer() throws java.lang.Throwable {
                desiredConfigPolls[0] += 1;
                return new java.io.ByteArrayInputStream(desiredConfigsString.getBytes());
            }
        }).anyTimes();
        EasyMock.expect(clusterStreamProvider.readFrom(EasyMock.eq(clusterPath + "/configurations?(type=test-site&tag=TAG)"), EasyMock.eq("GET"), ((java.lang.String) (EasyMock.isNull())), org.easymock.EasyMock.anyObject())).andAnswer(new org.easymock.IAnswer<java.io.InputStream>() {
            @java.lang.Override
            public java.io.InputStream answer() throws java.lang.Throwable {
                testConfigPolls[0] += 1;
                return new java.io.ByteArrayInputStream(configurationString.getBytes());
            }
        }).anyTimes();
        org.apache.ambari.server.orm.entities.RemoteAmbariClusterEntity entity = EasyMock.createNiceMock(org.apache.ambari.server.orm.entities.RemoteAmbariClusterEntity.class);
        EasyMock.replay(clusterStreamProvider, entity);
        org.apache.ambari.server.view.RemoteAmbariCluster cluster = new org.apache.ambari.server.view.RemoteAmbariCluster("Test", clusterPath, clusterStreamProvider);
        java.lang.String value = cluster.getConfigurationValue("test-site", "test.property.name");
        org.junit.Assert.assertEquals(value, "test property value");
        org.junit.Assert.assertEquals(desiredConfigPolls[0], 1);
        org.junit.Assert.assertEquals(testConfigPolls[0], 1);
        value = cluster.getConfigurationValue("test-site", "test.property.name");
        org.junit.Assert.assertEquals(value, "test property value");
        org.junit.Assert.assertEquals(desiredConfigPolls[0], 1);
        org.junit.Assert.assertEquals(testConfigPolls[0], 1);
    }

    @org.junit.Test
    public void testGetHostsForServiceComponent() throws java.io.IOException, org.apache.ambari.view.AmbariHttpException {
        final java.lang.String componentHostsString = "{\"host_components\": [{" + ((((("\"HostRoles\": {" + "\"cluster_name\": \"Ambari\",") + "\"host_name\": \"host1\"}}, {") + "\"HostRoles\": {") + "\"cluster_name\": \"Ambari\",") + "\"host_name\": \"host2\"}}]}");
        org.apache.ambari.view.AmbariStreamProvider clusterStreamProvider = EasyMock.createNiceMock(org.apache.ambari.view.AmbariStreamProvider.class);
        java.lang.String service = "SERVICE";
        java.lang.String component = "COMPONENT";
        final java.lang.String clusterPath = "/api/v1/clusters/Test";
        EasyMock.expect(clusterStreamProvider.readFrom(EasyMock.eq(java.lang.String.format("%s/services/%s/components/%s?" + "fields=host_components/HostRoles/host_name", clusterPath, service, component)), EasyMock.eq("GET"), ((java.lang.String) (EasyMock.isNull())), org.easymock.EasyMock.anyObject())).andReturn(new java.io.ByteArrayInputStream(componentHostsString.getBytes()));
        org.apache.ambari.server.orm.entities.RemoteAmbariClusterEntity entity = EasyMock.createNiceMock(org.apache.ambari.server.orm.entities.RemoteAmbariClusterEntity.class);
        EasyMock.replay(clusterStreamProvider, entity);
        org.apache.ambari.server.view.RemoteAmbariCluster cluster = new org.apache.ambari.server.view.RemoteAmbariCluster("Test", clusterPath, clusterStreamProvider);
        java.util.List<java.lang.String> hosts = cluster.getHostsForServiceComponent(service, component);
        org.junit.Assert.assertEquals(2, hosts.size());
        org.junit.Assert.assertEquals("host1", hosts.get(0));
        org.junit.Assert.assertEquals("host2", hosts.get(1));
        EasyMock.verify(clusterStreamProvider, entity);
    }
}