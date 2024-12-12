package org.apache.ambari.server.state.quicklinksprofile;
public class QuickLinkVisibilityControllerTest {
    static final java.lang.String AUTHENTICATED = "authenticated";

    static final java.lang.String SSO = "sso";

    static final java.lang.String NAMENODE = "NAMENODE";

    static final java.lang.String HDFS = "HDFS";

    public static final java.lang.String YARN = "YARN";

    static final java.lang.String NAMENODE_UI = "namenode_ui";

    static final java.lang.String NAMENODE_LOGS = "namenode_logs";

    static final java.lang.String NAMENODE_JMX = "namenode_jmx";

    static final java.lang.String THREAD_STACKS = "Thread Stacks";

    static final java.lang.String LINK_URL_1 = "www.overridden.org/1";

    static final java.lang.String LINK_URL_2 = "www.overridden.org/2";

    static final java.lang.String LINK_URL_3 = "www.overridden.org/3";

    private org.apache.ambari.server.state.quicklinks.Link namenodeUi;

    private org.apache.ambari.server.state.quicklinks.Link namenodeLogs;

    private org.apache.ambari.server.state.quicklinks.Link namenodeJmx;

    private org.apache.ambari.server.state.quicklinks.Link threadStacks;

    public QuickLinkVisibilityControllerTest() {
        namenodeUi = org.apache.ambari.server.state.quicklinksprofile.QuickLinkVisibilityControllerTest.link(org.apache.ambari.server.state.quicklinksprofile.QuickLinkVisibilityControllerTest.NAMENODE_UI, org.apache.ambari.server.state.quicklinksprofile.QuickLinkVisibilityControllerTest.NAMENODE, com.google.common.collect.ImmutableList.of(org.apache.ambari.server.state.quicklinksprofile.QuickLinkVisibilityControllerTest.AUTHENTICATED));
        namenodeLogs = org.apache.ambari.server.state.quicklinksprofile.QuickLinkVisibilityControllerTest.link(org.apache.ambari.server.state.quicklinksprofile.QuickLinkVisibilityControllerTest.NAMENODE_LOGS, org.apache.ambari.server.state.quicklinksprofile.QuickLinkVisibilityControllerTest.NAMENODE, null);
        namenodeJmx = org.apache.ambari.server.state.quicklinksprofile.QuickLinkVisibilityControllerTest.link(org.apache.ambari.server.state.quicklinksprofile.QuickLinkVisibilityControllerTest.NAMENODE_JMX, org.apache.ambari.server.state.quicklinksprofile.QuickLinkVisibilityControllerTest.NAMENODE, null);
        threadStacks = org.apache.ambari.server.state.quicklinksprofile.QuickLinkVisibilityControllerTest.link(org.apache.ambari.server.state.quicklinksprofile.QuickLinkVisibilityControllerTest.THREAD_STACKS, org.apache.ambari.server.state.quicklinksprofile.QuickLinkVisibilityControllerTest.NAMENODE, null);
    }

    @org.junit.Test
    public void testNullsAreAccepted() throws java.lang.Exception {
        org.apache.ambari.server.state.quicklinksprofile.QuickLinksProfile profile = org.apache.ambari.server.state.quicklinksprofile.QuickLinksProfile.create(com.google.common.collect.ImmutableList.of(org.apache.ambari.server.state.quicklinksprofile.Filter.acceptAllFilter(true)), null);
        org.apache.ambari.server.state.quicklinksprofile.DefaultQuickLinkVisibilityController evaluator = new org.apache.ambari.server.state.quicklinksprofile.DefaultQuickLinkVisibilityController(profile);
        evaluator.isVisible(org.apache.ambari.server.state.quicklinksprofile.QuickLinkVisibilityControllerTest.HDFS, namenodeUi);
        org.apache.ambari.server.state.quicklinksprofile.Service service = org.apache.ambari.server.state.quicklinksprofile.Service.create(org.apache.ambari.server.state.quicklinksprofile.QuickLinkVisibilityControllerTest.HDFS, com.google.common.collect.ImmutableList.of(org.apache.ambari.server.state.quicklinksprofile.Filter.acceptAllFilter(true)), null);
        profile = org.apache.ambari.server.state.quicklinksprofile.QuickLinksProfile.create(null, com.google.common.collect.ImmutableList.of(service));
        evaluator = new org.apache.ambari.server.state.quicklinksprofile.DefaultQuickLinkVisibilityController(profile);
        evaluator.isVisible(org.apache.ambari.server.state.quicklinksprofile.QuickLinkVisibilityControllerTest.HDFS, namenodeUi);
    }

