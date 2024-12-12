package org.apache.ambari.server.controller.internal;
import javax.persistence.EntityManager;
import org.easymock.Capture;
import org.easymock.IAnswer;
import static org.easymock.EasyMock.anyString;
import static org.easymock.EasyMock.capture;
import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.createStrictMock;
import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.getCurrentArguments;
import static org.easymock.EasyMock.newCapture;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.reset;
import static org.easymock.EasyMock.verify;
@java.lang.SuppressWarnings("unchecked")
public class ArtifactResourceProviderTest {
    private org.apache.ambari.server.orm.dao.ArtifactDAO dao = EasyMock.createStrictMock(org.apache.ambari.server.orm.dao.ArtifactDAO.class);

    private javax.persistence.EntityManager em = EasyMock.createStrictMock(javax.persistence.EntityManager.class);

    private org.apache.ambari.server.controller.AmbariManagementController controller = EasyMock.createStrictMock(org.apache.ambari.server.controller.AmbariManagementController.class);

    private org.apache.ambari.server.controller.spi.Request request = EasyMock.createMock(org.apache.ambari.server.controller.spi.Request.class);

    private org.apache.ambari.server.state.Clusters clusters = EasyMock.createMock(org.apache.ambari.server.state.Clusters.class);

    private org.apache.ambari.server.state.Cluster cluster = EasyMock.createMock(org.apache.ambari.server.state.Cluster.class);

    private org.apache.ambari.server.orm.entities.ArtifactEntity entity = EasyMock.createMock(org.apache.ambari.server.orm.entities.ArtifactEntity.class);

    private org.apache.ambari.server.orm.entities.ArtifactEntity entity2 = EasyMock.createMock(org.apache.ambari.server.orm.entities.ArtifactEntity.class);

    org.apache.ambari.server.controller.internal.ArtifactResourceProvider resourceProvider;

    @org.junit.Before
    public void setUp() throws java.lang.Exception {
        EasyMock.reset(dao, em, controller, request, clusters, cluster, entity, entity2);
        resourceProvider = new org.apache.ambari.server.controller.internal.ArtifactResourceProvider(controller);
        setPrivateField(resourceProvider, "artifactDAO", dao);
    }

