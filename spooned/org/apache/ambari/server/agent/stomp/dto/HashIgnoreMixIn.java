package org.apache.ambari.server.agent.stomp.dto;
public abstract class HashIgnoreMixIn {
    @com.fasterxml.jackson.annotation.JsonIgnore
    abstract java.lang.String getHash();
}