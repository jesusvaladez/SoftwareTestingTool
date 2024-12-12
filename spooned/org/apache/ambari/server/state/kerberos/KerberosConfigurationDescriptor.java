package org.apache.ambari.server.state.kerberos;
public class KerberosConfigurationDescriptor extends org.apache.ambari.server.state.kerberos.AbstractKerberosDescriptor {
    private java.util.Map<java.lang.String, java.lang.String> properties = null;

    public KerberosConfigurationDescriptor(java.util.Map<?, ?> data) {
        if (data != null) {
            java.util.Set<?> keySet = data.keySet();
            if (!keySet.isEmpty()) {
                java.lang.Object key = keySet.iterator().next();
                if (key != null) {
                    java.lang.Object object = data.get(key);
                    setType(key.toString());
                    if (object instanceof java.util.Map) {
                        for (java.util.Map.Entry<?, ?> entry : ((java.util.Map<?, ?>) (object)).entrySet()) {
                            java.lang.Object value = entry.getValue();
                            putProperty(entry.getKey().toString(), value == null ? null : value.toString());
                        }
                    }
                }
            }
        }
    }

    public java.lang.String getType() {
        return getName();
    }

    public void setType(java.lang.String type) {
        setName(type);
    }

    public void setProperties(java.util.Map<java.lang.String, java.lang.String> properties) {
        if (properties == null) {
            this.properties = null;
        } else {
            this.properties = new java.util.TreeMap<>(properties);
        }
    }

    public java.util.Map<java.lang.String, java.lang.String> getProperties() {
        return properties;
    }

    public java.lang.String getProperty(java.lang.String name) {
        return (name == null) || (properties == null) ? null : properties.get(name);
    }

    public void putProperty(java.lang.String name, java.lang.String value) {
        if (name == null) {
            throw new java.lang.IllegalArgumentException("The property name must not be null");
        }
        if (properties == null) {
            properties = new java.util.TreeMap<>();
        }
        properties.put(name, value);
    }

    public void update(org.apache.ambari.server.state.kerberos.KerberosConfigurationDescriptor updates) {
        if (updates != null) {
            setType(updates.getType());
            java.util.Map<java.lang.String, java.lang.String> updatedProperties = updates.getProperties();
            if (updatedProperties != null) {
                for (java.util.Map.Entry<java.lang.String, java.lang.String> entry : updatedProperties.entrySet()) {
                    putProperty(entry.getKey(), entry.getValue());
                }
            }
        }
    }

    @java.lang.Override
    public java.util.Map<java.lang.String, java.lang.Object> toMap() {
        java.util.Map<java.lang.String, java.lang.Object> map = new java.util.TreeMap<>();
        map.put(getName(), properties == null ? null : new java.util.TreeMap<java.lang.String, java.lang.Object>(properties));
        return map;
    }

    @java.lang.Override
    public int hashCode() {
        return super.hashCode() + (getProperties() == null ? 0 : getProperties().hashCode());
    }

    @java.lang.Override
    public boolean equals(java.lang.Object object) {
        if (object == null) {
            return false;
        } else if (object == this) {
            return true;
        } else if (object.getClass() == org.apache.ambari.server.state.kerberos.KerberosConfigurationDescriptor.class) {
            org.apache.ambari.server.state.kerberos.KerberosConfigurationDescriptor descriptor = ((org.apache.ambari.server.state.kerberos.KerberosConfigurationDescriptor) (object));
            return super.equals(object) && (getProperties() == null ? descriptor.getProperties() == null : getProperties().equals(descriptor.getProperties()));
        } else {
            return false;
        }
    }
}