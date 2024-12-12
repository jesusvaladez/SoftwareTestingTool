package org.apache.ambari.server.state.quicklinksprofile;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import static org.apache.ambari.server.state.quicklinksprofile.QuickLinksProfileBuilder.COMPONENTS;
import static org.apache.ambari.server.state.quicklinksprofile.QuickLinksProfileBuilder.FILTERS;
import static org.apache.ambari.server.state.quicklinksprofile.QuickLinksProfileBuilder.NAME;
public class QuickLinksProfileBuilderTest {
    @org.junit.Test
    public void testBuildProfileOnlyGlobalFilters() throws java.lang.Exception {
        java.util.Set<java.util.Map<java.lang.String, java.lang.String>> filters = com.google.common.collect.Sets.newHashSet(org.apache.ambari.server.state.quicklinksprofile.QuickLinksProfileBuilderTest.filter("namenode_ui", null, true), org.apache.ambari.server.state.quicklinksprofile.QuickLinksProfileBuilderTest.filter(null, "sso", true), org.apache.ambari.server.state.quicklinksprofile.QuickLinksProfileBuilderTest.filter(null, null, false));
        java.lang.String profileJson = new org.apache.ambari.server.state.quicklinksprofile.QuickLinksProfileBuilder().buildQuickLinksProfile(filters, null);
        org.apache.ambari.server.state.quicklinksprofile.QuickLinksProfile profile = new org.apache.ambari.server.state.quicklinksprofile.QuickLinksProfileParser().parse(profileJson.getBytes());
        org.apache.ambari.server.state.quicklinksprofile.QuickLinksProfileBuilderTest.assertFilterExists(profile, null, null, org.apache.ambari.server.state.quicklinksprofile.Filter.linkNameFilter("namenode_ui", true));
        org.apache.ambari.server.state.quicklinksprofile.QuickLinksProfileBuilderTest.assertFilterExists(profile, null, null, org.apache.ambari.server.state.quicklinksprofile.Filter.linkAttributeFilter("sso", true));
        org.apache.ambari.server.state.quicklinksprofile.QuickLinksProfileBuilderTest.assertFilterExists(profile, null, null, org.apache.ambari.server.state.quicklinksprofile.Filter.acceptAllFilter(false));
    }

    @org.junit.Test
    public void testBuildProfileOnlyServiceFilters() throws java.lang.Exception {
        java.util.Map<java.lang.String, java.lang.Object> nameNode = org.apache.ambari.server.state.quicklinksprofile.QuickLinksProfileBuilderTest.component("NAMENODE", com.google.common.collect.Sets.newHashSet(org.apache.ambari.server.state.quicklinksprofile.QuickLinksProfileBuilderTest.filter("namenode_ui", null, false)));
        java.util.Map<java.lang.String, java.lang.Object> hdfs = org.apache.ambari.server.state.quicklinksprofile.QuickLinksProfileBuilderTest.service("HDFS", com.google.common.collect.Sets.newHashSet(nameNode), com.google.common.collect.Sets.newHashSet(org.apache.ambari.server.state.quicklinksprofile.QuickLinksProfileBuilderTest.filter(null, "sso", true)));
        java.util.Set<java.util.Map<java.lang.String, java.lang.Object>> services = com.google.common.collect.Sets.newHashSet(hdfs);
        java.lang.String profileJson = new org.apache.ambari.server.state.quicklinksprofile.QuickLinksProfileBuilder().buildQuickLinksProfile(null, services);
        org.apache.ambari.server.state.quicklinksprofile.QuickLinksProfile profile = new org.apache.ambari.server.state.quicklinksprofile.QuickLinksProfileParser().parse(profileJson.getBytes());
        org.apache.ambari.server.state.quicklinksprofile.QuickLinksProfileBuilderTest.assertFilterExists(profile, "HDFS", "NAMENODE", org.apache.ambari.server.state.quicklinksprofile.Filter.linkNameFilter("namenode_ui", false));
        org.apache.ambari.server.state.quicklinksprofile.QuickLinksProfileBuilderTest.assertFilterExists(profile, "HDFS", null, org.apache.ambari.server.state.quicklinksprofile.Filter.linkAttributeFilter("sso", true));
    }

