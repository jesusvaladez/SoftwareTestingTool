package org.apache.ambari.server.utils;
@org.apache.ambari.server.StaticallyInject
public class SecretReference {
    private static final java.lang.String secretPrefix = "SECRET";

    private java.lang.String configType;

    private java.lang.Long version;

    private java.lang.String value;

    private static final java.lang.String PASSWORD_TEXT = "password";

    private static final java.lang.String PASSWD_TEXT = "passwd";

    @com.google.inject.Inject
    private static com.google.gson.Gson gson;

    public SecretReference(java.lang.String reference, org.apache.ambari.server.state.Cluster cluster) throws org.apache.ambari.server.AmbariException {
        java.lang.String[] values = reference.split(":");
        configType = values[1];
        version = java.lang.Long.valueOf(values[2]);
        java.lang.String propertyName = values[3];
        java.lang.String clusterName = cluster.getClusterName();
        org.apache.ambari.server.state.Config refConfig = cluster.getConfigByVersion(configType, version);
        if (refConfig == null)
            throw new org.apache.ambari.server.AmbariException(java.lang.String.format("Error when parsing secret reference. Cluster: %s does not contain ConfigType: %s ConfigVersion: %s", clusterName, configType, version));

        java.util.Map<java.lang.String, java.lang.String> refProperties = refConfig.getProperties();
        if (!refProperties.containsKey(propertyName))
            throw new org.apache.ambari.server.AmbariException(java.lang.String.format("Error when parsing secret reference. Cluster: %s ConfigType: %s ConfigVersion: %s does not contain property '%s'", clusterName, configType, version, propertyName));

        this.value = refProperties.get(propertyName);
    }

    public void setConfigType(java.lang.String configType) {
        this.configType = configType;
    }

    public java.lang.Long getVersion() {
        return version;
    }

    public java.lang.String getValue() {
        return value;
    }

    public static boolean isSecret(java.lang.String value) {
        java.lang.String[] values = value.split(":");
        return (values.length == 4) && values[0].equals(org.apache.ambari.server.utils.SecretReference.secretPrefix);
    }

    public static java.lang.String generateStub(java.lang.String configType, java.lang.Long configVersion, java.lang.String propertyName) {
        return (((((org.apache.ambari.server.utils.SecretReference.secretPrefix + ":") + configType) + ":") + configVersion) + ":") + propertyName;
    }

    public static java.lang.String maskPasswordInPropertyMap(java.lang.String propertyMap) {
        if (null == propertyMap) {
            return null;
        }
        final java.util.Map<java.lang.String, java.lang.String> map = org.apache.ambari.server.utils.SecretReference.gson.fromJson(propertyMap, new com.google.gson.reflect.TypeToken<java.util.Map<java.lang.String, java.lang.String>>() {}.getType());
        return org.apache.ambari.server.utils.SecretReference.gson.toJson(org.apache.ambari.server.utils.SecretReference.maskPasswordInPropertyMap(map));
    }

    public static java.util.Map<java.lang.String, java.lang.String> maskPasswordInPropertyMap(java.util.Map<java.lang.String, java.lang.String> propertyMap) {
        if (null == propertyMap) {
            return null;
        }
        final java.util.Map<java.lang.String, java.lang.String> maskedMap = new java.util.HashMap<>();
        for (java.util.Map.Entry<java.lang.String, java.lang.String> property : propertyMap.entrySet()) {
            java.lang.String value = (org.apache.ambari.server.utils.SecretReference.isPassword(property.getKey())) ? org.apache.ambari.server.utils.SecretReference.secretPrefix : property.getValue();
            maskedMap.put(property.getKey(), value);
        }
        return maskedMap;
    }

    private static final boolean isPassword(java.lang.String propertyName) {
        return propertyName.toLowerCase().contains(org.apache.ambari.server.utils.SecretReference.PASSWORD_TEXT) || propertyName.toLowerCase().contains(org.apache.ambari.server.utils.SecretReference.PASSWD_TEXT);
    }

    public static void replaceReferencesWithPasswords(java.util.Map<java.lang.String, java.lang.String> targetMap, org.apache.ambari.server.state.Cluster cluster) throws org.apache.ambari.server.AmbariException {
        if (cluster != null) {
            for (java.util.Map.Entry<java.lang.String, java.lang.String> propertyValueEntry : targetMap.entrySet()) {
                java.lang.String key = propertyValueEntry.getKey();
                java.lang.String value = propertyValueEntry.getValue();
                if ((value != null) && org.apache.ambari.server.utils.SecretReference.isSecret(value)) {
                    org.apache.ambari.server.utils.SecretReference ref = new org.apache.ambari.server.utils.SecretReference(value, cluster);
                    targetMap.put(key, ref.getValue());
                }
            }
        }
    }

    public static void replacePasswordsWithReferences(java.util.Map<org.apache.ambari.server.state.PropertyInfo.PropertyType, java.util.Set<java.lang.String>> propertiesTypes, java.util.Map<java.lang.String, java.lang.String> propertiesMap, java.lang.String configType, java.lang.Long configVersion) {
        if ((propertiesTypes != null) && propertiesTypes.containsKey(org.apache.ambari.server.state.PropertyInfo.PropertyType.PASSWORD)) {
            for (java.lang.String pwdPropertyName : propertiesTypes.get(org.apache.ambari.server.state.PropertyInfo.PropertyType.PASSWORD)) {
                if (propertiesMap.containsKey(pwdPropertyName)) {
                    if (!propertiesMap.get(pwdPropertyName).equals("")) {
                        java.lang.String stub = org.apache.ambari.server.utils.SecretReference.generateStub(configType, configVersion, pwdPropertyName);
                        propertiesMap.put(pwdPropertyName, stub);
                    }
                }
            }
        }
    }

    public static void replacePasswordsWithReferencesForCustomProperties(java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> configAttributes, java.util.Map<java.lang.String, java.lang.String> propertiesMap, java.lang.String configType, java.lang.Long configVersion) {
        if ((configAttributes != null) && configAttributes.containsKey("password")) {
            for (java.lang.String pwdPropertyName : configAttributes.get("password").keySet()) {
                if (propertiesMap.containsKey(pwdPropertyName)) {
                    if (!propertiesMap.get(pwdPropertyName).equals("")) {
                        java.lang.String stub = org.apache.ambari.server.utils.SecretReference.generateStub(configType, configVersion, pwdPropertyName);
                        propertiesMap.put(pwdPropertyName, stub);
                    }
                }
            }
        }
    }
}