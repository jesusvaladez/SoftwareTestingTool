package org.apache.ambari.server.serveraction.kerberos;
import org.easymock.Capture;
import org.easymock.CaptureType;
import org.easymock.IAnswer;
import static org.easymock.EasyMock.anyObject;
import static org.easymock.EasyMock.anyString;
import static org.easymock.EasyMock.capture;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.isNull;
import static org.easymock.EasyMock.newCapture;
import static org.easymock.EasyMock.replay;
public class ADKerberosOperationHandlerTest extends org.apache.ambari.server.serveraction.kerberos.KerberosOperationHandlerTest {
    private static final java.lang.String DEFAULT_ADMIN_PRINCIPAL = "cluser_admin@HDP01.LOCAL";

    private static final java.lang.String DEFAULT_ADMIN_PASSWORD = "Hadoop12345";

    private static final java.lang.String DEFAULT_LDAP_URL = "ldaps://10.0.100.4";

    private static final java.lang.String DEFAULT_PRINCIPAL_CONTAINER_DN = "ou=HDP,DC=HDP01,DC=LOCAL";

    private static final java.lang.String DEFAULT_REALM = "HDP01.LOCAL";

    private static final java.util.Map<java.lang.String, java.lang.String> KERBEROS_ENV_MAP;

    static {
        java.util.Map<java.lang.String, java.lang.String> map = new java.util.HashMap<>(org.apache.ambari.server.serveraction.kerberos.KerberosOperationHandlerTest.DEFAULT_KERBEROS_ENV_MAP);
        map.put(org.apache.ambari.server.serveraction.kerberos.ADKerberosOperationHandler.KERBEROS_ENV_PRINCIPAL_CONTAINER_DN, DEFAULT_PRINCIPAL_CONTAINER_DN);
        map.put(org.apache.ambari.server.serveraction.kerberos.ADKerberosOperationHandler.KERBEROS_ENV_LDAP_URL, DEFAULT_LDAP_URL);
        KERBEROS_ENV_MAP = java.util.Collections.unmodifiableMap(map);
    }

    private static java.lang.reflect.Method methodCreateInitialLdapContext;

    private com.google.inject.Injector injector;

    private javax.naming.ldap.LdapContext ldapContext;

    @org.junit.BeforeClass
    public static void beforeMITKerberosOperationHandlerTest() throws java.lang.Exception {
        org.apache.ambari.server.serveraction.kerberos.ADKerberosOperationHandlerTest.methodCreateInitialLdapContext = org.apache.ambari.server.serveraction.kerberos.ADKerberosOperationHandler.class.getDeclaredMethod("createInitialLdapContext", java.util.Properties.class, javax.naming.ldap.Control[].class);
    }

    @org.junit.Before
    public void setup() {
        injector = com.google.inject.Guice.createInjector(new com.google.inject.AbstractModule() {
            @java.lang.Override
            protected void configure() {
                bind(org.apache.ambari.server.state.Clusters.class).toInstance(createNiceMock(org.apache.ambari.server.state.Clusters.class));
                bind(org.apache.ambari.server.configuration.Configuration.class).toInstance(createNiceMock(org.apache.ambari.server.configuration.Configuration.class));
                bind(org.apache.ambari.server.state.stack.OsFamily.class).toInstance(createNiceMock(org.apache.ambari.server.state.stack.OsFamily.class));
            }
        });
        ldapContext = createMock(javax.naming.ldap.LdapContext.class);
    }

    @org.junit.Test(expected = org.apache.ambari.server.serveraction.kerberos.KerberosKDCConnectionException.class)
    public void testOpenExceptionLdapUrlNotProvided() throws java.lang.Exception {
        org.apache.ambari.server.serveraction.kerberos.KerberosOperationHandler handler = new org.apache.ambari.server.serveraction.kerberos.ADKerberosOperationHandler();
        org.apache.ambari.server.security.credential.PrincipalKeyCredential kc = new org.apache.ambari.server.security.credential.PrincipalKeyCredential(org.apache.ambari.server.serveraction.kerberos.ADKerberosOperationHandlerTest.DEFAULT_ADMIN_PRINCIPAL, org.apache.ambari.server.serveraction.kerberos.ADKerberosOperationHandlerTest.DEFAULT_ADMIN_PASSWORD);
        java.util.Map<java.lang.String, java.lang.String> kerberosEnvMap = new java.util.HashMap<java.lang.String, java.lang.String>() {
            {
                put(org.apache.ambari.server.serveraction.kerberos.ADKerberosOperationHandler.KERBEROS_ENV_PRINCIPAL_CONTAINER_DN, org.apache.ambari.server.serveraction.kerberos.ADKerberosOperationHandlerTest.DEFAULT_PRINCIPAL_CONTAINER_DN);
            }
        };
        handler.open(kc, org.apache.ambari.server.serveraction.kerberos.ADKerberosOperationHandlerTest.DEFAULT_REALM, kerberosEnvMap);
        handler.close();
    }

    @org.junit.Test(expected = org.apache.ambari.server.serveraction.kerberos.KerberosLDAPContainerException.class)
    public void testOpenExceptionPrincipalContainerDnNotProvided() throws java.lang.Exception {
        org.apache.ambari.server.serveraction.kerberos.KerberosOperationHandler handler = new org.apache.ambari.server.serveraction.kerberos.ADKerberosOperationHandler();
        org.apache.ambari.server.security.credential.PrincipalKeyCredential kc = new org.apache.ambari.server.security.credential.PrincipalKeyCredential(org.apache.ambari.server.serveraction.kerberos.ADKerberosOperationHandlerTest.DEFAULT_ADMIN_PRINCIPAL, org.apache.ambari.server.serveraction.kerberos.ADKerberosOperationHandlerTest.DEFAULT_ADMIN_PASSWORD);
        java.util.Map<java.lang.String, java.lang.String> kerberosEnvMap = new java.util.HashMap<java.lang.String, java.lang.String>() {
            {
                put(org.apache.ambari.server.serveraction.kerberos.ADKerberosOperationHandler.KERBEROS_ENV_LDAP_URL, org.apache.ambari.server.serveraction.kerberos.ADKerberosOperationHandlerTest.DEFAULT_LDAP_URL);
            }
        };
        handler.open(kc, org.apache.ambari.server.serveraction.kerberos.ADKerberosOperationHandlerTest.DEFAULT_REALM, kerberosEnvMap);
        handler.close();
    }

