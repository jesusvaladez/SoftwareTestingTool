package org.apache.ambari.server.view.persistence;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;
import org.eclipse.persistence.dynamic.DynamicClassLoader;
import org.eclipse.persistence.dynamic.DynamicEntity;
import org.eclipse.persistence.dynamic.DynamicType;
import org.eclipse.persistence.exceptions.DynamicException;
import org.eclipse.persistence.internal.helper.DatabaseField;
import org.eclipse.persistence.jpa.JpaHelper;
import org.eclipse.persistence.jpa.dynamic.JPADynamicHelper;
import org.eclipse.persistence.jpa.dynamic.JPADynamicTypeBuilder;
import org.eclipse.persistence.mappings.DirectToFieldMapping;
import org.eclipse.persistence.sequencing.TableSequence;
import org.eclipse.persistence.sessions.Session;
public class DataStoreImpl implements org.apache.ambari.view.DataStore {
    @javax.inject.Inject
    javax.persistence.EntityManagerFactory entityManagerFactory;

    @javax.inject.Inject
    org.eclipse.persistence.dynamic.DynamicClassLoader classLoader;

    @javax.inject.Inject
    org.eclipse.persistence.jpa.dynamic.JPADynamicHelper jpaDynamicHelper;

    @javax.inject.Inject
    org.apache.ambari.server.view.persistence.SchemaManagerFactory schemaManagerFactory;

    @javax.inject.Inject
    org.apache.ambari.server.orm.entities.ViewInstanceEntity viewInstanceEntity;

    private final java.util.Map<java.lang.Class, java.lang.String> entityClassMap = new java.util.LinkedHashMap<>();

    private final java.util.Map<java.lang.String, org.apache.ambari.server.orm.entities.ViewEntityEntity> entityMap = new java.util.LinkedHashMap<>();

    private final java.util.Map<java.lang.String, org.eclipse.persistence.jpa.dynamic.JPADynamicTypeBuilder> typeBuilderMap = new java.util.LinkedHashMap<>();

