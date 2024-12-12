package org.apache.ambari.server.controller.internal;
import org.easymock.EasyMock;
import org.springframework.security.core.context.SecurityContextHolder;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
public class CompatibleRepositoryVersionResourceProviderTest {
    private static com.google.inject.Injector injector;

    private static final java.util.List<org.apache.ambari.server.orm.entities.RepoOsEntity> osRedhat6 = new java.util.ArrayList<>();

    {
        org.apache.ambari.server.orm.entities.RepoOsEntity repoOsEntity = new org.apache.ambari.server.orm.entities.RepoOsEntity();
        repoOsEntity.setFamily("redhat6");
        repoOsEntity.setAmbariManaged(true);
        osRedhat6.add(repoOsEntity);
    }

    private static org.apache.ambari.server.state.StackId stackId11 = new org.apache.ambari.server.state.StackId("HDP", "1.1");

    private static org.apache.ambari.server.state.StackId stackId22 = new org.apache.ambari.server.state.StackId("HDP", "2.2");

    @org.junit.Before
    public void before() throws java.lang.Exception {
        final org.apache.ambari.server.api.services.AmbariMetaInfo ambariMetaInfo = org.easymock.EasyMock.createMock(org.apache.ambari.server.api.services.AmbariMetaInfo.class);
        org.apache.ambari.server.orm.entities.StackEntity hdp11Stack = new org.apache.ambari.server.orm.entities.StackEntity();
        hdp11Stack.setStackName("HDP");
        hdp11Stack.setStackVersion("1.1");
        org.apache.ambari.server.orm.entities.RepositoryVersionEntity entity1 = new org.apache.ambari.server.orm.entities.RepositoryVersionEntity();
        entity1.setDisplayName("name1");
        entity1.addRepoOsEntities(org.apache.ambari.server.controller.internal.CompatibleRepositoryVersionResourceProviderTest.osRedhat6);
        entity1.setStack(hdp11Stack);
        entity1.setVersion("1.1.1.1");
        entity1.setId(1L);
        org.apache.ambari.server.orm.entities.StackEntity hdp22Stack = new org.apache.ambari.server.orm.entities.StackEntity();
        hdp22Stack.setStackName("HDP");
        hdp22Stack.setStackVersion("2.2");
        org.apache.ambari.server.orm.entities.RepositoryVersionEntity entity2 = new org.apache.ambari.server.controller.internal.CompatibleRepositoryVersionResourceProviderTest.ExtendedRepositoryVersionEntity();
        entity2.setDisplayName("name2");
        entity2.addRepoOsEntities(org.apache.ambari.server.controller.internal.CompatibleRepositoryVersionResourceProviderTest.osRedhat6);
        entity2.setStack(hdp22Stack);
        entity2.setVersion("2.2.2.2");
        entity2.setId(2L);
        final org.apache.ambari.server.orm.dao.RepositoryVersionDAO repoVersionDAO = org.easymock.EasyMock.createMock(org.apache.ambari.server.orm.dao.RepositoryVersionDAO.class);
        EasyMock.expect(repoVersionDAO.findByStack(org.apache.ambari.server.controller.internal.CompatibleRepositoryVersionResourceProviderTest.stackId11)).andReturn(java.util.Collections.singletonList(entity1)).atLeastOnce();
        EasyMock.expect(repoVersionDAO.findByStack(org.apache.ambari.server.controller.internal.CompatibleRepositoryVersionResourceProviderTest.stackId22)).andReturn(java.util.Collections.singletonList(entity2)).atLeastOnce();
        EasyMock.replay(repoVersionDAO);
        final org.apache.ambari.server.state.StackInfo stack1 = new org.apache.ambari.server.state.StackInfo() {
            @java.lang.Override
            public java.util.Map<java.lang.String, org.apache.ambari.server.stack.upgrade.UpgradePack> getUpgradePacks() {
                java.util.Map<java.lang.String, org.apache.ambari.server.stack.upgrade.UpgradePack> map = new java.util.HashMap<>();
                org.apache.ambari.server.stack.upgrade.UpgradePack pack1 = new org.apache.ambari.server.stack.upgrade.UpgradePack() {
                    @java.lang.Override
                    public java.lang.String getName() {
                        return "pack1";
                    }

                    @java.lang.Override
                    public java.lang.String getTarget() {
                        return "1.1.*.*";
                    }

                    @java.lang.Override
                    public org.apache.ambari.spi.upgrade.UpgradeType getType() {
                        return org.apache.ambari.spi.upgrade.UpgradeType.ROLLING;
                    }
                };
                org.apache.ambari.server.stack.upgrade.UpgradePack pack2 = new org.apache.ambari.server.stack.upgrade.UpgradePack() {
                    @java.lang.Override
                    public java.lang.String getName() {
                        return "pack2";
                    }

                    @java.lang.Override
                    public java.lang.String getTarget() {
                        return "2.2.*.*";
                    }

                    @java.lang.Override
                    public java.lang.String getTargetStack() {
                        return "HDP-2.2";
                    }

                    @java.lang.Override
                    public org.apache.ambari.spi.upgrade.UpgradeType getType() {
                        return org.apache.ambari.spi.upgrade.UpgradeType.NON_ROLLING;
                    }
                };
                org.apache.ambari.server.stack.upgrade.UpgradePack pack3 = new org.apache.ambari.server.stack.upgrade.UpgradePack() {
                    @java.lang.Override
                    public java.lang.String getName() {
                        return "pack2";
                    }

                    @java.lang.Override
                    public java.lang.String getTarget() {
                        return "2.2.*.*";
                    }

                    @java.lang.Override
                    public java.lang.String getTargetStack() {
                        return "HDP-2.2";
                    }

                    @java.lang.Override
                    public org.apache.ambari.spi.upgrade.UpgradeType getType() {
                        return org.apache.ambari.spi.upgrade.UpgradeType.ROLLING;
                    }
                };
                map.put("pack1", pack1);
                map.put("pack2", pack2);
                map.put("pack3", pack3);
                return map;
            }
        };
        final org.apache.ambari.server.state.StackInfo stack2 = new org.apache.ambari.server.state.StackInfo() {
            @java.lang.Override
            public java.util.Map<java.lang.String, org.apache.ambari.server.stack.upgrade.UpgradePack> getUpgradePacks() {
                java.util.Map<java.lang.String, org.apache.ambari.server.stack.upgrade.UpgradePack> map = new java.util.HashMap<>();
                org.apache.ambari.server.stack.upgrade.UpgradePack pack = new org.apache.ambari.server.stack.upgrade.UpgradePack() {
                    @java.lang.Override
                    public java.lang.String getName() {
                        return "pack2";
                    }

                    @java.lang.Override
                    public java.lang.String getTarget() {
                        return "2.2.*.*";
                    }

                    @java.lang.Override
                    public org.apache.ambari.spi.upgrade.UpgradeType getType() {
                        return org.apache.ambari.spi.upgrade.UpgradeType.NON_ROLLING;
                    }
                };
                map.put("pack2", pack);
                return map;
            }
        };
        org.apache.ambari.server.orm.InMemoryDefaultTestModule injectorModule = new org.apache.ambari.server.orm.InMemoryDefaultTestModule() {
            @java.lang.Override
            protected void configure() {
                super.configure();
                bind(org.apache.ambari.server.api.services.AmbariMetaInfo.class).toInstance(ambariMetaInfo);
                bind(org.apache.ambari.server.orm.dao.RepositoryVersionDAO.class).toInstance(repoVersionDAO);
                requestStaticInjection(org.apache.ambari.server.controller.internal.CompatibleRepositoryVersionResourceProvider.class);
            }
        };
        org.apache.ambari.server.controller.internal.CompatibleRepositoryVersionResourceProviderTest.injector = com.google.inject.Guice.createInjector(injectorModule);
        EasyMock.expect(ambariMetaInfo.getStack("HDP", "1.1")).andReturn(stack1).atLeastOnce();
        EasyMock.expect(ambariMetaInfo.getStack("HDP", "2.2")).andReturn(stack2).atLeastOnce();
        EasyMock.expect(ambariMetaInfo.getUpgradePacks("HDP", "1.1")).andReturn(stack1.getUpgradePacks()).atLeastOnce();
        EasyMock.expect(ambariMetaInfo.getUpgradePacks("HDP", "2.2")).andReturn(stack2.getUpgradePacks()).atLeastOnce();
        EasyMock.replay(ambariMetaInfo);
        org.apache.ambari.server.controller.internal.CompatibleRepositoryVersionResourceProviderTest.injector.getInstance(org.apache.ambari.server.orm.GuiceJpaInitializer.class);
    }

