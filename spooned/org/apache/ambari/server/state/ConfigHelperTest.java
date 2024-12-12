package org.apache.ambari.server.state;
import com.google.inject.persist.Transactional;
import javax.persistence.EntityManager;
import org.springframework.security.core.context.SecurityContextHolder;
import static org.easymock.EasyMock.createNiceMock;
import static org.easymock.EasyMock.createStrictMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
@org.junit.runner.RunWith(org.junit.experimental.runners.Enclosed.class)
public class ConfigHelperTest {
    public static class RunWithInMemoryDefaultTestModule {
        private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.state.ConfigHelperTest.class);

        private static org.apache.ambari.server.state.Clusters clusters;

        private static com.google.inject.Injector injector;

        private static java.lang.String clusterName;

        private static org.apache.ambari.server.state.Cluster cluster;

        private static org.apache.ambari.server.state.configgroup.ConfigGroupFactory configGroupFactory;

        private static org.apache.ambari.server.state.ConfigHelper configHelper;

        private static org.apache.ambari.server.controller.AmbariManagementController managementController;

        private static org.apache.ambari.server.api.services.AmbariMetaInfo metaInfo;

        private static org.apache.ambari.server.state.ConfigFactory configFactory;

        @org.junit.Before
        public void setup() throws java.lang.Exception {
            org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(org.apache.ambari.server.security.TestAuthenticationFactory.createAdministrator("admin"));
            org.apache.ambari.server.state.ConfigHelperTest.RunWithInMemoryDefaultTestModule.injector = com.google.inject.Guice.createInjector(new org.apache.ambari.server.orm.InMemoryDefaultTestModule());
            org.apache.ambari.server.state.ConfigHelperTest.RunWithInMemoryDefaultTestModule.injector.getInstance(org.apache.ambari.server.orm.GuiceJpaInitializer.class);
            org.apache.ambari.server.state.ConfigHelperTest.RunWithInMemoryDefaultTestModule.clusters = org.apache.ambari.server.state.ConfigHelperTest.RunWithInMemoryDefaultTestModule.injector.getInstance(org.apache.ambari.server.state.Clusters.class);
            org.apache.ambari.server.state.ConfigHelperTest.RunWithInMemoryDefaultTestModule.configGroupFactory = org.apache.ambari.server.state.ConfigHelperTest.RunWithInMemoryDefaultTestModule.injector.getInstance(org.apache.ambari.server.state.configgroup.ConfigGroupFactory.class);
            org.apache.ambari.server.state.ConfigHelperTest.RunWithInMemoryDefaultTestModule.configHelper = org.apache.ambari.server.state.ConfigHelperTest.RunWithInMemoryDefaultTestModule.injector.getInstance(org.apache.ambari.server.state.ConfigHelper.class);
            org.apache.ambari.server.state.ConfigHelperTest.RunWithInMemoryDefaultTestModule.managementController = org.apache.ambari.server.state.ConfigHelperTest.RunWithInMemoryDefaultTestModule.injector.getInstance(org.apache.ambari.server.controller.AmbariManagementController.class);
            org.apache.ambari.server.state.ConfigHelperTest.RunWithInMemoryDefaultTestModule.metaInfo = org.apache.ambari.server.state.ConfigHelperTest.RunWithInMemoryDefaultTestModule.injector.getInstance(org.apache.ambari.server.api.services.AmbariMetaInfo.class);
            org.apache.ambari.server.state.ConfigHelperTest.RunWithInMemoryDefaultTestModule.configFactory = org.apache.ambari.server.state.ConfigHelperTest.RunWithInMemoryDefaultTestModule.injector.getInstance(org.apache.ambari.server.state.ConfigFactory.class);
            org.apache.ambari.server.state.StackId stackId = new org.apache.ambari.server.state.StackId("HDP-2.0.6");
            org.apache.ambari.server.orm.OrmTestHelper helper = org.apache.ambari.server.state.ConfigHelperTest.RunWithInMemoryDefaultTestModule.injector.getInstance(org.apache.ambari.server.orm.OrmTestHelper.class);
            helper.createStack(stackId);
            org.apache.ambari.server.orm.entities.RepositoryVersionEntity repositoryVersion = helper.getOrCreateRepositoryVersion(stackId, "2.0.6");
            org.apache.ambari.server.state.ConfigHelperTest.RunWithInMemoryDefaultTestModule.clusterName = "c1";
            org.apache.ambari.server.state.ConfigHelperTest.RunWithInMemoryDefaultTestModule.clusters.addCluster(org.apache.ambari.server.state.ConfigHelperTest.RunWithInMemoryDefaultTestModule.clusterName, stackId);
            org.apache.ambari.server.state.ConfigHelperTest.RunWithInMemoryDefaultTestModule.cluster = org.apache.ambari.server.state.ConfigHelperTest.RunWithInMemoryDefaultTestModule.clusters.getCluster(org.apache.ambari.server.state.ConfigHelperTest.RunWithInMemoryDefaultTestModule.clusterName);
            junit.framework.Assert.assertNotNull(org.apache.ambari.server.state.ConfigHelperTest.RunWithInMemoryDefaultTestModule.cluster);
            org.apache.ambari.server.state.ConfigHelperTest.RunWithInMemoryDefaultTestModule.clusters.addHost("h1");
            org.apache.ambari.server.state.ConfigHelperTest.RunWithInMemoryDefaultTestModule.clusters.addHost("h2");
            org.apache.ambari.server.state.ConfigHelperTest.RunWithInMemoryDefaultTestModule.clusters.addHost("h3");
            org.apache.ambari.server.state.ConfigHelperTest.RunWithInMemoryDefaultTestModule.clusters.getHosts().forEach(h -> org.apache.ambari.server.state.ConfigHelperTest.RunWithInMemoryDefaultTestModule.clusters.updateHostMappings(h));
            junit.framework.Assert.assertNotNull(org.apache.ambari.server.state.ConfigHelperTest.RunWithInMemoryDefaultTestModule.clusters.getHost("h1"));
            junit.framework.Assert.assertNotNull(org.apache.ambari.server.state.ConfigHelperTest.RunWithInMemoryDefaultTestModule.clusters.getHost("h2"));
            junit.framework.Assert.assertNotNull(org.apache.ambari.server.state.ConfigHelperTest.RunWithInMemoryDefaultTestModule.clusters.getHost("h3"));
            org.apache.ambari.server.controller.ConfigurationRequest cr = new org.apache.ambari.server.controller.ConfigurationRequest();
            cr.setClusterName(org.apache.ambari.server.state.ConfigHelperTest.RunWithInMemoryDefaultTestModule.clusterName);
            cr.setType("core-site");
            cr.setVersionTag("version1");
            cr.setProperties(new java.util.HashMap<java.lang.String, java.lang.String>() {
                {
                    put("ipc.client.connect.max.retries", "30");
                    put("fs.trash.interval", "30");
                }
            });
            cr.setPropertiesAttributes(new java.util.HashMap<java.lang.String, java.util.Map<java.lang.String, java.lang.String>>() {
                {
                    java.util.Map<java.lang.String, java.lang.String> attrs = new java.util.HashMap<>();
                    attrs.put("ipc.client.connect.max.retries", "1");
                    attrs.put("fs.trash.interval", "2");
                    put("attribute1", attrs);
                }
            });
            final org.apache.ambari.server.controller.ClusterRequest clusterRequest1 = new org.apache.ambari.server.controller.ClusterRequest(org.apache.ambari.server.state.ConfigHelperTest.RunWithInMemoryDefaultTestModule.cluster.getClusterId(), org.apache.ambari.server.state.ConfigHelperTest.RunWithInMemoryDefaultTestModule.clusterName, org.apache.ambari.server.state.ConfigHelperTest.RunWithInMemoryDefaultTestModule.cluster.getDesiredStackVersion().getStackVersion(), null);
            clusterRequest1.setDesiredConfig(java.util.Collections.singletonList(cr));
            org.apache.ambari.server.state.ConfigHelperTest.RunWithInMemoryDefaultTestModule.managementController.updateClusters(new java.util.HashSet<org.apache.ambari.server.controller.ClusterRequest>() {
                {
                    add(clusterRequest1);
                }
            }, null);
            org.apache.ambari.server.controller.ConfigurationRequest cr2 = new org.apache.ambari.server.controller.ConfigurationRequest();
            cr2.setClusterName(org.apache.ambari.server.state.ConfigHelperTest.RunWithInMemoryDefaultTestModule.clusterName);
            cr2.setType("flume-conf");
            cr2.setVersionTag("version1");
            org.apache.ambari.server.state.ConfigHelperTest.RunWithInMemoryDefaultTestModule.cluster.addService("FLUME", repositoryVersion);
            org.apache.ambari.server.state.ConfigHelperTest.RunWithInMemoryDefaultTestModule.cluster.addService("OOZIE", repositoryVersion);
            org.apache.ambari.server.state.ConfigHelperTest.RunWithInMemoryDefaultTestModule.cluster.addService("HDFS", repositoryVersion);
            final org.apache.ambari.server.controller.ClusterRequest clusterRequest2 = new org.apache.ambari.server.controller.ClusterRequest(org.apache.ambari.server.state.ConfigHelperTest.RunWithInMemoryDefaultTestModule.cluster.getClusterId(), org.apache.ambari.server.state.ConfigHelperTest.RunWithInMemoryDefaultTestModule.clusterName, org.apache.ambari.server.state.ConfigHelperTest.RunWithInMemoryDefaultTestModule.cluster.getDesiredStackVersion().getStackVersion(), null);
            clusterRequest2.setDesiredConfig(java.util.Collections.singletonList(cr2));
            org.apache.ambari.server.state.ConfigHelperTest.RunWithInMemoryDefaultTestModule.managementController.updateClusters(new java.util.HashSet<org.apache.ambari.server.controller.ClusterRequest>() {
                {
                    add(clusterRequest2);
                }
            }, null);
            cr.setType("global");
            cr.setVersionTag("version1");
            cr.setProperties(new java.util.HashMap<java.lang.String, java.lang.String>() {
                {
                    put("dfs_namenode_name_dir", "/hadoop/hdfs/namenode");
                    put("namenode_heapsize", "1024");
                }
            });
            cr.setPropertiesAttributes(new java.util.HashMap<java.lang.String, java.util.Map<java.lang.String, java.lang.String>>() {
                {
                    java.util.Map<java.lang.String, java.lang.String> attrs = new java.util.HashMap<>();
                    attrs.put("dfs_namenode_name_dir", "3");
                    attrs.put("namenode_heapsize", "4");
                    put("attribute2", attrs);
                }
            });
            final org.apache.ambari.server.controller.ClusterRequest clusterRequest3 = new org.apache.ambari.server.controller.ClusterRequest(org.apache.ambari.server.state.ConfigHelperTest.RunWithInMemoryDefaultTestModule.cluster.getClusterId(), org.apache.ambari.server.state.ConfigHelperTest.RunWithInMemoryDefaultTestModule.clusterName, org.apache.ambari.server.state.ConfigHelperTest.RunWithInMemoryDefaultTestModule.cluster.getDesiredStackVersion().getStackVersion(), null);
            clusterRequest3.setDesiredConfig(java.util.Collections.singletonList(cr));
            org.apache.ambari.server.state.ConfigHelperTest.RunWithInMemoryDefaultTestModule.managementController.updateClusters(new java.util.HashSet<org.apache.ambari.server.controller.ClusterRequest>() {
                {
                    add(clusterRequest3);
                }
            }, null);
            org.apache.ambari.server.controller.ConfigurationRequest cr4 = new org.apache.ambari.server.controller.ConfigurationRequest();
            cr4.setClusterName(org.apache.ambari.server.state.ConfigHelperTest.RunWithInMemoryDefaultTestModule.clusterName);
            cr4.setType("oozie-site");
            cr4.setVersionTag("version1");
            cr4.setProperties(new java.util.HashMap<java.lang.String, java.lang.String>() {
                {
                    put("oozie.authentication.type", "simple");
                    put("oozie.service.HadoopAccessorService.kerberos.enabled", "false");
                }
            });
            cr4.setPropertiesAttributes(null);
            final org.apache.ambari.server.controller.ClusterRequest clusterRequest4 = new org.apache.ambari.server.controller.ClusterRequest(org.apache.ambari.server.state.ConfigHelperTest.RunWithInMemoryDefaultTestModule.cluster.getClusterId(), org.apache.ambari.server.state.ConfigHelperTest.RunWithInMemoryDefaultTestModule.clusterName, org.apache.ambari.server.state.ConfigHelperTest.RunWithInMemoryDefaultTestModule.cluster.getDesiredStackVersion().getStackVersion(), null);
            clusterRequest4.setDesiredConfig(java.util.Collections.singletonList(cr4));
            org.apache.ambari.server.state.ConfigHelperTest.RunWithInMemoryDefaultTestModule.managementController.updateClusters(new java.util.HashSet<org.apache.ambari.server.controller.ClusterRequest>() {
                {
                    add(clusterRequest4);
                }
            }, null);
            org.apache.ambari.server.controller.ConfigurationRequest cr5 = new org.apache.ambari.server.controller.ConfigurationRequest();
            cr5.setClusterName(org.apache.ambari.server.state.ConfigHelperTest.RunWithInMemoryDefaultTestModule.clusterName);
            cr5.setType("ams-site");
            cr5.setVersionTag("version1");
            cr5.setProperties(new java.util.HashMap<java.lang.String, java.lang.String>() {
                {
                    put("timeline.service.operating.mode", "embedded");
                    put("timeline.service.fifo.enabled", "false");
                }
            });
            cr5.setPropertiesAttributes(null);
            final org.apache.ambari.server.controller.ClusterRequest clusterRequest5 = new org.apache.ambari.server.controller.ClusterRequest(org.apache.ambari.server.state.ConfigHelperTest.RunWithInMemoryDefaultTestModule.cluster.getClusterId(), org.apache.ambari.server.state.ConfigHelperTest.RunWithInMemoryDefaultTestModule.clusterName, org.apache.ambari.server.state.ConfigHelperTest.RunWithInMemoryDefaultTestModule.cluster.getDesiredStackVersion().getStackVersion(), null);
            clusterRequest5.setDesiredConfig(java.util.Collections.singletonList(cr5));
            org.apache.ambari.server.state.ConfigHelperTest.RunWithInMemoryDefaultTestModule.managementController.updateClusters(new java.util.HashSet<org.apache.ambari.server.controller.ClusterRequest>() {
                {
                    add(clusterRequest5);
                }
            }, null);
            org.apache.ambari.server.controller.ConfigurationRequest cr6 = new org.apache.ambari.server.controller.ConfigurationRequest();
            cr6.setClusterName(org.apache.ambari.server.state.ConfigHelperTest.RunWithInMemoryDefaultTestModule.clusterName);
            cr6.setType("hdfs-site");
            cr6.setVersionTag("version1");
            cr6.setProperties(new java.util.HashMap<java.lang.String, java.lang.String>() {
                {
                    put("hadoop.caller.context.enabled", "true");
                }
            });
            cr6.setPropertiesAttributes(null);
            final org.apache.ambari.server.controller.ClusterRequest clusterRequest6 = new org.apache.ambari.server.controller.ClusterRequest(org.apache.ambari.server.state.ConfigHelperTest.RunWithInMemoryDefaultTestModule.cluster.getClusterId(), org.apache.ambari.server.state.ConfigHelperTest.RunWithInMemoryDefaultTestModule.clusterName, org.apache.ambari.server.state.ConfigHelperTest.RunWithInMemoryDefaultTestModule.cluster.getDesiredStackVersion().getStackVersion(), null);
            clusterRequest6.setDesiredConfig(java.util.Collections.singletonList(cr6));
            org.apache.ambari.server.state.ConfigHelperTest.RunWithInMemoryDefaultTestModule.managementController.updateClusters(new java.util.HashSet<org.apache.ambari.server.controller.ClusterRequest>() {
                {
                    add(clusterRequest6);
                }
            }, null);
            org.apache.ambari.server.controller.ConfigurationRequest cr7 = new org.apache.ambari.server.controller.ConfigurationRequest();
            cr7.setClusterName(org.apache.ambari.server.state.ConfigHelperTest.RunWithInMemoryDefaultTestModule.clusterName);
            cr7.setType("hdfs-site");
            cr7.setVersionTag("version2");
            cr7.setProperties(new java.util.HashMap<java.lang.String, java.lang.String>() {
                {
                    put("hadoop.caller.context.enabled", "false");
                }
            });
            cr7.setPropertiesAttributes(null);
            final org.apache.ambari.server.controller.ClusterRequest clusterRequest7 = new org.apache.ambari.server.controller.ClusterRequest(org.apache.ambari.server.state.ConfigHelperTest.RunWithInMemoryDefaultTestModule.cluster.getClusterId(), org.apache.ambari.server.state.ConfigHelperTest.RunWithInMemoryDefaultTestModule.clusterName, org.apache.ambari.server.state.ConfigHelperTest.RunWithInMemoryDefaultTestModule.cluster.getDesiredStackVersion().getStackVersion(), null);
            clusterRequest7.setDesiredConfig(java.util.Collections.singletonList(cr7));
            org.apache.ambari.server.state.ConfigHelperTest.RunWithInMemoryDefaultTestModule.managementController.updateClusters(new java.util.HashSet<org.apache.ambari.server.controller.ClusterRequest>() {
                {
                    add(clusterRequest7);
                }
            }, null);
        }

