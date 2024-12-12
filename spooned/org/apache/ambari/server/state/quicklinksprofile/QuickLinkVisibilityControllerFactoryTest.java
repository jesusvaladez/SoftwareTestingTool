package org.apache.ambari.server.state.quicklinksprofile;
public class QuickLinkVisibilityControllerFactoryTest {
    @org.junit.Test
    public void nullStringPassed() throws java.lang.Exception {
        org.junit.Assert.assertTrue(("An instance of " + org.apache.ambari.server.state.quicklinksprofile.ShowAllLinksVisibilityController.class.getSimpleName()) + " should have been returned", org.apache.ambari.server.state.quicklinksprofile.QuickLinkVisibilityControllerFactory.get(null) instanceof org.apache.ambari.server.state.quicklinksprofile.ShowAllLinksVisibilityController);
    }

    @org.junit.Test
    public void invalidJsonPassed() throws java.lang.Exception {
        org.junit.Assert.assertTrue(("An instance of " + org.apache.ambari.server.state.quicklinksprofile.ShowAllLinksVisibilityController.class.getSimpleName()) + " should have been returned", org.apache.ambari.server.state.quicklinksprofile.QuickLinkVisibilityControllerFactory.get("Hello world!") instanceof org.apache.ambari.server.state.quicklinksprofile.ShowAllLinksVisibilityController);
    }

    @org.junit.Test
    public void invalidProfilePassed() throws java.lang.Exception {
        java.lang.String json = com.google.common.io.Resources.toString(com.google.common.io.Resources.getResource("inconsistent_quicklinks_profile.json"), com.google.common.base.Charsets.UTF_8);
        org.junit.Assert.assertTrue(("An instance of " + org.apache.ambari.server.state.quicklinksprofile.ShowAllLinksVisibilityController.class.getSimpleName()) + " should have been returned", org.apache.ambari.server.state.quicklinksprofile.QuickLinkVisibilityControllerFactory.get(json) instanceof org.apache.ambari.server.state.quicklinksprofile.ShowAllLinksVisibilityController);
        json = com.google.common.io.Resources.toString(com.google.common.io.Resources.getResource("inconsistent_quicklinks_profile_2.json"), com.google.common.base.Charsets.UTF_8);
        org.junit.Assert.assertTrue(("An instance of " + org.apache.ambari.server.state.quicklinksprofile.ShowAllLinksVisibilityController.class.getSimpleName()) + " should have been returned", org.apache.ambari.server.state.quicklinksprofile.QuickLinkVisibilityControllerFactory.get(json) instanceof org.apache.ambari.server.state.quicklinksprofile.ShowAllLinksVisibilityController);
    }

    @org.junit.Test
    public void validProfilePassed() throws java.lang.Exception {
        java.lang.String json = com.google.common.io.Resources.toString(com.google.common.io.Resources.getResource("example_quicklinks_profile.json"), com.google.common.base.Charsets.UTF_8);
        org.junit.Assert.assertTrue(("An instance of " + org.apache.ambari.server.state.quicklinksprofile.DefaultQuickLinkVisibilityController.class.getSimpleName()) + " should have been returned", org.apache.ambari.server.state.quicklinksprofile.QuickLinkVisibilityControllerFactory.get(json) instanceof org.apache.ambari.server.state.quicklinksprofile.DefaultQuickLinkVisibilityController);
    }
}