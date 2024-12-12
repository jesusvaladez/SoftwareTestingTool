package org.apache.ambari.server.api.query.render;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonSerialize;
public final class AlertStateValues {
    @org.codehaus.jackson.annotate.JsonProperty("count")
    @com.fasterxml.jackson.annotation.JsonProperty("count")
    public int Count = 0;

    @org.codehaus.jackson.annotate.JsonProperty("original_timestamp")
    @com.fasterxml.jackson.annotation.JsonProperty("original_timestamp")
    public long Timestamp = 0;

    @org.codehaus.jackson.annotate.JsonProperty("maintenance_count")
    @com.fasterxml.jackson.annotation.JsonProperty("maintenance_count")
    public int MaintenanceCount = 0;

    @org.codehaus.jackson.annotate.JsonProperty("latest_text")
    @com.fasterxml.jackson.annotation.JsonProperty("latest_text")
    @org.codehaus.jackson.map.annotate.JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
    @com.fasterxml.jackson.annotation.JsonInclude(com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL)
    public java.lang.String AlertText = null;

    @java.lang.Override
    public boolean equals(java.lang.Object o) {
        if (this == o)
            return true;

        if ((o == null) || (getClass() != o.getClass()))
            return false;

        org.apache.ambari.server.api.query.render.AlertStateValues that = ((org.apache.ambari.server.api.query.render.AlertStateValues) (o));
        if (Count != that.Count)
            return false;

        if (Timestamp != that.Timestamp)
            return false;

        if (MaintenanceCount != that.MaintenanceCount)
            return false;

        return AlertText != null ? AlertText.equals(that.AlertText) : that.AlertText == null;
    }

    @java.lang.Override
    public int hashCode() {
        int result = Count;
        result = (31 * result) + ((int) (Timestamp ^ (Timestamp >>> 32)));
        result = (31 * result) + MaintenanceCount;
        result = (31 * result) + (AlertText != null ? AlertText.hashCode() : 0);
        return result;
    }
}