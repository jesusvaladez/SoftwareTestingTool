package org.apache.ambari.server.state.kerberos;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
public class KerberosServiceDescriptor extends org.apache.ambari.server.state.kerberos.AbstractKerberosDescriptorContainer {
    public static final java.lang.String KEY_NAME = "name";

    static final java.lang.String KEY_PRECONFIGURE = "preconfigure";

    static final java.lang.String KEY_COMPONENTS = org.apache.ambari.server.state.kerberos.AbstractKerberosDescriptor.Type.COMPONENT.getDescriptorPluralName();

    private java.util.Map<java.lang.String, org.apache.ambari.server.state.kerberos.KerberosComponentDescriptor> components;

    private java.lang.Boolean preconfigure = null;

    KerberosServiceDescriptor(java.util.Map<?, ?> data) {
        this(org.apache.ambari.server.state.kerberos.AbstractKerberosDescriptor.getStringValue(data, org.apache.ambari.server.state.kerberos.KerberosServiceDescriptor.KEY_NAME), data);
    }

    @java.lang.Override
    public java.util.Collection<? extends org.apache.ambari.server.state.kerberos.AbstractKerberosDescriptorContainer> getChildContainers() {
        return components == null ? null : java.util.Collections.unmodifiableCollection(components.values());
    }

    @java.lang.Override
    public org.apache.ambari.server.state.kerberos.AbstractKerberosDescriptorContainer getChildContainer(java.lang.String name) {
        return getComponent(name);
    }

    KerberosServiceDescriptor(java.lang.String name, java.util.Map<?, ?> data) {
        super(data);
        setName(name);
        if (data != null) {
            java.lang.Object list = data.get(org.apache.ambari.server.state.kerberos.KerberosServiceDescriptor.KEY_COMPONENTS);
            if (list instanceof java.util.Collection) {
                for (java.lang.Object item : ((java.util.Collection) (list))) {
                    if (item instanceof java.util.Map) {
                        putComponent(new org.apache.ambari.server.state.kerberos.KerberosComponentDescriptor(((java.util.Map<?, ?>) (item))));
                    }
                }
            }
            setPreconfigure(org.apache.ambari.server.state.kerberos.AbstractKerberosDescriptor.getBooleanValue(data, org.apache.ambari.server.state.kerberos.KerberosServiceDescriptor.KEY_PRECONFIGURE));
        }
    }

    public java.util.Map<java.lang.String, org.apache.ambari.server.state.kerberos.KerberosComponentDescriptor> getComponents() {
        return components;
    }

    public org.apache.ambari.server.state.kerberos.KerberosComponentDescriptor getComponent(java.lang.String name) {
        return (name == null) || (components == null) ? null : components.get(name);
    }

    public void putComponent(org.apache.ambari.server.state.kerberos.KerberosComponentDescriptor component) {
        if (component != null) {
            java.lang.String name = component.getName();
            if (name == null) {
                throw new java.lang.IllegalArgumentException("The component name must not be null");
            }
            if (components == null) {
                components = new java.util.TreeMap<>();
            }
            components.put(name, component);
            component.setParent(this);
        }
    }

    public void update(org.apache.ambari.server.state.kerberos.KerberosServiceDescriptor updates) {
        if (updates != null) {
            java.util.Map<java.lang.String, org.apache.ambari.server.state.kerberos.KerberosComponentDescriptor> updatedComponents = updates.getComponents();
            if (updatedComponents != null) {
                for (java.util.Map.Entry<java.lang.String, org.apache.ambari.server.state.kerberos.KerberosComponentDescriptor> entry : updatedComponents.entrySet()) {
                    org.apache.ambari.server.state.kerberos.KerberosComponentDescriptor existing = getComponent(entry.getKey());
                    if (existing == null) {
                        putComponent(entry.getValue());
                    } else {
                        existing.update(entry.getValue());
                    }
                }
            }
        }
        super.update(updates);
    }

    public boolean shouldPreconfigure() {
        return java.lang.Boolean.TRUE.equals(preconfigure);
    }

    public void setPreconfigure(java.lang.Boolean preconfigure) {
        this.preconfigure = preconfigure;
    }

    @java.lang.Override
    protected org.apache.ambari.server.state.kerberos.AbstractKerberosDescriptor getDescriptor(org.apache.ambari.server.state.kerberos.AbstractKerberosDescriptor.Type type, java.lang.String name) {
        if (org.apache.ambari.server.state.kerberos.AbstractKerberosDescriptor.Type.COMPONENT == type) {
            return getComponent(name);
        } else {
            return super.getDescriptor(type, name);
        }
    }

    @java.lang.Override
    public java.util.Map<java.lang.String, java.lang.Object> toMap() {
        java.util.Map<java.lang.String, java.lang.Object> map = super.toMap();
        if (components != null) {
            java.util.List<java.util.Map<java.lang.String, java.lang.Object>> list = new java.util.ArrayList<>();
            for (org.apache.ambari.server.state.kerberos.KerberosComponentDescriptor component : components.values()) {
                list.add(component.toMap());
            }
            map.put(org.apache.ambari.server.state.kerberos.KerberosServiceDescriptor.KEY_COMPONENTS, list);
        }
        if (preconfigure != null) {
            map.put(org.apache.ambari.server.state.kerberos.KerberosServiceDescriptor.KEY_PRECONFIGURE, preconfigure.toString());
        }
        return map;
    }

    public java.util.List<org.apache.ambari.server.state.kerberos.KerberosIdentityDescriptor> getComponentIdentities(java.lang.String componentName) {
        return getComponent(componentName) != null ? org.apache.ambari.server.state.kerberos.AbstractKerberosDescriptor.nullToEmpty(getComponent(componentName).getIdentities()) : java.util.Collections.emptyList();
    }

    @java.lang.Override
    public int hashCode() {
        return new org.apache.commons.lang.builder.HashCodeBuilder().appendSuper(super.hashCode()).append(components).append(preconfigure).toHashCode();
    }

    @java.lang.Override
    public boolean equals(java.lang.Object object) {
        if (object == this) {
            return true;
        }
        if (!(object instanceof org.apache.ambari.server.state.kerberos.KerberosServiceDescriptor)) {
            return false;
        }
        org.apache.ambari.server.state.kerberos.KerberosServiceDescriptor that = ((org.apache.ambari.server.state.kerberos.KerberosServiceDescriptor) (object));
        return new org.apache.commons.lang.builder.EqualsBuilder().appendSuper(super.equals(object)).append(components, components).append(preconfigure, that.preconfigure).isEquals();
    }
}