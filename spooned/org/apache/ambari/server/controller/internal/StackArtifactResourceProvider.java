package org.apache.ambari.server.controller.internal;
import org.apache.commons.lang.StringUtils;
@org.apache.ambari.server.StaticallyInject
public class StackArtifactResourceProvider extends org.apache.ambari.server.controller.internal.AbstractControllerResourceProvider {
    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.controller.internal.StackArtifactResourceProvider.class);

    public static final java.lang.String STACK_NAME_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("Artifacts", "stack_name");

    public static final java.lang.String STACK_VERSION_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("Artifacts", "stack_version");

    public static final java.lang.String STACK_SERVICE_NAME_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("Artifacts", "service_name");

    public static final java.lang.String STACK_COMPONENT_NAME_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("Artifacts", "component_name");

    public static final java.lang.String ARTIFACT_NAME_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("Artifacts", "artifact_name");

    public static final java.lang.String ARTIFACT_DATA_PROPERTY_ID = "artifact_data";

    public static final java.util.Set<java.lang.String> pkPropertyIds = com.google.common.collect.ImmutableSet.<java.lang.String>builder().add(org.apache.ambari.server.controller.internal.StackArtifactResourceProvider.ARTIFACT_NAME_PROPERTY_ID).build();

    public static final java.util.Map<org.apache.ambari.server.controller.spi.Resource.Type, java.lang.String> keyPropertyIds = com.google.common.collect.ImmutableMap.<org.apache.ambari.server.controller.spi.Resource.Type, java.lang.String>builder().put(org.apache.ambari.server.controller.spi.Resource.Type.StackArtifact, org.apache.ambari.server.controller.internal.StackArtifactResourceProvider.ARTIFACT_NAME_PROPERTY_ID).put(org.apache.ambari.server.controller.spi.Resource.Type.Stack, org.apache.ambari.server.controller.internal.StackArtifactResourceProvider.STACK_NAME_PROPERTY_ID).put(org.apache.ambari.server.controller.spi.Resource.Type.StackVersion, org.apache.ambari.server.controller.internal.StackArtifactResourceProvider.STACK_VERSION_PROPERTY_ID).put(org.apache.ambari.server.controller.spi.Resource.Type.StackService, org.apache.ambari.server.controller.internal.StackArtifactResourceProvider.STACK_SERVICE_NAME_PROPERTY_ID).build();

    public static final java.util.Set<java.lang.String> propertyIds = com.google.common.collect.ImmutableSet.<java.lang.String>builder().add(org.apache.ambari.server.controller.internal.StackArtifactResourceProvider.STACK_NAME_PROPERTY_ID).add(org.apache.ambari.server.controller.internal.StackArtifactResourceProvider.STACK_VERSION_PROPERTY_ID).add(org.apache.ambari.server.controller.internal.StackArtifactResourceProvider.STACK_SERVICE_NAME_PROPERTY_ID).add(org.apache.ambari.server.controller.internal.StackArtifactResourceProvider.ARTIFACT_NAME_PROPERTY_ID).add(org.apache.ambari.server.controller.internal.StackArtifactResourceProvider.ARTIFACT_DATA_PROPERTY_ID).build();

    public static final java.lang.String KERBEROS_DESCRIPTOR_NAME = "kerberos_descriptor";

    public static final java.lang.String METRICS_DESCRIPTOR_NAME = "metrics_descriptor";

    public static final java.lang.String WIDGETS_DESCRIPTOR_NAME = "widgets_descriptor";

    @com.google.inject.Inject
    private static org.apache.ambari.server.state.kerberos.KerberosDescriptorFactory kerberosDescriptorFactory;

    @com.google.inject.Inject
    private static org.apache.ambari.server.state.kerberos.KerberosServiceDescriptorFactory kerberosServiceDescriptorFactory;

    java.lang.reflect.Type widgetLayoutType = new com.google.gson.reflect.TypeToken<java.util.Map<java.lang.String, java.util.List<org.apache.ambari.server.state.stack.WidgetLayout>>>() {}.getType();

    com.google.gson.Gson gson = new com.google.gson.Gson();

    protected StackArtifactResourceProvider(org.apache.ambari.server.controller.AmbariManagementController managementController) {
        super(org.apache.ambari.server.controller.spi.Resource.Type.StackArtifact, org.apache.ambari.server.controller.internal.StackArtifactResourceProvider.propertyIds, org.apache.ambari.server.controller.internal.StackArtifactResourceProvider.keyPropertyIds, managementController);
    }

    @java.lang.Override
    public java.util.Set<org.apache.ambari.server.controller.spi.Resource> getResources(org.apache.ambari.server.controller.spi.Request request, org.apache.ambari.server.controller.spi.Predicate predicate) throws org.apache.ambari.server.controller.spi.SystemException, org.apache.ambari.server.controller.spi.UnsupportedPropertyException, org.apache.ambari.server.controller.spi.NoSuchResourceException, org.apache.ambari.server.controller.spi.NoSuchParentResourceException {
        java.util.Set<org.apache.ambari.server.controller.spi.Resource> resources = new java.util.HashSet<>();
        resources.addAll(getKerberosDescriptors(request, predicate));
        resources.addAll(getMetricsDescriptors(request, predicate));
        resources.addAll(getWidgetsDescriptors(request, predicate));
        if (resources.isEmpty()) {
            throw new org.apache.ambari.server.controller.spi.NoSuchResourceException("The requested resource doesn't exist: Artifact not found, " + predicate);
        }
        return resources;
    }

    @java.lang.Override
    protected java.util.Set<java.lang.String> getPKPropertyIds() {
        return org.apache.ambari.server.controller.internal.StackArtifactResourceProvider.pkPropertyIds;
    }

    @java.lang.Override
    public org.apache.ambari.server.controller.spi.RequestStatus createResources(org.apache.ambari.server.controller.spi.Request request) throws org.apache.ambari.server.controller.spi.SystemException, org.apache.ambari.server.controller.spi.UnsupportedPropertyException, org.apache.ambari.server.controller.spi.ResourceAlreadyExistsException, org.apache.ambari.server.controller.spi.NoSuchParentResourceException {
        throw new java.lang.UnsupportedOperationException("Creating stack artifacts is not supported");
    }

    @java.lang.Override
    public org.apache.ambari.server.controller.spi.RequestStatus updateResources(org.apache.ambari.server.controller.spi.Request request, org.apache.ambari.server.controller.spi.Predicate predicate) throws org.apache.ambari.server.controller.spi.SystemException, org.apache.ambari.server.controller.spi.UnsupportedPropertyException, org.apache.ambari.server.controller.spi.NoSuchResourceException, org.apache.ambari.server.controller.spi.NoSuchParentResourceException {
        throw new java.lang.UnsupportedOperationException("Updating of stack artifacts is not supported");
    }

    @java.lang.Override
    public org.apache.ambari.server.controller.spi.RequestStatus deleteResources(org.apache.ambari.server.controller.spi.Request request, org.apache.ambari.server.controller.spi.Predicate predicate) throws org.apache.ambari.server.controller.spi.SystemException, org.apache.ambari.server.controller.spi.UnsupportedPropertyException, org.apache.ambari.server.controller.spi.NoSuchResourceException, org.apache.ambari.server.controller.spi.NoSuchParentResourceException {
        throw new java.lang.UnsupportedOperationException("Deletion of stack artifacts is not supported");
    }

    private java.util.Set<org.apache.ambari.server.controller.spi.Resource> getKerberosDescriptors(org.apache.ambari.server.controller.spi.Request request, org.apache.ambari.server.controller.spi.Predicate predicate) throws org.apache.ambari.server.controller.spi.SystemException, org.apache.ambari.server.controller.spi.UnsupportedPropertyException, org.apache.ambari.server.controller.spi.NoSuchParentResourceException, org.apache.ambari.server.controller.spi.NoSuchResourceException {
        java.util.Set<org.apache.ambari.server.controller.spi.Resource> resources = new java.util.HashSet<>();
        for (java.util.Map<java.lang.String, java.lang.Object> properties : getPropertyMaps(predicate)) {
            java.lang.String artifactName = ((java.lang.String) (properties.get(org.apache.ambari.server.controller.internal.StackArtifactResourceProvider.ARTIFACT_NAME_PROPERTY_ID)));
            if ((artifactName == null) || artifactName.equals(org.apache.ambari.server.controller.internal.StackArtifactResourceProvider.KERBEROS_DESCRIPTOR_NAME)) {
                java.lang.String stackName = ((java.lang.String) (properties.get(org.apache.ambari.server.controller.internal.StackArtifactResourceProvider.STACK_NAME_PROPERTY_ID)));
                java.lang.String stackVersion = ((java.lang.String) (properties.get(org.apache.ambari.server.controller.internal.StackArtifactResourceProvider.STACK_VERSION_PROPERTY_ID)));
                java.lang.String stackService = ((java.lang.String) (properties.get(org.apache.ambari.server.controller.internal.StackArtifactResourceProvider.STACK_SERVICE_NAME_PROPERTY_ID)));
                java.util.Map<java.lang.String, java.lang.Object> descriptor;
                try {
                    descriptor = getKerberosDescriptor(stackName, stackVersion, stackService);
                } catch (java.io.IOException e) {
                    org.apache.ambari.server.controller.internal.StackArtifactResourceProvider.LOG.error("Unable to process Kerberos Descriptor. Properties: " + properties, e);
                    throw new org.apache.ambari.server.controller.spi.SystemException("An internal exception occurred while attempting to build a Kerberos Descriptor " + "artifact. See ambari server logs for more information", e);
                }
                if (descriptor != null) {
                    org.apache.ambari.server.controller.spi.Resource resource = new org.apache.ambari.server.controller.internal.ResourceImpl(org.apache.ambari.server.controller.spi.Resource.Type.StackArtifact);
                    java.util.Set<java.lang.String> requestedIds = getRequestPropertyIds(request, predicate);
                    org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.StackArtifactResourceProvider.ARTIFACT_NAME_PROPERTY_ID, org.apache.ambari.server.controller.internal.StackArtifactResourceProvider.KERBEROS_DESCRIPTOR_NAME, requestedIds);
                    org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.StackArtifactResourceProvider.ARTIFACT_DATA_PROPERTY_ID, descriptor, requestedIds);
                    org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.StackArtifactResourceProvider.STACK_NAME_PROPERTY_ID, stackName, requestedIds);
                    org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.StackArtifactResourceProvider.STACK_VERSION_PROPERTY_ID, stackVersion, requestedIds);
                    if (stackService != null) {
                        org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.StackArtifactResourceProvider.STACK_SERVICE_NAME_PROPERTY_ID, stackService, requestedIds);
                    }
                    resources.add(resource);
                }
            }
        }
        return resources;
    }

    private java.util.Set<org.apache.ambari.server.controller.spi.Resource> getMetricsDescriptors(org.apache.ambari.server.controller.spi.Request request, org.apache.ambari.server.controller.spi.Predicate predicate) throws org.apache.ambari.server.controller.spi.SystemException, org.apache.ambari.server.controller.spi.UnsupportedPropertyException, org.apache.ambari.server.controller.spi.NoSuchParentResourceException, org.apache.ambari.server.controller.spi.NoSuchResourceException {
        java.util.Set<org.apache.ambari.server.controller.spi.Resource> resources = new java.util.HashSet<>();
        for (java.util.Map<java.lang.String, java.lang.Object> properties : getPropertyMaps(predicate)) {
            java.lang.String artifactName = ((java.lang.String) (properties.get(org.apache.ambari.server.controller.internal.StackArtifactResourceProvider.ARTIFACT_NAME_PROPERTY_ID)));
            if ((artifactName == null) || artifactName.equals(org.apache.ambari.server.controller.internal.StackArtifactResourceProvider.METRICS_DESCRIPTOR_NAME)) {
                java.lang.String stackName = ((java.lang.String) (properties.get(org.apache.ambari.server.controller.internal.StackArtifactResourceProvider.STACK_NAME_PROPERTY_ID)));
                java.lang.String stackVersion = ((java.lang.String) (properties.get(org.apache.ambari.server.controller.internal.StackArtifactResourceProvider.STACK_VERSION_PROPERTY_ID)));
                java.lang.String stackService = ((java.lang.String) (properties.get(org.apache.ambari.server.controller.internal.StackArtifactResourceProvider.STACK_SERVICE_NAME_PROPERTY_ID)));
                java.lang.String componentName = ((java.lang.String) (properties.get(org.apache.ambari.server.controller.internal.StackArtifactResourceProvider.STACK_COMPONENT_NAME_PROPERTY_ID)));
                java.util.Map<java.lang.String, java.lang.Object> descriptor;
                org.apache.ambari.server.api.services.AmbariMetaInfo metaInfo = getManagementController().getAmbariMetaInfo();
                try {
                    java.util.List<org.apache.ambari.server.state.stack.MetricDefinition> componentMetrics;
                    java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.util.List<org.apache.ambari.server.state.stack.MetricDefinition>>> serviceMetrics;
                    if (stackService != null) {
                        if (componentName == null) {
                            serviceMetrics = org.apache.ambari.server.controller.internal.StackArtifactResourceProvider.removeAggregateFunctions(metaInfo.getServiceMetrics(stackName, stackVersion, stackService));
                            descriptor = java.util.Collections.singletonMap(stackService, ((java.lang.Object) (serviceMetrics)));
                        } else {
                            componentMetrics = org.apache.ambari.server.controller.internal.StackArtifactResourceProvider.removeAggregateFunctions(metaInfo.getMetrics(stackName, stackVersion, stackService, componentName, org.apache.ambari.server.controller.spi.Resource.Type.Component.name()));
                            descriptor = java.util.Collections.singletonMap(componentName, ((java.lang.Object) (componentMetrics)));
                        }
                    } else {
                        java.util.Map<java.lang.String, java.util.Map<java.lang.String, org.apache.ambari.server.controller.internal.PropertyInfo>> clusterMetrics = org.apache.ambari.server.controller.utilities.PropertyHelper.getMetricPropertyIds(org.apache.ambari.server.controller.spi.Resource.Type.Cluster);
                        java.util.Map<java.lang.String, java.util.Map<java.lang.String, org.apache.ambari.server.controller.internal.PropertyInfo>> hostMetrics = org.apache.ambari.server.controller.utilities.PropertyHelper.getMetricPropertyIds(org.apache.ambari.server.controller.spi.Resource.Type.Host);
                        descriptor = new java.util.HashMap<>();
                        descriptor.put(org.apache.ambari.server.controller.spi.Resource.Type.Cluster.name(), clusterMetrics);
                        descriptor.put(org.apache.ambari.server.controller.spi.Resource.Type.Host.name(), hostMetrics);
                    }
                } catch (java.io.IOException e) {
                    org.apache.ambari.server.controller.internal.StackArtifactResourceProvider.LOG.error("Unable to process Metrics Descriptor. Properties: " + properties, e);
                    throw new org.apache.ambari.server.controller.spi.SystemException("An internal exception occurred while attempting to build a Metrics Descriptor " + "artifact. See ambari server logs for more information", e);
                }
                org.apache.ambari.server.controller.spi.Resource resource = new org.apache.ambari.server.controller.internal.ResourceImpl(org.apache.ambari.server.controller.spi.Resource.Type.StackArtifact);
                java.util.Set<java.lang.String> requestedIds = getRequestPropertyIds(request, predicate);
                org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.StackArtifactResourceProvider.ARTIFACT_NAME_PROPERTY_ID, org.apache.ambari.server.controller.internal.StackArtifactResourceProvider.METRICS_DESCRIPTOR_NAME, requestedIds);
                org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.StackArtifactResourceProvider.ARTIFACT_DATA_PROPERTY_ID, descriptor, requestedIds);
                org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.StackArtifactResourceProvider.STACK_NAME_PROPERTY_ID, stackName, requestedIds);
                org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.StackArtifactResourceProvider.STACK_VERSION_PROPERTY_ID, stackVersion, requestedIds);
                if (stackService != null) {
                    org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.StackArtifactResourceProvider.STACK_SERVICE_NAME_PROPERTY_ID, stackService, requestedIds);
                }
                resources.add(resource);
            }
        }
        return resources;
    }

    private java.util.Set<org.apache.ambari.server.controller.spi.Resource> getWidgetsDescriptors(org.apache.ambari.server.controller.spi.Request request, org.apache.ambari.server.controller.spi.Predicate predicate) throws org.apache.ambari.server.controller.spi.SystemException, org.apache.ambari.server.controller.spi.UnsupportedPropertyException, org.apache.ambari.server.controller.spi.NoSuchParentResourceException, org.apache.ambari.server.controller.spi.NoSuchResourceException {
        java.util.Set<org.apache.ambari.server.controller.spi.Resource> resources = new java.util.HashSet<>();
        for (java.util.Map<java.lang.String, java.lang.Object> properties : getPropertyMaps(predicate)) {
            java.lang.String artifactName = ((java.lang.String) (properties.get(org.apache.ambari.server.controller.internal.StackArtifactResourceProvider.ARTIFACT_NAME_PROPERTY_ID)));
            if ((artifactName == null) || artifactName.equals(org.apache.ambari.server.controller.internal.StackArtifactResourceProvider.WIDGETS_DESCRIPTOR_NAME)) {
                java.lang.String stackName = ((java.lang.String) (properties.get(org.apache.ambari.server.controller.internal.StackArtifactResourceProvider.STACK_NAME_PROPERTY_ID)));
                java.lang.String stackVersion = ((java.lang.String) (properties.get(org.apache.ambari.server.controller.internal.StackArtifactResourceProvider.STACK_VERSION_PROPERTY_ID)));
                java.lang.String stackService = ((java.lang.String) (properties.get(org.apache.ambari.server.controller.internal.StackArtifactResourceProvider.STACK_SERVICE_NAME_PROPERTY_ID)));
                java.util.Map<java.lang.String, java.lang.Object> descriptor;
                try {
                    descriptor = getWidgetsDescriptor(stackName, stackVersion, stackService);
                } catch (java.io.IOException e) {
                    org.apache.ambari.server.controller.internal.StackArtifactResourceProvider.LOG.error("Unable to process Widgets Descriptor. Properties: " + properties, e);
                    throw new org.apache.ambari.server.controller.spi.SystemException("An internal exception occurred while attempting to build a Widgets Descriptor " + "artifact. See ambari server logs for more information", e);
                }
                if (descriptor != null) {
                    org.apache.ambari.server.controller.spi.Resource resource = new org.apache.ambari.server.controller.internal.ResourceImpl(org.apache.ambari.server.controller.spi.Resource.Type.StackArtifact);
                    java.util.Set<java.lang.String> requestedIds = getRequestPropertyIds(request, predicate);
                    org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.StackArtifactResourceProvider.ARTIFACT_NAME_PROPERTY_ID, org.apache.ambari.server.controller.internal.StackArtifactResourceProvider.WIDGETS_DESCRIPTOR_NAME, requestedIds);
                    org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.StackArtifactResourceProvider.ARTIFACT_DATA_PROPERTY_ID, descriptor, requestedIds);
                    org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.StackArtifactResourceProvider.STACK_NAME_PROPERTY_ID, stackName, requestedIds);
                    org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.StackArtifactResourceProvider.STACK_VERSION_PROPERTY_ID, stackVersion, requestedIds);
                    if (stackService != null) {
                        org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.StackArtifactResourceProvider.STACK_SERVICE_NAME_PROPERTY_ID, stackService, requestedIds);
                    }
                    resources.add(resource);
                }
            }
        }
        return resources;
    }

    private java.util.Map<java.lang.String, java.lang.Object> getWidgetsDescriptor(java.lang.String stackName, java.lang.String stackVersion, java.lang.String serviceName) throws org.apache.ambari.server.controller.spi.NoSuchParentResourceException, java.io.IOException {
        org.apache.ambari.server.controller.AmbariManagementController controller = getManagementController();
        org.apache.ambari.server.state.StackInfo stackInfo;
        try {
            stackInfo = controller.getAmbariMetaInfo().getStack(stackName, stackVersion);
        } catch (org.apache.ambari.server.StackAccessException e) {
            throw new org.apache.ambari.server.controller.spi.NoSuchParentResourceException(java.lang.String.format("Parent stack resource doesn't exist: stackName='%s', stackVersion='%s'", stackName, stackVersion));
        }
        if (org.apache.commons.lang.StringUtils.isEmpty(serviceName)) {
            return null;
        } else {
            return getWidgetsDescriptorForService(stackInfo, serviceName);
        }
    }

    public java.util.Map<java.lang.String, java.lang.Object> getWidgetsDescriptorForService(org.apache.ambari.server.state.StackInfo stackInfo, java.lang.String serviceName) throws org.apache.ambari.server.controller.spi.NoSuchParentResourceException, java.io.IOException {
        java.util.Map<java.lang.String, java.lang.Object> widgetDescriptor = null;
        org.apache.ambari.server.state.ServiceInfo serviceInfo = stackInfo.getService(serviceName);
        if (serviceInfo == null) {
            throw new org.apache.ambari.server.controller.spi.NoSuchParentResourceException(("Service not found. serviceName" + " = ") + serviceName);
        }
        java.io.File widgetDescriptorFile = serviceInfo.getWidgetsDescriptorFile();
        if ((widgetDescriptorFile != null) && widgetDescriptorFile.exists()) {
            widgetDescriptor = gson.fromJson(new java.io.FileReader(widgetDescriptorFile), widgetLayoutType);
        }
        return widgetDescriptor;
    }

    private java.util.Map<java.lang.String, java.lang.Object> getKerberosDescriptor(java.lang.String stackName, java.lang.String stackVersion, java.lang.String serviceName) throws org.apache.ambari.server.controller.spi.NoSuchParentResourceException, java.io.IOException {
        return serviceName == null ? buildStackDescriptor(stackName, stackVersion) : getServiceDescriptor(stackName, stackVersion, serviceName);
    }

    private java.util.Map<java.lang.String, java.lang.Object> buildStackDescriptor(java.lang.String stackName, java.lang.String stackVersion) throws org.apache.ambari.server.controller.spi.NoSuchParentResourceException, java.io.IOException {
        org.apache.ambari.server.state.kerberos.KerberosDescriptor kerberosDescriptor = new org.apache.ambari.server.state.kerberos.KerberosDescriptor();
        org.apache.ambari.server.controller.AmbariManagementController controller = getManagementController();
        org.apache.ambari.server.state.StackInfo stackInfo;
        try {
            stackInfo = controller.getAmbariMetaInfo().getStack(stackName, stackVersion);
        } catch (org.apache.ambari.server.StackAccessException e) {
            throw new org.apache.ambari.server.controller.spi.NoSuchParentResourceException(java.lang.String.format("Parent stack resource doesn't exist: stackName='%s', stackVersion='%s'", stackName, stackVersion));
        }
        java.util.Collection<org.apache.ambari.server.state.kerberos.KerberosServiceDescriptor> serviceDescriptors = getServiceDescriptors(stackInfo);
        serviceDescriptors.forEach(kerberosDescriptor::putService);
        return kerberosDescriptor.toMap();
    }

    private java.util.Map<java.lang.String, java.lang.Object> getServiceDescriptor(java.lang.String stackName, java.lang.String stackVersion, java.lang.String serviceName) throws org.apache.ambari.server.controller.spi.NoSuchParentResourceException, java.io.IOException {
        org.apache.ambari.server.controller.AmbariManagementController controller = getManagementController();
        org.apache.ambari.server.state.ServiceInfo serviceInfo;
        try {
            serviceInfo = controller.getAmbariMetaInfo().getService(stackName, stackVersion, serviceName);
        } catch (org.apache.ambari.server.StackAccessException e) {
            throw new org.apache.ambari.server.controller.spi.NoSuchParentResourceException(java.lang.String.format("Parent stack/service resource doesn't exist: stackName='%s', stackVersion='%s', serviceName='%s'", stackName, stackVersion, serviceName));
        }
        java.io.File kerberosFile = serviceInfo.getKerberosDescriptorFile();
        if (kerberosFile != null) {
            org.apache.ambari.server.state.kerberos.KerberosServiceDescriptor serviceDescriptor = org.apache.ambari.server.controller.internal.StackArtifactResourceProvider.kerberosServiceDescriptorFactory.createInstance(kerberosFile, serviceName);
            if (serviceDescriptor != null) {
                return serviceDescriptor.toMap();
            }
        }
        return null;
    }

    private java.util.Collection<org.apache.ambari.server.state.kerberos.KerberosServiceDescriptor> getServiceDescriptors(org.apache.ambari.server.state.StackInfo stack) throws java.io.IOException {
        java.util.Collection<org.apache.ambari.server.state.kerberos.KerberosServiceDescriptor> serviceDescriptors = new java.util.ArrayList<>();
        for (org.apache.ambari.server.state.ServiceInfo service : stack.getServices()) {
            java.io.File descriptorFile = service.getKerberosDescriptorFile();
            if (descriptorFile != null) {
                org.apache.ambari.server.state.kerberos.KerberosServiceDescriptor descriptor = org.apache.ambari.server.controller.internal.StackArtifactResourceProvider.kerberosServiceDescriptorFactory.createInstance(descriptorFile, service.getName());
                if (descriptor != null) {
                    serviceDescriptors.add(descriptor);
                }
            }
        }
        return serviceDescriptors;
    }

    private static java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.util.List<org.apache.ambari.server.state.stack.MetricDefinition>>> removeAggregateFunctions(java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.util.List<org.apache.ambari.server.state.stack.MetricDefinition>>> serviceMetrics) {
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.util.List<org.apache.ambari.server.state.stack.MetricDefinition>>> filteredServiceMetrics = null;
        if (serviceMetrics != null) {
            filteredServiceMetrics = new java.util.HashMap<>();
            for (java.lang.String component : serviceMetrics.keySet()) {
                java.util.Map<java.lang.String, java.util.List<org.apache.ambari.server.state.stack.MetricDefinition>> componentMetricsCopy = new java.util.HashMap<>();
                java.util.Map<java.lang.String, java.util.List<org.apache.ambari.server.state.stack.MetricDefinition>> componentMetrics = serviceMetrics.get(component);
                for (java.lang.String category : componentMetrics.keySet()) {
                    componentMetricsCopy.put(category, org.apache.ambari.server.controller.internal.StackArtifactResourceProvider.removeAggregateFunctions(componentMetrics.get(category)));
                }
                filteredServiceMetrics.put(component, componentMetricsCopy);
            }
        }
        return filteredServiceMetrics;
    }

    private static java.util.List<org.apache.ambari.server.state.stack.MetricDefinition> removeAggregateFunctions(java.util.List<org.apache.ambari.server.state.stack.MetricDefinition> metricsDefinitions) {
        java.util.List<org.apache.ambari.server.state.stack.MetricDefinition> filteredComponentMetrics = null;
        if (metricsDefinitions != null) {
            filteredComponentMetrics = new java.util.ArrayList<>();
            for (org.apache.ambari.server.state.stack.MetricDefinition metricDefinition : metricsDefinitions) {
                java.util.Map<java.lang.String, java.util.Map<java.lang.String, org.apache.ambari.server.state.stack.Metric>> categorizedMetricsCopy = new java.util.HashMap<>();
                java.util.Map<java.lang.String, java.util.Map<java.lang.String, org.apache.ambari.server.state.stack.Metric>> categorizedMetrics = metricDefinition.getMetricsByCategory();
                for (java.lang.String category : categorizedMetrics.keySet()) {
                    java.util.Map<java.lang.String, org.apache.ambari.server.state.stack.Metric> namedMetricsCopy = new java.util.HashMap<>();
                    java.util.Map<java.lang.String, org.apache.ambari.server.state.stack.Metric> namedMetrics = categorizedMetrics.get(category);
                    for (java.lang.String metricName : namedMetrics.keySet()) {
                        if (!(metricDefinition.getType().equals("ganglia") && org.apache.ambari.server.controller.utilities.PropertyHelper.hasAggregateFunctionSuffix(metricName))) {
                            namedMetricsCopy.put(metricName, namedMetrics.get(metricName));
                        }
                    }
                    categorizedMetricsCopy.put(category, namedMetricsCopy);
                }
                filteredComponentMetrics.add(new org.apache.ambari.server.state.stack.MetricDefinition(metricDefinition.getType(), metricDefinition.getProperties(), categorizedMetricsCopy));
            }
        }
        return filteredComponentMetrics;
    }
}