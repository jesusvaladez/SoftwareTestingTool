package org.apache.ambari.server.stack;
import javax.xml.bind.JAXBException;
import javax.xml.bind.UnmarshalException;
import org.apache.commons.lang.StringUtils;
public class ConfigurationDirectory extends org.apache.ambari.server.stack.StackDefinitionDirectory {
    private static org.apache.ambari.server.stack.ModuleFileUnmarshaller unmarshaller = new org.apache.ambari.server.stack.ModuleFileUnmarshaller();

    private java.util.Map<java.lang.String, org.apache.ambari.server.stack.ConfigurationModule> configurationModules = new java.util.HashMap<>();

    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.stack.ConfigurationDirectory.class);

    protected java.io.File propertiesDirFile;

    public ConfigurationDirectory(java.lang.String directoryName, java.lang.String propertiesDirectoryName) {
        super(directoryName);
        if (!org.apache.commons.lang.StringUtils.isBlank(propertiesDirectoryName)) {
            propertiesDirFile = new java.io.File(propertiesDirectoryName);
        }
        parsePath();
    }

    public java.util.Collection<org.apache.ambari.server.stack.ConfigurationModule> getConfigurationModules() {
        return configurationModules.values();
    }

    private void parsePath() {
        java.io.File[] configFiles = directory.listFiles(org.apache.ambari.server.stack.StackDirectory.FILENAME_FILTER);
        if (configFiles != null) {
            for (java.io.File configFile : configFiles) {
                if (configFile.getName().endsWith(org.apache.ambari.server.stack.StackDirectory.SERVICE_CONFIG_FILE_NAME_POSTFIX)) {
                    java.lang.String configType = org.apache.ambari.server.state.ConfigHelper.fileNameToConfigType(configFile.getName());
                    org.apache.ambari.server.state.stack.ConfigurationXml config = null;
                    try {
                        config = org.apache.ambari.server.stack.ConfigurationDirectory.unmarshaller.unmarshal(org.apache.ambari.server.state.stack.ConfigurationXml.class, configFile);
                        org.apache.ambari.server.stack.ConfigurationInfo configInfo = new org.apache.ambari.server.stack.ConfigurationInfo(parseProperties(config, configFile.getName()), parseAttributes(config));
                        org.apache.ambari.server.stack.ConfigurationModule module = new org.apache.ambari.server.stack.ConfigurationModule(configType, configInfo);
                        configurationModules.put(configType, module);
                    } catch (java.lang.Exception e) {
                        java.lang.String error = null;
                        if (((e instanceof javax.xml.bind.JAXBException) || (e instanceof javax.xml.bind.UnmarshalException)) || (e instanceof org.xml.sax.SAXParseException)) {
                            error = (("Could not parse XML " + configFile) + ": ") + e;
                        } else {
                            error = "Could not load configuration for " + configFile;
                        }
                        config = new org.apache.ambari.server.state.stack.ConfigurationXml();
                        config.setValid(false);
                        config.addError(error);
                        org.apache.ambari.server.stack.ConfigurationInfo configInfo = new org.apache.ambari.server.stack.ConfigurationInfo(parseProperties(config, configFile.getName()), parseAttributes(config));
                        configInfo.setValid(false);
                        configInfo.addError(error);
                        org.apache.ambari.server.stack.ConfigurationModule module = new org.apache.ambari.server.stack.ConfigurationModule(configType, configInfo);
                        configurationModules.put(configType, module);
                    }
                }
            }
        }
    }

    private java.util.Collection<org.apache.ambari.server.state.PropertyInfo> parseProperties(org.apache.ambari.server.state.stack.ConfigurationXml configuration, java.lang.String fileName) {
        java.util.List<org.apache.ambari.server.state.PropertyInfo> props = new java.util.ArrayList<>();
        for (org.apache.ambari.server.state.PropertyInfo pi : configuration.getProperties()) {
            pi.setFilename(fileName);
            if (pi.getPropertyTypes().contains(org.apache.ambari.server.state.PropertyInfo.PropertyType.VALUE_FROM_PROPERTY_FILE)) {
                if (((propertiesDirFile != null) && propertiesDirFile.exists()) && propertiesDirFile.isDirectory()) {
                    java.lang.String propertyFileName = pi.getPropertyValueAttributes().getPropertyFileName();
                    java.lang.String propertyFileType = pi.getPropertyValueAttributes().getPropertyFileType();
                    java.lang.String propertyFilePath = (propertiesDirFile.getAbsolutePath() + java.io.File.separator) + propertyFileName;
                    java.io.File propertyFile = new java.io.File(propertyFilePath);
                    if (propertyFile.exists() && propertyFile.isFile()) {
                        try {
                            java.lang.String propertyValue = org.apache.commons.io.FileUtils.readFileToString(propertyFile, java.nio.charset.Charset.defaultCharset());
                            boolean valid = true;
                            switch (propertyFileType.toLowerCase()) {
                                case "xml" :
                                    if (!org.apache.ambari.server.utils.XmlUtils.isValidXml(propertyValue)) {
                                        valid = false;
                                        org.apache.ambari.server.stack.ConfigurationDirectory.LOG.error("Failed to load value from property file. Property file {} is not a valid XML file", propertyFilePath);
                                    }
                                    break;
                                case "json" :
                                    if (!org.apache.ambari.server.utils.JsonUtils.isValidJson(propertyValue)) {
                                        valid = false;
                                        org.apache.ambari.server.stack.ConfigurationDirectory.LOG.error("Failed to load value from property file. Property file {} is not a valid JSON file", propertyFilePath);
                                    }
                                    break;
                                case "text" :
                                default :
                                    break;
                            }
                            if (valid) {
                                pi.setValue(propertyValue);
                            }
                        } catch (java.io.IOException e) {
                            org.apache.ambari.server.stack.ConfigurationDirectory.LOG.error("Failed to load value from property file {}. Error Message {}", propertyFilePath, e.getMessage());
                        }
                    } else {
                        org.apache.ambari.server.stack.ConfigurationDirectory.LOG.error("Failed to load value from property file. Properties file {} does not exist", propertyFilePath);
                    }
                } else if (propertiesDirFile == null) {
                    org.apache.ambari.server.stack.ConfigurationDirectory.LOG.error("Failed to load value from property file. Properties directory is null");
                } else {
                    org.apache.ambari.server.stack.ConfigurationDirectory.LOG.error("Failed to load value from property file. Properties directory {} does not exist", propertiesDirFile.getAbsolutePath());
                }
            }
            props.add(pi);
        }
        return props;
    }

    private java.util.Map<java.lang.String, java.lang.String> parseAttributes(org.apache.ambari.server.state.stack.ConfigurationXml configuration) {
        java.util.Map<java.lang.String, java.lang.String> attributes = new java.util.HashMap<>();
        for (java.util.Map.Entry<javax.xml.namespace.QName, java.lang.String> attribute : configuration.getAttributes().entrySet()) {
            attributes.put(attribute.getKey().getLocalPart(), attribute.getValue());
        }
        return attributes;
    }
}