    @org.junit.Test(expected = org.apache.ambari.server.state.quicklinksprofile.QuickLinksProfileEvaluationException.class)
    public void testProfileMustContainAtLeastOneFilter() throws java.lang.Exception {
        org.apache.ambari.server.state.quicklinksprofile.Component component = org.apache.ambari.server.state.quicklinksprofile.Component.create("NAMENODE", null);
        org.apache.ambari.server.state.quicklinksprofile.Service service = org.apache.ambari.server.state.quicklinksprofile.Service.create(org.apache.ambari.server.state.quicklinksprofile.QuickLinkVisibilityControllerTest.HDFS, null, com.google.common.collect.ImmutableList.of(component));
        org.apache.ambari.server.state.quicklinksprofile.QuickLinksProfile profile = org.apache.ambari.server.state.quicklinksprofile.QuickLinksProfile.create(null, com.google.common.collect.ImmutableList.of(service));
        org.apache.ambari.server.state.quicklinksprofile.QuickLinkVisibilityController evaluator = new org.apache.ambari.server.state.quicklinksprofile.DefaultQuickLinkVisibilityController(profile);
    }

    @org.junit.Test
    public void testLinkWithNoComponentField() throws java.lang.Exception {
        org.apache.ambari.server.state.quicklinksprofile.Component component = org.apache.ambari.server.state.quicklinksprofile.Component.create(org.apache.ambari.server.state.quicklinksprofile.QuickLinkVisibilityControllerTest.NAMENODE, com.google.common.collect.ImmutableList.of(org.apache.ambari.server.state.quicklinksprofile.Filter.linkNameFilter(org.apache.ambari.server.state.quicklinksprofile.QuickLinkVisibilityControllerTest.NAMENODE_UI, true)));
        org.apache.ambari.server.state.quicklinksprofile.Service service = org.apache.ambari.server.state.quicklinksprofile.Service.create(org.apache.ambari.server.state.quicklinksprofile.QuickLinkVisibilityControllerTest.HDFS, com.google.common.collect.ImmutableList.of(), com.google.common.collect.ImmutableList.of(component));
        org.apache.ambari.server.state.quicklinksprofile.QuickLinksProfile profile = org.apache.ambari.server.state.quicklinksprofile.QuickLinksProfile.create(com.google.common.collect.ImmutableList.of(), com.google.common.collect.ImmutableList.of(service));
        org.apache.ambari.server.state.quicklinksprofile.DefaultQuickLinkVisibilityController evaluator = new org.apache.ambari.server.state.quicklinksprofile.DefaultQuickLinkVisibilityController(profile);
        namenodeUi.setComponentName(null);
        org.junit.Assert.assertFalse("Link should be hidden as there are no applicable filters", evaluator.isVisible(org.apache.ambari.server.state.quicklinksprofile.QuickLinkVisibilityControllerTest.HDFS, namenodeUi));
    }

