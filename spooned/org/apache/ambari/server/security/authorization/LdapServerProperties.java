package org.apache.ambari.server.security.authorization;
import org.apache.commons.lang.StringUtils;
public class LdapServerProperties {
    private java.lang.String primaryUrl;

    private java.lang.String secondaryUrl;

    private boolean useSsl;

    private boolean anonymousBind;

    private java.lang.String managerDn;

    private java.lang.String managerPassword;

    private java.lang.String baseDN;

    private java.lang.String dnAttribute;

    private java.lang.String referralMethod;

    private java.lang.String groupBase;

    private java.lang.String groupObjectClass;

    private java.lang.String groupMembershipAttr;

    private java.lang.String groupNamingAttr;

    private java.lang.String adminGroupMappingRules;

    private boolean groupMappingEnabled;

    private java.lang.String userBase;

    private java.lang.String userObjectClass;

    private java.lang.String usernameAttribute;

    private boolean forceUsernameToLowercase = false;

    private java.lang.String userSearchBase = "";

    private java.lang.String syncGroupMemberReplacePattern = "";

    private java.lang.String syncUserMemberReplacePattern = "";

    private java.lang.String groupSearchFilter;

    private java.lang.String userSearchFilter;

    private boolean alternateUserSearchFilterEnabled;

    private java.lang.String alternateUserSearchFilter;

    private java.lang.String syncUserMemberFilter = "";

    private java.lang.String syncGroupMemberFilter = "";

    private boolean disableEndpointIdentification = false;

    private boolean paginationEnabled = true;

    private java.lang.String adminGroupMappingMemberAttr = "";

    public java.util.List<java.lang.String> getLdapUrls() {
        java.lang.String protocol = (useSsl) ? "ldaps://" : "ldap://";
        if (org.apache.commons.lang.StringUtils.isEmpty(primaryUrl)) {
            return java.util.Collections.emptyList();
        } else {
            java.util.List<java.lang.String> list = new java.util.ArrayList<>();
            list.add(protocol + primaryUrl);
            if (!org.apache.commons.lang.StringUtils.isEmpty(secondaryUrl)) {
                list.add(protocol + secondaryUrl);
            }
            return list;
        }
    }

    public java.lang.String getPrimaryUrl() {
        return primaryUrl;
    }

    public void setPrimaryUrl(java.lang.String primaryUrl) {
        this.primaryUrl = primaryUrl;
    }

    public java.lang.String getSecondaryUrl() {
        return secondaryUrl;
    }

    public void setSecondaryUrl(java.lang.String secondaryUrl) {
        this.secondaryUrl = secondaryUrl;
    }

    public boolean isUseSsl() {
        return useSsl;
    }

    public void setUseSsl(boolean useSsl) {
        this.useSsl = useSsl;
    }

    public boolean isAnonymousBind() {
        return anonymousBind;
    }

    public void setAnonymousBind(boolean anonymousBind) {
        this.anonymousBind = anonymousBind;
    }

    public java.lang.String getManagerDn() {
        return managerDn;
    }

    public void setManagerDn(java.lang.String managerDn) {
        this.managerDn = managerDn;
    }

    public java.lang.String getManagerPassword() {
        return managerPassword;
    }

    public void setManagerPassword(java.lang.String managerPassword) {
        this.managerPassword = managerPassword;
    }

    public java.lang.String getBaseDN() {
        return baseDN;
    }

    public void setBaseDN(java.lang.String baseDN) {
        this.baseDN = baseDN;
    }

    public java.lang.String getUserSearchBase() {
        return userSearchBase;
    }

    public void setUserSearchBase(java.lang.String userSearchBase) {
        this.userSearchBase = userSearchBase;
    }

    public java.lang.String getUserSearchFilter(boolean useAlternateUserSearchFilter) {
        java.lang.String filter = (useAlternateUserSearchFilter) ? alternateUserSearchFilter : userSearchFilter;
        return resolveUserSearchFilterPlaceHolders(filter);
    }

    public java.lang.String getUsernameAttribute() {
        return usernameAttribute;
    }

    public void setUsernameAttribute(java.lang.String usernameAttribute) {
        this.usernameAttribute = usernameAttribute;
    }

    public void setForceUsernameToLowercase(boolean forceUsernameToLowercase) {
        this.forceUsernameToLowercase = forceUsernameToLowercase;
    }

    public boolean isForceUsernameToLowercase() {
        return forceUsernameToLowercase;
    }

    public java.lang.String getGroupBase() {
        return groupBase;
    }

    public void setGroupBase(java.lang.String groupBase) {
        this.groupBase = groupBase;
    }

    public java.lang.String getGroupObjectClass() {
        return groupObjectClass;
    }

    public void setGroupObjectClass(java.lang.String groupObjectClass) {
        this.groupObjectClass = groupObjectClass;
    }

    public java.lang.String getGroupMembershipAttr() {
        return groupMembershipAttr;
    }

