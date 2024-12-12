package org.apache.ambari.server.serveraction.kerberos;
import org.apache.directory.server.kerberos.shared.keytab.Keytab;
import org.apache.directory.server.kerberos.shared.keytab.KeytabEntry;
import org.apache.directory.shared.kerberos.codec.types.EncryptionType;
import org.easymock.EasyMockSupport;
public abstract class KerberosOperationHandlerTest extends org.easymock.EasyMockSupport {
    static final java.lang.String DEFAULT_ADMIN_PRINCIPAL = "admin";

    static final java.lang.String DEFAULT_ADMIN_PASSWORD = "hadoop";

    static final java.lang.String DEFAULT_REALM = "EXAMPLE.COM";

    static final org.apache.ambari.server.security.credential.PrincipalKeyCredential DEFAULT_ADMIN_CREDENTIALS = new org.apache.ambari.server.security.credential.PrincipalKeyCredential(org.apache.ambari.server.serveraction.kerberos.KerberosOperationHandlerTest.DEFAULT_ADMIN_PRINCIPAL, org.apache.ambari.server.serveraction.kerberos.KerberosOperationHandlerTest.DEFAULT_ADMIN_PASSWORD);

    static final java.util.Map<java.lang.String, java.lang.String> DEFAULT_KERBEROS_ENV_MAP;

    static {
        java.util.Map<java.lang.String, java.lang.String> map = new java.util.HashMap<>();
        map.put(org.apache.ambari.server.serveraction.kerberos.KerberosOperationHandler.KERBEROS_ENV_ENCRYPTION_TYPES, "aes des3-cbc-sha1 rc4 des-cbc-md5");
        map.put(org.apache.ambari.server.serveraction.kerberos.IPAKerberosOperationHandler.KERBEROS_ENV_KDC_HOSTS, "localhost");
        map.put(org.apache.ambari.server.serveraction.kerberos.IPAKerberosOperationHandler.KERBEROS_ENV_ADMIN_SERVER_HOST, "localhost");
        DEFAULT_KERBEROS_ENV_MAP = java.util.Collections.unmodifiableMap(map);
    }

    @org.junit.Rule
    public org.junit.rules.TemporaryFolder folder = new org.junit.rules.TemporaryFolder();

    @org.junit.Test
    public void testOpenSucceeded() throws java.lang.Exception {
        org.apache.ambari.server.serveraction.kerberos.KerberosOperationHandler handler = createMockedHandler();
        setupOpenSuccess(handler);
        replayAll();
        handler.open(getAdminCredentials(), org.apache.ambari.server.serveraction.kerberos.KerberosOperationHandlerTest.DEFAULT_REALM, getKerberosEnv());
        verifyAll();
        junit.framework.Assert.assertTrue(handler.isOpen());
    }

    @org.junit.Test
    public void testOpenFailed() throws java.lang.Exception {
        org.apache.ambari.server.serveraction.kerberos.KerberosOperationHandler handler = createMockedHandler();
        setupOpenFailure(handler);
        replayAll();
        try {
            handler.open(getAdminCredentials(), org.apache.ambari.server.serveraction.kerberos.KerberosOperationHandlerTest.DEFAULT_REALM, getKerberosEnv());
            junit.framework.Assert.fail("KerberosAdminAuthenticationException expected");
        } catch (org.apache.ambari.server.serveraction.kerberos.KerberosAdminAuthenticationException e) {
        }
        verifyAll();
        junit.framework.Assert.assertFalse(handler.isOpen());
    }

    @org.junit.Test(expected = org.apache.ambari.server.serveraction.kerberos.KerberosPrincipalAlreadyExistsException.class)
    public void testCreateUserPrincipalPrincipalAlreadyExists() throws java.lang.Exception {
        testCreatePrincipalPrincipalAlreadyExists(false);
    }

    @org.junit.Test(expected = org.apache.ambari.server.serveraction.kerberos.KerberosPrincipalAlreadyExistsException.class)
    public void testCreateServicePrincipalPrincipalAlreadyExists() throws java.lang.Exception {
        testCreatePrincipalPrincipalAlreadyExists(true);
    }

