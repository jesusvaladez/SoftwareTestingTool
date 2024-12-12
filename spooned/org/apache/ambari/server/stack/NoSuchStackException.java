package org.apache.ambari.server.stack;
public class NoSuchStackException extends java.lang.Exception {
    public NoSuchStackException(java.lang.String stackName, java.lang.String stackVersion) {
        super(java.lang.String.format("The requested stack doesn't exist. Name='%s' Version='%s'", stackName, stackVersion));
    }
}