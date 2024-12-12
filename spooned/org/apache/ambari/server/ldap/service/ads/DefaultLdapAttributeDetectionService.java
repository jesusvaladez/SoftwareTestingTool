package org.apache.ambari.server.ldap.service.ads;
import org.apache.directory.api.ldap.model.entry.Entry;
import org.apache.directory.api.ldap.model.exception.LdapException;
import org.apache.directory.api.ldap.model.message.SearchRequest;
import org.apache.directory.api.ldap.model.message.SearchScope;
import org.apache.directory.api.util.Strings;
import org.apache.directory.ldap.client.api.search.FilterBuilder;
import org.apache.directory.ldap.client.template.EntryMapper;
import org.apache.directory.ldap.client.template.LdapConnectionTemplate;
import static org.apache.ambari.server.configuration.AmbariServerConfigurationCategory.LDAP_CONFIGURATION;
@javax.inject.Singleton
public class DefaultLdapAttributeDetectionService implements org.apache.ambari.server.ldap.service.LdapAttributeDetectionService {
    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.ldap.service.ads.DefaultLdapAttributeDetectionService.class);

    private static final int SAMPLE_RESULT_SIZE = 50;

    @javax.inject.Inject
    private org.apache.ambari.server.ldap.service.ads.detectors.AttributeDetectorFactory attributeDetectorFactory;

    @javax.inject.Inject
    private org.apache.ambari.server.ldap.service.ads.LdapConnectionTemplateFactory ldapConnectionTemplateFactory;

    @javax.inject.Inject
    public DefaultLdapAttributeDetectionService() {
    }

    @java.lang.Override
    public org.apache.ambari.server.ldap.domain.AmbariLdapConfiguration detectLdapUserAttributes(org.apache.ambari.server.ldap.domain.AmbariLdapConfiguration ambariLdapConfiguration) throws org.apache.ambari.server.ldap.service.AmbariLdapException {
        org.apache.ambari.server.ldap.service.ads.DefaultLdapAttributeDetectionService.LOG.info("Detecting LDAP user attributes ...");
        if (org.apache.directory.api.util.Strings.isEmpty(ambariLdapConfiguration.userSearchBase())) {
            org.apache.ambari.server.ldap.service.ads.DefaultLdapAttributeDetectionService.LOG.warn("No user search base provided");
            return ambariLdapConfiguration;
        }
        try {
            org.apache.directory.ldap.client.template.LdapConnectionTemplate ldapConnectionTemplate = ldapConnectionTemplateFactory.create(ambariLdapConfiguration);
            org.apache.ambari.server.ldap.service.AttributeDetector<org.apache.directory.api.ldap.model.entry.Entry> userAttributeDetector = attributeDetectorFactory.userAttributDetector();
            org.apache.directory.api.ldap.model.message.SearchRequest searchRequest = assembleUserSearchRequest(ldapConnectionTemplate, ambariLdapConfiguration);
            java.util.List<org.apache.directory.api.ldap.model.entry.Entry> entries = ldapConnectionTemplate.search(searchRequest, getEntryMapper());
            for (org.apache.directory.api.ldap.model.entry.Entry entry : entries) {
                org.apache.ambari.server.ldap.service.ads.DefaultLdapAttributeDetectionService.LOG.info("Collecting user attribute information from the sample entry with dn: [{}]", entry.getDn());
                userAttributeDetector.collect(entry);
            }
            java.util.Map<java.lang.String, java.lang.String> detectedUserAttributes = userAttributeDetector.detect();
            setDetectedAttributes(ambariLdapConfiguration, detectedUserAttributes);
            org.apache.ambari.server.ldap.service.ads.DefaultLdapAttributeDetectionService.LOG.info("Decorated ambari ldap config : [{}]", ambariLdapConfiguration);
        } catch (java.lang.Exception e) {
            org.apache.ambari.server.ldap.service.ads.DefaultLdapAttributeDetectionService.LOG.error("Ldap operation failed while detecting user attributes", e);
            throw new org.apache.ambari.server.ldap.service.AmbariLdapException(e);
        }
        return ambariLdapConfiguration;
    }

    @java.lang.Override
    public org.apache.ambari.server.ldap.domain.AmbariLdapConfiguration detectLdapGroupAttributes(org.apache.ambari.server.ldap.domain.AmbariLdapConfiguration ambariLdapConfiguration) throws org.apache.ambari.server.ldap.service.AmbariLdapException {
        org.apache.ambari.server.ldap.service.ads.DefaultLdapAttributeDetectionService.LOG.info("Detecting LDAP group attributes ...");
        if (org.apache.directory.api.util.Strings.isEmpty(ambariLdapConfiguration.groupSearchBase())) {
            org.apache.ambari.server.ldap.service.ads.DefaultLdapAttributeDetectionService.LOG.warn("No group search base provided");
            return ambariLdapConfiguration;
        }
        try {
            org.apache.directory.ldap.client.template.LdapConnectionTemplate ldapConnectionTemplate = ldapConnectionTemplateFactory.create(ambariLdapConfiguration);
            org.apache.ambari.server.ldap.service.AttributeDetector<org.apache.directory.api.ldap.model.entry.Entry> groupAttributeDetector = attributeDetectorFactory.groupAttributeDetector();
            org.apache.directory.api.ldap.model.message.SearchRequest searchRequest = assembleGroupSearchRequest(ldapConnectionTemplate, ambariLdapConfiguration);
            java.util.List<org.apache.directory.api.ldap.model.entry.Entry> groupEntries = ldapConnectionTemplate.search(searchRequest, getEntryMapper());
            for (org.apache.directory.api.ldap.model.entry.Entry groupEntry : groupEntries) {
                org.apache.ambari.server.ldap.service.ads.DefaultLdapAttributeDetectionService.LOG.info("Collecting group attribute information from the sample entry with dn: [{}]", groupEntry.getDn());
                groupAttributeDetector.collect(groupEntry);
            }
            java.util.Map<java.lang.String, java.lang.String> detectedGroupAttributes = groupAttributeDetector.detect();
            setDetectedAttributes(ambariLdapConfiguration, detectedGroupAttributes);
            org.apache.ambari.server.ldap.service.ads.DefaultLdapAttributeDetectionService.LOG.info("Decorated ambari ldap config : [{}]", ambariLdapConfiguration);
        } catch (java.lang.Exception e) {
            org.apache.ambari.server.ldap.service.ads.DefaultLdapAttributeDetectionService.LOG.error("Ldap operation failed while detecting group attributes", e);
            throw new org.apache.ambari.server.ldap.service.AmbariLdapException(e);
        }
        return ambariLdapConfiguration;
    }

    private void setDetectedAttributes(org.apache.ambari.server.ldap.domain.AmbariLdapConfiguration ambariLdapConfiguration, java.util.Map<java.lang.String, java.lang.String> detectedAttributes) {
        for (java.util.Map.Entry<java.lang.String, java.lang.String> detecteMapEntry : detectedAttributes.entrySet()) {
            org.apache.ambari.server.ldap.service.ads.DefaultLdapAttributeDetectionService.LOG.info("Setting detected configuration value: [{}] - > [{}]", detecteMapEntry.getKey(), detecteMapEntry.getValue());
            org.apache.ambari.server.configuration.AmbariServerConfigurationKey key = org.apache.ambari.server.configuration.AmbariServerConfigurationKey.translate(org.apache.ambari.server.configuration.AmbariServerConfigurationCategory.LDAP_CONFIGURATION, detecteMapEntry.getKey());
            if (key != null) {
                ambariLdapConfiguration.setValueFor(key, detecteMapEntry.getValue());
            }
        }
    }

    private org.apache.directory.api.ldap.model.message.SearchRequest assembleUserSearchRequest(org.apache.directory.ldap.client.template.LdapConnectionTemplate ldapConnectionTemplate, org.apache.ambari.server.ldap.domain.AmbariLdapConfiguration ambariLdapConfiguration) throws org.apache.ambari.server.ldap.service.AmbariLdapException {
        try {
            org.apache.directory.api.ldap.model.message.SearchRequest req = ldapConnectionTemplate.newSearchRequest(ambariLdapConfiguration.userSearchBase(), org.apache.directory.ldap.client.api.search.FilterBuilder.present("objectClass").toString(), SearchScope.SUBTREE);
            req.setSizeLimit(org.apache.ambari.server.ldap.service.ads.DefaultLdapAttributeDetectionService.SAMPLE_RESULT_SIZE);
            return req;
        } catch (java.lang.Exception e) {
            org.apache.ambari.server.ldap.service.ads.DefaultLdapAttributeDetectionService.LOG.error("Could not assemble ldap search request", e);
            throw new org.apache.ambari.server.ldap.service.AmbariLdapException(e);
        }
    }

    private org.apache.directory.api.ldap.model.message.SearchRequest assembleGroupSearchRequest(org.apache.directory.ldap.client.template.LdapConnectionTemplate ldapConnectionTemplate, org.apache.ambari.server.ldap.domain.AmbariLdapConfiguration ambariLdapConfiguration) throws org.apache.ambari.server.ldap.service.AmbariLdapException {
        try {
            org.apache.directory.api.ldap.model.message.SearchRequest req = ldapConnectionTemplate.newSearchRequest(ambariLdapConfiguration.groupSearchBase(), org.apache.directory.ldap.client.api.search.FilterBuilder.present("objectClass").toString(), SearchScope.SUBTREE);
            req.setSizeLimit(org.apache.ambari.server.ldap.service.ads.DefaultLdapAttributeDetectionService.SAMPLE_RESULT_SIZE);
            return req;
        } catch (java.lang.Exception e) {
            org.apache.ambari.server.ldap.service.ads.DefaultLdapAttributeDetectionService.LOG.error("Could not assemble ldap search request", e);
            throw new org.apache.ambari.server.ldap.service.AmbariLdapException(e);
        }
    }

    public org.apache.directory.ldap.client.template.EntryMapper<org.apache.directory.api.ldap.model.entry.Entry> getEntryMapper() {
        return new org.apache.directory.ldap.client.template.EntryMapper<org.apache.directory.api.ldap.model.entry.Entry>() {
            @java.lang.Override
            public org.apache.directory.api.ldap.model.entry.Entry map(org.apache.directory.api.ldap.model.entry.Entry entry) throws org.apache.directory.api.ldap.model.exception.LdapException {
                return entry;
            }
        };
    }
}