    private void testCreatePrincipalPrincipalAlreadyExists(boolean service) throws java.lang.Exception {
        org.apache.ambari.server.serveraction.kerberos.KerberosOperationHandler handler = createMockedHandler();
        setupOpenSuccess(handler);
        setupPrincipalAlreadyExists(handler, service);
        replayAll();
        handler.open(getAdminCredentials(), org.apache.ambari.server.serveraction.kerberos.KerberosOperationHandlerTest.DEFAULT_REALM, getKerberosEnv());
        handler.createPrincipal(createPrincipal(service), "password", service);
        handler.close();
        verifyAll();
    }

    @org.junit.Test
    public void testUserPrincipalExistsNotFound() throws java.lang.Exception {
        testPrincipalExistsNotFound(false);
    }

    @org.junit.Test
    public void testServicePrincipalExistsNotFound() throws java.lang.Exception {
        testPrincipalExistsNotFound(true);
    }

    private void testPrincipalExistsNotFound(boolean service) throws java.lang.Exception {
        org.apache.ambari.server.serveraction.kerberos.KerberosOperationHandler handler = createMockedHandler();
        setupOpenSuccess(handler);
        setupPrincipalDoesNotExist(handler, service);
        replayAll();
        handler.open(getAdminCredentials(), org.apache.ambari.server.serveraction.kerberos.KerberosOperationHandlerTest.DEFAULT_REALM, getKerberosEnv());
        junit.framework.Assert.assertFalse(handler.principalExists(createPrincipal(service), service));
        handler.close();
        verifyAll();
    }

    @org.junit.Test
    public void testUserPrincipalExistsFound() throws java.lang.Exception {
        testPrincipalExistsFound(false);
    }

    @org.junit.Test
    public void testServicePrincipalExistsFound() throws java.lang.Exception {
        testPrincipalExistsFound(true);
    }

    private void testPrincipalExistsFound(boolean service) throws java.lang.Exception {
        org.apache.ambari.server.serveraction.kerberos.KerberosOperationHandler handler = createMockedHandler();
        setupOpenSuccess(handler);
        setupPrincipalExists(handler, service);
        replayAll();
        handler.open(getAdminCredentials(), org.apache.ambari.server.serveraction.kerberos.KerberosOperationHandlerTest.DEFAULT_REALM, getKerberosEnv());
        junit.framework.Assert.assertTrue(handler.principalExists(createPrincipal(service), service));
        handler.close();
        verifyAll();
    }

    @org.junit.Test
    public void testCreateKeytabFileOneAtATime() throws java.lang.Exception {
        org.apache.ambari.server.serveraction.kerberos.KerberosOperationHandler handler = createHandler();
        java.io.File file = folder.newFile();
        final java.lang.String principal1 = "principal1@REALM.COM";
        final java.lang.String principal2 = "principal2@REALM.COM";
        int count;
        junit.framework.Assert.assertTrue(handler.createKeytabFile(principal1, "some password", 0, file));
        org.apache.directory.server.kerberos.shared.keytab.Keytab keytab = org.apache.directory.server.kerberos.shared.keytab.Keytab.read(file);
        junit.framework.Assert.assertNotNull(keytab);
        java.util.List<org.apache.directory.server.kerberos.shared.keytab.KeytabEntry> entries = keytab.getEntries();
        junit.framework.Assert.assertNotNull(entries);
        junit.framework.Assert.assertFalse(entries.isEmpty());
        count = entries.size();
        for (org.apache.directory.server.kerberos.shared.keytab.KeytabEntry entry : entries) {
            junit.framework.Assert.assertEquals(principal1, entry.getPrincipalName());
        }
        junit.framework.Assert.assertTrue(handler.createKeytabFile(principal2, "some password", 0, file));
        keytab = org.apache.directory.server.kerberos.shared.keytab.Keytab.read(file);
        junit.framework.Assert.assertNotNull(keytab);
        entries = keytab.getEntries();
        junit.framework.Assert.assertNotNull(entries);
        junit.framework.Assert.assertFalse(entries.isEmpty());
        junit.framework.Assert.assertEquals(count * 2, entries.size());
    }