    @org.junit.Test(expected = org.apache.ambari.server.serveraction.kerberos.KerberosAdminAuthenticationException.class)
    public void testOpenExceptionAdminCredentialsNotProvided() throws java.lang.Exception {
        org.apache.ambari.server.serveraction.kerberos.KerberosOperationHandler handler = new org.apache.ambari.server.serveraction.kerberos.ADKerberosOperationHandler();
        handler.open(null, org.apache.ambari.server.serveraction.kerberos.ADKerberosOperationHandlerTest.DEFAULT_REALM, getKerberosEnv());
        handler.close();
    }

    @org.junit.Test(expected = org.apache.ambari.server.serveraction.kerberos.KerberosKDCConnectionException.class)
    public void testOpenExceptionNoLdaps() throws java.lang.Exception {
        org.apache.ambari.server.security.credential.PrincipalKeyCredential kc = new org.apache.ambari.server.security.credential.PrincipalKeyCredential(org.apache.ambari.server.serveraction.kerberos.ADKerberosOperationHandlerTest.DEFAULT_ADMIN_PRINCIPAL, "hello");
        org.apache.ambari.server.serveraction.kerberos.KerberosOperationHandler handler = new org.apache.ambari.server.serveraction.kerberos.ADKerberosOperationHandler();
        java.util.Map<java.lang.String, java.lang.String> kerberosEnvMap = new java.util.HashMap<java.lang.String, java.lang.String>() {
            {
                put(org.apache.ambari.server.serveraction.kerberos.ADKerberosOperationHandler.KERBEROS_ENV_LDAP_URL, "ldap://this_wont_work");
                put(org.apache.ambari.server.serveraction.kerberos.ADKerberosOperationHandler.KERBEROS_ENV_PRINCIPAL_CONTAINER_DN, org.apache.ambari.server.serveraction.kerberos.ADKerberosOperationHandlerTest.DEFAULT_PRINCIPAL_CONTAINER_DN);
            }
        };
        handler.open(kc, org.apache.ambari.server.serveraction.kerberos.ADKerberosOperationHandlerTest.DEFAULT_REALM, kerberosEnvMap);
        handler.close();
    }

    @org.junit.Test(expected = org.apache.ambari.server.serveraction.kerberos.KerberosAdminAuthenticationException.class)
    public void testTestAdministratorCredentialsIncorrectAdminPassword() throws java.lang.Exception {
        com.google.inject.Injector injector = getInjector();
        org.apache.ambari.server.security.credential.PrincipalKeyCredential kc = new org.apache.ambari.server.security.credential.PrincipalKeyCredential(org.apache.ambari.server.serveraction.kerberos.ADKerberosOperationHandlerTest.DEFAULT_ADMIN_PRINCIPAL, "wrong");
        org.apache.ambari.server.serveraction.kerberos.ADKerberosOperationHandler handler = createMockBuilder(org.apache.ambari.server.serveraction.kerberos.ADKerberosOperationHandler.class).addMockedMethod(org.apache.ambari.server.serveraction.kerberos.ADKerberosOperationHandler.class.getDeclaredMethod("createInitialLdapContext", java.util.Properties.class, javax.naming.ldap.Control[].class)).createNiceMock();
        injector.injectMembers(handler);
        EasyMock.expect(handler.createInitialLdapContext(EasyMock.anyObject(java.util.Properties.class), EasyMock.anyObject(javax.naming.ldap.Control[].class))).andAnswer(new org.easymock.IAnswer<javax.naming.ldap.LdapContext>() {
            @java.lang.Override
            public javax.naming.ldap.LdapContext answer() throws java.lang.Throwable {
                throw new javax.naming.AuthenticationException();
            }
        }).once();
        replayAll();
        handler.open(kc, org.apache.ambari.server.serveraction.kerberos.ADKerberosOperationHandlerTest.DEFAULT_REALM, getKerberosEnv());
        handler.testAdministratorCredentials();
        handler.close();
    }

    @org.junit.Test(expected = org.apache.ambari.server.serveraction.kerberos.KerberosAdminAuthenticationException.class)
    public void testTestAdministratorCredentialsIncorrectAdminPrincipal() throws java.lang.Exception {
        com.google.inject.Injector injector = getInjector();
        org.apache.ambari.server.security.credential.PrincipalKeyCredential kc = new org.apache.ambari.server.security.credential.PrincipalKeyCredential("wrong", org.apache.ambari.server.serveraction.kerberos.ADKerberosOperationHandlerTest.DEFAULT_ADMIN_PASSWORD);
        org.apache.ambari.server.serveraction.kerberos.ADKerberosOperationHandler handler = createMockBuilder(org.apache.ambari.server.serveraction.kerberos.ADKerberosOperationHandler.class).addMockedMethod(org.apache.ambari.server.serveraction.kerberos.ADKerberosOperationHandler.class.getDeclaredMethod("createInitialLdapContext", java.util.Properties.class, javax.naming.ldap.Control[].class)).createNiceMock();
        injector.injectMembers(handler);
        EasyMock.expect(handler.createInitialLdapContext(EasyMock.anyObject(java.util.Properties.class), EasyMock.anyObject(javax.naming.ldap.Control[].class))).andAnswer(new org.easymock.IAnswer<javax.naming.ldap.LdapContext>() {
            @java.lang.Override
            public javax.naming.ldap.LdapContext answer() throws java.lang.Throwable {
                throw new javax.naming.AuthenticationException();
            }
        }).once();
        replayAll();
        handler.open(kc, org.apache.ambari.server.serveraction.kerberos.ADKerberosOperationHandlerTest.DEFAULT_REALM, getKerberosEnv());
        handler.testAdministratorCredentials();
        handler.close();
    }

