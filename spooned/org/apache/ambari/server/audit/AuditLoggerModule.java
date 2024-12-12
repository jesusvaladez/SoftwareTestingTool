package org.apache.ambari.server.audit;
import com.google.inject.multibindings.Multibinder;
public class AuditLoggerModule extends com.google.inject.AbstractModule {
    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.audit.AuditLoggerModule.class);

    protected java.util.List<java.lang.Class<?>> getSelectors() {
        java.util.List<java.lang.Class<?>> selectorList = new java.util.ArrayList<>();
        selectorList.add(org.apache.ambari.server.audit.request.eventcreator.RequestAuditEventCreator.class);
        return selectorList;
    }

    protected java.util.List<java.lang.Class<?>> getExclusions() {
        return java.util.Collections.emptyList();
    }

    protected java.lang.String getPackageToScan() {
        return org.apache.ambari.server.audit.request.eventcreator.RequestAuditEventCreator.class.getPackage().getName();
    }

    @java.lang.Override
    protected void configure() {
        bind(org.apache.ambari.server.audit.AuditLogger.class).to(org.apache.ambari.server.audit.AsyncAuditLogger.class);
        bind(org.apache.ambari.server.audit.AuditLogger.class).annotatedWith(com.google.inject.name.Names.named(org.apache.ambari.server.audit.AsyncAuditLogger.InnerLogger)).to(org.apache.ambari.server.audit.AuditLoggerDefaultImpl.class);
        com.google.inject.multibindings.Multibinder<org.apache.ambari.server.audit.request.eventcreator.RequestAuditEventCreator> multiBinder = com.google.inject.multibindings.Multibinder.newSetBinder(binder(), org.apache.ambari.server.audit.request.eventcreator.RequestAuditEventCreator.class);
        java.util.Set<java.lang.Class<?>> bindingSet = org.apache.ambari.server.cleanup.ClasspathScannerUtils.findOnClassPath(getPackageToScan(), getExclusions(), getSelectors());
        for (java.lang.Class clazz : bindingSet) {
            org.apache.ambari.server.audit.AuditLoggerModule.LOG.info("Binding audit event creator {}", clazz);
            multiBinder.addBinding().to(clazz).in(com.google.inject.Scopes.SINGLETON);
        }
        bind(org.apache.ambari.server.audit.request.RequestAuditLogger.class).to(org.apache.ambari.server.audit.request.RequestAuditLoggerImpl.class);
    }
}