package org.apache.ambari.server.state.theme;
import io.swagger.annotations.ApiModelProperty;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonSerialize;
@org.codehaus.jackson.map.annotate.JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
@org.codehaus.jackson.annotate.JsonIgnoreProperties(ignoreUnknown = true)
public class Theme implements org.apache.ambari.server.controller.ApiModel {
    @org.codehaus.jackson.annotate.JsonProperty("description")
    private java.lang.String description;

    @org.codehaus.jackson.annotate.JsonProperty("name")
    private java.lang.String name;

    @org.codehaus.jackson.annotate.JsonProperty("configuration")
    private org.apache.ambari.server.state.theme.ThemeConfiguration themeConfiguration;

    @io.swagger.annotations.ApiModelProperty(name = "description")
    public java.lang.String getDescription() {
        return description;
    }

    public void setDescription(java.lang.String description) {
        this.description = description;
    }

    @io.swagger.annotations.ApiModelProperty(name = "name")
    public java.lang.String getName() {
        return name;
    }

    public void setName(java.lang.String name) {
        this.name = name;
    }

    @io.swagger.annotations.ApiModelProperty(name = "configuration")
    public org.apache.ambari.server.state.theme.ThemeConfiguration getThemeConfiguration() {
        return themeConfiguration;
    }

    public void setThemeConfiguration(org.apache.ambari.server.state.theme.ThemeConfiguration themeConfiguration) {
        this.themeConfiguration = themeConfiguration;
    }

    public void mergeWithParent(org.apache.ambari.server.state.theme.Theme parent) {
        if (parent == null) {
            return;
        }
        if (name == null) {
            name = parent.name;
        }
        if (description == null) {
            description = parent.description;
        }
        if (themeConfiguration == null) {
            themeConfiguration = parent.themeConfiguration;
        } else {
            themeConfiguration.mergeWithParent(parent.themeConfiguration);
        }
    }
}