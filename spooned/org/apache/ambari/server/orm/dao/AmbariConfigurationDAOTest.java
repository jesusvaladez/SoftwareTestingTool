package org.apache.ambari.server.orm.dao;
import javax.persistence.EntityManager;
import org.easymock.Capture;
import org.easymock.CaptureType;
import org.easymock.EasyMockSupport;
import static org.easymock.EasyMock.capture;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.newCapture;
public class AmbariConfigurationDAOTest extends org.easymock.EasyMockSupport {
    private static final java.lang.String CATEGORY_NAME = "test-category";

    private static java.lang.reflect.Method methodMerge;

    private static java.lang.reflect.Method methodRemove;

    private static java.lang.reflect.Method methodCreate;

    private static java.lang.reflect.Method methodFindByCategory;

    private static java.lang.reflect.Field fieldEntityManagerProvider;

    @org.junit.BeforeClass
    public static void beforeKDCKerberosOperationHandlerTest() throws java.lang.Exception {
        org.apache.ambari.server.orm.dao.AmbariConfigurationDAOTest.methodMerge = org.apache.ambari.server.orm.dao.AmbariConfigurationDAO.class.getMethod("merge", org.apache.ambari.server.orm.entities.AmbariConfigurationEntity.class);
        org.apache.ambari.server.orm.dao.AmbariConfigurationDAOTest.methodRemove = org.apache.ambari.server.orm.dao.CrudDAO.class.getMethod("remove", java.lang.Object.class);
        org.apache.ambari.server.orm.dao.AmbariConfigurationDAOTest.methodCreate = org.apache.ambari.server.orm.dao.AmbariConfigurationDAO.class.getMethod("create", org.apache.ambari.server.orm.entities.AmbariConfigurationEntity.class);
        org.apache.ambari.server.orm.dao.AmbariConfigurationDAOTest.methodFindByCategory = org.apache.ambari.server.orm.dao.AmbariConfigurationDAO.class.getMethod("findByCategory", java.lang.String.class);
        org.apache.ambari.server.orm.dao.AmbariConfigurationDAOTest.fieldEntityManagerProvider = org.apache.ambari.server.orm.dao.CrudDAO.class.getDeclaredField("entityManagerProvider");
    }

    @org.junit.Test
    public void testReconcileCategoryNewCategory() throws java.lang.Exception {
        org.easymock.Capture<org.apache.ambari.server.orm.entities.AmbariConfigurationEntity> capturedEntities = EasyMock.newCapture(CaptureType.ALL);
        org.apache.ambari.server.orm.dao.AmbariConfigurationDAO dao = createDao();
        EasyMock.expect(dao.findByCategory(org.apache.ambari.server.orm.dao.AmbariConfigurationDAOTest.CATEGORY_NAME)).andReturn(null).once();
        dao.create(EasyMock.capture(capturedEntities));
        EasyMock.expectLastCall().anyTimes();
        replayAll();
        java.util.Map<java.lang.String, java.lang.String> properties;
        properties = new java.util.HashMap<>();
        properties.put("property1", "value1");
        properties.put("property2", "value2");
        dao.reconcileCategory(org.apache.ambari.server.orm.dao.AmbariConfigurationDAOTest.CATEGORY_NAME, properties, true);
        verifyAll();
        validateCapturedEntities(org.apache.ambari.server.orm.dao.AmbariConfigurationDAOTest.CATEGORY_NAME, properties, capturedEntities);
    }

