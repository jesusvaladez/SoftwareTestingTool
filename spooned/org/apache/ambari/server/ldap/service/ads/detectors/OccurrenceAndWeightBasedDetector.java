package org.apache.ambari.server.ldap.service.ads.detectors;
import org.apache.directory.api.ldap.model.entry.Entry;
public abstract class OccurrenceAndWeightBasedDetector implements org.apache.ambari.server.ldap.service.AttributeDetector<org.apache.directory.api.ldap.model.entry.Entry> {
    private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.ldap.service.ads.detectors.OccurrenceAndWeightBasedDetector.class);

    private java.util.Map<java.lang.String, java.lang.Integer> occurrenceMap = com.google.common.collect.Maps.newHashMap();

    private java.util.Map<java.lang.String, java.lang.Integer> weightsMap = com.google.common.collect.Maps.newHashMap();

    protected java.util.Map<java.lang.String, java.lang.Integer> occurrenceMap() {
        return occurrenceMap;
    }

    protected java.util.Map<java.lang.String, java.lang.Integer> weightsMap() {
        return weightsMap;
    }

    protected abstract boolean applies(org.apache.directory.api.ldap.model.entry.Entry entry, java.lang.String attribute);

    public abstract java.lang.String detectedProperty();

    @java.lang.Override
    public java.util.Map<java.lang.String, java.lang.String> detect() {
        org.apache.ambari.server.ldap.service.ads.detectors.OccurrenceAndWeightBasedDetector.LOGGER.info("Calculating the most probable attribute/value ...");
        java.util.Map<java.lang.String, java.lang.String> detectedMap = com.google.common.collect.Maps.newHashMap();
        java.util.Map.Entry<java.lang.String, java.lang.Integer> selectedEntry = null;
        for (java.util.Map.Entry<java.lang.String, java.lang.Integer> entry : occurrenceMap().entrySet()) {
            if (selectedEntry == null) {
                selectedEntry = entry;
                org.apache.ambari.server.ldap.service.ads.detectors.OccurrenceAndWeightBasedDetector.LOGGER.debug("Initial attribute / value entry: {}", selectedEntry);
                continue;
            }
            if (selectedEntry.getValue() < entry.getValue()) {
                org.apache.ambari.server.ldap.service.ads.detectors.OccurrenceAndWeightBasedDetector.LOGGER.info("Changing potential attribute / value entry from : [{}] to: [{}]", selectedEntry, entry);
                selectedEntry = entry;
            }
        }
        java.lang.String detectedVal = "N/A";
        if (selectedEntry.getValue() > 0) {
            detectedVal = selectedEntry.getKey();
        } else {
            org.apache.ambari.server.ldap.service.ads.detectors.OccurrenceAndWeightBasedDetector.LOGGER.warn("Unable to detect attribute or attribute value");
        }
        org.apache.ambari.server.ldap.service.ads.detectors.OccurrenceAndWeightBasedDetector.LOGGER.info("Detected attribute or value: [{}]", detectedVal);
        detectedMap.put(detectedProperty(), detectedVal);
        return detectedMap;
    }

    @java.lang.Override
    public void collect(org.apache.directory.api.ldap.model.entry.Entry entry) {
        org.apache.ambari.server.ldap.service.ads.detectors.OccurrenceAndWeightBasedDetector.LOGGER.info("Collecting ldap attributes/values form entry with dn: [{}]", entry.getDn());
        for (java.lang.String attributeValue : occurrenceMap().keySet()) {
            if (applies(entry, attributeValue)) {
                java.lang.Integer cnt = occurrenceMap().get(attributeValue).intValue();
                if (weightsMap().containsKey(attributeValue)) {
                    cnt = cnt + weightsMap().get(attributeValue);
                } else {
                    cnt = cnt + 1;
                }
                occurrenceMap().put(attributeValue, cnt);
                org.apache.ambari.server.ldap.service.ads.detectors.OccurrenceAndWeightBasedDetector.LOGGER.info("Collected potential name attr: {}, count: {}", attributeValue, cnt);
            } else {
                org.apache.ambari.server.ldap.service.ads.detectors.OccurrenceAndWeightBasedDetector.LOGGER.info("The result entry doesn't contain the attribute: [{}]", attributeValue);
            }
        }
    }
}