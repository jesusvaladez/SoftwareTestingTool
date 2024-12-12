package org.apache.ambari.server.checks;
public class UpgradeTypeQualificationTest {
    @org.junit.Test
    public void testRequired() throws java.lang.Exception {
        org.apache.ambari.spi.upgrade.UpgradeCheckRequest rolling = new org.apache.ambari.spi.upgrade.UpgradeCheckRequest(null, org.apache.ambari.spi.upgrade.UpgradeType.ROLLING, null, null, null);
        org.apache.ambari.spi.upgrade.UpgradeCheckRequest express = new org.apache.ambari.spi.upgrade.UpgradeCheckRequest(null, org.apache.ambari.spi.upgrade.UpgradeType.NON_ROLLING, null, null, null);
        org.apache.ambari.server.checks.UpgradeTypeQualification rollingQualification = new org.apache.ambari.server.checks.UpgradeTypeQualification(org.apache.ambari.server.checks.UpgradeTypeQualificationTest.RollingTestCheckImpl.class);
        junit.framework.Assert.assertTrue(rollingQualification.isApplicable(rolling));
        junit.framework.Assert.assertFalse(rollingQualification.isApplicable(express));
        org.apache.ambari.server.checks.UpgradeTypeQualification notRequiredQualification = new org.apache.ambari.server.checks.UpgradeTypeQualification(org.apache.ambari.server.checks.UpgradeTypeQualificationTest.NotRequiredCheckTest.class);
        junit.framework.Assert.assertTrue(notRequiredQualification.isApplicable(rolling));
        junit.framework.Assert.assertTrue(notRequiredQualification.isApplicable(express));
    }

    @org.apache.ambari.annotations.UpgradeCheckInfo(group = org.apache.ambari.spi.upgrade.UpgradeCheckGroup.DEFAULT, order = 1.0F, required = { org.apache.ambari.spi.upgrade.UpgradeType.ROLLING })
    private class RollingTestCheckImpl implements org.apache.ambari.spi.upgrade.UpgradeCheck {
        @java.lang.Override
        public java.util.Set<java.lang.String> getApplicableServices() {
            return null;
        }

        @java.lang.Override
        public java.util.List<org.apache.ambari.spi.upgrade.CheckQualification> getQualifications() {
            return null;
        }

        @java.lang.Override
        public org.apache.ambari.spi.upgrade.UpgradeCheckResult perform(org.apache.ambari.spi.upgrade.UpgradeCheckRequest request) throws org.apache.ambari.server.AmbariException {
            return null;
        }

        @java.lang.Override
        public org.apache.ambari.spi.upgrade.UpgradeCheckDescription getCheckDescription() {
            return null;
        }
    }

    @org.apache.ambari.annotations.UpgradeCheckInfo(group = org.apache.ambari.spi.upgrade.UpgradeCheckGroup.DEFAULT, order = 1.0F)
    private class NotRequiredCheckTest implements org.apache.ambari.spi.upgrade.UpgradeCheck {
        @java.lang.Override
        public java.util.Set<java.lang.String> getApplicableServices() {
            return null;
        }

        @java.lang.Override
        public java.util.List<org.apache.ambari.spi.upgrade.CheckQualification> getQualifications() {
            return null;
        }

        @java.lang.Override
        public org.apache.ambari.spi.upgrade.UpgradeCheckResult perform(org.apache.ambari.spi.upgrade.UpgradeCheckRequest request) throws org.apache.ambari.server.AmbariException {
            return null;
        }

        @java.lang.Override
        public org.apache.ambari.spi.upgrade.UpgradeCheckDescription getCheckDescription() {
            return null;
        }
    }
}