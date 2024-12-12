package org.apache.ambari.server.state.repository;
public class StackPackage {
    @com.google.gson.annotations.SerializedName("upgrade-dependencies")
    public org.apache.ambari.server.state.repository.StackPackage.UpgradeDependencies upgradeDependencies;

    public static class UpgradeDependencies {
        java.util.Map<java.lang.String, java.util.List<java.lang.String>> dependencies;
    }

    public static class UpgradeDependencyDeserializer implements com.google.gson.JsonDeserializer<org.apache.ambari.server.state.repository.StackPackage.UpgradeDependencies> {
        @java.lang.Override
        public org.apache.ambari.server.state.repository.StackPackage.UpgradeDependencies deserialize(com.google.gson.JsonElement json, java.lang.reflect.Type typeOfT, com.google.gson.JsonDeserializationContext context) throws com.google.gson.JsonParseException {
            java.lang.reflect.Type mapType = new com.google.gson.reflect.TypeToken<java.util.Map<java.lang.String, java.util.List<java.lang.String>>>() {}.getType();
            java.util.Map<java.lang.String, java.util.List<java.lang.String>> data = context.deserialize(json, mapType);
            org.apache.ambari.server.state.repository.StackPackage.UpgradeDependencies upgradeDependencies = new org.apache.ambari.server.state.repository.StackPackage.UpgradeDependencies();
            upgradeDependencies.dependencies = data;
            return upgradeDependencies;
        }
    }
}