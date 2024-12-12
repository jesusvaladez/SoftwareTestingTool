package org.apache.ambari.server.upgrade;
import com.google.inject.persist.UnitOfWork;
import javax.persistence.EntityManager;
import org.easymock.Capture;
import org.easymock.EasyMockRunner;
import org.easymock.Mock;
import org.easymock.MockType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.StandardPasswordEncoder;
import static org.easymock.EasyMock.anyObject;
import static org.easymock.EasyMock.anyString;
import static org.easymock.EasyMock.capture;
import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.createNiceMock;
import static org.easymock.EasyMock.createStrictMock;
import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.newCapture;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.reset;
import static org.easymock.EasyMock.verify;
@org.junit.runner.RunWith(org.easymock.EasyMockRunner.class)
public class UpgradeCatalog252Test {
    @org.easymock.Mock(type = org.easymock.MockType.STRICT)
    private com.google.inject.Provider<javax.persistence.EntityManager> entityManagerProvider;

    @org.easymock.Mock(type = org.easymock.MockType.NICE)
    private javax.persistence.EntityManager entityManager;

    @org.easymock.Mock(type = org.easymock.MockType.NICE)
    private org.apache.ambari.server.orm.DBAccessor dbAccessor;

    @org.easymock.Mock(type = org.easymock.MockType.NICE)
    private org.apache.ambari.server.configuration.Configuration configuration;

    @org.easymock.Mock(type = org.easymock.MockType.NICE)
    private java.sql.Connection connection;

    @org.easymock.Mock(type = org.easymock.MockType.NICE)
    private java.sql.Statement statement;

    @org.easymock.Mock(type = org.easymock.MockType.NICE)
    private java.sql.ResultSet resultSet;

    @org.easymock.Mock(type = org.easymock.MockType.NICE)
    private org.apache.ambari.server.state.stack.OsFamily osFamily;

    @org.easymock.Mock(type = org.easymock.MockType.NICE)
    private org.apache.ambari.server.controller.KerberosHelper kerberosHelper;

    @org.easymock.Mock(type = org.easymock.MockType.NICE)
    private org.apache.ambari.server.actionmanager.ActionManager actionManager;

    @org.easymock.Mock(type = org.easymock.MockType.NICE)
    private org.apache.ambari.server.state.Config config;

    @org.easymock.Mock(type = org.easymock.MockType.STRICT)
    private org.apache.ambari.server.state.Service service;

    @org.easymock.Mock(type = org.easymock.MockType.NICE)
    private org.apache.ambari.server.state.Clusters clusters;

    @org.easymock.Mock(type = org.easymock.MockType.NICE)
    private org.apache.ambari.server.state.Cluster cluster;

    @org.easymock.Mock(type = org.easymock.MockType.NICE)
    private org.apache.ambari.server.agent.stomp.MetadataHolder metadataHolder;

    @org.easymock.Mock(type = org.easymock.MockType.NICE)
    private com.google.inject.Injector injector;

    @org.junit.Before
    public void init() {
        EasyMock.reset(entityManagerProvider, injector);
        EasyMock.expect(entityManagerProvider.get()).andReturn(entityManager).anyTimes();
        EasyMock.expect(injector.getInstance(com.google.gson.Gson.class)).andReturn(null).anyTimes();
        EasyMock.expect(injector.getInstance(org.apache.ambari.server.controller.MaintenanceStateHelper.class)).andReturn(null).anyTimes();
        EasyMock.expect(injector.getInstance(org.apache.ambari.server.controller.KerberosHelper.class)).andReturn(kerberosHelper).anyTimes();
        EasyMock.replay(entityManagerProvider, injector);
    }

    @org.junit.After
    public void tearDown() {
    }

