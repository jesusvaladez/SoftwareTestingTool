package org.apache.ambari.server.update;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import com.google.inject.persist.UnitOfWork;
import javax.persistence.EntityManager;
import org.easymock.Capture;
import org.easymock.EasyMock;
import org.easymock.EasyMockSupport;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.StandardPasswordEncoder;
import static org.easymock.EasyMock.anyString;
import static org.easymock.EasyMock.createNiceMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
public class HostUpdateHelperTest {
    @org.junit.Test
    public void testValidateHostChanges_Success() throws java.lang.Exception {
        org.easymock.EasyMockSupport easyMockSupport = new org.easymock.EasyMockSupport();
        final org.apache.ambari.server.controller.AmbariManagementController mockAmbariManagementController = easyMockSupport.createNiceMock(org.apache.ambari.server.controller.AmbariManagementController.class);
        org.apache.ambari.server.state.Clusters mockClusters = easyMockSupport.createStrictMock(org.apache.ambari.server.state.Clusters.class);
        org.apache.ambari.server.state.Cluster mockCluster = easyMockSupport.createNiceMock(org.apache.ambari.server.state.Cluster.class);
        org.apache.ambari.server.state.Host host1 = easyMockSupport.createNiceMock(org.apache.ambari.server.state.Host.class);
        org.apache.ambari.server.state.Host host2 = easyMockSupport.createNiceMock(org.apache.ambari.server.state.Host.class);
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> clusterHostsToChange = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.lang.String> hosts = new java.util.HashMap<>();
        java.util.List<org.apache.ambari.server.state.Host> clusterHosts = new java.util.ArrayList<>();
        final com.google.inject.Injector mockInjector = com.google.inject.Guice.createInjector(new com.google.inject.AbstractModule() {
            @java.lang.Override
            protected void configure() {
                bind(org.apache.ambari.server.controller.AmbariManagementController.class).toInstance(mockAmbariManagementController);
            }
        });
        clusterHosts.add(host1);
        clusterHosts.add(host2);
        hosts.put("host1", "host10");
        hosts.put("host2", "host11");
        clusterHostsToChange.put("cl1", hosts);
        EasyMock.expect(mockAmbariManagementController.getClusters()).andReturn(mockClusters).once();
        EasyMock.expect(mockClusters.getCluster("cl1")).andReturn(mockCluster).once();
        EasyMock.expect(mockCluster.getHosts()).andReturn(clusterHosts).once();
        EasyMock.expect(host1.getHostName()).andReturn("host1");
        EasyMock.expect(host2.getHostName()).andReturn("host2");
        org.apache.ambari.server.update.HostUpdateHelper hostUpdateHelper = new org.apache.ambari.server.update.HostUpdateHelper(null, null, mockInjector);
        hostUpdateHelper.setHostChangesFileMap(clusterHostsToChange);
        easyMockSupport.replayAll();
        hostUpdateHelper.validateHostChanges();
        easyMockSupport.verifyAll();
    }

    @org.junit.Test
    public void testValidateHostChanges_InvalidHost() throws java.lang.Exception {
        org.easymock.EasyMockSupport easyMockSupport = new org.easymock.EasyMockSupport();
        final org.apache.ambari.server.controller.AmbariManagementController mockAmbariManagementController = easyMockSupport.createNiceMock(org.apache.ambari.server.controller.AmbariManagementController.class);
        org.apache.ambari.server.state.Clusters mockClusters = easyMockSupport.createStrictMock(org.apache.ambari.server.state.Clusters.class);
        org.apache.ambari.server.state.Cluster mockCluster = easyMockSupport.createNiceMock(org.apache.ambari.server.state.Cluster.class);
        org.apache.ambari.server.state.Host host1 = easyMockSupport.createNiceMock(org.apache.ambari.server.state.Host.class);
        org.apache.ambari.server.state.Host host2 = easyMockSupport.createNiceMock(org.apache.ambari.server.state.Host.class);
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> clusterHostsToChange = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.lang.String> hosts = new java.util.HashMap<>();
        java.util.List<org.apache.ambari.server.state.Host> clusterHosts = new java.util.ArrayList<>();
        final com.google.inject.Injector mockInjector = com.google.inject.Guice.createInjector(new com.google.inject.AbstractModule() {
            @java.lang.Override
            protected void configure() {
                bind(org.apache.ambari.server.controller.AmbariManagementController.class).toInstance(mockAmbariManagementController);
            }
        });
        clusterHosts.add(host1);
        clusterHosts.add(host2);
        hosts.put("host1", "host10");
        hosts.put("host3", "host11");
        clusterHostsToChange.put("cl1", hosts);
        EasyMock.expect(mockAmbariManagementController.getClusters()).andReturn(mockClusters).once();
        EasyMock.expect(mockClusters.getCluster("cl1")).andReturn(mockCluster).once();
        EasyMock.expect(mockCluster.getHosts()).andReturn(clusterHosts).once();
        EasyMock.expect(host1.getHostName()).andReturn("host1");
        EasyMock.expect(host2.getHostName()).andReturn("host2");
        org.apache.ambari.server.update.HostUpdateHelper hostUpdateHelper = new org.apache.ambari.server.update.HostUpdateHelper(null, null, mockInjector);
        hostUpdateHelper.setHostChangesFileMap(clusterHostsToChange);
        easyMockSupport.replayAll();
        try {
            hostUpdateHelper.validateHostChanges();
        } catch (org.apache.ambari.server.AmbariException e) {
            assert e.getMessage().equals("Hostname(s): host3 was(were) not found.");
        }
        easyMockSupport.verifyAll();
    }

