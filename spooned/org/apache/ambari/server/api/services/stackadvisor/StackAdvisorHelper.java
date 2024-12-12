package org.apache.ambari.server.api.services.stackadvisor;
import org.codehaus.jackson.JsonNode;
@com.google.inject.Singleton
public class StackAdvisorHelper {
    protected static org.apache.commons.logging.Log LOG = org.apache.commons.logging.LogFactory.getLog(org.apache.ambari.server.api.services.stackadvisor.StackAdvisorHelper.class);

    private java.io.File recommendationsDir;

    private java.lang.String recommendationsArtifactsLifetime;

    private int recommendationsArtifactsRolloverMax;

    private final org.apache.ambari.server.api.services.AmbariMetaInfo metaInfo;

    private final org.apache.ambari.server.controller.internal.AmbariServerConfigurationHandler ambariServerConfigurationHandler;

    private final com.google.gson.Gson gson;

    private int requestId = 0;

    private org.apache.ambari.server.api.services.stackadvisor.StackAdvisorRunner saRunner;

    private java.util.Map<java.lang.String, org.codehaus.jackson.JsonNode> hostInfoCache = new java.util.concurrent.ConcurrentHashMap<>();

    private java.util.Map<java.lang.String, org.apache.ambari.server.api.services.stackadvisor.recommendations.RecommendationResponse> configsRecommendationResponse = new java.util.concurrent.ConcurrentHashMap<>();

    @com.google.inject.Inject
    public StackAdvisorHelper(org.apache.ambari.server.configuration.Configuration conf, org.apache.ambari.server.api.services.stackadvisor.StackAdvisorRunner saRunner, org.apache.ambari.server.api.services.AmbariMetaInfo metaInfo, org.apache.ambari.server.controller.internal.AmbariServerConfigurationHandler ambariServerConfigurationHandler, com.google.gson.Gson gson) throws java.io.IOException {
        this.recommendationsDir = conf.getRecommendationsDir();
        this.recommendationsArtifactsLifetime = conf.getRecommendationsArtifactsLifetime();
        this.recommendationsArtifactsRolloverMax = conf.getRecommendationsArtifactsRolloverMax();
        this.saRunner = saRunner;
        this.metaInfo = metaInfo;
        this.ambariServerConfigurationHandler = ambariServerConfigurationHandler;
        this.gson = gson;
    }

    public synchronized org.apache.ambari.server.api.services.stackadvisor.validations.ValidationResponse validate(org.apache.ambari.server.api.services.stackadvisor.StackAdvisorRequest request) throws org.apache.ambari.server.api.services.stackadvisor.StackAdvisorException {
        requestId = generateRequestId();
        java.lang.String serviceName = "ZOOKEEPER";
        org.apache.ambari.server.state.ServiceInfo.ServiceAdvisorType serviceAdvisorType = getServiceAdvisorType(request.getStackName(), request.getStackVersion(), serviceName);
        org.apache.ambari.server.api.services.stackadvisor.commands.StackAdvisorCommand<org.apache.ambari.server.api.services.stackadvisor.validations.ValidationResponse> command = createValidationCommand(serviceName, request);
        return command.invoke(request, serviceAdvisorType);
    }

    org.apache.ambari.server.api.services.stackadvisor.commands.StackAdvisorCommand<org.apache.ambari.server.api.services.stackadvisor.validations.ValidationResponse> createValidationCommand(java.lang.String serviceName, org.apache.ambari.server.api.services.stackadvisor.StackAdvisorRequest request) throws org.apache.ambari.server.api.services.stackadvisor.StackAdvisorException {
        org.apache.ambari.server.api.services.stackadvisor.StackAdvisorRequest.StackAdvisorRequestType requestType = request.getRequestType();
        org.apache.ambari.server.state.ServiceInfo.ServiceAdvisorType serviceAdvisorType = getServiceAdvisorType(request.getStackName(), request.getStackVersion(), serviceName);
        org.apache.ambari.server.api.services.stackadvisor.commands.StackAdvisorCommand<org.apache.ambari.server.api.services.stackadvisor.validations.ValidationResponse> command;
        if (requestType == org.apache.ambari.server.api.services.stackadvisor.StackAdvisorRequest.StackAdvisorRequestType.HOST_GROUPS) {
            command = new org.apache.ambari.server.api.services.stackadvisor.commands.ComponentLayoutValidationCommand(recommendationsDir, recommendationsArtifactsLifetime, serviceAdvisorType, requestId, saRunner, metaInfo, ambariServerConfigurationHandler);
        } else if (requestType == org.apache.ambari.server.api.services.stackadvisor.StackAdvisorRequest.StackAdvisorRequestType.CONFIGURATIONS) {
            command = new org.apache.ambari.server.api.services.stackadvisor.commands.ConfigurationValidationCommand(recommendationsDir, recommendationsArtifactsLifetime, serviceAdvisorType, requestId, saRunner, metaInfo, ambariServerConfigurationHandler);
        } else {
            throw new org.apache.ambari.server.api.services.stackadvisor.StackAdvisorRequestException(java.lang.String.format("Unsupported request type, type=%s", requestType));
        }
        return command;
    }