    @org.junit.Test
    public void testBuildProfileBothGlobalAndServiceFilters() throws java.lang.Exception {
        java.util.Set<java.util.Map<java.lang.String, java.lang.String>> globalFilters = com.google.common.collect.Sets.newHashSet(org.apache.ambari.server.state.quicklinksprofile.QuickLinksProfileBuilderTest.filter(null, null, false));
        java.util.Map<java.lang.String, java.lang.Object> nameNode = org.apache.ambari.server.state.quicklinksprofile.QuickLinksProfileBuilderTest.component("NAMENODE", com.google.common.collect.Sets.newHashSet(org.apache.ambari.server.state.quicklinksprofile.QuickLinksProfileBuilderTest.filter("namenode_ui", null, false), org.apache.ambari.server.state.quicklinksprofile.QuickLinksProfileBuilderTest.filter("namenode_logs", null, "http://customlink.org/namenode_logs", true)));
        java.util.Map<java.lang.String, java.lang.Object> hdfs = org.apache.ambari.server.state.quicklinksprofile.QuickLinksProfileBuilderTest.service("HDFS", com.google.common.collect.Sets.newHashSet(nameNode), com.google.common.collect.Sets.newHashSet(org.apache.ambari.server.state.quicklinksprofile.QuickLinksProfileBuilderTest.filter(null, "sso", true)));
        java.util.Set<java.util.Map<java.lang.String, java.lang.Object>> services = com.google.common.collect.Sets.newHashSet(hdfs);
        java.lang.String profileJson = new org.apache.ambari.server.state.quicklinksprofile.QuickLinksProfileBuilder().buildQuickLinksProfile(globalFilters, services);
        org.apache.ambari.server.state.quicklinksprofile.QuickLinksProfile profile = new org.apache.ambari.server.state.quicklinksprofile.QuickLinksProfileParser().parse(profileJson.getBytes());
        org.apache.ambari.server.state.quicklinksprofile.QuickLinksProfileBuilderTest.assertFilterExists(profile, null, null, org.apache.ambari.server.state.quicklinksprofile.Filter.acceptAllFilter(false));
        org.apache.ambari.server.state.quicklinksprofile.QuickLinksProfileBuilderTest.assertFilterExists(profile, "HDFS", "NAMENODE", org.apache.ambari.server.state.quicklinksprofile.Filter.linkNameFilter("namenode_ui", false));
        org.apache.ambari.server.state.quicklinksprofile.QuickLinksProfileBuilderTest.assertFilterExists(profile, "HDFS", "NAMENODE", org.apache.ambari.server.state.quicklinksprofile.Filter.linkNameFilter("namenode_ui", false));
        org.apache.ambari.server.state.quicklinksprofile.QuickLinksProfileBuilderTest.assertFilterExists(profile, "HDFS", "NAMENODE", org.apache.ambari.server.state.quicklinksprofile.Filter.linkNameFilter("namenode_logs", "http://customlink.org/namenode_logs", true));
        org.apache.ambari.server.state.quicklinksprofile.QuickLinksProfileBuilderTest.assertFilterExists(profile, "HDFS", null, org.apache.ambari.server.state.quicklinksprofile.Filter.linkAttributeFilter("sso", true));
    }

    @org.junit.Test(expected = org.apache.ambari.server.state.quicklinksprofile.QuickLinksProfileEvaluationException.class)
    public void testBuildProfileBadInputStructure() throws java.lang.Exception {
        new org.apache.ambari.server.state.quicklinksprofile.QuickLinksProfileBuilder().buildQuickLinksProfile("Hello", "World");
    }

    @org.junit.Test(expected = org.apache.ambari.server.state.quicklinksprofile.QuickLinksProfileEvaluationException.class)
    public void testBuildProfileMissingDataServiceName() throws java.lang.Exception {
        java.util.Map<java.lang.String, java.lang.Object> nameNode = org.apache.ambari.server.state.quicklinksprofile.QuickLinksProfileBuilderTest.component("NAMENODE", com.google.common.collect.Sets.newHashSet(org.apache.ambari.server.state.quicklinksprofile.QuickLinksProfileBuilderTest.filter("namenode_ui", null, false)));
        java.util.Map<java.lang.String, java.lang.Object> hdfs = org.apache.ambari.server.state.quicklinksprofile.QuickLinksProfileBuilderTest.service(null, com.google.common.collect.Sets.newHashSet(nameNode), com.google.common.collect.Sets.newHashSet(org.apache.ambari.server.state.quicklinksprofile.QuickLinksProfileBuilderTest.filter(null, "sso", true)));
        java.util.Set<java.util.Map<java.lang.String, java.lang.Object>> services = com.google.common.collect.Sets.newHashSet(hdfs);
        new org.apache.ambari.server.state.quicklinksprofile.QuickLinksProfileBuilder().buildQuickLinksProfile(null, services);
    }

