package org.apache.ambari.server.state.quicklinksprofile;
public class FilterEvaluatorTest {
    static final java.lang.String NAMENODE = "NAMENODE";

    static final java.lang.String NAMENODE_UI = "namenode_ui";

    static final java.lang.String AUTHENTICATED = "authenticated";

    static final java.lang.String NAMENODE_JMX = "namenode_jmx";

    static final java.lang.String SSO = "sso";

    private org.apache.ambari.server.state.quicklinks.Link namenodeUi;

    private org.apache.ambari.server.state.quicklinks.Link nameNodeJmx;

    public FilterEvaluatorTest() {
        namenodeUi = new org.apache.ambari.server.state.quicklinks.Link();
        namenodeUi.setComponentName(org.apache.ambari.server.state.quicklinksprofile.FilterEvaluatorTest.NAMENODE);
        namenodeUi.setName(org.apache.ambari.server.state.quicklinksprofile.FilterEvaluatorTest.NAMENODE_UI);
        namenodeUi.setAttributes(com.google.common.collect.ImmutableList.of(org.apache.ambari.server.state.quicklinksprofile.FilterEvaluatorTest.AUTHENTICATED));
        nameNodeJmx = new org.apache.ambari.server.state.quicklinks.Link();
        nameNodeJmx.setComponentName(org.apache.ambari.server.state.quicklinksprofile.FilterEvaluatorTest.NAMENODE);
        nameNodeJmx.setName(org.apache.ambari.server.state.quicklinksprofile.FilterEvaluatorTest.NAMENODE_JMX);
    }

    @org.junit.Test
    public void testWithEmptyFilters() throws java.lang.Exception {
        org.apache.ambari.server.state.quicklinksprofile.FilterEvaluator evaluator = new org.apache.ambari.server.state.quicklinksprofile.FilterEvaluator(new java.util.ArrayList<>());
        org.junit.Assert.assertEquals(java.util.Optional.empty(), evaluator.isVisible(namenodeUi));
        org.apache.ambari.server.state.quicklinksprofile.FilterEvaluator evaluator2 = new org.apache.ambari.server.state.quicklinksprofile.FilterEvaluator(null);
        org.junit.Assert.assertEquals(java.util.Optional.empty(), evaluator2.isVisible(namenodeUi));
    }

    @org.junit.Test
    public void testNoMatchingFilter() throws java.lang.Exception {
        java.util.List<org.apache.ambari.server.state.quicklinksprofile.Filter> filters = com.google.common.collect.Lists.newArrayList(org.apache.ambari.server.state.quicklinksprofile.Filter.linkNameFilter(org.apache.ambari.server.state.quicklinksprofile.FilterEvaluatorTest.NAMENODE_JMX, true), org.apache.ambari.server.state.quicklinksprofile.Filter.linkAttributeFilter(org.apache.ambari.server.state.quicklinksprofile.FilterEvaluatorTest.SSO, false));
        org.apache.ambari.server.state.quicklinksprofile.FilterEvaluator evaluator = new org.apache.ambari.server.state.quicklinksprofile.FilterEvaluator(filters);
        org.junit.Assert.assertEquals(java.util.Optional.empty(), evaluator.isVisible(namenodeUi));
    }

    @org.junit.Test
    public void testLinkNameFiltersEvaluatedFirst() throws java.lang.Exception {
        java.util.List<org.apache.ambari.server.state.quicklinksprofile.Filter> filters = com.google.common.collect.Lists.newArrayList(org.apache.ambari.server.state.quicklinksprofile.Filter.acceptAllFilter(false), org.apache.ambari.server.state.quicklinksprofile.Filter.linkNameFilter(org.apache.ambari.server.state.quicklinksprofile.FilterEvaluatorTest.NAMENODE_UI, true), org.apache.ambari.server.state.quicklinksprofile.Filter.linkNameFilter(org.apache.ambari.server.state.quicklinksprofile.FilterEvaluatorTest.NAMENODE_JMX, false), org.apache.ambari.server.state.quicklinksprofile.Filter.linkAttributeFilter(org.apache.ambari.server.state.quicklinksprofile.FilterEvaluatorTest.AUTHENTICATED, false), org.apache.ambari.server.state.quicklinksprofile.Filter.linkAttributeFilter(org.apache.ambari.server.state.quicklinksprofile.FilterEvaluatorTest.SSO, false));
        org.apache.ambari.server.state.quicklinksprofile.FilterEvaluator evaluator = new org.apache.ambari.server.state.quicklinksprofile.FilterEvaluator(filters);
        org.junit.Assert.assertEquals(java.util.Optional.of(true), evaluator.isVisible(namenodeUi));
    }

