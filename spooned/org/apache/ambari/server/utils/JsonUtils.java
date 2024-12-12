package org.apache.ambari.server.utils;
import org.apache.commons.lang.StringUtils;
public class JsonUtils {
    public static final com.google.gson.JsonParser jsonParser = new com.google.gson.JsonParser();

    public static boolean isValidJson(java.lang.String jsonString) {
        if (org.apache.commons.lang.StringUtils.isBlank(jsonString)) {
            return false;
        }
        try {
            org.apache.ambari.server.utils.JsonUtils.jsonParser.parse(jsonString);
            return true;
        } catch (com.google.gson.JsonSyntaxException jse) {
            return false;
        }
    }
}