package org.apache.ambari.server.stack.upgrade;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import org.apache.commons.lang.StringUtils;
@javax.xml.bind.annotation.XmlRootElement
@javax.xml.bind.annotation.XmlAccessorType(javax.xml.bind.annotation.XmlAccessType.FIELD)
public class ConfigUpgradeChangeDefinition {
    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.stack.upgrade.ConfigUpgradeChangeDefinition.class);

    public static final java.lang.String PARAMETER_CONFIG_TYPE = "configure-task-config-type";

    public static final java.lang.String PARAMETER_KEY_VALUE_PAIRS = "configure-task-key-value-pairs";

    public static final java.lang.String PARAMETER_TRANSFERS = "configure-task-transfers";

    public static final java.lang.String PARAMETER_REPLACEMENTS = "configure-task-replacements";

    public static final java.lang.String actionVerb = "Configuring";

    public static final java.lang.Float DEFAULT_PRIORITY = 1.0F;

    @javax.xml.bind.annotation.XmlAttribute(name = "summary")
    public java.lang.String summary;

    @javax.xml.bind.annotation.XmlAttribute(name = "id", required = true)
    public java.lang.String id;

    @javax.xml.bind.annotation.XmlElement(name = "type")
    private java.lang.String configType;

    @javax.xml.bind.annotation.XmlElement(name = "set")
    private java.util.List<org.apache.ambari.server.stack.upgrade.ConfigUpgradeChangeDefinition.ConfigurationKeyValue> keyValuePairs;

    @javax.xml.bind.annotation.XmlElement(name = "transfer")
    private java.util.List<org.apache.ambari.server.stack.upgrade.ConfigUpgradeChangeDefinition.Transfer> transfers;

    @javax.xml.bind.annotation.XmlElement(name = "replace")
    private java.util.List<org.apache.ambari.server.stack.upgrade.ConfigUpgradeChangeDefinition.Replace> replacements;

    @javax.xml.bind.annotation.XmlElement(name = "regex-replace")
    private java.util.List<org.apache.ambari.server.stack.upgrade.ConfigUpgradeChangeDefinition.RegexReplace> regexReplacements;

    @javax.xml.bind.annotation.XmlElement(name = "insert")
    private java.util.List<org.apache.ambari.server.stack.upgrade.ConfigUpgradeChangeDefinition.Insert> inserts;

    public java.lang.String getConfigType() {
        return configType;
    }

    public java.util.List<org.apache.ambari.server.stack.upgrade.ConfigUpgradeChangeDefinition.ConfigurationKeyValue> getKeyValuePairs() {
        return keyValuePairs;
    }

    public java.util.List<org.apache.ambari.server.stack.upgrade.ConfigUpgradeChangeDefinition.Transfer> getTransfers() {
        if (null == transfers) {
            return java.util.Collections.emptyList();
        }
        java.util.List<org.apache.ambari.server.stack.upgrade.ConfigUpgradeChangeDefinition.Transfer> list = new java.util.ArrayList<>();
        for (org.apache.ambari.server.stack.upgrade.ConfigUpgradeChangeDefinition.Transfer t : transfers) {
            switch (t.operation) {
                case COPY :
                case MOVE :
                    if ((null != t.fromKey) && (null != t.toKey)) {
                        list.add(t);
                    } else {
                        org.apache.ambari.server.stack.upgrade.ConfigUpgradeChangeDefinition.LOG.warn(java.lang.String.format("Transfer %s is invalid", t));
                    }
                    break;
                case DELETE :
                    if (null != t.deleteKey) {
                        list.add(t);
                    } else {
                        org.apache.ambari.server.stack.upgrade.ConfigUpgradeChangeDefinition.LOG.warn(java.lang.String.format("Transfer %s is invalid", t));
                    }
                    break;
            }
        }
        return list;
    }

    public java.util.List<org.apache.ambari.server.stack.upgrade.ConfigUpgradeChangeDefinition.Replace> getReplacements() {
        if (null == replacements) {
            return java.util.Collections.emptyList();
        }
        java.util.List<org.apache.ambari.server.stack.upgrade.ConfigUpgradeChangeDefinition.Replace> list = new java.util.ArrayList<>();
        for (org.apache.ambari.server.stack.upgrade.ConfigUpgradeChangeDefinition.Replace r : replacements) {
            if ((org.apache.commons.lang.StringUtils.isBlank(r.key) || org.apache.commons.lang.StringUtils.isEmpty(r.find)) || (null == r.replaceWith)) {
                org.apache.ambari.server.stack.upgrade.ConfigUpgradeChangeDefinition.LOG.warn(java.lang.String.format("Replacement %s is invalid", r));
                continue;
            }
            list.add(r);
        }
        return list;
    }

    public java.util.List<org.apache.ambari.server.stack.upgrade.ConfigUpgradeChangeDefinition.Replace> getRegexReplacements(org.apache.ambari.server.state.Cluster cluster) {
        if (null == regexReplacements) {
            return java.util.Collections.emptyList();
        }
        java.util.List<org.apache.ambari.server.stack.upgrade.ConfigUpgradeChangeDefinition.Replace> list = new java.util.ArrayList<>();
        for (org.apache.ambari.server.stack.upgrade.ConfigUpgradeChangeDefinition.RegexReplace regexReplaceObj : regexReplacements) {
            if ((org.apache.commons.lang.StringUtils.isBlank(regexReplaceObj.key) || org.apache.commons.lang.StringUtils.isEmpty(regexReplaceObj.find)) || (null == regexReplaceObj.replaceWith)) {
                org.apache.ambari.server.stack.upgrade.ConfigUpgradeChangeDefinition.LOG.warn(java.lang.String.format("Replacement %s is invalid", regexReplaceObj));
                continue;
            }
            try {
                org.apache.ambari.server.state.Config config = cluster.getDesiredConfigByType(configType);
                java.util.Map<java.lang.String, java.lang.String> properties = config.getProperties();
                java.lang.String content = properties.get(regexReplaceObj.key);
                java.util.regex.Pattern REGEX = java.util.regex.Pattern.compile(regexReplaceObj.find, java.util.regex.Pattern.MULTILINE);
                java.util.regex.Matcher patternMatchObj = REGEX.matcher(content);
                if (regexReplaceObj.matchAll) {
                    while (patternMatchObj.find()) {
                        regexReplaceObj.find = patternMatchObj.group();
                        if (org.apache.commons.lang.StringUtils.isNotBlank(regexReplaceObj.find)) {
                            org.apache.ambari.server.stack.upgrade.ConfigUpgradeChangeDefinition.Replace rep = regexReplaceObj.copyToReplaceObject();
                            list.add(rep);
                        }
                    } 
                } else if (patternMatchObj.find() && (patternMatchObj.groupCount() == 1)) {
                    regexReplaceObj.find = patternMatchObj.group();
                    org.apache.ambari.server.stack.upgrade.ConfigUpgradeChangeDefinition.Replace rep = regexReplaceObj.copyToReplaceObject();
                    list.add(rep);
                }
            } catch (java.lang.Exception e) {
                org.apache.ambari.server.stack.upgrade.ConfigUpgradeChangeDefinition.LOG.error(java.lang.String.format("There was an error while trying to execute a regex replacement for %s/%s. The regular expression was %s", configType, regexReplaceObj.key, regexReplaceObj.find), e);
            }
        }
        return list;
    }

    public java.util.List<org.apache.ambari.server.stack.upgrade.ConfigUpgradeChangeDefinition.Insert> getInsertions() {
        if (null == inserts) {
            return java.util.Collections.emptyList();
        }
        return inserts;
    }

    @javax.xml.bind.annotation.XmlAccessorType(javax.xml.bind.annotation.XmlAccessType.FIELD)
    public static class ConditionalField {
        @javax.xml.bind.annotation.XmlAttribute(name = "if-key")
        public java.lang.String ifKey;

        @javax.xml.bind.annotation.XmlAttribute(name = "if-type")
        public java.lang.String ifType;

        @javax.xml.bind.annotation.XmlAttribute(name = "if-value")
        public java.lang.String ifValue;

        @javax.xml.bind.annotation.XmlAttribute(name = "if-value-not-matched")
        public boolean ifValueNotMatched = false;

        @javax.xml.bind.annotation.XmlAttribute(name = "if-value-match-type")
        public org.apache.ambari.server.stack.upgrade.ConfigUpgradeChangeDefinition.IfValueMatchType ifValueMatchType = org.apache.ambari.server.stack.upgrade.ConfigUpgradeChangeDefinition.IfValueMatchType.EXACT;

        @javax.xml.bind.annotation.XmlAttribute(name = "if-key-state")
        public org.apache.ambari.server.stack.upgrade.PropertyKeyState ifKeyState;
    }

    @javax.xml.bind.annotation.XmlAccessorType(javax.xml.bind.annotation.XmlAccessType.FIELD)
    public static class Masked extends org.apache.ambari.server.stack.upgrade.ConfigUpgradeChangeDefinition.ConditionalField {
        @javax.xml.bind.annotation.XmlAttribute(name = "mask")
        public boolean mask = false;
    }

    @javax.xml.bind.annotation.XmlAccessorType(javax.xml.bind.annotation.XmlAccessType.FIELD)
    @javax.xml.bind.annotation.XmlType(name = "set")
    public static class ConfigurationKeyValue extends org.apache.ambari.server.stack.upgrade.ConfigUpgradeChangeDefinition.Masked {
        @javax.xml.bind.annotation.XmlAttribute(name = "key")
        public java.lang.String key;

        @javax.xml.bind.annotation.XmlAttribute(name = "value")
        public java.lang.String value;

        @java.lang.Override
        public java.lang.String toString() {
            return com.google.common.base.MoreObjects.toStringHelper("Set").add("key", key).add("value", value).add("ifKey", ifKey).add("ifType", ifType).add("ifValue", ifValue).add("ifKeyState", ifKeyState).omitNullValues().toString();
        }
    }

    @javax.xml.bind.annotation.XmlAccessorType(javax.xml.bind.annotation.XmlAccessType.FIELD)
    @javax.xml.bind.annotation.XmlType(name = "transfer")
    public static class Transfer extends org.apache.ambari.server.stack.upgrade.ConfigUpgradeChangeDefinition.Masked {
        @javax.xml.bind.annotation.XmlAttribute(name = "operation")
        public org.apache.ambari.server.stack.upgrade.TransferOperation operation;

        @javax.xml.bind.annotation.XmlAttribute(name = "from-type")
        public java.lang.String fromType;

        @javax.xml.bind.annotation.XmlAttribute(name = "from-key")
        public java.lang.String fromKey;

        @javax.xml.bind.annotation.XmlAttribute(name = "to-key")
        public java.lang.String toKey;

        @javax.xml.bind.annotation.XmlAttribute(name = "delete-key")
        public java.lang.String deleteKey;

        @javax.xml.bind.annotation.XmlAttribute(name = "preserve-edits")
        public boolean preserveEdits = false;

        @javax.xml.bind.annotation.XmlAttribute(name = "default-value")
        public java.lang.String defaultValue;

        @javax.xml.bind.annotation.XmlAttribute(name = "coerce-to")
        public org.apache.ambari.server.stack.upgrade.TransferCoercionType coerceTo;

        @javax.xml.bind.annotation.XmlElement(name = "keep-key")
        public java.util.List<java.lang.String> keepKeys = new java.util.ArrayList<>();

        @java.lang.Override
        public java.lang.String toString() {
            return com.google.common.base.MoreObjects.toStringHelper(this).add("operation", operation).add("fromType", fromType).add("fromKey", fromKey).add("toKey", toKey).add("deleteKey", deleteKey).add("preserveEdits", preserveEdits).add("defaultValue", defaultValue).add("coerceTo", coerceTo).add("ifKey", ifKey).add("ifType", ifType).add("ifValue", ifValue).add("ifKeyState", ifKeyState).add("keepKeys", keepKeys).omitNullValues().toString();
        }
    }

    @javax.xml.bind.annotation.XmlAccessorType(javax.xml.bind.annotation.XmlAccessType.FIELD)
    @javax.xml.bind.annotation.XmlType(name = "replace")
    public static class Replace extends org.apache.ambari.server.stack.upgrade.ConfigUpgradeChangeDefinition.Masked {
        @javax.xml.bind.annotation.XmlAttribute(name = "key")
        public java.lang.String key;

        @javax.xml.bind.annotation.XmlAttribute(name = "find")
        public java.lang.String find;

        @javax.xml.bind.annotation.XmlAttribute(name = "replace-with")
        public java.lang.String replaceWith;

        @java.lang.Override
        public java.lang.String toString() {
            return com.google.common.base.MoreObjects.toStringHelper(this).add("key", key).add("find", find).add("replaceWith", replaceWith).add("ifKey", ifKey).add("ifType", ifType).add("ifValue", ifValue).add("ifKeyState", ifKeyState).omitNullValues().toString();
        }
    }

    @javax.xml.bind.annotation.XmlAccessorType(javax.xml.bind.annotation.XmlAccessType.FIELD)
    @javax.xml.bind.annotation.XmlType(name = "regex-replace")
    public static class RegexReplace extends org.apache.ambari.server.stack.upgrade.ConfigUpgradeChangeDefinition.Masked {
        @javax.xml.bind.annotation.XmlAttribute(name = "key")
        public java.lang.String key;

        @javax.xml.bind.annotation.XmlAttribute(name = "find")
        public java.lang.String find;

        @javax.xml.bind.annotation.XmlAttribute(name = "replace-with")
        public java.lang.String replaceWith;

        @javax.xml.bind.annotation.XmlAttribute(name = "match-all")
        public boolean matchAll = false;

        @java.lang.Override
        public java.lang.String toString() {
            return com.google.common.base.MoreObjects.toStringHelper(this).add("key", key).add("find", find).add("replaceWith", replaceWith).add("ifKey", ifKey).add("ifType", ifType).add("ifValue", ifValue).add("ifKeyState", ifKeyState).omitNullValues().toString();
        }

        public org.apache.ambari.server.stack.upgrade.ConfigUpgradeChangeDefinition.Replace copyToReplaceObject() {
            org.apache.ambari.server.stack.upgrade.ConfigUpgradeChangeDefinition.Replace rep = new org.apache.ambari.server.stack.upgrade.ConfigUpgradeChangeDefinition.Replace();
            rep.find = find;
            rep.key = key;
            rep.replaceWith = replaceWith;
            rep.ifKey = ifKey;
            rep.ifType = ifType;
            rep.ifValue = ifValue;
            rep.ifKeyState = ifKeyState;
            return rep;
        }
    }

    @javax.xml.bind.annotation.XmlAccessorType(javax.xml.bind.annotation.XmlAccessType.FIELD)
    @javax.xml.bind.annotation.XmlType(name = "insert")
    public static class Insert extends org.apache.ambari.server.stack.upgrade.ConfigUpgradeChangeDefinition.Masked {
        @javax.xml.bind.annotation.XmlAttribute(name = "key", required = true)
        public java.lang.String key;

        @javax.xml.bind.annotation.XmlAttribute(name = "value", required = true)
        public java.lang.String value;

        @javax.xml.bind.annotation.XmlAttribute(name = "insert-type", required = true)
        public org.apache.ambari.server.stack.upgrade.ConfigUpgradeChangeDefinition.InsertType insertType = org.apache.ambari.server.stack.upgrade.ConfigUpgradeChangeDefinition.InsertType.APPEND;

        @javax.xml.bind.annotation.XmlAttribute(name = "newline-before")
        public boolean newlineBefore = false;

        @javax.xml.bind.annotation.XmlAttribute(name = "newline-after")
        public boolean newlineAfter = false;

        @java.lang.Override
        public java.lang.String toString() {
            return com.google.common.base.MoreObjects.toStringHelper(this).add("insertType", insertType).add("key", key).add("value", value).add("newlineBefore", newlineBefore).add("newlineAfter", newlineAfter).omitNullValues().toString();
        }
    }

    @javax.xml.bind.annotation.XmlEnum
    public enum InsertType {

        @javax.xml.bind.annotation.XmlEnumValue("prepend")
        PREPEND,
        @javax.xml.bind.annotation.XmlEnumValue("append")
        APPEND;}

    @javax.xml.bind.annotation.XmlEnum
    public enum IfValueMatchType {

        @javax.xml.bind.annotation.XmlEnumValue("exact")
        EXACT,
        @javax.xml.bind.annotation.XmlEnumValue("partial")
        PARTIAL;}
}