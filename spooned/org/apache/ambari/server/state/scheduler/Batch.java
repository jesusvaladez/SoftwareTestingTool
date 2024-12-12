package org.apache.ambari.server.state.scheduler;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonSerialize;
public class Batch {
    private final java.util.List<org.apache.ambari.server.state.scheduler.BatchRequest> batchRequests = new java.util.ArrayList<>();

    private org.apache.ambari.server.state.scheduler.BatchSettings batchSettings;

    @org.codehaus.jackson.map.annotate.JsonSerialize(include = JsonSerialize.Inclusion.NON_EMPTY)
    @org.codehaus.jackson.annotate.JsonProperty("batch_requests")
    public java.util.List<org.apache.ambari.server.state.scheduler.BatchRequest> getBatchRequests() {
        return batchRequests;
    }

    @org.codehaus.jackson.map.annotate.JsonSerialize(include = JsonSerialize.Inclusion.NON_EMPTY)
    @org.codehaus.jackson.annotate.JsonProperty("batch_settings")
    public org.apache.ambari.server.state.scheduler.BatchSettings getBatchSettings() {
        return batchSettings;
    }

    public void setBatchSettings(org.apache.ambari.server.state.scheduler.BatchSettings batchSettings) {
        this.batchSettings = batchSettings;
    }
}