    @org.junit.Test
    public void testEnsureKeytabFileContainsNoDuplicates() throws java.lang.Exception {
        org.apache.ambari.server.serveraction.kerberos.KerberosOperationHandler handler = createHandler();
        java.io.File file = folder.newFile();
        final java.lang.String principal1 = "principal1@REALM.COM";
        final java.lang.String principal2 = "principal2@REALM.COM";
        java.util.Set<java.lang.String> seenEntries = new java.util.HashSet<>();
        junit.framework.Assert.assertTrue(handler.createKeytabFile(principal1, "some password", 0, file));
        junit.framework.Assert.assertTrue(handler.createKeytabFile(principal2, "some password", 0, file));
        junit.framework.Assert.assertTrue(handler.createKeytabFile(principal2, "some password", 0, file));
        org.apache.directory.server.kerberos.shared.keytab.Keytab keytab = org.apache.directory.server.kerberos.shared.keytab.Keytab.read(file);
        junit.framework.Assert.assertNotNull(keytab);
        java.util.List<org.apache.directory.server.kerberos.shared.keytab.KeytabEntry> entries = keytab.getEntries();
        junit.framework.Assert.assertNotNull(entries);
        junit.framework.Assert.assertFalse(entries.isEmpty());
        for (org.apache.directory.server.kerberos.shared.keytab.KeytabEntry entry : entries) {
            java.lang.String seenEntry = java.lang.String.format("%s|%s", entry.getPrincipalName(), entry.getKey().getKeyType().toString());
            junit.framework.Assert.assertFalse(seenEntries.contains(seenEntry));
            seenEntries.add(seenEntry);
        }
    }

    @org.junit.Test
    public void testCreateKeytabFileExceptions() throws java.lang.Exception {
        org.apache.ambari.server.serveraction.kerberos.KerberosOperationHandler handler = createHandler();
        java.io.File file = folder.newFile();
        final java.lang.String principal1 = "principal1@REALM.COM";
        try {
            handler.createKeytabFile(null, "some password", 0, file);
            junit.framework.Assert.fail("KerberosOperationException not thrown with null principal");
        } catch (java.lang.Throwable t) {
            junit.framework.Assert.assertEquals(org.apache.ambari.server.serveraction.kerberos.KerberosOperationException.class, t.getClass());
        }
        try {
            handler.createKeytabFile(principal1, null, null, file);
            junit.framework.Assert.fail("KerberosOperationException not thrown with null password");
        } catch (java.lang.Throwable t) {
            junit.framework.Assert.assertEquals(org.apache.ambari.server.serveraction.kerberos.KerberosOperationException.class, t.getClass());
        }
        try {
            handler.createKeytabFile(principal1, "some password", 0, null);
            junit.framework.Assert.fail("KerberosOperationException not thrown with null file");
        } catch (java.lang.Throwable t) {
            junit.framework.Assert.assertEquals(org.apache.ambari.server.serveraction.kerberos.KerberosOperationException.class, t.getClass());
        }
    }

