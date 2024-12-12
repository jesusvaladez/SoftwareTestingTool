package org.apache.ambari.server.controller.internal;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
public class VersionDefinitionResourceProviderTest {
    private com.google.inject.Injector injector;

    private org.apache.ambari.server.orm.entities.RepositoryVersionEntity parentEntity = null;

    @org.junit.Before
    public void before() throws java.lang.Exception {
        org.apache.ambari.server.orm.InMemoryDefaultTestModule module = new org.apache.ambari.server.orm.InMemoryDefaultTestModule();
        module.getProperties().put(org.apache.ambari.server.configuration.Configuration.VDF_FROM_FILESYSTEM.getKey(), "true");
        injector = com.google.inject.Guice.createInjector(module);
        injector.getInstance(org.apache.ambari.server.orm.GuiceJpaInitializer.class);
        org.apache.ambari.server.api.services.AmbariMetaInfo ami = injector.getInstance(org.apache.ambari.server.api.services.AmbariMetaInfo.class);
        ami.init();
        org.apache.ambari.server.orm.dao.StackDAO stackDao = injector.getInstance(org.apache.ambari.server.orm.dao.StackDAO.class);
        org.apache.ambari.server.orm.entities.StackEntity stack = stackDao.find("HDP", "2.2.0");
        parentEntity = new org.apache.ambari.server.orm.entities.RepositoryVersionEntity();
        parentEntity.setStack(stack);
        parentEntity.setDisplayName("2.2.0.0");
        parentEntity.setVersion("2.3.4.4-1234");
        org.apache.ambari.server.orm.dao.RepositoryVersionDAO dao = injector.getInstance(org.apache.ambari.server.orm.dao.RepositoryVersionDAO.class);
        dao.create(parentEntity);
    }

    @org.junit.After
    public void after() throws java.lang.Exception {
        org.apache.ambari.server.H2DatabaseCleaner.clearDatabaseAndStopPersistenceService(injector);
    }

    @org.junit.Test
    public void testWithParentFromBase64() throws java.lang.Exception {
        org.springframework.security.core.Authentication authentication = org.apache.ambari.server.security.TestAuthenticationFactory.createAdministrator();
        org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(authentication);
        java.io.File file = new java.io.File("src/test/resources/version_definition_resource_provider.xml");
        byte[] bytes = org.apache.commons.io.IOUtils.toByteArray(new java.io.FileInputStream(file));
        java.lang.String base64Str = org.apache.commons.codec.binary.Base64.encodeBase64String(bytes);
        final org.apache.ambari.server.controller.spi.ResourceProvider versionProvider = new org.apache.ambari.server.controller.internal.VersionDefinitionResourceProvider();
        final org.apache.ambari.server.controller.spi.ResourceProvider provider = injector.getInstance(org.apache.ambari.server.controller.ResourceProviderFactory.class).getRepositoryVersionResourceProvider();
        final java.util.Set<java.util.Map<java.lang.String, java.lang.Object>> propertySet = new java.util.LinkedHashSet<>();
        final java.util.Map<java.lang.String, java.lang.Object> properties = new java.util.LinkedHashMap<>();
        properties.put(org.apache.ambari.server.controller.internal.VersionDefinitionResourceProvider.VERSION_DEF_DEFINITION_BASE64, base64Str);
        propertySet.add(properties);
        final org.apache.ambari.server.controller.spi.Request createRequest = org.apache.ambari.server.controller.utilities.PropertyHelper.getCreateRequest(propertySet, null);
        org.apache.ambari.server.controller.spi.RequestStatus status = versionProvider.createResources(createRequest);
        org.junit.Assert.assertEquals(1, status.getAssociatedResources().size());
        org.apache.ambari.server.controller.spi.Request getRequest = org.apache.ambari.server.controller.utilities.PropertyHelper.getReadRequest("VersionDefinition");
        java.util.Set<org.apache.ambari.server.controller.spi.Resource> results = versionProvider.getResources(getRequest, null);
        org.junit.Assert.assertEquals(1, results.size());
        final org.apache.ambari.server.controller.spi.Predicate predicateStackName = new org.apache.ambari.server.controller.utilities.PredicateBuilder().property(org.apache.ambari.server.controller.internal.RepositoryVersionResourceProvider.REPOSITORY_VERSION_STACK_NAME_PROPERTY_ID).equals("HDP").toPredicate();
        final org.apache.ambari.server.controller.spi.Predicate predicateStackVersion = new org.apache.ambari.server.controller.utilities.PredicateBuilder().property(org.apache.ambari.server.controller.internal.RepositoryVersionResourceProvider.REPOSITORY_VERSION_STACK_VERSION_PROPERTY_ID).equals("2.2.0").toPredicate();
        results = provider.getResources(getRequest, new org.apache.ambari.server.controller.predicate.AndPredicate(predicateStackName, predicateStackVersion));
        org.junit.Assert.assertEquals(1, results.size());
        getRequest = org.apache.ambari.server.controller.utilities.PropertyHelper.getReadRequest(org.apache.ambari.server.controller.internal.RepositoryVersionResourceProvider.REPOSITORY_VERSION_DISPLAY_NAME_PROPERTY_ID, org.apache.ambari.server.controller.internal.RepositoryVersionResourceProvider.REPOSITORY_VERSION_ID_PROPERTY_ID, org.apache.ambari.server.controller.internal.RepositoryVersionResourceProvider.REPOSITORY_VERSION_REPOSITORY_VERSION_PROPERTY_ID, org.apache.ambari.server.controller.internal.RepositoryVersionResourceProvider.REPOSITORY_VERSION_STACK_NAME_PROPERTY_ID, org.apache.ambari.server.controller.internal.RepositoryVersionResourceProvider.REPOSITORY_VERSION_STACK_VERSION_PROPERTY_ID, org.apache.ambari.server.controller.internal.RepositoryVersionResourceProvider.SUBRESOURCE_OPERATING_SYSTEMS_PROPERTY_ID, org.apache.ambari.server.controller.internal.RepositoryVersionResourceProvider.SUBRESOURCE_REPOSITORIES_PROPERTY_ID, "RepositoryVersions/release", "RepositoryVersions/services", "RepositoryVersions/has_children", "RepositoryVersions/parent_id");
        results = provider.getResources(getRequest, new org.apache.ambari.server.controller.predicate.AndPredicate(predicateStackName, predicateStackVersion));
        org.junit.Assert.assertEquals(2, results.size());
        org.apache.ambari.server.controller.spi.Resource r = null;
        for (org.apache.ambari.server.controller.spi.Resource result : results) {
            if (result.getPropertyValue("RepositoryVersions/repository_version").equals("2.2.0.8-5678")) {
                r = result;
                break;
            }
        }
        org.junit.Assert.assertNotNull(r);
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.Object>> map = r.getPropertiesMap();
        org.junit.Assert.assertTrue(map.containsKey("RepositoryVersions"));
        java.util.Map<java.lang.String, java.lang.Object> vals = map.get("RepositoryVersions");
        org.junit.Assert.assertEquals("2.2.0.8-5678", vals.get("repository_version"));
        org.junit.Assert.assertNotNull(vals.get("parent_id"));
        org.junit.Assert.assertEquals(java.lang.Boolean.FALSE, vals.get("has_children"));
        org.junit.Assert.assertTrue(map.containsKey("RepositoryVersions/release"));
        vals = map.get("RepositoryVersions/release");
        org.junit.Assert.assertEquals("5678", vals.get("build"));
        org.junit.Assert.assertEquals("2.3.4.[1-9]", vals.get("compatible_with"));
        org.junit.Assert.assertEquals("http://docs.hortonworks.com/HDPDocuments/HDP2/HDP-2.3.4/", vals.get("notes"));
    }

