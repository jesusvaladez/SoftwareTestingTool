package org.apache.ambari.server.state;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
@javax.xml.bind.annotation.XmlAccessorType(javax.xml.bind.annotation.XmlAccessType.FIELD)
public class CommandScriptDefinition {
    private java.lang.String script = null;

    private org.apache.ambari.server.state.CommandScriptDefinition.Type scriptType = org.apache.ambari.server.state.CommandScriptDefinition.Type.PYTHON;

    private int timeout = 0;

    public java.lang.String getScript() {
        return script;
    }

    public org.apache.ambari.server.state.CommandScriptDefinition.Type getScriptType() {
        return scriptType;
    }

    public int getTimeout() {
        return timeout;
    }

    public enum Type {

        PYTHON;}

    @java.lang.Override
    public boolean equals(java.lang.Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof org.apache.ambari.server.state.CommandScriptDefinition)) {
            return false;
        }
        org.apache.ambari.server.state.CommandScriptDefinition rhs = ((org.apache.ambari.server.state.CommandScriptDefinition) (obj));
        return new org.apache.commons.lang.builder.EqualsBuilder().append(script, rhs.script).append(scriptType, rhs.scriptType).append(timeout, rhs.timeout).isEquals();
    }

    @java.lang.Override
    public int hashCode() {
        return new org.apache.commons.lang.builder.HashCodeBuilder(17, 31).append(script).append(scriptType).append(timeout).toHashCode();
    }
}