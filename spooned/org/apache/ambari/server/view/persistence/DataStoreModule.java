package org.apache.ambari.server.view.persistence;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import org.eclipse.persistence.config.PersistenceUnitProperties;
import org.eclipse.persistence.dynamic.DynamicClassLoader;
import org.eclipse.persistence.jpa.dynamic.JPADynamicHelper;
import org.eclipse.persistence.sessions.DatabaseSession;
import org.eclipse.persistence.tools.schemaframework.SchemaManager;
import static org.eclipse.persistence.config.PersistenceUnitProperties.JDBC_PASSWORD;
import static org.eclipse.persistence.config.PersistenceUnitProperties.JDBC_USER;
public class DataStoreModule implements com.google.inject.Module , org.apache.ambari.server.view.persistence.SchemaManagerFactory {
    private final org.apache.ambari.server.orm.entities.ViewInstanceEntity viewInstanceEntity;

    private final org.eclipse.persistence.dynamic.DynamicClassLoader classLoader;

    private final javax.persistence.EntityManagerFactory entityManagerFactory;

    private final org.eclipse.persistence.jpa.dynamic.JPADynamicHelper jpaDynamicHelper;

    private static final java.lang.String VIEWS_PERSISTENCE_UNIT_NAME = "ambari-views";

    private com.google.common.base.Optional<java.lang.String> puName = com.google.common.base.Optional.absent();

    public DataStoreModule(org.apache.ambari.server.orm.entities.ViewInstanceEntity viewInstanceEntity) {
        org.apache.ambari.server.orm.entities.ViewEntity view = viewInstanceEntity.getViewEntity();
        this.viewInstanceEntity = viewInstanceEntity;
        this.classLoader = new org.eclipse.persistence.dynamic.DynamicClassLoader(view.getClassLoader());
        this.entityManagerFactory = getEntityManagerFactory(view.getAmbariConfiguration());
        this.jpaDynamicHelper = new org.eclipse.persistence.jpa.dynamic.JPADynamicHelper(entityManagerFactory.createEntityManager());
    }

    public DataStoreModule(org.apache.ambari.server.orm.entities.ViewInstanceEntity viewInstanceEntity, java.lang.String puName) {
        this.puName = com.google.common.base.Optional.of(puName);
        org.apache.ambari.server.orm.entities.ViewEntity view = viewInstanceEntity.getViewEntity();
        this.viewInstanceEntity = viewInstanceEntity;
        this.classLoader = new org.eclipse.persistence.dynamic.DynamicClassLoader(view.getClassLoader());
        this.entityManagerFactory = getEntityManagerFactory(view.getAmbariConfiguration());
        this.jpaDynamicHelper = new org.eclipse.persistence.jpa.dynamic.JPADynamicHelper(entityManagerFactory.createEntityManager());
    }

    @java.lang.Override
    public void configure(com.google.inject.Binder binder) {
        binder.bind(org.apache.ambari.server.orm.entities.ViewInstanceEntity.class).toInstance(viewInstanceEntity);
        binder.bind(org.eclipse.persistence.dynamic.DynamicClassLoader.class).toInstance(classLoader);
        binder.bind(javax.persistence.EntityManagerFactory.class).toInstance(entityManagerFactory);
        binder.bind(org.eclipse.persistence.jpa.dynamic.JPADynamicHelper.class).toInstance(jpaDynamicHelper);
        binder.bind(org.apache.ambari.server.view.persistence.SchemaManagerFactory.class).toInstance(this);
    }

    public void close() {
        entityManagerFactory.close();
    }

    @java.lang.Override
    public org.eclipse.persistence.tools.schemaframework.SchemaManager getSchemaManager(org.eclipse.persistence.sessions.DatabaseSession session) {
        return new org.eclipse.persistence.tools.schemaframework.SchemaManager(session);
    }

    private javax.persistence.EntityManagerFactory getEntityManagerFactory(org.apache.ambari.server.configuration.Configuration configuration) {
        java.util.Map<java.lang.Object, java.lang.Object> persistenceMap = org.apache.ambari.server.controller.ControllerModule.getPersistenceProperties(configuration);
        if (!configuration.getPersistenceType().equals(org.apache.ambari.server.orm.PersistenceType.IN_MEMORY)) {
            persistenceMap.put(org.apache.ambari.server.view.persistence.JDBC_USER, configuration.getDatabaseUser());
            persistenceMap.put(org.apache.ambari.server.view.persistence.JDBC_PASSWORD, configuration.getDatabasePassword());
            persistenceMap.put(PersistenceUnitProperties.CLASSLOADER, classLoader);
            persistenceMap.put(PersistenceUnitProperties.WEAVING, "static");
        }
        return javax.persistence.Persistence.createEntityManagerFactory(puName.isPresent() ? puName.get() : org.apache.ambari.server.view.persistence.DataStoreModule.VIEWS_PERSISTENCE_UNIT_NAME, persistenceMap);
    }
}