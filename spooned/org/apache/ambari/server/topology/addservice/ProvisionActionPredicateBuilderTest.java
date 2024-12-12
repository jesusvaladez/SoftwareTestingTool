package org.apache.ambari.server.topology.addservice;
public class ProvisionActionPredicateBuilderTest {
    private static final java.lang.String CLUSTER_NAME = "TEST";

    private static final java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.util.Set<java.lang.String>>> NEW_SERVICES = com.google.common.collect.ImmutableMap.of("AMBARI_METRICS", com.google.common.collect.ImmutableMap.of("METRICS_COLLECTOR", com.google.common.collect.ImmutableSet.of("c7401"), "METRICS_GRAFANA", com.google.common.collect.ImmutableSet.of("c7403"), "METRICS_MONITOR", com.google.common.collect.ImmutableSet.of("c7401", "c7402", "c7403", "c7404", "c7405")), "KAFKA", com.google.common.collect.ImmutableMap.of("KAFKA_BROKER", com.google.common.collect.ImmutableSet.of("c7402", "c7404")), "ZOOKEEPER", com.google.common.collect.ImmutableMap.of("ZOOKEEPER_SERVER", com.google.common.collect.ImmutableSet.of("c7401", "c7402", "c7403"), "ZOOKEEPER_CLIENT", com.google.common.collect.ImmutableSet.of("c7404")));

    private static final org.apache.ambari.server.controller.internal.RequestStageContainer STAGES = new org.apache.ambari.server.controller.internal.RequestStageContainer(42L, null, null, null, null);

    private static final org.apache.ambari.server.topology.addservice.AddServiceInfo.Builder ADD_SERVICE_INFO_BUILDER = new org.apache.ambari.server.topology.addservice.AddServiceInfo.Builder().setClusterName(org.apache.ambari.server.topology.addservice.ProvisionActionPredicateBuilderTest.CLUSTER_NAME).setStages(org.apache.ambari.server.topology.addservice.ProvisionActionPredicateBuilderTest.STAGES).setNewServices(org.apache.ambari.server.topology.addservice.ProvisionActionPredicateBuilderTest.NEW_SERVICES);

    @org.junit.Test
    public void noCustomProvisionAction() {
        org.apache.ambari.server.topology.addservice.AddServiceRequest request = org.apache.ambari.server.topology.addservice.ProvisionActionPredicateBuilderTest.createRequest(null, null, null);
        org.apache.ambari.server.topology.addservice.AddServiceInfo info = org.apache.ambari.server.topology.addservice.ProvisionActionPredicateBuilderTest.ADD_SERVICE_INFO_BUILDER.setRequest(request).build();
        org.apache.ambari.server.topology.addservice.ProvisionActionPredicateBuilder builder = new org.apache.ambari.server.topology.addservice.ProvisionActionPredicateBuilder(info);
        org.junit.Assert.assertTrue(builder.getPredicate(org.apache.ambari.server.topology.ProvisionStep.INSTALL).isPresent());
        org.junit.Assert.assertFalse(builder.getPredicate(org.apache.ambari.server.topology.ProvisionStep.SKIP_INSTALL).isPresent());
        org.junit.Assert.assertTrue(builder.getPredicate(org.apache.ambari.server.topology.ProvisionStep.START).isPresent());
        org.apache.ambari.server.controller.spi.Predicate installPredicate = builder.getPredicate(org.apache.ambari.server.topology.ProvisionStep.INSTALL).get();
        org.apache.ambari.server.controller.spi.Predicate startPredicate = builder.getPredicate(org.apache.ambari.server.topology.ProvisionStep.START).get();
        java.util.Set<org.apache.ambari.server.controller.spi.Resource> allNewHostComponents = org.apache.ambari.server.topology.addservice.ProvisionActionPredicateBuilderTest.allHostComponents(org.apache.ambari.server.topology.addservice.ProvisionActionPredicateBuilderTest.NEW_SERVICES);
        org.apache.ambari.server.topology.addservice.ProvisionActionPredicateBuilderTest.assertMatchesAll(installPredicate, allNewHostComponents);
        org.apache.ambari.server.topology.addservice.ProvisionActionPredicateBuilderTest.assertMatchesAll(startPredicate, allNewHostComponents);
        org.apache.ambari.server.topology.addservice.ProvisionActionPredicateBuilderTest.assertNoMatchForExistingComponents(installPredicate, startPredicate);
    }