    @org.junit.After
    public void after() throws org.apache.ambari.server.AmbariException, java.sql.SQLException {
        org.apache.ambari.server.H2DatabaseCleaner.clearDatabaseAndStopPersistenceService(org.apache.ambari.server.controller.internal.CompatibleRepositoryVersionResourceProviderTest.injector);
        org.apache.ambari.server.controller.internal.CompatibleRepositoryVersionResourceProviderTest.injector = null;
        org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(null);
    }

    @org.junit.Test
    public void testVersionInStack() {
        junit.framework.Assert.assertTrue(org.apache.ambari.server.orm.entities.RepositoryVersionEntity.isVersionInStack(new org.apache.ambari.server.state.StackId("HDP", "2.3"), "2.3.0.0"));
        junit.framework.Assert.assertTrue(org.apache.ambari.server.orm.entities.RepositoryVersionEntity.isVersionInStack(new org.apache.ambari.server.state.StackId("HDPWIN", "2.3"), "2.3.0.0"));
        junit.framework.Assert.assertTrue(org.apache.ambari.server.orm.entities.RepositoryVersionEntity.isVersionInStack(new org.apache.ambari.server.state.StackId("HDP", "2.3.GlusterFS"), "2.3.0.0"));
        junit.framework.Assert.assertTrue(org.apache.ambari.server.orm.entities.RepositoryVersionEntity.isVersionInStack(new org.apache.ambari.server.state.StackId("HDF", "2.1"), "2.1.0.0"));
        junit.framework.Assert.assertTrue(org.apache.ambari.server.orm.entities.RepositoryVersionEntity.isVersionInStack(new org.apache.ambari.server.state.StackId("HDP", "2.3"), "HDP-2.3.0.0"));
        junit.framework.Assert.assertTrue(org.apache.ambari.server.orm.entities.RepositoryVersionEntity.isVersionInStack(new org.apache.ambari.server.state.StackId("HDPWIN", "2.3"), "HDPWIN-2.3.0.0"));
        junit.framework.Assert.assertTrue(org.apache.ambari.server.orm.entities.RepositoryVersionEntity.isVersionInStack(new org.apache.ambari.server.state.StackId("HDP", "2.3.GlusterFS"), "HDP-2.3.0.0"));
        junit.framework.Assert.assertTrue(org.apache.ambari.server.orm.entities.RepositoryVersionEntity.isVersionInStack(new org.apache.ambari.server.state.StackId("HDF", "2.1"), "HDF-2.1.0.0"));
    }

