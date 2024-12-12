package org.apache.ambari.server.controller.ivory;
public class Cluster {
    private final java.lang.String name;

    private final java.lang.String colo;

    private final java.util.Set<org.apache.ambari.server.controller.ivory.Cluster.Interface> interfaces;

    private final java.util.Set<org.apache.ambari.server.controller.ivory.Cluster.Location> locations;

    private final java.util.Map<java.lang.String, java.lang.String> properties;

    public Cluster(java.lang.String name, java.lang.String colo, java.util.Set<org.apache.ambari.server.controller.ivory.Cluster.Interface> interfaces, java.util.Set<org.apache.ambari.server.controller.ivory.Cluster.Location> locations, java.util.Map<java.lang.String, java.lang.String> properties) {
        this.name = name;
        this.colo = colo;
        this.interfaces = interfaces;
        this.locations = locations;
        this.properties = properties;
    }

    public java.lang.String getName() {
        return name;
    }

    public java.lang.String getColo() {
        return colo;
    }

    public java.util.Set<org.apache.ambari.server.controller.ivory.Cluster.Interface> getInterfaces() {
        return interfaces;
    }

    public java.util.Set<org.apache.ambari.server.controller.ivory.Cluster.Location> getLocations() {
        return locations;
    }

    public java.util.Map<java.lang.String, java.lang.String> getProperties() {
        return properties;
    }

    @java.lang.Override
    public boolean equals(java.lang.Object o) {
        if (this == o)
            return true;

        if ((o == null) || (getClass() != o.getClass()))
            return false;

        org.apache.ambari.server.controller.ivory.Cluster cluster = ((org.apache.ambari.server.controller.ivory.Cluster) (o));
        return ((((!(colo != null ? !colo.equals(cluster.colo) : cluster.colo != null)) && (!(interfaces != null ? !interfaces.equals(cluster.interfaces) : cluster.interfaces != null))) && (!(locations != null ? !locations.equals(cluster.locations) : cluster.locations != null))) && (!(name != null ? !name.equals(cluster.name) : cluster.name != null))) && (!(properties != null ? !properties.equals(cluster.properties) : cluster.properties != null));
    }

    @java.lang.Override
    public int hashCode() {
        int result = (name != null) ? name.hashCode() : 0;
        result = (31 * result) + (colo != null ? colo.hashCode() : 0);
        result = (31 * result) + (interfaces != null ? interfaces.hashCode() : 0);
        result = (31 * result) + (locations != null ? locations.hashCode() : 0);
        result = (31 * result) + (properties != null ? properties.hashCode() : 0);
        return result;
    }

    public static class Interface {
        private final java.lang.String type;

        private final java.lang.String endpoint;

        private final java.lang.String version;

        public Interface(java.lang.String type, java.lang.String endpoint, java.lang.String version) {
            this.type = type;
            this.endpoint = endpoint;
            this.version = version;
        }

        public java.lang.String getType() {
            return type;
        }

        public java.lang.String getEndpoint() {
            return endpoint;
        }

        public java.lang.String getVersion() {
            return version;
        }

        @java.lang.Override
        public boolean equals(java.lang.Object o) {
            if (this == o)
                return true;

            if ((o == null) || (getClass() != o.getClass()))
                return false;

            org.apache.ambari.server.controller.ivory.Cluster.Interface that = ((org.apache.ambari.server.controller.ivory.Cluster.Interface) (o));
            return ((!(endpoint != null ? !endpoint.equals(that.endpoint) : that.endpoint != null)) && (!(type != null ? !type.equals(that.type) : that.type != null))) && (!(version != null ? !version.equals(that.version) : that.version != null));
        }

        @java.lang.Override
        public int hashCode() {
            int result = (type != null) ? type.hashCode() : 0;
            result = (31 * result) + (endpoint != null ? endpoint.hashCode() : 0);
            result = (31 * result) + (version != null ? version.hashCode() : 0);
            return result;
        }
    }

    public static class Location {
        private final java.lang.String name;

        private final java.lang.String path;

        public Location(java.lang.String name, java.lang.String path) {
            this.name = name;
            this.path = path;
        }

        public java.lang.String getName() {
            return name;
        }

        public java.lang.String getPath() {
            return path;
        }

        @java.lang.Override
        public boolean equals(java.lang.Object o) {
            if (this == o)
                return true;

            if ((o == null) || (getClass() != o.getClass()))
                return false;

            org.apache.ambari.server.controller.ivory.Cluster.Location location = ((org.apache.ambari.server.controller.ivory.Cluster.Location) (o));
            return (!(name != null ? !name.equals(location.name) : location.name != null)) && (!(path != null ? !path.equals(location.path) : location.path != null));
        }

        @java.lang.Override
        public int hashCode() {
            int result = (name != null) ? name.hashCode() : 0;
            result = (31 * result) + (path != null ? path.hashCode() : 0);
            return result;
        }
    }
}