package org.apache.ambari.server.view.validation;
public class ValidationResultImplTest {
    @org.junit.Test
    public void testIsValid() throws java.lang.Exception {
        org.apache.ambari.view.validation.ValidationResult result = new org.apache.ambari.server.view.validation.ValidationResultImpl(true, "detail");
        junit.framework.Assert.assertTrue(result.isValid());
        result = new org.apache.ambari.server.view.validation.ValidationResultImpl(false, "detail");
        junit.framework.Assert.assertFalse(result.isValid());
    }

    @org.junit.Test
    public void testGetDetail() throws java.lang.Exception {
        org.apache.ambari.view.validation.ValidationResult result = new org.apache.ambari.server.view.validation.ValidationResultImpl(true, "detail");
        junit.framework.Assert.assertEquals("detail", result.getDetail());
    }

    @org.junit.Test
    public void testCreate() throws java.lang.Exception {
        org.apache.ambari.view.validation.ValidationResult result = org.apache.ambari.server.view.validation.ValidationResultImpl.create(new org.apache.ambari.server.view.validation.ValidationResultImpl(true, "is true"));
        junit.framework.Assert.assertTrue(result.isValid());
        junit.framework.Assert.assertEquals("is true", result.getDetail());
        result = org.apache.ambari.server.view.validation.ValidationResultImpl.create(new org.apache.ambari.server.view.validation.ValidationResultImpl(false, "is false"));
        junit.framework.Assert.assertFalse(result.isValid());
        junit.framework.Assert.assertEquals("is false", result.getDetail());
    }
}