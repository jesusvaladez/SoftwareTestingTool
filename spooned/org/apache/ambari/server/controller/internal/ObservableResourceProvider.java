package org.apache.ambari.server.controller.internal;
public interface ObservableResourceProvider {
    void updateObservers(org.apache.ambari.server.controller.internal.ResourceProviderEvent event);

    void addObserver(org.apache.ambari.server.controller.internal.ResourceProviderObserver observer);
}