package org.apache.ambari.server.ldap.service.ads.detectors;
import org.apache.directory.api.ldap.model.entry.Entry;
@javax.inject.Singleton
public class UserNameAttrDetector extends org.apache.ambari.server.ldap.service.ads.detectors.OccurrenceAndWeightBasedDetector {
    private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.ldap.service.ads.detectors.UserNameAttrDetector.class);

    private enum UserNameAttrs {

        SAM_ACCOUNT_NAME("sAMAccountName", 5),
        UID("uid", 3),
        CN("cn", 1);
        private java.lang.String attrName;

        private java.lang.Integer weight;

        UserNameAttrs(java.lang.String attr, java.lang.Integer weght) {
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
    public UserNameAttrDetector() {
        for (org.apache.ambari.server.ldap.service.ads.detectors.UserNameAttrDetector.UserNameAttrs nameAttr : org.apache.ambari.server.ldap.service.ads.detectors.UserNameAttrDetector.UserNameAttrs.values()) {
            occurrenceMap().put(nameAttr.attrName(), 0);
            weightsMap().put(nameAttr.attrName(), nameAttr.weight());
        }
    }

    @java.lang.Override
    protected boolean applies(org.apache.directory.api.ldap.model.entry.Entry entry, java.lang.String attribute) {
        org.apache.ambari.server.ldap.service.ads.detectors.UserNameAttrDetector.LOGGER.info("Checking for attribute  [{}] in entry [{}]", attribute, entry.getDn());
        return entry.containsAttribute(attribute);
    }

    @java.lang.Override
    public java.lang.String detectedProperty() {
        return org.apache.ambari.server.configuration.AmbariServerConfigurationKey.USER_NAME_ATTRIBUTE.key();
    }
}