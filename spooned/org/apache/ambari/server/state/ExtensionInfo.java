package org.apache.ambari.server.state;
public class ExtensionInfo implements java.lang.Comparable<org.apache.ambari.server.state.ExtensionInfo> , org.apache.ambari.server.stack.Validable {
    private java.lang.String name;

    private java.lang.String version;

    private java.util.Collection<org.apache.ambari.server.state.ServiceInfo> services;

    private java.lang.String parentExtensionVersion;

    private java.util.List<org.apache.ambari.server.state.stack.ExtensionMetainfoXml.Stack> stacks;

    private java.util.List<org.apache.ambari.server.state.stack.ExtensionMetainfoXml.Extension> extensions;

    private boolean valid = true;

    private boolean autoLink = false;

    private boolean active = false;

    @java.lang.Override
    public boolean isValid() {
        return valid;
    }

    @java.lang.Override
    public void setValid(boolean valid) {
        this.valid = valid;
    }

    private java.util.Set<java.lang.String> errorSet = new java.util.HashSet<>();

    @java.lang.Override
    public void addError(java.lang.String error) {
        errorSet.add(error);
    }

    @java.lang.Override
    public java.util.Collection<java.lang.String> getErrors() {
        return errorSet;
    }

    @java.lang.Override
    public void addErrors(java.util.Collection<java.lang.String> errors) {
        this.errorSet.addAll(errors);
    }

    private java.lang.String upgradesFolder = null;

    private volatile java.util.Map<java.lang.String, org.apache.ambari.server.state.PropertyInfo> requiredProperties;

    public java.lang.String getName() {
        return name;
    }

    public void setName(java.lang.String name) {
        this.name = name;
    }

    public java.lang.String getVersion() {
        return version;
    }

    public void setVersion(java.lang.String version) {
        this.version = version;
    }

    public synchronized java.util.Collection<org.apache.ambari.server.state.ServiceInfo> getServices() {
        if (services == null)
            services = new java.util.ArrayList<>();

        return services;
    }

    public org.apache.ambari.server.state.ServiceInfo getService(java.lang.String name) {
        java.util.Collection<org.apache.ambari.server.state.ServiceInfo> services = getServices();
        for (org.apache.ambari.server.state.ServiceInfo service : services) {
            if (service.getName().equals(name)) {
                return service;
            }
        }
        return null;
    }

    public synchronized void setServices(java.util.Collection<org.apache.ambari.server.state.ServiceInfo> services) {
        this.services = services;
    }

    @java.lang.Override
    public java.lang.String toString() {
        java.lang.StringBuilder sb = new java.lang.StringBuilder((((("Extension name:" + name) + "\nversion:") + version) + " \nvalid:") + isValid());
        if (services != null) {
            sb.append("\n\t\tService:");
            for (org.apache.ambari.server.state.ServiceInfo service : services) {
                sb.append("\t\t");
                sb.append(service);
            }
        }
        return sb.toString();
    }

    @java.lang.Override
    public int hashCode() {
        return (31 + name.hashCode()) + version.hashCode();
    }

    @java.lang.Override
    public boolean equals(java.lang.Object obj) {
        if (!(obj instanceof org.apache.ambari.server.state.ExtensionInfo)) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        org.apache.ambari.server.state.ExtensionInfo extInfo = ((org.apache.ambari.server.state.ExtensionInfo) (obj));
        return getName().equals(extInfo.getName()) && getVersion().equals(extInfo.getVersion());
    }

    public org.apache.ambari.server.controller.ExtensionVersionResponse convertToResponse() {
        java.util.Collection<org.apache.ambari.server.state.ServiceInfo> serviceInfos = getServices();
        java.util.Collection<java.io.File> serviceDescriptorFiles = new java.util.HashSet<>();
        if (serviceInfos != null) {
            for (org.apache.ambari.server.state.ServiceInfo serviceInfo : serviceInfos) {
                java.io.File file = serviceInfo.getKerberosDescriptorFile();
                if (file != null) {
                    serviceDescriptorFiles.add(file);
                }
            }
        }
        return new org.apache.ambari.server.controller.ExtensionVersionResponse(getVersion(), getParentExtensionVersion(), isValid(), getErrors());
    }

    public java.lang.String getParentExtensionVersion() {
        return parentExtensionVersion;
    }

    public void setParentExtensionVersion(java.lang.String parentExtensionVersion) {
        this.parentExtensionVersion = parentExtensionVersion;
    }

    @java.lang.Override
    public int compareTo(org.apache.ambari.server.state.ExtensionInfo o) {
        if (name.equals(o.name)) {
            return org.apache.ambari.server.utils.VersionUtils.compareVersions(version, o.version);
        }
        return name.compareTo(o.name);
    }

    public java.util.List<org.apache.ambari.server.state.stack.ExtensionMetainfoXml.Stack> getStacks() {
        return stacks;
    }

    public void setStacks(java.util.List<org.apache.ambari.server.state.stack.ExtensionMetainfoXml.Stack> stacks) {
        this.stacks = stacks;
    }

    public java.util.List<org.apache.ambari.server.state.stack.ExtensionMetainfoXml.Extension> getExtensions() {
        return extensions;
    }

    public void setExtensions(java.util.List<org.apache.ambari.server.state.stack.ExtensionMetainfoXml.Extension> extensions) {
        this.extensions = extensions;
    }

    public boolean isAutoLink() {
        return autoLink;
    }

    public void setAutoLink(boolean autoLink) {
        this.autoLink = autoLink;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}