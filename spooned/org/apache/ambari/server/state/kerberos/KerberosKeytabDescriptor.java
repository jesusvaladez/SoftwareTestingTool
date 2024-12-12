package org.apache.ambari.server.state.kerberos;
public class KerberosKeytabDescriptor extends org.apache.ambari.server.state.kerberos.AbstractKerberosDescriptor {
    static final java.lang.String KEY_FILE = "file";

    static final java.lang.String KEY_OWNER = "owner";

    static final java.lang.String KEY_GROUP = "group";

    static final java.lang.String KEY_CONFIGURATION = "configuration";

    static final java.lang.String KEY_CACHABLE = "cachable";

    static final java.lang.String KEY_ACL_NAME = "name";

    static final java.lang.String KEY_ACL_ACCESS = "access";

    private java.lang.String ownerName = null;

    private java.lang.String ownerAccess = null;

    private java.lang.String groupName = null;

    private java.lang.String groupAccess = null;

    private java.lang.String configuration = null;

    private boolean cachable = true;

    public KerberosKeytabDescriptor(java.lang.String file, java.lang.String ownerName, java.lang.String ownerAccess, java.lang.String groupName, java.lang.String groupAccess, java.lang.String configuration, boolean cachable) {
        setName(file);
        setOwnerName(ownerName);
        setOwnerAccess(ownerAccess);
        setGroupName(groupName);
        setGroupAccess(groupAccess);
        setConfiguration(configuration);
        setCachable(cachable);
    }

    public KerberosKeytabDescriptor(java.util.Map<?, ?> data) {
        setName(org.apache.ambari.server.state.kerberos.AbstractKerberosDescriptor.getStringValue(data, org.apache.ambari.server.state.kerberos.KerberosKeytabDescriptor.KEY_FILE));
        if (data != null) {
            java.lang.Object object;
            object = data.get(org.apache.ambari.server.state.kerberos.KerberosKeytabDescriptor.KEY_OWNER);
            if (object instanceof java.util.Map) {
                java.util.Map<?, ?> map = ((java.util.Map<?, ?>) (object));
                setOwnerName(org.apache.ambari.server.state.kerberos.AbstractKerberosDescriptor.getStringValue(map, org.apache.ambari.server.state.kerberos.KerberosKeytabDescriptor.KEY_ACL_NAME));
                setOwnerAccess(org.apache.ambari.server.state.kerberos.AbstractKerberosDescriptor.getStringValue(map, org.apache.ambari.server.state.kerberos.KerberosKeytabDescriptor.KEY_ACL_ACCESS));
            }
            object = data.get(org.apache.ambari.server.state.kerberos.KerberosKeytabDescriptor.KEY_GROUP);
            if (object instanceof java.util.Map) {
                java.util.Map<?, ?> map = ((java.util.Map<?, ?>) (object));
                setGroupName(org.apache.ambari.server.state.kerberos.AbstractKerberosDescriptor.getStringValue(map, org.apache.ambari.server.state.kerberos.KerberosKeytabDescriptor.KEY_ACL_NAME));
                setGroupAccess(org.apache.ambari.server.state.kerberos.AbstractKerberosDescriptor.getStringValue(map, org.apache.ambari.server.state.kerberos.KerberosKeytabDescriptor.KEY_ACL_ACCESS));
            }
            setConfiguration(org.apache.ambari.server.state.kerberos.AbstractKerberosDescriptor.getStringValue(data, org.apache.ambari.server.state.kerberos.KerberosKeytabDescriptor.KEY_CONFIGURATION));
            setCachable(!"false".equalsIgnoreCase(org.apache.ambari.server.state.kerberos.AbstractKerberosDescriptor.getStringValue(data, org.apache.ambari.server.state.kerberos.KerberosKeytabDescriptor.KEY_CACHABLE)));
        }
    }

    public java.lang.String getFile() {
        return getName();
    }

    public void setFile(java.lang.String file) {
        setName(file);
    }

    public java.lang.String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(java.lang.String name) {
        this.ownerName = name;
    }

    public java.lang.String getOwnerAccess() {
        return ownerAccess;
    }

    public void setOwnerAccess(java.lang.String access) {
        this.ownerAccess = access;
    }

    public java.lang.String getGroupName() {
        return groupName;
    }

    public void setGroupName(java.lang.String name) {
        this.groupName = name;
    }

    public java.lang.String getGroupAccess() {
        return groupAccess;
    }

    public void setGroupAccess(java.lang.String access) {
        this.groupAccess = access;
    }

    public java.lang.String getConfiguration() {
        return configuration;
    }

    public void setConfiguration(java.lang.String configuration) {
        this.configuration = configuration;
    }

