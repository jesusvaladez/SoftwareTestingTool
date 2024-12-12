package org.apache.ambari.server.security;
public class SecurePasswordHelperTest {
    private org.apache.ambari.server.security.SecurePasswordHelper securePasswordHelper;

    @org.junit.Before
    public void setUp() throws java.lang.Exception {
        securePasswordHelper = new org.apache.ambari.server.security.SecurePasswordHelper();
    }

    @org.junit.Test
    public void testCreateSecurePassword() throws java.lang.Exception {
        java.lang.String password1 = securePasswordHelper.createSecurePassword();
        junit.framework.Assert.assertNotNull(password1);
        junit.framework.Assert.assertEquals(org.apache.ambari.server.security.SecurePasswordHelper.DEFAULT_SECURE_PASSWORD_LENGTH, password1.length());
        java.lang.String password2 = securePasswordHelper.createSecurePassword();
        junit.framework.Assert.assertNotNull(password2);
        junit.framework.Assert.assertEquals(org.apache.ambari.server.security.SecurePasswordHelper.DEFAULT_SECURE_PASSWORD_LENGTH, password2.length());
        junit.framework.Assert.assertFalse(password1.equals(password2));
    }

    @org.junit.Test
    public void testCreateSecurePasswordWithRules() throws java.lang.Exception {
        java.lang.String password;
        password = securePasswordHelper.createSecurePassword(null, null, null, null, null, null);
        junit.framework.Assert.assertNotNull(password);
        junit.framework.Assert.assertEquals(org.apache.ambari.server.security.SecurePasswordHelper.DEFAULT_SECURE_PASSWORD_LENGTH, password.length());
        assertMinLowercaseLetters(org.apache.ambari.server.security.SecurePasswordHelper.DEFAULT_SECURE_PASSWORD_MIN_LOWERCASE_LETTERS, password);
        assertMinUppercaseLetters(org.apache.ambari.server.security.SecurePasswordHelper.DEFAULT_SECURE_PASSWORD_MIN_UPPERCASE_LETTERS, password);
        assertMinDigits(org.apache.ambari.server.security.SecurePasswordHelper.DEFAULT_SECURE_PASSWORD_MIN_DIGITS, password);
        assertMinPunctuation(org.apache.ambari.server.security.SecurePasswordHelper.DEFAULT_SECURE_PASSWORD_MIN_PUNCTUATION, password);
        assertMinWhitespace(org.apache.ambari.server.security.SecurePasswordHelper.DEFAULT_SECURE_PASSWORD_MIN_WHITESPACE, password);
        password = securePasswordHelper.createSecurePassword(10, null, null, null, null, null);
        junit.framework.Assert.assertNotNull(password);
        junit.framework.Assert.assertEquals(10, password.length());
        assertMinLowercaseLetters(org.apache.ambari.server.security.SecurePasswordHelper.DEFAULT_SECURE_PASSWORD_MIN_LOWERCASE_LETTERS, password);
        assertMinUppercaseLetters(org.apache.ambari.server.security.SecurePasswordHelper.DEFAULT_SECURE_PASSWORD_MIN_UPPERCASE_LETTERS, password);
        assertMinDigits(org.apache.ambari.server.security.SecurePasswordHelper.DEFAULT_SECURE_PASSWORD_MIN_DIGITS, password);
        assertMinPunctuation(org.apache.ambari.server.security.SecurePasswordHelper.DEFAULT_SECURE_PASSWORD_MIN_PUNCTUATION, password);
        assertMinWhitespace(org.apache.ambari.server.security.SecurePasswordHelper.DEFAULT_SECURE_PASSWORD_MIN_WHITESPACE, password);
        password = securePasswordHelper.createSecurePassword(0, null, null, null, null, null);
        junit.framework.Assert.assertNotNull(password);
        junit.framework.Assert.assertEquals(org.apache.ambari.server.security.SecurePasswordHelper.DEFAULT_SECURE_PASSWORD_LENGTH, password.length());
        assertMinLowercaseLetters(org.apache.ambari.server.security.SecurePasswordHelper.DEFAULT_SECURE_PASSWORD_MIN_LOWERCASE_LETTERS, password);
        assertMinUppercaseLetters(org.apache.ambari.server.security.SecurePasswordHelper.DEFAULT_SECURE_PASSWORD_MIN_UPPERCASE_LETTERS, password);
        assertMinDigits(org.apache.ambari.server.security.SecurePasswordHelper.DEFAULT_SECURE_PASSWORD_MIN_DIGITS, password);
        assertMinPunctuation(org.apache.ambari.server.security.SecurePasswordHelper.DEFAULT_SECURE_PASSWORD_MIN_PUNCTUATION, password);
        assertMinWhitespace(org.apache.ambari.server.security.SecurePasswordHelper.DEFAULT_SECURE_PASSWORD_MIN_WHITESPACE, password);
        password = securePasswordHelper.createSecurePassword(-20, null, null, null, null, null);
        junit.framework.Assert.assertNotNull(password);
        junit.framework.Assert.assertEquals(org.apache.ambari.server.security.SecurePasswordHelper.DEFAULT_SECURE_PASSWORD_LENGTH, password.length());
        assertMinLowercaseLetters(org.apache.ambari.server.security.SecurePasswordHelper.DEFAULT_SECURE_PASSWORD_MIN_LOWERCASE_LETTERS, password);
        assertMinUppercaseLetters(org.apache.ambari.server.security.SecurePasswordHelper.DEFAULT_SECURE_PASSWORD_MIN_UPPERCASE_LETTERS, password);
        assertMinDigits(org.apache.ambari.server.security.SecurePasswordHelper.DEFAULT_SECURE_PASSWORD_MIN_DIGITS, password);
        assertMinPunctuation(org.apache.ambari.server.security.SecurePasswordHelper.DEFAULT_SECURE_PASSWORD_MIN_PUNCTUATION, password);
        assertMinWhitespace(org.apache.ambari.server.security.SecurePasswordHelper.DEFAULT_SECURE_PASSWORD_MIN_WHITESPACE, password);
        password = securePasswordHelper.createSecurePassword(100, 30, 20, 10, 5, 2);
        junit.framework.Assert.assertNotNull(password);
        junit.framework.Assert.assertEquals(100, password.length());
        assertMinLowercaseLetters(30, password);
        assertMinUppercaseLetters(20, password);
        assertMinDigits(10, password);
        assertMinPunctuation(5, password);
        assertMinWhitespace(2, password);
        password = securePasswordHelper.createSecurePassword(100, 20, 20, 20, 20, 0);
        junit.framework.Assert.assertNotNull(password);
        junit.framework.Assert.assertEquals(100, password.length());
        assertMinLowercaseLetters(20, password);
        assertMinUppercaseLetters(20, password);
        assertMinDigits(20, password);
        assertMinPunctuation(20, password);
        assertMinWhitespace(0, password);
    }