    @org.junit.Test
    public void testWithParent() throws java.lang.Exception {
        org.springframework.security.core.Authentication authentication = org.apache.ambari.server.security.TestAuthenticationFactory.createAdministrator();
        org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(authentication);
        java.io.File file = new java.io.File("src/test/resources/version_definition_resource_provider.xml");
        final org.apache.ambari.server.controller.spi.ResourceProvider versionProvider = new org.apache.ambari.server.controller.internal.VersionDefinitionResourceProvider();
        final org.apache.ambari.server.controller.spi.ResourceProvider provider = injector.getInstance(org.apache.ambari.server.controller.ResourceProviderFactory.class).getRepositoryVersionResourceProvider();
        final java.util.Set<java.util.Map<java.lang.String, java.lang.Object>> propertySet = new java.util.LinkedHashSet<>();
        final java.util.Map<java.lang.String, java.lang.Object> properties = new java.util.LinkedHashMap<>();
        properties.put(org.apache.ambari.server.controller.internal.VersionDefinitionResourceProvider.VERSION_DEF_DEFINITION_URL, file.toURI().toURL().toString());
        propertySet.add(properties);
        final org.apache.ambari.server.controller.spi.Request createRequest = org.apache.ambari.server.controller.utilities.PropertyHelper.getCreateRequest(propertySet, null);
        org.apache.ambari.server.controller.spi.RequestStatus status = versionProvider.createResources(createRequest);
        org.junit.Assert.assertEquals(1, status.getAssociatedResources().size());
        org.apache.ambari.server.controller.spi.Request getRequest = org.apache.ambari.server.controller.utilities.PropertyHelper.getReadRequest("VersionDefinition");
        java.util.Set<org.apache.ambari.server.controller.spi.Resource> results = versionProvider.getResources(getRequest, null);
        org.junit.Assert.assertEquals(1, results.size());
        final org.apache.ambari.server.controller.spi.Predicate predicateStackName = new org.apache.ambari.server.controller.utilities.PredicateBuilder().property(org.apache.ambari.server.controller.internal.RepositoryVersionResourceProvider.REPOSITORY_VERSION_STACK_NAME_PROPERTY_ID).equals("HDP").toPredicate();
        final org.apache.ambari.server.controller.spi.Predicate predicateStackVersion = new org.apache.ambari.server.controller.utilities.PredicateBuilder().property(org.apache.ambari.server.controller.internal.RepositoryVersionResourceProvider.REPOSITORY_VERSION_STACK_VERSION_PROPERTY_ID).equals("2.2.0").toPredicate();
        results = provider.getResources(getRequest, new org.apache.ambari.server.controller.predicate.AndPredicate(predicateStackName, predicateStackVersion));
        org.junit.Assert.assertEquals(1, results.size());
        getRequest = org.apache.ambari.server.controller.utilities.PropertyHelper.getReadRequest(org.apache.ambari.server.controller.internal.RepositoryVersionResourceProvider.REPOSITORY_VERSION_DISPLAY_NAME_PROPERTY_ID, org.apache.ambari.server.controller.internal.RepositoryVersionResourceProvider.REPOSITORY_VERSION_ID_PROPERTY_ID, org.apache.ambari.server.controller.internal.RepositoryVersionResourceProvider.REPOSITORY_VERSION_REPOSITORY_VERSION_PROPERTY_ID, org.apache.ambari.server.controller.internal.RepositoryVersionResourceProvider.REPOSITORY_VERSION_STACK_NAME_PROPERTY_ID, org.apache.ambari.server.controller.internal.RepositoryVersionResourceProvider.REPOSITORY_VERSION_STACK_VERSION_PROPERTY_ID, org.apache.ambari.server.controller.internal.RepositoryVersionResourceProvider.SUBRESOURCE_OPERATING_SYSTEMS_PROPERTY_ID, org.apache.ambari.server.controller.internal.RepositoryVersionResourceProvider.SUBRESOURCE_REPOSITORIES_PROPERTY_ID, "RepositoryVersions/release", "RepositoryVersions/services", "RepositoryVersions/has_children", "RepositoryVersions/parent_id");
        results = provider.getResources(getRequest, new org.apache.ambari.server.controller.predicate.AndPredicate(predicateStackName, predicateStackVersion));
        org.junit.Assert.assertEquals(2, results.size());
        org.apache.ambari.server.controller.spi.Resource r = null;
        for (org.apache.ambari.server.controller.spi.Resource result : results) {
            if (result.getPropertyValue("RepositoryVersions/repository_version").equals("2.2.0.8-5678")) {
                r = result;
                break;
            }
        }
        org.junit.Assert.assertNotNull(r);
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.Object>> map = r.getPropertiesMap();
        org.junit.Assert.assertTrue(map.containsKey("RepositoryVersions"));
        java.util.Map<java.lang.String, java.lang.Object> vals = map.get("RepositoryVersions");
        org.junit.Assert.assertEquals("2.2.0.8-5678", vals.get("repository_version"));
        org.junit.Assert.assertNotNull(vals.get("parent_id"));
        org.junit.Assert.assertEquals(java.lang.Boolean.FALSE, vals.get("has_children"));
        org.junit.Assert.assertTrue(map.containsKey("RepositoryVersions/release"));
        vals = map.get("RepositoryVersions/release");
        org.junit.Assert.assertEquals("5678", vals.get("build"));
        org.junit.Assert.assertEquals("2.3.4.[1-9]", vals.get("compatible_with"));
        org.junit.Assert.assertEquals("http://docs.hortonworks.com/HDPDocuments/HDP2/HDP-2.3.4/", vals.get("notes"));
    }