    @org.junit.Test
    public void requestLevelStartOnly() {
        org.apache.ambari.server.topology.addservice.AddServiceRequest request = org.apache.ambari.server.topology.addservice.ProvisionActionPredicateBuilderTest.createRequest(org.apache.ambari.server.controller.internal.ProvisionAction.START_ONLY, null, null);
        org.apache.ambari.server.topology.addservice.AddServiceInfo info = org.apache.ambari.server.topology.addservice.ProvisionActionPredicateBuilderTest.ADD_SERVICE_INFO_BUILDER.setRequest(request).build();
        org.apache.ambari.server.topology.addservice.ProvisionActionPredicateBuilder builder = new org.apache.ambari.server.topology.addservice.ProvisionActionPredicateBuilder(info);
        org.junit.Assert.assertEquals(java.util.Optional.empty(), builder.getPredicate(org.apache.ambari.server.topology.ProvisionStep.INSTALL));
        org.junit.Assert.assertTrue(builder.getPredicate(org.apache.ambari.server.topology.ProvisionStep.SKIP_INSTALL).isPresent());
        org.junit.Assert.assertTrue(builder.getPredicate(org.apache.ambari.server.topology.ProvisionStep.START).isPresent());
        org.apache.ambari.server.controller.spi.Predicate skipInstallPredicate = builder.getPredicate(org.apache.ambari.server.topology.ProvisionStep.SKIP_INSTALL).get();
        org.apache.ambari.server.controller.spi.Predicate startPredicate = builder.getPredicate(org.apache.ambari.server.topology.ProvisionStep.START).get();
        java.util.Set<org.apache.ambari.server.controller.spi.Resource> allNewHostComponents = org.apache.ambari.server.topology.addservice.ProvisionActionPredicateBuilderTest.allHostComponents(org.apache.ambari.server.topology.addservice.ProvisionActionPredicateBuilderTest.NEW_SERVICES);
        org.apache.ambari.server.topology.addservice.ProvisionActionPredicateBuilderTest.assertMatchesAll(skipInstallPredicate, allNewHostComponents);
        org.apache.ambari.server.topology.addservice.ProvisionActionPredicateBuilderTest.assertMatchesAll(startPredicate, allNewHostComponents);
        org.apache.ambari.server.topology.addservice.ProvisionActionPredicateBuilderTest.assertNoMatchForExistingComponents(skipInstallPredicate, startPredicate);
    }

