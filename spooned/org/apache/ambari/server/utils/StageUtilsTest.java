package org.apache.ambari.server.utils;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import javax.xml.bind.JAXBException;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.easymock.EasyMockSupport;
import org.easymock.IAnswer;
import static org.easymock.EasyMock.anyObject;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.getCurrentArguments;
public class StageUtilsTest extends org.easymock.EasyMockSupport {
    private static final java.lang.String STACK_ID = "HDP-1.3.1";

    private com.google.inject.Injector injector;

    @org.junit.Before
    public void setup() throws java.lang.Exception {
        injector = com.google.inject.Guice.createInjector(new com.google.inject.AbstractModule() {
            @java.lang.Override
            protected void configure() {
                org.apache.ambari.server.testutils.PartialNiceMockBinder.newBuilder(StageUtilsTest.this).addAmbariMetaInfoBinding().addDBAccessorBinding().addLdapBindings().build().configure(binder());
                bind(org.apache.ambari.server.api.services.AmbariMetaInfo.class).toInstance(createMock(org.apache.ambari.server.api.services.AmbariMetaInfo.class));
                bind(org.apache.ambari.server.topology.TopologyManager.class).toInstance(createNiceMock(org.apache.ambari.server.topology.TopologyManager.class));
                bind(org.apache.ambari.server.orm.dao.HostDAO.class).toInstance(createNiceMock(org.apache.ambari.server.orm.dao.HostDAO.class));
                install(new com.google.inject.assistedinject.FactoryModuleBuilder().build(org.apache.ambari.server.actionmanager.ExecutionCommandWrapperFactory.class));
                install(new com.google.inject.assistedinject.FactoryModuleBuilder().build(org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeContextFactory.class));
            }
        });
        org.apache.ambari.server.utils.StageUtils.setTopologyManager(injector.getInstance(org.apache.ambari.server.topology.TopologyManager.class));
        org.apache.ambari.server.utils.StageUtils.setConfiguration(injector.getInstance(org.apache.ambari.server.configuration.Configuration.class));
    }

    @org.junit.Test
    public void testGetATestStage() {
        org.apache.ambari.server.utils.StageUtils stageUtils = new org.apache.ambari.server.utils.StageUtils(injector.getInstance(org.apache.ambari.server.actionmanager.StageFactory.class));
        org.apache.ambari.server.actionmanager.Stage s = org.apache.ambari.server.utils.StageUtils.getATestStage(1, 2, "host2", "", "hostParamsStage");
        java.lang.String hostname = s.getHosts().get(0);
        java.util.List<org.apache.ambari.server.actionmanager.ExecutionCommandWrapper> wrappers = s.getExecutionCommands(hostname);
        for (org.apache.ambari.server.actionmanager.ExecutionCommandWrapper wrapper : wrappers) {
            org.junit.Assert.assertEquals("cluster1", wrapper.getExecutionCommand().getClusterName());
            org.junit.Assert.assertEquals(org.apache.ambari.server.utils.StageUtils.getActionId(1, 2), wrapper.getExecutionCommand().getCommandId());
            org.junit.Assert.assertEquals(hostname, wrapper.getExecutionCommand().getHostname());
        }
    }

    @org.junit.Test
    public void testJaxbToString() throws java.lang.Exception {
        org.apache.ambari.server.utils.StageUtils stageUtils = new org.apache.ambari.server.utils.StageUtils(injector.getInstance(org.apache.ambari.server.actionmanager.StageFactory.class));
        org.apache.ambari.server.actionmanager.Stage s = org.apache.ambari.server.utils.StageUtils.getATestStage(1, 2, "host1", "", "hostParamsStage");
        java.lang.String hostname = s.getHosts().get(0);
        java.util.List<org.apache.ambari.server.actionmanager.ExecutionCommandWrapper> wrappers = s.getExecutionCommands(hostname);
        for (org.apache.ambari.server.actionmanager.ExecutionCommandWrapper wrapper : wrappers) {
        }
        org.junit.Assert.assertEquals(org.apache.ambari.server.utils.StageUtils.getActionId(1, 2), s.getActionId());
    }

    @org.junit.Test
    public void testJasonToExecutionCommand() throws org.codehaus.jackson.JsonGenerationException, org.codehaus.jackson.map.JsonMappingException, javax.xml.bind.JAXBException, java.io.IOException {
        org.apache.ambari.server.utils.StageUtils stageUtils = new org.apache.ambari.server.utils.StageUtils(injector.getInstance(org.apache.ambari.server.actionmanager.StageFactory.class));
        org.apache.ambari.server.actionmanager.Stage s = org.apache.ambari.server.utils.StageUtils.getATestStage(1, 2, "host1", "clusterHostInfo", "hostParamsStage");
        org.apache.ambari.server.agent.ExecutionCommand cmd = s.getExecutionCommands("host1").get(0).getExecutionCommand();
        java.lang.String json = org.apache.ambari.server.utils.StageUtils.jaxbToString(cmd);
        java.io.InputStream is = new java.io.ByteArrayInputStream(json.getBytes(java.nio.charset.Charset.forName("UTF8")));
        org.apache.ambari.server.agent.ExecutionCommand cmdDes = new com.google.gson.Gson().fromJson(new java.io.InputStreamReader(is), org.apache.ambari.server.agent.ExecutionCommand.class);
        org.junit.Assert.assertEquals(cmd.toString(), cmdDes.toString());
        org.junit.Assert.assertEquals(cmd, cmdDes);
    }

