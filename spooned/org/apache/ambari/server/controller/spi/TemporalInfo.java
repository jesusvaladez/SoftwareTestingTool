package org.apache.ambari.server.controller.spi;
public interface TemporalInfo {
    java.lang.Long getStartTime();

    java.lang.Long getEndTime();

    java.lang.Long getStep();

    java.lang.Long getStartTimeMillis();

    java.lang.Long getEndTimeMillis();
}