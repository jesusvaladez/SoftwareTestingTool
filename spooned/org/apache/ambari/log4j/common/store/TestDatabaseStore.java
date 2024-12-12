package org.apache.ambari.log4j.common.store;
import org.apache.log4j.spi.LoggingEvent;
public class TestDatabaseStore extends junit.framework.TestCase {
    class SampleLogStoreUpdateProvider implements org.apache.ambari.log4j.common.LogStoreUpdateProvider {
        public void init(java.sql.Connection connection) throws java.io.IOException {
        }

        public void update(org.apache.log4j.spi.LoggingEvent originalEvent, java.lang.Object parsedEvent) throws java.io.IOException {
        }
    }

    public void testDatabaseStore() throws java.io.IOException {
        org.apache.ambari.log4j.common.store.DatabaseStore store = new org.apache.ambari.log4j.common.store.DatabaseStore(org.apache.ambari.log4j.common.store.TestDatabaseStore.class.getName(), "", "", "", new org.apache.ambari.log4j.common.store.TestDatabaseStore.SampleLogStoreUpdateProvider());
        store.close();
    }
}