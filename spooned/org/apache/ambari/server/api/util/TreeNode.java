package org.apache.ambari.server.api.util;
public interface TreeNode<T> {
    org.apache.ambari.server.api.util.TreeNode<T> getParent();

    java.util.Collection<org.apache.ambari.server.api.util.TreeNode<T>> getChildren();

    T getObject();

    java.lang.String getName();

    void setName(java.lang.String name);

    void setParent(org.apache.ambari.server.api.util.TreeNode<T> parent);

    org.apache.ambari.server.api.util.TreeNode<T> addChild(T child, java.lang.String name);

    org.apache.ambari.server.api.util.TreeNode<T> addChild(org.apache.ambari.server.api.util.TreeNode<T> child);

    org.apache.ambari.server.api.util.TreeNode<T> removeChild(java.lang.String name);

    void setProperty(java.lang.String name, java.lang.Object value);

    java.lang.Object getProperty(java.lang.String name);

    java.lang.String getStringProperty(java.lang.String name);

    void removeProperty(java.lang.String name);

    org.apache.ambari.server.api.util.TreeNode<T> getChild(java.lang.String name);
}