    @org.junit.Test
    public void testCreateKeytabFileFromBase64EncodedData() throws java.lang.Exception {
        org.apache.ambari.server.serveraction.kerberos.KerberosOperationHandler handler = createHandler();
        java.io.File file = folder.newFile();
        final java.lang.String principal = "principal@REALM.COM";
        junit.framework.Assert.assertTrue(handler.createKeytabFile(principal, "some password", 0, file));
        java.io.FileInputStream fis = new java.io.FileInputStream(file);
        byte[] data = new byte[((int) (file.length()))];
        junit.framework.Assert.assertEquals(data.length, fis.read(data));
        fis.close();
        java.io.File f = handler.createKeytabFile(org.apache.commons.codec.binary.Base64.encodeBase64String(data));
        if (f != null) {
            try {
                org.apache.directory.server.kerberos.shared.keytab.Keytab keytab = org.apache.directory.server.kerberos.shared.keytab.Keytab.read(f);
                junit.framework.Assert.assertNotNull(keytab);
                java.util.List<org.apache.directory.server.kerberos.shared.keytab.KeytabEntry> entries = keytab.getEntries();
                junit.framework.Assert.assertNotNull(entries);
                junit.framework.Assert.assertFalse(entries.isEmpty());
                for (org.apache.directory.server.kerberos.shared.keytab.KeytabEntry entry : entries) {
                    junit.framework.Assert.assertEquals(principal, entry.getPrincipalName());
                }
            } finally {
                if (!f.delete()) {
                    f.deleteOnExit();
                }
            }
        }
    }

    @org.junit.Test
    public void testMergeKeytabs() throws org.apache.ambari.server.serveraction.kerberos.KerberosOperationException {
        org.apache.ambari.server.serveraction.kerberos.KerberosOperationHandler handler = createHandler();
        org.apache.directory.server.kerberos.shared.keytab.Keytab keytab1 = handler.createKeytab("principal@EXAMPLE.COM", "password", 1);
        org.apache.directory.server.kerberos.shared.keytab.Keytab keytab2 = handler.createKeytab("principal@EXAMPLE.COM", "password1", 1);
        org.apache.directory.server.kerberos.shared.keytab.Keytab keytab3 = handler.createKeytab("principal1@EXAMPLE.COM", "password", 4);
        org.apache.directory.server.kerberos.shared.keytab.Keytab merged;
        merged = handler.mergeKeytabs(keytab1, keytab2);
        junit.framework.Assert.assertEquals(keytab1.getEntries().size(), merged.getEntries().size());
        merged = handler.mergeKeytabs(keytab1, keytab3);
        junit.framework.Assert.assertEquals(keytab1.getEntries().size() + keytab3.getEntries().size(), merged.getEntries().size());
        merged = handler.mergeKeytabs(keytab2, keytab3);
        junit.framework.Assert.assertEquals(keytab2.getEntries().size() + keytab3.getEntries().size(), merged.getEntries().size());
        merged = handler.mergeKeytabs(keytab2, merged);
        junit.framework.Assert.assertEquals(keytab2.getEntries().size() + keytab3.getEntries().size(), merged.getEntries().size());
    }

    @org.junit.Test
    public void testTranslateEncryptionTypes() throws java.lang.Exception {
        org.apache.ambari.server.serveraction.kerberos.KerberosOperationHandler handler = createHandler();
        junit.framework.Assert.assertEquals(new java.util.HashSet<org.apache.directory.shared.kerberos.codec.types.EncryptionType>() {
            {
                add(EncryptionType.AES256_CTS_HMAC_SHA1_96);
                add(EncryptionType.AES128_CTS_HMAC_SHA1_96);
                add(EncryptionType.DES3_CBC_SHA1_KD);
                add(EncryptionType.DES_CBC_MD5);
                add(EncryptionType.DES_CBC_MD4);
                add(EncryptionType.DES_CBC_CRC);
                add(EncryptionType.UNKNOWN);
            }
        }, handler.translateEncryptionTypes("aes256-cts-hmac-sha1-96\n aes128-cts-hmac-sha1-96\tdes3-cbc-sha1 arcfour-hmac-md5 " + "camellia256-cts-cmac camellia128-cts-cmac des-cbc-crc des-cbc-md5 des-cbc-md4", "\\s+"));
        junit.framework.Assert.assertEquals(new java.util.HashSet<org.apache.directory.shared.kerberos.codec.types.EncryptionType>() {
            {
                add(EncryptionType.AES256_CTS_HMAC_SHA1_96);
                add(EncryptionType.AES128_CTS_HMAC_SHA1_96);
            }
        }, handler.translateEncryptionTypes("aes", " "));
        junit.framework.Assert.assertEquals(new java.util.HashSet<org.apache.directory.shared.kerberos.codec.types.EncryptionType>() {
            {
                add(EncryptionType.AES256_CTS_HMAC_SHA1_96);
            }
        }, handler.translateEncryptionTypes("aes-256", " "));
        junit.framework.Assert.assertEquals(new java.util.HashSet<org.apache.directory.shared.kerberos.codec.types.EncryptionType>() {
            {
                add(EncryptionType.DES3_CBC_SHA1_KD);
            }
        }, handler.translateEncryptionTypes("des3", " "));
    }

