package org.apache.ambari.server.view.configuration;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import org.apache.commons.lang.StringUtils;
@javax.xml.bind.annotation.XmlRootElement(name = "view")
@javax.xml.bind.annotation.XmlAccessorType(javax.xml.bind.annotation.XmlAccessType.FIELD)
public class ViewConfig {
    private java.lang.String name;

    private java.lang.String label;

    private java.lang.String description;

    private java.lang.String version;

    private java.lang.String build;

    @javax.xml.bind.annotation.XmlElement(name = "min-ambari-version")
    private java.lang.String minAmbariVersion;

    @javax.xml.bind.annotation.XmlElement(name = "max-ambari-version")
    private java.lang.String maxAmbariVersion;

    private java.lang.String icon;

    private java.lang.String icon64;

    private boolean system;

    @javax.xml.bind.annotation.XmlElementWrapper
    @javax.xml.bind.annotation.XmlElement(name = "path")
    private java.util.List<java.lang.String> classpath;

    @javax.xml.bind.annotation.XmlElement(name = "view-class")
    private java.lang.String view;

    private java.lang.Class<? extends org.apache.ambari.view.View> viewClass = null;

    @javax.xml.bind.annotation.XmlElement(name = "data-migrator-class")
    private java.lang.String dataMigrator;

    @javax.xml.bind.annotation.XmlElement(name = "data-version")
    private java.lang.String dataVersion;

    @javax.xml.bind.annotation.XmlElement(name = "cluster-config-options")
    private java.lang.String clusterConfigOptions;

    private java.lang.Class<? extends org.apache.ambari.view.migration.ViewDataMigrator> dataMigratorClass = null;

    @javax.xml.bind.annotation.XmlElement(name = "validator-class")
    private java.lang.String validator;

    private java.lang.Class<? extends org.apache.ambari.view.validation.Validator> validatorClass = null;

    @javax.xml.bind.annotation.XmlElement(name = "masker-class")
    private java.lang.String masker;

    private java.lang.Class<? extends org.apache.ambari.view.Masker> maskerClass = null;

    @javax.xml.bind.annotation.XmlElement(name = "parameter")
    private java.util.List<org.apache.ambari.server.view.configuration.ParameterConfig> parameters;

    @javax.xml.bind.annotation.XmlElement(name = "resource")
    private java.util.List<org.apache.ambari.server.view.configuration.ResourceConfig> resources;

    @javax.xml.bind.annotation.XmlElement(name = "auto-instance")
    private org.apache.ambari.server.view.configuration.AutoInstanceConfig autoInstance;

    @javax.xml.bind.annotation.XmlElement(name = "instance")
    private java.util.List<org.apache.ambari.server.view.configuration.InstanceConfig> instances;

    @javax.xml.bind.annotation.XmlElement(name = "persistence")
    private org.apache.ambari.server.view.configuration.PersistenceConfig persistence;

    @javax.xml.bind.annotation.XmlElement(name = "permission")
    private java.util.List<org.apache.ambari.server.view.configuration.PermissionConfig> permissions;

    public java.lang.String getName() {
        return name;
    }

    public java.lang.String getLabel() {
        return label;
    }

    public java.lang.String getDescription() {
        return description;
    }

    public java.lang.String getVersion() {
        return version;
    }

    public java.lang.String getBuild() {
        return build;
    }

    public java.lang.String getMinAmbariVersion() {
        return minAmbariVersion;
    }

    public java.lang.String getMaxAmbariVersion() {
        return maxAmbariVersion;
    }

    public java.lang.String getIcon() {
        return icon;
    }

    public java.lang.String getIcon64() {
        return icon64;
    }

    public boolean isSystem() {
        return system;
    }

    public java.lang.String getExtraClasspath() {
        return classpath == null ? null : org.apache.commons.lang.StringUtils.join(classpath, ",");
    }

    public java.lang.String getView() {
        return view;
    }

    public java.lang.Class<? extends org.apache.ambari.view.View> getViewClass(java.lang.ClassLoader cl) throws java.lang.ClassNotFoundException {
        if (viewClass == null) {
            viewClass = cl.loadClass(view).asSubclass(org.apache.ambari.view.View.class);
        }
        return viewClass;
    }

    public java.lang.String getDataMigrator() {
        return dataMigrator;
    }

    public java.lang.Class<? extends org.apache.ambari.view.migration.ViewDataMigrator> getDataMigratorClass(java.lang.ClassLoader cl) throws java.lang.ClassNotFoundException {
        if (dataMigratorClass == null) {
            dataMigratorClass = cl.loadClass(dataMigrator).asSubclass(org.apache.ambari.view.migration.ViewDataMigrator.class);
        }
        return dataMigratorClass;
    }

    public int getDataVersion() {
        return dataVersion == null ? 0 : java.lang.Integer.parseInt(dataVersion);
    }

    public java.lang.String getValidator() {
        return validator;
    }

    public java.lang.Class<? extends org.apache.ambari.view.validation.Validator> getValidatorClass(java.lang.ClassLoader cl) throws java.lang.ClassNotFoundException {
        if (validatorClass == null) {
            validatorClass = cl.loadClass(validator).asSubclass(org.apache.ambari.view.validation.Validator.class);
        }
        return validatorClass;
    }

    public java.lang.String getMasker() {
        return masker;
    }

    public java.lang.Class<? extends org.apache.ambari.view.Masker> getMaskerClass(java.lang.ClassLoader cl) throws java.lang.ClassNotFoundException {
        if (maskerClass == null) {
            if (org.apache.commons.lang.StringUtils.isBlank(masker)) {
                maskerClass = org.apache.ambari.server.view.DefaultMasker.class;
            } else {
                maskerClass = cl.loadClass(masker).asSubclass(org.apache.ambari.view.Masker.class);
            }
        }
        return maskerClass;
    }

    public java.util.List<org.apache.ambari.server.view.configuration.ParameterConfig> getParameters() {
        return parameters == null ? java.util.Collections.emptyList() : parameters;
    }

    public java.util.List<org.apache.ambari.server.view.configuration.ResourceConfig> getResources() {
        return resources == null ? java.util.Collections.emptyList() : resources;
    }

    public org.apache.ambari.server.view.configuration.AutoInstanceConfig getAutoInstance() {
        return autoInstance;
    }

    public java.util.List<org.apache.ambari.server.view.configuration.InstanceConfig> getInstances() {
        return instances == null ? java.util.Collections.emptyList() : instances;
    }

    public org.apache.ambari.server.view.configuration.PersistenceConfig getPersistence() {
        return persistence;
    }

    public java.util.List<org.apache.ambari.server.view.configuration.PermissionConfig> getPermissions() {
        return permissions == null ? java.util.Collections.emptyList() : permissions;
    }

    public java.lang.String getClusterConfigOptions() {
        return clusterConfigOptions;
    }
}