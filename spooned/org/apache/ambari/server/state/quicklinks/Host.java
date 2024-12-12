package org.apache.ambari.server.state.quicklinks;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonSerialize;
@org.codehaus.jackson.map.annotate.JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
@org.codehaus.jackson.annotate.JsonIgnoreProperties(ignoreUnknown = true)
public class Host {
    @org.codehaus.jackson.annotate.JsonProperty("http_property")
    private java.lang.String httpProperty;

    @org.codehaus.jackson.annotate.JsonProperty("https_property")
    private java.lang.String httpsProperty;

    @org.codehaus.jackson.annotate.JsonProperty("site")
    private java.lang.String site;

    public java.lang.String getHttpProperty() {
        return httpProperty;
    }

    public java.lang.String getHttpsProperty() {
        return httpsProperty;
    }

    public java.lang.String getSite() {
        return site;
    }

    public void mergeWithParent(org.apache.ambari.server.state.quicklinks.Host parentHost) {
        if (null == parentHost) {
            return;
        }
        if ((null == httpProperty) && (null != parentHost.getHttpProperty())) {
            httpProperty = parentHost.getHttpProperty();
        }
        if ((null == httpsProperty) && (null != parentHost.getHttpsProperty())) {
            httpsProperty = parentHost.getHttpsProperty();
        }
        if ((null == site) && (null != parentHost.getSite())) {
            site = parentHost.getSite();
        }
    }
}