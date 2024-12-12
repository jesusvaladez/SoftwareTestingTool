package org.apache.ambari.server.security.authorization;
import org.springframework.ldap.core.DirContextAdapter;
import org.springframework.ldap.support.LdapUtils;
public class AmbariLdapUtils {
    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.security.authorization.AmbariLdapUtils.class);

    private static final java.util.regex.Pattern UPN_FORMAT = java.util.regex.Pattern.compile(".+@\\w+(\\.\\w+)*");

    public static boolean isUserPrincipalNameFormat(java.lang.String loginName) {
        return org.apache.ambari.server.security.authorization.AmbariLdapUtils.UPN_FORMAT.matcher(loginName).matches();
    }

    public static boolean isLdapObjectOutOfScopeFromBaseDn(org.springframework.ldap.core.DirContextAdapter adapter, java.lang.String baseDn) {
        boolean isOutOfScope = true;
        try {
            javax.naming.Name dn = adapter.getDn();
            com.google.common.base.Preconditions.checkArgument(dn != null, "DN cannot be null in LDAP response object");
            javax.naming.Name fullDn = org.apache.ambari.server.security.authorization.AmbariLdapUtils.getFullDn(dn, adapter);
            javax.naming.Name base = org.springframework.ldap.support.LdapUtils.newLdapName(baseDn);
            if (fullDn.startsWith(base)) {
                isOutOfScope = false;
            }
        } catch (java.lang.Exception e) {
            org.apache.ambari.server.security.authorization.AmbariLdapUtils.LOG.error(e.getMessage());
        }
        return isOutOfScope;
    }

    public static javax.naming.Name getFullDn(java.lang.String dn, javax.naming.Context context) throws javax.naming.NamingException {
        return org.apache.ambari.server.security.authorization.AmbariLdapUtils.getFullDn(org.springframework.ldap.support.LdapUtils.newLdapName(dn), context);
    }

    public static javax.naming.Name getFullDn(javax.naming.Name dn, javax.naming.Context context) throws javax.naming.NamingException {
        return org.apache.ambari.server.security.authorization.AmbariLdapUtils.getFullDn(org.springframework.ldap.support.LdapUtils.newLdapName(dn), org.springframework.ldap.support.LdapUtils.newLdapName(context.getNameInNamespace()));
    }

    public static javax.naming.Name getFullDn(javax.naming.Name dn, javax.naming.Name baseDn) {
        if (dn.startsWith(baseDn)) {
            return dn;
        } else {
            try {
                baseDn = org.springframework.ldap.support.LdapUtils.newLdapName(baseDn);
                baseDn.addAll(dn);
            } catch (javax.naming.InvalidNameException e) {
                org.apache.ambari.server.security.authorization.AmbariLdapUtils.LOG.error(e.getMessage());
            }
            return baseDn;
        }
    }
}