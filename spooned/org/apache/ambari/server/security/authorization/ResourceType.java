package org.apache.ambari.server.security.authorization;
public enum ResourceType {

    AMBARI(1),
    CLUSTER(2),
    VIEW(3);
    private final int id;

    ResourceType(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public static org.apache.ambari.server.security.authorization.ResourceType translate(java.lang.String resourceTypeName) {
        if (resourceTypeName == null) {
            return null;
        } else {
            resourceTypeName = resourceTypeName.trim();
            if (resourceTypeName.isEmpty()) {
                return null;
            } else {
                try {
                    return org.apache.ambari.server.security.authorization.ResourceType.valueOf(resourceTypeName.toUpperCase());
                } catch (java.lang.IllegalArgumentException e) {
                    return org.apache.ambari.server.security.authorization.ResourceType.VIEW;
                }
            }
        }
    }
}