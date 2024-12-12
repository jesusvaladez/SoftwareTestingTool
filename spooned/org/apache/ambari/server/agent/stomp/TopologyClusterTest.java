package org.apache.ambari.server.agent.stomp;
public class TopologyClusterTest {
    @org.junit.Test
    public void testHandlingReportHostAdd() throws org.apache.ambari.server.NullHostNameException {
        org.apache.ambari.server.agent.stomp.dto.TopologyHost dummyHost = new org.apache.ambari.server.agent.stomp.dto.TopologyHost(1L, "hostName1");
        org.apache.ambari.server.agent.stomp.dto.TopologyHost hostToAddition = new org.apache.ambari.server.agent.stomp.dto.TopologyHost(2L, "hostName2");
        org.apache.ambari.server.agent.stomp.dto.TopologyCluster topologyCluster = new org.apache.ambari.server.agent.stomp.dto.TopologyCluster(new java.util.HashSet<>(), new java.util.HashSet() {
            {
                add(dummyHost);
            }
        });
        org.apache.ambari.server.agent.stomp.dto.TopologyUpdateHandlingReport report = new org.apache.ambari.server.agent.stomp.dto.TopologyUpdateHandlingReport();
        topologyCluster.update(java.util.Collections.emptySet(), java.util.Collections.singleton(hostToAddition), org.apache.ambari.server.events.UpdateEventType.UPDATE, report);
        org.junit.Assert.assertEquals(1L, report.getUpdatedHostNames().size());
        org.junit.Assert.assertEquals("hostName2", report.getUpdatedHostNames().iterator().next());
        org.junit.Assert.assertEquals(2L, topologyCluster.getTopologyHosts().size());
    }

    @org.junit.Test
    public void testHandlingReportHostDelete() throws org.apache.ambari.server.NullHostNameException {
        org.apache.ambari.server.agent.stomp.dto.TopologyHost dummyHost = new org.apache.ambari.server.agent.stomp.dto.TopologyHost(1L, "hostName1");
        org.apache.ambari.server.agent.stomp.dto.TopologyHost hostToDelete = new org.apache.ambari.server.agent.stomp.dto.TopologyHost(2L, "hostName2");
        org.apache.ambari.server.agent.stomp.dto.TopologyHost update = new org.apache.ambari.server.agent.stomp.dto.TopologyHost(2L, "hostName2");
        org.apache.ambari.server.agent.stomp.dto.TopologyCluster topologyCluster = new org.apache.ambari.server.agent.stomp.dto.TopologyCluster(new java.util.HashSet<>(), new java.util.HashSet() {
            {
                add(dummyHost);
                add(hostToDelete);
            }
        });
        org.apache.ambari.server.agent.stomp.dto.TopologyUpdateHandlingReport report = new org.apache.ambari.server.agent.stomp.dto.TopologyUpdateHandlingReport();
        topologyCluster.update(java.util.Collections.emptySet(), java.util.Collections.singleton(update), org.apache.ambari.server.events.UpdateEventType.DELETE, report);
        org.junit.Assert.assertEquals(1L, report.getUpdatedHostNames().size());
        org.junit.Assert.assertEquals("hostName2", report.getUpdatedHostNames().iterator().next());
        org.junit.Assert.assertEquals(1L, topologyCluster.getTopologyHosts().size());
        org.junit.Assert.assertEquals("hostName1", topologyCluster.getTopologyHosts().iterator().next().getHostName());
    }

    @org.junit.Test
    public void testHandlingReportHostUpdate() throws org.apache.ambari.server.NullHostNameException {
        org.apache.ambari.server.agent.stomp.dto.TopologyHost dummyHost = new org.apache.ambari.server.agent.stomp.dto.TopologyHost(1L, "hostName1");
        org.apache.ambari.server.agent.stomp.dto.TopologyHost hostToUpdate = new org.apache.ambari.server.agent.stomp.dto.TopologyHost(2L, "hostName2");
        org.apache.ambari.server.agent.stomp.dto.TopologyHost update = new org.apache.ambari.server.agent.stomp.dto.TopologyHost(2L, "hostName2", "rack", "ipv4");
        org.apache.ambari.server.agent.stomp.dto.TopologyCluster topologyCluster = new org.apache.ambari.server.agent.stomp.dto.TopologyCluster(new java.util.HashSet<>(), new java.util.HashSet() {
            {
                add(dummyHost);
                add(hostToUpdate);
            }
        });
        org.apache.ambari.server.agent.stomp.dto.TopologyUpdateHandlingReport report = new org.apache.ambari.server.agent.stomp.dto.TopologyUpdateHandlingReport();
        topologyCluster.update(java.util.Collections.emptySet(), java.util.Collections.singleton(update), org.apache.ambari.server.events.UpdateEventType.UPDATE, report);
        org.junit.Assert.assertEquals(1L, report.getUpdatedHostNames().size());
        org.junit.Assert.assertEquals("hostName2", report.getUpdatedHostNames().iterator().next());
        org.junit.Assert.assertEquals(2L, topologyCluster.getTopologyHosts().size());
    }