        @org.junit.After
        public void tearDown() throws org.apache.ambari.server.AmbariException, java.sql.SQLException {
            org.apache.ambari.server.H2DatabaseCleaner.clearDatabaseAndStopPersistenceService(org.apache.ambari.server.state.ConfigHelperTest.RunWithInMemoryDefaultTestModule.injector);
            org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(null);
        }

        @com.google.inject.persist.Transactional
        java.lang.Long addConfigGroup(java.lang.String name, java.lang.String tag, java.util.List<java.lang.String> hosts, java.util.List<org.apache.ambari.server.state.Config> configs) throws org.apache.ambari.server.AmbariException {
            java.util.Map<java.lang.Long, org.apache.ambari.server.state.Host> hostMap = new java.util.HashMap<>();
            java.util.Map<java.lang.String, org.apache.ambari.server.state.Config> configMap = new java.util.HashMap<>();
            java.lang.Long hostId = 1L;
            for (java.lang.String hostname : hosts) {
                org.apache.ambari.server.state.Host host = org.apache.ambari.server.state.ConfigHelperTest.RunWithInMemoryDefaultTestModule.clusters.getHost(hostname);
                hostMap.put(hostId, host);
                hostId++;
            }
            for (org.apache.ambari.server.state.Config config : configs) {
                configMap.put(config.getType(), config);
            }
            org.apache.ambari.server.state.configgroup.ConfigGroup configGroup = org.apache.ambari.server.state.ConfigHelperTest.RunWithInMemoryDefaultTestModule.configGroupFactory.createNew(org.apache.ambari.server.state.ConfigHelperTest.RunWithInMemoryDefaultTestModule.cluster, null, name, tag, "", configMap, hostMap);
            org.apache.ambari.server.state.ConfigHelperTest.RunWithInMemoryDefaultTestModule.LOG.info("Config group created with tag " + tag);
            configGroup.setTag(tag);
            org.apache.ambari.server.state.ConfigHelperTest.RunWithInMemoryDefaultTestModule.cluster.addConfigGroup(configGroup);
            return configGroup.getId();
        }

        void applyConfig(java.util.Map<java.lang.String, java.lang.String> properties, java.lang.String configType, java.lang.String configTag) throws java.lang.Exception {
            org.apache.ambari.server.controller.ConfigurationRequest cr = new org.apache.ambari.server.controller.ConfigurationRequest();
            cr.setClusterName(org.apache.ambari.server.state.ConfigHelperTest.RunWithInMemoryDefaultTestModule.clusterName);
            cr.setType(configType);
            cr.setVersionTag(configTag);
            cr.setProperties(properties);
            final org.apache.ambari.server.controller.ClusterRequest clusterRequest = new org.apache.ambari.server.controller.ClusterRequest(org.apache.ambari.server.state.ConfigHelperTest.RunWithInMemoryDefaultTestModule.cluster.getClusterId(), org.apache.ambari.server.state.ConfigHelperTest.RunWithInMemoryDefaultTestModule.clusterName, org.apache.ambari.server.state.ConfigHelperTest.RunWithInMemoryDefaultTestModule.cluster.getDesiredStackVersion().getStackVersion(), null);
            clusterRequest.setDesiredConfig(java.util.Collections.singletonList(cr));
            org.apache.ambari.server.state.ConfigHelperTest.RunWithInMemoryDefaultTestModule.managementController.updateClusters(new java.util.HashSet<org.apache.ambari.server.controller.ClusterRequest>() {
                {
                    add(clusterRequest);
                }
            }, null);
        }

        @org.junit.Test
        public void testProcessHiddenAttribute() throws java.lang.Exception {
            org.apache.ambari.server.state.StackInfo stackInfo = org.apache.ambari.server.state.ConfigHelperTest.RunWithInMemoryDefaultTestModule.metaInfo.getStack("HDP", "2.0.5");
            java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>>> configAttributes = new java.util.HashMap<>();
            configAttributes.put("hive-site", stackInfo.getDefaultConfigAttributesForConfigType("hive-site"));
            java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> originalConfig_hiveClient = createHiveConfig();
            java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> expectedConfig_hiveClient = new java.util.HashMap<java.lang.String, java.util.Map<java.lang.String, java.lang.String>>() {
                {
                    put("hive-site", new java.util.HashMap<java.lang.String, java.lang.String>() {
                        {
                            put("javax.jdo.option.ConnectionDriverName", "oracle");
                            put("hive.metastore.warehouse.dir", "/tmp");
                        }
                    });
                }
            };
            org.apache.ambari.server.state.ConfigHelper.processHiddenAttribute(originalConfig_hiveClient, configAttributes, "HIVE_CLIENT", false);
            junit.framework.Assert.assertEquals(expectedConfig_hiveClient, originalConfig_hiveClient);
            java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> originalConfig_hiveServer = createHiveConfig();
            java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> expectedConfig_hiveServer = createHiveConfig();
            org.apache.ambari.server.state.ConfigHelper.processHiddenAttribute(originalConfig_hiveServer, configAttributes, "HIVE_SERVER", false);
            junit.framework.Assert.assertEquals(expectedConfig_hiveServer, originalConfig_hiveServer);
            java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> originalConfig_hiveServer1 = createHiveConfig();
            java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> expectedConfig_hiveServer1 = expectedConfig_hiveClient;
            org.apache.ambari.server.state.ConfigHelper.processHiddenAttribute(originalConfig_hiveServer1, configAttributes, "HIVE_SERVER", true);
            junit.framework.Assert.assertEquals(expectedConfig_hiveServer1, originalConfig_hiveServer1);
        }

