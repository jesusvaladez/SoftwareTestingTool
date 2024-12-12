package org.apache.ambari.server.controller;
import javax.annotation.Nullable;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
public class AuthToLocalBuilder implements java.lang.Cloneable {
    public static final org.apache.ambari.server.controller.AuthToLocalBuilder.ConcatenationType DEFAULT_CONCATENATION_TYPE = org.apache.ambari.server.controller.AuthToLocalBuilder.ConcatenationType.NEW_LINES;

    private java.util.Set<org.apache.ambari.server.controller.AuthToLocalBuilder.Rule> setRules = new java.util.TreeSet<>();

    private final java.lang.String defaultRealm;

    private final java.util.Set<java.lang.String> additionalRealms;

    private boolean caseInsensitiveUser;

    public AuthToLocalBuilder(java.lang.String defaultRealm, java.lang.String additionalRealms, boolean caseInsensitiveUserSupport) {
        this(defaultRealm, org.apache.ambari.server.controller.AuthToLocalBuilder.splitDelimitedString(additionalRealms), caseInsensitiveUserSupport);
    }

    public AuthToLocalBuilder(java.lang.String defaultRealm, java.util.Collection<java.lang.String> additionalRealms, boolean caseInsensitiveUserSupport) {
        this.defaultRealm = defaultRealm;
        this.additionalRealms = (additionalRealms == null) ? java.util.Collections.emptySet() : java.util.Collections.unmodifiableSet(new java.util.HashSet<>(additionalRealms));
        this.caseInsensitiveUser = caseInsensitiveUserSupport;
    }

    @java.lang.Override
    public java.lang.Object clone() throws java.lang.CloneNotSupportedException {
        org.apache.ambari.server.controller.AuthToLocalBuilder copy = ((org.apache.ambari.server.controller.AuthToLocalBuilder) (super.clone()));
        copy.setRules = new java.util.TreeSet<>(setRules);
        return copy;
    }

    public org.apache.ambari.server.controller.AuthToLocalBuilder addRules(java.lang.String authToLocalRules) {
        if (!org.apache.commons.lang.StringUtils.isEmpty(authToLocalRules)) {
            java.lang.String[] rules = authToLocalRules.split("RULE:|DEFAULT");
            for (java.lang.String r : rules) {
                r = r.trim();
                if (!r.isEmpty()) {
                    org.apache.ambari.server.controller.AuthToLocalBuilder.Rule rule = createRule(r);
                    if (!setRules.contains(rule.caseSensitivityInverted())) {
                        setRules.add(rule);
                    }
                }
            }
        }
        return this;
    }

    public org.apache.ambari.server.controller.AuthToLocalBuilder addRule(java.lang.String principal, java.lang.String localUsername) {
        if ((!org.apache.commons.lang.StringUtils.isEmpty(principal)) && (!org.apache.commons.lang.StringUtils.isEmpty(localUsername))) {
            org.apache.ambari.server.controller.AuthToLocalBuilder.Principal p = new org.apache.ambari.server.controller.AuthToLocalBuilder.Principal(principal);
            if (p.getRealm() == null) {
                throw new java.lang.IllegalArgumentException("Attempted to add a rule for a principal with no realm: " + principal);
            }
            org.apache.ambari.server.controller.AuthToLocalBuilder.Rule rule = createHostAgnosticRule(p, localUsername);
            setRules.add(rule);
        }
        return this;
    }

    public java.lang.String generate() {
        return generate(null);
    }

