package org.apache.ambari.server.controller.logging;
import org.codehaus.jackson.map.AnnotationIntrospector;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.ObjectReader;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.map.introspect.JacksonAnnotationIntrospector;
public class LogLevelQueryResponseTest {
    private static final java.lang.String TEST_JSON_INPUT_LOG_LEVEL_QUERY = "{\"pageSize\":\"0\",\"queryTimeMS\":\"1459970731998\",\"resultSize\":\"6\",\"startIndex\":\"0\",\"totalCount\":\"0\"," + (("\"vNameValues\":[{\"name\":\"FATAL\",\"value\":\"0\"},{\"name\":\"ERROR\",\"value\":\"0\"}," + "{\"name\":\"WARN\",\"value\":\"41\"},{\"name\":\"INFO\",\"value\":\"186\"},{\"name\":\"DEBUG\",\"value\":\"0\"},") + "{\"name\":\"TRACE\",\"value\":\"0\"}]}");

    @org.junit.Test
    public void testBasicParsing() throws java.lang.Exception {
        java.io.StringReader stringReader = new java.io.StringReader(org.apache.ambari.server.controller.logging.LogLevelQueryResponseTest.TEST_JSON_INPUT_LOG_LEVEL_QUERY);
        org.codehaus.jackson.map.ObjectMapper mapper = new org.codehaus.jackson.map.ObjectMapper();
        org.codehaus.jackson.map.AnnotationIntrospector introspector = new org.codehaus.jackson.map.introspect.JacksonAnnotationIntrospector();
        mapper.setAnnotationIntrospector(introspector);
        mapper.getSerializationConfig().setSerializationInclusion(JsonSerialize.Inclusion.NON_NULL);
        org.codehaus.jackson.map.ObjectReader logLevelQueryResponseReader = mapper.reader(org.apache.ambari.server.controller.logging.LogLevelQueryResponse.class);
        org.apache.ambari.server.controller.logging.LogLevelQueryResponse result = logLevelQueryResponseReader.readValue(stringReader);
        org.junit.Assert.assertEquals("startIndex not parsed properly", "0", result.getStartIndex());
        org.junit.Assert.assertEquals("pageSize not parsed properly", "0", result.getPageSize());
        org.junit.Assert.assertEquals("totalCount not parsed properly", "0", result.getTotalCount());
        org.junit.Assert.assertEquals("resultSize not parsed properly", "6", result.getResultSize());
        org.junit.Assert.assertEquals("queryTimeMS not parsed properly", "1459970731998", result.getQueryTimeMS());
        org.junit.Assert.assertEquals("Incorrect number of log level count items parsed", 6, result.getNameValueList().size());
        java.util.List<org.apache.ambari.server.controller.logging.NameValuePair> resultList = result.getNameValueList();
        org.apache.ambari.server.controller.logging.LogLevelQueryResponseTest.assertNameValuePair("FATAL", "0", resultList.get(0));
        org.apache.ambari.server.controller.logging.LogLevelQueryResponseTest.assertNameValuePair("ERROR", "0", resultList.get(1));
        org.apache.ambari.server.controller.logging.LogLevelQueryResponseTest.assertNameValuePair("WARN", "41", resultList.get(2));
        org.apache.ambari.server.controller.logging.LogLevelQueryResponseTest.assertNameValuePair("INFO", "186", resultList.get(3));
        org.apache.ambari.server.controller.logging.LogLevelQueryResponseTest.assertNameValuePair("DEBUG", "0", resultList.get(4));
        org.apache.ambari.server.controller.logging.LogLevelQueryResponseTest.assertNameValuePair("TRACE", "0", resultList.get(5));
    }

    static void assertNameValuePair(java.lang.String expectedName, java.lang.String expectedValue, org.apache.ambari.server.controller.logging.NameValuePair nameValuePair) {
        org.junit.Assert.assertEquals("Unexpected name found in this pair", expectedName, nameValuePair.getName());
        org.junit.Assert.assertEquals("Unexpected value found in this pair", expectedValue, nameValuePair.getValue());
    }
}