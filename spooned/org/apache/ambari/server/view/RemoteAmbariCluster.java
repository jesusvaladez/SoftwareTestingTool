package org.apache.ambari.server.view;
public class RemoteAmbariCluster implements org.apache.ambari.view.cluster.Cluster {
    public static final java.lang.String AMBARI_OR_CLUSTER_ADMIN = "/api/v1/users/%s?privileges/PrivilegeInfo/permission_name=AMBARI.ADMINISTRATOR|" + "(privileges/PrivilegeInfo/permission_name=CLUSTER.ADMINISTRATOR&privileges/PrivilegeInfo/cluster_name=%s)";

    private java.lang.String name;

    private org.apache.ambari.view.AmbariStreamProvider streamProvider;

    private java.lang.String clusterPath;

    private java.lang.String username;

    private final com.google.common.cache.LoadingCache<java.lang.String, com.google.gson.JsonElement> configurationCache = com.google.common.cache.CacheBuilder.newBuilder().expireAfterWrite(10, java.util.concurrent.TimeUnit.SECONDS).build(new com.google.common.cache.CacheLoader<java.lang.String, com.google.gson.JsonElement>() {
        @java.lang.Override
        public com.google.gson.JsonElement load(java.lang.String url) throws java.lang.Exception {
            return readFromUrlJSON(url);
        }
    });

    public RemoteAmbariCluster(org.apache.ambari.server.orm.entities.RemoteAmbariClusterEntity remoteAmbariClusterEntity, org.apache.ambari.server.configuration.Configuration config) throws java.net.MalformedURLException {
        this.name = getClusterName(remoteAmbariClusterEntity);
        this.username = remoteAmbariClusterEntity.getUsername();
        java.net.URL url = new java.net.URL(remoteAmbariClusterEntity.getUrl());
        java.lang.String portString = (url.getPort() == (-1)) ? "" : ":" + url.getPort();
        java.lang.String baseUrl = ((url.getProtocol() + "://") + url.getHost()) + portString;
        this.clusterPath = url.getPath();
        this.streamProvider = new org.apache.ambari.server.view.RemoteAmbariStreamProvider(baseUrl, remoteAmbariClusterEntity.getUsername(), remoteAmbariClusterEntity.getPassword(), config.getRequestConnectTimeout(), config.getRequestReadTimeout());
    }

    private java.lang.String getClusterName(org.apache.ambari.server.orm.entities.RemoteAmbariClusterEntity remoteAmbariClusterEntity) {
        java.lang.String[] urlSplit = remoteAmbariClusterEntity.getUrl().split("/");
        return urlSplit[urlSplit.length - 1];
    }

    public RemoteAmbariCluster(java.lang.String name, java.lang.String clusterPath, org.apache.ambari.view.AmbariStreamProvider streamProvider) {
        this.name = name;
        this.clusterPath = clusterPath;
        this.streamProvider = streamProvider;
    }

    @java.lang.Override
    public java.lang.String getName() {
        return this.name;
    }

    @java.lang.Override
    public java.lang.String getConfigurationValue(java.lang.String type, java.lang.String key) {
        com.google.gson.JsonElement config = null;
        try {
            java.lang.String desiredTag = getDesiredConfig(type);
            if (desiredTag != null) {
                config = configurationCache.get(java.lang.String.format("%s/configurations?(type=%s&tag=%s)", this.clusterPath, type, desiredTag));
            }
        } catch (java.util.concurrent.ExecutionException e) {
            throw new org.apache.ambari.server.view.RemoteAmbariConfigurationReadException("Can't retrieve configuration from Remote Ambari", e);
        }
        if ((config == null) || (!config.isJsonObject()))
            return null;

        com.google.gson.JsonElement items = config.getAsJsonObject().get("items");
        if ((items == null) || (!items.isJsonArray()))
            return null;

        com.google.gson.JsonElement item = items.getAsJsonArray().get(0);
        if ((item == null) || (!item.isJsonObject()))
            return null;

        com.google.gson.JsonElement properties = item.getAsJsonObject().get("properties");
        if ((properties == null) || (!properties.isJsonObject()))
            return null;

        com.google.gson.JsonElement property = properties.getAsJsonObject().get(key);
        if ((property == null) || (!property.isJsonPrimitive()))
            return null;

        return property.getAsJsonPrimitive().getAsString();
    }

    @java.lang.Override
    public java.util.Map<java.lang.String, java.lang.String> getConfigByType(java.lang.String type) {
        com.google.gson.JsonElement config = null;
        try {
            java.lang.String desiredTag = getDesiredConfig(type);
            if (desiredTag != null) {
                config = configurationCache.get(java.lang.String.format("%s/configurations?(type=%s&tag=%s)", this.clusterPath, type, desiredTag));
            }
        } catch (java.util.concurrent.ExecutionException e) {
            throw new org.apache.ambari.server.view.RemoteAmbariConfigurationReadException("Can't retrieve configuration from Remote Ambari", e);
        }
        if ((config == null) || (!config.isJsonObject()))
            return null;

        com.google.gson.JsonElement items = config.getAsJsonObject().get("items");
        if ((items == null) || (!items.isJsonArray()))
            return null;

        com.google.gson.JsonElement item = items.getAsJsonArray().get(0);
        if ((item == null) || (!item.isJsonObject()))
            return null;

        com.google.gson.JsonElement properties = item.getAsJsonObject().get("properties");
        if ((properties == null) || (!properties.isJsonObject()))
            return null;

        java.util.Map<java.lang.String, java.lang.String> retMap = new com.google.gson.Gson().fromJson(properties, new com.google.gson.reflect.TypeToken<java.util.HashMap<java.lang.String, java.lang.String>>() {}.getType());
        return retMap;
    }

