package org.apache.ambari.server.state.quicklinksprofile;
import javax.annotation.Nullable;
import static org.apache.ambari.server.state.quicklinksprofile.Filter.VISIBLE;
import static org.apache.ambari.server.state.quicklinksprofile.LinkAttributeFilter.LINK_ATTRIBUTE;
import static org.apache.ambari.server.state.quicklinksprofile.LinkNameFilter.LINK_NAME;
import static org.apache.ambari.server.state.quicklinksprofile.LinkNameFilter.LINK_URL;
public class QuickLinksProfileBuilder {
    public static final java.lang.String NAME = "name";

    public static final java.lang.String COMPONENTS = "components";

    public static final java.lang.String FILTERS = "filters";

    public static final java.util.Set<java.lang.String> ALLOWED_FILTER_ATTRIBUTES = com.google.common.collect.ImmutableSet.of(org.apache.ambari.server.state.quicklinksprofile.Filter.VISIBLE, org.apache.ambari.server.state.quicklinksprofile.LinkNameFilter.LINK_NAME, org.apache.ambari.server.state.quicklinksprofile.LinkNameFilter.LINK_URL, org.apache.ambari.server.state.quicklinksprofile.LinkAttributeFilter.LINK_ATTRIBUTE);

    public java.lang.String buildQuickLinksProfile(@javax.annotation.Nullable
    java.lang.Object globalFiltersRaw, @javax.annotation.Nullable
    java.lang.Object serviceFiltersRaw) throws org.apache.ambari.server.state.quicklinksprofile.QuickLinksProfileEvaluationException {
        try {
            java.util.List<org.apache.ambari.server.state.quicklinksprofile.Filter> globalFilters = buildQuickLinkFilters(globalFiltersRaw);
            java.util.List<org.apache.ambari.server.state.quicklinksprofile.Service> services = buildServices(serviceFiltersRaw);
            org.apache.ambari.server.state.quicklinksprofile.QuickLinksProfile profile = org.apache.ambari.server.state.quicklinksprofile.QuickLinksProfile.create(globalFilters, services);
            new org.apache.ambari.server.state.quicklinksprofile.DefaultQuickLinkVisibilityController(profile);
            return new org.apache.ambari.server.state.quicklinksprofile.QuickLinksProfileParser().encode(profile);
        } catch (org.apache.ambari.server.state.quicklinksprofile.QuickLinksProfileEvaluationException ex) {
            throw ex;
        } catch (java.lang.Exception ex) {
            throw new org.apache.ambari.server.state.quicklinksprofile.QuickLinksProfileEvaluationException("Error interpreting quicklinks profile data", ex);
        }
    }

    java.util.List<org.apache.ambari.server.state.quicklinksprofile.Service> buildServices(@javax.annotation.Nullable
    java.lang.Object servicesRaw) {
        if (null == servicesRaw) {
            return com.google.common.collect.ImmutableList.of();
        }
        java.util.List<org.apache.ambari.server.state.quicklinksprofile.Service> services = new java.util.ArrayList<>();
        for (java.util.Map<java.lang.String, java.lang.Object> serviceAsMap : ((java.util.Collection<java.util.Map<java.lang.String, java.lang.Object>>) (servicesRaw))) {
            java.lang.String serviceName = ((java.lang.String) (serviceAsMap.get(org.apache.ambari.server.state.quicklinksprofile.QuickLinksProfileBuilder.NAME)));
            java.lang.Object componentsRaw = serviceAsMap.get(org.apache.ambari.server.state.quicklinksprofile.QuickLinksProfileBuilder.COMPONENTS);
            java.lang.Object filtersRaw = serviceAsMap.get(org.apache.ambari.server.state.quicklinksprofile.QuickLinksProfileBuilder.FILTERS);
            services.add(org.apache.ambari.server.state.quicklinksprofile.Service.create(serviceName, buildQuickLinkFilters(filtersRaw), buildComponents(componentsRaw)));
        }
        return services;
    }