    @org.junit.Test(expected = org.apache.ambari.server.serveraction.kerberos.KerberosKDCConnectionException.class)
    public void testTestAdministratorCredentialsKDCConnectionException() throws java.lang.Exception {
        org.apache.ambari.server.security.credential.PrincipalKeyCredential kc = new org.apache.ambari.server.security.credential.PrincipalKeyCredential(org.apache.ambari.server.serveraction.kerberos.ADKerberosOperationHandlerTest.DEFAULT_ADMIN_PRINCIPAL, org.apache.ambari.server.serveraction.kerberos.ADKerberosOperationHandlerTest.DEFAULT_ADMIN_PASSWORD);
        org.apache.ambari.server.serveraction.kerberos.ADKerberosOperationHandler handler = createMockedHandler(org.apache.ambari.server.serveraction.kerberos.ADKerberosOperationHandlerTest.methodCreateInitialLdapContext);
        EasyMock.expect(handler.createInitialLdapContext(EasyMock.anyObject(java.util.Properties.class), EasyMock.anyObject(javax.naming.ldap.Control[].class))).andAnswer(new org.easymock.IAnswer<javax.naming.ldap.LdapContext>() {
            @java.lang.Override
            public javax.naming.ldap.LdapContext answer() throws java.lang.Throwable {
                throw new javax.naming.CommunicationException();
            }
        }).once();
        replayAll();
        handler.open(kc, org.apache.ambari.server.serveraction.kerberos.ADKerberosOperationHandlerTest.DEFAULT_REALM, getKerberosEnv());
        handler.testAdministratorCredentials();
        handler.close();
    }

    @org.junit.Test
    public void testTestAdministratorCredentialsSuccess() throws java.lang.Exception {
        org.apache.ambari.server.security.credential.PrincipalKeyCredential kc = new org.apache.ambari.server.security.credential.PrincipalKeyCredential(org.apache.ambari.server.serveraction.kerberos.ADKerberosOperationHandlerTest.DEFAULT_ADMIN_PRINCIPAL, org.apache.ambari.server.serveraction.kerberos.ADKerberosOperationHandlerTest.DEFAULT_ADMIN_PASSWORD);
        org.apache.ambari.server.serveraction.kerberos.ADKerberosOperationHandler handler = createMockedHandler(org.apache.ambari.server.serveraction.kerberos.ADKerberosOperationHandlerTest.methodCreateInitialLdapContext);
        EasyMock.expect(handler.createInitialLdapContext(EasyMock.anyObject(java.util.Properties.class), EasyMock.anyObject(javax.naming.ldap.Control[].class))).andAnswer(new org.easymock.IAnswer<javax.naming.ldap.LdapContext>() {
            @java.lang.Override
            public javax.naming.ldap.LdapContext answer() throws java.lang.Throwable {
                javax.naming.ldap.LdapContext ldapContext = createNiceMock(javax.naming.ldap.LdapContext.class);
                EasyMock.expect(ldapContext.search(EasyMock.anyObject(java.lang.String.class), EasyMock.anyObject(java.lang.String.class), EasyMock.anyObject(javax.naming.directory.SearchControls.class))).andAnswer(new org.easymock.IAnswer<javax.naming.NamingEnumeration<javax.naming.directory.SearchResult>>() {
                    @java.lang.Override
                    public javax.naming.NamingEnumeration<javax.naming.directory.SearchResult> answer() throws java.lang.Throwable {
                        @java.lang.SuppressWarnings("unchecked")
                        javax.naming.NamingEnumeration<javax.naming.directory.SearchResult> result = createNiceMock(javax.naming.NamingEnumeration.class);
                        EasyMock.expect(result.hasMore()).andReturn(false).once();
                        EasyMock.replay(result);
                        return result;
                    }
                }).once();
                return ldapContext;
            }
        }).once();
        replayAll();
        handler.open(kc, org.apache.ambari.server.serveraction.kerberos.ADKerberosOperationHandlerTest.DEFAULT_REALM, getKerberosEnv());
        handler.testAdministratorCredentials();
        handler.close();
    }

