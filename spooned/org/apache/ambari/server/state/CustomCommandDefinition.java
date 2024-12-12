package org.apache.ambari.server.state;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
@javax.xml.bind.annotation.XmlAccessorType(javax.xml.bind.annotation.XmlAccessType.FIELD)
public class CustomCommandDefinition {
    private java.lang.String name;

    private java.lang.String opsDisplayName;

    private org.apache.ambari.server.state.CommandScriptDefinition commandScript;

    private boolean background = false;

    private boolean hidden = false;

    public java.lang.String getName() {
        return name;
    }

    public boolean isBackground() {
        return background;
    }

    public boolean isHidden() {
        return hidden;
    }

    public void setHidden(boolean hidden) {
        this.hidden = hidden;
    }

    public java.lang.String getOpsDisplayName() {
        return opsDisplayName;
    }

    public org.apache.ambari.server.state.CommandScriptDefinition getCommandScript() {
        return commandScript;
    }

    @java.lang.Override
    public boolean equals(java.lang.Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof org.apache.ambari.server.state.CustomCommandDefinition)) {
            return false;
        }
        org.apache.ambari.server.state.CustomCommandDefinition rhs = ((org.apache.ambari.server.state.CustomCommandDefinition) (obj));
        return new org.apache.commons.lang.builder.EqualsBuilder().append(name, rhs.name).append(commandScript, rhs.commandScript).isEquals();
    }

    @java.lang.Override
    public int hashCode() {
        return new org.apache.commons.lang.builder.HashCodeBuilder(17, 31).append(name).append(commandScript).toHashCode();
    }
}