    @org.junit.Test
    public void testGetAvailable() throws java.lang.Exception {
        org.apache.ambari.server.api.services.AmbariMetaInfo ami = injector.getInstance(org.apache.ambari.server.api.services.AmbariMetaInfo.class);
        org.apache.ambari.server.stack.StackManager sm = ami.getStackManager();
        int maxWait = 15000;
        int waitTime = 0;
        while ((waitTime < maxWait) && (!sm.haveAllRepoUrlsBeenResolved())) {
            java.lang.Thread.sleep(5);
            waitTime += 5;
        } 
        if (waitTime >= maxWait) {
            org.junit.Assert.fail("Latest Repo tasks did not complete");
        }
        org.springframework.security.core.Authentication authentication = org.apache.ambari.server.security.TestAuthenticationFactory.createAdministrator();
        org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(authentication);
        final org.apache.ambari.server.controller.spi.ResourceProvider versionProvider = new org.apache.ambari.server.controller.internal.VersionDefinitionResourceProvider();
        org.apache.ambari.server.controller.spi.Request getRequest = org.apache.ambari.server.controller.utilities.PropertyHelper.getReadRequest("VersionDefinition");
        org.apache.ambari.server.controller.spi.Predicate predicate = new org.apache.ambari.server.controller.utilities.PredicateBuilder().property(org.apache.ambari.server.controller.internal.VersionDefinitionResourceProvider.SHOW_AVAILABLE).equals("true").toPredicate();
        java.util.Set<org.apache.ambari.server.controller.spi.Resource> results = versionProvider.getResources(getRequest, predicate);
        org.junit.Assert.assertEquals(3, results.size());
        boolean found1 = false;
        boolean found2 = false;
        for (org.apache.ambari.server.controller.spi.Resource res : results) {
            if ("HDP-2.2.0-2.2.1.0".equals(res.getPropertyValue("VersionDefinition/id"))) {
                org.junit.Assert.assertEquals(java.lang.Boolean.FALSE, res.getPropertyValue("VersionDefinition/stack_default"));
                found1 = true;
            } else if ("HDP-2.2.0".equals(res.getPropertyValue("VersionDefinition/id"))) {
                org.junit.Assert.assertEquals(java.lang.Boolean.TRUE, res.getPropertyValue("VersionDefinition/stack_default"));
                org.apache.ambari.server.state.repository.VersionDefinitionXml vdf = ami.getVersionDefinition("HDP-2.2.0");
                org.junit.Assert.assertNotNull(vdf);
                org.junit.Assert.assertEquals(1, vdf.repositoryInfo.getOses().size());
                java.lang.String family1 = vdf.repositoryInfo.getOses().get(0).getFamily();
                org.junit.Assert.assertEquals("redhat6", family1);
                found2 = true;
            }
        }
        org.junit.Assert.assertTrue(found1);
        org.junit.Assert.assertTrue(found2);
    }

