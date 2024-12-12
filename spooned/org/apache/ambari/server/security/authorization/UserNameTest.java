package org.apache.ambari.server.security.authorization;
@org.junit.runner.RunWith(org.junit.runners.Parameterized.class)
public class UserNameTest {
    private final java.lang.String name;

    private final boolean valid;

    @org.junit.runners.Parameterized.Parameters
    public static java.util.Collection userNames() {
        return java.util.Arrays.asList(new java.lang.Object[][]{ new java.lang.Object[]{ "", false }, new java.lang.Object[]{ null, false }, new java.lang.Object[]{ "invalid<", false }, new java.lang.Object[]{ ">invalid", false }, new java.lang.Object[]{ "inv&lid", false }, new java.lang.Object[]{ "i\\nvalid", false }, new java.lang.Object[]{ "inva`lid", false }, new java.lang.Object[]{ "inval|d", false }, new java.lang.Object[]{ "user01", true }, new java.lang.Object[]{ "user_name", true } });
    }

    public UserNameTest(java.lang.String name, boolean valid) {
        this.name = name;
        this.valid = valid;
    }

    @org.junit.Test
    public void testRejectsForbiddenCharacters() throws java.lang.Exception {
        try {
            org.junit.Assert.assertEquals(name, org.apache.ambari.server.security.authorization.UserName.fromString(name).toString());
            if (!valid) {
                org.junit.Assert.fail(("Expected user " + name) + " to be invalid.");
            }
        } catch (java.lang.IllegalArgumentException e) {
            if (valid) {
                org.junit.Assert.fail((("Expected user " + name) + " to be valid. But was: ") + e.getMessage());
            }
        }
    }
}