    @org.junit.Test
    public void testLinkAttributeFiltersEvaluatedSecondly() throws java.lang.Exception {
        java.util.List<org.apache.ambari.server.state.quicklinksprofile.Filter> filters = com.google.common.collect.Lists.newArrayList(org.apache.ambari.server.state.quicklinksprofile.Filter.acceptAllFilter(false), org.apache.ambari.server.state.quicklinksprofile.Filter.linkNameFilter(org.apache.ambari.server.state.quicklinksprofile.FilterEvaluatorTest.NAMENODE_JMX, false), org.apache.ambari.server.state.quicklinksprofile.Filter.linkAttributeFilter(org.apache.ambari.server.state.quicklinksprofile.FilterEvaluatorTest.AUTHENTICATED, true), org.apache.ambari.server.state.quicklinksprofile.Filter.linkAttributeFilter(org.apache.ambari.server.state.quicklinksprofile.FilterEvaluatorTest.SSO, true));
        org.apache.ambari.server.state.quicklinksprofile.FilterEvaluator evaluator = new org.apache.ambari.server.state.quicklinksprofile.FilterEvaluator(filters);
        org.junit.Assert.assertEquals(java.util.Optional.of(true), evaluator.isVisible(namenodeUi));
    }

    @org.junit.Test
    public void testLinkAttributeFiltersWorkWithNullAttributes() throws java.lang.Exception {
        java.util.List<org.apache.ambari.server.state.quicklinksprofile.Filter> filters = com.google.common.collect.Lists.newArrayList(org.apache.ambari.server.state.quicklinksprofile.Filter.acceptAllFilter(true), org.apache.ambari.server.state.quicklinksprofile.Filter.linkAttributeFilter(org.apache.ambari.server.state.quicklinksprofile.FilterEvaluatorTest.AUTHENTICATED, false), org.apache.ambari.server.state.quicklinksprofile.Filter.linkAttributeFilter(org.apache.ambari.server.state.quicklinksprofile.FilterEvaluatorTest.SSO, false));
        org.apache.ambari.server.state.quicklinksprofile.FilterEvaluator evaluator = new org.apache.ambari.server.state.quicklinksprofile.FilterEvaluator(filters);
        org.junit.Assert.assertEquals(java.util.Optional.of(true), evaluator.isVisible(nameNodeJmx));
    }

    @org.junit.Test
    public void testHideFilterTakesPrecedence() throws java.lang.Exception {
        java.util.List<org.apache.ambari.server.state.quicklinksprofile.Filter> filters = com.google.common.collect.Lists.newArrayList(org.apache.ambari.server.state.quicklinksprofile.Filter.linkAttributeFilter(org.apache.ambari.server.state.quicklinksprofile.FilterEvaluatorTest.AUTHENTICATED, false), org.apache.ambari.server.state.quicklinksprofile.Filter.linkAttributeFilter(org.apache.ambari.server.state.quicklinksprofile.FilterEvaluatorTest.SSO, true));
        org.apache.ambari.server.state.quicklinksprofile.FilterEvaluator evaluator = new org.apache.ambari.server.state.quicklinksprofile.FilterEvaluator(filters);
        namenodeUi.setAttributes(com.google.common.collect.ImmutableList.of(org.apache.ambari.server.state.quicklinksprofile.FilterEvaluatorTest.AUTHENTICATED, org.apache.ambari.server.state.quicklinksprofile.FilterEvaluatorTest.SSO));
        org.junit.Assert.assertEquals(java.util.Optional.of(false), evaluator.isVisible(namenodeUi));
    }

    @org.junit.Test
    public void acceptAllFilterEvaluatedLast() throws java.lang.Exception {
        java.util.List<org.apache.ambari.server.state.quicklinksprofile.Filter> filters = com.google.common.collect.Lists.newArrayList(org.apache.ambari.server.state.quicklinksprofile.Filter.acceptAllFilter(false), org.apache.ambari.server.state.quicklinksprofile.Filter.linkNameFilter(org.apache.ambari.server.state.quicklinksprofile.FilterEvaluatorTest.NAMENODE_JMX, true), org.apache.ambari.server.state.quicklinksprofile.Filter.linkAttributeFilter(org.apache.ambari.server.state.quicklinksprofile.FilterEvaluatorTest.SSO, true));
        org.apache.ambari.server.state.quicklinksprofile.FilterEvaluator evaluator = new org.apache.ambari.server.state.quicklinksprofile.FilterEvaluator(filters);
        org.junit.Assert.assertEquals(java.util.Optional.of(false), evaluator.isVisible(namenodeUi));
    }

