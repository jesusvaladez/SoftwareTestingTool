package org.apache.ambari.server.controller.internal;
import org.easymock.Capture;
import org.easymock.EasyMock;
import org.powermock.api.easymock.PowerMock;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import static org.easymock.EasyMock.anyObject;
import static org.easymock.EasyMock.capture;
import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.createStrictMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.resetToStrict;
import static org.easymock.EasyMock.verify;
public class WidgetResourceProviderTest {
    private org.apache.ambari.server.orm.dao.WidgetDAO dao = null;

    private com.google.inject.Injector m_injector;

    @org.junit.Before
    public void before() {
        org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(org.apache.ambari.server.security.TestAuthenticationFactory.createAdministrator());
        dao = EasyMock.createStrictMock(org.apache.ambari.server.orm.dao.WidgetDAO.class);
        m_injector = com.google.inject.Guice.createInjector(com.google.inject.util.Modules.override(new org.apache.ambari.server.orm.InMemoryDefaultTestModule()).with(new org.apache.ambari.server.controller.internal.WidgetResourceProviderTest.MockModule()));
    }

    @org.junit.Test
    public void testGetResourcesNoPredicate() throws java.lang.Exception {
        org.apache.ambari.server.controller.internal.WidgetResourceProvider provider = createProvider(null);
        org.apache.ambari.server.controller.spi.Request request = org.apache.ambari.server.controller.utilities.PropertyHelper.getReadRequest("Widgets/id");
        java.util.Set<org.apache.ambari.server.controller.spi.Resource> results = provider.getResources(request, null);
        org.junit.Assert.assertEquals(0, results.size());
    }