    @org.junit.Test
    public void customAtAllLevels() {
        org.apache.ambari.server.topology.addservice.AddServiceRequest request = org.apache.ambari.server.topology.addservice.ProvisionActionPredicateBuilderTest.createRequest(org.apache.ambari.server.controller.internal.ProvisionAction.START_ONLY, com.google.common.collect.ImmutableSet.of(org.apache.ambari.server.topology.addservice.Service.of("AMBARI_METRICS", org.apache.ambari.server.controller.internal.ProvisionAction.INSTALL_AND_START), org.apache.ambari.server.topology.addservice.Service.of("KAFKA"), org.apache.ambari.server.topology.addservice.Service.of("ZOOKEEPER", org.apache.ambari.server.controller.internal.ProvisionAction.INSTALL_ONLY)), com.google.common.collect.ImmutableSet.of(org.apache.ambari.server.topology.addservice.Component.of("KAFKA_BROKER", org.apache.ambari.server.controller.internal.ProvisionAction.INSTALL_AND_START, "c7404"), org.apache.ambari.server.topology.addservice.Component.of("METRICS_GRAFANA", org.apache.ambari.server.controller.internal.ProvisionAction.START_ONLY, "c7403"), org.apache.ambari.server.topology.addservice.Component.of("METRICS_MONITOR", "c7401"), org.apache.ambari.server.topology.addservice.Component.of("METRICS_MONITOR", org.apache.ambari.server.controller.internal.ProvisionAction.INSTALL_AND_START, "c7402"), org.apache.ambari.server.topology.addservice.Component.of("METRICS_MONITOR", org.apache.ambari.server.controller.internal.ProvisionAction.INSTALL_ONLY, "c7404", "c7405")));
        org.apache.ambari.server.topology.addservice.AddServiceInfo info = org.apache.ambari.server.topology.addservice.ProvisionActionPredicateBuilderTest.ADD_SERVICE_INFO_BUILDER.setRequest(request).build();
        org.apache.ambari.server.topology.addservice.ProvisionActionPredicateBuilder builder = new org.apache.ambari.server.topology.addservice.ProvisionActionPredicateBuilder(info);
        org.junit.Assert.assertTrue(builder.getPredicate(org.apache.ambari.server.topology.ProvisionStep.INSTALL).isPresent());
        org.junit.Assert.assertTrue(builder.getPredicate(org.apache.ambari.server.topology.ProvisionStep.SKIP_INSTALL).isPresent());
        org.junit.Assert.assertTrue(builder.getPredicate(org.apache.ambari.server.topology.ProvisionStep.START).isPresent());
        org.apache.ambari.server.controller.spi.Predicate installPredicate = builder.getPredicate(org.apache.ambari.server.topology.ProvisionStep.INSTALL).get();
        org.apache.ambari.server.controller.spi.Predicate skipInstallPredicate = builder.getPredicate(org.apache.ambari.server.topology.ProvisionStep.SKIP_INSTALL).get();
        org.apache.ambari.server.controller.spi.Predicate startPredicate = builder.getPredicate(org.apache.ambari.server.topology.ProvisionStep.START).get();
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.util.Set<java.lang.String>>> installComponents = com.google.common.collect.ImmutableMap.of("AMBARI_METRICS", com.google.common.collect.ImmutableMap.of("METRICS_COLLECTOR", com.google.common.collect.ImmutableSet.of("c7401"), "METRICS_MONITOR", com.google.common.collect.ImmutableSet.of("c7401", "c7402", "c7404", "c7405")), "KAFKA", com.google.common.collect.ImmutableMap.of("KAFKA_BROKER", com.google.common.collect.ImmutableSet.of("c7404")), "ZOOKEEPER", com.google.common.collect.ImmutableMap.of("ZOOKEEPER_SERVER", com.google.common.collect.ImmutableSet.of("c7401", "c7402", "c7403"), "ZOOKEEPER_CLIENT", com.google.common.collect.ImmutableSet.of("c7404")));
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.util.Set<java.lang.String>>> skipInstallComponents = com.google.common.collect.ImmutableMap.of("AMBARI_METRICS", com.google.common.collect.ImmutableMap.of("METRICS_GRAFANA", com.google.common.collect.ImmutableSet.of("c7403")), "KAFKA", com.google.common.collect.ImmutableMap.of("KAFKA_BROKER", com.google.common.collect.ImmutableSet.of("c7402")));
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.util.Set<java.lang.String>>> startComponents = com.google.common.collect.ImmutableMap.of("AMBARI_METRICS", com.google.common.collect.ImmutableMap.of("METRICS_COLLECTOR", com.google.common.collect.ImmutableSet.of("c7401"), "METRICS_GRAFANA", com.google.common.collect.ImmutableSet.of("c7403"), "METRICS_MONITOR", com.google.common.collect.ImmutableSet.of("c7401", "c7402", "c7403")), "KAFKA", com.google.common.collect.ImmutableMap.of("KAFKA_BROKER", com.google.common.collect.ImmutableSet.of("c7402", "c7404")));
        org.apache.ambari.server.topology.addservice.ProvisionActionPredicateBuilderTest.assertMatchesAll(installPredicate, org.apache.ambari.server.topology.addservice.ProvisionActionPredicateBuilderTest.allHostComponents(installComponents));
        org.apache.ambari.server.topology.addservice.ProvisionActionPredicateBuilderTest.assertMatchesAll(skipInstallPredicate, org.apache.ambari.server.topology.addservice.ProvisionActionPredicateBuilderTest.allHostComponents(skipInstallComponents));
        org.apache.ambari.server.topology.addservice.ProvisionActionPredicateBuilderTest.assertMatchesAll(startPredicate, org.apache.ambari.server.topology.addservice.ProvisionActionPredicateBuilderTest.allHostComponents(startComponents));
        org.apache.ambari.server.topology.addservice.ProvisionActionPredicateBuilderTest.assertNoMatchForExistingComponents(skipInstallPredicate, startPredicate);
    }