    @org.junit.Test
    public void testExecuteDDLUpdates() throws java.lang.Exception {
        org.easymock.Capture<org.apache.ambari.server.orm.DBAccessor.DBColumnInfo> hrcBackgroundColumnCapture = EasyMock.newCapture();
        dbAccessor.addColumn(EasyMock.eq(org.apache.ambari.server.upgrade.UpgradeCatalog252.CLUSTERCONFIG_TABLE), EasyMock.capture(hrcBackgroundColumnCapture));
        EasyMock.expect(dbAccessor.getConnection()).andReturn(connection).anyTimes();
        EasyMock.expect(connection.createStatement()).andReturn(statement).anyTimes();
        EasyMock.expect(statement.executeQuery(EasyMock.anyObject(java.lang.String.class))).andReturn(resultSet).anyTimes();
        EasyMock.expect(configuration.getDatabaseType()).andReturn(org.apache.ambari.server.configuration.Configuration.DatabaseType.POSTGRES).anyTimes();
        EasyMock.replay(dbAccessor, configuration, connection, statement, resultSet);
        com.google.inject.Injector injector = getInjector(EasyMock.createMock(org.apache.ambari.server.state.Clusters.class), EasyMock.createNiceMock(org.apache.ambari.server.controller.AmbariManagementControllerImpl.class));
        org.apache.ambari.server.upgrade.UpgradeCatalog252 upgradeCatalog252 = injector.getInstance(org.apache.ambari.server.upgrade.UpgradeCatalog252.class);
        upgradeCatalog252.executeDDLUpdates();
        EasyMock.verify(dbAccessor);
        org.apache.ambari.server.orm.DBAccessor.DBColumnInfo captured = hrcBackgroundColumnCapture.getValue();
        org.junit.Assert.assertEquals(org.apache.ambari.server.upgrade.UpgradeCatalog252.SERVICE_DELETED_COLUMN, captured.getName());
        org.junit.Assert.assertEquals(0, captured.getDefaultValue());
        org.junit.Assert.assertEquals(java.lang.Short.class, captured.getType());
    }

