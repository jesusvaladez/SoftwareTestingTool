package org.apache.ambari.server.controller.internal;
public abstract class BaseProvider {
    private final java.util.Set<java.lang.String> propertyIds;

    private final java.util.Set<java.lang.String> categoryIds;

    private final java.util.Set<java.lang.String> combinedIds;

    private final java.util.Map<java.lang.String, java.util.regex.Pattern> patterns;

    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.controller.internal.BaseProvider.class);

    private static final java.util.regex.Pattern METRIC_ARGUMENT_METHOD_REPLACEMENT = java.util.regex.Pattern.compile("\\$\\d+(\\.\\S+\\(\\S+\\))*");

    public BaseProvider(java.util.Set<java.lang.String> propertyIds) {
        this.propertyIds = new java.util.HashSet<>(propertyIds);
        categoryIds = org.apache.ambari.server.controller.utilities.PropertyHelper.getCategories(propertyIds);
        combinedIds = new java.util.HashSet<>(propertyIds);
        combinedIds.addAll(categoryIds);
        patterns = new java.util.HashMap<>();
        for (java.lang.String id : combinedIds) {
            if (containsArguments(id)) {
                java.lang.String pattern = org.apache.ambari.server.controller.internal.BaseProvider.METRIC_ARGUMENT_METHOD_REPLACEMENT.matcher(id).replaceAll("(\\\\S*)");
                patterns.put(id, java.util.regex.Pattern.compile(pattern));
            }
        }
    }

    protected java.util.Set<java.lang.String> checkConfigPropertyIds(java.util.Set<java.lang.String> base, java.lang.String configCategory) {
        if (0 == base.size()) {
            return base;
        }
        java.util.Set<java.lang.String> unsupported = new java.util.HashSet<>();
        for (java.lang.String propertyId : base) {
            if (!propertyId.startsWith(configCategory + "/desired_config")) {
                unsupported.add(propertyId);
            }
        }
        return unsupported;
    }

    public java.util.Set<java.lang.String> checkPropertyIds(java.util.Set<java.lang.String> propertyIds) {
        if (!this.propertyIds.containsAll(propertyIds)) {
            java.util.Set<java.lang.String> unsupportedPropertyIds = new java.util.HashSet<>(propertyIds);
            unsupportedPropertyIds.removeAll(combinedIds);
            java.util.Set<java.lang.String> categoryProperties = new java.util.HashSet<>();
            for (java.lang.String unsupportedPropertyId : unsupportedPropertyIds) {
                if (checkCategory(unsupportedPropertyId) || checkRegExp(unsupportedPropertyId)) {
                    categoryProperties.add(unsupportedPropertyId);
                }
            }
            unsupportedPropertyIds.removeAll(categoryProperties);
            return unsupportedPropertyIds;
        }
        return java.util.Collections.emptySet();
    }

    protected java.util.Set<java.lang.String> getRequestPropertyIds(org.apache.ambari.server.controller.spi.Request request, org.apache.ambari.server.controller.spi.Predicate predicate) {
        java.util.Set<java.lang.String> propertyIds = request.getPropertyIds();
        if ((propertyIds == null) || propertyIds.isEmpty()) {
            return new java.util.HashSet<>(this.propertyIds);
        }
        propertyIds = new java.util.HashSet<>(propertyIds);
        if (predicate != null) {
            propertyIds.addAll(org.apache.ambari.server.controller.utilities.PredicateHelper.getPropertyIds(predicate));
        }
        if (!combinedIds.containsAll(propertyIds)) {
            java.util.Set<java.lang.String> keepers = new java.util.HashSet<>();
            java.util.Set<java.lang.String> unsupportedPropertyIds = new java.util.HashSet<>(propertyIds);
            unsupportedPropertyIds.removeAll(combinedIds);
            for (java.lang.String unsupportedPropertyId : unsupportedPropertyIds) {
                if (checkCategory(unsupportedPropertyId) || checkRegExp(unsupportedPropertyId)) {
                    keepers.add(unsupportedPropertyId);
                }
            }
            propertyIds.retainAll(combinedIds);
            propertyIds.addAll(keepers);
        }
        return propertyIds;
    }

    protected boolean checkCategory(java.lang.String unsupportedPropertyId) {
        java.lang.String category = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyCategory(unsupportedPropertyId);
        while (category != null) {
            if (propertyIds.contains(category)) {
                return true;
            }
            category = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyCategory(category);
        } 
        return false;
    }

    private boolean checkRegExp(java.lang.String unsupportedPropertyId) {
        for (java.util.regex.Pattern pattern : patterns.values()) {
            java.util.regex.Matcher matcher = pattern.matcher(unsupportedPropertyId);
            if (matcher.matches()) {
                return true;
            }
        }
        return false;
    }

    protected java.util.Map.Entry<java.lang.String, java.util.regex.Pattern> getRegexEntry(java.lang.String id) {
        java.util.Map.Entry<java.lang.String, java.util.regex.Pattern> regexEntry = null;
        for (java.util.Map.Entry<java.lang.String, java.util.regex.Pattern> entry : patterns.entrySet()) {
            java.util.regex.Pattern pattern = entry.getValue();
            java.util.regex.Matcher matcher = pattern.matcher(id);
            if (matcher.matches()) {
                java.lang.String key = entry.getKey();
                if ((regexEntry == null) || key.startsWith(regexEntry.getKey())) {
                    regexEntry = entry;
                }
            }
        }
        return regexEntry;
    }

    protected java.util.List<java.lang.String> getRegexGroups(java.lang.String regExpKey, java.lang.String id) {
        java.util.regex.Pattern pattern = patterns.get(regExpKey);
        java.util.List<java.lang.String> regexGroups = new java.util.ArrayList<>();
        if (pattern != null) {
            java.util.regex.Matcher matcher = pattern.matcher(id);
            if (matcher.matches()) {
                for (int i = 0; i < matcher.groupCount(); i++) {
                    regexGroups.add(matcher.group(i + 1));
                }
            }
        }
        return regexGroups;
    }

    protected boolean isPatternKey(java.lang.String id) {
        return patterns.containsKey(id);
    }

    protected boolean containsArguments(java.lang.String propertyId) {
        return org.apache.ambari.server.controller.utilities.PropertyHelper.containsArguments(propertyId);
    }

    protected static boolean isPropertyRequested(java.lang.String propertyId, java.util.Set<java.lang.String> requestedIds) {
        return (requestedIds.contains(propertyId) || org.apache.ambari.server.controller.internal.BaseProvider.isPropertyCategoryRequested(propertyId, requestedIds)) || org.apache.ambari.server.controller.internal.BaseProvider.isPropertyEntryRequested(propertyId, requestedIds);
    }

    protected static boolean setResourceProperty(org.apache.ambari.server.controller.spi.Resource resource, java.lang.String propertyId, java.lang.Object value, java.util.Set<java.lang.String> requestedIds) {
        boolean contains = requestedIds.contains(propertyId) || org.apache.ambari.server.controller.internal.BaseProvider.isPropertyCategoryRequested(propertyId, requestedIds);
        if (contains) {
            if (org.apache.ambari.server.controller.internal.BaseProvider.LOG.isDebugEnabled()) {
                org.apache.ambari.server.controller.internal.BaseProvider.LOG.debug("Setting property for resource, resourceType={}, propertyId={}, value={}", resource.getType(), propertyId, value);
            }
            if (!org.apache.ambari.server.controller.internal.BaseProvider.setResourceMapProperty(resource, propertyId, value)) {
                resource.setProperty(propertyId, value);
            }
        } else {
            if (value instanceof java.util.Map<?, ?>) {
                java.util.Map<?, ?> mapValue = ((java.util.Map) (value));
                for (java.util.Map.Entry entry : mapValue.entrySet()) {
                    java.lang.String entryPropertyId = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId(propertyId, entry.getKey().toString());
                    java.lang.Object entryValue = entry.getValue();
                    contains = org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, entryPropertyId, entryValue, requestedIds) || contains;
                }
            }
            if ((!contains) && org.apache.ambari.server.controller.internal.BaseProvider.LOG.isDebugEnabled()) {
                org.apache.ambari.server.controller.internal.BaseProvider.LOG.debug("Skipping property for resource as not in requestedIds, resourceType={}, propertyId={}, value={}", resource.getType(), propertyId, value);
            }
        }
        return contains;
    }

    private static boolean setResourceMapProperty(org.apache.ambari.server.controller.spi.Resource resource, java.lang.String propertyId, java.lang.Object value) {
        if (value instanceof java.util.Map<?, ?>) {
            java.util.Map<?, ?> mapValue = ((java.util.Map) (value));
            if (mapValue.isEmpty()) {
                resource.addCategory(propertyId);
            } else {
                for (java.util.Map.Entry entry : mapValue.entrySet()) {
                    java.lang.String entryPropertyId = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId(propertyId, entry.getKey().toString());
                    java.lang.Object entryValue = entry.getValue();
                    if (!org.apache.ambari.server.controller.internal.BaseProvider.setResourceMapProperty(resource, entryPropertyId, entryValue)) {
                        resource.setProperty(entryPropertyId, entryValue);
                    }
                }
            }
            return true;
        }
        return false;
    }

    protected static boolean isPropertyEntryRequested(java.lang.String propertyId, java.util.Set<java.lang.String> requestedIds) {
        for (java.lang.String requestedId : requestedIds) {
            if (requestedId.startsWith(propertyId)) {
                return true;
            }
        }
        return false;
    }

    protected static boolean isPropertyCategoryRequested(java.lang.String propertyId, java.util.Set<java.lang.String> requestedIds) {
        java.lang.String category = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyCategory(propertyId);
        while (category != null) {
            if (requestedIds.contains(category)) {
                return true;
            }
            category = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyCategory(category);
        } 
        return false;
    }

    public java.util.Set<java.lang.String> getPropertyIds() {
        return propertyIds;
    }

    public java.util.Set<java.lang.String> getCategoryIds() {
        return categoryIds;
    }
}