    @org.junit.Test
    public void testValidateHostChanges_InvalidCluster() throws java.lang.Exception {
        org.easymock.EasyMockSupport easyMockSupport = new org.easymock.EasyMockSupport();
        final org.apache.ambari.server.controller.AmbariManagementController mockAmbariManagementController = easyMockSupport.createNiceMock(org.apache.ambari.server.controller.AmbariManagementController.class);
        org.apache.ambari.server.state.Clusters mockClusters = easyMockSupport.createStrictMock(org.apache.ambari.server.state.Clusters.class);
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> clusterHostsToChange = new java.util.HashMap<>();
        final com.google.inject.Injector mockInjector = com.google.inject.Guice.createInjector(new com.google.inject.AbstractModule() {
            @java.lang.Override
            protected void configure() {
                bind(org.apache.ambari.server.controller.AmbariManagementController.class).toInstance(mockAmbariManagementController);
            }
        });
        clusterHostsToChange.put("cl1", null);
        EasyMock.expect(mockAmbariManagementController.getClusters()).andReturn(mockClusters).once();
        EasyMock.expect(mockClusters.getCluster("cl1")).andReturn(null).once();
        org.apache.ambari.server.update.HostUpdateHelper hostUpdateHelper = new org.apache.ambari.server.update.HostUpdateHelper(null, null, mockInjector);
        hostUpdateHelper.setHostChangesFileMap(clusterHostsToChange);
        easyMockSupport.replayAll();
        try {
            hostUpdateHelper.validateHostChanges();
        } catch (org.apache.ambari.server.AmbariException e) {
            assert e.getMessage().equals("Cluster cl1 was not found.");
        }
        easyMockSupport.verifyAll();
    }

    @org.junit.Test
    public void testValidateHostChanges_HostChangesNull() throws java.lang.Exception {
        org.apache.ambari.server.update.HostUpdateHelper hostUpdateHelper = new org.apache.ambari.server.update.HostUpdateHelper(null, null, null);
        hostUpdateHelper.setHostChangesFileMap(null);
        try {
            hostUpdateHelper.validateHostChanges();
        } catch (org.apache.ambari.server.AmbariException e) {
            assert e.getMessage().equals("File with host names changes is null or empty");
        }
    }