    public void setGroupMembershipAttr(java.lang.String groupMembershipAttr) {
        this.groupMembershipAttr = groupMembershipAttr;
    }

    public java.lang.String getGroupNamingAttr() {
        return groupNamingAttr;
    }

    public void setGroupNamingAttr(java.lang.String groupNamingAttr) {
        this.groupNamingAttr = groupNamingAttr;
    }

    public java.lang.String getAdminGroupMappingRules() {
        return adminGroupMappingRules;
    }

    public void setAdminGroupMappingRules(java.lang.String adminGroupMappingRules) {
        this.adminGroupMappingRules = adminGroupMappingRules;
    }

    public java.lang.String getGroupSearchFilter() {
        return groupSearchFilter;
    }

    public void setGroupSearchFilter(java.lang.String groupSearchFilter) {
        this.groupSearchFilter = groupSearchFilter;
    }

    public void setUserSearchFilter(java.lang.String userSearchFilter) {
        this.userSearchFilter = userSearchFilter;
    }

    public void setAlternateUserSearchFilterEnabled(boolean alternateUserSearchFilterEnabled) {
        this.alternateUserSearchFilterEnabled = alternateUserSearchFilterEnabled;
    }

    public boolean isAlternateUserSearchFilterEnabled() {
        return alternateUserSearchFilterEnabled;
    }

    public void setAlternateUserSearchFilter(java.lang.String alternateUserSearchFilter) {
        this.alternateUserSearchFilter = alternateUserSearchFilter;
    }

    public boolean isGroupMappingEnabled() {
        return groupMappingEnabled;
    }

    public void setGroupMappingEnabled(boolean groupMappingEnabled) {
        this.groupMappingEnabled = groupMappingEnabled;
    }

    public void setUserBase(java.lang.String userBase) {
        this.userBase = userBase;
    }

    public void setUserObjectClass(java.lang.String userObjectClass) {
        this.userObjectClass = userObjectClass;
    }

    public java.lang.String getUserBase() {
        return userBase;
    }

    public java.lang.String getUserObjectClass() {
        return userObjectClass;
    }

    public java.lang.String getDnAttribute() {
        return dnAttribute;
    }

    public void setDnAttribute(java.lang.String dnAttribute) {
        this.dnAttribute = dnAttribute;
    }

    public void setReferralMethod(java.lang.String referralMethod) {
        this.referralMethod = referralMethod;
    }

    public java.lang.String getReferralMethod() {
        return referralMethod;
    }

    public boolean isDisableEndpointIdentification() {
        return disableEndpointIdentification;
    }

    public void setDisableEndpointIdentification(boolean disableEndpointIdentification) {
        this.disableEndpointIdentification = disableEndpointIdentification;
    }

    public boolean isPaginationEnabled() {
        return paginationEnabled;
    }

    public void setPaginationEnabled(boolean paginationEnabled) {
        this.paginationEnabled = paginationEnabled;
    }

    public java.lang.String getSyncGroupMemberReplacePattern() {
        return syncGroupMemberReplacePattern;
    }

    public void setSyncGroupMemberReplacePattern(java.lang.String syncGroupMemberReplacePattern) {
        this.syncGroupMemberReplacePattern = syncGroupMemberReplacePattern;
    }

    public java.lang.String getSyncUserMemberReplacePattern() {
        return syncUserMemberReplacePattern;
    }

    public void setSyncUserMemberReplacePattern(java.lang.String syncUserMemberReplacePattern) {
        this.syncUserMemberReplacePattern = syncUserMemberReplacePattern;
    }

    public java.lang.String getSyncUserMemberFilter() {
        return syncUserMemberFilter;
    }

    public void setSyncUserMemberFilter(java.lang.String syncUserMemberFilter) {
        this.syncUserMemberFilter = syncUserMemberFilter;
    }

    public java.lang.String getSyncGroupMemberFilter() {
        return syncGroupMemberFilter;
    }

    public void setSyncGroupMemberFilter(java.lang.String syncGroupMemberFilter) {
        this.syncGroupMemberFilter = syncGroupMemberFilter;
    }

    public java.lang.String getAdminGroupMappingMemberAttr() {
        return adminGroupMappingMemberAttr;
    }

    public void setAdminGroupMappingMemberAttr(java.lang.String adminGroupMappingMemberAttr) {
        this.adminGroupMappingMemberAttr = adminGroupMappingMemberAttr;
    }

    @java.lang.Override
    public final boolean equals(java.lang.Object obj) {
        return org.apache.commons.lang3.builder.EqualsBuilder.reflectionEquals(this, obj, false);
    }

    @java.lang.Override
    public final int hashCode() {
        return org.apache.commons.lang3.builder.HashCodeBuilder.reflectionHashCode(1, 31, this);
    }

    protected java.lang.String resolveUserSearchFilterPlaceHolders(java.lang.String filter) {
        return filter.replace("{usernameAttribute}", usernameAttribute).replace("{userObjectClass}", userObjectClass);
    }
}