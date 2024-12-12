package org.apache.ambari.server.controller.internal;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
public class RepositoryVersionResourceProviderTest {
    private static com.google.inject.Injector injector;

    public static final java.util.List<org.apache.ambari.server.orm.entities.RepoOsEntity> osRedhat6 = new java.util.ArrayList<>();

    static {
        org.apache.ambari.server.orm.entities.RepoOsEntity repoOsEntity = new org.apache.ambari.server.orm.entities.RepoOsEntity();
        repoOsEntity.setFamily("redhat6");
        repoOsEntity.setAmbariManaged(true);
        osRedhat6.add(repoOsEntity);
    }

    public static final java.util.List<org.apache.ambari.server.orm.entities.RepoOsEntity> osRedhat7 = new java.util.ArrayList<>();

    @org.junit.Before
    public void before() throws java.lang.Exception {
        final java.util.Set<java.lang.String> validVersions = com.google.common.collect.Sets.newHashSet("1.1", "1.1-17", "1.1.1.1", "1.1.343432.2", "1.1.343432.2-234234324");
        final java.util.Set<org.apache.ambari.server.state.StackInfo> stacks = new java.util.HashSet<>();
        final org.apache.ambari.server.api.services.AmbariMetaInfo ambariMetaInfo = org.mockito.Mockito.mock(org.apache.ambari.server.api.services.AmbariMetaInfo.class);
        final org.apache.ambari.server.orm.InMemoryDefaultTestModule injectorModule = new org.apache.ambari.server.orm.InMemoryDefaultTestModule() {
            @java.lang.Override
            protected void configure() {
                super.configure();
                bind(org.apache.ambari.server.api.services.AmbariMetaInfo.class).toInstance(ambariMetaInfo);
            }
        };
        org.apache.ambari.server.controller.internal.RepositoryVersionResourceProviderTest.injector = com.google.inject.Guice.createInjector(injectorModule);
        final org.apache.ambari.server.state.StackInfo stackInfo = new org.apache.ambari.server.state.StackInfo() {
            @java.lang.Override
            public java.util.Map<java.lang.String, org.apache.ambari.server.stack.upgrade.UpgradePack> getUpgradePacks() {
                final java.util.Map<java.lang.String, org.apache.ambari.server.stack.upgrade.UpgradePack> map = new java.util.HashMap<>();
                final org.apache.ambari.server.stack.upgrade.UpgradePack pack1 = new org.apache.ambari.server.stack.upgrade.UpgradePack() {
                    @java.lang.Override
                    public java.lang.String getName() {
                        return "pack1";
                    }

                    @java.lang.Override
                    public java.lang.String getTarget() {
                        return "1.1.*.*";
                    }
                };
                final org.apache.ambari.server.stack.upgrade.UpgradePack pack2 = new org.apache.ambari.server.stack.upgrade.UpgradePack() {
                    @java.lang.Override
                    public java.lang.String getName() {
                        return "pack2";
                    }

                    @java.lang.Override
                    public java.lang.String getTarget() {
                        return "1.1.*.*";
                    }
                };
                map.put("pack1", pack1);
                map.put("pack2", pack2);
                return map;
            }

            @java.lang.Override
            public org.apache.ambari.server.state.ServiceInfo getService(java.lang.String name) {
                return new org.apache.ambari.server.state.ServiceInfo();
            }
        };
        stackInfo.setName("HDP");
        stackInfo.setVersion("1.1");
        stacks.add(stackInfo);
        org.mockito.Mockito.when(ambariMetaInfo.getStack(org.mockito.Mockito.anyString(), org.mockito.Mockito.anyString())).thenAnswer(new org.mockito.stubbing.Answer<org.apache.ambari.server.state.StackInfo>() {
            @java.lang.Override
            public org.apache.ambari.server.state.StackInfo answer(org.mockito.invocation.InvocationOnMock invocation) throws java.lang.Throwable {
                final java.lang.String stack = invocation.getArguments()[0].toString();
                final java.lang.String version = invocation.getArguments()[1].toString();
                if (stack.equals("HDP") && validVersions.contains(version)) {
                    return stackInfo;
                } else {
                    throw new java.lang.Exception("error");
                }
            }
        });
        org.mockito.Mockito.when(ambariMetaInfo.getStacks()).thenReturn(stacks);
        org.mockito.Mockito.when(ambariMetaInfo.getUpgradePacks(org.mockito.Mockito.anyString(), org.mockito.Mockito.anyString())).thenAnswer(new org.mockito.stubbing.Answer<java.util.Map<java.lang.String, org.apache.ambari.server.stack.upgrade.UpgradePack>>() {
            @java.lang.Override
            public java.util.Map<java.lang.String, org.apache.ambari.server.stack.upgrade.UpgradePack> answer(org.mockito.invocation.InvocationOnMock invocation) throws java.lang.Throwable {
                return stackInfo.getUpgradePacks();
            }
        });
        final java.util.HashSet<org.apache.ambari.server.state.OperatingSystemInfo> osInfos = new java.util.HashSet<>();
        osInfos.add(new org.apache.ambari.server.state.OperatingSystemInfo("redhat6"));
        osInfos.add(new org.apache.ambari.server.state.OperatingSystemInfo("redhat7"));
        org.mockito.Mockito.when(ambariMetaInfo.getOperatingSystems(org.mockito.Mockito.anyString(), org.mockito.Mockito.anyString())).thenAnswer(new org.mockito.stubbing.Answer<java.util.Set<org.apache.ambari.server.state.OperatingSystemInfo>>() {
            @java.lang.Override
            public java.util.Set<org.apache.ambari.server.state.OperatingSystemInfo> answer(org.mockito.invocation.InvocationOnMock invocation) throws java.lang.Throwable {
                final java.lang.String stack = invocation.getArguments()[0].toString();
                final java.lang.String version = invocation.getArguments()[1].toString();
                if (stack.equals("HDP") && validVersions.contains(version)) {
                    return osInfos;
                } else {
                    return new java.util.HashSet<>();
                }
            }
        });
        org.apache.ambari.server.H2DatabaseCleaner.resetSequences(org.apache.ambari.server.controller.internal.RepositoryVersionResourceProviderTest.injector);
        org.apache.ambari.server.controller.internal.RepositoryVersionResourceProviderTest.injector.getInstance(org.apache.ambari.server.orm.GuiceJpaInitializer.class);
        org.apache.ambari.server.orm.dao.StackDAO stackDAO = org.apache.ambari.server.controller.internal.RepositoryVersionResourceProviderTest.injector.getInstance(org.apache.ambari.server.orm.dao.StackDAO.class);
        org.apache.ambari.server.orm.entities.StackEntity stackEntity = new org.apache.ambari.server.orm.entities.StackEntity();
        stackEntity.setStackName("HDP");
        stackEntity.setStackVersion("1.1");
        stackDAO.create(stackEntity);
        org.apache.ambari.server.state.Clusters clusters = org.apache.ambari.server.controller.internal.RepositoryVersionResourceProviderTest.injector.getInstance(org.apache.ambari.server.state.Clusters.class);
        clusters.addCluster("c1", new org.apache.ambari.server.state.StackId("HDP", "1.1"));
    }