    @org.junit.Test
    public void testHandlingReportComponentAdd() throws org.apache.ambari.server.NullHostNameException {
        org.apache.ambari.server.agent.stomp.dto.TopologyComponent dummyComponent = createDummyTopologyComponent("comp1", new java.lang.Long[]{ 1L, 2L }, new java.lang.String[]{ "hostName1", "hostName2" });
        org.apache.ambari.server.agent.stomp.dto.TopologyComponent componentToAddition = createDummyTopologyComponent("comp2", new java.lang.Long[]{ 1L, 3L }, new java.lang.String[]{ "hostName1", "hostName3" });
        org.apache.ambari.server.agent.stomp.dto.TopologyCluster topologyCluster = new org.apache.ambari.server.agent.stomp.dto.TopologyCluster(new java.util.HashSet() {
            {
                add(dummyComponent);
            }
        }, new java.util.HashSet<>());
        org.apache.ambari.server.agent.stomp.dto.TopologyUpdateHandlingReport report = new org.apache.ambari.server.agent.stomp.dto.TopologyUpdateHandlingReport();
        topologyCluster.update(java.util.Collections.singleton(componentToAddition), java.util.Collections.emptySet(), org.apache.ambari.server.events.UpdateEventType.UPDATE, report);
        org.junit.Assert.assertEquals(2L, report.getUpdatedHostNames().size());
        org.junit.Assert.assertTrue(report.getUpdatedHostNames().contains("hostName1"));
        org.junit.Assert.assertTrue(report.getUpdatedHostNames().contains("hostName3"));
        org.junit.Assert.assertEquals(2L, topologyCluster.getTopologyComponents().size());
    }

    @org.junit.Test
    public void testHandlingReportComponentDeletePartially() throws org.apache.ambari.server.NullHostNameException {
        org.apache.ambari.server.agent.stomp.dto.TopologyComponent dummyComponent = createDummyTopologyComponent("comp1", new java.lang.Long[]{ 1L, 2L }, new java.lang.String[]{ "hostName1", "hostName2" });
        org.apache.ambari.server.agent.stomp.dto.TopologyComponent componentToDelete = createDummyTopologyComponent("comp2", new java.lang.Long[]{ 1L, 3L }, new java.lang.String[]{ "hostName1", "hostName3" });
        org.apache.ambari.server.agent.stomp.dto.TopologyComponent update = createDummyTopologyComponent("comp2", new java.lang.Long[]{ 1L }, new java.lang.String[]{ "hostName1" });
        org.apache.ambari.server.agent.stomp.dto.TopologyCluster topologyCluster = new org.apache.ambari.server.agent.stomp.dto.TopologyCluster(new java.util.HashSet() {
            {
                add(dummyComponent);
                add(componentToDelete);
            }
        }, new java.util.HashSet<>());
        org.apache.ambari.server.agent.stomp.dto.TopologyUpdateHandlingReport report = new org.apache.ambari.server.agent.stomp.dto.TopologyUpdateHandlingReport();
        topologyCluster.update(java.util.Collections.singleton(update), java.util.Collections.emptySet(), org.apache.ambari.server.events.UpdateEventType.DELETE, report);
        org.junit.Assert.assertEquals(1L, report.getUpdatedHostNames().size());
        org.junit.Assert.assertTrue(report.getUpdatedHostNames().contains("hostName1"));
        org.junit.Assert.assertEquals(2L, topologyCluster.getTopologyComponents().size());
    }