    @org.junit.Test
    public void testReconcileCategoryReplaceCategory() throws java.lang.Exception {
        java.util.Map<java.lang.String, java.lang.String> existingProperties;
        existingProperties = new java.util.HashMap<>();
        existingProperties.put("property1", "value1");
        existingProperties.put("property2", "value2");
        org.easymock.Capture<org.apache.ambari.server.orm.entities.AmbariConfigurationEntity> capturedCreatedEntities = EasyMock.newCapture(CaptureType.ALL);
        org.easymock.Capture<org.apache.ambari.server.orm.entities.AmbariConfigurationEntity> capturedRemovedEntities = EasyMock.newCapture(CaptureType.ALL);
        org.apache.ambari.server.orm.dao.AmbariConfigurationDAO dao = createDao();
        EasyMock.expect(dao.findByCategory(org.apache.ambari.server.orm.dao.AmbariConfigurationDAOTest.CATEGORY_NAME)).andReturn(toEntities(org.apache.ambari.server.orm.dao.AmbariConfigurationDAOTest.CATEGORY_NAME, existingProperties)).once();
        dao.remove(EasyMock.capture(capturedRemovedEntities));
        EasyMock.expectLastCall().anyTimes();
        dao.create(EasyMock.capture(capturedCreatedEntities));
        EasyMock.expectLastCall().anyTimes();
        replayAll();
        java.util.Map<java.lang.String, java.lang.String> newProperties;
        newProperties = new java.util.HashMap<>();
        newProperties.put("property1_new", "value1");
        newProperties.put("property2_new", "value2");
        dao.reconcileCategory(org.apache.ambari.server.orm.dao.AmbariConfigurationDAOTest.CATEGORY_NAME, newProperties, true);
        verifyAll();
        validateCapturedEntities(org.apache.ambari.server.orm.dao.AmbariConfigurationDAOTest.CATEGORY_NAME, newProperties, capturedCreatedEntities);
        validateCapturedEntities(org.apache.ambari.server.orm.dao.AmbariConfigurationDAOTest.CATEGORY_NAME, existingProperties, capturedRemovedEntities);
    }

    @org.junit.Test
    public void testReconcileCategoryUpdateCategoryKeepNotSpecified() throws java.lang.Exception {
        java.util.Map<java.lang.String, java.lang.String> existingProperties;
        existingProperties = new java.util.HashMap<>();
        existingProperties.put("property1", "value1");
        existingProperties.put("property2", "value2");
        org.easymock.Capture<org.apache.ambari.server.orm.entities.AmbariConfigurationEntity> capturedCreatedEntities = EasyMock.newCapture(CaptureType.ALL);
        org.easymock.Capture<org.apache.ambari.server.orm.entities.AmbariConfigurationEntity> capturedMergedEntities = EasyMock.newCapture(CaptureType.ALL);
        org.apache.ambari.server.orm.dao.AmbariConfigurationDAO dao = createDao();
        EasyMock.expect(dao.findByCategory(org.apache.ambari.server.orm.dao.AmbariConfigurationDAOTest.CATEGORY_NAME)).andReturn(toEntities(org.apache.ambari.server.orm.dao.AmbariConfigurationDAOTest.CATEGORY_NAME, existingProperties)).once();
        EasyMock.expect(dao.merge(EasyMock.capture(capturedMergedEntities))).andReturn(createNiceMock(org.apache.ambari.server.orm.entities.AmbariConfigurationEntity.class)).anyTimes();
        dao.create(EasyMock.capture(capturedCreatedEntities));
        EasyMock.expectLastCall().anyTimes();
        replayAll();
        java.util.Map<java.lang.String, java.lang.String> newProperties;
        newProperties = new java.util.HashMap<>();
        newProperties.put("property1", "new_value1");
        newProperties.put("property2_new", "value2");
        newProperties.put("property3", "value3");
        dao.reconcileCategory(org.apache.ambari.server.orm.dao.AmbariConfigurationDAOTest.CATEGORY_NAME, newProperties, false);
        verifyAll();
        java.util.Map<java.lang.String, java.lang.String> expectedProperties;
        expectedProperties = new java.util.HashMap<>();
        expectedProperties.put("property2_new", "value2");
        expectedProperties.put("property3", "value3");
        validateCapturedEntities(org.apache.ambari.server.orm.dao.AmbariConfigurationDAOTest.CATEGORY_NAME, expectedProperties, capturedCreatedEntities);
        expectedProperties = new java.util.HashMap<>();
        expectedProperties.put("property1", "new_value1");
        validateCapturedEntities(org.apache.ambari.server.orm.dao.AmbariConfigurationDAOTest.CATEGORY_NAME, expectedProperties, capturedMergedEntities);
    }