    @org.junit.Test
    public void testCreateResourcesAsAdministrator() throws java.lang.Exception {
        testCreateResources(org.apache.ambari.server.security.TestAuthenticationFactory.createAdministrator());
    }

    @org.junit.Test(expected = org.apache.ambari.server.security.authorization.AuthorizationException.class)
    public void testCreateResourcesAsClusterAdministrator() throws java.lang.Exception {
        testCreateResources(org.apache.ambari.server.security.TestAuthenticationFactory.createClusterAdministrator());
    }

    private void testCreateResources(org.springframework.security.core.Authentication authentication) throws java.lang.Exception {
        org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(authentication);
        final org.apache.ambari.server.controller.spi.ResourceProvider provider = org.apache.ambari.server.controller.internal.RepositoryVersionResourceProviderTest.injector.getInstance(org.apache.ambari.server.controller.ResourceProviderFactory.class).getRepositoryVersionResourceProvider();
        final java.util.Set<java.util.Map<java.lang.String, java.lang.Object>> propertySet = new java.util.LinkedHashSet<>();
        final java.util.Map<java.lang.String, java.lang.Object> properties = new java.util.LinkedHashMap<>();
        properties.put(org.apache.ambari.server.controller.internal.RepositoryVersionResourceProvider.REPOSITORY_VERSION_DISPLAY_NAME_PROPERTY_ID, "name");
        properties.put(org.apache.ambari.server.controller.internal.RepositoryVersionResourceProvider.SUBRESOURCE_OPERATING_SYSTEMS_PROPERTY_ID, new com.google.gson.Gson().fromJson("[{\"OperatingSystems/os_type\":\"redhat6\",\"repositories\":[{\"Repositories/repo_id\":\"1\",\"Repositories/repo_name\":\"1\",\"Repositories/base_url\":\"1\",\"Repositories/unique\":\"true\"}]}]", java.lang.Object.class));
        properties.put(org.apache.ambari.server.controller.internal.RepositoryVersionResourceProvider.REPOSITORY_VERSION_STACK_NAME_PROPERTY_ID, "HDP");
        properties.put(org.apache.ambari.server.controller.internal.RepositoryVersionResourceProvider.REPOSITORY_VERSION_STACK_VERSION_PROPERTY_ID, "1.1");
        properties.put(org.apache.ambari.server.controller.internal.RepositoryVersionResourceProvider.REPOSITORY_VERSION_REPOSITORY_VERSION_PROPERTY_ID, "1.1.1.1");
        propertySet.add(properties);
        final org.apache.ambari.server.controller.spi.Predicate predicateStackName = new org.apache.ambari.server.controller.utilities.PredicateBuilder().property(org.apache.ambari.server.controller.internal.RepositoryVersionResourceProvider.REPOSITORY_VERSION_STACK_NAME_PROPERTY_ID).equals("HDP").toPredicate();
        final org.apache.ambari.server.controller.spi.Predicate predicateStackVersion = new org.apache.ambari.server.controller.utilities.PredicateBuilder().property(org.apache.ambari.server.controller.internal.RepositoryVersionResourceProvider.REPOSITORY_VERSION_STACK_VERSION_PROPERTY_ID).equals("1.1").toPredicate();
        final org.apache.ambari.server.controller.spi.Request getRequest = org.apache.ambari.server.controller.utilities.PropertyHelper.getReadRequest(org.apache.ambari.server.controller.internal.RepositoryVersionResourceProvider.REPOSITORY_VERSION_DISPLAY_NAME_PROPERTY_ID);
        org.junit.Assert.assertEquals(0, provider.getResources(getRequest, new org.apache.ambari.server.controller.predicate.AndPredicate(predicateStackName, predicateStackVersion)).size());
        final org.apache.ambari.server.controller.spi.Request createRequest = org.apache.ambari.server.controller.utilities.PropertyHelper.getCreateRequest(propertySet, null);
        provider.createResources(createRequest);
        org.junit.Assert.assertEquals(1, provider.getResources(getRequest, new org.apache.ambari.server.controller.predicate.AndPredicate(predicateStackName, predicateStackVersion)).size());
    }

    @org.junit.Test
    public void testGetResourcesAsAdministrator() throws java.lang.Exception {
        testGetResources(org.apache.ambari.server.security.TestAuthenticationFactory.createAdministrator());
    }

    @org.junit.Test
    public void testGetResourcesAsClusterAdministrator() throws java.lang.Exception {
        testGetResources(org.apache.ambari.server.security.TestAuthenticationFactory.createClusterAdministrator());
    }

