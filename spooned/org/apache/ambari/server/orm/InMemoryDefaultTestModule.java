package org.apache.ambari.server.orm;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import org.easymock.EasyMock;
public class InMemoryDefaultTestModule extends com.google.inject.AbstractModule {
    java.util.Properties properties = new java.util.Properties();

    private static class BeanDefinitionsCachingTestControllerModule extends org.apache.ambari.server.controller.ControllerModule {
        private static final java.util.concurrent.atomic.AtomicReference<java.util.Set<java.lang.Class<?>>> matchedAnnotationClasses = new java.util.concurrent.atomic.AtomicReference<>(null);

        private static final java.util.concurrent.atomic.AtomicReference<java.util.Set<org.springframework.beans.factory.config.BeanDefinition>> foundNotificationBeanDefinitions = new java.util.concurrent.atomic.AtomicReference<>(null);

        public BeanDefinitionsCachingTestControllerModule(java.util.Properties properties) throws java.lang.Exception {
            super(properties);
        }

        @java.lang.Override
        protected java.util.Set<java.lang.Class<?>> bindByAnnotation(java.util.Set<java.lang.Class<?>> matchedClasses) {
            java.util.Set<java.lang.Class<?>> newMatchedClasses = super.bindByAnnotation(org.apache.ambari.server.orm.InMemoryDefaultTestModule.BeanDefinitionsCachingTestControllerModule.matchedAnnotationClasses.get());
            org.apache.ambari.server.orm.InMemoryDefaultTestModule.BeanDefinitionsCachingTestControllerModule.matchedAnnotationClasses.compareAndSet(null, java.util.Collections.unmodifiableSet(newMatchedClasses));
            return null;
        }

        @java.lang.Override
        protected java.util.Set<org.springframework.beans.factory.config.BeanDefinition> bindNotificationDispatchers(java.util.Set<org.springframework.beans.factory.config.BeanDefinition> beanDefinitions) {
            java.util.Set<org.springframework.beans.factory.config.BeanDefinition> newBeanDefinitions = super.bindNotificationDispatchers(org.apache.ambari.server.orm.InMemoryDefaultTestModule.BeanDefinitionsCachingTestControllerModule.foundNotificationBeanDefinitions.get());
            org.apache.ambari.server.orm.InMemoryDefaultTestModule.BeanDefinitionsCachingTestControllerModule.foundNotificationBeanDefinitions.compareAndSet(null, java.util.Collections.unmodifiableSet(newBeanDefinitions));
            return null;
        }
    }

    @java.lang.Override
    protected void configure() {
        java.lang.String stacks = "src/test/resources/stacks";
        java.lang.String version = "src/test/resources/version";
        java.lang.String sharedResourcesDir = "src/test/resources/";
        java.lang.String resourcesDir = "src/test/resources/";
        java.lang.String mpacksv2 = "src/main/resources/mpacks-v2";
        if (java.lang.System.getProperty("os.name").contains("Windows")) {
            stacks = java.lang.ClassLoader.getSystemClassLoader().getResource("stacks").getPath();
            version = new java.io.File(new java.io.File(java.lang.ClassLoader.getSystemClassLoader().getResource("").getPath()), "version").getPath();
            sharedResourcesDir = java.lang.ClassLoader.getSystemClassLoader().getResource("").getPath();
        }
        if (!properties.containsKey(org.apache.ambari.server.configuration.Configuration.SERVER_PERSISTENCE_TYPE.getKey())) {
            properties.setProperty(org.apache.ambari.server.configuration.Configuration.SERVER_PERSISTENCE_TYPE.getKey(), "in-memory");
        }
        if (!properties.containsKey(org.apache.ambari.server.configuration.Configuration.METADATA_DIR_PATH.getKey())) {
            properties.setProperty(org.apache.ambari.server.configuration.Configuration.METADATA_DIR_PATH.getKey(), stacks);
        }
        if (!properties.containsKey(org.apache.ambari.server.configuration.Configuration.SERVER_VERSION_FILE.getKey())) {
            properties.setProperty(org.apache.ambari.server.configuration.Configuration.SERVER_VERSION_FILE.getKey(), version);
        }
        if (!properties.containsKey(org.apache.ambari.server.configuration.Configuration.MPACKS_V2_STAGING_DIR_PATH.getKey())) {
            properties.setProperty(org.apache.ambari.server.configuration.Configuration.MPACKS_V2_STAGING_DIR_PATH.getKey(), mpacksv2);
        }
        if (!properties.containsKey(org.apache.ambari.server.configuration.Configuration.OS_VERSION.getKey())) {
            properties.setProperty(org.apache.ambari.server.configuration.Configuration.OS_VERSION.getKey(), "centos5");
        }
        if (!properties.containsKey(org.apache.ambari.server.configuration.Configuration.SHARED_RESOURCES_DIR.getKey())) {
            properties.setProperty(org.apache.ambari.server.configuration.Configuration.SHARED_RESOURCES_DIR.getKey(), sharedResourcesDir);
        }
        if (!properties.containsKey(org.apache.ambari.server.configuration.Configuration.RESOURCES_DIR.getKey())) {
            properties.setProperty(org.apache.ambari.server.configuration.Configuration.RESOURCES_DIR.getKey(), resourcesDir);
        }
        try {
            install(new org.apache.ambari.server.ldap.LdapModule());
            install(com.google.inject.util.Modules.override(new org.apache.ambari.server.orm.InMemoryDefaultTestModule.BeanDefinitionsCachingTestControllerModule(properties)).with(new com.google.inject.AbstractModule() {
                @java.lang.Override
                protected void configure() {
                    install(new com.google.inject.assistedinject.FactoryModuleBuilder().implement(org.apache.ambari.server.stack.StackManager.class, org.apache.ambari.server.stack.StackManagerMock.class).build(org.apache.ambari.server.stack.StackManagerFactory.class));
                    install(new com.google.inject.assistedinject.FactoryModuleBuilder().implement(org.apache.ambari.server.mpack.MpackManager.class, org.apache.ambari.server.mpack.MpackManagerMock.class).build(org.apache.ambari.server.mpack.MpackManagerFactory.class));
                }
            }));
            org.apache.ambari.server.audit.AuditLogger al = org.easymock.EasyMock.createNiceMock(org.apache.ambari.server.audit.AuditLogger.class);
            org.easymock.EasyMock.expect(al.isEnabled()).andReturn(false).anyTimes();
            bind(org.apache.ambari.server.audit.AuditLogger.class).toInstance(al);
            bind(org.apache.ambari.server.ldap.service.AmbariLdapConfigurationProvider.class).toInstance(org.easymock.EasyMock.createMock(org.apache.ambari.server.ldap.service.AmbariLdapConfigurationProvider.class));
            bind(new com.google.inject.TypeLiteral<org.apache.ambari.server.security.encryption.Encryptor<org.apache.ambari.server.events.AgentConfigsUpdateEvent>>() {}).annotatedWith(com.google.inject.name.Names.named("AgentConfigEncryptor")).toInstance(org.apache.ambari.server.security.encryption.Encryptor.NONE);
        } catch (java.lang.Exception e) {
            throw new java.lang.RuntimeException(e);
        }
    }

    public java.util.Properties getProperties() {
        return properties;
    }
}