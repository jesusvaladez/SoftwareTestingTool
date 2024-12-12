package org.apache.ambari.server.serveraction.kerberos;
import org.apache.commons.lang.StringUtils;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.exception.MethodInvocationException;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;
public class ADKerberosOperationHandler extends org.apache.ambari.server.serveraction.kerberos.KerberosOperationHandler {
    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.serveraction.kerberos.ADKerberosOperationHandler.class);

    private static final java.lang.String LDAP_CONTEXT_FACTORY_CLASS = "com.sun.jndi.ldap.LdapCtxFactory";

    private java.lang.String ldapUrl = null;

    private java.lang.String principalContainerDn = null;

    private javax.naming.ldap.LdapName principalContainerLdapName = null;

    private java.lang.String createTemplate = null;

    private javax.naming.ldap.LdapContext ldapContext = null;

    private javax.naming.directory.SearchControls searchControls = null;

    @com.google.inject.Inject
    private com.google.gson.Gson gson;

    @com.google.inject.Inject
    private org.apache.ambari.server.configuration.Configuration configuration;

    @java.lang.Override
    public void open(org.apache.ambari.server.security.credential.PrincipalKeyCredential administratorCredential, java.lang.String realm, java.util.Map<java.lang.String, java.lang.String> kerberosConfiguration) throws org.apache.ambari.server.serveraction.kerberos.KerberosOperationException {
        if (isOpen()) {
            close();
        }
        if (administratorCredential == null) {
            throw new org.apache.ambari.server.serveraction.kerberos.KerberosAdminAuthenticationException("administrator credential not provided");
        }
        if (realm == null) {
            throw new org.apache.ambari.server.serveraction.kerberos.KerberosRealmException("realm not provided");
        }
        if (kerberosConfiguration == null) {
            throw new org.apache.ambari.server.serveraction.kerberos.KerberosRealmException("kerberos-env configuration may not be null");
        }
        this.ldapUrl = kerberosConfiguration.get(org.apache.ambari.server.serveraction.kerberos.KerberosOperationHandler.KERBEROS_ENV_LDAP_URL);
        if (this.ldapUrl == null) {
            throw new org.apache.ambari.server.serveraction.kerberos.KerberosKDCConnectionException("ldapUrl not provided");
        }
        if (!this.ldapUrl.startsWith("ldaps://")) {
            throw new org.apache.ambari.server.serveraction.kerberos.KerberosKDCConnectionException("ldapUrl is not valid ldaps URL");
        }
        this.principalContainerDn = kerberosConfiguration.get(org.apache.ambari.server.serveraction.kerberos.KerberosOperationHandler.KERBEROS_ENV_PRINCIPAL_CONTAINER_DN);
        if (this.principalContainerDn == null) {
            throw new org.apache.ambari.server.serveraction.kerberos.KerberosLDAPContainerException("principalContainerDn not provided");
        }
        try {
            this.principalContainerLdapName = new javax.naming.ldap.LdapName(principalContainerDn);
        } catch (javax.naming.InvalidNameException e) {
            throw new org.apache.ambari.server.serveraction.kerberos.KerberosLDAPContainerException("principalContainerDn is not a valid LDAP name", e);
        }
        super.open(administratorCredential, realm, kerberosConfiguration);
        this.ldapContext = createLdapContext();
        this.searchControls = createSearchControls();
        this.createTemplate = kerberosConfiguration.get(org.apache.ambari.server.serveraction.kerberos.KerberosOperationHandler.KERBEROS_ENV_AD_CREATE_ATTRIBUTES_TEMPLATE);
        setOpen(true);
    }

    @java.lang.Override
    public void close() throws org.apache.ambari.server.serveraction.kerberos.KerberosOperationException {
        this.searchControls = null;
        if (this.ldapContext != null) {
            try {
                this.ldapContext.close();
            } catch (javax.naming.NamingException e) {
                throw new org.apache.ambari.server.serveraction.kerberos.KerberosOperationException("Unexpected error", e);
            } finally {
                this.ldapContext = null;
            }
        }
        setOpen(false);
    }

    @java.lang.Override
    public boolean principalExists(java.lang.String principal, boolean service) throws org.apache.ambari.server.serveraction.kerberos.KerberosOperationException {
        if (!isOpen()) {
            throw new org.apache.ambari.server.serveraction.kerberos.KerberosOperationException("This operation handler has not been opened");
        }
        if (principal == null) {
            throw new org.apache.ambari.server.serveraction.kerberos.KerberosOperationException("principal is null");
        }
        org.apache.ambari.server.serveraction.kerberos.DeconstructedPrincipal deconstructPrincipal = createDeconstructPrincipal(principal);
        try {
            return findPrincipalDN(deconstructPrincipal.getNormalizedPrincipal()) != null;
        } catch (javax.naming.NamingException ne) {
            throw new org.apache.ambari.server.serveraction.kerberos.KerberosOperationException("can not check if principal exists: " + principal, ne);
        }
    }

    @java.lang.Override
    public java.lang.Integer createPrincipal(java.lang.String principal, java.lang.String password, boolean service) throws org.apache.ambari.server.serveraction.kerberos.KerberosOperationException {
        if (!isOpen()) {
            throw new org.apache.ambari.server.serveraction.kerberos.KerberosOperationException("This operation handler has not been opened");
        }
        if (principal == null) {
            throw new org.apache.ambari.server.serveraction.kerberos.KerberosOperationException("principal is null");
        }
        if (password == null) {
            throw new org.apache.ambari.server.serveraction.kerberos.KerberosOperationException("principal password is null");
        }
        if (principalExists(principal, service)) {
            throw new org.apache.ambari.server.serveraction.kerberos.KerberosPrincipalAlreadyExistsException(principal);
        }
        org.apache.ambari.server.serveraction.kerberos.DeconstructedPrincipal deconstructedPrincipal = createDeconstructPrincipal(principal);
        java.lang.String realm = deconstructedPrincipal.getRealm();
        if (realm == null) {
            realm = "";
        }
        java.util.Map<java.lang.String, java.lang.Object> context = new java.util.HashMap<>();
        context.put("normalized_principal", deconstructedPrincipal.getNormalizedPrincipal());
        context.put("principal_name", deconstructedPrincipal.getPrincipalName());
        context.put("principal_primary", deconstructedPrincipal.getPrimary());
        context.put("principal_instance", deconstructedPrincipal.getInstance());
        context.put("realm", realm);
        context.put("realm_lowercase", realm.toLowerCase());
        context.put("password", password);
        context.put("is_service", service);
        context.put("container_dn", this.principalContainerDn);
        context.put("principal_digest", org.apache.commons.codec.digest.DigestUtils.sha1Hex(deconstructedPrincipal.getNormalizedPrincipal()));
        context.put("principal_digest_256", org.apache.commons.codec.digest.DigestUtils.sha256Hex(deconstructedPrincipal.getNormalizedPrincipal()));
        context.put("principal_digest_512", org.apache.commons.codec.digest.DigestUtils.sha512Hex(deconstructedPrincipal.getNormalizedPrincipal()));
        java.util.Map<java.lang.String, java.lang.Object> data = processCreateTemplate(context);
        javax.naming.directory.Attributes attributes = new javax.naming.directory.BasicAttributes();
        java.lang.String cn = null;
        if (data != null) {
            for (java.util.Map.Entry<java.lang.String, java.lang.Object> entry : data.entrySet()) {
                java.lang.String key = entry.getKey();
                java.lang.Object value = entry.getValue();
                if ("unicodePwd".equals(key)) {
                    if (value instanceof java.lang.String) {
                        try {
                            attributes.put(new javax.naming.directory.BasicAttribute("unicodePwd", java.lang.String.format("\"%s\"", password).getBytes("UTF-16LE")));
                        } catch (java.io.UnsupportedEncodingException ue) {
                            throw new org.apache.ambari.server.serveraction.kerberos.KerberosOperationException("Can not encode password with UTF-16LE", ue);
                        }
                    }
                } else {
                    javax.naming.directory.Attribute attribute = new javax.naming.directory.BasicAttribute(key);
                    if (value instanceof java.util.Collection) {
                        for (java.lang.Object object : ((java.util.Collection) (value))) {
                            attribute.add(object);
                        }
                    } else {
                        if ("cn".equals(key) && (value != null)) {
                            cn = value.toString();
                        } else if ("sAMAccountName".equals(key) && (value != null)) {
                            value = value.toString().replaceAll("\\[|\\]|\\:|\\;|\\||\\=|\\+|\\*|\\?|\\<|\\>|\\/|\\\\|\\,|\\s", "_");
                        }
                        attribute.add(value);
                    }
                    attributes.put(attribute);
                }
            }
        }
        if (cn == null) {
            cn = deconstructedPrincipal.getNormalizedPrincipal();
        }
        try {
            javax.naming.ldap.Rdn rdn = new javax.naming.ldap.Rdn("cn", cn);
            javax.naming.ldap.LdapName name = new javax.naming.ldap.LdapName(principalContainerLdapName.getRdns());
            name.add(name.size(), rdn);
            ldapContext.createSubcontext(name, attributes);
        } catch (javax.naming.NamingException ne) {
            throw new org.apache.ambari.server.serveraction.kerberos.KerberosOperationException("Can not create principal : " + principal, ne);
        }
        return 0;
    }

    @java.lang.Override
    public java.lang.Integer setPrincipalPassword(java.lang.String principal, java.lang.String password, boolean service) throws org.apache.ambari.server.serveraction.kerberos.KerberosOperationException {
        if (!isOpen()) {
            throw new org.apache.ambari.server.serveraction.kerberos.KerberosOperationException("This operation handler has not been opened");
        }
        if (principal == null) {
            throw new org.apache.ambari.server.serveraction.kerberos.KerberosOperationException("principal is null");
        }
        if (password == null) {
            throw new org.apache.ambari.server.serveraction.kerberos.KerberosOperationException("principal password is null");
        }
        if (!principalExists(principal, service)) {
            throw new org.apache.ambari.server.serveraction.kerberos.KerberosPrincipalDoesNotExistException(principal);
        }
        org.apache.ambari.server.serveraction.kerberos.DeconstructedPrincipal deconstructPrincipal = createDeconstructPrincipal(principal);
        try {
            java.lang.String dn = findPrincipalDN(deconstructPrincipal.getNormalizedPrincipal());
            if (dn != null) {
                ldapContext.modifyAttributes(new javax.naming.ldap.LdapName(dn), new javax.naming.directory.ModificationItem[]{ new javax.naming.directory.ModificationItem(javax.naming.directory.DirContext.REPLACE_ATTRIBUTE, new javax.naming.directory.BasicAttribute("unicodePwd", java.lang.String.format("\"%s\"", password).getBytes("UTF-16LE"))) });
            } else {
                throw new org.apache.ambari.server.serveraction.kerberos.KerberosOperationException(java.lang.String.format("Can not set password for principal %s: Not Found", principal));
            }
        } catch (javax.naming.NamingException e) {
            throw new org.apache.ambari.server.serveraction.kerberos.KerberosOperationException(java.lang.String.format("Can not set password for principal %s: %s", principal, e.getMessage()), e);
        } catch (java.io.UnsupportedEncodingException e) {
            throw new org.apache.ambari.server.serveraction.kerberos.KerberosOperationException("Unsupported encoding UTF-16LE", e);
        }
        return 0;
    }

    @java.lang.Override
    public boolean removePrincipal(java.lang.String principal, boolean service) throws org.apache.ambari.server.serveraction.kerberos.KerberosOperationException {
        if (!isOpen()) {
            throw new org.apache.ambari.server.serveraction.kerberos.KerberosOperationException("This operation handler has not been opened");
        }
        if (principal == null) {
            throw new org.apache.ambari.server.serveraction.kerberos.KerberosOperationException("principal is null");
        }
        org.apache.ambari.server.serveraction.kerberos.DeconstructedPrincipal deconstructPrincipal = createDeconstructPrincipal(principal);
        try {
            java.lang.String dn = findPrincipalDN(deconstructPrincipal.getNormalizedPrincipal());
            if (dn != null) {
                ldapContext.destroySubcontext(new javax.naming.ldap.LdapName(dn));
            }
        } catch (javax.naming.NamingException e) {
            throw new org.apache.ambari.server.serveraction.kerberos.KerberosOperationException(java.lang.String.format("Can not remove principal %s: %s", principal, e.getMessage()), e);
        }
        return true;
    }

    @java.lang.Override
    public boolean testAdministratorCredentials() throws org.apache.ambari.server.serveraction.kerberos.KerberosOperationException {
        if (!isOpen()) {
            throw new org.apache.ambari.server.serveraction.kerberos.KerberosOperationException("This operation handler has not been opened");
        }
        return true;
    }

    protected javax.naming.ldap.LdapContext createLdapContext() throws org.apache.ambari.server.serveraction.kerberos.KerberosOperationException {
        org.apache.ambari.server.security.credential.PrincipalKeyCredential administratorCredential = getAdministratorCredential();
        java.util.Properties properties = new java.util.Properties();
        properties.put(javax.naming.Context.INITIAL_CONTEXT_FACTORY, org.apache.ambari.server.serveraction.kerberos.ADKerberosOperationHandler.LDAP_CONTEXT_FACTORY_CLASS);
        properties.put(javax.naming.Context.PROVIDER_URL, ldapUrl);
        properties.put(javax.naming.Context.SECURITY_PRINCIPAL, administratorCredential.getPrincipal());
        properties.put(javax.naming.Context.SECURITY_CREDENTIALS, java.lang.String.valueOf(administratorCredential.getKey()));
        properties.put(javax.naming.Context.SECURITY_AUTHENTICATION, "simple");
        properties.put(javax.naming.Context.REFERRAL, "follow");
        if (ldapUrl.startsWith("ldaps")) {
            if (configuration.validateKerberosOperationSSLCertTrust()) {
                properties.put("java.naming.ldap.factory.socket", org.apache.ambari.server.security.InternalSSLSocketFactoryNonTrusting.class.getName());
            } else {
                properties.put("java.naming.ldap.factory.socket", org.apache.ambari.server.security.InternalSSLSocketFactoryTrusting.class.getName());
            }
        }
        try {
            return createInitialLdapContext(properties, null);
        } catch (javax.naming.CommunicationException e) {
            java.lang.Throwable rootCause = e.getRootCause();
            java.lang.String message = java.lang.String.format("Failed to communicate with the Active Directory at %s: %s", ldapUrl, e.getMessage());
            org.apache.ambari.server.serveraction.kerberos.ADKerberosOperationHandler.LOG.warn(message, e);
            if (rootCause instanceof javax.net.ssl.SSLHandshakeException) {
                throw new org.apache.ambari.server.serveraction.kerberos.KerberosKDCSSLConnectionException(message, e);
            } else {
                throw new org.apache.ambari.server.serveraction.kerberos.KerberosKDCConnectionException(message, e);
            }
        } catch (javax.naming.AuthenticationException e) {
            java.lang.String message = java.lang.String.format("Failed to authenticate with the Active Directory at %s: %s", ldapUrl, e.getMessage());
            org.apache.ambari.server.serveraction.kerberos.ADKerberosOperationHandler.LOG.warn(message, e);
            throw new org.apache.ambari.server.serveraction.kerberos.KerberosAdminAuthenticationException(message, e);
        } catch (javax.naming.NamingException e) {
            java.lang.String error = e.getMessage();
            if (org.apache.commons.lang.StringUtils.isEmpty(error)) {
                java.lang.String message = java.lang.String.format("Failed to communicate with the Active Directory at %s: %s", ldapUrl, e.getMessage());
                org.apache.ambari.server.serveraction.kerberos.ADKerberosOperationHandler.LOG.warn(message, e);
                if (error.startsWith("Cannot parse url:")) {
                    throw new org.apache.ambari.server.serveraction.kerberos.KerberosKDCConnectionException(message, e);
                } else {
                    throw new org.apache.ambari.server.serveraction.kerberos.KerberosOperationException(message, e);
                }
            } else {
                throw new org.apache.ambari.server.serveraction.kerberos.KerberosOperationException("Unexpected error condition", e);
            }
        }
    }

    protected javax.naming.ldap.LdapContext createInitialLdapContext(java.util.Properties properties, javax.naming.ldap.Control[] controls) throws javax.naming.NamingException {
        return new javax.naming.ldap.InitialLdapContext(properties, controls);
    }

    protected javax.naming.directory.SearchControls createSearchControls() {
        javax.naming.directory.SearchControls searchControls = new javax.naming.directory.SearchControls();
        searchControls.setSearchScope(javax.naming.directory.SearchControls.ONELEVEL_SCOPE);
        searchControls.setReturningAttributes(new java.lang.String[]{ "cn" });
        return searchControls;
    }

    protected java.util.Map<java.lang.String, java.lang.Object> processCreateTemplate(java.util.Map<java.lang.String, java.lang.Object> context) throws org.apache.ambari.server.serveraction.kerberos.KerberosOperationException {
        if (gson == null) {
            throw new org.apache.ambari.server.serveraction.kerberos.KerberosOperationException("The JSON parser must not be null");
        }
        java.util.Map<java.lang.String, java.lang.Object> data = null;
        java.lang.String template;
        java.io.StringWriter stringWriter = new java.io.StringWriter();
        if (org.apache.commons.lang.StringUtils.isEmpty(createTemplate)) {
            template = "{" + ((((((((("\"objectClass\": [\"top\", \"person\", \"organizationalPerson\", \"user\"]," + "\"cn\": \"$principal_name\",") + "#if( $is_service )") + "  \"servicePrincipalName\": \"$principal_name\",") + "#end") + "\"userPrincipalName\": \"$normalized_principal\",") + "\"unicodePwd\": \"$password\",") + "\"accountExpires\": \"0\",") + "\"userAccountControl\": \"66048\"") + "}");
        } else {
            template = createTemplate;
        }
        try {
            if (org.apache.velocity.app.Velocity.evaluate(new org.apache.velocity.VelocityContext(context), stringWriter, "Active Directory principal create template", template)) {
                java.lang.String json = stringWriter.toString();
                java.lang.reflect.Type type = new com.google.common.reflect.TypeToken<java.util.Map<java.lang.String, java.lang.Object>>() {}.getType();
                data = gson.fromJson(json, type);
            }
        } catch (org.apache.velocity.exception.ParseErrorException e) {
            org.apache.ambari.server.serveraction.kerberos.ADKerberosOperationHandler.LOG.warn("Failed to parse Active Directory create principal template", e);
            throw new org.apache.ambari.server.serveraction.kerberos.KerberosOperationException("Failed to parse Active Directory create principal template", e);
        } catch (org.apache.velocity.exception.MethodInvocationException | org.apache.velocity.exception.ResourceNotFoundException e) {
            org.apache.ambari.server.serveraction.kerberos.ADKerberosOperationHandler.LOG.warn("Failed to process Active Directory create principal template", e);
            throw new org.apache.ambari.server.serveraction.kerberos.KerberosOperationException("Failed to process Active Directory create principal template", e);
        }
        return data;
    }

    private java.lang.String findPrincipalDN(java.lang.String normalizedPrincipal) throws javax.naming.NamingException, org.apache.ambari.server.serveraction.kerberos.KerberosOperationException {
        java.lang.String dn = null;
        if (normalizedPrincipal != null) {
            javax.naming.NamingEnumeration<javax.naming.directory.SearchResult> results = null;
            try {
                results = ldapContext.search(principalContainerLdapName, java.lang.String.format("(userPrincipalName=%s)", normalizedPrincipal), searchControls);
                if ((results != null) && results.hasMore()) {
                    javax.naming.directory.SearchResult result = results.next();
                    dn = result.getNameInNamespace();
                }
            } finally {
                try {
                    if (results != null) {
                        results.close();
                    }
                } catch (javax.naming.NamingException ne) {
                }
            }
        }
        return dn;
    }
}