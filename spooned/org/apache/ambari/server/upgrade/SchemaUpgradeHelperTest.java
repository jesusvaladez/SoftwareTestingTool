package org.apache.ambari.server.upgrade;
public class SchemaUpgradeHelperTest {
    private org.apache.ambari.server.upgrade.SchemaUpgradeHelper schemaUpgradeHelper;

    @org.junit.Before
    public void init() {
        com.google.inject.Injector injector = com.google.inject.Guice.createInjector(new org.apache.ambari.server.upgrade.UpgradeHelperTestModule());
        injector.getInstance(com.google.inject.persist.jpa.AmbariJpaPersistService.class).start();
        schemaUpgradeHelper = injector.getInstance(org.apache.ambari.server.upgrade.SchemaUpgradeHelper.class);
    }

    @org.junit.Test
    public void testGetMinimalUpgradeCatalogVersion() throws java.lang.Exception {
        java.lang.reflect.Method getMinimalUpgradeCatalogVersion = schemaUpgradeHelper.getClass().getDeclaredMethod("getMinimalUpgradeCatalogVersion");
        getMinimalUpgradeCatalogVersion.setAccessible(true);
        java.lang.String s = ((java.lang.String) (getMinimalUpgradeCatalogVersion.invoke(schemaUpgradeHelper)));
        org.junit.Assert.assertEquals("0.1.0", s);
    }

    @org.junit.Test
    public void testVerifyUpgradePath() throws java.lang.Exception {
        java.lang.reflect.Method verifyUpgradePath = schemaUpgradeHelper.getClass().getDeclaredMethod("verifyUpgradePath", java.lang.String.class, java.lang.String.class);
        verifyUpgradePath.setAccessible(true);
        boolean failToVerify = ((boolean) (verifyUpgradePath.invoke(schemaUpgradeHelper, "0.3.0", "0.2.0")));
        boolean verifyPassed = ((boolean) (verifyUpgradePath.invoke(schemaUpgradeHelper, "0.1.0", "0.2.0")));
        org.junit.Assert.assertTrue(verifyPassed);
        org.junit.Assert.assertFalse(failToVerify);
    }
}