    @org.junit.Test(expected = org.apache.ambari.server.state.quicklinksprofile.QuickLinksProfileEvaluationException.class)
    public void contradictingLinkNameFiltersRejected() throws java.lang.Exception {
        java.util.List<org.apache.ambari.server.state.quicklinksprofile.Filter> filters = com.google.common.collect.Lists.newArrayList(org.apache.ambari.server.state.quicklinksprofile.Filter.linkNameFilter(org.apache.ambari.server.state.quicklinksprofile.FilterEvaluatorTest.NAMENODE_JMX, true), org.apache.ambari.server.state.quicklinksprofile.Filter.linkNameFilter(org.apache.ambari.server.state.quicklinksprofile.FilterEvaluatorTest.NAMENODE_JMX, false), org.apache.ambari.server.state.quicklinksprofile.Filter.linkAttributeFilter(org.apache.ambari.server.state.quicklinksprofile.FilterEvaluatorTest.SSO, true));
        new org.apache.ambari.server.state.quicklinksprofile.FilterEvaluator(filters);
    }

    @org.junit.Test(expected = org.apache.ambari.server.state.quicklinksprofile.QuickLinksProfileEvaluationException.class)
    public void contradictingPropertyFiltersRejected() throws java.lang.Exception {
        java.util.List<org.apache.ambari.server.state.quicklinksprofile.Filter> filters = com.google.common.collect.Lists.newArrayList(org.apache.ambari.server.state.quicklinksprofile.Filter.linkAttributeFilter(org.apache.ambari.server.state.quicklinksprofile.FilterEvaluatorTest.SSO, true), org.apache.ambari.server.state.quicklinksprofile.Filter.linkAttributeFilter(org.apache.ambari.server.state.quicklinksprofile.FilterEvaluatorTest.SSO, false));
        new org.apache.ambari.server.state.quicklinksprofile.FilterEvaluator(filters);
    }

    @org.junit.Test(expected = org.apache.ambari.server.state.quicklinksprofile.QuickLinksProfileEvaluationException.class)
    public void contradictingLinkAttributeFiltersRejected() throws java.lang.Exception {
        java.util.List<org.apache.ambari.server.state.quicklinksprofile.Filter> filters = com.google.common.collect.Lists.newArrayList(org.apache.ambari.server.state.quicklinksprofile.Filter.linkAttributeFilter(org.apache.ambari.server.state.quicklinksprofile.FilterEvaluatorTest.SSO, true), org.apache.ambari.server.state.quicklinksprofile.Filter.linkAttributeFilter(org.apache.ambari.server.state.quicklinksprofile.FilterEvaluatorTest.SSO, false));
        new org.apache.ambari.server.state.quicklinksprofile.FilterEvaluator(filters);
    }

    @org.junit.Test(expected = org.apache.ambari.server.state.quicklinksprofile.QuickLinksProfileEvaluationException.class)
    public void contradictingAcceptAllFiltersRejected() throws java.lang.Exception {
        java.util.List<org.apache.ambari.server.state.quicklinksprofile.Filter> filters = com.google.common.collect.Lists.newArrayList(org.apache.ambari.server.state.quicklinksprofile.Filter.linkNameFilter(org.apache.ambari.server.state.quicklinksprofile.FilterEvaluatorTest.NAMENODE_JMX, true), org.apache.ambari.server.state.quicklinksprofile.Filter.linkAttributeFilter(org.apache.ambari.server.state.quicklinksprofile.FilterEvaluatorTest.SSO, true), org.apache.ambari.server.state.quicklinksprofile.Filter.acceptAllFilter(true), org.apache.ambari.server.state.quicklinksprofile.Filter.acceptAllFilter(false));
        new org.apache.ambari.server.state.quicklinksprofile.FilterEvaluator(filters);
    }

    @org.junit.Test
    public void duplicateFiltersAreOkIfDoNotContradict() throws java.lang.Exception {
        java.util.List<org.apache.ambari.server.state.quicklinksprofile.Filter> filters = com.google.common.collect.Lists.newArrayList(org.apache.ambari.server.state.quicklinksprofile.Filter.acceptAllFilter(true), org.apache.ambari.server.state.quicklinksprofile.Filter.acceptAllFilter(true), org.apache.ambari.server.state.quicklinksprofile.Filter.linkNameFilter(org.apache.ambari.server.state.quicklinksprofile.FilterEvaluatorTest.NAMENODE_JMX, false), org.apache.ambari.server.state.quicklinksprofile.Filter.linkNameFilter(org.apache.ambari.server.state.quicklinksprofile.FilterEvaluatorTest.NAMENODE_JMX, false), org.apache.ambari.server.state.quicklinksprofile.Filter.linkAttributeFilter(org.apache.ambari.server.state.quicklinksprofile.FilterEvaluatorTest.SSO, false), org.apache.ambari.server.state.quicklinksprofile.Filter.linkAttributeFilter(org.apache.ambari.server.state.quicklinksprofile.FilterEvaluatorTest.SSO, false));
        org.apache.ambari.server.state.quicklinksprofile.FilterEvaluator evaluator = new org.apache.ambari.server.state.quicklinksprofile.FilterEvaluator(filters);
        org.junit.Assert.assertEquals(java.util.Optional.of(true), evaluator.isVisible(namenodeUi));
    }
}