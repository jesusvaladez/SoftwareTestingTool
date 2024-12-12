package org.apache.ambari.server.serveraction.kerberos;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
public class DestroyPrincipalsServerAction extends org.apache.ambari.server.serveraction.kerberos.KerberosServerAction {
    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.serveraction.kerberos.DestroyPrincipalsServerAction.class);

    @com.google.inject.Inject
    private org.apache.ambari.server.serveraction.kerberos.KerberosOperationHandlerFactory kerberosOperationHandlerFactory;

    @com.google.inject.Inject
    private org.apache.ambari.server.controller.KerberosHelper kerberosHelper;

    @com.google.inject.Inject
    private org.apache.ambari.server.orm.dao.KerberosPrincipalDAO kerberosPrincipalDAO;

    @com.google.inject.Inject
    private org.apache.ambari.server.orm.dao.KerberosKeytabPrincipalDAO kerberosKeytabPrincipalDAO;

    @com.google.inject.Inject
    private org.apache.ambari.server.orm.dao.KerberosKeytabDAO kerberosKeytabDAO;

    private java.util.Set<java.lang.String> seenPrincipals = new java.util.HashSet<>();

    @java.lang.Override
    public org.apache.ambari.server.agent.CommandReport execute(java.util.concurrent.ConcurrentMap<java.lang.String, java.lang.Object> requestSharedDataContext) throws org.apache.ambari.server.AmbariException, java.lang.InterruptedException {
        java.util.Map<java.lang.String, java.lang.String> commandParameters = getCommandParameters();
        org.apache.ambari.server.serveraction.kerberos.KDCType kdcType = org.apache.ambari.server.serveraction.kerberos.KerberosServerAction.getKDCType(commandParameters);
        org.apache.ambari.server.security.credential.PrincipalKeyCredential administratorCredential = kerberosHelper.getKDCAdministratorCredentials(getClusterName());
        java.lang.String defaultRealm = org.apache.ambari.server.serveraction.kerberos.KerberosServerAction.getDefaultRealm(commandParameters);
        org.apache.ambari.server.serveraction.kerberos.KerberosOperationHandler operationHandler = kerberosOperationHandlerFactory.getKerberosOperationHandler(kdcType);
        java.util.Map<java.lang.String, java.lang.String> kerberosConfiguration = getConfigurationProperties("kerberos-env");
        try {
            operationHandler.open(administratorCredential, defaultRealm, kerberosConfiguration);
        } catch (org.apache.ambari.server.serveraction.kerberos.KerberosOperationException e) {
            java.lang.String message = java.lang.String.format("Failed to process the identities, could not properly open the KDC operation handler: %s", e.getMessage());
            actionLog.writeStdErr(message);
            org.apache.ambari.server.serveraction.kerberos.DestroyPrincipalsServerAction.LOG.error(message);
            throw new org.apache.ambari.server.AmbariException(message, e);
        }
        actionLog.writeStdOut("Cleaning up Kerberos identities.");
        java.util.Map<java.lang.String, ? extends java.util.Collection<java.lang.String>> serviceComponentFilter = getServiceComponentFilter();
        java.util.Set<java.lang.String> hostFilter = getHostFilter();
        java.util.Collection<java.lang.String> principalNameFilter = getIdentityFilter();
        java.util.List<org.apache.ambari.server.orm.entities.KerberosKeytabPrincipalEntity> kerberosKeytabPrincipalEntities;
        if ((org.apache.commons.collections.MapUtils.isEmpty(serviceComponentFilter) && org.apache.commons.collections.CollectionUtils.isEmpty(hostFilter)) && org.apache.commons.collections.CollectionUtils.isEmpty(principalNameFilter)) {
            kerberosKeytabPrincipalEntities = kerberosKeytabPrincipalDAO.findAll();
        } else {
            java.util.ArrayList<org.apache.ambari.server.orm.dao.KerberosKeytabPrincipalDAO.KerberosKeytabPrincipalFilter> filters = new java.util.ArrayList<>();
            if (org.apache.commons.collections.MapUtils.isEmpty(serviceComponentFilter)) {
                filters.add(org.apache.ambari.server.orm.dao.KerberosKeytabPrincipalDAO.KerberosKeytabPrincipalFilter.createFilter(null, null, hostFilter, principalNameFilter));
            } else {
                for (java.util.Map.Entry<java.lang.String, ? extends java.util.Collection<java.lang.String>> entry : serviceComponentFilter.entrySet()) {
                    filters.add(org.apache.ambari.server.orm.dao.KerberosKeytabPrincipalDAO.KerberosKeytabPrincipalFilter.createFilter(entry.getKey(), entry.getValue(), hostFilter, principalNameFilter));
                }
            }
            kerberosKeytabPrincipalEntities = kerberosKeytabPrincipalDAO.findByFilters(filters);
        }
        if (kerberosKeytabPrincipalEntities != null) {
            try {
                java.util.Set<java.lang.Long> visitedKKPID = new java.util.HashSet<>();
                for (org.apache.ambari.server.orm.entities.KerberosKeytabPrincipalEntity kerberosKeytabPrincipalEntity : kerberosKeytabPrincipalEntities) {
                    if (!visitedKKPID.contains(kerberosKeytabPrincipalEntity.getKkpId())) {
                        visitedKKPID.add(kerberosKeytabPrincipalEntity.getKkpId());
                        org.apache.ambari.server.orm.entities.KerberosKeytabEntity kerberosKeytabEntity = kerberosKeytabPrincipalEntity.getKerberosKeytabEntity();
                        org.apache.ambari.server.orm.entities.KerberosPrincipalEntity kerberosPrincipalEntity = kerberosKeytabPrincipalEntity.getKerberosPrincipalEntity();
                        if (serviceComponentFilter == null) {
                            kerberosKeytabPrincipalEntity.setServiceMapping(null);
                        } else {
                            java.util.List<org.apache.ambari.server.orm.entities.KerberosKeytabServiceMappingEntity> serviceMapping = kerberosKeytabPrincipalEntity.getServiceMapping();
                            if (org.apache.commons.collections.CollectionUtils.isNotEmpty(serviceMapping)) {
                                java.util.Iterator<org.apache.ambari.server.orm.entities.KerberosKeytabServiceMappingEntity> iterator = serviceMapping.iterator();
                                while (iterator.hasNext()) {
                                    org.apache.ambari.server.orm.entities.KerberosKeytabServiceMappingEntity entity = iterator.next();
                                    if (serviceComponentFilter.containsKey(entity.getServiceName())) {
                                        java.util.Collection<java.lang.String> components = serviceComponentFilter.get(entity.getServiceName());
                                        if (org.apache.commons.collections.CollectionUtils.isEmpty(components) || components.contains(entity.getComponentName())) {
                                            iterator.remove();
                                        }
                                    }
                                } 
                                kerberosKeytabPrincipalEntity.setServiceMapping(serviceMapping);
                            }
                        }
                        kerberosKeytabPrincipalEntity = kerberosKeytabPrincipalDAO.merge(kerberosKeytabPrincipalEntity);
                        if (org.apache.commons.collections.CollectionUtils.isEmpty(kerberosKeytabPrincipalEntity.getServiceMapping())) {
                            kerberosKeytabPrincipalDAO.remove(kerberosKeytabPrincipalEntity);
                            if (org.apache.ambari.server.serveraction.kerberos.DestroyPrincipalsServerAction.LOG.isDebugEnabled()) {
                                org.apache.ambari.server.serveraction.kerberos.DestroyPrincipalsServerAction.LOG.debug("Cleaning up keytab/principal entry: {}:{}:{}:{}", kerberosKeytabPrincipalEntity.getKkpId(), kerberosKeytabEntity.getKeytabPath(), kerberosPrincipalEntity.getPrincipalName(), kerberosKeytabPrincipalEntity.getHostName());
                            } else {
                                org.apache.ambari.server.serveraction.kerberos.DestroyPrincipalsServerAction.LOG.info("Cleaning up keytab/principal entry: {}:{}:{}", kerberosKeytabEntity.getKeytabPath(), kerberosPrincipalEntity.getPrincipalName(), kerberosKeytabPrincipalEntity.getHostName());
                            }
                            kerberosKeytabEntity.getKerberosKeytabPrincipalEntities().remove(kerberosKeytabPrincipalEntity);
                            kerberosKeytabEntity = kerberosKeytabDAO.merge(kerberosKeytabEntity);
                            kerberosPrincipalEntity.getKerberosKeytabPrincipalEntities().remove(kerberosKeytabPrincipalEntity);
                            kerberosPrincipalEntity = kerberosPrincipalDAO.merge(kerberosPrincipalEntity);
                        }
                        if (kerberosKeytabDAO.removeIfNotReferenced(kerberosKeytabEntity)) {
                            java.lang.String message = java.lang.String.format("Cleaning up keytab entry: %s", kerberosKeytabEntity.getKeytabPath());
                            org.apache.ambari.server.serveraction.kerberos.DestroyPrincipalsServerAction.LOG.info(message);
                            actionLog.writeStdOut(message);
                        }
                        if (kerberosPrincipalDAO.removeIfNotReferenced(kerberosPrincipalEntity)) {
                            java.lang.String message = java.lang.String.format("Cleaning up principal entry: %s", kerberosPrincipalEntity.getPrincipalName());
                            org.apache.ambari.server.serveraction.kerberos.DestroyPrincipalsServerAction.LOG.info(message);
                            actionLog.writeStdOut(message);
                            destroyIdentity(operationHandler, kerberosPrincipalEntity);
                        }
                    }
                }
            } finally {
                try {
                    operationHandler.close();
                } catch (org.apache.ambari.server.serveraction.kerberos.KerberosOperationException e) {
                }
            }
        }
        return createCommandReport(0, org.apache.ambari.server.actionmanager.HostRoleStatus.COMPLETED, "{}", actionLog.getStdOut(), actionLog.getStdErr());
    }

    @java.lang.Override
    protected boolean pruneServiceFilter() {
        return false;
    }

    @java.lang.Override
    protected org.apache.ambari.server.agent.CommandReport processIdentity(org.apache.ambari.server.serveraction.kerberos.stageutils.ResolvedKerberosPrincipal resolvedPrincipal, org.apache.ambari.server.serveraction.kerberos.KerberosOperationHandler operationHandler, java.util.Map<java.lang.String, java.lang.String> kerberosConfiguration, boolean includedInFilter, java.util.Map<java.lang.String, java.lang.Object> requestSharedDataContext) throws org.apache.ambari.server.AmbariException {
        throw new java.lang.UnsupportedOperationException();
    }

    private void destroyIdentity(org.apache.ambari.server.serveraction.kerberos.KerberosOperationHandler operationHandler, org.apache.ambari.server.orm.entities.KerberosPrincipalEntity kerberosPrincipalEntity) {
        java.lang.String principalName = kerberosPrincipalEntity.getPrincipalName();
        java.lang.String message = java.lang.String.format("Destroying identity, %s", principalName);
        org.apache.ambari.server.serveraction.kerberos.DestroyPrincipalsServerAction.LOG.info(message);
        actionLog.writeStdOut(message);
        org.apache.ambari.server.audit.event.kerberos.DestroyPrincipalKerberosAuditEvent.DestroyPrincipalKerberosAuditEventBuilder auditEventBuilder = org.apache.ambari.server.audit.event.kerberos.DestroyPrincipalKerberosAuditEvent.builder().withTimestamp(java.lang.System.currentTimeMillis()).withRequestId(getHostRoleCommand().getRequestId()).withTaskId(getHostRoleCommand().getTaskId()).withPrincipal(principalName);
        try {
            try {
                operationHandler.removePrincipal(principalName, kerberosPrincipalEntity.isService());
            } catch (org.apache.ambari.server.serveraction.kerberos.KerberosOperationException e) {
                message = java.lang.String.format("Failed to remove identity for %s from the KDC - %s", principalName, e.getMessage());
                org.apache.ambari.server.serveraction.kerberos.DestroyPrincipalsServerAction.LOG.warn(message, e);
                actionLog.writeStdErr(message);
                auditEventBuilder.withReasonOfFailure(message);
            }
            try {
                org.apache.ambari.server.orm.entities.KerberosPrincipalEntity principalEntity = kerberosPrincipalDAO.find(principalName);
                if (principalEntity != null) {
                    java.lang.String cachedKeytabPath = principalEntity.getCachedKeytabPath();
                    if (cachedKeytabPath != null) {
                        if (!new java.io.File(cachedKeytabPath).delete()) {
                            org.apache.ambari.server.serveraction.kerberos.DestroyPrincipalsServerAction.LOG.debug("Failed to remove cached keytab for {}", principalName);
                        }
                    }
                }
            } catch (java.lang.Throwable t) {
                message = java.lang.String.format("Failed to remove identity for %s from the Ambari database - %s", principalName, t.getMessage());
                org.apache.ambari.server.serveraction.kerberos.DestroyPrincipalsServerAction.LOG.warn(message, t);
                actionLog.writeStdErr(message);
                auditEventBuilder.withReasonOfFailure(message);
            }
        } finally {
            auditLog(auditEventBuilder.build());
        }
    }
}