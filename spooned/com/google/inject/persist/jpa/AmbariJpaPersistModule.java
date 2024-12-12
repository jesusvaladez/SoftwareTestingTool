package com.google.inject.persist.jpa;
import com.google.inject.persist.PersistModule;
import com.google.inject.persist.PersistService;
import com.google.inject.persist.UnitOfWork;
import com.google.inject.persist.finder.DynamicFinder;
import com.google.inject.persist.finder.Finder;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
public class AmbariJpaPersistModule extends com.google.inject.persist.PersistModule {
    private final java.lang.String jpaUnit;

    public AmbariJpaPersistModule(java.lang.String jpaUnit) {
        if ((null != jpaUnit) && (jpaUnit.length() > 0)) {
            this.jpaUnit = jpaUnit;
        } else {
            throw new java.lang.IllegalArgumentException("jpaUnit should not be null");
        }
    }

    private java.util.Map<?, ?> properties;

    private org.aopalliance.intercept.MethodInterceptor transactionInterceptor;

    @java.lang.Override
    protected void configurePersistence() {
        bindConstant().annotatedWith(com.google.inject.persist.jpa.Jpa.class).to(jpaUnit);
        bind(com.google.inject.persist.jpa.AmbariJpaPersistService.class).in(com.google.inject.Singleton.class);
        bind(com.google.inject.persist.PersistService.class).to(com.google.inject.persist.jpa.AmbariJpaPersistService.class);
        bind(com.google.inject.persist.UnitOfWork.class).to(com.google.inject.persist.jpa.AmbariJpaPersistService.class);
        bind(javax.persistence.EntityManager.class).toProvider(com.google.inject.persist.jpa.AmbariJpaPersistService.class);
        bind(javax.persistence.EntityManagerFactory.class).toProvider(JpaPersistService.EntityManagerFactoryProvider.class);
        transactionInterceptor = new org.apache.ambari.server.orm.AmbariJpaLocalTxnInterceptor();
        requestInjection(transactionInterceptor);
        org.aopalliance.intercept.MethodInterceptor sessionInterceptor = new org.apache.ambari.server.orm.AmbariLocalSessionInterceptor();
        requestInjection(sessionInterceptor);
        for (java.lang.Class<?> finder : dynamicFinders) {
            bindFinder(finder);
        }
        bindInterceptor(com.google.inject.matcher.Matchers.annotatedWith(org.apache.ambari.server.orm.RequiresSession.class), com.google.inject.matcher.Matchers.any(), sessionInterceptor);
        bindInterceptor(com.google.inject.matcher.Matchers.any(), com.google.inject.matcher.Matchers.annotatedWith(org.apache.ambari.server.orm.RequiresSession.class), sessionInterceptor);
    }

    @java.lang.Override
    protected org.aopalliance.intercept.MethodInterceptor getTransactionInterceptor() {
        return transactionInterceptor;
    }

    @com.google.inject.Provides
    @com.google.inject.persist.jpa.Jpa
    java.util.Map<?, ?> provideProperties() {
        return properties;
    }

    public com.google.inject.persist.jpa.AmbariJpaPersistModule properties(java.util.Map<?, ?> properties) {
        this.properties = properties;
        return this;
    }

    private final java.util.List<java.lang.Class<?>> dynamicFinders = com.google.common.collect.Lists.newArrayList();

    public <T> com.google.inject.persist.jpa.AmbariJpaPersistModule addFinder(java.lang.Class<T> iface) {
        dynamicFinders.add(iface);
        return this;
    }

    private <T> void bindFinder(java.lang.Class<T> iface) {
        if (!isDynamicFinderValid(iface)) {
            return;
        }
        java.lang.reflect.InvocationHandler finderInvoker = new java.lang.reflect.InvocationHandler() {
            @com.google.inject.Inject
            com.google.inject.persist.jpa.JpaFinderProxy finderProxy;

            @java.lang.Override
            public java.lang.Object invoke(final java.lang.Object thisObject, final java.lang.reflect.Method method, final java.lang.Object[] args) throws java.lang.Throwable {
                if (!method.isAnnotationPresent(com.google.inject.persist.finder.Finder.class)) {
                    return method.invoke(this, args);
                }
                return finderProxy.invoke(new org.aopalliance.intercept.MethodInvocation() {
                    @java.lang.Override
                    public java.lang.reflect.Method getMethod() {
                        return method;
                    }

                    @java.lang.Override
                    public java.lang.Object[] getArguments() {
                        return null == args ? new java.lang.Object[0] : args;
                    }

                    @java.lang.Override
                    public java.lang.Object proceed() throws java.lang.Throwable {
                        return method.invoke(thisObject, args);
                    }

                    @java.lang.Override
                    public java.lang.Object getThis() {
                        throw new java.lang.UnsupportedOperationException("Bottomless proxies don't expose a this.");
                    }

                    @java.lang.Override
                    public java.lang.reflect.AccessibleObject getStaticPart() {
                        throw new java.lang.UnsupportedOperationException();
                    }
                });
            }
        };
        requestInjection(finderInvoker);
        @java.lang.SuppressWarnings("unchecked")
        T proxy = ((T) (java.lang.reflect.Proxy.newProxyInstance(java.lang.Thread.currentThread().getContextClassLoader(), new java.lang.Class<?>[]{ iface }, finderInvoker)));
        bind(iface).toInstance(proxy);
    }

    private boolean isDynamicFinderValid(java.lang.Class<?> iface) {
        boolean valid = true;
        if (!iface.isInterface()) {
            addError(iface + " is not an interface. Dynamic Finders must be interfaces.");
            valid = false;
        }
        for (java.lang.reflect.Method method : iface.getMethods()) {
            com.google.inject.persist.finder.DynamicFinder finder = com.google.inject.persist.finder.DynamicFinder.from(method);
            if (null == finder) {
                addError(((("Dynamic Finder methods must be annotated with @Finder, but " + iface) + ".") + method.getName()) + " was not");
                valid = false;
            }
        }
        return valid;
    }

    private class LocalTypeLiteral<T> extends com.google.inject.TypeLiteral<T> {}
}