    @org.junit.Test
    public void testProcessCreateTemplateDefault() throws java.lang.Exception {
        com.google.inject.Injector injector = getInjector();
        org.apache.ambari.server.security.credential.PrincipalKeyCredential kc = new org.apache.ambari.server.security.credential.PrincipalKeyCredential(org.apache.ambari.server.serveraction.kerberos.ADKerberosOperationHandlerTest.DEFAULT_ADMIN_PRINCIPAL, org.apache.ambari.server.serveraction.kerberos.ADKerberosOperationHandlerTest.DEFAULT_ADMIN_PASSWORD);
        org.easymock.Capture<javax.naming.Name> capturedName = EasyMock.newCapture(CaptureType.ALL);
        org.easymock.Capture<javax.naming.directory.Attributes> capturedAttributes = EasyMock.newCapture(CaptureType.ALL);
        org.apache.ambari.server.serveraction.kerberos.ADKerberosOperationHandler handler = createMockBuilder(org.apache.ambari.server.serveraction.kerberos.ADKerberosOperationHandler.class).addMockedMethod(org.apache.ambari.server.serveraction.kerberos.ADKerberosOperationHandler.class.getDeclaredMethod("createInitialLdapContext", java.util.Properties.class, javax.naming.ldap.Control[].class)).addMockedMethod(org.apache.ambari.server.serveraction.kerberos.ADKerberosOperationHandler.class.getDeclaredMethod("createSearchControls")).createNiceMock();
        injector.injectMembers(handler);
        @java.lang.SuppressWarnings("unchecked")
        javax.naming.NamingEnumeration<javax.naming.directory.SearchResult> searchResult = createNiceMock(javax.naming.NamingEnumeration.class);
        EasyMock.expect(searchResult.hasMore()).andReturn(false).once();
        javax.naming.ldap.LdapContext ldapContext = createNiceMock(javax.naming.ldap.LdapContext.class);
        EasyMock.expect(ldapContext.search(EasyMock.anyObject(java.lang.String.class), EasyMock.anyObject(java.lang.String.class), EasyMock.anyObject(javax.naming.directory.SearchControls.class))).andReturn(searchResult).once();
        EasyMock.expect(ldapContext.createSubcontext(EasyMock.capture(capturedName), EasyMock.capture(capturedAttributes))).andReturn(createNiceMock(javax.naming.directory.DirContext.class)).anyTimes();
        EasyMock.expect(handler.createInitialLdapContext(EasyMock.anyObject(java.util.Properties.class), EasyMock.anyObject(javax.naming.ldap.Control[].class))).andReturn(ldapContext).once();
        EasyMock.expect(handler.createSearchControls()).andAnswer(new org.easymock.IAnswer<javax.naming.directory.SearchControls>() {
            @java.lang.Override
            public javax.naming.directory.SearchControls answer() throws java.lang.Throwable {
                javax.naming.directory.SearchControls searchControls = createNiceMock(javax.naming.directory.SearchControls.class);
                EasyMock.replay(searchControls);
                return searchControls;
            }
        }).once();
        replayAll();
        handler.open(kc, org.apache.ambari.server.serveraction.kerberos.ADKerberosOperationHandlerTest.DEFAULT_REALM, getKerberosEnv());
        handler.createPrincipal("nn/c6501.ambari.apache.org", "secret", true);
        handler.createPrincipal("hdfs@" + org.apache.ambari.server.serveraction.kerberos.ADKerberosOperationHandlerTest.DEFAULT_REALM, "secret", false);
        handler.close();
        java.util.List<javax.naming.directory.Attributes> attributesList = capturedAttributes.getValues();
        javax.naming.directory.Attributes attributes;
        attributes = attributesList.get(0);
        java.lang.String[] objectClasses = new java.lang.String[]{ "top", "person", "organizationalPerson", "user" };
        junit.framework.Assert.assertNotNull(attributes);
        junit.framework.Assert.assertEquals(7, attributes.size());
        junit.framework.Assert.assertNotNull(attributes.get("objectClass"));
        junit.framework.Assert.assertEquals(objectClasses.length, attributes.get("objectClass").size());
        for (int i = 0; i < objectClasses.length; i++) {
            junit.framework.Assert.assertEquals(objectClasses[i], attributes.get("objectClass").get(i));
        }
        junit.framework.Assert.assertNotNull(attributes.get("cn"));
        junit.framework.Assert.assertEquals("nn/c6501.ambari.apache.org", attributes.get("cn").get());
        junit.framework.Assert.assertNotNull(attributes.get("servicePrincipalName"));
        junit.framework.Assert.assertEquals("nn/c6501.ambari.apache.org", attributes.get("servicePrincipalName").get());
        junit.framework.Assert.assertNotNull(attributes.get("userPrincipalName"));
        junit.framework.Assert.assertEquals("nn/c6501.ambari.apache.org@HDP01.LOCAL", attributes.get("userPrincipalName").get());
        junit.framework.Assert.assertNotNull(attributes.get("unicodePwd"));
        junit.framework.Assert.assertEquals("\"secret\"", new java.lang.String(((byte[]) (attributes.get("unicodePwd").get())), java.nio.charset.Charset.forName("UTF-16LE")));
        junit.framework.Assert.assertNotNull(attributes.get("accountExpires"));
        junit.framework.Assert.assertEquals("0", attributes.get("accountExpires").get());
        junit.framework.Assert.assertNotNull(attributes.get("userAccountControl"));
        junit.framework.Assert.assertEquals("66048", attributes.get("userAccountControl").get());
        attributes = attributesList.get(1);
        junit.framework.Assert.assertNotNull(attributes);
        junit.framework.Assert.assertEquals(6, attributes.size());
        junit.framework.Assert.assertNotNull(attributes.get("objectClass"));
        junit.framework.Assert.assertEquals(objectClasses.length, attributes.get("objectClass").size());
        for (int i = 0; i < objectClasses.length; i++) {
            junit.framework.Assert.assertEquals(objectClasses[i], attributes.get("objectClass").get(i));
        }
        junit.framework.Assert.assertNotNull(attributes.get("cn"));
        junit.framework.Assert.assertEquals("hdfs", attributes.get("cn").get());
        junit.framework.Assert.assertNotNull(attributes.get("userPrincipalName"));
        junit.framework.Assert.assertEquals("hdfs@HDP01.LOCAL", attributes.get("userPrincipalName").get());
        junit.framework.Assert.assertNotNull(attributes.get("unicodePwd"));
        junit.framework.Assert.assertEquals("\"secret\"", new java.lang.String(((byte[]) (attributes.get("unicodePwd").get())), java.nio.charset.Charset.forName("UTF-16LE")));
        junit.framework.Assert.assertNotNull(attributes.get("accountExpires"));
        junit.framework.Assert.assertEquals("0", attributes.get("accountExpires").get());
        junit.framework.Assert.assertNotNull(attributes.get("userAccountControl"));
        junit.framework.Assert.assertEquals("66048", attributes.get("userAccountControl").get());
    }