    @org.junit.Test
    public void testGetSingleResource() throws java.lang.Exception {
        org.apache.ambari.server.controller.spi.Request request = org.apache.ambari.server.controller.utilities.PropertyHelper.getReadRequest(org.apache.ambari.server.controller.internal.WidgetResourceProvider.WIDGET_ID_PROPERTY_ID, org.apache.ambari.server.controller.internal.WidgetResourceProvider.WIDGET_WIDGET_NAME_PROPERTY_ID, org.apache.ambari.server.controller.internal.WidgetResourceProvider.WIDGET_WIDGET_TYPE_PROPERTY_ID, org.apache.ambari.server.controller.internal.WidgetResourceProvider.WIDGET_TIME_CREATED_PROPERTY_ID, org.apache.ambari.server.controller.internal.WidgetResourceProvider.WIDGET_CLUSTER_NAME_PROPERTY_ID, org.apache.ambari.server.controller.internal.WidgetResourceProvider.WIDGET_AUTHOR_PROPERTY_ID, org.apache.ambari.server.controller.internal.WidgetResourceProvider.WIDGET_DESCRIPTION_PROPERTY_ID, org.apache.ambari.server.controller.internal.WidgetResourceProvider.WIDGET_SCOPE_PROPERTY_ID, org.apache.ambari.server.controller.internal.WidgetResourceProvider.WIDGET_METRICS_PROPERTY_ID, org.apache.ambari.server.controller.internal.WidgetResourceProvider.WIDGET_VALUES_PROPERTY_ID, org.apache.ambari.server.controller.internal.WidgetResourceProvider.WIDGET_PROPERTIES_PROPERTY_ID);
        org.apache.ambari.server.controller.AmbariManagementController amc = EasyMock.createMock(org.apache.ambari.server.controller.AmbariManagementController.class);
        org.apache.ambari.server.state.Clusters clusters = EasyMock.createMock(org.apache.ambari.server.state.Clusters.class);
        org.apache.ambari.server.state.Cluster cluster = EasyMock.createMock(org.apache.ambari.server.state.Cluster.class);
        EasyMock.expect(amc.getClusters()).andReturn(clusters).atLeastOnce();
        EasyMock.expect(clusters.getClusterById(1L)).andReturn(cluster).atLeastOnce();
        EasyMock.expect(cluster.getClusterName()).andReturn("c1").anyTimes();
        org.apache.ambari.server.controller.spi.Predicate predicate = new org.apache.ambari.server.controller.utilities.PredicateBuilder().property(org.apache.ambari.server.controller.internal.WidgetResourceProvider.WIDGET_CLUSTER_NAME_PROPERTY_ID).equals("c1").and().property(org.apache.ambari.server.controller.internal.WidgetResourceProvider.WIDGET_ID_PROPERTY_ID).equals("1").and().property(org.apache.ambari.server.controller.internal.WidgetResourceProvider.WIDGET_AUTHOR_PROPERTY_ID).equals("username").toPredicate();
        EasyMock.expect(dao.findById(1L)).andReturn(getMockEntities("CLUSTER").get(0));
        EasyMock.replay(amc, clusters, cluster, dao);
        org.apache.ambari.server.controller.internal.WidgetResourceProvider provider = createProvider(amc);
        java.util.Set<org.apache.ambari.server.controller.spi.Resource> results = provider.getResources(request, predicate);
        org.junit.Assert.assertEquals(1, results.size());
        org.apache.ambari.server.controller.spi.Resource r = results.iterator().next();
        org.junit.Assert.assertEquals("GAUGE", r.getPropertyValue(org.apache.ambari.server.controller.internal.WidgetResourceProvider.WIDGET_WIDGET_TYPE_PROPERTY_ID));
        org.junit.Assert.assertEquals("CLUSTER", r.getPropertyValue(org.apache.ambari.server.controller.internal.WidgetResourceProvider.WIDGET_SCOPE_PROPERTY_ID));
        org.junit.Assert.assertEquals("username", r.getPropertyValue(org.apache.ambari.server.controller.internal.WidgetResourceProvider.WIDGET_AUTHOR_PROPERTY_ID));
        org.junit.Assert.assertEquals("widget name", r.getPropertyValue(org.apache.ambari.server.controller.internal.WidgetResourceProvider.WIDGET_WIDGET_NAME_PROPERTY_ID));
        java.lang.Object metrics = r.getPropertyValue(org.apache.ambari.server.controller.internal.WidgetResourceProvider.WIDGET_METRICS_PROPERTY_ID);
        org.junit.Assert.assertEquals("[{\"widget_id\":\"metrics/jvm/HeapMemoryUsed\"," + ((((((("\"host_component_criteria\":\"host_components/metrics/dfs/FSNamesystem/HAState\\u003dactive\"," + "\"service_name\":\"HDFS\",\"component_name\":\"NAMENODE\",") + "\"name\":\"java.lang:type\\u003dMemory.HeapMemoryUsage[used]\",\"category\":\"\"},") + "{\"widget_id\":\"metrics/jvm/HeapMemoryMax\",") + "\"host_component_criteria\":\"host_components/metrics/dfs/FSNamesystem/HAState\\u003dactive\",") + "\"service_name\":\"HDFS\",\"component_name\":\"NAMENODE\",") + "\"name\":\"java.lang:type\\u003dMemory.HeapMemoryUsage[max]\",") + "\"category\":\"\"}]"), r.getPropertyValue(org.apache.ambari.server.controller.internal.WidgetResourceProvider.WIDGET_METRICS_PROPERTY_ID));
        org.junit.Assert.assertEquals("[{\"name\":\"NameNode Heap\"," + ("\"value\":\"${java.lang:type\\u003dMemory.HeapMemoryUsage[used] / " + "java.lang:type\\u003dMemory.HeapMemoryUsage[max]}\"}]"), r.getPropertyValue(org.apache.ambari.server.controller.internal.WidgetResourceProvider.WIDGET_VALUES_PROPERTY_ID));
        org.junit.Assert.assertEquals("{\"name\":\"value\"}", r.getPropertyValue(org.apache.ambari.server.controller.internal.WidgetResourceProvider.WIDGET_PROPERTIES_PROPERTY_ID));
    }

