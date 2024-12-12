package org.apache.ambari.server.serveraction.users;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;
@javax.inject.Singleton
public class ShellCommandUtilityCallable implements java.util.concurrent.Callable<org.apache.ambari.server.utils.ShellCommandUtil.Result> {
    private java.lang.String[] args;

    @com.google.inject.assistedinject.AssistedInject
    public ShellCommandUtilityCallable(@com.google.inject.assistedinject.Assisted
    java.lang.String[] args) {
        this.args = args;
    }

    @java.lang.Override
    public org.apache.ambari.server.utils.ShellCommandUtil.Result call() throws java.lang.Exception {
        return org.apache.ambari.server.utils.ShellCommandUtil.runCommand(args);
    }
}