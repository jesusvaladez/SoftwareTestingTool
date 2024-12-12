package org.apache.ambari.tools.zk;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.RetryOneTime;
import org.apache.curator.test.TestingServer;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.data.Id;
import static org.apache.zookeeper.ZooDefs.Perms.ALL;
import static org.apache.zookeeper.ZooDefs.Perms.DELETE;
import static org.apache.zookeeper.ZooDefs.Perms.READ;
import static org.apache.zookeeper.ZooDefs.Perms.WRITE;
@org.junit.experimental.categories.Category({ category.SlowTest.class })
public class ZkMigratorTest {
    private org.apache.curator.framework.CuratorFramework cli;

    private org.apache.curator.test.TestingServer zkTestServer;

    @org.junit.Test
    public void testSetAclsOnSingleNode() throws java.lang.Exception {
        path("/single");
        setAcls("/single", "ip:127.0.0.1:rwd");
        assertHasAcl("/single", "ip", "127.0.0.1", (WRITE | READ) | DELETE);
    }

    @org.junit.Test
    public void testSetAclsOnParentAndItsDirectChildren() throws java.lang.Exception {
        path("/parent");
        path("/parent/a");
        path("/parent/b");
        setAcls("/parent", "ip:127.0.0.1:rd");
        assertHasAcl("/parent", "ip", "127.0.0.1", READ | DELETE);
        assertHasAcl("/parent/a", "ip", "127.0.0.1", READ | DELETE);
        assertHasAcl("/parent/b", "ip", "127.0.0.1", READ | DELETE);
    }

    @org.junit.Test
    public void testDeleteRecursive() throws java.lang.Exception {
        path("/parent");
        path("/parent/a");
        path("/parent/b");
        path("/parent/b/q");
        deleteZnode("/parent");
        assertRemoved("/parent");
        assertRemoved("/parent/a");
        assertRemoved("/parent/b");
        assertRemoved("/parent/b/q");
    }

    @org.junit.Test
    public void testDeleteRecursiveWildcard() throws java.lang.Exception {
        path("/parent");
        path("/parent/a");
        path("/parent/b");
        path("/parent/b/q");
        deleteZnode("/parent/*");
        assertHasNode("/parent");
        assertRemoved("/parent/a");
        assertRemoved("/parent/b");
        assertRemoved("/parent/b/q");
    }

    @org.junit.Test
    public void testSetAclsRecursively() throws java.lang.Exception {
        path("/parent");
        path("/parent/a");
        path("/parent/a/b");
        path("/parent/a/b/c");
        setAcls("/parent", "ip:127.0.0.1:r");
        assertHasAcl("/parent", "ip", "127.0.0.1", org.apache.ambari.tools.zk.READ);
        assertHasAcl("/parent/a", "ip", "127.0.0.1", org.apache.ambari.tools.zk.READ);
        assertHasAcl("/parent/a/b", "ip", "127.0.0.1", org.apache.ambari.tools.zk.READ);
        assertHasAcl("/parent/a/b/c", "ip", "127.0.0.1", org.apache.ambari.tools.zk.READ);
    }

    @org.junit.Test
    public void testSupportsWildcard() throws java.lang.Exception {
        path("/abc123");
        path("/abcdef/efg");
        path("/abc/123");
        path("/x");
        path("/y/a");
        path("/ab");
        setAcls("/abc*", "ip:127.0.0.1:r");
        assertHasAcl("/abc123", "ip", "127.0.0.1", org.apache.ambari.tools.zk.READ);
        assertHasAcl("/abcdef/efg", "ip", "127.0.0.1", org.apache.ambari.tools.zk.READ);
        assertHasAcl("/abc/123", "ip", "127.0.0.1", org.apache.ambari.tools.zk.READ);
        assertHasAcl("/x", "world", "anyone", org.apache.ambari.tools.zk.ALL);
        assertHasAcl("/y/a", "world", "anyone", org.apache.ambari.tools.zk.ALL);
        assertHasAcl("/ab", "world", "anyone", org.apache.ambari.tools.zk.ALL);
    }