    private void testGetResources(org.springframework.security.core.Authentication authentication) throws java.lang.Exception {
        org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(authentication);
        org.apache.ambari.server.orm.dao.StackDAO stackDAO = org.apache.ambari.server.controller.internal.RepositoryVersionResourceProviderTest.injector.getInstance(org.apache.ambari.server.orm.dao.StackDAO.class);
        org.apache.ambari.server.orm.entities.StackEntity stackEntity = stackDAO.find("HDP", "1.1");
        org.junit.Assert.assertNotNull(stackEntity);
        final org.apache.ambari.server.controller.spi.ResourceProvider provider = org.apache.ambari.server.controller.internal.RepositoryVersionResourceProviderTest.injector.getInstance(org.apache.ambari.server.controller.ResourceProviderFactory.class).getRepositoryVersionResourceProvider();
        final org.apache.ambari.server.orm.dao.RepositoryVersionDAO repositoryVersionDAO = org.apache.ambari.server.controller.internal.RepositoryVersionResourceProviderTest.injector.getInstance(org.apache.ambari.server.orm.dao.RepositoryVersionDAO.class);
        final org.apache.ambari.server.orm.entities.RepositoryVersionEntity entity = new org.apache.ambari.server.orm.entities.RepositoryVersionEntity();
        entity.setDisplayName("name");
        entity.addRepoOsEntities(org.apache.ambari.server.controller.internal.RepositoryVersionResourceProviderTest.osRedhat6);
        entity.setStack(stackEntity);
        entity.setVersion("1.1.1.1");
        final org.apache.ambari.server.controller.spi.Request getRequest = org.apache.ambari.server.controller.utilities.PropertyHelper.getReadRequest(org.apache.ambari.server.controller.internal.RepositoryVersionResourceProvider.REPOSITORY_VERSION_ID_PROPERTY_ID, org.apache.ambari.server.controller.internal.RepositoryVersionResourceProvider.REPOSITORY_VERSION_STACK_NAME_PROPERTY_ID, org.apache.ambari.server.controller.internal.RepositoryVersionResourceProvider.REPOSITORY_VERSION_STACK_VERSION_PROPERTY_ID);
        final org.apache.ambari.server.controller.spi.Predicate predicateStackName = new org.apache.ambari.server.controller.utilities.PredicateBuilder().property(org.apache.ambari.server.controller.internal.RepositoryVersionResourceProvider.REPOSITORY_VERSION_STACK_NAME_PROPERTY_ID).equals("HDP").toPredicate();
        final org.apache.ambari.server.controller.spi.Predicate predicateStackVersion = new org.apache.ambari.server.controller.utilities.PredicateBuilder().property(org.apache.ambari.server.controller.internal.RepositoryVersionResourceProvider.REPOSITORY_VERSION_STACK_VERSION_PROPERTY_ID).equals("1.1").toPredicate();
        org.junit.Assert.assertEquals(0, provider.getResources(getRequest, new org.apache.ambari.server.controller.predicate.AndPredicate(predicateStackName, predicateStackVersion)).size());
        repositoryVersionDAO.create(entity);
        org.junit.Assert.assertEquals(1, provider.getResources(getRequest, new org.apache.ambari.server.controller.predicate.AndPredicate(predicateStackName, predicateStackVersion)).size());
    }