    @org.junit.Test
    public void testGetResourceOfOtherUser() throws java.lang.Exception {
        org.apache.ambari.server.controller.spi.Request request = org.apache.ambari.server.controller.utilities.PropertyHelper.getReadRequest(org.apache.ambari.server.controller.internal.WidgetResourceProvider.WIDGET_ID_PROPERTY_ID, org.apache.ambari.server.controller.internal.WidgetResourceProvider.WIDGET_WIDGET_NAME_PROPERTY_ID, org.apache.ambari.server.controller.internal.WidgetResourceProvider.WIDGET_WIDGET_TYPE_PROPERTY_ID, org.apache.ambari.server.controller.internal.WidgetResourceProvider.WIDGET_TIME_CREATED_PROPERTY_ID, org.apache.ambari.server.controller.internal.WidgetResourceProvider.WIDGET_CLUSTER_NAME_PROPERTY_ID, org.apache.ambari.server.controller.internal.WidgetResourceProvider.WIDGET_AUTHOR_PROPERTY_ID, org.apache.ambari.server.controller.internal.WidgetResourceProvider.WIDGET_DESCRIPTION_PROPERTY_ID, org.apache.ambari.server.controller.internal.WidgetResourceProvider.WIDGET_SCOPE_PROPERTY_ID, org.apache.ambari.server.controller.internal.WidgetResourceProvider.WIDGET_METRICS_PROPERTY_ID, org.apache.ambari.server.controller.internal.WidgetResourceProvider.WIDGET_VALUES_PROPERTY_ID, org.apache.ambari.server.controller.internal.WidgetResourceProvider.WIDGET_PROPERTIES_PROPERTY_ID);
        org.apache.ambari.server.controller.AmbariManagementController amc = EasyMock.createMock(org.apache.ambari.server.controller.AmbariManagementController.class);
        org.apache.ambari.server.state.Clusters clusters = EasyMock.createMock(org.apache.ambari.server.state.Clusters.class);
        org.apache.ambari.server.state.Cluster cluster = EasyMock.createMock(org.apache.ambari.server.state.Cluster.class);
        EasyMock.expect(amc.getClusters()).andReturn(clusters).atLeastOnce();
        EasyMock.expect(clusters.getClusterById(1L)).andReturn(cluster).atLeastOnce();
        EasyMock.expect(cluster.getClusterName()).andReturn("c1").anyTimes();
        org.apache.ambari.server.controller.spi.Predicate predicate = new org.apache.ambari.server.controller.utilities.PredicateBuilder().property(org.apache.ambari.server.controller.internal.WidgetResourceProvider.WIDGET_CLUSTER_NAME_PROPERTY_ID).equals("c1").and().property(org.apache.ambari.server.controller.internal.WidgetResourceProvider.WIDGET_ID_PROPERTY_ID).equals("1").and().property(org.apache.ambari.server.controller.internal.WidgetResourceProvider.WIDGET_AUTHOR_PROPERTY_ID).equals("username").toPredicate();
        EasyMock.expect(dao.findById(1L)).andReturn(getMockEntities("USER").get(0));
        EasyMock.replay(amc, clusters, cluster, dao);
        org.apache.ambari.server.controller.internal.WidgetResourceProvider provider = createProvider(amc);
        try {
            java.util.Set<org.apache.ambari.server.controller.spi.Resource> results = provider.getResources(request, predicate);
        } catch (org.springframework.security.access.AccessDeniedException ex) {
            org.junit.Assert.assertEquals("User must be author of the widget or widget must have cluster scope", ex.getMessage());
        }
    }

