package org.apache.ambari.server.state.quicklinks;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonSerialize;
@org.codehaus.jackson.map.annotate.JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
@org.codehaus.jackson.annotate.JsonIgnoreProperties(ignoreUnknown = true)
public class Check {
    @org.codehaus.jackson.annotate.JsonProperty("property")
    private java.lang.String property;

    @org.codehaus.jackson.annotate.JsonProperty("desired")
    private java.lang.String desired;

    @org.codehaus.jackson.annotate.JsonProperty("site")
    private java.lang.String site;

    public java.lang.String getProperty() {
        return property;
    }

    public void setProperty(java.lang.String property) {
        this.property = property;
    }

    public java.lang.String getDesired() {
        return desired;
    }

    public void setDesired(java.lang.String desired) {
        this.desired = desired;
    }

    public java.lang.String getSite() {
        return site;
    }

    public void setSite(java.lang.String site) {
        this.site = site;
    }
}