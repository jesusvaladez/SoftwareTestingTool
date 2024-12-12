package org.apache.ambari.server.controller.logging;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;
@org.codehaus.jackson.annotate.JsonIgnoreProperties(ignoreUnknown = true)
public class LogLineResult {
    private final java.util.Map<java.lang.String, java.lang.String> mapOfLogLineProperties = new java.util.HashMap<>();

    private java.lang.String clusterName;

    private java.lang.String logMethod;

    private java.lang.String logLevel;

    private java.lang.String eventCount;

    private java.lang.String ipAddress;

    private java.lang.String componentType;

    private java.lang.String sequenceNumber;

    private java.lang.String logFilePath;

    private java.lang.String sourceFile;

    private java.lang.String sourceFileLineNumber;

    private java.lang.String hostName;

    private java.lang.String logMessage;

    private java.lang.String loggerName;

    private java.lang.String id;

    private java.lang.String messageMD5;

    private java.lang.String logTime;

    private java.lang.String eventMD5;

    private java.lang.String logFileLineNumber;

    private java.lang.String ttl;

    private java.lang.String expirationTime;

    private java.lang.String version;

    private java.lang.String thread_name;

    public LogLineResult() {
    }

    public LogLineResult(java.util.Map<java.lang.String, java.lang.String> propertiesMap) {
        mapOfLogLineProperties.putAll(propertiesMap);
    }

    public java.lang.String getProperty(java.lang.String propertyName) {
        return mapOfLogLineProperties.get(propertyName);
    }

    public boolean doesPropertyExist(java.lang.String propertyName) {
        return mapOfLogLineProperties.containsKey(propertyName);
    }

    @org.codehaus.jackson.annotate.JsonProperty("cluster")
    public java.lang.String getClusterName() {
        return clusterName;
    }

    @org.codehaus.jackson.annotate.JsonProperty("cluster")
    public void setClusterName(java.lang.String clusterName) {
        this.clusterName = clusterName;
    }

    @org.codehaus.jackson.annotate.JsonProperty("method")
    public java.lang.String getLogMethod() {
        return logMethod;
    }

    @org.codehaus.jackson.annotate.JsonProperty("method")
    public void setLogMethod(java.lang.String logMethod) {
        this.logMethod = logMethod;
    }

    @org.codehaus.jackson.annotate.JsonProperty("level")
    public java.lang.String getLogLevel() {
        return logLevel;
    }

    @org.codehaus.jackson.annotate.JsonProperty("level")
    public void setLogLevel(java.lang.String logLevel) {
        this.logLevel = logLevel;
    }

    @org.codehaus.jackson.annotate.JsonProperty("event_count")
    public java.lang.String getEventCount() {
        return eventCount;
    }

    @org.codehaus.jackson.annotate.JsonProperty("event_count")
    public void setEventCount(java.lang.String eventCount) {
        this.eventCount = eventCount;
    }

    @org.codehaus.jackson.annotate.JsonProperty("ip")
    public java.lang.String getIpAddress() {
        return ipAddress;
    }

    @org.codehaus.jackson.annotate.JsonProperty("ip")
    public void setIpAddress(java.lang.String ipAddress) {
        this.ipAddress = ipAddress;
    }

    @org.codehaus.jackson.annotate.JsonProperty("type")
    public java.lang.String getComponentType() {
        return componentType;
    }

    @org.codehaus.jackson.annotate.JsonProperty("type")
    public void setComponentType(java.lang.String componentType) {
        this.componentType = componentType;
    }

    @org.codehaus.jackson.annotate.JsonProperty("seq_num")
    public java.lang.String getSequenceNumber() {
        return sequenceNumber;
    }

    @org.codehaus.jackson.annotate.JsonProperty("seq_num")
    public void setSequenceNumber(java.lang.String sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
    }

    @org.codehaus.jackson.annotate.JsonProperty("path")
    public java.lang.String getLogFilePath() {
        return logFilePath;
    }

    @org.codehaus.jackson.annotate.JsonProperty("path")
    public void setLogFilePath(java.lang.String logFilePath) {
        this.logFilePath = logFilePath;
    }

    @org.codehaus.jackson.annotate.JsonProperty("file")
    public java.lang.String getSourceFile() {
        return sourceFile;
    }