    @org.junit.Test
    public void testCreateResources() throws java.lang.Exception {
        org.apache.ambari.server.controller.AmbariManagementController amc = EasyMock.createMock(org.apache.ambari.server.controller.AmbariManagementController.class);
        org.apache.ambari.server.state.Clusters clusters = EasyMock.createMock(org.apache.ambari.server.state.Clusters.class);
        org.apache.ambari.server.state.Cluster cluster = EasyMock.createMock(org.apache.ambari.server.state.Cluster.class);
        EasyMock.expect(amc.getClusters()).andReturn(clusters).atLeastOnce();
        EasyMock.expect(clusters.getCluster(((java.lang.String) (EasyMock.anyObject())))).andReturn(cluster).atLeastOnce();
        EasyMock.expect(cluster.getClusterId()).andReturn(java.lang.Long.valueOf(1)).anyTimes();
        org.easymock.Capture<org.apache.ambari.server.orm.entities.WidgetEntity> entityCapture = org.easymock.EasyMock.newCapture();
        dao.create(EasyMock.capture(entityCapture));
        EasyMock.expectLastCall();
        EasyMock.replay(amc, clusters, cluster, dao);
        org.apache.ambari.server.controller.internal.WidgetResourceProvider provider = createProvider(amc);
        java.util.Map<java.lang.String, java.lang.Object> requestProps = new java.util.HashMap<>();
        requestProps.put(org.apache.ambari.server.controller.internal.WidgetResourceProvider.WIDGET_CLUSTER_NAME_PROPERTY_ID, "c1");
        requestProps.put(org.apache.ambari.server.controller.internal.WidgetResourceProvider.WIDGET_WIDGET_NAME_PROPERTY_ID, "widget name");
        requestProps.put(org.apache.ambari.server.controller.internal.WidgetResourceProvider.WIDGET_WIDGET_TYPE_PROPERTY_ID, "GAUGE");
        requestProps.put(org.apache.ambari.server.controller.internal.WidgetResourceProvider.WIDGET_AUTHOR_PROPERTY_ID, "admin");
        requestProps.put(org.apache.ambari.server.controller.internal.WidgetResourceProvider.WIDGET_SCOPE_PROPERTY_ID, "USER");
        java.util.Set<java.util.Map<java.lang.String, java.lang.String>> testSet = new java.util.LinkedHashSet<>();
        java.util.Map<java.lang.String, java.lang.String> testMap = new java.util.HashMap<>();
        testMap.put("name", "value");
        testMap.put("name2", "value2");
        testSet.add(testMap);
        requestProps.put(org.apache.ambari.server.controller.internal.WidgetResourceProvider.WIDGET_METRICS_PROPERTY_ID, testSet);
        requestProps.put(org.apache.ambari.server.controller.internal.WidgetResourceProvider.WIDGET_VALUES_PROPERTY_ID, testSet);
        requestProps.put(org.apache.ambari.server.controller.internal.WidgetResourceProvider.WIDGET_PROPERTIES_PROPERTY_ID + "/property1", "value1");
        requestProps.put(org.apache.ambari.server.controller.internal.WidgetResourceProvider.WIDGET_PROPERTIES_PROPERTY_ID + "/property2", "value2");
        org.apache.ambari.server.controller.spi.Request request = org.apache.ambari.server.controller.utilities.PropertyHelper.getCreateRequest(java.util.Collections.singleton(requestProps), null);
        org.apache.ambari.server.controller.spi.RequestStatus requestStatus = provider.createResources(request);
        org.junit.Assert.assertTrue(entityCapture.hasCaptured());
        org.apache.ambari.server.orm.entities.WidgetEntity entity = entityCapture.getValue();
        org.junit.Assert.assertNotNull(entity);
        org.junit.Assert.assertEquals(1, requestStatus.getAssociatedResources().size());
        org.junit.Assert.assertEquals(java.lang.Long.valueOf(1), entity.getClusterId());
        org.junit.Assert.assertEquals("USER", entity.getScope());
        org.junit.Assert.assertEquals("widget name", entity.getWidgetName());
        org.junit.Assert.assertEquals(null, entity.getDefaultSectionName());
        org.junit.Assert.assertEquals("GAUGE", entity.getWidgetType());
        org.junit.Assert.assertEquals("admin", entity.getAuthor());
        org.junit.Assert.assertEquals("[{\"name\":\"value\",\"name2\":\"value2\"}]", entity.getMetrics());
        org.junit.Assert.assertEquals("[{\"name\":\"value\",\"name2\":\"value2\"}]", entity.getWidgetValues());
        org.junit.Assert.assertEquals("{\"property2\":\"value2\",\"property1\":\"value1\"}", entity.getProperties());
        EasyMock.verify(amc, clusters, cluster, dao);
    }

