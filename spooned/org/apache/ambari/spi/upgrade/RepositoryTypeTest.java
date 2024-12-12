package org.apache.ambari.spi.upgrade;
public class RepositoryTypeTest {
    @org.junit.Test
    public void testIsRevertable() throws java.lang.Exception {
        org.junit.Assert.assertTrue(org.apache.ambari.spi.RepositoryType.MAINT.isRevertable());
        org.junit.Assert.assertTrue(org.apache.ambari.spi.RepositoryType.PATCH.isRevertable());
        org.junit.Assert.assertFalse(org.apache.ambari.spi.RepositoryType.STANDARD.isRevertable());
    }
}