    @org.junit.Test
    public void testCreateByAvailable() throws java.lang.Exception {
        org.apache.ambari.server.api.services.AmbariMetaInfo ami = injector.getInstance(org.apache.ambari.server.api.services.AmbariMetaInfo.class);
        org.apache.ambari.server.stack.StackManager sm = ami.getStackManager();
        int maxWait = 15000;
        int waitTime = 0;
        while ((waitTime < maxWait) && (!sm.haveAllRepoUrlsBeenResolved())) {
            java.lang.Thread.sleep(5);
            waitTime += 5;
        } 
        if (waitTime >= maxWait) {
            org.junit.Assert.fail("Latest Repo tasks did not complete");
        }
        org.springframework.security.core.Authentication authentication = org.apache.ambari.server.security.TestAuthenticationFactory.createAdministrator();
        org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(authentication);
        final org.apache.ambari.server.controller.spi.ResourceProvider versionProvider = new org.apache.ambari.server.controller.internal.VersionDefinitionResourceProvider();
        org.apache.ambari.server.controller.spi.Request getRequest = org.apache.ambari.server.controller.utilities.PropertyHelper.getReadRequest("VersionDefinition");
        java.util.Set<org.apache.ambari.server.controller.spi.Resource> results = versionProvider.getResources(getRequest, null);
        org.junit.Assert.assertEquals(0, results.size());
        java.util.Map<java.lang.String, java.lang.Object> createMap = new java.util.HashMap<>();
        createMap.put("VersionDefinition/available", "HDP-2.2.0-2.2.1.0");
        org.apache.ambari.server.controller.spi.Request createRequest = org.apache.ambari.server.controller.utilities.PropertyHelper.getCreateRequest(java.util.Collections.singleton(createMap), null);
        versionProvider.createResources(createRequest);
        results = versionProvider.getResources(getRequest, null);
        org.junit.Assert.assertEquals(1, results.size());
    }

    @org.junit.Test
    public void testCreateDryRun() throws java.lang.Exception {
        org.springframework.security.core.Authentication authentication = org.apache.ambari.server.security.TestAuthenticationFactory.createAdministrator();
        org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(authentication);
        java.io.File file = new java.io.File("src/test/resources/version_definition_resource_provider.xml");
        final org.apache.ambari.server.controller.spi.ResourceProvider versionProvider = new org.apache.ambari.server.controller.internal.VersionDefinitionResourceProvider();
        final java.util.Set<java.util.Map<java.lang.String, java.lang.Object>> propertySet = new java.util.LinkedHashSet<>();
        final java.util.Map<java.lang.String, java.lang.Object> properties = new java.util.LinkedHashMap<>();
        properties.put(org.apache.ambari.server.controller.internal.VersionDefinitionResourceProvider.VERSION_DEF_DEFINITION_URL, file.toURI().toURL().toString());
        propertySet.add(properties);
        java.util.Map<java.lang.String, java.lang.String> info = java.util.Collections.singletonMap(org.apache.ambari.server.controller.spi.Request.DIRECTIVE_DRY_RUN, "true");
        final org.apache.ambari.server.controller.spi.Request createRequest = org.apache.ambari.server.controller.utilities.PropertyHelper.getCreateRequest(propertySet, info);
        org.apache.ambari.server.controller.spi.RequestStatus status = versionProvider.createResources(createRequest);
        org.junit.Assert.assertEquals(1, status.getAssociatedResources().size());
        org.apache.ambari.server.controller.spi.Resource res = status.getAssociatedResources().iterator().next();
        org.junit.Assert.assertTrue(res.getPropertiesMap().containsKey(""));
        java.util.Map<java.lang.String, java.lang.Object> resMap = res.getPropertiesMap().get("");
        org.junit.Assert.assertTrue(resMap.containsKey("operating_systems"));
        org.junit.Assert.assertTrue(res.getPropertiesMap().containsKey("VersionDefinition"));
        org.junit.Assert.assertEquals("2.2.0.8-5678", res.getPropertyValue("VersionDefinition/repository_version"));
        org.junit.Assert.assertNull(res.getPropertyValue("VersionDefinition/show_available"));
        org.apache.ambari.server.controller.spi.Request getRequest = org.apache.ambari.server.controller.utilities.PropertyHelper.getReadRequest("VersionDefinition");
        java.util.Set<org.apache.ambari.server.controller.spi.Resource> results = versionProvider.getResources(getRequest, null);
        org.junit.Assert.assertEquals(0, results.size());
    }

