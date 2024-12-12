package org.apache.ambari.view;
public interface ResourceProvider<T> {
    public T getResource(java.lang.String resourceId, java.util.Set<java.lang.String> properties) throws org.apache.ambari.view.SystemException, org.apache.ambari.view.NoSuchResourceException, org.apache.ambari.view.UnsupportedPropertyException;

    public java.util.Set<T> getResources(org.apache.ambari.view.ReadRequest request) throws org.apache.ambari.view.SystemException, org.apache.ambari.view.NoSuchResourceException, org.apache.ambari.view.UnsupportedPropertyException;

    public void createResource(java.lang.String resourceId, java.util.Map<java.lang.String, java.lang.Object> properties) throws org.apache.ambari.view.SystemException, org.apache.ambari.view.ResourceAlreadyExistsException, org.apache.ambari.view.NoSuchResourceException, org.apache.ambari.view.UnsupportedPropertyException;

    public boolean updateResource(java.lang.String resourceId, java.util.Map<java.lang.String, java.lang.Object> properties) throws org.apache.ambari.view.SystemException, org.apache.ambari.view.NoSuchResourceException, org.apache.ambari.view.UnsupportedPropertyException;

    public boolean deleteResource(java.lang.String resourceId) throws org.apache.ambari.view.SystemException, org.apache.ambari.view.NoSuchResourceException, org.apache.ambari.view.UnsupportedPropertyException;
}