package org.apache.ambari.server.events;
public class AlertUpdateEvent extends org.apache.ambari.server.events.STOMPEvent {
    private java.util.Map<java.lang.Long, java.util.Map<java.lang.String, org.apache.ambari.server.api.query.render.AlertSummaryGroupedRenderer.AlertDefinitionSummary>> summaries = new java.util.HashMap<>();

    public AlertUpdateEvent(java.util.Map<java.lang.Long, java.util.Map<java.lang.String, org.apache.ambari.server.api.query.render.AlertSummaryGroupedRenderer.AlertDefinitionSummary>> summaries) {
        super(org.apache.ambari.server.events.STOMPEvent.Type.ALERT);
        this.summaries = summaries;
    }

    public java.util.Map<java.lang.Long, java.util.Map<java.lang.String, org.apache.ambari.server.api.query.render.AlertSummaryGroupedRenderer.AlertDefinitionSummary>> getSummaries() {
        return summaries;
    }

    public void setSummaries(java.util.Map<java.lang.Long, java.util.Map<java.lang.String, org.apache.ambari.server.api.query.render.AlertSummaryGroupedRenderer.AlertDefinitionSummary>> summaries) {
        this.summaries = summaries;
    }

    @java.lang.Override
    public boolean equals(java.lang.Object o) {
        if (this == o)
            return true;

        if ((o == null) || (getClass() != o.getClass()))
            return false;

        org.apache.ambari.server.events.AlertUpdateEvent that = ((org.apache.ambari.server.events.AlertUpdateEvent) (o));
        return summaries != null ? summaries.equals(that.summaries) : that.summaries == null;
    }

    @java.lang.Override
    public int hashCode() {
        return summaries != null ? summaries.hashCode() : 0;
    }
}