    @org.junit.Test
    public void testComponentLevelFiltersEvaluatedFirst() throws java.lang.Exception {
        org.apache.ambari.server.state.quicklinksprofile.Component component = org.apache.ambari.server.state.quicklinksprofile.Component.create(org.apache.ambari.server.state.quicklinksprofile.QuickLinkVisibilityControllerTest.NAMENODE, com.google.common.collect.ImmutableList.of(org.apache.ambari.server.state.quicklinksprofile.Filter.linkAttributeFilter(org.apache.ambari.server.state.quicklinksprofile.QuickLinkVisibilityControllerTest.AUTHENTICATED, true)));
        org.apache.ambari.server.state.quicklinksprofile.Service service = org.apache.ambari.server.state.quicklinksprofile.Service.create(org.apache.ambari.server.state.quicklinksprofile.QuickLinkVisibilityControllerTest.HDFS, com.google.common.collect.ImmutableList.of(org.apache.ambari.server.state.quicklinksprofile.Filter.linkAttributeFilter(org.apache.ambari.server.state.quicklinksprofile.QuickLinkVisibilityControllerTest.AUTHENTICATED, false)), com.google.common.collect.ImmutableList.of(component));
        org.apache.ambari.server.state.quicklinksprofile.QuickLinksProfile profile = org.apache.ambari.server.state.quicklinksprofile.QuickLinksProfile.create(com.google.common.collect.ImmutableList.of(org.apache.ambari.server.state.quicklinksprofile.Filter.acceptAllFilter(false)), com.google.common.collect.ImmutableList.of(service));
        org.apache.ambari.server.state.quicklinksprofile.DefaultQuickLinkVisibilityController evaluator = new org.apache.ambari.server.state.quicklinksprofile.DefaultQuickLinkVisibilityController(profile);
        org.junit.Assert.assertTrue("Component level filter should have been applied.", evaluator.isVisible(org.apache.ambari.server.state.quicklinksprofile.QuickLinkVisibilityControllerTest.HDFS, namenodeUi));
    }

    @org.junit.Test
    public void testServiceLevelFiltersEvaluatedSecondly() throws java.lang.Exception {
        org.apache.ambari.server.state.quicklinksprofile.Component component = org.apache.ambari.server.state.quicklinksprofile.Component.create(org.apache.ambari.server.state.quicklinksprofile.QuickLinkVisibilityControllerTest.NAMENODE, com.google.common.collect.ImmutableList.of(org.apache.ambari.server.state.quicklinksprofile.Filter.linkAttributeFilter(org.apache.ambari.server.state.quicklinksprofile.QuickLinkVisibilityControllerTest.SSO, false)));
        org.apache.ambari.server.state.quicklinksprofile.Service service = org.apache.ambari.server.state.quicklinksprofile.Service.create(org.apache.ambari.server.state.quicklinksprofile.QuickLinkVisibilityControllerTest.HDFS, com.google.common.collect.ImmutableList.of(org.apache.ambari.server.state.quicklinksprofile.Filter.linkAttributeFilter(org.apache.ambari.server.state.quicklinksprofile.QuickLinkVisibilityControllerTest.AUTHENTICATED, true)), com.google.common.collect.ImmutableList.of(component));
        org.apache.ambari.server.state.quicklinksprofile.QuickLinksProfile profile = org.apache.ambari.server.state.quicklinksprofile.QuickLinksProfile.create(com.google.common.collect.ImmutableList.of(org.apache.ambari.server.state.quicklinksprofile.Filter.acceptAllFilter(false)), com.google.common.collect.ImmutableList.of(service));
        org.apache.ambari.server.state.quicklinksprofile.DefaultQuickLinkVisibilityController evaluator = new org.apache.ambari.server.state.quicklinksprofile.DefaultQuickLinkVisibilityController(profile);
        org.junit.Assert.assertTrue("Component level filter should have been applied.", evaluator.isVisible(org.apache.ambari.server.state.quicklinksprofile.QuickLinkVisibilityControllerTest.HDFS, namenodeUi));
    }

