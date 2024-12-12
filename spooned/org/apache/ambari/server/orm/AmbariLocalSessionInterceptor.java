package org.apache.ambari.server.orm;
public class AmbariLocalSessionInterceptor implements org.aopalliance.intercept.MethodInterceptor {
    @com.google.inject.Inject
    private com.google.inject.persist.jpa.AmbariJpaPersistService emProvider;

    private final java.lang.ThreadLocal<java.lang.Boolean> didWeStartWork = new java.lang.ThreadLocal<>();

    @java.lang.Override
    public java.lang.Object invoke(org.aopalliance.intercept.MethodInvocation invocation) throws java.lang.Throwable {
        if (!emProvider.isWorking()) {
            emProvider.begin();
            didWeStartWork.set(true);
            try {
                return invocation.proceed();
            } finally {
                if (null != didWeStartWork.get()) {
                    didWeStartWork.remove();
                    emProvider.end();
                }
            }
        } else {
            return invocation.proceed();
        }
    }
}