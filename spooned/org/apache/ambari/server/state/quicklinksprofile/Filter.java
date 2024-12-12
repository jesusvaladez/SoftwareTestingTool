package org.apache.ambari.server.state.quicklinksprofile;
@com.fasterxml.jackson.databind.annotation.JsonSerialize(include = com.fasterxml.jackson.databind.annotation.JsonSerialize.Inclusion.NON_NULL)
@com.fasterxml.jackson.annotation.JsonIgnoreProperties(ignoreUnknown = true)
public abstract class Filter {
    static final java.lang.String VISIBLE = "visible";

    @com.fasterxml.jackson.annotation.JsonProperty(org.apache.ambari.server.state.quicklinksprofile.Filter.VISIBLE)
    private boolean visible;

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public abstract boolean accept(org.apache.ambari.server.state.quicklinks.Link link);

    static org.apache.ambari.server.state.quicklinksprofile.AcceptAllFilter acceptAllFilter(boolean visible) {
        org.apache.ambari.server.state.quicklinksprofile.AcceptAllFilter acceptAllFilter = new org.apache.ambari.server.state.quicklinksprofile.AcceptAllFilter();
        acceptAllFilter.setVisible(visible);
        return acceptAllFilter;
    }

    static org.apache.ambari.server.state.quicklinksprofile.LinkNameFilter linkNameFilter(java.lang.String linkName, boolean visible) {
        return org.apache.ambari.server.state.quicklinksprofile.Filter.linkNameFilter(linkName, null, visible);
    }

    static org.apache.ambari.server.state.quicklinksprofile.LinkNameFilter linkNameFilter(java.lang.String linkName, java.lang.String linkUrl, boolean visible) {
        com.google.common.base.Preconditions.checkNotNull(linkName, "Link name must not be null");
        org.apache.ambari.server.state.quicklinksprofile.LinkNameFilter linkNameFilter = new org.apache.ambari.server.state.quicklinksprofile.LinkNameFilter();
        linkNameFilter.setLinkName(linkName);
        linkNameFilter.setLinkUrl(linkUrl);
        linkNameFilter.setVisible(visible);
        return linkNameFilter;
    }

    static org.apache.ambari.server.state.quicklinksprofile.LinkAttributeFilter linkAttributeFilter(java.lang.String linkAttribute, boolean visible) {
        com.google.common.base.Preconditions.checkNotNull(linkAttribute, "Attribute name must not be null");
        org.apache.ambari.server.state.quicklinksprofile.LinkAttributeFilter linkAttributeFilter = new org.apache.ambari.server.state.quicklinksprofile.LinkAttributeFilter();
        linkAttributeFilter.setLinkAttribute(linkAttribute);
        linkAttributeFilter.setVisible(visible);
        return linkAttributeFilter;
    }
}