    @org.junit.Test
    public void testSupportsMultupleWildcards() throws java.lang.Exception {
        path("/abc123");
        path("/a/abcdef");
        path("/def/abc");
        path("/xy/abc/efg");
        path("/a/xyabc");
        path("/a/b/abc");
        path("/b");
        setAcls("/*/abc*", "ip:127.0.0.1:r");
        assertHasAcl("/a/abcdef", "ip", "127.0.0.1", org.apache.ambari.tools.zk.READ);
        assertHasAcl("/xy/abc/efg", "ip", "127.0.0.1", org.apache.ambari.tools.zk.READ);
        assertHasAcl("/def/abc", "ip", "127.0.0.1", org.apache.ambari.tools.zk.READ);
        assertHasAcl("/a/xyabc", "world", "anyone", org.apache.ambari.tools.zk.ALL);
        assertHasAcl("/abc123", "world", "anyone", org.apache.ambari.tools.zk.ALL);
        assertHasAcl("/a/b/abc", "world", "anyone", org.apache.ambari.tools.zk.ALL);
        assertHasAcl("/b", "world", "anyone", org.apache.ambari.tools.zk.ALL);
    }

    @org.junit.Test
    public void testSupportsWorldScheme() throws java.lang.Exception {
        path("/unprotected");
        setAcls("/unprotected", "world:anyone:r");
        assertHasAcl("/unprotected", "world", "anyone", org.apache.ambari.tools.zk.READ);
    }

    @org.junit.Test(expected = java.lang.IllegalArgumentException.class)
    public void testRejectsUnsupportedScheme() throws java.lang.Exception {
        path("/any");
        setAcls("/any", "unsupported:anyone:r");
    }

    @org.junit.Test(expected = java.lang.IllegalArgumentException.class)
    public void testRejectUnsupportedPermission() throws java.lang.Exception {
        path("/any");
        setAcls("/any", "world:anyone:invalid");
    }

    @org.junit.Test
    public void testIgnoresNonExistentNode() throws java.lang.Exception {
        setAcls("/nonexistent", "world:anyone:rw");
    }

    @org.junit.Before
    public void startZookeeper() throws java.lang.Exception {
        zkTestServer = new org.apache.curator.test.TestingServer(org.apache.ambari.tools.zk.ZkMigratorTest.Port.free());
        zkTestServer.start();
        cli = org.apache.curator.framework.CuratorFrameworkFactory.newClient(zkTestServer.getConnectString(), new org.apache.curator.retry.RetryOneTime(2000));
        cli.start();
    }

    @org.junit.After
    public void stopZookeeper() throws java.io.IOException {
        cli.close();
        zkTestServer.stop();
    }

    private java.lang.String path(java.lang.String s) throws java.lang.Exception {
        return cli.create().creatingParentsIfNeeded().forPath(s, "any".getBytes());
    }

    private void setAcls(java.lang.String path, java.lang.String acl) throws java.lang.Exception {
        org.apache.ambari.tools.zk.ZkMigrator.main(new java.lang.String[]{ "-connection-string", zkTestServer.getConnectString(), "-znode", path, "-acl", acl });
    }

    private void deleteZnode(java.lang.String path) throws java.lang.Exception {
        org.apache.ambari.tools.zk.ZkMigrator.main(new java.lang.String[]{ "-connection-string", zkTestServer.getConnectString(), "-znode", path, "-delete" });
    }

    private void assertHasAcl(java.lang.String path, java.lang.String scheme, java.lang.String id, int permission) throws java.lang.Exception {
        java.util.List<org.apache.zookeeper.data.ACL> acls = cli.getACL().forPath(path);
        org.junit.Assert.assertEquals("expected 1 acl on " + path, 1, acls.size());
        org.junit.Assert.assertEquals("acl on " + path, new org.apache.zookeeper.data.Id(scheme, id), acls.get(0).getId());
        org.junit.Assert.assertEquals(permission, acls.get(0).getPerms());
    }

    private void assertRemoved(java.lang.String path) throws java.lang.Exception {
        try {
            cli.getACL().forPath(path);
            org.junit.Assert.assertTrue(false);
        } catch (org.apache.zookeeper.KeeperException.NoNodeException e) {
        }
    }

    private void assertHasNode(java.lang.String path) throws java.lang.Exception {
        try {
            cli.getACL().forPath(path);
        } catch (org.apache.zookeeper.KeeperException.NoNodeException e) {
            org.junit.Assert.assertTrue(false);
        }
    }

    static class Port {
        public static int free() throws java.io.IOException {
            java.net.ServerSocket socket = null;
            try {
                socket = new java.net.ServerSocket(0);
                return socket.getLocalPort();
            } finally {
                if (socket != null) {
                    socket.close();
                }
            }
        }
    }
}