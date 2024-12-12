package org.apache.ambari.server.controller.internal;
import static org.easymock.EasyMock.createNiceMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
public class AtlasServerHttpPropertyRequestTest {
    @org.junit.Test
    public void testGetUrl() throws java.lang.Exception {
        org.apache.ambari.server.state.Cluster cluster = EasyMock.createNiceMock(org.apache.ambari.server.state.Cluster.class);
        org.apache.ambari.server.state.Config config = EasyMock.createNiceMock(org.apache.ambari.server.state.Config.class);
        java.util.Map<java.lang.String, java.lang.String> map = new java.util.HashMap<>();
        map.put("atlas.enableTLS", "false");
        map.put("atlas.server.http.port", "21000");
        EasyMock.expect(cluster.getDesiredConfigByType("application-properties")).andReturn(config).anyTimes();
        EasyMock.expect(config.getProperties()).andReturn(map).anyTimes();
        EasyMock.replay(cluster, config);
        org.apache.ambari.server.controller.internal.AtlasServerHttpPropertyRequest propertyRequest = new org.apache.ambari.server.controller.internal.AtlasServerHttpPropertyRequest();
        java.lang.String url = propertyRequest.getUrl(cluster, "host1");
        org.junit.Assert.assertEquals("http://host1:21000/api/atlas/admin/status", url);
    }

    @org.junit.Test
    public void testGetUrl_https() throws java.lang.Exception {
        org.apache.ambari.server.state.Cluster cluster = EasyMock.createNiceMock(org.apache.ambari.server.state.Cluster.class);
        org.apache.ambari.server.state.Config config = EasyMock.createNiceMock(org.apache.ambari.server.state.Config.class);
        java.util.Map<java.lang.String, java.lang.String> map = new java.util.HashMap<>();
        map.put("atlas.enableTLS", "true");
        map.put("atlas.server.https.port", "21443");
        EasyMock.expect(cluster.getDesiredConfigByType("application-properties")).andReturn(config).anyTimes();
        EasyMock.expect(config.getProperties()).andReturn(map).anyTimes();
        EasyMock.replay(cluster, config);
        org.apache.ambari.server.controller.internal.AtlasServerHttpPropertyRequest propertyRequest = new org.apache.ambari.server.controller.internal.AtlasServerHttpPropertyRequest();
        java.lang.String url = propertyRequest.getUrl(cluster, "host1");
        org.junit.Assert.assertEquals("https://host1:21443/api/atlas/admin/status", url);
    }
}