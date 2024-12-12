package org.apache.ambari.server.upgrade;
public class SchemaUpgradeUtil {
    private SchemaUpgradeUtil() {
        throw new java.lang.UnsupportedOperationException();
    }

    public static java.lang.String extractProperty(java.lang.String content, java.lang.String propertyName, java.lang.String variableName, java.lang.String propertyPattern, java.lang.String defaultValue, java.util.Map<java.lang.String, java.lang.String> newProperties) {
        if (org.springframework.util.StringUtils.countOccurrencesOf(propertyPattern, "(\\w+)") != 1) {
            throw new java.lang.IllegalArgumentException("propertyPattern must contain exactly one \'(\\w+)\': " + propertyPattern);
        }
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(propertyPattern);
        java.util.regex.Matcher m = p.matcher(content);
        java.lang.String propertyValue = defaultValue;
        if (m.find()) {
            propertyValue = m.group(1);
            java.lang.String unescapedPattern = propertyPattern.replace("\\{", "{");
            java.lang.String toReplace = unescapedPattern.replace("(\\w+)", propertyValue);
            java.lang.String replaceWith = unescapedPattern.replace("(\\w+)", ("{{" + variableName) + "}}");
            content = content.replace(toReplace, replaceWith);
        }
        newProperties.put(propertyName, propertyValue);
        return content;
    }
}