package org.apache.ambari.server.state.kerberos;
import org.apache.commons.lang.StringUtils;
public class KerberosDescriptor extends org.apache.ambari.server.state.kerberos.AbstractKerberosDescriptorContainer {
    public static final java.lang.String KEY_PROPERTIES = "properties";

    public static final java.lang.String KEY_SERVICES = org.apache.ambari.server.state.kerberos.AbstractKerberosDescriptor.Type.SERVICE.getDescriptorPluralName();

    private java.util.Map<java.lang.String, java.lang.String> properties = null;

    private java.util.Map<java.lang.String, org.apache.ambari.server.state.kerberos.KerberosServiceDescriptor> services = null;

    public KerberosDescriptor() {
        this(null);
    }

    KerberosDescriptor(java.util.Map<?, ?> data) {
        super(data);
        if (data != null) {
            java.lang.Object list = data.get(org.apache.ambari.server.state.kerberos.KerberosDescriptor.KEY_SERVICES);
            if (list instanceof java.util.Collection) {
                for (java.lang.Object item : ((java.util.Collection) (list))) {
                    if (item instanceof java.util.Map) {
                        putService(new org.apache.ambari.server.state.kerberos.KerberosServiceDescriptor(((java.util.Map<?, ?>) (item))));
                    }
                }
            }
            java.lang.Object map = data.get(org.apache.ambari.server.state.kerberos.KerberosDescriptor.KEY_PROPERTIES);
            if (map instanceof java.util.Map) {
                for (java.util.Map.Entry<?, ?> entry : ((java.util.Map<?, ?>) (map)).entrySet()) {
                    java.lang.Object value = entry.getValue();
                    putProperty(entry.getKey().toString(), value == null ? null : value.toString());
                }
            }
        }
    }

    @java.lang.Override
    public java.util.Collection<? extends org.apache.ambari.server.state.kerberos.AbstractKerberosDescriptorContainer> getChildContainers() {
        return services == null ? null : java.util.Collections.unmodifiableCollection(services.values());
    }

    @java.lang.Override
    public org.apache.ambari.server.state.kerberos.AbstractKerberosDescriptorContainer getChildContainer(java.lang.String name) {
        return getService(name);
    }

    public void setServices(java.util.Map<java.lang.String, org.apache.ambari.server.state.kerberos.KerberosServiceDescriptor> services) {
        this.services = (services == null) ? null : new java.util.TreeMap<>(services);
    }

    public java.util.Map<java.lang.String, org.apache.ambari.server.state.kerberos.KerberosServiceDescriptor> getServices() {
        return services;
    }

    public org.apache.ambari.server.state.kerberos.KerberosServiceDescriptor getService(java.lang.String name) {
        return (name == null) || (services == null) ? null : services.get(name);
    }

    public void putService(org.apache.ambari.server.state.kerberos.KerberosServiceDescriptor service) {
        if (service != null) {
            java.lang.String name = service.getName();
            if (name == null) {
                throw new java.lang.IllegalArgumentException("The service name must not be null");
            }
            if (services == null) {
                services = new java.util.TreeMap<java.lang.String, org.apache.ambari.server.state.kerberos.KerberosServiceDescriptor>();
            }
            org.apache.ambari.server.state.kerberos.KerberosServiceDescriptor existing = services.get(name);
            if (existing == null) {
                services.put(name, service);
                service.setParent(this);
            } else {
                existing.update(service);
            }
        }
    }