    public java.lang.String generate(org.apache.ambari.server.controller.AuthToLocalBuilder.ConcatenationType concatenationType) {
        java.lang.StringBuilder builder = new java.lang.StringBuilder();
        if (!org.apache.commons.lang.StringUtils.isEmpty(defaultRealm)) {
            setRules.remove(createDefaultRealmRule(defaultRealm, !caseInsensitiveUser));
            setRules.add(createDefaultRealmRule(defaultRealm, caseInsensitiveUser));
        }
        for (java.lang.String additionalRealm : additionalRealms) {
            setRules.remove(createDefaultRealmRule(additionalRealm, !caseInsensitiveUser));
            setRules.add(createDefaultRealmRule(additionalRealm, caseInsensitiveUser));
        }
        if (concatenationType == null) {
            concatenationType = org.apache.ambari.server.controller.AuthToLocalBuilder.DEFAULT_CONCATENATION_TYPE;
        }
        for (org.apache.ambari.server.controller.AuthToLocalBuilder.Rule rule : setRules) {
            appendRule(builder, rule.toString(), concatenationType);
        }
        appendRule(builder, "DEFAULT", concatenationType);
        return builder.toString();
    }

    private void appendRule(java.lang.StringBuilder stringBuilder, java.lang.String rule, org.apache.ambari.server.controller.AuthToLocalBuilder.ConcatenationType concatenationType) {
        if (stringBuilder.length() > 0) {
            switch (concatenationType) {
                case NEW_LINES :
                    stringBuilder.append('\n');
                    break;
                case NEW_LINES_ESCAPED :
                    stringBuilder.append("\\\n");
                    break;
                case SPACES :
                    stringBuilder.append(" ");
                    break;
                case COMMA :
                    stringBuilder.append(",");
                    break;
                default :
                    throw new java.lang.UnsupportedOperationException(java.lang.String.format("The auth-to-local rule concatenation type is not supported: %s", concatenationType.name()));
            }
        }
        stringBuilder.append(rule);
    }

    private org.apache.ambari.server.controller.AuthToLocalBuilder.Rule createHostAgnosticRule(org.apache.ambari.server.controller.AuthToLocalBuilder.Principal principal, java.lang.String localUser) {
        java.util.List<java.lang.String> principalComponents = principal.getComponents();
        int componentCount = principalComponents.size();
        return new org.apache.ambari.server.controller.AuthToLocalBuilder.Rule(org.apache.ambari.server.controller.AuthToLocalBuilder.MatchingRule.ignoreHostWhenComponentCountIs(componentCount), new org.apache.ambari.server.controller.AuthToLocalBuilder.Principal((principal.getComponent(1) + "@") + principal.getRealm()), new org.apache.ambari.server.controller.AuthToLocalBuilder.Substitution(".*", localUser, "", false));
    }

    private org.apache.ambari.server.controller.AuthToLocalBuilder.Rule createDefaultRealmRule(java.lang.String realm, boolean caseInsensitive) {
        return new org.apache.ambari.server.controller.AuthToLocalBuilder.Rule(org.apache.ambari.server.controller.AuthToLocalBuilder.MatchingRule.ignoreHostWhenComponentCountIs(1), new org.apache.ambari.server.controller.AuthToLocalBuilder.Principal(".*@" + realm), new org.apache.ambari.server.controller.AuthToLocalBuilder.Substitution("@.*", "", "", caseInsensitive));
    }

    private org.apache.ambari.server.controller.AuthToLocalBuilder.Rule createRule(java.lang.String rule) {
        return org.apache.ambari.server.controller.AuthToLocalBuilder.Rule.parse(rule.startsWith("RULE:") ? rule : java.lang.String.format("RULE:%s", rule));
    }

    private static java.util.Collection<java.lang.String> splitDelimitedString(java.lang.String string) {
        java.util.Collection<java.lang.String> collection = null;
        if (!org.apache.commons.lang.StringUtils.isEmpty(string)) {
            collection = new java.util.HashSet<>();
            for (java.lang.String realm : string.split("\\s*(?:\\r?\\n|,)\\s*")) {
                realm = realm.trim();
                if (!realm.isEmpty()) {
                    collection.add(realm);
                }
            }
        }
        return collection;
    }

