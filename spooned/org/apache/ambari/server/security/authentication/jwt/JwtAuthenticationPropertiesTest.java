package org.apache.ambari.server.security.authentication.jwt;
public class JwtAuthenticationPropertiesTest {
    @org.junit.Test
    public void testSetNullAudiences() {
        org.junit.Assert.assertNull(new org.apache.ambari.server.security.authentication.jwt.JwtAuthenticationProperties(java.util.Collections.emptyMap()).getAudiences());
    }

    @org.junit.Test
    public void testSetEmptyAudiences() {
        final java.util.Map<java.lang.String, java.lang.String> configurationMap = new java.util.HashMap<>();
        configurationMap.put(org.apache.ambari.server.configuration.AmbariServerConfigurationKey.SSO_JWT_AUDIENCES.key(), "");
        org.junit.Assert.assertNull(new org.apache.ambari.server.security.authentication.jwt.JwtAuthenticationProperties(configurationMap).getAudiences());
    }

    @org.junit.Test
    public void testSetValidAudiences() {
        final java.lang.String[] expectedAudiences = new java.lang.String[]{ "first", "second", "third" };
        final java.util.Map<java.lang.String, java.lang.String> configurationMap = new java.util.HashMap<>();
        configurationMap.put(org.apache.ambari.server.configuration.AmbariServerConfigurationKey.SSO_JWT_AUDIENCES.key(), "first,second,third");
        final org.apache.ambari.server.security.authentication.jwt.JwtAuthenticationProperties jwtAuthenticationProperties = new org.apache.ambari.server.security.authentication.jwt.JwtAuthenticationProperties(configurationMap);
        org.junit.Assert.assertNotNull(jwtAuthenticationProperties.getAudiences());
        org.junit.Assert.assertArrayEquals(expectedAudiences, jwtAuthenticationProperties.getAudiences().toArray(new java.lang.String[]{  }));
    }
}