package org.apache.ambari.server.controller.logging;
import org.codehaus.jackson.annotate.JsonProperty;
public class HostComponentLoggingInfo {
    private java.lang.String componentName;

    private java.util.List<org.apache.ambari.server.controller.logging.LogFileDefinitionInfo> listOfLogFileDefinitions;

    private java.util.List<org.apache.ambari.server.controller.logging.NameValuePair> listOfLogLevels;

    public HostComponentLoggingInfo() {
    }

    @org.codehaus.jackson.annotate.JsonProperty("name")
    public java.lang.String getComponentName() {
        return componentName;
    }

    @org.codehaus.jackson.annotate.JsonProperty("name")
    public void setComponentName(java.lang.String componentName) {
        this.componentName = componentName;
    }

    @org.codehaus.jackson.annotate.JsonProperty("logs")
    public java.util.List<org.apache.ambari.server.controller.logging.LogFileDefinitionInfo> getListOfLogFileDefinitions() {
        return listOfLogFileDefinitions;
    }

    @org.codehaus.jackson.annotate.JsonProperty("logs")
    public void setListOfLogFileDefinitions(java.util.List<org.apache.ambari.server.controller.logging.LogFileDefinitionInfo> listOfLogFileDefinitions) {
        this.listOfLogFileDefinitions = listOfLogFileDefinitions;
    }

    @org.codehaus.jackson.annotate.JsonProperty("log_level_counts")
    public java.util.List<org.apache.ambari.server.controller.logging.NameValuePair> getListOfLogLevels() {
        return listOfLogLevels;
    }

    @org.codehaus.jackson.annotate.JsonProperty("log_level_counts")
    public void setListOfLogLevels(java.util.List<org.apache.ambari.server.controller.logging.NameValuePair> listOfLogLevels) {
        this.listOfLogLevels = listOfLogLevels;
    }
}