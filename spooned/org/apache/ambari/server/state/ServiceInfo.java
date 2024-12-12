package org.apache.ambari.server.state;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlTransient;
import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.map.annotate.JsonFilter;
@javax.xml.bind.annotation.XmlAccessorType(javax.xml.bind.annotation.XmlAccessType.FIELD)
@org.codehaus.jackson.map.annotate.JsonFilter("propertiesfilter")
public class ServiceInfo implements org.apache.ambari.server.stack.Validable {
    public static final java.util.AbstractMap.SimpleEntry<java.lang.String, java.lang.String> DEFAULT_SERVICE_INSTALLABLE_PROPERTY = new java.util.AbstractMap.SimpleEntry<>("installable", "true");

    public static final java.util.AbstractMap.SimpleEntry<java.lang.String, java.lang.String> DEFAULT_SERVICE_MANAGED_PROPERTY = new java.util.AbstractMap.SimpleEntry<>("managed", "true");

    public static final java.util.AbstractMap.SimpleEntry<java.lang.String, java.lang.String> DEFAULT_SERVICE_MONITORED_PROPERTY = new java.util.AbstractMap.SimpleEntry<>("monitored", "true");

    public static final java.lang.String HADOOP_COMPATIBLE_FS = "HCFS";

    @javax.xml.bind.annotation.XmlTransient
    private java.lang.String schemaVersion;

    private java.lang.String name;

    private java.lang.String displayName;

    private java.lang.String version;

    private java.lang.String comment;

    private java.lang.String serviceType;

    private org.apache.ambari.server.state.ServiceInfo.Selection selection;

    private java.lang.String maintainer;

    @javax.xml.bind.annotation.XmlEnum
    public enum ServiceAdvisorType {

        @javax.xml.bind.annotation.XmlEnumValue("PYTHON")
        PYTHON,
        @javax.xml.bind.annotation.XmlEnumValue("JAVA")
        JAVA;}

    @javax.xml.bind.annotation.XmlElement(name = "service_advisor_type")
    private org.apache.ambari.server.state.ServiceInfo.ServiceAdvisorType serviceAdvisorType = org.apache.ambari.server.state.ServiceInfo.ServiceAdvisorType.PYTHON;

    @javax.xml.bind.annotation.XmlTransient
    private java.util.List<org.apache.ambari.server.state.PropertyInfo> properties;

    @javax.xml.bind.annotation.XmlElementWrapper(name = "components")
    @javax.xml.bind.annotation.XmlElements(@javax.xml.bind.annotation.XmlElement(name = "component"))
    private java.util.List<org.apache.ambari.server.state.ComponentInfo> components;

    @javax.xml.bind.annotation.XmlElement(name = "deleted")
    private boolean isDeleted = false;

    @javax.xml.bind.annotation.XmlElement(name = "supportDeleteViaUI")
    private java.lang.Boolean supportDeleteViaUIField;

    private boolean supportDeleteViaUIInternal = true;

    @org.codehaus.jackson.annotate.JsonIgnore
    @javax.xml.bind.annotation.XmlTransient
    private volatile java.util.Map<java.lang.String, java.util.Set<java.lang.String>> configLayout = null;

    @javax.xml.bind.annotation.XmlElementWrapper(name = "configuration-dependencies")
    @javax.xml.bind.annotation.XmlElement(name = "config-type")
    private java.util.List<java.lang.String> configDependencies;

    @javax.xml.bind.annotation.XmlElementWrapper(name = "excluded-config-types")
    @javax.xml.bind.annotation.XmlElement(name = "config-type")
    private java.util.Set<java.lang.String> excludedConfigTypes = new java.util.HashSet<>();

    @javax.xml.bind.annotation.XmlTransient
    private java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>>> configTypes;

    @org.codehaus.jackson.annotate.JsonIgnore
    private java.lang.Boolean monitoringService;

    @org.codehaus.jackson.annotate.JsonIgnore
    @javax.xml.bind.annotation.XmlElement(name = "restartRequiredAfterChange")
    private java.lang.Boolean restartRequiredAfterChange;

    @org.codehaus.jackson.annotate.JsonIgnore
    @javax.xml.bind.annotation.XmlElement(name = "restartRequiredAfterRackChange")
    private java.lang.Boolean restartRequiredAfterRackChange;

    @javax.xml.bind.annotation.XmlElement(name = "extends")
    private java.lang.String parent;

    @javax.xml.bind.annotation.XmlElement(name = "widgetsFileName")
    private java.lang.String widgetsFileName = org.apache.ambari.server.api.services.AmbariMetaInfo.WIDGETS_DESCRIPTOR_FILE_NAME;

    @javax.xml.bind.annotation.XmlElement(name = "metricsFileName")
    private java.lang.String metricsFileName = org.apache.ambari.server.stack.StackDirectory.SERVICE_METRIC_FILE_NAME;