    private static class Rule implements java.lang.Comparable<org.apache.ambari.server.controller.AuthToLocalBuilder.Rule> {
        private static final java.util.regex.Pattern PATTERN_RULE_PARSE = java.util.regex.Pattern.compile("RULE:\\s*\\[\\s*(\\d)\\s*:\\s*(.+?)(?:@(.+?))??\\s*\\]\\s*\\((.+?)\\)\\s*s/(.*?)/(.*?)/([a-zA-Z]*)((/L)?)(?:.|\n)*");

        private final org.apache.ambari.server.controller.AuthToLocalBuilder.MatchingRule matchingRule;

        private final org.apache.ambari.server.controller.AuthToLocalBuilder.Principal principal;

        private final org.apache.ambari.server.controller.AuthToLocalBuilder.Substitution substitution;

        public static org.apache.ambari.server.controller.AuthToLocalBuilder.Rule parse(java.lang.String rule) {
            java.util.regex.Matcher m = org.apache.ambari.server.controller.AuthToLocalBuilder.Rule.PATTERN_RULE_PARSE.matcher(rule);
            if (!m.matches()) {
                throw new java.lang.IllegalArgumentException("Invalid rule: " + rule);
            }
            int expectedComponentCount = java.lang.Integer.parseInt(m.group(1));
            java.lang.String matchPattern = m.group(2);
            java.lang.String optionalPatternRealm = m.group(3);
            java.lang.String matchingRegexp = m.group(4);
            java.lang.String replacementPattern = m.group(5);
            java.lang.String replacementReplacement = m.group(6);
            java.lang.String replacementModifier = m.group(7);
            java.lang.String caseSensitivity = m.group(8);
            return new org.apache.ambari.server.controller.AuthToLocalBuilder.Rule(new org.apache.ambari.server.controller.AuthToLocalBuilder.MatchingRule(expectedComponentCount, matchPattern, optionalPatternRealm), new org.apache.ambari.server.controller.AuthToLocalBuilder.Principal(matchingRegexp), new org.apache.ambari.server.controller.AuthToLocalBuilder.Substitution(replacementPattern, replacementReplacement, replacementModifier, !caseSensitivity.isEmpty()));
        }

        public Rule(org.apache.ambari.server.controller.AuthToLocalBuilder.MatchingRule matchingRule, org.apache.ambari.server.controller.AuthToLocalBuilder.Principal principal, org.apache.ambari.server.controller.AuthToLocalBuilder.Substitution substitution) {
            this.matchingRule = matchingRule;
            this.principal = principal;
            this.substitution = substitution;
        }

        @java.lang.Override
        public java.lang.String toString() {
            return java.lang.String.format("RULE:%s(%s)%s", matchingRule, principal, substitution);
        }

        @java.lang.Override
        public boolean equals(java.lang.Object o) {
            if (this == o)
                return true;

            if ((o == null) || (getClass() != o.getClass()))
                return false;

            org.apache.ambari.server.controller.AuthToLocalBuilder.Rule rule = ((org.apache.ambari.server.controller.AuthToLocalBuilder.Rule) (o));
            return new org.apache.commons.lang.builder.EqualsBuilder().append(matchingRule, rule.matchingRule).append(principal, rule.principal).append(substitution, rule.substitution).isEquals();
        }

        @java.lang.Override
        public int hashCode() {
            return new org.apache.commons.lang.builder.HashCodeBuilder(17, 37).append(matchingRule).append(principal).append(substitution).toHashCode();
        }