        private java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> createHiveConfig() {
            return new java.util.HashMap<java.lang.String, java.util.Map<java.lang.String, java.lang.String>>() {
                {
                    put("hive-site", new java.util.HashMap<java.lang.String, java.lang.String>() {
                        {
                            put("javax.jdo.option.ConnectionDriverName", "oracle");
                            put("javax.jdo.option.ConnectionPassword", "1");
                            put("hive.metastore.warehouse.dir", "/tmp");
                        }
                    });
                }
            };
        }

        @org.junit.Test
        public void testEffectiveTagsForHost() throws java.lang.Exception {
            org.apache.ambari.server.controller.ConfigurationRequest cr5 = new org.apache.ambari.server.controller.ConfigurationRequest();
            cr5.setClusterName(org.apache.ambari.server.state.ConfigHelperTest.RunWithInMemoryDefaultTestModule.clusterName);
            cr5.setType("ams-env");
            cr5.setVersionTag("version1");
            cr5.setProperties(new java.util.HashMap<java.lang.String, java.lang.String>() {
                {
                    put("metrics_collector_log_dir", "/var/log/ambari-metrics-collector");
                    put("metrics_collector_pid_dir", "/var/run/ambari-metrics-collector");
                }
            });
            cr5.setPropertiesAttributes(null);
            final org.apache.ambari.server.controller.ClusterRequest clusterRequest6 = new org.apache.ambari.server.controller.ClusterRequest(org.apache.ambari.server.state.ConfigHelperTest.RunWithInMemoryDefaultTestModule.cluster.getClusterId(), org.apache.ambari.server.state.ConfigHelperTest.RunWithInMemoryDefaultTestModule.clusterName, org.apache.ambari.server.state.ConfigHelperTest.RunWithInMemoryDefaultTestModule.cluster.getDesiredStackVersion().getStackVersion(), null);
            clusterRequest6.setDesiredConfig(java.util.Collections.singletonList(cr5));
            org.apache.ambari.server.state.ConfigHelperTest.RunWithInMemoryDefaultTestModule.managementController.updateClusters(new java.util.HashSet<org.apache.ambari.server.controller.ClusterRequest>() {
                {
                    add(clusterRequest6);
                }
            }, null);
            java.util.Map<java.lang.String, java.lang.String> properties = new java.util.HashMap<>();
            properties.put("a", "b");
            properties.put("c", "d");
            final org.apache.ambari.server.state.Config config = org.apache.ambari.server.state.ConfigHelperTest.RunWithInMemoryDefaultTestModule.configFactory.createNew(org.apache.ambari.server.state.ConfigHelperTest.RunWithInMemoryDefaultTestModule.cluster, "ams-env", "version122", properties, null);
            java.lang.Long groupId = addConfigGroup("g1", "t1", new java.util.ArrayList<java.lang.String>() {
                {
                    add("h1");
                }
            }, new java.util.ArrayList<org.apache.ambari.server.state.Config>() {
                {
                    add(config);
                }
            });
            junit.framework.Assert.assertNotNull(groupId);
            java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> configTags = org.apache.ambari.server.state.ConfigHelperTest.RunWithInMemoryDefaultTestModule.configHelper.getEffectiveDesiredTags(org.apache.ambari.server.state.ConfigHelperTest.RunWithInMemoryDefaultTestModule.cluster, "h1");
            junit.framework.Assert.assertNotNull(configTags);
            java.util.Map<java.lang.String, java.lang.String> tagsWithOverrides = configTags.get("ams-env");
            junit.framework.Assert.assertNotNull(tagsWithOverrides);
            junit.framework.Assert.assertTrue(tagsWithOverrides.containsKey(org.apache.ambari.server.state.ConfigHelper.CLUSTER_DEFAULT_TAG));
            junit.framework.Assert.assertEquals("version1", tagsWithOverrides.get(org.apache.ambari.server.state.ConfigHelper.CLUSTER_DEFAULT_TAG));
            junit.framework.Assert.assertTrue(tagsWithOverrides.containsKey(groupId.toString()));
            junit.framework.Assert.assertEquals("version122", tagsWithOverrides.get(groupId.toString()));
        }

        @org.junit.Test
        public void testEffectivePropertiesWithOverrides() throws java.lang.Exception {
            org.apache.ambari.server.controller.ConfigurationRequest cr = new org.apache.ambari.server.controller.ConfigurationRequest();
            cr.setClusterName(org.apache.ambari.server.state.ConfigHelperTest.RunWithInMemoryDefaultTestModule.clusterName);
            cr.setType("core-site2");
            cr.setVersionTag("version1");
            cr.setProperties(new java.util.HashMap<java.lang.String, java.lang.String>() {
                {
                    put("ipc.client.connect.max.retries", "30");
                    put("fs.trash.interval", "30");
                }
            });
            cr.setPropertiesAttributes(new java.util.HashMap<java.lang.String, java.util.Map<java.lang.String, java.lang.String>>() {
                {
                    java.util.Map<java.lang.String, java.lang.String> attrs = new java.util.HashMap<>();
                    attrs.put("ipc.client.connect.max.retries", "1");
                    attrs.put("fs.trash.interval", "2");
                    put("attribute1", attrs);
                }
            });
            final org.apache.ambari.server.controller.ClusterRequest clusterRequest1 = new org.apache.ambari.server.controller.ClusterRequest(org.apache.ambari.server.state.ConfigHelperTest.RunWithInMemoryDefaultTestModule.cluster.getClusterId(), org.apache.ambari.server.state.ConfigHelperTest.RunWithInMemoryDefaultTestModule.clusterName, org.apache.ambari.server.state.ConfigHelperTest.RunWithInMemoryDefaultTestModule.cluster.getDesiredStackVersion().getStackVersion(), null);
            clusterRequest1.setDesiredConfig(java.util.Collections.singletonList(cr));
            org.apache.ambari.server.state.ConfigHelperTest.RunWithInMemoryDefaultTestModule.managementController.updateClusters(new java.util.HashSet<org.apache.ambari.server.controller.ClusterRequest>() {
                {
                    add(clusterRequest1);
                }
            }, null);
            cr.setType("global2");
            cr.setVersionTag("version1");
            cr.setProperties(new java.util.HashMap<java.lang.String, java.lang.String>() {
                {
                    put("dfs_namenode_name_dir", "/hadoop/hdfs/namenode");
                    put("namenode_heapsize", "1024");
                }
            });
            cr.setPropertiesAttributes(new java.util.HashMap<java.lang.String, java.util.Map<java.lang.String, java.lang.String>>() {
                {
                    java.util.Map<java.lang.String, java.lang.String> attrs = new java.util.HashMap<>();
                    attrs.put("dfs_namenode_name_dir", "3");
                    attrs.put("namenode_heapsize", "4");
                    put("attribute2", attrs);
                }
            });
            final org.apache.ambari.server.controller.ClusterRequest clusterRequest3 = new org.apache.ambari.server.controller.ClusterRequest(org.apache.ambari.server.state.ConfigHelperTest.RunWithInMemoryDefaultTestModule.cluster.getClusterId(), org.apache.ambari.server.state.ConfigHelperTest.RunWithInMemoryDefaultTestModule.clusterName, org.apache.ambari.server.state.ConfigHelperTest.RunWithInMemoryDefaultTestModule.cluster.getDesiredStackVersion().getStackVersion(), null);
            clusterRequest3.setDesiredConfig(java.util.Collections.singletonList(cr));
            org.apache.ambari.server.state.ConfigHelperTest.RunWithInMemoryDefaultTestModule.managementController.updateClusters(new java.util.HashSet<org.apache.ambari.server.controller.ClusterRequest>() {
                {
                    add(clusterRequest3);
                }
            }, null);
            java.util.Map<java.lang.String, java.lang.String> properties = new java.util.HashMap<>();
            properties.put("a", "b");
            properties.put("c", "d");
            final org.apache.ambari.server.state.Config config1 = org.apache.ambari.server.state.ConfigHelperTest.RunWithInMemoryDefaultTestModule.configFactory.createNew(org.apache.ambari.server.state.ConfigHelperTest.RunWithInMemoryDefaultTestModule.cluster, "core-site2", "version122", properties, null);
            java.util.Map<java.lang.String, java.lang.String> properties2 = new java.util.HashMap<>();
            properties2.put("namenode_heapsize", "1111");
            final org.apache.ambari.server.state.Config config2 = org.apache.ambari.server.state.ConfigHelperTest.RunWithInMemoryDefaultTestModule.configFactory.createNew(org.apache.ambari.server.state.ConfigHelperTest.RunWithInMemoryDefaultTestModule.cluster, "global2", "version122", properties2, null);
            java.lang.Long groupId = addConfigGroup("g2", "t1", new java.util.ArrayList<java.lang.String>() {
                {
                    add("h1");
                }
            }, new java.util.ArrayList<org.apache.ambari.server.state.Config>() {
                {
                    add(config1);
                    add(config2);
                }
            });
            junit.framework.Assert.assertNotNull(groupId);
            java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> propertyMap = org.apache.ambari.server.state.ConfigHelperTest.RunWithInMemoryDefaultTestModule.configHelper.getEffectiveConfigProperties(org.apache.ambari.server.state.ConfigHelperTest.RunWithInMemoryDefaultTestModule.cluster, org.apache.ambari.server.state.ConfigHelperTest.RunWithInMemoryDefaultTestModule.configHelper.getEffectiveDesiredTags(org.apache.ambari.server.state.ConfigHelperTest.RunWithInMemoryDefaultTestModule.cluster, "h1"));
            junit.framework.Assert.assertNotNull(propertyMap);
            junit.framework.Assert.assertTrue(propertyMap.containsKey("global2"));
            java.util.Map<java.lang.String, java.lang.String> globalProps = propertyMap.get("global2");
            junit.framework.Assert.assertEquals("1111", globalProps.get("namenode_heapsize"));
            junit.framework.Assert.assertEquals("/hadoop/hdfs/namenode", globalProps.get("dfs_namenode_name_dir"));
            junit.framework.Assert.assertTrue(propertyMap.containsKey("core-site"));
            java.util.Map<java.lang.String, java.lang.String> coreProps = propertyMap.get("core-site2");
            junit.framework.Assert.assertTrue(coreProps.containsKey("a"));
            junit.framework.Assert.assertTrue(coreProps.containsKey("c"));
            junit.framework.Assert.assertEquals("30", coreProps.get("ipc.client.connect.max.retries"));
        }