    public void setProperties(java.util.Map<java.lang.String, java.lang.String> properties) {
        this.properties = (properties == null) ? null : new java.util.TreeMap<>(properties);
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

    public org.apache.ambari.server.state.kerberos.KerberosDescriptor update(org.apache.ambari.server.state.kerberos.KerberosDescriptor updates) {
        if (updates != null) {
            java.util.Map<java.lang.String, org.apache.ambari.server.state.kerberos.KerberosServiceDescriptor> updatedServiceDescriptors = updates.getServices();
            if (updatedServiceDescriptors != null) {
                for (java.util.Map.Entry<java.lang.String, org.apache.ambari.server.state.kerberos.KerberosServiceDescriptor> entry : updatedServiceDescriptors.entrySet()) {
                    putService(entry.getValue());
                }
            }
            java.util.Map<java.lang.String, java.lang.String> updatedProperties = updates.getProperties();
            if (updatedProperties != null) {
                for (java.util.Map.Entry<java.lang.String, java.lang.String> entry : updatedProperties.entrySet()) {
                    putProperty(entry.getKey(), entry.getValue());
                }
            }
        }
        super.update(updates);
        return this;
    }

    @java.lang.Override
    protected org.apache.ambari.server.state.kerberos.AbstractKerberosDescriptor getDescriptor(org.apache.ambari.server.state.kerberos.AbstractKerberosDescriptor.Type type, java.lang.String name) {
        if (org.apache.ambari.server.state.kerberos.AbstractKerberosDescriptor.Type.SERVICE == type) {
            return getService(name);
        } else {
            return super.getDescriptor(type, name);
        }
    }

    @java.lang.Override
    public java.util.Map<java.lang.String, java.lang.Object> toMap() {
        java.util.Map<java.lang.String, java.lang.Object> map = super.toMap();
        if (services != null) {
            java.util.List<java.util.Map<java.lang.String, java.lang.Object>> list = new java.util.ArrayList<>();
            for (org.apache.ambari.server.state.kerberos.KerberosServiceDescriptor service : services.values()) {
                list.add(service.toMap());
            }
            map.put(org.apache.ambari.server.state.kerberos.KerberosDescriptor.KEY_SERVICES, list);
        }
        if (properties != null) {
            map.put(org.apache.ambari.server.state.kerberos.KerberosDescriptor.KEY_PROPERTIES, new java.util.TreeMap<>(properties));
        }
        return map;
    }

    @java.lang.Override
    public void setParent(org.apache.ambari.server.state.kerberos.AbstractKerberosDescriptor parent) {
        throw new java.lang.UnsupportedOperationException("This KerberosDescriptor may not have a parent assigned to it.");
    }

    @java.lang.Override
    public int hashCode() {
        return (super.hashCode() + (getProperties() == null ? 0 : getProperties().hashCode())) + (getServices() == null ? 0 : getServices().hashCode());
    }

    @java.lang.Override
    public boolean equals(java.lang.Object object) {
        if (object == null) {
            return false;
        } else if (object == this) {
            return true;
        } else if (object.getClass() == org.apache.ambari.server.state.kerberos.KerberosDescriptor.class) {
            org.apache.ambari.server.state.kerberos.KerberosDescriptor descriptor = ((org.apache.ambari.server.state.kerberos.KerberosDescriptor) (object));
            return (super.equals(object) && (getProperties() == null ? descriptor.getProperties() == null : getProperties().equals(descriptor.getProperties()))) && (getServices() == null ? descriptor.getServices() == null : getServices().equals(descriptor.getServices()));
        } else {
            return false;
        }
    }

    public java.util.Set<java.lang.String> getAllAuthToLocalProperties() {
        java.util.Set<java.lang.String> authToLocalProperties = new java.util.HashSet<>();
        java.util.Set<java.lang.String> set;
        set = getAuthToLocalProperties();
        if (set != null) {
            authToLocalProperties.addAll(set);
        }
        if (services != null) {
            for (org.apache.ambari.server.state.kerberos.KerberosServiceDescriptor service : services.values()) {
                java.util.Map<java.lang.String, org.apache.ambari.server.state.kerberos.KerberosComponentDescriptor> components = service.getComponents();
                if (components != null) {
                    for (org.apache.ambari.server.state.kerberos.KerberosComponentDescriptor component : components.values()) {
                        set = component.getAuthToLocalProperties();
                        if (set != null) {
                            authToLocalProperties.addAll(set);
                        }
                    }
                }
                set = service.getAuthToLocalProperties();
                if (set != null) {
                    authToLocalProperties.addAll(set);
                }
            }
        }
        return authToLocalProperties;
    }

    public java.util.Map<java.lang.String, java.lang.String> principals() throws org.apache.ambari.server.AmbariException {
        java.util.Map<java.lang.String, java.lang.String> result = new java.util.HashMap<>();
        for (org.apache.ambari.server.state.kerberos.AbstractKerberosDescriptorContainer each : org.apache.ambari.server.state.kerberos.AbstractKerberosDescriptor.nullToEmpty(getChildContainers())) {
            if (each instanceof org.apache.ambari.server.state.kerberos.KerberosServiceDescriptor) {
                org.apache.ambari.server.state.kerberos.KerberosDescriptor.collectFromComponents(each.getName(), org.apache.ambari.server.state.kerberos.AbstractKerberosDescriptor.nullToEmpty(((org.apache.ambari.server.state.kerberos.KerberosServiceDescriptor) (each)).getComponents()).values(), result);
                org.apache.ambari.server.state.kerberos.KerberosDescriptor.collectFromIdentities(each.getName(), "", org.apache.ambari.server.state.kerberos.AbstractKerberosDescriptor.nullToEmpty(each.getIdentities()), result);
            }
        }
        return result;
    }

    private static void collectFromComponents(java.lang.String service, java.util.Collection<org.apache.ambari.server.state.kerberos.KerberosComponentDescriptor> components, java.util.Map<java.lang.String, java.lang.String> result) {
        for (org.apache.ambari.server.state.kerberos.KerberosComponentDescriptor each : components) {
            org.apache.ambari.server.state.kerberos.KerberosDescriptor.collectFromIdentities(service, each.getName(), org.apache.ambari.server.state.kerberos.AbstractKerberosDescriptor.nullToEmpty(each.getIdentities()), result);
        }
    }

    private static void collectFromIdentities(java.lang.String service, java.lang.String component, java.util.Collection<org.apache.ambari.server.state.kerberos.KerberosIdentityDescriptor> identities, java.util.Map<java.lang.String, java.lang.String> result) {
        for (org.apache.ambari.server.state.kerberos.KerberosIdentityDescriptor each : identities) {
            if (((each.getPrincipalDescriptor() != null) && (!each.getReferencedServiceName().isPresent())) && (!each.getName().startsWith("/"))) {
                java.lang.String path = (org.apache.commons.lang.StringUtils.isBlank(component)) ? java.lang.String.format("%s/%s", service, each.getName()) : java.lang.String.format("%s/%s/%s", service, component, each.getName());
                result.put(path, each.getPrincipalDescriptor().getName());
            }
        }
    }
}