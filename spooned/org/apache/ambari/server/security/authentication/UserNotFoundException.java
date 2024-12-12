package org.apache.ambari.server.security.authentication;
public class UserNotFoundException extends org.apache.ambari.server.security.authentication.AmbariAuthenticationException {
    public static final java.lang.String MESSAGE = "User does not exist.";

    public UserNotFoundException(java.lang.String userName) {
        super(userName, org.apache.ambari.server.security.authentication.UserNotFoundException.MESSAGE, false);
    }

    public UserNotFoundException(java.lang.String userName, java.lang.Throwable cause) {
        super(userName, org.apache.ambari.server.security.authentication.UserNotFoundException.MESSAGE, false, cause);
    }

    public UserNotFoundException(java.lang.String username, java.lang.String message) {
        super(username, message, false);
    }

    public UserNotFoundException(java.lang.String username, java.lang.String message, java.lang.Throwable throwable) {
        super(username, message, false, throwable);
    }
}