        @org.junit.Test
        public void testEffectivePropertiesAttributesWithOverrides() throws java.lang.Exception {
            org.apache.ambari.server.controller.ConfigurationRequest crr = new org.apache.ambari.server.controller.ConfigurationRequest();
            crr.setClusterName(org.apache.ambari.server.state.ConfigHelperTest.RunWithInMemoryDefaultTestModule.clusterName);
            crr.setType("core-site3");
            crr.setVersionTag("version1");
            crr.setProperties(new java.util.HashMap<java.lang.String, java.lang.String>() {
                {
                    put("ipc.client.connect.max.retries", "30");
                    put("fs.trash.interval", "30");
                }
            });
            crr.setPropertiesAttributes(new java.util.HashMap<java.lang.String, java.util.Map<java.lang.String, java.lang.String>>() {
                {
                    java.util.Map<java.lang.String, java.lang.String> attrs = new java.util.HashMap<>();
                    attrs.put("ipc.client.connect.max.retries", "1");
                    attrs.put("fs.trash.interval", "2");
                    put("attribute1", attrs);
                }
            });
            final org.apache.ambari.server.controller.ClusterRequest clusterRequestDup = new org.apache.ambari.server.controller.ClusterRequest(org.apache.ambari.server.state.ConfigHelperTest.RunWithInMemoryDefaultTestModule.cluster.getClusterId(), org.apache.ambari.server.state.ConfigHelperTest.RunWithInMemoryDefaultTestModule.clusterName, org.apache.ambari.server.state.ConfigHelperTest.RunWithInMemoryDefaultTestModule.cluster.getDesiredStackVersion().getStackVersion(), null);
            clusterRequestDup.setDesiredConfig(java.util.Collections.singletonList(crr));
            org.apache.ambari.server.state.ConfigHelperTest.RunWithInMemoryDefaultTestModule.managementController.updateClusters(new java.util.HashSet<org.apache.ambari.server.controller.ClusterRequest>() {
                {
                    add(clusterRequestDup);
                }
            }, null);
            crr.setType("global3");
            crr.setVersionTag("version1");
            crr.setProperties(new java.util.HashMap<java.lang.String, java.lang.String>() {
                {
                    put("dfs_namenode_name_dir", "/hadoop/hdfs/namenode");
                    put("namenode_heapsize", "1024");
                }
            });
            crr.setPropertiesAttributes(new java.util.HashMap<java.lang.String, java.util.Map<java.lang.String, java.lang.String>>() {
                {
                    java.util.Map<java.lang.String, java.lang.String> attrs = new java.util.HashMap<>();
                    attrs.put("dfs_namenode_name_dir", "3");
                    attrs.put("namenode_heapsize", "4");
                    put("attribute2", attrs);
                }
            });
            final org.apache.ambari.server.controller.ClusterRequest clusterRequestGlobalDup = new org.apache.ambari.server.controller.ClusterRequest(org.apache.ambari.server.state.ConfigHelperTest.RunWithInMemoryDefaultTestModule.cluster.getClusterId(), org.apache.ambari.server.state.ConfigHelperTest.RunWithInMemoryDefaultTestModule.clusterName, org.apache.ambari.server.state.ConfigHelperTest.RunWithInMemoryDefaultTestModule.cluster.getDesiredStackVersion().getStackVersion(), null);
            clusterRequestGlobalDup.setDesiredConfig(java.util.Collections.singletonList(crr));
            org.apache.ambari.server.state.ConfigHelperTest.RunWithInMemoryDefaultTestModule.managementController.updateClusters(new java.util.HashSet<org.apache.ambari.server.controller.ClusterRequest>() {
                {
                    add(clusterRequestGlobalDup);
                }
            }, null);
            java.util.Map<java.lang.String, java.lang.String> attributes = new java.util.HashMap<>();
            attributes.put("fs.trash.interval", "11");
            attributes.put("b", "y");
            java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> config1Attributes = new java.util.HashMap<>();
            config1Attributes.put("attribute1", attributes);
            final org.apache.ambari.server.state.Config config1 = org.apache.ambari.server.state.ConfigHelperTest.RunWithInMemoryDefaultTestModule.configFactory.createNew(org.apache.ambari.server.state.ConfigHelperTest.RunWithInMemoryDefaultTestModule.cluster, "core-site3", "version122", new java.util.HashMap<>(), config1Attributes);
            attributes = new java.util.HashMap<>();
            attributes.put("namenode_heapsize", "z");
            attributes.put("c", "q");
            java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> config2Attributes = new java.util.HashMap<>();
            config2Attributes.put("attribute2", attributes);
            final org.apache.ambari.server.state.Config config2 = org.apache.ambari.server.state.ConfigHelperTest.RunWithInMemoryDefaultTestModule.configFactory.createNew(org.apache.ambari.server.state.ConfigHelperTest.RunWithInMemoryDefaultTestModule.cluster, "global3", "version122", new java.util.HashMap<>(), config2Attributes);
            java.lang.Long groupId = addConfigGroup("g3", "t1", new java.util.ArrayList<java.lang.String>() {
                {
                    add("h3");
                }
            }, new java.util.ArrayList<org.apache.ambari.server.state.Config>() {
                {
                    add(config1);
                    add(config2);
                }
            });
            junit.framework.Assert.assertNotNull(groupId);
            java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>>> effectiveAttributes = org.apache.ambari.server.state.ConfigHelperTest.RunWithInMemoryDefaultTestModule.configHelper.getEffectiveConfigAttributes(org.apache.ambari.server.state.ConfigHelperTest.RunWithInMemoryDefaultTestModule.cluster, org.apache.ambari.server.state.ConfigHelperTest.RunWithInMemoryDefaultTestModule.configHelper.getEffectiveDesiredTags(org.apache.ambari.server.state.ConfigHelperTest.RunWithInMemoryDefaultTestModule.cluster, "h3"));
            junit.framework.Assert.assertNotNull(effectiveAttributes);
            junit.framework.Assert.assertEquals(8, effectiveAttributes.size());
            junit.framework.Assert.assertTrue(effectiveAttributes.containsKey("global3"));
            java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> globalAttrs = effectiveAttributes.get("global3");
            junit.framework.Assert.assertEquals(1, globalAttrs.size());
            junit.framework.Assert.assertTrue(globalAttrs.containsKey("attribute2"));
            java.util.Map<java.lang.String, java.lang.String> attribute2Occurances = globalAttrs.get("attribute2");
            junit.framework.Assert.assertEquals(3, attribute2Occurances.size());
            junit.framework.Assert.assertTrue(attribute2Occurances.containsKey("namenode_heapsize"));
            junit.framework.Assert.assertEquals("z", attribute2Occurances.get("namenode_heapsize"));
            junit.framework.Assert.assertTrue(attribute2Occurances.containsKey("dfs_namenode_name_dir"));
            junit.framework.Assert.assertEquals("3", attribute2Occurances.get("dfs_namenode_name_dir"));
            junit.framework.Assert.assertTrue(attribute2Occurances.containsKey("c"));
            junit.framework.Assert.assertEquals("q", attribute2Occurances.get("c"));
            junit.framework.Assert.assertTrue(effectiveAttributes.containsKey("core-site3"));
            java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> coreAttrs = effectiveAttributes.get("core-site3");
            junit.framework.Assert.assertEquals(1, coreAttrs.size());
            junit.framework.Assert.assertTrue(coreAttrs.containsKey("attribute1"));
            java.util.Map<java.lang.String, java.lang.String> attribute1Occurances = coreAttrs.get("attribute1");
            junit.framework.Assert.assertEquals(3, attribute1Occurances.size());
            junit.framework.Assert.assertTrue(attribute1Occurances.containsKey("ipc.client.connect.max.retries"));
            junit.framework.Assert.assertEquals("1", attribute1Occurances.get("ipc.client.connect.max.retries"));
            junit.framework.Assert.assertTrue(attribute1Occurances.containsKey("fs.trash.interval"));
            junit.framework.Assert.assertEquals("11", attribute1Occurances.get("fs.trash.interval"));
            junit.framework.Assert.assertTrue(attribute1Occurances.containsKey("b"));
            junit.framework.Assert.assertEquals("y", attribute1Occurances.get("b"));
        }

        @org.junit.Test
        public void testCloneAttributesMap() throws java.lang.Exception {
            java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> targetAttributesMap = new java.util.HashMap<>();
            java.util.Map<java.lang.String, java.lang.String> attributesValues = new java.util.HashMap<>();
            attributesValues.put("a", "1");
            attributesValues.put("b", "2");
            attributesValues.put("f", "3");
            attributesValues.put("q", "4");
            targetAttributesMap.put("attr", attributesValues);
            java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> sourceAttributesMap = new java.util.HashMap<>();
            attributesValues = new java.util.HashMap<>();
            attributesValues.put("a", "5");
            attributesValues.put("f", "6");
            sourceAttributesMap.put("attr", attributesValues);
            attributesValues = new java.util.HashMap<>();
            attributesValues.put("f", "7");
            attributesValues.put("q", "8");
            sourceAttributesMap.put("attr1", attributesValues);
            org.apache.ambari.server.state.ConfigHelperTest.RunWithInMemoryDefaultTestModule.configHelper.cloneAttributesMap(sourceAttributesMap, targetAttributesMap);
            junit.framework.Assert.assertEquals(2, targetAttributesMap.size());
            junit.framework.Assert.assertTrue(targetAttributesMap.containsKey("attr"));
            junit.framework.Assert.assertTrue(targetAttributesMap.containsKey("attr1"));
            java.util.Map<java.lang.String, java.lang.String> attributes = targetAttributesMap.get("attr");
            junit.framework.Assert.assertEquals(4, attributes.size());
            junit.framework.Assert.assertEquals("5", attributes.get("a"));
            junit.framework.Assert.assertEquals("2", attributes.get("b"));
            junit.framework.Assert.assertEquals("6", attributes.get("f"));
            junit.framework.Assert.assertEquals("4", attributes.get("q"));
            attributes = targetAttributesMap.get("attr1");
            junit.framework.Assert.assertEquals(2, attributes.size());
            junit.framework.Assert.assertEquals("7", attributes.get("f"));
            junit.framework.Assert.assertEquals("8", attributes.get("q"));
        }

        @org.junit.Test
        public void testCloneAttributesMapSourceIsNull() throws java.lang.Exception {
            java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> targetAttributesMap = new java.util.HashMap<>();
            java.util.Map<java.lang.String, java.lang.String> attributesValues = new java.util.HashMap<>();
            attributesValues.put("a", "1");
            attributesValues.put("b", "2");
            attributesValues.put("f", "3");
            attributesValues.put("q", "4");
            targetAttributesMap.put("attr", attributesValues);
            java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> sourceAttributesMap = null;
            org.apache.ambari.server.state.ConfigHelperTest.RunWithInMemoryDefaultTestModule.configHelper.cloneAttributesMap(sourceAttributesMap, targetAttributesMap);
            junit.framework.Assert.assertEquals(1, targetAttributesMap.size());
            junit.framework.Assert.assertTrue(targetAttributesMap.containsKey("attr"));
            java.util.Map<java.lang.String, java.lang.String> attributes = targetAttributesMap.get("attr");
            junit.framework.Assert.assertEquals(4, attributes.size());
            junit.framework.Assert.assertEquals("1", attributes.get("a"));
            junit.framework.Assert.assertEquals("2", attributes.get("b"));
            junit.framework.Assert.assertEquals("3", attributes.get("f"));
            junit.framework.Assert.assertEquals("4", attributes.get("q"));
        }

