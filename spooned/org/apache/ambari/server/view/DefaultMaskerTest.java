package org.apache.ambari.server.view;
public class DefaultMaskerTest {
    private org.apache.ambari.server.view.DefaultMasker masker = new org.apache.ambari.server.view.DefaultMasker();

    @org.junit.Test
    public void testMask() throws java.lang.Exception {
        final java.lang.String source = "unmasked";
        final java.lang.String masked = masker.mask(source);
        junit.framework.Assert.assertNotNull(masked);
        junit.framework.Assert.assertTrue(masked.length() > 0);
    }

    @org.junit.Test
    public void testMaskUnmask() throws java.lang.Exception {
        final java.lang.String source = "unmasked";
        final java.lang.String masked = masker.mask(source);
        final java.lang.String unmasked = masker.unmask(masked);
        junit.framework.Assert.assertEquals(source, unmasked);
    }
}