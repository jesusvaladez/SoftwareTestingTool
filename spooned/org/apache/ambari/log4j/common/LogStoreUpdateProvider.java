package org.apache.ambari.log4j.common;
import org.apache.log4j.spi.LoggingEvent;
public interface LogStoreUpdateProvider {
    void init(java.sql.Connection connection) throws java.io.IOException;

    void update(org.apache.log4j.spi.LoggingEvent originalEvent, java.lang.Object parsedEvent) throws java.io.IOException;
}