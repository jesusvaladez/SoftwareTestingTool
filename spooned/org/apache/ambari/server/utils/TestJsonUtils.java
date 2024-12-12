package org.apache.ambari.server.utils;
public class TestJsonUtils {
    @org.junit.Test
    public void testIsValidJson() throws java.lang.Exception {
        junit.framework.Assert.assertFalse(org.apache.ambari.server.utils.JsonUtils.isValidJson(null));
        junit.framework.Assert.assertFalse(org.apache.ambari.server.utils.JsonUtils.isValidJson(""));
        junit.framework.Assert.assertFalse(org.apache.ambari.server.utils.JsonUtils.isValidJson("{"));
        junit.framework.Assert.assertFalse(org.apache.ambari.server.utils.JsonUtils.isValidJson("}"));
        junit.framework.Assert.assertTrue(org.apache.ambari.server.utils.JsonUtils.isValidJson("{}"));
        junit.framework.Assert.assertTrue(org.apache.ambari.server.utils.JsonUtils.isValidJson("{ \"stack\" : \"HDP\" }"));
        junit.framework.Assert.assertTrue(org.apache.ambari.server.utils.JsonUtils.isValidJson("{\n" + (("  \"stack_selector\": [\"hdp-select\", \"/usr/bin/hdp-select\", \"hdp-select\"],\n" + "  \"conf_selector\": [\"conf-select\", \"/usr/bin/conf-select\", \"conf-select\"]\n") + "}")));
        junit.framework.Assert.assertFalse(org.apache.ambari.server.utils.JsonUtils.isValidJson("{\n" + (("  \"stack_selector\": [\"hdp-select\", \"/usr/bin/hdp-select\", \"hdp-select\"],\n" + "  \"conf_selector\": [\"conf-select\", \"/usr/bin/conf-select\", \"conf-select\"]\n") + "")));
    }
}