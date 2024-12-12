package org.apache.ambari.server.controller.logging;
public interface LoggingRequestHelper {
    org.apache.ambari.server.controller.logging.LogQueryResponse sendQueryRequest(java.util.Map<java.lang.String, java.lang.String> queryParameters);

    org.apache.ambari.server.controller.logging.HostLogFilesResponse sendGetLogFileNamesRequest(java.lang.String hostName);

    org.apache.ambari.server.controller.logging.LogLevelQueryResponse sendLogLevelQueryRequest(java.lang.String componentName, java.lang.String hostName);

    java.lang.String createLogFileTailURI(java.lang.String baseURI, java.lang.String componentName, java.lang.String hostName);
}