    @org.junit.Test
    public void testGlobalFiltersEvaluatedLast() throws java.lang.Exception {
        org.apache.ambari.server.state.quicklinksprofile.Component component = org.apache.ambari.server.state.quicklinksprofile.Component.create(org.apache.ambari.server.state.quicklinksprofile.QuickLinkVisibilityControllerTest.NAMENODE, com.google.common.collect.ImmutableList.of(org.apache.ambari.server.state.quicklinksprofile.Filter.linkAttributeFilter(org.apache.ambari.server.state.quicklinksprofile.QuickLinkVisibilityControllerTest.SSO, false)));
        org.apache.ambari.server.state.quicklinksprofile.Service service = org.apache.ambari.server.state.quicklinksprofile.Service.create(org.apache.ambari.server.state.quicklinksprofile.QuickLinkVisibilityControllerTest.HDFS, com.google.common.collect.ImmutableList.of(org.apache.ambari.server.state.quicklinksprofile.Filter.linkAttributeFilter(org.apache.ambari.server.state.quicklinksprofile.QuickLinkVisibilityControllerTest.SSO, false)), com.google.common.collect.ImmutableList.of(component));
        org.apache.ambari.server.state.quicklinksprofile.QuickLinksProfile profile = org.apache.ambari.server.state.quicklinksprofile.QuickLinksProfile.create(com.google.common.collect.ImmutableList.of(org.apache.ambari.server.state.quicklinksprofile.Filter.acceptAllFilter(true)), com.google.common.collect.ImmutableList.of(service));
        org.apache.ambari.server.state.quicklinksprofile.DefaultQuickLinkVisibilityController evaluator = new org.apache.ambari.server.state.quicklinksprofile.DefaultQuickLinkVisibilityController(profile);
        org.junit.Assert.assertTrue("Global filter should have been applied.", evaluator.isVisible(org.apache.ambari.server.state.quicklinksprofile.QuickLinkVisibilityControllerTest.HDFS, namenodeUi));
    }

    @org.junit.Test
    public void testNoMatchingRule() throws java.lang.Exception {
        org.apache.ambari.server.state.quicklinksprofile.Component component1 = org.apache.ambari.server.state.quicklinksprofile.Component.create(org.apache.ambari.server.state.quicklinksprofile.QuickLinkVisibilityControllerTest.NAMENODE, com.google.common.collect.ImmutableList.of(org.apache.ambari.server.state.quicklinksprofile.Filter.linkAttributeFilter(org.apache.ambari.server.state.quicklinksprofile.QuickLinkVisibilityControllerTest.SSO, true)));
        org.apache.ambari.server.state.quicklinksprofile.Component component2 = org.apache.ambari.server.state.quicklinksprofile.Component.create("DATANODE", com.google.common.collect.ImmutableList.of(org.apache.ambari.server.state.quicklinksprofile.Filter.acceptAllFilter(true)));
        org.apache.ambari.server.state.quicklinksprofile.Service service1 = org.apache.ambari.server.state.quicklinksprofile.Service.create(org.apache.ambari.server.state.quicklinksprofile.QuickLinkVisibilityControllerTest.HDFS, com.google.common.collect.ImmutableList.of(org.apache.ambari.server.state.quicklinksprofile.Filter.linkAttributeFilter(org.apache.ambari.server.state.quicklinksprofile.QuickLinkVisibilityControllerTest.SSO, true)), com.google.common.collect.ImmutableList.of(component1, component2));
        org.apache.ambari.server.state.quicklinksprofile.Service service2 = org.apache.ambari.server.state.quicklinksprofile.Service.create("YARN", com.google.common.collect.ImmutableList.of(org.apache.ambari.server.state.quicklinksprofile.Filter.acceptAllFilter(true)), com.google.common.collect.ImmutableList.of());
        org.apache.ambari.server.state.quicklinksprofile.QuickLinksProfile profile = org.apache.ambari.server.state.quicklinksprofile.QuickLinksProfile.create(com.google.common.collect.ImmutableList.of(org.apache.ambari.server.state.quicklinksprofile.Filter.linkAttributeFilter(org.apache.ambari.server.state.quicklinksprofile.QuickLinkVisibilityControllerTest.SSO, true)), com.google.common.collect.ImmutableList.of(service1, service2));
        org.apache.ambari.server.state.quicklinksprofile.DefaultQuickLinkVisibilityController evaluator = new org.apache.ambari.server.state.quicklinksprofile.DefaultQuickLinkVisibilityController(profile);
        org.junit.Assert.assertFalse("No filters should have been applied, so default false should have been returned.", evaluator.isVisible(org.apache.ambari.server.state.quicklinksprofile.QuickLinkVisibilityControllerTest.HDFS, namenodeUi));
    }

