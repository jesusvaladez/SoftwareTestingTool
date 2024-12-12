package org.apache.ambari.server.api.services.stackadvisor.commands;
public class ConfigurationValidationCommand extends org.apache.ambari.server.api.services.stackadvisor.commands.StackAdvisorCommand<org.apache.ambari.server.api.services.stackadvisor.validations.ValidationResponse> {
    public ConfigurationValidationCommand(java.io.File recommendationsDir, java.lang.String recommendationsArtifactsLifetime, org.apache.ambari.server.state.ServiceInfo.ServiceAdvisorType serviceAdvisorType, int requestId, org.apache.ambari.server.api.services.stackadvisor.StackAdvisorRunner saRunner, org.apache.ambari.server.api.services.AmbariMetaInfo metaInfo, org.apache.ambari.server.controller.internal.AmbariServerConfigurationHandler ambariServerConfigurationHandler) {
        super(recommendationsDir, recommendationsArtifactsLifetime, serviceAdvisorType, requestId, saRunner, metaInfo, ambariServerConfigurationHandler);
    }

    @java.lang.Override
    protected org.apache.ambari.server.api.services.stackadvisor.commands.StackAdvisorCommandType getCommandType() {
        return org.apache.ambari.server.api.services.stackadvisor.commands.StackAdvisorCommandType.VALIDATE_CONFIGURATIONS;
    }

    @java.lang.Override
    protected void validate(org.apache.ambari.server.api.services.stackadvisor.StackAdvisorRequest request) throws org.apache.ambari.server.api.services.stackadvisor.StackAdvisorException {
        if ((((request.getHosts() == null) || request.getHosts().isEmpty()) || (request.getServices() == null)) || request.getServices().isEmpty()) {
            throw new org.apache.ambari.server.api.services.stackadvisor.StackAdvisorException("Hosts, services and configurations must not be empty");
        }
    }

    @java.lang.Override
    protected org.apache.ambari.server.api.services.stackadvisor.validations.ValidationResponse updateResponse(org.apache.ambari.server.api.services.stackadvisor.StackAdvisorRequest request, org.apache.ambari.server.api.services.stackadvisor.validations.ValidationResponse response) {
        return response;
    }

    @java.lang.Override
    protected java.lang.String getResultFileName() {
        return "configurations-validation.json";
    }
}