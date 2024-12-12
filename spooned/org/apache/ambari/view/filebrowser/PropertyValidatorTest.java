package org.apache.ambari.view.filebrowser;
import static org.easymock.EasyMock.createNiceMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
public class PropertyValidatorTest {
    @org.junit.Test
    public void testValidatePropertyWithValidWebhdfsURI() {
        java.util.Map<java.lang.String, java.lang.String> propertyMap = new java.util.HashMap<java.lang.String, java.lang.String>();
        propertyMap.put(org.apache.ambari.view.filebrowser.PropertyValidator.WEBHDFS_URL, "webhdfs://host:1234/");
        org.apache.ambari.view.ViewInstanceDefinition instanceDefinition = getInstanceDef(propertyMap);
        org.apache.ambari.view.validation.ValidationResult result = new org.apache.ambari.view.filebrowser.PropertyValidator().validateProperty(org.apache.ambari.view.filebrowser.PropertyValidator.WEBHDFS_URL, instanceDefinition, null);
        org.junit.Assert.assertEquals(result, org.apache.ambari.view.validation.ValidationResult.SUCCESS);
        org.junit.Assert.assertEquals(result.isValid(), true);
    }

    @org.junit.Test
    public void testValidatePropertyWithValidHdfsURI() {
        java.util.Map<java.lang.String, java.lang.String> propertyMap = new java.util.HashMap<java.lang.String, java.lang.String>();
        propertyMap.put(org.apache.ambari.view.filebrowser.PropertyValidator.WEBHDFS_URL, "hdfs://host:1234/");
        org.apache.ambari.view.ViewInstanceDefinition instanceDefinition = getInstanceDef(propertyMap);
        org.apache.ambari.view.validation.ValidationResult result = new org.apache.ambari.view.filebrowser.PropertyValidator().validateProperty(org.apache.ambari.view.filebrowser.PropertyValidator.WEBHDFS_URL, instanceDefinition, null);
        org.junit.Assert.assertEquals(result, org.apache.ambari.view.validation.ValidationResult.SUCCESS);
        org.junit.Assert.assertEquals(result.isValid(), true);
    }

    @org.junit.Test
    public void testValidatePropertyWithLocalFileURI() {
        java.util.Map<java.lang.String, java.lang.String> propertyMap = new java.util.HashMap<java.lang.String, java.lang.String>();
        propertyMap.put(org.apache.ambari.view.filebrowser.PropertyValidator.WEBHDFS_URL, "file:///");
        org.apache.ambari.view.ViewInstanceDefinition instanceDefinition = getInstanceDef(propertyMap);
        org.apache.ambari.view.validation.ValidationResult result = new org.apache.ambari.view.filebrowser.PropertyValidator().validateProperty(org.apache.ambari.view.filebrowser.PropertyValidator.WEBHDFS_URL, instanceDefinition, null);
        org.junit.Assert.assertEquals(result.getClass(), org.apache.ambari.view.filebrowser.PropertyValidator.InvalidPropertyValidationResult.class);
        org.junit.Assert.assertEquals(result.isValid(), false);
        org.junit.Assert.assertEquals(result.getDetail(), "Must be valid URL");
    }

    private org.apache.ambari.view.ViewInstanceDefinition getInstanceDef(java.util.Map<java.lang.String, java.lang.String> propertyMap) {
        org.apache.ambari.view.ViewInstanceDefinition instanceDefinition = EasyMock.createNiceMock(org.apache.ambari.view.ViewInstanceDefinition.class);
        EasyMock.expect(instanceDefinition.getPropertyMap()).andReturn(propertyMap);
        EasyMock.replay(instanceDefinition);
        return instanceDefinition;
    }
}