    @org.junit.Test
    public void testGetResources_instance() throws java.lang.Exception {
        java.util.Set<java.lang.String> propertyIds = new java.util.HashSet<>();
        java.util.TreeMap<java.lang.String, java.lang.String> foreignKeys = new java.util.TreeMap<>();
        foreignKeys.put("cluster", "500");
        java.util.Map<java.lang.String, java.lang.Object> childMap = new java.util.TreeMap<>();
        childMap.put("childKey", "childValue");
        java.util.Map<java.lang.String, java.lang.Object> child2Map = new java.util.TreeMap<>();
        childMap.put("child2", child2Map);
        child2Map.put("child2Key", "child2Value");
        java.util.Map<java.lang.String, java.lang.Object> child3Map = new java.util.TreeMap<>();
        child2Map.put("child3", child3Map);
        java.util.Map<java.lang.String, java.lang.Object> child4Map = new java.util.TreeMap<>();
        child3Map.put("child4", child4Map);
        child4Map.put("child4Key", "child4Value");
        java.util.Map<java.lang.String, java.lang.Object> artifact_data = new java.util.TreeMap<>();
        artifact_data.put("foo", "bar");
        artifact_data.put("child", childMap);
        java.util.Map<java.lang.String, java.lang.String> responseForeignKeys = new java.util.HashMap<>();
        responseForeignKeys.put("cluster", "500");
        EasyMock.expect(controller.getClusters()).andReturn(clusters).anyTimes();
        EasyMock.expect(clusters.getCluster("test-cluster")).andReturn(cluster).anyTimes();
        EasyMock.expect(clusters.getClusterById(500L)).andReturn(cluster).anyTimes();
        EasyMock.expect(cluster.getClusterId()).andReturn(500L).anyTimes();
        EasyMock.expect(cluster.getClusterName()).andReturn("test-cluster").anyTimes();
        EasyMock.expect(request.getPropertyIds()).andReturn(propertyIds).anyTimes();
        EasyMock.expect(dao.findByNameAndForeignKeys(EasyMock.eq("test-artifact"), EasyMock.eq(foreignKeys))).andReturn(entity).once();
        EasyMock.expect(entity.getArtifactName()).andReturn("test-artifact").anyTimes();
        EasyMock.expect(entity.getForeignKeys()).andReturn(responseForeignKeys).anyTimes();
        EasyMock.expect(entity.getArtifactData()).andReturn(artifact_data).anyTimes();
        EasyMock.replay(dao, em, controller, request, clusters, cluster, entity, entity2);
        org.apache.ambari.server.controller.utilities.PredicateBuilder pb = new org.apache.ambari.server.controller.utilities.PredicateBuilder();
        org.apache.ambari.server.controller.spi.Predicate predicate = pb.begin().property("Artifacts/cluster_name").equals("test-cluster").and().property("Artifacts/artifact_name").equals("test-artifact").end().toPredicate();
        java.util.Set<org.apache.ambari.server.controller.spi.Resource> response = resourceProvider.getResources(request, predicate);
        org.junit.Assert.assertEquals(1, response.size());
        org.apache.ambari.server.controller.spi.Resource resource = response.iterator().next();
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.Object>> responseProperties = resource.getPropertiesMap();
        org.junit.Assert.assertEquals(5, responseProperties.size());
        java.util.Map<java.lang.String, java.lang.Object> artifactDataMap = responseProperties.get("artifact_data");
        org.junit.Assert.assertEquals("bar", artifactDataMap.get("foo"));
        org.junit.Assert.assertEquals("test-artifact", resource.getPropertyValue("Artifacts/artifact_name"));
        org.junit.Assert.assertEquals("test-cluster", resource.getPropertyValue("Artifacts/cluster_name"));
        org.junit.Assert.assertEquals("bar", resource.getPropertyValue("artifact_data/foo"));
        org.junit.Assert.assertEquals("childValue", resource.getPropertyValue("artifact_data/child/childKey"));
        org.junit.Assert.assertEquals("child2Value", resource.getPropertyValue("artifact_data/child/child2/child2Key"));
        org.junit.Assert.assertEquals("child4Value", resource.getPropertyValue("artifact_data/child/child2/child3/child4/child4Key"));
    }

