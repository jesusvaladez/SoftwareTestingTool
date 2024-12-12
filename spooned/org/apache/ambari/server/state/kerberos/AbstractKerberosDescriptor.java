package org.apache.ambari.server.state.kerberos;
import org.apache.commons.lang.StringUtils;
public abstract class AbstractKerberosDescriptor {
    static final java.lang.String KEY_NAME = "name";

    private org.apache.ambari.server.state.kerberos.AbstractKerberosDescriptor parent = null;

    private java.lang.String name = null;

    public java.util.Map<java.lang.String, java.lang.Object> toMap() {
        java.util.TreeMap<java.lang.String, java.lang.Object> dataMap = new java.util.TreeMap<>();
        java.lang.String name = getName();
        if (name != null) {
            dataMap.put(org.apache.ambari.server.state.kerberos.AbstractKerberosDescriptor.KEY_NAME, name);
        }
        return dataMap;
    }

    public java.lang.String getName() {
        return name;
    }

    public void setName(java.lang.String name) {
        this.name = name;
    }

    public org.apache.ambari.server.state.kerberos.AbstractKerberosDescriptor getParent() {
        return parent;
    }

    public void setParent(org.apache.ambari.server.state.kerberos.AbstractKerberosDescriptor parent) {
        this.parent = parent;
    }

    public boolean isContainer() {
        return false;
    }

    protected static java.lang.Object getValue(java.util.Map<?, ?> map, java.lang.String key) {
        return (map == null) || (key == null) ? null : map.get(key);
    }

    protected static java.lang.String getStringValue(java.util.Map<?, ?> map, java.lang.String key) {
        java.lang.Object value = org.apache.ambari.server.state.kerberos.AbstractKerberosDescriptor.getValue(map, key);
        return value == null ? null : value.toString();
    }

    protected static java.lang.Boolean getBooleanValue(java.util.Map<?, ?> map, java.lang.String key) {
        return org.apache.ambari.server.state.kerberos.AbstractKerberosDescriptor.getBooleanValue(map, key, null);
    }

    protected static java.lang.Boolean getBooleanValue(java.util.Map<?, ?> map, java.lang.String key, java.lang.Boolean defaultValue) {
        java.lang.String value = org.apache.ambari.server.state.kerberos.AbstractKerberosDescriptor.getStringValue(map, key);
        return org.apache.commons.lang.StringUtils.isEmpty(value) ? defaultValue : java.lang.Boolean.valueOf(value);
    }

    protected org.apache.ambari.server.state.kerberos.AbstractKerberosDescriptor getDescriptor(org.apache.ambari.server.state.kerberos.AbstractKerberosDescriptor.Type type, java.lang.String name) {
        return null;
    }

    protected org.apache.ambari.server.state.kerberos.AbstractKerberosDescriptor getRoot() {
        org.apache.ambari.server.state.kerberos.AbstractKerberosDescriptor root = this;
        while (root.getParent() != null) {
            root = root.getParent();
        } 
        return root;
    }

    public static <T> java.util.Collection<T> nullToEmpty(java.util.Collection<T> collection) {
        return collection == null ? java.util.Collections.emptyList() : collection;
    }

    public static <T> java.util.List<T> nullToEmpty(java.util.List<T> list) {
        return list == null ? java.util.Collections.emptyList() : list;
    }

    public static <K, V> java.util.Map<K, V> nullToEmpty(java.util.Map<K, V> collection) {
        return collection == null ? java.util.Collections.emptyMap() : collection;
    }

    @java.lang.Override
    public int hashCode() {
        return 37 * (getName() == null ? 0 : getName().hashCode());
    }

    @java.lang.Override
    public boolean equals(java.lang.Object object) {
        if (object == null) {
            return false;
        } else if (object == this) {
            return true;
        } else if (object instanceof org.apache.ambari.server.state.kerberos.AbstractKerberosDescriptor) {
            org.apache.ambari.server.state.kerberos.AbstractKerberosDescriptor descriptor = ((org.apache.ambari.server.state.kerberos.AbstractKerberosDescriptor) (object));
            return getName() == null ? descriptor.getName() == null : getName().equals(descriptor.getName());
        } else {
            return false;
        }
    }

    public java.lang.String getPath() {
        java.lang.StringBuilder path = new java.lang.StringBuilder();
        org.apache.ambari.server.state.kerberos.AbstractKerberosDescriptor current = this;
        while ((current != null) && (current.getName() != null)) {
            path.insert(0, current.getName());
            path.insert(0, '/');
            current = current.getParent();
        } 
        return path.toString();
    }

    public enum Type {

        SERVICE("service", "services"),
        COMPONENT("component", "components"),
        IDENTITY("identity", "identities"),
        PRINCIPAL("principal", "principals"),
        KEYTAB("keytab", "keytabs"),
        CONFIGURATION("configuration", "configurations"),
        AUTH_TO_LOCAL_PROPERTY("auth_to_local_property", "auth_to_local_properties");
        private final java.lang.String descriptorName;

        private final java.lang.String descriptorPluralName;

        Type(java.lang.String descriptorName, java.lang.String descriptorPluralName) {
            this.descriptorName = descriptorName;
            this.descriptorPluralName = descriptorPluralName;
        }

        public java.lang.String getDescriptorName() {
            return descriptorName;
        }

        public java.lang.String getDescriptorPluralName() {
            return descriptorPluralName;
        }
    }
}