package org.apache.ambari.server.stack.upgrade;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;
import org.apache.commons.lang.StringUtils;
@javax.xml.bind.annotation.XmlType(name = "config")
@javax.xml.bind.annotation.XmlAccessorType(javax.xml.bind.annotation.XmlAccessType.FIELD)
public final class ConfigurationCondition extends org.apache.ambari.server.stack.upgrade.Condition {
    @javax.xml.bind.annotation.XmlEnum
    public enum ComparisonType {

        @javax.xml.bind.annotation.XmlEnumValue("equals")
        EQUALS,
        @javax.xml.bind.annotation.XmlEnumValue("not-equals")
        NOT_EQUALS,
        @javax.xml.bind.annotation.XmlEnumValue("contains")
        CONTAINS,
        @javax.xml.bind.annotation.XmlEnumValue("not-contains")
        NOT_CONTAINS,
        @javax.xml.bind.annotation.XmlEnumValue("exists")
        EXISTS,
        @javax.xml.bind.annotation.XmlEnumValue("not-exists")
        NOT_EXISTS;}

    @javax.xml.bind.annotation.XmlAttribute(name = "type")
    public java.lang.String type;

    @javax.xml.bind.annotation.XmlAttribute(name = "property")
    public java.lang.String property;

    @javax.xml.bind.annotation.XmlAttribute(name = "value")
    public java.lang.String value;

    @javax.xml.bind.annotation.XmlAttribute(name = "return_value_if_config_missing")
    public boolean returnValueIfConfigMissing;

    @javax.xml.bind.annotation.XmlAttribute(name = "comparison")
    public org.apache.ambari.server.stack.upgrade.ConfigurationCondition.ComparisonType comparisonType;

    @java.lang.Override
    public java.lang.String toString() {
        return com.google.common.base.MoreObjects.toStringHelper(this).add("type", type).add("property", property).add("value", value).add("comparison", comparisonType).omitNullValues().toString();
    }

    @java.lang.Override
    public boolean isSatisfied(org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeContext upgradeContext) {
        org.apache.ambari.server.state.Cluster cluster = upgradeContext.getCluster();
        boolean propertyExists = false;
        org.apache.ambari.server.state.Config config = cluster.getDesiredConfigByType(type);
        java.util.Map<java.lang.String, java.lang.String> properties = null;
        if (null != config) {
            properties = config.getProperties();
            if (properties.containsKey(property)) {
                propertyExists = true;
            }
        }
        if (comparisonType == org.apache.ambari.server.stack.upgrade.ConfigurationCondition.ComparisonType.EXISTS) {
            return propertyExists;
        }
        if (comparisonType == org.apache.ambari.server.stack.upgrade.ConfigurationCondition.ComparisonType.NOT_EXISTS) {
            return !propertyExists;
        }
        if (!propertyExists) {
            return returnValueIfConfigMissing;
        }
        java.lang.String propertyValue = properties.get(property);
        switch (comparisonType) {
            case EQUALS :
                return org.apache.commons.lang.StringUtils.equals(propertyValue, value);
            case NOT_EQUALS :
                return !org.apache.commons.lang.StringUtils.equals(propertyValue, value);
            case CONTAINS :
                return org.apache.commons.lang.StringUtils.contains(propertyValue, value);
            case NOT_CONTAINS :
                return !org.apache.commons.lang.StringUtils.contains(propertyValue, value);
            default :
                return false;
        }
    }
}