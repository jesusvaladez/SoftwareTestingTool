package org.apache.ambari.server;
@java.lang.SuppressWarnings("serial")
public class ObjectNotFoundException extends org.apache.ambari.server.AmbariException {
    public ObjectNotFoundException(java.lang.String msg, org.apache.ambari.server.ObjectNotFoundException cause) {
        super((msg + ".  ") + cause.getMessage(), cause);
    }

    public ObjectNotFoundException(java.lang.String msg) {
        super(msg);
    }
}