    @org.junit.Test(expected = org.apache.ambari.server.state.quicklinksprofile.QuickLinksProfileEvaluationException.class)
    public void testBuildProfileMissingDataComponentName() throws java.lang.Exception {
        java.util.Map<java.lang.String, java.lang.Object> nameNode = org.apache.ambari.server.state.quicklinksprofile.QuickLinksProfileBuilderTest.component(null, com.google.common.collect.Sets.newHashSet(org.apache.ambari.server.state.quicklinksprofile.QuickLinksProfileBuilderTest.filter("namenode_ui", null, false)));
        java.util.Map<java.lang.String, java.lang.Object> hdfs = org.apache.ambari.server.state.quicklinksprofile.QuickLinksProfileBuilderTest.service("HDFS", com.google.common.collect.Sets.newHashSet(nameNode), com.google.common.collect.Sets.newHashSet(org.apache.ambari.server.state.quicklinksprofile.QuickLinksProfileBuilderTest.filter(null, "sso", true)));
        java.util.Set<java.util.Map<java.lang.String, java.lang.Object>> services = com.google.common.collect.Sets.newHashSet(hdfs);
        new org.apache.ambari.server.state.quicklinksprofile.QuickLinksProfileBuilder().buildQuickLinksProfile(null, services);
    }

    @org.junit.Test(expected = org.apache.ambari.server.state.quicklinksprofile.QuickLinksProfileEvaluationException.class)
    public void testBuildProfileInvalidProfileDefiniton_contradictingFilters() throws java.lang.Exception {
        java.util.Set<java.util.Map<java.lang.String, java.lang.String>> filters = com.google.common.collect.Sets.newHashSet(org.apache.ambari.server.state.quicklinksprofile.QuickLinksProfileBuilderTest.filter(null, "sso", true), org.apache.ambari.server.state.quicklinksprofile.QuickLinksProfileBuilderTest.filter(null, "sso", false));
        new org.apache.ambari.server.state.quicklinksprofile.QuickLinksProfileBuilder().buildQuickLinksProfile(filters, null);
    }

    @org.junit.Test(expected = org.apache.ambari.server.state.quicklinksprofile.QuickLinksProfileEvaluationException.class)
    public void testBuildProfileInvalidProfileDefiniton_invalidAttribute() throws java.lang.Exception {
        java.util.Map<java.lang.String, java.lang.String> badFilter = com.google.common.collect.ImmutableMap.of("visible", "true", "linkkk_atirbuteee", "sso");
        java.util.Set<java.util.Map<java.lang.String, java.lang.String>> filters = com.google.common.collect.Sets.newHashSet(badFilter);
        new org.apache.ambari.server.state.quicklinksprofile.QuickLinksProfileBuilder().buildQuickLinksProfile(filters, null);
    }

    private static void assertFilterExists(@javax.annotation.Nonnull
    org.apache.ambari.server.state.quicklinksprofile.QuickLinksProfile profile, @javax.annotation.Nullable
    java.lang.String serviceName, @javax.annotation.Nullable
    java.lang.String componentName, @javax.annotation.Nonnull
    org.apache.ambari.server.state.quicklinksprofile.Filter filter) {
        if (null == serviceName) {
            if (!profile.getFilters().contains(filter)) {
                throw new java.lang.AssertionError("Expected global filter not found: " + filter);
            }
        } else {
            org.apache.ambari.server.state.quicklinksprofile.Service service = org.apache.ambari.server.state.quicklinksprofile.QuickLinksProfileBuilderTest.findService(profile.getServices(), serviceName);
            if (null == componentName) {
                if (!service.getFilters().contains(filter)) {
                    throw new java.lang.AssertionError(java.lang.String.format("Expected filter not found. Service: %s, Filter: %s", serviceName, filter));
                }
            } else {
                org.apache.ambari.server.state.quicklinksprofile.Component component = org.apache.ambari.server.state.quicklinksprofile.QuickLinksProfileBuilderTest.findComponent(service.getComponents(), componentName);
                if (!component.getFilters().contains(filter)) {
                    throw new java.lang.AssertionError(java.lang.String.format("Expected filter not found. Service: %s, Component: %s, Filter: %s", serviceName, componentName, filter));
                }
            }
        }
    }

