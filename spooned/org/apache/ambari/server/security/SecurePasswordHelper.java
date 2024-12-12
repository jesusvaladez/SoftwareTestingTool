package org.apache.ambari.server.security;
@com.google.inject.Singleton
public class SecurePasswordHelper {
    public static final int DEFAULT_SECURE_PASSWORD_LENGTH = 18;

    public static final int DEFAULT_SECURE_PASSWORD_MIN_LOWERCASE_LETTERS = 1;

    public static final int DEFAULT_SECURE_PASSWORD_MIN_UPPERCASE_LETTERS = 1;

    public static final int DEFAULT_SECURE_PASSWORD_MIN_DIGITS = 1;

    public static final int DEFAULT_SECURE_PASSWORD_MIN_PUNCTUATION = 1;

    public static final int DEFAULT_SECURE_PASSWORD_MIN_WHITESPACE = 1;

    static final char[] SECURE_PASSWORD_CHARACTER_CLASS_LOWERCASE_LETTERS = "abcdefghijklmnopqrstuvwxyz".toCharArray();

    static final char[] SECURE_PASSWORD_CHARACTER_CLASS_UPPERCASE_LETTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();

    static final char[] SECURE_PASSWORD_CHARACTER_CLASS_DIGITS = "0123456789".toCharArray();

    static final char[] SECURE_PASSWORD_CHARACTER_CLASS_PUNCTUATION = "?.!$%^*()-_+=~".toCharArray();

    static final char[] SECURE_PASSWORD_CHARACTER_CLASS_WHITESPACE = " ".toCharArray();

    private static final char[][] SECURE_PASSWORD_CHARACTER_CLASSES = new char[][]{ org.apache.ambari.server.security.SecurePasswordHelper.SECURE_PASSWORD_CHARACTER_CLASS_LOWERCASE_LETTERS, org.apache.ambari.server.security.SecurePasswordHelper.SECURE_PASSWORD_CHARACTER_CLASS_UPPERCASE_LETTERS, org.apache.ambari.server.security.SecurePasswordHelper.SECURE_PASSWORD_CHARACTER_CLASS_DIGITS, org.apache.ambari.server.security.SecurePasswordHelper.SECURE_PASSWORD_CHARACTER_CLASS_PUNCTUATION, org.apache.ambari.server.security.SecurePasswordHelper.SECURE_PASSWORD_CHARACTER_CLASS_WHITESPACE };

    private final java.security.SecureRandom secureRandom = new java.security.SecureRandom();

    public java.lang.String createSecurePassword() {
        return createSecurePassword(null, null, null, null, null, null);
    }

    public java.lang.String createSecurePassword(java.lang.Integer length, java.lang.Integer minLowercaseLetters, java.lang.Integer minUppercaseLetters, java.lang.Integer minDigits, java.lang.Integer minPunctuation, java.lang.Integer minWhitespace) {
        if ((length == null) || (length < 1)) {
            length = org.apache.ambari.server.security.SecurePasswordHelper.DEFAULT_SECURE_PASSWORD_LENGTH;
        }
        if (minLowercaseLetters == null) {
            minLowercaseLetters = org.apache.ambari.server.security.SecurePasswordHelper.DEFAULT_SECURE_PASSWORD_MIN_LOWERCASE_LETTERS;
        }
        if (minUppercaseLetters == null) {
            minUppercaseLetters = org.apache.ambari.server.security.SecurePasswordHelper.DEFAULT_SECURE_PASSWORD_MIN_UPPERCASE_LETTERS;
        }
        if (minDigits == null) {
            minDigits = org.apache.ambari.server.security.SecurePasswordHelper.DEFAULT_SECURE_PASSWORD_MIN_DIGITS;
        }
        if (minPunctuation == null) {
            minPunctuation = org.apache.ambari.server.security.SecurePasswordHelper.DEFAULT_SECURE_PASSWORD_MIN_PUNCTUATION;
        }
        if (minWhitespace == null) {
            minWhitespace = org.apache.ambari.server.security.SecurePasswordHelper.DEFAULT_SECURE_PASSWORD_MIN_WHITESPACE;
        }
        java.util.List<java.lang.Character> characters = new java.util.ArrayList<>(length);
        for (int i = 0; i < minLowercaseLetters; i++) {
            characters.add(org.apache.ambari.server.security.SecurePasswordHelper.SECURE_PASSWORD_CHARACTER_CLASS_LOWERCASE_LETTERS[secureRandom.nextInt(org.apache.ambari.server.security.SecurePasswordHelper.SECURE_PASSWORD_CHARACTER_CLASS_LOWERCASE_LETTERS.length)]);
        }
        for (int i = 0; i < minUppercaseLetters; i++) {
            characters.add(org.apache.ambari.server.security.SecurePasswordHelper.SECURE_PASSWORD_CHARACTER_CLASS_UPPERCASE_LETTERS[secureRandom.nextInt(org.apache.ambari.server.security.SecurePasswordHelper.SECURE_PASSWORD_CHARACTER_CLASS_UPPERCASE_LETTERS.length)]);
        }
        for (int i = 0; i < minDigits; i++) {
            characters.add(org.apache.ambari.server.security.SecurePasswordHelper.SECURE_PASSWORD_CHARACTER_CLASS_DIGITS[secureRandom.nextInt(org.apache.ambari.server.security.SecurePasswordHelper.SECURE_PASSWORD_CHARACTER_CLASS_DIGITS.length)]);
        }
        for (int i = 0; i < minPunctuation; i++) {
            characters.add(org.apache.ambari.server.security.SecurePasswordHelper.SECURE_PASSWORD_CHARACTER_CLASS_PUNCTUATION[secureRandom.nextInt(org.apache.ambari.server.security.SecurePasswordHelper.SECURE_PASSWORD_CHARACTER_CLASS_PUNCTUATION.length)]);
        }
        for (int i = 0; i < minWhitespace; i++) {
            characters.add(org.apache.ambari.server.security.SecurePasswordHelper.SECURE_PASSWORD_CHARACTER_CLASS_WHITESPACE[secureRandom.nextInt(org.apache.ambari.server.security.SecurePasswordHelper.SECURE_PASSWORD_CHARACTER_CLASS_WHITESPACE.length)]);
        }
        if (characters.size() < length) {
            int difference = length - characters.size();
            for (int i = 0; i < difference; i++) {
                char[] characterClass = org.apache.ambari.server.security.SecurePasswordHelper.SECURE_PASSWORD_CHARACTER_CLASSES[secureRandom.nextInt(org.apache.ambari.server.security.SecurePasswordHelper.SECURE_PASSWORD_CHARACTER_CLASSES.length - 1)];
                characters.add(characterClass[secureRandom.nextInt(characterClass.length)]);
            }
        }
        java.lang.StringBuilder passwordBuilder = new java.lang.StringBuilder(characters.size());
        while (!characters.isEmpty()) {
            passwordBuilder.append(characters.remove(secureRandom.nextInt(characters.size())));
        } 
        return passwordBuilder.toString();
    }
}