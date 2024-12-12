package org.apache.ambari.server.api.query.render;
import org.codehaus.jackson.annotate.JsonProperty;
public final class AlertStateSummary {
    @org.codehaus.jackson.annotate.JsonProperty("OK")
    @com.fasterxml.jackson.annotation.JsonProperty("OK")
    public final org.apache.ambari.server.api.query.render.AlertStateValues Ok = new org.apache.ambari.server.api.query.render.AlertStateValues();

    @org.codehaus.jackson.annotate.JsonProperty("WARNING")
    @com.fasterxml.jackson.annotation.JsonProperty("WARNING")
    public final org.apache.ambari.server.api.query.render.AlertStateValues Warning = new org.apache.ambari.server.api.query.render.AlertStateValues();

    @org.codehaus.jackson.annotate.JsonProperty("CRITICAL")
    @com.fasterxml.jackson.annotation.JsonProperty("CRITICAL")
    public final org.apache.ambari.server.api.query.render.AlertStateValues Critical = new org.apache.ambari.server.api.query.render.AlertStateValues();

    @org.codehaus.jackson.annotate.JsonProperty("UNKNOWN")
    @com.fasterxml.jackson.annotation.JsonProperty("UNKNOWN")
    public final org.apache.ambari.server.api.query.render.AlertStateValues Unknown = new org.apache.ambari.server.api.query.render.AlertStateValues();

    @java.lang.Override
    public boolean equals(java.lang.Object o) {
        if (this == o)
            return true;

        if ((o == null) || (getClass() != o.getClass()))
            return false;

        org.apache.ambari.server.api.query.render.AlertStateSummary that = ((org.apache.ambari.server.api.query.render.AlertStateSummary) (o));
        if (Ok != null ? !Ok.equals(that.Ok) : that.Ok != null)
            return false;

        if (Warning != null ? !Warning.equals(that.Warning) : that.Warning != null)
            return false;

        if (Critical != null ? !Critical.equals(that.Critical) : that.Critical != null)
            return false;

        return Unknown != null ? Unknown.equals(that.Unknown) : that.Unknown == null;
    }

    @java.lang.Override
    public int hashCode() {
        int result = (Ok != null) ? Ok.hashCode() : 0;
        result = (31 * result) + (Warning != null ? Warning.hashCode() : 0);
        result = (31 * result) + (Critical != null ? Critical.hashCode() : 0);
        result = (31 * result) + (Unknown != null ? Unknown.hashCode() : 0);
        return result;
    }
}