    @org.junit.Test
    public void testCreateDryRunByAvailable() throws java.lang.Exception {
        org.apache.ambari.server.api.services.AmbariMetaInfo ami = injector.getInstance(org.apache.ambari.server.api.services.AmbariMetaInfo.class);
        org.apache.ambari.server.stack.StackManager sm = ami.getStackManager();
        int maxWait = 15000;
        int waitTime = 0;
        while ((waitTime < maxWait) && (!sm.haveAllRepoUrlsBeenResolved())) {
            java.lang.Thread.sleep(5);
            waitTime += 5;
        } 
        if (waitTime >= maxWait) {
            org.junit.Assert.fail("Latest Repo tasks did not complete");
        }
        org.springframework.security.core.Authentication authentication = org.apache.ambari.server.security.TestAuthenticationFactory.createAdministrator();
        org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(authentication);
        final org.apache.ambari.server.controller.spi.ResourceProvider versionProvider = new org.apache.ambari.server.controller.internal.VersionDefinitionResourceProvider();
        org.apache.ambari.server.controller.spi.Request getRequest = org.apache.ambari.server.controller.utilities.PropertyHelper.getReadRequest("VersionDefinition");
        java.util.Set<org.apache.ambari.server.controller.spi.Resource> results = versionProvider.getResources(getRequest, null);
        org.junit.Assert.assertEquals(0, results.size());
        java.util.Map<java.lang.String, java.lang.Object> createMap = new java.util.HashMap<>();
        createMap.put("VersionDefinition/available", "HDP-2.2.0-2.2.1.0");
        java.util.Map<java.lang.String, java.lang.String> infoProps = java.util.Collections.singletonMap(org.apache.ambari.server.controller.spi.Request.DIRECTIVE_DRY_RUN, "true");
        org.apache.ambari.server.controller.spi.Request createRequest = org.apache.ambari.server.controller.utilities.PropertyHelper.getCreateRequest(java.util.Collections.singleton(createMap), infoProps);
        org.apache.ambari.server.controller.spi.RequestStatus status = versionProvider.createResources(createRequest);
        org.junit.Assert.assertEquals(1, status.getAssociatedResources().size());
        org.apache.ambari.server.controller.spi.Resource res = status.getAssociatedResources().iterator().next();
        org.junit.Assert.assertTrue(res.getPropertiesMap().containsKey(""));
        java.util.Map<java.lang.String, java.lang.Object> resMap = res.getPropertiesMap().get("");
        org.junit.Assert.assertTrue(resMap.containsKey("operating_systems"));
        org.junit.Assert.assertTrue(res.getPropertiesMap().containsKey("VersionDefinition"));
        org.junit.Assert.assertEquals("2.2.1.0", res.getPropertyValue("VersionDefinition/repository_version"));
        org.junit.Assert.assertNotNull(res.getPropertyValue("VersionDefinition/show_available"));
        org.junit.Assert.assertNotNull(res.getPropertyValue("VersionDefinition/validation"));
        java.util.Set<java.lang.String> validation = ((java.util.Set<java.lang.String>) (res.getPropertyValue("VersionDefinition/validation")));
        org.junit.Assert.assertNotNull(validation);
        org.junit.Assert.assertEquals(0, validation.size());
        getRequest = org.apache.ambari.server.controller.utilities.PropertyHelper.getReadRequest("VersionDefinition");
        results = versionProvider.getResources(getRequest, null);
        org.junit.Assert.assertEquals(0, results.size());
    }

