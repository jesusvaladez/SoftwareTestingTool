package org.apache.ambari.server.stack;
public class ExtensionHelper {
    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.stack.ExtensionHelper.class);

    public static void validateDeleteLink(org.apache.ambari.server.state.Clusters clusters, org.apache.ambari.server.state.StackInfo stack, org.apache.ambari.server.state.ExtensionInfo extension) throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.server.stack.ExtensionHelper.validateNotRequiredExtension(stack, extension);
        org.apache.ambari.server.stack.ExtensionHelper.validateServicesNotInstalled(clusters, stack, extension);
    }

    private static void validateServicesNotInstalled(org.apache.ambari.server.state.Clusters clusters, org.apache.ambari.server.state.StackInfo stack, org.apache.ambari.server.state.ExtensionInfo extension) throws org.apache.ambari.server.AmbariException {
        for (org.apache.ambari.server.state.Cluster cluster : clusters.getClusters().values()) {
            for (org.apache.ambari.server.state.ServiceInfo service : extension.getServices()) {
                try {
                    if ((service != null) && (cluster.getService(service.getName()) != null)) {
                        java.lang.String message = ((((((((("Extension service is still installed" + ", stackName=") + stack.getName()) + ", stackVersion=") + stack.getVersion()) + ", service=") + service.getName()) + ", extensionName=") + extension.getName()) + ", extensionVersion=") + extension.getVersion();
                        throw new org.apache.ambari.server.AmbariException(message);
                    }
                } catch (org.apache.ambari.server.ServiceNotFoundException e) {
                }
            }
        }
    }

    public static void validateCreateLink(org.apache.ambari.server.stack.StackManager stackManager, org.apache.ambari.server.state.StackInfo stack, org.apache.ambari.server.state.ExtensionInfo extension) throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.server.stack.ExtensionHelper.validateSupportedStackVersion(stack, extension);
        org.apache.ambari.server.stack.ExtensionHelper.validateServiceDuplication(stackManager, stack, extension);
        org.apache.ambari.server.stack.ExtensionHelper.validateRequiredExtensions(stack, extension);
    }

    public static void validateUpdateLink(org.apache.ambari.server.stack.StackManager stackManager, org.apache.ambari.server.state.StackInfo stack, org.apache.ambari.server.state.ExtensionInfo oldExtension, org.apache.ambari.server.state.ExtensionInfo newExtension) throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.server.stack.ExtensionHelper.validateSupportedStackVersion(stack, newExtension);
        org.apache.ambari.server.stack.ExtensionHelper.validateServiceDuplication(stackManager, stack, oldExtension, newExtension);
        org.apache.ambari.server.stack.ExtensionHelper.validateRequiredExtensions(stack, newExtension);
    }

    private static void validateSupportedStackVersion(org.apache.ambari.server.state.StackInfo stack, org.apache.ambari.server.state.ExtensionInfo extension) throws org.apache.ambari.server.AmbariException {
        for (org.apache.ambari.server.state.stack.ExtensionMetainfoXml.Stack validStack : extension.getStacks()) {
            if (validStack.getName().equals(stack.getName())) {
                java.lang.String minStackVersion = validStack.getVersion();
                if (org.apache.ambari.server.utils.VersionUtils.compareVersions(stack.getVersion(), minStackVersion) >= 0) {
                    return;
                }
            }
        }
        java.lang.String message = ((((((("Stack is not supported by extension" + ", stackName=") + stack.getName()) + ", stackVersion=") + stack.getVersion()) + ", extensionName=") + extension.getName()) + ", extensionVersion=") + extension.getVersion();
        throw new org.apache.ambari.server.AmbariException(message);
    }

    private static void validateServiceDuplication(org.apache.ambari.server.stack.StackManager stackManager, org.apache.ambari.server.state.StackInfo stack, org.apache.ambari.server.state.ExtensionInfo extension) throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.server.stack.ExtensionHelper.validateServiceDuplication(stackManager, stack, extension, extension.getServices());
    }

    private static void validateServiceDuplication(org.apache.ambari.server.stack.StackManager stackManager, org.apache.ambari.server.state.StackInfo stack, org.apache.ambari.server.state.ExtensionInfo oldExtension, org.apache.ambari.server.state.ExtensionInfo newExtension) throws org.apache.ambari.server.AmbariException {
        java.util.ArrayList<org.apache.ambari.server.state.ServiceInfo> services = new java.util.ArrayList<>(newExtension.getServices().size());
        for (org.apache.ambari.server.state.ServiceInfo service : newExtension.getServices()) {
            boolean found = false;
            for (org.apache.ambari.server.state.ServiceInfo current : oldExtension.getServices()) {
                if (service.getName().equals(current.getName())) {
                    found = true;
                }
            }
            if (!found) {
                services.add(service);
            }
        }
        org.apache.ambari.server.stack.ExtensionHelper.validateServiceDuplication(stackManager, stack, newExtension, services);
    }

    private static void validateServiceDuplication(org.apache.ambari.server.stack.StackManager stackManager, org.apache.ambari.server.state.StackInfo stack, org.apache.ambari.server.state.ExtensionInfo extension, java.util.Collection<org.apache.ambari.server.state.ServiceInfo> services) throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.server.stack.ExtensionHelper.LOG.debug("Looking for duplicate services");
        for (org.apache.ambari.server.state.ServiceInfo service : services) {
            if (service != null) {
                org.apache.ambari.server.stack.ExtensionHelper.LOG.debug("Looking for duplicate service " + service.getName());
                org.apache.ambari.server.state.ServiceInfo stackService = null;
                try {
                    stackService = stack.getService(service.getName());
                    if (stackService != null) {
                        org.apache.ambari.server.stack.ExtensionHelper.LOG.debug("Found service " + service.getName());
                        if (org.apache.ambari.server.stack.ExtensionHelper.isInheritedExtensionService(stackManager, stack, service.getName(), extension.getName())) {
                            stackService = null;
                        }
                    }
                } catch (java.lang.Exception e) {
                    org.apache.ambari.server.stack.ExtensionHelper.LOG.error("Error validating service duplication", e);
                }
                if (stackService != null) {
                    java.lang.String message = ((((((((("Existing service is included in extension" + ", stackName=") + stack.getName()) + ", stackVersion=") + stack.getVersion()) + ", service=") + service.getName()) + ", extensionName=") + extension.getName()) + ", extensionVersion=") + extension.getVersion();
                    throw new org.apache.ambari.server.AmbariException(message);
                }
            }
        }
    }

    private static boolean isInheritedExtensionService(org.apache.ambari.server.stack.StackManager stackManager, org.apache.ambari.server.state.StackInfo stack, java.lang.String serviceName, java.lang.String extensionName) {
        if (org.apache.ambari.server.stack.ExtensionHelper.isExtensionService(stack, serviceName, extensionName)) {
            org.apache.ambari.server.stack.ExtensionHelper.LOG.debug("Service is at requested stack/version level " + serviceName);
            return false;
        }
        return org.apache.ambari.server.stack.ExtensionHelper.isExtensionService(stackManager, stack.getName(), stack.getParentStackVersion(), serviceName, extensionName);
    }

    private static boolean isExtensionService(org.apache.ambari.server.stack.StackManager stackManager, java.lang.String stackName, java.lang.String stackVersion, java.lang.String serviceName, java.lang.String extensionName) {
        org.apache.ambari.server.stack.ExtensionHelper.LOG.debug((("Checking at stack/version " + stackName) + "/") + stackVersion);
        org.apache.ambari.server.state.StackInfo stack = stackManager.getStack(stackName, stackVersion);
        if (stack == null) {
            org.apache.ambari.server.stack.ExtensionHelper.LOG.warn((("Stack/version not found " + stackName) + "/") + stackVersion);
            return false;
        }
        if (org.apache.ambari.server.stack.ExtensionHelper.isExtensionService(stack, serviceName, extensionName)) {
            org.apache.ambari.server.stack.ExtensionHelper.LOG.debug((((("Stack/version " + stackName) + "/") + stackVersion) + " contains service ") + serviceName);
            return true;
        } else {
            return org.apache.ambari.server.stack.ExtensionHelper.isExtensionService(stackManager, stackName, stack.getParentStackVersion(), serviceName, extensionName);
        }
    }

    private static boolean isExtensionService(org.apache.ambari.server.state.StackInfo stack, java.lang.String serviceName, java.lang.String extensionName) {
        org.apache.ambari.server.state.ExtensionInfo extension = stack.getExtension(extensionName);
        if (extension == null) {
            org.apache.ambari.server.stack.ExtensionHelper.LOG.debug("Extension not found " + extensionName);
            return false;
        }
        return extension.getService(serviceName) != null;
    }

    private static void validateRequiredExtensions(org.apache.ambari.server.state.StackInfo stack, org.apache.ambari.server.state.ExtensionInfo extension) throws org.apache.ambari.server.AmbariException {
        for (org.apache.ambari.server.state.stack.ExtensionMetainfoXml.Extension requiredExtension : extension.getExtensions()) {
            if (requiredExtension != null) {
                java.lang.String message = ((((((((((("Stack has not linked required extension" + ", stackName=") + stack.getName()) + ", stackVersion=") + stack.getVersion()) + ", extensionName=") + extension.getName()) + ", extensionVersion=") + extension.getVersion()) + ", requiredExtensionName=") + requiredExtension.getName()) + ", requiredExtensionVersion=") + requiredExtension.getVersion();
                try {
                    org.apache.ambari.server.state.ExtensionInfo stackExtension = stack.getExtension(requiredExtension.getName());
                    if (stackExtension != null) {
                        java.lang.String version = requiredExtension.getVersion();
                        if (version.endsWith("*")) {
                            version = version.substring(0, version.length() - 1);
                            if (!stackExtension.getVersion().startsWith(version)) {
                                throw new org.apache.ambari.server.AmbariException(message);
                            }
                        } else if (!stackExtension.getVersion().equals(version)) {
                            throw new org.apache.ambari.server.AmbariException(message);
                        }
                    }
                } catch (java.lang.Exception e) {
                    throw new org.apache.ambari.server.AmbariException(message, e);
                }
            }
        }
    }

    private static void validateNotRequiredExtension(org.apache.ambari.server.state.StackInfo stack, org.apache.ambari.server.state.ExtensionInfo extension) throws org.apache.ambari.server.AmbariException {
        for (org.apache.ambari.server.state.ExtensionInfo stackExtension : stack.getExtensions()) {
            if (stackExtension != null) {
                for (org.apache.ambari.server.state.stack.ExtensionMetainfoXml.Extension requiredExtension : stackExtension.getExtensions()) {
                    if ((requiredExtension != null) && requiredExtension.getName().equals(extension.getName())) {
                        java.lang.String message = ((((((((((("Stack extension is required by extension" + ", stackName=") + stack.getName()) + ", stackVersion=") + stack.getVersion()) + ", extensionName=") + extension.getName()) + ", extensionVersion=") + extension.getVersion()) + ", dependentExtensionName=") + stackExtension.getName()) + ", dependentExtensionVersion=") + stackExtension.getVersion();
                        throw new org.apache.ambari.server.AmbariException(message);
                    }
                }
            }
        }
    }
}