    @org.junit.Test
    public void testFixLivySuperUsers() throws org.apache.ambari.server.AmbariException {
        final org.apache.ambari.server.state.Clusters clusters = EasyMock.createMock(org.apache.ambari.server.state.Clusters.class);
        final org.apache.ambari.server.state.Cluster cluster = EasyMock.createMock(org.apache.ambari.server.state.Cluster.class);
        final org.apache.ambari.server.state.Config zeppelinEnv = EasyMock.createMock(org.apache.ambari.server.state.Config.class);
        final org.apache.ambari.server.state.Config livyConf = EasyMock.createMock(org.apache.ambari.server.state.Config.class);
        final org.apache.ambari.server.state.Config livyConfNew = EasyMock.createMock(org.apache.ambari.server.state.Config.class);
        final org.apache.ambari.server.state.Config livy2Conf = EasyMock.createMock(org.apache.ambari.server.state.Config.class);
        final org.apache.ambari.server.state.Config livy2ConfNew = EasyMock.createMock(org.apache.ambari.server.state.Config.class);
        final org.apache.ambari.server.controller.AmbariManagementController controller = EasyMock.createMock(org.apache.ambari.server.controller.AmbariManagementController.class);
        org.apache.ambari.server.state.StackId stackId = new org.apache.ambari.server.state.StackId("HDP", "2.2");
        org.easymock.Capture<? extends java.util.Map<java.lang.String, java.lang.String>> captureLivyConfProperties = EasyMock.newCapture();
        org.easymock.Capture<? extends java.util.Map<java.lang.String, java.lang.String>> captureLivy2ConfProperties = EasyMock.newCapture();
        EasyMock.expect(clusters.getClusters()).andReturn(java.util.Collections.singletonMap("c1", cluster)).once();
        EasyMock.expect(cluster.getClusterName()).andReturn("c1").atLeastOnce();
        EasyMock.expect(cluster.getClusterId()).andReturn(1L).anyTimes();
        EasyMock.expect(cluster.getDesiredStackVersion()).andReturn(stackId).atLeastOnce();
        EasyMock.expect(cluster.getDesiredConfigByType("zeppelin-env")).andReturn(zeppelinEnv).atLeastOnce();
        EasyMock.expect(cluster.getServiceByConfigType("livy-conf")).andReturn("SPARK").atLeastOnce();
        EasyMock.expect(cluster.getDesiredConfigByType("livy-conf")).andReturn(livyConf).atLeastOnce();
        EasyMock.expect(cluster.getConfigsByType("livy-conf")).andReturn(java.util.Collections.singletonMap("tag1", livyConf)).atLeastOnce();
        EasyMock.expect(cluster.getConfig(EasyMock.eq("livy-conf"), EasyMock.anyString())).andReturn(livyConfNew).atLeastOnce();
        EasyMock.expect(cluster.getServiceByConfigType("livy2-conf")).andReturn("SPARK2").atLeastOnce();
        EasyMock.expect(cluster.getDesiredConfigByType("livy2-conf")).andReturn(livy2Conf).atLeastOnce();
        EasyMock.expect(cluster.getConfigsByType("livy2-conf")).andReturn(java.util.Collections.singletonMap("tag1", livy2Conf)).atLeastOnce();
        EasyMock.expect(cluster.getConfig(EasyMock.eq("livy2-conf"), EasyMock.anyString())).andReturn(livy2ConfNew).atLeastOnce();
        EasyMock.expect(cluster.addDesiredConfig(EasyMock.eq("ambari-upgrade"), EasyMock.anyObject(java.util.Set.class), EasyMock.anyString())).andReturn(null).atLeastOnce();
        EasyMock.expect(zeppelinEnv.getProperties()).andReturn(java.util.Collections.singletonMap("zeppelin.server.kerberos.principal", "zeppelin_user@AMBARI.LOCAL")).once();
        EasyMock.expect(livyConf.getProperties()).andReturn(java.util.Collections.singletonMap("livy.superusers", "zeppelin-c1, some_user")).atLeastOnce();
        EasyMock.expect(livyConf.getPropertiesAttributes()).andReturn(java.util.Collections.<java.lang.String, java.util.Map<java.lang.String, java.lang.String>>emptyMap()).atLeastOnce();
        EasyMock.expect(livy2Conf.getProperties()).andReturn(java.util.Collections.<java.lang.String, java.lang.String>emptyMap()).atLeastOnce();
        EasyMock.expect(livy2Conf.getPropertiesAttributes()).andReturn(java.util.Collections.<java.lang.String, java.util.Map<java.lang.String, java.lang.String>>emptyMap()).atLeastOnce();
        EasyMock.expect(controller.createConfig(EasyMock.eq(cluster), EasyMock.eq(stackId), EasyMock.eq("livy-conf"), EasyMock.capture(captureLivyConfProperties), EasyMock.anyString(), EasyMock.anyObject(java.util.Map.class))).andReturn(livyConfNew).once();
        EasyMock.expect(controller.createConfig(EasyMock.eq(cluster), EasyMock.eq(stackId), EasyMock.eq("livy2-conf"), EasyMock.capture(captureLivy2ConfProperties), EasyMock.anyString(), EasyMock.anyObject(java.util.Map.class))).andReturn(livy2ConfNew).once();
        EasyMock.replay(clusters, cluster, zeppelinEnv, livy2Conf, livyConf, controller, metadataHolder);
        com.google.inject.Injector injector = getInjector(clusters, controller);
        final org.apache.ambari.server.state.ConfigHelper configHelper = injector.getInstance(org.apache.ambari.server.state.ConfigHelper.class);
        configHelper.updateAgentConfigs(EasyMock.anyObject(java.util.Set.class));
        EasyMock.expectLastCall().times(2);
        EasyMock.replay(configHelper);
        org.apache.ambari.server.upgrade.UpgradeCatalog252 upgradeCatalog252 = injector.getInstance(org.apache.ambari.server.upgrade.UpgradeCatalog252.class);
        upgradeCatalog252.fixLivySuperusers();
        EasyMock.verify(clusters, cluster, zeppelinEnv, livy2Conf, livyConf, controller, configHelper);
        org.junit.Assert.assertTrue(captureLivyConfProperties.hasCaptured());
        org.junit.Assert.assertEquals("some_user,zeppelin_user", captureLivyConfProperties.getValue().get("livy.superusers"));
        org.junit.Assert.assertTrue(captureLivy2ConfProperties.hasCaptured());
        org.junit.Assert.assertEquals("zeppelin_user", captureLivy2ConfProperties.getValue().get("livy.superusers"));
    }

