package org.apache.ambari.server.stack;
public class QuickLinksConfigurationModuleTest {
    @org.junit.Test
    public void testAddErrors() {
        java.util.Set<java.lang.String> errors = com.google.common.collect.ImmutableSet.of("one error", "two errors");
        org.apache.ambari.server.stack.QuickLinksConfigurationModule module = new org.apache.ambari.server.stack.QuickLinksConfigurationModule(((java.io.File) (null)));
        module.addErrors(errors);
        org.junit.Assert.assertEquals(errors, com.google.common.collect.ImmutableSet.copyOf(module.getErrors()));
    }

    @org.junit.Test
    public void testResolveInherit() throws java.lang.Exception {
        org.apache.ambari.server.state.quicklinks.QuickLinks[] results = resolveQuickLinks("parent_quicklinks.json", "child_quicklinks_to_inherit.json");
        org.apache.ambari.server.state.quicklinks.QuickLinks parentQuickLinks = results[0];
        org.apache.ambari.server.state.quicklinks.QuickLinks childQuickLinks = results[1];
        org.apache.ambari.server.state.quicklinks.QuickLinksConfiguration childQuickLinksConfig = childQuickLinks.getQuickLinksConfiguration();
        org.junit.Assert.assertNotNull(childQuickLinksConfig);
        java.util.List<org.apache.ambari.server.state.quicklinks.Link> links = childQuickLinksConfig.getLinks();
        org.junit.Assert.assertNotNull(links);
        org.junit.Assert.assertEquals(4, links.size());
        org.junit.Assert.assertEquals(4, parentQuickLinks.getQuickLinksConfiguration().getLinks().size());
        org.apache.ambari.server.state.quicklinks.Protocol protocol = childQuickLinksConfig.getProtocol();
        org.junit.Assert.assertNotNull(protocol);
        org.junit.Assert.assertEquals("https", protocol.getType());
        org.junit.Assert.assertEquals(1, protocol.getChecks().size());
    }

    @org.junit.Test
    public void testResolveMerge() throws java.lang.Exception {
        org.apache.ambari.server.state.quicklinks.QuickLinks[] results = resolveQuickLinks("parent_quicklinks.json", "child_quicklinks_to_merge.json");
        org.apache.ambari.server.state.quicklinks.QuickLinks parentQuickLinks = results[0];
        org.apache.ambari.server.state.quicklinks.QuickLinks childQuickLinks = results[1];
        org.apache.ambari.server.state.quicklinks.QuickLinksConfiguration childQuickLinksConfig = childQuickLinks.getQuickLinksConfiguration();
        org.junit.Assert.assertNotNull(childQuickLinksConfig);
        java.util.List<org.apache.ambari.server.state.quicklinks.Link> links = childQuickLinksConfig.getLinks();
        org.junit.Assert.assertNotNull(links);
        org.junit.Assert.assertEquals(7, links.size());
        org.junit.Assert.assertEquals(4, parentQuickLinks.getQuickLinksConfiguration().getLinks().size());
        org.apache.ambari.server.state.quicklinks.Link threadStacks = getLink(links, "thread_stacks");
        org.junit.Assert.assertNotNull("https_regex property should have been inherited", threadStacks.getPort().getHttpsRegex());
    }

    private org.apache.ambari.server.state.quicklinks.Link getLink(java.util.Collection<org.apache.ambari.server.state.quicklinks.Link> links, java.lang.String name) {
        for (org.apache.ambari.server.state.quicklinks.Link link : links) {
            if (name.equals(link.getName())) {
                return link;
            }
        }
        throw new java.util.NoSuchElementException("name");
    }

