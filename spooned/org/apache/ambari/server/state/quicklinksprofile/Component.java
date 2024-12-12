package org.apache.ambari.server.state.quicklinksprofile;
@com.fasterxml.jackson.annotation.JsonInclude(com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL)
@com.fasterxml.jackson.annotation.JsonIgnoreProperties(ignoreUnknown = true)
public class Component {
    @com.fasterxml.jackson.annotation.JsonProperty("name")
    private java.lang.String name;

    @com.fasterxml.jackson.annotation.JsonProperty("filters")
    private java.util.List<org.apache.ambari.server.state.quicklinksprofile.Filter> filters;

    static org.apache.ambari.server.state.quicklinksprofile.Component create(java.lang.String name, java.util.List<org.apache.ambari.server.state.quicklinksprofile.Filter> filters) {
        com.google.common.base.Preconditions.checkNotNull(name, "Component name must not be null");
        org.apache.ambari.server.state.quicklinksprofile.Component component = new org.apache.ambari.server.state.quicklinksprofile.Component();
        component.setName(name);
        component.setFilters(filters);
        return component;
    }

    public java.lang.String getName() {
        return name;
    }

    public void setName(java.lang.String name) {
        this.name = name;
    }

    public java.util.List<org.apache.ambari.server.state.quicklinksprofile.Filter> getFilters() {
        return null != filters ? filters : java.util.Collections.emptyList();
    }

    public void setFilters(java.util.List<org.apache.ambari.server.state.quicklinksprofile.Filter> filters) {
        this.filters = filters;
    }
}