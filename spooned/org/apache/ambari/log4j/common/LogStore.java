package org.apache.ambari.log4j.common;
import org.apache.log4j.spi.LoggingEvent;
public interface LogStore {
    void persist(org.apache.log4j.spi.LoggingEvent originalEvent, java.lang.Object parsedEvent) throws java.io.IOException;

    void close() throws java.io.IOException;
}