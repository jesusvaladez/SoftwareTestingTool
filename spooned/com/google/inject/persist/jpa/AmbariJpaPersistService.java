package com.google.inject.persist.jpa;
public class AmbariJpaPersistService extends com.google.inject.persist.jpa.JpaPersistService {
    private final java.util.concurrent.atomic.AtomicBoolean jpaStarted = new java.util.concurrent.atomic.AtomicBoolean(false);

    @com.google.inject.Inject
    public AmbariJpaPersistService(@com.google.inject.persist.jpa.Jpa
    java.lang.String persistenceUnitName, @com.google.inject.persist.jpa.Jpa
    java.util.Map<?, ?> persistenceProperties) {
        super(persistenceUnitName, persistenceProperties);
    }

    @java.lang.Override
    public synchronized void start() {
        if (!jpaStarted.get()) {
            super.start();
            jpaStarted.set(true);
        }
    }

    public boolean isStarted() {
        return jpaStarted.get();
    }
}