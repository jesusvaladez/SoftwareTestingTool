package org.apache.ambari.server.controller.logging;
import org.codehaus.jackson.annotate.JsonProperty;
public class LogFileDefinitionInfo {
    private java.lang.String logFileName;

    private org.apache.ambari.server.controller.logging.LogFileType logFileType;

    private java.lang.String searchEngineURL;

    private java.lang.String logFileTailURL;

    public LogFileDefinitionInfo() {
    }

    protected LogFileDefinitionInfo(java.lang.String logFileName, org.apache.ambari.server.controller.logging.LogFileType logFileType, java.lang.String searchEngineURL, java.lang.String logFileTailURL) {
        this.logFileName = logFileName;
        this.logFileType = logFileType;
        this.searchEngineURL = searchEngineURL;
        this.logFileTailURL = logFileTailURL;
    }

    @org.codehaus.jackson.annotate.JsonProperty("name")
    public java.lang.String getLogFileName() {
        return logFileName;
    }

    @org.codehaus.jackson.annotate.JsonProperty("name")
    public void setLogFileName(java.lang.String logFileName) {
        this.logFileName = logFileName;
    }

    @org.codehaus.jackson.annotate.JsonProperty("type")
    public org.apache.ambari.server.controller.logging.LogFileType getLogFileType() {
        return logFileType;
    }

    @org.codehaus.jackson.annotate.JsonProperty("type")
    public void setLogFileType(org.apache.ambari.server.controller.logging.LogFileType logFileType) {
        this.logFileType = logFileType;
    }

    @org.codehaus.jackson.annotate.JsonProperty("searchEngineURL")
    public java.lang.String getSearchEngineURL() {
        return searchEngineURL;
    }

    @org.codehaus.jackson.annotate.JsonProperty("searchEngineURL")
    public void setSearchEngineURL(java.lang.String searchEngineURL) {
        this.searchEngineURL = searchEngineURL;
    }

    @org.codehaus.jackson.annotate.JsonProperty("logFileTailURL")
    public java.lang.String getLogFileTailURL() {
        return logFileTailURL;
    }

    @org.codehaus.jackson.annotate.JsonProperty("logFileTailURL")
    public void setLogFileTailURL(java.lang.String logFileTailURL) {
        this.logFileTailURL = logFileTailURL;
    }
}