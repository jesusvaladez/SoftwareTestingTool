package org.apache.ambari.server.stack;
import org.apache.commons.lang.builder.ToStringBuilder;
import static org.apache.commons.lang.StringUtils.isBlank;
public class NameService {
    public static class NameNode {
        private final java.lang.String uniqueId;

        private final java.lang.String address;

        private final boolean encrypted;

        private final java.lang.String propertyName;

        public static org.apache.ambari.server.stack.NameService.NameNode fromConfig(java.lang.String nameServiceId, java.lang.String nnUniqueId, org.apache.ambari.server.state.ConfigHelper config, org.apache.ambari.server.state.Cluster cluster) {
            java.lang.String namenodeFragment = ("dfs.namenode." + (org.apache.ambari.server.stack.NameService.NameNode.isEncrypted(config, cluster) ? "https-address" : "http-address")) + ".{0}.{1}";
            java.lang.String propertyName = java.text.MessageFormat.format(namenodeFragment, nameServiceId, nnUniqueId);
            return new org.apache.ambari.server.stack.NameService.NameNode(nnUniqueId, config.getValueFromDesiredConfigurations(cluster, org.apache.ambari.server.state.ConfigHelper.HDFS_SITE, propertyName), org.apache.ambari.server.stack.NameService.NameNode.isEncrypted(config, cluster), propertyName);
        }

        NameNode(java.lang.String uniqueId, java.lang.String address, boolean encrypted, java.lang.String propertyName) {
            this.uniqueId = uniqueId;
            this.address = address;
            this.encrypted = encrypted;
            this.propertyName = propertyName;
        }

        public java.lang.String getHost() {
            return getAddress().host.toLowerCase();
        }

        public int getPort() {
            return getAddress().port;
        }

        private org.apache.ambari.server.utils.HostAndPort getAddress() {
            org.apache.ambari.server.utils.HostAndPort hp = org.apache.ambari.server.utils.HTTPUtils.getHostAndPortFromProperty(address);
            if (hp == null) {
                throw new java.lang.IllegalArgumentException("Could not parse host and port from " + address);
            }
            return hp;
        }

        private static boolean isEncrypted(org.apache.ambari.server.state.ConfigHelper config, org.apache.ambari.server.state.Cluster cluster) {
            java.lang.String policy = config.getValueFromDesiredConfigurations(cluster, org.apache.ambari.server.state.ConfigHelper.HDFS_SITE, "dfs.http.policy");
            return (policy != null) && policy.equalsIgnoreCase(org.apache.ambari.server.state.ConfigHelper.HTTPS_ONLY);
        }

        public boolean isEncrypted() {
            return encrypted;
        }

        public java.lang.String getPropertyName() {
            return propertyName;
        }

        @java.lang.Override
        public java.lang.String toString() {
            return new org.apache.commons.lang.builder.ToStringBuilder(this).append("uniqueId", uniqueId).append("address", address).append("encrypted", encrypted).append("propertyName", propertyName).toString();
        }
    }

    public final java.lang.String nameServiceId;

    private final java.util.List<org.apache.ambari.server.stack.NameService.NameNode> nameNodes;

    public static java.util.List<org.apache.ambari.server.stack.NameService> fromConfig(org.apache.ambari.server.state.ConfigHelper config, org.apache.ambari.server.state.Cluster cluster) {
        return org.apache.ambari.server.stack.NameService.nameServiceIds(config, cluster).stream().map(nameServiceId -> org.apache.ambari.server.stack.NameService.nameService(nameServiceId, config, cluster)).collect(java.util.stream.Collectors.toList());
    }

    private static java.util.List<java.lang.String> nameServiceIds(org.apache.ambari.server.state.ConfigHelper config, org.apache.ambari.server.state.Cluster cluster) {
        return org.apache.ambari.server.stack.NameService.separateByComma(config, cluster, "dfs.internal.nameservices");
    }

    private static org.apache.ambari.server.stack.NameService nameService(java.lang.String nameServiceId, org.apache.ambari.server.state.ConfigHelper config, org.apache.ambari.server.state.Cluster cluster) {
        java.util.List<org.apache.ambari.server.stack.NameService.NameNode> namenodes = org.apache.ambari.server.stack.NameService.nnUniqueIds(nameServiceId, config, cluster).stream().map(nnUniquId -> org.apache.ambari.server.stack.NameService.NameNode.fromConfig(nameServiceId, nnUniquId, config, cluster)).collect(java.util.stream.Collectors.toList());
        return new org.apache.ambari.server.stack.NameService(nameServiceId, namenodes);
    }

    private static java.util.List<java.lang.String> nnUniqueIds(java.lang.String nameServiceId, org.apache.ambari.server.state.ConfigHelper config, org.apache.ambari.server.state.Cluster cluster) {
        return org.apache.ambari.server.stack.NameService.separateByComma(config, cluster, "dfs.ha.namenodes." + nameServiceId);
    }

    private static java.util.List<java.lang.String> separateByComma(org.apache.ambari.server.state.ConfigHelper config, org.apache.ambari.server.state.Cluster cluster, java.lang.String propertyName) {
        java.lang.String propertyValue = config.getValueFromDesiredConfigurations(cluster, org.apache.ambari.server.state.ConfigHelper.HDFS_SITE, propertyName);
        return StringUtils.isBlank(propertyValue) ? java.util.Collections.emptyList() : java.util.Arrays.asList(propertyValue.split(","));
    }

    private NameService(java.lang.String nameServiceId, java.util.List<org.apache.ambari.server.stack.NameService.NameNode> nameNodes) {
        this.nameServiceId = nameServiceId;
        this.nameNodes = nameNodes;
    }

    public java.util.List<org.apache.ambari.server.stack.NameService.NameNode> getNameNodes() {
        return nameNodes;
    }

    @java.lang.Override
    public java.lang.String toString() {
        return new org.apache.commons.lang.builder.ToStringBuilder(this).append("nameServiceId", nameServiceId).append("nameNodes", getNameNodes()).toString();
    }
}