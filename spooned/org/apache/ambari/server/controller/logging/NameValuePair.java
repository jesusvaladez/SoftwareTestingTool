package org.apache.ambari.server.controller.logging;
import org.codehaus.jackson.annotate.JsonProperty;
public class NameValuePair {
    private java.lang.String name;

    private java.lang.String value;

    public NameValuePair() {
    }

    public NameValuePair(java.lang.String name, java.lang.String value) {
        this.name = name;
        this.value = value;
    }

    @org.codehaus.jackson.annotate.JsonProperty("name")
    public java.lang.String getName() {
        return name;
    }

    @org.codehaus.jackson.annotate.JsonProperty("name")
    public void setName(java.lang.String name) {
        this.name = name;
    }

    @org.codehaus.jackson.annotate.JsonProperty("value")
    public java.lang.String getValue() {
        return value;
    }

    @org.codehaus.jackson.annotate.JsonProperty("value")
    public void setValue(java.lang.String value) {
        this.value = value;
    }
}