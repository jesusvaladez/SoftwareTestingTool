package org.apache.ambari.server.utils;
public class EventBusSynchronizer {
    public static void synchronizeAmbariEventPublisher(com.google.inject.Binder binder) {
        com.google.common.eventbus.EventBus synchronizedBus = new com.google.common.eventbus.EventBus();
        org.apache.ambari.server.events.publishers.AmbariEventPublisher ambariEventPublisher = new org.apache.ambari.server.events.publishers.AmbariEventPublisher();
        org.apache.ambari.server.utils.EventBusSynchronizer.replaceEventBus(org.apache.ambari.server.events.publishers.AmbariEventPublisher.class, ambariEventPublisher, synchronizedBus);
        binder.bind(org.apache.ambari.server.events.publishers.AmbariEventPublisher.class).toInstance(ambariEventPublisher);
    }

    public static com.google.common.eventbus.EventBus synchronizeAmbariEventPublisher(com.google.inject.Injector injector) {
        com.google.common.eventbus.EventBus synchronizedBus = new com.google.common.eventbus.EventBus();
        org.apache.ambari.server.events.publishers.AmbariEventPublisher publisher = injector.getInstance(org.apache.ambari.server.events.publishers.AmbariEventPublisher.class);
        org.apache.ambari.server.utils.EventBusSynchronizer.replaceEventBus(org.apache.ambari.server.events.publishers.AmbariEventPublisher.class, publisher, synchronizedBus);
        org.apache.ambari.server.utils.EventBusSynchronizer.registerAmbariListeners(injector, synchronizedBus);
        return synchronizedBus;
    }

    public static com.google.common.eventbus.EventBus synchronizeAlertEventPublisher(com.google.inject.Injector injector) {
        com.google.common.eventbus.EventBus synchronizedBus = new com.google.common.eventbus.EventBus();
        org.apache.ambari.server.events.publishers.AlertEventPublisher publisher = injector.getInstance(org.apache.ambari.server.events.publishers.AlertEventPublisher.class);
        org.apache.ambari.server.utils.EventBusSynchronizer.replaceEventBus(org.apache.ambari.server.events.publishers.AlertEventPublisher.class, publisher, synchronizedBus);
        org.apache.ambari.server.utils.EventBusSynchronizer.registerAlertListeners(injector, synchronizedBus);
        return synchronizedBus;
    }

    public static void synchronizeSTOMPUpdatePublisher(com.google.inject.Injector injector) {
        com.google.common.eventbus.EventBus agentEventBus = new com.google.common.eventbus.EventBus();
        com.google.common.eventbus.EventBus apiEventBus = new com.google.common.eventbus.EventBus();
        org.apache.ambari.server.events.publishers.STOMPUpdatePublisher publisher = injector.getInstance(org.apache.ambari.server.events.publishers.STOMPUpdatePublisher.class);
        org.apache.ambari.server.utils.EventBusSynchronizer.replaceSTOMPEventBuses(org.apache.ambari.server.events.publishers.STOMPUpdatePublisher.class, publisher, agentEventBus, apiEventBus);
        org.apache.ambari.server.utils.EventBusSynchronizer.registerSTOMPApiListeners(injector, apiEventBus);
    }

    private static void registerAmbariListeners(com.google.inject.Injector injector, com.google.common.eventbus.EventBus synchronizedBus) {
        synchronizedBus.register(injector.getInstance(org.apache.ambari.server.events.listeners.alerts.AlertMaintenanceModeListener.class));
        synchronizedBus.register(injector.getInstance(org.apache.ambari.server.events.listeners.alerts.AlertLifecycleListener.class));
        synchronizedBus.register(injector.getInstance(org.apache.ambari.server.events.listeners.alerts.AlertServiceStateListener.class));
        synchronizedBus.register(injector.getInstance(org.apache.ambari.server.events.listeners.upgrade.DistributeRepositoriesActionListener.class));
        synchronizedBus.register(injector.getInstance(org.apache.ambari.server.events.listeners.upgrade.HostVersionOutOfSyncListener.class));
    }

    private static void registerAlertListeners(com.google.inject.Injector injector, com.google.common.eventbus.EventBus synchronizedBus) {
        synchronizedBus.register(injector.getInstance(org.apache.ambari.server.events.listeners.alerts.AlertAggregateListener.class));
        synchronizedBus.register(injector.getInstance(org.apache.ambari.server.events.listeners.alerts.AlertReceivedListener.class));
        synchronizedBus.register(injector.getInstance(org.apache.ambari.server.events.listeners.alerts.AlertStateChangedListener.class));
    }

    private static void registerSTOMPApiListeners(com.google.inject.Injector injector, com.google.common.eventbus.EventBus synchronizedBus) {
        synchronizedBus.register(injector.getInstance(org.apache.ambari.server.events.listeners.services.ServiceUpdateListener.class));
        synchronizedBus.register(injector.getInstance(org.apache.ambari.server.events.listeners.upgrade.UpgradeUpdateListener.class));
    }

    private static void replaceEventBus(java.lang.Class<?> eventPublisherClass, java.lang.Object instance, com.google.common.eventbus.EventBus eventBus) {
        try {
            java.lang.reflect.Field field = eventPublisherClass.getDeclaredField("m_eventBus");
            field.setAccessible(true);
            field.set(instance, eventBus);
        } catch (java.lang.Exception exception) {
            throw new java.lang.RuntimeException(exception);
        }
    }

    private static void replaceSTOMPEventBuses(java.lang.Class<?> eventPublisherClass, java.lang.Object instance, com.google.common.eventbus.EventBus agentEventBus, com.google.common.eventbus.EventBus apiEventBus) {
        try {
            java.lang.reflect.Field agentEventBusField = eventPublisherClass.getDeclaredField("agentEventBus");
            agentEventBusField.setAccessible(true);
            agentEventBusField.set(instance, agentEventBus);
            java.lang.reflect.Field apiEventBusField = eventPublisherClass.getDeclaredField("apiEventBus");
            apiEventBusField.setAccessible(true);
            apiEventBusField.set(instance, apiEventBus);
        } catch (java.lang.Exception exception) {
            throw new java.lang.RuntimeException(exception);
        }
    }
}