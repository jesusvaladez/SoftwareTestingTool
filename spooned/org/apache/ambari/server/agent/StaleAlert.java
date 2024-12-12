package org.apache.ambari.server.agent;
public class StaleAlert {
    @com.fasterxml.jackson.annotation.JsonProperty("id")
    private java.lang.Long id;

    @com.fasterxml.jackson.annotation.JsonProperty("timestamp")
    private java.lang.Long timestamp;

    public StaleAlert() {
    }

    public StaleAlert(java.lang.Long id, java.lang.Long timestamp) {
        this.id = id;
        this.timestamp = timestamp;
    }

    public java.lang.Long getId() {
        return id;
    }

    public java.lang.Long getTimestamp() {
        return timestamp;
    }

    @java.lang.Override
    public java.lang.String toString() {
        return (((("StaleAlert{" + "id=") + id) + ", timestamp=") + timestamp) + '}';
    }
}