    @org.junit.Test
    public void testGetResources_collection() throws java.lang.Exception {
        java.util.Set<java.lang.String> propertyIds = new java.util.HashSet<>();
        java.util.TreeMap<java.lang.String, java.lang.String> foreignKeys = new java.util.TreeMap<>();
        foreignKeys.put("cluster", "500");
        java.util.List<org.apache.ambari.server.orm.entities.ArtifactEntity> entities = new java.util.ArrayList<>();
        entities.add(entity);
        entities.add(entity2);
        java.util.Map<java.lang.String, java.lang.Object> artifact_data = java.util.Collections.singletonMap("foo", "bar");
        java.util.Map<java.lang.String, java.lang.Object> artifact_data2 = java.util.Collections.singletonMap("foo2", "bar2");
        java.util.Map<java.lang.String, java.lang.String> responseForeignKeys = new java.util.HashMap<>();
        responseForeignKeys.put("cluster", "500");
        EasyMock.expect(controller.getClusters()).andReturn(clusters).anyTimes();
        EasyMock.expect(clusters.getCluster("test-cluster")).andReturn(cluster).anyTimes();
        EasyMock.expect(clusters.getClusterById(500L)).andReturn(cluster).anyTimes();
        EasyMock.expect(cluster.getClusterId()).andReturn(500L).anyTimes();
        EasyMock.expect(cluster.getClusterName()).andReturn("test-cluster").anyTimes();
        EasyMock.expect(request.getPropertyIds()).andReturn(propertyIds).anyTimes();
        EasyMock.expect(dao.findByForeignKeys(EasyMock.eq(foreignKeys))).andReturn(entities).anyTimes();
        EasyMock.expect(entity.getArtifactName()).andReturn("test-artifact").anyTimes();
        EasyMock.expect(entity.getForeignKeys()).andReturn(responseForeignKeys).anyTimes();
        EasyMock.expect(entity.getArtifactData()).andReturn(artifact_data).anyTimes();
        EasyMock.expect(entity2.getArtifactName()).andReturn("test-artifact2").anyTimes();
        EasyMock.expect(entity2.getForeignKeys()).andReturn(responseForeignKeys).anyTimes();
        EasyMock.expect(entity2.getArtifactData()).andReturn(artifact_data2).anyTimes();
        EasyMock.replay(dao, em, controller, request, clusters, cluster, entity, entity2);
        org.apache.ambari.server.controller.utilities.PredicateBuilder pb = new org.apache.ambari.server.controller.utilities.PredicateBuilder();
        org.apache.ambari.server.controller.spi.Predicate predicate = pb.begin().property("Artifacts/cluster_name").equals("test-cluster").end().toPredicate();
        java.util.Set<org.apache.ambari.server.controller.spi.Resource> response = resourceProvider.getResources(request, predicate);
        org.junit.Assert.assertEquals(2, response.size());
        boolean artifact1Returned = false;
        boolean artifact2Returned = false;
        for (org.apache.ambari.server.controller.spi.Resource resource : response) {
            if (resource.getPropertyValue("Artifacts/artifact_name").equals("test-artifact")) {
                artifact1Returned = true;
                org.junit.Assert.assertEquals("bar", resource.getPropertyValue("artifact_data/foo"));
                org.junit.Assert.assertEquals("test-cluster", resource.getPropertyValue("Artifacts/cluster_name"));
            } else if (resource.getPropertyValue("Artifacts/artifact_name").equals("test-artifact2")) {
                artifact2Returned = true;
                org.junit.Assert.assertEquals("bar2", resource.getPropertyValue("artifact_data/foo2"));
                org.junit.Assert.assertEquals("test-cluster", resource.getPropertyValue("Artifacts/cluster_name"));
            } else {
                org.junit.Assert.fail("unexpected artifact name");
            }
        }
        org.junit.Assert.assertTrue(artifact1Returned);
        org.junit.Assert.assertTrue(artifact2Returned);
    }

