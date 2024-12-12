package org.apache.ambari.server.state.quicklinksprofile;
public class LinkAttributeFilter extends org.apache.ambari.server.state.quicklinksprofile.Filter {
    static final java.lang.String LINK_ATTRIBUTE = "link_attribute";

    @com.fasterxml.jackson.annotation.JsonProperty(org.apache.ambari.server.state.quicklinksprofile.LinkAttributeFilter.LINK_ATTRIBUTE)
    private java.lang.String linkAttribute;

    public java.lang.String getLinkAttribute() {
        return linkAttribute;
    }

    public void setLinkAttribute(java.lang.String linkAttribute) {
        this.linkAttribute = linkAttribute;
    }

    @java.lang.Override
    public boolean accept(org.apache.ambari.server.state.quicklinks.Link link) {
        return link.getAttributes().contains(linkAttribute);
    }

    @java.lang.Override
    public boolean equals(java.lang.Object o) {
        if (this == o)
            return true;

        if ((o == null) || (getClass() != o.getClass()))
            return false;

        org.apache.ambari.server.state.quicklinksprofile.LinkAttributeFilter that = ((org.apache.ambari.server.state.quicklinksprofile.LinkAttributeFilter) (o));
        return (isVisible() == that.isVisible()) && java.util.Objects.equals(linkAttribute, that.linkAttribute);
    }

    @java.lang.Override
    public int hashCode() {
        return java.util.Objects.hash(isVisible(), linkAttribute);
    }

    @java.lang.Override
    public java.lang.String toString() {
        return com.google.common.base.MoreObjects.toStringHelper(this).add("linkAttribute", linkAttribute).toString();
    }
}