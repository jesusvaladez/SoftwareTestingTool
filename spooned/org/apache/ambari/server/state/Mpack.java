package org.apache.ambari.server.state;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.EqualsBuilder;
public class Mpack {
    private java.lang.Long resourceId;

    private java.lang.Long registryId;

    @com.google.gson.annotations.SerializedName("id")
    private java.lang.String mpackId;

    @com.google.gson.annotations.SerializedName("name")
    private java.lang.String name;

    @com.google.gson.annotations.SerializedName("version")
    private java.lang.String version;

    @com.google.gson.annotations.SerializedName("prerequisites")
    private java.util.HashMap<java.lang.String, java.lang.String> prerequisites;

    @com.google.gson.annotations.SerializedName("modules")
    private java.util.List<org.apache.ambari.server.state.Module> modules;

    @com.google.gson.annotations.SerializedName("definition")
    private java.lang.String definition;

    @com.google.gson.annotations.SerializedName("description")
    private java.lang.String description;

    @com.google.gson.annotations.SerializedName("displayName")
    private java.lang.String displayName;

    private java.lang.String mpackUri;

    public java.lang.Long getResourceId() {
        return resourceId;
    }

    public void setResourceId(java.lang.Long resourceId) {
        this.resourceId = resourceId;
    }

    public java.lang.Long getRegistryId() {
        return registryId;
    }

    public void setRegistryId(java.lang.Long registryId) {
        this.registryId = registryId;
    }

    public java.lang.String getMpackUri() {
        return mpackUri;
    }

    public void setMpackUri(java.lang.String mpackUri) {
        this.mpackUri = mpackUri;
    }

    public java.lang.String getMpackId() {
        return mpackId;
    }

    public void setMpackId(java.lang.String mpackId) {
        this.mpackId = mpackId;
    }

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

    public java.lang.String getDescription() {
        return description;
    }

    public void setDescription(java.lang.String description) {
        this.description = description;
    }

    public java.util.HashMap<java.lang.String, java.lang.String> getPrerequisites() {
        return prerequisites;
    }

    public void setPrerequisites(java.util.HashMap<java.lang.String, java.lang.String> prerequisites) {
        this.prerequisites = prerequisites;
    }

    public java.util.List<org.apache.ambari.server.state.Module> getModules() {
        return modules;
    }

    public void setModules(java.util.List<org.apache.ambari.server.state.Module> modules) {
        this.modules = modules;
    }

    public java.lang.String getDefinition() {
        return definition;
    }

    public void setDefinition(java.lang.String definition) {
        this.definition = definition;
    }

    public java.lang.String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(java.lang.String displayName) {
        this.displayName = displayName;
    }

    public org.apache.ambari.server.state.Module getModule(java.lang.String moduleName) {
        for (org.apache.ambari.server.state.Module module : modules) {
            if (org.apache.commons.lang.StringUtils.equals(moduleName, module.getName())) {
                return module;
            }
        }
        return null;
    }

    public org.apache.ambari.server.state.ModuleComponent getModuleComponent(java.lang.String moduleName, java.lang.String moduleComponentName) {
        for (org.apache.ambari.server.state.Module module : modules) {
            org.apache.ambari.server.state.ModuleComponent moduleComponent = module.getModuleComponent(moduleComponentName);
            if (null != moduleComponent) {
                return moduleComponent;
            }
        }
        return null;
    }

    @java.lang.Override
    public boolean equals(java.lang.Object o) {
        if (this == o)
            return true;

        if ((o == null) || (getClass() != o.getClass()))
            return false;

        org.apache.ambari.server.state.Mpack mpack = ((org.apache.ambari.server.state.Mpack) (o));
        org.apache.ambari.server.state.Mpack that = ((org.apache.ambari.server.state.Mpack) (o));
        org.apache.commons.lang.builder.EqualsBuilder equalsBuilder = new org.apache.commons.lang.builder.EqualsBuilder();
        equalsBuilder.append(resourceId, that.resourceId);
        equalsBuilder.append(registryId, that.registryId);
        equalsBuilder.append(mpackId, that.mpackId);
        equalsBuilder.append(name, that.name);
        equalsBuilder.append(version, that.version);
        equalsBuilder.append(prerequisites, that.prerequisites);
        equalsBuilder.append(modules, that.modules);
        equalsBuilder.append(definition, that.definition);
        equalsBuilder.append(description, that.description);
        equalsBuilder.append(mpackUri, that.mpackUri);
        equalsBuilder.append(displayName, that.displayName);
        return equalsBuilder.isEquals();
    }

    @java.lang.Override
    public int hashCode() {
        return java.util.Objects.hash(resourceId, registryId, mpackId, name, version, prerequisites, modules, definition, description, mpackUri, displayName);
    }

    @java.lang.Override
    public java.lang.String toString() {
        return ((((((((((((((((((((((((((((("Mpack{" + "id=") + resourceId) + ", registryId=") + registryId) + ", mpackId='") + mpackId) + '\'') + ", name='") + name) + '\'') + ", version='") + version) + '\'') + ", prerequisites=") + prerequisites) + ", modules=") + modules) + ", definition='") + definition) + '\'') + ", description='") + description) + '\'') + ", mpackUri='") + mpackUri) + '\'') + ", displayName='") + mpackUri) + '\'') + '}';
    }

    public void copyFrom(org.apache.ambari.server.state.Mpack mpack) {
        if (this.resourceId == null) {
            this.resourceId = mpack.getResourceId();
        }
        if (this.name == null) {
            this.name = mpack.getName();
            if (this.mpackId == null)
                this.mpackId = mpack.getMpackId();

            if (this.version == null)
                this.version = mpack.getVersion();

        }
        if (this.registryId == null) {
            this.registryId = mpack.getRegistryId();
            if (this.description == null)
                this.description = mpack.getDescription();

        }
        if (this.modules == null) {
            this.modules = mpack.getModules();
        }
        if (this.prerequisites == null) {
            this.prerequisites = mpack.getPrerequisites();
        }
        if (this.definition == null) {
            this.definition = mpack.getDefinition();
        }
        if (displayName == null) {
            displayName = mpack.getDisplayName();
        }
    }
}