    @org.junit.Test
    public void testHandlingReportComponentDeleteFully() throws org.apache.ambari.server.NullHostNameException {
        org.apache.ambari.server.agent.stomp.dto.TopologyComponent dummyComponent = createDummyTopologyComponent("comp1", new java.lang.Long[]{ 1L, 2L }, new java.lang.String[]{ "hostName1", "hostName2" });
        org.apache.ambari.server.agent.stomp.dto.TopologyComponent componentToDelete = createDummyTopologyComponent("comp2", new java.lang.Long[]{ 1L, 3L }, new java.lang.String[]{ "hostName1", "hostName3" });
        org.apache.ambari.server.agent.stomp.dto.TopologyComponent update = createDummyTopologyComponent("comp2", new java.lang.Long[]{ 1L, 3L }, new java.lang.String[]{ "hostName1", "hostName3" });
        org.apache.ambari.server.agent.stomp.dto.TopologyCluster topologyCluster = new org.apache.ambari.server.agent.stomp.dto.TopologyCluster(new java.util.HashSet() {
            {
                add(dummyComponent);
                add(componentToDelete);
            }
        }, new java.util.HashSet<>());
        org.apache.ambari.server.agent.stomp.dto.TopologyUpdateHandlingReport report = new org.apache.ambari.server.agent.stomp.dto.TopologyUpdateHandlingReport();
        topologyCluster.update(java.util.Collections.singleton(update), java.util.Collections.emptySet(), org.apache.ambari.server.events.UpdateEventType.DELETE, report);
        org.junit.Assert.assertEquals(2L, report.getUpdatedHostNames().size());
        org.junit.Assert.assertTrue(report.getUpdatedHostNames().contains("hostName1"));
        org.junit.Assert.assertTrue(report.getUpdatedHostNames().contains("hostName3"));
        org.junit.Assert.assertEquals(1L, topologyCluster.getTopologyComponents().size());
    }

    @org.junit.Test
    public void testHandlingReportComponentUpdate() throws org.apache.ambari.server.NullHostNameException {
        org.apache.ambari.server.agent.stomp.dto.TopologyComponent dummyComponent = createDummyTopologyComponent("comp1", new java.lang.Long[]{ 1L, 2L }, new java.lang.String[]{ "hostName1", "hostName2" });
        org.apache.ambari.server.agent.stomp.dto.TopologyComponent componentToUpdate = createDummyTopologyComponent("comp2", new java.lang.Long[]{ 1L, 3L }, new java.lang.String[]{ "hostName1", "hostName3" });
        org.apache.ambari.server.agent.stomp.dto.TopologyComponent update = createDummyTopologyComponent("comp2", new java.lang.Long[]{ 1L, 4L }, new java.lang.String[]{ "hostName1", "hostName4" });
        org.apache.ambari.server.agent.stomp.dto.TopologyCluster topologyCluster = new org.apache.ambari.server.agent.stomp.dto.TopologyCluster(new java.util.HashSet() {
            {
                add(dummyComponent);
                add(componentToUpdate);
            }
        }, new java.util.HashSet<>());
        org.apache.ambari.server.agent.stomp.dto.TopologyUpdateHandlingReport report = new org.apache.ambari.server.agent.stomp.dto.TopologyUpdateHandlingReport();
        topologyCluster.update(java.util.Collections.singleton(update), java.util.Collections.emptySet(), org.apache.ambari.server.events.UpdateEventType.UPDATE, report);
        org.junit.Assert.assertEquals(1L, report.getUpdatedHostNames().size());
        org.junit.Assert.assertTrue(report.getUpdatedHostNames().contains("hostName4"));
        org.junit.Assert.assertEquals(2L, topologyCluster.getTopologyComponents().size());
    }

    private org.apache.ambari.server.agent.stomp.dto.TopologyComponent createDummyTopologyComponent(java.lang.String componentName, java.lang.Long[] hostIds, java.lang.String[] hostNames) {
        return org.apache.ambari.server.agent.stomp.dto.TopologyComponent.newBuilder().setComponentName(componentName).setServiceName("serviceName").setHostIdentifiers(new java.util.HashSet<java.lang.Long>(java.util.Arrays.asList(hostIds)), new java.util.HashSet<java.lang.String>(java.util.Arrays.asList(hostNames))).build();
    }
}