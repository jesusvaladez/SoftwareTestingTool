package org.apache.ambari.server.state.quicklinksprofile;
@com.fasterxml.jackson.annotation.JsonInclude(com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY)
@com.fasterxml.jackson.annotation.JsonIgnoreProperties(ignoreUnknown = true)
public class Service {
    @com.fasterxml.jackson.annotation.JsonProperty("name")
    private java.lang.String name;

    @com.fasterxml.jackson.annotation.JsonProperty("components")
    private java.util.List<org.apache.ambari.server.state.quicklinksprofile.Component> components;

    @com.fasterxml.jackson.annotation.JsonProperty("filters")
    private java.util.List<org.apache.ambari.server.state.quicklinksprofile.Filter> filters;

    static org.apache.ambari.server.state.quicklinksprofile.Service create(java.lang.String name, java.util.List<org.apache.ambari.server.state.quicklinksprofile.Filter> filters, java.util.List<org.apache.ambari.server.state.quicklinksprofile.Component> components) {
        com.google.common.base.Preconditions.checkNotNull(name, "Service name must not be null");
        org.apache.ambari.server.state.quicklinksprofile.Service service = new org.apache.ambari.server.state.quicklinksprofile.Service();
        service.setName(name);
        service.setFilters(filters);
        service.setComponents(components);
        return service;
    }

    public java.lang.String getName() {
        return name;
    }

    public void setName(java.lang.String name) {
        this.name = name;
    }

    public java.util.List<org.apache.ambari.server.state.quicklinksprofile.Component> getComponents() {
        return null != components ? components : java.util.Collections.emptyList();
    }

    public void setComponents(java.util.List<org.apache.ambari.server.state.quicklinksprofile.Component> components) {
        this.components = components;
    }

    public java.util.List<org.apache.ambari.server.state.quicklinksprofile.Filter> getFilters() {
        return null != filters ? filters : java.util.Collections.emptyList();
    }

    public void setFilters(java.util.List<org.apache.ambari.server.state.quicklinksprofile.Filter> filters) {
        this.filters = filters;
    }
}