    @org.junit.Test
    public void testUpdateKerberosDescriptorArtifact() throws org.apache.ambari.server.AmbariException {
        java.lang.String initialJson = "{" + (((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((("  \"services\": [" + "    {") + "      \"name\": \"SPARK\",") + "      \"configurations\": [") + "        {") + "          \"livy-conf\": {") + "            \"property1\": \"true\",") + "            \"property2\": \"true\",") + "            \"livy.superusers\": \"somevalue\"") + "          }") + "        },") + "        {") + "          \"some-env\": {") + "            \"groups\": \"${hadoop-env/proxyuser_group}\",") + "            \"hosts\": \"${clusterHostInfo/existing_service_master_hosts}\"") + "          }") + "        }") + "      ]") + "    },") + "    {") + "      \"name\": \"SPARK2\",") + "      \"configurations\": [") + "        {") + "          \"livy2-conf\": {") + "            \"property1\": \"true\",") + "            \"property2\": \"true\",") + "            \"livy.superusers\": \"somevalue\"") + "          }") + "        },") + "        {") + "          \"some2-env\": {") + "            \"groups\": \"${hadoop-env/proxyuser_group}\",") + "            \"hosts\": \"${clusterHostInfo/existing_service_master_hosts}\"") + "          }") + "        }") + "      ]") + "    },") + "    {") + "      \"name\": \"KNOX\",") + "      \"components\": [") + "        {") + "          \"name\": \"KNOX_GATEWAY\",") + "          \"configurations\": [") + "            {") + "              \"core-site\": {") + "                \"property1\": \"true\",") + "                \"property2\": \"true\",") + "                \"hadoop.proxyuser.knox.groups\": \"somevalue\",") + "                \"hadoop.proxyuser.knox.hosts\": \"somevalue\"") + "              }") + "            },") + "            {") + "              \"webhcat-site\": {") + "                \"webhcat.proxyuser.knox.groups\": \"somevalue\",") + "                \"webhcat.proxyuser.knox.hosts\": \"somevalue\"") + "              }") + "            },") + "            {") + "              \"oozie-site\": {") + "                \"oozie.service.ProxyUserService.proxyuser.knox.groups\": \"somevalue\",") + "                \"oozie.service.ProxyUserService.proxyuser.knox.hosts\": \"somevalue\"") + "              }") + "            },") + "            {") + "              \"falcon-runtime.properties\": {") + "                \"*.falcon.service.ProxyUserService.proxyuser.knox.groups\": \"somevalue\",") + "                \"*.falcon.service.ProxyUserService.proxyuser.knox.hosts\": \"somevalue\"") + "              }") + "            },") + "            {") + "              \"some-env\": {") + "                \"groups\": \"${hadoop-env/proxyuser_group}\",") + "                \"hosts\": \"${clusterHostInfo/existing_service_master_hosts}\"") + "              }") + "            }") + "          ]") + "        }") + "      ]") + "    },") + "    {") + "      \"name\": \"NOT_SPARK\",") + "      \"configurations\": [") + "        {") + "          \"not-livy-conf\": {") + "            \"property1\": \"true\",") + "            \"property2\": \"true\",") + "            \"livy.superusers\": \"somevalue\"") + "          }") + "        },") + "        {") + "          \"some2-env\": {") + "            \"groups\": \"${hadoop-env/proxyuser_group}\",") + "            \"hosts\": \"${clusterHostInfo/existing_service_master_hosts}\"") + "          }") + "        }") + "      ]") + "    }") + "  ]") + "}");
        java.lang.reflect.Type type = new com.google.gson.reflect.TypeToken<java.util.Map<java.lang.String, java.lang.Object>>() {}.getType();
        java.util.Map<java.lang.String, java.lang.Object> map = new com.google.gson.Gson().fromJson(initialJson, type);
        org.easymock.Capture<? extends java.util.Map<java.lang.String, java.lang.Object>> captureMap = EasyMock.newCapture();
        org.apache.ambari.server.orm.entities.ArtifactEntity artifactEntity = EasyMock.createMock(org.apache.ambari.server.orm.entities.ArtifactEntity.class);
        EasyMock.expect(artifactEntity.getArtifactData()).andReturn(map).once();
        artifactEntity.setArtifactData(EasyMock.capture(captureMap));
        EasyMock.expectLastCall().once();
        org.apache.ambari.server.orm.dao.ArtifactDAO artifactDAO = EasyMock.createMock(org.apache.ambari.server.orm.dao.ArtifactDAO.class);
        EasyMock.expect(artifactDAO.merge(artifactEntity)).andReturn(artifactEntity).once();
        EasyMock.replay(artifactDAO, artifactEntity);
        com.google.inject.Injector injector = getInjector(EasyMock.createMock(org.apache.ambari.server.state.Clusters.class), EasyMock.createNiceMock(org.apache.ambari.server.controller.AmbariManagementControllerImpl.class));
        org.apache.ambari.server.upgrade.UpgradeCatalog252 upgradeCatalog252 = injector.getInstance(org.apache.ambari.server.upgrade.UpgradeCatalog252.class);
        upgradeCatalog252.updateKerberosDescriptorArtifact(artifactDAO, artifactEntity);
        EasyMock.verify(artifactDAO, artifactEntity);
        org.junit.Assert.assertTrue(captureMap.hasCaptured());
        org.apache.ambari.server.state.kerberos.KerberosDescriptor result = new org.apache.ambari.server.state.kerberos.KerberosDescriptorFactory().createInstance(captureMap.getValue());
        org.junit.Assert.assertNotNull(result.getService("SPARK"));
        org.junit.Assert.assertNotNull(result.getService("SPARK").getConfiguration("livy-conf"));
        org.junit.Assert.assertNotNull(result.getService("SPARK").getConfiguration("livy-conf").getProperties());
        org.junit.Assert.assertFalse(result.getService("SPARK").getConfiguration("livy-conf").getProperties().containsKey("livy.superusers"));
        org.junit.Assert.assertNotNull(result.getService("SPARK2"));
        org.junit.Assert.assertNotNull(result.getService("SPARK2").getConfiguration("livy2-conf"));
        org.junit.Assert.assertNotNull(result.getService("SPARK2").getConfiguration("livy2-conf").getProperties());
        org.junit.Assert.assertFalse(result.getService("SPARK2").getConfiguration("livy2-conf").getProperties().containsKey("livy.superusers"));
        org.junit.Assert.assertNotNull(result.getService("NOT_SPARK"));
        org.junit.Assert.assertNotNull(result.getService("NOT_SPARK").getConfiguration("not-livy-conf"));
        org.junit.Assert.assertNotNull(result.getService("NOT_SPARK").getConfiguration("not-livy-conf").getProperties());
        org.junit.Assert.assertTrue(result.getService("NOT_SPARK").getConfiguration("not-livy-conf").getProperties().containsKey("livy.superusers"));
        org.junit.Assert.assertNotNull(result.getService("KNOX"));
        org.apache.ambari.server.state.kerberos.KerberosComponentDescriptor knoxGateway = result.getService("KNOX").getComponent("KNOX_GATEWAY");
        org.junit.Assert.assertNotNull(knoxGateway);
        org.junit.Assert.assertNotNull(knoxGateway.getConfiguration("core-site"));
        org.junit.Assert.assertNotNull(knoxGateway.getConfiguration("core-site").getProperties());
        org.junit.Assert.assertTrue(knoxGateway.getConfiguration("core-site").getProperties().containsKey("property1"));
        org.junit.Assert.assertFalse(knoxGateway.getConfiguration("core-site").getProperties().containsKey("hadoop.proxyuser.knox.groups"));
        org.junit.Assert.assertFalse(knoxGateway.getConfiguration("core-site").getProperties().containsKey("hadoop.proxyuser.knox.hosts"));
        org.junit.Assert.assertNull(knoxGateway.getConfiguration("oozie-site"));
        org.junit.Assert.assertNull(knoxGateway.getConfiguration("webhcat-site"));
        org.junit.Assert.assertNull(knoxGateway.getConfiguration("falcon-runtime.properties"));
        org.junit.Assert.assertNotNull(knoxGateway.getConfiguration("some-env"));
    }

    private com.google.inject.Injector getInjector(org.apache.ambari.server.state.Clusters clusters, org.apache.ambari.server.controller.AmbariManagementController ambariManagementController) {
        com.google.inject.Module module = new com.google.inject.Module() {
            @java.lang.Override
            public void configure(com.google.inject.Binder binder) {
                org.apache.ambari.server.testutils.PartialNiceMockBinder.newBuilder().addConfigsBindings().addFactoriesInstallBinding().addPasswordEncryptorBindings().addLdapBindings().build().configure(binder);
                binder.bind(org.apache.ambari.server.orm.DBAccessor.class).toInstance(dbAccessor);
                binder.bind(org.apache.ambari.server.state.stack.OsFamily.class).toInstance(osFamily);
                binder.bind(javax.persistence.EntityManager.class).toInstance(entityManager);
                binder.bind(org.apache.ambari.server.state.Clusters.class).toInstance(clusters);
                binder.bind(org.apache.ambari.server.controller.AmbariManagementController.class).toInstance(ambariManagementController);
                binder.bind(org.apache.ambari.server.actionmanager.ActionDBAccessor.class).toInstance(EasyMock.createNiceMock(org.apache.ambari.server.actionmanager.ActionDBAccessorImpl.class));
                binder.bind(org.apache.ambari.server.topology.PersistedState.class).toInstance(EasyMock.createMock(org.apache.ambari.server.topology.PersistedStateImpl.class));
                binder.bind(org.apache.ambari.server.actionmanager.HostRoleCommandFactory.class).to(org.apache.ambari.server.actionmanager.HostRoleCommandFactoryImpl.class);
                binder.bind(org.apache.ambari.server.audit.AuditLogger.class).toInstance(EasyMock.createNiceMock(org.apache.ambari.server.audit.AuditLoggerDefaultImpl.class));
                binder.bind(org.apache.ambari.server.actionmanager.StageFactory.class).to(org.apache.ambari.server.actionmanager.StageFactoryImpl.class);
                binder.bind(com.google.inject.persist.UnitOfWork.class).toInstance(EasyMock.createNiceMock(com.google.inject.persist.UnitOfWork.class));
                binder.bind(org.apache.ambari.server.metadata.RoleCommandOrderProvider.class).to(org.apache.ambari.server.metadata.CachedRoleCommandOrderProvider.class);
                binder.bind(org.springframework.security.crypto.password.PasswordEncoder.class).toInstance(new org.springframework.security.crypto.password.StandardPasswordEncoder());
                binder.bind(org.apache.ambari.server.hooks.HookService.class).to(org.apache.ambari.server.hooks.users.UserHookService.class);
                binder.bind(org.apache.ambari.server.state.ServiceComponentHostFactory.class).toInstance(EasyMock.createNiceMock(org.apache.ambari.server.state.ServiceComponentHostFactory.class));
                binder.bind(org.apache.ambari.server.controller.AbstractRootServiceResponseFactory.class).to(org.apache.ambari.server.controller.RootServiceResponseFactory.class);
                binder.bind(org.apache.ambari.server.security.encryption.CredentialStoreService.class).toInstance(EasyMock.createNiceMock(org.apache.ambari.server.security.encryption.CredentialStoreService.class));
                binder.bind(org.apache.ambari.server.scheduler.ExecutionScheduler.class).toInstance(EasyMock.createNiceMock(org.apache.ambari.server.scheduler.ExecutionScheduler.class));
                binder.bind(org.apache.ambari.server.api.services.AmbariMetaInfo.class).toInstance(EasyMock.createNiceMock(org.apache.ambari.server.api.services.AmbariMetaInfo.class));
                binder.bind(org.apache.ambari.server.controller.KerberosHelper.class).toInstance(EasyMock.createNiceMock(org.apache.ambari.server.controller.KerberosHelperImpl.class));
                binder.bind(org.apache.ambari.server.agent.stomp.MetadataHolder.class).toInstance(metadataHolder);
                binder.bind(org.apache.ambari.server.agent.stomp.AgentConfigsHolder.class).toInstance(EasyMock.createNiceMock(org.apache.ambari.server.agent.stomp.AgentConfigsHolder.class));
                binder.bind(org.apache.ambari.server.stack.StackManagerFactory.class).toInstance(EasyMock.createNiceMock(org.apache.ambari.server.stack.StackManagerFactory.class));
                binder.bind(org.apache.ambari.server.state.ConfigHelper.class).toInstance(EasyMock.createStrictMock(org.apache.ambari.server.state.ConfigHelper.class));
                binder.bind(org.apache.ambari.server.mpack.MpackManagerFactory.class).toInstance(EasyMock.createNiceMock(org.apache.ambari.server.mpack.MpackManagerFactory.class));
            }
        };
        return com.google.inject.Guice.createInjector(module);
    }
}