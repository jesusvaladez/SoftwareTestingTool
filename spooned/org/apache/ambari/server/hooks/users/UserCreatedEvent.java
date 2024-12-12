package org.apache.ambari.server.hooks.users;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;
public class UserCreatedEvent extends org.apache.ambari.server.events.AmbariEvent {
    private org.apache.ambari.server.hooks.HookContext context;

    @com.google.inject.assistedinject.AssistedInject
    public UserCreatedEvent(@com.google.inject.assistedinject.Assisted
    org.apache.ambari.server.hooks.HookContext context) {
        super(org.apache.ambari.server.events.AmbariEvent.AmbariEventType.USER_CREATED);
        this.context = context;
    }

    public org.apache.ambari.server.hooks.HookContext getContext() {
        return context;
    }
}