    @org.junit.Test
    public void testProcessCreateTemplateCustom() throws java.lang.Exception {
        com.google.inject.Injector injector = getInjector();
        org.apache.ambari.server.security.credential.PrincipalKeyCredential kc = new org.apache.ambari.server.security.credential.PrincipalKeyCredential(org.apache.ambari.server.serveraction.kerberos.ADKerberosOperationHandlerTest.DEFAULT_ADMIN_PRINCIPAL, org.apache.ambari.server.serveraction.kerberos.ADKerberosOperationHandlerTest.DEFAULT_ADMIN_PASSWORD);
        java.util.Map<java.lang.String, java.lang.String> kerberosEnvMap = new java.util.HashMap<>(getKerberosEnv());
        kerberosEnvMap.put(org.apache.ambari.server.serveraction.kerberos.ADKerberosOperationHandler.KERBEROS_ENV_AD_CREATE_ATTRIBUTES_TEMPLATE, "" + ((((((((((((((((("#set( $user = \"${principal_primary}-${principal_digest}\" )" + "{") + "  \"objectClass\": [") + "    \"top\",") + "    \"person\",") + "    \"organizationalPerson\",") + "    \"user\"") + "  ],") + "  \"cn\": \"$user\",") + "  \"sAMAccountName\": \"$user.substring(0,20)\",") + "  #if( $is_service )") + "  \"servicePrincipalName\": \"$principal_name\",") + "  #end") + "  \"userPrincipalName\": \"$normalized_principal\",") + "  \"unicodePwd\": \"$password\",") + "  \"accountExpires\": \"0\",") + "  \"userAccountControl\": \"66048\"") + "}"));
        org.easymock.Capture<javax.naming.Name> capturedName = EasyMock.newCapture();
        org.easymock.Capture<javax.naming.directory.Attributes> capturedAttributes = EasyMock.newCapture();
        org.apache.ambari.server.serveraction.kerberos.ADKerberosOperationHandler handler = createMockBuilder(org.apache.ambari.server.serveraction.kerberos.ADKerberosOperationHandler.class).addMockedMethod(org.apache.ambari.server.serveraction.kerberos.ADKerberosOperationHandler.class.getDeclaredMethod("createInitialLdapContext", java.util.Properties.class, javax.naming.ldap.Control[].class)).addMockedMethod(org.apache.ambari.server.serveraction.kerberos.ADKerberosOperationHandler.class.getDeclaredMethod("createSearchControls")).createNiceMock();
        injector.injectMembers(handler);
        @java.lang.SuppressWarnings("unchecked")
        javax.naming.NamingEnumeration<javax.naming.directory.SearchResult> searchResult = createNiceMock(javax.naming.NamingEnumeration.class);
        EasyMock.expect(searchResult.hasMore()).andReturn(false).once();
        javax.naming.ldap.LdapContext ldapContext = createNiceMock(javax.naming.ldap.LdapContext.class);
        EasyMock.expect(ldapContext.search(EasyMock.anyObject(java.lang.String.class), EasyMock.anyObject(java.lang.String.class), EasyMock.anyObject(javax.naming.directory.SearchControls.class))).andReturn(searchResult).once();
        EasyMock.expect(ldapContext.createSubcontext(EasyMock.capture(capturedName), EasyMock.capture(capturedAttributes))).andReturn(createNiceMock(javax.naming.directory.DirContext.class)).once();
        EasyMock.expect(handler.createInitialLdapContext(EasyMock.anyObject(java.util.Properties.class), EasyMock.anyObject(javax.naming.ldap.Control[].class))).andReturn(ldapContext).once();
        EasyMock.expect(handler.createSearchControls()).andAnswer(new org.easymock.IAnswer<javax.naming.directory.SearchControls>() {
            @java.lang.Override
            public javax.naming.directory.SearchControls answer() throws java.lang.Throwable {
                javax.naming.directory.SearchControls searchControls = createNiceMock(javax.naming.directory.SearchControls.class);
                EasyMock.replay(searchControls);
                return searchControls;
            }
        }).once();
        replayAll();
        handler.open(kc, org.apache.ambari.server.serveraction.kerberos.ADKerberosOperationHandlerTest.DEFAULT_REALM, kerberosEnvMap);
        handler.createPrincipal("nn/c6501.ambari.apache.org", "secret", true);
        handler.close();
        javax.naming.directory.Attributes attributes = capturedAttributes.getValue();
        java.lang.String[] objectClasses = new java.lang.String[]{ "top", "person", "organizationalPerson", "user" };
        junit.framework.Assert.assertNotNull(attributes);
        junit.framework.Assert.assertEquals(8, attributes.size());
        junit.framework.Assert.assertNotNull(attributes.get("objectClass"));
        junit.framework.Assert.assertEquals(objectClasses.length, attributes.get("objectClass").size());
        for (int i = 0; i < objectClasses.length; i++) {
            junit.framework.Assert.assertEquals(objectClasses[i], attributes.get("objectClass").get(i));
        }
        junit.framework.Assert.assertNotNull(attributes.get("cn"));
        junit.framework.Assert.assertEquals("nn-995e1580db28198e7fda1417ab5d894c877937d2", attributes.get("cn").get());
        junit.framework.Assert.assertNotNull(attributes.get("servicePrincipalName"));
        junit.framework.Assert.assertEquals("nn/c6501.ambari.apache.org", attributes.get("servicePrincipalName").get());
        junit.framework.Assert.assertNotNull(attributes.get("userPrincipalName"));
        junit.framework.Assert.assertEquals("nn/c6501.ambari.apache.org@HDP01.LOCAL", attributes.get("userPrincipalName").get());
        junit.framework.Assert.assertNotNull(attributes.get("sAMAccountName"));
        junit.framework.Assert.assertTrue(attributes.get("sAMAccountName").get().toString().length() <= 20);
        junit.framework.Assert.assertEquals("nn-995e1580db28198e7", attributes.get("sAMAccountName").get());
        junit.framework.Assert.assertNotNull(attributes.get("unicodePwd"));
        junit.framework.Assert.assertEquals("\"secret\"", new java.lang.String(((byte[]) (attributes.get("unicodePwd").get())), java.nio.charset.Charset.forName("UTF-16LE")));
        junit.framework.Assert.assertNotNull(attributes.get("accountExpires"));
        junit.framework.Assert.assertEquals("0", attributes.get("accountExpires").get());
        junit.framework.Assert.assertNotNull(attributes.get("userAccountControl"));
        junit.framework.Assert.assertEquals("66048", attributes.get("userAccountControl").get());
    }

