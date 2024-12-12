package org.apache.ambari.server.state;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
public class ServiceInfoTest {
    @org.junit.Test
    public void testIsRestartRequiredAfterRackChange() throws java.lang.Exception {
        java.lang.String serviceInfoXml = "<metainfo>\n" + (((((((((((((((((((((((((((((("  <schemaVersion>2.0</schemaVersion>\n" + "  <services>\n") + "    <service>\n") + "      <name>RESTART</name>\n") + "      <displayName>RESTART</displayName>\n") + "      <comment>Apache Hadoop Distributed File System</comment>\n") + "      <version>2.1.0.2.0</version>\n") + "      <restartRequiredAfterRackChange>true</restartRequiredAfterRackChange>\n") + "    </service>\n") + "    <service>\n") + "      <name>NO_RESTART</name>\n") + "      <displayName>NO_RESTART</displayName>\n") + "      <comment>Apache Hadoop Distributed File System</comment>\n") + "      <version>2.1.0.2.0</version>\n") + "      <restartRequiredAfterRackChange>false</restartRequiredAfterRackChange>\n") + "    </service>\n") + "    <service>\n") + "      <name>DEFAULT_RESTART</name>\n") + "      <displayName>DEFAULT_RESTART</displayName>\n") + "      <comment>Apache Hadoop Distributed File System</comment>\n") + "      <version>2.1.0.2.0</version>\n") + "    </service>\n") + "    <service>\n") + "      <name>HCFS_SERVICE</name>\n") + "      <displayName>HCFS_SERVICE</displayName>\n") + "      <comment>Hadoop Compatible File System</comment>\n") + "      <version>2.1.1.0</version>\n") + "      <serviceType>HCFS</serviceType>\n") + "    </service>\n") + "  </services>\n") + "</metainfo>\n");
        java.util.Map<java.lang.String, org.apache.ambari.server.state.ServiceInfo> serviceInfoMap = org.apache.ambari.server.state.ServiceInfoTest.getServiceInfo(serviceInfoXml);
        org.junit.Assert.assertTrue(serviceInfoMap.get("RESTART").isRestartRequiredAfterRackChange());
        org.junit.Assert.assertFalse(serviceInfoMap.get("NO_RESTART").isRestartRequiredAfterRackChange());
        org.junit.Assert.assertNull(serviceInfoMap.get("DEFAULT_RESTART").isRestartRequiredAfterRackChange());
        org.junit.Assert.assertEquals(serviceInfoMap.get("HCFS_SERVICE").getServiceType(), "HCFS");
    }

    @org.junit.Test
    public void testCustomMetricsWidgetsFiles() throws java.lang.Exception {
        java.lang.String serviceInfoXml = "<metainfo>\n" + ((((((((((((((("  <schemaVersion>2.0</schemaVersion>\n" + "  <services>\n") + "    <service>\n") + "      <name>CUSTOM</name>\n") + "      <displayName>CUSTOM</displayName>\n") + "      <metricsFileName>CUSTOM_metrics.json</metricsFileName>\n") + "      <widgetsFileName>CUSTOM_widgets.json</widgetsFileName>\n") + "    </service>\n") + "    <service>\n") + "      <name>DEFAULT</name>\n") + "      <displayName>DEFAULT</displayName>\n") + "      <comment>Apache Hadoop Distributed File System</comment>\n") + "      <version>2.1.0.2.0</version>\n") + "    </service>\n") + "  </services>\n") + "</metainfo>\n");
        java.util.Map<java.lang.String, org.apache.ambari.server.state.ServiceInfo> serviceInfoMap = org.apache.ambari.server.state.ServiceInfoTest.getServiceInfo(serviceInfoXml);
        org.junit.Assert.assertEquals("CUSTOM_metrics.json", serviceInfoMap.get("CUSTOM").getMetricsFileName());
        org.junit.Assert.assertEquals("CUSTOM_widgets.json", serviceInfoMap.get("CUSTOM").getWidgetsFileName());
        org.junit.Assert.assertEquals("metrics.json", serviceInfoMap.get("DEFAULT").getMetricsFileName());
        org.junit.Assert.assertEquals("widgets.json", serviceInfoMap.get("DEFAULT").getWidgetsFileName());
    }

    @org.junit.Test
    public void testSelectionField() throws java.lang.Exception {
        java.lang.String serviceInfoXmlDeprecated = "<metainfo>\n" + ((((((("  <schemaVersion>2.0</schemaVersion>\n" + "  <services>\n") + "    <service>\n") + "      <name>DEPRECATED</name>\n") + "      <selection>DEPRECATED</selection>\n") + "    </service>\n") + "  </services>\n") + "</metainfo>\n");
        java.util.Map<java.lang.String, org.apache.ambari.server.state.ServiceInfo> serviceInfoMapDeprecated = org.apache.ambari.server.state.ServiceInfoTest.getServiceInfo(serviceInfoXmlDeprecated);
        org.apache.ambari.server.state.ServiceInfo deprecated = serviceInfoMapDeprecated.get("DEPRECATED");
        org.junit.Assert.assertEquals(deprecated.getSelection(), org.apache.ambari.server.state.ServiceInfo.Selection.DEPRECATED);
        org.junit.Assert.assertFalse(deprecated.isSelectionEmpty());
        java.lang.String serviceInfoXmlDefault = "<metainfo>\n" + (((((("  <schemaVersion>2.0</schemaVersion>\n" + "  <services>\n") + "    <service>\n") + "      <name>DEFAULT</name>\n") + "    </service>\n") + "  </services>\n") + "</metainfo>\n");
        java.util.Map<java.lang.String, org.apache.ambari.server.state.ServiceInfo> serviceInfoMapDefault = org.apache.ambari.server.state.ServiceInfoTest.getServiceInfo(serviceInfoXmlDefault);
        org.apache.ambari.server.state.ServiceInfo defaultSi = serviceInfoMapDefault.get("DEFAULT");
        org.junit.Assert.assertEquals(defaultSi.getSelection(), org.apache.ambari.server.state.ServiceInfo.Selection.DEFAULT);
        org.junit.Assert.assertTrue(defaultSi.isSelectionEmpty());
    }

