package org.apache.ambari.server.ldap.service.ads.detectors;
import org.apache.directory.api.ldap.model.entry.DefaultAttribute;
import org.apache.directory.api.ldap.model.entry.DefaultEntry;
import org.apache.directory.api.ldap.model.entry.Entry;
import org.apache.directory.api.ldap.model.entry.Value;
import org.easymock.TestSubject;
public class GroupMemberAttrDetectorTest {
    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.ldap.service.ads.detectors.GroupMemberAttrDetector.class);

    @org.easymock.TestSubject
    org.apache.ambari.server.ldap.service.ads.detectors.GroupMemberAttrDetector groupMemberAttrDetector = new org.apache.ambari.server.ldap.service.ads.detectors.GroupMemberAttrDetector();

    @org.junit.Test
    public void testShouldDetectAttributeBasedOnOccurrence() throws java.lang.Exception {
        java.util.List<org.apache.directory.api.ldap.model.entry.Entry> sampleEntryList = com.google.common.collect.Lists.newArrayList();
        sampleEntryList.addAll(getSampleEntryList(org.apache.ambari.server.ldap.service.ads.detectors.GroupMemberAttrDetector.GroupMemberAttr.MEMBER_UID, 2));
        sampleEntryList.addAll(getSampleEntryList(org.apache.ambari.server.ldap.service.ads.detectors.GroupMemberAttrDetector.GroupMemberAttr.UNIQUE_MEMBER, 7));
        sampleEntryList.addAll(getSampleEntryList(org.apache.ambari.server.ldap.service.ads.detectors.GroupMemberAttrDetector.GroupMemberAttr.MEMBER, 5));
        for (org.apache.directory.api.ldap.model.entry.Entry entry : sampleEntryList) {
            groupMemberAttrDetector.collect(entry);
        }
        java.util.Map<java.lang.String, java.lang.String> detectedAttributeMap = groupMemberAttrDetector.detect();
        org.junit.Assert.assertEquals(1, detectedAttributeMap.size());
        java.util.Map.Entry<java.lang.String, java.lang.String> selectedEntry = detectedAttributeMap.entrySet().iterator().next();
        org.junit.Assert.assertEquals("The selected configuration property is not the expected one", groupMemberAttrDetector.detectedProperty(), selectedEntry.getKey());
        org.junit.Assert.assertEquals("The selected configuration property value is not the expected one", org.apache.ambari.server.ldap.service.ads.detectors.GroupMemberAttrDetector.GroupMemberAttr.UNIQUE_MEMBER.attrName(), selectedEntry.getValue());
    }

    @org.junit.Test
    public void testShouldDetectorPassWhenEmptySampleSetProvided() throws java.lang.Exception {
        java.util.List<org.apache.directory.api.ldap.model.entry.Entry> sampleEntryList = com.google.common.collect.Lists.newArrayList();
        for (org.apache.directory.api.ldap.model.entry.Entry entry : sampleEntryList) {
            groupMemberAttrDetector.collect(entry);
        }
        java.util.Map<java.lang.String, java.lang.String> detectedAttributeMap = groupMemberAttrDetector.detect();
        org.junit.Assert.assertEquals(1, detectedAttributeMap.size());
        java.util.Map.Entry<java.lang.String, java.lang.String> selectedEntry = detectedAttributeMap.entrySet().iterator().next();
        org.junit.Assert.assertEquals("The selected configuration property is not the expected one", groupMemberAttrDetector.detectedProperty(), selectedEntry.getKey());
        org.junit.Assert.assertEquals("The selected configuration property value is not the expected one", "N/A", selectedEntry.getValue());
    }

    private java.util.List<org.apache.directory.api.ldap.model.entry.Entry> getSampleEntryList(org.apache.ambari.server.ldap.service.ads.detectors.GroupMemberAttrDetector.GroupMemberAttr member, int count) {
        java.util.List<org.apache.directory.api.ldap.model.entry.Entry> entryList = com.google.common.collect.Lists.newArrayList();
        for (int i = 0; i < count; i++) {
            org.apache.directory.api.ldap.model.entry.Entry entry = new org.apache.directory.api.ldap.model.entry.DefaultEntry();
            try {
                entry.setDn((("dn=" + member.name()) + "-") + i);
                entry.add(new org.apache.directory.api.ldap.model.entry.DefaultAttribute(member.attrName(), new org.apache.directory.api.ldap.model.entry.Value("xxx")));
                entryList.add(entry);
            } catch (java.lang.Exception e) {
                org.apache.ambari.server.ldap.service.ads.detectors.GroupMemberAttrDetectorTest.LOG.error(e.getMessage());
            }
        }
        return entryList;
    }
}