    @org.codehaus.jackson.annotate.JsonProperty("file")
    public void setSourceFile(java.lang.String sourceFile) {
        this.sourceFile = sourceFile;
    }

    @org.codehaus.jackson.annotate.JsonProperty("line_number")
    public java.lang.String getSourceFileLineNumber() {
        return sourceFileLineNumber;
    }

    @org.codehaus.jackson.annotate.JsonProperty("line_number")
    public void setSourceFileLineNumber(java.lang.String sourceFileLineNumber) {
        this.sourceFileLineNumber = sourceFileLineNumber;
    }

    @org.codehaus.jackson.annotate.JsonProperty("host")
    public java.lang.String getHostName() {
        return hostName;
    }

    @org.codehaus.jackson.annotate.JsonProperty("host")
    public void setHostName(java.lang.String hostName) {
        this.hostName = hostName;
    }

    @org.codehaus.jackson.annotate.JsonProperty("log_message")
    public java.lang.String getLogMessage() {
        return logMessage;
    }

    @org.codehaus.jackson.annotate.JsonProperty("log_message")
    public void setLogMessage(java.lang.String logMessage) {
        this.logMessage = logMessage;
    }

    @org.codehaus.jackson.annotate.JsonProperty("logger_name")
    public java.lang.String getLoggerName() {
        return loggerName;
    }

    @org.codehaus.jackson.annotate.JsonProperty("logger_name")
    public void setLoggerName(java.lang.String loggerName) {
        this.loggerName = loggerName;
    }

    @org.codehaus.jackson.annotate.JsonProperty("id")
    public java.lang.String getId() {
        return id;
    }

    @org.codehaus.jackson.annotate.JsonProperty("id")
    public void setId(java.lang.String id) {
        this.id = id;
    }

    @org.codehaus.jackson.annotate.JsonProperty("message_md5")
    public java.lang.String getMessageMD5() {
        return messageMD5;
    }

    @org.codehaus.jackson.annotate.JsonProperty("message_md5")
    public void setMessageMD5(java.lang.String messageMD5) {
        this.messageMD5 = messageMD5;
    }

    @org.codehaus.jackson.annotate.JsonProperty("logtime")
    public java.lang.String getLogTime() {
        return logTime;
    }

    @org.codehaus.jackson.annotate.JsonProperty("logtime")
    public void setLogTime(java.lang.String logTime) {
        this.logTime = logTime;
    }

    @org.codehaus.jackson.annotate.JsonProperty("event_md5")
    public java.lang.String getEventMD5() {
        return eventMD5;
    }

    @org.codehaus.jackson.annotate.JsonProperty("event_md5")
    public void setEventMD5(java.lang.String eventMD5) {
        this.eventMD5 = eventMD5;
    }

    @org.codehaus.jackson.annotate.JsonProperty("logfile_line_number")
    public java.lang.String getLogFileLineNumber() {
        return logFileLineNumber;
    }

    @org.codehaus.jackson.annotate.JsonProperty("logfile_line_number")
    public void setLogFileLineNumber(java.lang.String logFileLineNumber) {
        this.logFileLineNumber = logFileLineNumber;
    }

    @org.codehaus.jackson.annotate.JsonProperty("_ttl_")
    public java.lang.String getTtl() {
        return ttl;
    }

    @org.codehaus.jackson.annotate.JsonProperty("_ttl_")
    public void setTtl(java.lang.String ttl) {
        this.ttl = ttl;
    }

    @org.codehaus.jackson.annotate.JsonProperty("_expire_at_")
    public java.lang.String getExpirationTime() {
        return expirationTime;
    }

    @org.codehaus.jackson.annotate.JsonProperty("_expire_at_")
    public void setExpirationTime(java.lang.String expirationTime) {
        this.expirationTime = expirationTime;
    }

    @org.codehaus.jackson.annotate.JsonProperty("_version_")
    public java.lang.String getVersion() {
        return version;
    }

    @org.codehaus.jackson.annotate.JsonProperty("_version_")
    public void setVersion(java.lang.String version) {
        this.version = version;
    }

    @org.codehaus.jackson.annotate.JsonProperty("thread_name")
    public java.lang.String getThreadName() {
        return thread_name;
    }

    @org.codehaus.jackson.annotate.JsonProperty("thread_name")
    public void setThreadName(java.lang.String thread_name) {
        this.thread_name = thread_name;
    }
}