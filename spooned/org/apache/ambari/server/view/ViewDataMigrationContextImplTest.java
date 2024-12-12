package org.apache.ambari.server.view;
import org.easymock.Capture;
import static org.easymock.EasyMock.capture;
import static org.easymock.EasyMock.createNiceMock;
import static org.easymock.EasyMock.createStrictMock;
import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.isNull;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
public class ViewDataMigrationContextImplTest {
    public static final java.lang.String VERSION_1 = "1.0.0";

    public static final java.lang.String VERSION_2 = "2.0.0";

    public static final java.lang.String INSTANCE = "INSTANCE_1";

    public static final java.lang.String VIEW_NAME = "MY_VIEW";

    @org.junit.Test
    public void getDataVersion() throws java.lang.Exception {
        org.apache.ambari.server.orm.entities.ViewEntity entity1 = getViewEntityMock(org.apache.ambari.server.view.ViewDataMigrationContextImplTest.VERSION_1);
        org.apache.ambari.server.orm.entities.ViewEntity entity2 = getViewEntityMock(org.apache.ambari.server.view.ViewDataMigrationContextImplTest.VERSION_2);
        org.apache.ambari.server.view.configuration.ViewConfig config1 = EasyMock.createNiceMock(org.apache.ambari.server.view.configuration.ViewConfig.class);
        EasyMock.expect(config1.getDataVersion()).andReturn(41);
        org.apache.ambari.server.view.configuration.ViewConfig config2 = EasyMock.createNiceMock(org.apache.ambari.server.view.configuration.ViewConfig.class);
        EasyMock.expect(config2.getDataVersion()).andReturn(42);
        EasyMock.replay(config1, config2);
        EasyMock.expect(entity1.getConfiguration()).andReturn(config1);
        EasyMock.expect(entity2.getConfiguration()).andReturn(config2);
        EasyMock.replay(entity1, entity2);
        org.apache.ambari.server.orm.entities.ViewInstanceEntity instanceEntity1 = getViewInstanceEntityMock(entity1);
        org.apache.ambari.server.orm.entities.ViewInstanceEntity instanceEntity2 = getViewInstanceEntityMock(entity2);
        EasyMock.replay(instanceEntity1, instanceEntity2);
        org.apache.ambari.server.view.ViewDataMigrationContextImpl context = new org.apache.ambari.server.view.ViewDataMigrationContextImplTest.TestViewDataMigrationContextImpl(instanceEntity1, instanceEntity2);
        junit.framework.Assert.assertEquals(41, context.getOriginDataVersion());
        junit.framework.Assert.assertEquals(42, context.getCurrentDataVersion());
    }

    @org.junit.Test
    public void getDataStore() throws java.lang.Exception {
        org.apache.ambari.server.orm.entities.ViewEntity entity1 = getViewEntityMock(org.apache.ambari.server.view.ViewDataMigrationContextImplTest.VERSION_1);
        org.apache.ambari.server.orm.entities.ViewEntity entity2 = getViewEntityMock(org.apache.ambari.server.view.ViewDataMigrationContextImplTest.VERSION_2);
        EasyMock.replay(entity1, entity2);
        org.apache.ambari.server.orm.entities.ViewInstanceEntity instanceEntity1 = getViewInstanceEntityMock(entity1);
        org.apache.ambari.server.orm.entities.ViewInstanceEntity instanceEntity2 = getViewInstanceEntityMock(entity2);
        EasyMock.replay(instanceEntity1, instanceEntity2);
        org.apache.ambari.server.view.ViewDataMigrationContextImpl context = new org.apache.ambari.server.view.ViewDataMigrationContextImplTest.TestViewDataMigrationContextImpl(instanceEntity1, instanceEntity2);
        junit.framework.Assert.assertNotSame(context.getCurrentDataStore(), context.getOriginDataStore());
    }

