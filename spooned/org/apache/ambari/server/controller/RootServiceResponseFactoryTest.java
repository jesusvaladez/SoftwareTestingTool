package org.apache.ambari.server.controller;
import static org.apache.ambari.server.controller.RootComponent.AMBARI_SERVER;
import static org.apache.ambari.server.controller.RootService.AMBARI;
public class RootServiceResponseFactoryTest {
    private com.google.inject.Injector injector;

    @com.google.inject.Inject
    private org.apache.ambari.server.controller.RootServiceResponseFactory responseFactory;

    @com.google.inject.Inject
    private org.apache.ambari.server.api.services.AmbariMetaInfo ambariMetaInfo;

    @com.google.inject.Inject
    private org.apache.ambari.server.configuration.Configuration config;

    @org.junit.Before
    public void setUp() throws java.lang.Exception {
        injector = com.google.inject.Guice.createInjector(new org.apache.ambari.server.orm.InMemoryDefaultTestModule());
        injector.getInstance(org.apache.ambari.server.orm.GuiceJpaInitializer.class);
        injector.injectMembers(this);
    }

    @org.junit.After
    public void teardown() throws org.apache.ambari.server.AmbariException, java.sql.SQLException {
        org.apache.ambari.server.H2DatabaseCleaner.clearDatabaseAndStopPersistenceService(injector);
    }

    @org.junit.Test
    public void getReturnsAllServicesForNullServiceName() throws java.lang.Exception {
        org.apache.ambari.server.controller.RootServiceRequest request = new org.apache.ambari.server.controller.RootServiceRequest(null);
        java.util.Set<org.apache.ambari.server.controller.RootServiceResponse> rootServices = responseFactory.getRootServices(request);
        junit.framework.Assert.assertEquals(org.apache.ambari.server.controller.RootService.values().length, rootServices.size());
    }

    @org.junit.Test
    public void getReturnsAllServicesForNullRequest() throws java.lang.Exception {
        java.util.Set<org.apache.ambari.server.controller.RootServiceResponse> rootServices = responseFactory.getRootServices(null);
        junit.framework.Assert.assertEquals(org.apache.ambari.server.controller.RootService.values().length, rootServices.size());
    }

    @org.junit.Test(expected = org.apache.ambari.server.ObjectNotFoundException.class)
    public void getThrowsForNonExistentService() throws java.lang.Exception {
        org.apache.ambari.server.controller.RootServiceRequest request = new org.apache.ambari.server.controller.RootServiceRequest("XXX");
        responseFactory.getRootServices(request);
    }

    @org.junit.Test
    public void getReturnsSingleServiceForValidServiceName() throws java.lang.Exception {
        org.apache.ambari.server.controller.RootServiceRequest request = new org.apache.ambari.server.controller.RootServiceRequest(org.apache.ambari.server.controller.RootService.AMBARI.name());
        java.util.Set<org.apache.ambari.server.controller.RootServiceResponse> rootServices = responseFactory.getRootServices(request);
        junit.framework.Assert.assertEquals(java.util.Collections.singleton(new org.apache.ambari.server.controller.RootServiceResponse(org.apache.ambari.server.controller.RootService.AMBARI.name())), rootServices);
    }

    @org.junit.Test(expected = org.apache.ambari.server.ObjectNotFoundException.class)
    public void getThrowsForNullServiceNameNullComponentName() throws java.lang.Exception {
        org.apache.ambari.server.controller.RootServiceComponentRequest request = new org.apache.ambari.server.controller.RootServiceComponentRequest(null, null);
        responseFactory.getRootServiceComponents(request);
    }

    @org.junit.Test(expected = org.apache.ambari.server.ObjectNotFoundException.class)
    public void getThrowsForNullServiceNameValidComponentName() throws java.lang.Exception {
        org.apache.ambari.server.controller.RootServiceComponentRequest request = new org.apache.ambari.server.controller.RootServiceComponentRequest(null, org.apache.ambari.server.controller.RootComponent.AMBARI_SERVER.name());
        responseFactory.getRootServiceComponents(request);
    }

    @org.junit.Test
    public void getReturnsAllComponentsForValidServiceNameNullComponentName() throws java.lang.Exception {
        org.apache.ambari.server.controller.RootServiceComponentRequest request = new org.apache.ambari.server.controller.RootServiceComponentRequest(org.apache.ambari.server.controller.RootService.AMBARI.name(), null);
        java.util.Set<org.apache.ambari.server.controller.RootServiceComponentResponse> rootServiceComponents = responseFactory.getRootServiceComponents(request);
        junit.framework.Assert.assertEquals(org.apache.ambari.server.controller.RootService.AMBARI.getComponents().length, rootServiceComponents.size());
        for (int i = 0; i < org.apache.ambari.server.controller.RootService.AMBARI.getComponents().length; i++) {
            org.apache.ambari.server.controller.RootComponent component = org.apache.ambari.server.controller.RootService.AMBARI.getComponents()[i];
            if (component.name().equals(org.apache.ambari.server.controller.RootComponent.AMBARI_SERVER.name())) {
                for (org.apache.ambari.server.controller.RootServiceComponentResponse response : rootServiceComponents) {
                    if (response.getComponentName().equals(org.apache.ambari.server.controller.RootComponent.AMBARI_SERVER.name())) {
                        verifyResponseForAmbariServer(response);
                    }
                }
            } else {
                junit.framework.Assert.assertTrue(rootServiceComponents.contains(new org.apache.ambari.server.controller.RootServiceComponentResponse(org.apache.ambari.server.controller.RootService.AMBARI.name(), component.name(), org.apache.ambari.server.controller.RootServiceResponseFactory.NOT_APPLICABLE, java.util.Collections.emptyMap())));
            }
        }
    }

    @org.junit.Test
    public void getReturnsSingleComponentForValidServiceAndComponentName() throws java.lang.Exception {
        org.apache.ambari.server.controller.RootServiceComponentRequest request = new org.apache.ambari.server.controller.RootServiceComponentRequest(org.apache.ambari.server.controller.RootService.AMBARI.name(), org.apache.ambari.server.controller.RootComponent.AMBARI_SERVER.name());
        java.util.Set<org.apache.ambari.server.controller.RootServiceComponentResponse> rootServiceComponents = responseFactory.getRootServiceComponents(request);
        junit.framework.Assert.assertEquals(1, rootServiceComponents.size());
        for (org.apache.ambari.server.controller.RootServiceComponentResponse response : rootServiceComponents) {
            verifyResponseForAmbariServer(response);
        }
    }

    @org.junit.Test(expected = org.apache.ambari.server.ObjectNotFoundException.class)
    public void getThrowsForNonexistentComponent() throws java.lang.Exception {
        org.apache.ambari.server.controller.RootServiceComponentRequest request = new org.apache.ambari.server.controller.RootServiceComponentRequest(org.apache.ambari.server.controller.RootService.AMBARI.name(), "XXX");
        responseFactory.getRootServiceComponents(request);
    }

    private void verifyResponseForAmbariServer(org.apache.ambari.server.controller.RootServiceComponentResponse response) {
        junit.framework.Assert.assertEquals(ambariMetaInfo.getServerVersion(), response.getComponentVersion());
        int expectedPropertyCount = config.getAmbariProperties().size() + 2;
        junit.framework.Assert.assertEquals(response.getProperties().toString(), expectedPropertyCount, response.getProperties().size());
        junit.framework.Assert.assertTrue(response.getProperties().containsKey("jdk_location"));
        junit.framework.Assert.assertTrue(response.getProperties().containsKey("java.version"));
    }
}