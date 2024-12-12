package org.apache.ambari.tools.zk;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.data.Id;
public class ZkAcl {
    private static final int ANY_NODE_VER = -1;

    private final org.apache.ambari.tools.zk.ZkAcl.AclScheme scheme;

    private final java.lang.String id;

    private final org.apache.ambari.tools.zk.ZkAcl.Permission permission;

    public static org.apache.ambari.tools.zk.ZkAcl parse(java.lang.String acl) {
        java.lang.String[] parts = acl.split(":");
        if (parts.length != 3) {
            throw new java.lang.IllegalArgumentException(("Invalid ACL: " + acl) + " must be <scheme:id:permission>");
        }
        return new org.apache.ambari.tools.zk.ZkAcl(org.apache.ambari.tools.zk.ZkAcl.AclScheme.parse(parts[0]), parts[1], org.apache.ambari.tools.zk.ZkAcl.Permission.parse(parts[2]));
    }

    private ZkAcl(org.apache.ambari.tools.zk.ZkAcl.AclScheme scheme, java.lang.String id, org.apache.ambari.tools.zk.ZkAcl.Permission permission) {
        this.scheme = scheme;
        this.id = id;
        this.permission = permission;
    }

    public void setRecursivelyOn(org.apache.zookeeper.ZooKeeper zkClient, org.apache.ambari.tools.zk.ZkPathPattern pattern) throws org.apache.zookeeper.KeeperException, java.lang.InterruptedException {
        for (java.lang.String each : pattern.findMatchingPaths(zkClient, "/")) {
            java.lang.System.out.println((("Set ACL " + asZkAcl()) + " recursively on ") + each);
            setRecursivelyOnSingle(zkClient, each);
        }
    }

    public void setRecursivelyOnSingle(org.apache.zookeeper.ZooKeeper zkClient, java.lang.String baseNode) throws org.apache.zookeeper.KeeperException, java.lang.InterruptedException {
        zkClient.setACL(baseNode, java.util.Collections.singletonList(asZkAcl()), org.apache.ambari.tools.zk.ZkAcl.ANY_NODE_VER);
        for (java.lang.String child : zkClient.getChildren(baseNode, null)) {
            setRecursivelyOnSingle(zkClient, org.apache.ambari.tools.zk.ZkAcl.append(baseNode, child));
        }
    }

    private org.apache.zookeeper.data.ACL asZkAcl() {
        return new org.apache.zookeeper.data.ACL(permission.code, new org.apache.zookeeper.data.Id(scheme.value, id));
    }

    public static java.lang.String append(java.lang.String node, java.lang.String child) {
        return node.endsWith("/") ? node + child : (node + "/") + child;
    }

    static class AclScheme {
        final java.lang.String value;

        public static org.apache.ambari.tools.zk.ZkAcl.AclScheme parse(java.lang.String scheme) {
            if (scheme.toLowerCase().equals("world") || scheme.toLowerCase().equals("ip")) {
                return new org.apache.ambari.tools.zk.ZkAcl.AclScheme(scheme);
            }
            throw new java.lang.IllegalArgumentException("Unsupported scheme: " + scheme);
        }

        private AclScheme(java.lang.String value) {
            this.value = value;
        }
    }

    static class Permission {
        final int code;

        public static org.apache.ambari.tools.zk.ZkAcl.Permission parse(java.lang.String permission) {
            int permissionCode = 0;
            for (char each : permission.toLowerCase().toCharArray()) {
                switch (each) {
                    case 'r' :
                        permissionCode |= ZooDefs.Perms.READ;
                        break;
                    case 'w' :
                        permissionCode |= ZooDefs.Perms.WRITE;
                        break;
                    case 'c' :
                        permissionCode |= ZooDefs.Perms.CREATE;
                        break;
                    case 'd' :
                        permissionCode |= ZooDefs.Perms.DELETE;
                        break;
                    case 'a' :
                        permissionCode |= ZooDefs.Perms.ADMIN;
                        break;
                    default :
                        throw new java.lang.IllegalArgumentException("Unsupported permission: " + permission);
                }
            }
            return new org.apache.ambari.tools.zk.ZkAcl.Permission(permissionCode);
        }

        private Permission(int code) {
            this.code = code;
        }
    }
}