    @org.junit.Test
    public void testUrlOverride() throws java.lang.Exception {
        org.apache.ambari.server.state.quicklinksprofile.Component nameNode = org.apache.ambari.server.state.quicklinksprofile.Component.create(org.apache.ambari.server.state.quicklinksprofile.QuickLinkVisibilityControllerTest.NAMENODE, com.google.common.collect.ImmutableList.of(org.apache.ambari.server.state.quicklinksprofile.Filter.linkNameFilter(org.apache.ambari.server.state.quicklinksprofile.QuickLinkVisibilityControllerTest.NAMENODE_UI, true), org.apache.ambari.server.state.quicklinksprofile.Filter.linkNameFilter(org.apache.ambari.server.state.quicklinksprofile.QuickLinkVisibilityControllerTest.NAMENODE_LOGS, org.apache.ambari.server.state.quicklinksprofile.QuickLinkVisibilityControllerTest.LINK_URL_1, true)));
        org.apache.ambari.server.state.quicklinksprofile.Service hdfs = org.apache.ambari.server.state.quicklinksprofile.Service.create(org.apache.ambari.server.state.quicklinksprofile.QuickLinkVisibilityControllerTest.HDFS, com.google.common.collect.ImmutableList.of(org.apache.ambari.server.state.quicklinksprofile.Filter.linkNameFilter(org.apache.ambari.server.state.quicklinksprofile.QuickLinkVisibilityControllerTest.NAMENODE_JMX, org.apache.ambari.server.state.quicklinksprofile.QuickLinkVisibilityControllerTest.LINK_URL_2, true)), com.google.common.collect.ImmutableList.of(nameNode));
        org.apache.ambari.server.state.quicklinksprofile.QuickLinksProfile profile = org.apache.ambari.server.state.quicklinksprofile.QuickLinksProfile.create(com.google.common.collect.ImmutableList.of(org.apache.ambari.server.state.quicklinksprofile.Filter.linkNameFilter(org.apache.ambari.server.state.quicklinksprofile.QuickLinkVisibilityControllerTest.THREAD_STACKS, org.apache.ambari.server.state.quicklinksprofile.QuickLinkVisibilityControllerTest.LINK_URL_3, true)), com.google.common.collect.ImmutableList.of(hdfs));
        org.apache.ambari.server.state.quicklinksprofile.DefaultQuickLinkVisibilityController evaluator = new org.apache.ambari.server.state.quicklinksprofile.DefaultQuickLinkVisibilityController(profile);
        org.junit.Assert.assertEquals(java.util.Optional.empty(), evaluator.getUrlOverride(org.apache.ambari.server.state.quicklinksprofile.QuickLinkVisibilityControllerTest.HDFS, namenodeUi));
        org.junit.Assert.assertEquals(java.util.Optional.of(org.apache.ambari.server.state.quicklinksprofile.QuickLinkVisibilityControllerTest.LINK_URL_1), evaluator.getUrlOverride(org.apache.ambari.server.state.quicklinksprofile.QuickLinkVisibilityControllerTest.HDFS, namenodeLogs));
        org.junit.Assert.assertEquals(java.util.Optional.of(org.apache.ambari.server.state.quicklinksprofile.QuickLinkVisibilityControllerTest.LINK_URL_2), evaluator.getUrlOverride(org.apache.ambari.server.state.quicklinksprofile.QuickLinkVisibilityControllerTest.HDFS, namenodeJmx));
        namenodeLogs.setComponentName(null);
        org.junit.Assert.assertEquals(java.util.Optional.of(org.apache.ambari.server.state.quicklinksprofile.QuickLinkVisibilityControllerTest.LINK_URL_1), evaluator.getUrlOverride(org.apache.ambari.server.state.quicklinksprofile.QuickLinkVisibilityControllerTest.HDFS, namenodeLogs));
        org.junit.Assert.assertEquals(java.util.Optional.empty(), evaluator.getUrlOverride(org.apache.ambari.server.state.quicklinksprofile.QuickLinkVisibilityControllerTest.YARN, org.apache.ambari.server.state.quicklinksprofile.QuickLinkVisibilityControllerTest.link("resourcemanager_ui", "RESOURCEMANAGER", null)));
        org.junit.Assert.assertEquals(java.util.Optional.empty(), evaluator.getUrlOverride(org.apache.ambari.server.state.quicklinksprofile.QuickLinkVisibilityControllerTest.HDFS, threadStacks));
    }