    @javax.xml.bind.annotation.XmlTransient
    private volatile java.util.Map<java.lang.String, org.apache.ambari.server.state.PropertyInfo> requiredProperties;

    @javax.xml.bind.annotation.XmlElements(@javax.xml.bind.annotation.XmlElement(name = "credential-store"))
    private org.apache.ambari.server.state.CredentialStoreInfo credentialStoreInfo;

    @javax.xml.bind.annotation.XmlElement(name = "kerberosEnabledTest")
    private java.lang.String kerberosEnabledTest = null;

    @javax.xml.bind.annotation.XmlElements(@javax.xml.bind.annotation.XmlElement(name = "sso"))
    private org.apache.ambari.server.state.SingleSignOnInfo singleSignOnInfo;

    @javax.xml.bind.annotation.XmlElements(@javax.xml.bind.annotation.XmlElement(name = "ldap"))
    private org.apache.ambari.server.state.ServiceLdapInfo ldapInfo;

    public java.lang.Boolean isRestartRequiredAfterChange() {
        return restartRequiredAfterChange;
    }

    public void setRestartRequiredAfterChange(java.lang.Boolean restartRequiredAfterChange) {
        this.restartRequiredAfterChange = restartRequiredAfterChange;
    }

    @javax.xml.bind.annotation.XmlTransient
    private java.io.File metricsFile = null;

    @javax.xml.bind.annotation.XmlTransient
    private java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.util.List<org.apache.ambari.server.state.stack.MetricDefinition>>> metrics = null;

    @javax.xml.bind.annotation.XmlTransient
    private java.io.File advisorFile = null;

    @javax.xml.bind.annotation.XmlTransient
    private java.lang.String advisorName = null;

    @javax.xml.bind.annotation.XmlTransient
    private java.io.File alertsFile = null;

    @javax.xml.bind.annotation.XmlTransient
    private java.io.File kerberosDescriptorFile = null;

    @javax.xml.bind.annotation.XmlTransient
    private java.io.File widgetsDescriptorFile = null;

    private org.apache.ambari.server.state.stack.StackRoleCommandOrder roleCommandOrder;

    @javax.xml.bind.annotation.XmlTransient
    private boolean valid = true;

    @javax.xml.bind.annotation.XmlElementWrapper(name = "properties")
    @javax.xml.bind.annotation.XmlElement(name = "property")
    private java.util.List<org.apache.ambari.server.state.ServicePropertyInfo> servicePropertyList = com.google.common.collect.Lists.newArrayList();

    @javax.xml.bind.annotation.XmlTransient
    private java.util.Map<java.lang.String, java.lang.String> servicePropertyMap = com.google.common.collect.ImmutableMap.copyOf(ensureMandatoryServiceProperties(com.google.common.collect.Maps.newHashMap()));

    @java.lang.Override
    public boolean isValid() {
        return valid;
    }

    @java.lang.Override
    public void setValid(boolean valid) {
        this.valid = valid;
    }