    @org.junit.Test
    public void testUpdateHostsInConfigurations() throws org.apache.ambari.server.AmbariException {
        org.easymock.EasyMockSupport easyMockSupport = new org.easymock.EasyMockSupport();
        final org.apache.ambari.server.controller.AmbariManagementController mockAmbariManagementController = easyMockSupport.createNiceMock(org.apache.ambari.server.controller.AmbariManagementController.class);
        final org.apache.ambari.server.orm.dao.ClusterDAO mockClusterDAO = easyMockSupport.createNiceMock(org.apache.ambari.server.orm.dao.ClusterDAO.class);
        final javax.persistence.EntityManager entityManager = EasyMock.createNiceMock(javax.persistence.EntityManager.class);
        final org.apache.ambari.server.orm.DBAccessor dbAccessor = EasyMock.createNiceMock(org.apache.ambari.server.orm.DBAccessor.class);
        org.apache.ambari.server.state.Clusters mockClusters = easyMockSupport.createStrictMock(org.apache.ambari.server.state.Clusters.class);
        org.apache.ambari.server.state.Cluster mockCluster = easyMockSupport.createNiceMock(org.apache.ambari.server.state.Cluster.class);
        org.apache.ambari.server.orm.entities.ClusterEntity mockClusterEntity1 = easyMockSupport.createNiceMock(org.apache.ambari.server.orm.entities.ClusterEntity.class);
        org.apache.ambari.server.orm.entities.ClusterConfigEntity mockClusterConfigEntity1 = easyMockSupport.createNiceMock(org.apache.ambari.server.orm.entities.ClusterConfigEntity.class);
        org.apache.ambari.server.orm.entities.ClusterConfigEntity mockClusterConfigEntity2 = easyMockSupport.createNiceMock(org.apache.ambari.server.orm.entities.ClusterConfigEntity.class);
        org.apache.ambari.server.orm.entities.StackEntity mockStackEntity = easyMockSupport.createNiceMock(org.apache.ambari.server.orm.entities.StackEntity.class);
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> clusterHostsToChange = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.lang.String> hosts = new java.util.HashMap<>();
        java.util.List<org.apache.ambari.server.orm.entities.ClusterConfigEntity> clusterConfigEntities1 = new java.util.ArrayList<>();
        final com.google.inject.Injector mockInjector = com.google.inject.Guice.createInjector(new com.google.inject.AbstractModule() {
            @java.lang.Override
            protected void configure() {
                org.apache.ambari.server.testutils.PartialNiceMockBinder.newBuilder().addClustersBinding(mockAmbariManagementController).addLdapBindings().build().configure(binder());
                bind(org.apache.ambari.server.stack.StackManagerFactory.class).toInstance(easyMockSupport.createNiceMock(org.apache.ambari.server.stack.StackManagerFactory.class));
                bind(org.apache.ambari.server.orm.DBAccessor.class).toInstance(dbAccessor);
                bind(javax.persistence.EntityManager.class).toInstance(entityManager);
                bind(org.apache.ambari.server.state.stack.OsFamily.class).toInstance(EasyMock.createNiceMock(org.apache.ambari.server.state.stack.OsFamily.class));
                bind(org.apache.ambari.server.orm.dao.ClusterDAO.class).toInstance(mockClusterDAO);
                bind(org.apache.ambari.server.state.Clusters.class).toInstance(EasyMock.createNiceMock(org.apache.ambari.server.state.Clusters.class));
                bind(org.apache.ambari.server.mpack.MpackManagerFactory.class).toInstance(easyMockSupport.createNiceMock(org.apache.ambari.server.mpack.MpackManagerFactory.class));
                bind(org.apache.ambari.server.api.services.AmbariMetaInfo.class).toInstance(org.easymock.EasyMock.createNiceMock(org.apache.ambari.server.api.services.AmbariMetaInfo.class));
                bind(org.apache.ambari.server.agent.stomp.AgentConfigsHolder.class).toInstance(org.easymock.EasyMock.createNiceMock(org.apache.ambari.server.agent.stomp.AgentConfigsHolder.class));
                install(new com.google.inject.assistedinject.FactoryModuleBuilder().implement(org.apache.ambari.server.state.Config.class, org.apache.ambari.server.state.ConfigImpl.class).build(org.apache.ambari.server.state.ConfigFactory.class));
            }
        });
        hosts.put("host11", "host55");
        hosts.put("HOST5", "host1");
        hosts.put("host1", "host5");
        hosts.put("host55", "host11");
        clusterConfigEntities1.add(mockClusterConfigEntity1);
        clusterConfigEntities1.add(mockClusterConfigEntity2);
        clusterHostsToChange.put("cl1", hosts);
        EasyMock.expect(mockClusterDAO.findByName("cl1")).andReturn(mockClusterEntity1).atLeastOnce();
        EasyMock.expect(mockAmbariManagementController.getClusters()).andReturn(mockClusters).once();
        EasyMock.expect(mockClusters.getCluster("cl1")).andReturn(mockCluster).once();
        EasyMock.expect(mockCluster.getClusterId()).andReturn(1L).anyTimes();
        org.apache.ambari.server.state.Host host = easyMockSupport.createNiceMock(org.apache.ambari.server.state.Host.class);
        EasyMock.expect(mockCluster.getHost(EasyMock.anyString())).andReturn(host).anyTimes();
        EasyMock.expect(host.getHostId()).andReturn(1L).anyTimes();
        EasyMock.expect(mockClusterEntity1.getClusterConfigEntities()).andReturn(clusterConfigEntities1).atLeastOnce();
        EasyMock.expect(mockClusterConfigEntity1.getClusterId()).andReturn(1L).atLeastOnce();
        EasyMock.expect(mockClusterConfigEntity1.getConfigId()).andReturn(1L).atLeastOnce();
        EasyMock.expect(mockClusterConfigEntity1.getStack()).andReturn(mockStackEntity).atLeastOnce();
        EasyMock.expect(mockClusterConfigEntity1.getData()).andReturn("{\"testProperty1\" : \"testValue_host1\", " + ("\"testProperty2\" : \"testValue_HOST5\", \"testProperty3\" : \"testValue_host11\", " + "\"testProperty4\" : \"testValue_host55\"}")).atLeastOnce();
        EasyMock.expect(mockClusterConfigEntity1.getTag()).andReturn("testTag1").atLeastOnce();
        EasyMock.expect(mockClusterConfigEntity1.getType()).andReturn("testType1").atLeastOnce();
        EasyMock.expect(mockClusterConfigEntity1.getVersion()).andReturn(1L).atLeastOnce();
        EasyMock.expect(mockClusterDAO.findConfig(1L)).andReturn(mockClusterConfigEntity1).atLeastOnce();
        EasyMock.expect(mockClusterConfigEntity2.getClusterId()).andReturn(1L).atLeastOnce();
        EasyMock.expect(mockClusterConfigEntity2.getConfigId()).andReturn(2L).anyTimes();
        EasyMock.expect(mockClusterConfigEntity2.getStack()).andReturn(mockStackEntity).atLeastOnce();
        EasyMock.expect(mockClusterConfigEntity2.getData()).andReturn("{\"testProperty5\" : \"test_host1_test_HOST5_test_host11_test_host55\"}").atLeastOnce();
        EasyMock.expect(mockClusterConfigEntity2.getTag()).andReturn("testTag2").atLeastOnce();
        EasyMock.expect(mockClusterConfigEntity2.getType()).andReturn("testType2").atLeastOnce();
        EasyMock.expect(mockClusterConfigEntity2.getVersion()).andReturn(2L).atLeastOnce();
        EasyMock.expect(mockClusterDAO.findConfig(2L)).andReturn(mockClusterConfigEntity2).atLeastOnce();
        org.easymock.Capture<java.lang.String> dataCapture = org.easymock.EasyMock.newCapture();
        mockClusterConfigEntity1.setData(org.easymock.EasyMock.capture(dataCapture));
        EasyMock.expectLastCall();
        mockClusterConfigEntity2.setData("{\"testProperty5\":\"test_host5_test_host1_test_host55_test_host11\"}");
        EasyMock.expectLastCall();
        org.apache.ambari.server.update.HostUpdateHelper hostUpdateHelper = new org.apache.ambari.server.update.HostUpdateHelper(null, null, mockInjector);
        hostUpdateHelper.setHostChangesFileMap(clusterHostsToChange);
        easyMockSupport.replayAll();
        hostUpdateHelper.updateHostsInConfigurations();
        easyMockSupport.verifyAll();
        junit.framework.Assert.assertTrue(org.apache.ambari.server.utils.CollectionPresentationUtils.isJsonsEquals("{\"testProperty4\":\"testValue_host11\",\"testProperty3\":\"testValue_host55\"," + "\"testProperty2\":\"testValue_host1\",\"testProperty1\":\"testValue_host5\"}", dataCapture.getValue()));
    }

