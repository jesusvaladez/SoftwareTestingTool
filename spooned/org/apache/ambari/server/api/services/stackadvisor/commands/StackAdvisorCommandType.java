package org.apache.ambari.server.api.services.stackadvisor.commands;
public enum StackAdvisorCommandType {

    RECOMMEND_COMPONENT_LAYOUT("recommend-component-layout"),
    VALIDATE_COMPONENT_LAYOUT("validate-component-layout"),
    RECOMMEND_CONFIGURATIONS("recommend-configurations"),
    RECOMMEND_CONFIGURATIONS_FOR_SSO("recommend-configurations-for-sso"),
    RECOMMEND_CONFIGURATIONS_FOR_LDAP("recommend-configurations-for-ldap"),
    RECOMMEND_CONFIGURATIONS_FOR_KERBEROS("recommend-configurations-for-kerberos"),
    RECOMMEND_CONFIGURATION_DEPENDENCIES("recommend-configuration-dependencies"),
    VALIDATE_CONFIGURATIONS("validate-configurations");
    private final java.lang.String name;

    StackAdvisorCommandType(java.lang.String name) {
        this.name = name;
    }

    @java.lang.Override
    public java.lang.String toString() {
        return name;
    }
}