    public synchronized org.apache.ambari.server.api.services.stackadvisor.recommendations.RecommendationResponse recommend(org.apache.ambari.server.api.services.stackadvisor.StackAdvisorRequest request) throws org.apache.ambari.server.api.services.stackadvisor.StackAdvisorException, org.apache.ambari.server.AmbariException {
        requestId = generateRequestId();
        java.lang.String serviceName = "ZOOKEEPER";
        org.apache.ambari.server.state.ServiceInfo.ServiceAdvisorType serviceAdvisorType = getServiceAdvisorType(request.getStackName(), request.getStackVersion(), serviceName);
        org.apache.ambari.server.api.services.stackadvisor.commands.StackAdvisorCommand<org.apache.ambari.server.api.services.stackadvisor.recommendations.RecommendationResponse> command = createRecommendationCommand(serviceName, request);
        org.apache.ambari.server.api.services.stackadvisor.StackAdvisorRequest.StackAdvisorRequestType requestType = request.getRequestType();
        org.apache.ambari.server.api.services.stackadvisor.recommendations.RecommendationResponse response = null;
        if (requestType == org.apache.ambari.server.api.services.stackadvisor.StackAdvisorRequest.StackAdvisorRequestType.CONFIGURATIONS) {
            java.lang.String hash = getHash(request);
            org.apache.ambari.server.api.services.stackadvisor.StackAdvisorHelper.LOG.info(java.lang.String.format("Calling stack advisor with hash: %s, service: %s", hash, request.getServiceName()));
            response = configsRecommendationResponse.computeIfAbsent(hash, h -> {
                try {
                    org.apache.ambari.server.api.services.stackadvisor.StackAdvisorHelper.LOG.info(java.lang.String.format("Invoking configuration stack advisor command with hash: %s, service: %s", hash, request.getServiceName()));
                    return command.invoke(request, serviceAdvisorType);
                } catch (org.apache.ambari.server.api.services.stackadvisor.StackAdvisorException e) {
                    return null;
                }
            });
        }
        return response == null ? command.invoke(request, serviceAdvisorType) : response;
    }

