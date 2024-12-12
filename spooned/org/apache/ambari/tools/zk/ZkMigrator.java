package org.apache.ambari.tools.zk;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooKeeper;
public class ZkMigrator {
    private static final int SESSION_TIMEOUT_MILLIS = 5000;

    private static final int CONNECTION_TIMEOUT_MILLIS = 30000;

    public static void main(java.lang.String[] args) throws java.lang.Exception {
        org.apache.commons.cli.CommandLine cli = new org.apache.commons.cli.DefaultParser().parse(org.apache.ambari.tools.zk.ZkMigrator.options(), args);
        if (cli.hasOption("connection-string") && cli.hasOption("znode")) {
            if (cli.hasOption("acl") && (!cli.hasOption("delete"))) {
                org.apache.ambari.tools.zk.ZkMigrator.setAcls(cli.getOptionValue("connection-string"), cli.getOptionValue("znode"), org.apache.ambari.tools.zk.ZkAcl.parse(cli.getOptionValue("acl")));
            } else if (cli.hasOption("delete") && (!cli.hasOption("acl"))) {
                org.apache.ambari.tools.zk.ZkMigrator.deleteZnodeRecursively(cli.getOptionValue("connection-string"), cli.getOptionValue("znode"));
            } else {
                org.apache.ambari.tools.zk.ZkMigrator.printHelp();
            }
        } else {
            org.apache.ambari.tools.zk.ZkMigrator.printHelp();
        }
    }

    private static void deleteZnodeRecursively(java.lang.String connectionString, java.lang.String znode) throws java.io.IOException, java.lang.InterruptedException, org.apache.zookeeper.KeeperException {
        org.apache.zookeeper.ZooKeeper client = org.apache.ambari.tools.zk.ZkConnection.open(connectionString, org.apache.ambari.tools.zk.ZkMigrator.SESSION_TIMEOUT_MILLIS, org.apache.ambari.tools.zk.ZkMigrator.CONNECTION_TIMEOUT_MILLIS);
        try {
            org.apache.ambari.tools.zk.ZkPathPattern paths = org.apache.ambari.tools.zk.ZkPathPattern.fromString(znode);
            for (java.lang.String path : paths.findMatchingPaths(client, "/")) {
                java.lang.System.out.println("Recursively deleting znodes with matching path " + path);
                org.apache.ambari.tools.zk.ZkMigrator.deleteZnodeRecursively(client, path);
            }
        } catch (org.apache.zookeeper.KeeperException.NoNodeException e) {
            java.lang.System.out.println((("Could not delete " + znode) + ". Reason: ") + e.getMessage());
        } finally {
            client.close();
        }
    }

    private static void deleteZnodeRecursively(org.apache.zookeeper.ZooKeeper zkClient, java.lang.String baseNode) throws org.apache.zookeeper.KeeperException, java.lang.InterruptedException {
        for (java.lang.String child : zkClient.getChildren(baseNode, null)) {
            org.apache.ambari.tools.zk.ZkMigrator.deleteZnodeRecursively(zkClient, org.apache.ambari.tools.zk.ZkAcl.append(baseNode, child));
        }
        java.lang.System.out.println("Deleting znode " + baseNode);
        zkClient.delete(baseNode, -1);
    }

    private static org.apache.commons.cli.Options options() {
        return new org.apache.commons.cli.Options().addOption(org.apache.commons.cli.Option.builder("h").longOpt("help").desc("print help").build()).addOption(org.apache.commons.cli.Option.builder("c").longOpt("connection-string").desc("zookeeper connection string").hasArg().argName("connection-string").build()).addOption(org.apache.commons.cli.Option.builder("a").longOpt("acl").desc("ACL of a znode in the following format <scheme:id:permission>").hasArg().argName("acl").build()).addOption(org.apache.commons.cli.Option.builder("z").longOpt("znode").desc("znode path").hasArg().argName("znode").build()).addOption(org.apache.commons.cli.Option.builder("d").longOpt("delete").desc("delete specified znode and all it's children recursively").argName("delete").build());
    }

    private static void setAcls(java.lang.String connectionString, java.lang.String znode, org.apache.ambari.tools.zk.ZkAcl acl) throws java.io.IOException, java.lang.InterruptedException, org.apache.zookeeper.KeeperException {
        org.apache.zookeeper.ZooKeeper client = org.apache.ambari.tools.zk.ZkConnection.open(connectionString, org.apache.ambari.tools.zk.ZkMigrator.SESSION_TIMEOUT_MILLIS, org.apache.ambari.tools.zk.ZkMigrator.CONNECTION_TIMEOUT_MILLIS);
        try {
            acl.setRecursivelyOn(client, org.apache.ambari.tools.zk.ZkPathPattern.fromString(znode));
        } catch (org.apache.zookeeper.KeeperException.NoNodeException e) {
            java.lang.System.out.println((("Could not set ACL on " + znode) + ". Reason: ") + e.getMessage());
        } finally {
            client.close();
        }
    }

    private static void printHelp() {
        java.lang.System.out.println("Usage zkmigrator -connection-string <host:port> -acl <scheme:id:permission> -znode /path/to/znode\n" + "              OR -connection-string <host:port> -znode /path/to/znode -delete");
        java.lang.System.exit(1);
    }
}