package org.apache.ambari.server.controller.internal;
import com.google.inject.assistedinject.Assisted;
public class KerberosDescriptorResourceProvider extends org.apache.ambari.server.controller.internal.AbstractControllerResourceProvider {
    private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.controller.internal.KerberosDescriptorResourceProvider.class);

    static final java.lang.String KERBEROS_DESCRIPTOR_NAME_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("KerberosDescriptors", "kerberos_descriptor_name");

    private static final java.lang.String KERBEROS_DESCRIPTOR_TEXT_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("KerberosDescriptors", "kerberos_descriptor_text");

    private static final java.util.Map<org.apache.ambari.server.controller.spi.Resource.Type, java.lang.String> KEY_PROPERTY_IDS = com.google.common.collect.ImmutableMap.of(org.apache.ambari.server.controller.spi.Resource.Type.KerberosDescriptor, org.apache.ambari.server.controller.internal.KerberosDescriptorResourceProvider.KERBEROS_DESCRIPTOR_NAME_PROPERTY_ID);

    private static final java.util.Set<java.lang.String> PROPERTY_IDS = com.google.common.collect.ImmutableSet.of(org.apache.ambari.server.controller.internal.KerberosDescriptorResourceProvider.KERBEROS_DESCRIPTOR_NAME_PROPERTY_ID, org.apache.ambari.server.controller.internal.KerberosDescriptorResourceProvider.KERBEROS_DESCRIPTOR_TEXT_PROPERTY_ID);

    private org.apache.ambari.server.orm.dao.KerberosDescriptorDAO kerberosDescriptorDAO;

    private org.apache.ambari.server.topology.KerberosDescriptorFactory kerberosDescriptorFactory;

    @javax.inject.Inject
    KerberosDescriptorResourceProvider(org.apache.ambari.server.orm.dao.KerberosDescriptorDAO kerberosDescriptorDAO, org.apache.ambari.server.topology.KerberosDescriptorFactory kerberosDescriptorFactory, @com.google.inject.assistedinject.Assisted
    org.apache.ambari.server.controller.AmbariManagementController managementController) {
        super(org.apache.ambari.server.controller.spi.Resource.Type.KerberosDescriptor, org.apache.ambari.server.controller.internal.KerberosDescriptorResourceProvider.PROPERTY_IDS, org.apache.ambari.server.controller.internal.KerberosDescriptorResourceProvider.KEY_PROPERTY_IDS, managementController);
        this.kerberosDescriptorDAO = kerberosDescriptorDAO;
        this.kerberosDescriptorFactory = kerberosDescriptorFactory;
    }

    @java.lang.Override
    public java.util.Set<java.lang.String> checkPropertyIds(java.util.Set<java.lang.String> propertyIds) {
        org.apache.ambari.server.controller.internal.KerberosDescriptorResourceProvider.LOGGER.debug("Skipping property id validation for kerberos descriptor resources");
        return java.util.Collections.emptySet();
    }

    @java.lang.Override
    public org.apache.ambari.server.controller.spi.RequestStatus createResources(org.apache.ambari.server.controller.spi.Request request) throws org.apache.ambari.server.controller.spi.ResourceAlreadyExistsException {
        java.lang.String name = org.apache.ambari.server.controller.internal.KerberosDescriptorResourceProvider.getNameFromRequest(request);
        java.lang.String descriptor = org.apache.ambari.server.controller.internal.KerberosDescriptorResourceProvider.getRawKerberosDescriptorFromRequest(request);
        if (kerberosDescriptorDAO.findByName(name) != null) {
            java.lang.String msg = java.lang.String.format("Kerberos descriptor named %s already exists", name);
            org.apache.ambari.server.controller.internal.KerberosDescriptorResourceProvider.LOGGER.info(msg);
            throw new org.apache.ambari.server.controller.spi.ResourceAlreadyExistsException(msg);
        }
        org.apache.ambari.server.topology.KerberosDescriptor kerberosDescriptor = kerberosDescriptorFactory.createKerberosDescriptor(name, descriptor);
        kerberosDescriptorDAO.create(kerberosDescriptor.toEntity());
        return getRequestStatus(null);
    }

    @java.lang.Override
    public java.util.Set<org.apache.ambari.server.controller.spi.Resource> getResources(org.apache.ambari.server.controller.spi.Request request, org.apache.ambari.server.controller.spi.Predicate predicate) throws org.apache.ambari.server.controller.spi.NoSuchResourceException, org.apache.ambari.server.controller.spi.NoSuchParentResourceException {
        java.util.List<org.apache.ambari.server.orm.entities.KerberosDescriptorEntity> results = null;
        boolean applyPredicate = false;
        if (predicate != null) {
            java.util.Set<java.util.Map<java.lang.String, java.lang.Object>> requestProps = getPropertyMaps(predicate);
            if (requestProps.size() == 1) {
                java.lang.String name = ((java.lang.String) (requestProps.iterator().next().get(org.apache.ambari.server.controller.internal.KerberosDescriptorResourceProvider.KERBEROS_DESCRIPTOR_NAME_PROPERTY_ID)));
                if (name != null) {
                    org.apache.ambari.server.orm.entities.KerberosDescriptorEntity entity = kerberosDescriptorDAO.findByName(name);
                    results = (entity == null) ? java.util.Collections.emptyList() : java.util.Collections.singletonList(entity);
                }
            }
        }
        if (results == null) {
            applyPredicate = true;
            results = kerberosDescriptorDAO.findAll();
        }
        java.util.Set<org.apache.ambari.server.controller.spi.Resource> resources = new java.util.HashSet<>();
        java.util.Set<java.lang.String> requestPropertyIds = getRequestPropertyIds(request, predicate);
        for (org.apache.ambari.server.orm.entities.KerberosDescriptorEntity entity : results) {
            org.apache.ambari.server.controller.spi.Resource resource = new org.apache.ambari.server.controller.internal.ResourceImpl(org.apache.ambari.server.controller.spi.Resource.Type.KerberosDescriptor);
            org.apache.ambari.server.controller.internal.KerberosDescriptorResourceProvider.toResource(resource, entity, requestPropertyIds);
            if (((predicate == null) || (!applyPredicate)) || predicate.evaluate(resource)) {
                resources.add(resource);
            }
        }
        if ((predicate != null) && resources.isEmpty()) {
            throw new org.apache.ambari.server.controller.spi.NoSuchResourceException("The requested resource doesn't exist: Kerberos Descriptor not found, " + predicate);
        }
        return resources;
    }

    private static void toResource(org.apache.ambari.server.controller.spi.Resource resource, org.apache.ambari.server.orm.entities.KerberosDescriptorEntity entity, java.util.Set<java.lang.String> requestPropertyIds) {
        org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.KerberosDescriptorResourceProvider.KERBEROS_DESCRIPTOR_NAME_PROPERTY_ID, entity.getName(), requestPropertyIds);
        org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.KerberosDescriptorResourceProvider.KERBEROS_DESCRIPTOR_TEXT_PROPERTY_ID, entity.getKerberosDescriptorText(), requestPropertyIds);
    }

    @java.lang.Override
    public org.apache.ambari.server.controller.spi.RequestStatus updateResources(org.apache.ambari.server.controller.spi.Request request, org.apache.ambari.server.controller.spi.Predicate predicate) throws org.apache.ambari.server.controller.spi.SystemException, org.apache.ambari.server.controller.spi.UnsupportedPropertyException, org.apache.ambari.server.controller.spi.NoSuchResourceException, org.apache.ambari.server.controller.spi.NoSuchParentResourceException {
        throw new java.lang.UnsupportedOperationException("Not yet implemented!");
    }

    @java.lang.Override
    public org.apache.ambari.server.controller.spi.RequestStatus deleteResources(org.apache.ambari.server.controller.spi.Request request, org.apache.ambari.server.controller.spi.Predicate predicate) throws org.apache.ambari.server.controller.spi.SystemException, org.apache.ambari.server.controller.spi.UnsupportedPropertyException, org.apache.ambari.server.controller.spi.NoSuchResourceException, org.apache.ambari.server.controller.spi.NoSuchParentResourceException {
        java.util.Set<org.apache.ambari.server.controller.spi.Resource> setResources = getResources(new org.apache.ambari.server.controller.internal.RequestImpl(null, null, null, null), predicate);
        for (org.apache.ambari.server.controller.spi.Resource resource : setResources) {
            final java.lang.String kerberosDescriptorName = ((java.lang.String) (resource.getPropertyValue(org.apache.ambari.server.controller.internal.KerberosDescriptorResourceProvider.KERBEROS_DESCRIPTOR_NAME_PROPERTY_ID)));
            org.apache.ambari.server.controller.internal.KerberosDescriptorResourceProvider.LOGGER.debug("Deleting resource with name: {}", kerberosDescriptorName);
            kerberosDescriptorDAO.removeByName(kerberosDescriptorName);
        }
        return getRequestStatus(null);
    }

    @java.lang.Override
    protected java.util.Set<java.lang.String> getPKPropertyIds() {
        return com.google.common.collect.ImmutableSet.copyOf(org.apache.ambari.server.controller.internal.KerberosDescriptorResourceProvider.KEY_PROPERTY_IDS.values());
    }

    private static java.lang.String getRawKerberosDescriptorFromRequest(org.apache.ambari.server.controller.spi.Request request) {
        java.util.Map<java.lang.String, java.lang.String> requestInfoProperties = request.getRequestInfoProperties();
        if (requestInfoProperties != null) {
            java.lang.String descriptorText = requestInfoProperties.get(org.apache.ambari.server.controller.spi.Request.REQUEST_INFO_BODY_PROPERTY);
            if (!com.google.common.base.Strings.isNullOrEmpty(descriptorText)) {
                return descriptorText;
            }
        }
        java.lang.String msg = "No Kerberos descriptor found in the request body";
        org.apache.ambari.server.controller.internal.KerberosDescriptorResourceProvider.LOGGER.error(msg);
        throw new java.lang.IllegalArgumentException(msg);
    }

    private static java.lang.String getNameFromRequest(org.apache.ambari.server.controller.spi.Request request) {
        if ((request.getProperties() != null) && (!request.getProperties().isEmpty())) {
            java.util.Map<java.lang.String, java.lang.Object> properties = request.getProperties().iterator().next();
            java.lang.Object name = properties.get(org.apache.ambari.server.controller.internal.KerberosDescriptorResourceProvider.KERBEROS_DESCRIPTOR_NAME_PROPERTY_ID);
            if (name != null) {
                return java.lang.String.valueOf(name);
            }
        }
        java.lang.String msg = "No name provided for the Kerberos descriptor";
        org.apache.ambari.server.controller.internal.KerberosDescriptorResourceProvider.LOGGER.error(msg);
        throw new java.lang.IllegalArgumentException(msg);
    }
}