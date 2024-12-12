package org.apache.ambari.server.configuration;
import org.apache.commons.lang.StringUtils;
public enum AmbariServerConfigurationCategory {

    LDAP_CONFIGURATION("ldap-configuration"),
    SSO_CONFIGURATION("sso-configuration"),
    TPROXY_CONFIGURATION("tproxy-configuration");
    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.configuration.AmbariServerConfigurationCategory.class);

    private final java.lang.String categoryName;

    AmbariServerConfigurationCategory(java.lang.String categoryName) {
        this.categoryName = categoryName;
    }

    public java.lang.String getCategoryName() {
        return categoryName;
    }

    public static org.apache.ambari.server.configuration.AmbariServerConfigurationCategory translate(java.lang.String categoryName) {
        if (!org.apache.commons.lang.StringUtils.isEmpty(categoryName)) {
            categoryName = categoryName.trim();
            for (org.apache.ambari.server.configuration.AmbariServerConfigurationCategory category : org.apache.ambari.server.configuration.AmbariServerConfigurationCategory.values()) {
                if (category.getCategoryName().equals(categoryName)) {
                    return category;
                }
            }
        }
        org.apache.ambari.server.configuration.AmbariServerConfigurationCategory.LOG.warn("Invalid Ambari server configuration category: {}", categoryName);
        return null;
    }

    public static java.lang.String translate(org.apache.ambari.server.configuration.AmbariServerConfigurationCategory category) {
        return category == null ? null : category.getCategoryName();
    }
}