    @org.junit.Test
    public void testCreateResource() throws java.lang.Exception {
        org.easymock.Capture<org.apache.ambari.server.orm.entities.ArtifactEntity> createEntityCapture = EasyMock.newCapture();
        java.util.Map<java.lang.String, java.lang.Object> outerMap = new java.util.TreeMap<>();
        java.util.Map<java.lang.String, java.lang.Object> childMap = new java.util.TreeMap<>();
        outerMap.put("child", childMap);
        childMap.put("childKey", "childValue");
        java.util.Map<java.lang.String, java.lang.Object> child2Map = new java.util.TreeMap<>();
        childMap.put("child2", child2Map);
        child2Map.put("child2Key", "child2Value");
        java.util.Map<java.lang.String, java.lang.Object> child3Map = new java.util.TreeMap<>();
        child2Map.put("child3", child3Map);
        java.util.Map<java.lang.String, java.lang.Object> child4Map = new java.util.TreeMap<>();
        child3Map.put("child4", child4Map);
        child4Map.put("child4Key", "child4Value");
        java.util.Set<java.util.Map<java.lang.String, java.lang.Object>> propertySet = new java.util.HashSet<>();
        propertySet.add(outerMap);
        propertySet.add(child4Map);
        java.util.Map<java.lang.String, java.lang.Object> artifact_data = new java.util.TreeMap<>();
        artifact_data.put("foo", "bar");
        artifact_data.put("child", childMap);
        artifact_data.put("collection", propertySet);
        java.util.TreeMap<java.lang.String, java.lang.String> foreignKeys = new java.util.TreeMap<>();
        foreignKeys.put("cluster", "500");
        java.util.Map<java.lang.String, java.lang.String> requestInfoProps = new java.util.HashMap<>();
        requestInfoProps.put(org.apache.ambari.server.controller.spi.Request.REQUEST_INFO_BODY_PROPERTY, bodyJson);
        java.util.Map<java.lang.String, java.lang.Object> properties = new java.util.HashMap<>();
        properties.put("Artifacts/artifact_name", "test-artifact");
        properties.put("Artifacts/cluster_name", "test-cluster");
        properties.put("artifact_data/foo", "bar");
        properties.put("artifact_data/child/childKey", "childValue");
        properties.put("artifact_data/child/child2/child2Key", "child2Value");
        properties.put("artifact_data/child/child2/child3/child4/child4Key", "child4Value");
        java.util.Collection<java.lang.Object> collectionProperties = new java.util.HashSet<>();
        properties.put("artifact_data/collection", collectionProperties);
        java.util.Map<java.lang.String, java.lang.Object> map1 = new java.util.TreeMap<>();
        collectionProperties.add(map1);
        map1.put("foo", "bar");
        map1.put("child/childKey", "childValue");
        map1.put("child/child2/child2Key", "child2Value");
        map1.put("child/child2/child3/child4/child4Key", "child4Value");
        java.util.Map<java.lang.String, java.lang.Object> map2 = new java.util.TreeMap<>();
        collectionProperties.add(map2);
        map2.put("child4Key", "child4Value");
        java.util.Set<java.util.Map<java.lang.String, java.lang.Object>> requestProperties = java.util.Collections.singleton(properties);
        EasyMock.expect(request.getRequestInfoProperties()).andReturn(requestInfoProps).anyTimes();
        EasyMock.expect(request.getProperties()).andReturn(requestProperties).anyTimes();
        EasyMock.expect(controller.getClusters()).andReturn(clusters).anyTimes();
        EasyMock.expect(clusters.getCluster("test-cluster")).andReturn(cluster).anyTimes();
        EasyMock.expect(clusters.getClusterById(500L)).andReturn(cluster).anyTimes();
        EasyMock.expect(cluster.getClusterId()).andReturn(500L).anyTimes();
        EasyMock.expect(cluster.getClusterName()).andReturn("test-cluster").anyTimes();
        EasyMock.expect(dao.findByNameAndForeignKeys(EasyMock.eq("test-artifact"), EasyMock.eq(foreignKeys))).andReturn(null).once();
        dao.create(EasyMock.capture(createEntityCapture));
        EasyMock.replay(dao, em, controller, request, clusters, cluster, entity, entity2);
        resourceProvider.createResources(request);
        org.apache.ambari.server.orm.entities.ArtifactEntity createEntity = createEntityCapture.getValue();
        org.junit.Assert.assertEquals("test-artifact", createEntity.getArtifactName());
        java.util.Map<java.lang.String, java.lang.Object> actualArtifactData = createEntity.getArtifactData();
        org.junit.Assert.assertEquals(artifact_data.size(), actualArtifactData.size());
        org.junit.Assert.assertEquals(artifact_data.get("foo"), actualArtifactData.get("foo"));
        org.junit.Assert.assertEquals(artifact_data.get("child"), actualArtifactData.get("child"));
        org.junit.Assert.assertEquals(artifact_data.get("collection"), new java.util.HashSet(((java.util.Collection) (actualArtifactData.get("collection")))));
        org.junit.Assert.assertEquals(foreignKeys, createEntity.getForeignKeys());
    }

