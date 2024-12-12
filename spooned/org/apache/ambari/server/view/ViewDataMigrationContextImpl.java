package org.apache.ambari.server.view;
import com.google.inject.persist.Transactional;
public class ViewDataMigrationContextImpl implements org.apache.ambari.view.migration.ViewDataMigrationContext {
    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.view.ViewDataMigrationContextImpl.class);

    private org.apache.ambari.view.DataStore originDataStore;

    private org.apache.ambari.view.DataStore currentDataStore;

    private final org.apache.ambari.server.orm.entities.ViewInstanceEntity originInstanceDefinition;

    private final org.apache.ambari.server.orm.entities.ViewInstanceEntity currentInstanceDefinition;

    public ViewDataMigrationContextImpl(org.apache.ambari.server.orm.entities.ViewInstanceEntity originInstanceDefinition, org.apache.ambari.server.orm.entities.ViewInstanceEntity currentInstanceDefinition) {
        this.originInstanceDefinition = originInstanceDefinition;
        this.currentInstanceDefinition = currentInstanceDefinition;
    }

    private java.util.Map<org.apache.ambari.server.orm.entities.ViewInstanceEntity, org.apache.ambari.server.view.persistence.DataStoreModule> dataStoreModules = new java.util.WeakHashMap<>();

    protected org.apache.ambari.view.DataStore getDataStore(org.apache.ambari.server.orm.entities.ViewInstanceEntity instanceDefinition) {
        if (!dataStoreModules.containsKey(instanceDefinition)) {
            org.apache.ambari.server.view.persistence.DataStoreModule module = new org.apache.ambari.server.view.persistence.DataStoreModule(instanceDefinition, "ambari-view-migration");
            dataStoreModules.put(instanceDefinition, module);
        }
        com.google.inject.Injector injector = com.google.inject.Guice.createInjector(dataStoreModules.get(instanceDefinition));
        return injector.getInstance(org.apache.ambari.server.view.persistence.DataStoreImpl.class);
    }

    @java.lang.Override
    public int getCurrentDataVersion() {
        return currentInstanceDefinition.getViewEntity().getConfiguration().getDataVersion();
    }

    @java.lang.Override
    public int getOriginDataVersion() {
        return originInstanceDefinition.getViewEntity().getConfiguration().getDataVersion();
    }

    @java.lang.Override
    public org.apache.ambari.view.DataStore getOriginDataStore() {
        if (originDataStore == null) {
            originDataStore = getDataStore(originInstanceDefinition);
        }
        return originDataStore;
    }

    @java.lang.Override
    public org.apache.ambari.view.DataStore getCurrentDataStore() {
        if (currentDataStore == null) {
            currentDataStore = getDataStore(currentInstanceDefinition);
        }
        return currentDataStore;
    }

    @java.lang.Override
    @com.google.inject.persist.Transactional
    public void putCurrentInstanceData(java.lang.String user, java.lang.String key, java.lang.String value) {
        org.apache.ambari.server.view.ViewDataMigrationContextImpl.putInstanceData(currentInstanceDefinition, user, key, value);
    }

    @java.lang.Override
    public void copyAllObjects(java.lang.Class originEntityClass, java.lang.Class currentEntityClass) throws org.apache.ambari.view.migration.ViewDataMigrationException {
        copyAllObjects(originEntityClass, currentEntityClass, new org.apache.ambari.view.migration.EntityConverter() {
            @java.lang.Override
            public void convert(java.lang.Object orig, java.lang.Object dest) {
                org.springframework.beans.BeanUtils.copyProperties(orig, dest);
            }
        });
    }

    @java.lang.Override
    @com.google.inject.persist.Transactional
    public void copyAllObjects(java.lang.Class originEntityClass, java.lang.Class currentEntityClass, org.apache.ambari.view.migration.EntityConverter entityConverter) throws org.apache.ambari.view.migration.ViewDataMigrationException {
        try {
            for (java.lang.Object origInstance : getOriginDataStore().findAll(originEntityClass, null)) {
                java.lang.Object newInstance = currentEntityClass.newInstance();
                entityConverter.convert(origInstance, newInstance);
                getCurrentDataStore().store(newInstance);
            }
        } catch (org.apache.ambari.view.PersistenceException | java.lang.InstantiationException | java.lang.IllegalAccessException e) {
            java.lang.String msg = "Error occured during copying data. Persistence entities are not compatible.";
            org.apache.ambari.server.view.ViewDataMigrationContextImpl.LOG.error(msg);
            throw new org.apache.ambari.view.migration.ViewDataMigrationException(msg, e);
        }
    }

    @java.lang.Override
    public void copyAllInstanceData() {
        for (java.util.Map.Entry<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> userData : getOriginInstanceDataByUser().entrySet()) {
            for (java.util.Map.Entry<java.lang.String, java.lang.String> entry : userData.getValue().entrySet()) {
                putCurrentInstanceData(userData.getKey(), entry.getKey(), entry.getValue());
            }
        }
    }

    @java.lang.Override
    public org.apache.ambari.server.orm.entities.ViewInstanceEntity getOriginInstanceDefinition() {
        return originInstanceDefinition;
    }

    @java.lang.Override
    public java.util.Map<java.lang.String, java.lang.Class> getOriginEntityClasses() {
        org.apache.ambari.server.orm.entities.ViewEntity viewDefinition = originInstanceDefinition.getViewEntity();
        return org.apache.ambari.server.view.ViewDataMigrationContextImpl.getPersistenceClassesOfView(viewDefinition);
    }

    @java.lang.Override
    public java.util.Map<java.lang.String, java.lang.Class> getCurrentEntityClasses() {
        org.apache.ambari.server.orm.entities.ViewEntity viewDefinition = currentInstanceDefinition.getViewEntity();
        return org.apache.ambari.server.view.ViewDataMigrationContextImpl.getPersistenceClassesOfView(viewDefinition);
    }

    private static java.util.Map<java.lang.String, java.lang.Class> getPersistenceClassesOfView(org.apache.ambari.server.orm.entities.ViewEntity viewDefinition) {
        org.apache.ambari.server.view.configuration.PersistenceConfig persistence = viewDefinition.getConfiguration().getPersistence();
        java.util.HashMap<java.lang.String, java.lang.Class> classes = new java.util.HashMap<>();
        if (persistence != null) {
            for (org.apache.ambari.server.view.configuration.EntityConfig c : persistence.getEntities()) {
                try {
                    java.lang.Class entity = viewDefinition.getClassLoader().loadClass(c.getClassName());
                    classes.put(c.getClassName(), entity);
                } catch (java.lang.ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
        return classes;
    }

    @java.lang.Override
    public org.apache.ambari.server.orm.entities.ViewInstanceEntity getCurrentInstanceDefinition() {
        return currentInstanceDefinition;
    }

    @java.lang.Override
    public java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> getOriginInstanceDataByUser() {
        return org.apache.ambari.server.view.ViewDataMigrationContextImpl.getInstanceDataByUser(originInstanceDefinition);
    }

    @java.lang.Override
    @com.google.inject.persist.Transactional
    public void putOriginInstanceData(java.lang.String user, java.lang.String key, java.lang.String value) {
        org.apache.ambari.server.view.ViewDataMigrationContextImpl.putInstanceData(originInstanceDefinition, user, key, value);
    }

    @java.lang.Override
    public java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> getCurrentInstanceDataByUser() {
        return org.apache.ambari.server.view.ViewDataMigrationContextImpl.getInstanceDataByUser(currentInstanceDefinition);
    }

    public void closeMigration() {
        for (org.apache.ambari.server.view.persistence.DataStoreModule module : dataStoreModules.values()) {
            module.close();
        }
        dataStoreModules.clear();
    }

    private static void putInstanceData(org.apache.ambari.server.orm.entities.ViewInstanceEntity instanceDefinition, java.lang.String user, java.lang.String name, java.lang.String value) {
        org.apache.ambari.server.orm.entities.ViewInstanceDataEntity oldInstanceDataEntity = org.apache.ambari.server.view.ViewDataMigrationContextImpl.getInstanceData(instanceDefinition, user, name);
        if (oldInstanceDataEntity != null) {
            instanceDefinition.getData().remove(oldInstanceDataEntity);
        }
        org.apache.ambari.server.orm.entities.ViewInstanceDataEntity instanceDataEntity = new org.apache.ambari.server.orm.entities.ViewInstanceDataEntity();
        instanceDataEntity.setViewName(instanceDefinition.getViewName());
        instanceDataEntity.setViewInstanceName(instanceDefinition.getName());
        instanceDataEntity.setName(name);
        instanceDataEntity.setUser(user);
        instanceDataEntity.setValue(value);
        instanceDataEntity.setViewInstanceEntity(instanceDefinition);
        instanceDefinition.getData().add(instanceDataEntity);
    }

    private static org.apache.ambari.server.orm.entities.ViewInstanceDataEntity getInstanceData(org.apache.ambari.server.orm.entities.ViewInstanceEntity instanceDefinition, java.lang.String user, java.lang.String key) {
        for (org.apache.ambari.server.orm.entities.ViewInstanceDataEntity viewInstanceDataEntity : instanceDefinition.getData()) {
            if (viewInstanceDataEntity.getName().equals(key) && viewInstanceDataEntity.getUser().equals(user)) {
                return viewInstanceDataEntity;
            }
        }
        return null;
    }

    private static java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> getInstanceDataByUser(org.apache.ambari.server.orm.entities.ViewInstanceEntity instanceDefinition) {
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> instanceDataByUser = new java.util.HashMap<>();
        for (org.apache.ambari.server.orm.entities.ViewInstanceDataEntity entity : instanceDefinition.getData()) {
            if (!instanceDataByUser.containsKey(entity.getUser())) {
                instanceDataByUser.put(entity.getUser(), new java.util.HashMap<>());
            }
            instanceDataByUser.get(entity.getUser()).put(entity.getName(), entity.getValue());
        }
        return instanceDataByUser;
    }
}