        @org.junit.Test
        public void testCloneAttributesMapTargetIsNull() throws java.lang.Exception {
            java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> targetAttributesMap = null;
            java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> sourceAttributesMap = new java.util.HashMap<>();
            java.util.Map<java.lang.String, java.lang.String> attributesValues = new java.util.HashMap<>();
            attributesValues.put("a", "5");
            attributesValues.put("f", "6");
            sourceAttributesMap.put("attr", attributesValues);
            attributesValues = new java.util.HashMap<>();
            attributesValues.put("f", "7");
            attributesValues.put("q", "8");
            sourceAttributesMap.put("attr1", attributesValues);
            org.apache.ambari.server.state.ConfigHelperTest.RunWithInMemoryDefaultTestModule.configHelper.cloneAttributesMap(sourceAttributesMap, targetAttributesMap);
            junit.framework.Assert.assertEquals(2, sourceAttributesMap.size());
            junit.framework.Assert.assertTrue(sourceAttributesMap.containsKey("attr"));
            junit.framework.Assert.assertTrue(sourceAttributesMap.containsKey("attr1"));
            java.util.Map<java.lang.String, java.lang.String> attributes = sourceAttributesMap.get("attr");
            junit.framework.Assert.assertEquals(2, attributes.size());
            junit.framework.Assert.assertEquals("5", attributes.get("a"));
            junit.framework.Assert.assertEquals("6", attributes.get("f"));
            attributes = sourceAttributesMap.get("attr1");
            junit.framework.Assert.assertEquals(2, attributes.size());
            junit.framework.Assert.assertEquals("7", attributes.get("f"));
            junit.framework.Assert.assertEquals("8", attributes.get("q"));
        }

        @org.junit.Test
        public void testMergeAttributes() throws java.lang.Exception {
            java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> persistedAttributes = new java.util.HashMap<>();
            java.util.Map<java.lang.String, java.lang.String> persistedFinalAttrs = new java.util.HashMap<>();
            persistedFinalAttrs.put("a", "true");
            persistedFinalAttrs.put("c", "true");
            persistedFinalAttrs.put("d", "true");
            persistedAttributes.put("final", persistedFinalAttrs);
            java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> confGroupAttributes = new java.util.HashMap<>();
            java.util.Map<java.lang.String, java.lang.String> confGroupFinalAttrs = new java.util.HashMap<>();
            confGroupFinalAttrs.put("b", "true");
            confGroupAttributes.put("final", confGroupFinalAttrs);
            java.util.Map<java.lang.String, java.lang.String> confGroupProperties = new java.util.HashMap<>();
            confGroupProperties.put("a", "any");
            confGroupProperties.put("b", "any");
            confGroupProperties.put("c", "any");
            org.apache.ambari.server.state.Config overrideConfig = org.apache.ambari.server.state.ConfigHelperTest.RunWithInMemoryDefaultTestModule.configFactory.createNew(org.apache.ambari.server.state.ConfigHelperTest.RunWithInMemoryDefaultTestModule.cluster, "type", null, confGroupProperties, confGroupAttributes);
            java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> result = org.apache.ambari.server.state.ConfigHelperTest.RunWithInMemoryDefaultTestModule.configHelper.overrideAttributes(overrideConfig, persistedAttributes);
            junit.framework.Assert.assertNotNull(result);
            junit.framework.Assert.assertEquals(1, result.size());
            java.util.Map<java.lang.String, java.lang.String> finalResultAttributes = result.get("final");
            junit.framework.Assert.assertNotNull(finalResultAttributes);
            junit.framework.Assert.assertEquals(2, finalResultAttributes.size());
            junit.framework.Assert.assertEquals("true", finalResultAttributes.get("b"));
            junit.framework.Assert.assertEquals("true", finalResultAttributes.get("d"));
        }

        @org.junit.Test
        public void testMergeAttributesWithNoAttributeOverrides() throws java.lang.Exception {
            java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> persistedAttributes = new java.util.HashMap<>();
            java.util.Map<java.lang.String, java.lang.String> persistedFinalAttrs = new java.util.HashMap<>();
            persistedFinalAttrs.put("a", "true");
            persistedFinalAttrs.put("c", "true");
            persistedFinalAttrs.put("d", "true");
            persistedAttributes.put("final", persistedFinalAttrs);
            java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> confGroupAttributes = new java.util.HashMap<>();
            java.util.Map<java.lang.String, java.lang.String> confGroupProperties = new java.util.HashMap<>();
            confGroupProperties.put("a", "any");
            confGroupProperties.put("b", "any");
            confGroupProperties.put("c", "any");
            org.apache.ambari.server.state.Config overrideConfig = org.apache.ambari.server.state.ConfigHelperTest.RunWithInMemoryDefaultTestModule.configFactory.createNew(org.apache.ambari.server.state.ConfigHelperTest.RunWithInMemoryDefaultTestModule.cluster, "type", null, confGroupProperties, confGroupAttributes);
            java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> result = org.apache.ambari.server.state.ConfigHelperTest.RunWithInMemoryDefaultTestModule.configHelper.overrideAttributes(overrideConfig, persistedAttributes);
            junit.framework.Assert.assertNotNull(result);
            junit.framework.Assert.assertEquals(1, result.size());
            java.util.Map<java.lang.String, java.lang.String> finalResultAttributes = result.get("final");
            junit.framework.Assert.assertNotNull(finalResultAttributes);
            junit.framework.Assert.assertEquals(1, finalResultAttributes.size());
            junit.framework.Assert.assertEquals("true", finalResultAttributes.get("d"));
        }

        @org.junit.Test
        public void testMergeAttributesWithNullAttributes() throws java.lang.Exception {
            java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> persistedAttributes = new java.util.HashMap<>();
            java.util.Map<java.lang.String, java.lang.String> persistedFinalAttrs = new java.util.HashMap<>();
            persistedFinalAttrs.put("a", "true");
            persistedFinalAttrs.put("c", "true");
            persistedFinalAttrs.put("d", "true");
            persistedAttributes.put("final", persistedFinalAttrs);
            java.util.Map<java.lang.String, java.lang.String> confGroupProperties = new java.util.HashMap<>();
            confGroupProperties.put("a", "any");
            confGroupProperties.put("b", "any");
            confGroupProperties.put("c", "any");
            org.apache.ambari.server.state.Config overrideConfig = org.apache.ambari.server.state.ConfigHelperTest.RunWithInMemoryDefaultTestModule.configFactory.createNew(org.apache.ambari.server.state.ConfigHelperTest.RunWithInMemoryDefaultTestModule.cluster, "type", null, confGroupProperties, null);
            java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> result = org.apache.ambari.server.state.ConfigHelperTest.RunWithInMemoryDefaultTestModule.configHelper.overrideAttributes(overrideConfig, persistedAttributes);
            junit.framework.Assert.assertNotNull(result);
            junit.framework.Assert.assertEquals(1, result.size());
            java.util.Map<java.lang.String, java.lang.String> finalResultAttributes = result.get("final");
            junit.framework.Assert.assertNotNull(finalResultAttributes);
            junit.framework.Assert.assertEquals(3, finalResultAttributes.size());
            junit.framework.Assert.assertEquals("true", finalResultAttributes.get("a"));
            junit.framework.Assert.assertEquals("true", finalResultAttributes.get("c"));
            junit.framework.Assert.assertEquals("true", finalResultAttributes.get("d"));
        }

        @org.junit.Test
        public void testFilterInvalidPropertyValues() {
            java.util.Map<org.apache.ambari.server.state.PropertyInfo, java.lang.String> properties = new java.util.HashMap<>();
            org.apache.ambari.server.state.PropertyInfo prop1 = new org.apache.ambari.server.state.PropertyInfo();
            prop1.setName("1");
            org.apache.ambari.server.state.PropertyInfo prop2 = new org.apache.ambari.server.state.PropertyInfo();
            prop1.setName("2");
            org.apache.ambari.server.state.PropertyInfo prop3 = new org.apache.ambari.server.state.PropertyInfo();
            prop1.setName("3");
            org.apache.ambari.server.state.PropertyInfo prop4 = new org.apache.ambari.server.state.PropertyInfo();
            prop1.setName("4");
            properties.put(prop1, "/tmp");
            properties.put(prop2, "null");
            properties.put(prop3, "");
            properties.put(prop4, null);
            java.util.Set<java.lang.String> resultSet = org.apache.ambari.server.state.ConfigHelperTest.RunWithInMemoryDefaultTestModule.configHelper.filterInvalidPropertyValues(properties, "testlist");
            junit.framework.Assert.assertEquals(1, resultSet.size());
            junit.framework.Assert.assertEquals(resultSet.iterator().next(), "/tmp");
        }

        @org.junit.Test
        public void testMergeAttributesWithNullProperties() throws java.lang.Exception {
            java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> persistedAttributes = new java.util.HashMap<>();
            java.util.Map<java.lang.String, java.lang.String> persistedFinalAttrs = new java.util.HashMap<>();
            persistedFinalAttrs.put("a", "true");
            persistedFinalAttrs.put("c", "true");
            persistedFinalAttrs.put("d", "true");
            persistedAttributes.put("final", persistedFinalAttrs);
            java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> confGroupAttributes = new java.util.HashMap<>();
            java.util.Map<java.lang.String, java.lang.String> confGroupFinalAttrs = new java.util.HashMap<>();
            confGroupFinalAttrs.put("b", "true");
            confGroupAttributes.put("final", confGroupFinalAttrs);
            org.apache.ambari.server.state.Config overrideConfig = org.apache.ambari.server.state.ConfigHelperTest.RunWithInMemoryDefaultTestModule.configFactory.createNew(org.apache.ambari.server.state.ConfigHelperTest.RunWithInMemoryDefaultTestModule.cluster, "type", "version122", new java.util.HashMap<>(), confGroupAttributes);
            java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> result = org.apache.ambari.server.state.ConfigHelperTest.RunWithInMemoryDefaultTestModule.configHelper.overrideAttributes(overrideConfig, persistedAttributes);
            junit.framework.Assert.assertNotNull(result);
            junit.framework.Assert.assertEquals(1, result.size());
            java.util.Map<java.lang.String, java.lang.String> finalResultAttributes = result.get("final");
            junit.framework.Assert.assertNotNull(finalResultAttributes);
            junit.framework.Assert.assertEquals(4, finalResultAttributes.size());
            junit.framework.Assert.assertEquals("true", finalResultAttributes.get("a"));
            junit.framework.Assert.assertEquals("true", finalResultAttributes.get("b"));
            junit.framework.Assert.assertEquals("true", finalResultAttributes.get("c"));
            junit.framework.Assert.assertEquals("true", finalResultAttributes.get("d"));
        }