    @org.junit.Test
    public void testUpdateResources() throws java.lang.Exception {
        org.apache.ambari.server.controller.AmbariManagementController amc = EasyMock.createMock(org.apache.ambari.server.controller.AmbariManagementController.class);
        org.apache.ambari.server.state.Clusters clusters = EasyMock.createMock(org.apache.ambari.server.state.Clusters.class);
        org.apache.ambari.server.state.Cluster cluster = EasyMock.createMock(org.apache.ambari.server.state.Cluster.class);
        EasyMock.expect(amc.getClusters()).andReturn(clusters).atLeastOnce();
        EasyMock.expect(clusters.getCluster(((java.lang.String) (EasyMock.anyObject())))).andReturn(cluster).atLeastOnce();
        EasyMock.expect(cluster.getClusterId()).andReturn(java.lang.Long.valueOf(1)).atLeastOnce();
        org.easymock.Capture<org.apache.ambari.server.orm.entities.WidgetEntity> entityCapture = org.easymock.EasyMock.newCapture();
        dao.create(EasyMock.capture(entityCapture));
        EasyMock.expectLastCall();
        EasyMock.replay(amc, clusters, cluster, dao);
        java.util.Map<java.lang.String, java.lang.Object> requestProps = new java.util.HashMap<>();
        requestProps.put(org.apache.ambari.server.controller.internal.WidgetResourceProvider.WIDGET_CLUSTER_NAME_PROPERTY_ID, "c1");
        requestProps.put(org.apache.ambari.server.controller.internal.WidgetResourceProvider.WIDGET_WIDGET_NAME_PROPERTY_ID, "widget name");
        requestProps.put(org.apache.ambari.server.controller.internal.WidgetResourceProvider.WIDGET_WIDGET_TYPE_PROPERTY_ID, "GAUGE");
        requestProps.put(org.apache.ambari.server.controller.internal.WidgetResourceProvider.WIDGET_AUTHOR_PROPERTY_ID, "admin");
        requestProps.put(org.apache.ambari.server.controller.internal.WidgetResourceProvider.WIDGET_SCOPE_PROPERTY_ID, "USER");
        java.util.Set<java.util.Map<java.lang.String, java.lang.String>> testSet = new java.util.LinkedHashSet<>();
        java.util.Map<java.lang.String, java.lang.String> testMap = new java.util.HashMap<>();
        testMap.put("name", "value");
        testMap.put("name2", "value2");
        testSet.add(testMap);
        requestProps.put(org.apache.ambari.server.controller.internal.WidgetResourceProvider.WIDGET_METRICS_PROPERTY_ID, testSet);
        requestProps.put(org.apache.ambari.server.controller.internal.WidgetResourceProvider.WIDGET_VALUES_PROPERTY_ID, testSet);
        requestProps.put(org.apache.ambari.server.controller.internal.WidgetResourceProvider.WIDGET_PROPERTIES_PROPERTY_ID + "/property1", "value1");
        requestProps.put(org.apache.ambari.server.controller.internal.WidgetResourceProvider.WIDGET_PROPERTIES_PROPERTY_ID + "/property2", "value2");
        org.apache.ambari.server.controller.spi.Request request = org.apache.ambari.server.controller.utilities.PropertyHelper.getCreateRequest(java.util.Collections.singleton(requestProps), null);
        org.apache.ambari.server.controller.internal.WidgetResourceProvider provider = createProvider(amc);
        provider.createResources(request);
        org.junit.Assert.assertTrue(entityCapture.hasCaptured());
        org.apache.ambari.server.orm.entities.WidgetEntity entity = entityCapture.getValue();
        org.junit.Assert.assertNotNull(entity);
        org.apache.ambari.server.controller.spi.Predicate predicate = new org.apache.ambari.server.controller.utilities.PredicateBuilder().property(org.apache.ambari.server.controller.internal.WidgetResourceProvider.WIDGET_CLUSTER_NAME_PROPERTY_ID).equals("c1").and().property(org.apache.ambari.server.controller.internal.WidgetResourceProvider.WIDGET_ID_PROPERTY_ID).equals("1").and().property(org.apache.ambari.server.controller.internal.WidgetResourceProvider.WIDGET_AUTHOR_PROPERTY_ID).equals("username").toPredicate();
        entity.setId(java.lang.Long.valueOf(1));
        java.lang.String oldMetrics = entity.getMetrics();
        java.lang.String oldProperties = entity.getProperties();
        java.lang.String oldName = entity.getWidgetName();
        EasyMock.resetToStrict(dao);
        EasyMock.expect(dao.findById(1L)).andReturn(entity).anyTimes();
        EasyMock.expect(dao.merge(((org.apache.ambari.server.orm.entities.WidgetEntity) (EasyMock.anyObject())))).andReturn(entity).anyTimes();
        EasyMock.replay(dao);
        requestProps = new java.util.HashMap<>();
        requestProps.put(org.apache.ambari.server.controller.internal.WidgetResourceProvider.WIDGET_ID_PROPERTY_ID, "1");
        requestProps.put(org.apache.ambari.server.controller.internal.WidgetResourceProvider.WIDGET_CLUSTER_NAME_PROPERTY_ID, "c1");
        requestProps.put(org.apache.ambari.server.controller.internal.WidgetResourceProvider.WIDGET_WIDGET_NAME_PROPERTY_ID, "widget name2");
        requestProps.put(org.apache.ambari.server.controller.internal.WidgetResourceProvider.WIDGET_WIDGET_TYPE_PROPERTY_ID, "GAUGE");
        requestProps.put(org.apache.ambari.server.controller.internal.WidgetResourceProvider.WIDGET_AUTHOR_PROPERTY_ID, "admin");
        requestProps.put(org.apache.ambari.server.controller.internal.WidgetResourceProvider.WIDGET_SCOPE_PROPERTY_ID, "USER");
        testSet = new java.util.LinkedHashSet<>();
        testMap = new java.util.HashMap<>();
        testMap.put("name", "new_value");
        testMap.put("new_name", "new_value2");
        testSet.add(testMap);
        requestProps.put(org.apache.ambari.server.controller.internal.WidgetResourceProvider.WIDGET_METRICS_PROPERTY_ID, testSet);
        requestProps.put(org.apache.ambari.server.controller.internal.WidgetResourceProvider.WIDGET_VALUES_PROPERTY_ID, testSet);
        requestProps.put(org.apache.ambari.server.controller.internal.WidgetResourceProvider.WIDGET_PROPERTIES_PROPERTY_ID + "/property1", "new_value1");
        requestProps.put(org.apache.ambari.server.controller.internal.WidgetResourceProvider.WIDGET_PROPERTIES_PROPERTY_ID + "/new_property", "new_value2");
        request = org.apache.ambari.server.controller.utilities.PropertyHelper.getUpdateRequest(requestProps, null);
        provider.updateResources(request, predicate);
        org.junit.Assert.assertFalse(oldName.equals(entity.getWidgetName()));
        org.junit.Assert.assertFalse(oldMetrics.equals(entity.getMetrics()));
        org.junit.Assert.assertFalse(oldProperties.equals(entity.getProperties()));
        org.junit.Assert.assertEquals("[{\"name\":\"new_value\",\"new_name\":\"new_value2\"}]", entity.getMetrics());
        org.junit.Assert.assertTrue(org.apache.ambari.server.utils.CollectionPresentationUtils.isJsonsEquals("{\"new_property\":\"new_value2\",\"property1\":\"new_value1\"}", entity.getProperties()));
        org.junit.Assert.assertEquals("widget name2", entity.getWidgetName());
        org.junit.Assert.assertEquals(null, entity.getDefaultSectionName());
        EasyMock.verify(amc, clusters, cluster, dao);
    }

