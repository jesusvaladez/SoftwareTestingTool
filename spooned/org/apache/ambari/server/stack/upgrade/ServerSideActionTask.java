package org.apache.ambari.server.stack.upgrade;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
public abstract class ServerSideActionTask extends org.apache.ambari.server.stack.upgrade.Task {
    @javax.xml.bind.annotation.XmlAttribute(name = "class")
    public java.lang.String implClass;

    @javax.xml.bind.annotation.XmlElement(name = "parameter")
    public java.util.List<org.apache.ambari.server.stack.upgrade.TaskParameter> parameters;

    public java.util.Map<java.lang.String, java.lang.String> getParameters() {
        java.util.Map<java.lang.String, java.lang.String> result = new java.util.HashMap<>();
        if (parameters != null) {
            for (org.apache.ambari.server.stack.upgrade.TaskParameter parameter : parameters) {
                result.put(parameter.name, parameter.value);
            }
        }
        return result;
    }

    public static final java.lang.String actionVerb = "Executing";

    public java.lang.String getImplementationClass() {
        return implClass;
    }

    public void setImplClass(java.lang.String implClass) {
        this.implClass = implClass;
    }

    @javax.xml.bind.annotation.XmlElement(name = "message")
    public java.util.List<java.lang.String> messages = new java.util.ArrayList<>();

    @java.lang.Override
    public java.lang.String getActionVerb() {
        return org.apache.ambari.server.stack.upgrade.ServerSideActionTask.actionVerb;
    }
}