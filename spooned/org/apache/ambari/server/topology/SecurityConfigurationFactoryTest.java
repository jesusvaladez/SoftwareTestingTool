package org.apache.ambari.server.topology;
import org.easymock.Capture;
import org.easymock.EasyMock;
import org.easymock.EasyMockRule;
import org.easymock.EasyMockSupport;
import org.easymock.Mock;
import org.easymock.MockType;
import static org.easymock.EasyMock.capture;
public class SecurityConfigurationFactoryTest {
    private static final java.lang.String TEST_KERBEROS_DESCRIPTOR_REFERENCE = "test-kd-reference";

    private static final java.lang.String TEST_KERBEROS_DESCRIPTOR_JSON = "{\"test\":\"test json\"}";

    @org.junit.Rule
    public org.easymock.EasyMockRule mocks = new org.easymock.EasyMockRule(this);

    @org.easymock.Mock(type = org.easymock.MockType.STRICT)
    private org.apache.ambari.server.orm.dao.KerberosDescriptorDAO kerberosDescriptorDAO;

    private org.apache.ambari.server.topology.SecurityConfigurationFactory testSubject;

    @org.junit.Before
    public void before() {
        testSubject = new org.apache.ambari.server.topology.SecurityConfigurationFactory(new com.google.gson.Gson(), kerberosDescriptorDAO, new org.apache.ambari.server.topology.KerberosDescriptorFactory());
        org.easymock.EasyMockSupport.injectMocks(testSubject);
    }

    @org.junit.Test
    public void testShouldLoadKerberosDescriptorWhenKDReferenceFoundInRequest() throws java.lang.Exception {
        org.easymock.EasyMock.expect(kerberosDescriptorDAO.findByName(org.apache.ambari.server.topology.SecurityConfigurationFactoryTest.TEST_KERBEROS_DESCRIPTOR_REFERENCE)).andReturn(testKDEntity());
        java.util.Map<java.lang.String, java.lang.Object> reuqestMap = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.lang.Object> security = new java.util.HashMap<>();
        security.put(org.apache.ambari.server.topology.SecurityConfigurationFactory.TYPE_PROPERTY_ID, org.apache.ambari.server.state.SecurityType.KERBEROS.toString());
        security.put(org.apache.ambari.server.topology.SecurityConfigurationFactory.KERBEROS_DESCRIPTOR_REFERENCE_PROPERTY_ID, org.apache.ambari.server.topology.SecurityConfigurationFactoryTest.TEST_KERBEROS_DESCRIPTOR_REFERENCE);
        reuqestMap.put(org.apache.ambari.server.topology.SecurityConfigurationFactory.SECURITY_PROPERTY_ID, security);
        org.easymock.EasyMock.replay(kerberosDescriptorDAO);
        org.apache.ambari.server.topology.SecurityConfiguration securityConfiguration = testSubject.createSecurityConfigurationFromRequest(reuqestMap, false);
        org.easymock.EasyMock.verify(kerberosDescriptorDAO);
        org.junit.Assert.assertTrue(securityConfiguration.getType() == org.apache.ambari.server.state.SecurityType.KERBEROS);
    }

    @org.junit.Test
    public void testShouldPersistKDWhenKDFoundInRequest() throws java.lang.Exception {
        org.easymock.Capture<org.apache.ambari.server.orm.entities.KerberosDescriptorEntity> kdEntityCaptor = org.easymock.EasyMock.newCapture();
        kerberosDescriptorDAO.create(EasyMock.capture(kdEntityCaptor));
        org.easymock.EasyMock.replay(kerberosDescriptorDAO);
        java.util.Map<java.lang.String, java.lang.Object> reuqestMap = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.lang.Object> security = new java.util.HashMap<>();
        security.put(org.apache.ambari.server.topology.SecurityConfigurationFactory.TYPE_PROPERTY_ID, org.apache.ambari.server.state.SecurityType.KERBEROS.toString());
        security.put(org.apache.ambari.server.topology.SecurityConfigurationFactory.KERBEROS_DESCRIPTOR_PROPERTY_ID, testKDReqPropertyMap());
        reuqestMap.put(org.apache.ambari.server.topology.SecurityConfigurationFactory.SECURITY_PROPERTY_ID, security);
        testSubject.createSecurityConfigurationFromRequest(reuqestMap, true);
        org.easymock.EasyMock.verify(kerberosDescriptorDAO);
        org.junit.Assert.assertEquals("The persisted descriptortext is not as expected", "{\"test\":\"{\\\"test\\\":\\\"test json\\\"}\"}", kdEntityCaptor.getValue().getKerberosDescriptorText());
        org.junit.Assert.assertNotNull("There is no generated kerberos descriptor reference in the persisting entity!", kdEntityCaptor.getValue().getName());
    }

