package org.apache.ambari.server.orm;
public enum JPATableGenerationStrategy {

    CREATE("create"),
    CREATE_OR_EXTEND("createOrExtend"),
    DROP_AND_CREATE("dropAndCreate"),
    NONE("none");
    private java.lang.String value;

    JPATableGenerationStrategy(java.lang.String value) {
        this.value = value;
    }

    public java.lang.String getValue() {
        return value;
    }

    public static org.apache.ambari.server.orm.JPATableGenerationStrategy fromString(java.lang.String value) {
        for (org.apache.ambari.server.orm.JPATableGenerationStrategy strategy : org.apache.ambari.server.orm.JPATableGenerationStrategy.values()) {
            if (strategy.value.equalsIgnoreCase(value)) {
                return strategy;
            }
        }
        return org.apache.ambari.server.orm.JPATableGenerationStrategy.NONE;
    }
}