package org.apache.ambari.server.view.validation;
public class InstanceValidationResultImplTest {
    @org.junit.Test
    public void testGetPropertyResults() throws java.lang.Exception {
        org.apache.ambari.view.validation.ValidationResult result = new org.apache.ambari.server.view.validation.ValidationResultImpl(true, "detail");
        java.util.Map<java.lang.String, org.apache.ambari.view.validation.ValidationResult> propertyResults = new java.util.HashMap<>();
        propertyResults.put("foo", new org.apache.ambari.server.view.validation.ValidationResultImpl(true, "foo detail"));
        propertyResults.put("bar", new org.apache.ambari.server.view.validation.ValidationResultImpl(false, "bar detail"));
        org.apache.ambari.server.view.validation.InstanceValidationResultImpl instanceValidationResult = new org.apache.ambari.server.view.validation.InstanceValidationResultImpl(result, propertyResults);
        propertyResults = instanceValidationResult.getPropertyResults();
        junit.framework.Assert.assertEquals(2, propertyResults.size());
        junit.framework.Assert.assertTrue(propertyResults.containsKey("foo"));
        junit.framework.Assert.assertTrue(propertyResults.containsKey("bar"));
        junit.framework.Assert.assertTrue(propertyResults.get("foo").isValid());
        junit.framework.Assert.assertFalse(propertyResults.get("bar").isValid());
    }

    @org.junit.Test
    public void testToJson() throws java.lang.Exception {
        org.apache.ambari.view.validation.ValidationResult result = new org.apache.ambari.server.view.validation.ValidationResultImpl(true, "detail");
        java.util.Map<java.lang.String, org.apache.ambari.view.validation.ValidationResult> propertyResults = new java.util.LinkedHashMap<>();
        propertyResults.put("foo", new org.apache.ambari.server.view.validation.ValidationResultImpl(true, "foo detail"));
        propertyResults.put("bar", new org.apache.ambari.server.view.validation.ValidationResultImpl(false, "bar detail"));
        org.apache.ambari.server.view.validation.InstanceValidationResultImpl instanceValidationResult = new org.apache.ambari.server.view.validation.InstanceValidationResultImpl(result, propertyResults);
        junit.framework.Assert.assertEquals("{\"propertyResults\":{\"foo\":{\"valid\":true,\"detail\":\"foo detail\"},\"bar\":{\"valid\":false,\"detail\":\"bar detail\"}},\"valid\":false,\"detail\":\"The instance has invalid properties.\"}", instanceValidationResult.toJson());
    }
}