    private static void assertNoMatchForExistingComponents(org.apache.ambari.server.controller.spi.Predicate... predicates) {
        org.apache.ambari.server.controller.spi.Resource existingComponent = org.apache.ambari.server.topology.addservice.ProvisionActionPredicateBuilderTest.existingComponent();
        for (org.apache.ambari.server.controller.spi.Predicate predicate : predicates) {
            org.junit.Assert.assertFalse(predicate.evaluate(existingComponent));
        }
    }

    private static org.apache.ambari.server.controller.spi.Resource existingComponent() {
        return org.apache.ambari.server.topology.addservice.ProvisionActionPredicateBuilderTest.hostComponent("HDFS", "NAMENODE", "c7401");
    }

    private static void assertMatchesAll(org.apache.ambari.server.controller.spi.Predicate predicates, java.util.Collection<? extends org.apache.ambari.server.controller.spi.Resource> resources) {
        org.junit.Assert.assertEquals(com.google.common.collect.ImmutableSet.of(), resources.stream().filter(each -> !predicates.evaluate(each)).collect(java.util.stream.Collectors.toSet()));
    }

    private static org.apache.ambari.server.topology.addservice.AddServiceRequest createRequest(org.apache.ambari.server.controller.internal.ProvisionAction provisionAction, java.util.Set<org.apache.ambari.server.topology.addservice.Service> services, java.util.Set<org.apache.ambari.server.topology.addservice.Component> components) {
        return new org.apache.ambari.server.topology.addservice.AddServiceRequest(org.apache.ambari.server.topology.addservice.AddServiceRequest.OperationType.ADD_SERVICE, null, provisionAction, org.apache.ambari.server.topology.addservice.AddServiceRequest.ValidationType.DEFAULT, "HDP", "3.0", services, components, null, null, null);
    }

    private static java.util.Set<org.apache.ambari.server.controller.spi.Resource> allHostComponents(java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.util.Set<java.lang.String>>> services) {
        return services.entrySet().stream().flatMap(componentsOfService -> componentsOfService.getValue().entrySet().stream().flatMap(hostsOfComponent -> hostsOfComponent.getValue().stream().map(host -> org.apache.ambari.server.topology.addservice.ProvisionActionPredicateBuilderTest.hostComponent(componentsOfService.getKey(), hostsOfComponent.getKey(), host)))).collect(java.util.stream.Collectors.toSet());
    }

    private static org.apache.ambari.server.controller.spi.Resource hostComponent(java.lang.String service, java.lang.String component, java.lang.String hostname) {
        org.apache.ambari.server.controller.spi.Resource hostComponent = new org.apache.ambari.server.controller.internal.ResourceImpl(org.apache.ambari.server.controller.spi.Resource.Type.HostComponent);
        hostComponent.setProperty(org.apache.ambari.server.controller.internal.HostComponentResourceProvider.CLUSTER_NAME, org.apache.ambari.server.topology.addservice.ProvisionActionPredicateBuilderTest.CLUSTER_NAME);
        hostComponent.setProperty(org.apache.ambari.server.controller.internal.HostComponentResourceProvider.SERVICE_NAME, service);
        hostComponent.setProperty(org.apache.ambari.server.controller.internal.HostComponentResourceProvider.COMPONENT_NAME, component);
        hostComponent.setProperty(org.apache.ambari.server.controller.internal.HostComponentResourceProvider.HOST_NAME, hostname);
        return hostComponent;
    }
}