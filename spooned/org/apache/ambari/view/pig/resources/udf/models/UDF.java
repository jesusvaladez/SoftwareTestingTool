package org.apache.ambari.view.pig.resources.udf.models;
import org.apache.commons.beanutils.BeanUtils;
public class UDF implements java.io.Serializable , org.apache.ambari.view.pig.persistence.utils.PersonalResource {
    private java.lang.String id;

    private java.lang.String path;

    private java.lang.String name;

    private java.lang.String owner;

    public UDF() {
    }

    public UDF(java.util.Map<java.lang.String, java.lang.Object> stringObjectMap) throws java.lang.reflect.InvocationTargetException, java.lang.IllegalAccessException {
        org.apache.commons.beanutils.BeanUtils.populate(this, stringObjectMap);
    }

    @java.lang.Override
    public java.lang.String getId() {
        return id;
    }

    @java.lang.Override
    public void setId(java.lang.String id) {
        this.id = id;
    }

    @java.lang.Override
    public java.lang.String getOwner() {
        return owner;
    }

    @java.lang.Override
    public void setOwner(java.lang.String owner) {
        this.owner = owner;
    }

    public java.lang.String getPath() {
        return path;
    }

    public void setPath(java.lang.String path) {
        this.path = path;
    }

    public java.lang.String getName() {
        return name;
    }

    public void setName(java.lang.String name) {
        this.name = name;
    }
}