package com.google.inject.persist.jpa;
public class AmbariJpaPersistServiceTest {
    @org.junit.Test
    public void start() {
        com.google.inject.Injector injector = com.google.inject.Guice.createInjector(new org.apache.ambari.server.orm.InMemoryDefaultTestModule());
        com.google.inject.persist.jpa.AmbariJpaPersistService persistService = injector.getInstance(com.google.inject.persist.jpa.AmbariJpaPersistService.class);
        persistService.start();
        persistService.start();
    }
}