package org.apache.ambari.server.stack;
public class ClassifyNameNodeException extends java.lang.RuntimeException {
    public ClassifyNameNodeException(org.apache.ambari.server.stack.NameService nameService) {
        super("Could not classify some of the NameNodes in namespace: " + nameService.nameServiceId);
    }
}