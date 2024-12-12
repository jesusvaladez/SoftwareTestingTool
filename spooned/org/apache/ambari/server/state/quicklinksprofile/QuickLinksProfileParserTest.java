package org.apache.ambari.server.state.quicklinksprofile;
public class QuickLinksProfileParserTest {
    @org.junit.Test
    public void testParseProfile() throws java.lang.Exception {
        java.lang.String profileName = "example_quicklinks_profile.json";
        org.apache.ambari.server.state.quicklinksprofile.QuickLinksProfileParser parser = new org.apache.ambari.server.state.quicklinksprofile.QuickLinksProfileParser();
        org.apache.ambari.server.state.quicklinksprofile.QuickLinksProfile profile = parser.parse(com.google.common.io.Resources.getResource(profileName));
        org.junit.Assert.assertEquals(1, profile.getFilters().size());
        org.junit.Assert.assertEquals(org.apache.ambari.server.state.quicklinksprofile.Filter.linkAttributeFilter("sso", true), profile.getFilters().get(0));
        org.junit.Assert.assertEquals(3, profile.getServices().size());
        org.apache.ambari.server.state.quicklinksprofile.Service hdfs = profile.getServices().get(0);
        org.junit.Assert.assertEquals("HDFS", hdfs.getName());
        org.junit.Assert.assertEquals(1, hdfs.getFilters().size());
        org.junit.Assert.assertEquals(1, hdfs.getComponents().size());
        org.junit.Assert.assertEquals(org.apache.ambari.server.state.quicklinksprofile.Filter.linkAttributeFilter("authenticated", true), hdfs.getFilters().get(0));
        org.apache.ambari.server.state.quicklinksprofile.Component nameNode = hdfs.getComponents().get(0);
        org.junit.Assert.assertEquals(2, nameNode.getFilters().size());
        org.junit.Assert.assertEquals(org.apache.ambari.server.state.quicklinksprofile.Filter.linkNameFilter("namenode_ui", "http://customlink.org/namenode", false), nameNode.getFilters().get(0));
        org.apache.ambari.server.state.quicklinksprofile.Component historyServer = profile.getServices().get(1).getComponents().get(0);
        org.junit.Assert.assertEquals(1, historyServer.getFilters().size());
        org.junit.Assert.assertEquals(org.apache.ambari.server.state.quicklinksprofile.Filter.acceptAllFilter(true), historyServer.getFilters().get(0));
        org.apache.ambari.server.state.quicklinksprofile.Service yarn = profile.getServices().get(2);
        org.junit.Assert.assertEquals(1, yarn.getFilters().size());
        org.junit.Assert.assertEquals(org.apache.ambari.server.state.quicklinksprofile.Filter.linkNameFilter("resourcemanager_ui", "http://customlink.org/resourcemanager", true), yarn.getFilters().get(0));
    }

    @org.junit.Test(expected = com.fasterxml.jackson.core.JsonProcessingException.class)
    public void testParseInconsistentProfile_ambigousFilterDefinition() throws java.lang.Exception {
        java.lang.String profileName = "inconsistent_quicklinks_profile.json";
        org.apache.ambari.server.state.quicklinksprofile.QuickLinksProfileParser parser = new org.apache.ambari.server.state.quicklinksprofile.QuickLinksProfileParser();
        parser.parse(com.google.common.io.Resources.getResource(profileName));
    }

    @org.junit.Test(expected = com.fasterxml.jackson.core.JsonProcessingException.class)
    public void testParseInconsistentProfile_invalidLinkUrl() throws java.lang.Exception {
        java.lang.String profileName = "inconsistent_quicklinks_profile_4.json";
        org.apache.ambari.server.state.quicklinksprofile.QuickLinksProfileParser parser = new org.apache.ambari.server.state.quicklinksprofile.QuickLinksProfileParser();
        parser.parse(com.google.common.io.Resources.getResource(profileName));
    }

    @org.junit.Test(expected = com.fasterxml.jackson.core.JsonProcessingException.class)
    public void testParseInconsistentProfile_misspelledFilerDefinition() throws java.lang.Exception {
        java.lang.String profileName = "inconsistent_quicklinks_profile_3.json";
        org.apache.ambari.server.state.quicklinksprofile.QuickLinksProfileParser parser = new org.apache.ambari.server.state.quicklinksprofile.QuickLinksProfileParser();
        parser.parse(com.google.common.io.Resources.getResource(profileName));
    }
}