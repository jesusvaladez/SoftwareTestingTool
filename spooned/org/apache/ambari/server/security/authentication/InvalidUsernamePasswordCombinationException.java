package org.apache.ambari.server.security.authentication;
public class InvalidUsernamePasswordCombinationException extends org.apache.ambari.server.security.authentication.AmbariAuthenticationException {
    public static final java.lang.String MESSAGE = "Unable to sign in. Invalid username/password combination.";

    public InvalidUsernamePasswordCombinationException(java.lang.String username) {
        super(username, org.apache.ambari.server.security.authentication.InvalidUsernamePasswordCombinationException.MESSAGE, true);
    }

    public InvalidUsernamePasswordCombinationException(java.lang.String username, boolean incrementFailureCount) {
        super(username, org.apache.ambari.server.security.authentication.InvalidUsernamePasswordCombinationException.MESSAGE, incrementFailureCount);
    }

    public InvalidUsernamePasswordCombinationException(java.lang.String username, java.lang.Throwable t) {
        super(username, org.apache.ambari.server.security.authentication.InvalidUsernamePasswordCombinationException.MESSAGE, true, t);
    }

    public InvalidUsernamePasswordCombinationException(java.lang.String username, boolean incrementFailureCount, java.lang.Throwable t) {
        super(username, org.apache.ambari.server.security.authentication.InvalidUsernamePasswordCombinationException.MESSAGE, incrementFailureCount, t);
    }
}