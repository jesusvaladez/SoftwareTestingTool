package org.apache.ambari.server.orm;
public enum PersistenceType {

    IN_MEMORY("in-memory"),
    LOCAL("local"),
    REMOTE("remote");
    java.lang.String value;

    PersistenceType(java.lang.String value) {
        this.value = value;
    }

    public static org.apache.ambari.server.orm.PersistenceType fromString(java.lang.String typeString) {
        for (org.apache.ambari.server.orm.PersistenceType type : org.apache.ambari.server.orm.PersistenceType.values()) {
            if (type.value.equals(typeString)) {
                return type;
            }
        }
        throw new java.lang.IllegalArgumentException("Unknown persistence type: " + typeString);
    }

    public java.lang.String getValue() {
        return value;
    }
}