    @org.junit.Test
    public void testCreateDryWithValidation() throws java.lang.Exception {
        org.apache.ambari.server.api.services.AmbariMetaInfo ami = injector.getInstance(org.apache.ambari.server.api.services.AmbariMetaInfo.class);
        org.apache.ambari.server.stack.StackManager sm = ami.getStackManager();
        int maxWait = 15000;
        int waitTime = 0;
        while ((waitTime < maxWait) && (!sm.haveAllRepoUrlsBeenResolved())) {
            java.lang.Thread.sleep(5);
            waitTime += 5;
        } 
        if (waitTime >= maxWait) {
            org.junit.Assert.fail("Latest Repo tasks did not complete");
        }
        org.springframework.security.core.Authentication authentication = org.apache.ambari.server.security.TestAuthenticationFactory.createAdministrator();
        org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(authentication);
        final org.apache.ambari.server.controller.spi.ResourceProvider versionProvider = new org.apache.ambari.server.controller.internal.VersionDefinitionResourceProvider();
        org.apache.ambari.server.controller.spi.Request getRequest = org.apache.ambari.server.controller.utilities.PropertyHelper.getReadRequest("VersionDefinition");
        java.util.Set<org.apache.ambari.server.controller.spi.Resource> results = versionProvider.getResources(getRequest, null);
        org.junit.Assert.assertEquals(0, results.size());
        java.util.Map<java.lang.String, java.lang.Object> createMap = new java.util.HashMap<>();
        createMap.put("VersionDefinition/available", "HDP-2.2.0-2.2.1.0");
        org.apache.ambari.server.controller.spi.Request createRequest = org.apache.ambari.server.controller.utilities.PropertyHelper.getCreateRequest(java.util.Collections.singleton(createMap), null);
        versionProvider.createResources(createRequest);
        results = versionProvider.getResources(getRequest, null);
        org.junit.Assert.assertEquals(1, results.size());
        java.util.Map<java.lang.String, java.lang.String> infoProps = new java.util.HashMap<>();
        infoProps.put(org.apache.ambari.server.controller.spi.Request.DIRECTIVE_DRY_RUN, "true");
        createRequest = org.apache.ambari.server.controller.utilities.PropertyHelper.getCreateRequest(java.util.Collections.singleton(createMap), infoProps);
        org.apache.ambari.server.controller.spi.RequestStatus status = versionProvider.createResources(createRequest);
        org.junit.Assert.assertEquals(1, status.getAssociatedResources().size());
        org.apache.ambari.server.controller.spi.Resource res = status.getAssociatedResources().iterator().next();
        org.junit.Assert.assertTrue(res.getPropertiesMap().containsKey(""));
        java.util.Map<java.lang.String, java.lang.Object> resMap = res.getPropertiesMap().get("");
        org.junit.Assert.assertTrue(resMap.containsKey("operating_systems"));
        org.junit.Assert.assertTrue(res.getPropertiesMap().containsKey("VersionDefinition"));
        org.junit.Assert.assertEquals("2.2.1.0", res.getPropertyValue("VersionDefinition/repository_version"));
        org.junit.Assert.assertNotNull(res.getPropertyValue("VersionDefinition/show_available"));
        org.junit.Assert.assertEquals("HDP-2.2.0.4", res.getPropertyValue("VersionDefinition/display_name"));
        org.junit.Assert.assertNotNull(res.getPropertyValue("VersionDefinition/validation"));
        java.util.Set<java.lang.String> validation = ((java.util.Set<java.lang.String>) (res.getPropertyValue("VersionDefinition/validation")));
        org.junit.Assert.assertEquals(3, validation.size());
        validation = ((java.util.Set<java.lang.String>) (res.getPropertyValue("VersionDefinition/validation")));
        org.junit.Assert.assertEquals(3, validation.size());
        boolean found = false;
        for (java.lang.String reason : validation) {
            if (reason.contains("http://baseurl1")) {
                found = true;
            }
        }
        org.junit.Assert.assertTrue("URL validation should be checked", found);
        infoProps.put(org.apache.ambari.server.controller.internal.VersionDefinitionResourceProvider.DIRECTIVE_SKIP_URL_CHECK, "true");
        createRequest = org.apache.ambari.server.controller.utilities.PropertyHelper.getCreateRequest(java.util.Collections.singleton(createMap), infoProps);
        status = versionProvider.createResources(createRequest);
        org.junit.Assert.assertEquals(1, status.getAssociatedResources().size());
        res = status.getAssociatedResources().iterator().next();
        org.junit.Assert.assertTrue(res.getPropertiesMap().containsKey("VersionDefinition"));
        org.junit.Assert.assertEquals("2.2.1.0", res.getPropertyValue("VersionDefinition/repository_version"));
        org.junit.Assert.assertNotNull(res.getPropertyValue("VersionDefinition/show_available"));
        org.junit.Assert.assertNotNull(res.getPropertyValue("VersionDefinition/validation"));
        validation = ((java.util.Set<java.lang.String>) (res.getPropertyValue("VersionDefinition/validation")));
        org.junit.Assert.assertEquals(2, validation.size());
        for (java.lang.String reason : validation) {
            if (reason.contains("http://baseurl1")) {
                org.junit.Assert.fail("URL validation should be skipped for http://baseurl1");
            }
        }
        infoProps.remove(org.apache.ambari.server.controller.internal.VersionDefinitionResourceProvider.DIRECTIVE_SKIP_URL_CHECK);
        createMap.put(org.apache.ambari.server.controller.internal.VersionDefinitionResourceProvider.VERSION_DEF_DISPLAY_NAME, "HDP-2.2.0.4-a");
        createRequest = org.apache.ambari.server.controller.utilities.PropertyHelper.getCreateRequest(java.util.Collections.singleton(createMap), infoProps);
        status = versionProvider.createResources(createRequest);
        org.junit.Assert.assertEquals(1, status.getAssociatedResources().size());
        res = status.getAssociatedResources().iterator().next();
        org.junit.Assert.assertTrue(res.getPropertiesMap().containsKey("VersionDefinition"));
        org.junit.Assert.assertEquals("2.2.0.4-a", res.getPropertyValue("VersionDefinition/repository_version"));
        org.junit.Assert.assertEquals("HDP-2.2.0.4-a", res.getPropertyValue("VersionDefinition/display_name"));
        org.junit.Assert.assertNotNull(res.getPropertyValue("VersionDefinition/show_available"));
        org.junit.Assert.assertNotNull(res.getPropertyValue("VersionDefinition/validation"));
        validation = ((java.util.Set<java.lang.String>) (res.getPropertyValue("VersionDefinition/validation")));
        org.junit.Assert.assertEquals(1, validation.size());
    }

