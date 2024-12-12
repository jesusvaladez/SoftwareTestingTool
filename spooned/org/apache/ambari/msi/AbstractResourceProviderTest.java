package org.apache.ambari.msi;
public class AbstractResourceProviderTest {
    private static org.apache.ambari.msi.Set<org.apache.ambari.msi.Resource.Type> types = new org.apache.ambari.msi.HashSet<org.apache.ambari.msi.Resource.Type>();

    static {
        types.add(Resource.Type.Cluster);
        types.add(Resource.Type.Service);
        types.add(Resource.Type.Component);
        types.add(Resource.Type.Host);
        types.add(Resource.Type.HostComponent);
        types.add(Resource.Type.Request);
        types.add(Resource.Type.Task);
        types.add(Resource.Type.Configuration);
    }

    @org.junit.Test
    public void testGetResourceProvider() throws java.lang.Exception {
        org.apache.ambari.msi.ClusterDefinition clusterDefinition = new org.apache.ambari.msi.ClusterDefinition(new org.apache.ambari.msi.TestStateProvider(), new org.apache.ambari.scom.TestClusterDefinitionProvider(), new org.apache.ambari.scom.TestHostInfoProvider());
        clusterDefinition.setServiceState("HDFS", "INSTALLED");
        for (org.apache.ambari.msi.Resource.Type type : org.apache.ambari.msi.AbstractResourceProviderTest.types) {
            org.apache.ambari.msi.ResourceProvider provider = org.apache.ambari.msi.AbstractResourceProvider.getResourceProvider(type, clusterDefinition);
            org.apache.ambari.msi.Set<org.apache.ambari.msi.Resource> resources = provider.getResources(org.apache.ambari.msi.PropertyHelper.getReadRequest(), null);
            for (org.apache.ambari.msi.Resource resource : resources) {
                org.junit.Assert.assertEquals(type, resource.getType());
            }
        }
    }
}