package org.apache.ambari.server.topology.addservice;
import static org.apache.ambari.server.controller.internal.HostComponentResourceProvider.CLUSTER_NAME;
import static org.apache.ambari.server.controller.internal.HostComponentResourceProvider.COMPONENT_NAME;
import static org.apache.ambari.server.controller.internal.HostComponentResourceProvider.HOST_NAME;
import static org.apache.ambari.server.controller.internal.HostComponentResourceProvider.SERVICE_NAME;
public class ProvisionActionPredicateBuilder {
    private final java.util.Map<org.apache.ambari.server.topology.ProvisionStep, org.apache.ambari.server.controller.spi.Predicate> predicates = new java.util.EnumMap<>(org.apache.ambari.server.topology.ProvisionStep.class);

    private final org.apache.ambari.server.topology.addservice.AddServiceInfo request;

    private final java.util.Map<java.lang.String, org.apache.ambari.server.controller.internal.ProvisionAction> customServiceActions;

    private final java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.util.Map<org.apache.ambari.server.controller.internal.ProvisionAction, java.util.Set<java.lang.String>>>> customComponentActions;

    private final java.util.Map<org.apache.ambari.server.topology.ProvisionStep, java.util.List<org.apache.ambari.server.controller.spi.Predicate>> servicePredicatesByStep;

    public ProvisionActionPredicateBuilder(org.apache.ambari.server.topology.addservice.AddServiceInfo request) {
        this.request = request;
        customServiceActions = findServicesWithCustomAction();
        customComponentActions = findComponentsWithCustomAction();
        servicePredicatesByStep = createServicePredicates();
        createGlobalPredicates();
    }

    public java.util.Optional<org.apache.ambari.server.controller.spi.Predicate> getPredicate(org.apache.ambari.server.topology.ProvisionStep action) {
        return java.util.Optional.ofNullable(predicates.get(action));
    }

    private void createGlobalPredicates() {
        com.google.common.base.Preconditions.checkState(servicePredicatesByStep != null);
        java.util.function.Function<org.apache.ambari.server.controller.spi.Predicate, org.apache.ambari.server.controller.spi.Predicate> andClusterNameMatches = org.apache.ambari.server.controller.predicate.Predicates.and(org.apache.ambari.server.topology.addservice.ProvisionActionPredicateBuilder.clusterNameIs(request.clusterName()));
        for (java.util.Map.Entry<org.apache.ambari.server.topology.ProvisionStep, java.util.List<org.apache.ambari.server.controller.spi.Predicate>> entry : servicePredicatesByStep.entrySet()) {
            org.apache.ambari.server.topology.ProvisionStep step = entry.getKey();
            java.util.List<org.apache.ambari.server.controller.spi.Predicate> servicePredicates = entry.getValue();
            org.apache.ambari.server.controller.predicate.Predicates.anyOf(servicePredicates).map(andClusterNameMatches).ifPresent(predicate -> predicates.put(step, predicate));
        }
    }

    private java.util.Map<org.apache.ambari.server.topology.ProvisionStep, java.util.List<org.apache.ambari.server.controller.spi.Predicate>> createServicePredicates() {
        com.google.common.base.Preconditions.checkState(customServiceActions != null);
        com.google.common.base.Preconditions.checkState(customComponentActions != null);
        org.apache.ambari.server.controller.internal.ProvisionAction requestAction = request.getRequest().getProvisionAction();
        java.util.Map<org.apache.ambari.server.topology.ProvisionStep, java.util.List<org.apache.ambari.server.controller.spi.Predicate>> servicePredicatesByStep = new java.util.EnumMap<>(org.apache.ambari.server.topology.ProvisionStep.class);
        for (java.util.Map.Entry<java.lang.String, java.util.Map<java.lang.String, java.util.Set<java.lang.String>>> serviceEntry : request.newServices().entrySet()) {
            java.lang.String serviceName = serviceEntry.getKey();
            java.util.Map<java.lang.String, java.util.Set<java.lang.String>> hostsByComponent = serviceEntry.getValue();
            org.apache.ambari.server.controller.internal.ProvisionAction serviceAction = customServiceActions.getOrDefault(serviceName, requestAction);
            org.apache.ambari.server.controller.spi.Predicate serviceNamePredicate = org.apache.ambari.server.topology.addservice.ProvisionActionPredicateBuilder.serviceNameIs(serviceName);
            java.util.Map<java.lang.String, java.util.Map<org.apache.ambari.server.controller.internal.ProvisionAction, java.util.Set<java.lang.String>>> customActionByComponent = customComponentActions.get(serviceName);
            if (customActionByComponent == null) {
                org.apache.ambari.server.topology.addservice.ProvisionActionPredicateBuilder.classifyItem(serviceAction, serviceNamePredicate, servicePredicatesByStep);
            } else {
                java.util.Map<org.apache.ambari.server.topology.ProvisionStep, java.util.List<org.apache.ambari.server.controller.spi.Predicate>> componentPredicatesByStep = org.apache.ambari.server.topology.addservice.ProvisionActionPredicateBuilder.createComponentPredicates(serviceAction, hostsByComponent, customActionByComponent);
                org.apache.ambari.server.topology.addservice.ProvisionActionPredicateBuilder.applyComponentOverrides(servicePredicatesByStep, serviceNamePredicate, componentPredicatesByStep);
            }
        }
        return servicePredicatesByStep;
    }