    @org.junit.Test
    public void testReconcileCategoryUpdateCategoryRemoveNotSpecified() throws java.lang.Exception {
        java.util.Map<java.lang.String, java.lang.String> existingProperties;
        existingProperties = new java.util.HashMap<>();
        existingProperties.put("property1", "value1");
        existingProperties.put("property2", "value2");
        org.easymock.Capture<org.apache.ambari.server.orm.entities.AmbariConfigurationEntity> capturedCreatedEntities = EasyMock.newCapture(CaptureType.ALL);
        org.easymock.Capture<org.apache.ambari.server.orm.entities.AmbariConfigurationEntity> capturedRemovedEntities = EasyMock.newCapture(CaptureType.ALL);
        org.easymock.Capture<org.apache.ambari.server.orm.entities.AmbariConfigurationEntity> capturedMergedEntities = EasyMock.newCapture(CaptureType.ALL);
        org.apache.ambari.server.orm.dao.AmbariConfigurationDAO dao = createDao();
        EasyMock.expect(dao.findByCategory(org.apache.ambari.server.orm.dao.AmbariConfigurationDAOTest.CATEGORY_NAME)).andReturn(toEntities(org.apache.ambari.server.orm.dao.AmbariConfigurationDAOTest.CATEGORY_NAME, existingProperties)).once();
        EasyMock.expect(dao.merge(EasyMock.capture(capturedMergedEntities))).andReturn(createNiceMock(org.apache.ambari.server.orm.entities.AmbariConfigurationEntity.class)).anyTimes();
        dao.remove(EasyMock.capture(capturedRemovedEntities));
        EasyMock.expectLastCall().anyTimes();
        dao.create(EasyMock.capture(capturedCreatedEntities));
        EasyMock.expectLastCall().anyTimes();
        replayAll();
        java.util.Map<java.lang.String, java.lang.String> newProperties;
        newProperties = new java.util.HashMap<>();
        newProperties.put("property1", "new_value1");
        newProperties.put("property2_new", "value2");
        newProperties.put("property3", "value3");
        dao.reconcileCategory(org.apache.ambari.server.orm.dao.AmbariConfigurationDAOTest.CATEGORY_NAME, newProperties, true);
        verifyAll();
        java.util.Map<java.lang.String, java.lang.String> expectedProperties;
        expectedProperties = new java.util.HashMap<>();
        expectedProperties.put("property2_new", "value2");
        expectedProperties.put("property3", "value3");
        validateCapturedEntities(org.apache.ambari.server.orm.dao.AmbariConfigurationDAOTest.CATEGORY_NAME, expectedProperties, capturedCreatedEntities);
        expectedProperties = new java.util.HashMap<>();
        expectedProperties.put("property2", "value2");
        validateCapturedEntities(org.apache.ambari.server.orm.dao.AmbariConfigurationDAOTest.CATEGORY_NAME, expectedProperties, capturedRemovedEntities);
        expectedProperties = new java.util.HashMap<>();
        expectedProperties.put("property1", "new_value1");
        validateCapturedEntities(org.apache.ambari.server.orm.dao.AmbariConfigurationDAOTest.CATEGORY_NAME, expectedProperties, capturedMergedEntities);
    }