    @org.junit.Test(expected = org.apache.ambari.server.serveraction.kerberos.KerberosOperationException.class)
    public void testTranslateWrongEncryptionTypes() throws java.lang.Exception {
        org.apache.ambari.server.serveraction.kerberos.KerberosOperationHandler handler = createHandler();
        handler.translateEncryptionTypes("aes-255", " ");
    }

    @org.junit.Test
    public void testEscapeCharacters() throws org.apache.ambari.server.serveraction.kerberos.KerberosOperationException {
        org.apache.ambari.server.serveraction.kerberos.KerberosOperationHandler handler = createHandler();
        java.util.HashSet<java.lang.Character> specialCharacters = new java.util.HashSet<java.lang.Character>() {
            {
                add('/');
                add(',');
                add('\\');
                add('#');
                add('+');
                add('<');
                add('>');
                add(';');
                add('"');
                add('=');
                add(' ');
            }
        };
        junit.framework.Assert.assertEquals("\\/\\,\\\\\\#\\+\\<\\>\\;\\\"\\=\\ ", handler.escapeCharacters("/,\\#+<>;\"= ", specialCharacters, '\\'));
        junit.framework.Assert.assertNull(handler.escapeCharacters(null, specialCharacters, '\\'));
        junit.framework.Assert.assertEquals("", handler.escapeCharacters("", specialCharacters, '\\'));
        junit.framework.Assert.assertEquals("nothing_special_here", handler.escapeCharacters("nothing_special_here", specialCharacters, '\\'));
        junit.framework.Assert.assertEquals("\\/\\,\\\\\\#\\+\\<\\>\\;\\\"\\=\\ ", handler.escapeCharacters("/,\\#+<>;\"= ", specialCharacters, '\\'));
        junit.framework.Assert.assertEquals("nothing<>special#here!", handler.escapeCharacters("nothing<>special#here!", null, '\\'));
        junit.framework.Assert.assertEquals("nothing<>special#here!", handler.escapeCharacters("nothing<>special#here!", java.util.Collections.emptySet(), '\\'));
        junit.framework.Assert.assertEquals("nothing<>special#here!", handler.escapeCharacters("nothing<>special#here!", java.util.Collections.singleton('?'), '\\'));
        junit.framework.Assert.assertEquals("\\A\'s are special!", handler.escapeCharacters("A's are special!", java.util.Collections.singleton('A'), '\\'));
    }

    @org.junit.Test(expected = org.apache.ambari.server.serveraction.kerberos.KerberosAdminAuthenticationException.class)
    public void testAdminCredentialsNullPrincipal() throws org.apache.ambari.server.serveraction.kerberos.KerberosOperationException {
        org.apache.ambari.server.serveraction.kerberos.KerberosOperationHandler handler = createHandler();
        org.apache.ambari.server.security.credential.PrincipalKeyCredential credentials = new org.apache.ambari.server.security.credential.PrincipalKeyCredential(null, "password");
        handler.setAdministratorCredential(credentials);
    }

    @org.junit.Test(expected = org.apache.ambari.server.serveraction.kerberos.KerberosAdminAuthenticationException.class)
    public void testAdminCredentialsEmptyPrincipal() throws org.apache.ambari.server.serveraction.kerberos.KerberosOperationException {
        org.apache.ambari.server.serveraction.kerberos.KerberosOperationHandler handler = createHandler();
        org.apache.ambari.server.security.credential.PrincipalKeyCredential credentials = new org.apache.ambari.server.security.credential.PrincipalKeyCredential("", "password");
        handler.setAdministratorCredential(credentials);
    }