    @org.junit.Test
    public void testGetClusterHostInfo() throws java.lang.Exception {
        final java.util.HashMap<java.lang.String, java.lang.String> hostAttributes = new java.util.HashMap<java.lang.String, java.lang.String>() {
            {
                put("os_family", "redhat");
                put("os_release_version", "5.9");
            }
        };
        final org.apache.ambari.server.state.Clusters clusters = createNiceMock(org.apache.ambari.server.state.Clusters.class);
        java.util.List<org.apache.ambari.server.state.Host> hosts = new java.util.ArrayList<>();
        java.util.List<java.lang.String> hostNames = new java.util.ArrayList<>();
        java.util.List<java.lang.Integer> pingPorts = java.util.Arrays.asList(org.apache.ambari.server.utils.StageUtils.DEFAULT_PING_PORT, org.apache.ambari.server.utils.StageUtils.DEFAULT_PING_PORT, org.apache.ambari.server.utils.StageUtils.DEFAULT_PING_PORT, 8671, 8671, null, 8672, 8672, null, 8673);
        for (int i = 0; i < 10; i++) {
            java.lang.String hostname = java.lang.String.format("h%d", i);
            org.apache.ambari.server.state.Host host = createNiceMock(org.apache.ambari.server.state.Host.class);
            EasyMock.expect(host.getHostName()).andReturn(hostname).anyTimes();
            EasyMock.expect(host.getHostAttributes()).andReturn(hostAttributes).anyTimes();
            EasyMock.expect(host.getCurrentPingPort()).andReturn(pingPorts.get(i)).anyTimes();
            hosts.add(host);
            hostNames.add(hostname);
            EasyMock.expect(clusters.getHost(hostname)).andReturn(host).anyTimes();
        }
        final org.apache.ambari.server.state.ServiceComponentHost nnh0ServiceComponentHost = createMock(org.apache.ambari.server.state.ServiceComponentHost.class);
        EasyMock.expect(nnh0ServiceComponentHost.getComponentAdminState()).andReturn(org.apache.ambari.server.state.HostComponentAdminState.INSERVICE).anyTimes();
        final org.apache.ambari.server.state.ServiceComponentHost snnh1ServiceComponentHost = createMock(org.apache.ambari.server.state.ServiceComponentHost.class);
        EasyMock.expect(snnh1ServiceComponentHost.getComponentAdminState()).andReturn(org.apache.ambari.server.state.HostComponentAdminState.INSERVICE).anyTimes();
        final org.apache.ambari.server.state.ServiceComponentHost dnh0ServiceComponentHost = createMock(org.apache.ambari.server.state.ServiceComponentHost.class);
        EasyMock.expect(dnh0ServiceComponentHost.getComponentAdminState()).andReturn(org.apache.ambari.server.state.HostComponentAdminState.INSERVICE).anyTimes();
        final org.apache.ambari.server.state.ServiceComponentHost dnh1ServiceComponentHost = createMock(org.apache.ambari.server.state.ServiceComponentHost.class);
        EasyMock.expect(dnh1ServiceComponentHost.getComponentAdminState()).andReturn(org.apache.ambari.server.state.HostComponentAdminState.INSERVICE).anyTimes();
        final org.apache.ambari.server.state.ServiceComponentHost dnh2ServiceComponentHost = createMock(org.apache.ambari.server.state.ServiceComponentHost.class);
        EasyMock.expect(dnh2ServiceComponentHost.getComponentAdminState()).andReturn(org.apache.ambari.server.state.HostComponentAdminState.INSERVICE).anyTimes();
        final org.apache.ambari.server.state.ServiceComponentHost dnh3ServiceComponentHost = createMock(org.apache.ambari.server.state.ServiceComponentHost.class);
        EasyMock.expect(dnh3ServiceComponentHost.getComponentAdminState()).andReturn(org.apache.ambari.server.state.HostComponentAdminState.INSERVICE).anyTimes();
        final org.apache.ambari.server.state.ServiceComponentHost dnh5ServiceComponentHost = createMock(org.apache.ambari.server.state.ServiceComponentHost.class);
        EasyMock.expect(dnh5ServiceComponentHost.getComponentAdminState()).andReturn(org.apache.ambari.server.state.HostComponentAdminState.INSERVICE).anyTimes();
        final org.apache.ambari.server.state.ServiceComponentHost dnh7ServiceComponentHost = createMock(org.apache.ambari.server.state.ServiceComponentHost.class);
        EasyMock.expect(dnh7ServiceComponentHost.getComponentAdminState()).andReturn(org.apache.ambari.server.state.HostComponentAdminState.INSERVICE).anyTimes();
        final org.apache.ambari.server.state.ServiceComponentHost dnh8ServiceComponentHost = createMock(org.apache.ambari.server.state.ServiceComponentHost.class);
        EasyMock.expect(dnh8ServiceComponentHost.getComponentAdminState()).andReturn(org.apache.ambari.server.state.HostComponentAdminState.INSERVICE).anyTimes();
        final org.apache.ambari.server.state.ServiceComponentHost dnh9ServiceComponentHost = createMock(org.apache.ambari.server.state.ServiceComponentHost.class);
        EasyMock.expect(dnh9ServiceComponentHost.getComponentAdminState()).andReturn(org.apache.ambari.server.state.HostComponentAdminState.INSERVICE).anyTimes();
        final org.apache.ambari.server.state.ServiceComponentHost hbm5ServiceComponentHost = createMock(org.apache.ambari.server.state.ServiceComponentHost.class);
        EasyMock.expect(hbm5ServiceComponentHost.getComponentAdminState()).andReturn(org.apache.ambari.server.state.HostComponentAdminState.INSERVICE).anyTimes();
        final org.apache.ambari.server.state.ServiceComponentHost hbrs1ServiceComponentHost = createMock(org.apache.ambari.server.state.ServiceComponentHost.class);
        EasyMock.expect(hbrs1ServiceComponentHost.getComponentAdminState()).andReturn(org.apache.ambari.server.state.HostComponentAdminState.INSERVICE).anyTimes();
        final org.apache.ambari.server.state.ServiceComponentHost hbrs3ServiceComponentHost = createMock(org.apache.ambari.server.state.ServiceComponentHost.class);
        EasyMock.expect(hbrs3ServiceComponentHost.getComponentAdminState()).andReturn(org.apache.ambari.server.state.HostComponentAdminState.INSERVICE).anyTimes();
        final org.apache.ambari.server.state.ServiceComponentHost hbrs5ServiceComponentHost = createMock(org.apache.ambari.server.state.ServiceComponentHost.class);
        EasyMock.expect(hbrs5ServiceComponentHost.getComponentAdminState()).andReturn(org.apache.ambari.server.state.HostComponentAdminState.INSERVICE).anyTimes();
        final org.apache.ambari.server.state.ServiceComponentHost hbrs8ServiceComponentHost = createMock(org.apache.ambari.server.state.ServiceComponentHost.class);
        EasyMock.expect(hbrs8ServiceComponentHost.getComponentAdminState()).andReturn(org.apache.ambari.server.state.HostComponentAdminState.INSERVICE).anyTimes();
        final org.apache.ambari.server.state.ServiceComponentHost hbrs9ServiceComponentHost = createMock(org.apache.ambari.server.state.ServiceComponentHost.class);
        EasyMock.expect(hbrs9ServiceComponentHost.getComponentAdminState()).andReturn(org.apache.ambari.server.state.HostComponentAdminState.INSERVICE).anyTimes();
        final org.apache.ambari.server.state.ServiceComponentHost mrjt5ServiceComponentHost = createMock(org.apache.ambari.server.state.ServiceComponentHost.class);
        EasyMock.expect(mrjt5ServiceComponentHost.getComponentAdminState()).andReturn(org.apache.ambari.server.state.HostComponentAdminState.INSERVICE).anyTimes();
        final org.apache.ambari.server.state.ServiceComponentHost mrtt1ServiceComponentHost = createMock(org.apache.ambari.server.state.ServiceComponentHost.class);
        EasyMock.expect(mrtt1ServiceComponentHost.getComponentAdminState()).andReturn(org.apache.ambari.server.state.HostComponentAdminState.INSERVICE).anyTimes();
        final org.apache.ambari.server.state.ServiceComponentHost mrtt2ServiceComponentHost = createMock(org.apache.ambari.server.state.ServiceComponentHost.class);
        EasyMock.expect(mrtt2ServiceComponentHost.getComponentAdminState()).andReturn(org.apache.ambari.server.state.HostComponentAdminState.DECOMMISSIONED).anyTimes();
        final org.apache.ambari.server.state.ServiceComponentHost mrtt3ServiceComponentHost = createMock(org.apache.ambari.server.state.ServiceComponentHost.class);
        EasyMock.expect(mrtt3ServiceComponentHost.getComponentAdminState()).andReturn(org.apache.ambari.server.state.HostComponentAdminState.DECOMMISSIONED).anyTimes();
        final org.apache.ambari.server.state.ServiceComponentHost mrtt4ServiceComponentHost = createMock(org.apache.ambari.server.state.ServiceComponentHost.class);
        EasyMock.expect(mrtt4ServiceComponentHost.getComponentAdminState()).andReturn(org.apache.ambari.server.state.HostComponentAdminState.INSERVICE).anyTimes();
        final org.apache.ambari.server.state.ServiceComponentHost mrtt5ServiceComponentHost = createMock(org.apache.ambari.server.state.ServiceComponentHost.class);
        EasyMock.expect(mrtt5ServiceComponentHost.getComponentAdminState()).andReturn(org.apache.ambari.server.state.HostComponentAdminState.INSERVICE).anyTimes();
        final org.apache.ambari.server.state.ServiceComponentHost mrtt7ServiceComponentHost = createMock(org.apache.ambari.server.state.ServiceComponentHost.class);
        EasyMock.expect(mrtt7ServiceComponentHost.getComponentAdminState()).andReturn(org.apache.ambari.server.state.HostComponentAdminState.INSERVICE).anyTimes();
        final org.apache.ambari.server.state.ServiceComponentHost mrtt9ServiceComponentHost = createMock(org.apache.ambari.server.state.ServiceComponentHost.class);
        EasyMock.expect(mrtt9ServiceComponentHost.getComponentAdminState()).andReturn(org.apache.ambari.server.state.HostComponentAdminState.INSERVICE).anyTimes();
        final org.apache.ambari.server.state.ServiceComponentHost nns7ServiceComponentHost = createMock(org.apache.ambari.server.state.ServiceComponentHost.class);
        EasyMock.expect(nns7ServiceComponentHost.getComponentAdminState()).andReturn(org.apache.ambari.server.state.HostComponentAdminState.INSERVICE).anyTimes();
        java.util.Map<java.lang.String, java.util.Collection<java.lang.String>> projectedTopology = new java.util.HashMap<>();
        final java.util.HashMap<java.lang.String, org.apache.ambari.server.state.ServiceComponentHost> nnServiceComponentHosts = new java.util.HashMap<java.lang.String, org.apache.ambari.server.state.ServiceComponentHost>() {
            {
                put("h0", nnh0ServiceComponentHost);
            }
        };
        insertTopology(projectedTopology, "NAMENODE", nnServiceComponentHosts.keySet());
        final java.util.HashMap<java.lang.String, org.apache.ambari.server.state.ServiceComponentHost> snnServiceComponentHosts = new java.util.HashMap<java.lang.String, org.apache.ambari.server.state.ServiceComponentHost>() {
            {
                put("h1", snnh1ServiceComponentHost);
            }
        };
        insertTopology(projectedTopology, "SECONDARY_NAMENODE", snnServiceComponentHosts.keySet());
        final java.util.Map<java.lang.String, org.apache.ambari.server.state.ServiceComponentHost> dnServiceComponentHosts = new java.util.HashMap<java.lang.String, org.apache.ambari.server.state.ServiceComponentHost>() {
            {
                put("h0", dnh0ServiceComponentHost);
                put("h1", dnh1ServiceComponentHost);
                put("h2", dnh2ServiceComponentHost);
                put("h3", dnh3ServiceComponentHost);
                put("h5", dnh5ServiceComponentHost);
                put("h7", dnh7ServiceComponentHost);
                put("h8", dnh8ServiceComponentHost);
                put("h9", dnh9ServiceComponentHost);
            }
        };
        insertTopology(projectedTopology, "DATANODE", dnServiceComponentHosts.keySet());
        final java.util.Map<java.lang.String, org.apache.ambari.server.state.ServiceComponentHost> hbmServiceComponentHosts = new java.util.HashMap<java.lang.String, org.apache.ambari.server.state.ServiceComponentHost>() {
            {
                put("h5", hbm5ServiceComponentHost);
            }
        };
        insertTopology(projectedTopology, "HBASE_MASTER", hbmServiceComponentHosts.keySet());
        final java.util.Map<java.lang.String, org.apache.ambari.server.state.ServiceComponentHost> hbrsServiceComponentHosts = new java.util.HashMap<java.lang.String, org.apache.ambari.server.state.ServiceComponentHost>() {
            {
                put("h1", hbrs1ServiceComponentHost);
                put("h3", hbrs3ServiceComponentHost);
                put("h5", hbrs5ServiceComponentHost);
                put("h8", hbrs8ServiceComponentHost);
                put("h9", hbrs9ServiceComponentHost);
            }
        };
        insertTopology(projectedTopology, "HBASE_REGIONSERVER", hbrsServiceComponentHosts.keySet());
        final java.util.Map<java.lang.String, org.apache.ambari.server.state.ServiceComponentHost> mrjtServiceComponentHosts = new java.util.HashMap<java.lang.String, org.apache.ambari.server.state.ServiceComponentHost>() {
            {
                put("h5", mrjt5ServiceComponentHost);
            }
        };
        insertTopology(projectedTopology, "JOBTRACKER", mrjtServiceComponentHosts.keySet());
        final java.util.Map<java.lang.String, org.apache.ambari.server.state.ServiceComponentHost> mrttServiceComponentHosts = new java.util.HashMap<java.lang.String, org.apache.ambari.server.state.ServiceComponentHost>() {
            {
                put("h1", mrtt1ServiceComponentHost);
                put("h2", mrtt2ServiceComponentHost);
                put("h3", mrtt3ServiceComponentHost);
                put("h4", mrtt4ServiceComponentHost);
                put("h5", mrtt5ServiceComponentHost);
                put("h7", mrtt7ServiceComponentHost);
                put("h9", mrtt9ServiceComponentHost);
            }
        };
        insertTopology(projectedTopology, "TASKTRACKER", mrttServiceComponentHosts.keySet());
        final java.util.Map<java.lang.String, org.apache.ambari.server.state.ServiceComponentHost> nnsServiceComponentHosts = new java.util.HashMap<java.lang.String, org.apache.ambari.server.state.ServiceComponentHost>() {
            {
                put("h7", nns7ServiceComponentHost);
            }
        };
        insertTopology(projectedTopology, "NONAME_SERVER", nnsServiceComponentHosts.keySet());
        final org.apache.ambari.server.state.ServiceComponent nnComponent = createMock(org.apache.ambari.server.state.ServiceComponent.class);
        EasyMock.expect(nnComponent.getName()).andReturn("NAMENODE").anyTimes();
        EasyMock.expect(nnComponent.getServiceComponentHost(EasyMock.anyObject(java.lang.String.class))).andAnswer(new org.easymock.IAnswer<org.apache.ambari.server.state.ServiceComponentHost>() {
            @java.lang.Override
            public org.apache.ambari.server.state.ServiceComponentHost answer() throws java.lang.Throwable {
                java.lang.Object[] args = EasyMock.getCurrentArguments();
                return nnServiceComponentHosts.get(args[0]);
            }
        }).anyTimes();
        EasyMock.expect(nnComponent.getServiceComponentHosts()).andReturn(nnServiceComponentHosts).anyTimes();
        EasyMock.expect(nnComponent.isClientComponent()).andReturn(false).anyTimes();
        final org.apache.ambari.server.state.ServiceComponent snnComponent = createMock(org.apache.ambari.server.state.ServiceComponent.class);
        EasyMock.expect(snnComponent.getName()).andReturn("SECONDARY_NAMENODE").anyTimes();
        EasyMock.expect(snnComponent.getServiceComponentHost(EasyMock.anyObject(java.lang.String.class))).andAnswer(new org.easymock.IAnswer<org.apache.ambari.server.state.ServiceComponentHost>() {
            @java.lang.Override
            public org.apache.ambari.server.state.ServiceComponentHost answer() throws java.lang.Throwable {
                java.lang.Object[] args = EasyMock.getCurrentArguments();
                return snnServiceComponentHosts.get(args[0]);
            }
        }).anyTimes();
        EasyMock.expect(snnComponent.getServiceComponentHosts()).andReturn(snnServiceComponentHosts).anyTimes();
        EasyMock.expect(snnComponent.isClientComponent()).andReturn(false).anyTimes();
        final org.apache.ambari.server.state.ServiceComponent dnComponent = createMock(org.apache.ambari.server.state.ServiceComponent.class);
        EasyMock.expect(dnComponent.getName()).andReturn("DATANODE").anyTimes();
        EasyMock.expect(dnComponent.getServiceComponentHost(EasyMock.anyObject(java.lang.String.class))).andAnswer(new org.easymock.IAnswer<org.apache.ambari.server.state.ServiceComponentHost>() {
            @java.lang.Override
            public org.apache.ambari.server.state.ServiceComponentHost answer() throws java.lang.Throwable {
                java.lang.Object[] args = EasyMock.getCurrentArguments();
                return dnServiceComponentHosts.get(args[0]);
            }
        }).anyTimes();
        EasyMock.expect(dnComponent.getServiceComponentHosts()).andReturn(dnServiceComponentHosts).anyTimes();
        EasyMock.expect(dnComponent.isClientComponent()).andReturn(false).anyTimes();
        final org.apache.ambari.server.state.ServiceComponent hbmComponent = createMock(org.apache.ambari.server.state.ServiceComponent.class);
        EasyMock.expect(hbmComponent.getName()).andReturn("HBASE_MASTER").anyTimes();
        EasyMock.expect(hbmComponent.getServiceComponentHost(EasyMock.anyObject(java.lang.String.class))).andAnswer(new org.easymock.IAnswer<org.apache.ambari.server.state.ServiceComponentHost>() {
            @java.lang.Override
            public org.apache.ambari.server.state.ServiceComponentHost answer() throws java.lang.Throwable {
                java.lang.Object[] args = EasyMock.getCurrentArguments();
                return hbmServiceComponentHosts.get(args[0]);
            }
        }).anyTimes();
        EasyMock.expect(hbmComponent.getServiceComponentHosts()).andReturn(hbmServiceComponentHosts).anyTimes();
        EasyMock.expect(hbmComponent.isClientComponent()).andReturn(false).anyTimes();
        final org.apache.ambari.server.state.ServiceComponent hbrsComponent = createMock(org.apache.ambari.server.state.ServiceComponent.class);
        EasyMock.expect(hbrsComponent.getName()).andReturn("HBASE_REGIONSERVER").anyTimes();
        EasyMock.expect(hbrsComponent.getServiceComponentHost(EasyMock.anyObject(java.lang.String.class))).andAnswer(new org.easymock.IAnswer<org.apache.ambari.server.state.ServiceComponentHost>() {
            @java.lang.Override
            public org.apache.ambari.server.state.ServiceComponentHost answer() throws java.lang.Throwable {
                java.lang.Object[] args = EasyMock.getCurrentArguments();
                return hbrsServiceComponentHosts.get(args[0]);
            }
        }).anyTimes();
        java.util.Map<java.lang.String, org.apache.ambari.server.state.ServiceComponentHost> hbrsHosts = com.google.common.collect.Maps.filterKeys(hbrsServiceComponentHosts, new com.google.common.base.Predicate<java.lang.String>() {
            @java.lang.Override
            public boolean apply(java.lang.String s) {
                return s.equals("h1");
            }
        });
        EasyMock.expect(hbrsComponent.getServiceComponentHosts()).andReturn(hbrsServiceComponentHosts).anyTimes();
        EasyMock.expect(hbrsComponent.isClientComponent()).andReturn(false).anyTimes();
        final org.apache.ambari.server.state.ServiceComponent mrjtComponent = createMock(org.apache.ambari.server.state.ServiceComponent.class);
        EasyMock.expect(mrjtComponent.getName()).andReturn("JOBTRACKER").anyTimes();
        EasyMock.expect(mrjtComponent.getServiceComponentHost(EasyMock.anyObject(java.lang.String.class))).andAnswer(new org.easymock.IAnswer<org.apache.ambari.server.state.ServiceComponentHost>() {
            @java.lang.Override
            public org.apache.ambari.server.state.ServiceComponentHost answer() throws java.lang.Throwable {
                java.lang.Object[] args = EasyMock.getCurrentArguments();
                return mrjtServiceComponentHosts.get(args[0]);
            }
        }).anyTimes();
        EasyMock.expect(mrjtComponent.getServiceComponentHosts()).andReturn(mrjtServiceComponentHosts).anyTimes();
        EasyMock.expect(mrjtComponent.isClientComponent()).andReturn(false).anyTimes();
        final org.apache.ambari.server.state.ServiceComponent mrttCompomnent = createMock(org.apache.ambari.server.state.ServiceComponent.class);
        EasyMock.expect(mrttCompomnent.getName()).andReturn("TASKTRACKER").anyTimes();
        EasyMock.expect(mrttCompomnent.getServiceComponentHost(EasyMock.anyObject(java.lang.String.class))).andAnswer(new org.easymock.IAnswer<org.apache.ambari.server.state.ServiceComponentHost>() {
            @java.lang.Override
            public org.apache.ambari.server.state.ServiceComponentHost answer() throws java.lang.Throwable {
                java.lang.Object[] args = EasyMock.getCurrentArguments();
                return mrttServiceComponentHosts.get(args[0]);
            }
        }).anyTimes();
        EasyMock.expect(mrttCompomnent.getServiceComponentHosts()).andReturn(mrttServiceComponentHosts).anyTimes();
        EasyMock.expect(mrttCompomnent.isClientComponent()).andReturn(false).anyTimes();
        final org.apache.ambari.server.state.ServiceComponent nnsComponent = createMock(org.apache.ambari.server.state.ServiceComponent.class);
        EasyMock.expect(nnsComponent.getName()).andReturn("NONAME_SERVER").anyTimes();
        EasyMock.expect(nnsComponent.getServiceComponentHost(EasyMock.anyObject(java.lang.String.class))).andAnswer(new org.easymock.IAnswer<org.apache.ambari.server.state.ServiceComponentHost>() {
            @java.lang.Override
            public org.apache.ambari.server.state.ServiceComponentHost answer() throws java.lang.Throwable {
                java.lang.Object[] args = EasyMock.getCurrentArguments();
                return nnsServiceComponentHosts.get(args[0]);
            }
        }).anyTimes();
        EasyMock.expect(nnsComponent.getServiceComponentHosts()).andReturn(nnsServiceComponentHosts).anyTimes();
        EasyMock.expect(nnsComponent.isClientComponent()).andReturn(false).anyTimes();
        final org.apache.ambari.server.state.Service hdfsService = createMock(org.apache.ambari.server.state.Service.class);
        EasyMock.expect(hdfsService.getServiceComponents()).andReturn(new java.util.HashMap<java.lang.String, org.apache.ambari.server.state.ServiceComponent>() {
            {
                put("NAMENODE", nnComponent);
                put("SECONDARY_NAMENODE", snnComponent);
                put("DATANODE", dnComponent);
            }
        }).anyTimes();
        final org.apache.ambari.server.state.Service hbaseService = createMock(org.apache.ambari.server.state.Service.class);
        EasyMock.expect(hbaseService.getServiceComponents()).andReturn(new java.util.HashMap<java.lang.String, org.apache.ambari.server.state.ServiceComponent>() {
            {
                put("HBASE_MASTER", hbmComponent);
                put("HBASE_REGIONSERVER", hbrsComponent);
            }
        }).anyTimes();
        final org.apache.ambari.server.state.Service mrService = createMock(org.apache.ambari.server.state.Service.class);
        EasyMock.expect(mrService.getServiceComponents()).andReturn(new java.util.HashMap<java.lang.String, org.apache.ambari.server.state.ServiceComponent>() {
            {
                put("JOBTRACKER", mrjtComponent);
                put("TASKTRACKER", mrttCompomnent);
            }
        }).anyTimes();
        final org.apache.ambari.server.state.Service nnService = createMock(org.apache.ambari.server.state.Service.class);
        EasyMock.expect(nnService.getServiceComponents()).andReturn(new java.util.HashMap<java.lang.String, org.apache.ambari.server.state.ServiceComponent>() {
            {
                put("NONAME_SERVER", nnsComponent);
            }
        }).anyTimes();
        org.apache.ambari.server.state.Cluster cluster = createMock(org.apache.ambari.server.state.Cluster.class);
        EasyMock.expect(cluster.getHosts()).andReturn(hosts).anyTimes();
        EasyMock.expect(cluster.getServices()).andReturn(new java.util.HashMap<java.lang.String, org.apache.ambari.server.state.Service>() {
            {
                put("HDFS", hdfsService);
                put("HBASE", hbaseService);
                put("MAPREDUCE", mrService);
                put("NONAME", nnService);
            }
        }).anyTimes();
        final org.apache.ambari.server.topology.TopologyManager topologyManager = injector.getInstance(org.apache.ambari.server.topology.TopologyManager.class);
        topologyManager.getPendingHostComponents();
        EasyMock.expectLastCall().andReturn(projectedTopology).once();
        replayAll();
        injector.getInstance(org.apache.ambari.server.api.services.AmbariMetaInfo.class).init();
        java.util.Map<java.lang.String, java.util.Set<java.lang.String>> info = org.apache.ambari.server.utils.StageUtils.getClusterHostInfo(cluster);
        verifyAll();
        java.util.Set<java.lang.String> allHosts = info.get(org.apache.ambari.server.utils.StageUtils.HOSTS_LIST);
        org.junit.Assert.assertEquals(hosts.size(), allHosts.size());
        for (org.apache.ambari.server.state.Host host : hosts) {
            org.junit.Assert.assertTrue(allHosts.contains(host.getHostName()));
        }
        checkServiceHostIndexes(info, "DATANODE", "slave_hosts", projectedTopology, hostNames);
        checkServiceHostIndexes(info, "NAMENODE", "namenode_host", projectedTopology, hostNames);
        checkServiceHostIndexes(info, "SECONDARY_NAMENODE", "snamenode_host", projectedTopology, hostNames);
        checkServiceHostIndexes(info, "HBASE_MASTER", "hbase_master_hosts", projectedTopology, hostNames);
        checkServiceHostIndexes(info, "HBASE_REGIONSERVER", "hbase_rs_hosts", projectedTopology, hostNames);
        checkServiceHostIndexes(info, "JOBTRACKER", "jtnode_host", projectedTopology, hostNames);
        checkServiceHostIndexes(info, "TASKTRACKER", "mapred_tt_hosts", projectedTopology, hostNames);
        checkServiceHostIndexes(info, "NONAME_SERVER", "noname_server_hosts", projectedTopology, hostNames);
        java.util.Set<java.lang.String> actualPingPorts = info.get(org.apache.ambari.server.utils.StageUtils.PORTS);
        if (pingPorts.contains(null)) {
            org.junit.Assert.assertEquals(new java.util.HashSet<>(pingPorts).size(), actualPingPorts.size() + 1);
        } else {
            org.junit.Assert.assertEquals(new java.util.HashSet<>(pingPorts).size(), actualPingPorts.size());
        }
        java.util.List<java.lang.Integer> pingPortsActual = getRangeMappedDecompressedSet(actualPingPorts);
        java.util.List<java.lang.Integer> reindexedPorts = getReindexedList(pingPortsActual, new java.util.ArrayList<>(allHosts), hostNames);
        java.util.List<java.lang.Integer> expectedPingPorts = new java.util.ArrayList<>(pingPorts);
        for (int i = 0; i < expectedPingPorts.size(); i++) {
            if (expectedPingPorts.get(i) == null) {
                expectedPingPorts.set(i, org.apache.ambari.server.utils.StageUtils.DEFAULT_PING_PORT);
            }
        }
        org.junit.Assert.assertEquals(expectedPingPorts, reindexedPorts);
        org.junit.Assert.assertTrue(info.containsKey(org.apache.ambari.server.utils.StageUtils.AMBARI_SERVER_HOST));
        java.util.Set<java.lang.String> serverHost = info.get(org.apache.ambari.server.utils.StageUtils.AMBARI_SERVER_HOST);
        org.junit.Assert.assertEquals(1, serverHost.size());
        org.junit.Assert.assertEquals(org.apache.ambari.server.utils.StageUtils.getHostName(), serverHost.iterator().next());
        org.junit.Assert.assertTrue(getDecompressedSet(info.get("hbase_regionserver_hosts")).contains(9));
        info = org.apache.ambari.server.utils.StageUtils.substituteHostIndexes(info);
        checkServiceHostNames(info, "DATANODE", projectedTopology);
        checkServiceHostNames(info, "NAMENODE", projectedTopology);
        checkServiceHostNames(info, "SECONDARY_NAMENODE", projectedTopology);
        checkServiceHostNames(info, "HBASE_MASTER", projectedTopology);
        checkServiceHostNames(info, "HBASE_REGIONSERVER", projectedTopology);
        checkServiceHostNames(info, "JOBTRACKER", projectedTopology);
        checkServiceHostNames(info, "TASKTRACKER", projectedTopology);
        checkServiceHostNames(info, "NONAME_SERVER", projectedTopology);
    }