    @org.junit.Test
    public void testCreatePatchNoParentVersions() throws java.lang.Exception {
        org.springframework.security.core.Authentication authentication = org.apache.ambari.server.security.TestAuthenticationFactory.createAdministrator();
        org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(authentication);
        java.io.File file = new java.io.File("src/test/resources/version_definition_resource_provider.xml");
        final org.apache.ambari.server.controller.spi.ResourceProvider versionProvider = new org.apache.ambari.server.controller.internal.VersionDefinitionResourceProvider();
        final java.util.Set<java.util.Map<java.lang.String, java.lang.Object>> propertySet = new java.util.LinkedHashSet<>();
        final java.util.Map<java.lang.String, java.lang.Object> properties = new java.util.LinkedHashMap<>();
        properties.put(org.apache.ambari.server.controller.internal.VersionDefinitionResourceProvider.VERSION_DEF_DEFINITION_URL, file.toURI().toURL().toString());
        propertySet.add(properties);
        org.apache.ambari.server.orm.dao.RepositoryVersionDAO dao = injector.getInstance(org.apache.ambari.server.orm.dao.RepositoryVersionDAO.class);
        dao.remove(dao.findByDisplayName("2.2.0.0"));
        java.util.Map<java.lang.String, java.lang.String> info = java.util.Collections.singletonMap(org.apache.ambari.server.controller.spi.Request.DIRECTIVE_DRY_RUN, "true");
        final org.apache.ambari.server.controller.spi.Request createRequest = org.apache.ambari.server.controller.utilities.PropertyHelper.getCreateRequest(propertySet, info);
        try {
            versionProvider.createResources(createRequest);
            org.junit.Assert.fail("Expected exception creating a resource with no parent");
        } catch (java.lang.IllegalArgumentException expected) {
            org.junit.Assert.assertTrue(expected.getMessage().contains("there are no repositories for"));
        }
    }

    @org.junit.Test
    public void testCreatePatchManyParentVersionsNoneUsed() throws java.lang.Exception {
        org.springframework.security.core.Authentication authentication = org.apache.ambari.server.security.TestAuthenticationFactory.createAdministrator();
        org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(authentication);
        java.io.File file = new java.io.File("src/test/resources/version_definition_resource_provider.xml");
        final org.apache.ambari.server.controller.spi.ResourceProvider versionProvider = new org.apache.ambari.server.controller.internal.VersionDefinitionResourceProvider();
        final java.util.Set<java.util.Map<java.lang.String, java.lang.Object>> propertySet = new java.util.LinkedHashSet<>();
        final java.util.Map<java.lang.String, java.lang.Object> properties = new java.util.LinkedHashMap<>();
        properties.put(org.apache.ambari.server.controller.internal.VersionDefinitionResourceProvider.VERSION_DEF_DEFINITION_URL, file.toURI().toURL().toString());
        propertySet.add(properties);
        org.apache.ambari.server.orm.dao.RepositoryVersionDAO dao = injector.getInstance(org.apache.ambari.server.orm.dao.RepositoryVersionDAO.class);
        org.apache.ambari.server.orm.entities.RepositoryVersionEntity entity = new org.apache.ambari.server.orm.entities.RepositoryVersionEntity();
        entity.setStack(parentEntity.getStack());
        entity.setDisplayName("2.2.1.0");
        entity.setVersion("2.3.4.5-1234");
        dao.create(entity);
        java.util.Map<java.lang.String, java.lang.String> info = java.util.Collections.singletonMap(org.apache.ambari.server.controller.spi.Request.DIRECTIVE_DRY_RUN, "true");
        final org.apache.ambari.server.controller.spi.Request createRequest = org.apache.ambari.server.controller.utilities.PropertyHelper.getCreateRequest(propertySet, info);
        try {
            versionProvider.createResources(createRequest);
            org.junit.Assert.fail("Expected exception creating a resource with no parent");
        } catch (java.lang.IllegalArgumentException expected) {
            org.junit.Assert.assertTrue(expected.getMessage().contains("Could not determine which version"));
        }
    }

