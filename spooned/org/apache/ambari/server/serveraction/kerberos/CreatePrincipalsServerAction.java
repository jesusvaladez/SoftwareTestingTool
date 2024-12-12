package org.apache.ambari.server.serveraction.kerberos;
import org.apache.commons.lang.StringUtils;
@java.lang.SuppressWarnings("UnstableApiUsage")
public class CreatePrincipalsServerAction extends org.apache.ambari.server.serveraction.kerberos.KerberosServerAction {
    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.serveraction.kerberos.CreatePrincipalsServerAction.class);

    @com.google.inject.Inject
    private org.apache.ambari.server.orm.dao.KerberosPrincipalDAO kerberosPrincipalDAO;

    @com.google.inject.Inject
    private org.apache.ambari.server.security.SecurePasswordHelper securePasswordHelper;

    @com.google.inject.Inject
    private org.apache.ambari.server.orm.dao.KerberosKeytabPrincipalDAO kerberosKeytabPrincipalDAO;

    private com.google.common.util.concurrent.Striped<java.util.concurrent.locks.Lock> locksByPrincipal = com.google.common.util.concurrent.Striped.lazyWeakLock(25);

    private java.util.Set<java.lang.String> seenPrincipals = new java.util.HashSet<>();

    @java.lang.Override
    public org.apache.ambari.server.agent.CommandReport execute(java.util.concurrent.ConcurrentMap<java.lang.String, java.lang.Object> requestSharedDataContext) throws org.apache.ambari.server.AmbariException, java.lang.InterruptedException {
        return processIdentities(requestSharedDataContext);
    }

    @java.lang.Override
    protected org.apache.ambari.server.agent.CommandReport processIdentity(org.apache.ambari.server.serveraction.kerberos.stageutils.ResolvedKerberosPrincipal resolvedPrincipal, org.apache.ambari.server.serveraction.kerberos.KerberosOperationHandler operationHandler, java.util.Map<java.lang.String, java.lang.String> kerberosConfiguration, boolean includedInFilter, java.util.Map<java.lang.String, java.lang.Object> requestSharedDataContext) throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.server.agent.CommandReport commandReport = null;
        if (!seenPrincipals.contains(resolvedPrincipal.getPrincipal())) {
            seenPrincipals.add(resolvedPrincipal.getPrincipal());
            boolean processPrincipal;
            org.apache.ambari.server.orm.entities.KerberosPrincipalEntity kerberosPrincipalEntity = kerberosPrincipalDAO.find(resolvedPrincipal.getPrincipal());
            boolean regenerateKeytabs = org.apache.ambari.server.serveraction.kerberos.KerberosServerAction.getOperationType(getCommandParameters()) == org.apache.ambari.server.serveraction.kerberos.KerberosServerAction.OperationType.RECREATE_ALL;
            boolean servicePrincipal = resolvedPrincipal.isService();
            if (!includedInFilter) {
                regenerateKeytabs = false;
            }
            if (regenerateKeytabs) {
                processPrincipal = (!hasHostFilters()) || servicePrincipal;
            } else if (kerberosPrincipalEntity == null) {
                processPrincipal = true;
            } else if (!org.apache.commons.lang.StringUtils.isEmpty(kerberosPrincipalEntity.getCachedKeytabPath())) {
                java.io.File file = new java.io.File(kerberosPrincipalEntity.getCachedKeytabPath());
                processPrincipal = !file.exists();
            } else {
                processPrincipal = true;
            }
            if (processPrincipal) {
                java.util.Map<java.lang.String, java.lang.String> principalPasswordMap = org.apache.ambari.server.serveraction.kerberos.KerberosServerAction.getPrincipalPasswordMap(requestSharedDataContext);
                java.lang.String principal = resolvedPrincipal.getPrincipal();
                java.util.concurrent.locks.Lock lock = locksByPrincipal.get(principal);
                lock.lock();
                java.lang.String password = principalPasswordMap.get(principal);
                try {
                    if (password == null) {
                        org.apache.ambari.server.serveraction.kerberos.CreatePrincipalsServerAction.CreatePrincipalResult result = createPrincipal(resolvedPrincipal.getPrincipal(), servicePrincipal, kerberosConfiguration, operationHandler, regenerateKeytabs, actionLog);
                        if (result == null) {
                            commandReport = createCommandReport(1, org.apache.ambari.server.actionmanager.HostRoleStatus.FAILED, "{}", actionLog.getStdOut(), actionLog.getStdErr());
                        } else {
                            java.util.Map<java.lang.String, java.lang.Integer> principalKeyNumberMap = org.apache.ambari.server.serveraction.kerberos.KerberosServerAction.getPrincipalKeyNumberMap(requestSharedDataContext);
                            principalPasswordMap.put(resolvedPrincipal.getPrincipal(), result.getPassword());
                            principalKeyNumberMap.put(resolvedPrincipal.getPrincipal(), result.getKeyNumber());
                            for (org.apache.ambari.server.orm.entities.KerberosKeytabPrincipalEntity kkpe : kerberosKeytabPrincipalDAO.findByPrincipal(resolvedPrincipal.getPrincipal())) {
                                kkpe.setDistributed(false);
                                kerberosKeytabPrincipalDAO.merge(kkpe);
                            }
                            org.apache.ambari.server.orm.entities.KerberosPrincipalEntity principalEntity = kerberosPrincipalDAO.find(resolvedPrincipal.getPrincipal());
                            try {
                                new java.io.File(principalEntity.getCachedKeytabPath()).delete();
                            } catch (java.lang.Exception e) {
                                org.apache.ambari.server.serveraction.kerberos.CreatePrincipalsServerAction.LOG.debug("Failed to delete cache file '{}'", principalEntity.getCachedKeytabPath());
                            }
                            principalEntity.setCachedKeytabPath(null);
                            kerberosPrincipalDAO.merge(principalEntity);
                        }
                    }
                } finally {
                    lock.unlock();
                }
            }
        }
        return commandReport;
    }

    public org.apache.ambari.server.serveraction.kerberos.CreatePrincipalsServerAction.CreatePrincipalResult createPrincipal(java.lang.String principal, boolean isServicePrincipal, java.util.Map<java.lang.String, java.lang.String> kerberosConfiguration, org.apache.ambari.server.serveraction.kerberos.KerberosOperationHandler kerberosOperationHandler, boolean regenerateKeytabs, org.apache.ambari.server.serveraction.ActionLog actionLog) {
        org.apache.ambari.server.audit.event.kerberos.CreatePrincipalKerberosAuditEvent.CreatePrincipalKerberosAuditEventBuilder auditEventBuilder = org.apache.ambari.server.audit.event.kerberos.CreatePrincipalKerberosAuditEvent.builder().withTimestamp(java.lang.System.currentTimeMillis()).withRequestId(getHostRoleCommand() != null ? getHostRoleCommand().getRequestId() : -1).withTaskId(getHostRoleCommand() != null ? getHostRoleCommand().getTaskId() : -1).withPrincipal(principal);
        org.apache.ambari.server.serveraction.kerberos.CreatePrincipalsServerAction.CreatePrincipalResult result = null;
        java.lang.String message = null;
        try {
            message = java.lang.String.format("Processing principal, %s", principal);
            org.apache.ambari.server.serveraction.kerberos.CreatePrincipalsServerAction.LOG.info(message);
            if (actionLog != null) {
                actionLog.writeStdOut(message);
            }
            java.lang.Integer length;
            java.lang.Integer minLowercaseLetters;
            java.lang.Integer minUppercaseLetters;
            java.lang.Integer minDigits;
            java.lang.Integer minPunctuation;
            java.lang.Integer minWhitespace;
            if (kerberosConfiguration == null) {
                length = null;
                minLowercaseLetters = null;
                minUppercaseLetters = null;
                minDigits = null;
                minPunctuation = null;
                minWhitespace = null;
            } else {
                length = org.apache.ambari.server.serveraction.kerberos.CreatePrincipalsServerAction.toInt(kerberosConfiguration.get("password_length"));
                minLowercaseLetters = org.apache.ambari.server.serveraction.kerberos.CreatePrincipalsServerAction.toInt(kerberosConfiguration.get("password_min_lowercase_letters"));
                minUppercaseLetters = org.apache.ambari.server.serveraction.kerberos.CreatePrincipalsServerAction.toInt(kerberosConfiguration.get("password_min_uppercase_letters"));
                minDigits = org.apache.ambari.server.serveraction.kerberos.CreatePrincipalsServerAction.toInt(kerberosConfiguration.get("password_min_digits"));
                minPunctuation = org.apache.ambari.server.serveraction.kerberos.CreatePrincipalsServerAction.toInt(kerberosConfiguration.get("password_min_punctuation"));
                minWhitespace = org.apache.ambari.server.serveraction.kerberos.CreatePrincipalsServerAction.toInt(kerberosConfiguration.get("password_min_whitespace"));
            }
            java.lang.String password = securePasswordHelper.createSecurePassword(length, minLowercaseLetters, minUppercaseLetters, minDigits, minPunctuation, minWhitespace);
            try {
                boolean created;
                java.lang.Integer keyNumber;
                if (regenerateKeytabs) {
                    try {
                        keyNumber = kerberosOperationHandler.setPrincipalPassword(principal, password, isServicePrincipal);
                        created = false;
                    } catch (org.apache.ambari.server.serveraction.kerberos.KerberosPrincipalDoesNotExistException e) {
                        message = java.lang.String.format("Principal, %s, does not exist, creating new principal", principal);
                        org.apache.ambari.server.serveraction.kerberos.CreatePrincipalsServerAction.LOG.warn(message);
                        if (actionLog != null) {
                            actionLog.writeStdOut(message);
                        }
                        keyNumber = kerberosOperationHandler.createPrincipal(principal, password, isServicePrincipal);
                        created = true;
                    }
                } else {
                    try {
                        keyNumber = kerberosOperationHandler.createPrincipal(principal, password, isServicePrincipal);
                        created = true;
                    } catch (org.apache.ambari.server.serveraction.kerberos.KerberosPrincipalAlreadyExistsException e) {
                        message = java.lang.String.format("Principal, %s, already exists, setting new password", principal);
                        org.apache.ambari.server.serveraction.kerberos.CreatePrincipalsServerAction.LOG.warn(message);
                        if (actionLog != null) {
                            actionLog.writeStdOut(message);
                        }
                        keyNumber = kerberosOperationHandler.setPrincipalPassword(principal, password, isServicePrincipal);
                        created = false;
                    }
                }
                if (keyNumber != null) {
                    result = new org.apache.ambari.server.serveraction.kerberos.CreatePrincipalsServerAction.CreatePrincipalResult(principal, password, keyNumber);
                    if (created) {
                        message = java.lang.String.format("Successfully created new principal, %s", principal);
                    } else {
                        message = java.lang.String.format("Successfully set password for %s", principal);
                    }
                    org.apache.ambari.server.serveraction.kerberos.CreatePrincipalsServerAction.LOG.debug(message);
                } else {
                    if (created) {
                        message = java.lang.String.format("Failed to create principal, %s - unknown reason", principal);
                    } else {
                        message = java.lang.String.format("Failed to set password for %s - unknown reason", principal);
                    }
                    org.apache.ambari.server.serveraction.kerberos.CreatePrincipalsServerAction.LOG.error(message);
                    if (actionLog != null) {
                        actionLog.writeStdErr(message);
                    }
                }
                if (!kerberosPrincipalDAO.exists(principal)) {
                    kerberosPrincipalDAO.create(principal, isServicePrincipal);
                }
            } catch (org.apache.ambari.server.serveraction.kerberos.KerberosOperationException e) {
                message = java.lang.String.format("Failed to create principal, %s - %s", principal, e.getMessage());
                org.apache.ambari.server.serveraction.kerberos.CreatePrincipalsServerAction.LOG.error(message, e);
                if (actionLog != null) {
                    actionLog.writeStdErr(message);
                }
            }
        } finally {
            if (result == null) {
                auditEventBuilder.withReasonOfFailure(message == null ? "Unknown error" : message);
            }
            auditLog(auditEventBuilder.build());
        }
        return result;
    }

    private static java.lang.Integer toInt(java.lang.String string) {
        if ((string == null) || string.isEmpty()) {
            return null;
        } else {
            try {
                return java.lang.Integer.parseInt(string);
            } catch (java.lang.NumberFormatException e) {
                return null;
            }
        }
    }

    public static class CreatePrincipalResult {
        private final java.lang.String principal;

        private final java.lang.String password;

        private final java.lang.Integer keyNumber;

        public CreatePrincipalResult(java.lang.String principal, java.lang.String password, java.lang.Integer keyNumber) {
            this.principal = principal;
            this.password = password;
            this.keyNumber = keyNumber;
        }

        public java.lang.String getPrincipal() {
            return principal;
        }

        public java.lang.String getPassword() {
            return password;
        }

        public java.lang.Integer getKeyNumber() {
            return keyNumber;
        }
    }
}