    @org.junit.Test
    public void testGetResources() throws java.lang.Exception {
        org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(org.apache.ambari.server.security.TestAuthenticationFactory.createClusterAdministrator("admin", 2L));
        final org.apache.ambari.server.controller.spi.ResourceProvider provider = org.apache.ambari.server.controller.internal.CompatibleRepositoryVersionResourceProviderTest.injector.getInstance(org.apache.ambari.server.controller.ResourceProviderFactory.class).getRepositoryVersionResourceProvider();
        org.apache.ambari.server.controller.spi.Request getRequest = org.apache.ambari.server.controller.utilities.PropertyHelper.getReadRequest(org.apache.ambari.server.controller.internal.RepositoryVersionResourceProvider.REPOSITORY_VERSION_ID_PROPERTY_ID, org.apache.ambari.server.controller.internal.RepositoryVersionResourceProvider.REPOSITORY_VERSION_STACK_NAME_PROPERTY_ID, org.apache.ambari.server.controller.internal.RepositoryVersionResourceProvider.REPOSITORY_VERSION_STACK_VERSION_PROPERTY_ID, org.apache.ambari.server.controller.internal.CompatibleRepositoryVersionResourceProvider.REPOSITORY_UPGRADES_SUPPORTED_TYPES_ID);
        org.apache.ambari.server.controller.spi.Predicate predicateStackName = new org.apache.ambari.server.controller.utilities.PredicateBuilder().property(org.apache.ambari.server.controller.internal.RepositoryVersionResourceProvider.REPOSITORY_VERSION_STACK_NAME_PROPERTY_ID).equals("HDP").toPredicate();
        org.apache.ambari.server.controller.spi.Predicate predicateStackVersion = new org.apache.ambari.server.controller.utilities.PredicateBuilder().property(org.apache.ambari.server.controller.internal.RepositoryVersionResourceProvider.REPOSITORY_VERSION_STACK_VERSION_PROPERTY_ID).equals("1.1").toPredicate();
        junit.framework.Assert.assertEquals(1, provider.getResources(getRequest, new org.apache.ambari.server.controller.predicate.AndPredicate(predicateStackName, predicateStackVersion)).size());
        org.apache.ambari.server.controller.internal.CompatibleRepositoryVersionResourceProvider compatibleProvider = new org.apache.ambari.server.controller.internal.CompatibleRepositoryVersionResourceProvider(null);
        getRequest = org.apache.ambari.server.controller.utilities.PropertyHelper.getReadRequest(org.apache.ambari.server.controller.internal.CompatibleRepositoryVersionResourceProvider.REPOSITORY_VERSION_ID_PROPERTY_ID, org.apache.ambari.server.controller.internal.CompatibleRepositoryVersionResourceProvider.REPOSITORY_VERSION_STACK_NAME_PROPERTY_ID, org.apache.ambari.server.controller.internal.CompatibleRepositoryVersionResourceProvider.REPOSITORY_VERSION_STACK_VERSION_PROPERTY_ID, org.apache.ambari.server.controller.internal.CompatibleRepositoryVersionResourceProvider.REPOSITORY_UPGRADES_SUPPORTED_TYPES_ID);
        predicateStackName = new org.apache.ambari.server.controller.utilities.PredicateBuilder().property(org.apache.ambari.server.controller.internal.CompatibleRepositoryVersionResourceProvider.REPOSITORY_VERSION_STACK_NAME_PROPERTY_ID).equals("HDP").toPredicate();
        predicateStackVersion = new org.apache.ambari.server.controller.utilities.PredicateBuilder().property(org.apache.ambari.server.controller.internal.CompatibleRepositoryVersionResourceProvider.REPOSITORY_VERSION_STACK_VERSION_PROPERTY_ID).equals("1.1").toPredicate();
        java.util.Set<org.apache.ambari.server.controller.spi.Resource> resources = compatibleProvider.getResources(getRequest, new org.apache.ambari.server.controller.predicate.AndPredicate(predicateStackName, predicateStackVersion));
        junit.framework.Assert.assertEquals(2, resources.size());
        java.util.Map<java.lang.String, java.util.List<org.apache.ambari.spi.upgrade.UpgradeType>> versionToUpgradeTypesMap = new java.util.HashMap<>();
        versionToUpgradeTypesMap.put("1.1", java.util.Arrays.asList(org.apache.ambari.spi.upgrade.UpgradeType.ROLLING));
        versionToUpgradeTypesMap.put("2.2", java.util.Arrays.asList(org.apache.ambari.spi.upgrade.UpgradeType.NON_ROLLING, org.apache.ambari.spi.upgrade.UpgradeType.ROLLING));
        junit.framework.Assert.assertEquals(versionToUpgradeTypesMap.size(), checkUpgradeTypes(resources, versionToUpgradeTypesMap));
        org.apache.ambari.server.orm.dao.RepositoryVersionDAO dao = org.apache.ambari.server.controller.internal.CompatibleRepositoryVersionResourceProviderTest.injector.getInstance(org.apache.ambari.server.orm.dao.RepositoryVersionDAO.class);
        java.util.List<org.apache.ambari.server.orm.entities.RepositoryVersionEntity> entities = dao.findByStack(org.apache.ambari.server.controller.internal.CompatibleRepositoryVersionResourceProviderTest.stackId22);
        junit.framework.Assert.assertEquals(1, entities.size());
        org.apache.ambari.server.orm.entities.RepositoryVersionEntity entity = entities.get(0);
        junit.framework.Assert.assertTrue(org.apache.ambari.server.controller.internal.CompatibleRepositoryVersionResourceProviderTest.ExtendedRepositoryVersionEntity.class.isInstance(entity));
        org.apache.ambari.server.state.repository.VersionDefinitionXml mockXml = org.easymock.EasyMock.createMock(org.apache.ambari.server.state.repository.VersionDefinitionXml.class);
        org.apache.ambari.server.state.repository.AvailableService mockAvailable = org.easymock.EasyMock.createMock(org.apache.ambari.server.state.repository.AvailableService.class);
        org.apache.ambari.server.state.repository.ManifestServiceInfo mockStackService = org.easymock.EasyMock.createMock(org.apache.ambari.server.state.repository.ManifestServiceInfo.class);
        EasyMock.expect(mockXml.getAvailableServices(((org.apache.ambari.server.state.StackInfo) (org.easymock.EasyMock.anyObject())))).andReturn(java.util.Collections.singletonList(mockAvailable)).atLeastOnce();
        EasyMock.expect(mockXml.getStackServices(((org.apache.ambari.server.state.StackInfo) (org.easymock.EasyMock.anyObject())))).andReturn(java.util.Collections.singletonList(mockStackService)).atLeastOnce();
        EasyMock.replay(mockXml);
        ((org.apache.ambari.server.controller.internal.CompatibleRepositoryVersionResourceProviderTest.ExtendedRepositoryVersionEntity) (entity)).m_xml = mockXml;
        getRequest = org.apache.ambari.server.controller.utilities.PropertyHelper.getReadRequest(org.apache.ambari.server.controller.internal.CompatibleRepositoryVersionResourceProvider.REPOSITORY_VERSION_ID_PROPERTY_ID, org.apache.ambari.server.controller.internal.CompatibleRepositoryVersionResourceProvider.REPOSITORY_VERSION_STACK_NAME_PROPERTY_ID, org.apache.ambari.server.controller.internal.CompatibleRepositoryVersionResourceProvider.REPOSITORY_VERSION_STACK_VERSION_PROPERTY_ID, org.apache.ambari.server.controller.internal.CompatibleRepositoryVersionResourceProvider.REPOSITORY_UPGRADES_SUPPORTED_TYPES_ID, org.apache.ambari.server.controller.internal.CompatibleRepositoryVersionResourceProvider.REPOSITORY_VERSION_SERVICES);
        predicateStackName = new org.apache.ambari.server.controller.utilities.PredicateBuilder().property(org.apache.ambari.server.controller.internal.CompatibleRepositoryVersionResourceProvider.REPOSITORY_VERSION_STACK_NAME_PROPERTY_ID).equals("HDP").toPredicate();
        predicateStackVersion = new org.apache.ambari.server.controller.utilities.PredicateBuilder().property(org.apache.ambari.server.controller.internal.CompatibleRepositoryVersionResourceProvider.REPOSITORY_VERSION_STACK_VERSION_PROPERTY_ID).equals("1.1").toPredicate();
        resources = compatibleProvider.getResources(getRequest, new org.apache.ambari.server.controller.predicate.AndPredicate(predicateStackName, predicateStackVersion));
        junit.framework.Assert.assertEquals(2, resources.size());
        for (org.apache.ambari.server.controller.spi.Resource r : resources) {
            java.lang.Object stackId = r.getPropertyValue(org.apache.ambari.server.controller.internal.CompatibleRepositoryVersionResourceProvider.REPOSITORY_VERSION_STACK_VERSION_PROPERTY_ID);
            junit.framework.Assert.assertNotNull(stackId);
            if (stackId.toString().equals("2.2")) {
                junit.framework.Assert.assertNotNull(r.getPropertyValue(org.apache.ambari.server.controller.internal.CompatibleRepositoryVersionResourceProvider.REPOSITORY_VERSION_SERVICES));
            } else {
                junit.framework.Assert.assertNull(r.getPropertyValue(org.apache.ambari.server.controller.internal.CompatibleRepositoryVersionResourceProvider.REPOSITORY_VERSION_SERVICES));
            }
        }
    }

