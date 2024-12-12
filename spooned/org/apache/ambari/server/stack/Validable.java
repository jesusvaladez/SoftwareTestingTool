package org.apache.ambari.server.stack;
public interface Validable {
    boolean isValid();

    void setValid(boolean valid);

    void addError(java.lang.String error);

    void addErrors(java.util.Collection<java.lang.String> errors);

    java.util.Collection<java.lang.String> getErrors();
}