    private void insertTopology(java.util.Map<java.lang.String, java.util.Collection<java.lang.String>> projectedTopology, java.lang.String componentName, java.util.Set<java.lang.String> hostNames) {
        if (hostNames != null) {
            for (java.lang.String hostname : hostNames) {
                java.util.Collection<java.lang.String> components = projectedTopology.get(hostname);
                if (components == null) {
                    components = new java.util.HashSet<>();
                    projectedTopology.put(hostname, components);
                }
                components.add(componentName);
            }
        }
    }

    @org.junit.Test
    public void testUseAmbariJdkWithoutavaHome() {
        java.util.Map<java.lang.String, java.lang.String> commandParams = new java.util.HashMap<>();
        org.apache.ambari.server.configuration.Configuration configuration = new org.apache.ambari.server.configuration.Configuration();
        org.apache.ambari.server.utils.StageUtils.useAmbariJdkInCommandParams(commandParams, configuration);
        org.junit.Assert.assertTrue(commandParams.isEmpty());
    }

    @org.junit.Test
    public void testUseAmbariJdkWithCustomJavaHome() {
        java.util.Map<java.lang.String, java.lang.String> commandParams = new java.util.HashMap<>();
        org.apache.ambari.server.configuration.Configuration configuration = new org.apache.ambari.server.configuration.Configuration();
        configuration.setProperty("java.home", "myJavaHome");
        org.apache.ambari.server.utils.StageUtils.useAmbariJdkInCommandParams(commandParams, configuration);
        org.junit.Assert.assertEquals("myJavaHome", commandParams.get("ambari_java_home"));
        org.junit.Assert.assertEquals(2, commandParams.size());
    }