    @org.junit.Test
    public void testCredentialStoreFields() throws java.lang.Exception {
        java.lang.String serviceInfoXml = "<metainfo>\n" + ((((((((((("  <schemaVersion>2.0</schemaVersion>\n" + "  <services>\n") + "    <service>\n") + "      <name>RANGER</name>\n") + "      <credential-store>\n") + "          <supported>true</supported>\n") + "          <enabled>true</enabled>\n") + "          <required>true</required>\n") + "      </credential-store>\n") + "    </service>\n") + "  </services>\n") + "</metainfo>\n");
        java.util.Map<java.lang.String, org.apache.ambari.server.state.ServiceInfo> serviceInfoMap = org.apache.ambari.server.state.ServiceInfoTest.getServiceInfo(serviceInfoXml);
        org.apache.ambari.server.state.ServiceInfo service = serviceInfoMap.get("RANGER");
        org.junit.Assert.assertTrue(service.isCredentialStoreSupported());
        org.junit.Assert.assertTrue(service.isCredentialStoreEnabled());
        org.junit.Assert.assertTrue(service.isCredentialStoreRequired());
        serviceInfoXml = "<metainfo>\n" + ((((((((((("  <schemaVersion>2.0</schemaVersion>\n" + "  <services>\n") + "    <service>\n") + "      <name>HIVE</name>\n") + "      <credential-store>\n") + "          <supported>true</supported>\n") + "          <enabled>false</enabled>\n") + "          <required>false</required>\n") + "      </credential-store>\n") + "    </service>\n") + "  </services>\n") + "</metainfo>\n");
        serviceInfoMap = org.apache.ambari.server.state.ServiceInfoTest.getServiceInfo(serviceInfoXml);
        service = serviceInfoMap.get("HIVE");
        org.junit.Assert.assertTrue(service.isCredentialStoreSupported());
        org.junit.Assert.assertFalse(service.isCredentialStoreEnabled());
        org.junit.Assert.assertFalse(service.isCredentialStoreRequired());
        serviceInfoXml = "<metainfo>\n" + (((((("  <schemaVersion>2.0</schemaVersion>\n" + "  <services>\n") + "    <service>\n") + "      <name>AMBARI_METRICS</name>\n") + "    </service>\n") + "  </services>\n") + "</metainfo>\n");
        serviceInfoMap = org.apache.ambari.server.state.ServiceInfoTest.getServiceInfo(serviceInfoXml);
        service = serviceInfoMap.get("AMBARI_METRICS");
        org.junit.Assert.assertFalse(service.isCredentialStoreSupported());
        org.junit.Assert.assertFalse(service.isCredentialStoreEnabled());
        serviceInfoXml = "<metainfo>\n" + ((((((((("  <schemaVersion>2.0</schemaVersion>\n" + "  <services>\n") + "    <service>\n") + "      <name>HBASE</name>\n") + "      <credential-store>\n") + "          <supported>true</supported>\n") + "      </credential-store>\n") + "    </service>\n") + "  </services>\n") + "</metainfo>\n");
        serviceInfoMap = org.apache.ambari.server.state.ServiceInfoTest.getServiceInfo(serviceInfoXml);
        service = serviceInfoMap.get("HBASE");
        org.junit.Assert.assertTrue(service.isCredentialStoreSupported());
        org.junit.Assert.assertFalse(service.isCredentialStoreEnabled());
    }

