package org.apache.ambari.server.state.kerberos;
public class KerberosPrincipalDescriptor extends org.apache.ambari.server.state.kerberos.AbstractKerberosDescriptor {
    static final java.lang.String KEY_VALUE = "value";

    static final java.lang.String KEY_TYPE = "type";

    static final java.lang.String KEY_CONFIGURATION = "configuration";

    static final java.lang.String KEY_LOCAL_USERNAME = "local_username";

    private org.apache.ambari.server.state.kerberos.KerberosPrincipalType type = null;

    private java.lang.String configuration = null;

    private java.lang.String localUsername = null;

    public KerberosPrincipalDescriptor(java.lang.String principal, org.apache.ambari.server.state.kerberos.KerberosPrincipalType type, java.lang.String configuration, java.lang.String localUsername) {
        setName(principal);
        setType(type);
        setConfiguration(configuration);
        setLocalUsername(localUsername);
    }

    public KerberosPrincipalDescriptor(java.util.Map<?, ?> data) {
        this(org.apache.ambari.server.state.kerberos.AbstractKerberosDescriptor.getStringValue(data, org.apache.ambari.server.state.kerberos.KerberosPrincipalDescriptor.KEY_VALUE), org.apache.ambari.server.state.kerberos.KerberosPrincipalDescriptor.getKerberosPrincipalTypeValue(data, org.apache.ambari.server.state.kerberos.KerberosPrincipalDescriptor.KEY_TYPE), org.apache.ambari.server.state.kerberos.AbstractKerberosDescriptor.getStringValue(data, org.apache.ambari.server.state.kerberos.KerberosPrincipalDescriptor.KEY_CONFIGURATION), org.apache.ambari.server.state.kerberos.AbstractKerberosDescriptor.getStringValue(data, org.apache.ambari.server.state.kerberos.KerberosPrincipalDescriptor.KEY_LOCAL_USERNAME));
    }

    public java.lang.String getValue() {
        return getName();
    }

    public void setValue(java.lang.String value) {
        setName(value);
    }

    public org.apache.ambari.server.state.kerberos.KerberosPrincipalType getType() {
        return type;
    }

    public void setType(org.apache.ambari.server.state.kerberos.KerberosPrincipalType type) {
        this.type = type;
    }

    public java.lang.String getConfiguration() {
        return configuration;
    }

    public void setConfiguration(java.lang.String configuration) {
        this.configuration = configuration;
    }

    public java.lang.String getLocalUsername() {
        return localUsername;
    }

    public void setLocalUsername(java.lang.String localUsername) {
        this.localUsername = localUsername;
    }

    public void update(org.apache.ambari.server.state.kerberos.KerberosPrincipalDescriptor updates) {
        if (updates != null) {
            java.lang.String updatedValue;
            updatedValue = updates.getValue();
            if (updatedValue != null) {
                setValue(updatedValue);
            }
            org.apache.ambari.server.state.kerberos.KerberosPrincipalType updatedType = updates.getType();
            if (updatedType != null) {
                setType(updatedType);
            }
            updatedValue = updates.getConfiguration();
            if (updatedValue != null) {
                setConfiguration(updatedValue);
            }
            updatedValue = updates.getLocalUsername();
            if (updatedValue != null) {
                setLocalUsername(updatedValue);
            }
        }
    }

    @java.lang.Override
    public java.util.Map<java.lang.String, java.lang.Object> toMap() {
        java.util.Map<java.lang.String, java.lang.Object> map = new java.util.TreeMap<>();
        map.put(org.apache.ambari.server.state.kerberos.KerberosPrincipalDescriptor.KEY_VALUE, getValue());
        map.put(org.apache.ambari.server.state.kerberos.KerberosPrincipalDescriptor.KEY_TYPE, org.apache.ambari.server.state.kerberos.KerberosPrincipalType.translate(getType()));
        map.put(org.apache.ambari.server.state.kerberos.KerberosPrincipalDescriptor.KEY_CONFIGURATION, getConfiguration());
        map.put(org.apache.ambari.server.state.kerberos.KerberosPrincipalDescriptor.KEY_LOCAL_USERNAME, getLocalUsername());
        return map;
    }

    @java.lang.Override
    public int hashCode() {
        return (super.hashCode() + (getConfiguration() == null ? 0 : getConfiguration().hashCode())) + (getType() == null ? 0 : getType().hashCode());
    }

    @java.lang.Override
    public boolean equals(java.lang.Object object) {
        if (object == null) {
            return false;
        } else if (object == this) {
            return true;
        } else if (object.getClass() == org.apache.ambari.server.state.kerberos.KerberosPrincipalDescriptor.class) {
            org.apache.ambari.server.state.kerberos.KerberosPrincipalDescriptor descriptor = ((org.apache.ambari.server.state.kerberos.KerberosPrincipalDescriptor) (object));
            return (super.equals(object) && (getConfiguration() == null ? descriptor.getConfiguration() == null : getConfiguration().equals(descriptor.getConfiguration()))) && (getType() == null ? descriptor.getType() == null : getType().equals(descriptor.getType()));
        } else {
            return false;
        }
    }

    private static org.apache.ambari.server.state.kerberos.KerberosPrincipalType getKerberosPrincipalTypeValue(java.util.Map<?, ?> map, java.lang.String key) {
        java.lang.String type = org.apache.ambari.server.state.kerberos.AbstractKerberosDescriptor.getStringValue(map, key);
        if ((type == null) || type.isEmpty()) {
            return null;
        } else {
            return org.apache.ambari.server.state.kerberos.KerberosPrincipalType.valueOf(type.toUpperCase());
        }
    }
}