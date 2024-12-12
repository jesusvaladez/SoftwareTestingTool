package org.apache.ambari.server.utils;
import org.apache.commons.lang.StringUtils;
public class XmlUtils {
    public static boolean isValidXml(java.lang.String input) {
        boolean result = true;
        try {
            if (org.apache.commons.lang.StringUtils.isBlank(input)) {
                result = false;
            } else {
                javax.xml.parsers.DocumentBuilderFactory dbFactory = javax.xml.parsers.DocumentBuilderFactory.newInstance();
                dbFactory.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
                javax.xml.parsers.DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
                dBuilder.parse(new java.io.ByteArrayInputStream(input.getBytes("UTF-8")));
            }
        } catch (java.lang.Exception e) {
            result = false;
        }
        return result;
    }
}