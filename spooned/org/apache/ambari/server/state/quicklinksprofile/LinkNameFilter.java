package org.apache.ambari.server.state.quicklinksprofile;
import javax.annotation.Nullable;
@com.fasterxml.jackson.annotation.JsonInclude(com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL)
public class LinkNameFilter extends org.apache.ambari.server.state.quicklinksprofile.Filter {
    static final java.lang.String LINK_NAME = "link_name";

    static final java.lang.String LINK_URL = "link_url";

    @com.fasterxml.jackson.annotation.JsonProperty(org.apache.ambari.server.state.quicklinksprofile.LinkNameFilter.LINK_NAME)
    private java.lang.String linkName;

    @com.fasterxml.jackson.annotation.JsonProperty(org.apache.ambari.server.state.quicklinksprofile.LinkNameFilter.LINK_URL)
    private java.lang.String linkUrl;

    @com.fasterxml.jackson.annotation.JsonProperty(org.apache.ambari.server.state.quicklinksprofile.LinkNameFilter.LINK_NAME)
    public java.lang.String getLinkName() {
        return linkName;
    }

    @com.fasterxml.jackson.annotation.JsonProperty(org.apache.ambari.server.state.quicklinksprofile.LinkNameFilter.LINK_NAME)
    public void setLinkName(java.lang.String linkName) {
        this.linkName = linkName;
    }

    @com.fasterxml.jackson.annotation.JsonProperty(org.apache.ambari.server.state.quicklinksprofile.LinkNameFilter.LINK_URL)
    @javax.annotation.Nullable
    public java.lang.String getLinkUrl() {
        return linkUrl;
    }

    @com.fasterxml.jackson.annotation.JsonProperty(org.apache.ambari.server.state.quicklinksprofile.LinkNameFilter.LINK_URL)
    public void setLinkUrl(@javax.annotation.Nullable
    java.lang.String linkUrl) {
        this.linkUrl = linkUrl;
    }

    @java.lang.Override
    public boolean accept(org.apache.ambari.server.state.quicklinks.Link link) {
        return java.util.Objects.equals(link.getName(), linkName);
    }

    @java.lang.Override
    public boolean equals(java.lang.Object o) {
        if (this == o)
            return true;

        if ((o == null) || (getClass() != o.getClass()))
            return false;

        org.apache.ambari.server.state.quicklinksprofile.LinkNameFilter that = ((org.apache.ambari.server.state.quicklinksprofile.LinkNameFilter) (o));
        return (java.util.Objects.equals(linkName, that.linkName) && java.util.Objects.equals(linkUrl, that.linkUrl)) && (isVisible() == that.isVisible());
    }

    @java.lang.Override
    public int hashCode() {
        return java.util.Objects.hash(linkName, linkUrl);
    }

    @java.lang.Override
    public java.lang.String toString() {
        return com.google.common.base.MoreObjects.toStringHelper(this).add("linkName", linkName).add("linkUrl", linkUrl).add("visible", isVisible()).toString();
    }

    static java.util.stream.Stream<org.apache.ambari.server.state.quicklinksprofile.LinkNameFilter> getLinkNameFilters(java.util.stream.Stream<org.apache.ambari.server.state.quicklinksprofile.Filter> input) {
        return org.apache.ambari.server.utils.StreamUtils.instancesOf(input, org.apache.ambari.server.state.quicklinksprofile.LinkNameFilter.class);
    }
}