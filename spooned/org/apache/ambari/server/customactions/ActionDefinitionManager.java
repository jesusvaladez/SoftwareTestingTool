package org.apache.ambari.server.customactions;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.UnmarshalException;
import javax.xml.bind.Unmarshaller;
import org.apache.commons.lang.StringUtils;
public class ActionDefinitionManager {
    public static final java.lang.Integer MIN_TIMEOUT = 60;

    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.customactions.ActionDefinitionManager.class);

    private static final java.util.Map<java.lang.Class<?>, javax.xml.bind.JAXBContext> _jaxbContexts = new java.util.HashMap<>();

    private static final java.lang.Integer MAX_TIMEOUT = java.lang.Integer.MAX_VALUE - 1;

    static {
        try {
            javax.xml.bind.JAXBContext ctx = javax.xml.bind.JAXBContext.newInstance(org.apache.ambari.server.customactions.ActionDefinitionXml.class);
            _jaxbContexts.put(org.apache.ambari.server.customactions.ActionDefinitionXml.class, ctx);
        } catch (javax.xml.bind.JAXBException e) {
            throw new java.lang.RuntimeException(e);
        }
    }

    private final java.util.Map<java.lang.String, org.apache.ambari.server.customactions.ActionDefinition> actionDefinitionMap = new java.util.HashMap<>();

    public ActionDefinitionManager() {
    }

    public static <T> T unmarshal(java.lang.Class<T> clz, java.io.File file) throws javax.xml.bind.JAXBException {
        javax.xml.bind.Unmarshaller u = org.apache.ambari.server.customactions.ActionDefinitionManager._jaxbContexts.get(clz).createUnmarshaller();
        return clz.cast(u.unmarshal(file));
    }

    private <E extends java.lang.Enum<E>> E safeValueOf(java.lang.Class<E> enumm, java.lang.String s, java.lang.StringBuilder reason) {
        if ((s == null) || (s.length() == 0)) {
            return null;
        }
        try {
            return java.lang.Enum.valueOf(enumm, s);
        } catch (java.lang.IllegalArgumentException iaex) {
            reason.append("Invalid value provided for ").append(enumm.getName());
            return null;
        }
    }

    public void readCustomActionDefinitions(java.io.File customActionDefinitionRoot) throws javax.xml.bind.JAXBException, org.apache.ambari.server.AmbariException {
        if (((customActionDefinitionRoot == null) || (!customActionDefinitionRoot.exists())) || (!customActionDefinitionRoot.canRead())) {
            org.apache.ambari.server.customactions.ActionDefinitionManager.LOG.warn("Cannot read custom action definitions. " + (customActionDefinitionRoot == null ? "" : "Check path " + customActionDefinitionRoot.getAbsolutePath()));
        }
        java.io.File[] customActionDefinitionFiles = customActionDefinitionRoot.listFiles(org.apache.ambari.server.stack.StackDirectory.FILENAME_FILTER);
        if (customActionDefinitionFiles != null) {
            for (java.io.File definitionFile : customActionDefinitionFiles) {
                org.apache.ambari.server.customactions.ActionDefinitionXml adx = null;
                try {
                    adx = org.apache.ambari.server.customactions.ActionDefinitionManager.unmarshal(org.apache.ambari.server.customactions.ActionDefinitionXml.class, definitionFile);
                } catch (javax.xml.bind.UnmarshalException uex) {
                    org.apache.ambari.server.customactions.ActionDefinitionManager.LOG.warn("Encountered badly formed action definition file - " + definitionFile.getAbsolutePath());
                    continue;
                }
                for (org.apache.ambari.server.customactions.ActionDefinitionSpec ad : adx.actionDefinitions()) {
                    org.apache.ambari.server.customactions.ActionDefinitionManager.LOG.debug("Read action definition = {}", ad);
                    java.lang.StringBuilder errorReason = new java.lang.StringBuilder("Error while parsing action definition. ").append(ad).append(" --- ");
                    org.apache.ambari.server.actionmanager.TargetHostType targetType = safeValueOf(org.apache.ambari.server.actionmanager.TargetHostType.class, ad.getTargetType(), errorReason);
                    org.apache.ambari.server.actionmanager.ActionType actionType = safeValueOf(org.apache.ambari.server.actionmanager.ActionType.class, ad.getActionType(), errorReason);
                    java.lang.Integer defaultTimeout = org.apache.ambari.server.customactions.ActionDefinitionManager.MIN_TIMEOUT;
                    if ((ad.getDefaultTimeout() != null) && (!ad.getDefaultTimeout().isEmpty())) {
                        defaultTimeout = java.lang.Integer.parseInt(ad.getDefaultTimeout());
                    }
                    if (isValidActionDefinition(ad, actionType, defaultTimeout, errorReason)) {
                        java.lang.String actionName = ad.getActionName();
                        if (actionDefinitionMap.containsKey(actionName)) {
                            org.apache.ambari.server.customactions.ActionDefinitionManager.LOG.warn("Ignoring action definition as a different definition by that name already exists. " + ad);
                            continue;
                        }
                        actionDefinitionMap.put(ad.getActionName(), new org.apache.ambari.server.customactions.ActionDefinition(ad.getActionName(), actionType, ad.getInputs(), ad.getTargetService(), ad.getTargetComponent(), ad.getDescription(), targetType, defaultTimeout, translatePermissions(ad.getPermissions())));
                        org.apache.ambari.server.customactions.ActionDefinitionManager.LOG.info("Added custom action definition for " + ad.getActionName());
                    } else {
                        org.apache.ambari.server.customactions.ActionDefinitionManager.LOG.warn(errorReason.toString());
                    }
                }
            }
        }
    }

    private boolean isValidActionDefinition(org.apache.ambari.server.customactions.ActionDefinitionSpec ad, org.apache.ambari.server.actionmanager.ActionType actionType, java.lang.Integer defaultTimeout, java.lang.StringBuilder reason) {
        if (isValidActionName(ad.getActionName(), reason)) {
            if ((defaultTimeout < org.apache.ambari.server.customactions.ActionDefinitionManager.MIN_TIMEOUT) || (defaultTimeout > org.apache.ambari.server.customactions.ActionDefinitionManager.MAX_TIMEOUT)) {
                reason.append((("Default timeout should be between " + org.apache.ambari.server.customactions.ActionDefinitionManager.MIN_TIMEOUT) + " and ") + org.apache.ambari.server.customactions.ActionDefinitionManager.MAX_TIMEOUT);
                return false;
            }
            if ((actionType == null) || (actionType == org.apache.ambari.server.actionmanager.ActionType.SYSTEM_DISABLED)) {
                reason.append("Action type cannot be ").append(actionType);
                return false;
            }
            if ((ad.getDescription() == null) || ad.getDescription().isEmpty()) {
                reason.append("Action description cannot be empty");
                return false;
            }
            if ((ad.getTargetService() == null) || ad.getTargetService().isEmpty()) {
                if ((ad.getTargetComponent() != null) && (!ad.getTargetComponent().isEmpty())) {
                    reason.append("Target component cannot be specified unless target service is specified");
                    return false;
                }
            }
            if ((ad.getInputs() != null) && (!ad.getInputs().isEmpty())) {
                java.lang.String[] parameters = ad.getInputs().split(",");
                for (java.lang.String parameter : parameters) {
                    if (parameter.trim().isEmpty()) {
                        reason.append("Empty parameter cannot be specified as an input parameter");
                    }
                }
            }
        } else {
            return false;
        }
        return true;
    }

    public java.util.List<org.apache.ambari.server.customactions.ActionDefinition> getAllActionDefinition() {
        return new java.util.ArrayList<>(actionDefinitionMap.values());
    }

    public org.apache.ambari.server.customactions.ActionDefinition getActionDefinition(java.lang.String name) {
        return actionDefinitionMap.get(name);
    }

    public void addActionDefinition(org.apache.ambari.server.customactions.ActionDefinition ad) throws org.apache.ambari.server.AmbariException {
        if (!actionDefinitionMap.containsKey(ad.getActionName())) {
            actionDefinitionMap.put(ad.getActionName(), ad);
        } else {
            throw new org.apache.ambari.server.AmbariException(("Action definition by name " + ad.getActionName()) + " already exists.");
        }
    }

    private boolean isValidActionName(java.lang.String actionName, java.lang.StringBuilder reason) {
        if ((actionName == null) || actionName.isEmpty()) {
            reason.append("Action name cannot be empty");
            return false;
        }
        java.lang.String trimmedName = actionName.replaceAll("\\s+", "");
        if (actionName.length() > trimmedName.length()) {
            reason.append("Action name cannot contain white spaces");
            return false;
        }
        return true;
    }

    private java.util.Set<org.apache.ambari.server.security.authorization.RoleAuthorization> translatePermissions(java.lang.String permissions) {
        if (org.apache.commons.lang.StringUtils.isEmpty(permissions)) {
            return null;
        } else {
            java.util.Set<org.apache.ambari.server.security.authorization.RoleAuthorization> authorizations = new java.util.HashSet<>();
            java.lang.String[] parts = permissions.split(",");
            for (java.lang.String permission : parts) {
                org.apache.ambari.server.security.authorization.RoleAuthorization authorization = org.apache.ambari.server.security.authorization.RoleAuthorization.translate(permission);
                if (authorization != null) {
                    authorizations.add(authorization);
                }
            }
            return authorizations.isEmpty() ? null : java.util.EnumSet.copyOf(authorizations);
        }
    }
}