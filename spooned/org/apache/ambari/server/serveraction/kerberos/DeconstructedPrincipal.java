package org.apache.ambari.server.serveraction.kerberos;
import javax.annotation.Nullable;
public class DeconstructedPrincipal {
    private static java.util.regex.Pattern PATTERN_PRINCIPAL = java.util.regex.Pattern.compile("^([^ /@]+)(?:/([^ /@]+))?(?:@(.+)?)?$");

    private final java.lang.String primary;

    private final java.lang.String instance;

    private final java.lang.String realm;

    private final java.lang.String principalName;

    private final java.lang.String normalizedPrincipal;

    public static org.apache.ambari.server.serveraction.kerberos.DeconstructedPrincipal valueOf(java.lang.String principal, @javax.annotation.Nullable
    java.lang.String defaultRealm) {
        if (principal == null) {
            throw new java.lang.IllegalArgumentException("The principal may not be null");
        }
        java.util.regex.Matcher matcher = org.apache.ambari.server.serveraction.kerberos.DeconstructedPrincipal.PATTERN_PRINCIPAL.matcher(principal);
        if (matcher.matches()) {
            java.lang.String primary = matcher.group(1);
            java.lang.String instance = matcher.group(2);
            java.lang.String realm = matcher.group(3);
            if ((realm == null) || realm.isEmpty()) {
                realm = defaultRealm;
            }
            return new org.apache.ambari.server.serveraction.kerberos.DeconstructedPrincipal(primary, instance, realm);
        } else {
            throw new java.lang.IllegalArgumentException(java.lang.String.format("Invalid principal value: %s", principal));
        }
    }

    protected DeconstructedPrincipal(java.lang.String primary, java.lang.String instance, java.lang.String realm) {
        this.primary = primary;
        this.instance = instance;
        this.realm = realm;
        java.lang.StringBuilder builder = new java.lang.StringBuilder();
        if (this.primary != null) {
            builder.append(primary);
        }
        if (this.instance != null) {
            builder.append('/');
            builder.append(this.instance);
        }
        this.principalName = builder.toString();
        if (this.realm != null) {
            builder.append('@');
            builder.append(this.realm);
        }
        this.normalizedPrincipal = builder.toString();
    }

    public java.lang.String getPrimary() {
        return primary;
    }

    public java.lang.String getInstance() {
        return instance;
    }

    public java.lang.String getRealm() {
        return realm;
    }

    public java.lang.String getPrincipalName() {
        return principalName;
    }

    public java.lang.String getNormalizedPrincipal() {
        return normalizedPrincipal;
    }
}