    @org.junit.Test
    public void testCreateKerberosSecurityWithoutDescriptor() throws java.lang.Exception {
        java.util.Map<java.lang.String, java.lang.Object> reuqestMap = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.lang.Object> security = new java.util.HashMap<>();
        security.put(org.apache.ambari.server.topology.SecurityConfigurationFactory.TYPE_PROPERTY_ID, org.apache.ambari.server.state.SecurityType.KERBEROS.toString());
        reuqestMap.put(org.apache.ambari.server.topology.SecurityConfigurationFactory.SECURITY_PROPERTY_ID, security);
        org.apache.ambari.server.topology.SecurityConfiguration securityConfiguration = testSubject.createSecurityConfigurationFromRequest(reuqestMap, false);
        org.junit.Assert.assertTrue(securityConfiguration.getType() == org.apache.ambari.server.state.SecurityType.KERBEROS);
    }

    @org.junit.Test
    public void testCreateEmpty() throws java.lang.Exception {
        java.util.Map<java.lang.String, java.lang.Object> reuqestMap = new java.util.HashMap<>();
        org.apache.ambari.server.topology.SecurityConfiguration securityConfiguration = testSubject.createSecurityConfigurationFromRequest(reuqestMap, false);
        org.junit.Assert.assertTrue(securityConfiguration == null);
    }

    @org.junit.Test(expected = java.lang.IllegalArgumentException.class)
    public void testCreateInvalidSecurityType() throws java.lang.Exception {
        java.util.Map<java.lang.String, java.lang.Object> reuqestMap = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.lang.Object> security = new java.util.HashMap<>();
        security.put(org.apache.ambari.server.topology.SecurityConfigurationFactory.TYPE_PROPERTY_ID, "INVALID_SECURITY_TYPE");
        reuqestMap.put(org.apache.ambari.server.topology.SecurityConfigurationFactory.SECURITY_PROPERTY_ID, security);
        org.apache.ambari.server.topology.SecurityConfiguration securityConfiguration = testSubject.createSecurityConfigurationFromRequest(reuqestMap, false);
        org.junit.Assert.assertTrue(securityConfiguration.getType() == org.apache.ambari.server.state.SecurityType.KERBEROS);
    }

    @org.junit.Test
    public void testCreateKerberosSecurityTypeNone() throws java.lang.Exception {
        java.util.Map<java.lang.String, java.lang.Object> reuqestMap = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.lang.Object> security = new java.util.HashMap<>();
        security.put(org.apache.ambari.server.topology.SecurityConfigurationFactory.TYPE_PROPERTY_ID, org.apache.ambari.server.state.SecurityType.NONE.toString());
        reuqestMap.put(org.apache.ambari.server.topology.SecurityConfigurationFactory.SECURITY_PROPERTY_ID, security);
        org.apache.ambari.server.topology.SecurityConfiguration securityConfiguration = testSubject.createSecurityConfigurationFromRequest(reuqestMap, false);
        org.junit.Assert.assertTrue(securityConfiguration.getType() == org.apache.ambari.server.state.SecurityType.NONE);
    }

    private org.apache.ambari.server.orm.entities.KerberosDescriptorEntity testKDEntity() {
        org.apache.ambari.server.orm.entities.KerberosDescriptorEntity testDescriptorEntity = new org.apache.ambari.server.orm.entities.KerberosDescriptorEntity();
        testDescriptorEntity.setName(org.apache.ambari.server.topology.SecurityConfigurationFactoryTest.TEST_KERBEROS_DESCRIPTOR_REFERENCE);
        testDescriptorEntity.setKerberosDescriptorText(org.apache.ambari.server.topology.SecurityConfigurationFactoryTest.TEST_KERBEROS_DESCRIPTOR_JSON);
        return testDescriptorEntity;
    }

    private java.util.Map<java.lang.String, java.lang.Object> testKDReqPropertyMap() {
        java.util.Map<java.lang.String, java.lang.Object> kdMap = new java.util.HashMap<>();
        kdMap.put("test", org.apache.ambari.server.topology.SecurityConfigurationFactoryTest.TEST_KERBEROS_DESCRIPTOR_JSON);
        return kdMap;
    }
}