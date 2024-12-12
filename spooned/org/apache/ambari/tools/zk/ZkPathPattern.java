package org.apache.ambari.tools.zk;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooKeeper;
public class ZkPathPattern {
    private final java.util.List<org.apache.ambari.tools.zk.ZkPathPattern.Segment> segments;

    public static org.apache.ambari.tools.zk.ZkPathPattern fromString(java.lang.String pattern) {
        pattern = pattern.trim();
        if (!pattern.startsWith("/")) {
            throw new java.lang.IllegalArgumentException("ZkPath must start with: '/'. Invalid path: " + pattern);
        }
        if ("/".equals(pattern)) {
            return new org.apache.ambari.tools.zk.ZkPathPattern(java.util.Collections.singletonList(new org.apache.ambari.tools.zk.ZkPathPattern.Segment("*")));
        }
        java.util.List<org.apache.ambari.tools.zk.ZkPathPattern.Segment> segments = new java.util.ArrayList<>();
        for (java.lang.String segment : pattern.substring(1).split("/")) {
            if (segment.isEmpty()) {
                throw new java.lang.IllegalArgumentException("Invalid ZkPath: " + pattern);
            }
            segments.add(new org.apache.ambari.tools.zk.ZkPathPattern.Segment(segment));
        }
        if (segments.isEmpty()) {
            throw new java.lang.IllegalArgumentException("Empty ZkPath: " + pattern);
        }
        return new org.apache.ambari.tools.zk.ZkPathPattern(segments);
    }

    private ZkPathPattern(java.util.List<org.apache.ambari.tools.zk.ZkPathPattern.Segment> segments) {
        this.segments = segments;
    }

    public java.util.List<java.lang.String> findMatchingPaths(org.apache.zookeeper.ZooKeeper zkClient, java.lang.String basePath) throws org.apache.zookeeper.KeeperException, java.lang.InterruptedException {
        java.util.List<java.lang.String> result = new java.util.ArrayList<>();
        collectMatching(zkClient, basePath, result);
        return result;
    }

    private void collectMatching(org.apache.zookeeper.ZooKeeper zkClient, java.lang.String basePath, java.util.List<java.lang.String> result) throws org.apache.zookeeper.KeeperException, java.lang.InterruptedException {
        for (java.lang.String child : zkClient.getChildren(basePath, null)) {
            if (first().matches(child)) {
                if (rest() == null) {
                    result.add(org.apache.ambari.tools.zk.ZkAcl.append(basePath, child));
                } else {
                    rest().collectMatching(zkClient, org.apache.ambari.tools.zk.ZkAcl.append(basePath, child), result);
                }
            }
        }
    }

    private org.apache.ambari.tools.zk.ZkPathPattern.Segment first() {
        return segments.get(0);
    }

    private org.apache.ambari.tools.zk.ZkPathPattern rest() {
        java.util.List<org.apache.ambari.tools.zk.ZkPathPattern.Segment> tail = segments.subList(1, segments.size());
        return tail.isEmpty() ? null : new org.apache.ambari.tools.zk.ZkPathPattern(tail);
    }

    public static class Segment {
        private final java.nio.file.PathMatcher glob;

        public Segment(java.lang.String segment) {
            this.glob = java.nio.file.FileSystems.getDefault().getPathMatcher("glob:" + segment);
        }

        public boolean matches(java.lang.String node) {
            return glob.matches(java.nio.file.Paths.get(node));
        }
    }
}