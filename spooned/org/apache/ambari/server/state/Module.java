package org.apache.ambari.server.state;
public class Module {
    public enum Category {

        @com.google.gson.annotations.SerializedName("SERVER")
        SERVER,
        @com.google.gson.annotations.SerializedName("CLIENT")
        CLIENT,
        @com.google.gson.annotations.SerializedName("LIBRARY")
        LIBRARY;}

    @com.google.gson.annotations.SerializedName("id")
    private java.lang.String id;

    @com.google.gson.annotations.SerializedName("displayName")
    private java.lang.String displayName;

    @com.google.gson.annotations.SerializedName("description")
    private java.lang.String description;

    @com.google.gson.annotations.SerializedName("category")
    private org.apache.ambari.server.state.Module.Category category;

    @com.google.gson.annotations.SerializedName("name")
    private java.lang.String name;

    @com.google.gson.annotations.SerializedName("version")
    private java.lang.String version;

    @com.google.gson.annotations.SerializedName("definition")
    private java.lang.String definition;

    @com.google.gson.annotations.SerializedName("dependencies")
    private java.util.List<org.apache.ambari.server.state.ModuleDependency> dependencies;

    @com.google.gson.annotations.SerializedName("components")
    private java.util.List<org.apache.ambari.server.state.ModuleComponent> components;

    private java.util.HashMap<java.lang.String, org.apache.ambari.server.state.ModuleComponent> componentHashMap;

    public org.apache.ambari.server.state.Module.Category getCategory() {
        return category;
    }

    public void setType(org.apache.ambari.server.state.Module.Category category) {
        this.category = category;
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

    public java.lang.String getDefinition() {
        return definition;
    }

    public void setDefinition(java.lang.String definition) {
        this.definition = definition;
    }

    public java.lang.String getId() {
        return id;
    }

    public void setId(java.lang.String id) {
        this.id = id;
    }

    public java.lang.String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(java.lang.String displayName) {
        this.displayName = displayName;
    }

    public java.lang.String getDescription() {
        return description;
    }

    public void setDescription(java.lang.String description) {
        this.description = description;
    }

    public void setCategory(org.apache.ambari.server.state.Module.Category category) {
        this.category = category;
    }

    public java.util.List<org.apache.ambari.server.state.ModuleDependency> getDependencies() {
        return dependencies;
    }

    public void setDependencies(java.util.List<org.apache.ambari.server.state.ModuleDependency> dependencies) {
        this.dependencies = dependencies;
    }

    public java.util.List<org.apache.ambari.server.state.ModuleComponent> getComponents() {
        return components;
    }

    public void setComponents(java.util.List<org.apache.ambari.server.state.ModuleComponent> components) {
        this.components = components;
    }

    public org.apache.ambari.server.state.ModuleComponent getModuleComponent(java.lang.String moduleComponentName) {
        return componentHashMap.get(moduleComponentName);
    }

    @java.lang.Override
    public boolean equals(java.lang.Object o) {
        if (this == o)
            return true;

        if ((o == null) || (getClass() != o.getClass()))
            return false;

        org.apache.ambari.server.state.Module module = ((org.apache.ambari.server.state.Module) (o));
        return (((((((java.util.Objects.equals(id, module.id) && java.util.Objects.equals(displayName, module.displayName)) && java.util.Objects.equals(description, module.description)) && java.util.Objects.equals(category, module.category)) && java.util.Objects.equals(name, module.name)) && java.util.Objects.equals(version, module.version)) && java.util.Objects.equals(definition, module.definition)) && java.util.Objects.equals(dependencies, module.dependencies)) && java.util.Objects.equals(components, module.components);
    }

    @java.lang.Override
    public int hashCode() {
        return java.util.Objects.hash(id, displayName, description, category, name, version, definition, dependencies, components);
    }

    @java.lang.Override
    public java.lang.String toString() {
        return (((((((((((((((((((((((("Module{" + "id='") + id) + '\'') + ", displayName='") + displayName) + '\'') + ", description='") + description) + '\'') + ", category=") + category) + ", name='") + name) + '\'') + ", version='") + version) + '\'') + ", definition='") + definition) + '\'') + ", dependencies=") + dependencies) + ", components=") + components) + '}';
    }

    public void populateComponentMap() {
        componentHashMap = new java.util.HashMap<>();
        for (org.apache.ambari.server.state.ModuleComponent moduleComponent : getComponents()) {
            moduleComponent.setModule(this);
            componentHashMap.put(moduleComponent.getName(), moduleComponent);
        }
    }
}