    @javax.xml.bind.annotation.XmlTransient
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
        errorSet.addAll(errors);
    }

    @org.codehaus.jackson.annotate.JsonIgnore
    @javax.xml.bind.annotation.XmlElementWrapper(name = "osSpecifics")
    @javax.xml.bind.annotation.XmlElements(@javax.xml.bind.annotation.XmlElement(name = "osSpecific"))
    private java.util.List<org.apache.ambari.server.state.ServiceOsSpecific> serviceOsSpecifics;

    @org.codehaus.jackson.annotate.JsonIgnore
    @javax.xml.bind.annotation.XmlElement(name = "configuration-dir")
    private java.lang.String configDir = org.apache.ambari.server.stack.StackDirectory.SERVICE_CONFIG_FOLDER_NAME;

    @org.codehaus.jackson.annotate.JsonIgnore
    @javax.xml.bind.annotation.XmlElement(name = "themes-dir")
    private java.lang.String themesDir = org.apache.ambari.server.stack.StackDirectory.SERVICE_THEMES_FOLDER_NAME;

    @org.codehaus.jackson.annotate.JsonIgnore
    @javax.xml.bind.annotation.XmlElementWrapper(name = "themes")
    @javax.xml.bind.annotation.XmlElements(@javax.xml.bind.annotation.XmlElement(name = "theme"))
    private java.util.List<org.apache.ambari.server.state.ThemeInfo> themes;

    @javax.xml.bind.annotation.XmlTransient
    private volatile java.util.Map<java.lang.String, org.apache.ambari.server.state.ThemeInfo> themesMap;

    @org.codehaus.jackson.annotate.JsonIgnore
    @javax.xml.bind.annotation.XmlElement(name = "quickLinksConfigurations-dir")
    private java.lang.String quickLinksConfigurationsDir = org.apache.ambari.server.stack.StackDirectory.SERVICE_QUICKLINKS_CONFIGURATIONS_FOLDER_NAME;

    @org.codehaus.jackson.annotate.JsonIgnore
    @javax.xml.bind.annotation.XmlElementWrapper(name = "quickLinksConfigurations")
    @javax.xml.bind.annotation.XmlElements(@javax.xml.bind.annotation.XmlElement(name = "quickLinksConfiguration"))
    private java.util.List<org.apache.ambari.server.state.QuickLinksConfigurationInfo> quickLinksConfigurations;

    @javax.xml.bind.annotation.XmlTransient
    private volatile java.util.Map<java.lang.String, org.apache.ambari.server.state.QuickLinksConfigurationInfo> quickLinksConfigurationsMap;

    private volatile java.util.Map<java.lang.String, org.apache.ambari.server.state.ServiceOsSpecific> serviceOsSpecificsMap;

    private org.apache.ambari.server.state.CommandScriptDefinition commandScript;

    @javax.xml.bind.annotation.XmlElementWrapper(name = "customCommands")
    @javax.xml.bind.annotation.XmlElements(@javax.xml.bind.annotation.XmlElement(name = "customCommand"))
    private java.util.List<org.apache.ambari.server.state.CustomCommandDefinition> customCommands;

    @javax.xml.bind.annotation.XmlElementWrapper(name = "requiredServices")
    @javax.xml.bind.annotation.XmlElement(name = "service")
    private java.util.List<java.lang.String> requiredServices = new java.util.ArrayList<>();

    @javax.xml.bind.annotation.XmlTransient
    private java.lang.String servicePackageFolder;

    @javax.xml.bind.annotation.XmlTransient
    private java.io.File serviceUpgradesFolder;

    @javax.xml.bind.annotation.XmlTransient
    private java.io.File checksFolder;

    @javax.xml.bind.annotation.XmlTransient
    private java.io.File serverActionsFolder;

    @javax.xml.bind.annotation.XmlElement(name = "rollingRestartSupported")
    private boolean rollingRestartSupported;

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    public java.lang.Boolean getSupportDeleteViaUIField() {
        return supportDeleteViaUIField;
    }

    public void setSupportDeleteViaUIField(java.lang.Boolean supportDeleteViaUIField) {
        this.supportDeleteViaUIField = supportDeleteViaUIField;
    }

    public boolean isSupportDeleteViaUI() {
        if (null != supportDeleteViaUIField) {
            return supportDeleteViaUIField.booleanValue();
        }
        return supportDeleteViaUIInternal;
    }

    public void setSupportDeleteViaUI(boolean supportDeleteViaUI) {
        supportDeleteViaUIInternal = supportDeleteViaUI;
    }

    public java.lang.String getName() {
        return name;
    }

    public void setName(java.lang.String name) {
        this.name = name;
    }

    public java.lang.String getParent() {
        return parent;
    }

    public void setParent(java.lang.String parent) {
        this.parent = parent;
    }

    public java.lang.String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(java.lang.String displayName) {
        this.displayName = displayName;
    }

    public void setServiceAdvisorType(org.apache.ambari.server.state.ServiceInfo.ServiceAdvisorType type) {
        serviceAdvisorType = type;
    }

    public org.apache.ambari.server.state.ServiceInfo.ServiceAdvisorType getServiceAdvisorType() {
        return serviceAdvisorType;
    }

    public java.lang.String getServiceType() {
        return serviceType;
    }

    public void setServiceType(java.lang.String serviceType) {
        this.serviceType = serviceType;
    }

    public java.lang.String getVersion() {
        return version;
    }

    public void setVersion(java.lang.String version) {
        this.version = version;
    }

    public org.apache.ambari.server.state.ServiceInfo.Selection getSelection() {
        if (selection == null) {
            return org.apache.ambari.server.state.ServiceInfo.Selection.DEFAULT;
        }
        return selection;
    }

    public void setSelection(org.apache.ambari.server.state.ServiceInfo.Selection selection) {
        this.selection = selection;
    }

    public boolean isSelectionEmpty() {
        return selection == null;
    }

    public java.lang.String getMaintainer() {
        return maintainer;
    }

    public void setMaintainer(java.lang.String maintainer) {
        this.maintainer = maintainer;
    }

    public boolean isMaintainerEmpty() {
        return maintainer == null;
    }

    public java.lang.String getComment() {
        return comment;
    }

    public void setComment(java.lang.String comment) {
        this.comment = comment;
    }

    public java.util.List<java.lang.String> getRequiredServices() {
        return requiredServices;
    }

    public java.lang.String getWidgetsFileName() {
        return widgetsFileName;
    }

    public void setWidgetsFileName(java.lang.String widgetsFileName) {
        this.widgetsFileName = widgetsFileName;
    }

    public java.lang.String getMetricsFileName() {
        return metricsFileName;
    }

    public void setMetricsFileName(java.lang.String metricsFileName) {
        this.metricsFileName = metricsFileName;
    }

    public void setRequiredServices(java.util.List<java.lang.String> requiredServices) {
        this.requiredServices = requiredServices;
    }

    public java.util.List<org.apache.ambari.server.state.PropertyInfo> getProperties() {
        if (properties == null) {
            properties = new java.util.ArrayList<>();
        }
        return properties;
    }

    public void setProperties(java.util.List<org.apache.ambari.server.state.PropertyInfo> properties) {
        this.properties = properties;
    }

    public java.util.List<org.apache.ambari.server.state.ComponentInfo> getComponents() {
        if (components == null) {
            components = new java.util.ArrayList<>();
        }
        return components;
    }

    public org.apache.ambari.server.state.ComponentInfo getComponentByName(java.lang.String componentName) {
        for (org.apache.ambari.server.state.ComponentInfo componentInfo : getComponents()) {
            if (componentInfo.getName().equals(componentName)) {
                return componentInfo;
            }
        }
        return null;
    }

    public boolean isClientOnlyService() {
        if ((components == null) || components.isEmpty()) {
            return false;
        }
        for (org.apache.ambari.server.state.ComponentInfo compInfo : components) {
            if (!compInfo.isClient()) {
                return false;
            }
        }
        return true;
    }

    public org.apache.ambari.server.state.ComponentInfo getClientComponent() {
        org.apache.ambari.server.state.ComponentInfo client = null;
        if (components != null) {
            for (org.apache.ambari.server.state.ComponentInfo compInfo : components) {
                if (compInfo.isClient()) {
                    client = compInfo;
                    break;
                }
            }
        }
        return client;
    }

    public java.io.File getAdvisorFile() {
        return advisorFile;
    }

    public void setAdvisorFile(java.io.File advisorFile) {
        this.advisorFile = advisorFile;
    }

    public java.lang.String getAdvisorName() {
        return advisorName;
    }

    public void setAdvisorName(java.lang.String advisorName) {
        this.advisorName = advisorName;
    }

    public boolean isCredentialStoreSupported() {
        if (credentialStoreInfo != null) {
            if (credentialStoreInfo.isSupported() != null) {
                return credentialStoreInfo.isSupported();
            }
        }
        return false;
    }

    public void setCredentialStoreSupported(boolean credentialStoreSupported) {
        if (credentialStoreInfo == null) {
            credentialStoreInfo = new org.apache.ambari.server.state.CredentialStoreInfo();
        }
        credentialStoreInfo.setSupported(credentialStoreSupported);
    }

    public boolean isCredentialStoreRequired() {
        if (credentialStoreInfo != null) {
            if (credentialStoreInfo.isRequired() != null) {
                return credentialStoreInfo.isRequired();
            }
        }
        return false;
    }

    public void setCredentialStoreRequired(boolean credentialStoreRequired) {
        if (credentialStoreInfo == null) {
            credentialStoreInfo = new org.apache.ambari.server.state.CredentialStoreInfo();
        }
        credentialStoreInfo.setRequired(credentialStoreRequired);
    }

    public boolean isCredentialStoreEnabled() {
        if (credentialStoreInfo != null) {
            if (credentialStoreInfo.isEnabled() != null) {
                return credentialStoreInfo.isEnabled();
            }
        }
        return false;
    }

    public void setCredentialStoreEnabled(boolean credentialStoreEnabled) {
        if (credentialStoreInfo == null) {
            credentialStoreInfo = new org.apache.ambari.server.state.CredentialStoreInfo();
        }
        credentialStoreInfo.setEnabled(credentialStoreEnabled);
    }

    public org.apache.ambari.server.state.CredentialStoreInfo getCredentialStoreInfo() {
        return credentialStoreInfo;
    }

    public void setCredentialStoreInfo(org.apache.ambari.server.state.CredentialStoreInfo credentialStoreInfo) {
        this.credentialStoreInfo = credentialStoreInfo;
    }

    public java.lang.String getKerberosEnabledTest() {
        return kerberosEnabledTest;
    }

    public void setKerberosEnabledTest(java.lang.String kerberosEnabledTest) {
        this.kerberosEnabledTest = kerberosEnabledTest;
    }

    public org.apache.ambari.server.state.SingleSignOnInfo getSingleSignOnInfo() {
        return singleSignOnInfo;
    }

    public void setSingleSignOnInfo(org.apache.ambari.server.state.SingleSignOnInfo singleSignOnInfo) {
        this.singleSignOnInfo = singleSignOnInfo;
    }

    public boolean isSingleSignOnSupported() {
        return (singleSignOnInfo != null) && singleSignOnInfo.isSupported();
    }

    @java.lang.Deprecated
    public java.lang.String getSingleSignOnEnabledConfiguration() {
        return singleSignOnInfo != null ? singleSignOnInfo.getEnabledConfiguration() : null;
    }

    public java.lang.String getSingleSignOnEnabledTest() {
        return singleSignOnInfo != null ? singleSignOnInfo.getSsoEnabledTest() : null;
    }

    public boolean isKerberosRequiredForSingleSignOnIntegration() {
        return (singleSignOnInfo != null) && singleSignOnInfo.isKerberosRequired();
    }

    public org.apache.ambari.server.state.ServiceLdapInfo getLdapInfo() {
        return ldapInfo;
    }

    public void setLdapInfo(org.apache.ambari.server.state.ServiceLdapInfo ldapInfo) {
        this.ldapInfo = ldapInfo;
    }

    public boolean isLdapSupported() {
        return (ldapInfo != null) && ldapInfo.isSupported();
    }

    public java.lang.String getLdapEnabledTest() {
        return ldapInfo != null ? ldapInfo.getLdapEnabledTest() : null;
    }

    @java.lang.Override
    public java.lang.String toString() {
        java.lang.StringBuilder sb = new java.lang.StringBuilder();
        sb.append("Service name:");
        sb.append(name);
        sb.append("\nService type:");
        sb.append(serviceType);
        sb.append("\nversion:");
        sb.append(version);
        sb.append("\nKerberos enabled test:");
        sb.append(kerberosEnabledTest);
        sb.append("\ncomment:");
        sb.append(comment);
        for (org.apache.ambari.server.state.ComponentInfo component : getComponents()) {
            sb.append("\n\n\nComponent:\n");
            sb.append("name=");
            sb.append(component.getName());
            sb.append("\tcategory=");
            sb.append(component.getCategory());
        }
        return sb.toString();
    }

    public synchronized java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>>> getConfigTypeAttributes() {
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>>> tmpConfigTypes = (configTypes == null) ? new java.util.HashMap<>() : configTypes;
        for (java.lang.String excludedtype : excludedConfigTypes) {
            tmpConfigTypes.remove(excludedtype);
        }
        return java.util.Collections.unmodifiableMap(tmpConfigTypes);
    }

    public synchronized void setTypeAttributes(java.lang.String type, java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> typeAttributes) {
        if (configTypes == null) {
            configTypes = new java.util.HashMap<>();
        }
        configTypes.put(type, typeAttributes);
    }

    public synchronized void setAllConfigAttributes(java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>>> types) {
        configTypes = new java.util.HashMap<>();
        for (java.util.Map.Entry<java.lang.String, java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>>> entry : types.entrySet()) {
            setTypeAttributes(entry.getKey(), entry.getValue());
        }
    }

    public boolean hasConfigDependency(java.lang.String type) {
        return (configDependencies != null) && configDependencies.contains(type);
    }

    public boolean hasConfigType(java.lang.String type) {
        return ((configTypes != null) && configTypes.containsKey(type)) && (!excludedConfigTypes.contains(type));
    }

    public boolean hasDependencyAndPropertyFor(java.lang.String type, java.util.Collection<java.lang.String> keyNames) {
        if (!hasConfigDependency(type)) {
            return false;
        }
        buildConfigLayout();
        java.util.Set<java.lang.String> keys = configLayout.get(type);
        for (java.lang.String staleCheck : keyNames) {
            if ((keys != null) && keys.contains(staleCheck)) {
                return true;
            }
        }
        return false;
    }

    private void buildConfigLayout() {
        if (null == configLayout) {
            synchronized(this) {
                if (null == configLayout) {
                    configLayout = new java.util.HashMap<>();
                    for (org.apache.ambari.server.state.PropertyInfo pi : getProperties()) {
                        java.lang.String type = pi.getFilename();
                        int idx = type.indexOf(".xml");
                        type = type.substring(0, idx);
                        if (!configLayout.containsKey(type)) {
                            configLayout.put(type, new java.util.HashSet<>());
                        }
                        configLayout.get(type).add(pi.getName());
                    }
                }
            }
        }
    }

    public java.util.List<java.lang.String> getConfigDependencies() {
        return configDependencies;
    }

    public java.util.List<java.lang.String> getConfigDependenciesWithComponents() {
        java.util.List<java.lang.String> retVal = new java.util.ArrayList<>();
        if (configDependencies != null) {
            retVal.addAll(configDependencies);
        }
        if (components != null) {
            for (org.apache.ambari.server.state.ComponentInfo c : components) {
                if (c.getConfigDependencies() != null) {
                    retVal.addAll(c.getConfigDependencies());
                }
            }
        }
        return retVal.size() == 0 ? configDependencies == null ? null : configDependencies : retVal;
    }

    public void setConfigDependencies(java.util.List<java.lang.String> configDependencies) {
        this.configDependencies = configDependencies;
    }

    public java.lang.String getSchemaVersion() {
        if (schemaVersion == null) {
            return org.apache.ambari.server.api.services.AmbariMetaInfo.SCHEMA_VERSION_2;
        } else {
            return schemaVersion;
        }
    }

    public void setSchemaVersion(java.lang.String schemaVersion) {
        this.schemaVersion = schemaVersion;
    }

    public java.lang.String getServicePackageFolder() {
        return servicePackageFolder;
    }

    public void setServicePackageFolder(java.lang.String servicePackageFolder) {
        this.servicePackageFolder = servicePackageFolder;
    }

    public java.io.File getServiceUpgradesFolder() {
        return serviceUpgradesFolder;
    }

    public void setServiceUpgradesFolder(java.io.File serviceUpgradesFolder) {
        this.serviceUpgradesFolder = serviceUpgradesFolder;
    }

    public java.io.File getChecksFolder() {
        return checksFolder;
    }

    public void setChecksFolder(java.io.File checksFolder) {
        this.checksFolder = checksFolder;
    }

    public java.io.File getServerActionsFolder() {
        return serverActionsFolder;
    }

    public void setServerActionsFolder(java.io.File serverActionsFolder) {
        this.serverActionsFolder = serverActionsFolder;
    }

    public java.util.Map<java.lang.String, org.apache.ambari.server.state.ServiceOsSpecific> getOsSpecifics() {
        if (serviceOsSpecificsMap == null) {
            synchronized(this) {
                if (serviceOsSpecificsMap == null) {
                    java.util.Map<java.lang.String, org.apache.ambari.server.state.ServiceOsSpecific> tmpMap = new java.util.TreeMap<>();
                    if (serviceOsSpecifics != null) {
                        for (org.apache.ambari.server.state.ServiceOsSpecific osSpecific : serviceOsSpecifics) {
                            tmpMap.put(osSpecific.getOsFamily(), osSpecific);
                        }
                    }
                    serviceOsSpecificsMap = tmpMap;
                }
            }
        }
        return serviceOsSpecificsMap;
    }

    public void setOsSpecifics(java.util.Map<java.lang.String, org.apache.ambari.server.state.ServiceOsSpecific> serviceOsSpecificsMap) {
        this.serviceOsSpecificsMap = serviceOsSpecificsMap;
    }

    public java.util.List<org.apache.ambari.server.state.CustomCommandDefinition> getCustomCommands() {
        if (customCommands == null) {
            customCommands = new java.util.ArrayList<>();
        }
        return customCommands;
    }

    public void setCustomCommands(java.util.List<org.apache.ambari.server.state.CustomCommandDefinition> customCommands) {
        this.customCommands = customCommands;
    }

    public org.apache.ambari.server.state.CommandScriptDefinition getCommandScript() {
        return commandScript;
    }

    public void setCommandScript(org.apache.ambari.server.state.CommandScriptDefinition commandScript) {
        this.commandScript = commandScript;
    }

    public void setMetricsFile(java.io.File file) {
        metricsFile = file;
    }

    public java.io.File getMetricsFile() {
        return metricsFile;
    }

    public java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.util.List<org.apache.ambari.server.state.stack.MetricDefinition>>> getMetrics() {
        return metrics;
    }

    public void setMetrics(java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.util.List<org.apache.ambari.server.state.stack.MetricDefinition>>> map) {
        metrics = map;
    }

    public java.lang.String getConfigDir() {
        return configDir;
    }

    public java.lang.Boolean isMonitoringService() {
        return monitoringService;
    }

    public void setMonitoringService(java.lang.Boolean monitoringService) {
        this.monitoringService = monitoringService;
    }

    public void setAlertsFile(java.io.File file) {
        alertsFile = file;
    }

    public java.io.File getAlertsFile() {
        return alertsFile;
    }

    public void setKerberosDescriptorFile(java.io.File file) {
        kerberosDescriptorFile = file;
    }

    public java.io.File getKerberosDescriptorFile() {
        return kerberosDescriptorFile;
    }

    public boolean isRollingRestartSupported() {
        return rollingRestartSupported;
    }

    public void setRollingRestartSupported(boolean rollingRestartSupported) {
        this.rollingRestartSupported = rollingRestartSupported;
    }

    public java.io.File getWidgetsDescriptorFile() {
        return widgetsDescriptorFile;
    }

    public void setWidgetsDescriptorFile(java.io.File widgetsDescriptorFile) {
        this.widgetsDescriptorFile = widgetsDescriptorFile;
    }

    public org.apache.ambari.server.state.stack.StackRoleCommandOrder getRoleCommandOrder() {
        return roleCommandOrder;
    }

    public void setRoleCommandOrder(org.apache.ambari.server.state.stack.StackRoleCommandOrder roleCommandOrder) {
        this.roleCommandOrder = roleCommandOrder;
    }

    public java.util.Set<java.lang.String> getExcludedConfigTypes() {
        return excludedConfigTypes;
    }

    public void setExcludedConfigTypes(java.util.Set<java.lang.String> excludedConfigTypes) {
        this.excludedConfigTypes = excludedConfigTypes;
    }

    public java.util.Map<java.lang.String, org.apache.ambari.server.state.PropertyInfo> getRequiredProperties() {
        java.util.Map<java.lang.String, org.apache.ambari.server.state.PropertyInfo> result = requiredProperties;
        if (result == null) {
            synchronized(this) {
                result = requiredProperties;
                if (result == null) {
                    requiredProperties = result = new java.util.HashMap<>();
                    java.util.List<org.apache.ambari.server.state.PropertyInfo> properties = getProperties();
                    for (org.apache.ambari.server.state.PropertyInfo propertyInfo : properties) {
                        if (propertyInfo.isRequireInput()) {
                            result.put(propertyInfo.getName(), propertyInfo);
                        }
                    }
                }
            }
        }
        return result;
    }

    public java.lang.Boolean isRestartRequiredAfterRackChange() {
        return restartRequiredAfterRackChange;
    }

    public void setRestartRequiredAfterRackChange(java.lang.Boolean restartRequiredAfterRackChange) {
        this.restartRequiredAfterRackChange = restartRequiredAfterRackChange;
    }

    public java.lang.String getThemesDir() {
        return themesDir;
    }

    public void setThemesDir(java.lang.String themesDir) {
        this.themesDir = themesDir;
    }

    public java.util.List<org.apache.ambari.server.state.ThemeInfo> getThemes() {
        return themes;
    }

    public void setThemes(java.util.List<org.apache.ambari.server.state.ThemeInfo> themes) {
        this.themes = themes;
    }

    public java.util.Map<java.lang.String, org.apache.ambari.server.state.ThemeInfo> getThemesMap() {
        if (themesMap == null) {
            java.util.Map<java.lang.String, org.apache.ambari.server.state.ThemeInfo> tmp = new java.util.TreeMap<>();
            if (themes != null) {
                for (org.apache.ambari.server.state.ThemeInfo theme : themes) {
                    tmp.put(theme.getFileName(), theme);
                }
            }
            themesMap = tmp;
        }
        return themesMap;
    }

    public void setThemesMap(java.util.Map<java.lang.String, org.apache.ambari.server.state.ThemeInfo> themesMap) {
        this.themesMap = themesMap;
    }

    public java.lang.String getQuickLinksConfigurationsDir() {
        return quickLinksConfigurationsDir;
    }

    public void setQuickLinksConfigurationsDir(java.lang.String quickLinksConfigurationsDir) {
        this.quickLinksConfigurationsDir = quickLinksConfigurationsDir;
    }

    public java.util.List<org.apache.ambari.server.state.QuickLinksConfigurationInfo> getQuickLinksConfigurations() {
        return quickLinksConfigurations;
    }

    public void setQuickLinksConfigurations(java.util.List<org.apache.ambari.server.state.QuickLinksConfigurationInfo> quickLinksConfigurations) {
        this.quickLinksConfigurations = quickLinksConfigurations;
    }

    public java.util.Map<java.lang.String, org.apache.ambari.server.state.QuickLinksConfigurationInfo> getQuickLinksConfigurationsMap() {
        if (quickLinksConfigurationsMap == null) {
            java.util.Map<java.lang.String, org.apache.ambari.server.state.QuickLinksConfigurationInfo> tmp = new java.util.TreeMap<>();
            if (quickLinksConfigurations != null) {
                for (org.apache.ambari.server.state.QuickLinksConfigurationInfo quickLinksConfiguration : quickLinksConfigurations) {
                    tmp.put(quickLinksConfiguration.getFileName(), quickLinksConfiguration);
                }
            }
            quickLinksConfigurationsMap = tmp;
        }
        return quickLinksConfigurationsMap;
    }

    public void setQuickLinksConfigurationsMap(java.util.Map<java.lang.String, org.apache.ambari.server.state.QuickLinksConfigurationInfo> quickLinksConfigurationsMap) {
        this.quickLinksConfigurationsMap = quickLinksConfigurationsMap;
    }

    public java.util.List<org.apache.ambari.server.state.ServicePropertyInfo> getServicePropertyList() {
        return servicePropertyList;
    }

    public void setServicePropertyList(java.util.List<org.apache.ambari.server.state.ServicePropertyInfo> servicePropertyList) {
        this.servicePropertyList = servicePropertyList;
        afterServicePropertyListSet();
    }

    private void afterServicePropertyListSet() {
        validateServiceProperties();
        buildServiceProperties();
    }

    public java.util.Map<java.lang.String, java.lang.String> getServiceProperties() {
        return servicePropertyMap;
    }

    private void buildServiceProperties() {
        if (isValid()) {
            java.util.Map<java.lang.String, java.lang.String> properties = com.google.common.collect.Maps.newHashMap();
            for (org.apache.ambari.server.state.ServicePropertyInfo property : getServicePropertyList()) {
                properties.put(property.getName(), property.getValue());
            }
            servicePropertyMap = com.google.common.collect.ImmutableMap.copyOf(ensureMandatoryServiceProperties(properties));
        } else {
            servicePropertyMap = com.google.common.collect.ImmutableMap.of();
        }
    }

    private java.util.Map<java.lang.String, java.lang.String> ensureMandatoryServiceProperties(java.util.Map<java.lang.String, java.lang.String> properties) {
        return ensureVisibilityServiceProperties(properties);
    }

    private java.util.Map<java.lang.String, java.lang.String> ensureVisibilityServiceProperties(java.util.Map<java.lang.String, java.lang.String> properties) {
        if (!properties.containsKey(org.apache.ambari.server.state.ServiceInfo.DEFAULT_SERVICE_INSTALLABLE_PROPERTY.getKey())) {
            properties.put(org.apache.ambari.server.state.ServiceInfo.DEFAULT_SERVICE_INSTALLABLE_PROPERTY.getKey(), org.apache.ambari.server.state.ServiceInfo.DEFAULT_SERVICE_INSTALLABLE_PROPERTY.getValue());
        }
        if (!properties.containsKey(org.apache.ambari.server.state.ServiceInfo.DEFAULT_SERVICE_MANAGED_PROPERTY.getKey())) {
            properties.put(org.apache.ambari.server.state.ServiceInfo.DEFAULT_SERVICE_MANAGED_PROPERTY.getKey(), org.apache.ambari.server.state.ServiceInfo.DEFAULT_SERVICE_MANAGED_PROPERTY.getValue());
        }
        if (!properties.containsKey(org.apache.ambari.server.state.ServiceInfo.DEFAULT_SERVICE_MONITORED_PROPERTY.getKey())) {
            properties.put(org.apache.ambari.server.state.ServiceInfo.DEFAULT_SERVICE_MONITORED_PROPERTY.getKey(), org.apache.ambari.server.state.ServiceInfo.DEFAULT_SERVICE_MONITORED_PROPERTY.getValue());
        }
        return properties;
    }

    void afterUnmarshal(javax.xml.bind.Unmarshaller unmarshaller, java.lang.Object parent) {
        afterServicePropertyListSet();
    }

    private void validateServiceProperties() {
        com.google.common.collect.Multimap<java.lang.String, org.apache.ambari.server.state.ServicePropertyInfo> servicePropsByName = com.google.common.collect.Multimaps.index(getServicePropertyList(), new com.google.common.base.Function<org.apache.ambari.server.state.ServicePropertyInfo, java.lang.String>() {
            @java.lang.Override
            public java.lang.String apply(org.apache.ambari.server.state.ServicePropertyInfo servicePropertyInfo) {
                return servicePropertyInfo.getName();
            }
        });
        for (java.lang.String propertyName : servicePropsByName.keySet()) {
            if (servicePropsByName.get(propertyName).size() > 1) {
                setValid(false);
                addError(((((("Duplicate service property with name '" + propertyName) + "' found in ") + getName()) + ":") + getVersion()) + " service definition !");
            }
        }
        for (org.apache.ambari.server.state.ComponentInfo component : getComponents()) {
            int primaryLogs = 0;
            for (org.apache.ambari.server.state.LogDefinition log : component.getLogs()) {
                primaryLogs += (log.isPrimary()) ? 1 : 0;
            }
            if (primaryLogs > 1) {
                setValid(false);
                addError("More than one primary log exists for the component " + component.getName());
            }
        }
        if (credentialStoreInfo != null) {
            if ((credentialStoreInfo.isSupported() != null) && (credentialStoreInfo.isEnabled() != null)) {
                if ((!credentialStoreInfo.isSupported()) && credentialStoreInfo.isEnabled()) {
                    setValid(false);
                    addError(("Credential store cannot be enabled for service " + getName()) + " as it does not support it.");
                }
            }
            if (credentialStoreInfo.isSupported() == null) {
                setValid(false);
                addError("Credential store supported is not specified for service " + getName());
            }
            if (credentialStoreInfo.isEnabled() == null) {
                setValid(false);
                addError("Credential store enabled is not specified for service " + getName());
            }
        }
        if (singleSignOnInfo != null) {
            if (singleSignOnInfo.isSupported()) {
                if (org.apache.commons.lang.StringUtils.isEmpty(singleSignOnInfo.getSsoEnabledTest()) && org.apache.commons.lang.StringUtils.isEmpty(singleSignOnInfo.getEnabledConfiguration())) {
                    setValid(false);
                    addError(("Single Sign-on support is indicated for service " + getName()) + " but no test configuration has been set (enabledConfiguration or ssoEnabledTest).");
                }
            }
        }
        if (((ldapInfo != null) && ldapInfo.isSupported()) && org.apache.commons.lang.StringUtils.isBlank(ldapInfo.getLdapEnabledTest())) {
            setValid(false);
            addError(("LDAP support is indicated for service " + getName()) + " but no test configuration has been set by ldapEnabledTest.");
        }
    }

    public boolean isVersionAdvertised() {
        if (null == components) {
            return false;
        }
        for (org.apache.ambari.server.state.ComponentInfo componentInfo : components) {
            if (componentInfo.isVersionAdvertised()) {
                return true;
            }
        }
        return false;
    }

    public enum Selection {

        DEFAULT,
        TECH_PREVIEW,
        MANDATORY,
        DEPRECATED;}
}