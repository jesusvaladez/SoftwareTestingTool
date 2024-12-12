package org.apache.ambari.server.state;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlElements;
import org.apache.commons.collections.CollectionUtils;
public class DependencyInfo {
    private java.lang.String name;

    private java.lang.String scope;

    private java.lang.String type = "inclusive";

    private java.lang.String serviceName;

    private java.lang.String componentName;

    @javax.xml.bind.annotation.XmlElement(name = "auto-deploy")
    private org.apache.ambari.server.state.AutoDeployInfo m_autoDeploy;

    private java.util.List<org.apache.ambari.server.state.DependencyConditionInfo> dependencyConditions = new java.util.ArrayList<>();

    public void setName(java.lang.String name) {
        if (!name.contains("/")) {
            throw new java.lang.IllegalArgumentException("Invalid dependency name specified in stack.  " + "Expected form is: serviceName/componentName");
        }
        this.name = name;
        int idx = name.indexOf('/');
        serviceName = name.substring(0, idx);
        componentName = name.substring(idx + 1);
    }

    public java.lang.String getName() {
        return name;
    }

    public void setScope(java.lang.String scope) {
        this.scope = scope;
    }

    public java.lang.String getScope() {
        return scope;
    }

    public void setAutoDeploy(org.apache.ambari.server.state.AutoDeployInfo autoDeploy) {
        m_autoDeploy = autoDeploy;
    }

    public org.apache.ambari.server.state.AutoDeployInfo getAutoDeploy() {
        return m_autoDeploy;
    }

    public java.lang.String getComponentName() {
        return componentName;
    }

    public java.lang.String getServiceName() {
        return serviceName;
    }

    @javax.xml.bind.annotation.XmlElementWrapper(name = "conditions")
    @javax.xml.bind.annotation.XmlElements(@javax.xml.bind.annotation.XmlElement(name = "condition"))
    public java.util.List<org.apache.ambari.server.state.DependencyConditionInfo> getDependencyConditions() {
        return dependencyConditions;
    }

    public void setDependencyConditions(java.util.List<org.apache.ambari.server.state.DependencyConditionInfo> dependencyConditions) {
        this.dependencyConditions = dependencyConditions;
    }

    public boolean hasDependencyConditions() {
        return !org.apache.commons.collections.CollectionUtils.isEmpty(dependencyConditions);
    }

    public java.lang.String getType() {
        return type;
    }

    public void setType(java.lang.String type) {
        this.type = type;
    }

    @java.lang.Override
    public java.lang.String toString() {
        java.lang.String autoDeployString = (m_autoDeploy == null) ? "false" : java.lang.String.valueOf(m_autoDeploy.isEnabled());
        return ((((((("DependencyInfo[name=" + getName()) + ", scope=") + getScope()) + ", type=") + getType()) + ", auto-deploy=") + autoDeployString) + "]";
    }

    @java.lang.Override
    public boolean equals(java.lang.Object o) {
        if (this == o)
            return true;

        if ((o == null) || (getClass() != o.getClass()))
            return false;

        org.apache.ambari.server.state.DependencyInfo that = ((org.apache.ambari.server.state.DependencyInfo) (o));
        if (componentName != null ? !componentName.equals(that.componentName) : that.componentName != null)
            return false;

        if (m_autoDeploy != null ? !m_autoDeploy.equals(that.m_autoDeploy) : that.m_autoDeploy != null)
            return false;

        if (name != null ? !name.equals(that.name) : that.name != null)
            return false;

        if (scope != null ? !scope.equals(that.scope) : that.scope != null)
            return false;

        if (type != null ? !type.equals(that.type) : that.type != null)
            return false;

        if (serviceName != null ? !serviceName.equals(that.serviceName) : that.serviceName != null)
            return false;

        return true;
    }

    @java.lang.Override
    public int hashCode() {
        int result = (name != null) ? name.hashCode() : 0;
        result = (31 * result) + (scope != null ? scope.hashCode() : 0);
        result = (31 * result) + (serviceName != null ? serviceName.hashCode() : 0);
        result = (31 * result) + (componentName != null ? componentName.hashCode() : 0);
        result = (31 * result) + (m_autoDeploy != null ? m_autoDeploy.hashCode() : 0);
        return result;
    }
}