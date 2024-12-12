package org.apache.ambari.server.security;
import org.springframework.ldap.core.DirContextAdapter;
import org.springframework.ldap.support.LdapUtils;
import static org.easymock.EasyMock.createNiceMock;
import static org.easymock.EasyMock.createStrictMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
public class AmbariLdapUtilsTest {
    private static final java.lang.String USER_BASE_DN = "ou=hdp,ou=Users,dc=apache,dc=org";

    private static final java.lang.String USER_RELATIVE_DN = "uid=myuser";

    private static final java.lang.String USER_DN = (org.apache.ambari.server.security.AmbariLdapUtilsTest.USER_RELATIVE_DN + ",") + org.apache.ambari.server.security.AmbariLdapUtilsTest.USER_BASE_DN;

    @org.junit.Test
    public void testIsUserPrincipalNameFormat_True() throws java.lang.Exception {
        java.lang.String testLoginName = "testuser@domain1.d_1.com";
        boolean isUserPrincipalNameFormat = org.apache.ambari.server.security.authorization.AmbariLdapUtils.isUserPrincipalNameFormat(testLoginName);
        junit.framework.Assert.assertTrue(isUserPrincipalNameFormat);
    }

    @org.junit.Test
    public void testIsUserPrincipalNameFormatMultipleAtSign_True() throws java.lang.Exception {
        java.lang.String testLoginName = "@testuser@domain1.d_1.com";
        boolean isUserPrincipalNameFormat = org.apache.ambari.server.security.authorization.AmbariLdapUtils.isUserPrincipalNameFormat(testLoginName);
        junit.framework.Assert.assertTrue(isUserPrincipalNameFormat);
    }

    @org.junit.Test
    public void testIsUserPrincipalNameFormat_False() throws java.lang.Exception {
        java.lang.String testLoginName = "testuser";
        boolean isUserPrincipalNameFormat = org.apache.ambari.server.security.authorization.AmbariLdapUtils.isUserPrincipalNameFormat(testLoginName);
        junit.framework.Assert.assertFalse(isUserPrincipalNameFormat);
    }

    @org.junit.Test
    public void testIsUserPrincipalNameFormatWithAtSign_False() throws java.lang.Exception {
        java.lang.String testLoginName = "@testuser";
        boolean isUserPrincipalNameFormat = org.apache.ambari.server.security.authorization.AmbariLdapUtils.isUserPrincipalNameFormat(testLoginName);
        junit.framework.Assert.assertFalse(isUserPrincipalNameFormat);
    }

    @org.junit.Test
    public void testIsUserPrincipalNameFormatWithAtSign1_False() throws java.lang.Exception {
        java.lang.String testLoginName = "testuser@";
        boolean isUserPrincipalNameFormat = org.apache.ambari.server.security.authorization.AmbariLdapUtils.isUserPrincipalNameFormat(testLoginName);
        junit.framework.Assert.assertFalse(isUserPrincipalNameFormat);
    }

    @org.junit.Test
    public void testIsLdapObjectOutOfScopeFromBaseDn() throws javax.naming.NamingException {
        javax.naming.Name fullDn = org.springframework.ldap.support.LdapUtils.newLdapName(org.apache.ambari.server.security.AmbariLdapUtilsTest.USER_DN);
        org.springframework.ldap.core.DirContextAdapter adapter = EasyMock.createNiceMock(org.springframework.ldap.core.DirContextAdapter.class);
        EasyMock.expect(adapter.getDn()).andReturn(fullDn);
        EasyMock.expect(adapter.getNameInNamespace()).andReturn(org.apache.ambari.server.security.AmbariLdapUtilsTest.USER_DN);
        EasyMock.replay(adapter);
        boolean isOutOfScopeFromBaseDN = org.apache.ambari.server.security.authorization.AmbariLdapUtils.isLdapObjectOutOfScopeFromBaseDn(adapter, "dc=apache,dc=org");
        junit.framework.Assert.assertFalse(isOutOfScopeFromBaseDN);
        EasyMock.verify(adapter);
    }

