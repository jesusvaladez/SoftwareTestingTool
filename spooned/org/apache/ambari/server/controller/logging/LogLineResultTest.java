package org.apache.ambari.server.controller.logging;
import org.codehaus.jackson.map.AnnotationIntrospector;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.ObjectReader;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.map.introspect.JacksonAnnotationIntrospector;
public class LogLineResultTest {
    private static final java.lang.String TEST_JSON_DATA_SINGLE_ENTRY = "{" + (((((((((((((((((((((("    \"cluster\" : \"clusterone\"," + "    \"method\" : \"chooseUnderReplicatedBlocks\",") + "    \"level\" : \"INFO\",") + "    \"event_count\" : 1,") + "    \"ip\" : \"192.168.1.1\",") + "    \"type\" : \"hdfs_namenode\",") + "    \"thread_name\" : \"thread-id-one\",") + "    \"seq_num\" : 10584,") + "    \"path\" : \"/var/log/hadoop/hdfs/hadoop-hdfs-namenode-c6401.ambari.apache.org.log\",") + "    \"file\" : \"UnderReplicatedBlocks.java\",") + "    \"line_number\" : 394,") + "    \"host\" : \"c6401.ambari.apache.org\",") + "    \"log_message\" : \"chooseUnderReplicatedBlocks selected 2 blocks at priority level 0;  Total=2 Reset bookmarks? false\",") + "    \"logger_name\" : \"BlockStateChange\",") + "    \"id\" : \"9c5562fb-123f-47c8-aaf5-b5e407326c08\",") + "    \"message_md5\" : \"-3892769501348410581\",") + "    \"logtime\" : 1458148749036,") + "    \"event_md5\" : \"1458148749036-2417481968206345035\",") + "    \"logfile_line_number\" : 2084,") + "    \"_ttl_\" : \"+7DAYS\",") + "    \"_expire_at_\" : 1458753550322,") + "    \"_version_\" : 1528979784023932928") + "  }");

    @org.junit.Test
    public void testBasicParsing() throws java.lang.Exception {
        java.io.StringReader stringReader = new java.io.StringReader(org.apache.ambari.server.controller.logging.LogLineResultTest.TEST_JSON_DATA_SINGLE_ENTRY);
        org.codehaus.jackson.map.ObjectMapper mapper = new org.codehaus.jackson.map.ObjectMapper();
        org.codehaus.jackson.map.AnnotationIntrospector introspector = new org.codehaus.jackson.map.introspect.JacksonAnnotationIntrospector();
        mapper.setAnnotationIntrospector(introspector);
        mapper.getSerializationConfig().setSerializationInclusion(JsonSerialize.Inclusion.NON_NULL);
        org.codehaus.jackson.map.ObjectReader logLineResultReader = mapper.reader(org.apache.ambari.server.controller.logging.LogLineResult.class);
        org.apache.ambari.server.controller.logging.LogLineResult result = logLineResultReader.readValue(stringReader);
        org.junit.Assert.assertEquals("Cluster name not parsed properly", "clusterone", result.getClusterName());
        org.junit.Assert.assertEquals("Method Name not parsed properly", "chooseUnderReplicatedBlocks", result.getLogMethod());
        org.junit.Assert.assertEquals("Log Level not parsed properly", "INFO", result.getLogLevel());
        org.junit.Assert.assertEquals("event_count not parsed properly", "1", result.getEventCount());
        org.junit.Assert.assertEquals("ip address not parsed properly", "192.168.1.1", result.getIpAddress());
        org.junit.Assert.assertEquals("component type not parsed properly", "hdfs_namenode", result.getComponentType());
        org.junit.Assert.assertEquals("sequence number not parsed properly", "10584", result.getSequenceNumber());
        org.junit.Assert.assertEquals("log file path not parsed properly", "/var/log/hadoop/hdfs/hadoop-hdfs-namenode-c6401.ambari.apache.org.log", result.getLogFilePath());
        org.junit.Assert.assertEquals("log src file name not parsed properly", "UnderReplicatedBlocks.java", result.getSourceFile());
        org.junit.Assert.assertEquals("log src line number not parsed properly", "394", result.getSourceFileLineNumber());
        org.junit.Assert.assertEquals("host name not parsed properly", "c6401.ambari.apache.org", result.getHostName());
        org.junit.Assert.assertEquals("log message not parsed properly", "chooseUnderReplicatedBlocks selected 2 blocks at priority level 0;  Total=2 Reset bookmarks? false", result.getLogMessage());
        org.junit.Assert.assertEquals("logger name not parsed properly", "BlockStateChange", result.getLoggerName());
        org.junit.Assert.assertEquals("id not parsed properly", "9c5562fb-123f-47c8-aaf5-b5e407326c08", result.getId());
        org.junit.Assert.assertEquals("message MD5 not parsed properly", "-3892769501348410581", result.getMessageMD5());
        org.junit.Assert.assertEquals("log time not parsed properly", "1458148749036", result.getLogTime());
        org.junit.Assert.assertEquals("event MD5 not parsed properly", "1458148749036-2417481968206345035", result.getEventMD5());
        org.junit.Assert.assertEquals("logfile line number not parsed properly", "2084", result.getLogFileLineNumber());
        org.junit.Assert.assertEquals("ttl not parsed properly", "+7DAYS", result.getTtl());
        org.junit.Assert.assertEquals("expire at not parsed properly", "1458753550322", result.getExpirationTime());
        org.junit.Assert.assertEquals("version not parsed properly", "1528979784023932928", result.getVersion());
        org.junit.Assert.assertEquals("thread_name not parsed properly", "thread-id-one", result.getThreadName());
    }
}