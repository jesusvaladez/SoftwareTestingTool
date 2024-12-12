package org.apache.ambari.server.controller.internal;
public class RootServiceComponentPropertyProvider extends org.apache.ambari.server.controller.internal.BaseProvider implements org.apache.ambari.server.controller.spi.PropertyProvider {
    public static final java.lang.String JCE_POLICY_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("RootServiceComponents", "jce_policy");

    public static final java.lang.String CIPHER_PROPERTIES_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("RootServiceComponents", "ciphers");

    private static final java.util.Set<java.lang.String> SUPPORTED_PROPERTY_IDS;

    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.controller.internal.RootServiceComponentPropertyProvider.class);

    static {
        java.util.Set<java.lang.String> propertyIds = new java.util.HashSet<>();
        propertyIds.add(JCE_POLICY_PROPERTY_ID);
        propertyIds.add(CIPHER_PROPERTIES_PROPERTY_ID);
        SUPPORTED_PROPERTY_IDS = java.util.Collections.unmodifiableSet(propertyIds);
    }

    private static final java.util.Map<java.lang.String, java.lang.Integer> CACHED_CIPHER_MAX_KEY_LENGTHS = new java.util.HashMap<>();

    public RootServiceComponentPropertyProvider() {
        super(org.apache.ambari.server.controller.internal.RootServiceComponentPropertyProvider.SUPPORTED_PROPERTY_IDS);
    }

    @java.lang.Override
    public java.util.Set<org.apache.ambari.server.controller.spi.Resource> populateResources(java.util.Set<org.apache.ambari.server.controller.spi.Resource> resources, org.apache.ambari.server.controller.spi.Request request, org.apache.ambari.server.controller.spi.Predicate predicate) throws org.apache.ambari.server.controller.spi.SystemException {
        java.util.Set<java.lang.String> requestedIds = request.getPropertyIds();
        for (org.apache.ambari.server.controller.spi.Resource resource : resources) {
            if (org.apache.ambari.server.controller.RootComponent.AMBARI_SERVER.name().equals(resource.getPropertyValue(org.apache.ambari.server.controller.internal.RootServiceComponentResourceProvider.COMPONENT_NAME_PROPERTY_ID))) {
                if (requestedIds.contains(org.apache.ambari.server.controller.internal.RootServiceComponentPropertyProvider.JCE_POLICY_PROPERTY_ID) || requestedIds.contains(org.apache.ambari.server.controller.internal.RootServiceComponentPropertyProvider.CIPHER_PROPERTIES_PROPERTY_ID)) {
                    setCipherDetails(resource, requestedIds);
                }
            }
        }
        return resources;
    }

    private void setCipherDetails(org.apache.ambari.server.controller.spi.Resource resource, java.util.Set<java.lang.String> requestedIds) {
        synchronized(org.apache.ambari.server.controller.internal.RootServiceComponentPropertyProvider.CACHED_CIPHER_MAX_KEY_LENGTHS) {
            if (org.apache.ambari.server.controller.internal.RootServiceComponentPropertyProvider.CACHED_CIPHER_MAX_KEY_LENGTHS.isEmpty()) {
                for (java.security.Provider provider : java.security.Security.getProviders()) {
                    java.lang.String providerName = provider.getName();
                    for (java.security.Provider.Service service : provider.getServices()) {
                        java.lang.String algorithmName = service.getAlgorithm();
                        if ("Cipher".equalsIgnoreCase(service.getType())) {
                            try {
                                org.apache.ambari.server.controller.internal.RootServiceComponentPropertyProvider.CACHED_CIPHER_MAX_KEY_LENGTHS.put(java.lang.String.format("%s.%s", providerName, algorithmName).toLowerCase(), javax.crypto.Cipher.getMaxAllowedKeyLength(algorithmName));
                            } catch (java.security.NoSuchAlgorithmException e) {
                                org.apache.ambari.server.controller.internal.RootServiceComponentPropertyProvider.LOG.warn(java.lang.String.format("Failed to get the max key length of cipher %s, skipping.", algorithmName), e);
                            }
                        }
                    }
                }
            }
        }
        if (requestedIds.contains(org.apache.ambari.server.controller.internal.RootServiceComponentPropertyProvider.CIPHER_PROPERTIES_PROPERTY_ID)) {
            for (java.util.Map.Entry<java.lang.String, java.lang.Integer> entry : org.apache.ambari.server.controller.internal.RootServiceComponentPropertyProvider.CACHED_CIPHER_MAX_KEY_LENGTHS.entrySet()) {
                org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId(org.apache.ambari.server.controller.internal.RootServiceComponentPropertyProvider.CIPHER_PROPERTIES_PROPERTY_ID, entry.getKey()), entry.getValue(), requestedIds);
            }
        }
        if (requestedIds.contains(org.apache.ambari.server.controller.internal.RootServiceComponentPropertyProvider.JCE_POLICY_PROPERTY_ID)) {
            java.lang.Boolean unlimitedKeyJCEPolicyInstalled = null;
            java.util.Map.Entry<java.lang.String, java.lang.Integer> entry = org.apache.ambari.server.controller.internal.RootServiceComponentPropertyProvider.CACHED_CIPHER_MAX_KEY_LENGTHS.entrySet().iterator().next();
            if (entry != null) {
                unlimitedKeyJCEPolicyInstalled = java.lang.Integer.MAX_VALUE == entry.getValue();
            }
            org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId(org.apache.ambari.server.controller.internal.RootServiceComponentPropertyProvider.JCE_POLICY_PROPERTY_ID, "unlimited_key"), unlimitedKeyJCEPolicyInstalled, requestedIds);
        }
    }
}