        @java.lang.Override
        public int compareTo(org.apache.ambari.server.controller.AuthToLocalBuilder.Rule other) {
            int retVal = matchingRule.expectedComponentCount - other.matchingRule.expectedComponentCount;
            if (retVal == 0) {
                retVal = other.matchingRule.matchComponentCount() - matchingRule.matchComponentCount();
                if (retVal == 0) {
                    if (this.principal.equals(other.principal)) {
                        retVal = toString().compareTo(other.toString());
                    } else {
                        java.lang.String realm = this.principal.getRealm();
                        java.lang.String otherRealm = other.principal.getRealm();
                        retVal = compareValueWithWildcards(realm, otherRealm);
                        if (retVal == 0) {
                            for (int i = 1; i <= matchingRule.matchComponentCount(); i++) {
                                java.lang.String component1 = this.principal.getComponent(1);
                                java.lang.String otherComponent1 = other.principal.getComponent(1);
                                retVal = compareValueWithWildcards(component1, otherComponent1);
                                if (retVal != 0) {
                                    break;
                                }
                            }
                        }
                    }
                }
            }
            return retVal;
        }

        private int compareValueWithWildcards(java.lang.String s1, java.lang.String s2) {
            if (s1 == null) {
                if (s2 == null) {
                    return 0;
                } else {
                    return -1;
                }
            } else if (s2 == null) {
                return 1;
            } else if (s1.equals(s2)) {
                return 0;
            } else if (s1.equals(".*")) {
                return 1;
            } else if (s2.equals(".*")) {
                return -1;
            } else {
                return s1.compareTo(s2);
            }
        }

        public org.apache.ambari.server.controller.AuthToLocalBuilder.Rule caseSensitivityInverted() {
            return new org.apache.ambari.server.controller.AuthToLocalBuilder.Rule(matchingRule, principal, substitution.caseSensitivityInverted());
        }
    }

    private static class MatchingRule {
        private final int expectedComponentCount;

        private final java.lang.String matchPattern;

        private final java.lang.String realmPattern;

        public static org.apache.ambari.server.controller.AuthToLocalBuilder.MatchingRule ignoreHostWhenComponentCountIs(int expectedComponentCount) {
            return new org.apache.ambari.server.controller.AuthToLocalBuilder.MatchingRule(expectedComponentCount, "$1", "$0");
        }

        public MatchingRule(int expectedComponentCount, java.lang.String matchPattern, @javax.annotation.Nullable
        java.lang.String realmPattern) {
            this.expectedComponentCount = expectedComponentCount;
            this.matchPattern = matchPattern;
            this.realmPattern = realmPattern;
        }

        public int matchComponentCount() {
            return (matchPattern.startsWith("$") ? matchPattern.substring(1) : matchPattern).split("\\$").length;
        }

        @java.lang.Override
        public java.lang.String toString() {
            return realmPattern != null ? java.lang.String.format("[%d:%s@%s]", expectedComponentCount, matchPattern, realmPattern) : java.lang.String.format("[%d:%s]", expectedComponentCount, matchPattern);
        }

        @java.lang.Override
        public boolean equals(java.lang.Object o) {
            if (this == o)
                return true;

            if ((o == null) || (getClass() != o.getClass()))
                return false;

            org.apache.ambari.server.controller.AuthToLocalBuilder.MatchingRule that = ((org.apache.ambari.server.controller.AuthToLocalBuilder.MatchingRule) (o));
            return new org.apache.commons.lang.builder.EqualsBuilder().append(expectedComponentCount, that.expectedComponentCount).append(matchPattern, that.matchPattern).append(realmPattern, that.realmPattern).isEquals();
        }

        @java.lang.Override
        public int hashCode() {
            return new org.apache.commons.lang.builder.HashCodeBuilder(17, 37).append(expectedComponentCount).append(matchPattern).append(realmPattern).toHashCode();
        }
    }

    private static class Substitution {
        private final java.lang.String pattern;

        private final java.lang.String replacement;

        private final java.lang.String modifier;

        private final boolean caseInsensitiveUser;

        public Substitution(java.lang.String pattern, java.lang.String replacement, java.lang.String modifier, boolean caseInsensitiveUser) {
            this.pattern = pattern;
            this.replacement = replacement;
            this.modifier = modifier;
            this.caseInsensitiveUser = caseInsensitiveUser;
        }

        @java.lang.Override
        public java.lang.String toString() {
            return java.lang.String.format("s/%s/%s/%s%s", pattern, replacement, modifier, caseInsensitiveUser ? "/L" : "");
        }