    @org.junit.Test
    public void testCredentialStoreInfoValidity() throws java.lang.Exception {
        java.lang.String serviceInfoXml = "<metainfo>\n" + (((((((((("  <schemaVersion>2.0</schemaVersion>\n" + "  <services>\n") + "    <service>\n") + "      <name>RANGER</name>\n") + "      <credential-store>\n") + "          <supported>true</supported>\n") + "          <enabled>false</enabled>\n") + "      </credential-store>\n") + "    </service>\n") + "  </services>\n") + "</metainfo>\n");
        java.util.Map<java.lang.String, org.apache.ambari.server.state.ServiceInfo> serviceInfoMap = org.apache.ambari.server.state.ServiceInfoTest.getServiceInfo(serviceInfoXml);
        org.apache.ambari.server.state.ServiceInfo serviceInfo = serviceInfoMap.get("RANGER");
        org.junit.Assert.assertTrue(serviceInfo.isValid());
        serviceInfoXml = "<metainfo>\n" + (((((((((("  <schemaVersion>2.0</schemaVersion>\n" + "  <services>\n") + "    <service>\n") + "      <name>RANGER</name>\n") + "      <credential-store>\n") + "          <supported>true</supported>\n") + "          <enabled>true</enabled>\n") + "      </credential-store>\n") + "    </service>\n") + "  </services>\n") + "</metainfo>\n");
        serviceInfoMap = org.apache.ambari.server.state.ServiceInfoTest.getServiceInfo(serviceInfoXml);
        serviceInfo = serviceInfoMap.get("RANGER");
        org.junit.Assert.assertTrue(serviceInfo.isValid());
        serviceInfoXml = "<metainfo>\n" + (((((((((("  <schemaVersion>2.0</schemaVersion>\n" + "  <services>\n") + "    <service>\n") + "      <name>RANGER</name>\n") + "      <credential-store>\n") + "          <supported>false</supported>\n") + "          <enabled>false</enabled>\n") + "      </credential-store>\n") + "    </service>\n") + "  </services>\n") + "</metainfo>\n");
        serviceInfoMap = org.apache.ambari.server.state.ServiceInfoTest.getServiceInfo(serviceInfoXml);
        serviceInfo = serviceInfoMap.get("RANGER");
        org.junit.Assert.assertTrue(serviceInfo.isValid());
        serviceInfoXml = "<metainfo>\n" + (((((("  <schemaVersion>2.0</schemaVersion>\n" + "  <services>\n") + "    <service>\n") + "      <name>RANGER</name>\n") + "    </service>\n") + "  </services>\n") + "</metainfo>\n");
        serviceInfoMap = org.apache.ambari.server.state.ServiceInfoTest.getServiceInfo(serviceInfoXml);
        serviceInfo = serviceInfoMap.get("RANGER");
        org.junit.Assert.assertTrue(serviceInfo.isValid());
        serviceInfoXml = "<metainfo>\n" + (((((((((("  <schemaVersion>2.0</schemaVersion>\n" + "  <services>\n") + "    <service>\n") + "      <name>RANGER</name>\n") + "      <credential-store>\n") + "          <supported>false</supported>\n") + "          <enabled>true</enabled>\n") + "      </credential-store>\n") + "    </service>\n") + "  </services>\n") + "</metainfo>\n");
        serviceInfoMap = org.apache.ambari.server.state.ServiceInfoTest.getServiceInfo(serviceInfoXml);
        serviceInfo = serviceInfoMap.get("RANGER");
        org.junit.Assert.assertFalse("Credential store is enabled for a service that does not support it", serviceInfo.isValid());
        serviceInfoXml = "<metainfo>\n" + (((((((("  <schemaVersion>2.0</schemaVersion>\n" + "  <services>\n") + "    <service>\n") + "      <name>RANGER</name>\n") + "      <credential-store>\n") + "      </credential-store>\n") + "    </service>\n") + "  </services>\n") + "</metainfo>\n");
        serviceInfoMap = org.apache.ambari.server.state.ServiceInfoTest.getServiceInfo(serviceInfoXml);
        serviceInfo = serviceInfoMap.get("RANGER");
        org.junit.Assert.assertFalse("Credential store details not specified", serviceInfo.isValid());
        serviceInfoXml = "<metainfo>\n" + ((((((((("  <schemaVersion>2.0</schemaVersion>\n" + "  <services>\n") + "    <service>\n") + "      <name>RANGER</name>\n") + "      <credential-store>\n") + "          <supported>true</supported>\n") + "      </credential-store>\n") + "    </service>\n") + "  </services>\n") + "</metainfo>\n");
        serviceInfoMap = org.apache.ambari.server.state.ServiceInfoTest.getServiceInfo(serviceInfoXml);
        serviceInfo = serviceInfoMap.get("RANGER");
        org.junit.Assert.assertFalse("Credential store enabled not specified", serviceInfo.isValid());
        serviceInfoXml = "<metainfo>\n" + ((((((((("  <schemaVersion>2.0</schemaVersion>\n" + "  <services>\n") + "    <service>\n") + "      <name>RANGER</name>\n") + "      <credential-store>\n") + "          <enabled>true</enabled>\n") + "      </credential-store>\n") + "    </service>\n") + "  </services>\n") + "</metainfo>\n");
        serviceInfoMap = org.apache.ambari.server.state.ServiceInfoTest.getServiceInfo(serviceInfoXml);
        serviceInfo = serviceInfoMap.get("RANGER");
        org.junit.Assert.assertFalse("Credential store supported not specified", serviceInfo.isValid());
    }

    @org.junit.Test
    public void testSetRestartRequiredAfterRackChange() throws java.lang.Exception {
        org.apache.ambari.server.state.ServiceInfo serviceInfo = new org.apache.ambari.server.state.ServiceInfo();
        serviceInfo.setRestartRequiredAfterRackChange(true);
        org.junit.Assert.assertTrue(serviceInfo.isRestartRequiredAfterRackChange());
        serviceInfo.setRestartRequiredAfterRackChange(false);
        org.junit.Assert.assertFalse(serviceInfo.isRestartRequiredAfterRackChange());
    }

    @org.junit.Test
    public void testServiceProperties() throws java.lang.Exception {
        java.lang.String serviceInfoXml = "<metainfo>" + ((((((((((((((((("  <schemaVersion>2.0</schemaVersion>" + "  <services>") + "    <service>") + "      <name>WITH_PROPS</name>") + "      <displayName>WITH_PROPS</displayName>") + "      <properties>") + "        <property>") + "          <name>PROP1</name>") + "          <value>VAL1</value>") + "        </property>") + "        <property>") + "          <name>PROP2</name>") + "          <value>VAL2</value>") + "        </property>") + "      </properties>") + "    </service>") + "  </services>") + "</metainfo>");
        java.util.Map<java.lang.String, org.apache.ambari.server.state.ServiceInfo> serviceInfoMap = org.apache.ambari.server.state.ServiceInfoTest.getServiceInfo(serviceInfoXml);
        java.util.Map<java.lang.String, java.lang.String> serviceProperties = serviceInfoMap.get("WITH_PROPS").getServiceProperties();
        org.junit.Assert.assertTrue(serviceProperties.containsKey("PROP1"));
        org.junit.Assert.assertEquals("VAL1", serviceProperties.get("PROP1"));
        org.junit.Assert.assertTrue(serviceProperties.containsKey("PROP2"));
        org.junit.Assert.assertEquals("VAL2", serviceProperties.get("PROP2"));
    }