    @org.junit.Test
    public void testValidateRepositoryVersion() throws java.lang.Exception {
        org.apache.ambari.server.orm.dao.StackDAO stackDAO = org.apache.ambari.server.controller.internal.RepositoryVersionResourceProviderTest.injector.getInstance(org.apache.ambari.server.orm.dao.StackDAO.class);
        org.apache.ambari.server.orm.entities.StackEntity stackEntity = stackDAO.find("HDP", "1.1");
        org.junit.Assert.assertNotNull(stackEntity);
        final org.apache.ambari.server.orm.entities.RepositoryVersionEntity entity = new org.apache.ambari.server.orm.entities.RepositoryVersionEntity();
        entity.setDisplayName("name");
        entity.setStack(stackEntity);
        entity.setVersion("1.1");
        java.util.List<org.apache.ambari.server.orm.entities.RepoOsEntity> osEntities = new java.util.ArrayList<>();
        org.apache.ambari.server.orm.entities.RepoDefinitionEntity repoDefinitionEntity1 = new org.apache.ambari.server.orm.entities.RepoDefinitionEntity();
        repoDefinitionEntity1.setRepoID("1");
        repoDefinitionEntity1.setBaseUrl("http://example.com/repo1");
        repoDefinitionEntity1.setRepoName("1");
        repoDefinitionEntity1.setUnique(true);
        org.apache.ambari.server.orm.entities.RepoOsEntity repoOsEntity = new org.apache.ambari.server.orm.entities.RepoOsEntity();
        repoOsEntity.setFamily("redhat6");
        repoOsEntity.setAmbariManaged(true);
        repoOsEntity.addRepoDefinition(repoDefinitionEntity1);
        osEntities.add(repoOsEntity);
        entity.addRepoOsEntities(osEntities);
        final org.apache.ambari.server.orm.dao.RepositoryVersionDAO repositoryVersionDAO = org.apache.ambari.server.controller.internal.RepositoryVersionResourceProviderTest.injector.getInstance(org.apache.ambari.server.orm.dao.RepositoryVersionDAO.class);
        org.apache.ambari.server.api.services.AmbariMetaInfo info = org.apache.ambari.server.controller.internal.RepositoryVersionResourceProviderTest.injector.getInstance(org.apache.ambari.server.api.services.AmbariMetaInfo.class);
        org.apache.ambari.server.controller.internal.RepositoryVersionResourceProvider.validateRepositoryVersion(repositoryVersionDAO, info, entity);
        entity.setVersion("1.1-17");
        org.apache.ambari.server.controller.internal.RepositoryVersionResourceProvider.validateRepositoryVersion(repositoryVersionDAO, info, entity);
        entity.setVersion("1.1.1.1");
        org.apache.ambari.server.controller.internal.RepositoryVersionResourceProvider.validateRepositoryVersion(repositoryVersionDAO, info, entity);
        entity.setVersion("1.1.343432.2");
        org.apache.ambari.server.controller.internal.RepositoryVersionResourceProvider.validateRepositoryVersion(repositoryVersionDAO, info, entity);
        entity.setVersion("1.1.343432.2-234234324");
        org.apache.ambari.server.controller.internal.RepositoryVersionResourceProvider.validateRepositoryVersion(repositoryVersionDAO, info, entity);
        entity.addRepoOsEntities(new java.util.ArrayList<>());
        try {
            org.apache.ambari.server.controller.internal.RepositoryVersionResourceProvider.validateRepositoryVersion(repositoryVersionDAO, info, entity);
            org.junit.Assert.fail("Should throw exception");
        } catch (java.lang.Exception ex) {
        }
        org.apache.ambari.server.orm.entities.StackEntity bigtop = new org.apache.ambari.server.orm.entities.StackEntity();
        bigtop.setStackName("BIGTOP");
        entity.setStack(bigtop);
        try {
            org.apache.ambari.server.controller.internal.RepositoryVersionResourceProvider.validateRepositoryVersion(repositoryVersionDAO, info, entity);
            org.junit.Assert.fail("Should throw exception");
        } catch (java.lang.Exception ex) {
        }
        entity.setDisplayName("name");
        entity.setStack(stackEntity);
        entity.setVersion("1.1");
        entity.addRepoOsEntities(osEntities);
        repositoryVersionDAO.create(entity);
        final org.apache.ambari.server.orm.entities.RepositoryVersionEntity entity2 = new org.apache.ambari.server.orm.entities.RepositoryVersionEntity();
        entity2.setId(2L);
        entity2.setDisplayName("name2");
        entity2.setStack(stackEntity);
        entity2.setVersion("1.2");
        entity2.addRepoOsEntities(osEntities);
        try {
            org.apache.ambari.server.controller.internal.RepositoryVersionResourceProvider.validateRepositoryVersion(repositoryVersionDAO, info, entity2);
            org.junit.Assert.fail("Should throw exception: Base url http://example.com/repo1 is already defined for another repository version");
        } catch (java.lang.Exception ex) {
        }
        final org.apache.ambari.server.orm.entities.RepositoryVersionEntity entity3 = new org.apache.ambari.server.orm.entities.RepositoryVersionEntity();
        entity3.setId(3L);
        entity3.setDisplayName("name2");
        entity3.setStack(stackEntity);
        entity3.setVersion("1.1");
        entity3.addRepoOsEntities(osEntities);
        try {
            org.apache.ambari.server.controller.internal.RepositoryVersionResourceProvider.validateRepositoryVersion(repositoryVersionDAO, info, entity3);
            org.junit.Assert.fail("Expected exception");
        } catch (org.apache.ambari.server.AmbariException e) {
        }
        entity3.addRepoOsEntities(osEntities);
        repoOsEntity.setAmbariManaged(false);
        org.apache.ambari.server.controller.internal.RepositoryVersionResourceProvider.validateRepositoryVersion(repositoryVersionDAO, info, entity3);
    }

    @org.junit.Test
    public void testDeleteResourcesAsAdministrator() throws java.lang.Exception {
        testDeleteResources(org.apache.ambari.server.security.TestAuthenticationFactory.createAdministrator());
    }

    @org.junit.Test(expected = org.apache.ambari.server.security.authorization.AuthorizationException.class)
    public void testDeleteResourcesAsClusterAdministrator() throws java.lang.Exception {
        testDeleteResources(org.apache.ambari.server.security.TestAuthenticationFactory.createClusterAdministrator());
    }

    private void testDeleteResources(org.springframework.security.core.Authentication authentication) throws java.lang.Exception {
        org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(authentication);
        final org.apache.ambari.server.controller.spi.ResourceProvider provider = org.apache.ambari.server.controller.internal.RepositoryVersionResourceProviderTest.injector.getInstance(org.apache.ambari.server.controller.ResourceProviderFactory.class).getRepositoryVersionResourceProvider();
        final java.util.Set<java.util.Map<java.lang.String, java.lang.Object>> propertySet = new java.util.LinkedHashSet<>();
        final java.util.Map<java.lang.String, java.lang.Object> properties = new java.util.LinkedHashMap<>();
        properties.put(org.apache.ambari.server.controller.internal.RepositoryVersionResourceProvider.REPOSITORY_VERSION_DISPLAY_NAME_PROPERTY_ID, "name");
        properties.put(org.apache.ambari.server.controller.internal.RepositoryVersionResourceProvider.SUBRESOURCE_OPERATING_SYSTEMS_PROPERTY_ID, new com.google.gson.Gson().fromJson("[{\"OperatingSystems/os_type\":\"redhat6\",\"repositories\":[{\"Repositories/repo_id\":\"1\",\"Repositories/repo_name\":\"1\",\"Repositories/base_url\":\"1\",\"Repositories/unique\":\"true\"}]}]", java.lang.Object.class));
        properties.put(org.apache.ambari.server.controller.internal.RepositoryVersionResourceProvider.REPOSITORY_VERSION_STACK_NAME_PROPERTY_ID, "HDP");
        properties.put(org.apache.ambari.server.controller.internal.RepositoryVersionResourceProvider.REPOSITORY_VERSION_STACK_VERSION_PROPERTY_ID, "1.1");
        properties.put(org.apache.ambari.server.controller.internal.RepositoryVersionResourceProvider.REPOSITORY_VERSION_REPOSITORY_VERSION_PROPERTY_ID, "1.1.1.2");
        propertySet.add(properties);
        final org.apache.ambari.server.controller.spi.Predicate predicateStackName = new org.apache.ambari.server.controller.utilities.PredicateBuilder().property(org.apache.ambari.server.controller.internal.RepositoryVersionResourceProvider.REPOSITORY_VERSION_STACK_NAME_PROPERTY_ID).equals("HDP").toPredicate();
        final org.apache.ambari.server.controller.spi.Predicate predicateStackVersion = new org.apache.ambari.server.controller.utilities.PredicateBuilder().property(org.apache.ambari.server.controller.internal.RepositoryVersionResourceProvider.REPOSITORY_VERSION_STACK_VERSION_PROPERTY_ID).equals("1.1").toPredicate();
        final org.apache.ambari.server.controller.spi.Request getRequest = org.apache.ambari.server.controller.utilities.PropertyHelper.getReadRequest(org.apache.ambari.server.controller.internal.RepositoryVersionResourceProvider.REPOSITORY_VERSION_DISPLAY_NAME_PROPERTY_ID);
        org.junit.Assert.assertEquals(0, provider.getResources(getRequest, new org.apache.ambari.server.controller.predicate.AndPredicate(predicateStackName, predicateStackVersion)).size());
        final org.apache.ambari.server.controller.spi.Request createRequest = org.apache.ambari.server.controller.utilities.PropertyHelper.getCreateRequest(propertySet, null);
        provider.createResources(createRequest);
        org.junit.Assert.assertEquals(1, provider.getResources(getRequest, new org.apache.ambari.server.controller.predicate.AndPredicate(predicateStackName, predicateStackVersion)).size());
        final org.apache.ambari.server.controller.spi.Predicate predicate = new org.apache.ambari.server.controller.utilities.PredicateBuilder().property(org.apache.ambari.server.controller.internal.RepositoryVersionResourceProvider.REPOSITORY_VERSION_ID_PROPERTY_ID).equals("1").toPredicate();
        provider.deleteResources(new org.apache.ambari.server.controller.internal.RequestImpl(null, null, null, null), predicate);
        org.junit.Assert.assertEquals(0, provider.getResources(getRequest, new org.apache.ambari.server.controller.predicate.AndPredicate(predicateStackName, predicateStackVersion)).size());
    }