    @org.junit.Test
    public void testUseAmbariJdk() {
        java.util.Map<java.lang.String, java.lang.String> commandParams = new java.util.HashMap<>();
        org.apache.ambari.server.configuration.Configuration configuration = new org.apache.ambari.server.configuration.Configuration();
        configuration.setProperty("java.home", "myJavaHome");
        configuration.setProperty("jdk.name", "myJdkName");
        configuration.setProperty("jce.name", "myJceName");
        org.apache.ambari.server.utils.StageUtils.useAmbariJdkInCommandParams(commandParams, configuration);
        org.junit.Assert.assertEquals("myJavaHome", commandParams.get("ambari_java_home"));
        org.junit.Assert.assertEquals("myJdkName", commandParams.get("ambari_jdk_name"));
        org.junit.Assert.assertEquals("myJceName", commandParams.get("ambari_jce_name"));
        org.junit.Assert.assertEquals(4, commandParams.size());
    }

    @org.junit.Test
    public void testUseStackJdkIfExistsWithCustomStackJdk() {
        java.util.Map<java.lang.String, java.lang.String> hostLevelParams = new java.util.HashMap<>();
        org.apache.ambari.server.configuration.Configuration configuration = new org.apache.ambari.server.configuration.Configuration();
        configuration.setProperty("java.home", "myJavaHome");
        configuration.setProperty("jdk.name", "myJdkName");
        configuration.setProperty("jce.name", "myJceName");
        configuration.setProperty("stack.java.home", "myStackJavaHome");
        org.apache.ambari.server.utils.StageUtils.useStackJdkIfExists(hostLevelParams, configuration);
        org.junit.Assert.assertEquals("myStackJavaHome", hostLevelParams.get("java_home"));
        org.junit.Assert.assertNull(hostLevelParams.get("jdk_name"));
        org.junit.Assert.assertNull(hostLevelParams.get("jce_name"));
        org.junit.Assert.assertEquals(4, hostLevelParams.size());
    }

