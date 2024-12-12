package org.apache.ambari.log4j.common;
import org.apache.log4j.spi.LoggingEvent;
public interface LogParser {
    void addEventToParse(org.apache.log4j.spi.LoggingEvent event);

    java.lang.Object getParseResult() throws java.io.IOException;
}