    private void assertMinLowercaseLetters(int minCount, java.lang.String password) {
        assertMinCharacterCount(minCount, password, org.apache.ambari.server.security.SecurePasswordHelper.SECURE_PASSWORD_CHARACTER_CLASS_LOWERCASE_LETTERS);
    }

    private void assertMinUppercaseLetters(int minCount, java.lang.String password) {
        assertMinCharacterCount(minCount, password, org.apache.ambari.server.security.SecurePasswordHelper.SECURE_PASSWORD_CHARACTER_CLASS_UPPERCASE_LETTERS);
    }

    private void assertMinDigits(int minCount, java.lang.String password) {
        assertMinCharacterCount(minCount, password, org.apache.ambari.server.security.SecurePasswordHelper.SECURE_PASSWORD_CHARACTER_CLASS_DIGITS);
    }

    private void assertMinPunctuation(int minCount, java.lang.String password) {
        assertMinCharacterCount(minCount, password, org.apache.ambari.server.security.SecurePasswordHelper.SECURE_PASSWORD_CHARACTER_CLASS_PUNCTUATION);
    }

    private void assertMinWhitespace(int minCount, java.lang.String password) {
        assertMinCharacterCount(minCount, password, org.apache.ambari.server.security.SecurePasswordHelper.SECURE_PASSWORD_CHARACTER_CLASS_WHITESPACE);
    }

    private void assertMinCharacterCount(int minCount, java.lang.String string, char[] characters) {
        int count = 0;
        java.util.Set<java.lang.Character> set = new java.util.HashSet<>();
        for (char c : characters) {
            set.add(c);
        }
        for (char c : string.toCharArray()) {
            if (set.contains(c)) {
                count++;
                if (count == minCount) {
                    break;
                }
            }
        }
        junit.framework.Assert.assertEquals(string, minCount, count);
    }
}