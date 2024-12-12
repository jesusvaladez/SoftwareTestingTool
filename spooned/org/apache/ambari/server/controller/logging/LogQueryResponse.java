package org.apache.ambari.server.controller.logging;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;
@org.codehaus.jackson.annotate.JsonIgnoreProperties(ignoreUnknown = true)
public class LogQueryResponse {
    private java.lang.String startIndex;

    private java.lang.String pageSize;

    private java.lang.String totalCount;

    private java.lang.String resultSize;

    private java.lang.String queryTimeMS;

    private java.util.List<org.apache.ambari.server.controller.logging.LogLineResult> listOfResults;

    private java.lang.StringBuffer resultString;

    public LogQueryResponse() {
    }

    public LogQueryResponse(java.lang.StringBuffer resultString) {
        this.resultString = resultString;
    }

    @org.codehaus.jackson.annotate.JsonProperty("logList")
    public java.util.List<org.apache.ambari.server.controller.logging.LogLineResult> getListOfResults() {
        return listOfResults;
    }

    @org.codehaus.jackson.annotate.JsonProperty("logList")
    public void setLogList(java.util.List<org.apache.ambari.server.controller.logging.LogLineResult> logList) {
        this.listOfResults = logList;
    }

    @org.codehaus.jackson.annotate.JsonProperty("startIndex")
    public java.lang.String getStartIndex() {
        return startIndex;
    }

    @org.codehaus.jackson.annotate.JsonProperty("startIndex")
    public void setStartIndex(java.lang.String startIndex) {
        this.startIndex = startIndex;
    }

    @org.codehaus.jackson.annotate.JsonProperty("pageSize")
    public java.lang.String getPageSize() {
        return pageSize;
    }

    @org.codehaus.jackson.annotate.JsonProperty("pageSize")
    public void setPageSize(java.lang.String pageSize) {
        this.pageSize = pageSize;
    }

    @org.codehaus.jackson.annotate.JsonProperty("totalCount")
    public java.lang.String getTotalCount() {
        return totalCount;
    }

    @org.codehaus.jackson.annotate.JsonProperty("totalCount")
    public void setTotalCount(java.lang.String totalCount) {
        this.totalCount = totalCount;
    }

    @org.codehaus.jackson.annotate.JsonProperty("resultSize")
    public java.lang.String getResultSize() {
        return resultSize;
    }

    @org.codehaus.jackson.annotate.JsonProperty("resultSize")
    public void setResultSize(java.lang.String resultSize) {
        this.resultSize = resultSize;
    }

    @org.codehaus.jackson.annotate.JsonProperty("queryTimeMS")
    public java.lang.String getQueryTimeMS() {
        return queryTimeMS;
    }

    @org.codehaus.jackson.annotate.JsonProperty("queryTimeMS")
    public void setQueryTimeMS(java.lang.String queryTimeMS) {
        this.queryTimeMS = queryTimeMS;
    }

    public java.lang.StringBuffer getResultString() {
        return resultString;
    }
}