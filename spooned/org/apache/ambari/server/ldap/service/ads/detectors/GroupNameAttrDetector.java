package org.apache.ambari.server.ldap.service.ads.detectors;
import org.apache.directory.api.ldap.model.entry.Entry;
public class GroupNameAttrDetector extends org.apache.ambari.server.ldap.service.ads.detectors.OccurrenceAndWeightBasedDetector {
    private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.ldap.service.ads.detectors.GroupNameAttrDetector.class);

    private enum GroupNameAttr {

        DISTINGUISHED_NAME("distinguishedName", 1),
        CN("cn", 1);
        private java.lang.String attrName;

        private java.lang.Integer weight;

        GroupNameAttr(java.lang.String attr, java.lang.Integer weght) {
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
    public GroupNameAttrDetector() {
        for (org.apache.ambari.server.ldap.service.ads.detectors.GroupNameAttrDetector.GroupNameAttr groupNameAttr : org.apache.ambari.server.ldap.service.ads.detectors.GroupNameAttrDetector.GroupNameAttr.values()) {
            occurrenceMap().put(groupNameAttr.attrName(), 0);
            weightsMap().put(groupNameAttr.attrName(), groupNameAttr.weight());
        }
    }

    @java.lang.Override
    protected boolean applies(org.apache.directory.api.ldap.model.entry.Entry entry, java.lang.String attribute) {
        return entry.containsAttribute(attribute);
    }

    @java.lang.Override
    public java.lang.String detectedProperty() {
        return org.apache.ambari.server.configuration.AmbariServerConfigurationKey.GROUP_NAME_ATTRIBUTE.key();
    }
}