    @org.junit.Test
    public void testUpdateResourcesAsAdministrator() throws java.lang.Exception {
        testUpdateResources(org.apache.ambari.server.security.TestAuthenticationFactory.createAdministrator());
    }

    @org.junit.Test(expected = org.apache.ambari.server.security.authorization.AuthorizationException.class)
    public void testUpdateResourcesAsClusterAdministrator() throws java.lang.Exception {
        testUpdateResources(org.apache.ambari.server.security.TestAuthenticationFactory.createClusterAdministrator());
    }

    private void testUpdateResources(org.springframework.security.core.Authentication authentication) throws java.lang.Exception {
        org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(authentication);
        final org.apache.ambari.server.controller.spi.ResourceProvider provider = org.apache.ambari.server.controller.internal.RepositoryVersionResourceProviderTest.injector.getInstance(org.apache.ambari.server.controller.ResourceProviderFactory.class).getRepositoryVersionResourceProvider();
        final java.util.Set<java.util.Map<java.lang.String, java.lang.Object>> propertySet = new java.util.LinkedHashSet<>();
        final java.util.Map<java.lang.String, java.lang.Object> properties = new java.util.LinkedHashMap<>();
        properties.put(org.apache.ambari.server.controller.internal.RepositoryVersionResourceProvider.REPOSITORY_VERSION_DISPLAY_NAME_PROPERTY_ID, "name");
        properties.put(org.apache.ambari.server.controller.internal.RepositoryVersionResourceProvider.SUBRESOURCE_OPERATING_SYSTEMS_PROPERTY_ID, new com.google.gson.Gson().fromJson("[{\"OperatingSystems/os_type\":\"redhat6\",\"repositories\":[{\"Repositories/repo_id\":\"1\",\"Repositories/repo_name\":\"1\",\"Repositories/base_url\":\"http://example.com/repo1\",\"Repositories/unique\":\"true\"}]}]", java.lang.Object.class));
        properties.put(org.apache.ambari.server.controller.internal.RepositoryVersionResourceProvider.REPOSITORY_VERSION_STACK_NAME_PROPERTY_ID, "HDP");
        properties.put(org.apache.ambari.server.controller.internal.RepositoryVersionResourceProvider.REPOSITORY_VERSION_STACK_VERSION_PROPERTY_ID, "1.1");
        properties.put(org.apache.ambari.server.controller.internal.RepositoryVersionResourceProvider.REPOSITORY_VERSION_REPOSITORY_VERSION_PROPERTY_ID, "1.1.1.1");
        propertySet.add(properties);
        final org.apache.ambari.server.controller.spi.Predicate predicateStackName = new org.apache.ambari.server.controller.utilities.PredicateBuilder().property(org.apache.ambari.server.controller.internal.RepositoryVersionResourceProvider.REPOSITORY_VERSION_STACK_NAME_PROPERTY_ID).equals("HDP").toPredicate();
        final org.apache.ambari.server.controller.spi.Predicate predicateStackVersion = new org.apache.ambari.server.controller.utilities.PredicateBuilder().property(org.apache.ambari.server.controller.internal.RepositoryVersionResourceProvider.REPOSITORY_VERSION_STACK_VERSION_PROPERTY_ID).equals("1.1").toPredicate();
        final org.apache.ambari.server.controller.spi.Request getRequest = org.apache.ambari.server.controller.utilities.PropertyHelper.getReadRequest(org.apache.ambari.server.controller.internal.RepositoryVersionResourceProvider.REPOSITORY_VERSION_DISPLAY_NAME_PROPERTY_ID, org.apache.ambari.server.controller.internal.RepositoryVersionResourceProvider.SUBRESOURCE_OPERATING_SYSTEMS_PROPERTY_ID);
        org.junit.Assert.assertEquals(0, provider.getResources(getRequest, new org.apache.ambari.server.controller.predicate.AndPredicate(predicateStackName, predicateStackVersion)).size());
        final org.apache.ambari.server.controller.spi.Request createRequest = org.apache.ambari.server.controller.utilities.PropertyHelper.getCreateRequest(propertySet, null);
        provider.createResources(createRequest);
        org.junit.Assert.assertEquals(1, provider.getResources(getRequest, new org.apache.ambari.server.controller.predicate.AndPredicate(predicateStackName, predicateStackVersion)).size());
        org.junit.Assert.assertEquals("name", provider.getResources(getRequest, new org.apache.ambari.server.controller.predicate.AndPredicate(predicateStackName, predicateStackVersion)).iterator().next().getPropertyValue(org.apache.ambari.server.controller.internal.RepositoryVersionResourceProvider.REPOSITORY_VERSION_DISPLAY_NAME_PROPERTY_ID));
        properties.put(org.apache.ambari.server.controller.internal.RepositoryVersionResourceProvider.REPOSITORY_VERSION_ID_PROPERTY_ID, "1");
        properties.put(org.apache.ambari.server.controller.internal.RepositoryVersionResourceProvider.REPOSITORY_VERSION_DISPLAY_NAME_PROPERTY_ID, "name2");
        final org.apache.ambari.server.controller.spi.Request updateRequest = org.apache.ambari.server.controller.utilities.PropertyHelper.getUpdateRequest(properties, null);
        provider.updateResources(updateRequest, new org.apache.ambari.server.controller.predicate.AndPredicate(predicateStackName, predicateStackVersion));
        org.junit.Assert.assertEquals("name2", provider.getResources(getRequest, new org.apache.ambari.server.controller.predicate.AndPredicate(predicateStackName, predicateStackVersion)).iterator().next().getPropertyValue(org.apache.ambari.server.controller.internal.RepositoryVersionResourceProvider.REPOSITORY_VERSION_DISPLAY_NAME_PROPERTY_ID));
        org.apache.ambari.server.api.services.AmbariMetaInfo ambariMetaInfo = org.apache.ambari.server.controller.internal.RepositoryVersionResourceProviderTest.injector.getInstance(org.apache.ambari.server.api.services.AmbariMetaInfo.class);
        java.lang.String stackName = properties.get(org.apache.ambari.server.controller.internal.RepositoryVersionResourceProvider.REPOSITORY_VERSION_STACK_NAME_PROPERTY_ID).toString();
        java.lang.String stackVersion = properties.get(org.apache.ambari.server.controller.internal.RepositoryVersionResourceProvider.REPOSITORY_VERSION_STACK_VERSION_PROPERTY_ID).toString();
        java.lang.Object operatingSystems = properties.get(org.apache.ambari.server.controller.internal.RepositoryVersionResourceProvider.SUBRESOURCE_OPERATING_SYSTEMS_PROPERTY_ID);
        com.google.gson.Gson gson = new com.google.gson.Gson();
        java.lang.String operatingSystemsJson = gson.toJson(operatingSystems);
        org.apache.ambari.server.stack.upgrade.RepositoryVersionHelper repositoryVersionHelper = new org.apache.ambari.server.stack.upgrade.RepositoryVersionHelper();
        java.util.List<org.apache.ambari.server.orm.entities.RepoOsEntity> operatingSystemEntities = repositoryVersionHelper.parseOperatingSystems(operatingSystemsJson);
        for (org.apache.ambari.server.orm.entities.RepoOsEntity operatingSystemEntity : operatingSystemEntities) {
            java.lang.String osType = operatingSystemEntity.getFamily();
            java.util.List<org.apache.ambari.server.orm.entities.RepoDefinitionEntity> repositories = operatingSystemEntity.getRepoDefinitionEntities();
            for (org.apache.ambari.server.orm.entities.RepoDefinitionEntity repository : repositories) {
                org.apache.ambari.server.state.RepositoryInfo repo = ambariMetaInfo.getRepository(stackName, stackVersion, osType, repository.getRepoID());
                if (repo != null) {
                    java.lang.String baseUrlActual = repo.getBaseUrl();
                    java.lang.String baseUrlExpected = repository.getBaseUrl();
                    org.junit.Assert.assertEquals(baseUrlExpected, baseUrlActual);
                }
            }
        }
        properties.put(org.apache.ambari.server.controller.internal.RepositoryVersionResourceProvider.SUBRESOURCE_OPERATING_SYSTEMS_PROPERTY_ID, new com.google.gson.Gson().fromJson("[{\"OperatingSystems/os_type\":\"redhat6\",\"repositories\":[{\"Repositories/repo_id\":\"2\",\"Repositories/repo_name\":\"2\",\"Repositories/base_url\":\"2\",\"Repositories/unique\":\"true\"}]}]", java.lang.Object.class));
        provider.updateResources(updateRequest, new org.apache.ambari.server.controller.predicate.AndPredicate(predicateStackName, predicateStackVersion));
        try {
            provider.updateResources(updateRequest, new org.apache.ambari.server.controller.predicate.AndPredicate(predicateStackName, predicateStackVersion));
        } catch (java.lang.Exception ex) {
            org.junit.Assert.fail("Update of repository should be allowed when repo version is installed on any cluster");
        }
    }

