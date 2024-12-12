package org.apache.ambari.server.security.authentication;
import org.springframework.security.core.userdetails.UserDetails;
public interface AmbariUserDetails extends org.springframework.security.core.userdetails.UserDetails {
    java.lang.Integer getUserId();
}