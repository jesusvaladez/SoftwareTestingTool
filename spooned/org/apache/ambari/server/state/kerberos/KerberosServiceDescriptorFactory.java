package org.apache.ambari.server.state.kerberos;
@com.google.inject.Singleton
public class KerberosServiceDescriptorFactory extends org.apache.ambari.server.state.kerberos.AbstractKerberosDescriptorFactory {
    public org.apache.ambari.server.state.kerberos.KerberosServiceDescriptor[] createInstances(java.io.File file) throws java.io.IOException {
        try {
            return createInstances(parseFile(file));
        } catch (org.apache.ambari.server.AmbariException e) {
            throw new org.apache.ambari.server.AmbariException(java.lang.String.format("An error occurred processing the JSON-formatted file: %s", file.getAbsolutePath()), e);
        }
    }

    public org.apache.ambari.server.state.kerberos.KerberosServiceDescriptor[] createInstances(java.lang.String json) throws org.apache.ambari.server.AmbariException {
        try {
            return createInstances(parseJSON(json));
        } catch (org.apache.ambari.server.AmbariException e) {
            throw new org.apache.ambari.server.AmbariException("An error occurred processing the JSON-formatted string", e);
        }
    }

    public org.apache.ambari.server.state.kerberos.KerberosServiceDescriptor[] createInstances(java.util.Map<java.lang.String, java.lang.Object> map) throws org.apache.ambari.server.AmbariException {
        java.util.ArrayList<org.apache.ambari.server.state.kerberos.KerberosServiceDescriptor> descriptors = new java.util.ArrayList<>();
        if (map != null) {
            java.lang.Object servicesData = map.get("services");
            if (servicesData == null) {
                throw new org.apache.ambari.server.AmbariException("Missing top-level \"services\" property in service-level Kerberos descriptor data");
            } else if (servicesData instanceof java.util.Collection) {
                for (java.lang.Object serviceData : ((java.util.Collection) (servicesData))) {
                    if (serviceData instanceof java.util.Map) {
                        descriptors.add(new org.apache.ambari.server.state.kerberos.KerberosServiceDescriptor(((java.util.Map) (serviceData))));
                    }
                }
            } else {
                throw new org.apache.ambari.server.AmbariException(java.lang.String.format("Unexpected top-level \"services\" type in service-level Kerberos descriptor data: %s", servicesData.getClass().getName()));
            }
        }
        return descriptors.toArray(new org.apache.ambari.server.state.kerberos.KerberosServiceDescriptor[descriptors.size()]);
    }

    public org.apache.ambari.server.state.kerberos.KerberosServiceDescriptor createInstance(java.io.File file, java.lang.String name) throws java.io.IOException {
        try {
            return createInstance(parseFile(file), name);
        } catch (org.apache.ambari.server.AmbariException e) {
            throw new org.apache.ambari.server.AmbariException(java.lang.String.format("An error occurred processing the JSON-formatted file: %s", file.getAbsolutePath()), e);
        }
    }

    public org.apache.ambari.server.state.kerberos.KerberosServiceDescriptor createInstance(java.util.Map<java.lang.String, java.lang.Object> map, java.lang.String name) throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.server.state.kerberos.KerberosServiceDescriptor descriptor = null;
        if ((map != null) && (name != null)) {
            java.lang.Object servicesData = map.get("services");
            if (servicesData == null) {
                throw new org.apache.ambari.server.AmbariException("Missing top-level \"services\" property in service-level Kerberos descriptor data");
            } else if (servicesData instanceof java.util.Collection) {
                for (java.lang.Object serviceData : ((java.util.Collection) (servicesData))) {
                    if (serviceData instanceof java.util.Map) {
                        java.util.Map<?, ?> serviceDataMap = ((java.util.Map<?, ?>) (serviceData));
                        if (name.equalsIgnoreCase(((java.lang.String) (serviceDataMap.get("name"))))) {
                            descriptor = new org.apache.ambari.server.state.kerberos.KerberosServiceDescriptor(serviceDataMap);
                            break;
                        }
                    }
                }
            } else {
                throw new org.apache.ambari.server.AmbariException(java.lang.String.format("Unexpected top-level \"services\" type in service-level Kerberos descriptor data: %s", servicesData.getClass().getName()));
            }
        }
        return descriptor;
    }

    public org.apache.ambari.server.state.kerberos.KerberosServiceDescriptor createInstance(java.lang.String name, java.lang.String json) throws org.apache.ambari.server.AmbariException {
        return new org.apache.ambari.server.state.kerberos.KerberosServiceDescriptor(name, parseJSON(json));
    }

    public org.apache.ambari.server.state.kerberos.KerberosServiceDescriptor createInstance(java.lang.String name, java.util.Map<?, ?> map) throws org.apache.ambari.server.AmbariException {
        return new org.apache.ambari.server.state.kerberos.KerberosServiceDescriptor(name, map);
    }
}