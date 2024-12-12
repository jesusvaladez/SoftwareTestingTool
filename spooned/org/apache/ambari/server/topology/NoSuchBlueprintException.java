package org.apache.ambari.server.topology;
public class NoSuchBlueprintException extends java.lang.Exception {
    public NoSuchBlueprintException(java.lang.String name) {
        super(java.lang.String.format("No blueprint exists with the name '%s'", name));
    }
}