        @org.junit.Test
        public void testUpdateConfigType() throws java.lang.Exception {
            org.apache.ambari.server.state.Config currentConfig = org.apache.ambari.server.state.ConfigHelperTest.RunWithInMemoryDefaultTestModule.cluster.getDesiredConfigByType("core-site");
            java.util.Map<java.lang.String, java.lang.String> properties = currentConfig.getProperties();
            java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> propertiesAttributes = currentConfig.getPropertiesAttributes();
            junit.framework.Assert.assertNotNull(propertiesAttributes);
            junit.framework.Assert.assertEquals(1, propertiesAttributes.size());
            junit.framework.Assert.assertTrue(propertiesAttributes.containsKey("attribute1"));
            junit.framework.Assert.assertEquals("version1", currentConfig.getTag());
            junit.framework.Assert.assertEquals("30", properties.get("fs.trash.interval"));
            junit.framework.Assert.assertTrue(properties.containsKey("ipc.client.connect.max.retries"));
            junit.framework.Assert.assertTrue(propertiesAttributes.get("attribute1").containsKey("ipc.client.connect.max.retries"));
            java.util.Map<java.lang.String, java.lang.String> updates = new java.util.HashMap<>();
            updates.put("new-property", "new-value");
            updates.put("fs.trash.interval", "updated-value");
            java.util.Collection<java.lang.String> removals = java.util.Collections.singletonList("ipc.client.connect.max.retries");
            org.apache.ambari.server.state.ConfigHelperTest.RunWithInMemoryDefaultTestModule.configHelper.updateConfigType(org.apache.ambari.server.state.ConfigHelperTest.RunWithInMemoryDefaultTestModule.cluster, org.apache.ambari.server.state.ConfigHelperTest.RunWithInMemoryDefaultTestModule.cluster.getCurrentStackVersion(), org.apache.ambari.server.state.ConfigHelperTest.RunWithInMemoryDefaultTestModule.managementController, "core-site", updates, removals, "admin", "Test note");
            org.apache.ambari.server.state.Config updatedConfig = org.apache.ambari.server.state.ConfigHelperTest.RunWithInMemoryDefaultTestModule.cluster.getDesiredConfigByType("core-site");
            propertiesAttributes = updatedConfig.getPropertiesAttributes();
            junit.framework.Assert.assertNotNull(propertiesAttributes);
            junit.framework.Assert.assertEquals(1, propertiesAttributes.size());
            junit.framework.Assert.assertTrue(propertiesAttributes.containsKey("attribute1"));
            junit.framework.Assert.assertFalse("version1".equals(updatedConfig.getTag()));
            properties = updatedConfig.getProperties();
            junit.framework.Assert.assertTrue(properties.containsKey("new-property"));
            junit.framework.Assert.assertEquals("new-value", properties.get("new-property"));
            junit.framework.Assert.assertTrue(properties.containsKey("fs.trash.interval"));
            junit.framework.Assert.assertEquals("updated-value", properties.get("fs.trash.interval"));
            junit.framework.Assert.assertEquals("2", propertiesAttributes.get("attribute1").get("fs.trash.interval"));
            junit.framework.Assert.assertFalse(properties.containsKey("ipc.client.connect.max.retries"));
            junit.framework.Assert.assertFalse(propertiesAttributes.get("attribute1").containsKey("ipc.client.connect.max.retries"));
        }

        @org.junit.Test
        public void testUpdateConfigTypeNoPropertyAttributes() throws java.lang.Exception {
            org.apache.ambari.server.state.Config currentConfig = org.apache.ambari.server.state.ConfigHelperTest.RunWithInMemoryDefaultTestModule.cluster.getDesiredConfigByType("oozie-site");
            java.util.Map<java.lang.String, java.lang.String> properties = currentConfig.getProperties();
            junit.framework.Assert.assertEquals("version1", currentConfig.getTag());
            junit.framework.Assert.assertEquals("simple", properties.get("oozie.authentication.type"));
            junit.framework.Assert.assertEquals("false", properties.get("oozie.service.HadoopAccessorService.kerberos.enabled"));
            java.util.Map<java.lang.String, java.lang.String> updates = new java.util.HashMap<>();
            updates.put("oozie.authentication.type", "kerberos");
            updates.put("oozie.service.HadoopAccessorService.kerberos.enabled", "true");
            org.apache.ambari.server.state.ConfigHelperTest.RunWithInMemoryDefaultTestModule.configHelper.updateConfigType(org.apache.ambari.server.state.ConfigHelperTest.RunWithInMemoryDefaultTestModule.cluster, org.apache.ambari.server.state.ConfigHelperTest.RunWithInMemoryDefaultTestModule.cluster.getCurrentStackVersion(), org.apache.ambari.server.state.ConfigHelperTest.RunWithInMemoryDefaultTestModule.managementController, "oozie-site", updates, null, "admin", "Test " + "note");
            org.apache.ambari.server.state.Config updatedConfig = org.apache.ambari.server.state.ConfigHelperTest.RunWithInMemoryDefaultTestModule.cluster.getDesiredConfigByType("oozie-site");
            junit.framework.Assert.assertFalse("version1".equals(updatedConfig.getTag()));
            properties = updatedConfig.getProperties();
            junit.framework.Assert.assertTrue(properties.containsKey("oozie.authentication.type"));
            junit.framework.Assert.assertEquals("kerberos", properties.get("oozie.authentication.type"));
            junit.framework.Assert.assertTrue(properties.containsKey("oozie.service.HadoopAccessorService.kerberos.enabled"));
            junit.framework.Assert.assertEquals("true", properties.get("oozie.service.HadoopAccessorService.kerberos.enabled"));
        }

        @org.junit.Test
        public void testUpdateConfigTypeRemovals() throws java.lang.Exception {
            org.apache.ambari.server.state.Config currentConfig = org.apache.ambari.server.state.ConfigHelperTest.RunWithInMemoryDefaultTestModule.cluster.getDesiredConfigByType("ams-site");
            java.util.Map<java.lang.String, java.lang.String> properties = currentConfig.getProperties();
            junit.framework.Assert.assertEquals("version1", currentConfig.getTag());
            junit.framework.Assert.assertEquals("embedded", properties.get("timeline.service.operating.mode"));
            junit.framework.Assert.assertEquals("false", properties.get("timeline.service.fifo.enabled"));
            java.util.List<java.lang.String> removals = new java.util.ArrayList<>();
            removals.add("timeline.service.operating.mode");
            org.apache.ambari.server.state.ConfigHelperTest.RunWithInMemoryDefaultTestModule.configHelper.updateConfigType(org.apache.ambari.server.state.ConfigHelperTest.RunWithInMemoryDefaultTestModule.cluster, org.apache.ambari.server.state.ConfigHelperTest.RunWithInMemoryDefaultTestModule.cluster.getCurrentStackVersion(), org.apache.ambari.server.state.ConfigHelperTest.RunWithInMemoryDefaultTestModule.managementController, "ams-site", null, removals, "admin", "Test note");
            org.apache.ambari.server.state.Config updatedConfig = org.apache.ambari.server.state.ConfigHelperTest.RunWithInMemoryDefaultTestModule.cluster.getDesiredConfigByType("ams-site");
            junit.framework.Assert.assertFalse("version1".equals(updatedConfig.getTag()));
            properties = updatedConfig.getProperties();
            junit.framework.Assert.assertFalse(properties.containsKey("timeline.service.operating.mode"));
            junit.framework.Assert.assertTrue(properties.containsKey("timeline.service.fifo.enabled"));
            junit.framework.Assert.assertEquals("false", properties.get("timeline.service.fifo.enabled"));
        }

        @org.junit.Test
        public void testCalculateIsStaleConfigs() throws java.lang.Exception {
            java.util.Map<java.lang.String, org.apache.ambari.server.state.HostConfig> schReturn = new java.util.HashMap<>();
            org.apache.ambari.server.state.HostConfig hc = new org.apache.ambari.server.state.HostConfig();
            hc.setDefaultVersionTag("version2");
            schReturn.put("flume-conf", hc);
            org.apache.ambari.server.state.ServiceComponent sc = EasyMock.createNiceMock(org.apache.ambari.server.state.ServiceComponent.class);
            org.apache.ambari.server.state.ServiceComponentHost sch = EasyMock.createNiceMock(org.apache.ambari.server.state.ServiceComponentHost.class);
            EasyMock.expect(sc.getDesiredStackId()).andReturn(org.apache.ambari.server.state.ConfigHelperTest.RunWithInMemoryDefaultTestModule.cluster.getDesiredStackVersion()).anyTimes();
            EasyMock.expect(sch.getActualConfigs()).andReturn(schReturn).times(6);
            EasyMock.expect(sch.getHostName()).andReturn("h1").anyTimes();
            EasyMock.expect(sch.getClusterId()).andReturn(org.apache.ambari.server.state.ConfigHelperTest.RunWithInMemoryDefaultTestModule.cluster.getClusterId()).anyTimes();
            EasyMock.expect(sch.getServiceName()).andReturn("FLUME").anyTimes();
            EasyMock.expect(sch.getServiceComponentName()).andReturn("FLUME_HANDLER").anyTimes();
            EasyMock.expect(sch.getServiceComponent()).andReturn(sc).anyTimes();
            EasyMock.replay(sc, sch);
            junit.framework.Assert.assertTrue(org.apache.ambari.server.state.ConfigHelperTest.RunWithInMemoryDefaultTestModule.configHelper.isStaleConfigs(sch, null));
            org.apache.ambari.server.state.HostConfig hc2 = new org.apache.ambari.server.state.HostConfig();
            hc2.setDefaultVersionTag("version1");
            schReturn.put("flume-conf", hc2);
            junit.framework.Assert.assertFalse(org.apache.ambari.server.state.ConfigHelperTest.RunWithInMemoryDefaultTestModule.configHelper.isStaleConfigs(sch, null));
            java.util.List<java.lang.String> hosts = new java.util.ArrayList<>();
            hosts.add("h1");
            java.util.List<org.apache.ambari.server.state.Config> configs = new java.util.ArrayList<>();
            org.apache.ambari.server.state.Config configImpl = org.apache.ambari.server.state.ConfigHelperTest.RunWithInMemoryDefaultTestModule.configFactory.createNew(org.apache.ambari.server.state.ConfigHelperTest.RunWithInMemoryDefaultTestModule.cluster, "flume-conf", "FLUME1", new java.util.HashMap<>(), null);
            configs.add(configImpl);
            addConfigGroup("configGroup1", "FLUME", hosts, configs);
            junit.framework.Assert.assertTrue(org.apache.ambari.server.state.ConfigHelperTest.RunWithInMemoryDefaultTestModule.configHelper.isStaleConfigs(sch, null));
            org.apache.ambari.server.state.HostConfig hc3 = new org.apache.ambari.server.state.HostConfig();
            hc3.setDefaultVersionTag("version1");
            hc3.getConfigGroupOverrides().put(1L, "FLUME1");
            schReturn.put("flume-conf", hc3);
            junit.framework.Assert.assertFalse(org.apache.ambari.server.state.ConfigHelperTest.RunWithInMemoryDefaultTestModule.configHelper.isStaleConfigs(sch, null));
            org.apache.ambari.server.state.HostConfig hc4 = new org.apache.ambari.server.state.HostConfig();
            hc4.setDefaultVersionTag("version1");
            hc4.getConfigGroupOverrides().put(1L, "FLUME2");
            schReturn.put("flume-conf", hc4);
            junit.framework.Assert.assertTrue(org.apache.ambari.server.state.ConfigHelperTest.RunWithInMemoryDefaultTestModule.configHelper.isStaleConfigs(sch, null));
            org.apache.ambari.server.state.HostConfig hc5 = new org.apache.ambari.server.state.HostConfig();
            hc5.setDefaultVersionTag("version3");
            hc5.getConfigGroupOverrides().put(1L, "FLUME1");
            schReturn.put("flume-conf", hc5);
            junit.framework.Assert.assertTrue(org.apache.ambari.server.state.ConfigHelperTest.RunWithInMemoryDefaultTestModule.configHelper.isStaleConfigs(sch, null));
            EasyMock.verify(sch);
        }

