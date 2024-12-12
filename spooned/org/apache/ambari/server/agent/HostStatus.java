package org.apache.ambari.server.agent;
import org.codehaus.jackson.annotate.JsonProperty;
public class HostStatus {
    org.apache.ambari.server.agent.HostStatus.Status status;

    java.lang.String cause;

    public HostStatus(org.apache.ambari.server.agent.HostStatus.Status status, java.lang.String cause) {
        super();
        this.status = status;
        this.cause = cause;
    }

    public HostStatus() {
        super();
    }

    public enum Status {

        HEALTHY,
        UNHEALTHY;}

    @org.codehaus.jackson.annotate.JsonProperty("status")
    @com.fasterxml.jackson.annotation.JsonProperty("status")
    public org.apache.ambari.server.agent.HostStatus.Status getStatus() {
        return status;
    }

    @org.codehaus.jackson.annotate.JsonProperty("status")
    @com.fasterxml.jackson.annotation.JsonProperty("status")
    public void setStatus(org.apache.ambari.server.agent.HostStatus.Status status) {
        this.status = status;
    }

    @org.codehaus.jackson.annotate.JsonProperty("cause")
    @com.fasterxml.jackson.annotation.JsonProperty("cause")
    public java.lang.String getCause() {
        return cause;
    }

    @org.codehaus.jackson.annotate.JsonProperty("cause")
    @com.fasterxml.jackson.annotation.JsonProperty("cause")
    public void setCause(java.lang.String cause) {
        this.cause = cause;
    }

    @java.lang.Override
    public java.lang.String toString() {
        return (((("HostStatus{" + "status=") + status) + ", cause='") + cause) + '}';
    }
}