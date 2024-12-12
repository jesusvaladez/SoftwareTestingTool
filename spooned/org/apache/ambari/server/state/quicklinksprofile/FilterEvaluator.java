package org.apache.ambari.server.state.quicklinksprofile;
class FilterEvaluator {
    private final java.util.Map<java.lang.String, java.lang.Boolean> linkNameFilters = new java.util.HashMap<>();

    private final java.util.Set<java.lang.String> showAttributes = new java.util.HashSet<>();

    private final java.util.Set<java.lang.String> hideAttributes = new java.util.HashSet<>();

    private java.util.Optional<java.lang.Boolean> acceptAllFilter = java.util.Optional.empty();

    FilterEvaluator(java.util.List<org.apache.ambari.server.state.quicklinksprofile.Filter> filters) throws org.apache.ambari.server.state.quicklinksprofile.QuickLinksProfileEvaluationException {
        for (org.apache.ambari.server.state.quicklinksprofile.Filter filter : org.apache.ambari.server.state.quicklinksprofile.DefaultQuickLinkVisibilityController.nullToEmptyList(filters)) {
            if (filter instanceof org.apache.ambari.server.state.quicklinksprofile.LinkNameFilter) {
                java.lang.String linkName = ((org.apache.ambari.server.state.quicklinksprofile.LinkNameFilter) (filter)).getLinkName();
                if (linkNameFilters.containsKey(linkName) && (linkNameFilters.get(linkName) != filter.isVisible())) {
                    throw new org.apache.ambari.server.state.quicklinksprofile.QuickLinksProfileEvaluationException(("Contradicting filters for link name [" + linkName) + "]");
                }
                linkNameFilters.put(linkName, filter.isVisible());
            } else if (filter instanceof org.apache.ambari.server.state.quicklinksprofile.LinkAttributeFilter) {
                java.lang.String linkAttribute = ((org.apache.ambari.server.state.quicklinksprofile.LinkAttributeFilter) (filter)).getLinkAttribute();
                if (filter.isVisible()) {
                    showAttributes.add(linkAttribute);
                } else {
                    hideAttributes.add(linkAttribute);
                }
                if (showAttributes.contains(linkAttribute) && hideAttributes.contains(linkAttribute)) {
                    throw new org.apache.ambari.server.state.quicklinksprofile.QuickLinksProfileEvaluationException(("Contradicting filters for link attribute [" + linkAttribute) + "]");
                }
            } else {
                if (acceptAllFilter.isPresent() && (!acceptAllFilter.get().equals(filter.isVisible()))) {
                    throw new org.apache.ambari.server.state.quicklinksprofile.QuickLinksProfileEvaluationException("Contradicting accept-all filters.");
                }
                acceptAllFilter = java.util.Optional.of(filter.isVisible());
            }
        }
    }

    java.util.Optional<java.lang.Boolean> isVisible(org.apache.ambari.server.state.quicklinks.Link quickLink) {
        if (linkNameFilters.containsKey(quickLink.getName())) {
            return java.util.Optional.of(linkNameFilters.get(quickLink.getName()));
        }
        for (java.lang.String attribute : org.apache.ambari.server.state.quicklinksprofile.DefaultQuickLinkVisibilityController.nullToEmptyList(quickLink.getAttributes())) {
            if (hideAttributes.contains(attribute))
                return java.util.Optional.of(false);

        }
        for (java.lang.String attribute : org.apache.ambari.server.state.quicklinksprofile.DefaultQuickLinkVisibilityController.nullToEmptyList(quickLink.getAttributes())) {
            if (showAttributes.contains(attribute))
                return java.util.Optional.of(true);

        }
        return acceptAllFilter;
    }
}