    @org.junit.Test(expected = org.apache.ambari.server.serveraction.kerberos.KerberosAdminAuthenticationException.class)
    public void testAdminCredentialsNullCredential() throws org.apache.ambari.server.serveraction.kerberos.KerberosOperationException {
        org.apache.ambari.server.serveraction.kerberos.KerberosOperationHandler handler = createHandler();
        org.apache.ambari.server.security.credential.PrincipalKeyCredential credentials = new org.apache.ambari.server.security.credential.PrincipalKeyCredential("principal", ((char[]) (null)));
        handler.setAdministratorCredential(credentials);
    }

    @org.junit.Test(expected = org.apache.ambari.server.serveraction.kerberos.KerberosAdminAuthenticationException.class)
    public void testAdminCredentialsEmptyCredential1() throws org.apache.ambari.server.serveraction.kerberos.KerberosOperationException {
        org.apache.ambari.server.serveraction.kerberos.KerberosOperationHandler handler = createHandler();
        org.apache.ambari.server.security.credential.PrincipalKeyCredential credentials = new org.apache.ambari.server.security.credential.PrincipalKeyCredential("principal", "");
        handler.setAdministratorCredential(credentials);
    }

    @org.junit.Test
    public void testSetExecutableSearchPaths() throws org.apache.ambari.server.serveraction.kerberos.KerberosOperationException {
        org.apache.ambari.server.serveraction.kerberos.KerberosOperationHandler handler = createHandler();
        handler.setExecutableSearchPaths(((java.lang.String) (null)));
        junit.framework.Assert.assertNull(handler.getExecutableSearchPaths());
        handler.setExecutableSearchPaths(((java.lang.String[]) (null)));
        junit.framework.Assert.assertNull(handler.getExecutableSearchPaths());
        handler.setExecutableSearchPaths("");
        junit.framework.Assert.assertNotNull(handler.getExecutableSearchPaths());
        junit.framework.Assert.assertEquals(0, handler.getExecutableSearchPaths().length);
        handler.setExecutableSearchPaths(new java.lang.String[0]);
        junit.framework.Assert.assertNotNull(handler.getExecutableSearchPaths());
        junit.framework.Assert.assertEquals(0, handler.getExecutableSearchPaths().length);
        handler.setExecutableSearchPaths(new java.lang.String[]{ "" });
        junit.framework.Assert.assertNotNull(handler.getExecutableSearchPaths());
        junit.framework.Assert.assertEquals(1, handler.getExecutableSearchPaths().length);
        handler.setExecutableSearchPaths("/path1, path2, path3/");
        junit.framework.Assert.assertNotNull(handler.getExecutableSearchPaths());
        junit.framework.Assert.assertEquals(3, handler.getExecutableSearchPaths().length);
        junit.framework.Assert.assertEquals("/path1", handler.getExecutableSearchPaths()[0]);
        junit.framework.Assert.assertEquals("path2", handler.getExecutableSearchPaths()[1]);
        junit.framework.Assert.assertEquals("path3/", handler.getExecutableSearchPaths()[2]);
        handler.setExecutableSearchPaths("/path1, path2, ,path3/");
        junit.framework.Assert.assertNotNull(handler.getExecutableSearchPaths());
        junit.framework.Assert.assertEquals(3, handler.getExecutableSearchPaths().length);
        junit.framework.Assert.assertEquals("/path1", handler.getExecutableSearchPaths()[0]);
        junit.framework.Assert.assertEquals("path2", handler.getExecutableSearchPaths()[1]);
        junit.framework.Assert.assertEquals("path3/", handler.getExecutableSearchPaths()[2]);
        handler.setExecutableSearchPaths(new java.lang.String[]{ "/path1", "path2", "path3/" });
        junit.framework.Assert.assertNotNull(handler.getExecutableSearchPaths());
        junit.framework.Assert.assertEquals(3, handler.getExecutableSearchPaths().length);
        junit.framework.Assert.assertEquals("/path1", handler.getExecutableSearchPaths()[0]);
        junit.framework.Assert.assertEquals("path2", handler.getExecutableSearchPaths()[1]);
        junit.framework.Assert.assertEquals("path3/", handler.getExecutableSearchPaths()[2]);
    }

