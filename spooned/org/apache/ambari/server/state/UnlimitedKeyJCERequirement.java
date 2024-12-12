package org.apache.ambari.server.state;
public enum UnlimitedKeyJCERequirement {

    ALWAYS,
    KERBEROS_ENABLED,
    NEVER;
    public static final org.apache.ambari.server.state.UnlimitedKeyJCERequirement DEFAULT = org.apache.ambari.server.state.UnlimitedKeyJCERequirement.KERBEROS_ENABLED;
}