package org.apache.ambari.server.collections;
public abstract class Predicate implements org.apache.commons.collections.Predicate {
    private final java.lang.String name;

    protected Predicate(java.lang.String name) {
        this.name = name;
    }

    public java.lang.String getName() {
        return name;
    }

    public abstract java.util.Map<java.lang.String, java.lang.Object> toMap();

    public java.lang.String toJSON() {
        java.util.Map<java.lang.String, java.lang.Object> map = toMap();
        return map == null ? null : new com.google.gson.Gson().toJson(map);
    }

    @java.lang.Override
    public int hashCode() {
        return 37 * (name == null ? 0 : name.hashCode());
    }

    @java.lang.Override
    public boolean equals(java.lang.Object obj) {
        if (obj == this) {
            return true;
        } else if (obj == null) {
            return false;
        } else if ((obj instanceof org.apache.ambari.server.collections.Predicate) && (hashCode() == obj.hashCode())) {
            org.apache.ambari.server.collections.Predicate p = ((org.apache.ambari.server.collections.Predicate) (obj));
            return name == null ? p.name == null : name.equals(p.name);
        } else {
            return false;
        }
    }
}