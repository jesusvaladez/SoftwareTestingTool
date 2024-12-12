package org.apache.ambari.server.controller.logging;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;
@org.codehaus.jackson.annotate.JsonIgnoreProperties(ignoreUnknown = true)
public class HostLogFilesResponse {
    private java.util.Map<java.lang.String, java.util.List<java.lang.String>> hostLogFiles;

    @org.codehaus.jackson.annotate.JsonProperty("hostLogFiles")
    public java.util.Map<java.lang.String, java.util.List<java.lang.String>> getHostLogFiles() {
        return hostLogFiles;
    }

    public void setHostLogFiles(java.util.Map<java.lang.String, java.util.List<java.lang.String>> hostLogFiles) {
        this.hostLogFiles = hostLogFiles;
    }
}