package org.apache.ambari.server.api.services;
import org.springframework.security.core.context.SecurityContextHolder;
import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.createNiceMock;
import static org.easymock.EasyMock.createStrictMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
public class PersistenceManagerImplTest {
    @org.junit.After
    public void clearAuthentication() {
        org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(null);
    }

    @org.junit.Test
    public void testPersistenceManagerImplAsClusterAdministrator() throws java.lang.Exception {
        org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(org.apache.ambari.server.security.TestAuthenticationFactory.createClusterAdministrator("ClusterAdmin", 2L));
        testCreate();
        testCreate___NoBodyProps();
        testCreate__MultipleResources();
        testUpdate();
        testDelete();
    }

    @org.junit.Test
    public void testPersistenceManagerImplAsServiceAdministrator() throws java.lang.Exception {
        org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(org.apache.ambari.server.security.TestAuthenticationFactory.createServiceAdministrator("ServiceAdmin", 2L));
        testCreate();
        testCreate___NoBodyProps();
        testCreate__MultipleResources();
        testUpdate();
        testDelete();
    }

    @org.junit.Test
    public void testPersistenceManagerImplAsServiceOperator() throws java.lang.Exception {
        org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(org.apache.ambari.server.security.TestAuthenticationFactory.createServiceOperator("ServiceOperator", 2L));
        testCreate();
        testCreate___NoBodyProps();
        testCreate__MultipleResources();
        testUpdate();
        testDelete();
    }

    @org.junit.Test
    public void testPersistenceManagerImplAsClusterUser() throws java.lang.Exception {
        org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(org.apache.ambari.server.security.TestAuthenticationFactory.createClusterUser("ClusterUser", 2L));
        testCreate();
        testCreate___NoBodyProps();
        testCreate__MultipleResources();
        testUpdate();
        testDelete();
    }

    @org.junit.Test(expected = org.apache.ambari.server.security.authorization.AuthorizationException.class)
    public void testPersistenceManagerImplAsViewUser() throws java.lang.Exception {
        org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(org.apache.ambari.server.security.TestAuthenticationFactory.createViewUser("ViewUser", 2L));
        testCreate();
        testCreate___NoBodyProps();
        testCreate__MultipleResources();
        testUpdate();
        testDelete();
    }

    public void testCreate() throws java.lang.Exception {
        org.apache.ambari.server.api.resources.ResourceInstance resource = EasyMock.createMock(org.apache.ambari.server.api.resources.ResourceInstance.class);
        org.apache.ambari.server.api.resources.ResourceDefinition resourceDefinition = EasyMock.createMock(org.apache.ambari.server.api.resources.ResourceDefinition.class);
        org.apache.ambari.server.controller.spi.ClusterController controller = EasyMock.createMock(org.apache.ambari.server.controller.spi.ClusterController.class);
        org.apache.ambari.server.controller.spi.Schema schema = EasyMock.createMock(org.apache.ambari.server.controller.spi.Schema.class);
        java.lang.String clusterId = "clusterId";
        java.lang.String serviceId = "serviceId";
        org.apache.ambari.server.controller.spi.Request serverRequest = EasyMock.createStrictMock(org.apache.ambari.server.controller.spi.Request.class);
        org.apache.ambari.server.api.services.RequestBody body = new org.apache.ambari.server.api.services.RequestBody();
        java.util.Map<org.apache.ambari.server.controller.spi.Resource.Type, java.lang.String> mapResourceIds = new java.util.HashMap<>();
        mapResourceIds.put(org.apache.ambari.server.controller.spi.Resource.Type.Cluster, "clusterId");
        mapResourceIds.put(org.apache.ambari.server.controller.spi.Resource.Type.Service, "serviceId");
        java.util.Map<java.lang.String, java.lang.Object> mapProperties = new java.util.HashMap<>();
        mapProperties.put("componentId", "id");
        mapProperties.put(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("foo", "bar"), "value");
        org.apache.ambari.server.api.services.NamedPropertySet namedPropSet = new org.apache.ambari.server.api.services.NamedPropertySet("", mapProperties);
        body.addPropertySet(namedPropSet);
        java.util.Set<java.util.Map<java.lang.String, java.lang.Object>> setExpected = new java.util.HashSet<>();
        java.util.Map<java.lang.String, java.lang.Object> mapExpected = new java.util.HashMap<>(mapProperties);
        mapExpected.put(clusterId, "clusterId");
        mapExpected.put(serviceId, "serviceId");
        setExpected.add(mapExpected);
        EasyMock.expect(resource.getKeyValueMap()).andReturn(mapResourceIds);
        EasyMock.expect(resource.getResourceDefinition()).andReturn(resourceDefinition).atLeastOnce();
        EasyMock.expect(resourceDefinition.getType()).andReturn(org.apache.ambari.server.controller.spi.Resource.Type.Component);
        EasyMock.expect(controller.getSchema(org.apache.ambari.server.controller.spi.Resource.Type.Component)).andReturn(schema);
        EasyMock.expect(schema.getKeyPropertyId(org.apache.ambari.server.controller.spi.Resource.Type.Cluster)).andReturn(clusterId);
        EasyMock.expect(schema.getKeyPropertyId(org.apache.ambari.server.controller.spi.Resource.Type.Service)).andReturn(serviceId);
        EasyMock.expect(controller.createResources(org.apache.ambari.server.controller.spi.Resource.Type.Component, serverRequest)).andReturn(new org.apache.ambari.server.controller.internal.RequestStatusImpl(null));
        EasyMock.replay(resource, resourceDefinition, controller, schema, serverRequest);
        new org.apache.ambari.server.api.services.PersistenceManagerImplTest.TestPersistenceManager(controller, setExpected, serverRequest).create(resource, body);
        EasyMock.verify(resource, resourceDefinition, controller, schema, serverRequest);
    }

