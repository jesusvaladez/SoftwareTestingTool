package org.apache.ambari.server.cleanup;
import com.google.inject.multibindings.Multibinder;
public class CleanupModule extends com.google.inject.AbstractModule {
    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.cleanup.CleanupModule.class);

    protected java.util.List<java.lang.Class<?>> getSelectors() {
        java.util.List<java.lang.Class<?>> selectorList = new java.util.ArrayList<>();
        selectorList.add(org.apache.ambari.server.orm.dao.Cleanable.class);
        return selectorList;
    }

    protected java.util.List<java.lang.Class<?>> getExclusions() {
        return java.util.Collections.emptyList();
    }

    protected java.lang.String getPackageToScan() {
        return org.apache.ambari.server.orm.dao.Cleanable.class.getPackage().getName();
    }

    @java.lang.Override
    protected void configure() {
        com.google.inject.multibindings.Multibinder<org.apache.ambari.server.orm.dao.Cleanable> multiBinder = com.google.inject.multibindings.Multibinder.newSetBinder(binder(), org.apache.ambari.server.orm.dao.Cleanable.class);
        java.util.Set<java.lang.Class<?>> bindingSet = org.apache.ambari.server.cleanup.ClasspathScannerUtils.findOnClassPath(getPackageToScan(), getExclusions(), getSelectors());
        for (java.lang.Class clazz : bindingSet) {
            org.apache.ambari.server.cleanup.CleanupModule.LOG.info("Binding cleaner {}", clazz);
            multiBinder.addBinding().to(clazz).in(com.google.inject.Scopes.SINGLETON);
        }
        bind(org.apache.ambari.server.cleanup.CleanupServiceImpl.class).in(com.google.inject.Scopes.SINGLETON);
    }
}