package org.apache.ambari.server.controller.logging;
import org.codehaus.jackson.map.AnnotationIntrospector;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.ObjectReader;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.map.introspect.JacksonAnnotationIntrospector;
public class LogQueryResponseTest {
    private static final java.lang.String TEST_JSON_INPUT_TWO_LIST_ENTRIES = "{" + ((((((((((((((((((((((((((((((((((((((((((((((((((("  \"startIndex\" : 0," + "  \"pageSize\" : 5,") + "  \"totalCount\" : 10452,") + "  \"resultSize\" : 5,") + "  \"queryTimeMS\" : 1458148754113,") + "  \"logList\" : [ {") + "    \"cluster\" : \"clusterone\",") + "    \"method\" : \"chooseUnderReplicatedBlocks\",") + "    \"level\" : \"INFO\",") + "    \"event_count\" : 1,") + "    \"ip\" : \"192.168.1.1\",") + "    \"type\" : \"hdfs_namenode\",") + "    \"seq_num\" : 10584,") + "    \"path\" : \"/var/log/hadoop/hdfs/hadoop-hdfs-namenode-c6401.ambari.apache.org.log\",") + "    \"file\" : \"UnderReplicatedBlocks.java\",") + "    \"line_number\" : 394,") + "    \"host\" : \"c6401.ambari.apache.org\",") + "    \"log_message\" : \"chooseUnderReplicatedBlocks selected 2 blocks at priority level 0;  Total=2 Reset bookmarks? false\",") + "    \"logger_name\" : \"BlockStateChange\",") + "    \"id\" : \"9c5562fb-123f-47c8-aaf5-b5e407326c08\",") + "    \"message_md5\" : \"-3892769501348410581\",") + "    \"logtime\" : 1458148749036,") + "    \"event_md5\" : \"1458148749036-2417481968206345035\",") + "    \"logfile_line_number\" : 2084,") + "    \"_ttl_\" : \"+7DAYS\",") + "    \"_expire_at_\" : 1458753550322,") + "    \"_version_\" : 1528979784023932928") + "  }, {") + "    \"cluster\" : \"clusterone\",") + "    \"method\" : \"putMetrics\",") + "    \"level\" : \"WARN\",") + "    \"event_count\" : 1,") + "    \"ip\" : \"192.168.1.1\",") + "    \"type\" : \"yarn_resourcemanager\",") + "    \"seq_num\" : 10583,") + "    \"path\" : \"/var/log/hadoop-yarn/yarn/yarn-yarn-resourcemanager-c6401.ambari.apache.org.log\",") + "    \"file\" : \"HadoopTimelineMetricsSink.java\",") + "    \"line_number\" : 262,") + "    \"host\" : \"c6401.ambari.apache.org\",") + "    \"log_message\" : \"Unable to send metrics to collector by address:http://c6401.ambari.apache.org:6188/ws/v1/timeline/metrics\",") + "    \"logger_name\" : \"timeline.HadoopTimelineMetricsSink\",") + "    \"id\" : \"8361c5a9-5b1c-4f44-bc8f-4c6f07d94228\",") + "    \"message_md5\" : \"5942185045779825717\",") + "    \"logtime\" : 1458148746937,") + "    \"event_md5\" : \"14581487469371427138486123628676\",") + "    \"logfile_line_number\" : 549,") + "    \"_ttl_\" : \"+7DAYS\",") + "    \"_expire_at_\" : 1458753550322,") + "    \"_version_\" : 1528979784022884357") + "  }") + "]") + "}");