    @org.junit.Test
    public void testCheckForSecurity_SecureCluster() throws org.apache.ambari.server.AmbariException {
        org.easymock.EasyMockSupport easyMockSupport = new org.easymock.EasyMockSupport();
        final org.apache.ambari.server.controller.AmbariManagementController mockAmbariManagementController = easyMockSupport.createNiceMock(org.apache.ambari.server.controller.AmbariManagementController.class);
        org.apache.ambari.server.state.Clusters mockClusters = easyMockSupport.createStrictMock(org.apache.ambari.server.state.Clusters.class);
        org.apache.ambari.server.state.Cluster mockCluster = easyMockSupport.createNiceMock(org.apache.ambari.server.state.Cluster.class);
        org.apache.ambari.server.state.Config mockConfig = easyMockSupport.createNiceMock(org.apache.ambari.server.state.Config.class);
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> clusterHostsToChange = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.lang.String> clusterEnvProperties = new java.util.HashMap<>();
        clusterHostsToChange.put("cl1", null);
        clusterEnvProperties.put("security_enabled", "true");
        final com.google.inject.Injector mockInjector = com.google.inject.Guice.createInjector(new com.google.inject.AbstractModule() {
            @java.lang.Override
            protected void configure() {
                bind(org.apache.ambari.server.controller.AmbariManagementController.class).toInstance(mockAmbariManagementController);
            }
        });
        EasyMock.expect(mockAmbariManagementController.getClusters()).andReturn(mockClusters).once();
        EasyMock.expect(mockClusters.getCluster("cl1")).andReturn(mockCluster).once();
        EasyMock.expect(mockCluster.getDesiredConfigByType("cluster-env")).andReturn(mockConfig).once();
        EasyMock.expect(mockConfig.getProperties()).andReturn(clusterEnvProperties).once();
        org.apache.ambari.server.update.HostUpdateHelper hostUpdateHelper = new org.apache.ambari.server.update.HostUpdateHelper(null, null, mockInjector);
        hostUpdateHelper.setHostChangesFileMap(clusterHostsToChange);
        easyMockSupport.replayAll();
        try {
            hostUpdateHelper.checkForSecurity();
        } catch (org.apache.ambari.server.AmbariException e) {
            assert e.getMessage().equals("Cluster(s) cl1 from file, is(are) in secure mode. Please, turn off security mode.");
        }
        easyMockSupport.verifyAll();
    }

