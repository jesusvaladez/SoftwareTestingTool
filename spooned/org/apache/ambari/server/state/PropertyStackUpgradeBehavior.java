package org.apache.ambari.server.state;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
@javax.xml.bind.annotation.XmlAccessorType(javax.xml.bind.annotation.XmlAccessType.FIELD)
public class PropertyStackUpgradeBehavior {
    @javax.xml.bind.annotation.XmlAttribute(name = "merge", required = false)
    private boolean merge = true;

    public PropertyStackUpgradeBehavior() {
    }

    public PropertyStackUpgradeBehavior(boolean merge) {
        this.merge = merge;
    }

    public boolean isMerge() {
        return merge;
    }

    public void setMerge(boolean merge) {
        this.merge = merge;
    }
}