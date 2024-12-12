package org.apache.ambari.server.hooks.users;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;
public class PostUserCreationHookContext implements org.apache.ambari.server.hooks.HookContext {
    private java.util.Map<java.lang.String, java.util.Set<java.lang.String>> userGroups = new java.util.HashMap<>();

    @com.google.inject.assistedinject.AssistedInject
    public PostUserCreationHookContext(@com.google.inject.assistedinject.Assisted
    java.util.Map<java.lang.String, java.util.Set<java.lang.String>> userGroups) {
        this.userGroups = userGroups;
    }

    @com.google.inject.assistedinject.AssistedInject
    public PostUserCreationHookContext(@com.google.inject.assistedinject.Assisted
    java.lang.String userName) {
        userGroups.put(userName, java.util.Collections.emptySet());
    }

    public java.util.Map<java.lang.String, java.util.Set<java.lang.String>> getUserGroups() {
        return userGroups;
    }

    @java.lang.Override
    public java.lang.String toString() {
        return (("BatchUserHookContext{" + "userGroups=") + userGroups) + '}';
    }
}