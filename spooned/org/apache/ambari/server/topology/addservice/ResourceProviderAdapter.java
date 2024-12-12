package org.apache.ambari.server.topology.addservice;
import static org.apache.ambari.server.controller.internal.RequestResourceProvider.CONTEXT;
@javax.inject.Singleton
public class ResourceProviderAdapter {
    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.topology.addservice.ResourceProviderAdapter.class);

    private static final org.apache.ambari.server.utils.LoggingPreconditions CHECK = new org.apache.ambari.server.utils.LoggingPreconditions(org.apache.ambari.server.topology.addservice.ResourceProviderAdapter.LOG);

    private final org.apache.ambari.server.state.kerberos.KerberosDescriptorFactory descriptorFactory = new org.apache.ambari.server.state.kerberos.KerberosDescriptorFactory();

    @javax.inject.Inject
    private org.apache.ambari.server.controller.AmbariManagementController controller;

    public void createServices(org.apache.ambari.server.topology.addservice.AddServiceInfo request) {
        org.apache.ambari.server.topology.addservice.ResourceProviderAdapter.LOG.info("Creating service resources for {}", request);
        java.util.Set<java.util.Map<java.lang.String, java.lang.Object>> properties = request.newServices().keySet().stream().map(service -> org.apache.ambari.server.topology.addservice.ResourceProviderAdapter.createServiceRequestProperties(request, service)).collect(java.util.stream.Collectors.toSet());
        org.apache.ambari.server.topology.addservice.ResourceProviderAdapter.createResources(request, org.apache.ambari.server.controller.spi.Resource.Type.Service, properties, null, false);
    }

    public void createComponents(org.apache.ambari.server.topology.addservice.AddServiceInfo request) {
        org.apache.ambari.server.topology.addservice.ResourceProviderAdapter.LOG.info("Creating component resources for {}", request);
        java.util.Set<java.util.Map<java.lang.String, java.lang.Object>> properties = request.newServices().entrySet().stream().flatMap(componentsOfService -> componentsOfService.getValue().keySet().stream().map(component -> org.apache.ambari.server.topology.addservice.ResourceProviderAdapter.createComponentRequestProperties(request, componentsOfService.getKey(), component))).collect(java.util.stream.Collectors.toSet());
        org.apache.ambari.server.topology.addservice.ResourceProviderAdapter.createResources(request, org.apache.ambari.server.controller.spi.Resource.Type.Component, properties, null, false);
    }

    public void createHostComponents(org.apache.ambari.server.topology.addservice.AddServiceInfo request) {
        org.apache.ambari.server.topology.addservice.ResourceProviderAdapter.LOG.info("Creating host component resources for {}", request);
        java.util.Set<java.util.Map<java.lang.String, java.lang.Object>> properties = request.newServices().entrySet().stream().flatMap(componentsOfService -> componentsOfService.getValue().entrySet().stream().flatMap(hostsOfComponent -> hostsOfComponent.getValue().stream().map(host -> org.apache.ambari.server.topology.addservice.ResourceProviderAdapter.createHostComponentRequestProperties(request, componentsOfService.getKey(), hostsOfComponent.getKey(), host)))).collect(java.util.stream.Collectors.toSet());
        org.apache.ambari.server.topology.addservice.ResourceProviderAdapter.createResources(request, org.apache.ambari.server.controller.spi.Resource.Type.HostComponent, properties, null, false);
    }

    public void createConfigs(org.apache.ambari.server.topology.addservice.AddServiceInfo request) {
        org.apache.ambari.server.topology.addservice.ResourceProviderAdapter.LOG.info("Creating configurations for {}", request);
        java.util.Set<org.apache.ambari.server.controller.ClusterRequest> requests = org.apache.ambari.server.topology.addservice.ResourceProviderAdapter.createConfigRequestsForNewServices(request);
        updateCluster(request, requests, "Error creating configurations for %s");
    }

    public void createCredentials(org.apache.ambari.server.topology.addservice.AddServiceInfo request) {
        if (!request.getRequest().getCredentials().isEmpty()) {
            org.apache.ambari.server.topology.addservice.ResourceProviderAdapter.LOG.info("Creating {} credential(s) for {}", request.getRequest().getCredentials().size(), request);
            request.getRequest().getCredentials().values().stream().peek(credential -> org.apache.ambari.server.topology.addservice.ResourceProviderAdapter.LOG.debug("Creating credential {}", credential)).map(credential -> org.apache.ambari.server.topology.addservice.ResourceProviderAdapter.createCredentialRequestProperties(request.clusterName(), credential)).forEach(properties -> org.apache.ambari.server.topology.addservice.ResourceProviderAdapter.createResources(request, org.apache.ambari.server.controller.spi.Resource.Type.Credential, com.google.common.collect.ImmutableSet.of(properties), null, true));
        }
    }

    public java.util.Optional<org.apache.ambari.server.state.kerberos.KerberosDescriptor> getKerberosDescriptor(org.apache.ambari.server.topology.addservice.AddServiceInfo request) {
        java.util.Set<java.lang.String> propertyIds = com.google.common.collect.ImmutableSet.of(org.apache.ambari.server.controller.internal.ArtifactResourceProvider.ARTIFACT_DATA_PROPERTY);
        org.apache.ambari.server.controller.spi.Predicate predicate = org.apache.ambari.server.topology.addservice.ResourceProviderAdapter.predicateForKerberosDescriptorArtifact(request.clusterName());
        java.util.Set<org.apache.ambari.server.controller.spi.Resource> resources = org.apache.ambari.server.topology.addservice.ResourceProviderAdapter.getResources(request, propertyIds, org.apache.ambari.server.controller.spi.Resource.Type.Artifact, predicate);
        if ((resources == null) || resources.isEmpty()) {
            return java.util.Optional.empty();
        }
        org.apache.ambari.server.topology.addservice.ResourceProviderAdapter.CHECK.checkArgument(resources.size() == 1, "Expected only one artifact of type %s, but got %d", org.apache.ambari.server.controller.internal.ArtifactResourceProvider.KERBEROS_DESCRIPTOR, resources.size());
        return java.util.Optional.of(descriptorFactory.createInstance(resources.iterator().next().getPropertiesMap().get(org.apache.ambari.server.controller.internal.ArtifactResourceProvider.ARTIFACT_DATA_PROPERTY)));
    }

    public void createKerberosDescriptor(org.apache.ambari.server.topology.addservice.AddServiceInfo request, org.apache.ambari.server.state.kerberos.KerberosDescriptor descriptor) {
        org.apache.ambari.server.topology.addservice.ResourceProviderAdapter.LOG.info("Creating Kerberos descriptor for {}", request);
        java.util.Map<java.lang.String, java.lang.Object> properties = org.apache.ambari.server.topology.addservice.ResourceProviderAdapter.createKerberosDescriptorRequestProperties(request.clusterName());
        java.util.Map<java.lang.String, java.lang.String> requestInfo = org.apache.ambari.server.topology.addservice.ResourceProviderAdapter.requestInfoForKerberosDescriptor(descriptor);
        org.apache.ambari.server.topology.addservice.ResourceProviderAdapter.createResources(request, org.apache.ambari.server.controller.spi.Resource.Type.Artifact, com.google.common.collect.ImmutableSet.of(properties), requestInfo, false);
    }

    public void updateKerberosDescriptor(org.apache.ambari.server.topology.addservice.AddServiceInfo request, org.apache.ambari.server.state.kerberos.KerberosDescriptor descriptor) {
        org.apache.ambari.server.topology.addservice.ResourceProviderAdapter.LOG.info("Updating Kerberos descriptor from {}", request);
        java.util.Map<java.lang.String, java.lang.Object> properties = org.apache.ambari.server.topology.addservice.ResourceProviderAdapter.createKerberosDescriptorRequestProperties(request.clusterName());
        java.util.Map<java.lang.String, java.lang.String> requestInfo = org.apache.ambari.server.topology.addservice.ResourceProviderAdapter.requestInfoForKerberosDescriptor(descriptor);
        org.apache.ambari.server.controller.spi.Predicate predicate = org.apache.ambari.server.topology.addservice.ResourceProviderAdapter.predicateForKerberosDescriptorArtifact(request.clusterName());
        org.apache.ambari.server.topology.addservice.ResourceProviderAdapter.updateResources(request, com.google.common.collect.ImmutableSet.of(properties), org.apache.ambari.server.controller.spi.Resource.Type.Artifact, predicate, requestInfo);
    }

    public void updateExistingConfigs(org.apache.ambari.server.topology.addservice.AddServiceInfo request, java.util.Set<java.lang.String> existingServices) {
        org.apache.ambari.server.topology.addservice.ResourceProviderAdapter.LOG.info("Updating existing configurations for {}", request);
        java.util.Set<org.apache.ambari.server.controller.ClusterRequest> requests = createConfigRequestsForExistingServices(request, existingServices);
        updateCluster(request, requests, "Error updating configurations for %s");
    }

    public void updateServiceDesiredState(org.apache.ambari.server.topology.addservice.AddServiceInfo request, org.apache.ambari.server.state.State desiredState) {
        org.apache.ambari.server.topology.addservice.ResourceProviderAdapter.LOG.info("Updating service desired state to {} for {}", desiredState, request);
        java.util.Set<java.util.Map<java.lang.String, java.lang.Object>> properties = com.google.common.collect.ImmutableSet.of(com.google.common.collect.ImmutableMap.of(org.apache.ambari.server.controller.internal.ServiceResourceProvider.SERVICE_SERVICE_STATE_PROPERTY_ID, desiredState.name()));
        java.util.Map<java.lang.String, java.lang.String> requestInfo = org.apache.ambari.server.controller.internal.RequestOperationLevel.propertiesFor(org.apache.ambari.server.controller.spi.Resource.Type.Service, request.clusterName());
        org.apache.ambari.server.controller.spi.Predicate predicate = org.apache.ambari.server.topology.addservice.ResourceProviderAdapter.predicateForNewServices(request);
        org.apache.ambari.server.topology.addservice.ResourceProviderAdapter.updateResources(request, properties, org.apache.ambari.server.controller.spi.Resource.Type.Service, predicate, requestInfo);
    }

    public void updateHostComponentDesiredState(org.apache.ambari.server.topology.addservice.AddServiceInfo request, org.apache.ambari.server.controller.spi.Predicate predicate, org.apache.ambari.server.topology.ProvisionStep step) {
        org.apache.ambari.server.state.State desiredState = step.getDesiredStateToSet();
        org.apache.ambari.server.topology.addservice.ResourceProviderAdapter.LOG.info("Updating host component desired state to {} per {} for {}", desiredState, step, request);
        org.apache.ambari.server.topology.addservice.ResourceProviderAdapter.LOG.debug("Using predicate {}", predicate);
        java.util.Set<java.util.Map<java.lang.String, java.lang.Object>> properties = com.google.common.collect.ImmutableSet.of(com.google.common.collect.ImmutableMap.of(org.apache.ambari.server.controller.internal.HostComponentResourceProvider.STATE, desiredState.name(), org.apache.ambari.server.controller.internal.RequestResourceProvider.CONTEXT, java.lang.String.format("Put new components to %s state", desiredState)));
        java.util.Map<java.lang.String, java.lang.String> requestInfo = new com.google.common.collect.ImmutableMap.Builder<java.lang.String, java.lang.String>().putAll(org.apache.ambari.server.controller.internal.RequestOperationLevel.propertiesFor(org.apache.ambari.server.controller.spi.Resource.Type.HostComponent, request.clusterName())).putAll(step.getProvisionProperties()).build();
        org.apache.ambari.server.controller.internal.HostComponentResourceProvider rp = ((org.apache.ambari.server.controller.internal.HostComponentResourceProvider) (org.apache.ambari.server.topology.addservice.ResourceProviderAdapter.getClusterController().ensureResourceProvider(org.apache.ambari.server.controller.spi.Resource.Type.HostComponent)));
        org.apache.ambari.server.controller.spi.Request internalRequest = org.apache.ambari.server.topology.addservice.ResourceProviderAdapter.createRequest(properties, requestInfo, null);
        try {
            rp.doUpdateResources(request.getStages(), internalRequest, predicate, false, false, false);
        } catch (org.apache.ambari.server.controller.spi.UnsupportedPropertyException | org.apache.ambari.server.controller.spi.SystemException | org.apache.ambari.server.controller.spi.NoSuchParentResourceException | org.apache.ambari.server.controller.spi.NoSuchResourceException e) {
            org.apache.ambari.server.topology.addservice.ResourceProviderAdapter.CHECK.wrapInUnchecked(e, java.lang.RuntimeException::new, "Error updating host component desired state for %s", request);
        }
    }

    private static java.util.Set<org.apache.ambari.server.controller.spi.Resource> getResources(org.apache.ambari.server.topology.addservice.AddServiceInfo request, java.util.Set<java.lang.String> propertyIds, org.apache.ambari.server.controller.spi.Resource.Type resourceType, org.apache.ambari.server.controller.spi.Predicate predicate) {
        org.apache.ambari.server.controller.spi.Request internalRequest = org.apache.ambari.server.topology.addservice.ResourceProviderAdapter.createRequest(null, null, propertyIds);
        org.apache.ambari.server.controller.spi.ResourceProvider rp = org.apache.ambari.server.topology.addservice.ResourceProviderAdapter.getClusterController().ensureResourceProvider(resourceType);
        try {
            return rp.getResources(internalRequest, predicate);
        } catch (org.apache.ambari.server.controller.spi.NoSuchResourceException e) {
            return com.google.common.collect.ImmutableSet.of();
        } catch (org.apache.ambari.server.controller.spi.UnsupportedPropertyException | org.apache.ambari.server.controller.spi.SystemException | org.apache.ambari.server.controller.spi.NoSuchParentResourceException e) {
            return org.apache.ambari.server.topology.addservice.ResourceProviderAdapter.CHECK.wrapInUnchecked(e, java.lang.RuntimeException::new, "Error getting resources %s for %s", resourceType, request);
        }
    }

    private static void createResources(org.apache.ambari.server.topology.addservice.AddServiceInfo request, org.apache.ambari.server.controller.spi.Resource.Type resourceType, java.util.Set<java.util.Map<java.lang.String, java.lang.Object>> properties, java.util.Map<java.lang.String, java.lang.String> requestInfo, boolean okIfExists) {
        org.apache.ambari.server.controller.spi.Request internalRequest = org.apache.ambari.server.topology.addservice.ResourceProviderAdapter.createRequest(properties, requestInfo, null);
        org.apache.ambari.server.controller.spi.ResourceProvider rp = org.apache.ambari.server.topology.addservice.ResourceProviderAdapter.getClusterController().ensureResourceProvider(resourceType);
        try {
            rp.createResources(internalRequest);
        } catch (org.apache.ambari.server.controller.spi.UnsupportedPropertyException | org.apache.ambari.server.controller.spi.SystemException | org.apache.ambari.server.controller.spi.ResourceAlreadyExistsException | org.apache.ambari.server.controller.spi.NoSuchParentResourceException e) {
            if (okIfExists && (e instanceof org.apache.ambari.server.controller.spi.ResourceAlreadyExistsException)) {
                org.apache.ambari.server.topology.addservice.ResourceProviderAdapter.LOG.info("Resource already exists: {}, no need to create", e.getMessage());
            } else {
                org.apache.ambari.server.topology.addservice.ResourceProviderAdapter.CHECK.wrapInUnchecked(e, java.lang.RuntimeException::new, "Error creating resources %s for %s", resourceType, request);
            }
        }
    }

    private static void updateResources(org.apache.ambari.server.topology.addservice.AddServiceInfo request, java.util.Set<java.util.Map<java.lang.String, java.lang.Object>> properties, org.apache.ambari.server.controller.spi.Resource.Type resourceType, org.apache.ambari.server.controller.spi.Predicate predicate, java.util.Map<java.lang.String, java.lang.String> requestInfo) {
        org.apache.ambari.server.controller.spi.Request internalRequest = org.apache.ambari.server.topology.addservice.ResourceProviderAdapter.createRequest(properties, requestInfo, null);
        org.apache.ambari.server.controller.spi.ResourceProvider rp = org.apache.ambari.server.topology.addservice.ResourceProviderAdapter.getClusterController().ensureResourceProvider(resourceType);
        try {
            rp.updateResources(internalRequest, predicate);
        } catch (org.apache.ambari.server.controller.spi.UnsupportedPropertyException | org.apache.ambari.server.controller.spi.SystemException | org.apache.ambari.server.controller.spi.NoSuchParentResourceException | org.apache.ambari.server.controller.spi.NoSuchResourceException e) {
            org.apache.ambari.server.topology.addservice.ResourceProviderAdapter.CHECK.wrapInUnchecked(e, java.lang.RuntimeException::new, "Error updating resources %s for %s", resourceType, request);
        }
    }

    private void updateCluster(org.apache.ambari.server.topology.addservice.AddServiceInfo addServiceRequest, java.util.Set<org.apache.ambari.server.controller.ClusterRequest> requests, java.lang.String errorMessageFormat) {
        try {
            controller.updateClusters(requests, null);
        } catch (org.apache.ambari.server.AmbariException | org.apache.ambari.server.security.authorization.AuthorizationException e) {
            org.apache.ambari.server.topology.addservice.ResourceProviderAdapter.CHECK.wrapInUnchecked(e, java.lang.RuntimeException::new, errorMessageFormat, addServiceRequest);
        }
    }

    private static org.apache.ambari.server.controller.spi.Request createRequest(java.util.Set<java.util.Map<java.lang.String, java.lang.Object>> properties, java.util.Map<java.lang.String, java.lang.String> requestInfoProperties, java.util.Set<java.lang.String> propertyIds) {
        return new org.apache.ambari.server.controller.internal.RequestImpl(propertyIds, properties, requestInfoProperties, null);
    }

    public static java.util.Map<java.lang.String, java.lang.String> requestInfoForKerberosDescriptor(org.apache.ambari.server.state.kerberos.KerberosDescriptor descriptor) {
        return com.google.common.collect.ImmutableMap.of(org.apache.ambari.server.controller.spi.Request.REQUEST_INFO_BODY_PROPERTY, org.apache.ambari.server.controller.internal.ArtifactResourceProvider.toArtifactDataJson(descriptor.toMap()));
    }

    private static java.util.Map<java.lang.String, java.lang.Object> createServiceRequestProperties(org.apache.ambari.server.topology.addservice.AddServiceInfo request, java.lang.String service) {
        com.google.common.collect.ImmutableMap.Builder<java.lang.String, java.lang.Object> properties = com.google.common.collect.ImmutableMap.builder();
        properties.put(org.apache.ambari.server.controller.internal.ServiceResourceProvider.SERVICE_CLUSTER_NAME_PROPERTY_ID, request.clusterName());
        properties.put(org.apache.ambari.server.controller.internal.ServiceResourceProvider.SERVICE_SERVICE_NAME_PROPERTY_ID, service);
        properties.put(org.apache.ambari.server.controller.internal.ServiceResourceProvider.SERVICE_SERVICE_STATE_PROPERTY_ID, org.apache.ambari.server.state.State.INIT.name());
        return properties.build();
    }

    private static java.util.Map<java.lang.String, java.lang.Object> createComponentRequestProperties(org.apache.ambari.server.topology.addservice.AddServiceInfo request, java.lang.String service, java.lang.String component) {
        com.google.common.collect.ImmutableMap.Builder<java.lang.String, java.lang.Object> properties = com.google.common.collect.ImmutableMap.builder();
        properties.put(org.apache.ambari.server.controller.internal.ComponentResourceProvider.CLUSTER_NAME, request.clusterName());
        properties.put(org.apache.ambari.server.controller.internal.ComponentResourceProvider.SERVICE_NAME, service);
        properties.put(org.apache.ambari.server.controller.internal.ComponentResourceProvider.COMPONENT_NAME, component);
        properties.put(org.apache.ambari.server.controller.internal.ComponentResourceProvider.STATE, org.apache.ambari.server.state.State.INIT.name());
        return properties.build();
    }

    private static java.util.Map<java.lang.String, java.lang.Object> createHostComponentRequestProperties(org.apache.ambari.server.topology.addservice.AddServiceInfo request, java.lang.String service, java.lang.String component, java.lang.String host) {
        com.google.common.collect.ImmutableMap.Builder<java.lang.String, java.lang.Object> properties = com.google.common.collect.ImmutableMap.builder();
        properties.put(org.apache.ambari.server.controller.internal.HostComponentResourceProvider.CLUSTER_NAME, request.clusterName());
        properties.put(org.apache.ambari.server.controller.internal.HostComponentResourceProvider.SERVICE_NAME, service);
        properties.put(org.apache.ambari.server.controller.internal.HostComponentResourceProvider.COMPONENT_NAME, component);
        properties.put(org.apache.ambari.server.controller.internal.HostComponentResourceProvider.HOST_NAME, host);
        properties.put(org.apache.ambari.server.controller.internal.HostComponentResourceProvider.STATE, org.apache.ambari.server.state.State.INIT.name());
        return properties.build();
    }

    public static java.util.Map<java.lang.String, java.lang.Object> createCredentialRequestProperties(java.lang.String clusterName, org.apache.ambari.server.topology.Credential credential) {
        com.google.common.collect.ImmutableMap.Builder<java.lang.String, java.lang.Object> properties = com.google.common.collect.ImmutableMap.builder();
        properties.put(org.apache.ambari.server.controller.internal.CredentialResourceProvider.CREDENTIAL_CLUSTER_NAME_PROPERTY_ID, clusterName);
        properties.put(org.apache.ambari.server.controller.internal.CredentialResourceProvider.CREDENTIAL_ALIAS_PROPERTY_ID, credential.getAlias());
        properties.put(org.apache.ambari.server.controller.internal.CredentialResourceProvider.CREDENTIAL_PRINCIPAL_PROPERTY_ID, credential.getPrincipal());
        properties.put(org.apache.ambari.server.controller.internal.CredentialResourceProvider.CREDENTIAL_KEY_PROPERTY_ID, credential.getKey());
        properties.put(org.apache.ambari.server.controller.internal.CredentialResourceProvider.CREDENTIAL_TYPE_PROPERTY_ID, credential.getType().name());
        return properties.build();
    }

    public static java.util.Map<java.lang.String, java.lang.Object> createKerberosDescriptorRequestProperties(java.lang.String clusterName) {
        return com.google.common.collect.ImmutableMap.of(org.apache.ambari.server.controller.internal.ArtifactResourceProvider.CLUSTER_NAME_PROPERTY, clusterName, org.apache.ambari.server.controller.internal.ArtifactResourceProvider.ARTIFACT_NAME_PROPERTY, org.apache.ambari.server.controller.internal.ArtifactResourceProvider.KERBEROS_DESCRIPTOR);
    }

    private static java.util.Set<org.apache.ambari.server.controller.ClusterRequest> createConfigRequestsForNewServices(org.apache.ambari.server.topology.addservice.AddServiceInfo request) {
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> fullProperties = request.getConfig().getFullProperties();
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>>> fullAttributes = request.getConfig().getFullAttributes();
        return org.apache.ambari.server.topology.addservice.ResourceProviderAdapter.createConfigRequestsForServices(request.newServices().keySet(), configType -> !java.util.Objects.equals(configType, org.apache.ambari.server.state.ConfigHelper.CLUSTER_ENV), request, fullProperties, fullAttributes);
    }

    private java.util.Set<org.apache.ambari.server.controller.ClusterRequest> createConfigRequestsForExistingServices(org.apache.ambari.server.topology.addservice.AddServiceInfo request, java.util.Set<java.lang.String> existingServices) {
        java.util.Set<java.lang.String> configTypesInRequest = com.google.common.collect.ImmutableSet.copyOf(com.google.common.collect.Sets.difference(com.google.common.collect.Sets.union(request.getConfig().getProperties().keySet(), request.getConfig().getAttributes().keySet()), com.google.common.collect.ImmutableSet.of(org.apache.ambari.server.state.ConfigHelper.CLUSTER_ENV)));
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> fullProperties = request.getConfig().getFullProperties();
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>>> fullAttributes = request.getConfig().getFullAttributes();
        java.util.Set<org.apache.ambari.server.controller.ClusterRequest> clusterRequests = org.apache.ambari.server.topology.addservice.ResourceProviderAdapter.createConfigRequestsForServices(existingServices, configTypesInRequest::contains, request, fullProperties, fullAttributes);
        if (request.getConfig().getProperties().containsKey(org.apache.ambari.server.state.ConfigHelper.CLUSTER_ENV)) {
            java.util.Optional<org.apache.ambari.server.controller.ClusterRequest> clusterEnvRequest = org.apache.ambari.server.topology.addservice.ResourceProviderAdapter.createConfigRequestForConfigTypes(java.util.stream.Stream.of(org.apache.ambari.server.state.ConfigHelper.CLUSTER_ENV), request, fullProperties, fullAttributes);
            clusterEnvRequest.ifPresent(clusterRequests::add);
        }
        return clusterRequests;
    }

    private static java.util.Set<org.apache.ambari.server.controller.ClusterRequest> createConfigRequestsForServices(java.util.Set<java.lang.String> services, java.util.function.Predicate<java.lang.String> predicate, org.apache.ambari.server.topology.addservice.AddServiceInfo request, java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> fullProperties, java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>>> fullAttributes) {
        return services.stream().map(service -> org.apache.ambari.server.topology.addservice.ResourceProviderAdapter.createConfigRequestForConfigTypes(request.getStack().getConfigurationTypes(service).stream().filter(predicate), request, fullProperties, fullAttributes)).filter(java.util.Optional::isPresent).map(java.util.Optional::get).collect(java.util.stream.Collectors.toSet());
    }

    private static java.util.Optional<org.apache.ambari.server.controller.ClusterRequest> createConfigRequestForConfigTypes(java.util.stream.Stream<java.lang.String> configTypes, org.apache.ambari.server.topology.addservice.AddServiceInfo addServiceRequest, java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> fullProperties, java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>>> fullAttributes) {
        java.util.List<org.apache.ambari.server.controller.ConfigurationRequest> configRequests = configTypes.peek(configType -> org.apache.ambari.server.topology.addservice.ResourceProviderAdapter.LOG.info("Creating request for config type {} for {}", configType, addServiceRequest)).map(configType -> new org.apache.ambari.server.controller.ConfigurationRequest(addServiceRequest.clusterName(), configType, "ADD_SERVICE_" + java.lang.System.currentTimeMillis(), fullProperties.getOrDefault(configType, com.google.common.collect.ImmutableMap.of()), fullAttributes.getOrDefault(configType, com.google.common.collect.ImmutableMap.of()))).collect(java.util.stream.Collectors.toList());
        if (configRequests.isEmpty()) {
            return java.util.Optional.empty();
        }
        org.apache.ambari.server.controller.ClusterRequest clusterRequest = new org.apache.ambari.server.controller.ClusterRequest(null, addServiceRequest.clusterName(), null, null);
        clusterRequest.setDesiredConfig(configRequests);
        return java.util.Optional.of(clusterRequest);
    }

    public static org.apache.ambari.server.controller.spi.Predicate predicateForKerberosDescriptorArtifact(java.lang.String clusterName) {
        return new org.apache.ambari.server.controller.utilities.PredicateBuilder().begin().property(org.apache.ambari.server.controller.internal.ArtifactResourceProvider.CLUSTER_NAME_PROPERTY).equals(clusterName).and().property(org.apache.ambari.server.controller.internal.ArtifactResourceProvider.ARTIFACT_NAME_PROPERTY).equals(org.apache.ambari.server.controller.internal.ArtifactResourceProvider.KERBEROS_DESCRIPTOR).end().toPredicate();
    }

    private static org.apache.ambari.server.controller.spi.Predicate predicateForNewServices(org.apache.ambari.server.topology.addservice.AddServiceInfo request) {
        return new org.apache.ambari.server.controller.predicate.AndPredicate(new org.apache.ambari.server.controller.predicate.EqualsPredicate<>(org.apache.ambari.server.controller.internal.ServiceResourceProvider.SERVICE_CLUSTER_NAME_PROPERTY_ID, request.clusterName()), org.apache.ambari.server.controller.predicate.OrPredicate.of(request.newServices().keySet().stream().map(service -> new org.apache.ambari.server.controller.predicate.EqualsPredicate<>(org.apache.ambari.server.controller.internal.ServiceResourceProvider.SERVICE_SERVICE_NAME_PROPERTY_ID, service)).collect(java.util.stream.Collectors.toList())));
    }

    private static org.apache.ambari.server.controller.spi.ClusterController getClusterController() {
        return org.apache.ambari.server.controller.utilities.ClusterControllerHelper.getClusterController();
    }
}