    @org.junit.Test
    public void testBasicParsing() throws java.lang.Exception {
        java.io.StringReader stringReader = new java.io.StringReader(org.apache.ambari.server.controller.logging.LogQueryResponseTest.TEST_JSON_INPUT_TWO_LIST_ENTRIES);
        org.codehaus.jackson.map.ObjectMapper mapper = new org.codehaus.jackson.map.ObjectMapper();
        org.codehaus.jackson.map.AnnotationIntrospector introspector = new org.codehaus.jackson.map.introspect.JacksonAnnotationIntrospector();
        mapper.setAnnotationIntrospector(introspector);
        mapper.getSerializationConfig().setSerializationInclusion(JsonSerialize.Inclusion.NON_NULL);
        org.codehaus.jackson.map.ObjectReader logQueryResponseReader = mapper.reader(org.apache.ambari.server.controller.logging.LogQueryResponse.class);
        org.apache.ambari.server.controller.logging.LogQueryResponse result = logQueryResponseReader.readValue(stringReader);
        org.junit.Assert.assertEquals("startIndex not parsed properly", "0", result.getStartIndex());
        org.junit.Assert.assertEquals("pageSize not parsed properly", "5", result.getPageSize());
        org.junit.Assert.assertEquals("totalCount not parsed properly", "10452", result.getTotalCount());
        org.junit.Assert.assertEquals("resultSize not parsed properly", "5", result.getResultSize());
        org.junit.Assert.assertEquals("queryTimeMS not parsed properly", "1458148754113", result.getQueryTimeMS());
        org.junit.Assert.assertEquals("incorrect number of LogLineResult items parsed", 2, result.getListOfResults().size());
        java.util.List<org.apache.ambari.server.controller.logging.LogLineResult> listOfLineResults = result.getListOfResults();
        org.apache.ambari.server.controller.logging.LogQueryResponseTest.verifyFirstLine(listOfLineResults);
        org.apache.ambari.server.controller.logging.LogQueryResponseTest.verifySecondLine(listOfLineResults);
    }

    private static void verifyFirstLine(java.util.List<org.apache.ambari.server.controller.logging.LogLineResult> listOfLineResults) {
        org.apache.ambari.server.controller.logging.LogLineResult resultOne = listOfLineResults.get(0);
        org.junit.Assert.assertEquals("Cluster name not parsed properly", "clusterone", resultOne.getClusterName());
        org.junit.Assert.assertEquals("Method Name not parsed properly", "chooseUnderReplicatedBlocks", resultOne.getLogMethod());
        org.junit.Assert.assertEquals("Log Level not parsed properly", "INFO", resultOne.getLogLevel());
        org.junit.Assert.assertEquals("event_count not parsed properly", "1", resultOne.getEventCount());
        org.junit.Assert.assertEquals("ip address not parsed properly", "192.168.1.1", resultOne.getIpAddress());
        org.junit.Assert.assertEquals("component type not parsed properly", "hdfs_namenode", resultOne.getComponentType());
        org.junit.Assert.assertEquals("sequence number not parsed properly", "10584", resultOne.getSequenceNumber());
        org.junit.Assert.assertEquals("log file path not parsed properly", "/var/log/hadoop/hdfs/hadoop-hdfs-namenode-c6401.ambari.apache.org.log", resultOne.getLogFilePath());
        org.junit.Assert.assertEquals("log src file name not parsed properly", "UnderReplicatedBlocks.java", resultOne.getSourceFile());
        org.junit.Assert.assertEquals("log src line number not parsed properly", "394", resultOne.getSourceFileLineNumber());
        org.junit.Assert.assertEquals("host name not parsed properly", "c6401.ambari.apache.org", resultOne.getHostName());
        org.junit.Assert.assertEquals("log message not parsed properly", "chooseUnderReplicatedBlocks selected 2 blocks at priority level 0;  Total=2 Reset bookmarks? false", resultOne.getLogMessage());
        org.junit.Assert.assertEquals("logger name not parsed properly", "BlockStateChange", resultOne.getLoggerName());
        org.junit.Assert.assertEquals("id not parsed properly", "9c5562fb-123f-47c8-aaf5-b5e407326c08", resultOne.getId());
        org.junit.Assert.assertEquals("message MD5 not parsed properly", "-3892769501348410581", resultOne.getMessageMD5());
        org.junit.Assert.assertEquals("log time not parsed properly", "1458148749036", resultOne.getLogTime());
        org.junit.Assert.assertEquals("event MD5 not parsed properly", "1458148749036-2417481968206345035", resultOne.getEventMD5());
        org.junit.Assert.assertEquals("logfile line number not parsed properly", "2084", resultOne.getLogFileLineNumber());
        org.junit.Assert.assertEquals("ttl not parsed properly", "+7DAYS", resultOne.getTtl());
        org.junit.Assert.assertEquals("expire at not parsed properly", "1458753550322", resultOne.getExpirationTime());
        org.junit.Assert.assertEquals("version not parsed properly", "1528979784023932928", resultOne.getVersion());
    }

