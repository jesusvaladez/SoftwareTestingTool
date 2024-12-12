package org.apache.ambari.server.topology;
public class NoSuchHostGroupException extends java.lang.Exception {
    public NoSuchHostGroupException(java.lang.String hostgroupName) {
        super("Requested HostGroup doesn't exist: " + hostgroupName);
    }

    public NoSuchHostGroupException(java.lang.String hostgroupName, java.lang.String msg) {
        super((msg + ".  Cause: Requested HostGroup doesn't exist: ") + hostgroupName);
    }
}