package org.apache.ambari.server.stack.upgrade;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;
@javax.xml.bind.annotation.XmlRootElement
@javax.xml.bind.annotation.XmlAccessorType(javax.xml.bind.annotation.XmlAccessType.FIELD)
@javax.xml.bind.annotation.XmlType(name = "add_component")
public class AddComponentTask extends org.apache.ambari.server.stack.upgrade.ServerSideActionTask {
    public static final java.lang.String PARAMETER_SERIALIZED_ADD_COMPONENT_TASK = "add-component-task";

    @com.google.gson.annotations.Expose
    @javax.xml.bind.annotation.XmlTransient
    private org.apache.ambari.server.stack.upgrade.Task.Type type = org.apache.ambari.server.stack.upgrade.Task.Type.ADD_COMPONENT;

    @com.google.gson.annotations.Expose
    @javax.xml.bind.annotation.XmlAttribute
    public org.apache.ambari.server.stack.upgrade.ExecuteHostType hosts = org.apache.ambari.server.stack.upgrade.ExecuteHostType.ANY;

    @com.google.gson.annotations.Expose
    @javax.xml.bind.annotation.XmlAttribute
    public java.lang.String service;

    @com.google.gson.annotations.Expose
    @javax.xml.bind.annotation.XmlAttribute
    public java.lang.String component;

    @com.google.gson.annotations.Expose
    @javax.xml.bind.annotation.XmlAttribute(name = "host-service")
    public java.lang.String hostService;

    @com.google.gson.annotations.Expose
    @javax.xml.bind.annotation.XmlAttribute(name = "host-component")
    public java.lang.String hostComponent;

    public AddComponentTask() {
        implClass = org.apache.ambari.server.serveraction.upgrades.AddComponentAction.class.getName();
    }

    @java.lang.Override
    public org.apache.ambari.server.stack.upgrade.Task.Type getType() {
        return type;
    }

    @java.lang.Override
    public org.apache.ambari.server.stack.upgrade.orchestrate.StageWrapper.Type getStageWrapperType() {
        return org.apache.ambari.server.stack.upgrade.orchestrate.StageWrapper.Type.SERVER_SIDE_ACTION;
    }

    @java.lang.Override
    public java.lang.String getActionVerb() {
        return "Adding";
    }

    public java.lang.String toJson() {
        return org.apache.ambari.server.stack.upgrade.Task.GSON.toJson(this);
    }

    public java.lang.String getServiceAndComponentAsString() {
        return (service + "/") + component;
    }
}