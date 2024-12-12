package org.apache.ambari.server.stack.upgrade;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;
@javax.xml.bind.annotation.XmlRootElement
@javax.xml.bind.annotation.XmlAccessorType(javax.xml.bind.annotation.XmlAccessType.FIELD)
@javax.xml.bind.annotation.XmlType(name = "regenerate_keytabs")
public class RegenerateKeytabsTask extends org.apache.ambari.server.stack.upgrade.Task {
    @com.google.gson.annotations.Expose
    @javax.xml.bind.annotation.XmlTransient
    private org.apache.ambari.server.stack.upgrade.Task.Type type = org.apache.ambari.server.stack.upgrade.Task.Type.REGENERATE_KEYTABS;

    public RegenerateKeytabsTask() {
    }

    @java.lang.Override
    public org.apache.ambari.server.stack.upgrade.Task.Type getType() {
        return type;
    }

    @java.lang.Override
    public org.apache.ambari.server.stack.upgrade.orchestrate.StageWrapper.Type getStageWrapperType() {
        return org.apache.ambari.server.stack.upgrade.orchestrate.StageWrapper.Type.REGENERATE_KEYTABS;
    }

    @java.lang.Override
    public java.lang.String getActionVerb() {
        return "Regenerating Keytabs";
    }

    public java.lang.String toJson() {
        return org.apache.ambari.server.stack.upgrade.Task.GSON.toJson(this);
    }
}