    @org.junit.Test
    public void testDigests() throws java.lang.Exception {
        com.google.inject.Injector injector = getInjector();
        org.apache.ambari.server.security.credential.PrincipalKeyCredential kc = new org.apache.ambari.server.security.credential.PrincipalKeyCredential(org.apache.ambari.server.serveraction.kerberos.ADKerberosOperationHandlerTest.DEFAULT_ADMIN_PRINCIPAL, org.apache.ambari.server.serveraction.kerberos.ADKerberosOperationHandlerTest.DEFAULT_ADMIN_PASSWORD);
        java.util.Map<java.lang.String, java.lang.String> kerberosEnvMap = new java.util.HashMap<>(getKerberosEnv());
        kerberosEnvMap.put(org.apache.ambari.server.serveraction.kerberos.ADKerberosOperationHandler.KERBEROS_ENV_AD_CREATE_ATTRIBUTES_TEMPLATE, "" + (((("{" + "\"principal_digest\": \"$principal_digest\",") + "\"principal_digest_256\": \"$principal_digest_256\",") + "\"principal_digest_512\": \"$principal_digest_512\"") + "}"));
        org.easymock.Capture<javax.naming.directory.Attributes> capturedAttributes = EasyMock.newCapture();
        org.apache.ambari.server.serveraction.kerberos.ADKerberosOperationHandler handler = createMockBuilder(org.apache.ambari.server.serveraction.kerberos.ADKerberosOperationHandler.class).addMockedMethod(org.apache.ambari.server.serveraction.kerberos.ADKerberosOperationHandler.class.getDeclaredMethod("createInitialLdapContext", java.util.Properties.class, javax.naming.ldap.Control[].class)).addMockedMethod(org.apache.ambari.server.serveraction.kerberos.ADKerberosOperationHandler.class.getDeclaredMethod("createSearchControls")).createNiceMock();
        injector.injectMembers(handler);
        @java.lang.SuppressWarnings("unchecked")
        javax.naming.NamingEnumeration<javax.naming.directory.SearchResult> searchResult = createNiceMock(javax.naming.NamingEnumeration.class);
        EasyMock.expect(searchResult.hasMore()).andReturn(false).once();
        javax.naming.ldap.LdapContext ldapContext = createNiceMock(javax.naming.ldap.LdapContext.class);
        EasyMock.expect(ldapContext.search(EasyMock.anyObject(java.lang.String.class), EasyMock.anyObject(java.lang.String.class), EasyMock.anyObject(javax.naming.directory.SearchControls.class))).andReturn(searchResult).once();
        EasyMock.expect(ldapContext.createSubcontext(EasyMock.anyObject(javax.naming.Name.class), EasyMock.capture(capturedAttributes))).andReturn(createNiceMock(javax.naming.directory.DirContext.class)).once();
        EasyMock.expect(handler.createInitialLdapContext(EasyMock.anyObject(java.util.Properties.class), EasyMock.anyObject(javax.naming.ldap.Control[].class))).andReturn(ldapContext).once();
        EasyMock.expect(handler.createSearchControls()).andAnswer(new org.easymock.IAnswer<javax.naming.directory.SearchControls>() {
            @java.lang.Override
            public javax.naming.directory.SearchControls answer() throws java.lang.Throwable {
                javax.naming.directory.SearchControls searchControls = createNiceMock(javax.naming.directory.SearchControls.class);
                EasyMock.replay(searchControls);
                return searchControls;
            }
        }).once();
        replayAll();
        handler.open(kc, org.apache.ambari.server.serveraction.kerberos.ADKerberosOperationHandlerTest.DEFAULT_REALM, kerberosEnvMap);
        handler.createPrincipal("nn/c6501.ambari.apache.org", "secret", true);
        handler.close();
        javax.naming.directory.Attributes attributes = capturedAttributes.getValue();
        junit.framework.Assert.assertNotNull(attributes);
        junit.framework.Assert.assertEquals("995e1580db28198e7fda1417ab5d894c877937d2", attributes.get("principal_digest").get());
        junit.framework.Assert.assertEquals("b65bc066d11ac8b1beb31dc84035d9c204736f823decf8dfedda05a30e4ae410", attributes.get("principal_digest_256").get());
        junit.framework.Assert.assertEquals("f48de28bc0467d764f5b04dbf04d35ff329a80277614be35eda0d0deed7f1c074cc5b0e0dc361130fdb078e09eb0ca545b9c653388192508ef382af89bd3a80c", attributes.get("principal_digest_512").get());
    }

