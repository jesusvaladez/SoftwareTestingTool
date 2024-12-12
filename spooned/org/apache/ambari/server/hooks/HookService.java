package org.apache.ambari.server.hooks;
public interface HookService {
    boolean execute(org.apache.ambari.server.hooks.HookContext hookContext);
}