    @org.junit.Test
    public void testUseStackJdkIfExists() {
        java.util.Map<java.lang.String, java.lang.String> hostLevelParams = new java.util.HashMap<>();
        org.apache.ambari.server.configuration.Configuration configuration = new org.apache.ambari.server.configuration.Configuration();
        configuration.setProperty("java.home", "myJavaHome");
        configuration.setProperty("jdk.name", "myJdkName");
        configuration.setProperty("jce.name", "myJceName");
        configuration.setProperty("stack.java.home", "myStackJavaHome");
        configuration.setProperty("stack.jdk.name", "myStackJdkName");
        configuration.setProperty("stack.jce.name", "myStackJceName");
        configuration.setProperty("stack.java.version", "7");
        org.apache.ambari.server.utils.StageUtils.useStackJdkIfExists(hostLevelParams, configuration);
        org.junit.Assert.assertEquals("myStackJavaHome", hostLevelParams.get("java_home"));
        org.junit.Assert.assertEquals("myStackJdkName", hostLevelParams.get("jdk_name"));
        org.junit.Assert.assertEquals("myStackJceName", hostLevelParams.get("jce_name"));
        org.junit.Assert.assertEquals("7", hostLevelParams.get("java_version"));
        org.junit.Assert.assertEquals(4, hostLevelParams.size());
    }

