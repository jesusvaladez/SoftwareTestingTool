package org.apache.ambari.server.state;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
@javax.xml.bind.annotation.XmlAccessorType(javax.xml.bind.annotation.XmlAccessType.FIELD)
public class PropertyUpgradeBehavior {
    @javax.xml.bind.annotation.XmlAttribute(name = "add", required = false)
    private boolean add = true;

    @javax.xml.bind.annotation.XmlAttribute(name = "delete", required = false)
    private boolean delete = false;

    @javax.xml.bind.annotation.XmlAttribute(name = "update", required = false)
    private boolean update = false;

    public PropertyUpgradeBehavior() {
    }

    public PropertyUpgradeBehavior(boolean add, boolean delete, boolean update) {
        this.add = add;
        this.delete = delete;
        this.update = update;
    }

    public void setAdd(boolean add) {
        this.add = add;
    }

    public void setDelete(boolean delete) {
        this.delete = delete;
    }

    public void setUpdate(boolean update) {
        this.update = update;
    }

    public boolean isAdd() {
        return add;
    }

    public boolean isDelete() {
        return delete;
    }

    public boolean isUpdate() {
        return update;
    }

    @java.lang.Override
    public java.lang.String toString() {
        return (((((("PropertyUpgradeBehavior{" + "add=") + add) + ", delete=") + delete) + ", update=") + update) + '}';
    }
}