    @org.junit.Test
    public void testDefaultVisibilityServiceProperties() throws java.lang.Exception {
        java.lang.String serviceInfoXml = "<metainfo>" + ((((((((((((((((("  <schemaVersion>2.0</schemaVersion>" + "  <services>") + "    <service>") + "      <name>WITH_PROPS</name>") + "      <displayName>WITH_PROPS</displayName>") + "      <properties>") + "        <property>") + "          <name>PROP1</name>") + "          <value>VAL1</value>") + "        </property>") + "        <property>") + "          <name>PROP2</name>") + "          <value>VAL2</value>") + "        </property>") + "      </properties>") + "    </service>") + "  </services>") + "</metainfo>");
        java.util.Map<java.lang.String, org.apache.ambari.server.state.ServiceInfo> serviceInfoMap = org.apache.ambari.server.state.ServiceInfoTest.getServiceInfo(serviceInfoXml);
        java.util.Map<java.lang.String, java.lang.String> serviceProperties = serviceInfoMap.get("WITH_PROPS").getServiceProperties();
        org.junit.Assert.assertTrue("true".equals(serviceProperties.get(org.apache.ambari.server.state.ServiceInfo.DEFAULT_SERVICE_INSTALLABLE_PROPERTY.getKey())));
        org.junit.Assert.assertTrue("true".equals(serviceProperties.get(org.apache.ambari.server.state.ServiceInfo.DEFAULT_SERVICE_MANAGED_PROPERTY.getKey())));
        org.junit.Assert.assertTrue("true".equals(serviceProperties.get(org.apache.ambari.server.state.ServiceInfo.DEFAULT_SERVICE_MONITORED_PROPERTY.getKey())));
    }

    @org.junit.Test
    public void testVisibilityServicePropertyOverride() throws java.lang.Exception {
        java.lang.String serviceInfoXml = "<metainfo>" + ((((((((((((((((((((("  <schemaVersion>2.0</schemaVersion>" + "  <services>") + "    <service>") + "      <name>WITH_PROPS</name>") + "      <displayName>WITH_PROPS</displayName>") + "      <properties>") + "        <property>") + "          <name>PROP1</name>") + "          <value>VAL1</value>") + "        </property>") + "        <property>") + "          <name>PROP2</name>") + "          <value>VAL2</value>") + "        </property>") + "        <property>") + "          <name>managed</name>") + "          <value>false</value>") + "        </property>") + "      </properties>") + "    </service>") + "  </services>") + "</metainfo>");
        java.util.Map<java.lang.String, org.apache.ambari.server.state.ServiceInfo> serviceInfoMap = org.apache.ambari.server.state.ServiceInfoTest.getServiceInfo(serviceInfoXml);
        java.util.Map<java.lang.String, java.lang.String> serviceProperties = serviceInfoMap.get("WITH_PROPS").getServiceProperties();
        org.junit.Assert.assertTrue("true".equals(serviceProperties.get(org.apache.ambari.server.state.ServiceInfo.DEFAULT_SERVICE_INSTALLABLE_PROPERTY.getKey())));
        org.junit.Assert.assertTrue("false".equals(serviceProperties.get(org.apache.ambari.server.state.ServiceInfo.DEFAULT_SERVICE_MANAGED_PROPERTY.getKey())));
        org.junit.Assert.assertTrue("true".equals(serviceProperties.get(org.apache.ambari.server.state.ServiceInfo.DEFAULT_SERVICE_MONITORED_PROPERTY.getKey())));
    }

    @org.junit.Test
    public void testDuplicateServicePropertyValidationAfterXmlDeserialization() throws java.lang.Exception {
        java.lang.String serviceInfoXml = "<metainfo>" + (((((((((((((((((((((("  <schemaVersion>2.0</schemaVersion>" + "  <services>") + "    <service>") + "      <version>1.0</version>") + "      <name>WITH_DUPLICATE_PROPS</name>") + "      <displayName>WITH_PROPS</displayName>") + "      <properties>") + "        <property>") + "          <name>PROP1</name>") + "          <value>VAL1</value>") + "        </property>") + "        <property>") + "          <name>PROP1</name>") + "          <value>VAL2</value>") + "        </property>") + "        <property>") + "          <name>managed</name>") + "          <value>false</value>") + "        </property>") + "      </properties>") + "    </service>") + "  </services>") + "</metainfo>");
        java.util.Map<java.lang.String, org.apache.ambari.server.state.ServiceInfo> serviceInfoMap = org.apache.ambari.server.state.ServiceInfoTest.getServiceInfo(serviceInfoXml);
        org.apache.ambari.server.state.ServiceInfo serviceInfo = serviceInfoMap.get("WITH_DUPLICATE_PROPS");
        org.junit.Assert.assertFalse("Service info should be in invalid state due to duplicate service property names !", serviceInfo.isValid());
        org.junit.Assert.assertTrue("Service info error collection should contain the name of the duplicate service property !", serviceInfo.getErrors().contains(((("Duplicate service property with name 'PROP1' found in " + serviceInfo.getName()) + ":") + serviceInfo.getVersion()) + " service definition !"));
    }

