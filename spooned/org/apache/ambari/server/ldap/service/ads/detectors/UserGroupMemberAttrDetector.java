package org.apache.ambari.server.ldap.service.ads.detectors;
import org.apache.directory.api.ldap.model.entry.Entry;
public class UserGroupMemberAttrDetector extends org.apache.ambari.server.ldap.service.ads.detectors.OccurrenceAndWeightBasedDetector {
    private enum UserGroupMemberAttr {

        MEMBER_OF("memberOf", 1),
        IS_MEMBER_OF("ismemberOf", 1);
        private java.lang.String attrName;

        private java.lang.Integer weight;

        UserGroupMemberAttr(java.lang.String attr, java.lang.Integer weght) {
            this.attrName = attr;
            this.weight = weght;
        }

        java.lang.Integer weight() {
            return this.weight;
        }

        java.lang.String attrName() {
            return this.attrName;
        }
    }

    @javax.inject.Inject
    public UserGroupMemberAttrDetector() {
        for (org.apache.ambari.server.ldap.service.ads.detectors.UserGroupMemberAttrDetector.UserGroupMemberAttr userGroupMemberAttr : org.apache.ambari.server.ldap.service.ads.detectors.UserGroupMemberAttrDetector.UserGroupMemberAttr.values()) {
            occurrenceMap().put(userGroupMemberAttr.attrName(), 0);
            weightsMap().put(userGroupMemberAttr.attrName(), userGroupMemberAttr.weight);
        }
    }

    @java.lang.Override
    protected boolean applies(org.apache.directory.api.ldap.model.entry.Entry entry, java.lang.String attribute) {
        return entry.containsAttribute(attribute);
    }

    @java.lang.Override
    public java.lang.String detectedProperty() {
        return org.apache.ambari.server.configuration.AmbariServerConfigurationKey.USER_GROUP_MEMBER_ATTRIBUTE.key();
    }
}