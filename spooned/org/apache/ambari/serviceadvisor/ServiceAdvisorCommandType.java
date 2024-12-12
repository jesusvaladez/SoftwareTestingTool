package org.apache.ambari.serviceadvisor;
public enum ServiceAdvisorCommandType {

    RECOMMEND_COMPONENT_LAYOUT("recommend-component-layout"),
    VALIDATE_COMPONENT_LAYOUT("validate-component-layout"),
    RECOMMEND_CONFIGURATIONS("recommend-configurations"),
    RECOMMEND_CONFIGURATIONS_FOR_SSO("recommend-configurations-for-sso"),
    RECOMMEND_CONFIGURATIONS_FOR_KERBEROS("recommend-configurations-for-kerberos"),
    RECOMMEND_CONFIGURATION_DEPENDENCIES("recommend-configuration-dependencies"),
    VALIDATE_CONFIGURATIONS("validate-configurations");
    private final java.lang.String name;

    private ServiceAdvisorCommandType(java.lang.String name) {
        this.name = name;
    }

    public java.lang.String getValue() {
        return this.name.toLowerCase();
    }

    @java.lang.Override
    public java.lang.String toString() {
        return this.name;
    }

    public static org.apache.ambari.serviceadvisor.ServiceAdvisorCommandType getEnum(java.lang.String name) {
        for (org.apache.ambari.serviceadvisor.ServiceAdvisorCommandType v : org.apache.ambari.serviceadvisor.ServiceAdvisorCommandType.values()) {
            if (v.getValue().equalsIgnoreCase(name.replace("_", "-"))) {
                return v;
            }
        }
        throw new java.lang.IllegalArgumentException();
    }
}