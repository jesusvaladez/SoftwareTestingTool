package org.apache.ambari.server.state.kerberos;
public class KerberosDescriptorUpdateHelper {
    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.state.kerberos.KerberosDescriptorUpdateHelper.class);

    public static org.apache.ambari.server.state.kerberos.KerberosDescriptor updateUserKerberosDescriptor(org.apache.ambari.server.state.kerberos.KerberosDescriptor beginningStackKerberosDescriptor, org.apache.ambari.server.state.kerberos.KerberosDescriptor endingStackKerberosDescriptor, org.apache.ambari.server.state.kerberos.KerberosDescriptor userKerberosDescriptor) {
        org.apache.ambari.server.state.kerberos.KerberosDescriptor updated = new org.apache.ambari.server.state.kerberos.KerberosDescriptor(userKerberosDescriptor.toMap());
        updated.setProperties(org.apache.ambari.server.state.kerberos.KerberosDescriptorUpdateHelper.processProperties(beginningStackKerberosDescriptor.getProperties(), endingStackKerberosDescriptor.getProperties(), updated.getProperties()));
        updated.setConfigurations(org.apache.ambari.server.state.kerberos.KerberosDescriptorUpdateHelper.processConfigurations(beginningStackKerberosDescriptor.getConfigurations(), endingStackKerberosDescriptor.getConfigurations(), updated.getConfigurations()));
        updated.setIdentities(org.apache.ambari.server.state.kerberos.KerberosDescriptorUpdateHelper.processIdentities(beginningStackKerberosDescriptor.getIdentities(), endingStackKerberosDescriptor.getIdentities(), updated.getIdentities()));
        updated.setAuthToLocalProperties(org.apache.ambari.server.state.kerberos.KerberosDescriptorUpdateHelper.processAuthToLocalProperties(beginningStackKerberosDescriptor.getAuthToLocalProperties(), endingStackKerberosDescriptor.getAuthToLocalProperties(), updated.getAuthToLocalProperties()));
        updated.setServices(org.apache.ambari.server.state.kerberos.KerberosDescriptorUpdateHelper.processServices(beginningStackKerberosDescriptor.getServices(), endingStackKerberosDescriptor.getServices(), updated.getServices()));
        return updated;
    }

    private static java.util.Map<java.lang.String, org.apache.ambari.server.state.kerberos.KerberosServiceDescriptor> processServices(java.util.Map<java.lang.String, org.apache.ambari.server.state.kerberos.KerberosServiceDescriptor> previousStackServices, java.util.Map<java.lang.String, org.apache.ambari.server.state.kerberos.KerberosServiceDescriptor> newStackServices, java.util.Map<java.lang.String, org.apache.ambari.server.state.kerberos.KerberosServiceDescriptor> userServices) {
        if (((userServices == null) || userServices.isEmpty()) || ((previousStackServices == null) && (newStackServices == null))) {
            return userServices;
        }
        java.util.Map<java.lang.String, org.apache.ambari.server.state.kerberos.KerberosServiceDescriptor> updatedServices = new java.util.TreeMap<>();
        if (previousStackServices == null) {
            previousStackServices = java.util.Collections.emptyMap();
        }
        if (newStackServices == null) {
            newStackServices = java.util.Collections.emptyMap();
        }
        for (java.util.Map.Entry<java.lang.String, org.apache.ambari.server.state.kerberos.KerberosServiceDescriptor> entry : userServices.entrySet()) {
            java.lang.String name = entry.getKey();
            org.apache.ambari.server.state.kerberos.KerberosServiceDescriptor userValue = entry.getValue();
            if (userValue != null) {
                if (newStackServices.containsKey(name)) {
                    org.apache.ambari.server.state.kerberos.KerberosServiceDescriptor oldValue = previousStackServices.get(name);
                    org.apache.ambari.server.state.kerberos.KerberosServiceDescriptor newValue = newStackServices.get(name);
                    org.apache.ambari.server.state.kerberos.KerberosDescriptorUpdateHelper.LOG.debug("Processing service {} for modifications", name);
                    updatedServices.put(name, org.apache.ambari.server.state.kerberos.KerberosDescriptorUpdateHelper.processService(oldValue, newValue, userValue));
                } else if (previousStackServices.containsKey(name)) {
                    org.apache.ambari.server.state.kerberos.KerberosDescriptorUpdateHelper.LOG.debug("Removing service {} from user-specified Kerberos Descriptor", name);
                } else {
                    org.apache.ambari.server.state.kerberos.KerberosDescriptorUpdateHelper.LOG.debug("Leaving service {} in user-specified Kerberos Descriptor unchanged since it was user-defined.", name);
                    updatedServices.put(name, userValue);
                }
            }
        }
        return updatedServices;
    }

    private static org.apache.ambari.server.state.kerberos.KerberosServiceDescriptor processService(org.apache.ambari.server.state.kerberos.KerberosServiceDescriptor previousStackService, org.apache.ambari.server.state.kerberos.KerberosServiceDescriptor newStackService, org.apache.ambari.server.state.kerberos.KerberosServiceDescriptor userService) {
        org.apache.ambari.server.state.kerberos.KerberosServiceDescriptor updatedService = new org.apache.ambari.server.state.kerberos.KerberosServiceDescriptor(userService.toMap());
        updatedService.setAuthToLocalProperties(org.apache.ambari.server.state.kerberos.KerberosDescriptorUpdateHelper.processAuthToLocalProperties(previousStackService == null ? null : previousStackService.getAuthToLocalProperties(), newStackService == null ? null : newStackService.getAuthToLocalProperties(), updatedService.getAuthToLocalProperties()));
        updatedService.setConfigurations(org.apache.ambari.server.state.kerberos.KerberosDescriptorUpdateHelper.processConfigurations(previousStackService == null ? null : previousStackService.getConfigurations(), newStackService == null ? null : newStackService.getConfigurations(), updatedService.getConfigurations()));
        updatedService.setIdentities(org.apache.ambari.server.state.kerberos.KerberosDescriptorUpdateHelper.processIdentities(previousStackService == null ? null : previousStackService.getIdentities(), newStackService == null ? null : newStackService.getIdentities(), updatedService.getIdentities()));
        java.util.Map<java.lang.String, org.apache.ambari.server.state.kerberos.KerberosComponentDescriptor> userServiceComponents = updatedService.getComponents();
        java.util.Map<java.lang.String, org.apache.ambari.server.state.kerberos.KerberosComponentDescriptor> newServiceComponents = (newStackService == null) ? null : newStackService.getComponents();
        java.util.Map<java.lang.String, org.apache.ambari.server.state.kerberos.KerberosComponentDescriptor> oldServiceComponents = (previousStackService == null) ? null : previousStackService.getComponents();
        if (newServiceComponents == null) {
            newServiceComponents = java.util.Collections.emptyMap();
        }
        if (oldServiceComponents == null) {
            oldServiceComponents = java.util.Collections.emptyMap();
        }
        if (userServiceComponents != null) {
            java.util.Iterator<java.util.Map.Entry<java.lang.String, org.apache.ambari.server.state.kerberos.KerberosComponentDescriptor>> iterator = userServiceComponents.entrySet().iterator();
            while (iterator.hasNext()) {
                java.util.Map.Entry<java.lang.String, org.apache.ambari.server.state.kerberos.KerberosComponentDescriptor> entry = iterator.next();
                java.lang.String name = entry.getKey();
                org.apache.ambari.server.state.kerberos.KerberosComponentDescriptor userValue = entry.getValue();
                if (userValue == null) {
                    iterator.remove();
                } else if (newServiceComponents.containsKey(name)) {
                    org.apache.ambari.server.state.kerberos.KerberosComponentDescriptor oldValue = oldServiceComponents.get(name);
                    org.apache.ambari.server.state.kerberos.KerberosComponentDescriptor newValue = newServiceComponents.get(name);
                    org.apache.ambari.server.state.kerberos.KerberosDescriptorUpdateHelper.LOG.debug("Processing component {}/{} for modifications", updatedService.getName(), name);
                    org.apache.ambari.server.state.kerberos.KerberosDescriptorUpdateHelper.processComponent(oldValue, newValue, userValue);
                } else {
                    org.apache.ambari.server.state.kerberos.KerberosDescriptorUpdateHelper.LOG.debug("Removing component {}/{} from user-specified Kerberos Descriptor", updatedService.getName(), name);
                    iterator.remove();
                }
            } 
        }
        return updatedService;
    }

    private static org.apache.ambari.server.state.kerberos.KerberosComponentDescriptor processComponent(org.apache.ambari.server.state.kerberos.KerberosComponentDescriptor previousStackComponent, org.apache.ambari.server.state.kerberos.KerberosComponentDescriptor newStackComponent, org.apache.ambari.server.state.kerberos.KerberosComponentDescriptor userComponent) {
        userComponent.setAuthToLocalProperties(org.apache.ambari.server.state.kerberos.KerberosDescriptorUpdateHelper.processAuthToLocalProperties(previousStackComponent == null ? null : previousStackComponent.getAuthToLocalProperties(), newStackComponent == null ? null : newStackComponent.getAuthToLocalProperties(), userComponent.getAuthToLocalProperties()));
        userComponent.setConfigurations(org.apache.ambari.server.state.kerberos.KerberosDescriptorUpdateHelper.processConfigurations(previousStackComponent == null ? null : previousStackComponent.getConfigurations(), newStackComponent == null ? null : newStackComponent.getConfigurations(), userComponent.getConfigurations()));
        userComponent.setIdentities(org.apache.ambari.server.state.kerberos.KerberosDescriptorUpdateHelper.processIdentities(previousStackComponent == null ? null : previousStackComponent.getIdentities(), newStackComponent == null ? null : newStackComponent.getIdentities(), userComponent.getIdentities()));
        return userComponent;
    }

    private static java.util.Set<java.lang.String> processAuthToLocalProperties(java.util.Set<java.lang.String> previousStackAuthToLocalProperties, java.util.Set<java.lang.String> newStackAuthToLocalProperties, java.util.Set<java.lang.String> userAuthToLocalProperties) {
        if (userAuthToLocalProperties == null) {
            return null;
        }
        java.util.TreeSet<java.lang.String> updatedAuthToLocalProperties = new java.util.TreeSet<>(userAuthToLocalProperties);
        if (previousStackAuthToLocalProperties != null) {
            updatedAuthToLocalProperties.removeAll(previousStackAuthToLocalProperties);
        }
        if (newStackAuthToLocalProperties != null) {
            updatedAuthToLocalProperties.addAll(newStackAuthToLocalProperties);
        }
        return updatedAuthToLocalProperties;
    }

    private static java.util.List<org.apache.ambari.server.state.kerberos.KerberosIdentityDescriptor> processIdentities(java.util.List<org.apache.ambari.server.state.kerberos.KerberosIdentityDescriptor> previousStackIdentities, java.util.List<org.apache.ambari.server.state.kerberos.KerberosIdentityDescriptor> newStackIdentities, java.util.List<org.apache.ambari.server.state.kerberos.KerberosIdentityDescriptor> userIdentities) {
        if (((userIdentities == null) || userIdentities.isEmpty()) || ((previousStackIdentities == null) && (newStackIdentities == null))) {
            return userIdentities;
        }
        java.util.Map<java.lang.String, org.apache.ambari.server.state.kerberos.KerberosIdentityDescriptor> previousStackIdentityMap = org.apache.ambari.server.state.kerberos.KerberosDescriptorUpdateHelper.toMap(previousStackIdentities);
        java.util.Map<java.lang.String, org.apache.ambari.server.state.kerberos.KerberosIdentityDescriptor> newStackIdentityMap = org.apache.ambari.server.state.kerberos.KerberosDescriptorUpdateHelper.toMap(newStackIdentities);
        java.util.Map<java.lang.String, org.apache.ambari.server.state.kerberos.KerberosIdentityDescriptor> userStackIdentityMap = org.apache.ambari.server.state.kerberos.KerberosDescriptorUpdateHelper.toMap(userIdentities);
        java.util.Map<java.lang.String, org.apache.ambari.server.state.kerberos.KerberosIdentityDescriptor> updatedIdentities = new java.util.TreeMap<>();
        if (previousStackIdentityMap == null) {
            previousStackIdentityMap = java.util.Collections.emptyMap();
        }
        if (newStackIdentityMap == null) {
            newStackIdentityMap = java.util.Collections.emptyMap();
        }
        for (java.util.Map.Entry<java.lang.String, org.apache.ambari.server.state.kerberos.KerberosIdentityDescriptor> entry : userStackIdentityMap.entrySet()) {
            java.lang.String name = entry.getKey();
            org.apache.ambari.server.state.kerberos.KerberosIdentityDescriptor userValue = entry.getValue();
            if (userValue != null) {
                if (newStackIdentityMap.containsKey(name)) {
                    org.apache.ambari.server.state.kerberos.KerberosIdentityDescriptor newValue = newStackIdentityMap.get(name);
                    org.apache.ambari.server.state.kerberos.KerberosIdentityDescriptor previousValue = previousStackIdentityMap.get(name);
                    updatedIdentities.put(name, org.apache.ambari.server.state.kerberos.KerberosDescriptorUpdateHelper.processIdentity(previousValue, newValue, userValue));
                } else if (previousStackIdentityMap.containsKey(name)) {
                    org.apache.ambari.server.state.kerberos.KerberosDescriptorUpdateHelper.LOG.debug("Removing identity named {} from user-specified Kerberos Descriptor", name);
                } else {
                    org.apache.ambari.server.state.kerberos.KerberosDescriptorUpdateHelper.LOG.debug("Leaving identity named {} in user-specified Kerberos Descriptor unchanged since it was user-defined.", name);
                    updatedIdentities.put(name, userValue);
                }
            }
        }
        return new java.util.ArrayList<>(updatedIdentities.values());
    }

    private static org.apache.ambari.server.state.kerberos.KerberosIdentityDescriptor processIdentity(org.apache.ambari.server.state.kerberos.KerberosIdentityDescriptor previousStackIdentity, org.apache.ambari.server.state.kerberos.KerberosIdentityDescriptor newStackIdentity, org.apache.ambari.server.state.kerberos.KerberosIdentityDescriptor userIdentity) {
        org.apache.ambari.server.state.kerberos.KerberosIdentityDescriptor updatedValue = new org.apache.ambari.server.state.kerberos.KerberosIdentityDescriptor(newStackIdentity.toMap());
        org.apache.ambari.server.state.kerberos.KerberosPrincipalDescriptor updatedValuePrincipal = updatedValue.getPrincipalDescriptor();
        org.apache.ambari.server.state.kerberos.KerberosKeytabDescriptor updatedValueKeytab = updatedValue.getKeytabDescriptor();
        if (updatedValuePrincipal != null) {
            org.apache.ambari.server.state.kerberos.KerberosPrincipalDescriptor oldValuePrincipal = (previousStackIdentity == null) ? null : previousStackIdentity.getPrincipalDescriptor();
            java.lang.String previousValuePrincipalValue = null;
            org.apache.ambari.server.state.kerberos.KerberosPrincipalDescriptor userValuePrincipal = userIdentity.getPrincipalDescriptor();
            java.lang.String userValuePrincipalValue = null;
            if (oldValuePrincipal != null) {
                previousValuePrincipalValue = oldValuePrincipal.getValue();
            }
            if (userValuePrincipal != null) {
                userValuePrincipalValue = userValuePrincipal.getValue();
            }
            if ((userValuePrincipalValue != null) && (!userValuePrincipalValue.equals(previousValuePrincipalValue))) {
                updatedValuePrincipal.setValue(userValuePrincipalValue);
            }
        }
        if (updatedValueKeytab != null) {
            org.apache.ambari.server.state.kerberos.KerberosKeytabDescriptor oldValueKeytab = (previousStackIdentity == null) ? null : previousStackIdentity.getKeytabDescriptor();
            java.lang.String previousValueKeytabFile = null;
            org.apache.ambari.server.state.kerberos.KerberosKeytabDescriptor userValueKeytab = userIdentity.getKeytabDescriptor();
            java.lang.String userValueKeytabFile = null;
            if (oldValueKeytab != null) {
                previousValueKeytabFile = oldValueKeytab.getFile();
            }
            if (userValueKeytab != null) {
                userValueKeytabFile = userValueKeytab.getFile();
            }
            if ((userValueKeytabFile != null) && (!userValueKeytabFile.equals(previousValueKeytabFile))) {
                updatedValueKeytab.setFile(userValueKeytabFile);
            }
        }
        updatedValue.setWhen(null);
        return updatedValue;
    }

    private static java.util.Map<java.lang.String, org.apache.ambari.server.state.kerberos.KerberosConfigurationDescriptor> processConfigurations(java.util.Map<java.lang.String, org.apache.ambari.server.state.kerberos.KerberosConfigurationDescriptor> previousStackConfigurations, java.util.Map<java.lang.String, org.apache.ambari.server.state.kerberos.KerberosConfigurationDescriptor> newStackConfigurations, java.util.Map<java.lang.String, org.apache.ambari.server.state.kerberos.KerberosConfigurationDescriptor> userConfigurations) {
        if ((userConfigurations == null) || ((previousStackConfigurations == null) && (newStackConfigurations == null))) {
            return userConfigurations;
        }
        java.util.Map<java.lang.String, org.apache.ambari.server.state.kerberos.KerberosConfigurationDescriptor> updatedConfigurations = new java.util.TreeMap<>();
        if (previousStackConfigurations == null) {
            previousStackConfigurations = java.util.Collections.emptyMap();
        }
        if (newStackConfigurations == null) {
            newStackConfigurations = java.util.Collections.emptyMap();
        }
        for (java.util.Map.Entry<java.lang.String, org.apache.ambari.server.state.kerberos.KerberosConfigurationDescriptor> entry : userConfigurations.entrySet()) {
            java.lang.String name = entry.getKey();
            org.apache.ambari.server.state.kerberos.KerberosConfigurationDescriptor userValue = entry.getValue();
            if (userValue != null) {
                if (newStackConfigurations.containsKey(name)) {
                    org.apache.ambari.server.state.kerberos.KerberosConfigurationDescriptor oldValue = previousStackConfigurations.get(name);
                    org.apache.ambari.server.state.kerberos.KerberosConfigurationDescriptor newValue = newStackConfigurations.get(name);
                    org.apache.ambari.server.state.kerberos.KerberosDescriptorUpdateHelper.LOG.debug("Processing configuration type {} for modifications", name);
                    updatedConfigurations.put(name, org.apache.ambari.server.state.kerberos.KerberosDescriptorUpdateHelper.processConfiguration(oldValue, newValue, userValue));
                } else if (previousStackConfigurations.containsKey(name)) {
                    org.apache.ambari.server.state.kerberos.KerberosDescriptorUpdateHelper.LOG.debug("Removing configuration type {} from user-specified Kerberos Descriptor", name);
                } else {
                    org.apache.ambari.server.state.kerberos.KerberosDescriptorUpdateHelper.LOG.debug("Leaving configuration type {} in user-specified Kerberos Descriptor unchanged since it was user-defined.", name);
                    updatedConfigurations.put(name, userValue);
                }
            }
        }
        return updatedConfigurations;
    }

    private static org.apache.ambari.server.state.kerberos.KerberosConfigurationDescriptor processConfiguration(org.apache.ambari.server.state.kerberos.KerberosConfigurationDescriptor previousStackConfiguration, org.apache.ambari.server.state.kerberos.KerberosConfigurationDescriptor newStackConfiguration, org.apache.ambari.server.state.kerberos.KerberosConfigurationDescriptor userConfiguration) {
        org.apache.ambari.server.state.kerberos.KerberosConfigurationDescriptor updatedValue = new org.apache.ambari.server.state.kerberos.KerberosConfigurationDescriptor(userConfiguration == null ? null : userConfiguration.toMap());
        java.util.Map<java.lang.String, java.lang.String> previousValue = (previousStackConfiguration == null) ? null : previousStackConfiguration.getProperties();
        java.util.Map<java.lang.String, java.lang.String> newValue = (newStackConfiguration == null) ? null : newStackConfiguration.getProperties();
        java.util.Map<java.lang.String, java.lang.String> userValue = updatedValue.getProperties();
        updatedValue.setProperties(org.apache.ambari.server.state.kerberos.KerberosDescriptorUpdateHelper.processProperties(previousValue, newValue, userValue));
        return updatedValue;
    }

    private static java.util.Map<java.lang.String, java.lang.String> processProperties(java.util.Map<java.lang.String, java.lang.String> previousStackProperties, java.util.Map<java.lang.String, java.lang.String> newStackProperties, java.util.Map<java.lang.String, java.lang.String> userProperties) {
        if ((previousStackProperties == null) && (newStackProperties == null)) {
            return userProperties;
        } else {
            java.util.Map<java.lang.String, java.lang.String> updatedProperties = new java.util.TreeMap<>();
            if (userProperties != null) {
                updatedProperties.putAll(userProperties);
            }
            if (previousStackProperties == null) {
                previousStackProperties = java.util.Collections.emptyMap();
            }
            if (newStackProperties == null) {
                newStackProperties = java.util.Collections.emptyMap();
            }
            for (java.util.Map.Entry<java.lang.String, java.lang.String> entry : previousStackProperties.entrySet()) {
                java.lang.String name = entry.getKey();
                if (newStackProperties.containsKey(name)) {
                    java.lang.String previousValue = entry.getValue();
                    java.lang.String newValue = newStackProperties.get(name);
                    java.lang.String userValue = updatedProperties.get(name);
                    if ((previousValue == null ? newValue != null : !previousValue.equals(newValue)) && (previousValue == null ? userValue == null : previousValue.equals(userValue))) {
                        org.apache.ambari.server.state.kerberos.KerberosDescriptorUpdateHelper.LOG.debug("Modifying property named {} from user-specified Kerberos Descriptor", name);
                        updatedProperties.put(name, newValue);
                    }
                } else {
                    org.apache.ambari.server.state.kerberos.KerberosDescriptorUpdateHelper.LOG.debug("Removing property named {} from user-specified Kerberos Descriptor", name);
                    updatedProperties.remove(name);
                }
            }
            for (java.util.Map.Entry<java.lang.String, java.lang.String> entry : newStackProperties.entrySet()) {
                java.lang.String name = entry.getKey();
                if ((!previousStackProperties.containsKey(name)) && (!updatedProperties.containsKey(name))) {
                    org.apache.ambari.server.state.kerberos.KerberosDescriptorUpdateHelper.LOG.debug("Adding property named {} to user-specified Kerberos Descriptor", name);
                    updatedProperties.put(name, entry.getValue());
                }
            }
            return updatedProperties;
        }
    }

    private static java.util.Map<java.lang.String, org.apache.ambari.server.state.kerberos.KerberosIdentityDescriptor> toMap(java.util.List<org.apache.ambari.server.state.kerberos.KerberosIdentityDescriptor> identities) {
        if (identities == null) {
            return null;
        } else {
            java.util.Map<java.lang.String, org.apache.ambari.server.state.kerberos.KerberosIdentityDescriptor> map = new java.util.TreeMap<>();
            for (org.apache.ambari.server.state.kerberos.KerberosIdentityDescriptor identity : identities) {
                map.put(identity.getName(), identity);
            }
            return map;
        }
    }
}