    @org.junit.Test
    public void testCheckForSecurity_NonSecureCluster() throws org.apache.ambari.server.AmbariException {
        org.easymock.EasyMockSupport easyMockSupport = new org.easymock.EasyMockSupport();
        final org.apache.ambari.server.controller.AmbariManagementController mockAmbariManagementController = easyMockSupport.createNiceMock(org.apache.ambari.server.controller.AmbariManagementController.class);
        org.apache.ambari.server.state.Clusters mockClusters = easyMockSupport.createStrictMock(org.apache.ambari.server.state.Clusters.class);
        org.apache.ambari.server.state.Cluster mockCluster = easyMockSupport.createNiceMock(org.apache.ambari.server.state.Cluster.class);
        org.apache.ambari.server.state.Config mockConfig = easyMockSupport.createNiceMock(org.apache.ambari.server.state.Config.class);
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> clusterHostsToChange = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.lang.String> clusterEnvProperties = new java.util.HashMap<>();
        clusterHostsToChange.put("cl1", null);
        clusterEnvProperties.put("security_enabled", "false");
        final com.google.inject.Injector mockInjector = com.google.inject.Guice.createInjector(new com.google.inject.AbstractModule() {
            @java.lang.Override
            protected void configure() {
                bind(org.apache.ambari.server.controller.AmbariManagementController.class).toInstance(mockAmbariManagementController);
            }
        });
        EasyMock.expect(mockAmbariManagementController.getClusters()).andReturn(mockClusters).once();
        EasyMock.expect(mockClusters.getCluster("cl1")).andReturn(mockCluster).once();
        EasyMock.expect(mockCluster.getDesiredConfigByType("cluster-env")).andReturn(mockConfig).once();
        EasyMock.expect(mockConfig.getProperties()).andReturn(clusterEnvProperties).once();
        org.apache.ambari.server.update.HostUpdateHelper hostUpdateHelper = new org.apache.ambari.server.update.HostUpdateHelper(null, null, mockInjector);
        hostUpdateHelper.setHostChangesFileMap(clusterHostsToChange);
        easyMockSupport.replayAll();
        hostUpdateHelper.checkForSecurity();
        easyMockSupport.verifyAll();
    }