    @org.junit.Test
    public void testReconcileCategoryAppendCategory() throws java.lang.Exception {
        java.util.Map<java.lang.String, java.lang.String> existingProperties;
        existingProperties = new java.util.HashMap<>();
        existingProperties.put("property1", "value1");
        existingProperties.put("property2", "value2");
        org.easymock.Capture<org.apache.ambari.server.orm.entities.AmbariConfigurationEntity> capturedCreatedEntities = EasyMock.newCapture(CaptureType.ALL);
        org.apache.ambari.server.orm.dao.AmbariConfigurationDAO dao = createDao();
        EasyMock.expect(dao.findByCategory(org.apache.ambari.server.orm.dao.AmbariConfigurationDAOTest.CATEGORY_NAME)).andReturn(toEntities(org.apache.ambari.server.orm.dao.AmbariConfigurationDAOTest.CATEGORY_NAME, existingProperties)).once();
        dao.create(EasyMock.capture(capturedCreatedEntities));
        EasyMock.expectLastCall().anyTimes();
        replayAll();
        java.util.Map<java.lang.String, java.lang.String> newProperties;
        newProperties = new java.util.HashMap<>();
        newProperties.put("property3", "value3");
        newProperties.put("property4", "value3");
        dao.reconcileCategory(org.apache.ambari.server.orm.dao.AmbariConfigurationDAOTest.CATEGORY_NAME, newProperties, false);
        verifyAll();
        validateCapturedEntities(org.apache.ambari.server.orm.dao.AmbariConfigurationDAOTest.CATEGORY_NAME, newProperties, capturedCreatedEntities);
    }

    private org.apache.ambari.server.orm.dao.AmbariConfigurationDAO createDao() throws java.lang.IllegalAccessException {
        org.apache.ambari.server.orm.dao.AmbariConfigurationDAO dao = createMockBuilder(org.apache.ambari.server.orm.dao.AmbariConfigurationDAO.class).addMockedMethods(org.apache.ambari.server.orm.dao.AmbariConfigurationDAOTest.methodMerge, org.apache.ambari.server.orm.dao.AmbariConfigurationDAOTest.methodRemove, org.apache.ambari.server.orm.dao.AmbariConfigurationDAOTest.methodCreate, org.apache.ambari.server.orm.dao.AmbariConfigurationDAOTest.methodFindByCategory).createMock();
        javax.persistence.EntityManager entityManager = createMock(javax.persistence.EntityManager.class);
        entityManager.flush();
        EasyMock.expectLastCall().anyTimes();
        com.google.inject.Provider<javax.persistence.EntityManager> entityManagerProvider = createMock(com.google.inject.Provider.class);
        EasyMock.expect(entityManagerProvider.get()).andReturn(entityManager).anyTimes();
        org.apache.ambari.server.orm.dao.AmbariConfigurationDAOTest.fieldEntityManagerProvider.set(dao, entityManagerProvider);
        return dao;
    }

    private java.util.List<org.apache.ambari.server.orm.entities.AmbariConfigurationEntity> toEntities(java.lang.String categoryName, java.util.Map<java.lang.String, java.lang.String> properties) {
        java.util.List<org.apache.ambari.server.orm.entities.AmbariConfigurationEntity> entities = new java.util.ArrayList<>();
        for (java.util.Map.Entry<java.lang.String, java.lang.String> property : properties.entrySet()) {
            org.apache.ambari.server.orm.entities.AmbariConfigurationEntity entity = new org.apache.ambari.server.orm.entities.AmbariConfigurationEntity();
            entity.setCategoryName(categoryName);
            entity.setPropertyName(property.getKey());
            entity.setPropertyValue(property.getValue());
            entities.add(entity);
        }
        return entities;
    }

    private void validateCapturedEntities(java.lang.String expectedCategoryName, java.util.Map<java.lang.String, java.lang.String> expectedProperties, org.easymock.Capture<org.apache.ambari.server.orm.entities.AmbariConfigurationEntity> capturedEntities) {
        junit.framework.Assert.assertTrue(capturedEntities.hasCaptured());
        java.util.List<org.apache.ambari.server.orm.entities.AmbariConfigurationEntity> entities = capturedEntities.getValues();
        junit.framework.Assert.assertNotNull(entities);
        java.util.Map<java.lang.String, java.lang.String> capturedProperties = new java.util.TreeMap<>();
        for (org.apache.ambari.server.orm.entities.AmbariConfigurationEntity entity : entities) {
            junit.framework.Assert.assertEquals(expectedCategoryName, entity.getCategoryName());
            capturedProperties.put(entity.getPropertyName(), entity.getPropertyValue());
        }
        expectedProperties = new java.util.TreeMap<>(expectedProperties);
        junit.framework.Assert.assertEquals(expectedProperties, capturedProperties);
    }
}