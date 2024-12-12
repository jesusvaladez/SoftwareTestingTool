package org.apache.ambari.server.state.quicklinks;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonSerialize;
@org.codehaus.jackson.map.annotate.JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
@org.codehaus.jackson.annotate.JsonIgnoreProperties(ignoreUnknown = true)
public class Protocol {
    @org.codehaus.jackson.annotate.JsonProperty("type")
    private java.lang.String type;

    @org.codehaus.jackson.annotate.JsonProperty("checks")
    private java.util.List<org.apache.ambari.server.state.quicklinks.Check> checks;

    public java.lang.String getType() {
        return type;
    }

    public void setType(java.lang.String type) {
        this.type = type;
    }

    public java.util.List<org.apache.ambari.server.state.quicklinks.Check> getChecks() {
        return checks;
    }

    public void setChecks(java.util.List<org.apache.ambari.server.state.quicklinks.Check> checks) {
        this.checks = checks;
    }
}