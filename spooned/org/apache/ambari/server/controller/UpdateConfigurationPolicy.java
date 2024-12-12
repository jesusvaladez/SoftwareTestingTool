package org.apache.ambari.server.controller;
public enum UpdateConfigurationPolicy {

    NONE(false, false, false, false),
    IDENTITIES_ONLY(false, true, false, false),
    NEW_AND_IDENTITIES(true, true, true, false),
    ALL(true, true, true, true);
    private final boolean invokeStackAdvisor;

    private final boolean applyIdentityChanges;

    private final boolean applyAdditions;

    private final boolean applyOtherChanges;

    UpdateConfigurationPolicy(boolean invokeStackAdvisor, boolean applyIdentityChanges, boolean applyAdditions, boolean applyOtherChanges) {
        this.invokeStackAdvisor = invokeStackAdvisor;
        this.applyIdentityChanges = applyIdentityChanges;
        this.applyAdditions = applyAdditions;
        this.applyOtherChanges = applyOtherChanges;
    }

    public boolean invokeStackAdvisor() {
        return invokeStackAdvisor;
    }

    public boolean applyIdentityChanges() {
        return applyIdentityChanges;
    }

    public boolean applyAdditions() {
        return applyAdditions;
    }

    public boolean applyOtherChanges() {
        return applyOtherChanges;
    }

    public static java.lang.String translate(org.apache.ambari.server.controller.UpdateConfigurationPolicy value) {
        return value == null ? null : value.name().toLowerCase();
    }

    public static org.apache.ambari.server.controller.UpdateConfigurationPolicy translate(java.lang.String stringValue) {
        if (stringValue != null) {
            stringValue = stringValue.trim().toUpperCase();
            if (!stringValue.isEmpty()) {
                try {
                    return org.apache.ambari.server.controller.UpdateConfigurationPolicy.valueOf(stringValue.replace('-', '_'));
                } catch (java.lang.IllegalArgumentException e) {
                }
            }
        }
        return null;
    }
}