    @org.junit.Test
    public void putCurrentInstanceData() throws java.lang.Exception {
        org.apache.ambari.server.orm.entities.ViewEntity entity1 = getViewEntityMock(org.apache.ambari.server.view.ViewDataMigrationContextImplTest.VERSION_1);
        org.apache.ambari.server.orm.entities.ViewEntity entity2 = getViewEntityMock(org.apache.ambari.server.view.ViewDataMigrationContextImplTest.VERSION_2);
        EasyMock.replay(entity1, entity2);
        org.apache.ambari.server.orm.entities.ViewInstanceEntity instanceEntity1 = getViewInstanceEntityMock(entity1);
        java.util.List<org.apache.ambari.server.orm.entities.ViewInstanceDataEntity> data1 = new java.util.ArrayList<>();
        EasyMock.expect(instanceEntity1.getData()).andReturn(data1).anyTimes();
        org.apache.ambari.server.orm.entities.ViewInstanceEntity instanceEntity2 = getViewInstanceEntityMock(entity2);
        java.util.List<org.apache.ambari.server.orm.entities.ViewInstanceDataEntity> data2 = new java.util.ArrayList<>();
        EasyMock.expect(instanceEntity2.getData()).andReturn(data2).anyTimes();
        EasyMock.replay(instanceEntity1, instanceEntity2);
        org.apache.ambari.server.view.ViewDataMigrationContextImpl context = new org.apache.ambari.server.view.ViewDataMigrationContextImplTest.TestViewDataMigrationContextImpl(instanceEntity1, instanceEntity2);
        context.putOriginInstanceData("user1", "key1", "val1");
        context.putCurrentInstanceData("user2", "key2", "val2");
        junit.framework.Assert.assertEquals(1, data1.size());
        junit.framework.Assert.assertEquals("user1", data1.get(0).getUser());
        junit.framework.Assert.assertEquals("key1", data1.get(0).getName());
        junit.framework.Assert.assertEquals("val1", data1.get(0).getValue());
        junit.framework.Assert.assertEquals(1, data2.size());
        junit.framework.Assert.assertEquals("user2", data2.get(0).getUser());
        junit.framework.Assert.assertEquals("key2", data2.get(0).getName());
        junit.framework.Assert.assertEquals("val2", data2.get(0).getValue());
    }

    @org.junit.Test
    public void copyAllObjects() throws java.lang.Exception {
        org.apache.ambari.server.orm.entities.ViewEntity entity1 = getViewEntityMock(org.apache.ambari.server.view.ViewDataMigrationContextImplTest.VERSION_1);
        org.apache.ambari.server.orm.entities.ViewEntity entity2 = getViewEntityMock(org.apache.ambari.server.view.ViewDataMigrationContextImplTest.VERSION_2);
        EasyMock.replay(entity1, entity2);
        org.apache.ambari.server.orm.entities.ViewInstanceEntity instanceEntity1 = getViewInstanceEntityMock(entity1);
        org.apache.ambari.server.orm.entities.ViewInstanceEntity instanceEntity2 = getViewInstanceEntityMock(entity2);
        EasyMock.replay(instanceEntity1, instanceEntity2);
        org.apache.ambari.server.view.ViewDataMigrationContextImplTest.TestViewDataMigrationContextImpl context = new org.apache.ambari.server.view.ViewDataMigrationContextImplTest.TestViewDataMigrationContextImpl(instanceEntity1, instanceEntity2);
        org.apache.ambari.view.DataStore dataStore1 = EasyMock.createStrictMock(org.apache.ambari.view.DataStore.class);
        EasyMock.expect(dataStore1.findAll(EasyMock.eq(org.apache.ambari.server.view.ViewDataMigrationContextImplTest.SampleEntity.class), ((java.lang.String) (EasyMock.isNull())))).andReturn(java.util.Arrays.asList(new org.apache.ambari.server.view.ViewDataMigrationContextImplTest.SampleEntity("data1"), new org.apache.ambari.server.view.ViewDataMigrationContextImplTest.SampleEntity("data2")));
        EasyMock.replay(dataStore1);
        org.apache.ambari.view.DataStore dataStore2 = EasyMock.createStrictMock(org.apache.ambari.view.DataStore.class);
        org.easymock.Capture<org.apache.ambari.server.view.ViewDataMigrationContextImplTest.SampleEntity> copiedEntity1 = org.easymock.Capture.newInstance();
        org.easymock.Capture<org.apache.ambari.server.view.ViewDataMigrationContextImplTest.SampleEntity> copiedEntity2 = org.easymock.Capture.newInstance();
        dataStore2.store(EasyMock.capture(copiedEntity1));
        EasyMock.expectLastCall();
        dataStore2.store(EasyMock.capture(copiedEntity2));
        EasyMock.expectLastCall();
        EasyMock.replay(dataStore2);
        context.setMockOriginDataStore(dataStore1);
        context.setMockCurrentDataStore(dataStore2);
        context.copyAllObjects(org.apache.ambari.server.view.ViewDataMigrationContextImplTest.SampleEntity.class, org.apache.ambari.server.view.ViewDataMigrationContextImplTest.SampleEntity.class);
        EasyMock.verify(dataStore1);
        EasyMock.verify(dataStore2);
        junit.framework.Assert.assertEquals("data1", copiedEntity1.getValue().getField());
        junit.framework.Assert.assertEquals("data2", copiedEntity2.getValue().getField());
    }

