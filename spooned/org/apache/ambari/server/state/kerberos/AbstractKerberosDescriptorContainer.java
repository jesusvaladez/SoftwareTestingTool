package org.apache.ambari.server.state.kerberos;
public abstract class AbstractKerberosDescriptorContainer extends org.apache.ambari.server.state.kerberos.AbstractKerberosDescriptor {
    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.state.kerberos.AbstractKerberosDescriptorContainer.class);

    static final java.lang.String KEY_IDENTITIES = org.apache.ambari.server.state.kerberos.AbstractKerberosDescriptor.Type.IDENTITY.getDescriptorPluralName();

    static final java.lang.String KEY_CONFIGURATIONS = org.apache.ambari.server.state.kerberos.AbstractKerberosDescriptor.Type.CONFIGURATION.getDescriptorPluralName();

    static final java.lang.String KEY_AUTH_TO_LOCAL_PROPERTIES = org.apache.ambari.server.state.kerberos.AbstractKerberosDescriptor.Type.AUTH_TO_LOCAL_PROPERTY.getDescriptorPluralName();

    public static final java.util.regex.Pattern AUTH_TO_LOCAL_PROPERTY_SPECIFICATION_PATTERN = java.util.regex.Pattern.compile("^(?:(.+?)/)?(.+?)(?:\\|(.+?))?$");

    private java.util.List<org.apache.ambari.server.state.kerberos.KerberosIdentityDescriptor> identities = null;

    private java.util.Map<java.lang.String, org.apache.ambari.server.state.kerberos.KerberosConfigurationDescriptor> configurations = null;

    private java.util.Set<java.lang.String> authToLocalProperties = null;

    protected AbstractKerberosDescriptorContainer(java.util.Map<?, ?> data) {
        if (data != null) {
            java.lang.Object list;
            list = data.get(org.apache.ambari.server.state.kerberos.AbstractKerberosDescriptorContainer.KEY_IDENTITIES);
            if (list instanceof java.util.Collection) {
                for (java.lang.Object item : ((java.util.Collection) (list))) {
                    if (item instanceof java.util.Map) {
                        putIdentity(new org.apache.ambari.server.state.kerberos.KerberosIdentityDescriptor(((java.util.Map<?, ?>) (item))));
                    }
                }
            }
            list = data.get(org.apache.ambari.server.state.kerberos.AbstractKerberosDescriptorContainer.KEY_CONFIGURATIONS);
            if (list instanceof java.util.Collection) {
                for (java.lang.Object item : ((java.util.Collection) (list))) {
                    if (item instanceof java.util.Map) {
                        putConfiguration(new org.apache.ambari.server.state.kerberos.KerberosConfigurationDescriptor(((java.util.Map<?, ?>) (item))));
                    }
                }
            }
            list = data.get(org.apache.ambari.server.state.kerberos.AbstractKerberosDescriptorContainer.KEY_AUTH_TO_LOCAL_PROPERTIES);
            if (list instanceof java.util.Collection) {
                for (java.lang.Object item : ((java.util.Collection) (list))) {
                    if (item instanceof java.lang.String) {
                        putAuthToLocalProperty(((java.lang.String) (item)));
                    }
                }
            }
        }
    }

    public abstract java.util.Collection<? extends org.apache.ambari.server.state.kerberos.AbstractKerberosDescriptorContainer> getChildContainers();

    public abstract org.apache.ambari.server.state.kerberos.AbstractKerberosDescriptorContainer getChildContainer(java.lang.String name);

    public java.util.List<org.apache.ambari.server.state.kerberos.KerberosIdentityDescriptor> getIdentities() {
        try {
            return getIdentities(false, null);
        } catch (org.apache.ambari.server.AmbariException e) {
            return null;
        }
    }

    public void setIdentities(java.util.List<org.apache.ambari.server.state.kerberos.KerberosIdentityDescriptor> identities) {
        this.identities = (identities == null) ? null : new java.util.ArrayList<>(identities);
    }

    public java.util.List<org.apache.ambari.server.state.kerberos.KerberosIdentityDescriptor> getIdentities(boolean resolveReferences, java.util.Map<java.lang.String, java.lang.Object> contextForFilter) throws org.apache.ambari.server.AmbariException {
        if (identities == null) {
            return null;
        } else {
            java.util.List<org.apache.ambari.server.state.kerberos.KerberosIdentityDescriptor> list = new java.util.ArrayList<>();
            for (org.apache.ambari.server.state.kerberos.KerberosIdentityDescriptor identity : identities) {
                org.apache.ambari.server.state.kerberos.KerberosIdentityDescriptor identityToAdd;
                if (resolveReferences) {
                    identityToAdd = dereferenceIdentity(identity);
                } else {
                    identityToAdd = identity;
                }
                if ((identityToAdd != null) && ((contextForFilter == null) || identityToAdd.shouldInclude(contextForFilter))) {
                    if (org.apache.ambari.server.state.kerberos.AbstractKerberosDescriptorContainer.isReferredServiceInstalled(identity, contextForFilter)) {
                        list.add(identityToAdd);
                    } else {
                        org.apache.ambari.server.state.kerberos.AbstractKerberosDescriptorContainer.LOG.info("Skipping identity {} because referred service is not installed.", identityToAdd.getName());
                    }
                }
            }
            return list;
        }
    }

    private static boolean isReferredServiceInstalled(org.apache.ambari.server.state.kerberos.KerberosIdentityDescriptor identity, java.util.Map<java.lang.String, java.lang.Object> contextForFilter) {
        if ((contextForFilter == null) || (!(contextForFilter.get("services") instanceof java.util.Collection))) {
            return true;
        }
        java.util.Set<java.lang.String> installedServices = com.google.common.collect.Sets.newHashSet(((java.util.Collection) (contextForFilter.get("services"))));
        return identity.getReferencedServiceName().transform(org.apache.ambari.server.state.kerberos.AbstractKerberosDescriptorContainer.contains(installedServices)).or(true);
    }

    private static com.google.common.base.Function<java.lang.String, java.lang.Boolean> contains(final java.util.Set<java.lang.String> installed) {
        return com.google.common.base.Functions.forPredicate(com.google.common.base.Predicates.in(installed));
    }

    public org.apache.ambari.server.state.kerberos.KerberosIdentityDescriptor getIdentity(java.lang.String name) {
        org.apache.ambari.server.state.kerberos.KerberosIdentityDescriptor identity = null;
        if ((name != null) && (identities != null)) {
            for (org.apache.ambari.server.state.kerberos.KerberosIdentityDescriptor descriptor : identities) {
                if (name.equals(descriptor.getName())) {
                    identity = descriptor;
                    break;
                }
            }
        }
        return identity;
    }

    public void putIdentity(org.apache.ambari.server.state.kerberos.KerberosIdentityDescriptor identity) {
        if (identity != null) {
            java.lang.String name = identity.getName();
            if (identities == null) {
                identities = new java.util.ArrayList<>();
            }
            if ((name != null) && (!name.isEmpty())) {
                removeIdentity(identity.getName());
            }
            identities.add(identity);
            identity.setParent(this);
        }
    }

    public void removeIdentity(java.lang.String name) {
        if ((name != null) && (identities != null)) {
            java.util.Iterator<org.apache.ambari.server.state.kerberos.KerberosIdentityDescriptor> iterator = identities.iterator();
            while (iterator.hasNext()) {
                org.apache.ambari.server.state.kerberos.KerberosIdentityDescriptor identity = iterator.next();
                if (name.equals(identity.getName())) {
                    identity.setParent(null);
                    iterator.remove();
                }
            } 
        }
    }

    public void setConfigurations(java.util.Map<java.lang.String, org.apache.ambari.server.state.kerberos.KerberosConfigurationDescriptor> configurations) {
        this.configurations = (configurations == null) ? null : new java.util.TreeMap<>(configurations);
    }

    public java.util.Map<java.lang.String, org.apache.ambari.server.state.kerberos.KerberosConfigurationDescriptor> getConfigurations() {
        return getConfigurations(false);
    }

    public java.util.Map<java.lang.String, org.apache.ambari.server.state.kerberos.KerberosConfigurationDescriptor> getConfigurations(boolean includeInherited) {
        if (includeInherited) {
            java.util.Map<java.lang.String, org.apache.ambari.server.state.kerberos.KerberosConfigurationDescriptor> mergedConfigurations = new java.util.TreeMap<>();
            java.util.List<java.util.Map<java.lang.String, org.apache.ambari.server.state.kerberos.KerberosConfigurationDescriptor>> configurationSets = new java.util.ArrayList<>();
            org.apache.ambari.server.state.kerberos.AbstractKerberosDescriptor currentDescriptor = this;
            while (currentDescriptor != null) {
                if (currentDescriptor.isContainer()) {
                    java.util.Map<java.lang.String, org.apache.ambari.server.state.kerberos.KerberosConfigurationDescriptor> configurations = ((org.apache.ambari.server.state.kerberos.AbstractKerberosDescriptorContainer) (currentDescriptor)).getConfigurations();
                    if (configurations != null) {
                        configurationSets.add(configurations);
                    }
                }
                currentDescriptor = currentDescriptor.getParent();
            } 
            java.util.Collections.reverse(configurationSets);
            for (java.util.Map<java.lang.String, org.apache.ambari.server.state.kerberos.KerberosConfigurationDescriptor> map : configurationSets) {
                for (java.util.Map.Entry<java.lang.String, org.apache.ambari.server.state.kerberos.KerberosConfigurationDescriptor> entry : map.entrySet()) {
                    java.lang.String currentType = entry.getKey();
                    org.apache.ambari.server.state.kerberos.KerberosConfigurationDescriptor currentConfiguration = entry.getValue();
                    if (currentConfiguration != null) {
                        org.apache.ambari.server.state.kerberos.KerberosConfigurationDescriptor detachedConfiguration = new org.apache.ambari.server.state.kerberos.KerberosConfigurationDescriptor(currentConfiguration.toMap());
                        org.apache.ambari.server.state.kerberos.KerberosConfigurationDescriptor mergedConfiguration = mergedConfigurations.get(entry.getKey());
                        if (mergedConfiguration == null) {
                            mergedConfigurations.put(currentType, detachedConfiguration);
                        } else {
                            mergedConfiguration.update(detachedConfiguration);
                        }
                    }
                }
            }
            return mergedConfigurations;
        } else {
            return configurations;
        }
    }

    public void putConfiguration(org.apache.ambari.server.state.kerberos.KerberosConfigurationDescriptor configuration) {
        if (configuration != null) {
            java.lang.String type = configuration.getType();
            if (type == null) {
                throw new java.lang.IllegalArgumentException("The configuration type must not be null");
            }
            if (configurations == null) {
                configurations = new java.util.TreeMap<>();
            }
            configurations.put(type, configuration);
            configuration.setParent(this);
        }
    }

    public org.apache.ambari.server.state.kerberos.KerberosConfigurationDescriptor getConfiguration(java.lang.String name) {
        return (name == null) || (configurations == null) ? null : configurations.get(name);
    }

    public void putAuthToLocalProperty(java.lang.String authToLocalProperty) {
        if (authToLocalProperty != null) {
            if (authToLocalProperties == null) {
                authToLocalProperties = new java.util.TreeSet<>();
            }
            authToLocalProperties.add(authToLocalProperty);
        }
    }

    public void setAuthToLocalProperties(java.util.Set<java.lang.String> authToLocalProperties) {
        this.authToLocalProperties = (authToLocalProperties == null) ? null : new java.util.TreeSet<>(authToLocalProperties);
    }

    public java.util.Set<java.lang.String> getAuthToLocalProperties() {
        return authToLocalProperties;
    }

    @java.lang.Override
    public boolean isContainer() {
        return true;
    }

    public void update(org.apache.ambari.server.state.kerberos.AbstractKerberosDescriptorContainer updates) {
        if (updates != null) {
            java.lang.String updatedName = updates.getName();
            if (updatedName != null) {
                setName(updatedName);
            }
            java.util.Map<java.lang.String, org.apache.ambari.server.state.kerberos.KerberosConfigurationDescriptor> updatedConfigurations = updates.getConfigurations();
            if (updatedConfigurations != null) {
                for (java.util.Map.Entry<java.lang.String, org.apache.ambari.server.state.kerberos.KerberosConfigurationDescriptor> entry : updatedConfigurations.entrySet()) {
                    org.apache.ambari.server.state.kerberos.KerberosConfigurationDescriptor existingConfiguration = getConfiguration(entry.getKey());
                    org.apache.ambari.server.state.kerberos.KerberosConfigurationDescriptor clone = new org.apache.ambari.server.state.kerberos.KerberosConfigurationDescriptor(entry.getValue().toMap());
                    if (existingConfiguration == null) {
                        putConfiguration(clone);
                    } else {
                        existingConfiguration.update(clone);
                    }
                }
            }
            java.util.List<org.apache.ambari.server.state.kerberos.KerberosIdentityDescriptor> updatedIdentities = updates.getIdentities();
            if (updatedIdentities != null) {
                for (org.apache.ambari.server.state.kerberos.KerberosIdentityDescriptor updatedIdentity : updatedIdentities) {
                    org.apache.ambari.server.state.kerberos.KerberosIdentityDescriptor existing = getIdentity(updatedIdentity.getName());
                    org.apache.ambari.server.state.kerberos.KerberosIdentityDescriptor clone = new org.apache.ambari.server.state.kerberos.KerberosIdentityDescriptor(updatedIdentity.toMap());
                    if (existing == null) {
                        putIdentity(clone);
                    } else {
                        existing.update(clone);
                    }
                }
            }
            java.util.Set<java.lang.String> updatedAuthToLocalProperties = updates.getAuthToLocalProperties();
            if (updatedAuthToLocalProperties != null) {
                for (java.lang.String updatedAuthToLocalProperty : updatedAuthToLocalProperties) {
                    putAuthToLocalProperty(updatedAuthToLocalProperty);
                }
            }
        }
    }

    public org.apache.ambari.server.state.kerberos.KerberosIdentityDescriptor getReferencedIdentityDescriptor(java.lang.String path) throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.server.state.kerberos.KerberosIdentityDescriptor identityDescriptor = null;
        if (path != null) {
            if (path.startsWith("../")) {
                org.apache.ambari.server.state.kerberos.AbstractKerberosDescriptor parent = getParent();
                path = path.substring(2);
                while (parent != null) {
                    java.lang.String name = parent.getName();
                    if (name != null) {
                        path = java.lang.String.format("/%s", name) + path;
                    }
                    parent = parent.getParent();
                } 
            }
            if (path.startsWith("/")) {
                java.lang.String[] pathParts = path.split("/");
                java.lang.String serviceName = null;
                java.lang.String componentName = null;
                java.lang.String identityName;
                switch (pathParts.length) {
                    case 4 :
                        serviceName = pathParts[1];
                        componentName = pathParts[2];
                        identityName = pathParts[3];
                        break;
                    case 3 :
                        serviceName = pathParts[1];
                        identityName = pathParts[2];
                        break;
                    case 2 :
                        identityName = pathParts[1];
                        break;
                    case 1 :
                        identityName = pathParts[0];
                        break;
                    default :
                        throw new org.apache.ambari.server.AmbariException(java.lang.String.format("Unexpected path length in %s", path));
                }
                if (identityName != null) {
                    org.apache.ambari.server.state.kerberos.AbstractKerberosDescriptor descriptor = getRoot();
                    if (descriptor != null) {
                        if ((serviceName != null) && (!serviceName.isEmpty())) {
                            descriptor = descriptor.getDescriptor(org.apache.ambari.server.state.kerberos.AbstractKerberosDescriptor.Type.SERVICE, serviceName);
                            if (((descriptor != null) && (componentName != null)) && (!componentName.isEmpty())) {
                                descriptor = descriptor.getDescriptor(org.apache.ambari.server.state.kerberos.AbstractKerberosDescriptor.Type.COMPONENT, componentName);
                            }
                        }
                        if (descriptor != null) {
                            descriptor = descriptor.getDescriptor(org.apache.ambari.server.state.kerberos.AbstractKerberosDescriptor.Type.IDENTITY, identityName);
                            if (descriptor instanceof org.apache.ambari.server.state.kerberos.KerberosIdentityDescriptor) {
                                identityDescriptor = ((org.apache.ambari.server.state.kerberos.KerberosIdentityDescriptor) (descriptor));
                            }
                        }
                    }
                }
            }
        }
        return identityDescriptor;
    }

    @java.lang.Override
    protected org.apache.ambari.server.state.kerberos.AbstractKerberosDescriptor getDescriptor(org.apache.ambari.server.state.kerberos.AbstractKerberosDescriptor.Type type, java.lang.String name) {
        if (org.apache.ambari.server.state.kerberos.AbstractKerberosDescriptor.Type.IDENTITY == type) {
            return getIdentity(name);
        } else if (org.apache.ambari.server.state.kerberos.AbstractKerberosDescriptor.Type.CONFIGURATION == type) {
            return getConfiguration(name);
        } else {
            return null;
        }
    }

    @java.lang.Override
    public java.util.Map<java.lang.String, java.lang.Object> toMap() {
        java.util.Map<java.lang.String, java.lang.Object> map = super.toMap();
        if (identities != null) {
            java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.Object>> list = new java.util.TreeMap<>();
            for (org.apache.ambari.server.state.kerberos.KerberosIdentityDescriptor identity : identities) {
                list.put(identity.getName(), identity.toMap());
            }
            map.put(org.apache.ambari.server.state.kerberos.AbstractKerberosDescriptorContainer.KEY_IDENTITIES, list.values());
        }
        if (configurations != null) {
            java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.Object>> list = new java.util.TreeMap<>();
            for (org.apache.ambari.server.state.kerberos.KerberosConfigurationDescriptor configuration : configurations.values()) {
                list.put(configuration.getType(), configuration.toMap());
            }
            map.put(org.apache.ambari.server.state.kerberos.AbstractKerberosDescriptorContainer.KEY_CONFIGURATIONS, list.values());
        }
        if (authToLocalProperties != null) {
            map.put(org.apache.ambari.server.state.kerberos.AbstractKerberosDescriptorContainer.KEY_AUTH_TO_LOCAL_PROPERTIES, authToLocalProperties);
        }
        return map;
    }

    public java.util.List<org.apache.ambari.server.state.kerberos.KerberosIdentityDescriptor> getIdentitiesSkipReferences() {
        return org.apache.ambari.server.state.kerberos.AbstractKerberosDescriptor.nullToEmpty(getIdentities()).stream().filter(identity -> (!identity.getReferencedServiceName().isPresent()) && (!identity.isReference())).collect(java.util.stream.Collectors.toList());
    }

    @java.lang.Override
    public int hashCode() {
        return ((super.hashCode() + (getIdentities() == null ? 0 : getIdentities().hashCode())) + (getAuthToLocalProperties() == null ? 0 : getAuthToLocalProperties().hashCode())) + (getConfigurations() == null ? 0 : getConfigurations().hashCode());
    }

    @java.lang.Override
    public boolean equals(java.lang.Object object) {
        if (object == null) {
            return false;
        } else if (object == this) {
            return true;
        } else if (object instanceof org.apache.ambari.server.state.kerberos.AbstractKerberosDescriptorContainer) {
            org.apache.ambari.server.state.kerberos.AbstractKerberosDescriptorContainer descriptor = ((org.apache.ambari.server.state.kerberos.AbstractKerberosDescriptorContainer) (object));
            return ((super.equals(object) && (getIdentities() == null ? descriptor.getIdentities() == null : getIdentities().equals(descriptor.getIdentities()))) && (getAuthToLocalProperties() == null ? descriptor.getAuthToLocalProperties() == null : getAuthToLocalProperties().equals(descriptor.getAuthToLocalProperties()))) && (getConfigurations() == null ? descriptor.getConfigurations() == null : getConfigurations().equals(descriptor.getConfigurations()));
        } else {
            return false;
        }
    }

    private org.apache.ambari.server.state.kerberos.KerberosIdentityDescriptor dereferenceIdentity(org.apache.ambari.server.state.kerberos.KerberosIdentityDescriptor identity) throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.server.state.kerberos.KerberosIdentityDescriptor dereferencedIdentity = null;
        if (identity != null) {
            org.apache.ambari.server.state.kerberos.KerberosIdentityDescriptor referencedIdentity;
            try {
                if (identity.getReference() != null) {
                    referencedIdentity = getReferencedIdentityDescriptor(identity.getReference());
                } else {
                    referencedIdentity = getReferencedIdentityDescriptor(identity.getName());
                    if (referencedIdentity != null) {
                        org.apache.ambari.server.state.kerberos.AbstractKerberosDescriptorContainer.LOG.warn("Referenced identities should be declared using the identity\'s \"reference\" attribute, not the identity\'s \"name\" attribute." + " This is a deprecated feature. Problems may occur in the future unless this is corrected: {}:{}", identity.getPath(), identity.getName());
                    }
                }
            } catch (org.apache.ambari.server.AmbariException e) {
                throw new org.apache.ambari.server.AmbariException(java.lang.String.format("Invalid Kerberos identity reference: %s", identity.getReference()), e);
            }
            if (referencedIdentity != null) {
                dereferencedIdentity = dereferenceIdentity(referencedIdentity);
                if (dereferencedIdentity != null) {
                    dereferencedIdentity.update(identity);
                } else {
                    dereferencedIdentity = new org.apache.ambari.server.state.kerberos.KerberosIdentityDescriptor(referencedIdentity.toMap());
                    dereferencedIdentity.update(identity);
                }
            } else {
                dereferencedIdentity = new org.apache.ambari.server.state.kerberos.KerberosIdentityDescriptor(identity.toMap());
            }
            dereferencedIdentity.setPath(identity.getPath());
        }
        return dereferencedIdentity;
    }
}