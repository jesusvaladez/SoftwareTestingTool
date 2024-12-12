package org.apache.ambari.server.agent.stomp.dto;
public abstract class HashAndTimestampIgnoreMixIn {
    @com.fasterxml.jackson.annotation.JsonIgnore
    abstract java.lang.String getHash();

    @com.fasterxml.jackson.annotation.JsonIgnore
    abstract java.lang.String getTimestamp();
}