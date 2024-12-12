package org.apache.ambari.view.pig.resources.jobs;
public class JobResourceProvider implements org.apache.ambari.view.ResourceProvider<org.apache.ambari.view.pig.resources.jobs.models.PigJob> {
    @com.google.inject.Inject
    org.apache.ambari.view.ViewContext context;

    protected org.apache.ambari.view.pig.resources.jobs.JobResourceManager resourceManager = null;

    protected synchronized org.apache.ambari.view.pig.resources.jobs.JobResourceManager getResourceManager() {
        if (resourceManager == null) {
            resourceManager = new org.apache.ambari.view.pig.resources.jobs.JobResourceManager(context);
        }
        return resourceManager;
    }

    @java.lang.Override
    public org.apache.ambari.view.pig.resources.jobs.models.PigJob getResource(java.lang.String resourceId, java.util.Set<java.lang.String> strings) throws org.apache.ambari.view.SystemException, org.apache.ambari.view.NoSuchResourceException, org.apache.ambari.view.UnsupportedPropertyException {
        try {
            return getResourceManager().read(resourceId);
        } catch (org.apache.ambari.view.pig.persistence.utils.ItemNotFound itemNotFound) {
            throw new org.apache.ambari.view.NoSuchResourceException(resourceId);
        }
    }

    @java.lang.Override
    public java.util.Set<org.apache.ambari.view.pig.resources.jobs.models.PigJob> getResources(org.apache.ambari.view.ReadRequest readRequest) throws org.apache.ambari.view.SystemException, org.apache.ambari.view.NoSuchResourceException, org.apache.ambari.view.UnsupportedPropertyException {
        return new java.util.HashSet<org.apache.ambari.view.pig.resources.jobs.models.PigJob>(getResourceManager().readAll(new org.apache.ambari.view.pig.persistence.utils.OnlyOwnersFilteringStrategy(this.context.getUsername())));
    }

    @java.lang.Override
    public void createResource(java.lang.String s, java.util.Map<java.lang.String, java.lang.Object> stringObjectMap) throws org.apache.ambari.view.SystemException, org.apache.ambari.view.ResourceAlreadyExistsException, org.apache.ambari.view.NoSuchResourceException, org.apache.ambari.view.UnsupportedPropertyException {
        org.apache.ambari.view.pig.resources.jobs.models.PigJob job = null;
        try {
            job = new org.apache.ambari.view.pig.resources.jobs.models.PigJob(stringObjectMap);
        } catch (java.lang.reflect.InvocationTargetException e) {
            throw new org.apache.ambari.view.SystemException("error on creating resource", e);
        } catch (java.lang.IllegalAccessException e) {
            throw new org.apache.ambari.view.SystemException("error on creating resource", e);
        }
        getResourceManager().create(job);
    }

    @java.lang.Override
    public boolean updateResource(java.lang.String resourceId, java.util.Map<java.lang.String, java.lang.Object> stringObjectMap) throws org.apache.ambari.view.SystemException, org.apache.ambari.view.NoSuchResourceException, org.apache.ambari.view.UnsupportedPropertyException {
        org.apache.ambari.view.pig.resources.jobs.models.PigJob job = null;
        try {
            job = new org.apache.ambari.view.pig.resources.jobs.models.PigJob(stringObjectMap);
        } catch (java.lang.reflect.InvocationTargetException e) {
            throw new org.apache.ambari.view.SystemException("error on updating resource", e);
        } catch (java.lang.IllegalAccessException e) {
            throw new org.apache.ambari.view.SystemException("error on updating resource", e);
        }
        try {
            getResourceManager().update(job, resourceId);
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