    @org.junit.Test
    public void testGetResourcesWithAmendedPredicate() throws java.lang.Exception {
        org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(org.apache.ambari.server.security.TestAuthenticationFactory.createClusterAdministrator("admin", 2L));
        final org.apache.ambari.server.controller.spi.ResourceProvider provider = org.apache.ambari.server.controller.internal.CompatibleRepositoryVersionResourceProviderTest.injector.getInstance(org.apache.ambari.server.controller.ResourceProviderFactory.class).getRepositoryVersionResourceProvider();
        org.apache.ambari.server.controller.spi.Request getRequest = org.apache.ambari.server.controller.utilities.PropertyHelper.getReadRequest(org.apache.ambari.server.controller.internal.RepositoryVersionResourceProvider.REPOSITORY_VERSION_ID_PROPERTY_ID, org.apache.ambari.server.controller.internal.RepositoryVersionResourceProvider.REPOSITORY_VERSION_STACK_NAME_PROPERTY_ID, org.apache.ambari.server.controller.internal.RepositoryVersionResourceProvider.REPOSITORY_VERSION_STACK_VERSION_PROPERTY_ID, org.apache.ambari.server.controller.internal.CompatibleRepositoryVersionResourceProvider.REPOSITORY_UPGRADES_SUPPORTED_TYPES_ID);
        org.apache.ambari.server.controller.spi.Predicate predicateStackName = new org.apache.ambari.server.controller.utilities.PredicateBuilder().property(org.apache.ambari.server.controller.internal.RepositoryVersionResourceProvider.REPOSITORY_VERSION_STACK_NAME_PROPERTY_ID).equals("HDP").toPredicate();
        org.apache.ambari.server.controller.spi.Predicate predicateStackVersion = new org.apache.ambari.server.controller.utilities.PredicateBuilder().property(org.apache.ambari.server.controller.internal.RepositoryVersionResourceProvider.REPOSITORY_VERSION_STACK_VERSION_PROPERTY_ID).equals("1.1").toPredicate();
        junit.framework.Assert.assertEquals(1, provider.getResources(getRequest, new org.apache.ambari.server.controller.predicate.AndPredicate(predicateStackName, predicateStackVersion)).size());
        org.apache.ambari.server.controller.internal.CompatibleRepositoryVersionResourceProvider compatibleProvider = new org.apache.ambari.server.controller.internal.CompatibleRepositoryVersionResourceProvider(null);
        getRequest = org.apache.ambari.server.controller.utilities.PropertyHelper.getReadRequest(org.apache.ambari.server.controller.internal.CompatibleRepositoryVersionResourceProvider.REPOSITORY_VERSION_ID_PROPERTY_ID, org.apache.ambari.server.controller.internal.CompatibleRepositoryVersionResourceProvider.REPOSITORY_VERSION_STACK_NAME_PROPERTY_ID, org.apache.ambari.server.controller.internal.CompatibleRepositoryVersionResourceProvider.REPOSITORY_VERSION_STACK_VERSION_PROPERTY_ID, org.apache.ambari.server.controller.internal.CompatibleRepositoryVersionResourceProvider.REPOSITORY_UPGRADES_SUPPORTED_TYPES_ID);
        predicateStackName = new org.apache.ambari.server.controller.utilities.PredicateBuilder().property(org.apache.ambari.server.controller.internal.CompatibleRepositoryVersionResourceProvider.REPOSITORY_VERSION_STACK_NAME_PROPERTY_ID).equals("HDP").toPredicate();
        predicateStackVersion = new org.apache.ambari.server.controller.utilities.PredicateBuilder().property(org.apache.ambari.server.controller.internal.CompatibleRepositoryVersionResourceProvider.REPOSITORY_VERSION_STACK_VERSION_PROPERTY_ID).equals("1.1").toPredicate();
        org.apache.ambari.server.controller.spi.Predicate newPredicate = compatibleProvider.amendPredicate(new org.apache.ambari.server.controller.predicate.AndPredicate(predicateStackName, predicateStackVersion));
        junit.framework.Assert.assertEquals(org.apache.ambari.server.controller.predicate.OrPredicate.class, newPredicate.getClass());
        java.util.Set<org.apache.ambari.server.controller.spi.Resource> resources = compatibleProvider.getResources(getRequest, newPredicate);
        junit.framework.Assert.assertEquals(2, resources.size());
        java.util.Map<java.lang.String, java.util.List<org.apache.ambari.spi.upgrade.UpgradeType>> versionToUpgradeTypesMap = new java.util.HashMap<>();
        versionToUpgradeTypesMap.put("1.1", java.util.Arrays.asList(org.apache.ambari.spi.upgrade.UpgradeType.ROLLING));
        versionToUpgradeTypesMap.put("2.2", java.util.Arrays.asList(org.apache.ambari.spi.upgrade.UpgradeType.NON_ROLLING, org.apache.ambari.spi.upgrade.UpgradeType.ROLLING));
        junit.framework.Assert.assertEquals(versionToUpgradeTypesMap.size(), checkUpgradeTypes(resources, versionToUpgradeTypesMap));
    }