    @org.junit.Test
    public void testUpdateHostsInDB() throws org.apache.ambari.server.AmbariException {
        org.easymock.EasyMockSupport easyMockSupport = new org.easymock.EasyMockSupport();
        final org.apache.ambari.server.orm.dao.HostDAO mockHostDAO = easyMockSupport.createNiceMock(org.apache.ambari.server.orm.dao.HostDAO.class);
        final org.apache.ambari.server.orm.dao.ClusterDAO mockClusterDAO = easyMockSupport.createNiceMock(org.apache.ambari.server.orm.dao.ClusterDAO.class);
        final javax.persistence.EntityManager entityManager = EasyMock.createNiceMock(javax.persistence.EntityManager.class);
        final org.apache.ambari.server.orm.DBAccessor dbAccessor = EasyMock.createNiceMock(org.apache.ambari.server.orm.DBAccessor.class);
        org.apache.ambari.server.orm.entities.ClusterEntity mockClusterEntity = easyMockSupport.createNiceMock(org.apache.ambari.server.orm.entities.ClusterEntity.class);
        org.apache.ambari.server.orm.entities.HostEntity mockHostEntity1 = new org.apache.ambari.server.orm.entities.HostEntity();
        org.apache.ambari.server.orm.entities.HostEntity mockHostEntity2 = new org.apache.ambari.server.orm.entities.HostEntity();
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> clusterHostsToChange = new java.util.HashMap<>();
        java.util.List<org.apache.ambari.server.orm.entities.HostEntity> hostEntities = new java.util.ArrayList<>();
        java.util.Map<java.lang.String, java.lang.String> hosts = new java.util.HashMap<>();
        hosts.put("host1", "host10");
        hosts.put("host2", "host11");
        clusterHostsToChange.put("cl1", hosts);
        hostEntities.add(mockHostEntity1);
        hostEntities.add(mockHostEntity2);
        final com.google.inject.Injector mockInjector = com.google.inject.Guice.createInjector(new com.google.inject.AbstractModule() {
            @java.lang.Override
            protected void configure() {
                bind(org.apache.ambari.server.orm.DBAccessor.class).toInstance(dbAccessor);
                bind(javax.persistence.EntityManager.class).toInstance(entityManager);
                bind(org.apache.ambari.server.state.stack.OsFamily.class).toInstance(EasyMock.createNiceMock(org.apache.ambari.server.state.stack.OsFamily.class));
                bind(org.apache.ambari.server.orm.dao.ClusterDAO.class).toInstance(mockClusterDAO);
                bind(org.apache.ambari.server.orm.dao.HostDAO.class).toInstance(mockHostDAO);
            }
        });
        EasyMock.expect(mockClusterDAO.findByName("cl1")).andReturn(mockClusterEntity).once();
        EasyMock.expect(mockClusterEntity.getHostEntities()).andReturn(hostEntities).times(2);
        mockHostEntity1.setHostName("host1");
        mockHostEntity2.setHostName("host2");
        org.apache.ambari.server.update.HostUpdateHelper hostUpdateHelper = new org.apache.ambari.server.update.HostUpdateHelper(null, null, mockInjector);
        hostUpdateHelper.setHostChangesFileMap(clusterHostsToChange);
        easyMockSupport.replayAll();
        hostUpdateHelper.updateHostsInDB();
        easyMockSupport.verifyAll();
        junit.framework.Assert.assertEquals(mockHostEntity1.getHostName(), "host10");
        junit.framework.Assert.assertEquals(mockHostEntity2.getHostName(), "host11");
    }

