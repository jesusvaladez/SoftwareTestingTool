package org.apache.ambari.server.api.util;
public class TreeNodeImpl<T> implements org.apache.ambari.server.api.util.TreeNode<T> {
    private java.lang.String m_name;

    private org.apache.ambari.server.api.util.TreeNode<T> m_parent;

    private java.util.Map<java.lang.String, org.apache.ambari.server.api.util.TreeNode<T>> m_mapChildren = new java.util.LinkedHashMap<>();

    private T m_object;

    private java.util.Map<java.lang.String, java.lang.Object> m_mapNodeProps;

    public TreeNodeImpl(org.apache.ambari.server.api.util.TreeNode<T> parent, T object, java.lang.String name) {
        m_parent = parent;
        m_object = object;
        m_name = name;
    }

    @java.lang.Override
    public org.apache.ambari.server.api.util.TreeNode<T> getParent() {
        return m_parent;
    }

    @java.lang.Override
    public java.util.Collection<org.apache.ambari.server.api.util.TreeNode<T>> getChildren() {
        return m_mapChildren.values();
    }

    @java.lang.Override
    public T getObject() {
        return m_object;
    }

    @java.lang.Override
    public void setName(java.lang.String name) {
        m_name = name;
    }

    @java.lang.Override
    public java.lang.String getName() {
        return m_name;
    }

    @java.lang.Override
    public void setParent(org.apache.ambari.server.api.util.TreeNode<T> parent) {
        m_parent = parent;
    }

    @java.lang.Override
    public org.apache.ambari.server.api.util.TreeNode<T> addChild(T child, java.lang.String name) {
        org.apache.ambari.server.api.util.TreeNodeImpl<T> node = new org.apache.ambari.server.api.util.TreeNodeImpl<>(this, child, name);
        m_mapChildren.put(name, node);
        return node;
    }

    @java.lang.Override
    public org.apache.ambari.server.api.util.TreeNode<T> addChild(org.apache.ambari.server.api.util.TreeNode<T> child) {
        child.setParent(this);
        m_mapChildren.put(child.getName(), child);
        return child;
    }

    @java.lang.Override
    public org.apache.ambari.server.api.util.TreeNode<T> removeChild(java.lang.String name) {
        return m_mapChildren.remove(name);
    }

    @java.lang.Override
    public void setProperty(java.lang.String name, java.lang.Object value) {
        if (m_mapNodeProps == null) {
            m_mapNodeProps = new java.util.LinkedHashMap<>();
        }
        m_mapNodeProps.put(name, value);
    }

    @java.lang.Override
    public java.lang.Object getProperty(java.lang.String name) {
        return m_mapNodeProps == null ? null : m_mapNodeProps.get(name);
    }

    @java.lang.Override
    public java.lang.String getStringProperty(java.lang.String name) {
        java.lang.Object value = getProperty(name);
        return value == null ? null : value.toString();
    }

    @java.lang.Override
    public void removeProperty(java.lang.String name) {
        if (m_mapNodeProps != null) {
            m_mapNodeProps.remove(name);
        }
    }

    @java.lang.Override
    public org.apache.ambari.server.api.util.TreeNode<T> getChild(java.lang.String name) {
        if ((name != null) && name.contains("/")) {
            int i = name.indexOf('/');
            java.lang.String s = name.substring(0, i);
            org.apache.ambari.server.api.util.TreeNode<T> node = m_mapChildren.get(s);
            return node == null ? null : node.getChild(name.substring(i + 1));
        } else {
            return m_mapChildren.get(name);
        }
    }
}