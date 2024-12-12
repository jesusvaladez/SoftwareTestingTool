package org.apache.ambari.server.agent;
import org.codehaus.jackson.annotate.JsonProperty;
public class ComponentRecoveryReport {
    private java.lang.String name;

    private int numAttempts;

    private boolean limitReached;

    @org.codehaus.jackson.annotate.JsonProperty("name")
    public java.lang.String getName() {
        return name;
    }

    @org.codehaus.jackson.annotate.JsonProperty("name")
    public void setName(java.lang.String name) {
        this.name = name;
    }

    @org.codehaus.jackson.annotate.JsonProperty("num_attempts")
    public int getNumAttempts() {
        return numAttempts;
    }

    @org.codehaus.jackson.annotate.JsonProperty("num_attempts")
    public void setNumAttempts(int numAttempts) {
        this.numAttempts = numAttempts;
    }

    @org.codehaus.jackson.annotate.JsonProperty("limit_reached")
    public boolean getLimitReached() {
        return limitReached;
    }

    @org.codehaus.jackson.annotate.JsonProperty("limit_reached")
    public void setLimitReached(boolean limitReached) {
        this.limitReached = limitReached;
    }

    @java.lang.Override
    public java.lang.String toString() {
        return ((((((((("ComponentRecoveryReport{" + "name='") + name) + '\'') + ", numFailures='") + numAttempts) + '\'') + ", limitReached='") + limitReached) + '\'') + '}';
    }
}