    @org.junit.Test
    public void testUseStackJdkIfExistsWithoutStackJdk() {
        java.util.Map<java.lang.String, java.lang.String> hostLevelParams = new java.util.HashMap<>();
        org.apache.ambari.server.configuration.Configuration configuration = new org.apache.ambari.server.configuration.Configuration();
        configuration.setProperty("java.home", "myJavaHome");
        configuration.setProperty("jdk.name", "myJdkName");
        configuration.setProperty("jce.name", "myJceName");
        org.apache.ambari.server.utils.StageUtils.useStackJdkIfExists(hostLevelParams, configuration);
        org.junit.Assert.assertEquals("myJavaHome", hostLevelParams.get("java_home"));
        org.junit.Assert.assertEquals("myJdkName", hostLevelParams.get("jdk_name"));
        org.junit.Assert.assertEquals("myJceName", hostLevelParams.get("jce_name"));
        org.junit.Assert.assertEquals(4, hostLevelParams.size());
    }

    private void checkServiceHostIndexes(java.util.Map<java.lang.String, java.util.Set<java.lang.String>> info, java.lang.String componentName, java.lang.String mappedComponentName, java.util.Map<java.lang.String, java.util.Collection<java.lang.String>> serviceTopology, java.util.List<java.lang.String> hostList) {
        java.util.Set<java.lang.Integer> expectedHostsList = new java.util.HashSet<>();
        java.util.Set<java.lang.Integer> actualHostsList = new java.util.HashSet<>();
        for (java.util.Map.Entry<java.lang.String, java.util.Collection<java.lang.String>> entry : serviceTopology.entrySet()) {
            if (entry.getValue().contains(componentName)) {
                expectedHostsList.add(hostList.indexOf(entry.getKey()));
            }
        }
        java.util.Set<java.lang.String> hosts = info.get(org.apache.ambari.server.utils.StageUtils.getClusterHostInfoKey(componentName));
        if (hosts != null) {
            actualHostsList.addAll(getDecompressedSet(hosts));
        }
        org.junit.Assert.assertEquals(expectedHostsList, actualHostsList);
    }