    private static void verifySecondLine(java.util.List<org.apache.ambari.server.controller.logging.LogLineResult> listOfLineResults) {
        org.apache.ambari.server.controller.logging.LogLineResult resultTwo = listOfLineResults.get(1);
        org.junit.Assert.assertEquals("Cluster name not parsed properly", "clusterone", resultTwo.getClusterName());
        org.junit.Assert.assertEquals("Method Name not parsed properly", "putMetrics", resultTwo.getLogMethod());
        org.junit.Assert.assertEquals("Log Level not parsed properly", "WARN", resultTwo.getLogLevel());
        org.junit.Assert.assertEquals("event_count not parsed properly", "1", resultTwo.getEventCount());
        org.junit.Assert.assertEquals("ip address not parsed properly", "192.168.1.1", resultTwo.getIpAddress());
        org.junit.Assert.assertEquals("component type not parsed properly", "yarn_resourcemanager", resultTwo.getComponentType());
        org.junit.Assert.assertEquals("sequence number not parsed properly", "10583", resultTwo.getSequenceNumber());
        org.junit.Assert.assertEquals("log file path not parsed properly", "/var/log/hadoop-yarn/yarn/yarn-yarn-resourcemanager-c6401.ambari.apache.org.log", resultTwo.getLogFilePath());
        org.junit.Assert.assertEquals("log src file name not parsed properly", "HadoopTimelineMetricsSink.java", resultTwo.getSourceFile());
        org.junit.Assert.assertEquals("log src line number not parsed properly", "262", resultTwo.getSourceFileLineNumber());
        org.junit.Assert.assertEquals("host name not parsed properly", "c6401.ambari.apache.org", resultTwo.getHostName());
        org.junit.Assert.assertEquals("log message not parsed properly", "Unable to send metrics to collector by address:http://c6401.ambari.apache.org:6188/ws/v1/timeline/metrics", resultTwo.getLogMessage());
        org.junit.Assert.assertEquals("logger name not parsed properly", "timeline.HadoopTimelineMetricsSink", resultTwo.getLoggerName());
        org.junit.Assert.assertEquals("id not parsed properly", "8361c5a9-5b1c-4f44-bc8f-4c6f07d94228", resultTwo.getId());
        org.junit.Assert.assertEquals("message MD5 not parsed properly", "5942185045779825717", resultTwo.getMessageMD5());
        org.junit.Assert.assertEquals("log time not parsed properly", "1458148746937", resultTwo.getLogTime());
        org.junit.Assert.assertEquals("event MD5 not parsed properly", "14581487469371427138486123628676", resultTwo.getEventMD5());
        org.junit.Assert.assertEquals("logfile line number not parsed properly", "549", resultTwo.getLogFileLineNumber());
        org.junit.Assert.assertEquals("ttl not parsed properly", "+7DAYS", resultTwo.getTtl());
        org.junit.Assert.assertEquals("expire at not parsed properly", "1458753550322", resultTwo.getExpirationTime());
        org.junit.Assert.assertEquals("version not parsed properly", "1528979784022884357", resultTwo.getVersion());
    }
}