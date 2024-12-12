package org.apache.ambari.server.hooks;
public interface AmbariEventFactory {
    @com.google.inject.name.Named("userCreated")
    org.apache.ambari.server.events.AmbariEvent newUserCreatedEvent(org.apache.ambari.server.hooks.HookContext context);
}