    @org.junit.Test
    public void testUpdateResourcesNoManageRepos() throws java.lang.Exception {
        org.springframework.security.core.Authentication authentication = org.apache.ambari.server.security.TestAuthenticationFactory.createAdministrator();
        org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(authentication);
        final org.apache.ambari.server.controller.spi.ResourceProvider provider = org.apache.ambari.server.controller.internal.RepositoryVersionResourceProviderTest.injector.getInstance(org.apache.ambari.server.controller.ResourceProviderFactory.class).getRepositoryVersionResourceProvider();
        final java.util.Set<java.util.Map<java.lang.String, java.lang.Object>> propertySet = new java.util.LinkedHashSet<>();
        final java.util.Map<java.lang.String, java.lang.Object> properties = new java.util.LinkedHashMap<>();
        properties.put(org.apache.ambari.server.controller.internal.RepositoryVersionResourceProvider.REPOSITORY_VERSION_DISPLAY_NAME_PROPERTY_ID, "name");
        properties.put(org.apache.ambari.server.controller.internal.RepositoryVersionResourceProvider.SUBRESOURCE_OPERATING_SYSTEMS_PROPERTY_ID, new com.google.gson.Gson().fromJson("[{\"OperatingSystems/os_type\":\"redhat6\",\"repositories\":[{\"Repositories/repo_id\":\"1\",\"Repositories/repo_name\":\"1\",\"Repositories/base_url\":\"http://example.com/repo1\",\"Repositories/unique\":\"true\"}]}]", java.lang.Object.class));
        properties.put(org.apache.ambari.server.controller.internal.RepositoryVersionResourceProvider.REPOSITORY_VERSION_STACK_NAME_PROPERTY_ID, "HDP");
        properties.put(org.apache.ambari.server.controller.internal.RepositoryVersionResourceProvider.REPOSITORY_VERSION_STACK_VERSION_PROPERTY_ID, "1.1");
        properties.put(org.apache.ambari.server.controller.internal.RepositoryVersionResourceProvider.REPOSITORY_VERSION_REPOSITORY_VERSION_PROPERTY_ID, "1.1.1.1");
        propertySet.add(properties);
        final org.apache.ambari.server.controller.spi.Predicate predicateStackName = new org.apache.ambari.server.controller.utilities.PredicateBuilder().property(org.apache.ambari.server.controller.internal.RepositoryVersionResourceProvider.REPOSITORY_VERSION_STACK_NAME_PROPERTY_ID).equals("HDP").toPredicate();
        final org.apache.ambari.server.controller.spi.Predicate predicateStackVersion = new org.apache.ambari.server.controller.utilities.PredicateBuilder().property(org.apache.ambari.server.controller.internal.RepositoryVersionResourceProvider.REPOSITORY_VERSION_STACK_VERSION_PROPERTY_ID).equals("1.1").toPredicate();
        final org.apache.ambari.server.controller.spi.Request getRequest = org.apache.ambari.server.controller.utilities.PropertyHelper.getReadRequest(org.apache.ambari.server.controller.internal.RepositoryVersionResourceProvider.REPOSITORY_VERSION_DISPLAY_NAME_PROPERTY_ID, org.apache.ambari.server.controller.internal.RepositoryVersionResourceProvider.SUBRESOURCE_OPERATING_SYSTEMS_PROPERTY_ID);
        org.junit.Assert.assertEquals(0, provider.getResources(getRequest, new org.apache.ambari.server.controller.predicate.AndPredicate(predicateStackName, predicateStackVersion)).size());
        final org.apache.ambari.server.controller.spi.Request createRequest = org.apache.ambari.server.controller.utilities.PropertyHelper.getCreateRequest(propertySet, null);
        provider.createResources(createRequest);
        org.junit.Assert.assertEquals(1, provider.getResources(getRequest, new org.apache.ambari.server.controller.predicate.AndPredicate(predicateStackName, predicateStackVersion)).size());
        org.junit.Assert.assertEquals("name", provider.getResources(getRequest, new org.apache.ambari.server.controller.predicate.AndPredicate(predicateStackName, predicateStackVersion)).iterator().next().getPropertyValue(org.apache.ambari.server.controller.internal.RepositoryVersionResourceProvider.REPOSITORY_VERSION_DISPLAY_NAME_PROPERTY_ID));
        properties.put(org.apache.ambari.server.controller.internal.RepositoryVersionResourceProvider.REPOSITORY_VERSION_ID_PROPERTY_ID, "1");
        properties.put(org.apache.ambari.server.controller.internal.RepositoryVersionResourceProvider.REPOSITORY_VERSION_DISPLAY_NAME_PROPERTY_ID, "name2");
        properties.put(org.apache.ambari.server.controller.internal.RepositoryVersionResourceProvider.SUBRESOURCE_OPERATING_SYSTEMS_PROPERTY_ID, new com.google.gson.Gson().fromJson("[{\"OperatingSystems/ambari_managed_repositories\":false, \"OperatingSystems/os_type\":\"redhat6\",\"repositories\":[{\"Repositories/repo_id\":\"1\",\"Repositories/repo_name\":\"1\",\"Repositories/base_url\":\"http://example.com/repo1\",\"Repositories/unique\":\"true\"}]}]", java.lang.Object.class));
        final org.apache.ambari.server.controller.spi.Request updateRequest = org.apache.ambari.server.controller.utilities.PropertyHelper.getUpdateRequest(properties, null);
        provider.updateResources(updateRequest, new org.apache.ambari.server.controller.predicate.AndPredicate(predicateStackName, predicateStackVersion));
        org.junit.Assert.assertEquals("name2", provider.getResources(getRequest, new org.apache.ambari.server.controller.predicate.AndPredicate(predicateStackName, predicateStackVersion)).iterator().next().getPropertyValue(org.apache.ambari.server.controller.internal.RepositoryVersionResourceProvider.REPOSITORY_VERSION_DISPLAY_NAME_PROPERTY_ID));
        org.apache.ambari.server.api.services.AmbariMetaInfo ambariMetaInfo = org.apache.ambari.server.controller.internal.RepositoryVersionResourceProviderTest.injector.getInstance(org.apache.ambari.server.api.services.AmbariMetaInfo.class);
        java.lang.String stackName = properties.get(org.apache.ambari.server.controller.internal.RepositoryVersionResourceProvider.REPOSITORY_VERSION_STACK_NAME_PROPERTY_ID).toString();
        java.lang.String stackVersion = properties.get(org.apache.ambari.server.controller.internal.RepositoryVersionResourceProvider.REPOSITORY_VERSION_STACK_VERSION_PROPERTY_ID).toString();
        java.lang.Object operatingSystems = properties.get(org.apache.ambari.server.controller.internal.RepositoryVersionResourceProvider.SUBRESOURCE_OPERATING_SYSTEMS_PROPERTY_ID);
        com.google.gson.Gson gson = new com.google.gson.Gson();
        java.lang.String operatingSystemsJson = gson.toJson(operatingSystems);
        org.apache.ambari.server.stack.upgrade.RepositoryVersionHelper repositoryVersionHelper = new org.apache.ambari.server.stack.upgrade.RepositoryVersionHelper();
        java.util.List<org.apache.ambari.server.orm.entities.RepoOsEntity> operatingSystemEntities = repositoryVersionHelper.parseOperatingSystems(operatingSystemsJson);
        for (org.apache.ambari.server.orm.entities.RepoOsEntity operatingSystemEntity : operatingSystemEntities) {
            org.junit.Assert.assertFalse(operatingSystemEntity.isAmbariManaged());
            java.lang.String osType = operatingSystemEntity.getFamily();
            java.util.List<org.apache.ambari.server.orm.entities.RepoDefinitionEntity> repositories = operatingSystemEntity.getRepoDefinitionEntities();
            for (org.apache.ambari.server.orm.entities.RepoDefinitionEntity repository : repositories) {
                org.apache.ambari.server.state.RepositoryInfo repo = ambariMetaInfo.getRepository(stackName, stackVersion, osType, repository.getRepoID());
                if (repo != null) {
                    java.lang.String baseUrlActual = repo.getBaseUrl();
                    java.lang.String baseUrlExpected = repository.getBaseUrl();
                    org.junit.Assert.assertEquals(baseUrlExpected, baseUrlActual);
                }
            }
        }
    }

