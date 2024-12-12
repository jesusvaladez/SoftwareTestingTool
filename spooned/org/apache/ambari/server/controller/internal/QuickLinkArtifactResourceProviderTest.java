package org.apache.ambari.server.controller.internal;
import static org.easymock.EasyMock.anyString;
import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
public class QuickLinkArtifactResourceProviderTest {
    private com.google.inject.Injector injector;

    java.lang.String quicklinkProfile;

    @org.junit.Before
    public void before() {
        injector = com.google.inject.Guice.createInjector(new org.apache.ambari.server.controller.internal.QuickLinkArtifactResourceProviderTest.MockModule());
    }

    private org.apache.ambari.server.controller.internal.QuickLinkArtifactResourceProvider createProvider() {
        return new org.apache.ambari.server.controller.internal.QuickLinkArtifactResourceProvider(injector.getInstance(org.apache.ambari.server.controller.AmbariManagementController.class));
    }

    @org.junit.Test
    public void getResourcesRespectsVisibility() throws java.lang.Exception {
        quicklinkProfile = com.google.common.io.Resources.toString(com.google.common.io.Resources.getResource("example_quicklinks_profile.json"), com.google.common.base.Charsets.UTF_8);
        org.apache.ambari.server.controller.internal.QuickLinkArtifactResourceProvider provider = createProvider();
        org.apache.ambari.server.controller.spi.Predicate predicate = new org.apache.ambari.server.controller.utilities.PredicateBuilder().property(org.apache.ambari.server.controller.internal.QuickLinkArtifactResourceProvider.STACK_NAME_PROPERTY_ID).equals("HDP").and().property(org.apache.ambari.server.controller.internal.QuickLinkArtifactResourceProvider.STACK_VERSION_PROPERTY_ID).equals("2.0.6").and().property(org.apache.ambari.server.controller.internal.QuickLinkArtifactResourceProvider.STACK_SERVICE_NAME_PROPERTY_ID).equals("YARN").toPredicate();
        java.util.Set<org.apache.ambari.server.controller.spi.Resource> resources = provider.getResources(org.apache.ambari.server.controller.utilities.PropertyHelper.getReadRequest(com.google.common.collect.Sets.newHashSet()), predicate);
        java.util.Map<java.lang.String, org.apache.ambari.server.state.quicklinks.Link> linkMap = getLinks(resources);
        for (java.util.Map.Entry<java.lang.String, org.apache.ambari.server.state.quicklinks.Link> entry : linkMap.entrySet()) {
            org.junit.Assert.assertTrue("Only resourcemanager_ui should be visible.", entry.getValue().isVisible() == entry.getKey().equals("resourcemanager_ui"));
        }
    }

    @org.junit.Test
    public void getResourcesWithUrlOverride() throws java.lang.Exception {
        quicklinkProfile = com.google.common.io.Resources.toString(com.google.common.io.Resources.getResource("example_quicklinks_profile.json"), com.google.common.base.Charsets.UTF_8);
        org.apache.ambari.server.controller.internal.QuickLinkArtifactResourceProvider provider = createProvider();
        org.apache.ambari.server.controller.spi.Predicate predicate = new org.apache.ambari.server.controller.utilities.PredicateBuilder().property(org.apache.ambari.server.controller.internal.QuickLinkArtifactResourceProvider.STACK_NAME_PROPERTY_ID).equals("HDP").and().property(org.apache.ambari.server.controller.internal.QuickLinkArtifactResourceProvider.STACK_VERSION_PROPERTY_ID).equals("2.0.6").and().property(org.apache.ambari.server.controller.internal.QuickLinkArtifactResourceProvider.STACK_SERVICE_NAME_PROPERTY_ID).equals("YARN").toPredicate();
        java.util.Set<org.apache.ambari.server.controller.spi.Resource> resources = provider.getResources(org.apache.ambari.server.controller.utilities.PropertyHelper.getReadRequest(com.google.common.collect.Sets.newHashSet()), predicate);
        java.util.Map<java.lang.String, org.apache.ambari.server.state.quicklinks.Link> linkMap = getLinks(resources);
        org.junit.Assert.assertEquals("http://customlink.org/resourcemanager", linkMap.get("resourcemanager_ui").getUrl());
    }

    @org.junit.Test
    public void whenNoProfileIsSetAllLinksAreVisible() throws java.lang.Exception {
        quicklinkProfile = null;
        org.apache.ambari.server.controller.internal.QuickLinkArtifactResourceProvider provider = createProvider();
        org.apache.ambari.server.controller.spi.Predicate predicate = new org.apache.ambari.server.controller.utilities.PredicateBuilder().property(org.apache.ambari.server.controller.internal.QuickLinkArtifactResourceProvider.STACK_NAME_PROPERTY_ID).equals("HDP").and().property(org.apache.ambari.server.controller.internal.QuickLinkArtifactResourceProvider.STACK_VERSION_PROPERTY_ID).equals("2.0.6").and().property(org.apache.ambari.server.controller.internal.QuickLinkArtifactResourceProvider.STACK_SERVICE_NAME_PROPERTY_ID).equals("YARN").toPredicate();
        java.util.Set<org.apache.ambari.server.controller.spi.Resource> resources = provider.getResources(org.apache.ambari.server.controller.utilities.PropertyHelper.getReadRequest(com.google.common.collect.Sets.newHashSet()), predicate);
        java.util.Map<java.lang.String, org.apache.ambari.server.state.quicklinks.Link> linkMap = getLinks(resources);
        for (org.apache.ambari.server.state.quicklinks.Link link : linkMap.values()) {
            org.junit.Assert.assertTrue("All links should be visible.", link.isVisible());
        }
    }

