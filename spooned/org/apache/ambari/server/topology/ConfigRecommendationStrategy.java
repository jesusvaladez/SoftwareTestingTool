package org.apache.ambari.server.topology;
public enum ConfigRecommendationStrategy {

    ALWAYS_APPLY(true, true),
    NEVER_APPLY(false, false),
    ONLY_STACK_DEFAULTS_APPLY(true, false),
    ALWAYS_APPLY_DONT_OVERRIDE_CUSTOM_VALUES(true, false);
    public static org.apache.ambari.server.topology.ConfigRecommendationStrategy defaultForAddService() {
        return org.apache.ambari.server.topology.ConfigRecommendationStrategy.ALWAYS_APPLY_DONT_OVERRIDE_CUSTOM_VALUES;
    }

    private final boolean useStackAdvisor;

    private final boolean overrideCustomValues;

    public boolean shouldUseStackAdvisor() {
        return useStackAdvisor;
    }

    public boolean shouldOverrideCustomValues() {
        return overrideCustomValues;
    }

    ConfigRecommendationStrategy(boolean useStackAdvisor, boolean overrideCustomValues) {
        this.useStackAdvisor = useStackAdvisor;
        this.overrideCustomValues = overrideCustomValues;
    }
}