package org.apache.ambari.server.ldap.service.ads.detectors;
import org.apache.directory.api.ldap.model.entry.Entry;
public class GroupMemberAttrDetector extends org.apache.ambari.server.ldap.service.ads.detectors.OccurrenceAndWeightBasedDetector {
    enum GroupMemberAttr {

        MEMBER("member", 1),
        MEMBER_UID("memberUid", 1),
        UNIQUE_MEMBER("uniqueMember", 1);
        private java.lang.String attrName;

        private java.lang.Integer weight;

        GroupMemberAttr(java.lang.String attr, java.lang.Integer weght) {
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
    public GroupMemberAttrDetector() {
        for (org.apache.ambari.server.ldap.service.ads.detectors.GroupMemberAttrDetector.GroupMemberAttr groupMemberAttr : org.apache.ambari.server.ldap.service.ads.detectors.GroupMemberAttrDetector.GroupMemberAttr.values()) {
            occurrenceMap().put(groupMemberAttr.attrName(), 0);
            weightsMap().put(groupMemberAttr.attrName(), groupMemberAttr.weight());
        }
    }

    @java.lang.Override
    protected boolean applies(org.apache.directory.api.ldap.model.entry.Entry entry, java.lang.String attribute) {
        return entry.containsAttribute(attribute);
    }

    @java.lang.Override
    public java.lang.String detectedProperty() {
        return org.apache.ambari.server.configuration.AmbariServerConfigurationKey.GROUP_MEMBER_ATTRIBUTE.key();
    }
}