    private static org.apache.ambari.server.state.quicklinksprofile.Component findComponent(java.util.List<org.apache.ambari.server.state.quicklinksprofile.Component> components, java.lang.String componentName) {
        for (org.apache.ambari.server.state.quicklinksprofile.Component component : components) {
            if (component.getName().equals(componentName)) {
                return component;
            }
        }
        throw new java.lang.AssertionError("Expected component not found: " + componentName);
    }

    private static org.apache.ambari.server.state.quicklinksprofile.Service findService(java.util.List<org.apache.ambari.server.state.quicklinksprofile.Service> services, java.lang.String serviceName) {
        for (org.apache.ambari.server.state.quicklinksprofile.Service service : services) {
            if (service.getName().equals(serviceName)) {
                return service;
            }
        }
        throw new java.lang.AssertionError("Expected service not found: " + serviceName);
    }

    public static java.util.Map<java.lang.String, java.lang.String> filter(@javax.annotation.Nullable
    java.lang.String linkName, @javax.annotation.Nullable
    java.lang.String attributeName, boolean visible) {
        return org.apache.ambari.server.state.quicklinksprofile.QuickLinksProfileBuilderTest.filter(linkName, attributeName, null, visible);
    }

    public static java.util.Map<java.lang.String, java.lang.String> filter(@javax.annotation.Nullable
    java.lang.String linkName, @javax.annotation.Nullable
    java.lang.String attributeName, @javax.annotation.Nullable
    java.lang.String linkUrl, boolean visible) {
        java.util.Map<java.lang.String, java.lang.String> map = new java.util.HashMap<>(4);
        if (null != linkName) {
            map.put(org.apache.ambari.server.state.quicklinksprofile.LinkNameFilter.LINK_NAME, linkName);
        }
        if (null != linkUrl) {
            map.put(org.apache.ambari.server.state.quicklinksprofile.LinkNameFilter.LINK_URL, linkUrl);
        }
        if (null != attributeName) {
            map.put(org.apache.ambari.server.state.quicklinksprofile.LinkAttributeFilter.LINK_ATTRIBUTE, attributeName);
        }
        map.put(org.apache.ambari.server.state.quicklinksprofile.Filter.VISIBLE, java.lang.Boolean.toString(visible));
        return map;
    }

    public static java.util.Map<java.lang.String, java.lang.Object> component(java.lang.String componentName, java.util.Set<java.util.Map<java.lang.String, java.lang.String>> filters) {
        java.util.Map<java.lang.String, java.lang.Object> map = new java.util.HashMap<>();
        map.put(org.apache.ambari.server.state.quicklinksprofile.QuickLinksProfileBuilder.NAME, componentName);
        map.put(org.apache.ambari.server.state.quicklinksprofile.QuickLinksProfileBuilder.FILTERS, filters);
        return map;
    }

    public static java.util.Map<java.lang.String, java.lang.Object> service(java.lang.String serviceName, java.util.Set<java.util.Map<java.lang.String, java.lang.Object>> components, java.util.Set<java.util.Map<java.lang.String, java.lang.String>> filters) {
        java.util.Map<java.lang.String, java.lang.Object> map = new java.util.HashMap<>();
        map.put(org.apache.ambari.server.state.quicklinksprofile.QuickLinksProfileBuilder.NAME, serviceName);
        if (null != components) {
            map.put(org.apache.ambari.server.state.quicklinksprofile.QuickLinksProfileBuilder.COMPONENTS, components);
        }
        if (null != filters) {
            map.put(org.apache.ambari.server.state.quicklinksprofile.QuickLinksProfileBuilder.FILTERS, filters);
        }
        return map;
    }
}