    public void testCreate___NoBodyProps() throws java.lang.Exception {
        org.apache.ambari.server.api.resources.ResourceInstance resource = EasyMock.createMock(org.apache.ambari.server.api.resources.ResourceInstance.class);
        org.apache.ambari.server.api.resources.ResourceDefinition resourceDefinition = EasyMock.createMock(org.apache.ambari.server.api.resources.ResourceDefinition.class);
        org.apache.ambari.server.controller.spi.ClusterController controller = EasyMock.createMock(org.apache.ambari.server.controller.spi.ClusterController.class);
        org.apache.ambari.server.controller.spi.Schema schema = EasyMock.createMock(org.apache.ambari.server.controller.spi.Schema.class);
        java.lang.String clusterId = "clusterId";
        java.lang.String serviceId = "serviceId";
        java.lang.String componentId = "componentId";
        org.apache.ambari.server.controller.spi.Request serverRequest = EasyMock.createStrictMock(org.apache.ambari.server.controller.spi.Request.class);
        org.apache.ambari.server.api.services.RequestBody body = new org.apache.ambari.server.api.services.RequestBody();
        java.util.Map<org.apache.ambari.server.controller.spi.Resource.Type, java.lang.String> mapResourceIds = new java.util.HashMap<>();
        mapResourceIds.put(org.apache.ambari.server.controller.spi.Resource.Type.Cluster, "clusterId");
        mapResourceIds.put(org.apache.ambari.server.controller.spi.Resource.Type.Service, "serviceId");
        mapResourceIds.put(org.apache.ambari.server.controller.spi.Resource.Type.Component, "componentId");
        java.util.Set<java.util.Map<java.lang.String, java.lang.Object>> setExpected = new java.util.HashSet<>();
        java.util.Map<java.lang.String, java.lang.Object> mapExpected = new java.util.HashMap<>();
        mapExpected.put(clusterId, "clusterId");
        mapExpected.put(serviceId, "serviceId");
        mapExpected.put(componentId, "componentId");
        setExpected.add(mapExpected);
        EasyMock.expect(resource.getKeyValueMap()).andReturn(mapResourceIds);
        EasyMock.expect(resource.getResourceDefinition()).andReturn(resourceDefinition).atLeastOnce();
        EasyMock.expect(resourceDefinition.getType()).andReturn(org.apache.ambari.server.controller.spi.Resource.Type.Component);
        EasyMock.expect(controller.getSchema(org.apache.ambari.server.controller.spi.Resource.Type.Component)).andReturn(schema);
        EasyMock.expect(schema.getKeyPropertyId(org.apache.ambari.server.controller.spi.Resource.Type.Cluster)).andReturn(clusterId);
        EasyMock.expect(schema.getKeyPropertyId(org.apache.ambari.server.controller.spi.Resource.Type.Service)).andReturn(serviceId);
        EasyMock.expect(schema.getKeyPropertyId(org.apache.ambari.server.controller.spi.Resource.Type.Component)).andReturn(componentId);
        EasyMock.expect(controller.createResources(org.apache.ambari.server.controller.spi.Resource.Type.Component, serverRequest)).andReturn(new org.apache.ambari.server.controller.internal.RequestStatusImpl(null));
        EasyMock.replay(resource, resourceDefinition, controller, schema, serverRequest);
        new org.apache.ambari.server.api.services.PersistenceManagerImplTest.TestPersistenceManager(controller, setExpected, serverRequest).create(resource, body);
        EasyMock.verify(resource, resourceDefinition, controller, schema, serverRequest);
    }

