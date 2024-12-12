package org.apache.ambari.server.api.resources;
import static org.easymock.EasyMock.anyObject;
import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.createNiceMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
public class BaseResourceDefinitionTest {
    @org.junit.Before
    public void before() {
        org.apache.ambari.server.events.publishers.AmbariEventPublisher publisher = EasyMock.createNiceMock(org.apache.ambari.server.events.publishers.AmbariEventPublisher.class);
        EasyMock.replay(publisher);
        org.apache.ambari.server.view.ViewRegistry.initInstance(new org.apache.ambari.server.view.ViewRegistry(publisher));
    }

    @org.junit.Test
    public void testGetPostProcessors() throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.server.api.resources.BaseResourceDefinition resourceDefinition = getResourceDefinition();
        java.util.List<org.apache.ambari.server.api.resources.ResourceDefinition.PostProcessor> postProcessors = resourceDefinition.getPostProcessors();
        org.junit.Assert.assertEquals(1, postProcessors.size());
        org.apache.ambari.server.api.resources.ResourceDefinition.PostProcessor processor = postProcessors.iterator().next();
        org.apache.ambari.server.controller.spi.Resource service = new org.apache.ambari.server.controller.internal.ResourceImpl(org.apache.ambari.server.controller.spi.Resource.Type.Service);
        service.setProperty("ServiceInfo/service_name", "Service1");
        org.apache.ambari.server.api.util.TreeNode<org.apache.ambari.server.controller.spi.Resource> parentNode = new org.apache.ambari.server.api.util.TreeNodeImpl<>(null, null, "services");
        org.apache.ambari.server.api.util.TreeNode<org.apache.ambari.server.controller.spi.Resource> serviceNode = new org.apache.ambari.server.api.util.TreeNodeImpl<>(parentNode, service, "service1");
        parentNode.setProperty("isCollection", "true");
        org.apache.ambari.server.controller.ResourceProviderFactory factory = EasyMock.createMock(org.apache.ambari.server.controller.ResourceProviderFactory.class);
        org.apache.ambari.server.controller.MaintenanceStateHelper maintenanceStateHelper = EasyMock.createNiceMock(org.apache.ambari.server.controller.MaintenanceStateHelper.class);
        org.apache.ambari.server.controller.AmbariManagementController managementController = EasyMock.createMock(org.apache.ambari.server.controller.AmbariManagementController.class);
        org.apache.ambari.server.orm.dao.RepositoryVersionDAO repositoryVersionDAO = EasyMock.createNiceMock(org.apache.ambari.server.orm.dao.RepositoryVersionDAO.class);
        EasyMock.expect(maintenanceStateHelper.isOperationAllowed(EasyMock.anyObject(org.apache.ambari.server.controller.spi.Resource.Type.class), EasyMock.anyObject(org.apache.ambari.server.state.Service.class))).andReturn(true).anyTimes();
        org.apache.ambari.server.controller.spi.ResourceProvider serviceResourceProvider = new org.apache.ambari.server.controller.internal.ServiceResourceProvider(managementController, maintenanceStateHelper, repositoryVersionDAO);
        EasyMock.expect(factory.getServiceResourceProvider(EasyMock.anyObject(org.apache.ambari.server.controller.AmbariManagementController.class))).andReturn(serviceResourceProvider);
        org.apache.ambari.server.controller.internal.AbstractControllerResourceProvider.init(factory);
        EasyMock.replay(factory, managementController, maintenanceStateHelper);
        processor.process(null, serviceNode, "http://c6401.ambari.apache.org:8080/api/v1/clusters/c1/services");
        java.lang.String href = serviceNode.getStringProperty("href");
        org.junit.Assert.assertEquals("http://c6401.ambari.apache.org:8080/api/v1/clusters/c1/services/Service1", href);
        org.apache.ambari.server.controller.spi.Resource configGroup = new org.apache.ambari.server.controller.internal.ResourceImpl(org.apache.ambari.server.controller.spi.Resource.Type.ConfigGroup);
        configGroup.setProperty("ConfigGroup/id", "2");
        org.apache.ambari.server.api.util.TreeNode<org.apache.ambari.server.controller.spi.Resource> resourcesNode = new org.apache.ambari.server.api.util.TreeNodeImpl<>(null, null, org.apache.ambari.server.api.handlers.BaseManagementHandler.RESOURCES_NODE_NAME);
        org.apache.ambari.server.api.util.TreeNode<org.apache.ambari.server.controller.spi.Resource> configGroupNode = new org.apache.ambari.server.api.util.TreeNodeImpl<>(resourcesNode, configGroup, "configGroup1");
        resourcesNode.setProperty("isCollection", "true");
        processor.process(null, configGroupNode, "http://c6401.ambari.apache.org:8080/api/v1/clusters/c1/config_groups");
        href = configGroupNode.getStringProperty("href");
        org.junit.Assert.assertEquals("http://c6401.ambari.apache.org:8080/api/v1/clusters/c1/config_groups/2", href);
    }

    @org.junit.Test
    public void testGetRenderer() {
        org.apache.ambari.server.api.resources.ResourceDefinition resource = getResourceDefinition();
        org.junit.Assert.assertTrue(resource.getRenderer(null) instanceof org.apache.ambari.server.api.query.render.DefaultRenderer);
        org.junit.Assert.assertTrue(resource.getRenderer("default") instanceof org.apache.ambari.server.api.query.render.DefaultRenderer);
        org.junit.Assert.assertTrue(resource.getRenderer("minimal") instanceof org.apache.ambari.server.api.query.render.MinimalRenderer);
        try {
            resource.getRenderer("foo");
            org.junit.Assert.fail("Should have thrown an exception due to invalid renderer type");
        } catch (java.lang.IllegalArgumentException e) {
            org.junit.Assert.assertEquals("Invalid renderer name for resource of type Service", e.getMessage());
        }
    }

    @org.junit.Test
    public void testReadDirectives() {
        org.apache.ambari.server.api.resources.ResourceDefinition resource = getResourceDefinition();
        org.junit.Assert.assertEquals(java.util.Collections.emptySet(), resource.getReadDirectives());
        java.util.Map<org.apache.ambari.server.api.resources.BaseResourceDefinition.DirectiveType, java.util.List<java.lang.String>> directives = new java.util.HashMap<>();
        directives.put(org.apache.ambari.server.api.resources.BaseResourceDefinition.DirectiveType.DELETE, java.util.Arrays.asList("do_something_delete", "do_something_else_delete"));
        directives.put(org.apache.ambari.server.api.resources.BaseResourceDefinition.DirectiveType.READ, java.util.Arrays.asList("do_something_get", "do_something_else_get"));
        directives.put(org.apache.ambari.server.api.resources.BaseResourceDefinition.DirectiveType.CREATE, java.util.Arrays.asList("do_something_post", "do_something_else_post"));
        directives.put(org.apache.ambari.server.api.resources.BaseResourceDefinition.DirectiveType.UPDATE, java.util.Arrays.asList("do_something_put", "do_something_else_put"));
        resource = getResourceDefinition(directives);
        org.junit.Assert.assertEquals(new java.util.HashSet<java.lang.String>() {
            {
                add("do_something_delete");
                add("do_something_else_delete");
            }
        }, resource.getDeleteDirectives());
        org.junit.Assert.assertEquals(new java.util.HashSet<java.lang.String>() {
            {
                add("do_something_get");
                add("do_something_else_get");
            }
        }, resource.getReadDirectives());
        org.junit.Assert.assertEquals(new java.util.HashSet<java.lang.String>() {
            {
                add("do_something_post");
                add("do_something_else_post");
            }
        }, resource.getCreateDirectives());
        org.junit.Assert.assertEquals(new java.util.HashSet<java.lang.String>() {
            {
                add("do_something_put");
                add("do_something_else_put");
            }
        }, resource.getUpdateDirectives());
    }

    private org.apache.ambari.server.api.resources.BaseResourceDefinition getResourceDefinition() {
        return new org.apache.ambari.server.api.resources.BaseResourceDefinition(org.apache.ambari.server.controller.spi.Resource.Type.Service) {
            @java.lang.Override
            public java.lang.String getPluralName() {
                return "pluralName";
            }

            @java.lang.Override
            public java.lang.String getSingularName() {
                return "singularName";
            }
        };
    }

    private org.apache.ambari.server.api.resources.BaseResourceDefinition getResourceDefinition(java.util.Map<org.apache.ambari.server.api.resources.BaseResourceDefinition.DirectiveType, ? extends java.util.Collection<java.lang.String>> directives) {
        return new org.apache.ambari.server.api.resources.BaseResourceDefinition(org.apache.ambari.server.controller.spi.Resource.Type.Service, java.util.Collections.emptySet(), directives) {
            @java.lang.Override
            public java.lang.String getPluralName() {
                return "pluralName";
            }

            @java.lang.Override
            public java.lang.String getSingularName() {
                return "singularName";
            }
        };
    }
}