    @org.junit.Test
    @org.junit.Ignore
    public void testLive() throws java.lang.Throwable {
        org.apache.ambari.server.serveraction.kerberos.ADKerberosOperationHandler handler = new org.apache.ambari.server.serveraction.kerberos.ADKerberosOperationHandler();
        java.lang.String principal = java.lang.System.getProperty("principal");
        java.lang.String password = java.lang.System.getProperty("password");
        java.lang.String realm = java.lang.System.getProperty("realm");
        java.lang.String ldapUrl = java.lang.System.getProperty("ldap_url");
        java.lang.String containerDN = java.lang.System.getProperty("container_dn");
        if (principal == null) {
            principal = org.apache.ambari.server.serveraction.kerberos.ADKerberosOperationHandlerTest.DEFAULT_ADMIN_PRINCIPAL;
        }
        if (password == null) {
            password = org.apache.ambari.server.serveraction.kerberos.ADKerberosOperationHandlerTest.DEFAULT_ADMIN_PASSWORD;
        }
        if (realm == null) {
            realm = org.apache.ambari.server.serveraction.kerberos.ADKerberosOperationHandlerTest.DEFAULT_REALM;
        }
        if (ldapUrl == null) {
            ldapUrl = org.apache.ambari.server.serveraction.kerberos.ADKerberosOperationHandlerTest.DEFAULT_LDAP_URL;
        }
        if (containerDN == null) {
            containerDN = org.apache.ambari.server.serveraction.kerberos.ADKerberosOperationHandlerTest.DEFAULT_PRINCIPAL_CONTAINER_DN;
        }
        org.apache.ambari.server.security.credential.PrincipalKeyCredential credentials = new org.apache.ambari.server.security.credential.PrincipalKeyCredential(principal, password);
        java.util.Map<java.lang.String, java.lang.String> kerberosEnvMap = new java.util.HashMap<>();
        kerberosEnvMap.put(org.apache.ambari.server.serveraction.kerberos.ADKerberosOperationHandler.KERBEROS_ENV_LDAP_URL, ldapUrl);
        kerberosEnvMap.put(org.apache.ambari.server.serveraction.kerberos.ADKerberosOperationHandler.KERBEROS_ENV_PRINCIPAL_CONTAINER_DN, containerDN);
        handler.open(credentials, realm, kerberosEnvMap);
        java.lang.System.out.println("Test Admin Credentials: " + handler.testAdministratorCredentials());
        java.lang.System.out.println("Principal exists: " + handler.principalExists("nn/c1508.ambari.apache.org", true));
        handler.close();
        handler.open(credentials, realm, kerberosEnvMap);
        java.lang.String evaluatedPrincipal;
        evaluatedPrincipal = "nn/c6501.ambari.apache.org@" + org.apache.ambari.server.serveraction.kerberos.ADKerberosOperationHandlerTest.DEFAULT_REALM;
        if (handler.principalExists(evaluatedPrincipal, true)) {
            handler.setPrincipalPassword(evaluatedPrincipal, "some password", true);
        } else {
            handler.createPrincipal(evaluatedPrincipal, "some password", true);
        }
        evaluatedPrincipal = "hdfs@" + org.apache.ambari.server.serveraction.kerberos.ADKerberosOperationHandlerTest.DEFAULT_REALM;
        if (handler.principalExists(evaluatedPrincipal, false)) {
            handler.setPrincipalPassword(evaluatedPrincipal, "some password", false);
        } else {
            handler.createPrincipal(evaluatedPrincipal, "some password", true);
        }
        kerberosEnvMap.put(org.apache.ambari.server.serveraction.kerberos.ADKerberosOperationHandler.KERBEROS_ENV_AD_CREATE_ATTRIBUTES_TEMPLATE, "#set( $user = \"${principal_primary}-${principal_digest}\" )" + (((((((((((((((("{" + "  \"objectClass\": [") + "    \"top\",") + "    \"person\",") + "    \"organizationalPerson\",") + "    \"user\"") + "  ],") + "  \"cn\": \"$user\",") + "  \"sAMAccountName\": \"$user.substring(0,20)\",") + "  #if( $is_service )") + "  \"servicePrincipalName\": \"$principal_name\",") + "  #end") + "  \"userPrincipalName\": \"$normalized_principal\",") + "  \"unicodePwd\": \"$password\",") + "  \"accountExpires\": \"0\",") + "  \"userAccountControl\": \"66048\"") + "}"));
        handler.close();
        handler.open(credentials, realm, kerberosEnvMap);
        handler.removePrincipal("abcdefg", false);
        handler.removePrincipal("abcdefg/c1509.ambari.apache.org@" + org.apache.ambari.server.serveraction.kerberos.ADKerberosOperationHandlerTest.DEFAULT_REALM, true);
        handler.createPrincipal("abcdefg/c1509.ambari.apache.org@" + org.apache.ambari.server.serveraction.kerberos.ADKerberosOperationHandlerTest.DEFAULT_REALM, "some password", true);
        handler.createPrincipal("abcdefg@" + org.apache.ambari.server.serveraction.kerberos.ADKerberosOperationHandlerTest.DEFAULT_REALM, "some password", false);
        handler.setPrincipalPassword("abcdefg/c1509.ambari.apache.org@" + org.apache.ambari.server.serveraction.kerberos.ADKerberosOperationHandlerTest.DEFAULT_REALM, "some password", true);
        handler.close();
    }

    @org.junit.Test
    public void testCreateLdapContextSSLSocketFactoryTrusting() throws java.lang.Exception {
        testCreateLdapContextSSLSocketFactory(true);
    }

    @org.junit.Test
    public void testCreateLdapContextSSLSocketFactoryNonTrusting() throws java.lang.Exception {
        testCreateLdapContextSSLSocketFactory(false);
    }