    @org.junit.Test
    public void copyAllObjectsWithCustomConverter() throws java.lang.Exception {
        org.apache.ambari.server.orm.entities.ViewEntity entity1 = getViewEntityMock(org.apache.ambari.server.view.ViewDataMigrationContextImplTest.VERSION_1);
        org.apache.ambari.server.orm.entities.ViewEntity entity2 = getViewEntityMock(org.apache.ambari.server.view.ViewDataMigrationContextImplTest.VERSION_2);
        EasyMock.replay(entity1, entity2);
        org.apache.ambari.server.orm.entities.ViewInstanceEntity instanceEntity1 = getViewInstanceEntityMock(entity1);
        org.apache.ambari.server.orm.entities.ViewInstanceEntity instanceEntity2 = getViewInstanceEntityMock(entity2);
        EasyMock.replay(instanceEntity1, instanceEntity2);
        org.apache.ambari.server.view.ViewDataMigrationContextImplTest.TestViewDataMigrationContextImpl context = new org.apache.ambari.server.view.ViewDataMigrationContextImplTest.TestViewDataMigrationContextImpl(instanceEntity1, instanceEntity2);
        org.apache.ambari.view.DataStore dataStore1 = EasyMock.createStrictMock(org.apache.ambari.view.DataStore.class);
        org.apache.ambari.server.view.ViewDataMigrationContextImplTest.SampleEntity sampleEntity1 = new org.apache.ambari.server.view.ViewDataMigrationContextImplTest.SampleEntity("data1");
        org.apache.ambari.server.view.ViewDataMigrationContextImplTest.SampleEntity sampleEntity2 = new org.apache.ambari.server.view.ViewDataMigrationContextImplTest.SampleEntity("data2");
        EasyMock.expect(dataStore1.findAll(EasyMock.eq(org.apache.ambari.server.view.ViewDataMigrationContextImplTest.SampleEntity.class), ((java.lang.String) (EasyMock.isNull())))).andReturn(java.util.Arrays.asList(sampleEntity1, sampleEntity2));
        EasyMock.replay(dataStore1);
        org.apache.ambari.view.DataStore dataStore2 = EasyMock.createStrictMock(org.apache.ambari.view.DataStore.class);
        org.easymock.Capture<org.apache.ambari.server.view.ViewDataMigrationContextImplTest.SampleEntity> copiedEntity1 = org.easymock.Capture.newInstance();
        org.easymock.Capture<org.apache.ambari.server.view.ViewDataMigrationContextImplTest.SampleEntity> copiedEntity2 = org.easymock.Capture.newInstance();
        org.easymock.Capture<org.apache.ambari.server.view.ViewDataMigrationContextImplTest.SampleEntity> convertedEntity1 = org.easymock.Capture.newInstance();
        org.easymock.Capture<org.apache.ambari.server.view.ViewDataMigrationContextImplTest.SampleEntity> convertedEntity2 = org.easymock.Capture.newInstance();
        dataStore2.store(EasyMock.capture(copiedEntity1));
        EasyMock.expectLastCall();
        dataStore2.store(EasyMock.capture(copiedEntity2));
        EasyMock.expectLastCall();
        EasyMock.replay(dataStore2);
        context.setMockOriginDataStore(dataStore1);
        context.setMockCurrentDataStore(dataStore2);
        org.apache.ambari.view.migration.EntityConverter converter = EasyMock.createStrictMock(org.apache.ambari.view.migration.EntityConverter.class);
        converter.convert(EasyMock.eq(sampleEntity1), EasyMock.capture(convertedEntity1));
        EasyMock.expectLastCall();
        converter.convert(EasyMock.eq(sampleEntity2), EasyMock.capture(convertedEntity2));
        EasyMock.expectLastCall();
        EasyMock.replay(converter);
        context.copyAllObjects(org.apache.ambari.server.view.ViewDataMigrationContextImplTest.SampleEntity.class, org.apache.ambari.server.view.ViewDataMigrationContextImplTest.SampleEntity.class, converter);
        EasyMock.verify(dataStore1);
        EasyMock.verify(dataStore2);
        EasyMock.verify(converter);
        junit.framework.Assert.assertSame(copiedEntity1.getValue(), convertedEntity1.getValue());
        junit.framework.Assert.assertSame(copiedEntity2.getValue(), convertedEntity2.getValue());
    }

