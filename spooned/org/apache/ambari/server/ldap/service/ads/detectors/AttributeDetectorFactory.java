package org.apache.ambari.server.ldap.service.ads.detectors;
@javax.inject.Singleton
public class AttributeDetectorFactory {
    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.ldap.service.ads.detectors.AttributeDetectorFactory.class);

    private static final java.lang.String USER_ATTRIBUTES_DETECTORS = "UserAttributesDetectors";

    private static final java.lang.String GROUP_ATTRIBUTES_DETECTORS = "GroupAttributesDetectors";

    @javax.inject.Inject
    @javax.inject.Named(org.apache.ambari.server.ldap.service.ads.detectors.AttributeDetectorFactory.GROUP_ATTRIBUTES_DETECTORS)
    java.util.Set<org.apache.ambari.server.ldap.service.AttributeDetector> groupAttributeDetectors;

    @javax.inject.Inject
    @javax.inject.Named(org.apache.ambari.server.ldap.service.ads.detectors.AttributeDetectorFactory.USER_ATTRIBUTES_DETECTORS)
    private java.util.Set<org.apache.ambari.server.ldap.service.AttributeDetector> userAttributeDetectors;

    @javax.inject.Inject
    public AttributeDetectorFactory() {
    }

    public org.apache.ambari.server.ldap.service.ads.detectors.ChainedAttributeDetector userAttributDetector() {
        org.apache.ambari.server.ldap.service.ads.detectors.AttributeDetectorFactory.LOG.info("Creating instance with user attribute detectors: [{}]", userAttributeDetectors);
        return new org.apache.ambari.server.ldap.service.ads.detectors.ChainedAttributeDetector(userAttributeDetectors);
    }

    public org.apache.ambari.server.ldap.service.ads.detectors.ChainedAttributeDetector groupAttributeDetector() {
        org.apache.ambari.server.ldap.service.ads.detectors.AttributeDetectorFactory.LOG.info("Creating instance with group attribute detectors: [{}]", groupAttributeDetectors);
        return new org.apache.ambari.server.ldap.service.ads.detectors.ChainedAttributeDetector(groupAttributeDetectors);
    }
}