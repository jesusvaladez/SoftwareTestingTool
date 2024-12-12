package org.apache.ambari.server;
public class ParentObjectNotFoundException extends org.apache.ambari.server.ObjectNotFoundException {
    public ParentObjectNotFoundException(java.lang.String msg, org.apache.ambari.server.ObjectNotFoundException cause) {
        super((msg + ".  ") + cause.getMessage(), cause);
    }

    public ParentObjectNotFoundException(java.lang.String message) {
        super(message);
    }
}