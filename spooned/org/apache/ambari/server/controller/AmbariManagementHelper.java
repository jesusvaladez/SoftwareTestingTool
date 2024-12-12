package org.apache.ambari.server.controller;
import javax.persistence.RollbackException;
import org.apache.commons.lang.StringUtils;
@com.google.inject.Singleton
public class AmbariManagementHelper {
    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.controller.AmbariManagementHelper.class);

    private org.apache.ambari.server.orm.dao.ExtensionLinkDAO linkDAO;

    private org.apache.ambari.server.orm.dao.ExtensionDAO extensionDAO;

    private org.apache.ambari.server.orm.dao.StackDAO stackDAO;

    @com.google.inject.Inject
    public AmbariManagementHelper(org.apache.ambari.server.orm.dao.StackDAO stackDAO, org.apache.ambari.server.orm.dao.ExtensionDAO extensionDAO, org.apache.ambari.server.orm.dao.ExtensionLinkDAO linkDAO) {
        this.stackDAO = stackDAO;
        this.extensionDAO = extensionDAO;
        this.linkDAO = linkDAO;
    }

    public void createExtensionLink(org.apache.ambari.server.stack.StackManager stackManager, org.apache.ambari.server.state.StackInfo stackInfo, org.apache.ambari.server.state.ExtensionInfo extensionInfo) throws org.apache.ambari.server.AmbariException {
        validateCreateExtensionLinkRequest(stackInfo, extensionInfo);
        org.apache.ambari.server.stack.ExtensionHelper.validateCreateLink(stackManager, stackInfo, extensionInfo);
        org.apache.ambari.server.orm.entities.ExtensionLinkEntity linkEntity = createExtensionLinkEntity(stackInfo, extensionInfo);
        stackManager.linkStackToExtension(stackInfo, extensionInfo);
        try {
            linkDAO.create(linkEntity);
        } catch (javax.persistence.RollbackException e) {
            java.lang.String message = "Unable to create extension link";
            org.apache.ambari.server.controller.AmbariManagementHelper.LOG.debug(message, e);
            java.lang.String errorMessage = (((((((message + ", stackName=") + stackInfo.getName()) + ", stackVersion=") + stackInfo.getVersion()) + ", extensionName=") + extensionInfo.getName()) + ", extensionVersion=") + extensionInfo.getVersion();
            org.apache.ambari.server.controller.AmbariManagementHelper.LOG.warn(errorMessage);
            throw new org.apache.ambari.server.AmbariException(errorMessage, e);
        }
    }

    public void createExtensionLinks(org.apache.ambari.server.stack.StackManager stackManager, java.util.List<org.apache.ambari.server.state.ExtensionInfo> extensions) throws org.apache.ambari.server.AmbariException {
        java.util.Map<java.lang.String, java.util.List<org.apache.ambari.server.state.StackInfo>> stackMap = stackManager.getStacksByName();
        for (java.util.List<org.apache.ambari.server.state.StackInfo> stacks : stackMap.values()) {
            java.util.Collections.sort(stacks);
            java.util.Collections.reverse(stacks);
        }
        java.util.Collections.sort(extensions);
        java.util.Collections.reverse(extensions);
        for (org.apache.ambari.server.state.ExtensionInfo extension : extensions) {
            if (extension.isActive() && extension.isAutoLink()) {
                org.apache.ambari.server.controller.AmbariManagementHelper.LOG.debug("Autolink - looking for matching stack versions for extension:{}/{} ", extension.getName(), extension.getVersion());
                for (org.apache.ambari.server.state.stack.ExtensionMetainfoXml.Stack supportedStack : extension.getStacks()) {
                    java.util.List<org.apache.ambari.server.state.StackInfo> stacks = stackMap.get(supportedStack.getName());
                    for (org.apache.ambari.server.state.StackInfo stack : stacks) {
                        if ((stack.getExtension(extension.getName()) == null) && (org.apache.ambari.server.utils.VersionUtils.compareVersions(stack.getVersion(), supportedStack.getVersion()) > (-1))) {
                            org.apache.ambari.server.controller.AmbariManagementHelper.LOG.debug("Autolink - extension: {}/{} stack: {}/{}", extension.getName(), extension.getVersion(), stack.getName(), stack.getVersion());
                            createExtensionLink(stackManager, stack, extension);
                        } else {
                            org.apache.ambari.server.controller.AmbariManagementHelper.LOG.debug("Autolink - not a match extension: {}/{} stack: {}/{}", extension.getName(), extension.getVersion(), stack.getName(), stack.getVersion());
                        }
                    }
                }
            } else {
                org.apache.ambari.server.controller.AmbariManagementHelper.LOG.debug("Autolink - skipping extension: {}/{}.  It is either not active or set to autolink.", extension.getName(), extension.getVersion());
            }
        }
    }

    private void validateCreateExtensionLinkRequest(org.apache.ambari.server.state.StackInfo stackInfo, org.apache.ambari.server.state.ExtensionInfo extensionInfo) throws org.apache.ambari.server.AmbariException {
        if (stackInfo == null) {
            throw new java.lang.IllegalArgumentException("Stack should be provided");
        }
        if (extensionInfo == null) {
            throw new java.lang.IllegalArgumentException("Extension should be provided");
        }
        if (((org.apache.commons.lang.StringUtils.isBlank(stackInfo.getName()) || org.apache.commons.lang.StringUtils.isBlank(stackInfo.getVersion())) || org.apache.commons.lang.StringUtils.isBlank(extensionInfo.getName())) || org.apache.commons.lang.StringUtils.isBlank(extensionInfo.getVersion())) {
            throw new java.lang.IllegalArgumentException("Stack name, stack version, extension name and extension version should be provided");
        }
        org.apache.ambari.server.orm.entities.ExtensionLinkEntity entity = linkDAO.findByStackAndExtension(stackInfo.getName(), stackInfo.getVersion(), extensionInfo.getName(), extensionInfo.getVersion());
        if (entity != null) {
            throw new org.apache.ambari.server.AmbariException(((((((("The stack and extension are already linked" + ", stackName=") + stackInfo.getName()) + ", stackVersion=") + stackInfo.getVersion()) + ", extensionName=") + extensionInfo.getName()) + ", extensionVersion=") + extensionInfo.getVersion());
        }
    }

    public void updateExtensionLink(org.apache.ambari.server.stack.StackManager stackManager, org.apache.ambari.server.orm.entities.ExtensionLinkEntity linkEntity, org.apache.ambari.server.state.StackInfo stackInfo, org.apache.ambari.server.state.ExtensionInfo oldExtensionInfo, org.apache.ambari.server.state.ExtensionInfo newExtensionInfo) throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.server.stack.ExtensionHelper.validateUpdateLink(stackManager, stackInfo, oldExtensionInfo, newExtensionInfo);
        org.apache.ambari.server.orm.entities.ExtensionEntity extension = extensionDAO.find(newExtensionInfo.getName(), newExtensionInfo.getVersion());
        linkEntity.setExtension(extension);
        try {
            linkEntity = linkDAO.merge(linkEntity);
        } catch (javax.persistence.RollbackException e) {
            java.lang.String message = "Unable to update extension link";
            org.apache.ambari.server.controller.AmbariManagementHelper.LOG.debug(message, e);
            java.lang.String errorMessage = (((((((message + ", stackName=") + stackInfo.getName()) + ", stackVersion=") + stackInfo.getVersion()) + ", extensionName=") + newExtensionInfo.getName()) + ", extensionVersion=") + newExtensionInfo.getVersion();
            org.apache.ambari.server.controller.AmbariManagementHelper.LOG.warn(errorMessage);
            throw new org.apache.ambari.server.AmbariException(errorMessage, e);
        }
    }

    private org.apache.ambari.server.orm.entities.ExtensionLinkEntity createExtensionLinkEntity(org.apache.ambari.server.state.StackInfo stackInfo, org.apache.ambari.server.state.ExtensionInfo extensionInfo) throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.server.orm.entities.StackEntity stack = stackDAO.find(stackInfo.getName(), stackInfo.getVersion());
        org.apache.ambari.server.orm.entities.ExtensionEntity extension = extensionDAO.find(extensionInfo.getName(), extensionInfo.getVersion());
        org.apache.ambari.server.orm.entities.ExtensionLinkEntity linkEntity = new org.apache.ambari.server.orm.entities.ExtensionLinkEntity();
        linkEntity.setStack(stack);
        linkEntity.setExtension(extension);
        return linkEntity;
    }
}