        @org.junit.Test
        public void testCalculateRefreshCommands() throws java.lang.Exception {
            java.util.Map<java.lang.String, org.apache.ambari.server.state.HostConfig> schReturn = new java.util.HashMap<>();
            org.apache.ambari.server.state.HostConfig hc = new org.apache.ambari.server.state.HostConfig();
            hc.setDefaultVersionTag("version1");
            schReturn.put("hdfs-site", hc);
            org.apache.ambari.server.state.ServiceComponent sc = EasyMock.createNiceMock(org.apache.ambari.server.state.ServiceComponent.class);
            org.apache.ambari.server.state.ServiceComponentHost sch = EasyMock.createNiceMock(org.apache.ambari.server.state.ServiceComponentHost.class);
            EasyMock.expect(sc.getDesiredStackId()).andReturn(org.apache.ambari.server.state.ConfigHelperTest.RunWithInMemoryDefaultTestModule.cluster.getDesiredStackVersion()).anyTimes();
            EasyMock.expect(sch.getActualConfigs()).andReturn(schReturn).anyTimes();
            EasyMock.expect(sch.getHostName()).andReturn("h1").anyTimes();
            EasyMock.expect(sch.getClusterId()).andReturn(org.apache.ambari.server.state.ConfigHelperTest.RunWithInMemoryDefaultTestModule.cluster.getClusterId()).anyTimes();
            EasyMock.expect(sch.getServiceName()).andReturn("HDFS").anyTimes();
            EasyMock.expect(sch.getServiceComponentName()).andReturn("NAMENODE").anyTimes();
            EasyMock.expect(sch.getServiceComponent()).andReturn(sc).anyTimes();
            EasyMock.replay(sc, sch);
            junit.framework.Assert.assertTrue(org.apache.ambari.server.state.ConfigHelperTest.RunWithInMemoryDefaultTestModule.configHelper.isStaleConfigs(sch, null));
            java.lang.String refreshConfigsCommand = org.apache.ambari.server.state.ConfigHelperTest.RunWithInMemoryDefaultTestModule.configHelper.getRefreshConfigsCommand(org.apache.ambari.server.state.ConfigHelperTest.RunWithInMemoryDefaultTestModule.cluster, sch);
            junit.framework.Assert.assertEquals("reload_configs", refreshConfigsCommand);
            EasyMock.verify(sch);
        }