    @org.junit.Test
    public void testDuplicateServicePropertyValidationAfterSet() {
        org.apache.ambari.server.state.ServicePropertyInfo p1 = new org.apache.ambari.server.state.ServicePropertyInfo();
        p1.setName("PROP1");
        p1.setValue("V1");
        org.apache.ambari.server.state.ServicePropertyInfo p2 = new org.apache.ambari.server.state.ServicePropertyInfo();
        p2.setName("PROP1");
        p2.setValue("V2");
        java.util.List<org.apache.ambari.server.state.ServicePropertyInfo> servicePropertyList = com.google.common.collect.Lists.newArrayList(p1, p2);
        org.apache.ambari.server.state.ServiceInfo serviceInfo = new org.apache.ambari.server.state.ServiceInfo();
        serviceInfo.setName("TEST_NAME");
        serviceInfo.setVersion("TEST_VERSION");
        serviceInfo.setServicePropertyList(servicePropertyList);
        org.junit.Assert.assertFalse("Service info should be in invalid state due to duplicate service property names !", serviceInfo.isValid());
        org.junit.Assert.assertTrue("Service info error collection should contain the name of the duplicate service property !", serviceInfo.getErrors().contains(((("Duplicate service property with name 'PROP1' found in " + serviceInfo.getName()) + ":") + serviceInfo.getVersion()) + " service definition !"));
    }

    @org.junit.Test
    public void testMultiplePimaryLogsValidationAfterXmlDeserialization() throws java.lang.Exception {
        java.lang.String serviceInfoXml = "<metainfo>" + ((((((((((((((((((((((((((((((((("  <schemaVersion>2.0</schemaVersion>" + "  <services>") + "    <service>") + "      <version>1.0</version>") + "      <name>WITH_MULTIPLE_PRIMARY_LOGS</name>") + "      <displayName>WITH_MULTIPLE_PRIMARY_LOGS</displayName>") + "      <properties>") + "        <property>") + "          <name>managed</name>") + "          <value>false</value>") + "        </property>") + "      </properties>") + "      <components> ") + "        <component> ") + "          <name>COMPONENT_WITH_MULTIPLE_PRIMARY_LOG</name> ") + "          <displayName>COMPONENT_WITH_MULTIPLE_PRIMARY_LOG</displayName> ") + "          <category>MASTER</category> ") + "          <cardinality>0-1</cardinality> ") + "          <versionAdvertised>true</versionAdvertised> ") + "          <logs> ") + "            <log> ") + "              <logId>log1</logId> ") + "              <primary>true</primary> ") + "            </log> ") + "            <log> ") + "              <logId>log2</logId> ") + "              <primary>true</primary> ") + "            </log> ") + "          </logs> ") + "       </component> ") + "      </components> ") + "    </service>") + "  </services>") + "</metainfo>");
        java.util.Map<java.lang.String, org.apache.ambari.server.state.ServiceInfo> serviceInfoMap = org.apache.ambari.server.state.ServiceInfoTest.getServiceInfo(serviceInfoXml);
        org.apache.ambari.server.state.ServiceInfo serviceInfo = serviceInfoMap.get("WITH_MULTIPLE_PRIMARY_LOGS");
        org.junit.Assert.assertFalse("Service info should be in invalid state due to multiple primary logs at one of it's components!", serviceInfo.isValid());
        org.junit.Assert.assertTrue("Service info error collection should contain the name of the component with the multiple primary logs!", serviceInfo.getErrors().contains("More than one primary log exists for the component COMPONENT_WITH_MULTIPLE_PRIMARY_LOG"));
    }

    @org.junit.Test
    public void testSetServicePropertiesAfterPropertyListSet() {
        org.apache.ambari.server.state.ServicePropertyInfo p1 = new org.apache.ambari.server.state.ServicePropertyInfo();
        p1.setName("PROP1");
        p1.setValue("V1");
        org.apache.ambari.server.state.ServicePropertyInfo p2 = new org.apache.ambari.server.state.ServicePropertyInfo();
        p2.setName("PROP2");
        p2.setValue("V2");
        java.util.List<org.apache.ambari.server.state.ServicePropertyInfo> servicePropertyList = com.google.common.collect.Lists.newArrayList(p1, p2);
        org.apache.ambari.server.state.ServiceInfo serviceInfo = new org.apache.ambari.server.state.ServiceInfo();
        serviceInfo.setName("TEST_NAME");
        serviceInfo.setVersion("TEST_VERSION");
        serviceInfo.setServicePropertyList(servicePropertyList);
        java.util.Map<java.lang.String, java.lang.String> serviceProperties = serviceInfo.getServiceProperties();
        org.junit.Assert.assertTrue(serviceProperties.containsKey("PROP1"));
        org.junit.Assert.assertEquals("V1", serviceProperties.get("PROP1"));
        org.junit.Assert.assertTrue(serviceProperties.containsKey("PROP2"));
        org.junit.Assert.assertEquals("V2", serviceProperties.get("PROP2"));
        org.junit.Assert.assertTrue("true".equals(serviceProperties.get(org.apache.ambari.server.state.ServiceInfo.DEFAULT_SERVICE_INSTALLABLE_PROPERTY.getKey())));
        org.junit.Assert.assertTrue("true".equals(serviceProperties.get(org.apache.ambari.server.state.ServiceInfo.DEFAULT_SERVICE_MANAGED_PROPERTY.getKey())));
        org.junit.Assert.assertTrue("true".equals(serviceProperties.get(org.apache.ambari.server.state.ServiceInfo.DEFAULT_SERVICE_MONITORED_PROPERTY.getKey())));
    }