    protected java.lang.String getHash(org.apache.ambari.server.api.services.stackadvisor.StackAdvisorRequest request) {
        java.lang.String json = gson.toJson(request);
        java.lang.String generatedPassword = null;
        try {
            java.security.MessageDigest md = java.security.MessageDigest.getInstance("SHA-512");
            md.update("".getBytes("UTF-8"));
            byte[] bytes = md.digest(json.getBytes("UTF-8"));
            java.lang.StringBuilder sb = new java.lang.StringBuilder();
            for (byte b : bytes) {
                sb.append(java.lang.Integer.toString((b & 0xff) + 0x100, 16).substring(1));
            }
            generatedPassword = sb.toString();
        } catch (java.security.NoSuchAlgorithmException | java.io.UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return generatedPassword;
    }

    org.apache.ambari.server.api.services.stackadvisor.commands.StackAdvisorCommand<org.apache.ambari.server.api.services.stackadvisor.recommendations.RecommendationResponse> createRecommendationCommand(java.lang.String serviceName, org.apache.ambari.server.api.services.stackadvisor.StackAdvisorRequest request) throws org.apache.ambari.server.api.services.stackadvisor.StackAdvisorException {
        org.apache.ambari.server.api.services.stackadvisor.StackAdvisorRequest.StackAdvisorRequestType requestType = request.getRequestType();
        org.apache.ambari.server.state.ServiceInfo.ServiceAdvisorType serviceAdvisorType = getServiceAdvisorType(request.getStackName(), request.getStackVersion(), serviceName);
        org.apache.ambari.server.api.services.stackadvisor.commands.StackAdvisorCommand<org.apache.ambari.server.api.services.stackadvisor.recommendations.RecommendationResponse> command;
        if (requestType == org.apache.ambari.server.api.services.stackadvisor.StackAdvisorRequest.StackAdvisorRequestType.HOST_GROUPS) {
            command = new org.apache.ambari.server.api.services.stackadvisor.commands.ComponentLayoutRecommendationCommand(recommendationsDir, recommendationsArtifactsLifetime, serviceAdvisorType, requestId, saRunner, metaInfo, ambariServerConfigurationHandler);
        } else if (requestType == org.apache.ambari.server.api.services.stackadvisor.StackAdvisorRequest.StackAdvisorRequestType.CONFIGURATIONS) {
            command = new org.apache.ambari.server.api.services.stackadvisor.commands.ConfigurationRecommendationCommand(org.apache.ambari.server.api.services.stackadvisor.commands.StackAdvisorCommandType.RECOMMEND_CONFIGURATIONS, recommendationsDir, recommendationsArtifactsLifetime, serviceAdvisorType, requestId, saRunner, metaInfo, ambariServerConfigurationHandler, hostInfoCache);
        } else if (requestType == org.apache.ambari.server.api.services.stackadvisor.StackAdvisorRequest.StackAdvisorRequestType.SSO_CONFIGURATIONS) {
            command = new org.apache.ambari.server.api.services.stackadvisor.commands.ConfigurationRecommendationCommand(org.apache.ambari.server.api.services.stackadvisor.commands.StackAdvisorCommandType.RECOMMEND_CONFIGURATIONS_FOR_SSO, recommendationsDir, recommendationsArtifactsLifetime, serviceAdvisorType, requestId, saRunner, metaInfo, ambariServerConfigurationHandler, null);
        } else if (requestType == org.apache.ambari.server.api.services.stackadvisor.StackAdvisorRequest.StackAdvisorRequestType.LDAP_CONFIGURATIONS) {
            command = new org.apache.ambari.server.api.services.stackadvisor.commands.ConfigurationRecommendationCommand(org.apache.ambari.server.api.services.stackadvisor.commands.StackAdvisorCommandType.RECOMMEND_CONFIGURATIONS_FOR_LDAP, recommendationsDir, recommendationsArtifactsLifetime, serviceAdvisorType, requestId, saRunner, metaInfo, ambariServerConfigurationHandler, null);
        } else if (requestType == org.apache.ambari.server.api.services.stackadvisor.StackAdvisorRequest.StackAdvisorRequestType.KERBEROS_CONFIGURATIONS) {
            command = new org.apache.ambari.server.api.services.stackadvisor.commands.ConfigurationRecommendationCommand(org.apache.ambari.server.api.services.stackadvisor.commands.StackAdvisorCommandType.RECOMMEND_CONFIGURATIONS_FOR_KERBEROS, recommendationsDir, recommendationsArtifactsLifetime, serviceAdvisorType, requestId, saRunner, metaInfo, ambariServerConfigurationHandler, null);
        } else if (requestType == org.apache.ambari.server.api.services.stackadvisor.StackAdvisorRequest.StackAdvisorRequestType.CONFIGURATION_DEPENDENCIES) {
            command = new org.apache.ambari.server.api.services.stackadvisor.commands.ConfigurationDependenciesRecommendationCommand(recommendationsDir, recommendationsArtifactsLifetime, serviceAdvisorType, requestId, saRunner, metaInfo, ambariServerConfigurationHandler);
        } else {
            throw new org.apache.ambari.server.api.services.stackadvisor.StackAdvisorRequestException(java.lang.String.format("Unsupported request type, type=%s", requestType));
        }
        return command;
    }

    private org.apache.ambari.server.state.ServiceInfo.ServiceAdvisorType getServiceAdvisorType(java.lang.String stackName, java.lang.String stackVersion, java.lang.String serviceName) {
        try {
            org.apache.ambari.server.state.ServiceInfo service = metaInfo.getService(stackName, stackVersion, serviceName);
            org.apache.ambari.server.state.ServiceInfo.ServiceAdvisorType serviceAdvisorType = service.getServiceAdvisorType();
            return serviceAdvisorType;
        } catch (org.apache.ambari.server.AmbariException e) {
        }
        return null;
    }

    private int generateRequestId() {
        requestId += 1;
        return requestId % recommendationsArtifactsRolloverMax;
    }

    public void clearCaches(java.lang.String hostName) {
        configsRecommendationResponse.clear();
        hostInfoCache.remove(hostName);
        org.apache.ambari.server.api.services.stackadvisor.StackAdvisorHelper.LOG.info("Clear stack advisor caches, host: " + hostName);
    }

    public void clearCaches(java.util.Set<java.lang.String> hostNames) {
        if ((hostNames != null) && (!hostNames.isEmpty())) {
            configsRecommendationResponse.clear();
            for (java.lang.String hostName : hostNames) {
                hostInfoCache.remove(hostName);
            }
        }
        org.apache.ambari.server.api.services.stackadvisor.StackAdvisorHelper.LOG.info("Clear stack advisor caches, hosts: " + hostNames.toString());
    }
}