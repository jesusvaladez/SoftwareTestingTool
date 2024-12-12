package org.apache.ambari.server.controller.internal;
import static org.easymock.EasyMock.anyObject;
import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
public class HostComponentProcessResourceProviderTest {
    @org.junit.Test
    public void testGetResources() throws java.lang.Exception {
        @java.lang.SuppressWarnings("unchecked")
        org.apache.ambari.server.controller.spi.ResourceProvider provider = init(new java.util.HashMap<java.lang.String, java.lang.String>() {
            {
                put("status", "RUNNING");
                put("name", "a1");
            }
        });
        org.apache.ambari.server.controller.utilities.PredicateBuilder pb = new org.apache.ambari.server.controller.utilities.PredicateBuilder().property(org.apache.ambari.server.controller.internal.HostComponentProcessResourceProvider.CLUSTER_NAME).equals("c1").and();
        pb = pb.property(org.apache.ambari.server.controller.internal.HostComponentProcessResourceProvider.HOST_NAME).equals("h1").and();
        org.apache.ambari.server.controller.spi.Predicate predicate = pb.property(org.apache.ambari.server.controller.internal.HostComponentProcessResourceProvider.COMPONENT_NAME).equals("comp1").toPredicate();
        org.apache.ambari.server.controller.spi.Request request = org.apache.ambari.server.controller.utilities.PropertyHelper.getReadRequest(java.util.Collections.emptySet());
        java.util.Set<org.apache.ambari.server.controller.spi.Resource> resources = provider.getResources(request, predicate);
        junit.framework.Assert.assertEquals(java.lang.Integer.valueOf(1), java.lang.Integer.valueOf(resources.size()));
        org.apache.ambari.server.controller.spi.Resource res = resources.iterator().next();
        junit.framework.Assert.assertNotNull(res.getPropertyValue(org.apache.ambari.server.controller.internal.HostComponentProcessResourceProvider.NAME));
        junit.framework.Assert.assertNotNull(res.getPropertyValue(org.apache.ambari.server.controller.internal.HostComponentProcessResourceProvider.STATUS));
    }

    @org.junit.Test
    public void testGetResources_none() throws java.lang.Exception {
        @java.lang.SuppressWarnings("unchecked")
        org.apache.ambari.server.controller.spi.ResourceProvider provider = init();
        org.apache.ambari.server.controller.utilities.PredicateBuilder pb = new org.apache.ambari.server.controller.utilities.PredicateBuilder().property(org.apache.ambari.server.controller.internal.HostComponentProcessResourceProvider.CLUSTER_NAME).equals("c1").and();
        pb = pb.property(org.apache.ambari.server.controller.internal.HostComponentProcessResourceProvider.HOST_NAME).equals("h1").and();
        org.apache.ambari.server.controller.spi.Predicate predicate = pb.property(org.apache.ambari.server.controller.internal.HostComponentProcessResourceProvider.COMPONENT_NAME).equals("comp1").toPredicate();
        org.apache.ambari.server.controller.spi.Request request = org.apache.ambari.server.controller.utilities.PropertyHelper.getReadRequest(java.util.Collections.emptySet());
        java.util.Set<org.apache.ambari.server.controller.spi.Resource> resources = provider.getResources(request, predicate);
        junit.framework.Assert.assertEquals(java.lang.Integer.valueOf(0), java.lang.Integer.valueOf(resources.size()));
    }

    @org.junit.Test
    public void testGetResources_many() throws java.lang.Exception {
        @java.lang.SuppressWarnings("unchecked")
        org.apache.ambari.server.controller.spi.ResourceProvider provider = init(new java.util.HashMap<java.lang.String, java.lang.String>() {
            {
                put("status", "RUNNING");
                put("name", "a");
            }
        }, new java.util.HashMap<java.lang.String, java.lang.String>() {
            {
                put("status", "RUNNING");
                put("name", "b");
            }
        }, new java.util.HashMap<java.lang.String, java.lang.String>() {
            {
                put("status", "NOT_RUNNING");
                put("name", "c");
            }
        });
        org.apache.ambari.server.controller.utilities.PredicateBuilder pb = new org.apache.ambari.server.controller.utilities.PredicateBuilder().property(org.apache.ambari.server.controller.internal.HostComponentProcessResourceProvider.CLUSTER_NAME).equals("c1").and();
        pb = pb.property(org.apache.ambari.server.controller.internal.HostComponentProcessResourceProvider.HOST_NAME).equals("h1").and();
        org.apache.ambari.server.controller.spi.Predicate predicate = pb.property(org.apache.ambari.server.controller.internal.HostComponentProcessResourceProvider.COMPONENT_NAME).equals("comp1").toPredicate();
        org.apache.ambari.server.controller.spi.Request request = org.apache.ambari.server.controller.utilities.PropertyHelper.getReadRequest(java.util.Collections.emptySet());
        java.util.Set<org.apache.ambari.server.controller.spi.Resource> resources = provider.getResources(request, predicate);
        junit.framework.Assert.assertEquals(java.lang.Integer.valueOf(3), java.lang.Integer.valueOf(resources.size()));
        for (org.apache.ambari.server.controller.spi.Resource r : resources) {
            junit.framework.Assert.assertNotNull(r.getPropertyValue(org.apache.ambari.server.controller.internal.HostComponentProcessResourceProvider.NAME));
            junit.framework.Assert.assertNotNull(r.getPropertyValue(org.apache.ambari.server.controller.internal.HostComponentProcessResourceProvider.STATUS));
        }
    }

    private org.apache.ambari.server.controller.spi.ResourceProvider init(java.util.Map<java.lang.String, java.lang.String>... processes) throws java.lang.Exception {
        org.apache.ambari.server.controller.spi.Resource.Type type = org.apache.ambari.server.controller.spi.Resource.Type.HostComponentProcess;
        org.apache.ambari.server.controller.AmbariManagementController amc = EasyMock.createMock(org.apache.ambari.server.controller.AmbariManagementController.class);
        org.apache.ambari.server.state.Clusters clusters = EasyMock.createMock(org.apache.ambari.server.state.Clusters.class);
        org.apache.ambari.server.state.Cluster cluster = EasyMock.createMock(org.apache.ambari.server.state.Cluster.class);
        org.apache.ambari.server.state.ServiceComponentHost sch = EasyMock.createMock(org.apache.ambari.server.state.ServiceComponentHost.class);
        java.util.List<org.apache.ambari.server.state.ServiceComponentHost> schList = java.util.Arrays.asList(sch);
        java.util.List<java.util.Map<java.lang.String, java.lang.String>> procList = java.util.Arrays.asList(processes);
        EasyMock.expect(amc.getClusters()).andReturn(clusters);
        EasyMock.expect(clusters.getCluster("c1")).andReturn(cluster);
        EasyMock.expect(cluster.getServiceComponentHosts(((java.lang.String) (EasyMock.anyObject())))).andReturn(schList);
        EasyMock.expect(sch.getServiceComponentName()).andReturn("comp1");
        EasyMock.expect(sch.getHostName()).andReturn("h1").anyTimes();
        EasyMock.expect(sch.getProcesses()).andReturn(procList);
        EasyMock.replay(amc, clusters, cluster, sch);
        org.apache.ambari.server.controller.spi.ResourceProvider provider = org.apache.ambari.server.controller.internal.AbstractControllerResourceProvider.getResourceProvider(type, amc);
        return provider;
    }
}