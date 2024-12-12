package org.apache.ambari.server.ldap.service.ads.detectors;
import org.apache.directory.api.ldap.model.entry.Entry;
@javax.inject.Singleton
public class ChainedAttributeDetector implements org.apache.ambari.server.ldap.service.AttributeDetector<org.apache.directory.api.ldap.model.entry.Entry> {
    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.ldap.service.ads.detectors.ChainedAttributeDetector.class);

    private final java.util.Set<org.apache.ambari.server.ldap.service.AttributeDetector> detectors;

    @javax.inject.Inject
    public ChainedAttributeDetector(java.util.Set<org.apache.ambari.server.ldap.service.AttributeDetector> detectors) {
        this.detectors = detectors;
    }

    @java.lang.Override
    public void collect(org.apache.directory.api.ldap.model.entry.Entry entry) {
        for (org.apache.ambari.server.ldap.service.AttributeDetector detector : detectors) {
            org.apache.ambari.server.ldap.service.ads.detectors.ChainedAttributeDetector.LOG.info("Collecting information for the detector: [{}]", detector);
            detector.collect(entry);
        }
    }

    @java.lang.Override
    public java.util.Map<java.lang.String, java.lang.String> detect() {
        java.util.Map<java.lang.String, java.lang.String> detectedAttributes = com.google.common.collect.Maps.newHashMap();
        for (org.apache.ambari.server.ldap.service.AttributeDetector detector : detectors) {
            org.apache.ambari.server.ldap.service.ads.detectors.ChainedAttributeDetector.LOG.info("Detecting ldap configuration value using the detector: [{}]", detector);
            detectedAttributes.putAll(detector.detect());
        }
        return detectedAttributes;
    }

    @java.lang.Override
    public java.lang.String toString() {
        return (("ChainedAttributeDetector{" + "detectors=") + detectors) + '}';
    }
}