    @org.junit.Test
    public void testUpdateHostsForAlertsInDB() throws org.apache.ambari.server.AmbariException {
        org.easymock.EasyMockSupport easyMockSupport = new org.easymock.EasyMockSupport();
        final org.apache.ambari.server.controller.AmbariManagementController mockAmbariManagementController = easyMockSupport.createNiceMock(org.apache.ambari.server.controller.AmbariManagementController.class);
        final org.apache.ambari.server.orm.dao.AlertsDAO mockAlertsDAO = easyMockSupport.createNiceMock(org.apache.ambari.server.orm.dao.AlertsDAO.class);
        final org.apache.ambari.server.orm.dao.AlertDefinitionDAO mockAlertDefinitionDAO = easyMockSupport.createNiceMock(org.apache.ambari.server.orm.dao.AlertDefinitionDAO.class);
        final org.apache.ambari.server.orm.dao.AlertDispatchDAO mockAlertDispatchDAO = easyMockSupport.createNiceMock(org.apache.ambari.server.orm.dao.AlertDispatchDAO.class);
        final javax.persistence.EntityManager entityManager = EasyMock.createNiceMock(javax.persistence.EntityManager.class);
        final org.apache.ambari.server.orm.DBAccessor dbAccessor = EasyMock.createNiceMock(org.apache.ambari.server.orm.DBAccessor.class);
        final org.apache.ambari.server.orm.dao.ClusterDAO mockClusterDAO = easyMockSupport.createNiceMock(org.apache.ambari.server.orm.dao.ClusterDAO.class);
        final org.apache.ambari.server.state.Clusters mockClusters = easyMockSupport.createNiceMock(org.apache.ambari.server.state.Clusters.class);
        org.apache.ambari.server.state.Cluster mockCluster = easyMockSupport.createNiceMock(org.apache.ambari.server.state.Cluster.class);
        org.apache.ambari.server.orm.entities.AlertCurrentEntity mockAlertCurrentEntity1 = easyMockSupport.createNiceMock(org.apache.ambari.server.orm.entities.AlertCurrentEntity.class);
        org.apache.ambari.server.orm.entities.AlertCurrentEntity mockAlertCurrentEntity2 = easyMockSupport.createNiceMock(org.apache.ambari.server.orm.entities.AlertCurrentEntity.class);
        org.apache.ambari.server.orm.entities.AlertHistoryEntity mockAlertHistoryEntity1 = easyMockSupport.createNiceMock(org.apache.ambari.server.orm.entities.AlertHistoryEntity.class);
        org.apache.ambari.server.orm.entities.AlertHistoryEntity mockAlertHistoryEntity2 = easyMockSupport.createNiceMock(org.apache.ambari.server.orm.entities.AlertHistoryEntity.class);
        org.apache.ambari.server.orm.entities.AlertDefinitionEntity mockAlertDefinitionEntity = easyMockSupport.createNiceMock(org.apache.ambari.server.orm.entities.AlertDefinitionEntity.class);
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> clusterHostsToChange = new java.util.HashMap<>();
        java.util.List<org.apache.ambari.server.orm.entities.AlertCurrentEntity> alertCurrentEntities = new java.util.ArrayList<>();
        java.util.List<org.apache.ambari.server.orm.entities.AlertDefinitionEntity> alertDefinitionEntities = new java.util.ArrayList<>();
        java.util.Map<java.lang.String, org.apache.ambari.server.state.Cluster> clusterMap = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.lang.String> hosts = new java.util.HashMap<>();
        hosts.put("host1", "host10");
        hosts.put("host2", "host11");
        clusterHostsToChange.put("cl1", hosts);
        clusterMap.put("cl1", mockCluster);
        alertCurrentEntities.add(mockAlertCurrentEntity1);
        alertCurrentEntities.add(mockAlertCurrentEntity2);
        alertDefinitionEntities.add(mockAlertDefinitionEntity);
        final com.google.inject.Injector mockInjector = com.google.inject.Guice.createInjector(new com.google.inject.AbstractModule() {
            @java.lang.Override
            protected void configure() {
                org.apache.ambari.server.testutils.PartialNiceMockBinder.newBuilder().addConfigsBindings().addFactoriesInstallBinding().addPasswordEncryptorBindings().addLdapBindings().build().configure(binder());
                bind(org.apache.ambari.server.orm.DBAccessor.class).toInstance(dbAccessor);
                bind(javax.persistence.EntityManager.class).toInstance(entityManager);
                bind(org.apache.ambari.server.state.stack.OsFamily.class).toInstance(EasyMock.createNiceMock(org.apache.ambari.server.state.stack.OsFamily.class));
                bind(org.apache.ambari.server.orm.dao.ClusterDAO.class).toInstance(mockClusterDAO);
                bind(org.apache.ambari.server.state.Clusters.class).toInstance(mockClusters);
                bind(org.apache.ambari.server.controller.AmbariManagementController.class).toInstance(mockAmbariManagementController);
                bind(org.apache.ambari.server.orm.dao.AlertDispatchDAO.class).toInstance(mockAlertDispatchDAO);
                bind(org.apache.ambari.server.orm.dao.AlertsDAO.class).toInstance(mockAlertsDAO);
                bind(org.apache.ambari.server.orm.dao.AlertDefinitionDAO.class).toInstance(mockAlertDefinitionDAO);
                bind(org.apache.ambari.server.topology.PersistedState.class).toInstance(EasyMock.createNiceMock(org.apache.ambari.server.topology.PersistedState.class));
                bind(org.apache.ambari.server.stack.StackManagerFactory.class).toInstance(EasyMock.createNiceMock(org.apache.ambari.server.stack.StackManagerFactory.class));
                bind(org.apache.ambari.server.mpack.MpackManagerFactory.class).toInstance(EasyMock.createNiceMock(org.apache.ambari.server.mpack.MpackManagerFactory.class));
                bind(org.apache.ambari.server.actionmanager.HostRoleCommandFactory.class).to(org.apache.ambari.server.actionmanager.HostRoleCommandFactoryImpl.class);
                bind(org.apache.ambari.server.actionmanager.ActionDBAccessor.class).to(org.apache.ambari.server.actionmanager.ActionDBAccessorImpl.class);
                bind(com.google.inject.persist.UnitOfWork.class).toInstance(EasyMock.createNiceMock(com.google.inject.persist.UnitOfWork.class));
                bind(org.apache.ambari.server.metadata.RoleCommandOrderProvider.class).to(org.apache.ambari.server.metadata.CachedRoleCommandOrderProvider.class);
                bind(org.apache.ambari.server.actionmanager.StageFactory.class).to(org.apache.ambari.server.actionmanager.StageFactoryImpl.class);
                bind(org.apache.ambari.server.audit.AuditLogger.class).toInstance(EasyMock.createNiceMock(org.apache.ambari.server.audit.AuditLoggerDefaultImpl.class));
                bind(org.springframework.security.crypto.password.PasswordEncoder.class).toInstance(new org.springframework.security.crypto.password.StandardPasswordEncoder());
                bind(org.apache.ambari.server.hooks.HookService.class).to(org.apache.ambari.server.hooks.users.UserHookService.class);
                bind(org.apache.ambari.server.state.ServiceComponentHostFactory.class).toInstance(EasyMock.createNiceMock(org.apache.ambari.server.state.ServiceComponentHostFactory.class));
                bind(org.apache.ambari.server.controller.AbstractRootServiceResponseFactory.class).to(org.apache.ambari.server.controller.RootServiceResponseFactory.class);
                bind(org.apache.ambari.server.security.encryption.CredentialStoreService.class).toInstance(EasyMock.createNiceMock(org.apache.ambari.server.security.encryption.CredentialStoreService.class));
                bind(org.apache.ambari.server.scheduler.ExecutionScheduler.class).to(org.apache.ambari.server.scheduler.ExecutionSchedulerImpl.class);
                bind(org.apache.ambari.server.controller.KerberosHelper.class).toInstance(EasyMock.createNiceMock(org.apache.ambari.server.controller.KerberosHelper.class));
            }
        });
        EasyMock.expect(mockAmbariManagementController.getClusters()).andReturn(mockClusters).once();
        EasyMock.expect(mockClusters.getClusters()).andReturn(clusterMap).once();
        EasyMock.expect(mockCluster.getClusterId()).andReturn(1L).once();
        EasyMock.expect(mockAlertsDAO.findCurrentByCluster(1L)).andReturn(alertCurrentEntities).once();
        EasyMock.expect(mockAlertCurrentEntity1.getAlertHistory()).andReturn(mockAlertHistoryEntity1).once();
        EasyMock.expect(mockAlertCurrentEntity2.getAlertHistory()).andReturn(mockAlertHistoryEntity2).once();
        EasyMock.expect(mockAlertHistoryEntity1.getHostName()).andReturn("host1").atLeastOnce();
        EasyMock.expect(mockAlertHistoryEntity2.getHostName()).andReturn("host2").atLeastOnce();
        EasyMock.expect(mockAlertDefinitionDAO.findAll(1L)).andReturn(alertDefinitionEntities).once();
        mockAlertHistoryEntity1.setHostName("host10");
        EasyMock.expectLastCall();
        mockAlertHistoryEntity2.setHostName("host11");
        EasyMock.expectLastCall();
        mockAlertDefinitionEntity.setHash(EasyMock.anyString());
        EasyMock.expectLastCall();
        org.apache.ambari.server.update.HostUpdateHelper hostUpdateHelper = new org.apache.ambari.server.update.HostUpdateHelper(null, null, mockInjector);
        hostUpdateHelper.setHostChangesFileMap(clusterHostsToChange);
        easyMockSupport.replayAll();
        hostUpdateHelper.updateHostsForAlertsInDB();
        easyMockSupport.verifyAll();
    }

