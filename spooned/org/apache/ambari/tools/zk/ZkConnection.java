package org.apache.ambari.tools.zk;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static org.apache.zookeeper.Watcher.Event.KeeperState.SyncConnected;
public class ZkConnection {
    public static org.apache.zookeeper.ZooKeeper open(java.lang.String serverAddress, int sessionTimeoutMillis, int connectionTimeoutMillis) throws java.io.IOException, java.lang.InterruptedException, java.lang.IllegalStateException {
        final java.util.concurrent.CountDownLatch connSignal = new java.util.concurrent.CountDownLatch(1);
        org.apache.zookeeper.ZooKeeper zooKeeper = new org.apache.zookeeper.ZooKeeper(serverAddress, sessionTimeoutMillis, new org.apache.zookeeper.Watcher() {
            @java.lang.Override
            public void process(org.apache.zookeeper.WatchedEvent event) {
                if (event.getState() == SyncConnected) {
                    connSignal.countDown();
                }
            }
        });
        connSignal.await(connectionTimeoutMillis, java.util.concurrent.TimeUnit.MILLISECONDS);
        return zooKeeper;
    }
}