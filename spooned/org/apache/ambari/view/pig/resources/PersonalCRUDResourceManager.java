package org.apache.ambari.view.pig.resources;
public class PersonalCRUDResourceManager<T extends org.apache.ambari.view.pig.persistence.utils.PersonalResource> extends org.apache.ambari.view.pig.resources.CRUDResourceManager<T> {
    protected org.apache.ambari.view.ViewContext context;

    protected boolean ignorePermissions = false;

    public PersonalCRUDResourceManager(java.lang.Class<T> responseClass, org.apache.ambari.view.ViewContext context) {
        super(responseClass);
        this.context = context;
    }

    @java.lang.Override
    public T update(T newObject, java.lang.String id) throws org.apache.ambari.view.pig.persistence.utils.ItemNotFound {
        T object = getPigStorage().load(this.resourceClass, java.lang.Integer.parseInt(id));
        if (object.getOwner().compareTo(this.context.getUsername()) != 0) {
            throw new org.apache.ambari.view.pig.persistence.utils.ItemNotFound();
        }
        newObject.setOwner(this.context.getUsername());
        return super.update(newObject, id);
    }

    @java.lang.Override
    public T save(T object) {
        if (!ignorePermissions) {
            object.setOwner(this.context.getUsername());
        }
        return super.save(object);
    }

    @java.lang.Override
    protected boolean checkPermissions(T object) {
        if (ignorePermissions)
            return true;

        return object.getOwner().compareTo(this.context.getUsername()) == 0;
    }

    @java.lang.Override
    public org.apache.ambari.view.ViewContext getContext() {
        return context;
    }

    public <T> T ignorePermissions(java.util.concurrent.Callable<T> actions) throws java.lang.Exception {
        ignorePermissions = true;
        T result;
        try {
            result = actions.call();
        } finally {
            ignorePermissions = false;
        }
        return result;
    }

    protected static java.lang.String getUsername(org.apache.ambari.view.ViewContext context) {
        java.lang.String userName = context.getProperties().get("dataworker.username");
        if (((userName == null) || (userName.compareTo("null") == 0)) || (userName.compareTo("") == 0))
            userName = context.getUsername();

        return userName;
    }

    protected java.lang.String getUsername() {
        return org.apache.ambari.view.pig.resources.PersonalCRUDResourceManager.getUsername(context);
    }
}