    public int checkUpgradeTypes(java.util.Set<org.apache.ambari.server.controller.spi.Resource> resources, java.util.Map<java.lang.String, java.util.List<org.apache.ambari.spi.upgrade.UpgradeType>> versionToUpgradeTypesMap) {
        int count = 0;
        java.util.Iterator<org.apache.ambari.server.controller.spi.Resource> itr = resources.iterator();
        while (itr.hasNext()) {
            org.apache.ambari.server.controller.spi.Resource res = itr.next();
            java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.Object>> resPropMap = res.getPropertiesMap();
            for (java.lang.String resource : resPropMap.keySet()) {
                java.util.Map<java.lang.String, java.lang.Object> propMap = resPropMap.get(resource);
                java.lang.String stackVersion = propMap.get("stack_version").toString();
                if (versionToUpgradeTypesMap.containsKey(stackVersion)) {
                    java.util.List<org.apache.ambari.spi.upgrade.UpgradeType> upgradeTypes = new java.util.ArrayList<>(((java.util.Set<org.apache.ambari.spi.upgrade.UpgradeType>) (propMap.get("upgrade_types"))));
                    java.util.List<org.apache.ambari.spi.upgrade.UpgradeType> expectedTypes = versionToUpgradeTypesMap.get(stackVersion);
                    java.util.Collections.sort(upgradeTypes);
                    java.util.Collections.sort(expectedTypes);
                    junit.framework.Assert.assertEquals(expectedTypes, upgradeTypes);
                    count++;
                }
            }
        } 
        return count;
    }

    private static class ExtendedRepositoryVersionEntity extends org.apache.ambari.server.orm.entities.RepositoryVersionEntity {
        private org.apache.ambari.server.state.repository.VersionDefinitionXml m_xml = null;

        @java.lang.Override
        public org.apache.ambari.server.state.repository.VersionDefinitionXml getRepositoryXml() throws java.lang.Exception {
            return m_xml;
        }
    }
}