    private static void applyComponentOverrides(java.util.Map<org.apache.ambari.server.topology.ProvisionStep, java.util.List<org.apache.ambari.server.controller.spi.Predicate>> servicePredicatesByStep, org.apache.ambari.server.controller.spi.Predicate serviceNamePredicate, java.util.Map<org.apache.ambari.server.topology.ProvisionStep, java.util.List<org.apache.ambari.server.controller.spi.Predicate>> componentPredicatesByStep) {
        java.util.function.Function<org.apache.ambari.server.controller.spi.Predicate, org.apache.ambari.server.controller.spi.Predicate> andServiceNameMatches = org.apache.ambari.server.controller.predicate.Predicates.and(serviceNamePredicate);
        for (java.util.Map.Entry<org.apache.ambari.server.topology.ProvisionStep, java.util.List<org.apache.ambari.server.controller.spi.Predicate>> entry : componentPredicatesByStep.entrySet()) {
            org.apache.ambari.server.topology.ProvisionStep step = entry.getKey();
            java.util.List<org.apache.ambari.server.controller.spi.Predicate> componentPredicates = entry.getValue();
            org.apache.ambari.server.controller.predicate.Predicates.anyOf(componentPredicates).map(andServiceNameMatches).ifPresent(predicate -> servicePredicatesByStep.computeIfAbsent(step, __ -> new java.util.LinkedList<>()).add(predicate));
        }
    }

    private static java.util.Map<org.apache.ambari.server.topology.ProvisionStep, java.util.List<org.apache.ambari.server.controller.spi.Predicate>> createComponentPredicates(org.apache.ambari.server.controller.internal.ProvisionAction serviceAction, java.util.Map<java.lang.String, java.util.Set<java.lang.String>> hostsByComponent, java.util.Map<java.lang.String, java.util.Map<org.apache.ambari.server.controller.internal.ProvisionAction, java.util.Set<java.lang.String>>> customActionByComponent) {
        java.util.Map<org.apache.ambari.server.topology.ProvisionStep, java.util.List<org.apache.ambari.server.controller.spi.Predicate>> componentPredicatesByStep = new java.util.EnumMap<>(org.apache.ambari.server.topology.ProvisionStep.class);
        for (java.util.Map.Entry<java.lang.String, java.util.Set<java.lang.String>> componentEntry : hostsByComponent.entrySet()) {
            java.lang.String componentName = componentEntry.getKey();
            java.util.Set<java.lang.String> allHosts = componentEntry.getValue();
            java.util.Map<org.apache.ambari.server.controller.internal.ProvisionAction, java.util.Set<java.lang.String>> hostsByAction = customActionByComponent.getOrDefault(componentName, com.google.common.collect.ImmutableMap.of());
            if (!hostsByAction.isEmpty()) {
                java.util.Set<java.lang.String> customActionHosts = new java.util.HashSet<>();
                for (java.util.Map.Entry<org.apache.ambari.server.controller.internal.ProvisionAction, java.util.Set<java.lang.String>> e : hostsByAction.entrySet()) {
                    org.apache.ambari.server.controller.internal.ProvisionAction componentAction = e.getKey();
                    java.util.Set<java.lang.String> hosts = e.getValue();
                    org.apache.ambari.server.controller.spi.Predicate componentPredicate = org.apache.ambari.server.topology.addservice.ProvisionActionPredicateBuilder.predicateForComponentHosts(componentName, hosts);
                    org.apache.ambari.server.topology.addservice.ProvisionActionPredicateBuilder.classifyItem(componentAction, componentPredicate, componentPredicatesByStep);
                    customActionHosts.addAll(hosts);
                }
                java.util.Set<java.lang.String> leftoverHosts = com.google.common.collect.ImmutableSet.copyOf(com.google.common.collect.Sets.difference(allHosts, customActionHosts));
                if (!leftoverHosts.isEmpty()) {
                    org.apache.ambari.server.controller.spi.Predicate componentPredicate = org.apache.ambari.server.topology.addservice.ProvisionActionPredicateBuilder.predicateForComponentHosts(componentName, leftoverHosts);
                    org.apache.ambari.server.topology.addservice.ProvisionActionPredicateBuilder.classifyItem(serviceAction, componentPredicate, componentPredicatesByStep);
                }
            } else {
                org.apache.ambari.server.controller.spi.Predicate componentPredicate = org.apache.ambari.server.topology.addservice.ProvisionActionPredicateBuilder.componentNameIs(componentName);
                org.apache.ambari.server.topology.addservice.ProvisionActionPredicateBuilder.classifyItem(serviceAction, componentPredicate, componentPredicatesByStep);
            }
        }
        return componentPredicatesByStep;
    }

