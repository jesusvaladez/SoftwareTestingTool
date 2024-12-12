package org.apache.ambari.server.serveraction.users;
public interface ShellCommandCallableFactory {
    org.apache.ambari.server.serveraction.users.ShellCommandUtilityCallable createRunCommandCallable(java.lang.String[] arguments);
}