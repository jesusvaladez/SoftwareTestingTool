package org.apache.ambari.swagger;
class NestedApiRecord {
    final java.lang.Class<?> nestedApi;

    final java.lang.Class<?> parentApi;

    final java.lang.String parentApiPath;

    final java.lang.reflect.Method parentMethod;

    final java.lang.String parentMethodPath;

    public NestedApiRecord(java.lang.Class<?> nestedApi, java.lang.Class<?> parentApi, java.lang.String parentApiPath, java.lang.reflect.Method parentMethod, java.lang.String parentMethodPath) {
        this.nestedApi = nestedApi;
        this.parentApi = parentApi;
        this.parentApiPath = parentApiPath;
        this.parentMethod = parentMethod;
        this.parentMethodPath = parentMethodPath;
    }

    @java.lang.Override
    public java.lang.String toString() {
        return (((((((((((("NestedApiRecord {" + "nestedApi=") + nestedApi) + ", parentApi=") + parentApi) + ", parentApiPath='") + parentApiPath) + '\'') + ", parentMethod=") + parentMethod) + ", parentMethodPath='") + parentMethodPath) + '\'') + '}';
    }
}