    @org.junit.Test
    public void testResolveOverride() throws java.lang.Exception {
        org.apache.ambari.server.state.quicklinks.QuickLinks[] results = resolveQuickLinks("parent_quicklinks.json", "child_quicklinks_to_override.json");
        org.apache.ambari.server.state.quicklinks.QuickLinks parentQuickLinks = results[0];
        org.apache.ambari.server.state.quicklinks.QuickLinks childQuickLinks = results[1];
        org.apache.ambari.server.state.quicklinks.QuickLinksConfiguration childQuickLinksConfig = childQuickLinks.getQuickLinksConfiguration();
        org.junit.Assert.assertNotNull(childQuickLinksConfig);
        java.util.List<org.apache.ambari.server.state.quicklinks.Link> links = childQuickLinksConfig.getLinks();
        org.junit.Assert.assertNotNull(links);
        org.junit.Assert.assertEquals(7, links.size());
        org.junit.Assert.assertEquals(4, parentQuickLinks.getQuickLinksConfiguration().getLinks().size());
        boolean hasLink = false;
        for (org.apache.ambari.server.state.quicklinks.Link link : links) {
            java.lang.String name = link.getName();
            if ("thread_stacks".equals(name)) {
                hasLink = true;
                org.apache.ambari.server.state.quicklinks.Port port = link.getPort();
                org.junit.Assert.assertEquals("mapred-site", port.getSite());
                org.apache.ambari.server.state.quicklinks.Host host = link.getHost();
                org.junit.Assert.assertEquals("core-site", host.getSite());
            }
        }
        org.junit.Assert.assertTrue(hasLink);
        org.apache.ambari.server.state.quicklinks.Protocol protocol = childQuickLinksConfig.getProtocol();
        org.junit.Assert.assertNotNull(protocol);
        org.junit.Assert.assertEquals("http", protocol.getType());
        org.junit.Assert.assertEquals(3, protocol.getChecks().size());
        java.util.List<org.apache.ambari.server.state.quicklinks.Check> checks = protocol.getChecks();
        for (org.apache.ambari.server.state.quicklinks.Check check : checks) {
            org.junit.Assert.assertEquals("mapred-site", check.getSite());
        }
    }

    @org.junit.Test
    public void testResolveOverrideProperties() throws java.lang.Exception {
        org.apache.ambari.server.state.quicklinks.QuickLinks[] results = resolveQuickLinks("parent_quicklinks_with_attributes.json", "child_quicklinks_with_attributes.json");
        org.apache.ambari.server.state.quicklinks.QuickLinks parentQuickLinks = results[0];
        org.apache.ambari.server.state.quicklinks.QuickLinks childQuickLinks = results[1];
        org.apache.ambari.server.state.quicklinks.QuickLinksConfiguration childQuickLinksConfig = childQuickLinks.getQuickLinksConfiguration();
        org.junit.Assert.assertNotNull(childQuickLinksConfig);
        java.util.List<org.apache.ambari.server.state.quicklinks.Link> links = childQuickLinksConfig.getLinks();
        org.junit.Assert.assertNotNull(links);
        org.junit.Assert.assertEquals(3, links.size());
        java.util.Map<java.lang.String, org.apache.ambari.server.state.quicklinks.Link> linksByName = new java.util.HashMap<>();
        for (org.apache.ambari.server.state.quicklinks.Link link : links) {
            linksByName.put(link.getName(), link);
        }
        org.junit.Assert.assertEquals("Links are not properly overridden for foo_ui", com.google.common.collect.Lists.newArrayList("authenticated", "sso"), linksByName.get("foo_ui").getAttributes());
        org.junit.Assert.assertEquals("Parent links for foo_jmx are not inherited.", com.google.common.collect.Lists.newArrayList("authenticated"), linksByName.get("foo_jmx").getAttributes());
        org.junit.Assert.assertEquals("Links are not properly overridden for foo_logs", new java.util.ArrayList<>(), linksByName.get("foo_logs").getAttributes());
    }

    private org.apache.ambari.server.state.quicklinks.QuickLinks[] resolveQuickLinks(java.lang.String parentJson, java.lang.String childJson) throws org.apache.ambari.server.AmbariException {
        java.io.File parentQuiclinksFile = new java.io.File(this.getClass().getClassLoader().getResource(parentJson).getFile());
        java.io.File childQuickLinksFile = new java.io.File(this.getClass().getClassLoader().getResource(childJson).getFile());
        org.apache.ambari.server.stack.QuickLinksConfigurationModule parentModule = new org.apache.ambari.server.stack.QuickLinksConfigurationModule(parentQuiclinksFile);
        org.apache.ambari.server.stack.QuickLinksConfigurationModule childModule = new org.apache.ambari.server.stack.QuickLinksConfigurationModule(childQuickLinksFile);
        childModule.resolve(parentModule, null, null, null);
        org.apache.ambari.server.state.quicklinks.QuickLinks parentQuickLinks = parentModule.getModuleInfo().getQuickLinksConfigurationMap().get(org.apache.ambari.server.stack.QuickLinksConfigurationModule.QUICKLINKS_CONFIGURATION_KEY);
        org.apache.ambari.server.state.quicklinks.QuickLinks childQuickLinks = childModule.getModuleInfo().getQuickLinksConfigurationMap().get(org.apache.ambari.server.stack.QuickLinksConfigurationModule.QUICKLINKS_CONFIGURATION_KEY);
        return new org.apache.ambari.server.state.quicklinks.QuickLinks[]{ parentQuickLinks, childQuickLinks };
    }
}