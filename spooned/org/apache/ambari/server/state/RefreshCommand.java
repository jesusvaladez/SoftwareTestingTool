package org.apache.ambari.server.state;
import javax.xml.bind.annotation.XmlAttribute;
public class RefreshCommand {
    @javax.xml.bind.annotation.XmlAttribute(name = "componentName", required = true)
    private java.lang.String componentName;

    @javax.xml.bind.annotation.XmlAttribute(name = "command", required = false)
    private java.lang.String command = org.apache.ambari.server.state.RefreshCommandConfiguration.RELOAD_CONFIGS;

    public RefreshCommand() {
    }

    public RefreshCommand(java.lang.String componentName, java.lang.String command) {
        this.componentName = componentName;
        this.command = command;
    }

    public java.lang.String getComponentName() {
        return componentName;
    }

    public java.lang.String getCommand() {
        return command;
    }
}