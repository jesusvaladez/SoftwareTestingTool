package org.apache.ambari.server.state.quicklinksprofile;
@com.fasterxml.jackson.annotation.JsonInclude(com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY)
@com.fasterxml.jackson.annotation.JsonIgnoreProperties(ignoreUnknown = true)
public class QuickLinksProfile {
    public static final java.lang.String SETTING_NAME_QUICKLINKS_PROFILE = "QuickLinksProfile";

    public static final java.lang.String SETTING_TYPE_AMBARI_SERVER = "ambari-server";

    @com.fasterxml.jackson.annotation.JsonProperty("filters")
    private java.util.List<org.apache.ambari.server.state.quicklinksprofile.Filter> filters;

    @com.fasterxml.jackson.annotation.JsonProperty("services")
    private java.util.List<org.apache.ambari.server.state.quicklinksprofile.Service> services;

    static org.apache.ambari.server.state.quicklinksprofile.QuickLinksProfile create(java.util.List<org.apache.ambari.server.state.quicklinksprofile.Filter> globalFilters, java.util.List<org.apache.ambari.server.state.quicklinksprofile.Service> services) {
        org.apache.ambari.server.state.quicklinksprofile.QuickLinksProfile profile = new org.apache.ambari.server.state.quicklinksprofile.QuickLinksProfile();
        profile.setFilters(globalFilters);
        profile.setServices(services);
        return profile;
    }

    public java.util.List<org.apache.ambari.server.state.quicklinksprofile.Service> getServices() {
        return services != null ? services : java.util.Collections.emptyList();
    }

    public void setServices(java.util.List<org.apache.ambari.server.state.quicklinksprofile.Service> services) {
        this.services = services;
    }

    public java.util.List<org.apache.ambari.server.state.quicklinksprofile.Filter> getFilters() {
        return null != filters ? filters : java.util.Collections.emptyList();
    }

    public void setFilters(java.util.List<org.apache.ambari.server.state.quicklinksprofile.Filter> filters) {
        this.filters = filters;
    }
}