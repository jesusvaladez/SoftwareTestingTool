package org.apache.ambari.server.state.quicklinks;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonSerialize;
@org.codehaus.jackson.map.annotate.JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
@org.codehaus.jackson.annotate.JsonIgnoreProperties(ignoreUnknown = true)
public class Port {
    @org.codehaus.jackson.annotate.JsonProperty("http_property")
    private java.lang.String httpProperty;

    @org.codehaus.jackson.annotate.JsonProperty("http_default_port")
    private java.lang.String httpDefaultPort;

    @org.codehaus.jackson.annotate.JsonProperty("https_property")
    private java.lang.String httpsProperty;

    @org.codehaus.jackson.annotate.JsonProperty("https_default_port")
    private java.lang.String httpsDefaultPort;

    @org.codehaus.jackson.annotate.JsonProperty("regex")
    private java.lang.String regex;

    @org.codehaus.jackson.annotate.JsonProperty("https_regex")
    private java.lang.String httpsRegex;

    @org.codehaus.jackson.annotate.JsonProperty("site")
    private java.lang.String site;

    public java.lang.String getHttpProperty() {
        return httpProperty;
    }

    public void setHttpProperty(java.lang.String httpProperty) {
        this.httpProperty = httpProperty;
    }

    public java.lang.String getHttpDefaultPort() {
        return httpDefaultPort;
    }

    public void setHttpDefaultPort(java.lang.String httpDefaultPort) {
        this.httpDefaultPort = httpDefaultPort;
    }

    public java.lang.String getHttpsProperty() {
        return httpsProperty;
    }

    public void setHttpsProperty(java.lang.String httpsProperty) {
        this.httpsProperty = httpsProperty;
    }

    public java.lang.String getHttpsDefaultPort() {
        return httpsDefaultPort;
    }

    public void setHttpsDefaultPort(java.lang.String httpsDefaultPort) {
        this.httpsDefaultPort = httpsDefaultPort;
    }

    public java.lang.String getRegex() {
        return regex;
    }

    public void setRegex(java.lang.String regex) {
        this.regex = regex;
    }

    public java.lang.String getHttpsRegex() {
        return httpsRegex;
    }

    public void setHttpsRegex(java.lang.String httpsRegex) {
        this.httpsRegex = httpsRegex;
    }

    public java.lang.String getSite() {
        return site;
    }

    public void setSite(java.lang.String site) {
        this.site = site;
    }

    public void mergetWithParent(org.apache.ambari.server.state.quicklinks.Port parentPort) {
        if (null == parentPort)
            return;

        if ((null == httpProperty) && (null != parentPort.getHttpProperty()))
            httpProperty = parentPort.getHttpProperty();

        if ((null == httpDefaultPort) && (null != parentPort.getHttpDefaultPort()))
            httpDefaultPort = parentPort.getHttpDefaultPort();

        if ((null == httpsProperty) && (null != parentPort.getHttpsProperty()))
            httpsProperty = parentPort.getHttpsProperty();

        if ((null == httpsDefaultPort) && (null != parentPort.getHttpsDefaultPort()))
            httpsDefaultPort = parentPort.getHttpsDefaultPort();

        if ((null == regex) && (null != parentPort.getRegex()))
            regex = parentPort.getRegex();

        if ((null == httpsRegex) && (null != parentPort.getHttpsRegex()))
            regex = parentPort.getHttpsRegex();

        if ((null == site) && (null != parentPort.getSite()))
            site = parentPort.getSite();

    }
}