    private void checkServiceHostNames(java.util.Map<java.lang.String, java.util.Set<java.lang.String>> info, java.lang.String componentName, java.util.Map<java.lang.String, java.util.Collection<java.lang.String>> serviceTopology) {
        java.util.Set<java.lang.String> expectedHostsList = new java.util.HashSet<>();
        java.util.Set<java.lang.String> actualHostsList = new java.util.HashSet<>();
        for (java.util.Map.Entry<java.lang.String, java.util.Collection<java.lang.String>> entry : serviceTopology.entrySet()) {
            if (entry.getValue().contains(componentName)) {
                expectedHostsList.add(entry.getKey());
            }
        }
        java.util.Set<java.lang.String> hosts = info.get(org.apache.ambari.server.utils.StageUtils.getClusterHostInfoKey(componentName));
        if (hosts != null) {
            actualHostsList.addAll(hosts);
        }
        org.junit.Assert.assertEquals(expectedHostsList, actualHostsList);
    }

    private java.util.Set<java.lang.Integer> getDecompressedSet(java.util.Set<java.lang.String> set) {
        java.util.Set<java.lang.Integer> resultSet = new java.util.HashSet<>();
        for (java.lang.String index : set) {
            java.lang.String[] ranges = index.split(",");
            for (java.lang.String r : ranges) {
                java.lang.String[] split = r.split("-");
                if (split.length == 2) {
                    java.lang.Integer start = java.lang.Integer.valueOf(split[0]);
                    java.lang.Integer end = java.lang.Integer.valueOf(split[1]);
                    com.google.common.collect.ContiguousSet<java.lang.Integer> rangeSet = com.google.common.collect.ContiguousSet.create(com.google.common.collect.Range.closed(start, end), com.google.common.collect.DiscreteDomain.integers());
                    for (java.lang.Integer i : rangeSet) {
                        resultSet.add(i);
                    }
                } else {
                    resultSet.add(java.lang.Integer.valueOf(split[0]));
                }
            }
        }
        return resultSet;
    }

