package org.apache.ambari.server.ldap.service.ads.detectors;
import org.apache.directory.api.ldap.model.entry.Entry;
public class GroupObjectClassDetector extends org.apache.ambari.server.ldap.service.ads.detectors.OccurrenceAndWeightBasedDetector {
    private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.ldap.service.ads.detectors.GroupObjectClassDetector.class);

    private enum ObjectClassValue {

        GROUP("group", 1),
        GROUP_OF_NAMES("groupOfNames", 1),
        POSIX_GROUP("posixGroup", 1),
        GROUP_OF_UNIQUE_NAMES("groupOfUniqueNames", 1);
        private java.lang.String ocVal;

        private java.lang.Integer weight;

        ObjectClassValue(java.lang.String attr, java.lang.Integer weght) {
            this.ocVal = attr;
            this.weight = weght;
        }

        java.lang.Integer weight() {
            return this.weight;
        }

        java.lang.String ocVal() {
            return this.ocVal;
        }
    }

    @javax.inject.Inject
    public GroupObjectClassDetector() {
        for (org.apache.ambari.server.ldap.service.ads.detectors.GroupObjectClassDetector.ObjectClassValue ocVal : org.apache.ambari.server.ldap.service.ads.detectors.GroupObjectClassDetector.ObjectClassValue.values()) {
            occurrenceMap().put(ocVal.ocVal(), 0);
            weightsMap().put(ocVal.ocVal(), ocVal.weight());
        }
    }

    @java.lang.Override
    protected boolean applies(org.apache.directory.api.ldap.model.entry.Entry entry, java.lang.String attribute) {
        return entry.hasObjectClass(attribute);
    }

    @java.lang.Override
    public java.lang.String detectedProperty() {
        return org.apache.ambari.server.configuration.AmbariServerConfigurationKey.GROUP_OBJECT_CLASS.key();
    }
}