    @java.lang.Override
    public java.util.List<java.lang.String> getHostsForServiceComponent(java.lang.String serviceName, java.lang.String componentName) {
        java.lang.String url = java.lang.String.format("%s/services/%s/components/%s?" + "fields=host_components/HostRoles/host_name", this.clusterPath, serviceName, componentName);
        java.util.List<java.lang.String> hosts = new java.util.ArrayList<>();
        try {
            com.google.gson.JsonElement response = configurationCache.get(url);
            if ((response == null) || (!response.isJsonObject()))
                return hosts;

            com.google.gson.JsonElement hostComponents = response.getAsJsonObject().get("host_components");
            if ((hostComponents == null) || (!hostComponents.isJsonArray()))
                return hosts;

            for (com.google.gson.JsonElement element : hostComponents.getAsJsonArray()) {
                com.google.gson.JsonElement hostRoles = element.getAsJsonObject().get("HostRoles");
                java.lang.String hostName = hostRoles.getAsJsonObject().get("host_name").getAsString();
                hosts.add(hostName);
            }
        } catch (java.util.concurrent.ExecutionException e) {
            throw new org.apache.ambari.server.view.RemoteAmbariConfigurationReadException("Can't retrieve host information from Remote Ambari", e);
        }
        return hosts;
    }

    public java.util.Set<java.lang.String> getServices() throws java.io.IOException, org.apache.ambari.view.AmbariHttpException {
        java.util.Set<java.lang.String> services = new java.util.HashSet<>();
        java.lang.String path = this.clusterPath + "?fields=services/ServiceInfo/service_name";
        com.google.gson.JsonElement config = configurationCache.getUnchecked(path);
        if ((config != null) && config.isJsonObject()) {
            com.google.gson.JsonElement items = config.getAsJsonObject().get("services");
            if ((items != null) && items.isJsonArray()) {
                for (com.google.gson.JsonElement item : items.getAsJsonArray()) {
                    com.google.gson.JsonElement serviceInfo = item.getAsJsonObject().get("ServiceInfo");
                    if ((serviceInfo != null) && serviceInfo.isJsonObject()) {
                        java.lang.String serviceName = serviceInfo.getAsJsonObject().get("service_name").getAsString();
                        services.add(serviceName);
                    }
                }
            }
        }
        return services;
    }

    public boolean isAmbariOrClusterAdmin() throws org.apache.ambari.view.AmbariHttpException {
        if (username == null)
            return false;

        java.lang.String url = java.lang.String.format(org.apache.ambari.server.view.RemoteAmbariCluster.AMBARI_OR_CLUSTER_ADMIN, username, name);
        com.google.gson.JsonElement response = configurationCache.getUnchecked(url);
        if ((response != null) && response.isJsonObject()) {
            com.google.gson.JsonElement privileges = response.getAsJsonObject().get("privileges");
            if ((privileges != null) && privileges.isJsonArray()) {
                if (privileges.getAsJsonArray().size() > 0)
                    return true;

            }
        }
        return false;
    }

    private java.lang.String getDesiredConfig(java.lang.String type) throws java.util.concurrent.ExecutionException {
        com.google.gson.JsonElement desiredConfigResponse = configurationCache.get(this.clusterPath + "?fields=services/ServiceInfo,hosts,Clusters");
        if ((desiredConfigResponse == null) || (!desiredConfigResponse.isJsonObject()))
            return null;

        com.google.gson.JsonElement clusters = desiredConfigResponse.getAsJsonObject().get("Clusters");
        if ((clusters == null) || (!clusters.isJsonObject()))
            return null;

        com.google.gson.JsonElement desiredConfig = clusters.getAsJsonObject().get("desired_configs");
        if ((desiredConfig == null) || (!desiredConfig.isJsonObject()))
            return null;

        com.google.gson.JsonElement desiredConfigForType = desiredConfig.getAsJsonObject().get(type);
        if ((desiredConfigForType == null) || (!desiredConfigForType.isJsonObject()))
            return null;

        com.google.gson.JsonElement typeJson = desiredConfigForType.getAsJsonObject().get("tag");
        if ((typeJson == null) || (!typeJson.isJsonPrimitive()))
            return null;

        return typeJson.getAsJsonPrimitive().getAsString();
    }

    private com.google.gson.JsonElement readFromUrlJSON(java.lang.String url) throws java.io.IOException, org.apache.ambari.view.AmbariHttpException {
        java.io.InputStream inputStream = streamProvider.readFrom(url, "GET", ((java.lang.String) (null)), null);
        java.lang.String response = org.apache.commons.io.IOUtils.toString(inputStream);
        return new com.google.gson.JsonParser().parse(response);
    }
}