    @org.junit.Test
    public void testCreatePatchManyParentVersionsOneUsed() throws java.lang.Exception {
        org.springframework.security.core.Authentication authentication = org.apache.ambari.server.security.TestAuthenticationFactory.createAdministrator();
        org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(authentication);
        java.io.File file = new java.io.File("src/test/resources/version_definition_resource_provider.xml");
        final org.apache.ambari.server.controller.spi.ResourceProvider versionProvider = new org.apache.ambari.server.controller.internal.VersionDefinitionResourceProvider();
        final java.util.Set<java.util.Map<java.lang.String, java.lang.Object>> propertySet = new java.util.LinkedHashSet<>();
        final java.util.Map<java.lang.String, java.lang.Object> properties = new java.util.LinkedHashMap<>();
        properties.put(org.apache.ambari.server.controller.internal.VersionDefinitionResourceProvider.VERSION_DEF_DEFINITION_URL, file.toURI().toURL().toString());
        propertySet.add(properties);
        org.apache.ambari.server.orm.dao.RepositoryVersionDAO dao = injector.getInstance(org.apache.ambari.server.orm.dao.RepositoryVersionDAO.class);
        org.apache.ambari.server.orm.entities.RepositoryVersionEntity entity = new org.apache.ambari.server.orm.entities.RepositoryVersionEntity();
        entity.setStack(parentEntity.getStack());
        entity.setDisplayName("2.2.1.0");
        entity.setVersion("2.3.4.5-1234");
        dao.create(entity);
        makeService("c1", "HDFS", parentEntity);
        makeService("c1", "ZOOKEEPER", parentEntity);
        java.util.Map<java.lang.String, java.lang.String> info = java.util.Collections.singletonMap(org.apache.ambari.server.controller.spi.Request.DIRECTIVE_DRY_RUN, "true");
        final org.apache.ambari.server.controller.spi.Request createRequest = org.apache.ambari.server.controller.utilities.PropertyHelper.getCreateRequest(propertySet, info);
        try {
            versionProvider.createResources(createRequest);
        } catch (java.lang.IllegalArgumentException unexpected) {
        }
    }

    @org.junit.Test
    public void testCreatePatchManyParentVersionsManyUsed() throws java.lang.Exception {
        org.springframework.security.core.Authentication authentication = org.apache.ambari.server.security.TestAuthenticationFactory.createAdministrator();
        org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(authentication);
        java.io.File file = new java.io.File("src/test/resources/version_definition_resource_provider.xml");
        final org.apache.ambari.server.controller.spi.ResourceProvider versionProvider = new org.apache.ambari.server.controller.internal.VersionDefinitionResourceProvider();
        final java.util.Set<java.util.Map<java.lang.String, java.lang.Object>> propertySet = new java.util.LinkedHashSet<>();
        final java.util.Map<java.lang.String, java.lang.Object> properties = new java.util.LinkedHashMap<>();
        properties.put(org.apache.ambari.server.controller.internal.VersionDefinitionResourceProvider.VERSION_DEF_DEFINITION_URL, file.toURI().toURL().toString());
        propertySet.add(properties);
        org.apache.ambari.server.orm.dao.RepositoryVersionDAO dao = injector.getInstance(org.apache.ambari.server.orm.dao.RepositoryVersionDAO.class);
        org.apache.ambari.server.orm.entities.RepositoryVersionEntity entity = new org.apache.ambari.server.orm.entities.RepositoryVersionEntity();
        entity.setStack(parentEntity.getStack());
        entity.setDisplayName("2.2.1.0");
        entity.setVersion("2.3.4.5-1234");
        dao.create(entity);
        makeService("c1", "HDFS", parentEntity);
        makeService("c1", "ZOOKEEPER", entity);
        java.util.Map<java.lang.String, java.lang.String> info = java.util.Collections.singletonMap(org.apache.ambari.server.controller.spi.Request.DIRECTIVE_DRY_RUN, "true");
        final org.apache.ambari.server.controller.spi.Request createRequest = org.apache.ambari.server.controller.utilities.PropertyHelper.getCreateRequest(propertySet, info);
        try {
            versionProvider.createResources(createRequest);
            org.junit.Assert.fail("expected exception creating resources");
        } catch (java.lang.IllegalArgumentException expected) {
            org.junit.Assert.assertTrue(expected.getMessage().contains("Move all services to a common version and try again."));
        }
    }

    private void makeService(java.lang.String clusterName, java.lang.String serviceName, org.apache.ambari.server.orm.entities.RepositoryVersionEntity serviceRepo) throws java.lang.Exception {
        org.apache.ambari.server.state.Clusters clusters = injector.getInstance(org.apache.ambari.server.state.Clusters.class);
        org.apache.ambari.server.state.Cluster cluster;
        try {
            cluster = clusters.getCluster("c1");
        } catch (org.apache.ambari.server.AmbariException e) {
            clusters.addCluster("c1", parentEntity.getStackId());
            cluster = clusters.getCluster("c1");
        }
        cluster.addService(serviceName, serviceRepo);
    }
}