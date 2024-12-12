package org.apache.ambari.server.state.quicklinks;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonSerialize;
@org.codehaus.jackson.map.annotate.JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
@org.codehaus.jackson.annotate.JsonIgnoreProperties(ignoreUnknown = true)
public class QuickLinksConfiguration {
    @org.codehaus.jackson.annotate.JsonProperty("protocol")
    private org.apache.ambari.server.state.quicklinks.Protocol protocol;

    @org.codehaus.jackson.annotate.JsonProperty("links")
    private java.util.List<org.apache.ambari.server.state.quicklinks.Link> links;

    public org.apache.ambari.server.state.quicklinks.Protocol getProtocol() {
        return protocol;
    }

    public void setProtocol(org.apache.ambari.server.state.quicklinks.Protocol protocol) {
        this.protocol = protocol;
    }

    public java.util.List<org.apache.ambari.server.state.quicklinks.Link> getLinks() {
        return links;
    }

    public void setLinks(java.util.List<org.apache.ambari.server.state.quicklinks.Link> links) {
        this.links = links;
    }

    public void mergeWithParent(org.apache.ambari.server.state.quicklinks.QuickLinksConfiguration parent) {
        if (parent == null) {
            return;
        }
        if (protocol == null)
            protocol = parent.getProtocol();

        if (links == null) {
            links = parent.getLinks();
        } else if (parent.getLinks() != null) {
            links = mergeLinks(parent.getLinks(), links);
        }
    }

    private java.util.List<org.apache.ambari.server.state.quicklinks.Link> mergeLinks(java.util.List<org.apache.ambari.server.state.quicklinks.Link> parentLinks, java.util.List<org.apache.ambari.server.state.quicklinks.Link> childLinks) {
        java.util.Map<java.lang.String, org.apache.ambari.server.state.quicklinks.Link> mergedLinks = new java.util.HashMap<>();
        for (org.apache.ambari.server.state.quicklinks.Link parentLink : parentLinks) {
            mergedLinks.put(parentLink.getName(), parentLink);
        }
        for (org.apache.ambari.server.state.quicklinks.Link childLink : childLinks) {
            if (childLink.getName() != null) {
                if (childLink.isRemoved()) {
                    mergedLinks.remove(childLink.getName());
                } else {
                    org.apache.ambari.server.state.quicklinks.Link parentLink = mergedLinks.get(childLink.getName());
                    childLink.mergeWithParent(parentLink);
                    mergedLinks.put(childLink.getName(), childLink);
                }
            }
        }
        return new java.util.ArrayList<>(mergedLinks.values());
    }
}