    @org.junit.Test
    public void testDeleteResources() throws java.lang.Exception {
        org.apache.ambari.server.controller.AmbariManagementController amc = EasyMock.createMock(org.apache.ambari.server.controller.AmbariManagementController.class);
        org.apache.ambari.server.state.Clusters clusters = EasyMock.createMock(org.apache.ambari.server.state.Clusters.class);
        org.apache.ambari.server.state.Cluster cluster = EasyMock.createMock(org.apache.ambari.server.state.Cluster.class);
        EasyMock.expect(amc.getClusters()).andReturn(clusters).atLeastOnce();
        EasyMock.expect(clusters.getCluster(((java.lang.String) (EasyMock.anyObject())))).andReturn(cluster).atLeastOnce();
        EasyMock.expect(cluster.getClusterId()).andReturn(java.lang.Long.valueOf(1)).anyTimes();
        org.easymock.Capture<org.apache.ambari.server.orm.entities.WidgetEntity> entityCapture = org.easymock.EasyMock.newCapture();
        dao.create(EasyMock.capture(entityCapture));
        EasyMock.expectLastCall();
        EasyMock.replay(amc, clusters, cluster, dao);
        org.apache.ambari.server.controller.internal.WidgetResourceProvider provider = createProvider(amc);
        java.util.Map<java.lang.String, java.lang.Object> requestProps = new java.util.HashMap<>();
        requestProps.put(org.apache.ambari.server.controller.internal.WidgetResourceProvider.WIDGET_CLUSTER_NAME_PROPERTY_ID, "c1");
        requestProps.put(org.apache.ambari.server.controller.internal.WidgetResourceProvider.WIDGET_WIDGET_NAME_PROPERTY_ID, "widget name");
        requestProps.put(org.apache.ambari.server.controller.internal.WidgetResourceProvider.WIDGET_WIDGET_TYPE_PROPERTY_ID, "GAUGE");
        requestProps.put(org.apache.ambari.server.controller.internal.WidgetResourceProvider.WIDGET_AUTHOR_PROPERTY_ID, "admin");
        requestProps.put(org.apache.ambari.server.controller.internal.WidgetResourceProvider.WIDGET_SCOPE_PROPERTY_ID, "USER");
        java.util.Set<java.util.Map<java.lang.String, java.lang.String>> testSet = new java.util.LinkedHashSet<>();
        java.util.Map<java.lang.String, java.lang.String> testMap = new java.util.HashMap<>();
        testMap.put("name", "value");
        testMap.put("name2", "value2");
        testSet.add(testMap);
        requestProps.put(org.apache.ambari.server.controller.internal.WidgetResourceProvider.WIDGET_METRICS_PROPERTY_ID, testSet);
        requestProps.put(org.apache.ambari.server.controller.internal.WidgetResourceProvider.WIDGET_VALUES_PROPERTY_ID, testSet);
        requestProps.put(org.apache.ambari.server.controller.internal.WidgetResourceProvider.WIDGET_PROPERTIES_PROPERTY_ID + "/property1", "value1");
        requestProps.put(org.apache.ambari.server.controller.internal.WidgetResourceProvider.WIDGET_PROPERTIES_PROPERTY_ID + "/property2", "value2");
        org.apache.ambari.server.controller.spi.Request request = org.apache.ambari.server.controller.utilities.PropertyHelper.getCreateRequest(java.util.Collections.singleton(requestProps), null);
        provider.createResources(request);
        org.junit.Assert.assertTrue(entityCapture.hasCaptured());
        org.apache.ambari.server.orm.entities.WidgetEntity entity = entityCapture.getValue();
        org.junit.Assert.assertNotNull(entity);
        org.apache.ambari.server.controller.spi.Predicate predicate = new org.apache.ambari.server.controller.utilities.PredicateBuilder().property(org.apache.ambari.server.controller.internal.WidgetResourceProvider.WIDGET_CLUSTER_NAME_PROPERTY_ID).equals("c1").and().property(org.apache.ambari.server.controller.internal.WidgetResourceProvider.WIDGET_ID_PROPERTY_ID).equals("1").and().property(org.apache.ambari.server.controller.internal.WidgetResourceProvider.WIDGET_AUTHOR_PROPERTY_ID).equals("username").toPredicate();
        entity.setId(java.lang.Long.valueOf(1));
        EasyMock.resetToStrict(dao);
        EasyMock.expect(dao.findById(1L)).andReturn(entity).anyTimes();
        dao.remove(EasyMock.capture(entityCapture));
        EasyMock.expectLastCall();
        EasyMock.replay(dao);
        provider.deleteResources(request, predicate);
        org.apache.ambari.server.orm.entities.WidgetEntity entity1 = entityCapture.getValue();
        org.junit.Assert.assertEquals(java.lang.Long.valueOf(1), entity1.getId());
        EasyMock.verify(amc, clusters, cluster, dao);
    }