    @org.junit.Test
    public void testSupportDeleteViaUI() throws java.lang.Exception {
        java.lang.String serviceInfoXml = "<metainfo>" + (((((((("  <schemaVersion>2.0</schemaVersion>" + "  <services>") + "    <service>") + "      <name>HDFS</name>") + "      <displayName>HDFS</displayName>") + "      <supportDeleteViaUI>true</supportDeleteViaUI>") + "    </service>") + "  </services>") + "</metainfo>");
        java.util.Map<java.lang.String, org.apache.ambari.server.state.ServiceInfo> serviceInfoMap = org.apache.ambari.server.state.ServiceInfoTest.getServiceInfo(serviceInfoXml);
        org.junit.Assert.assertTrue(serviceInfoMap.get("HDFS").isSupportDeleteViaUI());
        serviceInfoXml = "<metainfo>" + (((((((("  <schemaVersion>2.0</schemaVersion>" + "  <services>") + "    <service>") + "      <name>HDFS</name>") + "      <displayName>HDFS</displayName>") + "      <supportDeleteViaUI>false</supportDeleteViaUI>") + "    </service>") + "  </services>") + "</metainfo>");
        serviceInfoMap = org.apache.ambari.server.state.ServiceInfoTest.getServiceInfo(serviceInfoXml);
        org.junit.Assert.assertFalse(serviceInfoMap.get("HDFS").isSupportDeleteViaUI());
        serviceInfoXml = "<metainfo>" + ((((((("  <schemaVersion>2.0</schemaVersion>" + "  <services>") + "    <service>") + "      <name>HDFS</name>") + "      <displayName>HDFS</displayName>") + "    </service>") + "  </services>") + "</metainfo>");
        serviceInfoMap = org.apache.ambari.server.state.ServiceInfoTest.getServiceInfo(serviceInfoXml);
        org.junit.Assert.assertTrue(serviceInfoMap.get("HDFS").isSupportDeleteViaUI());
    }

    @org.junit.Test
    public void testSingleSignOnSupport() throws javax.xml.bind.JAXBException {
        java.lang.String serviceInfoXml = "<metainfo>" + (((((("  <schemaVersion>2.0</schemaVersion>" + "  <services>") + "    <service>") + "      <name>SERVICE</name>") + "    </service>") + "  </services>") + "</metainfo>");
        java.util.Map<java.lang.String, org.apache.ambari.server.state.ServiceInfo> serviceInfoMap = org.apache.ambari.server.state.ServiceInfoTest.getServiceInfo(serviceInfoXml);
        org.junit.Assert.assertFalse(serviceInfoMap.get("SERVICE").isSingleSignOnSupported());
        org.junit.Assert.assertTrue(serviceInfoMap.get("SERVICE").isValid());
        org.apache.ambari.server.state.SingleSignOnInfo singleSignOnInfo = serviceInfoMap.get("SERVICE").getSingleSignOnInfo();
        org.junit.Assert.assertNull(singleSignOnInfo);
        serviceInfoXml = "<metainfo>" + ((((((((((("  <schemaVersion>2.0</schemaVersion>" + "  <services>") + "    <service>") + "      <name>SERVICE</name>") + "      <sso>") + "        <supported>true</supported>") + "        <enabledConfiguration>config-type/property_name</enabledConfiguration>") + "        <kerberosRequired>true</kerberosRequired> ") + "      </sso>") + "    </service>") + "  </services>") + "</metainfo>");
        serviceInfoMap = org.apache.ambari.server.state.ServiceInfoTest.getServiceInfo(serviceInfoXml);
        org.junit.Assert.assertTrue(serviceInfoMap.get("SERVICE").isSingleSignOnSupported());
        org.junit.Assert.assertTrue(serviceInfoMap.get("SERVICE").isValid());
        singleSignOnInfo = serviceInfoMap.get("SERVICE").getSingleSignOnInfo();
        org.junit.Assert.assertNotNull(singleSignOnInfo);
        org.junit.Assert.assertTrue(singleSignOnInfo.isSupported());
        org.junit.Assert.assertEquals(java.lang.Boolean.TRUE, singleSignOnInfo.getSupported());
        org.junit.Assert.assertEquals("config-type/property_name", singleSignOnInfo.getEnabledConfiguration());
        org.junit.Assert.assertTrue(singleSignOnInfo.isKerberosRequired());
        serviceInfoXml = "<metainfo>" + ((((((((("  <schemaVersion>2.0</schemaVersion>" + "  <services>") + "    <service>") + "      <name>SERVICE</name>") + "      <sso>") + "        <supported>false</supported>") + "      </sso>") + "    </service>") + "  </services>") + "</metainfo>");
        serviceInfoMap = org.apache.ambari.server.state.ServiceInfoTest.getServiceInfo(serviceInfoXml);
        org.junit.Assert.assertFalse(serviceInfoMap.get("SERVICE").isSingleSignOnSupported());
        org.junit.Assert.assertTrue(serviceInfoMap.get("SERVICE").isValid());
        singleSignOnInfo = serviceInfoMap.get("SERVICE").getSingleSignOnInfo();
        org.junit.Assert.assertNotNull(singleSignOnInfo);
        org.junit.Assert.assertFalse(singleSignOnInfo.isSupported());
        org.junit.Assert.assertEquals(java.lang.Boolean.FALSE, singleSignOnInfo.getSupported());
        org.junit.Assert.assertNull(singleSignOnInfo.getEnabledConfiguration());
        serviceInfoXml = "<metainfo>" + ((((((((("  <schemaVersion>2.0</schemaVersion>" + "  <services>") + "    <service>") + "      <name>SERVICE</name>") + "      <sso>") + "        <supported>true</supported>") + "      </sso>") + "    </service>") + "  </services>") + "</metainfo>");
        serviceInfoMap = org.apache.ambari.server.state.ServiceInfoTest.getServiceInfo(serviceInfoXml);
        org.junit.Assert.assertTrue(serviceInfoMap.get("SERVICE").isSingleSignOnSupported());
        org.junit.Assert.assertFalse(serviceInfoMap.get("SERVICE").isValid());
        org.junit.Assert.assertEquals(1, serviceInfoMap.get("SERVICE").getErrors().size());
        singleSignOnInfo = serviceInfoMap.get("SERVICE").getSingleSignOnInfo();
        org.junit.Assert.assertNotNull(singleSignOnInfo);
        org.junit.Assert.assertTrue(singleSignOnInfo.isSupported());
        org.junit.Assert.assertEquals(java.lang.Boolean.TRUE, singleSignOnInfo.getSupported());
        org.junit.Assert.assertNull(singleSignOnInfo.getEnabledConfiguration());
        org.junit.Assert.assertNull(singleSignOnInfo.getSsoEnabledTest());
    }