    public boolean isCachable() {
        return cachable;
    }

    public void setCachable(boolean cachable) {
        this.cachable = cachable;
    }

    public void update(org.apache.ambari.server.state.kerberos.KerberosKeytabDescriptor updates) {
        if (updates != null) {
            java.lang.String updatedValue;
            updatedValue = updates.getFile();
            if (updatedValue != null) {
                setFile(updatedValue);
            }
            updatedValue = updates.getConfiguration();
            if (updatedValue != null) {
                setConfiguration(updatedValue);
            }
            updatedValue = updates.getOwnerName();
            if (updatedValue != null) {
                setOwnerName(updatedValue);
            }
            updatedValue = updates.getOwnerAccess();
            if (updatedValue != null) {
                setOwnerAccess(updatedValue);
            }
            updatedValue = updates.getGroupName();
            if (updatedValue != null) {
                setGroupName(updatedValue);
            }
            updatedValue = updates.getGroupAccess();
            if (updatedValue != null) {
                setGroupAccess(updatedValue);
            }
        }
    }

    @java.lang.Override
    public java.util.Map<java.lang.String, java.lang.Object> toMap() {
        java.util.Map<java.lang.String, java.lang.Object> map = new java.util.TreeMap<>();
        java.lang.String data;
        data = getFile();
        map.put(org.apache.ambari.server.state.kerberos.KerberosKeytabDescriptor.KEY_FILE, data);
        java.util.Map<java.lang.String, java.lang.String> owner = new java.util.TreeMap<>();
        data = getOwnerName();
        if (data != null) {
            owner.put(org.apache.ambari.server.state.kerberos.KerberosKeytabDescriptor.KEY_ACL_NAME, data);
        }
        data = getOwnerAccess();
        if (data != null) {
            owner.put(org.apache.ambari.server.state.kerberos.KerberosKeytabDescriptor.KEY_ACL_ACCESS, data);
        }
        if (!owner.isEmpty()) {
            map.put(org.apache.ambari.server.state.kerberos.KerberosKeytabDescriptor.KEY_OWNER, owner);
        }
        java.util.Map<java.lang.String, java.lang.String> group = new java.util.TreeMap<>();
        data = getGroupName();
        if (data != null) {
            group.put(org.apache.ambari.server.state.kerberos.KerberosKeytabDescriptor.KEY_ACL_NAME, data);
        }
        data = getGroupAccess();
        if (data != null) {
            group.put(org.apache.ambari.server.state.kerberos.KerberosKeytabDescriptor.KEY_ACL_ACCESS, data);
        }
        if (!group.isEmpty()) {
            map.put(org.apache.ambari.server.state.kerberos.KerberosKeytabDescriptor.KEY_GROUP, group);
        }
        data = getConfiguration();
        if (data != null) {
            map.put(org.apache.ambari.server.state.kerberos.KerberosKeytabDescriptor.KEY_CONFIGURATION, data);
        }
        return map;
    }

    @java.lang.Override
    public int hashCode() {
        return (((((super.hashCode() + (getConfiguration() == null ? 0 : getConfiguration().hashCode())) + (getOwnerName() == null ? 0 : getOwnerName().hashCode())) + (getOwnerAccess() == null ? 0 : getOwnerAccess().hashCode())) + (getGroupName() == null ? 0 : getGroupName().hashCode())) + (getGroupAccess() == null ? 0 : getGroupAccess().hashCode())) + (getConfiguration() == null ? 0 : getConfiguration().hashCode());
    }

    @java.lang.Override
    public boolean equals(java.lang.Object object) {
        if (object == null) {
            return false;
        } else if (object == this) {
            return true;
        } else if (object.getClass() == org.apache.ambari.server.state.kerberos.KerberosKeytabDescriptor.class) {
            org.apache.ambari.server.state.kerberos.KerberosKeytabDescriptor descriptor = ((org.apache.ambari.server.state.kerberos.KerberosKeytabDescriptor) (object));
            return ((((super.equals(object) && (getConfiguration() == null ? descriptor.getConfiguration() == null : getConfiguration().equals(descriptor.getConfiguration()))) && (getOwnerName() == null ? descriptor.getOwnerName() == null : getOwnerName().equals(descriptor.getOwnerName()))) && (getOwnerAccess() == null ? descriptor.getOwnerAccess() == null : getOwnerAccess().equals(descriptor.getOwnerAccess()))) && (getGroupName() == null ? descriptor.getGroupName() == null : getGroupName().equals(descriptor.getGroupName()))) && (getGroupAccess() == null ? descriptor.getGroupAccess() == null : getGroupAccess().equals(descriptor.getGroupAccess()));
        } else {
            return false;
        }
    }
}