    @org.junit.Test
    public void testScopePrivilegeCheck() throws java.lang.Exception {
        org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(org.apache.ambari.server.security.TestAuthenticationFactory.createServiceOperator());
        org.apache.ambari.server.controller.AmbariManagementController amc = EasyMock.createMock(org.apache.ambari.server.controller.AmbariManagementController.class);
        org.apache.ambari.server.state.Clusters clusters = EasyMock.createMock(org.apache.ambari.server.state.Clusters.class);
        org.apache.ambari.server.state.Cluster cluster = EasyMock.createMock(org.apache.ambari.server.state.Cluster.class);
        EasyMock.expect(amc.getClusters()).andReturn(clusters).atLeastOnce();
        EasyMock.expect(clusters.getCluster(((java.lang.String) (EasyMock.anyObject())))).andReturn(cluster).atLeastOnce();
        EasyMock.expect(cluster.getResourceId()).andReturn(java.lang.Long.valueOf(1)).atLeastOnce();
        org.easymock.Capture<org.apache.ambari.server.orm.entities.WidgetEntity> entityCapture = org.easymock.EasyMock.newCapture();
        dao.create(EasyMock.capture(entityCapture));
        EasyMock.expectLastCall();
        EasyMock.replay(amc, clusters, cluster, dao);
        org.powermock.api.easymock.PowerMock.replayAll();
        java.util.Map<java.lang.String, java.lang.Object> requestProps = new java.util.HashMap<>();
        requestProps.put(org.apache.ambari.server.controller.internal.WidgetResourceProvider.WIDGET_CLUSTER_NAME_PROPERTY_ID, "c1");
        requestProps.put(org.apache.ambari.server.controller.internal.WidgetResourceProvider.WIDGET_WIDGET_NAME_PROPERTY_ID, "widget name");
        requestProps.put(org.apache.ambari.server.controller.internal.WidgetResourceProvider.WIDGET_WIDGET_TYPE_PROPERTY_ID, "GAUGE");
        requestProps.put(org.apache.ambari.server.controller.internal.WidgetResourceProvider.WIDGET_AUTHOR_PROPERTY_ID, "admin");
        requestProps.put(org.apache.ambari.server.controller.internal.WidgetResourceProvider.WIDGET_SCOPE_PROPERTY_ID, "CLUSTER");
        org.apache.ambari.server.controller.spi.Request request = org.apache.ambari.server.controller.utilities.PropertyHelper.getCreateRequest(java.util.Collections.singleton(requestProps), null);
        try {
            org.apache.ambari.server.controller.internal.WidgetResourceProvider widgetResourceProvider = createProvider(amc);
            widgetResourceProvider.createResources(request);
        } catch (org.springframework.security.access.AccessDeniedException ex) {
        }
    }