    @org.junit.Test
    public void copyAllInstanceData() throws java.lang.Exception {
        org.apache.ambari.server.orm.entities.ViewEntity entity1 = getViewEntityMock(org.apache.ambari.server.view.ViewDataMigrationContextImplTest.VERSION_1);
        org.apache.ambari.server.orm.entities.ViewEntity entity2 = getViewEntityMock(org.apache.ambari.server.view.ViewDataMigrationContextImplTest.VERSION_2);
        EasyMock.replay(entity1, entity2);
        org.apache.ambari.server.orm.entities.ViewInstanceDataEntity dataEntity = new org.apache.ambari.server.orm.entities.ViewInstanceDataEntity();
        dataEntity.setName("name1");
        dataEntity.setValue("value1");
        dataEntity.setUser("user1");
        java.util.Collection<org.apache.ambari.server.orm.entities.ViewInstanceDataEntity> data1 = java.util.Arrays.asList(dataEntity);
        java.util.List<org.apache.ambari.server.orm.entities.ViewInstanceDataEntity> data2 = new java.util.ArrayList<>();
        org.apache.ambari.server.orm.entities.ViewInstanceEntity instanceEntity1 = getViewInstanceEntityMock(entity1);
        EasyMock.expect(instanceEntity1.getData()).andReturn(data1).anyTimes();
        org.apache.ambari.server.orm.entities.ViewInstanceEntity instanceEntity2 = getViewInstanceEntityMock(entity2);
        EasyMock.expect(instanceEntity2.getData()).andReturn(data2).anyTimes();
        EasyMock.replay(instanceEntity1, instanceEntity2);
        org.apache.ambari.server.view.ViewDataMigrationContextImpl context = new org.apache.ambari.server.view.ViewDataMigrationContextImplTest.TestViewDataMigrationContextImpl(instanceEntity1, instanceEntity2);
        context.copyAllInstanceData();
        junit.framework.Assert.assertEquals(1, data2.size());
        junit.framework.Assert.assertEquals("user1", data2.get(0).getUser());
        junit.framework.Assert.assertEquals("name1", data2.get(0).getName());
        junit.framework.Assert.assertEquals("value1", data2.get(0).getValue());
    }

    @org.junit.Test
    public void getEntityClasses() throws java.lang.Exception {
        org.apache.ambari.server.orm.entities.ViewEntity entity1 = getViewEntityMock(org.apache.ambari.server.view.ViewDataMigrationContextImplTest.VERSION_1);
        org.apache.ambari.server.orm.entities.ViewEntity entity2 = getViewEntityMock(org.apache.ambari.server.view.ViewDataMigrationContextImplTest.VERSION_2);
        org.apache.ambari.server.view.configuration.EntityConfig entityConfig = EasyMock.createNiceMock(org.apache.ambari.server.view.configuration.EntityConfig.class);
        EasyMock.expect(entityConfig.getClassName()).andReturn(org.apache.ambari.server.view.ViewDataMigrationContextImplTest.SampleEntity.class.getName()).anyTimes();
        EasyMock.replay(entityConfig);
        org.apache.ambari.server.view.configuration.PersistenceConfig persistenceConfig1 = EasyMock.createStrictMock(org.apache.ambari.server.view.configuration.PersistenceConfig.class);
        EasyMock.expect(persistenceConfig1.getEntities()).andReturn(java.util.Arrays.asList(entityConfig));
        org.apache.ambari.server.view.configuration.PersistenceConfig persistenceConfig2 = EasyMock.createStrictMock(org.apache.ambari.server.view.configuration.PersistenceConfig.class);
        EasyMock.expect(persistenceConfig2.getEntities()).andReturn(java.util.Arrays.asList(entityConfig));
        EasyMock.replay(persistenceConfig1, persistenceConfig2);
        org.apache.ambari.server.view.configuration.ViewConfig config1 = EasyMock.createNiceMock(org.apache.ambari.server.view.configuration.ViewConfig.class);
        EasyMock.expect(config1.getPersistence()).andReturn(persistenceConfig1);
        org.apache.ambari.server.view.configuration.ViewConfig config2 = EasyMock.createNiceMock(org.apache.ambari.server.view.configuration.ViewConfig.class);
        EasyMock.expect(config2.getPersistence()).andReturn(persistenceConfig2);
        EasyMock.replay(config1, config2);
        EasyMock.expect(entity1.getConfiguration()).andReturn(config1);
        EasyMock.expect(entity2.getConfiguration()).andReturn(config2);
        EasyMock.replay(entity1, entity2);
        org.apache.ambari.server.orm.entities.ViewInstanceEntity instanceEntity1 = getViewInstanceEntityMock(entity1);
        org.apache.ambari.server.orm.entities.ViewInstanceEntity instanceEntity2 = getViewInstanceEntityMock(entity2);
        EasyMock.replay(instanceEntity1, instanceEntity2);
        org.apache.ambari.server.view.ViewDataMigrationContextImpl context = new org.apache.ambari.server.view.ViewDataMigrationContextImplTest.TestViewDataMigrationContextImpl(instanceEntity1, instanceEntity2);
        java.util.Map<java.lang.String, java.lang.Class> current = context.getCurrentEntityClasses();
        junit.framework.Assert.assertEquals(1, current.size());
        junit.framework.Assert.assertEquals(org.apache.ambari.server.view.ViewDataMigrationContextImplTest.SampleEntity.class.getName(), current.entrySet().iterator().next().getKey());
        junit.framework.Assert.assertEquals(org.apache.ambari.server.view.ViewDataMigrationContextImplTest.SampleEntity.class, current.entrySet().iterator().next().getValue());
        java.util.Map<java.lang.String, java.lang.Class> origin = context.getOriginEntityClasses();
        junit.framework.Assert.assertEquals(1, origin.size());
        junit.framework.Assert.assertEquals(org.apache.ambari.server.view.ViewDataMigrationContextImplTest.SampleEntity.class.getName(), origin.entrySet().iterator().next().getKey());
        junit.framework.Assert.assertEquals(org.apache.ambari.server.view.ViewDataMigrationContextImplTest.SampleEntity.class, origin.entrySet().iterator().next().getValue());
    }

