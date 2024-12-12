package org.apache.ambari.server.controller.internal;
public class LdapSyncEventResourceProvider extends org.apache.ambari.server.controller.internal.AbstractControllerResourceProvider {
    private static java.util.concurrent.ExecutorService executorService;

    private static final int THREAD_POOL_CORE_SIZE = 2;

    private static final int THREAD_POOL_MAX_SIZE = 5;

    private static final long THREAD_POOL_TIMEOUT = 1000L;

    public static final java.lang.String EVENT_ID_PROPERTY_ID = "Event/id";

    public static final java.lang.String EVENT_STATUS_PROPERTY_ID = "Event/status";

    public static final java.lang.String EVENT_STATUS_DETAIL_PROPERTY_ID = "Event/status_detail";

    public static final java.lang.String EVENT_START_TIME_PROPERTY_ID = "Event/sync_time/start";

    public static final java.lang.String EVENT_END_TIME_PROPERTY_ID = "Event/sync_time/end";

    public static final java.lang.String USERS_CREATED_PROPERTY_ID = "Event/summary/users/created";

    public static final java.lang.String USERS_UPDATED_PROPERTY_ID = "Event/summary/users/updated";

    public static final java.lang.String USERS_REMOVED_PROPERTY_ID = "Event/summary/users/removed";

    public static final java.lang.String USERS_SKIPPED_PROPERTY_ID = "Event/summary/users/skipped";

    public static final java.lang.String GROUPS_CREATED_PROPERTY_ID = "Event/summary/groups/created";

    public static final java.lang.String GROUPS_UPDATED_PROPERTY_ID = "Event/summary/groups/updated";

    public static final java.lang.String GROUPS_REMOVED_PROPERTY_ID = "Event/summary/groups/removed";

    public static final java.lang.String MEMBERSHIPS_CREATED_PROPERTY_ID = "Event/summary/memberships/created";

    public static final java.lang.String MEMBERSHIPS_REMOVED_PROPERTY_ID = "Event/summary/memberships/removed";

    public static final java.lang.String EVENT_SPECS_PROPERTY_ID = "Event/specs";

    private static final java.util.Map<org.apache.ambari.server.controller.spi.Resource.Type, java.lang.String> keyPropertyIds = com.google.common.collect.ImmutableMap.<org.apache.ambari.server.controller.spi.Resource.Type, java.lang.String>builder().put(org.apache.ambari.server.controller.spi.Resource.Type.LdapSyncEvent, org.apache.ambari.server.controller.internal.LdapSyncEventResourceProvider.EVENT_ID_PROPERTY_ID).build();

    private static final java.util.Set<java.lang.String> propertyIds = com.google.common.collect.Sets.newHashSet(org.apache.ambari.server.controller.internal.LdapSyncEventResourceProvider.EVENT_ID_PROPERTY_ID, org.apache.ambari.server.controller.internal.LdapSyncEventResourceProvider.EVENT_STATUS_PROPERTY_ID, org.apache.ambari.server.controller.internal.LdapSyncEventResourceProvider.EVENT_STATUS_DETAIL_PROPERTY_ID, org.apache.ambari.server.controller.internal.LdapSyncEventResourceProvider.EVENT_START_TIME_PROPERTY_ID, org.apache.ambari.server.controller.internal.LdapSyncEventResourceProvider.EVENT_END_TIME_PROPERTY_ID, org.apache.ambari.server.controller.internal.LdapSyncEventResourceProvider.USERS_CREATED_PROPERTY_ID, org.apache.ambari.server.controller.internal.LdapSyncEventResourceProvider.USERS_UPDATED_PROPERTY_ID, org.apache.ambari.server.controller.internal.LdapSyncEventResourceProvider.USERS_REMOVED_PROPERTY_ID, org.apache.ambari.server.controller.internal.LdapSyncEventResourceProvider.USERS_SKIPPED_PROPERTY_ID, org.apache.ambari.server.controller.internal.LdapSyncEventResourceProvider.GROUPS_CREATED_PROPERTY_ID, org.apache.ambari.server.controller.internal.LdapSyncEventResourceProvider.GROUPS_UPDATED_PROPERTY_ID, org.apache.ambari.server.controller.internal.LdapSyncEventResourceProvider.GROUPS_REMOVED_PROPERTY_ID, org.apache.ambari.server.controller.internal.LdapSyncEventResourceProvider.MEMBERSHIPS_CREATED_PROPERTY_ID, org.apache.ambari.server.controller.internal.LdapSyncEventResourceProvider.MEMBERSHIPS_REMOVED_PROPERTY_ID, org.apache.ambari.server.controller.internal.LdapSyncEventResourceProvider.EVENT_SPECS_PROPERTY_ID);