    private java.util.List<java.lang.Integer> getRangeMappedDecompressedSet(java.util.Set<java.lang.String> compressedSet) {
        java.util.SortedMap<java.lang.Integer, java.lang.Integer> resultMap = new java.util.TreeMap<>();
        for (java.lang.String token : compressedSet) {
            java.lang.String[] split = token.split(":");
            if (split.length != 2) {
                throw new java.lang.RuntimeException("Broken data, expected format - m:r, got - " + token);
            }
            java.lang.Integer index = java.lang.Integer.valueOf(split[0]);
            java.lang.String rangeTokens = split[1];
            java.util.Set<java.lang.String> rangeTokensSet = new java.util.HashSet<>(java.util.Arrays.asList(rangeTokens.split(",")));
            java.util.Set<java.lang.Integer> decompressedSet = getDecompressedSet(rangeTokensSet);
            for (java.lang.Integer i : decompressedSet) {
                resultMap.put(i, index);
            }
        }
        java.util.List<java.lang.Integer> resultList = new java.util.ArrayList<>(resultMap.values());
        return resultList;
    }

    private java.util.List<java.lang.Integer> getReindexedList(java.util.List<java.lang.Integer> list, java.util.List<java.lang.String> currentIndexes, java.util.List<java.lang.String> desiredIndexes) {
        java.util.SortedMap<java.lang.Integer, java.lang.Integer> sortedMap = new java.util.TreeMap<>();
        int index = 0;
        for (java.lang.Integer value : list) {
            java.lang.String currentIndexValue = currentIndexes.get(index);
            java.lang.Integer desiredIndexValue = desiredIndexes.indexOf(currentIndexValue);
            sortedMap.put(desiredIndexValue, value);
            index++;
        }
        return new java.util.ArrayList<>(sortedMap.values());
    }

    private java.lang.String getHostName() {
        java.lang.String hostname;
        try {
            hostname = java.net.InetAddress.getLocalHost().getHostName();
        } catch (java.net.UnknownHostException e) {
            hostname = "host-dummy";
        }
        return hostname;
    }
}