    private org.apache.ambari.server.controller.internal.WidgetResourceProvider createProvider(org.apache.ambari.server.controller.AmbariManagementController amc) {
        return new org.apache.ambari.server.controller.internal.WidgetResourceProvider(amc);
    }

    private java.util.List<org.apache.ambari.server.orm.entities.WidgetEntity> getMockEntities(java.lang.String scope) throws java.lang.Exception {
        org.apache.ambari.server.orm.entities.WidgetEntity widgetEntity = new org.apache.ambari.server.orm.entities.WidgetEntity();
        widgetEntity.setClusterId(java.lang.Long.valueOf(1L));
        widgetEntity.setWidgetName("widget name");
        widgetEntity.setWidgetType("GAUGE");
        widgetEntity.setAuthor("username");
        widgetEntity.setScope(scope);
        widgetEntity.setDefaultSectionName("default_section_name");
        widgetEntity.setDescription("Description");
        widgetEntity.setMetrics("[{\"widget_id\":\"metrics/jvm/HeapMemoryUsed\"," + ((((((("\"host_component_criteria\":\"host_components/metrics/dfs/FSNamesystem/HAState\\u003dactive\"," + "\"service_name\":\"HDFS\",\"component_name\":\"NAMENODE\",") + "\"name\":\"java.lang:type\\u003dMemory.HeapMemoryUsage[used]\",\"category\":\"\"},") + "{\"widget_id\":\"metrics/jvm/HeapMemoryMax\",") + "\"host_component_criteria\":\"host_components/metrics/dfs/FSNamesystem/HAState\\u003dactive\",") + "\"service_name\":\"HDFS\",\"component_name\":\"NAMENODE\",") + "\"name\":\"java.lang:type\\u003dMemory.HeapMemoryUsage[max]\",") + "\"category\":\"\"}]"));
        widgetEntity.setWidgetValues("[{\"name\":\"NameNode Heap\"," + ("\"value\":\"${java.lang:type\\u003dMemory.HeapMemoryUsage[used] / " + "java.lang:type\\u003dMemory.HeapMemoryUsage[max]}\"}]"));
        widgetEntity.setProperties("{\"name\":\"value\"}");
        return java.util.Arrays.asList(widgetEntity);
    }

    private class MockModule implements com.google.inject.Module {
        @java.lang.Override
        public void configure(com.google.inject.Binder binder) {
            binder.bind(org.apache.ambari.server.orm.dao.WidgetDAO.class).toInstance(dao);
            binder.bind(org.apache.ambari.server.state.Clusters.class).toInstance(org.easymock.EasyMock.createNiceMock(org.apache.ambari.server.state.Clusters.class));
            binder.bind(org.apache.ambari.server.state.Cluster.class).toInstance(org.easymock.EasyMock.createNiceMock(org.apache.ambari.server.state.Cluster.class));
            binder.bind(org.apache.ambari.server.security.encryption.CredentialStoreService.class).toInstance(org.easymock.EasyMock.createNiceMock(org.apache.ambari.server.security.encryption.CredentialStoreService.class));
        }
    }
}