    @org.junit.Test
    public void testKerberosEnabledTest() throws java.lang.Exception {
        java.util.Map<java.lang.String, org.apache.ambari.server.state.ServiceInfo> serviceInfoMap;
        org.apache.ambari.server.state.ServiceInfo service;
        java.lang.String kerberosEnabledTest = "{\n" + (((((((((((((("  \"or\": [\n" + "    {\n") + "      \"equals\": [\n") + "        \"core-site/hadoop.security.authentication\",\n") + "        \"kerberos\"\n") + "      ]\n") + "    },\n") + "    {\n") + "      \"equals\": [\n") + "        \"hdfs-site/hadoop.security.authentication\",\n") + "        \"kerberos\"\n") + "      ]\n") + "    }\n") + "  ]\n") + "}");
        java.lang.String serviceInfoXml = ((((("<metainfo>\n" + (((("  <schemaVersion>2.0</schemaVersion>\n" + "  <services>\n") + "    <service>\n") + "      <name>HDFS</name>\n") + "      <kerberosEnabledTest>\n")) + kerberosEnabledTest) + "      </kerberosEnabledTest>\n") + "    </service>\n") + "  </services>\n") + "</metainfo>\n";
        serviceInfoMap = org.apache.ambari.server.state.ServiceInfoTest.getServiceInfo(serviceInfoXml);
        service = serviceInfoMap.get("HDFS");
        org.junit.Assert.assertEquals(kerberosEnabledTest, service.getKerberosEnabledTest().trim());
        serviceInfoXml = "<metainfo>\n" + (((((("  <schemaVersion>2.0</schemaVersion>\n" + "  <services>\n") + "    <service>\n") + "      <name>HDFS</name>\n") + "    </service>\n") + "  </services>\n") + "</metainfo>\n");
        serviceInfoMap = org.apache.ambari.server.state.ServiceInfoTest.getServiceInfo(serviceInfoXml);
        service = serviceInfoMap.get("HDFS");
        org.junit.Assert.assertNull(service.getKerberosEnabledTest());
    }