    protected abstract org.apache.ambari.server.serveraction.kerberos.KerberosOperationHandler createMockedHandler() throws org.apache.ambari.server.serveraction.kerberos.KerberosOperationException;

    protected abstract void setupOpenSuccess(org.apache.ambari.server.serveraction.kerberos.KerberosOperationHandler handler) throws java.lang.Exception;

    protected abstract void setupOpenFailure(org.apache.ambari.server.serveraction.kerberos.KerberosOperationHandler handler) throws java.lang.Exception;

    protected abstract void setupPrincipalAlreadyExists(org.apache.ambari.server.serveraction.kerberos.KerberosOperationHandler handler, boolean service) throws java.lang.Exception;

    protected abstract void setupPrincipalDoesNotExist(org.apache.ambari.server.serveraction.kerberos.KerberosOperationHandler handler, boolean service) throws java.lang.Exception;

    protected abstract void setupPrincipalExists(org.apache.ambari.server.serveraction.kerberos.KerberosOperationHandler handler, boolean service) throws java.lang.Exception;

    protected abstract java.util.Map<java.lang.String, java.lang.String> getKerberosEnv();

    protected org.apache.ambari.server.security.credential.PrincipalKeyCredential getAdminCredentials() {
        return org.apache.ambari.server.serveraction.kerberos.KerberosOperationHandlerTest.DEFAULT_ADMIN_CREDENTIALS;
    }

    private org.apache.ambari.server.serveraction.kerberos.KerberosOperationHandler createHandler() throws org.apache.ambari.server.serveraction.kerberos.KerberosOperationException {
        org.apache.ambari.server.serveraction.kerberos.KerberosOperationHandler handler = new org.apache.ambari.server.serveraction.kerberos.KerberosOperationHandler() {
            @java.lang.Override
            public void open(org.apache.ambari.server.security.credential.PrincipalKeyCredential administratorCredentials, java.lang.String defaultRealm, java.util.Map<java.lang.String, java.lang.String> kerberosConfiguration) throws org.apache.ambari.server.serveraction.kerberos.KerberosOperationException {
                setAdministratorCredential(administratorCredentials);
                setDefaultRealm(defaultRealm);
                setExecutableSearchPaths("/usr/bin, /usr/kerberos/bin, /usr/sbin");
            }

            @java.lang.Override
            public void close() throws org.apache.ambari.server.serveraction.kerberos.KerberosOperationException {
            }

            @java.lang.Override
            public boolean principalExists(java.lang.String principal, boolean service) throws org.apache.ambari.server.serveraction.kerberos.KerberosOperationException {
                return false;
            }

            @java.lang.Override
            public java.lang.Integer createPrincipal(java.lang.String principal, java.lang.String password, boolean service) throws org.apache.ambari.server.serveraction.kerberos.KerberosOperationException {
                return 0;
            }

            @java.lang.Override
            public java.lang.Integer setPrincipalPassword(java.lang.String principal, java.lang.String password, boolean service) throws org.apache.ambari.server.serveraction.kerberos.KerberosOperationException {
                return 0;
            }

            @java.lang.Override
            public boolean removePrincipal(java.lang.String principal, boolean service) throws org.apache.ambari.server.serveraction.kerberos.KerberosOperationException {
                return false;
            }
        };
        handler.open(new org.apache.ambari.server.security.credential.PrincipalKeyCredential("admin/admin", "hadoop"), "EXAMPLE.COM", null);
        return handler;
    }

    private java.lang.String createPrincipal(boolean service) {
        return java.lang.String.format("%s@%s", service ? "service/host" : "user", org.apache.ambari.server.serveraction.kerberos.KerberosOperationHandlerTest.DEFAULT_REALM);
    }
}