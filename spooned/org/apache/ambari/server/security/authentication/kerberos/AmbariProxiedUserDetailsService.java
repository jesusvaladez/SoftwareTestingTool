package org.apache.ambari.server.security.authentication.kerberos;
import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.security.authentication.util.KerberosName;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
@org.springframework.stereotype.Component
public class AmbariProxiedUserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {
    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.security.authentication.kerberos.AmbariProxiedUserDetailsService.class);

    private static final java.util.regex.Pattern IP_ADDRESS_PATTERN = java.util.regex.Pattern.compile("^(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)(?:\\.(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)){3}$");

    private static final java.util.regex.Pattern IP_ADDRESS_RANGE_PATTERN = java.util.regex.Pattern.compile("^((?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)(?:\\.(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)){3})/(\\d{1,2})$");

    @javax.inject.Inject
    private com.google.inject.Provider<org.apache.ambari.server.security.authentication.tproxy.AmbariTProxyConfiguration> ambariTProxyConfigurationProvider;

    private final org.apache.ambari.server.configuration.Configuration configuration;

    private final org.apache.ambari.server.security.authorization.Users users;

    AmbariProxiedUserDetailsService(org.apache.ambari.server.configuration.Configuration configuration, org.apache.ambari.server.security.authorization.Users users) {
        this.configuration = configuration;
        this.users = users;
    }

    @java.lang.Override
    public org.springframework.security.core.userdetails.UserDetails loadUserByUsername(java.lang.String proxiedUserName) throws org.springframework.security.core.userdetails.UsernameNotFoundException {
        return loadProxiedUser(proxiedUserName, null, null);
    }

    public org.springframework.security.core.userdetails.UserDetails loadProxiedUser(java.lang.String proxiedUserName, java.lang.String proxyUserName, org.apache.ambari.server.security.authentication.tproxy.TrustedProxyAuthenticationDetails trustedProxyAuthenticationDetails) throws org.springframework.security.core.AuthenticationException {
        org.apache.ambari.server.security.authentication.kerberos.AmbariProxiedUserDetailsService.LOG.info("Proxy user {} specified {} as proxied user.", proxyUserName, proxiedUserName);
        if (org.apache.commons.lang.StringUtils.isEmpty(proxiedUserName)) {
            java.lang.String message = "No proxied username was specified.";
            org.apache.ambari.server.security.authentication.kerberos.AmbariProxiedUserDetailsService.LOG.warn(message);
            throw new org.springframework.security.core.userdetails.UsernameNotFoundException(message);
        }
        if (trustedProxyAuthenticationDetails == null) {
            java.lang.String message = "Trusted proxy details have not been provided.";
            org.apache.ambari.server.security.authentication.kerberos.AmbariProxiedUserDetailsService.LOG.warn(message);
            throw new org.apache.ambari.server.security.authentication.tproxy.TrustedProxyAuthenticationNotAllowedException(message);
        }
        org.apache.ambari.server.security.authentication.tproxy.AmbariTProxyConfiguration tProxyConfiguration = ambariTProxyConfigurationProvider.get();
        if (!tProxyConfiguration.isEnabled()) {
            java.lang.String message = "Trusted proxy support is not enabled.";
            org.apache.ambari.server.security.authentication.kerberos.AmbariProxiedUserDetailsService.LOG.warn(message);
            throw new org.apache.ambari.server.security.authentication.tproxy.TrustedProxyAuthenticationNotAllowedException(message);
        }
        if (!validateHost(tProxyConfiguration, proxyUserName, trustedProxyAuthenticationDetails.getRemoteAddress())) {
            java.lang.String message = java.lang.String.format("Trusted proxy is not allowed for %s -> %s: host match not found.", proxyUserName, proxiedUserName);
            org.apache.ambari.server.security.authentication.kerberos.AmbariProxiedUserDetailsService.LOG.warn(message);
            throw new org.apache.ambari.server.security.authentication.tproxy.TrustedProxyAuthenticationNotAllowedException(message);
        }
        if (!validateUser(tProxyConfiguration, proxyUserName, proxiedUserName)) {
            java.lang.String message = java.lang.String.format("Trusted proxy is not allowed for %s -> %s: user match not found.", proxyUserName, proxiedUserName);
            org.apache.ambari.server.security.authentication.kerberos.AmbariProxiedUserDetailsService.LOG.warn(message);
            throw new org.apache.ambari.server.security.authentication.tproxy.TrustedProxyAuthenticationNotAllowedException(message);
        }
        org.apache.ambari.server.orm.entities.UserEntity userEntity = users.getUserEntity(proxiedUserName);
        if (userEntity == null) {
            java.lang.String message = java.lang.String.format("Failed to find an account for the proxied user, %s.", proxiedUserName);
            org.apache.ambari.server.security.authentication.kerberos.AmbariProxiedUserDetailsService.LOG.warn(message);
            throw new org.springframework.security.core.userdetails.UsernameNotFoundException(message);
        }
        if (!validateGroup(tProxyConfiguration, proxyUserName, userEntity)) {
            java.lang.String message = java.lang.String.format("Trusted proxy is not allowed for %s -> %s: group match not found.", proxyUserName, proxiedUserName);
            org.apache.ambari.server.security.authentication.kerberos.AmbariProxiedUserDetailsService.LOG.warn(message);
            throw new org.apache.ambari.server.security.authentication.tproxy.TrustedProxyAuthenticationNotAllowedException(message);
        }
        return createUserDetails(userEntity);
    }

    boolean validateGroup(org.apache.ambari.server.security.authentication.tproxy.AmbariTProxyConfiguration tProxyConfiguration, java.lang.String proxyUserName, org.apache.ambari.server.orm.entities.UserEntity userEntity) {
        java.lang.String allowedGroups = tProxyConfiguration.getAllowedGroups(proxyUserName);
        if (org.apache.commons.lang.StringUtils.isNotEmpty(allowedGroups)) {
            java.util.Set<java.lang.String> groupSpecs = java.util.Arrays.stream(allowedGroups.split("\\s*,\\s*")).map(s -> s.trim().toLowerCase()).collect(java.util.stream.Collectors.toSet());
            if (groupSpecs.contains("*")) {
                return true;
            } else {
                java.util.Set<org.apache.ambari.server.orm.entities.MemberEntity> memberEntities = userEntity.getMemberEntities();
                if (memberEntities != null) {
                    for (org.apache.ambari.server.orm.entities.MemberEntity memberEntity : memberEntities) {
                        org.apache.ambari.server.orm.entities.GroupEntity group = memberEntity.getGroup();
                        if (group != null) {
                            java.lang.String groupName = group.getGroupName();
                            if (org.apache.commons.lang.StringUtils.isNotEmpty(groupName) && groupSpecs.contains(groupName.toLowerCase())) {
                                return true;
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    boolean validateUser(org.apache.ambari.server.security.authentication.tproxy.AmbariTProxyConfiguration tProxyConfiguration, java.lang.String proxyUserName, java.lang.String proxiedUserName) {
        java.lang.String allowedUsers = tProxyConfiguration.getAllowedUsers(proxyUserName);
        if (org.apache.commons.lang.StringUtils.isNotEmpty(allowedUsers)) {
            java.lang.String[] userSpecs = allowedUsers.split("\\s*,\\s*");
            for (java.lang.String userSpec : userSpecs) {
                if ("*".equals(userSpec) || userSpec.equalsIgnoreCase(proxiedUserName)) {
                    return true;
                }
            }
        }
        return false;
    }

    boolean validateHost(org.apache.ambari.server.security.authentication.tproxy.AmbariTProxyConfiguration tProxyConfiguration, java.lang.String proxyUserName, java.lang.String remoteAddress) {
        java.lang.String allowedHosts = tProxyConfiguration.getAllowedHosts(proxyUserName);
        if (org.apache.commons.lang.StringUtils.isNotEmpty(allowedHosts)) {
            java.util.Set<java.lang.String> hostSpecs = java.util.Arrays.stream(allowedHosts.split("\\s*,\\s*")).map(s -> s.trim().toLowerCase()).collect(java.util.stream.Collectors.toSet());
            if (hostSpecs.contains("*")) {
                return true;
            } else {
                for (java.lang.String hostSpec : hostSpecs) {
                    if (isIPAddress(hostSpec)) {
                        if (hostSpec.equals(remoteAddress)) {
                            return true;
                        }
                    } else if (isIPAddressRange(hostSpec)) {
                        if (isInIpAddressRange(hostSpec, remoteAddress)) {
                            return true;
                        }
                    } else if (matchesHostname(hostSpec, remoteAddress)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    boolean matchesHostname(java.lang.String hostSpec, java.lang.String remoteAddress) {
        try {
            java.lang.String ipAddress = getIpAddress(hostSpec);
            if (org.apache.commons.lang.StringUtils.isNotEmpty(ipAddress) && ipAddress.equals(remoteAddress)) {
                return true;
            }
        } catch (java.lang.Throwable t) {
            org.apache.ambari.server.security.authentication.kerberos.AmbariProxiedUserDetailsService.LOG.warn("Invalid hostname in host specification, skipping: " + hostSpec, t);
        }
        return false;
    }

    java.lang.String getIpAddress(java.lang.String hostname) throws java.net.UnknownHostException {
        java.net.InetAddress inetAddress = java.net.InetAddress.getByName(hostname);
        return inetAddress == null ? null : inetAddress.getHostAddress();
    }

    boolean isInIpAddressRange(java.lang.String cidr, java.lang.String ipAddress) {
        java.util.regex.Matcher matcher = org.apache.ambari.server.security.authentication.kerberos.AmbariProxiedUserDetailsService.IP_ADDRESS_RANGE_PATTERN.matcher(cidr);
        if (matcher.matches() && (matcher.groupCount() == 2)) {
            try {
                java.lang.String hostSpecIPAddress = matcher.group(1);
                java.lang.String hostSpecBits = matcher.group(2);
                int hostSpecIPAddressInt = ipAddressToInt(hostSpecIPAddress);
                int remoteAddressInt = ipAddressToInt(ipAddress);
                int mask = (-1) << (32 - java.lang.Integer.valueOf(hostSpecBits));
                return (hostSpecIPAddressInt & mask) == (remoteAddressInt & mask);
            } catch (java.lang.Throwable t) {
                org.apache.ambari.server.security.authentication.kerberos.AmbariProxiedUserDetailsService.LOG.warn("Invalid CIDR in host specification, skipping: " + cidr, t);
            }
        }
        return false;
    }

    private int ipAddressToInt(java.lang.String s) throws java.net.UnknownHostException {
        java.net.InetAddress inetAddress = java.net.InetAddress.getByName(s);
        byte[] b = inetAddress.getAddress();
        return ((((b[0] & 0xff) << 24) | ((b[1] & 0xff) << 16)) | ((b[2] & 0xff) << 8)) | ((b[3] & 0xff) << 0);
    }

    private boolean isIPAddressRange(java.lang.String hostSpec) {
        return org.apache.ambari.server.security.authentication.kerberos.AmbariProxiedUserDetailsService.IP_ADDRESS_RANGE_PATTERN.matcher(hostSpec).matches();
    }

    private boolean isIPAddress(java.lang.String hostSpec) {
        return org.apache.ambari.server.security.authentication.kerberos.AmbariProxiedUserDetailsService.IP_ADDRESS_PATTERN.matcher(hostSpec).matches();
    }

    private org.springframework.security.core.userdetails.UserDetails createUserDetails(org.apache.ambari.server.orm.entities.UserEntity userEntity) {
        java.lang.String username = userEntity.getUserName();
        try {
            users.validateLogin(userEntity, username);
        } catch (org.apache.ambari.server.security.authentication.AccountDisabledException | org.apache.ambari.server.security.authentication.TooManyLoginFailuresException e) {
            if (configuration.showLockedOutUserMessage()) {
                throw e;
            } else {
                throw new org.apache.ambari.server.security.authentication.InvalidUsernamePasswordCombinationException(username, false, e);
            }
        }
        return new org.apache.ambari.server.security.authentication.AmbariUserDetailsImpl(new org.apache.ambari.server.security.authorization.User(userEntity), null, users.getUserAuthorities(userEntity));
    }
}