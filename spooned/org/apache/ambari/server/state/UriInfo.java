package org.apache.ambari.server.state;
@com.fasterxml.jackson.annotation.JsonInclude(com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY)
public class UriInfo {
    @com.google.gson.annotations.SerializedName("acceptable_codes")
    private java.util.Set<java.lang.Integer> m_acceptableCodes;

    @com.google.gson.annotations.SerializedName("http")
    private java.lang.String m_httpUri;

    @com.google.gson.annotations.SerializedName("https")
    private java.lang.String m_httpsUri;

    @com.google.gson.annotations.SerializedName("https_property")
    private java.lang.String m_httpsProperty;

    @com.google.gson.annotations.SerializedName("https_property_value")
    private java.lang.String m_httpsPropertyValue;

    @com.google.gson.annotations.SerializedName("kerberos_keytab")
    private java.lang.String m_kerberosKeytab;

    @com.google.gson.annotations.SerializedName("kerberos_principal")
    private java.lang.String m_kerberosPrincipal;

    @com.google.gson.annotations.SerializedName("default_port")
    private java.lang.Number m_port = 0;

    @com.google.gson.annotations.SerializedName("connection_timeout")
    @com.fasterxml.jackson.annotation.JsonProperty("connection_timeout")
    private float m_connectionTimeout = 5.0F;

    @com.google.gson.annotations.SerializedName("read_timeout")
    private float readTimeout = 15.0F;

    @com.google.gson.annotations.SerializedName("high_availability")
    private org.apache.ambari.server.state.UriInfo.HighAvailability m_highAvailability;

    @com.fasterxml.jackson.annotation.JsonProperty("http")
    public java.lang.String getHttpUri() {
        return m_httpUri;
    }

    public void setHttpUri(java.lang.String httpUri) {
        m_httpUri = httpUri;
    }

    public void setHttpsUri(java.lang.String httpsUri) {
        this.m_httpsUri = httpsUri;
    }

    public void setHttpsPropertyValue(java.lang.String m_httpsPropertyValue) {
        this.m_httpsPropertyValue = m_httpsPropertyValue;
    }

    public void setHttpsProperty(java.lang.String m_httpsProperty) {
        this.m_httpsProperty = m_httpsProperty;
    }

    @com.fasterxml.jackson.annotation.JsonProperty("default_port")
    public java.lang.Number getDefaultPort() {
        return m_port;
    }

    @com.fasterxml.jackson.annotation.JsonProperty("https")
    public java.lang.String getHttpsUri() {
        return m_httpsUri;
    }

    @com.fasterxml.jackson.annotation.JsonProperty("https_property")
    public java.lang.String getHttpsProperty() {
        return m_httpsProperty;
    }

    @com.fasterxml.jackson.annotation.JsonProperty("https_property_value")
    public java.lang.String getHttpsPropertyValue() {
        return m_httpsPropertyValue;
    }

    @com.fasterxml.jackson.annotation.JsonProperty("kerberos_keytab")
    public java.lang.String getKerberosKeytab() {
        return m_kerberosKeytab;
    }

    @com.fasterxml.jackson.annotation.JsonProperty("kerberos_principal")
    public java.lang.String getKerberosPrincipal() {
        return m_kerberosPrincipal;
    }

    @com.fasterxml.jackson.annotation.JsonProperty("high_availability")
    public org.apache.ambari.server.state.UriInfo.HighAvailability getHighAvailability() {
        return m_highAvailability;
    }

    public java.util.Set<java.lang.Integer> getAcceptableCodes() {
        return m_acceptableCodes;
    }

    public void setAcceptableCodes(java.util.Set<java.lang.Integer> m_acceptableCodes) {
        this.m_acceptableCodes = m_acceptableCodes;
    }

    public class HighAvailability {
        @com.google.gson.annotations.SerializedName("nameservice")
        private java.lang.String m_nameservice;

        @com.google.gson.annotations.SerializedName("alias_key")
        private java.lang.String m_aliasKey;

        @com.google.gson.annotations.SerializedName("http_pattern")
        private java.lang.String m_httpPattern;

        @com.google.gson.annotations.SerializedName("https_pattern")
        private java.lang.String m_httpsPattern;

        @com.fasterxml.jackson.annotation.JsonProperty("nameservice")
        public java.lang.String getNameservice() {
            return m_nameservice;
        }

        @com.fasterxml.jackson.annotation.JsonProperty("alias_key")
        public java.lang.String getAliasKey() {
            return m_aliasKey;
        }

        @com.fasterxml.jackson.annotation.JsonProperty("http_pattern")
        public java.lang.String getHttpPattern() {
            return m_httpPattern;
        }

        @com.fasterxml.jackson.annotation.JsonProperty("https_pattern")
        public java.lang.String getHttpsPattern() {
            return m_httpsPattern;
        }
    }

    public java.net.URI resolve(java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> config) throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.server.state.kerberos.VariableReplacementHelper variableReplacer = new org.apache.ambari.server.state.kerberos.VariableReplacementHelper();
        java.lang.String httpsProperty = variableReplacer.replaceVariables(m_httpsProperty, config);
        java.lang.String httpsPropertyValue = variableReplacer.replaceVariables(m_httpsPropertyValue, config);
        return (httpsProperty == null) || (!httpsProperty.equals(httpsPropertyValue)) ? java.net.URI.create(java.lang.String.format("http://%s", variableReplacer.replaceVariables(m_httpUri, config))) : java.net.URI.create(java.lang.String.format("https://%s", variableReplacer.replaceVariables(m_httpsUri, config)));
    }

    public int getConnectionTimeoutMsec() {
        return ((int) (m_connectionTimeout)) * 1000;
    }

    public int getReadTimeoutMsec() {
        return ((int) (readTimeout)) * 1000;
    }

    @java.lang.Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = (prime * result) + (m_httpUri == null ? 0 : m_httpUri.hashCode());
        result = (prime * result) + (m_httpsProperty == null ? 0 : m_httpsProperty.hashCode());
        result = (prime * result) + (m_httpsPropertyValue == null ? 0 : m_httpsPropertyValue.hashCode());
        result = (prime * result) + (m_httpsUri == null ? 0 : m_httpsUri.hashCode());
        result = (prime * result) + m_port.intValue();
        return result;
    }

    @java.lang.Override
    public boolean equals(java.lang.Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        org.apache.ambari.server.state.UriInfo other = ((org.apache.ambari.server.state.UriInfo) (obj));
        if (m_httpUri == null) {
            if (other.m_httpUri != null) {
                return false;
            }
        } else if (!m_httpUri.equals(other.m_httpUri)) {
            return false;
        }
        if (m_httpsProperty == null) {
            if (other.m_httpsProperty != null) {
                return false;
            }
        } else if (!m_httpsProperty.equals(other.m_httpsProperty)) {
            return false;
        }
        if (m_httpsPropertyValue == null) {
            if (other.m_httpsPropertyValue != null) {
                return false;
            }
        } else if (!m_httpsPropertyValue.equals(other.m_httpsPropertyValue)) {
            return false;
        }
        if (m_httpsUri == null) {
            if (other.m_httpsUri != null) {
                return false;
            }
        } else if (!m_httpsUri.equals(other.m_httpsUri)) {
            return false;
        }
        if (m_connectionTimeout != other.m_connectionTimeout) {
            return false;
        }
        if (m_port.intValue() != other.m_port.intValue()) {
            return false;
        }
        return true;
    }
}