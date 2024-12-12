package org.apache.ambari.view;
public interface ReadRequest {
    public java.util.Set<java.lang.String> getPropertyIds();

    public java.lang.String getPredicate();

    public org.apache.ambari.view.ReadRequest.TemporalInfo getTemporalInfo(java.lang.String id);

    public interface TemporalInfo {
        java.lang.Long getStartTime();

        java.lang.Long getEndTime();

        java.lang.Long getStep();
    }
}