    @org.junit.Test
    public void whenInvalidProfileIsSetAllLinksAreVisible() throws java.lang.Exception {
        quicklinkProfile = "{}";
        org.apache.ambari.server.controller.internal.QuickLinkArtifactResourceProvider provider = createProvider();
        org.apache.ambari.server.controller.spi.Predicate predicate = new org.apache.ambari.server.controller.utilities.PredicateBuilder().property(org.apache.ambari.server.controller.internal.QuickLinkArtifactResourceProvider.STACK_NAME_PROPERTY_ID).equals("HDP").and().property(org.apache.ambari.server.controller.internal.QuickLinkArtifactResourceProvider.STACK_VERSION_PROPERTY_ID).equals("2.0.6").and().property(org.apache.ambari.server.controller.internal.QuickLinkArtifactResourceProvider.STACK_SERVICE_NAME_PROPERTY_ID).equals("YARN").toPredicate();
        java.util.Set<org.apache.ambari.server.controller.spi.Resource> resources = provider.getResources(org.apache.ambari.server.controller.utilities.PropertyHelper.getReadRequest(com.google.common.collect.Sets.newHashSet()), predicate);
        java.util.Map<java.lang.String, org.apache.ambari.server.state.quicklinks.Link> linkMap = getLinks(resources);
        for (org.apache.ambari.server.state.quicklinks.Link link : linkMap.values()) {
            org.junit.Assert.assertTrue("All links should be visible.", link.isVisible());
        }
    }

    private java.util.Map<java.lang.String, org.apache.ambari.server.state.quicklinks.Link> getLinks(java.util.Set<org.apache.ambari.server.controller.spi.Resource> resources) {
        org.apache.ambari.server.state.quicklinks.QuickLinks quickLinks = ((org.apache.ambari.server.state.quicklinks.QuickLinks) (resources.iterator().next().getPropertiesMap().get("QuickLinkInfo/quicklink_data").values().iterator().next()));
        java.util.Map<java.lang.String, org.apache.ambari.server.state.quicklinks.Link> linksMap = new java.util.HashMap<>();
        for (org.apache.ambari.server.state.quicklinks.Link link : quickLinks.getQuickLinksConfiguration().getLinks()) {
            linksMap.put(link.getName(), link);
        }
        return linksMap;
    }

    private class MockModule implements com.google.inject.Module {
        @java.lang.Override
        public void configure(com.google.inject.Binder binder) {
            org.apache.ambari.server.api.services.AmbariMetaInfo metaInfo = EasyMock.createMock(org.apache.ambari.server.api.services.AmbariMetaInfo.class);
            org.apache.ambari.server.state.StackInfo stack = EasyMock.createMock(org.apache.ambari.server.state.StackInfo.class);
            org.apache.ambari.server.state.ServiceInfo service = EasyMock.createMock(org.apache.ambari.server.state.ServiceInfo.class);
            org.apache.ambari.server.state.QuickLinksConfigurationInfo qlConfigInfo = new org.apache.ambari.server.state.QuickLinksConfigurationInfo();
            qlConfigInfo.setDeleted(false);
            qlConfigInfo.setFileName("parent_quicklinks.json");
            qlConfigInfo.setIsDefault(true);
            java.io.File qlFile = new java.io.File(com.google.common.io.Resources.getResource("parent_quicklinks.json").getFile());
            org.apache.ambari.server.stack.QuickLinksConfigurationModule module = new org.apache.ambari.server.stack.QuickLinksConfigurationModule(qlFile, qlConfigInfo);
            module.getModuleInfo();
            org.apache.ambari.server.controller.AmbariManagementController amc = EasyMock.createMock(org.apache.ambari.server.controller.AmbariManagementController.class);
            EasyMock.expect(amc.getAmbariMetaInfo()).andReturn(metaInfo).anyTimes();
            EasyMock.expect(amc.getQuicklinkVisibilityController()).andAnswer(() -> org.apache.ambari.server.state.quicklinksprofile.QuickLinkVisibilityControllerFactory.get(org.apache.ambari.server.controller.internal.quicklinkProfile)).anyTimes();
            try {
                EasyMock.expect(metaInfo.getStack(EasyMock.anyString(), EasyMock.anyString())).andReturn(stack).anyTimes();
            } catch (org.apache.ambari.server.AmbariException ex) {
                throw new java.lang.RuntimeException(ex);
            }
            EasyMock.expect(stack.getServices()).andReturn(com.google.common.collect.ImmutableList.of(service)).anyTimes();
            EasyMock.expect(stack.getService("YARN")).andReturn(service).anyTimes();
            EasyMock.expect(service.getQuickLinksConfigurationsMap()).andReturn(com.google.common.collect.ImmutableMap.of("YARN", qlConfigInfo));
            EasyMock.expect(service.getName()).andReturn("YARN").anyTimes();
            binder.bind(org.apache.ambari.server.controller.AmbariManagementController.class).toInstance(amc);
            EasyMock.replay(amc, metaInfo, stack, service);
        }
    }
}