    private java.util.Map<java.lang.String, java.lang.String> mapServicesByComponent() {
        java.util.Map<java.lang.String, java.lang.String> serviceByComponent = new java.util.HashMap<>();
        for (java.util.Map.Entry<java.lang.String, java.util.Map<java.lang.String, java.util.Set<java.lang.String>>> e : request.newServices().entrySet()) {
            java.lang.String service = e.getKey();
            for (java.lang.String component : e.getValue().keySet()) {
                serviceByComponent.put(component, service);
            }
        }
        return serviceByComponent;
    }

    private java.util.Map<java.lang.String, org.apache.ambari.server.controller.internal.ProvisionAction> findServicesWithCustomAction() {
        org.apache.ambari.server.controller.internal.ProvisionAction requestAction = request.getRequest().getProvisionAction();
        return request.getRequest().getServices().stream().filter(service -> service.getProvisionAction().isPresent()).filter(service -> !java.util.Objects.equals(requestAction, service.getProvisionAction().get())).collect(java.util.stream.Collectors.toMap(org.apache.ambari.server.topology.addservice.Service::getName, service -> service.getProvisionAction().get()));
    }

    private java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.util.Map<org.apache.ambari.server.controller.internal.ProvisionAction, java.util.Set<java.lang.String>>>> findComponentsWithCustomAction() {
        com.google.common.base.Preconditions.checkState(customServiceActions != null);
        java.util.Map<java.lang.String, java.lang.String> serviceByComponent = mapServicesByComponent();
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.util.Map<org.apache.ambari.server.controller.internal.ProvisionAction, java.util.Set<java.lang.String>>>> result = new java.util.HashMap<>();
        for (org.apache.ambari.server.topology.addservice.Component component : request.getRequest().getComponents()) {
            component.getProvisionAction().ifPresent(componentAction -> {
                java.lang.String componentName = component.getName();
                java.lang.String serviceName = serviceByComponent.get(componentName);
                org.apache.ambari.server.controller.internal.ProvisionAction serviceAction = customServiceActions.getOrDefault(serviceName, request.getRequest().getProvisionAction());
                if (!java.util.Objects.equals(serviceAction, componentAction)) {
                    result.computeIfAbsent(serviceName, __ -> new java.util.HashMap<>()).computeIfAbsent(componentName, __ -> new java.util.EnumMap<>(org.apache.ambari.server.controller.internal.ProvisionAction.class)).computeIfAbsent(componentAction, __ -> new java.util.HashSet<>()).addAll(component.getHosts().stream().map(org.apache.ambari.server.topology.addservice.Host::getFqdn).collect(java.util.stream.Collectors.toSet()));
                }
            });
        }
        return result;
    }

    private static <T> void classifyItem(org.apache.ambari.server.controller.internal.ProvisionAction action, T item, java.util.Map<org.apache.ambari.server.topology.ProvisionStep, java.util.List<T>> itemsByStep) {
        for (org.apache.ambari.server.topology.ProvisionStep step : action.getSteps()) {
            itemsByStep.computeIfAbsent(step, __ -> new java.util.LinkedList<>()).add(item);
        }
    }

    private static org.apache.ambari.server.controller.spi.Predicate predicateForComponentHosts(java.lang.String componentName, java.util.Set<java.lang.String> hosts) {
        com.google.common.base.Preconditions.checkNotNull(hosts);
        com.google.common.base.Preconditions.checkArgument(!hosts.isEmpty());
        java.util.Set<org.apache.ambari.server.controller.spi.Predicate> hostPredicates = hosts.stream().map(org.apache.ambari.server.topology.addservice.ProvisionActionPredicateBuilder::hostnameIs).collect(java.util.stream.Collectors.toSet());
        return org.apache.ambari.server.controller.predicate.Predicates.anyOf(hostPredicates).map(org.apache.ambari.server.controller.predicate.Predicates.and(org.apache.ambari.server.topology.addservice.ProvisionActionPredicateBuilder.componentNameIs(componentName))).get();
    }

    private static org.apache.ambari.server.controller.spi.Predicate clusterNameIs(java.lang.String clusterName) {
        return new org.apache.ambari.server.controller.predicate.EqualsPredicate<>(org.apache.ambari.server.controller.internal.HostComponentResourceProvider.CLUSTER_NAME, clusterName);
    }

    private static org.apache.ambari.server.controller.spi.Predicate serviceNameIs(java.lang.String serviceName) {
        return new org.apache.ambari.server.controller.predicate.EqualsPredicate<>(org.apache.ambari.server.controller.internal.HostComponentResourceProvider.SERVICE_NAME, serviceName);
    }

    private static org.apache.ambari.server.controller.spi.Predicate componentNameIs(java.lang.String componentName) {
        return new org.apache.ambari.server.controller.predicate.EqualsPredicate<>(org.apache.ambari.server.controller.internal.HostComponentResourceProvider.COMPONENT_NAME, componentName);
    }

    private static org.apache.ambari.server.controller.spi.Predicate hostnameIs(java.lang.String hostname) {
        return new org.apache.ambari.server.controller.predicate.EqualsPredicate<>(org.apache.ambari.server.controller.internal.HostComponentResourceProvider.HOST_NAME, hostname);
    }
}