    @org.junit.Test
    public void testUpdateResources() throws java.lang.Exception {
        java.util.Map<java.lang.String, java.lang.String> requestInfoProps = new java.util.HashMap<>();
        requestInfoProps.put(org.apache.ambari.server.controller.spi.Request.REQUEST_INFO_BODY_PROPERTY, bodyJson);
        org.easymock.Capture<org.apache.ambari.server.orm.entities.ArtifactEntity> updateEntityCapture = EasyMock.newCapture();
        org.easymock.Capture<org.apache.ambari.server.orm.entities.ArtifactEntity> updateEntityCapture2 = EasyMock.newCapture();
        java.util.Set<java.lang.String> propertyIds = new java.util.HashSet<>();
        java.util.TreeMap<java.lang.String, java.lang.String> foreignKeys = new java.util.TreeMap<>();
        foreignKeys.put("cluster", "500");
        java.util.List<org.apache.ambari.server.orm.entities.ArtifactEntity> entities = new java.util.ArrayList<>();
        entities.add(entity);
        entities.add(entity2);
        java.util.Map<java.lang.String, java.lang.Object> artifact_data = java.util.Collections.singletonMap("foo", "bar");
        java.util.Map<java.lang.String, java.lang.Object> artifact_data2 = java.util.Collections.singletonMap("foo2", "bar2");
        java.util.Map<java.lang.String, java.lang.String> responseForeignKeys = new java.util.HashMap<>();
        responseForeignKeys.put("cluster", "500");
        java.util.Map<java.lang.String, java.lang.Object> properties = new java.util.HashMap<>();
        properties.put("Artifacts/artifact_name", "test-artifact");
        properties.put("Artifacts/cluster_name", "test-cluster");
        properties.put("artifact_data/foo", "bar");
        properties.put("artifact_data/child/childKey", "childValue");
        properties.put("artifact_data/child/child2/child2Key", "child2Value");
        properties.put("artifact_data/child/child2/child3/child4/child4Key", "child4Value");
        java.util.Set<java.util.Map<java.lang.String, java.lang.Object>> requestProperties = java.util.Collections.singleton(properties);
        properties.put("artifact_data/collection", java.util.Collections.emptySet());
        EasyMock.expect(request.getProperties()).andReturn(requestProperties).anyTimes();
        EasyMock.expect(request.getRequestInfoProperties()).andReturn(requestInfoProps).anyTimes();
        EasyMock.expect(controller.getClusters()).andReturn(clusters).anyTimes();
        EasyMock.expect(clusters.getCluster("test-cluster")).andReturn(cluster).anyTimes();
        EasyMock.expect(clusters.getClusterById(500L)).andReturn(cluster).anyTimes();
        EasyMock.expect(cluster.getClusterId()).andReturn(500L).anyTimes();
        EasyMock.expect(cluster.getClusterName()).andReturn("test-cluster").anyTimes();
        EasyMock.expect(request.getPropertyIds()).andReturn(propertyIds).anyTimes();
        EasyMock.expect(dao.findByForeignKeys(EasyMock.eq(foreignKeys))).andReturn(entities).anyTimes();
        EasyMock.expect(entity.getArtifactName()).andReturn("test-artifact").anyTimes();
        EasyMock.expect(entity.getForeignKeys()).andReturn(responseForeignKeys).anyTimes();
        EasyMock.expect(entity.getArtifactData()).andReturn(artifact_data).anyTimes();
        EasyMock.expect(entity2.getArtifactName()).andReturn("test-artifact2").anyTimes();
        EasyMock.expect(entity2.getForeignKeys()).andReturn(responseForeignKeys).anyTimes();
        EasyMock.expect(entity2.getArtifactData()).andReturn(artifact_data2).anyTimes();
        EasyMock.expect(dao.merge(EasyMock.capture(updateEntityCapture))).andReturn(entity).once();
        EasyMock.expect(dao.merge(EasyMock.capture(updateEntityCapture2))).andReturn(entity2).once();
        EasyMock.replay(dao, em, controller, request, clusters, cluster, entity, entity2);
        org.apache.ambari.server.controller.utilities.PredicateBuilder pb = new org.apache.ambari.server.controller.utilities.PredicateBuilder();
        org.apache.ambari.server.controller.spi.Predicate predicate = pb.begin().property("Artifacts/cluster_name").equals("test-cluster").end().toPredicate();
        org.apache.ambari.server.controller.spi.RequestStatus response = resourceProvider.updateResources(request, predicate);
        org.apache.ambari.server.orm.entities.ArtifactEntity updateEntity = updateEntityCapture.getValue();
        org.apache.ambari.server.orm.entities.ArtifactEntity updateEntity2 = updateEntityCapture2.getValue();
        com.google.gson.Gson serializer = new com.google.gson.Gson();
        org.apache.ambari.server.orm.entities.ArtifactEntity expected = new org.apache.ambari.server.orm.entities.ArtifactEntity();
        expected.setArtifactData(((java.util.Map<java.lang.String, java.lang.Object>) (serializer.<java.util.Map<java.lang.String, java.lang.Object>>fromJson(bodyJson, java.util.Map.class).get("artifact_data"))));
        org.junit.Assert.assertEquals(expected.getArtifactData(), updateEntity.getArtifactData());
        org.junit.Assert.assertEquals(expected.getArtifactData(), updateEntity2.getArtifactData());
        if (updateEntity.getArtifactName().equals("test-artifact")) {
            org.junit.Assert.assertEquals("test-artifact2", updateEntity2.getArtifactName());
        } else if (updateEntity.getArtifactName().equals("test-artifact2")) {
            org.junit.Assert.assertEquals("test-artifact", updateEntity2.getArtifactName());
        } else {
            org.junit.Assert.fail("Unexpected artifact name: " + updateEntity.getArtifactName());
        }
        org.junit.Assert.assertEquals(foreignKeys, updateEntity.getForeignKeys());
        org.junit.Assert.assertEquals(foreignKeys, updateEntity2.getForeignKeys());
        org.junit.Assert.assertEquals(org.apache.ambari.server.controller.spi.RequestStatus.Status.Complete, response.getStatus());
        EasyMock.verify(dao, em, controller, request, clusters, cluster, entity, entity2);
    }

