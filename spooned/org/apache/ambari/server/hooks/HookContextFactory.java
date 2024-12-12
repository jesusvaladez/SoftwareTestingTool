package org.apache.ambari.server.hooks;
public interface HookContextFactory {
    org.apache.ambari.server.hooks.HookContext createUserHookContext(java.lang.String userName);

    org.apache.ambari.server.hooks.HookContext createBatchUserHookContext(java.util.Map<java.lang.String, java.util.Set<java.lang.String>> userGroups);
}