        @org.junit.Test
        @java.lang.SuppressWarnings("unchecked")
        public void testFindChangedKeys() throws org.apache.ambari.server.AmbariException, org.apache.ambari.server.security.authorization.AuthorizationException, java.lang.NoSuchMethodException, java.lang.reflect.InvocationTargetException, java.lang.IllegalAccessException {
            final java.lang.String configType = "hdfs-site";
            final java.lang.String newPropertyName = "new.property";
            org.apache.ambari.server.controller.ConfigurationRequest newProperty = new org.apache.ambari.server.controller.ConfigurationRequest();
            newProperty.setClusterName(org.apache.ambari.server.state.ConfigHelperTest.RunWithInMemoryDefaultTestModule.clusterName);
            newProperty.setType(configType);
            newProperty.setVersionTag("version3");
            newProperty.setProperties(new java.util.HashMap<java.lang.String, java.lang.String>() {
                {
                    put("hadoop.caller.context.enabled", "false");
                    put(newPropertyName, "true");
                }
            });
            newProperty.setPropertiesAttributes(null);
            final org.apache.ambari.server.controller.ClusterRequest clusterRequestNewProperty = new org.apache.ambari.server.controller.ClusterRequest(org.apache.ambari.server.state.ConfigHelperTest.RunWithInMemoryDefaultTestModule.cluster.getClusterId(), org.apache.ambari.server.state.ConfigHelperTest.RunWithInMemoryDefaultTestModule.clusterName, org.apache.ambari.server.state.ConfigHelperTest.RunWithInMemoryDefaultTestModule.cluster.getDesiredStackVersion().getStackVersion(), null);
            clusterRequestNewProperty.setDesiredConfig(java.util.Collections.singletonList(newProperty));
            org.apache.ambari.server.state.ConfigHelperTest.RunWithInMemoryDefaultTestModule.managementController.updateClusters(new java.util.HashSet<org.apache.ambari.server.controller.ClusterRequest>() {
                {
                    add(clusterRequestNewProperty);
                }
            }, null);
            org.apache.ambari.server.controller.ConfigurationRequest removedNewProperty = new org.apache.ambari.server.controller.ConfigurationRequest();
            removedNewProperty.setClusterName(org.apache.ambari.server.state.ConfigHelperTest.RunWithInMemoryDefaultTestModule.clusterName);
            removedNewProperty.setType(configType);
            removedNewProperty.setVersionTag("version4");
            removedNewProperty.setProperties(new java.util.HashMap<java.lang.String, java.lang.String>() {
                {
                    put("hadoop.caller.context.enabled", "false");
                }
            });
            removedNewProperty.setPropertiesAttributes(null);
            final org.apache.ambari.server.controller.ClusterRequest clusterRequestRemovedNewProperty = new org.apache.ambari.server.controller.ClusterRequest(org.apache.ambari.server.state.ConfigHelperTest.RunWithInMemoryDefaultTestModule.cluster.getClusterId(), org.apache.ambari.server.state.ConfigHelperTest.RunWithInMemoryDefaultTestModule.clusterName, org.apache.ambari.server.state.ConfigHelperTest.RunWithInMemoryDefaultTestModule.cluster.getDesiredStackVersion().getStackVersion(), null);
            clusterRequestRemovedNewProperty.setDesiredConfig(java.util.Collections.singletonList(removedNewProperty));
            org.apache.ambari.server.state.ConfigHelperTest.RunWithInMemoryDefaultTestModule.managementController.updateClusters(new java.util.HashSet<org.apache.ambari.server.controller.ClusterRequest>() {
                {
                    add(clusterRequestRemovedNewProperty);
                }
            }, null);
            org.apache.ambari.server.state.ConfigHelper configHelper = org.apache.ambari.server.state.ConfigHelperTest.RunWithInMemoryDefaultTestModule.injector.getInstance(org.apache.ambari.server.state.ConfigHelper.class);
            java.lang.reflect.Method method = org.apache.ambari.server.state.ConfigHelper.class.getDeclaredMethod("findChangedKeys", org.apache.ambari.server.state.Cluster.class, java.lang.String.class, java.util.Collection.class, java.util.Collection.class);
            method.setAccessible(true);
            java.util.Collection<java.lang.String> value = ((java.util.Collection<java.lang.String>) (method.invoke(configHelper, org.apache.ambari.server.state.ConfigHelperTest.RunWithInMemoryDefaultTestModule.cluster, configType, java.util.Collections.singletonList("version1"), java.util.Collections.singletonList("version2"))));
            org.junit.Assert.assertTrue(value.size() == 1);
            org.junit.Assert.assertEquals(configType + "/hadoop.caller.context.enabled", value.iterator().next());
            value = ((java.util.Collection<java.lang.String>) (method.invoke(configHelper, org.apache.ambari.server.state.ConfigHelperTest.RunWithInMemoryDefaultTestModule.cluster, configType, java.util.Collections.singletonList("version2"), java.util.Collections.singletonList("version3"))));
            org.junit.Assert.assertTrue(value.size() == 1);
            org.junit.Assert.assertEquals((configType + "/") + newPropertyName, value.iterator().next());
            value = ((java.util.Collection<java.lang.String>) (method.invoke(configHelper, org.apache.ambari.server.state.ConfigHelperTest.RunWithInMemoryDefaultTestModule.cluster, configType, java.util.Collections.singletonList("version3"), java.util.Collections.singletonList("version4"))));
            org.junit.Assert.assertTrue(value.size() == 1);
            org.junit.Assert.assertEquals((configType + "/") + newPropertyName, value.iterator().next());
        }
    }

    public static class RunWithCustomModule {
        private com.google.inject.Injector injector;

        @org.junit.Before
        public void setup() throws java.lang.Exception {
            injector = com.google.inject.Guice.createInjector(new com.google.inject.AbstractModule() {
                @java.lang.Override
                protected void configure() {
                    final org.apache.ambari.server.api.services.AmbariMetaInfo mockMetaInfo = EasyMock.createNiceMock(org.apache.ambari.server.api.services.AmbariMetaInfo.class);
                    final org.apache.ambari.server.controller.spi.ClusterController clusterController = EasyMock.createStrictMock(org.apache.ambari.server.controller.spi.ClusterController.class);
                    org.apache.ambari.server.testutils.PartialNiceMockBinder.newBuilder().addAmbariMetaInfoBinding().addFactoriesInstallBinding().addLdapBindings().build().configure(binder());
                    bind(javax.persistence.EntityManager.class).toInstance(EasyMock.createNiceMock(javax.persistence.EntityManager.class));
                    bind(org.apache.ambari.server.orm.DBAccessor.class).toInstance(EasyMock.createNiceMock(org.apache.ambari.server.orm.DBAccessor.class));
                    bind(org.apache.ambari.server.security.SecurityHelper.class).toInstance(EasyMock.createNiceMock(org.apache.ambari.server.security.SecurityHelper.class));
                    bind(org.apache.ambari.server.state.stack.OsFamily.class).toInstance(EasyMock.createNiceMock(org.apache.ambari.server.state.stack.OsFamily.class));
                    bind(org.apache.ambari.server.controller.AmbariCustomCommandExecutionHelper.class).toInstance(EasyMock.createNiceMock(org.apache.ambari.server.controller.AmbariCustomCommandExecutionHelper.class));
                    bind(org.apache.ambari.server.api.services.AmbariMetaInfo.class).toInstance(mockMetaInfo);
                    bind(org.apache.ambari.server.state.Clusters.class).toInstance(EasyMock.createNiceMock(org.apache.ambari.server.state.Clusters.class));
                    bind(org.apache.ambari.server.controller.spi.ClusterController.class).toInstance(clusterController);
                    bind(org.apache.ambari.server.stack.StackManagerFactory.class).toInstance(EasyMock.createNiceMock(org.apache.ambari.server.stack.StackManagerFactory.class));
                    bind(org.apache.ambari.server.orm.dao.HostRoleCommandDAO.class).toInstance(EasyMock.createNiceMock(org.apache.ambari.server.orm.dao.HostRoleCommandDAO.class));
                    bind(org.apache.ambari.server.events.publishers.STOMPUpdatePublisher.class).toInstance(EasyMock.createNiceMock(org.apache.ambari.server.events.publishers.STOMPUpdatePublisher.class));
                    bind(org.apache.ambari.server.mpack.MpackManagerFactory.class).toInstance(EasyMock.createNiceMock(org.apache.ambari.server.mpack.MpackManagerFactory.class));
                }
            });
            org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(org.apache.ambari.server.security.TestAuthenticationFactory.createAdministrator("admin"));
        }

        @org.junit.After
        public void teardown() {
            org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(null);
        }

        @org.junit.Test
        public void testGetServicePropertiesSimpleInvocation() throws java.lang.Exception {
            org.apache.ambari.server.state.Cluster mockCluster = EasyMock.createStrictMock(org.apache.ambari.server.state.Cluster.class);
            org.apache.ambari.server.state.StackId mockStackVersion = EasyMock.createStrictMock(org.apache.ambari.server.state.StackId.class);
            org.apache.ambari.server.api.services.AmbariMetaInfo mockAmbariMetaInfo = injector.getInstance(org.apache.ambari.server.api.services.AmbariMetaInfo.class);
            org.apache.ambari.server.state.Service mockService = EasyMock.createStrictMock(org.apache.ambari.server.state.Service.class);
            org.apache.ambari.server.state.ServiceInfo mockServiceInfo = EasyMock.createStrictMock(org.apache.ambari.server.state.ServiceInfo.class);
            org.apache.ambari.server.state.PropertyInfo mockPropertyInfo1 = EasyMock.createStrictMock(org.apache.ambari.server.state.PropertyInfo.class);
            org.apache.ambari.server.state.PropertyInfo mockPropertyInfo2 = EasyMock.createStrictMock(org.apache.ambari.server.state.PropertyInfo.class);
            java.util.List<org.apache.ambari.server.state.PropertyInfo> serviceProperties = java.util.Arrays.asList(mockPropertyInfo1, mockPropertyInfo2);
            EasyMock.expect(mockCluster.getService("SERVICE")).andReturn(mockService).once();
            EasyMock.expect(mockService.getDesiredStackId()).andReturn(mockStackVersion).once();
            EasyMock.expect(mockStackVersion.getStackName()).andReturn("HDP").once();
            EasyMock.expect(mockStackVersion.getStackVersion()).andReturn("2.2").once();
            EasyMock.expect(mockAmbariMetaInfo.getService("HDP", "2.2", "SERVICE")).andReturn(mockServiceInfo).once();
            EasyMock.expect(mockServiceInfo.getProperties()).andReturn(serviceProperties).once();
            EasyMock.replay(mockAmbariMetaInfo, mockCluster, mockService, mockStackVersion, mockServiceInfo, mockPropertyInfo1, mockPropertyInfo2);
            mockAmbariMetaInfo.init();
            java.util.Set<org.apache.ambari.server.state.PropertyInfo> result = injector.getInstance(org.apache.ambari.server.state.ConfigHelper.class).getServiceProperties(mockCluster, "SERVICE");
            junit.framework.Assert.assertNotNull(result);
            junit.framework.Assert.assertEquals(2, result.size());
            EasyMock.verify(mockAmbariMetaInfo, mockCluster, mockStackVersion, mockServiceInfo, mockPropertyInfo1, mockPropertyInfo2);
        }

        @org.junit.Test
        public void testGetServicePropertiesDoNoRemoveExcluded() throws java.lang.Exception {
            org.apache.ambari.server.state.StackId mockStackVersion = EasyMock.createStrictMock(org.apache.ambari.server.state.StackId.class);
            org.apache.ambari.server.api.services.AmbariMetaInfo mockAmbariMetaInfo = injector.getInstance(org.apache.ambari.server.api.services.AmbariMetaInfo.class);
            org.apache.ambari.server.state.ServiceInfo mockServiceInfo = EasyMock.createStrictMock(org.apache.ambari.server.state.ServiceInfo.class);
            org.apache.ambari.server.state.PropertyInfo mockPropertyInfo1 = EasyMock.createStrictMock(org.apache.ambari.server.state.PropertyInfo.class);
            org.apache.ambari.server.state.PropertyInfo mockPropertyInfo2 = EasyMock.createStrictMock(org.apache.ambari.server.state.PropertyInfo.class);
            java.util.List<org.apache.ambari.server.state.PropertyInfo> serviceProperties = java.util.Arrays.asList(mockPropertyInfo1, mockPropertyInfo2);
            EasyMock.expect(mockStackVersion.getStackName()).andReturn("HDP").once();
            EasyMock.expect(mockStackVersion.getStackVersion()).andReturn("2.2").once();
            EasyMock.expect(mockAmbariMetaInfo.getService("HDP", "2.2", "SERVICE")).andReturn(mockServiceInfo).once();
            EasyMock.expect(mockServiceInfo.getProperties()).andReturn(serviceProperties).once();
            EasyMock.replay(mockAmbariMetaInfo, mockStackVersion, mockServiceInfo, mockPropertyInfo1, mockPropertyInfo2);
            mockAmbariMetaInfo.init();
            java.util.Set<org.apache.ambari.server.state.PropertyInfo> result = injector.getInstance(org.apache.ambari.server.state.ConfigHelper.class).getServiceProperties(mockStackVersion, "SERVICE", false);
            junit.framework.Assert.assertNotNull(result);
            junit.framework.Assert.assertEquals(2, result.size());
            EasyMock.verify(mockAmbariMetaInfo, mockStackVersion, mockServiceInfo, mockPropertyInfo1, mockPropertyInfo2);
        }

        @org.junit.Test
        public void testGetServicePropertiesRemoveExcluded() throws java.lang.Exception {
            org.apache.ambari.server.state.StackId mockStackVersion = EasyMock.createStrictMock(org.apache.ambari.server.state.StackId.class);
            org.apache.ambari.server.api.services.AmbariMetaInfo mockAmbariMetaInfo = injector.getInstance(org.apache.ambari.server.api.services.AmbariMetaInfo.class);
            org.apache.ambari.server.state.ServiceInfo mockServiceInfo = EasyMock.createStrictMock(org.apache.ambari.server.state.ServiceInfo.class);
            org.apache.ambari.server.state.PropertyInfo mockPropertyInfo1 = EasyMock.createStrictMock(org.apache.ambari.server.state.PropertyInfo.class);
            org.apache.ambari.server.state.PropertyInfo mockPropertyInfo2 = EasyMock.createStrictMock(org.apache.ambari.server.state.PropertyInfo.class);
            java.util.List<org.apache.ambari.server.state.PropertyInfo> serviceProperties = java.util.Arrays.asList(mockPropertyInfo1, mockPropertyInfo2);
            EasyMock.expect(mockStackVersion.getStackName()).andReturn("HDP").once();
            EasyMock.expect(mockStackVersion.getStackVersion()).andReturn("2.2").once();
            EasyMock.expect(mockAmbariMetaInfo.getService("HDP", "2.2", "SERVICE")).andReturn(mockServiceInfo).once();
            EasyMock.expect(mockServiceInfo.getProperties()).andReturn(serviceProperties).once();
            EasyMock.expect(mockServiceInfo.getExcludedConfigTypes()).andReturn(java.util.Collections.singleton("excluded-type")).once();
            EasyMock.expect(mockPropertyInfo1.getFilename()).andReturn("included-type.xml").times(2);
            EasyMock.expect(mockPropertyInfo2.getFilename()).andReturn("excluded-type.xml").once();
            EasyMock.replay(mockAmbariMetaInfo, mockStackVersion, mockServiceInfo, mockPropertyInfo1, mockPropertyInfo2);
            mockAmbariMetaInfo.init();
            java.util.Set<org.apache.ambari.server.state.PropertyInfo> result = injector.getInstance(org.apache.ambari.server.state.ConfigHelper.class).getServiceProperties(mockStackVersion, "SERVICE", true);
            junit.framework.Assert.assertNotNull(result);
            junit.framework.Assert.assertEquals(1, result.size());
            junit.framework.Assert.assertEquals("included-type.xml", result.iterator().next().getFilename());
            EasyMock.verify(mockAmbariMetaInfo, mockStackVersion, mockServiceInfo, mockPropertyInfo1, mockPropertyInfo2);
        }
    }

    public static class RunWithoutModules {
        @org.junit.Test
        public void nullsAreEqual() {
            org.junit.Assert.assertTrue(org.apache.ambari.server.state.ConfigHelper.valuesAreEqual(null, null));
        }

        @org.junit.Test
        public void equalStringsAreEqual() {
            org.junit.Assert.assertTrue(org.apache.ambari.server.state.ConfigHelper.valuesAreEqual("asdf", "asdf"));
            org.junit.Assert.assertTrue(org.apache.ambari.server.state.ConfigHelper.valuesAreEqual("qwerty", "qwerty"));
        }

        @org.junit.Test
        public void nullIsNotEqualWithNonNull() {
            org.junit.Assert.assertFalse(org.apache.ambari.server.state.ConfigHelper.valuesAreEqual(null, "asdf"));
            org.junit.Assert.assertFalse(org.apache.ambari.server.state.ConfigHelper.valuesAreEqual("asdf", null));
        }

        @org.junit.Test
        public void equalNumbersInDifferentFormsAreEqual() {
            org.junit.Assert.assertTrue(org.apache.ambari.server.state.ConfigHelper.valuesAreEqual("1.234", "1.2340"));
            org.junit.Assert.assertTrue(org.apache.ambari.server.state.ConfigHelper.valuesAreEqual("12.34", "1.234e1"));
            org.junit.Assert.assertTrue(org.apache.ambari.server.state.ConfigHelper.valuesAreEqual("123L", "123l"));
            org.junit.Assert.assertTrue(org.apache.ambari.server.state.ConfigHelper.valuesAreEqual("-1.234", "-1.2340"));
            org.junit.Assert.assertTrue(org.apache.ambari.server.state.ConfigHelper.valuesAreEqual("-12.34", "-1.234e1"));
            org.junit.Assert.assertTrue(org.apache.ambari.server.state.ConfigHelper.valuesAreEqual("-123L", "-123l"));
            org.junit.Assert.assertTrue(org.apache.ambari.server.state.ConfigHelper.valuesAreEqual("1f", "1.0f"));
            org.junit.Assert.assertTrue(org.apache.ambari.server.state.ConfigHelper.valuesAreEqual("0", "000"));
            org.junit.Assert.assertTrue(org.apache.ambari.server.state.ConfigHelper.valuesAreEqual("123", "123L"));
            org.junit.Assert.assertTrue(org.apache.ambari.server.state.ConfigHelper.valuesAreEqual("0", "0.0"));
        }

        @org.junit.Test
        public void differentNumbersAreNotEqual() {
            org.junit.Assert.assertFalse(org.apache.ambari.server.state.ConfigHelper.valuesAreEqual("1.234", "1.2341"));
            org.junit.Assert.assertFalse(org.apache.ambari.server.state.ConfigHelper.valuesAreEqual("123L", "124L"));
            org.junit.Assert.assertFalse(org.apache.ambari.server.state.ConfigHelper.valuesAreEqual("-1.234", "1.234"));
            org.junit.Assert.assertFalse(org.apache.ambari.server.state.ConfigHelper.valuesAreEqual("-123L", "123L"));
            org.junit.Assert.assertFalse(org.apache.ambari.server.state.ConfigHelper.valuesAreEqual("-1.234", "-1.2341"));
            org.junit.Assert.assertFalse(org.apache.ambari.server.state.ConfigHelper.valuesAreEqual("-123L", "-124L"));
        }
    }
}