        @java.lang.Override
        public boolean equals(java.lang.Object o) {
            if (this == o)
                return true;

            if ((o == null) || (getClass() != o.getClass()))
                return false;

            org.apache.ambari.server.controller.AuthToLocalBuilder.Substitution that = ((org.apache.ambari.server.controller.AuthToLocalBuilder.Substitution) (o));
            return new org.apache.commons.lang.builder.EqualsBuilder().append(caseInsensitiveUser, that.caseInsensitiveUser).append(pattern, that.pattern).append(replacement, that.replacement).append(modifier, that.modifier).isEquals();
        }

        @java.lang.Override
        public int hashCode() {
            return new org.apache.commons.lang.builder.HashCodeBuilder(17, 37).append(pattern).append(replacement).append(modifier).append(caseInsensitiveUser).toHashCode();
        }

        public org.apache.ambari.server.controller.AuthToLocalBuilder.Substitution caseSensitivityInverted() {
            return new org.apache.ambari.server.controller.AuthToLocalBuilder.Substitution(pattern, replacement, modifier, !caseInsensitiveUser);
        }
    }

    private static class Principal {
        private static final java.util.regex.Pattern p = java.util.regex.Pattern.compile("([^@]+)(?:@(.*))?");

        private java.lang.String principal;

        private java.lang.String realm;

        private java.util.List<java.lang.String> components;

        public Principal(java.lang.String principal) {
            this.principal = principal;
            java.util.regex.Matcher m = org.apache.ambari.server.controller.AuthToLocalBuilder.Principal.p.matcher(principal);
            if (m.matches()) {
                java.lang.String allComponents = m.group(1);
                if (allComponents == null) {
                    components = java.util.Collections.emptyList();
                } else {
                    allComponents = (allComponents.startsWith("/")) ? allComponents.substring(1) : allComponents;
                    components = java.util.Arrays.asList(allComponents.split("/"));
                }
                realm = m.group(2);
            } else {
                throw new java.lang.IllegalArgumentException("Invalid Principal: " + principal);
            }
        }

        public java.util.List<java.lang.String> getComponents() {
            return components;
        }

        public java.lang.String getComponent(int position) {
            if (position > components.size()) {
                return null;
            } else {
                return components.get(position - 1);
            }
        }

        public java.lang.String getRealm() {
            return realm;
        }

        @java.lang.Override
        public java.lang.String toString() {
            return principal;
        }

        @java.lang.Override
        public boolean equals(java.lang.Object o) {
            if (this == o) {
                return true;
            }
            if ((o == null) || (getClass() != o.getClass())) {
                return false;
            }
            org.apache.ambari.server.controller.AuthToLocalBuilder.Principal principal1 = ((org.apache.ambari.server.controller.AuthToLocalBuilder.Principal) (o));
            return (components.equals(principal1.components) && principal.equals(principal1.principal)) && (!(realm != null ? !realm.equals(principal1.realm) : principal1.realm != null));
        }

        @java.lang.Override
        public int hashCode() {
            int result = principal.hashCode();
            result = (31 * result) + (realm != null ? realm.hashCode() : 0);
            result = (31 * result) + components.hashCode();
            return result;
        }
    }

    public enum ConcatenationType {

        NEW_LINES,
        NEW_LINES_ESCAPED,
        SPACES,
        COMMA;
        public static org.apache.ambari.server.controller.AuthToLocalBuilder.ConcatenationType translate(java.lang.String value) {
            if (value != null) {
                value = value.trim();
                if (!value.isEmpty()) {
                    return org.apache.ambari.server.controller.AuthToLocalBuilder.ConcatenationType.valueOf(value.toUpperCase());
                }
            }
            return org.apache.ambari.server.controller.AuthToLocalBuilder.DEFAULT_CONCATENATION_TYPE;
        }
    }
}