    private static final java.lang.String PRINCIPAL_TYPE_SPEC_KEY = "principal_type";

    private static final java.lang.String SYNC_TYPE_SPEC_KEY = "sync_type";

    private static final java.lang.String POST_PROCESS_EXISTING_USERS_SPEC_KEY = "post_process_existing_users";

    private static final java.lang.String NAMES_SPEC_KEY = "names";

    private final java.util.Map<java.lang.Long, org.apache.ambari.server.orm.entities.LdapSyncEventEntity> events = new java.util.concurrent.ConcurrentSkipListMap<>();

    private final java.util.Queue<org.apache.ambari.server.orm.entities.LdapSyncEventEntity> eventQueue = new java.util.LinkedList<>();

    private volatile boolean processingEvents = false;

    private java.util.concurrent.atomic.AtomicLong nextEventId = new java.util.concurrent.atomic.AtomicLong(1L);

    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.controller.internal.LdapSyncEventResourceProvider.class);

    public LdapSyncEventResourceProvider(org.apache.ambari.server.controller.AmbariManagementController managementController) {
        super(org.apache.ambari.server.controller.spi.Resource.Type.LdapSyncEvent, org.apache.ambari.server.controller.internal.LdapSyncEventResourceProvider.propertyIds, org.apache.ambari.server.controller.internal.LdapSyncEventResourceProvider.keyPropertyIds, managementController);
        java.util.EnumSet<org.apache.ambari.server.security.authorization.RoleAuthorization> roleAuthorizations = java.util.EnumSet.of(org.apache.ambari.server.security.authorization.RoleAuthorization.AMBARI_MANAGE_GROUPS, org.apache.ambari.server.security.authorization.RoleAuthorization.AMBARI_MANAGE_USERS);
        setRequiredCreateAuthorizations(roleAuthorizations);
        setRequiredDeleteAuthorizations(roleAuthorizations);
    }

    @java.lang.Override
    public org.apache.ambari.server.controller.spi.RequestStatus createResourcesAuthorized(org.apache.ambari.server.controller.spi.Request event) throws org.apache.ambari.server.controller.spi.SystemException, org.apache.ambari.server.controller.spi.UnsupportedPropertyException, org.apache.ambari.server.controller.spi.ResourceAlreadyExistsException, org.apache.ambari.server.controller.spi.NoSuchParentResourceException {
        java.util.Set<org.apache.ambari.server.orm.entities.LdapSyncEventEntity> newEvents = new java.util.HashSet<>();
        for (java.util.Map<java.lang.String, java.lang.Object> properties : event.getProperties()) {
            newEvents.add(createResources(getCreateCommand(properties)));
        }
        notifyCreate(org.apache.ambari.server.controller.spi.Resource.Type.ViewInstance, event);
        java.util.Set<org.apache.ambari.server.controller.spi.Resource> associatedResources = new java.util.HashSet<>();
        for (org.apache.ambari.server.orm.entities.LdapSyncEventEntity eventEntity : newEvents) {
            org.apache.ambari.server.controller.spi.Resource resource = new org.apache.ambari.server.controller.internal.ResourceImpl(org.apache.ambari.server.controller.spi.Resource.Type.LdapSyncEvent);
            resource.setProperty(org.apache.ambari.server.controller.internal.LdapSyncEventResourceProvider.EVENT_ID_PROPERTY_ID, eventEntity.getId());
            associatedResources.add(resource);
            synchronized(eventQueue) {
                eventQueue.offer(eventEntity);
            }
        }
        ensureEventProcessor();
        return getRequestStatus(null, associatedResources);
    }

    @java.lang.Override
    public java.util.Set<org.apache.ambari.server.controller.spi.Resource> getResources(org.apache.ambari.server.controller.spi.Request event, org.apache.ambari.server.controller.spi.Predicate predicate) throws org.apache.ambari.server.controller.spi.SystemException, org.apache.ambari.server.controller.spi.UnsupportedPropertyException, org.apache.ambari.server.controller.spi.NoSuchResourceException, org.apache.ambari.server.controller.spi.NoSuchParentResourceException {
        java.util.Set<org.apache.ambari.server.controller.spi.Resource> resources = new java.util.HashSet<>();
        java.util.Set<java.lang.String> requestedIds = getRequestPropertyIds(event, predicate);
        for (org.apache.ambari.server.orm.entities.LdapSyncEventEntity eventEntity : events.values()) {
            resources.add(toResource(eventEntity, requestedIds));
        }
        return resources;
    }

    @java.lang.Override
    public org.apache.ambari.server.controller.spi.RequestStatus updateResources(org.apache.ambari.server.controller.spi.Request event, org.apache.ambari.server.controller.spi.Predicate predicate) throws org.apache.ambari.server.controller.spi.SystemException, org.apache.ambari.server.controller.spi.UnsupportedPropertyException, org.apache.ambari.server.controller.spi.NoSuchResourceException, org.apache.ambari.server.controller.spi.NoSuchParentResourceException {
        throw new java.lang.UnsupportedOperationException("Not supported.");
    }

    @java.lang.Override
    public org.apache.ambari.server.controller.spi.RequestStatus deleteResourcesAuthorized(org.apache.ambari.server.controller.spi.Request request, org.apache.ambari.server.controller.spi.Predicate predicate) throws org.apache.ambari.server.controller.spi.SystemException, org.apache.ambari.server.controller.spi.UnsupportedPropertyException, org.apache.ambari.server.controller.spi.NoSuchResourceException, org.apache.ambari.server.controller.spi.NoSuchParentResourceException {
        modifyResources(getDeleteCommand(predicate));
        notifyDelete(org.apache.ambari.server.controller.spi.Resource.Type.ViewInstance, predicate);
        return getRequestStatus(null);
    }

    @java.lang.Override
    public java.util.Map<org.apache.ambari.server.controller.spi.Resource.Type, java.lang.String> getKeyPropertyIds() {
        return org.apache.ambari.server.controller.internal.LdapSyncEventResourceProvider.keyPropertyIds;
    }

    @java.lang.Override
    protected java.util.Set<java.lang.String> getPKPropertyIds() {
        return new java.util.HashSet<>(org.apache.ambari.server.controller.internal.LdapSyncEventResourceProvider.keyPropertyIds.values());
    }

    protected void ensureEventProcessor() {
        if (!processingEvents) {
            synchronized(eventQueue) {
                if (!processingEvents) {
                    processingEvents = true;
                    org.apache.ambari.server.controller.internal.LdapSyncEventResourceProvider.getExecutorService().submit(new java.lang.Runnable() {
                        @java.lang.Override
                        public void run() {
                            processSyncEvents();
                        }
                    });
                }
            }
        }
    }

    private org.apache.ambari.server.controller.spi.Resource toResource(org.apache.ambari.server.orm.entities.LdapSyncEventEntity eventEntity, java.util.Set<java.lang.String> requestedIds) {
        org.apache.ambari.server.controller.spi.Resource resource = new org.apache.ambari.server.controller.internal.ResourceImpl(org.apache.ambari.server.controller.spi.Resource.Type.LdapSyncEvent);
        org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.LdapSyncEventResourceProvider.EVENT_ID_PROPERTY_ID, eventEntity.getId(), requestedIds);
        org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.LdapSyncEventResourceProvider.EVENT_STATUS_PROPERTY_ID, eventEntity.getStatus().toString().toUpperCase(), requestedIds);
        org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.LdapSyncEventResourceProvider.EVENT_STATUS_DETAIL_PROPERTY_ID, eventEntity.getStatusDetail(), requestedIds);
        org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.LdapSyncEventResourceProvider.USERS_CREATED_PROPERTY_ID, eventEntity.getUsersCreated(), requestedIds);
        org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.LdapSyncEventResourceProvider.USERS_UPDATED_PROPERTY_ID, eventEntity.getUsersUpdated(), requestedIds);
        org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.LdapSyncEventResourceProvider.USERS_REMOVED_PROPERTY_ID, eventEntity.getUsersRemoved(), requestedIds);
        org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.LdapSyncEventResourceProvider.USERS_SKIPPED_PROPERTY_ID, eventEntity.getUsersSkipped(), requestedIds);
        org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.LdapSyncEventResourceProvider.GROUPS_CREATED_PROPERTY_ID, eventEntity.getGroupsCreated(), requestedIds);
        org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.LdapSyncEventResourceProvider.GROUPS_UPDATED_PROPERTY_ID, eventEntity.getGroupsUpdated(), requestedIds);
        org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.LdapSyncEventResourceProvider.GROUPS_REMOVED_PROPERTY_ID, eventEntity.getGroupsRemoved(), requestedIds);
        org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.LdapSyncEventResourceProvider.MEMBERSHIPS_CREATED_PROPERTY_ID, eventEntity.getMembershipsCreated(), requestedIds);
        org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.LdapSyncEventResourceProvider.MEMBERSHIPS_REMOVED_PROPERTY_ID, eventEntity.getMembershipsRemoved(), requestedIds);
        java.util.Set<java.util.Map<java.lang.String, java.lang.String>> specs = new java.util.HashSet<>();
        java.util.List<org.apache.ambari.server.orm.entities.LdapSyncSpecEntity> specList = eventEntity.getSpecs();
        for (org.apache.ambari.server.orm.entities.LdapSyncSpecEntity spec : specList) {
            java.util.Map<java.lang.String, java.lang.String> specMap = new java.util.HashMap<>();
            specMap.put(org.apache.ambari.server.controller.internal.LdapSyncEventResourceProvider.PRINCIPAL_TYPE_SPEC_KEY, spec.getPrincipalType().toString().toLowerCase());
            specMap.put(org.apache.ambari.server.controller.internal.LdapSyncEventResourceProvider.SYNC_TYPE_SPEC_KEY, spec.getSyncType().toString().toLowerCase());
            java.util.List<java.lang.String> names = spec.getPrincipalNames();
            if (!names.isEmpty()) {
                specMap.put(org.apache.ambari.server.controller.internal.LdapSyncEventResourceProvider.NAMES_SPEC_KEY, names.toString().replace("[", "").replace("]", "").replace(", ", ","));
            }
            specs.add(specMap);
        }
        org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.LdapSyncEventResourceProvider.EVENT_SPECS_PROPERTY_ID, specs, requestedIds);
        org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.LdapSyncEventResourceProvider.EVENT_START_TIME_PROPERTY_ID, eventEntity.getStartTime(), requestedIds);
        org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.LdapSyncEventResourceProvider.EVENT_END_TIME_PROPERTY_ID, eventEntity.getEndTime(), requestedIds);
        return resource;
    }

    private org.apache.ambari.server.orm.entities.LdapSyncEventEntity toEntity(java.util.Map<java.lang.String, java.lang.Object> properties) {
        org.apache.ambari.server.orm.entities.LdapSyncEventEntity entity = new org.apache.ambari.server.orm.entities.LdapSyncEventEntity(getNextEventId());
        java.util.List<org.apache.ambari.server.orm.entities.LdapSyncSpecEntity> specList = new java.util.LinkedList<>();
        java.util.Set<java.util.Map<java.lang.String, java.lang.String>> specs = ((java.util.Set<java.util.Map<java.lang.String, java.lang.String>>) (properties.get(org.apache.ambari.server.controller.internal.LdapSyncEventResourceProvider.EVENT_SPECS_PROPERTY_ID)));
        for (java.util.Map<java.lang.String, java.lang.String> specMap : specs) {
            org.apache.ambari.server.orm.entities.LdapSyncSpecEntity.SyncType syncType = null;
            org.apache.ambari.server.orm.entities.LdapSyncSpecEntity.PrincipalType principalType = null;
            boolean postProcessExistingUsers = false;
            java.util.List<java.lang.String> principalNames = java.util.Collections.emptyList();
            for (java.util.Map.Entry<java.lang.String, java.lang.String> entry : specMap.entrySet()) {
                java.lang.String key = entry.getKey();
                if (key.equalsIgnoreCase(org.apache.ambari.server.controller.internal.LdapSyncEventResourceProvider.PRINCIPAL_TYPE_SPEC_KEY)) {
                    principalType = org.apache.ambari.server.orm.entities.LdapSyncSpecEntity.PrincipalType.valueOfIgnoreCase(entry.getValue());
                } else if (key.equalsIgnoreCase(org.apache.ambari.server.controller.internal.LdapSyncEventResourceProvider.SYNC_TYPE_SPEC_KEY)) {
                    syncType = org.apache.ambari.server.orm.entities.LdapSyncSpecEntity.SyncType.valueOfIgnoreCase(entry.getValue());
                } else if (key.equalsIgnoreCase(org.apache.ambari.server.controller.internal.LdapSyncEventResourceProvider.NAMES_SPEC_KEY)) {
                    java.lang.String names = entry.getValue();
                    principalNames = java.util.Arrays.asList(names.split("\\s*,\\s*"));
                } else if (key.equalsIgnoreCase(org.apache.ambari.server.controller.internal.LdapSyncEventResourceProvider.POST_PROCESS_EXISTING_USERS_SPEC_KEY)) {
                    postProcessExistingUsers = "true".equalsIgnoreCase(entry.getValue());
                } else {
                    throw new java.lang.IllegalArgumentException(("Unknown spec key " + key) + ".");
                }
            }
            if ((syncType == null) || (principalType == null)) {
                throw new java.lang.IllegalArgumentException("LDAP event spec must include both sync-type and principal-type.");
            }
            org.apache.ambari.server.orm.entities.LdapSyncSpecEntity spec = new org.apache.ambari.server.orm.entities.LdapSyncSpecEntity(principalType, syncType, principalNames, postProcessExistingUsers);
            specList.add(spec);
        }
        entity.setSpecs(specList);
        return entity;
    }

    private long getNextEventId() {
        return nextEventId.getAndIncrement();
    }

    private org.apache.ambari.server.controller.internal.AbstractResourceProvider.Command<org.apache.ambari.server.orm.entities.LdapSyncEventEntity> getCreateCommand(final java.util.Map<java.lang.String, java.lang.Object> properties) {
        return new org.apache.ambari.server.controller.internal.AbstractResourceProvider.Command<org.apache.ambari.server.orm.entities.LdapSyncEventEntity>() {
            @java.lang.Override
            public org.apache.ambari.server.orm.entities.LdapSyncEventEntity invoke() throws org.apache.ambari.server.AmbariException, org.apache.ambari.server.security.authorization.AuthorizationException {
                org.apache.ambari.server.orm.entities.LdapSyncEventEntity eventEntity = toEntity(properties);
                for (org.apache.ambari.server.orm.entities.LdapSyncSpecEntity ldapSyncSpecEntity : eventEntity.getSpecs()) {
                    if (ldapSyncSpecEntity.getPrincipalType() == org.apache.ambari.server.orm.entities.LdapSyncSpecEntity.PrincipalType.USERS) {
                        if (!org.apache.ambari.server.security.authorization.AuthorizationHelper.isAuthorized(org.apache.ambari.server.security.authorization.ResourceType.AMBARI, null, org.apache.ambari.server.security.authorization.RoleAuthorization.AMBARI_MANAGE_USERS)) {
                            throw new org.apache.ambari.server.security.authorization.AuthorizationException("The uthenticated user is not authorized to syng LDAP users");
                        }
                    } else if (!org.apache.ambari.server.security.authorization.AuthorizationHelper.isAuthorized(org.apache.ambari.server.security.authorization.ResourceType.AMBARI, null, org.apache.ambari.server.security.authorization.RoleAuthorization.AMBARI_MANAGE_GROUPS)) {
                        throw new org.apache.ambari.server.security.authorization.AuthorizationException("The uthenticated user is not authorized to syng LDAP groups");
                    }
                }
                events.put(eventEntity.getId(), eventEntity);
                return eventEntity;
            }
        };
    }

    private org.apache.ambari.server.controller.internal.AbstractResourceProvider.Command<java.lang.Void> getDeleteCommand(final org.apache.ambari.server.controller.spi.Predicate predicate) {
        return new org.apache.ambari.server.controller.internal.AbstractResourceProvider.Command<java.lang.Void>() {
            @java.lang.Override
            public java.lang.Void invoke() throws org.apache.ambari.server.AmbariException {
                java.util.Set<java.lang.String> requestedIds = getRequestPropertyIds(org.apache.ambari.server.controller.utilities.PropertyHelper.getReadRequest(), predicate);
                java.util.Set<org.apache.ambari.server.orm.entities.LdapSyncEventEntity> entities = new java.util.HashSet<>();
                for (org.apache.ambari.server.orm.entities.LdapSyncEventEntity entity : events.values()) {
                    org.apache.ambari.server.controller.spi.Resource resource = toResource(entity, requestedIds);
                    if ((predicate == null) || predicate.evaluate(resource)) {
                        entities.add(entity);
                    }
                }
                for (org.apache.ambari.server.orm.entities.LdapSyncEventEntity entity : entities) {
                    events.remove(entity.getId());
                }
                return null;
            }
        };
    }

    private static synchronized java.util.concurrent.ExecutorService getExecutorService() {
        if (org.apache.ambari.server.controller.internal.LdapSyncEventResourceProvider.executorService == null) {
            java.util.concurrent.LinkedBlockingQueue<java.lang.Runnable> queue = new java.util.concurrent.LinkedBlockingQueue<>();
            java.util.concurrent.ThreadPoolExecutor threadPoolExecutor = new java.util.concurrent.ThreadPoolExecutor(org.apache.ambari.server.controller.internal.LdapSyncEventResourceProvider.THREAD_POOL_CORE_SIZE, org.apache.ambari.server.controller.internal.LdapSyncEventResourceProvider.THREAD_POOL_MAX_SIZE, org.apache.ambari.server.controller.internal.LdapSyncEventResourceProvider.THREAD_POOL_TIMEOUT, java.util.concurrent.TimeUnit.MILLISECONDS, queue);
            threadPoolExecutor.allowCoreThreadTimeOut(true);
            org.apache.ambari.server.controller.internal.LdapSyncEventResourceProvider.executorService = threadPoolExecutor;
        }
        return org.apache.ambari.server.controller.internal.LdapSyncEventResourceProvider.executorService;
    }

    private void processSyncEvents() {
        while (processingEvents) {
            org.apache.ambari.server.orm.entities.LdapSyncEventEntity event;
            synchronized(eventQueue) {
                if (processingEvents) {
                    event = eventQueue.poll();
                    if (event == null) {
                        processingEvents = false;
                        return;
                    }
                } else {
                    break;
                }
            }
            event.setStatus(org.apache.ambari.server.orm.entities.LdapSyncEventEntity.Status.RUNNING);
            event.setStatusDetail("Running LDAP sync.");
            event.setStartTime(java.lang.System.currentTimeMillis());
            try {
                populateLdapSyncEvent(event, syncLdap(event));
                event.setStatus(org.apache.ambari.server.orm.entities.LdapSyncEventEntity.Status.COMPLETE);
                event.setStatusDetail("Completed LDAP sync.");
            } catch (java.lang.Exception e) {
                event.setStatus(org.apache.ambari.server.orm.entities.LdapSyncEventEntity.Status.ERROR);
                java.lang.String msg = "Caught exception running LDAP sync. ";
                if (e.getCause() instanceof javax.naming.OperationNotSupportedException) {
                    msg += "LDAP server may not support search results pagination. " + "Try to turn the pagination off.";
                }
                event.setStatusDetail(msg + e.getMessage());
                org.apache.ambari.server.controller.internal.LdapSyncEventResourceProvider.LOG.error(msg, e);
            } finally {
                event.setEndTime(java.lang.System.currentTimeMillis());
            }
        } 
    }

    private org.apache.ambari.server.security.ldap.LdapBatchDto syncLdap(org.apache.ambari.server.orm.entities.LdapSyncEventEntity event) throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.server.controller.LdapSyncRequest userRequest = null;
        org.apache.ambari.server.controller.LdapSyncRequest groupRequest = null;
        for (org.apache.ambari.server.orm.entities.LdapSyncSpecEntity spec : event.getSpecs()) {
            switch (spec.getPrincipalType()) {
                case USERS :
                    userRequest = getLdapRequest(userRequest, spec);
                    break;
                case GROUPS :
                    groupRequest = getLdapRequest(groupRequest, spec);
                    break;
            }
        }
        return getManagementController().synchronizeLdapUsersAndGroups(userRequest, groupRequest);
    }

    private org.apache.ambari.server.controller.LdapSyncRequest getLdapRequest(org.apache.ambari.server.controller.LdapSyncRequest request, org.apache.ambari.server.orm.entities.LdapSyncSpecEntity spec) {
        switch (spec.getSyncType()) {
            case ALL :
                return new org.apache.ambari.server.controller.LdapSyncRequest(org.apache.ambari.server.orm.entities.LdapSyncSpecEntity.SyncType.ALL, spec.getPostProcessExistingUsers());
            case EXISTING :
                return new org.apache.ambari.server.controller.LdapSyncRequest(org.apache.ambari.server.orm.entities.LdapSyncSpecEntity.SyncType.EXISTING, spec.getPostProcessExistingUsers());
            case SPECIFIC :
                java.util.Set<java.lang.String> principalNames = new java.util.HashSet<>(spec.getPrincipalNames());
                if (request == null) {
                    request = new org.apache.ambari.server.controller.LdapSyncRequest(org.apache.ambari.server.orm.entities.LdapSyncSpecEntity.SyncType.SPECIFIC, principalNames, spec.getPostProcessExistingUsers());
                } else {
                    request.addPrincipalNames(principalNames);
                }
        }
        return request;
    }

    private void populateLdapSyncEvent(org.apache.ambari.server.orm.entities.LdapSyncEventEntity event, org.apache.ambari.server.security.ldap.LdapBatchDto syncInfo) {
        event.setUsersCreated(syncInfo.getUsersToBeCreated().size());
        event.setUsersUpdated(syncInfo.getUsersToBecomeLdap().size());
        event.setUsersRemoved(syncInfo.getUsersToBeRemoved().size());
        event.setUsersSkipped(syncInfo.getUsersSkipped().size());
        event.setGroupsCreated(syncInfo.getGroupsToBeCreated().size());
        event.setGroupsUpdated(syncInfo.getGroupsToBecomeLdap().size());
        event.setGroupsRemoved(syncInfo.getGroupsToBeRemoved().size());
        event.setMembershipsCreated(syncInfo.getMembershipToAdd().size());
        event.setMembershipsRemoved(syncInfo.getMembershipToRemove().size());
    }
}