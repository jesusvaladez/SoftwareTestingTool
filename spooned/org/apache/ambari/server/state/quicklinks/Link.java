package org.apache.ambari.server.state.quicklinks;
import javax.annotation.Nullable;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonSerialize;
@org.codehaus.jackson.map.annotate.JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
@org.codehaus.jackson.annotate.JsonIgnoreProperties(ignoreUnknown = true)
public class Link {
    @org.codehaus.jackson.annotate.JsonProperty("name")
    private java.lang.String name;

    @org.codehaus.jackson.annotate.JsonProperty("label")
    private java.lang.String label;

    @org.codehaus.jackson.annotate.JsonProperty("component_name")
    private java.lang.String componentName;

    @org.codehaus.jackson.annotate.JsonProperty("requires_user_name")
    private java.lang.String requiresUserName;

    @org.codehaus.jackson.annotate.JsonProperty("url")
    private java.lang.String url;

    @org.codehaus.jackson.annotate.JsonProperty("port")
    private org.apache.ambari.server.state.quicklinks.Port port;

    @org.codehaus.jackson.annotate.JsonProperty("host")
    private org.apache.ambari.server.state.quicklinks.Host host;

    @org.codehaus.jackson.annotate.JsonProperty("protocol")
    private org.apache.ambari.server.state.quicklinks.Protocol protocol;

    @org.codehaus.jackson.annotate.JsonProperty("attributes")
    private java.util.List<java.lang.String> attributes;

    @org.codehaus.jackson.annotate.JsonProperty("visible")
    private boolean visible = true;

    public java.lang.String getName() {
        return name;
    }

    public void setName(java.lang.String name) {
        this.name = name;
    }

    public java.lang.String getLabel() {
        return label;
    }

    public void setLabel(java.lang.String label) {
        this.label = label;
    }

    public java.lang.String getComponentName() {
        return componentName;
    }

    public void setComponentName(java.lang.String componentName) {
        this.componentName = componentName;
    }

    public java.lang.String getUrl() {
        return url;
    }

    public void setUrl(java.lang.String url) {
        this.url = url;
    }

    public java.lang.String getRequiresUserName() {
        return requiresUserName;
    }

    public void setRequiresUserName(java.lang.String requiresUserName) {
        this.requiresUserName = requiresUserName;
    }

    public org.apache.ambari.server.state.quicklinks.Port getPort() {
        return port;
    }

    public org.apache.ambari.server.state.quicklinks.Host getHost() {
        return host;
    }

    public void setPort(org.apache.ambari.server.state.quicklinks.Port port) {
        this.port = port;
    }

    public org.apache.ambari.server.state.quicklinks.Protocol getProtocol() {
        return protocol;
    }

    public void setProtocol(org.apache.ambari.server.state.quicklinks.Protocol protocol) {
        this.protocol = protocol;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    @javax.annotation.Nullable
    public java.util.List<java.lang.String> getAttributes() {
        return attributes;
    }

    public void setAttributes(java.util.List<java.lang.String> attributes) {
        this.attributes = attributes;
    }

    public boolean isRemoved() {
        return (((null == port) && (null == url)) && (null == label)) && (null == requiresUserName);
    }

    public void mergeWithParent(org.apache.ambari.server.state.quicklinks.Link parentLink) {
        if (null == parentLink)
            return;

        if ((null == label) && (null != parentLink.getLabel()))
            label = parentLink.getLabel();

        if ((null == componentName) && (null != parentLink.getComponentName()))
            componentName = parentLink.getComponentName();

        if ((null == url) && (null != parentLink.getUrl()))
            url = parentLink.getUrl();

        if ((null == requiresUserName) && (null != parentLink.getRequiresUserName()))
            requiresUserName = parentLink.getRequiresUserName();

        if (null == port) {
            port = parentLink.getPort();
        } else {
            port.mergetWithParent(parentLink.getPort());
        }
        if (null == host) {
            host = parentLink.getHost();
        } else {
            host.mergeWithParent(parentLink.getHost());
        }
        if ((null == attributes) && (null != parentLink.attributes)) {
            attributes = parentLink.attributes;
        }
    }
}