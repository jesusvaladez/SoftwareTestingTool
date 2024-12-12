package org.apache.ambari.server.topology;
public class SecurityConfigurationFactory {
    private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.topology.SecurityConfigurationFactory.class);

    public static final java.lang.String SECURITY_PROPERTY_ID = "security";

    public static final java.lang.String TYPE_PROPERTY_ID = "type";

    public static final java.lang.String KERBEROS_DESCRIPTOR_PROPERTY_ID = "kerberos_descriptor";

    public static final java.lang.String KERBEROS_DESCRIPTOR_REFERENCE_PROPERTY_ID = "kerberos_descriptor_reference";

    @com.google.inject.Inject
    protected com.google.gson.Gson jsonSerializer;

    @com.google.inject.Inject
    private org.apache.ambari.server.orm.dao.KerberosDescriptorDAO kerberosDescriptorDAO;

    @com.google.inject.Inject
    private org.apache.ambari.server.topology.KerberosDescriptorFactory kerberosDescriptorFactory;

    public SecurityConfigurationFactory() {
    }

    protected SecurityConfigurationFactory(com.google.gson.Gson jsonSerializer, org.apache.ambari.server.orm.dao.KerberosDescriptorDAO kerberosDescriptorDAO, org.apache.ambari.server.topology.KerberosDescriptorFactory kerberosDescriptorFactory) {
        this.jsonSerializer = jsonSerializer;
        this.kerberosDescriptorDAO = kerberosDescriptorDAO;
        this.kerberosDescriptorFactory = kerberosDescriptorFactory;
    }

    public org.apache.ambari.server.topology.SecurityConfiguration createSecurityConfigurationFromRequest(java.util.Map<java.lang.String, java.lang.Object> properties, boolean persistEmbeddedDescriptor) {
        org.apache.ambari.server.topology.SecurityConfiguration securityConfiguration;
        org.apache.ambari.server.topology.SecurityConfigurationFactory.LOGGER.debug("Creating security configuration from properties: {}", properties);
        java.util.Map<?, ?> securityProperties = ((java.util.Map<?, ?>) (properties.get(org.apache.ambari.server.topology.SecurityConfigurationFactory.SECURITY_PROPERTY_ID)));
        if (securityProperties == null) {
            org.apache.ambari.server.topology.SecurityConfigurationFactory.LOGGER.debug("No security information properties provided, returning null");
            return null;
        }
        java.lang.String securityTypeString = com.google.common.base.Strings.emptyToNull(((java.lang.String) (securityProperties.get(org.apache.ambari.server.topology.SecurityConfigurationFactory.TYPE_PROPERTY_ID))));
        if (securityTypeString == null) {
            org.apache.ambari.server.topology.SecurityConfigurationFactory.LOGGER.error("Type is missing from security block.");
            throw new java.lang.IllegalArgumentException("Type missing from security block.");
        }
        org.apache.ambari.server.state.SecurityType securityType = com.google.common.base.Enums.getIfPresent(org.apache.ambari.server.state.SecurityType.class, securityTypeString).orNull();
        if (securityType == null) {
            org.apache.ambari.server.topology.SecurityConfigurationFactory.LOGGER.error("Unsupported security type specified: {}", securityType);
            throw new java.lang.IllegalArgumentException("Invalid security type specified: " + securityTypeString);
        }
        if (securityType == org.apache.ambari.server.state.SecurityType.KERBEROS) {
            java.lang.String descriptorReference = com.google.common.base.Strings.emptyToNull(((java.lang.String) (securityProperties.get(org.apache.ambari.server.topology.SecurityConfigurationFactory.KERBEROS_DESCRIPTOR_REFERENCE_PROPERTY_ID))));
            java.lang.Object descriptorJsonMap = securityProperties.get(org.apache.ambari.server.topology.SecurityConfigurationFactory.KERBEROS_DESCRIPTOR_PROPERTY_ID);
            if ((descriptorReference != null) && (descriptorJsonMap != null)) {
                org.apache.ambari.server.topology.SecurityConfigurationFactory.LOGGER.error("Both kerberos descriptor and kerberos descriptor reference are set in the security configuration!");
                throw new java.lang.IllegalArgumentException(((("Usage of properties : " + org.apache.ambari.server.topology.SecurityConfigurationFactory.KERBEROS_DESCRIPTOR_PROPERTY_ID) + " and ") + org.apache.ambari.server.topology.SecurityConfigurationFactory.KERBEROS_DESCRIPTOR_REFERENCE_PROPERTY_ID) + " at the same time, is not allowed.");
            }
            if (descriptorJsonMap != null) {
                org.apache.ambari.server.topology.SecurityConfigurationFactory.LOGGER.debug("Found embedded descriptor: {}", descriptorJsonMap);
                java.lang.String descriptorText = jsonSerializer.toJson(descriptorJsonMap, java.util.Map.class);
                if (persistEmbeddedDescriptor) {
                    descriptorReference = persistKerberosDescriptor(descriptorText);
                }
                java.util.Map<?, ?> descriptorMap = ((java.util.Map<?, ?>) (descriptorJsonMap));
                securityConfiguration = (persistEmbeddedDescriptor) ? org.apache.ambari.server.topology.SecurityConfiguration.withReference(descriptorReference) : org.apache.ambari.server.topology.SecurityConfiguration.withDescriptor(descriptorMap);
            } else if (descriptorReference != null) {
                org.apache.ambari.server.topology.SecurityConfigurationFactory.LOGGER.debug("Found descriptor reference: {}", descriptorReference);
                securityConfiguration = loadSecurityConfigurationByReference(descriptorReference);
            } else {
                org.apache.ambari.server.topology.SecurityConfigurationFactory.LOGGER.debug("There is no security descriptor found in the request");
                securityConfiguration = org.apache.ambari.server.topology.SecurityConfiguration.KERBEROS;
            }
        } else {
            org.apache.ambari.server.topology.SecurityConfigurationFactory.LOGGER.debug("There is no security configuration found in the request");
            securityConfiguration = org.apache.ambari.server.topology.SecurityConfiguration.NONE;
        }
        return securityConfiguration;
    }

    public org.apache.ambari.server.topology.SecurityConfiguration loadSecurityConfigurationByReference(java.lang.String reference) {
        org.apache.ambari.server.topology.SecurityConfiguration securityConfiguration;
        org.apache.ambari.server.topology.SecurityConfigurationFactory.LOGGER.debug("Loading security configuration by reference: {}", reference);
        if (reference == null) {
            org.apache.ambari.server.topology.SecurityConfigurationFactory.LOGGER.error("No security configuration reference provided!");
            throw new java.lang.IllegalArgumentException("No security configuration reference provided!");
        }
        org.apache.ambari.server.orm.entities.KerberosDescriptorEntity descriptorEntity = kerberosDescriptorDAO.findByName(reference);
        if (descriptorEntity == null) {
            org.apache.ambari.server.topology.SecurityConfigurationFactory.LOGGER.error("No security configuration found for the reference: {}", reference);
            throw new java.lang.IllegalArgumentException("No security configuration found for the reference: " + reference);
        }
        java.lang.String descriptorText = descriptorEntity.getKerberosDescriptorText();
        java.util.Map<java.lang.String, ?> descriptorMap = jsonSerializer.<java.util.Map<java.lang.String, ?>>fromJson(descriptorText, java.util.Map.class);
        securityConfiguration = org.apache.ambari.server.topology.SecurityConfiguration.withDescriptor(descriptorMap);
        return securityConfiguration;
    }

    private java.lang.String persistKerberosDescriptor(java.lang.String descriptor) {
        org.apache.ambari.server.topology.SecurityConfigurationFactory.LOGGER.debug("Generating new kerberos descriptor reference ...");
        java.lang.String kdReference = generateKerberosDescriptorReference();
        org.apache.ambari.server.topology.KerberosDescriptor kerberosDescriptor = kerberosDescriptorFactory.createKerberosDescriptor(kdReference, descriptor);
        org.apache.ambari.server.topology.SecurityConfigurationFactory.LOGGER.debug("Persisting kerberos descriptor ...");
        kerberosDescriptorDAO.create(kerberosDescriptor.toEntity());
        return kdReference;
    }

    private java.lang.String generateKerberosDescriptorReference() {
        java.lang.String kdReference = java.util.UUID.randomUUID().toString();
        org.apache.ambari.server.topology.SecurityConfigurationFactory.LOGGER.debug("Generated new kerberos descriptor reference: {}", kdReference);
        return kdReference;
    }
}