package org.apache.ambari.view.pig.resources.scripts.models;
import org.apache.commons.beanutils.BeanUtils;
public class PigScript implements java.io.Serializable , org.apache.ambari.view.pig.persistence.utils.PersonalResource {
    private java.lang.String id;

    private java.lang.String title = "";

    private java.lang.String pigScript = "";

    private java.lang.String pythonScript = "";

    private java.lang.String templetonArguments = "";

    private java.util.Date dateCreated;

    private java.lang.String owner = "";

    private boolean opened = false;

    public PigScript() {
    }

    public PigScript(java.util.Map<java.lang.String, java.lang.Object> stringObjectMap) throws java.lang.reflect.InvocationTargetException, java.lang.IllegalAccessException {
        org.apache.commons.beanutils.BeanUtils.populate(this, stringObjectMap);
    }

    @java.lang.Override
    public boolean equals(java.lang.Object o) {
        if (this == o)
            return true;

        if (!(o instanceof org.apache.ambari.view.pig.resources.scripts.models.PigScript))
            return false;

        org.apache.ambari.view.pig.resources.scripts.models.PigScript pigScript = ((org.apache.ambari.view.pig.resources.scripts.models.PigScript) (o));
        if (!id.equals(pigScript.id))
            return false;

        return true;
    }

    @java.lang.Override
    public int hashCode() {
        return id.hashCode();
    }

    public java.lang.String getId() {
        return id;
    }

    public void setId(java.lang.String id) {
        this.id = id;
    }

    public java.lang.String getTitle() {
        return title;
    }

    public void setTitle(java.lang.String title) {
        this.title = title;
    }

    public java.lang.String getPigScript() {
        return pigScript;
    }

    public void setPigScript(java.lang.String pigScript) {
        this.pigScript = pigScript;
    }

    public java.lang.String getTempletonArguments() {
        return templetonArguments;
    }

    public void setTempletonArguments(java.lang.String templetonArguments) {
        this.templetonArguments = templetonArguments;
    }

    public java.util.Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(java.util.Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public boolean isOpened() {
        return opened;
    }

    public void setOpened(boolean opened) {
        this.opened = opened;
    }

    public java.lang.String getOwner() {
        return owner;
    }

    public void setOwner(java.lang.String owner) {
        this.owner = owner;
    }

    public java.lang.String getPythonScript() {
        return pythonScript;
    }

    public void setPythonScript(java.lang.String pythonScript) {
        this.pythonScript = pythonScript;
    }
}