    private volatile boolean initialized = false;

    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.view.persistence.DataStoreImpl.class);

    protected static final int MAX_ENTITY_STRING_FIELD_LENGTH = 3000;

    protected static final int MAX_ENTITY_FIELD_LENGTH_TOTAL = 65000;

    private static final java.lang.String NAME_PREFIX = "DS_";

    @java.lang.Override
    public void store(java.lang.Object entity) throws org.apache.ambari.view.PersistenceException {
        checkInitialize();
        javax.persistence.EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            try {
                org.eclipse.persistence.dynamic.DynamicEntity dynamicEntity = persistEntity(entity, em, new java.util.HashSet<>());
                em.getTransaction().commit();
                java.util.Map<java.lang.String, java.lang.Object> props = getEntityProperties(entity);
                java.util.List<java.lang.String> keys = new java.util.ArrayList<>(props.keySet());
                for (java.lang.String key : keys) {
                    java.lang.String attribute = getAttributeName(key);
                    try {
                        props.put(key, dynamicEntity.get(attribute));
                    } catch (org.eclipse.persistence.exceptions.DynamicException de) {
                        org.apache.ambari.server.view.persistence.DataStoreImpl.LOG.debug("Error occurred while copying entity property : {} : {}", key, de);
                    }
                }
                setEntityProperties(entity, props);
            } catch (java.lang.Exception e) {
                org.apache.ambari.server.view.persistence.DataStoreImpl.rollbackTransaction(em.getTransaction());
                org.apache.ambari.server.view.persistence.DataStoreImpl.throwPersistenceException("Caught exception trying to store view entity " + entity, e);
            }
        } finally {
            em.close();
        }
    }

    @java.lang.Override
    public void remove(java.lang.Object entity) throws org.apache.ambari.view.PersistenceException {
        checkInitialize();
        javax.persistence.EntityManager em = getEntityManager();
        try {
            java.lang.Class clazz = entity.getClass();
            java.lang.String id = getIdFieldName(clazz);
            org.eclipse.persistence.dynamic.DynamicType type = getDynamicEntityType(clazz);
            if (type != null) {
                try {
                    java.util.Map<java.lang.String, java.lang.Object> properties = getEntityProperties(entity);
                    org.eclipse.persistence.dynamic.DynamicEntity dynamicEntity = em.getReference(type.getJavaClass(), properties.get(id));
                    if (dynamicEntity != null) {
                        em.getTransaction().begin();
                        try {
                            em.remove(dynamicEntity);
                            em.getTransaction().commit();
                        } catch (java.lang.Exception e) {
                            org.apache.ambari.server.view.persistence.DataStoreImpl.rollbackTransaction(em.getTransaction());
                            org.apache.ambari.server.view.persistence.DataStoreImpl.throwPersistenceException("Caught exception trying to remove view entity " + entity, e);
                        }
                    }
                } catch (java.lang.Exception e) {
                    org.apache.ambari.server.view.persistence.DataStoreImpl.throwPersistenceException("Caught exception trying to remove view entity " + entity, e);
                }
            }
        } finally {
            em.close();
        }
    }

    @java.lang.Override
    public <T> T find(java.lang.Class<T> clazz, java.lang.Object primaryKey) throws org.apache.ambari.view.PersistenceException {
        checkInitialize();
        javax.persistence.EntityManager em = getEntityManager();
        try {
            org.eclipse.persistence.dynamic.DynamicEntity dynamicEntity = null;
            org.eclipse.persistence.dynamic.DynamicType type = getDynamicEntityType(clazz);
            if (type != null) {
                dynamicEntity = em.find(type.getJavaClass(), primaryKey);
            }
            return dynamicEntity == null ? null : toEntity(clazz, type, dynamicEntity);
        } catch (java.lang.Exception e) {
            org.apache.ambari.server.view.persistence.DataStoreImpl.throwPersistenceException((("Caught exception trying to find " + clazz.getName()) + " where key=") + primaryKey, e);
        } finally {
            em.close();
        }
        return null;
    }

    @java.lang.Override
    public <T> java.util.Collection<T> findAll(java.lang.Class<T> clazz, java.lang.String whereClause) throws org.apache.ambari.view.PersistenceException {
        checkInitialize();
        javax.persistence.EntityManager em = getEntityManager();
        try {
            java.util.Collection<T> resources = new java.util.HashSet<>();
            org.eclipse.persistence.dynamic.DynamicType type = getDynamicEntityType(clazz);
            if (type != null) {
                try {
                    javax.persistence.Query query = em.createQuery(getSelectStatement(clazz, whereClause));
                    java.util.List dynamicEntities = query.getResultList();
                    for (java.lang.Object dynamicEntity : dynamicEntities) {
                        resources.add(toEntity(clazz, type, ((org.eclipse.persistence.dynamic.DynamicEntity) (dynamicEntity))));
                    }
                } catch (java.lang.Exception e) {
                    org.apache.ambari.server.view.persistence.DataStoreImpl.throwPersistenceException((("Caught exception trying to find " + clazz.getName()) + " where ") + whereClause, e);
                }
            }
            return resources;
        } finally {
            em.close();
        }
    }

    private void checkInitialize() throws org.apache.ambari.view.PersistenceException {
        if (!initialized) {
            synchronized(this) {
                if (!initialized) {
                    try {
                        for (org.apache.ambari.server.orm.entities.ViewEntityEntity viewEntityEntity : viewInstanceEntity.getEntities()) {
                            java.lang.String className = viewEntityEntity.getClassName();
                            java.lang.Class clazz = classLoader.loadClass(className);
                            java.lang.String name = getEntityName(viewEntityEntity);
                            entityMap.put(name, viewEntityEntity);
                            entityClassMap.put(clazz, name);
                        }
                        configureTypes(jpaDynamicHelper, classLoader);
                        initialized = true;
                    } catch (java.lang.Exception e) {
                        org.apache.ambari.server.view.persistence.DataStoreImpl.throwPersistenceException((("Can't initialize data store for view " + viewInstanceEntity.getViewName()) + ".") + viewInstanceEntity.getName(), e);
                    }
                }
            }
        }
    }

    private void configureTypes(org.eclipse.persistence.jpa.dynamic.JPADynamicHelper helper, org.eclipse.persistence.dynamic.DynamicClassLoader dcl) throws java.beans.IntrospectionException, org.apache.ambari.view.PersistenceException, java.lang.NoSuchFieldException {
        for (java.util.Map.Entry<java.lang.Class, java.lang.String> entry : entityClassMap.entrySet()) {
            java.lang.String entityName = entry.getValue();
            java.lang.Class<?> javaType = dcl.createDynamicClass(entityName);
            java.lang.String tableName = getTableName(entityMap.get(entityName));
            org.eclipse.persistence.jpa.dynamic.JPADynamicTypeBuilder typeBuilder = new org.eclipse.persistence.jpa.dynamic.JPADynamicTypeBuilder(javaType, null, tableName);
            typeBuilderMap.put(entityName, typeBuilder);
        }
        org.eclipse.persistence.sessions.Session session = org.eclipse.persistence.jpa.JpaHelper.getEntityManager(getEntityManager()).getServerSession();
        for (java.util.Map.Entry<java.lang.Class, java.lang.String> entry : entityClassMap.entrySet()) {
            java.lang.Class clazz = entry.getKey();
            java.lang.String entityName = entry.getValue();
            org.eclipse.persistence.jpa.dynamic.JPADynamicTypeBuilder typeBuilder = typeBuilderMap.get(entityName);
            java.lang.String seqName = new java.lang.String(entityName + "_id_seq").toLowerCase();
            org.eclipse.persistence.sequencing.TableSequence tableSequence = new org.eclipse.persistence.sequencing.TableSequence(seqName, 50, "ambari_sequences", "sequence_name", "sequence_value");
            session.getLogin().addSequence(tableSequence);
            java.util.Map<java.lang.String, java.beans.PropertyDescriptor> descriptorMap = org.apache.ambari.server.view.persistence.DataStoreImpl.getDescriptorMap(clazz);
            long totalLength = 0L;
            for (java.util.Map.Entry<java.lang.String, java.beans.PropertyDescriptor> descriptorEntry : descriptorMap.entrySet()) {
                java.lang.String fieldName = descriptorEntry.getKey();
                java.lang.String attributeName = getAttributeName(fieldName);
                java.beans.PropertyDescriptor descriptor = descriptorEntry.getValue();
                if (fieldName.equals(entityMap.get(entityName).getIdProperty())) {
                    typeBuilder.setPrimaryKeyFields(attributeName);
                    typeBuilder.configureSequencing(tableSequence, seqName, attributeName);
                }
                java.lang.Class<?> propertyType = descriptor.getPropertyType();
                if (isDirectMappingType(propertyType)) {
                    org.eclipse.persistence.mappings.DirectToFieldMapping mapping = typeBuilder.addDirectMapping(attributeName, propertyType, attributeName);
                    org.eclipse.persistence.internal.helper.DatabaseField field = mapping.getField();
                    if (java.lang.String.class.isAssignableFrom(propertyType)) {
                        field.setLength(org.apache.ambari.server.view.persistence.DataStoreImpl.MAX_ENTITY_STRING_FIELD_LENGTH);
                    }
                    totalLength += field.getLength();
                    if (totalLength > org.apache.ambari.server.view.persistence.DataStoreImpl.MAX_ENTITY_FIELD_LENGTH_TOTAL) {
                        java.lang.String msg = java.lang.String.format("The total length of the fields of the %s entity can not exceed %d characters.", clazz.getSimpleName(), org.apache.ambari.server.view.persistence.DataStoreImpl.MAX_ENTITY_FIELD_LENGTH_TOTAL);
                        org.apache.ambari.server.view.persistence.DataStoreImpl.LOG.error(msg);
                        throw new java.lang.IllegalStateException(msg);
                    }
                }
            }
        }
        for (java.util.Map.Entry<java.lang.Class, java.lang.String> entry : entityClassMap.entrySet()) {
            java.lang.Class clazz = entry.getKey();
            java.lang.String entityName = entry.getValue();
            org.eclipse.persistence.jpa.dynamic.JPADynamicTypeBuilder typeBuilder = typeBuilderMap.get(entityName);
            java.util.Map<java.lang.String, java.beans.PropertyDescriptor> descriptorMap = org.apache.ambari.server.view.persistence.DataStoreImpl.getDescriptorMap(clazz);
            for (java.util.Map.Entry<java.lang.String, java.beans.PropertyDescriptor> descriptorEntry : descriptorMap.entrySet()) {
                java.lang.String fieldName = descriptorEntry.getKey();
                java.lang.String attributeName = getAttributeName(fieldName);
                java.beans.PropertyDescriptor descriptor = descriptorEntry.getValue();
                java.lang.Class<?> propertyType = descriptor.getPropertyType();
                java.lang.String refEntityName = entityClassMap.get(propertyType);
                if (refEntityName == null) {
                    if (java.util.Collection.class.isAssignableFrom(propertyType)) {
                        java.lang.String tableName = (getTableName(entityMap.get(entityName)) + "_") + attributeName;
                        java.lang.Class<?> parameterizedTypeClass = org.apache.ambari.server.view.persistence.DataStoreImpl.getParameterizedTypeClass(clazz, attributeName);
                        refEntityName = entityClassMap.get(parameterizedTypeClass);
                        if (refEntityName == null) {
                            typeBuilder.addDirectCollectionMapping(attributeName, tableName, attributeName, parameterizedTypeClass, entityMap.get(entityName).getIdProperty());
                        } else {
                            org.eclipse.persistence.dynamic.DynamicType refType = typeBuilderMap.get(refEntityName).getType();
                            typeBuilder.addManyToManyMapping(attributeName, refType, tableName);
                        }
                    }
                } else {
                    org.eclipse.persistence.dynamic.DynamicType refType = typeBuilderMap.get(refEntityName).getType();
                    typeBuilder.addOneToOneMapping(attributeName, refType, attributeName);
                }
            }
        }
        org.eclipse.persistence.dynamic.DynamicType[] types = new org.eclipse.persistence.dynamic.DynamicType[typeBuilderMap.size()];
        int i = typeBuilderMap.size() - 1;
        for (org.eclipse.persistence.jpa.dynamic.JPADynamicTypeBuilder typeBuilder : typeBuilderMap.values()) {
            types[i--] = typeBuilder.getType();
        }
        helper.addTypes(true, true, types);
        schemaManagerFactory.getSchemaManager(helper.getSession()).extendDefaultTables(true);
    }

    private org.eclipse.persistence.dynamic.DynamicEntity persistEntity(java.lang.Object entity, javax.persistence.EntityManager em, java.util.Set<org.eclipse.persistence.dynamic.DynamicEntity> persistSet) throws org.apache.ambari.view.PersistenceException, java.beans.IntrospectionException, java.lang.reflect.InvocationTargetException, java.lang.IllegalAccessException, java.lang.NoSuchFieldException {
        org.eclipse.persistence.dynamic.DynamicEntity dynamicEntity = null;
        java.lang.Class clazz = entity.getClass();
        java.lang.String id = getIdFieldName(clazz);
        java.util.Map<java.lang.String, java.lang.Object> properties = getEntityProperties(entity);
        org.eclipse.persistence.dynamic.DynamicType type = getDynamicEntityType(clazz);
        if (type != null) {
            if (null != properties.get(id)) {
                dynamicEntity = em.find(type.getJavaClass(), properties.get(id));
            }
            boolean create = dynamicEntity == null;
            if (create) {
                dynamicEntity = type.newDynamicEntity();
            }
            if (persistSet.contains(dynamicEntity)) {
                return dynamicEntity;
            }
            persistSet.add(dynamicEntity);
            for (java.lang.String attributeName : type.getPropertiesNames()) {
                java.lang.String fieldName = getFieldName(attributeName);
                if (properties.containsKey(fieldName)) {
                    java.lang.Object value = properties.get(fieldName);
                    if (value != null) {
                        java.lang.Class<?> valueClass = value.getClass();
                        if (java.util.Collection.class.isAssignableFrom(valueClass)) {
                            java.lang.Class<?> typeClass = org.apache.ambari.server.view.persistence.DataStoreImpl.getParameterizedTypeClass(clazz, fieldName);
                            java.util.Collection<java.lang.Object> collection = dynamicEntity.get(attributeName);
                            collection.clear();
                            for (java.lang.Object collectionValue : ((java.util.Collection) (value))) {
                                if (getDynamicEntityType(typeClass) != null) {
                                    collectionValue = persistEntity(collectionValue, em, persistSet);
                                }
                                if (collectionValue != null) {
                                    collection.add(collectionValue);
                                }
                            }
                        } else {
                            if (getDynamicEntityType(valueClass) != null) {
                                value = persistEntity(value, em, persistSet);
                            }
                            if (value != null) {
                                if (java.lang.String.class.isAssignableFrom(valueClass)) {
                                    org.apache.ambari.server.view.persistence.DataStoreImpl.checkStringValue(entity, fieldName, ((java.lang.String) (value)));
                                }
                                dynamicEntity.set(attributeName, value);
                            }
                        }
                    }
                }
            }
            if (create) {
                em.persist(dynamicEntity);
            }
        }
        return dynamicEntity;
    }

    private <T> T toEntity(java.lang.Class<T> clazz, org.eclipse.persistence.dynamic.DynamicType type, org.eclipse.persistence.dynamic.DynamicEntity entity) throws java.beans.IntrospectionException, java.lang.reflect.InvocationTargetException, java.lang.IllegalAccessException, java.lang.InstantiationException, java.lang.NoSuchFieldException {
        T resource = clazz.newInstance();
        java.util.Map<java.lang.String, java.lang.Object> properties = new java.util.HashMap<>();
        for (java.lang.String attributeName : type.getPropertiesNames()) {
            java.lang.String fieldName = getFieldName(attributeName);
            properties.put(fieldName, entity.get(attributeName));
        }
        setEntityProperties(resource, properties);
        return resource;
    }

    private <T> java.lang.String getSelectStatement(java.lang.Class<T> clazz, java.lang.String whereClause) throws java.beans.IntrospectionException {
        java.lang.StringBuilder stringBuilder = new java.lang.StringBuilder();
        java.lang.String entityName = entityClassMap.get(clazz);
        stringBuilder.append("SELECT e FROM ").append(entityName).append(" e");
        if (whereClause != null) {
            stringBuilder.append(" WHERE");
            java.util.Set<java.lang.String> propertyNames = org.apache.ambari.server.view.persistence.DataStoreImpl.getPropertyNames(clazz);
            java.util.StringTokenizer tokenizer = new java.util.StringTokenizer(whereClause, " \t\n\r\f+-*/=><()\"", true);
            boolean quoted = false;
            while (tokenizer.hasMoreElements()) {
                java.lang.String token = tokenizer.nextToken();
                quoted = quoted ^ token.equals("\"");
                if (propertyNames.contains(token) && (!quoted)) {
                    stringBuilder.append(" e.").append(getAttributeName(token));
                } else {
                    stringBuilder.append(token);
                }
            } 
        }
        return stringBuilder.toString();
    }

    private java.util.Map<java.lang.String, java.lang.Object> getEntityProperties(java.lang.Object entity) throws java.beans.IntrospectionException, java.lang.reflect.InvocationTargetException, java.lang.IllegalAccessException {
        java.util.Map<java.lang.String, java.lang.Object> properties = new java.util.HashMap<>();
        for (java.beans.PropertyDescriptor pd : java.beans.Introspector.getBeanInfo(entity.getClass()).getPropertyDescriptors()) {
            java.lang.String name = pd.getName();
            java.lang.reflect.Method readMethod = pd.getReadMethod();
            if (readMethod != null) {
                properties.put(name, readMethod.invoke(entity));
            }
        }
        return properties;
    }

    private void setEntityProperties(java.lang.Object entity, java.util.Map<java.lang.String, java.lang.Object> properties) throws java.beans.IntrospectionException, java.lang.reflect.InvocationTargetException, java.lang.IllegalAccessException, java.lang.InstantiationException, java.lang.NoSuchFieldException {
        for (java.beans.PropertyDescriptor pd : java.beans.Introspector.getBeanInfo(entity.getClass()).getPropertyDescriptors()) {
            java.lang.String name = pd.getName();
            if (properties.containsKey(name)) {
                java.lang.reflect.Method writeMethod = pd.getWriteMethod();
                if (writeMethod != null) {
                    java.lang.Object value = properties.get(name);
                    if (value instanceof java.util.Collection) {
                        java.util.Set<java.lang.Object> newCollection = new java.util.HashSet<>();
                        for (java.lang.Object collectionValue : ((java.util.Collection) (value))) {
                            if (collectionValue instanceof org.eclipse.persistence.dynamic.DynamicEntity) {
                                java.lang.Class<?> clazz = entity.getClass();
                                java.lang.Class<?> parameterizedTypeClass = org.apache.ambari.server.view.persistence.DataStoreImpl.getParameterizedTypeClass(clazz, pd.getName());
                                collectionValue = toEntity(parameterizedTypeClass, getDynamicEntityType(parameterizedTypeClass), ((org.eclipse.persistence.dynamic.DynamicEntity) (collectionValue)));
                            }
                            if (collectionValue != null) {
                                newCollection.add(collectionValue);
                            }
                        }
                        writeMethod.invoke(entity, newCollection);
                    } else {
                        if (value instanceof org.eclipse.persistence.dynamic.DynamicEntity) {
                            java.lang.Class<?> clazz = pd.getPropertyType();
                            value = toEntity(clazz, getDynamicEntityType(clazz), ((org.eclipse.persistence.dynamic.DynamicEntity) (value)));
                        }
                        if (value != null) {
                            writeMethod.invoke(entity, value);
                        }
                    }
                }
            }
        }
    }

    private boolean isDirectMappingType(java.lang.Class<?> propertyType) {
        return (!java.util.Collection.class.isAssignableFrom(propertyType)) && (entityClassMap.get(propertyType) == null);
    }

    private org.eclipse.persistence.dynamic.DynamicType getDynamicEntityType(java.lang.Class clazz) {
        org.eclipse.persistence.jpa.dynamic.JPADynamicTypeBuilder builder = typeBuilderMap.get(entityClassMap.get(clazz));
        return builder == null ? null : builder.getType();
    }

    private java.lang.String getIdFieldName(java.lang.Class clazz) throws org.apache.ambari.view.PersistenceException {
        if (entityClassMap.containsKey(clazz)) {
            java.lang.String entityName = entityClassMap.get(clazz);
            if (entityMap.containsKey(entityName)) {
                return entityMap.get(entityName).getIdProperty();
            }
        }
        throw new org.apache.ambari.view.PersistenceException(("The class " + clazz.getName()) + "is not registered as an entity.");
    }

    private static java.util.Map<java.lang.String, java.beans.PropertyDescriptor> getDescriptorMap(java.lang.Class<?> clazz) throws java.beans.IntrospectionException {
        java.util.Map<java.lang.String, java.beans.PropertyDescriptor> descriptorMap = new java.util.HashMap<>();
        for (java.beans.PropertyDescriptor pd : java.beans.Introspector.getBeanInfo(clazz).getPropertyDescriptors()) {
            java.lang.String name = pd.getName();
            if ((pd.getReadMethod() != null) && (!name.equals("class"))) {
                descriptorMap.put(name, pd);
            }
        }
        return descriptorMap;
    }

    private static java.util.Set<java.lang.String> getPropertyNames(java.lang.Class clazz) throws java.beans.IntrospectionException {
        java.util.Set<java.lang.String> propertyNames = new java.util.HashSet<>();
        for (java.beans.PropertyDescriptor pd : java.beans.Introspector.getBeanInfo(clazz).getPropertyDescriptors()) {
            propertyNames.add(pd.getName());
        }
        return propertyNames;
    }

    private static java.lang.Class<?> getParameterizedTypeClass(java.lang.Class clazz, java.lang.String fieldName) throws java.lang.NoSuchFieldException {
        java.lang.reflect.Field field = clazz.getDeclaredField(fieldName);
        java.lang.reflect.ParameterizedType parameterizedType = ((java.lang.reflect.ParameterizedType) (field.getGenericType()));
        return ((java.lang.Class<?>) (parameterizedType.getActualTypeArguments()[0]));
    }

    private static void checkStringValue(java.lang.Object entity, java.lang.String fieldName, java.lang.String value) {
        if (value.length() > org.apache.ambari.server.view.persistence.DataStoreImpl.MAX_ENTITY_STRING_FIELD_LENGTH) {
            java.lang.String msg = java.lang.String.format("The value for the %s field of the %s entity can not exceed %d characters.  " + "Given value = %s", fieldName, entity.getClass().getSimpleName(), org.apache.ambari.server.view.persistence.DataStoreImpl.MAX_ENTITY_STRING_FIELD_LENGTH, value);
            org.apache.ambari.server.view.persistence.DataStoreImpl.LOG.error(msg);
            throw new java.lang.IllegalStateException(msg);
        }
    }

    private static void rollbackTransaction(javax.persistence.EntityTransaction transaction) {
        if ((transaction != null) && transaction.isActive()) {
            transaction.rollback();
        }
    }

    private static void throwPersistenceException(java.lang.String msg, java.lang.Exception e) throws org.apache.ambari.view.PersistenceException {
        org.apache.ambari.server.view.persistence.DataStoreImpl.LOG.error(msg, e);
        throw new org.apache.ambari.view.PersistenceException(msg, e);
    }

    private java.lang.String getTableName(org.apache.ambari.server.orm.entities.ViewEntityEntity entity) {
        return getEntityName(entity).toUpperCase();
    }

    private java.lang.String getFieldName(java.lang.String attributeName) {
        return alterNames() ? attributeName.substring(org.apache.ambari.server.view.persistence.DataStoreImpl.NAME_PREFIX.length()) : attributeName;
    }

    private java.lang.String getAttributeName(java.lang.String fieldName) {
        return alterNames() ? org.apache.ambari.server.view.persistence.DataStoreImpl.NAME_PREFIX + fieldName : fieldName;
    }

    private java.lang.String getEntityName(org.apache.ambari.server.orm.entities.ViewEntityEntity entity) {
        java.lang.String className = entity.getClassName();
        java.lang.String[] parts = className.split("\\.");
        java.lang.String simpleClassName = parts[parts.length - 1];
        if (alterNames()) {
            return ((org.apache.ambari.server.view.persistence.DataStoreImpl.NAME_PREFIX + simpleClassName) + "_") + entity.getId();
        }
        return simpleClassName + entity.getId();
    }

    private javax.persistence.EntityManager getEntityManager() {
        return entityManagerFactory.createEntityManager();
    }

    private boolean alterNames() {
        return viewInstanceEntity.alterNames();
    }
}