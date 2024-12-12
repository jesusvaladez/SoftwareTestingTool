package org.apache.ambari.server.state.quicklinksprofile;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
public class DefaultQuickLinkVisibilityController implements org.apache.ambari.server.state.quicklinksprofile.QuickLinkVisibilityController {
    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.state.quicklinksprofile.DefaultQuickLinkVisibilityController.class);

    private final org.apache.ambari.server.state.quicklinksprofile.FilterEvaluator globalRules;

    private final java.util.Map<java.lang.String, org.apache.ambari.server.state.quicklinksprofile.FilterEvaluator> serviceRules = new java.util.HashMap<>();

    private final java.util.Map<org.apache.commons.lang3.tuple.Pair<java.lang.String, java.lang.String>, org.apache.ambari.server.state.quicklinksprofile.FilterEvaluator> componentRules = new java.util.HashMap<>();

    private final java.util.Map<org.apache.commons.lang3.tuple.Pair<java.lang.String, java.lang.String>, java.lang.String> urlOverrides = new java.util.HashMap<>();

    public DefaultQuickLinkVisibilityController(org.apache.ambari.server.state.quicklinksprofile.QuickLinksProfile profile) throws org.apache.ambari.server.state.quicklinksprofile.QuickLinksProfileEvaluationException {
        int filterCount = size(profile.getFilters());
        globalRules = new org.apache.ambari.server.state.quicklinksprofile.FilterEvaluator(profile.getFilters());
        for (org.apache.ambari.server.state.quicklinksprofile.Service service : profile.getServices()) {
            filterCount += size(service.getFilters());
            serviceRules.put(service.getName(), new org.apache.ambari.server.state.quicklinksprofile.FilterEvaluator(service.getFilters()));
            for (org.apache.ambari.server.state.quicklinksprofile.Component component : service.getComponents()) {
                filterCount += size(component.getFilters());
                componentRules.put(org.apache.commons.lang3.tuple.Pair.of(service.getName(), component.getName()), new org.apache.ambari.server.state.quicklinksprofile.FilterEvaluator(component.getFilters()));
            }
        }
        if (filterCount == 0) {
            throw new org.apache.ambari.server.state.quicklinksprofile.QuickLinksProfileEvaluationException("At least one filter must be defined.");
        }
        java.lang.String globalOverrides = org.apache.ambari.server.state.quicklinksprofile.LinkNameFilter.getLinkNameFilters(profile.getFilters().stream()).filter(f -> f.getLinkUrl() != null).map(f -> (f.getLinkName() + " -> ") + f.getLinkUrl()).collect(java.util.stream.Collectors.joining(", "));
        if (!globalOverrides.isEmpty()) {
            org.apache.ambari.server.state.quicklinksprofile.DefaultQuickLinkVisibilityController.LOG.warn("Link url overrides only work on service and component levels. The following global overrides will be " + "ignored: {}", globalOverrides);
        }
        for (org.apache.ambari.server.state.quicklinksprofile.Service service : profile.getServices()) {
            urlOverrides.putAll(getUrlOverrides(service.getName(), service.getFilters()));
            for (org.apache.ambari.server.state.quicklinksprofile.Component component : service.getComponents()) {
                java.util.Map<org.apache.commons.lang3.tuple.Pair<java.lang.String, java.lang.String>, java.lang.String> componentUrlOverrides = getUrlOverrides(service.getName(), component.getFilters());
                java.util.Set<org.apache.commons.lang3.tuple.Pair<java.lang.String, java.lang.String>> duplicateOverrides = com.google.common.collect.Sets.intersection(urlOverrides.keySet(), componentUrlOverrides.keySet());
                if (!duplicateOverrides.isEmpty()) {
                    org.apache.ambari.server.state.quicklinksprofile.DefaultQuickLinkVisibilityController.LOG.warn("Duplicate url overrides in quick links profile: {}", duplicateOverrides);
                }
                urlOverrides.putAll(componentUrlOverrides);
            }
        }
    }

    private java.util.Map<org.apache.commons.lang3.tuple.Pair<java.lang.String, java.lang.String>, java.lang.String> getUrlOverrides(java.lang.String serviceName, java.util.Collection<org.apache.ambari.server.state.quicklinksprofile.Filter> filters) {
        return filters.stream().filter(f -> (f instanceof org.apache.ambari.server.state.quicklinksprofile.LinkNameFilter) && (null != ((org.apache.ambari.server.state.quicklinksprofile.LinkNameFilter) (f)).getLinkUrl())).map(f -> {
            org.apache.ambari.server.state.quicklinksprofile.LinkNameFilter lnf = ((org.apache.ambari.server.state.quicklinksprofile.LinkNameFilter) (f));
            return org.apache.commons.lang3.tuple.Pair.of(org.apache.commons.lang3.tuple.Pair.of(serviceName, lnf.getLinkName()), lnf.getLinkUrl());
        }).collect(java.util.stream.Collectors.toMap(org.apache.commons.lang3.tuple.Pair::getKey, org.apache.commons.lang3.tuple.Pair::getValue));
    }

    @java.lang.Override
    public java.util.Optional<java.lang.String> getUrlOverride(@javax.annotation.Nonnull
    java.lang.String service, @javax.annotation.Nonnull
    org.apache.ambari.server.state.quicklinks.Link quickLink) {
        return java.util.Optional.ofNullable(urlOverrides.get(org.apache.commons.lang3.tuple.Pair.of(service, quickLink.getName())));
    }

    @java.lang.Override
    public boolean isVisible(@javax.annotation.Nonnull
    java.lang.String service, @javax.annotation.Nonnull
    org.apache.ambari.server.state.quicklinks.Link quickLink) {
        java.util.Optional<java.lang.Boolean> componentResult = evaluateComponentRules(service, quickLink);
        if (componentResult.isPresent()) {
            return componentResult.get();
        }
        java.util.Optional<java.lang.Boolean> serviceResult = evaluateServiceRules(service, quickLink);
        if (serviceResult.isPresent()) {
            return serviceResult.get();
        }
        return globalRules.isVisible(quickLink).orElse(false);
    }

    private int size(@javax.annotation.Nullable
    java.util.Collection<?> collection) {
        return null == collection ? 0 : collection.size();
    }

    private java.util.Optional<java.lang.Boolean> evaluateComponentRules(@javax.annotation.Nonnull
    java.lang.String service, @javax.annotation.Nonnull
    org.apache.ambari.server.state.quicklinks.Link quickLink) {
        if (null == quickLink.getComponentName()) {
            return java.util.Optional.empty();
        } else {
            org.apache.ambari.server.state.quicklinksprofile.FilterEvaluator componentEvaluator = componentRules.get(org.apache.commons.lang3.tuple.Pair.of(service, quickLink.getComponentName()));
            return componentEvaluator != null ? componentEvaluator.isVisible(quickLink) : java.util.Optional.empty();
        }
    }

    private java.util.Optional<java.lang.Boolean> evaluateServiceRules(@javax.annotation.Nonnull
    java.lang.String service, @javax.annotation.Nonnull
    org.apache.ambari.server.state.quicklinks.Link quickLink) {
        return serviceRules.containsKey(service) ? serviceRules.get(service).isVisible(quickLink) : java.util.Optional.empty();
    }

    static <T> java.util.List<T> nullToEmptyList(@javax.annotation.Nullable
    java.util.List<T> items) {
        return items != null ? items : java.util.Collections.emptyList();
    }
}