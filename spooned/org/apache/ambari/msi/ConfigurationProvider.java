package org.apache.ambari.msi;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.io.xml.StaxDriver;
import org.apache.commons.lang.StringUtils;
public class ConfigurationProvider extends org.apache.ambari.msi.BaseResourceProvider {
    protected static final java.lang.String CONFIGURATION_CLUSTER_NAME_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("Config", "cluster_name");

    public static final java.lang.String CONFIGURATION_CONFIG_TYPE_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId(null, "type");

    public static final java.lang.String CONFIGURATION_CONFIG_TAG_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId(null, "tag");

    private java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> allConfigs;

    private static final java.lang.String DESTINATION = "xml";

    private static final java.util.Set<java.lang.String> clusterConfigurationResources = new java.util.HashSet<java.lang.String>();

    static {
        clusterConfigurationResources.add("hdfs-site");
        clusterConfigurationResources.add("mapred-site");
        clusterConfigurationResources.add("hbase-site");
        clusterConfigurationResources.add("yarn-site");
        clusterConfigurationResources.add("core-site");
    }

    @java.lang.Override
    public void updateProperties(org.apache.ambari.server.controller.spi.Resource resource, org.apache.ambari.server.controller.spi.Request request, org.apache.ambari.server.controller.spi.Predicate predicate) {
    }

    @java.lang.Override
    public int updateProperties(org.apache.ambari.server.controller.spi.Resource resource, java.util.Map<java.lang.String, java.lang.Object> properties) {
        return -1;
    }

    public ConfigurationProvider(org.apache.ambari.msi.ClusterDefinition clusterDefinition) {
        super(org.apache.ambari.server.controller.spi.Resource.Type.Configuration, clusterDefinition);
        init();
        initConfigurationResources();
    }

    class ScomConfigConverter implements com.thoughtworks.xstream.converters.Converter {
        @java.lang.Override
        public void marshal(java.lang.Object o, com.thoughtworks.xstream.io.HierarchicalStreamWriter hierarchicalStreamWriter, com.thoughtworks.xstream.converters.MarshallingContext marshallingContext) {
        }

        @java.lang.Override
        public java.lang.Object unmarshal(com.thoughtworks.xstream.io.HierarchicalStreamReader hierarchicalStreamReader, com.thoughtworks.xstream.converters.UnmarshallingContext unmarshallingContext) {
            java.util.Map<java.lang.String, java.lang.String> map = new java.util.HashMap<java.lang.String, java.lang.String>();
            while (hierarchicalStreamReader.hasMoreChildren()) {
                hierarchicalStreamReader.moveDown();
                java.lang.String name = "";
                java.lang.String value = "";
                while (hierarchicalStreamReader.hasMoreChildren()) {
                    hierarchicalStreamReader.moveDown();
                    if ("name".equalsIgnoreCase(hierarchicalStreamReader.getNodeName())) {
                        name = hierarchicalStreamReader.getValue();
                    }
                    if ("value".equalsIgnoreCase(hierarchicalStreamReader.getNodeName())) {
                        value = hierarchicalStreamReader.getValue();
                    }
                    hierarchicalStreamReader.moveUp();
                } 
                if (org.apache.commons.lang.StringUtils.isNotEmpty(name) && org.apache.commons.lang.StringUtils.isNotEmpty(value)) {
                    map.put(name, value);
                }
                hierarchicalStreamReader.moveUp();
            } 
            return map;
        }

        @java.lang.Override
        public boolean canConvert(java.lang.Class aClass) {
            return java.util.AbstractMap.class.isAssignableFrom(aClass);
        }
    }

    @java.lang.SuppressWarnings("unchecked")
    private void init() {
        allConfigs = new java.util.HashMap<java.lang.String, java.util.Map<java.lang.String, java.lang.String>>();
        com.thoughtworks.xstream.XStream xstream = new com.thoughtworks.xstream.XStream(new com.thoughtworks.xstream.io.xml.StaxDriver());
        xstream.alias("configuration", java.util.Map.class);
        xstream.registerConverter(new org.apache.ambari.msi.ConfigurationProvider.ScomConfigConverter());
        for (java.lang.String configurationResource : org.apache.ambari.msi.ConfigurationProvider.clusterConfigurationResources) {
            java.lang.String configFileName = (configurationResource + ".") + org.apache.ambari.msi.ConfigurationProvider.DESTINATION;
            java.io.InputStream is = java.lang.ClassLoader.getSystemResourceAsStream(configFileName);
            if (is == null)
                continue;

            java.util.Map<java.lang.String, java.lang.String> properties = ((java.util.HashMap<java.lang.String, java.lang.String>) (xstream.fromXML(is)));
            allConfigs.put(configurationResource, properties);
        }
    }

    private void initConfigurationResources() {
        java.lang.String clusterName = getClusterDefinition().getClusterName();
        for (java.lang.String type : allConfigs.keySet()) {
            org.apache.ambari.server.controller.spi.Resource resource = new org.apache.ambari.server.controller.internal.ResourceImpl(org.apache.ambari.server.controller.spi.Resource.Type.Configuration);
            resource.setProperty(org.apache.ambari.msi.ConfigurationProvider.CONFIGURATION_CLUSTER_NAME_PROPERTY_ID, clusterName);
            resource.setProperty(org.apache.ambari.msi.ConfigurationProvider.CONFIGURATION_CONFIG_TYPE_PROPERTY_ID, type);
            resource.setProperty(org.apache.ambari.msi.ConfigurationProvider.CONFIGURATION_CONFIG_TAG_PROPERTY_ID, "version1");
            java.util.Map<java.lang.String, java.lang.String> properties = allConfigs.get(type);
            for (java.util.Map.Entry<java.lang.String, java.lang.String> entry : properties.entrySet()) {
                java.lang.String id = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("properties", entry.getKey());
                resource.setProperty(id, entry.getValue());
            }
            addResource(resource);
        }
    }
}