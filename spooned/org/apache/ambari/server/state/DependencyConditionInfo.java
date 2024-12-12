package org.apache.ambari.server.state;
@javax.xml.bind.annotation.XmlTransient
@javax.xml.bind.annotation.XmlSeeAlso({ org.apache.ambari.server.state.PropertyExists.class, org.apache.ambari.server.state.PropertyValueEquals.class })
public interface DependencyConditionInfo {
    boolean isResolved(java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> properties);
}