    public void testCreate__MultipleResources() throws java.lang.Exception {
        org.apache.ambari.server.api.resources.ResourceInstance resource = EasyMock.createMock(org.apache.ambari.server.api.resources.ResourceInstance.class);
        org.apache.ambari.server.api.resources.ResourceDefinition resourceDefinition = EasyMock.createMock(org.apache.ambari.server.api.resources.ResourceDefinition.class);
        org.apache.ambari.server.controller.spi.ClusterController controller = EasyMock.createMock(org.apache.ambari.server.controller.spi.ClusterController.class);
        org.apache.ambari.server.controller.spi.Schema schema = EasyMock.createMock(org.apache.ambari.server.controller.spi.Schema.class);
        org.apache.ambari.server.controller.spi.Request serverRequest = EasyMock.createStrictMock(org.apache.ambari.server.controller.spi.Request.class);
        org.apache.ambari.server.api.services.RequestBody body = new org.apache.ambari.server.api.services.RequestBody();
        java.lang.String clusterId = "clusterId";
        java.lang.String serviceId = "serviceId";
        java.util.Map<org.apache.ambari.server.controller.spi.Resource.Type, java.lang.String> mapResourceIds = new java.util.HashMap<>();
        mapResourceIds.put(org.apache.ambari.server.controller.spi.Resource.Type.Cluster, "clusterId");
        mapResourceIds.put(org.apache.ambari.server.controller.spi.Resource.Type.Service, "serviceId");
        java.util.Map<java.lang.String, java.lang.Object> mapResourceProps1 = new java.util.HashMap<>();
        mapResourceProps1.put("componentId", "id1");
        mapResourceProps1.put(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("foo", "bar"), "value");
        java.util.Map<java.lang.String, java.lang.Object> mapResourceProps2 = new java.util.HashMap<>();
        mapResourceProps2.put("componentId", "id2");
        mapResourceProps2.put(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("foo", "bar2"), "value2");
        org.apache.ambari.server.api.services.NamedPropertySet namedPropSet1 = new org.apache.ambari.server.api.services.NamedPropertySet("", mapResourceProps1);
        org.apache.ambari.server.api.services.NamedPropertySet namedPropSet2 = new org.apache.ambari.server.api.services.NamedPropertySet("", mapResourceProps2);
        body.addPropertySet(namedPropSet1);
        body.addPropertySet(namedPropSet2);
        java.util.Set<java.util.Map<java.lang.String, java.lang.Object>> setExpected = new java.util.HashSet<>();
        java.util.Map<java.lang.String, java.lang.Object> mapExpected1 = new java.util.HashMap<>(mapResourceProps1);
        mapExpected1.put(clusterId, "clusterId");
        mapExpected1.put(serviceId, "serviceId");
        setExpected.add(mapExpected1);
        java.util.Map<java.lang.String, java.lang.Object> mapExpected2 = new java.util.HashMap<>(mapResourceProps2);
        mapExpected2.put(clusterId, "clusterId");
        mapExpected2.put(serviceId, "serviceId");
        setExpected.add(mapExpected2);
        EasyMock.expect(resource.getKeyValueMap()).andReturn(mapResourceIds);
        EasyMock.expect(resource.getResourceDefinition()).andReturn(resourceDefinition);
        EasyMock.expect(resourceDefinition.getType()).andReturn(org.apache.ambari.server.controller.spi.Resource.Type.Component);
        EasyMock.expect(controller.getSchema(org.apache.ambari.server.controller.spi.Resource.Type.Component)).andReturn(schema);
        EasyMock.expect(schema.getKeyPropertyId(org.apache.ambari.server.controller.spi.Resource.Type.Cluster)).andReturn(clusterId).times(2);
        EasyMock.expect(schema.getKeyPropertyId(org.apache.ambari.server.controller.spi.Resource.Type.Service)).andReturn(serviceId).times(2);
        EasyMock.expect(controller.createResources(org.apache.ambari.server.controller.spi.Resource.Type.Component, serverRequest)).andReturn(new org.apache.ambari.server.controller.internal.RequestStatusImpl(null));
        EasyMock.replay(resource, resourceDefinition, controller, schema, serverRequest);
        new org.apache.ambari.server.api.services.PersistenceManagerImplTest.TestPersistenceManager(controller, setExpected, serverRequest).create(resource, body);
        EasyMock.verify(resource, resourceDefinition, controller, schema, serverRequest);
    }