    @org.junit.Test
    public void testVersionInStack() {
        org.apache.ambari.server.state.StackId sid = new org.apache.ambari.server.state.StackId("HDP-2.3");
        org.apache.ambari.server.state.StackId sid2 = new org.apache.ambari.server.state.StackId("HDP-2.3.NEW");
        org.apache.ambari.server.state.StackId sid3 = new org.apache.ambari.server.state.StackId("HDF-2.3");
        org.junit.Assert.assertEquals(true, org.apache.ambari.server.orm.entities.RepositoryVersionEntity.isVersionInStack(sid, "2.3"));
        org.junit.Assert.assertEquals(true, org.apache.ambari.server.orm.entities.RepositoryVersionEntity.isVersionInStack(sid2, "2.3"));
        org.junit.Assert.assertEquals(true, org.apache.ambari.server.orm.entities.RepositoryVersionEntity.isVersionInStack(sid3, "2.3"));
        org.junit.Assert.assertEquals(true, org.apache.ambari.server.orm.entities.RepositoryVersionEntity.isVersionInStack(sid, "2.3.1"));
        org.junit.Assert.assertEquals(true, org.apache.ambari.server.orm.entities.RepositoryVersionEntity.isVersionInStack(sid2, "2.3.1"));
        org.junit.Assert.assertEquals(true, org.apache.ambari.server.orm.entities.RepositoryVersionEntity.isVersionInStack(sid3, "2.3.1"));
        org.junit.Assert.assertEquals(true, org.apache.ambari.server.orm.entities.RepositoryVersionEntity.isVersionInStack(sid, "2.3.2.0-2300"));
        org.junit.Assert.assertEquals(true, org.apache.ambari.server.orm.entities.RepositoryVersionEntity.isVersionInStack(sid2, "2.3.2.1-3562"));
        org.junit.Assert.assertEquals(true, org.apache.ambari.server.orm.entities.RepositoryVersionEntity.isVersionInStack(sid3, "2.3.2.1-3562"));
        org.junit.Assert.assertEquals(true, org.apache.ambari.server.orm.entities.RepositoryVersionEntity.isVersionInStack(sid, "HDP-2.3.2.0-2300"));
        org.junit.Assert.assertEquals(true, org.apache.ambari.server.orm.entities.RepositoryVersionEntity.isVersionInStack(sid2, "HDP-2.3.2.1-3562"));
        org.junit.Assert.assertEquals(true, org.apache.ambari.server.orm.entities.RepositoryVersionEntity.isVersionInStack(sid3, "HDF-2.3.2.1-3562"));
        org.junit.Assert.assertEquals(false, org.apache.ambari.server.orm.entities.RepositoryVersionEntity.isVersionInStack(sid, "2.4.2.0-2300"));
        org.junit.Assert.assertEquals(false, org.apache.ambari.server.orm.entities.RepositoryVersionEntity.isVersionInStack(sid2, "2.1"));
        org.junit.Assert.assertEquals(false, org.apache.ambari.server.orm.entities.RepositoryVersionEntity.isVersionInStack(sid3, "2.1"));
        org.junit.Assert.assertEquals(false, org.apache.ambari.server.orm.entities.RepositoryVersionEntity.isVersionInStack(sid, "HDP-2.4.2.0-2300"));
        org.junit.Assert.assertEquals(false, org.apache.ambari.server.orm.entities.RepositoryVersionEntity.isVersionInStack(sid2, "HDP-2.1"));
        org.junit.Assert.assertEquals(false, org.apache.ambari.server.orm.entities.RepositoryVersionEntity.isVersionInStack(sid3, "HDF-2.1"));
    }

    @org.junit.After
    public void after() throws org.apache.ambari.server.AmbariException, java.sql.SQLException {
        org.apache.ambari.server.H2DatabaseCleaner.clearDatabaseAndStopPersistenceService(org.apache.ambari.server.controller.internal.RepositoryVersionResourceProviderTest.injector);
        org.apache.ambari.server.controller.internal.RepositoryVersionResourceProviderTest.injector = null;
        org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(null);
    }
}