    @org.junit.Test
    public void getInstanceDataByUser() throws java.lang.Exception {
        org.apache.ambari.server.orm.entities.ViewEntity entity1 = getViewEntityMock(org.apache.ambari.server.view.ViewDataMigrationContextImplTest.VERSION_1);
        org.apache.ambari.server.orm.entities.ViewEntity entity2 = getViewEntityMock(org.apache.ambari.server.view.ViewDataMigrationContextImplTest.VERSION_2);
        EasyMock.replay(entity1, entity2);
        org.apache.ambari.server.orm.entities.ViewInstanceDataEntity dataEntityUser1 = new org.apache.ambari.server.orm.entities.ViewInstanceDataEntity();
        dataEntityUser1.setName("key1");
        dataEntityUser1.setUser("user1");
        org.apache.ambari.server.orm.entities.ViewInstanceDataEntity dataEntityUser2 = new org.apache.ambari.server.orm.entities.ViewInstanceDataEntity();
        dataEntityUser2.setName("key1");
        dataEntityUser2.setUser("user2");
        org.apache.ambari.server.orm.entities.ViewInstanceDataEntity dataEntity2User2 = new org.apache.ambari.server.orm.entities.ViewInstanceDataEntity();
        dataEntity2User2.setName("key2");
        dataEntity2User2.setUser("user2");
        java.util.Collection<org.apache.ambari.server.orm.entities.ViewInstanceDataEntity> data2 = java.util.Arrays.asList(dataEntityUser2, dataEntity2User2);
        java.util.Collection<org.apache.ambari.server.orm.entities.ViewInstanceDataEntity> data1 = java.util.Arrays.asList(dataEntityUser1, dataEntityUser2, dataEntity2User2);
        org.apache.ambari.server.orm.entities.ViewInstanceEntity instanceEntity1 = getViewInstanceEntityMock(entity1);
        EasyMock.expect(instanceEntity1.getData()).andReturn(data1);
        org.apache.ambari.server.orm.entities.ViewInstanceEntity instanceEntity2 = getViewInstanceEntityMock(entity2);
        EasyMock.expect(instanceEntity2.getData()).andReturn(data2);
        EasyMock.replay(instanceEntity1, instanceEntity2);
        org.apache.ambari.server.view.ViewDataMigrationContextImpl context = new org.apache.ambari.server.view.ViewDataMigrationContextImplTest.TestViewDataMigrationContextImpl(instanceEntity1, instanceEntity2);
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> instanceData2 = context.getCurrentInstanceDataByUser();
        junit.framework.Assert.assertEquals(1, instanceData2.size());
        junit.framework.Assert.assertEquals(2, instanceData2.get("user2").size());
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> instanceData1 = context.getOriginInstanceDataByUser();
        junit.framework.Assert.assertEquals(2, instanceData1.size());
        junit.framework.Assert.assertEquals(1, instanceData1.get("user1").size());
        junit.framework.Assert.assertEquals(2, instanceData1.get("user2").size());
    }

