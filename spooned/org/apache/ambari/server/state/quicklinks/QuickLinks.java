package org.apache.ambari.server.state.quicklinks;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonSerialize;
@org.codehaus.jackson.map.annotate.JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
@org.codehaus.jackson.annotate.JsonIgnoreProperties(ignoreUnknown = true)
public class QuickLinks {
    @org.codehaus.jackson.annotate.JsonProperty("description")
    private java.lang.String description;

    @org.codehaus.jackson.annotate.JsonProperty("name")
    private java.lang.String name;

    @org.codehaus.jackson.annotate.JsonProperty("configuration")
    private org.apache.ambari.server.state.quicklinks.QuickLinksConfiguration quickLinksConfiguration;

    public java.lang.String getDescription() {
        return description;
    }

    public void setDescription(java.lang.String description) {
        this.description = description;
    }

    public java.lang.String getName() {
        return name;
    }

    public void setName(java.lang.String name) {
        this.name = name;
    }

    public org.apache.ambari.server.state.quicklinks.QuickLinksConfiguration getQuickLinksConfiguration() {
        return quickLinksConfiguration;
    }

    public void setQuickLinksConfiguration(org.apache.ambari.server.state.quicklinks.QuickLinksConfiguration quickLinksConfiguration) {
        this.quickLinksConfiguration = quickLinksConfiguration;
    }

    public void mergeWithParent(org.apache.ambari.server.state.quicklinks.QuickLinks parent) {
        if (parent == null) {
            return;
        }
        if (name == null) {
            name = parent.name;
        }
        if (description == null) {
            description = parent.description;
        }
        if (quickLinksConfiguration == null) {
            quickLinksConfiguration = parent.quickLinksConfiguration;
        } else {
            quickLinksConfiguration.mergeWithParent(parent.quickLinksConfiguration);
        }
    }
}