    public void testUpdate() throws java.lang.Exception {
        org.apache.ambari.server.api.resources.ResourceInstance resource = EasyMock.createMock(org.apache.ambari.server.api.resources.ResourceInstance.class);
        org.apache.ambari.server.api.resources.ResourceDefinition resourceDefinition = EasyMock.createMock(org.apache.ambari.server.api.resources.ResourceDefinition.class);
        org.apache.ambari.server.controller.spi.ClusterController controller = EasyMock.createMock(org.apache.ambari.server.controller.spi.ClusterController.class);
        org.apache.ambari.server.controller.spi.Schema schema = EasyMock.createMock(org.apache.ambari.server.controller.spi.Schema.class);
        org.apache.ambari.server.controller.spi.Request serverRequest = EasyMock.createStrictMock(org.apache.ambari.server.controller.spi.Request.class);
        org.apache.ambari.server.api.query.Query query = EasyMock.createMock(org.apache.ambari.server.api.query.Query.class);
        org.apache.ambari.server.controller.spi.Predicate predicate = EasyMock.createMock(org.apache.ambari.server.controller.spi.Predicate.class);
        org.apache.ambari.server.api.services.RequestBody body = new org.apache.ambari.server.api.services.RequestBody();
        java.lang.String clusterId = "clusterId";
        java.lang.String serviceId = null;
        java.util.Map<org.apache.ambari.server.controller.spi.Resource.Type, java.lang.String> mapResourceIds = new java.util.HashMap<>();
        mapResourceIds.put(org.apache.ambari.server.controller.spi.Resource.Type.Cluster, clusterId);
        mapResourceIds.put(org.apache.ambari.server.controller.spi.Resource.Type.Service, serviceId);
        java.util.Map<java.lang.String, java.lang.Object> mapProperties = new java.util.HashMap<>();
        mapProperties.put(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("foo", "bar"), "value");
        org.apache.ambari.server.api.services.NamedPropertySet namedPropSet = new org.apache.ambari.server.api.services.NamedPropertySet("", mapProperties);
        body.addPropertySet(namedPropSet);
        java.util.Set<java.util.Map<java.lang.String, java.lang.Object>> setExpected = new java.util.HashSet<>();
        setExpected.add(mapProperties);
        EasyMock.expect(resource.getKeyValueMap()).andReturn(mapResourceIds);
        EasyMock.expect(resource.getResourceDefinition()).andReturn(resourceDefinition).atLeastOnce();
        EasyMock.expect(controller.getSchema(org.apache.ambari.server.controller.spi.Resource.Type.Component)).andReturn(schema);
        EasyMock.expect(resourceDefinition.getType()).andReturn(org.apache.ambari.server.controller.spi.Resource.Type.Component);
        EasyMock.expect(resource.getQuery()).andReturn(query);
        EasyMock.expect(query.getPredicate()).andReturn(predicate);
        EasyMock.expect(schema.getKeyPropertyId(org.apache.ambari.server.controller.spi.Resource.Type.Cluster)).andReturn(clusterId);
        EasyMock.expect(controller.updateResources(org.apache.ambari.server.controller.spi.Resource.Type.Component, serverRequest, predicate)).andReturn(new org.apache.ambari.server.controller.internal.RequestStatusImpl(null));
        EasyMock.replay(resource, resourceDefinition, controller, schema, serverRequest, query, predicate);
        new org.apache.ambari.server.api.services.PersistenceManagerImplTest.TestPersistenceManager(controller, setExpected, serverRequest).update(resource, body);
        EasyMock.verify(resource, resourceDefinition, controller, schema, serverRequest, query, predicate);
    }