    @org.junit.Test
    public void testIsLdapObjectOutOfScopeFromBaseDn_dnOutOfScope() throws javax.naming.NamingException {
        javax.naming.Name fullDn = org.springframework.ldap.support.LdapUtils.newLdapName(org.apache.ambari.server.security.AmbariLdapUtilsTest.USER_DN);
        org.springframework.ldap.core.DirContextAdapter adapter = EasyMock.createNiceMock(org.springframework.ldap.core.DirContextAdapter.class);
        EasyMock.expect(adapter.getDn()).andReturn(fullDn);
        EasyMock.expect(adapter.getNameInNamespace()).andReturn(org.apache.ambari.server.security.AmbariLdapUtilsTest.USER_DN);
        EasyMock.replay(adapter);
        boolean isOutOfScopeFromBaseDN = org.apache.ambari.server.security.authorization.AmbariLdapUtils.isLdapObjectOutOfScopeFromBaseDn(adapter, "dc=apache,dc=org,ou=custom");
        junit.framework.Assert.assertTrue(isOutOfScopeFromBaseDN);
        EasyMock.verify(adapter);
    }

    @org.junit.Test
    public void testGetFullDn() throws java.lang.Exception {
        org.springframework.ldap.core.DirContextAdapter adapterFullDn = EasyMock.createStrictMock(org.springframework.ldap.core.DirContextAdapter.class);
        EasyMock.expect(adapterFullDn.getNameInNamespace()).andReturn(org.apache.ambari.server.security.AmbariLdapUtilsTest.USER_DN).anyTimes();
        org.springframework.ldap.core.DirContextAdapter adapterBaseDn = EasyMock.createStrictMock(org.springframework.ldap.core.DirContextAdapter.class);
        EasyMock.expect(adapterBaseDn.getNameInNamespace()).andReturn(org.apache.ambari.server.security.AmbariLdapUtilsTest.USER_BASE_DN).anyTimes();
        javax.naming.Name absoluteDn = org.springframework.ldap.support.LdapUtils.newLdapName(org.apache.ambari.server.security.AmbariLdapUtilsTest.USER_DN);
        javax.naming.Name relativeDn = org.springframework.ldap.support.LdapUtils.newLdapName(org.apache.ambari.server.security.AmbariLdapUtilsTest.USER_RELATIVE_DN);
        EasyMock.replay(adapterFullDn, adapterBaseDn);
        javax.naming.Name fullDn;
        fullDn = org.apache.ambari.server.security.authorization.AmbariLdapUtils.getFullDn(absoluteDn, adapterFullDn);
        org.junit.Assert.assertEquals(absoluteDn, fullDn);
        fullDn = org.apache.ambari.server.security.authorization.AmbariLdapUtils.getFullDn(absoluteDn, adapterBaseDn);
        org.junit.Assert.assertEquals(absoluteDn, fullDn);
        fullDn = org.apache.ambari.server.security.authorization.AmbariLdapUtils.getFullDn(relativeDn, adapterBaseDn);
        org.junit.Assert.assertEquals(absoluteDn, fullDn);
        fullDn = org.apache.ambari.server.security.authorization.AmbariLdapUtils.getFullDn(absoluteDn.toString(), adapterFullDn);
        org.junit.Assert.assertEquals(absoluteDn, fullDn);
        fullDn = org.apache.ambari.server.security.authorization.AmbariLdapUtils.getFullDn(absoluteDn.toString(), adapterBaseDn);
        org.junit.Assert.assertEquals(absoluteDn, fullDn);
        fullDn = org.apache.ambari.server.security.authorization.AmbariLdapUtils.getFullDn(relativeDn.toString(), adapterBaseDn);
        org.junit.Assert.assertEquals(absoluteDn, fullDn);
        javax.naming.Name nameInNamespaceFullDn = org.springframework.ldap.support.LdapUtils.newLdapName(adapterFullDn.getNameInNamespace());
        javax.naming.Name nameInNamespaceBaseDn = org.springframework.ldap.support.LdapUtils.newLdapName(adapterBaseDn.getNameInNamespace());
        fullDn = org.apache.ambari.server.security.authorization.AmbariLdapUtils.getFullDn(absoluteDn, nameInNamespaceFullDn);
        org.junit.Assert.assertEquals(absoluteDn, fullDn);
        fullDn = org.apache.ambari.server.security.authorization.AmbariLdapUtils.getFullDn(absoluteDn, nameInNamespaceBaseDn);
        org.junit.Assert.assertEquals(absoluteDn, fullDn);
        fullDn = org.apache.ambari.server.security.authorization.AmbariLdapUtils.getFullDn(relativeDn, nameInNamespaceBaseDn);
        org.junit.Assert.assertEquals(absoluteDn, fullDn);
        org.junit.Assert.assertEquals(adapterFullDn.getNameInNamespace(), nameInNamespaceFullDn.toString());
        org.junit.Assert.assertEquals(adapterBaseDn.getNameInNamespace(), nameInNamespaceBaseDn.toString());
        EasyMock.verify(adapterFullDn, adapterBaseDn);
    }
}