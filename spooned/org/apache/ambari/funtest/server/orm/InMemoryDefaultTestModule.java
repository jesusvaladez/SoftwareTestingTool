package org.apache.ambari.funtest.server.orm;
public class InMemoryDefaultTestModule extends com.google.inject.AbstractModule {
    private static class BeanDefinitionsCachingTestControllerModule extends org.apache.ambari.server.controller.ControllerModule {
        private static final java.util.concurrent.atomic.AtomicReference<java.util.Set<java.lang.Class<?>>> matchedAnnotationClasses = new java.util.concurrent.atomic.AtomicReference<>(null);

        public BeanDefinitionsCachingTestControllerModule(java.util.Properties properties) throws java.lang.Exception {
            super(properties);
        }

        @java.lang.Override
        protected java.util.Set<java.lang.Class<?>> bindByAnnotation(java.util.Set<java.lang.Class<?>> matchedClasses) {
            java.util.Set<java.lang.Class<?>> newMatchedClasses = super.bindByAnnotation(org.apache.ambari.funtest.server.orm.InMemoryDefaultTestModule.BeanDefinitionsCachingTestControllerModule.matchedAnnotationClasses.get());
            org.apache.ambari.funtest.server.orm.InMemoryDefaultTestModule.BeanDefinitionsCachingTestControllerModule.matchedAnnotationClasses.compareAndSet(null, java.util.Collections.unmodifiableSet(newMatchedClasses));
            return null;
        }
    }

    java.util.Properties properties = new java.util.Properties();

    @java.lang.Override
    protected void configure() {
        java.lang.String stacks = "src/test/resources/stacks";
        java.lang.String version = "src/test/resources/version";
        java.lang.String sharedResourcesDir = "src/test/resources/";
        if (java.lang.System.getProperty("os.name").contains("Windows")) {
            stacks = java.lang.ClassLoader.getSystemClassLoader().getResource("stacks").getPath();
            version = new java.io.File(new java.io.File(java.lang.ClassLoader.getSystemClassLoader().getResource("").getPath()).getParent(), "version").getPath();
            sharedResourcesDir = java.lang.ClassLoader.getSystemClassLoader().getResource("").getPath();
        }
        properties.setProperty(org.apache.ambari.server.configuration.Configuration.SERVER_PERSISTENCE_TYPE.getKey(), "in-memory");
        properties.setProperty(org.apache.ambari.server.configuration.Configuration.METADATA_DIR_PATH.getKey(), stacks);
        properties.setProperty(org.apache.ambari.server.configuration.Configuration.SERVER_VERSION_FILE.getKey(), version);
        properties.setProperty(org.apache.ambari.server.configuration.Configuration.OS_VERSION.getKey(), "centos6");
        properties.setProperty(org.apache.ambari.server.configuration.Configuration.SHARED_RESOURCES_DIR.getKey(), sharedResourcesDir);
        try {
            install(new org.apache.ambari.funtest.server.orm.InMemoryDefaultTestModule.BeanDefinitionsCachingTestControllerModule(properties));
        } catch (java.lang.Exception e) {
            throw new java.lang.RuntimeException(e);
        }
    }

    public java.util.Properties getProperties() {
        return properties;
    }
}