    @org.junit.Test
    public void testUrlOverride_duplicateDefinitions() throws java.lang.Exception {
        org.apache.ambari.server.state.quicklinksprofile.Component nameNode = org.apache.ambari.server.state.quicklinksprofile.Component.create(org.apache.ambari.server.state.quicklinksprofile.QuickLinkVisibilityControllerTest.NAMENODE, com.google.common.collect.ImmutableList.of(org.apache.ambari.server.state.quicklinksprofile.Filter.linkNameFilter(org.apache.ambari.server.state.quicklinksprofile.QuickLinkVisibilityControllerTest.NAMENODE_UI, org.apache.ambari.server.state.quicklinksprofile.QuickLinkVisibilityControllerTest.LINK_URL_1, true)));
        org.apache.ambari.server.state.quicklinksprofile.Service hdfs = org.apache.ambari.server.state.quicklinksprofile.Service.create(org.apache.ambari.server.state.quicklinksprofile.QuickLinkVisibilityControllerTest.HDFS, com.google.common.collect.ImmutableList.of(org.apache.ambari.server.state.quicklinksprofile.Filter.linkNameFilter(org.apache.ambari.server.state.quicklinksprofile.QuickLinkVisibilityControllerTest.NAMENODE_UI, org.apache.ambari.server.state.quicklinksprofile.QuickLinkVisibilityControllerTest.LINK_URL_2, true)), com.google.common.collect.ImmutableList.of(nameNode));
        org.apache.ambari.server.state.quicklinksprofile.Service yarn = org.apache.ambari.server.state.quicklinksprofile.Service.create(org.apache.ambari.server.state.quicklinksprofile.QuickLinkVisibilityControllerTest.YARN, com.google.common.collect.ImmutableList.of(org.apache.ambari.server.state.quicklinksprofile.Filter.linkNameFilter(org.apache.ambari.server.state.quicklinksprofile.QuickLinkVisibilityControllerTest.NAMENODE_UI, org.apache.ambari.server.state.quicklinksprofile.QuickLinkVisibilityControllerTest.LINK_URL_3, true)), com.google.common.collect.ImmutableList.of(nameNode));
        org.apache.ambari.server.state.quicklinksprofile.QuickLinksProfile profile = org.apache.ambari.server.state.quicklinksprofile.QuickLinksProfile.create(com.google.common.collect.ImmutableList.of(), com.google.common.collect.ImmutableList.of(hdfs));
        org.apache.ambari.server.state.quicklinksprofile.DefaultQuickLinkVisibilityController evaluator = new org.apache.ambari.server.state.quicklinksprofile.DefaultQuickLinkVisibilityController(profile);
        org.junit.Assert.assertEquals(java.util.Optional.of(org.apache.ambari.server.state.quicklinksprofile.QuickLinkVisibilityControllerTest.LINK_URL_1), evaluator.getUrlOverride(org.apache.ambari.server.state.quicklinksprofile.QuickLinkVisibilityControllerTest.HDFS, namenodeUi));
    }

    private static final org.apache.ambari.server.state.quicklinks.Link link(java.lang.String name, java.lang.String componentName, java.util.List<java.lang.String> attributes) {
        org.apache.ambari.server.state.quicklinks.Link link = new org.apache.ambari.server.state.quicklinks.Link();
        link.setName(name);
        link.setComponentName(componentName);
        link.setAttributes(attributes);
        return link;
    }
}