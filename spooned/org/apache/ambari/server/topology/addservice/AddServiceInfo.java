package org.apache.ambari.server.topology.addservice;
public final class AddServiceInfo {
    private final org.apache.ambari.server.topology.addservice.AddServiceRequest request;

    private final java.lang.String clusterName;

    private final org.apache.ambari.server.controller.internal.Stack stack;

    private final org.apache.ambari.server.state.kerberos.KerberosDescriptor kerberosDescriptor;

    private final java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.util.Set<java.lang.String>>> newServices;

    private final org.apache.ambari.server.controller.internal.RequestStageContainer stages;

    private final org.apache.ambari.server.topology.Configuration config;

    private final org.apache.ambari.server.topology.addservice.LayoutRecommendationInfo recommendationInfo;

    private AddServiceInfo(org.apache.ambari.server.topology.addservice.AddServiceRequest request, java.lang.String clusterName, org.apache.ambari.server.controller.internal.RequestStageContainer stages, org.apache.ambari.server.controller.internal.Stack stack, org.apache.ambari.server.topology.Configuration config, java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.util.Set<java.lang.String>>> newServices, org.apache.ambari.server.state.kerberos.KerberosDescriptor kerberosDescriptor, org.apache.ambari.server.topology.addservice.LayoutRecommendationInfo recommendationInfo) {
        this.request = request;
        this.clusterName = clusterName;
        this.stack = stack;
        this.kerberosDescriptor = kerberosDescriptor;
        this.newServices = newServices;
        this.stages = stages;
        this.config = config;
        this.recommendationInfo = recommendationInfo;
    }

    public org.apache.ambari.server.topology.addservice.AddServiceInfo.Builder toBuilder() {
        return new org.apache.ambari.server.topology.addservice.AddServiceInfo.Builder().setRequest(request).setClusterName(clusterName).setStack(stack).setKerberosDescriptor(kerberosDescriptor).setNewServices(newServices).setStages(stages).setConfig(config).setRecommendationInfo(recommendationInfo);
    }

    public org.apache.ambari.server.topology.addservice.AddServiceInfo withLayoutRecommendation(java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.util.Set<java.lang.String>>> services, org.apache.ambari.server.topology.addservice.LayoutRecommendationInfo recommendation) {
        return toBuilder().setNewServices(services).setRecommendationInfo(recommendation).build();
    }

    public org.apache.ambari.server.topology.addservice.AddServiceInfo withConfig(org.apache.ambari.server.topology.Configuration newConfig) {
        return toBuilder().setConfig(newConfig).build();
    }

    @java.lang.Override
    public java.lang.String toString() {
        return ("AddServiceRequest(" + stages.getId()) + ")";
    }

    public org.apache.ambari.server.topology.addservice.AddServiceRequest getRequest() {
        return request;
    }

    public java.lang.String clusterName() {
        return clusterName;
    }

    public org.apache.ambari.server.controller.internal.RequestStageContainer getStages() {
        return stages;
    }

    public java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.util.Set<java.lang.String>>> newServices() {
        return newServices;
    }

    public org.apache.ambari.server.controller.internal.Stack getStack() {
        return stack;
    }

    public org.apache.ambari.server.topology.Configuration getConfig() {
        return config;
    }

    public java.util.Optional<org.apache.ambari.server.topology.addservice.LayoutRecommendationInfo> getRecommendationInfo() {
        return java.util.Optional.ofNullable(recommendationInfo);
    }

    public java.util.Optional<org.apache.ambari.server.state.kerberos.KerberosDescriptor> getKerberosDescriptor() {
        return java.util.Optional.ofNullable(kerberosDescriptor);
    }

    public java.lang.String describe() {
        int maxServicesToShow = 3;
        java.lang.StringBuilder sb = new java.lang.StringBuilder("Add Services: ").append(newServices.keySet().stream().sorted().limit(maxServicesToShow).collect(java.util.stream.Collectors.joining(", ")));
        if (newServices.size() > maxServicesToShow) {
            sb.append(" and ").append(newServices.size() - maxServicesToShow).append(" more");
        }
        sb.append(" to cluster ").append(clusterName);
        return sb.toString();
    }

    public boolean requiresLayoutRecommendation() {
        return !request.getServices().isEmpty();
    }

    public static class Builder {
        private org.apache.ambari.server.topology.addservice.AddServiceRequest request;

        private java.lang.String clusterName;

        private org.apache.ambari.server.controller.internal.Stack stack;

        private org.apache.ambari.server.state.kerberos.KerberosDescriptor kerberosDescriptor;

        private java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.util.Set<java.lang.String>>> newServices;

        private org.apache.ambari.server.controller.internal.RequestStageContainer stages;

        private org.apache.ambari.server.topology.Configuration config;

        private org.apache.ambari.server.topology.addservice.LayoutRecommendationInfo recommendationInfo;

        public org.apache.ambari.server.topology.addservice.AddServiceInfo build() {
            return new org.apache.ambari.server.topology.addservice.AddServiceInfo(request, clusterName, stages, stack, config, newServices, kerberosDescriptor, recommendationInfo);
        }

        public org.apache.ambari.server.topology.addservice.AddServiceInfo.Builder setRequest(org.apache.ambari.server.topology.addservice.AddServiceRequest request) {
            this.request = request;
            return this;
        }

        public org.apache.ambari.server.topology.addservice.AddServiceInfo.Builder setClusterName(java.lang.String clusterName) {
            this.clusterName = clusterName;
            return this;
        }

        public org.apache.ambari.server.topology.addservice.AddServiceInfo.Builder setStages(org.apache.ambari.server.controller.internal.RequestStageContainer stages) {
            this.stages = stages;
            return this;
        }

        public org.apache.ambari.server.topology.addservice.AddServiceInfo.Builder setStack(org.apache.ambari.server.controller.internal.Stack stack) {
            this.stack = stack;
            return this;
        }

        public org.apache.ambari.server.topology.addservice.AddServiceInfo.Builder setConfig(org.apache.ambari.server.topology.Configuration config) {
            this.config = config;
            return this;
        }

        public org.apache.ambari.server.topology.addservice.AddServiceInfo.Builder setNewServices(java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.util.Set<java.lang.String>>> newServices) {
            this.newServices = newServices;
            return this;
        }

        public org.apache.ambari.server.topology.addservice.AddServiceInfo.Builder setKerberosDescriptor(org.apache.ambari.server.state.kerberos.KerberosDescriptor kerberosDescriptor) {
            this.kerberosDescriptor = kerberosDescriptor;
            return this;
        }

        public org.apache.ambari.server.topology.addservice.AddServiceInfo.Builder setRecommendationInfo(org.apache.ambari.server.topology.addservice.LayoutRecommendationInfo recommendationInfo) {
            this.recommendationInfo = recommendationInfo;
            return this;
        }
    }
}