    public void testDelete() throws java.lang.Exception {
        org.apache.ambari.server.api.resources.ResourceInstance resource = EasyMock.createNiceMock(org.apache.ambari.server.api.resources.ResourceInstance.class);
        org.apache.ambari.server.api.resources.ResourceDefinition resourceDefinition = EasyMock.createNiceMock(org.apache.ambari.server.api.resources.ResourceDefinition.class);
        org.apache.ambari.server.controller.spi.ClusterController controller = EasyMock.createMock(org.apache.ambari.server.controller.spi.ClusterController.class);
        org.apache.ambari.server.api.query.Query query = EasyMock.createMock(org.apache.ambari.server.api.query.Query.class);
        org.apache.ambari.server.controller.spi.Predicate predicate = EasyMock.createMock(org.apache.ambari.server.controller.spi.Predicate.class);
        org.apache.ambari.server.api.services.RequestBody body = new org.apache.ambari.server.api.services.RequestBody();
        EasyMock.expect(resource.getResourceDefinition()).andReturn(resourceDefinition).anyTimes();
        EasyMock.expect(resourceDefinition.getType()).andReturn(org.apache.ambari.server.controller.spi.Resource.Type.Component).anyTimes();
        EasyMock.expect(resource.getQuery()).andReturn(query).anyTimes();
        EasyMock.expect(query.getPredicate()).andReturn(predicate).anyTimes();
        EasyMock.expect(controller.deleteResources(org.apache.ambari.server.controller.spi.Resource.Type.Component, new org.apache.ambari.server.controller.internal.RequestImpl(null, null, null, null), predicate)).andReturn(new org.apache.ambari.server.controller.internal.RequestStatusImpl(null));
        EasyMock.replay(resource, resourceDefinition, controller, query, predicate);
        new org.apache.ambari.server.api.services.persistence.PersistenceManagerImpl(controller).delete(resource, body);
        EasyMock.verify(resource, resourceDefinition, controller, query, predicate);
    }

    private class TestPersistenceManager extends org.apache.ambari.server.api.services.persistence.PersistenceManagerImpl {
        private org.apache.ambari.server.controller.spi.Request m_request;

        private java.util.Set<java.util.Map<java.lang.String, java.lang.Object>> m_setProperties;

        private TestPersistenceManager(org.apache.ambari.server.controller.spi.ClusterController controller, java.util.Set<java.util.Map<java.lang.String, java.lang.Object>> setProperties, org.apache.ambari.server.controller.spi.Request controllerRequest) {
            super(controller);
            m_setProperties = setProperties;
            m_request = controllerRequest;
        }

        @java.lang.Override
        protected org.apache.ambari.server.controller.spi.Request createControllerRequest(org.apache.ambari.server.api.services.RequestBody body) {
            org.junit.Assert.assertEquals(m_setProperties, body.getPropertySets());
            return m_request;
        }
    }
}