    @org.junit.Test
    public void testInitHostChangesFileMap_SUCCESS() throws org.apache.ambari.server.AmbariException {
        org.easymock.EasyMockSupport easyMockSupport = new org.easymock.EasyMockSupport();
        final org.apache.ambari.server.configuration.Configuration mockConfiguration = easyMockSupport.createNiceMock(org.apache.ambari.server.configuration.Configuration.class);
        com.google.gson.JsonObject cluster = new com.google.gson.JsonObject();
        com.google.gson.JsonObject hostPairs = new com.google.gson.JsonObject();
        hostPairs.add("Host1", new com.google.gson.JsonPrimitive("Host11"));
        hostPairs.add("Host2", new com.google.gson.JsonPrimitive("Host22"));
        cluster.add("cl1", hostPairs);
        EasyMock.expect(mockConfiguration.getHostChangesJson(null)).andReturn(cluster).once();
        org.apache.ambari.server.update.HostUpdateHelper hostUpdateHelper = new org.apache.ambari.server.update.HostUpdateHelper(null, mockConfiguration, null);
        easyMockSupport.replayAll();
        hostUpdateHelper.initHostChangesFileMap();
        easyMockSupport.verifyAll();
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> hostChangesFileMap = hostUpdateHelper.getHostChangesFileMap();
        junit.framework.Assert.assertTrue(hostChangesFileMap.get("cl1").containsKey("host1"));
        junit.framework.Assert.assertTrue(hostChangesFileMap.get("cl1").containsKey("host2"));
        junit.framework.Assert.assertTrue(hostChangesFileMap.get("cl1").get("host1").equals("host11"));
        junit.framework.Assert.assertTrue(hostChangesFileMap.get("cl1").get("host2").equals("host22"));
    }
}