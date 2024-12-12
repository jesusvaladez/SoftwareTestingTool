package org.apache.ambari.server.topology;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import static org.apache.ambari.server.topology.SecurityConfigurationFactory.KERBEROS_DESCRIPTOR_PROPERTY_ID;
import static org.apache.ambari.server.topology.SecurityConfigurationFactory.KERBEROS_DESCRIPTOR_REFERENCE_PROPERTY_ID;
import static org.apache.ambari.server.topology.SecurityConfigurationFactory.TYPE_PROPERTY_ID;
@io.swagger.annotations.ApiModel
@com.fasterxml.jackson.annotation.JsonInclude(com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY)
public class SecurityConfiguration {
    public static final org.apache.ambari.server.topology.SecurityConfiguration NONE = new org.apache.ambari.server.topology.SecurityConfiguration(org.apache.ambari.server.state.SecurityType.NONE, null, null);

    public static final org.apache.ambari.server.topology.SecurityConfiguration KERBEROS = new org.apache.ambari.server.topology.SecurityConfiguration(org.apache.ambari.server.state.SecurityType.KERBEROS, null, null);

    private final org.apache.ambari.server.state.SecurityType type;

    private final java.lang.String descriptorReference;

    private final java.util.Map<?, ?> descriptor;

    public static org.apache.ambari.server.topology.SecurityConfiguration of(org.apache.ambari.server.state.SecurityType type, java.lang.String reference, java.util.Map<?, ?> descriptorMap) {
        if (type == org.apache.ambari.server.state.SecurityType.NONE) {
            return org.apache.ambari.server.topology.SecurityConfiguration.NONE;
        }
        if (type != org.apache.ambari.server.state.SecurityType.KERBEROS) {
            throw new java.lang.IllegalArgumentException("Unexpected SecurityType: " + type);
        }
        if ((reference == null) && (descriptorMap == null)) {
            return org.apache.ambari.server.topology.SecurityConfiguration.KERBEROS;
        }
        if ((reference != null) && (descriptorMap != null)) {
            throw new java.lang.IllegalArgumentException("Cannot set both descriptor and reference");
        }
        return reference != null ? org.apache.ambari.server.topology.SecurityConfiguration.withReference(reference) : org.apache.ambari.server.topology.SecurityConfiguration.withDescriptor(descriptorMap);
    }

    public static org.apache.ambari.server.topology.SecurityConfiguration withReference(java.lang.String reference) {
        return new org.apache.ambari.server.topology.SecurityConfiguration(org.apache.ambari.server.state.SecurityType.KERBEROS, reference, null);
    }

    public static org.apache.ambari.server.topology.SecurityConfiguration withDescriptor(java.util.Map<?, ?> descriptorMap) {
        return new org.apache.ambari.server.topology.SecurityConfiguration(org.apache.ambari.server.state.SecurityType.KERBEROS, null, descriptorMap);
    }

    public static org.apache.ambari.server.topology.SecurityConfiguration forTest(org.apache.ambari.server.state.SecurityType type, java.lang.String reference, java.util.Map<?, ?> descriptorMap) {
        return new org.apache.ambari.server.topology.SecurityConfiguration(type, reference, descriptorMap);
    }

    @com.fasterxml.jackson.annotation.JsonCreator
    SecurityConfiguration(@com.fasterxml.jackson.annotation.JsonProperty(org.apache.ambari.server.topology.SecurityConfigurationFactory.TYPE_PROPERTY_ID)
    org.apache.ambari.server.state.SecurityType type, @com.fasterxml.jackson.annotation.JsonProperty(org.apache.ambari.server.topology.SecurityConfigurationFactory.KERBEROS_DESCRIPTOR_REFERENCE_PROPERTY_ID)
    java.lang.String descriptorReference, @com.fasterxml.jackson.annotation.JsonProperty(org.apache.ambari.server.topology.SecurityConfigurationFactory.KERBEROS_DESCRIPTOR_PROPERTY_ID)
    java.util.Map<?, ?> descriptorMap) {
        this.type = type;
        this.descriptorReference = descriptorReference;
        this.descriptor = (descriptorMap != null) ? com.google.common.collect.ImmutableMap.copyOf(descriptorMap) : null;
    }

    @com.fasterxml.jackson.annotation.JsonProperty(org.apache.ambari.server.topology.SecurityConfigurationFactory.TYPE_PROPERTY_ID)
    @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.topology.SecurityConfigurationFactory.TYPE_PROPERTY_ID)
    public org.apache.ambari.server.state.SecurityType getType() {
        return type;
    }

    @com.fasterxml.jackson.annotation.JsonProperty(org.apache.ambari.server.topology.SecurityConfigurationFactory.KERBEROS_DESCRIPTOR_PROPERTY_ID)
    @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.topology.SecurityConfigurationFactory.KERBEROS_DESCRIPTOR_PROPERTY_ID)
    public java.util.Map<?, ?> _getDescriptor() {
        return getDescriptor().isPresent() ? descriptor : com.google.common.collect.ImmutableMap.of();
    }

    @org.apache.ambari.annotations.ApiIgnore
    @com.fasterxml.jackson.annotation.JsonIgnore
    public java.util.Optional<java.util.Map<?, ?>> getDescriptor() {
        return java.util.Optional.ofNullable(descriptor);
    }

    @com.fasterxml.jackson.annotation.JsonProperty(org.apache.ambari.server.topology.SecurityConfigurationFactory.KERBEROS_DESCRIPTOR_REFERENCE_PROPERTY_ID)
    @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.topology.SecurityConfigurationFactory.KERBEROS_DESCRIPTOR_REFERENCE_PROPERTY_ID)
    public java.lang.String getDescriptorReference() {
        return descriptorReference;
    }

    @java.lang.Override
    public boolean equals(java.lang.Object obj) {
        if (this == obj) {
            return true;
        }
        if ((obj == null) || (obj.getClass() != getClass())) {
            return false;
        }
        org.apache.ambari.server.topology.SecurityConfiguration other = ((org.apache.ambari.server.topology.SecurityConfiguration) (obj));
        return (java.util.Objects.equals(type, other.type) && java.util.Objects.equals(descriptor, other.descriptor)) && java.util.Objects.equals(descriptorReference, other.descriptorReference);
    }

    @java.lang.Override
    public int hashCode() {
        return java.util.Objects.hash(type, descriptor, descriptorReference);
    }
}