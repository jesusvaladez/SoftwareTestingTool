package org.apache.ambari.server.audit.request;
import com.google.inject.multibindings.Multibinder;
import org.easymock.EasyMock;
public class RequestAuditLogModule extends com.google.inject.AbstractModule {
    @java.lang.Override
    protected void configure() {
        com.google.inject.multibindings.Multibinder<org.apache.ambari.server.audit.request.eventcreator.RequestAuditEventCreator> auditLogEventCreatorBinder = com.google.inject.multibindings.Multibinder.newSetBinder(binder(), org.apache.ambari.server.audit.request.eventcreator.RequestAuditEventCreator.class);
        auditLogEventCreatorBinder.addBinding().to(org.apache.ambari.server.audit.request.AllPostAndPutCreator.class);
        auditLogEventCreatorBinder.addBinding().to(org.apache.ambari.server.audit.request.AllGetCreator.class);
        auditLogEventCreatorBinder.addBinding().to(org.apache.ambari.server.audit.request.PutHostComponentCreator.class);
        bind(org.apache.ambari.server.audit.AuditLogger.class).toInstance(org.easymock.EasyMock.createStrictMock(org.apache.ambari.server.audit.AuditLoggerDefaultImpl.class));
        bind(org.apache.ambari.server.audit.request.RequestAuditLogger.class).to(org.apache.ambari.server.audit.request.RequestAuditLoggerImpl.class);
    }
}