    @org.junit.Test
    public void testLdapIntegrationSupport() throws java.lang.Exception {
        java.lang.String serviceInfoXml = "<metainfo>" + (((((("  <schemaVersion>2.0</schemaVersion>" + "  <services>") + "    <service>") + "      <name>SERVICE</name>") + "    </service>") + "  </services>") + "</metainfo>");
        java.util.Map<java.lang.String, org.apache.ambari.server.state.ServiceInfo> serviceInfoMap = org.apache.ambari.server.state.ServiceInfoTest.getServiceInfo(serviceInfoXml);
        org.junit.Assert.assertFalse(serviceInfoMap.get("SERVICE").isLdapSupported());
        org.junit.Assert.assertTrue(serviceInfoMap.get("SERVICE").isValid());
        org.apache.ambari.server.state.ServiceLdapInfo ldapInfo = serviceInfoMap.get("SERVICE").getLdapInfo();
        org.junit.Assert.assertNull(ldapInfo);
        serviceInfoXml = "<metainfo>" + (((((((((("  <schemaVersion>2.0</schemaVersion>" + "  <services>") + "    <service>") + "      <name>SERVICE</name>") + "      <ldap>") + "        <supported>true</supported>") + "        <ldapEnabledTest>{\"equals\": [\"config-type/property_name\", \"true\"]}</ldapEnabledTest>") + "      </ldap>") + "    </service>") + "  </services>") + "</metainfo>");
        serviceInfoMap = org.apache.ambari.server.state.ServiceInfoTest.getServiceInfo(serviceInfoXml);
        org.junit.Assert.assertTrue(serviceInfoMap.get("SERVICE").isLdapSupported());
        org.junit.Assert.assertTrue(serviceInfoMap.get("SERVICE").isValid());
        ldapInfo = serviceInfoMap.get("SERVICE").getLdapInfo();
        org.junit.Assert.assertNotNull(ldapInfo);
        org.junit.Assert.assertTrue(ldapInfo.isSupported());
        org.junit.Assert.assertEquals("{\"equals\": [\"config-type/property_name\", \"true\"]}", ldapInfo.getLdapEnabledTest());
        serviceInfoXml = "<metainfo>" + ((((((((("  <schemaVersion>2.0</schemaVersion>" + "  <services>") + "    <service>") + "      <name>SERVICE</name>") + "      <ldap>") + "        <supported>false</supported>") + "      </ldap>") + "    </service>") + "  </services>") + "</metainfo>");
        serviceInfoMap = org.apache.ambari.server.state.ServiceInfoTest.getServiceInfo(serviceInfoXml);
        org.junit.Assert.assertFalse(serviceInfoMap.get("SERVICE").isLdapSupported());
        org.junit.Assert.assertTrue(serviceInfoMap.get("SERVICE").isValid());
        ldapInfo = serviceInfoMap.get("SERVICE").getLdapInfo();
        org.junit.Assert.assertNotNull(ldapInfo);
        org.junit.Assert.assertFalse(ldapInfo.isSupported());
        org.junit.Assert.assertNull(ldapInfo.getLdapEnabledTest());
        serviceInfoXml = "<metainfo>" + ((((((((("  <schemaVersion>2.0</schemaVersion>" + "  <services>") + "    <service>") + "      <name>SERVICE</name>") + "      <ldap>") + "        <supported>true</supported>") + "      </ldap>") + "    </service>") + "  </services>") + "</metainfo>");
        serviceInfoMap = org.apache.ambari.server.state.ServiceInfoTest.getServiceInfo(serviceInfoXml);
        org.junit.Assert.assertTrue(serviceInfoMap.get("SERVICE").isLdapSupported());
        org.junit.Assert.assertFalse(serviceInfoMap.get("SERVICE").isValid());
        org.junit.Assert.assertEquals(1, serviceInfoMap.get("SERVICE").getErrors().size());
    }

    @org.junit.Test
    public void testIsRollingRestartSupported() throws javax.xml.bind.JAXBException {
        java.lang.String serviceInfoXml = "<metainfo>" + ((((((("  <schemaVersion>2.0</schemaVersion>" + "  <services>") + "    <service>") + "      <name>SERVICE</name>") + "      <rollingRestartSupported>true</rollingRestartSupported>") + "    </service>") + "  </services>") + "</metainfo>");
        java.util.Map<java.lang.String, org.apache.ambari.server.state.ServiceInfo> serviceInfoMap = org.apache.ambari.server.state.ServiceInfoTest.getServiceInfo(serviceInfoXml);
        org.junit.Assert.assertTrue(serviceInfoMap.get("SERVICE").isRollingRestartSupported());
        org.junit.Assert.assertTrue(serviceInfoMap.get("SERVICE").isValid());
        serviceInfoXml = "<metainfo>" + ((((((("  <schemaVersion>2.0</schemaVersion>" + "  <services>") + "    <service>") + "      <name>SERVICE</name>") + "      <rollingRestartSupported>false</rollingRestartSupported>") + "    </service>") + "  </services>") + "</metainfo>");
        serviceInfoMap = org.apache.ambari.server.state.ServiceInfoTest.getServiceInfo(serviceInfoXml);
        org.junit.Assert.assertFalse(serviceInfoMap.get("SERVICE").isRollingRestartSupported());
        org.junit.Assert.assertTrue(serviceInfoMap.get("SERVICE").isValid());
        serviceInfoXml = "<metainfo>" + (((((("  <schemaVersion>2.0</schemaVersion>" + "  <services>") + "    <service>") + "      <name>SERVICE</name>") + "    </service>") + "  </services>") + "</metainfo>");
        serviceInfoMap = org.apache.ambari.server.state.ServiceInfoTest.getServiceInfo(serviceInfoXml);
        org.junit.Assert.assertFalse(serviceInfoMap.get("SERVICE").isRollingRestartSupported());
        org.junit.Assert.assertTrue(serviceInfoMap.get("SERVICE").isValid());
    }

    private static java.util.Map<java.lang.String, org.apache.ambari.server.state.ServiceInfo> getServiceInfo(java.lang.String xml) throws javax.xml.bind.JAXBException {
        java.io.InputStream configStream = new java.io.ByteArrayInputStream(xml.getBytes());
        javax.xml.bind.JAXBContext jaxbContext = javax.xml.bind.JAXBContext.newInstance(org.apache.ambari.server.state.stack.ServiceMetainfoXml.class);
        javax.xml.bind.Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
        org.apache.ambari.server.state.stack.ServiceMetainfoXml serviceMetainfoXml = ((org.apache.ambari.server.state.stack.ServiceMetainfoXml) (unmarshaller.unmarshal(configStream)));
        java.util.Map<java.lang.String, org.apache.ambari.server.state.ServiceInfo> serviceInfoMap = new java.util.HashMap<>();
        for (org.apache.ambari.server.state.ServiceInfo serviceInfo : serviceMetainfoXml.getServices()) {
            serviceInfoMap.put(serviceInfo.getName(), serviceInfo);
        }
        return serviceInfoMap;
    }
}