    @org.junit.Test
    public void testDeleteResources() throws java.lang.Exception {
        org.easymock.Capture<org.apache.ambari.server.orm.entities.ArtifactEntity> deleteEntityCapture = EasyMock.newCapture();
        org.easymock.Capture<org.apache.ambari.server.orm.entities.ArtifactEntity> deleteEntityCapture2 = EasyMock.newCapture();
        java.util.TreeMap<java.lang.String, java.lang.String> foreignKeys = new java.util.TreeMap<>();
        foreignKeys.put("cluster", "500");
        java.util.List<org.apache.ambari.server.orm.entities.ArtifactEntity> entities = new java.util.ArrayList<>();
        entities.add(entity);
        entities.add(entity2);
        java.util.Map<java.lang.String, java.lang.Object> artifact_data = java.util.Collections.singletonMap("foo", "bar");
        java.util.Map<java.lang.String, java.lang.Object> artifact_data2 = java.util.Collections.singletonMap("foo2", "bar2");
        java.util.Map<java.lang.String, java.lang.String> responseForeignKeys = new java.util.HashMap<>();
        responseForeignKeys.put("cluster", "500");
        EasyMock.expect(controller.getClusters()).andReturn(clusters).anyTimes();
        EasyMock.expect(clusters.getCluster("test-cluster")).andReturn(cluster).anyTimes();
        EasyMock.expect(clusters.getClusterById(500L)).andReturn(cluster).anyTimes();
        EasyMock.expect(cluster.getClusterId()).andReturn(500L).anyTimes();
        EasyMock.expect(cluster.getClusterName()).andReturn("test-cluster").anyTimes();
        EasyMock.expect(entity.getArtifactName()).andReturn("test-artifact").anyTimes();
        EasyMock.expect(entity.getForeignKeys()).andReturn(responseForeignKeys).anyTimes();
        EasyMock.expect(entity.getArtifactData()).andReturn(artifact_data).anyTimes();
        EasyMock.expect(entity2.getArtifactName()).andReturn("test-artifact2").anyTimes();
        EasyMock.expect(entity2.getForeignKeys()).andReturn(responseForeignKeys).anyTimes();
        EasyMock.expect(entity2.getArtifactData()).andReturn(artifact_data2).anyTimes();
        org.easymock.IAnswer<org.apache.ambari.server.orm.entities.ArtifactEntity> findByNameAndForeignKeys = new org.easymock.IAnswer<org.apache.ambari.server.orm.entities.ArtifactEntity>() {
            @java.lang.Override
            public org.apache.ambari.server.orm.entities.ArtifactEntity answer() throws java.lang.Throwable {
                java.lang.String artifactName = ((java.lang.String) (EasyMock.getCurrentArguments()[0]));
                if ("test-artifact".equals(artifactName)) {
                    return entity;
                } else if ("test-artifact2".equals(artifactName)) {
                    return entity2;
                } else {
                    return null;
                }
            }
        };
        EasyMock.expect(dao.findByForeignKeys(EasyMock.eq(foreignKeys))).andReturn(entities).once();
        EasyMock.expect(dao.findByNameAndForeignKeys(EasyMock.anyString(), EasyMock.eq(foreignKeys))).andAnswer(findByNameAndForeignKeys).once();
        dao.remove(EasyMock.capture(deleteEntityCapture));
        EasyMock.expect(dao.findByNameAndForeignKeys(EasyMock.anyString(), EasyMock.eq(foreignKeys))).andAnswer(findByNameAndForeignKeys).once();
        dao.remove(EasyMock.capture(deleteEntityCapture2));
        EasyMock.replay(dao, em, controller, request, clusters, cluster, entity, entity2);
        org.apache.ambari.server.controller.utilities.PredicateBuilder pb = new org.apache.ambari.server.controller.utilities.PredicateBuilder();
        org.apache.ambari.server.controller.spi.Predicate predicate = pb.begin().property("Artifacts/cluster_name").equals("test-cluster").end().toPredicate();
        org.apache.ambari.server.controller.spi.RequestStatus response = resourceProvider.deleteResources(new org.apache.ambari.server.controller.internal.RequestImpl(null, null, null, null), predicate);
        org.apache.ambari.server.orm.entities.ArtifactEntity deleteEntity = deleteEntityCapture.getValue();
        org.apache.ambari.server.orm.entities.ArtifactEntity deleteEntity2 = deleteEntityCapture2.getValue();
        if (deleteEntity.getArtifactName().equals("test-artifact")) {
            org.junit.Assert.assertEquals("test-artifact2", deleteEntity2.getArtifactName());
        } else if (deleteEntity.getArtifactName().equals("test-artifact2")) {
            org.junit.Assert.assertEquals("test-artifact", deleteEntity2.getArtifactName());
        } else {
            org.junit.Assert.fail("Unexpected artifact name: " + deleteEntity.getArtifactName());
        }
        org.junit.Assert.assertEquals(foreignKeys, deleteEntity.getForeignKeys());
        org.junit.Assert.assertEquals(foreignKeys, deleteEntity2.getForeignKeys());
        org.junit.Assert.assertEquals(org.apache.ambari.server.controller.spi.RequestStatus.Status.Complete, response.getStatus());
        EasyMock.verify(dao, em, controller, request, clusters, cluster, entity, entity2);
    }

