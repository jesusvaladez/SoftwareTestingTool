package org.apache.ambari.view.pig.resources.scripts;
public class ScriptResourceProvider implements org.apache.ambari.view.ResourceProvider<org.apache.ambari.view.pig.resources.scripts.models.PigScript> {
    @com.google.inject.Inject
    org.apache.ambari.view.ViewContext context;

    protected org.apache.ambari.view.pig.resources.scripts.ScriptResourceManager resourceManager = null;

    protected static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.view.pig.resources.scripts.ScriptResourceProvider.class);

    protected synchronized org.apache.ambari.view.pig.resources.PersonalCRUDResourceManager<org.apache.ambari.view.pig.resources.scripts.models.PigScript> getResourceManager() {
        if (resourceManager == null) {
            resourceManager = new org.apache.ambari.view.pig.resources.scripts.ScriptResourceManager(context);
        }
        return resourceManager;
    }

    @java.lang.Override
    public org.apache.ambari.view.pig.resources.scripts.models.PigScript getResource(java.lang.String resourceId, java.util.Set<java.lang.String> properties) throws org.apache.ambari.view.SystemException, org.apache.ambari.view.NoSuchResourceException, org.apache.ambari.view.UnsupportedPropertyException {
        try {
            return getResourceManager().read(resourceId);
        } catch (org.apache.ambari.view.pig.persistence.utils.ItemNotFound itemNotFound) {
            throw new org.apache.ambari.view.NoSuchResourceException(resourceId);
        }
    }

    @java.lang.Override
    public java.util.Set<org.apache.ambari.view.pig.resources.scripts.models.PigScript> getResources(org.apache.ambari.view.ReadRequest readRequest) throws org.apache.ambari.view.SystemException, org.apache.ambari.view.NoSuchResourceException, org.apache.ambari.view.UnsupportedPropertyException {
        return new java.util.HashSet<org.apache.ambari.view.pig.resources.scripts.models.PigScript>(getResourceManager().readAll(new org.apache.ambari.view.pig.persistence.utils.OnlyOwnersFilteringStrategy(this.context.getUsername())));
    }

    @java.lang.Override
    public void createResource(java.lang.String s, java.util.Map<java.lang.String, java.lang.Object> stringObjectMap) throws org.apache.ambari.view.SystemException, org.apache.ambari.view.ResourceAlreadyExistsException, org.apache.ambari.view.NoSuchResourceException, org.apache.ambari.view.UnsupportedPropertyException {
        org.apache.ambari.view.pig.resources.scripts.models.PigScript script = null;
        try {
            script = new org.apache.ambari.view.pig.resources.scripts.models.PigScript(stringObjectMap);
        } catch (java.lang.reflect.InvocationTargetException e) {
            throw new org.apache.ambari.view.SystemException("error on creating resource", e);
        } catch (java.lang.IllegalAccessException e) {
            throw new org.apache.ambari.view.SystemException("error on creating resource", e);
        }
        getResourceManager().create(script);
    }

    @java.lang.Override
    public boolean updateResource(java.lang.String resourceId, java.util.Map<java.lang.String, java.lang.Object> stringObjectMap) throws org.apache.ambari.view.SystemException, org.apache.ambari.view.NoSuchResourceException, org.apache.ambari.view.UnsupportedPropertyException {
        org.apache.ambari.view.pig.resources.scripts.models.PigScript script = null;
        try {
            script = new org.apache.ambari.view.pig.resources.scripts.models.PigScript(stringObjectMap);
        } catch (java.lang.reflect.InvocationTargetException e) {
            throw new org.apache.ambari.view.SystemException("error on updating resource", e);
        } catch (java.lang.IllegalAccessException e) {
            throw new org.apache.ambari.view.SystemException("error on updating resource", e);
        }
        try {
            getResourceManager().update(script, resourceId);
        } catch (org.apache.ambari.view.pig.persistence.utils.ItemNotFound itemNotFound) {
            throw new org.apache.ambari.view.NoSuchResourceException(resourceId);
        }
        return true;
    }

    @java.lang.Override
    public boolean deleteResource(java.lang.String resourceId) throws org.apache.ambari.view.SystemException, org.apache.ambari.view.NoSuchResourceException, org.apache.ambari.view.UnsupportedPropertyException {
        try {
            getResourceManager().delete(resourceId);
        } catch (org.apache.ambari.view.pig.persistence.utils.ItemNotFound itemNotFound) {
            throw new org.apache.ambari.view.NoSuchResourceException(resourceId);
        }
        return true;
    }
}