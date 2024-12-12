package org.apache.ambari.server.stack;
public class ConfigurationInfo implements org.apache.ambari.server.stack.Validable {
    private java.util.Collection<org.apache.ambari.server.state.PropertyInfo> properties;

    private java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> attributes;

    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.stack.ConfigurationInfo.class);

    protected boolean valid = true;

    public ConfigurationInfo(java.util.Collection<org.apache.ambari.server.state.PropertyInfo> properties, java.util.Map<java.lang.String, java.lang.String> attributes) {
        this.properties = properties;
        setAttributes(attributes);
    }

    public java.util.Collection<org.apache.ambari.server.state.PropertyInfo> getProperties() {
        return properties;
    }

    public java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> getAttributes() {
        return attributes;
    }

    public void ensureDefaultAttributes() {
        java.util.Map<java.lang.String, java.lang.String> supportsAttributes = attributes.get(org.apache.ambari.server.stack.ConfigurationInfo.Supports.KEYWORD);
        for (org.apache.ambari.server.stack.ConfigurationInfo.Supports supportsProperty : org.apache.ambari.server.stack.ConfigurationInfo.Supports.values()) {
            java.lang.String propertyName = supportsProperty.getPropertyName();
            if (!supportsAttributes.containsKey(propertyName)) {
                supportsAttributes.put(propertyName, supportsProperty.getDefaultValue());
            }
        }
    }

    private void setAttributes(java.util.Map<java.lang.String, java.lang.String> specifiedAttributes) {
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> attributes = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.lang.String> supportsAttributes = new java.util.HashMap<>();
        attributes.put(org.apache.ambari.server.stack.ConfigurationInfo.Supports.KEYWORD, supportsAttributes);
        for (java.util.Map.Entry<java.lang.String, java.lang.String> entry : specifiedAttributes.entrySet()) {
            java.lang.String attributeName = entry.getKey();
            org.apache.ambari.server.stack.ConfigurationInfo.Supports s = org.apache.ambari.server.stack.ConfigurationInfo.Supports.attributeNameValueOf(attributeName);
            if (s != null) {
                supportsAttributes.put(s.getPropertyName(), java.lang.Boolean.valueOf(entry.getValue()).toString());
            } else {
                org.apache.ambari.server.stack.ConfigurationInfo.LOG.warn("Unknown configuration type attribute is specified: {}={}", attributeName, entry.getValue());
            }
        }
        this.attributes = attributes;
    }

    @java.lang.Override
    public boolean isValid() {
        return valid;
    }

    @java.lang.Override
    public void setValid(boolean valid) {
        this.valid = valid;
    }

    private java.util.Set<java.lang.String> errorSet = new java.util.HashSet<>();

    @java.lang.Override
    public void addError(java.lang.String error) {
        errorSet.add(error);
    }

    @java.lang.Override
    public java.util.Collection<java.lang.String> getErrors() {
        return errorSet;
    }

    @java.lang.Override
    public void addErrors(java.util.Collection<java.lang.String> errors) {
        this.errorSet.addAll(errors);
    }

    public enum Supports {

        FINAL("supports_final"),
        ADDING_FORBIDDEN("supports_adding_forbidden"),
        DO_NOT_EXTEND("supports_do_not_extend");
        public static final java.lang.String KEYWORD = "supports";

        private java.lang.String defaultValue;

        private java.lang.String xmlAttributeName;

        Supports(java.lang.String xmlAttributeName) {
            this(xmlAttributeName, java.lang.Boolean.FALSE.toString());
        }

        Supports(java.lang.String xmlAttributeName, java.lang.String defaultValue) {
            this.defaultValue = defaultValue;
            this.xmlAttributeName = xmlAttributeName;
        }

        public java.lang.String getDefaultValue() {
            return defaultValue;
        }

        public java.lang.String getXmlAttributeName() {
            return xmlAttributeName;
        }

        public java.lang.String getPropertyName() {
            return name().toLowerCase();
        }

        public static org.apache.ambari.server.stack.ConfigurationInfo.Supports attributeNameValueOf(java.lang.String attributeName) {
            for (org.apache.ambari.server.stack.ConfigurationInfo.Supports s : org.apache.ambari.server.stack.ConfigurationInfo.Supports.values()) {
                if (s.getXmlAttributeName().equals(attributeName)) {
                    return s;
                }
            }
            return null;
        }
    }
}