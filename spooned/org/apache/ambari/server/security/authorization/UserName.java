package org.apache.ambari.server.security.authorization;
import org.apache.commons.lang.StringUtils;
public class UserName {
    private static final char[] FORBIDDEN_CHARS = new char[]{ '<', '>', '&', '|', '\\', '`' };

    private final java.lang.String userName;

    public static org.apache.ambari.server.security.authorization.UserName fromString(java.lang.String userName) {
        return new org.apache.ambari.server.security.authorization.UserName(org.apache.ambari.server.security.authorization.UserName.validated(userName));
    }

    private static java.lang.String validated(java.lang.String userName) {
        if (org.apache.commons.lang.StringUtils.isBlank(userName)) {
            throw new java.lang.IllegalArgumentException("Username cannot be empty");
        }
        org.apache.ambari.server.security.authorization.UserName.rejectIfContainsAnyOf(userName, org.apache.ambari.server.security.authorization.UserName.FORBIDDEN_CHARS);
        return userName;
    }

    private static void rejectIfContainsAnyOf(java.lang.String name, char[] forbiddenChars) {
        for (char each : forbiddenChars) {
            if (name.contains(java.lang.Character.toString(each))) {
                throw new java.lang.IllegalArgumentException((("Invalid username: " + name) + " Avoid characters ") + java.util.Arrays.toString(forbiddenChars));
            }
        }
    }

    private UserName(java.lang.String userName) {
        this.userName = userName;
    }

    @java.lang.Override
    public java.lang.String toString() {
        return userName;
    }

    @java.lang.Override
    public boolean equals(java.lang.Object o) {
        if (this == o)
            return true;

        if ((o == null) || (getClass() != o.getClass()))
            return false;

        org.apache.ambari.server.security.authorization.UserName userName1 = ((org.apache.ambari.server.security.authorization.UserName) (o));
        return userName.equals(userName1.userName);
    }

    @java.lang.Override
    public int hashCode() {
        return userName.hashCode();
    }
}