    private void testCreateLdapContextSSLSocketFactory(boolean trusting) throws java.lang.Exception {
        com.google.inject.Injector injector = getInjector();
        org.apache.ambari.server.configuration.Configuration configuration = injector.getInstance(org.apache.ambari.server.configuration.Configuration.class);
        EasyMock.expect(configuration.validateKerberosOperationSSLCertTrust()).andReturn(!trusting).once();
        javax.naming.ldap.LdapContext initialContext = createNiceMock(javax.naming.ldap.LdapContext.class);
        org.easymock.Capture<? extends java.util.Properties> capturedProperties = EasyMock.newCapture(CaptureType.FIRST);
        org.apache.ambari.server.serveraction.kerberos.ADKerberosOperationHandler handler = createMockBuilder(org.apache.ambari.server.serveraction.kerberos.ADKerberosOperationHandler.class).addMockedMethod(org.apache.ambari.server.serveraction.kerberos.ADKerberosOperationHandler.class.getDeclaredMethod("createInitialLdapContext", java.util.Properties.class, javax.naming.ldap.Control[].class)).createNiceMock();
        injector.injectMembers(handler);
        EasyMock.expect(handler.createInitialLdapContext(EasyMock.capture(capturedProperties), EasyMock.anyObject(javax.naming.ldap.Control[].class))).andReturn(initialContext).once();
        replayAll();
        handler.open(new org.apache.ambari.server.security.credential.PrincipalKeyCredential("principal", "key"), "EXAMPLE.COM", getKerberosEnv());
        java.util.Properties properties = capturedProperties.getValue();
        junit.framework.Assert.assertNotNull(properties);
        java.lang.String socketFactoryClassName = properties.getProperty("java.naming.ldap.factory.socket");
        if (trusting) {
            junit.framework.Assert.assertEquals(org.apache.ambari.server.security.InternalSSLSocketFactoryTrusting.class.getName(), socketFactoryClassName);
        } else {
            junit.framework.Assert.assertEquals(org.apache.ambari.server.security.InternalSSLSocketFactoryNonTrusting.class.getName(), socketFactoryClassName);
        }
    }

    private com.google.inject.Injector getInjector() {
        return injector;
    }

    @java.lang.Override
    protected org.apache.ambari.server.serveraction.kerberos.KerberosOperationHandler createMockedHandler() throws org.apache.ambari.server.serveraction.kerberos.KerberosOperationException {
        return createMockedHandler(org.apache.ambari.server.serveraction.kerberos.ADKerberosOperationHandlerTest.methodCreateInitialLdapContext);
    }

    private org.apache.ambari.server.serveraction.kerberos.ADKerberosOperationHandler createMockedHandler(java.lang.reflect.Method... mockedMethods) {
        org.apache.ambari.server.serveraction.kerberos.ADKerberosOperationHandler handler = createMockBuilder(org.apache.ambari.server.serveraction.kerberos.ADKerberosOperationHandler.class).addMockedMethods(mockedMethods).createMock();
        injector.injectMembers(handler);
        return handler;
    }

    @java.lang.Override
    protected void setupOpenSuccess(org.apache.ambari.server.serveraction.kerberos.KerberosOperationHandler handler) throws java.lang.Exception {
        org.apache.ambari.server.serveraction.kerberos.ADKerberosOperationHandler adHandler = ((org.apache.ambari.server.serveraction.kerberos.ADKerberosOperationHandler) (handler));
        EasyMock.expect(adHandler.createInitialLdapContext(EasyMock.anyObject(java.util.Properties.class), EasyMock.isNull())).andReturn(ldapContext).anyTimes();
    }

    @java.lang.Override
    protected void setupOpenFailure(org.apache.ambari.server.serveraction.kerberos.KerberosOperationHandler handler) throws java.lang.Exception {
        org.apache.ambari.server.serveraction.kerberos.ADKerberosOperationHandler adHandler = ((org.apache.ambari.server.serveraction.kerberos.ADKerberosOperationHandler) (handler));
        EasyMock.expect(adHandler.createInitialLdapContext(EasyMock.anyObject(java.util.Properties.class), EasyMock.isNull())).andThrow(new javax.naming.AuthenticationException("Bogus error!")).anyTimes();
    }

    @java.lang.Override
    protected void setupPrincipalAlreadyExists(org.apache.ambari.server.serveraction.kerberos.KerberosOperationHandler handler, boolean service) throws java.lang.Exception {
        setupPrincipalExists(handler, service);
    }

    @java.lang.Override
    protected void setupPrincipalDoesNotExist(org.apache.ambari.server.serveraction.kerberos.KerberosOperationHandler handler, boolean service) throws java.lang.Exception {
        javax.naming.NamingEnumeration<javax.naming.directory.SearchResult> results = createMock(javax.naming.NamingEnumeration.class);
        results.close();
        EasyMock.expectLastCall().once();
        EasyMock.expect(results.hasMore()).andReturn(false).anyTimes();
        EasyMock.expect(ldapContext.search(EasyMock.anyObject(javax.naming.Name.class), EasyMock.anyString(), EasyMock.anyObject(javax.naming.directory.SearchControls.class))).andReturn(results).anyTimes();
        ldapContext.close();
        EasyMock.expectLastCall().once();
    }

    @java.lang.Override
    protected void setupPrincipalExists(org.apache.ambari.server.serveraction.kerberos.KerberosOperationHandler handler, boolean service) throws java.lang.Exception {
        javax.naming.directory.SearchResult result = createMock(javax.naming.directory.SearchResult.class);
        EasyMock.expect(result.getNameInNamespace()).andReturn("user/service dn").anyTimes();
        javax.naming.NamingEnumeration<javax.naming.directory.SearchResult> results = createMock(javax.naming.NamingEnumeration.class);
        results.close();
        EasyMock.expectLastCall().anyTimes();
        EasyMock.expect(results.hasMore()).andReturn(true).once();
        EasyMock.expect(results.next()).andReturn(result).once();
        EasyMock.expect(results.hasMore()).andReturn(false).anyTimes();
        EasyMock.expect(ldapContext.search(EasyMock.anyObject(javax.naming.Name.class), EasyMock.anyString(), EasyMock.anyObject(javax.naming.directory.SearchControls.class))).andReturn(results).anyTimes();
        ldapContext.close();
        EasyMock.expectLastCall().once();
    }

    @java.lang.Override
    protected java.util.Map<java.lang.String, java.lang.String> getKerberosEnv() {
        return org.apache.ambari.server.serveraction.kerberos.ADKerberosOperationHandlerTest.KERBEROS_ENV_MAP;
    }
}