    private org.apache.ambari.server.orm.entities.ViewInstanceEntity getViewInstanceEntityMock(org.apache.ambari.server.orm.entities.ViewEntity viewEntity) {
        org.apache.ambari.server.orm.entities.ViewInstanceEntity instanceEntity = EasyMock.createNiceMock(org.apache.ambari.server.orm.entities.ViewInstanceEntity.class);
        EasyMock.expect(instanceEntity.getViewEntity()).andReturn(viewEntity).anyTimes();
        EasyMock.expect(instanceEntity.getViewName()).andReturn(org.apache.ambari.server.view.ViewDataMigrationContextImplTest.VIEW_NAME).anyTimes();
        EasyMock.expect(instanceEntity.getInstanceName()).andReturn(org.apache.ambari.server.view.ViewDataMigrationContextImplTest.INSTANCE).anyTimes();
        return instanceEntity;
    }

    private org.apache.ambari.server.orm.entities.ViewEntity getViewEntityMock(java.lang.String version) {
        org.apache.ambari.server.orm.entities.ViewEntity viewEntity = EasyMock.createNiceMock(org.apache.ambari.server.orm.entities.ViewEntity.class);
        EasyMock.expect(viewEntity.getViewName()).andReturn(org.apache.ambari.server.view.ViewDataMigrationContextImplTest.VIEW_NAME).anyTimes();
        EasyMock.expect(viewEntity.getVersion()).andReturn(version).anyTimes();
        EasyMock.expect(viewEntity.getClassLoader()).andReturn(getClass().getClassLoader()).anyTimes();
        return viewEntity;
    }

    private static class TestViewDataMigrationContextImpl extends org.apache.ambari.server.view.ViewDataMigrationContextImpl {
        private org.apache.ambari.view.DataStore mockOriginDataStore;

        private org.apache.ambari.view.DataStore mockCurrentDataStore;

        public TestViewDataMigrationContextImpl(org.apache.ambari.server.orm.entities.ViewInstanceEntity originInstanceDefinition, org.apache.ambari.server.orm.entities.ViewInstanceEntity currentInstanceDefinition) {
            super(originInstanceDefinition, currentInstanceDefinition);
        }

        @java.lang.Override
        protected org.apache.ambari.view.DataStore getDataStore(org.apache.ambari.server.orm.entities.ViewInstanceEntity instanceDefinition) {
            if (instanceDefinition.getViewEntity().getVersion().equals(org.apache.ambari.server.view.ViewDataMigrationContextImplTest.VERSION_1)) {
                if (mockOriginDataStore == null) {
                    return createDataStoreMock();
                }
                return mockOriginDataStore;
            }
            if (instanceDefinition.getViewEntity().getVersion().equals(org.apache.ambari.server.view.ViewDataMigrationContextImplTest.VERSION_2)) {
                if (mockCurrentDataStore == null) {
                    return createDataStoreMock();
                }
                return mockCurrentDataStore;
            }
            return null;
        }

        private org.apache.ambari.view.DataStore createDataStoreMock() {
            org.apache.ambari.view.DataStore dataStoreMock = EasyMock.createNiceMock(org.apache.ambari.view.DataStore.class);
            EasyMock.replay(dataStoreMock);
            return dataStoreMock;
        }

        public org.apache.ambari.view.DataStore getMockOriginDataStore() {
            return mockOriginDataStore;
        }

        public void setMockOriginDataStore(org.apache.ambari.view.DataStore mockOriginDataStore) {
            this.mockOriginDataStore = mockOriginDataStore;
        }

        public org.apache.ambari.view.DataStore getMockCurrentDataStore() {
            return mockCurrentDataStore;
        }

        public void setMockCurrentDataStore(org.apache.ambari.view.DataStore mockCurrentDataStore) {
            this.mockCurrentDataStore = mockCurrentDataStore;
        }
    }

    private static class SampleEntity {
        private java.lang.String field;

        public SampleEntity() {
        }

        public SampleEntity(java.lang.String field) {
            this.field = field;
        }

        public java.lang.String getField() {
            return field;
        }

        public void setField(java.lang.String field) {
            this.field = field;
        }
    }
}