    private void setPrivateField(java.lang.Object o, java.lang.String field, java.lang.Object value) throws java.lang.Exception {
        java.lang.Class<?> c = o.getClass();
        java.lang.reflect.Field f = c.getDeclaredField(field);
        f.setAccessible(true);
        f.set(o, value);
    }

    private java.lang.String bodyJson = "{ " + (((((((((((((((((((((((((((((((("  \"artifact_data\" : {" + "    \"foo\" : \"bar\",") + "    \"child\" : {") + "      \"childKey\" : \"childValue\",") + "      \"child2\" : {") + "        \"child2Key\" : \"child2Value\",") + "        \"child3\" : {") + "          \"child4\" : {") + "            \"child4Key\" : \"child4Value\"") + "          }") + "        }") + "      }") + "    },") + "    \"collection\" : [") + "      {") + "        \"child\" : {") + "          \"childKey\" : \"childValue\",") + "          \"child2\" : {") + "            \"child2Key\" : \"child2Value\",") + "            \"child3\" : {") + "              \"child4\" : {") + "                \"child4Key\" : \"child4Value\"") + "              }") + "            }") + "          }") + "        }") + "      },") + "      {") + "        \"child4Key\" : \"child4Value\"") + "      } ") + "    ]") + "  }") + "}");
}