package org.apache.ambari.server.api.services;
public class PersistKeyValueImplTest extends org.junit.Assert {
    public static final int NUMB_THREADS = 1000;

    private com.google.inject.Injector injector;

    @org.junit.Before
    public void setUp() throws java.lang.Exception {
        injector = com.google.inject.Guice.createInjector(new org.apache.ambari.server.orm.InMemoryDefaultTestModule());
        injector.getInstance(org.apache.ambari.server.orm.GuiceJpaInitializer.class);
    }

    @org.junit.After
    public void tearDown() throws java.lang.Exception {
        org.apache.ambari.server.H2DatabaseCleaner.clearDatabaseAndStopPersistenceService(injector);
    }

    @org.junit.Test
    public void testStore() throws java.lang.Exception {
        org.apache.ambari.server.api.services.PersistKeyValueImpl impl = injector.getInstance(org.apache.ambari.server.api.services.PersistKeyValueImpl.class);
        java.util.Map<java.lang.String, java.lang.String> map = impl.getAllKeyValues();
        org.junit.Assert.assertEquals(0, map.size());
        impl.put("key1", "value1");
        impl.put("key2", "value2");
        map = impl.getAllKeyValues();
        org.junit.Assert.assertEquals(2, map.size());
        org.junit.Assert.assertEquals("value1", impl.getValue("key1"));
        org.junit.Assert.assertEquals("value2", impl.getValue("key2"));
        org.junit.Assert.assertEquals(map.get("key1"), impl.getValue("key1"));
        impl.put("key1", "value1-2");
        org.junit.Assert.assertEquals("value1-2", impl.getValue("key1"));
        org.junit.Assert.assertEquals(2, map.size());
        java.lang.StringBuilder largeValueBuilder = new java.lang.StringBuilder();
        for (int i = 0; i < 320; i++) {
            largeValueBuilder.append("0123456789");
        }
        java.lang.String largeValue = largeValueBuilder.toString();
        impl.put("key3", largeValue);
        org.junit.Assert.assertEquals(largeValue, impl.getValue("key3"));
    }

    @org.junit.Test
    public void testMultiThreaded() throws java.lang.Exception {
        final org.apache.ambari.server.api.services.PersistKeyValueImpl impl = injector.getInstance(org.apache.ambari.server.api.services.PersistKeyValueImpl.class);
        java.lang.Thread[] threads = new java.lang.Thread[org.apache.ambari.server.api.services.PersistKeyValueImplTest.NUMB_THREADS];
        for (int i = 0; i < org.apache.ambari.server.api.services.PersistKeyValueImplTest.NUMB_THREADS; ++i) {
            threads[i] = new java.lang.Thread() {
                @java.lang.Override
                public void run() {
                    for (int i = 0; i < 100; ++i) {
                        impl.put("key1", "value1");
                        impl.put("key2", "value2");
                        impl.put("key3", "value3");
                        impl.put("key4", "value4");
                    }
                }
            };
        }
        for (int i = 0; i < org.apache.ambari.server.api.services.PersistKeyValueImplTest.NUMB_THREADS; ++i) {
            threads[i].start();
        }
        for (int i = 0; i < org.apache.ambari.server.api.services.PersistKeyValueImplTest.NUMB_THREADS; ++i) {
            threads[i].join();
        }
    }
}