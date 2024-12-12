package org.apache.ambari.server.serveraction.kerberos;
import javax.persistence.EntityManager;
import org.easymock.Capture;
import org.easymock.CaptureType;
import org.easymock.EasyMockSupport;
import org.springframework.security.crypto.password.PasswordEncoder;
import static org.easymock.EasyMock.anyBoolean;
import static org.easymock.EasyMock.anyObject;
import static org.easymock.EasyMock.capture;
import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.newCapture;
public class AbstractPrepareKerberosServerActionTest extends org.easymock.EasyMockSupport {
    private static final java.lang.String KERBEROS_DESCRIPTOR_JSON = "" + (((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((("{" + "  \"identities\": [") + "    {") + "      \"keytab\": {") + "        \"file\": \"${keytab_dir}/spnego.service.keytab\",") + "        \"group\": {") + "          \"access\": \"r\",") + "          \"name\": \"${cluster-env/user_group}\"") + "        },") + "        \"owner\": {") + "          \"access\": \"r\",") + "          \"name\": \"root\"") + "        }") + "      },") + "      \"name\": \"spnego\",") + "      \"principal\": {") + "        \"configuration\": null,") + "        \"local_username\": null,") + "        \"type\": \"service\",") + "        \"value\": \"HTTP/_HOST@${realm}\"") + "      }") + "    }") + "  ],") + "  \"services\": [") + "    {") + "      \"components\": [") + "        {") + "          \"identities\": [") + "            {") + "              \"name\": \"service_master_spnego_identity\",") + "              \"reference\": \"/spnego\"") + "            }") + "          ],") + "          \"name\": \"SERVICE_MASTER\"") + "        }") + "      ],") + "      \"configurations\": [") + "        {") + "          \"service-site\": {") + "            \"property1\": \"property1_updated_value\",") + "            \"property2\": \"property2_updated_value\"") + "          }") + "        }") + "      ],") + "      \"identities\": [") + "        {") + "          \"name\": \"service_identity\",") + "          \"keytab\": {") + "            \"configuration\": \"service-site/keytab_file_path\",") + "            \"file\": \"${keytab_dir}/service.service.keytab\",") + "            \"group\": {") + "              \"access\": \"r\",") + "              \"name\": \"${cluster-env/user_group}\"") + "            },") + "            \"owner\": {") + "              \"access\": \"r\",") + "              \"name\": \"${service-env/service_user}\"") + "            }") + "          },") + "          \"principal\": {") + "            \"configuration\": \"service-site/principal_name\",") + "            \"local_username\": \"${service-env/service_user}\",") + "            \"type\": \"service\",") + "            \"value\": \"${service-env/service_user}/_HOST@${realm}\"") + "          }") + "        }") + "      ],") + "      \"name\": \"SERVICE\"") + "    }") + "  ],") + "  \"properties\": {") + "    \"additional_realms\": \"\",") + "    \"keytab_dir\": \"/etc/security/keytabs\",") + "    \"principal_suffix\": \"-${cluster_name|toLower()}\",") + "    \"realm\": \"${kerberos-env/realm}\"") + "  }") + "}");

    private class TestKerberosServerAction extends org.apache.ambari.server.serveraction.kerberos.AbstractPrepareKerberosServerAction {
        @java.lang.Override
        protected java.lang.String getClusterName() {
            return "c1";
        }

        @java.lang.Override
        public org.apache.ambari.server.agent.CommandReport execute(java.util.concurrent.ConcurrentMap<java.lang.String, java.lang.Object> requestSharedDataContext) {
            return null;
        }
    }

    private com.google.inject.Injector injector;

    private final org.apache.ambari.server.serveraction.kerberos.AbstractPrepareKerberosServerAction testKerberosServerAction = new org.apache.ambari.server.serveraction.kerberos.AbstractPrepareKerberosServerActionTest.TestKerberosServerAction();

    @org.junit.Before
    public void setUp() throws java.lang.Exception {
        injector = com.google.inject.Guice.createInjector(new com.google.inject.AbstractModule() {
            @java.lang.Override
            protected void configure() {
                bind(org.apache.ambari.server.api.services.AmbariMetaInfo.class).toInstance(createNiceMock(org.apache.ambari.server.api.services.AmbariMetaInfo.class));
                bind(org.apache.ambari.server.controller.KerberosHelper.class).to(org.apache.ambari.server.controller.KerberosHelperImpl.class);
                bind(org.apache.ambari.server.serveraction.kerberos.KerberosIdentityDataFileWriterFactory.class).toInstance(createNiceMock(org.apache.ambari.server.serveraction.kerberos.KerberosIdentityDataFileWriterFactory.class));
                bind(org.apache.ambari.server.serveraction.kerberos.KerberosConfigDataFileWriterFactory.class).toInstance(createNiceMock(org.apache.ambari.server.serveraction.kerberos.KerberosConfigDataFileWriterFactory.class));
                bind(org.apache.ambari.server.state.Clusters.class).toInstance(createNiceMock(org.apache.ambari.server.state.Clusters.class));
                bind(org.apache.ambari.server.audit.AuditLogger.class).toInstance(createNiceMock(org.apache.ambari.server.audit.AuditLogger.class));
                bind(org.apache.ambari.server.state.ConfigHelper.class).toInstance(createNiceMock(org.apache.ambari.server.state.ConfigHelper.class));
                bind(org.apache.ambari.server.orm.dao.HostRoleCommandDAO.class).toInstance(createNiceMock(org.apache.ambari.server.orm.dao.HostRoleCommandDAO.class));
                bind(org.apache.ambari.server.actionmanager.ActionManager.class).toInstance(createNiceMock(org.apache.ambari.server.actionmanager.ActionManager.class));
                bind(org.apache.ambari.server.state.stack.OsFamily.class).toInstance(createNiceMock(org.apache.ambari.server.state.stack.OsFamily.class));
                bind(org.apache.ambari.server.scheduler.ExecutionScheduler.class).toInstance(createNiceMock(org.apache.ambari.server.scheduler.ExecutionScheduler.class));
                bind(org.apache.ambari.server.controller.AmbariManagementController.class).toInstance(createNiceMock(org.apache.ambari.server.controller.AmbariManagementController.class));
                bind(org.apache.ambari.server.actionmanager.ActionDBAccessor.class).toInstance(createNiceMock(org.apache.ambari.server.actionmanager.ActionDBAccessor.class));
                bind(org.apache.ambari.server.stack.StackManagerFactory.class).toInstance(createNiceMock(org.apache.ambari.server.stack.StackManagerFactory.class));
                bind(org.apache.ambari.server.state.ConfigFactory.class).toInstance(createNiceMock(org.apache.ambari.server.state.ConfigFactory.class));
                bind(org.apache.ambari.server.state.configgroup.ConfigGroupFactory.class).toInstance(createNiceMock(org.apache.ambari.server.state.configgroup.ConfigGroupFactory.class));
                bind(org.apache.ambari.server.security.encryption.CredentialStoreService.class).toInstance(createNiceMock(org.apache.ambari.server.security.encryption.CredentialStoreService.class));
                bind(org.apache.ambari.server.state.scheduler.RequestExecutionFactory.class).toInstance(createNiceMock(org.apache.ambari.server.state.scheduler.RequestExecutionFactory.class));
                bind(org.apache.ambari.server.actionmanager.RequestFactory.class).toInstance(createNiceMock(org.apache.ambari.server.actionmanager.RequestFactory.class));
                bind(org.apache.ambari.server.metadata.RoleCommandOrderProvider.class).toInstance(createNiceMock(org.apache.ambari.server.metadata.RoleCommandOrderProvider.class));
                bind(org.apache.ambari.server.stageplanner.RoleGraphFactory.class).toInstance(createNiceMock(org.apache.ambari.server.stageplanner.RoleGraphFactory.class));
                bind(org.apache.ambari.server.controller.AbstractRootServiceResponseFactory.class).toInstance(createNiceMock(org.apache.ambari.server.controller.AbstractRootServiceResponseFactory.class));
                bind(org.apache.ambari.server.state.ServiceComponentFactory.class).toInstance(createNiceMock(org.apache.ambari.server.state.ServiceComponentFactory.class));
                bind(org.apache.ambari.server.state.ServiceComponentHostFactory.class).toInstance(createNiceMock(org.apache.ambari.server.state.ServiceComponentHostFactory.class));
                bind(org.apache.ambari.server.actionmanager.StageFactory.class).toInstance(createNiceMock(org.apache.ambari.server.actionmanager.StageFactory.class));
                bind(org.apache.ambari.server.actionmanager.HostRoleCommandFactory.class).toInstance(createNiceMock(org.apache.ambari.server.actionmanager.HostRoleCommandFactory.class));
                bind(org.apache.ambari.server.hooks.HookContextFactory.class).toInstance(createNiceMock(org.apache.ambari.server.hooks.HookContextFactory.class));
                bind(org.apache.ambari.server.hooks.HookService.class).toInstance(createNiceMock(org.apache.ambari.server.hooks.HookService.class));
                bind(org.springframework.security.crypto.password.PasswordEncoder.class).toInstance(createNiceMock(org.springframework.security.crypto.password.PasswordEncoder.class));
                bind(org.apache.ambari.server.topology.PersistedState.class).toInstance(createNiceMock(org.apache.ambari.server.topology.PersistedState.class));
                bind(org.apache.ambari.server.topology.tasks.ConfigureClusterTaskFactory.class).toInstance(createNiceMock(org.apache.ambari.server.topology.tasks.ConfigureClusterTaskFactory.class));
                com.google.inject.Provider<javax.persistence.EntityManager> entityManagerProvider = createNiceMock(com.google.inject.Provider.class);
                bind(javax.persistence.EntityManager.class).toProvider(entityManagerProvider);
                bind(org.apache.ambari.server.mpack.MpackManagerFactory.class).toInstance(createNiceMock(org.apache.ambari.server.mpack.MpackManagerFactory.class));
                bind(org.apache.ambari.server.ldap.service.AmbariLdapConfigurationProvider.class).toInstance(createMock(org.apache.ambari.server.ldap.service.AmbariLdapConfigurationProvider.class));
                bind(org.apache.ambari.server.orm.dao.KerberosKeytabPrincipalDAO.class).toInstance(createNiceMock(org.apache.ambari.server.orm.dao.KerberosKeytabPrincipalDAO.class));
                bind(new com.google.inject.TypeLiteral<org.apache.ambari.server.security.encryption.Encryptor<org.apache.ambari.server.events.AgentConfigsUpdateEvent>>() {}).annotatedWith(com.google.inject.name.Names.named("AgentConfigEncryptor")).toInstance(org.apache.ambari.server.security.encryption.Encryptor.NONE);
                bind(new com.google.inject.TypeLiteral<org.apache.ambari.server.security.encryption.Encryptor<org.apache.ambari.server.configuration.AmbariServerConfiguration>>() {}).annotatedWith(com.google.inject.name.Names.named("AmbariServerConfigurationEncryptor")).toInstance(org.apache.ambari.server.security.encryption.Encryptor.NONE);
            }
        });
        injector.injectMembers(testKerberosServerAction);
    }

    @org.junit.Test
    @java.lang.SuppressWarnings("unchecked")
    public void testProcessServiceComponentHosts() throws java.lang.Exception {
        final org.apache.ambari.server.state.Cluster cluster = createNiceMock(org.apache.ambari.server.state.Cluster.class);
        final org.apache.ambari.server.serveraction.kerberos.KerberosIdentityDataFileWriter kerberosIdentityDataFileWriter = createNiceMock(org.apache.ambari.server.serveraction.kerberos.KerberosIdentityDataFileWriter.class);
        final org.apache.ambari.server.state.kerberos.KerberosDescriptor kerberosDescriptor = createNiceMock(org.apache.ambari.server.state.kerberos.KerberosDescriptor.class);
        final org.apache.ambari.server.state.ServiceComponentHost serviceComponentHostHDFS = createNiceMock(org.apache.ambari.server.state.ServiceComponentHost.class);
        final org.apache.ambari.server.state.ServiceComponentHost serviceComponentHostZK = createNiceMock(org.apache.ambari.server.state.ServiceComponentHost.class);
        final org.apache.ambari.server.state.kerberos.KerberosServiceDescriptor serviceDescriptor = createNiceMock(org.apache.ambari.server.state.kerberos.KerberosServiceDescriptor.class);
        final org.apache.ambari.server.state.kerberos.KerberosComponentDescriptor componentDescriptor = createNiceMock(org.apache.ambari.server.state.kerberos.KerberosComponentDescriptor.class);
        final java.lang.String hdfsService = "HDFS";
        final java.lang.String zookeeperService = "ZOOKEEPER";
        final java.lang.String hostName = "host1";
        final java.lang.String hdfsComponent = "DATANODE";
        final java.lang.String zkComponent = "ZK";
        java.util.Collection<java.lang.String> identityFilter = new java.util.ArrayList<>();
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> kerberosConfigurations = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.util.Set<java.lang.String>> propertiesToIgnore = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> configurations = new java.util.HashMap<>();
        java.util.List<org.apache.ambari.server.state.ServiceComponentHost> serviceComponentHosts = new java.util.ArrayList<org.apache.ambari.server.state.ServiceComponentHost>() {
            {
                add(serviceComponentHostHDFS);
                add(serviceComponentHostZK);
            }
        };
        java.util.Map<java.lang.String, org.apache.ambari.server.state.Service> clusterServices = new java.util.HashMap<java.lang.String, org.apache.ambari.server.state.Service>() {
            {
                put(hdfsService, null);
                put(zookeeperService, null);
            }
        };
        org.apache.ambari.server.serveraction.kerberos.KerberosIdentityDataFileWriterFactory kerberosIdentityDataFileWriterFactory = injector.getInstance(org.apache.ambari.server.serveraction.kerberos.KerberosIdentityDataFileWriterFactory.class);
        EasyMock.expect(kerberosIdentityDataFileWriterFactory.createKerberosIdentityDataFileWriter(EasyMock.anyObject(java.io.File.class))).andReturn(kerberosIdentityDataFileWriter);
        EasyMock.expect(cluster.getServices()).andReturn(new java.util.HashMap<>(clusterServices)).atLeastOnce();
        EasyMock.expect(serviceComponentHostHDFS.getHostName()).andReturn(hostName).atLeastOnce();
        EasyMock.expect(serviceComponentHostHDFS.getServiceName()).andReturn(hdfsService).atLeastOnce();
        EasyMock.expect(serviceComponentHostHDFS.getServiceComponentName()).andReturn(hdfsComponent).atLeastOnce();
        EasyMock.expect(serviceComponentHostHDFS.getHost()).andReturn(createNiceMock(org.apache.ambari.server.state.Host.class)).atLeastOnce();
        EasyMock.expect(serviceComponentHostZK.getHostName()).andReturn(hostName).atLeastOnce();
        EasyMock.expect(serviceComponentHostZK.getServiceName()).andReturn(zookeeperService).atLeastOnce();
        EasyMock.expect(serviceComponentHostZK.getServiceComponentName()).andReturn(zkComponent).atLeastOnce();
        EasyMock.expect(serviceComponentHostZK.getHost()).andReturn(createNiceMock(org.apache.ambari.server.state.Host.class)).atLeastOnce();
        EasyMock.expect(kerberosDescriptor.getService(hdfsService)).andReturn(serviceDescriptor).once();
        EasyMock.expect(serviceDescriptor.getComponent(hdfsComponent)).andReturn(componentDescriptor).once();
        EasyMock.expect(componentDescriptor.getConfigurations(EasyMock.anyBoolean())).andReturn(null);
        replayAll();
        injector.getInstance(org.apache.ambari.server.api.services.AmbariMetaInfo.class).init();
        testKerberosServerAction.processServiceComponentHosts(cluster, kerberosDescriptor, serviceComponentHosts, identityFilter, "", configurations, kerberosConfigurations, false, propertiesToIgnore);
        verifyAll();
        org.junit.Assert.assertEquals("host1", configurations.get("").get("host"));
        org.junit.Assert.assertEquals("host1", configurations.get("").get("hostname"));
    }

    @org.junit.Test
    public void testProcessConfigurationChanges() throws java.lang.Exception {
        java.util.Map<java.lang.String, java.lang.String> serviceSiteProperties = new java.util.HashMap<>();
        serviceSiteProperties.put("property1", "property1_value");
        serviceSiteProperties.put("principal_name", "principal_name_value");
        serviceSiteProperties.put("keytab_file_path", "keytab_file_path_value");
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> effectiveProperties = new java.util.HashMap<>();
        effectiveProperties.put("service-site", serviceSiteProperties);
        java.util.Map<java.lang.String, java.lang.String> updatedServiceSiteProperties = new java.util.HashMap<>();
        updatedServiceSiteProperties.put("property1", "property1_updated_value");
        updatedServiceSiteProperties.put("property2", "property2_updated_value");
        updatedServiceSiteProperties.put("principal_name", "principal_name_updated_value");
        updatedServiceSiteProperties.put("keytab_file_path", "keytab_file_path_updated_value");
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> kerberosConfigurations = new java.util.HashMap<>();
        kerberosConfigurations.put("service-site", updatedServiceSiteProperties);
        org.apache.ambari.server.state.kerberos.KerberosDescriptor kerberosDescriptor = new org.apache.ambari.server.state.kerberos.KerberosDescriptorFactory().createInstance(org.apache.ambari.server.serveraction.kerberos.AbstractPrepareKerberosServerActionTest.KERBEROS_DESCRIPTOR_JSON);
        org.apache.ambari.server.state.ConfigHelper configHelper = injector.getInstance(org.apache.ambari.server.state.ConfigHelper.class);
        EasyMock.expect(configHelper.getEffectiveConfigProperties(EasyMock.eq("c1"), EasyMock.eq(null))).andReturn(effectiveProperties).anyTimes();
        org.apache.ambari.server.serveraction.kerberos.KerberosConfigDataFileWriterFactory factory = injector.getInstance(org.apache.ambari.server.serveraction.kerberos.KerberosConfigDataFileWriterFactory.class);
        org.apache.ambari.server.serveraction.kerberos.AbstractPrepareKerberosServerActionTest.ConfigWriterData dataCaptureAll = setupConfigWriter(factory);
        org.apache.ambari.server.serveraction.kerberos.AbstractPrepareKerberosServerActionTest.ConfigWriterData dataCaptureIdentitiesOnly = setupConfigWriter(factory);
        org.apache.ambari.server.serveraction.kerberos.AbstractPrepareKerberosServerActionTest.ConfigWriterData dataCaptureNewAndIdentities = setupConfigWriter(factory);
        org.apache.ambari.server.serveraction.kerberos.AbstractPrepareKerberosServerActionTest.ConfigWriterData dataCaptureNone = setupConfigWriter(factory);
        replayAll();
        injector.getInstance(org.apache.ambari.server.api.services.AmbariMetaInfo.class).init();
        java.util.Map<java.lang.String, java.lang.String> expectedProperties;
        testKerberosServerAction.processConfigurationChanges("test_directory", kerberosConfigurations, java.util.Collections.emptyMap(), kerberosDescriptor, org.apache.ambari.server.controller.UpdateConfigurationPolicy.ALL);
        expectedProperties = new java.util.HashMap<>();
        expectedProperties.put("property1", "property1_updated_value");
        expectedProperties.put("property2", "property2_updated_value");
        expectedProperties.put("principal_name", "principal_name_updated_value");
        expectedProperties.put("keytab_file_path", "keytab_file_path_updated_value");
        verifyDataCapture(dataCaptureAll, java.util.Collections.singletonMap("service-site", expectedProperties));
        testKerberosServerAction.processConfigurationChanges("test_directory", kerberosConfigurations, java.util.Collections.emptyMap(), kerberosDescriptor, org.apache.ambari.server.controller.UpdateConfigurationPolicy.IDENTITIES_ONLY);
        expectedProperties = new java.util.HashMap<>();
        expectedProperties.put("principal_name", "principal_name_updated_value");
        expectedProperties.put("keytab_file_path", "keytab_file_path_updated_value");
        verifyDataCapture(dataCaptureIdentitiesOnly, java.util.Collections.singletonMap("service-site", expectedProperties));
        testKerberosServerAction.processConfigurationChanges("test_directory", kerberosConfigurations, java.util.Collections.emptyMap(), kerberosDescriptor, org.apache.ambari.server.controller.UpdateConfigurationPolicy.NEW_AND_IDENTITIES);
        expectedProperties = new java.util.HashMap<>();
        expectedProperties.put("property2", "property2_updated_value");
        expectedProperties.put("principal_name", "principal_name_updated_value");
        expectedProperties.put("keytab_file_path", "keytab_file_path_updated_value");
        verifyDataCapture(dataCaptureNewAndIdentities, java.util.Collections.singletonMap("service-site", expectedProperties));
        testKerberosServerAction.processConfigurationChanges("test_directory", kerberosConfigurations, java.util.Collections.emptyMap(), kerberosDescriptor, org.apache.ambari.server.controller.UpdateConfigurationPolicy.NONE);
        verifyDataCapture(dataCaptureNone, java.util.Collections.emptyMap());
        verifyAll();
    }

    private void verifyDataCapture(org.apache.ambari.server.serveraction.kerberos.AbstractPrepareKerberosServerActionTest.ConfigWriterData configWriterData, java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> expectedConfigurations) {
        int expectedCaptures = 0;
        java.util.Collection<java.util.Map<java.lang.String, java.lang.String>> expectedValuesCollection = expectedConfigurations.values();
        for (java.util.Map<java.lang.String, java.lang.String> expectedValues : expectedValuesCollection) {
            expectedCaptures += expectedValues.size();
        }
        org.easymock.Capture<java.lang.String> captureConfigType = configWriterData.getCaptureConfigType();
        if (expectedCaptures > 0) {
            org.junit.Assert.assertTrue(captureConfigType.hasCaptured());
            java.util.List<java.lang.String> valuesConfigType = captureConfigType.getValues();
            org.junit.Assert.assertEquals(expectedCaptures, valuesConfigType.size());
        } else {
            org.junit.Assert.assertFalse(captureConfigType.hasCaptured());
        }
        org.easymock.Capture<java.lang.String> capturePropertyName = configWriterData.getCapturePropertyName();
        if (expectedCaptures > 0) {
            org.junit.Assert.assertTrue(capturePropertyName.hasCaptured());
            java.util.List<java.lang.String> valuesPropertyName = capturePropertyName.getValues();
            org.junit.Assert.assertEquals(expectedCaptures, valuesPropertyName.size());
        } else {
            org.junit.Assert.assertFalse(capturePropertyName.hasCaptured());
        }
        org.easymock.Capture<java.lang.String> capturePropertyValue = configWriterData.getCapturePropertyValue();
        if (expectedCaptures > 0) {
            org.junit.Assert.assertTrue(capturePropertyValue.hasCaptured());
            java.util.List<java.lang.String> valuesPropertyValue = capturePropertyValue.getValues();
            org.junit.Assert.assertEquals(expectedCaptures, valuesPropertyValue.size());
        } else {
            org.junit.Assert.assertFalse(capturePropertyValue.hasCaptured());
        }
        if (expectedCaptures > 0) {
            int i = 0;
            java.util.List<java.lang.String> valuesConfigType = captureConfigType.getValues();
            java.util.List<java.lang.String> valuesPropertyName = capturePropertyName.getValues();
            java.util.List<java.lang.String> valuesPropertyValue = capturePropertyValue.getValues();
            for (java.util.Map.Entry<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> entry : expectedConfigurations.entrySet()) {
                java.lang.String configType = entry.getKey();
                java.util.Map<java.lang.String, java.lang.String> properties = entry.getValue();
                for (java.util.Map.Entry<java.lang.String, java.lang.String> property : properties.entrySet()) {
                    org.junit.Assert.assertEquals(configType, valuesConfigType.get(i));
                    org.junit.Assert.assertEquals(property.getKey(), valuesPropertyName.get(i));
                    org.junit.Assert.assertEquals(property.getValue(), valuesPropertyValue.get(i));
                    i++;
                }
            }
        }
    }

    private org.apache.ambari.server.serveraction.kerberos.AbstractPrepareKerberosServerActionTest.ConfigWriterData setupConfigWriter(org.apache.ambari.server.serveraction.kerberos.KerberosConfigDataFileWriterFactory factory) throws java.io.IOException {
        org.easymock.Capture<java.lang.String> captureConfigType = EasyMock.newCapture(CaptureType.ALL);
        org.easymock.Capture<java.lang.String> capturePropertyName = EasyMock.newCapture(CaptureType.ALL);
        org.easymock.Capture<java.lang.String> capturePropertyValue = EasyMock.newCapture(CaptureType.ALL);
        org.apache.ambari.server.serveraction.kerberos.KerberosConfigDataFileWriter mockWriter = createMock(org.apache.ambari.server.serveraction.kerberos.KerberosConfigDataFileWriter.class);
        mockWriter.addRecord(EasyMock.capture(captureConfigType), EasyMock.capture(capturePropertyName), EasyMock.capture(capturePropertyValue), EasyMock.eq(org.apache.ambari.server.serveraction.kerberos.KerberosConfigDataFileWriter.OPERATION_TYPE_SET));
        EasyMock.expectLastCall().anyTimes();
        mockWriter.close();
        EasyMock.expectLastCall().anyTimes();
        EasyMock.expect(factory.createKerberosConfigDataFileWriter(EasyMock.anyObject(java.io.File.class))).andReturn(mockWriter).once();
        return new org.apache.ambari.server.serveraction.kerberos.AbstractPrepareKerberosServerActionTest.ConfigWriterData(captureConfigType, capturePropertyName, capturePropertyValue);
    }

    private class ConfigWriterData {
        private final org.easymock.Capture<java.lang.String> captureConfigType;

        private final org.easymock.Capture<java.lang.String> capturePropertyName;

        private final org.easymock.Capture<java.lang.String> capturePropertyValue;

        private ConfigWriterData(org.easymock.Capture<java.lang.String> captureConfigType, org.easymock.Capture<java.lang.String> capturePropertyName, org.easymock.Capture<java.lang.String> capturePropertyValue) {
            this.captureConfigType = captureConfigType;
            this.capturePropertyName = capturePropertyName;
            this.capturePropertyValue = capturePropertyValue;
        }

        org.easymock.Capture<java.lang.String> getCaptureConfigType() {
            return captureConfigType;
        }

        org.easymock.Capture<java.lang.String> getCapturePropertyName() {
            return capturePropertyName;
        }

        org.easymock.Capture<java.lang.String> getCapturePropertyValue() {
            return capturePropertyValue;
        }
    }
}