    java.util.List<org.apache.ambari.server.state.quicklinksprofile.Component> buildComponents(@javax.annotation.Nullable
    java.lang.Object componentsRaw) {
        if (null == componentsRaw) {
            return com.google.common.collect.ImmutableList.of();
        }
        java.util.List<org.apache.ambari.server.state.quicklinksprofile.Component> components = new java.util.ArrayList<>();
        for (java.util.Map<java.lang.String, java.lang.Object> componentAsMap : ((java.util.Collection<java.util.Map<java.lang.String, java.lang.Object>>) (componentsRaw))) {
            java.lang.String componentName = ((java.lang.String) (componentAsMap.get(org.apache.ambari.server.state.quicklinksprofile.QuickLinksProfileBuilder.NAME)));
            java.lang.Object filtersRaw = componentAsMap.get(org.apache.ambari.server.state.quicklinksprofile.QuickLinksProfileBuilder.FILTERS);
            components.add(org.apache.ambari.server.state.quicklinksprofile.Component.create(componentName, buildQuickLinkFilters(filtersRaw)));
        }
        return components;
    }

    java.util.List<org.apache.ambari.server.state.quicklinksprofile.Filter> buildQuickLinkFilters(@javax.annotation.Nullable
    java.lang.Object filtersRaw) throws java.lang.ClassCastException, java.lang.IllegalArgumentException {
        if (null == filtersRaw) {
            return com.google.common.collect.ImmutableList.of();
        }
        java.util.List<org.apache.ambari.server.state.quicklinksprofile.Filter> filters = new java.util.ArrayList<>();
        for (java.util.Map<java.lang.String, java.lang.String> filterAsMap : ((java.util.Collection<java.util.Map<java.lang.String, java.lang.String>>) (filtersRaw))) {
            java.util.Set<java.lang.String> invalidAttributes = com.google.common.collect.Sets.difference(filterAsMap.keySet(), org.apache.ambari.server.state.quicklinksprofile.QuickLinksProfileBuilder.ALLOWED_FILTER_ATTRIBUTES);
            com.google.common.base.Preconditions.checkArgument(invalidAttributes.isEmpty(), "%s%s", org.apache.ambari.server.state.quicklinksprofile.QuickLinksFilterDeserializer.PARSE_ERROR_MESSAGE_INVALID_JSON_TAG, invalidAttributes);
            java.lang.String linkName = filterAsMap.get(org.apache.ambari.server.state.quicklinksprofile.LinkNameFilter.LINK_NAME);
            java.lang.String linkUrl = filterAsMap.get(org.apache.ambari.server.state.quicklinksprofile.LinkNameFilter.LINK_URL);
            java.lang.String attributeName = filterAsMap.get(org.apache.ambari.server.state.quicklinksprofile.LinkAttributeFilter.LINK_ATTRIBUTE);
            boolean visible = java.lang.Boolean.parseBoolean(filterAsMap.get(org.apache.ambari.server.state.quicklinksprofile.Filter.VISIBLE));
            com.google.common.base.Preconditions.checkArgument((null == linkName) || (null == attributeName), "%s link_name: %s, link_attribute: %s", org.apache.ambari.server.state.quicklinksprofile.QuickLinksFilterDeserializer.PARSE_ERROR_MESSAGE_AMBIGUOUS_FILTER, linkName, attributeName);
            com.google.common.base.Preconditions.checkArgument((null == linkUrl) || (null != linkName), "Invalid filter. Link url can only be applied to link name filters. link_url: %s", linkUrl);
            if (null != linkName) {
                filters.add(org.apache.ambari.server.state.quicklinksprofile.Filter.linkNameFilter(linkName, linkUrl, visible));
            } else if (null != attributeName) {
                filters.add(org.apache.ambari.server.state.quicklinksprofile.Filter.linkAttributeFilter(attributeName, visible));
            } else {
                filters.add(org.apache.ambari.server.state.quicklinksprofile.Filter.acceptAllFilter(visible));
            }
        }
        return filters;
    }
}